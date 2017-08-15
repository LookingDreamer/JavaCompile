package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBOperatorcommentDao;
import com.zzb.conf.entity.INSBOperatorcomment;

@Repository
public class INSBOperatorcommentDaoImpl extends BaseDaoImpl<INSBOperatorcomment> implements
		INSBOperatorcommentDao {

	/**
	 * 根据流程轨迹id查询某节点的操作员备注列表
	 * 入参     trackid： 任务轨迹id ; tracktype 轨迹类型： 0主流程 1报价子流程
	 */
	@Override
	public List<INSBOperatorcomment> selectOperatorCommentByTrackid(
			Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectOperatorCommentByTrackid"),params);
	}

	/**
	 * 通过流程实例id和保险公司code查询给操作员的备注列表
	 * 入参     instanceid： 主流程id ; inscomcode 保险公司code
	 */
	@Override
	public List<INSBOperatorcomment> getOperatorCommentByMaininstanceid(
			Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getOperatorCommentByMaininstanceid"),params);
	}

}