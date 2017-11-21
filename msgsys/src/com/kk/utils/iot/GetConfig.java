package com.kk.utils.iot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;


/**
 * 
 * @author han 解析的是default.cfg的信息
 * 
 */
public class GetConfig {

	// 得到配置文件
	public static String getServerIP(String type) {
		String returnInfo = "";
		// 获得配置文件中的ip
		Properties properties = new Properties();
		File directory1 = new File("");// 参数为空
		// 路径
		String thispath = null;
		try {
			thispath = directory1.getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.error(e);
		}
		if (System.getProperty("os.name").startsWith("Windows")) { // 如果是windows操作系统
			thispath = thispath + "\\default.cfg";
		} else { // 如果非windows操作系统 linux
			thispath = thispath + "/default.cfg";
		}

		try {
			properties.loadFromXML(new FileInputStream(thispath));
		} catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.error(e);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.error(e);
		}

		if (type.equals("messageIP")) {
			returnInfo = properties.getProperty(type); // 获取消息服务器IP
		} else if (type.equals("serverIP")) {
			returnInfo = properties.getProperty(type); // 获取服务器IP
		} else if (type.equals("appKey")) {
			returnInfo = properties.getProperty(type); // 连接的
		} else if (type.equals("secret")) {
			returnInfo = properties.getProperty(type); // 签名密钥
		} else if (type.equals("smsName")) {
			returnInfo = properties.getProperty(type); // 发短信的名称
		} else if (type.equals("smsPassword")) {
			returnInfo = properties.getProperty(type); // 发短信的密码
		} else if (type.equals("database.driver")) {
			returnInfo = properties.getProperty(type); // 驱动
		} else if (type.equals("database.url")) {
			returnInfo = properties.getProperty(type); // url地址
		} else if (type.equals("database.user")) {
			returnInfo = properties.getProperty(type); // 用户
		} else if (type.equals("database.password")) {
			returnInfo = properties.getProperty(type); // 密码
		} else if (type.equals("database1.url")) {
			returnInfo = properties.getProperty(type);
		} else if (type.equals("database1.user")) {
			returnInfo = properties.getProperty(type); // 用户
		} else if (type.equals("database1.password")) {
			returnInfo = properties.getProperty(type);
		} else if (type.equals("memCachedIP")) {
			returnInfo = properties.getProperty(type); // memCachedIP
		} else if (type.equals("serverInfoIP1")) {
			returnInfo = properties.getProperty(type); // 所有的数据入库的IP
		} else if (type.equals("serverInfoIP2")) {
			returnInfo = properties.getProperty(type); // 所有的数据入库的IP
		} else if (type.equals("consumerIP1")) {
			returnInfo = properties.getProperty(type); // 接收服务器ip
		} else if (type.equals("consumerIP2")) {
			returnInfo = properties.getProperty(type); // 接收服务器ip
		} else if (type.equals("consumerIP3")) {
			returnInfo = properties.getProperty(type); // 接收服务器ip
		} else if (type.equals("smsContentIP")) {
			returnInfo = properties.getProperty(type); // 接收服务器ip
		}
		return returnInfo;
	}

}
