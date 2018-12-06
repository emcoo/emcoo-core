package com.emcoo.ef.filesystem.adapter;

import com.emcoo.ef.common.exception.RRException;
import com.emcoo.ef.filesystem.StorageResponse;

import com.emcoo.ef.filesystem.config.properties.StorageProperties;
import com.emcoo.ef.filesystem.utils.FilenameUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;

/**
 * Local Adapter
 *
 * @author mark
 */
public class LocalAdapter extends AbstractAdapter implements CanOverwriteFiles {

	private final Logger LOGGER = LoggerFactory.getLogger(LocalAdapter.class);

	StorageProperties storageProperties;

	public LocalAdapter(StorageProperties storageProperties) {
		this.storageProperties = storageProperties;
	}

	@Override
	public Boolean has(String path) {
		path = Paths.get(storageProperties.getUploadPath(), path).toString();

		return Paths.get(path).toFile().exists();
	}

	@Override
	public File read(String path) {
		path = Paths.get(storageProperties.getUploadPath(), path).toString();
		return new File(path);
	}

	@Override
	public FileInputStream readStream(String path) {
		path = Paths.get(storageProperties.getUploadPath(), path).toString();
		try {
			return new FileInputStream(path);
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	@Override
	public StorageResponse update(String path, MultipartFile multipartFile) {
		StorageResponse storageResponse = new StorageResponse();
		return storageResponse;
	}

	@Override
	public StorageResponse write(String path, MultipartFile multipartFile) {
		try {
			return this.upload(path, multipartFile.getInputStream());
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found " + path, e);
		} catch (Exception e) {
			throw new RuntimeException("Could not write file " + path, e);
		}
	}

	@Override
	public StorageResponse writeStream(String path, InputStream is) {
		return this.upload(path, is);
	}

	@Override
	public Boolean delete(String path) {
		path = Paths.get(storageProperties.getUploadPath(), path).toString();
		Path targetLocation = Paths.get(path);

		try {
			Files.delete(targetLocation);
			return true;
		} catch (NoSuchFileException e) {
			LOGGER.info("no such file or directory " + path, e);
		} catch (DirectoryNotEmptyException e) {
			LOGGER.info("no empty " + path, e);
		} catch (IOException e) {
			LOGGER.info("Could not delete file or directory" + path, e);
		}

		return false;
	}

	@Override
	public Boolean deleteDir(String path) {
		path = Paths.get(storageProperties.getUploadPath(), path).toString();
		try {
			File directory = new File(path);
			if (!directory.isDirectory()) {
				return false;
			}

			FileUtils.deleteDirectory(directory);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public Boolean createDir(String path) {
		try {
			this.upload(path + "", null);
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	protected StorageResponse upload(String path, InputStream is) {
		String fullPath = Paths.get(storageProperties.getUploadPath(), path).toString();

		StorageResponse storageResponse = new StorageResponse();

		String baseName = FilenameUtils.getBaseName(path);
		String name = FilenameUtils.getName(path);
		String extension = FilenameUtils.getExtension(path);

		try {
			Path targetLocation = Paths.get(fullPath);
			// Create dir
			Files.createDirectories(targetLocation.getParent());

			storageResponse.setPath(path);
			storageResponse.setFullPath(fullPath);
			// Copy file to the target location (Replacing existing file with the same name)
			if (is != null) {
				Files.copy(is, targetLocation, StandardCopyOption.REPLACE_EXISTING);
				storageResponse.setType("file");
				storageResponse.setName(name);
				storageResponse.setBaseName(baseName);
				storageResponse.setExtension(extension);
				storageResponse.setMimetype(FilenameUtils.getMimeType(path));
				storageResponse.setSize(Long.valueOf(is.available()));
			}
		} catch (NoSuchFileException e) {
			throw new RuntimeException("Invalid path " + name, e);
		} catch (IOException e) {
			throw new RuntimeException("Could not store file " + name, e);
		}
		return storageResponse;
	}
}

