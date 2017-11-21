package com.kk.utils.iot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class MysqlRegex {
	// 创建静态全局变量
	static Connection conn;

	static Statement st;

//	static MemCached cache = MemCached.getInstance();
	static EhcacheUtil ehcacheUtil=new EhcacheUtil();
	
	// public static void main(String[] args) {
	// insert(); //插入添加记录
	// update(); //更新记录数据
	// delete(); //删除记录
	// query(); //查询记录并显示
	// getInfo();
	// }

	/* 插入数据记录，并输出插入的数据记录数 */
	public static void insert() {
		conn = getConnection(); // 首先要获取连接，即连接到数据库
		String messageInfo="+RESP:GTNMR,ProtocolVersion,UniqueID,DeviceName,ReportID,ReportType,Number,GPSAccuracy,Speed,Azimuth,Altitude,Longitude,Latitude,GPSUTCtime,MCC,MNC,LAC,CellID,OdoMileage,batteryPercentage,SendTime,CountNumber,TailCharacter";
		String messageEg="+RESP:GTNMR,1A0000100002,860000599000000000000000057596,GL300000000,0000,0000,1,0000,0000.0000,0000,238.1,116.327833,39.961949,20000140000700008221200007,0000460000,0000000000001,10000C5,CE5F,0000.5,60000,20000140000700009000084722,2B95$";
		String regex=  "\\\\+RESP:GTNMR," +
		    "[0000-9a-fA-F]{6}," +                 // Protocol version
		    "(\\\\d{15}),.*," +                   // IMEI
		    "(\\\\d*)," +                         // GPS accuracy
		    "(\\\\d+.\\\\d)," +                     // Speed
		    "(\\\\d+)," +                         // Azimuth
		    "(-?\\\\d+\\\\.\\\\d)," +                 // Altitude
		    "(-?\\\\d+\\\\.\\\\d+)," +                // Longitude
		    "(-?\\\\d+\\\\.\\\\d+)," +                // Latitude
		    "(\\\\d{4}+\\\\d{2}+\\\\d{2}+\\\\d{2}+\\\\d{2}+\\\\d{2})," +        // GPS UTC time (YYYYMMDD)
		    "(\\\\d{4})," +                       // MCC
		    "(\\\\d{4})," +                       // MNC
		    "(\\\\p{XDigit}{4})," +               // LAC
		    "(\\\\p{XDigit}{4})," +               // Cell
			"(\\\\d+\\\\.\\\\d+)," +              // Odo mileage  
		    "(\\\\d+)?" +     // Battery
		    ".*";
		String regexInfo="Protocolversion,(IMEI)+*,(GPSaccuracy),(Speed),(Azimuth),(Altitude),(Longitude),(Latitude),(GPSUTC),(mcc),(mnc),(lac),(cell),(Odomileage),(Battery)+*";
		String protocolVersion="1A0000100002";
		
		try {// 消息id,设备厂商,设备型号 ,协议头,协议内容,协议例子 ,正则表达式,正则表达式每项的含义
			String sql = "INSERT INTO message(messageid,vendor, model, messageHead,messageInfo, messageEg, regex,regexInfo,protocolVersion)"
					+ " VALUES ('9','移为', 'gl300000000', '+RESP:GTNMR','"
					+ messageInfo
					+ "','"
					+ messageEg
					+ "','"
					+ regex
					+ "','"
					+ regexInfo + "','"
							+ protocolVersion + "')"; // 插入数据的sql语句

			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象

			int count = st.executeUpdate(sql); // 执行插入操作的sql语句，并返回插入数据的个数

			conn.close(); // 关闭数据库连接

		} catch (SQLException e) {
			System.out.println("插入数据失败" + e.getMessage());
		}
	}
	public static void insertCatchzhuolist(String  uniqueId,String atInstruction,String guid,String deviceType,String SetNum,String type) {
		conn = getConnection(); // 首先要获取连接，即连接到数据库
		
		try {// 消息id,设备厂商,设备型号 ,协议头,协议内容,协议例子 ,正则表达式,正则表达式每项的含义
			String sql = "insert  into `catchzhuolist`(`uniqueId`,`atInstruction`,`guid`,`deviceType`,`setnum`,`type`)"
					+ " VALUES ('"
					+ uniqueId
					+ "','"
					+ atInstruction
					+ "','"
					+ guid
					+ "','"
					+ deviceType
					+"','"
					+ SetNum
					+"','"
					+ type
					+ "')"; // 插入数据的sql语句
			System.out.println("sql========?????????"+sql);
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象
			int count = st.executeUpdate(sql); // 执行插入操作的sql语句，并返回插入数据的个数
			conn.close(); // 关闭数据库连接
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("插入数据失败" + e.getMessage());
		}
	}
	
	public static int insertMessagelist(String uniqueId, String appContentMsg) {
		int count=0;
		conn = getConnection(); // 首先要获取连接，即连接到数据库
		
		try {// 消息id,设备厂商,设备型号 ,协议头,协议内容,协议例子 ,正则表达式,正则表达式每项的含义
			String sql = "insert  into `messagelist`(`uniqueId`,`appContentMsg`)"
					+ " VALUES ('"
					+ uniqueId
					+ "','"
					+ appContentMsg
					+ "')"; // 插入数据的sql语句
			System.out.println("sql========?????????"+sql);
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象
			 count = st.executeUpdate(sql); // 执行插入操作的sql语句，并返回插入数据的个数
			conn.close(); // 关闭数据库连接
		} catch (SQLException e) {
			 count=0;
			e.printStackTrace();
			System.out.println("插入数据失败" + e.getMessage());
		}
		return count;
	}
	public static void updateCatchzhuolist(String  uniqueId,String atInstruction,String guid,String deviceType,String SetNum,String type) {
		conn = getConnection(); // 首先要获取连接，即连接到数据库
		
		try {// 消息id,设备厂商,设备型号 ,协议头,协议内容,协议例子 ,正则表达式,正则表达式每项的含义
			String sql ="UPDATE `catchzhuolist` SET `atInstruction`='"+atInstruction+"' ,`guid` ='"+guid+"',`deviceType`='"+deviceType+"',`setnum`='"+SetNum+"'  WHERE `uniqueId` ='"+uniqueId+"' AND `type` ='"+type+"'"; // 插入数据的sql语句

			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象

			int count = st.executeUpdate(sql); // 执行插入操作的sql语句，并返回插入数据的个数

			conn.close(); // 关闭数据库连接

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("更新数据失败" + e.getMessage());
		}
	}
	public static int queryCatchzhuolistCount(String uniqueId) {
		conn = getConnection(); // 同样先要获取连接，即连接到数据库
		try {
			String sql = "SELECT COUNT(1) FROM `catchzhuolist`  WHERE `uniqueId` ='"+uniqueId +"'"; // 查询数据的sql语句
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象，st属局部变量
			ResultSet rs = st.executeQuery(sql); // 执行sql查询语句，返回查询数据的结果集
			rs.next();
			int count = rs.getInt(1);
			conn.close(); // 关闭数据库连接
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("查询数据失败");
			return 0;
		}
	}
	public static int queryMessagelistCount() {
		conn = getConnection(); // 同样先要获取连接，即连接到数据库
		try {
			String sql = "SELECT COUNT(1) FROM `messagelist`"; // 查询数据的sql语句
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象，st属局部变量
			ResultSet rs = st.executeQuery(sql); // 执行sql查询语句，返回查询数据的结果集
			rs.next();
			int count = rs.getInt(1);
			conn.close(); // 关闭数据库连接
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("查询数据失败");
			return 0;
		}
	}
	public static int queryCatchzhuolistCountAndType(String uniqueId,String type) {
		conn = getConnection(); // 同样先要获取连接，即连接到数据库
		try {
			String sql = "SELECT COUNT(1) FROM `catchzhuolist` WHERE `uniqueId` ='"+uniqueId +"' AND `type` ='"+type +"'"; // 查询数据的sql语句
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象，st属局部变量
			ResultSet rs = st.executeQuery(sql); // 执行sql查询语句，返回查询数据的结果集
			rs.next();
			int count = rs.getInt(1);
			conn.close(); // 关闭数据库连接
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("查询数据失败");
			return 0;
		}
	}
	public static int deleteCatchzhuolistCountAndType(String uniqueId,String type) {
		conn = getConnection(); // 同样先要获取连接，即连接到数据库
		try {
			String sql = "DELETE FROM `catchzhuolist` WHERE `uniqueId` ='"+uniqueId +"' AND `type` ='"+type +"'"; // 查询数据的sql语句
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象
			int count = st.executeUpdate(sql); // 执行插入操作的sql语句，并返回插入数据的个数

			conn.close(); // 关闭数据库连接
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("查询数据失败");
			return 0;
		}
	}
	public static int deleteMessagelist(String uniqueId,String appContentMsg) {
		conn = getConnection(); // 同样先要获取连接，即连接到数据库
		int count=0;
		try {
			String sql = "DELETE FROM `messagelist` WHERE `uniqueId` ='"+uniqueId +"' AND `appContentMsg` ='"+appContentMsg +"'"; // 查询数据的sql语句
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象
			count = st.executeUpdate(sql); // 执行插入操作的sql语句，并返回插入数据的个数
			conn.close(); // 关闭数据库连接
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("查询数据失败");
			//return 0;
		}
		return count;
	}
	public static List<String>  queryCatchzhuolist(String uniqueId) {
		conn = getConnection(); // 同样先要获取连接，即连接到数据库
		try {
			List<String> list=new ArrayList<String>();
			String sql = "SELECT atInstruction,guid,setnum,type FROM `catchzhuolist`  WHERE `uniqueId` ='"+uniqueId +"'"; // 查询数据的sql语句
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象，st属局部变量
			ResultSet rs = st.executeQuery(sql); // 执行sql查询语句，返回查询数据的结果集
			while (rs.next()) { // 判断是否还有下一个数据
				String atInstruction = rs.getString("atInstruction");
				String guid = rs.getString("guid");
				String SetNum = rs.getString("setnum");
				String type = rs.getString("type");
				list.add(atInstruction);
				list.add(type);
				//ehcacheUtil.put(uniqueId+SetNum, guid, "guid");
			}
			conn.close(); // 关闭数据库连接
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("查询数据失败");
			return null;
		}
	}
	/* 查询数据库，输出符合要求的记录的情况 */
	public static void query() {
		conn = getConnection(); // 同样先要获取连接，即连接到数据库
		try {
			String sql = "select model,messageHead,messageInfo,regex from message"; // 查询数据的sql语句
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象，st属局部变量

			ResultSet rs = st.executeQuery(sql); // 执行sql查询语句，返回查询数据的结果集

			while (rs.next()) { // 判断是否还有下一个数据

				// 根据字段名获取相应的值 (messageid,vendor, model,
				// messageHead,messageInfo, messageEg, regex,regexInfo)"
				//String messageid = rs.getString("messageid");
				//String vendor = rs.getString("vendor");
				String model = rs.getString("model");
				String messageHead = rs.getString("messageHead");
				String messageInfo = rs.getString("messageInfo");
				//String messageEg = rs.getString("messageEg");
				String regex = rs.getString("regex");
				//String regexInfo = rs.getString("regexInfo");
				//String protocolVersion = rs.getString("protocolVersion");

				// 输出查到的记录的各个字段的值
			//	System.out.println(messageid + "===" + vendor + " ===" + model);
			//	System.out.println( messageHead + " ===" + messageInfo );
			//	System.out.println( messageEg + "======" + regexInfo+"==="+protocolVersion);
				System.out.println(  regex );

			//	cache.add(model + messageHead, regex);
				ehcacheUtil.put(model + messageHead, regex,"messageCache");
				ehcacheUtil.put(messageHead+model+"messageInfo", messageInfo, "messageInfo");
				Object object = ehcacheUtil.get(model + messageHead, "messageCache");
				System.out.println("00000"+object);
			}
			conn.close(); // 关闭数据库连接

		} catch (SQLException e) {
			System.out.println("查询数据失败");
		}
	}

	// 删除符合要求的记录，输出情况
	public static void delete() {

		conn = getConnection(); // 同样先要获取连接，即连接到数据库
		try {
			String sql = "select * from message"; // 查询数据的sql语句
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象，st属局部变量
			ResultSet rs = st.executeQuery(sql); // 执行sql查询语句，返回查询数据的结果集
			while (rs.next()) { // 判断是否还有下一个数据
				// 根据字段名获取相应的值 (messageid,vendor, model,
				// messageHead,messageInfo, messageEg, regex,regexInfo)"
				String model = rs.getString("model");
				String messageHead = rs.getString("messageHead");
			//	cache.delete(model + messageHead);
				ehcacheUtil.remove(model + messageHead,"messageCache");
			}
			conn.close(); // 关闭数据库连接

		} catch (SQLException e) {
			System.out.println("查询数据失败");
		}

	}
	/* 查询数据库，输出符合要求的记录的情况 */
	public List<String> queryBaseStation(String lac1, String cellid1, String lac2,String cellid2,
			String lac3, String cellid3,String lac4, String cellid4, String lac5, String cellid5,String lac6,String cellid6,String lac7, String cellid7) {
		List<String> list=new ArrayList<String>();
		if (lac1=="0000"||cellid1=="0000"||"0000".equals(lac1)||"0000".equals(cellid1)||lac1=="0"||cellid1=="0"||"0".equals(lac1)||"0".equals(cellid1)||lac1==null||cellid1==null||lac1==""||cellid1==""||"".equals(lac1)||"".equals(cellid1)) {
			cellid1="";
			lac1="";
		}
		else {
			cellid1=Integer.parseInt(cellid1, 16)+"".trim();
			lac1=Integer.parseInt(lac1, 16)+"".trim();
		}
		if (lac2=="0000"||cellid2=="0000"||"0000".equals(lac2)||"0000".equals(cellid2)||lac2=="0"||cellid2=="0"||"0".equals(lac2)||"0".equals(cellid2)||lac2==null||cellid2==null||lac2==""||cellid2==""||"".equals(lac2)||"".equals(cellid2)) {
			cellid2="";
			lac2="";
		}else {
			cellid2=Integer.parseInt(cellid2, 16)+"".trim();
			lac2=Integer.parseInt(lac2, 16)+"".trim();
		}
		if (lac3=="0000"||cellid3=="0000"||"0000".equals(lac3)||"0000".equals(cellid3)||lac3=="0"||cellid3=="0"||"0".equals(lac3)||"0".equals(cellid3)||lac3==null||cellid3==null||lac3==""||cellid3==""||"".equals(lac3)||"".equals(cellid3)) {
			cellid3="";
			lac3="";
		}else {
			cellid3=Integer.parseInt(cellid3, 16)+"".trim();
			lac3=Integer.parseInt(lac3, 16)+"".trim();
		}
		if (lac4=="0000"||cellid4=="0000"||"0000".equals(lac4)||"0000".equals(cellid4)||lac4=="0"||cellid4=="0"||"0".equals(lac4)||"0".equals(cellid4)||lac4==null||cellid4==null||lac4==""||cellid4==""||"".equals(lac4)||"".equals(cellid4)) {
			cellid4="";
			lac4="";
		}else {
			cellid4=Integer.parseInt(cellid4, 16)+"".trim();
			lac4=Integer.parseInt(lac4, 16)+"".trim();
		}
		if (lac5=="0000"||cellid5=="0000"||"0000".equals(lac5)||"0000".equals(cellid5)||lac5=="0"||cellid5=="0"||"0".equals(lac5)||"0".equals(cellid5)||lac5==null||cellid5==null||lac5==""||cellid5==""||"".equals(lac5)||"".equals(cellid5)) {
			cellid5="";
			lac5="";
		}else {
			cellid5=Integer.parseInt(cellid5, 16)+"".trim();
			lac5=Integer.parseInt(lac5, 16)+"".trim();
		}
		if (lac6=="0000"||cellid6=="0000"||"0000".equals(lac6)||"0000".equals(cellid6)||lac6=="0"||cellid6=="0"||"0".equals(lac6)||"0".equals(cellid6)||lac6==null||cellid6==null||lac6==""||cellid6==""||"".equals(lac6)||"".equals(cellid6)) {
			cellid6="";
			lac6="";
		}else {
			cellid6=Integer.parseInt(cellid6, 16)+"".trim();
			lac6=Integer.parseInt(lac6, 16)+"".trim();
		}
		if (lac7=="0000"||cellid7=="0000"||"0000".equals(lac7)||"0000".equals(cellid7)||lac7=="0"||cellid7=="0"||"0".equals(lac7)||"0".equals(cellid7)||lac7==null||cellid7==null||lac7==""||cellid7==""||"".equals(lac7)||"".equals(cellid7)) {
			cellid7="";
			lac7="";
			
		}else {
			cellid7=Integer.parseInt(cellid7, 16)+"".trim();
			lac7=Integer.parseInt(lac7, 16)+"".trim();
		}
		conn = getConnection1(); // 同样先要获取连接，即连接到数据库
		try {
			String sql = "select lac,ci,lat,lon,addr from cellinfo_addr where (lac='"+lac1+"' and ci='"+cellid1+"') or (lac='"+lac2+"' and ci='"+cellid2+"') or (lac='"+lac3+"' and ci='"+cellid3+"') or (lac='"+lac4+"' and ci='"+cellid4+"') or (lac='"+lac5+"' and ci='"+cellid5+"') or (lac='"+lac6+"' and ci='"+cellid6+"') or (lac='"+lac7+"' and ci='"+cellid7+"')";
			System.out.println("sql>>>>>>>>>>>>>>>>>"+sql);
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象，st属局部变量
			ResultSet rs = st.executeQuery(sql); // 执行sql查询语句，返回查询数据的结果集
			String oLng ="";
			String oLat ="";
			String address = "";
			String lac = "";
			String ci = "";
			if (rs != null) {
				while (rs.next()) { // 判断是否还有下一个数据
					//根据字段名获取相应的值 (messageid,vendor, model,
					oLng = rs.getString("lon");
					oLat = rs.getString("lat");
					address = rs.getString("addr");
					lac=rs.getString("lac");
					ci=rs.getString("ci");
					ehcacheUtil.put(lac + ci, oLng, "oLng");
					ehcacheUtil.put(lac + ci, oLat, "oLat");
					String basesation=oLng + "," + oLat + "," +address+ ","+lac+ ","+ci;
					System.out.println("kukubasesation>>>>>"+basesation);
					list.add(basesation);
				}
				conn.close(); // 关闭数据库连接
				do {
					//String basesation1="0000.0000" + "," + "0000.0000" + "," +address;
					list.add("");
				} while (list.size()!=8);
				return list;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public  void getInfo() {
	//	System.out.println(cache.get("gl300000000+RESP:GTSOS"));

		String regex ="\\+RESP:GTGSM,[0000-9a-fA-F]{6},(\\d{15}),([A-Z]{3}),(\\d*),(\\d*),([0000-9a-fA-F]*),([0000-9a-fA-F]*),(\\d*),,(\\d*),(\\d*),([0000-9a-fA-F]*),([0000-9a-fA-F]*),(\\d*),,(\\d*),(\\d*),([0000-9a-fA-F]*),([0000-9a-fA-F]*),(\\d*),,(\\d*),(\\d*),([0000-9a-fA-F]*),([0000-9a-fA-F]*),(\\d*),,(\\d*),(\\d*),([0000-9a-fA-F]*),([0000-9a-fA-F]*),(\\d*),,(\\d*),(\\d*),([0000-9a-fA-F]*),([0000-9a-fA-F]*),(\\d*),,(\\d*),(\\d*),([0000-9a-fA-F]*),([0000-9a-fA-F]*),(\\d*),\\d*,(\\d*),(\\d+)?.*";
		
		String gtfri = "+RESP:GTGSM,1A0000100002,860000599000000000000000056663,FRI,0000460000,0000000000001,10000c5,b23f,44,,0000460000,0000000000001,10000c5,e563,33,,0000460000,0000000000001,10000c5,e562,32,,0000460000,0000000000001,10000c5,ce60000,30000,,0000460000,0000000000001,10000c5,ca68,29,,0000460000,0000000000001,10000c5,4e5d,28,,0000460000,0000000000001,10000c5,ce5f,42,00000000,20000141230000160000525,4336$";

		Pattern patterngtrtl = Pattern.compile(regex);
			
		Matcher matchergtfri = patterngtrtl.matcher(gtfri);
		
		if (matchergtfri.matches()) {
			System.out.println("匹配成功==========");
		} else {
			System.out.println("匹配不成功-------------");
		}
		String 	power = matchergtfri.group(14);// 电量
		String 	odo = matchergtfri.group(13);// 电量
		System.out.println("power=="+power+"====odo===="+odo);
		
	}

	/* 获取数据库连接的函数 */
	public static Connection getConnection() {

		GetConfig getConfig =new GetConfig();
		String driver =getConfig.getServerIP("database.driver");// 获取服务器IP
		String url = getConfig.getServerIP("database.url"); // 消息服务器ip
		String user = getConfig.getServerIP("database.user");// 连接的
		String password = getConfig.getServerIP("database.password");// 签名密钥

		Connection con = null; // 创建用于连接数据库的Connection对象
		try {

			Class.forName(driver);// 加载Mysql数据驱动
			con = DriverManager.getConnection(url, user, password);// 创建数据连接

		} catch (Exception e) {
			System.out.println("数据库连接失败" + e.getMessage());
		}
		return con; // 返回所建立的数据库连接
	}
	public static Connection getConnection1() {
		GetConfig getConfig = new GetConfig();
		String driver = getConfig.getServerIP("database.driver");// 获取服务器IP
		String url = getConfig.getServerIP("database1.url"); // 消息服务器ip
		String user = getConfig.getServerIP("database1.user");// 连接的
		String password = getConfig.getServerIP("database1.password");// 签名密钥
		Connection con = null; // 创建用于连接数据库的Connection对象
		try {
			Class.forName(driver);// 加载Mysql数据驱动
			con = DriverManager.getConnection(url, user, password);// 创建数据连接
		} catch (Exception e) {
			System.out.println("数据库连接失败" + e.getMessage());
		}
		return con; // 返回所建立的数据库连接
	}
	
}
