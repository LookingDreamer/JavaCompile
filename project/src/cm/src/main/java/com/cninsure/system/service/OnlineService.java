package com.cninsure.system.service;
public interface OnlineService {
	/**
	 * 用户线下线
	 * @param usercode
	 * @param onlinestatus 0 下线 1 上线
	 * @return
	 */
	public boolean changeOnlinestatus(String usercode, int onlinestatus);
	/**
	 * 启动初始化所有openfire在线的用户
	 * @param userCodes
	 * @param onlinestatus 0 下线 1 上线
	 */
	public void changeAllOnlinestatus(String[] userCodes, int onlinestatus);

}
