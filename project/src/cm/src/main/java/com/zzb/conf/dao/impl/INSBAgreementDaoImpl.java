package com.zzb.conf.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cninsure.core.exception.DaoException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.BeanUtils;
import com.zzb.conf.dao.INSBAgreementDao;
import com.zzb.conf.entity.INSBAgreement;

@Repository
public class INSBAgreementDaoImpl extends BaseDaoImpl<INSBAgreement> implements
		INSBAgreementDao {

	@Override
	public Long queryCountVo(INSBAgreement agreement) {
		Map<String, Object> params = BeanUtils.toMap(agreement);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCountVo"),params);
	
	}

	@Override
	public List<INSBAgreement> queryListVo(INSBAgreement agreement) {
		Map<String, Object> params = BeanUtils.toMap(agreement);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectVo"),params);
	
	}

	@Override
	public List<String> selectProviderIdByDeptId(String deptid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectProviderByDeptId"), deptid);
	}

	@Override
	public List<String> selectAgreementIdByproviderId(String providerId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAgreementIdByPro"), providerId);
	}
	
	@Override
	public List<String> selectAgreementIdByDeptId(String deptid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAgreementIdByDeptId"), deptid);
	}

	@Override
	public String selectProviderIdByAgreementId(String id) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectProviderIdsByAgreementId"),id);
	}

	@Override
	public List<String> selectAgreementtruleListByProviderid(String providerid,
			String deptid) {
		if(StringUtils.isNoneBlank(providerid,deptid)){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("providerid", providerid);
			map.put("deptid", deptid);
			return this.sqlSessionTemplate.selectList(this.getSqlName("selectAgreementtruleListByProviderid"),map);
		}
		return null;
	}

	@Override
	public List<INSBAgreement> queryAgreement(Map<String, Object> map) throws DaoException{
		if ("001".equals(map.get("agreementcode"))) {
			return this.sqlSessionTemplate.selectList(this.getSqlName("querymeAgreement001"),map);
		} else if ("010".equals(map.get("agreementcode"))) {
			return this.sqlSessionTemplate.selectList(this.getSqlName("querymeAgreement010"),map);
		}else if ("100".equals(map.get("agreementcode"))) {
			return this.sqlSessionTemplate.selectList(this.getSqlName("querymeAgreement100"),map);
		}else if ("011".equals(map.get("agreementcode"))) {
			return this.sqlSessionTemplate.selectList(this.getSqlName("querymeAgreement011"),map);
		}else if ("101".equals(map.get("agreementcode"))) {
			return this.sqlSessionTemplate.selectList(this.getSqlName("querymeAgreement101"),map);
		}else if ("110".equals(map.get("agreementcode"))) {
			return this.sqlSessionTemplate.selectList(this.getSqlName("querymeAgreement110"),map);
		}else {
			return this.sqlSessionTemplate.selectList(this.getSqlName("querymeAgreement111"),map);
		}
	}

	@Override
	public List<INSBAgreement> selectAllSubAgreement(List<String> deptids) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAllSubAgreement"), deptids);
	}

	@Override
	public List<String> getAgreementProvider(String comcode) {
		Map<String,String> parm=new HashMap<String, String>();
		parm.put("comcode", comcode);
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryProvider"),parm);
	}

	/**
	 * 根据出单网点获取协议
	 *
	 * @param map {deptid5}
	 * @return
	 */
	@Override
	public List<INSBAgreement> selectByOutorderdept(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByOutorderdept"),map);
	}

	@Override
	public List<INSBAgreement> queryAgreementByComecode(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryAgreementByComecode"),map);
	}


}