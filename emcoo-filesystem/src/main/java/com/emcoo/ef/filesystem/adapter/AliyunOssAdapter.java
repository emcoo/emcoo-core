package com.emcoo.ef.filesystem.adapter;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.emcoo.ef.common.exception.RRException;
import com.emcoo.ef.filesystem.StorageResponse;
import com.emcoo.ef.filesystem.config.properties.AliyunOssStorageProperties;
import com.emcoo.ef.filesystem.utils.FilenameUtils;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * AliyunOss Storage Adapter
 *
 * @author mark
 */
@Getter
@Setter
public class AliyunOssAdapter extends AbstractAdapter {

	private final Logger LOGGER = LoggerFactory.getLogger(AliyunOssAdapter.class);

	@Autowired
	private AliyunOssStorageProperties aliyunOssStorageProperties;

	public OSS getOSSClient() {
		return new OSSClientBuilder().build(aliyunOssStorageProperties.getEndpoint(), aliyunOssStorageProperties.getAccessKeyId(), aliyunOssStorageProperties.getAccessKeySecret());
	}

	@Override
	public Boolean has(String path) {
		return null;
	}

	@Override
	public File read(String path) {
		return null;
	}

	@Override
	public StorageResponse update(String path, MultipartFile multipartFile) {
		StorageResponse storageResponse = new StorageResponse();
		return storageResponse;
	}

	@Override
	public StorageResponse write(String path, MultipartFile multipartFile) {
		try {
			return this.upload(path, multipartFile.getInputStream());
		} catch (FileNotFoundException e) {
			throw new RRException(e);
		} catch (Exception e) {
			throw new RRException(e);
		}
	}

	@Override
	public StorageResponse writeStream(String path, InputStream is) {
		return this.upload(path, is);
	}

	protected StorageResponse upload(String path, InputStream is) {
		String bucketName = aliyunOssStorageProperties.getBucketName();

		StorageResponse storageResponse = new StorageResponse();
		OSS ossClient = this.getOSSClient();
		try {
			// Object's Metadata
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(is.available());
			metadata.setCacheControl("no-cache");
			metadata.setHeader("Pragma", "no-cache");
			metadata.setContentEncoding("utf-8");
			metadata.setContentType(FilenameUtils.getMimeType(path));
			metadata.setContentDisposition("inline;filename=" + FilenameUtils.getBaseName(path));

			PutObjectResult putResult = ossClient.putObject(bucketName, path, is, metadata);

			storageResponse.setType("file");
			storageResponse.setMimetype(metadata.getContentType());
			storageResponse.setSize(metadata.getContentLength());
			storageResponse.setPath(path);
			return storageResponse;
		} catch (OSSException oe) {
			LOGGER.error("Upload failed: Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
			LOGGER.debug("Error Message: " + oe.getMessage());
			LOGGER.debug("Error Code:       " + oe.getErrorCode());
			LOGGER.debug("Request ID:      " + oe.getRequestId());
			LOGGER.debug("Host ID:           " + oe.getHostId());
		} catch (ClientException ce) {
			LOGGER.debug("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
			LOGGER.debug("Error Message: " + ce.getMessage());
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			ossClient.shutdown();
		}
		return storageResponse;
	}

	@Override
	public FileInputStream readStream(String path) {
		return null;
	}

	@Override
	public Boolean delete(String path) {
		return null;
	}

	@Override
	public Boolean deleteDir(String path) {
		return null;
	}

	@Override
	public Boolean createDir(String path) {
		return null;
	}
}

