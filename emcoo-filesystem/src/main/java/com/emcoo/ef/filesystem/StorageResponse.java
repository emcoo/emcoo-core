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

	String fileName;

	String originalFilename;

	String type;

	String path;

	Long size;

	String visibility;

	String mimetype;

	InputStream stream;

	Object extra;
}
