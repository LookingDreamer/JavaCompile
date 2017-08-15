package com.zzb.extra.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBAgentBankcard;

import java.util.List;
import java.util.Map;

public interface INSBAgentBankcardDao extends BaseDao<INSBAgentBankcard> {
	public List<Map<Object, Object>> selectAgentBankCardListPaging(Map<String, Object> data);
	public List<Map<Object, Object>> selectAgentBankCardByCardagentid(Map<String, Object> data);

	public int addAgentBankCard(INSBAgentBankcard agentbankcard);

	public int setDefaultCardByCardagentid(String cardagentid, String isdefault);
	public int setDefaultCardByCardId(String id, String isdefault);

	public List<Map<String, String>> selectAgentBanknamelist();

	public List<Map<Object, Object>> queryByAgentBanktoname(Map<String, Object> map);
	
	public String selectNameByCardagentId(Map<String, Object> map);
	
}