package com.emcoo.ef.filesystem;

import com.emcoo.ef.filesystem.adapter.CanOverwriteFiles;
import com.emcoo.ef.filesystem.utils.FilenameUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;

/**
 * Default File System Impl
 *
 * @author mark
 */
@Getter
@Setter
public class DefaultFilesystem implements Filesystem {

	protected Adapter adapter;

	public DefaultFilesystem(Adapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public Boolean exists(String path) {
		path = FilenameUtils.normalize(path);

		if (path.isEmpty()) {
			return false;
		}

		return this.getAdapter().has(path);
	}

	@Override
	public File get(String path) {
		path = FilenameUtils.normalize(path);

		return this.getAdapter().read(path);
	}

	@Override
	public ResponseEntity download(String path) {
		path = FilenameUtils.normalize(path);


		FileInputStream fileInputStream = this.getAdapter().readStream(path);
		if (fileInputStream == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		InputStreamResource resource = new InputStreamResource(fileInputStream);

		// Try to determine file's content type
		String contentType = FilenameUtils.getMimeType(path);
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.body(resource);
	}

	@Override
	public StorageResponse put(String path, MultipartFile multipartFile) {
		path = FilenameUtils.normalize(path);

		if (!(this.getAdapter() instanceof CanOverwriteFiles)) {
			return this.getAdapter().update(path, multipartFile);
		}
		return this.getAdapter().write(path, multipartFile);
	}

	@Override
	public StorageResponse putFile(String path, MultipartFile multipartFile) {
		String fileName = FilenameUtils.randomFilename(multipartFile.getOriginalFilename());

		return this.putFileAs(FilenameUtils.getFullPath(path), multipartFile, fileName);
	}

	@Override
	public StorageResponse putFileAs(String path, MultipartFile multipartFile, String fileName) {
		String fullPath = Paths.get(FilenameUtils.getFullPath(path), fileName).toString();

		return this.put(fullPath, multipartFile);
	}

	@Override
	public Boolean delete(String path) {
		path = FilenameUtils.normalize(path);

		return this.getAdapter().delete(path);
	}

	@Override
	public Boolean deleteDirectory(String path) {
		path = FilenameUtils.normalize(path);

		return this.getAdapter().deleteDir(path);
	}

	@Override
	public Boolean makeDirectory(String path) {
		path = FilenameUtils.normalize(path);

		return this.getAdapter().createDir(path);
	}
}

