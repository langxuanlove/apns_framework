package com.kk.utils.jpush;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

import com.kk.utils.PropertiesUtil;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.report.ReceivedsResult;
/**
 * 
 * @author  Jikey
 * 
 * @version 2015年12月07日
 *
 * @Description: 激光推送后台
 */
public class PushClientUtil {
	private static Logger logger = Logger.getLogger(PushClientUtil.class);
	private static final String appKey =PropertiesUtil.getKeyValue("APP_KEY"); // 必填
	private static final String masterSecret =PropertiesUtil.getKeyValue("MASTER_SECRET");//必填,每个应用都对应一个masterSecret
	/**
	 * 保存离线的时长。秒为单位。最多支持10天（864000秒）。 0 表示该消息不保存离线。即：用户在线马上发出，当前不在线用户将不会收到此消息。
	 * 此参数不设置则表示默认，默认为保存1天的离线消息（86400秒)。
	 */
	private static int timeToLive = 60 * 60 * 24;
	/**
	 * @param goType
	 * @throws Exception
	 * @Description: 推送消息
	 * @param
	 * @return
	 * @throws
	 */
	public static String send(String sendMethod, String sendType,
			String appKey, String masterSecret, String alert, String goType)
			throws Exception {
		JPushClient jpushClient = new JPushClient(masterSecret, appKey,
				timeToLive);
		try {
			PushResult result;
			if ("IOS".equals(sendType)) {
				logger.info("IOS推送消息内容:"+goType);
				PushPayload payload = buildPushObject_ios_tagAnd_alertWithExtrasAndMessage(
						alert, sendMethod, goType);
				result = jpushClient.sendPush(payload);
			} else {
				logger.info("Android推送消息内容:"+goType);
				PushPayload payload = buildPushObject_android_tag_alertWithTitle(
						alert, sendMethod, goType);
				result = jpushClient.sendPush(payload);
			}
			logger.info("PushClientUtil.send返回的结果:"+result);
			return result.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}

	}

	public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage(
			String alert, String tag, String goType) {
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.ios())
				.setAudience(Audience.alias(tag))
				.setNotification(
						Notification
								.newBuilder()
								.addPlatformNotification(
										IosNotification.newBuilder()
												.setAlert(alert).incrBadge(+1)
												.setSound("happy")
												.addExtra("goType", goType)
												.build()).build())
				.setMessage(Message.content(alert))
				.setOptions(Options.newBuilder().setApnsProduction(true)// true生产；false开发
						.build()).build();
	}

	public static PushPayload buildPushObject_android_tag_alertWithTitle(
			String alert, String tag, String goType) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("goType", goType);
		return PushPayload.newBuilder().setPlatform(Platform.android())
				.setAudience(Audience.alias(tag))
				.setNotification(Notification.android(alert, "", map)).build();
	}

	public static void result() {
		JPushClient jpushClient = new JPushClient(masterSecret, appKey);
		try {
			//返回的msgno
			ReceivedsResult result = jpushClient.getReportReceiveds("2971200492");
			logger.debug("Got result - " + result);
		} catch (APIConnectionException e) {
			e.printStackTrace();
			logger.error("Connection error, should retry later", e);
		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			e.printStackTrace();
			logger.error("Should review the error, and fix the request", e);
			logger.info("HTTP Status: " + e.getStatus());
			logger.info("Error Code: " + e.getErrorCode());
			logger.info("Error Message: " + e.getErrorMessage());
		}
	}
}
