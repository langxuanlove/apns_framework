package com.kk.utils.activemq;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TopicConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQTopicSession;
import org.apache.activemq.ActiveMQTopicSubscriber;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.util.ByteSequence;

import com.kk.utils.PropertiesUtil;

/**
 * 
 * @author JiKey 普通消息订阅者 问题：如果生产者之前已经发送了消息、那么之前的消息订阅者目前的写法是收不到的。
 *         持久化订阅可以解决之前消息没法送到的问题.
 */
public class TopicSubscriberUtil implements MessageListener {
	private String URL = PropertiesUtil.getKeyValue("ACTIVEMQ_URL");

	public void getMsg(String clientId, String requestTopic, String subscriberId) {
		final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnectionFactory.DEFAULT_USER,
				ActiveMQConnectionFactory.DEFAULT_PASSWORD, URL);
		TopicConnection connection = null;
		ActiveMQTopicSession session = null;
		ActiveMQTopic topic = null;
		ActiveMQTopicSubscriber subscriber = null;
		// 发送队列
		try {
			connection = connectionFactory.createTopicConnection();
			connection.setClientID(clientId);
			connection.start();
			session = (ActiveMQTopicSession) connection.createTopicSession(
					Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			topic = (ActiveMQTopic) session.createTopic(requestTopic);
			// 创建持久订阅
			subscriber = (ActiveMQTopicSubscriber) session
					.createDurableSubscriber(topic, subscriberId);
			subscriber.setMessageListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onMessage(Message message) {
		try {
			// 判断消息的类型是文本型纤细
			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				System.out.println(message);
				// 从返回的信息中获取CorrelationID即可，判断是不是同一个设备返回的
				String id = message.getJMSCorrelationID();
				System.out.println("获取的数据id:" + id + ";消息体:"
						+ textMessage.getText());
			}
			// 判断消息的类型是字节流消息即安卓手机端传送回来的.
			if (message instanceof ActiveMQBytesMessage) {
				ActiveMQBytesMessage a = (ActiveMQBytesMessage) message;
				ByteSequence bs = a.getMessage().getContent();
				System.out.println("获取字节数据bytes data:"
						+ new String(bs.getData()));
			}
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		TopicSubscriberUtil chijiuhua = new TopicSubscriberUtil();
		chijiuhua.getMsg("client_subscriber1", "123123_request", "subscriber1");
	}
}
