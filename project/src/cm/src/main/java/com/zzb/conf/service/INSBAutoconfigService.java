package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.entity.INSBAutoconfig;

/**
 *  It's deprecated. Please use <code>INSBAutoconfigshowService</code> instead.
 */
@Deprecated
public interface INSBAutoconfigService extends BaseService<INSBAutoconfig> {
	public int deleteByAgreementId(String agreementid,String conftype);
	
	/**
	 * 根据EDI|精灵id删除
	 * @param elfid 规则id(contentid)
	 * @param quoteType 能力类型(01:EDI  02:精灵)
	 * @return
	 */
	public int deleteByeElfId(String elfid, String quoteType);
	
	/**
	 * 根据供应商id查询自动化配置信息
	 * @return
	 */
	public List<String> queryByProId(Map<String, Object> map);
	
	
	public Map<String,String> saveEdiConfig(String type,String showid,String providerId,String deptId,String confid,String conftype,INSCUser user);
	
	/**
	 * 
	 * 得到精灵EDI 接口地址
	 * 
	 *  @param param
	 *  
	 *  出单网点  key:deptid ，
	 *  保险公司  key:providerid ，
	 *  EDI/精灵 key quotetype    value: 01/02    EDI/精灵
	 *  报价/核保/承保 key:conftype    value: 01/02/03/04/05       报价/核保/续保/承保/平台
	 * 
	 * @return 精灵/EDI接口路径
	 */
	public String getEpathByAutoConfig(Map<String,String> param);
	
	/**
	 * 根据autoshow表id删除能力配置表
	 * @param showid
	 * @return
	 */
	public int deleteautobyshowid(String showid,String parentcodes,String deptid);
	
	/**
	 * 根据autoshow表id删除能力配置表
	 * @param showid
	 * @return
	 */
	public void deleteautobyshowid(String id,String quotetype);
	/**
	 * zd-监控用
	 * @param param
	 * @return
	 */
	public List<Object> getEpathByAutoConfig4New(Map<String,String> param);
}