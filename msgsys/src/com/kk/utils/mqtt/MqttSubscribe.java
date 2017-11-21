package com.kk.utils.mqtt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttSubscribe implements  MqttCallback{
	private int a=0;
	public static void main(String[] args) {
		MqttSubscribe client=new MqttSubscribe();
		client.message();
		
		
	}
	public void message() {
        String topic        = "iphone_request";
        String content      = "Message from MqttPublishSample";
        int qos             = 2;
//        String broker       = "tcp://192.168.4.52:1883";
        String broker       = "tcp://114.112.90.40:8161";
        String clientId     = "MqttiPhoneRequest";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            sampleClient.connect(connOpts);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.subscribe(topic,2);
            sampleClient.setCallback(this);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }

	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
		System.out.println("connectionLost"+arg0);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
		System.out.println("deliveryComplete:"+arg0);
	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("messageArrived:"+arg0);
		a++;
		String msg="messageArrived:("+a+"):"+arg1.toString()+"\r\n";
		System.out.println(msg);
		//判断系统类型
		String systemtype=System.getProperties().getProperty("os.name").toLowerCase();
		if(systemtype.contains("windows")){
			appendMessageToFile("c:/msg.txt",msg);
			System.out.println("===========os.name:"+systemtype);
		}else{
			appendMessageToFile("/home/msg.txt",msg);
			System.out.println("===========os.name:"+systemtype);
		}
		try {
			MqttPublish publish=new MqttPublish();
			publish.sendMessage();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	   public  void appendMessageToFile(String fileName, String content) {
	        try {
	        	File file =new File(fileName);
	            if(!file.exists()){
	                System.out.println("文件不存在");
	                file.createNewFile();
	                System.out.println("文件创建完毕");
	            } 
	            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
	            FileWriter writer = new FileWriter(fileName, true);
	            writer.write(content);
	            writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
