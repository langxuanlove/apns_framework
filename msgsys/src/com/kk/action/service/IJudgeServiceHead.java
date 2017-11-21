package com.kk.action.service;

public interface IJudgeServiceHead {
	
	/**
	 * 根据业务逻辑判断信息属于哪类报警信息 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public String judgeServiceType(String message)throws Exception;
}
