package com.kk.action.dao;

public interface IDrfDao {
	
	
	/**
	 * 通过uniqueId获得NickName值
	 * @param uniqueId
	 * @return
	 * @throws Exception
	 */
	public String getNickName(String uniqueId)throws Exception;
}
