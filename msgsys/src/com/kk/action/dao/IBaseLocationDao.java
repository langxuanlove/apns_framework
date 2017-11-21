package com.kk.action.dao;

import java.util.List;

import com.kk.utils.gps.BaseStationModel;

public interface IBaseLocationDao {

	/**
	 * 根据设备返回的基站cellid,去数据库查询基站所在的经纬度
	 * @param lac1
	 * @param cellid1
	 * @param lac2
	 * @param cellid2
	 * @param lac3
	 * @param cellid3
	 * @param lac4
	 * @param cellid4
	 * @param lac5
	 * @param cellid5
	 * @param lac6
	 * @param cellid6
	 * @param lac7
	 * @param cellid7
	 * @return
	 * @throws Exception
	 */
	public  BaseStationModel queryBaseStation(List<BaseStationModel> list)
			throws Exception ;
}
