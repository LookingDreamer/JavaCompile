package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.controller.vo.DeptPayTypeVo;
import com.zzb.conf.entity.INSBPaychannelmanager;

public interface INSBPaychannelmanagerService extends BaseService<INSBPaychannelmanager> {

	public Long deleteByExceptIds(List<String> ids,String agreementid);
	/**
	 * 
	 * 通过逻辑主键删除
	 * @param param
	 */
	public void delteDataBylogicId(Map<String,String> param);
	 
	public List<DeptPayTypeVo> getDeptPayType(String providerid, String deptid, String agreementid);
	
	public List<INSBPaychannelmanager> queryListByParam(Map<String, Object> param);
	public Long queryListByParamSize(Map<String, Object> param);
	/**
	 * 渠道协议配置
	 * 保存支付方式
	 * @param deptPayTypeVo
	 * @param flag
	 * @return
	 */
	public String savePayType(List<DeptPayTypeVo> deptPayTypeVo,String flag, String providerid, String agreementid);

    public void copySubmitData(INSBPaychannelmanager src, INSBPaychannelmanager dest);
}