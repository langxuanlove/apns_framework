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

@Component(value="sendTopicRequest")
public class SendTopicRequest {
	@Resource(name="jmsTemplate")
	private final JmsTemplate jmsTemplate;
	@Autowired
	public SendTopicRequest(final JmsTemplate jmsTemplate) {
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
			Message messge;
			try {
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
				messge= consumer.receive(10000);
			} finally {
				// Don't forget to close your resources
				JmsUtils.closeMessageConsumer(consumer);
				JmsUtils.closeMessageProducer(producer);
				System.out.println("SendTopicRequest.关闭连接!");
			}
			return messge;
		}
	}


	public String request(final String request, String queue,String correlationId,String timeout) {
		// Must pass true as the second param to start the connection
		TextMessage message = (TextMessage) jmsTemplate.execute(
				new ProducerConsumer(request, queue, jmsTemplate
						.getDestinationResolver(),correlationId,timeout), true);
		try {
			if (message == null	|| "".equals(message.getText().toString())) {
				return "fail";
			} else {
				System.out.println("业务系统返回消息："+message.getText().toString());
					return "success";
			}
		} catch (JMSException e) {
			e.printStackTrace();
			return "exception in requestor";
		}
	}
}