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

import com.kk.utils.PropertiesUtil;

public class JMSConnectionUtill {

	private PooledConnectionFactory pooledConnectionFactory = null;
	private String URL =PropertiesUtil.getKeyValue("ACTIVEMQ_URL");

	private static class CreateConnUtil {
		private static JMSConnectionUtill instance = new JMSConnectionUtill();
	}

	private JMSConnectionUtill() {
		// ConnectionFactory ：连接工厂，JMS 用它创建连接
		ActiveMQConnectionFactory connectionFactory = null;
		// 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
		connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, URL);
		pooledConnectionFactory = new PooledConnectionFactory();
		pooledConnectionFactory.setConnectionFactory(connectionFactory);
		//此最大连接数不是超出50就回收,而是维护的连接数量，与jdbc线程池有本质的区别.
		pooledConnectionFactory.setMaxConnections(5);
		pooledConnectionFactory.setMaximumActiveSessionPerConnection(100);
		pooledConnectionFactory.setTimeBetweenExpirationCheckMillis(3000);
	}
	// 获取链接
	public Session getConnection() {
		PooledConnection connection = null;
		Session session=null;
		try {
			System.out.println(pooledConnectionFactory);
			connection = (PooledConnection) pooledConnectionFactory.createConnection();
			session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return session;
	}
	public static final JMSConnectionUtill getInstance() {
		return CreateConnUtil.instance;
	}
	
	public  void queueProduce(String queue,String message)throws Exception{
		Session session=JMSConnectionUtill.getInstance().getConnection();
		TextMessage textMessage=session.createTextMessage(message);
		Destination destination=session.createQueue(queue);
		MessageProducer producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		producer.send(textMessage);
	}
	public void topicProduce(String topic,String message)throws Exception{
		Session session=JMSConnectionUtill.getInstance().getConnection();
		TextMessage textMessage=session.createTextMessage(message);
		Destination destination=session.createTopic(topic);
		MessageProducer producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		producer.send(textMessage);
	}
	//线程池有点慢.
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 50; i++) {
			JMSConnectionUtill.getInstance().queueProduce("demo", "sdf");
		}
	}
}