package com.kk.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

/**
 * <table width="100%" border="1px">
 * <tr>
 * <td width="20%">作者</td><td width="80%" colspan="2">sam</td>
 * </tr>
 * <tr>
 * <td width="20%">版本</td><td width="80%" colspan="2">v1.0</td>
 * </tr>
 * <tr>
 * <td width="20%">创建日期</td><td width="80%" colspan="2">2013-06-24</td>
 * </tr>
 * <tr>
 * <td width="100%" colspan="3">修订记录:</td>
 * <tr>
 * <td width="20%">修改日期</td><td width="20%">修改人</td><td width="60%">修改记录</td>
 * </tr>
 * <tr>
 * <td width="20%">-------</td><td width="20%">-------</td><td width="60%">--------------</td>
 * </tr>
 * <tr>
 * <td width="20%">描述信息</td><td width="80%" colspan="2">对http请求访问，返回http内容</td>
 * </tr>
 * </tr>
 * </table>
 */
public class HttpUtil {

	/**
	 * 描述信息:进行http请求
	 * 
	 * @param psHttpUrl
	 * 				http访问路径
	 * 
	 * @return
	 * 				http响应数据
	 */
	@SuppressWarnings({ "resource", "deprecation" })
	public static String sendHttpReqeust(String psHttpUrl) {
		HttpClient httpClient = new DefaultHttpClient();
		// HTTP请求
		HttpUriRequest request = new HttpPost(psHttpUrl);
		try {
			httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
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
}
