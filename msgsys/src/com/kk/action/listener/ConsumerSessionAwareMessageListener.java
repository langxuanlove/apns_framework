package com.kk.action.listener;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

@Component(value="consumerSessionAware")
public class ConsumerSessionAwareMessageListener implements
		SessionAwareMessageListener<TextMessage> {
	@Resource(name="sessionAwareQueue")
	private Destination destination;
	
	public void onMessage(TextMessage message, Session session)
			throws JMSException {
		// TODO Auto-generated method stub
		System.out.println("ConsumerSessionAwareMessageListener_收到一条消息内容是：" + message.getText());
		System.out.println(destination.toString());
		MessageProducer producer = session.createProducer(destination);
		Message textMessage = session
				.createTextMessage("ConsumerSessionAwareMessageListener。。。");
		producer.send(textMessage);
	}
	
	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

}