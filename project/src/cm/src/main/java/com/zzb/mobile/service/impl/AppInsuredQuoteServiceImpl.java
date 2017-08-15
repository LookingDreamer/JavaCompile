package com.zzb.mobile.service.impl;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.common.*;
import com.zzb.ads.service.INSBAdsService;
import com.zzb.ads.util.AdsUtil;
import com.zzb.cm.entity.*;
import com.zzb.cm.service.*;
import com.zzb.conf.dao.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.jobpool.Pool;
import com.cninsure.jobpool.Task;
import com.cninsure.jobpool.dispatch.DispatchTaskService;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCDeptService;
import com.common.redis.CMRedisClient;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import com.lzgapi.order.service.LzgOrderService;
import com.zzb.app.model.bean.RisksData;
import com.zzb.app.model.bean.SelectOption;
import com.zzb.app.service.AppQuotationService;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.cm.dao.INSBApplicantDao;
import com.zzb.cm.dao.INSBApplicanthisDao;
import com.zzb.cm.dao.INSBCarconfigDao;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarinfohisDao;
import com.zzb.cm.dao.INSBCarkindpriceDao;
import com.zzb.cm.dao.INSBCarmodelinfoDao;
import com.zzb.cm.dao.INSBCarmodelinfohisDao;
import com.zzb.cm.dao.INSBCarowneinfoDao;
import com.zzb.cm.dao.INSBDeliveryaddressDao;
import com.zzb.cm.dao.INSBFilebusinessDao;
import com.zzb.cm.dao.INSBFlowerrorDao;
import com.zzb.cm.dao.INSBInsuredDao;
import com.zzb.cm.dao.INSBInsuredhisDao;
import com.zzb.cm.dao.INSBInvoiceinfoDao;
import com.zzb.cm.dao.INSBLastyearinsurestatusDao;
import com.zzb.cm.dao.INSBLegalrightclaimDao;
import com.zzb.cm.dao.INSBLegalrightclaimhisDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.dao.INSBRelationpersonDao;
import com.zzb.cm.dao.INSBRelationpersonhisDao;
import com.zzb.cm.dao.INSBRulequerycarinfoDao;
import com.zzb.cm.dao.INSBRulequeryrepeatinsuredDao;
import com.zzb.cm.dao.INSBSpecialkindconfigDao;
import com.zzb.cm.dao.INSBSpecifydriverDao;
import com.zzb.cm.dao.INSBSupplementaryinfoDao;
import com.zzb.cm.dao.INSHCarkindpriceDao;
import com.zzb.conf.controller.vo.AgentRelatePermissionVo;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBAgreement;
import com.zzb.conf.entity.INSBAutoconfigshow;
import com.zzb.conf.entity.INSBAutoconfigshowQueryModel;
import com.zzb.conf.entity.INSBChannel;
import com.zzb.conf.entity.INSBElfconf;
import com.zzb.conf.entity.INSBOutorderdept;
import com.zzb.conf.entity.INSBPermissionset;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBRiskitem;
import com.zzb.conf.entity.INSBRiskkindconfig;
import com.zzb.conf.entity.INSBRiskplanrelation;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.entity.INSBUsercommentUploadFile;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowmaintrack;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import com.zzb.conf.service.INSBAutoconfigshowService;
import com.zzb.conf.service.INSBChannelService;
import com.zzb.conf.service.INSBPermissionallotService;
import com.zzb.conf.service.INSBPolicyitemService;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.extra.model.SearchProviderModelForMinizzb;
import com.zzb.extra.model.SelectProviderBeanForMinizzb;
import com.zzb.mobile.CheckAddQuoteProviderRoleModel;
import com.zzb.mobile.dao.AppInsuredQuoteDao;
import com.zzb.mobile.model.AppAllPersonInfo;
import com.zzb.mobile.model.AppBusiness;
import com.zzb.mobile.model.AppCarInfo;
import com.zzb.mobile.model.AppInsureinfoBean;
import com.zzb.mobile.model.AppPerson;
import com.zzb.mobile.model.BranchProBean;
import com.zzb.mobile.model.BusinessInsureddateBean;
import com.zzb.mobile.model.BusinessRisks;
import com.zzb.mobile.model.CallPlatformQueryModel;
import com.zzb.mobile.model.CarDriver;
import com.zzb.mobile.model.CarModelInfoBean;
import com.zzb.mobile.model.CarModelInfoModel;
import com.zzb.mobile.model.CarowerInfoModel;
import com.zzb.mobile.model.CarowerinfoBean;
import com.zzb.mobile.model.CheckInsuredConfigModel;
import com.zzb.mobile.model.CheckUpdateInsureConfBean;
import com.zzb.mobile.model.ChoiceProviderIdsModel;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.DriversInfoModel;
import com.zzb.mobile.model.ExtendCommonModel;
import com.zzb.mobile.model.GuiZeCheckBean;
import com.zzb.mobile.model.InsuranceBookModel;
import com.zzb.mobile.model.InsuranceImageBean;
import com.zzb.mobile.model.InsureConfigsBean;
import com.zzb.mobile.model.InsureConfigsModel;
import com.zzb.mobile.model.InsurePersionBean;
import com.zzb.mobile.model.InsuredConfig;
import com.zzb.mobile.model.InsuredConfigModel;
import com.zzb.mobile.model.InsuredDateModel;
import com.zzb.mobile.model.InsuredInfoModel;
import com.zzb.mobile.model.InsuredOneConfigModel;
import com.zzb.mobile.model.InsuredQuoteCreateModel;
import com.zzb.mobile.model.LastInsuredRenewaByCarModel;
import com.zzb.mobile.model.NewEquipmentInsBean;
import com.zzb.mobile.model.OtherInsuredInfoModel;
import com.zzb.mobile.model.PassiveInsurePersionBean;
import com.zzb.mobile.model.PeopleInfoModel;
import com.zzb.mobile.model.ProviderListBean;
import com.zzb.mobile.model.QueryCarinfoModelBean;
import com.zzb.mobile.model.QueryLastInsuredByNumOrCarModel;
import com.zzb.mobile.model.RenewaSubmitModel;
import com.zzb.mobile.model.RenewalQuoteinfoModel;
import com.zzb.mobile.model.SaveCarInfoModel;
import com.zzb.mobile.model.SaveUploadImageModel;
import com.zzb.mobile.model.SearchProviderModel;
import com.zzb.mobile.model.SelectCarModel;
import com.zzb.mobile.model.SelectProviderBean;
import com.zzb.mobile.model.SingleSiteBean;
import com.zzb.mobile.model.StrongRisk;
import com.zzb.mobile.model.SupplementInfo;
import com.zzb.mobile.model.SupplementaryinfoBean;
import com.zzb.mobile.model.TrafficInsureddateBean;
import com.zzb.mobile.model.UpdateSingleSiteModel;
import com.zzb.mobile.model.VerificationConfigBean;
import com.zzb.mobile.model.WorkFlowRestartConfModel;
import com.zzb.mobile.model.WorkFlowRuleInfo;
import com.zzb.mobile.model.WorkflowStartQuoteModel;
import com.zzb.mobile.model.lastinsured.CarModel;
import com.zzb.mobile.model.lastinsured.LastYearCarinfoBean;
import com.zzb.mobile.model.lastinsured.LastYearClaimBean;
import com.zzb.mobile.model.lastinsured.LastYearPersonBean;
import com.zzb.mobile.model.lastinsured.LastYearPolicyBean;
import com.zzb.mobile.model.lastinsured.LastYearPolicyInfoBean;
import com.zzb.mobile.model.lastinsured.LastYearSupplierBean;
import com.zzb.mobile.service.AppInsuredQuoteService;
import com.zzb.mobile.service.AppOtherRequestService;
import com.zzb.mobile.service.AppPermissionService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Service
@Transactional
public class AppInsuredQuoteServiceImpl implements AppInsuredQuoteService {

	@Resource
	private AppInsuredQuoteDao appInsuredQuoteDao;
	@Resource
	private INSBUsercommentUploadFileDao insbusercommentUploadFileDao;
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSBCarowneinfoDao insbCarowneinfoDao;
	@Resource
	private INSBPersonDao insbPersonDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBCarmodelinfoDao insbCarmodelinfoDao;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;
	@Resource
	private INSBCarconfigDao insbCarconfigDao;
	@Resource
	private INSBCarkindpriceDao insbCarkindpriceDao;
	@Resource
	private INSBOutorderdeptDao insbOutorderdeptDao;
	@Resource
	private INSCDeptDao inscDeptDao;
	@Resource
	private INSBRegionDao insbRegionDao;
	@Resource
	private INSBApplicantDao insbApplicantDao;
	@Resource
	private INSBInsuredDao insbInsrueDao;
	@Resource
	private INSBSpecifydriverDao insbSpecifydriverDao;
	@Resource
	private INSBSupplementaryinfoDao insbSupplementaryinfoDao;
	@Resource
	private INSBWorkflowmainDao insbWorkflowmainDao;
	@Resource
	private INSBWorkflowmainService insbWorkflowmainService;
	@Resource
	private INSBWorkflowDataService insbWorkflowDataService;
	@Resource
	private INSBRiskplanrelationDao insbRiskplanrelationDao;
	@Resource
	private INSBRiskkindconfigDao insbRiskkindconfigDao;
	@Resource
	private INSBProviderDao insbProviderDao;
	@Resource
	private INSBFilebusinessDao insbFilebusinessDao;
	@Resource
	private INSCCodeDao inscCodeDao;
	@Resource
	private INSBInsuredDao insbInsuredDao;
	@Resource
	private AppQuotationService appQuotationService;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBLegalrightclaimDao insbLegalrightclaimDao;
	@Resource
	private INSBWorkflowmaintrackDao insbWorkflowmaintrackDao;
	@Resource
	private INSBUsercommentDao insbUsercommentDao;
	@Resource
	private INSBWorkflowsubtrackDao insbWorkflowsubtrackDao;
	@Resource
	private INSBCarinfohisDao insbCarinfohisDao;
	@Resource
	private INSBApplicanthisDao insbApplicanthisDao;
	@Resource
	private INSBRelationpersonhisDao insbRelationpersonhisDao;
	@Resource
	private INSBLegalrightclaimhisDao insbLegalrightclaimhisDao;
	@Resource
	private INSBRelationpersonDao insbRelationpersonDao;
	@Resource
	private INSBInsuredhisDao insbInsuredhisDao;
	@Resource
	private INSBCarmodelinfohisDao insbCarmodelinfohisDao;
	@Resource
	private LzgOrderService lzgOrderService;
	@Resource
	private INSBDeliveryaddressDao insbDeliveryaddressDao;
	@Resource
	private INSBSpecialkindconfigDao insbSpecialkindconfigDao;
	@Resource
	private INSBRulequerycarinfoDao insbRulequerycarinfoDao;
	@Resource
	private InterFaceService interFaceService;
	@Resource
	private INSBFlowerrorDao insbFlowerrorDao;
	@Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
	@Resource
	private INSBQuoteVerifyService insbQuoteVerifyService;
	@Resource
	private INSHCarkindpriceDao inshCarkindpriceDao;
	@Resource
	private INSBAutoconfigshowService insbAutoconfigshowService;
	@Resource
	private INSBWorkflowsubDao insbWorkflowsubDao;
	@Resource
	private INSBInvoiceinfoDao insbInvoiceinfoDao;
	@Resource
	private INSBChannelService insbChannelService;
	@Resource
	private INSBAgreementDao insbAgreementDao;
    @Resource
    private INSBUsercommentService insbUsercommentService;
	@Resource
    private IRedisClient redisClient;
	@Resource
	private INSBLastyearinsurestatusDao insbLastyearinsurestatusDao;
	@Resource
	private Scheduler sched;
	@Resource
	private AppPermissionService appPermissionService;
	@Resource
	private AppOtherRequestService appOtherRequestService;
	@Resource
	private INSBPermissionallotService insbPermissionallotService;
	@Resource
	private INSBWorkflowsubService workflowsubService;
	@Resource
	private DispatchTaskService dispatchService;
	@Resource
	private INSBCommonQuoteinfoService commonQuoteinfoService;
	@Resource
	private INSBPolicyitemService insbPolicyitemService;// 保单信息表
	@Resource
	private INSCDeptService inscDeptService;//机构表
	@Resource
	private INSBPersonHelpService insbPersonHelpService;
	@Resource
	private INSBRulequeryrepeatinsuredDao insbRulequeryrepeatinsuredDao;
    @Resource
    private INSBRealtimetaskDao insbRealtimetaskDao;
	@Resource
	private INSBInsuresupplyparamService insbInsuresupplyparamService;
	@Resource
	private INSBChannelagreementDao insbChannelagreementDao;
	@Resource
	private INSBAdsService insbAdsService;

	//REDIS过期时间
	private static int OVER_TIME_SECONDS = 2*24*3600;

	@Override
	public CommonModel getQuoteArea(String agentid) {
		CommonModel commonModel = new CommonModel();
		try {
			if (StringUtil.isEmpty(agentid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("代理人id不能为空");
				return commonModel;
			}
			// 根据代理人id查询代理人所属机构
			INSBAgent agent = insbAgentDao.selectById(agentid);
			if (null == agent) {
				commonModel.setStatus("fail");
				commonModel.setMessage("代理人不存在");
				return commonModel;
			}
			// 根据机构查询所属区域
			INSCDept inscDept = inscDeptDao.selectById(agent.getDeptid());
			if (null == inscDept) {
				commonModel.setStatus("fail");
				commonModel.setMessage("代理人所属机构不存在");
				return commonModel;
			}
			Map<String, String> result = new HashMap<String, String>();
			result.put("provinceCode", inscDept.getProvince());
			result.put("provinceName", insbRegionDao.selectById(inscDept.getProvince()).getComcodename());
			result.put("cityCode", inscDept.getCity());
			result.put("cityName", insbRegionDao.selectById(inscDept.getCity()).getComcodename());
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(result);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public Map<String, String> getQuoteAreaByAgentid(String agentid) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			INSBAgent agent = insbAgentDao.selectById(agentid);
			// 根据机构查询所属区域
			INSCDept inscDept = inscDeptDao.selectById(agent.getDeptid());
			result.put("provinceCode", inscDept.getProvince());
			result.put("provinceName", insbRegionDao.selectById(inscDept.getProvince()).getComcodename());
			result.put("cityCode", inscDept.getCity());
			result.put("cityName", insbRegionDao.selectById(inscDept.getCity()).getComcodename());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public CommonModel createInsuredInit(InsuredQuoteCreateModel model) {
		ExtendCommonModel commonModel = new ExtendCommonModel();
		try {
			if (StringUtil.isEmpty(model.getFlag())) {
				commonModel.setStatus("fail");
				commonModel.setMessage("是否为快速续保标志不能为空");
				return commonModel;
			}

			if ("".equals(model.getAgentid())) {
				commonModel.setStatus("fail");
				commonModel.setMessage("代理人id不能为空");
				return commonModel;
			}
			// 查询代理人信息
			INSBAgent insbAgent = insbAgentDao.selectById(model.getAgentid());
			if (null == insbAgent) {
				commonModel.setStatus("fail");
				commonModel.setMessage("代理人不存在");
				return commonModel;
			}

			//权限包
			Map<String,Object> permissionMap = appPermissionService.findPermission(insbAgent.getJobnum(), "", "insured");
			if ((int)permissionMap.get("status") == 2 || (int)permissionMap.get("status") == -1) {
				commonModel.setStatus(CommonModel.STATUS_FAIL);
				commonModel.setMessage((String) permissionMap.get("message"));
				return commonModel;
			}
			if ((int)permissionMap.get("status") == 1) {
				commonModel.setExtend(permissionMap);
			}

			//0 投保  1 快速续保
			String flag = model.getFlag();
			// 调用工作流接口，获取实例id
			String taskid = WorkFlowUtil.startWorkflowProcess(flag);
			if ("".equals(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("获取实例id失败");
				return commonModel;
			}

			//保存行驶证上的扫描地址
			if(!StringUtil.isEmpty(model.getAddress())){
				INSBDeliveryaddress deliveryaddress = new INSBDeliveryaddress();
				deliveryaddress.setOperator(insbAgent.getName());
				deliveryaddress.setCreatetime(new Date());
				deliveryaddress.setOwnername(model.getOwerName());
				deliveryaddress.setRecipientname(model.getOwerName());
				deliveryaddress.setRecipientprovince(model.getProvinceCode());
				deliveryaddress.setRecipientcity(model.getCityCode());
				deliveryaddress.setRecipientaddress(model.getAddress());
				insbDeliveryaddressDao.insert(deliveryaddress);
				/*try {
					KafkaClientHelper.collect("insbDeliveryaddress",JSONObject.fromObject(deliveryaddress).toString());
				} catch (Exception e) {
					e.printStackTrace();
				}*/
				LogUtil.info("INSBDeliveryaddress|报表数据埋点|"+JSONObject.fromObject(deliveryaddress).toString());
			}

			// 保存车主信息
			INSBPerson insbPerson = new INSBPerson();
			insbPerson.setOperator(insbAgent.getName());
			insbPerson.setCreatetime(new Date());
			insbPerson.setTaskid(taskid);
			insbPerson.setName(model.getOwerName());
			// 1-女,0-男
			insbPerson.setGender(1);
			insbPersonDao.insert(insbPerson);
			/*try {
				KafkaClientHelper.collect("insbPerson",JSONObject.fromObject(insbPerson).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			LogUtil.info("INSBPerson|报表数据埋点|"+JSONObject.fromObject(insbPerson).toString());
			INSBCarowneinfo carowneinfo = new INSBCarowneinfo();
			carowneinfo.setOperator(insbAgent.getName());
			carowneinfo.setCreatetime(new Date());
			carowneinfo.setTaskid(taskid);
			carowneinfo.setPersonid(insbPerson.getId());
			insbCarowneinfoDao.insert(carowneinfo);
			/*try {
				KafkaClientHelper.collect("insbCarowneinfo",JSONObject.fromObject(carowneinfo).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			LogUtil.info("INSBCarowneinfo|报表数据埋点|"+JSONObject.fromObject(carowneinfo).toString());

			// 向车辆信息表插入车辆信息
			INSBCarinfo insbCarinfo = new INSBCarinfo();
			insbCarinfo.setCreatetime(new Date());
			insbCarinfo.setOperator(insbAgent.getName());
			insbCarinfo.setTaskid(taskid);
			insbCarinfo.setOwner(insbPerson.getId());
			insbCarinfo.setOwnername(model.getOwerName());
			if(StringUtil.isEmpty(model.getPlateNumber())){
				insbCarinfo.setCarlicenseno("新车未上牌");
				insbCarinfo.setIsNew("1");
			}else{
				insbCarinfo.setCarlicenseno(model.getPlateNumber());
				insbCarinfo.setIsNew("0");
			}
			insbCarinfoDao.insert(insbCarinfo);
			LogUtil.info("INSBCarinfo|报表数据埋点|"+JSONObject.fromObject(insbCarinfo).toString());
			/*try {
				KafkaClientHelper.collect("insbCarinfo",JSONObject.fromObject(insbCarinfo).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			LogUtil.info("INSBCarinfo|报表数据埋点|"+JSONObject.fromObject(insbCarinfo).toString());

			// 报价信息总表插入投保地区
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setOperator(insbAgent.getName());
			insbQuotetotalinfo.setCreatetime(new Date());
			insbQuotetotalinfo.setAgentname(insbAgent.getName());
			insbQuotetotalinfo.setAgentnum(insbAgent.getJobnum());
			insbQuotetotalinfo.setInsprovincecode(model.getProvinceCode());
			insbQuotetotalinfo.setInsprovincename(model.getProvinceName());
			insbQuotetotalinfo.setInscitycode(model.getCityCode());
			insbQuotetotalinfo.setInscityname(model.getCityName());
			insbQuotetotalinfo.setTaskid(taskid);
			//默认没有保存投保书
			insbQuotetotalinfo.setInsurancebooks("0");
			//是否续保 0 不是 1是
			if("0".equals(flag)){
				insbQuotetotalinfo.setIsrenewal("0");
			}else{
				insbQuotetotalinfo.setIsrenewal("1");
			}
			//cm后台，代客录单，保存业管id
			if(!StringUtil.isEmpty(model.getUserid())){
				insbQuotetotalinfo.setUserid(model.getUserid());
			}
			//LZG 通过懒掌柜购买的，保存购买人的id
//			if(!StringUtil.isEmpty(model.getPurchaserid())){
//			}
			//LZG 通过懒掌柜购买的，保存分享人的id
			if(!StringUtil.isEmpty(model.getPurchaserid())){
				insbQuotetotalinfo.setLzgshareid(model.getLzgshareid());
				insbQuotetotalinfo.setPurchaserid("1");
			}
			if(!StringUtil.isEmpty(model.getDatasourcesfrom())){
				insbQuotetotalinfo.setDatasourcesfrom(model.getDatasourcesfrom());
			}

			//http://pm.baoxian.in/zentao/task-view-1106.html
			//增加掌中保出单的渠道标识
			if (StringUtil.isNotEmpty(insbAgent.getChannelid())) {
				insbQuotetotalinfo.setSourceFrom("zzb");
				INSBChannel channel = insbChannelService.queryById(insbAgent.getChannelid());
				insbQuotetotalinfo.setPurchaserChannel(channel == null ? "": channel.getChannelinnercode());
				insbQuotetotalinfo.setPurchaserid(model.getChanneluserid());//添加渠道USERID
			}

			insbQuotetotalinfoDao.insert(insbQuotetotalinfo);
			LogUtil.info("INSBQuotetotalinfo|报表数据埋点|"+JSONObject.fromObject(insbQuotetotalinfo).toString());
			/*try {
				KafkaClientHelper.collect("insbQuotetotalinfo",JSONObject.fromObject(insbQuotetotalinfo).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			LogUtil.info("INSBQuotetotalinfo|报表数据埋点|"+JSONObject.fromObject(insbQuotetotalinfo).toString());
			// 保存懒掌柜订单信息
			if(!StringUtil.isEmpty(model.getRequirementid())){
				LogUtil.info("\r==保存insborderlister表数据-->> taskid=="+taskid);
				lzgOrderService.addorderListerpust(taskid,model.getRequirementid());
			}
			// 保存保单表信息
			INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
			insbPolicyitem.setOperator(insbAgent.getName());
			insbPolicyitem.setCreatetime(new Date());
			insbPolicyitem.setTaskid(taskid);
			insbPolicyitem.setCarownerid(carowneinfo.getId());
			insbPolicyitem.setCarownername(model.getOwerName());
			insbPolicyitem.setAgentname(insbAgent.getName());
			insbPolicyitem.setCarinfoid(insbCarinfo.getId());
			// 默认商业险
			insbPolicyitem.setRisktype("0");
			insbPolicyitem.setAgentnum(insbAgent.getJobnum());
			//未生效
			insbPolicyitem.setPolicystatus("5");
			insbPolicyitemDao.insert(insbPolicyitem);
			/*try {
				KafkaClientHelper.collect("insbPolicyitem",JSONObject.fromObject(insbPolicyitem).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			LogUtil.info("INSBPolicyitem|报表数据埋点|"+JSONObject.fromObject(insbPolicyitem).toString());
			Map<String, Object> body = new HashMap<String, Object>();

			//无车牌，投保人，被保人，默认车主一样
			//saveAllPersonInfo(taskid);
			body.put("processinstanceid", taskid);

			//出险信息表插入数据，防止后期生成两条


			//记录普通平台查询，规则平台查询发起时间，返回时间
			INSBLastyearinsurestatus lastyearinsurestatus = new INSBLastyearinsurestatus();
			lastyearinsurestatus.setOperator("平台查询");
			lastyearinsurestatus.setCreatetime(new Date());
			lastyearinsurestatus.setModifytime(new Date());
			lastyearinsurestatus.setTaskid(taskid);
			insbLastyearinsurestatusDao.insert(lastyearinsurestatus);
			/*try {
				KafkaClientHelper.collect("insbLastyearinsurestatus",JSONObject.fromObject(lastyearinsurestatus).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			LogUtil.info("INSBLastyearinsurestatus|报表数据埋点|"+JSONObject.fromObject(lastyearinsurestatus).toString());

			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(body);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
			commonModel.setBody(null);
		}
		return commonModel;
	}
	//saveDefaultPeopleInfo
	//不存在全默认车主 2016-03-29
	public void saveAllPersonInfo(String taskid){
		INSBCarowneinfo insbCarowneinfo = new INSBCarowneinfo();
		insbCarowneinfo.setTaskid(taskid);
		INSBCarowneinfo carowneinfo = insbCarowneinfoDao.selectOne(insbCarowneinfo);
		INSBPerson carperson = null;
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskid);
		List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(insbPolicyitem);
		if(null != carowneinfo){
			String operator = carowneinfo.getOperator();
			carperson = insbPersonDao.selectById(carowneinfo.getPersonid());
			if(null != carperson){
				//默认投保人被保人，都是车主信息
				saveDefaultPeopleInfo(carperson, taskid, operator);
				INSBInsured insured = new INSBInsured();
				insured.setTaskid(taskid);
				INSBInsured insbInsured2 = insbInsuredDao.selectOne(insured);
				INSBApplicant applicant = new INSBApplicant();
				applicant.setTaskid(taskid);
				INSBApplicant insbApplicant2 = insbApplicantDao.selectOne(applicant);
				for(INSBPolicyitem policyitem : insbPolicyitems){
					if(null != insbApplicant2){
						policyitem.setApplicantid(insbApplicant2.getId());
					}
					policyitem.setApplicantname(carperson.getName());
					if(null != insbInsured2){
						policyitem.setInsuredid(insbInsured2.getId());
					}
					policyitem.setInsuredname(carperson.getName());
					policyitem.setCarownerid(carowneinfo.getId());
					policyitem.setCarownername(carperson.getName());
					policyitem.setStartdate(ModelUtil.nowDateAddOneDay(new Date()));
					policyitem.setEnddate(ModelUtil.nowDateAddOneYear(new Date(),1));
					insbPolicyitemDao.updateById(policyitem);
				}
			}
		}
	}

	@Override
	public CommonModel recommendProvider(SearchProviderModel model) {
		CommonModel  commom= searchProvider(model);
		@SuppressWarnings("unchecked")
		List<ProviderListBean> providerListBeans  = (List<ProviderListBean>)commom.getBody();
		List<ProviderListBean> providerListBeans2 = new  ArrayList<ProviderListBean>();
		for (ProviderListBean providerListBean : providerListBeans) {
			INSBElfconf insbElfconf = appInsuredQuoteDao.selectOneElfconf(providerListBean.getProId());
			if(insbElfconf!=null){
				providerListBeans2.add(providerListBean);
			}
		}
		commom.setBody(providerListBeans2);
		return commom;
	}

	@Override
	public CommonModel recommendCarModel() {
		CommonModel commonModel = new CommonModel();
		Set<String> resultSet = redisClient.zrevrange(Constants.CM_ZZB, "hot_key_list", 5);
		if(resultSet==null){
			commonModel.setStatus("fail");
			commonModel.setMessage("暂无车型信息");
			return commonModel;
		}else{
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(getCarInfo(CloudQueryUtil.getCarInfo(null,resultSet.iterator().next(),"5","1")));
		}
		return commonModel;
	}
	@Override
	public CommonModel searchCarModel(String modelName,String pageSize,String currentPage) {
		CommonModel commonModel = new CommonModel();
		if(modelName==null || "".equals(modelName)){
			commonModel.setStatus("fail");
			commonModel.setMessage("参数不能为空");
			return commonModel;
		}
		if(pageSize==null || "".equals(pageSize)){
			pageSize = "10";
		}
		if(currentPage==null || "".equals(currentPage)){
			currentPage = "1";
		}

		String result = "";
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("flag", "2");
			params.put("vehicleName", modelName);
			params.put("pageSize", pageSize);
			params.put("pageNumber", currentPage);
			result = CloudQueryUtil.jingYouCarModelSearch(params);
			LogUtil.info(modelName + " jingyoucarmodelsearch==:" + result);

			if(StringUtil.isEmpty(result)){
				commonModel.setStatus("fail");
				commonModel.setMessage("没有数据返回，请查看参数是否有误");
				return commonModel;
			}
			int totalCount = getJYCarModelTotalCount(result);
			List<SelectCarModel> list = getJYCarInfo(result);
			if(null != list && list.size() > 0){
				commonModel.setStatus("success");
				commonModel.setMessage(totalCount+"");
				commonModel.setBody(list);
			}else{
				commonModel.setStatus("fail");
				commonModel.setMessage("没查到该车型信息");
				return commonModel;
			}
		} catch (IOException e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}

		return commonModel;
	}

	@Override
	public CommonModel searchCarModelVin(String modelName,String pageSize,String currentPage, String carlicenseno, String agentid) {
		CommonModel commonModel = new CommonModel();
		if(modelName==null || "".equals(modelName)){
			commonModel.setStatus("fail");
			commonModel.setMessage("参数不能为空");
			return commonModel;
		}
		if(pageSize==null || "".equals(pageSize)){
			pageSize = "10";
		}
		if(currentPage==null || "".equals(currentPage)){
			currentPage = "1";
		}
		String result = "";
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("flag", "2");
			params.put("vehicleName", modelName);
			params.put("pageSize", pageSize);
			params.put("pageNumber", currentPage);
			if(!StringUtil.isEmpty(carlicenseno) && !"新车未上牌".equals(carlicenseno)){
				params.put("vehicleno", carlicenseno);
			}

			result = CloudQueryUtil.jingYouCarModelSearchVin(params);
			LogUtil.info(modelName + " " + carlicenseno + " jingyoucarmodelsearch==:" + result);

			if(StringUtil.isEmpty(result)){
				commonModel.setStatus("fail");
				commonModel.setMessage("没有数据返回，请查看参数是否有误");
				return commonModel;
			}
			int totalCount = getJYCarModelTotalCount(result);
			List<SelectCarModel> list = getJYCarInfo(result);
			if(null != list && list.size() > 0){
				commonModel.setStatus("success");
				commonModel.setMessage(totalCount+"");
				commonModel.setBody(list);
			}else{
				commonModel.setStatus("fail");
				commonModel.setMessage("没查到该车型信息");
				return commonModel;
			}
		} catch (IOException e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}

		return commonModel;
	}

	private List<SelectCarModel> getJYCarInfo(String result) {
		List<SelectCarModel> list = new ArrayList<SelectCarModel>();
		JSONObject jsonObject = JSONObject.fromObject(result);
		JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("List"));
		for(int i = 0;i < jsonArray.size(); i ++){
			JSONObject object = JSONObject.fromObject(jsonArray.get(i));
			CarModel carModelInfoBean = (CarModel) JSONObject.toBean(object, CarModel.class);
			SelectCarModel selectCarModel = new SelectCarModel();
			selectCarModel.setAliasname("");
			selectCarModel.setAnalogyprice(carModelInfoBean.getAnalogyprice());
			selectCarModel.setAnalogytaxprice(carModelInfoBean.getAnalogytaxprice());
			selectCarModel.setBrandname(carModelInfoBean.getBrandname());
			selectCarModel.setCarVehicleOrigin(getCarSource(carModelInfoBean.getManufacturer()));
			selectCarModel.setDisplacement(convertToDouble(carModelInfoBean.getDisplacement()));
			selectCarModel.setFamilyname(carModelInfoBean.getFamilyname());
			selectCarModel.setFullweight(convertToDouble(carModelInfoBean.getFullweight()));
			selectCarModel.setGearbox(carModelInfoBean.getGearbox());
			selectCarModel.setJgVehicleType(carModelInfoBean.getVehicletype());
			selectCarModel.setListedyear(carModelInfoBean.getMaketdate());
			selectCarModel.setLoads(convertToDouble(carModelInfoBean.getModelLoads()));
			selectCarModel.setPrice(carModelInfoBean.getPrice());
			selectCarModel.setSeat(convertToInteger(carModelInfoBean.getSeat()));
			selectCarModel.setStandardname(carModelInfoBean.getVehiclename());
			selectCarModel.setTaxPrice(carModelInfoBean.getTaxprice());
			selectCarModel.setVehicleid(carModelInfoBean.getVehicleId());
			selectCarModel.setSyvehicletypename(carModelInfoBean.getSyvehicletypename());//商业险车辆名称
			selectCarModel.setSyvehicletypecode(carModelInfoBean.getSyvehicletype());//机动车编码
			list.add(selectCarModel);
			if(StringUtil.isEmpty(carModelInfoBean.getVehicleId())){
				System.out.println(new Date() + " ===出错啦====vehicleidnull");
			}
			System.out.println("hejie20160226add:" + carModelInfoBean.getVehicleId());
		}
		return list;
	}

	//精友接口映射
	private String getCarSource(String key){
		Map<String, String> map = new HashMap<String, String>();
		map.put("0", "国产");
		map.put("1", "合资");
		map.put("2", "进口");
		return StringUtil.isEmpty(key)||StringUtil.isEmpty(map.get(key))?"":map.get(key);
	}

	private int getJYCarModelTotalCount(String result) {
		JSONObject jsonObject = JSONObject.fromObject(result);
		return jsonObject.containsKey("RecTotal")?jsonObject.getInt("RecTotal"):0;
	}


	public List<SelectCarModel> getCarInfo(String result){
		List<SelectCarModel> list = new ArrayList<SelectCarModel>();
		JSONObject jsonObject = JSONObject.fromObject(result);
		JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("carModelList"));
		for(int i = 0;i < jsonArray.size(); i ++){
			JSONObject object = JSONObject.fromObject(jsonArray.get(i));
			CarModelInfoBean carModelInfoBean = (CarModelInfoBean) JSONObject.toBean(object, CarModelInfoBean.class);
			SelectCarModel selectCarModel = new SelectCarModel();
			selectCarModel.setAliasname(carModelInfoBean.getAliasName());
			selectCarModel.setAnalogyprice(carModelInfoBean.getAnalogyPrice());
			selectCarModel.setAnalogytaxprice(carModelInfoBean.getAnalogyTaxPrice());
			selectCarModel.setBrandname(carModelInfoBean.getBrandName());
			selectCarModel.setCarVehicleOrigin(getCarSource(carModelInfoBean.getVehicleSource()));
			selectCarModel.setDisplacement(carModelInfoBean.getDisplacement());
			selectCarModel.setFamilyname(carModelInfoBean.getFamilyName());
			selectCarModel.setFullweight(carModelInfoBean.getFullWeight());
			selectCarModel.setGearbox(carModelInfoBean.getGearBox());
			selectCarModel.setJgVehicleType(carModelInfoBean.getVehicleType());
			selectCarModel.setListedyear(carModelInfoBean.getMarkTime());
			selectCarModel.setLoads(carModelInfoBean.getLoads());
			selectCarModel.setPrice(carModelInfoBean.getPrice());
			selectCarModel.setSeat(carModelInfoBean.getSeat());
			selectCarModel.setStandardname(carModelInfoBean.getStandardName());
			selectCarModel.setTaxPrice(carModelInfoBean.getTaxPrice());
			list.add(selectCarModel);
		}
		return list;
	}

	@Override
	public CommonModel updateCarModelAndCarinfo(CarModelInfoModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if (null == taskid || "".equals(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			// 根据实例id查询车辆信息表，获取操作员信息
			INSBCarinfo insbCarinfo = new INSBCarinfo();
			insbCarinfo.setTaskid(taskid);
			INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
			if (null == carinfo) {
				commonModel.setStatus("fail");
				commonModel.setMessage("车辆信息不存在");
				return commonModel;
			}
			// 更新车辆信息表数据
			carinfo.setModifytime(new Date());
			carinfo.setStandardfullname(model.getModelCode());
			//所属性质 0:个人用车, 1:企业用车,2:机关团体用车
			carinfo.setProperty(model.getInstitutionType());
			//车辆性质,使用性质
			carinfo.setCarproperty(model.getUseProperty());
			insbCarinfoDao.updateById(carinfo);
			LogUtil.info("INSBCarinfo|报表数据埋点|"+JSONObject.fromObject(carinfo).toString());
            LogUtil.info("carModelInfo:"+taskid+",核定载质量:"+model.getTonnage()+"==所有入参:"+model.toString()+",操作人:"+carinfo.getOperator());
			// 向车型信息表插入车型信息
			INSBCarmodelinfo carmodelinfo = new INSBCarmodelinfo();
			carmodelinfo.setCarinfoid(carinfo.getId());
			INSBCarmodelinfo modelinfo = insbCarmodelinfoDao.selectOne(carmodelinfo);
			if(null == modelinfo){
				INSBCarmodelinfo insbCarmodelinfo = new INSBCarmodelinfo();
				insbCarmodelinfo.setOperator(carinfo.getOperator());
				insbCarmodelinfo.setCreatetime(new Date());
				insbCarmodelinfo.setCarinfoid(carinfo.getId());
				insbCarmodelinfo.setStandardname(model.getModelCode());
				insbCarmodelinfo.setStandardfullname(model.getModelCode());
				insbCarmodelinfo.setDisplacement(model.getDisplacement());
				insbCarmodelinfo.setSeat(model.getApprovedLoad());
				insbCarmodelinfo.setCarprice(model.getRulePriceProvideType());
				insbCarmodelinfo.setLoads(model.getTonnage());
				insbCarmodelinfo.setUnwrtweight(model.getTonnage());
				insbCarmodelinfo.setFullweight(model.getWholeWeight());
				insbCarmodelinfo.setPolicycarprice(model.getCustomPrice());
				// 不能为空的字段
				insbCarmodelinfo.setPrice(0.0);
				insbCarmodelinfo.setTaxprice(0.0);
				insbCarmodelinfo.setAnalogyprice(0.0);
				insbCarmodelinfo.setAnalogytaxprice(0.0);
				//标准车型
				insbCarmodelinfo.setIsstandardcar("0");
				//查找问题
				//insbCarmodelinfo.setNoti("手动录入的车型信息"+new Date());
				insbCarmodelinfoDao.insert(insbCarmodelinfo);
                LogUtil.info("=====手动录入车型信息======taskid:"+taskid+" carmodelid:"+insbCarmodelinfo.getId()+" Vehicleid:"+insbCarmodelinfo.getVehicleid());
			}else{
				modelinfo.setOperator(carinfo.getOperator());
				modelinfo.setModifytime(new Date());
				modelinfo.setCarinfoid(carinfo.getId());
				modelinfo.setStandardname(model.getModelCode());
				modelinfo.setStandardfullname(model.getModelCode());
				modelinfo.setDisplacement(model.getDisplacement());
				modelinfo.setSeat(model.getApprovedLoad());
				modelinfo.setCarprice(model.getRulePriceProvideType());
				modelinfo.setLoads(model.getTonnage());
				modelinfo.setUnwrtweight(model.getTonnage());
				modelinfo.setFullweight(model.getWholeWeight());
				modelinfo.setPolicycarprice(model.getCustomPrice());
				//标准车型
				modelinfo.setIsstandardcar("0");
				//查找问题
				//modelinfo.setNoti("手动录入的车型信息"+new Date());
				insbCarmodelinfoDao.updateById(modelinfo);
                LogUtil.info("=====车型信息更新======taskid:"+taskid+" carmodelid:"+modelinfo.getId()+" Vehicleid:"+modelinfo.getVehicleid());
			}
			//保存投保书标志位
			saveInsuranceBooksMark(taskid,model.getWebpagekey());

			/*20170619 hwc 去除旧版懒掌柜相关代码
			INSBOrderlistenpush order = new INSBOrderlistenpush();
			Map<String, String> param = new HashMap<String, String>();
			param.put("taskid", taskid);
			param.put("taskcode", "1");
			param.put("operationtype", "1");
			order = insbOrderlistenpushDao.queryOrderListen(param);//order by createtime
			*/
			model.getProcessinstanceid();
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			String agentnum = "";
			if(quotetotalinfo!=null){
				agentnum = quotetotalinfo.getAgentnum();
			}
			/*20170619 hwc 去除旧版懒掌柜相关代码
			 if(order!=null){
				lzgOrderService.addOrderData(order, "",agentnum);
			}*/
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel saveCarInfo(SaveCarInfoModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if (null == taskid && "".equals(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			// 根据实例id查询车辆信息表
			INSBCarinfo insbCarinfo = new INSBCarinfo();
			insbCarinfo.setTaskid(taskid);
			INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
			if (null == carinfo) {
				commonModel.setStatus("fail");
				commonModel.setMessage("车辆信息不存在");
				return commonModel;
			}
            LogUtil.info("saveCarInfo:"+taskid+"==所有入参:"+model.toString()+",操作人:"+carinfo.getOperator());
			// 更新车辆信息表数据
			carinfo.setModifytime(new Date());
			carinfo.setStandardfullname(model.getModelCode());
			//处理发动机号 没有*直接保存，有*的用数据库中的隐藏，和前端传入的不想同时保存
			if(StringUtil.isEmpty(carinfo.getEngineno())){
				carinfo.setEngineno(model.getEngineNo());
			}else{
				if(!model.getEngineNo().contains("*")){
					carinfo.setEngineno(model.getEngineNo());
				}else{
					if(!model.getEngineNo().equals(ModelUtil.hiddenEngineNo(carinfo.getEngineno()))){
						carinfo.setEngineno(model.getEngineNo());
					}
				}
			}
			if(StringUtil.isEmpty(carinfo.getVincode())){
				carinfo.setVincode(model.getVin());
			}else{
				if(!model.getVin().contains("*")){
					carinfo.setVincode(model.getVin());
				}else{
					if(!model.getVin().equals(ModelUtil.hiddenVin(carinfo.getVincode()))){
						carinfo.setVincode(model.getVin());
					}
				}
			}
			carinfo.setIsTransfercar(model.getChgOwnerFlag());
			if ("1".equals(model.getChgOwnerFlag()) && !StringUtil.isEmpty(model.getChgOwnerDate())) {
				carinfo.setTransferdate(ModelUtil.conbertStringToNyrDate(model.getChgOwnerDate()));
			}else{
				carinfo.setIsTransfercar("0");
			}
			if(!StringUtil.isEmpty(model.getFirstRegisterDate())){
				carinfo.setRegistdate(ModelUtil.conbertStringToNyrDate(model.getFirstRegisterDate()));
				//新车 1 新车未上牌 2 处登记日期少于九个月 270天
				//isnew 1 新车
				if(!"新车未上牌".equals(carinfo.getCarlicenseno())){
					if(ModelUtil.daysBetween(ModelUtil.conbertStringToNyrDate(model.getFirstRegisterDate()), new Date()) < 270){
						carinfo.setIsNew("1");
					}else{
						carinfo.setIsNew("0");
					}
				}
			}
			//行驶证图片路径
			carinfo.setDrivinglicenseurl(model.getDrivinglicense());
			insbCarinfoDao.updateById(carinfo);
			LogUtil.info("INSBCarinfo|报表数据埋点|"+JSONObject.fromObject(carinfo).toString());
			//保存投保书标志位
			saveInsuranceBooksMark(taskid, model.getWebpagekey());
			String agentnum = "";
			//保存行驶证正面照
			if(!StringUtil.isEmpty(model.getFileid())){
				saveFileImageId(taskid,model.getFileid());
			}

            // 先从redis里面取值，如果存在，大数据查询没有超时，或者失败失败
            LastYearPolicyInfoBean redisYearPolicyInfoBean = redisClient.get(Constants.TASK, taskid, LastYearPolicyInfoBean.class);
            if(null == redisYearPolicyInfoBean){
                LogUtil.info("saveCarInfo"+taskid+"=redis中没有大数据对象，初始化人员信息="+carinfo.getCarlicenseno());
                saveAllPersonInfo(taskid);
            }

            LogUtil.info(taskid + "录入车辆信息调用cif开始==车牌号:" + carinfo.getCarlicenseno() + ",是否是新车" + carinfo.getIsNew());
			//根据车辆信息，调用平台查询，获取上年保单信息--出险信息  update 2016-03-17
			if(!StringUtil.isEmpty(carinfo.getCarlicenseno()) && !"新车未上牌".equals(carinfo.getCarlicenseno()) && "0".equals(carinfo.getIsNew())){
				//获取报价区域
				INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
				insbQuotetotalinfo.setTaskid(taskid);
				INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
				if(null == quotetotalinfo){
					commonModel.setStatus("fail");
					commonModel.setMessage("报价总表信息不存在");
					return commonModel;
				}
				String areaId = quotetotalinfo.getInscitycode();
				agentnum = quotetotalinfo.getAgentnum();
				//根据车牌平台查询
				LastYearPolicyInfoBean lastYearPolicyInfoBean = null;
				//权限包
				Map<String,Object> permissionMap = appPermissionService.checkPermission("", taskid, "query");
				Map<String, Object> body = new HashMap<String, Object>();
				commonModel.setBody(body);
				if ((int)permissionMap.get("status") == 0) {
					lastYearPolicyInfoBean = queryLastInsuredInfoByCarinfo(taskid,carinfo,areaId,quotetotalinfo.getAgentnum(),quotetotalinfo.getInsprovincecode());
				}
				if ((int)permissionMap.get("status") == 2 || (int)permissionMap.get("status") == -1) {
					body.put("permissionStatus", CommonModel.STATUS_FAIL);
					body.put("permissionMessage", (String) permissionMap.get("message"));
				}

				//平台查询,返回4，代理人修改了车辆信息，重新发起平台查询，insblastyearinsureinfo删掉现有数据，保存新的数据
				if(null != lastYearPolicyInfoBean){
					LogUtil.info("车辆信息平台查询返回"+taskid+"车辆信息有改动状态="+lastYearPolicyInfoBean.getStatus()+"当前时间="+new Date());
					if("4".equals(lastYearPolicyInfoBean.getStatus())){
						//backlastyearinsureinfo(taskid, quotetotalinfo.getOperator());
					}else{
						//返回值放入ridis,key值 实例id
						cachelastYearPolicyInfo(taskid, lastYearPolicyInfoBean);
					}
				}
				//记录平台查询开始时间
				INSBLastyearinsurestatus insbLastyearinsurestatus = new INSBLastyearinsurestatus();
				insbLastyearinsurestatus.setTaskid(taskid);
				INSBLastyearinsurestatus lastyearinsurestatus = insbLastyearinsurestatusDao.selectOne(insbLastyearinsurestatus);
				if(null != lastyearinsurestatus){
					lastyearinsurestatus.setOperator("平台查询");
					lastyearinsurestatus.setModifytime(new Date());
					lastyearinsurestatus.setCifstarttime(new Date());
					lastyearinsurestatus.setCifflag("0");//默认未超时
					lastyearinsurestatus.setTaskid(taskid);
					insbLastyearinsurestatusDao.updateById(lastyearinsurestatus);
				}
			}

			//bug4492 新车有牌普通平台返回置为无效
			if(!"新车未上牌".equals(carinfo.getCarlicenseno()) && "1".equals(carinfo.getIsNew())){
				//backlastyearinsureinfo(taskid, "新车有牌");
			}

			/* 20170619 hwc 去除旧版懒掌柜相关代码
			INSBOrderlistenpush order = new INSBOrderlistenpush();
			Map<String, String> param = new HashMap<String, String>();
			param.put("taskid", taskid);
			param.put("taskcode", "1");
			param.put("operationtype", "1");
			order = insbOrderlistenpushDao.queryOrderListen(param);//order by createtime
			if(order!=null){
				lzgOrderService.addOrderData(order, "",agentnum);
			}*/
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	private void cachelastYearPolicyInfo(String taskid, LastYearPolicyInfoBean bean) {
		if (bean == null || (bean.getCarowner() == null && bean.getLastYearCarinfoBean() == null &&
				bean.getLastYearSupplierBean() == null && bean.getLastYearPolicyBean() == null &&
				bean.getLastYearClaimBean() == null && (bean.getLastYearRiskinfos() == null || bean.getLastYearRiskinfos().isEmpty()) &&
				(bean.getCarModels() == null || bean.getCarModels().isEmpty()))) {
			return;
		}

		redisClient.set(Constants.TASK, taskid, JSONObject.fromObject(bean).toString(), OVER_TIME_SECONDS);
	}

	private void saveInsuranceBooksMark(String taskid,String webpagekey){
		webpagekey = StringUtil.isEmpty(webpagekey)?"0":webpagekey;
		INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
		insbQuotetotalinfo.setTaskid(taskid);
		INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
		if(null != quotetotalinfo){
			quotetotalinfo.setInsurancebooks("1");
			quotetotalinfo.setWebpagekey(webpagekey);
			insbQuotetotalinfoDao.updateById(quotetotalinfo);
			LogUtil.info("INSBQuotetotalinfo|报表数据埋点|"+JSONObject.fromObject(quotetotalinfo).toString());
		}
	}
	/**
	 *
	 * @param taskid 任务号
	 * @param insbcarinfo 车辆信息
	 * @param areaId 市编码
	 * @param jobnum 代理人工号
	 * @param provinceCode 省编码
	 * @return
	 */
	private LastYearPolicyInfoBean queryLastInsuredInfoByCarinfo(String taskid,INSBCarinfo insbcarinfo,String areaId,String jobnum,String provinceCode){
		JSONObject object = new JSONObject();
		object.put("flag", "XB");
		//quickflag  0 正常投保 1快速续保
		object.put("quickflag", "0");
		//issavecarinfo  0 保存  1 不保存
		object.put("issavecarinfo", "1");
		JSONObject inParas = new JSONObject();
		inParas.put("car.specific.license", insbcarinfo.getCarlicenseno());
		object.put("inParas", inParas);
		JSONObject carinfo = new JSONObject();
		String vehiclename = "";
		//获取车型名称
		INSBCarmodelinfo carmodelinfo = new INSBCarmodelinfo();
		carmodelinfo.setCarinfoid(insbcarinfo.getId());
		INSBCarmodelinfo insbCarmodelinfo = insbCarmodelinfoDao.selectOne(carmodelinfo);
		if(null != insbCarmodelinfo){
			vehiclename = insbCarmodelinfo.getStandardfullname();
		}
		carinfo.put("engineno", insbcarinfo.getEngineno());//发动机号
		carinfo.put("vehicleframeno", insbcarinfo.getVincode());//车架号
		carinfo.put("registerdate", DateUtil.toString(insbcarinfo.getRegistdate()));//初登日期
		carinfo.put("vehiclename", vehiclename);//品牌型号
		carinfo.put("chgownerflag", insbcarinfo.getIsTransfercar());//是否过户车  0不 是  1 是
		carinfo.put("usingnaturecode", cifUsePropsFrommap(insbcarinfo.getCarproperty()));//使用性质编码
		carinfo.put("vehicletype", getProperty(insbcarinfo.getProperty()));//车辆性质
		object.put("carinfo", carinfo);
		object.put("areaId", areaId);
		object.put("provinceCode", provinceCode);
		object.put("eid", taskid);
		//获取代理人所属机构id
		if(!StringUtil.isEmpty(jobnum)){
			INSBAgent insbAgent = insbAgentDao.selectByJobnum(jobnum);
			object.put("singlesite", insbAgent.getDeptid());

			if (StringUtil.isNotEmpty(insbAgent.getDeptid())) {
				object.put("platCode", inscDeptService.getPingTaiDeptId(insbAgent.getDeptid()));
			}
		}

		LastYearPolicyInfoBean lastYearPolicyInfoBean = null;
		try {
            LogUtil.info(taskid + "以车辆信息查询cif: "+object.toString());

			String resultJson = CloudQueryUtil.getLastYearInsurePolicy(object.toString());
            LogUtil.info(taskid + "以车辆信息查询cif返回: " + resultJson);

			JSONObject jsonObject=JSONObject.fromObject(resultJson);
			lastYearPolicyInfoBean = (LastYearPolicyInfoBean) JSONObject.toBean(jsonObject, LastYearPolicyInfoBean.class);
		} catch (Exception e) {
			lastYearPolicyInfoBean = null;
			e.printStackTrace();
		}
		return lastYearPolicyInfoBean;
	}

	/**
	 * 车辆性质与平台映射
	 * @param key
	 * @return
	 */
	public String cifUsePropsFrommap(String key){
		Map<String, String> mapping = new HashMap<String, String>();
		mapping.put("16", "701");
		mapping.put("15", "801");
		mapping.put("12", "240");
		mapping.put("11", "230");
		mapping.put("10", "220");
		mapping.put("1", "1");
		mapping.put("2", "101");
		mapping.put("3", "102");
		mapping.put("4", "103");
		mapping.put("6", "104");
		return null == mapping.get(key)||"".equals(mapping.get(key))?"1":mapping.get(key);
	}


	private String getProperty(String key){
		Map<String, String> map = new HashMap<String, String>();
		map.put("0", "个人用车");
		map.put("1", "企业用车");
		map.put("2", "机关团体");
		return StringUtil.isEmpty(key)? "" : map.get(key);
	}
	
	/**
	 * 获取供应商之前，校验数据是否完整
	 * @param taskID 任务号
	 */
	@Override
	public ExtendCommonModel inspectInfo(String taskID) {
		ExtendCommonModel commonModel = new ExtendCommonModel();
		if(StringUtil.isEmpty(taskID)){
			commonModel.setStatus(CommonModel.STATUS_FAIL);
			commonModel.setMessage("实例id不能为空");
			return commonModel;
		}
		boolean flag=true;
		List<Map<Object, Object>> list = insbApplicantDao.getCheckData(taskID);
		for (Map<Object, Object> m : list) {
		    for (Object k : m.keySet()) {
		    	//0表示没有相关记录数，为数据不完整
		        if(m.get(k).toString().equals("0")){
		        	flag=false;
		        	break;
		        }
		    }
		}
		if(flag){
			commonModel.setStatus(CommonModel.STATUS_SUCCESS);
			commonModel.setMessage("校验通过");
		}else{
			commonModel.setStatus(CommonModel.STATUS_CHECK);
			commonModel.setMessage("车辆相关信息丢失，返回上一步重新录入");
//			LastYearPolicyInfoBean redisYearPolicyInfoBean = redisClient.get(Constants.TASK,  taskID, LastYearPolicyInfoBean.class);
//	        if(redisYearPolicyInfoBean!=null && redisYearPolicyInfoBean.getStatus().equals("2")){
	        	redisClient.del(Constants.TASK, taskID);
//	        }
		}
		return commonModel;
	}

	@Override
	public ExtendCommonModel searchProvider(SearchProviderModel model) {
		ExtendCommonModel commonModel = new ExtendCommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if(StringUtil.isEmpty(taskid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			if(StringUtil.isEmpty(model.getAgentid())){
				commonModel.setStatus("fail");
				commonModel.setMessage("代理人id不能为空");
				return commonModel;
			}
			//获取代理人常用出单网点
			INSBAgent agent = insbAgentDao.selectById(model.getAgentid());
			List<SelectProviderBean> selectProviderBeans = null;
			//代理人所属网点，查询该网点关联所有协议和供应商
			//代理人配置了权限包，关联权限包，取交集，没配置按代理人所属机构出单网点
//			if(StringUtil.isEmpty(agent.getSetid())){
//				selectProviderBeans = appInsuredQuoteDao.getAgreementidPidByAgentid(model.getAgentid());
//			}else{
			INSBPermissionset set = insbPermissionallotService.getPermissionsetByAgentId(model.getAgentid());
			if (set == null || StringUtil.isEmpty(set.getId())) {
				commonModel.setStatus("fail");
				commonModel.setMessage("该代理人没有配置权限包");
				return commonModel;
			}
			if ("1200000000".equals(set.getDeptid())) {
				selectProviderBeans = appInsuredQuoteDao.getAgreementidPidByAgentid(model.getAgentid(), set.getId());
			} else {
				selectProviderBeans = appInsuredQuoteDao.getAgreementidPidByAgentidHavesetid(model.getAgentid(), set.getId());
			}
//			}
			if(selectProviderBeans.isEmpty()){
				commonModel.setStatus("fail");
				commonModel.setMessage("该代理人没有配置供应商");
				return commonModel;
			}
			List<ProviderListBean> providerListBeans = getproviderListBeansByAgengid(selectProviderBeans,taskid,agent.getCommonusebranch());
			//获取平台查询状态，手工选择的投保公司名称，查询到的上年投保公司名称
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			Map<String, Object> extend = new HashMap<String, Object>();
			if(quotetotalinfo != null){
				if(StringUtil.isEmpty(quotetotalinfo.getCloudstate())){
					extend.put("cloudstate", null);
				}else{
					extend.put("cloudstate", "1".equals(quotetotalinfo.getCloudstate())?true:false);
				}
				extend.put("handersupplier", quotetotalinfo.getHandersupplier());
				extend.put("lastyearsupplier", quotetotalinfo.getLastyearsupplier());
				Map<String, Object> mapresult = new HashMap<String, Object>();
				mapresult.put("syenddateflag", true);
				mapresult.put("syenddate", "");
				mapresult.put("jqenddateflag", true);
				mapresult.put("jqenddate", "");
				extend.put("insuredatemap", mapresult);
				//extend.put("insuredatemap", lastInsuredDate(taskid)); 820改版
			}
			extend.put("feeinfo", getChangeFee(model.getAgentid(),null));
			extend.put("suppliername", getLastYearProvName(taskid));
			extend.put("syrepeatinsuranceinfo", "");
			extend.put("jqrepeatinsuranceinfo","");
			commonModel.setExtend(extend);
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(providerListBeans);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	/**
	 *
	 * @param taskid
	 * @return
	 */
	private String getLastYearProvName(String taskid){
		String lastproname = "";
		//从redis里面取值，取到直接返回
		LastYearPolicyInfoBean yearPolicyInfoBean = redisClient.get(Constants.TASK, taskid, LastYearPolicyInfoBean.class);
		if(null != yearPolicyInfoBean){
			//云查询結果中去上年投保公司
			LastYearSupplierBean lastYearSupplierBean = yearPolicyInfoBean.getLastYearSupplierBean();
			if(null != lastYearSupplierBean){
				lastproname = lastYearSupplierBean.getSuppliername();
			}
		}
		return lastproname;
	}

	/**
	 * 参考:com.zzb.mobile.service.impl.AppInsuredQuoteServiceImpl#searchProvider(com.zzb.mobile.model.SearchProviderModel)
	 */
	@Override
	public ExtendCommonModel searchProviderForMinizzb(SearchProviderModelForMinizzb model) {
		ExtendCommonModel commonModel = new ExtendCommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if(StringUtil.isEmpty(taskid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			if(StringUtil.isEmpty(model.getAgentid())){
				commonModel.setStatus("fail");
				commonModel.setMessage("出单代理人id不能为空");
				return commonModel;
			}
			if(StringUtil.isEmpty(model.getChannelId())){
				commonModel.setStatus("fail");
				commonModel.setMessage("渠道id不能为空");
				return commonModel;
			}
			if(StringUtil.isEmpty(model.getCity())){
				commonModel.setStatus("fail");
				commonModel.setMessage("出单地区不能为空");
				return commonModel;
			}
//			//获取代理人常用出单网点
			INSBAgent agent = insbAgentDao.selectById(model.getAgentid());
//			List<SelectProviderBean> selectProviderBeans = null;
//			//代理人所属网点，查询该网点关联所有协议和供应商
//			//代理人配置了权限包，关联权限包，取交集，没配置按代理人所属机构出单网点
//			if(StringUtil.isEmpty(agent.getSetid())){
//				selectProviderBeans = appInsuredQuoteDao.getAgreementidPidByAgentid(model.getAgentid());
//			}else{
//				selectProviderBeans = appInsuredQuoteDao.getAgreementidPidByAgentidHavesetid(model.getAgentid());
//			}

			//获取代理人常用出单网点 改为 获取渠道出单代理人所属网点
			//获取供应列表信息（按 渠道来源、投保城市）
			//String channelinnercode = "minizzb";//usersource
			String channelinnercode = model.getChannelId();//usersource
			String city = model.getCity();
			List<SelectProviderBeanForMinizzb> selectProviderBeans = insbChannelService.queryChannelProviderList(city, channelinnercode);

			if(selectProviderBeans.isEmpty()){
				commonModel.setStatus("fail");
				commonModel.setMessage("该代理人没有配置供应商");
				return commonModel;
			}
//			List<ProviderListBean> providerListBeans = getproviderListBeansByAgengid(selectProviderBeans,taskid,agent.getCommonusebranch());
			List<ProviderListBean> providerListBeans = getproviderListBeansByAgengidForMinizzb(selectProviderBeans, taskid, agent);
			//获取平台查询状态，手工选择的投保公司名称，查询到的上年投保公司名称
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			Map<String, Object> extend = new HashMap<String, Object>();
			if(quotetotalinfo != null){
				if(StringUtil.isEmpty(quotetotalinfo.getCloudstate())){
					extend.put("cloudstate", null);
				}else{
					extend.put("cloudstate", "1".equals(quotetotalinfo.getCloudstate())?true:false);
				}
				extend.put("handersupplier", quotetotalinfo.getHandersupplier());
				extend.put("lastyearsupplier", quotetotalinfo.getLastyearsupplier());
				Map<String, Object> mapresult = new HashMap<String, Object>();
				mapresult.put("syenddateflag", true);
				mapresult.put("syenddate", "");
				mapresult.put("jqenddateflag", true);
				mapresult.put("jqenddate", "");
				extend.put("insuredatemap", mapresult);
				//extend.put("insuredatemap", lastInsuredDate(taskid));
			}
			extend.put("feeinfo", getChangeFee(model.getAgentid(), null));
			extend.put("suppliername", getLastYearProName(taskid));
			extend.put("syrepeatinsuranceinfo", "");
			extend.put("jqrepeatinsuranceinfo","");
			commonModel.setExtend(extend);
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(providerListBeans);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}
	//
	private String getLastYearProName(String taskid){
		String suppername = "";
		INSBLastyearinsureinfo lastyearinsureinfo = insbRulequerycarinfoDao.queryLastYearClainInfo(taskid);
		if(null != lastyearinsureinfo){
			suppername = lastyearinsureinfo.getSuppliername();
		}
		return suppername;
	}


	//费改地区map集合
	public Map<String, String> getFeelist(){
		Map<String, String> map = new HashMap<String, String>();
		// 非费改地区去掉 20160601
		//map.put("lnpt", "1221000000");//辽宁平台
		map.put("lnpt", "删除辽宁平台");
		return map;
	}
//	费改返回
	public Map<String, Object> getChangeFee(String agentid,String jobnum){
		Map<String, Object> map = new HashMap<String, Object>();
		String deptid = "";
		INSBAgent agent = new INSBAgent();
		if(agentid!=null){
			agent.setId(agentid);
		}
		if(jobnum!=null){
			agent.setJobnum(jobnum);
		}
		List<INSBAgent> lista = insbAgentDao.selectList(agent);
		if(lista!=null&&lista.size()>0){
			agent = lista.get(0);
			deptid = agent.getDeptid();
			INSCDept dept = inscDeptDao.selectById(deptid);
			if((dept.getParentcodes()+"+"+deptid).contains(getFeelist().get("lnpt"))){
				map.put("isfeeflag", Boolean.TRUE);
				map.put("feenum", "3");
			}else{
				map.put("isfeeflag", Boolean.FALSE);
				map.put("feenum", "1");
			}
		}
		return map;
	}
	/**
	 * true 可以投保
	 * @param taskid
	 * @return
	 */
	private Map<String, Object> lastInsuredDate(String taskid){
		return judgeInsuredDate(90,taskid);
	}

	private List<ProviderListBean> getproviderListBeansByAgengid(List<SelectProviderBean> selectProviderBeans, String taskid,String commonusebranch) {
		List<String> selected = getSelectedProviders(taskid);
		String lastProid = getLastYearProvId(taskid);
		String treeTop = "";
		if(lastProid.length() >= 4){
			treeTop = lastProid.substring(0,4);
		}

		StringBuffer agreements = new StringBuffer();
		for(SelectProviderBean bean : selectProviderBeans){
			if (StringUtil.isEmpty(bean.getAgreementid())) continue;
			agreements.append(bean.getAgreementid() + ",");
		}
		JSONObject adsResult =  AdsUtil.getAgreementAds (agreements.toString());

		List<ProviderListBean>  providerListBeans = new ArrayList<ProviderListBean>();
		List<ProviderListBean>  lastyearRet = new ArrayList<ProviderListBean>();
		List<ProviderListBean>  adsRet = new ArrayList<ProviderListBean>();
		boolean add = false;

		for(SelectProviderBean bean : selectProviderBeans){
			add = false;
			ProviderListBean providerListBean = new ProviderListBean();
			INSBProvider insbProviderParent = insbProviderDao.selectById(bean.getParentid());
			//判断是否是上年投保公司
			if(treeTop.equals(bean.getParentid())){
				providerListBean.setIslastyearpro(true);
				lastyearRet.add(providerListBean);
				add = true;
			}
			providerListBean.setProId(insbProviderParent.getId());
			providerListBean.setPrvcode(insbProviderParent.getPrvcode());
			providerListBean.setPrvname(insbProviderParent.getPrvname());
			providerListBean.setPrvshotname(insbProviderParent.getPrvshotname());
			providerListBean.setLogo(insbProviderParent.getLogo());
			providerListBean.setChanneltype(insbProviderParent.getBusinesstype());
			providerListBean.setAgreementname(bean.getAgreementname());
			if(treeTop.equals(providerListBean.getProId()) || selected.contains(providerListBean.getProId())){
				providerListBean.setIsflag(true);
			}
			//判断投保时间是否重复投保     重复投保状态   0 都重复 1 交强险 商业险 有一个重复或者都不重复
			providerListBean.setRepeatinsured(ifRepeatInsured(taskid,bean.getProvid()));

			String agreementid = bean.getAgreementid();
			if (adsResult != null && StringUtil.isNotEmpty(agreementid)) {
				JSONArray adsResultJSONArray= adsResult.getJSONArray(agreementid);
				if (adsResultJSONArray != null && !adsResultJSONArray.isEmpty()) {
					providerListBean.setAds(adsResultJSONArray);
					providerListBean.setIsads(Boolean.TRUE);
					if (!add) {
						adsRet.add(providerListBean);
						add = true;
					}
				} else {
					providerListBean.setIsads(Boolean.FALSE);
				}
			} else {
				providerListBean.setIsads(Boolean.FALSE);
			}

			List<BranchProBean>  branchProBeans = new ArrayList<BranchProBean>();
			BranchProBean branchProBean = new BranchProBean();
			branchProBean.setAgreementid(agreementid);
			INSBProvider sprProvider = insbProviderDao.selectById(bean.getProvid());
			branchProBean.setComcode(sprProvider.getPrvcode());
			branchProBean.setDeptId(sprProvider.getId());
			branchProBean.setComname(sprProvider.getPrvname());
			branchProBean.setShortname(sprProvider.getPrvshotname());
			branchProBean.setLogo(sprProvider.getLogo());

			String deptid = "";
			List<SingleSiteBean> singleSiteBeans = new ArrayList<SingleSiteBean>();
			List<INSCDept>  deptS = appInsuredQuoteDao.getOutProviderList(agreementid);//出单网点
			//处理默认选中的出单网点，代理人出单网点存在优先，无默认选中第一个,判断 是否包含默认出单网点
			int index = selectDefaultSingleSite(deptS,commonusebranch);
			int k=0;
			for(int i = 0;i <  deptS.size();i ++){
				k++;
				SingleSiteBean singleSiteBean = new SingleSiteBean();
				singleSiteBean.setSiteId(deptS.get(i).getId());
				singleSiteBean.setSiteName(deptS.get(i).getComname());
				singleSiteBean.setSiteShortName(deptS.get(i).getShortname());
				singleSiteBean.setSiteAddress(deptS.get(i).getAddress());
				if(index >= 0){
					if(i == index){
						deptid = deptS.get(i).getId();
						singleSiteBean.setSelected(true);
						branchProBean.setMatchsite(true);
					}
				}else{
					if(k==1){
						deptid = deptS.get(i).getId();
						singleSiteBean.setSelected(true);
					}
				}
				singleSiteBeans.add(singleSiteBean);
			}
			branchProBean.setSingleSiteBeans(singleSiteBeans);
			if(lastProid.equals(sprProvider.getPrvcode())){
				branchProBean.setSelected(true);
			}
			branchProBean.setSelected(true);
			branchProBeans.add(branchProBean);

			//添加能力类型 0 自动(精灵，EDI) 1 人工
			String result = getAutoHandFlag(sprProvider.getId(),deptid);
			providerListBean.setAotohand(result);
			//添加能力是否具备备用平台信息查询能力
			providerListBean.setReservedPlatformresult(getReservedPlatformresultFlag(sprProvider.getId(),deptid));
			providerListBean.setBranchProBeans(branchProBeans);
			if (!add) {
				providerListBeans.add(providerListBean);
			}
		}
		/*Collections.sort(providerListBeans, new Comparator<ProviderListBean>() {
			@Override
			public int compare(ProviderListBean o1, ProviderListBean o2) {
				if (o1.getIsads()) return -1;
				return 0;
			}
		});
		//bug 3481 带出的上年投保公司，没有显示在保险公司列表的第一位
		Collections.sort(providerListBeans, new Comparator<ProviderListBean>() {
			@Override
			public int compare(ProviderListBean o1, ProviderListBean o2) {
				if (o1.isIslastyearpro()) return -1;
				return 0;
			}
		});*/
		lastyearRet.addAll(adsRet);
		lastyearRet.addAll(providerListBeans);
		return lastyearRet;
	}
	private String getReservedPlatformresultFlag(String pid, String deptid) {
    	String result = "1";
    	INSBAutoconfigshowQueryModel queryModel = new INSBAutoconfigshowQueryModel();
		queryModel.setDeptId(deptid);
		queryModel.setProviderid(pid);
		List<String> list = new ArrayList<String>();
		list.add("02");//精灵能力
		queryModel.setQuoteList(list);
		queryModel.setConftype("05");//备用平台
		List<INSBAutoconfigshow> autoconfigshows = insbAutoconfigshowService.autoOrArtificial(queryModel);
		if(null != autoconfigshows && autoconfigshows.size() > 0){
			result = "0";
		}
		return result;
	}
    private List<ProviderListBean> buildProviderListBeansForRenewal(List<SelectProviderBean> selectProviderBeans, String lastProid, String commonusebranch) {
        String treeTop = "";
        if(lastProid != null && lastProid.length() >= 4){
            treeTop = lastProid.substring(0,4);
        }

        List<ProviderListBean>  providerListBeans = new ArrayList<ProviderListBean>();

        for(SelectProviderBean bean : selectProviderBeans){
            ProviderListBean providerListBean = new ProviderListBean();
            INSBProvider insbProviderParent = insbProviderDao.selectById(bean.getParentid());
            //判断是否是上年投保公司
            if(treeTop.equals(bean.getParentid())){
                providerListBean.setIslastyearpro(true);
            }
            providerListBean.setProId(insbProviderParent.getId());
            providerListBean.setPrvcode(insbProviderParent.getPrvcode());
            providerListBean.setPrvname(insbProviderParent.getPrvname());
            providerListBean.setPrvshotname(insbProviderParent.getPrvshotname());
            providerListBean.setLogo(insbProviderParent.getLogo());
            providerListBean.setChanneltype(insbProviderParent.getBusinesstype());
            providerListBean.setAgreementname(bean.getAgreementname());
            if(treeTop.equals(providerListBean.getProId())){
                providerListBean.setIsflag(true);
            }

            List<BranchProBean> branchProBeans = new ArrayList<BranchProBean>();
            BranchProBean branchProBean = new BranchProBean();

            String agreementid = bean.getAgreementid();
            branchProBean.setAgreementid(agreementid);

            INSBProvider sprProvider = insbProviderDao.selectById(bean.getProvid());
            branchProBean.setComcode(sprProvider.getPrvcode());
            branchProBean.setDeptId(sprProvider.getId());
            branchProBean.setComname(sprProvider.getPrvname());
            branchProBean.setShortname(sprProvider.getPrvshotname());

            List<SingleSiteBean> singleSiteBeans = new ArrayList<SingleSiteBean>();

            List<INSCDept> deptS = appInsuredQuoteDao.getOutProviderList(agreementid);//出单网点

            //处理默认选中的出单网点，代理人出单网点存在优先，无默认选中第一个,判断 是否包含默认出单网点
            int index = selectDefaultSingleSite(deptS,commonusebranch);
            int k=0;

            for(int i = 0;i <  deptS.size();i ++){
                k++;
                SingleSiteBean singleSiteBean = new SingleSiteBean();
                singleSiteBean.setSiteId(deptS.get(i).getId());
                singleSiteBean.setSiteName(deptS.get(i).getComname());
                singleSiteBean.setSiteShortName(deptS.get(i).getShortname());
                singleSiteBean.setSiteAddress(deptS.get(i).getAddress());
                if(index >= 0){
                    if(i == index){
                        singleSiteBean.setSelected(true);
                        branchProBean.setMatchsite(true);
                    }
                }else{
                    if(k==1){
                        singleSiteBean.setSelected(true);
                    }
                }
                singleSiteBeans.add(singleSiteBean);
            }

            branchProBean.setSingleSiteBeans(singleSiteBeans);

            if(lastProid != null && lastProid.equals(sprProvider.getPrvcode())){
                branchProBean.setSelected(true);
            }
            branchProBean.setSelected(true);

            branchProBeans.add(branchProBean);
            providerListBean.setBranchProBeans(branchProBeans);

            //添加能力类型 0 自动(精灵，EDI) 1 人工
            /*String result = getAutoHandFlag(sprProvider.getId(),deptid);
            providerListBean.setAotohand(result);*/
            providerListBeans.add(providerListBean);
        }

        return providerListBeans;
    }

	private List<ProviderListBean> getproviderListBeansByAgengidForMinizzb(
			List<SelectProviderBeanForMinizzb> selectProviderBeans, String taskid,INSBAgent agent) {
		String commonusebranch = agent.getCommonusebranch();
		List<String> selected = getSelectedProviders(taskid);
		String lastProid = getLastYearProvId(taskid);
		String treeTop = "";
		if(lastProid.length() >= 4){
			treeTop = lastProid.substring(0,4);
		}

		List<ProviderListBean>  providerListBeans = new ArrayList<ProviderListBean>();
		for(SelectProviderBeanForMinizzb bean : selectProviderBeans){
			ProviderListBean providerListBean = new ProviderListBean();
			INSBProvider insbProviderParent = insbProviderDao.selectById(bean.getParentid());
			//判断是否是上年投保公司
			if(treeTop.equals(bean.getParentid())){
				providerListBean.setIslastyearpro(true);
			}
			providerListBean.setProId(insbProviderParent.getId());
			providerListBean.setPrvcode(insbProviderParent.getPrvcode());
			providerListBean.setPrvname(insbProviderParent.getPrvname());
			providerListBean.setPrvshotname(insbProviderParent.getPrvshotname());
			providerListBean.setLogo(insbProviderParent.getLogo());
			providerListBean.setChanneltype(insbProviderParent.getBusinesstype());
			providerListBean.setAgreementname(bean.getAgreementname());
			if(treeTop.equals(providerListBean.getProId()) || selected.contains(providerListBean.getProId())){
				providerListBean.setIsflag(true);
			}
			//判断投保时间是否重复投保     重复投保状态   0 都重复 1 交强险 商业险 有一个重复或者都不重复
			providerListBean.setRepeatinsured(ifRepeatInsured(taskid,bean.getProvid()));

			List<BranchProBean>  branchProBeans = new ArrayList<BranchProBean>();
			BranchProBean  branchProBean = new BranchProBean();
			String agreementid = bean.getAgreementid();
			branchProBean.setAgreementid(agreementid);
			INSBProvider sprProvider = insbProviderDao.selectById(bean.getProvid());
			branchProBean.setComcode(sprProvider.getPrvcode());
			branchProBean.setDeptId(sprProvider.getId());
			branchProBean.setComname(sprProvider.getPrvname());
			branchProBean.setShortname(sprProvider.getPrvshotname());
			List<SingleSiteBean> singleSiteBeans   = new ArrayList<SingleSiteBean>();
			// minizzb 的出单网点改为出单代理人所属网点
			//List<INSCDept>  deptS = new ArrayList<INSCDept>(); //appInsuredQuoteDao.getOutProviderList(agreementid);//出单网点
			//String userdeptid = agent.getDeptid(); //出单代理人
			//INSCDept deptModel = inscDeptDao.selectById(userdeptid); //出单代理人所属机构
			List<INSCDept> deptS = insbChannelagreementDao.getAgreementDeptByPrvcode(bean.getChnagreementid(),bean.getProvid());
			/*if(deptModel!=null){
				deptS.add(deptModel);
			}*/
			//处理默认选中的出单网点，代理人出单网点存在优先，无默认选中第一个,判断 是否包含默认出单网点
			String deptid = "";
			if( deptS !=null ) {
				int index = selectDefaultSingleSite(deptS, commonusebranch);
				int k = 0;
				for (int i = 0; i < deptS.size(); i++) {
					k++;
					SingleSiteBean singleSiteBean = new SingleSiteBean();
					singleSiteBean.setSiteId(deptS.get(i).getId());
					singleSiteBean.setSiteName(deptS.get(i).getComname());
					singleSiteBean.setSiteShortName(deptS.get(i).getShortname());
					singleSiteBean.setSiteAddress(deptS.get(i).getAddress());
					if (index >= 0) {
						if (i == index) {
							deptid = deptS.get(i).getId();
							singleSiteBean.setSelected(true);
							branchProBean.setMatchsite(true);
						}
					} else {
						if (k == 1) {
							deptid = deptS.get(i).getId();
							singleSiteBean.setSelected(true);
						}
					}
					singleSiteBeans.add(singleSiteBean);
				}
			}

			branchProBean.setSingleSiteBeans(singleSiteBeans);
			if(lastProid.equals(sprProvider.getPrvcode())){
				branchProBean.setSelected(true);
			}
			branchProBean.setSelected(true);
			branchProBeans.add(branchProBean);

			//添加能力类型 0 自动(精灵，EDI) 1 人工
			//String result = "0";// minizzb 无论什么情况下都是0 //getAutoHandFlag(sprProvider.getId(),deptid);
			//providerListBean.setAotohand(result);
			//providerListBean.setBranchProBeans(branchProBeans);

			//添加能力类型 0 自动(精灵，EDI) 1 人工
			String result = getAutoHandFlag(sprProvider.getId(),deptid); // --new
			providerListBean.setAotohand(result);// --new
			providerListBean.setBranchProBeans(branchProBeans);
			//添加能力是否具备备用平台信息查询能力 --new
			providerListBean.setReservedPlatformresult(getReservedPlatformresultFlag(sprProvider.getId(),deptid));


			providerListBeans.add(providerListBean);

		}
		return providerListBeans;
	}

	/**
	 *  0 都重复 1 交强险 商业险 有一个重复或者都不重复
	 * @param taskid
	 * @param provid
	 * @return
	 */
	private int ifRepeatInsured(String taskid, String provid) {
		//从redis中取数据
		int result = 1;
		INSBProvider provider = insbProviderDao.selectById(provid);
		if(null != provider && null != provider.getAdvancequote() && provider.getAdvancequote() != 0 ){
			Map<String, Object> insuredatemap = judgeInsuredDate(provider.getAdvancequote(),taskid);
			boolean syenddateflag = true;
			boolean jqenddateflag = true;
			if(insuredatemap.containsKey("syenddateflag")){
				syenddateflag = (boolean) insuredatemap.get("syenddateflag");
			}
			if(insuredatemap.containsKey("jqenddateflag")){
				jqenddateflag = (boolean) insuredatemap.get("jqenddateflag");
			}
			if(!syenddateflag && !jqenddateflag){
				result = 0;
			}else{
				result = 1;
			}
		}
		return result;
	}

	private int selectDefaultSingleSite(List<INSCDept> depts,String commonusebranch) {
		int result = -1;
		for(int i = 0;i <  depts.size();i ++){
			if(!StringUtil.isEmpty(commonusebranch) && commonusebranch.equals(depts.get(i).getId())){
				result = i;
			}
		}
		return result;
	}

	private String getAutoHandFlag(String pid, String deptid) {
		String result = "1";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("providerid", pid);
//		map.put("deptid", deptid);
//		List<INSBAutoconfig> autoconfigs = appInsuredQuoteDao.selectAutoconfigList(map);
//		if(null != autoconfigs && autoconfigs.size() > 0){
//			String autoconfig = autoconfigs.get(0).getQuotetype();
//			if("01".equals(autoconfig) || "02".equals(autoconfig)){
//				result = "0";
//			}
//		}
		INSBAutoconfigshowQueryModel queryModel = new INSBAutoconfigshowQueryModel();
		queryModel.setDeptId(deptid);
		queryModel.setProviderid(pid);
		List<String> list = new ArrayList<String>();
		list.add("01");
		list.add("02");
		queryModel.setQuoteList(list);
		List<INSBAutoconfigshow> autoconfigshows = insbAutoconfigshowService.autoOrArtificial(queryModel);
		if(null != autoconfigshows && autoconfigshows.size() > 0){
			result = "0";
		}
		return result;
	}

	/**
	 * 根据任务id获取已经选中的报价公司
	 * @param taskid
	 * @return
	 */
	private List<String> getSelectedProviders(String taskid){
		List<INSBQuoteinfo> insbQuoteinfos = appInsuredQuoteDao.getSelectedProvidersByTaskid(taskid);
		List<String> selected = new ArrayList<String>();
		if(null != insbQuoteinfos && insbQuoteinfos.size() > 0){
			for(INSBQuoteinfo insbQuoteinfo : insbQuoteinfos){
				if(insbQuoteinfo.getInscomcode().length() >= 4){
					selected.add(insbQuoteinfo.getInscomcode().substring(0, 4));
				}
			}
		}
		return selected;
	}

	/**
	 * 获取上年投保公司id
	 * @param taskid
	 * @return
	 */
	private String getLastYearProvId(String taskid){
		String lastProid = "";
		//从redis里面取值，取到直接返回
		LastYearPolicyInfoBean yearPolicyInfoBean = redisClient.get(Constants.TASK, taskid, LastYearPolicyInfoBean.class);
		if(null != yearPolicyInfoBean){
			//云查询結果中去上年投保公司
			LastYearSupplierBean lastYearSupplierBean = yearPolicyInfoBean.getLastYearSupplierBean();
			if(null != lastYearSupplierBean){
				lastProid = lastYearSupplierBean.getSupplierid();
			}
		}
		return lastProid;
	}

	@Override
	public CommonModel choiceProviderIds(ChoiceProviderIdsModel model) {
		LogUtil.info(model.getProcessinstanceid() + "选择报价公司=" + model.getParamsList());
		return saveProviderList(model,0);
	}

	/**
	 * 保存选中的供应商列表
	 * @param model
	 * @param flag  0正常调用  1 需要调用工作流增加报价公司
	 * @return
	 */
	private CommonModel saveProviderList(ChoiceProviderIdsModel model,int flag){
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if (StringUtil.isEmpty(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			// 根据实例id查询报价信息总表，获取id
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			if (null == quotetotalinfo) {
				commonModel.setStatus("fail");
				commonModel.setMessage("报价总表信息不存在");
				return commonModel;
			}

			//添加平台查询状态 选择上年投保公司 查询的上年投保公司名称 20151215
			//平台查询状态 1 true 0 false
			quotetotalinfo.setCloudstate(model.isCloudstate()?"1":"0");
			quotetotalinfo.setHandersupplier(model.getHandersupplier());
			quotetotalinfo.setLastyearsupplier(model.getLastyearsupplier());
			insbQuotetotalinfoDao.updateById(quotetotalinfo);
			LogUtil.info("INSBQuotetotalinfo|报表数据埋点|"+JSONObject.fromObject(quotetotalinfo).toString());

			//根据实例id查询车辆信息
			INSBCarinfo insbCarinfo = insbCarinfoDao.selectCarinfoByTaskId(taskid);
			if (null == insbCarinfo) {
				commonModel.setStatus("fail");
				commonModel.setMessage("车辆信息不存在");
				return commonModel;
			}

			List<String> params = model.getParamsList();
			if(null == params || params.size() <= 0){
				commonModel.setStatus("fail");
				commonModel.setMessage("还没有选择报价公司");
				return commonModel;
			}

			List<String> deptcodes = new ArrayList<>(1);
			List<String> inscomcodes = new ArrayList<>();

			for(String data : params) {
				if (!StringUtil.isEmpty(data)) {
					String[] arra = data.split("#");
					if (arra.length != 4) {
						continue;
					}
					inscomcodes.add(arra[1]);

					if (!deptcodes.contains(arra[2])) {
						deptcodes.add(arra[2]);
					}
				}
			}

			List<String> insuredcomcodes = new ArrayList<>();

			INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
			quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
			List<INSBQuoteinfo> quoteinfos = insbQuoteinfoDao.selectList(quoteinfo);
			if(null != quoteinfos && quoteinfos.size() > 0){
				for(INSBQuoteinfo insbQuoteinfo : quoteinfos){
					insuredcomcodes.add(insbQuoteinfo.getInscomcode());

					if (0 == flag && !inscomcodes.contains(insbQuoteinfo.getInscomcode())) {
						insbQuoteinfoDao.deleteById(insbQuoteinfo.getId());
						LogUtil.info("0删除报价" + insbQuoteinfo.getInscomcode() + " in " + taskid);

						insbAdsService.deleteAds(taskid,insbQuoteinfo.getAgreementid());

						//删除工作流轨迹
						if (StringUtil.isNotEmpty(insbQuoteinfo.getWorkflowinstanceid())) {
							insbWorkflowsubDao.deleteByInstanceId(insbQuoteinfo.getWorkflowinstanceid());
							LogUtil.info("0删除工作流轨迹" + insbQuoteinfo.getWorkflowinstanceid() + " in " + taskid);

                            Map<String,Object> queryMap = new HashMap<>();
                            queryMap.put("maininstanceid", taskid);
                            queryMap.put("subinstanceid", insbQuoteinfo.getWorkflowinstanceid());
                            if (StringUtil.isNotEmpty(insbQuoteinfo.getInscomcode())) {
                                queryMap.put("inscomcode", insbQuoteinfo.getInscomcode());
                            }
                            insbRealtimetaskDao.deleteRealtimetask(queryMap);
                            LogUtil.info("0删除实时任务表" + insbQuoteinfo.getWorkflowinstanceid() + " in " + taskid + "/" + insbQuoteinfo.getInscomcode());
						}
					}
				}
			}

			String platformInnercode = null;
			List<INSCDept> deptList = inscDeptDao.selectAllByComcodes(deptcodes);
            List<INSBQuoteinfo> insbQuoteinfos = new ArrayList<INSBQuoteinfo>();
			List<String> agreementList = new ArrayList<String>();

			// 向报价信息表插入数据，一个报价公司一条
			List<String> ids = new ArrayList<String>();
			for(String data : params){
				if(!StringUtil.isEmpty(data)){
					String[] arra = data.split("#");
					if(arra.length != 4){
						continue;
					}

					if (!insuredcomcodes.contains(arra[1])) {

						ids.add(arra[1]);

						INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
						insbQuoteinfo.setCreatetime(new Date());
						insbQuoteinfo.setOperator(quotetotalinfo.getOperator());
						insbQuoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
						insbQuoteinfo.setOwnername(insbCarinfo.getOwnername());
						insbQuoteinfo.setPlatenumber(insbCarinfo.getCarlicenseno());
						insbQuoteinfo.setInscomcode(arra[1]);
						insbQuoteinfo.setAgreementid(arra[0]);
						agreementList.add(arra[0]);
						insbQuoteinfo.setDeptcode(arra[2]);
						insbQuoteinfo.setBuybusitype(arra[3]);

						for (INSCDept dept : deptList) {
							if (dept.getId().equals(arra[2])) {
								platformInnercode = inscDeptService.getPlatformInnercode(dept.getDeptinnercode());
								if (platformInnercode != null) {
									insbQuoteinfo.setPlatforminnercode(Integer.parseInt(platformInnercode));
								}
							}
						}

						insbQuoteinfos.add(insbQuoteinfo);
						LogUtil.info("添加报价" + insbQuoteinfo.getInscomcode() + " in " + taskid);


						//bug3566 如果我提交一个单子，提交的天安平安，备注车辆信息转人工，这个时候我再增加保险公司，增加的保险公司就不转人工了就直接报价出来了
						Map<String, Object> commentQuery = new HashMap<String, Object>();
						commentQuery.put("instanceid", taskid);
						List<INSBUsercomment> usercomments = insbUsercommentDao.getMainComment(commentQuery);
						if (!usercomments.isEmpty()) {
							// 判断备注类型是否需要转人工
							LogUtil.info("增加的保险公司,判断备注类型是否需要转人工taskid=" + taskid + "代理人选择的备注类型=" + usercomments.get(0).getCommentcontenttype());
							Map<String, Object> map = isNeedToManMade(String.valueOf(usercomments.get(0).getCommentcontenttype()));
							if (null != map && (boolean) map.get("flag")) {
								LogUtil.info("taskid=" + taskid + "代理人选择的备注类型=" + usercomments.get(0).getCommentcontenttype());
								saveFlowerrorToManWork(taskid, insbQuoteinfo.getInscomcode(),
										"代理人录入备注类型（" + map.get("result").toString() + "）,备注内容（" + usercomments.get(0).getCommentcontent() + "）,转人工处理", "8");
							}
						}

					} else {
						LogUtil.info(arra[1] + "已存在，不用添加 in " + taskid);
					}
				}
			}

			if (!insbQuoteinfos.isEmpty()) {
				insbQuoteinfoDao.insertInBatch(insbQuoteinfos);
			}
			if (!agreementList.isEmpty()) {
				insbAdsService.saveAds(taskid,agreementList);
			}

			if(1 == flag){
				addOneProviderInsuredConfig(taskid,ids);
			}else{
				//正常投保流程  保存投保书标志位
				saveInsuranceBooksMark(taskid,model.getWebpagekey());
			}

			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}
	/**
	 * 新增加的公司增加保险配置信息,向保单表插入数据
	 * @param taskid
	 * @param ids
	 */
	private void addOneProviderInsuredConfig(String taskid,List<String> ids){
		LogUtil.info("新增加的公司增加保险配置信息"+taskid+"=====供应商="+ids);

		if (ids == null || ids.isEmpty()) {
			return;
		}

		//保险配置存在先删除，在插入
		for (String proid : ids) {
			INSBCarkindprice insureCarkindprice = new INSBCarkindprice();
			insureCarkindprice.setTaskid(taskid);
			insureCarkindprice.setInscomcode(proid);
			List<INSBCarkindprice> carkindpricesList = insbCarkindpriceDao.selectList(insureCarkindprice);
			if(null != carkindpricesList && carkindpricesList.size() > 0){
				LogUtil.info("保存保险配置删除已有的险别分表"+taskid+"供应商id="+proid);
				for(INSBCarkindprice insbCarkindprice : carkindpricesList){
					insbCarkindpriceDao.deleteById(insbCarkindprice.getId());
				}
			}
		}

		boolean syflag = false;
		boolean jqflag = false;
		INSBCarconfig insbCarconfig = new INSBCarconfig();
		insbCarconfig.setTaskid(taskid);
		List<INSBCarconfig> insbCarconfigs = insbCarconfigDao.selectList(insbCarconfig);
		List<INSBCarkindprice> insbCarkindprices = new ArrayList<INSBCarkindprice>();
		for(INSBCarconfig carconfig : insbCarconfigs){
			INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
			insbCarkindprice.setOperator(carconfig.getOperator());
			insbCarkindprice.setCreatetime(new Date());
			insbCarkindprice.setTaskid(taskid);
			insbCarkindprice.setInskindcode(carconfig.getInskindcode());
			insbCarkindprice.setInskindtype(carconfig.getInskindtype());
			insbCarkindprice.setAmount(Double.parseDouble(carconfig.getAmount()));
			insbCarkindprice.setNotdeductible(carconfig.getNotdeductible());
			insbCarkindprice.setSelecteditem(carconfig.getSelecteditem());
			insbCarkindprice.setPreriskkind(carconfig.getPreriskkind());
			insbCarkindprice.setPlankey(carconfig.getPlankey());
			INSBRiskkindconfig vo = new INSBRiskkindconfig();
			vo.setRiskkindcode(carconfig.getInskindcode());
			INSBRiskkindconfig nfcRIkInsbRiskkindconfig=insbRiskkindconfigDao.selectOne(vo);
			if(nfcRIkInsbRiskkindconfig!=null){
				insbCarkindprice.setRiskname(nfcRIkInsbRiskkindconfig.getRiskkindname());
			}
			insbCarkindprices.add(insbCarkindprice);
			//商业险
			if("0".equals(carconfig.getInskindtype()) && !syflag){
				syflag = true;
			}
			//交强险
			if("2".equals(carconfig.getInskindtype())  && !jqflag){
				jqflag = true;
			}
		}

		List<INSBCarkindprice> carkindprices = new ArrayList<INSBCarkindprice>();
		for (String proid : ids) {
			for (INSBCarkindprice carkindprice : insbCarkindprices) {
				INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
				insbCarkindprice.setOperator(carkindprice.getOperator());
				insbCarkindprice.setCreatetime(carkindprice.getCreatetime());
				insbCarkindprice.setTaskid(carkindprice.getTaskid());
				insbCarkindprice.setInskindcode(carkindprice.getInskindcode());
				insbCarkindprice.setAmount(carkindprice.getAmount());
				insbCarkindprice.setSelecteditem(carkindprice.getSelecteditem());
				insbCarkindprice.setInskindtype(carkindprice.getInskindtype());
				insbCarkindprice.setNotdeductible(carkindprice.getNotdeductible());
				insbCarkindprice.setInscomcode(proid);
				if("1".equals(carkindprice.getNotdeductible())){
					insbCarkindprice.setBjmpCode("Ncf"+carkindprice.getInskindcode());
				}
				//保存plankey
				insbCarkindprice.setPlankey(carkindprice.getPlankey());
				insbCarkindprice.setPreriskkind(carkindprice.getPreriskkind());
				insbCarkindprice.setRiskname(carkindprice.getRiskname());
				carkindprices.add(insbCarkindprice);
			}
		}

		// 保险公司险别报价表
		insbCarkindpriceDao.insertInBatch(carkindprices);

		if(syflag){
			addSaveOrUpdatePolocy(taskid, ids, "0");
		}
		if(jqflag){
			addSaveOrUpdatePolocy(taskid, ids, "1");
		}
	}

	/**
	 * 更新保单表数据，每个报价公司一条  20160112
	 * @param taskid
	 * @param type 0 商业险 1交强险
	 */
	private void addSaveOrUpdatePolocy(String taskid,List<String> provids,String type){
		if(null == provids || provids.isEmpty() || StringUtil.isEmpty(taskid)){
			LogUtil.error("%s参数为空：%s", taskid, provids);
		}

		//查询保单信息(只查商业险)
		INSBPolicyitem allpolicys = new INSBPolicyitem();
		allpolicys.setTaskid(taskid);
		List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(allpolicys);
		INSBPolicyitem insbPolicyitem = insbPolicyitems.get(0);//取出一个拿数据
		//获取上年投保单时间
		Date startdate = queryInsureStartDate(taskid, type);

		for(String pid : provids){
			//删除已经存在的
			INSBPolicyitem isexist = new INSBPolicyitem();
			isexist.setTaskid(taskid);
			isexist.setInscomcode(pid);
			isexist.setRisktype(type);
			isexist = insbPolicyitemDao.selectOne(isexist);
			if(null != isexist){
				LogUtil.info("保单表删数据"+taskid+" 删除的保单表id="+isexist.getId()+"商业险交强险标识risktype="+isexist.getRisktype());
				insbPolicyitemDao.deleteById(isexist.getId());
			}

			INSBPolicyitem policyitem = new INSBPolicyitem();
			policyitem.setOperator(insbPolicyitem.getAgentname());
			policyitem.setCreatetime(new Date());
			policyitem.setTaskid(taskid);
			policyitem.setCarownerid(insbPolicyitem.getCarownerid());
			policyitem.setCarownername(insbPolicyitem.getCarownername());
			policyitem.setAgentname(insbPolicyitem.getAgentname());
			policyitem.setCarinfoid(insbPolicyitem.getCarinfoid());
			//险种类型
			policyitem.setRisktype(type);
			policyitem.setAgentnum(insbPolicyitem.getAgentnum());
			//policyitem.setNoti(remarks);
			//投时间
			policyitem.setStartdate(startdate);
			policyitem.setEnddate(ModelUtil.nowDateMinusOneDay(ModelUtil.nowDateAddOneYear(startdate, 1)));

			policyitem.setApplicantid(insbPolicyitem.getApplicantid());
			policyitem.setApplicantname(insbPolicyitem.getApplicantname());
			policyitem.setInsuredid(insbPolicyitem.getInsuredid());
			policyitem.setInsuredname(insbPolicyitem.getInsuredname());
			//未生效
			policyitem.setPolicystatus("5");
			//关联报价公司
			policyitem.setInscomcode(pid);
			insbPolicyitemDao.insert(policyitem);
		}
	}

	@Override
	public CommonModel insuranceScheme(String plankey,String taskid) {
		ExtendCommonModel commonModel = new ExtendCommonModel();
		try {
			if (null == plankey || "".equals(plankey)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("方案key不能为空");
				return commonModel;
			}
			List<InsureConfigsBean> configsBeans = null;
			if("snbxpz".equals(plankey) && !StringUtil.isEmpty(taskid)){
				//查询上年保险配置信息
				configsBeans = queryLastYearInsConfig(taskid);
			}else{
				configsBeans = getInsureConfigsModel(plankey);
			}
			if (null != configsBeans && configsBeans.size() > 0) {
				commonModel.setStatus("success");
				commonModel.setBody(configsBeans);
				Map<String, Object> insuredatemap = new HashMap<String, Object>();
				insuredatemap.put("syenddateflag", true);
				insuredatemap.put("jqenddateflag", true);
				insuredatemap.put("systartdate", "");
				insuredatemap.put("jqstartdate", "");
                insuredatemap.put("agentnoti", remarkTypeFromCode("0"));
				commonModel.setExtend(insuredatemap);
				//commonModel.setExtend(getCifInsuredDateINnPolicy(taskid)); 820改版
				commonModel.setMessage("操作成功");
			} else {
				commonModel.setStatus("fail");
				commonModel.setMessage("方案不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	private Map<String,Object> remarkTypeFromCode(String agentnotitype){
		Map<String,Object> map = new HashMap<String, Object>();
		INSCCode code = new INSCCode();
		code.setParentcode("agentnoti");
		if("1".equals(agentnotitype))
			code.setCodetype("agentnoti1");
		else
			code.setCodetype("agentnoti");
		List<INSCCode> listcode = inscCodeDao.selectList(code);
		if(listcode!=null&&listcode.size()>0){
			for (INSCCode inscCode : listcode) {
//				map.put(inscCode.getCodename(), inscCode.getProp1()==0?Boolean.FALSE:Boolean.TRUE);
				map.put(inscCode.getCodename(), inscCode.getCodevalue());
			}
		}
		return map;
	}


	private List<InsureConfigsBean> getInsureConfigsModel(String plankey){
		boolean flag = true;
		if ("zdypz".equals(plankey)) {
			flag = false;
			plankey = "dzrmx";
		}
		List<InsureConfigsModel> insureConfigs = appInsuredQuoteDao
				.queryInsureConfigByKey(plankey);

		List<InsureConfigsBean> configsBeans = new ArrayList<InsureConfigsBean>();
		for(InsureConfigsModel configsBean : insureConfigs){
			InsureConfigsBean insureConfigsBean = new InsureConfigsBean();
			insureConfigsBean.setId(configsBean.getId());
			insureConfigsBean.setIsdeductible(configsBean.getIsdeductible());
			insureConfigsBean.setPlankey(configsBean.getPlankey());
			insureConfigsBean.setPlanname(flag ? configsBean.getPlanname() : "自定义配置");
			insureConfigsBean.setPrekindcode(configsBean.getPrekindcode());
			insureConfigsBean.setRiskkindcode(configsBean.getRiskkindcode());
			insureConfigsBean.setRiskkindname(configsBean.getShortname());
			insureConfigsBean.setType(configsBean.getType());
			insureConfigsBean.setIsSelect("0".equals(configsBean.getSelected())&&flag?true:false);
			insureConfigsBean.setFlag("0".equals(configsBean.getIsflag())&&flag ?true:false);
			insureConfigsBean.setCoverage(flag ? configsBean.getSelectkey() : "0");
			String dataTemp = configsBean.getDatatemp();
			if(!StringUtil.isEmpty(dataTemp)){
				InsuredConfig insuredConfig = stringToInsuredConfig(dataTemp);
				insureConfigsBean.setInsuredConfig(insuredConfig);
			}
			configsBeans.add(insureConfigsBean);
		}
		return configsBeans;
	}

	private InsuredConfig stringToInsuredConfig(String res){
		JSONArray jsonArray = JSONArray.fromObject(res);
		Object[] datas = jsonArray.toArray();
		JSONObject jsonObject = JSONObject.fromObject(datas[0]);
		InsuredConfig config = (InsuredConfig) JSONObject.toBean(jsonObject, InsuredConfig.class);
		JSONArray array = JSONArray.fromObject(config.getVALUE());
		Object[] objects = array.toArray();
		List<RisksData> risksDatas = new ArrayList<RisksData>();
		for(Object object : objects){
			JSONObject jsonObject2 = JSONObject.fromObject(object);
			RisksData data = (RisksData) JSONObject.toBean(jsonObject2, RisksData.class);
			risksDatas.add(data);
		}
		config.setVALUE(risksDatas);
		return config;
	}
	@Override
	public CommonModel querySelectedInsure(String proid, String taskid) {
		ExtendCommonModel commonModel = new ExtendCommonModel();
		LogUtil.info("=======修改保险配置入参参数proid="+proid+"taskid="+taskid);
		try {
			if (StringUtil.isEmpty(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("任务id不能为空");
				return commonModel;
			}
			String plankey = "dzrmx";
			//proid为空时，保险险别选择表，当前任务报价选的所有的保险配置，不为空查询当前报价公司保险配置
			if(StringUtil.isEmpty(proid)){
				INSBCarconfig insbCarconfig = new INSBCarconfig();
				insbCarconfig.setTaskid(taskid);
				List<INSBCarconfig> insbCarconfigs = insbCarconfigDao.selectList(insbCarconfig);
				if(null == insbCarconfigs || insbCarconfigs.size() < 0){
					commonModel.setStatus("fail");
					commonModel.setMessage("保险险别选择表信息不存在");
					LogUtil.info(taskid+"=====保险险别选择表信息不存在");
					return commonModel;
				}
				plankey = insbCarconfigs.get(0).getPlankey();
			}else{
				INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
				insbCarkindprice.setTaskid(taskid);
				//查询所有的时候不传proid
				insbCarkindprice.setInscomcode(proid);
				List<INSBCarkindprice> insbCarkindprices = insbCarkindpriceDao.selectList(insbCarkindprice);
				if(null == insbCarkindprices || insbCarkindprices.size() < 0){
					commonModel.setStatus("fail");
					commonModel.setMessage("险别报价表信息不存在");
					LogUtil.info(taskid+"=====险别报价表信息不存在");
					return commonModel;
				}
				plankey = insbCarkindprices.get(0).getPlankey();
			}
			List<InsureConfigsBean> configsBeans = getSelectedInsure(proid,taskid,plankey);
			Map<String, Object> map = getSpecialInsuredConfigs(configsBeans,proid,taskid);
			String flowflag = queryWorkflowStatus(taskid,proid);
			map.put("flowflag", flowflag);
			if (null != configsBeans && configsBeans.size() > 0) {
				commonModel.setStatus("success");
				commonModel.setBody(configsBeans);
				commonModel.setMessage("操作成功");
				commonModel.setExtend(map);
			} else {
				commonModel.setStatus("fail");
				commonModel.setMessage("已选择的保险配置不存在");
				LogUtil.info(taskid+"=====已选择的保险配置不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
			LogUtil.info(taskid+"=====保险配置信息返回操作失败");
		}
		LogUtil.info(taskid+"修改保险配置操作信息："+JSONObject.fromObject(commonModel));
		return commonModel;
	}
	/**
	 * 13 2
	 *	14 0
	 *	19 1
	 * @param taskid
	 * @param inscomcode
	 * @return
	 */
	private String queryWorkflowStatus(String taskid,String inscomcode){
		LogUtil.info("====进入queryWorkflowStatus");
		String result = "";
		Map<String, String> map = new HashMap<String, String>();
		map.put("maininstanceid", taskid);
		map.put("inscomcode", inscomcode);
		String status = appInsuredQuoteDao.queryWorkflowStatusByMainAndSub(map);
		LogUtil.info("taskid="+ taskid+",inscomcode="+inscomcode+",status="+status);
		if(!StringUtil.isEmpty(status)){
			if("13".equals(status)){
				result = "2";
			}else if("14".equals(status)){
				result = "0";
			}else if("19".equals(status)){
				result = "1";
			}else if("51".equals(status)){
				result = "3";
			}
		}
		LogUtil.info("taskid="+ taskid+",inscomcode="+inscomcode+",result="+result);
		return result;
	}

	private Map<String, Object> getSpecialInsuredConfigs(
			List<InsureConfigsBean> configsBeans,String proid,String taskid) {
		LogUtil.info("====进入getSpecialInsuredConfigs");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<NewEquipmentInsBean> newEquipmentIns = null;
		if (null != configsBeans && configsBeans.size() > 0) {
			for(InsureConfigsBean bean : configsBeans){
				String value = getValueFromCode(bean.getRiskkindcode());
				if("04".equals(value)){//新增设备
					newEquipmentIns = querySpecialInsuredConf(taskid,proid,"04");
					if(null != newEquipmentIns && newEquipmentIns.size() > 0){
						resultMap.put("equipmentInsBeans", newEquipmentIns);
					}
				}
				if("05".equals(value)){//维修
					newEquipmentIns = querySpecialInsuredConf(taskid,proid,"05");
					if(null != newEquipmentIns && newEquipmentIns.size() > 0){
						resultMap.put("compensationdays", newEquipmentIns.get(0).getValue());
					}
				}
			}
		}
		return resultMap;
	}

	/**
	 * 查询特殊险别附加选项
	 * @param taskid
	 * @param proid
	 * @param type
	 */
	private List<NewEquipmentInsBean> querySpecialInsuredConf(String taskid, String proid,
			String type) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("taskid", taskid);
		map.put("inscomcode", proid);
		map.put("typecode", type);
		return appInsuredQuoteDao.querySpecialInsuredConf(map);
	}

	public List<InsureConfigsBean> getSelectedInsure(String inscomcode,String taskid,String plankey){
		LogUtil.info(taskid+"======进入getSelectedInsure、plankey="+plankey);
		Map<String, String> para = new HashMap<String, String>();
		List<InsureConfigsModel> insureConfigs = null;
		if(StringUtil.isEmpty(inscomcode)){
			para.put("taskid", taskid);
			if("snbxpz".equals(plankey)){
				//查询所有的保险配置
				insureConfigs = appInsuredQuoteDao.querySelectedAllInsuresnbxpz(para);
			}else{
				//查询所有的保险配置
				para.put("plankey", plankey);
				insureConfigs = appInsuredQuoteDao.querySelectedAllInsure(para);
			}
		}else{
			para.put("inscomcode", inscomcode);
			para.put("taskid", taskid);
			if("snbxpz".equals(plankey)){
				//查询单个公司的保险配置
				//insureConfigs = appInsuredQuoteDao.querySelectedInsuresnbxpz(para);
				para.put("plankey", "jjshx");//用经济实惠型映射出所有的险种 20160918 bug 4710
				insureConfigs = appInsuredQuoteDao.querySelectedInsure(para);
			}else{
				//查询单个公司的保险配置
				para.put("plankey", plankey);
				insureConfigs = appInsuredQuoteDao.querySelectedInsure(para);
			}
		}
		List<InsureConfigsBean> configsBeans = new ArrayList<InsureConfigsBean>();
		for(InsureConfigsModel configsBean : insureConfigs){
			InsureConfigsBean insureConfigsBean = new InsureConfigsBean();
			insureConfigsBean.setId(configsBean.getId());
			insureConfigsBean.setIsdeductible(configsBean.getIsdeductible());
			insureConfigsBean.setPlankey(plankey);
			insureConfigsBean.setPlanname(configsBean.getPlanname());
			insureConfigsBean.setPrekindcode(configsBean.getPrekindcode());
			insureConfigsBean.setRiskkindcode(configsBean.getRiskkindcode());
			insureConfigsBean.setRiskkindname(configsBean.getShortname());
			insureConfigsBean.setType(configsBean.getType());
			//获取选中的key值
			if("0".equals(configsBean.getType())){
				if(null != configsBean.getSelectkey()){
					JSONArray jsonArray = JSONArray.fromObject(configsBean.getSelectkey());
					if(null != jsonArray && jsonArray.size() > 0){
						JSONObject jsonObject = JSONObject.fromObject(jsonArray.get(0));
						JSONObject json = JSONObject.fromObject(jsonObject.get("VALUE"));
						insureConfigsBean.setCoverage(json.get("KEY").toString());
					}
				}else{
					insureConfigsBean.setCoverage("不投保");
				}
			}else{
				if(null != configsBean.getSelectkey()){
					insureConfigsBean.setCoverage(configsBean.getSelectkey());
				}else{
					//交强险
					if("2".equals(configsBean.getType())){
						insureConfigsBean.setCoverage("不购买");
					}else{
						insureConfigsBean.setCoverage("不代缴");
					}
				}
			}
			if(null == configsBean.getSelectkey()){
				insureConfigsBean.setIsSelect(false);
			}else{
				insureConfigsBean.setIsSelect(true);
			}

			//不计免赔 1选中 0 不选
			if("1".equals(configsBean.getIsflag())){
				insureConfigsBean.setFlag(true);
			}else{
				insureConfigsBean.setFlag(false);
			}
			String dataTemp = configsBean.getDatatemp();
			if(!StringUtil.isEmpty(dataTemp)){
				InsuredConfig insuredConfig = stringToInsuredConfig(dataTemp);
				insureConfigsBean.setInsuredConfig(insuredConfig);
			}
			configsBeans.add(insureConfigsBean);
		}
		return configsBeans;
	}

	/**
	 * 根据实例id查询投保信息
	 */
	@Override
	public CommonModel queryInsureInfo(String processinstanceid,String inscomcode) {
		CommonModel commonModel = new CommonModel();
		if(null == processinstanceid || "".equals(processinstanceid)){
			commonModel.setStatus("fail");
			commonModel.setMessage("实例id不能为空");
			return commonModel;
		}
		AppInsureinfoBean bean = new AppInsureinfoBean();
		// 商业险
		AppBusiness business = null;
		// 交强险
		AppBusiness compulsoryBusiness = null ;
		//车辆信息
		AppCarInfo carInfo = null;
		// 投保人
		AppPerson insurePersion = null;
		//权益索赔人
		AppPerson legalrightclaim = null;
		// 车主
		AppPerson carowerinfo = null;
		//被保人
		AppPerson passivePersion = null;
		try {
//			被保人
			INSBInsured beibaoren = new INSBInsured();
			beibaoren.setTaskid(processinstanceid);
			INSBInsured insured = insbInsuredDao.selectOne(beibaoren);
			if(insured!=null){
				INSBPerson p = insbPersonDao.selectById(insured.getPersonid());
				if(p!=null){
					passivePersion = new AppPerson();//被保人
					passivePersion.setName(p.getName());
					passivePersion.setCertificateType(String.valueOf(p.getIdcardtype()));
					passivePersion.setCertNumber(p.getIdcardno());
					passivePersion.setEmail(p.getEmail());
					passivePersion.setTel(p.getCellphone());
				}
			}
//			投保人
			INSBApplicant toubaoren = new INSBApplicant();
			toubaoren.setTaskid(processinstanceid);
			INSBApplicant applicant = insbApplicantDao.selectOne(toubaoren);
			if(applicant!=null){
				INSBPerson p = insbPersonDao.selectById(applicant.getPersonid());
				if(p!=null){
					insurePersion = new AppPerson();//投保人
					insurePersion.setName(p.getName());
					insurePersion.setCertificateType(String.valueOf(p.getIdcardtype()));
					insurePersion.setCertNumber(p.getIdcardno());
					insurePersion.setEmail(p.getEmail());
					insurePersion.setTel(p.getCellphone());
				}
			}
//			车主
			INSBCarowneinfo chezhu = new INSBCarowneinfo();
			chezhu.setTaskid(processinstanceid);
			INSBCarowneinfo carowneinfo = insbCarowneinfoDao.selectOne(chezhu);
			if(carowneinfo!=null){
				INSBPerson p = insbPersonDao.selectById(carowneinfo.getPersonid());
				if(p!=null){
					carowerinfo = new AppPerson();//车主
					carowerinfo.setName(p.getName());
					carowerinfo.setCertificateType(String.valueOf(p.getIdcardtype()));
					carowerinfo.setCertNumber(p.getIdcardno());
					carowerinfo.setEmail(p.getEmail());
					carowerinfo.setTel(p.getCellphone());
				}
			}

//			权益索赔人
			INSBLegalrightclaim insbLegalrightclaim = new INSBLegalrightclaim();
			insbLegalrightclaim.setTaskid(processinstanceid);
			INSBLegalrightclaim insblegalrightclaim = insbLegalrightclaimDao.selectOne(insbLegalrightclaim);
			if(insblegalrightclaim!=null){
				INSBPerson p = insbPersonDao.selectById(insblegalrightclaim.getPersonid());
				if(p!=null){
					legalrightclaim = new AppPerson();//投保人
					legalrightclaim.setName(p.getName());
					legalrightclaim.setCertificateType(String.valueOf(p.getIdcardtype()));
					legalrightclaim.setCertNumber(p.getIdcardno());
					legalrightclaim.setEmail(p.getEmail());
					legalrightclaim.setTel(p.getCellphone());
				}
			}

//			商业险、交强险
			INSBCarinfo insbCarinfo = new INSBCarinfo();//车辆信息表
			insbCarinfo.setTaskid(processinstanceid);
			INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
			if(carinfo!=null){
				INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
				insbPolicyitem.setTaskid(processinstanceid);
				List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(insbPolicyitem);
				business = new AppBusiness();//商业险
				compulsoryBusiness = new AppBusiness();//交强险
				if(null != insbPolicyitems && insbPolicyitems.size() > 0){
					for(INSBPolicyitem policyitem : insbPolicyitems){
						if("0".equals(policyitem.getRisktype())){
							business.setRiskstartdate(ModelUtil.conbertToString(policyitem.getStartdate()));
							business.setRiskenddate(ModelUtil.conbertToString(policyitem.getEnddate()));
						}else{
							compulsoryBusiness.setRiskstartdate(ModelUtil.conbertToString(policyitem.getStartdate()));
							compulsoryBusiness.setRiskenddate(ModelUtil.conbertToString(policyitem.getEnddate()));
						}
					}
				}
				carInfo = new AppCarInfo();//车辆信息
				carInfo.setModelCode(carinfo.getStandardfullname());
			    carInfo.setEngineNo(ModelUtil.hiddenNumber(carinfo.getEngineno()));
			    carInfo.setVin(ModelUtil.hiddenNumber(carinfo.getVincode()));
			    carInfo.setIstransfer(StringUtil.isEmpty(carinfo.getIsTransfercar()) || carinfo.getIsTransfercar().equals("0")?false:true);
			    carInfo.setProperty(carinfo.getProperty());
			    carInfo.setCarproperty(carinfo.getCarproperty());
			    if(carinfo.getTransferdate()!=null){
			    	carInfo.setChgOwnerDate(ModelUtil.conbertToString(carinfo.getTransferdate()));
			    }
			    if(carinfo.getRegistdate()!=null){
			    	carInfo.setFirstRegisterDate(ModelUtil.conbertToString(carinfo.getRegistdate()));
			    }
			  //上年商业承保公司
			    bean.setLastComId(carinfo.getPreinscode());
				bean.setDrivingRegion(carinfo.getDrivingarea());
			}
			// 商业险
			bean.setProcessinstanceid(processinstanceid);
			bean.setPassiveInsurePersion(passivePersion);
			bean.setInsurePersion(insurePersion);
			bean.setCarowerinfo(carowerinfo);
			bean.setLegalrightclaim(legalrightclaim);
			bean.setBusinessInsureddate(business);
			bean.setTrafficInsureddate(compulsoryBusiness);
			bean.setCarinfo(carInfo);
			//当前结点状态
			bean.setFlowflag(queryWorkflowStatus(processinstanceid, inscomcode));
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(bean);

		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel saveOrUpdateInsureInfo(AppInsureinfoBean bean) {
		CommonModel commonModel = new CommonModel();
		try {
			if (null == bean) {
				commonModel.setStatus("fail");
				commonModel.setMessage("传入对象为空");
				return commonModel;
			}
			LogUtil.info("修改投保信息saveOrUpdateInsureInfo" + bean.getProcessinstanceid() + "===修改的供应商的id=" + bean.getInscomcode());

			String taskid = bean.getProcessinstanceid();// 实例id
			String inscomcode = bean.getInscomcode();//保险号code
            String flowflag = bean.getFlowflag();

            INSBPolicyitem po = new INSBPolicyitem();// 保单表
            po.setTaskid(taskid);
            po.setInscomcode(inscomcode);
            List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(po);

			String operator = null;
			//old code: String operator = null == insbPolicyitems ? "" : insbPolicyitems.get(0).getOperator();
            //bug 保单信息表的operator在CM后台操作任务中"保险配置"时会被修改，现从报价表中取代理人信息
            INSBQuotetotalinfo paramTemp = new INSBQuotetotalinfo();
            paramTemp.setTaskid(taskid);
            INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(paramTemp);

            //报价总表中agentname有null值，取agentnum查代理人表
			if (quotetotalinfo != null && StringUtil.isNotEmpty(quotetotalinfo.getAgentnum())) {
				INSBAgent agentnumTemp = new INSBAgent();
				agentnumTemp.setJobnum(quotetotalinfo.getAgentnum());
				INSBAgent insbagent = insbAgentDao.selectOne(agentnumTemp);
				if (insbagent != null) {
					operator = insbagent.getName();
				}
			}
            
            if (!"1".equals(flowflag) && !"2".equals(flowflag)) {
                AppPerson beibaoren = bean.getPassiveInsurePersion();// 被保人
                AppPerson toubaoren = bean.getInsurePersion();// 投保人
                AppPerson quanyisuopeiren = bean.getLegalrightclaim();// 权益索赔人
                AppPerson ower = bean.getCarowerinfo();// 车主
                AppBusiness bu = bean.getBusinessInsureddate();// 商业险期间
                AppBusiness trbu = bean.getTrafficInsureddate();// 交强期间
                AppCarInfo cl = bean.getCarinfo();// 车辆信息
                INSBInvoiceinfo invoiceinfo = bean.getInvoiceinfo();//发票信息
                // String email = bean.getEmail();
                // String tel = bean.getTel();
                String lastyearcomid = bean.getLastComId();// 上年保险公司id
                String region = bean.getDrivingRegion();// 行驶区域
                Date date = new Date();
                INSBInsured insure = new INSBInsured();// 被保人表
                INSBApplicant applicant = new INSBApplicant();// 投保人表
                INSBLegalrightclaim insbLegalrightclaim = new INSBLegalrightclaim();
                INSBCarowneinfo carowne = new INSBCarowneinfo();// 车主信息
                INSBCarinfo carinfo = new INSBCarinfo();// 车辆信息表
                INSBPerson insureper = null;
                INSBPerson applicper = null;
                INSBPerson carowercper = null;
                INSBInvoiceinfo invoice = null;
                //发票信息
                if (invoiceinfo != null) {
                    invoice = new INSBInvoiceinfo();
                    invoice.setTaskid(taskid);
                    INSBInvoiceinfo infos = insbInvoiceinfoDao.selectOne(invoice);
                    if (infos == null) {
                        if (invoiceinfo.getInvoicetype() == 1) {
                            invoice = new INSBInvoiceinfo();
                            invoice.setBankname(invoiceinfo.getBankname());
                            invoice.setCreatetime(date);
                            invoice.setNoti(invoiceinfo.getNoti());
                            invoice.setEmail(invoiceinfo.getEmail());
                            invoice.setIdentifynumber(invoiceinfo.getIdentifynumber());
                            invoice.setInscomcode(inscomcode);
                            invoice.setInvoicetype(invoiceinfo.getInvoicetype());
                            invoice.setOperator(operator);
                            invoice.setRegisteraddress(invoiceinfo.getRegisteraddress());
                            invoice.setTaskid(taskid);
                            invoice.setRegisterphone(invoiceinfo.getRegisterphone());
                            insbInvoiceinfoDao.insert(invoice);
                        } else if (invoiceinfo.getInvoicetype() == 0) {
                            invoice = new INSBInvoiceinfo();
                            invoice.setAccountnumber(null);
                            invoice.setBankname(null);
                            invoice.setNoti(invoiceinfo.getNoti());
                            invoice.setCreatetime(date);
                            invoice.setEmail(null);
                            invoice.setIdentifynumber(null);
                            invoice.setInscomcode(inscomcode);
                            invoice.setInvoicetype(invoiceinfo.getInvoicetype());
                            invoice.setOperator(operator);
                            invoice.setRegisteraddress(null);
                            invoice.setTaskid(taskid);
                            invoice.setRegisterphone(null);
                            insbInvoiceinfoDao.insert(invoice);
                        }
                    } else {
                        if (invoiceinfo.getInvoicetype() == 1) {
                            invoice.setAccountnumber(invoiceinfo.getAccountnumber());
                            invoice.setBankname(invoiceinfo.getBankname());
                            invoice.setModifytime(date);
                            invoice.setEmail(invoiceinfo.getEmail());
                            invoice.setIdentifynumber(invoiceinfo.getIdentifynumber());
                            invoice.setInscomcode(inscomcode);
                            invoice.setInvoicetype(invoiceinfo.getInvoicetype());
                            invoice.setNoti(invoiceinfo.getNoti());
                            invoice.setOperator(operator);
                            invoice.setRegisteraddress(invoiceinfo.getRegisteraddress());
                            invoice.setTaskid(taskid);
                            invoice.setRegisterphone(invoiceinfo.getRegisterphone());
                            insbInvoiceinfoDao.updateByTaskid(invoice);
                        } else if (invoiceinfo.getInvoicetype() == 0) {
                            invoice.setAccountnumber(null);
                            invoice.setBankname(null);
                            invoice.setModifytime(date);
                            invoice.setNoti(invoiceinfo.getNoti());
                            invoice.setEmail(null);
                            invoice.setIdentifynumber(null);
                            invoice.setInscomcode(inscomcode);
                            invoice.setInvoicetype(invoiceinfo.getInvoicetype());
                            invoice.setOperator(operator);
                            invoice.setRegisteraddress(null);
                            invoice.setTaskid(taskid);
                            invoice.setRegisterphone(null);
                            insbInvoiceinfoDao.updateByTaskid(invoice);
                        }
                    }
                }
                if (ower != null) {
                    carowne.setTaskid(taskid);
                    INSBCarowneinfo car = insbCarowneinfoDao.selectOne(carowne);
                    if (car == null) {
                        carowercper = new INSBPerson();
                        carowercper.setTaskid(taskid);
                        carowercper.setName(ower.getName());
                        carowercper.setIdcardtype((StringUtil.isEmpty(ower.getCertificateType()) || "null".equals(ower
                                .getCertificateType())) ? 0 : Integer.valueOf(ower.getCertificateType()));
                        carowercper.setIdcardno(ower.getCertNumber());
                        carowercper.setCreatetime(date);
                        carowercper.setOperator(operator);
                        if ("0".equals(ower.getCertificateType()) && ower.getCertNumber().length() == 18) {
                            carowercper.setGender(ModelUtil.getGenderByIdCard(ower.getCertNumber()));
                        } else {
                            // 1-女,0-男
                            carowercper.setGender(1);
                        }
                        carowercper.setEmail(ower.getEmail());
                        carowercper.setCellphone(ower.getTel());
                        insbPersonDao.insert(carowercper);
                        carowne.setCreatetime(date);
                        carowne.setOperator(operator);
                        carowne.setPersonid(carowercper.getId());
                        insbCarowneinfoDao.insert(carowne);
                    } else {
                        carowercper = insbPersonDao.selectById(car.getPersonid());
                        carowercper.setTaskid(taskid);
                        carowercper.setName(ower.getName());
                        carowercper.setIdcardtype((StringUtil.isEmpty(ower.getCertificateType()) || "null".equals(ower
                                .getCertificateType())) ? 0 : Integer.valueOf(ower.getCertificateType()));
                        carowercper.setIdcardno(ower.getCertNumber());
                        carowercper.setEmail(ower.getEmail());
                        carowercper.setCellphone(ower.getTel());
                        insbPersonDao.updateById(carowercper);
                    }
                }
                if (beibaoren != null) {
                    insure.setTaskid(taskid);
                    INSBInsured person = insbInsrueDao.selectOne(insure);
                    if (person == null) {
                        insureper = new INSBPerson();
                        insureper.setTaskid(taskid);
                        insureper.setName(beibaoren.getName());
                        insureper.setIdcardtype((StringUtil.isEmpty(beibaoren.getCertificateType()) || "null".equals(beibaoren
                                .getCertificateType())) ? 0 : Integer.valueOf(beibaoren.getCertificateType()));
                        insureper.setIdcardno(beibaoren.getCertNumber());
                        insureper.setEmail(beibaoren.getEmail());
                        insureper.setCellphone(beibaoren.getTel());
                        insureper.setCreatetime(date);
                        insureper.setOperator(operator);
                        if ("0".equals(beibaoren.getCertificateType()) && beibaoren.getCertNumber().length() == 18) {
                            insureper.setGender(ModelUtil.getGenderByIdCard(beibaoren.getCertNumber()));
                        } else {
                            // 1-女,0-男
                            insureper.setGender(1);
                        }
                        insbPersonDao.insert(insureper);
                        insure.setCreatetime(date);
                        insure.setOperator(operator);
                        insure.setPersonid(insureper.getId());
                        insure.setTaskid(taskid);
                        insbInsrueDao.insert(insure);
                    } else {
                        if (person.getPersonid().equals(carowercper.getId())) {
                            insureper = new INSBPerson();
                            insureper.setTaskid(taskid);
                            insureper.setName(beibaoren.getName());
                            insureper.setIdcardtype((StringUtil.isEmpty(beibaoren.getCertificateType()) || "null".equals(beibaoren
									.getCertificateType())) ? 0 : Integer.valueOf(beibaoren.getCertificateType()));
                            insureper.setIdcardno(beibaoren.getCertNumber());
                            insureper.setEmail(beibaoren.getEmail());
                            insureper.setCellphone(beibaoren.getTel());
                            insureper.setCreatetime(date);
                            insureper.setOperator(operator);
                            if ("0".equals(beibaoren.getCertificateType()) && beibaoren.getCertNumber().length() == 18) {
                                insureper.setGender(ModelUtil.getGenderByIdCard(beibaoren.getCertNumber()));
                            } else {
                                // 1-女,0-男
                                insureper.setGender(1);
                            }
                            insbPersonDao.insert(insureper);
                            person.setPersonid(insureper.getId());
                            person.setTaskid(taskid);
                            person.setModifytime(new Date());
                            insbInsrueDao.updateById(person);
                        } else {
                            insureper = insbPersonDao.selectById(person.getPersonid());
                            insureper.setTaskid(taskid);
                            insureper.setName(beibaoren.getName());
                            insureper.setIdcardtype((StringUtil.isEmpty(beibaoren.getCertificateType()) || "null".equals(beibaoren.
									getCertificateType())) ? 0 : Integer.valueOf(beibaoren.getCertificateType()));
                            insureper.setIdcardno(beibaoren.getCertNumber());
                            insureper.setEmail(beibaoren.getEmail());
                            insureper.setCellphone(beibaoren.getTel());
                            insbPersonDao.updateById(insureper);
                        }
                    }
                }
                if (toubaoren != null) {
                    applicant.setTaskid(taskid);
                    INSBApplicant li = insbApplicantDao.selectOne(applicant);
                    if (li == null) {
                        applicper = new INSBPerson();
                        applicper.setTaskid(taskid);
                        applicper.setName(toubaoren.getName());
                        applicper.setIdcardtype((StringUtil.isEmpty(toubaoren.getCertificateType()) || "".equals(toubaoren
                                .getCertificateType())) ? 0 : Integer.valueOf(toubaoren.getCertificateType()));
                        applicper.setIdcardno(toubaoren.getCertNumber());
                        applicper.setCreatetime(date);
                        applicper.setOperator(operator);
                        if ("0".equals(toubaoren.getCertificateType()) && toubaoren.getCertNumber().length() == 18) {
                            applicper.setGender(ModelUtil.getGenderByIdCard(toubaoren.getCertNumber()));
                        } else {
                            // 1-女,0-男
                            applicper.setGender(1);
                        }
                        applicper.setEmail(toubaoren.getEmail());
                        applicper.setCellphone(toubaoren.getTel());
                        insbPersonDao.insert(applicper);
                        applicant.setCreatetime(date);
                        applicant.setOperator(operator);
                        applicant.setPersonid(applicper.getId());
                        applicant.setTaskid(taskid);
                        insbApplicantDao.insert(applicant);
                    } else {
                        if (li.getPersonid().equals(carowercper.getId())) {
                            applicper = new INSBPerson();
                            applicper.setTaskid(taskid);
                            applicper.setName(toubaoren.getName());
                            applicper.setIdcardtype((StringUtil.isEmpty(toubaoren.getCertificateType()) || "".equals(toubaoren
                                    .getCertificateType())) ? 0 : Integer.valueOf(toubaoren.getCertificateType()));
                            applicper.setIdcardno(toubaoren.getCertNumber());
                            applicper.setCreatetime(date);
                            applicper.setOperator(operator);
                            if ("0".equals(toubaoren.getCertificateType()) && toubaoren.getCertNumber().length() == 18) {
                                applicper.setGender(ModelUtil.getGenderByIdCard(toubaoren.getCertNumber()));
                            } else {
                                // 1-女,0-男
                                applicper.setGender(1);
                            }
                            applicper.setEmail(toubaoren.getEmail());
                            applicper.setCellphone(toubaoren.getTel());
                            insbPersonDao.insert(applicper);
                            li.setPersonid(insureper.getId());
                            li.setTaskid(taskid);
                            li.setModifytime(new Date());
                            insbApplicantDao.updateById(li);
                        } else {
                            applicper = insbPersonDao.selectById(li.getPersonid());
                            applicper.setTaskid(taskid);
                            applicper.setName(toubaoren.getName());
                            applicper.setIdcardtype((StringUtil.isEmpty(toubaoren.getCertificateType()) || "null".equals(toubaoren.
									getCertificateType())) ? 0 : Integer.valueOf(toubaoren.getCertificateType()));
                            applicper.setIdcardno(toubaoren.getCertNumber());
                            applicper.setEmail(toubaoren.getEmail());
                            applicper.setCellphone(toubaoren.getTel());
                            insbPersonDao.updateById(applicper);
                        }
                    }
                }
                // 权益索赔人
                if (quanyisuopeiren != null) {
                    insbLegalrightclaim.setTaskid(taskid);
                    INSBLegalrightclaim legalrightclaim = insbLegalrightclaimDao
                            .selectOne(insbLegalrightclaim);
                    if (legalrightclaim == null) {
                        INSBPerson legalright = new INSBPerson();
                        legalright.setTaskid(taskid);
                        legalright.setName(quanyisuopeiren.getName());
                        legalright.setIdcardtype((StringUtil.isEmpty(quanyisuopeiren.getCertificateType()) || "".equals(quanyisuopeiren.
								getCertificateType())) ? 0 : Integer.valueOf(quanyisuopeiren.getCertificateType()));
                        legalright.setIdcardno(quanyisuopeiren.getCertNumber());
                        legalright.setCreatetime(date);
                        legalright.setOperator(operator);
                        if ("0".equals(quanyisuopeiren.getCertificateType()) && quanyisuopeiren.getCertNumber().length() == 18) {
                            legalright.setGender(ModelUtil.getGenderByIdCard(quanyisuopeiren.getCertNumber()));
                        } else {
                            // 1-女,0-男
                            legalright.setGender(1);
                        }
                        legalright.setEmail(quanyisuopeiren.getEmail());
                        legalright.setCellphone(quanyisuopeiren.getTel());
                        insbPersonDao.insert(legalright);
                        INSBLegalrightclaim legalrightclaim2 = new INSBLegalrightclaim();
                        legalrightclaim2.setCreatetime(date);
                        legalrightclaim2.setOperator(operator);
                        legalrightclaim2.setPersonid(legalright.getId());
                        legalrightclaim2.setTaskid(taskid);
                        insbLegalrightclaimDao.insert(legalrightclaim2);
                    }
                }

                if (cl != null) {
                    carinfo.setTaskid(taskid);
                    INSBCarinfo info = insbCarinfoDao.selectOne(carinfo);
                    if (info == null) {
                        carinfo.setStandardfullname(cl.getModelCode());
                        carinfo.setEngineno(cl.getEngineNo());
                        carinfo.setVincode(cl.getVin());
                        carinfo.setIsTransfercar(cl.isIstransfer() ? "1" : "0");
                        carinfo.setTransferdate((cl.getChgOwnerDate() == null
                                || "".equals(cl.getChgOwnerDate()) ? null : ModelUtil.conbertStringToNyrDate(cl.getChgOwnerDate())));
                        carinfo.setRegistdate((cl.getFirstRegisterDate() == null
                                || "".equals(cl.getFirstRegisterDate()) ? null : ModelUtil.conbertStringToNyrDate(cl.getFirstRegisterDate())));
                        // 上年商业承保公司
                        carinfo.setPreinscode(lastyearcomid);
                        carinfo.setDrivingarea(region);
                        carinfo.setProperty(cl.getProperty());
                        carinfo.setCarproperty(cl.getCarproperty());
                        insbCarinfoDao.insert(carinfo);
                        LogUtil.info("INSBCarinfo|报表数据埋点|"+JSONObject.fromObject(carinfo).toString());
                    } else {
                        info.setStandardfullname(cl.getModelCode());
                        info.setEngineno(cl.getEngineNo());
                        info.setVincode(cl.getVin());
                        info.setIsTransfercar(cl.isIstransfer() ? "1" : "0");
                        info.setTransferdate((cl.getChgOwnerDate() == null
                                || "".equals(cl.getChgOwnerDate()) ? null : ModelUtil.conbertStringToNyrDate(cl.getChgOwnerDate())));
                        info.setRegistdate((cl.getFirstRegisterDate() == null
                                || "".equals(cl.getFirstRegisterDate()) ? null : ModelUtil.conbertStringToNyrDate(cl.getFirstRegisterDate())));
                        // 上年商业承保公司
                        info.setPreinscode(lastyearcomid);
                        info.setDrivingarea(region);
                        info.setProperty(cl.getProperty());
                        info.setCarproperty(cl.getCarproperty());
                        insbCarinfoDao.updateById(info);
                        LogUtil.info("INSBCarinfo|报表数据埋点|"+JSONObject.fromObject(info).toString());
                    }
                }
                for (INSBPolicyitem insbPolicyitem : insbPolicyitems) {
                    insbPolicyitem.setApplicantid(applicper.getId());
                    insbPolicyitem.setApplicantname(applicper.getName());
                    insbPolicyitem.setInsuredid(insureper.getId());
                    insbPolicyitem.setApplicantname(insureper.getName());
                    insbPolicyitem.setCarownerid(carowercper.getId());
                    insbPolicyitem.setCarownername(carowercper.getName());
                    if ("0".equals(insbPolicyitem.getRisktype())) {
                        if (bu != null) {
                            insbPolicyitem.setStartdate(ModelUtil.conbertStringToNyrDate(bu.getRiskstartdate()));
                            insbPolicyitem.setEnddate(ModelUtil.conbertStringToNyrDate(bu.getRiskenddate()));
                        }
                    } else {
                        if (trbu != null) {
                            insbPolicyitem.setStartdate(ModelUtil.conbertStringToNyrDate(trbu.getRiskstartdate()));
                            insbPolicyitem.setEnddate(ModelUtil.conbertStringToNyrDate(trbu.getRiskenddate()));
                        }
                    }
                    insbPolicyitemDao.updateById(insbPolicyitem);
                }

                //向his表中插入数据，是否与车主保持一致以证件号是否相同做判断 2016-04-16
                LogUtil.info("开始向his表插入人员信息" + bean.getProcessinstanceid() + "===修改的供应商的id=" + bean.getInscomcode());
                saveOrUpdatePeopleInfoToHis(beibaoren, toubaoren, quanyisuopeiren, bean.getInscomcode(), taskid, operator, ower.getCertNumber());
                LogUtil.info("向his表插入人员信息完成" + bean.getProcessinstanceid() + "===修改的供应商的id=" + bean.getInscomcode());
            }

			INSBWorkflowmain mainModel = insbWorkflowmainDao.selectByInstanceId(taskid);
			if (null == mainModel) {
				commonModel.setStatus("fail");
				commonModel.setMessage("获取操作人失败");
				return commonModel;
			}
			// 获取重新报价公司的的子流程id
			String workflowinstanceid = "";
			Map<String, String> map = new HashMap<String, String>();
			map.put("taskid", taskid);
			map.put("inscomcode", bean.getInscomcode());
			INSBQuoteinfo insbQuoteinfo = appInsuredQuoteDao.selectInsbQuoteInfoByTaskidAndPid(map);
			if (null != insbQuoteinfo) {
				workflowinstanceid = insbQuoteinfo.getWorkflowinstanceid();
			}
			if (StringUtil.isEmpty(workflowinstanceid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("报价信息表中信息不存在");
				return commonModel;
			}

			//插入备注信息
			LogUtil.info("插入备注信息"+taskid+","+workflowinstanceid+"=="+bean.getRemark());
			if(!StringUtil.isEmpty(bean.getRemark())){
				saveRemarkInTabSubtrack(taskid, bean.getRemark(), operator, bean.getRemarkcode(), workflowinstanceid, bean.getInscomcode());
				// 判断备注类型是否需要转人工
				LogUtil.info("saveRemarkInTab 重新提交=" + taskid + "代理人选择的备注类型=" + bean.getRemarkcode());
				Map<String, Object> map1 = isNeedToManMade(bean.getRemarkcode());
				if (null != map1 && (boolean) map1.get("flag")) {
					saveFlowerrorToManWork(taskid,insbQuoteinfo.getInscomcode(), "代理人录入备注类型（"+ map1.get("result").toString() + "）,备注内容（"+ bean.getRemark() + "）,转人工处理","8");
				}
			}

			LogUtil.info("修改投保信息调用工作流开始" + bean.getProcessinstanceid()
					+ "===传入节点标识码=" + flowflag + "===修改的供应商的id=" + bean.getInscomcode());

			if (!StringUtil.isEmpty(flowflag)) {
				if ("0".equals(flowflag)) {// 报价修改投保信息
					// 删除报价信息表中写入的价格
					deleteSuccessQuotePrice(insbQuoteinfo, taskid, bean.getInscomcode());
					final String subid = workflowinstanceid;
					taskthreadPool4workflow.execute(new Runnable() {
						@Override
						public void run() {
							WorkflowFeedbackUtil.setWorkflowFeedback(taskid, subid, "14", "Completed", "选择投保", WorkflowFeedbackUtil.quote_redo, "admin");
							Map<String, Object> datamap = new HashMap<String, Object>();
							//调用规则判断承保限制条件
							datamap = getPriceParamWay(datamap, taskid, inscomcode, "0");
							
							WorkFlowUtil.completeTaskWorkflowRecheck("1",insbQuoteinfo.getWorkflowinstanceid(), mainModel.getOperator(), "选择投保",datamap);
//							WorkFlowUtil.updateInsuredInfoNoticeWorkflow(insbQuoteinfo.getWorkflowinstanceid(), mainModel.getOperator(), "选择投保", "1");
						}
					});

				} else if ("1".equals(flowflag)) {// 核保退回修改投保信息
					LogUtil.info("核保退回-->人工核保workflowinstanceid="+insbQuoteinfo.getWorkflowinstanceid());

					/*taskthreadPool4workflow.execute(new Runnable() {
						@Override
						public void run() {
							WorkFlowUtil.updateInsuredInfoNoticeWorkflow(
									insbQuoteinfo.getWorkflowinstanceid(), mainModel.getOperator(),
									"核保退回", "");
						}
					});*/

					workflowsubService.updateWorkFlowSubData(null, insbQuoteinfo.getWorkflowinstanceid(), "19", "Completed", "核保退回", "修改提交", "admin");
					workflowsubService.updateWorkFlowSubData(null, insbQuoteinfo.getWorkflowinstanceid(), "18", "Reserved", "人工核保", "", null);
					
					taskthreadPool4workflow.execute(new Runnable() {
						@Override
						public void run() {
							Task task = new Task();
							task.setProInstanceId(taskid);
							task.setSonProInstanceId(insbQuoteinfo.getWorkflowinstanceid());
							task.setPrvcode(insbQuoteinfo.getInscomcode());
							task.setTaskTracks("admin");
							task.setTaskName("人工核保");
							task.setTaskcode("18");
							//调度任务
							Pool.addOrUpdate(task);
							try {
								Thread.sleep(2000);
								dispatchService.dispatchTask(task);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});

				} else if ("2".equals(flowflag)) {
					// 删除报价信息表中写入的价格
					deleteSuccessQuotePrice(insbQuoteinfo, taskid, inscomcode);

					INSBCarmodelinfohis carmodelinfohis = insbCarmodelinfohisDao.selectModelInfoByTaskIdAndPrvId(taskid, inscomcode);
					if(carmodelinfohis == null || StringUtil.isEmpty(carmodelinfohis.getJyCode())){
						LogUtil.info("报价退回修改提交%s无车型code===被修改供应商=%s===操作人=%s===重新匹配年款开始=", taskid, inscomcode, operator);
						saveCarmodelinfoToData(taskid, inscomcode, workflowinstanceid);
					}

					final String subid = workflowinstanceid;
					taskthreadPool4workflow.execute(new Runnable() {
						@Override
						public void run() {
							WorkflowFeedbackUtil.setWorkflowFeedback(taskid, subid, "13", "Completed", "报价退回", "修改提交", "admin");
							Map<String, Object> datamap = new HashMap<String, Object>();
							//调用规则判断承保限制条件
							datamap = getPriceParamWay(datamap, taskid, inscomcode, "0");
							WorkFlowUtil.completeTaskWorkflowRecheck("1",insbQuoteinfo.getWorkflowinstanceid(), mainModel.getOperator(), "报价退回",datamap);
//							WorkFlowUtil.updateInsuredInfoNoticeWorkflow(insbQuoteinfo.getWorkflowinstanceid(), mainModel.getOperator(), "报价退回", "");
						}
					});

				} else {
					final String subid = workflowinstanceid;
					taskthreadPool4workflow.execute(new Runnable() {
						@Override
						public void run() {
							WorkflowFeedbackUtil.setWorkflowFeedback(taskid, subid, "51", "Completed", "承保政策限制", "修改提交", "admin");
							Map<String, Object> datamap = new HashMap<String, Object>();
							datamap = getPriceParamWay(datamap, taskid, insbQuoteinfo.getInscomcode(), "0");
							WorkFlowUtil.resquestQuotationToWorkflow(insbQuoteinfo.getWorkflowinstanceid(), "承保政策限制",datamap);
						}
					});
				}
				LogUtil.info("修改投保信息调用工作流结束" + bean.getProcessinstanceid());
			} else {
				LogUtil.info(bean.getProcessinstanceid()+ "--------获取报价状态失败了--------" + workflowinstanceid);
			}

			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

    public String saveRemarkInTabSubtrack(String taskid, String inscomcode, String remark, String remarkcode, String operator) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("taskid", taskid);
        map.put("inscomcode", inscomcode);
        INSBQuoteinfo insbQuoteinfo = appInsuredQuoteDao.selectInsbQuoteInfoByTaskidAndPid(map);

        String workflowinstanceid = "";
        if (null != insbQuoteinfo) {
            workflowinstanceid = insbQuoteinfo.getWorkflowinstanceid();
			operator = insbQuoteinfo.getOperator();//原本写死admin操作人，现改为取报价信息表操作人
        }

        saveRemarkInTabSubtrack(taskid, remark, operator, remarkcode, workflowinstanceid, inscomcode);
        return workflowinstanceid;
    }

    /**
	 * 添加子流程备注
	 * @param taskid
	 * @param remark
	 * @param operator
	 * @param remarkcode
	 * @param workflowinstanceid
	 * @param inscomcode
	 */
	private void saveRemarkInTabSubtrack(String taskid, String remark,
			String operator, String remarkcode, String workflowinstanceid, String inscomcode) {
		INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();
		insbWorkflowsub.setMaininstanceid(taskid);
		insbWorkflowsub.setInstanceid(workflowinstanceid);
		INSBWorkflowsub workflowsub = insbWorkflowsubDao.selectOne(insbWorkflowsub);
		if(null != workflowsub){
			INSBWorkflowsubtrack insbWorkflowsubtrack = new INSBWorkflowsubtrack();
			insbWorkflowsubtrack.setMaininstanceid(taskid);
			insbWorkflowsubtrack.setInstanceid(workflowinstanceid);
			insbWorkflowsubtrack.setTaskcode(workflowsub.getTaskcode());
			INSBWorkflowsubtrack workflowsubtrack = insbWorkflowsubtrackDao.selectOne(insbWorkflowsubtrack);
			if(null != workflowsubtrack && StringUtil.isNotEmpty(workflowsubtrack.getId())){
				INSBUsercomment insbUsercomment = new INSBUsercomment();
				insbUsercomment.setTrackid(workflowsubtrack.getId());//任务轨迹id
				insbUsercomment.setTracktype(2);//1：主流程 2：子流程
				INSBUsercomment usercomment = insbUsercommentDao.selectOne(insbUsercomment);
				if(null == usercomment){
					usercomment = new INSBUsercomment();
					usercomment.setOperator(operator);
					usercomment.setCreatetime(new Date());
					usercomment.setModifytime(new Date());
					usercomment.setTrackid(workflowsubtrack.getId());
					usercomment.setTracktype(2);
					usercomment.setCommentcontent(remark);
					usercomment.setCommentcontenttype(StringUtil.isEmpty(remarkcode)?0:Integer.parseInt(remarkcode));
					usercomment.setCommenttype(1);

					insbUsercommentDao.insert(usercomment);
                    LogUtil.info(taskid+","+workflowinstanceid+","+workflowsub.getTaskcode()+","+workflowsubtrack.getId()+"新增usercomment("+remarkcode+")");
				}else{
                    usercomment.setOperator(operator);
                    usercomment.setModifytime(new Date());
                    usercomment.setTrackid(workflowsubtrack.getId());
                    usercomment.setTracktype(2);
                    usercomment.setCommentcontent(remark);
                    usercomment.setCommentcontenttype(StringUtil.isEmpty(remarkcode)?0:Integer.parseInt(remarkcode));
                    usercomment.setCommenttype(1);

					insbUsercommentDao.updateById(usercomment);
                    LogUtil.info(taskid+","+workflowinstanceid+","+workflowsub.getTaskcode()+","+workflowsubtrack.getId()+"修改usercomment("+remarkcode+")");
				}
			} else {
                LogUtil.info(taskid+","+workflowinstanceid+","+workflowsub.getTaskcode()+"的workflowsubtrack数据为空");
            }
		} else {
            LogUtil.info(taskid + "," + workflowinstanceid + "的workflowsub数据为空");
        }
	}

	/**
	 * 向his表中插入数据，是否与车主保持一致以证件号是否相同做判断 2016-04-16
	 * @param beibaoren
	 * @param toubaoren
	 * @param quanyisuopeiren
	 * @param inscomcode
	 * @param taskid
	 */
	private void saveOrUpdatePeopleInfoToHis(AppPerson beibaoren, AppPerson toubaoren, AppPerson quanyisuopeiren,String inscomcode,String taskid,String operator,String carowerno) {
		INSBCarowneinfo insbCarowneinfo = new INSBCarowneinfo();
		insbCarowneinfo.setTaskid(taskid);
		INSBCarowneinfo carowneinfo = insbCarowneinfoDao.selectOne(insbCarowneinfo);
		if(null != carowneinfo && !StringUtil.isEmpty(carowerno)){
			//被保人his表插入数据
			INSBInsuredhis insbInsuredhis = new INSBInsuredhis();
			insbInsuredhis.setTaskid(taskid);
			insbInsuredhis.setInscomcode(inscomcode);
			INSBInsuredhis insuredhis = insbInsuredhisDao.selectOne(insbInsuredhis);
			if(null == insuredhis){
				/*//证件号相同默认与车主一致
				if(carowerno.equals(beibaoren.getCertNumber())){
					LogUtil.info("his表数据不存在同车主"+taskid+"单方修改his表插入数据");
					insuredhis = new INSBInsuredhis();
					insuredhis.setCreatetime(new Date());
					insuredhis.setOperator(operator);
					insuredhis.setTaskid(taskid);
					insuredhis.setInscomcode(inscomcode);
					insuredhis.setPersonid(carowneinfo.getPersonid());
					insbInsuredhisDao.insert(insuredhis);
				}else{*/
					LogUtil.info("his表数据不存在与车主不同"+taskid+"单方修改his表插入数据");
					//person表插入新数据
					INSBPerson insureper = new INSBPerson();
					insureper.setTaskid(taskid);
					insureper.setName(beibaoren.getName());
					insureper.setIdcardtype((StringUtil.isEmpty(beibaoren
							.getCertificateType()) || "null"
							.equals(beibaoren.getCertificateType())) ? 0
							: Integer.valueOf(beibaoren
									.getCertificateType()));
					insureper.setIdcardno(beibaoren.getCertNumber());
					insureper.setEmail(beibaoren.getEmail());
					insureper.setCellphone(beibaoren.getTel());
					insureper.setCreatetime(new Date());
					insureper.setOperator(operator);
					if ("0".equals(beibaoren.getCertificateType())
							&& beibaoren.getCertNumber().length() == 18) {
						insureper.setGender(ModelUtil
								.getGenderByIdCard(beibaoren
                                        .getCertNumber()));
					} else {
						// 1-女,0-男
						insureper.setGender(1);
					}
					insbPersonDao.insert(insureper);

					insuredhis = new INSBInsuredhis();
					insuredhis.setCreatetime(new Date());
					insuredhis.setOperator(operator);
					insuredhis.setTaskid(taskid);
					insuredhis.setInscomcode(inscomcode);
					insuredhis.setPersonid(insureper.getId());
					insbInsuredhisDao.insert(insuredhis);
				//}
			}else{
				/*//证件号相同默认与车主一致
				if(carowerno.equals(beibaoren.getCertNumber())){
					LogUtil.info("his表数据存在同车主"+taskid+"单方修改his表插入数据");
					insuredhis.setModifytime(new Date());
					insuredhis.setOperator(operator);
					insuredhis.setTaskid(taskid);
					insuredhis.setInscomcode(inscomcode);
					insuredhis.setPersonid(carowneinfo.getPersonid());
					insbInsuredhisDao.updateById(insuredhis);
				}else{*/
					LogUtil.info("his表数据存在不同车主"+taskid+"单方修改his表插入数据");
					//person表插入新数据
					INSBPerson insureper = insbPersonDao.selectById(insuredhis.getPersonid());
					insureper.setTaskid(taskid);
					insureper.setName(beibaoren.getName());
					insureper.setIdcardtype((StringUtil.isEmpty(beibaoren
							.getCertificateType()) || "null"
							.equals(beibaoren.getCertificateType())) ? 0
							: Integer.valueOf(beibaoren
									.getCertificateType()));
					insureper.setIdcardno(beibaoren.getCertNumber());
					insureper.setEmail(beibaoren.getEmail());
					insureper.setCellphone(beibaoren.getTel());
					insureper.setModifytime(new Date());
					insureper.setOperator(operator);
					if ("0".equals(beibaoren.getCertificateType())
							&& beibaoren.getCertNumber().length() == 18) {
						insureper.setGender(ModelUtil
								.getGenderByIdCard(beibaoren
                                        .getCertNumber()));
					} else {
						// 1-女,0-男
						insureper.setGender(1);
					}
					insbPersonDao.updateById(insureper);
				//}
			}

			//投保人his表插入数据
			INSBApplicanthis insbApplicanthis = new INSBApplicanthis();
			insbApplicanthis.setTaskid(taskid);
			insbApplicanthis.setInscomcode(inscomcode);
			INSBApplicanthis applicanthis = insbApplicanthisDao.selectOne(insbApplicanthis);
			if(null == applicanthis){
				/*//证件号相同默认与车主一致
				if(carowerno.equals(toubaoren.getCertNumber())){
					LogUtil.info("his表数据不存在同车主"+taskid+"单方修改his表插入数据");
					applicanthis = new INSBApplicanthis();
					applicanthis.setCreatetime(new Date());
					applicanthis.setOperator(operator);
					applicanthis.setTaskid(taskid);
					applicanthis.setInscomcode(inscomcode);
					applicanthis.setPersonid(carowneinfo.getPersonid());
					insbApplicanthisDao.insert(applicanthis);
				}else{*/
					LogUtil.info("his表数据不存在与车主不同"+taskid+"单方修改his表插入数据");
					//person表插入新数据
					INSBPerson insureper = new INSBPerson();
					insureper.setTaskid(taskid);
					insureper.setName(toubaoren.getName());
					insureper.setIdcardtype((StringUtil.isEmpty(toubaoren
							.getCertificateType()) || "null"
							.equals(toubaoren.getCertificateType())) ? 0
							: Integer.valueOf(toubaoren
									.getCertificateType()));
					insureper.setIdcardno(toubaoren.getCertNumber());
					insureper.setEmail(toubaoren.getEmail());
					insureper.setCellphone(toubaoren.getTel());
					insureper.setCreatetime(new Date());
					insureper.setOperator(operator);
					if ("0".equals(toubaoren.getCertificateType())
							&& toubaoren.getCertNumber().length() == 18) {
						insureper.setGender(ModelUtil
								.getGenderByIdCard(toubaoren
                                        .getCertNumber()));
					} else {
						// 1-女,0-男
						insureper.setGender(1);
					}
					insbPersonDao.insert(insureper);

					applicanthis = new INSBApplicanthis();
					applicanthis.setCreatetime(new Date());
					applicanthis.setOperator(operator);
					applicanthis.setTaskid(taskid);
					applicanthis.setInscomcode(inscomcode);
					applicanthis.setPersonid(insureper.getId());
					insbApplicanthisDao.insert(applicanthis);
				//}
			}else{
				/*//证件号相同默认与车主一致
				if(carowerno.equals(toubaoren.getCertNumber())){
					LogUtil.info("his表数据存在同车主"+taskid+"单方修改his表插入数据");
					applicanthis.setModifytime(new Date());
					applicanthis.setOperator(operator);
					applicanthis.setTaskid(taskid);
					applicanthis.setInscomcode(inscomcode);
					applicanthis.setPersonid(carowneinfo.getPersonid());
					insbApplicanthisDao.updateById(applicanthis);
				}else{*/
					LogUtil.info("his表数据存在不同车主"+taskid+"单方修改his表插入数据");
					//person表插入新数据
					INSBPerson insureper = insbPersonDao.selectById(applicanthis.getPersonid());
					insureper.setTaskid(taskid);
					insureper.setName(toubaoren.getName());
					insureper.setIdcardtype((StringUtil.isEmpty(toubaoren
							.getCertificateType()) || "null"
							.equals(toubaoren.getCertificateType())) ? 0
							: Integer.valueOf(toubaoren
									.getCertificateType()));
					insureper.setIdcardno(toubaoren.getCertNumber());
					insureper.setEmail(toubaoren.getEmail());
					insureper.setCellphone(toubaoren.getTel());
					insureper.setModifytime(new Date());
					insureper.setOperator(operator);
					if ("0".equals(toubaoren.getCertificateType())
							&& toubaoren.getCertNumber().length() == 18) {
						insureper.setGender(ModelUtil
								.getGenderByIdCard(toubaoren
                                        .getCertNumber()));
					} else {
						// 1-女,0-男
						insureper.setGender(1);
					}
					insbPersonDao.updateById(insureper);
				//}
			}

			//权益索赔人his表插入数据
			INSBLegalrightclaimhis insbLegalrightclaimhis = new INSBLegalrightclaimhis();
			insbLegalrightclaimhis.setTaskid(taskid);
			insbLegalrightclaimhis.setInscomcode(inscomcode);
			INSBLegalrightclaimhis legalrightclaimhis = insbLegalrightclaimhisDao.selectOne(insbLegalrightclaimhis);
			if(null == legalrightclaimhis){
				/*//证件号相同默认与车主一致
				if(carowerno.equals(quanyisuopeiren.getCertNumber())){
					LogUtil.info("his表数据不存在同车主"+taskid+"单方修改his表插入数据");
					legalrightclaimhis = new INSBLegalrightclaimhis();
					legalrightclaimhis.setCreatetime(new Date());
					legalrightclaimhis.setOperator(operator);
					legalrightclaimhis.setTaskid(taskid);
					legalrightclaimhis.setInscomcode(inscomcode);
					legalrightclaimhis.setPersonid(carowneinfo.getPersonid());
					insbLegalrightclaimhisDao.insert(legalrightclaimhis);
				}else{*/
					LogUtil.info("his表数据不存在与车主不同"+taskid+"单方修改his表插入数据");
					//person表插入新数据
					INSBPerson insureper = new INSBPerson();
					insureper.setTaskid(taskid);
					insureper.setName(quanyisuopeiren.getName());
					insureper.setIdcardtype((StringUtil.isEmpty(quanyisuopeiren
							.getCertificateType()) || "null"
							.equals(quanyisuopeiren.getCertificateType())) ? 0
							: Integer.valueOf(quanyisuopeiren
									.getCertificateType()));
					insureper.setIdcardno(quanyisuopeiren.getCertNumber());
					insureper.setEmail(quanyisuopeiren.getEmail());
					insureper.setCellphone(quanyisuopeiren.getTel());
					insureper.setCreatetime(new Date());
					insureper.setOperator(operator);
					if ("0".equals(quanyisuopeiren.getCertificateType())
							&& quanyisuopeiren.getCertNumber().length() == 18) {
						insureper.setGender(ModelUtil
								.getGenderByIdCard(quanyisuopeiren
                                        .getCertNumber()));
					} else {
						// 1-女,0-男
						insureper.setGender(1);
					}
					insbPersonDao.insert(insureper);

					legalrightclaimhis = new INSBLegalrightclaimhis();
					legalrightclaimhis.setCreatetime(new Date());
					legalrightclaimhis.setOperator(operator);
					legalrightclaimhis.setTaskid(taskid);
					legalrightclaimhis.setInscomcode(inscomcode);
					legalrightclaimhis.setPersonid(insureper.getId());
					insbLegalrightclaimhisDao.insert(legalrightclaimhis);
				//}
			}else{
				/*//证件号相同默认与车主一致
				if(carowerno.equals(quanyisuopeiren.getCertNumber())){
					LogUtil.info("his表数据存在同车主"+taskid+"单方修改his表插入数据");
					legalrightclaimhis.setModifytime(new Date());
					legalrightclaimhis.setOperator(operator);
					legalrightclaimhis.setTaskid(taskid);
					legalrightclaimhis.setInscomcode(inscomcode);
					legalrightclaimhis.setPersonid(carowneinfo.getPersonid());
					insbLegalrightclaimhisDao.updateById(legalrightclaimhis);
				}else{*/
					LogUtil.info("his表数据存在不同车主"+taskid+"单方修改his表插入数据");
					//person表插入新数据
					INSBPerson insureper = insbPersonDao.selectById(legalrightclaimhis.getPersonid());
					insureper.setTaskid(taskid);
					insureper.setName(quanyisuopeiren.getName());
					insureper.setIdcardtype((StringUtil.isEmpty(quanyisuopeiren.getCertificateType()) ||
							"null".equals(quanyisuopeiren.getCertificateType())) ? 0
							: Integer.valueOf(quanyisuopeiren.getCertificateType()));
					insureper.setIdcardno(quanyisuopeiren.getCertNumber());
					insureper.setEmail(quanyisuopeiren.getEmail());
					insureper.setCellphone(quanyisuopeiren.getTel());
					insureper.setModifytime(new Date());
					insureper.setOperator(operator);
					if ("0".equals(quanyisuopeiren.getCertificateType())
							&& quanyisuopeiren.getCertNumber().length() == 18) {
						insureper.setGender(ModelUtil
								.getGenderByIdCard(quanyisuopeiren
                                        .getCertNumber()));
					} else {
						// 1-女,0-男
						insureper.setGender(1);
					}
					insbPersonDao.updateById(insureper);
				//}
			}
		}
	}
	/**
	 * 判断报价信息表是否已包含子流程id，包好不做保存动作
	 * @param taskid
	 * @return true 已生成子流程id
	 */
	private boolean isHaveWorkflowSubid(String taskid){
		boolean result = false;
		List<INSBQuoteinfo> insbQuoteinfos = appInsuredQuoteDao.getSelectedProvidersByTaskid(taskid);
		if(null != insbQuoteinfos && insbQuoteinfos.size() > 0){
			for(INSBQuoteinfo insbQuoteinfo : insbQuoteinfos){
				if(!StringUtil.isEmpty(insbQuoteinfo.getWorkflowinstanceid())){
					result = true;
					break;
				}
			}
		}
		return result;
	}

	@Override
	public CommonModel insuredConfig(InsuredConfigModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if (StringUtil.isEmpty(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			LogUtil.info("insuredConfig==taskid:"+taskid+",商业险起保日期="+model.getSystartdate()+",交强险起保日期="+model.getJqstartdate());
			//防止重复提交保险配置
			if(isHaveWorkflowSubid(taskid)){
				commonModel.setStatus("success");
				commonModel.setMessage("报价已生成请勿重复提交");
				commonModel.setBody("repeated");
				return commonModel;
			}
			//更新证件号,值更新被保人的证件号  20160121   更新车主证件号 2016-03-29
			if (model.getCertNumber()!=null&&!StringUtil.isEmpty(model.getCertNumber().trim())) {
				int gender = 0;
				if(model.getCertNumber().length() == 18&&"0".equals(model.getCertificateType())){
					gender = ModelUtil.getGenderByIdCard(model.getCertNumber());
				}
				int idtype = StringUtil.isEmpty(model.getCertificateType())?0:Integer.parseInt(model.getCertificateType());
				INSBCarowneinfo insbCarowneinfo = new INSBCarowneinfo();
				insbCarowneinfo.setTaskid(taskid);
				INSBCarowneinfo carowneinfo = insbCarowneinfoDao.selectOne(insbCarowneinfo);
				if(null != carowneinfo){
					INSBPerson insbPerson = insbPersonDao.selectById(carowneinfo.getPersonid());
					if(null != insbPerson){
						insbPerson.setIdcardno(model.getCertNumber().trim());
						insbPerson.setIdcardtype(idtype);
						insbPerson.setGender(gender);
						insbPersonDao.updateById(insbPerson);
					}
				}

				INSBInsured insbInsured = new INSBInsured();
				insbInsured.setTaskid(taskid);
				INSBInsured insured = insbInsuredDao.selectOne(insbInsured);
				if(null != insured){
					INSBPerson insbPerson = insbPersonDao.selectById(insured.getPersonid());
					if(null != insbPerson && StringUtil.isEmpty(insbPerson.getIdcardno())){
						insbPerson.setIdcardno(model.getCertNumber().trim());
						insbPerson.setIdcardtype(idtype);
						insbPerson.setGender(gender);
						insbPersonDao.updateById(insbPerson);
					}
				}
				INSBApplicant insbApplicant = new INSBApplicant();
				insbApplicant.setTaskid(taskid);
				INSBApplicant applicant = insbApplicantDao.selectOne(insbApplicant);
				if(null != applicant){
					INSBPerson insbPerson = insbPersonDao.selectById(applicant.getPersonid());
					if(null != insbPerson && StringUtil.isEmpty(insbPerson.getIdcardno())){
						insbPerson.setIdcardno(model.getCertNumber().trim());
						insbPerson.setIdcardtype(idtype);
						insbPerson.setGender(gender);
						insbPersonDao.updateById(insbPerson);
					}
				}
                INSBLegalrightclaim insbLegalrightclaim = new INSBLegalrightclaim();
                insbLegalrightclaim.setTaskid(taskid);
                INSBLegalrightclaim legalrightclaim = insbLegalrightclaimDao.selectOne(insbLegalrightclaim);
                if(null != legalrightclaim){
                    INSBPerson insbPerson = insbPersonDao.selectById(legalrightclaim.getPersonid());
                    if(null != insbPerson && StringUtil.isEmpty(insbPerson.getIdcardno())){
                        insbPerson.setIdcardno(model.getCertNumber().trim());
                        insbPerson.setIdcardtype(idtype);
                        insbPerson.setGender(gender);
                        insbPersonDao.updateById(insbPerson);
                    }
                }
                INSBRelationperson insbRelationperson = new INSBRelationperson();
                insbRelationperson.setTaskid(taskid);
                INSBRelationperson relationperson = insbRelationpersonDao.selectOne(insbRelationperson);
                if(null != relationperson){
                    INSBPerson insbPerson = insbPersonDao.selectById(relationperson.getPersonid());
                    if(null != insbPerson && StringUtil.isEmpty(insbPerson.getIdcardno())){
                        insbPerson.setIdcardno(model.getCertNumber().trim());
                        insbPerson.setIdcardtype(idtype);
                        insbPerson.setGender(gender);
                        insbPersonDao.updateById(insbPerson);
                    }
                }

			}
			//修改投保车价
			if(!StringUtil.isEmpty(model.getPolicycarprice())){
				INSBCarinfo insbCarinfo = new INSBCarinfo();
				insbCarinfo.setTaskid(taskid);
				INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
				if (null == carinfo) {
					commonModel.setStatus("fail");
					commonModel.setMessage("车辆信息不存在");
					return commonModel;
				}
				INSBCarmodelinfo insbCarmodelinfo = new INSBCarmodelinfo();
				insbCarmodelinfo.setCarinfoid(carinfo.getId());
				INSBCarmodelinfo carmodelinfo = insbCarmodelinfoDao.selectOne(insbCarmodelinfo);
				if (null == carmodelinfo) {
					commonModel.setStatus("fail");
					commonModel.setMessage("车型信息不存在");
					return commonModel;
				}
				//调整车价有值改为自定义
				carmodelinfo.setCarprice("2");
				carmodelinfo.setPolicycarprice(model.getPolicycarprice());
				insbCarmodelinfoDao.updateById(carmodelinfo);
			}

			// 已选择的报价公司（报价信息总表，报价信息表）
			INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
			quotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo insbQuotetotalinfo = insbQuotetotalinfoDao
					.selectOne(quotetotalinfo);
			if (null == insbQuotetotalinfo) {
				commonModel.setStatus("fail");
				commonModel.setMessage("报价信息总表不存在");
				return commonModel;
			}

			INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
			insbQuoteinfo.setQuotetotalinfoid(insbQuotetotalinfo.getId());
			List<INSBQuoteinfo> insbQuoteinfos = insbQuoteinfoDao
					.selectList(insbQuoteinfo);
			if (null == insbQuoteinfos || insbQuoteinfos.size() <= 0) {
				commonModel.setStatus("fail");
				commonModel.setMessage("没选择报价公司");
				return commonModel;
			}
			List<String> proids = new ArrayList<String>();
			for(INSBQuoteinfo quoteinfo : insbQuoteinfos){
				proids.add(quoteinfo.getInscomcode());
			}
			//保存身份证正面照
			if(!StringUtil.isEmpty(model.getFileid())){
				saveFileImageId(taskid, model.getFileid());
			}
			//保存保险配置
			LogUtil.info("首次保存保险配置=insuredConfigtaskid:"+taskid+"===供应商列表:"+proids+"===操作人："+insbQuotetotalinfo.getOperator());
			saveInsuredConfig(taskid, insbQuotetotalinfo.getOperator(), model.getBusinessRisks(), model.getStrongRisk(), proids, model.getRemark(), model.getPlankey(), model.getSystartdate(), model.getJqstartdate());
			//保存备注信息  20160623 bug2668注意需要转人工的备注
			if(!StringUtil.isEmpty(model.getRemark())){
				saveRemarkInTab(taskid, model.getRemark(), insbQuotetotalinfo.getOperator(), model.getRemarkcode());
			}
			//特殊险别处理
			//specialInsureconfDeal(taskid,model,insbQuotetotalinfo.getOperator(),proids);
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	/**
	 * 特殊险种处理，误删
	 * @param taskid
	 * @param model
	 * @param operator
	 * @param proids
	 */
	public void specialInsureconfDeal(String taskid, InsuredConfigModel model,String operator,List<String> proids) {
		List<INSBSpecialkindconfig> insbSpecialkindconfigs = new ArrayList<INSBSpecialkindconfig>();
		List<BusinessRisks> businessRisks = model.getBusinessRisks();
		// 商业险
		if (null != businessRisks && businessRisks.size() > 0) {
			for(BusinessRisks risks : businessRisks){
				String value = getValueFromCode(risks.getCode());
				if(!StringUtil.isEmpty(value)){
					List<NewEquipmentInsBean> beans = model.getEquipmentInsBeans();
					if("04".equals(value) && null != beans && beans.size() > 0){
						for(NewEquipmentInsBean bean : beans){
							INSBSpecialkindconfig insbSpecialkindconfig = new INSBSpecialkindconfig();
							insbSpecialkindconfig.setCreatetime(new Date());
							insbSpecialkindconfig.setOperator(operator);
							insbSpecialkindconfig.setTaskid(taskid);
							insbSpecialkindconfig.setTypecode("04");
							insbSpecialkindconfig.setKindcode(risks.getCode());
							insbSpecialkindconfig.setCodekey(bean.getKey());
							insbSpecialkindconfig.setCodevalue(bean.getValue());
							insbSpecialkindconfigs.add(insbSpecialkindconfig);
						}
					}
					if("05".equals(value) && !StringUtil.isEmpty(model.getCompensationdays())){
						INSBSpecialkindconfig insbSpecialkindconfig = new INSBSpecialkindconfig();
						insbSpecialkindconfig.setCreatetime(new Date());
						insbSpecialkindconfig.setOperator(operator);
						insbSpecialkindconfig.setTaskid(taskid);
						insbSpecialkindconfig.setTypecode("05");
						insbSpecialkindconfig.setKindcode(risks.getCode());
						insbSpecialkindconfig.setCodekey("");
						insbSpecialkindconfig.setCodevalue(model.getCompensationdays());
						insbSpecialkindconfigs.add(insbSpecialkindconfig);
					}
				}
			}
		}
		//先删除
		INSBSpecialkindconfig insbSpecialkindconfig = new INSBSpecialkindconfig();
		insbSpecialkindconfig.setTaskid(taskid);
		List<INSBSpecialkindconfig> specialkindconfigs = insbSpecialkindconfigDao.selectList(insbSpecialkindconfig);
		if(null != specialkindconfigs && specialkindconfigs.size() > 0){
			for(INSBSpecialkindconfig specialkindconfig : specialkindconfigs){
				insbSpecialkindconfigDao.deleteById(specialkindconfig.getId());
			}
		}
		//保存
		List<INSBSpecialkindconfig> savelist = new ArrayList<INSBSpecialkindconfig>();
		if(proids.size() > 0 && insbSpecialkindconfigs.size() > 0){
			for(String pid : proids){
				for(INSBSpecialkindconfig specialkindconfig : insbSpecialkindconfigs){
					INSBSpecialkindconfig specialkindconfig2 = new INSBSpecialkindconfig();
					specialkindconfig2.setCreatetime(specialkindconfig.getCreatetime());
					specialkindconfig2.setOperator(specialkindconfig.getOperator());
					specialkindconfig2.setTaskid(taskid);
					specialkindconfig2.setTypecode(specialkindconfig.getTypecode());
					specialkindconfig2.setKindcode(specialkindconfig.getKindcode());
					specialkindconfig2.setCodekey(specialkindconfig.getCodekey());
					specialkindconfig2.setCodevalue(specialkindconfig.getCodevalue());
					specialkindconfig2.setInscomcode(pid);
					savelist.add(specialkindconfig2);
				}
			}
			insbSpecialkindconfigDao.insertInBatch(savelist);
		}
	}

	private String getValueFromCode(String code){
		String result = "";
		if(!StringUtil.isEmpty(code)){
			result = appInsuredQuoteDao.getCodeValueFromCode(code);
		}
		return result;
	}

	private void saveRemarkInTab(String taskid, String remark, String operator, String remarkcode) {
		INSBWorkflowmaintrack insbWorkflowmaintrack = new INSBWorkflowmaintrack();
		insbWorkflowmaintrack.setInstanceid(taskid);
		List<INSBWorkflowmaintrack> insbWorkflowmaintracks = insbWorkflowmaintrackDao.selectList(insbWorkflowmaintrack);
		if(null != insbWorkflowmaintracks && insbWorkflowmaintracks.size() > 0){
			INSBWorkflowmaintrack workflowmaintrack = insbWorkflowmaintracks.get(0);
			INSBUsercomment insbUsercomment = new INSBUsercomment();
			insbUsercomment.setOperator(operator);
			insbUsercomment.setCreatetime(new Date());
			insbUsercomment.setTrackid(workflowmaintrack.getId());
			insbUsercomment.setTracktype(1);
			insbUsercomment.setCommentcontent(remark);
			insbUsercomment.setCommentcontenttype(StringUtil.isEmpty(remarkcode)?0:Integer.parseInt(remarkcode));
			insbUsercomment.setCommenttype(1);
			insbUsercommentDao.insert(insbUsercomment);
		}

		// 判断备注类型是否需要转人工
		LogUtil.info("saveRemarkInTab=" + taskid + "代理人选择的备注类型=" + remarkcode);
		Map<String, Object> map = isNeedToManMade(remarkcode);
		if (null != map && (boolean) map.get("flag")) {
			List<INSBQuoteinfo> insbQuoteinfos = appInsuredQuoteDao.getSelectedProvidersByTaskid(taskid);
			if (null != insbQuoteinfos && insbQuoteinfos.size() > 0) {
				for (INSBQuoteinfo insbQuoteinfo : insbQuoteinfos) {
					saveFlowerrorToManWork(taskid,insbQuoteinfo.getInscomcode(), "代理人录入备注类型（"+ map.get("result").toString() + "）,备注内容（"+ remark + "）,转人工处理","8");
				}
			}
		}
	}

	/**
	 * 判断是否为转人工备注，prop1 为1需要转人工
	 *
	 * @param codevalue
	 * @return flag true 转人工标志 result 转人工类型
	 */
	public Map<String, Object> isNeedToManMade(String codevalue) {
		Map<String, Object> map = null;
		INSCCode inscCode = new INSCCode();
		inscCode.setCodetype("agentnoti");
		inscCode.setParentcode("agentnoti");
		inscCode.setCodevalue(codevalue);
		INSCCode code = inscCodeDao.selectOne(inscCode);
		if (null != code && 1 == code.getProp1()) {
			map = new HashMap<String, Object>();
			map.put("flag", true);
			map.put("result", code.getCodename());
		}
		return map;
	}

	/**
	 * 影像关联任务id
	 * @param taskid
	 * @param fileid 文件id
	 */
	private void saveFileImageId(String taskid, String fileid) {
		INSBFilebusiness insbFilebusiness = new INSBFilebusiness();
		insbFilebusiness.setFilelibraryid(fileid);
		List<INSBFilebusiness> insbFilebusinesses = insbFilebusinessDao.selectList(insbFilebusiness);
		if(null != insbFilebusinesses && insbFilebusinesses.size() > 0){
			INSBFilebusiness filebusiness = insbFilebusinesses.get(0);
			filebusiness.setCode(taskid);
			insbFilebusinessDao.updateById(filebusiness);
		}
	}

	@Override
	public CommonModel supplementInfo(String taskid) {
		CommonModel commonModel = new CommonModel();
		try {
			if (StringUtil.isEmpty(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			//根据实例id获取选择的报价信息总表id
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			if(null == quotetotalinfo){
				commonModel.setStatus("fail");
				commonModel.setMessage("报价总表信息不存在");
				return commonModel;
			}
			//获取报价信息表，报价公司id列表
			INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
			insbQuoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
			List<INSBQuoteinfo> insbQuoteinfos = insbQuoteinfoDao.selectList(insbQuoteinfo);
			List<String> ids = new ArrayList<String>();
			for(INSBQuoteinfo quoteinfo : insbQuoteinfos){
				ids.add(quoteinfo.getInscomcode());
			}
			//根据供应商id查询险种投保数据项，获取需要填写的补充信息
			if(ids.size() <= 0){
				commonModel.setStatus("fail");
				commonModel.setMessage("没有查询到报价公司");
				return commonModel;
			}
			List<INSBRiskitem> insbRiskitems = appInsuredQuoteDao.queryRiskItemsByProviderids(ids);
			List<SupplementaryinfoBean> supplementaryinfoBeans = new ArrayList<SupplementaryinfoBean>();
			if(null == insbRiskitems || insbRiskitems.size() <= 0){
				commonModel.setStatus("fail");
				commonModel.setMessage("没有补充信息");
			}else{
				for(INSBRiskitem insbRiskitem : insbRiskitems){
					SupplementaryinfoBean supplementaryinfoBean = new SupplementaryinfoBean();
					supplementaryinfoBean.setInputtype(insbRiskitem.getInputtype());
					supplementaryinfoBean.setIsquotemust(insbRiskitem.getIsquotemust());
					supplementaryinfoBean.setItemcode(insbRiskitem.getItemcode());
					supplementaryinfoBean.setItemname(insbRiskitem.getItemname());
					supplementaryinfoBean.setItemtype(insbRiskitem.getItemtype());
					if("2".equals(insbRiskitem.getInputtype()) || "select".equals(insbRiskitem.getInputtype())){
						JSONArray jsonArray = JSONArray.fromObject(insbRiskitem.getOptional());
						supplementaryinfoBean.setOptional(jsonArray);
					}else{
						supplementaryinfoBean.setOptional(insbRiskitem.getOptional());
					}
					supplementaryinfoBeans.add(supplementaryinfoBean);
				}
				commonModel.setStatus("success");
				commonModel.setMessage("操作成功");
				commonModel.setBody(supplementaryinfoBeans);
			}
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel peopleInfo(PeopleInfoModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if (StringUtil.isEmpty(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			INSBPolicyitem policyitem = new INSBPolicyitem();
			policyitem.setTaskid(taskid);
			List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(policyitem);
			if(null == insbPolicyitems || insbPolicyitems.size() <= 0){
				commonModel.setStatus("fail");
				commonModel.setMessage("保单表信息不存在");
				return commonModel;
			}
			//查询车主信息
			INSBCarowneinfo insbCarowneinfo = new INSBCarowneinfo();
			insbCarowneinfo.setTaskid(taskid);
			INSBCarowneinfo carowneinfo = insbCarowneinfoDao.selectOne(insbCarowneinfo);
			if(null == carowneinfo){
				commonModel.setStatus("fail");
				commonModel.setMessage("车主信息不存在");
				return commonModel;
			}
			String carowerid = "";
			String carowername = "";
			String operator = insbPolicyitems.get(0).getOperator();
			INSBPerson insbPerson = insbPersonDao.selectById(carowneinfo.getPersonid());
			if(null != insbPerson){
				carowerid = insbPerson.getId();
				carowername = insbPerson.getName();
			}
			//0被保人	1 投保人 	2权益索赔人
			String flag = model.getFlag();
			if("0".equals(flag)){
				saveInsuredInfo(taskid, model.isSamecarowner(), model.getPersoninfo(), carowerid, carowername, operator);
			}else if("1".equals(flag)){
				saveInsbapplicantInfo(taskid, model.isSamecarowner(), model.getPersoninfo(), carowerid, carowername, operator);
			}else{
				saveInsblegalrightclaimInfo(taskid, model.isSamecarowner(), model.getPersoninfo(), carowerid, carowername, operator);
			}
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}
	/**
	 * 保存被保人信息
	 * @param taskid
	 * @param issame
	 * @param personinfo
	 * @param carowerid
	 * @param carowername
	 * @param operator
	 */
	private void saveInsuredInfo(String taskid,boolean issame,AppPerson personinfo,String carowerid,String carowername,String operator){
		//车主人员信息表
		INSBPerson insbPerson = new INSBPerson();
		insbPerson=insbPersonDao.selectById(carowerid);
		//被保人表信息
		INSBInsured insured = new INSBInsured();
		insured.setTaskid(taskid);
		insured = insbInsrueDao.selectOne(insured);
		//被保人人员信息表
		INSBPerson insbPersonInsured = new INSBPerson();
		//为空的时候创建被保人信息,否则直接获取对应人员信息
		if(insured==null){
			insbPersonInsured=insbPersonHelpService.addPersonIsNull(insbPerson,operator,ConstUtil.STATUS_2);
		}else{
			insbPersonInsured=insbPersonDao.selectById(insured.getPersonid());
			if(insbPersonInsured==null){
				insbPersonInsured=new INSBPerson();
				insbPersonInsured=insbPersonHelpService.repairPerson(insbPersonInsured,insbPerson,operator);
				insbPersonInsured.setId(UUIDUtils.create());
				insbPersonDao.insert(insbPersonInsured);	
				//更新被保人关联指向insbperson
				insured.setPersonid(insbPersonInsured.getId());
				insbInsrueDao.updateById(insured);	
			}
		}		
		//与车主一致
		if(issame){
			insbPersonInsured=insbPersonHelpService.repairPerson(insbPersonInsured,insbPerson,operator);
			insbPersonDao.updateById(insbPersonInsured);	
		}else{
			insbPersonInsured.setOperator(operator);
			insbPersonInsured.setCreatetime(new Date());
			insbPersonInsured.setTaskid(taskid);
			insbPersonInsured.setName(personinfo.getName());
			// 1-女,0-男
			if("0".equals(personinfo.getCertificateType())){
				insbPersonInsured.setGender(ModelUtil.getGenderByIdCard(personinfo.getCertNumber()));
			}else{
				insbPersonInsured.setGender(1);
			}
			insbPersonInsured.setIdcardtype(Integer.valueOf(personinfo.getCertificateType()));//证件类型
			insbPersonInsured.setIdcardno(personinfo.getCertNumber());//证件号码
			insbPersonInsured.setCellphone(personinfo.getTel());
			insbPersonInsured.setEmail(personinfo.getEmail());
			insbPersonDao.updateById(insbPersonInsured);
		}
		//更新保单表
		updateInsbPolicyitemInsured(taskid,insured.getId(),insbPersonInsured.getName());
		
	}
	/**
	 * 保存投保人信息
	 * @param taskid
	 * @param issame
	 * @param personinfo
	 * @param carowerid
	 * @param carowername
	 * @param operator
	 */
	private void saveInsbapplicantInfo(String taskid,boolean issame,AppPerson personinfo,String carowerid,String carowername,String operator){
		//车主人员信息表
		INSBPerson insbPerson = new INSBPerson();
		insbPerson=insbPersonDao.selectById(carowerid);
		//投保人表信息
		INSBApplicant applicant = new INSBApplicant();
		applicant.setTaskid(taskid);
		applicant = insbApplicantDao.selectOne(applicant);
		//投保人人员信息表
		INSBPerson insbPersonApplicant = new INSBPerson();
		//为空的时候创建投保人信息,否则直接获取对应人员信息
		if(applicant==null){
			insbPersonApplicant=insbPersonHelpService.addPersonIsNull(insbPerson,operator,ConstUtil.STATUS_1);
		}else{
			insbPersonApplicant=insbPersonDao.selectById(applicant.getPersonid());
			if(insbPersonApplicant==null){
				insbPersonApplicant=new INSBPerson();
				insbPersonApplicant=insbPersonHelpService.repairPerson(insbPersonApplicant,insbPerson,operator);
				insbPersonApplicant.setId(UUIDUtils.create());
				insbPersonDao.insert(insbPersonApplicant);	
				//更新投保人关联指向insbperson
				applicant.setPersonid(insbPersonApplicant.getId());
				insbApplicantDao.updateById(applicant);	
			}
		}	
		//与车主一致
		if(issame){
			//insbPersonInsured=insbPersonDao.selectById(insured.getPersonid());
			insbPersonApplicant=insbPersonHelpService.repairPerson(insbPersonApplicant,insbPerson,operator);
			insbPersonDao.updateById(insbPersonApplicant);	
		}else{
			//insbPersonInsured=insbPersonDao.selectById(insured.getPersonid());
			insbPersonApplicant.setOperator(operator);
			insbPersonApplicant.setCreatetime(new Date());
			insbPersonApplicant.setTaskid(taskid);
			insbPersonApplicant.setName(personinfo.getName());
			// 1-女,0-男
			if("0".equals(personinfo.getCertificateType())){
				insbPersonApplicant.setGender(ModelUtil.getGenderByIdCard(personinfo.getCertNumber()));
			}else{
				insbPersonApplicant.setGender(1);
			}
			insbPersonApplicant.setIdcardtype(Integer.valueOf(personinfo.getCertificateType()));//证件类型
			insbPersonApplicant.setIdcardno(personinfo.getCertNumber());//证件号码
			insbPersonApplicant.setCellphone(personinfo.getTel());
			insbPersonApplicant.setEmail(personinfo.getEmail());
			insbPersonDao.updateById(insbPersonApplicant);
		}
		//更新保单表
		updateInsbPolicyitemApplicant(taskid,applicant.getId(),insbPersonApplicant.getName());
	}
	/**
	 * 保存权益索赔人信息
	  * @param taskid
	 * @param issame
	 * @param personinfo
	 * @param carowerid
	 * @param carowername
	 * @param operator
	 */
	private void saveInsblegalrightclaimInfo(String taskid,boolean issame,AppPerson personinfo,String carowerid,String carowername,String operator){		
		//车主人员信息表
		INSBPerson insbPerson = new INSBPerson();
		insbPerson=insbPersonDao.selectById(carowerid);
		//权益人表信息
		INSBLegalrightclaim legalrightclaim = new INSBLegalrightclaim();
		legalrightclaim.setTaskid(taskid);
		legalrightclaim = insbLegalrightclaimDao.selectOne(legalrightclaim);
		//权益人人员信息表
		INSBPerson insbPersonLegalrightclaim = new INSBPerson();		
		//为空的时候创建权益人信息,否则直接获取对应人员信息
		if(legalrightclaim==null){
			insbPersonLegalrightclaim=insbPersonHelpService.addPersonIsNull(insbPerson,operator,ConstUtil.STATUS_3);
		}else{
			insbPersonLegalrightclaim=insbPersonDao.selectById(legalrightclaim.getPersonid());
			if(insbPersonLegalrightclaim==null){
				insbPersonLegalrightclaim=new INSBPerson();
				insbPersonLegalrightclaim=insbPersonHelpService.repairPerson(insbPersonLegalrightclaim,insbPerson,operator);
				insbPersonLegalrightclaim.setId(UUIDUtils.create());
				insbPersonDao.insert(insbPersonLegalrightclaim);	
				//更新被权益人关联指向insbperson
				legalrightclaim.setPersonid(insbPersonLegalrightclaim.getId());
				insbLegalrightclaimDao.updateById(legalrightclaim);	
			}
		}	
		//与车主一致
		if(issame){
			//insbPersonInsured=insbPersonDao.selectById(insured.getPersonid());
			insbPersonLegalrightclaim=insbPersonHelpService.repairPerson(insbPersonLegalrightclaim,insbPerson,operator);
			insbPersonDao.updateById(insbPersonLegalrightclaim);	
		}else{
			//insbPersonInsured=insbPersonDao.selectById(insured.getPersonid());
			insbPersonLegalrightclaim.setOperator(operator);
			insbPersonLegalrightclaim.setCreatetime(new Date());
			insbPersonLegalrightclaim.setTaskid(taskid);
			insbPersonLegalrightclaim.setName(personinfo.getName());
			// 1-女,0-男
			if("0".equals(personinfo.getCertificateType())){
				insbPersonLegalrightclaim.setGender(ModelUtil.getGenderByIdCard(personinfo.getCertNumber()));
			}else{
				insbPersonLegalrightclaim.setGender(1);
			}
			insbPersonLegalrightclaim.setIdcardtype(Integer.valueOf(personinfo.getCertificateType()));//证件类型
			insbPersonLegalrightclaim.setIdcardno(personinfo.getCertNumber());//证件号码
			insbPersonLegalrightclaim.setCellphone(personinfo.getTel());
			insbPersonLegalrightclaim.setEmail(personinfo.getEmail());
			insbPersonDao.updateById(insbPersonLegalrightclaim);
		}
	}
	//被保人信息
	private void updateInsbPolicyitemInsured(String taskid,String insuredId,String name){
		INSBPolicyitem policyitem = new INSBPolicyitem();
		policyitem.setTaskid(taskid);
		List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(policyitem);
		for(INSBPolicyitem insbPolicyitem : insbPolicyitems){
			insbPolicyitem.setInsuredid(insuredId);
			insbPolicyitem.setInsuredname(name);
			insbPolicyitemDao.updateById(insbPolicyitem);
		}
	}
	//投保人信息
	private void updateInsbPolicyitemApplicant(String taskid,String applicantId,String name){
		INSBPolicyitem policyitem = new INSBPolicyitem();
		policyitem.setTaskid(taskid);
		List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(policyitem);
		for(INSBPolicyitem insbPolicyitem : insbPolicyitems){
			insbPolicyitem.setApplicantid(applicantId);
			insbPolicyitem.setApplicantname(name);
			insbPolicyitemDao.updateById(insbPolicyitem);
		}
	}
	@Override
	public CommonModel carowerInfo(CarowerInfoModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if (StringUtil.isEmpty(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			INSBCarowneinfo insbCarowneinfo = new INSBCarowneinfo();
			insbCarowneinfo.setTaskid(taskid);
			INSBCarowneinfo carowneinfo = insbCarowneinfoDao.selectOne(insbCarowneinfo);
			if(null == carowneinfo){
				commonModel.setStatus("fail");
				commonModel.setMessage("车主信息不存在");
				return commonModel;
			}
			INSBPerson insbPerson = insbPersonDao.selectById(carowneinfo.getPersonid());
			//证件与被保人一致  0 一致  1不一致
			if("1".equals(model.getFlag())){
				insbPerson.setIdcardtype(Integer.valueOf(model.getCertificateType()));//证件类型
				insbPerson.setIdcardno(model.getCertNumber());//证件号码
			}else{
				//查出被保人信息
				INSBInsured insbInsured = new INSBInsured();
				insbInsured.setTaskid(taskid);
				INSBInsured insured = insbInsrueDao.selectOne(insbInsured);
				INSBPerson person = insbPersonDao.selectById(insured.getPersonid());
				insbPerson.setIdcardtype(person.getIdcardtype());//证件类型
				insbPerson.setIdcardno(person.getIdcardno());//证件号码
			}
			insbPersonDao.updateById(insbPerson);
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel insuredDate(InsuredDateModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if (StringUtil.isEmpty(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
			insbPolicyitem.setTaskid(taskid);
			//商业险
			if("0".equals(model.getType())){
				insbPolicyitem.setRisktype("0");
			}else{
				insbPolicyitem.setRisktype("1");
			}
			INSBPolicyitem policyitem = insbPolicyitemDao.selectOne(insbPolicyitem);
			if(null == policyitem){
				commonModel.setStatus("fail");
				commonModel.setMessage("保单数据不存在");
				return commonModel;
			}
			policyitem.setModifytime(new Date());
			if(!StringUtil.isEmpty(model.getRiskstartdate())){
				policyitem.setStartdate(ModelUtil.conbertStringToNyrDate(model.getRiskstartdate()));
			}
			if(!StringUtil.isEmpty(model.getRiskenddate())){
				policyitem.setStartdate(ModelUtil.conbertStringToNyrDate(model.getRiskenddate()));
			}
			insbPolicyitemDao.updateById(policyitem);
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel driversInfo(DriversInfoModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if (StringUtil.isEmpty(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			//
			INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
			insbPolicyitem.setTaskid(taskid);
			INSBPolicyitem policyitem = insbPolicyitemDao.selectPolicyitemByTaskId(taskid);
			if(null == policyitem){
				commonModel.setStatus("fail");
				commonModel.setMessage("保单数据不存在");
				return commonModel;
			}
			//获取车辆信息
			INSBCarinfo insbCarinfo = new INSBCarinfo();
			insbCarinfo.setTaskid(taskid);
			INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
			if(null == carinfo){
				commonModel.setStatus("fail");
				commonModel.setMessage("车辆信息不存在");
				return commonModel;
			}
			//保存驾驶人信息
			List<CarDriver> drivers = model.getDrivers();
			List<INSBSpecifydriver> insbSpecifydrivers = new ArrayList<INSBSpecifydriver>();
			if(null != drivers && drivers.size() > 0){
				for(CarDriver carDriver : drivers){
					INSBPerson insbPerson = new INSBPerson();
					insbPerson.setOperator(policyitem.getOperator());
					insbPerson.setCreatetime(new Date());
					insbPerson.setTaskid(taskid);
					insbPerson.setName(carDriver.getName());
					insbPerson.setGender(Integer.parseInt(carDriver.getGender()));
					insbPerson.setBirthday(ModelUtil.conbertStringToNyrDate(carDriver.getBirthday()));
					insbPerson.setLicensetype(carDriver.getDriverTypeCode());
					insbPerson.setLicensedate(ModelUtil.conbertStringToNyrDate(carDriver.getLicensedDate()));
					insbPerson.setLicenseno(carDriver.getLicenseNo());
					insbPersonDao.insert(insbPerson);
					INSBSpecifydriver insbSpecifydriver = new INSBSpecifydriver();
					insbSpecifydriver.setCreatetime(new Date());
					insbSpecifydriver.setOperator(policyitem.getOperator());
					insbSpecifydriver.setTaskid(taskid);
					insbSpecifydriver.setCarinfoid(carinfo.getId());
					insbSpecifydriver.setPersonid(insbPerson.getId());
					insbSpecifydrivers.add(insbSpecifydriver);
				}
				insbSpecifydriverDao.insertInBatch(insbSpecifydrivers);
				commonModel.setStatus("success");
				commonModel.setMessage("操作成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel otherInsuredInfo(OtherInsuredInfoModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if (StringUtil.isEmpty(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			//更新车辆信息表，增加行驶区域，上年云南查询投保公司
			INSBCarinfo insbCarinfo = insbCarinfoDao.selectCarinfoByTaskId(taskid);
			if(null == insbCarinfo){
				commonModel.setStatus("fail");
				commonModel.setMessage("车辆信息不存在");
				return commonModel;
			}
			insbCarinfo.setDrivingarea(model.getDrivingRegion());
			//上年商业承保公司
			insbCarinfo.setPreinscode(model.getLastComId());
			insbCarinfoDao.updateById(insbCarinfo);
			LogUtil.info("INSBCarinfo|报表数据埋点|"+JSONObject.fromObject(insbCarinfo).toString());
			//更新投保人，被保人，车主信息，增加邮箱，联系方式
			updateaLLPeopleInfo(model, insbCarinfo.getOperator());

			//updatePeopleInfo(taskid,model.getTel(),model.getEmail());
			//保存投保补充信息
			List<SupplementInfo> supplementInfos = model.getSupplementInfos();
			List<INSBSupplementaryinfo> supplementaryinfos = new ArrayList<INSBSupplementaryinfo>();
			if(null != supplementInfos && supplementInfos.size() > 0){
				for(SupplementInfo supplementInfo : supplementInfos){
					INSBSupplementaryinfo insbSupplementaryinfo = new INSBSupplementaryinfo();
					insbSupplementaryinfo.setCreatetime(new Date());
					insbSupplementaryinfo.setOperator(insbCarinfo.getOperator());
					insbSupplementaryinfo.setTaskid(taskid);
					insbSupplementaryinfo.setInputtype(supplementInfo.getInputtype());
					insbSupplementaryinfo.setItemcode(supplementInfo.getItemcode());
					insbSupplementaryinfo.setItemname(supplementInfo.getItemname());
					insbSupplementaryinfo.setItemtype(supplementInfo.getItemtype());
					insbSupplementaryinfo.setOptional(supplementInfo.getOptional());
					supplementaryinfos.add(insbSupplementaryinfo);
				}
			}
			insbSupplementaryinfoDao.insertInBatch(supplementaryinfos);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	private void updateaLLPeopleInfo(OtherInsuredInfoModel model,String operator) {
		String taskid = model.getProcessinstanceid();
		//更新投保人，，车主信息，增加邮箱，联系方式
		//被保人
		INSBPerson insbPersonbb = null;
		//INSBPerson insbPersontb = null;
		INSBInsured insbInsured2 = null;
		INSBInsured insbInsured = new INSBInsured();
		insbInsured.setTaskid(taskid);
		INSBInsured insured = insbInsuredDao.selectOne(insbInsured);
		INSBApplicant insbApplicant = null;
//		applicant.setTaskid(taskid);
//		INSBApplicant applicant2 = insbApplicantDao.selectOne(applicant);
		//更新保单表数据
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskid);
		List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(insbPolicyitem);
		if(null == insured){
			//被保人表信息
			insbPersonbb = new INSBPerson();
			insbPersonbb.setOperator(operator);
			insbPersonbb.setCreatetime(new Date());
			insbPersonbb.setTaskid(taskid);
			insbPersonbb.setName(model.getName());
			if("0".equals(model.getCertificateType()) && model.getCertNumber().length() == 18){
				insbPersonbb.setGender(ModelUtil.getGenderByIdCard(model.getCertNumber()));
			}else{
				// 1-女,0-男
				insbPersonbb.setGender(1);
			}
			insbPersonbb.setIdcardtype(Integer.valueOf(model.getCertificateType()));//证件类型
			insbPersonbb.setIdcardno(model.getCertNumber());//证件号码
			insbPersonbb.setCellphone(model.getTel());
			insbPersonbb.setEmail(model.getEmail());
			insbPersonDao.insert(insbPersonbb);
			//被保人表
			insbInsured2 = new INSBInsured();
			insbInsured2.setTaskid(taskid);
			insbInsured2.setPersonid(insbPersonbb.getId());
			insbInsured2.setOperator(operator);
			insbInsured2.setCreatetime(new Date());
			insbInsrueDao.insert(insbInsured2);
		}else{
			insbInsured2 = insured;
			insbPersonbb = insbPersonDao.selectById(insured.getPersonid());
			insbPersonbb.setOperator(operator);
			insbPersonbb.setModifytime(new Date());
			insbPersonbb.setTaskid(taskid);
			insbPersonbb.setName(model.getName());
			if("0".equals(model.getCertificateType()) && model.getCertNumber().length() == 18){
				insbPersonbb.setGender(ModelUtil.getGenderByIdCard(model.getCertNumber()));
			}else{
				// 1-女,0-男
				insbPersonbb.setGender(1);
			}
			insbPersonbb.setIdcardtype(Integer.valueOf(model.getCertificateType()));//证件类型
			insbPersonbb.setIdcardno(model.getCertNumber());//证件号码
			insbPersonbb.setCellphone(model.getTel());
			insbPersonbb.setEmail(model.getEmail());
			insbPersonDao.updateById(insbPersonbb);
		}
		for(INSBPolicyitem policyitem : insbPolicyitems){
			policyitem.setInsuredid(insbInsured2.getId());
			policyitem.setInsuredname(model.getName());
			insbPolicyitemDao.updateById(policyitem);
		}
		//与被保人一致 0 一致 1 不一致
		if("0".equals(model.getSameinsured())){
			INSBApplicant applicant = new INSBApplicant();
			applicant.setTaskid(taskid);
			INSBApplicant insbApplicant2 = insbApplicantDao.selectOne(applicant);
			if(null == insbApplicant2){
				insbApplicant = new INSBApplicant();
				insbApplicant.setOperator(operator);
				insbApplicant.setCreatetime(new Date());
				insbApplicant.setTaskid(taskid);
				insbApplicant.setPersonid(insbPersonbb.getId());
				insbApplicantDao.insert(insbApplicant);
			}else{
				insbApplicant = insbApplicant2;
				insbApplicant2.setOperator(operator);
				insbApplicant2.setModifytime(new Date());
				insbApplicant2.setTaskid(taskid);
				insbApplicant2.setPersonid(insbPersonbb.getId());
				insbApplicantDao.updateById(insbApplicant2);
			}
			for(INSBPolicyitem policyitem : insbPolicyitems){
				policyitem.setApplicantid(insbApplicant.getId());;
				policyitem.setApplicantname(model.getName());
				insbPolicyitemDao.updateById(policyitem);
			}
		}
		//车主证件是否与被保人一致 0 一致 1 不一致
		if("0".equals(model.getCardnum())){
			INSBCarowneinfo insbCarowneinfo = new INSBCarowneinfo();
			insbCarowneinfo.setTaskid(taskid);
			INSBCarowneinfo carowneinfo = insbCarowneinfoDao.selectOne(insbCarowneinfo);
			if(null != carowneinfo){
				INSBPerson carperson = insbPersonDao.selectById(carowneinfo.getPersonid());
				if(null != carperson){
					if("0".equals(model.getCertificateType()) && model.getCertNumber().length() == 18){
						carperson.setGender(ModelUtil.getGenderByIdCard(model.getCertNumber()));
					}else{
						// 1-女,0-男
						carperson.setGender(1);
					}
					carperson.setIdcardtype(Integer.valueOf(model.getCertificateType()));//证件类型
					carperson.setIdcardno(model.getCertNumber());//证件号码
					carperson.setCellphone(model.getTel());
					carperson.setEmail(model.getEmail());
					insbPersonDao.updateById(carperson);
				}
				for(INSBPolicyitem policyitem : insbPolicyitems){
					policyitem.setCarownerid(insbCarowneinfo.getId());;
					//policyitem.setApplicantname(model.getName());
					insbPolicyitemDao.updateById(policyitem);
				}
			}
		}

		//商业险区间
		for(INSBPolicyitem policyitem : insbPolicyitems){
			if("0".equals(policyitem.getRisktype())){
				if(!StringUtil.isEmpty(model.getSriskstartdate()) && !StringUtil.isEmpty(model.getSriskenddate())){
					policyitem.setStartdate(ModelUtil.conbertStringToNyrDate(model.getSriskstartdate()));
					policyitem.setEnddate(ModelUtil.conbertStringToNyrDate(model.getSriskenddate()));
				}
			}else{
				if(!StringUtil.isEmpty(model.getJriskstartdate()) && !StringUtil.isEmpty(model.getJriskenddate())){
					policyitem.setStartdate(ModelUtil.conbertStringToNyrDate(model.getJriskstartdate()));
					policyitem.setEnddate(ModelUtil.conbertStringToNyrDate(model.getJriskenddate()));
				}
			}
			insbPolicyitemDao.updateById(policyitem);
		}
	}

	@Override
	public CommonModel schemeList(String taskid, String agentnotitype) {
		ExtendCommonModel commonModel = new ExtendCommonModel();
		try {
			List<INSBRiskplanrelation> insbRiskplanrelations = insbRiskplanrelationDao.selectAllPlankey();
			Map<String, String> resultMap = new HashMap<String, String>();
			for(INSBRiskplanrelation riskplanrelation : insbRiskplanrelations){
				resultMap.put(riskplanrelation.getPlankey(), riskplanrelation.getPlanname());
			}
			if(!StringUtil.isEmpty(taskid)){
				//先从redis里面取值，取到直接返回
				LastYearPolicyInfoBean yearPolicyInfoBean = redisClient.get(Constants.TASK, taskid,LastYearPolicyInfoBean.class);
				if(null != yearPolicyInfoBean){
					//险种信息
					JSONArray lastYearRiskinfos = yearPolicyInfoBean.getLastYearRiskinfos();
					if(null != lastYearRiskinfos && lastYearRiskinfos.size() >0){
						resultMap.put("snbxpz", "上年保险配置信息");
					} else {
						resultMap.put("zdypz", "自定义配置");
					}
					//2016-04-13 如果只能单保就不出现上年保险配置 820注
//					Map<String, Object> mapresult = judgeInsuredDate(90,taskid);
//					//险种信息
//					JSONArray lastYearRiskinfos = yearPolicyInfoBean.getLastYearRiskinfos();
//					if(mapresult.isEmpty()){
//						if(null != lastYearRiskinfos && lastYearRiskinfos.size() >0){
//							resultMap.put("snbxpz", "上年保险配置信息");
//						}
//					}else{
//						boolean syenddateflag = (boolean) mapresult.get("syenddateflag");
//						boolean jqenddateflag = (boolean) mapresult.get("jqenddateflag");
//						if(syenddateflag && jqenddateflag){
//							if(null != lastYearRiskinfos && lastYearRiskinfos.size() >0){
//								resultMap.put("snbxpz", "上年保险配置信息");
//							}
//						}else{
//							//不显示上年保险配置
//						}
//					}
				}else {
					resultMap.put("zdypz", "自定义配置");
				}
			}else {
				resultMap.put("zdypz", "自定义配置");
			}
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(resultMap);

			Map<String, Object> insuredatemap = new HashMap<String, Object>();
			insuredatemap.put("agentnoti", remarkTypeFromCode(agentnotitype));
			commonModel.setExtend(insuredatemap);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel selectNeedUploadImage_ByParentcode(String processinstanceid) {
		CommonModel commonModel = new CommonModel();
		try {
			if (StringUtil.isEmpty(processinstanceid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			//查询所有
			List<InsuranceImageBean> insbRiskimgs = appInsuredQuoteDao.selectNeedUploadImage_ByParentcode();
			LogUtil.info("任务号："+processinstanceid+" 默认返回的："+ JSON.toJSONString(insbRiskimgs));
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(insbRiskimgs);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public ExtendCommonModel needUploadImage(String processinstanceid) {
		ExtendCommonModel commonModel = new ExtendCommonModel();
		try {
			if (StringUtil.isEmpty(processinstanceid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			//根据实例id获取报价公司列表
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(processinstanceid);
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			if (null == quotetotalinfo) {
				commonModel.setStatus("fail");
				commonModel.setMessage("报价总表信息不存在");
				return commonModel;
			}
			INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
			insbQuoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
			List<INSBQuoteinfo> insbQuoteinfos = insbQuoteinfoDao.selectList(insbQuoteinfo);
			List<String> list = new ArrayList<String>();
			for(INSBQuoteinfo quoteinfo : insbQuoteinfos){
				list.add(quoteinfo.getInscomcode().substring(0, 4));
			}
			if(list.size() <= 0){
				commonModel.setStatus("fail");
				commonModel.setMessage("没有查询到报价公司");
				return commonModel;
			}
			//根据报价公司查询影像挂件
			List<InsuranceImageBean> insbRiskimgs = appInsuredQuoteDao.selectNeedUploadImage(list);
			LogUtil.info("任务号："+processinstanceid+" 默认返回的："+ JSON.toJSONString(insbRiskimgs));
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(insbRiskimgs);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	/**
	 *
	 * @param processinstanceid
	 * @param inscomcode  协义id
	 * @param status
	 *                1：核保退回时指定要上传的影像。必提交数据项：processinstanceid、inscomcode、status。
	 *                2：获取规则定义要上传的影像(提交核保时)。必提交数据项：processinstanceid、inscomcode、status上传影像）
	 *                3：获取cm配置的上传影像。必提交数据项：processinstanceid。
	 * @return
	 */
	@Override
	public ExtendCommonModel needUploadImageByCodeType(String processinstanceid, String inscomcode, String status) {
		ExtendCommonModel cm = new ExtendCommonModel();
		if("1".equals(status)){//退回修改
			if (!StringUtils.isNoneBlank(processinstanceid, inscomcode)) {
				cm.setStatus(CommonModel.STATUS_FAIL);
				cm.setMessage("参数错误：核保退回修改 任务号或保险公司编码为空");
				LogUtil.error("核保退回修改 任务号或保险公司编码为空：" + processinstanceid + ", " + inscomcode);
			} else {
				//查询库
                List<String> comm_rule_CodeType = new ArrayList<>(); //所有要返回的detype,备注的 并集 规则返回的

				//过规则返回结果集
//                List<String> codeValueList = this.checkUploadImageRule("", processinstanceid, companyId);
//                List<InsuranceImageBean> ruleUpFile = appInsuredQuoteDao.selectRuleNeedUploadImageByCodeValue(codeValueList);
//                Iterator<String> listCodeType_rule = ruleUpFile.stream().map(uf -> uf.getCodetype()).iterator();//规则返回要上传的
//                comm_rule_CodeType.addAll(Lists.newArrayList(listCodeType_rule));

				//最后一次核保退回 备注信息
				String commentContent = queryUserCommentContent(processinstanceid, inscomcode);
				commentContent = commentContent!=null ? commentContent.replaceAll("\n","#") : "";
				cm.setMessage(commentContent);
                // 查询备 注要上传的文件codetype
                List<String> listCodeType = this.queryUserCommentUploadFile_codeType(processinstanceid, inscomcode);
				LogUtil.info("任务号："+processinstanceid+" 保险公司："+inscomcode+" status："+status+" 备注返回的："+listCodeType);
                comm_rule_CodeType.addAll(listCodeType);
                //并集 按备注上传文件类型，查询所有上传类型
                List<InsuranceImageBean> usercommentUpFile = appInsuredQuoteDao.selectBackNeedUploadImageByCodeType(comm_rule_CodeType);
				cm.setStatus(CommonModel.STATUS_SUCCESS);
                cm.setBody(usercommentUpFile);
			}
			return cm;
		}else if("2".equals(status)){//提交核保
			//调规则按公司返回要提交的
			//appQuotationService.getQuotationValidatedInfo()
			//AppQuotationServiceImpl.getQuotationValidatedInfo()
			if (!StringUtils.isNoneBlank(processinstanceid, inscomcode)) {
				cm.setStatus(CommonModel.STATUS_FAIL);
				cm.setMessage("参数错误：核保过规则请求，任务号或保险公司编码为空");
				LogUtil.error("核保过规则请求，任务号或保险公司编码为空：" + processinstanceid + ", " + inscomcode);
			} else {
				Map<String,Object> reMap = this.checkUploadImageRule("", processinstanceid, inscomcode);
				LogUtil.info("任务号：" + processinstanceid + " 保险公司：" + inscomcode + " status：" + status + " 规则返回：" + com.alibaba.fastjson.JSONObject.toJSONString(reMap));

				List<String> codeValueList = (List)reMap.get("codevalue");
				String uploadImageHint = (String)reMap.get("uploadImageHint");

                List<InsuranceImageBean> ruleUpFile = appInsuredQuoteDao.selectRuleNeedUploadImageByCodeValue(codeValueList);
				LogUtil.info("任务号："+processinstanceid+" 保险公司："+inscomcode+" status："+status+" 规则返回（接口）："+ com.alibaba.fastjson.JSONObject.toJSONString(ruleUpFile));

				cm.setStatus(CommonModel.STATUS_SUCCESS);
				cm.setMessage(uploadImageHint);
				cm.setBody(ruleUpFile);

				if (reMap.containsKey("insureSupplyParam")) {
					cm.setExtend(reMap.get("insureSupplyParam"));
				}
			}
			return cm;
		}else { //默认返回：cm配置的上传影像
			//查询配置了的所有
			return this.needUploadImage(processinstanceid);
		}
	}

    /**
     * 查询用户备注关联要上传的文件codetype
     * @return
     */
    public List<String> queryUserCommentUploadFile_codeType(String processinstanceid, String inscomcode){
        List<String> listCodeType = new ArrayList<>();
        List<INSBUsercomment> usercomment = insbUsercommentService.getNearestInsureBack(processinstanceid, inscomcode);
        if(usercomment!=null && usercomment.size()>0){
            String usercommentid = usercomment.get(0).getId(); //核保退回的用户备注id
            Map<String,String> param = new HashMap<>();
            param.put("usercommentid", usercommentid);
            //查询备注上传文件
            List<INSBUsercommentUploadFile> list = insbusercommentUploadFileDao.selectUsercommentUploadFile_byCommId(param);
            for(INSBUsercommentUploadFile cuf : list){
                listCodeType.add(cuf.getCodetype());
            }
        }
        return listCodeType;
    }

	/**
	 * 查询用户最新备注内容
	 * @return
	 */
	public String queryUserCommentContent(String processinstanceid, String inscomcode){
		List<String> listCodeType = new ArrayList<>();
		List<INSBUsercomment> usercomment = insbUsercommentService.getNearestInsureBack(processinstanceid, inscomcode);
		String commentContent = "暂无说明";
		if(usercomment!=null && usercomment.size()>0) {
			commentContent = usercomment.get(0).getCommentcontent(); //核保退回的用户备注内容
		}
		return commentContent;
	}

	private List<INSBInsuresupplyparam> parseInsureSupplyParams(JSONObject insureSupplyParams) {
		if (insureSupplyParams == null) return null;

		List<INSBInsuresupplyparam> result = new ArrayList<>();
		String key=null, pkey=null, name=null, inputType=null, optionalListStr=null;
		String[] optionalList=null, optionalKV=null;
		JSONObject item = null;

		for (Iterator iterator=insureSupplyParams.keys(); iterator.hasNext(); ) {
			key = String.valueOf(iterator.next());
			if (StringUtil.isEmpty(key)) {
				LogUtil.error("数据项编码为空");
				continue;
			}

			item = insureSupplyParams.getJSONObject(key);
			if (item == null) {
				LogUtil.error(key+"数据项值为空");
				continue;
			}

			pkey = key;
			int idx = pkey.indexOf(".");
			if (idx > -1) {
				pkey = pkey.substring(idx+1);
			}

			name = item.getString("metadataName");
			if (StringUtil.isNotEmpty(name)) {
				idx = name.indexOf(".");
				if (idx > -1) {
					name = name.substring(idx+1);
				}
			}

			INSBInsuresupplyparam supplyparam = new INSBInsuresupplyparam();
			supplyparam.setItemcode(pkey);
			supplyparam.setItemname(name);

			inputType = "text";
			optionalListStr = item.getString("metadataValue");
			if (StringUtil.isNotEmpty(optionalListStr) && optionalListStr.contains(":")) {
				inputType = "select";
				optionalList = optionalListStr.split(",");
				Map<String, String> options = new HashMap<>(optionalList.length);

				for (String option : optionalList) {
					if (StringUtil.isEmpty(option)) continue;

					optionalKV = option.split(":");
					if (optionalKV.length == 0) continue;

					if (optionalKV.length == 1) {
						options.put("", optionalKV[0]);
					} else {
						options.put(optionalKV[1], optionalKV[0]);
					}
				}
				supplyparam.setItemoptions(options);
			} else if (pkey.endsWith("DocumentType")) {
				inputType = "select";
				List<INSCCode> certKinds = insbInsuresupplyparamService.getCertKinds();
				Map<String, String> options = new HashMap<>(certKinds.size());

				for (INSCCode code : certKinds) {
					options.put(code.getCodevalue(), code.getCodename());
				}
				supplyparam.setItemoptions(options);
			}

			supplyparam.setIteminputtype(inputType);
			result.add(supplyparam);
		}

		return result;
	}

	/**
	 * 检查 上传影像规则
	 * @param subTaskId
	 * @param taskId
	 * @param inscomcode
	 * @return 影像类型 codeValue 的 List 集合.
	 */
	private Map<String,Object> checkUploadImageRule(String subTaskId, String taskId, String inscomcode){
		Map<String,Object> map = new HashMap<String,Object>(){
			{put("codevalue",new ArrayList<>());put("uploadImageHint", "");}
		};
		String uploadImage_ruleResult = null;
		try {
			uploadImage_ruleResult = appQuotationService.getQuotationValidOrQuotationInfo("", taskId, inscomcode,"imageRule");
			LogUtil.warn("核保过规则请求，规则返回的数据:"+uploadImage_ruleResult +"  任务号：" + taskId + ",保险公司：" + inscomcode);
		} catch (Exception e) {
			LogUtil.error("核保过规则请求，上传影像规则校验时发生了异常" + taskId + "," + inscomcode + "：" + e.getMessage());
            e.printStackTrace();
			return map;
		}

		if (StringUtils.isNotBlank(uploadImage_ruleResult)) {
			JSONObject jobj = JSONObject.fromObject(uploadImage_ruleResult);
			if (jobj != null && jobj.containsKey("ruleItem.uploadImageList")) {
				String uploadImage_codeValueList = jobj.getString("ruleItem.uploadImageList");
				if(uploadImage_codeValueList!=null&& !"".equals(uploadImage_codeValueList.trim())
						&& !"null".equals(uploadImage_codeValueList.trim().toLowerCase())){
					String[] codevalue = uploadImage_codeValueList.trim().replaceAll("，",",").split(",");
					List<String> list = Arrays.asList(codevalue);
					map.put("codevalue",list);
				}else{
					LogUtil.warn("核保过规则请求，上传影像规则校验时,规则返回节点: ruleItem.uploadImageList 数据为空" + taskId + "," + inscomcode);
				}
			}else{
				LogUtil.warn("核保过规则请求，上传影像规则校验时,规则返回数据不含有节点: ruleItem.uploadImageList " + taskId + "," + inscomcode);
			}

			if (jobj != null && jobj.containsKey("ruleItem.uploadImageHint")) {
				String uploadImageHint = jobj.getString("ruleItem.uploadImageHint");
				if(uploadImageHint!=null&& !"".equals(uploadImageHint.trim())
						&& !"null".equals(uploadImageHint.trim().toLowerCase())){
					map.put("uploadImageHint",uploadImageHint);
				}else{
					LogUtil.warn("核保过规则请求，上传影像规则校验时,规则返回节点: ruleItem.uploadImageHint 数据为空" + taskId + "," + inscomcode);
				}
			}else{
				LogUtil.warn("核保过规则请求，上传影像规则校验时,规则返回数据不含有节点: ruleItem.uploadImageHint " + taskId + "," + inscomcode);
			}

			//核保补充数据项
			if (jobj != null && jobj.containsKey("ruleItem.insureSupplyParam")) {
				JSONObject insureSupplyParams = jobj.getJSONObject("ruleItem.insureSupplyParam");
				if(insureSupplyParams!=null){
					List<INSBInsuresupplyparam> params = parseInsureSupplyParams(insureSupplyParams);
					insbInsuresupplyparamService.insertByTask(params, taskId, inscomcode);
					map.put("insureSupplyParam", params);
				}else{
					LogUtil.warn("核保过规则请求，规则返回节点: ruleItem.insureSupplyParam 数据为空" + taskId + "," + inscomcode);
				}
			}else{
				LogUtil.warn("核保过规则请求，规则返回数据不含有节点: ruleItem.insureSupplyParam " + taskId + "," + inscomcode);
			}
		}else{
			LogUtil.warn("核保过规则请求，上传影像规则校验时,规则返回数据为空" + taskId + "," + inscomcode);
		}
		return map;
	}

	@Override
	public CommonModel alreadyUploadImage(String processinstanceid) {
		CommonModel commonModel = new CommonModel();
		try {
			if (StringUtil.isEmpty(processinstanceid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			List<Map<String, String>> list = appInsuredQuoteDao.selectAlreadyUploadImage(processinstanceid);
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(list);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel myUploadImage(String agentnum, String carlicenseno){
		CommonModel commonModel = new CommonModel();
		try {
			if (StringUtil.isEmpty(agentnum)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("代理人工号不能为空");
				return commonModel;
			}

			Map<String, String> map = new HashMap<String, String>();
            map.put("agentnum", agentnum);
            map.put("carlicenseno", carlicenseno);
			List<Map<String, String>> list = appInsuredQuoteDao.selectUserAlreadyUploadImageCar(map);
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(list);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel saveInsuranceBooks(String processinstanceid) {
		CommonModel commonModel = new CommonModel();
		try {
			if (StringUtil.isEmpty(processinstanceid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(processinstanceid);
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			if(null == quotetotalinfo){
				commonModel.setStatus("fail");
				commonModel.setMessage("报价总表信息不存在");
				return commonModel;
			}
			//保存投保书
			quotetotalinfo.setInsurancebooks("1");
			insbQuotetotalinfoDao.updateById(quotetotalinfo);
			LogUtil.info("INSBQuotetotalinfo|报表数据埋点|"+JSONObject.fromObject(quotetotalinfo).toString());
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel verificationInsuredConfig(InsuredConfigModel model) {
		LogUtil.info("verificationInsuredConfig="+model.getProcessinstanceid()+"验证保险配置调用规则");
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if (null == taskid && "".equals(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			// 已选择的报价公司（报价信息总表，报价信息表）
			INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
			quotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
			if (null == insbQuotetotalinfo) {
				commonModel.setStatus("fail");
				commonModel.setMessage("报价信息总表不存在");
				return commonModel;
			}
			INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
			insbQuoteinfo.setQuotetotalinfoid(insbQuotetotalinfo.getId());
			List<INSBQuoteinfo> insbQuoteinfos = insbQuoteinfoDao.selectList(insbQuoteinfo);
			if (null == insbQuoteinfos || insbQuoteinfos.size() <= 0) {
				commonModel.setStatus("fail");
				commonModel.setMessage("没选择报价公司");
				return commonModel;
			}

			Map<String, List<String>> mapList = new HashMap<String, List<String>>();
			// 商业险
			List<BusinessRisks> businessRisks = model.getBusinessRisks();
			List<String> provideIdsubList = new ArrayList<String>();//保存截取4位的供应商id
			List<String> provideIdList = new ArrayList<String>();//保存完整供应商id
			List<String> riskKindCodeList = new ArrayList<String>();//保存险别编码
			for (INSBQuoteinfo quoteinfo : insbQuoteinfos) {
				provideIdsubList.add(quoteinfo.getInscomcode().substring(0, 4));
				provideIdList.add(quoteinfo.getInscomcode());
			}
			for (BusinessRisks businessRisk : businessRisks) {
				riskKindCodeList.add(businessRisk.getCode());
			}
			//组织in查询条件
			Map<String, Object> maps = new HashMap<String, Object>();
			maps.put("provideIdsubList", provideIdsubList);
			maps.put("riskKindCodeList", riskKindCodeList);
			maps.put("provideIdList", provideIdList);		
			//查询此次请求所有供应商数据
			List<INSBProvider> insbProviders =insbProviderDao.selectListIn(maps);
			//查询此次请求所有供应商与配置的所有险别编码数据
			List<VerificationConfigBean> insuredConfigModels= new ArrayList<VerificationConfigBean>();
			//查询此次请求所有险别编码数据
			List<INSBRiskkindconfig> insbRiskkindconfigs=new ArrayList<INSBRiskkindconfig>();
			if (null != businessRisks && businessRisks.size() > 0) {
				insuredConfigModels= appInsuredQuoteDao.verificationInsuredConfigIn(maps);
				insbRiskkindconfigs = insbRiskkindconfigDao.selectListIn(maps);
				for (BusinessRisks businessRisk : businessRisks) {
					List<String> provids = new ArrayList<String>();
					for (INSBQuoteinfo quoteinfo : insbQuoteinfos) {
						//根据险别编码，和供应商id查询供应商是否支持该先别
						/*System.out.println("1---businessRisk="+businessRisk.getCode()+
								",getInscomcode="+quoteinfo.getInscomcode());
						long num = appInsuredQuoteDao.verificationInsuredConfig(businessRisk.getCode(),quoteinfo.getInscomcode().substring(0, 4));
						if(num <= 0){
							provids.add(quoteinfo.getInscomcode());
						}*/
						//根据险别编码，和供应商id查询供应商是否支持该先别，不支持的记录
						if(!isVerification(insuredConfigModels,businessRisk.getCode(),quoteinfo.getInscomcode().substring(0, 4))){
							provids.add(quoteinfo.getInscomcode());
						}
					}
					if(provids.size() > 0){
						mapList.put(businessRisk.getCode(), provids);
					}
				}
			}
			// 交强险  不做校验～
//			List<StrongRisk> strongRisks = model.getStrongRisk();
//			if (null != strongRisks && strongRisks.size() > 0) {
//				for (StrongRisk strongRisk : strongRisks) {
//					List<String> provids = new ArrayList<String>();
//					for (INSBQuoteinfo quoteinfo : insbQuoteinfos) {
//						//根据险别编码，和供应商id查询供应商是否支持该先别
//						long num = appInsuredQuoteDao.verificationInsuredConfig(strongRisk.getCode(),quoteinfo.getInscomcode().substring(0, 4));
//						if(num <= 0){
//							provids.add(quoteinfo.getInscomcode());
//						}
//					}
//					if(provids.size() > 0){
//						mapList.put(strongRisk.getCode(), provids);
//					}
//				}
//			}
			//组织返回险别校验数据
			List<GuiZeCheckBean> guiZeCheckBeans = new ArrayList<GuiZeCheckBean>();
			List<VerificationConfigBean> configBeans = new ArrayList<VerificationConfigBean>();
			for(Entry<String, List<String>> entry : mapList.entrySet()){
				VerificationConfigBean verificationConfigBean = new VerificationConfigBean();
				INSBRiskkindconfig insbRiskkindconfig = new INSBRiskkindconfig();
				insbRiskkindconfig.setRiskkindcode(entry.getKey());
				//INSBRiskkindconfig riskkindconfig = insbRiskkindconfigDao.selectOne(insbRiskkindconfig);
				INSBRiskkindconfig riskkindconfig = getINSBRiskkindconfig(insbRiskkindconfigs,entry.getKey());
				if(null != riskkindconfig){
					verificationConfigBean.setKindcode(riskkindconfig.getRiskkindcode());
					verificationConfigBean.setKindname(riskkindconfig.getRiskkindname());
				}
				List<Map<String, String>> providers = new ArrayList<Map<String,String>>();
				List<String> pids = entry.getValue();
				for(String id : pids){
					//INSBProvider insbProvider = insbProviderDao.selectById(id);
					INSBProvider insbProvider =getINSBProvider(insbProviders,id);
					Map<String, String> map = new HashMap<String, String>();
					if(null != insbProvider){
						map.put("prvshotname", insbProvider.getPrvshotname());
						map.put("prvname", insbProvider.getPrvname());
						map.put("logo", insbProvider.getLogo());
						GuiZeCheckBean guiZeCheckBean = new GuiZeCheckBean();//07-25 加上下面的数据
						guiZeCheckBean.setPid(insbProvider.getId());
						guiZeCheckBean.setPrvname(insbProvider.getPrvname());
						guiZeCheckBean.setLogo(insbProvider.getLogo());
						guiZeCheckBean.setPrvshotname(insbProvider.getPrvshotname());
						guiZeCheckBean.setReason("");
						guiZeCheckBeans.add(guiZeCheckBean);
					}
					providers.add(map);
				}
				if(providers.size() > 0){
					verificationConfigBean.setProviders(providers);
				}
				configBeans.add(verificationConfigBean);
			}

			//调用规则
			CheckInsuredConfigModel checkInsuredConfigModel = new CheckInsuredConfigModel();
			boolean flag = false;
			for (INSBQuoteinfo quoteinfo : insbQuoteinfos) {
				//INSBProvider insbProvider = insbProviderDao.selectById(quoteinfo.getInscomcode());
				INSBProvider insbProvider =getINSBProvider(insbProviders,quoteinfo.getInscomcode());
				//判断提前报价时间是否在许可范围内
				String judgeResult = "";

				//bug 2432 20160620 年款匹配不到转人工，jycode。rbcode获取不到转人工
				saveCarmodelinfoToData(taskid, quoteinfo.getInscomcode(), "");

				LogUtil.info("规则调用开始"+model.getProcessinstanceid()+"==供应商="+quoteinfo.getInscomcode());
				String result = appQuotationService.getQuotationValidatedInfo("",taskid,quoteinfo.getInscomcode());
                LogUtil.info("规则调用出参"+model.getProcessinstanceid()+"==供应商="+quoteinfo.getInscomcode()+": "+result);
				JSONObject jsonObject = JSONObject.fromObject(result);

				//{"success":false,"quotationMode":0,"resultMsg":["商业险、交强起保日期不一致需分开提交"]}   提示性规则:0,转人工规则:1,限制性规则:2,阻断性规则:3
				if(!jsonObject.getBoolean("success") || !StringUtil.isEmpty(judgeResult)){//规则校验没通过 或者开始报价时间大于规定的时间间隔
					if(null != insbProvider){
						if((!jsonObject.getBoolean("success") && jsonObject.containsKey("quotationMode")  && "3".equals(jsonObject.get("quotationMode").toString())) || !StringUtil.isEmpty(judgeResult)){
							GuiZeCheckBean guiZeCheckBean = new GuiZeCheckBean();
							guiZeCheckBean.setPid(insbProvider.getId());
							guiZeCheckBean.setPrvname(insbProvider.getPrvname());
							guiZeCheckBean.setLogo(insbProvider.getLogo());
							guiZeCheckBean.setPrvshotname(insbProvider.getPrvshotname());
							judgeResult = jsonObject.getString("resultMsg")+judgeResult;
							guiZeCheckBean.setReason(judgeResult);
							guiZeCheckBeans.add(guiZeCheckBean);
						}else{//规则校验通过
							flag = true;
						}
					}
				}else{//规则校验通过
					flag = true;
				}
			}
			checkInsuredConfigModel.setFlag(flag);
			checkInsuredConfigModel.setConfigBeans(configBeans);
			checkInsuredConfigModel.setGuiZeCheckBeans(guiZeCheckBeans);

			commonModel.setMessage("操作成功");
			commonModel.setStatus("success");
			commonModel.setBody(checkInsuredConfigModel);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}
	/**
	 * 根据供应商id，获取数据
	 * @param insbProviders 供应商数据来源list
	 * @param providerId 供应商id
	 * @return
	 */
	private INSBProvider getINSBProvider(List<INSBProvider> insbProviders,String providerId){
		INSBProvider insbProvider = null;
		for(INSBProvider provider : insbProviders){
			if(provider.getId().equals(providerId)){
				insbProvider=provider;
				break;
			}
		}
		return insbProvider;
		
	}
	/**
	 * 根据险别编码，获取数据
	 * @param insbRiskkindconfigs  险别数据来源list
	 * @param riskKindCode 险别编码
	 * @return
	 */
	private INSBRiskkindconfig getINSBRiskkindconfig(List<INSBRiskkindconfig> insbRiskkindconfigs,String riskKindCode){
		INSBRiskkindconfig riskkindconfig = null;
		for(INSBRiskkindconfig insbRiskkindconfig : insbRiskkindconfigs){
			if(insbRiskkindconfig.getRiskkindcode().equals(riskKindCode)){
				riskkindconfig=insbRiskkindconfig;
				break;
			}
		}
		return riskkindconfig;
		
	}
	/**
	 * 判断对象是否存在
	 * @param insuredConfigModels 数据来源list
	 * @param strKindCode 险别编码
	 * @param strProvideid 供应商id
	 * @return
	 */
	private boolean isVerification(List<VerificationConfigBean> insuredConfigModels ,String strKindCode,String strProvideid){

		String kindCode="";
		String prvoideid="";
		boolean flag=false;
		for(VerificationConfigBean configModel :insuredConfigModels){
			kindCode=configModel.getKindcode().toString();
			prvoideid=configModel.getProvideid();
			if(kindCode.equals(strKindCode)&&prvoideid.equals(strProvideid)){
				//System.out.println(configModel);
				flag=true;
				break;
			}
		}
		return flag;
	}
	/**
	 * bug 2400 20160628
	 * @param taskid
	 * @param inscomcode
	 */
	public void resetInsuredDate(String taskid,String inscomcode){
		LogUtil.info("投保日期重置resetInsuredDate"+taskid+"供应商id="+inscomcode);
		INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setTaskid(taskid);
		INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
		if(null != carinfo){
			LogUtil.info("投保日期重置resetInsuredDate"+taskid+"供应商id="+inscomcode+"当前车牌号="+carinfo.getCarlicenseno());
			//新车不做处理
			if(!"新车未上牌".equals(carinfo.getCarlicenseno()) && "0".equals(carinfo.getIsNew())){
//				INSBLastyearinsureinfo insbLastyearinsureinfo = new INSBLastyearinsureinfo();
//				insbLastyearinsureinfo.setSflag("2");
//				insbLastyearinsureinfo.setTaskid(taskid);
				INSBLastyearinsureinfo lastyearinsureinfo = insbRulequerycarinfoDao.queryLastYearClainInfo(taskid);
				if(null == lastyearinsureinfo){ //平台没返回转人工，记录当前日期
					LogUtil.info("投保日期重置resetInsuredDate"+taskid+"供应商id="+inscomcode+"当前车牌号="+carinfo.getCarlicenseno()+"平台未返回数据转人工了=");
					saveFlowerrorToManWork(taskid, inscomcode,"平台查询未返回数据，当前时间"+ModelUtil.conbertToStringsdf(new Date())+"，转人工处理","9");
				}else{
					String ptsyenddate = lastyearinsureinfo.getSyenddate();
					String ptjqenddate = lastyearinsureinfo.getJqenddate();
					LogUtil.info("投保日期重置resetInsuredDate"+taskid+"供应商id="+inscomcode+"平台返回商业险结束日期="+ptsyenddate+"平台返回交强险结束日期="+ptjqenddate);
					if(!StringUtil.isEmpty(ptsyenddate) || !StringUtil.isEmpty(ptjqenddate)){
						INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
						insbPolicyitem.setTaskid(taskid);
						insbPolicyitem.setInscomcode(inscomcode);
						List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(insbPolicyitem);
						if(null != insbPolicyitems && insbPolicyitems.size() > 0){
							LogUtil.info("投保日期重置resetInsuredDate"+taskid +"供应商id="+inscomcode+"保单表数据条数size="+insbPolicyitems.size());
							for(INSBPolicyitem policyitem : insbPolicyitems){
								if("0".equals(policyitem.getRisktype()) && !StringUtil.isEmpty(ptsyenddate)){
									LogUtil.info("投保日期重置resetInsuredDate"+taskid +"供应商id="+inscomcode+"商业险起保日期="+ModelUtil.conbertToString(policyitem.getStartdate())+"平台返回商业险结束日期="+ptsyenddate);
									Date startdate = newDateMatch(policyitem.getStartdate(),ptsyenddate);
									LogUtil.info("投保日期重置resetInsuredDate"+taskid +"供应商id="+inscomcode+"商业险新的起保日期="+ModelUtil.conbertToString(startdate));
									policyitem.setModifytime(new Date());
									policyitem.setStartdate(startdate);
									policyitem.setEnddate(ModelUtil.nowDateMinusOneDay(ModelUtil.nowDateAddOneYear(startdate, 1)));
									//更新保单表
									insbPolicyitemDao.updateById(policyitem);
								}else if("1".equals(policyitem.getRisktype()) && !StringUtil.isEmpty(ptjqenddate)){
									LogUtil.info("投保日期重置resetInsuredDate"+taskid +"供应商id="+inscomcode+"交强险起保日期="+ModelUtil.conbertToString(policyitem.getStartdate())+"平台返回交强险结束日期="+ptjqenddate);
									Date startdate = newDateMatch(policyitem.getStartdate(),ptjqenddate);
									LogUtil.info("投保日期重置resetInsuredDate"+taskid +"供应商id="+inscomcode+"交强险新的起保日期="+ModelUtil.conbertToString(startdate));
									policyitem.setModifytime(new Date());
									policyitem.setStartdate(startdate);
									policyitem.setEnddate(ModelUtil.nowDateMinusOneDay(ModelUtil.nowDateAddOneYear(startdate, 1)));
									//更新保单表
									insbPolicyitemDao.updateById(policyitem);
								}
							}
						}
					}
				}
			}
		}
	}

	private Date newDateMatch(Date tdate,String cifenddate){
		//日期格式保持一致 yyyy-MM-dd
		String result = ModelUtil.conbertToString(tdate);
		//前端录入起保日期与平台查询返回的结束日期相等,不做处理
		if(!result.startsWith(cifenddate.substring(0, 10))){
			String nowdate = ModelUtil.conbertToString(ModelUtil.nowDateAddOneDay(new Date()));
			if(result.startsWith(nowdate.substring(0, 10))){ //前端录入时间为T+1时，用cif返回的日期修正
				result = cifenddate;
			}
		}
		return ModelUtil.conbertStringToNyrDate(result);
	}

	public boolean chudanDeptisNanFengDept(String taskid, String inscomcode) {
		boolean result = false;
		INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
		insbQuotetotalinfo.setTaskid(taskid);
		INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
		if (null != quotetotalinfo) {
			INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
			insbQuoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
			insbQuoteinfo.setInscomcode(inscomcode);
			INSBQuoteinfo quoteinfo = insbQuoteinfoDao.selectOne(insbQuoteinfo);
			if (null != quoteinfo) {
				INSCDept inscDept = inscDeptDao.selectById(quoteinfo.getDeptcode());
				// 广东南枫平台
				if (null != inscDept && inscDept.getParentcodes().contains("1244000000")) {
					result = true;
				}
			}
		}
		LogUtil.info("chudanDeptisNanFengDept"+taskid+"供应商id="+inscomcode+"返回结果="+result);
		return result;
	}

	/**
	 * 非费改险种校验,不通过的直接阻断，新车，单交强转人工（直接放过）
	 * @param taskid
	 * @param inscomcode
	 * @return
	 */
	public String checkNonFeereform(String taskid,String inscomcode){
		String result = "";
		INSBCarinfo insbCarinfo = insbCarinfoDao.selectCarinfoByTaskId(taskid);
		LogUtil.info("非费改险种校验checkNonFeereform"+taskid+":当前供应商="+inscomcode+",车牌="+insbCarinfo.getCarlicenseno());
		//混保
		if(isMixedInsurance(taskid, inscomcode)){
			int res = insbQuoteVerifyService.verifyCommercial(taskid, inscomcode);
			LogUtil.info("非费改险种校验checkNonFeereform"+taskid+":当前供应商="+inscomcode+",车牌="+insbCarinfo.getCarlicenseno()+",混保返回值="+res);
			if(res == 0 || res == 2){
				result = "非费改险种校验不通过";
			}
		}
		return result;
	}

	/**
	 * 非费改险种校验
	 * @param taskid
	 * @param inscomcode
	 * @return
	 */
	public String checkNonFeereformTemp(String taskid,String inscomcode){
		INSBCarinfo insbCarinfo = insbCarinfoDao.selectCarinfoByTaskId(taskid);
		LogUtil.info("非费改险种校验checkNonFeereform"+taskid+":当前供应商="+inscomcode+",车牌="+insbCarinfo.getCarlicenseno());
		//新车
		if("新车未上牌".equals(insbCarinfo.getCarlicenseno()) || "1".equals(insbCarinfo.getIsNew())){
			boolean res = insbQuoteVerifyService.verifyNewVehicle(taskid, inscomcode);
			LogUtil.info("非费改险种校验checkNonFeereform"+taskid+":当前供应商="+inscomcode+",车牌="+insbCarinfo.getCarlicenseno()+",返回值="+res);
			if(!res){
				saveFlowerrorToManWork(taskid, inscomcode, "非费改规则校验不通过，转人工","6");
			}
		}else{
			//混保
			if(isMixedInsurance(taskid, inscomcode)){
				int res = insbQuoteVerifyService.verifyCommercial(taskid, inscomcode);
				LogUtil.info("非费改险种校验checkNonFeereform"+taskid+":当前供应商="+inscomcode+",车牌="+insbCarinfo.getCarlicenseno()+",混保返回值="+res);
				if(res == 0 || res == 2){
					// result = "校验不通过";
				}else if(res == 3){
					saveFlowerrorToManWork(taskid, inscomcode, "需要剔除所有商业险","6");
				}else if(res == 4){
					saveFlowerrorToManWork(taskid, inscomcode, "需要剔除交强险以及车船税","6");
				}else if(res == 5){
					saveFlowerrorToManWork(taskid, inscomcode, "校验通过,但指定（自定义车价）小于最低价,需要将车损保额设置为最低价","6");
				}else{
					// result = "";
				}
			}else{
				//单交强
				boolean res = insbQuoteVerifyService.verifyTraffic(taskid, inscomcode);
				LogUtil.info("非费改险种校验checkNonFeereform"+taskid+":当前供应商="+inscomcode+",车牌="+insbCarinfo.getCarlicenseno()+",单交强返回值="+res);
				if(!res){
					saveFlowerrorToManWork(taskid, inscomcode, "非费改规则校验不通过，转人工","6");
				}
			}
		}
		return "";
	}
	/**
	 * 判断是否混保，true 混保，false 单交强
	 * @param taskid
	 * @param inscomcode
	 * @return
	 */
	private boolean isMixedInsurance(String taskid,String inscomcode){
		boolean result = false;
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskid);
		insbPolicyitem.setInscomcode(inscomcode);
		List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(insbPolicyitem);
		if(null != insbPolicyitems && insbPolicyitems.size() > 0){
			if(insbPolicyitems.size() == 2){
				result =  true;
			}
		}
		return result;
	}

	/**
	 * 保存操作记录，工作流转人工判断标识
	 * @param taskid
	 * @param inscomcode
	 * @param errdesc 描述
	 * @param flowcode 对应INSBFlowerror表firoredi字段      1  规则返回的转人工，5年款匹配没有匹配到  6 非费改地区 走人工规则报价 7 特种车转人工   8 代理人备注    9  投保日期   10 转人工规则  11普通平台查询80秒为查到数据
	 */
	public void saveFlowerrorToManWork(String taskid,String inscomcode,String errdesc,String flowcode){
		LogUtil.info("saveFlowerrorToManWork:"+taskid+",供应商="+inscomcode+",描述="+errdesc);
		wipeFlowerrorOfManWork(taskid, inscomcode, flowcode);

		INSBFlowerror newflowerror = new INSBFlowerror();
		newflowerror.setOperator("caryear");
		newflowerror.setCreatetime(new Date());
		newflowerror.setTaskid(taskid);
		newflowerror.setInscomcode(inscomcode);
		newflowerror.setFlowcode(flowcode);//规则级别
		newflowerror.setFlowname("规则校验");
		newflowerror.setFiroredi("4");
		newflowerror.setTaskstatus("guize5");
		newflowerror.setErrordesc(errdesc);
		insbFlowerrorDao.insert(newflowerror);
	}

	private void wipeFlowerrorOfManWork(String taskid,String inscomcode,String flowcode) {
		INSBFlowerror insbFlowerror = new INSBFlowerror();
		insbFlowerror.setTaskid(taskid);
		insbFlowerror.setInscomcode(inscomcode);
		insbFlowerror.setFiroredi("4");
		insbFlowerror.setFlowcode(flowcode);
		List<INSBFlowerror> flowerrorList = insbFlowerrorDao.selectList(insbFlowerror);

		if(null != flowerrorList && !flowerrorList.isEmpty()){
			//备份已经存在的
			for (INSBFlowerror flowerror : flowerrorList) {
				flowerror.setModifytime(new Date());
				flowerror.setTaskid(taskid + "_temp");
				flowerror.setInscomcode(inscomcode);
				flowerror.setFlowname("规则校验_备份");
				flowerror.setFiroredi("4");
				insbFlowerrorDao.updateById(flowerror);
			}
		}
	}

	/**
	 * 车型年款匹配  2016-05-15
	 * @param taskid
	 * @param inscomcode
	 * @param subworkflowid
	 * @return true 匹配到合适车型
	 */
	public boolean saveCarmodelinfoToData(String taskid,String inscomcode,String subworkflowid){
		LogUtil.info("saveCarmodelinfoToData开始匹配年款：taskid:"+taskid +",inscomcode:"+inscomcode);
		boolean result = true;
		INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setTaskid(taskid);
		INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);

		if(null != carinfo){
			String vin = carinfo.getVincode();
			INSBCarmodelinfohis carmodelinfo = commonQuoteinfoService.getCarModelInfo(carinfo.getId(), inscomcode);
			com.zzb.cm.Interface.model.CarModelInfoBean carModelInfoBean = null;

			if(null != carmodelinfo){
				String pointPrice = "";
				if(null != carmodelinfo.getPolicycarprice())
					pointPrice = carmodelinfo.getPolicycarprice().toString();

				carModelInfoBean = interFaceService.getCarModelInfo(vin, subworkflowid, taskid, inscomcode, carmodelinfo,ModelUtil.conbertToString(carinfo.getRegistdate()),pointPrice);
				String errordesc = "车型年款无法匹配，转人工处理";
				if(carModelInfoBean != null && "-1".equals(carModelInfoBean.getId())){
					errordesc = carModelInfoBean.getFamilyname();
					carModelInfoBean = null;
				}
				
				if(null != carModelInfoBean){
					String jycode = carModelInfoBean.getJycode();
					String rbcode = carModelInfoBean.getRbcode();
					if(StringUtil.isEmpty(jycode)){
						LogUtil.info("saveCarmodelinfoToData：taskid:"+taskid +",inscomcode:"+inscomcode + ",errormsg=没有返回jycode，转人工处理");
						saveFlowerrorToManWork(taskid, inscomcode,"该车暂时无法自动报价，需要转人工处理，请点击“我要人工报价”!","5");
						result = false;
					}
					if(StringUtil.isEmpty(rbcode)){
						LogUtil.info("saveCarmodelinfoToData：taskid:"+taskid +",inscomcode:"+inscomcode + ",errormsg=没有返回rbcode，转人工处理");
						saveFlowerrorToManWork(taskid, inscomcode,"该车暂时无法自动报价，需要转人工处理，请点击“我要人工报价”!","5");
						result = false;
					}
					INSBCarmodelinfohis insbCarmodelinfohis = new INSBCarmodelinfohis();
					insbCarmodelinfohis.setCarinfoid(carinfo.getId());
					insbCarmodelinfohis.setInscomcode(inscomcode);
					INSBCarmodelinfohis carmodelinfohis = insbCarmodelinfohisDao.selectOne(insbCarmodelinfohis);
					//2016-04-12 bug 4135
					if(null == carmodelinfohis){
						carmodelinfohis = new INSBCarmodelinfohis();
						carmodelinfohis.setOperator(null==carmodelinfo.getOperator()?"":carmodelinfo.getOperator());
						carmodelinfohis.setCreatetime(new Date());
						carmodelinfohis.setAnalogyprice(carModelInfoBean.getAnalogyprice());
						carmodelinfohis.setAnalogytaxprice(carModelInfoBean.getAnalogytaxprice());
						carmodelinfohis.setBrandname(carModelInfoBean.getBrandname());
//						carmodelinfohis.setDisplacement(convertToDouble(carModelInfoBean.getDisplacement()));
						carmodelinfohis.setDisplacement(carmodelinfo.getDisplacement());//排气量按照前端修改后的提交
						carmodelinfohis.setFactoryname(carModelInfoBean.getFactoryname());
						carmodelinfohis.setFamilyname(carModelInfoBean.getFamilyname());
//						carmodelinfohis.setFullweight(convertToDouble(carModelInfoBean.getFullweight()));
						carmodelinfohis.setFullweight(carmodelinfo.getFullweight());
						carmodelinfohis.setGearbox(carModelInfoBean.getGearbox());
						carmodelinfohis.setCarVehicleOrigin(getCarVehicleOriginByVin(vin));
//						carmodelinfohis.setLoads(convertToDouble(carModelInfoBean.getModelLoads()));
//						carmodelinfohis.setUnwrtweight(convertToDouble(carModelInfoBean.getModelLoads()));
						carmodelinfohis.setLoads(carmodelinfo.getLoads());
						carmodelinfohis.setUnwrtweight(carmodelinfo.getUnwrtweight());
						carmodelinfohis.setPrice(carModelInfoBean.getPrice());
//						carmodelinfohis.setSeat(convertToInteger(carModelInfoBean.getSeat()));
						carmodelinfohis.setSeat(carmodelinfo.getSeat());
						carmodelinfohis.setTaxprice(carModelInfoBean.getTaxprice());
						carmodelinfohis.setSyvehicletypecode(carModelInfoBean.getSyvehicletype());
						carmodelinfohis.setSyvehicletypename(carModelInfoBean.getSyvehicletypename());
						carmodelinfohis.setVehicleid(carModelInfoBean.getVehicleId());
						carmodelinfohis.setStandardfullname(carModelInfoBean.getVehiclename());
						carmodelinfohis.setStandardname(carModelInfoBean.getVehiclename());
						carmodelinfohis.setJgVehicleType(getjgVehicleTypeAndCodeName("VehicleType",carModelInfoBean.getVehicletype())); ;
						carmodelinfohis.setListedyear(carModelInfoBean.getMaketdate());
						//carModelInfoBean.getMaketdate();
						carmodelinfohis.setCarinfoid(carinfo.getId());
						carmodelinfohis.setInscomcode(inscomcode);
						//指定车价
						carmodelinfohis.setCarprice(carmodelinfo.getCarprice());
						carmodelinfohis.setPolicycarprice(carmodelinfo.getPolicycarprice());
						carmodelinfohis.setJyCode(jycode);
						carmodelinfohis.setRbCode(rbcode);
						insbCarmodelinfohisDao.insert(carmodelinfohis);
					}else{
						carmodelinfohis.setOperator(null==carmodelinfo.getOperator()?"":carmodelinfo.getOperator());
						carmodelinfohis.setModifytime(new Date());
						carmodelinfohis.setAnalogyprice(carModelInfoBean.getAnalogyprice());
						carmodelinfohis.setAnalogytaxprice(carModelInfoBean.getAnalogytaxprice());
						carmodelinfohis.setBrandname(carModelInfoBean.getBrandname());
//						carmodelinfohis.setDisplacement(convertToDouble(carModelInfoBean.getDisplacement()));
						carmodelinfohis.setDisplacement(carmodelinfo.getDisplacement());
						carmodelinfohis.setFactoryname(carModelInfoBean.getFactoryname());
						carmodelinfohis.setFamilyname(carModelInfoBean.getFamilyname());
						//carmodelinfohis.setFullweight(convertToDouble(carModelInfoBean.getFullweight()));
						carmodelinfohis.setFullweight(carmodelinfo.getFullweight());
						carmodelinfohis.setGearbox(carModelInfoBean.getGearbox());
						carmodelinfohis.setCarVehicleOrigin(getCarVehicleOriginByVin(vin));
//						carmodelinfohis.setLoads(convertToDouble(carModelInfoBean.getModelLoads()));
//						carmodelinfohis.setUnwrtweight(convertToDouble(carModelInfoBean.getModelLoads()));
						carmodelinfohis.setLoads(carmodelinfo.getLoads());
						carmodelinfohis.setUnwrtweight(carmodelinfo.getUnwrtweight());
						carmodelinfohis.setPrice(carModelInfoBean.getPrice());
//						carmodelinfohis.setSeat(convertToInteger(carModelInfoBean.getSeat()));
						carmodelinfohis.setSeat(carmodelinfo.getSeat());
						carmodelinfohis.setTaxprice(carModelInfoBean.getTaxprice());
						carmodelinfohis.setSyvehicletypecode(carModelInfoBean.getSyvehicletype());
						carmodelinfohis.setSyvehicletypename(carModelInfoBean.getSyvehicletypename());
						carmodelinfohis.setVehicleid(carModelInfoBean.getVehicleId());
						carmodelinfohis.setStandardfullname(carModelInfoBean.getVehiclename());
						carmodelinfohis.setJgVehicleType(getjgVehicleTypeAndCodeName("VehicleType",carModelInfoBean.getVehicletype())); ;
						carmodelinfohis.setListedyear(carModelInfoBean.getMaketdate());
						//carModelInfoBean.getMaketdate();
						carmodelinfohis.setCarinfoid(carinfo.getId());
						carmodelinfohis.setInscomcode(inscomcode);
						//指定车价
						carmodelinfohis.setCarprice(carmodelinfo.getCarprice());
						carmodelinfohis.setPolicycarprice(carmodelinfo.getPolicycarprice());
						carmodelinfohis.setJyCode(jycode);
						carmodelinfohis.setRbCode(rbcode);
						insbCarmodelinfohisDao.updateById(carmodelinfohis);
					}

                    //非指定车价时使用返回的车型信息更新车损等险的保额
                    if (!"2".equals(carmodelinfo.getCarprice())) {
                        INSBCarkindprice insureCarkindprice = new INSBCarkindprice();
                        insureCarkindprice.setTaskid(taskid);
                        insureCarkindprice.setInscomcode(inscomcode);
                        List<INSBCarkindprice> carkindpricesList = insbCarkindpriceDao.selectList(insureCarkindprice);

                        if (null != carkindpricesList && carkindpricesList.size() > 0) {
                            for (INSBCarkindprice carkindprice : carkindpricesList) {
                                if (special.contains(carkindprice.getInskindcode())) {
                                    carkindprice.setAmount(carModelInfoBean.getPrice());
                                    try {
                                        insbCarkindpriceDao.updateById(carkindprice);
                                    } catch (Exception e) {
                                        LogUtil.error("根据筛选年款车型数据更新"+taskid+","+inscomcode+"险种"+carkindprice.getInskindcode()+"保额出错："+e.getMessage());
                                    }
                                }
                            }
                        }
                    }

                    wipeFlowerrorOfManWork(taskid, inscomcode, "5");

				}else{
					LogUtil.info("saveCarmodelinfoToData没有匹配到年款：taskid:"+taskid +",inscomcode:"+inscomcode+"==interFaceService.getCarModelInfo接口没返回数据");
					saveFlowerrorToManWork(taskid, inscomcode,errordesc,"5");
					result = false;
				}
			}
		}
		return result;
	}

	/**
	 *
	 * @param taskid
	 * @param inscomcode 供应商id
	 * @param advancequote 提前报价天数
	 * @return
	 */
	public String judgeAdvanceInsuranceDate(String taskid, String inscomcode,int advancequote) {
		String result = "";
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setInscomcode(inscomcode);
		insbPolicyitem.setTaskid(taskid);
		List<INSBPolicyitem> policyitems = insbPolicyitemDao.selectList(insbPolicyitem);
		if(null != policyitems && policyitems.size() > 0){
			for(INSBPolicyitem policyitem : policyitems){
				if("0".equals(policyitem.getRisktype())){
					int  days = ModelUtil.compareDateWithDay(policyitem.getStartdate(), advancequote);
					if(days < 0){
						result += "[商业险开始投保时间大于"+advancequote+"天]";
					}
				}else{
					int  days = ModelUtil.compareDateWithDay(policyitem.getStartdate(), advancequote);
					if(days < 0){
						result += "[交强险开始投保时间大于"+advancequote+"天]";
					}
				}
			}
		}
		return result;
	}

    /**
     * 平台查询
     * @param taskId 实例id
     * @param plateNumber 车牌号
     * @param owerName 车主
     * @return
     */
    private LastYearPolicyInfoBean queryLastInsuredInfoByCar(String taskId, String plateNumber, String owerName, String agentId, String quickflag,Boolean... cif){
        Map<String, String> params = new HashMap<>(3);
        params.put("taskId", taskId);
        params.put("vehicleNo", plateNumber);
        params.put("personName", owerName);
		if (StringUtil.isNotEmpty(quickflag)) {
			params.put("quickflag", quickflag);
		}
		if (cif != null && cif.length == 3) {
			params.put("cif_own", String.valueOf(cif[0]));
			params.put("cif_renewal", String.valueOf(cif[1]));
			params.put("cif_social", String.valueOf(cif[2]));
		}
        if(StringUtil.isEmpty(agentId)){
            INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
            insbQuotetotalinfo.setTaskid(taskId);
            INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
            if (quotetotalinfo != null && StringUtil.isNotEmpty(quotetotalinfo.getAgentnum())) {
                INSBAgent insbAgent = insbAgentDao.selectByJobnum(quotetotalinfo.getAgentnum());
                if (insbAgent != null) {
                    agentId = insbAgent.getId();
                }
            }
        }
        if(!StringUtil.isEmpty(agentId)){
            INSBAgent insbAgent = insbAgentDao.selectById(agentId);
            params.put("singlesite", insbAgent.getDeptid());
            params.put("agentnum", insbAgent.getJobnum());

			if (StringUtil.isNotEmpty(insbAgent.getDeptid())) {
				params.put("platCode", inscDeptService.getPingTaiDeptId(insbAgent.getDeptid()));
			}
        }


        LogUtil.info("==续保请求平台查询==" + JSONObject.fromObject(params).toString());
        String resultJson = null;
        try {
            resultJson = CloudQueryUtil.getLastYearInsureInfoByCarNumberAndCarOwerForRenewal(params);
        } catch (IOException e) {
            LogUtil.error("续保"+taskId+","+plateNumber+","+owerName+"请求平台查询出错：" + e.getMessage());
            e.printStackTrace();
        }
        LogUtil.info("==续保接收平台查询==" +taskId + ", resultJson = "+ resultJson);

        LastYearPolicyInfoBean yearPolicyInfoBean = null;

        if (StringUtil.isNotEmpty(resultJson)) {
            JSONObject jsonObject = JSONObject.fromObject(resultJson);
            if (jsonObject.containsKey("status")) {
                yearPolicyInfoBean = (LastYearPolicyInfoBean) JSONObject.toBean(jsonObject, LastYearPolicyInfoBean.class);

            }
        }

        return yearPolicyInfoBean;
    }

	/**
	 * 平台查询
	 * @param taskid 实例id
	 * @param plateNumber 车牌号
	 * @param provinceCode 市代码
	 * @param provid  上年投保公司id
	 * @param parityflag  比价 0 不比价 1比价
	 * @param agentid  代理人id，获取所属机构id
	 * @param vincode 车架号(江苏流程使用)
	 * @return
	 */
	private LastYearPolicyInfoBean queryLastInsuredInfoByPlateNumber(String taskid,String plateNumber,
			String cityCode,String provid,
			String parityflag,String agentid,
			String owerName,String personIdno,
			String provinceCode,
			String vincode, Boolean... cif){
		//quickflag  0 正常投保  1快速续保
		String quickflag = "0";
		//不传车辆信息
		JSONObject object = new JSONObject();
		object.put("flag", "XB");
		//车主名称
		if(!StringUtil.isEmpty(owerName)){
			object.put("personName", owerName);
		}
		if(!StringUtil.isEmpty(personIdno)){
			object.put("personIdno", personIdno);
		}
		//上年投保公司有就传，没有不传
		if(!StringUtil.isEmpty(provid)){
			quickflag = "1";
			object.put("robotId", provid);
		}
		if(!StringUtil.isEmpty(parityflag) && "1".equals(parityflag)){
			quickflag = "1";
		}
		object.put("quickflag", quickflag);
		JSONObject inParas = new JSONObject();
		inParas.put("car.specific.license", plateNumber);
		object.put("inParas", inParas);
		object.put("provinceCode", provinceCode);
		object.put("areaId", cityCode);
		object.put("eid", taskid);
		object.put("vincode", vincode);

		if (cif != null && cif.length == 3) {
			object.put("cif_own", cif[0]);
			object.put("cif_renewal", cif[1]);
			object.put("cif_social", cif[2]);
		}

		//获取代理人所属机构id
		if(!StringUtil.isEmpty(agentid)){
			INSBAgent insbAgent = insbAgentDao.selectById(agentid);
			object.put("singlesite", insbAgent.getDeptid());
			object.put("agentnum", insbAgent.getJobnum());

			if (StringUtil.isNotEmpty(insbAgent.getDeptid())) {
				object.put("platCode", inscDeptService.getPingTaiDeptId(insbAgent.getDeptid()));
			}
		}

        LogUtil.info("==请求平台查询==：" + object.toString());
		//测试数据
		//String resultJson = "{\"carModels\":[{\"analogyprice\":0,\"analogytaxprice\":0,\"brandname\":\"比亚迪\",\"carmodelCode\":\"\",\"carmodelsource\":\"\",\"configname\":\"鑫雅型\",\"displacement\":\"1.488\",\"factoryname\":\"比亚迪汽车有限公司\",\"familyid\":\"\",\"familyname\":\"比亚迪G3\",\"fullweight\":\"1180\",\"gearbox\":\"手动档\",\"gearboxtype\":\"\",\"jqvehicletype\":\"KA\",\"kindname\":\"\",\"maketdate\":\"201103\",\"manufacturer\":\"国产\",\"modelLoads\":\"\",\"price\":56900,\"seat\":\"5\",\"serchtimes\":\"\",\"supplierId\":\"\",\"syvehicletype\":\"KA\",\"syvehicletypename\":\"六座以下客车\",\"taxprice\":59332,\"type\":\"\",\"vehicleId\":\"402880883324fe95013333bcd4380340\",\"vehiclecode\":\"QCJ7152A\",\"vehiclename\":\"比亚迪QCJ7152A轿车\",\"vehicleno\":\"\",\"vehicletype\":\"轿车类\",\"yearstyle\":\"2011\"}],\"carowner\":{\"addrss\":\"\",\"email\":\"zzb520@baoxian.com\",\"idno\":\"442000197609024658\",\"idtype\":\"01\",\"mobile\":\"13610225931\",\"name\":\"卢桂荣\"},\"lastYearCarinfoBean\":{\"area\":\"\",\"brandimg\":\"http://119.29.112.30/MK0080/BYA0.jpg\",\"brandmodel\":\"比亚迪QCJ7152A轿车\",\"chgownerflag\":\"0\",\"engineno\":\"211316788\",\"registerdate\":\"2011-03-01 00:00:00\",\"usingnature\":\"1\",\"vehicleframeno\":\"LGXC16DF8B0034110\",\"vehicleno\":\"粤TCJ760\",\"vehicletype\":\"0\"},\"lastYearClaimBean\":{\"bwcommercialclaimtimes\":\"\",\"bwcompulsoryclaimtimes\":\"新保或上年发生一次有责任不涉及死亡理赔\",\"claimrate\":0,\"claimtimes\":0,\"compulsoryclaimrate\":1,\"compulsoryclaimratereasons\":\"新保或上年发生一次有责任不涉及死亡理赔\",\"firstinsuretype\":\"\",\"jqclaimrate\":1,\"jqclaims\":[{\"caseEndTime\":\"2015-01-25 00:00:00.0\",\"caseStartTime\":\"2015-01-24 00:00:00.0\",\"insCorpCode\":\"CICP\",\"insCorpName\":\"中华联合财产保险公司\",\"policyId\":\"PDAA201544190000122719\"}],\"jqclaimtimes\":\"0\",\"jqlastclaimsum\":0,\"lastclaimsum\":0,\"noclaimdiscountcoefficient\":\"\",\"noclaimdiscountcoefficientreasons\":\"\",\"syclaims\":[{\"caseEndTime\":\"2015-01-25 00:00:00.0\",\"caseStartTime\":\"2015-01-24 00:00:00.0\",\"insCorpCode\":\"CICP\",\"insCorpName\":\"中华联合财产保险公司\",\"policyId\":\"PDAA201544190000122719\"}],\"trafficoffence\":\"0\",\"trafficoffencediscount\":0},\"lastYearPolicyBean\":{\"discount\":0,\"enddate\":\"2016-03-30 00:00:00\",\"insureder\":{\"addrss\":\"\",\"email\":\"\",\"idno\":\"\",\"idtype\":\"\",\"mobile\":\"\",\"name\":\"\"},\"jqenddate\":null,\"jqpolicyno\":\"\",\"jqprem\":0,\"jqstartdate\":null,\"policyno\":\"8105024420160000531000\",\"proper\":{\"addrss\":\"\",\"email\":\"\",\"idno\":\"\",\"idtype\":\"\",\"mobile\":\"\",\"name\":\"\"},\"startdate\":\"2015-03-31 00:00:00\",\"sumprem\":2125,\"syprem\":2125,\"tax\":0},\"lastYearRiskinfos\":[],\"lastYearSupplierBean\":{\"supplierid\":\"208844              \",\"suppliername\":\"鼎和财产保险股份有限公司广东分公司\"},\"message\":\"成功\",\"status\":\"0\"}";
		String resultJson = CloudQueryUtil.getLastYearInsurePolicy(object.toString());
        LogUtil.info("==接收平台查询==：" +taskid + ", resultJson = "+ resultJson);

		LastYearPolicyInfoBean yearPolicyInfoBean = null;
		if(resultJson != null && resultJson.contains("status")){
			yearPolicyInfoBean = com.alibaba.fastjson.JSONObject.parseObject(resultJson, LastYearPolicyInfoBean.class);
		}

		return yearPolicyInfoBean;
	}

	@Override
	public CommonModel queryLastInsuredInfo(String processinstanceid,
			String plateNumber) {
		CommonModel commonModel = new CommonModel();
		try {
			if (StringUtil.isEmpty(processinstanceid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			if (StringUtil.isEmpty(plateNumber)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("车牌号不能为空");
				return commonModel;
			}
			LastYearPolicyInfoBean lastYearPolicyInfoBean = queryLastInsuredInfoByPlateNumber(processinstanceid,plateNumber,"","","0","","","","","");
			if(null != lastYearPolicyInfoBean && "0".equals(lastYearPolicyInfoBean.getStatus())){
				commonModel.setStatus("success");
				commonModel.setMessage("平台查询成功");
				commonModel.setBody(lastYearPolicyInfoBean);
			}else{
				commonModel.setStatus("fail");
				commonModel.setMessage("平台查询没查到数据");
			}
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel getProvider(String taskid,String inscomcode) {
		ExtendCommonModel commonModel = new ExtendCommonModel();
		try {
			if(StringUtil.isEmpty(taskid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			if(StringUtil.isEmpty(inscomcode)){
				commonModel.setStatus("fail");
				commonModel.setMessage("供应商id不能为空");
				return commonModel;
			}
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
			insbQuoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
			insbQuoteinfo.setInscomcode(inscomcode);
			INSBQuoteinfo quoteinfo = insbQuoteinfoDao.selectOne(insbQuoteinfo);
//			List<BranchProBean> branchProBeans = getBranchProByPid(quoteinfo.getAgreementid());
			List<BranchProBean> branchProBeans = new ArrayList<BranchProBean>();
			// 出单网点
			List<SingleSiteBean> singleSiteBeans = new ArrayList<SingleSiteBean>();
			BranchProBean branchProBean = new BranchProBean();
			INSCDept inscDept = inscDeptDao.selectById(quoteinfo.getDeptcode());
			INSCDept platform = inscDeptService.getPlatformDept(inscDept.getId());//平台编码
			//
			if (null != inscDept) {
				branchProBean.setDeptId(inscDept.getId());
				branchProBean.setComcode(inscDept.getComcode());
				branchProBean.setComname(inscDept.getComname());
				branchProBean.setShortname(inscDept.getShortname());
				branchProBean.setPlatform(platform.getComcode());
				SingleSiteBean singleSiteBean = new SingleSiteBean();
				singleSiteBean.setSiteId(inscDept.getId());
				singleSiteBean.setSiteName(inscDept.getComname());
				singleSiteBean.setSiteAddress(inscDept.getAddress());
				singleSiteBean.setSiteShortName(inscDept.getShortname());
				singleSiteBean.setSelected(true);
				singleSiteBeans.add(singleSiteBean);
			}
			branchProBean.setSingleSiteBeans(singleSiteBeans);
			branchProBeans.add(branchProBean);
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(branchProBeans);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel getProviderSingleSite(String processinstanceid,String pid) {
		ExtendCommonModel commonModel = new ExtendCommonModel();
		try {
			if(StringUtil.isEmpty(processinstanceid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			if(StringUtil.isEmpty(pid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("供应商id不能为空");
				return commonModel;
			}
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(processinstanceid);
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
			insbQuoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
			insbQuoteinfo.setInscomcode(pid);
			INSBQuoteinfo quoteinfo = insbQuoteinfoDao.selectOne(insbQuoteinfo);
			List<BranchProBean> branchProBeans = getBranchProByPid(quoteinfo.getAgreementid());
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(branchProBeans);
			commonModel.setExtend(isHavePolicyNo(processinstanceid, pid));
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}
	/**
	 * 是否有投保单号
	 * @param taskid
	 * @param inscomcode
	 * @return true有
	 */
	private boolean isHavePolicyNo(String taskid,String inscomcode){
		boolean havepolicyno = false;
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskid);
		insbPolicyitem.setInscomcode(inscomcode);
		List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(insbPolicyitem);
		if(null != insbPolicyitems && insbPolicyitems.size() > 0){
			for(INSBPolicyitem policyitem : insbPolicyitems){
				if(!StringUtil.isEmpty(policyitem.getProposalformno())){
					havepolicyno = true;
					break;
				}
			}
		}
		return havepolicyno;
	}

	/**
	 * 根据协议id查询，分公司，出单网点
	 * @param agreeid
	 * @return
	 */
	private List<BranchProBean> getBranchProByPid(String agreeid) {
		// 根据协议id，查询出单网点表，获取分公司和网点id,对应的机构id，以及地址信息
		INSBOutorderdept insbOutorderdept = new INSBOutorderdept();
		insbOutorderdept.setAgreementid(agreeid);
		List<INSBOutorderdept> insbOutorderdepts = insbOutorderdeptDao
				.selectList(insbOutorderdept);
		// 分公司去除重复值,分公司出单网点对应关系
		Map<String, List<String>> dataNeed = new HashMap<String, List<String>>();
		for (INSBOutorderdept outorderdept : insbOutorderdepts) {
			// 出单网点
			List<String> temp = new ArrayList<String>();
			temp.add(outorderdept.getDeptid5());
			if (null != dataNeed.get(outorderdept.getDeptid4())) {
				List<String> tempOld = dataNeed.get(outorderdept.getDeptid4());
				temp.addAll(tempOld);
			}
			dataNeed.put(outorderdept.getDeptid4(), temp);
		}
		// 分公司列表
		List<BranchProBean> branchProBeans = new ArrayList<BranchProBean>();
		for (Map.Entry<String, List<String>> entry : dataNeed.entrySet()) {
			BranchProBean branchProBean = new BranchProBean();
			INSCDept inscDept = inscDeptDao.selectById(entry.getKey());
			if (null != inscDept) {
				branchProBean.setDeptId(inscDept.getId());
				branchProBean.setComcode(inscDept.getComcode());
				branchProBean.setComname(inscDept.getComname());
				branchProBean.setShortname(inscDept.getShortname());
			}
			// 出单网点
			List<SingleSiteBean> singleSiteBeans = new ArrayList<SingleSiteBean>();
			int i = 0;
			for (String deptid : entry.getValue()) {
				SingleSiteBean singleSiteBean = new SingleSiteBean();
				INSCDept siteDept = inscDeptDao.selectById(deptid);
				if (null != siteDept) {
					singleSiteBean.setSiteId(siteDept.getId());
					singleSiteBean.setSiteName(siteDept.getComname());
					singleSiteBean.setSiteAddress(siteDept.getAddress());
					singleSiteBean.setSiteShortName(siteDept.getShortname());
					singleSiteBean.setSiteShortName(siteDept.getShortname());
					if (i == 0) {
						singleSiteBean.setSelected(true);
					} else {
						singleSiteBean.setSelected(false);
					}
					i++;
					singleSiteBeans.add(singleSiteBean);
				}
			}
			branchProBean.setSingleSiteBeans(singleSiteBeans);
			branchProBeans.add(branchProBean);
		}
		return branchProBeans;
	}

	@Override
	public CommonModel updateSingleSite(UpdateSingleSiteModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if(StringUtil.isEmpty(taskid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			if(StringUtil.isEmpty(model.getPid())){
				commonModel.setStatus("fail");
				commonModel.setMessage("供应商id不能为空");
				return commonModel;
			}
			if(StringUtil.isEmpty(model.getSiteid())){
				commonModel.setStatus("fail");
				commonModel.setMessage("出单网点id不能为空");
				return commonModel;
			}
			INSBOrder insbOrder = new INSBOrder();
			insbOrder.setTaskid(taskid);
			insbOrder.setPrvid(model.getPid());
			INSBOrder order = insbOrderDao.selectOne(insbOrder);
			if(null != order){
				//订单表出单网点
				order.setDeptcode(model.getSiteid());
				insbOrderDao.updateById(order);
			}
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			if(null == quotetotalinfo){
				commonModel.setStatus("fail");
				commonModel.setMessage("报价总表信息不存在");
				return commonModel;
			}
			INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
			insbQuoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
			insbQuoteinfo.setInscomcode(model.getPid());
			insbQuoteinfo = insbQuoteinfoDao.selectOne(insbQuoteinfo);
			if(null == insbQuoteinfo){
				commonModel.setStatus("fail");
				commonModel.setMessage("该供应商对应报价表信息不存在");
				return commonModel;
			}

			insbQuoteinfo.setDeptcode(model.getSiteid());

			if (StringUtil.isNotEmpty(model.getSiteid())) {
				INSCDept dept = inscDeptDao.selectByComcode(model.getSiteid());
				if (dept != null) {
					String platformInnercode = inscDeptService.getPlatformInnercode(dept.getDeptinnercode());
					if (platformInnercode != null) {
						insbQuoteinfo.setPlatforminnercode(Integer.parseInt(platformInnercode));
					}
				}
			}

			insbQuoteinfoDao.updateById(insbQuoteinfo);

			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel queryTermsByAgreeid(String pid) {
		CommonModel commonModel = new CommonModel();
		try {
			if(StringUtil.isEmpty(pid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("供应商id不能为空");
				return commonModel;
			}
			INSBElfconf insbElfconf = appInsuredQuoteDao.selectOneElfconf(pid);
			if(null != insbElfconf){
				String elfid = insbElfconf.getId();
				List<Map<String, String>> result = queryElfInfoByIdAndCode("inputItem",elfid);
				commonModel.setStatus("success");
				commonModel.setMessage("操作成功");
				commonModel.setBody(result);
			}else{
				commonModel.setStatus("fail");
				commonModel.setMessage("该协议下没有可用精灵信息");
				return commonModel;
			}
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	private List<Map<String, String>> queryElfInfoByIdAndCode(String code,String elfid){
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", code);
		map.put("elfid", elfid);
		return appInsuredQuoteDao.selectInputCodeByElfId(map);
	}

	@Override
	public CommonModel saveUploadImage(SaveUploadImageModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if(StringUtil.isEmpty(taskid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("供应商id不能为空");
				return commonModel;
			}
			//如果已经存在，先删除
			INSBFilebusiness filebusiness = new INSBFilebusiness();
			filebusiness.setCode(taskid);
			List<INSBFilebusiness> filebusinesses = insbFilebusinessDao.selectList(filebusiness);
			if(null != filebusinesses && filebusinesses.size() > 0){
				for(INSBFilebusiness insbFilebusiness : filebusinesses){
					insbFilebusinessDao.deleteById(insbFilebusiness.getId());
				}
			}
			//插入新的数据
			List<String> fids = model.getFileids();
			if(null != fids && fids.size() > 0){
				for(String fid : fids){
					if(!StringUtil.isEmpty(fid)){
						INSBFilebusiness insbFilebusiness = new INSBFilebusiness();
						insbFilebusiness.setCreatetime(new Date());
						insbFilebusiness.setOperator(null == model.getOperator()?"":model.getOperator());
						insbFilebusiness.setType("insuranceimage");
						insbFilebusiness.setCode(taskid);
						insbFilebusiness.setFilelibraryid(fid);
						insbFilebusinessDao.insert(insbFilebusiness);
					}
				}
				commonModel.setStatus("success");
				commonModel.setMessage("操作成功");
			}else{
				commonModel.setStatus("fail");
				commonModel.setMessage("没有上传影像");
			}

		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel carModelInfo(SelectCarModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if(StringUtil.isEmpty(taskid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			INSBCarinfo insbCarinfo = new INSBCarinfo();
			insbCarinfo.setTaskid(taskid);
			INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
			if(null == carinfo){
				commonModel.setStatus("fail");
				commonModel.setMessage("车辆信息不存在");
				return commonModel;
			}
			INSBCarmodelinfo insbCarmodelinfo = new INSBCarmodelinfo();
			insbCarmodelinfo.setCarinfoid(carinfo.getId());
			INSBCarmodelinfo carmodelinfo = insbCarmodelinfoDao.selectOne(insbCarmodelinfo);
			//存在更新，不存在插入
			if(null == carmodelinfo){
				carmodelinfo = new INSBCarmodelinfo();
				carmodelinfo.setCarinfoid(carinfo.getId());
				carmodelinfo.setOperator(carinfo.getOperator());
				carmodelinfo.setCreatetime(new Date());
				carmodelinfo.setAnalogyprice(model.getAnalogyprice());
				carmodelinfo.setAnalogytaxprice(model.getAnalogytaxprice());
				carmodelinfo.setBrandname(model.getBrandname());
				carmodelinfo.setCarVehicleOrigin(getVehicleTypeByCodeName("CarVehicleOrigin",model.getCarVehicleOrigin()));
				carmodelinfo.setDisplacement(model.getDisplacement());
				carmodelinfo.setFamilyname(model.getFamilyname());
				carmodelinfo.setFullweight(model.getFullweight());;
				carmodelinfo.setGearbox(model.getGearbox());
				carmodelinfo.setJgVehicleType(getjgVehicleTypeAndCodeName("VehicleType",model.getJgVehicleType()));
				carmodelinfo.setListedyear(model.getListedyear());
				carmodelinfo.setLoads(model.getLoads());
				carmodelinfo.setUnwrtweight(model.getLoads());
				carmodelinfo.setPrice(model.getPrice());
				carmodelinfo.setSeat(model.getSeat());
				carmodelinfo.setStandardname(model.getStandardname());
				carmodelinfo.setTaxprice(model.getTaxPrice());
				carmodelinfo.setStandardfullname(model.getStandardname());
				carmodelinfo.setVehicleid(model.getVehicleid());//精友车型id
				carmodelinfo.setSyvehicletypename(model.getSyvehicletypename());//商业险车辆名称
				carmodelinfo.setSyvehicletypecode(model.getSyvehicletypecode());//机动车编码
				//标准车型
				carmodelinfo.setIsstandardcar("0");
				//查找问题
				//carmodelinfo.setNoti("精友查询到的"+new Date());
				insbCarmodelinfoDao.insert(carmodelinfo);
                LogUtil.info("=====精友车型查询插入车型信息======taskid:"+taskid+" carmodelid:"+carmodelinfo.getId()+" Vehicleid:"+carmodelinfo.getVehicleid());
			}else{
				carmodelinfo.setModifytime(new Date());
				carmodelinfo.setAliasname(model.getAliasname());
				carmodelinfo.setAnalogyprice(model.getAnalogyprice());
				carmodelinfo.setAnalogytaxprice(model.getAnalogytaxprice());
				carmodelinfo.setBrandname(model.getBrandname());
				carmodelinfo.setCarVehicleOrigin(getVehicleTypeByCodeName("CarVehicleOrigin",model.getCarVehicleOrigin()));
				carmodelinfo.setDisplacement(model.getDisplacement());
				carmodelinfo.setFamilyname(model.getFamilyname());
				carmodelinfo.setFullweight(model.getFullweight());;
				carmodelinfo.setGearbox(model.getGearbox());
				carmodelinfo.setJgVehicleType(getjgVehicleTypeAndCodeName("VehicleType",model.getJgVehicleType()));
				carmodelinfo.setListedyear(model.getListedyear());
				carmodelinfo.setLoads(model.getLoads());
				carmodelinfo.setUnwrtweight(model.getLoads());
				carmodelinfo.setPrice(model.getPrice());
				carmodelinfo.setSeat(model.getSeat());
				carmodelinfo.setStandardname(model.getStandardname());
				carmodelinfo.setTaxprice(model.getTaxPrice());
				carmodelinfo.setVehicleid(model.getVehicleid());//精友车型id
				carmodelinfo.setSyvehicletypename(model.getSyvehicletypename());//商业险车辆名称
				carmodelinfo.setSyvehicletypecode(model.getSyvehicletypecode());//机动车编码
				//标准车型
				carmodelinfo.setIsstandardcar("0");
				//查找问题
				//carmodelinfo.setNoti("精友查询到的"+new Date());
				insbCarmodelinfoDao.updateById(carmodelinfo);
                LogUtil.info("=====精友车型查询更新车型信息======taskid:"+taskid+" carmodelid:"+carmodelinfo.getId()+" Vehicleid:"+carmodelinfo.getVehicleid());
			}
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel queryLastInsuredByNumOrCar(QueryLastInsuredByNumOrCarModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if(StringUtil.isEmpty(taskid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("实例不能为空");
				return commonModel;
			}
			Map<String, String> input = model.getInputData();
			if(null == input || input.size() < 0){
				commonModel.setStatus("fail");
				commonModel.setMessage("必录项不能为空");
				return commonModel;
			}
			String proid = model.getProid();
			if(StringUtil.isEmpty(proid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("供应商id不能为空");
				return commonModel;
			}
			String agentId = model.getAgentId();
			if(StringUtil.isEmpty(agentId)){
				commonModel.setStatus("fail");
				commonModel.setMessage("代理人id不能为空");
				return commonModel;
			}
			//查询投保区域,代理人所属机构是市区域
			INSCDept inscDept = appInsuredQuoteDao.sellectCityAreaByAgreeid(agentId);
			if(null == inscDept || StringUtil.isEmpty(inscDept.getCity())){
				commonModel.setStatus("fail");
				commonModel.setMessage("代理人所属机构不存在");
				return commonModel;
			}
			//查询报价表
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			if(null == quotetotalinfo){
				commonModel.setStatus("fail");
				commonModel.setMessage("报价总表信息不存在");
				return commonModel;
			}

			//查询精灵信息
			INSBElfconf insbElfconf = appInsuredQuoteDao.selectOneElfconf(proid);
			String elfid = "";
			if(null != insbElfconf){
				elfid = insbElfconf.getId();
			}else{
				commonModel.setStatus("fail");
				commonModel.setMessage("供应商精灵信息不存在");
				return commonModel;
			}
			LastYearPolicyInfoBean yearPolicyInfoBean = getLastYearPolicyInfoBean(input,taskid,proid,elfid,inscDept.getCity(),inscDept.getId());
			if(null != yearPolicyInfoBean){
				if("0".equals(yearPolicyInfoBean.getStatus())){
					commonModel.setStatus("success");
					commonModel.setMessage("查询成功");
					//返回值放入ridis,key值 实例id
					cachelastYearPolicyInfo(taskid, yearPolicyInfoBean);
					//保存
					Map<String, Object> resultMap = new HashMap<String, Object>();
					resultMap =  noticeResult(yearPolicyInfoBean,quotetotalinfo.getInsprovincecode(),quotetotalinfo.getInscitycode(),agentId,"",taskid);
					if(resultMap.isEmpty()){
						resultMap.put("insureinfo", false);
						resultMap.put("carinfo", false);
						resultMap.put("carmodel", false);
						resultMap.put("provider", false);
						resultMap.put("insuredconf", false);
						saveAllPersonInfo(taskid);
					}
					commonModel.setBody(resultMap);
					//保存
					saveLastYearPolicyInfoBean(taskid,yearPolicyInfoBean,quotetotalinfo.getInsprovincecode(),quotetotalinfo.getInscitycode(),agentId,"",quotetotalinfo.getOperator(),"0");
					//只有查询成功才保存出险信息
					if("0".equals(yearPolicyInfoBean.getStatus())){
						//保存出险信息
						saveLastYearClaimsInfo(taskid, quotetotalinfo.getOperator(), yearPolicyInfoBean);
					}
				}else if("1".equals(yearPolicyInfoBean.getStatus())){
					commonModel.setStatus("fail");
					commonModel.setMessage("等待查询结果返回");
				}else{
					commonModel.setStatus("fail");
					commonModel.setMessage("没有查询结果");
				}
			}else{
				commonModel.setStatus("fail");
				commonModel.setMessage("平台查询失败了");
			}

		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}
	//针对精灵
	private LastYearPolicyInfoBean getLastYearPolicyInfoBean(Map<String, String> input,String taskid,String pid,String elfid,String areaId,String deptid) {
		//云查询精灵查询传入参数
		JSONObject object = new JSONObject();
		object.put("flag", "XB");
		//0 正常投保 1快速续保
		object.put("quickflag", "0");
		JSONObject inParas = new JSONObject();
		for(Map.Entry<String, String> entry : input.entrySet()){
			inParas.put(entry.getKey().trim(), entry.getValue().trim());
		}
		object.put("inParas", inParas);
		JSONObject carinfo = new JSONObject();

		INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setTaskid(taskid);
		INSBCarinfo oneCarinfo = insbCarinfoDao.selectOne(insbCarinfo);
		String vehiclename = "";
		if(null != oneCarinfo){
			INSBCarmodelinfo carmodelinfo = new INSBCarmodelinfo();
			carmodelinfo.setCarinfoid(oneCarinfo.getId());
			INSBCarmodelinfo insbCarmodelinfo = insbCarmodelinfoDao.selectOne(carmodelinfo);
			if(null != insbCarmodelinfo){
				vehiclename = insbCarmodelinfo.getStandardfullname();
			}
			carinfo.put("engineno", oneCarinfo.getEngineno());//发动机号
			carinfo.put("vehicleframeno", oneCarinfo.getVincode());//车架号
			carinfo.put("registerdate", DateUtil.toString(oneCarinfo.getRegistdate()));//初登日期
			carinfo.put("vehiclename", vehiclename);//品牌型号
			carinfo.put("chgownerflag", "0");//是否过户车
		}
		object.put("carinfo", carinfo);
		object.put("areaId", areaId);
		object.put("eid", taskid);

		LastYearPolicyInfoBean lastYearPolicyInfoBean = null;
		try {
			String resultJson = CloudQueryUtil.getLastYearInsurePolicy(object.toString());
			JSONObject jsonObject=JSONObject.fromObject(resultJson);
			lastYearPolicyInfoBean = (LastYearPolicyInfoBean) JSONObject.toBean(jsonObject, LastYearPolicyInfoBean.class);
		} catch (Exception e) {
			lastYearPolicyInfoBean = null;
			e.printStackTrace();
		}
		return lastYearPolicyInfoBean;
	}


	@Override
	public CommonModel lastInsuredRenewaByCar(LastInsuredRenewaByCarModel model) {
        ExtendCommonModel commonModel = new ExtendCommonModel();
        try {
            String taskid = model.getProcessinstanceid();
            if (StringUtil.isEmpty(taskid)) {
                commonModel.setStatus("fail");
                commonModel.setMessage("实例id不能为空");
                return commonModel;
            }
            if (StringUtil.isEmpty(model.getCarNumber())) {
                commonModel.setStatus("fail");
                commonModel.setMessage("快速续保，车牌号不能为空");
                return commonModel;
            }
            if (StringUtil.isEmpty(model.getCarOwer())) {
                commonModel.setStatus("fail");
                commonModel.setMessage("快速续保，车主不能为空");
                return commonModel;
            }
			//权限包
			Map<String,Object> permissionMap = appPermissionService.findPermission("", taskid, "renewal");
			if ((int)permissionMap.get("status") == 2 || (int)permissionMap.get("status") == -1) {
				commonModel.setStatus(CommonModel.STATUS_FAIL);
				commonModel.setMessage((String) permissionMap.get("message"));
				return commonModel;
			}
			if ((int)permissionMap.get("status") == 1) {
				commonModel.setExtend(permissionMap);
			}
            Map<String, Object> map = new HashMap<String, Object>();

            //当前投保公司是否与上年投保公司一致
            boolean isLastProviderMatch = false;
            //上年投保公司
            String lastprovider = "";
            String lastprovidername = "";

            // 根据车牌等信息进行平台查询
            LastYearPolicyInfoBean lastYearPolicyInfoBean = null;

			//权限包
			permissionMap =new HashMap<String,Object>();
			List<Boolean> cif = new ArrayList<Boolean>();
			List<AgentRelatePermissionVo> permissionVoList = new ArrayList<AgentRelatePermissionVo>();
			boolean hasPrivilege = false;
			if (StringUtil.isEmpty(redisClient.get(Constants.TASK, "cif" + taskid))) {
				String[] ciflist={"cif_own","cif_renewal","cif_social"};
				for (String s: ciflist) {
					permissionMap = appPermissionService.findPermission(model.getAgentid(), "", s);
					permissionVoList.add((AgentRelatePermissionVo)permissionMap.get("AgentRelatePermissionVo"));
					if ((int)permissionMap.get("status") == 0) {
						cif.add(Boolean.TRUE);
						hasPrivilege = true;
					} else {
						cif.add(Boolean.FALSE);
					}
				}
				if (hasPrivilege) {
					lastYearPolicyInfoBean = queryLastInsuredInfoByCar(taskid, model.getCarNumber(), model.getCarOwer(), model.getAgentid(), "",cif.toArray(new Boolean[3]));
					redisClient.set(Constants.TASK, "cif" + taskid,taskid,60*60);
					redisClient.setList(Constants.TASK, "cifprivilege" + taskid,cif,60*60);
					redisClient.setList(Constants.TASK, "cifAgentRelatePermission" + taskid,permissionVoList,60*60);
					appPermissionService.updatePermission(lastYearPolicyInfoBean, permissionVoList, taskid);
				} else {
					map.put("permissionStatus", CommonModel.STATUS_FAIL);
					map.put("permissionMessage", "no cif query privilege");
				}

			} else {
				cif=redisClient.getList(Constants.TASK, "cifprivilege" + taskid,Boolean.class);
				permissionVoList=redisClient.getList(Constants.TASK, "cifAgentRelatePermission" + taskid,AgentRelatePermissionVo.class);
				lastYearPolicyInfoBean = queryLastInsuredInfoByCar(taskid, model.getCarNumber(), model.getCarOwer(), model.getAgentid(),"", cif.toArray(new Boolean[3]));
				appPermissionService.updatePermission(lastYearPolicyInfoBean, permissionVoList, taskid);
			}


            //0 符合续保要求 1 需要等待精灵查询 2 不符合续保要求
            if(null != lastYearPolicyInfoBean && ("0".equals(lastYearPolicyInfoBean.getStatus()) || "1".equals(lastYearPolicyInfoBean.getStatus()))){

                //上年投保公司信息
                LastYearSupplierBean supplierBean = lastYearPolicyInfoBean.getLastYearSupplierBean();
                if(null != supplierBean){
                    if (StringUtil.isNotEmpty(supplierBean.getSupplierid()) && supplierBean.getSupplierid().length() >= 4 &&
                            StringUtil.isNotEmpty(model.getLastInsuredComcode()) && model.getLastInsuredComcode().length() >= 4) {
                        if (supplierBean.getSupplierid().substring(0, 4).equals(model.getLastInsuredComcode().substring(0, 4))) {
                            isLastProviderMatch = true;
                        }
                    }
                    lastprovider = supplierBean.getSupplierid();
                    lastprovidername = supplierBean.getSuppliername();
                }

                INSBAgent agent = insbAgentDao.selectById(model.getAgentid());

                //保存
                saveLastYearPolicyInfoBean(taskid, lastYearPolicyInfoBean, "", "", model.getAgentid(), "", (agent!=null?agent.getJobnum():"itf"), "0");

                //只有查询成功才保存出险信息
                if("0".equals(lastYearPolicyInfoBean.getStatus()) && isLastProviderMatch){
                    //保存出险信息
                    saveLastYearClaimsInfo(taskid, (agent!=null?agent.getJobnum():"itf"), lastYearPolicyInfoBean);
                }

                //根据上年投保公司查询供应商
                if (!isLastProviderMatch && StringUtil.isNotEmpty(lastprovider)) {
                    SearchProviderModel searchProviderModel = new SearchProviderModel();
                    searchProviderModel.setAgentid(model.getAgentid());
                    searchProviderModel.setPrvid(lastprovider);
                    CommonModel resultModel = fastRenewProviders(searchProviderModel);

                    if ("success".equals(resultModel.getStatus())) {
                        map.put("providerListBeans", resultModel.getBody());
                    }
                }

                commonModel.setStatus("success");
                commonModel.setMessage("查询成功");

            }else{
                commonModel.setStatus("success");
                commonModel.setMessage("快速续保平台查询返回数据不完整");
            }

            map.put("isLastProviderMatch", isLastProviderMatch);
            map.put("lastInsuredComcode", lastprovider);
            map.put("lastInsuredCom", lastprovidername);

            commonModel.setBody(map);

        } catch (Exception e) {
            e.printStackTrace();
            commonModel.setStatus("fail");
            commonModel.setMessage("操作失败");
        }
        return commonModel;
	}

	@Override
	public CommonModel pay(String taskid) {
		CommonModel commonModel = new CommonModel();
		//权限包
		Map<String,Object> permissionMap = appPermissionService.checkPermission("", taskid, "pay");
		if ((int)permissionMap.get("status") == 2 || (int)permissionMap.get("status") == -1) {
			commonModel.setStatus(CommonModel.STATUS_FAIL);
			commonModel.setMessage((String) permissionMap.get("message"));
			return commonModel;
		} else {
			commonModel.setStatus(CommonModel.STATUS_SUCCESS);
			commonModel.setMessage("");
			return commonModel;
		}
	}

	@Override
	public CommonModel renewaSubmit(RenewaSubmitModel model) {
		ExtendCommonModel commonModel = new ExtendCommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if (StringUtil.isEmpty(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			//权限包
			Map<String,Object> permissionMap = appPermissionService.checkPermission("", taskid, "underwriting");
			if ((int)permissionMap.get("status") == 2 || (int)permissionMap.get("status") == -1) {
				commonModel.setStatus(CommonModel.STATUS_FAIL);
				commonModel.setMessage((String) permissionMap.get("message"));
				return commonModel;
			}
			if ((int)permissionMap.get("status") == 1) {
				//commonModel.setExtend(permissionMap);
			}

			//权限包
			permissionMap = appPermissionService.checkPermission("", taskid, "renewal");
			if ((int)permissionMap.get("status") == 2 || (int)permissionMap.get("status") == -1) {
				commonModel.setStatus(CommonModel.STATUS_FAIL);
				commonModel.setMessage((String) permissionMap.get("message"));
				return commonModel;
			}
			if ((int)permissionMap.get("status") == 1) {
				//commonModel.setExtend(permissionMap);
			}

			if ("1".equals(model.getDriverway())) {
				INSBCarinfo insbCarinfo=insbCarinfoDao.selectCarinfoByTaskId(taskid);
				insbCarinfo.setDrivingarea(model.getDrivingRegion());
				insbCarinfoDao.updateById(insbCarinfo);
			}
			if ("1".equals(model.getDriversflag())) {
				List<CarDriver> carDrivers=model.getDrivers();
				for (CarDriver carDriver : carDrivers) {
					INSBCarinfo insbCarinfo1=insbCarinfoDao.selectCarinfoByTaskId(taskid);
					//将制定驾驶员添加到人员关联表中
					INSBPerson insbPersonDriver= new INSBPerson();
					insbPersonDriver.setOperator(insbCarinfo1.getOperator());
					insbPersonDriver.setCreatetime(new Date());
					insbPersonDriver.setTaskid(taskid);
					insbPersonDriver.setName(carDriver.getName());
					insbPersonDriver.setBirthday(ModelUtil.conbertStringToDate(carDriver.getBirthday()));
					// 0-女,1-男
					insbPersonDriver.setGender(Integer.valueOf(carDriver.getGender()));
					insbPersonDao.insert(insbPersonDriver);
					//保存指定驾驶员到数据库
					INSBSpecifydriver insbSpecifydriver=new INSBSpecifydriver();
					insbSpecifydriver.setCarinfoid(insbCarinfo1.getId());
					insbSpecifydriver.setTaskid(taskid);
					insbSpecifydriver.setCreatetime(new Date());
					insbSpecifydriver.setPersonid(insbPersonDriver.getId());
					insbSpecifydriver.setOperator(insbCarinfo1.getOperator());
					insbSpecifydriverDao.insert(insbSpecifydriver);
				}
			}

            //保存数据到his表
            createHisTableInit(taskid);

			//投保提交调用工作流接口，传递报价公司列表，返回报价公司id，以及对应的报价流程id
			//调用工作流接口
			INSBWorkflowmain mainModel = insbWorkflowmainDao.selectByInstanceId(taskid);
			//根据实例id获取选择的报价信息总表id
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			if(null == quotetotalinfo){
				commonModel.setStatus("fail");
				commonModel.setMessage("报价总表信息不存在");
				return commonModel;
			}
			//获取报价信息表，报价公司id列表
			INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
			insbQuoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
			List<INSBQuoteinfo> insbQuoteinfos = insbQuoteinfoDao.selectList(insbQuoteinfo);
			if(null == insbQuoteinfos || insbQuoteinfos.size() <= 0){
				commonModel.setStatus("fail");
				commonModel.setMessage("报价信息不存在");
				return commonModel;
			}
			Map<String, Object> param = new HashMap<String, Object>();
			List<String> ids = new ArrayList<String>();
			for(INSBQuoteinfo quoteinfo : insbQuoteinfos){
				if (StringUtil.isEmpty(quoteinfo.getWorkflowinstanceid())) {
					ids.add(quoteinfo.getInscomcode());
					//调用规则判断承保限制条件
					param = getPriceParamWayStartSubflow(param, taskid, quoteinfo.getInscomcode());
					//续保获取续保途径，获取核保途径
					String result = insbWorkflowmainService.getContracthbType(taskid, quoteinfo.getInscomcode(), "", "renewal");//续保只提一家供应商
					param.put("underwriteway", JSONObject.fromObject(result).optString("quotecode"));//快速续保，设置续保途径//续保只提一家供应商
				}
			}

			if (!ids.isEmpty()) {
				String result = WorkFlowUtil.noticeWorkflowStartQuote(mainModel.getOperator(), taskid, param, "1");
				if (StringUtil.isEmpty(result)) {
					commonModel.setStatus("fail");
					commonModel.setMessage("保存失败");
					return commonModel;
				}

				//向报价信息表中插入工作流对应的报价id
				//List<INSBQuoteinfo> quoteinfos = new ArrayList<INSBQuoteinfo>();
				JSONObject jsonObj = JSONObject.fromObject(result);
				for (INSBQuoteinfo quoteinfo : insbQuoteinfos) {
					if (StringUtil.isEmpty(jsonObj.get(quoteinfo.getInscomcode()))) continue;

					quoteinfo.setWorkflowinstanceid(jsonObj.get(quoteinfo.getInscomcode()).toString());
					//quoteinfos.add(quoteinfo);
					insbQuoteinfoDao.updateById(quoteinfo);
				}

				// 得到子流程id，开启多线程推流程
				LogUtil.info("快速续保获得子流程开启线程推流程taskid=" + taskid);
				for (INSBQuoteinfo quoteinfo : insbQuoteinfos) {
					LogUtil.info("快速续保获得子流程开启线程推流程taskid=" + taskid + ",当前供应商=" + quoteinfo.getInscomcode());
					taskthreadPool4workflow.execute(new Runnable() {
						@Override
						public void run() {
							Map<String,Object> param = new HashMap<String, Object>();
							param = getPriceParamWay(param, taskid, quoteinfo.getInscomcode(), "0");
							String callback = WorkFlowUtil.updateInsuredInfoNoticeWorkflow(jsonObj.get(quoteinfo.getInscomcode()).toString(), "admin", "子流程前置", param);
							LogUtil.info("快速续保子流程前置主流程=" + taskid + ",开启子流程=" + jsonObj.get(quoteinfo.getInscomcode()).toString() + ",当前供应商=" + quoteinfo.getInscomcode() + ",返回结果=" + callback);
						}
					});
				}
			}

            String insureflag = selectInsureProvid(taskid, ids.get(0));
            LogUtil.info("====fast insured==== " + taskid + " == " + insureflag);

			//保存投保书失效
			quotetotalinfo.setInsurancebooks("0");
			insbQuotetotalinfoDao.updateById(quotetotalinfo);

			commonModel.setStatus("success");
			commonModel.setMessage("操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

    // 特别处理的险别，保额为车价信息
    private static final List<String> special = new ArrayList<String>();
    static {
        special.add("VehicleDemageIns");
        special.add("TheftIns");
        special.add("CombustionIns");
    }

	/**
	 * 保存保险配置信息
	 * @param taskid 实例id
	 * @param operator 操作人
	 * @param businessRisks 商业险
	 * @param strongRisks 交强险
	 * @param providers 供应商id
	 */
	private void saveInsuredConfig(String taskid,String operator,List<BusinessRisks> businessRisks,List<StrongRisk> strongRisks,List<String> providers,String remarks,String plankey,String systartdate,String jqstartdate){
		//险别保险配置，查询是否已经存在，存在则删除
		INSBCarconfig carconfig = new INSBCarconfig();
		carconfig.setTaskid(taskid);
		List<INSBCarconfig> carconfigs = insbCarconfigDao.selectList(carconfig);
		if(null != carconfigs && carconfigs.size() > 0){
			LogUtil.info("保存保险配置删除已有的险别总表"+taskid+"操作人="+operator);
			for(INSBCarconfig insbCarconfig : carconfigs){
				insbCarconfigDao.deleteById(insbCarconfig.getId());
			}
		}
		INSBCarkindprice insureCarkindprice = new INSBCarkindprice();
		insureCarkindprice.setTaskid(taskid);
		List<INSBCarkindprice> carkindpricesList = insbCarkindpriceDao.selectList(insureCarkindprice);
		if(null != carkindpricesList && carkindpricesList.size() > 0){
			LogUtil.info("保存保险配置删除已有的险别分表"+taskid+"操作人="+operator);
			for(INSBCarkindprice insbCarkindprice : carkindpricesList){
				insbCarkindpriceDao.deleteById(insbCarkindprice.getId());
			}
		}

		// 保险险别选择表插入基本配置
		List<INSBCarconfig> insbCarconfigs = new ArrayList<INSBCarconfig>();
		List<INSBCarkindprice> insbCarkindprices = new ArrayList<INSBCarkindprice>();

		// 商业险
		if (null != businessRisks && businessRisks.size() > 0) {

			for (BusinessRisks businessRisk : businessRisks) {
				INSBCarconfig insbCarconfig = new INSBCarconfig();
				INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
				insbCarconfig.setOperator(operator);
				insbCarconfig.setCreatetime(new Date());
				insbCarconfig.setTaskid(taskid);
				// 商业险
				insbCarconfig.setInskindtype("0");
				insbCarkindprice.setInskindtype("0");
				// 险别不计免赔 0选中 1 没选中
				if ("0".equals(businessRisk.getFlag())) {
					insbCarconfig.setNotdeductible("1");
					insbCarkindprice.setNotdeductible("1");
					insbCarkindprice.setBjmpCode("Ncf"+businessRisk.getCode());

					/**
					 * hxx 保存附加险
					 */
					INSBCarkindprice ncfInsbCarkindprice  = new INSBCarkindprice();
					ncfInsbCarkindprice.setAmount(0.0);
					ncfInsbCarkindprice.setInskindcode("Ncf"+businessRisk.getCode());
					ncfInsbCarkindprice.setInskindtype("1");//附加险
					ncfInsbCarkindprice.setOperator(operator);
					ncfInsbCarkindprice.setCreatetime(new Date());
					ncfInsbCarkindprice.setTaskid(taskid);
					ncfInsbCarkindprice.setPreriskkind(businessRisk.getCode());
					ncfInsbCarkindprice.setNotdeductible("0");

					//保险险别选择表 插入不计免赔
					INSBCarconfig ncfInsbCarconfig = new INSBCarconfig();
					ncfInsbCarconfig.setAmount("0.0");
					ncfInsbCarconfig.setInskindcode("Ncf"+businessRisk.getCode());
					ncfInsbCarconfig.setInskindtype("1");//附加险
					ncfInsbCarconfig.setOperator(operator);
					ncfInsbCarconfig.setCreatetime(new Date());
					ncfInsbCarconfig.setTaskid(taskid);
					ncfInsbCarconfig.setPreriskkind(businessRisk.getCode());
					ncfInsbCarconfig.setNotdeductible("0");
					ncfInsbCarconfig.setPlankey(plankey);

					INSBRiskkindconfig vo = new INSBRiskkindconfig();
					vo.setRiskkindcode("Ncf"+businessRisk.getCode());
					INSBRiskkindconfig nfcRIkInsbRiskkindconfig=insbRiskkindconfigDao.selectOne(vo);
					if(nfcRIkInsbRiskkindconfig!=null){
						ncfInsbCarkindprice.setRiskname(nfcRIkInsbRiskkindconfig.getRiskkindname());
						insbCarkindprices.add(ncfInsbCarkindprice);
						insbCarconfigs.add(ncfInsbCarconfig);
					}

				} else {
					insbCarconfig.setNotdeductible("0");
					insbCarkindprice.setNotdeductible("0");
				}
				insbCarconfig.setInskindcode(businessRisk.getCode());
				// 保额
				if(!StringUtil.isEmpty(businessRisk.getCoverage())&&ModelUtil.isNumeric(businessRisk.getCoverage())){
					insbCarconfig.setAmount(businessRisk.getCoverage());
					insbCarkindprice.setAmount(Double.parseDouble(businessRisk.getCoverage()));
				}else{
					insbCarconfig.setAmount("0");
					insbCarkindprice.setAmount(0d);
				}
				//单独处理  车辆损失险
				if(special.contains(businessRisk.getCode())){
					INSBCarinfo insbCarinfo = new INSBCarinfo();
					insbCarinfo.setTaskid(taskid);
					INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
					if(null != carinfo){
						INSBCarmodelinfo insbCarmodelinfo = new INSBCarmodelinfo();
						insbCarmodelinfo.setCarinfoid(carinfo.getId());
						INSBCarmodelinfo carmodelinfo = insbCarmodelinfoDao.selectOne(insbCarmodelinfo);
						if(null != carmodelinfo){
							//20160126 如果自定义改为自定义车价
							if("2".equals(carmodelinfo.getCarprice())){
								insbCarconfig.setAmount(carmodelinfo.getPolicycarprice().toString());
								insbCarkindprice.setAmount(carmodelinfo.getPolicycarprice());
							}else{
								insbCarconfig.setAmount(carmodelinfo.getPrice().toString());
								insbCarkindprice.setAmount(carmodelinfo.getPrice());
							}
						}
					}
				}
				// 保存险别要素已选项
				List<SelectOption> list = new ArrayList<SelectOption>();
				SelectOption selectOption = new SelectOption();
				RisksData risksData = new RisksData();
				risksData.setKEY(businessRisk.getSelectedOption());
				risksData.setVALUE(businessRisk.getCoverage());
				risksData.setUNIT(businessRisk.getUnit());
				// 是否是玻璃   01不是  02是
				selectOption.setTYPE(businessRisk.getType());
				selectOption.setVALUE(risksData);
				list.add(selectOption);
				JSONArray jsonObject = JSONArray.fromObject(list);
				insbCarconfig.setSelecteditem(jsonObject.toString());
				insbCarkindprice.setSelecteditem(jsonObject.toString());
				//查询基础表
				INSBRiskkindconfig rkc = new INSBRiskkindconfig();
				rkc.setRiskkindcode(businessRisk.getCode());
				INSBRiskkindconfig vo = insbRiskkindconfigDao.selectOne(rkc);
				if(null != vo){
					insbCarconfig.setPreriskkind(vo.getPrekindcode());
					insbCarkindprice.setPreriskkind(vo.getPrekindcode());
				}
				insbCarconfig.setPlankey(plankey);
				insbCarconfigs.add(insbCarconfig);

				insbCarkindprice.setOperator(operator);
				insbCarkindprice.setCreatetime(new Date());
				insbCarkindprice.setTaskid(taskid);
				insbCarkindprice.setInskindcode(businessRisk.getCode());
				insbCarkindprice.setRiskname(businessRisk.getName());
				insbCarkindprices.add(insbCarkindprice);
			}
			//更新保单表 商业险 0
			saveOrUpdatePolocy(taskid,providers,"0",systartdate,jqstartdate);
		}else{
			//没买商业险，删除已有的
			deletePolocy(taskid,providers,"0");
		}
		// 交强险
		if (null != strongRisks && strongRisks.size() > 0) {
			for (StrongRisk strongRisk : strongRisks) {
				INSBRiskkindconfig vo = new INSBRiskkindconfig();
				vo.setRiskkindcode(strongRisk.getCode());
				INSBRiskkindconfig inscRiskkindconfig=insbRiskkindconfigDao.selectOne(vo);
				if(inscRiskkindconfig==null){
					continue;
				}
				INSBCarconfig insbCarconfig = new INSBCarconfig();
				INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
				insbCarconfig.setOperator(operator);
				insbCarconfig.setCreatetime(new Date());
				insbCarconfig.setTaskid(taskid);

				// 交强险
				insbCarconfig.setInskindtype(inscRiskkindconfig.getType());
				insbCarkindprice.setInskindtype(inscRiskkindconfig.getType());
				// 险别不计免赔 没选中
				insbCarconfig.setNotdeductible("0");
				insbCarkindprice.setNotdeductible("0");
				insbCarconfig.setInskindcode(strongRisk.getCode());
				// 保额
				insbCarconfig.setAmount("0");
				insbCarkindprice.setAmount(0d);
				// 险别要素为空
				insbCarconfig.setSelecteditem(strongRisk.getSelected());
				insbCarkindprice.setSelecteditem(strongRisk.getSelected());
				insbCarconfig.setPreriskkind(inscRiskkindconfig.getPrekindcode());
				insbCarconfig.setPlankey(plankey);
				insbCarconfigs.add(insbCarconfig);

				insbCarkindprice.setOperator(operator);
				insbCarkindprice.setCreatetime(new Date());
				insbCarkindprice.setTaskid(taskid);
				insbCarkindprice.setInskindcode(strongRisk.getCode());
				insbCarkindprice.setPreriskkind(inscRiskkindconfig.getPrekindcode());
				insbCarkindprice.setRiskname(strongRisk.getName());
				insbCarkindprices.add(insbCarkindprice);
				//有交强险的时候存保单表
				if("VehicleCompulsoryIns".equals(strongRisk.getCode().trim())){
					//更新保单表交强险 1
					saveOrUpdatePolocy(taskid,providers,"1",systartdate,jqstartdate);
				}
			}

		}else{
			//没买交强险，删除已有的
			deletePolocy(taskid, providers, "1");
		}
		// 保险险别选择表插入基本配置
		insbCarconfigDao.insertInBatch(insbCarconfigs);

		List<INSBCarkindprice> carkindprices = new ArrayList<INSBCarkindprice>();
		if (null != providers && providers.size() > 0) {
			for (String proid : providers) {
				for (INSBCarkindprice carkindprice : insbCarkindprices) {
					INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
					insbCarkindprice.setOperator(carkindprice.getOperator());
					insbCarkindprice
							.setCreatetime(carkindprice.getCreatetime());
					insbCarkindprice.setTaskid(carkindprice.getTaskid());
					insbCarkindprice.setInskindcode(carkindprice
							.getInskindcode());
					insbCarkindprice.setAmount(carkindprice.getAmount());
					insbCarkindprice.setSelecteditem(carkindprice
							.getSelecteditem());
					insbCarkindprice.setInskindtype(carkindprice
							.getInskindtype());
					insbCarkindprice.setNotdeductible(carkindprice
							.getNotdeductible());
					insbCarkindprice.setInscomcode(proid);
					insbCarkindprice.setBjmpCode(carkindprice.getBjmpCode());
					//保存plankey
					insbCarkindprice.setPlankey(plankey);
					insbCarkindprice.setPreriskkind(carkindprice.getPreriskkind());
					insbCarkindprice.setRiskname(carkindprice.getRiskname());
					carkindprices.add(insbCarkindprice);
				}
			}
		}
		// 保险公司险别报价表
		insbCarkindpriceDao.insertInBatch(carkindprices);
	}
	/**
	 * 删除保单表数据
	 * @param taskid
	 * @param providers
	 * @param type
	 */
	private void deletePolocy(String taskid, List<String> providers,
			String type) {
		if(null != providers && providers.size() > 0){
			for(String pid : providers){
				deleteOnePolicy(taskid, pid, type);
			}
		}
	}

	private void deleteOnePolicy(String taskid, String pid,String type){
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskid);
		insbPolicyitem.setRisktype(type);
		insbPolicyitem.setInscomcode(pid);
		INSBPolicyitem policyitem = insbPolicyitemDao.selectOne(insbPolicyitem);
		if(null != policyitem){
			LogUtil.info("保单表删数据"+taskid+" deleteOnePolicy 删除的保单表id="+policyitem.getId()+"商业险交强险标识risktype="+policyitem.getRisktype());
			insbPolicyitemDao.deleteById(policyitem.getId());
		}
	}

	/**
	 * 更新保单表数据，每个报价公司一条  20160112
	 * @param taskid
	 * @param type 0 商业险 1交强险
	 */
	private void saveOrUpdatePolocy(String taskid,List<String> provids,String type,String sysdate,String jqsdate){
		//查询保单信息(只查商业险)
		INSBPolicyitem allpolicys = new INSBPolicyitem();
		allpolicys.setTaskid(taskid);
		List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(allpolicys);
		INSBPolicyitem insbPolicyitem = insbPolicyitems.get(0);//取出一个拿数据
		//删除所有的保单信息，重新插入数据
		deleteAllPolocys(taskid,type);
//		Map<String, Date> map = null;
//		if(StringUtil.isEmpty(sysdate) || StringUtil.isEmpty(jqsdate)){
//			//获取上年投保单时间
//			map = selectLastPolicyDate(taskid);
//		}else{
//			//投保区间根据保险配置页面录入，存入保单表
//			map = selectPolicyDate(sysdate,jqsdate);
//		}
		Date systartdate = null;
		Date syenddate = null;
		Date jqstartdate = null;
		Date jqenddate = null;
		if(!StringUtil.isEmpty(sysdate)){
			systartdate = ModelUtil.conbertStringToNyrDate(sysdate);// 开始时间
			syenddate = ModelUtil.nowDateMinusOneDay(ModelUtil.nowDateAddOneYear(ModelUtil.conbertStringToNyrDate(sysdate), 1));// 开始时间   加一年减一天
		}
		if(!StringUtil.isEmpty(jqsdate)){
			jqstartdate = ModelUtil.conbertStringToNyrDate(jqsdate);// 开始时间
			jqenddate = ModelUtil.nowDateMinusOneDay(ModelUtil.nowDateAddOneYear(ModelUtil.conbertStringToNyrDate(jqsdate), 1));// 开始时间   加一年减一天
		}
		if(null != provids && provids.size() > 0){
			for(String pid : provids){
				INSBPolicyitem policyitem = new INSBPolicyitem();
				policyitem.setOperator(insbPolicyitem.getAgentname());
				policyitem.setCreatetime(new Date());
				policyitem.setTaskid(taskid);
				policyitem.setCarownerid(insbPolicyitem.getCarownerid());
				policyitem.setCarownername(insbPolicyitem.getCarownername());
				policyitem.setAgentname(insbPolicyitem.getAgentname());
				policyitem.setCarinfoid(insbPolicyitem.getCarinfoid());
				//险种类型
				policyitem.setRisktype(type);
				policyitem.setAgentnum(insbPolicyitem.getAgentnum());
				//policyitem.setNoti(remarks);
				//投时间
				if("0".equals(type)){
					policyitem.setStartdate(systartdate);
					policyitem.setEnddate(syenddate);
				}else{
					policyitem.setStartdate(jqstartdate);
					policyitem.setEnddate(jqenddate);
				}

				policyitem.setApplicantid(insbPolicyitem.getApplicantid());
				policyitem.setApplicantname(insbPolicyitem.getApplicantname());
				policyitem.setInsuredid(insbPolicyitem.getInsuredid());
				policyitem.setInsuredname(insbPolicyitem.getInsuredname());
				//未生效
				policyitem.setPolicystatus("5");
				//关联报价公司
				policyitem.setInscomcode(pid);
				insbPolicyitemDao.insert(policyitem);
			}
		}

		//删除没有供应商id的记录
		INSBPolicyitem delpolicyitem = new INSBPolicyitem();
		delpolicyitem.setTaskid(taskid);
		delpolicyitem.setRisktype("0");
		List<INSBPolicyitem> policyitems = insbPolicyitemDao.selectList(delpolicyitem);
		for(INSBPolicyitem policyitem : policyitems){
			if(StringUtil.isEmpty(policyitem.getInscomcode())){
				LogUtil.info("保单表删数据"+taskid+" saveOrUpdatePolocy 删除的保单表id="+policyitem.getId()+"商业险交强险标识risktype="+policyitem.getRisktype());
				insbPolicyitemDao.deleteById(policyitem.getId());
			}
		}
	}

	private void deleteAllPolocys(String taskid,String type) {
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskid);
		insbPolicyitem.setRisktype(type);
		List<INSBPolicyitem> policyitems = insbPolicyitemDao.selectList(insbPolicyitem);
		if(null != policyitems && policyitems.size() > 0){
			for(INSBPolicyitem policyitem : policyitems){
				LogUtil.info("保单表删数据"+taskid+" deleteAllPolocys 删除的保单表id="+policyitem.getId()+"商业险交强险标识risktype="+policyitem.getRisktype());
				insbPolicyitemDao.deleteById(policyitem.getId());
			}
		}

	}

	private Map<String, Date> getPolicyDate(
			LastYearPolicyInfoBean lastYearPolicyInfoBean) {
		Map<String, Date> map = new HashMap<String, Date>();
		// 平台返回的有投保时间
		if (null != lastYearPolicyInfoBean
				&& "0".equals(lastYearPolicyInfoBean.getStatus())
				&& null != lastYearPolicyInfoBean.getLastYearPolicyBean()) {
			LastYearPolicyBean policy = lastYearPolicyInfoBean
					.getLastYearPolicyBean();
			if (!StringUtil.isEmpty(policy.getStartdate())
					&& !StringUtil.isEmpty(policy.getEnddate())) {
				// 商业险 保单已经过期了
				if (ModelUtil.compareDate(
						ModelUtil.conbertStringToNyrDate(policy.getEnddate()),
						new Date()) <= 0) {
					map.put("systartdate",
							ModelUtil.nowDateAddOneDay(new Date()));// 开始时间
					map.put("syenddate",
							ModelUtil.nowDateAddOneYear(new Date(), 1));// 结束时间
				} else {
					// 判断是否大于一年 364天
					if (ModelUtil.getBetweenDay(policy.getStartdate(),
							policy.getEnddate()) > 364) {
						map.put("systartdate", ModelUtil.nowDateAddOneYear(
								ModelUtil.conbertStringToNyrDate(policy
										.getStartdate()), 1));// 开始时间
						map.put("syenddate", ModelUtil
								.nowDateMinusOneDay(ModelUtil
                                        .nowDateAddOneYear(ModelUtil
                                                .conbertStringToNyrDate(policy
                                                        .getStartdate()), 2)));// 结束时间
																				// 加一年减一天
					} else {
						map.put("systartdate", ModelUtil.nowDateAddOneYear(
								ModelUtil.conbertStringToNyrDate(policy
										.getStartdate()), 1));// 开始时间
						map.put("syenddate", ModelUtil.nowDateAddOneYear(
								ModelUtil.conbertStringToNyrDate(policy
										.getEnddate()), 1));// 结束时间
					}
				}
			} else {
				// 时间有一个为空时，默认当前时间
				map.put("systartdate", ModelUtil.nowDateAddOneDay(new Date()));// 开始时间
				map.put("syenddate", ModelUtil.nowDateAddOneYear(new Date(), 1));// 结束时间   加一年减一天
			}
			// 交强险 保单已经过期了
			if (!StringUtil.isEmpty(policy.getJqstartdate())
					&& !StringUtil.isEmpty(policy.getJqenddate())) {
				// 保单已经过期了
				if (ModelUtil
						.compareDate(ModelUtil.conbertStringToNyrDate(policy
                                .getJqenddate()), new Date()) <= 0) {
					map.put("jqstartdate",
							ModelUtil.nowDateAddOneDay(new Date()));// 开始时间
					map.put("jqenddate",
							ModelUtil.nowDateAddOneYear(new Date(), 1));// 结束时间
				} else {
					// 判断是否大于一年 364天
					if (ModelUtil.getBetweenDay(policy.getJqstartdate(),
							policy.getJqenddate()) > 364) {
						map.put("jqstartdate", ModelUtil.nowDateAddOneYear(
								ModelUtil.conbertStringToNyrDate(policy
										.getJqstartdate()), 1));// 开始时间
						map.put("jqenddate", ModelUtil
								.nowDateMinusOneDay(ModelUtil
                                        .nowDateAddOneYear(ModelUtil
                                                .conbertStringToNyrDate(policy
                                                        .getJqstartdate()), 2)));// 结束时间
																					// 加一年减一天
					} else {
						map.put("jqstartdate", ModelUtil.nowDateAddOneYear(
								ModelUtil.conbertStringToNyrDate(policy
										.getJqstartdate()), 1));// 开始时间
						map.put("jqenddate", ModelUtil.nowDateAddOneYear(
								ModelUtil.conbertStringToNyrDate(policy
										.getJqenddate()), 1));// 结束时间
					}
				}
			} else {
				// 时间有一个为空时，默认当前时间
				map.put("jqstartdate", ModelUtil.nowDateAddOneDay(new Date()));// 开始时间
				map.put("jqenddate", ModelUtil.nowDateAddOneYear(new Date(), 1));// 结束时间   加一年减一天
			}
		} else {
			map.put("systartdate", ModelUtil.nowDateAddOneDay(new Date()));// 开始时间
			map.put("syenddate", ModelUtil.nowDateAddOneYear(new Date(), 1));// 结束时间   加一年减一天
			map.put("jqstartdate", ModelUtil.nowDateAddOneDay(new Date()));// 开始时间
			map.put("jqenddate", ModelUtil.nowDateAddOneYear(new Date(), 1));// 结束时间   加一年减一天
		}
		return map;
	}

	/**
	 * 平台查询返回某一模块成功或者失败
	 * @param lastYearPolicyInfoBean
	 * @return
	 */
	private Map<String, Object> noticeResult(LastYearPolicyInfoBean lastYearPolicyInfoBean,String province,String city,String agentid,String channeltype,String taskid){
		Map<String, Object> result = new HashMap<String, Object>();
		//车主信息
		LastYearPersonBean carowner = lastYearPolicyInfoBean.getCarowner();
		//保单信息
		LastYearPolicyBean policy = lastYearPolicyInfoBean.getLastYearPolicyBean();
		//车辆信息
		LastYearCarinfoBean lastYearCarinfoBean = lastYearPolicyInfoBean.getLastYearCarinfoBean();
		//保险公司名称
		LastYearSupplierBean supplier = lastYearPolicyInfoBean.getLastYearSupplierBean();
		//车型信息
		JSONArray carModels = lastYearPolicyInfoBean.getCarModels();
		int carModelSize = carModels.size();
		String insurederid = "";
		String properid = "";
		if(null == carowner){
			result.put("insureinfo", false);
		}else{
			InsuredInfoModel model = new InsuredInfoModel();
			CarowerinfoBean carowerBean = new CarowerinfoBean();//车主
			PassiveInsurePersionBean passiveBean = new PassiveInsurePersionBean();//被保人
			BusinessInsureddateBean busBean = new BusinessInsureddateBean();//商业险
			TrafficInsureddateBean trafficBean = new TrafficInsureddateBean();//交强险
			InsurePersionBean insureBean = new InsurePersionBean();//投保人
			if(null !=  policy){
				//平台被保人
				LastYearPersonBean bbpersonBean = policy.getInsureder();
				//平台投保人
				LastYearPersonBean tbpersonBean = policy.getProper();
				if(bbpersonBean!=null&&tbpersonBean!=null){
					insurederid = bbpersonBean.getIdno();//被保人身份证
					properid = tbpersonBean.getIdno();//投保人身份证
				}
				//平台车主
			    carowerBean.setName(carowner.getName());
			    carowerBean.setCertificateType(getPeopleIdcardType(carowner.getIdtype()));//证件类型
			    carowerBean.setCertNumber(carowner.getIdno());//证件号
			    carowerBean.setFlag(!StringUtil.isEmpty(insurederid) && insurederid.equals(carowner.getIdno())?"0":"1");//与被保人一致

			    passiveBean.setFlag(!StringUtil.isEmpty(insurederid) && insurederid.equals(properid)?"2":"1");//1被保人 0投保人 2与投保人一致
			    if(null == bbpersonBean || bbpersonBean.getName().isEmpty()){
			    	passiveBean.setName(carowerBean.getName());//姓名
					passiveBean.setCertificateType(carowerBean.getCertificateType());//证件类型
					passiveBean.setCertNumber(carowerBean.getCertNumber());
			    }else{
			    	 //保证车主与被保人信息一致
			    	 if(StringUtil.isEmpty(carowner.getName()) && !StringUtil.isEmpty(bbpersonBean.getName())){
			    		 carowerBean.setName(bbpersonBean.getName());
			    	 }
			    	 if(StringUtil.isEmpty(carowner.getIdtype()) && !StringUtil.isEmpty(bbpersonBean.getIdtype())){
			    		 carowerBean.setCertificateType(getPeopleIdcardType(bbpersonBean.getIdtype()));
			    	 }
			    	 if(StringUtil.isEmpty(carowner.getIdno()) && !StringUtil.isEmpty(bbpersonBean.getIdno())){
			    		 carowerBean.setCertNumber(bbpersonBean.getIdno());
			    	 }
			    	passiveBean.setName(bbpersonBean.getName());//姓名
					passiveBean.setCertificateType(getPeopleIdcardType(bbpersonBean.getIdtype()));//证件类型
					passiveBean.setCertNumber(insurederid);//证件号码
			    }

			    insureBean.setFlag(!StringUtil.isEmpty(insurederid) && insurederid.equals(properid)?"2":"0");//1被保人 0投保人 2与被保人一致

			    if(null == tbpersonBean || tbpersonBean.getName().isEmpty()){
			    	insureBean.setName(carowerBean.getName());//姓名
			    	insureBean.setCertificateType(carowerBean.getCertificateType());//证件类型
			    	insureBean.setCertNumber(carowerBean.getCertNumber());//证件号码
			    }else{
			    	insureBean.setName(tbpersonBean.getName());//姓名
			    	insureBean.setCertificateType(getPeopleIdcardType(tbpersonBean.getIdtype()));//证件类型
			    	insureBean.setCertNumber(properid);//证件号码
			    }

			    //商业险交强险时间
			    Map<String, Date> map = getPolicyDate(lastYearPolicyInfoBean);
				Date systartdate = null;
				Date syenddate = null;
				Date jqstartdate = null;
				Date jqenddate = null;
				if(!map.isEmpty()){
					systartdate = map.get("systartdate");
					syenddate = map.get("syenddate");
					jqstartdate = map.get("jqstartdate");
					jqenddate = map.get("jqenddate");
				}
				busBean.setRiskstartdate(ModelUtil.conbertToString(systartdate));//开始时间
	    		busBean.setRiskenddate(ModelUtil.conbertToString(syenddate));//结束时间
	    		trafficBean.setRiskstartdate(ModelUtil.conbertToString(jqstartdate));//开始时间
	    		trafficBean.setRiskenddate(ModelUtil.conbertToString(jqenddate));//结束时间
			}
		    busBean.setType("0");// 0 商业险 1 交强险
		    trafficBean.setType("1");// 0 商业险 1 交强险
		    model.setPassiveInsurePersion(passiveBean);//被保人
			model.setInsurePersion(insureBean);//投保人
			model.setCarowerinfo(carowerBean);//车主
			model.setBusinessInsureddate(busBean);//商业险
			model.setTrafficInsureddate(trafficBean);//交强险
			model.setSelectdrivers("1");//是否指定驾驶员 0是，1否
			model.setEmail(carowner.getEmail());//邮箱
			model.setTel(carowner.getMobile());//联系电话
			model.setLastComId(supplier!=null?supplier.getSupplierid():"");//上家商业承保公司ID
			model.setDrivingRegion(getCarDrivingArea(lastYearCarinfoBean== null? "" :lastYearCarinfoBean.getArea()));//行驶区域
			result.put("insureinfo", true);
			result.put("insureinfobean", model);
		}
		//车辆信息
		//车辆类型  是这两种的时候，默认家庭自用
//		List<String> vehicletype = new ArrayList<String>();
//		vehicletype.add("KA");
//		vehicletype.add("KB");
//		String vehicle = "";
		if(null == lastYearCarinfoBean){
			result.put("carinfo", false);
		}else{
			SaveCarInfoModel model = new SaveCarInfoModel();
			if(carModelSize == 1){
				JSONObject car = JSONObject.fromObject(carModels.get(0));
//				vehicle = car.get("syvehicletype").toString();
				model.setProcessinstanceid("");
				model.setModelCode(car.get("vehiclename").toString());//品牌型号
				model.setVin(ModelUtil.hiddenVin(lastYearCarinfoBean.getVehicleframeno()));//车辆识别代号
				model.setEngineNo(ModelUtil.hiddenEngineNo(lastYearCarinfoBean.getEngineno()));//发动机号
				model.setChgOwnerFlag("");//是否过户车  0不 是  1 是
				model.setChgOwnerDate("");//过户时间
				model.setFirstRegisterDate(lastYearCarinfoBean.getRegisterdate());//车辆初次登录日期
				//俩性质
				model.setInstitutionType(lastYearCarinfoBean.getVehicletype());//所属性质
				if(StringUtil.isEmpty(lastYearCarinfoBean.getUsingnature())){
					model.setUseProperty("");//车辆性质
				}else{
					model.setUseProperty(getUsePropsFrommap(lastYearCarinfoBean.getUsingnature()));//车辆性质
				}
				//平台传递品牌型号
				model.setCifstandardname(lastYearCarinfoBean.getBrandmodel());
				result.put("carinfobean", model);
				//车型名称 vin码 发动机号  所属性质   车辆性质
				if(StringUtil.isEmpty(car.get("vehiclename").toString()) || StringUtil.isEmpty(lastYearCarinfoBean.getVehicleframeno()) ||
						StringUtil.isEmpty(lastYearCarinfoBean.getEngineno())){
					result.put("carinfo", false);
				}else{
					result.put("carinfo", true);
				}
			}else{
//				if(carModelSize > 0){
//					JSONObject car = JSONObject.fromObject(carModels.get(0));
//					vehicle = car.get("syvehicletype").toString();
//				}
				model.setProcessinstanceid("");
				model.setVin(ModelUtil.hiddenVin(lastYearCarinfoBean.getVehicleframeno()));//车辆识别代号
				model.setEngineNo(ModelUtil.hiddenEngineNo(lastYearCarinfoBean.getEngineno()));//发动机号
				model.setChgOwnerFlag("");//是否过户车  0不 是  1 是
				model.setChgOwnerDate("");//过户时间
				model.setFirstRegisterDate(lastYearCarinfoBean.getRegisterdate());//车辆初次登录日期
				//俩性质
				model.setInstitutionType(lastYearCarinfoBean.getVehicletype());//所属性质
				if(StringUtil.isEmpty(lastYearCarinfoBean.getUsingnature())){
					model.setUseProperty("");//车辆性质
				}else{
					model.setUseProperty(getUsePropsFrommap(lastYearCarinfoBean.getUsingnature()));//车辆性质
				}
				//平台传递品牌型号
				model.setCifstandardname(lastYearCarinfoBean.getBrandmodel());
				result.put("carinfobean", model);
				result.put("carinfo", false);
			}
		}
		//车型信息
		//先查数据库，不存在返回false
		INSBCarmodelinfo  carmodelinfo = appInsuredQuoteDao.selectCarmodelInfoByTaskid(taskid);
		if(null == carModels || carModels.size()<1){
			result.put("carmodel", false);
		}else{
			if(carModelSize == 1){
				JSONObject car = JSONObject.fromObject(carModels.get(0));
//				vehicle = car.get("syvehicletype").toString();
				CarModelInfoModel model = new CarModelInfoModel();
				model.setProcessinstanceid("");
				model.setModelCode(car.get("vehiclename").toString());//品牌型号
				model.setDisplacement(convertToDouble(car.get("displacement").toString()));//排量
				model.setApprovedLoad(convertToInteger(car.get("seat").toString()));//核定载人数
				model.setRulePriceProvideType("0");//车价选择，0最低，1最高，2自定义
				String cmvehicletype = "";
				String cmusingnature = "";
				if(null != lastYearCarinfoBean){
					cmvehicletype = lastYearCarinfoBean.getVehicletype();
					cmusingnature = getUsePropsFrommap(lastYearCarinfoBean.getUsingnature());
					model.setInstitutionType(lastYearCarinfoBean.getVehicletype());//所属性质
					if(StringUtil.isEmpty(lastYearCarinfoBean.getUsingnature())){
						model.setUseProperty("");//车辆性质
					}else{
						model.setUseProperty(getUsePropsFrommap(lastYearCarinfoBean.getUsingnature()));//车辆性质
					}
					//model.setUseProperty(getUsePropsFrommap(lastYearCarinfoBean.getUsingnature()));//车辆性质
				}
				model.setTonnage(convertToDouble(car.get("modelLoads").toString()));//核定载质量
				model.setWholeWeight(convertToDouble(car.get("fullweight").toString()));//整备质量
				model.setCustomPrice(0d);//制定车价   车价选择2的时候输入的值
				result.put("carmodelbean", model);
				if("".equals(car.get("vehiclename").toString()) || StringUtil.isEmpty(cmvehicletype) || StringUtil.isEmpty(cmusingnature)){
					result.put("carmodel", false);
				}else{
					if(null == carmodelinfo){
						result.put("carmodel", false);
					}else{
						result.put("carmodel", true);
					}
				}
			}else{
				List<SelectCarModel> carmodelList = new ArrayList<SelectCarModel>();
				SelectCarModel model = null;
				for(int i = 0;i < carModelSize ; i++){
					JSONObject car = JSONObject.fromObject(carModels.get(i));
					model = new SelectCarModel();
					model.setBrandname(car.get("brandname").toString());//品牌型号
					model.setTaxPrice(convertToDouble(car.get("taxprice").toString()));// 新车购置价
					model.setAliasname(""); //* 别名
					model.setAnalogyprice(convertToDouble(car.get("analogyprice").toString()));//* 不含税类比价
					model.setAnalogytaxprice(convertToDouble(car.get("analogytaxprice").toString()));// * 含税类比价
					model.setDisplacement(convertToDouble(car.get("displacement").toString()));//* 排气量
					model.setFamilyname(car.get("familyname").toString());//车型系列名称
					model.setFullweight(convertToDouble(car.get("fullweight").toString()));//整备质量
					model.setGearbox(car.get("gearbox").toString());//变速箱 自动档  手动挡
					model.setLoads(convertToDouble(car.get("modelLoads").toString()));// 载荷
					model.setListedyear(car.get("maketdate").toString());//上市年份
					model.setPrice(convertToDouble(car.get("price").toString()));//不含税价
					model.setSeat(convertToInteger(car.get("seat").toString()));//座位数
					model.setStandardname(car.get("vehiclename").toString());//车型品牌名称
					model.setCarVehicleOrigin(car.get("manufacturer").toString().trim());//车辆产地类型  国产:0,进口:1,合资:2
					model.setJgVehicleType(car.get("vehiclecode").toString());//交管车辆类型
					model.setVehicleid(car.get("vehicleId").toString());//精友车型id
					model.setSyvehicletypename(car.get("syvehicletypename").toString());//商业险车辆名称
					model.setSyvehicletypecode(car.get("syvehicletype").toString());//机动车编码
					carmodelList.add(model);
				}
				result.put("carmodelbean", carmodelList);
				result.put("carmodel", false);
			}
		}
		//保险公司名称
		LastYearSupplierBean riskBean = lastYearPolicyInfoBean.getLastYearSupplierBean();
		if(null == riskBean || StringUtil.isEmpty(riskBean.getSupplierid())){
			result.put("provider", false);
			result.put("insuredconf", false);
		}else{
			//获取供应商出单网点
			String agreeidpidsitid = judjeThisProidInThat(riskBean.getSupplierid(), province, city, agentid, channeltype,taskid);
			INSBProvider provider = insbProviderDao.selectById(riskBean.getSupplierid());
			if(null != provider){
				LastYearSupplierBean providerbean = new LastYearSupplierBean();
				providerbean.setSupplierid(provider.getId());
				providerbean.setSuppliername(provider.getPrvshotname());
				result.put("providerbean", providerbean);
			}
			if(StringUtil.isEmpty(agreeidpidsitid)){
				result.put("provider", false);
				result.put("insuredconf", false);
			}else{
				result.put("provider", true);
				//险种信息
				JSONArray lastYearRiskinfos = lastYearPolicyInfoBean.getLastYearRiskinfos();
				if(null == lastYearRiskinfos || lastYearRiskinfos.size() <= 0){
					result.put("insuredconf", false);
				}else{
					result.put("insuredconf", true);
				}
			}
		}
		return result;
	}

	/**
	 * 20160530 只取回调接口数据，没有起保日期默认第二天
	 * BUG 4138 2016-04-12
	 * 以平台回调插入的时间为准，20160505
	 * 当平台查询只查到一个起保日期时，没查到的险种默认起保日期改成T+1
	 * @param day
     * @param taskid
	 * @return false 重复投保
	 */
	private Map<String, Object> judgeInsuredDate(int day,String taskid) {
		Map<String, Object> mapresult = new HashMap<String, Object>();
		INSBLastyearinsureinfo lastyearinsureinfo = insbRulequerycarinfoDao.queryLastYearClainInfo(taskid);
		if(null != lastyearinsureinfo){
			String lsyenddate = lastyearinsureinfo.getSyenddate();
			String ljqenddate = lastyearinsureinfo.getJqenddate();

			Map<String, Date> dates = commonQuoteinfoService.getNextPolicyDate(taskid);
			Date systartdate = dates.get("commEffectDate"), jqstartdate = dates.get("trafEffectDate");

			LogUtil.info("judgeInsuredDate"+taskid+"平台回写终保日期，商业险="+lsyenddate+"，交强险="+ljqenddate+"。"+
					"计算起保日期，商业险="+systartdate+"，交强险="+jqstartdate);

			mapresult.put("syenddateflag", true);
			mapresult.put("syenddate", systartdate!=null ? ModelUtil.conbertToStringsdf(systartdate) : "");
			mapresult.put("jqenddateflag", true);
			mapresult.put("jqenddate", jqstartdate!=null ? ModelUtil.conbertToStringsdf(jqstartdate) : "");

			//商业险重复投保返回一句话，优先判断
			if(!StringUtil.isEmpty(lastyearinsureinfo.getRepeatinsurance())){
				LogUtil.info(taskid+"商业险重复投保啦,平台回写数据="+lastyearinsureinfo.getRepeatinsurance());
				mapresult.put("syenddateflag", false);
				mapresult.put("syenddate", "");
			}else{
				if(systartdate != null){
					int between = ModelUtil.compareDateWithDay(systartdate, day);
					if(between < 0){
						mapresult.put("syenddateflag", false);
					}
				}else{
					mapresult.put("syenddate", ModelUtil.conbertToString(ModelUtil.nowDateAddOneDay(new Date())));
				}
			}

			//交强重复投保返回一句话，优先判断
			if(!StringUtil.isEmpty(lastyearinsureinfo.getJqrepeatinsurance())){
				LogUtil.info(taskid+"交强险重复投保啦,平台回写数据="+lastyearinsureinfo.getJqrepeatinsurance());
				mapresult.put("jqenddateflag", false);
				mapresult.put("jqenddate", "");
			}else{
				if(jqstartdate != null){
					int between = ModelUtil.compareDateWithDay(jqstartdate, day);
					if(between < 0){
						mapresult.put("jqenddateflag", false);
					}
				}else{
					mapresult.put("jqenddate", ModelUtil.conbertToString(ModelUtil.nowDateAddOneDay(new Date())));
				}
			}
		}else{
			LogUtil.info("judgeInsuredDate"+taskid+"平台数据没有回写，默认第二天");
			mapresult.put("syenddateflag", true);
			mapresult.put("syenddate", ModelUtil.conbertToString(ModelUtil.nowDateAddOneDay(new Date())));
			mapresult.put("jqenddateflag", true);
			mapresult.put("jqenddate", ModelUtil.conbertToString(ModelUtil.nowDateAddOneDay(new Date())));
		}
		return mapresult;
	}

	/**
	 *
	 * @param taskid 任务id
	 * @param lastYearPolicyInfoBean 上年保单信息
	 * @param province 省代码
	 * @param city 市代码
	 * @param agentid 代理人
	 * @param channeltype 渠道
	 * @param operator 操作人
	 * @param flag 0 投保  1 快速续保
	 */
	private void saveLastYearPolicyInfoBean(String taskid,LastYearPolicyInfoBean lastYearPolicyInfoBean,String province,String city,
			String agentid,String channeltype,String operator,String flag){
		operator = null ==operator?"":operator;
//		//保存出险信息
//		saveLastYearClaimsInfo(taskid, operator, lastYearPolicyInfoBean);

		String carid = "";
		INSBCarinfo carinfo ;//车辆信息
		INSBCarmodelinfo carmoderCarmodelinfo;//车型信息
		//车辆信息
		LastYearCarinfoBean lastYearCarinfoBean = lastYearPolicyInfoBean.getLastYearCarinfoBean();
		//车型信息
		JSONArray carModels = lastYearPolicyInfoBean.getCarModels();
//		CarModel carModel = lastYearPolicyInfoBean.getCarModel();
//		List<String> vehicletype = new ArrayList<String>();
//		vehicletype.add("KA");
//		vehicletype.add("KB");
//		String vehicle = "";
		if(null != lastYearCarinfoBean){
			String standardfullname = "";
			//先拿到车型全称
			if(null != carModels){
				if(carModels.size()==1){
					JSONObject car = JSONObject.fromObject(carModels.get(0));
					standardfullname = car.get("vehiclename").toString();
//					vehicle = car.get("syvehicletype").toString();
				}
			}
			//保存车辆信息
			INSBCarinfo insbCarinfo = new INSBCarinfo();
			insbCarinfo.setTaskid(taskid);
			carinfo = insbCarinfoDao.selectOne(insbCarinfo);
			if(null != carinfo){
				carid = carinfo.getId();
				//2 封装
				//车辆相关信息(carinfo)
				carinfo.setOperator(operator);
				carinfo.setCarlicenseno(lastYearCarinfoBean.getVehicleno());//车牌号
				//所属性质 使用性质不存在默认
				if(StringUtil.isEmpty(lastYearCarinfoBean.getUsingnature())){
//					if(vehicletype.contains(vehicle)){
//						carinfo.setCarproperty("1");//家庭自用
//					}else{
						carinfo.setCarproperty("");
//					}
				}else{
					carinfo.setCarproperty(getUsePropsFrommap(lastYearCarinfoBean.getUsingnature()));//车辆使用性质  字典表  UseProps
				}
				if(StringUtil.isEmpty(lastYearCarinfoBean.getVehicletype())){
					carinfo.setProperty("");//个人用车 不默认
				}else{
					carinfo.setProperty(lastYearCarinfoBean.getVehicletype());//所属性质  字典表  UserType ----不存在默认值个人用车
				}
				carinfo.setEngineno(lastYearCarinfoBean.getEngineno());//发动机号
				carinfo.setVincode(lastYearCarinfoBean.getVehicleframeno());//车辆识别代号 (车架号)
				carinfo.setIsTransfercar(lastYearCarinfoBean.getChgownerflag());//是否过户车 过户日期 (无)（是:1,否:0）
				carinfo.setRegistdate(ModelUtil.conbertStringToNyrDate(lastYearCarinfoBean.getRegisterdate()));//车辆初登日期
				carinfo.setDrivingarea(getCarDrivingArea(lastYearCarinfoBean.getArea()));//行驶区域  CarDrivingArea
				carinfo.setStandardfullname(standardfullname);
				//车辆照片
				carinfo.setBrandimg(lastYearCarinfoBean.getBrandimg());
				//平台传递品牌型号
				carinfo.setCifstandardname(lastYearCarinfoBean.getBrandmodel());
				insbCarinfoDao.updateById(carinfo);
			}else{
				//2 封装
				//save
				carinfo = new INSBCarinfo();
				carinfo.setTaskid(taskid);
				carinfo.setCreatetime(new Date());
				carinfo.setOperator(operator);
				carinfo.setCarlicenseno(lastYearCarinfoBean.getVehicleno());//车牌号
//				//所属性质 使用性质不存在默认
				if(StringUtil.isEmpty(lastYearCarinfoBean.getUsingnature())){
					//车辆类型  是这两种的时候，默认家庭自用
//					if(vehicletype.contains(vehicle)){
//						carinfo.setCarproperty("1");//家庭自用
//					}else{
						carinfo.setCarproperty("");
//					}
				}else{
					carinfo.setCarproperty(getUsePropsFrommap(lastYearCarinfoBean.getUsingnature()));//车辆使用性质  字典表  UseProps
				}
				if(StringUtil.isEmpty(lastYearCarinfoBean.getVehicletype())){
					carinfo.setProperty("");//个人用车 不默认
				}else{
					carinfo.setProperty(lastYearCarinfoBean.getVehicletype());//所属性质  字典表  UserType 不存在默认值个人用车
				}
				carinfo.setEngineno(lastYearCarinfoBean.getEngineno());//发动机号
				carinfo.setVincode(lastYearCarinfoBean.getVehicleframeno());//车辆识别代号 (车架号)
				carinfo.setIsTransfercar(lastYearCarinfoBean.getChgownerflag());//是否过户车 过户日期 (无)
				carinfo.setRegistdate(ModelUtil.conbertStringToNyrDate(lastYearCarinfoBean.getRegisterdate()));//车辆初登日期
				carinfo.setDrivingarea(lastYearCarinfoBean.getArea());//行驶区域
				carinfo.setStandardfullname(standardfullname);
				//车辆照片
				carinfo.setBrandimg(lastYearCarinfoBean.getBrandimg());
				//平台传递品牌型号
				carinfo.setCifstandardname(lastYearCarinfoBean.getBrandmodel());
				insbCarinfoDao.insert(carinfo);
				carid = carinfo.getId();
			}
			//保存投保书   快速续保 不保存
			if("0".equals(flag)){
				saveInsuranceBooksMark(taskid, "0");
			}
		}
		if(null != carModels && carModels.size() > 0){
			//1  车型信息 通过车辆信息去查
			INSBCarmodelinfo vo  = new INSBCarmodelinfo();
			vo.setCarinfoid(carid);
			carmoderCarmodelinfo =insbCarmodelinfoDao.selectOne(vo);
			if(null != carmoderCarmodelinfo){
				if(carModels.size() ==1){
					JSONObject car = JSONObject.fromObject(carModels.get(0));
					carmoderCarmodelinfo.setModifytime(new Date());
					carmoderCarmodelinfo.setOperator(operator);
					carmoderCarmodelinfo.setJgVehicleType(getjgVehicleTypeAndCodeName("VehicleType",car.get("vehicletype").toString()));//车辆类型  ?交管车辆类型  code=VehicleType
					carmoderCarmodelinfo.setBrandname(car.get("brandname").toString());//车辆品牌
					carmoderCarmodelinfo.setFamilyname(car.get("familyname").toString());//车系名称
					carmoderCarmodelinfo.setFactoryname(car.get("factoryname").toString());//生产厂商
					carmoderCarmodelinfo.setDisplacement(convertToDouble(car.get("displacement").toString()));//排气量
					carmoderCarmodelinfo.setFullweight(convertToDouble(car.get("fullweight").toString()));//整备质量
					carmoderCarmodelinfo.setLoads(convertToDouble(car.get("modelLoads").toString()));//核定载质量
					carmoderCarmodelinfo.setUnwrtweight(convertToDouble(car.get("modelLoads").toString()));//核定载质量
					carmoderCarmodelinfo.setGearbox(car.get("gearbox").toString());//变速器
					carmoderCarmodelinfo.setSeat(convertToInteger(car.get("seat").toString()));//座位数
					carmoderCarmodelinfo.setPrice(Double.valueOf(car.get("price").toString()));//新车购置价（不含税）
					carmoderCarmodelinfo.setListedyear(car.get("maketdate").toString());//上市年月
					carmoderCarmodelinfo.setCardeploy(car.get("configname").toString());//配置名称
					carmoderCarmodelinfo.setAnalogyprice(convertToDouble(car.get("analogyprice").toString()));//类比价
					carmoderCarmodelinfo.setAnalogytaxprice(convertToDouble(car.get("analogytaxprice").toString()));//类比价含税
					carmoderCarmodelinfo.setTaxprice(convertToDouble(car.get("taxprice").toString()));//税额
					carmoderCarmodelinfo.setCarVehicleOrigin(getCodeManuFacturer(car.get("manufacturer").toString()));//产地来源
					carmoderCarmodelinfo.setStandardname(car.get("vehiclename").toString());//车辆信息名称
					carmoderCarmodelinfo.setVehicleid(car.get("vehicleId").toString());//精友车型id
					carmoderCarmodelinfo.setSyvehicletypename(car.get("syvehicletypename").toString());//商业险车辆名称
					carmoderCarmodelinfo.setSyvehicletypecode(car.get("syvehicletype").toString());//机动车编码
					//标准车型
					carmoderCarmodelinfo.setIsstandardcar("0");
					carmoderCarmodelinfo.setCarprice("0");//车价选择，0最低，1最高，2自定义
					//查找问题
					//carmoderCarmodelinfo.setNoti("上年投保单带出来的"+new Date());
					insbCarmodelinfoDao.updateById(carmoderCarmodelinfo);
					LogUtil.info(taskid +"=====上年保单车型信息更新====== carmodelid:"+carmoderCarmodelinfo.getId()+" Vehicleid:"+carmoderCarmodelinfo.getVehicleid());
				}
			}else{
				//2 封装
				//save
				if(carModels.size() ==1){
					JSONObject car = JSONObject.fromObject(carModels.get(0));
					carmoderCarmodelinfo = new INSBCarmodelinfo();
					carmoderCarmodelinfo.setOperator(operator);
					//carmoderCarmodelinfo.setModifytime(new Date());
					carmoderCarmodelinfo.setCreatetime(new Date());
					carmoderCarmodelinfo.setCarinfoid(carid);//车辆信息表id
					carmoderCarmodelinfo.setOperator(operator);
					carmoderCarmodelinfo.setJgVehicleType(getjgVehicleTypeAndCodeName("VehicleType",car.get("vehicletype").toString()));//车辆类型  ?交管车辆类型  code=VehicleType
					carmoderCarmodelinfo.setBrandname(car.get("brandname").toString());//车辆品牌
					carmoderCarmodelinfo.setFamilyname(car.get("familyname").toString());//车系名称
					carmoderCarmodelinfo.setFactoryname(car.get("factoryname").toString());//生产厂商
					carmoderCarmodelinfo.setDisplacement(convertToDouble(car.get("displacement").toString()));//排气量
					carmoderCarmodelinfo.setFullweight(convertToDouble(car.get("fullweight").toString()));//整备质量
					carmoderCarmodelinfo.setLoads(convertToDouble(car.get("modelLoads").toString()));//核定载质量
					carmoderCarmodelinfo.setUnwrtweight(convertToDouble(car.get("modelLoads").toString()));//核定载质量
					carmoderCarmodelinfo.setGearbox(car.get("gearbox").toString());//变速器
					carmoderCarmodelinfo.setSeat(convertToInteger(car.get("seat").toString()));//座位数
					carmoderCarmodelinfo.setPrice(Double.valueOf(car.get("price").toString()));//新车购置价（不含税）
					carmoderCarmodelinfo.setListedyear(car.get("maketdate").toString());//上市年月
					carmoderCarmodelinfo.setCardeploy(car.get("configname").toString());//配置名称
					carmoderCarmodelinfo.setAnalogyprice(convertToDouble(car.get("analogyprice").toString()));//类比价
					carmoderCarmodelinfo.setAnalogytaxprice(convertToDouble(car.get("analogytaxprice").toString()));//类比价含税
					carmoderCarmodelinfo.setTaxprice(convertToDouble(car.get("taxprice").toString()));//税额
					carmoderCarmodelinfo.setCarVehicleOrigin(getCodeManuFacturer(car.get("manufacturer").toString()));//产地来源
					carmoderCarmodelinfo.setStandardname(car.get("vehiclename").toString());//车辆信息名称
					carmoderCarmodelinfo.setStandardfullname(car.get("vehiclename").toString());
					carmoderCarmodelinfo.setVehicleid(car.get("vehicleId").toString());//精友车型id
					carmoderCarmodelinfo.setSyvehicletypename(car.get("syvehicletypename").toString());//商业险车辆名称
					carmoderCarmodelinfo.setSyvehicletypecode(car.get("syvehicletype").toString());//机动车编码
					//标准车型
					carmoderCarmodelinfo.setIsstandardcar("0");
					carmoderCarmodelinfo.setCarprice("0");//车价选择，0最低，1最高，2自定义
					//查找问题
					//carmoderCarmodelinfo.setNoti("上年投保单带出来的"+new Date());
					insbCarmodelinfoDao.insert(carmoderCarmodelinfo);
                    LogUtil.info(taskid +"=====上年保单车型信息插入====== carmodelid:"+carmoderCarmodelinfo.getId()+" Vehicleid:"+carmoderCarmodelinfo.getVehicleid());
				}
			}
			//保存投保书   快速续保 不保存
			if("0".equals(flag)){
				saveInsuranceBooksMark(taskid, "1");
			}
		}

		// 根据实例id查询报价信息总表，获取id
		INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
		insbQuotetotalinfo.setTaskid(taskid);
		INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
		String agreeidpidsitid = "";
		//保险公司名称
		LastYearSupplierBean riskBean = lastYearPolicyInfoBean.getLastYearSupplierBean();
		//快速续保的时候一步跳过
		if(null != riskBean && !StringUtil.isEmpty(riskBean.getSupplierid())  && "1".equals(flag)){
			//获取供应商出单网点
			agreeidpidsitid = judjeThisProidInThat(riskBean.getSupplierid(), province, city, agentid, channeltype,taskid);
			if (null != quotetotalinfo && !StringUtil.isEmpty(agreeidpidsitid)) {
				INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
				quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
				List<INSBQuoteinfo> quoteinfos = insbQuoteinfoDao.selectList(quoteinfo);
				if(null != quoteinfos && quoteinfos.size() > 0){
					for(INSBQuoteinfo insbQuoteinfo : quoteinfos){
						//前端轮询   需要删除上年投保的公司 update 20160119
						//if(riskBean.getSupplierid().equals(insbQuoteinfo.getInscomcode())){
							insbQuoteinfoDao.deleteById(insbQuoteinfo.getId());
						//}
					}
				}
				INSBCarinfo querycar = new INSBCarinfo();
				querycar.setTaskid(taskid);
				INSBCarinfo querycarinfo = insbCarinfoDao.selectOne(querycar);
				if(null != querycarinfo){
					//上年商业承保公司
					querycarinfo.setPreinscode(riskBean.getSupplierid());
					insbCarinfoDao.updateById(querycarinfo);
				}
				if(null != quotetotalinfo){
					//上年商业承保公司
					quotetotalinfo.setLastyearsupplier(riskBean.getSuppliername());
					insbQuotetotalinfoDao.updateById(quotetotalinfo);
				}
				// 向报价信息表插入数据，一个报价公司一条
				String[] arra = agreeidpidsitid.split("#");
				if(arra.length == 4){
					INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
					insbQuoteinfo.setCreatetime(new Date());
					insbQuoteinfo.setOperator(quotetotalinfo.getOperator());
					insbQuoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
					if(null != querycarinfo){
						insbQuoteinfo.setOwnername(querycarinfo.getOwnername());
						insbQuoteinfo.setPlatenumber(querycarinfo.getCarlicenseno());
					}
					insbQuoteinfo.setInscomcode(arra[1]);
					insbQuoteinfo.setAgreementid(arra[0]);
					insbQuoteinfo.setDeptcode(arra[2]);
					insbQuoteinfo.setBuybusitype(arra[3]);

					if (StringUtil.isNotEmpty(arra[2])) {
						INSCDept dept = inscDeptDao.selectByComcode(arra[2]);
						if (dept != null) {
							String platformInnercode = inscDeptService.getPlatformInnercode(dept.getDeptinnercode());
							if (platformInnercode != null) {
								insbQuoteinfo.setPlatforminnercode(Integer.parseInt(platformInnercode));
							}
						}
					}

					insbQuoteinfoDao.insert(insbQuoteinfo);
				}
			}
			//保存投保书   快速续保 不保存
			//if("0".equals(flag)){
			//		saveInsuranceBooksMark(taskid, "2");
			//}
		}
		//险种信息
		JSONArray lastYearRiskinfos = lastYearPolicyInfoBean.getLastYearRiskinfos();
		//快速续保的时候一步跳过
		if(null != lastYearRiskinfos && lastYearRiskinfos.size() > 0 && null != riskBean && !StringUtil.isEmpty(riskBean.getSupplierid()) && "1".equals(flag)){
			//获取供应商出单网点
			//String agreeidpidsitid = judjeThisProidInThat(riskBean.getSupplierid(), province, city, agentid, channeltype,taskid);
			//上年投保公司验证通过，保存保险配置
			if(!StringUtil.isEmpty(agreeidpidsitid)){
				List<String> list = new ArrayList<String>();
				for(int i = 0;i < lastYearRiskinfos.size(); i++){
					JSONObject jsonObject = JSONObject.fromObject(lastYearRiskinfos.get(i));
					list.add(jsonObject.getString("kindcode").trim());
				}
				//不管有没有交强险车船税，都加上 20151201
		 		list.add("VehicleCompulsoryIns");
				list.add("VehicleTax");
				//商业险
				List<BusinessRisks> businessRisks = new ArrayList<BusinessRisks>();
				//交强险
				List<StrongRisk> strongRisks = new ArrayList<StrongRisk>();
				if(list.size() > 0){
					List<InsureConfigsModel> insureConfigs = appInsuredQuoteDao.queryInsureConfigByKindCode(list);
					for(InsureConfigsModel configsBean : insureConfigs){
						if("0".equals(configsBean.getType())){
							BusinessRisks risks = new BusinessRisks();
							risks.setCode(configsBean.getRiskkindcode());
							//设置保额
							for(int i = 0;i < lastYearRiskinfos.size(); i++){
								JSONObject jsonObject = JSONObject.fromObject(lastYearRiskinfos.get(i));
								if(jsonObject.getString("kindcode").trim().equals(configsBean.getRiskkindcode())){
									risks.setCoverage(jsonObject.getString("amount"));
									risks.setSelectedOption(jsonObject.getString("amount"));
								}
								if(jsonObject.getString("kindcode").trim().equals("Ncf"+configsBean.getRiskkindcode())){
									//有不计免赔
									risks.setFlag("0");
								}
							}
							//不计免赔
							risks.setName(configsBean.getRiskkindname());
							if("GlassIns".equals(configsBean.getRiskkindcode())){
								risks.setType("02");
							}else{
								risks.setType("01");
							}
							risks.setUnit("");
							businessRisks.add(risks);
						}else{
							StrongRisk strongRisk = new StrongRisk();
							strongRisk.setCode(configsBean.getRiskkindcode());
							strongRisk.setName(configsBean.getRiskkindname());
							strongRisk.setSelected("1");
							strongRisks.add(strongRisk);
						}
					}
				}
				//保存
				List<String> providers = new ArrayList<String>();
				INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
				quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
				List<INSBQuoteinfo> quoteinfos = insbQuoteinfoDao.selectList(quoteinfo);
				if(null != quoteinfos && quoteinfos.size() > 0){
					for(INSBQuoteinfo insbQuoteinfo : quoteinfos){
						providers.add(insbQuoteinfo.getInscomcode());
					}
				}
				//providers.add(riskBean.getSupplierid());
				if(null != providers && providers.size() > 0){
					saveInsuredConfig(taskid,operator,businessRisks,strongRisks,providers, "","snbxpz","","");
				}
			}
		}

		//车主信息
		LastYearPersonBean carowner = lastYearPolicyInfoBean.getCarowner();
		//投保信息
		LastYearPolicyBean policy = lastYearPolicyInfoBean.getLastYearPolicyBean();
		//保存投保信息
		allPeopleinfoSave(taskid, operator, policy, carowner);
		//快速续保调用工作流，获取子实例id
		if("1".equals(flag)){
			//int result = noticeWorkflowStartQuote(taskid,"1");
			//快速续保标志
			if (null != quotetotalinfo) {
				quotetotalinfo.setIsrenewal("1");
				insbQuotetotalinfoDao.updateById(quotetotalinfo);
			}
			//System.out.println("========="+result);
		}
	}
	//保存投保人，被保人信息
	private void allPeopleinfoSave(String taskid, String operator,LastYearPolicyBean policy,LastYearPersonBean carowner) {
        // 车主信息
		INSBCarowneinfo insbCarowneinfo = new INSBCarowneinfo();
		insbCarowneinfo.setTaskid(taskid);
		INSBCarowneinfo insbCarowneinfo2 = insbCarowneinfoDao.selectOne(insbCarowneinfo);
		// 车主
		INSBPerson carperson = insbPersonDao.selectById(insbCarowneinfo2.getPersonid());
		if (null == carowner || StringUtil.isEmpty(carowner.getName().trim())){
			//1.投保人，被保人默认为输入的车主信息 2.投保人，被保人默认为平台查询的信息
			savePeopleInfo(carperson, taskid, operator, policy);
		}else{
			//更新车主信息
			//carperson.setName(carowner.getName());
			carperson.setEmail(carowner.getEmail());
			//身份证
			if("0".equals(getPeopleIdcardType(carowner.getIdtype())) && carowner.getIdno().length() == 18){
				carperson.setGender(ModelUtil.getGenderByIdCard(carowner.getIdno()));
			}
			carperson.setIdcardtype(getPeopleIdcardTypeInteger(carowner.getIdtype()));
			carperson.setIdcardno(carowner.getIdno());
			carperson.setCellphone(carowner.getMobile());
			carperson.setAddress(carowner.getAddrss());
			insbPersonDao.updateById(carperson);
			//1.投保人，被保人默认为平台查询的车主信息 2.投保人，被保人默认为平台查询的信息
			savePeopleInfo(carperson, taskid, operator, policy);
		}

		// 更新保单表数据
		// 保单信息
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskid);
		List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(insbPolicyitem);
		//投保人
		INSBApplicant applicant = new INSBApplicant();
		applicant.setTaskid(taskid);
		INSBApplicant insbApplicant = insbApplicantDao.selectOne(applicant);
		INSBPerson tbperson = insbPersonDao.selectById(insbApplicant.getPersonid()); ;
		//被保人
		INSBInsured insured = new INSBInsured();
		insured.setTaskid(taskid);
		INSBInsured insbInsured = insbInsuredDao.selectOne(insured);
		INSBPerson bbperson = insbPersonDao.selectById(insbInsured.getPersonid());
		for (INSBPolicyitem policyitem : insbPolicyitems) {
			policyitem.setApplicantid(insbApplicant.getId());
			if (null == tbperson) {
				policyitem.setApplicantname(carperson.getName());
			} else {
				policyitem.setApplicantname(tbperson.getName());
			}
			policyitem.setInsuredid(insbInsured.getId());
			if (null == bbperson) {
				policyitem.setApplicantname(carperson.getName());
			} else {
				policyitem.setApplicantname(bbperson.getName());
			}
			policyitem.setCarownerid(insbCarowneinfo2.getId());
			policyitem.setCarownername(carperson.getName());
			insbPolicyitemDao.updateById(policyitem);
		}
	}

	private void savePeopleInfo(INSBPerson carperson, String taskid,String operator, LastYearPolicyBean policy){
		if(null != policy){
			saveDefaultCifPeopleInfo(carperson, taskid, operator, policy);
		}else{
			saveDefaultPeopleInfo(carperson, taskid, operator);
		}
	}

	//投保人，被保人默认车主信息 ,只存一条记录 20160119
	private void saveDefaultPeopleInfo(INSBPerson carperson, String taskid, String operator){
        //被保人
		INSBInsured insured = new INSBInsured();
		insured.setTaskid(taskid);
		INSBInsured insbInsured2 = insbInsuredDao.selectOne(insured);
		if (null == insbInsured2) {         
            insbPersonHelpService.addPersonIsNull(carperson,operator,ConstUtil.STATUS_2);
		}
		//投保人
		INSBApplicant applicant = new INSBApplicant();
		applicant.setTaskid(taskid);
		INSBApplicant insbApplicant2 = insbApplicantDao.selectOne(applicant);
		if (null == insbApplicant2) {
			insbPersonHelpService.addPersonIsNull(carperson,operator,ConstUtil.STATUS_1);
		}
		//权益索赔人
		INSBLegalrightclaim insbLegalrightclaim = new INSBLegalrightclaim();
		insbLegalrightclaim.setTaskid(taskid);
		INSBLegalrightclaim legalrightclaim = insbLegalrightclaimDao.selectOne(insbLegalrightclaim);
		if(null == legalrightclaim){
			insbPersonHelpService.addPersonIsNull(carperson,operator,ConstUtil.STATUS_3);
		}
		//联系人
		INSBRelationperson insbRelationperson = new INSBRelationperson();
		insbRelationperson.setTaskid(taskid);
		INSBRelationperson relationperson = insbRelationpersonDao.selectOne(insbRelationperson);
		if(null == relationperson){
			insbPersonHelpService.addPersonIsNull(carperson,operator,ConstUtil.STATUS_4);
		}
	}

    //投保人，被保人平台查询的信息不回写了 2016-08-08
	//投保人，被保人平台查询不存在，默认车主信息  2016-03-29
	private void saveDefaultCifPeopleInfo(INSBPerson carperson, String taskid,String operator, LastYearPolicyBean policy){
		//被保人存在保存平台存的，不存在则默认车主
		LastYearPersonBean personBean = policy.getInsureder();
		if(null == personBean || StringUtil.isEmpty(personBean.getName())){
			INSBInsured insured = new INSBInsured();
			insured.setTaskid(taskid);
			INSBInsured insbInsured2 = insbInsuredDao.selectOne(insured);
			if(null == insbInsured2){
				insbPersonHelpService.addPersonIsNull(carperson,operator,ConstUtil.STATUS_2);
			}
		}else{
			INSBInsured insbInsured2 = new INSBInsured();
			insbInsured2.setTaskid(taskid);
			INSBInsured insuredc = insbInsuredDao.selectOne(insbInsured2);
			/*//如果车主证件号与被保人证件号一致，默认同一个人
			if(!StringUtil.isEmpty(carperson.getIdcardno()) && carperson.getIdcardno().equals(personBean.getIdno())){
				if(null == insuredc){
					INSBInsured insbInsured = new INSBInsured();
					insbPersonHelpService.addINSBInsured(insbInsured, null, operator, taskid, carperson.getId());
				}
			}else{*/
				if(null == insuredc){
					INSBPerson bbperson = new INSBPerson();
					bbperson=insbPersonHelpService.addLastYearPersonBean(bbperson, personBean, operator, taskid,
							getPeopleIdcardType(personBean.getIdtype()),getPeopleIdcardTypeInteger(personBean.getIdtype()));
					INSBInsured insbInsured = new INSBInsured();
					insbPersonHelpService.addINSBInsured(insbInsured, null, operator, taskid, bbperson.getId());
				}
			//}
		}
		//投保人 不存在默认车主，存在插入新的数据
		LastYearPersonBean yearPersonBean = policy.getProper();
		if(null == yearPersonBean || StringUtil.isEmpty(yearPersonBean.getName())){
			INSBApplicant applicant = new INSBApplicant();
			applicant.setTaskid(taskid);
			INSBApplicant insbApplicant2 = insbApplicantDao.selectOne(applicant);
			if(null == insbApplicant2){
				insbPersonHelpService.addPersonIsNull(carperson,operator,ConstUtil.STATUS_1);
			}
		}else{
			INSBApplicant insbApplicant2 = new INSBApplicant();
			insbApplicant2.setTaskid(taskid);
			INSBApplicant applicantc = insbApplicantDao.selectOne(insbApplicant2);
			/*//与车主证件号一致默认同一个人
			if(null != personBean && !StringUtil.isEmpty(carperson.getIdcardno()) && yearPersonBean.getIdno().equals(carperson.getIdcardno())){
				if(null == applicantc){
					INSBApplicant insbApplicant = new INSBApplicant();
					insbPersonHelpService.addINSBApplicant(insbApplicant, null, operator, taskid, carperson.getId());
				}
			}else{*/
				if(null == applicantc){
					INSBPerson tbperson = new INSBPerson();
					tbperson=insbPersonHelpService.addLastYearPersonBean(tbperson, yearPersonBean, operator, taskid,
							getPeopleIdcardType(yearPersonBean.getIdtype()),getPeopleIdcardTypeInteger(yearPersonBean.getIdtype()));
					INSBApplicant insbApplicant = new INSBApplicant();
					insbPersonHelpService.addINSBApplicant(insbApplicant, null, operator, taskid, tbperson.getId());
				}
			//}
		}
		//权益索赔人  默认与车主一致
		INSBLegalrightclaim insbLegalrightclaim = new INSBLegalrightclaim();
		insbLegalrightclaim.setTaskid(taskid);
		INSBLegalrightclaim legalrightclaim = insbLegalrightclaimDao.selectOne(insbLegalrightclaim);
		if(null == legalrightclaim){
			insbPersonHelpService.addPersonIsNull(carperson,operator,ConstUtil.STATUS_3);
		}
		//联系人  默认与车主一致
		INSBRelationperson insbRelationperson = new INSBRelationperson();
		insbRelationperson.setTaskid(taskid);
		INSBRelationperson relationperson = insbRelationpersonDao.selectOne(insbRelationperson);
		if(null == relationperson){
			insbPersonHelpService.addPersonIsNull(carperson,operator,ConstUtil.STATUS_4);
		}
	}

	/**
	 * 保存上年出险信息 20160317
	 * @param taskid
	 * @param operator
	 */
	private void saveLastYearClaimsInfo(String taskid,String operator,LastYearPolicyInfoBean lastYearPolicyInfoBean){
		String lastproviderid = "";
		String lastprovidername = "";
		String sypolicyno = "";
		String jqpolicyno = "";
		int claimtimes = 0;
		double claimrate = 0d;
		String jqclaimtimes = "0";
		double jqclaimrate  = 0d;
		String firstinsuretype = "";
		double lastclaimsum  = 0d;
		double jqlastclaimsum  = 0d;
		String trafficoffence  = "0";
		double trafficoffencediscount = 0d;
		//20160328
		String noclaimdiscountcoefficient = "";// 无赔款优待系数-
		double compulsoryclaimrate = 0.0;// 交强险理赔系数-
		String bwcompulsoryclaimtimes = "";//交强险理赔次数 汉子
		String bwcommercialclaimtimes = "";//商业险理赔次数 汉子
		String noclaimdiscountcoefficientreasons = "";// 无赔款折扣浮动原因-
		String compulsoryclaimratereasons = "";// 交强险事故浮动原因
		String syclaims = "";//商业险事故 JSONArray
		String jqclaims = "";//交强险事故 JSONArray
		String systartdate = "";
		String syenddate = "";
		String jqstartdate = "";
		String jqenddate = "";

		if(null != lastYearPolicyInfoBean){
			if(null != lastYearPolicyInfoBean.getLastYearPolicyBean()){
				sypolicyno = lastYearPolicyInfoBean.getLastYearPolicyBean().getPolicyno();
				jqpolicyno = lastYearPolicyInfoBean.getLastYearPolicyBean().getJqpolicyno();
				systartdate = lastYearPolicyInfoBean.getLastYearPolicyBean().getStartdate();
				syenddate = lastYearPolicyInfoBean.getLastYearPolicyBean().getEnddate();
				jqstartdate = lastYearPolicyInfoBean.getLastYearPolicyBean().getJqstartdate();
				jqenddate = lastYearPolicyInfoBean.getLastYearPolicyBean().getJqenddate();

				List<INSBRulequeryrepeatinsured> insbRulequeryrepeatinsuredList= insbRulequeryrepeatinsuredDao.selectPolicy(taskid);
				if (insbRulequeryrepeatinsuredList == null || insbRulequeryrepeatinsuredList.isEmpty()) {
					INSBRulequeryrepeatinsured insbRulequeryrepeatinsured = new INSBRulequeryrepeatinsured();
					insbRulequeryrepeatinsured.setOperator("admin");
					insbRulequeryrepeatinsured.setCreatetime(new Date());
					insbRulequeryrepeatinsured.setTaskid(taskid);
					insbRulequeryrepeatinsured.setRisktype("0");
					insbRulequeryrepeatinsured.setPolicyid(sypolicyno);
					insbRulequeryrepeatinsuredDao.insert(insbRulequeryrepeatinsured);
				} else {
					for (INSBRulequeryrepeatinsured insbRulequeryrepeatinsured : insbRulequeryrepeatinsuredList) {
						if (insbRulequeryrepeatinsured == null || StringUtil.isEmpty(insbRulequeryrepeatinsured.getRisktype())) continue;
						if ("0".equals(insbRulequeryrepeatinsured.getRisktype())) {
							insbRulequeryrepeatinsured.setPolicyid(sypolicyno);
						} else  if ("1".equals(insbRulequeryrepeatinsured.getRisktype())) {
							insbRulequeryrepeatinsured.setPolicyid(jqpolicyno);
						}
						insbRulequeryrepeatinsured.setModifytime(new Date());
						insbRulequeryrepeatinsuredDao.updateById(insbRulequeryrepeatinsured);
					}
				}

			}
			if(null != lastYearPolicyInfoBean.getLastYearSupplierBean()){
				lastproviderid = lastYearPolicyInfoBean.getLastYearSupplierBean().getSupplierid();
				lastprovidername = lastYearPolicyInfoBean.getLastYearSupplierBean().getSuppliername();
			}
			LastYearClaimBean lastYearClaimBean = lastYearPolicyInfoBean.getLastYearClaimBean();
			if(null != lastYearClaimBean){
				claimtimes = lastYearClaimBean.getClaimtimes();
				claimrate = lastYearClaimBean.getClaimrate();
				jqclaimtimes = lastYearClaimBean.getJqclaimtimes();
				firstinsuretype = lastYearClaimBean.getFirstinsuretype();
				lastclaimsum = lastYearClaimBean.getLastclaimsum();
				jqlastclaimsum = lastYearClaimBean.getJqlastclaimsum();
				trafficoffence = lastYearClaimBean.getTrafficoffence();
				jqclaimrate = lastYearClaimBean.getJqclaimrate();
				trafficoffencediscount = lastYearClaimBean.getTrafficoffencediscount();

				noclaimdiscountcoefficient = lastYearClaimBean.getNoclaimdiscountcoefficient();
				compulsoryclaimrate = lastYearClaimBean.getCompulsoryclaimrate();
				bwcompulsoryclaimtimes = lastYearClaimBean.getBwcompulsoryclaimtimes();
				bwcommercialclaimtimes = lastYearClaimBean.getBwcommercialclaimtimes();
				noclaimdiscountcoefficientreasons = lastYearClaimBean.getNoclaimdiscountcoefficientreasons();
				compulsoryclaimratereasons = lastYearClaimBean.getCompulsoryclaimratereasons();
				if(!lastYearClaimBean.getSyclaims().isEmpty()){
					syclaims = lastYearClaimBean.getSyclaims().toString();
				}
				if(!lastYearClaimBean.getJqclaims().isEmpty()){
					jqclaims = lastYearClaimBean.getJqclaims().toString();
				}
			}
		}

	}

	private List<InsureConfigsBean> getInsuredconfigByJaonArray(JSONArray lastYearRiskinfos){
		//update 20160317 显示所有的险别，选中上年保险配置
		List<InsureConfigsBean> configsBeans = new ArrayList<InsureConfigsBean>();
		Map<String, String> lastconfig = new HashMap<String, String>();

		//上年购买的险种"lastYearRiskinfos":[{"amount":439620,"kindcode":"VehicleDemageIns","kindname":"车损险","prem":3146.92}...]
		Map<String, String> lastbuyMap = new HashMap<String, String>();

		boolean NcfClause = false;
		if(null != lastYearRiskinfos && lastYearRiskinfos.size() > 0){
			for(int i = 0;i < lastYearRiskinfos.size(); i++) {
				JSONObject jsonObject = JSONObject.fromObject(lastYearRiskinfos.get(i));
				String riskcode = jsonObject.getString("kindcode");
				if (StringUtil.isNotEmpty(riskcode)) {
					riskcode = riskcode.trim();
					lastbuyMap.put(riskcode, jsonObject.getString("kindname"));
				}
				if ("NcfClause".equals(riskcode)) {
					NcfClause = true;
				}
			}

			for(int i = 0;i < lastYearRiskinfos.size(); i++){
				JSONObject jsonObject = JSONObject.fromObject(lastYearRiskinfos.get(i));
				String riskcode = jsonObject.getString("kindcode").trim();
				//特殊处理的不计免赔
				if("NcfDriverPassengerIns".equals(riskcode)){
					lastconfig.put("NcfDriverIns","投保");
					lastconfig.put("NcfPassengerIns","投保");
				}
				//1.如果上年保险配置中有购买“附加险不计免赔”（NcfAddtionalClause）。则自动将上年配置中的已购买商业险中各附加险（包括：自燃险、划痕险、涉水险）的不计免赔勾选上。
				//2.如果上年购买了“机动车车上人员责任不计免赔保险”，则自动将上年配置中的已购买的司机责任险、乘客责任险的不计免赔勾选上。（已完成）
				//3.如果上年购买了“基本险不计免赔”，则自动将上年配置中的已购买的车损险、三者险、司机责任险、乘客责任险、盗抢险的不计免赔勾选上。
				//4.如果上年购买了“不计免赔险”，则自动将所有已购买险种的不计免赔险勾选上。
				if("NcfAddtionalClause".equals(riskcode)){ //1
					lastconfig.put("NcfCombustionIns","投保");
					lastconfig.put("NcfScratchIns","投保");
					lastconfig.put("NcfWadingIns","投保");
				}
				if("NcfBasicClause".equals(riskcode)){ //3
					lastconfig.put("NcfVehicleDemageIns","投保");
					lastconfig.put("NcfThirdPartyIns","投保");
					lastconfig.put("NcfDriverIns","投保");
					lastconfig.put("NcfPassengerIns","投保");
					lastconfig.put("NcfTheftIns","投保");
				}
				if (NcfClause) { //4
					lastconfig.put("Ncf" +riskcode ,"投保");
				}
				lastconfig.put(riskcode,jsonObject.getString("amount").trim());
			}
		}
		//不管有没有交强险车船税，都加上 20160314  BUG4720 20160918
//		lastconfig.put("VehicleCompulsoryIns","购买");
//		lastconfig.put("VehicleTax","代缴");
		List<InsureConfigsModel> insureConfigs = appInsuredQuoteDao.queryInsureConfigByKey("jjshx");
		for(InsureConfigsModel configsBean : insureConfigs){
			InsureConfigsBean insureConfigsBean = new InsureConfigsBean();
			insureConfigsBean.setId(configsBean.getId());
			insureConfigsBean.setIsdeductible(configsBean.getIsdeductible());
			insureConfigsBean.setPlankey("snbxpz");
			insureConfigsBean.setPlanname("上年保险配置信息");
			insureConfigsBean.setPrekindcode(configsBean.getPrekindcode());
			insureConfigsBean.setRiskkindcode(configsBean.getRiskkindcode());
			insureConfigsBean.setRiskkindname(configsBean.getShortname());
			insureConfigsBean.setType(configsBean.getType());
			//全不选中
			insureConfigsBean.setIsSelect(false);
			//insureConfigsBean.setCoverage(configsBean.getSelectkey());
			//默认不选中不计免赔
			insureConfigsBean.setFlag(false);
			String dataTemp = configsBean.getDatatemp();
			if(!StringUtil.isEmpty(dataTemp)){
				InsuredConfig insuredConfig = stringToInsuredConfig(dataTemp);
				insureConfigsBean.setInsuredConfig(insuredConfig);
			}
			//1拥有不计免赔的险种，判断不计免赔是否选中
			if("1".equals(configsBean.getIsdeductible()) && lastbuyMap.containsKey(configsBean.getRiskkindcode())){
				if(lastconfig.containsKey("Ncf"+configsBean.getRiskkindcode())){
					//选中不计免赔
					insureConfigsBean.setFlag(true);
				}
			}
			if(lastconfig.containsKey(configsBean.getRiskkindcode())){
				//选中
				insureConfigsBean.setIsSelect(true);
				//0商业险 1 不计免赔 2 交强险 3 车船税
				if("0".equals(configsBean.getType())){
					insureConfigsBean.setCoverage(lastconfig.get(configsBean.getRiskkindcode()).trim());
				}else if("2".equals(configsBean.getType())){
					insureConfigsBean.setCoverage("购买");
				}else if("3".equals(configsBean.getType())){
					insureConfigsBean.setCoverage("代缴");
				}
			}else{
				//选中
				insureConfigsBean.setIsSelect(false);
				//0商业险 1 不计免赔 2 交强险 3 车船税
				if("0".equals(configsBean.getType())){
					insureConfigsBean.setCoverage("不投保");
				}else if("2".equals(configsBean.getType())){
					insureConfigsBean.setCoverage("不购买");
				}else if("3".equals(configsBean.getType())){
					insureConfigsBean.setCoverage("不代缴");
				}
			}
			configsBeans.add(insureConfigsBean);
		}
		return configsBeans;
	}

	private int getPeopleIdcardTypeInteger(String key){
		return StringUtil.isEmpty(getPeopleIdcardType(key))?0:Integer.parseInt(getPeopleIdcardType(key));
	}

	/**
	 * 平台查询，证件信息映射
	 * 01 == 0 居民身份证
		02 == 1	居民户口簿
		03 == 2	驾驶证
		04 == 3	军官证
		05 == 3	士兵证
		07 == 4	中国护照
		09 == 5	港澳通行证
		10 == 5	台湾通行证
		66 == 8 社会信用代码证
		71 == 6	组织机构代码证
		99 == 7	其他证件
	 * @param key
	 * @return
	 */
	private String getPeopleIdcardType(String key){
		if(StringUtil.isEmpty(key)){
			return "";
		}
		Map<String, String> mapping = new HashMap<String, String>();
		mapping.put("01", "0");//身份证
		mapping.put("02", "1");//户口本
		mapping.put("03", "2");//驾照
		mapping.put("04", "3");//军官证/士兵证
		mapping.put("05", "3");//军官证/士兵证
		mapping.put("07", "4");//护照
		mapping.put("09", "5");//港澳回乡证/台胞证
		mapping.put("10", "5");//港澳回乡证/台胞证
		mapping.put("66", "8");//社会信用代码证
		mapping.put("71", "6");//组织代码证
		mapping.put("99", "7");//其他证件
		return StringUtil.isEmpty(mapping.get(key))?"7":mapping.get(key);
	}
	//产地来源
	private  int getCodeManuFacturer(String key){
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("国产", 0);
		map.put("合资", 2);
		map.put("进口", 1);
		return null == map.get(key)||"".equals(map.get(key))?0:map.get(key);
	}
	/**
	 * 车辆性质与平台映射
	 * @param key
	 * @return
	 */
	public String getUsePropsFrommap(String key){
		Map<String, String> mapping = new HashMap<String, String>();
		mapping.put("0", "");//不区分营业非营业 2 8 9 15 16 17
		mapping.put("101", "2");//营业出租租赁
		mapping.put("102", "3");//营业城市公交
		mapping.put("103", "4");//营业公路客运
		mapping.put("104", "6");//营业货车
		mapping.put("1", "1");//家庭自用
		mapping.put("220", "10");//非营业企业客车
		mapping.put("230", "11");//非营业机关客车
		mapping.put("240", "12");//非营业货车
		mapping.put("701", "16");//非营业特种车车型一
		mapping.put("702", "16");//非营业特种车车型二
		mapping.put("703", "16");//非营业特种车车型三
		mapping.put("704", "16");//非营业特种车车型四
		mapping.put("801", "15");//营业特种车车型一
		mapping.put("802", "15");//营业特种车车型二
		mapping.put("803", "15");//营业特种车车型三
		mapping.put("804", "15");//营业特种车车型四
		mapping.put("210", "");//非营业个人

//		mapping.put("200", "2");//非营业
//		mapping.put("100", "8");//营业
//		mapping.put("401", "17");//挂车
//		mapping.put("501", "8");//50CC及以下摩托车
//		mapping.put("502", "8");//50CC-250CC(含)摩托车
//		mapping.put("503", "8");//250CC以上摩托车及侧三轮
//		mapping.put("601", "15");//兼用型14.7KW及以下拖拉机
//		mapping.put("602", "15");//兼用型14.7KW以上拖拉机
//		mapping.put("603", "16");//运输型14.7KW及以下拖拉机
//		mapping.put("604", "16");//运输型14.7KW以上拖拉机
//		mapping.put("605", "16");//变型拖拉机

		mapping.put("200", "");//非营业
		mapping.put("100", "");//营业
		mapping.put("401", "");//挂车
		mapping.put("501", "");//50CC及以下摩托车
		mapping.put("502", "");//50CC-250CC(含)摩托车
		mapping.put("503", "");//250CC以上摩托车及侧三轮
		mapping.put("601", "");//兼用型14.7KW及以下拖拉机
		mapping.put("602", "");//兼用型14.7KW以上拖拉机
		mapping.put("603", "");//运输型14.7KW及以下拖拉机
		mapping.put("604", "");//运输型14.7KW以上拖拉机
		mapping.put("605", "");//变型拖拉机
		return null == mapping.get(key)||"".equals(mapping.get(key))?"":mapping.get(key);
	}
	public String getCarDrivingArea(String key){
		if (StringUtil.isEmpty(key)) {
			return "";
		}
		Map<String, String> mapping = new HashMap<String, String>();
		mapping.put("1", "1");//出入境
		mapping.put("2", "0");//境内
		mapping.put("3", "2");//省内
		mapping.put("4", "3");//场内
		mapping.put("5", "4");//固定线路
		return null == mapping.get(key)||"".equals(mapping.get(key))?"":mapping.get(key);
	}
	/**
	 * 判断上年查询的保险公司是否是当前代理人关联的
	 * @param thispid 上年保险公司
	 * @param province 省代码
	 * @param city 市代码
	 * @param agentid 代理人id
	 * @param channeltype 渠道
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String judjeThisProidInThat(String thispid,String province,String city,String agentid,String channeltype,String taskid){
		StringBuffer buffer = new StringBuffer("");
		String treeTop = thispid;
		if(thispid.length() > 4){
			treeTop = thispid.substring(0, 4);
		}
		SearchProviderModel model = new SearchProviderModel();
		model.setProcessinstanceid(taskid);
		model.setAgentid(agentid);
		model.setChannel(channeltype);
		model.setCity(city);
		model.setProvince(province);
		CommonModel commonModel = searchProvider(model);
		List<ProviderListBean> providerListBeans = (List<ProviderListBean>) commonModel.getBody();
		if(null != providerListBeans && providerListBeans.size() > 0){
			for(ProviderListBean providerListBean : providerListBeans){
				String channel = providerListBean.getChanneltype();
				if(channel.contains(",")){
					channel = channel.substring(0, channel.indexOf(","));
				}
				List<BranchProBean> branchProBeans = providerListBean.getBranchProBeans();
				if(null != branchProBeans && branchProBeans.size() > 0 && "0".equals(providerListBean.getReservedPlatformresult())){//拥有平台查询能力
					for(BranchProBean branchProBean : branchProBeans){
						//上年投保公司id，当前存在，默认一个出单网点
						if(thispid.equals(branchProBean.getDeptId()) || branchProBean.getDeptId().startsWith(treeTop)){
							buffer.append(branchProBean.getAgreementid()+ "#" + branchProBean.getDeptId());
							List<SingleSiteBean> singleSiteBeans = branchProBean.getSingleSiteBeans();
							if(null != singleSiteBeans && singleSiteBeans.size() > 0){
								//bug-4429 3014629 报价阶段，后台根据上年投保公司自动添加平安进行报价，但是报价网点没有选择代理人工号常用网点。
								for (SingleSiteBean ssb : singleSiteBeans) {
									LogUtil.info(taskid+ "SingleSiteBean: "+ ssb.getSiteId() + ",isSelected:" + ssb.isSelected());
									if (ssb.isSelected()) {
										buffer.append("#" + ssb.getSiteId()+"#"+channel);
										return buffer.toString();
									}
								}
								buffer.append("#" + singleSiteBeans.get(0).getSiteId()+"#"+channel);
								return buffer.toString();
							}
						}
					}
				}
			}
		}
		return buffer.toString();
	}

	@Override
	public CommonModel queryCarinfoModel(String processinstanceid) {
		CommonModel commonModel = new CommonModel();
		try {
			if(StringUtil.isEmpty(processinstanceid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			QueryCarinfoModelBean carinfoModelBean = appInsuredQuoteDao.selectCarinfoModelBean(processinstanceid);
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(carinfoModelBean);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	private List<InsureConfigsBean> queryLastYearInsConfig(
			String processinstanceid) {
		// 先从redis里面取值，取到直接返回
		LastYearPolicyInfoBean yearPolicyInfoBean = redisClient.get(Constants.TASK, processinstanceid, LastYearPolicyInfoBean.class);
		List<InsureConfigsBean> configsBeans = null;
//		int seat = 0;
//		INSBCarmodelinfo carmodelinfo = appInsuredQuoteDao.selectCarmodelInfoByTaskid(processinstanceid);
//		if(null != carmodelinfo){
//			seat = carmodelinfo.getSeat();
//		}
		if (null != yearPolicyInfoBean) {
			// 险种信息
			JSONArray lastYearRiskinfos = yearPolicyInfoBean
					.getLastYearRiskinfos();
			if (null != lastYearRiskinfos && lastYearRiskinfos.size() > 0) {
				configsBeans = getInsuredconfigByJaonArray(lastYearRiskinfos);
			}
		}
		return configsBeans;
	}

	@Override
	public CommonModel addQuoteProvider(SearchProviderModel model) {
		ExtendCommonModel commonModel = new ExtendCommonModel();
		try {

			//流程id
			String taskid = model.getProcessinstanceid();
			if(StringUtil.isEmpty(taskid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}

			//报价总表信息
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(taskid);

			//得到报价总表信息
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			if(null == quotetotalinfo){
				commonModel.setStatus("fail");
				commonModel.setMessage("报价总表信息不存在");
				return commonModel;
			}

			//获取报价信息表，报价公司id列表
			INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
			insbQuoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
			List<INSBQuoteinfo> insbQuoteinfos = insbQuoteinfoDao.selectList(insbQuoteinfo);

			List<ProviderListBean> removeProviderListBeans = new ArrayList<ProviderListBean>();

			if(null == insbQuoteinfos || insbQuoteinfos.size() <= 0){
				commonModel.setStatus("fail");
				commonModel.setMessage("报价表信息不存在");
				return commonModel;
			}else{
				for(INSBQuoteinfo quoteinfoModel :insbQuoteinfos){
					ProviderListBean pListModel = new ProviderListBean();
					pListModel.setProId(newIdSubstring(quoteinfoModel.getInscomcode()));
					pListModel.setPrvcode(newIdSubstring(quoteinfoModel.getInscomcode()));
					removeProviderListBeans.add(pListModel);
				}
			}
			CommonModel  commom= searchProvider(model);
			@SuppressWarnings("unchecked")
			List<ProviderListBean> providerListBeans  = (List<ProviderListBean>)commom.getBody();
			//已经存在的保险公司
			providerListBeans.removeAll(removeProviderListBeans);


			Map<String, Object> extend = new HashMap<String, Object>();
			if(quotetotalinfo != null){
				if(StringUtil.isEmpty(quotetotalinfo.getCloudstate())){
					extend.put("cloudstate", null);
				}else{
					extend.put("cloudstate", "1".equals(quotetotalinfo.getCloudstate())?true:false);
				}
				extend.put("handersupplier", quotetotalinfo.getHandersupplier());
				extend.put("lastyearsupplier", quotetotalinfo.getLastyearsupplier());
				extend.put("insuredatemap", lastInsuredDate(taskid));
			}
			extend.put("feeinfo", getChangeFee(model.getAgentid(),null));
			extend.put("suppliername", getLastYearProName(taskid));
			commonModel.setExtend(extend);

			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(providerListBeans);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	private String newIdSubstring(String oldpid){
		return oldpid.length() > 4 ? oldpid.substring(0, 4) : oldpid;
	}

	/**
	 * 调用工作流接口，返回报价子实例id
	 * @param taskid
	 * @param flag 0 投保 1 快速续保
	 * @return
	 */
	private int noticeWorkflowStartQuote(String taskid,String flag){
		//投保提交调用工作流接口，传递报价公司列表，返回报价公司id，以及对应的报价流程id
		//调用工作流接口
		INSBWorkflowmain mainModel = insbWorkflowmainDao.selectByInstanceId(taskid);
		//根据实例id获取选择的报价信息总表id
		INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
		insbQuotetotalinfo.setTaskid(taskid);
		INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
		if(null == quotetotalinfo){
			return 1;
		}
		//保存投保书失效
		quotetotalinfo.setInsurancebooks("0");
		//保存是否续保标志位  是否续保 0 不是 1是
		quotetotalinfo.setIsrenewal(flag);
		insbQuotetotalinfoDao.updateById(quotetotalinfo);

		//获取报价信息表，报价公司id列表
		INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
		insbQuoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
		List<INSBQuoteinfo> insbQuoteinfos = insbQuoteinfoDao.selectList(insbQuoteinfo);
		if(null == insbQuoteinfos || insbQuoteinfos.size() <= 0){
			return 2;
		}
		Map<String,Object> param = new HashMap<String, Object>();
		List<String> ids = new ArrayList<String>();
		for(INSBQuoteinfo quoteinfo : insbQuoteinfos){
			if (StringUtil.isEmpty(quoteinfo.getWorkflowinstanceid())) {
				ids.add(quoteinfo.getInscomcode());
				//调用规则判断承保限制条件
				param = getPriceParamWayStartSubflow(param, taskid, quoteinfo.getInscomcode());
			} else {
				LogUtil.info(taskid+"中的"+quoteinfo.getInscomcode()+"已有子任务号，不需要再推开始工作流");
			}
		}
		if(null == mainModel){
			return 3;
		}

		if (!ids.isEmpty()) {

			LogUtil.info("noticeWorkflowStartQuote" + taskid + "=====供应商ids=" + ids + ",操作人：" + mainModel.getOperator() + ",0 投保 1 快速续保 标识flag：" + flag);
			String result = WorkFlowUtil.noticeWorkflowStartQuote(mainModel.getOperator(), taskid, param, flag);
			LogUtil.info("noticeWorkflowStartQuote" + taskid + ",result:" + result);

			if (StringUtil.isEmpty(result)) {
				return 4;
			} else {
				JSONObject jsonObj = JSONObject.fromObject(result);
				if (jsonObj.containsKey("message") && "fail".equals(jsonObj.getString("message"))) {
					return 4;
				} else {
					//平台查询是否回调数据
//					INSBLastyearinsureinfo insbLastyearinsureinfo = new INSBLastyearinsureinfo();
//					insbLastyearinsureinfo.setTaskid(taskid);
//					insbLastyearinsureinfo.setSflag("2");
					INSBLastyearinsureinfo lastyearinsureinfo = insbRulequerycarinfoDao.queryLastYearClainInfo(taskid);

					for (INSBQuoteinfo quoteinfo : insbQuoteinfos) {
						String subtaskid = jsonObj.get(quoteinfo.getInscomcode()).toString();
						//防止多次提交修改
						if (StringUtil.isEmpty(quoteinfo.getWorkflowinstanceid())) {
							Map<String, String> map = new HashMap<String, String>();
							map.put("workflowinstanceid", subtaskid);
							map.put("id", quoteinfo.getId());
							appInsuredQuoteDao.updateInsbQuoteinfoById(map);
						} else {
							subtaskid = quoteinfo.getWorkflowinstanceid();
						}

						//新车未上牌  不启动平台查询定时器，直接跑流程
						INSBCarinfo insbCarinfo = new INSBCarinfo();
						insbCarinfo.setTaskid(taskid);
						INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
						LogUtil.info("noticeWorkflowStartQuote" + taskid + "=供应商id=" + quoteinfo.getInscomcode() + "=车牌=" + quoteinfo.getPlatenumber() + "=是否渠道标识==" + quotetotalinfo.getSourceFrom());
						//新车，渠道的不启动定时器
						if ("channel".equals(quotetotalinfo.getSourceFrom()) || "FirstPayLastInsure".equals(quotetotalinfo.getSourceFrom()) ||
								"新车未上牌".equals(carinfo.getCarlicenseno()) || "1".equals(carinfo.getIsNew())) {
							//保单表插入投保日期
							cifHasNodataBackTocm(taskid, subtaskid, quoteinfo.getInscomcode());
						} else {
							//普通平台查询有数据返回，不启动定时器，无返回启动80秒定时器
							if (null == lastyearinsureinfo) {
//							LogUtil.info("noticeWorkflowStartQuote"+taskid+"=供应商id=" +quoteinfo.getInscomcode() +"=子流程subtaskid="+ subtaskid + "=普通平台查询在报价前未返回数据");
								// 跟新平台查询表状态
//							INSBLastyearinsurestatus insbLastyearinsurestatus = new INSBLastyearinsurestatus();
//							insbLastyearinsurestatus.setTaskid(taskid);
//							INSBLastyearinsurestatus lastyearinsurestatus = insbLastyearinsurestatusDao.selectOne(insbLastyearinsurestatus);
//							if (null != lastyearinsurestatus) {
//								// 判断发起平台查询和当前时间差， 80秒超时
//								long times = ModelUtil.twoDatesDifferSeconds(new Date(),lastyearinsurestatus.getCifstarttime());
//								LogUtil.info("noticeWorkflowStartQuote"+taskid+"=平台查询耗时=" + times + "秒"+"当前时间="+new Date());
//								if (times > 80) {
								cifHasNodataBackTocm(taskid, subtaskid, quoteinfo.getInscomcode());
//								}else{
//									long endtime = new Date().getTime() + (80 - times) * 1000;
//									//启动定时器
//									Date startTime = new Date(endtime);
//									//开启任务前清除历史定时任务
//									String jobName = taskid+"#"+subtaskid+"#"+quoteinfo.getInscomcode()+"#cifqueryjob";
//									deleteHistoryJob(jobName);
//									LogUtil.info("noticeWorkflowStartQuote"+taskid+"开始启动平台查询定时器=定时器启动时间="+startTime+"定时器任务jobname="+jobName+",当前时间："+new Date());
//									JobDetail job = JobBuilder.newJob(TaskCifQuerytimeJob.class).withIdentity(jobName).build();
//									SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(jobName).startAt(startTime).build();
//									//加入调度
//									try {
//										sched.scheduleJob(job, trigger);
//									} catch (SchedulerException e) {
//										e.printStackTrace();
//									}
//								}
//							}
							} else {
								LogUtil.info("noticeWorkflowStartQuote" + taskid + "=供应商id=" + quoteinfo.getInscomcode() + "=普通平台查询在报价前有数据返回");
								dateIsRepeatInsured(taskid, subtaskid, quoteinfo.getInscomcode(), lastyearinsureinfo.getSyenddate(),
										lastyearinsureinfo.getJqenddate(), lastyearinsureinfo.getRepeatinsurance(), lastyearinsureinfo.getJqrepeatinsurance(), lastyearinsureinfo.getSupplierid());
							}
						}
					}

					//快速续保，提交投保
					if ("1".equals(flag)) {
						String insureflag = selectInsureProvid(taskid, ids.get(0));
						LogUtil.info("====fast insured==== " + taskid + " == " + insureflag);
					}
				}
			}
		}

		return 0;
	}

	/**
	 * 如果存在，先删除
	 * @param jobName
	 */
	public void deleteHistoryJob(String jobName) {
		JobKey jobKey = new JobKey(jobName);
		try {
			if(sched.deleteJob(jobKey)){
				LogUtil.info("deleteHistoryJob=jobname="+jobName+"已经存在先删除");
			}else{
				LogUtil.info("deleteHistoryJob=jobname="+jobName+"不存在");
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 选择投保
	 * @param taskid 流程实例id
	 * @param inscomcode 保险公司code
	 */
	private String selectInsureProvid(String taskid,String inscomcode){
		INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
		insbQuotetotalinfo.setTaskid(taskid);
		INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
		String agentnum = "";
		if(null != quotetotalinfo){
			agentnum = quotetotalinfo.getAgentnum();
		}
//		//原始保费
//		double totalproductamount = 0;
//		//折后保费
//		double totalpaymentamount = 0;
//		INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
//		insbCarkindprice.setTaskid(taskid);
//		insbCarkindprice.setInscomcode(inscomcode);
//		List<INSBCarkindprice> carkindprices = insbCarkindpriceDao.selectList(insbCarkindprice);
//		for(INSBCarkindprice carkindprice : carkindprices){
//			if((null == carkindprice.getAmountprice() || 0 == carkindprice.getAmountprice()) && null != carkindprice.getDiscountCharge()){
//				totalproductamount += carkindprice.getDiscountCharge();
//				totalpaymentamount += carkindprice.getDiscountCharge();
//			}else{
//				totalproductamount += carkindprice.getAmountprice();
//			}
//		}
		return policySubmit(taskid, inscomcode, agentnum, 0.0, 0.0);
	}

	/**
	 * 与核保提交方法相同
	 * @param processInstanceId
	 * @param inscomcode
	 * @param agentnum
	 * @param totalproductamount
	 * @param totalpaymentamount
	 * @return
	 */
	public String policySubmit(String processInstanceId, String inscomcode,
			String agentnum, double totalproductamount, double totalpaymentamount) {
		// 判断是否过期报价周期和起保时间
//		Date busStartDate = null, strStartDate = null;
		INSBPolicyitem policyitem = new INSBPolicyitem();
		policyitem.setTaskid(processInstanceId);
		policyitem.setInscomcode(inscomcode);
		List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectList(policyitem);
		/*for (int i = 0; i < policyitemList.size(); i++) {
			if("0".equals(policyitemList.get(i).getRisktype())){//商业险保单
				busStartDate = policyitemList.get(i).getStartdate();
			}else if("1".equals(policyitemList.get(i).getRisktype())){//交强险保单
				strStartDate = policyitemList.get(i).getStartdate();
			}
		}*/
		INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
		quotetotalinfo.setTaskid(processInstanceId);
		quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
		INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
		quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
		quoteinfo.setInscomcode(inscomcode);
		quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
		//获取子流程
//		INSBWorkflowsub workflowsub = new INSBWorkflowsub();
//		workflowsub.setMaininstanceid(processInstanceId);
//		workflowsub.setInstanceid(quoteinfo.getWorkflowinstanceid());
//		workflowsub = insbWorkflowsubDao.selectOne(workflowsub);
		//获取选择报价轨迹时间
		/*INSBWorkflowsubtrack subtrack = new INSBWorkflowsubtrack();
		subtrack.setMaininstanceid(processInstanceId);
		subtrack.setInstanceid(quoteinfo.getWorkflowinstanceid());
		subtrack.setTaskcode("14");//选择投保
		subtrack = insbWorkflowsubtrackDao.selectOne(subtrack);//选择投保子流程轨迹信息
		//报价有效期
		INSBProvider atainteger = insbProviderDao.selectById(inscomcode);
		Date quotesuccessTimes = null;
		if(null == atainteger.getQuotationvalidity()){
			quotesuccessTimes = ModelUtil.gatFastPaydate(busStartDate, strStartDate);
		}else{
			quotesuccessTimes = ModelUtil.gatFastPaydateToNow(busStartDate, strStartDate, subtrack.getCreatetime(), atainteger.getQuotationvalidity());
		}

		LogUtil.info("报价有效期taskid"+processInstanceId+"到期时间为="+quotesuccessTimes+"供应商id="+inscomcode);
		if(ModelUtil.daysBetween(quotesuccessTimes, new Date()) > 0){
			return "已超过报价有效期！";
		}else{*/
            Date date = new Date();
			// 报价时历史表数据回写
			// 车辆信息表数据查询
			INSBCarinfohis carinfohis = new INSBCarinfohis();
			carinfohis.setTaskid(processInstanceId);
			carinfohis.setInscomcode(inscomcode);
			carinfohis = insbCarinfohisDao.selectOne(carinfohis);
			INSBCarinfo carinfo = insbCarinfoDao.selectCarinfoByTaskId(processInstanceId);
			String carinfoid = carinfo.getId();
//			String specifydriverid = carinfo.getSpecifydriver();//车辆信息表里指定驾驶人字段值
//			if(carinfohis != null){
//				specifydriverid = carinfohis.getSpecifydriver();
//			}
			// 指定驾驶人中间表信息回写
//			INSBSpecifydriverhis specifydriverhis = new INSBSpecifydriverhis();
//			specifydriverhis.setTaskid(processInstanceId);
//			specifydriverhis.setInscomcode(inscomcode);
//			List<INSBSpecifydriverhis> sdriverhisList = insbSpecifydriverhisDao.selectList(specifydriverhis);
//			if(sdriverhisList!=null && sdriverhisList.size()>0){
//				INSBSpecifydriver specifydriver = new INSBSpecifydriver();
//				specifydriver.setTaskid(processInstanceId);
//				List<INSBSpecifydriver> sdriverList = insbSpecifydriverDao.selectList(specifydriver);
//				for (int i = 0; i < sdriverhisList.size(); i++) {
//					INSBSpecifydriverhis temphis = sdriverhisList.get(i);
//					String spid = "";//回写后的驾驶人中间表id
//					if(sdriverList.size() > i){//此判断保证能用取到驾驶人原表中i坐标的记录
//						INSBSpecifydriver temp = sdriverList.get(i);
//						spid = temp.getId();
//						temp = EntityTransformUtil.specifydriverhis2Specifydriver(temphis);
//						temp.setId(spid);
//						temp.setCarinfoid(carinfoid);
//						insbSpecifydriverDao.updateById(temp);
//					}else{
//						INSBSpecifydriver temp = EntityTransformUtil.specifydriverhis2Specifydriver(temphis);
//						temp.setCarinfoid(carinfoid);
//						insbSpecifydriverDao.insert(temp);
//						spid = temp.getId();
//					}
//					if(specifydriverid.equals(temphis.getId())){
//						specifydriverid = spid;//如果车辆信息中指定的驾驶人为驾驶人中间历史表的id，需要跟换为回写后驾驶人中间表的id
//					}
//				}
//			}
//			// 车辆信息表数据回写
//			if(carinfohis != null){
//				carinfo = EntityTransformUtil.carinfohis2Carinfo(carinfohis);
//				carinfo.setId(carinfoid);
//				carinfo.setSpecifydriver(specifydriverid);//把车辆信息中指定驾驶人字段更新为回写后驾驶人中间表的id
//				insbCarinfoDao.updateById(carinfo);
//			}
			// 车型信息数据回写
			INSBCarmodelinfohis carmodelinfohis = new INSBCarmodelinfohis();
			carmodelinfohis.setCarinfoid(carinfoid);
			carmodelinfohis.setInscomcode(inscomcode);
			carmodelinfohis = insbCarmodelinfohisDao.selectOne(carmodelinfohis);
//			if(carmodelinfohis != null){
//				INSBCarmodelinfo carmodelinfo = new INSBCarmodelinfo();
//				carmodelinfo.setCarinfoid(carinfoid);
//				carmodelinfo = insbCarmodelinfoDao.selectOne(carmodelinfo);
//				String carmodelinfoid = carmodelinfo.getId();
//				carmodelinfo = EntityTransformUtil.carmodelinfohis2Carmodelinfo(carmodelinfohis);
//				carmodelinfo.setId(carmodelinfoid);
//				insbCarmodelinfoDao.updateById(carmodelinfo);
//			}
			// 被保人信息回写
			INSBInsuredhis insuredhis = new INSBInsuredhis();
			insuredhis.setTaskid(processInstanceId);
			insuredhis.setInscomcode(inscomcode);
			insuredhis = insbInsuredhisDao.selectOne(insuredhis);
//			if(insuredhis != null){
//				INSBInsured insured = new INSBInsured();
//				insured.setTaskid(processInstanceId);
//				insured = insbInsuredDao.selectOne(insured);
//				String insuredid = insured.getId();
//				insured = EntityTransformUtil.insuredhis2Insured(insuredhis);
//				insured.setId(insuredid);
//				insbInsuredDao.updateById(insured);
//			}
			// 投保人信息回写
			INSBApplicanthis applicanthis = new INSBApplicanthis();
			applicanthis.setTaskid(processInstanceId);
			applicanthis.setInscomcode(inscomcode);
			applicanthis = insbApplicanthisDao.selectOne(applicanthis);
//			if(applicanthis != null){
//				INSBApplicant applicant = new INSBApplicant();
//				applicant.setTaskid(processInstanceId);
//				applicant = insbApplicantDao.selectOne(applicant);
//				String applicantid = applicant.getId();
//				applicant = EntityTransformUtil.applicanthis2Applicant(applicanthis);
//				applicant.setId(applicantid);
//				insbApplicantDao.updateById(applicant);
//			}
			// 权益索赔人信息回写
			INSBLegalrightclaimhis legalrightclaimhis = new INSBLegalrightclaimhis();
			legalrightclaimhis.setTaskid(processInstanceId);
			legalrightclaimhis.setInscomcode(inscomcode);
			legalrightclaimhis = insbLegalrightclaimhisDao.selectOne(legalrightclaimhis);
//			if(legalrightclaimhis != null){
//				INSBLegalrightclaim legalrightclaim = new INSBLegalrightclaim();
//				legalrightclaim.setTaskid(processInstanceId);
//				legalrightclaim = insbLegalrightclaimDao.selectOne(legalrightclaim);
//				String legalrightclaimid = legalrightclaim.getId();
//				legalrightclaim = EntityTransformUtil.legalrightclaimhis2Legalrightclaim(legalrightclaimhis);
//				legalrightclaim.setId(legalrightclaimid);
//				insbLegalrightclaimDao.updateById(legalrightclaim);
//			}
			// 联系人信息回写
			INSBRelationpersonhis relationpersonhis = new INSBRelationpersonhis();
			relationpersonhis.setTaskid(processInstanceId);
			relationpersonhis.setInscomcode(inscomcode);
			relationpersonhis = insbRelationpersonhisDao.selectOne(relationpersonhis);
//			if(relationpersonhis != null){
//				INSBRelationperson relationperson = new INSBRelationperson();
//				relationperson.setTaskid(processInstanceId);
//				relationperson = insbRelationpersonDao.selectOne(relationperson);
//				String relationpersonid = relationperson.getId();
//				relationperson = EntityTransformUtil.relationpersonhis2Relationperson(relationpersonhis);
//				relationperson.setId(relationpersonid);
//				insbRelationpersonDao.updateById(relationperson);
//			}
			// 生成订单
			INSBAgent agent = insbAgentDao.selectByJobnum(agentnum);
			/*INSBProvider provider = new INSBProvider();
			provider.setPrvcode(inscomcode);
			provider = insbProviderDao.selectOne(provider);*/
			//根据协议查询出单网点机构编码
//			INSBAgreement agreement = new INSBAgreement();
//			agreement.setProviderid(inscomcode);
//			agreement = insbAgreementDao.selectOne(agreement);
//			INSCDept dept = inscDeptDao.selectById(agreement.getDeptid());
			INSBOrder order = new INSBOrder();
			order.setOperator(agent.getName());//操作员
			order.setCreatetime(new Date());//创建时间
			order.setTaskid(processInstanceId);//流程实例id
//			UUID uuid = UUID.randomUUID();
//			order.setOrderno(uuid.toString());//订单号
			order.setOrderno(GenerateSequenceUtil.generateSequenceNo(processInstanceId,quoteinfo.getWorkflowinstanceid()));//订单号
//			order.setOrderstatus("1");//订单状态todo
			order.setOrderstatus("0");//0-待投保
			order.setPaymentstatus("0");//支付状态todo
			order.setDeliverystatus("0");//配送状态todo
			order.setBuyway(quoteinfo.getBuybusitype());//销售渠道(01传统02网销03电销)todo
			order.setAgentcode(agentnum);//代理人编码
			order.setAgentname(agent.getName());//代理人姓名
			order.setInputusercode(agent.getAgentcode());//录单人
			order.setTotalproductamount(totalproductamount);//订单标价总金额
			order.setTotalpaymentamount(totalpaymentamount);//实付金额
			order.setTotalpromotionamount(totalproductamount-totalpaymentamount);//优惠金额
//			order.setDeptcode(dept.getComcode());//出单网点
			order.setDeptcode(quoteinfo.getDeptcode());//出单网点
			order.setCurrency("RMB");
			order.setPrvid(inscomcode);
			insbOrderDao.insert(order);
			INSBPolicyitem policyBus = null;//商业险保单接收变量
			INSBPolicyitem policyStr = null;//交强险保单接收变量
			// 更新保单表中的orderid字段
			for (int i = 0; i < policyitemList.size(); i++) {
				if("0".equals(policyitemList.get(i).getRisktype())){
					policyBus = policyitemList.get(i);
				}else if("1".equals(policyitemList.get(i).getRisktype())){
					policyStr = policyitemList.get(i);
				}
//				if(policyitemList.get(i).getOrderid()==null || "".equals(policyitemList.get(i).getOrderid())){
//					insbPolicyitemDao.deleteById(policyitemList.get(i).getId());
//				}
			}
			INSBPerson tempPerson = null;
			if(applicanthis!=null){
				tempPerson = insbPersonDao.selectById(applicanthis.getPersonid());
				if(tempPerson!=null){
					if(policyBus!=null){
						policyBus.setApplicantname(tempPerson.getName());
					}
					if(policyStr!=null){
						policyStr.setApplicantname(tempPerson.getName());
					}
				}
			}
			if(insuredhis!=null){
				tempPerson = insbPersonDao.selectById(insuredhis.getPersonid());
				if(tempPerson!=null){
					if(policyBus!=null){
						policyBus.setInsuredname(tempPerson.getName());
					}
					if(policyStr!=null){
						policyStr.setInsuredname(tempPerson.getName());
					}
				}
			}
			INSBCarowneinfo ownerinfo = insbCarowneinfoDao.selectByTaskId(processInstanceId);
			if(ownerinfo!=null){
				tempPerson = insbPersonDao.selectById(ownerinfo.getPersonid());
				if(tempPerson!=null){
					if(policyBus!=null){
						policyBus.setCarownername(tempPerson.getName());
					}
					if(policyStr!=null){
						policyStr.setCarownername(tempPerson.getName());
					}
				}
			}
			if(carmodelinfohis!=null){
				if(policyBus!=null){
					policyBus.setStandardfullname(carmodelinfohis.getStandardfullname());
				}
				if(policyStr!=null){
					policyStr.setStandardfullname(carmodelinfohis.getStandardfullname());
				}
			}
			if(policyBus!=null){
//				policyBus.setId(null);
//				policyBus.setModifytime(null);
				policyBus.setModifytime(date);
//				policyBus.setCreatetime(date);
//				policyBus.setInscomcode(inscomcode);
				policyBus.setOrderid(order.getId());
				//不确定置空的字段
//				policyBus.setTotalepremium(null);//
//				policyBus.setPremium(null);//
//				policyBus.setDiscountCharge(null);//折后保费
//				policyBus.setDiscountRate(null);//折扣率
//				policyBus.setAmount(null);//保额
//				policyBus.setProposalformno(null);//投保单号
//				policyBus.setPolicyno(null);//保单号
//				policyBus.setPolicystatus(null);//保单状态
//				policyBus.setPaynum(null);//支付号
//				policyBus.setCheckcode(null);//校验码
//				insbPolicyitemDao.insert(policyBus);
				insbPolicyitemDao.updateById(policyBus);
			}
			if(policyStr!=null){
//				policyStr.setId(null);
//				policyStr.setModifytime(null);
				policyStr.setModifytime(date);
//				policyStr.setCreatetime(date);
//				policyStr.setInscomcode(inscomcode);
				policyStr.setOrderid(order.getId());
				//不确定置空的字段
//				policyStr.setTotalepremium(null);//
//				policyStr.setPremium(null);//
//				policyStr.setDiscountCharge(null);//折后保费
//				policyStr.setDiscountRate(null);//折扣率
//				policyStr.setAmount(null);//保额
//				policyStr.setProposalformno(null);//投保单号
//				policyStr.setPolicyno(null);//保单号
//				policyStr.setPolicystatus(null);//保单状态
//				policyStr.setPaynum(null);//支付号
//				policyStr.setCheckcode(null);//校验码
//				insbPolicyitemDao.insert(policyStr);
				insbPolicyitemDao.updateById(policyStr);
			}
		/*}*/
		return "success";
	}

	/**
	 * task 105  CM后台，增加“人工报价成功”标记给前端使用，供前端锁死
	 * 人工报价成功的任务，前端除锁定投保信息不能进行修改外，增加锁定保险配置也无法进行修改。
	 * @param instanceId
	 * @return
	 */
	private boolean lockConf(String instanceId, String inscomcode) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("inscomcode", inscomcode);
		map.put("taskid", instanceId);
		//map.put("taskcode", 8);
		map.put("taskstate", "Completed");
		List<INSBWorkflowsubtrack> l = this.insbWorkflowsubtrackDao.selectLockConf(map);
		if(l != null && !l.isEmpty()){
			// http://pm.baoxian.in/zentao/task-view-1768.html
			// 前端在校验是否可以修改保险配置时，需要根据最后一次报价结果的报价能力来判断。
			// 如果是通过精灵、EDI、规则报价能力获得的报价结果，则可以修改保险配置
			INSBWorkflowsubtrack workflowsubtrack = l.get(0);
			if (workflowsubtrack == null || StringUtil.isEmpty(workflowsubtrack.getTaskcode()) || !"8".equals(workflowsubtrack.getTaskcode())) {
				return false;
			}
			//报价成功
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("inscomcode", inscomcode);
			map2.put("taskid", instanceId);
			map2.put("taskcode", 14);
			List<INSBWorkflowsubtrack> l1 = this.insbWorkflowsubtrackDao.selectLockConf(map2);
			if(l1 != null && !l1.isEmpty()){
				return true;
			}
		}
		return false;
	}

	@Override
	public CommonModel updateInduredConfig(InsuredOneConfigModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if(StringUtil.isEmpty(taskid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			String pid = model.getPid();
			if(StringUtil.isEmpty(pid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("供应商id不能为空");
				return commonModel;
			}
			//bug 5469【生产环境】[福建平台]前端在9月30号提了单子，前端承保规则限制之后，在10月17号关了人保泉州的供应商协议，但是10.28号还是能走EDI报价（未下掉人保的泉州的EDI能力）
			INSBAgreement agreement = new INSBAgreement();
			agreement.setProviderid(pid);
			agreement.setAgreementstatus("1");//已生效
			List<INSBAgreement> agreementList = insbAgreementDao.selectList(agreement);
			if (agreementList == null || agreementList.isEmpty()) {
				commonModel.setStatus("fail");
				commonModel.setMessage("该保险公司已下架，请选择其他公司投保。");
				return commonModel;
			}
			if(lockConf(taskid, pid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("人工报价成功,不能修改保险配置");
				return commonModel;
			}

			INSBCarinfo insbCarinfo = new INSBCarinfo();
			insbCarinfo.setTaskid(taskid);
			INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
			if (null == carinfo) {
				commonModel.setStatus("fail");
				commonModel.setMessage("车辆信息不存在");
				return commonModel;
			}

			//更新属性:保险配置是否与上年一致
			insbCarinfoDao.updateInsureconfigsameaslastyear(taskid, model.getSameaslastyear());
			insbCarinfohisDao.updateInsureconfigsameaslastyear(taskid, pid, model.getSameaslastyear());

			//修改投保车价
			if(!StringUtil.isEmpty(model.getPolicycarprice())){
				INSBCarmodelinfo insbCarmodelinfo = new INSBCarmodelinfo();
				insbCarmodelinfo.setCarinfoid(carinfo.getId());
				INSBCarmodelinfo carmodelinfo = insbCarmodelinfoDao.selectOne(insbCarmodelinfo);
				if (null == carmodelinfo) {
					commonModel.setStatus("fail");
					commonModel.setMessage("车型信息不存在");
					return commonModel;
				}
				//调整车价有值改为自定义
				carmodelinfo.setCarprice("2");
				carmodelinfo.setPolicycarprice(model.getPolicycarprice());
				insbCarmodelinfoDao.updateById(carmodelinfo);
			}
			String operator = model.getOperator();
			if (operator == null) {
				INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
				insbQuotetotalinfo.setTaskid(taskid);
				insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
				operator = insbQuotetotalinfo.getOperator();
			}
			//保存保险配置
			LogUtil.info("修改保险配置updateInduredConfig="+taskid + "===被修改供应商id="+pid+"===前端传的操作人="+model.getOperator()+"===后台取的操作人==="+operator);
			//修改前投保类型
			int updateBefore = commonQuoteinfoService.getInsureConfigType(taskid, pid);
			if(updateBefore == 4) updateBefore = 2; //4相当于单交
			
			updateInsuredConfig(taskid, model.getOperator(), model.getBusinessRisks(), model.getStrongRisk(), pid, model.getPlankey(), model.getSameaslastyear());
			
			//修改后的投保类型
			int updateAfter = commonQuoteinfoService.getInsureConfigType(taskid, pid);
			if(updateAfter == 4) updateAfter = 2; //4相当于单交
			
			//1-单商,2-单交,3-混保,4:交强险+车船税. 投保类型变更：单交改混保，单交改单商，单商改单交，单商改混保, 点重新报价后重新平台查询
			LogUtil.info("(前端请求)修改保险配置, 投保类型(1-单商,2-单交,3-混保)由 '" + updateBefore + "' 转为 '" + updateAfter+ "', taskid=" + taskid);
			if(commonQuoteinfoService.isDoPTQuery(updateBefore, updateAfter)){
				CMRedisClient.getInstance().set(Constants.CM_GLOBAL, taskid + "startedBakQuery", "0", 20 * 60);
			}
			
			//特殊险别处理
			specialOneInsureconfDeal(taskid, model, model.getOperator(), pid);

            //保存备注信息 bug2668注意需要转人工的备注
            if(!StringUtil.isEmpty(model.getRemark())){
                saveRemarkInTab(taskid, model.getRemark(), operator, model.getRemarkcode());
            }

			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	private void specialOneInsureconfDeal(String taskid, InsuredOneConfigModel model,String operator,String pid) {
		//先删除
		INSBSpecialkindconfig insbSpecialkindconfig = new INSBSpecialkindconfig();
		insbSpecialkindconfig.setTaskid(taskid);
		insbSpecialkindconfig.setInscomcode(pid);
		List<INSBSpecialkindconfig> specialkindconfigs = insbSpecialkindconfigDao.selectList(insbSpecialkindconfig);
		if(null != specialkindconfigs && specialkindconfigs.size() > 0){
			for(INSBSpecialkindconfig specialkindconfig : specialkindconfigs){
				insbSpecialkindconfigDao.deleteById(specialkindconfig.getId());
			}
		}
		List<INSBSpecialkindconfig> insbSpecialkindconfigs = new ArrayList<INSBSpecialkindconfig>();
		List<BusinessRisks> businessRisks = model.getBusinessRisks();
		// 商业险
		if (null != businessRisks && businessRisks.size() > 0) {
			for(BusinessRisks risks : businessRisks){
				String value = getValueFromCode(risks.getCode());
				if(!StringUtil.isEmpty(value)){
					List<NewEquipmentInsBean> beans = model.getEquipmentInsBeans();
					if("04".equals(value) && null != beans && beans.size() > 0){
						for(NewEquipmentInsBean bean : beans){
							INSBSpecialkindconfig specialkindconfig = new INSBSpecialkindconfig();
							specialkindconfig.setCreatetime(new Date());
							specialkindconfig.setOperator(operator);
							specialkindconfig.setTaskid(taskid);
							specialkindconfig.setTypecode("04");
							specialkindconfig.setKindcode(risks.getCode());
							specialkindconfig.setCodekey(bean.getKey());
							specialkindconfig.setCodevalue(bean.getValue());
							specialkindconfig.setInscomcode(pid);
							insbSpecialkindconfigs.add(specialkindconfig);
						}
					}
					if("05".equals(value) && !StringUtil.isEmpty(model.getCompensationdays())){
						INSBSpecialkindconfig specialkindconfig = new INSBSpecialkindconfig();
						specialkindconfig.setCreatetime(new Date());
						specialkindconfig.setOperator(operator);
						specialkindconfig.setTaskid(taskid);
						specialkindconfig.setTypecode("05");
						specialkindconfig.setKindcode(risks.getCode());
						specialkindconfig.setCodekey("");
						specialkindconfig.setCodevalue(model.getCompensationdays());
						specialkindconfig.setInscomcode(pid);
						insbSpecialkindconfigs.add(specialkindconfig);
					}
				}
			}
		}
		//保存
		if(insbSpecialkindconfigs.size() > 0){
			insbSpecialkindconfigDao.insertInBatch(insbSpecialkindconfigs);
		}
	}

	@Override
	public CommonModel saveAddQuoteProvider(ChoiceProviderIdsModel model) {
		LogUtil.info("在已有的报价列表增加报价公司"+model.getProcessinstanceid()+"新增的数据="+model.getParamsList());
		return saveProviderList(model,1);
	}

	/**
	 * 保存保险配置信息
	 * @param taskid 实例id
	 * @param operator 操作人
	 * @param businessRisks 商业险
	 * @param strongRisks 交强险
	 */
	private void updateInsuredConfig(String taskid, String operator, List<BusinessRisks> businessRisks, List<StrongRisk> strongRisks, 
			String pid, String plankey, String sameaslastyear) {
		operator = null == operator ? "" : operator;

		// 删除当前报价公司险别配置
		INSBCarconfig carconfig = new INSBCarconfig();
		carconfig.setTaskid(taskid);
		List<INSBCarconfig> carconfigs = insbCarconfigDao.selectList(carconfig);
		if(null != carconfigs && carconfigs.size() > 0){
			LogUtil.info("更新保险配置删除已有的险别总表insbCarconfig,taskid="+taskid+"操作人="+operator);
			for(INSBCarconfig insbCarconfig : carconfigs){
				insbCarconfigDao.deleteById(insbCarconfig.getId());
			}
		}
		
		INSBCarkindprice insureCarkindprice = new INSBCarkindprice();
		insureCarkindprice.setTaskid(taskid);
		insureCarkindprice.setInscomcode(pid);
		List<INSBCarkindprice> carkindpricesList = insbCarkindpriceDao.selectList(insureCarkindprice);
		if (null != carkindpricesList && carkindpricesList.size() > 0) {
			LogUtil.info("更新保险配置删除已有的险别分表insbCarkindprice,taskid="+taskid+"操作人="+operator);
			for (INSBCarkindprice insbCarkindprice : carkindpricesList) {
				insbCarkindpriceDao.deleteById(insbCarkindprice.getId());
			}
		}
		
		List<INSBCarconfig> insbCarconfigs = new ArrayList<INSBCarconfig>();
		List<INSBCarkindprice> insbCarkindprices = new ArrayList<INSBCarkindprice>();

		// 商业险
		if (null != businessRisks && businessRisks.size() > 0) {
			for (BusinessRisks businessRisk : businessRisks) {
				INSBCarconfig insbCarconfig = new INSBCarconfig();
				INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
				insbCarconfig.setOperator(operator);
				insbCarconfig.setCreatetime(new Date());
				insbCarconfig.setTaskid(taskid);

				// 商业险
				insbCarconfig.setInskindtype("0");
				insbCarkindprice.setInskindtype("0");
				// 险别不计免赔 0选中 1 没选中
				if ("0".equals(businessRisk.getFlag())) {
					insbCarconfig.setNotdeductible("1");
					insbCarkindprice.setNotdeductible("1");
					insbCarkindprice.setBjmpCode("Ncf" + businessRisk.getCode());

					/**
					 * hxx 保存附加险
					 */
					INSBCarkindprice ncfInsbCarkindprice = new INSBCarkindprice();
					ncfInsbCarkindprice.setAmount(0.0);
					ncfInsbCarkindprice.setInskindcode("Ncf"+ businessRisk.getCode());
					ncfInsbCarkindprice.setInskindtype("1");// 附加险
					ncfInsbCarkindprice.setOperator(operator);
					ncfInsbCarkindprice.setCreatetime(new Date());
					ncfInsbCarkindprice.setTaskid(taskid);
					ncfInsbCarkindprice.setPreriskkind(businessRisk.getCode());
					ncfInsbCarkindprice.setNotdeductible("0");
					ncfInsbCarkindprice.setInscomcode(pid);
					ncfInsbCarkindprice.setPlankey(plankey);

					//保险险别选择表 插入不计免赔
					INSBCarconfig ncfInsbCarconfig = new INSBCarconfig();
					ncfInsbCarconfig.setAmount("0.0");
					ncfInsbCarconfig.setInskindcode("Ncf"+businessRisk.getCode());
					ncfInsbCarconfig.setInskindtype("1");//附加险
					ncfInsbCarconfig.setOperator(operator);
					ncfInsbCarconfig.setCreatetime(new Date());
					ncfInsbCarconfig.setTaskid(taskid);
					ncfInsbCarconfig.setPreriskkind(businessRisk.getCode());
					ncfInsbCarconfig.setNotdeductible("0");
					ncfInsbCarconfig.setPlankey(plankey);

					INSBRiskkindconfig vo = new INSBRiskkindconfig();
					vo.setRiskkindcode("Ncf" + businessRisk.getCode());
					INSBRiskkindconfig nfcRIkInsbRiskkindconfig = insbRiskkindconfigDao.selectOne(vo);
					if (nfcRIkInsbRiskkindconfig != null) {
						ncfInsbCarkindprice.setRiskname(nfcRIkInsbRiskkindconfig.getRiskkindname());
						insbCarkindprices.add(ncfInsbCarkindprice);
						insbCarconfigs.add(ncfInsbCarconfig);
					}

				} else {
					insbCarconfig.setNotdeductible("0");
					insbCarkindprice.setNotdeductible("0");
				}
				insbCarconfig.setInskindcode(businessRisk.getCode());
				// 保额
				if (StringUtil.isNotEmpty(businessRisk.getCoverage())&&ModelUtil.isNumeric(businessRisk.getCoverage())) {
					insbCarconfig.setAmount(businessRisk.getCoverage());
					insbCarkindprice.setAmount(Double.parseDouble(businessRisk.getCoverage()));
				} else {
					insbCarconfig.setAmount("0");
					insbCarkindprice.setAmount(0d);
				}
				// 单独处理 车辆损失险
				if (special.contains(businessRisk.getCode())) {
					INSBCarinfo insbCarinfo = new INSBCarinfo();
					insbCarinfo.setTaskid(taskid);
					INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
					if (null != carinfo) {
                        Double price = null;

                        INSBCarmodelinfohis carmodelinfohis = new INSBCarmodelinfohis();
                        carmodelinfohis.setCarinfoid(carinfo.getId());
                        carmodelinfohis.setInscomcode(pid);
                        List<INSBCarmodelinfohis> carmodelinfohisList = insbCarmodelinfohisDao.selectList(carmodelinfohis);
                        if (carmodelinfohisList != null && carmodelinfohisList.size() > 0) {
                            carmodelinfohis = carmodelinfohisList.get(0);
                            if("2".equals(carmodelinfohis.getCarprice())) {
                                price = carmodelinfohis.getPolicycarprice();
                            } else {
                                price = carmodelinfohis.getPrice();
                            }
                        }
                        if (price == null) {
                            INSBCarmodelinfo insbCarmodelinfo = new INSBCarmodelinfo();
                            insbCarmodelinfo.setCarinfoid(carinfo.getId());
                            INSBCarmodelinfo carmodelinfo = insbCarmodelinfoDao.selectOne(insbCarmodelinfo);
                            if (null != carmodelinfo) {
                                //20160126 如果自定义改为自定义车价
                                if ("2".equals(carmodelinfo.getCarprice())) {
                                    price = carmodelinfo.getPolicycarprice();
                                } else {
                                    price = carmodelinfo.getPrice();
                                }
                            }
                        }
                        if (price != null) {
                        	insbCarconfig.setAmount(price +"");
                            insbCarkindprice.setAmount(price);
                        }
					}
				}
				// 保存险别要素已选项
				List<SelectOption> list = new ArrayList<SelectOption>();
				SelectOption selectOption = new SelectOption();
				RisksData risksData = new RisksData();
				risksData.setKEY(businessRisk.getSelectedOption());
				risksData.setVALUE(businessRisk.getCoverage());
				risksData.setUNIT(businessRisk.getUnit());
				// 是否是玻璃 01不是 02是
				selectOption.setTYPE(businessRisk.getType());
				selectOption.setVALUE(risksData);
				list.add(selectOption);
				JSONArray jsonObject = JSONArray.fromObject(list);
				insbCarconfig.setSelecteditem(jsonObject.toString());
				insbCarkindprice.setSelecteditem(jsonObject.toString());
				// 查询基础表
				INSBRiskkindconfig rkc = new INSBRiskkindconfig();
				rkc.setRiskkindcode(businessRisk.getCode());
				INSBRiskkindconfig vo = insbRiskkindconfigDao.selectOne(rkc);
				if (null != vo) {
					insbCarconfig.setPreriskkind(vo.getPrekindcode());
					insbCarkindprice.setPreriskkind(vo.getPrekindcode());
				}
				insbCarconfig.setPlankey(plankey);
				insbCarconfig.setNoti(pid);//放到备注中便于查数据
				insbCarconfigs.add(insbCarconfig);
				
				insbCarkindprice.setOperator(operator);
				insbCarkindprice.setCreatetime(new Date());
				insbCarkindprice.setTaskid(taskid);
				insbCarkindprice.setInskindcode(businessRisk.getCode());
				insbCarkindprice.setRiskname(businessRisk.getName());
				insbCarkindprice.setInscomcode(pid);
				insbCarkindprice.setPlankey(plankey);
				insbCarkindprices.add(insbCarkindprice);
			}
			saveOrUpdateOnePolocy(taskid,pid,"0");
		}else if (!"1".equals(sameaslastyear)){
			updateOnePolicyItem(taskid, pid, "0");
		}

		// 交强险
		if (null != strongRisks && strongRisks.size() > 0) {
			for (StrongRisk strongRisk : strongRisks) {
				INSBRiskkindconfig vo = new INSBRiskkindconfig();
				vo.setRiskkindcode(strongRisk.getCode());
				INSBRiskkindconfig inscRiskkindconfig = insbRiskkindconfigDao.selectOne(vo);
				if (inscRiskkindconfig == null) {
					continue;
				}
				INSBCarconfig insbCarconfig = new INSBCarconfig();
				insbCarconfig.setOperator(operator);
				insbCarconfig.setCreatetime(new Date());
				insbCarconfig.setTaskid(taskid);
				
				INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
				
				// 交强险
				insbCarconfig.setInskindtype(inscRiskkindconfig.getType());
				insbCarkindprice.setInskindtype(inscRiskkindconfig.getType());
				// 险别不计免赔 没选中
				insbCarconfig.setNotdeductible("0");
				insbCarkindprice.setNotdeductible("0");
				insbCarconfig.setInskindcode(strongRisk.getCode());
				// 保额
				insbCarconfig.setAmount("0");
				insbCarkindprice.setAmount(0d);
				// 险别要素为空
				insbCarconfig.setSelecteditem(strongRisk.getSelected());
				insbCarkindprice.setSelecteditem(strongRisk.getSelected());
				
				insbCarconfig.setPreriskkind(inscRiskkindconfig.getPrekindcode());
				insbCarconfig.setPlankey(plankey);
				insbCarconfig.setNoti(pid);//放到备注中便于查数据
				insbCarconfigs.add(insbCarconfig);
				
				insbCarkindprice.setOperator(operator);
				insbCarkindprice.setCreatetime(new Date());
				insbCarkindprice.setTaskid(taskid);
				insbCarkindprice.setInskindcode(strongRisk.getCode());
				insbCarkindprice.setPreriskkind(inscRiskkindconfig.getPrekindcode());
				insbCarkindprice.setRiskname(strongRisk.getName());
				insbCarkindprice.setInscomcode(pid);
				insbCarkindprice.setPlankey(plankey);
				insbCarkindprices.add(insbCarkindprice);
				//有交强险的时候存保单表
				if("VehicleCompulsoryIns".equals(strongRisk.getCode().trim())){
					//更新保单表交强险 1
					saveOrUpdateOnePolocy(taskid,pid,"1");
				}
			}
		}else{
			deleteOnePolicy(taskid, pid, "1");
		}

		// 保险险别选择表插入基本配置
		insbCarconfigDao.insertInBatch(insbCarconfigs);
		// 保险公司险别报价表
		insbCarkindpriceDao.insertInBatch(insbCarkindprices);
	}

	/**
	 * 保险配置修改时调用
	 */
	private void updateOnePolicyItem(String taskid, String pid, String type){
		INSBPolicyitem insbPolicyitemtem = new INSBPolicyitem();
		insbPolicyitemtem.setTaskid(taskid);
		insbPolicyitemtem.setInscomcode(pid);
		List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(insbPolicyitemtem);
		if(null != insbPolicyitems && insbPolicyitems.size() > 0){
			if(insbPolicyitems.size() == 1){
				INSBPolicyitem insbPolicyitem = insbPolicyitems.get(0);
				insbPolicyitem.setTaskid(taskid);
				insbPolicyitem.setRisktype("1");
				insbPolicyitem.setInscomcode(pid);
				// 获取投保时间,交强险时间
				Date startdate = queryInsureStartDate(taskid, "1");
				insbPolicyitem.setStartdate(startdate);
				// 开始时间   加一年减一天
				insbPolicyitem.setEnddate(ModelUtil.nowDateMinusOneDay(ModelUtil.nowDateAddOneYear(startdate, 1)));
				LogUtil.info("保单表更新数据"+taskid+" updateOnePolicyItem 修改的保单表id="+insbPolicyitem.getId()+" 商业险交强险标识risktype="+insbPolicyitem.getRisktype());
				insbPolicyitemDao.updateById(insbPolicyitem);
			}else{
				INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
				insbPolicyitem.setTaskid(taskid);
				insbPolicyitem.setRisktype(type);
				insbPolicyitem.setInscomcode(pid);
				INSBPolicyitem policyitem = insbPolicyitemDao.selectOne(insbPolicyitem);
				if(null != policyitem){
					LogUtil.info("保单表删数据"+taskid+" updateOnePolicyItem 删除的保单表id="+policyitem.getId()+" 商业险交强险标识risktype="+policyitem.getRisktype());
					insbPolicyitemDao.deleteById(policyitem.getId());
				}
			}
		}
	}

	private void saveOrUpdateOnePolocy(String taskid, String pid, String type) {
		// 查询保单信息(只会有一条)
		INSBPolicyitem mypolicyitem = new INSBPolicyitem();
		mypolicyitem.setTaskid(taskid);
		mypolicyitem.setInscomcode(pid);
		mypolicyitem.setRisktype(type);
		INSBPolicyitem oneinsbPolicyitem = insbPolicyitemDao.selectOne(mypolicyitem);
		//插入新的数据
		if(null == oneinsbPolicyitem){
			INSBPolicyitem onepolicyitem = new INSBPolicyitem();
			onepolicyitem.setTaskid(taskid);
			onepolicyitem.setInscomcode(pid);
			INSBPolicyitem insbPolicyitem = insbPolicyitemDao.selectOne(onepolicyitem);//数据复制
			if(insbPolicyitem != null){
				INSBPolicyitem policyitem = new INSBPolicyitem();
				policyitem.setOperator(insbPolicyitem.getAgentname());
				policyitem.setCreatetime(new Date());
				policyitem.setTaskid(taskid);
				policyitem.setCarownerid(insbPolicyitem.getCarownerid());
				policyitem.setCarownername(insbPolicyitem.getCarownername());
				policyitem.setAgentname(insbPolicyitem.getAgentname());
				policyitem.setCarinfoid(insbPolicyitem.getCarinfoid());
				// 险种类型
				policyitem.setRisktype(type);
				policyitem.setAgentnum(insbPolicyitem.getAgentnum());
				// policyitem.setNoti(remarks);
				// 获取投保时间
				Date startdate = queryInsureStartDate(taskid, type);
				policyitem.setStartdate(startdate);
				// 开始时间   加一年减一天
				policyitem.setEnddate(ModelUtil.nowDateMinusOneDay(ModelUtil.nowDateAddOneYear(startdate, 1)));

				policyitem.setApplicantid(insbPolicyitem.getApplicantid());
				policyitem.setApplicantname(insbPolicyitem.getApplicantname());
				policyitem.setInsuredid(insbPolicyitem.getInsuredid());
				policyitem.setInsuredname(insbPolicyitem.getInsuredname());
				// 未生效
				policyitem.setPolicystatus("5");
				// 关联报价公司
				policyitem.setInscomcode(pid);
				insbPolicyitemDao.insert(policyitem);
			}
//			else{
//				//最初买单商，后来修改单交
//				INSBPolicyitem policyitem = new INSBPolicyitem();
//				policyitem.setTaskid(taskid);
//				List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(policyitem);//数据复制
//				if(null != insbPolicyitems ){
//
//				}
//			}
		}  else if (null != oneinsbPolicyitem
				&& oneinsbPolicyitem.getEnddate() ==null
				&& oneinsbPolicyitem.getStartdate() ==null) {
			Date startdate = queryInsureStartDate(taskid, type);
			oneinsbPolicyitem.setStartdate(startdate);
			// 开始时间   加一年减一天
			oneinsbPolicyitem.setEnddate(ModelUtil.nowDateMinusOneDay(ModelUtil.nowDateAddOneYear(startdate, 1)));
			insbPolicyitemDao.updateById(oneinsbPolicyitem);
		}
	}

	/**
	 *
	 * @param taskid
	 * @param type 0 商业险   1 交强险
	 * @return
	 */
	private Date queryInsureStartDate(String taskid,String type){
		Map<String, Object>  map = judgeInsuredDate(90, taskid);
		String sdate = "";
		if(!map.isEmpty()){
			LogUtil.info("queryInsureStartDate"+ taskid +"=类型="+ type +"事件参数="+map);
			if("0".equals(type)){
				sdate = map.get("syenddate").toString();
			}else{
				sdate = map.get("jqenddate").toString();
			}
		}else{
			LogUtil.info("queryInsureStartDate"+ taskid +"=类型="+ type);
		}
		return StringUtil.isEmpty(sdate)?ModelUtil.nowDateAddOneDay(new Date()):ModelUtil.conbertStringToNyrDate(sdate);
	}

	@Override
	public CommonModel workflowStartQuote(WorkflowStartQuoteModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if(StringUtil.isEmpty(taskid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			//权限包
			Map<String,Object> permissionMap = appPermissionService.checkPermission("", taskid, "insured");
			if ((int)permissionMap.get("status") == 2 || (int)permissionMap.get("status") == -1) {
				commonModel.setStatus(CommonModel.STATUS_FAIL);
				commonModel.setMessage((String) permissionMap.get("message"));
				return commonModel;
			}


			//删除规则校验没通过的报价公司
			List<String> list = model.getPids();
			if(null != list && list.size() > 0){
				deleteGuiZeFailProviders(taskid,list);
			}

			//==================调用工作流之前，初始化his数据===========
			createHisTableInit(taskid);

			String flag = StringUtil.isEmpty(model.getFlag())?"0":model.getFlag();
			//调用工作流,投保流程
			LogUtil.info("workflowStartQuote"+model.getProcessinstanceid()+"=====调用工作流开始");
			int result = noticeWorkflowStartQuote(taskid,flag);
			LogUtil.info("workflowStartQuote"+model.getProcessinstanceid()+"=====调用工作流结束返回参数="+result);

			if(result == 1){
				commonModel.setStatus("fail");
				commonModel.setMessage("报价总表信息不存在");
				return commonModel;
			}
			if(result == 2){
				commonModel.setStatus("fail");
				commonModel.setMessage("报价信息不存在");
				return commonModel;
			}
			if(result == 3){
				commonModel.setStatus("fail");
				commonModel.setMessage("投保信息保存失败了");
				return commonModel;
			}
			if(result == 4){
				commonModel.setStatus("fail");
				commonModel.setMessage("保存失败");
				return commonModel;
			}else{
				commonModel.setMessage("操作成功");
				commonModel.setStatus("success");
			}
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}
	/**
	 * 转换不合法默认为0
	 * @param str
	 * @return
	 */
	private double convertToDouble(String str){
		return StringUtil.isEmpty(str) || !ModelUtil.isFloatNumber(str)?0d:Double.parseDouble(str);
	}
	/**
	 * 转换不合法默认为0
	 * @param str
	 * @return
	 */
	private int convertToInteger(String str){
		return StringUtil.isEmpty(str) || !ModelUtil.isNumeric(str)?0:Integer.parseInt(str);
	}

	@Override
	public CommonModel workFlowRestartConf(WorkFlowRestartConfModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if(StringUtil.isEmpty(taskid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			String pid = model.getPid();
			if(StringUtil.isEmpty(pid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("供应商id不能为空");
				return commonModel;
			}
			//获取重新报价公司的的子流程id
			String workflowinstanceid = "";
			Map<String, String> map = new HashMap<String, String>();
			map.put("taskid", taskid);
			map.put("inscomcode", pid);

			INSBQuoteinfo insbQuoteinfo = appInsuredQuoteDao.selectInsbQuoteInfoByTaskidAndPid(map);
			if(null != insbQuoteinfo){
				workflowinstanceid = insbQuoteinfo.getWorkflowinstanceid();
			}
			if(StringUtil.isEmpty(workflowinstanceid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("该公司没有对应的报价信息"+pid);
				return commonModel;
			}

			//如果自定义车价，或车型code为空（bug-5055），重新匹配年款
			INSBCarmodelinfo insbCarmodelinfo = appInsuredQuoteDao.selectCarmodelInfoByTaskid(taskid);
			INSBCarmodelinfohis carmodelinfohis = insbCarmodelinfohisDao.selectModelInfoByTaskIdAndPrvId(taskid, pid);

			if((null != insbCarmodelinfo && "2".equals(insbCarmodelinfo.getCarprice())) ||
					carmodelinfohis == null || StringUtil.isEmpty(carmodelinfohis.getJyCode())){
				LogUtil.info("修改保险配置调用工作流workFlowRestartConf="+taskid + "===被修改供应商id="+pid+"===操作人="+insbQuoteinfo.getOperator()+
	                    "===重新匹配年款开始="+model.getFlowflag());
				saveCarmodelinfoToData(taskid, pid, workflowinstanceid);
			}

			//调用工作流,重新获取报价途径
			LogUtil.info("修改保险配置调用工作流workFlowRestartConf="+taskid + "===被修改供应商id="+pid+"===操作人="+insbQuoteinfo.getOperator()+
                    "===0 选择投保 1 核保退回修改信息 2 报价退回 接收参数flowflag="+model.getFlowflag());

			String flowflag = model.getFlowflag();
			if(!StringUtil.isEmpty(flowflag)){
				INSBWorkflowmain mainModel = insbWorkflowmainDao.selectByInstanceId(taskid);
                final String subInstanceid = workflowinstanceid;

                //启用线程，避免事务问题
                if ("0".equals(flowflag)) {//报价修改投保信息
                    //删除报价信息表中写入的价格
                    deleteSuccessQuotePrice(insbQuoteinfo, taskid, pid);
                    taskthreadPool4workflow.execute(new Runnable() {
                        @Override
                        public void run() {
                            WorkflowFeedbackUtil.setWorkflowFeedback(taskid, subInstanceid, "14", "Completed", "选择投保", WorkflowFeedbackUtil.quote_redo, "admin");
                            Map<String, Object> datamap = new HashMap<String, Object>();
                            datamap = getPriceParamWay(datamap, taskid, insbQuoteinfo.getInscomcode(), "0");
                            WorkFlowUtil.completeTaskWorkflowRecheck("1", subInstanceid, mainModel.getOperator(), "选择投保", datamap);
                        }
                    });
                } else if ("1".equals(flowflag)) {//核保退回修改投保信息
                    taskthreadPool4workflow.execute(new Runnable() {
                        @Override
                        public void run() {
                            WorkFlowUtil.updateInsuredInfoNoticeWorkflow(null,subInstanceid, mainModel.getOperator(), "核保退回", "");
                        }
                    });
                } else if ("2".equals(flowflag)) {
                    //删除报价信息表中写入的价格
                    deleteSuccessQuotePrice(insbQuoteinfo, taskid, pid);
                    taskthreadPool4workflow.execute(new Runnable() {
                        @Override
                        public void run() {
                            WorkflowFeedbackUtil.setWorkflowFeedback(taskid, subInstanceid, "13", "Completed", "报价退回", "修改提交", "admin");
                            Map<String, Object> datamap = new HashMap<String, Object>();
                            datamap = getPriceParamWay(datamap, taskid, insbQuoteinfo.getInscomcode(), "0");
                            WorkFlowUtil.completeTaskWorkflowRecheck("1", subInstanceid, mainModel.getOperator(), "报价退回", datamap);
                        }
                    });
                } else {
                    //删除报价信息表中写入的价格
                    //deleteSuccessQuotePrice(insbQuoteinfo,taskid,pid);
                    taskthreadPool4workflow.execute(new Runnable() {
                        @Override
                        public void run() {
                            WorkflowFeedbackUtil.setWorkflowFeedback(taskid, subInstanceid, "51", "Completed", "承保政策限制", "修改提交", "admin");
                            Map<String, Object> datamap = new HashMap<String, Object>();
                            datamap = getPriceParamWay(datamap, taskid, insbQuoteinfo.getInscomcode(), "0");
                            WorkFlowUtil.resquestQuotationToWorkflow(subInstanceid, "承保政策限制", datamap);
                        }
                    });
                }
                LogUtil.info("修改保险配置调用工作流结束workFlowRestartConf=" + taskid + "," + subInstanceid);
			}else{
				LogUtil.info("修改保险配置调用工作流workFlowRestartConf="+taskid+","+workflowinstanceid + "标识flowflag为空");
				commonModel.setStatus("fail");
				commonModel.setMessage("标识flowflag为空");
				return commonModel;
			}

			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");

		} catch (Exception e) {
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败,发生异常");
            e.printStackTrace();
		}
		return commonModel;
	}
	
	@Override
	public Map<String, Object> getPriceParamWay(Map<String, Object> datamap,String taskid,String inscomcode,String type){
		//调用规则判断承保限制条件
		WorkFlowRuleInfo ruleCheckmodel = new WorkFlowRuleInfo();
		ruleCheckmodel.setProcessinstanceid(taskid);
		ruleCheckmodel.setInscomcode(inscomcode);
		CommonModel ruleResult = appOtherRequestService.saveWorkFlowRuleInfo(ruleCheckmodel);
		boolean callback = true;
		if ("success".equals(ruleResult.getStatus())) {
			LogUtil.info("获取报价途径getPriceParamWay,taskid="+taskid+"inscomcode="+inscomcode+"result:"+ruleResult.getBody());
			if (null == ruleResult.getBody()||"false".equals(ruleResult.getBody().toString())) {
				callback = false;
			}else{
				callback = true;
				//可以报价，获取报价途径
				String quoteType = JSONObject.fromObject(insbWorkflowDataService.getQuoteType(Long.valueOf(taskid), inscomcode, type)).getString("quotecode");
				if("1".equals(quoteType)){
					datamap.put("ptediway", insbWorkflowmainService.getEDIPTway(taskid, inscomcode));
				}else if("3".equals(quoteType) || "5".equals(quoteType)){
					datamap.put("ptgzway", insbWorkflowmainService.getGZPTway(taskid, inscomcode));
					datamap.put("gzway", JSONObject.fromObject(insbWorkflowmainService.getGZway(taskid, inscomcode)).getString("gzway"));
				}
				datamap.put("getpriceway", quoteType);
			}
		}
		datamap.put("rulesDecision", callback);
		return datamap;
	}
	
	@Override
	public Map<String, Object> getPriceParamWayStartSubflow(Map<String, Object> param,String taskid,String inscomcode){
		/*WorkFlowRuleInfo ruleCheckmodel = new WorkFlowRuleInfo();
		ruleCheckmodel.setProcessinstanceid(taskid);
		ruleCheckmodel.setInscomcode(inscomcode);
		CommonModel ruleResult = appOtherRequestService.saveWorkFlowRuleInfo(ruleCheckmodel);*/
		boolean callback = true;
		/*if ("success".equals(ruleResult.getStatus())) {
			LogUtil.info("获取报价途径getPriceParamWay,taskid="+taskid+"inscomcode="+inscomcode+"result:"+ruleResult.getBody());
			if (null == ruleResult.getBody()||"false".equals(ruleResult.getBody().toString())) {
				callback = false;
			}else{
				callback = true;
				//可以报价，获取报价途径
				String quoteType = JSONObject.fromObject(insbWorkflowDataService.getQuoteType(Long.valueOf(taskid), inscomcode, "0")).getString("quotecode");
				if ("1".equals(quoteType)) {
					param.put(inscomcode + "ptediway", insbWorkflowmainService.getEDIPTway(taskid, inscomcode));
				} else if ("3".equals(quoteType) || "5".equals(quoteType)) {
					param.put(inscomcode + "ptgzway", insbWorkflowmainService.getGZPTway(taskid, inscomcode));
					param.put(inscomcode + "gzway",JSONObject.fromObject(insbWorkflowmainService.getGZway(taskid, inscomcode)).getString("gzway"));
				}
				param.put(inscomcode+"quoteType", quoteType);
			}
		}*/
		param.put(inscomcode, callback);
		return param;
	}
	
	/**
	 * 规则校验没通过的，删除未通过的报价公司，报价信息表，保险公司险别报价表,保单表
	 * @param taskid
	 * @param list
	 */
	private void deleteGuiZeFailProviders(String taskid,List<String> list){
		LogUtil.info("deleteGuiZeFailProviders"+taskid+"=====规则校验没通过的供应商删除报价信息：" +list);
		for(String pid : list){
			//删除保险公司险别报价表
			INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
			insbCarkindprice.setTaskid(taskid);
			insbCarkindprice.setInscomcode(pid);
			List<INSBCarkindprice> carkindprices = insbCarkindpriceDao.selectList(insbCarkindprice);
			if(null != carkindprices && carkindprices.size() > 0){
				LogUtil.info("规则校验没通过删除已有的险别分表"+taskid+"操作人=规则");
				for(INSBCarkindprice carkindprice : carkindprices){
					insbCarkindpriceDao.deleteById(carkindprice.getId());
				}
			}
			//报价信息表
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			if(null != quotetotalinfo){
				INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
				insbQuoteinfo.setInscomcode(pid);
				insbQuoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
				INSBQuoteinfo quoteinfo = insbQuoteinfoDao.selectOne(insbQuoteinfo);
				if(null != quoteinfo){
					insbQuoteinfoDao.deleteById(quoteinfo.getId());
				}
			}
			//保单表数据
			INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
			insbPolicyitem.setTaskid(taskid);
			insbPolicyitem.setInscomcode(pid);
			List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(insbPolicyitem);
			if(null != insbPolicyitems && insbPolicyitems.size() > 0){
				for(INSBPolicyitem policyitem : insbPolicyitems){
					insbPolicyitemDao.deleteById(policyitem.getId());
				}
			}
		}
	}

	/**
	 *
	 * @param model
     * @return
     */
	@Override
	public CommonModel checkRestartConf(InsuredOneConfigModel model) {
		//CommonModel commonModel = checkUpdateInsureConf(model);
//		if("fail".equalsIgnoreCase(commonModel.getStatus())) {
//			return commonModel;
//		}
		WorkFlowRestartConfModel workFlowModel = new WorkFlowRestartConfModel();
		workFlowModel.setProcessinstanceid(model.getProcessinstanceid());
		workFlowModel.setPid(model.getPid());
		workFlowModel.setFlowflag("3");
		LogUtil.info("新流程承保政策重提报价：" + JsonUtil.serialize(workFlowModel));
		return workFlowRestartConf(workFlowModel);
	}

	@Override
	public CommonModel checkUpdateInsureConf(InsuredOneConfigModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if (StringUtil.isEmpty(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			String pid = model.getPid();
			if (StringUtil.isEmpty(pid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("报价公司id不能为空");
				return commonModel;
			}

            LogUtil.info("修改保险配置调用规则checkUpdateInsureConf==taskid:"+taskid+"===被修改供应商id="+pid+"===操作人="+model.getOperator());

			// 已选择的报价公司（报价信息总表，报价信息表）
			INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
			quotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
			if (null == insbQuotetotalinfo) {
				commonModel.setStatus("fail");
				commonModel.setMessage("报价信息总表不存在");
				return commonModel;
			}
			INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
			insbQuoteinfo.setQuotetotalinfoid(insbQuotetotalinfo.getId());
			insbQuoteinfo.setInscomcode(pid);
			INSBQuoteinfo quoteinfo = insbQuoteinfoDao.selectOne(insbQuoteinfo);
			if (null == quoteinfo) {
				commonModel.setStatus("fail");
				commonModel.setMessage("没选择该报价公司");
				return commonModel;
			}
			INSBProvider insbProvider = insbProviderDao.selectById(pid);
			if(null == insbProvider){
				commonModel.setStatus("fail");
				commonModel.setMessage("该报价公司不存在");
				return commonModel;
			}
			//修改投保车价
			if(!StringUtil.isEmpty(model.getPolicycarprice())){
				INSBCarinfo insbCarinfo = new INSBCarinfo();
				insbCarinfo.setTaskid(taskid);
				INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
				if (null == carinfo) {
					commonModel.setStatus("fail");
					commonModel.setMessage("车辆信息不存在");
					return commonModel;
				}
				INSBCarmodelinfo insbCarmodelinfo = new INSBCarmodelinfo();
				insbCarmodelinfo.setCarinfoid(carinfo.getId());
				INSBCarmodelinfo carmodelinfo = insbCarmodelinfoDao.selectOne(insbCarmodelinfo);
				if (null == carmodelinfo) {
					commonModel.setStatus("fail");
					commonModel.setMessage("车型信息不存在");
					return commonModel;
				}
				//调整车价有值改为自定义
				carmodelinfo.setCarprice("2");
				carmodelinfo.setPolicycarprice(model.getPolicycarprice());
				insbCarmodelinfoDao.updateById(carmodelinfo);
				//车型历史表维护自定义车价
				INSBCarmodelinfohis insbCarmodelinfohis = new INSBCarmodelinfohis();
				insbCarmodelinfohis.setCarinfoid(carinfo.getId());
                insbCarmodelinfohis.setInscomcode(pid);
				INSBCarmodelinfohis carmodelinfohis = insbCarmodelinfohisDao.selectOne(insbCarmodelinfohis);
				if(null != carmodelinfohis){
					//调整车价有值改为自定义
					carmodelinfohis.setCarprice("2");
					carmodelinfohis.setPolicycarprice(model.getPolicycarprice());
					insbCarmodelinfohisDao.updateById(carmodelinfohis);
				}

			}

			List<String> list = new ArrayList<String>();
			// 商业险
			List<BusinessRisks> businessRisks = model.getBusinessRisks();
			if (null != businessRisks && businessRisks.size() > 0) {
				for (BusinessRisks businessRisk : businessRisks) {
					//根据险别编码，和供应商id查询供应商是否支持该先别
					long num = appInsuredQuoteDao.verificationInsuredConfig(businessRisk.getCode(),pid.substring(0, 4));
					if(num <= 0){
						list.add(businessRisk.getName());
					}
				}
			}
			// 交强险
//			List<StrongRisk> strongRisks = model.getStrongRisk();
//			if (null != strongRisks && strongRisks.size() > 0) {
//				for (StrongRisk strongRisk : strongRisks) {
//					//根据险别编码，和供应商id查询供应商是否支持该先别
//					long num = appInsuredQuoteDao.verificationInsuredConfig(strongRisk.getCode(),pid);
//					if(num <= 0){
//						list.add(strongRisk.getName());
//					}
//				}
//			}
			CheckUpdateInsureConfBean updateInsureConfBean = new CheckUpdateInsureConfBean();
			if(list.size() > 0){
				updateInsureConfBean.setRiskinfos(list);
			}
			updateInsureConfBean.setId(insbProvider.getId());
			updateInsureConfBean.setLogo(insbProvider.getLogo());
			updateInsureConfBean.setPrvname(insbProvider.getPrvname());
			updateInsureConfBean.setPrvshotname(insbProvider.getPrvshotname());
			updateInsureConfBean.setFlag(true);

			//调用规则
			String result = appQuotationService.getQuotationValidatedInfo("",taskid,pid);
            LogUtil.info("规则结果taskid:"+taskid+"=="+result);

			JSONObject jsonObject = JSONObject.fromObject(result);

			//{"success":false,"quotationMode":0,"resultMsg":["商业险、交强起保日期不一致需分开提交"]}  提示性规则:0,转人工规则:1,限制性规则:2,阻断性规则:3
			if(!jsonObject.getBoolean("success") && jsonObject.containsKey("quotationMode") && "3".equals(jsonObject.get("quotationMode").toString())){
				updateInsureConfBean.setReason(jsonObject.getString("resultMsg"));
				updateInsureConfBean.setFlag(false);
			}
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(updateInsureConfBean);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel callPlatformQuery(CallPlatformQueryModel model) {
		System.out.print(JsonUtil.serialize(model));

		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if (StringUtil.isEmpty(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			LogUtil.info("callPlatformQuery:"+taskid+",代理人提交的车主:"+model.getOwerName()+",车牌号:"+model.getPlateNumber());

			Map<String, Object> body = new HashMap<String, Object>();
			if(!StringUtil.isEmpty(model.getPlateNumber())){
				//根据车牌平台查询
				LastYearPolicyInfoBean lastYearPolicyInfoBean = null;
				//权限包
				Map<String,Object> permissionMap =new HashMap<String,Object>();
				List<Boolean> cif = new ArrayList<Boolean>();
				List<AgentRelatePermissionVo> permissionVoList = new ArrayList<AgentRelatePermissionVo>();
				boolean hasPrivilege = false;
				if (StringUtil.isEmpty(redisClient.get(Constants.TASK, "cif" + taskid))) {
					String[] ciflist={"cif_own","cif_renewal","cif_social"};
					for (String s: ciflist) {
						permissionMap = appPermissionService.findPermission(model.getAgentid(), "", s);
						permissionVoList.add((AgentRelatePermissionVo)permissionMap.get("AgentRelatePermissionVo"));
						if ((int)permissionMap.get("status") == 0) {
							cif.add(Boolean.TRUE);
							hasPrivilege = true;
						} else {
							cif.add(Boolean.FALSE);
						}
					}
					if (hasPrivilege) {
						lastYearPolicyInfoBean = queryLastInsuredInfoByPlateNumber(taskid,model.getPlateNumber(),model.getCityCode(),"",model.getParityflag(),model.getAgentid(),model.getOwerName(),model.getPersonIdno(),model.getProvinceCode(), model.getVincode(), cif.toArray(new Boolean[3]));
						redisClient.set(Constants.TASK, "cif" + taskid,taskid,60*60);
						redisClient.setList(Constants.TASK, "cifprivilege" + taskid,cif,60*60);
						redisClient.setList(Constants.TASK, "cifAgentRelatePermission" + taskid,permissionVoList,60*60);
						appPermissionService.updatePermission(lastYearPolicyInfoBean, permissionVoList, taskid);
					} else {
						body.put("permissionStatus", CommonModel.STATUS_FAIL);
						body.put("permissionMessage", "no cif query privilege");
					}

				} else {
					cif=redisClient.getList(Constants.TASK, "cifprivilege" + taskid,Boolean.class);
					permissionVoList=redisClient.getList(Constants.TASK, "cifAgentRelatePermission" + taskid,AgentRelatePermissionVo.class);
					lastYearPolicyInfoBean = queryLastInsuredInfoByPlateNumber(taskid,model.getPlateNumber(),model.getCityCode(),"",model.getParityflag(),model.getAgentid(),model.getOwerName(),model.getPersonIdno(),model.getProvinceCode(), model.getVincode(), cif.toArray(new Boolean[3]));
					appPermissionService.updatePermission(lastYearPolicyInfoBean, permissionVoList, taskid);
				}

				//校验 车主姓名和cif带出的车主姓名是否一致，不一致直接默认没查到信息 20160509
				boolean issameower = false;
				if(null != lastYearPolicyInfoBean){
					//车主信息
					LastYearPersonBean carowner = lastYearPolicyInfoBean.getCarowner();
					if(null != carowner && !model.getOwerName().equals(carowner.getName())){
						issameower = true;
						LogUtil.info("callPlatformQuery:"+taskid+",代理人提交的车主:"+model.getOwerName()+",cif返回的车主:"+carowner.getName()+",是否不一致:"+issameower);
					}

					if (StringUtil.isNotEmpty(model.getQueryFlag()) && "quickInsurancePolicy".equalsIgnoreCase(model.getQueryFlag())) {
						//我要比价，不进行车主判断，直接返回
						issameower = false;
					}
				}
				Map<String, Object> resultMap = new HashMap<String, Object>();
				//平台查询失败了
				//传给前端queryflag 0成功, 1等待, 2失败, 3输入身份证, 7需要提供车架号查询
				if(null == lastYearPolicyInfoBean || "2".equals(lastYearPolicyInfoBean.getStatus()) 
						|| "3".equals(lastYearPolicyInfoBean.getStatus()) 
						|| issameower || "7".equals(lastYearPolicyInfoBean.getStatus())){
					resultMap.put("insureinfo", false);
					resultMap.put("carinfo", false);
					resultMap.put("carmodel", false);
					resultMap.put("provider", false);
					resultMap.put("insuredconf", false);
					resultMap.put("queryflag",null == lastYearPolicyInfoBean? "2": lastYearPolicyInfoBean.getStatus());
					//大数据查询失败了，超时，可能会导致投保人，被保人表数据重复，保存后置
					//saveAllPersonInfo(taskid);
				}else{
					resultMap =  noticeResult(lastYearPolicyInfoBean,model.getProvinceCode(),model.getCityCode(),model.getAgentid(),"",taskid);
					resultMap.put("queryflag", lastYearPolicyInfoBean.getStatus());
					//保存，20160505，减少入库修改次数 20160822 必须存在vin码的时候数据入库，解决社会化接口查到数据前端不展示
					LastYearCarinfoBean lastYearCarinfoBean = lastYearPolicyInfoBean.getLastYearCarinfoBean();
					//从redis里面取值
					LastYearPolicyInfoBean redisYearPolicyInfoBean = redisClient.get(Constants.TASK, taskid, LastYearPolicyInfoBean.class);

					if(null == redisYearPolicyInfoBean && null != lastYearCarinfoBean && !StringUtil.isEmpty(lastYearCarinfoBean.getVehicleframeno())){
						saveLastYearPolicyInfoBean(taskid,lastYearPolicyInfoBean,model.getProvinceCode(),model.getCityCode(),model.getAgentid(),"",model.getAgentName(),model.getFlag());
						//返回值放入ridis,key值 实例id
						cachelastYearPolicyInfo(taskid, lastYearPolicyInfoBean);
					}
					//只有查询成功才保存出险信息  以会调为主，大对象返回不保存
//					if("0".equals(lastYearPolicyInfoBean.getStatus())){
						//保存出险信息
//						saveLastYearClaimsInfo(taskid, model.getAgentName(), lastYearPolicyInfoBean);
//					}
				}
				body.put("resultMap", resultMap);
				commonModel.setStatus("success");
				commonModel.setMessage("操作成功");
				commonModel.setBody(body);
			}else{
				//无车牌，投保人，被保人，默认车主一样
				saveAllPersonInfo(taskid);
				commonModel.setStatus("success");
				commonModel.setMessage("操作成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel lastInsuredRenewaByCar(CallPlatformQueryModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if (StringUtil.isEmpty(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			LogUtil.info("lastInsuredRenewaByCar:"+taskid+",代理人提交的车主:"+model.getOwerName()+",车牌号:"+model.getPlateNumber());

			Map<String, Object> body = new HashMap<String, Object>();
			if(!StringUtil.isEmpty(model.getPlateNumber())){
				//根据车牌平台查询
				LastYearPolicyInfoBean lastYearPolicyInfoBean = null;
				//权限包
				Map<String,Object> permissionMap =new HashMap<String,Object>();
				List<Boolean> cif = new ArrayList<Boolean>();
				List<AgentRelatePermissionVo> permissionVoList = new ArrayList<AgentRelatePermissionVo>();
				boolean hasPrivilege = false;
				if (StringUtil.isEmpty(redisClient.get(Constants.TASK, "cif" + taskid))) {
					String[] ciflist={"cif_own","cif_renewal","cif_social"};
					for (String s: ciflist) {
						permissionMap = appPermissionService.findPermission(model.getAgentid(), "", s);
						permissionVoList.add((AgentRelatePermissionVo)permissionMap.get("AgentRelatePermissionVo"));
						if ((int)permissionMap.get("status") == 0) {
							cif.add(Boolean.TRUE);
							hasPrivilege = true;
						} else {
							cif.add(Boolean.FALSE);
						}
					}
					if (hasPrivilege) {
						lastYearPolicyInfoBean = queryLastInsuredInfoByCar(taskid, model.getPlateNumber(), model.getOwerName(), model.getAgentid(), "1",cif.toArray(new Boolean[3]));
						redisClient.set(Constants.TASK, "cif" + taskid,taskid,60*60);
						redisClient.setList(Constants.TASK, "cifprivilege" + taskid,cif,60*60);
						redisClient.setList(Constants.TASK, "cifAgentRelatePermission" + taskid,permissionVoList,60*60);
						appPermissionService.updatePermission(lastYearPolicyInfoBean, permissionVoList, taskid);
					} else {
						body.put("permissionStatus", CommonModel.STATUS_FAIL);
						body.put("permissionMessage", "no cif query privilege");
					}

				} else {
					cif=redisClient.getList(Constants.TASK, "cifprivilege" + taskid,Boolean.class);
					permissionVoList=redisClient.getList(Constants.TASK, "cifAgentRelatePermission" + taskid,AgentRelatePermissionVo.class);
					lastYearPolicyInfoBean = queryLastInsuredInfoByCar(taskid, model.getPlateNumber(), model.getOwerName(), model.getAgentid(), "1", cif.toArray(new Boolean[3]));
					appPermissionService.updatePermission(lastYearPolicyInfoBean, permissionVoList, taskid);
				}


				//校验 车主姓名和cif带出的车主姓名是否一致，不一致直接默认没查到信息 20160509
				boolean issameower = false;
				if(null != lastYearPolicyInfoBean){
					//车主信息
					LastYearPersonBean carowner = lastYearPolicyInfoBean.getCarowner();
					if(null != carowner && !model.getOwerName().equals(carowner.getName())){
						issameower = true;
						LogUtil.info("callPlatformQuery:"+taskid+",代理人提交的车主:"+model.getOwerName()+",cif返回的车主:"+carowner.getName()+",是否不一致:"+issameower);
					}
				}
				Map<String, Object> resultMap = new HashMap<String, Object>();
				//平台查询失败了
				//传给前端queryflag 0成功, 1等待, 2失败, 3输入身份证, 7需要提供车架号查询
				if(null == lastYearPolicyInfoBean || "2".equals(lastYearPolicyInfoBean.getStatus())
						|| "3".equals(lastYearPolicyInfoBean.getStatus())
						|| issameower || "7".equals(lastYearPolicyInfoBean.getStatus())){
					resultMap.put("insureinfo", false);
					resultMap.put("carinfo", false);
					resultMap.put("carmodel", false);
					resultMap.put("provider", false);
					resultMap.put("insuredconf", false);
					resultMap.put("queryflag",null == lastYearPolicyInfoBean? "2": lastYearPolicyInfoBean.getStatus());
					//大数据查询失败了，超时，可能会导致投保人，被保人表数据重复，保存后置
					//saveAllPersonInfo(taskid);
				}else{
					resultMap =  noticeResult(lastYearPolicyInfoBean,model.getProvinceCode(),model.getCityCode(),model.getAgentid(),"",taskid);
					resultMap.put("queryflag", lastYearPolicyInfoBean.getStatus());
					//保存，20160505，减少入库修改次数 20160822 必须存在vin码的时候数据入库，解决社会化接口查到数据前端不展示
					LastYearCarinfoBean lastYearCarinfoBean = lastYearPolicyInfoBean.getLastYearCarinfoBean();
					//从redis里面取值
					LastYearPolicyInfoBean redisYearPolicyInfoBean = redisClient.get(Constants.TASK, taskid, LastYearPolicyInfoBean.class);

					if(null == redisYearPolicyInfoBean && null != lastYearCarinfoBean && !StringUtil.isEmpty(lastYearCarinfoBean.getVehicleframeno())){
						saveLastYearPolicyInfoBean(taskid,lastYearPolicyInfoBean,model.getProvinceCode(),model.getCityCode(),model.getAgentid(),"",model.getAgentName(),model.getFlag());
						//返回值放入ridis,key值 实例id
						cachelastYearPolicyInfo(taskid, lastYearPolicyInfoBean);
					}
					//只有查询成功才保存出险信息  以会调为主，大对象返回不保存
//					if("0".equals(lastYearPolicyInfoBean.getStatus())){
					//保存出险信息
//						saveLastYearClaimsInfo(taskid, model.getAgentName(), lastYearPolicyInfoBean);
//					}
				}
				body.put("resultMap", resultMap);
				commonModel.setStatus("success");
				commonModel.setMessage("操作成功");
				commonModel.setBody(body);
			}else{
				//无车牌，投保人，被保人，默认车主一样
				saveAllPersonInfo(taskid);
				commonModel.setStatus("success");
				commonModel.setMessage("操作成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	/**
	 * 页面初始化
	 * @param  processinstanceid 主实例ID
	 */
	public CommonModel findInitWebPage(String processinstanceid,String webPageKey){
		// key 声明 设置
		CommonModel commonModel = new CommonModel();
//		String webPageKey =getWebPageKey(processinstanceid);
		if(webPageKey==null||"".equals(webPageKey)){
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败,webpagekey空");
			return commonModel;
		}else if("0".equals(webPageKey)){//车俩Key
			commonModel.setBody(getSaveCarInfoModel(processinstanceid));
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			return commonModel;
		}else if("1".equals(webPageKey)){//车型KEy
			commonModel.setBody(getCarModelInfoModel(processinstanceid));
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			return commonModel;
		}else{
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
			return commonModel;
		}
	}

	/**
	 * 根据 实例ID 获取WebPage Key
	 * @param processinstanceid
	 * @return
	 */
	/*private  String getWebPageKey(String processinstanceid){
		// 获取 页面Key
		return "";
	}*/

	/**
	 * 车辆信息页面
	 * @param processinstanceid
	 * @return
	 */
	private SaveCarInfoModel getSaveCarInfoModel(String processinstanceid){
		SaveCarInfoModel carInfoModel = new SaveCarInfoModel();
		// 查询车辆信息封装 返回
		INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setTaskid(processinstanceid);
		INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo); //车辆
		if(null != carinfo){
			carInfoModel.setProcessinstanceid(processinstanceid); //实例id
			if(StringUtil.isEmpty(carinfo.getRegistdate())){
				carInfoModel.setFirstRegisterDate(""); //车辆初登日期
			}else{
				carInfoModel.setFirstRegisterDate(ModelUtil.conbertToString(carinfo.getRegistdate())); //车辆初登日期
			}
			carInfoModel.setVin(StringUtil.isEmpty(carinfo.getVincode())?"":ModelUtil.hiddenVin(carinfo.getVincode())); //车辆识别代号
			carInfoModel.setEngineNo(StringUtil.isEmpty(carinfo.getEngineno())?"":ModelUtil.hiddenEngineNo(carinfo.getEngineno())); //发动机号
			carInfoModel.setTempVin(StringUtil.isEmpty(carinfo.getVincode())?"":ModelUtil.hiddenVin(carinfo.getVincode())); //车辆识别代号
			carInfoModel.setTempEngineNo(StringUtil.isEmpty(carinfo.getEngineno())?"":ModelUtil.hiddenEngineNo(carinfo.getEngineno())); //发动机号
			carInfoModel.setModelCode(carinfo.getStandardfullname());//品牌型号
			carInfoModel.setChgOwnerFlag(carinfo.getIsTransfercar()); //是否过户车
			if(StringUtil.isEmpty(carinfo.getTransferdate())){
				carInfoModel.setChgOwnerDate(""); //车辆过户日期
			}else{
                carInfoModel.setChgOwnerDate(ModelUtil.conbertToString(carinfo.getTransferdate())); //车辆过户日期
			}
			carInfoModel.setDrivinglicense(carinfo.getDrivinglicenseurl());
		}

		return carInfoModel;
	}

	/**
	 * 车型信息页面
	 * @param processinstanceid
	 * @return
	 */
	private CarModelInfoModel getCarModelInfoModel(String processinstanceid){
		CarModelInfoModel carModelInfo = new CarModelInfoModel();
		// 查询车型息封装 返回
		INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setTaskid(processinstanceid);
		INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo); //车辆
		if(null != carinfo){
			INSBCarmodelinfo carModel = insbCarmodelinfoDao.selectByCarinfoId(carinfo.getId()); //车型
			if(null != carModel){
				carModelInfo.setProcessinstanceid(processinstanceid);//实例id
				carModelInfo.setModelCode(carModel.getStandardname());//品牌型号
				carModelInfo.setDisplacement(carModel.getDisplacement());//排量
				carModelInfo.setApprovedLoad(carModel.getSeat());//核载人数
				carModelInfo.setRulePriceProvideType(carModel.getCarprice());//车价选择
				carModelInfo.setInstitutionType(carinfo.getProperty());//所属性质：个人用车，企业用车，机关团体用车
				carModelInfo.setUseProperty(carinfo.getCarproperty()); //车辆性质，试用性质
				carModelInfo.setTonnage(carModel.getUnwrtweight()); //核定载质量
				carModelInfo.setWholeWeight(carModel.getFullweight());//整备质量
				carModelInfo.setCustomPrice(carModel.getPolicycarprice());//自定义车价
			}
		}

		return carModelInfo;
	}

	//删除报价信息表中写入的价格 20160304
	private void deleteSuccessQuotePrice(INSBQuoteinfo insbQuoteinfo,String taskid,String inscomcode){
		LogUtil.info("deleteSuccessQuotePrice"+taskid+"删除上次报价信息的供应商="+inscomcode);
		//报价信息表  Quotediscountamount、Noti、Taskstatus
		if(null != insbQuoteinfo){
			insbQuoteinfo.setQuoteresult(null);
			insbQuoteinfo.setQuoteamount(null);
			insbQuoteinfo.setQuotediscountamount(null);
			insbQuoteinfo.setQuoteinfo(null);
			insbQuoteinfo.setNoti(null);
			insbQuoteinfo.setTaskstatus(null);
			insbQuoteinfoDao.updateById(insbQuoteinfo);
		}
		//保单表  premium,totalepremium,discountCharge,checkcode,discountRate,amount,paynum
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskid);
		insbPolicyitem.setInscomcode(inscomcode);
		List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(insbPolicyitem);
		if(null != insbPolicyitems && insbPolicyitems.size() > 0){
			for(INSBPolicyitem policyitem : insbPolicyitems){
				policyitem.setPremium(null);
				policyitem.setTotalepremium(null);
				policyitem.setDiscountCharge(null);
				policyitem.setCheckcode(null);
				policyitem.setDiscountRate(null);
				policyitem.setAmount(0d);
				policyitem.setPaynum(null);
				insbPolicyitemDao.updateById(policyitem);
			}
		}
		//InsbCarinfoHis noti
		INSBCarinfohis insbCarinfohis = new INSBCarinfohis();
		insbCarinfohis.setTaskid(taskid);
		insbCarinfohis.setInscomcode(inscomcode);
		List<INSBCarinfohis> insbCarinfohiss = insbCarinfohisDao.selectList(insbCarinfohis);
		if(null != insbCarinfohiss && insbCarinfohiss.size() > 0){
			for(INSBCarinfohis carinfohis : insbCarinfohiss){
				carinfohis.setNoti(null);
				insbCarinfohisDao.updateById(carinfohis);
			}
		}

		//退回修改，备份报价信息表数据，备份表存在则删除 20160505
		INSHCarkindprice inshCarkindprice = new INSHCarkindprice();
		inshCarkindprice.setTaskid(taskid);
		inshCarkindprice.setInscomcode(inscomcode);
		inshCarkindprice.setFairyoredi("appback");
		inshCarkindprice.setNodecode("H");
		List<INSHCarkindprice> inshCarkindprices = inshCarkindpriceDao.selectList(inshCarkindprice);
		if(null != inshCarkindprices && inshCarkindprices.size() > 0){
			for(INSHCarkindprice carkindprice : inshCarkindprices){
				inshCarkindpriceDao.deleteById(carkindprice.getId());
			}
		}
		//删除insbcarkindprice表报价数据
		INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
		insbCarkindprice.setTaskid(taskid);
		insbCarkindprice.setInscomcode(inscomcode);
		List<INSBCarkindprice> insbCarkindprices = insbCarkindpriceDao.selectList(insbCarkindprice);
		if(null != insbCarkindprices && insbCarkindprices.size() > 0){
			for(INSBCarkindprice carkindprice : insbCarkindprices){
				//备份数据
				INSHCarkindprice backcarkindprice = new INSHCarkindprice();
				backcarkindprice.setCreatetime(new Date());
				backcarkindprice.setModifytime(new Date());
				backcarkindprice.setOperator(carkindprice.getOperator());
				backcarkindprice.setTaskid(taskid);
				backcarkindprice.setNoti("退回修改备份");
				backcarkindprice.setInscomcode(carkindprice.getInscomcode());
				backcarkindprice.setInskindcode(carkindprice.getInskindcode());
				backcarkindprice.setInskindtype(carkindprice.getInskindtype());
				backcarkindprice.setAmount(carkindprice.getAmount());
				backcarkindprice.setNotdeductible(carkindprice.getNotdeductible());
				backcarkindprice.setAmountDesc(carkindprice.getAmountDesc());
				backcarkindprice.setAmountprice(carkindprice.getAmountprice());
				backcarkindprice.setSelecteditem(carkindprice.getSelecteditem());
				backcarkindprice.setPreriskkind(carkindprice.getPreriskkind());
				backcarkindprice.setRiskname(carkindprice.getRiskname());
				backcarkindprice.setDiscountCharge(carkindprice.getDiscountCharge());
				backcarkindprice.setDiscountRate(carkindprice.getDiscountRate());
				backcarkindprice.setBjmpCode(carkindprice.getBjmpCode());
				backcarkindprice.setPlankey(carkindprice.getPlankey());
				backcarkindprice.setFairyoredi("appback");
				backcarkindprice.setNodecode("H");
				inshCarkindpriceDao.insert(backcarkindprice);
				//清空报价数据
				carkindprice.setModifytime(new Date());
				carkindprice.setAmountprice(null);
				carkindprice.setDiscountCharge(null);
				carkindprice.setDiscountRate(null);
				insbCarkindpriceDao.updateById(carkindprice);
			}
		}
        LogUtil.info("deleteSuccessQuotePrice"+taskid+"删除上次报价信息的供应商="+inscomcode+"完成");
	}

	/**
	 * 交管车辆类型
	 * @param codeType  VehicleType
	 * @param codeName
	 * @return
	 */
	private int getjgVehicleTypeAndCodeName(String codeType,String codeName){
		int result = 0;
		//轿车
		if(StringUtil.isEmpty(codeName) && "VehicleType".equals(codeType)){
			result = 13;
		}else{
			//没查到其他类型
			Map<String, String> map = new HashMap<String, String>();
			map.put("codetype", codeType);
			map.put("codename", null == codeName?"":codeName.trim());
			String value = inscCodeDao.selectCodeValueByCodeName(map);
			result = StringUtil.isEmpty(value)?30:Integer.parseInt(value);
		}
		return result;
	}
	/**
	 * 产地类型
	 * @param codeType  CarVehicleOrigin
	 * @param codeName
	 * @return
	 */
	private int getVehicleTypeByCodeName(String codeType,String codeName){
		int result = 0;
		Map<String, String> map = new HashMap<String, String>();
		map.put("codetype", codeType);
		map.put("codename", null == codeName?"":codeName.trim());
		String value = inscCodeDao.selectCodeValueByCodeName(map);
		result = StringUtil.isEmpty(value)?0:Integer.parseInt(value);
		return result;
	}

	@Override
	public List<Map<String, String>> getCarUpdateByCode(String codetype) {
		return appInsuredQuoteDao.getCarUpdateByCode(codetype);
	}

	@Override
	public CommonModel fastRenewProviders(SearchProviderModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			if (StringUtil.isEmpty(model.getAgentid())) {
				commonModel.setStatus("fail");
				commonModel.setMessage("代理人id不能为空");
				return commonModel;
			}
			//代理人所属网点，查询该网点关联所有开启快速续保的协议和供应商
			List<SelectProviderBean> selectProviderBeans = appInsuredQuoteDao.getRenewalAgreementidPidByAgentid(model.getAgentid());
			if(selectProviderBeans.isEmpty()){
				commonModel.setStatus("fail");
				commonModel.setMessage("该代理人没有配置供应商");
				return commonModel;
			}

            List<SelectProviderBean> returnProviderList = new ArrayList<>();

            for(SelectProviderBean pbean : selectProviderBeans){
				//判断该供应商是否有续保能力（精灵、EDI配置）
				/*int num = appInsuredQuoteDao.isHaveFastRenewConfig(pbean.getParentid());
				if(num <= 0){
                    LogUtil.info("供应商"+pbean.getParentid()+"无续保能力");
					continue;
				}*/

                if (StringUtil.isNotEmpty(model.getPrvid()) && !pbean.getProvid().startsWith(model.getPrvid())) {
                    LogUtil.info("供应商"+pbean.getProvid()+"不属于"+model.getPrvid());
                    continue;
                }

                returnProviderList.add(pbean);
			}

            //获取代理人常用出单网点
            INSBAgent agent = insbAgentDao.selectById(model.getAgentid());

            //组装供应商与网点
            List<ProviderListBean> providerListBeans = buildProviderListBeansForRenewal(returnProviderList, model.getPrvid(), agent.getCommonusebranch());

			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(providerListBeans);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

    public CommonModel saveRenewalQuoteinfo(RenewalQuoteinfoModel model) {
        CommonModel resultModel = new CommonModel();
        resultModel.setStatus("fail");

        if (model == null) {
            resultModel.setMessage("参数为空");
            return resultModel;
        }

        String msg = null;
        String taskid = model.getProcessinstanceid();
        String plateNumber = model.getCarNumber();
        String carOwer = model.getCarOwer();
        String inscomcode = model.getInscomcode();
        String agreementid = model.getAgreementid();
        String singlesite = model.getSinglesite();
        String buybusitype = model.getBuybusitype();

        if (StringUtil.isEmpty(taskid)) {
            msg = "任务号为空";
        }
        if (StringUtil.isEmpty(plateNumber)) {
            msg = "车牌为空";
        }
        if (StringUtil.isEmpty(carOwer)) {
            msg = "车主为空";
        }
        if (StringUtil.isEmpty(inscomcode)) {
            msg = "投保公司为空";
        }
        if (StringUtil.isEmpty(agreementid)) {
            msg = "协议id为空";
        }
        if (StringUtil.isEmpty(singlesite)) {
            msg = "出单网点为空";
        }
        if (StringUtil.isEmpty(buybusitype)) {
            msg = "投保方式为空";
        }
        if (msg != null) {
            resultModel.setMessage(msg);
            return resultModel;
        }

        INSBCarinfo insbCarinfo = new INSBCarinfo();
        insbCarinfo.setTaskid(taskid);
        insbCarinfo = insbCarinfoDao.selectOne(insbCarinfo);
        if (insbCarinfo != null) {
            insbCarinfo.setCarlicenseno(plateNumber);
            insbCarinfo.setOwnername(carOwer);
            insbCarinfoDao.updateById(insbCarinfo);

            INSBPerson insbPerson = insbPersonDao.selectById(insbCarinfo.getOwner());
            if (insbPerson != null) {
                insbPerson.setName(carOwer);
                insbPersonDao.updateById(insbPerson);
            }
        }

        /**
         * 传递的参数  协议id#供应商id#出单网点id#网销传统类型
         * 必需参数，一一对应
         */
        List<String> paramsList = new ArrayList<String>(1);
        paramsList.add(agreementid+"#"+inscomcode+"#"+singlesite+"#"+buybusitype);

        ChoiceProviderIdsModel choiceProviderIdsModel = new ChoiceProviderIdsModel();
        choiceProviderIdsModel.setProcessinstanceid(model.getProcessinstanceid());
        choiceProviderIdsModel.setHandersupplier(model.getInscomcode());
        choiceProviderIdsModel.setParamsList(paramsList);

        //添加供应商
        CommonModel prosaveresult = saveProviderList(choiceProviderIdsModel, 0);
        if (!"success".equals(prosaveresult.getStatus())) {
            return prosaveresult;
        }

        //更新供应商、车主
        List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectPolicyitemList(taskid);
        if (policyitemList != null && !policyitemList.isEmpty()) {
            for (INSBPolicyitem policyitem : policyitemList) {
                if (StringUtil.isEmpty(policyitem.getInscomcode())) {
                    policyitem.setInscomcode(inscomcode);
                    policyitem.setCarownername(carOwer);
                    insbPolicyitemDao.updateById(policyitem);
                }
            }
        }

        //订单表出单网点
        INSBOrder insbOrder = new INSBOrder();
        insbOrder.setTaskid(taskid);
        insbOrder.setPrvid(inscomcode);
        INSBOrder order = insbOrderDao.selectOne(insbOrder);
        if(null != order){
            order.setDeptcode(singlesite);
            insbOrderDao.updateById(order);
        }

        //更新投保省份、地市
        if (StringUtil.isNotEmpty(model.getInsuredprovince()) || StringUtil.isNotEmpty(model.getInsuredcity())) {
            INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
            insbQuotetotalinfo.setTaskid(taskid);
            insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
            if (insbQuotetotalinfo != null) {
                if (StringUtil.isEmpty(insbQuotetotalinfo.getInsprovincecode())) {
                    insbQuotetotalinfo.setInsprovincecode(model.getInsuredprovince());
                }
                if (StringUtil.isEmpty(insbQuotetotalinfo.getInscitycode())) {
                    insbQuotetotalinfo.setInscitycode(model.getInsuredcity());
                }
                insbQuotetotalinfoDao.updateById(insbQuotetotalinfo);
            }
        }

        resultModel.setStatus("success");
        resultModel.setMessage("保存成功");
        return resultModel;
    }

    @Override
	public CommonModel fastRenewCallPlatformQuery(CallPlatformQueryModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if (StringUtil.isEmpty(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			if (StringUtil.isEmpty(model.getPlateNumber())) {
				commonModel.setStatus("fail");
				commonModel.setMessage("快速续保，车牌号不能为空");
				return commonModel;
			}
			// 根据车牌平台查询
			LastYearPolicyInfoBean lastYearPolicyInfoBean = queryLastInsuredInfoByPlateNumber(taskid, model.getPlateNumber(), model.getCityCode(),model.getProvid(),"0",model.getAgentid(),model.getOwerName(),"",model.getProvinceCode(),"");
			// 平台查询失败了，快速续保失败了
			if (null == lastYearPolicyInfoBean) {
				commonModel.setStatus("fail");
				commonModel.setMessage("快速续保失败了");
				return commonModel;
			} else {
				//0 符合续保要求 1 需要等待精灵查询 2 不符合续保要求
				Map<String, Object> map = new HashMap<String, Object>();
				String certNumber = "";
				String certificateType = "";
				//获取身份证号,先取被保人车牌号，不存在则取车主的
				if(null != lastYearPolicyInfoBean.getLastYearPolicyBean() && null != lastYearPolicyInfoBean.getLastYearPolicyBean().getInsureder() && !StringUtil.isEmpty(lastYearPolicyInfoBean.getLastYearPolicyBean().getInsureder().getIdno().trim())){
					certNumber = lastYearPolicyInfoBean.getLastYearPolicyBean().getInsureder().getIdno().trim();
					certificateType = getPeopleIdcardType(lastYearPolicyInfoBean.getLastYearPolicyBean().getInsureder().getIdtype().trim());
				}else{
					if(null != lastYearPolicyInfoBean.getCarowner()){
						certNumber = lastYearPolicyInfoBean.getCarowner().getIdno().trim();
						certificateType = getPeopleIdcardType(lastYearPolicyInfoBean.getCarowner().getIdtype().trim());
					}
				}
				//是否查到上年投保公司标识
				boolean havelastprovider = false;
				String havelastprovidername = "";
				Map<String, Object> resultMap = noticeResult(lastYearPolicyInfoBean,model.getProvinceCode(), model.getCityCode(),model.getAgentid(), "", taskid);
				boolean queryflag = false;
				boolean insuredconf = (boolean) resultMap.get("insuredconf");
				for(Entry<String, Object> entry : resultMap.entrySet()){
					if(entry.getKey().contains("bean") || "insuredconf".equals(entry.getKey())){
						continue;
					}else{
						queryflag = (boolean) entry.getValue();
						if(!queryflag){
							break;
						}
					}
				}

				if(("0".equals(lastYearPolicyInfoBean.getStatus()) || "1".equals(lastYearPolicyInfoBean.getStatus())) && queryflag){
					//保存
					saveLastYearPolicyInfoBean(taskid, lastYearPolicyInfoBean,model.getProvinceCode(), model.getCityCode(),model.getAgentid(), "", model.getAgentName(),model.getFlag());
					commonModel.setStatus("success");
					commonModel.setMessage("操作成功");
					map.put("queryflag", lastYearPolicyInfoBean.getStatus());
					if(queryflag){
						// 返回值放入ridis,key值 实例id
						cachelastYearPolicyInfo(taskid, lastYearPolicyInfoBean);
					}
					map.put("insuredconf", insuredconf);
					//上年投保公司信息
					LastYearSupplierBean supplierBean = lastYearPolicyInfoBean.getLastYearSupplierBean();
					if(null != supplierBean){
						if(supplierBean.getSupplierid().startsWith(model.getProvid())){
							havelastprovider = true;
						}
						havelastprovidername = supplierBean.getSuppliername();
					}
					//只有查询成功才保存出险信息
					if("0".equals(lastYearPolicyInfoBean.getStatus())){
						//保存出险信息
						saveLastYearClaimsInfo(taskid, model.getAgentName(), lastYearPolicyInfoBean);
					}
				}else{
					commonModel.setStatus("success");
					commonModel.setMessage("查询失败了");
					map.put("queryflag", 2);
				}
				map.put("certNumber", certNumber);
				map.put("certificateType", certificateType);
				map.put("havelastprovider", havelastprovider);
				map.put("havelastprovidername", havelastprovidername);
				commonModel.setBody(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel getWebpagekey(String processinstanceid) {
		CommonModel commonModel = new CommonModel();
		Map<String, String> map = new HashMap<String, String>();
		try {
			if(processinstanceid.isEmpty()){
				commonModel.setStatus("fail");
				commonModel.setMessage("传入主实例id不能为空");
				return commonModel;
			}
			INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
			quotetotalinfo.setTaskid(processinstanceid);
			quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
			if(quotetotalinfo!=null){
				if("1".equals(quotetotalinfo.getInsurancebooks())){
					commonModel.setStatus("success");
					commonModel.setMessage("操作成功");
					map.put("webpagekey", quotetotalinfo.getWebpagekey());
					commonModel.setBody(map);
				}else{
					commonModel.setStatus("success");
					commonModel.setMessage("操作成功");
					map.put("webpagekey", "3");
					commonModel.setBody(map);
				}
			}else{
				commonModel.setStatus("success");
				commonModel.setMessage("没有查到数据");
			}
		} catch (Exception e) {
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel addQuoteProviderWorkflowold(ChoiceProviderIdsModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if (StringUtil.isEmpty(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			List<String> params = model.getParamsList();
			if(null == params || params.size() <= 0){
				commonModel.setStatus("fail");
				commonModel.setMessage("还没有选择报价公司");
				return commonModel;
			}
			// 向报价信息表插入数据，一个报价公司一条
			Map<String, Object> param = new HashMap<String, Object>();
			List<String> ids = new ArrayList<String>();
			List<INSBQuoteinfo> insbQuoteinfos = new ArrayList<INSBQuoteinfo>();
			for(String data : params){
				if(!StringUtil.isEmpty(data)){
					String[] arra = data.split("#");
						if(arra.length != 4){
							continue;
						}
					ids.add(arra[1]);
					Map<String, String> map = new HashMap<String, String>();
					map.put("taskid", taskid);
					map.put("inscomcode", arra[1]);
					INSBQuoteinfo insbQuoteinfo = appInsuredQuoteDao.selectInsbQuoteInfoByTaskidAndPid(map);
					insbQuoteinfos.add(insbQuoteinfo);
					//调用规则判断承保限制条件
					param = getPriceParamWayStartSubflow(param, taskid, arra[1]);
				}
			}

			//==================调用工作流之前，初始化his数据===========
			createHisTableInit(taskid);

			LogUtil.info("新增供应商接口"+"addQuoteProviderWorkflow"+taskid+"新增的报价公司列表="+ids);
			//TODO验证
			//调用工作流，新增加的报价公司
			INSBWorkflowmain mainModel = insbWorkflowmainDao.selectByInstanceId(taskid);
			//投保流程
			String result = WorkFlowUtil.noticeWorkflowStartQuote(mainModel.getOperator(), taskid, param,"0");
			System.out.println("==========="+result);
			LogUtil.info("新增供应商接口调用工作流结束"+"addQuoteProviderWorkflow"+taskid+"新增的报价公司列表="+ids+"工作流返回值"+result);
			if(StringUtil.isEmpty(result)){
				commonModel.setStatus("fail");
				commonModel.setMessage("保存失败");
				return commonModel;
			}else{
				JSONObject jsonObj = JSONObject.fromObject(result);
				if(jsonObj.containsKey("message") && "fail".equals(jsonObj.getString("message"))){
					commonModel.setStatus("fail");
					commonModel.setMessage("保存失败");
					return commonModel;
				}else{
					for(INSBQuoteinfo quoteinfoo : insbQuoteinfos){
						//防止多次提交修改
						if(StringUtil.isEmpty(quoteinfoo.getWorkflowinstanceid())){
							Map<String, String> map = new HashMap<String, String>();
							map.put("workflowinstanceid", jsonObj.get(quoteinfoo.getInscomcode()).toString());
							map.put("id", quoteinfoo.getId());
							appInsuredQuoteDao.updateInsbQuoteinfoById(map);
						}
					}
					// 得到子流程id，开启多线程推流程
					LogUtil.info("获得子流程开启线程推流程taskid="+taskid+"开始时间="+new Date());
					for(INSBQuoteinfo quoteinfo : insbQuoteinfos){
						LogUtil.info("addQuoteProviderWorkflow"+taskid+"当前供应商="+quoteinfo.getInscomcode()+"开始时间="+new Date());
						taskthreadPool4workflow.execute(new Runnable() {
							@Override
							public void run() {
								Map<String,Object> param = new HashMap<String, Object>();
								param = getPriceParamWay(param, taskid, quoteinfo.getInscomcode(), "0");
								String callback = WorkFlowUtil.updateInsuredInfoNoticeWorkflow(jsonObj.get(quoteinfo.getInscomcode()).toString(), "admin", "子流程前置", param);
								LogUtil.info("子流程前置主流程addQuoteProviderWorkflow"+taskid+"开启子流程="+jsonObj.get(quoteinfo.getInscomcode()).toString()+"当前供应商="+quoteinfo.getInscomcode()+"返回结果="+callback);
							}
						});
					}
				}
			}
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
		} catch (Exception e) {
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel insuranceBookSearch(String taskid) {
		CommonModel commonModel = new CommonModel();
		try {
			if(StringUtil.isEmpty(taskid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("传入主实例id不能为空");
				return commonModel;
			}
			InsuranceBookModel bookModel = new InsuranceBookModel();
			SaveCarInfoModel carInfoModel = getSaveCarInfoModel(taskid);
			if(null != carInfoModel){
				bookModel.setFirstRegisterDate(carInfoModel.getFirstRegisterDate());
				bookModel.setVin(carInfoModel.getVin()); //车辆识别代号
				bookModel.setEngineNo(carInfoModel.getEngineNo()); //发动机号
				bookModel.setModelCode(carInfoModel.getModelCode());//品牌型号
				bookModel.setChgOwnerFlag(carInfoModel.getChgOwnerFlag()); //是否过户车
				bookModel.setChgOwnerDate(carInfoModel.getChgOwnerDate()); //是否过户车; //车辆过户日期
			}
			CarModelInfoModel carModelInfoModel = getCarModelInfoModel(taskid);
			if(null != carModelInfoModel){
				bookModel.setModelCode(carModelInfoModel.getModelCode());//品牌型号
				bookModel.setDisplacement(carModelInfoModel.getDisplacement());//排量
				bookModel.setApprovedLoad(carModelInfoModel.getApprovedLoad());//核载人数
				bookModel.setRulePriceProvideType(carModelInfoModel.getRulePriceProvideType());//车价选择
				bookModel.setInstitutionType(carModelInfoModel.getInstitutionType());//所属性质：个人用车，企业用车，机关团体用车
				bookModel.setUseProperty(carModelInfoModel.getUseProperty()); //车辆性质，试用性质
				bookModel.setTonnage(carModelInfoModel.getTonnage()); //核定载质量
				bookModel.setWholeWeight(carModelInfoModel.getWholeWeight());//整备质量
                bookModel.setCustomPrice(carModelInfoModel.getCustomPrice());//自定义车价
			}
			commonModel.setStatus("success");
            commonModel.setMessage("操作成功");
            commonModel.setBody(bookModel);
		} catch (Exception e) {
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel deleteUpdateImage(String processinstanceid, String fileid) {
		CommonModel commonModel = new CommonModel();
		try {
			if(StringUtil.isEmpty(processinstanceid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			INSBFilebusiness insbFilebusiness = new INSBFilebusiness();
			insbFilebusiness.setCode(processinstanceid);
			insbFilebusiness.setFilelibraryid(fileid);
			INSBFilebusiness filebusiness = insbFilebusinessDao.selectOne(insbFilebusiness);
			if(null != filebusiness){
				insbFilebusinessDao.deleteById(filebusiness.getId());
			}
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
		} catch (Exception e) {
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	/**
	 * 产地类型
	 * vin吗  大写的L  国产车  0 其他进口车 1
	 * @param vin
	 * @return
	 */
	private int getCarVehicleOriginByVin(String vin){
		int res = 0;
		if(StringUtil.isEmpty(vin)){
			return res;
		}
		char manufacturer = vin.charAt(0);
		if(manufacturer!='L'){
			res = 1;
		}
		return res;
	}

	/**
	 * 查询被保人+投保人+权益索赔人 信息
	 */
	@Override
	public CommonModel queryOtherPersonInfo(String taskid) {
		CommonModel commonModel = new CommonModel();
		AppAllPersonInfo allPersonInfo = new AppAllPersonInfo();

		try {
			if(StringUtil.isEmpty(taskid)){
				commonModel.setStatus("fail");
				commonModel.setMessage("传入主实例id不能为空");
				return commonModel;
			}
			//车主信息
			INSBCarowneinfo carowner = insbCarowneinfoDao.selectByTaskId(taskid);
			AppPerson carOwnerAppPerson = new AppPerson();
			if(null == carowner){
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id对应的车主信息不存在");
				return commonModel;
			}else {
				INSBPerson carOwnerPerson = insbPersonDao.selectById(carowner.getPersonid());
				carOwnerAppPerson.setTel(carOwnerPerson.getCellphone());
				carOwnerAppPerson.setName(carOwnerPerson.getName());
				carOwnerAppPerson.setEmail(carOwnerPerson.getEmail());
				carOwnerAppPerson.setCertNumber(carOwnerPerson.getIdcardno());
				carOwnerAppPerson.setCertificateType(String.valueOf(carOwnerPerson.getIdcardtype()));
			}


			//被保人信息
			INSBInsured insure = new INSBInsured();
			insure.setTaskid(taskid);
			INSBInsured insuredPerson =insbInsrueDao.selectOne(insure);
			if(null == insuredPerson){
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id对应的被保人信息不存在");
				return commonModel;
			} else {
				if (insuredPerson.getPersonid().equals(carowner.getPersonid())) {
					allPersonInfo.setSameCarownerInsured(true);
					allPersonInfo.setInsuredPerson(carOwnerAppPerson);
				}else {
					INSBPerson person = insbPersonDao.selectById(insuredPerson.getPersonid());
					AppPerson appPerson = new AppPerson();
					appPerson.setTel(person.getCellphone());
					appPerson.setName(person.getName());
					appPerson.setEmail(person.getEmail());
					appPerson.setCertNumber(person.getIdcardno());
					appPerson.setCertificateType(String.valueOf(person.getIdcardtype()));
					allPersonInfo.setSameCarownerInsured(false);
					allPersonInfo.setInsuredPerson(appPerson);
				}
			}


			//投保人信息
			INSBApplicant applicant =insbApplicantDao.selectByTaskID(taskid);
			if(null == applicant){
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id对应的投保人信息不存在");
				return commonModel;
			} else {
				if (applicant.getPersonid().equals(carowner.getPersonid())) {
					allPersonInfo.setSameCarownerapplicant(true);
					allPersonInfo.setApplicantPerson(carOwnerAppPerson);
				}else {
					INSBPerson person = insbPersonDao.selectById(applicant.getPersonid());
					AppPerson appPerson = new AppPerson();
					appPerson.setTel(person.getCellphone());
					appPerson.setName(person.getName());
					appPerson.setEmail(person.getEmail());
					appPerson.setCertNumber(person.getIdcardno());
					appPerson.setCertificateType(String.valueOf(person.getIdcardtype()));
					allPersonInfo.setSameCarownerapplicant(false);
					allPersonInfo.setApplicantPerson(appPerson);
				}
			}

			//权益索赔人信息
			INSBLegalrightclaim rightclaim =insbLegalrightclaimDao.selectByTaskID(taskid);
			if(null == rightclaim){
				/*commonModel.setStatus("fail");
				commonModel.setMessage("实例id对应的权益索赔人信息不存在");
				return commonModel;*/
				allPersonInfo.setSameCarownerRightclaim(true);
				allPersonInfo.setLegalrightclaimPerson(carOwnerAppPerson);
			} else {
				if (rightclaim.getPersonid().equals(carowner.getPersonid())) {
					allPersonInfo.setSameCarownerRightclaim(true);
					allPersonInfo.setLegalrightclaimPerson(carOwnerAppPerson);
				}else {
					INSBPerson person = insbPersonDao.selectById(rightclaim.getPersonid());
					AppPerson appPerson = new AppPerson();
					appPerson.setTel(person.getCellphone());
					appPerson.setName(person.getName());
					appPerson.setEmail(person.getEmail());
					appPerson.setCertNumber(person.getIdcardno());
					appPerson.setCertificateType(String.valueOf(person.getIdcardtype()));
					allPersonInfo.setSameCarownerRightclaim(false);
					allPersonInfo.setLegalrightclaimPerson(appPerson);
				}
			}

			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(allPersonInfo);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel queryInsureInfoHis(String processinstanceid,
			String inscomcode) {
		CommonModel commonModel = new CommonModel();
		if(null == processinstanceid || "".equals(processinstanceid)){
			commonModel.setStatus("fail");
			commonModel.setMessage("实例id不能为空");
			return commonModel;
		}
		AppInsureinfoBean bean = new AppInsureinfoBean();
		// 商业险
		AppBusiness business = null;
		// 交强险
		AppBusiness compulsoryBusiness = null ;
		//车辆信息
		AppCarInfo carInfo = null;
		// 投保人
		AppPerson insurePersion = null;
		//权益索赔人
		AppPerson legalrightclaim = null;
		// 车主
		AppPerson carowerinfo = null;
		//被保人
		AppPerson passivePersion = null;
		//发票信息
		INSBInvoiceinfo invoiceInfo=null;
		try {
			//=========================Tops 所有人员信息，his表没有默认取主表信息======================
			//  发票信息
			INSBInvoiceinfo invoice = new INSBInvoiceinfo();
			invoice.setTaskid(processinstanceid);
			invoiceInfo=insbInvoiceinfoDao.selectOne(invoice);
			if(invoiceInfo!=null){
				bean.setInvoiceinfo(invoiceInfo);
			}
			//			被保人
			INSBInsuredhis beibaoren = new INSBInsuredhis();
			beibaoren.setTaskid(processinstanceid);
			beibaoren.setInscomcode(inscomcode);
			INSBInsuredhis insured = insbInsuredhisDao.selectOne(beibaoren);
			if(insured!=null){
				INSBPerson p = insbPersonDao.selectById(insured.getPersonid());
				if(p!=null){
					passivePersion = new AppPerson();//被保人
					passivePersion.setName(p.getName());
					passivePersion.setCertificateType(String.valueOf(p.getIdcardtype()));
					passivePersion.setCertNumber(p.getIdcardno());
					passivePersion.setEmail(p.getEmail());
					passivePersion.setTel(p.getCellphone());
				}
			}else{
				INSBInsured insbInsured = new INSBInsured();
				insbInsured.setTaskid(processinstanceid);
				INSBInsured minsured = insbInsuredDao.selectOne(insbInsured);
				if(minsured!=null){
					INSBPerson p = insbPersonDao.selectById(minsured.getPersonid());
					if(p!=null){
						passivePersion = new AppPerson();//被保人
						passivePersion.setName(p.getName());
						passivePersion.setCertificateType(String.valueOf(p.getIdcardtype()));
						passivePersion.setCertNumber(p.getIdcardno());
						passivePersion.setEmail(p.getEmail());
						passivePersion.setTel(p.getCellphone());
					}
				}
			}
//			投保人
			INSBApplicanthis toubaoren = new INSBApplicanthis();
			toubaoren.setTaskid(processinstanceid);
			toubaoren.setInscomcode(inscomcode);
			INSBApplicanthis applicant = insbApplicanthisDao.selectOne(toubaoren);
			if(applicant!=null){
				INSBPerson p = insbPersonDao.selectById(applicant.getPersonid());
				if(p!=null){
					insurePersion = new AppPerson();//投保人
					insurePersion.setName(p.getName());
					insurePersion.setCertificateType(String.valueOf(p.getIdcardtype()));
					insurePersion.setCertNumber(p.getIdcardno());
					insurePersion.setEmail(p.getEmail());
					insurePersion.setTel(p.getCellphone());
				}
			}else{
				INSBApplicant insbApplicant = new INSBApplicant();
				insbApplicant.setTaskid(processinstanceid);
				INSBApplicant mapplicant = insbApplicantDao.selectOne(insbApplicant);
				if(mapplicant!=null){
					INSBPerson p = insbPersonDao.selectById(mapplicant.getPersonid());
					if(p!=null){
						insurePersion = new AppPerson();//投保人
						insurePersion.setName(p.getName());
						insurePersion.setCertificateType(String.valueOf(p.getIdcardtype()));
						insurePersion.setCertNumber(p.getIdcardno());
						insurePersion.setEmail(p.getEmail());
						insurePersion.setTel(p.getCellphone());
					}
				}
			}
//			车主
			INSBCarowneinfo chezhu = new INSBCarowneinfo();
			chezhu.setTaskid(processinstanceid);
			INSBCarowneinfo carowneinfo = insbCarowneinfoDao.selectOne(chezhu);
			if(carowneinfo!=null){
				INSBPerson p = insbPersonDao.selectById(carowneinfo.getPersonid());
				if(p!=null){
					carowerinfo = new AppPerson();//车主
					carowerinfo.setName(p.getName());
					carowerinfo.setCertificateType(String.valueOf(p.getIdcardtype()));
					carowerinfo.setCertNumber(p.getIdcardno());
					carowerinfo.setEmail(p.getEmail());
					carowerinfo.setTel(p.getCellphone());
				}
			}

//			权益索赔人
			INSBLegalrightclaimhis insbLegalrightclaim = new INSBLegalrightclaimhis();
			insbLegalrightclaim.setTaskid(processinstanceid);
			insbLegalrightclaim.setInscomcode(inscomcode);
			INSBLegalrightclaimhis insblegalrightclaim = insbLegalrightclaimhisDao.selectOne(insbLegalrightclaim);
			if(insblegalrightclaim!=null){
				INSBPerson p = insbPersonDao.selectById(insblegalrightclaim.getPersonid());
				if(p!=null){
					legalrightclaim = new AppPerson();//投保人
					legalrightclaim.setName(p.getName());
					legalrightclaim.setCertificateType(String.valueOf(p.getIdcardtype()));
					legalrightclaim.setCertNumber(p.getIdcardno());
					legalrightclaim.setEmail(p.getEmail());
					legalrightclaim.setTel(p.getCellphone());
				}
			}else{
				INSBLegalrightclaim minsbLegalrightclaim = new INSBLegalrightclaim();
				minsbLegalrightclaim.setTaskid(processinstanceid);
				INSBLegalrightclaim mlegalrightclaim = insbLegalrightclaimDao.selectOne(minsbLegalrightclaim);
				if(mlegalrightclaim!=null){
					INSBPerson p = insbPersonDao.selectById(mlegalrightclaim.getPersonid());
					if(p!=null){
						legalrightclaim = new AppPerson();//投保人
						legalrightclaim.setName(p.getName());
						legalrightclaim.setCertificateType(String.valueOf(p.getIdcardtype()));
						legalrightclaim.setCertNumber(p.getIdcardno());
						legalrightclaim.setEmail(p.getEmail());
						legalrightclaim.setTel(p.getCellphone());
					}
				}
			}

//			商业险、交强险
			//查询历史表
			INSBCarinfohis insbCarinfohis = new INSBCarinfohis();//车辆信息表
			insbCarinfohis.setTaskid(processinstanceid);
			insbCarinfohis.setInscomcode(inscomcode);
			INSBCarinfohis carinfohis = insbCarinfohisDao.selectOne(insbCarinfohis);
			if(null != carinfohis){
				carInfo = new AppCarInfo();//车辆信息
				carInfo.setModelCode(carinfohis.getStandardfullname());
				carInfo.setEngineNo(ModelUtil.hiddenNumber(carinfohis.getEngineno()));
				carInfo.setVin(ModelUtil.hiddenNumber(carinfohis.getVincode()));
				carInfo.setIstransfer(StringUtil.isEmpty(carinfohis.getIsTransfercar()) || carinfohis.getIsTransfercar().equals("0")?false:true);
				carInfo.setProperty(carinfohis.getProperty());
				carInfo.setCarproperty(carinfohis.getCarproperty());
				if(carinfohis.getTransferdate()!=null){
				   carInfo.setChgOwnerDate(ModelUtil.conbertToString(carinfohis.getTransferdate()));
				}
				if(carinfohis.getRegistdate()!=null){
				   carInfo.setFirstRegisterDate(ModelUtil.conbertToString(carinfohis.getRegistdate()));
				}
				//上年商业承保公司
				bean.setLastComId(carinfohis.getPreinscode());
				bean.setDrivingRegion(carinfohis.getDrivingarea());
			}else{
				//查询主表
				INSBCarinfo insbCarinfo = new INSBCarinfo();//车辆信息表
				insbCarinfo.setTaskid(processinstanceid);
				INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
				if(carinfo!=null){
					carInfo = new AppCarInfo();//车辆信息
					carInfo.setModelCode(carinfo.getStandardfullname());
				    carInfo.setEngineNo(ModelUtil.hiddenNumber(carinfo.getEngineno()));
				    carInfo.setVin(ModelUtil.hiddenNumber(carinfo.getVincode()));
				    carInfo.setIstransfer(StringUtil.isEmpty(carinfo.getIsTransfercar()) || carinfo.getIsTransfercar().equals("0")?false:true);
				    carInfo.setProperty(carinfo.getProperty());
				    carInfo.setCarproperty(carinfo.getCarproperty());
				    if(carinfo.getTransferdate()!=null){
				    	carInfo.setChgOwnerDate(ModelUtil.conbertToString(carinfo.getTransferdate()));
				    }
				    if(carinfo.getRegistdate()!=null){
				    	carInfo.setFirstRegisterDate(ModelUtil.conbertToString(carinfo.getRegistdate()));
				    }
				  //上年商业承保公司
				    bean.setLastComId(carinfo.getPreinscode());
					bean.setDrivingRegion(carinfo.getDrivingarea());
				}
			}
			//商业险。交强险时间
			INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
			insbPolicyitem.setTaskid(processinstanceid);
			insbPolicyitem.setInscomcode(inscomcode);
			List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(insbPolicyitem);
			business = new AppBusiness();//商业险
			compulsoryBusiness = new AppBusiness();//交强险
			if(null != insbPolicyitems && insbPolicyitems.size() > 0){
				for(INSBPolicyitem policyitem : insbPolicyitems){
					if("0".equals(policyitem.getRisktype())){
						business.setRiskstartdate(ModelUtil.conbertToString(policyitem.getStartdate()));
						business.setRiskenddate(ModelUtil.conbertToString(policyitem.getEnddate()));
					}else{
						compulsoryBusiness.setRiskstartdate(ModelUtil.conbertToString(policyitem.getStartdate()));
						compulsoryBusiness.setRiskenddate(ModelUtil.conbertToString(policyitem.getEnddate()));
					}
				}
			}
			// 商业险
			bean.setProcessinstanceid(processinstanceid);
			bean.setPassiveInsurePersion(passivePersion);
			bean.setInsurePersion(insurePersion);
			bean.setCarowerinfo(carowerinfo);
			bean.setLegalrightclaim(legalrightclaim);
			bean.setBusinessInsureddate(business);
			bean.setTrafficInsureddate(compulsoryBusiness);
			bean.setCarinfo(carInfo);
			//当前结点状态
			bean.setFlowflag(queryWorkflowStatus(processinstanceid, inscomcode));
			//备注类型
			bean.setRemartsmap(remarkTypeFromCode("0"));
			//查询备注信息
			//默认不存在备注信息
			bean.setRemarkcode("");
			bean.setRemark("");
			INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
			quotetotalinfo.setTaskid(processinstanceid);
			quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
			INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
			quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
			quoteinfo.setInscomcode(inscomcode);
			quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
			INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();
			insbWorkflowsub.setMaininstanceid(processinstanceid);
			insbWorkflowsub.setInstanceid(quoteinfo.getWorkflowinstanceid());
			INSBWorkflowsub workflowsub = insbWorkflowsubDao.selectOne(insbWorkflowsub);
			if(null != workflowsub){
				INSBWorkflowsubtrack insbWorkflowsubtrack = new INSBWorkflowsubtrack();
				insbWorkflowsubtrack.setMaininstanceid(processinstanceid);
				insbWorkflowsubtrack.setInstanceid(quoteinfo.getWorkflowinstanceid());
				insbWorkflowsubtrack.setTaskcode(workflowsub.getTaskcode());
				INSBWorkflowsubtrack workflowsubtrack = insbWorkflowsubtrackDao.selectOne(insbWorkflowsubtrack);
				if(null != workflowsubtrack && StringUtil.isNotEmpty(workflowsubtrack.getId())){
					INSBUsercomment insbUsercomment = new INSBUsercomment();
					insbUsercomment.setTrackid(workflowsubtrack.getId());//任务轨迹id
					insbUsercomment.setTracktype(2);//1：主流程 2：子流程
					INSBUsercomment usercomment = insbUsercommentDao.selectOne(insbUsercomment);
					if(null != usercomment){
						bean.setRemarkcode(usercomment.getCommentcontenttype()+"");
						bean.setRemark(usercomment.getCommentcontent());
					}
				}
			}

			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(bean);

		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public CommonModel queryClaiminfo(String processinstanceid) {
		CommonModel commonModel = new CommonModel();
		if(StringUtil.isEmpty(processinstanceid)){
			commonModel.setStatus("fail");
			commonModel.setMessage("实例id不能为空");
			return commonModel;
		}
		try {
			INSBCarinfo insbCarinfo = new INSBCarinfo();
			insbCarinfo.setTaskid(processinstanceid);
			INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
			if(!StringUtil.isEmpty(carinfo.getCarlicenseno()) && !"新车未上牌".equals(carinfo.getCarlicenseno()) && "0".equals(carinfo.getIsNew())){
				//获取报价区域
				INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
				insbQuotetotalinfo.setTaskid(processinstanceid);
				INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
				if(null == quotetotalinfo){
					commonModel.setStatus("fail");
					commonModel.setMessage("报价总表信息不存在");
					return commonModel;
				}
				//根据车牌平台查询
				LastYearPolicyInfoBean lastYearPolicyInfoBean = queryLastInsuredInfoByCarinfo(processinstanceid,carinfo,quotetotalinfo.getInscitycode(),quotetotalinfo.getAgentnum(),quotetotalinfo.getInsprovincecode());

				if(null != lastYearPolicyInfoBean && "0".equals(lastYearPolicyInfoBean.getStatus())){
					LogUtil.info("车辆信息轮询查询平台返回成功"+processinstanceid+"状态="+lastYearPolicyInfoBean.getStatus());
					//saveLastYearClaimsInfo(processinstanceid, quotetotalinfo.getOperator(), lastYearPolicyInfoBean);
					//返回值放入ridis,key值 实例id
					cachelastYearPolicyInfo(processinstanceid, lastYearPolicyInfoBean);
				}
			}
//			INSBLastyearinsureinfo insbLastyearinsureinfo = new INSBLastyearinsureinfo();
//			insbLastyearinsureinfo.setTaskid(processinstanceid);
//			insbLastyearinsureinfo.setSflag("2");//平台回写的为主
			INSBLastyearinsureinfo lastyearinsureinfo = insbRulequerycarinfoDao.queryLastYearClainInfo(processinstanceid);
			if(null == lastyearinsureinfo){
				commonModel.setBody(false);
			}else{
				commonModel.setBody(true);
			}
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
            commonModel.setStatus("fail");
            commonModel.setMessage("操作失败");
		}
		return commonModel;
	}
	/**
	 * 生成his表数据初始化
	 * @param taskid
	 */
	public void createHisTableInit(String taskid){
		try {
			LogUtil.info("初始化his表数据开始"+taskid);
			//车辆信息
			INSBCarinfo insbCarinfo = insbCarinfoDao.selectCarinfoByTaskId(taskid);
			//被保人
			INSBInsured insbInsured = insbInsuredDao.selectInsuredByTaskId(taskid);
			//投保人
			INSBApplicant insbApplicant = insbApplicantDao.selectByTaskID(taskid);
			//权益索赔人
			INSBLegalrightclaim insbLegalrightclaim = insbLegalrightclaimDao.selectByTaskID(taskid);
			//联系人
			INSBRelationperson insbRelationperson = insbRelationpersonDao.selectByTaskID(taskid);
			INSBPerson oldPerson = new INSBPerson();
			INSBPerson newPerson = new INSBPerson();
            //每个供应商
			List<INSBQuoteinfo> insbQuoteinfos = appInsuredQuoteDao.getSelectedProvidersByTaskid(taskid);
			if(null != insbQuoteinfos && insbQuoteinfos.size() > 0){
				for(INSBQuoteinfo quoteinfo : insbQuoteinfos){
                    if (insbCarinfo != null) {
                        //insbcarinfohis 生成
                        INSBCarinfohis insbCarinfohis = new INSBCarinfohis();
                        insbCarinfohis.setTaskid(taskid);
                        insbCarinfohis.setInscomcode(quoteinfo.getInscomcode());
                        INSBCarinfohis carinfohis = insbCarinfohisDao.selectOne(insbCarinfohis);
                        //LogUtil.info("生成INSBCarinfohis表开始" + taskid + ",供应商id=" + quoteinfo.getInscomcode());
                        if (null == carinfohis) {
                            //根据原始insbperson表数据新增记录
                            oldPerson=insbPersonDao.selectById(insbCarinfo.getOwner());
                            newPerson=insbPersonHelpService.repairPerson(newPerson,oldPerson,insbCarinfo.getOperator());
                    		newPerson.setId(UUIDUtils.create());
                            insbPersonDao.insert(newPerson);
                            //插入新的数据
                            carinfohis = new INSBCarinfohis();
                            PropertyUtils.copyProperties(carinfohis, insbCarinfo);
                            carinfohis=insbPersonHelpService.addINSBCarinfohis(carinfohis, new Date(), 
                            		insbCarinfo.getOperator(), taskid, quoteinfo.getInscomcode(), newPerson.getId());
                        }
                        LogUtil.info("生成INSBCarinfohis表结束" + taskid + ",供应商id=" + quoteinfo.getInscomcode());
                    }

                    if (insbInsured != null) {
                        //INSBInsuredhis 被保人表
                        INSBInsuredhis insbInsuredhis = new INSBInsuredhis();
                        insbInsuredhis.setTaskid(taskid);
                        insbInsuredhis.setInscomcode(quoteinfo.getInscomcode());
                        INSBInsuredhis insuredhis = insbInsuredhisDao.selectOne(insbInsuredhis);
                        //LogUtil.info("生成INSBInsuredhis表开始" + taskid + ",供应商id=" + quoteinfo.getInscomcode());
                        if (null == insuredhis) {
                        	insbPersonHelpService.addPersonHisIsNull(insbInsured,taskid, quoteinfo.getInscomcode(), ConstUtil.STATUS_2);
                        }
                        LogUtil.info("生成INSBInsuredhis表结束" + taskid + ",供应商id=" + quoteinfo.getInscomcode());
                    }

                    if (insbApplicant != null) {
                        //insbapplicanthis 投保人表
                        INSBApplicanthis insbApplicanthis = new INSBApplicanthis();
                        insbApplicanthis.setTaskid(taskid);
                        insbApplicanthis.setInscomcode(quoteinfo.getInscomcode());
                        INSBApplicanthis applicanthis = insbApplicanthisDao.selectOne(insbApplicanthis);
                        //LogUtil.info("生成INSBApplicanthis表开始" + taskid + ",供应商id=" + quoteinfo.getInscomcode());
                        if (null == applicanthis) {
                            insbPersonHelpService.addPersonHisIsNull(insbApplicant,taskid, quoteinfo.getInscomcode(), ConstUtil.STATUS_1);
                        }
                        LogUtil.info("生成INSBApplicanthis表结束" + taskid + ",供应商id=" + quoteinfo.getInscomcode());
                    }

                    if (insbLegalrightclaim != null) {
                        //insblegalrightclaimhis 权益索赔人
                        INSBLegalrightclaimhis insbLegalrightclaimhis = new INSBLegalrightclaimhis();
                        insbLegalrightclaimhis.setTaskid(taskid);
                        insbLegalrightclaimhis.setInscomcode(quoteinfo.getInscomcode());
                        INSBLegalrightclaimhis legalrightclaimhis = insbLegalrightclaimhisDao.selectOne(insbLegalrightclaimhis);
                       // LogUtil.info("生成INSBLegalrightclaimhis表开始" + taskid + ",供应商id=" + quoteinfo.getInscomcode());
                        if (null == legalrightclaimhis) {
                            insbPersonHelpService.addPersonHisIsNull(insbLegalrightclaim,taskid, quoteinfo.getInscomcode(), ConstUtil.STATUS_3);
                        }
                        LogUtil.info("生成INSBLegalrightclaimhis表结束" + taskid + ",供应商id=" + quoteinfo.getInscomcode());
                    }

                    if (insbRelationperson != null) {
                        //insbrelationpersonhis 联系人
                        INSBRelationpersonhis insbRelationpersonhis = new INSBRelationpersonhis();
                        insbRelationpersonhis.setTaskid(taskid);
                        insbRelationpersonhis.setInscomcode(quoteinfo.getInscomcode());
                        INSBRelationpersonhis relationpersonhis = insbRelationpersonhisDao.selectOne(insbRelationpersonhis);
                        //LogUtil.info("生成INSBRelationpersonhis表开始" + taskid + ",供应商id=" + quoteinfo.getInscomcode());
                        if (null == relationpersonhis) {
                            insbPersonHelpService.addPersonHisIsNull(insbRelationperson,taskid, quoteinfo.getInscomcode(), ConstUtil.STATUS_4);
                        }
                        LogUtil.info("生成INSBRelationpersonhis表结束" + taskid + ",供应商id=" + quoteinfo.getInscomcode());
                    }
                   LogUtil.info("");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
            LogUtil.info("生成his表失败了" + taskid + ": " + e.getMessage());
		}
	}
	
	/**
	 * 获取保单列表
	 * @param taskid 工作流号
	 * @param inscomcode 供应商code
	 * @return
	 */
	public List<INSBPolicyitem> getINSBPolicyItemList(String taskid,String inscomcode){
		INSBPolicyitem queryInsbPolicyitem = new INSBPolicyitem();
		queryInsbPolicyitem.setTaskid(taskid);
		queryInsbPolicyitem.setInscomcode(inscomcode);
		return insbPolicyitemService.queryList(queryInsbPolicyitem);
	}

	@Override
	public CommonModel checkAddQuoteProviderRole(CheckAddQuoteProviderRoleModel model) {
		LogUtil.info("新增报价公司checkAddQuoteProviderRole"+model.getProcessinstanceid()+"验证保险配置调用规则");
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if (null == taskid && "".equals(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			// 已选择的报价公司（报价信息总表，报价信息表）
			INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
			quotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
			if (null == insbQuotetotalinfo) {
				commonModel.setStatus("fail");
				commonModel.setMessage("报价信息总表不存在");
				return commonModel;
			}
			List<String> newproids = model.getNewproids();
			if(null == newproids || newproids.size() <= 0){
				commonModel.setStatus("fail");
				commonModel.setMessage("没有需要新增的供应商");
				return commonModel;
			}
			Map<String, List<String>> mapList = new HashMap<String, List<String>>();
			//只查总表
			INSBCarconfig insbcarconfig = new INSBCarconfig();
			insbcarconfig.setTaskid(taskid);
			List<INSBCarconfig> insbCarconfigs = insbCarconfigDao.selectList(insbcarconfig);
			for(INSBCarconfig carconfig : insbCarconfigs){
				List<String> provids = new ArrayList<String>();
				for(String pid : newproids){
					//只校验商业险及不计免赔险
					if("0".equals(carconfig.getInskindtype()) || "1".equals(carconfig.getInskindtype())){
						//根据险别编码，和供应商id查询供应商是否支持该先别
						long num = appInsuredQuoteDao.verificationInsuredConfig(carconfig.getInskindcode(),pid.substring(0, 4));
						if(num <= 0){
							provids.add(pid);
						}
					}
				}
				if(provids.size() > 0){
					mapList.put(carconfig.getInskindcode(), provids);
				}
			}
			List<VerificationConfigBean> configBeans = new ArrayList<VerificationConfigBean>();
			for(Entry<String, List<String>> entry : mapList.entrySet()){
				VerificationConfigBean verificationConfigBean = new VerificationConfigBean();
				INSBRiskkindconfig insbRiskkindconfig = new INSBRiskkindconfig();
				insbRiskkindconfig.setRiskkindcode(entry.getKey());
				INSBRiskkindconfig riskkindconfig = insbRiskkindconfigDao.selectOne(insbRiskkindconfig);
				if(null != riskkindconfig){
					verificationConfigBean.setKindcode(riskkindconfig.getRiskkindcode());
					verificationConfigBean.setKindname(riskkindconfig.getRiskkindname());
				}
				List<Map<String, String>> providers = new ArrayList<Map<String,String>>();
				List<String> pids = entry.getValue();
				for(String id : pids){
					INSBProvider insbProvider = insbProviderDao.selectById(id);
					Map<String, String> map = new HashMap<String, String>();
					if(null != insbProvider){
						map.put("prvshotname", insbProvider.getPrvshotname());
						map.put("prvname", insbProvider.getPrvname());
						map.put("logo", insbProvider.getLogo());
					}
					providers.add(map);
				}
				if(providers.size() > 0){
					verificationConfigBean.setProviders(providers);
				}
				configBeans.add(verificationConfigBean);
			}
			//非费改判断是否非该区域
//			Map<String, Object> map = getChangeFee(insbQuotetotalinfo.getAgentnum(), null);
//			boolean isnonfeereform = false;
//			if(!map.isEmpty()){
//				isnonfeereform = (boolean) map.get("isfeeflag");
//			}

			//调用规则
			List<GuiZeCheckBean> guiZeCheckBeans = new ArrayList<GuiZeCheckBean>();
			CheckInsuredConfigModel checkInsuredConfigModel = new CheckInsuredConfigModel();
			boolean flag = false;
			for (String pid : newproids) {
				INSBProvider insbProvider = insbProviderDao.selectById(pid);
				//判断提前报价时间是否在许可范围内
				String judgeResult = "";
//				if(null != insbProvider.getAdvancequote() && 0 != insbProvider.getAdvancequote()){
//					judgeResult = judgeAdvanceInsuranceDate(taskid,pid,insbProvider.getAdvancequote());
//				}
				//没有匹配到
//				if(!saveCarmodelinfoToData(taskid, pid, "")){
//					LogUtil.info("新增报价公司没有匹配到年款车型"+model.getProcessinstanceid()+"=====供应商="+pid);
//					//judgeResult += "[该指定车价超出浮动范围]";
//					saveFlowerrorToManWork(taskid, pid,"提示年款无法匹配，转人工处理");
//				}
				//bug 2432 20160620 年款匹配不到转人工，jycode。rbcode获取不到转人工
				saveCarmodelinfoToData(taskid, pid, "");
				//南枫平台如果平台查询没返回日期，直接转人工，混保有一个没查到就转人工，新车不管
				//南枫平台新需求 20160628 bug2400
//				if(chudanDeptisNanFengDept(taskid, pid)){
//					resetInsuredDate(taskid, pid);
//				}
//
//				//非费改地区校验，辽宁
//				if(isnonfeereform){
//					judgeResult += checkNonFeereform(taskid, pid);
//				}

				LogUtil.info("新增报价公司规则调用开始"+model.getProcessinstanceid()+"=====供应商="+pid);
				String result = appQuotationService.getQuotationValidatedInfo("",taskid,pid);
                LogUtil.info("新增报价公司规则调用出参"+model.getProcessinstanceid()+"==="+result);

				JSONObject jsonObject = JSONObject.fromObject(result);

				//{"success":false,"quotationMode":0,"resultMsg":["商业险、交强起保日期不一致需分开提交"]}   提示性规则:0,转人工规则:1,限制性规则:2,阻断性规则:3
				if(!jsonObject.getBoolean("success") || !StringUtil.isEmpty(judgeResult)){//规则校验没通过 或者开始报价时间大于规定的时间间隔
					if(null != insbProvider){
						if((!jsonObject.getBoolean("success") && jsonObject.containsKey("quotationMode")  && "3".equals(jsonObject.get("quotationMode").toString())) || !StringUtil.isEmpty(judgeResult)){
							GuiZeCheckBean guiZeCheckBean = new GuiZeCheckBean();
							guiZeCheckBean.setPid(insbProvider.getId());
							guiZeCheckBean.setPrvname(insbProvider.getPrvname());
							guiZeCheckBean.setLogo(insbProvider.getLogo());
							guiZeCheckBean.setPrvshotname(insbProvider.getPrvshotname());
							judgeResult = jsonObject.getString("resultMsg")+judgeResult;
							guiZeCheckBean.setReason(judgeResult);
							guiZeCheckBeans.add(guiZeCheckBean);
							//阻断规则，删除表中记录
							if("3".equals(jsonObject.get("quotationMode").toString())){
								deleteGuiZeFailProviderData(taskid, insbQuotetotalinfo.getId(), pid);
							}
						}else{//规则校验通过
							flag = true;
						}
					}
				}else{//规则校验通过
					flag = true;
				}
			}
			checkInsuredConfigModel.setFlag(flag);
			checkInsuredConfigModel.setConfigBeans(configBeans);
			checkInsuredConfigModel.setGuiZeCheckBeans(guiZeCheckBeans);
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(checkInsuredConfigModel);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	/**
	 * 新增供应商删除阻断规则提示的供应商报价信息
	 * @param taskid 任务id
	 * @param quotetotalinfoid  报价总表id
	 * @param inscomcode  供应商id
	 */
	private void deleteGuiZeFailProviderData(String taskid,String quotetotalinfoid, String inscomcode) {
		LogUtil.info("deleteGuiZeFailProviderData" + taskid + "=====规则校验没通过的供应商删除报价信息,总表id=" + quotetotalinfoid);
		// 删除保险公司险别报价表
		INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
		insbCarkindprice.setTaskid(taskid);
		insbCarkindprice.setInscomcode(inscomcode);
		List<INSBCarkindprice> carkindprices = insbCarkindpriceDao.selectList(insbCarkindprice);
		if (null != carkindprices && carkindprices.size() > 0) {
			LogUtil.info("规则校验没通过删除已有的险别分表" + taskid + "操作人=规则");
			for (INSBCarkindprice carkindprice : carkindprices) {
				insbCarkindpriceDao.deleteById(carkindprice.getId());
			}
		}
		// 报价信息表
		INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
		insbQuoteinfo.setInscomcode(inscomcode);
		insbQuoteinfo.setQuotetotalinfoid(quotetotalinfoid);
		INSBQuoteinfo quoteinfo = insbQuoteinfoDao.selectOne(insbQuoteinfo);
		if (null != quoteinfo) {
			LogUtil.info("规则校验没通过删除已有报价信息表数据" + taskid + "操作人=规则");
			insbQuoteinfoDao.deleteById(quoteinfo.getId());
		}
	}

	@Override
	public CommonModel addQuoteProviderWorkflow(
			CheckAddQuoteProviderRoleModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			if (StringUtil.isEmpty(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			List<String> newproids = model.getNewproids();
			if(null == newproids || newproids.size() <= 0){
				commonModel.setStatus("fail");
				commonModel.setMessage("还没有选择报价公司");
				return commonModel;
			}

			//删除规则校验没通过的报价公司
			List<String> needdels = model.getNeeddels();
			if(null != needdels && needdels.size() > 0){
				deleteGuiZeFailProviders(taskid,needdels);
				//从所有的供应商id中去掉规则不通过的
				for(String str : needdels){
					if(newproids.contains(str)){
						newproids.remove(str);
					}
				}
			}

			// 向报价信息表插入数据，一个报价公司一条
			Map<String, Object> param = new HashMap<String, Object>();
			List<String> ids = new ArrayList<String>();
			List<INSBQuoteinfo> insbQuoteinfos = new ArrayList<INSBQuoteinfo>();
			for(String pid : newproids){
				if(!StringUtil.isEmpty(pid)){
					Map<String, String> map = new HashMap<String, String>();
					map.put("taskid", taskid);
					map.put("inscomcode", pid);
					ids.add(pid);
					INSBQuoteinfo insbQuoteinfo = appInsuredQuoteDao.selectInsbQuoteInfoByTaskidAndPid(map);
					insbQuoteinfos.add(insbQuoteinfo);
					//调用规则判断承保限制条件
					param = getPriceParamWayStartSubflow(param, taskid, pid);
				}
			}

			if(null == ids || ids.size() <= 0){
				commonModel.setStatus("fail");
				commonModel.setMessage("数据有问题，请重新操作");
				return commonModel;
			}

			//==================调用工作流之前，初始化his数据===========
			createHisTableInit(taskid);

			LogUtil.info("新增供应商接口"+"addQuoteProviderWorkflow"+taskid+"新增的报价公司列表="+ids);
			//TODO验证
			//调用工作流，新增加的报价公司
			INSBWorkflowmain mainModel = insbWorkflowmainDao.selectByInstanceId(taskid);
			//投保流程
			String result = WorkFlowUtil.noticeWorkflowStartQuote(mainModel.getOperator(), taskid, param,"0");
			System.out.println("==========="+result);
			LogUtil.info("新增供应商接口调用工作流结束"+"addQuoteProviderWorkflow"+taskid+"新增的报价公司列表="+ids+"工作流返回值"+result);
			if(StringUtil.isEmpty(result)){
				commonModel.setStatus("fail");
				commonModel.setMessage("保存失败");
				return commonModel;
			}else{
				JSONObject jsonObj = JSONObject.fromObject(result);
				if(jsonObj.containsKey("message") && "fail".equals(jsonObj.getString("message"))){
					commonModel.setStatus("fail");
					commonModel.setMessage("保存失败");
					return commonModel;
				}else{
					for(INSBQuoteinfo quoteinfoo : insbQuoteinfos){
						//防止多次提交修改
						if(StringUtil.isEmpty(quoteinfoo.getWorkflowinstanceid())){
							Map<String, String> map = new HashMap<String, String>();
							map.put("workflowinstanceid", jsonObj.get(quoteinfoo.getInscomcode()).toString());
							map.put("id", quoteinfoo.getId());
							appInsuredQuoteDao.updateInsbQuoteinfoById(map);
						}
					}
					// 得到子流程id，开启多线程推流程
					LogUtil.info("获得子流程开启线程推流程taskid="+taskid+"开始时间="+new Date());
					for(INSBQuoteinfo quoteinfo : insbQuoteinfos){
						LogUtil.info("addQuoteProviderWorkflow"+taskid+"当前供应商="+quoteinfo.getInscomcode()+"开始时间="+new Date());
						taskthreadPool4workflow.execute(new Runnable() {
							@Override
							public void run() {
								Map<String,Object> param = new HashMap<String, Object>();
								param = getPriceParamWay(param, taskid, quoteinfo.getInscomcode(), "0");
								String callback = WorkFlowUtil.updateInsuredInfoNoticeWorkflow(jsonObj.get(quoteinfo.getInscomcode()).toString(), "admin", "子流程前置", param);
								LogUtil.info("子流程前置主流程addQuoteProviderWorkflow"+taskid+"开启子流程="+jsonObj.get(quoteinfo.getInscomcode()).toString()+"当前供应商="+quoteinfo.getInscomcode()+"返回结果="+callback);
							}
						});
					}
				}
			}
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
		} catch (Exception e) {
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}
	/**
	 * 20160616 南枫平台如果平台查询没返回日期，直接转人工，混保有一个没查到就转人工，新车不管
	 * bug 2400
	 * 任务 104 20160621
	 * @param taskid
	 * @param inscomcode
	 * @return true 需要转人工
	 */
	public boolean isNanFengDept(String taskid, String inscomcode) {
		boolean result = false;
		INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setTaskid(taskid);
		INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
		if(null == carinfo){
			result = true;
		}else{
			if(!"新车未上牌".equals(carinfo.getCarlicenseno()) && "0".equals(carinfo.getIsNew())){
				INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
				insbQuotetotalinfo.setTaskid(taskid);
				INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
				if(null != quotetotalinfo){
					INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
					insbQuoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
					insbQuoteinfo.setInscomcode(inscomcode);
					INSBQuoteinfo quoteinfo = insbQuoteinfoDao.selectOne(insbQuoteinfo);
					if(null != quoteinfo){
						INSCDept inscDept = inscDeptDao.selectById(quoteinfo.getDeptcode());
						//广东南枫平台
						if(null != inscDept && inscDept.getParentcodes().contains("1244000000")){
							result = isHaveCifBackDate(taskid, inscomcode);
						}
					}
				}
			}
		}
		return result;
	}
	/**
	 * 只针对广东南枫平台
	 * 平台返回的上年投保时间
	 * 混保时，交强商业上年投保时间有一个没返回就转人工
	 * 单保时，平台没返回时间转人工
	 * @param taskid
	 * @param inscomcode
	 * @return true 转人工
	 */
	private boolean isHaveCifBackDate(String taskid,String inscomcode){
		boolean result = false;
//		INSBLastyearinsureinfo insbLastyearinsureinfo = new INSBLastyearinsureinfo();
//		insbLastyearinsureinfo.setTaskid(taskid);
		INSBLastyearinsureinfo lastyearinsureinfo = insbRulequerycarinfoDao.queryLastYearClainInfo(taskid);
		if(null == lastyearinsureinfo){
			result = true;
		}else{
			//默认都存在
			boolean syflag = true;
			boolean jqflag = true;
			if(StringUtil.isEmpty(lastyearinsureinfo.getSyenddate())){
				syflag = false;
			}
			if(StringUtil.isEmpty(lastyearinsureinfo.getJqenddate())){
				jqflag = false;
			}
			//LogUtil.info("南枫平台isHaveCifBackDate"+taskid +"cof上年商业险结束时间="+lastyearinsureinfo.getSyenddate()+"cof上年交强险结束时间="+lastyearinsureinfo.getJqenddate());
			//查询保单表
			INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
			insbPolicyitem.setTaskid(taskid);
			insbPolicyitem.setInscomcode(inscomcode);
			List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(insbPolicyitem);
			if(null != insbPolicyitems && insbPolicyitems.size() > 0){
				LogUtil.info("南枫平台isHaveCifBackDate"+taskid +"保单表数据条数size="+insbPolicyitems.size()+"cof上年商业险结束时间="+lastyearinsureinfo.getSyenddate()+"cof上年交强险结束时间="+lastyearinsureinfo.getJqenddate());
				for(INSBPolicyitem policyitem : insbPolicyitems){
					if("0".equals(policyitem.getRisktype()) && !syflag){
						result = true;
					}
					if("1".equals(policyitem.getRisktype()) && !jqflag){
						result = true;
					}
				}
			}
		}
		return result;
	}


	/**
	 * 普通平台查询无数据返回或者超时调用
	 * @param taskid
	 * @param subwkid
	 * @param inscomcode
	 */
	private void cifHasNodataBackTocm(String taskid,String subwkid,String inscomcode){
		LogUtil.info("cifHasNodataBackTocm开始向保单表插入投保时间"+taskid+"=供应商id="+inscomcode+"=子流程id="+subwkid+"普通平台查询无数据返回或者超时");
		dateIsRepeatInsured(taskid, subwkid, inscomcode, "", "", "", "","");
	}

	/**
	 * 平台查询有数据返回
	 * 入库最新的起保日期 20160820版本used
	 * @param taskid
	 * @param subwkid 子流程id
	 * @param inscomcode
	 * @param systartdate 上年商业险终保日期
	 * @param jqstartdate 上年交强险终保日期
	 * @param syrepeatinsurance 上年商业险重复投保提示
	 * @param jqrepeatinsurance 上年交强险重复投保提示
	 */
	public void dateIsRepeatInsured(String taskid,String subwkid,String inscomcode,String systartdate,String jqstartdate,String syrepeatinsurance,String jqrepeatinsurance,String lastyearpid){
		//新增上年报价公司,存在事务不一致问题
		if(!StringUtil.isEmpty(lastyearpid) && lastyearpid.length() >= 4 && !isHaveThisPrid(taskid, lastyearpid)){
			addLastYearProidToinsured(taskid, lastyearpid);
		}

		LogUtil.info("开始向保单表插入投保时间"+taskid+"=供应商id="+inscomcode+"=子流程id="+subwkid+"=上年商业险终保日期="+systartdate+"=上年交强险终保日期="+jqstartdate);
		LogUtil.info("开始向保单表插入投保时间"+taskid+"=供应商id="+inscomcode+"=上年商业险重复投保提示="+syrepeatinsurance+"=上年交强险重复投保提示="+jqrepeatinsurance);
		INSBProvider provider = insbProviderDao.selectById(inscomcode);
		int day = 0;
		if(null != provider && null != provider.getAdvancequote()){
			day = provider.getAdvancequote();
			LogUtil.info("开始向保单表插入投保时间"+taskid+"=供应商id="+inscomcode+"=允许提前报价天数="+day);
		}
		boolean syflag = false;
		boolean jqflag = false;
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskid);
		insbPolicyitem.setInscomcode(inscomcode);
		List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(insbPolicyitem);
		if(null != insbPolicyitems && insbPolicyitems.size() > 0){
			for(INSBPolicyitem policyitem : insbPolicyitems){
				if("0".equals(policyitem.getRisktype())){
					if(!StringUtil.isEmpty(systartdate) || !StringUtil.isEmpty(syrepeatinsurance)){
						syflag = syRepeatInsured("0", systartdate, taskid, day, syrepeatinsurance,inscomcode);
					}
					//保存起保日期
					if(StringUtil.isEmpty(systartdate)){
						LogUtil.info("开始向保单表插入投保时间"+taskid+"=供应商id="+inscomcode+"平台未返回上年商业险起保日期默认即日起保");
						policyitem.setStartdate(ModelUtil.nowDateAddOneDay(new Date()));
						policyitem.setEnddate(ModelUtil.nowDateMinusOneDay(ModelUtil.nowDateAddOneYear(ModelUtil.nowDateAddOneDay(new Date()), 1)));
					}else{
						LogUtil.info("开始向保单表插入投保时间"+taskid+"=供应商id="+inscomcode+"商业险起保日期="+systartdate);
						policyitem.setStartdate(ModelUtil.conbertStringToNyrDate(systartdate));
						policyitem.setEnddate(ModelUtil.nowDateMinusOneDay(ModelUtil.nowDateAddOneYear(ModelUtil.conbertStringToNyrDate(systartdate), 1)));
					}
				}else{
					if(!StringUtil.isEmpty(jqstartdate) || !StringUtil.isEmpty(jqrepeatinsurance)){
						jqflag = syRepeatInsured("1", jqstartdate, taskid, day, jqrepeatinsurance,inscomcode);
					}
					//保存起保日期
					if(StringUtil.isEmpty(jqstartdate)){
						LogUtil.info("开始向保单表插入投保时间"+taskid+"=供应商id="+inscomcode+"平台未返回上年交强险起保日期默认即日起保");
						policyitem.setStartdate(ModelUtil.nowDateAddOneDay(new Date()));
						policyitem.setEnddate(ModelUtil.nowDateMinusOneDay(ModelUtil.nowDateAddOneYear(ModelUtil.nowDateAddOneDay(new Date()), 1)));
					}else{
						LogUtil.info("开始向保单表插入投保时间"+taskid+"=供应商id="+inscomcode+"交强险起保日期="+jqstartdate);
						policyitem.setStartdate(ModelUtil.conbertStringToNyrDate(jqstartdate));
						policyitem.setEnddate(ModelUtil.nowDateMinusOneDay(ModelUtil.nowDateAddOneYear(ModelUtil.conbertStringToNyrDate(jqstartdate), 1)));
					}
				}
				insbPolicyitemDao.updateById(policyitem);
			}
		}

		//有一个为true 关闭子流程
		if(syflag || jqflag){
			//保存关闭子流程原因
			INSBQuotetotalinfo param = new INSBQuotetotalinfo();
			param.setTaskid(taskid);
			INSBQuotetotalinfo insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(param);
			INSBQuoteinfo paramq = new INSBQuoteinfo();
			paramq.setQuotetotalinfoid(insbQuotetotalinfo.getId());
			List<INSBQuoteinfo> quotelists = insbQuoteinfoDao.selectList(paramq);
			for(INSBQuoteinfo quote:quotelists){
				String desc = "上年商业险终保日期："+systartdate+"，上年交强险终保日期："+jqstartdate+"，上年商业险重复投保提示："+syrepeatinsurance+"，上年交强险重复投保提示："+jqrepeatinsurance;
				saveFlowerrorToManWork(taskid, quote.getInscomcode(), desc, "10");
				LogUtil.info("开始向保单表插入投保时间"+taskid+"=供应商id="+quote.getInscomcode()+"=子流程id="+subwkid+"重复投保或者报价超期关闭子流程");
				String message =WorkFlowUtil.abortProcessByMainIdSubid(quote.getWorkflowinstanceid(), "sub", "back",taskid);
				JSONObject result=JSONObject.fromObject(message);
				if("success".equals(result.getString("message"))){
					LogUtil.info("开始向保单表插入投保时间"+taskid+"=供应商id="+quote.getInscomcode()+"=子流程id="+quote.getWorkflowinstanceid()+"关闭子流程成功");
				}else{
					LogUtil.info("开始向保单表插入投保时间"+taskid+"=供应商id="+quote.getInscomcode()+"=子流程id="+quote.getWorkflowinstanceid()+"关闭子流程失败");
				}
			}
		} else {
			//推工作流报价接口
	        taskthreadPool4workflow.execute(new Runnable() {
	        @Override
	        public void run() {
				LogUtil.info("开始向保单表插入投保时间结束推子流程前置" + taskid + "=供应商id=" + inscomcode + "=子流程id=" + subwkid + "==休息两秒钟==");
	        	try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        	//调用规则判断承保限制条件
	        	Map<String,Object> param = new HashMap<String, Object>();
				param = getPriceParamWay(param, taskid, inscomcode, "0");
	            String callback = WorkFlowUtil.updateInsuredInfoNoticeWorkflow(subwkid, "admin", "子流程前置", param);
	            LogUtil.info("开始向保单表插入投保时间" + taskid + "=供应商id=" + inscomcode + "=子流程id=" + subwkid + "=子流程前置返回结果=" + callback);
	           }
	        });
		}
		LogUtil.info("开始向保单表插入投保时间"+taskid+"=供应商id="+inscomcode+"=子流程id="+subwkid+"操作结束了");
	}

	/**
	 * 判断是否重复投保
	 * 提前报价时间是否大于cm后太设定时间
	 * @param type 0 商业险 1较强险
	 * @param startdate 起保日期 可能为null
	 * @param taskid 任务id
	 * @param day 允许提前报价天数
	 * @param repeat 重复投保提示
	 * @return true 重复投保或者超期
	 */
	private boolean syRepeatInsured(String type,String startdate,String taskid,int day,String repeat,String inscomcode){
		boolean result = false;
		String res = "0".equals(type)?"商业险":"交强险";
		if(!StringUtil.isEmpty(repeat)){
			LogUtil.info("开始向保单表插入投保时间"+taskid+"=="+res+"重复投保啦=cif回写数据="+repeat);
			result = true;
		}else{
			if(!StringUtil.isEmpty(startdate)){
				if(0 != day){
					LogUtil.info("开始向保单表插入投保时间"+taskid+"=="+res+"cm后台允许提前"+day+"天报价"+"=起保日期="+startdate);
					int between = ModelUtil.compareDateWithDay(ModelUtil.conbertStringToNyrDate(startdate), day);
					if(between < 0){
                        LogUtil.info("开始向保单表插入投保时间"+taskid+"=="+res+"提前报价超期了");
						result = true;
					}
				}
			}else{
				saveFlowerrorToManWork(taskid, inscomcode,"平台查询"+res+"起保日期为空"+"，转人工处理","9");
			}
		}
		return result;
	}

	@Override
	public void saveInsuredDateBycifBack(String taskid, String subwfid,String inscomcode) {
		//平台查询是否回调数据
//		INSBLastyearinsureinfo insbLastyearinsureinfo = new INSBLastyearinsureinfo();
//		insbLastyearinsureinfo.setTaskid(taskid);
//		insbLastyearinsureinfo.setSflag("2");
		INSBLastyearinsureinfo lastyearinsureinfo = insbRulequerycarinfoDao.queryLastYearClainInfo(taskid);
		if(null == lastyearinsureinfo){
			cifHasNodataBackTocm(taskid, subwfid, inscomcode);
			LogUtil.info("saveInsuredDateBycfiBack"+taskid+"=普通查询80秒时间到了未返回数据="+"当前时间="+new Date());
			//80秒时间到了平台查询还没返回
			saveFlowerrorToManWork(taskid, inscomcode, "普通平台查询80秒未返回数据", "11");
		}else{
			LogUtil.info("saveInsuredDateBycfiBack"+taskid+"=普通查询80秒返回了数据="+"当前时间="+new Date());
			dateIsRepeatInsured(taskid, subwfid,inscomcode, lastyearinsureinfo.getSyenddate(),
					lastyearinsureinfo.getJqenddate(), lastyearinsureinfo.getRepeatinsurance(), lastyearinsureinfo.getJqrepeatinsurance(),lastyearinsureinfo.getSupplierid());
		}

	}

	/**
	 * 如果上年供应商不在已选择的列表里，增加上年投保公司报价
	 * 验证上年投保公司该代理人是否拥有协议
	 * 默认选择常用出单网点，不存在默认选中第一个
	 * @param taskid
	 * @param lastyearpid
	 */
	public void addLastYearProidToinsured(String taskid,String lastyearpid){
		if(!StringUtil.isEmpty(lastyearpid) && lastyearpid.length() >= 4){
			//传递的参数  协议id#供应商id#出单网点id#网销传统类型
			String agreeidpidsitid = "";
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			if(null != quotetotalinfo){
				INSBAgent insbAgent = insbAgentDao.selectByJobnum(quotetotalinfo.getAgentnum());
				if(insbAgent != null){
					agreeidpidsitid = judjeThisProidInThat(lastyearpid, quotetotalinfo.getInsprovincecode(), quotetotalinfo.getInscitycode(), insbAgent.getId(), "", taskid);
				}
			}
			LogUtil.info("增加上年投保公司报价addLastYearProidToinsured"+taskid+"=start报价信息表中插入新增供应商数据=agreeidpidsitid="+agreeidpidsitid);
			//报价信息表中不存在，agreeidpidsitid不能为空
			if(!StringUtil.isEmpty(agreeidpidsitid)){
				//--------------------------新增供应商-----------------------
				LogUtil.info("增加上年投保公司报价addLastYearProidToinsured"+taskid+"=start报价信息表中插入新增供应商数据="+agreeidpidsitid);
				String[] arra = agreeidpidsitid.split("#");
				//根据实例id查询车辆信息
				INSBCarinfo insbCarinfo = insbCarinfoDao.selectCarinfoByTaskId(taskid);
				if(null != insbCarinfo){
					INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
					insbQuoteinfo.setCreatetime(new Date());
					insbQuoteinfo.setOperator(quotetotalinfo.getOperator());
					insbQuoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
					insbQuoteinfo.setOwnername(insbCarinfo.getOwnername());
					insbQuoteinfo.setPlatenumber(insbCarinfo.getCarlicenseno());
					insbQuoteinfo.setInscomcode(arra[1]);
					insbQuoteinfo.setAgreementid(arra[0]);
					insbQuoteinfo.setDeptcode(arra[2]);
					insbQuoteinfo.setBuybusitype(arra[3]);
					//insbQuoteinfo.setNoti("新增上年报价公司");

					if (StringUtil.isNotEmpty(arra[2])) {
						INSCDept dept = inscDeptDao.selectByComcode(arra[2]);
						if (dept != null) {
							String platformInnercode = inscDeptService.getPlatformInnercode(dept.getDeptinnercode());
							if (platformInnercode != null) {
								insbQuoteinfo.setPlatforminnercode(Integer.parseInt(platformInnercode));
							}
						}
					}

					insbQuoteinfoDao.insert(insbQuoteinfo);
					//--------------------------新增供应商增加保险配置信息-----------------------
					LogUtil.info("增加上年投保公司报价addLastYearProidToinsured"+taskid+"=start保存保险配置信息=");
					List<String> ids = new ArrayList<String>();
					ids.add(arra[1]);
					addOneProviderInsuredConfig(taskid,ids);
					//--------------------------新增供应商调用工作流-----------------------
					LogUtil.info("增加上年投保公司报价addLastYearProidToinsured"+taskid+"=start推送的供应商ids="+ids);
					addLastyearpidToWorkflow(taskid, insbQuoteinfo);
				}
			}
		}
	}
	/**
	 * 添加上年投保公司报价，获取子流程id，推子流程前置节点
	 * @param taskid
	 * @param insbQuoteinfo
	 */
	private void addLastyearpidToWorkflow(String taskid,INSBQuoteinfo insbQuoteinfo){
		String inscomcode = insbQuoteinfo.getInscomcode();
		//==================调用工作流之前，初始化his数据===========
		createHisTableInit(taskid);
		//进行车型年款匹配
		saveCarmodelinfoToData(taskid, inscomcode, "");

		List<String> ids = new ArrayList<String>();
		//调用规则判断承保限制条件
		Map<String, Object> param = new HashMap<String, Object>();
		param = getPriceParamWayStartSubflow(param, taskid, inscomcode);
		
		ids.add(inscomcode);
		LogUtil.info("添加上年投保公司报价addLastyearpidToWorkflow"+taskid+"新增的报价公司列表="+inscomcode);
		//TODO验证
		//调用工作流，新增加的报价公司
		INSBWorkflowmain mainModel = insbWorkflowmainDao.selectByInstanceId(taskid);
		//投保流程
		String result = WorkFlowUtil.noticeWorkflowStartQuote(mainModel.getOperator(), taskid, param,"0");
		LogUtil.info("添加上年投保公司报价addLastyearpidToWorkflow"+taskid+"新增的报价公司列表="+ids+"工作流返回值"+result);
		if(!StringUtil.isEmpty(result)){
			JSONObject jsonObj = JSONObject.fromObject(result);
			if(jsonObj.containsKey("message") && "fail".equals(jsonObj.getString("message"))){
				LogUtil.info("添加上年投保公司报价addLastyearpidToWorkflow"+taskid+"=获取子流程id失败了=");
			}else{
				//防止多次提交修改
				if(StringUtil.isEmpty(insbQuoteinfo.getWorkflowinstanceid())){
					Map<String, String> map = new HashMap<String, String>();
					map.put("workflowinstanceid", jsonObj.get(inscomcode).toString());
					map.put("id", insbQuoteinfo.getId());
					appInsuredQuoteDao.updateInsbQuoteinfoById(map);
				}
				taskthreadPool4workflow.execute(new Runnable() {
					@Override
					public void run() {
						// 得到子流程id，开启多线程推流程
						LogUtil.info("添加上年投保公司报价addLastyearpidToWorkflow"+taskid+"当前供应商="+inscomcode+"开始时间=");
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						LogUtil.info("添加上年投保公司报价addLastyearpidToWorkflow"+taskid+",休息5秒钟继续="+inscomcode+"开始时间=");
						Map<String,Object> param = new HashMap<String, Object>();
						param = getPriceParamWay(param, taskid, inscomcode, "0");
						String callback = WorkFlowUtil.updateInsuredInfoNoticeWorkflow(jsonObj.get(inscomcode).toString(), "admin", "子流程前置", param);
						LogUtil.info("添加上年投保公司报价addLastyearpidToWorkflow"+taskid+"开启子流程="+jsonObj.get(inscomcode).toString()+"当前供应商="+inscomcode+"返回结果="+callback);
					}
				});
			}
		}
	}

	/**
	 * 报价信息表中是否存在这家报价公司
	 * @param taskid
	 * @param lastyearpid
	 * @return true 包含
	 */
	private boolean isHaveThisPrid(String taskid,String lastyearpid){
		boolean result = false;
		List<INSBQuoteinfo> insbQuoteinfos = appInsuredQuoteDao.getSelectedProvidersByTaskid(taskid);
		if(null != insbQuoteinfos && insbQuoteinfos.size() > 0){
			for(INSBQuoteinfo insbQuoteinfo : insbQuoteinfos){
				if(insbQuoteinfo.getInscomcode().startsWith(lastyearpid.substring(0, 4))){
					result = true;
					break;
				}
			}
		}
		return result;
	}

	@Override
	public String judgeThisPidIsAgentHave(String taskid, String inscomcode) {
		String agreeidpidsitid = "";
		INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
        insbQuotetotalinfo.setTaskid(taskid);
		INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
		if(null != quotetotalinfo){
			INSBAgent insbAgent = insbAgentDao.selectByJobnum(quotetotalinfo.getAgentnum());
			if(insbAgent != null){
				agreeidpidsitid = judjeThisProidInThat(inscomcode, quotetotalinfo.getInsprovincecode(), quotetotalinfo.getInscitycode(), insbAgent.getId(), "", taskid);
			}
		}
		return agreeidpidsitid;
	}
}