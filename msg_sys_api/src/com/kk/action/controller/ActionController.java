package com.kk.action.controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kk.action.model.MessageModel;
import com.kk.action.service.IBaseLocationService;
import com.kk.action.service.IMessageService;
import com.kk.action.service.ISendIOSService;
import com.kk.action.thread.ThreadDoInfo;
import com.kk.action.thread.ThreadDoInfoService;
import com.kk.action.thread.ThreadQueueMessage;
import com.kk.mq.producer.queue.QueueSendMessage;
import com.kk.mq.producer.queue.QueueSender;
import com.kk.mq.producer.queue.RequestQueue;
import com.kk.mq.producer.topic.SendTopicReqResp;
import com.kk.mq.producer.topic.SendTopicRequest;
import com.kk.utils.DateUtil;
import com.kk.utils.gps.BaseStationModel;
import com.kk.utils.iot.BaiduAddress;

@Controller
@RequestMapping("/actionController")
public class ActionController {
	private static Log logger = LogFactory.getLog(ActionController.class);
	//域名称
	@Resource(name="queueDestinationQueue")   
	private Destination queueDestinationQueue; 
	//服务的类
	@Resource(name="queueSender")
	private QueueSender  queueSender;
	@Resource(name="sendTopicRequest")
	private SendTopicRequest sendTopicRequest;
	@Resource(name="sendTopicReqResp")
	private SendTopicReqResp sendTopicReqResp;
	@Resource(name="queueSendMessage")
	private QueueSendMessage queueSendMessage;
	@Resource(name="requestQueue")
	private RequestQueue requestQueue;
	@Resource(name="connectionFactory")
	private ConnectionFactory connectionFactory;
	@Resource(name="baseLocationService")
	private IBaseLocationService baseLocationService;
	//IOS推送服务类
	@Resource(name="sendIOSService")
	private ISendIOSService sendIOSService;
	@Resource(name="messageService")
	private IMessageService messageService;
	//提供异步发送机制,省略客户端请求等待时间
	@Resource(name = "taskExecutor")
	private ThreadPoolTaskExecutor taskExecutor;
	
	/**
	 * 提供韩的接口  业务系统
	 * @param request
	 * @param response
	 * @param topic
	 * @param message
	 * @throws IOException
	 */
	@RequestMapping(value = "/requestorRep", method = RequestMethod.POST)
	public void requestorRep(HttpServletRequest request,HttpServletResponse response,
	    @RequestParam("topic") String topic,@RequestParam("message") String message) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String error="";
		String timeout=request.getParameter("timeout");
		String phoneNumber=request.getParameter("phoneNumber");
		String msg=new String((message).getBytes("iso-8859-1"),"utf-8");
		logger.info("requestorRep方法接受消息："+msg);
		PrintWriter out = response.getWriter();
		try {
			taskExecutor.execute(new ThreadDoInfoService(msg,topic,phoneNumber,timeout,sendTopicRequest));
			//规定客户端的Id发送者以此Id返回内容
			String resultSMS="{\"RECODE\":\"0000\",\"RESULT\":\"success\"}";
			Object json = JSON.toJSON(resultSMS);
			out.print(json);
		} catch (Exception e) {
			String result =e.getCause().toString();
			error=result;
			String resError="{\"RECODE\":\"1000\",\"RESULT\":\"fail\",\"ERROR\":\""+error+"\"}";
			out.print(resError);
		}
	}
	
	/**
	 * 提供手机端的接口 手机客户端   (安防使用)
	 * @param request
	 * @param response
	 * @param topic
	 * @param message
	 * @throws IOException
	 */
	@RequestMapping(value = "/requestorRepPhone", method ={ RequestMethod.POST,RequestMethod.GET})
	public void requestorRepPhone(HttpServletRequest request,HttpServletResponse response,
	    @RequestParam("topic") String topic,@RequestParam("message") String message) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String error="";
		String msg=new String((message).getBytes("iso-8859-1"),"utf-8");
		String timeout=request.getParameter("timeout");
