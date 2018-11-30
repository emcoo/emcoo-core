package com.emcoo.ef.filesystem.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Aliyun Oss Properties
 *
 * @author mark
 */
@Data
@ConfigurationProperties(prefix = "emcoo.storage.aliyun")
public class AliyunOssStorageProperties {

	private String endpoint;

	private String accessKeyId;

	private String accessKeySecret;

	private String bucketName;

}
