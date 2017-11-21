package com.kk.action.service;

public interface IDrfService {

	/**
	 * 通过uniqueId获得NickName值
	 * @param uniqueId
	 * @return
	 * @throws Exception
	 */
	public String getNickName(String uniqueId) throws Exception;
}
