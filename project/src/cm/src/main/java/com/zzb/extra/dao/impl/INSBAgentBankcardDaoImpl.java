package com.zzb.extra.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.extra.dao.INSBAgentBankcardDao;
import com.zzb.extra.entity.INSBAgentBankcard;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class INSBAgentBankcardDaoImpl extends BaseDaoImpl<INSBAgentBankcard> implements INSBAgentBankcardDao {

	@Override
	public List<Map<Object, Object>> selectAgentBankCardListPaging(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryByBankCardId"),map);
	}
	@Override
	public List<Map<Object, Object>> selectAgentBankCardByCardagentid(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryAgentBankCardByCardagentid"),map);
	}

	@Override
	public int addAgentBankCard(INSBAgentBankcard agentbankcard) {
		String id = UUIDUtils.random();
		agentbankcard.setId(id);
		return this.sqlSessionTemplate.insert(this.getSqlName("insert"),agentbankcard);
	}

	@Override
	public int setDefaultCardByCardagentid(String cardagentid,String isdefault){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("cardagentid",cardagentid);
		map.put("isdefault", isdefault);
		return this.sqlSessionTemplate.update(this.getSqlName("updateAgentBankcardIsdefaultByCardagentid"),map);
	}

	@Override
	public int setDefaultCardByCardId(String id,String isdefault){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("id",id);
		map.put("isdefault", isdefault);
		return this.sqlSessionTemplate.update(this.getSqlName("updateAgentBankcardIsdefaultByCardId"),map);
	}

	@Override
	public List<Map<String, String>> selectAgentBanknamelist() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectBanknamelist"));
	}

	@Override
	public List<Map<Object, Object>> queryByAgentBanktoname(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryByBanktoname"),map);
	}
	@Override
	public String selectNameByCardagentId(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne("selectNameByCardagentId", map);
	}

}