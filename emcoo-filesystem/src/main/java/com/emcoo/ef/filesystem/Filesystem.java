package com.emcoo.ef.filesystem;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Filesystem Interface
 *
 * @author mark
 */
public interface Filesystem {

	Boolean exists(String path);

	File get(String path);

	ResponseEntity download(String path);

	StorageResponse put(String path, MultipartFile multipartFile);

	StorageResponse putFile(String path, MultipartFile multipartFile);

	StorageResponse putFileAs(String path, MultipartFile multipartFile, String fileName);

	Boolean delete(String path);

	Boolean deleteDirectory(String path);

	Boolean makeDirectory(String path);

}
