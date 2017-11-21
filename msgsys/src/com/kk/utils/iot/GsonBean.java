package com.kk.utils.iot;

import io.netty.channel.group.DefaultChannelGroup;

import java.util.List;


public class GsonBean {
	// private String alarmType;// 类型
	private String uniqueId;// 设备ID
	private String latitude;// 纬度
	private String longitude;// 经度
	private String address;// 地址
	private static DefaultChannelGroup channelGroup; // 存放channelGroup
	private List UserDevicesInfoList;
	private String phone;// 设备关联的手机号
	private String requestTime;// 请求时间
	private String sendType;// 是否发送过信息
	private String radius;// 电子围栏半径
	private String result;// http链接的返回值
	private String fenceEnabled;// 电子围栏是否可用，1为可用，2为不可用
	private String nickName;// 设备昵称
	private String user;// 设备绑定的手机号
	private String sendTime;// 发送报警的时间
	// private String at;
	private String type;
	private String azimuth;// 航向
	private String speed;// 速度
	private String guid;// 速度
	private String DevNam;// 速度
	private String HardVer;// 速度
	private String FirmVer;// 速度
	private String ProVer;// 速度
	private String PhoNum;// 速度
	private String MovFlag;// 速度
	private String BatPerc;// 速度
	private String SensFlag;// 速度
	private String GSMLevel;// 速度
	private String isGroups;// 速度
	
	public String getIsGroups() {
		return isGroups;
	}

	public void setIsGroups(String isGroups) {
		this.isGroups = isGroups;
	}

	public String getIsIos() {
		return isIos;
	}

	public void setIsIos(String isIos) {
		this.isIos = isIos;
	}

	private String isIos;// 设备绑定的手机号

	private String locationType;

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getDevNam() {
		return DevNam;
	}

	public void setDevNam(String devNam) {
		DevNam = devNam;
	}

	public String getHardVer() {
		return HardVer;
	}

	public void setHardVer(String hardVer) {
		HardVer = hardVer;
	}

	public String getFirmVer() {
		return FirmVer;
	}

	public void setFirmVer(String firmVer) {
		FirmVer = firmVer;
	}

	public String getProVer() {
		return ProVer;
	}

	public void setProVer(String proVer) {
		ProVer = proVer;
	}

	public String getPhoNum() {
		return PhoNum;
	}

	public void setPhoNum(String phoNum) {
		PhoNum = phoNum;
	}

	public String getMovFlag() {
		return MovFlag;
	}

	public void setMovFlag(String movFlag) {
		MovFlag = movFlag;
	}

	public String getBatPerc() {
		return BatPerc;
	}

	public void setBatPerc(String batPerc) {
		BatPerc = batPerc;
	}

	public String getSensFlag() {
		return SensFlag;
	}

	public void setSensFlag(String sensFlag) {
		SensFlag = sensFlag;
	}

	public String getGSMLevel() {
		return GSMLevel;
	}

	public void setGSMLevel(String gSMLevel) {
		GSMLevel = gSMLevel;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(String azimuth) {
		this.azimuth = azimuth;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/*
	 * public String getAt() { return at; }
	 * 
	 * public void setAt(String at) { this.at = at; }
	 */

	public GsonBean() {
		super();
	}

	/*
	 * public String getAlarmType() { return alarmType; }
	 * 
	 * public void setAlarmType(String alarmType) { this.alarmType = alarmType;
	 * }
	 */
	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public DefaultChannelGroup getChannelGroup() {
		return channelGroup;
	}

	public void setChannelGroup(DefaultChannelGroup channelGroup) {
		this.channelGroup = channelGroup;
	}

	public List getUserDevicesInfoList() {
		return UserDevicesInfoList;
	}

	public void setUserDevicesInfoList(List userDevicesInfoList) {
		UserDevicesInfoList = userDevicesInfoList;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getFenceEnabled() {
		if ("".equals(fenceEnabled) || "" == fenceEnabled
				|| null == fenceEnabled) {
			fenceEnabled = "0.0";
		}
		return fenceEnabled;
	}

	public void setFenceEnabled(String fenceEnabled) {
		this.fenceEnabled = fenceEnabled;
	}

	/*
	 * public String getCall() { return call; }
	 * 
	 * public void setCall(String call) { this.call = call; }
	 */

	public String getUser() {
		return user;
	}

	

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
}
