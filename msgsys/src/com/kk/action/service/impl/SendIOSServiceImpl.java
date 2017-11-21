package com.kk.action.service.impl;

import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kk.action.model.MessageModel;
import com.kk.action.service.ISendIOSService;
import com.kk.apns.IApnsService;
import com.kk.apns.impl.ApnsServiceImpl;
import com.kk.apns.model.ApnsConfig;
import com.kk.apns.model.Payload;
import com.kk.utils.PropertiesUtil;

@Service(value="sendIOSService")
public class SendIOSServiceImpl implements ISendIOSService{
	private Logger logger = Logger.getLogger(SendIOSServiceImpl.class);
	private static IApnsService apnsService;
	private static String env = PropertiesUtil.getKeyValue("IOS_DEV_ENV");
	private static String file = PropertiesUtil.getKeyValue("FILE_IOS");
	private static IApnsService getApnsService() {
		if (apnsService == null) {
			ApnsConfig config = new ApnsConfig();
			InputStream is = SendIOSServiceImpl.class.getClassLoader().getResourceAsStream(file);
			config.setKeyStore(is);
			//生产环境false,开发环境true
			if("Production".equals(env)){
				config.setDevEnv(false);
			}else{
				config.setDevEnv(true);
			}
			config.setPassword("123456");
			config.setPoolSize(3);
			// 假如需要在同个java进程里给不同APP发送通知，那就需要设置为不同的name
//			config.setName("welove1");
			apnsService = ApnsServiceImpl.createInstance(config);
		}
		return apnsService;
	}
	@Override
	public void sendMessageForIOS(List<MessageModel> list)throws Exception {
		System.out.println("发送的数据:"+JSON.toJSON(list));
		//http://blog.jpush.cn/ios_apns_badge_plus/
		//为了解决此问题为每个客户端保存其特定的 badge值.客户端有变更时,
		//把 badge值更新到服务器.有新的推送时,把这个值 +1 推送下来(默认是 +1).这样就符合实际的使用场景了.
		IApnsService service = getApnsService();
		for (int i = 0; i < list.size(); i++) {
			Payload payload = new Payload();
			payload.setBadge(1);
			payload.setAlert(list.get(i).getMessage());
			payload.setSound(null);
			service.sendNotification(list.get(i).getTokenId(), payload);
		}
	}
	public void sendMessageForIOS(String alert,String sound, String message, String deviceToken)throws Exception {
		//http://blog.jpush.cn/ios_apns_badge_plus/
		//为了解决此问题为每个客户端保存其特定的 badge值.客户端有变更时,
		//把 badge值更新到服务器.有新的推送时,把这个值 +1 推送下来(默认是 +1).这样就符合实际的使用场景了.
		IApnsService service = getApnsService();
			Payload payload = new Payload();
			payload.setBadge(1);
			payload.setAlert(alert);
			payload.setSound(sound);
			payload.addParam("message", message);
			logger.info("消息长度:"+payload.toString().getBytes().length);
			logger.info("消息内容:"+payload);
			service.sendNotification(deviceToken, payload);
	}
}
