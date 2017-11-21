package com.kk.action.thread;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 获取json格式的字符串
 * @author chixianpeng
 * @date 2014-12-17 18:47
 */
public class ParseStrToJsonUtil {

	/**
	 * 解析成json格式
	 * @param result
	 * @return
	 */
	public static String parseStrToJsonUtil(String result){
		if(result.indexOf("{") != -1 || result.indexOf("}") != -1){
			return result.substring(result.indexOf("{"),result.lastIndexOf("}") + 1);
		}
		return null;
	}
	
	/**
	 * 获取短信内容
	 * @return
	 */
	public static String getSmsContent(String jsonStr){
		if(jsonStr != null){
			String json = parseStrToJsonUtil(jsonStr);
			JSONObject jo = JSON.parseObject(json);
			StringBuilder sb = new StringBuilder();
			String smsContent = "";
			if(jo.getString("type").equals(Constants.SOS)){
				if (jo.containsKey("address")) {
					smsContent = sb.append("报警:手动报警")
							.append(",").append("您好您绑定的设备：" + jo.getString("nickName"))
							.append("在"+jo.getString("sendTime")+"在"+jo.getString("address")+"发生报警。")
							.append("【坐标派】").toString();
				}else {
					smsContent = sb.append("报警:手动报警")
							.append(",").append("您好您绑定的设备：" + jo.getString("nickName"))
							.append("在"+jo.getString("sendTime")+"发生报警。但暂时无法获取设备位置")
							.append("【坐标派】").toString();
				}
				
			}else if(jo.getString("type").equals(Constants.close)){
				smsContent = sb.append("关机事件")
						.append(",").append("您好您绑定的设备：" + jo.getString("nickName"))
						.append("在"+jo.getString("sendTime")+"时间关机了。")
						.append("【坐标派】").toString();
			}else if(jo.getString("type").equals(Constants.notOnline)){
				smsContent = sb.append("设备不在线")
						.append(",").append("您好您绑定的设备：" + jo.getString("nickName"))
						.append("在"+jo.getString("sendTime")+"不在线了。")
						.append("【坐标派】").toString();
			}else if(jo.getString("type").equals(Constants.batteryLow)){
				if (jo.containsKey("address")) {
					smsContent = sb.append("设备电池电量低")
							.append(",").append("您好您绑定的设备：" + jo.getString("nickName"))
							.append("在"+jo.getString("sendTime")+"在"+jo.getString("address")+"电池电量低。")
							.append("【坐标派】").toString();
				}else {
					smsContent = sb.append("设备电池电量低")
							.append(",").append("您好您绑定的设备：" + jo.getString("nickName"))
							.append("在"+jo.getString("sendTime")+"电池电量低了。")
							.append("【坐标派】").toString();
				}
				
			}else if(jo.getString("type").equals(Constants.illegalOpenDoor)){
				smsContent = sb.append("报警:开关输入报警")
						.append(",").append("您好您绑定的设备：" + jo.getString("nickName"))
						.append("在"+jo.getString("sendTime")+"在"+jo.getString("address")+"开关输入报警。")
						.append("【坐标派】").toString();
			}else if(jo.getString("type").equals(Constants.beyondSafetyArea)){
				smsContent = sb.append("报警:超出安全区域")
						.append(",").append("您好您绑定的设备：" + jo.getString("nickName"))
						.append("在"+jo.getString("sendTime")+"在"+jo.getString("address")+"超出安全区域。")
						.append("【坐标派】").toString();
			}else if(jo.getString("type").equals(Constants.inSafetyArea)){
				smsContent = sb.append("报警:进入安全区域")
						.append(",").append("您好您绑定的设备：" + jo.getString("nickName"))
						.append("在"+jo.getString("sendTime")+"在"+jo.getString("address")+"进入安全区域。")
						.append("【坐标派】").toString();
			}else if(jo.getString("type").equals(Constants.open)){
				smsContent = sb.append("开机事件")
						.append(",").append("您好您绑定的设备：" + jo.getString("nickName"))
						.append("在"+jo.getString("sendTime")+"时间开机了。")
						.append("【坐标派】").toString();
			}else if(jo.getString("type").equals(Constants.VIB)){
				if (jo.containsKey("address")) {
					smsContent = sb.append("报警:震动报警")
							.append(",").append("您好您绑定的设备：" + jo.getString("nickName")).append(",")
							.append(jo.getString("sendTime")+"在"+jo.getString("address")+"发生震动报警。")
							.append("【坐标派】").toString();
				}else {
					smsContent = sb.append("报警:震动报警")
							.append(",").append("您好您绑定的设备：" + jo.getString("nickName")).append(",")
							.append(jo.getString("sendTime")+"发生震动报警。但暂时无法获取设备位置")
							.append("【坐标派】").toString();
				}
				
			}else if(jo.getString("type").equals(Constants.TESTREPORT)){
				smsContent = sb.append("测试报告：")
						.append("您好您绑定的设备：" + jo.getString("nickName"))
						.append("现在电量是: "+ jo.getString("BatPerc")).append("%")
						.append("设备工作正常")
						.append("【坐标派】").toString();
			}/*else if(jo.getString("type").equals(Constants.REALLOCATION)){
				smsContent = sb.append("实时位置")
						.append(",").append("您好您绑定的设备：" + jo.getString("uniqueId"))
						.append("在"+jo.getString("sendTime")+"时间上报当前实时位置是").append(jo.getString("address"))
						.append("【坐标派】").toString();
			}*/else if(jo.getString("type").equals(Constants.OPEN_SUCCESS)){
				smsContent = sb.append("您好您绑定的设备：" + jo.getString("nickName"))
							.append("开启追踪模式成功")
						    .append("【坐标派】").toString();
			}else if(jo.getString("type").equals(Constants.CLOSE_SUCCESS)){
				smsContent = sb.append("您好您绑定的设备：" + jo.getString("nickName"))
						.append("关闭追踪模式成功")
					    .append("【坐标派】").toString();
	     	}else if(jo.getString("type").equals(Constants.ChangeFrequency_SUCCESS)){
				smsContent = sb.append("您好您绑定的设备：" + jo.getString("nickName"))
						.append("设备定位频率成功")
					    .append("【坐标派】").toString();
	     	}else if(jo.getString("type").equals(Constants.DeviceSetting_SUCCESS)){
				smsContent = sb.append("您好您绑定的设备：" + jo.getString("nickName"))
						.append("设备设置成功")
					    .append("【坐标派】").toString();
	     	}
			return smsContent;
		}
		return null;
	}
	
	public static void main(String[] args) {
		try {
			String llString="{\"uniqueId\":\"860599000058289\",\"latitude\":\"0\",\"longitude\":\"0\",\"address\":\"北京市朝阳区皂君庙路5号\",\"sendTime\":\"2014-12-17 17:54:45\",\"type\":\"1\",\"locationType\":\"GPS/GSM\",\"nickName\":\"设备1号\"}";
			String smsContent = getSmsContent(llString);
			System.out.println("smsContent=="+smsContent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
