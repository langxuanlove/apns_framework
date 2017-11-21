package com.kk.action.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.kk.action.service.ITestService;
import com.kk.mq.consumer.queue.QueueReceiver;
import com.kk.mq.consumer.topic.TopicReceiver;
import com.kk.mq.producer.queue.QueueSender;
import com.kk.mq.producer.queue.SessionQueueSender;
import com.kk.mq.producer.topic.RequestorR;
import com.kk.mq.producer.topic.SendTopicRequest;
import com.kk.mq.producer.topic.TopicSender;

@Controller
@RequestMapping("/actionControllerDemo")
public class ActionControllerDemo {
	@Resource(name = "testService")
	private ITestService testService;
	//域名称
	@Resource(name="sessionAwareQueue")
	private Destination sessionAwareQueue;
	@Resource(name="queueDestinationQueue")   
	private Destination queueDestinationQueue; 
	@Resource(name="queueDestinationTopic")
	private Destination queueDestinationTopic;
	//服务的类
	@Resource(name="sessionQueueSender")
	private SessionQueueSender  sessionQueueSender;
	@Resource(name="queueSender")
	private QueueSender  queueSender;
	@Resource(name="topicSender")
	private TopicSender topicSender;
	@Resource(name="queueReceiver")
	private QueueReceiver queueReceiver;
	@Resource(name="topicReceiver")
	private TopicReceiver topicReceiver;
	@Resource(name="sendTopicRequest")
	private SendTopicRequest sendTopicRequest;
	@Resource(name="queueSenderRep")
	private RequestorR queueSenderRep;
	
	@RequestMapping(value = "/getTasks", method = RequestMethod.GET)
	public void getTasks(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		List<Map<String, Object>> _list = testService.getTaskAll();
		Object json = JSON.toJSON(_list);
		PrintWriter out = response.getWriter();
		out.print(json);
	}
	
	 /**
	 * 发送消息到队列
	 * @param message
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queueSender", method = RequestMethod.GET)
	public String queueSender(@RequestParam("message") String message) {
		String opt = "";
		try {
			queueSender.send(queueDestinationQueue, message);
			opt = "suc";
		} catch (Exception e) {
			opt = e.getCause().toString();
		}
		return opt;
	}
	/**
	 * 发送消息到队列
	 * @param message
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sessionQueueSender", method = RequestMethod.GET)
	public String sessionQueueSender(@RequestParam("message") String message) {
		String opt = "";
		try {
			sessionQueueSender.send(sessionAwareQueue, message);
			opt = "success!";
		} catch (Exception e) {
			opt = e.getCause().toString();
		}
		return opt;
	}
	/**
	 * 发送消息到主题
	 * @param message
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/topicSender", method = RequestMethod.GET)
	public String topicSender(@RequestParam("message") String message) {
		String opt = "";
		try {
			topicSender.send(queueDestinationTopic, message);
			opt = "suc";
		} catch (Exception e) {
			opt = e.getCause().toString();
		}
		return opt;
	}
	@ResponseBody
	@RequestMapping(value = "/topicSenderRep", method = RequestMethod.GET)
	public void topicSenderRep(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("message") String message) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String opt = "";
		try {
			opt=sendTopicRequest.request("queue is send !", "queue", "","");
			Object json = JSON.toJSON(opt);
			PrintWriter out = response.getWriter();
			out.print(json);
//			opt = queueSenderRep.request("queue is demo test !", "queue");
		} catch (Exception e) {
			opt = e.getCause().toString();
		}
//		return opt;
	}
}