//		String phoneNumber=request.getParameter("phoneNumber");
		PrintWriter out = response.getWriter();
		try {
//			taskExecutor.execute(new ThreadDoInfo(msg,topic,phoneNumber,timeout,sendTopicRequest,sendTopicReqResp));
			String correlationId=UUID.randomUUID().toString().replaceAll("-", "");
			sendTopicReqResp.sendMsg(msg, topic, correlationId,timeout);
			//规定客户端的Id发送者以此Id返回内容
			String resultSMS="{\"RECODE\":\"0000\",\"RESULT\":\"success\"}";
			Object json = JSON.toJSON(resultSMS);
			out.print(json);
		} catch (Exception e) {
			String result =e.getCause().toString();
			error=result;
			String resError="{\"RECODE\":\"1000\",\"RESULT\":\"fail\",\"ERROR\":\""+error+"\"}";
			out.print(resError);
		}
	}
	
	/**
	 * 提供手机端的接口 手机客户端  此接口为(极光推送 )(安防使用)
	 * @param request
	 * @param response
	 * @param topic
	 * @param message
	 * @throws IOException
	 */
	@RequestMapping(value = "/sendMessageToPhone", method ={ RequestMethod.POST,RequestMethod.GET})
	public void sendMessageToPhone(HttpServletRequest request,HttpServletResponse response,@RequestParam("message") String message) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String error="";
		String result="";
		String msg=new String((message).getBytes("iso-8859-1"),"utf-8");
		logger.info("sendMessageToPhone接收消息内容:"+msg);
		PrintWriter out = response.getWriter();
		String deviceType="";
		String alert="";
		String sendMsg="";
		String topic="";
		try {
			JSONObject jsonObject=JSONObject.parseObject(msg);
			topic=jsonObject.getString("topic");
			sendMsg=jsonObject.getString("messageBody");
			deviceType=jsonObject.getString("deviceType");
			alert=jsonObject.getString("alert");
			result=messageService.sendMessage(topic,deviceType,alert,sendMsg);
			//规定客户端的Id发送者以此Id返回内容
			String resultSMS="{\"RECODE\":\"0000\",\"RESULT\":\""+result+"\"}";
			Object json = JSON.toJSON(resultSMS);
			out.print(json);
		} catch (Exception e) {
			result =e.getCause().toString();
			error="fail";
			String resError="{\"RECODE\":\"1000\",\"RESULT\":\"fail\",\"ERROR\":\""+error+"\"}";
			out.print(resError);
		}
	}
	/**
	 * 提供队列应答接口
	 * @param request
	 * @param response
	 * @param topic
	 * @param message
	 * @throws IOException
	 */
	@RequestMapping(value = "/requestQueue", method = RequestMethod.GET)
	public void requestQueue(HttpServletRequest request,HttpServletResponse response,
	    @RequestParam("topic") String topic,@RequestParam("message") String message) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String error="";
		String timeout=request.getParameter("timeout");
		PrintWriter out = response.getWriter();
		String msg=new String((message).getBytes("iso-8859-1"),"utf-8");
		logger.info("requestQueue接受消息："+msg);
		try {
			//规定客户端的Id发送者以此Id返回内容
			taskExecutor.execute(new ThreadQueueMessage(msg,topic,timeout,requestQueue));
			String result="success";
			String resultSMS="{\"RECODE\":\"0000\",\"RESULT\":\""+result+"\"}";
			Object json = JSON.toJSON(resultSMS);
			out.print(json);
		} catch (Exception e) {
			String result =e.getCause().toString();
			error=result;
			String resError="{\"RECODE\":\"1000\",\"RESULT\":\"fail\",\"ERROR\":\""+error+"\"}";
			out.print(resError);
		}
	}
	/**
	 * 提供倪的接口
	 * @param request
	 * @param response
	 * @param topic
	 * @param message
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/serviceTopic", method = RequestMethod.POST)
	public void serviceTopic(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("topic") String topic,
										 @RequestParam("message") String message) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String timeout=request.getParameter("timeout");
		String error="";
		try {
			//规定客户端的Id发送者以此Id返回内容
			String correlationId=UUID.randomUUID().toString().replaceAll("-", timeout);
			String result=sendTopicRequest.request(message, topic,correlationId,"");
			String resultSMS="{\"RECODE\":\"0000\",\"RESULT\":\""+result+"\"}";
			Object json = JSON.toJSON(resultSMS);
			out.print(json);
		} catch (Exception e) {
			String result =e.getCause().toString();
			error=result;
			String resError="{\"RECODE\":\"1000\",\"RESULT\":\"fail\",\"ERROR\":\""+error+"\"}";
			out.print(resError);
		}
	}
	   /**
		 * 发送消息到队列          (安防使用,目前停用)
		 * @param message
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/queueSendMessage", method = RequestMethod.GET)
		public void queueSendMessage(HttpServletRequest request,
				HttpServletResponse response,@RequestParam("topic") String topic,
				@RequestParam("message") String message) throws Exception{
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
				String error="";
				PrintWriter out = response.getWriter();
			try {
//				queueSender.send(queueDestinationQueue, message);
				String msg=new String((message).getBytes("iso-8859-1"),"utf-8");
//				String result=queueSendMessage.sendMsg(msg, topic);
				String result=queueSendMessage.produce(msg, topic);
				
				String resSuccess="{\"RECODE\":\"0000\",\"RESULT\":\""+result+"\"}";
				Object json = JSON.toJSON(resSuccess);
				out.print(json);
			} catch (Exception e) {
				String result =e.getCause().toString();
				error=result;
				String resError="{\"RECODE\":\"1000\",\"RESULT\":\"fail\",\"ERROR\":\""+error+"\"}";
				out.print(resError);
			}
		}
		/**
		 * 发送消息到主题
		 * @param message
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/topicSender", method = RequestMethod.POST)
		public void topicSender(HttpServletRequest request,
				HttpServletResponse response,@RequestParam("message") String message,
											 @RequestParam("topic") String topic) throws Exception{
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			String error="";
			String success="发送成功";
			PrintWriter out = response.getWriter();
			try {
//				向指定的topic发送主题
//				topicSender.send(queueDestinationTopic, message);
				//单例模式获取连接
				Connection connection=connectionFactory.createConnection();
				connection.start();
				Session session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				Topic topicp = session.createTopic(topic);  
	            MessageProducer producer = session.createProducer(topicp);  
	            //持久化保存数据库
	            producer.setDeliveryMode(DeliveryMode.PERSISTENT);  
	            TextMessage msg = session.createTextMessage();  
	            msg.setText(message);  
	            producer.send(msg);  
	            logger.info("topicSender发送的消息:" + msg.getText());  
	            String resSuccess="{\"RECODE\":\"0000\",\"RESULT\":\""+success+"\"}";
				Object json = JSON.toJSON(resSuccess);
				out.print(json);
			} catch (Exception e) {
				String result =e.getCause().toString();
				error=result;
				String resError="{\"RECODE\":\"1000\",\"RESULT\":\"fail\",\"ERROR\":\""+error+"\"}";
				out.print(resError);
			}
		}
		/**
		 * 基站定位       (安防使用)
		 * @param message
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/getAddressByGSM", method = RequestMethod.POST)
		public void getAddressByGSM(HttpServletRequest request,
				HttpServletResponse response,@RequestParam("message") String message)  throws IOException{
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			String error="";
			PrintWriter out = response.getWriter();
			try {
				JSONObject json=JSONObject.parseObject(message);
				JSONArray  array=json.getJSONArray("baseLocation");
				List<BaseStationModel> listBase=new ArrayList<BaseStationModel>();
				for (int i = 0; i < array.size(); i++) {
					JSONObject  jsonin=JSONObject.parseObject(array.get(i).toString());
					BaseStationModel models=new BaseStationModel();
					if("".equals(jsonin.getString("MCC"))||jsonin.getString("MCC")==null){
						continue;
					}
					if("".equals(jsonin.getString("MNC"))||jsonin.getString("MNC")==null){
						continue;
					}
					if("".equals(jsonin.getString("LAC"))||jsonin.getString("LAC")==null){
						continue;
					}
					if("".equals(jsonin.getString("CELLID"))||jsonin.getString("CELLID")==null){
						continue;
					}
					if("".equals(jsonin.getString("RX_LEVEL"))||jsonin.getString("RX_LEVEL")==null){
						continue;
					}
					models.setMcc(jsonin.getString("MCC"));
					models.setMnc(jsonin.getString("MNC"));
					models.setLac(jsonin.getString("LAC"));
					models.setCell(jsonin.getString("CELLID"));
					models.setRxlevel(jsonin.getString("RX_LEVEL"));
					listBase.add(models);
				}
				if(listBase==null || "".equals(listBase)){
					String resSuccess="{\"RECODE\":\"1000\",\"RESULT\":\"fail\",\"ERROR\":\"传入参数全部为空,不符合要求\"}";
					out.print(resSuccess);
				}
				//返回的结果中包含有rxlevel可能不是准确的,如果有需要可以只返回地址和经纬度.
				BaseStationModel reModel=baseLocationService.queryBaseStation(listBase);
				String resSuccess="{\"RECODE\":\"0000\",\"RESULT\":\"success\",\"BODY\":{\"lat\":\""+reModel.getLat()+"\",\"lng\":\""+reModel.getLng()+"\",\"address\":\""+reModel.getAddress()+"\"}}";
				out.print(resSuccess);
			} catch (Exception e) {
				String result =e.getCause().toString();
				error=result;
				String resError="{\"RECODE\":\"1000\",\"RESULT\":\"fail\",\"ERROR\":\""+error+"\"}";
				out.print(resError);
			}
		}
		/**
		 * GPS定位
		 * @param message
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/getAddressByGPS", method = {RequestMethod.POST,RequestMethod.GET})
		public void getAddressByGPS(HttpServletRequest request,
				HttpServletResponse response,@RequestParam("message") String message)  throws IOException{
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			String error="";
			PrintWriter out = response.getWriter();
			try {
				JSONObject json=JSONObject.parseObject(message);
				String latitude=json.getString("latitude");
				String longitude=json.getString("longitude");
				String address=BaiduAddress.getAddress(latitude, longitude);
				String resSuccess="{\"RECODE\":\"0000\",\"RESULT\":\"success\",\"BODY\":{\"address\":\""+address+"\"}}";
				Object res = JSON.toJSON(resSuccess);
				logger.info("getAddressByGPS返回的内容:"+res);
				out.print(res);
			} catch (Exception e) {
				String result =e.getCause().toString();
				error=result;
				String resError="{\"RECODE\":\"1000\",\"RESULT\":\"fail\",\"ERROR\":\""+error+"\"}";
				out.print(resError);
			}
		}
		
		/**
		 *  Send IOS   (安防使用)需要改造
		 * @param message
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/sendIOS", method = {RequestMethod.GET,RequestMethod.POST})
		public void sendIOS(HttpServletRequest request,
				HttpServletResponse response,@RequestParam("sound") String sound,
				@RequestParam("alert") String alert,@RequestParam("message") String message,
				@RequestParam("deviceToken") String deviceToken)  throws IOException{
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			message=new String((message).getBytes("iso-8859-1"),"utf-8");
			alert=new String((alert).getBytes("iso-8859-1"),"utf-8");
			String error="";
			String success="success";
			PrintWriter out = response.getWriter();
			if("no".equals(sound)){
				sound=null;
			}
			try {
				sendIOSService.sendMessageForIOS(alert,sound,message,deviceToken);
				String resSuccess="{\"RECODE\":\"0000\",\"RESULT\":\""+success+"\"}";
				out.print(resSuccess);
			} catch (Exception e) {
				String result =e.getCause().toString();
				error=result;
				String resError="{\"RECODE\":\"1000\",\"RESULT\":\"fail\",\"ERROR\":\""+error+"\"}";
				out.print(resError);
			}
		}
		
		/**
		 *  Send IOS   (安防使用)需要改造
		 * @param message
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/sendIOSList", method = {RequestMethod.POST,RequestMethod.GET})
		public void sendIOSList(HttpServletRequest request,
				HttpServletResponse response,@RequestParam("message") String message
				)  throws IOException{
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			message=new String((message).getBytes("iso-8859-1"),"utf-8");
			String error="";
			String success="success";
			PrintWriter out = response.getWriter();
			try {
				//接受的消息
				JSONObject json=JSONObject.parseObject(message);
				JSONArray array=json.getJSONArray("tokens");
				List<MessageModel> list=new ArrayList<MessageModel>();
				for (int i = 0; i < array.size(); i++) {
					MessageModel model=new MessageModel();
					JSONObject jsonstr = array.getJSONObject(i);
					model.setTokenId(jsonstr.getString("tokenId"));
					model.setMessage(jsonstr.getString("msg"));
					list.add(model);
				}
				sendIOSService.sendMessageForIOS(list);
				String resSuccess="{\"RECODE\":\"0000\",\"RESULT\":\""+success+"\"}";
				out.print(resSuccess);
			} catch (Exception e) {
				String result =e.getCause().toString();
				error=result;
				String resError="{\"RECODE\":\"1000\",\"RESULT\":\"fail\",\"ERROR\":\""+error+"\"}";
				out.print(resError);
			}
		}
		
		
		@ResponseBody
		@RequestMapping(value = "/updateMessageStatus", method = {RequestMethod.POST,RequestMethod.GET})
		public void updateMessageStatus(HttpServletRequest request,
				HttpServletResponse response,@RequestParam("messageId") String messageId
				)  throws IOException{
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			String error="";
			String success="fail";
			PrintWriter out = response.getWriter();
			try {
				//此messageId是极光返回的msg_id
				logger.info(DateUtil.getCurrentDateTime()+"接受手机端回调的jpushId:"+messageId);
				success=messageService.updateMsgStatusByJpushId(messageId);
				String resSuccess="{\"RECODE\":\"0000\",\"RESULT\":\""+success+"\"}";
				out.print(resSuccess);
			} catch (Exception e) {
				String result =e.getCause().toString();
				error=result;
				String resError="{\"RECODE\":\"1000\",\"RESULT\":\"fail\",\"ERROR\":\""+error+"\"}";
				out.print(resError);
			}
		}
		
		
}











