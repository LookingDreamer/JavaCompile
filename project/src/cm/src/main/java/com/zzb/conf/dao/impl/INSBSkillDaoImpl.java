package com.zzb.conf.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBSkillDao;
import com.zzb.conf.entity.INSBSkill;

@Repository
public class INSBSkillDaoImpl extends BaseDaoImpl<INSBSkill> implements
		INSBSkillDao {

	@Override
	public List<INSBSkill> queryListByelfId(String id, String type) {
		if(type.equals("in")){
			return this.sqlSessionTemplate.selectList(this.getSqlName("inputlist"),id);
		}else{
			return this.sqlSessionTemplate.selectList(this.getSqlName("outputlist"),id);
		}
	}

	@Override
	public int deletebyelfid(String elfid) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deletebyelfid"),elfid);
	}

	/** 
	 * 获取现有的输入输出项
	 * @see com.zzb.conf.dao.INSBSkillDao#filter(java.lang.String, java.lang.String)
	 */
	@Override
	public String filter(String id, String type) {
		if(type.equals("in")){
			return this.sqlSessionTemplate.selectOne(this.getSqlName("filterin"),id);
		}else{
			return this.sqlSessionTemplate.selectOne(this.getSqlName("filterout"),id);
		}

	}

	@Override
	public String querySkillnameByelfid(String elfid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("querySkillnameByelfid"),elfid);
	}

}