package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCDept;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.model.ProOrParModel;

public interface INSBProviderService extends BaseService<INSBProvider> {
	
	public Map<String, Object> showProviderList(Map<String, Object> map);
	
	public List<Map<Object, Object>> queryProTreeList(String parentcode);
	public List<Map<Object, Object>> queryProTreeList2(ProOrParModel parentcode);
	
	public List<INSBProvider> queryProByInsureAreaCode(String insureAreaCode);
	public List<INSBProvider> queryProByInsureAreaCode2(Map<String, Object> map);
	
	public int addProData(INSBProvider pro);
	
	public INSBProvider queryProinfoById(String id);
	/**
	 * ztree
	 * @param parentcode
	 * @return
	 */
	public List<Map<String, String>> queryTreeList(String parentcode);
	
	/**
	 * 
	 * 只拿第一层
	 * @param parentcode
	 * @return
	 */
	public List<Map<String, String>> queryTreeListFirst(String parentcode);
	
	public List<Map<String, String>> queryTreeListStair();
	
	public INSBProvider queryByPrvcode(String prvcode);
	
	public int updateProById(String id);
	
	public int updateProByIddel(String id);
	
	public List<INSBProvider> queryListPro(String ediid);
	
	
	/**
	 * 按照修改时间查询供应商信息
	 * 
	 * @param modiftime
	 * @return
	 */
	public List<INSBProvider> queryDataByModifyTime(String modiftime);

	/**
	 * 通过code得到供应商
	 * @param insComCodeList
	 * @return
	 */
	public List<INSBProvider> getInscomNameList(List<String> insComCodeList);
	
	
	public INSBProvider getProvDept(String providerCode);

	
	/**
	 * 根据供应商id返回报价有效周期
	 * @param id
	 * @return
	 */
	public int getQuotationValidityById(String id);

	/**
	 * 获取顶级供应商
	 * @return
	 */
	public List<INSBProvider> getprovidedata();
	/**
	 * 查询所有的供应商树
	 * @return
	 */
	public List<Map<String, String>> queryAllProTreeList();
	@Deprecated
	public List<INSBProvider> queryAll();
	
}