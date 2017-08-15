package com.zzb.conf.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.controller.vo.CertificationVo;
import com.zzb.conf.dao.INSBCertificationDao;
import com.zzb.conf.entity.INSBCertification;

import java.util.List;
import java.util.Map;

@Repository
public class INSBCertificationDaoImpl extends BaseDaoImpl<INSBCertification> implements
	INSBCertificationDao {

	@Override
	public CertificationVo getCertificationInfo(CertificationVo certificationVo) {
		CertificationVo temp = this.sqlSessionTemplate.selectOne(this.getSqlName("getCertificationInfo"), certificationVo);
		if("1".equals(temp.getJobnumtype())){
			temp.setFormalnum(null);
		}else{
			temp.setFormalnum(temp.getJobnum());
		}
		return temp;
	}

	@Override
	public CertificationVo getOneCertificationInfo(CertificationVo certificationVo) {
		CertificationVo temp = this.sqlSessionTemplate.selectOne(this.getSqlName("getOneCertificationInfo"), certificationVo);
		if(temp == null){
			return temp;//无认证信息
		}
		if("1".equals(temp.getJobnumtype())){
			temp.setFormalnum(null);
		}else{
			temp.setFormalnum(temp.getJobnum());
		}
		return temp;
	}

	@Override
	public int updateDesignatedoperator(INSBCertification certificationVo) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateByCertificationID"), certificationVo);
	}

	/**
	 * 分页认证任务列表
	 *
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getCertificationPage(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getCertificationPage"), map);
	}


	@Override
	public int getCertificationCount(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getCertificationCount"), map);
	}


}