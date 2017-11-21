package com.kk.action.thread;

import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kk.mq.producer.topic.SendTopicReqResp;
import com.kk.mq.producer.topic.SendTopicRequest;
import com.kk.utils.PhoneNumUtil;
import com.kk.utils.SendSMS;
/**
 * 连接手机客户端
 * @author Jikey
 *
 */
public class ThreadDoInfo implements Runnable {
	private static Log logger = LogFactory.getLog(ThreadDoInfo.class);
	private SendTopicReqResp reqResp;
	private String message;
	private String topic;
	private String phoneNumber;
	private String timeout;
	public ThreadDoInfo(String message,String topic,String phoneNumber,String timeout,SendTopicRequest sendTopicRequest,SendTopicReqResp reqResp) {
		this.message = message;
		this.topic = topic;
		this.phoneNumber=phoneNumber;
		this.timeout =timeout;
		this.reqResp=reqResp;
	}
	public ThreadDoInfo() {
	}

	@Override
	public void run() {
		try {
			//规定客户端的Id发送者以此Id返回内容
			String correlationId=UUID.randomUUID().toString().replaceAll("-", "");
			String result = reqResp.sendMsg(message, topic, correlationId,
					timeout);
			logger.info("result:" + result+";主题topic:"+topic);
			String resultSMS = "{\"RECODE\":\"0000\",\"RESULT\":\"" + result
					+ "\"}";
			if ("fail".equals(result)) {
				// 查询手机号码给用户发短信息
				// 接受手机号smsMob注：多个号码用“,”隔开
				logger.info("发送的message:"+message);
				String smsContent = ParseStrToJsonUtil.getSmsContent(message);
				logger.info("发送短信消息内容smsContent:"+smsContent);
				if("".equals(smsContent)||smsContent==null){
					resultSMS="success";
				}else{
					//判断是不是手机号码，如果是，发短信消息不是就不发送短信消息
					if(PhoneNumUtil.flagisPhoneNum(topic)){
						resultSMS = SendSMS.sendSMS(topic, smsContent);
					}else{
						resultSMS="success";
					}
				}
				if ("success".equals(resultSMS)) {
					result = "success";
				} else {
					result = "fail";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
