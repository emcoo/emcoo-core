package com.emcoo.ef.filesystem;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Adapter Interface
 *
 * @author mark
 */
public interface Adapter {

	Boolean has(String path);

	File read(String path);

	InputStream readStream(String path);

	StorageResponse write(String path, MultipartFile multipartFile);

	StorageResponse writeStream(String path, InputStream is);

	StorageResponse update(String path, MultipartFile multipartFile);

	Boolean delete(String path);

	Boolean deleteDir(String path);

	Boolean createDir(String path);

}