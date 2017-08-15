package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.common.PagingParams;
import com.zzb.conf.controller.vo.BaseInfoVo;
import com.zzb.conf.controller.vo.BillTypeVo;
import com.zzb.conf.controller.vo.InterfaceVo;
import com.zzb.conf.entity.INSBAgreementdept;
import com.zzb.conf.entity.INSBChannel;
import com.zzb.conf.entity.INSBChannelagreement;

public interface INSBChannelagreementService extends BaseService<INSBChannelagreement> {

	public BillTypeVo getBillTypeInfo(INSBChannel insbChannel,int offset,int limit);
	
	public List<INSBAgreementdept> existChannelid(String channelid); 

	public String updateBillTypeInfo(BillTypeVo billTypeVo);
	
	public List<Map<String,Object>> qChannelAgreementprovider(String channelid,PagingParams para);
 
	/**
	 * 保存基础信息
	 * @param baseInfoVo
	 * @return
	 */
	public String saveBaseInfo(BaseInfoVo baseInfoVo);

	public List<InterfaceVo> getInterfaceInfo(String agreementid, InterfaceVo interfaceVo,int offset,int limit);
	
	public Long getCountProvider(String channelid);
	

	public Map<String, String> getDeptIdByChannelinnercodeAndPrvcode(String channelinnercode, String city);
	
	public String getAgreementByArea(String channelinnercode, String prvcode, String city);
	
	public Map<String, Object> aglist(Map<String, Object> map);
	
	public Map<String, Object> agchnlist(Map<String, Object> map);
	
	public String delchn(String ids);
	
	public String addchn(String operator, String channelIds, String agreeId, String deptId, String payIds);
	
	public String swagchn(String operator, String channelIds, String agreeId, String toAgreeId, String payIds, String deptId);
	
	public Map<String, Object> agdeptlist(Map<String, Object> map);
}
