package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.app.model.ImageManagerModel;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.mobile.model.AppInsuredMycustomerModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface INSBPolicyitemDao extends BaseDao<INSBPolicyitem> {
	public INSBPolicyitem selectPolicyitemByTaskId(String taskId);

	public List<INSBPolicyitem> selectPolicyitemList(Map<String, Object> map);

	public ImageManagerModel queryModelList(String policyid);
 
	public List<Map<Object, Object>> queryImageList(String policyid);

	public List<Map<String, Object>> getImageInfoList(String agentid);

	public List<INSBPolicyitem> selectByEndDate(Map<String, Object> queryParams);

	/**
	 * 根据客户证件号查找保单信息
	 */
	public List<Map<String, Object>> queryPolicyInfobyPerson(Map<String, Object> Params);

	/**
	 * 查询保单列表信息接口使用
	 * */
	public List<Map<String, Object>> getPolicyitemListByAgentnum(Map<String, Object> queryParams);

	/**
	 * 查询保单列表信息接口使用 获取cm端Policyitem的总条数,以决定回参code是否更改
	 * 
	 * @param queryParams
	 * @return
	 */
	public long getPolicyitemListTotalsByAgentnum(Map<String, Object> queryParams);

	public List<INSBPolicyitem> selectPolicyitemList(String taskid);

	public List<AppInsuredMycustomerModel> queryCustomersInfo(Map<String, Object> map);

	public List<INSBPolicyitem> queryPolicyByDate();

	public List<Map<String, Object>> getPolicyitems(Map<String, Object> queryParams);
	public List<Map<String, Object>> getPolicyitemsForMinizzb(Map<String, Object> queryParams);

	/**
	 * 通过inscomcode和taskid查询
	 * 
	 * param maporder
	 * @return
	 */
	public List<INSBPolicyitem> selectPolicyitemByInscomTask(Map<String, Object> map);

	public List<Map> selectRemoveOverTimeData(Map<String, Object> map);

	/**
	 * 通过实例id得到当前代理人组织机构
	 * 
	 * @param taskId
	 * @return
	 */
	public List<String> selectAgentDeptIdByTaskId4Pool(String taskId);

	public List<INSBPolicyitem> getListByParam(Map<String, Object> param);

	/**
	 * 根据商业险或者交强险保单号查询是否重复
	 */
	public List<INSBPolicyitem> getListByPolicyno(String policyno, String taskid);

	/**
	 * 根据taskid和公司code查询商险折扣率和交强险折扣率
	 */
	public List<INSBPolicyitem> getListByProposalformno(String proposalformno, String taskid);

	/**
	 * 根据代理人id查询该代理人下的保单
	 * 
	 * @param param
	 * @return
	 */
	public List<Map<Object, Object>> policcyitembyagentid(Map<String, Object> param);

	/**
	 * 根据代理人id查询该代理人下的保单总数
	 * 
	 * @param param
	 * @return
	 */
	public Long policcyitembyagentidcount(Map<String, Object> param);

	/**
	 * 根据主流程id和保险公司编码查询保单记录liuchao
	 * 
	 * param mInstanceid
	 *        inscomcode
	 */
	public List<INSBPolicyitem> getPolicyByMinstanceidAndComcode(Map<String, Object> params);

	/**
	 * 根据taskid和公司code查询商险折扣率和交强险折扣率
	 */
	public List<INSBPolicyitem> getListByMap(Map<String, String> map);

	public int deleteByObj(INSBPolicyitem insbPolicyitem);
	
	public List<Map<String, Object>> getPolicyitemList(Map<String, Object> queryParams);
	public List<Map<String, Object>> getPolicyitemListForMinizzb(Map<String, Object> queryParams);
	
	public List<Map<Object, Object>> selectClosstaskPaging(Map<String, Object> data);

	public long selectClosstaskPagingCount(Map<String, Object> data);

	public List<INSBPolicyitem> selectLastest(Map<String, Object> map);
	
	
	/**
	 * 根据车主，车牌获取 已生效的保单（车童对接专用）
	 * 
	 * @param ownername
	 * @param carlicenseno
	 */
	public List<INSBPolicyitem> getPolicyByOwnernameAndCarlicenseno(String ownername,String carlicenseno);


	public List<INSBPolicyitem> selectList(INSBPolicyitem query);
	@Deprecated
	public List<INSBPolicyitem> selectAll();

	public Map<String, INSBPolicyitem> selectMap(INSBPolicyitem query, String s);

	public List<INSBPolicyitem> selectList(INSBPolicyitem query, Pageable pageable);

	public Page<INSBPolicyitem> selectPageList(INSBPolicyitem query, Pageable pageable);

	public Map<String, INSBPolicyitem> selectMap(INSBPolicyitem query, String s, Pageable pageable);

	public Long selectCount();

	public Long selectCount(INSBPolicyitem query);

	public List<INSBPolicyitem> queryList(String s, Object o);
	
	public List<Map<String, String>> selectAllPolicynoByTimePeriod(Map<String, String> queryParams);
}