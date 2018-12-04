package com.emcoo.ef.notification.template;

import lombok.Data;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * 手机短信模板
 *
 * @author mark
 */
@Data
public class MobileMessageTemplate extends BaseMessageTemplate implements Serializable {

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 组装后的模板内容JSON字符串
	 */
	private String content;

	/**
	 * 是否发送至全球
	 */
	private boolean oversea;

	public MobileMessageTemplate() {
	}

	public MobileMessageTemplate(MobileMessageTemplate original) {
		Assert.notNull(original, "'original' message argument must not be null");
		this.mobile = original.getMobile();
		this.content = original.getContent();
		this.oversea = original.oversea;
	}
}
