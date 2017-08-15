package com.zzb.conf.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBSkillDao;
import com.zzb.conf.entity.INSBSkill;
import com.zzb.conf.service.INSBSkillService;

@Service
@Transactional
public class INSBSkillServiceImpl extends BaseServiceImpl<INSBSkill> implements
		INSBSkillService {
	@Resource
	private INSBSkillDao insbSkillDao;

	@Override
	protected BaseDao<INSBSkill> getBaseDao() {
		return insbSkillDao;
	}

	@Override
	public List<INSBSkill> queryListByelfId(String id, String type) {
		return insbSkillDao.queryListByelfId(id,type);
	}

	@Override
	public int deletebyelfid(String elfid) {
		return insbSkillDao.deletebyelfid(elfid);
	}

	/** 
	 * 获取项目中现有的输入输出项
	 * @see com.zzb.conf.service.INSBSkillService#filter(java.lang.String)
	 */
	@Override
	public String filter(String id ,String type) {
		return insbSkillDao.filter(id,type);
	}

	@Override
	public String querySkillnameByelfid(String elfid) {
		return insbSkillDao.querySkillnameByelfid(elfid);
	}

}