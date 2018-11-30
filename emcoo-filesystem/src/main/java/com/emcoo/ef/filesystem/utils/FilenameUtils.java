package com.emcoo.ef.filesystem.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Filename Utils
 *
 * @author mark
 */
public class FilenameUtils extends org.apache.commons.io.FilenameUtils {

	public static String randomFilename(String path) {
		String fileName = FilenameUtils.getBaseName(path);
		String type = org.apache.commons.io.FilenameUtils.getExtension(path);

		String random = UUID.randomUUID().toString();

		return fileName.replace(fileName, random + "." + type);
	}

	public static String getMimeType(File file) {
		try {
			Path path = file.toPath();
			return Files.probeContentType(path);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getMimeType(String filename) {
		File file = new File(filename);
		return FilenameUtils.getMimeType(file);
	}

}
