package com.zzb.conf.service;

import java.util.List;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBOperatorcomment;

public interface INSBOperatorcommentService extends BaseService<INSBOperatorcomment> {

	/**
	 * 根据流程轨迹id查询某节点的操作员备注列表
	 * trackid:主或子流程轨迹id；tracktype:主流程或子流程标记 1主流程，2子流程
	 */
	public List<INSBOperatorcomment> selectOperatorCommentByTrackid(String trackid, Integer tracktype);

	/**
	 * 添加操作人备注
	 */
	public String addOperatorComment(INSBOperatorcomment operatorcomment);
	
	/**
	 * 通过流程实例id和保险公司code查询给操作员的备注列表
	 * 入参     instanceid： 主流程id ; inscomcode 保险公司code
	 */
	public List<INSBOperatorcomment> getOperatorCommentByMaininstanceid(String maininstanceid, String inscomcode);
	
	/**
	 * 通过主流程id和保险公司id来查询备注信息,并将备注信息处理为String类型返回
	 * 入参 maininstanceid主流程id;inscomcode 保险公司code
	 */
	public List<String> getOperCommentByMaininstanceid(String maininstanceid, String inscomcode);
}