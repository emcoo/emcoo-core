package com.emcoo.ef.notification.channel;

import com.emcoo.ef.common.exception.RRException;
import com.emcoo.ef.notification.SmsChannel;
import com.emcoo.ef.notification.config.properties.YunpainSmsProperties;
import com.emcoo.ef.notification.template.MobileMessageTemplate;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.api.SmsApi;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * Yunpian SMS
 *
 * @author mark
 */
public class YunpianSmsChannel implements SmsChannel {

	private final Logger LOGGER = LoggerFactory.getLogger(YunpianSmsChannel.class);

	YunpainSmsProperties yunpainSmsProperties;

	private String smsOverseaUrl = "https://us.yunpian.com";

	public YunpianSmsChannel(YunpainSmsProperties yunpainSmsProperties) {
		this.yunpainSmsProperties = yunpainSmsProperties;
	}

	/**
	 * 业务处理: 发送邮件
	 *
	 * @param mobileMessageTemplate
	 */
	@Override
	public boolean send(MobileMessageTemplate mobileMessageTemplate) {
		YunpianClient yunpianClient = new YunpianClient(yunpainSmsProperties.getAccessKey()).init();

		try {
			Map<String, String> param = yunpianClient.newParam(2);
			param.put(YunpianClient.MOBILE, mobileMessageTemplate.getMobile());
			param.put(YunpianClient.TEXT, mobileMessageTemplate.getContent());
			SmsApi smsClient = yunpianClient.sms();

			if (mobileMessageTemplate.isOversea()) {
				smsClient.host(this.smsOverseaUrl);
			}

			Result<SmsSingleSend> yunpainResult = smsClient.single_send(param);
			if (yunpainResult.getCode() != 0) {
				throw new RRException(String.format("Send sms failed. %s", yunpainResult.getMsg()));
			}
		} catch (Exception e) {
			throw new RRException(e);
		} finally {
			yunpianClient.close();
		}
		return true;
	}

}
