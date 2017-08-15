package com.cninsure.system.manager.scm;

import java.util.Map;

public interface INSCSyncService {

	/**
	 * 同步机构数据
	 * @param operator
	 * @return
	 */
	Map<String, Object> getSyncDeptData(String operator,String comcode);

	/**
	 * 同步的总个数
	 * @return
	 */
	public long getSyncCount();

	/**
	 * 同步到第几个
	 * @return
	 */
	public long getSyncProcess();
	
}