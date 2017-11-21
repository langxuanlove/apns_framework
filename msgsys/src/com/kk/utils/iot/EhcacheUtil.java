package com.kk.utils.iot;

import java.io.File;
import java.io.IOException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcacheUtil {
	static String path = "";
	static {
		File directory1 = new File("");// 参数为空
		// 路径
		// String path = "";
		try {
			path = directory1.getCanonicalPath();
			if (System.getProperty("os.name").startsWith("Windows")) { // 如果是windows操作系统
				path = path + "\\ehcache.xml";
			} else { // 如果非windows操作系统 linux
				path = path + "/ehcache.xml";
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static final CacheManager manager = new CacheManager(path);

	// private static final Cache cache = manager.getCache("devicesCache");

	public static Cache getCache(String cahceName) {
		return manager.getCache(cahceName);
	}

	// 添加緩存
	public static void put(String key, Object val, String cahceName) {
		try {
			Element e = new Element(key, val);
			getCache(cahceName).put(e);
		} catch (Exception e) {
			
		}
	}

	// 刪除緩存
	public static void remove(String key, String cahceName) {
		getCache(cahceName).remove(key);
	}

	// 替換緩存
	public static void replace(String key, Object val, String cahceName) {
		Element e = new Element(key, val);
		getCache(cahceName).remove(key);
		getCache(cahceName).put(e);
	}

	// 根据key得到值
	public static Object get(String key, String cahceName) {
		Element e = getCache(cahceName).get(key);
		Object val = null;
		if (e == null) {
			val = null;
		} else {
			val = e.getObjectValue();
		}
		return val;
	}

	public void close() {
		manager.shutdown();
	}



	/**
	 * @param args
	 */
/*	public static void main(String[] args) {
		EhcacheUtil.put("name", "sssssss", "messageCache");
		EhcacheUtil.put("password", "22222", "messageCache");

		System.out.println(EhcacheUtil.get("name", "messageCache") + "   "
				+ get("password", "messageCache"));

		EhcacheUtil.replace("name", "555", "messageCache");
		EhcacheUtil.replace("password", "6666", "messageCache");

		EhcacheUtil.remove("name", "messageCache");

		System.out.println(EhcacheUtil.get("name", "messageCache") + "   "
				+ get("password", "messageCache"));
	}*/
}
