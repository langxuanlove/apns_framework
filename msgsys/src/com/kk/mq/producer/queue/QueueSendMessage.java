package com.kk.mq.producer.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnection;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kk.utils.PropertiesUtil;

@Component(value = "queueSendMessage")
public class QueueSendMessage {
	public static final Logger LOG = LoggerFactory.getLogger(QueueSendMessage.class);
	private static PooledConnectionFactory poolFactory;
	
	
//	private ConnectionFactory connectionFactory;
//	private Connection connection = null;
//	private Session session;
	private Destination destination;
	private MessageProducer producer;
	private String URL = PropertiesUtil.getKeyValue("ACTIVEMQ_URL");

	public QueueSendMessage() {
//		connectionFactory = new ActiveMQConnectionFactory(
//				ActiveMQConnection.DEFAULT_USER,
//				ActiveMQConnection.DEFAULT_PASSWORD, URL);
	}

	public String sendMsg(String message, String queue) {
		String result = "";
		try {
//			connection = connectionFactory.createConnection();
//			connection.start();
//			session = connection.createSession(Boolean.TRUE,
//					Session.AUTO_ACKNOWLEDGE);
			Session session = createSession();
			destination = session.createQueue(queue + ".request");
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			System.out.println("QueueSendMessage信息内容:" + message);
			sendMessage(session, queue, message, producer);
			session.commit();
			result = "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		} finally {
			try {
//				if (null != connection)
//				connection.close();
			} catch (Throwable ignore) {
			}
		}
		return result;
	}

	public void sendMessage(Session session, String topic, String msg,
			MessageProducer producer) throws Exception {
		TextMessage message = session.createTextMessage(msg);
		producer.send(message);
	}


	  /**
	   * 获取单例的PooledConnectionFactory
	   *  @return
	   */
	  private  synchronized PooledConnectionFactory getPooledConnectionFactory() {
	    LOG.info("getPooledConnectionFactory");
	    if (poolFactory != null) return poolFactory;
	    LOG.info("getPooledConnectionFactory create new");
	    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD,URL);
	    poolFactory = new PooledConnectionFactory(factory);      
	    // 池中借出的对象的最大数目
	    poolFactory.setMaxConnections(100);
	    poolFactory.setMaximumActiveSessionPerConnection(50);      
	    //后台对象清理时，休眠时间超过了3000毫秒的对象为过期
	    poolFactory.setTimeBetweenExpirationCheckMillis(3000);
	    LOG.info("getPooledConnectionFactory create success");
	    return poolFactory;
	  }

	  /**
	   * 1.对象池管理connection和session,包括创建和关闭等
	   * 2.PooledConnectionFactory缺省设置MaxIdle为1，
	   *  官方解释Set max idle (not max active) since our connections always idle in the pool.   *
	   *  @return   * @throws JMSException
	   */
	  public  Session createSession() throws JMSException {
	    PooledConnectionFactory poolFactory = getPooledConnectionFactory();
	    PooledConnection pooledConnection = (PooledConnection) poolFactory.createConnection();
	    //false 参数表示 为非事务型消息，后面的参数表示消息的确认类型（见4.消息发出去后的确认模式）
	    return pooledConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	  }

	  public String  produce(String msg, String topic) {
	    LOG.info("producer send msg: {} ", msg);
	    if (msg==null||"".equals(msg)) {
	      LOG.warn("发送消息不能为空。");
	      return "fail";
	    }
	    try {
	      Session session = createSession();
	      LOG.info("create session");
	      TextMessage textMessage = session.createTextMessage(msg);
	      Destination destination = session.createQueue(topic);
	      MessageProducer producer = session.createProducer(destination);
	      producer.setDeliveryMode(DeliveryMode.PERSISTENT);
	      producer.send(textMessage);
	      LOG.info("create session success");
	      return "success";
	    } catch (JMSException e) {
	      LOG.error(e.getMessage(), e);
	      return "fail";
	    }
	  }
	
}
