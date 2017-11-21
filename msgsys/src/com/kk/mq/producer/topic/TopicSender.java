package com.kk.mq.producer.topic;

import java.io.Serializable;
import java.util.UUID;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

/**
 * @描述 发送消息到主题
 */
@Component(value = "topicSender")
public class TopicSender implements MessageListener{
	@Resource(name="jmsTopicTemplate")
	private JmsTemplate jmsTemplate;
	private String id;
	/**
	 * 发送一条消息到指定的队列（目标）
	 * 
	 * @param queueName
	 *            队列名称
	 * @param message
	 *            消息内容
	 */
	public void send(Destination destination, final String message) {
		System.out.println("TopicSender_producer:"+message.toString());
		jmsTemplate.send(destination, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}
		});
//发送端		
//		jmsTemplate.sendAndReceive("", new MessageCreator() {
//			public Message createMessage(Session session) throws JMSException {
//				Serializable user = null;
//				ObjectMessage objMsg = session.createObjectMessage(user);
//				String correlationID = UUID.randomUUID().toString();
//				System.out.println("correlationID:" + correlationID);
//				objMsg.setJMSCorrelationID(correlationID);
//				return objMsg;
//				}
//		});
//客户端响应
//		connection = connectionFactory.createConnection();
//		connection.start();
//		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//
//		producer = session.createProducer(null);
//		consumer = session.createConsumer(destination);
//		Message msg = consumer.receive();
//		ObjectMessage objMsg = (ObjectMessage)msg;
//		User user = (User)objMsg.getObject();
//		System.out.println("receive user:"+ user.getUsername());
//
//		TextMessage replyMsg = session.createTextMessage();
//		replyMsg.setJMSCorrelationID(msg.getJMSCorrelationID());
//		replyMsg.setText("reply message");
//		producer.send(msg.getJMSReplyTo(), replyMsg);
		
	}
	
	public void sendReturn(Destination destination, final String message) {
		System.out.println("TopicSender_producer:"+message.toString());
		jmsTemplate.send(destination, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				Destination tempDest = session.createTemporaryQueue();
	        	MessageConsumer responseConsumer = session.createConsumer(tempDest);
	            TextMessage textmessage = session.createTextMessage("ActiveMq_发送内容:" + message);
	            textmessage.setJMSReplyTo(tempDest);
	            textmessage.setJMSCorrelationID("60000000000");
				return textmessage;
			}
		});
	}
	   public void onMessage(Message message) {
	        String messageText = null;
	        try {
	            if (message instanceof TextMessage) {
	                TextMessage textMessage = (TextMessage) message;
	                messageText = textMessage.getText();
	                if(!messageText.equals("")){
	                	System.out.println("this id :"+id+"消息返回！");
	                }
	                System.out.println("messageText = " + messageText);
	            }
	        } catch (JMSException e) {
	            //Handle the exception appropriately
	        }
	    }
	    private String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
}