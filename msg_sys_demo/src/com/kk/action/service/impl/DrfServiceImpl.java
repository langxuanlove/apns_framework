package com.kk.action.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kk.action.dao.IDrfDao;
import com.kk.action.service.IDrfService;

@Service(value = "drfService")
public class DrfServiceImpl implements IDrfService{
	@Resource(name="drfDao")
	private IDrfDao drfDao;
	public String getNickName(String uniqueId) throws Exception{
		return drfDao.getNickName(uniqueId);
	};
}
