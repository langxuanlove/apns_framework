package com.kk.apns.impl;

import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kk.apns.IApnsService;
import com.kk.apns.model.PushNotification;

/**
 * EN: resend the notifications which sent after an error one using same connection
 * CN: 重发，没啥好说的
 * @author RamosLi
 *
 */
public class ApnsResender {
	private static Log logger = LogFactory.getLog(ApnsResender.class);
	private static ApnsResender instance = new ApnsResender();
	public static ApnsResender getInstance() {
		return instance;
	}
	public void resend(String name, Queue<PushNotification> queue) {
		IApnsService service = ApnsServiceImpl.getCachedService(name);
		if (service != null) {
			while (!queue.isEmpty()) {
				service.sendNotification(queue.poll());
			}
		} else {
			logger.error("Cached service is null. name: " + name);
		}
	}

}
