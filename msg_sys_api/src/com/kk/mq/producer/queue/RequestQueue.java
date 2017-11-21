package com.kk.mq.producer.queue;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.SessionCallback;
import org.springframework.jms.support.JmsUtils;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.stereotype.Component;

@Component(value="requestQueue")
public class RequestQueue {
	@Resource(name="jmsTemplate")
	private final JmsTemplate jmsTemplate;
	@Autowired
	public RequestQueue(final JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	private static final class ProducerConsumer implements
			SessionCallback<Message> {
		private   int TIMEOUT = 5000;
		private   int timetemp; 
		private final String msg;

		private final DestinationResolver destinationResolver;

		private final String queue;
		private final String correlationId;
		public ProducerConsumer(final String msg, String queue,
				final DestinationResolver destinationResolver,final String correlationId,String timeout) {
			this.msg = msg;
			this.queue = queue;
			this.destinationResolver = destinationResolver;
			this.correlationId=correlationId;
			if(timeout==null ||"".equals(timeout)||"0".equals(timeout)){
				this.timetemp=TIMEOUT;
			}else{
				this.timetemp=Integer.parseInt(timeout);
			}
		}

		public Message doInJms(final Session session) throws JMSException {
			MessageConsumer consumer = null;
			MessageProducer producer = null;
			Message message;
			try {
				//参数为true订阅模式为主题,参数为false订阅模式为queue
				final Destination requestQueue = destinationResolver
						.resolveDestinationName(session, queue + ".request",
								false);
				final Destination replyQueue = destinationResolver
						.resolveDestinationName(session, queue + ".response",
								false);
				// Create the consumer first!
				consumer = session.createConsumer(replyQueue,
				"JMSCorrelationID = '" + correlationId + "'");
				System.out.println("correlationId:"+correlationId);
				final TextMessage textMessage = session.createTextMessage(msg);
				textMessage.setJMSCorrelationID(correlationId);
				textMessage.setJMSReplyTo(replyQueue);
				// Send the request second!
				producer = session.createProducer(requestQueue);
				//设置存储功能2为存储,保证客户端离线后上线能收到消息
				producer.setDeliveryMode(2);
				producer.send(requestQueue, textMessage);
				// Block on receiving the response with a timeout
				// 设置5秒以后如果没结果返回就直接发短信了.
				message=consumer.receive(30000);
			} finally {
				JmsUtils.closeMessageConsumer(consumer);
				JmsUtils.closeMessageProducer(producer);
			}
			return message;
		}
	}
	public String request(final String request, String queue,String correlationId,String timeout) {
		TextMessage message = (TextMessage) jmsTemplate.execute(
				new ProducerConsumer(request, queue, jmsTemplate
						.getDestinationResolver(),correlationId,timeout), true);
		try {
			if (message == null	|| "".equals(message.getText().toString())) {
				return "fail";
			} else {
					return "success";
			}
		} catch (JMSException e) {
			e.printStackTrace();
			return "exception in RequestQueue.class";
		}
	}
}
