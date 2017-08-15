package com.zzb.conf.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBOperatorcommentDao;
import com.zzb.conf.entity.INSBOperatorcomment;
import com.zzb.conf.service.INSBOperatorcommentService;

@Service
@Transactional
public class INSBOperatorcommentServiceImpl extends BaseServiceImpl<INSBOperatorcomment> implements
		INSBOperatorcommentService {
	@Resource
	private INSBOperatorcommentDao insbOperatorcommentDao;

	@Override
	protected BaseDao<INSBOperatorcomment> getBaseDao() {
		return insbOperatorcommentDao;
	}

	/**
	 * 根据流程轨迹id查询某节点的操作员备注列表
	 * trackid:主或子流程轨迹id；tracktype:主流程或子流程标记 0主流程，1子流程
	 */
	@Override
	public List<INSBOperatorcomment> selectOperatorCommentByTrackid(
			String trackid, Integer tracktype) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("trackid", trackid);
		params.put("tracktype", tracktype);
		return insbOperatorcommentDao.selectOperatorCommentByTrackid(params);
	}

	/**
	 * 添加操作人备注
	 */
	@Override
	public String addOperatorComment(INSBOperatorcomment operatorcomment) {
		//添加操作员备注
		Date date = new Date();
		operatorcomment.setCreatetime(date);
		insbOperatorcommentDao.insert(operatorcomment);
		return "success";
	}

	/**
	 * 通过流程实例id和保险公司code查询给操作员的备注列表
	 * 入参     instanceid： 主流程id ; inscomcode 保险公司code
	 */
	@Override
	public List<INSBOperatorcomment> getOperatorCommentByMaininstanceid(
			String maininstanceid, String inscomcode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("instanceid", maininstanceid);
		params.put("inscomcode", inscomcode);
		return insbOperatorcommentDao.getOperatorCommentByMaininstanceid(params);
	}

	@Override
	public List<String> getOperCommentByMaininstanceid(String maininstanceid, String inscomcode) {
		List<String> notiList = new ArrayList<String>();
		List<INSBOperatorcomment> operatorcommentList = getOperatorCommentByMaininstanceid(maininstanceid, inscomcode);
		if(operatorcommentList!=null && operatorcommentList.size()>0){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
			//将所有的备注信息拼接成一个字符串(包括修改时间和操作人),并且在一个备注完后换行
			for(INSBOperatorcomment comment:operatorcommentList){
				String noti = "";
				//判断给操作人的备注是否为空和空格
				if(comment.getCommentcontent()!=null && !"".equals(comment.getCommentcontent().trim())){
					//判断有无修改时间
					if(comment.getModifytime()!=null){
						noti = comment.getCommentcontent()+"-"+comment.getOperator()+"-"+sdf.format(comment.getModifytime());
					}else{
						noti = comment.getCommentcontent()+"-"+comment.getOperator()+"-"+sdf.format(comment.getCreatetime());	
					}
				}
				notiList.add(noti);
			}
		}
		return notiList;
	}
}