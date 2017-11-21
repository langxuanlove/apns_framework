package com.kk.ws.impl;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.annotations.UseAsyncMethod;
import org.springframework.stereotype.Component;

import com.kk.action.service.ITestService;
import com.kk.ws.IWsCommon;

@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
@WebService(serviceName = "WebService", portName = "commonServicePort", endpointInterface = "com.gnet.ws.IWsCommon", targetNamespace = "http://webService.cn.com/")
@Component(value = "wsCommon")
public class WsCommonImpl implements IWsCommon {

	@Resource(name = "testService")
	private ITestService testService;
	
	@Override
	@UseAsyncMethod
	public String check(String psJson) {
		System.out.println("参数："+psJson);
		return "OK";
	}

}
