package com.kk.utils.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import net.sf.json.JSONObject;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.util.ByteSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestReponse implements MessageListener{
	private static final Logger LOG = LoggerFactory.getLogger(RequestReponse.class);
	private ConnectionFactory connectionFactory;
	private Connection connection = null;
	private Session session;
	private Destination destination;
	private MessageProducer producer;
	private MessageConsumer consumer;
	private String returnTopic;
	private String URL="failover:(tcp://114.112.90.40:61616,tcp://114.112.90.44:61616)";
//	private String URL="failover:(tcp://192.168.4.25:61616,tcp://192.168.4.26:61616,tcp://192.168.4.27:61616)";
	public void receiveMessage(String topic) throws Exception {
		returnTopic=topic;
		connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, URL);
		connection = connectionFactory.createConnection();
		connection.start();
		session = connection.createSession(Boolean.FALSE,
				Session.AUTO_ACKNOWLEDGE);
		destination = session.createQueue(topic + "_request");
		consumer = session.createConsumer(destination);
		consumer.setMessageListener(this);
		System.out.println("接收session："+session);
	}

	@Override
	public void onMessage(Message msg) {
		String str="";
		try {
			TextMessage message=(TextMessage) msg;
			if(message!=null){
				str=message.getText();
				JSONObject jsonObject=JSONObject.fromObject(str);
				String content = jsonObject.get("messageId").toString();
//				session = connection.createSession(Boolean.TRUE,
//						Session.AUTO_ACKNOWLEDGE);
//				destination = session.createQueue(returnTopic + "_response");
//				producer = session.createProducer(destination);
//				producer.setDeliveryMode(DeliveryMode.PERSISTENT);
				System.out.println("发送的数据:"+content);
//				TextMessage textMessage=session.createTextMessage(content);
//				producer.send(textMessage);
//				session.commit();
//				System.out.println("发送session："+session);
//				session.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception {
		RequestReponse reponse=new RequestReponse();
		reponse.receiveMessage("010lza88022843");
	}
}
