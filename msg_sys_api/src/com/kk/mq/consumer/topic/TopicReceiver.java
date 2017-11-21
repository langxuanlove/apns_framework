package com.kk.mq.consumer.topic;

/**
 * 
 */
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.springframework.stereotype.Component;

/**
 * @描述 Topic消息监听器
 */
@Component(value = "topicReceiver")
public class TopicReceiver implements MessageListener {
	@Override
	public void onMessage(Message message) {
		try {
			System.out.println("TopicReceiver1接收到消息:"
					+ ((TextMessage) message).getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}