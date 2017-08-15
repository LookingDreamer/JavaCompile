package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.conf.entity.INSBAgreement;

import java.util.List;
import java.util.Map;

public interface INSBQuoteinfoDao extends BaseDao<INSBQuoteinfo> {

	/**
	 *  获取规则信息
	 * 
	 * @param   para
	 * @return
	 */
	public INSBAgreement queryAgreementIdByTaksId(Map<String, String> para);

	/**
	 *  通过子流程ID获得报价信息
	 * 
	 * @param workflowinstanceid
	 * @return
	 */
	public INSBQuoteinfo queryQuoteinfoByWorkflowinstanceid(
			String workflowinstanceid);

	public int closeTask(Map<String, String> params);

	/**
	 * 任务规则
	 * 
	 * 通过供应商id报价总表id得到网点id
	 * 
	 * @param param
	 * @return
	 */
	public String selectDeptcodeByProviderIdTatolId(Map<String, String> param);

	/**
	 * 获得报价信息
	 * 
	 * @param param
	 * @return
	 */
	public INSBQuoteinfo selectQuoteinfoByQuotetotalinfoidAndinscomcode(Map<String, String> param);

	public INSBQuoteinfo getByTaskidAndCompanyid(Map<String, String> param);
	
	/**
	 * 根据供应商id和实例id，找到出单网点
	 * @param map
	 * @return
	 */
	public String querydeptcode(Map<String, String> map);
	
	/**
	 * 
	 * 通过主实例id得到
	 * @param quotetotalinfoid
	 * @return
	 */
	public List<Map<String,String>> selectSubInstanceIdProviderIdByTotalId(String quotetotalinfoid);

	/**
	 * 通过任务id得到对应报价表中网点
	 * @param param
	 * @return
	 */
	public List<String> selectDeptIdByQuotetotalIdAndComCode4Task(Map<String,String> param);
	
	/**
	 * 通过子流程id查询该子流程规则试算后的总保费
	 * @param subInstanceId
	 * @return
	 */
	public Double getTotalDiscountAmountPrice(String subInstanceId);
	
	/**
	 * @param subInstancId
	 * @return
	 */
	public Map<String,String> selectMainInstanceIdProviderBySubInstanceId(String subInstancId);
	
	public List<Map<String, String>> selectWorkflowinstanceid(String taskid);
	
	public List<Map<String, String>> selectByTaskid(String taskid);

	public List<Map<String, Object>> selectComcodeByTaskid(Map<String, Object> map);

	
	public int deleteByObj(INSBQuoteinfo insbQuoteinfo);

	public List<Map<String,String>> selectTaskInfo(Map<String, Object> map);
	
	public List<Map<String, String>> queryDeptAllQuoteCount(Map<String,String> param);

}