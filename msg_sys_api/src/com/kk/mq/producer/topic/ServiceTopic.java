package com.kk.mq.producer.topic;

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

@Component(value="serviceTopic")
public class ServiceTopic {
	private static final class ProducerConsumer implements
			SessionCallback<Message> {
		private static final int TIMEOUT = 10000;

		private final String msg;

		private final DestinationResolver destinationResolver;

		private final String queue;
		private final String correlationId;
		public ProducerConsumer(final String msg, String queue,
				final DestinationResolver destinationResolver,final String correlationId) {
			this.msg = msg;
			this.queue = queue;
			this.destinationResolver = destinationResolver;
			this.correlationId=correlationId;
		}

		public Message doInJms(final Session session) throws JMSException {
			MessageConsumer consumer = null;
			MessageProducer producer = null;
			Message message;
			try {
//				final String correlationId = UUID.randomUUID().toString();
				//参数为true订阅模式为主题,参数为false订阅模式为queue
				final Destination requestQueue = destinationResolver
						.resolveDestinationName(session, queue + ".request",
								true);
				final Destination replyQueue = destinationResolver
						.resolveDestinationName(session, queue + ".response",
								true);
				// Create the consumer first!
				consumer = session.createConsumer(replyQueue,
				"JMSCorrelationID = '" + correlationId + "'");
				System.out.println("correlationId:"+correlationId);
				final TextMessage textMessage = session.createTextMessage(msg);
				textMessage.setJMSCorrelationID(correlationId);
				textMessage.setJMSReplyTo(replyQueue);
				// Send the request second!
				producer = session.createProducer(requestQueue);
				producer.send(requestQueue, textMessage);
				// Block on receiving the response with a timeout
				// 设置5秒以后如果没结果返回就直接发短信了.
				message= consumer.receive(TIMEOUT);
			} finally {
				// Don't forget to close your resources
				JmsUtils.closeMessageConsumer(consumer);
				JmsUtils.closeMessageProducer(producer);
				System.out.println("ProducerConsumer关闭连接！");
			}
			return message;
		}
	}
	@Resource(name="jmsTemplate")
	private final JmsTemplate jmsTemplate;
	@Autowired
	public ServiceTopic(final JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public String request(final String request, String queue,String correlationId) {
		// Must pass true as the second param to start the connection
		TextMessage message = (TextMessage) jmsTemplate.execute(
					new ProducerConsumer(request, queue, jmsTemplate
							.getDestinationResolver(),correlationId), true);
		try {
			if (message == null	|| "".equals(message.getText().toString())) {
				return "fail";
			} else {
					return "success";
			}
		} catch (JMSException e) {
			e.printStackTrace();
			return "fail";
		}
	}
}