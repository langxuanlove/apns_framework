package com.kk.utils.activemq;
import java.util.Date;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kk.utils.PropertiesUtil;
/**
 * 
 * @author Jikey
 * @version 
 * @className: ActivemqSend <br/>
 * @date: 2016-1-5 下午2:09:50 <br/>
 * @since JDK 1.7
 *
 */
public class ActivemqSend {
	public static final Logger LOG = LoggerFactory.getLogger(ActivemqSend.class);
	private static ConnectionFactory connectionFactory;
	private String URL =PropertiesUtil.getKeyValue("ACTIVEMQ_URL");
	private Connection connection = null;
	private static ActivemqSend instance=null;
    public static ActivemqSend getInstance(){
        if(instance==null){
            synchronized(ActivemqSend.class){
                if(instance==null){
                    instance=new ActivemqSend();
                }
            }
        }
        return instance;
    }
    private ActivemqSend(){}

	/**
	 * 获取单例的ConnectionFactory
	 * 
	 * @return
	 * @throws Exception 
	 */
	private synchronized Connection getConnectionFactory() throws Exception {
		LOG.info("Connection:"+connection);
		if(connection!=null)return connection;
		LOG.info("getConnectionFactory create new");
		connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, URL);
		connection=connectionFactory.createConnection();
		connection.start();
		LOG.info("Connection:"+connection+"create success");
		return connection;
	}
	/**
	 * 
	 * produce:(发送消息). <br/>
	 *
	 * @author Jikey
	 * @param msg
	 * @param topic
	 * @param flag
	 * 			判断是队列模式还是主题模式发送,true:队列;false:主题
	 * @return
	 * @throws Exception
	 */
	public String produce(String msg, String topic,boolean flag) throws Exception {
		LOG.info("producer send msg:",msg);
		if (msg == null || "".equals(msg)) {
			LOG.warn("发送消息不能为空。");
			return "fail";
		}
		try {
			Connection connection = getConnectionFactory();
			Session session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			TextMessage textMessage = session.createTextMessage(msg);
			Destination destination=null; 
			if(flag){
				destination= session.createQueue(topic);
			}else{
				destination= session.createTopic(topic);
			}
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			producer.send(textMessage);
			session.close();
			return "success";
		} catch (JMSException e) {
			LOG.error(e.getMessage(), e);
			return "fail";
		}
	}
	public static void main(String[] args) throws Exception {
		System.out.println(ActivemqSend.getInstance()==ActivemqSend.getInstance());
		System.out.println("开始时间:"+new Date());
		for (int i = 0; i < 30; i++) {
			ActivemqSend.getInstance().produce("demomessage"+i, "demo",false);
		}
		System.out.println("结束时间:"+new Date());
	}
}
