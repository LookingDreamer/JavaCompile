package com.cninsure.system.manager.scm;

import java.util.Map;
/**
 * 同步代理人
 * @author Administrator
 *
 */
public interface INSASyncService {

	public Map<String, Object> getAgentData(String operator, String agentcode);
	
	public Map<String, Object> getAgentDataorOrg(String operator, String orgcode);

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
