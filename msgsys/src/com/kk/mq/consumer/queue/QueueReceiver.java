package com.kk.mq.consumer.queue;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.springframework.stereotype.Component;


/**
 * @描述 队列消息监听器
 */
@Component(value="queueReceiver")
public class QueueReceiver implements MessageListener {
	private Session session;
	private MessageProtocol messageProtocol;
	 private MessageProducer replyProducer;
	@Override
	public void onMessage(Message message) {
		try {
			System.out.println(message.getJMSCorrelationID()+"message.getJMSReplyTo():"+message.getJMSReplyTo());
			 TextMessage response = this.session.createTextMessage();
			 if (message instanceof TextMessage) {
				 	System.out.println("if语句中的内容:"+((TextMessage) message).getText());
	                TextMessage txtMsg = (TextMessage) message;
	                String messageText = txtMsg.getText();
	                response.setText(this.messageProtocol.handleProtocolMessage(messageText));
	            }
			 response.setJMSCorrelationID(message.getJMSCorrelationID());
			 this.replyProducer.send(message.getJMSReplyTo(), response);
	         this.session.commit();//如果开启事务，这儿就需要提交，才会消费掉这条消息
			System.out.println("QueueReceiver1接收到消息:"
					+ ((TextMessage) message).getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}