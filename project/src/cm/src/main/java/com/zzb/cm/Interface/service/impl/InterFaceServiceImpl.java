package com.zzb.cm.Interface.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.JsonUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.timer.SchedulerService;
import com.cninsure.monitor.main.MonitorUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCCodeService;
import com.cninsure.system.service.INSCDeptService;
import com.common.CloudQueryUtil;
import com.common.ConstUtil;
import com.common.HttpClientUtil;
import com.common.MiscConst;
import com.common.ModelUtil;
import com.common.TaskConst;
import com.common.WorkflowFeedbackUtil;
import com.common.XMPPUtils;
import com.common.redis.CMRedisClient;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import com.zzb.app.service.AppQuotationService;
import com.zzb.cm.Interface.entity.car.model.AgentInfo;
import com.zzb.cm.Interface.entity.car.model.BaseSuiteInfo;
import com.zzb.cm.Interface.entity.car.model.BeneficiaryPerson;
import com.zzb.cm.Interface.entity.car.model.BizSuiteInfo;
import com.zzb.cm.Interface.entity.car.model.CarInfo;
import com.zzb.cm.Interface.entity.car.model.CarOwnerInfo;
import com.zzb.cm.Interface.entity.car.model.ConfigInfo;
import com.zzb.cm.Interface.entity.car.model.DeliverInfo;
import com.zzb.cm.Interface.entity.car.model.DriverPerson;
import com.zzb.cm.Interface.entity.car.model.EfcSuiteInfo;
import com.zzb.cm.Interface.entity.car.model.InsurePerson;
import com.zzb.cm.Interface.entity.car.model.Invoiceinfo;
import com.zzb.cm.Interface.entity.car.model.PersonInfo;
import com.zzb.cm.Interface.entity.car.model.ProviderInfo;
import com.zzb.cm.Interface.entity.car.model.SQ;
import com.zzb.cm.Interface.entity.car.model.SpecialRisk;
import com.zzb.cm.Interface.entity.car.model.SubAgentInfo;
import com.zzb.cm.Interface.entity.car.model.SuiteDef;
import com.zzb.cm.Interface.entity.car.model.TaxSuiteInfo;
import com.zzb.cm.Interface.entity.car.model.TrackInfo;
import com.zzb.cm.Interface.entity.cif.model.Beneficiary;
import com.zzb.cm.Interface.entity.cif.model.CarKind;
import com.zzb.cm.Interface.entity.cif.model.CarKindPolicy;
import com.zzb.cm.Interface.entity.cif.model.CarModel;
import com.zzb.cm.Interface.entity.cif.model.DeliveryInfo;
import com.zzb.cm.Interface.entity.cif.model.Insured;
import com.zzb.cm.Interface.entity.cif.model.IntendToPay;
import com.zzb.cm.Interface.entity.cif.model.TaxpayerInfo;
import com.zzb.cm.Interface.model.CarModelInfoBean;
import com.zzb.cm.Interface.service.FlowInfo;
import com.zzb.cm.Interface.service.InterFaceDefaultValueUtil;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.cm.dao.INSBApplicantDao;
import com.zzb.cm.dao.INSBApplicanthisDao;
import com.zzb.cm.dao.INSBCarconfigDao;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarinfohisDao;
import com.zzb.cm.dao.INSBCarkindpriceDao;
import com.zzb.cm.dao.INSBCarmodelinfoDao;
import com.zzb.cm.dao.INSBCarmodelinfohisDao;
import com.zzb.cm.dao.INSBFairyInsureErrorLogDao;
import com.zzb.cm.dao.INSBFlowinfoDao;
import com.zzb.cm.dao.INSBInsuresupplyparamDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.dao.INSBRulequerycarinfoDao;
import com.zzb.cm.entity.INSBApplicant;
import com.zzb.cm.entity.INSBApplicanthis;
import com.zzb.cm.entity.INSBCarconfig;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.entity.INSBCarmodelinfohis;
import com.zzb.cm.entity.INSBCarowneinfo;
import com.zzb.cm.entity.INSBFairyInsureErrorLog;
import com.zzb.cm.entity.INSBFlowerror;
import com.zzb.cm.entity.INSBFlowinfo;
import com.zzb.cm.entity.INSBFlowlogs;
import com.zzb.cm.entity.INSBInsured;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBInvoiceinfo;
import com.zzb.cm.entity.INSBLastyearinsureinfo;
import com.zzb.cm.entity.INSBLegalrightclaim;
import com.zzb.cm.entity.INSBLegalrightclaimhis;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.entity.INSBRelationpersonhis;
import com.zzb.cm.entity.INSBRulequerycarinfo;
import com.zzb.cm.entity.INSBSpecifydriver;
import com.zzb.cm.entity.INSBSpecifydriverhis;
import com.zzb.cm.entity.INSHApplicanthis;
import com.zzb.cm.entity.INSHCarinfohis;
import com.zzb.cm.entity.INSHCarkindprice;
import com.zzb.cm.entity.INSHCarmodelinfohis;
import com.zzb.cm.entity.INSHCarowneinfo;
import com.zzb.cm.entity.INSHInsuredhis;
import com.zzb.cm.entity.INSHLegalrightclaimhis;
import com.zzb.cm.entity.INSHOrder;
import com.zzb.cm.entity.INSHOrderdelivery;
import com.zzb.cm.entity.INSHOrderpayment;
import com.zzb.cm.entity.INSHPerson;
import com.zzb.cm.entity.INSHPolicyitem;
import com.zzb.cm.entity.INSHQuoteinfo;
import com.zzb.cm.entity.INSHQuotetotalinfo;
import com.zzb.cm.entity.INSHRelationpersonhis;
import com.zzb.cm.entity.INSHSpecifydriverhis;
import com.zzb.cm.service.INSBApplicantService;
import com.zzb.cm.service.INSBApplicanthisService;
import com.zzb.cm.service.INSBCarinfoService;
import com.zzb.cm.service.INSBCarinfohisService;
import com.zzb.cm.service.INSBCarkindpriceService;
import com.zzb.cm.service.INSBCarmodelinfoService;
import com.zzb.cm.service.INSBCarmodelinfohisService;
import com.zzb.cm.service.INSBCarowneinfoService;
import com.zzb.cm.service.INSBCommonQuoteinfoService;
import com.zzb.cm.service.INSBFilelibraryService;
import com.zzb.cm.service.INSBFlowerrorService;
import com.zzb.cm.service.INSBFlowinfoService;
import com.zzb.cm.service.INSBFlowlogsService;
import com.zzb.cm.service.INSBInsuredService;
import com.zzb.cm.service.INSBInsuredhisService;
import com.zzb.cm.service.INSBInvoiceinfoService;
import com.zzb.cm.service.INSBLastyearinsureinfoService;
import com.zzb.cm.service.INSBLegalrightclaimService;
import com.zzb.cm.service.INSBLegalrightclaimhisService;
import com.zzb.cm.service.INSBManualPriceService;
import com.zzb.cm.service.INSBOrderService;
import com.zzb.cm.service.INSBPersonHelpService;
import com.zzb.cm.service.INSBPersonService;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.cm.service.INSBQuotetotalinfoService;
import com.zzb.cm.service.INSBRelationpersonService;
import com.zzb.cm.service.INSBRelationpersonhisService;
import com.zzb.cm.service.INSBRenewalService;
import com.zzb.cm.service.INSBSpecifydriverService;
import com.zzb.cm.service.INSBSpecifydriverhisService;
import com.zzb.cm.service.INSBWorkflowDataService;
import com.zzb.cm.service.INSHApplicanthisService;
import com.zzb.cm.service.INSHCarinfohisService;
import com.zzb.cm.service.INSHCarkindpriceService;
import com.zzb.cm.service.INSHCarmodelinfohisService;
import com.zzb.cm.service.INSHCarowneinfoService;
import com.zzb.cm.service.INSHInsuredhisService;
import com.zzb.cm.service.INSHLegalrightclaimhisService;
import com.zzb.cm.service.INSHOrderService;
import com.zzb.cm.service.INSHOrderdeliveryService;
import com.zzb.cm.service.INSHOrderpaymentService;
import com.zzb.cm.service.INSHPersonService;
import com.zzb.cm.service.INSHPolicyitemService;
import com.zzb.cm.service.INSHQuoteinfoService;
import com.zzb.cm.service.INSHQuotetotalinfoService;
import com.zzb.cm.service.INSHRelationpersonhisService;
import com.zzb.cm.service.INSHSpecifydriverhisService;
import com.zzb.conf.component.SupplementCache;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBAgentpermissionDao;
import com.zzb.conf.dao.INSBOrderdeliveryDao;
import com.zzb.conf.dao.INSBOrderpaymentDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.dao.INSBWorkflowsubtrackDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBAgreement;
import com.zzb.conf.entity.INSBOperatorcomment;
import com.zzb.conf.entity.INSBOrderdelivery;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.entity.INSBPaychannel;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBRegion;
import com.zzb.conf.entity.INSBRiskkind;
import com.zzb.conf.entity.INSBRiskkindconfig;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowmaintrack;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBAgreementService;
import com.zzb.conf.service.INSBAutoconfigService;
import com.zzb.conf.service.INSBEdiconfigurationService;
import com.zzb.conf.service.INSBElfconfService;
import com.zzb.conf.service.INSBOperatorcommentService;
import com.zzb.conf.service.INSBOrderdeliveryService;
import com.zzb.conf.service.INSBOrderpaymentService;
import com.zzb.conf.service.INSBPaychannelService;
import com.zzb.conf.service.INSBPolicyitemService;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.conf.service.INSBPrvaccountmanagerService;
import com.zzb.conf.service.INSBRegionService;
import com.zzb.conf.service.INSBRiskkindService;
import com.zzb.conf.service.INSBRiskkindconfigService;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.conf.service.INSBWorkflowmaintrackService;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.conf.service.INSBWorkflowsubtrackService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.WorkFlowRuleInfo;
import com.zzb.mobile.service.AppInsuredQuoteService;
import com.zzb.mobile.service.AppOtherRequestService;
import com.zzb.mobile.util.EncodeUtils.Md5Encodes;
import com.zzb.mobile.util.PayStatus;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings("deprecation")
@Service
public class InterFaceServiceImpl implements InterFaceService {
	private static final int expireTime = 20 * 60;

	@Resource
	private SchedulerService schedule;
	@Resource
	INSBCarinfoService insbCarinfoService;// 车辆信息原表
	@Resource
	INSBCarinfohisService insbCarinfohisService;// 车辆信息历史表
	@Resource
	INSBCarmodelinfoService insbCarmodelinfoService;// 车型信息原表
	@Resource
	INSBCarmodelinfohisService insbCarmodelinfohisService;// 车型信息历史表
	@Resource
	INSBPersonService insbPersonService;// 人员信息表
	@Resource
	INSBCarowneinfoService insbCarowneinfoService;// 车主信息表
	@Resource
	INSBApplicantService insbApplicantService;// 投保人原表
	@Resource
	INSBApplicanthisService insbApplicanthisService;// 投保人历史表
	@Resource
	INSBInsuredService insbInsuredService;// 被保人原表
	@Resource
	INSBInsuredhisService insbInsuredhisService;// 被保人历史表
	@Resource
	INSBLegalrightclaimService insbLegalrightclaimService;// 受益人原表
	@Resource
	INSBLegalrightclaimhisService insbLegalrightclaimhisService;// 受益人历史表
	@Resource
	INSBSpecifydriverService insbspecifydriverService;// 驾驶人原表
	@Resource
	INSBSpecifydriverhisService insbspecifydriverhisService;// 驾驶人原表
	@Resource
	INSBAgentService insbAgentService;// 代理人信息表
	@Resource
	INSBProviderService insbProviderService;// 供应商表
	@Resource
	INSBOrderdeliveryService insbOrderdeliveryService;// 配送信息表
	@Resource
	INSBCarkindpriceService insbCarkindpriceService;// 保险公司险别表
	@Resource
	INSBOrderService insbOrderService;// 订单信息表
	@Resource
	INSBPolicyitemService insbPolicyitemService;// 保单信息表
	@Resource
	INSBQuoteinfoService insbQuoteinfoService;// 报价信息表
	@Resource
	INSBQuotetotalinfoService insbQuotetotalinfoService;// 报价信息总表
	@Resource
	INSBOrderpaymentService insbOrderpaymentService;// 订单支付信息表
	@Resource
	INSBPaychannelService insbPaychannelService;// 支付渠道表
	@Resource
	INSBFlowinfoService insbFlowinfoService;// 工作流自状态流程信息表
	@Resource
	INSBElfconfService insbElfconfService;// 精灵配置信息
	@Resource
	INSBEdiconfigurationService insbEdiconfigurationService;// edi配置信息
	@Resource
	INSBAgentDao iNSBAgentDao;
	@Resource
	INSBAgentpermissionDao iNSBAgentpermissionDao;
	@Resource
	INSBCarinfoDao insbCarinfoDao;
	@Resource
	INSBCarinfohisDao insbCarinfohisDao;
	@Resource
	INSBCarmodelinfoDao insbCarmodelinfoDao;
	@Resource
	INSBCarmodelinfohisDao insbCarmodelinfohisDao;
	@Resource
	INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	INSBCarkindpriceDao insbCarkindpriceDao;
	@Resource
	INSBApplicantDao insbApplicantDao;
	@Resource
	INSBApplicanthisDao insbApplicanthisDao;
	@Resource
	INSBOrderdeliveryDao insbOrderdeliveryDao;
	@Resource
	INSBWorkflowsubtrackDao insbWorkflowsubtrackDao;
	@Resource
	INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	INSBFlowinfoDao insbFlowinfoDao;
	@Resource
	INSCDeptService inscDeptService;
	@Resource
	INSBRiskkindService insbriskkindService;
	@Resource
	INSBFlowlogsService insbFlowlogsService;
	@Resource
	INSBRegionService insbRegionService;
	@Resource
	INSBFlowerrorService insbFlowerrorService;
	@Resource
	INSCCodeService inscCodeService;
	@Resource
	INSBWorkflowsubDao insbWorkflowsubDao;
	@Resource
	INSBAutoconfigService insbAutoconfigService;
	@Resource
	INSBRiskkindconfigService insbRiskkindconfigService;
	@Resource
	INSBWorkflowmaintrackService insbWorkflowmaintrackService;
	@Resource
	INSBWorkflowsubtrackService insbWorkflowsubtrackService;
	@Resource
	INSBOperatorcommentService insbOperatorcommentService;
	@Resource
	INSBPrvaccountmanagerService insbPrvaccountmanagerService;
	@Resource
	INSBManualPriceService insbManualPriceService;
	@Resource
	INSBWorkflowmainService insbWorkflowmainService;
	@Resource
	INSBWorkflowDataService insbWorkflowDataService;
	@Resource
	INSBWorkflowsubService insbWorkflowsubService;
	@Resource
	INSBRelationpersonService insbRelationpersonService;
	@Resource
	INSBRelationpersonhisService insbRelationpersonhisService;
	@Resource
	INSBLastyearinsureinfoService insbLastyearinsureinfoService;
	@Resource
	AppQuotationService appQuotationService;
	@Resource
	ThreadPoolTaskExecutor taskthreadPool4workflow;
	@Resource
	private INSBAgreementService insbagreementservice;
	@Resource
	private IRedisClient redisClient;
	@Resource
	private AppInsuredQuoteService appInsuredQuoteService;
	@Resource
	private AppOtherRequestService appOtherRequestService;
	@Resource
	private INSCCodeDao inscCodeDao;
	@Resource
	private SupplementCache supplementCache;
	@Resource
	private INSBWorkflowmainService workflowmainService;
	@Resource
	private INSBRulequerycarinfoDao insbRulequerycarinfoDao;
	@Resource
	private INSBCommonQuoteinfoService commonQuoteinfoService;
	@Resource
	private INSBPersonHelpService insbPersonHelpService;
	@Resource
	private INSBOrderpaymentDao insbOrderpaymentDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private INSBFairyInsureErrorLogDao fairyInsureErrorLogDao;
	@Resource
	private INSBFilelibraryService filelibraryService;
	@Resource
	private INSBRenewalService insbRenewalService;
	@Resource
	private INSBInsuresupplyparamDao insbInsuresupplyparamDao;

	private static String AUTO_INSURE_PLATFORM = "";
	static {
		// 读取相关的配置
		ResourceBundle resourceBundle = ResourceBundle.getBundle("config/config");
		AUTO_INSURE_PLATFORM = resourceBundle.getString("autoinsure.platform");
	}

	// 规则平台查询的供应商id
	private static String[] CANUSEDPROID = new String[] { "2011", "2027" };
	//承保流程
	private List<String> approvedTaskcode = Collections.unmodifiableList(Arrays.asList("25","26","27"));

	@Override
	public String goRobot(String processinstanceid, String incoid, String touserid, String taskType, String childTaskId) throws Exception {
		LogUtil.info("进入==zzww调用接口=====精灵任务通知接口=taskid=" + processinstanceid + ",inscomcode=" + incoid + ",taskType=" + taskType + ",touserid=" + touserid + ",childTaskId:" + childTaskId + ",key:"
				+ childTaskId + "_" + taskType + "_robot======");
		return this.goToFairyQuote(processinstanceid, incoid, touserid, taskType);
	}

	@Override
	public String goEDi(String processinstanceid, String incoid, String ediid, String taskType, String childTaskId) throws Exception {
		LogUtil.info("进入==zzww调用接口==taskid=" + processinstanceid + ",inscomcode=" + incoid + ",taskType=" + taskType + ",ediid=" + ediid + ",childTaskId:" + childTaskId + ",key:" + childTaskId + "_"
				+ taskType + "_edi===");
		return this.goToEdiQuote(processinstanceid, incoid, ediid, taskType);
	}



	/**
	 * 工作流调用精灵任务接口
	 *
	 */
	@Override
	public String goToFairyQuote(String processinstanceid, String incoid, String touserid, String taskType) throws Exception {
		String reserved = "";
		String quoteinscom = "";
		String callBackUrl = "/interface/savePacket";
		String payCode_query = "";	//二维码支付查询类型
		String orderPaymentId = "";	//支付平台订单号
		String sid = "";			//二维码轮询唯一标识
		if(taskType.contains("quote@reserved")){
			quoteinscom = taskType.split("@")[2];//取得报价中存在的保险公司
			taskType = "quote";
			reserved = "true";
		}
		if(taskType.contains("insurequery@qrcode#")){	//核保查询、拆分Url和订单号
			payCode_query = taskType;
			callBackUrl = taskType.substring(taskType.indexOf("#") + 1,taskType.length());
			taskType = "qrcode_insurequery";
			orderPaymentId = touserid.substring(touserid.indexOf("#") + 1,touserid.length());
			touserid = touserid.substring(0,touserid.indexOf("#"));
			sid = incoid.substring(incoid.indexOf("#") + 1,incoid.length());
			incoid = incoid.substring(0,incoid.indexOf("#"));
		}
		if(taskType.contains("approvedquery@qrcode")){	//承保查询
			payCode_query = taskType;
			taskType = "qrcode_approvedquery";
			callBackUrl = "/interface/saveQRCodePacket";
		}
		String subTaskId = "";
		if(StringUtil.isNotEmpty(quoteinscom)){//用真实存在需要报价的公司
			subTaskId = this.getChildTaskId(processinstanceid, quoteinscom);
		}else{
			subTaskId = this.getChildTaskId(processinstanceid, incoid);
		}
		String key = "";
		if ("approved".equals(taskType)) {
			taskType = "approvedquery";
		}
		if ("approvedquery".equals(taskType) || "qrcode_approvedquery".equals(taskType)) {
			key = processinstanceid + "_" + "approved" + "_robot";
		} else {
			if (taskType.contains("query")) {
				key = subTaskId + "_" + taskType.replace("query", "") + "_robot";
			} else {
				key = subTaskId + "_" + taskType + "_robot";
			}
		}
		if(!"true".equals(reserved)) {
			redisClient.set(Constants.CM_GLOBAL, key, "2", expireTime);
		}
		LogUtil.info("====精灵任务通知接口：taskid=" + processinstanceid + ",inscomcode=" + incoid + ",taskType=" + taskType + ",touserid=" + touserid + "，key=" + subTaskId + "_" + taskType + "_robot===");
		Map<String, Object> RequestMap = new HashMap<String, Object>();
		Map<String, String> messageMap = new HashMap<String, String>();

		if ("insure".equals(taskType) || "quote".equals(taskType)) {
			INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
			insbPolicyitem.setTaskid(processinstanceid);
			if(StringUtil.isNotEmpty(quoteinscom)){//用真实存在需要报价的公司
				insbPolicyitem.setInscomcode(quoteinscom);
			}else{
				insbPolicyitem.setInscomcode(incoid);
			}
			List<INSBPolicyitem> insbPolicyitemList = insbPolicyitemService.queryList(insbPolicyitem);
			if (insbPolicyitemList != null && insbPolicyitemList.size() > 0) {
				String msg = "历史";
				for (INSBPolicyitem dataInsbPolicyitem : insbPolicyitemList) {
					if ("1".equals(dataInsbPolicyitem.getRisktype()) && StringUtil.isNotEmpty(dataInsbPolicyitem.getProposalformno())) {
						msg += "交强险投保单号为：" + dataInsbPolicyitem.getProposalformno() + ",";
					} else if ("0".equals(dataInsbPolicyitem.getRisktype()) && StringUtil.isNotEmpty(dataInsbPolicyitem.getProposalformno())) {
						msg += "商业险投保单号为：" + dataInsbPolicyitem.getProposalformno() + ",";
					}
				}
				if (!"历史".equals(msg)) {
					if(StringUtil.isNotEmpty(quoteinscom)){//用真实存在需要报价的公司
						this.insertOrUpdateOperatorComment(processinstanceid, quoteinscom, "robot", "2", msg, "quote".equals(taskType) ? "1" : "9");
					}else{
						this.insertOrUpdateOperatorComment(processinstanceid, incoid, "robot", "2", msg, "quote".equals(taskType) ? "1" : "9");
					}
				}
			}

		}

		INSBQuotetotalinfo queryInsbQuotetotalinfo = new INSBQuotetotalinfo();
		queryInsbQuotetotalinfo.setTaskid(processinstanceid);
		queryInsbQuotetotalinfo = insbQuotetotalinfoService.queryOne(queryInsbQuotetotalinfo);
		if (!StringUtil.isEmpty(queryInsbQuotetotalinfo)) {
			messageMap.put("isrenewal", ("0".equals(queryInsbQuotetotalinfo.getIsrenewal())) ? "false" : "true");// 是否是续保-true续保单-false非续保单
		} else {
			messageMap.put("isrenewal", "false");// 是否是续保-true续保单-false非续保单
		}
		String dadikey = String.valueOf(redisClient.get(DADIKEY, subTaskId + "@" + incoid));
		if (!StringUtil.isEmpty(dadikey)) {
			messageMap.put("dadikey", dadikey);
		}
		//如果特约不为空，则传参给精灵处理
		String specialStr = String.valueOf(CMRedisClient.getInstance().get(3, SupplementCache.MODULE_NAME, processinstanceid+".ruleItem.specialAgreementList."+incoid));
		if(StringUtil.isNotEmpty(specialStr)){
			messageMap.put("specialStr", specialStr);
		}
		messageMap.put("taskType", taskType);
		messageMap.put("businessId", processinstanceid + "@" + incoid);// taskid,任务id
		messageMap.put("enquiryId", processinstanceid + "@" + incoid);// 单方询价号
		messageMap.put("isLocked", "false");// 是否锁定任务
		messageMap.put("processType", "robot");// 精灵、人工、edi//robot
		if("true".equals(reserved)){//备用平台查询则按照规则之前的拼装精灵需要的参数控制
			List<INSBQuoteinfo> insbQuoteinfos = insbQuoteinfoService.getQuoteinfosByInsbQuotetotalinfoid(queryInsbQuotetotalinfo.getId());
			for(INSBQuoteinfo insbQuoteinfo:insbQuoteinfos){
				if(insbQuoteinfo.getInscomcode().startsWith(incoid)){
					LogUtil.info("中止====精灵任务通知接口的备用平台查询包含在报价公司里,不启动：taskid=%s,inscomcode=%s,touserid=%s", processinstanceid, incoid, touserid);
					return JSONObject.fromObject(RequestMap).toString();
				}
			}
			messageMap.put("isReserved", "true");//备用平台标识true表示备用标识有效，false表示不是备用
			messageMap.put("requestDataUrl",
					"http://"+ValidateUtil.getConfigValue("localhost.ip")+":" + ValidateUtil.getConfigValue("localhost.port") + "/"+ValidateUtil.getConfigValue("localhost.projectName")
							+"/interface/getgzquerydata");
		}else{
			messageMap.put("isReserved", "false");//备用平台标识true表示备用标识有效，false表示不是备用
			messageMap.put("requestDataUrl",
					"http://" + ValidateUtil.getConfigValue("localhost.ip") + ":" + ValidateUtil.getConfigValue("localhost.port") + "/" + ValidateUtil.getConfigValue("localhost.projectName")
							+ "/interface/getpacket");
		}
		messageMap.put("ruleUrl",  ValidateUtil.getConfigValue("rule.engine.server.fairyip"));//精灵访问规则ip_url
		messageMap.put("platformSaveUrl",
				"http://" + ValidateUtil.getConfigValue("localhost.ip") + ":" + ValidateUtil.getConfigValue("localhost.port") + "/" + ValidateUtil.getConfigValue("localhost.projectName")
						+ "/workflow/saverulequeryinfo");//平台信息回写url
		INSBQuoteinfo dataInsbQuoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(processinstanceid, incoid);
		if(StringUtil.isNotEmpty(quoteinscom)){//用真实存在需要报价的公司
			dataInsbQuoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(processinstanceid, quoteinscom);
		}
		INSBAgreement insbAgreement = insbagreementservice.queryById(dataInsbQuoteinfo.getAgreementid());
		if(null != insbAgreement && StringUtil.isNotEmpty(insbAgreement.getAgreementtrule())){
			messageMap.put("ruleid", insbAgreement.getAgreementtrule());//规则id
		}else{
			messageMap.put("ruleid", "");//规则id
			LogUtil.info(incoid+"incoid====精灵任务通知接口无规则ruleid：taskid="+processinstanceid);
		}
		messageMap.put("isTry", "false");// 是否试用任务
		messageMap.put("companyId", incoid);// 保险公司id
		messageMap.put("callBackUrl",
				"http://" + ValidateUtil.getConfigValue("localhost.ip") + ":" + ValidateUtil.getConfigValue("localhost.port") + "/" + ValidateUtil.getConfigValue("localhost.projectName")
						+ callBackUrl);
		if(payCode_query.contains("insurequery@qrcode#") && StringUtil.isNotEmpty(orderPaymentId) && StringUtil.isNotEmpty(sid)) {//二维码核保查询组装数据给精灵
			messageMap.put("payCode_query", "insurequery@qrcode");
			messageMap.remove("callBackUrl");
			messageMap.put("callBackUrl", callBackUrl);
			messageMap.put("orderPaymentId", orderPaymentId);
			messageMap.put("sid", sid);
		}
		if(payCode_query.contains("approvedquery@qrcode")){
			messageMap.put("payCode_query", "approvedquery@qrcode");
		}

		try{
			INSBFlowinfo qinsbFlowinfo = new INSBFlowinfo();
			qinsbFlowinfo.setTaskid(processinstanceid);
			qinsbFlowinfo.setInscomcode(incoid);
			qinsbFlowinfo.setFiroredi("0");
			INSBFlowinfo dataInsbFlowinfo = insbFlowinfoService.queryOne(qinsbFlowinfo);
			if (dataInsbFlowinfo == null) {
				INSBFlowinfo insbFlowinfo = new INSBFlowinfo();
				insbFlowinfo.setOperator("sys");
				insbFlowinfo.setCreatetime(new Date());
				insbFlowinfo.setTaskid(processinstanceid);
				insbFlowinfo.setInscomcode(incoid);
				insbFlowinfo.setCurrenthandle(touserid);
				insbFlowinfo.setFlowcode(FlowInfo.quoteapply.getCode());
				insbFlowinfo.setFlowname(FlowInfo.quoteapply.getDesc());
				insbFlowinfo.setFiroredi("0");
				insbFlowinfo.setIslock("0");
				insbFlowinfo.setTaskstatus(taskType);
				insbFlowinfoService.insert(insbFlowinfo);
				insbFlowlogsService.logs(insbFlowinfo);
				messageMap.put("taskStatus", "0");// 流程状态
			} else {
				dataInsbFlowinfo.setOverflowcode(dataInsbFlowinfo.getFlowcode());
				dataInsbFlowinfo.setOverflowname(dataInsbFlowinfo.getFlowname());
				dataInsbFlowinfo.setModifytime(new Date());
				dataInsbFlowinfo.setCurrenthandle(touserid);
				dataInsbFlowinfo.setTaskstatus(taskType);
				dataInsbFlowinfo.setFlowcode(this.getFlowCodeByTaskType(taskType));
				dataInsbFlowinfo.setFlowname(this.getFlowNameByTaskType(taskType));
				messageMap.put("taskStatus", this.getFlowCodeByTaskType(taskType));
				insbFlowinfoService.updateById(dataInsbFlowinfo);
				insbFlowlogsService.logs(dataInsbFlowinfo);
			}
		}catch(Exception e){
			LogUtil.info("通知精灵保存流程错误信息异常，暂时不中断流程+taskid=" + processinstanceid);
			e.printStackTrace();
		}
		// ---------------
		Map<String, String> queryUrlMap = new HashMap<String, String>();
		queryUrlMap.put("deptid", dataInsbQuoteinfo.getDeptcode());
		queryUrlMap.put("providerid", incoid);
		String conftype = this.getConfTypeByTaskType(taskType);
//		if("true".equals(reserved)){//如果是备用则查询备用平台能力，且可以直接用平台公司deptcode去查询
//			conftype = "05";
//		}
		queryUrlMap.put("quotetype", "02");
		queryUrlMap.put("conftype", conftype);
		LogUtil.info("通知精灵启动targetUrl查找param=" + queryUrlMap);
		// ----------------
		List<Object> targetUrl = insbAutoconfigService.getEpathByAutoConfig4New(queryUrlMap);
		Map<String, String> result = new HashMap<String, String>();
		if (taskType.contains("insure") && "true".equals(messageMap.get("isrenewal"))) {
			result = MonitorUtil.go2Robot(processinstanceid, incoid, "xb", messageMap, targetUrl);
		} else {
			result = MonitorUtil.go2Robot(processinstanceid, incoid, taskType, messageMap, targetUrl);
		}
		try{
			if ("true".equals(result.get("flag").toString())) {
				INSBFlowinfo qinsbFlowinfo1 = new INSBFlowinfo();
				qinsbFlowinfo1.setTaskid(processinstanceid);
				qinsbFlowinfo1.setInscomcode(incoid);
				qinsbFlowinfo1.setFiroredi("0");
				qinsbFlowinfo1.setTaskstatus(taskType);
				INSBFlowinfo dataInsbFlowinfo1 = insbFlowinfoService.queryOne(qinsbFlowinfo1);
				dataInsbFlowinfo1.setOverflowcode(dataInsbFlowinfo1.getFlowcode());
				dataInsbFlowinfo1.setOverflowname(dataInsbFlowinfo1.getFlowname());
				dataInsbFlowinfo1.setModifytime(new Date());
				dataInsbFlowinfo1.setCurrenthandle(touserid);
				dataInsbFlowinfo1.setTaskstatus(taskType);
				dataInsbFlowinfo1.setFlowcode(this.getFlowCodeByTaskType(taskType));
				dataInsbFlowinfo1.setFlowname(this.getFlowNameByTaskType(taskType));
				insbFlowinfoService.updateById(dataInsbFlowinfo1);
				insbFlowlogsService.logs(dataInsbFlowinfo1);
			} else {
				INSBFlowinfo qinsbFlowinfo1 = new INSBFlowinfo();
				qinsbFlowinfo1.setTaskid(processinstanceid);
				qinsbFlowinfo1.setInscomcode(incoid);
				qinsbFlowinfo1.setFiroredi("0");
				INSBFlowinfo dataInsbFlowinfo1 = insbFlowinfoService.queryOne(qinsbFlowinfo1);
				if (taskType.contains("insure") && "true".equals(messageMap.get("isrenewal"))) {
					dataInsbFlowinfo1.setFlowcode(FlowInfo.xb.getCode());
					dataInsbFlowinfo1.setFlowname(FlowInfo.xb.getDesc());
					insertOrUpdateFlowError(dataInsbFlowinfo1, result.get("msg"));
				} else {
					insertOrUpdateFlowError(dataInsbFlowinfo1, result.get("msg"));
				}

				RequestMap.put("result", false);
				RequestMap.put("processinstanceid", processinstanceid);
				RequestMap.put("incoid", incoid);
				RequestMap.put("ediid", touserid);
				RequestMap.put("taskType", taskType);
				RequestMap.put("flag", result.get("flag"));
				RequestMap.put("msg", result.get("msg"));

				LogUtil.info("失败====精灵任务通知接口：taskid=%s,inscomcode=%s,touserid=%s,返回结果：%s", processinstanceid, incoid, touserid, result);
				return JSONObject.fromObject(RequestMap).toString();
			}
		}catch(Exception e){
			LogUtil.info("通知精灵保存流程错误信息异常，暂时不中断流程2+taskid=" + processinstanceid);
			e.printStackTrace();
		}
		RequestMap.put("result", true);
		RequestMap.put("processinstanceid", processinstanceid);
		RequestMap.put("incoid", incoid);
		RequestMap.put("ediid", touserid);
		RequestMap.put("taskType", taskType);
		RequestMap.put("flag", result.get("flag"));
		RequestMap.put("msg", result.get("msg"));

		LogUtil.info("结束====精灵任务通知接口：taskid=" + processinstanceid + ",inscomcode=" + incoid + ",touserid=" + touserid + ",返回结果：" + result + "====");
		return JSONObject.fromObject(RequestMap).toString();
	}

	/**
	 * 工作流调用edi任务接口
	 */
	@Override
	public String goToEdiQuote(String processinstanceid, String incoid, String ediid, String taskType) throws Exception {
		LogUtil.info("====EDI任务通知接口：taskid=" + processinstanceid + ",inscomcode=" + incoid + ",taskType=" + taskType + ",ediid=" + ediid + "====");
		String subTaskId = this.getChildTaskId(processinstanceid, incoid);
		String key = "";
		String callBackUrl = "/interface/savePacket";
		if(taskType.contains("approvedquery@qrcode")){	//待支付承保查询
			taskType = "approvedquery";
			callBackUrl = "/interface/saveQRCodePacket";
		}
		if ("approved".equals(taskType)) {
			taskType = "approvedquery";
		}
		if ("approvedquery".equals(taskType)) {
			key = processinstanceid + "_" + "approved" + "_edi";
		} else {
			if (taskType.contains("query")) {
				key = subTaskId + "_" + taskType.replace("query", "") + "_edi";
			} else {
				key = subTaskId + "_" + taskType + "_edi";
			}
		}
		redisClient.set(Constants.CM_GLOBAL, key, "2", expireTime);
		LogUtil.info("====EDI任务通知接口：taskid=" + processinstanceid + ",inscomcode=" + incoid + ",taskType=" + taskType + ",key=" + key + "，set的keyvalue:" + redisClient.get(Constants.CM_GLOBAL, key)
				+ "===");
		Map<String, Object> RequestMap = new HashMap<String, Object>();
		Map<String, String> ediRequestMap = new HashMap<String, String>();

		if ("insure".equals(taskType) || "quote".equals(taskType)) {
			INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
			insbPolicyitem.setTaskid(processinstanceid);
			insbPolicyitem.setInscomcode(incoid);
			List<INSBPolicyitem> insbPolicyitemList = insbPolicyitemService.queryList(insbPolicyitem);
			if (insbPolicyitemList != null && insbPolicyitemList.size() > 0) {
				String msg = "历史";
				for (INSBPolicyitem dataInsbPolicyitem : insbPolicyitemList) {
					if ("1".equals(dataInsbPolicyitem.getRisktype()) && StringUtil.isNotEmpty(dataInsbPolicyitem.getProposalformno())) {
						msg += "交强险投保单号为：" + dataInsbPolicyitem.getProposalformno() + ",";
					} else if ("0".equals(dataInsbPolicyitem.getRisktype()) && StringUtil.isNotEmpty(dataInsbPolicyitem.getProposalformno())) {
						msg += "商业险投保单号为：" + dataInsbPolicyitem.getProposalformno() + ",";
					}
				}
				if (!"历史".equals(msg)) {
					this.insertOrUpdateOperatorComment(processinstanceid, incoid, "edi", "2", msg, "quote".equals(taskType) ? "1" : "9");
				}
			}

		}

		INSBQuotetotalinfo queryInsbQuotetotalinfo = new INSBQuotetotalinfo();
		queryInsbQuotetotalinfo.setTaskid(processinstanceid);
		queryInsbQuotetotalinfo = insbQuotetotalinfoService.queryOne(queryInsbQuotetotalinfo);
		if (!StringUtil.isEmpty(queryInsbQuotetotalinfo)) {
			ediRequestMap.put("isrenewal", ("0".equals(queryInsbQuotetotalinfo.getIsrenewal())) ? "false" : "true");// 是否是续保-true续保单-false非续保单
		} else {
			ediRequestMap.put("isrenewal", "false");// 是否是续保-true续保单-false非续保单
		}
		ediRequestMap.put("taskType", taskType);
		ediRequestMap.put("callBackUrl",
				"http://" + ValidateUtil.getConfigValue("localhost.ip") + ":" + ValidateUtil.getConfigValue("localhost.port") + "/" + ValidateUtil.getConfigValue("localhost.projectName")
						+ callBackUrl);
		ediRequestMap.put("requestDataUrl",
				"http://" + ValidateUtil.getConfigValue("localhost.ip") + ":" + ValidateUtil.getConfigValue("localhost.port") + "/" + ValidateUtil.getConfigValue("localhost.projectName")
						+ "/interface/getpacket");
		ediRequestMap.put("taskId", processinstanceid + "@" + incoid);
		ediRequestMap.put("processType", "edi");// 精灵、人工、edi//robot
		//如果特约不为空，则传参给精灵处理
		String specialStr = String.valueOf(CMRedisClient.getInstance().get(3, SupplementCache.MODULE_NAME, processinstanceid+".ruleItem.specialAgreementList."+incoid));
		if(StringUtil.isNotEmpty(specialStr)){
			ediRequestMap.put("specialStr", specialStr);
		}
		INSBFlowinfo qinsbFlowinfo = new INSBFlowinfo();
		qinsbFlowinfo.setTaskid(processinstanceid);
		qinsbFlowinfo.setInscomcode(incoid);
		qinsbFlowinfo.setFiroredi("1");
		INSBFlowinfo dataInsbFlowinfo = insbFlowinfoService.queryOne(qinsbFlowinfo);
		if (dataInsbFlowinfo == null) {
			INSBFlowinfo insbFlowinfo = new INSBFlowinfo();
			insbFlowinfo.setOperator("sys");
			insbFlowinfo.setCreatetime(new Date());
			insbFlowinfo.setTaskid(processinstanceid);
			insbFlowinfo.setInscomcode(incoid);
			insbFlowinfo.setCurrenthandle(ediid);
			insbFlowinfo.setFlowcode(FlowInfo.quoteapply.getCode());
			insbFlowinfo.setFlowname(FlowInfo.quoteapply.getDesc());
			insbFlowinfo.setFiroredi("1");
			insbFlowinfo.setIslock("0");
			insbFlowinfo.setTaskstatus(taskType);
			insbFlowinfoService.insert(insbFlowinfo);
			insbFlowlogsService.logs(insbFlowinfo);
			ediRequestMap.put("taskStatus", FlowInfo.quoteapply.getCode());
		} else {
			dataInsbFlowinfo.setOverflowcode(dataInsbFlowinfo.getFlowcode());
			dataInsbFlowinfo.setOverflowname(dataInsbFlowinfo.getFlowname());
			dataInsbFlowinfo.setModifytime(new Date());
			dataInsbFlowinfo.setCurrenthandle(ediid);
			dataInsbFlowinfo.setTaskstatus(taskType);
			dataInsbFlowinfo.setFlowcode(this.getFlowCodeByTaskType(taskType));
			dataInsbFlowinfo.setFlowname(this.getFlowNameByTaskType(taskType));
			ediRequestMap.put("taskStatus", this.getFlowCodeByTaskType(taskType));
			insbFlowinfoService.updateById(dataInsbFlowinfo);
			insbFlowlogsService.logs(dataInsbFlowinfo);
		}
		INSBQuoteinfo dataInsbQuoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(processinstanceid, incoid);
		ediRequestMap.put("buybusitype", StringUtil.isEmpty(dataInsbQuoteinfo.getBuybusitype()) ? "01" : dataInsbQuoteinfo.getBuybusitype());
		// ---------------
		INSBQuoteinfo dataInsbQuoteinfo1 = insbQuoteinfoService.getByTaskidAndCompanyid(processinstanceid, incoid);
		Map<String, String> queryUrlMap = new HashMap<String, String>();
		queryUrlMap.put("deptid", dataInsbQuoteinfo1.getDeptcode());
		queryUrlMap.put("providerid", incoid);
		String conftype = this.getConfTypeByTaskType(taskType);
		queryUrlMap.put("quotetype", "01");
		queryUrlMap.put("conftype", conftype);
		// ----------------
		List<Object> targetUrl = insbAutoconfigService.getEpathByAutoConfig4New(queryUrlMap);
		Map<String, String> result = new HashMap<String, String>();
		if (taskType.contains("insure") && "true".equals(ediRequestMap.get("isrenewal"))) {
			result = MonitorUtil.go2Edi(processinstanceid, incoid, "xb", ediRequestMap, targetUrl);
		} else {
			result = MonitorUtil.go2Edi(processinstanceid, incoid, taskType, ediRequestMap, targetUrl);
		}
		if ("true".equals(result.get("flag").toString())) {
			INSBFlowinfo qinsbFlowinfo1 = new INSBFlowinfo();
			qinsbFlowinfo1.setTaskid(processinstanceid);
			qinsbFlowinfo1.setInscomcode(incoid);
			qinsbFlowinfo1.setFiroredi("1");
			INSBFlowinfo dataInsbFlowinfo1 = insbFlowinfoService.queryOne(qinsbFlowinfo1);
			dataInsbFlowinfo1.setOverflowcode(dataInsbFlowinfo1.getFlowcode());
			dataInsbFlowinfo1.setOverflowname(dataInsbFlowinfo1.getFlowname());
			dataInsbFlowinfo1.setModifytime(new Date());
			dataInsbFlowinfo1.setCurrenthandle(ediid);
			dataInsbFlowinfo1.setTaskstatus(taskType);
			dataInsbFlowinfo1.setFlowcode(this.getFlowCodeByTaskType(taskType));
			dataInsbFlowinfo1.setFlowname(this.getFlowNameByTaskType(taskType));
			insbFlowinfoService.updateById(dataInsbFlowinfo1);
			insbFlowlogsService.logs(dataInsbFlowinfo1);
		} else {
			INSBFlowinfo qinsbFlowinfo1 = new INSBFlowinfo();
			qinsbFlowinfo1.setTaskid(processinstanceid);
			qinsbFlowinfo1.setInscomcode(incoid);
			qinsbFlowinfo1.setFiroredi("1");
			INSBFlowinfo dataInsbFlowinfo1 = insbFlowinfoService.queryOne(qinsbFlowinfo1);
			if (taskType.contains("insure") && "true".equals(ediRequestMap.get("isrenewal"))) {
				dataInsbFlowinfo1.setFlowcode(FlowInfo.xb.getCode());
				dataInsbFlowinfo1.setFlowname(FlowInfo.xb.getDesc());
				insertOrUpdateFlowError(dataInsbFlowinfo1, result.get("msg"));
			} else {
				insertOrUpdateFlowError(dataInsbFlowinfo1, result.get("msg"));
			}
		}
		RequestMap.put("result", true);
		RequestMap.put("processinstanceid", processinstanceid);
		RequestMap.put("incoid", incoid);
		RequestMap.put("ediid", ediid);
		RequestMap.put("taskType", taskType);
		RequestMap.put("flag", result.get("flag"));
		RequestMap.put("msg", result.get("msg"));

		LogUtil.info("结束====EDI任务通知接口：taskid=" + processinstanceid + ",inscomcode=" + incoid + ",ediid=" + ediid + ",edi返回信息：" + result + ";====");
		return JSONObject.fromObject(RequestMap).toString();
	}

	/**
	 * 数据大对象
	 */
	@Override
	public Map<String, Object> getPacket(String taskId, String insurancecompanyid, String processType, String monitorid, String taskType) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (StringUtil.isEmpty(processType)) {
			resultMap.put("error", "processType字段为空或不存在，请检查请求报文");
			return resultMap;
		}
		if (StringUtil.isEmpty(monitorid)) {
			resultMap.put("error", "monitorid字段为空或不存在，请检查请求报文");
			return resultMap;
		}
		if (StringUtil.isEmpty(taskType)) {
			resultMap.put("error", "taskType字段为空或不存在，请检查请求报文");
			return resultMap;
		}
		LogUtil.info("进入====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====");

		try {
			//如果特约不为空，则传参给精灵处理
			String specialStr = String.valueOf(CMRedisClient.getInstance().get(3, SupplementCache.MODULE_NAME, taskId+".ruleItem.specialAgreementList."+insurancecompanyid));
			if(StringUtil.isNotEmpty(specialStr)){
				LogUtil.info("进入====数据获取特约信息：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "===="+specialStr);
				resultMap.put("specialStr", specialStr);
			}
			INSBQuoteinfo dataInsbQuoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(taskId, insurancecompanyid);
			String deptid = dataInsbQuoteinfo.getDeptcode();
			ConfigInfo configInfo = appQuotationService.getConfigInfo(insurancecompanyid, deptid, processType);
			if (StringUtil.isEmpty(configInfo)) {
				LogUtil.error("进入====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====配置信息:(无);over");
			} else {
				LogUtil.info("进入====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====配置信息:" + configInfo.getConfigMap() + ";over");
			}
			CarInfo carInfo = this.getCarInfo(taskId, insurancecompanyid);// 车信息
			LogUtil.info("进入====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====车信息over");
			CarOwnerInfo carOwnerInfo = this.getCarOwnerInfo(taskId, insurancecompanyid);// 车主信息
			LogUtil.info("进入====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====车主信息over");
			SubAgentInfo subAgentInfo = this.getSubAgentInfo(taskId, insurancecompanyid);// 用户信息
			LogUtil.info("进入====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====用户信息over");
			AgentInfo agentInfo = this.getAgentInfo(taskId, insurancecompanyid);// 代理人信息
			LogUtil.info("进入====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====代理人信息over");
			BaseSuiteInfo baseSuiteInfo = this.getBaseSuiteInfo(taskId, insurancecompanyid);// 投保基本信息：（交强险信息、车船税信息、商业险信息（险种配置））
			if("1111_2222".equals(monitorid)){//平台查询组装数据，将起保日期默认T+1
				if(null!=baseSuiteInfo.getBizSuiteInfo()){
					baseSuiteInfo.getBizSuiteInfo().setStart(DateUtil.parse((String) InterFaceDefaultValueUtil.getDefaultValue(taskId, insurancecompanyid,"INSBPolicyitem", "startdate")));
					baseSuiteInfo.getBizSuiteInfo().setEnd(DateUtil.parse((String) InterFaceDefaultValueUtil.getDefaultValue(taskId, insurancecompanyid,"INSBPolicyitem", "enddate")));
				}
				if(null!=baseSuiteInfo.getEfcSuiteInfo()){
					baseSuiteInfo.getEfcSuiteInfo().setStart(DateUtil.parse((String) InterFaceDefaultValueUtil.getDefaultValue(taskId, insurancecompanyid,"INSBPolicyitem", "startdate")));
					baseSuiteInfo.getEfcSuiteInfo().setEnd(DateUtil.parse((String) InterFaceDefaultValueUtil.getDefaultValue(taskId, insurancecompanyid,"INSBPolicyitem", "enddate")));
				}
			}
			LogUtil.info("进入====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====投保基本信息over");
			PersonInfo applicantPersonInfo = this.getApplicantPersonInfo(taskId, insurancecompanyid);// 投保人信息
			LogUtil.info("进入====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====投保人信息over");
			List<BeneficiaryPerson> beneficiaryPersonList = this.getBeneficiaryPersonList(taskId, insurancecompanyid);// 受益人列表
			LogUtil.info("进入====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====受益人列表over");
			List<InsurePerson> insuredPersonInfoList = this.getInsuredPersonInfo(taskId, insurancecompanyid);// 被保人信息
			LogUtil.info("进入====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====被保人信息over");
			List<DriverPerson> driverPersonList = this.getDriverPersonlist(taskId, insurancecompanyid);// 驾驶人列表
			LogUtil.info("进入====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====驾驶人列表over");
			List<ProviderInfo> providerInfoList = this.getProviderInfoList(taskId, insurancecompanyid);// 供应商列表
			LogUtil.info("进入====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====供应商列表over");
			List<TrackInfo> trackInfoList = this.getTrackInfoList(taskId);// 任务流程轨迹
			LogUtil.info("进入====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====任务流程轨迹over");
			DeliverInfo deliverInfo = this.getDeliverInfo(taskId, insurancecompanyid, agentInfo.getWorkerID());// 配送信息
			LogUtil.info("进入====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====配送信息over");
			Invoiceinfo invoiceinfo = this.getInvoiceinfo(taskId, insurancecompanyid);// 营改增
			LogUtil.info("进入====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====发票信息over");
			SQ sq = this.getSQ(taskId, insurancecompanyid);// SQ
			LogUtil.info("进入====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====SQ over");
			SpecialRisk specialRisk = this.getSpecialRisk(taskId, insurancecompanyid);// SpecialRisk
			LogUtil.info("进入====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====SpecialRisk特殊险种over");
			resultMap.put("configInfo", configInfo);
			resultMap.put("carInfo", carInfo);
			resultMap.put("carOwnerInfo", carOwnerInfo);
			resultMap.put("subAgentInfo", subAgentInfo);
			resultMap.put("agentInfo", agentInfo);
			resultMap.put("baseSuiteInfo", baseSuiteInfo);
			resultMap.put("applicantPersonInfo", applicantPersonInfo);
			resultMap.put("beneficiaryPersonList", beneficiaryPersonList);
			resultMap.put("insuredPersonInfoList", insuredPersonInfoList);
			resultMap.put("driverPersonList", driverPersonList);
			resultMap.put("providerInfoList", providerInfoList);
			resultMap.put("trackInfoList", trackInfoList);
			resultMap.put("deliverInfo", deliverInfo);
			resultMap.put("invoiceinfo", invoiceinfo);
			resultMap.put("sq", sq);
			resultMap.put("specialRisk", specialRisk);
			resultMap.put("businessId", taskId + "@" + insurancecompanyid);
			resultMap.put("singlesite", deptid);// 出单网点
			resultMap.put("orgCode", inscDeptService.getPingTaiDeptId(dataInsbQuoteinfo.getDeptcode()));// 法人机构代码
			INSBQuotetotalinfo queryInsbQuotetotalinfo = new INSBQuotetotalinfo();
			queryInsbQuotetotalinfo.setTaskid(taskId);
			INSBQuotetotalinfo dataInsbQuotetotalinfo = insbQuotetotalinfoService.queryOne(queryInsbQuotetotalinfo);
			Map<String, String> insAreaMap = new HashMap<String, String>();
			insAreaMap.put("province", dataInsbQuotetotalinfo.getInsprovincecode());// 投保地区（省份）
			insAreaMap.put("city", dataInsbQuotetotalinfo.getInscitycode());// 投保地区（城市）
			resultMap.put("insArea", insAreaMap);
			resultMap.put("insComId", insurancecompanyid.substring(0, 4));// 保险公司，即总公司的编码，长度4位
			resultMap.put("buybusitype", StringUtil.isEmpty(dataInsbQuoteinfo.getBuybusitype()) ? "01" : dataInsbQuoteinfo.getBuybusitype());// 数据类型：传统，网销
			if (!StringUtil.isEmpty(dataInsbQuotetotalinfo)) {
				resultMap.put("isrenewal", ("0".equals(dataInsbQuotetotalinfo.getIsrenewal())) ? "false" : "true");// 是否是续保-true续保单-false非续保单
				resultMap.put("renewalquoteitem", insbRenewalService.getRenewalQuoteItems(taskId, insurancecompanyid));
			} else {
				resultMap.put("isrenewal", "false");// 是否是续保-true续保单-false非续保单
			}

			String carPriceType = dataInsbQuoteinfo.getInsureway();
			if (StringUtil.isEmpty(carPriceType)) {
				try {
					String result = appQuotationService.getPriceInterval(dataInsbQuoteinfo.getWorkflowinstanceid(), taskId, insurancecompanyid,
							carInfo.getTaxPrice().doubleValue(), carInfo.getPrice().doubleValue(), carInfo.getTaxAnalogyPrice().doubleValue(), carInfo.getAnalogyPrice().doubleValue(),
							DateUtil.toString(carInfo.getFirstRegDate(), DateUtil.Format_Date), carInfo.getSeatCnt());
					JSONObject JudgeJsonObject = JSONObject.fromObject(result);
					carPriceType = String.valueOf(JudgeJsonObject.get("ruleItem.carPriceType"));
				}catch(Exception e){
					LogUtil.info("进入====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====" + "获取规则carPriceType错误: " + e.getMessage());
					e.getStackTrace();
				}
			}

			resultMap.put("carPriceType", carPriceType);

			if ("不含税类比价".equals(carPriceType) && (carInfo.getAnalogyPrice() == null || carInfo.getAnalogyPrice().doubleValue() == 0.0)) {
				carInfo.setAnalogyPrice(carInfo.getPrice());
			} else if ("含税类比价".equals(carPriceType) && (carInfo.getTaxAnalogyPrice() == null || carInfo.getTaxAnalogyPrice().doubleValue() == 0.0)) {
				carInfo.setTaxAnalogyPrice(carInfo.getTaxPrice());
			}

			resultMap.put("supplyParam", insbInsuresupplyparamDao.getByTask(taskId, insurancecompanyid));

			LogUtil.info("结束====数据获取接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "====获取数据为："
					+ com.alibaba.fastjson.JSONObject.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat));

			return resultMap;
		} catch (Exception e) {
			INSBFlowinfo queryInsbFlowinfo = new INSBFlowinfo();
			queryInsbFlowinfo.setTaskid(taskId);
			queryInsbFlowinfo.setInscomcode(insurancecompanyid);
			queryInsbFlowinfo.setFiroredi("robot".equalsIgnoreCase(processType) ? "0" : "1");
			INSBFlowinfo dataInsbFlowinfo = insbFlowinfoService.queryOne(queryInsbFlowinfo);
			insertOrUpdateFlowError(dataInsbFlowinfo, "获取cm数据出错：" + e.getMessage());
			return resultMap;
		}
	}

	/**
	 * 数据回写,保存大对象
	 */
	@Override
	public Map<String, String> savePacket(String json) {
		JSONObject dataPacket = JSONObject.fromObject(json);
		Map<String, String> resultMap = new HashMap<String, String>();
		// 取出数据
		if (StringUtil.isEmpty(dataPacket.get("businessId")) || "null".equalsIgnoreCase(dataPacket.get("businessId").toString())) {
			LogUtil.info("进入====数据回写接口：错误,key：businessId,不存在====");
			resultMap.put("result", "error");
			resultMap.put("msg", "businessId不存在");
			return resultMap;
		}
		String businessId = (String) dataPacket.get("businessId");
		if (businessId.indexOf("@") < 0) {
			LogUtil.info("进入====数据回写接口：错误,businessId格式不对,没有@符号====");
			resultMap.put("result", "error");
			resultMap.put("msg", "businessId格式不对,没有@符号");
			return resultMap;
		}
		if (StringUtil.isEmpty(dataPacket.get("taskStatus")) || "null".equalsIgnoreCase(dataPacket.get("taskStatus").toString())) {
			LogUtil.info("进入====数据回写接口：错误,key：taskStatus,不存在====");
			resultMap.put("result", "error");
			resultMap.put("msg", "taskStatus不存在");
			return resultMap;
		}
		if (StringUtil.isEmpty(dataPacket.get("processType")) || "null".equalsIgnoreCase(dataPacket.get("processType").toString())) {// processType：edi/robot
			LogUtil.info("进入====数据回写接口：错误,key：processType,不存在====");
			resultMap.put("result", "error");
			resultMap.put("msg", "processType不存在");
			return resultMap;
		}
		if (StringUtil.isEmpty(dataPacket.get("buybusitype")) || "null".equalsIgnoreCase(dataPacket.get("buybusitype").toString())) {// buybusitype
			LogUtil.info("进入====数据回写接口：错误,key：buybusitype,不存在====");
			resultMap.put("result", "error");
			resultMap.put("msg", "buybusitype不存在");
			return resultMap;
		}
		/*if (StringUtil.isEmpty(dataPacket.get("monitorid")) || "null".equalsIgnoreCase(dataPacket.get("monitorid").toString())) {
			resultMap.put("error", "monitorid字段为空或不存在，请检查请求报文");
			return resultMap;
		}*/
		if (StringUtil.isEmpty(dataPacket.get("taskType")) || "null".equalsIgnoreCase(dataPacket.get("taskType").toString())) {
			resultMap.put("error", "taskType字段为空或不存在，请检查请求报文");
			return resultMap;
		}

		String processType = dataPacket.get("processType").toString().trim();
		String taskStatus = dataPacket.get("taskStatus").toString().trim();
		//String taskType = dataPacket.get("taskType").toString().trim();
		if ("21".equals(taskStatus)) {
			taskStatus = "D1";
		}
		if ("20".equals(taskStatus)) {
			taskStatus = "D";
		}
		String closeName = "";
		String redisTaskType = "";
		if ("robot".equals(processType)) {
			closeName = "精灵";
		} else {
			closeName = "EDI";
		}

		//String monitorId = dataPacket.get("monitorid").toString();
		String taskCode = null;
		String buybusitype = dataPacket.get("buybusitype").toString().trim();
		String taskId = businessId.split("@")[0];
		String insurancecompanyid = businessId.split("@")[1];
		Map<String, Object> flowInfoCode = this.getFlowInfoCode(taskStatus);
		String taskName = flowInfoCode.get("codename").toString();
		String subTaskId = this.getChildTaskId(taskId, insurancecompanyid);
		String jobName = "", feedbackSubTaskId = subTaskId;

		if (taskName.contains("报价")) {
			redisTaskType = "quote";
			closeName += "报价";
			jobName = subTaskId + "_" + closeName;
		} else if (taskName.contains("自动核保")){
			redisTaskType = "autoinsure";
			closeName += "自动核保";
			jobName = subTaskId + "_" + closeName;
		} else if (taskName.contains("核保")) {
			redisTaskType = "insure";
			closeName += "核保暂存";
			jobName = subTaskId + "_" + closeName;
		} else if (taskName.contains("支付")) {
			redisTaskType = "pay";
			closeName += "支付";
		} else if (taskName.contains("承保")) {
			redisTaskType = "approved";
			closeName += "承保";
			jobName = taskId + "_" + closeName;
		} else {
			redisTaskType = "quote";
			closeName += "报价";
		}
		LogUtil.info("进入====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",接收到的报文时:" + json + "====");

		if (taskName.contains("报价")) {
			INSBRulequerycarinfo insbRulequerycarinfo = new INSBRulequerycarinfo();
			insbRulequerycarinfo.setTaskid(taskId);
			List<INSBRulequerycarinfo> list = insbRulequerycarinfoDao.selectList(insbRulequerycarinfo);
			String status = String.valueOf(CMRedisClient.getInstance().get(Constants.CM_GLOBAL, taskId + "startedBakQuery"));
			if(list == null || list.isEmpty() || !"guizequery".equals(list.get(0).getOperator()) || (!StringUtil.isEmpty(status) && status.equals("0"))){
				int isoverAutoquote = 0;
				INSBWorkflowsub param = new INSBWorkflowsub();
				param.setMaininstanceid(taskId);
				List<INSBWorkflowsub> subs = insbWorkflowsubDao.selectList(param);
				for(INSBWorkflowsub sub:subs){
					if(!subTaskId.equals(sub.getInstanceid()) &&
							(TaskConst.QUOTING_3.equals(sub.getTaskcode())||TaskConst.QUOTING_4.equals(sub.getTaskcode()))){
						isoverAutoquote++;
						LogUtil.info("taskid=" + taskId + ",subtaskid=" + subTaskId + ",还有报价未返回");
					}
				}

				if(isoverAutoquote <= 1){// 所有报价已完成并且没有平台信息
					LogUtil.info("taskid=" + taskId + ",subtaskid=" + subTaskId + ",CM中未查到平台信息,启动备用平台查询");
					String tmpJobName = subTaskId + "_平台查询_"+taskId;
					long nowDate = new Date().getTime();
					nowDate = nowDate + 1000; // 1秒超时,相当于立即启动备用平台查询
					Date startTime = new Date(nowDate);
					try {
						schedule.deleteHistoryJobAndStartNewJob(tmpJobName, "平台查询", startTime, "", "1000", "TaskOvertimeJob4EdiFairy");
						LogUtil.info("taskid=" + taskId + ",subtaskid=" + subTaskId + ","+ isoverAutoquote+"=isoverAutoquote,精灵报价回写平台查询加入调度成功,jobname=" + tmpJobName + ",执行时间=" + DateUtil.toDateTimeString(startTime));
					} catch (SchedulerException e) {
						LogUtil.info("taskid=" + taskId + ",subtaskid=" + subTaskId + ",精灵报价回写平台查询加入调度失败,jobname=" + tmpJobName);
						e.printStackTrace();
					}
				}
			}
		}

		String rdKey = "";
		if (taskName.contains("承保")) {
			rdKey = taskId + "_" + redisTaskType + "_" + processType;
		} else {
			rdKey = subTaskId + "_" + redisTaskType + "_" + processType;
		}
		int keyValue = redisClient.atomicIncr(Constants.CM_GLOBAL, rdKey);
		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",key是：" + rdKey + ",拿到keyValue：" + keyValue + "===");

		if (!dataPacket.containsKey("oper") || !"52a4dd37160ecacc502910c9a9d7ffea".equals(Md5Encodes.encodeMd5(dataPacket.get("oper").toString()))) {
			if (3 != keyValue) {// 超时锁的机制
				LogUtil.info("进入====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",已经超时了====");
				resultMap.put("result", "error");
				resultMap.put("msg", "已经超时，回写内容将不保存");
				redisClient.del(Constants.CM_GLOBAL, rdKey);
				this.insertOrUpdateOperatorComment(taskId, insurancecompanyid, processType, "0", resultMap.get("msg"), taskStatus);
				return resultMap;
			} else {
				try {
					//deleteHistoryJob(sched, jobName, closeName, taskId, insurancecompanyid);
				} catch (Exception e) {
					LogUtil.info("====删除定时器失败：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ", " + jobName + "====");
				}
				redisClient.del(Constants.CM_GLOBAL, rdKey);
				LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",删除key=" + rdKey + "===");
			}
		}

		INSBFlowinfo queryInsbFlowinfo = new INSBFlowinfo();
		queryInsbFlowinfo.setTaskid(taskId);
		queryInsbFlowinfo.setInscomcode(insurancecompanyid);
		queryInsbFlowinfo.setFiroredi("robot".equalsIgnoreCase(processType) ? "0" : "1");
		INSBFlowinfo dataInsbFlowinfo = insbFlowinfoService.queryOne(queryInsbFlowinfo);
		if (dataInsbFlowinfo == null) {
			LogUtil.info("错误==没有查到该流程的子流程信息insbflowinfo表==数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",taskStatus=" + taskStatus + ",processType=" + processType + "====");
			resultMap.put("result", "error");
			resultMap.put("msg", "没有查到该流程的子流程信息insbflowinfo表");
			return resultMap;
		}

		LogUtil.info("==更新子流程：由‘" + dataInsbFlowinfo.getFlowname() + "’变为‘" + taskName
				+ "’==数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",taskStatus=" + taskStatus + ",processType=" + processType + "====");
		String toUserId = dataInsbFlowinfo.getCurrenthandle();
		if ("admin".equalsIgnoreCase(toUserId)) {
			toUserId = "admin";
		}

		INSBWorkflowsub thisWorkflowsub = null;
		if (taskName.contains("承保")) {
			INSBWorkflowmain queryInsbWorkflowmain = new INSBWorkflowmain();
			queryInsbWorkflowmain.setInstanceid(taskId);
			queryInsbWorkflowmain = insbWorkflowmainService.queryOne(queryInsbWorkflowmain);
			if (queryInsbWorkflowmain != null) {
				taskCode = queryInsbWorkflowmain.getTaskcode();
			}
		} else {
			thisWorkflowsub = new INSBWorkflowsub();
			thisWorkflowsub.setInstanceid(subTaskId);
			thisWorkflowsub = insbWorkflowsubService.queryOne(thisWorkflowsub);
			if (thisWorkflowsub != null) {
				taskCode = thisWorkflowsub.getTaskcode();
			}
		}

		if (approvedTaskcode.contains(taskCode)) {
			feedbackSubTaskId = null;
		}

		//是否停止核保轮询
		boolean stopLoopUnderWriting = true;
		boolean logLoopUnderWriting = false;

		try {
			INSBCarinfohis queryInsbCarinfohis = new INSBCarinfohis();
			queryInsbCarinfohis.setTaskid(taskId);
			queryInsbCarinfohis.setInscomcode(insurancecompanyid);
			queryInsbCarinfohis = insbCarinfohisService.queryOne(queryInsbCarinfohis);

			/**
			 * 验证车架号和引擎号是否同先前数据一致，如果不一致回写失败信息
			 */
			if (!StringUtil.isEmpty(queryInsbCarinfohis)) {
				if (dataPacket.get("carInfo") != null && dataPacket.get("isrenewal") != null && "false".equals(dataPacket.get("isrenewal").toString())) {
					JSONObject carInfo = JSONObject.fromObject(dataPacket.get("carInfo"));// 车信息
					if (!StringUtil.isEmpty(carInfo.get("vin")) && !StringUtil.isEmpty(carInfo.get("engineNum"))) {
						if (!carInfo.get("vin").toString().trim().equals(queryInsbCarinfohis.getVincode().trim()) || !carInfo.get("engineNum").toString().trim().equals(queryInsbCarinfohis.getEngineno().trim())) {
							String msg = "回写vin码是：" + carInfo.get("vin").toString() + ",回写发动机号是：" + carInfo.get("engineNum").toString() +
									",提交的vin码是:" + queryInsbCarinfohis.getVincode() + ",提交的发动机号是:" + queryInsbCarinfohis.getEngineno() + ",不一致";
							LogUtil.info("vin码、发动机号不一致，进入====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ","+msg+"====");
							resultMap.put("result", "error");
							resultMap.put("msg", msg);
							this.insertOrUpdateOperatorComment(taskId, insurancecompanyid, processType, "0", msg, taskStatus);
							WorkflowFeedbackUtil.setWorkflowFeedback(taskId, feedbackSubTaskId, taskCode, "Completed", taskName, WorkflowFeedbackUtil.writeback_inconformity, "admin");
							this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "false", closeName, taskStatus, null, taskName, subTaskId);
							return resultMap;
						}
					}
				}
			}

			/**
			 * 校验失败，更新错误备注insboperatorcomment
			 * 回写错误信息
			 * 通知监控
			 */
			if (FlowInfo.quoteover.getCode().equals(taskStatus) || FlowInfo.underwritingover.getCode().equals(taskStatus)
					|| FlowInfo.insover.getCode().equals(taskStatus) || FlowInfo.autoinsureover.getCode().equals(taskStatus)) {
				/**
				 * 校验保费，校验结果不为空，则校验失败
				 */
				String validateResult = this.valiedatePremiu(dataPacket);

				if (!StringUtil.isEmpty(validateResult)) {
					LogUtil.info("校验结果不为空====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",taskStatus=" + taskStatus + ",processType=" + processType + ",校验结果：" + validateResult + "====");
					this.insertOrUpdateOperatorComment(taskId, insurancecompanyid, processType, "0", "回写的" + validateResult, taskStatus);
					WorkflowFeedbackUtil.setWorkflowFeedback(taskId, feedbackSubTaskId, taskCode, "Completed", taskName, WorkflowFeedbackUtil.writeback_inconformity, "admin");
					this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "false", closeName, taskStatus, null, taskName, subTaskId);
					resultMap.put("result", "error");
					resultMap.put("msg", "回写的" + validateResult);
					return resultMap;
				}
			}
			dataInsbFlowinfo.setOverflowcode(dataInsbFlowinfo.getFlowcode());
			dataInsbFlowinfo.setOverflowname(dataInsbFlowinfo.getFlowname());
			dataInsbFlowinfo.setFiroredi("robot".equalsIgnoreCase(processType) ? "0" : "1");
			dataInsbFlowinfo.setModifytime(new Date());
			dataInsbFlowinfo.setFlowcode(flowInfoCode.get("code").toString());
			dataInsbFlowinfo.setFlowname(flowInfoCode.get("codename").toString());
			insbFlowinfoService.updateById(dataInsbFlowinfo);
			insbFlowlogsService.logs(dataInsbFlowinfo);

			if (dataPacket.get("errorInfo") != null) {
				JSONObject errorInfoJson = JSONObject.fromObject(dataPacket.get("errorInfo"));// 错误信息
				insertOrUpdateFlowError(dataInsbFlowinfo, errorInfoJson);

				if (!StringUtil.isEmpty(errorInfoJson.get("errorcode"))) {
					if (!StringUtil.isEmpty(errorInfoJson.get("errordesc"))) {
						this.insertOrUpdateOperatorComment(taskId, insurancecompanyid, processType, errorInfoJson.get("errorcode").toString(), errorInfoJson.get("errordesc").toString(), taskStatus);
					}

					if ("12".equals(errorInfoJson.get("errorcode").toString())) {
						if (taskName.contains("报价")) {
							INSBWorkflowmain workflowmain = insbWorkflowmainService.selectByInstanceId(taskId);
							// 非支付后的节点
							if (!MiscConst.noCancelNodeList.contains(workflowmain.getTaskcode())) {
								List<INSBWorkflowsub> subList = insbWorkflowsubService.selectSubModelByMainInstanceId(taskId);
								if (subList != null) {
									for (INSBWorkflowsub sub : subList) {
										WorkflowFeedbackUtil.setWorkflowFeedback(sub.getMaininstanceid(), sub.getInstanceid(), sub.getTaskcode(), "Completed", null,
												WorkflowFeedbackUtil.writeback_duplication, "admin");
										WorkflowFeedbackUtil.setWorkflowFeedback(sub.getMaininstanceid(), sub.getInstanceid(), "37", "Closed", null,
												WorkflowFeedbackUtil.writeback_duplication+"#重复投保关闭", "admin");
									}
								}

								taskthreadPool4workflow.execute(new Runnable() {
									@Override
									public void run() {
										WorkflowFeedbackUtil.setWorkflowFeedback(taskId, null, "37", "Closed", null, WorkflowFeedbackUtil.writeback_duplication+"#重复投保关闭", "admin");
										String result = insbManualPriceService.refuseUnderwrite2(taskId, "", "", "main", "back");

										LogUtil.info("进入====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",重复投保了关闭流程=报价阶段=result:" + result + "==");
										if (result == null) {
											LogUtil.info("进入====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",重复投保了关闭流程=报价阶段=result:" + result + "=已支付不能取消报价=");
										}
									}
								});
							}
						}
						if (taskName.contains("核保")) {
							INSBOrderpayment pay = new INSBOrderpayment();
							pay.setTaskid(taskId);
							pay.setSubinstanceid(subTaskId);
							List<INSBOrderpayment> paylist = insbOrderpaymentService.queryList(pay);
							boolean flag = true;
							if (paylist.size() > 0 && PayStatus.PAYING.equals(paylist.get(0).getPayresult())) {
								LogUtil.info("进入====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",重复投保了关闭流程=核保阶段==已支付不能取消报价=");
								flag = false;
							}
							if (flag) {
								final String ffeedbackSubTaskId = feedbackSubTaskId, ftaskCode = taskCode;
								taskthreadPool4workflow.execute(new Runnable() {
									@Override
									public void run() {
										WorkflowFeedbackUtil.setWorkflowFeedback(taskId, ffeedbackSubTaskId, ftaskCode, "Completed", taskName, WorkflowFeedbackUtil.writeback_duplication, "admin");
										WorkflowFeedbackUtil.setWorkflowFeedback(taskId, ffeedbackSubTaskId, "37", "Closed", null, WorkflowFeedbackUtil.writeback_duplication+"#重复投保关闭", "admin");
										String result = insbManualPriceService.refuseUnderwrite2(taskId, subTaskId, insurancecompanyid, "sub", "back");
										LogUtil.info("进入====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",重复投保了关闭流程=核保阶段=result:" + result + "==");
									}
								});
							}
						}

						resultMap.put("result", "error");
						resultMap.put("msg", "重复投保的单子，回写信息将不保存，流程停止");
						return resultMap;
					}

					if ("6".equals(errorInfoJson.get("errorcode").toString()) ) {//|| !"robot".equals(processType)
						stopLoopUnderWriting = false;
						logLoopUnderWriting = true;
					}
				}
			}

			//设置完成原因
			WorkflowFeedbackUtil.setWorkflowFeedback(taskId, feedbackSubTaskId, taskCode, "Completed", taskName, taskName, "admin");

			// 当是核保查询回写并且errorcode!=6时，删除定时任务，推工作流
			if ("38".equals(taskCode)) {
				if (stopLoopUnderWriting) {
					String logMsg = "轮询结果：%s，通知" + ("robot".equalsIgnoreCase(processType) ? "精灵核保" : "EDI核保") + "停止：taskid=" + taskId +
							",inscomcode=" + insurancecompanyid + ",taskStatus=" + taskStatus + ",subTaskId=" + subTaskId + "，通知结果：%s";

					if (FlowInfo.underwritingover.getCode().equals(taskStatus)) {
						try {
							Map<String, Object> result = insbWorkflowmaintrackService.loopUWResultHandler(subTaskId, "success", taskId, insurancecompanyid, null);
							LogUtil.info(logMsg, "success", result);
						} catch (Exception e) {
							e.printStackTrace();
							LogUtil.info(logMsg, "success", "调用错误");
						}

						this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "true", "核保查询", taskStatus, "3", taskName, subTaskId);
						appQuotationService.setThreadLocalVal("true");

					} else {
						try {
							Map<String, Object> result = insbWorkflowmaintrackService.loopUWResultHandler(subTaskId, "fail", taskId, insurancecompanyid, null);
							LogUtil.info(logMsg, "fail", result);
						} catch (Exception e) {
							e.printStackTrace();
							LogUtil.info(logMsg, "fail", "调用错误");
						}

						this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "false", "核保查询", taskStatus, "3", taskName, subTaskId);
						appQuotationService.setThreadLocalVal("true");
					}
				}
				if (logLoopUnderWriting) {
					Map<String, Object> result = insbWorkflowmaintrackService.loopUWResultHandler(subTaskId, "logDetect", taskId, insurancecompanyid, null);
				}
			}

			String strOwnerName="";//车主名称
			if (FlowInfo.quotestorgefiled.getCode().equals(taskStatus) || FlowInfo.underwritingstorgefiled.getCode().equals(taskStatus)
					|| FlowInfo.autoinsurestorgefiled.getCode().equals(taskStatus) || FlowInfo.autoinsurefiled.getCode().equals(taskStatus)) {// emu5("4","报价暂存失败")
				String underwriteway = null;
				if (FlowInfo.autoinsurefiled.getCode().equals(taskStatus) && dataPacket.get("sq") != null) {
					JSONObject sq = JSONObject.fromObject(dataPacket.get("sq"));// 出单信息
					saveSQ(taskId, insurancecompanyid, sq, taskStatus);
				}
				if(!FlowInfo.quotestorgefiled.getCode().equals(taskStatus)){ // 暂存失败、自动暂存失败、自动核保失败
					if ("robot".equals(processType)) // 如果是精灵，则下一次转人工
						underwriteway = "3";
				}
				this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "false", closeName, taskStatus, underwriteway, taskName, subTaskId);
			} else if(FlowInfo.autoinsurequery.getCode().equals(taskStatus)){ // 自核待查询，推核保查询工作流
				if (dataPacket.get("sq") != null) {
					JSONObject sq = JSONObject.fromObject(dataPacket.get("sq"));// 出单信息
					saveSQ(taskId, insurancecompanyid, sq, taskStatus);
				}
				this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "false", closeName, taskStatus, "4", taskName, subTaskId);
			} else if (FlowInfo.quotefiled.getCode().equals(taskStatus) || FlowInfo.underwritingoverfiled.getCode().equals(taskStatus)) {// emu91("A1","报价失败")
				if (dataPacket.get("sq") != null) {
					JSONObject sq = JSONObject.fromObject(dataPacket.get("sq"));// 出单信息
					saveSQ(taskId, insurancecompanyid, sq, taskStatus);
				}
				if (FlowInfo.underwritingoverfiled.getCode().equals(taskStatus)) {
					redisClient.set(Constants.CM_ZZB, taskId + "@" + insurancecompanyid + "@insure", "false", expireTime);
				}
				if (FlowInfo.quotefiled.getCode().equals(taskStatus)) {
					redisClient.set(Constants.CM_ZZB, taskId + "@" + insurancecompanyid + "@quote", "false", expireTime);
				}
				this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "false", closeName, taskStatus, null, taskName, subTaskId);
			} else if (FlowInfo.quotestorgesuc.getCode().equals(taskStatus) || FlowInfo.underwritingstorgesuc.getCode().equals(taskStatus)) {// emu4("3","报价暂存成功")
				//http://pm.baoxian.in/zentao/task-view-972.html
				if (FlowInfo.underwritingstorgesuc.getCode().equals(taskStatus) && "edi".equalsIgnoreCase(processType)) {
					if (dataPacket.get("baseSuiteInfo") != null) {
						JSONObject baseSuiteInfo = JSONObject.fromObject(dataPacket.get("baseSuiteInfo"));// 投保基本信息：（交强险信息、车船税信息、商业险信息（险种配置））
						Object specialRiskObj = dataPacket.get("specialRisk");
						JSONObject specialRisk = specialRiskObj!=null ? JSONObject.fromObject(specialRiskObj) : null;
						saveBaseSuiteInfo(taskId, insurancecompanyid, baseSuiteInfo, JSONObject.fromObject(dataPacket.get("sq")), taskStatus, specialRisk);
					}
				}
				if (dataPacket.get("sq") != null) {
					JSONObject sq = JSONObject.fromObject(dataPacket.get("sq"));// 出单信息
					saveSQ(taskId, insurancecompanyid, sq, taskStatus);
				}
				if ("01".equals(buybusitype) && FlowInfo.quotestorgesuc.getCode().equals(taskStatus)) {
					LogUtil.info((this.getFlowInfoCode(taskStatus).get("code")).toString() + "关闭工作流流程继续走后面====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",taskStatus="
							+ taskStatus + "," + ("robot".equalsIgnoreCase(processType) ? "精灵" : "EDI") + "==则关闭工作流流程继续走后面==");
					this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "false", closeName, taskStatus, null, taskName, subTaskId);
				} else if ("edi".equalsIgnoreCase(processType) && "02".equals(buybusitype)) {
					LogUtil.info((this.getFlowInfoCode(taskStatus).get("code")).toString() + "==edi网销==数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",taskStatus=" + taskStatus
							+ "," + ("robot".equalsIgnoreCase(processType) ? "精灵" : "EDI") + "====");
				} else if ("01".equals(buybusitype) && FlowInfo.underwritingstorgesuc.getCode().equals(taskStatus)) {
					String result = this.valiedatePropNo(dataPacket);
					if (StringUtil.isEmpty(result) || "robot".equalsIgnoreCase(processType)) { // EDI验证数据成功或者精灵暂存
						LogUtil.info((this.getFlowInfoCode(taskStatus).get("code")).toString() + "关闭工作流流程跳到走人工====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",taskStatus="
								+ taskStatus + "," + ("robot".equalsIgnoreCase(processType) ? "精灵" : "EDI") + "==则关闭工作流流程跳到走人工==");
						this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "false", closeName, taskStatus, "3", taskName, subTaskId);
					} else {
						LogUtil.info("回写的" + result + ",转false：taskid=" + taskId + "result" + result);
						this.insertOrUpdateOperatorComment(taskId, insurancecompanyid, processType, "0", "回写的" + result, taskStatus);
						this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "false", closeName, taskStatus, "2", taskName, subTaskId);
					}
				}
			} else if (FlowInfo.quoteover.getCode().equals(taskStatus) || FlowInfo.underwritingover.getCode().equals(taskStatus)
					|| FlowInfo.insover.getCode().equals(taskStatus) || FlowInfo.autoinsureover.getCode().equals(taskStatus)) {// emu9("A","报价完成")
				INSBQuotetotalinfo queryInsbQuotetotalinfo = new INSBQuotetotalinfo();
				queryInsbQuotetotalinfo.setTaskid(taskId);
				queryInsbQuotetotalinfo = insbQuotetotalinfoService.queryOne(queryInsbQuotetotalinfo);
				INSBQuoteinfo queryInsbQuoteinfo = new INSBQuoteinfo();
				queryInsbQuoteinfo.setQuotetotalinfoid(queryInsbQuotetotalinfo.getId());
				queryInsbQuoteinfo.setInscomcode(insurancecompanyid);
				queryInsbQuoteinfo = insbQuoteinfoService.queryOne(queryInsbQuoteinfo);
				queryInsbQuoteinfo.setTaskstatus("0".equals(dataInsbFlowinfo.getFiroredi()) ? "2" : "1");
				queryInsbQuoteinfo.setModifytime(new Date());
				insbQuoteinfoService.updateById(queryInsbQuoteinfo);
				if (dataPacket.get("carOwnerInfo") != null) {
					JSONObject carOwnerInfo = JSONObject.fromObject(dataPacket.get("carOwnerInfo"));// 车主信息
					if (!StringUtil.isEmpty(carOwnerInfo.get("name"))){
						strOwnerName=carOwnerInfo.get("name").toString();
					}
					saveCarOwnerInfo(taskId, insurancecompanyid, carOwnerInfo);
				}
				if (dataPacket.get("applicantPersonInfo") != null) {
					JSONObject applicantPersonInfo = JSONObject.fromObject(dataPacket.get("applicantPersonInfo"));// 投保人信息
					saveApplicantPersonInfo(taskId, insurancecompanyid, applicantPersonInfo);
				}
				if (dataPacket.get("invoiceinfo") != null) {
					JSONObject invoiceinfo = JSONObject.fromObject(dataPacket.get("invoiceinfo"));// 车信息
					saveInvoiceinfo(taskId, insurancecompanyid, invoiceinfo);
				}
				if (dataPacket.get("insuredPersonInfoList") != null) {
					JSONArray insuredPersonInfoList = JSONArray.fromObject(dataPacket.get("insuredPersonInfoList"));// 被保人信息
					saveInsuredPersonInfoList(taskId, insurancecompanyid, insuredPersonInfoList);
				}
				if(dataPacket.get("beneficiaryPersonList") != null){
					JSONArray beneficiaryPersonList=JSONArray.fromObject(dataPacket.get("beneficiaryPersonList"));//受益人信息
					saveBeneficiaryPersonList(taskId, insurancecompanyid, beneficiaryPersonList);
				}
				if (dataPacket.get("driverPersonList") != null) {
					JSONArray driverPersonList = JSONArray.fromObject(dataPacket.get("driverPersonList"));// 驾驶人列表
					saveDriverPersonList(taskId, insurancecompanyid, driverPersonList);
				}
				if (dataPacket.get("carInfo") != null) {
					JSONObject carInfo = JSONObject.fromObject(dataPacket.get("carInfo"));// 车信息
					saveCarInfo(taskId, insurancecompanyid, carInfo,strOwnerName);
				}
				/*if (dataPacket.get("subAgentInfo") != null) {
					JSONObject subAgentInfo = JSONObject.fromObject(dataPacket.get("subAgentInfo"));// 用户信息
					saveSubAgentInfo(taskId, insurancecompanyid, subAgentInfo);
				}
				if (dataPacket.get("agentInfo") != null) {
					JSONObject agentInfo = JSONObject.fromObject(dataPacket.get("agentInfo"));// 代理人信息
					saveAgentInfo(taskId, insurancecompanyid, agentInfo);
				}*/
				if (dataPacket.get("baseSuiteInfo") != null) {
					JSONObject baseSuiteInfo = JSONObject.fromObject(dataPacket.get("baseSuiteInfo"));// 投保基本信息：（交强险信息、车船税信息、商业险信息（险种配置））
					saveBaseSuiteInfo(taskId, insurancecompanyid, baseSuiteInfo, JSONObject.fromObject(dataPacket.get("sq")), taskStatus, null);
				}
				/*if (dataPacket.get("trackInfoList") != null) {
					JSONArray trackInfoList = JSONArray.fromObject(dataPacket.get("trackInfoList"));// 任务流程轨迹
					saveTrackInfoList(taskId, insurancecompanyid, trackInfoList);
				}*/
				/*if (dataPacket.get("deliverInfo") != null) {
					JSONObject deliverInfo = JSONObject.fromObject(dataPacket.get("deliverInfo"));// 配送信息
					saveDeliverInfo(taskId, insurancecompanyid, deliverInfo);
				}*/
				if (dataPacket.get("orderInfo") != null) {
					JSONObject orderInfo = JSONObject.fromObject(dataPacket.get("orderInfo"));// 出单信息
					saveOrderInfo(taskId, insurancecompanyid, orderInfo);
				}
				if (dataPacket.get("sq") != null) {
					JSONObject sq = JSONObject.fromObject(dataPacket.get("sq"));// 出单信息
					saveSQ(taskId, insurancecompanyid, sq, taskStatus);
				}

				if (FlowInfo.underwritingover.getCode().equals(taskStatus) || FlowInfo.autoinsureover.getCode().equals(taskStatus)) {
                    //记录(自动)核保(查询)成功
                    if (thisWorkflowsub != null) {
                        LogUtil.info("===数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "记录(自动)核保(查询)成功");
                        Map<String, String> track = new HashMap<>(2);
                        track.put("instanceid", subTaskId);
                        track.put("taskcode", taskCode);
                        INSBWorkflowsubtrack subtrack = insbWorkflowsubtrackDao.selectByInstanceIdTaskCode(track);
                        if (subtrack != null) {
                            String underwritingsuccesstype = thisWorkflowsub.getUnderwritingsuccesstype() + "," + taskCode + "#" + subtrack.getId();
                            insbWorkflowsubDao.updateUnderwritingsuccesstype(underwritingsuccesstype, thisWorkflowsub.getId());
                        } else {
                            LogUtil.info("===数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "子任务subtrack表数据为空，不记录(自动)核保(查询)成功");
                        }
                    } else {
                        LogUtil.info("===数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "子任务sub表数据为空，不记录(自动)核保(查询)成功");
                    }

					//校验回写的投保单号
					String result1 = this.valiedatePropNo(dataPacket);

					if (StringUtil.isEmpty(result1)) {
						redisClient.set(Constants.CM_ZZB, taskId + "@" + insurancecompanyid + "@insure", "true", expireTime);
						if(FlowInfo.autoinsureover.getCode().equals(taskStatus)){ // 自核成功推支付
							this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "true", closeName, taskStatus, null, taskName, subTaskId);
						} else {
							this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "false", closeName, taskStatus, null, taskName, subTaskId);
						}
					} else {
						this.insertOrUpdateOperatorComment(taskId, insurancecompanyid, processType, "0", "回写的" + result1, taskStatus);
						redisClient.set(Constants.CM_ZZB, taskId + "@" + insurancecompanyid + "@insure", "false", expireTime);
						if (insurancecompanyid.length() >= 6
								&& (insurancecompanyid.contains("200751") || insurancecompanyid.contains("200551") || insurancecompanyid.contains("204251") || insurancecompanyid.contains("204351")
								|| insurancecompanyid.contains("205851") || insurancecompanyid.contains("210151") || insurancecompanyid.contains("206851")
								|| insurancecompanyid.contains("209551") || insurancecompanyid.contains("208551"))) {
							LogUtil.info(insurancecompanyid + "=insurancecompanyid遇到保险公司特殊情况转false且skipStep=1：taskid=" + taskId);
							this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "false", closeName, taskStatus, "1", taskName, subTaskId);
						} else {
							LogUtil.info(insurancecompanyid + "=insurancecompanyid回写的" + result1 + ",转false：taskid=" + taskId);
							this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "false", closeName, taskStatus, null, taskName, subTaskId);
						}
					}

				}
				if (FlowInfo.insover.getCode().equals(taskStatus)) {
					String result = this.valiedatePolicyNo(dataPacket);
					if (StringUtil.isEmpty(result)) {
						redisClient.set(Constants.CM_ZZB, taskId + "@" + insurancecompanyid + "@approved", "true", expireTime);
						this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "true", closeName, taskStatus, null, taskName, subTaskId);
					} else {
						redisClient.set(Constants.CM_ZZB, taskId + "@" + insurancecompanyid + "@approved", "false", expireTime);
						this.insertOrUpdateOperatorComment(taskId, insurancecompanyid, processType, "0", "回写的" + result, taskStatus);
						updateOrderStatus(taskId, insurancecompanyid, "2", "5");
						LogUtil.info(insurancecompanyid + "=insurancecompanyid回写的" + result + ",转false：taskid=" + taskId);
						this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "false", closeName, taskStatus, null, taskName, subTaskId);
					}
					
					//存电子保单数据
					if(dataPacket.get("elecPolicyInfo") != null){
						JSONArray elecPolicyInfoList = JSONArray.fromObject(dataPacket.get("elecPolicyInfo"));
						saveElecPolicy(taskId, insurancecompanyid, elecPolicyInfoList);
					}
				}
				if (FlowInfo.quoteover.getCode().equals(taskStatus)) {
					redisClient.set(Constants.CM_ZZB, taskId + "@" + insurancecompanyid + "@quote", "true", expireTime);
					//报价完成再次过承保规则限制
					//调用规则判断承保限制条件
					WorkFlowRuleInfo ruleCheckmodel = new WorkFlowRuleInfo();
					ruleCheckmodel.setProcessinstanceid(taskId);
					ruleCheckmodel.setInscomcode(insurancecompanyid);
					CommonModel ruleResult = appOtherRequestService.saveWorkFlowRuleInfo(ruleCheckmodel);
					LogUtil.info("调用规则判断承保限制条件,taskid="+taskId+"inscomcode="+insurancecompanyid+"result:"+ruleResult.getBody());
					if ("success".equals(ruleResult.getStatus())) {
						if (null == ruleResult.getBody()||"false".equals(ruleResult.getBody().toString())) {
							this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "false", closeName, taskStatus, "false", taskName, subTaskId);
						}else{//规则通过推报价完成
							this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "true", closeName, taskStatus, null, taskName, subTaskId);
						}
					}else{//规则查询异常，默认通过
						this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "true", closeName, taskStatus, null, taskName, subTaskId);
					}
				}
				//报表数据埋点
				if(FlowInfo.quoteover.getCode().equals(taskStatus)){
					//报价完成取数据
					LogUtil.info("INSBQuotetotalinfo|报表数据埋点|"+JSONObject.fromObject(queryInsbQuotetotalinfo).toString());
					LogUtil.info("INSBQuoteinfo|报表数据埋点|"+JSONObject.fromObject(queryInsbQuoteinfo).toString());
				}

			} else if (FlowInfo.quotequerysuc.getCode().equals(taskStatus) || FlowInfo.payapplysuc.getCode().equals(taskStatus)) {
				if (dataPacket.get("carOwnerInfo") != null) {
					JSONObject carOwnerInfo = JSONObject.fromObject(dataPacket.get("carOwnerInfo"));// 车主信息
					if (!StringUtil.isEmpty(carOwnerInfo.get("name"))){
						strOwnerName=carOwnerInfo.get("name").toString();
					}
					saveCarOwnerInfo(taskId, insurancecompanyid, carOwnerInfo);
				}
				if (dataPacket.get("applicantPersonInfo") != null) {
					JSONObject applicantPersonInfo = JSONObject.fromObject(dataPacket.get("applicantPersonInfo"));// 投保人信息
					saveApplicantPersonInfo(taskId, insurancecompanyid, applicantPersonInfo);
				}
				if (dataPacket.get("insuredPersonInfoList") != null) {
					JSONArray insuredPersonInfoList = JSONArray.fromObject(dataPacket.get("insuredPersonInfoList"));// 被保人信息
					saveInsuredPersonInfoList(taskId, insurancecompanyid, insuredPersonInfoList);
				}
				if(dataPacket.get("beneficiaryPersonList") != null){
					JSONArray beneficiaryPersonList=JSONArray.fromObject(dataPacket.get("beneficiaryPersonList"));//受益人信息
					saveBeneficiaryPersonList(taskId, insurancecompanyid, beneficiaryPersonList);
				}
				if (dataPacket.get("driverPersonList") != null) {
					JSONArray driverPersonList = JSONArray.fromObject(dataPacket.get("driverPersonList"));// 驾驶人列表
					saveDriverPersonList(taskId, insurancecompanyid, driverPersonList);
				}
				if (dataPacket.get("carInfo") != null) {
					JSONObject carInfo = JSONObject.fromObject(dataPacket.get("carInfo"));// 车信息
					saveCarInfo(taskId, insurancecompanyid, carInfo,strOwnerName);
				}
				/*if (dataPacket.get("subAgentInfo") != null) {
					JSONObject subAgentInfo = JSONObject.fromObject(dataPacket.get("subAgentInfo"));// 用户信息
					saveSubAgentInfo(taskId, insurancecompanyid, subAgentInfo);
				}
				if (dataPacket.get("agentInfo") != null) {
					JSONObject agentInfo = JSONObject.fromObject(dataPacket.get("agentInfo"));// 代理人信息
					saveAgentInfo(taskId, insurancecompanyid, agentInfo);
				}*/
				if (dataPacket.get("baseSuiteInfo") != null) {
					JSONObject baseSuiteInfo = JSONObject.fromObject(dataPacket.get("baseSuiteInfo"));// 投保基本信息：（交强险信息、车船税信息、商业险信息（险种配置））
					saveBaseSuiteInfo(taskId, insurancecompanyid, baseSuiteInfo, JSONObject.fromObject(dataPacket.get("sq")), taskStatus, null);
				}
				/*if (dataPacket.get("trackInfoList") != null) {
					JSONArray trackInfoList = JSONArray.fromObject(dataPacket.get("trackInfoList"));// 任务流程轨迹
					saveTrackInfoList(taskId, insurancecompanyid, trackInfoList);
				}*/
				/*if (dataPacket.get("deliverInfo") != null) {
					JSONObject deliverInfo = JSONObject.fromObject(dataPacket.get("deliverInfo"));// 配送信息
					saveDeliverInfo(taskId, insurancecompanyid, deliverInfo);
				}*/
				if (dataPacket.get("orderInfo") != null) {
					JSONObject orderInfo = JSONObject.fromObject(dataPacket.get("orderInfo"));// 出单信息
					saveOrderInfo(taskId, insurancecompanyid, orderInfo);
				}
				if (dataPacket.get("sq") != null) {
					JSONObject sq = JSONObject.fromObject(dataPacket.get("sq"));// 出单信息
					saveSQ(taskId, insurancecompanyid, sq, taskStatus);
				}
			} else if (FlowInfo.insfiled.getCode().equals(taskStatus) || FlowInfo.insdatanotover.getCode().equals(taskStatus)) {
				if (dataPacket.get("sq") != null) {
					JSONObject sq = JSONObject.fromObject(dataPacket.get("sq"));// 出单信息
					saveSQ(taskId, insurancecompanyid, sq, taskStatus);
				}

				redisClient.set(Constants.CM_ZZB, taskId + "@" + insurancecompanyid + "@approved", "false", expireTime);

				LogUtil.info("承保失败，承保数据未回写转false：taskid=" + taskId);
				this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "false", closeName, taskStatus, null, taskName, subTaskId);
			} else {
				LogUtil.info("错误==taskStatus值错误==数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",taskStatus=" + taskStatus + ","
						+ ("robot".equalsIgnoreCase(processType) ? "精灵报价" : "EDI报价") + "====");
				resultMap.put("result", "error");
				resultMap.put("msg", "taskStatus值错误");
				insertOrUpdateFlowError(dataInsbFlowinfo, "回写taskStatus值错误");
				return resultMap;
			}

		} catch (Exception e) {
			LogUtil.error("出错==数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",taskStatus=" + taskStatus + ",processType=" + processType + "====" + e.getMessage());
			e.printStackTrace();
			WorkflowFeedbackUtil.setWorkflowFeedback(taskId, feedbackSubTaskId, taskCode, "Completed", taskName, WorkflowFeedbackUtil.writeback_fail, "admin");
			appQuotationService.removeThreadLocalVal();
			this.TaskResultWriteBack(taskId, insurancecompanyid, toUserId, "false", closeName, taskStatus, null, taskName, subTaskId);
			this.insertOrUpdateOperatorComment(taskId, insurancecompanyid, processType, "0", "回写数据保存失败", taskStatus);
			insertOrUpdateFlowError(dataInsbFlowinfo, "回写数据保存失败" + e.getMessage());
			resultMap.put("result", "error");
			resultMap.put("msg", "回写数据保存失败" + e.getMessage());
			return resultMap;
		} finally {
			appQuotationService.removeThreadLocalVal();
		}

		resultMap.put("result", "success");
		resultMap.put("msg", "回写成功,状态为：" + taskName);
		LogUtil.info("结束====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",返回的报文:" + resultMap + "====");

		/**
		 * 报价完成、核保完成、承保完成、自动核保完成后，保存投保历史数据
		 */
		if (FlowInfo.quoteover.getCode().equals(taskStatus) || FlowInfo.underwritingover.getCode().equals(taskStatus)
				|| FlowInfo.insover.getCode().equals(taskStatus) || FlowInfo.autoinsureover.getCode().equals(taskStatus)) {
			boolean needto = true;
			if (FlowInfo.insover.getCode().equals(taskStatus)) {
				needto = checkTaskNameIsCorrect("completeTask", taskId, null, closeName);
			} else {
				needto = checkTaskNameIsCorrect("completeSubTask", taskId, subTaskId, closeName);
			}

			if (needto) {
				Boolean flag = this.saveHistoryData(taskId, insurancecompanyid, processType, taskStatus);
				if (flag) {
					LogUtil.info("结束====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",历史保存成功====");
				} else {
					LogUtil.info("结束====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",历史保存失败====");
				}
			}
		}


		/**
		 * 精灵支付的，并且精灵承保的，并且承保失败的，并且是夜间定时任务请求的回调，保存失败信息
		 */
		String approvedQueryStatus = (String)redisClient.get(Constants.CM_GLOBAL, taskId+"_approvedQueryStatus");
		if (FlowInfo.insfiled.getCode().equals(approvedQueryStatus)) {
			JSONObject errorInfoJson = JSONObject.fromObject(dataPacket.get("errorInfo"));// 错误信息
			if (errorInfoJson != null) {
				String errorCode = errorInfoJson.getString("errorcode");
				String errorDesc = errorInfoJson.getString("errordesc");

				INSBFairyInsureErrorLog query = new INSBFairyInsureErrorLog();
				query.setTaskId(taskId);
				query.setInsuranceCompanyId(insurancecompanyid);

				INSBFairyInsureErrorLog insureErrorLog = fairyInsureErrorLogDao.selectOne(query);
				if (insureErrorLog != null) {
					LogUtil.info("平安二维码-精灵承保查询-承保失败[taskid=%s, prvid=%s, error desc= %s]", taskId, insurancecompanyid, errorDesc);
					Date now = new Date();
					insureErrorLog.setModifyTime(now);
					insureErrorLog.setErrorCode(errorCode);
					insureErrorLog.setErrorDesc(errorDesc);
					fairyInsureErrorLogDao.updateById(insureErrorLog);
				}
			}
		}
		return resultMap;
	}

	/*private void markRuleFlag(String val, String taskid, String inscomcode) {
		if (val != null) {
			appQuotationService.setThreadLocalVal(val);
			return;
		}

		if (appQuotationService.getThreadLocalVal() != null) {
			if (appQuotationService.getThreadLocalVal().contains("_")) {
				redisClient.set(8, "quoteRuleMark", taskid + ":" + inscomcode, appQuotationService.getThreadLocalVal(), 259200);
			}
			appQuotationService.removeThreadLocalVal();
		}
	}*/

	/**
	 * underwriteway 1=edi暂存、2=精灵暂存、3=人工核保、4=核保查询、5=edi自动核保、6=精灵自动核保
	 * edi/精灵处理完成调用工作流接口
	 */
	@Override
	public void TaskResultWriteBack(String taskid, String inscomcode, String userid, String result, String quotename, String taskStatus, String underwriteway, String taskName, String subTaskId) {
		if ("true".equals(appQuotationService.getThreadLocalVal())) {
			LogUtil.info("===%s, %s, %s, %s, %s, %s===前面已推工作流，本次调用不处理", taskid, inscomcode, quotename, taskName, taskStatus, result);
			return;
		}

		taskthreadPool4workflow.execute(new Runnable() {
			@Override
			public void run() {
				Map<String, Object> resultmap = new HashMap<String, Object>();
				Map<String, Object> datamap = new HashMap<String, Object>();
				if (!StringUtil.isEmpty(underwriteway)) { // 下一步，仅在核保时用
					datamap.put("underwriteway", underwriteway);
					if("false".equals(underwriteway)){
						datamap.put("rulesDecision", false);
					}
				}else{
					if("精灵核保暂存".equals(quotename)||"精灵自动核保".equals(quotename)){
						datamap.put("underwriteway", "3");
					}
					if("EDI核保暂存".equals(quotename)||"EDI自动核保".equals(quotename)){
						String result = workflowmainService.getContracthbType(taskid, inscomcode, "", "underwritingType");
						JSONObject jsonObject = JSONObject.fromObject(result);
						LogUtil.info(taskid+"=====EDI核保结束=====获取核保途径getContracthbType="+result);
						if(!StringUtil.isEmpty(jsonObject)){
							datamap.put("result", "1");
							datamap.put("underwriteway", jsonObject.optString("quotecode"));
						}else{
							datamap.put("underwriteway", "3");
						}
					}
				}
				if ("true".equalsIgnoreCase(result)) {
					if ("approved".equalsIgnoreCase(taskStatus) || taskName.contains("承保")) {
						datamap.put("result", "31");
					} else {
						datamap.put("result", "3");
						if(("EDI自动核保".equals(quotename)||"精灵自动核保".equals(quotename))){//不包含在可以自动核保的平台范围内，通过自动核保的不推支付还是推人工核保
							LogUtil.info(quotename+"quotename=====自动核保成功但是不在可以自动核保机构平台范围AUTO_INSURE_PLATFORM="+AUTO_INSURE_PLATFORM);
							INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
							quotetotalinfo.setTaskid(taskid);
							quotetotalinfo = insbQuotetotalinfoService.queryOne(quotetotalinfo);
							if(null!=quotetotalinfo&&StringUtil.isNotEmpty(quotetotalinfo.getAgentnum())){
								INSBAgent insbAgent = iNSBAgentDao.selectByJobnum(quotetotalinfo.getAgentnum());
								if(null!=insbAgent&&StringUtil.isNotEmpty(insbAgent.getDeptid())){
									INSCDept deptPlatform = inscDeptservice.getPlatformDept(insbAgent.getDeptid());
									LogUtil.info(deptPlatform.getComcode()+"=====自动核保成功但是不在可以自动核保机构平台范围AUTO_INSURE_PLATFORM="+AUTO_INSURE_PLATFORM);
									if(!(AUTO_INSURE_PLATFORM.contains(deptPlatform.getComcode()))){//平台code不包含在可以自动核保的平台范围内
										datamap.put("result", "1");
										datamap.put("underwriteway", "3");//推人工核保
										LogUtil.info(deptPlatform.getComcode()+"=====自动核保成功但是不在可以自动核保机构平台范围，默认走人工核保datamap="+datamap);
									}
								}
							}
						} else if("核保查询".equals(quotename)){
							//人工核保工作台发起的核保轮询, 直接到支付
							String curremtCode = String.valueOf(CMRedisClient.getInstance().get("TaskCurrentCode", taskid));
							if(curremtCode != null && "18".equals(curremtCode)){
								CMRedisClient.getInstance().del("TaskCurrentCode", taskid);
								LogUtil.info("taskId=" + taskid + ",核保查询成功, 由人工核保工作台发起, 直接推下个节点(支付)datamap=" + datamap);
							} else {
								LogUtil.info("taskId=" + taskid + ",quotename=" + quotename+ "===核保查询成功, 是否?在自动核保机构平台范围AUTO_INSURE_PLATFORM="+AUTO_INSURE_PLATFORM);
								INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
								quotetotalinfo.setTaskid(taskid);
								quotetotalinfo = insbQuotetotalinfoService.queryOne(quotetotalinfo);
								if (quotetotalinfo != null && StringUtil.isNotEmpty(quotetotalinfo.getAgentnum())) {
									INSBAgent insbAgent = iNSBAgentDao.selectByJobnum(quotetotalinfo.getAgentnum());
									if (insbAgent != null && StringUtil.isNotEmpty(insbAgent.getDeptid())) {
										INSCDept deptPlatform = inscDeptservice.getPlatformDept(insbAgent.getDeptid());
										if (!(AUTO_INSURE_PLATFORM.contains(deptPlatform.getComcode()))) {// 机构code不包含在可以自动核保的平台范围内
											datamap.put("result", "1");
											datamap.put("underwriteway", "3");// 推人工核保
											LogUtil.info("taskId=" + taskid + ", deptcomcode="
													+ deptPlatform.getComcode()
													+ "=====核保查询成功, 但是不在自动核保机构平台范围(配置文件), 默认走人工核保datamap=" + datamap);
										}
									}
								}
							}
						}
					}
				} else {
					datamap.put("result", "1");
					if("EDI报价".equals(quotename)){
						//markRuleFlag("1", taskid, inscomcode);
						datamap = appInsuredQuoteService.getPriceParamWay(datamap, taskid, inscomcode, "1");
						//markRuleFlag(null, taskid, inscomcode);
					}else if("精灵报价".equals(quotename)){
						//markRuleFlag("2", taskid, inscomcode);
						datamap = appInsuredQuoteService.getPriceParamWay(datamap, taskid, inscomcode, "2");
						//markRuleFlag(null, taskid, inscomcode);
					}else if("EDI承保".equals(quotename)){
						datamap.put("acceptway", JSONObject.fromObject(workflowmainService.getContractcbType(taskid, inscomcode, "1", "contract")).getString("quotecode"));
					}else if("精灵承保".equals(quotename)){
						datamap.put("acceptway", JSONObject.fromObject(workflowmainService.getContractcbType(taskid, inscomcode, "2", "contract")).getString("quotecode"));
					}
				}
				resultmap.put("taskName", quotename);
				resultmap.put("userid", "admin");
				resultmap.put("data", datamap);

				try {
					LogUtil.info("进入====任务完成通知工作流接口：taskid=" + taskid + ",inscomcode=" + inscomcode + ",taskid=" + taskid + ",userid：" + userid + ",result=" + result + ",quotename=" + quotename
							+ ",taskStatus=" + taskStatus + ",underwriteway=" + underwriteway + ",taskName=" + taskName + ",subTaskId=" + subTaskId + "====");
					if ("approved".equalsIgnoreCase(taskStatus) || taskName.contains("承保")) {
						resultmap.put("processinstanceid", Long.parseLong(taskid));
						JSONObject jsonObject = JSONObject.fromObject(resultmap);
						Map<String, String> params = new HashMap<String, String>();
						params.put("datas", jsonObject.toString());
//						Thread.sleep(10000);//为了等待工作流状态一致。

						//判断如果是北京二维码承保接口进来的话、直接推支付完成 0
						String completedStatus = "/process/completeTask";
						String approvedQueryStatus = (String)redisClient.get(Constants.CM_GLOBAL, taskid+"_approvedQueryStatus");

						// 查询承保结果成功
						if ("D".equals(approvedQueryStatus)) {
							if("false".equalsIgnoreCase(result))
								redisClient.set(Constants.CM_GLOBAL, taskid+"_approvedQueryStatus", "D1", expireTime);
							//更新订单表的东西
							INSBOrder insbOrder = new INSBOrder();
							insbOrder.setTaskid(taskid);
							insbOrder.setPrvid(inscomcode);
							insbOrder = insbOrderDao.selectOne(insbOrder);//获取当前订单
							if (insbOrder != null) {
								insbOrder.setTaskid(taskid);
								insbOrder.setOrderstatus("2"); // 待承保打单
								insbOrder.setPaymentstatus("02");
								insbOrderDao.updateById(insbOrder);//更新订单状态
								LogUtil.info(taskid+"====承保查询接口、更新order订单状态");
								//如果首单时间为空则更新代理人首单时间和taskid
								INSBAgent agent = new INSBAgent();
								agent.setAgentcode(insbOrder.getAgentcode());
								agent = insbAgentDao.selectOne(agent);
								if (null == agent.getFirstOderSuccesstime()) {
									agent.setFirstOderSuccesstime(new Date());
									agent.setTaskid(insbOrder.getTaskid());
									insbAgentDao.updateById(agent);
									LogUtil.info(taskid+"====承保查询接口、更新代理人首单时间和taskid");
								}
							}

							//更新表状态   查询最新的一条订单数据
							INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
							insbOrderpayment.setTaskid(taskid);
							insbOrderpayment.setPaychannelid("51");//“51”改成跟order表的一致--2083需求回滚
							insbOrderpayment = insbOrderpaymentDao.selectOneByCode(insbOrderpayment);
							if(insbOrderpayment!=null){
								insbOrderpayment.setId(insbOrderpayment.getId());
								insbOrderpayment.setTaskid(taskid);
								insbOrderpayment.setPayresult("02");
								insbOrderpayment.setPaytime(new Date());
								insbOrderpaymentDao.updateById(insbOrderpayment);
								LogUtil.info(taskid+"====承保查询接口、更新orderPayment订单状态");
							}

							// 更新承保日志
							INSBFairyInsureErrorLog query = new INSBFairyInsureErrorLog();
							query.setTaskId(taskid);
							query.setInsuranceCompanyId(inscomcode);

							INSBFairyInsureErrorLog insureErrorLog = fairyInsureErrorLogDao.selectOne(query);
							if (insureErrorLog != null) {
								LogUtil.info("平安二维码-精灵承保查询-承保成功[taskid=%s, prvid=%s]", taskid, inscomcode);
								Date now = new Date();
								insureErrorLog.setModifyTime(now);
								insureErrorLog.setErrorCode("D");
								insureErrorLog.setErrorDesc("承保成功");
								insureErrorLog.setOperator("fairy");
								fairyInsureErrorLogDao.updateById(insureErrorLog);
							}

							//如果承保查询成功，直接推承保任务完成
							resultmap.put("taskName", "支付");
							Map<String, Object> param = new HashMap<String, Object>();
							param.put("userid", userid);
							param.put("taskName", "支付");
							param.put("processinstanceid", subTaskId);
							Map<String, Object> data = new HashMap<String, Object>();
							data.put("issecond", "0");
							data.put("acceptway", JSONObject.fromObject(workflowmainService.getContractcbType(taskid, inscomcode, "0", "contract")).getString("quotecode"));
							param.put("data", data);
							JSONObject jsonb = JSONObject.fromObject(param);
							params.put("datas", jsonb.toString());
							completedStatus = "/process/completeSubTask";
							WorkflowFeedbackUtil.setWorkflowFeedback(taskid, subTaskId, "20", "Completed", "支付", WorkflowFeedbackUtil.payment_complete, "admin");
							LogUtil.info(taskid+"====二维码支付接口、承保完成后推支付完成===");
						}else if("D1".equals(approvedQueryStatus)){
							//更新订单表的东西
							INSBOrder insbOrder = new INSBOrder();
							insbOrder.setTaskid(taskid);
							insbOrder.setPrvid(inscomcode);
							insbOrder = insbOrderDao.selectOne(insbOrder);//获取当前订单
							if (insbOrder != null) {
								insbOrder.setTaskid(taskid);
								insbOrder.setPaymentstatus("03");
								insbOrderDao.updateById(insbOrder);//更新订单状态
								LogUtil.info(taskid+"====承保查询失败后、更新order订单状态03");
							}
						}
						if(checkTaskNameIsCorrect(completedStatus,taskid,subTaskId,String.valueOf(resultmap.get("taskName")))){
							LogUtil.info("====任务完成通知工作流接口：taskid=" + taskid + ",inscomcode=" + inscomcode + ",主流程taskid=" + taskid + ",userid：" + userid + ",result=" + result + ",quotename=" + quotename
									+ ",taskTyep=" + taskStatus + ",调用工作流地址：" + ValidateUtil.getConfigValue("workflow.url") + completedStatus + ",请求参数：" + JsonUtil.getJsonString(params) + "====");
							String result1 = HttpClientUtil.doGet(ValidateUtil.getConfigValue("workflow.url") + completedStatus, params);
							LogUtil.info("结束====任务完成通知工作流接口：taskid=" + taskid + ",inscomcode=" + inscomcode + ",主流程taskid=" + taskid + ",userid：" + userid + ",result=" + result + ",quotename="
									+ quotename + ",taskTyep=" + taskStatus + ",工作流返回信息：" + result1 + "====");
						}else{
							LogUtil.info("结束====人工回写数据环节不需要通知工作流接口：taskid=%s,inscomcode=%s,子流程subinstanceId=%s,userid：%s,result=%s,quotename=%s,taskTyep=%s",taskid, inscomcode, subTaskId, userid, result, quotename, taskStatus);
						}
					} else {
						resultmap.put("processinstanceid", Long.parseLong(subTaskId));
						JSONObject jsonObject = JSONObject.fromObject(resultmap);
						Map<String, String> params = new HashMap<String, String>();
						params.put("datas", jsonObject.toString());
						if(checkTaskNameIsCorrect("completeSubTask",taskid,subTaskId,String.valueOf(resultmap.get("taskName")))){
							LogUtil.info("====任务完成通知工作流接口：taskid=" + taskid + ",inscomcode=" + inscomcode + ",子流程taskid=" + subTaskId + ",userid：" + userid + ",result=" + result + ",quotename="
									+ quotename + ",taskTyep=" + taskStatus + ",调用工作流地址：" + ValidateUtil.getConfigValue("workflow.url") + "/process/completeSubTask" + ",请求参数："
									+ JsonUtil.getJsonString(params) + "====");
							String result1 = HttpClientUtil.doGet(ValidateUtil.getConfigValue("workflow.url") + "/process/completeSubTask", params);
							LogUtil.info("结束====任务完成通知工作流接口：taskid=" + taskid + ",inscomcode=" + inscomcode + ",子流程subinstanceId=" + subTaskId + ",userid：" + userid + ",result=" + result + ",quotename="
									+ quotename + ",taskTyep=" + taskStatus + ",工作流返回信息：" + result1 + "====");
						}else{
							LogUtil.info("结束====人工回写数据环节不需要通知工作流接口：taskid=%s,inscomcode=%s,子流程subinstanceId=%s,userid：%s,result=%s,quotename=%s,taskTyep=%s",taskid, inscomcode, subTaskId, userid, result, quotename, taskStatus);
						}
					}
				} catch (Exception e) {
					LogUtil.info("结束==错误==调用工作流链接出错==任务完成通知工作流接口：taskid=" + taskid + ",inscomcode=" + inscomcode + ",taskid=" + taskid + ",userid：" + userid + ",result=" + result + ",quotename=" + quotename
							+ ",taskStatus=" + taskStatus + ",underwriteway=" + underwriteway + ",taskName=" + taskName + ",subTaskId=" + subTaskId + "====");
					INSBFlowlogs insbFlowlogs = new INSBFlowlogs();
					insbFlowlogs.setCreatetime(new Date());
					insbFlowlogs.setCurrenthandle(userid);
					insbFlowlogs.setTaskid(taskid);
					insbFlowlogs.setNoti(result);
					insbFlowlogs.setOperator("sys");
					insbFlowlogs.setFlowcode("taskover2workflowfailed");
					insbFlowlogs.setFlowname("任务完成通知工作流失败");
					insbFlowlogs.setFromwhere("sys");
					insbFlowlogs.setCount(1);
					insbFlowlogs.setFiroredi("精灵报价".equalsIgnoreCase(quotename) ? "0" : "1");
					insbFlowlogs.setTaskstatus(taskStatus);
					insbFlowlogsService.insert(insbFlowlogs);
					e.printStackTrace();
				}
			}
		});
	}
	//taskName TO taskCode
    private static ResourceBundle resourceBundleTaskcode = ResourceBundle.getBundle("config/taskCode");
	/**
	 * 检验当前推工作流的任务号是否与cm本身任务一致
	 * @return
	 */
	private boolean checkTaskNameIsCorrect(String url, String mainInstanceId, String instanceId, String taskName){
		boolean flag = false;
		if(url.contains("completeSubTask")){
			INSBWorkflowsub sub = new INSBWorkflowsub();
			sub.setInstanceid(instanceId);
			sub = insbWorkflowsubService.queryOne(sub);
			if(null != sub && sub.getTaskcode().equals(resourceBundleTaskcode.getString(taskName))){
				flag = true;
			}else{
				LogUtil.info("sub结束====人工回写数据环节不需要通知工作流接口：taskid=%s,quotename=%s,getTaskcode=%s",sub.getMaininstanceid(), taskName, sub.getTaskcode());
			}
		}else{
			INSBWorkflowmain main = new INSBWorkflowmain();
			main.setInstanceid(mainInstanceId);
			main = insbWorkflowmainService.queryOne(main);
			if(null != main && main.getTaskcode().equals(resourceBundleTaskcode.getString(taskName))){
				flag = true;
			}else{
				LogUtil.info("main结束====人工回写数据环节不需要通知工作流接口：taskid=%s,quotename=%s,getTaskcode=%s",main.getInstanceid(), taskName, main.getTaskcode());
			}
		}
		return flag;
	}
	/**
	 * 任务状态更新（edi调用）
	 */
	@Override
	public Map<String, Object> updateTaskStatus(String bussnissId, String taskStatus, String processType) {
		INSBFlowinfo queryInsbFlowinfo = new INSBFlowinfo();
		String taskId = bussnissId.split("@")[0];
		String inscomcode = bussnissId.split("@")[1];
		LogUtil.info("进入====任务状态通知接口：taskid=" + taskId + ",inscomcode=" + inscomcode + ",processType" + processType + "====");
		queryInsbFlowinfo.setTaskid(taskId);
		queryInsbFlowinfo.setInscomcode(inscomcode);
		queryInsbFlowinfo.setFiroredi("robot".equalsIgnoreCase(processType) ? "0" : "1");
		INSBFlowinfo info = insbFlowinfoDao.selectOne(queryInsbFlowinfo);
		Map<String, Object> resultmap = new HashMap<String, Object>();
		resultmap.put("taskId", bussnissId);
		resultmap.put("taskStatus", taskStatus);
		resultmap.put("processType", processType);
		try {
			info.setOverflowcode(info.getFlowcode());
			info.setOverflowname(info.getFlowname());
			LogUtil.info("====任务状态通知接口：taskid=" + taskId + ",inscomcode=" + inscomcode + ",由‘" + info.getFlowname() + "’变为'" + FlowInfo.values()[Integer.parseInt(taskStatus)].getDesc() + "'====");
			info.setModifytime(new Date());
			info.setFlowcode(FlowInfo.values()[Integer.parseInt(taskStatus)].getCode());
			info.setFlowname(FlowInfo.values()[Integer.parseInt(taskStatus)].getDesc());
			info.setTaskstatus(taskStatus);
			insbFlowinfoDao.updateById(info);
			LogUtil.info("结束====任务状态通知接口：taskid=" + taskId + ",inscomcode=" + inscomcode + "'====");
			resultmap.put("result", "success");
			resultmap.put("msg", "");
		} catch (Exception e) {
			LogUtil.info("结束==错误==更新流程信息错误==任务状态通知接口：taskid=" + taskId + ",inscomcode=" + inscomcode + "'====");
			resultmap.put("result", "error");
			resultmap.put("msg", "更新流程信息错误");
			e.printStackTrace();
		}
		return resultmap;
	}

	/**
	 * 精灵报价任务回收接口
	 *
	 */
	public String recoverytask(String taskId, String taskType) {
		INSBFlowinfo insbFlowinfo = new INSBFlowinfo();
		insbFlowinfo.setTaskid(taskId);
		insbFlowinfo.setFiroredi("0"); // 0-精灵 1-EDI
		INSBFlowinfo flowinfo = insbFlowinfoDao.selectOne(insbFlowinfo);
		String result = "false";
		if (flowinfo != null) {
			String incoid = flowinfo.getInscomcode();
			String touserid = flowinfo.getCurrenthandle();
			Map<String, Object> messageMap = new HashMap<String, Object>();
			messageMap.put("businessId", taskId + "@" + incoid);
			messageMap.put("taskType", taskType);
			messageMap.put("enquiryId", "");
			messageMap.put("isLocked", "false");
			messageMap.put("processType", "");
			messageMap.put("taskType", taskType); // tasktype为recovery
			messageMap.put("isTry", false);
			messageMap.put("companyId", incoid);

			try {

				XMPPUtils.getInstance().sendMessage(touserid, JsonUtil.getJsonString(messageMap));
				result = "true";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	@Override
	public Map<String, Object> getMessage(String taskId) {
		Map<String, Object> messageMap = new HashMap<String, Object>();
		messageMap.put("businessId", taskId);// taskid,任务id
		messageMap.put("enquiryId", "");// 单方询价号
		messageMap.put("isLocked", "");// 是否锁定任务
		messageMap.put("processType", "");// 精灵、人工、edi//robot
		messageMap.put("taskType", "");// 任务类型,报价任务、核保任务、支付任务、承保任务
		messageMap.put("isTry", false);// 是否试用任务
		messageMap.put("companyId", "");// 保险公司id
		return messageMap;
	}

	private CarInfo getCarInfo(String taskId, String insurancecompanyid) {
		/**
		 * 车辆信息保存逻辑：用户首次录入的车辆信息保存在insbcarinfo中,
		 * 当用户选择完毕报价的保险公司后,并且修改了对应某一家保险公司的车辆信息,则修改后的数据保存在insbcarinfohis中；
		 * 获取这些数据需要根据taskid和companyid获得。
		 *
		 * @author zhangdi
		 * @@获取车辆信息的时候先去insbcarinfohis中查询,如果没有查到结果,则去insbcarinfo中查询。
		 */
		CarInfo carInfo = new CarInfo();

		INSBCarinfo dataInsbCarinfo = new INSBCarinfo();
		INSBCarinfo queryInsbCarinfo = new INSBCarinfo();
		queryInsbCarinfo.setTaskid(taskId);
		List<INSBCarinfo> resultInsbCarinfoList = insbCarinfoService.queryList(queryInsbCarinfo);
		if (resultInsbCarinfoList.size() > 0) {
			dataInsbCarinfo = resultInsbCarinfoList.get(0);
		} else {
			return carInfo;
		}
		INSBCarinfohis dataInsbCarinfoHis = new INSBCarinfohis();
		INSBCarinfohis queryInsbCarinfoHis = new INSBCarinfohis();
		queryInsbCarinfoHis.setTaskid(taskId);
		queryInsbCarinfoHis.setInscomcode(insurancecompanyid);
		List<INSBCarinfohis> resultInsbCarinfoHisList = insbCarinfohisService.queryList(queryInsbCarinfoHis);
		if (resultInsbCarinfoHisList.size() > 0) {
			dataInsbCarinfoHis = resultInsbCarinfoHisList.get(0);
		} else {
			try {
				PropertyUtils.copyProperties(dataInsbCarinfoHis, dataInsbCarinfo);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				LogUtil.error("taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",接口车辆信息获得出错1");
				e.printStackTrace();
				return carInfo;
			}
		}

		carInfo.setInsureconfigsameaslastyear(dataInsbCarinfoHis.getInsureconfigsameaslastyear());

		INSBCarmodelinfohis dataInsbCarmodelinfohis = new INSBCarmodelinfohis();
		INSBCarmodelinfohis queryInsbCarmodelinfohis = new INSBCarmodelinfohis();
		queryInsbCarmodelinfohis.setCarinfoid(dataInsbCarinfo.getId());
		queryInsbCarmodelinfohis.setInscomcode(insurancecompanyid);
		List<INSBCarmodelinfohis> resultInsbCarmodelinfohisList = insbCarmodelinfohisService.queryList(queryInsbCarmodelinfohis);
		if (resultInsbCarmodelinfohisList.size() > 0) {
			dataInsbCarmodelinfohis = resultInsbCarmodelinfohisList.get(0);
		} else {
			INSBCarmodelinfo datainsbCarmodelinfo = new INSBCarmodelinfo();
			INSBCarmodelinfo queryinsbCarmodelinfo = new INSBCarmodelinfo();
			queryinsbCarmodelinfo.setCarinfoid(dataInsbCarinfo.getId());
			List<INSBCarmodelinfo> resultInsbCarmodelinfoList = insbCarmodelinfoService.queryList(queryinsbCarmodelinfo);
			if (resultInsbCarmodelinfoList.size() > 0) {
				datainsbCarmodelinfo = resultInsbCarmodelinfoList.get(0);
				try {
					PropertyUtils.copyProperties(dataInsbCarmodelinfohis, datainsbCarmodelinfo);
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					LogUtil.error("taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",接口车型信息获得出错2");
					e.printStackTrace();
					return carInfo;
				}
			} else {
				LogUtil.error("taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",接口车型信息获得出错3");
				return carInfo;
			}
		}
		if ("新车未上牌".equals(dataInsbCarinfo.getCarlicenseno())) // 逻辑不严谨
			carInfo.setNoLicenseFlag(true);
		else
			carInfo.setNoLicenseFlag(false);
		// carInfo.setNoLicenseFlag("1".equals(dataInsbCarinfo.getIsNew()));//
		// 未上牌标记
		carInfo.setPlateNum(dataInsbCarinfoHis.getCarlicenseno());// 车牌号
		if (!StringUtil.isEmpty(dataInsbCarinfoHis.getCarproperty())) {
			Map<String, Integer> plateData = commonQuoteinfoService.getPlateType(taskId, insurancecompanyid,
					dataInsbCarinfoHis.getCarproperty(), dataInsbCarmodelinfohis.getSeat(), dataInsbCarmodelinfohis.getUnwrtweight());
			carInfo.setPlateColor(plateData.get("plateColor"));// 号牌颜色
			carInfo.setPlateType(plateData.get("plateType"));// 号牌种类;
		} else {
			LogUtil.info("==错误==根据车辆性质判断车牌号类型和颜色：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",使用类型：" + dataInsbCarinfoHis.getCarproperty() + ",座位数："
					+ dataInsbCarmodelinfohis.getSeat() + ",核定载重量：" + dataInsbCarmodelinfohis.getUnwrtweight() + "====");
			if (!StringUtil.isEmpty(dataInsbCarinfoHis.getPlatecolor())) {
				carInfo.setPlateColor(dataInsbCarinfoHis.getPlatecolor());// 号牌颜色
			}
			if (!StringUtil.isEmpty(dataInsbCarinfoHis.getPlateType())) {
				carInfo.setPlateType(dataInsbCarinfoHis.getPlateType());// 号牌种类
			}
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getGlassType())) {
			carInfo.setGlassType(dataInsbCarmodelinfohis.getGlassType() - 1);// 玻璃类型
		}
		carInfo.setVin(dataInsbCarinfoHis.getVincode());// 车架号
		carInfo.setEngineNum(dataInsbCarinfoHis.getEngineno());// 发动机号
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getJgVehicleType())) {
			carInfo.setJgVehicleType(dataInsbCarmodelinfohis.getJgVehicleType());// 交管车辆类型
		}

		if (!StringUtil.isEmpty(dataInsbCarinfoHis.getCarproperty())) {
			INSCCode queryCode = new INSCCode();
			queryCode.setCodetype("UseProps");
			queryCode.setCodevalue(dataInsbCarinfoHis.getCarproperty());
			queryCode = inscCodeService.queryOne(queryCode);
			LogUtil.info("====根据车辆性质判断交管类型：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",使用类型：" + (queryCode!=null ? queryCode.getCodename() : "") + "====");

			if (!StringUtil.isEmpty(queryCode)) {
				Integer jgvt = commonQuoteinfoService.getJgVehicleType(taskId, insurancecompanyid,
						dataInsbCarinfoHis.getCarproperty(), dataInsbCarmodelinfohis.getSeat(), dataInsbCarmodelinfohis.getUnwrtweight());
				LogUtil.info("====根据车辆性质判断交管类型：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",结果：" + jgvt + "====");
				if (jgvt != null) {
					carInfo.setJgVehicleType(jgvt);
				}
			}
		} else {
			LogUtil.info("==错误=则默认=根据车辆性质判断交管类型：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",使用类型：" + dataInsbCarinfoHis.getCarproperty() + ",座位数："
					+ dataInsbCarmodelinfohis.getSeat() + ",核定载重量：" + dataInsbCarmodelinfohis.getUnwrtweight() + "====");
			carInfo.setJgVehicleType(13);
		}

		if (!StringUtil.isEmpty(dataInsbCarinfoHis.getCarproperty())) {
			carInfo.setUseProps(Integer.parseInt(dataInsbCarinfoHis.getCarproperty()));// 使用性质
		}
		// else{
		// carInfo.setUseProps(Integer.parseInt((String)InterFaceDefaultValueUtil.getDefaultValue(taskId,
		// insurancecompanyid, "INSBCarinfo", "carproperty")));//使用性质
		// }
		if (!StringUtil.isEmpty(dataInsbCarinfoHis.getProperty())) {
			carInfo.setCarUserType(Integer.parseInt(dataInsbCarinfoHis.getProperty()));// 车辆用户类型
		}
		// else{
		// carInfo.setCarUserType(Integer.parseInt((String)InterFaceDefaultValueUtil.getDefaultValue(taskId,
		// insurancecompanyid, "INSBCarinfo", "property")));//车辆用户类型
		// }
		carInfo.setFirstRegDate(dataInsbCarinfoHis.getRegistdate());// 初登日期
		carInfo.setIneffectualDate(dataInsbCarinfoHis.getIneffectualDate());// 检验有效日期
		carInfo.setRejectDate(dataInsbCarinfoHis.getRejectDate());// 强制有效期
		carInfo.setLastCheckDate(dataInsbCarinfoHis.getLastCheckDate());// 最近定检日期
		carInfo.setIsTransfer("1".equals(dataInsbCarinfoHis.getIsTransfercar()));// 是过户车
		carInfo.setTransferDate(dataInsbCarinfoHis.getTransferdate());// 过户转移登记日期

		boolean flag = true;
		INSBLastyearinsureinfo insbLastyearinsureinfo = insbRulequerycarinfoDao.queryLastYearClainInfo(taskId);
		LogUtil.info("====根据初等日期判断是否新车：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",初等日期是：" + dataInsbCarinfo.getRegistdate() + ",投保类型是："
				+ insbLastyearinsureinfo.getFirstinsuretype());
		if (!StringUtil.isEmpty(INSBLastyearinsureinfo.convertFirstInsureTypeToCm(insbLastyearinsureinfo.getFirstinsuretype()))) {
			flag = false;
			if ("1".equals(INSBLastyearinsureinfo.convertFirstInsureTypeToCm(insbLastyearinsureinfo.getFirstinsuretype()))) {
				carInfo.setIsNew(true);// 新车标志
			} else {
				carInfo.setIsNew(false);// 新车标志
			}
		}

		if (flag) {
			if (!StringUtil.isEmpty(dataInsbCarinfo.getRegistdate())) {
				INSBPolicyitem queryInsbPolicyitem = new INSBPolicyitem();
				queryInsbPolicyitem.setTaskid(taskId);
				queryInsbPolicyitem.setInscomcode(insurancecompanyid);
				queryInsbPolicyitem.setRisktype("0");
				INSBPolicyitem dateInsbPolicyitem = new INSBPolicyitem();
				dateInsbPolicyitem = insbPolicyitemService.queryOne(queryInsbPolicyitem);
				if (!StringUtil.isEmpty(dateInsbPolicyitem) && !StringUtil.isEmpty(dateInsbPolicyitem.getStartdate())) {
					LogUtil.info("====根据初等日期判断是否新车：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",初等日期是：" + dataInsbCarinfo.getRegistdate() + "," + ",商业险起保日期是："
							+ dateInsbPolicyitem.getStartdate() + ",往后270天是：" + LocalDate.parse(DateUtil.toString(dateInsbPolicyitem.getStartdate(), DateUtil.Format_Date)).minusDays(270) + ",====");
					if (LocalDate.parse(DateUtil.toString(dataInsbCarinfo.getRegistdate(), DateUtil.Format_Date)).isAfter(LocalDate.now().minusDays(270))) {
						carInfo.setIsNew(true);// 新车标志
					}
				} else {
					LogUtil.info("====根据初等日期判断是否新车：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",初等日期是：" + dataInsbCarinfo.getRegistdate() + "," + ",商业险起保日期是：空,换为交强险起保日期进行判断；");
					INSBPolicyitem queryInsbPolicyitem1 = new INSBPolicyitem();
					queryInsbPolicyitem1.setTaskid(taskId);
					queryInsbPolicyitem1.setInscomcode(insurancecompanyid);
					queryInsbPolicyitem1.setRisktype("1");
					INSBPolicyitem dateInsbPolicyitem1 = new INSBPolicyitem();
					dateInsbPolicyitem1 = insbPolicyitemService.queryOne(queryInsbPolicyitem1);
					if (!StringUtil.isEmpty(dateInsbPolicyitem1) && !StringUtil.isEmpty(dateInsbPolicyitem1.getStartdate())) {
						LogUtil.info("====根据初等日期判断是否新车：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",初等日期是：" + dataInsbCarinfo.getRegistdate() + "," + ",交强险起保日期是："
								+ dateInsbPolicyitem1.getStartdate() + ",往后270天是：" + LocalDate.parse(DateUtil.toString(dateInsbPolicyitem1.getStartdate(), DateUtil.Format_Date)).minusDays(270)
								+ ",====");
						if (LocalDate.parse(DateUtil.toString(dataInsbCarinfo.getRegistdate(), DateUtil.Format_Date)).isAfter(LocalDate.now().minusDays(270))) {
							carInfo.setIsNew(true);// 新车标志
						}
					} else {
						LogUtil.info("====根据初等日期判断是否新车：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",初等日期是：" + dataInsbCarinfo.getRegistdate() + ","
								+ ",商业险起保日期是：空,换为交强险起保日期进行判断；交强险起保日期也为空，没办法了");
						carInfo.setIsNew(false);// 新车标志
					}
				}
			} else {
				LogUtil.info("====根据初等日期判断是否新车：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",初等日期是：" + dataInsbCarinfo.getRegistdate() + "," + ",当前时间往前推270天是："
						+ LocalDate.now().minusDays(270) + ",====");
				carInfo.setIsNew(false);// 新车标志
			}
		}
		carInfo.setFamilyName(dataInsbCarmodelinfohis.getFamilyname());//车系
		carInfo.setCarBrandName(dataInsbCarmodelinfohis.getBrandname());// 车型品牌名称
		carInfo.setCarModelName(dataInsbCarmodelinfohis.getStandardfullname());// 车型名称
		carInfo.setSyvehicletypename(dataInsbCarmodelinfohis.getSyvehicletypename());//商业险车辆名称
		carInfo.setSyvehicletypecode(dataInsbCarmodelinfohis.getSyvehicletypecode());//机动车编码
		// carInfo.setCarModelMisc(carModelMisc);//车型信息
		// carInfo.setCarModelMiscStream(carModelMisc);//车型信息
		carInfo.setRbCarModelName(dataInsbCarmodelinfohis.getRbCarModelName());// 人保车型名称
		carInfo.setJyCarModelName(dataInsbCarmodelinfohis.getJyCarModelName());// 精友车型名称
		carInfo.setBwCode(dataInsbCarmodelinfohis.getBwCode());// 内部车型code
		if (StringUtil.isEmpty(dataInsbCarmodelinfohis.getJyCode())) {
			if (StringUtil.isEmpty(dataInsbCarmodelinfohis.getRbCode())) {
				String cifCode = this.getRbCode(dataInsbCarmodelinfohis.getVehicleid(), insurancecompanyid, taskId);
				carInfo.setJyCode(cifCode);// 精友车型code
				carInfo.setRbCode(cifCode);// 人保车型code
			} else {
				carInfo.setJyCode(dataInsbCarmodelinfohis.getRbCode());// 精友车型code
				carInfo.setRbCode(dataInsbCarmodelinfohis.getRbCode());// 人保车型code
			}
		} else {
			carInfo.setJyCode(dataInsbCarmodelinfohis.getJyCode());// 精友车型code
			carInfo.setRbCode(dataInsbCarmodelinfohis.getRbCode());// 人保车型code
		}
		carInfo.setIsRbMatch("1".equals(dataInsbCarmodelinfohis.getIsRbMatch()));// 是否人保code匹配
		carInfo.setIsJyMatch("1".equals(dataInsbCarmodelinfohis.getIsJyMatch()));// 是否精友code匹配
		carInfo.setInsuranceCode(dataInsbCarmodelinfohis.getInsuranceCode());// 保险公司车型Code
		carInfo.setCarPriceType(!StringUtil.isEmpty(dataInsbCarmodelinfohis.getCarprice()) ? dataInsbCarmodelinfohis.getCarprice() : "0");// 车价选择，最高最低自定义
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getPolicycarprice()) && "2".equals(dataInsbCarmodelinfohis.getCarprice())) {
			carInfo.setDefinedCarPrice(BigDecimal.valueOf(dataInsbCarmodelinfohis.getPolicycarprice()).setScale(5, RoundingMode.HALF_UP));// 自定义车价金额
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getListedyear())) {
			carInfo.setListedYear(dataInsbCarmodelinfohis.getListedyear());
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getPrice())) {
			carInfo.setPrice(BigDecimal.valueOf(dataInsbCarmodelinfohis.getPrice()).setScale(5, RoundingMode.HALF_UP));// 不含税价
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getTaxprice())) {
			carInfo.setTaxPrice(BigDecimal.valueOf(dataInsbCarmodelinfohis.getTaxprice()).setScale(5, RoundingMode.HALF_UP));// 新车购置价
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getAnalogytaxprice())) {
			carInfo.setTaxAnalogyPrice(BigDecimal.valueOf(dataInsbCarmodelinfohis.getAnalogytaxprice()).setScale(5, RoundingMode.HALF_UP));// 含税类比价
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getAnalogyprice())) {
			carInfo.setAnalogyPrice(BigDecimal.valueOf(dataInsbCarmodelinfohis.getAnalogyprice()).setScale(5, RoundingMode.HALF_UP));// 不含税类比价
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getDisplacement()) && new BigDecimal("0").compareTo(new BigDecimal(dataInsbCarmodelinfohis.getDisplacement())) != 0) {
			carInfo.setDisplacement(BigDecimal.valueOf(dataInsbCarmodelinfohis.getDisplacement()).setScale(6));// 排气量
		} else if ("6".equals(dataInsbCarinfoHis.getCarproperty()) || "12".equals(dataInsbCarinfoHis.getCarproperty()) || "15".equals(dataInsbCarinfoHis.getCarproperty())
				|| "16".equals(dataInsbCarinfoHis.getCarproperty())) {
			carInfo.setDisplacement(BigDecimal.valueOf(Double.parseDouble("1")).setScale(6));// 排气量-非营业特种车-营业特种车-非营业货车-营业货车--齐毅需求
		} else if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getDisplacement())) {
			carInfo.setDisplacement(BigDecimal.valueOf(dataInsbCarmodelinfohis.getDisplacement()).setScale(6));// 排气量
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getUnwrtweight())) {
			carInfo.setModelLoad(BigDecimal.valueOf(dataInsbCarmodelinfohis.getUnwrtweight()).setScale(5, RoundingMode.HALF_UP));// 载重量
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getFullweight())) {
			carInfo.setFullLoad(BigDecimal.valueOf(dataInsbCarmodelinfohis.getFullweight()).setScale(5, RoundingMode.HALF_UP));// 车身自重
		}
		// carInfo.setIsChangeKind("1".equals(dataInsbCarinfoHis.get));//营业转非营业
		if (!StringUtil.isEmpty(dataInsbCarinfoHis.getMileage())) {
			if ("0".equals(dataInsbCarinfoHis.getMileage())) {
				carInfo.setAvgMileage(BigDecimal.valueOf(20000).setScale(5, RoundingMode.HALF_UP));// 平均行驶里程
			} else if ("1".equals(dataInsbCarinfoHis.getMileage())) {
				carInfo.setAvgMileage(BigDecimal.valueOf(40000).setScale(5, RoundingMode.HALF_UP));// 平均行驶里程
			} else if ("2".equals(dataInsbCarinfoHis.getMileage())) {
				carInfo.setAvgMileage(BigDecimal.valueOf(60000).setScale(5, RoundingMode.HALF_UP));// 平均行驶里程
			} else {
				carInfo.setAvgMileage(BigDecimal.valueOf(50000).setScale(5, RoundingMode.HALF_UP));// 平均行驶里程
			}
		}
		if (!StringUtil.isEmpty(dataInsbCarinfoHis.getDrivingarea())) {
			carInfo.setDriverArea(Integer.parseInt(dataInsbCarinfoHis.getDrivingarea()));// 约定行驶区域
		} else {
			carInfo.setDriverArea(Integer.parseInt(InterFaceDefaultValueUtil.getDefaultValue(taskId, insurancecompanyid, "CarInfo", "drivingarea").toString()));// 约定行驶区域
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getCarVehicleOrigin())) {
			carInfo.setCarVehicleOrigin(dataInsbCarmodelinfohis.getCarVehicleOrigin());// 产地类型
		} else {
			if (!StringUtil.isEmpty(dataInsbCarinfoHis.getVincode())) {
				carInfo.setCarVehicleOrigin("L".equalsIgnoreCase(dataInsbCarinfoHis.getVincode().substring(0, 1)) ? 0 : 1);// 产地类型
			}
		}
		carInfo.setIsLoansCar("1".equals(dataInsbCarinfoHis.getIsLoansCar()));// 是否贷款车
		if (!StringUtil.isEmpty(dataInsbCarinfoHis.getCarVehicularApplications())) {
			carInfo.setCarVehicularApplications(dataInsbCarinfoHis.getCarVehicularApplications());// 车辆用途
		}

		String registerArea = supplementCache.checkRegisterArea(taskId, dataInsbCarinfoHis.getCarlicenseno());
		if (!"3".equals(registerArea)) {
			carInfo.setIsFieldCar(!"0".equals(registerArea) && !"1".equals(registerArea));// 是否军牌或外地车
		} else  {
			INSBQuotetotalinfo info = new INSBQuotetotalinfo();
			info.setTaskid(taskId);
			info = insbQuotetotalinfoService.queryOne(info);
			//粤Z 港|澳 牌车在广东省投保算本地
			if (info != null && !"440000".equals(info.getInsprovincecode())) {
				carInfo.setIsFieldCar(true);
			} else {
				carInfo.setIsFieldCar(false);
			}
		}

		if (!StringUtil.isEmpty(dataInsbCarinfoHis.getCarBodyColor())) {
			carInfo.setCarBodyColor(dataInsbCarinfoHis.getCarBodyColor());// 车身颜色
		}
		carInfo.setSeatCnt(dataInsbCarmodelinfohis.getSeat());// 座位数
		carInfo.setRatedPassengerCapacity(dataInsbCarmodelinfohis.getRatedPassengerCapacity());// 核定载客人数
		// carInfo.setHaulage(haulage);//准牵引总质量
		String misc = dataInsbCarinfoHis.getNoti();
		JSONObject jsonObj = null;
		if(StringUtils.isNotEmpty(misc)){
			jsonObj = JSONObject.fromObject(misc);
		}else{
			jsonObj = new JSONObject();
		}
		jsonObj.put("syvehicletypecode", dataInsbCarmodelinfohis.getSyvehicletypecode());
		jsonObj.put("syvehicletypename", dataInsbCarmodelinfohis.getSyvehicletypename());
		carInfo.setMisc(dataInsbCarinfoHis.getNoti());// 杂项
		// carInfo.setMiscStream(misc);//杂项
		carInfo.setIsLoanManyYearsFlag("1".equals(dataInsbCarinfoHis.getLoanManyYearsFlag()));// 是否车贷投保多年标志
		
		if(null!=carInfo.getSeatCnt()&&carInfo.getSeatCnt()<=9&&null!=carInfo.getDisplacement()&&carInfo.getDisplacement().floatValue()<1.0
				&&("1".equals(dataInsbCarinfoHis.getCarproperty()) || "2".equals(dataInsbCarinfoHis.getCarproperty()) || "3".equals(dataInsbCarinfoHis.getCarproperty())
						|| "4".equals(dataInsbCarinfoHis.getCarproperty()) || "5".equals(dataInsbCarinfoHis.getCarproperty()))){
			carInfo.setJgVehicleType(21);//微型普通客车,微型车标记---交管车俩类型
		}
		
		return carInfo;
	}

	private BaseSuiteInfo getBaseSuiteInfo(String taskId, String insurancecompanyid) {
		BaseSuiteInfo baseSuiteInfo = new BaseSuiteInfo();

		INSBPolicyitem queryInsbPolicyitem = new INSBPolicyitem();
		queryInsbPolicyitem.setTaskid(taskId);
		queryInsbPolicyitem.setInscomcode(insurancecompanyid);
		List<INSBPolicyitem> resultInsbPolicyitemList = insbPolicyitemService.queryList(queryInsbPolicyitem);

		if (resultInsbPolicyitemList.size() <= 0) {
			return baseSuiteInfo;
		}

		INSBCarkindprice queryInsbCarkindprice = new INSBCarkindprice();
		queryInsbCarkindprice.setTaskid(taskId);
		queryInsbCarkindprice.setInscomcode(insurancecompanyid);
		List<INSBCarkindprice> resultInsbCarkindpriceList = insbCarkindpriceService.queryList(queryInsbCarkindprice);
		if (resultInsbCarkindpriceList.size() <= 0) {
			return baseSuiteInfo;
		}

		BizSuiteInfo bizSuiteInfo = new BizSuiteInfo();// 商业险信息
		EfcSuiteInfo efcSuiteInfo = new EfcSuiteInfo();// 交强险信息
		TaxSuiteInfo taxSuiteInfo = new TaxSuiteInfo();// 车船税

		for (INSBPolicyitem dataInsbPolicyitem : resultInsbPolicyitemList) {
			if ("0".equals(dataInsbPolicyitem.getRisktype())) {// 商业险
				bizSuiteInfo.setStart(StringUtil.isEmpty(dataInsbPolicyitem.getStartdate()) ? DateUtil.parse((String) InterFaceDefaultValueUtil.getDefaultValue(taskId, insurancecompanyid,
						"INSBPolicyitem", "startdate")) : DateUtil.parseDateTime((DateUtil.toString(dataInsbPolicyitem.getStartdate(), DateUtil.Format_Date) + " 00:00:00")));// 起保日期
				bizSuiteInfo.setEnd(StringUtil.isEmpty(dataInsbPolicyitem.getEnddate()) ? DateUtil.parse((String) InterFaceDefaultValueUtil.getDefaultValue(taskId, insurancecompanyid,
						"INSBPolicyitem", "enddate")) : DateUtil.parseDateTime((DateUtil.toString(dataInsbPolicyitem.getEnddate(), DateUtil.Format_Date) + " 23:59:59")));// 终止日期
				if (!StringUtil.isEmpty(dataInsbPolicyitem.getNoti())) {
					bizSuiteInfo.setOrgCharge(BigDecimal.valueOf(Double.parseDouble(dataInsbPolicyitem.getNoti())).setScale(5, RoundingMode.HALF_UP));// 原始保费
				}
				if (!StringUtil.isEmpty(dataInsbPolicyitem.getDiscountCharge())) {
					bizSuiteInfo.setDiscountCharge(BigDecimal.valueOf(dataInsbPolicyitem.getDiscountCharge()).setScale(5, RoundingMode.HALF_UP));// 折后保费
				}
				if (!StringUtil.isEmpty(dataInsbPolicyitem.getDiscountRate())) {
					bizSuiteInfo.setDiscountRate(BigDecimal.valueOf(dataInsbPolicyitem.getDiscountRate()).setScale(6));// 折扣率
				}
			} else if ("1".equals(dataInsbPolicyitem.getRisktype())) {// 交强险
				efcSuiteInfo.setStart(StringUtil.isEmpty(dataInsbPolicyitem.getStartdate()) ? DateUtil.parse((String) InterFaceDefaultValueUtil.getDefaultValue(taskId, insurancecompanyid,
						"INSBPolicyitem", "startdate")) : DateUtil.parseDateTime((DateUtil.toString(dataInsbPolicyitem.getStartdate(), DateUtil.Format_Date) + " 00:00:00")));// 起保日期
				efcSuiteInfo.setEnd(StringUtil.isEmpty(dataInsbPolicyitem.getEnddate()) ? DateUtil.parse((String) InterFaceDefaultValueUtil.getDefaultValue(taskId, insurancecompanyid,
						"INSBPolicyitem", "enddate")) : DateUtil.parseDateTime((DateUtil.toString(dataInsbPolicyitem.getEnddate(), DateUtil.Format_Date) + " 23:59:59")));// 终止日期
				if (!StringUtil.isEmpty(dataInsbPolicyitem.getAmount())) {
					efcSuiteInfo.setAmount(BigDecimal.valueOf(dataInsbPolicyitem.getAmount()).setScale(5, RoundingMode.HALF_UP));// 交强险总保额！
				}
				INSBCarkindprice query = new INSBCarkindprice();
				query.setTaskid(taskId);
				query.setInscomcode(insurancecompanyid);
				query.setInskindcode("VehicleCompulsoryIns");
				query = insbCarkindpriceService.queryOne(query);
				if (!StringUtil.isEmpty(query) && !StringUtil.isEmpty(query.getAmountprice())) {
					efcSuiteInfo.setOrgCharge(BigDecimal.valueOf(query.getAmountprice()).setScale(5, RoundingMode.HALF_UP));// 原始保费
				}
				if (!StringUtil.isEmpty(dataInsbPolicyitem.getNoti())) {
					efcSuiteInfo.setOrgCharge(BigDecimal.valueOf(Double.parseDouble(dataInsbPolicyitem.getNoti())).setScale(5, RoundingMode.HALF_UP));// 原始保费
				}
				if (!StringUtil.isEmpty(dataInsbPolicyitem.getDiscountCharge())) {
					efcSuiteInfo.setDiscountCharge(BigDecimal.valueOf(dataInsbPolicyitem.getDiscountCharge()).setScale(5, RoundingMode.HALF_UP));// 折后保费
				}
				if (!StringUtil.isEmpty(dataInsbPolicyitem.getDiscountRate())) {
					efcSuiteInfo.setDiscountRate(BigDecimal.valueOf(dataInsbPolicyitem.getDiscountRate()).setScale(6));// 折扣率
				}
			}
		}
		boolean VCI = false;
		boolean BZI = false;
		List<SuiteDef> suiteDefList = new ArrayList<SuiteDef>();
		for (INSBCarkindprice dataInsbCarkindprice : resultInsbCarkindpriceList) {
			if ("NcfDriverPassengerIns".equals(dataInsbCarkindprice.getInskindcode()) || "NcfBasicClause".equals(dataInsbCarkindprice.getInskindcode())
					|| "NcfAddtionalClause".equals(dataInsbCarkindprice.getInskindcode()) || "NcfClause".equals(dataInsbCarkindprice.getInskindcode())) {
				continue;
			}
			SuiteDef suiteDef = new SuiteDef();// 商业投保险种信息 2明明就是交强
			if ("VehicleCompulsoryIns".equals(dataInsbCarkindprice.getInskindcode()) && "2".equals(dataInsbCarkindprice.getInskindtype())) {
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getAmount())) {
					efcSuiteInfo.setAmount(BigDecimal.valueOf(dataInsbCarkindprice.getAmount()).setScale(5, RoundingMode.HALF_UP));// 保额
				}
				/*
				 * if(!StringUtil.isEmpty(dataInsbCarkindprice.getAmountprice()))
				 * {
				 * efcSuiteInfo.setOrgCharge(BigDecimal.valueOf(dataInsbCarkindprice
				 * .getAmountprice()).setScale(5, RoundingMode.HALF_UP));//原始保费
				 * }
				 */
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getDiscountCharge())) {
					efcSuiteInfo.setDiscountCharge(BigDecimal.valueOf(dataInsbCarkindprice.getDiscountCharge()).setScale(5, RoundingMode.HALF_UP));// 折后保费
				}
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getDiscountRate())) {
					efcSuiteInfo.setDiscountRate(BigDecimal.valueOf(dataInsbCarkindprice.getDiscountRate()).setScale(6));// 折扣率
				}
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getNoTaxPremium())) {
					efcSuiteInfo.setNoTaxPremium(dataInsbCarkindprice.getNoTaxPremium());
					;// 不含税保费-营改增
				}
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getTax())) {
					efcSuiteInfo.setTax(dataInsbCarkindprice.getTax());
					;// 税-营改增
				}
				baseSuiteInfo.setEfcSuiteInfo(efcSuiteInfo);
				VCI = true;
			}

			if ("VehicleTax".equals(dataInsbCarkindprice.getInskindcode()) && "3".equals(dataInsbCarkindprice.getInskindtype())) {
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getDiscountCharge())) {
					taxSuiteInfo.setCharge(BigDecimal.valueOf(dataInsbCarkindprice.getDiscountCharge()).setScale(5, RoundingMode.HALF_UP));// 保额
				}
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getNoTaxPremium())) {
					taxSuiteInfo.setNoTaxPremium(dataInsbCarkindprice.getNoTaxPremium());// 不含税保费-营改增
				}
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getTax())) {
					taxSuiteInfo.setTax(dataInsbCarkindprice.getTax());
					;// 税-营改增
				}
				baseSuiteInfo.setTaxSuiteInfo(taxSuiteInfo);
			}
			if ("VehicleTaxOverdueFine".equals(dataInsbCarkindprice.getInskindcode()) && "3".equals(dataInsbCarkindprice.getInskindtype())) {
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getDiscountCharge())) {
					taxSuiteInfo.setDelayCharge(BigDecimal.valueOf(dataInsbCarkindprice.getDiscountCharge()).setScale(5, RoundingMode.HALF_UP));// 保额
				}
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getNoTaxPremium())) {
					taxSuiteInfo.setNoTaxPremium(dataInsbCarkindprice.getNoTaxPremium());// 不含税保费-营改增
				}
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getTax())) {
					taxSuiteInfo.setTax(dataInsbCarkindprice.getTax());
					;// 税-营改增
				}
				baseSuiteInfo.setTaxSuiteInfo(taxSuiteInfo);
			}
			if (!"VehicleTaxOverdueFine".equals(dataInsbCarkindprice.getInskindcode()) && !"VehicleTax".equals(dataInsbCarkindprice.getInskindcode())
					&& !"VehicleCompulsoryIns".equals(dataInsbCarkindprice.getInskindcode())
					&& ("0".equals(dataInsbCarkindprice.getInskindtype()) || "1".equals(dataInsbCarkindprice.getInskindtype()))) {
				suiteDef.setCode(dataInsbCarkindprice.getInskindcode());// 险种代码
				suiteDef.setName(dataInsbCarkindprice.getRiskname());// *险种名称
				if ("GlassIns".equalsIgnoreCase(dataInsbCarkindprice.getInskindcode()) || "MirrorLightIns".equalsIgnoreCase(dataInsbCarkindprice.getInskindcode())) {
					if (!StringUtil.isEmpty(dataInsbCarkindprice.getAmount())) {
						suiteDef.setAmount(BigDecimal.valueOf(dataInsbCarkindprice.getAmount()).setScale(5, RoundingMode.HALF_UP).subtract(new BigDecimal(1)));// 保额
					}
				} else {
					if (!StringUtil.isEmpty(dataInsbCarkindprice.getAmount())) {
						suiteDef.setAmount(BigDecimal.valueOf(dataInsbCarkindprice.getAmount()).setScale(5, RoundingMode.HALF_UP));// 保额
					}
				}
				suiteDef.setAmountDesc(dataInsbCarkindprice.getAmountDesc());// 保额描述
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getAmountprice())) {
					suiteDef.setOrgCharge(BigDecimal.valueOf(dataInsbCarkindprice.getAmountprice()).setScale(5, RoundingMode.HALF_UP));// 原始保费
				}
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getDiscountCharge())) {
					suiteDef.setDiscountCharge(BigDecimal.valueOf(dataInsbCarkindprice.getDiscountCharge()).setScale(5, RoundingMode.HALF_UP));// 折后保费
				}
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getDiscountRate())) {
					suiteDef.setDiscountRate(BigDecimal.valueOf(dataInsbCarkindprice.getDiscountRate()).setScale(6));// 折扣率
				}
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getNoTaxPremium())) {
					suiteDef.setNoTaxPremium(dataInsbCarkindprice.getNoTaxPremium());// 不含税保费-营改增
				}
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getTax())) {
					suiteDef.setTax(dataInsbCarkindprice.getTax());// 税-营改增
				}
				suiteDefList.add(suiteDef);
				BZI = true;
			}
		}
		if (!VCI) {
			baseSuiteInfo.setEfcSuiteInfo(null);
		}
		if (!BZI) {
			baseSuiteInfo.setBizSuiteInfo(null);
		}
		if (suiteDefList.size() > 0) {
			bizSuiteInfo.setSuites(suiteDefList);
			baseSuiteInfo.setBizSuiteInfo(bizSuiteInfo);
		}
		return baseSuiteInfo;
	}

	private CarOwnerInfo getCarOwnerInfo(String taskId, String insurancecompanyid) {
		/**
		 * 根据taskid到insbcarowneinfo表中查询,得到personid,即人员信息表id,从而获得车主信息
		 *
		 * @author zhangdi
		 */
		CarOwnerInfo carOwnerInfo = new CarOwnerInfo();
		INSBPerson dataInsbPerson = new INSBPerson();
		INSBCarowneinfo queryCarowneinfo = new INSBCarowneinfo();
		queryCarowneinfo.setTaskid(taskId);
		INSBCarowneinfo dataInsbCarowneinfo = insbCarowneinfoService.queryOne(queryCarowneinfo);
		if (dataInsbCarowneinfo == null) {
			return carOwnerInfo;
		} else {
			dataInsbPerson = insbPersonService.queryById(dataInsbCarowneinfo.getPersonid());
			if (dataInsbPerson == null) {
				return carOwnerInfo;
			}
		}
		carOwnerInfo.setName(dataInsbPerson.getName());// 姓名
		carOwnerInfo.setEnName(dataInsbPerson.getEname());// 英文名
		if (!StringUtil.isEmpty(dataInsbPerson.getGender())) {
			carOwnerInfo.setSex(dataInsbPerson.getGender());// 性别
		}
//		carOwnerInfo.setAddress(StringUtil.isEmpty(dataInsbPerson.getAddress()) ? this.getAddress(taskId, insurancecompanyid, "INSBCarowneinfo", "address") : dataInsbPerson.getAddress());// 居住地址
		carOwnerInfo.setAddress(StringUtil.isEmpty(dataInsbPerson.getAddress()) ? "" : dataInsbPerson.getAddress());// 居住地址
		// carOwnerInfo.setNational(dataInsbPerson.get);//民族
		carOwnerInfo.setPostCode(dataInsbPerson.getZip());// 邮编
		carOwnerInfo.setBirthday(dataInsbPerson.getBirthday());// 出生日期
		carOwnerInfo.setIdCardType(dataInsbPerson.getIdcardtype());// 证件类型
		carOwnerInfo.setIdCard(dataInsbPerson.getIdcardno());// 证件号
		// carOwnerInfo.setPhone(dataInsbPerson.get);//电话
		carOwnerInfo.setMobile(dataInsbPerson.getCellphone());// 手机
		carOwnerInfo.setEmail(dataInsbPerson.getEmail());// 邮箱
		return carOwnerInfo;
	}

	private SubAgentInfo getSubAgentInfo(String taskId, String insurancecompanyid) {
		/**
		 * 根据taskid去订单表中 查到代理人的工号,再去insbagent中查到代理人的信息
		 *
		 * @author zhangdi
		 */
		SubAgentInfo subAgentInfo = new SubAgentInfo();

		/*
		 * INSBOrder queryInsbOrder = new INSBOrder();
		 * queryInsbOrder.setTaskid(taskId); INSBOrder dataInsbOrder =
		 * insbOrderService.queryOne(queryInsbOrder);
		 */
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskId);
		List<INSBPolicyitem> insbPolicyitemList = insbPolicyitemService.queryList(insbPolicyitem);
		List<INSBPolicyitem> resultInsbPolicyitemList1 = new ArrayList<INSBPolicyitem>();
		for (INSBPolicyitem insbPolicyitem111 : insbPolicyitemList) {
			if (insurancecompanyid.equals(insbPolicyitem111.getInscomcode())) {
				resultInsbPolicyitemList1.add(insbPolicyitem111);
			}
		}
		if (resultInsbPolicyitemList1.size() == 0)
			resultInsbPolicyitemList1 = insbPolicyitemList;
		if (resultInsbPolicyitemList1.size() == 0 || StringUtil.isEmpty(resultInsbPolicyitemList1.get(0).getAgentnum())) {
			return subAgentInfo;
		}
		INSBAgent queryInsbAgent = new INSBAgent();
		queryInsbAgent.setAgentcode(resultInsbPolicyitemList1.get(0).getAgentnum());
		INSBAgent dataInsbAgent = insbAgentService.queryOne(queryInsbAgent);
		if (dataInsbAgent == null) {
			return subAgentInfo;
		}
		subAgentInfo.setOfficial("2".equals(dataInsbAgent.getJobnumtype()));// 用户类型
		subAgentInfo.setName(dataInsbAgent.getName());// 姓名
		if (!StringUtil.isEmpty(dataInsbAgent.getSex())) {
			subAgentInfo.setSex(Integer.parseInt(dataInsbAgent.getSex()));// 性别
		}
		// subAgentInfo.setAddress(dataInsbAgent.get);//居住地址
		// subAgentInfo.setNational(dataInsbAgent.get);//民族
		// subAgentInfo.setPostCode(dataInsbAgent.getZip());//邮编
		// subAgentInfo.setBirthday(dataInsbAgent.getBirthday());//出生日期
		if (!StringUtil.isEmpty(dataInsbAgent.getIdnotype())) {
			subAgentInfo.setIdCardType(Integer.parseInt(dataInsbAgent.getIdnotype()));// 证件类型
		}
		subAgentInfo.setIdCard(dataInsbAgent.getIdno());// 证件号
		subAgentInfo.setPhone(dataInsbAgent.getPhone());// 电话
		subAgentInfo.setMobile((StringUtil.isEmpty(dataInsbAgent.getMobile()) ? dataInsbAgent.getMobile2() : dataInsbAgent.getMobile()));// 手机
		subAgentInfo.setEmail(dataInsbAgent.getEmail());// 邮箱
		subAgentInfo.setVirtualWorkerID(dataInsbAgent.getTempjobnum());// 临时工号
		return subAgentInfo;
	}

	private AgentInfo getAgentInfo(String taskId, String insurancecompanyid) {
		/**
		 * 根据taskid去订单表中 查到代理人的工号,再去insbagent中查到代理人的信息
		 *
		 * @author zhangdi
		 */
		AgentInfo agentInfo = new AgentInfo();

		INSBPolicyitem queryInsbPolicyitem = new INSBPolicyitem();
		INSBPolicyitem dataInsbPolicyitem = new INSBPolicyitem();
		queryInsbPolicyitem.setTaskid(taskId);
		List<INSBPolicyitem> resultInsbPolicyitemList = insbPolicyitemService.queryList(queryInsbPolicyitem);
		if (resultInsbPolicyitemList.size() > 0) {
			dataInsbPolicyitem = resultInsbPolicyitemList.get(0);
			for (INSBPolicyitem insbPolicyitem111 : resultInsbPolicyitemList) {
				if (insurancecompanyid.equals(insbPolicyitem111.getInscomcode())) {
					dataInsbPolicyitem = insbPolicyitem111;
					break;
				}
			}
		} else {
			return agentInfo;
		}

		INSBAgent queryInsbAgent = new INSBAgent();
		queryInsbAgent.setAgentcode(dataInsbPolicyitem.getAgentnum());
		INSBAgent dataInsbAgent = insbAgentService.queryOne(queryInsbAgent);
		if (dataInsbAgent == null) {
			return agentInfo;
		}

		agentInfo.setName(dataInsbAgent.getName());// 姓名
		if (!StringUtil.isEmpty(dataInsbAgent.getSex())) {
			agentInfo.setSex(Integer.parseInt(dataInsbAgent.getSex()));// 性别
		}
		// agentInfo.setAddress(dataInsbAgent.get);//居住地址
		// agentInfo.setNational(dataInsbAgent.get);//民族
		// agentInfo.setPostCode(dataInsbAgent.getZip());//邮编
		// agentInfo.setBirthday(dataInsbAgent.getBirthday());//出生日期
		if (!StringUtil.isEmpty(dataInsbAgent.getIdnotype())) {
			agentInfo.setIdCardType(Integer.valueOf(dataInsbAgent.getIdnotype()));// 证件类型
		}
		agentInfo.setIdCard(dataInsbAgent.getIdno());// 证件号
		agentInfo.setPhone(dataInsbAgent.getTelnum());// 电话
		agentInfo.setMobile((StringUtil.isEmpty(dataInsbAgent.getMobile()) ? dataInsbAgent.getMobile2() : dataInsbAgent.getMobile()));// 手机
		agentInfo.setEmail(dataInsbAgent.getEmail());// 邮箱
		agentInfo.setWorkerID(dataInsbAgent.getJobnum());// 工号
		/*if (StringUtil.isEmpty(dataInsbAgent.getLicenseno())) {
			agentInfo.setCertNumber("20403000000080002014020990");// 从业资格证
		} else {*/
		agentInfo.setCertNumber(dataInsbAgent.getLicenseno());// 从业资格证
		/*}*/
		// agentInfo.setOrgCode(dataInsbAgent.get);//机构编码
		// agentInfo.setOrgName(orgName);//机构名称
		agentInfo.setTeamCode(dataInsbAgent.getTeamcode());// 团队编码
		agentInfo.setTeamName(dataInsbAgent.getTeamname());// 团队名称
		agentInfo.setPlatformCode(dataInsbAgent.getPlatformcode());// 所属平台编码
		agentInfo.setPlatformName(dataInsbAgent.getPlatformname());// 所属平台名称
		return agentInfo;
	}

	private PersonInfo getApplicantPersonInfo(String taskId, String insurancecompanyid) {
		/**
		 * 根据taskid和companyid去insbapplicanthis中查,如果没有查到数据,则去insbapplicant中查
		 *
		 * @author zhangdi
		 */
		InsurePerson applicantPersonInfo = new InsurePerson();
		INSBApplicant dataInsbApplicant = new INSBApplicant();

		INSBApplicanthis queryInsbApplicanthis = new INSBApplicanthis();
		queryInsbApplicanthis.setTaskid(taskId);
		queryInsbApplicanthis.setInscomcode(insurancecompanyid);
		INSBApplicanthis dataInsbApplicanthis = new INSBApplicanthis();
		dataInsbApplicanthis = insbApplicanthisService.queryOne(queryInsbApplicanthis);
		if (dataInsbApplicanthis == null) {
			INSBApplicant queryInsbApplicant = new INSBApplicant();
			queryInsbApplicant.setTaskid(taskId);
			dataInsbApplicant = insbApplicantService.queryOne(queryInsbApplicant);
			if (dataInsbApplicant == null) {
				return applicantPersonInfo;
			} else {
				try {
					dataInsbApplicanthis = new INSBApplicanthis();
					PropertyUtils.copyProperties(dataInsbApplicanthis, dataInsbApplicant);
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					LogUtil.error("taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",接口投保人信息获得出错");
					e.printStackTrace();
					return applicantPersonInfo;
				}
			}
		}

		INSBPerson dataInsbPerson = insbPersonService.queryById(dataInsbApplicanthis.getPersonid());
		if (dataInsbPerson == null) {
			return applicantPersonInfo;
		}
		applicantPersonInfo.setRelation(dataInsbApplicanthis.getRelation());// 与车主关系
		applicantPersonInfo.setName(dataInsbPerson.getName());// 姓名
		applicantPersonInfo.setEnName(dataInsbPerson.getEname());// 英文名
		if (!StringUtil.isEmpty(dataInsbPerson.getGender())) {
			applicantPersonInfo.setSex(dataInsbPerson.getGender());// 性别
		}
//		applicantPersonInfo.setAddress(StringUtil.isEmpty(dataInsbPerson.getAddress()) ? this.getAddress(taskId, insurancecompanyid, "INSBApplicant", "address") : dataInsbPerson.getAddress());// 居住地址
		applicantPersonInfo.setAddress(StringUtil.isEmpty(dataInsbPerson.getAddress()) ? "" : dataInsbPerson.getAddress());// 居住地址
		// applicantPersonInfo.setNational(dataInsbPerson.get);//民族
		applicantPersonInfo.setPostCode(dataInsbPerson.getZip());// 邮编
		applicantPersonInfo.setBirthday(dataInsbPerson.getBirthday());// 出生日期
		applicantPersonInfo.setIdCardType(dataInsbPerson.getIdcardtype());// 证件类型
		applicantPersonInfo.setIdCard(dataInsbPerson.getIdcardno());// 证件号
		// applicantPersonInfo.setPhone(dataInsbPerson.get);//电话
		applicantPersonInfo.setMobile(dataInsbPerson.getCellphone());// 手机
		applicantPersonInfo.setEmail(dataInsbPerson.getEmail());// 邮箱
		return applicantPersonInfo;
	}

	private List<InsurePerson> getInsuredPersonInfo(String taskId, String insurancecompanyid) {
		/**
		 * 根据taskid和companyid去insbinsuredhis中查,如果没有查到数据,则去insbinsured中查
		 *
		 * @author zhangdi
		 */

		List<InsurePerson> insuredPersonInfoList = new ArrayList<InsurePerson>();
		InsurePerson insuredPersonInfo = new InsurePerson();

		INSBInsuredhis queryInsbInsuredhis = new INSBInsuredhis();
		INSBInsuredhis dataInsbInsuredhis = new INSBInsuredhis();
		INSBInsured dataInsbInsured = new INSBInsured();
		queryInsbInsuredhis.setTaskid(taskId);
		queryInsbInsuredhis.setInscomcode(insurancecompanyid);
		dataInsbInsuredhis = insbInsuredhisService.queryOne(queryInsbInsuredhis);
		if (dataInsbInsuredhis == null) {
			INSBInsured queryInsbInsured = new INSBInsured();
			queryInsbInsured.setTaskid(taskId);
			dataInsbInsured = insbInsuredService.queryOne(queryInsbInsured);
			if (dataInsbInsured == null) {
				return insuredPersonInfoList;
			} else {
				try {
					dataInsbInsuredhis = new INSBInsuredhis();
					PropertyUtils.copyProperties(dataInsbInsuredhis, dataInsbInsured);
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					LogUtil.error("taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",接口被保人信息获得出错");
					e.printStackTrace();
					return insuredPersonInfoList;
				}
			}
		}

		INSBPerson dataInsbPerson = insbPersonService.queryById(dataInsbInsuredhis.getPersonid());
		if (dataInsbPerson == null) {
			return insuredPersonInfoList;
		}

		insuredPersonInfo.setRelation(dataInsbInsuredhis.getRelation());// 与车主关系
		insuredPersonInfo.setName(dataInsbPerson.getName());// 姓名
		insuredPersonInfo.setEnName(dataInsbPerson.getEname());// 英文名
		insuredPersonInfo.setSex(dataInsbPerson.getGender());// 性别
//		insuredPersonInfo.setAddress(StringUtil.isEmpty(dataInsbPerson.getAddress()) ? this.getAddress(taskId, insurancecompanyid, "INSBInsured", "address") : dataInsbPerson.getAddress());// 居住地址
		insuredPersonInfo.setAddress(StringUtil.isEmpty(dataInsbPerson.getAddress()) ? "" : dataInsbPerson.getAddress());// 居住地址
		// insuredPersonInfo.setNational(dataInsbPerson.get);//民族
		insuredPersonInfo.setPostCode(dataInsbPerson.getZip());// 邮编
		insuredPersonInfo.setBirthday(dataInsbPerson.getBirthday());// 出生日期
		insuredPersonInfo.setIdCardType(dataInsbPerson.getIdcardtype());// 证件类型
		insuredPersonInfo.setIdCard(dataInsbPerson.getIdcardno());// 证件号
		// insuredPersonInfo.setPhone(dataInsbPerson.get);//电话
		insuredPersonInfo.setMobile(dataInsbPerson.getCellphone());// 手机
		insuredPersonInfo.setEmail(dataInsbPerson.getEmail());// 邮箱
		insuredPersonInfoList.add(insuredPersonInfo);
		return insuredPersonInfoList;
	}

	private List<BeneficiaryPerson> getBeneficiaryPersonList(String taskId, String insurancecompanyid) {
		/**
		 * 根据taskid和companyid去insblegalrightclaimhis中查,如果没有查到数据,
		 * 则去insblegalrightclaim中查
		 *
		 * @author zhangdi
		 */
		List<BeneficiaryPerson> beneficiaryPersonsList = new ArrayList<BeneficiaryPerson>();
		List<INSBLegalrightclaim> resultInsbLegalrightclaimList = new ArrayList<INSBLegalrightclaim>();

		INSBLegalrightclaimhis queryInsbLegalrightclaimhis = new INSBLegalrightclaimhis();
		queryInsbLegalrightclaimhis.setTaskid(taskId);
		queryInsbLegalrightclaimhis.setInscomcode(insurancecompanyid);
		List<INSBLegalrightclaimhis> resultInsbLegalrightclaimhisList = insbLegalrightclaimhisService.queryList(queryInsbLegalrightclaimhis);
		if (resultInsbLegalrightclaimhisList.size() > 0) {
		} else {
			INSBLegalrightclaim queryInsbLegalrightclaim = new INSBLegalrightclaim();
			queryInsbLegalrightclaim.setTaskid(taskId);
			resultInsbLegalrightclaimList = insbLegalrightclaimService.queryList(queryInsbLegalrightclaim);
			if (resultInsbLegalrightclaimList.size() > 0) {
				for (INSBLegalrightclaim dataInsbLegalrightclaim : resultInsbLegalrightclaimList) {
					try {
						INSBLegalrightclaimhis dataInsbLegalrightclaimhis = new INSBLegalrightclaimhis();
						PropertyUtils.copyProperties(dataInsbLegalrightclaimhis, dataInsbLegalrightclaim);
						resultInsbLegalrightclaimhisList.add(dataInsbLegalrightclaimhis);
					} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
						LogUtil.error("taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",接口受益人信息获得出错");
						e.printStackTrace();
						return beneficiaryPersonsList;
					}
				}
			} else {
				return beneficiaryPersonsList;
			}
		}
		for (INSBLegalrightclaimhis dataInsbLegalrightclaimhis : resultInsbLegalrightclaimhisList) {
			INSBPerson dataInsbPerson = new INSBPerson();
			dataInsbPerson = insbPersonService.queryById(dataInsbLegalrightclaimhis.getPersonid());
			if (dataInsbPerson == null) {
				return beneficiaryPersonsList;
			} else {
				BeneficiaryPerson beneficiaryPerson = new BeneficiaryPerson();
				// beneficiaryPerson.setRelation(dataInsbLegalrightclaimhis.get);//与车主关系
				beneficiaryPerson.setLegal("1".equals(dataInsbLegalrightclaimhis.getIsLegal()));// 是否法定
				if (!StringUtil.isEmpty(dataInsbLegalrightclaimhis.getOrderNum())) {
					beneficiaryPerson.setOrderNum(dataInsbLegalrightclaimhis.getOrderNum());// 受益顺序
				}
				if (!StringUtil.isEmpty(dataInsbLegalrightclaimhis.getRatio())) {
					beneficiaryPerson.setRatio(BigDecimal.valueOf(dataInsbLegalrightclaimhis.getRatio()).setScale(5, RoundingMode.HALF_UP));// 收益比例
				}
				beneficiaryPerson.setName(dataInsbPerson.getName());// 姓名
				beneficiaryPerson.setEnName(dataInsbPerson.getEname());// 英文名
				if (!StringUtil.isEmpty(dataInsbPerson.getGender())) {
					beneficiaryPerson.setSex(dataInsbPerson.getGender());// 性别
				}
				beneficiaryPerson.setAddress(dataInsbPerson.getAddress());// 居住地址
				// beneficiaryPerson.setNational(dataInsbPerson.get);//民族
				beneficiaryPerson.setPostCode(dataInsbPerson.getZip());// 邮编
				beneficiaryPerson.setBirthday(dataInsbPerson.getBirthday());// 出生日期
				beneficiaryPerson.setIdCardType(dataInsbPerson.getIdcardtype());// 证件类型
				beneficiaryPerson.setIdCard(dataInsbPerson.getIdcardno());
				// beneficiaryPerson.setPhone(dataInsbPerson.get);//电话
				beneficiaryPerson.setMobile(dataInsbPerson.getCellphone());// 手机
				beneficiaryPerson.setEmail(dataInsbPerson.getEmail());// 邮箱
				beneficiaryPersonsList.add(beneficiaryPerson);
			}
		}
		return beneficiaryPersonsList;
	}

	private List<DriverPerson> getDriverPersonlist(String taskId, String insurancecompanyid) {
		/**
		 * 根据taskid和companyid去insbspecifydriverhis中查,如果没有查到数据,
		 * 则去insbspecifydriver中查
		 *
		 * @author zhangdi
		 */
		List<DriverPerson> driverPersonlist = new ArrayList<DriverPerson>();

		INSBSpecifydriverhis queryInsbSpecifydriverhis = new INSBSpecifydriverhis();
		queryInsbSpecifydriverhis.setTaskid(taskId);
		queryInsbSpecifydriverhis.setInscomcode(insurancecompanyid);
		List<INSBSpecifydriverhis> resultInsbSpecifydriverhisList = insbspecifydriverhisService.queryList(queryInsbSpecifydriverhis);
		if (resultInsbSpecifydriverhisList.size() > 0) {
		} else {
			INSBSpecifydriver queryInsbSpecifydriver = new INSBSpecifydriver();
			queryInsbSpecifydriver.setTaskid(taskId);
			List<INSBSpecifydriver> resultInsbSpecifydriverList = insbspecifydriverService.queryList(queryInsbSpecifydriver);
			if (resultInsbSpecifydriverList.size() > 0) {
				for (INSBSpecifydriver dataInsbSpecifydriver : resultInsbSpecifydriverList) {
					try {
						INSBSpecifydriverhis dataInsbSpecifydriverhis = new INSBSpecifydriverhis();
						PropertyUtils.copyProperties(dataInsbSpecifydriverhis, dataInsbSpecifydriver);
						resultInsbSpecifydriverhisList.add(dataInsbSpecifydriverhis);
					} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
						LogUtil.error("taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",接口驾驶人信息获得出错");
						e.printStackTrace();
						return driverPersonlist;
					}
				}
			} else {
				return driverPersonlist;
			}
		}
		for (INSBSpecifydriverhis dataInsbSpecifydriverhis : resultInsbSpecifydriverhisList) {
			INSBPerson dataInsbPerson = new INSBPerson();
			dataInsbPerson = insbPersonService.queryById(dataInsbSpecifydriverhis.getPersonid());
			if (dataInsbPerson != null) {
				DriverPerson driverPerson = new DriverPerson();
				// driverPerson.setRelation(insbLegalrightclaim);//与车主关系
				driverPerson.setName(dataInsbPerson.getName());// 姓名
				driverPerson.setEnName(dataInsbPerson.getEname());// 英文名
				driverPerson.setSex(dataInsbPerson.getGender());// 性别
				driverPerson.setAddress(dataInsbPerson.getAddress());// 居住地址
				// driverPerson.setNational(dataInsbPerson.get);//民族
				driverPerson.setPostCode(dataInsbPerson.getZip());// 邮编
				driverPerson.setBirthday(dataInsbPerson.getBirthday());// 出生日期
				driverPerson.setIdCardType(Integer.valueOf(dataInsbPerson.getIdcardtype()));
				driverPerson.setIdCard(dataInsbPerson.getIdcardno());
				// driverPerson.setPhone(dataInsbPerson.get);//电话
				driverPerson.setMobile(dataInsbPerson.getCellphone());// 手机
				driverPerson.setEmail(dataInsbPerson.getEmail());// 邮箱
				// driverPerson.setLicenseType(LicenseType.values()[dataInsbSpecifydriverhis.getLicensetype()]);//驾驶证类型
				driverPerson.setLicenseDesc(dataInsbSpecifydriverhis.getLicenseDesc());// 驾照描述
				driverPerson.setLicenseNo(dataInsbPerson.getLicenseno());// 驾驶证号
				driverPerson.setLicenseState(dataInsbSpecifydriverhis.getLicenseState());// 驾驶证状态代码
				driverPerson.setFirstRegDate(dataInsbPerson.getLicensedate());// 首次发证日期
				// driverPerson.setPrimary(isPrimary);//是否是主驾驶员
				// driverPerson.setHabits(habits);//驾驶习惯
				driverPersonlist.add(driverPerson);
			}
		}
		return driverPersonlist;
	}

	private List<ProviderInfo> getProviderInfoList(String taskId, String insurancecompanyid) {
		/**
		 * 根据companyid查询保险公司（供应商）的信息insbprovider
		 *
		 * @author zhangdi
		 */

		List<ProviderInfo> providerInfoList = new ArrayList<ProviderInfo>();
		INSBProvider queryInsbProvider = new INSBProvider();
		queryInsbProvider.setPrvcode(insurancecompanyid);
		INSBProvider dataInsbProvider = insbProviderService.queryOne(queryInsbProvider);
		if (dataInsbProvider == null) {
			return providerInfoList;
		}
		ProviderInfo providerInfo = new ProviderInfo();
		providerInfo.setId(dataInsbProvider.getPrvcode());// 供应商id
		providerInfo.setComId(insurancecompanyid);// 保险公司ID
		providerInfo.setFullName(dataInsbProvider.getPrvname());// 保险公司名称
		providerInfo.setNickName(dataInsbProvider.getPrvshotname());// 简称
		providerInfoList.add(providerInfo);
		return providerInfoList;
	}

	private List<TrackInfo> getTrackInfoList(String taskId) {
		List<TrackInfo> trackInfoList = new ArrayList<TrackInfo>();
		TrackInfo trackInfo = new TrackInfo();
		trackInfoList.add(trackInfo);
		return trackInfoList;
	}

	private DeliverInfo getDeliverInfo(String taskId, String insurancecompanyid, String agentNum) {
		/**
		 * 根据taskid去insborderdelivery中查询
		 *
		 * @author zhangdi
		 */
		DeliverInfo deliverInfo = new DeliverInfo();
		INSBOrderdelivery queryInsbOrderdelivery = new INSBOrderdelivery();
		queryInsbOrderdelivery.setTaskid(taskId);
		queryInsbOrderdelivery.setProviderid(insurancecompanyid);
		List<INSBOrderdelivery> INSBOrderdeliveryList = insbOrderdeliveryService.queryList(queryInsbOrderdelivery);
		if (INSBOrderdeliveryList.size() <= 0) {
			if (StringUtil.isEmpty(agentNum)) {
				return deliverInfo;
			}
			INSBAgent insbAgent = new INSBAgent();
			insbAgent.setJobnum(agentNum);
			insbAgent = insbAgentService.queryOne(insbAgent);
			deliverInfo.setName(insbAgent.getName());// 姓名
			// deliverInfo.setDelive电话号码);
			deliverInfo.setPhone(insbAgent.getPhone());
			;// 手机号码
			INSCDept inscDept = inscDeptService.queryById(insbAgent.getDeptid());
			INSBRegion insbRegion1 = null;
			INSBRegion insbRegion2 = null;
			INSBRegion insbRegion3 = null;
			if (!StringUtil.isEmpty(inscDept.getProvince())) {
				insbRegion1 = insbRegionService.queryById(inscDept.getProvince());
			}
			if (!StringUtil.isEmpty(inscDept.getCity())) {
				insbRegion2 = insbRegionService.queryById(inscDept.getCity());
			}
			if (!StringUtil.isEmpty(inscDept.getCounty())) {
				insbRegion3 = insbRegionService.queryById(inscDept.getCounty());
			}
			deliverInfo.setProvince(inscDept.getProvince());// 省 代理机构地址
			deliverInfo.setCity(inscDept.getCity());// 市
			deliverInfo.setArea(inscDept.getCounty());// 区县
			deliverInfo.setAddress((StringUtil.isEmpty(insbRegion1) ? "" : insbRegion1.getComcodename()) + (StringUtil.isEmpty(insbRegion2) ? "" : insbRegion2.getComcodename())
					+ (StringUtil.isEmpty(insbRegion3) ? "" : insbRegion3.getComcodename()) + inscDept.getComname());// 投送地址
			// 代理人所属机构省市县加网点
			deliverInfo.setPostCode("10100");// 邮编 随便
			if (!StringUtil.isEmpty(2)) {
				deliverInfo.setReceiveDayType(Integer.parseInt("2"));// 接收日期
				// 工作日/休息日/全部
				// 全部
				// //有问题
			}
			if (!StringUtil.isEmpty(2)) {
				deliverInfo.setReceiveTimeType(Integer.parseInt("3"));// 接收时间
				// 早/中/晚/全天
				// 全天
				// //有问题
			}
			deliverInfo.setInvoice(false);// 是否需要发票
			deliverInfo.setInvoiceTitle("");// 发票抬头
			deliverInfo.setRemark("");// 备注
			if (!StringUtil.isEmpty(1)) {
				deliverInfo.setDeliveType(Integer.parseInt("0"));// 配送方式编码
				// 自取/快递/电子
				// 快递
			}
			deliverInfo.setInsureCoDelive(true);// 是否保险公司配送 是
			deliverInfo.setFreightCollect(false);// 是否货到付款 否
			if (!StringUtil.isEmpty(null)) {
				deliverInfo.setFee(null);// 配送费用 空
			}
			deliverInfo.setTraceNumber(null);// 快递单号 空
			return deliverInfo;
		} else {
			INSBOrderdelivery dataInsbOrderdelivery = INSBOrderdeliveryList.get(0);
			deliverInfo.setName(dataInsbOrderdelivery.getRecipientname());// 姓名
			// deliverInfo.setDelive电话号码);
			deliverInfo.setPhone(dataInsbOrderdelivery.getRecipientmobilephone());
			;// 手机号码
			deliverInfo.setProvince(dataInsbOrderdelivery.getRecipientprovince());// 省
			// 代理机构地址
			deliverInfo.setCity(dataInsbOrderdelivery.getRecipientcity());// 市
			deliverInfo.setArea(dataInsbOrderdelivery.getRecipientarea());// 区县
			deliverInfo.setAddress(dataInsbOrderdelivery.getRecipientaddress());// 投送地址
			// 代理人所属机构省市县加网点
			deliverInfo.setPostCode(dataInsbOrderdelivery.getZip());// 邮编 随便
			if (!StringUtil.isEmpty(dataInsbOrderdelivery.getReceiveday())) {
				deliverInfo.setReceiveDayType(Integer.parseInt(dataInsbOrderdelivery.getReceiveday()));// 接收日期
				// 工作日/休息日/全部
				// 全部
			}
			if (!StringUtil.isEmpty(dataInsbOrderdelivery.getReceivetime())) {
				deliverInfo.setReceiveTimeType(Integer.parseInt(dataInsbOrderdelivery.getReceivetime()));// 接收时间
				// 早/中/晚/全天
				// 全天
			}
			deliverInfo.setInvoice("1".equals(dataInsbOrderdelivery.getIsinvoice()));// 是否需要发票
			deliverInfo.setInvoiceTitle(dataInsbOrderdelivery.getInvoicetitle());// 发票抬头
			deliverInfo.setRemark(dataInsbOrderdelivery.getNoti());// 备注
			if (!StringUtil.isEmpty(dataInsbOrderdelivery.getDelivetype())) {
				deliverInfo.setDeliveType(Integer.parseInt(dataInsbOrderdelivery.getDelivetype()));// 配送方式编码
				// 自取/快递/电子
				// 快递
			}
			deliverInfo.setInsureCoDelive("1".equals(dataInsbOrderdelivery.getIsinsurecodelive()));// 是否保险公司配送
			// 是
			deliverInfo.setFreightCollect("1".equals(dataInsbOrderdelivery.getIsfreightcollect()));// 是否货到付款
			// 否
			if (!StringUtil.isEmpty(dataInsbOrderdelivery.getFee())) {
				deliverInfo.setFee(BigDecimal.valueOf(dataInsbOrderdelivery.getFee()).setScale(5, RoundingMode.HALF_UP));// 配送费用
				// 空
			}
			deliverInfo.setTraceNumber(dataInsbOrderdelivery.getTracenumber());// 快递单号
			// 空
			return deliverInfo;
		}
	}

	@Resource
	INSBInvoiceinfoService insbInvoiceinfoService;

	private Invoiceinfo getInvoiceinfo(String taskId, String insurancecompanyid) {
		INSBInvoiceinfo queryInsbInvoiceinfo = new INSBInvoiceinfo();
		queryInsbInvoiceinfo.setTaskid(taskId);
		queryInsbInvoiceinfo.setInscomcode(insurancecompanyid);
		queryInsbInvoiceinfo = insbInvoiceinfoService.queryOne(queryInsbInvoiceinfo);
		if (!StringUtil.isEmpty(queryInsbInvoiceinfo)) {
			Invoiceinfo resultInvoiceinfo = new Invoiceinfo();
			if (!StringUtil.isEmpty(queryInsbInvoiceinfo.getInvoicetype())) {// 发票信息
				// 0普通发票
				// 1增值税发票
				resultInvoiceinfo.setInvoicetype(queryInsbInvoiceinfo.getInvoicetype());
			}
			if (!StringUtil.isEmpty(queryInsbInvoiceinfo.getBankname())) {
				resultInvoiceinfo.setBankname(queryInsbInvoiceinfo.getBankname());
			}
			if (!StringUtil.isEmpty(queryInsbInvoiceinfo.getAccountnumber())) {
				resultInvoiceinfo.setAccountnumber(queryInsbInvoiceinfo.getAccountnumber());
			}
			if (!StringUtil.isEmpty(queryInsbInvoiceinfo.getIdentifynumber())) {
				resultInvoiceinfo.setIdentifynumber(queryInsbInvoiceinfo.getIdentifynumber());
			}
			if (!StringUtil.isEmpty(queryInsbInvoiceinfo.getRegisterphone())) {
				resultInvoiceinfo.setRegisterphone(queryInsbInvoiceinfo.getRegisterphone());
			}
			if (!StringUtil.isEmpty(queryInsbInvoiceinfo.getRegisteraddress())) {
				resultInvoiceinfo.setRegisteraddress(queryInsbInvoiceinfo.getRegisteraddress());
			}
			if (!StringUtil.isEmpty(queryInsbInvoiceinfo.getEmail())) {
				resultInvoiceinfo.setEmail(queryInsbInvoiceinfo.getEmail());
			}
			return resultInvoiceinfo;
		}
		return null;
	}

	private void saveInvoiceinfo(String taskId, String insurancecompanyid, JSONObject invoiceInfo) {
		INSBInvoiceinfo queryInsbInvoiceinfo = new INSBInvoiceinfo();
		queryInsbInvoiceinfo.setTaskid(taskId);
		queryInsbInvoiceinfo.setInscomcode(insurancecompanyid);
		queryInsbInvoiceinfo = insbInvoiceinfoService.queryOne(queryInsbInvoiceinfo);
		if (!StringUtil.isEmpty(invoiceInfo.get("invoicetype"))) {
			queryInsbInvoiceinfo.setInvoicetype(Integer.parseInt(invoiceInfo.get("invoicetype").toString()));
		}
		if (!StringUtil.isEmpty(invoiceInfo.get("bankname"))) {
			queryInsbInvoiceinfo.setBankname(invoiceInfo.get("bankname").toString());
		}
		if (!StringUtil.isEmpty(invoiceInfo.get("accountnumber"))) {
			queryInsbInvoiceinfo.setAccountnumber(invoiceInfo.get("accountnumber").toString());
		}
		if (!StringUtil.isEmpty(invoiceInfo.get("identifynumber"))) {
			queryInsbInvoiceinfo.setIdentifynumber(invoiceInfo.get("identifynumber").toString());
		}
		if (!StringUtil.isEmpty(invoiceInfo.get("registerphone"))) {
			queryInsbInvoiceinfo.setRegisterphone(invoiceInfo.get("registerphone").toString());
		}
		if (!StringUtil.isEmpty(invoiceInfo.get("registeraddress"))) {
			queryInsbInvoiceinfo.setRegisteraddress(invoiceInfo.get("registeraddress").toString());
		}
		if (!StringUtil.isEmpty(invoiceInfo.get("email"))) {
			queryInsbInvoiceinfo.setEmail(invoiceInfo.get("email").toString());
		}
		insbInvoiceinfoService.updateById(queryInsbInvoiceinfo);
	}

	private SQ getSQ(String taskId, String insurancecompanyid) {
		SQ sq = new SQ();
		INSBPolicyitem queryInsbPolicyitem = new INSBPolicyitem();
		queryInsbPolicyitem.setTaskid(taskId);
		List<INSBPolicyitem> resultInsbPolicyitemList = insbPolicyitemService.queryList(queryInsbPolicyitem);
		List<INSBPolicyitem> resultInsbPolicyitemList1 = new ArrayList<INSBPolicyitem>();
		for (INSBPolicyitem insbPolicyitem111 : resultInsbPolicyitemList) {
			if (insurancecompanyid.equals(insbPolicyitem111.getInscomcode())) {
				resultInsbPolicyitemList1.add(insbPolicyitem111);
			}
		}
		if (resultInsbPolicyitemList1.size() == 0)
			resultInsbPolicyitemList1 = resultInsbPolicyitemList;
		if (resultInsbPolicyitemList1.size() <= 0) {
			return sq;
		}

		for (INSBPolicyitem dataInsbPolicyitem : resultInsbPolicyitemList1) {
			if ("0".equals(dataInsbPolicyitem.getRisktype())) {
				sq.setBizTempId(dataInsbPolicyitem.getCheckcode());// 暂存单号
				sq.setBizProposeNum(dataInsbPolicyitem.getProposalformno());// 商业投保单号
				sq.setBizPolicyCode(dataInsbPolicyitem.getPolicyno());// 商业保单号
				if (!StringUtil.isEmpty(dataInsbPolicyitem.getDiscountCharge())) {
					sq.setBizCharge(BigDecimal.valueOf(dataInsbPolicyitem.getDiscountCharge()).setScale(5, RoundingMode.HALF_UP));// 商业险保费
				}
				if (!StringUtil.isEmpty(dataInsbPolicyitem.getDiscountRate())) {
					sq.setBussDiscountRate(BigDecimal.valueOf(dataInsbPolicyitem.getDiscountRate()).setScale(6));// 商业险折扣
				}
			} else {
				sq.setEfcTempId(dataInsbPolicyitem.getCheckcode());// 暂存单号
				sq.setEfcProposeNum(dataInsbPolicyitem.getProposalformno());// 交强投保单号
				sq.setEfcPolicyCode(dataInsbPolicyitem.getPolicyno());// 交强保单号
				if (!StringUtil.isEmpty(dataInsbPolicyitem.getDiscountCharge())) {
					sq.setEfcCharge(BigDecimal.valueOf(dataInsbPolicyitem.getDiscountCharge()).setScale(5, RoundingMode.HALF_UP));// 交强险保费
				}
				if (!StringUtil.isEmpty(dataInsbPolicyitem.getDiscountRate())) {
					sq.setTrafficDiscountRate(BigDecimal.valueOf(dataInsbPolicyitem.getDiscountRate()).setScale(6));// 交强险折扣
				}
			}
		}

		INSBCarkindprice queryInsbCarkindprice = new INSBCarkindprice();
		queryInsbCarkindprice.setTaskid(taskId);
		queryInsbCarkindprice.setInscomcode(insurancecompanyid);
		List<INSBCarkindprice> resultInsbCarkindpriceList = insbCarkindpriceService.queryList(queryInsbCarkindprice);
		for (INSBCarkindprice dataInsbCarkindprice : resultInsbCarkindpriceList) {
			if ("VehicleTax".equalsIgnoreCase(dataInsbCarkindprice.getInskindcode())&&!StringUtil.isEmpty(dataInsbCarkindprice.getDiscountCharge())) {
				if (StringUtil.isEmpty(sq.getTaxCharge())) {
					sq.setTaxCharge(BigDecimal.valueOf(dataInsbCarkindprice.getDiscountCharge()).setScale(5, RoundingMode.HALF_UP));// 车船税金额
				}else{
					sq.setTaxCharge(BigDecimal.valueOf(dataInsbCarkindprice.getDiscountCharge()).setScale(5, RoundingMode.HALF_UP).add(sq.getTaxCharge()));
				}
			}
			if ("VehicleTaxOverdueFine".equalsIgnoreCase(dataInsbCarkindprice.getInskindcode())&&!StringUtil.isEmpty(dataInsbCarkindprice.getDiscountCharge())) {
				if (StringUtil.isEmpty(sq.getTaxCharge())) {
					sq.setTaxCharge(BigDecimal.valueOf(dataInsbCarkindprice.getDiscountCharge()).setScale(5, RoundingMode.HALF_UP));// 车船税金额滞纳金
				}else{
					sq.setTaxCharge(BigDecimal.valueOf(dataInsbCarkindprice.getDiscountCharge()).setScale(5, RoundingMode.HALF_UP).add(sq.getTaxCharge()));
				}
			}
		}

		if (!StringUtil.isEmpty(resultInsbPolicyitemList1.get(0).getTotalepremium())) {
			sq.setTotalCharge(BigDecimal.valueOf(resultInsbPolicyitemList1.get(0).getTotalepremium()).setScale(5, RoundingMode.HALF_UP));
		}

		INSBQuotetotalinfo queryInsbQuotetotalinfo = new INSBQuotetotalinfo();
		queryInsbQuotetotalinfo.setTaskid(taskId);
		queryInsbQuotetotalinfo = insbQuotetotalinfoService.queryOne(queryInsbQuotetotalinfo);
		INSBQuoteinfo queryInsbQuoteinfo = new INSBQuoteinfo();
		queryInsbQuoteinfo.setQuotetotalinfoid(queryInsbQuotetotalinfo.getId());
		queryInsbQuoteinfo.setInscomcode(insurancecompanyid);
		queryInsbQuoteinfo = insbQuoteinfoService.queryOne(queryInsbQuoteinfo);
		if (!StringUtil.isEmpty(queryInsbQuoteinfo)) {
			sq.setMisc(queryInsbQuoteinfo.getNoti());

			if (StringUtil.isNotEmpty(queryInsbQuoteinfo.getDiscountrate())) {
				Map discountrate = com.alibaba.fastjson.JSONObject.parseObject(queryInsbQuoteinfo.getDiscountrate(), Map.class);
				Map<String, Double> discountRates = new HashMap<>(discountrate.size());

				for (Object key : discountrate.keySet()) {
					discountRates.put(key.toString(), Double.parseDouble((String) discountrate.get(key.toString())));
				}

				if (!discountRates.isEmpty()) {
					sq.setDiscountRates(discountRates);
				}
			}
		}

		INSBLastyearinsureinfo queryInsbLastyearinsureinfo = insbRulequerycarinfoDao.queryLastYearClainInfo(taskId);

		if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo)) {
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getSypolicyno())) {
				sq.setSypolicyno(queryInsbLastyearinsureinfo.getSypolicyno());
			}
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getJqpolicyno())) {
				sq.setJqpolicyno(queryInsbLastyearinsureinfo.getJqpolicyno());
			}
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getSyclaimrate())) {
				sq.setSyclaimrate(queryInsbLastyearinsureinfo.getSyclaimrate());
			}
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getJqclaimrate())) {
				sq.setJqclaimrate(queryInsbLastyearinsureinfo.getJqclaimrate());
			}
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getSyclaimtimes())) {
				sq.setSyclaimtimes(queryInsbLastyearinsureinfo.getSyclaimtimes());
			}
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getJqclaimtimes())) {
				sq.setJqclaimtimes(queryInsbLastyearinsureinfo.getJqclaimtimes());
			}
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getNoclaimdiscountcoefficient())) {
				sq.setNoclaimdiscountcoefficient(new BigDecimal(queryInsbLastyearinsureinfo.getNoclaimdiscountcoefficient()));
			}
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getCompulsoryclaimrate())) {
				sq.setCompulsoryclaimrate(new BigDecimal(queryInsbLastyearinsureinfo.getCompulsoryclaimrate()));
			}
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getBwcompulsoryclaimtimes())) {
				sq.setBwcompulsoryclaimtimes(queryInsbLastyearinsureinfo.getBwcompulsoryclaimtimes());
			}
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getNoclaimdiscountcoefficientreasons())) {
				sq.setNoclaimdiscountcoefficientreasons(queryInsbLastyearinsureinfo.getNoclaimdiscountcoefficientreasons());
			}
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getCompulsoryclaimratereasons())) {
				sq.setCompulsoryclaimratereasons(queryInsbLastyearinsureinfo.getCompulsoryclaimratereasons());
			}
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getSyclaims())) {
				sq.setSyclaims(queryInsbLastyearinsureinfo.getSyclaims());
			}
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getJqclaims())) {
				sq.setJqclaims(queryInsbLastyearinsureinfo.getJqclaims());
			}
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getSystartdate())) {
				sq.setSystartdate(queryInsbLastyearinsureinfo.getSystartdate());
			}
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getSyenddate())) {
				sq.setSyenddate(queryInsbLastyearinsureinfo.getSyenddate());
			}
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getJqstartdate())) {
				sq.setJqstartdate(queryInsbLastyearinsureinfo.getJqstartdate());
			}
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getJqenddate())) {
				sq.setJqenddate(queryInsbLastyearinsureinfo.getJqenddate());
			}
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getBwcommercialclaimtimes())) {
				sq.setBwcommercialclaimtimes(queryInsbLastyearinsureinfo.getBwcommercialclaimtimes());
			}
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getSupplierid())) {
				sq.setSupplierid(queryInsbLastyearinsureinfo.getSupplierid());
			}
			if (!StringUtil.isEmpty(queryInsbLastyearinsureinfo.getLoyaltyreasons())) {
				sq.setLoyaltyreasons(queryInsbLastyearinsureinfo.getLoyaltyreasons());
			}
		}

		// INSBOrder insbOrder = new INSBOrder();
		// insbOrder.setTaskid(taskId);
		// insbOrder.setPrvid(insurancecompanyid);
		// insbOrder = insbOrderService.queryOne(insbOrder);
		// if(insbOrder!=null)
		// sq.setOrderId(insbOrder.getOrderno());
		return sq;
	}

	private SpecialRisk getSpecialRisk(String taskId, String insurancecompanyid) {
		SpecialRisk returnSpecialRisk = new SpecialRisk();
		INSBCarkindprice queryInsbCarkindprice = new INSBCarkindprice();
		queryInsbCarkindprice.setTaskid(taskId);
		queryInsbCarkindprice.setInscomcode(insurancecompanyid);
		List<INSBCarkindprice> resultInsbCarkindpriceList = insbCarkindpriceService.queryList(queryInsbCarkindprice);
		List<SuiteDef> suiteDefList = new ArrayList<SuiteDef>();
		for (INSBCarkindprice dataInsbCarkindprice : resultInsbCarkindpriceList) {
			if ("NcfDriverPassengerIns".equals(dataInsbCarkindprice.getInskindcode()) || "NcfBasicClause".equals(dataInsbCarkindprice.getInskindcode())
					|| "NcfAddtionalClause".equals(dataInsbCarkindprice.getInskindcode()) || "NcfClause".equals(dataInsbCarkindprice.getInskindcode())) {
				SuiteDef suiteDef = new SuiteDef();
				suiteDef.setCode(dataInsbCarkindprice.getInskindcode());// 险种代码
				suiteDef.setName(dataInsbCarkindprice.getRiskname());// *险种名称
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getAmount())) {
					suiteDef.setAmount(BigDecimal.valueOf(dataInsbCarkindprice.getAmount()).setScale(5, RoundingMode.HALF_UP));// 保额
				}
				suiteDef.setAmountDesc(dataInsbCarkindprice.getAmountDesc());// 保额描述
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getAmountprice())) {
					suiteDef.setOrgCharge(BigDecimal.valueOf(dataInsbCarkindprice.getAmountprice()).setScale(5, RoundingMode.HALF_UP));// 原始保费
				}
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getDiscountCharge())) {
					suiteDef.setDiscountCharge(BigDecimal.valueOf(dataInsbCarkindprice.getDiscountCharge()).setScale(5, RoundingMode.HALF_UP));// 折后保费
				}
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getDiscountRate())) {
					suiteDef.setDiscountRate(BigDecimal.valueOf(dataInsbCarkindprice.getDiscountRate()).setScale(6));// 折扣率
				}
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getNoTaxPremium())) {
					suiteDef.setNoTaxPremium(dataInsbCarkindprice.getNoTaxPremium());// 不含税保费-营改增
				}
				if (!StringUtil.isEmpty(dataInsbCarkindprice.getTax())) {
					suiteDef.setTax(dataInsbCarkindprice.getTax());// 税-营改增
				}
				suiteDefList.add(suiteDef);
			}
		}
		returnSpecialRisk.setSuites(suiteDefList);
		return returnSpecialRisk;
	}

	/**
	 * 任务锁定
	 */
	@Override
	public String lockTask(String taskId, String isLocked, String insurancecompanyid) {
		INSBFlowinfo insbFlowinfo = new INSBFlowinfo();
		insbFlowinfo.setTaskid(taskId);
		insbFlowinfo.setInscomcode(insurancecompanyid);
		INSBFlowinfo resultInsbFlowinfo = insbFlowinfoService.queryOne(insbFlowinfo);
		Map<String, String> Map = new HashMap<String, String>();
		String result = "false";
		if (resultInsbFlowinfo != null) {
			switch (isLocked) {
				case "true":
					resultInsbFlowinfo.setIslock("1");
					break;
				case "false":
					resultInsbFlowinfo.setIslock("0");
					break;
				default:
					Map.put("error", "状态有误");
					break;
			}
			if (Map.isEmpty()) {
				resultInsbFlowinfo.setId(resultInsbFlowinfo.getId());
				resultInsbFlowinfo.setModifytime(new Date());
				try {
					insbFlowinfoService.updateById(resultInsbFlowinfo);
					result = "true";

				} catch (Exception e) {
					e.printStackTrace();
				}

				Map.put("result", result);

			}
		}
		return JSONObject.fromObject(Map).toString();
	}

	/**
	 * 任务改派
	 */
	@Override
	public String forwardTask(String taskid, String insurancecompanyid, String fromuser, String touser) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", true);
		// }
		//
		return JSONObject.fromObject(map).toString();
	}

	/**
	 * 
	 * @param taskId 工作流程号
	 * @param insurancecompanyid 供应商id
	 * @param carInfo 回写CarInfo数据json
	 * @param ownerName 车主名称
	 * @throws Exception
	 */
	private void saveCarInfo(String taskId, String insurancecompanyid, JSONObject carInfo,String ownerName) throws Exception {
		/**
		 * 根据taskId、insurancecompanyid 查询INSBCarinfohis
		 * 如果有数据就更新到INSBCarinfohis如果没有数据就更新到INSBCarinfo
		 */
		INSBCarinfo dataInsbCarinfo = new INSBCarinfo();
		INSBCarinfo queryInsbCarinfo = new INSBCarinfo();
		queryInsbCarinfo.setTaskid(taskId);
		List<INSBCarinfo> resultInsbCarinfoList = insbCarinfoService.queryList(queryInsbCarinfo);
		if (resultInsbCarinfoList.size() > 0) {
			dataInsbCarinfo = resultInsbCarinfoList.get(0);
			if(!StringUtil.isEmpty(carInfo.get("paidtaxes")) && "1".equals(String.valueOf(carInfo.get("paidtaxes"))))
				dataInsbCarinfo.setPaidtaxes(String.valueOf(carInfo.get("paidtaxes")));
				insbCarinfoService.updateById(dataInsbCarinfo);
		} else
			return;

		INSBCarinfohis dataInsbCarinfoHis = new INSBCarinfohis();
		INSBCarinfohis queryInsbCarinfoHis = new INSBCarinfohis();
		queryInsbCarinfoHis.setTaskid(taskId);
		queryInsbCarinfoHis.setInscomcode(insurancecompanyid);
		List<INSBCarinfohis> resultInsbCarinfoHisList = insbCarinfohisService.queryList(queryInsbCarinfoHis);
		if (resultInsbCarinfoHisList.size() > 0) {
			dataInsbCarinfoHis = resultInsbCarinfoHisList.get(0);
		} else {
			try {
				PropertyUtils.copyProperties(dataInsbCarinfoHis, dataInsbCarinfo);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				LogUtil.error("taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",接口车辆信息获得出错1");
				e.printStackTrace();
				return;
			}
		}

		INSBCarmodelinfo datainsbCarmodelinfo = new INSBCarmodelinfo();
		INSBCarmodelinfo queryinsbCarmodelinfo = new INSBCarmodelinfo();
		queryinsbCarmodelinfo.setCarinfoid(dataInsbCarinfo.getId());
		List<INSBCarmodelinfo> resultInsbCarmodelinfoList = insbCarmodelinfoService.queryList(queryinsbCarmodelinfo);
		if (resultInsbCarmodelinfoList.size() > 0) {
			datainsbCarmodelinfo = resultInsbCarmodelinfoList.get(0);
		}
		INSBCarmodelinfohis dataInsbCarmodelinfohis = new INSBCarmodelinfohis();
		INSBCarmodelinfohis queryInsbCarmodelinfohis = new INSBCarmodelinfohis();
		queryInsbCarmodelinfohis.setCarinfoid(dataInsbCarinfo.getId());
		queryInsbCarmodelinfohis.setInscomcode(insurancecompanyid);
		List<INSBCarmodelinfohis> resultInsbCarmodelinfohisList = insbCarmodelinfohisService.queryList(queryInsbCarmodelinfohis);
		if (resultInsbCarmodelinfohisList.size() > 0) {
			dataInsbCarmodelinfohis = resultInsbCarmodelinfohisList.get(0);
		} else {
			try {
				PropertyUtils.copyProperties(dataInsbCarmodelinfohis, datainsbCarmodelinfo);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				LogUtil.error("taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",接口车型信息获得出错2");
				e.printStackTrace();
				return;
			}
		}

		if (!StringUtil.isEmpty(carInfo.get("plateNum"))) {
			dataInsbCarinfoHis.setCarlicenseno(carInfo.get("plateNum").toString());// 车牌号
		}
		if (!StringUtil.isEmpty(carInfo.get("plateColor"))) {
			dataInsbCarinfoHis.setPlatecolor(Integer.parseInt(carInfo.get("plateColor").toString()));// 号牌颜色
		}
		if (!StringUtil.isEmpty(carInfo.get("plateType"))) {
			dataInsbCarinfoHis.setPlateType(Integer.parseInt(carInfo.get("plateType").toString()));// 号牌种类
		}
		if (!StringUtil.isEmpty(carInfo.get("vin"))) {
			dataInsbCarinfoHis.setVincode(carInfo.get("vin").toString());// 车架号
		}
		if (!StringUtil.isEmpty(carInfo.get("engineNum"))) {
			dataInsbCarinfoHis.setEngineno(carInfo.get("engineNum").toString());// 发动机号
		}
		if (!StringUtil.isEmpty(carInfo.get("useProps"))) {
			try {
				INSCCode queryInscCode = new INSCCode();
				queryInscCode.setCodetype("UseProps");
				queryInscCode.setCodevalue(carInfo.get("useProps").toString());
				List<INSCCode> insclist = inscCodeService.queryList(queryInscCode);
				dataInsbCarinfoHis.setCarproperty((insclist.size() > 0) ? carInfo.get("useProps").toString() : "1");// 使用性质
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!StringUtil.isEmpty(carInfo.get("carUserType"))) {
			try {
				INSCCode queryInscCode = new INSCCode();
				queryInscCode.setCodetype("UserType");
				queryInscCode.setCodevalue(carInfo.get("carUserType").toString());
				List<INSCCode> insclist = inscCodeService.queryList(queryInscCode);
				dataInsbCarinfoHis.setProperty((insclist.size() > 0) ? carInfo.get("carUserType").toString() : "0");// 车辆用户类型
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!StringUtil.isEmpty(carInfo.get("firstRegDate"))) {
			dataInsbCarinfoHis.setRegistdate(DateUtil.parse(carInfo.get("firstRegDate").toString(), "yyyy-MM-dd"));// 初登日期
		}
		if (!StringUtil.isEmpty(carInfo.get("ineffectualDate"))) {
			dataInsbCarinfoHis.setIneffectualDate(DateUtil.parse(carInfo.get("ineffectualDate").toString(), "yyyy-MM-dd HH:mm:ss"));// 检验有效日期
		}
		if (!StringUtil.isEmpty(carInfo.get("rejectDate"))) {
			dataInsbCarinfoHis.setRejectDate(DateUtil.parse(carInfo.get("rejectDate").toString(), "yyyy-MM-dd HH:mm:ss"));// 强制有效期
		}
		if (!StringUtil.isEmpty(carInfo.get("lastCheckDate"))) {
			dataInsbCarinfoHis.setLastCheckDate(DateUtil.parse(carInfo.get("lastCheckDate").toString(), "yyyy-MM-dd HH:mm:ss"));// 最近定检日期
		}
		if (!StringUtil.isEmpty(carInfo.get("isTransfer"))) {
			dataInsbCarinfoHis.setIsTransfercar(Boolean.valueOf(carInfo.get("isTransfer").toString()) ? "1" : "0");// 是过户车
		}
		if (!StringUtil.isEmpty(carInfo.get("isNew"))) {
			dataInsbCarinfoHis.setIsNew(Boolean.valueOf(carInfo.get("isNew").toString()) ? "1" : "0");// 新车标志
		}
		if (!StringUtil.isEmpty(carInfo.get("isTransfer")) && "true".equals(carInfo.get("isTransfer").toString())) {
			if (!StringUtil.isEmpty(carInfo.get("transferDate"))) {
				dataInsbCarinfoHis.setTransferdate(DateUtil.parse((carInfo.get("transferDate").toString()), "yyyy-MM-dd HH:mm:ss"));// 过户转移登记日期
			}
		} else if (!StringUtil.isEmpty(carInfo.get("isTransfer")) && "false".equals(carInfo.get("isTransfer").toString())) {
			dataInsbCarinfoHis.setTransferdate(null);
		}
		if (!StringUtil.isEmpty(carInfo.get("isLoanManyYearsFlag"))) {
			dataInsbCarinfoHis.setLoanManyYearsFlag(Boolean.valueOf(carInfo.get("isLoanManyYearsFlag").toString()) ? "1" : "0");// 是否车贷投保多年标志
		}
		if (!StringUtil.isEmpty(carInfo.get("isLoansCar"))) {
			dataInsbCarinfoHis.setIsLoansCar(Boolean.valueOf(carInfo.get("isLoansCar").toString()) ? "1" : "0");// 是否贷款车
		}
		if (!StringUtil.isEmpty(carInfo.get("carVehicularApplications"))) {
			dataInsbCarinfoHis.setCarVehicularApplications(Integer.valueOf(carInfo.get("carVehicularApplications").toString()));// 车辆用途
		}
		if (!StringUtil.isEmpty(carInfo.get("isFieldCar"))) {
			dataInsbCarinfoHis.setIsFieldCar(Boolean.valueOf(carInfo.get("isFieldCar").toString()) ? "1" : "0");// 是否军牌或外地车
		}
		if (!StringUtil.isEmpty(carInfo.get("carBodyColor"))) {
			dataInsbCarinfoHis.setCarBodyColor(Integer.valueOf(carInfo.get("carBodyColor").toString()));// 车身颜色
		}
		if (!StringUtil.isEmpty(carInfo.get("avgMileage"))) {
			String temp = "0";
			if (carInfo.get("avgMileage").toString().contains("万")) {
				temp = carInfo.get("avgMileage").toString().replace("万", "");
			} else {
				temp = Double.valueOf(carInfo.get("avgMileage").toString()) / 10000 + "";
			}
			if (Double.valueOf(temp) < 3) {
				dataInsbCarinfoHis.setMileage("0");// 平均行驶里程
			} else if (Double.valueOf(temp) >= 3 && Double.valueOf(temp) <= 5) {
				dataInsbCarinfoHis.setMileage("1");// 平均行驶里程
			} else if (Double.valueOf(temp) > 5) {
				dataInsbCarinfoHis.setMileage("2");// 平均行驶里程
			}
		}
		if (!StringUtil.isEmpty(carInfo.get("misc"))) {// 车的杂项
			dataInsbCarinfoHis.setNoti(carInfo.get("misc").toString());
		}
		if (!StringUtil.isEmpty(carInfo.get("driverArea"))) {
			dataInsbCarinfoHis.setDrivingarea(carInfo.get("driverArea").toString());// 约定行驶区域
		}
		if (!StringUtil.isEmpty(carInfo.get("jgVehicleType"))) {
			dataInsbCarmodelinfohis.setJgVehicleType(Integer.valueOf(carInfo.get("jgVehicleType").toString()));// 交管车辆类型
		}
		if (!StringUtil.isEmpty(carInfo.get("glassType"))) {
			dataInsbCarmodelinfohis.setGlassType(Integer.valueOf(carInfo.get("glassType").toString()) + 1);// 玻璃类型
		}
		if (!StringUtil.isEmpty(carInfo.get("carBrandName"))) {
			dataInsbCarmodelinfohis.setBrandname(carInfo.get("carBrandName").toString());// 车型品牌名称
		}
		if (!StringUtil.isEmpty(carInfo.get("carModelName"))) {
			dataInsbCarmodelinfohis.setStandardname(carInfo.get("carModelName").toString());// 车型名称
			dataInsbCarinfoHis.setStandardfullname(carInfo.get("carModelName").toString());
			dataInsbCarmodelinfohis.setStandardfullname(carInfo.get("carModelName").toString());
		}
		if (!StringUtil.isEmpty(carInfo.get("rbCarModelName"))) {
			dataInsbCarmodelinfohis.setRbCarModelName(carInfo.get("rbCarModelName").toString());// 人保车型名称
		}
		if (!StringUtil.isEmpty(carInfo.get("jyCarModelName"))) {
			dataInsbCarmodelinfohis.setJyCarModelName(carInfo.get("jyCarModelName").toString());// 精友车型名称
		}
		if (!StringUtil.isEmpty(carInfo.get("bwCode"))) {
			dataInsbCarmodelinfohis.setBwCode(carInfo.get("bwCode").toString());// 内部车型code
		}
		if (!StringUtil.isEmpty(carInfo.get("jyCode"))) {
			dataInsbCarmodelinfohis.setJyCode(carInfo.get("jyCode").toString());// 精友车型code
		}
		if (!StringUtil.isEmpty(carInfo.get("rbCode"))) {
			dataInsbCarmodelinfohis.setRbCode(carInfo.get("rbCode").toString());// 人保车型code
		}
		if (!StringUtil.isEmpty(carInfo.get("isRbMatch"))) {
			dataInsbCarmodelinfohis.setIsRbMatch(Boolean.valueOf(carInfo.get("isRbMatch").toString()) ? "1" : "0");// 是否人保code匹配
		}
		if (!StringUtil.isEmpty(carInfo.get("isJyMatch"))) {
			dataInsbCarmodelinfohis.setIsJyMatch(Boolean.valueOf(carInfo.get("isJyMatch").toString()) ? "1" : "0");// 是否精友code匹配
		}
		if (!StringUtil.isEmpty(carInfo.get("insuranceCode"))) {
			dataInsbCarmodelinfohis.setInsuranceCode(carInfo.get("insuranceCode").toString());// 保险公司车型Code
		}
		if (!StringUtil.isEmpty(carInfo.get("price"))) {
			dataInsbCarmodelinfohis.setPrice(Double.valueOf(carInfo.get("price").toString()));// 不含税价
		}
		if (!StringUtil.isEmpty(carInfo.get("taxPrice"))) {
			dataInsbCarmodelinfohis.setTaxprice(Double.valueOf(carInfo.get("taxPrice").toString()));// 不含税价
		}
		if (!StringUtil.isEmpty(carInfo.get("taxAnalogyPrice"))) {
			dataInsbCarmodelinfohis.setAnalogytaxprice(Double.valueOf(carInfo.get("taxAnalogyPrice").toString()));// 含税类比价
		}
		if (!StringUtil.isEmpty(carInfo.get("analogyPrice"))) {
			dataInsbCarmodelinfohis.setAnalogyprice(Double.valueOf(carInfo.get("analogyPrice").toString()));// 不含税类比价
		}
		if (!StringUtil.isEmpty(carInfo.get("displacement"))) {
			dataInsbCarmodelinfohis.setDisplacement(Double.valueOf(carInfo.get("displacement").toString()));// 排气量
		}
		if (!StringUtil.isEmpty(carInfo.get("modelLoad"))) {
			dataInsbCarmodelinfohis.setUnwrtweight(Double.valueOf(carInfo.get("modelLoad").toString()));// 载重量
		}
		if (!StringUtil.isEmpty(carInfo.get("fullLoad"))) {
			dataInsbCarmodelinfohis.setFullweight(Double.valueOf(carInfo.get("fullLoad").toString()));// 车身自重
		}
		if (!StringUtil.isEmpty(carInfo.get("carVehicleOrigin"))) {
			dataInsbCarmodelinfohis.setCarVehicleOrigin(Integer.valueOf(carInfo.get("carVehicleOrigin").toString()));// 产地类型
		}
		if (!StringUtil.isEmpty(carInfo.get("seatCnt"))) {
			dataInsbCarmodelinfohis.setSeat(Integer.valueOf(carInfo.get("seatCnt").toString()));// 座位数
		}
		if (!StringUtil.isEmpty(carInfo.get("ratedPassengerCapacity"))) {
			dataInsbCarmodelinfohis.setRatedPassengerCapacity(Integer.valueOf(carInfo.get("ratedPassengerCapacity").toString()));// 核定载客人数
		}
		if (!StringUtil.isEmpty(carInfo.get("carPriceType"))) {
			dataInsbCarmodelinfohis.setCarprice(carInfo.get("carPriceType").toString());// 车价选择，最高最低自定义
		}
		if (!StringUtil.isEmpty(carInfo.get("syvehicletypename"))) {
			dataInsbCarmodelinfohis.setSyvehicletypename(carInfo.get("syvehicletypename").toString());// 商业险车辆名称
		}
		if (!StringUtil.isEmpty(carInfo.get("syvehicletypecode"))) {
			dataInsbCarmodelinfohis.setSyvehicletypecode(carInfo.get("syvehicletypecode").toString());// 机动车编码
		}
		if (!StringUtil.isEmpty(carInfo.get("definedCarPrice"))) {
			// dataInsbCarmodelinfohis.setPolicycarprice(Double.valueOf(carInfo.get("definedCarPrice").toString()));
		}
		//修改车主名称
		if(ownerName!=null&&!ownerName.equals("")){
			dataInsbCarinfoHis.setOwnername(ownerName);
		}
		if (resultInsbCarinfoHisList.size() > 0) {
			dataInsbCarinfoHis.setModifytime(new Date());
			insbCarinfohisDao.updateById(dataInsbCarinfoHis);
		} else {
			dataInsbCarinfoHis.setId(null);
			dataInsbCarinfoHis.setOperator("admin");
			dataInsbCarinfoHis.setCreatetime(new Date());
			dataInsbCarinfoHis.setModifytime(new Date());
			dataInsbCarinfoHis.setInscomcode(insurancecompanyid);
			insbCarinfohisDao.insert(dataInsbCarinfoHis);
		}
		if (resultInsbCarmodelinfohisList.size() > 0) {
			dataInsbCarmodelinfohis.setModifytime(new Date());
			insbCarmodelinfohisDao.updateById(dataInsbCarmodelinfohis);
		} else {
			dataInsbCarmodelinfohis.setId(null);
			dataInsbCarmodelinfohis.setOperator("admin");
			dataInsbCarmodelinfohis.setCreatetime(new Date());
			dataInsbCarmodelinfohis.setModifytime(new Date());
			dataInsbCarmodelinfohis.setCarinfoid(dataInsbCarinfo.getId());
			dataInsbCarmodelinfohis.setInscomcode(insurancecompanyid);

			if (StringUtil.isEmpty(dataInsbCarmodelinfohis.getStandardfullname())) {
				dataInsbCarmodelinfohis.setStandardfullname(dataInsbCarinfoHis.getStandardfullname());
			}
			if (dataInsbCarmodelinfohis.getPrice() == null) {
				dataInsbCarmodelinfohis.setPrice(Double.valueOf("0.0"));
			}
			if (dataInsbCarmodelinfohis.getTaxprice() == null) {
				dataInsbCarmodelinfohis.setTaxprice(Double.valueOf("0.0"));
			}
			if (dataInsbCarmodelinfohis.getAnalogyprice() == null) {
				dataInsbCarmodelinfohis.setAnalogyprice(Double.valueOf("0.0"));
			}
			if (dataInsbCarmodelinfohis.getAnalogytaxprice() == null) {
				dataInsbCarmodelinfohis.setAnalogytaxprice(Double.valueOf("0.0"));
			}

			insbCarmodelinfohisDao.insert(dataInsbCarmodelinfohis);
		}

		INSBCarowneinfo queryInsbCarowneinfo = new INSBCarowneinfo();
		queryInsbCarowneinfo.setTaskid(taskId);
		queryInsbCarowneinfo = insbCarowneinfoService.queryOne(queryInsbCarowneinfo);
		if (!StringUtil.isEmpty(queryInsbCarowneinfo)) {
			INSBPerson dateInsbPerson = new INSBPerson();
			dateInsbPerson = insbPersonService.queryById(queryInsbCarowneinfo.getPersonid());
			if (!StringUtil.isEmpty(dateInsbPerson)) {
				INSBCarinfohis queryInsbCarinfohis2 = new INSBCarinfohis();
				queryInsbCarinfohis2.setTaskid(taskId);
				queryInsbCarinfohis2.setInscomcode(insurancecompanyid);
				queryInsbCarinfohis2 = insbCarinfohisService.queryOne(queryInsbCarinfohis2);
				if (!StringUtil.isEmpty(queryInsbCarinfohis2)) {
					queryInsbCarinfohis2.setOwner(dateInsbPerson.getId());
					queryInsbCarinfohis2.setOwnername(dateInsbPerson.getName());
					insbCarinfohisService.updateById(queryInsbCarinfohis2);
				}
			}
		}
		INSBQuotetotalinfo queryInsbQuotetotalinfo = new INSBQuotetotalinfo();
		queryInsbQuotetotalinfo.setTaskid(taskId);
		queryInsbQuotetotalinfo = insbQuotetotalinfoService.queryOne(queryInsbQuotetotalinfo);
		INSBQuoteinfo queryInsbQuoteinfo = new INSBQuoteinfo();
		queryInsbQuoteinfo.setQuotetotalinfoid(queryInsbQuotetotalinfo.getId());
		queryInsbQuoteinfo.setInscomcode(insurancecompanyid);
		queryInsbQuoteinfo = insbQuoteinfoService.queryOne(queryInsbQuoteinfo);
		queryInsbQuoteinfo.setModifytime(new Date());
		if (!StringUtil.isEmpty(carInfo.get("plateNum"))) {
			queryInsbQuoteinfo.setPlatenumber(carInfo.get("plateNum").toString());
		}
		insbQuoteinfoService.updateById(queryInsbQuoteinfo);
		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",车信息保存完成====");
	}
	/**
	 * 根据taskid到insbcarowneinfo表中查询,得到personid,即人员信息表id,从而获得车主信息
	 * 
	 */
	private void saveCarOwnerInfo(String taskId, String insurancecompanyid, JSONObject carOwnerInfo) throws Exception {
		INSBCarowneinfo dataInsbCarowneinfo = new INSBCarowneinfo();
		dataInsbCarowneinfo.setTaskid(taskId);
		dataInsbCarowneinfo = insbCarowneinfoService.queryOne(dataInsbCarowneinfo);
		if (StringUtil.isEmpty(dataInsbCarowneinfo)) {
			INSBPerson dataInsbPerson1 = new INSBPerson();
			dataInsbPerson1=insbPersonHelpService.getINSBPersonObj(ConstUtil.OPER_ADD, carOwnerInfo, taskId, insurancecompanyid,null,"INSBCarowneinfo", "address",true,dataInsbPerson1);
			//dataInsbPerson1.setNoti("车主信息1-"+insurancecompanyid);
			insbPersonService.insert(dataInsbPerson1);
			INSBCarowneinfo dataCarowneinfo = new INSBCarowneinfo();
			dataCarowneinfo=insbPersonHelpService.addINSBCarowneinfo(dataCarowneinfo, new Date(), ConstUtil.OPER_ADMIN, taskId, dataInsbPerson1.getId());
			insbPersonHelpService.operINSBRelationpersonhis(taskId,insurancecompanyid,carOwnerInfo);
		} else {
			INSBPerson dataInsbPerson = insbPersonService.queryById(dataInsbCarowneinfo.getPersonid());
			if (dataInsbPerson == null) {
				dataInsbPerson = new INSBPerson();
				dataInsbPerson=insbPersonHelpService.getINSBPersonObj(ConstUtil.OPER_ADD, carOwnerInfo, taskId, insurancecompanyid,null,"INSBCarowneinfo", "address",true,dataInsbPerson);
				//dataInsbPerson.setNoti("车主信息2-"+insurancecompanyid);
				insbPersonService.insert(dataInsbPerson);
				dataInsbCarowneinfo.setPersonid(dataInsbPerson.getId());
				insbCarowneinfoService.updateById(dataInsbCarowneinfo);
				insbPersonHelpService.operINSBRelationpersonhis(taskId,insurancecompanyid,carOwnerInfo);
			} else {
				dataInsbPerson=insbPersonHelpService.getINSBPersonObj(ConstUtil.OPER_UPDATE, carOwnerInfo, taskId, insurancecompanyid,dataInsbPerson.getId(),"INSBCarowneinfo", "address",true,dataInsbPerson);
				//dataInsbPerson.setNoti("车主信息3-"+insurancecompanyid);
				insbPersonService.updateById(dataInsbPerson);
				insbPersonHelpService.operINSBRelationpersonhis(taskId,insurancecompanyid,carOwnerInfo);
			}
			INSBQuotetotalinfo queryInsbQuotetotalinfo = new INSBQuotetotalinfo();
			queryInsbQuotetotalinfo.setTaskid(taskId);
			queryInsbQuotetotalinfo = insbQuotetotalinfoService.queryOne(queryInsbQuotetotalinfo);
			INSBQuoteinfo queryInsbQuoteinfo = new INSBQuoteinfo();
			queryInsbQuoteinfo.setQuotetotalinfoid(queryInsbQuotetotalinfo.getId());
			queryInsbQuoteinfo.setInscomcode(insurancecompanyid);
			queryInsbQuoteinfo = insbQuoteinfoService.queryOne(queryInsbQuoteinfo);
			queryInsbQuoteinfo.setModifytime(new Date());
			if (!StringUtil.isEmpty(carOwnerInfo.get("name"))) {
				queryInsbQuoteinfo.setOwnername(carOwnerInfo.get("name").toString());// 姓名
			}
			insbQuoteinfoService.updateById(queryInsbQuoteinfo);
		}
		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",车主信息保存完成====");
	}
	private void saveSubAgentInfo(String taskId, String insurancecompanyid, JSONObject subAgentInfo) throws Exception {
		/**
		 * 根据taskid去订单表中 查到代理人的工号,再去insbagent中查到代理人的信息
		 *
		 * @author zhangdi
		 */

		/*
		 * INSBOrder queryInsbOrder = new INSBOrder();
		 * queryInsbOrder.setTaskid(taskId); INSBOrder dataInsbOrder =
		 * insbOrderService.queryOne(queryInsbOrder);
		 */
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskId);
		List<INSBPolicyitem> insbPolicyitemList = insbPolicyitemService.queryList(insbPolicyitem);
		List<INSBPolicyitem> resultInsbPolicyitemList1 = new ArrayList<INSBPolicyitem>();
		for (INSBPolicyitem insbPolicyitem111 : insbPolicyitemList) {
			if (insurancecompanyid.equals(insbPolicyitem111.getInscomcode())) {
				resultInsbPolicyitemList1.add(insbPolicyitem111);
			}
		}
		if (resultInsbPolicyitemList1.size() == 0)
			resultInsbPolicyitemList1 = insbPolicyitemList;
		if (resultInsbPolicyitemList1.size() < 0 && !StringUtil.isEmpty(resultInsbPolicyitemList1.get(0).getAgentnum()))
			return;
		INSBAgent queryInsbAgent = new INSBAgent();
		queryInsbAgent.setAgentcode(resultInsbPolicyitemList1.get(0).getAgentnum());
		INSBAgent dataInsbAgent = insbAgentService.queryOne(queryInsbAgent);
		if (dataInsbAgent == null)
			return;

		if (!StringUtil.isEmpty(subAgentInfo.get("isOfficial"))) {
			dataInsbAgent.setJobnumtype(Boolean.valueOf(subAgentInfo.get("isOfficial").toString()) ? 2 : 1);// 用户类型
		}
		if (!StringUtil.isEmpty(subAgentInfo.get("name"))) {
			dataInsbAgent.setName(subAgentInfo.get("name").toString());// 姓名
		}
		if (!StringUtil.isEmpty(subAgentInfo.get("sex"))) {
			dataInsbAgent.setSex(subAgentInfo.get("sex").toString());// 性别
		}
		if (!StringUtil.isEmpty(subAgentInfo.get("idCardType"))) {
			dataInsbAgent.setIdnotype(subAgentInfo.get("idCardType").toString());// 证件类型
		}
		if (!StringUtil.isEmpty(subAgentInfo.get("idCard"))) {
			dataInsbAgent.setIdno(subAgentInfo.get("idCard").toString());// 证件号
		}
		if (!StringUtil.isEmpty(subAgentInfo.get("phone"))) {
			dataInsbAgent.setPhone(subAgentInfo.get("phone").toString());// 电话
		}
		if (!StringUtil.isEmpty(subAgentInfo.get("mobile"))) {// 手机
			if (StringUtil.isEmpty(dataInsbAgent.getMobile()))
				dataInsbAgent.setMobile2(subAgentInfo.get("mobile").toString());
			else
				dataInsbAgent.setMobile(subAgentInfo.get("mobile").toString());
		}
		if (!StringUtil.isEmpty(subAgentInfo.get("email"))) {
			dataInsbAgent.setEmail(subAgentInfo.get("email").toString());// 邮箱
		}
		if (!StringUtil.isEmpty(subAgentInfo.get("virtualWorkerID"))) {
			dataInsbAgent.setTempjobnum(subAgentInfo.get("virtualWorkerID").toString());// 临时工号
		}
		dataInsbAgent.setModifytime(new Date());
		iNSBAgentDao.updateById(dataInsbAgent);

		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",sub代理人信息保存完成====");

	}

	private void saveAgentInfo(String taskId, String insurancecompanyid, JSONObject agentInfo) throws Exception {
		/**
		 * 根据taskid去订单表中 查到代理人的工号,再去insbagent中查到代理人的信息
		 *
		 * @author zhangdi
		 */
		INSBPolicyitem dataInsbPolicyitem = new INSBPolicyitem();
		dataInsbPolicyitem.setTaskid(taskId);
		List<INSBPolicyitem> resultInsbPolicyitemList = insbPolicyitemService.queryList(dataInsbPolicyitem);
		if (resultInsbPolicyitemList.size() == 0)
			return;
		else {
			dataInsbPolicyitem = resultInsbPolicyitemList.get(0);
			for (INSBPolicyitem insbPolicyitem111 : resultInsbPolicyitemList) {
				if (insurancecompanyid.equals(insbPolicyitem111.getInscomcode())) {
					dataInsbPolicyitem = insbPolicyitem111;
					break;
				}
			}
		}
		INSBAgent queryInsbAgent = new INSBAgent();
		queryInsbAgent.setAgentcode(dataInsbPolicyitem.getAgentnum());
		INSBAgent dataInsbAgent = insbAgentService.queryOne(queryInsbAgent);
		if (dataInsbAgent == null)
			return;

		if (!StringUtil.isEmpty(agentInfo.get("name"))) {
			dataInsbAgent.setName(agentInfo.get("name").toString());// 姓名
		}
		if (!StringUtil.isEmpty(agentInfo.get("sex"))) {
			dataInsbAgent.setSex(agentInfo.get("sex").toString());// 性别
		}
		if (!StringUtil.isEmpty(agentInfo.get("idCardType"))) {
			dataInsbAgent.setIdnotype(agentInfo.get("idCardType").toString());// 证件类型
		}
		if (!StringUtil.isEmpty(agentInfo.get("idCard"))) {
			dataInsbAgent.setIdno(agentInfo.get("idCard").toString());// 证件号
		}
		if (!StringUtil.isEmpty(agentInfo.get("phone"))) {
			dataInsbAgent.setTelnum(agentInfo.get("phone").toString());// 电话
		}
		if (!StringUtil.isEmpty(agentInfo.get("mobile"))) {// 手机
			if (StringUtil.isEmpty(dataInsbAgent.getMobile())) {
				dataInsbAgent.setMobile2(agentInfo.get("mobile").toString());
			} else {
				dataInsbAgent.setMobile(agentInfo.get("mobile").toString());
			}
		}
		if (!StringUtil.isEmpty(agentInfo.get("email"))) {
			dataInsbAgent.setEmail(agentInfo.get("email").toString());// 邮箱
		}
		if (!StringUtil.isEmpty(agentInfo.get("workerID"))) {
			dataInsbAgent.setJobnum(agentInfo.get("workerID").toString());// 工号
		}
		if (!StringUtil.isEmpty(agentInfo.get("certNumber"))) {
			if (!dataInsbPolicyitem.getAgentnum().equals("820000234")) {
			} else {
				dataInsbAgent.setLicenseno(agentInfo.get("certNumber").toString());// 从业资格证
			}
		}
		if (!StringUtil.isEmpty(agentInfo.get("teamCode"))) {
			dataInsbAgent.setTeamcode(agentInfo.get("teamCode").toString());// 团队编码
		}
		if (!StringUtil.isEmpty(agentInfo.get("teamName"))) {
			dataInsbAgent.setTeamname(agentInfo.get("teamName").toString());// 团队名称
		}
		if (!StringUtil.isEmpty(agentInfo.get("platformCode"))) {
			dataInsbAgent.setPlatformcode(agentInfo.get("platformCode").toString());// 所属平台编码
		}
		if (!StringUtil.isEmpty(agentInfo.get("platformName"))) {
			dataInsbAgent.setPlatformname(agentInfo.get("platformName").toString());// 所属平台名称
		}
		dataInsbAgent.setModifytime(new Date());
		iNSBAgentDao.updateById(dataInsbAgent);

		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",代理人信息保存完成====");

	}

	private void saveBaseSuiteInfo(String taskId, String insurancecompanyid, JSONObject baseSuiteInfo, JSONObject SQ, String taskSatus, JSONObject specialRisk) throws Exception {

		String oldPolcyInfo = "BaseSuite,原保单信息有：";
		String oldCarkind = "BaseSuite,原保险配置有：";
		String newPolcyInfo = "BaseSuite,回写保单信息有：";
		String newCarkind = "BaseSuite,回写险配置有：";
		String addPolcyInfo = "BaseSuite,新增保单信息有：";
		String addCarkind = "BaseSuite,新增保险配置有：";
		String delPolcyInfo = "BaseSuite,删除保单信息有：";
		String delCarkind = "BaseSuite,删除保险配置有：";
		String updPolcyInfo = "BaseSuite,更新保单信息有：";
		String updCarkind = "BaseSuite,更新保险配置有：";

		List<INSBCarkindprice> oldCarkInd = new ArrayList<INSBCarkindprice>();
		List<INSBCarkindprice> newCarkInd = new ArrayList<INSBCarkindprice>();

		String policyStatus = "5";
		if ("D".equals(taskSatus) || "D2".equals(taskSatus)) {
			policyStatus = "1";
		}

		String plantkey = "qmbzx";

		INSBCarkindprice defultCarkindprice = new INSBCarkindprice();
		defultCarkindprice.setTaskid(taskId);
		defultCarkindprice.setInscomcode(insurancecompanyid);
		List<INSBCarkindprice> defultCarkindpricelists = insbCarkindpriceService.queryList(defultCarkindprice);
		if (defultCarkindpricelists.size() > 0) {
			INSBCarkindprice defultCarkindprice1 = defultCarkindpricelists.get(0);
			if (!StringUtil.isEmpty(defultCarkindprice1) && !StringUtil.isEmpty(defultCarkindprice1.getPlankey())) {
				plantkey = defultCarkindprice1.getPlankey();
			}
		}
		INSBPolicyitem initInsbPolicyitem = new INSBPolicyitem();
		initInsbPolicyitem.setTaskid(taskId);
		initInsbPolicyitem.setInscomcode(insurancecompanyid);
		List<INSBPolicyitem> initInsbPolicyitemList = insbPolicyitemService.queryList(initInsbPolicyitem);
		if (initInsbPolicyitemList.size() == 0) {
			LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",保存险种信息失败,保单表数据不存在====");
			return;
		}
		initInsbPolicyitem = initInsbPolicyitemList.get(0);

		for (INSBPolicyitem countInsbPolicyitem : initInsbPolicyitemList) {
			if ("0".equals(countInsbPolicyitem.getRisktype())) {
				oldPolcyInfo += "商业险,";
			} else if ("1".equals(countInsbPolicyitem.getRisktype())) {
				oldPolcyInfo += "交强险,";
			}
		}
		INSBCarkindprice initInsbCarkindprice = new INSBCarkindprice();
		initInsbCarkindprice.setTaskid(taskId);
		initInsbCarkindprice.setInscomcode(insurancecompanyid);
		List<INSBCarkindprice> initInsbCarkindpriceList = insbCarkindpriceService.queryList(initInsbCarkindprice);
		if (initInsbCarkindpriceList.size() == 0) {
			// 快速续保时没有险种数据，以回写数据为准添加进险种配置
			initInsbCarkindprice = new INSBCarkindprice();
			initInsbCarkindprice.setCreatetime(new Date());
			initInsbCarkindprice.setModifytime(new Date());
			initInsbCarkindprice.setOperator("admin");
			initInsbCarkindprice.setTaskid(taskId);
			initInsbCarkindprice.setInscomcode(insurancecompanyid);
		} else {
			initInsbCarkindprice = initInsbCarkindpriceList.get(0);
		}

		for (INSBCarkindprice countInsbCarkindprice : initInsbCarkindpriceList) {
			oldCarkind += this.getRiskNameByRiskCode(countInsbCarkindprice.getInskindcode()) + ",";
			if (!"VehicleCompulsoryIns".equals(countInsbCarkindprice.getInskindcode()) && !"VehicleTax".equals(countInsbCarkindprice.getInskindcode())
					&& !"VehicleTaxOverdueFine".equals(countInsbCarkindprice.getInskindcode())) {
				oldCarkInd.add(countInsbCarkindprice);
			}
		}

		JSONObject bizSuiteInfoJson = null, efcSuiteInfoJson = null, taxSuiteInfo = null, suiteDefJson = null;
		JSONArray suiteDefListJson = null;
		/* 判断有没有交强险 */
		if (!StringUtil.isEmpty(baseSuiteInfo.get("efcSuiteInfo"))) {
			newPolcyInfo += "交强险,";
			newCarkind += "交强险,";
			efcSuiteInfoJson = JSONObject.fromObject(baseSuiteInfo.get("efcSuiteInfo"));
			INSBPolicyitem queryInsbPolicyitem = new INSBPolicyitem();
			queryInsbPolicyitem.setTaskid(taskId);
			queryInsbPolicyitem.setInscomcode(insurancecompanyid);
			queryInsbPolicyitem.setRisktype("1");
			INSBPolicyitem efcInsbPolicyitem = insbPolicyitemService.queryOne(queryInsbPolicyitem);
			if (StringUtil.isEmpty(efcInsbPolicyitem)) {// 没有查到，则新建一个
				INSBPolicyitem newInsbPolicyitem = new INSBPolicyitem();
				PropertyUtils.copyProperties(newInsbPolicyitem, initInsbPolicyitem);
				newInsbPolicyitem.setId(null);
				newInsbPolicyitem.setCreatetime(new Date());
				newInsbPolicyitem.setModifytime(new Date());
				newInsbPolicyitem.setRisktype("1");
				newInsbPolicyitem.setPaynum(null);
				newInsbPolicyitem.setCheckcode(StringUtil.isEmpty(SQ) ? null : StringUtil.isEmpty(SQ.get("efcTempId")) ? null : SQ.get("efcTempId").toString());
				newInsbPolicyitem.setProposalformno(StringUtil.isEmpty(SQ) ? null : StringUtil.isEmpty(SQ.get("efcProposeNum")) ? null : SQ.get("efcProposeNum").toString());
				newInsbPolicyitem.setPolicyno(StringUtil.isEmpty(SQ) ? null : StringUtil.isEmpty(SQ.get("efcPolicyCode")) ? null : SQ.get("efcPolicyCode").toString());
				newInsbPolicyitem.setTotalepremium(Double.valueOf(StringUtil.isEmpty(SQ) ? "0.00" : StringUtil.isEmpty(SQ.get("totalCharge")) ? "0.00" : SQ.get("totalCharge").toString()));
				newInsbPolicyitem.setPremium(Double.valueOf(StringUtil.isEmpty(SQ) ? "0.00" : StringUtil.isEmpty(SQ.get("efcCharge")) ? "0.00" : SQ.get("efcCharge").toString()));
				newInsbPolicyitem.setDiscountCharge(Double.valueOf(StringUtil.isEmpty(SQ) ? "0.00" : StringUtil.isEmpty(SQ.get("efcCharge")) ? "0.00" : SQ.get("efcCharge").toString()));
				newInsbPolicyitem.setDiscountRate(Double.valueOf(StringUtil.isEmpty(SQ) ? "0.00" : StringUtil.isEmpty(SQ.get("trafficDiscountRate")) ? "0.00" : SQ.get("trafficDiscountRate")
						.toString()));
				newInsbPolicyitem.setPolicystatus(policyStatus);
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("discountCharge"))) {
					newInsbPolicyitem.setPremium(Double.valueOf(efcSuiteInfoJson.get("discountCharge").toString()));
				} else {
					newInsbPolicyitem.setPremium(null);
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("start"))) {
					newInsbPolicyitem.setStartdate(DateUtil.parse(efcSuiteInfoJson.get("start").toString(), "yyyy-MM-dd HH:mm:ss"));
				} else {
					newInsbPolicyitem.setStartdate(null);
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("end"))) {
					newInsbPolicyitem.setEnddate(DateUtil.parse(efcSuiteInfoJson.get("end").toString(), "yyyy-MM-dd HH:mm:ss"));
				} else {
					newInsbPolicyitem.setEnddate(null);
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("discountRate"))) {
					newInsbPolicyitem.setDiscountRate(Double.valueOf(efcSuiteInfoJson.get("discountRate").toString()));
				} else {
					newInsbPolicyitem.setDiscountRate(null);
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("amount"))) {
					newInsbPolicyitem.setAmount(Double.valueOf(efcSuiteInfoJson.get("amount").toString()));
				} else {
					newInsbPolicyitem.setAmount(0.0000);
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("orgCharge"))) {
					newInsbPolicyitem.setNoti(efcSuiteInfoJson.get("orgCharge").toString());
				} else {
					newInsbPolicyitem.setNoti("0.0000");
				}
				insbPolicyitemService.insert(newInsbPolicyitem);
				addPolcyInfo += "交强险,";
			} else {
				efcInsbPolicyitem.setCheckcode(StringUtil.isEmpty(SQ) ? null : StringUtil.isEmpty(SQ.get("efcTempId")) ? null : SQ.get("efcTempId").toString());
				efcInsbPolicyitem.setProposalformno(StringUtil.isEmpty(SQ) ? null : StringUtil.isEmpty(SQ.get("efcProposeNum")) ? null : SQ.get("efcProposeNum").toString());
				efcInsbPolicyitem.setPolicyno(StringUtil.isEmpty(SQ) ? null : StringUtil.isEmpty(SQ.get("efcPolicyCode")) ? null : SQ.get("efcPolicyCode").toString());
				efcInsbPolicyitem.setTotalepremium(Double.valueOf(StringUtil.isEmpty(SQ) ? "0.00" : StringUtil.isEmpty(SQ.get("totalCharge")) ? "0.00" : SQ.get("totalCharge").toString()));
				efcInsbPolicyitem.setPremium(Double.valueOf(StringUtil.isEmpty(SQ) ? "0.00" : StringUtil.isEmpty(SQ.get("efcCharge")) ? "0.00" : SQ.get("efcCharge").toString()));
				efcInsbPolicyitem.setDiscountCharge(Double.valueOf(StringUtil.isEmpty(SQ) ? "0.00" : StringUtil.isEmpty(SQ.get("efcCharge")) ? "0.00" : SQ.get("efcCharge").toString()));
				efcInsbPolicyitem.setDiscountRate(Double.valueOf(StringUtil.isEmpty(SQ) ? "0.00" : StringUtil.isEmpty(SQ.get("trafficDiscountRate")) ? "0.00" : SQ.get("trafficDiscountRate")
						.toString()));
				efcInsbPolicyitem.setPolicystatus(policyStatus);
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("discountCharge"))) {
					efcInsbPolicyitem.setPremium(Double.valueOf(efcSuiteInfoJson.get("discountCharge").toString()));
				} else {
					efcInsbPolicyitem.setPremium(null);
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("start"))) {
					efcInsbPolicyitem.setStartdate(DateUtil.parse(efcSuiteInfoJson.get("start").toString(), "yyyy-MM-dd HH:mm:ss"));
				} else {
					efcInsbPolicyitem.setStartdate(null);
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("end"))) {
					efcInsbPolicyitem.setEnddate(DateUtil.parse(efcSuiteInfoJson.get("end").toString(), "yyyy-MM-dd HH:mm:ss"));
				} else {
					efcInsbPolicyitem.setEnddate(null);
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("discountRate"))) {
					efcInsbPolicyitem.setDiscountRate(Double.valueOf(efcSuiteInfoJson.get("discountRate").toString()));
				} else {
					efcInsbPolicyitem.setDiscountRate(null);
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("amount"))) {
					efcInsbPolicyitem.setAmount(Double.valueOf(efcSuiteInfoJson.get("amount").toString()));
				} else {
					efcInsbPolicyitem.setAmount(0.0000);
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("orgCharge"))) {
					efcInsbPolicyitem.setNoti(efcSuiteInfoJson.get("orgCharge").toString());
				} else {
					efcInsbPolicyitem.setNoti("0.0000");
				}
				efcInsbPolicyitem.setModifytime(new Date());
				insbPolicyitemService.updateById(efcInsbPolicyitem);
				updPolcyInfo += "交强险,";
			}

			INSBCarkindprice queryInsbCarkindprice = new INSBCarkindprice();
			queryInsbCarkindprice.setTaskid(taskId);
			queryInsbCarkindprice.setInscomcode(insurancecompanyid);
			queryInsbCarkindprice.setInskindtype("2");
			queryInsbCarkindprice.setInskindcode("VehicleCompulsoryIns");
			INSBCarkindprice efcInsbCarkindprice = insbCarkindpriceService.queryOne(queryInsbCarkindprice);
			if (StringUtil.isEmpty(efcInsbCarkindprice)) {// 不存在的情况
				INSBCarkindprice newInsbCarkindprice = new INSBCarkindprice();
				PropertyUtils.copyProperties(newInsbCarkindprice, initInsbCarkindprice);
				newInsbCarkindprice.setId(null);
				newInsbCarkindprice.setCreatetime(new Date());
				newInsbCarkindprice.setModifytime(new Date());
				newInsbCarkindprice.setNoti(null);
				newInsbCarkindprice.setInskindcode("VehicleCompulsoryIns");
				newInsbCarkindprice.setInskindtype("2");
				newInsbCarkindprice.setNotdeductible("0");
				newInsbCarkindprice.setPreriskkind("");
				newInsbCarkindprice.setSelecteditem("购买");
				newInsbCarkindprice.setRiskname(this.getRiskNameByRiskCode("VehicleCompulsoryIns"));
				newInsbCarkindprice.setPlankey(plantkey);
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("amount"))) {
					newInsbCarkindprice.setAmount(Double.valueOf(this.getTheRealAmout(efcSuiteInfoJson.get("amount").toString(), "1")));
				} else {
					newInsbCarkindprice.setAmount(0.0000);
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("discountRate"))) {
					newInsbCarkindprice.setDiscountRate(Double.valueOf(efcSuiteInfoJson.get("discountRate").toString()));
				} else {
					newInsbCarkindprice.setDiscountRate(StringUtil.isEmpty(SQ) ? null : StringUtil.isEmpty(SQ.get("trafficDiscountRate")) ? null : Double.valueOf(SQ.get("trafficDiscountRate")
							.toString()));
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("discountCharge"))) {
					newInsbCarkindprice.setDiscountCharge(Double.valueOf(efcSuiteInfoJson.get("discountCharge").toString()));
				} else {
					newInsbCarkindprice.setDiscountCharge(StringUtil.isEmpty(SQ) ? null : StringUtil.isEmpty(SQ.get("efcCharge")) ? null : Double.valueOf(SQ.get("efcCharge").toString()));
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("orgCharge"))) {
					newInsbCarkindprice.setAmountprice(Double.valueOf(efcSuiteInfoJson.get("orgCharge").toString()));
				} else {
					newInsbCarkindprice.setAmountprice(null);
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("noTaxPremium"))) {
					newInsbCarkindprice.setNoTaxPremium((Double.valueOf(efcSuiteInfoJson.get("noTaxPremium").toString())));
				} else {
					newInsbCarkindprice.setNoTaxPremium(null);
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("tax"))) {
					newInsbCarkindprice.setTax(Double.valueOf(efcSuiteInfoJson.get("tax").toString()));
				} else {
					newInsbCarkindprice.setTax(null);
				}
				insbCarkindpriceService.insert(newInsbCarkindprice);
				addCarkind += "交强险,";
			} else {
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("amount"))) {
					efcInsbCarkindprice.setAmount(Double.valueOf(this.getTheRealAmout(efcSuiteInfoJson.get("amount").toString(), "1")));
				} else {
					efcInsbCarkindprice.setAmount(0.0000);
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("discountRate"))) {
					efcInsbCarkindprice.setDiscountRate(Double.valueOf(efcSuiteInfoJson.get("discountRate").toString()));
				} else {
					efcInsbCarkindprice.setDiscountRate(null);
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("discountCharge"))) {
					efcInsbCarkindprice.setDiscountCharge(Double.valueOf(efcSuiteInfoJson.get("discountCharge").toString()));
				} else {
					efcInsbCarkindprice.setDiscountCharge(null);
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("orgCharge"))) {
					efcInsbCarkindprice.setAmountprice(Double.valueOf(efcSuiteInfoJson.get("orgCharge").toString()));
				} else {
					efcInsbCarkindprice.setAmountprice(null);
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("noTaxPremium"))) {
					efcInsbCarkindprice.setNoTaxPremium((Double.valueOf(efcSuiteInfoJson.get("noTaxPremium").toString())));
				} else {
					efcInsbCarkindprice.setNoTaxPremium(null);
				}
				if (!StringUtil.isEmpty(efcSuiteInfoJson.get("tax"))) {
					efcInsbCarkindprice.setTax(Double.valueOf(efcSuiteInfoJson.get("tax").toString()));
				} else {
					efcInsbCarkindprice.setTax(null);
				}
				efcInsbCarkindprice.setModifytime(new Date());
				insbCarkindpriceService.updateById(efcInsbCarkindprice);
				updCarkind += "交强险,";
			}
		} else {// 没有回写交强险
			for (INSBPolicyitem delInsbPolicyitem : initInsbPolicyitemList) {
				if ("1".equals(delInsbPolicyitem.getRisktype())) {
					insbPolicyitemService.deleteById(delInsbPolicyitem.getId());
					INSBCarkindprice delInsbCarkindprice = new INSBCarkindprice();
					delInsbCarkindprice.setTaskid(taskId);
					delInsbCarkindprice.setInscomcode(insurancecompanyid);
					delInsbCarkindprice.setInskindcode("VehicleCompulsoryIns");
					delInsbCarkindprice = insbCarkindpriceService.queryOne(delInsbCarkindprice);
					if (!StringUtil.isEmpty(delInsbCarkindprice)) {
						insbCarkindpriceService.deleteById(delInsbCarkindprice.getId());
					}
					delPolcyInfo += "交强险,";
					delCarkind += "交强险,";
				}
			}
		}
		/* 判断有没有车船税 */
		if (!StringUtil.isEmpty(baseSuiteInfo.get("taxSuiteInfo"))) {
			newCarkind += "车船税,";
			taxSuiteInfo = JSONObject.fromObject(baseSuiteInfo.get("taxSuiteInfo"));
			INSBCarkindprice queryInsbCarkindprice = new INSBCarkindprice();
			queryInsbCarkindprice.setTaskid(taskId);
			queryInsbCarkindprice.setInscomcode(insurancecompanyid);
			queryInsbCarkindprice.setInskindtype("3");
			queryInsbCarkindprice.setInskindcode("VehicleTax");
			INSBCarkindprice taxInsbCarkindprice = insbCarkindpriceService.queryOne(queryInsbCarkindprice);
			if (StringUtil.isEmpty(taxInsbCarkindprice)) {// 不存在的情况
				INSBCarkindprice newInsbCarkindprice = new INSBCarkindprice();
				PropertyUtils.copyProperties(newInsbCarkindprice, initInsbCarkindprice);
				newInsbCarkindprice.setId(null);
				newInsbCarkindprice.setCreatetime(new Date());
				newInsbCarkindprice.setModifytime(new Date());
				newInsbCarkindprice.setNoti(null);
				newInsbCarkindprice.setInskindcode("VehicleTax");
				newInsbCarkindprice.setInskindtype("3");
				newInsbCarkindprice.setNotdeductible("0");
				newInsbCarkindprice.setPreriskkind("");
				newInsbCarkindprice.setSelecteditem("代缴");
				newInsbCarkindprice.setRiskname(this.getRiskNameByRiskCode("VehicleTax"));
				newInsbCarkindprice.setPlankey(plantkey);
				if (!StringUtil.isEmpty(taxSuiteInfo.get("amount"))) {
					newInsbCarkindprice.setAmount(Double.valueOf(this.getTheRealAmout(taxSuiteInfo.get("amount").toString(), "1")));
				} else {
					newInsbCarkindprice.setAmount(0.0000);
				}
				if (!StringUtil.isEmpty(taxSuiteInfo.get("discountRate"))) {
					newInsbCarkindprice.setDiscountRate(Double.valueOf(taxSuiteInfo.get("discountRate").toString()));
				} else {
					newInsbCarkindprice.setDiscountRate(null);
				}
				if (!StringUtil.isEmpty(taxSuiteInfo.get("charge"))) {
					newInsbCarkindprice.setDiscountCharge(Double.valueOf(taxSuiteInfo.get("charge").toString()));
				} else {
					newInsbCarkindprice.setDiscountCharge(null);
				}
				if (!StringUtil.isEmpty(taxSuiteInfo.get("orgCharge"))) {
					newInsbCarkindprice.setAmountprice(Double.valueOf(taxSuiteInfo.get("orgCharge").toString()));
				} else {
					newInsbCarkindprice.setAmountprice(null);
				}
				if (!StringUtil.isEmpty(taxSuiteInfo.get("noTaxPremium"))) {
					newInsbCarkindprice.setNoTaxPremium((Double.valueOf(taxSuiteInfo.get("noTaxPremium").toString())));
				} else {
					newInsbCarkindprice.setNoTaxPremium(null);
				}
				if (!StringUtil.isEmpty(taxSuiteInfo.get("tax"))) {
					newInsbCarkindprice.setTax(Double.valueOf(taxSuiteInfo.get("tax").toString()));
				} else {
					newInsbCarkindprice.setTax(null);
				}
				insbCarkindpriceService.insert(newInsbCarkindprice);
				addCarkind += "车船税,";
			} else {
				if (!StringUtil.isEmpty(taxSuiteInfo.get("amount"))) {
					taxInsbCarkindprice.setAmount(Double.valueOf(this.getTheRealAmout(taxSuiteInfo.get("amount").toString(), "1")));
				} else {
					taxInsbCarkindprice.setAmount(0.0000);
				}
				if (!StringUtil.isEmpty(taxSuiteInfo.get("discountRate"))) {
					taxInsbCarkindprice.setDiscountRate(Double.valueOf(taxSuiteInfo.get("discountRate").toString()));
				} else {
					taxInsbCarkindprice.setDiscountRate(null);
				}
				if (!StringUtil.isEmpty(taxSuiteInfo.get("charge"))) {
					taxInsbCarkindprice.setDiscountCharge(Double.valueOf(taxSuiteInfo.get("charge").toString()));
				} else {
					taxInsbCarkindprice.setDiscountCharge(null);
				}
				if (!StringUtil.isEmpty(taxSuiteInfo.get("orgCharge"))) {
					taxInsbCarkindprice.setAmountprice(Double.valueOf(taxSuiteInfo.get("orgCharge").toString()));
				} else {
					taxInsbCarkindprice.setAmountprice(null);
				}
				if (!StringUtil.isEmpty(taxSuiteInfo.get("noTaxPremium"))) {
					taxInsbCarkindprice.setNoTaxPremium((Double.valueOf(taxSuiteInfo.get("noTaxPremium").toString())));
				} else {
					taxInsbCarkindprice.setNoTaxPremium(null);
				}
				if (!StringUtil.isEmpty(taxSuiteInfo.get("tax"))) {
					taxInsbCarkindprice.setTax(Double.valueOf(taxSuiteInfo.get("tax").toString()));
				} else {
					taxInsbCarkindprice.setTax(null);
				}
				taxInsbCarkindprice.setModifytime(new Date());
				insbCarkindpriceService.updateById(taxInsbCarkindprice);
				updCarkind += "车船税,";
			}
			if (!StringUtil.isEmpty(taxSuiteInfo.get("delayCharge")) && (new BigDecimal(taxSuiteInfo.get("delayCharge").toString()).compareTo(new BigDecimal("0")) != 0)) {
				INSBCarkindprice queryInsbCarkindprice1 = new INSBCarkindprice();
				queryInsbCarkindprice1.setTaskid(taskId);
				queryInsbCarkindprice1.setInscomcode(insurancecompanyid);
				queryInsbCarkindprice1.setInskindtype("3");
				queryInsbCarkindprice1.setInskindcode("VehicleTaxOverdueFine");
				INSBCarkindprice taxInsbCarkindprice1 = insbCarkindpriceService.queryOne(queryInsbCarkindprice1);
				if (StringUtil.isEmpty(taxInsbCarkindprice1)) {// 不存在的情况
					INSBCarkindprice newInsbCarkindprice = new INSBCarkindprice();
					PropertyUtils.copyProperties(newInsbCarkindprice, initInsbCarkindprice);
					newInsbCarkindprice.setId(null);
					newInsbCarkindprice.setCreatetime(new Date());
					newInsbCarkindprice.setModifytime(new Date());
					newInsbCarkindprice.setNoti(null);
					newInsbCarkindprice.setInskindcode("VehicleTaxOverdueFine");
					newInsbCarkindprice.setInskindtype("3");
					newInsbCarkindprice.setNotdeductible("0");
					newInsbCarkindprice.setPreriskkind("");
					newInsbCarkindprice.setSelecteditem("代缴");
					newInsbCarkindprice.setRiskname(this.getRiskNameByRiskCode("VehicleTaxOverdueFine"));
					newInsbCarkindprice.setPlankey(plantkey);
					if (!StringUtil.isEmpty(taxSuiteInfo.get("amount"))) {
						newInsbCarkindprice.setAmount(Double.valueOf(this.getTheRealAmout(taxSuiteInfo.get("amount").toString(), "1")));
					} else {
						newInsbCarkindprice.setAmount(0.0000);
					}
					if (!StringUtil.isEmpty(taxSuiteInfo.get("discountRate"))) {
						newInsbCarkindprice.setDiscountRate(Double.valueOf(taxSuiteInfo.get("discountRate").toString()));
					} else {
						newInsbCarkindprice.setDiscountRate(null);
					}
					if (!StringUtil.isEmpty(taxSuiteInfo.get("delayCharge"))) {
						newInsbCarkindprice.setDiscountCharge(Double.valueOf(taxSuiteInfo.get("delayCharge").toString()));
					} else {
						newInsbCarkindprice.setDiscountCharge(null);
					}
					if (!StringUtil.isEmpty(taxSuiteInfo.get("orgCharge"))) {
						newInsbCarkindprice.setAmountprice(Double.valueOf(taxSuiteInfo.get("orgCharge").toString()));
					} else {
						newInsbCarkindprice.setAmountprice(null);
					}
					if (!StringUtil.isEmpty(taxSuiteInfo.get("noTaxPremium"))) {
						newInsbCarkindprice.setNoTaxPremium((Double.valueOf(taxSuiteInfo.get("noTaxPremium").toString())));
					} else {
						newInsbCarkindprice.setNoTaxPremium(null);
					}
					if (!StringUtil.isEmpty(taxSuiteInfo.get("tax"))) {
						newInsbCarkindprice.setTax(Double.valueOf(taxSuiteInfo.get("tax").toString()));
					} else {
						newInsbCarkindprice.setTax(null);
					}
					insbCarkindpriceService.insert(newInsbCarkindprice);
					addCarkind += "车船税滞纳金,";
					newCarkind += "车船税滞纳金,";
				} else {
					if (!StringUtil.isEmpty(taxSuiteInfo.get("amount"))) {
						taxInsbCarkindprice1.setAmount(Double.valueOf(this.getTheRealAmout(taxSuiteInfo.get("amount").toString(), "1")));
					} else {
						taxInsbCarkindprice1.setAmount(0.0000);
					}
					if (!StringUtil.isEmpty(taxSuiteInfo.get("discountRate"))) {
						taxInsbCarkindprice1.setDiscountRate(Double.valueOf(taxSuiteInfo.get("discountRate").toString()));
					} else {
						taxInsbCarkindprice1.setDiscountRate(null);
					}
					if (!StringUtil.isEmpty(taxSuiteInfo.get("delayCharge"))) {
						taxInsbCarkindprice1.setDiscountCharge(Double.valueOf(taxSuiteInfo.get("delayCharge").toString()));
					} else {
						taxInsbCarkindprice1.setDiscountCharge(null);
					}
					if (!StringUtil.isEmpty(taxSuiteInfo.get("orgCharge"))) {
						taxInsbCarkindprice1.setAmountprice(Double.valueOf(taxSuiteInfo.get("orgCharge").toString()));
					} else {
						taxInsbCarkindprice1.setAmountprice(null);
					}
					if (!StringUtil.isEmpty(taxSuiteInfo.get("noTaxPremium"))) {
						taxInsbCarkindprice1.setNoTaxPremium((Double.valueOf(taxSuiteInfo.get("noTaxPremium").toString())));
					} else {
						taxInsbCarkindprice1.setNoTaxPremium(null);
					}
					if (!StringUtil.isEmpty(taxSuiteInfo.get("tax"))) {
						taxInsbCarkindprice1.setTax(Double.valueOf(taxSuiteInfo.get("tax").toString()));
					} else {
						taxInsbCarkindprice1.setTax(null);
					}
					taxInsbCarkindprice1.setModifytime(new Date());
					insbCarkindpriceService.updateById(taxInsbCarkindprice1);
					updCarkind += "车船税滞纳金,";
				}
			} else {
				INSBCarkindprice delInsbCarkindprice1 = new INSBCarkindprice();
				delInsbCarkindprice1.setInskindcode("VehicleTaxOverdueFine");
				delInsbCarkindprice1.setTaskid(taskId);
				delInsbCarkindprice1.setInscomcode(insurancecompanyid);
				delInsbCarkindprice1 = insbCarkindpriceService.queryOne(delInsbCarkindprice1);
				if (!StringUtil.isEmpty(delInsbCarkindprice1)) {
					insbCarkindpriceService.deleteById(delInsbCarkindprice1.getId());
					delCarkind += "车船税滞纳金,";
				}
			}
		} else {
			INSBCarkindprice delInsbCarkindprice = new INSBCarkindprice();
			delInsbCarkindprice.setInskindcode("VehicleTax");
			delInsbCarkindprice.setTaskid(taskId);
			delInsbCarkindprice.setInscomcode(insurancecompanyid);
			delInsbCarkindprice = insbCarkindpriceService.queryOne(delInsbCarkindprice);
			if (!StringUtil.isEmpty(delInsbCarkindprice)) {
				insbCarkindpriceService.deleteById(delInsbCarkindprice.getId());
				delCarkind += "车船税,";
			}
			INSBCarkindprice delInsbCarkindprice1 = new INSBCarkindprice();
			delInsbCarkindprice1.setInskindcode("VehicleTaxOverdueFine");
			delInsbCarkindprice1.setTaskid(taskId);
			delInsbCarkindprice1.setInscomcode(insurancecompanyid);
			delInsbCarkindprice1 = insbCarkindpriceService.queryOne(delInsbCarkindprice1);
			if (!StringUtil.isEmpty(delInsbCarkindprice1)) {
				insbCarkindpriceService.deleteById(delInsbCarkindprice1.getId());
				delCarkind += "车船税滞纳金,";
			}
		}
		/* 判断有没有商业险 */
		if (!StringUtil.isEmpty(baseSuiteInfo.get("bizSuiteInfo"))) {
			newPolcyInfo += "商业险,";
			bizSuiteInfoJson = JSONObject.fromObject(baseSuiteInfo.get("bizSuiteInfo"));
			INSBPolicyitem queryInsbPolicyitem = new INSBPolicyitem();
			queryInsbPolicyitem.setTaskid(taskId);
			queryInsbPolicyitem.setInscomcode(insurancecompanyid);
			queryInsbPolicyitem.setRisktype("0");
			INSBPolicyitem bizInsbPolicyitem = insbPolicyitemService.queryOne(queryInsbPolicyitem);
			if (StringUtil.isEmpty(bizInsbPolicyitem)) {// 没有查到，则新建一个
				INSBPolicyitem newInsbPolicyitem = new INSBPolicyitem();
				PropertyUtils.copyProperties(newInsbPolicyitem, initInsbPolicyitem);
				newInsbPolicyitem.setId(null);
				newInsbPolicyitem.setCreatetime(new Date());
				newInsbPolicyitem.setModifytime(new Date());
				newInsbPolicyitem.setRisktype("0");
				newInsbPolicyitem.setPaynum(null);
				newInsbPolicyitem.setCheckcode(StringUtil.isEmpty(SQ) ? null : StringUtil.isEmpty(SQ.get("bizTempId")) ? null : SQ.get("bizTempId").toString());
				newInsbPolicyitem.setProposalformno(StringUtil.isEmpty(SQ) ? null : StringUtil.isEmpty(SQ.get("bizProposeNum")) ? null : SQ.get("bizProposeNum").toString());
				newInsbPolicyitem.setPolicyno(StringUtil.isEmpty(SQ) ? null : StringUtil.isEmpty(SQ.get("bizPolicyCode")) ? null : SQ.get("bizPolicyCode").toString());
				newInsbPolicyitem.setTotalepremium(Double.valueOf(StringUtil.isEmpty(SQ) ? "0.00" : StringUtil.isEmpty(SQ.get("totalCharge")) ? "0.00" : SQ.get("totalCharge").toString()));
				newInsbPolicyitem.setPremium(Double.valueOf(StringUtil.isEmpty(SQ) ? "0.00" : StringUtil.isEmpty(SQ.get("bizCharge")) ? "0.00" : SQ.get("bizCharge").toString()));
				newInsbPolicyitem.setDiscountCharge(Double.valueOf(StringUtil.isEmpty(SQ) ? "0.00" : StringUtil.isEmpty(SQ.get("bizCharge")) ? "0.00" : SQ.get("bizCharge").toString()));
				newInsbPolicyitem.setDiscountRate(Double.valueOf(StringUtil.isEmpty(SQ) ? "0.00" : StringUtil.isEmpty(SQ.get("bussDiscountRate")) ? "0.00" : SQ.get("bussDiscountRate").toString()));
				newInsbPolicyitem.setPolicystatus(policyStatus);
				if (!StringUtil.isEmpty(bizSuiteInfoJson.get("discountCharge"))) {
					newInsbPolicyitem.setPremium(Double.valueOf(bizSuiteInfoJson.get("discountCharge").toString()));
				} else {
					newInsbPolicyitem.setPremium(null);
				}
				if (!StringUtil.isEmpty(bizSuiteInfoJson.get("start"))) {
					newInsbPolicyitem.setStartdate(DateUtil.parse(bizSuiteInfoJson.get("start").toString(), "yyyy-MM-dd HH:mm:ss"));
				} else {
					newInsbPolicyitem.setStartdate(null);
				}
				if (!StringUtil.isEmpty(bizSuiteInfoJson.get("end"))) {
					newInsbPolicyitem.setEnddate(DateUtil.parse(bizSuiteInfoJson.get("end").toString(), "yyyy-MM-dd HH:mm:ss"));
				} else {
					newInsbPolicyitem.setEnddate(null);
				}
				if (!StringUtil.isEmpty(bizSuiteInfoJson.get("discountRate"))) {
					newInsbPolicyitem.setDiscountRate(Double.valueOf(bizSuiteInfoJson.get("discountRate").toString()));
				} else {
					newInsbPolicyitem.setDiscountRate(null);
				}
				if (!StringUtil.isEmpty(bizSuiteInfoJson.get("amount"))) {
					newInsbPolicyitem.setAmount(Double.valueOf(bizSuiteInfoJson.get("amount").toString()));
				} else {
					newInsbPolicyitem.setAmount(0.0000);
				}
				if (!StringUtil.isEmpty(bizSuiteInfoJson.get("orgCharge"))) {
					newInsbPolicyitem.setNoti(bizSuiteInfoJson.get("orgCharge").toString());
				} else {
					newInsbPolicyitem.setNoti("0.0000");
				}
				insbPolicyitemService.insert(newInsbPolicyitem);
				addPolcyInfo += "商业险,";
			} else {
				bizInsbPolicyitem.setCheckcode(StringUtil.isEmpty(SQ) ? null : StringUtil.isEmpty(SQ.get("bizTempId")) ? null : SQ.get("bizTempId").toString());
				bizInsbPolicyitem.setProposalformno(StringUtil.isEmpty(SQ) ? null : StringUtil.isEmpty(SQ.get("bizProposeNum")) ? null : SQ.get("bizProposeNum").toString());
				bizInsbPolicyitem.setPolicyno(StringUtil.isEmpty(SQ) ? null : StringUtil.isEmpty(SQ.get("bizPolicyCode")) ? null : SQ.get("bizPolicyCode").toString());
				bizInsbPolicyitem.setTotalepremium(Double.valueOf(StringUtil.isEmpty(SQ) ? "0.00" : StringUtil.isEmpty(SQ.get("totalCharge")) ? "0.00" : SQ.get("totalCharge").toString()));
				bizInsbPolicyitem.setPremium(Double.valueOf(StringUtil.isEmpty(SQ) ? "0.00" : StringUtil.isEmpty(SQ.get("bizCharge")) ? "0.00" : SQ.get("bizCharge").toString()));
				bizInsbPolicyitem.setDiscountCharge(Double.valueOf(StringUtil.isEmpty(SQ) ? "0.00" : StringUtil.isEmpty(SQ.get("bizCharge")) ? "0.00" : SQ.get("bizCharge").toString()));
				bizInsbPolicyitem.setDiscountRate(Double.valueOf(StringUtil.isEmpty(SQ) ? "0.00" : StringUtil.isEmpty(SQ.get("bussDiscountRate")) ? "0.00" : SQ.get("bussDiscountRate").toString()));
				bizInsbPolicyitem.setPolicystatus(policyStatus);
				if (!StringUtil.isEmpty(bizSuiteInfoJson.get("discountCharge"))) {
					bizInsbPolicyitem.setPremium(Double.valueOf(bizSuiteInfoJson.get("discountCharge").toString()));
				} else {
					bizInsbPolicyitem.setPremium(null);
				}
				if (!StringUtil.isEmpty(bizSuiteInfoJson.get("start"))) {
					bizInsbPolicyitem.setStartdate(DateUtil.parse(bizSuiteInfoJson.get("start").toString(), "yyyy-MM-dd HH:mm:ss"));
				} else {
					bizInsbPolicyitem.setStartdate(null);
				}
				if (!StringUtil.isEmpty(bizSuiteInfoJson.get("end"))) {
					bizInsbPolicyitem.setEnddate(DateUtil.parse(bizSuiteInfoJson.get("end").toString(), "yyyy-MM-dd HH:mm:ss"));
				} else {
					bizInsbPolicyitem.setEnddate(null);
				}
				if (!StringUtil.isEmpty(bizSuiteInfoJson.get("discountRate"))) {
					bizInsbPolicyitem.setDiscountRate(Double.valueOf(bizSuiteInfoJson.get("discountRate").toString()));
				} else {
					bizInsbPolicyitem.setDiscountRate(null);
				}
				if (!StringUtil.isEmpty(bizSuiteInfoJson.get("amount"))) {
					bizInsbPolicyitem.setAmount(Double.valueOf(bizSuiteInfoJson.get("amount").toString()));
				} else {
					bizInsbPolicyitem.setAmount(0.0000);
				}
				if (!StringUtil.isEmpty(bizSuiteInfoJson.get("orgCharge"))) {
					bizInsbPolicyitem.setNoti(bizSuiteInfoJson.get("orgCharge").toString());
				} else {
					bizInsbPolicyitem.setNoti("0.0000");
				}
				bizInsbPolicyitem.setModifytime(new Date());
				insbPolicyitemService.updateById(bizInsbPolicyitem);
				updPolcyInfo += "商业险,";
			}
		} else {
			for (INSBPolicyitem delInsbPolicyitem : initInsbPolicyitemList) {
				if ("0".equals(delInsbPolicyitem.getRisktype())) {
					insbPolicyitemService.deleteById(delInsbPolicyitem.getId());
					delPolcyInfo += "商业险,";
				}
			}
		}

		//是否回写了商业险
		boolean insuredSy = false;

		/* 开始处理险别信息 */
		if (bizSuiteInfoJson != null && !StringUtil.isEmpty(bizSuiteInfoJson.get("suites"))) {
			suiteDefListJson = JSONArray.fromObject(bizSuiteInfoJson.get("suites"));
			if (!StringUtil.isEmpty(suiteDefListJson) && suiteDefListJson.size() > 0) {
				insuredSy = true;

				if (specialRisk != null && !StringUtil.isEmpty(specialRisk.get("suites"))) {
					JSONArray specialRiskListJson = JSONArray.fromObject(specialRisk.get("suites"));
					suiteDefListJson.addAll(specialRiskListJson);
				}

				for (Object obj : suiteDefListJson) {
					suiteDefJson = JSONObject.fromObject(obj);
					String code = (String)suiteDefJson.get("code");
					if ("VehicleTax".equals(code) || "VehicleCompulsoryIns".equals(code)) {
						continue;
					}else if(null==code){
						LogUtil.info("taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",回写险种编码为空：" + code);
						break;
					}
					String insKindType = this.getInRiskTypeByRiskCode(code);
					if (StringUtil.isEmpty(insKindType)) {
						LogUtil.info("taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",回写险种编码错误：" + code);
						break;
					}
					INSBCarkindprice ncfCarkindprice = new INSBCarkindprice();
					ncfCarkindprice.setTaskid(taskId);
					ncfCarkindprice.setInscomcode(insurancecompanyid);
					ncfCarkindprice.setInskindcode(code);
					ncfCarkindprice = insbCarkindpriceService.queryOne(ncfCarkindprice);
					if (StringUtil.isEmpty(ncfCarkindprice)) {
						INSBCarkindprice newInsbCarkindprice = new INSBCarkindprice();
						PropertyUtils.copyProperties(newInsbCarkindprice, initInsbCarkindprice);
						newInsbCarkindprice.setId(null);
						newInsbCarkindprice.setCreatetime(new Date());
						newInsbCarkindprice.setModifytime(new Date());
						newInsbCarkindprice.setNoti(null);
						newInsbCarkindprice.setInskindcode(code);
						newInsbCarkindprice.setInskindtype(insKindType);
						newInsbCarkindprice.setNotdeductible("0".equals(insKindType) ? "1" : "0");
						newInsbCarkindprice.setPreriskkind(this.getParentRiskNameByRiskCode(code));
						if ("0".equals(insKindType)) {
							newInsbCarkindprice.setSelecteditem(this.getSelectItem(code, suiteDefJson.get("amount").toString()));
						} else {
							newInsbCarkindprice.setSelecteditem(null);
						}
						newInsbCarkindprice.setRiskname(this.getRiskNameByRiskCode(code));
						newInsbCarkindprice.setPlankey(plantkey);
						if ("GlassIns".equalsIgnoreCase(newInsbCarkindprice.getInskindcode()) || "MirrorLightIns".equalsIgnoreCase(newInsbCarkindprice.getInskindcode())) {
							if (!StringUtil.isEmpty(suiteDefJson.get("amount"))) {
								newInsbCarkindprice.setAmount(new BigDecimal(suiteDefJson.get("amount").toString()).add(new BigDecimal(1)).doubleValue());// 保额
							} else {
								newInsbCarkindprice.setAmount(0.0000);
							}
						} else {
							if (!StringUtil.isEmpty(suiteDefJson.get("amount"))) {
								newInsbCarkindprice.setAmount(Double.valueOf(this.getTheRealAmout(suiteDefJson.get("amount").toString(), insKindType)));// 保额
							} else {
								newInsbCarkindprice.setAmount(0.0000);
							}
						}
						if (!StringUtil.isEmpty(suiteDefJson.get("amountDesc"))) {
							newInsbCarkindprice.setAmountDesc(suiteDefJson.get("amountDesc").toString());// 保额描述
						} else {
							newInsbCarkindprice.setAmountDesc(null);
						}
						if (!StringUtil.isEmpty(suiteDefJson.get("orgCharge"))) {
							newInsbCarkindprice.setAmountprice(Double.valueOf(suiteDefJson.get("orgCharge").toString()));// 原始保费
						} else {
							newInsbCarkindprice.setAmountprice(null);
						}
						if (!StringUtil.isEmpty(suiteDefJson.get("discountCharge"))) {
							newInsbCarkindprice.setDiscountCharge(Double.valueOf(suiteDefJson.get("discountCharge").toString()));// 折后保费
						} else {
							newInsbCarkindprice.setDiscountCharge(null);
						}
						if (!StringUtil.isEmpty(suiteDefJson.get("discountRate"))) {
							newInsbCarkindprice.setDiscountRate(Double.valueOf(suiteDefJson.get("discountRate").toString()));// 折扣率
						} else {
							newInsbCarkindprice.setDiscountRate(null);
						}
						if (!StringUtil.isEmpty(suiteDefJson.get("noTaxPremium"))) {
							newInsbCarkindprice.setNoTaxPremium((Double.valueOf(suiteDefJson.get("noTaxPremium").toString())));
						} else {
							newInsbCarkindprice.setNoTaxPremium(null);
						}
						if (!StringUtil.isEmpty(suiteDefJson.get("tax"))) {
							newInsbCarkindprice.setTax(Double.valueOf(suiteDefJson.get("tax").toString()));
						} else {
							newInsbCarkindprice.setTax(null);
						}
						insbCarkindpriceService.insert(newInsbCarkindprice);
						addCarkind += this.getRiskNameByRiskCode(code) + ",";
						newCarkInd.add(newInsbCarkindprice);
					} else {
						if (!StringUtil.isEmpty(suiteDefJson.get("amountDesc"))) {
							ncfCarkindprice.setAmountDesc(suiteDefJson.get("amountDesc").toString());// 保额描述
						} else {
							ncfCarkindprice.setAmountDesc(null);
						}
						if ("GlassIns".equalsIgnoreCase(ncfCarkindprice.getInskindcode()) || "MirrorLightIns".equalsIgnoreCase(ncfCarkindprice.getInskindcode())) {
							if (!StringUtil.isEmpty(suiteDefJson.get("amount"))) {
								ncfCarkindprice.setAmount(new BigDecimal(suiteDefJson.get("amount").toString()).add(new BigDecimal(1)).doubleValue());// 保额
							} else {
								ncfCarkindprice.setAmount(0.0000);
							}
						} else {
							if (!StringUtil.isEmpty(suiteDefJson.get("amount"))) {
								ncfCarkindprice.setAmount(Double.valueOf(this.getTheRealAmout(suiteDefJson.get("amount").toString(), insKindType)));// 保额
							} else {
								ncfCarkindprice.setAmount(0.0000);
							}
						}
						if (!StringUtil.isEmpty(suiteDefJson.get("orgCharge"))) {
							ncfCarkindprice.setAmountprice(Double.valueOf(suiteDefJson.get("orgCharge").toString()));// 原始保费
						} else {
							ncfCarkindprice.setAmountprice(null);
						}
						if (!StringUtil.isEmpty(suiteDefJson.get("discountCharge"))) {
							ncfCarkindprice.setDiscountCharge(Double.valueOf(suiteDefJson.get("discountCharge").toString()));// 折后保费
						} else {
							ncfCarkindprice.setDiscountCharge(null);
						}
						if (!StringUtil.isEmpty(suiteDefJson.get("discountRate"))) {
							ncfCarkindprice.setDiscountRate(Double.valueOf(suiteDefJson.get("discountRate").toString()));// 折扣率
						} else {
							ncfCarkindprice.setDiscountRate(null);
						}
						ncfCarkindprice.setModifytime(new Date());
						if ("0".equals(ncfCarkindprice.getInskindtype())) {
							ncfCarkindprice.setSelecteditem(this.getSelectItem(code, suiteDefJson.get("amount").toString()));
						}
						if (!StringUtil.isEmpty(suiteDefJson.get("noTaxPremium"))) {
							ncfCarkindprice.setNoTaxPremium((Double.valueOf(suiteDefJson.get("noTaxPremium").toString())));
						} else {
							ncfCarkindprice.setNoTaxPremium(null);
						}
						if (!StringUtil.isEmpty(suiteDefJson.get("tax"))) {
							ncfCarkindprice.setTax(Double.valueOf(suiteDefJson.get("tax").toString()));
						} else {
							ncfCarkindprice.setTax(null);
						}
						insbCarkindpriceService.updateById(ncfCarkindprice);
						updCarkind += this.getRiskNameByRiskCode(code) + ",";
						newCarkInd.add(ncfCarkindprice);
					}
				}
			}
		}
		List<INSBCarkindprice> delList = new ArrayList<INSBCarkindprice>();
		delList.addAll(oldCarkInd);
		for (INSBCarkindprice old : oldCarkInd) {
			for (INSBCarkindprice newcarkind : newCarkInd) {
				if (old.getInskindcode().equals(newcarkind.getInskindcode())) {
					delList.remove(old);
				}
			}
		}
		for (INSBCarkindprice del : delList) {
			insbCarkindpriceService.deleteById(del.getId());
			delCarkind += del.getRiskname() + ",";
		}

		INSBPolicyitem queryInsbPolicyitem = new INSBPolicyitem();
		queryInsbPolicyitem.setTaskid(taskId);
		queryInsbPolicyitem.setInscomcode(insurancecompanyid);
		List<INSBPolicyitem> dateInsbPolicyitems = insbPolicyitemService.queryList(queryInsbPolicyitem);

		INSBCarowneinfo queryInsbCarowneinfo = new INSBCarowneinfo();
		queryInsbCarowneinfo.setTaskid(taskId);
		queryInsbCarowneinfo = insbCarowneinfoService.queryOne(queryInsbCarowneinfo);
		if (!StringUtil.isEmpty(queryInsbCarowneinfo)) {
			INSBPerson dateInsbPerson = new INSBPerson();
			dateInsbPerson = insbPersonService.queryById(queryInsbCarowneinfo.getPersonid());
			if (!StringUtil.isEmpty(dateInsbPerson)) {
				for (INSBPolicyitem ddInsbPolicyitem : dateInsbPolicyitems) {
					ddInsbPolicyitem.setCarownerid(queryInsbCarowneinfo.getId());
					ddInsbPolicyitem.setCarownername(dateInsbPerson.getName());
					insbPolicyitemService.updateById(ddInsbPolicyitem);
				}
			}
		}

		INSBInsuredhis dataInsbInsuredhis = new INSBInsuredhis();
		dataInsbInsuredhis.setTaskid(taskId);
		dataInsbInsuredhis.setInscomcode(insurancecompanyid);
		dataInsbInsuredhis = insbInsuredhisService.queryOne(dataInsbInsuredhis);
		if (!StringUtil.isEmpty(dataInsbInsuredhis)) {
			INSBPerson dateInsbPerson = new INSBPerson();
			dateInsbPerson = insbPersonService.queryById(dataInsbInsuredhis.getPersonid());
			if (!StringUtil.isEmpty(dateInsbPerson)) {
				for (INSBPolicyitem ddInsbPolicyitem : dateInsbPolicyitems) {
					ddInsbPolicyitem.setInsuredid(dataInsbInsuredhis.getId());
					ddInsbPolicyitem.setInsuredname(dateInsbPerson.getName());
					insbPolicyitemService.updateById(ddInsbPolicyitem);
				}
			}
		}

		INSBApplicanthis queryInsbApplicanthis = new INSBApplicanthis();
		queryInsbApplicanthis.setTaskid(taskId);
		queryInsbApplicanthis.setInscomcode(insurancecompanyid);
		queryInsbApplicanthis = insbApplicanthisService.queryOne(queryInsbApplicanthis);
		if (!StringUtil.isEmpty(queryInsbApplicanthis)) {
			INSBPerson dateInsbPerson = new INSBPerson();
			dateInsbPerson = insbPersonService.queryById(queryInsbApplicanthis.getPersonid());
			if (!StringUtil.isEmpty(dateInsbPerson)) {
				for (INSBPolicyitem ddInsbPolicyitem : dateInsbPolicyitems) {
					ddInsbPolicyitem.setApplicantid(queryInsbApplicanthis.getId());
					ddInsbPolicyitem.setApplicantname(dateInsbPerson.getName());
					insbPolicyitemService.updateById(ddInsbPolicyitem);
				}
			}
		}

		INSBQuotetotalinfo queryInsbQuotetotalinfo = new INSBQuotetotalinfo();
		queryInsbQuotetotalinfo.setTaskid(taskId);
		queryInsbQuotetotalinfo = insbQuotetotalinfoService.queryOne(queryInsbQuotetotalinfo);
		queryInsbQuotetotalinfo.setModifytime(new Date());
		insbQuotetotalinfoService.updateById(queryInsbQuotetotalinfo);
		INSBQuoteinfo queryInsbQuoteinfo = new INSBQuoteinfo();
		queryInsbQuoteinfo.setQuotetotalinfoid(queryInsbQuotetotalinfo.getId());
		queryInsbQuoteinfo.setInscomcode(insurancecompanyid);
		queryInsbQuoteinfo = insbQuoteinfoService.queryOne(queryInsbQuoteinfo);
		queryInsbQuoteinfo.setModifytime(new Date());
		if (!StringUtil.isEmpty(SQ.get("totalCharge"))) {
			queryInsbQuoteinfo.setQuotediscountamount(Double.valueOf(SQ.get("totalCharge").toString()));
		}
		insbQuoteinfoService.updateById(queryInsbQuoteinfo);

		//回写了商业险，将保险配置与上年一致的属性清空
		if (insuredSy) {
			insbCarinfohisDao.updateInsureconfigsameaslastyear(taskId, insurancecompanyid, null);
		}

		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "," + oldPolcyInfo + "====");
		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "," + oldCarkind + "====");
		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "," + newPolcyInfo + "====");
		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "," + newCarkind + "====");
		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "," + addPolcyInfo + "====");
		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "," + addCarkind + "====");
		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "," + delPolcyInfo + "====");
		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "," + delCarkind + "====");
		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "," + updPolcyInfo + "====");
		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "," + updCarkind + "====");
		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",保险配置信息保存完成====");

	}
	/**
	 * 根据taskid和companyid去insbapplicanthis中查,如果没有查到数据,则去insbapplicant中查
	 * 
	 */
	private void saveApplicantPersonInfo(String taskId, String insurancecompanyid, JSONObject applicantPersonInfo) throws Exception {

		INSBApplicanthis queryInsbApplicanthis = new INSBApplicanthis();
		queryInsbApplicanthis.setTaskid(taskId);
		queryInsbApplicanthis.setInscomcode(insurancecompanyid);
		queryInsbApplicanthis = insbApplicanthisService.queryOne(queryInsbApplicanthis);
		INSBPerson insbPerson = new INSBPerson();
		//没有投保人关系信息，默认无相关insbperson数据，插入数据
		if(StringUtil.isEmpty(queryInsbApplicanthis)){
			insbPerson=insbPersonHelpService.getINSBPersonObj(ConstUtil.OPER_ADD, applicantPersonInfo, taskId, insurancecompanyid,
						null,"INSBApplicant", "address",true,insbPerson);
			//insbPerson.setNoti("投保人1-"+insurancecompanyid);
			insbPersonService.insert(insbPerson);
			queryInsbApplicanthis = new INSBApplicanthis();
			queryInsbApplicanthis=insbPersonHelpService.addINSBApplicanthis(queryInsbApplicanthis,new Date(),ConstUtil.OPER_ADMIN,
					taskId,insurancecompanyid,insbPerson.getId());
		}else{
			//查询是否有关联insbPerson信息,①：没有添加，②：有更新
			insbPerson=insbPersonService.queryById(queryInsbApplicanthis.getPersonid());
			if(insbPerson==null){
				insbPerson = new INSBPerson();
				insbPerson=insbPersonHelpService.getINSBPersonObj(ConstUtil.OPER_ADD, applicantPersonInfo, taskId, insurancecompanyid,
						null,"INSBApplicant", "address",true,insbPerson);	
				//insbPerson.setNoti("投保人2-"+insurancecompanyid);
				insbPersonService.insert(insbPerson);
			}else{
				insbPerson=insbPersonHelpService.getINSBPersonObj(ConstUtil.OPER_UPDATE, applicantPersonInfo, taskId, insurancecompanyid,
						queryInsbApplicanthis.getPersonid(),"INSBApplicant", "address",true,insbPerson);
				//insbPerson.setNoti("投保人3-"+insurancecompanyid);
				insbPersonService.updateById(insbPerson);
			}
			queryInsbApplicanthis.setPersonid(insbPerson.getId());
			insbApplicanthisService.updateById(queryInsbApplicanthis);
		}
		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",投保人信息保存完成====");

	}
	/**
	 * 根据taskid和companyid去insblegalrightclaimhis中查,如果没有查到数据,
	 * 则去insblegalrightclaim中查
	 * 
	 */
	private void saveBeneficiaryPersonList(String taskId, String insurancecompanyid, JSONArray beneficiaryPersonList) throws Exception {

		INSBLegalrightclaimhis queryInsbLegalrightclaimhis = new INSBLegalrightclaimhis();
		queryInsbLegalrightclaimhis.setTaskid(taskId);
		queryInsbLegalrightclaimhis.setInscomcode(insurancecompanyid);
		/*
		List<INSBLegalrightclaimhis> resultInsbLegalrightclaimhisList = insbLegalrightclaimhisService.queryList(queryInsbLegalrightclaimhis);
		if (resultInsbLegalrightclaimhisList.size() > 0 && beneficiaryPersonList.size() > 0) {
			for (INSBLegalrightclaimhis delInsbLegalrightclaimhis : resultInsbLegalrightclaimhisList) {
				insbPersonService.deleteById(delInsbLegalrightclaimhis.getPersonid());
				insbLegalrightclaimhisService.deleteById(delInsbLegalrightclaimhis.getId());
			}
		}
		*/
		if (beneficiaryPersonList.size() > 0) {
			for (Object obj : beneficiaryPersonList) {
				JSONObject beneficiaryPerson = (JSONObject) obj;
				INSBPerson insbPerson = new INSBPerson();
				
				//可能多条权益人，所有根据受益顺序、工作流号、供应商id判断唯一
				if (!StringUtil.isEmpty(beneficiaryPerson.get("orderNum")))
					queryInsbLegalrightclaimhis.setOrderNum(Integer.valueOf(beneficiaryPerson.get("orderNum").toString()));
				queryInsbLegalrightclaimhis=insbLegalrightclaimhisService.queryOne(queryInsbLegalrightclaimhis);

				//没有受益人关系信息，默认无相关insbperson数据，插入数据
				if(StringUtil.isEmpty(queryInsbLegalrightclaimhis)){
					insbPerson=insbPersonHelpService.getINSBPersonObj(ConstUtil.OPER_ADD, beneficiaryPerson, taskId, insurancecompanyid,
							null,null, null,false,insbPerson);
					//insbPerson.setNoti("受益人1-"+insurancecompanyid);
					insbPersonService.insert(insbPerson);
					INSBLegalrightclaimhis dataInsbLegalrightclaimhis = new INSBLegalrightclaimhis();
					if (!StringUtil.isEmpty(beneficiaryPerson.get("legal")))
						dataInsbLegalrightclaimhis.setIsLegal(beneficiaryPerson.get("legal").toString().equals("true") ? "1" : "0");// 是否法定

					if (!StringUtil.isEmpty(beneficiaryPerson.get("orderNum")))
						dataInsbLegalrightclaimhis.setOrderNum(Integer.valueOf(beneficiaryPerson.get("orderNum").toString()));// 受益顺序

					if (!StringUtil.isEmpty(beneficiaryPerson.get("ratio")))
						dataInsbLegalrightclaimhis.setRatio(Double.valueOf(beneficiaryPerson.get("ratio").toString()));// 收益比例
					//insbLegalrightclaimhisService.insert(dataInsbLegalrightclaimhis);
					dataInsbLegalrightclaimhis=insbPersonHelpService.addINSBLegalrightclaimhis(dataInsbLegalrightclaimhis,new Date(),ConstUtil.OPER_ADMIN,
							taskId,insurancecompanyid,insbPerson.getId());
				}else{
					//查询是否有关联insbPerson信息,①：没有添加，②：有更新
					insbPerson=insbPersonService.queryById(queryInsbLegalrightclaimhis.getPersonid());
					if(insbPerson==null){
						insbPerson = new INSBPerson();
						insbPerson=insbPersonHelpService.getINSBPersonObj(ConstUtil.OPER_ADD, beneficiaryPerson, taskId, insurancecompanyid,
								null,null, null,false,insbPerson);		
						//insbPerson.setNoti("受益人2-"+insurancecompanyid);
						insbPersonService.insert(insbPerson);
					}else{
						insbPerson=insbPersonHelpService.getINSBPersonObj(ConstUtil.OPER_UPDATE, beneficiaryPerson, taskId, insurancecompanyid,
								queryInsbLegalrightclaimhis.getPersonid(),null, null,false,insbPerson);	
						//insbPerson.setNoti("受益人3-"+insurancecompanyid);
						insbPersonService.updateById(insbPerson);
					}
					queryInsbLegalrightclaimhis.setPersonid(insbPerson.getId());
					insbLegalrightclaimhisService.updateById(queryInsbLegalrightclaimhis);
				}	
			}
		}
		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",受益人信息保存完成====");
	}

	private void saveTrackInfoList(String taskId, String insurancecompanyid, JSONArray trackInfoList) throws Exception {

	}

	private void saveDriverPersonList(String taskId, String insurancecompanyid, JSONArray driverPersonList) {
	}

	private void saveInsuredPersonInfoList(String taskId, String insurancecompanyid, JSONArray insuredPersonInfoList) throws Exception {
		if (insuredPersonInfoList.size() == 0) {
			return;
		}
		JSONObject insuredPersonInfo = (JSONObject) insuredPersonInfoList.get(0);
		
		INSBInsuredhis dataInsbInsuredhis = new INSBInsuredhis();
		dataInsbInsuredhis.setTaskid(taskId);
		dataInsbInsuredhis.setInscomcode(insurancecompanyid);
		dataInsbInsuredhis = insbInsuredhisService.queryOne(dataInsbInsuredhis);
		INSBPerson insbPerson = new INSBPerson();
		//没有被保人关系信息，默认无相关insbperson数据，插入数据
		if(StringUtil.isEmpty(dataInsbInsuredhis)){
			
			dataInsbInsuredhis=new INSBInsuredhis();
			insbPerson=insbPersonHelpService.getINSBPersonObj(ConstUtil.OPER_ADD, insuredPersonInfo, taskId, insurancecompanyid,
						null,"INSBInsured", "address",true,insbPerson);
			//insbPerson.setNoti("被保人1-"+insurancecompanyid);
			insbPersonService.insert(insbPerson);
			dataInsbInsuredhis=insbPersonHelpService.addINSBInsuredhis(dataInsbInsuredhis,new Date(),ConstUtil.OPER_ADMIN,
					taskId,insurancecompanyid,insbPerson.getId());
		}else{
			//查询是否有关联insbPerson信息,①：没有添加，②：有更新
			insbPerson=insbPersonService.queryById(dataInsbInsuredhis.getPersonid());
			if(insbPerson==null){
				insbPerson = new INSBPerson();
				insbPerson=insbPersonHelpService.getINSBPersonObj(ConstUtil.OPER_ADD, insuredPersonInfo, taskId, insurancecompanyid,
						null,"INSBInsured", "address",true,insbPerson);		
				//insbPerson.setNoti("被保人2-"+insurancecompanyid);
				insbPersonService.insert(insbPerson);
			}else{
				insbPerson=insbPersonHelpService.getINSBPersonObj(ConstUtil.OPER_UPDATE, insuredPersonInfo, taskId, insurancecompanyid,
						dataInsbInsuredhis.getPersonid(),"INSBInsured", "address",true,insbPerson);	
				//insbPerson.setNoti("被保人3-"+insurancecompanyid);
				insbPersonService.updateById(insbPerson);
			}
			dataInsbInsuredhis.setPersonid(insbPerson.getId());
			insbInsuredhisService.updateById(dataInsbInsuredhis);
		}
		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",被保人信息保存完成====");
		//saveBeneficiaryPersonList(taskId, insurancecompanyid, insuredPersonInfoList);
	}
	
	/** 保存电子保单 **/
	public void saveElecPolicy(String taskId, String insccomcode, JSONArray elecPolicyInfoList){
		if(elecPolicyInfoList == null || elecPolicyInfoList.size() == 0){
			return;
		}
		//{"elecPolicyInfo":[{"jqElecPolicyPath":"交强险电子保单cos路径","retStatus":"true","errMsg":"retStatus=false的错误信息"},{"retStatus":"true","errMsg":"retStatus=false的错误信息","syElecPolicyPath":"商业险电子保单cos路径"}]}
		for (int i = 0; i < elecPolicyInfoList.size(); i++) {
			JSONObject elecPolicyInfoOne = (JSONObject) elecPolicyInfoList.get(i);
			if(elecPolicyInfoOne != null && elecPolicyInfoOne.containsKey("retStatus")){
				String retStatus = elecPolicyInfoOne.getString("retStatus");
				if(StringUtil.isEmpty(retStatus) || "false".equals(retStatus)){
					String errMsg = null;
					if(elecPolicyInfoOne.containsKey("errMsg")) 
						errMsg = elecPolicyInfoOne.getString("errMsg");
					//失败的原因
					LogUtil.error("====精灵返回电子保单异常：taskid=" + taskId + ",inscomcode=" + insccomcode + ", retStatus=" + retStatus + ", errMsg=" + errMsg);
				} else {
					String filePath = null;
					String type = null;
					String fileType = ConstUtil.FILE_TYPE_ELECPOLICY;
					String fileDesc = "电子保单";
					if(elecPolicyInfoOne.containsKey("jqElecPolicyPath")){
						filePath = elecPolicyInfoOne.getString("jqElecPolicyPath");
						type = "1";//交强
					}
					if(elecPolicyInfoOne.containsKey("syElecPolicyPath")){
						filePath = elecPolicyInfoOne.getString("syElecPolicyPath");
						type = "0";//商业
					}
					
					//电子保单保存到附件库中, 有数据才保存
					if(!StringUtil.isEmpty(filePath)){
						String fileName = null;
						String array[] = filePath.split("/");
						if(array != null){
							String file = array[array.length - 1];
							if(file != null){
								fileName = file.split("\\.")[0]; //取文件名
							}
						}
						filelibraryService.onlySaveNotUpload(filePath, fileName, fileType, fileDesc, taskId, type);
						LogUtil.info("====电子保单保存完成：taskid=" + taskId + ",inscomcode=" + insccomcode + ", (1-交强0-商业)type=" + type);
					}
				}
			}
		}
	}

	private void saveSQ(String taskId, String insurancecompanyid, JSONObject sq, String taskStatus) throws Exception {
		String policyStatus = null, orderStatus = "0";
		boolean updateOrder = false;

		INSBPolicyitem queryInsbPolicyitem = new INSBPolicyitem();
		queryInsbPolicyitem.setTaskid(taskId);
		queryInsbPolicyitem.setInscomcode(insurancecompanyid);
		List<INSBPolicyitem> resultInsbPolicyitemList1 = insbPolicyitemService.queryList(queryInsbPolicyitem);
		if ("D".equals(taskStatus) || "D2".equals(taskStatus)) {
			orderStatus = "3";
			policyStatus = "1";
		} else if ("C".equals(taskStatus)) {
			orderStatus = "2";
		} else if ("B".equals(taskStatus)||"32".equals(taskStatus)||"11".equals(taskStatus)) {//核保成功 或 核保暂存成功 标志1需要回写订单表订单价格
			if (!"32".equals(taskStatus)) {
				orderStatus = "1";
			} else {
				updateOrder = true;
				if(!resultInsbPolicyitemList1.isEmpty()&&StringUtil.isNotEmpty(resultInsbPolicyitemList1.get(0).getAgentnum())){
					INSBAgent insbAgent = iNSBAgentDao.selectByJobnum(resultInsbPolicyitemList1.get(0).getAgentnum());
					if(null!=insbAgent&&StringUtil.isNotEmpty(insbAgent.getDeptid())){
						INSCDept deptPlatform = inscDeptservice.getPlatformDept(insbAgent.getDeptid());
						if(AUTO_INSURE_PLATFORM.contains(deptPlatform.getComcode())){//平台code包含在可以自动核保的平台范围内
							orderStatus = "1";
						}
					}
				}
			}
		} else {
			policyStatus = "5";
			orderStatus = "0";
		}

		for (INSBPolicyitem datainInsbPolicyitem : resultInsbPolicyitemList1) {
			if ("0".equals(datainInsbPolicyitem.getRisktype())) {
				if (!StringUtil.isEmpty(sq.get("bizTempId"))) {
					datainInsbPolicyitem.setCheckcode(sq.get("bizTempId").toString());
				}
				if (!StringUtil.isEmpty(sq.get("bizProposeNum"))) {
					datainInsbPolicyitem.setProposalformno(sq.get("bizProposeNum").toString());
				}
				if (!StringUtil.isEmpty(sq.get("bizPolicyCode"))) {
					datainInsbPolicyitem.setPolicyno(sq.get("bizPolicyCode").toString());
				}
				datainInsbPolicyitem.setPolicystatus(policyStatus);
				datainInsbPolicyitem.setModifytime(new Date());
				insbPolicyitemService.updateById(datainInsbPolicyitem);
				LogUtil.info("INSBPolicyitem|报表数据埋点|"+JSONObject.fromObject(datainInsbPolicyitem).toString());
			} else {
				if (!StringUtil.isEmpty(sq.get("efcTempId"))) {
					datainInsbPolicyitem.setCheckcode(sq.get("efcTempId").toString());
				}
				if (!StringUtil.isEmpty(sq.get("efcProposeNum"))) {
					datainInsbPolicyitem.setProposalformno(sq.get("efcProposeNum").toString());
				}
				if (!StringUtil.isEmpty(sq.get("efcPolicyCode"))) {
					datainInsbPolicyitem.setPolicyno(sq.get("efcPolicyCode").toString());
				}
				datainInsbPolicyitem.setPolicystatus(policyStatus);
				datainInsbPolicyitem.setModifytime(new Date());
				insbPolicyitemService.updateById(datainInsbPolicyitem);
				LogUtil.info("INSBPolicyitem|报表数据埋点|"+JSONObject.fromObject(datainInsbPolicyitem).toString());
			}
		}

		INSBQuotetotalinfo queryInsbQuotetotalinfo = new INSBQuotetotalinfo();
		queryInsbQuotetotalinfo.setTaskid(taskId);
		queryInsbQuotetotalinfo = insbQuotetotalinfoService.queryOne(queryInsbQuotetotalinfo);
		queryInsbQuotetotalinfo.setModifytime(new Date());
		insbQuotetotalinfoService.updateById(queryInsbQuotetotalinfo);
		LogUtil.info("INSBQuotetotalinfo|报表数据埋点|"+JSONObject.fromObject(queryInsbQuotetotalinfo).toString());
		INSBQuoteinfo queryInsbQuoteinfo = new INSBQuoteinfo();
		queryInsbQuoteinfo.setQuotetotalinfoid(queryInsbQuotetotalinfo.getId());
		queryInsbQuoteinfo.setInscomcode(insurancecompanyid);
		queryInsbQuoteinfo = insbQuoteinfoService.queryOne(queryInsbQuoteinfo);
		queryInsbQuoteinfo.setModifytime(new Date());
		if (!StringUtil.isEmpty(sq.get("misc"))) {//
			queryInsbQuoteinfo.setNoti(sq.get("misc").toString());
		}
		insbQuoteinfoService.updateById(queryInsbQuoteinfo);
		LogUtil.info("INSBQuoteinfo|报表数据埋点|"+JSONObject.fromObject(queryInsbQuoteinfo).toString());

		if ((!StringUtil.isEmpty(orderStatus) && !"0".equals(orderStatus)) || updateOrder) {
			INSBOrder insbOrder = new INSBOrder();
			insbOrder.setTaskid(taskId);
			insbOrder.setPrvid(insurancecompanyid);
			insbOrder = insbOrderService.queryOne(insbOrder);
			if (insbOrder != null) {
				if (!StringUtil.isEmpty(sq.get("orderId"))) {
					insbOrder.setInsorderno(sq.get("orderId").toString());
				}
				if (!StringUtil.isEmpty(sq.get("totalCharge"))) {
					insbOrder.setTotalproductamount(Double.valueOf(sq.get("totalCharge").toString()));
					insbOrder.setTotalpaymentamount(Double.valueOf(sq.get("totalCharge").toString()));
				}
				insbOrder.setOrderstatus(orderStatus);
				insbOrderService.updateById(insbOrder);
				LogUtil.info("INSBOrder|报表数据埋点|"+JSONObject.fromObject(insbOrder).toString());
				LogUtil.info(taskId+","+insurancecompanyid+"更新insborder: "+insbOrder.getTotalpaymentamount()+", "+insbOrder.getOrderstatus());
			}
		}

		LogUtil.info(taskStatus+"=taskStatus,orderStatus="+orderStatus+"====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",SQ保存完成===="+sq.get("totalCharge"));
	}

	private void updateOrderStatus(String taskId, String insurancecompanyid, String orderStatus, String policyStatus) throws Exception {
		if (StringUtil.isNotEmpty(orderStatus)) {
			INSBOrder insbOrder = new INSBOrder();
			insbOrder.setTaskid(taskId);
			insbOrder.setPrvid(insurancecompanyid);
			insbOrder = insbOrderService.queryOne(insbOrder);

			if (insbOrder != null) {
				insbOrder.setOrderstatus(orderStatus);
				insbOrderService.updateById(insbOrder);
				LogUtil.info("INSBOrder|报表数据埋点|"+JSONObject.fromObject(insbOrder).toString());
				LogUtil.info(taskId+","+insurancecompanyid+"更新insborder: "+insbOrder.getTotalpaymentamount()+", "+insbOrder.getOrderstatus());
			}
		}

		if (StringUtil.isNotEmpty(policyStatus)) {
			INSBPolicyitem queryInsbPolicyitem = new INSBPolicyitem();
			queryInsbPolicyitem.setTaskid(taskId);
			queryInsbPolicyitem.setInscomcode(insurancecompanyid);
			List<INSBPolicyitem> resultInsbPolicyitemList1 = insbPolicyitemService.queryList(queryInsbPolicyitem);

			for (INSBPolicyitem datainInsbPolicyitem : resultInsbPolicyitemList1) {
				datainInsbPolicyitem.setPolicystatus(policyStatus);
				datainInsbPolicyitem.setModifytime(new Date());
				insbPolicyitemService.updateById(datainInsbPolicyitem);
				LogUtil.info("INSBPolicyitem|报表数据埋点|" + JSONObject.fromObject(datainInsbPolicyitem).toString());
			}
		}
	}

	private void saveDeliverInfo(String taskId, String insurancecompanyid, JSONObject deliverInfo) throws Exception {
		/**
		 * 根据taskid去insborderdelivery中查询
		 *
		 * @author zhangdi
		 */
		INSBOrderdelivery dataInsbOrderdelivery = new INSBOrderdelivery();
		dataInsbOrderdelivery.setTaskid(taskId);
		dataInsbOrderdelivery.setProviderid(insurancecompanyid);
		dataInsbOrderdelivery = insbOrderdeliveryService.queryOne(dataInsbOrderdelivery);
		if (dataInsbOrderdelivery == null)
			return;

		if (!StringUtil.isEmpty(deliverInfo.get("name"))) {
			dataInsbOrderdelivery.setRecipientname(deliverInfo.get("name").toString());// 姓名
		}
		if (!StringUtil.isEmpty(deliverInfo.get("phone"))) {
			dataInsbOrderdelivery.setRecipientmobilephone(deliverInfo.get("phone").toString());// 手机号码
		}
		if (!StringUtil.isEmpty(deliverInfo.get("province"))) {
			dataInsbOrderdelivery.setRecipientprovince(deliverInfo.get("province").toString());// 省
		}
		if (!StringUtil.isEmpty(deliverInfo.get("city"))) {
			dataInsbOrderdelivery.setRecipientcity(deliverInfo.get("city").toString());// 市
		}
		if (!StringUtil.isEmpty(deliverInfo.get("area"))) {
			dataInsbOrderdelivery.setRecipientarea(deliverInfo.get("area").toString());// 区县
		}
		if (!StringUtil.isEmpty(deliverInfo.get("address"))) {
			dataInsbOrderdelivery.setRecipientaddress(deliverInfo.get("address").toString());// 投送地址
		}
		if (!StringUtil.isEmpty(deliverInfo.get("postCode"))) {
			dataInsbOrderdelivery.setZip(deliverInfo.get("postCode").toString());// 邮编
		}
		if (!StringUtil.isEmpty(deliverInfo.get("receiveDayType"))) {
			dataInsbOrderdelivery.setReceiveday(String.valueOf(deliverInfo.get("receiveDayType")));// 接收日期
			// 工作日/休息日/全部
		}
		if (!StringUtil.isEmpty(deliverInfo.get("receiveTimeType"))) {
			dataInsbOrderdelivery.setReceivetime(String.valueOf(deliverInfo.get("receiveTimeType")));// 接收时间
			// 早/中/晚/全天
		}
		if (!StringUtil.isEmpty(deliverInfo.get("isInvoice"))) {
			dataInsbOrderdelivery.setIsinvoice(deliverInfo.getBoolean("isInvoice") ? "1" : "0");// 是否需要发票
		}
		if (!StringUtil.isEmpty(deliverInfo.get("invoiceTitle"))) {
			dataInsbOrderdelivery.setInvoicetitle(deliverInfo.get("invoiceTitle").toString());// 发票抬头
		}
		if (!StringUtil.isEmpty(deliverInfo.get("remark"))) {
			dataInsbOrderdelivery.setNoti(deliverInfo.get("remark").toString());// 备注
		}
		if (!StringUtil.isEmpty(deliverInfo.get("deliveType"))) {
			dataInsbOrderdelivery.setDelivetype(String.valueOf(deliverInfo.get("deliveType")));// 配送方式编码
			// 自取/快递/电子
		}
		if (!StringUtil.isEmpty(deliverInfo.get("isInsureCoDelive"))) {
			dataInsbOrderdelivery.setIsinsurecodelive(Boolean.valueOf(deliverInfo.get("isInsureCoDelive").toString()) ? "1" : "0");// 是否保险公司配送
		}
		if (!StringUtil.isEmpty(deliverInfo.get("isFreightCollect"))) {
			dataInsbOrderdelivery.setIsfreightcollect(Boolean.valueOf(deliverInfo.get("isFreightCollect").toString()) ? "1" : "0");// 是否货到付款
		}
		if (!StringUtil.isEmpty(deliverInfo.get("fee"))) {
			dataInsbOrderdelivery.setFee(Double.valueOf(deliverInfo.get("fee").toString()));// 配送费用
		}
		if (!StringUtil.isEmpty(deliverInfo.get("traceNumber"))) {
			dataInsbOrderdelivery.setTracenumber(deliverInfo.get("traceNumber").toString());// 快递单号
		}
		dataInsbOrderdelivery.setModifytime(new Date());
		insbOrderdeliveryDao.updateById(dataInsbOrderdelivery);

		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",配送信息保存完成====");

	}

	private void saveOrderInfo(String taskId, String insurancecompanyid, JSONObject orderInfo) throws Exception {

		INSBQuoteinfo dataInsbQuoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(taskId, insurancecompanyid);
		if (dataInsbQuoteinfo == null)
			return;

		if (!StringUtil.isEmpty(orderInfo.get("remark")))
			dataInsbQuoteinfo.setNoti(orderInfo.get("remark").toString());
		if (!StringUtil.isEmpty(orderInfo.get("created")))
			dataInsbQuoteinfo.setCreatetime(DateUtil.parse(orderInfo.get("created").toString(), "yyyy-MM-dd HH:mm:ss"));
		if (!StringUtil.isEmpty(orderInfo.get("updated")))
			dataInsbQuoteinfo.setModifytime(DateUtil.parse(orderInfo.get("updated").toString(), "yyyy-MM-dd HH:mm:ss"));
		dataInsbQuoteinfo.setModifytime(new Date());
		insbQuoteinfoService.updateById(dataInsbQuoteinfo);
		LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",订单信息保存完成====");
	}

	public String pushtocif1(String taskid, String companyid, String risktype) {
		LogUtil.info("进入推cif任务接口：taskid=" + taskid + ",companyid=" + companyid + ",risktype=" + risktype);
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> carInfoAndCarModel;
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskid);
		insbPolicyitem.setRisktype(risktype);
		List<INSBPolicyitem> insbPolicyitemList = insbPolicyitemService.queryList(insbPolicyitem);
		if (insbPolicyitemList.size() == 0) {
			LogUtil.info("结束推cif任务接口：taskid=" + taskid + ",companyid=" + companyid + ",risktype=" + risktype + ",未找到订单");
			return "success";
		}
		INSBPolicyitem policyitem = null;
		for (INSBPolicyitem insbPolicyitem111 : insbPolicyitemList) {
			if (companyid.equals(insbPolicyitem111.getInscomcode())) {
				policyitem = insbPolicyitem111;
			}
		}
		if (policyitem == null)
			policyitem = insbPolicyitemList.get(0);
		carInfoAndCarModel = this.getCarInfoAndCarModel(taskid, companyid);
		map.put("carinfo", carInfoAndCarModel.get("carinfo"));
		map.put("carModel", carInfoAndCarModel.get("carModel"));

		if (StringUtil.isEmpty(policyitem.getPolicyno()))
			return "fail";
		String vehicleno = ((com.zzb.cm.Interface.entity.cif.model.CarInfo) carInfoAndCarModel.get("carinfo")).getVehicleno();
		String policyno = policyitem.getPolicyno();
		List<CarKind> carKinds1, carKinds2;
		if ("0".equals(risktype)) {
			carKinds1 = this.getCarKinds(taskid, companyid, "0", policyno, vehicleno);
			carKinds2 = this.getCarKinds(taskid, companyid, "1", policyno, vehicleno);
		} else {
			carKinds1 = this.getCarKinds(taskid, companyid, "2", policyno, vehicleno);
			carKinds2 = this.getCarKinds(taskid, companyid, "3", policyno, vehicleno);
		}
		carKinds1.addAll(carKinds2);
		map.put("carKinds", carKinds1);
		BigDecimal totaleCharge = new BigDecimal("0");
		for (CarKind carKind : carKinds1) {
			totaleCharge.add(BigDecimal.valueOf(carKind.getPrem()));
		}
		CarKindPolicy carKindPolicy = this.getCarKindPolicy(policyitem, taskid, companyid, vehicleno);
		carKindPolicy.setSumprem(totaleCharge.setScale(5, RoundingMode.HALF_UP).doubleValue());
		map.put("carKindPolicy", carKindPolicy);
		map.put("insureds", this.getInsureds(taskid, companyid, policyno, vehicleno));
		map.put("personInfo", this.getPersonInfo(taskid, vehicleno));
		map.put("status", "2");
		List<Beneficiary> beneficiarysList = this.getBeneficiarys(taskid, companyid, policyno);
		map.put("beneficiarys", beneficiarysList);
		// 获取纳税人信息
		TaxpayerInfo taxpayerinfo = new TaxpayerInfo();
		taxpayerinfo.setTaxPersonCert(carKindPolicy.getPropno());// '纳税人证件号'
		taxpayerinfo.setPersonName(carKindPolicy.getPropname());// '纳税人姓名'
		taxpayerinfo.setPolicyno(policyno);// 保单号
		taxpayerinfo.setSource("cm");
		map.put("taxpayerinfo", taxpayerinfo);
		// 配送信息
		DeliveryInfo deliveryinfo = this.getDeliveryInfo(policyitem);
		map.put("deliveryinfo", deliveryinfo);
		// 支付信息
		IntendToPay intendtopay = this.getIntendToPay(policyitem);
		map.put("intendtopay", intendtopay);

		String json = com.alibaba.fastjson.JSONObject.toJSONStringWithDateFormat(map, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
		String result = HttpClientUtil.doPostJsonAsyncClientJson(ValidateUtil.getConfigValue("pushtocif.url"), json,"utf-8");
		LogUtil.info("关闭推cif任务接口：taskid=" + taskid + ",companyid=" + companyid + ",risktype=" + risktype + ",url=" + ValidateUtil.getConfigValue("pushtocif.url") + ",result=" + result + ",json="
				+ json);
		return result;
	}

	private IntendToPay getIntendToPay(INSBPolicyitem insbPolicyitem) {
		IntendToPay intendToPay = new IntendToPay();
		intendToPay.setPolicyno(insbPolicyitem.getPolicyno());
		INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
		insbOrderpayment.setTaskid(insbPolicyitem.getTaskid());
		insbOrderpayment.setSubinstanceid(this.getChildTaskId(insbPolicyitem.getTaskid(), insbPolicyitem.getInscomcode()));
		insbOrderpayment = insbOrderpaymentService.queryOne(insbOrderpayment);
		if (insbOrderpayment != null) {
			INSBPaychannel insbPaychannel = insbPaychannelService.queryById(insbOrderpayment.getPaychannelid());
			intendToPay.setPaymentMethod(insbPaychannel.getPaychannelname());// 支付方法
			intendToPay.setTxType(insbPaychannel.getPaytype());// 交易类型
		}
		intendToPay.setSource("cm");
		// intendToPay.setPaymentTarget(paymentTarget);//paymentTarget
		// intendToPay.setMerchantId(merchantId);//收款商户号码
		// intendToPay.setEnableChanged(enableChanged);//是否可以更改支付方式
		// intendToPay.setThirdPartyMerchantId(thirdPartyMerchantId);//第三方收款服务方ID
		// intendToPay.setPayeeCode(payeeCode);//收款方编码
		return intendToPay;
	}

	private DeliveryInfo getDeliveryInfo(INSBPolicyitem insbPolicyitem) {
		DeliveryInfo deliveryinfo = new DeliveryInfo();
		deliveryinfo.setPolicyno(insbPolicyitem.getPolicyno());// 保单号
		INSBOrderdelivery insbOrderdelivery = new INSBOrderdelivery();
		insbOrderdelivery.setTaskid(insbPolicyitem.getTaskid());
		insbOrderdelivery.setProviderid(insbPolicyitem.getInscomcode());
		insbOrderdelivery = insbOrderdeliveryService.queryOne(insbOrderdelivery);
		if (insbOrderdelivery != null) {
			deliveryinfo.setCode("0".equals(insbOrderdelivery.getDelivetype()) ? "SelfTake" : ("1".equals(insbOrderdelivery.getDelivetype()) ? "Express" : insbOrderdelivery.getDelivetype()));// '配送方式编码（SelfTake－自取，Express－快递）',
			deliveryinfo.setDeliveryCost(String.valueOf(insbOrderdelivery.getFee()));// '配送费用',
			INSCDept inscDept = new INSCDept();
			inscDept.setComcode(insbOrderdelivery.getDeptcode());
			inscDept = inscDeptService.queryOne(inscDept);
			deliveryinfo.setNodeLevel(inscDept.getComgrade());// 出单网点级别
			deliveryinfo.setNodeid(inscDept.getId());// 网点id
			deliveryinfo.setNodename(inscDept.getComname());// 网点名称
			deliveryinfo.setOrgCode(inscDept.getComcode());// 网点code
			INSBRegion insbRegion1 = insbRegionService.queryById(inscDept.getProvince());
			INSBRegion insbRegion2 = insbRegionService.queryById(inscDept.getCity());
			INSBRegion insbRegion3 = insbRegionService.queryById(inscDept.getCounty());
			deliveryinfo.setNodeaddress((StringUtil.isEmpty(insbRegion1) ? "" : insbRegion1.getComcodename()) + (StringUtil.isEmpty(insbRegion2) ? "" : insbRegion2.getComcodename())
					+ (StringUtil.isEmpty(insbRegion3) ? "" : insbRegion3.getComcodename()) + inscDept.getAddress());// 网点地址
			if (!StringUtil.isEmpty(insbOrderdelivery.getRecipientprovince()) && !StringUtil.isEmpty(insbOrderdelivery.getRecipientcity()) && !StringUtil.isEmpty(insbOrderdelivery.getRecipientarea())) {
				insbRegion1 = insbRegionService.queryById(inscDept.getProvince());
				insbRegion2 = insbRegionService.queryById(inscDept.getCity());
				insbRegion3 = insbRegionService.queryById(inscDept.getCounty());
				deliveryinfo.setReceiveAddress((StringUtil.isEmpty(insbRegion1) ? "" : insbRegion1.getComcodename()) + (StringUtil.isEmpty(insbRegion2) ? "" : insbRegion2.getComcodename())
						+ (StringUtil.isEmpty(insbRegion3) ? "" : insbRegion3.getComcodename()) + insbOrderdelivery.getRecipientaddress());// '投送地址'
			}
		}
		// deliveryinfo.setPersonName(personName);// '配送人姓名'
		// deliveryinfo.setShippingCode(shippingCode);//配送商品编码
		// deliveryinfo.setShippingName(shippingName);//配送商品名称
		return deliveryinfo;
	}

	public List<Beneficiary> getBeneficiarys(String taskId, String insurancecompanyid, String policyno) {
		/**
		 * 根据taskid和companyid去insblegalrightclaimhis中查,如果没有查到数据,
		 * 则去insblegalrightclaim中查
		 *
		 * @author zhangdi
		 */
		List<Beneficiary> beneficiarys = new ArrayList<Beneficiary>();
		List<INSBLegalrightclaim> resultInsbLegalrightclaimList = new ArrayList<INSBLegalrightclaim>();

		INSBLegalrightclaimhis queryInsbLegalrightclaimhis = new INSBLegalrightclaimhis();
		queryInsbLegalrightclaimhis.setTaskid(taskId);
		queryInsbLegalrightclaimhis.setInscomcode(insurancecompanyid);
		List<INSBLegalrightclaimhis> resultInsbLegalrightclaimhisList = insbLegalrightclaimhisService.queryList(queryInsbLegalrightclaimhis);
		if (resultInsbLegalrightclaimhisList.size() > 0) {
		} else {
			INSBLegalrightclaim queryInsbLegalrightclaim = new INSBLegalrightclaim();
			queryInsbLegalrightclaim.setTaskid(taskId);
			resultInsbLegalrightclaimList = insbLegalrightclaimService.queryList(queryInsbLegalrightclaim);
			if (resultInsbLegalrightclaimList.size() > 0) {
				for (INSBLegalrightclaim dataInsbLegalrightclaim : resultInsbLegalrightclaimList) {
					try {
						INSBLegalrightclaimhis dataInsbLegalrightclaimhis = new INSBLegalrightclaimhis();
						PropertyUtils.copyProperties(dataInsbLegalrightclaimhis, dataInsbLegalrightclaim);
						resultInsbLegalrightclaimhisList.add(dataInsbLegalrightclaimhis);
					} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
						LogUtil.error("taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",接口受益人信息获得出错");
						e.printStackTrace();
						return beneficiarys;
					}
				}
			} else {
				return beneficiarys;
			}
		}
		for (INSBLegalrightclaimhis dataInsbLegalrightclaimhis : resultInsbLegalrightclaimhisList) {
			INSBPerson dataInsbPerson = new INSBPerson();
			dataInsbPerson = insbPersonService.queryById(dataInsbLegalrightclaimhis.getPersonid());
			if (dataInsbPerson == null) {
				return beneficiarys;
			} else {
				Beneficiary beneficiary = new Beneficiary();
				beneficiary.setPersonName(dataInsbPerson.getName());
				beneficiary.setGender(StringUtil.isEmpty(dataInsbPerson.getGender()) ? null : String.valueOf(dataInsbPerson.getGender()));
				beneficiary.setBirthday(dataInsbPerson.getBirthday());
				beneficiary.setCertNumber(dataInsbPerson.getIdcardno());
				beneficiary.setCertType(StringUtil.isEmpty(dataInsbPerson.getIdcardtype()) ? null : String.valueOf(dataInsbPerson.getIdcardtype()));
				beneficiary.setAge(StringUtil.isEmpty(dataInsbPerson.getAge()) ? null : String.valueOf(dataInsbPerson.getAge()));
				beneficiary.setScaned("false");
				beneficiary.setPolicyno(policyno);
				beneficiary.setAddress(dataInsbPerson.getAddress());
				beneficiary.setSource("cm");
				beneficiarys.add(beneficiary);
			}
		}
		return beneficiarys;
	}

	public String pushtocif(String taskid, String companyid) {
		String pushtocif1;
		String pushtocif2;
		try {
			pushtocif1 = this.pushtocif1(taskid, companyid, "0");
			pushtocif2 = this.pushtocif1(taskid, companyid, "1");
		} catch (Exception e) {
			LogUtil.info("推cif任务接口：taskid=" + taskid + ",companyid=" + companyid + "结果：fail,系统异常：" + e.getStackTrace());
			e.printStackTrace();
			return "fail";
		}
		if ("fail".equals(pushtocif1) || "fail".equals(pushtocif2)) {
			LogUtil.info("推cif任务接口：taskid=" + taskid + ",companyid=" + companyid + "结果：fail");
			return "fail";
		} else {
			LogUtil.info("推cif任务接口：taskid=" + taskid + ",companyid=" + companyid + "结果：success");
			return "success";
		}
	}

	private CarKindPolicy getCarKindPolicy(INSBPolicyitem policyitem, String taskid, String companyid, String vehicleno) {
		CarKindPolicy carKindPolicy = new CarKindPolicy();
		carKindPolicy.setPolicysource("cm");
		carKindPolicy.setAgentcode(policyitem.getAgentnum());// 代理人编码
		carKindPolicy.setAgentname(policyitem.getAgentname());// 代理人姓名
		carKindPolicy.setEnddate(policyitem.getEnddate());// 保险止期
		carKindPolicy.setIfjq("0".equals(policyitem.getRisktype()) ? "N" : "Y");// 是否交强（"Y"：交强,"N":商业）
		carKindPolicy.setPolicyno(StringUtil.isEmpty(policyitem.getPolicyno()) ? "" : policyitem.getPolicyno());// 保单号
		carKindPolicy.setPrem(BigDecimal.valueOf(StringUtil.isEmpty(policyitem.getDiscountCharge()) ? 0.00 : policyitem.getDiscountCharge()).setScale(5, RoundingMode.HALF_UP).doubleValue());// 保费
		this.getApplicantInfo(carKindPolicy, taskid, companyid);
		carKindPolicy.setStartdate(policyitem.getStartdate());// 保险起期
		// carKindPolicy.setSumprem(BigDecimal.valueOf(StringUtil.isEmpty(policyitem.getTotalepremium())?0.00:policyitem.getTotalepremium()).setScale(5,
		// RoundingMode.HALF_UP).doubleValue());//总保费
		carKindPolicy.setSupplierid(companyid);// 保险公司
		carKindPolicy.setVehicleno(vehicleno);// 车牌号
		if ("1".equals(policyitem.getRisktype())) {
			INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
			insbCarkindprice.setTaskid(taskid);
			insbCarkindprice.setInskindtype("3");
			insbCarkindprice.setInscomcode(companyid);
			insbCarkindprice.setInskindcode("VehicleTax");
			List<INSBCarkindprice> resultInsbCarkindpriceList = insbCarkindpriceService.queryList(insbCarkindprice);
			if (resultInsbCarkindpriceList.size() > 0)
				carKindPolicy.setTax(BigDecimal.valueOf(resultInsbCarkindpriceList.get(0).getDiscountCharge() == null ? 0.0 : resultInsbCarkindpriceList.get(0).getDiscountCharge())
						.setScale(1, RoundingMode.HALF_UP).doubleValue());// 车船税费用
		}
		return carKindPolicy;
	}

	private com.zzb.cm.Interface.entity.cif.model.PersonInfo getPersonInfo(String taskid, String carlicenseno) {
		com.zzb.cm.Interface.entity.cif.model.PersonInfo personInfo = new com.zzb.cm.Interface.entity.cif.model.PersonInfo();
		INSBCarowneinfo insbCarowneinfo = new INSBCarowneinfo();
		insbCarowneinfo.setTaskid(taskid);
		insbCarowneinfo = insbCarowneinfoService.queryOne(insbCarowneinfo);
		if (insbCarowneinfo == null || StringUtil.isEmpty(insbCarowneinfo.getPersonid()))
			return personInfo;
		INSBPerson insbPerson = insbPersonService.queryById(insbCarowneinfo.getPersonid());
		// personInfo.setContactoradress();//联系人地址
		// personInfo.setContactoridno();//联系人证件编号
		// personInfo.setContactoridtype();//联系人证件类型
		// personInfo.setContactormail();//联系人email
		// personInfo.setContactormobile();//联系人手机号
		// personInfo.setContactorrealname();//联系人姓名
		// personInfo.setContactorwxcode();//联系人微信号

		personInfo.setOwneradress(insbPerson.getAddress());// 车主地址
		personInfo.setOwneridno(insbPerson.getIdcardno());// 车主证件编号
		if (insbPerson.getIdcardtype() != null)
			personInfo.setOwneridtype(String.valueOf(insbPerson.getIdcardtype()));// 车主证件类型
		personInfo.setOwnermail(insbPerson.getEmail());// 车主email
		personInfo.setOwnermobile(insbPerson.getCellphone());// 车主手机号
		personInfo.setOwnerrealname(insbPerson.getName());// 车主姓名
		// personInfo.setOwnerregistration();//车主户籍所在地
		// personInfo.setOwnerwxcode();//车主微信号
		personInfo.setVehicleno(carlicenseno);// 车牌号
		personInfo.setPersonsource("cm");
		return personInfo;
	}

	private List<Insured> getInsureds(String taskid, String companyid, String policyno, String carlicenseno) {

		List<Insured> insuredInfoList = new ArrayList<Insured>();
		Insured insuredInfo = new Insured();

		INSBInsuredhis queryInsbInsuredhis = new INSBInsuredhis();
		INSBInsuredhis dataInsbInsuredhis = new INSBInsuredhis();
		queryInsbInsuredhis.setTaskid(taskid);
		queryInsbInsuredhis.setInscomcode(companyid);
		List<INSBInsuredhis> insbInsuredhisList = insbInsuredhisService.queryList(queryInsbInsuredhis);
		if (insbInsuredhisList.size() == 0) {
			INSBInsured queryInsbInsured = new INSBInsured();
			queryInsbInsured.setTaskid(taskid);
			List<INSBInsured> insbInsuredList = insbInsuredService.queryList(queryInsbInsured);
			if (insbInsuredList.size() == 0) {
				return insuredInfoList;
			} else {
				for (INSBInsured dataInsbInsured : insbInsuredList) {
					try {
						dataInsbInsuredhis = new INSBInsuredhis();
						PropertyUtils.copyProperties(dataInsbInsuredhis, dataInsbInsured);
						insbInsuredhisList.add(dataInsbInsuredhis);
					} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
						LogUtil.error("taskid=" + taskid + ",inscomcode=" + companyid + ",接口被保人信息获得出错");
						e.printStackTrace();
						return insuredInfoList;
					}
				}
			}
		}

		INSBPerson dataInsbPerson;
		for (INSBInsuredhis insbInsuredhis : insbInsuredhisList) {
			dataInsbPerson = insbPersonService.queryById(insbInsuredhis.getPersonid());
			if (dataInsbPerson == null)
				continue;
			if (dataInsbPerson.getIdcardtype() != null)
				insuredInfo.setInsurecertificate(String.valueOf(dataInsbPerson.getIdcardtype()));// 被保人证件类型
			insuredInfo.setInsuremobile(dataInsbPerson.getCellphone());// 被保人手机号
			insuredInfo.setInsurename(dataInsbPerson.getName());// 被保人姓名
			insuredInfo.setInsureno(dataInsbPerson.getIdcardno());// 被保人证件编号
			insuredInfo.setPolicyno(policyno);// 保单号
			insuredInfo.setVehicleno(carlicenseno);// 车牌号
			insuredInfo.setInsured_source("cm");
			insuredInfoList.add(insuredInfo);
		}
		return insuredInfoList;
	}

	private void getApplicantInfo(CarKindPolicy carKindPolicy, String taskid, String companyid) {
		INSBApplicant dataInsbApplicant = new INSBApplicant();

		INSBApplicanthis queryInsbApplicanthis = new INSBApplicanthis();
		queryInsbApplicanthis.setTaskid(taskid);
		queryInsbApplicanthis.setInscomcode(companyid);
		INSBApplicanthis dataInsbApplicanthis1 = insbApplicanthisService.queryOne(queryInsbApplicanthis);
		INSBApplicanthis dataInsbApplicanthis = new INSBApplicanthis();
		if (dataInsbApplicanthis1 == null) {
			INSBApplicant queryInsbApplicant = new INSBApplicant();
			queryInsbApplicant.setTaskid(taskid);
			dataInsbApplicant = insbApplicantService.queryOne(queryInsbApplicant);
			if (dataInsbApplicant == null) {
				return;
			} else {
				try {
					dataInsbApplicanthis = new INSBApplicanthis();
					PropertyUtils.copyProperties(dataInsbApplicanthis, dataInsbApplicant);
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					LogUtil.error("taskid=" + taskid + ",inscomcode=" + companyid + ",接口投保人信息获得出错");
					e.printStackTrace();
					return;
				}
			}
		} else {
			dataInsbApplicanthis = dataInsbApplicanthis1;
		}

		INSBPerson dataInsbPerson = insbPersonService.queryById(dataInsbApplicanthis.getPersonid());
		if (dataInsbPerson == null)
			return;
		if (dataInsbPerson.getIdcardtype() != null)
			carKindPolicy.setPropcertificate(String.valueOf(dataInsbPerson.getIdcardtype()));// 投保人证件类型
		carKindPolicy.setPropmobile(dataInsbPerson.getCellphone());// 投保人手机号
		carKindPolicy.setPropname(dataInsbPerson.getName());// 投保人姓名
		carKindPolicy.setPropno(dataInsbPerson.getIdcardno());// 投保人证件编号
	}

	private List<CarKind> getCarKinds(String taskid, String companyId, String riskType, String policyno, String carlicenseno) {
		List<CarKind> carKinds = new ArrayList<CarKind>();
		INSBCarinfohis insbCarinfoHis = new INSBCarinfohis();
		insbCarinfoHis.setTaskid(taskid);
		insbCarinfoHis.setInscomcode(companyId);
		List<INSBCarinfohis> resultInsbCarinfoHisList = insbCarinfohisService.queryList(insbCarinfoHis);
		if (resultInsbCarinfoHisList.size() > 0) {
			insbCarinfoHis = resultInsbCarinfoHisList.get(0);
		} else {
			INSBCarinfo insbCarinfo = new INSBCarinfo();
			insbCarinfo.setTaskid(taskid);
			List<INSBCarinfo> resultInsbCarinfoList = insbCarinfoService.queryList(insbCarinfo);
			if (resultInsbCarinfoList.size() > 0) {
				insbCarinfo = resultInsbCarinfoList.get(0);
			} else {
				return carKinds;
			}
			try {
				PropertyUtils.copyProperties(insbCarinfoHis, insbCarinfo);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				LogUtil.error("taskid=" + taskid + ",inscomcode=" + companyId + ",接口车辆信息获得出错1");
				e.printStackTrace();
				return carKinds;
			}
		}
		// 车辆类型
		INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
		insbCarkindprice.setTaskid(taskid);
		insbCarkindprice.setInskindtype(riskType);
		insbCarkindprice.setInscomcode(companyId);
		List<INSBCarkindprice> resultInsbCarkindpriceList = insbCarkindpriceService.queryList(insbCarkindprice);
		if (resultInsbCarkindpriceList.size() == 0) {
			return carKinds;
		}
		CarKind carKind;
		INSBRiskkind insbriskkind;
		List<INSBRiskkind> insbriskkindList;
		for (INSBCarkindprice carkindprice : resultInsbCarkindpriceList) {
			carKind = new CarKind();
			carKind.setAmount(BigDecimal.valueOf(carkindprice.getAmount() == null ? 0.00 : carkindprice.getAmount()).setScale(5, RoundingMode.HALF_UP).doubleValue());// 保额
			carKind.setKindcode(carkindprice.getInskindcode());// 险别代码
			insbriskkind = new INSBRiskkind();
			insbriskkind.setKindcode(carkindprice.getInskindcode());
			insbriskkindList = insbriskkindService.queryList(insbriskkind);
			if (insbriskkindList.size() > 0)
				carKind.setKindname(insbriskkindList.get(0).getKindname());// 险别名称
			carKind.setPolicyno(policyno);// 对应保单号
			carKind.setPrem(BigDecimal.valueOf(carkindprice.getDiscountCharge() == null ? 0.00 : carkindprice.getDiscountCharge()).setScale(5, RoundingMode.HALF_UP).doubleValue());// 保费
			// carKind.setSource();//来源
			carKind.setSupplierid(companyId);// 保险公司
			carKind.setVehicleno(carlicenseno);// 车牌号
			carKind.setSource("cm");
			carKinds.add(carKind);
		}
		return carKinds;
	}

	private Map<String, Object> getCarInfoAndCarModel(String taskId, String insurancecompanyid) {
		/**
		 * 车辆信息保存逻辑：用户首次录入的车辆信息保存在insbcarinfo中,
		 * 当用户选择完毕报价的保险公司后,并且修改了对应某一家保险公司的车辆信息,则修改后的数据保存在insbcarinfohis中；
		 * 获取这些数据需要根据taskid和companyid获得。
		 *
		 * @author zhangdi
		 * @@获取车辆信息的时候先去insbcarinfohis中查询,如果没有查到结果,则去insbcarinfo中查询。
		 */
		Map<String, Object> map = new HashMap<String, Object>();
		com.zzb.cm.Interface.entity.cif.model.CarInfo carInfo = new com.zzb.cm.Interface.entity.cif.model.CarInfo();
		carInfo.setSyssource("cm");
		CarModel carModel = new CarModel();
		carModel.setCarmodelsource("cm");
		map.put("carinfo", carInfo);
		map.put("carModel", carModel);
		INSBCarinfohis insbCarinfoHis = new INSBCarinfohis();
		insbCarinfoHis.setTaskid(taskId);
		insbCarinfoHis.setInscomcode(insurancecompanyid);
		List<INSBCarinfohis> resultInsbCarinfoHisList = insbCarinfohisService.queryList(insbCarinfoHis);
		if (resultInsbCarinfoHisList.size() > 0) {
			insbCarinfoHis = resultInsbCarinfoHisList.get(0);
		} else {
			INSBCarinfo insbCarinfo = new INSBCarinfo();
			insbCarinfo.setTaskid(taskId);
			List<INSBCarinfo> resultInsbCarinfoList = insbCarinfoService.queryList(insbCarinfo);
			if (resultInsbCarinfoList.size() > 0) {
				insbCarinfo = resultInsbCarinfoList.get(0);
			} else {
				LogUtil.error("taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",接口车辆信息获得出错0");
				return map;
			}
			try {
				PropertyUtils.copyProperties(insbCarinfoHis, insbCarinfo);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				LogUtil.error("taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",接口车辆信息获得出错1");
				e.printStackTrace();
				return map;
			}
		}
		INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setTaskid(taskId);
		List<INSBCarinfo> resultInsbCarinfoList = insbCarinfoService.queryList(insbCarinfo);
		if (resultInsbCarinfoList.size() > 0) {
			insbCarinfo = resultInsbCarinfoList.get(0);
		} else
			return map;
		INSBCarmodelinfohis insbCarmodelinfohis = new INSBCarmodelinfohis();
		insbCarmodelinfohis.setCarinfoid(insbCarinfo.getId());
		insbCarmodelinfohis.setInscomcode(insurancecompanyid);
		List<INSBCarmodelinfohis> resultInsbCarmodelinfohisList = insbCarmodelinfohisService.queryList(insbCarmodelinfohis);
		if (resultInsbCarmodelinfohisList.size() > 0) {
			insbCarmodelinfohis = resultInsbCarmodelinfohisList.get(0);
		} else {
			INSBCarmodelinfo insbCarmodelinfo = new INSBCarmodelinfo();
			insbCarmodelinfo.setCarinfoid(insbCarinfo.getId());
			List<INSBCarmodelinfo> resultInsbCarmodelinfoList = insbCarmodelinfoService.queryList(insbCarmodelinfo);
			if (resultInsbCarmodelinfoList.size() > 0) {
				insbCarmodelinfo = resultInsbCarmodelinfoList.get(0);
				try {
					PropertyUtils.copyProperties(insbCarmodelinfohis, insbCarmodelinfo);
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					LogUtil.error("taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",接口车型信息获得出错2");
					e.printStackTrace();
					return map;
				}
			} else {
				LogUtil.error("taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",接口车型信息获得出错3");
				//return map;
			}
		}

		carInfo.setAreaname(InterFaceDefaultValueUtil.getDefaultValue(taskId, insurancecompanyid, "CarInfo", "drivingarea").toString());// 约定行驶区域
		carInfo.setChgownerflag(insbCarinfoHis.getIsTransfercar());// 是否过户车
		carInfo.setEngineno(insbCarinfoHis.getEngineno());// 发动机号
		carInfo.setPrice(BigDecimal.valueOf(StringUtil.isEmpty(insbCarmodelinfohis.getPrice()) ? 0.00 : insbCarmodelinfohis.getPrice()).setScale(5, RoundingMode.HALF_UP).doubleValue());// 不含税价
		carInfo.setRegisterdate(insbCarinfoHis.getRegistdate());// 初登日期
		carInfo.setTaxprice(BigDecimal.valueOf(StringUtil.isEmpty(insbCarinfoHis.getTaxprice()) ? 0.00 : insbCarinfoHis.getTaxprice())
				.add(BigDecimal.valueOf(StringUtil.isEmpty(insbCarmodelinfohis.getTaxprice()) ? 0.0 : insbCarmodelinfohis.getTaxprice())).setScale(5, RoundingMode.HALF_UP).doubleValue());// 含税价=购置价+税额
		// 新车购置价
		carInfo.setUsingnature(insbCarinfoHis.getCarproperty());// 使用性质
		carInfo.setVehicleno(insbCarinfoHis.getCarlicenseno());// 车牌号
		if (insbCarmodelinfohis.getJgVehicleType() != null)
			carInfo.setVehicletype(String.valueOf(insbCarmodelinfohis.getJgVehicleType()));// 车辆类型
		carInfo.setVehicleframeno(StringUtil.isEmpty(insbCarinfoHis.getVincode()) ? "" : insbCarinfoHis.getVincode());// 车架号
		carInfo.setDrivendistancerange(insbCarinfoHis.getMileage());// 平均行驶里程
		carModel.setAnalogyprice(insbCarmodelinfohis.getAnalogyprice());// 不含税类比价
		carModel.setAnalogytaxprice(insbCarmodelinfohis.getAnalogytaxprice());// 含税类比价
		carModel.setBrandname(insbCarmodelinfohis.getBrandname());// 车辆品牌
		// carModel.setCarmodelCode();//车型编码
		// carModel.setCarmodelsource();//车型来源
		// hxx 排量报错
		carModel.setDisplacement(BigDecimal.valueOf(insbCarmodelinfohis.getDisplacement() == null ? 0.00 : insbCarmodelinfohis.getDisplacement()).setScale(3, RoundingMode.HALF_UP).toString());// //发动机排量
		carModel.setFactoryname(insbCarmodelinfohis.getFactoryname());// 生产厂商
		// carModel.setFamilyid();//车系代码
		carModel.setFamilyname(insbCarmodelinfohis.getFamilyname());// 车系名称
		if (insbCarmodelinfohis.getFullweight() != null)
			carModel.setFullweight(String.valueOf(insbCarmodelinfohis.getFullweight()));// 整备质量
		carModel.setGearbox(insbCarmodelinfohis.getGearbox());// 变速器
		// carModel.setGearboxtype();//驱动形式
		if (insbCarmodelinfohis.getJgVehicleType() != null)
			carModel.setJqvehicletype(String.valueOf(insbCarmodelinfohis.getJgVehicleType()));// 交强险车辆类型
		carModel.setMaketdate(insbCarmodelinfohis.getListedyear());// 上市年月
		// carModel.setManufacturer();//产地来源
		if (insbCarmodelinfohis.getUnwrtweight() != null)
			carModel.setModelLoads(String.valueOf(insbCarmodelinfohis.getUnwrtweight()));// 核定载质量
		carModel.setPrice(BigDecimal.valueOf(StringUtil.isEmpty(insbCarmodelinfohis.getTaxprice()) ? 0.00 : insbCarmodelinfohis.getTaxprice()).setScale(5, RoundingMode.HALF_UP).doubleValue());// 新车购置价（不含税）
		if (insbCarmodelinfohis.getSeat() != null)
			carModel.setSeat(String.valueOf(insbCarmodelinfohis.getSeat()));// 座位数
		// carModel.setSupplierId();//保险公司
		if (insbCarmodelinfohis.getJgVehicleType() != null)
			carModel.setSyvehicletype(String.valueOf(insbCarmodelinfohis.getJgVehicleType()));// 交强险车辆类型
		// carModel.setSerchtimes();//查询次数
		carModel.setTaxprice(BigDecimal.valueOf(StringUtil.isEmpty(insbCarinfoHis.getTaxprice()) ? 0.00 : insbCarinfoHis.getTaxprice())
				.add(BigDecimal.valueOf(StringUtil.isEmpty(insbCarmodelinfohis.getTaxprice()) ? 0.00 : insbCarmodelinfohis.getTaxprice())).setScale(5, RoundingMode.HALF_UP).doubleValue());// 新车购置价（含税）
		// carModel.setType();//车辆模型类型
		// carModel.setVehiclecode();//品牌型号

		carModel.setVehiclename(insbCarmodelinfohis.getStandardname());// 车型全称
		carModel.setVehicleno(insbCarinfoHis.getCarlicenseno());
		// carModel.setYearstyle();//年款

		return map;
	}

	public Map<String, Object> getFlowInfoCode(String taskStatus) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			int code = Integer.parseInt(taskStatus);
			if (code <= 7 && code >= 0) {
				map.put("code", FlowInfo.values()[code].getCode());
				map.put("codename", FlowInfo.values()[code].getDesc());
				map.put("taskType", "quote");
			}else if (code <= 15 && code >= 8) {
				map.put("code", FlowInfo.values()[code + 2].getCode());
				map.put("codename", FlowInfo.values()[code + 2].getDesc());
				map.put("taskType", "insure");
			}else if (code <= 18 && code >= 16) {
				map.put("code", FlowInfo.values()[code + 4].getCode());
				map.put("codename", FlowInfo.values()[code + 4].getDesc());
				map.put("taskType", "pay");
			}else if (code <= 21 && code >= 19) {
				map.put("code", FlowInfo.values()[code + 6].getCode());
				map.put("codename", FlowInfo.values()[code + 6].getDesc());
				map.put("taskType", "approved");
			}else if (code <= 35 && code >= 30){
				map.put("code", FlowInfo.values()[code + 4].getCode());
				map.put("codename", FlowInfo.values()[code + 4].getDesc());
				map.put("taskType", "autoinsure");
			}

		} catch (Exception e) {

			if ("A".equalsIgnoreCase(taskStatus)) {
				map.put("code", FlowInfo.quoteover.getCode());
				map.put("codename", FlowInfo.quoteover.getDesc());
				map.put("taskType", "quote");
			}
			if ("B".equalsIgnoreCase(taskStatus)) {
				map.put("code", FlowInfo.underwritingover.getCode());
				map.put("codename", FlowInfo.underwritingover.getDesc());
				map.put("taskType", "insure");
			}
			if ("C".equalsIgnoreCase(taskStatus)) {
				map.put("code", FlowInfo.payover.getCode());
				map.put("codename", FlowInfo.payover.getDesc());
				map.put("taskType", "pay");
			}
			if ("D".equalsIgnoreCase(taskStatus)) {
				map.put("code", FlowInfo.insover.getCode());
				map.put("codename", FlowInfo.insover.getDesc());
				map.put("taskType", "approved");
			}
			if ("A1".equalsIgnoreCase(taskStatus)) {
				map.put("code", FlowInfo.quotefiled.getCode());
				map.put("codename", FlowInfo.quotefiled.getDesc());
				map.put("taskType", "quotefiled");
			}
			if ("B1".equalsIgnoreCase(taskStatus)) {
				map.put("code", FlowInfo.underwritingoverfiled.getCode());
				map.put("codename", FlowInfo.underwritingoverfiled.getDesc());
				map.put("taskType", "insurefiled");
			}
			if ("C1".equalsIgnoreCase(taskStatus)) {
				map.put("code", FlowInfo.payfiled.getCode());
				map.put("codename", FlowInfo.payfiled.getDesc());
				map.put("taskType", "payfiled");
			}
			if ("D1".equalsIgnoreCase(taskStatus)) {
				map.put("code", FlowInfo.insfiled.getCode());
				map.put("codename", FlowInfo.insfiled.getDesc());
				map.put("taskType", "approvedfiled");
			}
			if ("D2".equalsIgnoreCase(taskStatus)) {
				map.put("code", FlowInfo.insdatanotover.getCode());
				map.put("codename", FlowInfo.insdatanotover.getDesc());
				map.put("taskType", "approvedovernotdata");
			}
			if ("xb".equalsIgnoreCase(taskStatus)) {
				map.put("code", FlowInfo.xb.getCode());
				map.put("codename", FlowInfo.xb.getDesc());
				map.put("taskType", "xb");
			}
		}

		return map;
	}

	private String getAddress(String taskId, String insurancecompanyid, String classType, String property) {//拼凑省份市区
		INSBQuotetotalinfo queryInsbQuotetotalinfo = new INSBQuotetotalinfo();
		queryInsbQuotetotalinfo.setTaskid(taskId);
		INSBQuotetotalinfo dateInsbQuotetotalinfo = insbQuotetotalinfoService.queryOne(queryInsbQuotetotalinfo);
		if (!StringUtil.isEmpty(dateInsbQuotetotalinfo.getInsprovincename()) && !StringUtil.isEmpty(dateInsbQuotetotalinfo.getInscityname())) {
			return InterFaceDefaultValueUtil.getAddress(taskId, insurancecompanyid, classType, property, dateInsbQuotetotalinfo.getInsprovincename() + dateInsbQuotetotalinfo.getInscityname());
		} else {
			return null;
		}

	}

	private String getChildTaskId(String taskId, String inscomde) {
		INSBQuoteinfo dataInsbQuoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(taskId, inscomde);
		if (dataInsbQuoteinfo == null) return "";
		return dataInsbQuoteinfo.getWorkflowinstanceid();
	}

	public void insertOrUpdateFlowError(INSBFlowinfo flowinfo, JSONObject errorInfoJson) throws Exception {
		INSBFlowerror dataInsbFlowerror = new INSBFlowerror();
		dataInsbFlowerror.setOperator("admin");
		dataInsbFlowerror.setTaskid(flowinfo.getTaskid());
		dataInsbFlowerror.setInscomcode(flowinfo.getInscomcode());
		dataInsbFlowerror.setFlowcode(flowinfo.getFlowcode());
		dataInsbFlowerror.setFlowname(flowinfo.getFlowname());
		dataInsbFlowerror.setFiroredi(flowinfo.getFiroredi());
		dataInsbFlowerror.setTaskstatus(flowinfo.getTaskstatus());
		if (!StringUtil.isEmpty(errorInfoJson.get("errordesc"))) {
			if (errorInfoJson.get("errordesc").toString().length() > 200) {
				dataInsbFlowerror.setErrordesc(errorInfoJson.get("errordesc").toString().substring(0, 200));
			} else {
				dataInsbFlowerror.setErrordesc(errorInfoJson.get("errordesc").toString());
			}
		}
		if (!StringUtil.isEmpty(errorInfoJson.get("errorcode"))) {
			dataInsbFlowerror.setErrorcode(errorInfoJson.get("errorcode").toString());
		}
		if (!StringUtil.isEmpty(errorInfoJson.get("stop"))) {
			dataInsbFlowerror.setResult(errorInfoJson.get("stop").toString());
		}
		dataInsbFlowerror.setCreatetime(new Date());
		insbFlowerrorService.insert(dataInsbFlowerror);
	}

	@Override
	public void insertOrUpdateFlowError(INSBFlowinfo flowinfo, String errorInfoJson) {
		INSBFlowerror dataInsbFlowerror = new INSBFlowerror();
		dataInsbFlowerror.setOperator("admin");
		dataInsbFlowerror.setTaskid(flowinfo.getTaskid());
		dataInsbFlowerror.setInscomcode(flowinfo.getInscomcode());
		dataInsbFlowerror.setFlowcode(flowinfo.getFlowcode());
		dataInsbFlowerror.setFlowname(flowinfo.getFlowname());
		dataInsbFlowerror.setFiroredi(flowinfo.getFiroredi());
		dataInsbFlowerror.setTaskstatus(flowinfo.getTaskstatus());
		dataInsbFlowerror.setErrordesc(errorInfoJson);
		dataInsbFlowerror.setCreatetime(new Date());
		insbFlowerrorService.insert(dataInsbFlowerror);
	}

	private void insertOrUpdateOperatorComment(String taskId, String insComCode, String taskType, String errorCode, String errorDesc, String taskStatus) {
		/**
		 * taskcode字典：
		 * 3-EDI报价；4-精灵报价；32-规则报价；6-人工调整；7-人工规则报价；8-人工报价；13-报价退回；14-选择投保
		 * ；31-人工回写； 16-EDI核保；17-精灵核保；18-人工核保；19-核保退回；25-edi承保，26-精灵承保，20-支付
		 */
		String subTaskId = this.getChildTaskId(taskId, insComCode);
		String taskStatusName = (String) this.getFlowInfoCode(taskStatus).get("codename");
		String taskcode = null;
		String tarckId = null;
		String tarckType = null;
		String from = "robot".equals(taskType) ? "精灵" : "EDI";
		if (taskStatusName.contains("承保")) {
			INSBWorkflowmain queryInsbWorkflowmain = new INSBWorkflowmain();
			queryInsbWorkflowmain.setInstanceid(taskId);
			queryInsbWorkflowmain = insbWorkflowmainService.queryOne(queryInsbWorkflowmain);
			if (!StringUtil.isEmpty(queryInsbWorkflowmain)) {
				taskcode = queryInsbWorkflowmain.getTaskcode();
			}
			INSBWorkflowmaintrack queryInsbWorkflowmaintrack = new INSBWorkflowmaintrack();
			queryInsbWorkflowmaintrack.setInstanceid(taskId);
			queryInsbWorkflowmaintrack.setTaskcode(taskcode);
			List<INSBWorkflowmaintrack> insbWorkflowmaintracks = insbWorkflowmaintrackService.queryList(queryInsbWorkflowmaintrack);
			if (insbWorkflowmaintracks.size() <= 0) {
				return;
			} else {
				queryInsbWorkflowmaintrack = insbWorkflowmaintracks.get(0);
				tarckId = queryInsbWorkflowmaintrack.getId();
			}
			tarckType = "1";
		} else {
			INSBWorkflowsub queryInsbWorkflowsub = new INSBWorkflowsub();
			queryInsbWorkflowsub.setInstanceid(subTaskId);
			queryInsbWorkflowsub = insbWorkflowsubService.queryOne(queryInsbWorkflowsub);
			if (!StringUtil.isEmpty(queryInsbWorkflowsub)) {
				taskcode = queryInsbWorkflowsub.getTaskcode();
			}
			INSBWorkflowsubtrack queryInsbWorkflowsubtrack = new INSBWorkflowsubtrack();
			queryInsbWorkflowsubtrack.setMaininstanceid(taskId);
			queryInsbWorkflowsubtrack.setInstanceid(subTaskId);
			queryInsbWorkflowsubtrack.setTaskcode(taskcode);
			List<INSBWorkflowsubtrack> insbWorkflowsubtracks = insbWorkflowsubtrackService.queryList(queryInsbWorkflowsubtrack);
			if (insbWorkflowsubtracks.size() <= 0) {
				return;
			} else {
				queryInsbWorkflowsubtrack = insbWorkflowsubtracks.get(0);
				tarckId = queryInsbWorkflowsubtrack.getId();
			}
			tarckType = "2";
		}
		if (StringUtil.isEmpty(taskcode)) {
			return;
		}
		if (!StringUtil.isEmpty(tarckId) && !StringUtil.isEmpty(tarckType)) {
			INSBOperatorcomment queryInsbOperatorcomment = new INSBOperatorcomment();
			queryInsbOperatorcomment.setTrackid(tarckId);
			queryInsbOperatorcomment.setTracktype(Integer.valueOf(tarckType));
			List<INSBOperatorcomment> insbOperatorcomments = insbOperatorcommentService.queryList(queryInsbOperatorcomment);

			if (errorDesc != null && errorDesc.length() > 950) {
				errorDesc = errorDesc.substring(0, 950);
			}

			if (insbOperatorcomments.size() <= 0) {
				INSBOperatorcomment newInsbOperatorcomment = new INSBOperatorcomment();
				newInsbOperatorcomment.setOperator("admin");
				newInsbOperatorcomment.setCreatetime(new Date());
				newInsbOperatorcomment.setTrackid(tarckId);
				newInsbOperatorcomment.setTracktype(Integer.valueOf(tarckType));
				newInsbOperatorcomment.setCommentcontent(from + "错误编码:" + errorCode + ",错误信息:" + errorDesc);
				insbOperatorcommentService.insert(newInsbOperatorcomment);
			} else {
				queryInsbOperatorcomment = insbOperatorcomments.get(0);
				queryInsbOperatorcomment.setCommentcontent(from + "错误编码:" + errorCode + ",错误信息:" + errorDesc);
				insbOperatorcommentService.updateById(queryInsbOperatorcomment);
			}
		}

	}

	public String flowererrorType(String errordesc) {
		if (errordesc.matches(".*(重复投保).*")) {
			return "1";
		} else {
			return "99999";
		}
	}

	private String getSelectItem(String riskCode, String amount) throws Exception {
		if (amount.contains(".")) {
			amount = amount.substring(0, amount.indexOf("."));
		}
		if (riskCode.equalsIgnoreCase("SpecifyingPlantCla")) {
			return "[{\"TYPE\":\"03\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("NewEquipmentIns")) {
			return "[{\"TYPE\":\"03\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("VehicleDemageMissedThirdPartyCla")) {
			return "[{\"TYPE\":\"03\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("GlassIns")) {
			if ("1".equals(amount)) {
				return "[{\"TYPE\":\"02\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"进口玻璃\",\"VALUE\":\"2\"}}]";
			} else if ("0".equals(amount)) {
				return "[{\"TYPE\":\"02\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"国产玻璃\",\"VALUE\":\"1\"}}]";
			} else {
				return "";
			}
		}
		if (riskCode.equalsIgnoreCase("MirrorLightIns")) {
			if ("1".equals(amount)) {
				return "[{\"TYPE\":\"02\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"进口玻璃\",\"VALUE\":\"2\"}}]";
			} else if ("0".equals(amount)) {
				return "[{\"TYPE\":\"02\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"国产玻璃\",\"VALUE\":\"1\"}}]";
			} else {
				return "";
			}
		}
		if (riskCode.equalsIgnoreCase("PassengerIns")) {
			int amoutt = Integer.valueOf(amount);
			if (amoutt < 10000 && 0 < amoutt) {
				return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"元/座\",\"KEY\":\"" + amoutt + "/座\",\"VALUE\":\"" + amoutt + "\"}}]";
			} else {
				return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"元/座\",\"KEY\":\"" + amoutt / 10000 + "万/座\",\"VALUE\":\"" + amoutt + "\"}}]";
			}
		}
		if (riskCode.equalsIgnoreCase("DriverIns")) {
			int amoutt = Integer.valueOf(amount);
			if (amoutt < 10000 && 0 < amoutt) {
				return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"元\",\"KEY\":\"" + amoutt + "\",\"VALUE\":\"" + amoutt + "\"}}]";
			} else {
				return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"元\",\"KEY\":\"" + amoutt / 10000 + "万\",\"VALUE\":\"" + amoutt + "\"}}]";
			}
		}
		// to do:
		if (riskCode.equalsIgnoreCase("VehicleCompulsoryIns")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("VehicleDemageIns")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("LossOfBaggageIns")) {
			return "[{\"TYPE\":\"03\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("TrainnigCarCla")) {
			return "[{\"TYPE\":\"03\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("VehicleSuspendedIns")) {
			return "[{\"TYPE\":\"03\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("SpecialVehicleExtensionIns")) {
			return "[{\"TYPE\":\"03\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("SpecifyRepairIns")) {
			return "[{\"TYPE\":\"03\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("NcfVehicleDemageIns")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("NcfThirdPartyIns")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("NcfDriverIns")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("NcfPassengerIns")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("NcfTheftIns")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("NcfCombustionIns")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("NcfScratchIns")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("NcfWadingIns")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("NcfNewEquipmentIns")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("NcfGoodsOnVehicleIns")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("SpecialCarDemageIns")) {
			return "[{\"TYPE\":\"03\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("NcfDriverPassengerIns")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("Ncfaddtionalclause")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("NcfMirrorLightIns")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("NcfBasicClause")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("NcfClause")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("NcfCompensationForMentalDistressIns")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("CompulsoryStamTax")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"代缴\",\"VALUE\":\"代缴\"}}]";
		}
		if (riskCode.equalsIgnoreCase("VehicleTax")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"代缴\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("VehicleTaxOverdueFine")) {
			return "代缴";
		}
		if (riskCode.equalsIgnoreCase("CommercialStamTax")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"代缴\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("CompensationDuringRepairIns")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"元/天\",\"KEY\":\"" + amount + "元/天\",\"VALUE\":\"" + amount + "\"}}]";
		}
		if (riskCode.equalsIgnoreCase("ThirdPartyIns")) {
			int amoutt = Integer.valueOf(amount);
			if (amoutt < 10000 && 0 < amoutt) {
				return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"元\",\"KEY\":\"" + amoutt + "\",\"VALUE\":\"" + amoutt + "\"}}]";
			} else {
				return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"元\",\"KEY\":\"" + amoutt / 10000 + "万\",\"VALUE\":\"" + amoutt + "\"}}]";
			}
		}
		if (riskCode.equalsIgnoreCase("ScratchIns")) {
			int amoutt = Integer.valueOf(amount);
			if (amoutt < 10000 && 0 < amoutt) {
				return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"元\",\"KEY\":\"" + amoutt + "\",\"VALUE\":\"" + amoutt + "\"}}]";
			} else {
				return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"元\",\"KEY\":\"" + amoutt / 10000 + "万\",\"VALUE\":\"" + amoutt + "\"}}]";
			}
		}
		if (riskCode.equalsIgnoreCase("GoodsOnVehicleIns")) {
			int amoutt = Integer.valueOf(amount);
			if (amoutt < 10000 && 0 < amoutt) {
				return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"元\",\"KEY\":\"" + amoutt + "\",\"VALUE\":\"" + amoutt + "\"}}]";
			} else {
				return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"元\",\"KEY\":\"" + amoutt / 10000 + "万\",\"VALUE\":\"" + amoutt + "\"}}]";
			}
		}
		if (riskCode.equalsIgnoreCase("WadingIns")) {
			return "[{\"TYPE\":\"03\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"投保\",\"VALUE\":\"1\"}}]";
		}
		if (riskCode.equalsIgnoreCase("TheftIns")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"指定保额投保\",\"VALUE\":\"2\"}}]";
		}
		if (riskCode.equalsIgnoreCase("CombustionIns")) {
			return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"指定保额投保\",\"VALUE\":\"2\"}}]";
		}
		if (riskCode.equalsIgnoreCase("AccidentfranchiseCla")) {
			if ("1".equals(amount))
				return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"事故责任免赔率I\",\"VALUE\":\"1\"}}]";
			if ("2".equals(amount))
				return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"事故责任免赔率II\",\"VALUE\":\"2\"}}]";
			if ("3".equals(amount))
				return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"\",\"KEY\":\"事故责任免赔率III\",\"VALUE\":\"3\"}}]";
		}
		if (riskCode.equalsIgnoreCase("CompensationForMentalDistressIns")) {
			int amoutt = Integer.valueOf(amount);
			if (amoutt < 10000 && 0 < amoutt) {
				return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"元\",\"KEY\":\"" + amoutt + "\",\"VALUE\":\"" + amoutt + "\"}}]";
			} else {
				return "[{\"TYPE\":\"01\",\"VALUE\":{\"UNIT\":\"元\",\"KEY\":\"" + amoutt / 10000 + "万\",\"VALUE\":\"" + amoutt + "\"}}]";
			}
		}
		return null;
	}

	private String getRiskNameByRiskCode(String riskCode) {
		INSBRiskkindconfig queryInsbRiskkindconfig = new INSBRiskkindconfig();
		queryInsbRiskkindconfig.setRiskkindcode(riskCode);
		queryInsbRiskkindconfig = insbRiskkindconfigService.queryOne(queryInsbRiskkindconfig);
		if (!StringUtil.isEmpty(queryInsbRiskkindconfig)) {
			return queryInsbRiskkindconfig.getRiskkindname();
		}
		return null;
	}

	private String getParentRiskNameByRiskCode(String riskCode) {
		INSBRiskkindconfig queryInsbRiskkindconfig = new INSBRiskkindconfig();
		queryInsbRiskkindconfig.setRiskkindcode(riskCode);
		queryInsbRiskkindconfig = insbRiskkindconfigService.queryOne(queryInsbRiskkindconfig);
		if (!StringUtil.isEmpty(queryInsbRiskkindconfig)) {
			return queryInsbRiskkindconfig.getPrekindcode();
		}
		return "";
	}

	private String getInRiskTypeByRiskCode(String riskCode) {
		INSBRiskkindconfig queryInsbRiskkindconfig = new INSBRiskkindconfig();
		queryInsbRiskkindconfig.setRiskkindcode(riskCode);
		queryInsbRiskkindconfig = insbRiskkindconfigService.queryOne(queryInsbRiskkindconfig);
		if (!StringUtil.isEmpty(queryInsbRiskkindconfig)) {
			return queryInsbRiskkindconfig.getType();
		}
		return null;
	}

	private String getTheRealAmout(String amount, String type) {
		if ("1".equals(type)) {
			String newamount = "";
			if (amount.contains(".")) {
				newamount = amount.substring(0, amount.indexOf("."));
				int t = Integer.valueOf(newamount);
				if (t <= 0) {
					return "1.00";
				} else {
					return amount;
				}
			}
		} else {
		}
		return amount;
	}

	private void logInfo(List<CarModelInfoBean> judgeList, String taskId, String inscomcode) {
		for (CarModelInfoBean logCarModelInfoBean : judgeList) {
			LogUtil.info("==筛选年款=处理完的车型列表=：taskid=" + taskId + ",inscomcode=" + inscomcode + "" + "=车型名称=" + logCarModelInfoBean.getVehiclename() + ",含税价=" + logCarModelInfoBean.getTaxprice()
					+ ",不含税价=" + logCarModelInfoBean.getPrice() + ",含税类比价=" + logCarModelInfoBean.getAnalogytaxprice() + ",不含税类比价=" + logCarModelInfoBean.getAnalogyprice() + ",初等日期="
					+ logCarModelInfoBean.getMaketdate());
		}
	}

	private List<CarModelInfoBean> sortByRegDate(String value, List<CarModelInfoBean> judgeList, String taskId, String inscomcode) throws Exception {
		Collections.sort(judgeList, new Comparator<CarModelInfoBean>() {
			public int compare(CarModelInfoBean arg0, CarModelInfoBean arg1) {
				return arg0.getTaxprice().compareTo(arg1.getTaxprice());
			}
		});
		LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "=" + value + "==初等日期排序=");
		return judgeList;
	}

	private List<CarModelInfoBean> sortByPrice(String value, List<CarModelInfoBean> judgeList, String taskId, String inscomcode) throws Exception {
		if ("含税价".equalsIgnoreCase(value)) {
			LogUtil.info("==筛选年款=进行排序=：taskid=" + taskId + ",inscomcode=" + inscomcode + "=车辆价格类型:含税价");
			Collections.sort(judgeList, new Comparator<CarModelInfoBean>() {
				public int compare(CarModelInfoBean arg0, CarModelInfoBean arg1) {
					return arg0.getTaxprice().compareTo(arg1.getTaxprice());
				}
			});
		} else if ("不含税价".equalsIgnoreCase(value)) {
			LogUtil.info("==筛选年款=进行排序=：taskid=" + taskId + ",inscomcode=" + inscomcode + "=车辆价格类型:不含税价");
			Collections.sort(judgeList, new Comparator<CarModelInfoBean>() {
				public int compare(CarModelInfoBean arg0, CarModelInfoBean arg1) {
					return arg0.getPrice().compareTo(arg1.getPrice());
				}
			});
		} else if ("含税类比价".equalsIgnoreCase(value)) {
			LogUtil.info("==筛选年款=进行排序=：taskid=" + taskId + ",inscomcode=" + inscomcode + "=车辆价格类型:含税类比价");
			boolean flag = false;
			for (CarModelInfoBean dataBean : judgeList) {
				if (StringUtil.isEmpty(dataBean.getAnalogytaxprice()) || new BigDecimal("0").compareTo(new BigDecimal(dataBean.getAnalogytaxprice())) == 0) {
					flag = true;
					break;
				}
			}
			if (flag) {
				LogUtil.info("==筛选年款=进行排序=：taskid=" + taskId + ",inscomcode=" + inscomcode + "=由于车型中有含税类比价为空的，改为=车辆价格类型:含税价");
				Collections.sort(judgeList, new Comparator<CarModelInfoBean>() {
					public int compare(CarModelInfoBean arg0, CarModelInfoBean arg1) {
						return arg0.getTaxprice().compareTo(arg1.getTaxprice());
					}
				});
			} else {
				Collections.sort(judgeList, new Comparator<CarModelInfoBean>() {
					public int compare(CarModelInfoBean arg0, CarModelInfoBean arg1) {
						return arg0.getAnalogytaxprice().compareTo(arg1.getAnalogytaxprice());
					}
				});
			}
		} else if ("不含税类比价".equalsIgnoreCase(value)) {
			LogUtil.info("==筛选年款=进行排序=：taskid=" + taskId + ",inscomcode=" + inscomcode + "=车辆价格类型:不含税类比价");
			boolean flag = false;
			for (CarModelInfoBean dataBean : judgeList) {
				if (StringUtil.isEmpty(dataBean.getAnalogyprice()) || new BigDecimal("0").compareTo(new BigDecimal(dataBean.getAnalogyprice())) == 0) {
					flag = true;
					break;
				}
			}
			if (flag) {
				LogUtil.info("==筛选年款=进行排序=：taskid=" + taskId + ",inscomcode=" + inscomcode + "=由于车型中有不含税类比价为空的，改为=车辆价格类型:不含税价");
				Collections.sort(judgeList, new Comparator<CarModelInfoBean>() {
					public int compare(CarModelInfoBean arg0, CarModelInfoBean arg1) {
						return arg0.getPrice().compareTo(arg1.getPrice());
					}
				});
			} else {
				Collections.sort(judgeList, new Comparator<CarModelInfoBean>() {
					public int compare(CarModelInfoBean arg0, CarModelInfoBean arg1) {
						return arg0.getAnalogyprice().compareTo(arg1.getAnalogyprice());
					}
				});
			}
		} else {
			LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆价格类型值错误=");
		}
		return judgeList;
	}

	private List<CarModelInfoBean> orderByCarModelSelectTypeAndCarPriceType(JSONObject JudgeJsonObject, List<CarModelInfoBean> judgeList, String taskId, String inscomcode) throws Exception {
		// 一 车款选取方式 "ruleItem.carModelSelectType": 价格最低优先:0,初登最近优先:1,
		// 二 车辆价格类型 "ruleItem.carPriceType": 含税价,不含税价,含税类比价,不含税类比价,
		if (JudgeJsonObject.containsKey("ruleItem.carModelSelectType") && !StringUtil.isEmpty(JudgeJsonObject.get("ruleItem.carModelSelectType"))) {
			if ("0".equals(JudgeJsonObject.get("ruleItem.carModelSelectType").toString().trim())) {
				LogUtil.info("==筛选年款=进行排序=：taskid=" + taskId + ",inscomcode=" + inscomcode + "=车款选取方式:价格最低优先:0");
				if (JudgeJsonObject.containsKey("ruleItem.carPriceType") && !StringUtil.isEmpty(JudgeJsonObject.get("ruleItem.carPriceType"))) {
					judgeList = this.sortByPrice(JudgeJsonObject.get("ruleItem.carPriceType").toString().trim(), judgeList, taskId, inscomcode);
				} else {
					LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆价格类型key不存在=");
				}
			} else if ("1".equals(JudgeJsonObject.get("ruleItem.carModelSelectType").toString().trim())) {
				LogUtil.info("==筛选年款=进行排序=：taskid=" + taskId + ",inscomcode=" + inscomcode + "=车款选取方式:初登最近优先:1");
				judgeList = this.sortByRegDate("", judgeList, taskId, inscomcode);
			} else {
				LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆价格类型_ruleItem.carModelSelectType值错误=");
			}
		} else {
			LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车款选取方式key不存在=");
		}
		return judgeList;
	}

	private String getRealPrice(String taskid,String inscomcode,String tempPrice,String seat,String regDate){
		try{
			INSBCarinfohis queryInsbCarinfohis = commonQuoteinfoService.getCarInfo(taskid, inscomcode);

			if(queryInsbCarinfohis==null){
				LogUtil.info("==筛选年款==实际价值==折扣获取==：taskid=" + taskid + ",inscomcode=" + inscomcode + ",没有查到carinfohis信息");
				return null;
			}
			if(StringUtil.isEmpty(queryInsbCarinfohis.getCarproperty())){
				LogUtil.info("==筛选年款==实际价值==折扣获取==：taskid=" + taskid + ",inscomcode=" + inscomcode + ",车辆使用性质为空");
				return null;
			}
			if(StringUtil.isEmpty(seat)){
				LogUtil.info("==筛选年款==实际价值==折扣获取==：taskid=" + taskid + ",inscomcode=" + inscomcode + ",车辆座位数为空");
				return null;
			}

			double tempRate = 0.0;
			if("1".equals(queryInsbCarinfohis.getCarproperty()) || "11".equals(queryInsbCarinfohis.getCarproperty()) || "10".equals(queryInsbCarinfohis.getCarproperty())){
				if(Integer.parseInt(seat)>=10){
					tempRate = Double.valueOf("0.009");
				}else if(Integer.parseInt(seat)<=9){
					tempRate = Double.valueOf("0.006");
				}
			}else if("2".equals(queryInsbCarinfohis.getCarproperty()) || "6".equals(queryInsbCarinfohis.getCarproperty())){
				tempRate = Double.valueOf("0.011");
			}else if("3".equals(queryInsbCarinfohis.getCarproperty()) || "4".equals(queryInsbCarinfohis.getCarproperty()) || "5".equals(queryInsbCarinfohis.getCarproperty()) || "12".equals(queryInsbCarinfohis.getCarproperty()) || "15".equals(queryInsbCarinfohis.getCarproperty()) || "16".equals(queryInsbCarinfohis.getCarproperty())){
				tempRate = Double.valueOf("0.009");
			}else{
				LogUtil.info("==筛选年款==实际价值==折扣获取==：taskid=" + taskid + ",inscomcode=" + inscomcode + ",车辆使用性质值错误，值为：" + queryInsbCarinfohis.getCarproperty());
				return null;
			}

			INSBLastyearinsureinfo dataInsbLastyearinsureinfo = insbRulequerycarinfoDao.queryLastYearClainInfo(taskid);
			String tempDate = "";
			if(dataInsbLastyearinsureinfo==null){
				tempDate = DateUtil.toString(DateUtil.addDay(new Date(), 1), DateUtil.Format_Date);
				LogUtil.info("==筛选年款==实际价值==使用月份==：taskid=" + taskid + ",inscomcode=" + inscomcode + ",该单没有商业险,则默认T+1:" + tempDate);
			}else if(StringUtil.isEmpty(dataInsbLastyearinsureinfo.getSystartdate())){
				tempDate = DateUtil.toString(DateUtil.addDay(new Date(), 1), DateUtil.Format_Date);
				LogUtil.info("==筛选年款==实际价值==使用月份==：taskid=" + taskid + ",inscomcode=" + inscomcode + ",商业险起保日期为空，则默认T+1:" + tempDate);
			}else if(!StringUtil.isEmpty(dataInsbLastyearinsureinfo.getSystartdate())){
				tempDate = LocalDate.parse(dataInsbLastyearinsureinfo.getSystartdate().substring(0,10)).plus(1, ChronoUnit.YEARS).toString();
				LogUtil.info("==筛选年款==实际价值==使用月份==：taskid=" + taskid + ",inscomcode=" + inscomcode + ",商业险起保日期为：" + tempDate);
			}else{
				tempDate = DateUtil.toString(DateUtil.addDay(new Date(), 1), DateUtil.Format_Date);
				LogUtil.info("==筛选年款==实际价值==使用月份==：taskid=" + taskid + ",inscomcode=" + inscomcode + ",该不会,则默认T+1:" + tempDate);
			}

			if(StringUtil.isEmpty(regDate)){
				LogUtil.info("==筛选年款==实际价值==使用月份==：taskid=" + taskid + ",inscomcode=" + inscomcode + ",传入的初登日期为空");
				return null;
			}else if(regDate.length()<4){
				LogUtil.info("==筛选年款==实际价值==使用月份==：taskid=" + taskid + ",inscomcode=" + inscomcode + ",传入的初登日期格式不对：" + regDate);
				return null;
			}else if(regDate.length()==4){
				LogUtil.info("==筛选年款==实际价值==使用月份==：taskid=" + taskid + ",inscomcode=" + inscomcode + ",传入的初登日期：" + regDate + ",则默认为：" + regDate + "0101");
				regDate = regDate + "0101";
			}else if(regDate.length()==5){
				LogUtil.info("==筛选年款==实际价值==使用月份==：taskid=" + taskid + ",inscomcode=" + inscomcode + ",传入的初登日期格式不对：" + regDate + ",则默认为：" + regDate.substring(0, 4) + "0101");
				regDate = regDate.substring(0, 4) + "0101";
			}else if(regDate.length()==6){
				LogUtil.info("==筛选年款==实际价值==使用月份==：taskid=" + taskid + ",inscomcode=" + inscomcode + ",传入的初登日期：" + regDate + ",则默认为：" + regDate + "01" );
				regDate = regDate + "01";
			}else if(regDate.length()==7){
				LogUtil.info("==筛选年款==实际价值==使用月份==：taskid=" + taskid + ",inscomcode=" + inscomcode + ",传入的初登日期格式不对：" + regDate + ",则默认为：" + regDate.substring(0, 6) + "01");
				regDate = regDate.substring(0, 6) + "01";
			}else if(regDate.length()==8){
				LogUtil.info("==筛选年款==实际价值==使用月份==：taskid=" + taskid + ",inscomcode=" + inscomcode + ",传入的初登日期：" + regDate);
			}else if(regDate.length()>8){
				LogUtil.info("==筛选年款==实际价值==使用月份==：taskid=" + taskid + ",inscomcode=" + inscomcode + ",传入的初登日期：" + regDate + ",则用：" + regDate.substring(0, 8));
				regDate = regDate.substring(0, 8);
			}

			LocalDate startDate = LocalDate.parse(regDate.substring(0, 4)+"-"+regDate.substring(4, 6)+"-"+regDate.substring(6, 8));
			LocalDate endDate = LocalDate.parse(tempDate);
			Period period = Period.between(startDate, endDate);
			int userMonths = period.getYears() * 12 + period.getMonths();
			LogUtil.info("==筛选年款==实际价值==使用月份==：taskid=" + taskid + ",inscomcode=" + inscomcode + ",使用月份是：" + userMonths);
			String realPrice = String.valueOf((new BigDecimal(tempPrice).multiply(new BigDecimal(1).subtract(new BigDecimal(tempRate).multiply(new BigDecimal(userMonths))))).setScale(0, BigDecimal.ROUND_HALF_UP));
			LogUtil.info("==筛选年款==实际价值====：taskid=" + taskid + ",inscomcode=" + inscomcode + ",计算后的实际价值是：" + realPrice);
			return realPrice;
		}catch(Exception e){
			LogUtil.info("==筛选年款==实际价值====：taskid=" + taskid + ",inscomcode=" + inscomcode + ",出错了," + e.getMessage());
			return null;
		}
	}


	private List<CarModelInfoBean> deleteBadCarInfoByPrice(String pointPrice, JSONObject JudgeJsonObject, List<CarModelInfoBean> judgeList, String taskId, String inscomcode, String regDate,String seat) throws Exception {
		if (!StringUtil.isEmpty(pointPrice)) {
			if (JudgeJsonObject.containsKey("car.specific.minPriceRate") && !StringUtil.isEmpty(JudgeJsonObject.get("car.specific.minPriceRate"))
					&& JudgeJsonObject.containsKey("car.specific.maxPriceRate") && !StringUtil.isEmpty(JudgeJsonObject.get("car.specific.maxPriceRate"))) {
				LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==指定车价：" + pointPrice + ",浮动价下限比率:" + JudgeJsonObject.get("car.specific.minPriceRate").toString()
						+ ",浮动价上限比率:" + JudgeJsonObject.get("car.specific.maxPriceRate").toString() + ",进行实际价值计算用：" + JudgeJsonObject.get("ruleItem.carPriceType"));
				String carPriceType = String.valueOf(JudgeJsonObject.get("ruleItem.carPriceType"));
				for (int i = judgeList.size() - 1; 0 <= i; i--) {
					CarModelInfoBean judgeBean = new CarModelInfoBean();
					judgeBean = judgeList.get(i);
					if("含税价".equalsIgnoreCase(carPriceType)){
						if(StringUtil.isEmpty(judgeBean.getTaxprice())||new BigDecimal("0").compareTo(new BigDecimal(judgeBean.getTaxprice()))==0){
							LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + judgeBean.toString() +",含税价为" + judgeBean.getTaxprice() + ",则用不含税价进行计算（新车直购价）");
							String tempRealPrice = getRealPrice(taskId, inscomcode, String.valueOf(judgeBean.getPrice()), seat, regDate);
							if(StringUtil.isEmpty(tempRealPrice)){
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + judgeBean.toString() +",实际价值计算失败，则用不含税价进行计算（新车直购价）");
								tempRealPrice = String.valueOf(judgeBean.getPrice());
							}
							if (new BigDecimal(pointPrice).compareTo(new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString()))) >= 0
									&& new BigDecimal(pointPrice).compareTo(new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString()))) <= 0) {
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==指定车价：" + pointPrice + ",车价是：" + tempRealPrice + ",浮动价下限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString())) + ",浮动价上限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString())) + "符合规则");
							} else {
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==指定车价：" + pointPrice + ",车价是：" + tempRealPrice + ",浮动价下限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString())) + ",浮动价上限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString())) + "不符合规则");
								judgeList.remove(i);
							}
						}else{
							String tempRealPrice = getRealPrice(taskId, inscomcode, String.valueOf(judgeBean.getTaxprice()), seat, regDate);
							if(StringUtil.isEmpty(tempRealPrice)){
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + judgeBean.toString() +",实际价值计算失败，则用含税价进行计算");
								tempRealPrice = String.valueOf(judgeBean.getTaxprice());
							}
							if (new BigDecimal(pointPrice).compareTo(new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString()))) >= 0
									&& new BigDecimal(pointPrice).compareTo(new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString()))) <= 0) {
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==指定车价：" + pointPrice + ",车价是：" + tempRealPrice + ",浮动价下限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString())) + ",浮动价上限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString())) + "符合规则");
							} else {
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==指定车价：" + pointPrice + ",车价是：" + tempRealPrice + ",浮动价下限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString())) + ",浮动价上限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString())) + "不符合规则");
								judgeList.remove(i);
							}
						}
					}
					if("不含税价".equalsIgnoreCase(carPriceType)){
						String tempRealPrice = getRealPrice(taskId, inscomcode, String.valueOf(judgeBean.getPrice()), seat, regDate);
						if(StringUtil.isEmpty(tempRealPrice)){
							LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + judgeBean.toString() +",实际价值计算失败，则用不含税价进行计算（新车直购价）");
							tempRealPrice = String.valueOf(judgeBean.getPrice());
						}
						if (new BigDecimal(pointPrice).compareTo(new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString()))) >= 0
								&& new BigDecimal(pointPrice).compareTo(new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString()))) <= 0) {
							LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==指定车价：" + pointPrice + ",车价是：" + tempRealPrice + ",浮动价下限车价:"
									+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString())) + ",浮动价上限车价:"
									+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString())) + "符合规则");
						} else {
							LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==指定车价：" + pointPrice + ",车价是：" + tempRealPrice + ",浮动价下限车价:"
									+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString())) + ",浮动价上限车价:"
									+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString())) + "不符合规则");
							judgeList.remove(i);
						}
					}
					if("含税类比价".equalsIgnoreCase(carPriceType)){
						if(StringUtil.isEmpty(judgeBean.getAnalogytaxprice())||new BigDecimal("0").compareTo(new BigDecimal(judgeBean.getAnalogytaxprice()))==0){
							LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + judgeBean.toString() +",含税类比价为" + judgeBean.getAnalogytaxprice() + ",则用含税价进行计算");
							String tempRealPrice = getRealPrice(taskId, inscomcode, String.valueOf(judgeBean.getTaxprice()), seat, regDate);
							if(StringUtil.isEmpty(tempRealPrice)){
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + judgeBean.toString() +",实际价值计算失败，则用含税价进行计算（新车直购价）");
								tempRealPrice = String.valueOf(judgeBean.getTaxprice());
							}
							if (new BigDecimal(pointPrice).compareTo(new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString()))) >= 0
									&& new BigDecimal(pointPrice).compareTo(new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString()))) <= 0) {
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==指定车价：" + pointPrice + ",车价是：" + tempRealPrice + ",浮动价下限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString())) + ",浮动价上限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString())) + "符合规则");
							} else {
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==指定车价：" + pointPrice + ",车价是：" + tempRealPrice + ",浮动价下限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString())) + ",浮动价上限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString())) + "不符合规则");
								judgeList.remove(i);
							}
						}else{
							String tempRealPrice = getRealPrice(taskId, inscomcode, String.valueOf(judgeBean.getAnalogytaxprice()), seat, regDate);
							if(StringUtil.isEmpty(tempRealPrice)){
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + judgeBean.toString() +",实际价值计算失败，则用含税类比价进行计算");
								tempRealPrice = String.valueOf(judgeBean.getAnalogytaxprice());
							}
							if (new BigDecimal(pointPrice).compareTo(new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString()))) >= 0
									&& new BigDecimal(pointPrice).compareTo(new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString()))) <= 0) {
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==指定车价：" + pointPrice + ",车价是：" + tempRealPrice + ",浮动价下限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString())) + ",浮动价上限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString())) + "符合规则");
							} else {
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==指定车价：" + pointPrice + ",车价是：" + tempRealPrice + ",浮动价下限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString())) + ",浮动价上限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString())) + "不符合规则");
								judgeList.remove(i);
							}
						}
					}
					if("不含税类比价".equalsIgnoreCase(carPriceType)){
						if(StringUtil.isEmpty(judgeBean.getAnalogyprice())||new BigDecimal("0").compareTo(new BigDecimal(judgeBean.getAnalogyprice()))==0){
							LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + judgeBean.toString() +",不含税类比价为" + judgeBean.getAnalogyprice() + ",则用不含税价进行计算");
							String tempRealPrice = getRealPrice(taskId, inscomcode, String.valueOf(judgeBean.getPrice()), seat, regDate);
							if(StringUtil.isEmpty(tempRealPrice)){
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + judgeBean.toString() +",实际价值计算失败，则用含税价进行计算（新车直购价）");
								tempRealPrice = String.valueOf(judgeBean.getPrice());
							}
							if (new BigDecimal(pointPrice).compareTo(new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString()))) >= 0
									&& new BigDecimal(pointPrice).compareTo(new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString()))) <= 0) {
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==指定车价：" + pointPrice + ",车价是：" + tempRealPrice + ",浮动价下限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString())) + ",浮动价上限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString())) + "符合规则");
							} else {
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==指定车价：" + pointPrice + ",车价是：" + tempRealPrice + ",浮动价下限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString())) + ",浮动价上限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString())) + "不符合规则");
								judgeList.remove(i);
							}
						}else{
							String tempRealPrice = getRealPrice(taskId, inscomcode, String.valueOf(judgeBean.getAnalogyprice()), seat, regDate);
							if(StringUtil.isEmpty(tempRealPrice)){
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + judgeBean.toString() +",实际价值计算失败，则用不含税类比价进行计算");
								tempRealPrice = String.valueOf(judgeBean.getAnalogyprice());
							}
							if (new BigDecimal(pointPrice).compareTo(new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString()))) >= 0
									&& new BigDecimal(pointPrice).compareTo(new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString()))) <= 0) {
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==指定车价：" + pointPrice + ",车价是：" + tempRealPrice + ",浮动价下限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString())) + ",浮动价上限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString())) + "符合规则");
							} else {
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==指定车价：" + pointPrice + ",车价是：" + tempRealPrice + ",浮动价下限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.minPriceRate").toString())) + ",浮动价上限车价:"
										+ new BigDecimal(tempRealPrice).multiply(new BigDecimal(JudgeJsonObject.get("car.specific.maxPriceRate").toString())) + "不符合规则");
								judgeList.remove(i);
							}
						}
					}
				}
			} else {
				LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==指定车价：" + pointPrice + ",浮动价上限比率,浮动价下限比率 的值不存在");
			}
		} else {
			LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==没有指定车价，跳过浮动价上/下限比率限制");
		}
		return judgeList;
	}

	private List<CarModelInfoBean> deleteBadCarInfoByRegDate(String pointPrice, JSONObject JudgeJsonObject, List<CarModelInfoBean> judgeList, String taskId, String inscomcode, String regDate) throws Exception {
		if (JudgeJsonObject.containsKey("ruleItem.inMakeDate") && !StringUtil.isEmpty(JudgeJsonObject.get("ruleItem.inMakeDate"))) {
			if ("true".equals(String.valueOf(JudgeJsonObject.get("ruleItem.inMakeDate")))) {
				if (JudgeJsonObject.containsKey("ruleItem.inMakeDateType") && !StringUtil.isEmpty(JudgeJsonObject.get("ruleItem.inMakeDateType"))) {
					LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准的值为：" + String.valueOf(JudgeJsonObject.get("ruleItem.inMakeDateType")));
					if ("限制到月".equals(JudgeJsonObject.get("ruleItem.inMakeDateType").toString())) {
						LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准，符合规则的初等日期是：" + regDate + "以前的车，包含");
						for (int i = judgeList.size() - 1; 0 <= i; i--) {
							CarModelInfoBean judgeBean = judgeList.get(i);
							if (!StringUtil.isEmpty(judgeBean.getMaketdate())) {
								if (StringUtil.isEmpty(judgeBean.getMaketdate()) || judgeBean.getMaketdate().length() < 4) {
									LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准，车型的初等日期为：" + judgeBean.getMaketdate() + "，不符合规则");
									judgeList.remove(i);
								} else if (judgeBean.getMaketdate().length() >= 4 && judgeBean.getMaketdate().length() < 6) {
									if ((Integer.parseInt(regDate.substring(0, 4))) < Integer.parseInt((judgeBean.getMaketdate().substring(0, 4)))) {
										LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准，车型的初等日期为：" + judgeBean.getMaketdate() + "，不符合规则");
										judgeList.remove(i);
									}
								} else if (judgeBean.getMaketdate().length() >= 6 && (Integer.parseInt(regDate.substring(0, 6))) < Integer.parseInt((judgeBean.getMaketdate().substring(0, 6)))) {
									LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准，车型的初等日期为：" + judgeBean.getMaketdate() + "，不符合规则");
									judgeList.remove(i);
								}
							} else {
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准，车型的初等日期为空，默认不从列表移除，按价格最低匹配");
							}
						}
					} else if ("限制到年".equals(JudgeJsonObject.get("ruleItem.inMakeDateType").toString())) {
						LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准，符合规则的初等日期是：" + regDate.substring(0, 4) + "1231以前的车，包含");
						for (int i = judgeList.size() - 1; 0 <= i; i--) {
							CarModelInfoBean judgeBean = judgeList.get(i);
							if (!StringUtil.isEmpty(judgeBean.getMaketdate())) {
								if (StringUtil.isEmpty(judgeBean.getMaketdate()) || judgeBean.getMaketdate().length() < 4) {
									LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准，车型的初等日期为：" + judgeBean.getMaketdate() + "，不符合规则");
									judgeList.remove(i);
								} else {
									if ((Integer.parseInt(regDate.substring(0, 4))) < Integer.parseInt((judgeBean.getMaketdate().substring(0, 4)))) {
										LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准，车型的初等日期为：" + judgeBean.getMaketdate() + "，不符合规则");
										judgeList.remove(i);
									}
								}
							} else {
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准，车型的初等日期为空，默认不从列表移除，按价格最低匹配");
							}
						}
					} else if ("限制到次年".equals(JudgeJsonObject.get("ruleItem.inMakeDateType").toString())) {
						LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准，符合规则的初等日期是：" + (Integer.parseInt(regDate.substring(0, 4)) + 1) + "1231以前的车，包含");
						for (int i = judgeList.size() - 1; 0 <= i; i--) {
							CarModelInfoBean judgeBean = judgeList.get(i);
							if (!StringUtil.isEmpty(judgeBean.getMaketdate())) {
								if (StringUtil.isEmpty(judgeBean.getMaketdate()) || judgeBean.getMaketdate().length() < 4) {
									LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准，车型的初等日期为：" + judgeBean.getMaketdate() + "，不符合规则");
									judgeList.remove(i);
								} else {
									if ((Integer.parseInt(regDate.substring(0, 4)) + 1) < Integer.parseInt((judgeBean.getMaketdate().substring(0, 4)))) {
										LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准，车型的初等日期为：" + judgeBean.getMaketdate() + "，不符合规则");
										judgeList.remove(i);
									}
								}
							} else {
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准，车型的初等日期为空，默认不从列表移除，按价格最低匹配");
							}
						}
					} else if ("限制到次年当月".equals(JudgeJsonObject.get("ruleItem.inMakeDateType").toString())) {
						LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准，符合规则的初等日期是：" + (Integer.parseInt(regDate.substring(0, 6)) + 100) + "以前的车，包含");
						for (int i = judgeList.size() - 1; 0 <= i; i--) {
							CarModelInfoBean judgeBean = judgeList.get(i);
							if (!StringUtil.isEmpty(judgeBean.getMaketdate())) {
								if (StringUtil.isEmpty(judgeBean.getMaketdate()) || judgeBean.getMaketdate().length() < 4) {
									LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准，车型的初等日期为：" + judgeBean.getMaketdate() + "，不符合规则");
									judgeList.remove(i);
								} else if (judgeBean.getMaketdate().length() >= 4 && judgeBean.getMaketdate().length() < 6) {
									if ((Integer.parseInt(regDate.substring(0, 4)) + 1) < Integer.parseInt((judgeBean.getMaketdate().substring(0, 4)))) {
										LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准，车型的初等日期为：" + judgeBean.getMaketdate() + "，不符合规则");
										judgeList.remove(i);
									}
								} else {
									if ((Integer.parseInt(regDate.substring(0, 6)) + 100) < Integer.parseInt((judgeBean.getMaketdate().substring(0, 6)))) {
										LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准，车型的初等日期为：" + judgeBean.getMaketdate() + "，不符合规则");
										judgeList.remove(i);
									}
								}
							} else {
								LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准，车型的初等日期为空，默认不从列表移除，按价格最低匹配");
							}
						}
					} else {
						LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==车辆年款限制标准_ruleItem.inMakeDateType:值错误");
					}
				} else {
					LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准_ruleItem.inMakeDateType的key不存在=");
				}
			} else if ("false".equals(String.valueOf(JudgeJsonObject.get("ruleItem.inMakeDate")))) {
				LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准_ruleItem.inMakeDate的值为false，跳过筛选=");
			} else {
				LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车辆年款限制标准_ruleItem.inMakeDate的值错误=");
			}
		} else {
			LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===限制车型年款_ruleItem.inMakeDate的key不存在=");
		}
		return judgeList;
	}

	private CarModelInfoBean getFianllyCarModel(JSONObject JudgeJsonObject, List<CarModelInfoBean> judgeList, String taskId, String inscomcode, String pointPrice, String regDate, String seat) throws Exception {
		CarModelInfoBean resultBean = null;
		if (checkListSize(judgeList)) {
			resultBean = new CarModelInfoBean();
			resultBean.setId("-1");
			resultBean.setFamilyname("所选车型排量与年款不匹配，请核实政策或车辆信息是否正确");
			return resultBean;
		}
		
		String regDateOrig = regDate.replaceAll("-", "");
		regDate = (String) regDateOrig.subSequence(0, 6);
		judgeList = this.deleteBadCarInfoByRegDate(pointPrice, JudgeJsonObject, judgeList, taskId, inscomcode, regDate);
		LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===根据初等日期筛选是=" + judgeList.size());
		this.logInfo(judgeList, taskId, inscomcode);

		if (checkListSize(judgeList)) {
			resultBean = new CarModelInfoBean();
			resultBean.setId("-1");
			resultBean.setFamilyname("初登日期与年款不匹配，请核实政策或车辆信息是否正确");
			return resultBean;
		}

		judgeList = this.orderByCarModelSelectTypeAndCarPriceType(JudgeJsonObject, judgeList, taskId, inscomcode);
		LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==排序完的大小是=" + judgeList.size());
		this.logInfo(judgeList, taskId, inscomcode);
		if (checkListSize(judgeList)) {
			return null;
		}

		judgeList = this.deleteBadCarInfoByPrice(pointPrice, JudgeJsonObject, judgeList, taskId, inscomcode, regDateOrig, seat);
		LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===根据指定车价筛选是=" + judgeList.size());
		this.logInfo(judgeList, taskId, inscomcode);
		if (checkListSize(judgeList)) {
			resultBean = new CarModelInfoBean();
			resultBean.setId("-1");
			resultBean.setFamilyname("指定车价超出浮动范围，请核实指定车价");
			return resultBean;
		}

		if (JudgeJsonObject.containsKey("ruleItem.lowestPriceType") && !StringUtil.isEmpty(JudgeJsonObject.get("ruleItem.lowestPriceType"))) {
			if ("0".equals(JudgeJsonObject.get("ruleItem.lowestPriceType").toString().trim())) {
				LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=无限制:0");
			} else if ("1".equals(JudgeJsonObject.get("ruleItem.lowestPriceType").toString().trim())) {
				LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=有年款的车价优先:1,");
				for (CarModelInfoBean dataBean : judgeList) {
					if (!StringUtil.isEmpty(dataBean.getMaketdate())) {
						return dataBean;
					}
				}
			} else if ("2".equals(JudgeJsonObject.get("ruleItem.lowestPriceType").toString().trim())) {
				LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=前三后一原则:2");
				for (CarModelInfoBean dataBean : judgeList) {
					if (!StringUtil.isEmpty(dataBean.getMaketdate())&&dataBean.getMaketdate().length()>=4) {
						LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式,按初登日期当年");
						if(regDate.substring(0, 4).equals(dataBean.getMaketdate().substring(0, 4))){
							LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式,按初登日期当年,命中的是" + dataBean.toString());
							return dataBean;
						}
					}
				}
				for (CarModelInfoBean dataBean : judgeList) {
					if (!StringUtil.isEmpty(dataBean.getMaketdate())&&dataBean.getMaketdate().length()>=4) {
						LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式,按初登日期前一年");
						if(String.valueOf((Integer.parseInt(regDate.substring(0, 4))-1)).equals(dataBean.getMaketdate().substring(0, 4))){
							LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式,按初登日期前一年,命中的是" + dataBean.toString());
							return dataBean;
						}
					}
				}
				for (CarModelInfoBean dataBean : judgeList) {
					if (!StringUtil.isEmpty(dataBean.getMaketdate())&&dataBean.getMaketdate().length()>=4) {
						LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式,按初登日期前两年");
						if(String.valueOf((Integer.parseInt(regDate.substring(0, 4))-2)).equals(dataBean.getMaketdate().substring(0, 4))){
							LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式,按初登日期前两年,命中的是" + dataBean.toString());
							return dataBean;
						}
					}
				}
				for (CarModelInfoBean dataBean : judgeList) {
					if (!StringUtil.isEmpty(dataBean.getMaketdate())&&dataBean.getMaketdate().length()>=4) {
						LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式,按初登日期后一年");
						if(String.valueOf((Integer.parseInt(regDate.substring(0, 4))+1)).equals(dataBean.getMaketdate().substring(0, 4))){
							LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式,按初登日期后一年,命中的是" + dataBean.toString());
							return dataBean;
						}
					}
				}
			} else if ("3".equals(JudgeJsonObject.get("ruleItem.lowestPriceType").toString().trim())) {
				LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=当年当月优先:3,初等日期是：" + regDate);

				for (CarModelInfoBean dataBean : judgeList) {
					if (regDate.equals(dataBean.getMaketdate())) {
						LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=先判断当年当月优先命中");
						return dataBean;
					}
				}
				LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=先判断当年当月优先,没有命中");

				boolean flag = true;
				String startDate = regDate.substring(0, 4) + "01";
				String endDate = regDate;
				LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=先判断当年当月优先,当前期间是：" + startDate + "到" + endDate);

				List<CarModelInfoBean> tempList1 = new ArrayList<>();
				for (CarModelInfoBean dataBean : judgeList) {
					if (!StringUtil.isEmpty(dataBean.getMaketdate()) && dataBean.getMaketdate().length() >= 6 && Integer.parseInt(startDate) <= Integer.parseInt(dataBean.getMaketdate())
							&& Integer.parseInt(endDate) >= Integer.parseInt(dataBean.getMaketdate())) {
						tempList1.add(dataBean);
					}
				}

				if (tempList1.size() <= 0) {
					for (CarModelInfoBean dataBean : judgeList) {
						if (!StringUtil.isEmpty(dataBean.getMaketdate()) && dataBean.getMaketdate().length() == 4 && Integer.parseInt(regDate.substring(0, 4)) == Integer.parseInt(dataBean.getMaketdate())) {
							tempList1.add(dataBean);
						}
					}
				}

				if (tempList1.size() <= 0) {
					LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=先判断当年当月优先,当前期间是：" + startDate + "到" + endDate + ",没有符合的车型");

					int count = 1;
					while (flag) {
						LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=先判断当年当月优先,当前期间是：" + (Integer.parseInt(regDate.substring(0, 4)) - count) + "01到"
								+ (Integer.parseInt(regDate.substring(0, 4)) - count) + "12");

						List<CarModelInfoBean> tempList = new ArrayList<>();
						for (CarModelInfoBean dataBean : judgeList) {
							if (!StringUtil.isEmpty(dataBean.getMaketdate()) && dataBean.getMaketdate().length() >= 4
									&& (Integer.parseInt(regDate.substring(0, 4)) - count) == Integer.parseInt(dataBean.getMaketdate().substring(0, 4))) {
								tempList.add(dataBean);
							}
						}
						if (tempList.size() <= 0) {
							LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=先判断当年当月优先,当前期间是：" + (Integer.parseInt(regDate.substring(0, 4)) - count) + "01到"
									+ (Integer.parseInt(regDate.substring(0, 4)) - count) + "12,没有符合的车型");
							count++;
						} else {
							LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=先判断当年当月优先,当前期间是：" + (Integer.parseInt(regDate.substring(0, 4)) - count) + "01到"
									+ (Integer.parseInt(regDate.substring(0, 4)) - count) + "12,命中的车型是：");
							this.logInfo(tempList, taskId, inscomcode);
							tempList = this.sortByPrice(JudgeJsonObject.get("ruleItem.carPriceType").toString().trim(), tempList, taskId, inscomcode);
							return tempList.get(0);
						}
						if (count >= 20) {
							LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=先判断当年当月优先,当前期间是：" + (Integer.parseInt(regDate.substring(0, 4)) - count) + "01到"
									+ (Integer.parseInt(regDate.substring(0, 4)) - count) + "12,往前推了20年了，不推了");
							flag = false;
						}
					}
				} else {
					LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=先判断当年当月优先,当前期间是：" + startDate + "到" + endDate + ",命中的车型是：");
					this.logInfo(tempList1, taskId, inscomcode);
					tempList1 = this.sortByPrice(JudgeJsonObject.get("ruleItem.carPriceType").toString().trim(), tempList1, taskId, inscomcode);
					return tempList1.get(0);
				}
			}else if ("4".equals(JudgeJsonObject.get("ruleItem.lowestPriceType").toString().trim())) {
				LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=当年优先: 4,初等日期是：" + regDate);
				boolean flag = true;
				String startDate = regDate.substring(0, 4);
				String endDate = regDate;
				LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=当年优先=当前期间是：" + startDate);

				List<CarModelInfoBean> tempList1 = new ArrayList<>();
				for (CarModelInfoBean dataBean : judgeList) {
					if (!StringUtil.isEmpty(dataBean.getMaketdate()) && dataBean.getMaketdate().length() >= 4 && Integer.parseInt(startDate) <= Integer.parseInt(dataBean.getMaketdate().substring(0, 4))
							&& Integer.parseInt(endDate) >= Integer.parseInt(dataBean.getMaketdate().substring(0, 4))) {
						tempList1.add(dataBean);
					}
				}

				if (tempList1.size() <= 0) {
					LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=当年优先=,当前期间是：" + startDate + ",没有符合的车型");

					int count = 1;
					while (flag) {
						LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=当年优先=,当前期间是：" + (Integer.parseInt(regDate.substring(0, 4)) - count));

						List<CarModelInfoBean> tempList = new ArrayList<>();
						for (CarModelInfoBean dataBean : judgeList) {
							if (!StringUtil.isEmpty(dataBean.getMaketdate()) && dataBean.getMaketdate().length() >= 4
									&& (Integer.parseInt(regDate.substring(0, 4)) - count) <= Integer.parseInt(dataBean.getMaketdate().substring(0, 4))
									&& (Integer.parseInt(regDate.substring(0, 4)) - count) >= Integer.parseInt(dataBean.getMaketdate().substring(0, 4))) {
								tempList.add(dataBean);
							}
						}
						if (tempList.size() <= 0) {
							LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=当年优先=,当前期间是：" + (Integer.parseInt(regDate.substring(0, 4)) - count) + ",没有符合的车型");
							count++;
						} else {
							LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=当年优先=,当前期间是：" + (Integer.parseInt(regDate.substring(0, 4)) - count) + ",命中的车型是：");
							this.logInfo(tempList, taskId, inscomcode);
							tempList = this.sortByPrice(JudgeJsonObject.get("ruleItem.carPriceType").toString().trim(), tempList, taskId, inscomcode);
							return tempList.get(0);
						}
						if (count >= 20) {
							LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=当年优先=,当前期间是：" + (Integer.parseInt(regDate.substring(0, 4)) - count) + ",往前推了20年了，不推了");
							flag = false;
						}
					}
				} else {
					LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "===最低车价选择方式=当年优先=,当前期间是：" + startDate + ",命中的车型是：");
					this.logInfo(tempList1, taskId, inscomcode);
					tempList1 = this.sortByPrice(JudgeJsonObject.get("ruleItem.carPriceType").toString().trim(), tempList1, taskId, inscomcode);
					return tempList1.get(0);
				}
			} else {
				LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==最低车价选择方式_ruleItem.lowestPriceType=值错误");
			}
		} else {
			LogUtil.info("==筛选年款==：taskid=" + taskId + ",inscomcode=" + inscomcode + "==最低车价选择方式_ruleItem.lowestPriceType=key不存在");
		}
		return judgeList.get(0);
	}

	private boolean checkListSize(List<CarModelInfoBean> judgeList) {
		return judgeList.size() <= 0;
	}

	private void ruleInfoToString(String info,String taskid,String inscomcode) {
		JSONObject JudgeJsonObject = JSONObject.fromObject(info);
		if(JudgeJsonObject.containsKey("ruleItem.inMakeDate")){
			LogUtil.info("==筛选年款=获得判断规则=：taskid=" + taskid + ",inscomcode=" + inscomcode + ",1.	限制车型年款_ruleItem.inMakeDate:" + String.valueOf(JudgeJsonObject.get("ruleItem.inMakeDate")) + ",");
		}
		if(JudgeJsonObject.containsKey("ruleItem.inMakeDateType")){
			LogUtil.info("==筛选年款=获得判断规则=：taskid=" + taskid + ",inscomcode=" + inscomcode + ",2.	车辆年款限制标准 _ruleItem.inMakeDateType:" + String.valueOf(JudgeJsonObject.get("ruleItem.inMakeDateType")) + ",");
		}
		if(JudgeJsonObject.containsKey("ruleItem.carModelSelectType")){
			LogUtil.info("==筛选年款=获得判断规则=：taskid=" + taskid + ",inscomcode=" + inscomcode + ",3.	车款选取方式_ruleItem.carModelSelectType:" + String.valueOf(JudgeJsonObject.get("ruleItem.carModelSelectType")) + "," + "(价格最低优先:0,初登最近优先:1)");
		}
		if(JudgeJsonObject.containsKey("ruleItem.carPriceType")){
			LogUtil.info("==筛选年款=获得判断规则=：taskid=" + taskid + ",inscomcode=" + inscomcode + ",4.	车辆价格类型_ruleItem.carPriceType:" + String.valueOf(JudgeJsonObject.get("ruleItem.carPriceType")) + ",");
		}
		if(JudgeJsonObject.containsKey("car.specific.maxPriceRate")){
			LogUtil.info("==筛选年款=获得判断规则=：taskid=" + taskid + ",inscomcode=" + inscomcode + ",5.	浮动价上限比_car.specific.maxPriceRate:" + String.valueOf(JudgeJsonObject.get("car.specific.maxPriceRate")) + ",");
		}
		if(JudgeJsonObject.containsKey("car.specific.minPriceRate")){
			LogUtil.info("==筛选年款=获得判断规则=：taskid=" + taskid + ",inscomcode=" + inscomcode + ",5. 浮动价下限比率_car.specific.minPriceRate:" + String.valueOf(JudgeJsonObject.get("car.specific.minPriceRate")) + ",");
		}
		if(JudgeJsonObject.containsKey("ruleItem.lowestPriceType")){
			LogUtil.info("==筛选年款=获得判断规则=：taskid=" + taskid + ",inscomcode=" + inscomcode + ",6.	最低车价选择方式_ruleItem.lowestPriceType:" + String.valueOf(JudgeJsonObject.get("ruleItem.lowestPriceType")) + "," + "(无限制:0,有年款的车价优先:1,前三后一原则:2,当年当月优先:3,当年优先：4)");
		}
	}

	@Override
	public CarModelInfoBean getCarModelInfo(String vinCode, String subtaskId, String taskId, String inscomcode, INSBCarmodelinfohis carmodelinfo, String regDate,String pointPrice) {
		LogUtil.info("==筛选年款=进入的参数是=：taskid=%s ,inscomcode=%s ====参数：子流程_subtaskId=%s ,新车购置价_taxPrice=%s ,不含税价_price=%s ,含税类比价_analogyTaxPrice=%s ,不含税类比价_analogyPrice=%s ,初等日期_regDate=%s ,座位数_seats=%s ,车型名称_modelName=%s ,指定车价_pointPrice=%s,排量_displacement=%s",
				taskId, inscomcode, subtaskId, carmodelinfo.getTaxprice(), carmodelinfo.getPrice(),
				carmodelinfo.getAnalogytaxprice(), carmodelinfo.getAnalogyprice(), regDate,
				carmodelinfo.getSeat(), carmodelinfo.getStandardfullname(), pointPrice, carmodelinfo.getDisplacement());
		CarModelInfoBean resultBean = null;
		try {
			String result = appQuotationService.getPriceInterval(subtaskId, taskId, inscomcode, carmodelinfo.getTaxprice(), carmodelinfo.getPrice(), carmodelinfo.getAnalogytaxprice(), carmodelinfo.getAnalogyprice(), regDate, carmodelinfo.getSeat());
			this.ruleInfoToString(result,taskId,inscomcode);

			if (!StringUtil.isEmpty(result)) {
				String date1Begain = DateUtil.getCurrentDateTime(DateUtil.Format_DateTime);
				LogUtil.info("==筛选年款=：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车型名称_modelName=" + carmodelinfo.getStandardfullname() + ",第一次获取请求时间=" + date1Begain);
				List<CarModelInfoBean> judgeList = this.getCarModelListFromCif(carmodelinfo.getStandardfullname(), taskId, inscomcode);
				String date1end = DateUtil.getCurrentDateTime(DateUtil.Format_DateTime);
				LogUtil.info("==筛选年款=：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车型名称_modelName=" + carmodelinfo.getStandardfullname() + ",第一次获取返回时间=" + date1end);

				LocalDateTime from = LocalDateTime.parse(date1end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				LocalDateTime to = LocalDateTime.parse(date1Begain, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				Duration duration = Duration.between(to, from);
				int tempUseTime = (int) (duration.toMillis() / 1000);
				LogUtil.info("==筛选年款=：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车型名称_modelName=" + carmodelinfo.getStandardfullname() + ",第一次用时=" + tempUseTime + (tempUseTime>=20?",超时了":""));

				JSONObject JudgeJsonObject = JSONObject.fromObject(result);
				String carPriceType = String.valueOf(JudgeJsonObject.get("ruleItem.carPriceType"));
				INSBQuoteinfo dataInsbQuoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(taskId, inscomcode);
				if(dataInsbQuoteinfo != null){
					dataInsbQuoteinfo.setInsureway(carPriceType);
					insbQuoteinfoService.updateById(dataInsbQuoteinfo);
				}

				if (judgeList.size() <= 0) {
					LogUtil.info("==筛选年款=：taskid=" + taskId + ",inscomcode=" + inscomcode + "=车型名称_modelName=" + carmodelinfo.getStandardfullname() + "=第一次=获取车型列表错误");
					String date2Begain = DateUtil.getCurrentDateTime(DateUtil.Format_DateTime);
					LogUtil.info("==筛选年款=：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车型名称_modelName=" + carmodelinfo.getStandardfullname() + ",第二次获取请求时间=" + date2Begain);
					List<CarModelInfoBean> judgeList2 = this.getCarModelListFromCif(carmodelinfo.getStandardfullname(), taskId, inscomcode);
					String date2end = DateUtil.getCurrentDateTime(DateUtil.Format_DateTime);
					LogUtil.info("==筛选年款=：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车型名称_modelName=" + carmodelinfo.getStandardfullname() + ",第二次获取返回时间=" + date2end);

					LocalDateTime from2 = LocalDateTime.parse(date2end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					LocalDateTime to2 = LocalDateTime.parse(date2Begain, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					Duration duration2 = Duration.between(to2, from2);
					int tempUseTime2 = (int) (duration2.toMillis() / 1000);
					LogUtil.info("==筛选年款=：taskid=" + taskId + ",inscomcode=" + inscomcode + "===车型名称_modelName=" + carmodelinfo.getStandardfullname() + ",第二次用时=" + tempUseTime2 + (tempUseTime2>=20?",超时了":""));

					if (judgeList2.size() <= 0) {
						LogUtil.info("==筛选年款=：taskid=" + taskId + ",inscomcode=" + inscomcode + "=车型名称_modelName=" + carmodelinfo.getStandardfullname() + "=第二次=获取车型列表错误");
						resultBean = new CarModelInfoBean();
						resultBean.setId("-1");
						resultBean.setFamilyname("车型有误，请核实");
						return resultBean;
					} else {
						//如果是进口车且规则规定设定需要过滤排量则过滤list   InterFaceServiceImpl#getCarVehicleOriginByVin）
						if(1 == getCarVehicleOriginByVin(vinCode) && JudgeJsonObject.getBoolean("ruleItem.restrictImportVehicleDisplacement")){
							judgeList2 = judgeList2.stream().filter(model -> model.getDisplacement().equals(String.valueOf(carmodelinfo.getDisplacement()))).collect(Collectors.toList());
						}
						LogUtil.info("size=%s=筛选年款=获得车型列表=：taskid=%s,inscomcode=%s=车型名称_modelName=%s=第二次获取车型列表成功",judgeList2.size(), taskId, inscomcode, carmodelinfo.getStandardfullname());
						resultBean = this.getFianllyCarModel(JudgeJsonObject, judgeList2, taskId, inscomcode, pointPrice, regDate, String.valueOf(carmodelinfo.getSeat()));
						if (StringUtil.isEmpty(resultBean) || "-1".equals(resultBean.getId())) {
							LogUtil.info("==筛选年款=：taskid=" + taskId + ",inscomcode=" + inscomcode + "" + "=为空，没有命中");
						} else {
							LogUtil.info("==筛选年款=处理完的车型列表=：taskid=" + taskId + ",inscomcode=" + inscomcode + "," + resultBean.toString());
						}
					}
				} else {
					//如果是进口车且规则规定设定需要过滤排量则过滤list   InterFaceServiceImpl#getCarVehicleOriginByVin）
					if(1 == getCarVehicleOriginByVin(vinCode) && JudgeJsonObject.getBoolean("ruleItem.restrictImportVehicleDisplacement")){
						judgeList = judgeList.stream().filter(model -> model.getDisplacement().equals(String.valueOf(carmodelinfo.getDisplacement()))).collect(Collectors.toList());
					}
					LogUtil.info("size=%s=筛选年款=获得车型列表=：taskid=%s,inscomcode=%s=车型名称_modelName=%s=第一次=获取车型列表成功",judgeList.size(), taskId, inscomcode, carmodelinfo.getStandardfullname());
					resultBean = this.getFianllyCarModel(JudgeJsonObject, judgeList, taskId, inscomcode, pointPrice, regDate, String.valueOf(carmodelinfo.getSeat()));
					if (StringUtil.isEmpty(resultBean) || "-1".equals(resultBean.getId())) {
						LogUtil.info("==筛选年款=：taskid=" + taskId + ",inscomcode=" + inscomcode + "=车型名称_modelName=" + carmodelinfo.getStandardfullname() + "=为空，没有命中");
					} else {
						LogUtil.info("==筛选年款=处理完的车型列表=：taskid=" + taskId + ",inscomcode=" + inscomcode + "," + resultBean.toString());
					}
				}

			} else {
				LogUtil.info("==筛选年款=获得判断规则=：taskid=" + taskId + ",inscomcode=" + inscomcode + "=车型名称_modelName=" + carmodelinfo.getStandardfullname() + "==result:" + result + "=错误，规则维度没取到信息=");
			}
		} catch (Exception e) {
			LogUtil.info("==筛选年款=主程序=：taskid=" + taskId + ",inscomcode=" + inscomcode + "==出错了=车型名称_modelName=" + carmodelinfo.getStandardfullname());
			e.printStackTrace();
			return resultBean;
		}

		if (resultBean != null && !"-1".equals(resultBean.getId())) {
			String cifCode = "";
			if (!StringUtil.isEmpty(resultBean.getVehicleId())) {
				cifCode = this.getRbCode(resultBean.getVehicleId(), inscomcode, taskId);
				LogUtil.info("==筛选年款=获取rbcode和jycode=：taskid=" + taskId + ",inscomcode=" + inscomcode + ",获得的code是=" + cifCode);
			}else{
				LogUtil.info("==筛选年款=获取rbcode和jycode=：taskid=" + taskId + ",inscomcode=" + inscomcode + ",VehicleId" + "=为空，获取失败");
			}
			resultBean.setJycode(cifCode);
			resultBean.setRbcode(cifCode);
		}

		return resultBean;
	}

	private List<CarModelInfoBean> getCarModelListFromCif(String modelName, String taskId, String inscomcode) throws Exception {
		List<CarModelInfoBean> resultList = new ArrayList<CarModelInfoBean>();
		Map<String, String> params = new HashMap<String, String>();
		params.put("flag", "2");
		params.put("vehicleName", modelName);
		params.put("pageSize", "100");
		params.put("pageNumber", "1");
		params.put("taskId", taskId);
		try {
			String result = CloudQueryUtil.jingYouCarModelSearch(params);
			if (!StringUtil.isEmpty(result)) {
				JSONObject resultJsonObject = JSONObject.fromObject(result);
				JSONArray carModelList = JSONArray.fromObject(resultJsonObject.get("List"));
				for (int i = 0; i < carModelList.size(); i++) {
					JSONObject data = (JSONObject) carModelList.get(i);
					CarModelInfoBean dataBean = (CarModelInfoBean) JSONObject.toBean(data, CarModelInfoBean.class);
					resultList.add(dataBean);
				}
			} else {
				return resultList;
			}
		} catch (Exception e) {
			LogUtil.info("==筛选年款=获得车型列表=：taskid=" + taskId + ",inscomcode=" + inscomcode + "=获取车型列表信息=出错了：" + e.getMessage());
			e.printStackTrace();
			return new ArrayList<CarModelInfoBean>();
		}
		return resultList;
	}

	@Override
	public String getConfigInfo(String inscomcode, String type, String singlesite, String key) {
		if (!StringUtil.isEmpty(key) && key.equals(Md5Encodes.encodeMd5(inscomcode + type + singlesite))) {
			ConfigInfo dataConfigInfo = appQuotationService.getConfigInfo(inscomcode, singlesite, type);
			String str = JSONObject.fromObject(dataConfigInfo).toString();

			LogUtil.info("配置信息：inscomcode=" + inscomcode + ",type=" + type + "：" + str);
			return str;
		} else {
			return "密钥验证错误";
		}
	}

	private void log(String taskId, String insCode, String str) {
		LogUtil.info("任务ID:" + taskId + "@" + insCode + "," + str);
	}

	/**
	 *
	 * @param taskId
	 *            主流程id
	 * @param inscomcode
	 *            保险公司
	 * @param processType
	 *            精灵edi，robot，edi
	 * @param taskStatus
	 *            A B D
	 * @return
	 */
	@Resource
	INSHPersonService inshPersonService;
	@Resource
	INSHApplicanthisService inshApplicanthisService;
	@Resource
	INSHInsuredhisService inshInsuredhisService;
	@Resource
	INSHLegalrightclaimhisService inshLegalrightclaimhisService;
	@Resource
	INSHRelationpersonhisService inshRelationpersonhisService;
	@Resource
	INSHSpecifydriverhisService inshSpecifydriverhisService;
	@Resource
	INSHCarowneinfoService inshCarowneinfoService;
	@Resource
	INSHPolicyitemService inshPolicyitemService;
	@Resource
	INSHOrderService inshOrderService;
	@Resource
	INSHOrderpaymentService inshOrderpaymentService;
	@Resource
	INSHOrderdeliveryService inshOrderdeliveryService;
	@Resource
	INSHQuotetotalinfoService inshQuotetotalinfoService;
	@Resource
	INSHQuoteinfoService inshQuoteinfoService;
	@Resource
	INSHCarkindpriceService inshCarkindpriceService;
	@Resource
	INSHCarinfohisService inshCarinfohisService;
	@Resource
	INSHCarmodelinfohisService inshCarmodelinfohisService;

	private Boolean saveHistoryData(String taskId, String inscomcode, String processType, String taskStatus) {
		if (StringUtil.isEmpty(taskId) || StringUtil.isEmpty(inscomcode) || StringUtil.isEmpty(processType) || StringUtil.isEmpty(taskStatus)) {
			return false;
		}
		try {
			this.log(taskId, inscomcode, "开始投保人备份.....");
			// ---------------------------------投保人备份-------------------------------
			INSBApplicanthis fromInsbApplicanthis = new INSBApplicanthis();
			fromInsbApplicanthis.setTaskid(taskId);
			fromInsbApplicanthis.setInscomcode(inscomcode);
			fromInsbApplicanthis = insbApplicanthisService.queryOne(fromInsbApplicanthis);
			INSBPerson fromInsbPersonApp = new INSBPerson();
			if (!StringUtil.isEmpty(fromInsbApplicanthis) && !StringUtil.isEmpty(fromInsbApplicanthis.getPersonid())) {
				fromInsbPersonApp = insbPersonService.queryById(fromInsbApplicanthis.getPersonid());
				if (!StringUtil.isEmpty(fromInsbPersonApp)) {
					INSHPerson toInshPerson = new INSHPerson();
					toInshPerson.setTaskid(taskId);
					toInshPerson.setFairyoredi(processType);
					toInshPerson.setNodecode(taskStatus);
					toInshPerson = inshPersonService.queryOne(toInshPerson);
					if (!StringUtil.isEmpty(toInshPerson)) {
						PropertyUtils.copyProperties(toInshPerson, fromInsbPersonApp);
						inshPersonService.updateById(toInshPerson);
					} else {
						toInshPerson = new INSHPerson();
						PropertyUtils.copyProperties(toInshPerson, fromInsbPersonApp);
						toInshPerson.setFairyoredi(processType);
						toInshPerson.setNodecode(taskStatus);
						inshPersonService.insert(toInshPerson);
					}
				}
				INSHApplicanthis toInshApplicanthis = new INSHApplicanthis();
				toInshApplicanthis.setTaskid(taskId);
				toInshApplicanthis.setInscomcode(inscomcode);
				toInshApplicanthis.setFairyoredi(processType);
				toInshApplicanthis.setNodecode(taskStatus);
				toInshApplicanthis = inshApplicanthisService.queryOne(toInshApplicanthis);
				if (!StringUtil.isEmpty(toInshApplicanthis)) {
					PropertyUtils.copyProperties(toInshApplicanthis, fromInsbApplicanthis);
					inshApplicanthisService.updateById(toInshApplicanthis);
				} else {
					toInshApplicanthis = new INSHApplicanthis();
					PropertyUtils.copyProperties(toInshApplicanthis, fromInsbApplicanthis);
					toInshApplicanthis.setFairyoredi(processType);
					toInshApplicanthis.setNodecode(taskStatus);
					inshApplicanthisService.insert(toInshApplicanthis);
				}
			}

			this.log(taskId, inscomcode, "开始被保人备份.....");
			// ---------------------------------被保人备份-------------------------------
			INSBInsuredhis fromInsbInsuredhis = new INSBInsuredhis();
			fromInsbInsuredhis.setTaskid(taskId);
			fromInsbInsuredhis.setInscomcode(inscomcode);
			List<INSBInsuredhis> fromInsuredhisList = insbInsuredhisService.queryList(fromInsbInsuredhis);
			if (fromInsuredhisList.size() > 0) {
				for (INSBInsuredhis dataInsbInsuredhis : fromInsuredhisList) {
					if (!StringUtil.isEmpty(dataInsbInsuredhis) && !StringUtil.isEmpty(dataInsbInsuredhis.getPersonid())) {
						INSBPerson dataInsbPerson = insbPersonService.queryById(dataInsbInsuredhis.getPersonid());
						if (!StringUtil.isEmpty(dataInsbPerson)) {
							INSHPerson toInshPerson = new INSHPerson();
							toInshPerson.setTaskid(taskId);
							toInshPerson.setFairyoredi(processType);
							toInshPerson.setNodecode(taskStatus);
							toInshPerson = inshPersonService.queryOne(toInshPerson);
							if (!StringUtil.isEmpty(toInshPerson)) {
								PropertyUtils.copyProperties(toInshPerson, dataInsbPerson);
								inshPersonService.updateById(toInshPerson);
							} else {
								toInshPerson = new INSHPerson();
								PropertyUtils.copyProperties(toInshPerson, dataInsbPerson);
								toInshPerson.setFairyoredi(processType);
								toInshPerson.setNodecode(taskStatus);
								inshPersonService.insert(toInshPerson);
							}
						}
					}
					if (!StringUtil.isEmpty(dataInsbInsuredhis)) {
						INSHInsuredhis toInshInsuredhis = new INSHInsuredhis();
						toInshInsuredhis.setTaskid(taskId);
						toInshInsuredhis.setInscomcode(inscomcode);
						toInshInsuredhis.setFairyoredi(processType);
						toInshInsuredhis.setNodecode(taskStatus);
						toInshInsuredhis = inshInsuredhisService.queryOne(toInshInsuredhis);
						if (!StringUtil.isEmpty(toInshInsuredhis)) {
							PropertyUtils.copyProperties(toInshInsuredhis, dataInsbInsuredhis);
							inshInsuredhisService.updateById(toInshInsuredhis);
						} else {
							toInshInsuredhis = new INSHInsuredhis();
							PropertyUtils.copyProperties(toInshInsuredhis, dataInsbInsuredhis);
							toInshInsuredhis.setFairyoredi(processType);
							toInshInsuredhis.setNodecode(taskStatus);
							inshInsuredhisService.insert(toInshInsuredhis);
						}
					}
				}
			}

			this.log(taskId, inscomcode, "开始受益人备份.....");
			// ---------------------------------受益人备份-------------------------------
			INSBLegalrightclaimhis fromInsbLegalrightclaimhis = new INSBLegalrightclaimhis();
			fromInsbLegalrightclaimhis.setTaskid(taskId);
			fromInsbLegalrightclaimhis.setInscomcode(inscomcode);
			List<INSBLegalrightclaimhis> fromLegalrightclaimhisList = insbLegalrightclaimhisService.queryList(fromInsbLegalrightclaimhis);
			if (fromLegalrightclaimhisList.size() > 0) {
				for (INSBLegalrightclaimhis dataInsbLegalrightclaimhis : fromLegalrightclaimhisList) {
					if (!StringUtil.isEmpty(dataInsbLegalrightclaimhis) && !StringUtil.isEmpty(dataInsbLegalrightclaimhis.getPersonid())) {
						INSBPerson dataInsbPerson = insbPersonService.queryById(dataInsbLegalrightclaimhis.getPersonid());
						if (!StringUtil.isEmpty(dataInsbPerson)) {
							INSHPerson toInshPerson = new INSHPerson();
							toInshPerson.setTaskid(taskId);
							toInshPerson.setFairyoredi(processType);
							toInshPerson.setNodecode(taskStatus);
							toInshPerson = inshPersonService.queryOne(toInshPerson);
							if (!StringUtil.isEmpty(toInshPerson)) {
								PropertyUtils.copyProperties(toInshPerson, dataInsbPerson);
								inshPersonService.updateById(toInshPerson);
							} else {
								toInshPerson = new INSHPerson();
								PropertyUtils.copyProperties(toInshPerson, dataInsbPerson);
								toInshPerson.setFairyoredi(processType);
								toInshPerson.setNodecode(taskStatus);
								inshPersonService.insert(toInshPerson);
							}
						}
					}
					if (!StringUtil.isEmpty(dataInsbLegalrightclaimhis)) {
						INSHLegalrightclaimhis toInshLegalrightclaimhis = new INSHLegalrightclaimhis();
						toInshLegalrightclaimhis.setTaskid(taskId);
						toInshLegalrightclaimhis.setInscomcode(inscomcode);
						toInshLegalrightclaimhis.setFairyoredi(processType);
						toInshLegalrightclaimhis.setNodecode(taskStatus);
						toInshLegalrightclaimhis = inshLegalrightclaimhisService.queryOne(toInshLegalrightclaimhis);
						if (!StringUtil.isEmpty(toInshLegalrightclaimhis)) {
							PropertyUtils.copyProperties(toInshLegalrightclaimhis, dataInsbLegalrightclaimhis);
							inshLegalrightclaimhisService.updateById(toInshLegalrightclaimhis);
						} else {
							toInshLegalrightclaimhis = new INSHLegalrightclaimhis();
							PropertyUtils.copyProperties(toInshLegalrightclaimhis, dataInsbLegalrightclaimhis);
							toInshLegalrightclaimhis.setFairyoredi(processType);
							toInshLegalrightclaimhis.setNodecode(taskStatus);
							inshLegalrightclaimhisService.insert(toInshLegalrightclaimhis);
						}
					}
				}
			}
			this.log(taskId, inscomcode, "开始关系人备份.....");
			// ---------------------------------关系人备份-------------------------------
			INSBRelationpersonhis fromInsbRelationpersonhis = new INSBRelationpersonhis();
			fromInsbRelationpersonhis.setTaskid(taskId);
			fromInsbRelationpersonhis.setInscomcode(inscomcode);
			List<INSBRelationpersonhis> fromRelationpersonhisList = insbRelationpersonhisService.queryList(fromInsbRelationpersonhis);
			if (fromRelationpersonhisList.size() > 0) {
				for (INSBRelationpersonhis dataInsbRelationpersonhis : fromRelationpersonhisList) {
					if (!StringUtil.isEmpty(dataInsbRelationpersonhis) && !StringUtil.isEmpty(dataInsbRelationpersonhis.getPersonid())) {
						INSBPerson dataInsbPerson = insbPersonService.queryById(dataInsbRelationpersonhis.getPersonid());
						if (!StringUtil.isEmpty(dataInsbPerson)) {
							INSHPerson toInshPerson = new INSHPerson();
							toInshPerson.setTaskid(taskId);
							toInshPerson.setFairyoredi(processType);
							toInshPerson.setNodecode(taskStatus);
							toInshPerson = inshPersonService.queryOne(toInshPerson);
							if (!StringUtil.isEmpty(toInshPerson)) {
								PropertyUtils.copyProperties(toInshPerson, dataInsbPerson);
								inshPersonService.updateById(toInshPerson);
							} else {
								toInshPerson = new INSHPerson();
								PropertyUtils.copyProperties(toInshPerson, dataInsbPerson);
								toInshPerson.setFairyoredi(processType);
								toInshPerson.setNodecode(taskStatus);
								inshPersonService.insert(toInshPerson);
							}
						}
					}
					if (!StringUtil.isEmpty(dataInsbRelationpersonhis)) {
						INSHRelationpersonhis toInshRelationpersonhis = new INSHRelationpersonhis();
						toInshRelationpersonhis.setTaskid(taskId);
						toInshRelationpersonhis.setInscomcode(inscomcode);
						toInshRelationpersonhis.setFairyoredi(processType);
						toInshRelationpersonhis.setNodecode(taskStatus);
						toInshRelationpersonhis = inshRelationpersonhisService.queryOne(toInshRelationpersonhis);
						if (!StringUtil.isEmpty(toInshRelationpersonhis)) {
							PropertyUtils.copyProperties(toInshRelationpersonhis, dataInsbRelationpersonhis);
							inshRelationpersonhisService.updateById(toInshRelationpersonhis);
						} else {
							toInshRelationpersonhis = new INSHRelationpersonhis();
							PropertyUtils.copyProperties(toInshRelationpersonhis, dataInsbRelationpersonhis);
							toInshRelationpersonhis.setFairyoredi(processType);
							toInshRelationpersonhis.setNodecode(taskStatus);
							inshRelationpersonhisService.insert(toInshRelationpersonhis);
						}
					}
				}
			}
			this.log(taskId, inscomcode, "开始指定驾驶人备份.....");
			// ---------------------------------指定驾驶人备份-------------------------------
			INSBSpecifydriverhis fromInsbSpecifydriverhis = new INSBSpecifydriverhis();
			fromInsbSpecifydriverhis.setTaskid(taskId);
			fromInsbSpecifydriverhis.setInscomcode(inscomcode);
			List<INSBSpecifydriverhis> fromSpecifydriverhisList = insbspecifydriverhisService.queryList(fromInsbSpecifydriverhis);
			if (fromSpecifydriverhisList.size() > 0) {
				for (INSBSpecifydriverhis dataInsbSpecifydriverhis : fromSpecifydriverhisList) {
					if (!StringUtil.isEmpty(dataInsbSpecifydriverhis) && !StringUtil.isEmpty(dataInsbSpecifydriverhis.getPersonid())) {
						INSBPerson dataInsbPerson = insbPersonService.queryById(dataInsbSpecifydriverhis.getPersonid());
						if (!StringUtil.isEmpty(dataInsbPerson)) {
							INSHPerson toInshPerson = new INSHPerson();
							toInshPerson.setTaskid(taskId);
							toInshPerson.setFairyoredi(processType);
							toInshPerson.setNodecode(taskStatus);
							toInshPerson = inshPersonService.queryOne(toInshPerson);
							if (!StringUtil.isEmpty(toInshPerson)) {
								PropertyUtils.copyProperties(toInshPerson, dataInsbPerson);
								inshPersonService.updateById(toInshPerson);
							} else {
								toInshPerson = new INSHPerson();
								PropertyUtils.copyProperties(toInshPerson, dataInsbPerson);
								toInshPerson.setFairyoredi(processType);
								toInshPerson.setNodecode(taskStatus);
								inshPersonService.insert(toInshPerson);
							}
						}
					}
					if (!StringUtil.isEmpty(dataInsbSpecifydriverhis)) {
						INSHSpecifydriverhis toInshSpecifydriverhis = new INSHSpecifydriverhis();
						toInshSpecifydriverhis.setTaskid(taskId);
						toInshSpecifydriverhis.setInscomcode(inscomcode);
						toInshSpecifydriverhis.setFairyoredi(processType);
						toInshSpecifydriverhis.setNodecode(taskStatus);
						toInshSpecifydriverhis = inshSpecifydriverhisService.queryOne(toInshSpecifydriverhis);
						if (!StringUtil.isEmpty(toInshSpecifydriverhis)) {
							PropertyUtils.copyProperties(toInshSpecifydriverhis, dataInsbSpecifydriverhis);
							inshSpecifydriverhisService.updateById(toInshSpecifydriverhis);
						} else {
							toInshSpecifydriverhis = new INSHSpecifydriverhis();
							PropertyUtils.copyProperties(toInshSpecifydriverhis, dataInsbSpecifydriverhis);
							toInshSpecifydriverhis.setFairyoredi(processType);
							toInshSpecifydriverhis.setNodecode(taskStatus);
							inshSpecifydriverhisService.insert(toInshSpecifydriverhis);
						}
					}
				}
			}
			this.log(taskId, inscomcode, "开始车主备份.....");
			// ---------------------------------车主备份-------------------------------
			INSBCarowneinfo fromInsbCarowneinfo = new INSBCarowneinfo();
			fromInsbCarowneinfo.setTaskid(taskId);
			fromInsbCarowneinfo = insbCarowneinfoService.queryOne(fromInsbCarowneinfo);
			INSBPerson fromInsbPersonCarOwer = new INSBPerson();
			if (!StringUtil.isEmpty(fromInsbCarowneinfo) && !StringUtil.isEmpty(fromInsbCarowneinfo.getPersonid())) {
				fromInsbPersonCarOwer = insbPersonService.queryById(fromInsbCarowneinfo.getPersonid());
				if (!StringUtil.isEmpty(fromInsbPersonCarOwer)) {
					INSHPerson toInshPerson = new INSHPerson();
					toInshPerson.setTaskid(taskId);
					toInshPerson.setFairyoredi(processType);
					toInshPerson.setNodecode(taskStatus);
					toInshPerson = inshPersonService.queryOne(toInshPerson);
					if (!StringUtil.isEmpty(toInshPerson)) {
						PropertyUtils.copyProperties(toInshPerson, fromInsbPersonCarOwer);
						inshPersonService.updateById(toInshPerson);
					} else {
						toInshPerson = new INSHPerson();
						PropertyUtils.copyProperties(toInshPerson, fromInsbPersonCarOwer);
						toInshPerson.setFairyoredi(processType);
						toInshPerson.setNodecode(taskStatus);
						inshPersonService.insert(toInshPerson);
					}
				}
				INSHCarowneinfo toCarowneinfo = new INSHCarowneinfo();
				toCarowneinfo.setTaskid(taskId);
				toCarowneinfo.setFairyoredi(processType);
				toCarowneinfo.setNodecode(taskStatus);
				toCarowneinfo = inshCarowneinfoService.queryOne(toCarowneinfo);
				if (!StringUtil.isEmpty(toCarowneinfo)) {
					PropertyUtils.copyProperties(toCarowneinfo, fromInsbCarowneinfo);
					inshCarowneinfoService.updateById(toCarowneinfo);
				} else {
					toCarowneinfo = new INSHCarowneinfo();
					PropertyUtils.copyProperties(toCarowneinfo, fromInsbCarowneinfo);
					toCarowneinfo.setFairyoredi(processType);
					toCarowneinfo.setNodecode(taskStatus);
					inshCarowneinfoService.insert(toCarowneinfo);
				}
			}

			this.log(taskId, inscomcode, "开始保单备份.....");
			// ---------------------------------保单备份-------------------------------
			INSBPolicyitem queryInsbPolicyitem = new INSBPolicyitem();
			queryInsbPolicyitem.setTaskid(taskId);
			queryInsbPolicyitem.setInscomcode(inscomcode);
			List<INSBPolicyitem> dataInsbPolicyitemList = insbPolicyitemService.queryList(queryInsbPolicyitem);
			if (dataInsbPolicyitemList.size() > 0) {
				INSHPolicyitem delInshPolicyitem = new INSHPolicyitem();
				delInshPolicyitem.setTaskid(taskId);
				delInshPolicyitem.setInscomcode(inscomcode);
				delInshPolicyitem.setFairyoredi(processType);
				delInshPolicyitem.setNodecode(taskStatus);
				inshPolicyitemService.delete(delInshPolicyitem);
				for (INSBPolicyitem dataInsbPolicyitem : dataInsbPolicyitemList) {
					if (!StringUtil.isEmpty(dataInsbPolicyitem)) {
						INSHPolicyitem toInshPolicyitem = new INSHPolicyitem();
						PropertyUtils.copyProperties(toInshPolicyitem, dataInsbPolicyitem);
						toInshPolicyitem.setFairyoredi(processType);
						toInshPolicyitem.setNodecode(taskStatus);
						inshPolicyitemService.insert(toInshPolicyitem);
					}
				}
			}

			this.log(taskId, inscomcode, "开始订单备份.....");
			// ---------------------------------订单备份-------------------------------
			INSBOrder queryInsbOrder = new INSBOrder();
			queryInsbOrder.setTaskid(taskId);
			queryInsbOrder.setPrvid(inscomcode);
			queryInsbOrder = insbOrderService.queryOne(queryInsbOrder);
			if (!StringUtil.isEmpty(queryInsbOrder)) {
				INSHOrder toInshOrder = new INSHOrder();
				toInshOrder.setTaskid(taskId);
				toInshOrder.setPrvid(inscomcode);
				toInshOrder.setFairyoredi(processType);
				toInshOrder.setNodecode(taskStatus);
				toInshOrder = inshOrderService.queryOne(toInshOrder);
				if (!StringUtil.isEmpty(toInshOrder)) {
					PropertyUtils.copyProperties(toInshOrder, queryInsbOrder);
					inshOrderService.updateById(toInshOrder);
				} else {
					toInshOrder = new INSHOrder();
					PropertyUtils.copyProperties(toInshOrder, queryInsbOrder);
					toInshOrder.setFairyoredi(processType);
					toInshOrder.setNodecode(taskStatus);
					inshOrderService.insert(toInshOrder);
				}
			}
			this.log(taskId, inscomcode, "开始支付备份.....");
			// ---------------------------------支付备份-------------------------------
			INSBOrderpayment queryInsbOrderpayment = new INSBOrderpayment();
			queryInsbOrderpayment.setTaskid(taskId);
			queryInsbOrderpayment = insbOrderpaymentService.queryOne(queryInsbOrderpayment);
			if (!StringUtil.isEmpty(queryInsbOrderpayment)) {
				INSHOrderpayment toInshOrderpayment = new INSHOrderpayment();
				toInshOrderpayment.setTaskid(taskId);
				toInshOrderpayment.setFairyoredi(processType);
				toInshOrderpayment.setNodecode(taskStatus);
				toInshOrderpayment = inshOrderpaymentService.queryOne(toInshOrderpayment);
				if (!StringUtil.isEmpty(toInshOrderpayment)) {
					PropertyUtils.copyProperties(toInshOrderpayment, queryInsbOrderpayment);
					inshOrderpaymentService.updateById(toInshOrderpayment);
				} else {
					toInshOrderpayment = new INSHOrderpayment();
					PropertyUtils.copyProperties(toInshOrderpayment, queryInsbOrderpayment);
					toInshOrderpayment.setFairyoredi(processType);
					toInshOrderpayment.setNodecode(taskStatus);
					inshOrderpaymentService.insert(toInshOrderpayment);
				}
			}

			this.log(taskId, inscomcode, "开始配送备份.....");
			// ---------------------------------配送备份-------------------------------
			INSBOrderdelivery queryInshOrderdelivery = new INSBOrderdelivery();
			queryInshOrderdelivery.setTaskid(taskId);
			queryInshOrderdelivery.setProviderid(inscomcode);
			queryInshOrderdelivery = insbOrderdeliveryService.queryOne(queryInshOrderdelivery);
			if (!StringUtil.isEmpty(queryInshOrderdelivery)) {
				INSHOrderdelivery toInshOrderdelivery = new INSHOrderdelivery();
				toInshOrderdelivery.setTaskid(taskId);
				toInshOrderdelivery.setFairyoredi(processType);
				toInshOrderdelivery.setProviderid(inscomcode);
				toInshOrderdelivery.setNodecode(taskStatus);
				toInshOrderdelivery = inshOrderdeliveryService.queryOne(toInshOrderdelivery);
				if (!StringUtil.isEmpty(toInshOrderdelivery)) {
					PropertyUtils.copyProperties(toInshOrderdelivery, queryInshOrderdelivery);
					inshOrderdeliveryService.updateById(toInshOrderdelivery);
				} else {
					toInshOrderdelivery = new INSHOrderdelivery();
					PropertyUtils.copyProperties(toInshOrderdelivery, queryInshOrderdelivery);
					toInshOrderdelivery.setFairyoredi(processType);
					toInshOrderdelivery.setNodecode(taskStatus);
					inshOrderdeliveryService.insert(toInshOrderdelivery);
				}
			}

			this.log(taskId, inscomcode, "开始报价总表备份.....");
			// ---------------------------------报价总表备份-------------------------------
			INSBQuotetotalinfo queryInsbQuotetotalinfo = new INSBQuotetotalinfo();
			queryInsbQuotetotalinfo.setTaskid(taskId);
			queryInsbQuotetotalinfo = insbQuotetotalinfoService.queryOne(queryInsbQuotetotalinfo);
			if (!StringUtil.isEmpty(queryInsbQuotetotalinfo)) {
				LogUtil.info(taskId + "@@" + inscomcode + "备份：queryInsbQuotetotalinfo的create和modify分别为：" + queryInsbQuotetotalinfo.getCreatetime() + "----" + queryInsbQuotetotalinfo.getModifytime());
				INSHQuotetotalinfo toInshQuotetotalinfo = new INSHQuotetotalinfo();
				toInshQuotetotalinfo.setTaskid(taskId);
				toInshQuotetotalinfo.setFairyoredi(processType);
				toInshQuotetotalinfo.setNodecode(taskStatus);
				toInshQuotetotalinfo = inshQuotetotalinfoService.queryOne(toInshQuotetotalinfo);
				if (!StringUtil.isEmpty(toInshQuotetotalinfo)) {
					LogUtil.info(taskId + "@@" + inscomcode + "1备份：toInshQuotetotalinfo的create和modify分别为：" + toInshQuotetotalinfo.getCreatetime() + "----" + toInshQuotetotalinfo.getModifytime());
					PropertyUtils.copyProperties(toInshQuotetotalinfo, queryInsbQuotetotalinfo);
					LogUtil.info(taskId + "@@" + inscomcode + "1备份copy完之后：toInshQuotetotalinfo的create和modify分别为：" + toInshQuotetotalinfo.getCreatetime() + "----"
							+ toInshQuotetotalinfo.getModifytime());
					toInshQuotetotalinfo.setModifytime(new Date());
					LogUtil.info(taskId + "@@" + inscomcode + "1备份重新set完之后：toInshQuotetotalinfo的create和modify分别为：" + toInshQuotetotalinfo.getCreatetime() + "----"
							+ toInshQuotetotalinfo.getModifytime());
					inshQuotetotalinfoService.updateById(toInshQuotetotalinfo);
				} else {
					toInshQuotetotalinfo = new INSHQuotetotalinfo();
					PropertyUtils.copyProperties(toInshQuotetotalinfo, queryInsbQuotetotalinfo);
					LogUtil.info(taskId + "@@" + inscomcode + "2备份copy完之后：toInshQuotetotalinfo的create和modify分别为：" + toInshQuotetotalinfo.getCreatetime() + "----"
							+ toInshQuotetotalinfo.getModifytime());
					toInshQuotetotalinfo.setFairyoredi(processType);
					toInshQuotetotalinfo.setNodecode(taskStatus);
					toInshQuotetotalinfo.setModifytime(new Date());
					LogUtil.info(taskId + "@@" + inscomcode + "2备份重新set完之后：toInshQuotetotalinfo的create和modify分别为：" + toInshQuotetotalinfo.getCreatetime() + "----"
							+ toInshQuotetotalinfo.getModifytime());
					inshQuotetotalinfoService.insert(toInshQuotetotalinfo);
				}
			}

			this.log(taskId, inscomcode, "开始报价分表备份.....");
			// ---------------------------------报价分表备份-------------------------------
			INSBQuotetotalinfo queryInsbQuotetotalinfo1 = new INSBQuotetotalinfo();
			queryInsbQuotetotalinfo1.setTaskid(taskId);
			queryInsbQuotetotalinfo1 = insbQuotetotalinfoService.queryOne(queryInsbQuotetotalinfo1);
			if (!StringUtil.isEmpty(queryInsbQuotetotalinfo1) && !StringUtil.isEmpty(queryInsbQuotetotalinfo1.getId())) {
				INSBQuoteinfo queryInsbQuoteinfo = new INSBQuoteinfo();
				queryInsbQuoteinfo.setQuotetotalinfoid(queryInsbQuotetotalinfo1.getId());
				queryInsbQuoteinfo.setInscomcode(inscomcode);
				queryInsbQuoteinfo = insbQuoteinfoService.queryOne(queryInsbQuoteinfo);
				if (!StringUtil.isEmpty(queryInsbQuoteinfo)) {
					LogUtil.info(taskId + "###" + inscomcode + "备份：queryInsbQuoteinfo的create和modify分别为：" + queryInsbQuoteinfo.getCreatetime() + "----" + queryInsbQuoteinfo.getModifytime());
					INSHQuoteinfo toInshQuoteinfo = new INSHQuoteinfo();
					toInshQuoteinfo.setQuotetotalinfoid(queryInsbQuotetotalinfo1.getId());
					toInshQuoteinfo.setFairyoredi(processType);
					toInshQuoteinfo.setNodecode(taskStatus);
					toInshQuoteinfo.setInscomcode(inscomcode);
					toInshQuoteinfo = inshQuoteinfoService.queryOne(toInshQuoteinfo);
					if (!StringUtil.isEmpty(toInshQuoteinfo)) {
						PropertyUtils.copyProperties(toInshQuoteinfo, queryInsbQuoteinfo);
						LogUtil.info(taskId + "###" + inscomcode + "备份copy1后：queryInsbQuoteinfo的create和modify分别为：" + toInshQuoteinfo.getCreatetime() + "----" + toInshQuoteinfo.getModifytime());
						toInshQuoteinfo.setModifytime(new Date());
						LogUtil.info(taskId + "###" + inscomcode + "备份set1后：queryInsbQuoteinfo的create和modify分别为：" + toInshQuoteinfo.getCreatetime() + "----" + toInshQuoteinfo.getModifytime());
						inshQuoteinfoService.updateById(toInshQuoteinfo);
					} else {
						toInshQuoteinfo = new INSHQuoteinfo();
						PropertyUtils.copyProperties(toInshQuoteinfo, queryInsbQuoteinfo);
						LogUtil.info(taskId + "###" + inscomcode + "备份copy2后：queryInsbQuoteinfo的create和modify分别为：" + toInshQuoteinfo.getCreatetime() + "----" + toInshQuoteinfo.getModifytime());
						toInshQuoteinfo.setFairyoredi(processType);
						toInshQuoteinfo.setNodecode(taskStatus);
						toInshQuoteinfo.setModifytime(new Date());
						LogUtil.info(taskId + "###" + inscomcode + "备份set2后：queryInsbQuoteinfo的create和modify分别为：" + toInshQuoteinfo.getCreatetime() + "----" + toInshQuoteinfo.getModifytime());
						inshQuoteinfoService.insert(toInshQuoteinfo);
					}
				}
			}

			this.log(taskId, inscomcode, "开始险种备份.....");
			// ---------------------------------险种备份-------------------------------
			INSBCarkindprice queryInsbCarkindprice = new INSBCarkindprice();
			queryInsbCarkindprice.setTaskid(taskId);
			queryInsbCarkindprice.setInscomcode(inscomcode);
			List<INSBCarkindprice> dataCarkindpriceList = insbCarkindpriceService.queryList(queryInsbCarkindprice);
			if (dataCarkindpriceList.size() > 0) {
				INSHCarkindprice delCarkindprice = new INSHCarkindprice();
				delCarkindprice.setTaskid(taskId);
				delCarkindprice.setInscomcode(inscomcode);
				delCarkindprice.setFairyoredi(processType);
				delCarkindprice.setNodecode(taskStatus);
				inshCarkindpriceService.delete(delCarkindprice);
			}
			for (INSBCarkindprice dataInsbCarkindprice : dataCarkindpriceList) {
				INSHCarkindprice inshCarkindprice = new INSHCarkindprice();
				PropertyUtils.copyProperties(inshCarkindprice, dataInsbCarkindprice);
				inshCarkindprice.setFairyoredi(processType);
				inshCarkindprice.setNodecode(taskStatus);
				inshCarkindprice.setModifytime(new Date());
				inshCarkindpriceService.insert(inshCarkindprice);
			}

			this.log(taskId, inscomcode, "开始车信息备份.....");
			// ---------------------------------车信息备份-------------------------------
			INSBCarinfohis queryInsbCarinfohis = new INSBCarinfohis();
			queryInsbCarinfohis.setTaskid(taskId);
			queryInsbCarinfohis.setInscomcode(inscomcode);
			queryInsbCarinfohis = insbCarinfohisService.queryOne(queryInsbCarinfohis);
			if (!StringUtil.isEmpty(queryInsbCarinfohis)) {
				INSHCarinfohis toInshCarinfohis = new INSHCarinfohis();
				toInshCarinfohis.setTaskid(taskId);
				toInshCarinfohis.setFairyoredi(processType);
				toInshCarinfohis.setNodecode(taskStatus);
				toInshCarinfohis.setInscomcode(inscomcode);
				toInshCarinfohis = inshCarinfohisService.queryOne(toInshCarinfohis);
				if (!StringUtil.isEmpty(toInshCarinfohis)) {
					PropertyUtils.copyProperties(toInshCarinfohis, queryInsbCarinfohis);
					inshCarinfohisService.updateById(toInshCarinfohis);
				} else {
					toInshCarinfohis = new INSHCarinfohis();
					PropertyUtils.copyProperties(toInshCarinfohis, queryInsbCarinfohis);
					toInshCarinfohis.setFairyoredi(processType);
					toInshCarinfohis.setNodecode(taskStatus);
					inshCarinfohisService.insert(toInshCarinfohis);
				}
			}

			this.log(taskId, inscomcode, "开始车型信息备份.....");
			// ---------------------------------车型信息备份-------------------------------
			INSBCarinfo queryInsbCarinfo = new INSBCarinfo();
			queryInsbCarinfo.setTaskid(taskId);
			queryInsbCarinfo = insbCarinfoService.queryOne(queryInsbCarinfo);
			if (!StringUtil.isEmpty(queryInsbCarinfo) && !StringUtil.isEmpty(queryInsbCarinfo.getId())) {
				INSBCarmodelinfohis dataInsbCarmodelinfohis = new INSBCarmodelinfohis();
				dataInsbCarmodelinfohis.setCarinfoid(queryInsbCarinfo.getId());
				dataInsbCarmodelinfohis.setInscomcode(inscomcode);
				dataInsbCarmodelinfohis = insbCarmodelinfohisService.queryOne(dataInsbCarmodelinfohis);
				if (!StringUtil.isEmpty(dataInsbCarmodelinfohis)) {
					INSHCarmodelinfohis toInshCarmodelinfohis = new INSHCarmodelinfohis();
					toInshCarmodelinfohis.setCarinfoid(queryInsbCarinfo.getId());
					toInshCarmodelinfohis.setFairyoredi(processType);
					toInshCarmodelinfohis.setNodecode(taskStatus);
					toInshCarmodelinfohis.setInscomcode(inscomcode);
					toInshCarmodelinfohis = inshCarmodelinfohisService.queryOne(toInshCarmodelinfohis);
					if (!StringUtil.isEmpty(toInshCarmodelinfohis)) {
						PropertyUtils.copyProperties(toInshCarmodelinfohis, dataInsbCarmodelinfohis);
						inshCarmodelinfohisService.updateById(toInshCarmodelinfohis);
					} else {
						toInshCarmodelinfohis = new INSHCarmodelinfohis();
						PropertyUtils.copyProperties(toInshCarmodelinfohis, dataInsbCarmodelinfohis);
						toInshCarmodelinfohis.setFairyoredi(processType);
						toInshCarmodelinfohis.setNodecode(taskStatus);
						inshCarmodelinfohisService.insert(toInshCarmodelinfohis);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Boolean saveHisInfo(String subTaskId, String processType, String taskStatus) {
		try {
			INSBQuoteinfo queryInsbQuoteinfo = new INSBQuoteinfo();
			queryInsbQuoteinfo.setWorkflowinstanceid(subTaskId);
			queryInsbQuoteinfo = insbQuoteinfoService.queryOne(queryInsbQuoteinfo);
			INSBQuotetotalinfo queryInsbQuotetotalinfo = new INSBQuotetotalinfo();
			queryInsbQuotetotalinfo = insbQuotetotalinfoService.queryById(queryInsbQuoteinfo.getQuotetotalinfoid());
			return this.saveHistoryData(queryInsbQuotetotalinfo.getTaskid(), queryInsbQuoteinfo.getInscomcode(), processType, taskStatus);
		} catch (Exception e) {
			LogUtil.info("手动成功出错");
			e.printStackTrace();
			return false;
		}
	}

	private String valiedatePolicyNo(JSONObject savePack) {
		if (savePack.get("baseSuiteInfo") != null) {
			JSONObject baseSuiteInfo = JSONObject.fromObject(savePack.get("baseSuiteInfo"));// 投保基本信息：（交强险信息、车船税信息、商业险信息（险种配置））
			if (!StringUtil.isEmpty(baseSuiteInfo.get("efcSuiteInfo"))) {
				if (savePack.get("sq") != null) {
					JSONObject sq = JSONObject.fromObject(savePack.get("sq"));// 出单信息
					if (StringUtil.isEmpty(sq.get("efcPolicyCode"))) {
						return "交强险保单号为空";
					}
				} else {
					return "sq信息空";
				}
			}
			if (!StringUtil.isEmpty(baseSuiteInfo.get("bizSuiteInfo"))) {
				if (savePack.get("sq") != null) {
					JSONObject sq = JSONObject.fromObject(savePack.get("sq"));// 出单信息
					if (StringUtil.isEmpty(sq.get("bizPolicyCode"))) {
						return "商业险保单号为空";
					}
				} else {
					return "sq信息空";
				}

			}
		}
		return null;
	}

	private String valiedatePropNo(JSONObject savePack) {
		if (savePack.get("baseSuiteInfo") != null) {
			JSONObject baseSuiteInfo = JSONObject.fromObject(savePack.get("baseSuiteInfo"));// 投保基本信息：（交强险信息、车船税信息、商业险信息（险种配置））
			if (!StringUtil.isEmpty(baseSuiteInfo.get("efcSuiteInfo"))) {
				if (savePack.get("sq") != null) {
					JSONObject sq = JSONObject.fromObject(savePack.get("sq"));// 出单信息
					if (StringUtil.isEmpty(sq.get("efcProposeNum"))) {
						return "交强险投保单号为空";
					}
				} else {
					return "sq信息空";
				}
			}
			if (!StringUtil.isEmpty(baseSuiteInfo.get("bizSuiteInfo"))) {
				if (savePack.get("sq") != null) {
					JSONObject sq = JSONObject.fromObject(savePack.get("sq"));// 出单信息
					if (StringUtil.isEmpty(sq.get("bizProposeNum"))) {
						return "商业险投保单号为空";
					}
				} else {
					return "sq信息空";
				}

			}
		}
		return null;
	}

	public static void main(String[] args) {
		LocalDate today = LocalDate.now();
		LocalDate previousYear = today.minus(1, ChronoUnit.YEARS);
		System.out.println("Date before 1 year : " + previousYear);
		LocalDate nextYear = today.plus(1, ChronoUnit.YEARS);
		System.out.println("Date after 1 year : " + nextYear);
		List<INSBRulequerycarinfo> list = new ArrayList<>();
		INSBRulequerycarinfo carinfo = new INSBRulequerycarinfo();
		carinfo.setOperator("rguizequery");list.add(carinfo);
		if(list == null || list.isEmpty() || !"guizequery".equals(list.get(0).getOperator())){
			System.out.println("yes"+list);
		}
		JSONObject JudgeJsonObject = JSONObject.fromObject("{\"ruleItem.restrictImportVehicleDisplacement\":\"true\"}");
		InterFaceServiceImpl ins = new InterFaceServiceImpl();
		if(1 == ins.getCarVehicleOriginByVin("Boooo") && JudgeJsonObject.getBoolean("ruleItem.restrictImportVehicleDisplacement")){
			System.out.println(ins.getCarVehicleOriginByVin("Boooo"));
		}else{
			System.out.println(JudgeJsonObject.getBoolean("ruleItem.restrictImportVehicleDisplacement"));
		}
		System.out.println(ins.getCarVehicleOriginByVin("Boooo")+""+JudgeJsonObject.getBoolean("ruleItem.restrictImportVehicleDisplacement"));
	}

	public String valiedatePremiu(JSONObject savePack) {
		JSONObject bizSuiteInfoJson = null, suiteDefJson = null;
		// efcSuiteInfoJson=null,taxSuiteInfo=null,
		JSONArray suiteDefListJson = null;
		BigDecimal bizPremiu = new BigDecimal(0);
		BigDecimal totalPremiu = new BigDecimal(0);
		BigDecimal totalbizPremiu = new BigDecimal(0);
		BigDecimal totalefcPremiu = new BigDecimal(0);
		BigDecimal totaltaxPremiu = new BigDecimal(0);
		if (savePack.get("sq") != null) {
			JSONObject sq = JSONObject.fromObject(savePack.get("sq"));// 出单信息
			if (!StringUtil.isEmpty(sq.get("totalCharge"))) {
				totalPremiu = BigDecimal.valueOf(Double.valueOf(sq.get("totalCharge").toString())).setScale(2, RoundingMode.HALF_UP);
			} else {
				return "总金额为空";
			}
			if (!StringUtil.isEmpty(sq.get("bizCharge"))) {
				totalbizPremiu = BigDecimal.valueOf(Double.valueOf(sq.get("bizCharge").toString())).setScale(2, RoundingMode.HALF_UP);
			}
			if (!StringUtil.isEmpty(sq.get("efcCharge"))) {
				totalefcPremiu = BigDecimal.valueOf(Double.valueOf(sq.get("efcCharge").toString())).setScale(2, RoundingMode.HALF_UP);
			}
			if (!StringUtil.isEmpty(sq.get("taxCharge"))) {
				totaltaxPremiu = BigDecimal.valueOf(Double.valueOf(sq.get("taxCharge").toString())).setScale(2, RoundingMode.HALF_UP);
			}
		} else {
			return "sq信息空";
		}
		if (totalPremiu.compareTo(totalbizPremiu.add(totalefcPremiu).add(totaltaxPremiu)) != 0) {
			return "sq中分项相加不等于总额";
		}
		if (savePack.get("baseSuiteInfo") != null) {
			JSONObject baseSuiteInfo = JSONObject.fromObject(savePack.get("baseSuiteInfo"));
			if (baseSuiteInfo.get("bizSuiteInfo") != null) {
				bizSuiteInfoJson = JSONObject.fromObject(baseSuiteInfo.get("bizSuiteInfo"));
				if (!StringUtil.isEmpty(bizSuiteInfoJson.get("suites"))) {
					suiteDefListJson = JSONArray.fromObject(bizSuiteInfoJson.get("suites"));
					if (!StringUtil.isEmpty(suiteDefListJson) && suiteDefListJson.size() > 0) {
						for (Object obj : suiteDefListJson) {
							suiteDefJson = JSONObject.fromObject(obj);
							if (!StringUtil.isEmpty(suiteDefJson.get("discountCharge"))) {
								bizPremiu = bizPremiu.add(BigDecimal.valueOf(Double.valueOf(suiteDefJson.get("discountCharge").toString())).setScale(6, RoundingMode.HALF_UP));
							} else {
								return "险别折扣保费为空";
							}
						}
					} else {
						return "险种明细信息为空";
					}
				} else {
					return "商业险中的险别为空";
				}
			}
		}
		if (bizPremiu.setScale(2, RoundingMode.HALF_UP).compareTo(totalbizPremiu) != 0) {
			return "险种分项相加不等于sq中商业总和";
		}
		return null;
	}

	public String getRbCode(String vehicleid, String insurancecompanyid, String taskid) {
		String flag = String.valueOf(CMRedisClient.getInstance().get("cm:config:jycode", "closed"));
		if ("true".equals(flag)) {
			return null;
		}
		String resultJson = "";
		String cifCode = "";
		if (StringUtil.isEmpty(vehicleid) || StringUtil.isEmpty(insurancecompanyid)) {
			LogUtil.info("==1==获取精友code：taskid=" + taskid + ",inscomcode=" + insurancecompanyid + ",vehicleid：" + vehicleid + "====");
			return resultJson;
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("PacketType", "xml");
		Map<String, Object> productRequestsMap = new HashMap<String, Object>();
		Map<String, Object> vehicleRequestMap = new HashMap<String, Object>();
		vehicleRequestMap.put("VehicleCode", vehicleid);
		Map<String, Object> insuranceRequestMap = new HashMap<String, Object>();
		Map<String, Object> insuranceMap = new HashMap<String, Object>();
		insuranceMap.put("InsuranceCode", insurancecompanyid.substring(0, 4));
		insuranceRequestMap.put("Insurance", insuranceMap);
		productRequestsMap.put("VehicleRequest", vehicleRequestMap);
		productRequestsMap.put("InsuranceRequest", insuranceRequestMap);
		paraMap.put("ProductRequests", productRequestsMap);
		paraMap.put("taskId", taskid);
		try {
			LogUtil.info("==2==获取精友code：taskid=" + taskid + ",inscomcode=" + insurancecompanyid + ",url：" + ValidateUtil.getConfigValue("cloudquery.url") + "/output/getInsureXML" + ",请求参数："
					+ JSONObject.fromObject(paraMap).toString() + "====");
			resultJson = HttpClientUtil.doPostJsonString(ValidateUtil.getConfigValue("cloudquery.url") + "/output/getInsureXML", JSONObject.fromObject(paraMap).toString());
			LogUtil.info("==3==获取精友code：taskid=" + taskid + ",inscomcode=" + insurancecompanyid + ",resultJson：" + resultJson + "====");
			if (!StringUtil.isEmpty(resultJson)) {
				JSONObject jsonObject = JSONObject.fromObject(resultJson).getJSONObject("productResponses");
				JSONObject jsonObject1 = jsonObject.getJSONObject("insuranceResponse");
				JSONObject jsonObject2 = jsonObject1.getJSONObject("insurance");
				if (!StringUtil.isEmpty(jsonObject2.get("vehicleCode"))) {
					cifCode = jsonObject2.get("vehicleCode").toString();
					return cifCode;
				}
			} else {
				LogUtil.info("==5=第一次失败，开始第二次=获取精友code：taskid=" + taskid + ",inscomcode=" + insurancecompanyid + ",url：" + ValidateUtil.getConfigValue("cloudquery.url") + "/output/getInsureXML"
						+ ",请求参数：" + JSONObject.fromObject(paraMap).toString() + "====");
				String resultJson2 = HttpClientUtil.doPostJsonString(ValidateUtil.getConfigValue("cloudquery.url") + "/output/getInsureXML", JSONObject.fromObject(paraMap).toString());
				LogUtil.info("==6==获取精友code：taskid=" + taskid + ",inscomcode=" + insurancecompanyid + ",resultJson：" + resultJson2 + "====");
				if (!StringUtil.isEmpty(resultJson2)) {
					JSONObject jsonObject = JSONObject.fromObject(resultJson2).getJSONObject("productResponses");
					JSONObject jsonObject1 = jsonObject.getJSONObject("insuranceResponse");
					JSONObject jsonObject2 = jsonObject1.getJSONObject("insurance");
					if (!StringUtil.isEmpty(jsonObject2.get("vehicleCode"))) {
						cifCode = jsonObject2.get("vehicleCode").toString();
						LogUtil.info("==7=第二次成功=获取精友code：taskid=" + taskid + ",inscomcode=" + insurancecompanyid + ",url：" + ValidateUtil.getConfigValue("cloudquery.url") + "/output/getInsureXML"
								+ ",请求参数：" + JSONObject.fromObject(paraMap).toString() + "====");
						return cifCode;
					}
				}
			}
		} catch (Exception e) {
			LogUtil.info("==4==获取精友code：taskid=" + taskid + ",inscomcode=" + insurancecompanyid + ",获取错误" + e.getMessage() + "====");
			return cifCode;
		}
		return cifCode;
	}

	/**
	 *
	 * 删除历史定时任务
	 *
	 * @param sched
	 * @param jobName
	 */
	private void deleteHistoryJob(Scheduler sched, String jobName, String taskName, String taskId, String insurancecompanyid) {
		LogUtil.info("删除历史定时任务=taskid=" + taskId + ",inscomcode=" + insurancecompanyid + "==" + taskName + "job====" + jobName);
		JobKey jobKey = new JobKey(jobName);
		try {
			CMRedisClient.getInstance().del(6, "Scheduler", jobName);
			sched.deleteJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	@Resource
	private INSCDeptService inscDeptservice;
	@Resource
    private INSBCarconfigDao insbCarconfigDao;
	@Override
	public Map<String, Object> getGzQueryData(String taskId, String insurancecompanyid, String processType, String taskType, String monitorid) {
		Map<String, Object> resultmap = new HashMap<String, Object>();
		try {
			// 协议id#供应商id#出单网点id#网销传统类型
			String alldata = getDeptFromthis(taskId,insurancecompanyid);
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(taskId);
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			List<INSBQuoteinfo> insbQuoteinfos = insbQuoteinfoService.getQuoteinfosByInsbQuotetotalinfoid(quotetotalinfo.getId());
			boolean isQuoteInscom = false;
			for(INSBQuoteinfo insbQuoteinfo:insbQuoteinfos){
				if(insbQuoteinfo.getInscomcode().startsWith(insurancecompanyid)){
					isQuoteInscom = true;
				}
			}
			if (StringUtil.isEmpty(alldata) || alldata.split("#").length != 4) {
				// 转人工规则报价
//				appInsuredQuoteService.saveFlowerrorToManWork(taskId, insurancecompanyid, "未拥有备用平台查询能力，转人工", "4");
//				resultmap.put("error", "未拥有备用平台查询能力");
//				return resultmap;
				INSCDept deptPlatform = inscDeptservice.getPlatformDept(insbQuoteinfos.get(0).getDeptcode());
				INSBAgreement agreementquery = new INSBAgreement();
				agreementquery.setAgreementstatus("1");//有效的协议
				agreementquery.setProviderid(insurancecompanyid);
				agreementquery = insbagreementservice.queryOne(agreementquery);//查找平台查询公司的对应协议id
				//启动的备用平台查询是属于平台级别的则拼装平台的信息用于组装精灵数据
				if(null == agreementquery){
					alldata = "#"+insurancecompanyid+"#"+(null!=deptPlatform?deptPlatform.getComcode():(insbQuoteinfos.get(0).getDeptcode().substring(0, 5)+"00000"))+"#";
				}else{
					alldata = agreementquery.getId()+"#"+insurancecompanyid+"#"+(null!=deptPlatform?deptPlatform.getComcode():(insbQuoteinfos.get(0).getDeptcode().substring(0, 5)+"00000"))+"#";
				}
			}
			LogUtil.info("规则平台查询获取查询数据taskid=" + taskId + "=getGzQueryData供应商id=" + insurancecompanyid + "=匹配出来的协议出单网点为=" + alldata + "=processType=" + processType + "=taskType=" + taskType
					+ "=monitorid=" + monitorid);
			if(isQuoteInscom){
				resultmap.put("error", "包含在报价公司里,不启用备用平台查询.启动失败");
				return resultmap;
			}
			if(null!=insbQuoteinfos&&insbQuoteinfos.size()>0){//是启动了备用规则平台查询,则随便赋值一个真实报价的公司，用于查询车辆信息
				insurancecompanyid = insbQuoteinfos.get(0).getInscomcode();
			}else{
				resultmap.put("error", "报价信息不存在,备用平台查询启动失败");
				return resultmap;
			}
			INSBCarconfig carconfig = new INSBCarconfig();
	        carconfig.setTaskid(taskId);
	        List<INSBCarconfig> carconfigs = insbCarconfigDao.selectList(carconfig);
	        if(null!=carconfigs&&carconfigs.size()>0 && StringUtil.isNotEmpty(carconfigs.get(0).getNoti())){//取后期修改保险配置的保险公司为其平台查询获取数据保险公司
				insurancecompanyid = carconfigs.get(0).getNoti();
				LogUtil.info("规则平台查询获取查询数据taskid=" + taskId + "=getGzQueryData修改保险配置之后的保险公司=" + carconfigs.get(0).getNoti());
			}
			resultmap = getPacket(taskId, insurancecompanyid, processType, "1111_2222", taskType);
			String deptid = alldata.split("#")[2];
			String inscomcode = alldata.split("#")[1];
			String agreementid = alldata.split("#")[0];
			resultmap.put("businessId", taskId + "@" + inscomcode);
			resultmap.put("insComId", inscomcode.substring(0, 4));
			LogUtil.info("规则平台查询获取查询数据taskid=" + taskId + "=getGzQueryData拥有规则查询能力的供应商id=" + inscomcode);
			if (!resultmap.containsKey("error")) {
				// 保险公司配置
				ConfigInfo configInfo = appQuotationService.getConfigInfo(inscomcode, deptid, processType);
				// 供应商信息
				List<ProviderInfo> providerInfoList = this.getProviderInfoList(taskId, inscomcode);// 供应商列表
				// 车辆信息
				CarInfo carInfo = this.getCarInfoGZQuery(taskId, insurancecompanyid, inscomcode, deptid, agreementid);// 车信息
				if (null == carInfo || null == providerInfoList || null == configInfo) {
					resultmap.put("error", "平台查询组织车辆信息数据出现问题");
					//appInsuredQuoteService.saveFlowerrorToManWork(taskId, insurancecompanyid, "平台查询报价车型年款无法匹配，转人工", "4");
				} else {
					resultmap.put("configInfo", configInfo);
					resultmap.put("carInfo", carInfo);
					resultmap.put("providerInfoList", providerInfoList);
				}
			}
			LogUtil.info("平台查询获取查询数据taskid=" + taskId + "=最终匹配出来的数据=" + JSONObject.fromObject(resultmap).toString());
		} catch (Exception e) {
			e.printStackTrace();
			//appInsuredQuoteService.saveFlowerrorToManWork(taskId, insurancecompanyid, "规则平台查询出错啦，转人工", "4");
		}
		return resultmap;
	}

	private String getDeptFromthis(String taskid,String reservedInscom) {
		String inscomcode = getNextProvid();
		if(StringUtil.isNotEmpty(reservedInscom)){
			inscomcode = reservedInscom;
		}
		String alldata = appInsuredQuoteService.judgeThisPidIsAgentHave(taskid, inscomcode);
		LogUtil.info("规则平台查询获取代理人拥有的供应商以及出单网点taskid=" + taskid + "=第一次查询供应商=" + inscomcode + "=返回数据=" + alldata);
//		if (StringUtil.isEmpty(alldata)) {
//			inscomcode = getNextProvid();
//			alldata = appInsuredQuoteService.judgeThisPidIsAgentHave(taskid, inscomcode);
//			LogUtil.info("规则平台查询获取代理人拥有的供应商以及出单网点taskid=" + taskid + "=第二次查询供应商=" + inscomcode + "=返回数据=" + alldata);
//		}
		return alldata;
	}

	/**
	 * 目前只有太保和中华，平均分配
	 *
	 * @return
	 */
	private String getNextProvid() {
		String result = CANUSEDPROID[0];
		redisClient.addOne("rulequerynum", "nextnumber");
		Object num = redisClient.get("rulequerynum", "nextnumber");
		if (null != num) {
			int i = Integer.parseInt(num.toString());
			result = CANUSEDPROID[i % 2];
		}
		LogUtil.info("规则平台查询太保中华查询平均分配pid=" + result + "====" + new Date());
		return result;
	}

	private INSBCarmodelinfohis getCarmodelinfoToData(String taskid, String insurancecompanyid, String inscomcode, String agreementid) {
		LogUtil.info("规则报价开始匹配年款saveCarmodelinfoToData：taskid:" + taskid + "=真正报价的供应商id=" + insurancecompanyid + "=拥有规则查询能力的供应商id=" + inscomcode + "=协议id=" + agreementid);
		INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setTaskid(taskid);
		INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
		INSBCarmodelinfohis carmodelinfohis = null;

		if (null != carinfo) {
			String vin = carinfo.getVincode();

			INSBCarmodelinfohis insbCarmodelinfohis = new INSBCarmodelinfohis();
			insbCarmodelinfohis.setCarinfoid(carinfo.getId());
			insbCarmodelinfohis.setInscomcode(insurancecompanyid);
			insbCarmodelinfohis = insbCarmodelinfohisDao.selectOne(insbCarmodelinfohis);

			if (insbCarmodelinfohis == null) {
				INSBCarmodelinfo insbCarmodelinfo = new INSBCarmodelinfo();
				insbCarmodelinfo.setCarinfoid(carinfo.getId());
				INSBCarmodelinfo carmodelinfo = insbCarmodelinfoDao.selectOne(insbCarmodelinfo);

				if (null != carmodelinfo) {
					insbCarmodelinfohis = new INSBCarmodelinfohis();

					try {
						PropertyUtils.copyProperties(insbCarmodelinfohis, carmodelinfo);
					} catch (Exception e) {
						LogUtil.error("carInfoId=" + carmodelinfo.getCarinfoid() + ",复制车型信息出错: " + e.getMessage());
						e.printStackTrace();
					}
				}
			}

			if (null != insbCarmodelinfohis) {
				String pointPrice = "";
				if (null != insbCarmodelinfohis.getPolicycarprice())
					pointPrice = insbCarmodelinfohis.getPolicycarprice().toString();

				CarModelInfoBean carModelInfoBean = getGzruleQueryCarmodelinfo(carinfo.getVincode(),"", taskid, inscomcode, insbCarmodelinfohis , ModelUtil.conbertToString(carinfo.getRegistdate()), pointPrice, agreementid);
				if(carModelInfoBean != null && "-1".equals(carModelInfoBean.getId()))
					carModelInfoBean = null;
				if (null != carModelInfoBean) {
					String jycode = carModelInfoBean.getJycode();
					String rbcode = carModelInfoBean.getRbcode();
					if (StringUtil.isEmpty(jycode)) {
						LogUtil.info("规则报价车型年款匹配saveCarmodelinfoToData：taskid:" + taskid + "规则报价车型年款匹配没有返回jycode,inscomcode:" + inscomcode);
						appInsuredQuoteService.saveFlowerrorToManWork(taskid, insurancecompanyid, "规则报价车型年款匹配没有返回jycode，转人工处理", "4");
					}
					if (StringUtil.isEmpty(rbcode)) {
						LogUtil.info("规则报价车型年款匹配saveCarmodelinfoToData：taskid:" + taskid + ",规则报价车型年款匹配没有返回rbcode,inscomcode:" + inscomcode);
						appInsuredQuoteService.saveFlowerrorToManWork(taskid, insurancecompanyid, "规则报价车型年款匹配没有返回rbcode，转人工处理", "4");
					}
					carmodelinfohis = new INSBCarmodelinfohis();
					carmodelinfohis.setOperator(null == insbCarmodelinfohis.getOperator() ? "" : insbCarmodelinfohis.getOperator());
					carmodelinfohis.setCreatetime(new Date());
					carmodelinfohis.setAnalogyprice(carModelInfoBean.getAnalogyprice());
					carmodelinfohis.setAnalogytaxprice(carModelInfoBean.getAnalogytaxprice());
					carmodelinfohis.setBrandname(carModelInfoBean.getBrandname());
					carmodelinfohis.setDisplacement(convertToDouble(carModelInfoBean.getDisplacement()));
					carmodelinfohis.setFactoryname(carModelInfoBean.getFactoryname());
					carmodelinfohis.setFamilyname(carModelInfoBean.getFamilyname());
					carmodelinfohis.setFullweight(insbCarmodelinfohis.getFullweight());
					carmodelinfohis.setGearbox(carModelInfoBean.getGearbox());
					carmodelinfohis.setCarVehicleOrigin(getCarVehicleOriginByVin(vin));
					carmodelinfohis.setLoads(insbCarmodelinfohis.getLoads());
					carmodelinfohis.setUnwrtweight(insbCarmodelinfohis.getUnwrtweight());
					carmodelinfohis.setPrice(carModelInfoBean.getPrice());
					carmodelinfohis.setSeat(insbCarmodelinfohis.getSeat());
					carmodelinfohis.setTaxprice(carModelInfoBean.getTaxprice());
					carmodelinfohis.setSyvehicletypecode(carModelInfoBean.getSyvehicletype());
					carmodelinfohis.setSyvehicletypename(carModelInfoBean.getSyvehicletypename());
					carmodelinfohis.setVehicleid(carModelInfoBean.getVehicleId());
					carmodelinfohis.setStandardfullname(carModelInfoBean.getVehiclename());
					carmodelinfohis.setJgVehicleType(getjgVehicleTypeAndCodeName("VehicleType", carModelInfoBean.getVehicletype()));
					carmodelinfohis.setListedyear(carModelInfoBean.getMaketdate());
					carmodelinfohis.setCarinfoid(carinfo.getId());
					carmodelinfohis.setInscomcode(inscomcode);
					// 指定车价
					carmodelinfohis.setCarprice(insbCarmodelinfohis.getCarprice());
					carmodelinfohis.setPolicycarprice(insbCarmodelinfohis.getPolicycarprice());
					carmodelinfohis.setJyCode(jycode);
					carmodelinfohis.setRbCode(rbcode);
				} else {
					LogUtil.info("saveCarmodelinfoToData没有匹配到年款：taskid:" + taskid + ",inscomcode:" + inscomcode + "==interFaceService.getCarModelInfo接口没返回数据");
					// appInsuredQuoteService.saveFlowerrorToManWork(taskid,insurancecompanyid,
					// "规则报价车型年款无法匹配，转人工规则", "4");
				}
			}
		}
		return carmodelinfohis;
	}

	/**
	 * 转换不合法默认为0
	 *
	 * @param str
	 * @return
	 */
	private double convertToDouble(String str) {
		return StringUtil.isEmpty(str) || !ModelUtil.isFloatNumber(str) ? 0d : Double.parseDouble(str);
	}

	/**
	 * 产地类型 vin吗 大写的L 国产车 0 其他进口车 1
	 *
	 * @param vin
	 * @return
	 */
	private int getCarVehicleOriginByVin(String vin) {
		int res = 0;
		if (StringUtil.isEmpty(vin)) {
			return res;
		}
		char manufacturer = vin.charAt(0);
		if (manufacturer != 'L') {
			res = 1;
		}
		return res;
	}

	/**
	 * 交管车辆类型
	 *
	 * @param codeType
	 *            VehicleType
	 * @param codeName
	 * @return
	 */
	private int getjgVehicleTypeAndCodeName(String codeType, String codeName) {
		int result = 0;
		// 轿车
		if (StringUtil.isEmpty(codeName) && "VehicleType".equals(codeType)) {
			result = 13;
		} else {
			// 没查到其他类型
			Map<String, String> map = new HashMap<String, String>();
			map.put("codetype", codeType);
			map.put("codename", null == codeName ? "" : codeName.trim());
			String value = inscCodeDao.selectCodeValueByCodeName(map);
			result = StringUtil.isEmpty(value) ? 30 : Integer.parseInt(value);
		}
		return result;
	}

	/**
	 * 获取规则报价才车辆信息数据
	 *
	 * @param taskId
	 * @param insurancecompanyid
	 *            真正报价的供应商
	 * @param tempinscomcode
	 *            拥有规则报价能力的供应商
	 * @param deptid
	 *            出单网点
	 * @return
	 */
	private CarInfo getCarInfoGZQuery(String taskId, String insurancecompanyid, String tempinscomcode, String deptid, String agreementid) {
		LogUtil.info("规则平台查询获取车辆信息开始taskid=" + taskId + "=实际报价的供应商=" + insurancecompanyid + "=拥有规则报价能力的供应商=" + tempinscomcode + "=出单网点=" + deptid + "=协议id=" + agreementid);
		CarInfo carInfo = null;
		INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setTaskid(taskId);
		insbCarinfo = insbCarinfoService.queryOne(insbCarinfo);
		if (null == insbCarinfo) {
			return carInfo;
		}

		INSBCarmodelinfohis dataInsbCarmodelinfohis = getCarmodelinfoToData(taskId, insurancecompanyid, tempinscomcode, agreementid);
		if (null == dataInsbCarmodelinfohis) {
			return carInfo;
		}

		INSBCarinfohis dataInsbCarinfo = new INSBCarinfohis();
		dataInsbCarinfo.setTaskid(taskId);
		dataInsbCarinfo.setInscomcode(insurancecompanyid);
		dataInsbCarinfo = insbCarinfohisDao.selectOne(dataInsbCarinfo);
		if (dataInsbCarinfo == null) {
			dataInsbCarinfo = new INSBCarinfohis();
			try {
				PropertyUtils.copyProperties(dataInsbCarinfo, insbCarinfo);
			} catch (Exception e) {
				LogUtil.error("taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",复制车辆信息出错: " + e.getMessage());
				e.printStackTrace();
			}
		}

		carInfo = new CarInfo();
		if ("新车未上牌".equals(dataInsbCarinfo.getCarlicenseno())) // 逻辑不严谨
			carInfo.setNoLicenseFlag(true);
		else
			carInfo.setNoLicenseFlag(false);
		// carInfo.setNoLicenseFlag("1".equals(dataInsbCarinfo.getIsNew()));//
		// 未上牌标记
		carInfo.setPlateNum(dataInsbCarinfo.getCarlicenseno());// 车牌号
		if (!StringUtil.isEmpty(dataInsbCarinfo.getCarproperty())) {
			Map<String, Integer> plateData = commonQuoteinfoService.getPlateType(taskId, insurancecompanyid, dataInsbCarinfo.getCarproperty(), dataInsbCarmodelinfohis.getSeat(), dataInsbCarmodelinfohis.getUnwrtweight());
			carInfo.setPlateColor(plateData.get("plateColor"));// 号牌颜色
			carInfo.setPlateType(plateData.get("plateType"));// 号牌种类;
		} else {
			LogUtil.info("==错误==根据车辆性质判断车牌号类型和颜色：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",使用类型：" + dataInsbCarinfo.getCarproperty() + ",座位数：" + dataInsbCarmodelinfohis.getSeat()
					+ ",核定载重量：" + dataInsbCarmodelinfohis.getUnwrtweight() + "====");
			if (!StringUtil.isEmpty(dataInsbCarinfo.getPlatecolor())) {
				carInfo.setPlateColor(dataInsbCarinfo.getPlatecolor());// 号牌颜色
			}
			if (!StringUtil.isEmpty(dataInsbCarinfo.getPlateType())) {
				carInfo.setPlateType(dataInsbCarinfo.getPlateType());// 号牌种类
			}
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getGlassType())) {
			carInfo.setGlassType(dataInsbCarmodelinfohis.getGlassType() - 1);// 玻璃类型
		}
		carInfo.setVin(dataInsbCarinfo.getVincode());// 车架号
		carInfo.setEngineNum(dataInsbCarinfo.getEngineno());// 发动机号
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getJgVehicleType())) {
			carInfo.setJgVehicleType(dataInsbCarmodelinfohis.getJgVehicleType());// 交管车辆类型
		}

		if (!StringUtil.isEmpty(dataInsbCarinfo.getCarproperty())) {
			INSCCode queryCode = new INSCCode();
			queryCode.setCodetype("UseProps");
			queryCode.setCodevalue(dataInsbCarinfo.getCarproperty());
			queryCode = inscCodeService.queryOne(queryCode);
			LogUtil.info("====根据车辆性质判断交管类型：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",使用类型：" + (queryCode!=null ? queryCode.getCodename() : "") + "====");

			if (!StringUtil.isEmpty(queryCode)) {
				Integer jgvt = commonQuoteinfoService.getJgVehicleType(taskId, insurancecompanyid,
						dataInsbCarinfo.getCarproperty(), dataInsbCarmodelinfohis.getSeat(), dataInsbCarmodelinfohis.getUnwrtweight());
				LogUtil.info("====根据车辆性质判断交管类型：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",结果：" + jgvt + "====");
				if (jgvt != null) {
					carInfo.setJgVehicleType(jgvt);
				}
			}
		} else {
			LogUtil.info("==错误=则默认=根据车辆性质判断交管类型：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",使用类型：" + dataInsbCarinfo.getCarproperty() + ",座位数：" + dataInsbCarmodelinfohis.getSeat()
					+ ",核定载重量：" + dataInsbCarmodelinfohis.getUnwrtweight() + "====");
			carInfo.setJgVehicleType(13);
		}

		if (!StringUtil.isEmpty(dataInsbCarinfo.getCarproperty())) {
			carInfo.setUseProps(Integer.parseInt(dataInsbCarinfo.getCarproperty()));// 使用性质
		}
		if (!StringUtil.isEmpty(dataInsbCarinfo.getProperty())) {
			carInfo.setCarUserType(Integer.parseInt(dataInsbCarinfo.getProperty()));// 车辆用户类型
		}
		carInfo.setFirstRegDate(dataInsbCarinfo.getRegistdate());// 初登日期
		carInfo.setIneffectualDate(dataInsbCarinfo.getIneffectualDate());// 检验有效日期
		carInfo.setRejectDate(dataInsbCarinfo.getRejectDate());// 强制有效期
		carInfo.setLastCheckDate(dataInsbCarinfo.getLastCheckDate());// 最近定检日期
		carInfo.setIsTransfer("1".equals(dataInsbCarinfo.getIsTransfercar()));// 是过户车
		carInfo.setTransferDate(dataInsbCarinfo.getTransferdate());// 过户转移登记日期

		boolean flag = true;
		INSBLastyearinsureinfo insbLastyearinsureinfo = insbRulequerycarinfoDao.queryLastYearClainInfo(taskId);
		LogUtil.info("====根据初等日期判断是否新车：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",初等日期是：" + dataInsbCarinfo.getRegistdate() + ",投保类型是："
				+ insbLastyearinsureinfo.getFirstinsuretype());
		if (!StringUtil.isEmpty(INSBLastyearinsureinfo.convertFirstInsureTypeToCm(insbLastyearinsureinfo.getFirstinsuretype()))) {
			flag = false;
			if ("1".equals(INSBLastyearinsureinfo.convertFirstInsureTypeToCm(insbLastyearinsureinfo.getFirstinsuretype()))) {
				carInfo.setIsNew(true);// 新车标志
			} else {
				carInfo.setIsNew(false);// 新车标志
			}
		}

		if (flag) {
			if (!StringUtil.isEmpty(dataInsbCarinfo.getRegistdate())) {
				INSBPolicyitem queryInsbPolicyitem = new INSBPolicyitem();
				queryInsbPolicyitem.setTaskid(taskId);
				queryInsbPolicyitem.setInscomcode(insurancecompanyid);
				queryInsbPolicyitem.setRisktype("0");
				INSBPolicyitem dateInsbPolicyitem = new INSBPolicyitem();
				dateInsbPolicyitem = insbPolicyitemService.queryOne(queryInsbPolicyitem);
				if (!StringUtil.isEmpty(dateInsbPolicyitem) && !StringUtil.isEmpty(dateInsbPolicyitem.getStartdate())) {
					LogUtil.info("====根据初等日期判断是否新车：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",初等日期是：" + dataInsbCarinfo.getRegistdate() + "," + ",商业险起保日期是："
							+ dateInsbPolicyitem.getStartdate() + ",往后270天是：" + LocalDate.parse(DateUtil.toString(dateInsbPolicyitem.getStartdate(), DateUtil.Format_Date)).minusDays(270) + ",====");
					if (LocalDate.parse(DateUtil.toString(dataInsbCarinfo.getRegistdate(), DateUtil.Format_Date)).isAfter(LocalDate.now().minusDays(270))) {
						carInfo.setIsNew(true);// 新车标志
					}
				} else {
					LogUtil.info("====根据初等日期判断是否新车：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",初等日期是：" + dataInsbCarinfo.getRegistdate() + "," + ",商业险起保日期是：空,换为交强险起保日期进行判断；");
					INSBPolicyitem queryInsbPolicyitem1 = new INSBPolicyitem();
					queryInsbPolicyitem1.setTaskid(taskId);
					queryInsbPolicyitem1.setInscomcode(insurancecompanyid);
					queryInsbPolicyitem1.setRisktype("1");
					INSBPolicyitem dateInsbPolicyitem1 = new INSBPolicyitem();
					dateInsbPolicyitem1 = insbPolicyitemService.queryOne(queryInsbPolicyitem1);
					if (!StringUtil.isEmpty(dateInsbPolicyitem1) && !StringUtil.isEmpty(dateInsbPolicyitem1.getStartdate())) {
						LogUtil.info("====根据初等日期判断是否新车：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",初等日期是：" + dataInsbCarinfo.getRegistdate() + "," + ",交强险起保日期是："
								+ dateInsbPolicyitem1.getStartdate() + ",往后270天是：" + LocalDate.parse(DateUtil.toString(dateInsbPolicyitem1.getStartdate(), DateUtil.Format_Date)).minusDays(270)
								+ ",====");
						if (LocalDate.parse(DateUtil.toString(dataInsbCarinfo.getRegistdate(), DateUtil.Format_Date)).isAfter(LocalDate.now().minusDays(270))) {
							carInfo.setIsNew(true);// 新车标志
						}
					} else {
						LogUtil.info("====根据初等日期判断是否新车：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",初等日期是：" + dataInsbCarinfo.getRegistdate() + ","
								+ ",商业险起保日期是：空,换为交强险起保日期进行判断；交强险起保日期也为空，没办法了");
						carInfo.setIsNew(false);// 新车标志
					}
				}
			} else {
				LogUtil.info("====根据初等日期判断是否新车：taskid=" + taskId + ",inscomcode=" + insurancecompanyid + ",初等日期是：" + dataInsbCarinfo.getRegistdate() + "," + ",当前时间往前推270天是："
						+ LocalDate.now().minusDays(270) + ",====");
				carInfo.setIsNew(false);// 新车标志
			}
		}
		carInfo.setCarBrandName(dataInsbCarmodelinfohis.getBrandname());// 车型品牌名称
		carInfo.setCarModelName(dataInsbCarmodelinfohis.getStandardfullname());// 车型名称
		// carInfo.setCarModelMisc(carModelMisc);//车型信息
		// carInfo.setCarModelMiscStream(carModelMisc);//车型信息
		carInfo.setRbCarModelName(dataInsbCarmodelinfohis.getRbCarModelName());// 人保车型名称
		carInfo.setJyCarModelName(dataInsbCarmodelinfohis.getJyCarModelName());// 精友车型名称
		carInfo.setBwCode(dataInsbCarmodelinfohis.getBwCode());// 内部车型code
		if (StringUtil.isEmpty(dataInsbCarmodelinfohis.getJyCode())) {
			if (StringUtil.isEmpty(dataInsbCarmodelinfohis.getRbCode())) {
				String cifCode = this.getRbCode(dataInsbCarmodelinfohis.getVehicleid(), insurancecompanyid, taskId);
				carInfo.setJyCode(cifCode);// 精友车型code
				carInfo.setRbCode(cifCode);// 人保车型code
			} else {
				carInfo.setJyCode(dataInsbCarmodelinfohis.getRbCode());// 精友车型code
				carInfo.setRbCode(dataInsbCarmodelinfohis.getRbCode());// 人保车型code
			}
		} else {
			carInfo.setJyCode(dataInsbCarmodelinfohis.getJyCode());// 精友车型code
			carInfo.setRbCode(dataInsbCarmodelinfohis.getRbCode());// 人保车型code
		}
		carInfo.setIsRbMatch("1".equals(dataInsbCarmodelinfohis.getIsRbMatch()));// 是否人保code匹配
		carInfo.setIsJyMatch("1".equals(dataInsbCarmodelinfohis.getIsJyMatch()));// 是否精友code匹配
		carInfo.setInsuranceCode(dataInsbCarmodelinfohis.getInsuranceCode());// 保险公司车型Code
		carInfo.setCarPriceType(!StringUtil.isEmpty(dataInsbCarmodelinfohis.getCarprice()) ? dataInsbCarmodelinfohis.getCarprice() : "0");// 车价选择，最高最低自定义
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getPolicycarprice()) && "2".equals(dataInsbCarmodelinfohis.getCarprice())) {
			carInfo.setDefinedCarPrice(BigDecimal.valueOf(dataInsbCarmodelinfohis.getPolicycarprice()).setScale(5, RoundingMode.HALF_UP));// 自定义车价金额
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getListedyear())) {
			carInfo.setListedYear(dataInsbCarmodelinfohis.getListedyear());
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getPrice())) {
			carInfo.setPrice(BigDecimal.valueOf(dataInsbCarmodelinfohis.getPrice()).setScale(5, RoundingMode.HALF_UP));// 不含税价
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getTaxprice())) {
			carInfo.setTaxPrice(BigDecimal.valueOf(dataInsbCarmodelinfohis.getTaxprice()).setScale(5, RoundingMode.HALF_UP));// 新车购置价
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getAnalogytaxprice())) {
			carInfo.setTaxAnalogyPrice(BigDecimal.valueOf(dataInsbCarmodelinfohis.getAnalogytaxprice()).setScale(5, RoundingMode.HALF_UP));// 含税类比价
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getAnalogyprice())) {
			carInfo.setAnalogyPrice(BigDecimal.valueOf(dataInsbCarmodelinfohis.getAnalogyprice()).setScale(5, RoundingMode.HALF_UP));// 不含税类比价
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getDisplacement()) && new BigDecimal("0").compareTo(new BigDecimal(dataInsbCarmodelinfohis.getDisplacement())) != 0) {
			carInfo.setDisplacement(BigDecimal.valueOf(dataInsbCarmodelinfohis.getDisplacement()).setScale(6));// 排气量
		} else if ("6".equals(dataInsbCarinfo.getCarproperty()) || "12".equals(dataInsbCarinfo.getCarproperty()) || "15".equals(dataInsbCarinfo.getCarproperty())
				|| "16".equals(dataInsbCarinfo.getCarproperty())) {
			carInfo.setDisplacement(BigDecimal.valueOf(Double.parseDouble("1")).setScale(6));// 排气量-非营业特种车-营业特种车-非营业货车-营业货车--齐毅需求
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getUnwrtweight())) {
			carInfo.setModelLoad(BigDecimal.valueOf(dataInsbCarmodelinfohis.getUnwrtweight()).setScale(5, RoundingMode.HALF_UP));// 载重量
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getFullweight())) {
			carInfo.setFullLoad(BigDecimal.valueOf(dataInsbCarmodelinfohis.getFullweight()).setScale(5, RoundingMode.HALF_UP));// 车身自重
		}
		// carInfo.setIsChangeKind("1".equals(dataInsbCarinfo.get));//营业转非营业
		if (!StringUtil.isEmpty(dataInsbCarinfo.getMileage())) {
			if ("0".equals(dataInsbCarinfo.getMileage())) {
				carInfo.setAvgMileage(BigDecimal.valueOf(20000).setScale(5, RoundingMode.HALF_UP));// 平均行驶里程
			} else if ("1".equals(dataInsbCarinfo.getMileage())) {
				carInfo.setAvgMileage(BigDecimal.valueOf(40000).setScale(5, RoundingMode.HALF_UP));// 平均行驶里程
			} else if ("2".equals(dataInsbCarinfo.getMileage())) {
				carInfo.setAvgMileage(BigDecimal.valueOf(60000).setScale(5, RoundingMode.HALF_UP));// 平均行驶里程
			} else {
				carInfo.setAvgMileage(BigDecimal.valueOf(50000).setScale(5, RoundingMode.HALF_UP));// 平均行驶里程
			}
		}
		if (!StringUtil.isEmpty(dataInsbCarinfo.getDrivingarea())) {
			carInfo.setDriverArea(Integer.parseInt(dataInsbCarinfo.getDrivingarea()));// 约定行驶区域
		} else {
			carInfo.setDriverArea(Integer.parseInt(InterFaceDefaultValueUtil.getDefaultValue(taskId, insurancecompanyid, "CarInfo", "drivingarea").toString()));// 约定行驶区域
		}
		if (!StringUtil.isEmpty(dataInsbCarmodelinfohis.getCarVehicleOrigin())) {
			carInfo.setCarVehicleOrigin(dataInsbCarmodelinfohis.getCarVehicleOrigin());// 产地类型
		} else {
			if (!StringUtil.isEmpty(dataInsbCarinfo.getVincode())) {
				carInfo.setCarVehicleOrigin("L".equalsIgnoreCase(dataInsbCarinfo.getVincode().substring(0, 1)) ? 0 : 1);// 产地类型
			}
		}
		carInfo.setIsLoansCar("1".equals(dataInsbCarinfo.getIsLoansCar()));// 是否贷款车
		if (!StringUtil.isEmpty(dataInsbCarinfo.getCarVehicularApplications())) {
			carInfo.setCarVehicularApplications(dataInsbCarinfo.getCarVehicularApplications());// 车辆用途
		}
		carInfo.setIsFieldCar("1".equals(dataInsbCarinfo.getIsFieldCar()));// 是否军牌或外地车
		if (!StringUtil.isEmpty(dataInsbCarinfo.getCarBodyColor())) {
			carInfo.setCarBodyColor(dataInsbCarinfo.getCarBodyColor());// 车身颜色
		}
		carInfo.setSeatCnt(dataInsbCarmodelinfohis.getSeat());// 座位数
		carInfo.setRatedPassengerCapacity(dataInsbCarmodelinfohis.getRatedPassengerCapacity());// 核定载客人数
		carInfo.setMisc(dataInsbCarinfo.getNoti());// 杂项
		carInfo.setIsLoanManyYearsFlag("1".equals(dataInsbCarinfo.getLoanManyYearsFlag()));// 是否车贷投保多年标志
		if(null!=carInfo.getSeatCnt()&&carInfo.getSeatCnt()<=9&&null!=carInfo.getDisplacement()&&carInfo.getDisplacement().floatValue()<1.0
				&&("1".equals(dataInsbCarinfo.getCarproperty()) || "2".equals(dataInsbCarinfo.getCarproperty()) || "3".equals(dataInsbCarinfo.getCarproperty())
						|| "4".equals(dataInsbCarinfo.getCarproperty()) || "5".equals(dataInsbCarinfo.getCarproperty()))){
			carInfo.setJgVehicleType(21);//微型普通客车,微型车标记---交管车俩类型
		}
		return carInfo;
	}

	private CarModelInfoBean getGzruleQueryCarmodelinfo(String vinCode, String subtaskId, String taskId, String inscomcode, INSBCarmodelinfohis carmodelinfo, String regDate,String pointPrice, String agreementid) {
		LogUtil.info("%s,getGzruleQueryCarmodelinfo筛选年款=进入的参数是=：taskid=%s ,inscomcode=%s ====参数：子流程_subtaskId=%s ,新车购置价_taxPrice=%s ,不含税价_price=%s ,含税类比价_analogyTaxPrice=%s ,不含税类比价_analogyPrice=%s ,初等日期_regDate=%s ,座位数_seats=%s ,车型名称_modelName=%s ,指定车价_pointPrice=%s,排量_displacement=%s",
				agreementid, taskId, inscomcode, subtaskId, carmodelinfo.getTaxprice(), carmodelinfo.getPrice(),
				carmodelinfo.getAnalogytaxprice(), carmodelinfo.getAnalogyprice(), regDate,
				carmodelinfo.getSeat(), carmodelinfo.getStandardfullname(), pointPrice, carmodelinfo.getDisplacement());
		CarModelInfoBean resultBean = null;
		try {
			String result = appQuotationService.getPriceIntervalByGZquery(subtaskId, taskId, inscomcode, carmodelinfo.getTaxprice(), carmodelinfo.getPrice(), carmodelinfo.getAnalogytaxprice(), carmodelinfo.getAnalogyprice(), regDate, carmodelinfo.getSeat(), agreementid);
			LogUtil.info("==筛选年款=获得判断规则=：taskid=" + taskId + ",inscomcode=" + inscomcode + "==result:" + result + "==" + carmodelinfo.getStandardfullname());
			regDate = (String) regDate.replaceAll("-", "").subSequence(0, 6);
			if (!StringUtil.isEmpty(result)) {
				JSONObject JudgeJsonObject = JSONObject.fromObject(result);
				List<CarModelInfoBean> judgeList = this.getCarModelListFromCif(carmodelinfo.getStandardfullname(), taskId, inscomcode);
				if (judgeList.size() <= 0) {
					LogUtil.info("==筛选年款=获得车型列表=：taskid=" + taskId + ",inscomcode=" + inscomcode + "=第一次=获取车型列表错误=" + carmodelinfo.getStandardfullname());
					List<CarModelInfoBean> judgeList2 = this.getCarModelListFromCif(carmodelinfo.getStandardfullname(), taskId, inscomcode);
					if (judgeList2.size() <= 0) {
						LogUtil.info("==筛选年款=获得车型列表=：taskid=" + taskId + ",inscomcode=" + inscomcode + "=第二次=获取车型列表错误=" + carmodelinfo.getStandardfullname());
						return resultBean;
					} else {
						//如果是进口车且规则规定设定需要过滤排量则过滤list   InterFaceServiceImpl#getCarVehicleOriginByVin）
						if(1 == getCarVehicleOriginByVin(vinCode) && JudgeJsonObject.getBoolean("ruleItem.restrictImportVehicleDisplacement")){
							judgeList2 = judgeList2.stream().filter(model -> model.getDisplacement().equals(String.valueOf(carmodelinfo.getDisplacement()))).collect(Collectors.toList());
						}
						LogUtil.info("size=%s=筛选年款=获得车型列表=：taskid=%s,inscomcode=%s=车型名称_modelName=%s=第二次获取车型列表成功",judgeList2.size(), taskId, inscomcode, carmodelinfo.getStandardfullname());
						resultBean = this.getFianllyCarModel(JudgeJsonObject, judgeList2, taskId, inscomcode, pointPrice, regDate,String.valueOf(carmodelinfo.getSeat()));
						if (StringUtil.isEmpty(resultBean)) {
							LogUtil.info("==筛选年款=命中的车型是=：taskid=" + taskId + ",inscomcode=" + inscomcode + "" + "=为空，没有命中");
						} else {
							LogUtil.info("==筛选年款=处理完的车型列表=：taskid=" + taskId + ",inscomcode=" + inscomcode + "" + "=车型名称=" + resultBean.getVehiclename() + ",含税价=" + resultBean.getTaxprice()
									+ ",不含税价=" + resultBean.getPrice() + ",含税类比价=" + resultBean.getAnalogytaxprice() + ",不含税类比价=" + resultBean.getAnalogyprice() + ",初等日期=" + resultBean.getMaketdate());
						}
					}
				} else {
					//如果是进口车且规则规定设定需要过滤排量则过滤list   InterFaceServiceImpl#getCarVehicleOriginByVin）
					if(1 == getCarVehicleOriginByVin(vinCode) && JudgeJsonObject.getBoolean("ruleItem.restrictImportVehicleDisplacement")){
						judgeList = judgeList.stream().filter(model -> model.getDisplacement().equals(String.valueOf(carmodelinfo.getDisplacement()))).collect(Collectors.toList());
					}
					LogUtil.info("size=%s=筛选年款=获得车型列表=：taskid=%s,inscomcode=%s=车型名称_modelName=%s=第一次获取车型列表成功",judgeList.size(), taskId, inscomcode, carmodelinfo.getStandardfullname());
					resultBean = this.getFianllyCarModel(JudgeJsonObject, judgeList, taskId, inscomcode, pointPrice, regDate,String.valueOf(carmodelinfo.getSeat()));
					if (StringUtil.isEmpty(resultBean)) {
						LogUtil.info("==筛选年款=命中的车型是=：taskid=" + taskId + ",inscomcode=" + inscomcode + "" + "=为空，没有命中");
					} else {
						LogUtil.info("==筛选年款=处理完的车型列表=：taskid=" + taskId + ",inscomcode=" + inscomcode + "" + "=车型名称=" + resultBean.getVehiclename() + ",含税价=" + resultBean.getTaxprice() + ",不含税价="
								+ resultBean.getPrice() + ",含税类比价=" + resultBean.getAnalogytaxprice() + ",不含税类比价=" + resultBean.getAnalogyprice() + ",初等日期=" + resultBean.getMaketdate());
					}
				}
			} else {
				LogUtil.info("==筛选年款=获得判断规则=：taskid=" + taskId + ",inscomcode=" + inscomcode + "==result:" + result + "=错误，规则维度没取到信息=" + carmodelinfo.getStandardfullname());
			}
		} catch (Exception e) {
			LogUtil.info("==筛选年款=主程序=：taskid=" + taskId + ",inscomcode=" + inscomcode + "==出错了");
			e.printStackTrace();
			return resultBean;
		}

		if (resultBean != null) {
			String cifCode = "";
			if (!StringUtil.isEmpty(resultBean.getVehicleId())) {
				cifCode = this.getRbCode(resultBean.getVehicleId(), inscomcode, taskId);
			}
			resultBean.setJycode(cifCode);
			resultBean.setRbcode(cifCode);
		}

		return resultBean;
	}

	/**
	 * 根据任务类型获取流程code
	 * @param taskType
	 * @return
	 */
	private String getFlowCodeByTaskType(String taskType){
		switch (taskType) {
			case "quote":
				return FlowInfo.quoteapply.getCode();
			case "quotequery":
				return FlowInfo.quotequery.getCode();
			case "insure":
				return FlowInfo.underwritingapply.getCode();
			case "pay":
				return FlowInfo.payapply.getCode();
			case "approved":
				return FlowInfo.insquery.getCode();
			case "insurequery":
				return FlowInfo.underwritingquery.getCode();
			case "approvedquery":
				return FlowInfo.insquery.getCode();
			case "autoinsure":
				return FlowInfo.autoinsuring.getCode();
		}
		return null;
	}

	/**
	 * 根据任务类型获取流程名称
	 * @param taskType
	 * @return
	 */
	private String getFlowNameByTaskType(String taskType){
		switch (taskType) {
			case "quote":
				return FlowInfo.quoteapply.getDesc();
			case "quotequery":
				return FlowInfo.quotequery.getDesc();
			case "insure":
				return FlowInfo.underwritingapply.getDesc();
			case "pay":
				return FlowInfo.payapply.getDesc();
			case "approved":
				return FlowInfo.insquery.getDesc();
			case "insurequery":
				return FlowInfo.underwritingquery.getDesc();
			case "approvedquery":
				return FlowInfo.insquery.getDesc();
			case "autoinsure":
				return FlowInfo.autoinsuring.getDesc();
		}
		return null;
	}

	/**
	 * 根据任务类型获取能力链接的类型
	 * @param taskType
	 * @return
	 */
	private String getConfTypeByTaskType(String taskType){
		switch (taskType) {
			case "quote":
				return "01";
			case "quotequery":
				return "02";
			case "insure":
			case "autoinsure":
				return "02";
			case "insurequery":
				return "02";
			case "pay":
				return "02";
			case "approved":
				return "04";
			case "approvedquery":
				return "04";
			default:
				return "01";
		}
	}

	/**
	 * 平安二维码支付、数据回写,保存大对象
	 */
	@Override
	public Map<String, String> saveQRCodePacket(String json){
		JSONObject dataPacket = JSONObject.fromObject(json);
		Map<String, String> resultMap = new HashMap<String, String>();
		if (StringUtil.isEmpty(dataPacket.get("taskStatus")) || "null".equalsIgnoreCase(dataPacket.get("taskStatus").toString())) {
			LogUtil.info("进入====数据回写接口：错误,key：taskStatus,不存在====");
			resultMap.put("result", "error");
			resultMap.put("msg", "taskStatus不存在");
			return resultMap;
		}
		if (StringUtil.isEmpty(dataPacket.get("businessId")) || "null".equalsIgnoreCase(dataPacket.get("businessId").toString())) {
			LogUtil.info("进入====数据回写接口：错误,key：businessId,不存在====");
			resultMap.put("result", "error");
			resultMap.put("msg", "businessId不存在");
			return resultMap;
		}
		String taskId = dataPacket.get("businessId").toString().substring(0,dataPacket.get("businessId").toString().indexOf("@"));
		redisClient.set(Constants.CM_GLOBAL, taskId + "_approvedQueryStatus", dataPacket.get("taskStatus").toString().trim(), expireTime);
		return savePacket(json);
	}

	/**
	 * 支付平台调用精灵核保查询任务接口
	 *
	 */
	@Override
	public String goToFairyQuote(String processinstanceid, String incoid, String touserid, String taskType, String callBackUrl, String orderPaymentId, String sid) throws Exception {
		taskType += StringUtil.isEmpty(callBackUrl)?"":"#"+callBackUrl;		//insurequery@qrcode#http***
		touserid += StringUtil.isEmpty(orderPaymentId)?"":"#"+orderPaymentId;
		incoid += StringUtil.isEmpty(sid)?"":"#"+sid;
		LogUtil.info("====二维码核保查询拼接后的taskType = "+taskType+"、touserid = "+touserid+"、sid="+sid);
		return goToFairyQuote(processinstanceid, incoid, touserid, taskType);
	}
}