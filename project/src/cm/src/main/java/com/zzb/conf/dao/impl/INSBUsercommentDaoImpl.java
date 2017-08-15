package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBUsercommentDao;
import com.zzb.conf.entity.INSBUsercomment;

@Repository
public class INSBUsercommentDaoImpl extends BaseDaoImpl<INSBUsercomment> implements
		INSBUsercommentDao {

	/**
	 * 根据流程轨迹id查询某节点的用户备注信息
	 * 入参     trackid： 任务轨迹id ; tracktype 轨迹类型： 0主流程 1报价子流程
	 */
	@Override
	public INSBUsercomment selectUserCommentByTrackid(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectUserCommentByTrackid"), params);
	}

	/**
	 * 查询当前节点之前的用户备注信息
	 * 入参     instanceid： 主流程实例id ; inscomcode 保险公司code ; dqtaskcode 当前节点code
	 */
	@Override
	public List<INSBUsercomment> getNearestUserComment(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getNearestUserComment"), params);
	}

	@Override
	public List<INSBUsercomment> getNearestUserComment2(
			Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getNearestUserComment2"), params);
	}
	@Override
	public List<Map<String, Object>> getNearestUserComment3(
			Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getNearestUserComment3"), params);
	}
	@Override
	public List<INSBUsercomment> getNearestUserComment4(
			Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getNearestUserComment4"), params);
	}
	@Override
	public List<INSBUsercomment> getNearestUserComment5(
			Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getNearestUserComment5"), params);
	}
	@Override
	public List<INSBUsercomment> getNearestInsureBack(
			Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getNearestInsureBack"), params);
	}
	
	@Override
	public List<INSBUsercomment> getAllUserComment(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getAllUserComment"), params);
	}

	/**
	 * 获取主流程提交的comment
	 *
	 * @param params instanceid
	 * @return
	 */
	@Override
	public List<INSBUsercomment> getMainComment(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getMainComment"), params);
	}
}