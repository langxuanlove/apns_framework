package com.kk.utils.activemq;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class ActivemqHttpSend {
	public static final Logger LOG = LoggerFactory.getLogger(ActivemqHttpSend.class);
	 private static ActivemqHttpSend instance=null;
	    public static ActivemqHttpSend getInstance(){
	        if(instance==null){
	            synchronized(ActivemqHttpSend.class){
	                if(instance==null){
	                    instance=new ActivemqHttpSend();
	                }
	            }
	        }
	        return instance;
	    }
	    private ActivemqHttpSend(){}
	/**
	 * 描述信息:进行http请求
	 * 
	 * @param psHttpUrl
	 *            http访问路径
	 * 
	 * @return http响应数据
	 */
	@SuppressWarnings({ "resource", "deprecation" })
	public String sendHttpReqeust(String psHttpUrl,String params) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		// HTTP请求
		HttpPost request = new HttpPost(psHttpUrl);
		try {
			//需要封装json数据
			 StringEntity param=new StringEntity(params, "UTF-8");
			 request.addHeader("content-type", "application/json");  
			 request.setEntity(param); 
			//设置权限验证的地方
			httpClient.getCredentialsProvider().setCredentials(
					new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT,
							AuthScope.ANY_REALM),
					new UsernamePasswordCredentials("admin", "admin"));
			httpClient.getParams().setParameter(
					CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
			// 发送请求，返回响应
			HttpResponse response = httpClient.execute(request);
			HttpEntity httpEntity = response.getEntity();
			// 得到相应正文
			String sReturnHtml = new String(EntityUtils.toString(httpEntity));
			request.abort();
			return sReturnHtml;
		} catch (ClientProtocolException e) {
			// 协议错误
			e.printStackTrace();
		} catch (IOException e) {
			// 网络异常
			e.printStackTrace();
		}
		return null;
	}
	public String sendMessage(String IP,String Port,String topic,String topicType,String message){
		ActivemqHttpSend httpTools = ActivemqHttpSend.getInstance();
		JSONObject json = null;
		String result="fail";
		String res="";
		try {
			json=JSONObject.parseObject(message);
			String url="http://"+IP+":"+Port+"/api/message/"+topic+"?type="+topicType;
			res=httpTools.sendHttpReqeust(url,json.toString());
			LOG.info("返回的消息:"+res);
			if(res.contains("Message")&res.contains("sent")){
				result= "success";
			}else{
				result= "fail";
			}
		} catch (Exception e) {
			LOG.info("json格式错误！");
			result="fail";
		}
		return result;		
	}
	public String sendMessage(String address,String topic,String topicType,String message){
		ActivemqHttpSend httpTools = ActivemqHttpSend.getInstance();
		JSONObject json = null;
		String result="fail";
		String res="";
		try {
			json=JSONObject.parseObject(message);
			String url="http://"+address+"/api/message/"+topic+"?type="+topicType;
			res=httpTools.sendHttpReqeust(url,json.toString());
			LOG.info("返回的消息:"+res);
			if(res.contains("Message")&res.contains("sent")){
				result= "success";
			}else{
				result= "fail";
			}
		} catch (Exception e) {
			LOG.info("json格式错误！");
			result="fail";
		}
		return result;		
	}
	public static void main(String[] args) {
		ActivemqHttpSend httpTools = ActivemqHttpSend.getInstance();
		//demo1为topic或者queue的名称 ,type=topic/queue判断发送的域名是什么,body为发送的参数
//		String a = httpTools
//				.sendHttpReqeust("http://192.168.4.52:8161/api/message/iphone_request?type=topic&body=sdf");
		String a = httpTools.sendMessage("192.168.4.52", "8161", "iphone_request", "topic", "{\"message\":\"sdf\",\"time\":\"2015-2\"}");
		System.out.println(a);

	}
}
