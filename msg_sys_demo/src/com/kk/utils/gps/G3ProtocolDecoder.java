package com.kk.utils.gps;
//package com.gnet.utils.gps;
//
//import io.netty.channel.Channel;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.InvalidPropertiesFormatException;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.TimerTask;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import com.gnet.utils.BaiduAddress;
//import com.gnet.utils.EhcacheUtil;
//import com.gnet.utils.GetPosition;
//import com.gnet.utils.GsonBean;
//import com.gnet.utils.MysqlRegex;
//import com.gnet.utils.protocol.InterfaceOfGl300;
//import com.google.gson.Gson;
//
//public class G3ProtocolDecoder  {
//	//SenderMQTT senderMQTT = new SenderMQTT();
//	GetPosition gtpo = new GetPosition();
//	static String messageIP = "";// 消息服务器ip
//	MysqlRegex mysqlRegex = new MysqlRegex();
//	static Gson gson = new Gson();
//	static EhcacheUtil ehcacheUtil = new EhcacheUtil();
//	InterfaceOfGl300 interfaceOfGl300 = new InterfaceOfGl300();
//	BaiduAddress baiduAddress = new BaiduAddress();
//	//MainAsyn mainAsyn = new MainAsyn();
//	String guid = "";
//	String ack = "";
//	String SetNum = "";
//	String returnReceipt = "";
//	String devNam = "";
//	String hardVer = "";
//	String firmVer = "";
//	String proVer = "";
//	String phoNum = "";
//	String movFlag = "";
//	String sensFlag = "";
//	String gsMLevel = "";
//	public static Channel channelTOP;
//	public static String uniqueIdTOP;
//	public static String[] keywords = {};
//	public static String[] sentenceArray = {};
//	private final static double EARTH_RADIUS = 6378137.0; // 地球半径
//	String time = "";
//	String uniqueId = "";
//	String fixtype = "";
//	String mcc1 = "";
//	String mnc1 = "";
//	String lac1 = "";
//	String cellid1 = "";
//	String rxlevel1 = "";
//	String mcc2 = "";
//	String mnc2 = "";
//	String lac2 = "";
//	String cellid2 = "";
//	String rxlevel2 = "";
//	String mcc3 = "";
//	String mnc3 = "";
//	String lac3 = "";
//	String cellid3 = "";
//	String rxlevel3 = "";
//	String mcc4 = "";
//	String mnc4 = "";
//	String lac4 = "";
//	String cellid4 = "";
//	String rxlevel4 = "";
//	String mcc5 = "";
//	String mnc5 = "";
//	String lac5 = "";
//	String cellid5 = "";
//	String rxlevel5 = "";
//	String mcc6 = "";
//	String mnc6 = "";
//	String lac6 = "";
//	String cellid6 = "";
//	String rxlevel6 = "";
//	String mcc7 = "";
//	String mnc7 = "";
//	String lac7 = "";
//	String cellid7 = "";
//	String rxlevel7 = "";
//	String paramInfo = "";
//	// String uniqueId= "";
//	public Object G3decode(Channel channel,
//			String msg) {
//		channelTOP=channel;
//		String sentence =msg.trim();
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String thisTime = format.format(new Date());
//		System.out.println(thisTime + "G3收到的数据==" + sentence);
//		String head = "";// 获得消息的头
//		// 通道ID
//		// 输入位置
//		String[] resp = {};
//		String[] resps = {};
//		try {
//			resps=sentence.split("\n");
//		
//				try {
//					head = resp[0];// 协议头
//					uniqueId = resp[1];// 设备ID	
//					String protocolVersion = "G30100";// 协议版本
//					time = resp[(resp.length - 2)];
//					if (time==""||"".equals(time)) {
//						time=thisTime;
//					}
//					
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				}
//				String messageInfo = (String) ehcacheUtil.get(head + "G3"
//						+ "messageInfo", "messageInfo");
//				String longitude = "";// 经度
//				String latitude = "";// 纬度
//				String alarmType = "";// 报警类型
//				String method = "";// 方法类型
//				String altitude = "0";// 高度
//				String azimuth = "0";// 航向
//				String power = "0";// 电量
//				String speed = "0";// 速度
//				String address = "";
//				String odoMileage = "0";// 移动的距离
//				int p = 0;// 判断 匹配类型
//				String dzwl = "";// 是否计算电子围栏（出圈）
//				String deviceSendTime = "";
//				String locationType="GSM";
//				Map<String, String> keyword = new HashMap<String, String>();
//				// 输入位置
//				keywords = messageInfo.split(",");
//				sentenceArray = resps[0].split(",");
//				for (int i = 1; i < keywords.length; i++) {
//					try {
//						keyword.put(keywords[i], sentenceArray[i]);
//						System.out.println("keywords[i]" + keywords[i] + ">>>>>>"
//								+ sentenceArray[i]);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//				if (("+RESP:GTFAM").equals(head)) {
//					
//					try {
//						System.out.println("进入追踪模式!!!");
//						longitude = keyword.get("Longitude");
//						latitude = keyword.get("Latitude");
//						alarmType = "11";
//						method = "position.addPositions";
//						altitude = keyword.get("Altitude");
//						azimuth = keyword.get("Azimuth");
//						power = keyword.get("BatPerc");
//						speed = keyword.get("Speed");
//						uniqueId = keyword.get("IMEINum");
//						// time = keyword.get("SenTim");
//						mcc1 = keyword.get("MCC");
//						mnc1 = keyword.get("MNC");
//						lac1 = keyword.get("LAC");
//						cellid1 = keyword.get("CELLID");
//						rxlevel1 = keyword.get("RX_LEVEL");
//						mcc2 = keyword.get("MCC1");
//						mnc2 = keyword.get("MNC1");
//						lac2 = keyword.get("LAC1");
//						cellid2 = keyword.get("CELLID1");
//						rxlevel2 = keyword.get("RX_LEVEL1");
//						mcc3 = keyword.get("MCC2");
//						mnc3 = keyword.get("MNC2");
//						lac3 = keyword.get("LAC2");
//						cellid3 = keyword.get("CELLID2");
//						rxlevel3 = keyword.get("RX_LEVEL2");
//						mcc4 = keyword.get("MCC3");
//						mnc4 = keyword.get("MNC3");
//						lac4 = keyword.get("LAC3");
//						cellid4 = keyword.get("CELLID3");
//						rxlevel4 = keyword.get("RX_LEVEL3");
//						mcc5 = keyword.get("MCC4");
//						mnc5 = keyword.get("MNC4");
//						lac5 = keyword.get("LAC4");
//						cellid5 = keyword.get("CELLID4");
//						rxlevel5 = keyword.get("RX_LEVEL4");
//						mcc6 = keyword.get("MCC5");
//						mnc6 = keyword.get("MCC5");
//						lac6 = keyword.get("LAC5");
//						cellid6 = keyword.get("CELLID5");
//						rxlevel6 = keyword.get("RX_LEVEL5");
//						
//						mcc7 = keyword.get("MCC6");
//						mnc7 = keyword.get("MNC6");
//						lac7 = keyword.get("LAC6");
//						cellid7 = keyword.get("CELLID6");
//						rxlevel7 = keyword.get("RX_LEVEL6");
//						ehcacheUtil.put(lac1 + cellid1, rxlevel1, "rxlevel");
//						ehcacheUtil.put(lac2 + cellid2, rxlevel2, "rxlevel");
//						ehcacheUtil.put(lac3 + cellid3, rxlevel3, "rxlevel");
//						ehcacheUtil.put(lac4 + cellid4, rxlevel4, "rxlevel");
//						ehcacheUtil.put(lac5 + cellid5, rxlevel5, "rxlevel");
//						ehcacheUtil.put(lac6 + cellid6, rxlevel6, "rxlevel");
//						ehcacheUtil.put(lac7 + cellid7, rxlevel7, "rxlevel");
//						if ("".equals(longitude) || "".equals(latitude)||"0".equals(longitude) || "0".equals(latitude)||"0"==longitude || "0"==latitude) {
//							locationType="GSM";
//							List<String> baseStation = baseStation(mcc1, mnc1, lac1,
//									cellid1, rxlevel1, mcc2, mnc2, lac2, cellid2,
//									rxlevel2, mcc3, mnc3, lac3, cellid3, rxlevel3,
//									mcc4, mnc4, lac4, cellid4, rxlevel4, mcc5, mnc5,
//									lac5, cellid5, rxlevel5, mcc6, mnc6, lac6, cellid6,
//									rxlevel6, mcc7, mnc7, lac7, cellid7, rxlevel7,
//									deviceSendTime, "GSM", uniqueId);
//							longitude = baseStation.get(1);// 经度
//							latitude = baseStation.get(0);// 纬度
//							if ("0" == longitude || "0" == latitude||longitude=="NaN"||latitude=="NaN"||"NaN".equals(longitude)||"NaN".equals(latitude)) {
//								longitude= "";
//								latitude= "";
//								address = "";
//								altitude = "0";// 高度
//								azimuth = "0";// 航向
//								speed = "0";// 速度
//							} else {
//								Double longitude2 = Double.parseDouble(longitude);// 经度
//								Double latitude2 = Double.parseDouble(latitude);// 纬度
//								// 百度地图纠偏 重新获得
//								String thisPostion1 = gtpo.transgpsbd(longitude2,
//										latitude2);
//								String longitudebd = thisPostion1.split(",")[0];
//								String latitudebd = thisPostion1.split(",")[1];
//								System.out.println("百度地址:"+longitudebd+","+latitudebd);
//								address = baiduAddress.getAddress(latitudebd,
//										longitudebd);// 地址
//								altitude = "0";// 高度
//								azimuth = "0";// 航向
//								speed = "0";// 速度
//							}
//
//						}  else {
//							locationType="GPS";
//							System.out.println("latitude0000>>>" + latitude);
//							System.out.println("longitude=00000=>>>>>" + longitude);
//							Double longitude2 = Double.parseDouble(longitude);// 经度
//							Double latitude2 = Double.parseDouble(latitude);// 纬度
//							String thisPostion = gtpo.transgpshx(longitude2, latitude2);
//							longitude = thisPostion.split(",")[0];
//							latitude = thisPostion.split(",")[1];
//							Double longitude3 = Double.parseDouble(longitude);// 经度
//							Double latitude3 = Double.parseDouble(latitude);// 纬度
//							String thisPostion1 = gtpo
//									.transgpsbd(longitude3, latitude3);
//							String longitudebd = thisPostion1.split(",")[0];
//							String latitudebd = thisPostion1.split(",")[1];
//							address = baiduAddress.getAddress(latitudebd, longitudebd);// 地址
//							/*Log.info("纠偏之后的位置=" + head + "," + uniqueId + ","
//									+ longitude + "," + latitude);*/
//							System.out.println("百度地址:"+longitudebd+","+latitudebd);
//							System.out.println("latitude111>>>>" + latitude);
//							System.out.println("longitude=1111>>>" + longitude);
//							System.out.println("address===" + address);
//							
//						}
//						p = 11;
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//				} else if (("+RESP:GTBPL").equals(head) || ("+BUFF:GTBPL").equals(head)) {
//					longitude = "";
//					uniqueId = keyword.get("IMEINum");
//					latitude = "";
//					alarmType = "4";
//					method = "alarm.addAlarm";
//					address = "";
//					//locationType="GSM";
//					time = keyword.get("SenTim");
//					p = 5;
//
//				} else if (("+RESP:GTALM").equals(head) || ("+BUFF:GTALM").equals(head)) {
//					try {
//						alarmType = "10";
//						method = "alarm.addAlarm";
//						power = keyword.get("BatPerc");
//						uniqueId = keyword.get("IMEINum");
//						// time = keyword.get("SenTim");
//						mcc1 = keyword.get("MCC");
//						mnc1 = keyword.get("MNC");
//						lac1 = keyword.get("LAC");
//						cellid1 = keyword.get("CELLID");
//						rxlevel1 = keyword.get("RX_LEVEL");
//						mcc2 = keyword.get("MCC1");
//						mnc2 = keyword.get("MNC1");
//						lac2 = keyword.get("LAC1");
//						cellid2 = keyword.get("CELLID1");
//						rxlevel2 = keyword.get("RX_LEVEL1");
//						mcc3 = keyword.get("MCC2");
//						mnc3 = keyword.get("MNC2");
//						lac3 = keyword.get("LAC2");
//						cellid3 = keyword.get("CELLID2");
//						rxlevel3 = keyword.get("RX_LEVEL2");
//						mcc4 = keyword.get("MCC3");
//						mnc4 = keyword.get("MNC3");
//						lac4 = keyword.get("LAC3");
//						cellid4 = keyword.get("CELLID3");
//						rxlevel4 = keyword.get("RX_LEVEL3");
//						mcc5 = keyword.get("MCC4");
//						mnc5 = keyword.get("MNC4");
//						lac5 = keyword.get("LAC4");
//						cellid5 = keyword.get("CELLID4");
//						rxlevel5 = keyword.get("RX_LEVEL4");
//						mcc6 = keyword.get("MCC5");
//						mnc6 = keyword.get("MCC5");
//						lac6 = keyword.get("LAC5");
//						cellid6 = keyword.get("CELLID5");
//						rxlevel6 = keyword.get("RX_LEVEL5");
//						mcc7 = keyword.get("MCC6");
//						mnc7 = keyword.get("MNC6");
//						lac7 = keyword.get("LAC6");
//						cellid7 = keyword.get("CELLID6");
//						rxlevel7 = keyword.get("RX_LEVEL6");
//						ehcacheUtil.put(lac1 + cellid1, rxlevel1, "rxlevel");
//						ehcacheUtil.put(lac2 + cellid2, rxlevel2, "rxlevel");
//						ehcacheUtil.put(lac3 + cellid3, rxlevel3, "rxlevel");
//						ehcacheUtil.put(lac4 + cellid4, rxlevel4, "rxlevel");
//						ehcacheUtil.put(lac5 + cellid5, rxlevel5, "rxlevel");
//						ehcacheUtil.put(lac6 + cellid6, rxlevel6, "rxlevel");
//						ehcacheUtil.put(lac7 + cellid7, rxlevel7, "rxlevel");
//						if ("".equals(longitude) || "".equals(latitude)) {
//							locationType="GSM";
//							List<String> baseStation = baseStation(mcc1, mnc1, lac1,
//									cellid1, rxlevel1, mcc2, mnc2, lac2, cellid2,
//									rxlevel2, mcc3, mnc3, lac3, cellid3, rxlevel3,
//									mcc4, mnc4, lac4, cellid4, rxlevel4, mcc5, mnc5,
//									lac5, cellid5, rxlevel5, mcc6, mnc6, lac6, cellid6,
//									rxlevel6, mcc7, mnc7, lac7, cellid7, rxlevel7,
//									deviceSendTime, "GSM", uniqueId);
//							longitude = baseStation.get(1);// 经度
//							latitude = baseStation.get(0);// 纬度
//							if ("0" == longitude || "0" == latitude||longitude=="NaN"||latitude=="NaN"||"NaN".equals(longitude)||"NaN".equals(latitude)) {
//								longitude= "";
//								latitude= "";
//								address = "";
//							} else {
//								Double longitude2 = Double.parseDouble(longitude);// 经度
//								Double latitude2 = Double.parseDouble(latitude);// 纬度
//								// 百度地图纠偏 重新获得
//								String thisPostion1 = gtpo.transgpsbd(longitude2,
//										latitude2);
//								String longitudebd = thisPostion1.split(",")[0];
//								String latitudebd = thisPostion1.split(",")[1];
//								address = baiduAddress.getAddress(latitudebd,
//										longitudebd);// 地址
//							}
//							p = 10;
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//				} else if (("+ACK:GTSTP").equals(head)) {
//					uniqueId = keyword.get("IMEINum");
//					ack = "STP";
//					try {
//						if (EhcacheUtil
//								.get(uniqueId
//										+ "trackStatus",
//										"trackStatus") != null) {
//							EhcacheUtil
//									.replace(
//											uniqueId
//													+ "trackStatus",
//											"close",
//											"trackStatus");
//						} else {
//							EhcacheUtil
//									.put(uniqueId
//											+ "trackStatus",
//											"close",
//											"trackStatus");
//						}
//						if (EhcacheUtil.get(uniqueId, "isOnline")!=null) {
//							EhcacheUtil.replace(uniqueId, "no","isOnline");
//						}else {
//							EhcacheUtil.put(uniqueId, "no","isOnline");
//							
//						}
//						ehcacheUtil.remove(uniqueId + "_channel", "devicesCache");
//					} catch (Exception e) {
//
//						e.printStackTrace();
//					}
//				
//					
//				} else if (("+ACK:GTFLW").equals(head)) {
//					uniqueId = keyword.get("IMEINum");
//					ack = "FLW";
//					
//				} else if (("+ACK:GTGBC").equals(head)) {
//					uniqueId = keyword.get("IMEINum");
//					ack = "GBC";
//					
//				} else if (("+RESP:GTTST").equals(head) || ("+BUFF:GTTST").equals(head)) {
//					try {
//						//longitude= "";
//						//latitude= "";
//						//address = "";
//						altitude = "0";// 高度
//						azimuth = "0";// 航向
//						speed = "0";// 速度
//						alarmType = "18";
//						method = "alarm.addAlarm";
//						power = keyword.get("BatPerc");
//						uniqueId = keyword.get("IMEINum");
//						devNam = keyword.get("DevNam");
//						hardVer = keyword.get("HardVer");
//						firmVer = keyword.get("FirmVer");
//						proVer = keyword.get("ProVer");
//						phoNum = keyword.get("PhoNum");
//						movFlag = keyword.get("MovFlag");
//						sensFlag = keyword.get("SensFlag");
//						gsMLevel = keyword.get("GSMLevel");
//						//time = keyword.get("SenTim");
//						mcc1 = keyword.get("MCC");
//						mnc1 = keyword.get("MNC");
//						lac1 = keyword.get("LAC");
//						cellid1 = keyword.get("CELLID");
//						rxlevel1 = keyword.get("RX_LEVEL");
//						mcc2 = keyword.get("MCC1");
//						mnc2 = keyword.get("MNC1");
//						lac2 = keyword.get("LAC1");
//						cellid2 = keyword.get("CELLID1");
//						rxlevel2 = keyword.get("RX_LEVEL1");
//						mcc3 = keyword.get("MCC2");
//						mnc3 = keyword.get("MNC2");
//						lac3 = keyword.get("LAC2");
//						cellid3 = keyword.get("CELLID2");
//						rxlevel3 = keyword.get("RX_LEVEL2");
//						mcc4 = keyword.get("MCC3");
//						mnc4 = keyword.get("MNC3");
//						lac4 = keyword.get("LAC3");
//						cellid4 = keyword.get("CELLID3");
//						rxlevel4 = keyword.get("RX_LEVEL3");
//						mcc5 = keyword.get("MCC4");
//						mnc5 = keyword.get("MNC4");
//						lac5 = keyword.get("LAC4");
//						cellid5 = keyword.get("CELLID4");
//						rxlevel5 = keyword.get("RX_LEVEL4");
//						mcc6 = keyword.get("MCC5");
//						mnc6 = keyword.get("MCC5");
//						lac6 = keyword.get("LAC5");
//						cellid6 = keyword.get("CELLID5");
//						rxlevel6 = keyword.get("RX_LEVEL5");
//						mcc7 = keyword.get("MCC6");
//						mnc7 = keyword.get("MNC6");
//						lac7 = keyword.get("LAC6");
//						cellid7 = keyword.get("CELLID6");
//						rxlevel7 = keyword.get("RX_LEVEL6");
//						ehcacheUtil.put(lac1 + cellid1, rxlevel1, "rxlevel");
//						ehcacheUtil.put(lac2 + cellid2, rxlevel2, "rxlevel");
//						ehcacheUtil.put(lac3 + cellid3, rxlevel3, "rxlevel");
//						ehcacheUtil.put(lac4 + cellid4, rxlevel4, "rxlevel");
//						ehcacheUtil.put(lac5 + cellid5, rxlevel5, "rxlevel");
//						ehcacheUtil.put(lac6 + cellid6, rxlevel6, "rxlevel");
//						ehcacheUtil.put(lac7 + cellid7, rxlevel7, "rxlevel");
//						locationType="GSM";
//						List<String> baseStation = baseStation(mcc1, mnc1, lac1,
//								cellid1, rxlevel1, mcc2, mnc2, lac2, cellid2, rxlevel2,
//								mcc3, mnc3, lac3, cellid3, rxlevel3, mcc4, mnc4, lac4,
//								cellid4, rxlevel4, mcc5, mnc5, lac5, cellid5, rxlevel5,
//								mcc6, mnc6, lac6, cellid6, rxlevel6, mcc7, mnc7, lac7,
//								cellid7, rxlevel7, deviceSendTime, "GSM", uniqueId);
//						longitude = baseStation.get(1);// 经度
//						latitude = baseStation.get(0);// 纬度
//						if ("0" == longitude || "0" == latitude||longitude=="NaN"||latitude=="NaN"||"NaN".equals(longitude)||"NaN".equals(latitude)) {
//							longitude= "";
//							latitude= "";
//							address = "";
//							
//						} else {
//							Double longitude2 = Double.parseDouble(longitude);// 经度
//							Double latitude2 = Double.parseDouble(latitude);// 纬度
//							// 百度地图纠偏 重新获得
//							String thisPostion1 = gtpo
//									.transgpsbd(longitude2, latitude2);
//							String longitudebd = thisPostion1.split(",")[0];
//							String latitudebd = thisPostion1.split(",")[1];
//							address = baiduAddress.getAddress(latitudebd, longitudebd);// 地址
//							
//					}
//						p = 17;
//
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//				} else if (("+ACK:GTSTU").equals(head)) {
//					try {
//						ack = "STU";
//						alarmType = "13";
//						power = keyword.get("BatPerc");
//						uniqueId = keyword.get("IMEINum");
//						devNam = keyword.get("DevNam");
//						hardVer = keyword.get("HardVer");
//						firmVer = keyword.get("FirmVer");
//						proVer = keyword.get("ProVer");
//						phoNum = keyword.get("PhoNum");
//						movFlag = keyword.get("MovFlag");
//						sensFlag = keyword.get("SensFlag");
//						gsMLevel = keyword.get("GSMLevel");
//						time = keyword.get("SenTim");
//						mcc1 = keyword.get("MCC");
//						mnc1 = keyword.get("MNC");
//						lac1 = keyword.get("LAC");
//						cellid1 = keyword.get("CELLID");
//						rxlevel1 = keyword.get("RX_LEVEL");
//						mcc2 = keyword.get("MCC1");
//						mnc2 = keyword.get("MNC1");
//						lac2 = keyword.get("LAC1");
//						cellid2 = keyword.get("CELLID1");
//						rxlevel2 = keyword.get("RX_LEVEL1");
//						mcc3 = keyword.get("MCC2");
//						mnc3 = keyword.get("MNC2");
//						lac3 = keyword.get("LAC2");
//						cellid3 = keyword.get("CELLID2");
//						rxlevel3 = keyword.get("RX_LEVEL2");
//						mcc4 = keyword.get("MCC3");
//						mnc4 = keyword.get("MNC3");
//						lac4 = keyword.get("LAC3");
//						cellid4 = keyword.get("CELLID3");
//						rxlevel4 = keyword.get("RX_LEVEL3");
//						mcc5 = keyword.get("MCC4");
//						mnc5 = keyword.get("MNC4");
//						lac5 = keyword.get("LAC4");
//						cellid5 = keyword.get("CELLID4");
//						rxlevel5 = keyword.get("RX_LEVEL4");
//						mcc6 = keyword.get("MCC5");
//						mnc6 = keyword.get("MCC5");
//						lac6 = keyword.get("LAC5");
//						cellid6 = keyword.get("CELLID5");
//						rxlevel6 = keyword.get("RX_LEVEL5");
//						mcc7 = keyword.get("MCC6");
//						mnc7 = keyword.get("MNC6");
//						lac7 = keyword.get("LAC6");
//						cellid7 = keyword.get("CELLID6");
//						rxlevel7 = keyword.get("RX_LEVEL6");
//						ehcacheUtil.put(lac1 + cellid1, rxlevel1, "rxlevel");
//						ehcacheUtil.put(lac2 + cellid2, rxlevel2, "rxlevel");
//						ehcacheUtil.put(lac3 + cellid3, rxlevel3, "rxlevel");
//						ehcacheUtil.put(lac4 + cellid4, rxlevel4, "rxlevel");
//						ehcacheUtil.put(lac5 + cellid5, rxlevel5, "rxlevel");
//						ehcacheUtil.put(lac6 + cellid6, rxlevel6, "rxlevel");
//						ehcacheUtil.put(lac7 + cellid7, rxlevel7, "rxlevel");
//						locationType="GSM";
//						List<String> baseStation = baseStation(mcc1, mnc1, lac1,
//								cellid1, rxlevel1, mcc2, mnc2, lac2, cellid2, rxlevel2,
//								mcc3, mnc3, lac3, cellid3, rxlevel3, mcc4, mnc4, lac4,
//								cellid4, rxlevel4, mcc5, mnc5, lac5, cellid5, rxlevel5,
//								mcc6, mnc6, lac6, cellid6, rxlevel6, mcc7, mnc7, lac7,
//								cellid7, rxlevel7, deviceSendTime, "GSM", uniqueId);
//						longitude = baseStation.get(1);// 经度
//						latitude = baseStation.get(0);// 纬度
//						if ("0" == longitude || "0" == latitude||longitude=="NaN"||latitude=="NaN"||"NaN".equals(longitude)||"NaN".equals(latitude)) {
//							longitude= "";
//							latitude= "";
//							address = "";
//							
//						} else {
//							Double longitude2 = Double.parseDouble(longitude);// 经度
//							Double latitude2 = Double.parseDouble(latitude);// 纬度
//							// 百度地图纠偏 重新获得
//							String thisPostion1 = gtpo
//									.transgpsbd(longitude2, latitude2);
//							String longitudebd = thisPostion1.split(",")[0];
//							String latitudebd = thisPostion1.split(",")[1];
//							address = baiduAddress.getAddress(latitudebd, longitudebd);// 地址
//						}
//
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//				}
//
//				GsonBean  appContent =null;// 手机端用的推送消息
//				String sendTime = format.format(new Date());
//				// 报警信息 推送
//				if ((p == 11&&latitude!=""&&longitude!=""&&!"".equals(latitude)) || p == 10 || p == 5 || p == 17 || ack == "STP"
//						|| "STP".equals(ack) || ack == "FLW" || "FLW".equals(ack)
//						|| ack == "STP" || "STP".equals(ack) || ack == "STU"
//						|| "STU".equals(ack) || ack == "GBC" || "GBC".equals(ack)) {
//					// 推送内容
//					// String type = "";
//					List list = interfaceOfGl300.getPhoneList(uniqueId);
//					if (alarmType.equals("11")) {
//						GsonBean gsonBean = new GsonBean();
//						gsonBean.setLatitude(latitude);
//						gsonBean.setLongitude(longitude);
//						gsonBean.setSpeed(speed);
//						gsonBean.setAzimuth(azimuth);
//						gsonBean.setUniqueId(uniqueId);
//						gsonBean.setAddress(address);
//						gsonBean.setType(alarmType);
//						gsonBean.setSendTime(sendTime);
//						gsonBean.setBatPerc(power);
//						gsonBean.setLocationType(locationType);
//						appContent = gsonBean;
//						try {
//
////							RocketmqProducer.mqttMain1(list, appContent,
////									uniqueId, "追踪模式上报位置信息", sendTime,address,"default");
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//					} else if (alarmType.equals("4")) {
//						GsonBean gsonBean = new GsonBean();
//						//gsonBean.setLatitude(latitude);
//						//gsonBean.setLongitude(longitude);
//						gsonBean.setUniqueId(uniqueId);
//						//gsonBean.setAddress(address);
//						gsonBean.setType(alarmType);
//						//gsonBean.setLocationType(locationType);
//						gsonBean.setSendTime(sendTime);
//						appContent = gsonBean;
//						try {
//
////							RocketmqProducer.mqttMain1(list, appContent,
////									uniqueId, "电池电量低", sendTime,"暂时无法获取位置信息","default");
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					} else if (alarmType.equals("10")) {
//						GsonBean gsonBean = new GsonBean();
//						if (latitude==""||longitude==""||"".equals(latitude)) {
//							//gsonBean.setLatitude(latitude);
//							//gsonBean.setLongitude(longitude);
//							gsonBean.setUniqueId(uniqueId);
//							//gsonBean.setAddress(address);
//							gsonBean.setType(alarmType);
//							//gsonBean.setLocationType(locationType);
//							gsonBean.setSendTime(sendTime);
//							try {
//
////								RocketmqProducer.mqttMain1(list, appContent,
////										uniqueId, "报警：震动报警", sendTime,"暂时无法获取位置信息","default");
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}else {
//							gsonBean.setLatitude(latitude);
//							gsonBean.setLongitude(longitude);
//							gsonBean.setUniqueId(uniqueId);
//							gsonBean.setAddress(address);
//							gsonBean.setType(alarmType);
//							gsonBean.setLocationType(locationType);
//							gsonBean.setSendTime(sendTime);
//							try {
//
////								RocketmqProducer.mqttMain1(list, appContent,
////										uniqueId, "报警：震动报警", sendTime,address,"default");
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//						
//						appContent = gsonBean;
//					} else if (alarmType.equals("18")) {
//						GsonBean gsonBean = new GsonBean();
//						if (latitude==""||longitude==""||"".equals(latitude)) {
//							//gsonBean.setLatitude(latitude);
//							//gsonBean.setLongitude(longitude);
//							//gsonBean.setLocationType(locationType);
//							gsonBean.setUniqueId(uniqueId);
//							//gsonBean.setAddress(address);
//							gsonBean.setType(alarmType);
//							gsonBean.setBatPerc(power);
//							gsonBean.setDevNam(devNam);
//							gsonBean.setFirmVer(firmVer);
//							gsonBean.setGSMLevel(gsMLevel);
//							gsonBean.setProVer(proVer);
//							gsonBean.setMovFlag(movFlag);
//							gsonBean.setSensFlag(sensFlag);
//							gsonBean.setSendTime(sendTime);
//							try {
//
//								//RocketmqProducer.mqttMain1(list, appContent,uniqueId, "测试报告", sendTime,"暂时无法获取位置信息","default");
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}else {
//							gsonBean.setLatitude(latitude);
//							gsonBean.setLongitude(longitude);
//							gsonBean.setLocationType(locationType);
//							gsonBean.setUniqueId(uniqueId);
//							gsonBean.setAddress(address);
//							gsonBean.setType(alarmType);
//							gsonBean.setBatPerc(power);
//							gsonBean.setDevNam(devNam);
//							gsonBean.setFirmVer(firmVer);
//							gsonBean.setGSMLevel(gsMLevel);
//							gsonBean.setProVer(proVer);
//							gsonBean.setMovFlag(movFlag);
//							gsonBean.setSensFlag(sensFlag);
//							gsonBean.setSendTime(sendTime);
//							try {
//
//								//RocketmqProducer.mqttMain1(list, appContent,uniqueId, "测试报告", sendTime,address,"default");
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//						
//						appContent = gsonBean;
//					} else if (ack == "STP" || "STP".equals(ack)) {
//						alarmType = "14";
//						GsonBean gsonBean = new GsonBean();
//						gsonBean.setUniqueId(uniqueId);
//						gsonBean.setType(alarmType);
//						gsonBean.setSendTime(sendTime);
//						//guid = (String) ehcacheUtil.get(uniqueId + "trackStatus",
//								//"guid");
//						//gsonBean.setGuid(guid);
//						appContent = gsonBean;
//						ack = "";
//						try {
//
//							//RocketmqProducer.mqttMain1(list, appContent,uniqueId, "关闭追踪模式成功", sendTime, "0","default");
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					} else if (ack == "FLW" || "FLW".equals(ack)) {
//						alarmType = "12";
//						GsonBean gsonBean = new GsonBean();
//						gsonBean.setUniqueId(uniqueId);
//						gsonBean.setType(alarmType);
//						gsonBean.setSendTime(sendTime);
//						//guid = (String) ehcacheUtil.get(uniqueId + "trackStatus",
//								//"guid");
//						//gsonBean.setGuid(guid);
//						appContent = gsonBean;
//						ack = "";
//						try {
//
//							//RocketmqProducer.mqttMain1(list, appContent,uniqueId, "开启追踪模式成功", sendTime, "0","default");
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					} else if (ack == "GBC" || "GBC".equals(ack)) {
//						alarmType = "15";
//						GsonBean gsonBean = new GsonBean();
//						gsonBean.setUniqueId(uniqueId);
//						gsonBean.setType(alarmType);
//						gsonBean.setSendTime(sendTime);
//						//guid = (String) ehcacheUtil.get(uniqueId
//								//+ "updateDeviceSetting", "guid");
//						//gsonBean.setGuid(guid);
//						appContent = gsonBean;
//						ack = "";
//						try {
//
//							//RocketmqProducer.mqttMain1(list, appContent,uniqueId, "设备设置成功回执", sendTime, "0","default");
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					} else if (ack == "STU" || "STU".equals(ack)) {
//						alarmType = "13";
//						GsonBean gsonBean = new GsonBean();
//						gsonBean.setUniqueId(uniqueId);
//						gsonBean.setType(alarmType);
//						gsonBean.setSendTime(sendTime);
//						gsonBean.setBatPerc(power);
//						gsonBean.setDevNam(devNam);
//						gsonBean.setFirmVer(firmVer);
//						gsonBean.setGSMLevel(gsMLevel);
//						gsonBean.setProVer(proVer);
//						gsonBean.setMovFlag(movFlag);
//						gsonBean.setSensFlag(sensFlag);
//						appContent = gsonBean;
//						ack = "";
//						try {
//							//RocketmqProducer.mqttMain1(list, appContent,uniqueId, "查询设备状态成功回执", sendTime, "0","default");
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//					
//				}
//				if (p != 0) {
//					String paramInfo = altitude.trim() + "," + address + "," + azimuth.trim()
//							+ "," + latitude + "," + longitude + "," + power.trim() + ","
//							+ speed.trim() + "," + alarmType.trim() + "," + uniqueId.trim() + ","
//							+ method.trim() + "," + odoMileage.trim() + "," + time.trim()+ ",no";
//					System.out.println("paramInfo>>>>>>>>>>>>>>>>>>>>" + paramInfo);
//					// 报警、位置等信息入库
//					if (method =="alarm.addAlarm"||"alarm.addAlarm".equals(method)) {
//						interfaceOfGl300.writeValueAlarm(paramInfo.trim());
//					} if(method =="position.addPositions"||"position.addPositions".equals(method)||p==17) {
//						interfaceOfGl300.writeValuePosition(paramInfo.trim());
//					}
//				}
//			
//		} catch (Exception e2) {
//			e2.printStackTrace();
//		}
//		
//		
//		return null;
//
//	}
//
//	// 计算坐标点之间的距离
//	public static double getDistance(double lat_a, double lng_a, double lat_b,
//			double lng_b) {
//		double radLat1 = (lat_a * Math.PI / 180.0);
//		double radLat2 = (lat_b * Math.PI / 180.0);
//		double a = radLat1 - radLat2;
//		double b = (lng_a - lng_b) * Math.PI / 180.0;
//		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
//				+ Math.cos(radLat1) * Math.cos(radLat2)
//				* Math.pow(Math.sin(b / 2), 2)));
//		s = s * EARTH_RADIUS;
//		s = Math.round(s * 10000) / 10000;
//		return s;
//	}
//
//	public List<String> baseStation(String mcc1, String mnc1, String lac1,
//			String cellid1, String rxlevel1, String mcc2, String mnc2,
//			String lac2, String cellid2, String rxlevel2, String mcc3,
//			String mnc3, String lac3, String cellid3, String rxlevel3,
//			String mcc4, String mnc4, String lac4, String cellid4,
//			String rxlevel4, String mcc5, String mnc5, String lac5,
//			String cellid5, String rxlevel5, String mcc6, String mnc6,
//			String lac6, String cellid6, String rxlevel6, String mcc7,
//			String mnc7, String lac7, String cellid7, String rxlevel7,
//			String time, String fixtype, String uniqueId) {
//		List<String> main = new ArrayList<String>();
//		try {
//			int count = 0;
//			System.out.println("进入了基站查询>>>>>>>>>");
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
//			System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
//			List<String> baseStationlist = mysqlRegex.queryBaseStation(lac1,
//					cellid1, lac2, cellid2, lac3, cellid3, lac4, cellid4, lac5,
//					cellid5, lac6, cellid6, lac7, cellid7);
//			String baseStationsString1 = baseStationlist.get(0);
//			String baseStationsString2 = baseStationlist.get(1);
//			String baseStationsString3 = baseStationlist.get(2);
//			String baseStationsString4 = baseStationlist.get(3);
//			String baseStationsString5 = baseStationlist.get(4);
//			String baseStationsString6 = baseStationlist.get(5);
//			String baseStationsString7 = baseStationlist.get(6);
//			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
//			System.out.println(df1.format(new Date()));// new Date()为获取当前系统时间
//			String[] baseStation1 = {};
//			String[] baseStation2 = {};
//			String[] baseStation3 = {};
//			String[] baseStation4 = {};
//			String[] baseStation5 = {};
//			String[] baseStation6 = {};
//			String[] baseStation7 = {};
//			BaseStationModel baseModel1 = null;
//			BaseStationModel baseModel2 = null;
//			BaseStationModel baseModel3 = null;
//			BaseStationModel baseModel4 = null;
//			BaseStationModel baseModel5 = null;
//			BaseStationModel baseModel6 = null;
//			BaseStationModel baseModel7 = null;
//			if (baseStationsString1 != "") {
//				System.out.println("/////////////////////////////");
//				baseStation1 = baseStationsString1.split(",");
//				lac1 = baseStation1[3];
//				cellid1 = baseStation1[4];
//				String longitude1 = (String) ehcacheUtil.get(lac1 + cellid1, "oLng");
//				String latitude1 = (String) ehcacheUtil.get(lac1 + cellid1, "oLat");
//				String address1 = baseStation1[2];
//				System.out.println("address1??????????" + address1);
//				System.out.println("lac1>>>>>>>>>" + lac1);
//				System.out.println("cellid1>>>>>>>>" + cellid1);
//				System.out.println("longitude1???????" + longitude1);
//				System.out.println("latitude1?????" + latitude1);
//				String thisPostion = gtpo.transgpshx(Double.parseDouble(longitude1),Double.parseDouble(latitude1));
//				longitude1 = thisPostion.split(",")[0];
//				latitude1 = thisPostion.split(",")[1];
//				baseModel1 = new BaseStationModel();
//				baseModel1.setId(1);
//				baseModel1.setCell(cellid1);
//				baseModel1.setAddress(address1);
//				baseModel1.setLac(lac1);
//				baseModel1.setMcc(mcc1);
//				baseModel1.setMnc(mnc1);
//				baseModel1.setLng(longitude1);
//				baseModel1.setLat(latitude1);
//				lac1 = Integer.toHexString(Integer.parseInt(lac1)) + "";
//				cellid1 = Integer.toHexString(Integer.parseInt(cellid1)) + "";
//				rxlevel1 = (String) ehcacheUtil.get(lac1 + cellid1, "rxlevel");
//				baseModel1.setRxlevel(rxlevel1);
//				count += 1;
//			}
//			if (baseStationsString2 != "") {
//				baseStation2 = baseStationsString2.split(",");
//				lac2 = baseStation2[3];
//				cellid2 = baseStation2[4];
//				String longitude2 = (String) ehcacheUtil.get(lac2 + cellid2,"oLng");
//				String latitude2 = (String) ehcacheUtil.get(lac2 + cellid2,"oLat");
//				String address2 = baseStation2[2];
//				System.out.println("address2??????????" + address2);
//				System.out.println("lac2>>>>>>>>>" + lac2);
//				System.out.println("cellid2>>>>>>>>" + cellid2);
//				System.out.println("longitude2???????" + longitude2);
//				System.out.println("latitude2?????" + latitude2);
//				String thisPostion = gtpo.transgpshx(Double.parseDouble(longitude2),Double.parseDouble(latitude2));
//				longitude2 = thisPostion.split(",")[0];
//				latitude2 = thisPostion.split(",")[1];
//				baseModel2 = new BaseStationModel();
//				baseModel2.setId(2);
//				baseModel2.setCell(cellid2);
//				baseModel2.setAddress(address2);
//				baseModel2.setLac(lac2);
//				baseModel2.setMcc(mcc2);
//				baseModel2.setMnc(mnc2);
//				baseModel2.setLng(longitude2);
//				baseModel2.setLat(latitude2);
//				lac2 = Integer.toHexString(Integer.parseInt(lac2)) + "";
//				cellid2 = Integer.toHexString(Integer.parseInt(cellid2)) + "";
//				rxlevel2 = (String) ehcacheUtil.get(lac2 + cellid2, "rxlevel");
//				baseModel2.setRxlevel(rxlevel2);
//				count += 1;
//			}
//			if (baseStationsString3 != "") {
//				baseStation3 = baseStationsString3.split(",");
//				lac3 = baseStation3[3];
//				cellid3 = baseStation3[4];
//				String longitude3 = (String) ehcacheUtil.get(lac3 + cellid3,"oLng");
//				String latitude3 = (String) ehcacheUtil.get(lac3 + cellid3,"oLat");
//				String address3 = baseStation3[2];
//				System.out.println("address3??????????" + address3);
//				System.out.println("lac3>>>>>>>>>" + lac3);
//				System.out.println("cellid3>>>>>>>>" + cellid3);
//				System.out.println("longitude3???????" + longitude3);
//				System.out.println("latitude3?????" + latitude3);
//				String thisPostion = gtpo.transgpshx(Double.parseDouble(longitude3),Double.parseDouble(latitude3));
//				longitude3 = thisPostion.split(",")[0];
//				latitude3 = thisPostion.split(",")[1];
//				baseModel3 = new BaseStationModel();
//				baseModel3.setId(3);
//				baseModel3.setCell(cellid3);
//				baseModel3.setAddress(address3);
//				baseModel3.setLac(lac3);
//				baseModel3.setMcc(mcc3);
//				baseModel3.setMnc(mnc3);
//				baseModel3.setLng(longitude3);
//				baseModel3.setLat(latitude3);
//				lac3 = Integer.toHexString(Integer.parseInt(lac3)) + "";
//				cellid3 = Integer.toHexString(Integer.parseInt(cellid3)) + "";
//				rxlevel3 = (String) ehcacheUtil.get(lac3 + cellid3, "rxlevel");
//				baseModel3.setRxlevel(rxlevel3);
//				count += 1;
//
//			}
//			if (baseStationsString4 != "") {
//				baseStation4 = baseStationsString4.split(",");
//				lac4 = baseStation4[3];
//				cellid4 = baseStation4[4];
//				String longitude4 = (String) ehcacheUtil.get(lac4 + cellid4,"oLng");
//				String latitude4 = (String) ehcacheUtil.get(lac4 + cellid4,"oLat");
//				String address4 = baseStation4[2];
//				System.out.println("address4??????????" + address4);
//				System.out.println("lac4>>>>>>>>>" + lac4);
//				System.out.println("cellid4>>>>>>>>" + cellid4);
//				System.out.println("longitude4???????" + longitude4);
//				System.out.println("latitude4?????" + latitude4);
//				String thisPostion = gtpo.transgpshx(Double.parseDouble(longitude4),Double.parseDouble(latitude4));
//				longitude4 = thisPostion.split(",")[0];
//				latitude4 = thisPostion.split(",")[1];
//				baseModel4 = new BaseStationModel();
//				baseModel4.setId(4);
//				baseModel4.setCell(cellid4);
//				baseModel4.setAddress(address4);
//				baseModel4.setLac(lac4);
//				baseModel4.setMcc(mcc4);
//				baseModel4.setMnc(mnc4);
//				baseModel4.setLng(longitude4);
//				baseModel4.setLat(latitude4);
//				lac4 = Integer.toHexString(Integer.parseInt(lac4)) + "";
//				cellid4 = Integer.toHexString(Integer.parseInt(cellid4)) + "";
//				rxlevel4 = (String) ehcacheUtil.get(lac4 + cellid4, "rxlevel");
//				baseModel4.setRxlevel(rxlevel4);
//				count += 1;
//			}
//			if (baseStationsString5 != "") {
//				baseStation5 = baseStationsString5.split(",");
//				lac5 = baseStation5[3];
//				cellid5 = baseStation5[4];
//				String longitude5 = (String) ehcacheUtil.get(lac5 + cellid5,"oLng");
//				String latitude5 = (String) ehcacheUtil.get(lac5 + cellid5,"oLat");
//				String address5 = baseStation5[2];
//				System.out.println("address5??????????" + address5);
//				System.out.println("lac5>>>>>>>>>" + lac5);
//				System.out.println("cellid5>>>>>>>>" + cellid5);
//				System.out.println("longitude5???????" + longitude5);
//				System.out.println("latitude5?????" + latitude5);
//				String thisPostion = gtpo.transgpshx(Double.parseDouble(longitude5),Double.parseDouble(latitude5));
//				longitude5 = thisPostion.split(",")[0];
//				latitude5 = thisPostion.split(",")[1];
//				baseModel5 = new BaseStationModel();
//				baseModel5.setId(5);
//				baseModel5.setCell(cellid5);
//				baseModel5.setAddress(address5);
//				baseModel5.setLac(lac5);
//				baseModel5.setMcc(mcc5);
//				baseModel5.setMnc(mnc5);
//				baseModel5.setLng(longitude5);
//				baseModel5.setLat(latitude5);
//				lac5 = Integer.toHexString(Integer.parseInt(lac5)) + "";
//				cellid5 = Integer.toHexString(Integer.parseInt(cellid5)) + "";
//				rxlevel5 = (String) ehcacheUtil.get(lac5 + cellid5, "rxlevel");
//				baseModel5.setRxlevel(rxlevel5);
//				count += 1;
//
//			}
//			if (baseStationsString6 != "") {
//				baseStation6 = baseStationsString6.split(",");
//				lac6 = baseStation6[3];
//				cellid6 = baseStation6[4];
//				String longitude6 = (String) ehcacheUtil.get(lac6 + cellid6,"oLng");
//				String latitude6 = (String) ehcacheUtil.get(lac6 + cellid6,"oLat");
//				String address6 = baseStation6[2];
//				System.out.println("address6??????????" + address6);
//				System.out.println("lac6>>>>>>>>>" + lac6);
//				System.out.println("cellid6>>>>>>>>" + cellid6);
//				System.out.println("longitude6???????" + longitude6);
//				System.out.println("latitude6?????" + latitude6);
//				String thisPostion = gtpo.transgpshx(Double.parseDouble(longitude6),Double.parseDouble(latitude6));
//				longitude6 = thisPostion.split(",")[0];
//				latitude6 = thisPostion.split(",")[1];
//				baseModel6 = new BaseStationModel();
//				baseModel6.setId(6);
//				baseModel6.setCell(cellid6);
//				baseModel6.setAddress(address6);
//				baseModel6.setLac(lac6);
//				baseModel6.setMcc(mcc6);
//				baseModel6.setMnc(mnc6);
//				baseModel6.setLng(longitude6);
//				baseModel6.setLat(latitude6);
//				lac6 = Integer.toHexString(Integer.parseInt(lac6)) + "";
//				cellid6 = Integer.toHexString(Integer.parseInt(cellid6)) + "";
//				rxlevel6 = (String) ehcacheUtil.get(lac6 + cellid6, "rxlevel");
//				baseModel6.setRxlevel(rxlevel6);
//				count += 1;
//
//			}
//			if (baseStationsString7 != "") {
//				baseStation7 = baseStationsString7.split(",");
//				lac7 = baseStation7[3];
//				cellid7 = baseStation7[4];
//				String longitude7 = (String) ehcacheUtil.get(lac7 + cellid7,
//						"oLng");
//				String latitude7 = (String) ehcacheUtil.get(lac7 + cellid7,
//						"oLat");
//				String address7 = baseStation7[2];
//				System.out.println("address7??????????" + address7);
//				System.out.println("lac7>>>>>>>>>" + lac7);
//				System.out.println("cellid7>>>>>>>>" + cellid7);
//				System.out.println("longitude7???????" + longitude7);
//				System.out.println("latitude7?????" + latitude7);
//				String thisPostion = gtpo.transgpshx(
//						Double.parseDouble(longitude7),
//						Double.parseDouble(latitude7));
//				longitude7 = thisPostion.split(",")[0];
//				latitude7 = thisPostion.split(",")[1];
//				baseModel7 = new BaseStationModel();
//				baseModel7.setId(7);
//				baseModel7.setCell(cellid7);
//				baseModel7.setAddress(address7);
//				baseModel7.setLac(lac7);
//				baseModel7.setMcc(mcc7);
//				baseModel7.setMnc(mnc7);
//				baseModel7.setLng(longitude7);
//				baseModel7.setLat(latitude7);
//				lac7 = Integer.toHexString(Integer.parseInt(lac7)) + "";
//				cellid7 = Integer.toHexString(Integer.parseInt(cellid7)) + "";
//				rxlevel7 = (String) ehcacheUtil.get(lac7 + cellid7, "rxlevel");
//				baseModel7.setRxlevel(rxlevel7);
//				count += 1;
//
//			}
//			if (baseModel7 == null) {
//				System.out.println("baseModel7 == null");
//			}
//			BSUtils bsUtils = new BSUtils();
//			System.out.println("count+=1=====" + count);
//			String[] bsStrings = new String[100];
//			int i = 0;
//
//			if (baseModel1 != null) {
//				bsStrings[i] = baseModel1.getLat();
//				bsStrings[i + 1] = baseModel1.getLng();
//				bsStrings[i + 2] = baseModel1.getRxlevel();
//				bsStrings[i + 3] = 1000 + "";
//				i += 4;
//				System.out.println("i1=" + i);
//			}
//			if (baseModel2 != null) {
//				bsStrings[i] = baseModel2.getLat();
//				bsStrings[i + 1] = baseModel2.getLng();
//				bsStrings[i + 2] = baseModel2.getRxlevel();
//				bsStrings[i + 3] = 1000 + "";
//				i += 4;
//				System.out.println("i2=" + i);
//			}
//			if (baseModel3 != null) {
//				bsStrings[i] = baseModel3.getLat();
//				bsStrings[i + 1] = baseModel3.getLng();
//				bsStrings[i + 2] = baseModel3.getRxlevel();
//				bsStrings[i + 3] = 1000 + "";
//				i += 4;
//				System.out.println("i3=" + i);
//			}
//			if (baseModel4 != null) {
//				bsStrings[i] = baseModel4.getLat();
//				bsStrings[i + 1] = baseModel4.getLng();
//				bsStrings[i + 2] = baseModel4.getRxlevel();
//				bsStrings[i + 3] = 1000 + "";
//				i += 4;
//				System.out.println("i4=" + i);
//			}
//			if (baseModel5 != null) {
//				bsStrings[i] = baseModel5.getLat();
//				bsStrings[i + 1] = baseModel5.getLng();
//				bsStrings[i + 2] = baseModel5.getRxlevel();
//				bsStrings[i + 3] = 1000 + "";
//				i += 4;
//				System.out.println("i5=" + i);
//			}
//			if (baseModel6 != null) {
//				bsStrings[i] = baseModel6.getLat();
//				bsStrings[i + 1] = baseModel6.getLng();
//				bsStrings[i + 2] = baseModel6.getRxlevel();
//				bsStrings[i + 3] = 1000 + "";
//				i += 4;
//				System.out.println("i6=" + i);
//			}
//			if (baseModel7 != null) {
//				System.out.println("baseModel7 != null");
//				bsStrings[i] = baseModel7.getLat();
//				bsStrings[i + 1] = baseModel7.getLng();
//				bsStrings[i + 2] = baseModel7.getRxlevel();
//				bsStrings[i + 3] = 1000 + "";
//				i += 4;
//				System.out.println("i7=" + i);
//			}
//			if (count >= 2) {
//				count *= 4;
//				System.out.println("count" + count);
//				System.out.println("bsStrings.lngth=========="
//						+ bsStrings.length);
//				main = bsUtils.main(count, bsStrings);
//			} else if (count == 1) {
//				main.add(bsStrings[0]);
//				main.add(bsStrings[1]);
//			} else {
//				main.add("0");
//				main.add("0");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return main;
//	}
//
//	// 判断是否出圈
//	public String judgeOnes(String lat, String lng, String uniqueId,
//			String address) throws Exception {
//		Double longitude = Double.parseDouble(lng);// 经度
//		Double latitude = Double.parseDouble(lat);// 纬度
//		Double memcacheLong = 0.0; // 电子围栏设置的经度
//		Double memcacheLat = 0.0;// 电子围栏设置的 纬度
//		Double radius = 0.0;// 电子围栏设置的 半径
//		String cacheInfo = "";
//		String alarmType = "";// 出圈入库用
//
//		if (ehcacheUtil.get(uniqueId + "_DZWL", "messageCache") != null) {
//			cacheInfo = (String) ehcacheUtil.get(uniqueId + "_DZWL",
//					"messageCache");
//			// Log.info("缓存电子围栏====" + cacheInfo);
//			// System.out.println("缓存电子围栏====" + cacheInfo);
//		} else {
//			cacheInfo = interfaceOfGl300.getDZWL(uniqueId);
//			// Log.info("数据库电子围栏===" + cacheInfo);
//			// System.out.println("数据库电子围栏===" + cacheInfo);
//		}
//
//		if (!cacheInfo.equals("")) {
//			String info[] = cacheInfo.split(",");
//			memcacheLong = Double.parseDouble(info[0]);// 经度
//			memcacheLat = Double.parseDouble(info[1]);// 纬度
//			radius = Double.parseDouble(info[2]);// 半径
//			double distance = getDistance(memcacheLat, memcacheLong, latitude,
//					longitude);
//			// System.out.println("====得到的两点之间的距离==" +
//			// distance+"====设置的半径=="+radius);
//			// 首先判断出圈
//			if (distance > radius) {
//				String isExist = "out";
//				String cache2 = "";
//				if (ehcacheUtil.get(uniqueId + "_exist", "messageCache") != null) {
//					cache2 = (String) ehcacheUtil.get(uniqueId + "_exist",
//							"messageCache");
//				}
//
//				if (isExist.equals("out") && cache2.equals("NO")) {
//					alarmType = "3";// alarmType3 代表 出圈
//					SimpleDateFormat format = new SimpleDateFormat(
//							"yyyy-MM-dd HH:mm:ss");
//					Gson gson = new Gson();
//					GsonBean appContent = null;// 手机端用的推送消息
//					String sendTime = format.format(new Date());
//					GsonBean gsonBean = new GsonBean();
//					gsonBean.setLatitude(latitude + "");
//					gsonBean.setLongitude(longitude + "");
//					gsonBean.setUniqueId(uniqueId);
//					gsonBean.setAddress(address);
//					gsonBean.setType(alarmType);
//					gsonBean.setSendTime(sendTime);
//					appContent = gsonBean;
//					/*
//					 * String CONTENT = "报警：超出安全区域,"; CONTENT = CONTENT +
//					 * uniqueId + "," + latitude + "," + longitude + "," +
//					 * address + "," + "beyondSafetyArea";
//					 */
//					// 根据设备ID得到手机号
//					List list = null;
//					list = interfaceOfGl300.getPhoneList(uniqueId);
//					//RocketmqProducer.mqttMain1(list, appContent, uniqueId);
//					// System.out.println("出圈报警出圈报警出圈报警出圈报警出圈报警");
//					ehcacheUtil.replace(uniqueId + "_exist", "YES",
//							"messageCache");
//
//				}
//			} else {
//				String isExist = "in";
//				String cache2 = "";
//				if (ehcacheUtil.get(uniqueId + "_exist", "messageCache") != null) {
//					cache2 = (String) ehcacheUtil.get(uniqueId + "_exist",
//							"messageCache");
//				}
//
//				if (isExist.equals("in") && cache2.equals("YES")) {
//					alarmType = "8";// alarmType 8 代表入圈
//					SimpleDateFormat format = new SimpleDateFormat(
//							"yyyy-MM-dd HH:mm:ss");
//					Gson gson = new Gson();
//					GsonBean appContent = null;// 手机端用的推送消息
//					String sendTime = format.format(new Date());
//					GsonBean gsonBean = new GsonBean();
//					gsonBean.setLatitude(latitude + "");
//					gsonBean.setLongitude(longitude + "");
//					gsonBean.setUniqueId(uniqueId);
//					gsonBean.setAddress(address);
//					gsonBean.setType(alarmType);
//					gsonBean.setSendTime(sendTime);
//					appContent = gsonBean;
//					/*
//					 * String CONTENT = "报警：进入安全区域,"; CONTENT = CONTENT +
//					 * uniqueId + "," + latitude + "," + longitude + "," +
//					 * address + "," + "inSafetyArea";
//					 */
//					// 根据设备ID得到手机号
//					List list = null;
//					list = interfaceOfGl300.getPhoneList(uniqueId);
//					//RocketmqProducer.mqttMain1(list, appContent, uniqueId);
//					ehcacheUtil.replace(uniqueId + "_exist", "NO",
//							"messageCache");
//					alarmType = "8";// alarmType 8 代表入圈
//				}
//			}
//		}
//
//		return alarmType;
//	}
//
//}
