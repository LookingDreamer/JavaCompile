package com.zzb.conf.manager.scr;

import java.util.List;

import com.zzb.conf.entity.INSBBwagent;

public interface INSBBwagentManager {

	List<INSBBwagent> getBwagentData(String maxSyncdateStr, String agentcode, String orgCode);
}