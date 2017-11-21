package com.kk.action.thread;

import java.util.UUID;
import com.alibaba.fastjson.JSON;
import com.kk.mq.producer.topic.SendTopicReqResp;
import com.kk.mq.producer.topic.SendTopicRequest;
import com.kk.utils.SendSMS;
/**
 *	连接业务系统 
 */
public class ThreadDoInfoService implements Runnable {
	private SendTopicRequest sendTopicRequest;
	private String message;
	private String topic;
	private String phoneNumber;
	private String timeout;
	public ThreadDoInfoService(String message,String topic,String phoneNumber,String timeout,SendTopicRequest sendTopicRequest) {
		this.message = message;
		this.topic = topic;
		this.phoneNumber=phoneNumber;
		this.timeout =timeout;
		this.sendTopicRequest=sendTopicRequest;
	}
	public ThreadDoInfoService() {
	}

	@Override
	public void run() {
		try {
			//规定客户端的Id发送者以此Id返回内容
			String correlationId=UUID.randomUUID().toString().replaceAll("-", "");
			String result = sendTopicRequest.request(message, topic, correlationId,
					timeout);
			//发送端就是根据逗号‘,’进行分割发送的，所以这里不用处理
			System.out.println("result:" + result);
			String resultSMS = "{\"RECODE\":\"0000\",\"RESULT\":\"" + result
					+ "\"}";
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
