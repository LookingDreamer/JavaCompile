package com.cninsure.system.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.system.entity.INSCCode;
import com.zzb.model.AppCodeModel;

public interface INSCCodeDao extends BaseDao<INSCCode> {

	public List<INSCCode> selectINSCCodeByCode(Map<String, String> para);
	/**
	 * 通过任意字段得到code信息
	 * 
	 * @param para  codetype  parentcode  codevalue codename 
			</if>
	 * @return
	 */
	public void insert(INSCCode insccode);
	
	public List<INSCCode> selectINSCCodeByParentCode(Map<String,String> map);

	/**
	 * 开户行
	 * @param map
	 * @return
     */
	public List<INSCCode> selectBank(Map<String,String> map);

	/**
	 * 查询物流公司
	 * @param logisticscompany
	 * @return
	 */
	public String getCodenameByLogisticscompany(String logisticscompany);
	
	/**
	 * 通过工作流节点code得到 节点名称
	 * @return Map<codename,value>
	 */
	public Map<String,String> selectWorkflowNodeNameByCode(String codevalue);
	
	
	/**
	 * 通过订单状态得到订单信息
	 * 
	 * @param orderStatus 1:待投保、2：待支付、3：全部
	 * @return 工作流节点状态
	 */
	public String selectByOrderStatus(String orderStatus);
	
	
	/**
	 * 
	 * 通过字段类型 查询所有字段信息
	 * 
	 * @param types
	 * @return
	 */
	public List<AppCodeModel> selectByTypes(List<String> types);
	
	
	
	/**
	 * 通过名称得到value
	 * 
	 * @param codename
	 * @return
	 */
	public String selectCodeValueByCodeName(Map<String,String> map);
	
	/**
	 * 通过字典类型得到 一类信息
	 * 
	 * @param type
	 * @return
	 */
	public List<Map<String,Object>> selectByType(String type);
	
	/**
	 * 根据code得到银行信息
	 * 
	 * @return
	 */
	public INSCCode selectBankList(String codevalue);
	/**
	 * 我的任务模块有哪几种任务类型
	 * @param para
	 * @return
	 */
	public List<INSCCode> selectMyTaskCode(Map<String, String> para);
	/**
	 * 我的任务code翻译成name
	 * @param para
	 * @return
	 */
	public INSCCode transferCodeToName(Map<String, String> para);
	
	/**
	 * 车险任务管理页面任务类型下拉框选项  liuchao
	 */
	public List<Map<String, String>> getWorkFlowNodesForCarTaskQuery();
	
	public String getcodeNameBycodeValue(String forname);
	
	/**
	 * 错误信息配置
	 * @param types
	 * @return
	 */
	public List<Map<String, Object>> queryAllErrorCode(Map<String, Object> map);
	public int queryAllErrorCodeCount();

	/**
	 * 获取码表codeValue与codeName对应的Map
	 * @param codeType 代码类型
	 * @param parentCode 父级类型
	 * @return
	 */
	public Map<String, String> getCodeNamesMap(String codeType, String parentCode);
}