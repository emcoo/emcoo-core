package com.emcoo.ef.filesystem;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

/**
 * Storage Response
 *
 * @author mark
 */
@Getter
@Setter
public class StorageResponse {

	// filename with ext
	String name;

	// without ext
	String baseName;

	// ext, e.g.: jpg, png
	String extension;

	String originalFilename;

	// file / dir
	String type;

	// fullpath
	String path;

	String fullPath;

	Long size;

	String visibility;

	String mimetype;

	InputStream stream;

	Object extra;
}
