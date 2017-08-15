package com.zzb.mobile.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
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
import com.zzb.mobile.dao.AppInsuredQuoteDao;
import com.zzb.mobile.model.InsuranceImageBean;
import com.zzb.mobile.model.InsureConfigsModel;
import com.zzb.mobile.model.NewEquipmentInsBean;
import com.zzb.mobile.model.QueryCarinfoModelBean;
import com.zzb.mobile.model.SelectProviderBean;
import com.zzb.mobile.model.VerificationConfigBean;

@Repository
public class AppInsuredQuoteDaoImpl extends BaseDaoImpl<INSBProvider> implements AppInsuredQuoteDao {

	@Override
	public List<InsureConfigsModel> queryInsureConfigByKey(String plankey) {
		return this.sqlSessionTemplate.selectList("appInsuredQuote.queryInsureConfigByKey",plankey);
	}
	@Override
	public List<InsureConfigsModel> querySelectedInsure(Map<String, String> para) {
		return this.sqlSessionTemplate.selectList("appInsuredQuote.querySelectedInsure", para);
	}
	@Override
	public String queryProviderList(Map<String, String> map) {
		return this.sqlSessionTemplate.selectOne("appInsuredQuote.getAllProviderList", map);
	}

	@Override
	public String getAgreementProvidersByArea(Map<String, String> map) {
		return this.sqlSessionTemplate.selectOne("appInsuredQuote.getAgreementProvidersByArea", map);
	}

	@Override
	public List<INSBProvider> queryProviderListByIds(List<String> providerIds) {
		if(providerIds.size() <= 0){
			return null;
		}
		return this.sqlSessionTemplate.selectList("appInsuredQuote.queryProviderListByIds", providerIds);
	}

	@Override
	public List<INSBAgreement> queryAgreementListByPids(List<String> providerIds) {
		return this.sqlSessionTemplate.selectList("appInsuredQuote.queryAgreementListByPids",providerIds);
	}

	@Override
	public List<INSBRiskitem> queryRiskItemsByProviderids(List<String> ids) {
		return this.sqlSessionTemplate.selectList("appInsuredQuote.queryRiskItemsByProviderids",ids);
	}

	@Override
	public INSBPerson queryPeopleInfoByTaskid(int flag, String taskid) {
		String path = "";
		if(0 == flag){
			path = "appInsuredQuote.queryInsuredPersonInfoByTaskid";
		}else if(1 == flag){
			path = "appInsuredQuote.queryApplicantPersonInfoByTaskid";
		}else{
			path = "appInsuredQuote.queryCarowneinfoPersonInfoByTaskid";
		}
		return this.sqlSessionTemplate.selectOne(path, taskid);
	}

	@Override
	public List<InsuranceImageBean> selectNeedUploadImage(List<String> list) {
		return this.sqlSessionTemplate.selectList("appInsuredQuote.selectNeedUploadImage", list);
	}

	@Override
	public List<InsuranceImageBean> selectNeedUploadImage_ByParentcode() {
			return this.sqlSessionTemplate.selectList("appInsuredQuote.selectNeedUploadImageByParentcode");
	}

	@Override
	public List<InsuranceImageBean> selectBackNeedUploadImageByCodeType(List<String> list) {
		if(list == null || list.size()==0){
			return new ArrayList<InsuranceImageBean>();
		}
		return this.sqlSessionTemplate.selectList("appInsuredQuote.selectNeedUploadImageByCodeType", list);
	}
	@Override
	public List<InsuranceImageBean> selectRuleNeedUploadImageByCodeValue(List<String> list) {
		if(list == null || list.size()==0){
			return new ArrayList<InsuranceImageBean>();
		}
		return this.sqlSessionTemplate.selectList("appInsuredQuote.selectNeedUploadImageByCodeValue", list);
	}

	@Override
	public List<Map<String, String>> selectAlreadyUploadImage(
			String processinstanceid) {
		return this.sqlSessionTemplate.selectList("appInsuredQuote.selectAlreadyUploadImage",processinstanceid);
	}
	@Override
	public List<Map<String, String>> selectUserAlreadyUploadImageCar(Map<String, String> params){
		return this.sqlSessionTemplate.selectList("appInsuredQuote.selectUserAlreadyUploadImageCarlicenseno",params);
	}

