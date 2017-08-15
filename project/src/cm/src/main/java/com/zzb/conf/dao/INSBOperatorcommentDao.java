package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBOperatorcomment;

public interface INSBOperatorcommentDao extends BaseDao<INSBOperatorcomment> {
	
	/**
	 * 根据流程轨迹id查询某节点的操作员备注列表
	 * 入参     trackid： 任务轨迹id ; tracktype 轨迹类型： 0主流程 1报价子流程
	 */
	public List<INSBOperatorcomment> selectOperatorCommentByTrackid(Map<String, Object> params);
	
	/**
	 * 通过流程实例id和保险公司code查询给操作员的备注列表
	 * 入参     instanceid： 主流程id ; inscomcode 保险公司code
	 */
	public List<INSBOperatorcomment> getOperatorCommentByMaininstanceid(Map<String, Object> params);
}