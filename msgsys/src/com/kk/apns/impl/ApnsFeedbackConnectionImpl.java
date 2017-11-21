package com.kk.apns.impl;

import static com.kk.apns.model.ApnsConstants.*;

import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.net.SocketFactory;

import com.kk.apns.IApnsFeedbackConnection;
import com.kk.apns.model.ApnsConfig;
import com.kk.apns.model.Feedback;
import com.kk.apns.tools.ApnsTools;

/**
 * @author RamosLi
 *
 */
public class ApnsFeedbackConnectionImpl implements IApnsFeedbackConnection {
	private static int READ_TIMEOUT = 10000; // 10s
	private static int DATA_LENGTH = 38;
	private ApnsConfig config;
	private SocketFactory factory;
	public ApnsFeedbackConnectionImpl(ApnsConfig config, SocketFactory factory) {
		this.config = config;
		this.factory = factory;
	}

	@Override
	public List<Feedback> getFeedbacks() {
		List<Feedback> list = null;
		Socket socket = null;
		try {
			String host = FEEDBACK_HOST_PRODUCTION_ENV;
			int port = FEEDBACK_PORT_PRODUCTION_ENV;
			if (config.isDevEnv()) {
				host = FEEDBACK_HOST_DEVELOPMENT_ENV;
				port = FEEDBACK_PORT_DEVELOPMENT_ENV;
			}
			socket = factory.createSocket(host, port);
			socket.setSoTimeout(READ_TIMEOUT);
			InputStream is = socket.getInputStream();
			while (true) {
				byte[] bytes = new byte[DATA_LENGTH];
				int size = is.read(bytes);
				if (size == DATA_LENGTH) {
					if (list == null) {
						list = new ArrayList<Feedback>();
					}
					/**
					 * see The Feedback Service chapter
					 * https://developer.apple.com/library/ios/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/Chapters/CommunicatingWIthAPS.html
					 * 
					 */
					byte[] tokenByte = new byte[32]; 
					System.arraycopy(bytes, 6, tokenByte, 0, 32);
					String token = ApnsTools.encodeHex(tokenByte).toLowerCase();
					long time = ApnsTools.parse4ByteInt(bytes[0], bytes[1], bytes[2], bytes[3]);
					Feedback feedback = new Feedback();
					feedback.setTime(time);
					feedback.setToken(token);
					list.add(feedback);
				} else {
					break;
				}
			}
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return list;
	}
	
}
