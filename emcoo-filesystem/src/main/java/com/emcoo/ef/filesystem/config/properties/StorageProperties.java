package com.emcoo.ef.filesystem.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Storage Properties
 *
 * @author mark
 */
@Data
@ConfigurationProperties(prefix = "emcoo.storage")
public class StorageProperties {

	private String uploadPath;

}