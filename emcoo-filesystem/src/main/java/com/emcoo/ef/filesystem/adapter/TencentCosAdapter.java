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
import com.qcloud.cos.exception.MultiObjectDeleteException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.model.DeleteObjectsRequest;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

		String baseName = FilenameUtils.getBaseName(path);
		String name = FilenameUtils.getName(path);
		String extension = FilenameUtils.getExtension(path);

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
			storageResponse.setName(name);
			storageResponse.setBaseName(baseName);
			storageResponse.setExtension(extension);
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
		String bucketName = tencentCosStorageProperties.getBucketName();
		COSClient cosclient = this.getCosClient();

		ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
		// 设置bucket名称
		listObjectsRequest.setBucketName(bucketName);
		// prefix表示列出的object的key以prefix开始
		listObjectsRequest.setPrefix(path);
		// deliter表示分隔符, 设置为/表示列出当前目录下的object, 设置为空表示列出所有的object
		listObjectsRequest.setDelimiter("");
		// 设置最大遍历出多少个对象, 一次listobject最大支持1000
		listObjectsRequest.setMaxKeys(1000);
		ObjectListing objectListing = null;
		do {
			try {
				objectListing = cosclient.listObjects(listObjectsRequest);

				// common prefix表示表示被delimiter截断的路径, 如delimter设置为/, common prefix则表示所有子目录的路径
				List<String> commonPrefixs = objectListing.getCommonPrefixes();

				DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);

				ArrayList<DeleteObjectsRequest.KeyVersion> keyList = new ArrayList<>();
				// 列出的object列表
				List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
				for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
					keyList.add(new DeleteObjectsRequest.KeyVersion(cosObjectSummary.getKey()));
				}
				deleteObjectsRequest.setKeys(keyList);
				// 批量删除文件
				DeleteObjectsResult deleteObjectsResult = cosclient.deleteObjects(deleteObjectsRequest);
				List<DeleteObjectsResult.DeletedObject> deleteObjectResultArray = deleteObjectsResult.getDeletedObjects();
			} catch (MultiObjectDeleteException mde) { // 部分产出成功部分失败
				List<DeleteObjectsResult.DeletedObject> deleteObjects = mde.getDeletedObjects();
				List<MultiObjectDeleteException.DeleteError> deleteErrors = mde.getErrors();
				LOGGER.error("Delete failed: Caught an CosServiceException, which means your request made it to COS, but was rejected with an error response for some reason.");
				LOGGER.debug("Error Code: " + mde.getErrorCode());
				LOGGER.debug("Error Message: " + mde.getMessage());
				return false;
			} catch (CosServiceException e) {
				LOGGER.error("Delete failed: Caught an CosServiceException, which means your request made it to COS, but was rejected with an error response for some reason.");
				LOGGER.debug("Error Code: " + e.getErrorCode());
				LOGGER.debug("Error Message: " + e.getMessage());
				return false;
			} catch (CosClientException e) {
				LOGGER.error("Delete failed: Caught an CosClientException, which means your request made it to COS, but was rejected with an error response for some reason.");
				LOGGER.debug("Error Message: " + e.getMessage());
				return false;
			}

			String nextMarker = objectListing.getNextMarker();
			listObjectsRequest.setMarker(nextMarker);
		} while (objectListing.isTruncated());

		cosclient.shutdown();
		return true;
	}

	@Override
	public Boolean createDir(String path) {
		return true;
	}
}

