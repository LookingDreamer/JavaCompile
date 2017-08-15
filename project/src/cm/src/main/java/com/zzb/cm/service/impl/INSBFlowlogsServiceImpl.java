package com.zzb.cm.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.StringUtil;
import com.zzb.cm.dao.INSBFlowlogsDao;
import com.zzb.cm.entity.INSBFlowinfo;
import com.zzb.cm.entity.INSBFlowlogs;
import com.zzb.cm.service.INSBFlowlogsService;

@Service
public class INSBFlowlogsServiceImpl extends BaseServiceImpl<INSBFlowlogs> implements
		INSBFlowlogsService {
	@Resource
	private INSBFlowlogsDao insbFlowlogsDao;

	@Override
	protected BaseDao<INSBFlowlogs> getBaseDao() {
		return insbFlowlogsDao;
	}

	@Override
	public void logs(INSBFlowinfo insbFlowinfo) {
		INSBFlowlogs insbFlowlogs = new INSBFlowlogs();
		insbFlowlogs.setCreatetime(new Date());
		insbFlowlogs.setOperator("defualt");
		insbFlowlogs.setCurrenthandle(insbFlowinfo.getCurrenthandle());
		insbFlowlogs.setTaskid(insbFlowinfo.getTaskid());
		insbFlowlogs.setInscomcode(insbFlowinfo.getInscomcode());
		insbFlowlogs.setNoti("");
		insbFlowlogs.setOperator(StringUtil.isEmpty(insbFlowlogs.getOperator())?"admin":insbFlowlogs.getOperator());
		insbFlowlogs.setFlowcode(insbFlowinfo.getFlowcode());
		insbFlowlogs.setFlowname(insbFlowinfo.getFlowname());
		insbFlowlogs.setFromwhere("sys");
		insbFlowlogs.setCount(1);
		insbFlowlogs.setFiroredi(insbFlowinfo.getFiroredi());
		insbFlowlogs.setTaskstatus(insbFlowinfo.getTaskstatus());
		insbFlowlogsDao.insert(insbFlowlogs);
	}

}