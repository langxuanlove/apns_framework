package com.kk.action.task;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.kk.action.dao.IMessageDao;
import com.kk.action.thread.ParseStrToJsonUtil;
import com.kk.utils.SendSMS;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component(value = "taskSendSMS")
public class TaskSendSMS extends QuartzJobBean {
	@Resource(name = "messageDao")
	private IMessageDao messageDao;
	private Logger logger = LoggerFactory.getLogger(TaskSendSMS.class);

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		logger.info("开始执行查询,时间:" + new Date());
		JobDataMap map = arg0.getMergedJobDataMap();
		messageDao = (IMessageDao) map.get("messageDao");
		String resultSMS = "";
		try {
			List<Map<String, Object>> _list = messageDao.queryExpiredMessage();
			Object json = JSON.toJSON(_list);
			logger.info("信息个数:" + _list.size());
			logger.info("查询信息:" + json);
			for (int i = 0; i < _list.size(); i++) {
				// 发送完短信成功后,更新消息的状态
				String smsContent = ParseStrToJsonUtil.getSmsContent((String) _list.get(i).get("MESSAGE"));
				logger.info("发送短信消息内容smsContent:" + smsContent);
				String messageId = (String) _list.get(i).get("ID");
				String res="";
				if ("".equals(smsContent) || smsContent == null) {
					logger.info("通过业务逻辑判断的需要发送的信息为空,smsContent:"+smsContent);
						res=messageDao.updateMsgStatusById(messageId);
						if("success".equals(res)){
							logger.info("不需要发送短信消息:"+messageId+"状态更新成功.");	
						}else{
							logger.info("不需要发送短信消息:"+messageId+"状态更新失败.");
						}
				} else {
					logger.info("通过业务逻辑判断需要发送的信息为,smsContent:"+smsContent);
					resultSMS = SendSMS.sendSMS((String) _list.get(i).get("TOPIC"), smsContent);
					if ("success".equals(resultSMS)){
						res=messageDao.updateMsgStatusById(messageId);
						if("success".equals(res)){
							logger.info("需要发送短信消息messageId:"+messageId+"状态更新成功.");	
						}else{
							logger.info("需要发送短信消息messageId:"+messageId+"状态更新失败.");
						}
					}
				}
			}
			logger.info("执行结束时间:" + new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
