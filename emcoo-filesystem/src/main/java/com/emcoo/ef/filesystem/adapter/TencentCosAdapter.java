package com.emcoo.ef.filesystem.adapter;

import com.emcoo.ef.common.exception.RRException;
import com.emcoo.ef.filesystem.StorageResponse;
import com.emcoo.ef.filesystem.config.properties.TencentCosStorageProperties;
import com.emcoo.ef.filesystem.utils.FilenameUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Tencent COS Storage Adapter
 *
 * @author mark
 */
@Getter
@Setter
public class TencentCosAdapter extends AbstractAdapter {

	private final Logger LOGGER = LoggerFactory.getLogger(TencentCosAdapter.class);

	private TencentCosStorageProperties tencentCosStorageProperties;

	public TencentCosAdapter(TencentCosStorageProperties tencentCosStorageProperties) {
		this.tencentCosStorageProperties = tencentCosStorageProperties;
	}

	public COSClient getCosClient() {
		COSCredentials cred = new BasicCOSCredentials(tencentCosStorageProperties.getAccessKeyId(), tencentCosStorageProperties.getAccessKeySecret());
		ClientConfig clientConfig = new ClientConfig(new Region(tencentCosStorageProperties.getRegion()));
		return new COSClient(cred, clientConfig);
	}

	@Override
	public Boolean has(String path) {
		String bucketName = tencentCosStorageProperties.getBucketName();
		COSClient cosclient = this.getCosClient();

		return cosclient.doesObjectExist(bucketName, path);
	}

	@Override
	public File read(String path) {
		String bucketName = tencentCosStorageProperties.getBucketName();
		COSClient cosclient = this.getCosClient();

		try {
			File tempFile = File.createTempFile("cos-", ".tmp");
			tempFile.deleteOnExit();

			GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, path);
			ObjectMetadata downObjectMeta = cosclient.getObject(getObjectRequest, tempFile);

			return tempFile;
		} catch (CosServiceException e) {
			LOGGER.error("Read failed: Caught an CosServiceException, which means your request made it to COS, but was rejected with an error response for some reason.");
			LOGGER.debug("Error Code: " + e.getErrorCode());
			LOGGER.debug("Error Message: " + e.getMessage());
		} catch (CosClientException e) {
			LOGGER.error("Read failed: Caught an CosClientException, which means your request made it to COS, but was rejected with an error response for some reason.");
			LOGGER.debug("Error Message: " + e.getMessage());
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			cosclient.shutdown();
		}

		return null;
	}

	@Override
	public StorageResponse update(String path, MultipartFile multipartFile) {
		return this.write(path, multipartFile);
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
		String bucketName = tencentCosStorageProperties.getBucketName();

		COSClient cosclient = this.getCosClient();
		StorageResponse storageResponse = new StorageResponse();

		try {
			// Object's Metadata
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(is.available());
			metadata.setCacheControl("no-cache");
			metadata.setHeader("Pragma", "no-cache");
			metadata.setContentEncoding("utf-8");
			metadata.setContentType(FilenameUtils.getMimeType(path));
			metadata.setContentDisposition("inline;filename=" + FilenameUtils.getBaseName(path));

			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, is, metadata);

			putObjectRequest.setStorageClass(StorageClass.Standard);

			PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
			// putobjectResult会返回文件的etag
			String etag = putObjectResult.getETag();

			storageResponse.setType("file");
			storageResponse.setMimetype(metadata.getContentType());
			storageResponse.setSize(metadata.getContentLength());
			storageResponse.setPath(path);
			return storageResponse;
		} catch (CosServiceException e) {
			LOGGER.error("Upload failed: Caught an CosServiceException, which means your request made it to COS, but was rejected with an error response for some reason.");
			LOGGER.debug("Error Code: " + e.getErrorCode());
			LOGGER.debug("Error Message: " + e.getMessage());
		} catch (CosClientException e) {
			LOGGER.debug("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with COS, such as not being able to access the network.");
			LOGGER.debug("Error Message: " + e.getMessage());
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			// 关闭客户端
			cosclient.shutdown();
		}

		return storageResponse;
	}

	@Override
	public InputStream readStream(String path) {
		String bucketName = tencentCosStorageProperties.getBucketName();
		COSClient cosclient = this.getCosClient();

		try {
			GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, path);
			COSObject cosObject = cosclient.getObject(getObjectRequest);
			COSObjectInputStream cosObjectInput = cosObject.getObjectContent();

			return cosObjectInput;
		} catch (CosServiceException e) {
			LOGGER.error("Read failed: Caught an CosServiceException, which means your request made it to COS, but was rejected with an error response for some reason.");
			LOGGER.debug("Error Code: " + e.getErrorCode());
			LOGGER.debug("Error Message: " + e.getMessage());
		} catch (CosClientException e) {
			LOGGER.error("Read failed: Caught an CosClientException, which means your request made it to COS, but was rejected with an error response for some reason.");
			LOGGER.debug("Error Message: " + e.getMessage());
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			cosclient.shutdown();
		}

		return null;
	}

	@Override
	public Boolean delete(String path) {
		String bucketName = tencentCosStorageProperties.getBucketName();
		COSClient cosclient = this.getCosClient();

		try {
			cosclient.deleteObject(bucketName, path);

		} catch (CosServiceException e) {
			LOGGER.error("Upload failed: Caught an CosServiceException, which means your request made it to COS, but was rejected with an error response for some reason.");
			LOGGER.debug("Error Code: " + e.getErrorCode());
			LOGGER.debug("Error Message: " + e.getMessage());
		} catch (CosClientException e) {
			LOGGER.error("Upload failed: Caught an CosClientException, which means your request made it to COS, but was rejected with an error response for some reason.");
			LOGGER.debug("Error Message: " + e.getMessage());
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			cosclient.shutdown();
		}

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

