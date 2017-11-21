package com.kk.utils.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.util.ByteSequence;

/**
 * topic持久化订阅工具类
 * @author Jikey
 *
 */
public class DurableSubscriberUtil implements MessageListener {
	public static void main(String[] args) {
		DurableSubscriberUtil receiver = new DurableSubscriberUtil();
		receiver.getMsg();
	}

	public void getMsg() {
		// ConnectionFactory ：连接工厂，JMS 用它创建连接
		ConnectionFactory connectionFactory;
		// Connection ：JMS 客户端到JMS Provider 的连接
		Connection connection = null;
		// Session： 一个发送或接收消息的线程
		Session session;
		// Destination ：消息的目的地;消息发送给谁.
		Destination destination;
		// 消费者，消息接收者
		MessageConsumer consumer;
		connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD,
				"failover:(tcp://192.168.4.25:61616,tcp://192.168.4.26:61616,tcp://192.168.4.27:61616)");
		try {
			// 构造从工厂得到连接对象
			connection = connectionFactory.createConnection();
			// 启动
			connection.setClientID("1234");
			connection.start();
			// 获取操作连接
			session = connection.createSession(Boolean.FALSE,
					Session.AUTO_ACKNOWLEDGE);
			// 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
			destination = session.createTopic("123123_request");
			consumer = session.createDurableSubscriber((Topic) destination,"123");
			consumer.setMessageListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onMessage(Message message) {
		String messageText = null;
		try {
			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				messageText = textMessage.getText();
				System.out.println("messageText = " + messageText);
			}
			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				System.out.println(message);
				// 从返回的信息中获取CorrelationID即可，判断是不是同一个设备返回的
				String id = message.getJMSCorrelationID();
				System.out.println(textMessage.getText());
			}
			if (message instanceof ActiveMQBytesMessage) {
				ActiveMQBytesMessage a = (ActiveMQBytesMessage) message;
				ByteSequence bs = a.getMessage().getContent();
				System.out.println("bytes data:" + new String(bs.getData()));
			}
		} catch (JMSException e) {
		}
	}

}