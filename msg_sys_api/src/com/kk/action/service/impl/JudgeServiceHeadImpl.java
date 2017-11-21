package com.kk.action.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kk.action.dao.IDrfDao;
import com.kk.action.service.IJudgeServiceHead;
import com.kk.utils.iot.BaiduAddress;
import com.kk.utils.iot.EhcacheUtil;

@Service(value="judgeServiceHead")
public class JudgeServiceHeadImpl implements IJudgeServiceHead{

	@Resource(name="drfDao")
	private IDrfDao drfDao;
	@Override
	public String judgeServiceType(String message) throws Exception {
		Map<String ,Object> sendMessage=new HashMap<String,Object>();
		String result="";
		String sentence = message;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = format.format(new Date());
		JSONObject msg=JSONObject.parseObject(message);
		String head = msg.getString("MessageHead");// 获得消息的头
		String protocolVersion=msg.getString("ProtocolVersion");// 协议版本
		String uniqueId = msg.getString("UniqueID");// 设备ID
		String deviceName=msg.getString("DeviceName");//设备名称
		String reportId=msg.getString("ReportID");
		String reportType=msg.getString("ReportType");
		String number=msg.getString("Number");
		String gpsAccuracy=msg.getString("GPSAccuracy");
		String speed=msg.getString("Speed");// 速度
		String azimuth=msg.getString("Azimuth");// 航向
		String altitude=msg.getString("Altitude");// 高度
		String longitude=msg.getString("Longitude");// 经度
		String latitude=msg.getString("Latitude");// 纬度
		String gpsUTCtime=msg.getString("GPSUTCtime");
		String mcc=msg.getString("MCC");//移动国家代码
		String mnc=msg.getString("MNC");//移动网络代码
		String lac=msg.getString("LAC");//16进制表示的位置区码
		String cellID=msg.getString("CellID");//16进制表示的小区ID
		String odoMileage=msg.getString("OdoMileage");// 移动的距离
		String batteryPercentage=msg.getString("batteryPercentage");//电池电量的比例
		String sendTime=msg.getString("SendTime");//服务器发送时间
		String countNumber=msg.getString("CountNumber");
		
		String alarmType = "";// 报警类型
		String method = "";// 方法类型
		int p = 0;// 判断 匹配类型
		String dzwl = "";// 是否计算电子围栏（出圈）
		String deviceSendTimeDZWL = "";
		String fixtype = "";
		String locationType = "GPS/GSM";
		String type="";//1代表手动报警 2代表跌倒 3 代表 出圈 4 代表电池电量低
		String address="";
		String nickName="";//设备nick名称
		BaiduAddress baiduAddress = new BaiduAddress();
		//根据设备的uniqueId获取nickName
		nickName=drfDao.getNickName(uniqueId);
		//根据纬度和经度通过百度接口获取地理位置
		address = baiduAddress.getAddress(latitude,longitude);
		if (head.equals("+RESP:GTSOS") || head.equals("+RESP:GTFRI")
				|| head.equals("+RESP:GTGEO") || head.equals("+RESP:GTRTL")
				|| head.equals("+RESP:GTDIS") || head.equals("+RESP:GTNMR")) {
			try {
			if (head.equals("+RESP:GTSOS")) {
					type = "1";// 代表手动报警消息 
					sendMessage.put("uniqueId", uniqueId);
					sendMessage.put("latitude", latitude);
					sendMessage.put("longitude", longitude);
					sendMessage.put("sendTime", nowTime);
					sendMessage.put("type", type);
					sendMessage.put("locationType", locationType);
					sendMessage.put("nickName", nickName);
					sendMessage.put("address", address);
					String json=JSON.toJSONString(sendMessage);
					System.out.println("发送的消息:"+json);
					
			} else if (head.equals("+RESP:GTFRI")) {
					type = "17";// 代表H3的固定上报
//					{"uniqueId":"860599000058289","latitude":"0","longitude":"0",
//					"address":"北京市朝阳区皂君庙路5号","sendTime":"2014-12-17 17:54:45","type":"17",
//					”speed”:”30.2”,”azimuth”:”38.5”,"locationType":"GPS/GSM","nickName":"设备1"}
					sendMessage.put("uniqueId", uniqueId);
					sendMessage.put("latitude", latitude);
					sendMessage.put("longitude", longitude);
					sendMessage.put("address", address);
					sendMessage.put("sendTime", nowTime);
					sendMessage.put("type", type);
					sendMessage.put("speed", speed);
					sendMessage.put("azimuth", azimuth);
					sendMessage.put("locationType", locationType);
					sendMessage.put("nickName", nickName);
					String json=JSON.toJSONString(sendMessage);
					System.out.println("发送的消息:"+json);
			} else if (head.equals("+RESP:GTDIS")) {
				//{"uniqueId":"860599000058289","latitude":"0","longitude":"0",
				//"address":"北京市朝阳区皂君庙路5号","sendTime":"2014-12-17 17:54:45","type":"9",
				//"locationType":"GPS/GSM","nickName":"设备1"}
					type="9";//代表开关输入报警消息
					sendMessage.put("uniqueId", uniqueId);
					sendMessage.put("latitude", latitude);
					sendMessage.put("longitude", longitude);
					sendMessage.put("address", address);
					sendMessage.put("sendTime", nowTime);
					sendMessage.put("type", type);
					sendMessage.put("locationType", locationType);
					sendMessage.put("nickName", nickName);
					String json=JSON.toJSONString(sendMessage);
					System.out.println("发送的消息:"+json);
					
			} else if (head.equals("+RESP:GTGEO")) {
				//{"uniqueId":"860599000058289","latitude":"0","longitude":"0",
				//"address":"北京市朝阳区皂君庙路5号","sendTime":"2014-12-17 17:54:45","type":"3",
				//"locationType":"GPS/GSM","nickName":"设备1"}
				type = "3";//代表出圈报警消息
				sendMessage.put("uniqueId", uniqueId);
				sendMessage.put("latitude", latitude);
				sendMessage.put("longitude", longitude);
				sendMessage.put("address", address);
				sendMessage.put("sendTime", nowTime);
				sendMessage.put("type", type);
				sendMessage.put("locationType", locationType);
				sendMessage.put("nickName", nickName);
				String json=JSON.toJSONString(sendMessage);
				System.out.println("发送的消息:"+json);
				
			} else if (head.equals("+RESP:GTRTL")) {
				alarmType = "16";//代表实时位置消息
				//{"uniqueId":"860599000058289","latitude":"0","longitude":"0",
				//"address":"北京市朝阳区皂君庙路5号","sendTime":"2014-12-17 17:54:45",
				//"type":"16","locationType":"GPS/GSM","nickName":"设备1"}
				sendMessage.put("uniqueId", uniqueId);
				sendMessage.put("latitude", latitude);
				sendMessage.put("longitude", longitude);
				sendMessage.put("address", address);
				sendMessage.put("sendTime", nowTime);
				sendMessage.put("type", type);
				sendMessage.put("locationType", locationType);
				sendMessage.put("nickName", nickName);
				String json=JSON.toJSONString(sendMessage);
				System.out.println("发送的消息:"+json);
				
			} else if (head.equals("+RESP:GTNMR")) {
				type = "7";// 代表动静检测      
				//如果是这个类型的，解析存库
				//暂不返回数据
				String json=JSON.toJSONString(sendMessage);
				System.out.println("发送的消息:"+json);
			}
		 } catch (Exception e) {
			 return "";
		 }
		} else if ("+ACK:GTFRI".equals(head)) {
			//开启追踪模式成功/失败回执
			//{"uniqueId":"860599000058289","sendTime":"2014-12-17 17:54:45","type":"12","nickName":"设备1"}
			type="12";
			sendMessage.put("uniqueId", uniqueId);
			sendMessage.put("sendTime", nowTime);
			sendMessage.put("type", type);
			sendMessage.put("nickName", nickName);
			String json=JSON.toJSONString(sendMessage);
			System.out.println("发送的消息:"+json);
			
		} else if (head.equals("+RESP:GTGSM")) {
			type="13";//代表设备状态消息
			//{"DevNam":"g3",”HardVer”:”1.0.0”,”FirmVer”:”1.0.0”,”ProVer”:”a1001”,
			//”PhoNum”:”13000000000”,”MovFlag”:”0/1”,”BatPerc”:”100%”,
			//”SensFlag”:”0/1”,”GSMLevel”:”30”,”uniqueId”:”800102021”,”type”:”13”,"nickName":"设备1"}
			//暂无返回信息
		}else if (head.equals("+RESP:GTPNA") || head.equals("+RESP:GTPFA")) {
			if(head.equals("+RESP:GTPNA")){
				type="5";
				//开机事件
				//{"uniqueId":"860599000058289","sendTime":"2014-12-17 17:54:45","type":"5","nickName":"设备1"}
				sendMessage.put("uniqueId", uniqueId);
				sendMessage.put("sendTime", nowTime);
				sendMessage.put("type", type);
				sendMessage.put("nickName", nickName);
				String json=JSON.toJSONString(sendMessage);
				System.out.println("发送的消息:"+json);
			}
			if(head.equals("+RESP:GTPFA")){
				type="6";
				//关机事件
				//{"uniqueId":"860599000058289","sendTime":"2014-12-17 17:54:45","type":"6","nickName":"设备1"}
				sendMessage.put("uniqueId", uniqueId);
				sendMessage.put("sendTime", nowTime);
				sendMessage.put("type", type);
				sendMessage.put("nickName", nickName);
				String json=JSON.toJSONString(sendMessage);
				System.out.println("发送的消息:"+json);
			}
			
		}else if(head.equals("+RESP:GTBPL")){
			type="4";//电池电量低
			//{"uniqueId":"860599000058289","latitude":"0","longitude":"0","address":"北京市朝阳区皂君庙路5号",
			//"sendTime":"2014-12-17 17:54:45","type":"4","locationType":"GPS/GSM","nickName":"设备1"}
			sendMessage.put("uniqueId", uniqueId);
			sendMessage.put("latitude", latitude);
			sendMessage.put("longitude", longitude);
			sendMessage.put("address", address);
			sendMessage.put("sendTime", nowTime);
			sendMessage.put("type", type);
			sendMessage.put("locationType", locationType);
			sendMessage.put("nickName", nickName);
			String json=JSON.toJSONString(sendMessage);
			System.out.println("发送的消息:"+json);
			
		}else if(head.equals("+RESP:GTALM")){
			type="10";//代表开启跟踪模式回执消息
			//{"uniqueId":"860599000058289","sendTime":"2014-12-17-17:54:45","type":"10","nickName":"设备1"}
			sendMessage.put("uniqueId", uniqueId);
			sendMessage.put("sendTime", nowTime);
			sendMessage.put("type", type);
			sendMessage.put("nickName", nickName);
			String json=JSON.toJSONString(sendMessage);
			System.out.println("发送的消息:"+json);
			
		}else if(head.equals("+RESP:GTFAM")){
			type="11";//代表跟踪模式消息
			//{"uniqueId":"860599000058289","latitude":"0","longitude":"0",
			//"address":"北京市朝阳区皂君庙路5号","sendTime":"2014-12-17 17:54:45",
			//"type":"11",”speed”:”30.2”,”azimuth”:”38.5”,"locationType":"GPS/GSM","nickName":"设备1"}
			sendMessage.put("uniqueId", uniqueId);
			sendMessage.put("latitude", latitude);
			sendMessage.put("longitude", longitude);
			sendMessage.put("address", address);
			sendMessage.put("sendTime", nowTime);
			sendMessage.put("type", type);
			sendMessage.put("speed", speed);
			sendMessage.put("azimuth",azimuth);
			sendMessage.put("locationType", locationType);
			sendMessage.put("nickName", nickName);
			String json=JSON.toJSONString(sendMessage);
			System.out.println("发送的消息:"+json);
		}else if(head.equals("+RESP:GTTST")){
			type="18";//定期测试报告
			//{"uniqueId":"865662000233669","latitude":"39.965483980952634","longitude":"116.33533329242947",
			//"address":"北京市海淀区皂君庙路4号","sendTime":"2015-03-11 11:08:38","type":"18","DevNam":"G3",
			//"FirmVer":"G30100","ProVer":"G30100","MovFlag":"0","BatPerc":"99","SensFlag":"1","GSMLevel":"21",
			//"locationType":"GPS/GSM","nickName":"设备1"}
			
			
			
		}
	    result=JSON.toJSONString(sendMessage);
		return result;
	}

}
