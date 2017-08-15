package com.zzb.cm.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.BeanUtils;
import com.zzb.cm.dao.INSBUserremarkDao;
import com.zzb.cm.dao.INSBUserremarkhisDao;
import com.zzb.cm.entity.INSBUserremark;
import com.zzb.cm.entity.INSBUserremarkhis;
import com.zzb.cm.service.INSBUserremarkService;

@Service
@Transactional
public class INSBUserremarkServiceImpl extends BaseServiceImpl<INSBUserremark> implements
		INSBUserremarkService {
	@Resource
	private INSBUserremarkDao insbUserremarkDao;
	@Resource
	private INSBUserremarkhisDao insbUserremarkhisDao;

	@Override
	protected BaseDao<INSBUserremark> getBaseDao() {
		return insbUserremarkDao;
	}

	@Override
	public INSBUserremark getRemarkByTaskId(String taskid) {
		return insbUserremarkDao.selectByTaskId(taskid);
	}

	@Override
	public Map<String, Object> getRemarkByTaskId_Comcode(String taskid,
			String comcode) {
		INSBUserremarkhis remarkhis = new INSBUserremarkhis();
		INSBUserremark remark = new INSBUserremark();
		Map<String, Object> map = new HashMap<String, Object>();
		remarkhis.setTaskid(taskid);
		remarkhis.setInscomcode(comcode);
		remarkhis = insbUserremarkhisDao.selectOne(remarkhis);
		if(remarkhis!=null&&(remarkhis.getId()!=null||!remarkhis.getId().equals(""))){
			map = BeanUtils.toMap(remarkhis);
		}else{
			remark = insbUserremarkDao.selectByTaskId(taskid);
			map = BeanUtils.toMap(remark);
		}
		return map;
	}

}