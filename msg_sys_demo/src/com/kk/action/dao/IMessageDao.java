package com.kk.action.dao;

import java.util.List;
import java.util.Map;

public interface IMessageDao {
	/**
	 * 
	 * 更新消息的状态(通过jpushId删除消息)
	 * 
	 * @param message
	 * @return
	 */
	public String updateMsgStatusByJpushId(String jpushId);
	
	/**
	 * 
	 * 保存发送消息历史数据
	 * 
	 * @param message
	 * @param jpushId
	 * @param jpushBody
	 */
	public void insertMessage(String messageId,String topic,String message,String deviceType,String alert,String jpushId,String jpushBody);
	
	/**
	 * 
	 * 查询消息是否超时,否则发短信
	 * 
	 * @return
	 */
	public List<Map<String,Object>> queryExpiredMessage();
	/**
	 * 
	 * 通过消息ID删除信息
	 * 
	 * @param messageId
	 * @return
	 */
	public String updateMsgStatusById(String messageId);
}