	@Override
	public long verificationInsuredConfig(String riskcode, String prid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("riskcode", riskcode);
		map.put("prid", prid);
		return this.sqlSessionTemplate.selectOne("appInsuredQuote.verificationInsuredConfig", map);
	}
	@Override
	public List<VerificationConfigBean> verificationInsuredConfigIn(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList("appInsuredQuote.verificationInsuredConfigIn", params);
	}

	@Override
	public List<Map<String, String>> selectInputCodeByElfId(Map<String, String> map) {
		return this.sqlSessionTemplate.selectList("appInsuredQuote.selectInputCodeByElfId", map);
	}

	@Override
	public INSBElfconf selectOneElfconf(String proid) {
		return this.sqlSessionTemplate.selectOne("appInsuredQuote.selectOneElfconf", proid);
	}

	@Override
	public INSCDept sellectCityAreaByAgreeid(String agreeid) {
		return this.sqlSessionTemplate.selectOne("appInsuredQuote.sellectCityAreaByAgreeid",agreeid);
	}

	@Override
	public QueryCarinfoModelBean selectCarinfoModelBean(String processinstanceid) {
		return this.sqlSessionTemplate.selectOne("appInsuredQuote.selectCarinfoModelBean", processinstanceid);
	}

	@Override
	public List<Map<String, String>> getAgreementProByCity(
			Map<String, String> map) {
		return this.sqlSessionTemplate.selectList("appInsuredQuote.getAgreementProByCity", map);
	}
	
	@Override
	public List<INSCDept> getOutProviderList(String agreementid) {
		return this.sqlSessionTemplate.selectList("appInsuredQuote.getOutProviderList", agreementid);
	}

	@Override
	public List<InsureConfigsModel> queryInsureConfigByKindCode(
			List<String> list) {
		return this.sqlSessionTemplate.selectList("appInsuredQuote.queryInsureConfigByKindCode", list);
	}
	@Override
	public List<InsureConfigsModel> querySelectedAllInsure(
			Map<String, String> para) {
		return this.sqlSessionTemplate.selectList("appInsuredQuote.querySelectedAllInsure", para);
	}
	@Override
	public int updateInsbQuoteinfoById(Map<String, String> map) {
		return this.sqlSessionTemplate.update("appInsuredQuote.updateInsbQuoteinfoById", map);
	}
	@Override
	public List<InsureConfigsModel> querySelectedAllInsuresnbxpz(
			Map<String, String> para) {
		return this.sqlSessionTemplate.selectList("appInsuredQuote.querySelectedAllInsuresnbxpz", para);
	}
	@Override
	public List<InsureConfigsModel> querySelectedInsuresnbxpz(
			Map<String, String> para) {
		return this.sqlSessionTemplate.selectList("appInsuredQuote.querySelectedInsuresnbxpz", para);
	}
	@Override
	public INSBQuoteinfo selectInsbQuoteInfoByTaskidAndPid(
			Map<String, String> map) {
		return this.sqlSessionTemplate.selectOne("appInsuredQuote.selectInsbQuoteInfoByTaskidAndPid", map);
	}
	@Override
	public List<INSBQuoteinfo> getSelectedProvidersByTaskid(String taskid) {
		return this.sqlSessionTemplate.selectList("appInsuredQuote.getSelectedProvidersByTaskid", taskid);
	}
	@Override
	public List<Map<String, String>> getCarUpdateByCode(String codetype) {
		return  this.sqlSessionTemplate.selectList("appInsuredQuote.select_all",codetype);
	}
	@Override
	public List<String> queryProviderOrderDesc(List<String> list) {
		return  this.sqlSessionTemplate.selectList("appInsuredQuote.queryProviderOrderDesc",list);
	}
	@Override
	public List<INSBCarkindprice> getInsuredConfigs(Map<String, String> map) {
		return  this.sqlSessionTemplate.selectList("appInsuredQuote.getInsuredConfigs",map);
	}
	@Override
	public Map<String, Object> getDeptInfo(Map<String, String> map) {
		return this.sqlSessionTemplate.selectOne("appInsuredQuote.getDeptInfo", map);
	}
	@Override
	public List<SelectProviderBean> getAgreementidPidByAgentid(String agentid, String setId) {
		Map<String, Object> map =new HashMap<String, Object>();
		map.put("agentid", agentid);
		map.put("setId", setId);
		return  this.sqlSessionTemplate.selectList("appInsuredQuote.getAgreementidPidByAgentid",map);
	}

