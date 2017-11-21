package com.kk.action.service;

import java.util.List;

import com.kk.action.model.MessageModel;
import com.kk.apns.IApnsService;
import com.kk.apns.model.Payload;

public interface ISendIOSService {
	/**
	 * 向苹果服务器发送消息,转给IPhone设备 list集合发送
	 * @param list
	 * @param message
	 * @return
	 */
	public void  sendMessageForIOS(List<MessageModel> list)throws Exception;
	/**
	 * 向苹果服务器发送消息,转给IPhone设备   单个发送
	 * @param alert
	 * @param sound
	 * @param message
	 * @param deviceToken
	 * @throws Exception
	 */
	public void sendMessageForIOS(String alert,String sound, String message, String deviceToken)throws Exception ;
}
