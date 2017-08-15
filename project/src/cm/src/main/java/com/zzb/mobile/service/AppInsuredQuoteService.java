package com.zzb.mobile.service;

import java.util.List;
import java.util.Map;

import com.zzb.extra.model.SearchProviderModelForMinizzb;
import com.zzb.mobile.CheckAddQuoteProviderRoleModel;
import com.zzb.mobile.model.*;

public interface AppInsuredQuoteService {
	List<String> queryUserCommentUploadFile_codeType(String processinstanceid, String inscomcode);
	String queryUserCommentContent(String processinstanceid, String inscomcode);

	CommonModel createInsuredInit(InsuredQuoteCreateModel model);

	CommonModel recommendProvider(SearchProviderModel model);

	CommonModel recommendCarModel();

	CommonModel searchCarModel(String modelName,String pageSize,String currentPage);

	CommonModel updateCarModelAndCarinfo(CarModelInfoModel model);

	CommonModel saveCarInfo(SaveCarInfoModel model);

	ExtendCommonModel searchProvider(SearchProviderModel model);
	ExtendCommonModel searchProviderForMinizzb(SearchProviderModelForMinizzb model);

	CommonModel choiceProviderIds(ChoiceProviderIdsModel model);

	CommonModel insuranceScheme(String plankey,String taskid);
	
	CommonModel querySelectedInsure(String proid,String taskid);
	
	CommonModel queryInsureInfo(String processinstanceid,String inscomcode);
	
	CommonModel saveOrUpdateInsureInfo(AppInsureinfoBean bean);

	CommonModel insuredConfig(InsuredConfigModel model);

	CommonModel supplementInfo(String taskid);

	CommonModel peopleInfo(PeopleInfoModel model);

	CommonModel carowerInfo(CarowerInfoModel model);

	CommonModel insuredDate(InsuredDateModel model);

	CommonModel driversInfo(DriversInfoModel model);

	CommonModel otherInsuredInfo(OtherInsuredInfoModel model);

	CommonModel getQuoteArea(String agentid);
	
	Map<String, String> getQuoteAreaByAgentid(String agentid);

	CommonModel lastInsuredRenewaByCar(LastInsuredRenewaByCarModel model);

	CommonModel renewaSubmit(RenewaSubmitModel model);

	CommonModel schemeList(String taskid, String agentnotitype);

	CommonModel needUploadImage(String processinstanceid);
	CommonModel selectNeedUploadImage_ByParentcode(String processinstanceid);
	ExtendCommonModel needUploadImageByCodeType(String processinstanceid, String inscomcode, String status);

	CommonModel alreadyUploadImage(String processinstanceid);
	CommonModel myUploadImage(String agentnum, String carlicenseno);;

	CommonModel saveInsuranceBooks(String processinstanceid);

	CommonModel verificationInsuredConfig(InsuredConfigModel model);

	CommonModel queryLastInsuredInfo(String processinstanceid,
			String plateNumber);

	CommonModel getProviderSingleSite(String processinstanceid,String pid);

	CommonModel updateSingleSite(UpdateSingleSiteModel model);

	CommonModel queryTermsByAgreeid(String agreeid);

	CommonModel saveUploadImage(SaveUploadImageModel model);

	CommonModel carModelInfo(SelectCarModel model);

	CommonModel queryLastInsuredByNumOrCar(QueryLastInsuredByNumOrCarModel model);

	CommonModel queryCarinfoModel(String processinstanceid);

	CommonModel addQuoteProvider(SearchProviderModel model);

	CommonModel updateInduredConfig(InsuredOneConfigModel model);

	CommonModel saveAddQuoteProvider(ChoiceProviderIdsModel model);

	CommonModel workflowStartQuote(WorkflowStartQuoteModel model);

	CommonModel workFlowRestartConf(WorkFlowRestartConfModel model);

	CommonModel checkUpdateInsureConf(InsuredOneConfigModel model);

	CommonModel callPlatformQuery(CallPlatformQueryModel model);

	CommonModel lastInsuredRenewaByCar(CallPlatformQueryModel model);

	CommonModel findInitWebPage(String processinstanceid, String webpagekey);
	
	/**
	 * 根据codetype从字典表中查询车辆性质、所属性质、车价选择信息 
	 * @return
	 */
	List<Map<String, String>> getCarUpdateByCode(String codetype);

