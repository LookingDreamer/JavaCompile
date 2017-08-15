package com.zzb.mobile.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.system.entity.INSCDept;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.conf.entity.INSBAgreement;
import com.zzb.conf.entity.INSBAutoconfig;
import com.zzb.conf.entity.INSBElfconf;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBRiskitem;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import com.zzb.mobile.model.InsuranceImageBean;
import com.zzb.mobile.model.InsureConfigsModel;
import com.zzb.mobile.model.NewEquipmentInsBean;
import com.zzb.mobile.model.QueryCarinfoModelBean;
import com.zzb.mobile.model.SelectProviderBean;
import com.zzb.mobile.model.VerificationConfigBean;

public interface AppInsuredQuoteDao extends BaseDao<INSBProvider>{

	List<InsureConfigsModel> queryInsureConfigByKey(String plankey);
	
	List<InsureConfigsModel> querySelectedInsure(Map<String, String> para);

	String queryProviderList(Map<String, String> map);

	String getAgreementProvidersByArea(Map<String, String> map);

	List<INSBProvider> queryProviderListByIds(List<String> providerIds);

	List<INSBAgreement> queryAgreementListByPids(List<String> providerIds);

	List<INSBRiskitem> queryRiskItemsByProviderids(List<String> ids);

	/**
	 * 
	 * @param flag 0 被保人 1 投保人 2 车主
	 * @param taskid 任务id
	 * @return
	 */
	INSBPerson queryPeopleInfoByTaskid(int flag, String taskid);

	List<InsuranceImageBean> selectNeedUploadImage(List<String> list);
	List<InsuranceImageBean> selectNeedUploadImage_ByParentcode();
	//按codetype查询
	List<InsuranceImageBean> selectBackNeedUploadImageByCodeType(List<String> list);
	//按codevalue查询
	List<InsuranceImageBean> selectRuleNeedUploadImageByCodeValue(List<String> list);

	List<Map<String, String>> selectAlreadyUploadImage(String processinstanceid);
	List<Map<String, String>> selectUserAlreadyUploadImageCar(Map<String, String> params);

	long verificationInsuredConfig(String riskcode, String prid);

	List<Map<String, String>> selectInputCodeByElfId(Map<String, String> map);

	INSBElfconf selectOneElfconf(String proid);

	INSCDept sellectCityAreaByAgreeid(String agreeid);

	QueryCarinfoModelBean selectCarinfoModelBean(String processinstanceid);
	
	List<Map<String, String>> getAgreementProByCity(Map<String, String> map);
	
	List<INSCDept> getOutProviderList(String agreementid);

	List<InsureConfigsModel> queryInsureConfigByKindCode(List<String> list);

	
	List<InsureConfigsModel> querySelectedAllInsure(Map<String, String> para);

	int updateInsbQuoteinfoById(Map<String, String> map);

	List<InsureConfigsModel> querySelectedAllInsuresnbxpz(
			Map<String, String> para);

	List<InsureConfigsModel> querySelectedInsuresnbxpz(Map<String, String> para);

	/**
	 * 根据任务id，保险公司编码查询一条报价信息
	 * @param taskid 任务id inscomcode 保险公司编码
	 * @return
	 */
	INSBQuoteinfo selectInsbQuoteInfoByTaskidAndPid(Map<String, String> map);
	/**
	 * 根据实例id获取报价信息表数据
	 * @param taskid
	 * @return
	 */
	List<INSBQuoteinfo> getSelectedProvidersByTaskid(String taskid);
	/**
	 * 根据codetype从字典表中查询车辆性质、所属性质、车价选择信息
	 * @param codetype
	 * @return
	 */
	List<Map<String, String>> getCarUpdateByCode(String codetype);
	/**
	 * 查询保单表，按照供应商排序，降序的
	 * @return
	 */
	List<String> queryProviderOrderDesc(List<String> list);

	List<INSBCarkindprice> getInsuredConfigs(Map<String, String> map);

	Map<String, Object> getDeptInfo(Map<String, String> map);

	List<SelectProviderBean> getAgreementidPidByAgentid(String agentid, String setId);

    List<SelectProviderBean> getRenewalAgreementidPidByAgentid(String agentid);

	INSBCarmodelinfo selectCarmodelInfoByTaskid(String processinstanceid);

	INSBWorkflowsubtrack selectOneWorkflowsubtrack(Map<String, String> map);

	List<INSBAutoconfig> selectAutoconfigList(Map<String, String> map);

	String getCodeValueFromCode(String code);

	String queryWorkflowStatusByMainAndSub(Map<String, String> map);

	List<NewEquipmentInsBean> querySpecialInsuredConf(Map<String, String> map);

	int isHaveFastRenewConfig(String parentid);
	/**
	 * 查询自己想要的数据
	 * @param insbCarkindprice
	 * @return
	 */
	List<INSBCarkindprice> selectIwantCarkindpriceData(Map<String, Object> map);
	/**
	 * 多方报价列表数据排序
	 * @param processInstanceId
	 * @return
	 */
	List<INSBQuoteinfo> selectQuoteInfoListDesc(String processInstanceId);

	List<INSBCarkindprice> selectSpecialRiskkind(Map<String, String> datamap);

	void deletedata(String string);

	List<String> selectmoredata();
	/**
	 * 查询待投保单个数
	 * @param queryParams
	 * @return
	 */
	long getTobeinsuredNum(Map<String, Object> queryParams);
	/**
	 * 查询待支付的单子
	 * @param queryParams
	 * @return
	 */
	List<Map<String, Object>> getTobePaymentOrderNum(
			Map<String, Object> queryParams);

	/**
	 *
	 * @param agentid
	 * @param setId 权限包id
     * @return
     */
	List<SelectProviderBean> getAgreementidPidByAgentidHavesetid(String agentid, String setId);
	
	public List<VerificationConfigBean> verificationInsuredConfigIn(Map<String, Object> params);

}
