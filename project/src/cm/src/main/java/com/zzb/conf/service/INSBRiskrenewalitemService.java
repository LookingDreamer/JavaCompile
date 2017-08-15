package com.zzb.conf.service;

import java.util.List;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBRiskrenewalitem;

public interface INSBRiskrenewalitemService extends BaseService<INSBRiskrenewalitem> {

	public Long queryCountVo(INSBRiskrenewalitem riskrenewalitem);

	public List<INSBRiskrenewalitem> queryListVo(INSBRiskrenewalitem riskrenewalitem);

}