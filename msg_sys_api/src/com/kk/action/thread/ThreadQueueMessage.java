package com.kk.action.thread;

import java.util.UUID;

import com.kk.mq.producer.queue.RequestQueue;

public class ThreadQueueMessage implements Runnable{
	private RequestQueue requestQueue;
	private String message;
	private String topic;
	private String timeout;
	public ThreadQueueMessage(String message,String topic,String timeout,RequestQueue requestQueue) {
		this.message = message;
		this.topic = topic;
		this.timeout =timeout;
		this.requestQueue=requestQueue;
	}
	public ThreadQueueMessage() {
	}

	@Override
	public void run() {
		try {
			//规定客户端的Id发送者以此Id返回内容
			String correlationId=UUID.randomUUID().toString().replaceAll("-", "");
			System.out.println("ThreadQueueMessage的correlationId:"+correlationId);
			String result = requestQueue.request(message, topic, correlationId,timeout);
			System.out.println("ThreadQueueMessage的result:" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
