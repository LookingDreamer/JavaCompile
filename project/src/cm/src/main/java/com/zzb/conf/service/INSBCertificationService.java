package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.controller.vo.CertificationVo;
import com.zzb.conf.entity.INSBCertification;

public interface INSBCertificationService extends BaseService<INSBCertification> {

	/**
	 * 获取认证任务信息
	 * @param certificationVo
	 * @return
	 */
	public CertificationVo getCertificationInfo(CertificationVo certificationVo);

	/**
	 * 获取代理人一个认证任务信息
	 * @param certificationVo
	 * @return
	 */
	public CertificationVo getOneCertificationInfo(CertificationVo certificationVo);

	/**
	 * 更新认证任务信息
	 * @param certificationVo
	 * @return
	 */
	public int updateCertificationInfo(CertificationVo certificationVo, String operator);

	/**
	 * 获取正式工号
	 * @param certificationVo
	 * @param operator
	 * @return
	 */
	public Map<String, String> getFormalNum(CertificationVo certificationVo, String operator);
	
	/**
	 *更新 INSBCertification中designatedoperator字段
	 */
	public int updateDesignatedoperator(INSBCertification certification);

	/**
	 * 分页认证任务列表
	 * @param map
	 * @return
	 */
	Map<String, Object> getCertificationPage(Map<String, Object> map);
}
