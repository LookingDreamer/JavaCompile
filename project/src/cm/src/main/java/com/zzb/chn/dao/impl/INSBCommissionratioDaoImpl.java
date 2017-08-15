package com.zzb.chn.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.chn.dao.INSBCommissionratioDao;
import com.zzb.chn.entity.INSBCommissionratio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class INSBCommissionratioDaoImpl extends BaseDaoImpl<INSBCommissionratio> implements
		INSBCommissionratioDao {
	@Override
	public List<Map<Object, Object>> queryPagingList(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList("queryCommissionRatioList", map);
	}

	@Override
	public long queryPagingListCount(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne("queryCommissionRatioListCount", map);
	}

	@Override
	public List<INSBCommissionratio> queryCommissionRatioByChannel(String channelid) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("channelid",channelid);
		return this.sqlSessionTemplate.selectList("queryCommissionRatioByChannel", map);
	}

	@Override
	public INSBCommissionratio queryCommissionRatioById(String id) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectById"), id);
	}

	@Override
	public int insertCommissionRatio(INSBCommissionratio commissionRatio) {
		return this.sqlSessionTemplate.insert(this.getSqlName("insert"), commissionRatio);
	}

	@Override
	public int updateCommissionRatio(INSBCommissionratio commissionRatio) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateById"), commissionRatio);
	}

	@Override
	public int updateCommissionRatioStatus(INSBCommissionratio commissionRatio) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateStatusById"), commissionRatio);
	}

	@Override
	public int delCommissionRatio(String id) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteById"), id);
	}

	@Override
	public List<Map<String, String>> initChannelList() {
		return this.sqlSessionTemplate.selectList("initChannelList");
	}

	@Override
	public int updateTerminalTimeByNoti(INSBCommissionratio commissionRatio) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateTerminalTimeByNoti"), commissionRatio);
	}

	@Override
	public List<Map<Object, Object>> queryCommissionRatioChannel() {
		return this.sqlSessionTemplate.selectList("queryCommissionRatioChannel");
	}

	@Override
	public int cleanCommissionRatioStatus(String channelid) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("channelid",channelid);
		return this.sqlSessionTemplate.update(this.getSqlName("cleanCommissionRatioStatus"), map);
	}
}