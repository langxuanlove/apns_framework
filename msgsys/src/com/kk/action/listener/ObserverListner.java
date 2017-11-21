package com.kk.action.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import activemq.Watched;
//@Service
public class ObserverListner implements Observer {
	@Resource(name="jdbcTemplateInfo")
	private  JdbcTemplate jdbcTemplateInfo;
	private Watched watched = Watched.getInstance();

	/***
	 * 把自己注入到topic中实际也就是Watcherd类中
	 * 
	 * @param o
	 */
	public ObserverListner() {
		watched.addObserver(this);
		System.out.println("MyObserver:观察者数量：" + watched.countObservers());
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("MyObserver观察者获取的消息为：");
		System.out.println(((Watched) o).getData());
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		list=jdbcTemplateInfo.queryForList("select count(*) from message_info where message_info.status=?",new Object[]{"0"});
		String jsonObject=JSONObject.toJSON(list).toString();
		System.out.println("返回的数据为:"+jsonObject);
		System.out.println("MyObserver观察者结束获取消息");
	}
}
