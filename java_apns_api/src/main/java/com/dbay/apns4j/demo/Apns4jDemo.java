package com.dbay.apns4j.demo;

import java.io.InputStream;
import java.util.List;

import com.dbay.apns4j.IApnsService;
import com.dbay.apns4j.impl.ApnsServiceImpl;
import com.dbay.apns4j.model.ApnsConfig;
import com.dbay.apns4j.model.Feedback;
import com.dbay.apns4j.model.Payload;

/**
 * @author RamosLi
 *
 */
public class Apns4jDemo {
	private static IApnsService apnsService;
	
	private static IApnsService getApnsService() {
		if (apnsService == null) {
			ApnsConfig config = new ApnsConfig();
			InputStream is = Apns4jDemo.class.getClassLoader().getResourceAsStream("TestJavaPush_Server_Development.p12");
			config.setKeyStore(is);
			//生产环境false,开发环境true
			config.setDevEnv(true);
			config.setPassword("123456");
			config.setPoolSize(3);
			// 假如需要在同个java进程里给不同APP发送通知，那就需要设置为不同的name
//			config.setName("welove1");
			apnsService = ApnsServiceImpl.createInstance(config);
		}
		return apnsService;
	}
	
	public static void main(String[] args) {
		IApnsService service = getApnsService();
		// send notification
		String token = "3046a5b09ade4febf2530fe01fd5140efda7cb019dd8cbf3bb5696efc88de388";
		for (int i = 0; i < 6; i++) {
			if(i==3||i==5){
				token="3046a5b09ade4febf2530fe01fd5140efda7cb019dd8cbf3bb5696efc88de312";
			}else{
				token="3046a5b09ade4febf2530fe01fd5140efda7cb019dd8cbf3bb5696efc88de388";
			}
			Payload payload2 = new Payload();
			payload2.setBadge(1);
			payload2.setAlert("大奎,你好帅！"+i+"【苹果公司|Apple Inc.】");
			payload2.setSound(null);
			service.sendNotification(token, payload2);
			// get feedback
			List<Feedback> list = service.getFeedbacks();
			if (list != null && list.size() > 0) {
				for (Feedback feedback : list) {
					System.out.println(feedback.getDate() + " " + feedback.getToken());
				}
			}
			System.out.println("测试第几条苹果开始拒绝："+i);
		}
//		b13dd8c08d6149cfa91be2fc1269d4d8435fccef481bbb1a857c6eef3a5f8d2e
//      3046a5b09ade4febf2530fe01fd5140efda7cb019dd8cbf3bb5696efc88de388
//		Payload payload = new Payload();
//		payload.setAlert("How are you?");
		// If this property is absent, the badge is not changed. To remove the badge, set the value of this property to 0
//		payload.setBadge(1);
//		 set sound null, the music won't be played
//		payload.setSound(null);
//		payload.setSound("msg.mp3");
//		payload.addParam("uid", 123456);
//		payload.addParam("type", 12);
//		service.sendNotification(token, payload);
		// payload, use loc string
//		Payload payload2 = new Payload();
//		payload2.setBadge(1);
//		payload2.setAlert("大奎,你好帅！【苹果公司|Apple Inc.】");
//		payload2.setSound(null);
//		service.sendNotification(token, payload2);
//		// get feedback
//		List<Feedback> list = service.getFeedbacks();
//		if (list != null && list.size() > 0) {
//			for (Feedback feedback : list) {
//				System.out.println(feedback.getDate() + " " + feedback.getToken());
//			}
//		}
		
//		try {
//			Thread.sleep(5000);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		// It's a good habit to shutdown what you never use
//		service.shutdown();
		
//		System.exit(0);
	}
}
