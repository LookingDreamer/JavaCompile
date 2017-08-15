package com.zzb.conf.service;

import java.util.List;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.controller.vo.InterfaceVo;
import com.zzb.conf.entity.INSBAgreementinterface;

public interface INSBAgreementinterfaceService extends BaseService<INSBAgreementinterface> {
  
	public InterfaceVo saveInterfaces(String agreementid, InterfaceVo interfaceVo);
	
	/**
	 * 批量开启和关闭
	 * @param agreementid
	 * @param inters
	 * @return
	 */
	public void bashSaveInterface(List<InterfaceVo> inters);
}