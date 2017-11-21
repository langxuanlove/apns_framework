package com.kk.action.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kk.action.dao.IBaseLocationDao;
import com.kk.action.service.IBaseLocationService;
import com.kk.utils.gps.BaseStationModel;

@Service(value = "baseLocationService")
public class BaseLocationServiceImpl implements IBaseLocationService {
	@Resource(name = "baseLocationDao")
	private IBaseLocationDao baseLocationDao;

	/**
	 * 根据基站返回的cellID,查询基站的经纬度
	 */
	@Transactional
	@Override
	public BaseStationModel queryBaseStation(List<BaseStationModel> list)
			throws Exception {
		return baseLocationDao.queryBaseStation(list);
	}

}
