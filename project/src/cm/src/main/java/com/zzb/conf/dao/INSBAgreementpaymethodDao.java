package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.controller.vo.DeptPayTypeVo;
import com.zzb.conf.entity.INSBAgreementpaymethod;

public interface INSBAgreementpaymethodDao extends BaseDao<INSBAgreementpaymethod> {
	public int delByAgreeid(String agreeid);
	
	/**
	 * 根据渠道id和供应商id取得支持的支付方式 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getByChannelinnercodeAndPrvid(Map<String, Object> map);

	public List<DeptPayTypeVo> getDeptPayType(Map<String, String> map);
}