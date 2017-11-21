package com.kk.utils;

import java.text.SimpleDateFormat;

public class SendSMS {

	/**
	 * 发送短信消息的接口
	 * @throws Exception
	 */
	public static String  sendSMS(String phoneNumber,String text)throws Exception{
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String result = "";
		String resultSMS = "";
		String nameSpace = "http://gnet.cn.com/";
		String sms_address = PropertiesUtil.getKeyValue("SMS_URL");;
		String str="{\"sendType\":\"0\",\"keyOfQM\":\"QMSendMsgKEYTEC1001\",\"smsMob\":\""+phoneNumber+"\"," +
			   "\"smsText\":\""+text+"\"}";
		resultSMS = AxisUtil.callService(sms_address, nameSpace, "QM_SendMsgOrEmail",
				"psJsonStr", str);
		if("0000".equals(resultSMS)){
			result="success";
		}
		if("0001".equals(resultSMS)){
			result="fail";
		}
		return result;
	};
//	public static void main(String[] args) throws Exception {
//		//接受手机号smsMob注：多个号码用“，”隔开
//		System.out.println(SendSMS.sendSMS("18911132523,13643230918"));
//	}
}
