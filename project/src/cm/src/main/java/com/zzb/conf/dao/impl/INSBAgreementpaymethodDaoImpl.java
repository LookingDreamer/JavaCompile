package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.controller.vo.DeptPayTypeVo;
import com.zzb.conf.dao.INSBAgreementpaymethodDao;
import com.zzb.conf.entity.INSBAgreementpaymethod;

@Repository
public class INSBAgreementpaymethodDaoImpl extends BaseDaoImpl<INSBAgreementpaymethod> implements
		INSBAgreementpaymethodDao {

	@Override
	public int delByAgreeid(String agreeid) {
		return this.sqlSessionTemplate.delete(this.getSqlName("delByAgreeid"), agreeid);
	}

	@Override
	public List<Map<String, Object>> getByChannelinnercodeAndPrvid(Map<String, Object> map) { 
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByChannelinnercodeAndPrvid"), map);
	}

	@Override
	public List<DeptPayTypeVo> getDeptPayType(Map<String, String> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getDeptPayType"), map);
	}

}