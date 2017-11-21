package com.kk.utils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import cn.jpush.api.utils.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.PayloadBuilder;
import com.notnoop.apns.internal.ApnsFeedbackConnection;
/**
 * @version
 *          2015年8月28日
 *          Revision History: Date Reviser Description
 *          ----------------------------------------------------
 * @Description: 
 */
public class PushClientUtil {
	static String path = "";
	/**
	 * 保存离线的时长。秒为单位。最多支持10天（864000秒）。 0 表示该消息不保存离线。即：用户在线马上发出，当前不在线用户将不会收到此消息。
	 * 此参数不设置则表示默认，默认为保存1天的离线消息（86400秒)。
	 */
	private static int timeToLive = 60 * 60 * 24;
	private static ApnsService iphoneApnsService;
	static {
		File directory1 = new File("");// 参数为空
		try {
			path = directory1.getCanonicalPath();
			if (System.getProperty("os.name").startsWith("Windows")) { // 如果是windows操作系统
				path = path + "\\xyzersPush.p12";
			} else { // 如果非windows操作系统 linux
				path = path + "/xyzersPush.p12";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 测试
	public static void main(String[] args) {
		PushClientUtil.IOSPushInfoNotnoop(
						"3046a5b09ade4febf2530fe01fd5140efda7cb019dd8cbf3bb5696efc88de388",
				"", "{\"uniqueId\":\"865662000605007\",\"latitude\":\"39.98848343\",\"longitude\":\"116.34983826\",\"address\":\"北京市海淀区成府路20号院\",\"nickName\":\"111\",\"sendTime\":\"2015-11-20 14:50:04\",\"type\":\"18\",\"DevNam\":\"G3\",\"FirmVer\":\"G30100\",\"ProVer\":\"G30100\",\"MovFlag\":\"0\",\"BatPerc\":\"99\",\"SensFlag\":\"0\",\"GSMLevel\":\"25\",\"locationType\":\"GSM\"}", "测试报告,111,2015-11-20 14:50:04,北京市海淀区成府路20号院", "default");
	}
	public static String IOSPushInfoNotnoop(String deviceToken, String sendType,
			String message,String alert,String iosSoundType) {
		try {
			boolean soundTip=true;
			// 前面生成的用于JAVA后台连接APNS服务的*.p12文件位置
			String iphoneCertPath = "c:/certificate.p12";
			String iphoneCertPassword = "123456";//p12文件密码。
			int badge = 0;
			iphoneApnsService = APNS.newService()
					.withCert(iphoneCertPath, iphoneCertPassword)
					.withAppleDestination(false).build();
			//.withAppleDestination(false).build();判断是否是生产环境,false为开发环境,true为生产环境.
			if (iosSoundType=="no"||"no".equals(iosSoundType)) {
				soundTip=false;
			}
			pushIphoneNotification(deviceToken, alert, badge, soundTip, message);
			return "0";
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("{\"Result\":\"1\",\"ErrorMessage\":\"发送失败！\"}");
			return "1";
		}
	}
	/**
	 * 推送单个iphone消息
	 */
	public static void pushIphoneNotification(String deviceToken, String alert,
			int badge, boolean soundTip, String message) {
		try {
			if (StringUtils.isEmpty(deviceToken)) {
				throw new RuntimeException("deviceToken为空");
			}
			String sound = null;
			if (soundTip) {
				sound = "default";
			}
			sendIphoneNotification(alert, badge, sound, message, deviceToken);
		} catch (Exception e) {
			throw new RuntimeException("推送通知出错");
		}
	}
	/**
	 * 推送iphone消息
	 * @param payload
	 * @param tokenList
	 * @param password
	 * @param production
	 */
	private static void sendIphoneNotification(String alert, int badge,
			String sound, String message, String deviceToken) {
		try {
			PayloadBuilder payloadBuilder = APNS.newPayload();
			if (StringUtils.isNotEmpty(alert)) {
				payloadBuilder.alertBody(alert);
			}
			if (badge > 0) {
				payloadBuilder.badge(badge);
			}
			if (StringUtils.isNotEmpty(sound)) {
				payloadBuilder.sound(sound);
			}
			if (StringUtils.isNotEmpty(message)) {
				payloadBuilder.customField("message", message);
			}
			System.out.println("消息长度：   "+payloadBuilder.toString().getBytes().length);
			System.out.println("IOS信息：      "+payloadBuilder.toString());
			String msg=payloadBuilder.toString();
			System.out.println(msg.getBytes("utf-8").length);
			// 次数发送给单个设备，也可以同时发给多个设备
			//单一设备发送
			ApnsNotification a=iphoneApnsService.push(deviceToken, payloadBuilder.build());
			Collection<String> collection=new ArrayList<String>();
			//奎的设备ID
//			collection.add("3046a5b09ade4febf2530fe01fd5140efda7cb019dd8cbf3bb5696efc88de388");
//			collection.add("b13dd8c08d6149cfa91be2fc1269d4d8435fccef481bbb1a857c6eef3a5f8d2d");
			//陈芳的设备ID
//			collection.add("b13dd8c08d6149cfa91be2fc1269d4d8435fccef481bbb1a857c6eef3a5f8d2e");
			//多个设备发送
//			Collection<? extends ApnsNotification> list=iphoneApnsService.push(collection,payloadBuilder.build());
			Object object=JSONObject.toJSON(a);
			System.out.println(object.toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("推送消息出错"+e);
		}
		
	}
}
