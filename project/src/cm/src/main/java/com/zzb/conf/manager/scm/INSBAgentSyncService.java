package com.zzb.conf.manager.scm;

import com.cninsure.system.entity.INSCDept;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.OFUser;

public interface INSBAgentSyncService {
	/**
	 * @param jobnum
	 * @return
	 */
	public INSBAgent getAgent(String jobnum);
	

	/**
	 * @param agent
	 */
	public void saveAgent(INSBAgent agent);
	
	/**
	 * @param agent
	 */
	public void updateAgent(INSBAgent agent);


	public INSBAgent getAgent(INSBAgent selectAgent, INSBAgent agent);


	public INSCDept selectByComcode(String agentgroup);


	public OFUser queryByUserName(String agentcode);


	public void updateByUserName(OFUser ofuserBean);


	public void insertOFUser(OFUser ofuser); 
	

}