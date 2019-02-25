package com.emcoo.ef.filesystem.utils;

import java.io.File;
import java.net.URLConnection;
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
		return URLConnection.guessContentTypeFromName(file.getName());
	}

	public static String getMimeType(String filename) {
		File file = new File(filename);
		return getMimeType(file);
	}

}
