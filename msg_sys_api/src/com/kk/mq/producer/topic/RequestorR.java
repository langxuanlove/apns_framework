package com.kk.mq.producer.topic;

import java.io.IOException;
import java.util.UUID;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;
@Component(value="queueSenderRep")
public class RequestorR {
	@Resource(name="queueDestinationQueue")   
    private Destination destination; 
	private static final class CorrelationIdPostProcessor implements
			MessagePostProcessor {
		private final String correlationId;

		public CorrelationIdPostProcessor(final String correlationId) {
			this.correlationId = correlationId;
		}

		@Override
		public Message postProcessMessage(final Message msg)
				throws JMSException {
			msg.setJMSCorrelationID(correlationId);
			return msg;
		}
	}

	private final JmsTemplate jmsTemplate;

	@Autowired
	public RequestorR(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public String request(final String request, String queue)
			throws IOException {
		final String correlationId = UUID.randomUUID().toString();
		jmsTemplate.convertAndSend(queue + ".request", request,
				new CorrelationIdPostProcessor(correlationId));
		return (String) jmsTemplate.receiveSelectedAndConvert(queue
				+ ".request", "JMSCorrelationID='" + correlationId + "'");
	}
//	public String request(final String request, String queue)
//			throws IOException {
//		final String correlationId = UUID.randomUUID().toString();
//		jmsTemplate.send("queue", new MessageCreator(){  
//			public Message createMessage(Session session)
//					throws JMSException {
//				TextMessage txtMessage=session.createTextMessage("这是一个测试");
//				txtMessage.setJMSCorrelationID(correlationId);
//				txtMessage.setJMSReplyTo(destination);
//				txtMessage.setJMSCorrelationID(correlationId);
//				  return txtMessage;
//			}  
//        } );
//		 jmsTemplate.send(destination,  
//		            new MessageCreator(){  
//						public Message createMessage(Session session)
//								throws JMSException {
//							TextMessage txtMessage=session.createTextMessage("这是一个测试");
//							txtMessage.setJMSCorrelationID(correlationId);
//							txtMessage.setJMSReplyTo(destination);
//							txtMessage.setJMSCorrelationID(correlationId);
//							  return txtMessage;
//						}  
//		            }  
//		        ); 
//		 return "success";
//	}
}