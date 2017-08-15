package com.zzb.extra.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBAgentBankcard;

import java.util.List;
import java.util.Map;

public interface INSBAgentBankcardService extends BaseService<INSBAgentBankcard> {
	
	public Map<String, Object> initAgentBankCardList(Map<String, Object> map);

	public List<Map<Object, Object>> agentBankcardquerybyagentid(Map<String, Object> map);

	public int addAgentBankCard(INSBAgentBankcard agentBankcard);
	public void setDefaultCard(String id, String cardagentid);

	public List<Map<String, String>> selectAgentBanknamelist();

	public Map<String, Object> queryByAgentBanktoname(Map<String, Object> map);
	
}