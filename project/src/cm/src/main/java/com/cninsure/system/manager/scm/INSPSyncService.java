package com.cninsure.system.manager.scm;

import java.util.Map;

public interface INSPSyncService {

		/**
		 * 同步供应商数据
		 * @param operator
		 * @return
		 */
		Map<String, Object> getSyncProviderData(String operator);
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
