package com.kk.action.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kk.action.dao.IMessageDao;
import com.kk.utils.CalendarUtil;
import com.kk.utils.DateUtil;
import com.kk.utils.PropertiesUtil;

@Repository(value="messageDao")
public class MessageDaoImpl implements IMessageDao{
	private String intervalSecond=PropertiesUtil.getKeyValue("INTERVAL_SECOND");
	private Logger logger = LoggerFactory.getLogger(MessageDaoImpl.class);
	@Resource(name="jdbcTemplateInfo")
	private JdbcTemplate jdbcTemplateInfo;
	
	/**
	 * 
	 * 更新消息的状态,通过jpushId删除
	 * 
	 */
	@Override
	public String updateMsgStatusByJpushId(String jpushId) {
		// TODO Auto-generated method stub
		try {
			String sql="DELETE FROM message_info WHERE message_info.JPUSH_ID=?";
			int result=jdbcTemplateInfo.update(sql,new Object[]{jpushId});
			if(result==1){
				logger.info(DateUtil.getCurrentDateTime()+"通过jpushId,数据删除成功.");
				return "success";
			}else{
				logger.info(DateUtil.getCurrentDateTime()+"通过jpushId,数据删除失败.");
				return "fail";	
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.info(DateUtil.getCurrentDateTime()+"通过jpushId,数据删除异常.");
			return "fail";
		}
	}
	/**
	 * 
	 * 更新消息的状态,通过messageId删除
	 * 
	 */
	@Override
	public String updateMsgStatusById(String messageId) {
		// TODO Auto-generated method stub
				try {
					String sql="DELETE FROM message_info WHERE message_info.ID=?";
					int result=jdbcTemplateInfo.update(sql,new Object[]{messageId});
					if(result==1){
						logger.info(DateUtil.getCurrentDateTime()+"通过messageId,数据删除成功.");
						return "success";
					}else{
						logger.info(DateUtil.getCurrentDateTime()+"通过messageId,数据删除失败.");
						return "fail";	
					}
				} catch (Exception e) {
					// TODO: handle exception
					logger.info(DateUtil.getCurrentDateTime()+"通过messageId,数据删除异常.");
					return "fail";
				}
	}
	@Override
	public void insertMessage(String messageId,String topic,String message,String deviceType,String alert, String jpushId, String jpushBody) {
		// TODO Auto-generated method stub
		try {
			String sql="insert into  message_info (ID,MESSAGE,TOPIC,DEVICE_TYPE,ALERT,SEND_DATE,JPUSH_ID,JPUSH_BODY,STATUS) values(?,?,?,?,?,?,?,?,'0')";
			int result = jdbcTemplateInfo.update(sql,new Object[] { messageId, message, topic, deviceType,alert, DateUtil.getCurrentDateTime(), jpushId,jpushBody });
			if (result == 1) {
				logger.info(DateUtil.getCurrentDateTime()+"插入数据成功,值为:" + result);
			} else {
				logger.info(DateUtil.getCurrentDateTime()+"插入数据失败,值为:" + result);
			}
		} catch (Exception e) {
			logger.info("插入数据异常");
		}
	}

	@Override
	public List<Map<String, Object>> queryExpiredMessage() {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		String datetime=CalendarUtil.getBeforSecond(intervalSecond);
		try {
			String sql="select message_info.ID,message_info.MESSAGE,message_info.STATUS,message_info.DEVICE_TYPE,message_info.SEND_DATE," +
					   "message_info.JPUSH_ID,message_info.JPUSH_BODY,message_info.TOPIC,message_info.ALERT " +
					   "from message_info where message_info.STATUS='0' AND message_info.SEND_DATE < ?";
			list=jdbcTemplateInfo.queryForList(sql,new Object[]{datetime});
			logger.info("查询的日期:"+datetime);
			logger.info(DateUtil.getCurrentDateTime()+"返回的消息记录信息条数为:"+list);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
}
