package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBOrderdeliveryDao;
import com.zzb.conf.entity.INSBOrderdelivery;
import com.zzb.conf.entity.INSBRegion;

@Repository
public class INSBOrderdeliveryDaoImpl extends BaseDaoImpl<INSBOrderdelivery> implements
		INSBOrderdeliveryDao {

	@Override
	public INSBOrderdelivery getOrderdeliveryByTaskId(Map<String,Object> map) {
		
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByTaskId"), map);
	}

	/**
	 * liuchao 
	 */
	@Override
	public String getOrderdeliveryAddress(Map<String, String> params) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getOrderdeliveryAddress"), params);
	} 

	@Override
	public List<INSBRegion> getRegionByParentId(String parentid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getRegionByParentId"), parentid);
	}

	/**
	 * 通过taskid和orderid查询
	 */
	@Override
	public INSBOrderdelivery getOrderdelivery(Map<String, String> params) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getOrderdelivery"), params);
	} 
}