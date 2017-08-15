package com.zzb.extra.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.extra.entity.INSBAgentDeliveryaddress;
import com.zzb.extra.dao.INSBAgentDeliveryaddressDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class INSBAgentDeliveryaddressDaoImpl extends BaseDaoImpl<INSBAgentDeliveryaddress> implements
		INSBAgentDeliveryaddressDao {

	@Override
	public List<Map<Object, Object>> selectAgentDeliveryaddressListPaging(Map<String, Object> data) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"),data);
	}
	@Override
	public List<Map<Object, Object>> queryAgentDeliveryAddressList(String agentid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByAgentid"),agentid);
	}

	/** 按代理人id修改是否 默认配送地址 */
	public int updateIsdefaultByAgentid(String agentid,String isdefault){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("agentid",agentid);
		map.put("isdefault", isdefault);
		return this.sqlSessionTemplate.update(this.getSqlName("updateAddressIsdefaultByAgentid"),map);
	}

	/** 按配送地址id修改是否 默认配送地址 */
	public int updateIsdefaultById(String id, String isdefault){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("id",id);
		map.put("isdefault",isdefault);
		return this.sqlSessionTemplate.update(this.getSqlName("updateAddressIsdefaultByAddressid"),map);
	}

	@Override
	public int addAgentDeliveryaddress(INSBAgentDeliveryaddress agentDeliveryaddress) {
		String id = UUIDUtils.random();
		agentDeliveryaddress.setId(id);
		return this.sqlSessionTemplate.insert(this.getSqlName("insert"), agentDeliveryaddress);
	}
	@Override
	public long selectCountDeliveryaddress(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne("INSBAgentDeliveryaddress_selectCount",map);
	}
}