	CommonModel fastRenewProviders(SearchProviderModel model);

    CommonModel saveRenewalQuoteinfo(RenewalQuoteinfoModel model);

	CommonModel fastRenewCallPlatformQuery(CallPlatformQueryModel model);
	
	CommonModel getWebpagekey(String processinstanceid);

	CommonModel addQuoteProviderWorkflowold(ChoiceProviderIdsModel model);

	CommonModel insuranceBookSearch(String taskid);

	CommonModel deleteUpdateImage(String processinstanceid, String fileid);

	Map<String, Object> getChangeFee(String agentid,String jobnum);

	CommonModel searchCarModelVin(String modelname, String pagesize,
			String currentpage, String carlicenseno, String agentid);

	CommonModel queryOtherPersonInfo(String taskid);

	CommonModel queryInsureInfoHis(String processinstanceid, String inscomcode);

	CommonModel queryClaiminfo(String processinstanceid);
	
	public CommonModel getProvider(String taskid,String inscomcode);

	CommonModel checkAddQuoteProviderRole(CheckAddQuoteProviderRoleModel model);

	CommonModel addQuoteProviderWorkflow(CheckAddQuoteProviderRoleModel model);

	CommonModel checkRestartConf(InsuredOneConfigModel model);

    public String saveRemarkInTabSubtrack(String taskid, String inscomcode, String remark, String remarkcode, String operator);
    
    /**
	 * 普通平台查询定时器到时间调用方法
	 * @param taskid
	 * @param subwfid
	 * @param inscomcode
	 */
	void saveInsuredDateBycifBack(String taskid,String subwfid,String inscomcode);
	/**
	 * 普通平台查询有数据返回调用
	 * @param taskid 主流程
	 * @param subwkid 子流程
	 * @param inscomcode 供应商id
	 * @param systartdate 上年商业险终保日期
	 * @param jqstartdate 上年交强险终 保日期
	 * @param syrepeatinsurance 上年商业险重复投保提示
	 * @param jqrepeatinsurance 上年交强险重复投保提示
	 * @param lastyearpid 上年投保公司
	 */
	void dateIsRepeatInsured(String taskid,String subwkid,String inscomcode,String systartdate,String jqstartdate,String syrepeatinsurance,String jqrepeatinsurance,String lastyearpid);
	/**
	 * 判断这个任务的代理人是否拥有该供应商的协议
	 * @param taskid
	 * @param inscomcode
	 * @return
	 */
	String judgeThisPidIsAgentHave(String taskid, String inscomcode);
	/**
	 * insbflowerror 保存相应的数据
	 * @param taskid
	 * @param inscomcode
	 * @param errdesc
	 * @param flowcode
	 */
	void saveFlowerrorToManWork(String taskid,String inscomcode,String errdesc,String flowcode);
	/**
	 * 新增上年投保公司报价
	 * @param taskid
	 * @param lastyearpid
	 */
	void addLastYearProidToinsured(String taskid,String lastyearpid);
	public boolean saveCarmodelinfoToData(String taskid, String inscomcode, String subworkflowid);
	public void createHisTableInit(String taskid);

	public CommonModel pay(String taskid);
	/**
	 * 获取供应商之前，校验数据是否完整
	 * @param taskID 任务号
	 */
	public ExtendCommonModel inspectInfo(String taskID);
	/**
	 * test
	 * @param taskid
	 */
	public void saveAllPersonInfo(String taskid);
	/**
	 * 组装报价途径,传参给工作流
	 * @param datamap
	 * @param taskid
	 * @param inscomcode
	 * @param type 当前报价类型 0为初始，1为EDI，2为精灵，3为规则。。。
	 * @return
	 */
	public Map<String, Object> getPriceParamWay(Map<String, Object> datamap,String taskid,String inscomcode,String type);
	/**
	 * 初启动自流长组装报价途径,传参给工作流
	 * @param datamap
	 * @param taskid
	 * @param inscomcode
	 * @return
	 */
	public Map<String, Object> getPriceParamWayStartSubflow(Map<String, Object> datamap,String taskid,String inscomcode);

    /**
     * 判断是否为转人工备注，prop1 为1需要转人工
     *
     * @param codevalue
     * @return flag true 转人工标志 result 转人工类型
     */
    public Map<String, Object> isNeedToManMade(String codevalue);
}
 