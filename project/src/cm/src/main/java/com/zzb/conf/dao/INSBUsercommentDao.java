package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBUsercomment;

public interface INSBUsercommentDao extends BaseDao<INSBUsercomment> {

	/**
	 * 根据流程轨迹id查询某节点的用户备注信息
	 * 入参     trackid： 任务轨迹id ; tracktype 轨迹类型： 0主流程 1报价子流程
	 */
	public INSBUsercomment selectUserCommentByTrackid(Map<String, Object> params);
	/**
	 * 查询当前节点之前的用户备注信息
	 * 入参     instanceid： 主流程实例id ; inscomcode 保险公司code ; dqtaskcode 当前节点code
	 */
	public List<INSBUsercomment> getNearestUserComment(Map<String, Object> params);
	//wy
	public List<INSBUsercomment> getNearestUserComment2(Map<String, Object> params);
	/**
	 * 查询当前节点之前的用户备注信息,前端显示后台业管添加的所有备注，剔除前端代理人提交的备注
	 */
	public List<Map<String, Object>> getNearestUserComment3(Map<String, Object> params);

	/**
	 * 查询当前节点之前的用户备注信息,前端显示用户提交的备注信息
	 */
	public List<INSBUsercomment> getNearestUserComment4(Map<String, Object> params);
	/**
	 * 查询所有给用户的备注信息
	 */
	public List<INSBUsercomment> getNearestUserComment5(Map<String, Object> params);
	public List<INSBUsercomment> getNearestInsureBack(Map<String, Object> params);
	public List<INSBUsercomment> getAllUserComment(Map<String, Object> params);

	/**
	 * 获取主流程提交的comment
	 * @param params instanceid
	 * @return
     */
	List<INSBUsercomment> getMainComment(Map<String, Object> params);
}