package com.kk.mq.producer.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.pool.PooledConnection;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.activemq.util.ByteSequence;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.kk.utils.PropertiesUtil;

import sun.print.resources.serviceui;

@Component(value = "sendTopicReqResp")
public class SendTopicReqResp {
	private static Log logger = LogFactory.getLog(SendTopicReqResp.class);
	private ConnectionFactory connectionFactory;
	private Connection connection = null;
	private Session session;
	private Destination destination;
	private MessageProducer producer;
	private static String URL = PropertiesUtil.getKeyValue("ACTIVEMQ_URL");

	public SendTopicReqResp() {
		
	}
	 private static PooledConnectionFactory poolFactory;

	  /**
	   * 获取单例的PooledConnectionFactory
	   *  @return
	   */
	  private static synchronized PooledConnectionFactory getPooledConnectionFactory() {
	    logger.info("getPooledConnectionFactory");
	    if (poolFactory != null) return poolFactory;
	    logger.info("getPooledConnectionFactory create new");
	    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, URL);
	    poolFactory = new PooledConnectionFactory(factory);      
	    // 池中借出的对象的最大数目
	    poolFactory.setMaxConnections(100);
	    poolFactory.setMaximumActiveSessionPerConnection(50);      
	    //后台对象清理时，休眠时间超过了3000毫秒的对象为过期
	    poolFactory.setTimeBetweenExpirationCheckMillis(3000);
	    logger.info("getPooledConnectionFactory create success");
	    return poolFactory;
	  }

	  /**
	   * 1.对象池管理connection和session,包括创建和关闭等
	   * 2.PooledConnectionFactory缺省设置MaxIdle为1，
	   *  官方解释Set max idle (not max active) since our connections always idle in the pool.   *
	   *  @return   * @throws JMSException
	   */
	  public static Session createSession() throws JMSException {
	    PooledConnectionFactory poolFactory = getPooledConnectionFactory();
	    PooledConnection pooledConnection = (PooledConnection) poolFactory.createConnection();
	    //false 参数表示 为非事务型消息，后面的参数表示消息的确认类型（见4.消息发出去后的确认模式）
	    return pooledConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	  }
	/**
	 * 发送消息到PC端,原来是提供手机端使用的,现在手机端使用的是极光推送.
	 * 
	 * @param message
	 * @param topic
	 * @param messageId
	 * @param timeout
	 * @return
	 */
	public String sendMsg(String message, String topic, String messageId,
			String timeout) {
		String result = "";
		try {
			String msg = "{\"messageId\":\"" + messageId
					+ "\",\"messageBody\":" + message + "}";
			session = createSession();
			destination = session.createQueue(topic + "_request");
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			logger.info("发送消息ID：" + messageId + ";信息内容:" + msg);
			TextMessage ms = session.createTextMessage(msg);
			producer.send(ms);
			logger.info("PC端消息发送成功.");
//			result=receiveMessage(session, topic, msg, producer, timeout,messageId);
			result="success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return result;
	}

	public void sendMsg(Session session, String topic, String msg,
			MessageProducer producer, String timeout, String messageId) throws Exception{
		TextMessage message = session.createTextMessage(msg);
		producer.send(message);
	}
	
	public String receiveMessage(Session session, String topic, String msg,
			MessageProducer producer, String timeout, String messageId)
			throws Exception {
		int timetemp = 15000;
		Destination replyTopic = session.createQueue(topic + "_response");
		MessageConsumer responseConsumer = session.createConsumer(replyTopic);
		try {
			//手机客户端发送的是字节流，所以必须使用ActiveMQBytesMessage类来接受使用
//			ActiveMQBytesMessage replyMsg = (ActiveMQBytesMessage) responseConsumer
//					.receive(timetemp);
//			if (replyMsg == null || "".equals(replyMsg)) {
//				logger.info("手机端无返回消息");
//				return "fail";
//			} else {
//				ByteSequence bs = replyMsg.getMessage().getContent();
//				logger.info("手机端消息内容messageId:" + new String(bs.getData()));
//				if (messageId.equals(new String(bs.getData()))) {
//					return "success";
//				} else {
//					return "fail";
//				}
//			}
			//接受PC端返回数据
			TextMessage replyMsg = (TextMessage) responseConsumer
					.receive(timetemp);
			if (replyMsg == null || "".equals(replyMsg)) {
				logger.info("手机端无返回消息");
				return "fail";
			} else {
				logger.info("PC端返回的消息messageId:" + replyMsg.getText());
				if (messageId.equals(replyMsg.getText())) {
					return "success";
				} else {
					return "fail";
				}
			}
		} catch (Exception e) {
			return "fail";
		}

	}
}