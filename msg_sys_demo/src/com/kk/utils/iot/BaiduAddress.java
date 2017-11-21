package com.kk.utils.iot;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class BaiduAddress {
	// 百度反解析地址信息
	public static String getAddress(String latitude, String longitude)
			throws Exception {
		String address = "";
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"http://api.map.baidu.com/geocoder?location=" + latitude + ","
						+ longitude + "&coord_type=gcj02&qq-pf-to=pcqq.c2c");
		HttpResponse response = httpClient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		if (null != entity) {
			InputStream in = entity.getContent();// 将返回的内容流入输入流内
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(in);// 用输入流实例化Document
			// 解析树节点
			Element rootElement = document.getDocumentElement();
			NodeList timeNode = rootElement.getElementsByTagName("status");
			if (timeNode.item(0).getTextContent().equals("OK")) {
				NodeList rresultNode = rootElement
						.getElementsByTagName("result");
				NodeList classNode = rresultNode.item(0).getChildNodes();
				address = classNode.item(3).getTextContent();
			} else {
				address = longitude + "," + latitude;
			}

			EntityUtils.consume(entity);
			httpGet.releaseConnection();
		}

		return address;
	}
}