    public List<SelectProviderBean> getRenewalAgreementidPidByAgentid(String agentid) {
        return this.sqlSessionTemplate.selectList("appInsuredQuote.getRenewalAgreementidPidByAgentid",agentid);
    }

    @Override
	public INSBCarmodelinfo selectCarmodelInfoByTaskid(String taskid) {
		return this.sqlSessionTemplate.selectOne("appInsuredQuote.selectCarmodelInfoByTaskid", taskid);
	}
	@Override
	public INSBWorkflowsubtrack selectOneWorkflowsubtrack(
			Map<String, String> map) {
		return this.sqlSessionTemplate.selectOne("appInsuredQuote.selectOneWorkflowsubtrack", map);
	}
	@Override
	public List<INSBAutoconfig> selectAutoconfigList(Map<String, String> map) {
		return  this.sqlSessionTemplate.selectList("appInsuredQuote.selectAutoconfigList",map);
	}
	@Override
	public String getCodeValueFromCode(String code) {
		return this.sqlSessionTemplate.selectOne("appInsuredQuote.getCodeValueFromCode", code);
	}
	@Override
	public String queryWorkflowStatusByMainAndSub(Map<String, String> map) {
		return this.sqlSessionTemplate.selectOne("appInsuredQuote.queryWorkflowStatusByMainAndSub", map);
	}
	@Override
	public List<NewEquipmentInsBean> querySpecialInsuredConf(
			Map<String, String> map) {
		return  this.sqlSessionTemplate.selectList("appInsuredQuote.querySpecialInsuredConf",map);
	}
	@Override
	public int isHaveFastRenewConfig(String parentid) {
		return this.sqlSessionTemplate.selectOne("appInsuredQuote.isHaveFastRenewConfig", parentid);
	}
	@Override
	public List<INSBCarkindprice> selectIwantCarkindpriceData(Map<String, Object> map) {
		return  this.sqlSessionTemplate.selectList("appInsuredQuote.selectIwantCarkindpriceData",map);
	}
	@Override
	public List<INSBQuoteinfo> selectQuoteInfoListDesc(String processInstanceId) {
		return  this.sqlSessionTemplate.selectList("appInsuredQuote.selectQuoteInfoListDesc",processInstanceId);
	}
	@Override
	public List<INSBCarkindprice> selectSpecialRiskkind(
			Map<String, String> datamap) {
		return  this.sqlSessionTemplate.selectList("appInsuredQuote.selectSpecialRiskkind",datamap);
	}
	@Override
	public void deletedata(String taskid) {
		this.sqlSessionTemplate.selectOne("appInsuredQuote.deletedata",taskid);
	}
	@Override
	public List<String> selectmoredata() {
		return  this.sqlSessionTemplate.selectList("appInsuredQuote.selectmoredata");
	}
	@Override
	public long getTobeinsuredNum(Map<String, Object> queryParams) {
		return this.sqlSessionTemplate.selectOne("appInsuredQuote.getTobeinsuredNum", queryParams);
	}
	@Override
	public List<Map<String, Object>> getTobePaymentOrderNum(
			Map<String, Object> queryParams) {
		return  this.sqlSessionTemplate.selectList("appInsuredQuote.getTobePaymentOrderNum",queryParams);
	}
	@Override
	public List<SelectProviderBean> getAgreementidPidByAgentidHavesetid(
			String agentid, String setId) {
		Map<String, Object> map =new HashMap<String, Object>();
		map.put("agentid", agentid);
		map.put("setId", setId);
		return  this.sqlSessionTemplate.selectList("appInsuredQuote.getAgreementidPidByAgentidHavesetid",map);
	}

}
