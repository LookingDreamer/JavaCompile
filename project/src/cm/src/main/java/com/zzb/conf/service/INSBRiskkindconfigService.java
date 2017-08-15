package com.zzb.conf.service;

import java.util.List;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBRiskkindconfig;

public interface INSBRiskkindconfigService extends BaseService<INSBRiskkindconfig> {
	/**
	 * 险别新增里的通过险别编码查询险别。
	 * @param riskkindcode
	 * @return
	 */
	public INSBRiskkindconfig selectKindByKindcode(String riskkindcode);
	@Deprecated
	public List<INSBRiskkindconfig> queryAll();
}