package com.kk.action.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.kk.action.dao.IMessageDao;
import com.kk.action.service.IMessageService;
import com.kk.utils.DateUtil;
import com.kk.utils.PropertiesUtil;
import com.kk.utils.UUIDUtil;
import com.kk.utils.jpush.PushClientUtil;

@Service(value="messageService")
public class MessageServiceImpl  implements IMessageService{
	private Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
	@Resource(name="messageDao")
	private IMessageDao messageDao;
	private String appKey=PropertiesUtil.getKeyValue("APP_KEY");
	private String masterSecret=PropertiesUtil.getKeyValue("MASTER_SECRET");
	@Override
	public String updateMsgStatusByJpushId(String jpushId) {
		// TODO Auto-generated method stub
		return messageDao.updateMsgStatusByJpushId(jpushId);
	}

	@Transactional
	@Override
	public String sendMessage(String topic,String deviceType,String alert, String message){
		// TODO Auto-generated method stub
		String jpushBody;
		try {
			String messageId=UUIDUtil.getUUID();
			logger.info("参数信息:appKey:"+appKey+";masterSecret:"+masterSecret);
			logger.info("topic:"+topic+";deviceType:"+deviceType+";alert:"+alert+";message:"+message);
			jpushBody = PushClientUtil.send(topic,deviceType,appKey,masterSecret,alert,message);
			logger.info("极光返回的消息时间:"+DateUtil.getCurrentDateTime()+";极光的jpushBody:"+jpushBody);
			String jpushId=JSONObject.parseObject(jpushBody).getString("msg_id");
			messageDao.insertMessage(messageId,topic,message,deviceType,alert, jpushId, jpushBody);
			return "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail";
		}
	}

	
	
	
}
