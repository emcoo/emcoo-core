package com.emcoo.ef.filesystem;

import com.emcoo.ef.common.exception.NotFoundException;
import com.emcoo.ef.common.exception.ResourceNotFoundException;
import com.emcoo.ef.filesystem.adapter.CanOverwriteFiles;
import com.emcoo.ef.filesystem.resize.ResizeTemplate;
import com.emcoo.ef.filesystem.utils.FilenameUtils;
import lombok.Getter;
import lombok.Setter;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Default File System Impl
 *
 * @author mark
 */
@Getter
@Setter
public class DefaultFilesystem implements Filesystem {

	protected Adapter adapter;
	private Integer maxWidth = 4000;
	private Integer maxHeight = 4000;

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
	public void download(HttpServletResponse response, String path) {
		path = FilenameUtils.normalize(path);

		FileInputStream fileInputStream = this.getAdapter().readStream(path);
		if (fileInputStream == null) {
			throw new ResourceNotFoundException();
		}

		String contentType = FilenameUtils.getMimeType(path);
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		try {
			OutputStream outputStream = response.getOutputStream();

			response.setContentType(contentType);
			IOUtils.copy(fileInputStream, outputStream);

			fileInputStream.close();
		} catch (IOException e) {
			throw new ResourceNotFoundException();
		}
	}

	@Override
	public void resizeAndDownload(HttpServletResponse response, String path, ResizeTemplate resizeTemplate) {
		path = FilenameUtils.normalize(path);

		// only support image format
		String[] ImageFormats = ImageIO.getWriterFormatNames();
		if (!Arrays.stream(ImageFormats).anyMatch(FilenameUtils.getExtension(path)::equals)) {
			throw new NotFoundException();
		}

		FileInputStream fileInputStream = this.getAdapter().readStream(path);
		if (fileInputStream == null) {
			throw new NotFoundException();
		}

		if (resizeTemplate.getWidth() >= this.getMaxWidth() || resizeTemplate.getHeight() >= this.getMaxHeight()) {
			this.download(response, path);
			return;
		}

		// check invalid resize param
		if (resizeTemplate.getWidth() == 0 && resizeTemplate.getHeight() == 0) {
			this.download(response, path);
			return;
		}

		try {
			Thumbnails.Builder thumb = Thumbnails.of(fileInputStream);

			try {
				if (resizeTemplate.getWidth() != 0 && resizeTemplate.getHeight() != 0) {
					thumb.size(resizeTemplate.getWidth(), resizeTemplate.getHeight());
				} else if (resizeTemplate.getWidth() != 0) {
					thumb.width(resizeTemplate.getWidth());
				} else if (resizeTemplate.getHeight() != 0) {
					thumb.height(resizeTemplate.getHeight());
				}
				if (resizeTemplate.getQuality() != 0) {
					thumb.outputQuality(resizeTemplate.getQuality());
				}
				thumb.toOutputStream(response.getOutputStream());
			} catch (IllegalArgumentException e) {
				this.download(response, path);
				return;
			}
		} catch (IOException e) {
			throw new NotFoundException();
		}
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

