package com.zzb.conf.service;

import java.util.List;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBRiskkind;

public interface INSBRiskkindService extends BaseService<INSBRiskkind> {

	public Long queryListByVoCount(INSBRiskkind riskkind);

	public List<INSBRiskkind> queryListByVo(INSBRiskkind riskkind);	
	/**
	 * 按照修改时间查找数据
	 * 
	 * @param modifydate
	 * @return
	 */
	public List<INSBRiskkind> selectByModifyDate(String modifydate);
	/**
	 * 查某险种下的险别数量
	 * @param riskid
	 * @return
	 */
	public int selectCountByRiskid(String riskid);
	/**
	 * 检查当前险别code是否重复
	 * @param id
	 * @param kindcode
	 * @param riskid
	 * @return
	 */
	public int selectcountByKindcode(String id,String kindcode,String riskid);
	@Deprecated
	public List<INSBRiskkind> queryAll();
}