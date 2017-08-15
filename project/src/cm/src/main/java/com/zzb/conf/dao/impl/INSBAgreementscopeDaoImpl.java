package com.zzb.conf.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.conf.dao.INSBAgreementscopeDao;
import com.zzb.conf.entity.INSBAgreementscope;

@Repository
public class INSBAgreementscopeDaoImpl extends BaseDaoImpl<INSBAgreementscope> implements
		INSBAgreementscopeDao {

	@Override
	public List<INSBAgreementscope> selectAdrAndDeptName(
			INSBAgreementscope query) {
		Map<String, Object> params = BeanUtils.toMap(query);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAdrAndDeptName"),params);
	}

	@Override
	public List<String> selectAreaDeptid(String city) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectArea"),city);
	}

	@Override
	public List<String> selectDeptIdByAgreementId(String agreementid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectDeptIdByAgreementId"),  agreementid);
	}

	@Override
	public List<String> selectAgreementIdByDeptId(String deptid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAgreementIdByDeptId"), deptid);
	}

	@Override
	public List<INSBAgreementscope> insertscope(Map<String, Object> map) {
		List<INSBAgreementscope> lis=this.sqlSessionTemplate.selectList(this.getSqlName("selectByCity"), map);
		for (INSBAgreementscope insbAgreementscope : lis) {
			insbAgreementscope.setId(UUIDUtils.random());
			insbAgreementscope.setAgreementid((String)map.get("agreementid"));
			insbAgreementscope.setCreatetime(new Date());
			insbAgreementscope.setModifytime(new Date());
			insbAgreementscope.setOperator((String)map.get("operator"));
			insbAgreementscope.setScopetype("2");
		}
		map.put("deptid", lis);
//		this.sqlSessionTemplate.selectList(this.getSqlName("selectByCityInsert"), map);
		return lis;
	}

	@Override
	public void insertscopes(Map<String, Object> map) {
		List<String> listciytid=(List<String>) map.get("cityids");
		for (String string : listciytid) {
			map.put("city", string);
			this.sqlSessionTemplate.delete("com.zzb.conf.entity.INSBAgreementscope_deletByCity", map);
			this.sqlSessionTemplate.selectList(this.getSqlName("selectByCityInsert"), map);
			map.remove("city");
		}
	}

	@Override
	public void deletebyagreementid(String agreementid,String city) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("agreementid", agreementid);
		map.put("city", city);
		this.sqlSessionTemplate.delete("com.zzb.conf.entity.INSBAgreementscope_deletByCity", map);
	}


}