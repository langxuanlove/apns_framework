package com.kk.action.service;

import java.util.List;

import com.kk.utils.gps.BaseStationModel;

public interface IBaseLocationService {

	/**
	 * 查询数据库，输出符合要求的记录的情况
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
	public BaseStationModel queryBaseStation(List<BaseStationModel> list)throws Exception;
}
