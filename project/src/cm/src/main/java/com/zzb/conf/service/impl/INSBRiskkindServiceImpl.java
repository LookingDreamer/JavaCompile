package com.zzb.conf.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBRiskDao;
import com.zzb.conf.dao.INSBRiskkindDao;
import com.zzb.conf.entity.INSBRiskkind;
import com.zzb.conf.service.INSBRiskkindService;

@Service
@Transactional
public class INSBRiskkindServiceImpl extends BaseServiceImpl<INSBRiskkind> implements
		INSBRiskkindService {
	@Resource
	private INSBRiskkindDao insbRiskkindDao;
	@Resource
	private INSBRiskDao insbRiskDao;

	@Override
	protected BaseDao<INSBRiskkind> getBaseDao() {
		return insbRiskkindDao;
	}
	
	@Override
	public Long queryListByVoCount(INSBRiskkind riskkind) {
		// TODO Auto-generated method stub
		return this.insbRiskkindDao.queryListByVoCount(riskkind);
	}

	@Override
	public List<INSBRiskkind> queryListByVo(INSBRiskkind riskkind) {
		return this.insbRiskkindDao.queryListByVo(riskkind);
	}
	@Override
	public List<INSBRiskkind> selectByModifyDate(String modifydate) {
		return insbRiskkindDao.selectByModifyDate(modifydate);
	}

	@Override
	public int selectCountByRiskid(String riskid) {
		return this.insbRiskkindDao.selectCountByRiskid(riskid);
	}

	@Override
	public int selectcountByKindcode(String id,String kindcode, String riskid) {
		int num = 0;
		Map<String, String> param = new HashMap<String, String>();
		param.put("kindcode", kindcode);
		param.put("riskid", riskid);
		// 修改
		if (id != null && !id.equals("")) {
			param.put("id", id);
		}
		num = insbRiskkindDao.selectCountByKindcode(param);
		return num;
	}

	@Override
	public List<INSBRiskkind> queryAll() {
		return insbRiskkindDao.selectAll();
	}
}