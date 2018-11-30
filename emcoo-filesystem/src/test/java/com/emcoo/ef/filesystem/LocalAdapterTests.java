package com.emcoo.ef.filesystem;

import com.emcoo.ef.filesystem.adapter.LocalAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junitpioneer.jupiter.TempDirectory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;

@ExtendWith(TempDirectory.class)
public class LocalAdapterTests {

//	@Test
//	public void writeStream_localStream_Ok(@TempDirectory.TempDir Path tempDir) {
//		Path file = tempDir.resolve("test.txt");
//		InputStream isContent = new ByteArrayInputStream("test data".getBytes());
//
//		LocalAdapter localAdapter = new LocalAdapter();
//		StorageResponse storageResponse = localAdapter.writeStream(file.getFileName().toAbsolutePath().toString(), isContent);
//
//		Assertions.assertNotNull(storageResponse);
//	}

//	@Test
//	public void write_localFile_Ok(@TempDirectory.TempDir Path tempDir) {
//
//		// given
//		Path path = tempDir.resolve("test.txt");
//		File file = new File(path.toAbsolutePath().toString());
//
//		InputStream fileContent = new ByteArrayInputStream("test data".getBytes());
//
//		LocalAdapter localAdapter = new LocalAdapter();
//		StorageResponse storageResponse = localAdapter.write(path.toAbsolutePath().toString(), file);
//
//		Assertions.assertNotNull(storageResponse);
//	}

}
