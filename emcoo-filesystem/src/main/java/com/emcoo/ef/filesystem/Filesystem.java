package com.emcoo.ef.filesystem;

import com.emcoo.ef.filesystem.resize.ResizeTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * Filesystem Interface
 *
 * @author mark
 */
public interface Filesystem {

	Boolean exists(String path);

	File get(String path);

	void download(HttpServletResponse response, String path);

	void resizeAndDownload(HttpServletResponse response, String path, ResizeTemplate resizeTemplate);

	StorageResponse put(String path, MultipartFile multipartFile);

	StorageResponse putFile(String path, MultipartFile multipartFile);

	StorageResponse putFileAs(String path, MultipartFile multipartFile, String fileName);

	Boolean delete(String path);

	Boolean deleteDirectory(String path);

	Boolean makeDirectory(String path);

}
