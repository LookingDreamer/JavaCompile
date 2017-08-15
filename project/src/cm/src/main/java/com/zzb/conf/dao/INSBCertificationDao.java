package com.zzb.conf.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.controller.vo.CertificationVo;
import com.zzb.conf.entity.INSBCertification;

import java.util.List;
import java.util.Map;

public interface INSBCertificationDao extends BaseDao<INSBCertification> {

	/**
	 * 获取认证任务信息
	 * @param certificationVo
	 * @return
	 */
	public CertificationVo getCertificationInfo(CertificationVo certificationVo);

	public CertificationVo getOneCertificationInfo(CertificationVo certificationVo);

	public int updateDesignatedoperator(INSBCertification certificationVo);

	/**
	 * 分页认证任务列表
	 * @param map
	 * @return
     */
	List<Map<String, Object>> getCertificationPage(Map<String, Object> map);


	int getCertificationCount(Map<String, Object> map);

}