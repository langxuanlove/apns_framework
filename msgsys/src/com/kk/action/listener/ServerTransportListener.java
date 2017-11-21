package com.kk.action.listener;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.activemq.transport.TransportListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
public class ServerTransportListener implements TransportListener {

  protected final Logger logger = LoggerFactory.getLogger(ServerTransportListener.class);
  /**
   * 对消息传输命令进行监控
   */
  public void onCommand(Object o) {
    logger.debug("onCommand检测到服务端命令:{}", o);
    System.out.println("onCommand检测到服务端命令:{}"+o.toString());
  }

  /**
   * 与服务器连接发生错误
   *
   * @param error
   */
  @Override
  public void onException(IOException arg0) {
	  System.out.println("与服务器连接发生错误......");
  }

  /**
   * 消息服务器连接发生中断
   */
  public void transportInterupted() {
	System.out.println("transportInterupted,与服务器连接发生中断......");
    logger.error("transportInterupted,与服务器连接发生中断......");
  }

  /**
   * 恢复与服务器的连接
   */
  public void transportResumed() {
	System.out.println("transportResumed,恢复与服务器连接....");
    logger.info("transportResumed,恢复与服务器连接....");
  }
}