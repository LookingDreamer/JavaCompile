package com.zzb.conf.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCCode;
import com.zzb.conf.entity.INSBUsercomment;

public interface INSBUsercommentService extends BaseService<INSBUsercomment> {

	/**
	 * 根据流程轨迹id查询某节点的用户备注信息
	 * trackid:主或子流程轨迹id；tracktype:主流程或子流程标记 1主流程，2子流程
	 */
	public INSBUsercomment selectUserCommentByTrackid(String trackid, Integer tracktype);
	
	/**
	 * 修改用户备注
	 */
	public String editUserComment(INSBUsercomment usercomment);

	/**
	 * 通过选定的备注类型查询对应的备注内容类型
	 */
	public List<INSCCode> getCommentcontenttypeListByCommenttype(String commenttypeCodeValue);
	/**
	 * 查询当前节点之前的用户备注信息
	 * 入参     instanceid： 主流程实例id ; inscomcode 保险公司code ; dqtaskcode 当前节点code
	 */
	public List<INSBUsercomment> getNearestUserComment(String instanceid, String inscomcode, String dqtaskcode);
	//wangyang
	public List<INSBUsercomment> getNearestUserComment2(String instanceid, String inscomcode, String dqtaskcode);
	/**
	 * 查询当前节点之前的用户备注信息,前端显示后台业管添加的所有备注，剔除前端代理人提交的备注
	 * @param instanceid 主流程实例id
	 * @param inscomcode 保险公司code
	 * @param dqtaskcode 当前节点code
	 * @return
	 */
	public List<Map<String, Object>> getNearestUserComment3(String instanceid, String inscomcode, String dqtaskcode, Integer commentsource);
	/**
	 * 查询当前节点之前的用户备注信息,前端显示用户提交的备注信息
	 * @param instanceid 主流程实例id
	 * @param inscomcode 保险公司code
	 * @return
	 */
	public List<INSBUsercomment> getNearestUserComment4(String instanceid, String inscomcode);
	public List<INSBUsercomment> getNearestInsureBack(String instanceid, String inscomcode);
	/**
	 * 查询所有备注信息
	 * 入参 instanceid:主流程实例id;inscomcode 保险公司code
	 */
	public List<INSBUsercomment> getAllUserComment(String instanceid, String inscomcode);
	/**
	 * 查询所有备注信息,并将备注转为String类型返回
	 * 入参 instanceid:主流程实例id;inscomcode 保险公司code
	 */
	public List<String> getUserComment(String instanceid, String inscomcode);

	/**
	 * 查询用户备注信息和备注类型
	 *
	 * @param instanceid 主流程实例id
	 * @param inscomcode 保险公司code
	 * @return
	 */
	public List<Map<String, Object>> getNearestUserCommentAndType(String instanceid, String inscomcode);

	/**
	 * 根据流程轨迹id查询某节点的用户备注信息和备注类型
	 *
	 * @param instanceid   主流程实例id
	 * @param inscomcode 保险公司code
	 * @return
	 */
	public List<Map<String, Object>> getUserCommentAndType(String instanceid, String inscomcode);

	/**
	 * 根据用户备注信息表的备注类型去字典表查找对应的中文名称
	 *
	 * @param codeValue 备注类型的值
	 * @param codeType 代码类型
	 * @param ParentCore 父级类型
	 * @return
	 */
	public String getCommentNameByCodeValue(String codeValue, String codeType, String ParentCore);
}