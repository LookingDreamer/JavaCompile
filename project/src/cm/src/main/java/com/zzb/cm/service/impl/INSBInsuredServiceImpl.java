package com.zzb.cm.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBInsuredDao;
import com.zzb.cm.entity.INSBInsured;
import com.zzb.cm.service.INSBInsuredService;

@Service
@Transactional
public class INSBInsuredServiceImpl extends BaseServiceImpl<INSBInsured> implements
		INSBInsuredService {
	@Resource
	private INSBInsuredDao insbInsuredDao;

	@Override
	protected BaseDao<INSBInsured> getBaseDao() {
		return insbInsuredDao;
	}

	@Override
	public Map<String, Object> getInsuredInfo(String taskid) {
		
		INSBInsured insured = insbInsuredDao.selectInsuredByTaskId(taskid);
		Map<String, Object> tempMap = new HashMap<String, Object>();
		if (insured != null) {
			//商业险投保单号
			tempMap.put("businessproposalformno", insured.getBusinessproposalformno());
			//商业险保单号
			tempMap.put("businesspolicyno", insured.getBusinesspolicyno());
		}
		return tempMap;
	}
	

}