package com.zzb.cm.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBCarkindpriceDao;
import com.zzb.cm.entity.INSBCarkindprice;

@Repository
public class INSBCarkindpriceDaoImpl extends BaseDaoImpl<INSBCarkindprice> implements INSBCarkindpriceDao {

	@Override
	public List<INSBCarkindprice> selectCarkindpriceList(Map<String, Object> map) {

		return this.sqlSessionTemplate.selectList(this.getSqlName("select"), map);
	}

	@Override
	public List<INSBCarkindprice> selectOrderRiskkind(Map<String, Object> map) {

		return this.sqlSessionTemplate.selectList(this.getSqlName("selectOrderRiskkind"), map);
	}


	@Override
	public INSBCarkindprice selectINSBCarkindprice(Map<String, Object> map) {

		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCarKindPrice"), map);
	}

	/*
	 * (non-Javadoc) 投保书页面商业计划数据
	 */
	@Override
	public List<INSBCarkindprice> businussKind(String taskId) {

		return this.sqlSessionTemplate.selectList(this.getSqlName("selectRiskKindMap"), taskId);
	}

	/**
	 * 手机端状态对应
	 */
	@Override
	public List<Map<String, String>> selectStatus(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectComcode"), map);
	}

	@Override
	public List<Map<String, String>> selectAllriskkind(String inscomcode) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAllriskkind"), inscomcode);
	}

	@Override
	public List<INSBCarkindprice> selectByTaskidAndInscomcode(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByTaskidAndInscomcode"), map);
	}

	@Override
	public double getTotalProductAmount(Map<String, Object> map) {
		double result = 0L;
		if (this.sqlSessionTemplate.selectOne(this.getSqlName("getTotalProductAmount"), map) != null) {
			result = this.sqlSessionTemplate.selectOne(this.getSqlName("getTotalProductAmount"), map);
		}
		return result;
	}

	/**
	 * 查询交强险险别记录条数liuchao
	 * 
	 * @param mInstanceid
	 *            inscomcode
	 */
	@Override
	public int getStrCount(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getStrCount"), params);
	}

	/**
	 * 查询商业险险别记录条数liuchao
	 * 
	 * @param mInstanceid
	 *            inscomcode
	 */
	@Override
	public int getBusCount(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getBusCount"), params);
	}

	/**
	 * 根据主任务号和供应商删除商业险信息
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public int deleteBizByTaskid(Map<String, Object> params) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteBizByTaskid"), params);
	}

	/**
	 * 根据主任务号和供应商删除交强险和车船税信息
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public int deleteEfcByTaskid(Map<String, Object> params) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteEfcByTaskid"), params);
	}

	@Override
	public int deleteByObj(INSBCarkindprice insbCarkindprice) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteByObj"), insbCarkindprice);
	}
}