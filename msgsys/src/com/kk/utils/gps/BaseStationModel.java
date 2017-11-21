package com.kk.utils.gps;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//基站数据model
public class BaseStationModel {
	private int id;
	private String mcc;//移动国家号
	private String mnc;//移动网号;MNC=0 中国移动，MNC=1 中国联通
	private String lac;//位置区码
	private String cell;//小区识别
	private String lng;//经度
	private String lat;//纬度
	private String b_lng;//是偏移过的经度,可直接用于GOOGLE地图
	private String b_lat;//是偏移过的经度,可直接用于GOOGLE地图
	private String precision;//是覆盖范围（米），半径，圆形
	private String address;//中文位置描述
	private String rxlevel;//信号强度值

	public String getRxlevel() {
		if (rxlevel == null || rxlevel.equals("")) {
			rxlevel = "99";
		}
		System.out.println("rxlevel==" + rxlevel);
		return rxlevel;
	}

	public void setRxlevel(String rxlevel) {
		if (rxlevel == null || rxlevel.equals("")) {
			this.rxlevel = "99";
		}
		this.rxlevel = rxlevel;
	}

	public BaseStationModel() {
		super();
	}

	/**
	 * @return 获 得 id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param i
	 *            设置 id
	 */
	public void setId(int i) {
		this.id = i;
	}

	/**
	 * @return 获得 mcc
	 */
	public String getMcc() {
		return mcc;
	}

	/**
	 * @param mcc
	 *            设置 mcc
	 */
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	/**
	 * @return 获得 mnc
	 */
	public String getMnc() {
		return mnc;
	}

	/**
	 * @param i
	 *            设置 mnc
	 */
	public void setMnc(String i) {
		this.mnc = i;
	}

	/**
	 * @return 获得 lac
	 */
	public String getLac() {
		return lac;
	}

	/**
	 * @param lac
	 *            设置 lac
	 */
	public void setLac(String lac) {
		this.lac = lac;
	}

	/**
	 * @return 获得 cell
	 */
	public String getCell() {
		return cell;
	}

	/**
	 * @param cell
	 *            设置 cell
	 */
	public void setCell(String cell) {
		this.cell = cell;
	}

	/**
	 * @return 获得 lng
	 */
	public String getLng() {
		return lng;
	}

	/**
	 * @param lng
	 *            设置 lng
	 */
	public void setLng(String lng) {
		this.lng = lng;
	}

	/**
	 * @return 获得 lat
	 */
	public String getLat() {
		return lat;
	}

	/**
	 * @param lat
	 *            设置 lat
	 */
	public void setLat(String lat) {
		this.lat = lat;
	}

	/**
	 * @return 获得 b_lng
	 */
	public String getB_lng() {
		return b_lng;
	}

	/**
	 * @param b_lng
	 *            设置 b_lng
	 */
	public void setB_lng(String b_lng) {
		this.b_lng = b_lng;
	}

	/**
	 * @return 获得 b_lat
	 */
	public String getB_lat() {
		return b_lat;
	}

	/**
	 * @param b_lat
	 *            设置 b_lat
	 */
	public void setB_lat(String b_lat) {
		this.b_lat = b_lat;
	}

	/**
	 * @return 获得 precision
	 */
	public String getPrecision() {
		return precision;
	}

	/**
	 * @param precision
	 *            设置 precision
	 */
	public void setPrecision(String precision) {
		this.precision = precision;
	}

	/**
	 * @return 获得 address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            设置 address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
}

// combination();
// double lat_a, double lng_a, double lat_b, double lng_b
// System.err.println(distance(31.294607,121.337825,31.2887316,121.3428055));
// System.err.println(getDistance(-54));

// BaseStationPosition stationPosition = new BaseStationPosition();
// PlaneCoordinate coordinate = stationPosition.getPlanCoordinate(121.337825,
// 31.294607);
// System.err.println(coordinate.getX()+","+coordinate.getY());
// Point point = stationPosition.getLngLat(coordinate.getX(),
// coordinate.getY());
//
// System.err.println(point.getLatitude()+"," + point.getLongitude());

