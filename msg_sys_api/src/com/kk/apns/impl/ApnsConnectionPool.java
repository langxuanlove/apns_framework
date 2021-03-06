package com.kk.apns.impl;

import static com.kk.apns.model.ApnsConstants.*;

import java.io.Closeable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.net.SocketFactory;

import com.kk.apns.IApnsConnection;
import com.kk.apns.model.ApnsConfig;

/**
 * 
 * @author RamosLi
 *
 */
public class ApnsConnectionPool implements Closeable {
	private static int CONN_ID_SEQ = 1;
	private SocketFactory factory;
	private BlockingQueue<IApnsConnection> connQueue = null;
	
	private ApnsConnectionPool(ApnsConfig config, SocketFactory factory) {
		this.factory = factory;
		
		String host = HOST_PRODUCTION_ENV;
		int port = PORT_PRODUCTION_ENV;
		if (config.isDevEnv()) {
			host = HOST_DEVELOPMENT_ENV;
			port = PORT_DEVELOPMENT_ENV;
		}
		
		int poolSize = config.getPoolSize();
		connQueue = new LinkedBlockingQueue<IApnsConnection>(poolSize);
		
		for (int i = 0; i < poolSize; i++) {
			String connName = (config.isDevEnv() ? "dev-" : "pro-") + CONN_ID_SEQ++;
			IApnsConnection conn = new ApnsConnectionImpl(this.factory, host, port, config.getRetries(), 
					config.getCacheLength(), config.getName(), connName, config.getIntervalTime(), config.getTimeout());
			connQueue.add(conn);
		}
	}
	
	public IApnsConnection borrowConn() {
		try {
			return connQueue.take();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void returnConn(IApnsConnection conn) {
		if (conn != null) {
			connQueue.add(conn);
		}
	}
	
	@Override
	public void close() {
		while (!connQueue.isEmpty()) {
			try {
				connQueue.take().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * create instance
	 * @param config
	 * @return
	 */
	public static ApnsConnectionPool newConnPool(ApnsConfig config, SocketFactory factory) {
		return new ApnsConnectionPool(config, factory);
	}
}
