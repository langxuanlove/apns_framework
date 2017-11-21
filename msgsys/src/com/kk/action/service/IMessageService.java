package com.kk.action.service;

public interface IMessageService {

	/**
	 * 更新消息的状态
	 * @param msg
	 * @return
	 */
	public String updateMsgStatusByJpushId(String jpushId);
	
	/**
	 * 
	 * 极光推送服务接口
	 * 
	 * @param topic
	 * @param message
	 * @return
	 */
	public String sendMessage(String topic,String deviceType,String alert,String message);
}
