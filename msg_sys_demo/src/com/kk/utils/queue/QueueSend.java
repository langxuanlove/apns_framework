package com.kk.utils.queue;

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

import com.kk.utils.PropertiesUtil;

public class QueueSend {
	public static final Logger LOG = LoggerFactory.getLogger(QueueSend.class);
	private static PooledConnectionFactory poolFactory;
	private String URL =PropertiesUtil.getKeyValue("ACTIVEMQ_URL");

	public QueueSend() {
	}

	/**
	 * 获取单例的PooledConnectionFactory
	 * 
	 * @return
	 */
	private synchronized PooledConnectionFactory getPooledConnectionFactory() {
		LOG.info("getPooledConnectionFactory");
		if (poolFactory != null)
			return poolFactory;
		LOG.info("getPooledConnectionFactory create new");
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, URL);
		poolFactory = new PooledConnectionFactory(factory);
		//不是超过了最大数量的连接就开始回收,而是维护的最大连接数.
		poolFactory.setMaxConnections(5);
		poolFactory.setMaximumActiveSessionPerConnection(100);
		poolFactory.setTimeBetweenExpirationCheckMillis(3000);
		LOG.info("getPooledConnectionFactory create success");
		return poolFactory;
	}

	/**
	 * 1.对象池管理connection和session,包括创建和关闭等
	 * 
	 * @return * @throws JMSException
	 */
	public Session createSession() throws JMSException {
		PooledConnectionFactory poolFactory = getPooledConnectionFactory();
		PooledConnection pooledConnection = (PooledConnection) poolFactory
				.createConnection();
		return pooledConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	public String produce(String msg, String topic) {
		LOG.info("producer send msg: {} ", msg);
		if (msg == null || "".equals(msg)) {
			LOG.warn("发送消息不能为空。");
			return "fail";
		}
		try {
			Session session = createSession();
			TextMessage textMessage = session.createTextMessage(msg);
			Destination destination = session.createQueue(topic);
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			producer.send(textMessage);
			return "success";
		} catch (JMSException e) {
			LOG.error(e.getMessage(), e);
			return "fail";
		}
	}
}
