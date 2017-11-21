package com.kk.utils;

import java.beans.Encoder;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
	/**
	 * http请求示例
	 * @param httpUrl
	 * @param httpArg
	 * @return
	 */
	public static String request(String httpUrl, String httpArg) {		
	    BufferedReader reader = null;		
	    String result = null;		
	    StringBuffer sbf = new StringBuffer();		
	    httpUrl = httpUrl + "?" + httpArg;		
	    try {		
	        URL url = new URL(httpUrl);	
	        HttpURLConnection connection = (HttpURLConnection) url		
	                .openConnection();		
	        connection.setRequestMethod("POST");		
	        connection.connect();	
	        connection.setReadTimeout(2000);
	        InputStream is = connection.getInputStream();		
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));		
	        String strRead = null;		
	        while ((strRead = reader.readLine()) != null) {		
	            sbf.append(strRead);		
	            sbf.append("\r\n");		
	        }		
	        reader.close();		
	        result = sbf.toString();		
	    } catch (Exception e) {		
	        e.printStackTrace();		
	    }		
	    return result;		
	}	
	public static void main(String[] args) {
		String URL = "http://192.168.1.94:8086/supernove/mbclient";
		String params = "{\"at\":\"createHealthReport\",\"vs\":\"14\",\"username\":\"lizhaoxia\"}";
		String messageString=java.net.URLEncoder.encode("{\"MessageHead\":\"+RESP:GTSOS==_)(*&^%$#@!;/*/\",\"ProtocolVersion\":\"1A0102\",\"UniqueID\":\"860599000057489\",\"DeviceName\":\"GL300\",\"ReportID\":\"0\",\"ReportType\":\"0\",\"Number\":\"1\",\"GPSAccuracy\":\"4\",\"Speed\":\"22.3\",\"Azimuth\":\"194\",\"Altitude\":\"96.9\",\"Longitude\":\"116.327874\",\"Latitude\":\"39.961480\",\"GPSUTCtime\":\"20141228111153\",\"MCC\":\"0460\",\"MNC\":\"0000\",\"LAC\":\"102C\",\"CellID\":\"8CCE\",\"OdoMileage\":\"388.5\",\"batteryPercentage\":\"98\",\"SendTime\":\"20141228111154\",\"CountNumber\":\"32B5$\"}");
//		String httpUrl = "http://114.112.90.40:9928/Gnet_Info/web/actionController/requestorRepPhone";
		String httpUrl = "http://localhost:8080/Gnet_Info/web/actionController/requestorRepPhone";
//		String httpArg = "topic=123&message="+messageString+"&timeout=2000";
		//String httpArg = "topic=11111111111&message={\"at\":\"createHealthReport\",\"vs\":\"14\",\"username\":\"lizhaoxia\"}";	
//		System.out.println("httpArg:"+httpArg);
//		String jsonResult = request(URL, httpArg);
//		System.out.println(jsonResult);
		String URL1 = "http://localhost:8080/Gnet_Info/web/actionController/sendIOS";
		String message="sound=default&deviceToken=3046a5b09ade4febf2530fe01fd5140efda7cb019dd8cbf3bb&message={\"uniqueId\":\"865662000605007\",\"latitude\":\"39.98848343\",\"longitude\":\"116.34983826\",\"address\":\"北京市海淀区成府路20号院\",\"nickName\":\"111\",\"sendTime\":\"2015-11-20 14:50:04\",\"type\":\"18\",\"DevNam\":\"G3\",\"FirmVer\":\"G30100\",\"ProVer\":\"G30100\",\"MovFlag\":\"0\",\"BatPerc\":\"99\",\"SensFlag\":\"0\",\"GSMLevel\":\"25\",\"locationType\":\"GSM\"}&alert=测试报告,111,2015-11-20 14:50:04,北京市海淀区成府路20号院";
		String jsonResult1 = request(URL1, message);
//		"http://192.168.4.52:8161/api/message/demo1?type=topic&body=messag1212&username=admin&password=admin"
//		request("http://192.168.4.52:8161/api/message/demo1","type=topic&body=messag1212&username=admin&password=admin");
//		System.out.println(jsonResult1);
//		for (int i = 0; i < 10; i++) {
//			System.out.println(HttpUtils.request(URL1, message));
//		}
//			String httpArg="topic=123123&message={test!}";
//			System.out.println(request("http://localhost:8080/Gnet_Info/web/actionController/requestorRepPhone", httpArg));
			
//		String a="message={\"topic\":\"15910627161\",\"messageBody\":{\"uniqueId\":\"860599000059923\",\"latitude\":\"39.98833646279974\",\"longitude\":\"116.3545014167476\",\"address\":\"北京市海淀区学院路26号\",\"nickName\":\"我的h3\",\"sendTime\":\"2016-03-02 15:24:24\",\"type\":\"1\",\"BatPerc\":\"19\",\"locationType\":\"GPS\"},\"deviceType\":\"Android\",\"alert\":\"\"}";	
//		System.out.println(request("http://114.112.90.40:9928/Gnet_Info/web/actionController/sendMessageToPhone", a));
	}
	
}
