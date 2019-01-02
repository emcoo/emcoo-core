package com.emcoo.ef.filesystem.config.autoconfigure;

import com.emcoo.ef.filesystem.DefaultFilesystem;
import com.emcoo.ef.filesystem.Filesystem;
import com.emcoo.ef.filesystem.adapter.AliyunOssAdapter;
import com.emcoo.ef.filesystem.adapter.LocalAdapter;
import com.emcoo.ef.filesystem.adapter.TencentCosAdapter;
import com.emcoo.ef.filesystem.config.properties.AliyunOssStorageProperties;
import com.emcoo.ef.filesystem.config.properties.StorageProperties;
import com.emcoo.ef.filesystem.config.properties.TencentCosStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Storage Auto Configuration
 *
 * @author mark
 */
@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageAutoConfiguration {

	@Autowired
	private StorageProperties storageProperties;

	@Autowired
	private TencentCosStorageProperties tencentCosStorageProperties;

	@Bean
	@ConditionalOnMissingBean
	public StorageProperties storageProperties() {
		return new StorageProperties();
	}

	@Bean
	@ConditionalOnMissingBean
	public AliyunOssStorageProperties aliyunOssStorageProperties() {
		return new AliyunOssStorageProperties();
	}

	@Bean
	@ConditionalOnMissingBean
	public TencentCosStorageProperties tencentCosStorageProperties() {
		return new TencentCosStorageProperties();
	}

	@Bean
	@Primary
	public Filesystem localFilesystem(StorageProperties storageProperties) {
		return new DefaultFilesystem(new LocalAdapter(storageProperties));
	}

	@Bean(name = "aliyunOss")
	public Filesystem aliyunOssFilesystem() {
		return new DefaultFilesystem(new AliyunOssAdapter());
	}

	@Bean(name = "tencentCos")
	public Filesystem tencentCosFilesystem() {
		return new DefaultFilesystem(new TencentCosAdapter(tencentCosStorageProperties));
	}


}

