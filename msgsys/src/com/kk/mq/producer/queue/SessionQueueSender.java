package com.kk.mq.producer.queue;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

/**
 * @描述 发送消息到队列
 */
@Component(value="sessionQueueSender")
public class SessionQueueSender {
	@Resource(name="jmsTemplate")
	private JmsTemplate jmsTemplate;// 通过@Qualifier修饰符来注入对应的bean

	/**
	 * 发送一条消息到指定的队列（目标）
	 * 
	 * @param queueName
	 *            队列名称
	 * @param message
	 *            消息内容
	 */
	public void send(Destination sessionAwareQueue, final String message) {
		System.out.println("SessionQueueSender_producer:"+message.toString());
		jmsTemplate.send(sessionAwareQueue, new MessageCreator() {  
	            public Message createMessage(Session session) throws JMSException {   
	                return session.createTextMessage(message);   
	            }  
	    });
	}   
}