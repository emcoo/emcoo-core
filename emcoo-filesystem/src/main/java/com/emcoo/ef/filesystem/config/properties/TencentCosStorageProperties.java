package com.emcoo.ef.filesystem.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Tencent Cos Properties
 *
 * @author mark
 */
@Data
@ConfigurationProperties(prefix = "emcoo.storage.tencent")
public class TencentCosStorageProperties {

	private String accessKeyId;

	private String accessKeySecret;

	private String bucketName;

	private String region;

}
