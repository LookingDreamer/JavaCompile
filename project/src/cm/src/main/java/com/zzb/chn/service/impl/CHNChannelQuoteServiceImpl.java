package com.zzb.chn.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.jobpool.Pool;
import com.cninsure.jobpool.Task;
import com.cninsure.jobpool.dispatch.DispatchTaskService;
import com.cninsure.system.service.INSCDeptService;
import com.common.CloudQueryUtil;
import com.common.ConfigUtil;
import com.common.GenerateSequenceUtil;
import com.common.ModelUtil;
import com.common.WorkFlowUtil;
import com.common.redis.IRedisClient;
import com.zzb.app.model.bean.RisksData;
import com.zzb.app.model.bean.SelectOption;
import com.zzb.app.service.AppQuotationService;
import com.zzb.chn.bean.BaseInsureInfoBean;
import com.zzb.chn.bean.BizInsureInfoBean;
import com.zzb.chn.bean.CarInfoBean;
import com.zzb.chn.bean.CarModelInfoBean;
import com.zzb.chn.bean.DeliveryBean;
import com.zzb.chn.bean.InsureInfoBean;
import com.zzb.chn.bean.InsureSupplyBean;
import com.zzb.chn.bean.InvoiceInfoBean;
import com.zzb.chn.bean.PersonBean;
import com.zzb.chn.bean.ProviderBean;
import com.zzb.chn.bean.QuoteBean;
import com.zzb.chn.bean.RiskKindBean;
import com.zzb.chn.dao.INSBChncarqryDao;
import com.zzb.chn.dao.INSBChncarqrycountDao;
import com.zzb.chn.entity.INSBChncarqry;
import com.zzb.chn.entity.INSBChncarqrycount;
import com.zzb.chn.service.CHNChannelQuoteService;
import com.zzb.chn.service.CHNChannelService;
import com.zzb.chn.service.CHNPaymentService;
import com.zzb.chn.util.JsonUtils;
import com.zzb.chn.util.ParaValidatorUtils;
import com.zzb.chn.util.StatusCodeMapperUtil;
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
import com.zzb.cm.dao.INSBInsuredDao;
import com.zzb.cm.dao.INSBInsuredhisDao;
import com.zzb.cm.dao.INSBInvoiceinfoDao;
import com.zzb.cm.dao.INSBLegalrightclaimDao;
import com.zzb.cm.dao.INSBLegalrightclaimhisDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.dao.INSBRelationpersonDao;
import com.zzb.cm.dao.INSBRelationpersonhisDao;
import com.zzb.cm.dao.INSHCarkindpriceDao;
import com.zzb.cm.entity.INSBApplicant;
import com.zzb.cm.entity.INSBApplicanthis;
import com.zzb.cm.entity.INSBCarconfig;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.entity.INSBCarmodelinfohis;
import com.zzb.cm.entity.INSBCarowneinfo;
import com.zzb.cm.entity.INSBDeliveryaddress;
import com.zzb.cm.entity.INSBInsured;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBInvoiceinfo;
import com.zzb.cm.entity.INSBLegalrightclaim;
import com.zzb.cm.entity.INSBLegalrightclaimhis;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.entity.INSBRelationperson;
import com.zzb.cm.entity.INSBRelationpersonhis;
import com.zzb.cm.entity.INSHCarkindprice;
import com.zzb.cm.service.INSBInsuresupplyparamService;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBAgreementdeptDao;
import com.zzb.conf.dao.INSBAgreementinterfaceDao;
import com.zzb.conf.dao.INSBHolidayareaDao;
import com.zzb.conf.dao.INSBOrderdeliveryDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.INSBRegionDao;
import com.zzb.conf.dao.INSBRiskkindconfigDao;
import com.zzb.conf.dao.INSBUsercommentDao;
import com.zzb.conf.dao.INSBWorkflowmainDao;
import com.zzb.conf.dao.INSBWorkflowmaintrackDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.dao.INSBWorkflowsubtrackDao;
import com.zzb.conf.dao.INSBWorktimeDao;
import com.zzb.conf.dao.INSBWorktimeareaDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBAgreementdept;
import com.zzb.conf.entity.INSBHolidayarea;
import com.zzb.conf.entity.INSBOrderdelivery;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBRegion;
import com.zzb.conf.entity.INSBRiskkindconfig;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowmaintrack;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import com.zzb.conf.entity.INSBWorktime;
import com.zzb.conf.entity.INSBWorktimearea;
import com.zzb.conf.service.INSBChannelagreementService;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.extra.entity.INSBAgentTask;
import com.zzb.extra.service.INSBAgentTaskService;
import com.zzb.mobile.dao.AppInsuredQuoteDao;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.InsuredOneConfigModel;
import com.zzb.mobile.model.WorkflowStartQuoteModel;
import com.zzb.mobile.service.AppInsuredQuoteService;
import com.zzb.mobile.service.AppMyOrderInfoService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class CHNChannelQuoteServiceImpl extends BaseServiceImpl<INSBWorkflowmain>
        implements CHNChannelQuoteService {
    @Resource
    private INSBPolicyitemDao insbPolicyitemDao;
    @Resource
    private INSBCarinfohisDao insbCarinfohisDao;
    @Resource
    private INSBCarinfoDao insbCarinfoDao;
    @Resource
    private INSBCarmodelinfohisDao insbCarmodelinfohisDao;
    @Resource
    private INSBInsuredhisDao insbInsuredhisDao;
    @Resource
    private INSBApplicanthisDao insbApplicanthisDao;
    @Resource
    private INSBLegalrightclaimhisDao insbLegalrightclaimhisDao;
    @Resource
    private INSBCarkindpriceDao insbCarkindpriceDao;
    @Resource
    private INSBOrderDao insbOrderDao;
    @Resource
    private INSBPersonDao insbPersonDao;
    @Resource
    private INSBCarowneinfoDao insbCarowneinfoDao;
    @Resource
    private INSBWorkflowsubDao insbWorkflowsubDao;
    @Resource
    private INSBCarmodelinfoDao insbCarmodelinfoDao;
    @Resource
    private INSBApplicantDao insbApplicantDao;
    @Resource
    private INSBInsuredDao insbInsuredDao;
    @Resource
    private INSBLegalrightclaimDao insbLegalrightclaimDao;
    @Resource
    private INSBOrderdeliveryDao insbOrderdeliveryDao;
    @Resource
    private INSBDeliveryaddressDao insbDeliveryaddressDao;
    @Resource
    private INSBQuoteinfoDao insbQuoteinfoDao;
    @Resource
    private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
    @Resource
    private INSBRiskkindconfigDao insbRiskkindconfigDao;
    @Resource
    private INSBChannelagreementService insbChannelagreementService;
    @Resource
    private INSBWorkflowmainDao insbWorkflowmainDao;
    @Resource
    private INSBRegionDao insbRegionDao;
    @Resource
    private AppInsuredQuoteService appInsuredQuoteService;
    @Resource
    private AppQuotationService appQuotationService;
    @Resource
    private AppMyOrderInfoService appMyOrderInfoService;
    @Resource
    private AppInsuredQuoteDao appInsuredQuoteDao;
    @Resource
    private INSBChncarqryDao insbChncarqryDao;
    @Resource
    private INSBProviderDao providerDao;
    @Resource
    private INSBChncarqrycountDao insbChncarqrycountDao;
    @Resource
    private INSBWorkflowmaintrackDao insbWorkflowmaintrackDao;
    @Resource
    private INSBUsercommentDao insbUsercommentDao;
    @Resource
    private INSBWorkflowsubtrackDao insbWorkflowsubtrackDao;
    @Resource
    private INSBRelationpersonDao insbRelationpersonDao;
    @Resource
    private INSBRelationpersonhisDao insbRelationpersonhisDao;
    @Resource
    private INSHCarkindpriceDao inshCarkindpriceDao;
    @Resource
    private INSBAgentDao insbAgentDao;
    @Resource
    private INSCDeptService inscDeptService;
    @Resource
    private INSBWorktimeDao insbWorktimeDao;
    @Resource
    private INSBWorktimeareaDao insbWorktimeareaDao;
    @Resource
    private INSBAgreementinterfaceDao insbAgreementinterfaceDao;
    @Resource
    private IRedisClient redisClient;
    @Resource
    public Scheduler sched;
    @Resource
    private INSBAgreementdeptDao insbAgreementdeptDao;
    @Autowired
    private CHNChannelService chnService;
    @Resource
	private INSBHolidayareaDao insbHolidayareaDao;
    @Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
    @Resource
    private INSBAgentTaskService insbAgentTaskService;
    @Resource
	private INSBWorkflowsubService workflowsubService;
	@Resource
	private DispatchTaskService dispatchService;
	@Resource
	private INSBCarconfigDao insbCarconfigDao;
	@Resource
    private AppInsuredQuoteService insuredQuoteService;
	@Resource
	private INSBInvoiceinfoDao insbInvoiceinfoDao;	
	@Resource
	private INSBInsuresupplyparamService insbInsuresupplyparamService;
    @Resource
    private INSCDeptDao inscDeptDao;
	
//	身份证代号
	public static final String IDENTITY_IDCARD_CODE = "0";
//	户口本代号
	public static final String HOUSE_HOLDREGISTER_CODE = "1";
//	驾照代号
	public static final String DRIVING_LICENSE_CODE = "2";
//	军官证、士兵证
	public static final String SOLDIER_CARD_CODE = "3";
//	护照代码
	public static final String PASS_PORT_CODE = "4";
//	港澳回乡证
	public static final String HONGKONG_MOK_CODE = "5";
//	组织代码代号
	public static final String ORG_CODECERT_CODE = "6";
	
	//单供应商修改保险配置重新提交报价
	public QuoteBean submitSingleQuote(QuoteBean quoteBeanIn) throws Exception {
		QuoteBean res = new QuoteBean();
        InsuredOneConfigModel model = new InsuredOneConfigModel();
        model.setProcessinstanceid(quoteBeanIn.getTaskId());
        model.setPid(quoteBeanIn.getPrvId());

        CommonModel resModel = insuredQuoteService.checkRestartConf(model);
        if ("fail".equals(resModel.getStatus())) {
        	res.setRespCode("01");
        } else {
        	res.setRespCode("00");
        }
        res.setErrorMsg(resModel.getMessage());

        return res;
	}

	//提交报价
    @Override
    public QuoteBean submitQuote(QuoteBean quoteBean) throws Exception {
        String taskId = quoteBean.getTaskId();
        QuoteBean result = new QuoteBean();

        if (StringUtil.isEmpty(taskId)) {
            result.setRespCode("01");
            result.setErrorMsg("任务号不能为空");
            return result;
        }
        INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
        insbQuotetotalinfo.setTaskid(taskId);
        insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
        if (insbQuotetotalinfo == null) {
            result.setRespCode("01");
            result.setErrorMsg("请提供正确的任务号");
            return result;
        }
        if (!quoteBean.getChannelId().equals(insbQuotetotalinfo.getPurchaserChannel())) {
            result.setRespCode("01");
            result.setErrorMsg("请提供正确的渠道id");
            return result;
        }
        if (!hasInterfacePower("4", insbQuotetotalinfo.getInscitycode(), quoteBean.getChannelId())) { //4-提交投保请求
            result.setRespCode("01");
            result.setErrorMsg("没有调用该接口的权限!");
            return result;
        }
        
        if ( StringUtil.isNotEmpty(quoteBean.getPrvId()) ) { //单供应商修改保险配置重新提交报价
             /* 记录chn推cm的时间点开始*/
            try {
                Map<String, String> phaseTime = new HashMap<>();
                phaseTime.put("channelId", quoteBean.getChannelId());
                phaseTime.put("taskId", quoteBean.getTaskId());
                phaseTime.put("prvId", quoteBean.getPrvId()==null?"":quoteBean.getPrvId());
                phaseTime.put("taskState", quoteBean.getTaskState()==null?"":quoteBean.getTaskState());
                phaseTime.put("phaseType", "submitQuoteChnCallCM");
                phaseTime.put("phaseSeq", "2");
                chnService.recordPhaseTime(phaseTime);
            }catch (Exception e){
                LogUtil.info("记录chn推cm的时间点异常:"+quoteBean.getTaskId()+e.getMessage());
            }
             /* 记录chn推cm的时间点结束*/
        	return submitSingleQuote(quoteBean);
        }
        INSBPerson person = insbPersonDao.selectCarOwnerPersonByTaskId(taskId);
        if(person != null) {
        	if(StringUtil.isEmpty(person.getIdcardno()) || StringUtil.isEmpty(person.getIdcardtype())) {
        		result.setRespCode("01");
        		result.setErrorMsg("请传车主证件类型和证件号码!");
        		return result;
        	}
        }
        
        Map<String, String> resMap = new HashMap<String, String>();
        try {
            List<String> incoids = new ArrayList<String>();
            boolean bool = false;
            List<Map<String, String>> quoteInfoList = insbQuoteinfoDao.selectWorkflowinstanceid(taskId);
            if (quoteInfoList != null && quoteInfoList.size() > 0) {
                for (Map<String, String> map : quoteInfoList) {
                    String workflowinstanceid = map.get("workflowinstanceid");
                    if (StringUtils.isBlank(workflowinstanceid)) {
                        bool = false;
                        break;
                    } else {
                        bool = true;
                    }
                }
            }
            if (bool) {
                result.setRespCode("01");
                result.setErrorMsg("已提交报价成功，不允许多次提交");
                return result;
            }
            List<Map<String, String>> quoteinfoList = insbQuoteinfoDao.selectByTaskid(taskId);
            if (quoteinfoList != null && quoteinfoList.size() > 0) {
                for (Map<String, String> inscomcodeMap : quoteinfoList) {
                    String inscomcode = inscomcodeMap.get("inscomcode");
                    //获得报价前规则校验信息
                    String resultQuote = appQuotationService.getQuotationValidatedInfo("", taskId, inscomcode);
                    if (StringUtil.isNotEmpty(resultQuote)) {
                        JSONObject jsonObject = JSONObject.fromObject(resultQuote);
                        LogUtil.info("taskid= " + taskId + "供应商ID:" + inscomcode + "规则调用结果：" + resultQuote);
                        // 0:提示 1:转人工 2:修改阻断 3:阻断
                        if (!jsonObject.getBoolean("success")) {// 规则校验没通过
                            if ( null != jsonObject.get("quotationMode") && "3".equals(jsonObject.get("quotationMode").toString()) ) {
                                incoids.add(inscomcode);
                                String comName = "";
                                INSBProvider insbprovider = providerDao.selectById(inscomcode.substring(0, 4));
                                if (insbprovider != null) {
                                    comName = insbprovider.getPrvshotname();
                                }

                                resMap.put(inscomcode, comName + jsonObject.getString("resultMsg").replace("\"", ""));
                            }
                        }
                    }
                }
            }
            if (resMap.size() > 0) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.putAll(resMap);
                result.setRespCode("01");
                result.setErrorMsg(JsonUtils.serialize(resMap));
                return result;
            }

            LogUtil.info("准备推工作流：" + taskId);
           /* 记录chn推cm的时间点开始*/
            try {
                Map<String, String> phaseTime = new HashMap<>();
                phaseTime.put("channelId", quoteBean.getChannelId());
                phaseTime.put("taskId", taskId);
                phaseTime.put("prvId", quoteBean.getPrvId()==null?"":quoteBean.getPrvId());
                phaseTime.put("taskState", quoteBean.getTaskState()==null?"":quoteBean.getTaskState());
                phaseTime.put("phaseType", "submitQuoteChnCallCM");
                phaseTime.put("phaseSeq", "2");
                chnService.recordPhaseTime(phaseTime);
            }catch (Exception e){
                LogUtil.info("记录chn推cm的时间点异常:"+taskId+e.getMessage());
            }
             /* 记录chn推cm的时间点结束*/
            WorkflowStartQuoteModel model = new WorkflowStartQuoteModel();
            model.setProcessinstanceid(taskId);
            model.setFlag("0");
            model.setPids(incoids); // 不提交报价的保险公司
            CommonModel commonModel = appInsuredQuoteService.workflowStartQuote(model);
//            String message = WorkFlowUtil.noticeWorkflowStartQuote("admin",taskId,incoids,"0");
            if (commonModel != null && "success".equals(commonModel.getStatus())) {
                result.setRespCode("00");
               /* if (resMap.size() > 0) {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.putAll(resMap);
                    result.setErrorMsg(JsonUtils.serialize(resMap));
                }*/
            } else {
                result.setRespCode("01");
                result.setErrorMsg("提交报价失败");
                LogUtil.info("提交报价失败：" + commonModel.getMessage());
                return result;
            }
        } catch (Exception e) {
            LogUtil.error("提交报价时出现异常，任务ID：" + taskId, e);
            result.setRespCode("01");
            result.setErrorMsg("提交报价异常");
            return result;
        }
        result.setRespCode("00");
        result.setErrorMsg("提交报价成功");
        return result;
    }

    //出单网点是否上班
    public boolean isWorkTime(String taskId, String prvId) {
        Calendar cal = Calendar.getInstance();

        Map<String, String> param = new HashMap<String, String>();
        param.put("taskid", taskId);
        param.put("providerid", prvId);

        List<INSBAgreementdept> insbAgreementdepts = insbAgreementdeptDao.queryByTaskIdAndPrvId(param);
        List<String> listDept = new ArrayList<String>();
        if (insbAgreementdepts != null && insbAgreementdepts.size() > 0) {
            INSBAgreementdept insbAgreementdept = insbAgreementdepts.get(0);
            String deptId5 = insbAgreementdept.getDeptid5(); //网点
            String deptId4 = insbAgreementdept.getDeptid4(); //分公司
            String deptId3 = insbAgreementdept.getDeptid3(); //法人公司
            String deptId2 = insbAgreementdept.getDeptid2(); //平台公司
            //String deptId1 = insbAgreementdept.getDeptid1(); //集团

            if (StringUtil.isNotEmpty(deptId5)) listDept.add(deptId5);
            if (StringUtil.isNotEmpty(deptId4)) listDept.add(deptId4);
            if (StringUtil.isNotEmpty(deptId3)) listDept.add(deptId3);
            if (StringUtil.isNotEmpty(deptId2)) listDept.add(deptId2);
            //if (StringUtil.isNotEmpty(deptId1)) listDept.add(deptId1);
        }

        for (String deptId : listDept) {
            INSBWorktime worktime = insbWorktimeDao.selectOneBydeptId(deptId);
            if (worktime == null) continue;
            String insbworktimeid = worktime.getId();
            if (insbworktimeid == null) continue;

            param = new HashMap<String, String>();
            param.put("insbworktimeid", insbworktimeid);
            param.put("isremindtime", "0");
            List<INSBWorktimearea> worktimeAreaList = insbWorktimeareaDao.selectByWorktimeId(param); //工作时间list
            if (null == worktimeAreaList || worktimeAreaList.size() <= 0) continue;
            
            param = new HashMap<String, String>();
            param.put("insbworktimeid", insbworktimeid);
            param.put("isonduty", "0");
			List<INSBHolidayarea> holidaylist = insbHolidayareaDao.selectByWorktimeId(param); // 节假日list
			
			param = new HashMap<String, String>();
			param.put("insbworktimeid", insbworktimeid);
			param.put("isonduty", "1");
			List<INSBHolidayarea> holidayworklist = insbHolidayareaDao.selectByWorktimeId(param); // 节假日值班list

            Map<String, String> mapOut = new HashMap<String, String>();
            return judgeWorkTime(cal, worktimeAreaList, holidaylist, holidayworklist, mapOut, true);
        }
        
        return false;    
    }
    
    //取得上班有效时间，包括支付有效期和报价有效期
    public String getWorkVaildTime(String taskId, String prvId, Date date, boolean isQuote) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
        insbQuotetotalinfo.setTaskid(taskId);
        insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
        boolean isNewFlow = false;
        if ( insbQuotetotalinfo != null && "FirstPayLastInsure".equals(insbQuotetotalinfo.getSourceFrom()) ) {
        	isNewFlow = true;
        }
        
        if (!isNewFlow && isQuote) { //原流程报价有效时间不匹配工作时间
        	return ModelUtil.conbertToString(cal.getTime()) + " 23:59:59";
        }

        Map<String, String> param = new HashMap<String, String>();
        param.put("taskid", taskId);
        param.put("providerid", prvId);

        List<INSBAgreementdept> insbAgreementdepts = insbAgreementdeptDao.queryByTaskIdAndPrvId(param);
        List<String> listDept = new ArrayList<String>();
        if (insbAgreementdepts != null && insbAgreementdepts.size() > 0) {
            INSBAgreementdept insbAgreementdept = insbAgreementdepts.get(0);
            String deptId5 = insbAgreementdept.getDeptid5(); //网点
            String deptId4 = insbAgreementdept.getDeptid4(); //分公司
            String deptId3 = insbAgreementdept.getDeptid3(); //法人公司
            String deptId2 = insbAgreementdept.getDeptid2(); //平台公司
            //String deptId1 = insbAgreementdept.getDeptid1(); //集团

            if (StringUtil.isNotEmpty(deptId5)) listDept.add(deptId5);
            if (StringUtil.isNotEmpty(deptId4)) listDept.add(deptId4);
            if (StringUtil.isNotEmpty(deptId3)) listDept.add(deptId3);
            if (StringUtil.isNotEmpty(deptId2)) listDept.add(deptId2);
            //if (StringUtil.isNotEmpty(deptId1)) listDept.add(deptId1);
        }

        for (String deptId : listDept) {
            INSBWorktime worktime = insbWorktimeDao.selectOneBydeptId(deptId);
            if (worktime == null) continue;
            String insbworktimeid = worktime.getId();
            if (insbworktimeid == null) continue;

            param = new HashMap<String, String>();
            param.put("insbworktimeid", insbworktimeid);
            param.put("isremindtime", "0");
            List<INSBWorktimearea> worktimeAreaList = insbWorktimeareaDao.selectByWorktimeId(param); //工作时间list
            if (null == worktimeAreaList || worktimeAreaList.size() <= 0) continue;
            
            param = new HashMap<String, String>();
            param.put("insbworktimeid", insbworktimeid);
            param.put("isonduty", "0");
			List<INSBHolidayarea> holidaylist = insbHolidayareaDao.selectByWorktimeId(param); // 节假日list
			
			param = new HashMap<String, String>();
			param.put("insbworktimeid", insbworktimeid);
			param.put("isonduty", "1");
			List<INSBHolidayarea> holidayworklist = insbHolidayareaDao.selectByWorktimeId(param); // 节假日值班list

            String workEndHm = null;
            boolean isInWorkTime = false;
            int dayCount = 1;
            while (!isInWorkTime && dayCount <= 15) { //一个星期 + 国家节假日(最多7天)
            	Map<String, String> mapOut = new HashMap<String, String>();
            	isInWorkTime = judgeWorkTime(cal, worktimeAreaList, holidaylist, holidayworklist, mapOut, false);
            	
            	if (isInWorkTime) {
            		workEndHm = mapOut.get("hm");
            		break;
            	}
	            
	            cal.add(Calendar.DATE, -1);
	            dayCount++;
            }
            
            if (StringUtil.isEmpty(workEndHm)) {
            	workEndHm = "18:00:00";
            } else {
            	workEndHm += ":00";
            }
            if (!isNewFlow && isQuote) workEndHm = "23:59:59";
            
            return ModelUtil.conbertToString(cal.getTime()) + " " + workEndHm;
        }

        return null;
    }
    
    //报价/支付有效期匹配上班时间
    private boolean judgeWorkTime(Calendar cal, List<INSBWorktimearea> worktimeAreaList, 
    		List<INSBHolidayarea> holidaylist, List<INSBHolidayarea> holidayworklist, Map<String, String> mapOut, boolean judgeHm) { 
    	Date date = cal.getTime();
    	//得到星期
    	int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week <= 0) week = 7;
        //得到时分
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        int hm = Integer.parseInt(sdf.format(date));
        //得到日期
        sdf = new SimpleDateFormat("yyyyMMdd");
        int day = Integer.parseInt(sdf.format(date));
    	
    	for (INSBHolidayarea holidayWork : holidayworklist) { //节日值班
    		String datestart = holidayWork.getDatestart();
    		String dateend = holidayWork.getDateend();
    		String daytimestart = holidayWork.getDaytimestart();
    		String daytimeend = holidayWork.getDaytimeend();
    		
    		if (StringUtil.isEmpty(datestart) || StringUtil.isEmpty(dateend) ||
    				StringUtil.isEmpty(daytimestart) || StringUtil.isEmpty(daytimeend)) {
    			continue;
    		}
    		
    		int holidayWorkStartDay = Integer.parseInt(datestart.replaceAll("-", ""));
        	int holidayWorkEndDay = Integer.parseInt(dateend.replaceAll("-", ""));
        	int holidayWorkStartHm = Integer.parseInt(daytimestart.replaceAll(":", ""));
        	int holidayWorkEndHm = Integer.parseInt(daytimeend.replaceAll(":", ""));
        	
        	if (day >= holidayWorkStartDay && day <= holidayWorkEndDay) {
        		if (judgeHm) {  
        			if (hm >= holidayWorkStartHm && hm <= holidayWorkEndHm) return true;
        		} else {
        			mapOut.put("hm", holidayWork.getDaytimeend());
        			return true;
        		}
        	}
    	}
    	
    	for (INSBHolidayarea holiday : holidaylist) { //节日
    		String datestart = holiday.getDatestart();
    		String dateend = holiday.getDateend();
    		
    		if (StringUtil.isEmpty(datestart) || StringUtil.isEmpty(dateend)) {
    			continue;
    		}
    		
    		int holidayStartDay = Integer.parseInt(datestart.replaceAll("-", ""));
        	int holidayEndDay = Integer.parseInt(dateend.replaceAll("-", ""));
        	//int holidayStartHm = Integer.parseInt(holiday.getDaytimestart().replaceAll(":", ""));
        	//int holidayEndHm = Integer.parseInt(holiday.getDaytimeend().replaceAll(":", ""));
        	
        	if (day >= holidayStartDay && day <= holidayEndDay) {
        		return false;
        	}
    	}
        
        for (INSBWorktimearea workTime : worktimeAreaList) {
        	String workdaystart = workTime.getWorkdaystart();
    		String workdayend = workTime.getWorkdayend();
    		String daytimestart = workTime.getDaytimestart();
    		String daytimeend = workTime.getDaytimeend();
    		
    		if (StringUtil.isEmpty(workdaystart) || StringUtil.isEmpty(workdayend) ||
    				StringUtil.isEmpty(daytimestart) || StringUtil.isEmpty(daytimeend)) {
    			continue;
    		}
    		
            int workStartWeek = Integer.parseInt(workdaystart);
            int workEndWeek = Integer.parseInt(workdayend);
            int workStartHm = Integer.parseInt(daytimestart.replaceAll(":", ""));
            int workEndHm = Integer.parseInt(daytimeend.replaceAll(":", ""));

            if (week >= workStartWeek && week <= workEndWeek) {
            	if (judgeHm) {  
        			if (hm >= workStartHm && hm <= workEndHm) return true;
        		} else {
        			mapOut.put("hm", workTime.getDaytimeend());
        			return true;
        		}
            }
        }
    	
        return false;
    }

    //提交核保
    @Override
    public QuoteBean submitInsure(QuoteBean quoteBean) throws Exception {
        QuoteBean result = new QuoteBean();
        if (StringUtil.isEmpty(quoteBean.getTaskId()) || StringUtil.isEmpty(quoteBean.getPrvId())) {
            result.setRespCode("01");
            result.setErrorMsg(" 任务号或供应商不能为空");
        }
        String taskId = quoteBean.getTaskId();
        String prvId = quoteBean.getPrvId();

        INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
        insbQuotetotalinfo.setTaskid(taskId);
        insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
        if (null == insbQuotetotalinfo) {
            result.setRespCode("01");
            result.setErrorMsg(taskId + " 任务号不存在");
            return result;
        }
        if (!quoteBean.getChannelId().equals(insbQuotetotalinfo.getPurchaserChannel())) {
            result.setRespCode("01");
            result.setErrorMsg("请提供正确的任务号");
            return result;
        }
        if (!hasInterfacePower("5", insbQuotetotalinfo.getInscitycode(), quoteBean.getChannelId())) { //5-提交核保请求
            result.setRespCode("01");
            result.setErrorMsg("没有调用该接口的权限!");
            return result;
        }
        
        INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
        insbQuoteinfo.setQuotetotalinfoid(insbQuotetotalinfo.getId());
        insbQuoteinfo.setInscomcode(prvId);
        insbQuoteinfo = insbQuoteinfoDao.selectOne(insbQuoteinfo);

        Date qutoValidTime = chnService.getQuoteValidDate(taskId, prvId, insbQuoteinfo.getWorkflowinstanceid(), false, true);
        if (qutoValidTime != null && (new Date().getTime() > qutoValidTime.getTime())) {
        	result.setRespCode("01");
            result.setErrorMsg("您好，你的订单已超过报价有效期，请重新提交报价，感谢您的支持！");
            return result;
    	}
        
        try {
            String taskcode = "";
            List<Map<String, String>> comcodeList = insbQuoteinfoDao.selectByTaskid(taskId);
            if (comcodeList != null && comcodeList.size() > 0) {
                for (Map<String, String> comcodeMap : comcodeList) {
                    String comcode = comcodeMap.get("inscomcode");
                    Map<String, String> mapTemp = insbWorkflowsubDao.getCurrentTaskcodeOfSubFlow(taskId, comcode);
                    if (mapTemp != null && mapTemp.size() > 0) {
                        taskcode = mapTemp.get("taskcode");
                    }
                    if (StatusCodeMapperUtil.insureState.contains(StatusCodeMapperUtil.states.get(taskcode))) {
                        if (!("19".equals(taskcode) && comcode.equals(prvId))) {
                            result.setRespCode("01");
                            result.setErrorMsg("仅支持提交一家公司的核保");
                            return result;
                        }
                    }
                }

            } else {
                result.setRespCode("01");
                result.setErrorMsg("请提供正确的任务号和供应商id");
                return result;
            }

            Map<String, String> map = insbWorkflowsubDao.getCurrentTaskcodeOfSubFlow(taskId, prvId);

            if (map != null && map.size() > 0) {
                taskcode = map.get("taskcode");
            }
            //boolean needUploadImg = chnService.getImageInfos(quoteBean, taskcode);
            //result.setMsgType(quoteBean.getMsgType());
            //result.setImageInfos(quoteBean.getImageInfos());
            
            if ("19".equals(taskcode)) {
                // 核保退回修改
                result = updateInsure(taskId, prvId);
                //result.setMsgType(quoteBean.getMsgType());
                //result.setImageInfos(quoteBean.getImageInfos());
                return result;
            }
            if (!"14".equals(taskcode)) {
                result.setRespCode("01");
                result.setErrorMsg("报价成功、退回修改才能提交核保");
                return result;
            }            

            Map<String, String> param = new HashMap<String, String>();
            param.put("taskid", taskId);
            param.put("inscomcode", prvId);
            List<INSBPolicyitem> policyitemList = insbPolicyitemDao.getListByMap(param);
            if (policyitemList == null || policyitemList.size() < 1) {
                result.setRespCode("01");
                result.setErrorMsg("供应商" + prvId + ":未查到保单信息");
                return result;
            }
            String agentnum = policyitemList.get(0).getAgentnum();
            LogUtil.info("taskid=" + taskId + "&inscomcode=" + prvId + "&agentnum=" + agentnum);
            String message = appMyOrderInfoService.policySubmit(taskId, prvId, agentnum, 0, 0);
            if ("success".equals(message)) {
                result.setRespCode("00");
                result.setErrorMsg("提交核保成功");
                /* 记录chn推cm的时间点开始*/
                try {
                    Map<String, String> phaseTime = new HashMap<>();
                    phaseTime.put("channelId", quoteBean.getChannelId());
                    phaseTime.put("taskId", taskId);
                    phaseTime.put("prvId", quoteBean.getPrvId()==null?"":quoteBean.getPrvId());
                    phaseTime.put("taskState", quoteBean.getTaskState()==null?"":quoteBean.getTaskState());
                    phaseTime.put("phaseType", "submitInsureChnCallCM");
                    phaseTime.put("phaseSeq", "9");
                    chnService.recordPhaseTime(phaseTime);
                }catch (Exception e){
                    LogUtil.info("记录chn推cm支付的时间点异常:"+taskId+e.getMessage());
                }
             /* 记录chn推cm的时间点结束*/
            } else {
                result.setRespCode("01");
                result.setErrorMsg("供应商" + prvId + ":" + message);
                return result;
            }
            if (!isWorkTime(taskId, prvId)) {
                result.setMsgType("00");
                result.setErrorMsg("您好，现在是非工作时间，您的核保请求将在上班后第一时间为您处理。感谢您的支持！");
            }
        } catch (Exception ex) {
        	LogUtil.error(taskId + "-" + prvId + "提交核保失败" + ex.getMessage());
            ex.printStackTrace();
            result.setRespCode("01");
            result.setErrorMsg("供应商" + prvId + "提交核保失败：" + ex.getMessage());
        }
        return result;
    }

    //重新提交核保
    private QuoteBean updateInsure(String taskid, String inscomcode) {
        QuoteBean quoteBean = new QuoteBean();
        //调用工作流，重新报价
        try {
            INSBWorkflowmain mainModel = insbWorkflowmainDao.selectByInstanceId(taskid);
            if (null == mainModel) {
                quoteBean.setRespCode("01");
                quoteBean.setErrorMsg("获取操作人失败");
                return quoteBean;
            }
            //获取重新报价公司的的子流程id
            String workflowinstanceid = "";
            Map<String, String> map = new HashMap<>();
            map.put("taskid", taskid);
            map.put("inscomcode", inscomcode);
            INSBQuoteinfo insbQuoteinfo = appInsuredQuoteDao.selectInsbQuoteInfoByTaskidAndPid(map);
            if (null != insbQuoteinfo) {
                workflowinstanceid = insbQuoteinfo.getWorkflowinstanceid();
            }
            if (StringUtil.isEmpty(workflowinstanceid)) {
                quoteBean.setRespCode("01");
                quoteBean.setErrorMsg("报价信息表中信息不存在");
                return quoteBean;
            }

            LogUtil.info("核保退回-->人工核保subid="+workflowinstanceid);

            workflowsubService.updateWorkFlowSubData(null, insbQuoteinfo.getWorkflowinstanceid(), "19", "Completed", "人工核保", "修改提交", "admin");
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
		            Pool.addOrUpdate(task);
					try {
						Thread.sleep(2000);
						dispatchService.dispatchTask(task);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
            /* LogUtil.info("渠道接口修改投保信息开始调用工作流" + taskid + "======修改的供应商的id=" + inscomcode + "===当前时间=" + new Date());
            String result = WorkFlowUtil.updateInsuredInfoNoticeWorkflow(workflowinstanceid, mainModel.getOperator(), "核保退回", "");
            System.out.println(workflowinstanceid + "===========" + result);
            LogUtil.info("渠道接口修改投保信息调用工作流结束" + taskid + "===工作流返回的结果result=" + result + "===当前时间=" + new Date());
            if (StringUtil.isEmpty(result)) {
                quoteBean.setRespCode("01");
                quoteBean.setErrorMsg("获取信息失败了");
            } else {
                JSONObject jsonObj = JSONObject.fromObject(result);
                if (jsonObj.containsKey("message") && "fail".equals(jsonObj.getString("message"))) {
                    quoteBean.setRespCode("01");
                    quoteBean.setErrorMsg("重新提交核保失败");
                } else {
                    //删除报价信息表中写入的价格
                    deleteSuccessQuotePrice(insbQuoteinfo, taskid, inscomcode);
                    quoteBean.setRespCode("00");
                    quoteBean.setErrorMsg("重新提交核保成功");
                }
            } */
            
            //删除报价信息表中写入的价格
            //deleteSuccessQuotePrice(insbQuoteinfo, taskid, inscomcode);/*坑，价格清空了*/
            quoteBean.setRespCode("00");
            quoteBean.setErrorMsg("重新提交核保成功");
            /* 记录chn推cm的时间点开始*/
            try {
                Map<String, String> phaseTime = new HashMap<>();
                phaseTime.put("channelId", quoteBean.getChannelId());
                phaseTime.put("taskId", taskid);
                phaseTime.put("prvId", insbQuoteinfo.getInscomcode()==null?"":insbQuoteinfo.getInscomcode());
                phaseTime.put("taskState", "18");
                phaseTime.put("phaseType", "submitInsureChnCallCM");
                phaseTime.put("phaseSeq", "9");
                chnService.recordPhaseTime(phaseTime);
            }catch (Exception e){
                LogUtil.info("记录chn推cm支付的时间点异常:"+taskid+e.getMessage());
            }
             /* 记录chn推cm的时间点结束*/
        } catch (Exception ex) {
        	LogUtil.error(taskid + "重新提交核保失败" + ex.getMessage());
            ex.printStackTrace();
            quoteBean.setRespCode("01");
            quoteBean.setErrorMsg("重新提交核保失败" + ex.getMessage());
        }
        return quoteBean;
    }

    private void deleteSuccessQuotePrice(INSBQuoteinfo insbQuoteinfo, String taskid, String inscomcode) {
        LogUtil.info("渠道接口deleteSuccessQuotePrice" + taskid + "删除上次报价信息的供应商=" + inscomcode + "当前时间=" + new Date());
        //报价信息表  Quotediscountamount、Noti、Taskstatus
        if (null != insbQuoteinfo) {
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
        if (null != insbPolicyitems && insbPolicyitems.size() > 0) {
            for (INSBPolicyitem policyitem : insbPolicyitems) {
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
        if (null != insbCarinfohiss && insbCarinfohiss.size() > 0) {
            for (INSBCarinfohis carinfohis : insbCarinfohiss) {
                carinfohis.setNoti(null);
                insbCarinfohisDao.updateById(carinfohis);
            }
        }

        LogUtil.info("退回修改" + "" + "" + "当前时间=" + new Date());
        //退回修改，备份报价信息表数据，备份表存在则删除 20160505
        INSHCarkindprice inshCarkindprice = new INSHCarkindprice();
        inshCarkindprice.setTaskid(taskid);
        inshCarkindprice.setInscomcode(inscomcode);
        inshCarkindprice.setFairyoredi("appback");
        inshCarkindprice.setNodecode("H");
        List<INSHCarkindprice> inshCarkindprices = inshCarkindpriceDao.selectList(inshCarkindprice);
        if (null != inshCarkindprices && inshCarkindprices.size() > 0) {
            for (INSHCarkindprice carkindprice : inshCarkindprices) {
                inshCarkindpriceDao.deleteById(carkindprice.getId());
            }
        }
        //删除insbcarkindprice表报价数据
        INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
        insbCarkindprice.setTaskid(taskid);
        insbCarkindprice.setInscomcode(inscomcode);
        List<INSBCarkindprice> insbCarkindprices = insbCarkindpriceDao.selectList(insbCarkindprice);
        if (null != insbCarkindprices && insbCarkindprices.size() > 0) {
            for (INSBCarkindprice carkindprice : insbCarkindprices) {
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
    }

    //创建报价任务b接口--相对于a接口需要填写所有信息
    public QuoteBean createTaskB(QuoteBean quoteBean) throws Exception {
        String taskId = null;
        QuoteBean result = new QuoteBean();
        result.setRespCode("00");

        if (!hasInterfacePower("1", quoteBean.getInsureAreaCode(), quoteBean.getChannelId())) { //1-报价任务创建
            result.setRespCode("01");
            result.setErrorMsg("没有调用该接口的权限!");
            return result;
        }

        chkQuoteInput(quoteBean, result); //检查入参

        if (StringUtil.isNotEmpty(result.getErrorMsg())) {
            result.setRespCode("01");
            return result;
        }
        
        try {
            taskId = WorkFlowUtil.startWorkflowProcess("0");//0投保 1续保
        } catch (Exception e) {
            LogUtil.error("生成任务号失败", e);
            result.setRespCode("01");
            result.setErrorMsg("生成任务号失败");
            return result;
        }
        if (taskId == null) {
            result.setRespCode("01");
            result.setErrorMsg("生成任务号失败");
            return result;
        }
        LogUtil.info("生成任务号:" + taskId);
        result.setTaskId(taskId);
        CarInfoBean carInfoBean = quoteBean.getCarInfo(); // 不能为空
        Map<String, String> map = insbChannelagreementService.getDeptIdByChannelinnercodeAndPrvcode(quoteBean.getChannelId(), quoteBean.getInsureAreaCode());
        if (map == null) {
            LogUtil.error("查询不到渠道相关配置信息[" + quoteBean.getChannelId() + "," + quoteBean.getInsureAreaCode() + "]");
            result.setRespCode("01");
            result.setErrorMsg("查询不到渠道相关配置信息");
            return result;
        }

        Map<String, INSBPerson> personMap = savePerson(quoteBean, map, taskId); 
        INSBPerson insbCarownerPerson = personMap.get("insbCarownerPerson");
        //INSBPerson insbApplicantPerson = personMap.get("insbApplicantPerson");
        //INSBPerson insbInsuredPerson = personMap.get("insbInsuredPerson");
        //INSBPerson insbLegalrightclaimPerson = personMap.get("insbLegalrightclaimPerson");

        carInfoBean = packetCarInfoBean(carInfoBean, null);

        INSBCarinfo insbCarinfo = new INSBCarinfo();
        insbCarinfo.setOperator("渠道");
        insbCarinfo.setCreatetime(new Date());
        insbCarinfo.setModifytime(new Date());
        if (StringUtil.isNotEmpty(carInfoBean.getCarLicenseNo()))
            insbCarinfo.setCarlicenseno(carInfoBean.getCarLicenseNo());
        insbCarinfo.setVincode(carInfoBean.getVinCode());
        insbCarinfo.setStandardfullname(carInfoBean.getVehicleName());//车型信息描述
        insbCarinfo.setEngineno(carInfoBean.getEngineNo());

        insbCarinfo.setRegistdate(ModelUtil.conbertStringToNyrDate(carInfoBean.getRegistDate()));

        insbCarinfo.setIsTransfercar("0");
        insbCarinfo.setProperty("0");//所属性质(默认:个人用车)
        insbCarinfo.setCarproperty("1");//车辆使用性质(默认:家庭自用)
        insbCarinfo.setIsNew("0");// 0 旧车/1 新车
        insbCarinfo.setCarVehicularApplications(0); // 车辆用途 默认
        insbCarinfo.setDrivingarea("0");
        insbCarinfo.setOwner(insbCarownerPerson.getId());
        insbCarinfo.setOwnername(insbCarownerPerson.getName());
//		insbCarinfo.setDrivinglicenseurl(carInfo.get("brandimg"));//行驶证图片路径
//      insbCarinfo.setBrandimg(carInfoMap.get("brandimg"));//车辆照片（平台提供）
        insbCarinfo.setTaskid(taskId);
        if (carInfoBean.getPurpose() != null)
            insbCarinfo.setCarVehicularApplications(Integer.valueOf(carInfoBean.getPurpose()));
        if (carInfoBean.getCarProperty() != null)
            insbCarinfo.setCarproperty(carInfoBean.getCarProperty());
        if (carInfoBean.getProperty() != null)
            insbCarinfo.setProperty(carInfoBean.getProperty());
        if (carInfoBean.getDrivingArea() != null)
            insbCarinfo.setDrivingarea(carInfoBean.getDrivingArea());
        if ("Y".equals(carInfoBean.getIsNew())) {
            insbCarinfo.setIsNew("1");
            insbCarinfo.setCarlicenseno("新车未上牌");
        }
        if ("Y".equals(carInfoBean.getIsTransferCar())) {
            insbCarinfo.setIsTransfercar("1");
            insbCarinfo.setTransferdate(ModelUtil.conbertStringToNyrDate(carInfoBean.getTransferDate()));
        } else {
            insbCarinfo.setIsTransfercar("0");
        }

        insbCarinfoDao.insert(insbCarinfo);
        LogUtil.info("INSBCarinfo|报表数据埋点|"+JSONObject.fromObject(insbCarinfo).toString());

        INSBCarmodelinfo insbCarmodelinfo = new INSBCarmodelinfo();
        insbCarmodelinfo.setOperator("渠道");
        insbCarmodelinfo.setCreatetime(new Date());
        insbCarmodelinfo.setModifytime(new Date());
        insbCarmodelinfo.setCarinfoid(insbCarinfo.getId());
        insbCarmodelinfo.setBrandname(carInfoBean.getBrandName());
        insbCarmodelinfo.setStandardfullname(carInfoBean.getVehicleName());
        insbCarmodelinfo.setPrice(0.0);
        insbCarmodelinfo.setTaxprice(0.0);
        insbCarmodelinfo.setAnalogyprice(0.0);
        insbCarmodelinfo.setAnalogytaxprice(0.0);
        insbCarmodelinfo.setLoads(0.0);
        insbCarmodelinfo.setUnwrtweight(0.0);
        if (carInfoBean.getAnalogyPrice() != null)
            insbCarmodelinfo.setAnalogyprice(new Double(carInfoBean.getAnalogyPrice()));
        if (carInfoBean.getAnalogyTaxPrice() != null)
            insbCarmodelinfo.setAnalogytaxprice(new Double(carInfoBean.getAnalogyPrice()));
        insbCarmodelinfo.setVehicleid(carInfoBean.getVehicleId());
        if (StringUtil.isNotEmpty(carInfoBean.getPrice()))
            insbCarmodelinfo.setPrice(Double.parseDouble(carInfoBean.getPrice()));
        if (StringUtil.isNotEmpty(carInfoBean.getDisplacement()))
            insbCarmodelinfo.setDisplacement(Double.parseDouble(carInfoBean.getDisplacement()));

        if (StringUtil.isNotEmpty(carInfoBean.getFullWeight()))
            insbCarmodelinfo.setFullweight(Double.parseDouble(carInfoBean.getFullWeight()));
        insbCarmodelinfo.setFamilyname(carInfoBean.getFamilyName());
        insbCarmodelinfo.setStandardname(carInfoBean.getVehicleName());
        insbCarmodelinfo.setIsstandardcar("0");        
        if (StringUtil.isNotEmpty(carInfoBean.getSeat()))
            insbCarmodelinfo.setSeat(Integer.valueOf(carInfoBean.getSeat()));
        if (StringUtil.isNotEmpty(carInfoBean.getModelLoads())) {
            insbCarmodelinfo.setLoads(Double.valueOf(carInfoBean.getModelLoads()));
            insbCarmodelinfo.setUnwrtweight(Double.valueOf(carInfoBean.getModelLoads()));
        }
        insbCarmodelinfo.setCarprice(carInfoBean.getCarPrice());
        if ( StringUtil.isNotEmpty(carInfoBean.getPolicycarprice()) )
        	insbCarmodelinfo.setPolicycarprice(Double.valueOf(carInfoBean.getPolicycarprice()));

        insbCarmodelinfoDao.insert(insbCarmodelinfo);

        INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();//报价总表数据初始化
        insbQuotetotalinfo.setTaskid(taskId);
        insbQuotetotalinfo.setCreatetime(new Date());
        insbQuotetotalinfo.setOperator("渠道");
        insbQuotetotalinfo.setInscitycode(quoteBean.getInsureAreaCode());
        if (StringUtil.isNotEmpty(quoteBean.getMsgType()))
            insbQuotetotalinfo.setSourceFrom(quoteBean.getMsgType());
        else {
            insbQuotetotalinfo.setSourceFrom("channel");
        }
        insbQuotetotalinfo.setPurchaserChannel(quoteBean.getChannelId());
        insbQuotetotalinfo.setPurchaserid(quoteBean.getChannelUserId());
        insbQuotetotalinfo.setCallBackUrl(quoteBean.getCallbackurl());
        String jobnum = "";
        if (map != null) {
            jobnum = (String)map.get("jobnum");
            jobnum = jobnum.trim(); 
        }
        insbQuotetotalinfo.setAgentnum(jobnum);
//		insbQuotetotalinfo.setIsrenewal("0");
//		insbQuotetotalinfo.setInsurancebooks("0");
//		insbQuotetotalinfo.setWebpagekey("2");

        INSBRegion insbRegion = new INSBRegion();
        insbRegion.setComcode(quoteBean.getInsureAreaCode());
        insbRegion = insbRegionDao.selectOne(insbRegion);
        insbQuotetotalinfo.setInsprovincecode(insbRegion.getParentid());
        insbQuotetotalinfo.setInscityname(insbRegion.getComcodename());

        INSBRegion parentRegion = new INSBRegion();
        parentRegion.setComcode(insbRegion.getParentid());
        parentRegion = insbRegionDao.selectOne(parentRegion);
        insbQuotetotalinfo.setInsprovincename(parentRegion.getComcodename());
        insbQuotetotalinfoDao.insert(insbQuotetotalinfo);
        LogUtil.info("INSBQuotetotalinfo|报表数据埋点|"+JSONObject.fromObject(insbQuotetotalinfo).toString());

        //表分区数据
        String platformInnercode = null;
        List<String> deptcodes = new ArrayList<>(1);

        Set<String> addPrvIds = new HashSet<String>();
        List<ProviderBean> providers = quoteBean.getProviders();
        for (ProviderBean pb : providers) {
            addPrvIds.add(pb.getPrvId());

            if (!deptcodes.contains(map.get(pb.getPrvId()))) {
                deptcodes.add(map.get(pb.getPrvId()));
            }
        }
        if (addPrvIds.size() > 0) {
            for (String prvId : addPrvIds) {
                INSBCarinfohis insbCarinfohis = new INSBCarinfohis();
                insbCarinfohis.setTaskid(taskId);
                insbCarinfohis.setInscomcode(prvId);
                precessCarinfohis(taskId, prvId, insbCarinfohis, insbCarownerPerson, carInfoBean, false);

                INSBCarmodelinfohis insbCarmodelinfohis = new INSBCarmodelinfohis();
                insbCarmodelinfohis.setCarinfoid(insbCarinfo.getId());
                insbCarmodelinfohis.setInscomcode(prvId);

                processCarmodelinfohis(taskId, prvId, insbCarmodelinfohis, carInfoBean, insbCarinfo.getId(), false);

                addPersonHis(quoteBean, personMap, taskId, prvId); 
            }

            List<INSCDept> deptList = inscDeptDao.selectAllByComcodes(deptcodes);

            // 增加险种
            for (String prvId : addPrvIds) {
                String agreementid = insbChannelagreementService.getAgreementByArea(quoteBean.getChannelId(), prvId, insbQuotetotalinfo.getInscitycode());
                INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
                insbQuoteinfo.setInscomcode(prvId);
                insbQuoteinfo.setCreatetime(new Date());
                insbQuoteinfo.setModifytime(new Date());
                insbQuoteinfo.setOperator("渠道");
                insbQuoteinfo.setWorkflowinstanceid("");
                insbQuoteinfo.setInsprovincecode(insbQuotetotalinfo.getInsprovincecode());
                insbQuoteinfo.setInsprovincename(insbQuotetotalinfo.getInsprovincename());
                insbQuoteinfo.setInscitycode(insbQuotetotalinfo.getInscitycode());
                insbQuoteinfo.setInscityname(insbQuotetotalinfo.getInscityname());
                insbQuoteinfo.setOwnername(insbCarinfo.getOwnername());
                insbQuoteinfo.setQuotetotalinfoid(insbQuotetotalinfo.getId());
                
                String deptCode = map.get(prvId);
                if (StringUtil.isEmpty(deptCode)) {
                    throw new Exception("供应商" + prvId + "配置错误");
                }
                insbQuoteinfo.setDeptcode(deptCode);

                for (INSCDept dept : deptList) {
                    if (dept.getId().equals(deptCode)) {
                        platformInnercode = inscDeptService.getPlatformInnercode(dept.getDeptinnercode());
                        if (platformInnercode != null) {
                            insbQuoteinfo.setPlatforminnercode(Integer.parseInt(platformInnercode));
                        }
                    }
                }
                
                insbQuoteinfo.setBuybusitype("01");
                insbQuoteinfo.setAgreementid(agreementid);
                insbQuoteinfoDao.insert(insbQuoteinfo);

                InsureInfoBean insureInfoBean = quoteBean.getInsureInfo();
                // 交强险
                BaseInsureInfoBean efcInsureInfoBean = insureInfoBean.getEfcInsureInfo();
                if (efcInsureInfoBean != null) {
                    addPolicyitem(taskId, prvId, efcInsureInfoBean, insbCarinfo.getId(), map, "1");
                    addEfcCarkindprice(taskId, prvId);
                }
                BaseInsureInfoBean taxInsureInfoBean = insureInfoBean.getTaxInsureInfo();
                if (taxInsureInfoBean != null && "Y".equals(taxInsureInfoBean.getIsPaymentTax())) {
                    addTaxCarkindprice(taskId, prvId);
                }

                BizInsureInfoBean bizInsureInfoBean = insureInfoBean.getBizInsureInfo();
                if (bizInsureInfoBean != null) {
                    // 商业险
                    addPolicyitem(taskId, prvId, bizInsureInfoBean, insbCarinfo.getId(), map, "0");
                    addBizCarkindprice(taskId, prvId, bizInsureInfoBean);
                }
                
                saveCarmodelinfoToData(taskId, prvId);
            }
        }

        //保存备注信息
        String remark = quoteBean.getRemark();
        if (!StringUtil.isEmpty(remark)) {
            saveRemark(taskId, remark, insbQuotetotalinfo.getOperator(), null);
        }

        //mini保存agenttask数据
        saveAgentTask(taskId,quoteBean.getChannelUserId(),quoteBean.getChannelId());

        return result;
    }

    private void saveAgentTask(String taskId,String agentid,String channelId ){
        try {
            if (channelId.equals(ConfigUtil.getPropString("miniChannelId", "nqd_minizzb2016"))) {
                INSBAgentTask insbAgentTask = new INSBAgentTask();
                insbAgentTask.setAgentid(agentid);
                insbAgentTask.setTaskid(taskId);
                Long total = insbAgentTaskService.queryCount(insbAgentTask);
                if(total<=0) {
                    insbAgentTask.setStatus("0");
                    insbAgentTask.setCreatetime(new Date());
                    insbAgentTaskService.saveAgentTask(insbAgentTask);
                }
            }
        }catch(Exception e){
            LogUtil.info("saveAgentTask error ! taskid="+taskId+" agentid="+agentid+" errmsg="+e.getMessage());
        }
    }

    //保存备注信息
    private void saveRemark(String taskId, String remark, String operator, String remarkCode) {
        INSBWorkflowmaintrack insbWorkflowmaintrack = new INSBWorkflowmaintrack();
        insbWorkflowmaintrack.setInstanceid(taskId);
        List<INSBWorkflowmaintrack> insbWorkflowmaintracks = insbWorkflowmaintrackDao.selectList(insbWorkflowmaintrack);
        if (null != insbWorkflowmaintracks && insbWorkflowmaintracks.size() > 0) {
            INSBWorkflowmaintrack workflowmaintrack = insbWorkflowmaintracks.get(0);
            INSBUsercomment insbUsercomment = new INSBUsercomment();
            insbUsercomment.setOperator(operator);
            insbUsercomment.setCreatetime(new Date());
            insbUsercomment.setTrackid(workflowmaintrack.getId());
            insbUsercomment.setTracktype(1);
            insbUsercomment.setCommentcontent(remark);
            //insbUsercomment.setCommentcontenttype(StringUtil.isEmpty(remarkCode) ? 0 : Integer.parseInt(remarkCode));
            //insbUsercomment.setCommenttype(1);
            insbUsercommentDao.insert(insbUsercomment);
        }
    }
    
    //保存人员信息
    private Map<String, INSBPerson> savePerson(QuoteBean quoteBean, Map<String, String> map, String taskId) throws Exception {
    	Date createDate = new Date();
    	//车主
        PersonBean carOwnerInfoBean = quoteBean.getCarOwner(); 
        INSBPerson insbCarownerPerson = new INSBPerson();
        insbCarownerPerson.setIdcardno(carOwnerInfoBean.getIdcardNo());
        if ( StringUtil.isNotEmpty(carOwnerInfoBean.getIdcardType()) ) {
            insbCarownerPerson.setIdcardtype(Integer.parseInt(carOwnerInfoBean.getIdcardType()));
        } else {
        	if ( StringUtil.isNotEmpty(carOwnerInfoBean.getIdcardNo()) ) {
        		insbCarownerPerson.setIdcardtype(0); //0-身份证
        	}
        }
        insbCarownerPerson.setTaskid(taskId);
        insbCarownerPerson.setName(carOwnerInfoBean.getName());
        insbCarownerPerson.setCellphone(carOwnerInfoBean.getPhone());
        insbCarownerPerson.setOperator("渠道");
        insbCarownerPerson.setCreatetime(createDate);
        insbCarownerPerson.setGender(0); //默认性别
        insbCarownerPerson.setEmail(carOwnerInfoBean.getEmail()); 
        
        INSBPerson insbApplicantPerson = new INSBPerson();
        INSBPerson insbInsuredPerson = new INSBPerson();
        INSBPerson insbLegalrightclaimPerson = new INSBPerson();
        INSBPerson insbRelationperson = new INSBPerson();
		PropertyUtils.copyProperties(insbApplicantPerson, insbCarownerPerson);
		PropertyUtils.copyProperties(insbInsuredPerson, insbCarownerPerson);
		PropertyUtils.copyProperties(insbLegalrightclaimPerson, insbCarownerPerson);
		PropertyUtils.copyProperties(insbRelationperson, insbCarownerPerson);
        
        insbPersonDao.insert(insbCarownerPerson);
        insbPersonDao.insert(insbApplicantPerson);
        insbPersonDao.insert(insbInsuredPerson);
        insbPersonDao.insert(insbLegalrightclaimPerson);
        insbPersonDao.insert(insbRelationperson);
        
        map.put("insbCarownerPersonId", insbCarownerPerson.getId());
        map.put("insbCarownerPersonName", insbCarownerPerson.getName());
        map.put("insbInsuredPersonId", insbInsuredPerson.getId());
        map.put("insbInsuredPersonName", insbInsuredPerson.getName());
        map.put("insbApplicantPersonId", insbApplicantPerson.getId());
        map.put("insbApplicantPersonName", insbApplicantPerson.getName());
        
        Map<String, INSBPerson> resultMap = new HashMap<String, INSBPerson>();
        resultMap.put("insbCarownerPerson", insbCarownerPerson);
        resultMap.put("insbApplicantPerson", insbApplicantPerson);
        resultMap.put("insbInsuredPerson", insbInsuredPerson);
        resultMap.put("insbLegalrightclaimPerson", insbLegalrightclaimPerson);
        resultMap.put("insbRelationperson", insbRelationperson);

        INSBCarowneinfo insbCarowneinfo = new INSBCarowneinfo();
        insbCarowneinfo.setOperator("渠道");
        insbCarowneinfo.setCreatetime(createDate);
        insbCarowneinfo.setModifytime(createDate);
        insbCarowneinfo.setTaskid(taskId);
        insbCarowneinfo.setPersonid(insbCarownerPerson.getId());
        insbCarowneinfoDao.insert(insbCarowneinfo);

        INSBRelationperson insbRelation = new INSBRelationperson();
        insbRelation.setOperator("渠道");
        insbRelation.setCreatetime(createDate);
        insbRelation.setModifytime(createDate);
        insbRelation.setTaskid(taskId);
        insbRelation.setPersonid(insbRelationperson.getId());
        insbRelationpersonDao.insert(insbRelation);
        
        INSBInsured insbInsured = new INSBInsured(); //被保人
        insbInsured.setCreatetime(createDate);
        insbInsured.setOperator("渠道");
        insbInsured.setPersonid(insbInsuredPerson.getId());
        insbInsured.setTaskid(taskId);
        insbInsuredDao.insert(insbInsured);

        INSBApplicant insbApplicant = new INSBApplicant(); //投保人
        insbApplicant.setCreatetime(createDate);
        insbApplicant.setOperator("渠道");
        insbApplicant.setPersonid(insbApplicantPerson.getId());
        insbApplicant.setTaskid(taskId);
        insbApplicantDao.insert(insbApplicant);

        INSBLegalrightclaim insbLegalrightclaim = new INSBLegalrightclaim(); //权益索赔人
        insbLegalrightclaim.setCreatetime(createDate);
        insbLegalrightclaim.setOperator("渠道");
        insbLegalrightclaim.setPersonid(insbLegalrightclaimPerson.getId());
        insbLegalrightclaim.setTaskid(taskId);
        insbLegalrightclaimDao.insert(insbLegalrightclaim);
        
        return resultMap;
    }
    
    //更新人员信息
    private Map<String, INSBPerson> updatePerson(QuoteBean quoteBean, Map<String, String> map, String taskId) {
    	//修改车主信息
        INSBCarowneinfo insbCarowneinfo = insbCarowneinfoDao.selectByTaskId(taskId);
        PersonBean carOwnerPersonBean = quoteBean.getCarOwner();
        INSBPerson insbCarownerPerson = insbPersonDao.selectById(insbCarowneinfo.getPersonid()); //车主信息一定能查到

        Date nowDate = new Date();
        if (carOwnerPersonBean != null) { //只有修改
            if (carOwnerPersonBean.getIdcardNo() != null) {
                if (carOwnerPersonBean.getIdcardType() != null) {
                    insbCarownerPerson.setIdcardtype(Integer.valueOf(carOwnerPersonBean.getIdcardType()));
                }
                insbCarownerPerson.setIdcardno(carOwnerPersonBean.getIdcardNo());
            }
            if (carOwnerPersonBean.getPhone() != null)
                insbCarownerPerson.setCellphone(carOwnerPersonBean.getPhone());
            if (carOwnerPersonBean.getEmail() != null)
                insbCarownerPerson.setEmail(carOwnerPersonBean.getEmail());
            insbCarownerPerson.setModifytime(nowDate);
            insbPersonDao.updateById(insbCarownerPerson);
            
            if ( quoteBean.getApplicant() == null && 
            	 quoteBean.getInsured() == null && 
            	 quoteBean.getBeneficiary() == null ) {
            	
            	quoteBean.setApplicant(carOwnerPersonBean);
            	quoteBean.setInsured(carOwnerPersonBean);
            	quoteBean.setBeneficiary(carOwnerPersonBean);
            }
        }
        map.put("insbCarownerPersonId", insbCarownerPerson.getId());
        map.put("insbCarownerPersonName", insbCarownerPerson.getName());
        
    	INSBApplicant insbApplicant = insbApplicantDao.selectByTaskID(taskId);
        INSBPerson insbApplicantPerson = insbPersonDao.selectById(insbApplicant.getPersonid());
        if (quoteBean.getApplicant() != null) {
    		insbApplicantPerson.setModifytime(nowDate);
    		if (quoteBean.getApplicant().getName() != null) 
    			insbApplicantPerson.setName(quoteBean.getApplicant().getName());
    		if (quoteBean.getApplicant().getEmail() != null)
    			insbApplicantPerson.setEmail(quoteBean.getApplicant().getEmail());
            if (quoteBean.getApplicant().getIdcardType() != null)
            	insbApplicantPerson.setIdcardtype(Integer.valueOf(quoteBean.getApplicant().getIdcardType()));
            if (quoteBean.getApplicant().getIdcardNo() != null)
            	insbApplicantPerson.setIdcardno(quoteBean.getApplicant().getIdcardNo());
            if (quoteBean.getApplicant().getPhone() != null)
            	insbApplicantPerson.setCellphone(quoteBean.getApplicant().getPhone());
    		
    		insbPersonDao.updateById(insbApplicantPerson);
        	
        }
        map.put("insbApplicantPersonId", insbApplicantPerson.getId());
        map.put("insbApplicantPersonName", insbApplicantPerson.getName());
        
        INSBInsured insbInsured = insbInsuredDao.selectInsuredByTaskId(taskId);
    	INSBPerson insbInsuredPerson = insbPersonDao.selectById(insbInsured.getPersonid());
        if (quoteBean.getInsured() != null) {
    		insbInsuredPerson.setModifytime(nowDate);
    		if (quoteBean.getInsured().getName() != null) 
    			insbInsuredPerson.setName(quoteBean.getInsured().getName());
    		if (quoteBean.getInsured().getEmail() != null)
    			insbInsuredPerson.setEmail(quoteBean.getInsured().getEmail());
            if (quoteBean.getInsured().getIdcardType() != null)
            	insbInsuredPerson.setIdcardtype(Integer.valueOf(quoteBean.getInsured().getIdcardType()));
            if (quoteBean.getInsured().getIdcardNo() != null)
            	insbInsuredPerson.setIdcardno(quoteBean.getInsured().getIdcardNo());
            if (quoteBean.getInsured().getPhone() != null)
            	insbInsuredPerson.setCellphone(quoteBean.getInsured().getPhone());
    		
    		insbPersonDao.updateById(insbInsuredPerson);
        	
        }
        map.put("insbInsuredPersonId", insbInsuredPerson.getId());
        map.put("insbInsuredPersonName", insbInsuredPerson.getName());
        
        INSBLegalrightclaim insbLegalrightclaim = insbLegalrightclaimDao.selectByTaskID(taskId);
    	INSBPerson insbLegalrightclaimPerson = insbPersonDao.selectById(insbLegalrightclaim.getPersonid());
        if (quoteBean.getBeneficiary() != null) {
    		insbLegalrightclaimPerson.setModifytime(nowDate);
    		if (quoteBean.getBeneficiary().getName() != null) 
    			insbLegalrightclaimPerson.setName(quoteBean.getBeneficiary().getName());
    		if (quoteBean.getBeneficiary().getEmail() != null)
    			insbLegalrightclaimPerson.setEmail(quoteBean.getBeneficiary().getEmail());
            if (quoteBean.getBeneficiary().getIdcardType() != null)
            	insbLegalrightclaimPerson.setIdcardtype(Integer.valueOf(quoteBean.getBeneficiary().getIdcardType()));
            if (quoteBean.getBeneficiary().getIdcardNo() != null)
            	insbLegalrightclaimPerson.setIdcardno(quoteBean.getBeneficiary().getIdcardNo());
            if (quoteBean.getBeneficiary().getPhone() != null)
            	insbLegalrightclaimPerson.setCellphone(quoteBean.getBeneficiary().getPhone());
    		
    		insbPersonDao.updateById(insbLegalrightclaimPerson);
        	
        }
        
        INSBRelationperson INSBRelation = insbRelationpersonDao.selectByTaskID(taskId);
        INSBPerson insbRelationperson = insbPersonDao.selectById(INSBRelation.getPersonid());
        if (carOwnerPersonBean != null) { //联系人跟车主一致
            if (carOwnerPersonBean.getIdcardNo() != null) {
                if (carOwnerPersonBean.getIdcardType() != null) {
                	insbRelationperson.setIdcardtype(Integer.valueOf(carOwnerPersonBean.getIdcardType()));
                }
                insbRelationperson.setIdcardno(carOwnerPersonBean.getIdcardNo());
            }
            if (carOwnerPersonBean.getPhone() != null)
            	insbRelationperson.setCellphone(carOwnerPersonBean.getPhone());
            if (carOwnerPersonBean.getEmail() != null)
            	insbRelationperson.setEmail(carOwnerPersonBean.getEmail());
            insbRelationperson.setModifytime(nowDate);
            insbPersonDao.updateById(insbRelationperson);
        }
        
        Map<String, INSBPerson> resultMap = new HashMap<String, INSBPerson>();
        resultMap.put("insbCarownerPerson", insbCarownerPerson);
        resultMap.put("insbApplicantPerson", insbApplicantPerson);
        resultMap.put("insbInsuredPerson", insbInsuredPerson);
        resultMap.put("insbLegalrightclaimPerson", insbLegalrightclaimPerson);
        resultMap.put("insbRelationperson", insbRelationperson);
        
        return resultMap;
    }
    
    //从其它bean里copy保存核保补充数据项
    private void copySaveSupplyInfo(QuoteBean quoteBean) {
    	String taskId = quoteBean.getTaskId();
    	String prvId = quoteBean.getPrvId();
    	
    	PersonBean carOwner = quoteBean.getCarOwner();
    	PersonBean applicant = quoteBean.getApplicant();
    	PersonBean insured = quoteBean.getInsured();
    	PersonBean beneficiary = quoteBean.getBeneficiary();
    	
    	if ( StringUtil.isEmpty(taskId) || StringUtil.isEmpty(prvId) ||
    			(carOwner == null && applicant == null && insured == null && beneficiary == null) ) {
    		return;
    	}
    	
        List<String> supplyParams = new ArrayList<String>();
        
        if (carOwner != null) {
	    	if ( StringUtil.isNotEmpty(carOwner.getPhone()) ) { //补充信息.车主手机号码
	    		supplyParams.add("ownerMobile" + "|||" + carOwner.getPhone());
	    	}
	    	if ( StringUtil.isNotEmpty(carOwner.getEmail()) ) { //补充信息.车主邮箱
	    		supplyParams.add("ownerEmail" + "|||" + carOwner.getEmail()); 
	    	}
	    	if ( StringUtil.isNotEmpty(carOwner.getAddress()) ) { //补充信息.车主身份证地址
	    		supplyParams.add("ownerAddress" + "|||" + carOwner.getAddress()); 
	    	}
	    	if ( StringUtil.isNotEmpty(carOwner.getDrivingLicenseAddress() ) ) { //补充信息.行驶证地址
	    		supplyParams.add("drivingLicenseAddress" + "|||" + carOwner.getDrivingLicenseAddress());
	    	}
        }
    	
    	if (insured != null) {
	    	if ( StringUtil.isNotEmpty(insured.getPhone()) ) { //补充信息.被保人手机号码
	    		supplyParams.add("insuredMobile" + "|||" + insured.getPhone());
	    	}
	    	if ( StringUtil.isNotEmpty(insured.getEmail()) ) { //补充信息.被保人邮箱
	    		supplyParams.add("insuredEmail" + "|||" + insured.getEmail());
	    	}
	    	if ( StringUtil.isNotEmpty(insured.getAddress()) ) { //补充信息.被保人身份证地址
	    		supplyParams.add("insuredAddress" + "|||" + insured.getAddress());
	    	}
    	}
    	
    	if (applicant != null) {
	    	if ( StringUtil.isNotEmpty(applicant.getPhone()) ) { //补充信息.投保人手机号码
	    		supplyParams.add("applicantMobile" + "|||" + applicant.getPhone());
	    	}
	    	if ( StringUtil.isNotEmpty(applicant.getEmail()) ) { //补充信息.投保人邮箱
	    		supplyParams.add("applicantEmail" + "|||" + applicant.getEmail());
	    	}                
	    	if ( StringUtil.isNotEmpty(applicant.getAddress()) ) { //补充信息.投保人身份证地址
	    		supplyParams.add("applicantAddress" + "|||" + applicant.getAddress());
	    	}
    	}
	    	                   
	    if (beneficiary != null) { 	
	    	if ( StringUtil.isNotEmpty(beneficiary.getName()) ) { //补充信息.权益索赔人姓名
	    		supplyParams.add("claimantName" + "|||" + beneficiary.getName());
	    	}                  
	    	if ( StringUtil.isNotEmpty(beneficiary.getIdcardType()) ) { //补充信息.权益索赔人证件类型
	    		supplyParams.add("claimantDocumentType" + "|||" + beneficiary.getIdcardType());
	    	}                    
	    	if ( StringUtil.isNotEmpty(beneficiary.getIdcardNo()) ) { //补充信息.权益索赔人证件号码
	    		supplyParams.add("claimantDocumentNumber" + "|||" + beneficiary.getIdcardNo());
	    	}                   
	    	if ( StringUtil.isNotEmpty(beneficiary.getPhone()) ) { //补充信息.权益索赔人手机号码
	    		supplyParams.add("claimantMobile" + "|||" + beneficiary.getPhone());
	    	}                   
	    	if ( StringUtil.isNotEmpty(beneficiary.getEmail()) ) { //补充信息.权益索赔人邮箱
	    		supplyParams.add("claimantEmail" + "|||" + beneficiary.getEmail());
	    	}  
	    }
    	
    	insbInsuresupplyparamService.updateByTask(supplyParams, taskId, prvId, "渠道");
    }
    
    //新增人员his信息-带his结尾的表除了关联了taskid，还关联了prvid
    private void addPersonHis(QuoteBean quoteBean, Map<String, INSBPerson> personMap, String taskId, String prvId) {
    	//INSBPerson insbCarownerPerson = personMap.get("insbCarownerPerson");
        INSBPerson insbApplicantPerson = personMap.get("insbApplicantPerson");
        INSBPerson insbInsuredPerson = personMap.get("insbInsuredPerson");
        INSBPerson insbLegalrightclaimPerson = personMap.get("insbLegalrightclaimPerson");
        INSBPerson insbRelationperson = personMap.get("insbRelationperson");
        
        Date nowDate = new Date();
        INSBApplicanthis insbApplicanthis = new INSBApplicanthis();
        insbApplicanthis.setTaskid(taskId);
        insbApplicanthis.setInscomcode(prvId);
        insbApplicanthis.setPersonid(insbApplicantPerson.getId());
        insbApplicanthis.setOperator("渠道");
        insbApplicanthis.setCreatetime(nowDate);
        insbApplicanthis.setModifytime(nowDate);
        insbApplicanthisDao.insert(insbApplicanthis);

        INSBInsuredhis insbInsuredhis = new INSBInsuredhis();
        insbInsuredhis.setTaskid(taskId);
        insbInsuredhis.setInscomcode(prvId);
        insbInsuredhis.setPersonid(insbInsuredPerson.getId());
        insbInsuredhis.setOperator("渠道");
        insbInsuredhis.setCreatetime(nowDate);
        insbInsuredhis.setModifytime(nowDate);
        insbInsuredhisDao.insert(insbInsuredhis);

        INSBLegalrightclaimhis insbLegalrightclaimhis = new INSBLegalrightclaimhis();
        insbLegalrightclaimhis.setTaskid(taskId);
        insbLegalrightclaimhis.setInscomcode(prvId);
        insbLegalrightclaimhis.setPersonid(insbLegalrightclaimPerson.getId());
        insbLegalrightclaimhis.setOperator("渠道");
        insbLegalrightclaimhis.setCreatetime(nowDate);
        insbLegalrightclaimhis.setModifytime(nowDate);
        insbLegalrightclaimhisDao.insert(insbLegalrightclaimhis);

        INSBRelationpersonhis insbRelationpersonhis = new INSBRelationpersonhis();
        insbRelationpersonhis.setOperator("渠道");
        insbRelationpersonhis.setCreatetime(nowDate);
        insbRelationpersonhis.setModifytime(nowDate);
        insbRelationpersonhis.setTaskid(taskId);
        insbRelationpersonhis.setInscomcode(prvId); 
        insbRelationpersonhis.setPersonid(insbRelationperson.getId());
        insbRelationpersonhisDao.insert(insbRelationpersonhis);
    }
    
    //核保补充数据项处理
    private void procInsureSupplyInfo(QuoteBean quoteBean) {
    	String taskId = quoteBean.getTaskId();
    	String prvId = quoteBean.getPrvId();
    	List<InsureSupplyBean> insureSupplyInfos = quoteBean.getInsureSupplys();
    	if ( StringUtil.isEmpty(taskId) || StringUtil.isEmpty(prvId) ||
    			insureSupplyInfos == null || insureSupplyInfos.isEmpty() ) {
    		return;
    	}
    	
    	List<String> supplyParams = new ArrayList<String>();
    	for (InsureSupplyBean insureSupplyInfo : insureSupplyInfos) {
    		String supplyParam = insureSupplyInfo.getItemCode() + "|||" + insureSupplyInfo.getItemValue();
    		supplyParams.add(supplyParam);
    	}
    	
    	insbInsuresupplyparamService.updateByTask(supplyParams, taskId, prvId, "渠道");
    }

    //修改任务
    @Override
    public QuoteBean updateTask(QuoteBean quoteBean) throws Exception {
        String taskId = quoteBean.getTaskId();
        QuoteBean result = new QuoteBean();
        result.setRespCode("00");
        
        // 查询工作流不能为提交报价的后续节点  	

        // 基本信息校验
        if (StringUtil.isEmpty(taskId)) {
            result.setErrorMsg("主任务号不能为空");
            result.setRespCode("01");
            return result;
        }
        INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
        insbQuotetotalinfo.setTaskid(taskId);
        insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
        if (!quoteBean.getChannelId().equals(insbQuotetotalinfo.getPurchaserChannel())) {
            result.setRespCode("01");
            result.setErrorMsg("请提供正确的任务号");
            return result;
        }
        quoteBean.setInsureAreaCode(insbQuotetotalinfo.getInscitycode()); 
        if (!hasInterfacePower("3", insbQuotetotalinfo.getInscitycode(), quoteBean.getChannelId())) { //3-投保数据修改
            result.setRespCode("01");
            result.setErrorMsg("没有调用该接口的权限!");
            return result;
        }
        
        INSBCarinfo insbCarinfo = insbCarinfoDao.selectCarinfoByTaskId(taskId);//getCarInfo(taskId);
        if (insbCarinfo == null) {
            result.setRespCode("01");
            result.setErrorMsg("不存在主车信息");
            return result;
        }
        if (quoteBean.getInsureInfo() != null) {
            if (quoteBean.getInsureInfo().getEfcInsureInfo() == null && quoteBean.getInsureInfo().getBizInsureInfo() == null) {
                result.setRespCode("01");
                result.setErrorMsg("存在insureInfo信息，交强和商业不能同时为空");
                return result;
            }
        }
        
        if ( null != quoteBean.getCarOwner() && StringUtil.isNotEmpty(quoteBean.getCarOwner().getName()) &&
                !insbCarinfo.getOwnername().equals(quoteBean.getCarOwner().getName()) ) {
            result.setRespCode("01");
            result.setErrorMsg("车主姓名不能修改");
            return result;
        }
        if ( null != quoteBean.getCarInfo() ) {
        	String carLicenseNo = quoteBean.getCarInfo().getCarLicenseNo();
        	String carLicenseNoData = insbCarinfo.getCarlicenseno();
        	String isNew = quoteBean.getCarInfo().getIsNew();
        	String isNewData = "";
        	if ("0".equals(insbCarinfo.getIsNew())) {
        		isNewData = "N";
        	} else if ("1".equals(insbCarinfo.getIsNew())) {
        		isNewData = "Y";
        	}
        	
        	if ( (carLicenseNo != null && !carLicenseNoData.equals(carLicenseNo)) || 
        		 (isNew != null && !isNewData.equals(isNew)) ) {
	            result.setRespCode("01");
	            result.setErrorMsg("任务已创建，不允许修改车牌号，请重新创建新报价");
	            return result;
        	}
        }
        
        //检查入参
        StringBuffer msg = new StringBuffer();
        if ( !transAndCheckImputCommon(quoteBean, msg, "updateTask") ) {
        	result.setRespCode("01");
            result.setErrorMsg(msg.toString());
            return result;
        }

        Map<String, String> map = insbChannelagreementService.getDeptIdByChannelinnercodeAndPrvcode(quoteBean.getChannelId(), insbQuotetotalinfo.getInscitycode());
        if (map == null) {
            LogUtil.error("查询不到渠道相关配置信息[" + quoteBean.getChannelId() + "," + insbQuotetotalinfo.getInscitycode() + "]");
            result.setRespCode("01");
            result.setErrorMsg("查询不到渠道相关配置信息");
            return result;
        }

        //表分区数据
        String platformInnercode = null;
        List<String> deptcodes = new ArrayList<>(1);
        
        List<ProviderBean> providers = quoteBean.getProviders();

        List<String> delPrvIds = new ArrayList<String>();
        List<String> modPrvIds = new ArrayList<String>();
        List<String> addPrvIds = new ArrayList<String>();

        if (StringUtil.isNotEmpty(quoteBean.getPrvId())) { // 只修改一家
            modPrvIds.add(quoteBean.getPrvId());
        } else {
            List<Map<String, String>> list = insbQuoteinfoDao.selectByTaskid(taskId);
            if (providers == null) { // 全部都是修改
                for (Map<String, String> map1 : list) {
                    modPrvIds.add(map1.get("inscomcode"));
                }
            } else {
                for (Map<String, String> map1 : list) {
                    if (map1 != null) {
                        boolean isFind = false;
                        for (ProviderBean pb : providers) {
                            if (map1.get("inscomcode").equals(pb.getPrvId())) {
                                modPrvIds.add(pb.getPrvId());
                                providers.remove(pb);
                                isFind = true;
                                break;
                            }
                        }
                        if (!isFind)
                            delPrvIds.add(map1.get("inscomcode"));
                    }
                }
                for (ProviderBean pb : providers) {
                    addPrvIds.add(pb.getPrvId());

                    if (!deptcodes.contains(map.get(pb.getPrvId()))) {
                        deptcodes.add(map.get(pb.getPrvId()));
                    }
                }
            }
        }
        // 去除重复的供应商
        Set<String> set = new HashSet<String>();
        set.addAll(delPrvIds);
        delPrvIds.clear();
        delPrvIds.addAll(set);
        set.clear();
        set.addAll(modPrvIds);
        modPrvIds.clear();
        modPrvIds.addAll(set);
        set.clear();
        set.addAll(addPrvIds);
        addPrvIds.clear();
        addPrvIds.addAll(set);
        set.clear();

        Map<String, INSBPerson> personMap = updatePerson(quoteBean, map, taskId);
        INSBPerson insbCarownerPerson = personMap.get("insbCarownerPerson");
        //INSBPerson insbApplicantPerson = personMap.get("insbApplicantPerson");
        //INSBPerson insbInsuredPerson = personMap.get("insbInsuredPerson");
        //INSBPerson insbLegalrightclaimPerson = personMap.get("insbLegalrightclaimPerson");
        
        CarInfoBean carInfoBean = quoteBean.getCarInfo();            

        carInfoBean = packetCarInfoBean(carInfoBean, insbCarinfo);
        
        //更新InsbCarinfo和INSBCarmodelinfo
        updateInsbCarinfo(insbCarinfo, carInfoBean);

        //需要修改的供应商
        if (modPrvIds.size() > 0) {
            for (String prvId : modPrvIds) {
                if (quoteBean.getCarInfo() != null) { 
                    // 修改 insbcarinfohis
                    INSBCarinfohis insbCarinfohis = new INSBCarinfohis();
                    insbCarinfohis.setTaskid(taskId);
                    insbCarinfohis.setInscomcode(prvId);
                    insbCarinfohis = insbCarinfohisDao.selectOne(insbCarinfohis); // 一定要能查到
                    precessCarinfohis(taskId, prvId, insbCarinfohis, insbCarownerPerson, carInfoBean, true);

                    INSBCarmodelinfohis insbCarmodelinfohis = new INSBCarmodelinfohis();
                    insbCarmodelinfohis.setCarinfoid(insbCarinfo.getId());
                    insbCarmodelinfohis.setInscomcode(prvId);
                    insbCarmodelinfohis = insbCarmodelinfohisDao.selectOne(insbCarmodelinfohis);
                    
                    processCarmodelinfohis(taskId, prvId, insbCarmodelinfohis, carInfoBean, insbCarinfo.getId(), true);
                    saveCarmodelinfoToData(taskId, prvId);
                }
                
                //修改险种信息
                InsureInfoBean insureInfoBean = quoteBean.getInsureInfo();
                if (insureInfoBean != null) {
                    INSBCarkindprice insbBizCarkindprice = new INSBCarkindprice();
                    insbBizCarkindprice.setTaskid(taskId);
                    insbBizCarkindprice.setInscomcode(prvId);
                    insbCarkindpriceDao.deleteByObj(insbBizCarkindprice); // 全部干掉再重新添加
                    
                    INSBCarconfig carconfig = new INSBCarconfig();
            		carconfig.setTaskid(taskId);
            		carconfig.setNoti(prvId); 
            		List<INSBCarconfig> carconfigs = insbCarconfigDao.selectList(carconfig);
        			for(INSBCarconfig insbCarconfig : carconfigs){
        				insbCarconfigDao.deleteById(insbCarconfig.getId());
        			}
                    
                    BaseInsureInfoBean efcInsureInfoBean = insureInfoBean.getEfcInsureInfo();
                    if (efcInsureInfoBean != null) {
                        INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
                        insbPolicyitem.setTaskid(taskId);
                        insbPolicyitem.setRisktype("1");
                        insbPolicyitem.setInscomcode(prvId);
                        insbPolicyitem = insbPolicyitemDao.selectOne(insbPolicyitem);
                        if (insbPolicyitem != null) { // 修改
                            if (efcInsureInfoBean.getStartDate() != null)
                                insbPolicyitem.setStartdate(ModelUtil.conbertStringToNyrDate(efcInsureInfoBean.getStartDate()));
                            if (efcInsureInfoBean.getEndDate() != null)
                                insbPolicyitem.setEnddate(ModelUtil.conbertStringToNyrDate(efcInsureInfoBean.getEndDate()));
                            insbPolicyitemDao.updateById(insbPolicyitem);
                        } else { // 增加
                            addPolicyitem(taskId, prvId, efcInsureInfoBean, insbCarinfo.getId(), map, "1");
                        }
                        addEfcCarkindprice(taskId, prvId);
                    } else { // 没有交强险
                        INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
                        insbPolicyitem.setTaskid(taskId);
                        insbPolicyitem.setRisktype("1");
                        insbPolicyitem.setInscomcode(prvId);
                        insbPolicyitemDao.deleteByObj(insbPolicyitem);
                    }

                    BaseInsureInfoBean taxInsureInfoBean = insureInfoBean.getTaxInsureInfo();
                    if (taxInsureInfoBean != null && "Y".equals(taxInsureInfoBean.getIsPaymentTax())) {
                        addTaxCarkindprice(taskId, prvId);
                    }
                    BizInsureInfoBean bizInsureInfoBean = insureInfoBean.getBizInsureInfo();
                    if (bizInsureInfoBean != null) {
                        INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
                        insbPolicyitem.setTaskid(taskId);
                        insbPolicyitem.setRisktype("0");
                        insbPolicyitem.setInscomcode(prvId);
                        insbPolicyitem = insbPolicyitemDao.selectOne(insbPolicyitem);
                        if (insbPolicyitem == null) {
                            addPolicyitem(taskId, prvId, bizInsureInfoBean, insbCarinfo.getId(), map, "0");
                        } else {
                            if (StringUtil.isNotEmpty(bizInsureInfoBean.getStartDate()))
                                insbPolicyitem.setStartdate(ModelUtil.conbertStringToNyrDate(bizInsureInfoBean.getStartDate()));
                            if (StringUtil.isNotEmpty(bizInsureInfoBean.getEndDate()))
                                insbPolicyitem.setEnddate(ModelUtil.conbertStringToNyrDate(bizInsureInfoBean.getEndDate()));
                            insbPolicyitemDao.updateById(insbPolicyitem);
                        }
                        addBizCarkindprice(taskId, prvId, bizInsureInfoBean);
                    } else {
                        INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
                        insbPolicyitem.setTaskid(taskId);
                        insbPolicyitem.setRisktype("0");
                        insbPolicyitem.setInscomcode(prvId);
                        insbPolicyitemDao.deleteByObj(insbPolicyitem);
                    }
                }
            }
        }

        //增加供应商
        if (addPrvIds.size() > 0) {
            for (String prvId : addPrvIds) {
                INSBCarinfohis insbCarinfohis = new INSBCarinfohis();
                insbCarinfohis.setTaskid(taskId);
                insbCarinfohis.setInscomcode(prvId);
                insbCarinfohis.setCarlicenseno(carInfoBean.getCarLicenseNo());
                precessCarinfohis(taskId, prvId, insbCarinfohis, insbCarownerPerson, carInfoBean, false);

                INSBCarmodelinfohis insbCarmodelinfohis = new INSBCarmodelinfohis();
                insbCarmodelinfohis.setCarinfoid(insbCarinfo.getId());
                insbCarmodelinfohis.setInscomcode(prvId);
                insbCarmodelinfohis.setStandardfullname(insbCarinfo.getStandardfullname());

                processCarmodelinfohis(taskId, prvId, insbCarmodelinfohis, carInfoBean, insbCarinfo.getId(), false);

                addPersonHis(quoteBean, personMap, taskId, prvId);
            }
            
            InsureInfoBean insureInfoBean = quoteBean.getInsureInfo();
            List<INSBPolicyitem> insbPolicyitems = null;
            List<INSBCarkindprice> insbCarkindprices = null;
            if (insureInfoBean == null) {
                String refPrvId = null;
                if (delPrvIds.size() > 0)
                    refPrvId = delPrvIds.get(0);
                else if (modPrvIds.size() > 0)
                    refPrvId = modPrvIds.get(0);
                if (refPrvId != null) {
                    INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
                    insbPolicyitem.setTaskid(taskId);
                    insbPolicyitem.setInscomcode(refPrvId);
                    insbPolicyitems = insbPolicyitemDao.selectList(insbPolicyitem);

                    INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
                    insbCarkindprice.setTaskid(taskId);
                    insbCarkindprice.setInscomcode(refPrvId);
                    insbCarkindprices = insbCarkindpriceDao.selectList(insbCarkindprice);
                }
            }

            List<INSCDept> deptList = inscDeptDao.selectAllByComcodes(deptcodes);

            // 增加险种
            for (String prvId : addPrvIds) {
                String agreementid = insbChannelagreementService.getAgreementByArea(quoteBean.getChannelId(), prvId, insbQuotetotalinfo.getInscitycode());
                INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
                insbQuoteinfo.setInscomcode(prvId);
                insbQuoteinfo.setCreatetime(new Date());
                insbQuoteinfo.setModifytime(new Date());
                insbQuoteinfo.setOperator("渠道");
                insbQuoteinfo.setWorkflowinstanceid("");
                insbQuoteinfo.setInsprovincecode(insbQuotetotalinfo.getInsprovincecode());
                insbQuoteinfo.setInsprovincename(insbQuotetotalinfo.getInsprovincename());
                insbQuoteinfo.setInscitycode(insbQuotetotalinfo.getInscitycode());
                insbQuoteinfo.setInscityname(insbQuotetotalinfo.getInscityname());
                insbQuoteinfo.setOwnername(insbCarinfo.getOwnername());
                insbQuoteinfo.setQuotetotalinfoid(insbQuotetotalinfo.getId());
                
                String deptCode = map.get(prvId);
                if (StringUtil.isEmpty(deptCode)) {
                    throw new Exception("供应商" + prvId + "配置错误");
                }
                insbQuoteinfo.setDeptcode(deptCode);

                for (INSCDept dept : deptList) {
                    if (dept.getId().equals(deptCode)) {
                        platformInnercode = inscDeptService.getPlatformInnercode(dept.getDeptinnercode());
                        if (platformInnercode != null) {
                            insbQuoteinfo.setPlatforminnercode(Integer.parseInt(platformInnercode));
                        }
                    }
                }
                
                insbQuoteinfo.setBuybusitype("01");
                insbQuoteinfo.setAgreementid(agreementid);
                insbQuoteinfoDao.insert(insbQuoteinfo);

                if (insureInfoBean != null) {
                    // 交强险
                    BaseInsureInfoBean efcInsureInfoBean = insureInfoBean.getEfcInsureInfo();
                    if (efcInsureInfoBean != null) {
                        addPolicyitem(taskId, prvId, efcInsureInfoBean, insbCarinfo.getId(), map, "1");
                        addEfcCarkindprice(taskId, prvId);
                    }
                    BaseInsureInfoBean taxInsureInfoBean = insureInfoBean.getTaxInsureInfo();
                    if (taxInsureInfoBean != null && "Y".equals(taxInsureInfoBean.getIsPaymentTax())) {
                        addTaxCarkindprice(taskId, prvId);
                    }

                    // 商业险
                    BizInsureInfoBean bizInsureInfoBean = insureInfoBean.getBizInsureInfo();
                    if (bizInsureInfoBean != null) {
                        addPolicyitem(taskId, prvId, bizInsureInfoBean, insbCarinfo.getId(), map, "0");
                        addBizCarkindprice(taskId, prvId, bizInsureInfoBean);
                    }
                } else {
                    if (insbPolicyitems != null && insbPolicyitems.size() > 0)
                        for (INSBPolicyitem insbPolicyitem : insbPolicyitems) {
                            insbPolicyitem.setId(null);
                            insbPolicyitem.setInscomcode(prvId);
                            insbPolicyitemDao.insert(insbPolicyitem);
                        }
                    if (insbCarkindprices != null && insbCarkindprices.size() > 0)
                        for (INSBCarkindprice insbCarkindprice : insbCarkindprices) {
                            insbCarkindprice.setId(null);
                            insbCarkindprice.setInscomcode(prvId);
                            insbCarkindpriceDao.insert(insbCarkindprice);
                        }
                }
                
                saveCarmodelinfoToData(taskId, prvId);
            }
        }

        if (delPrvIds.size() > 0) { 
            for (String prvId : delPrvIds) {
            	//删除供应商相关信息
            	delPrvInfo(taskId, prvId, insbQuotetotalinfo);
            }
        }

        //配送地址
        DeliveryBean deliveryBean = quoteBean.getDelivery();
        if (deliveryBean != null) {
        	handleDeliveryAddress(taskId, deliveryBean, modPrvIds);
        }

        //更新备注信息
        String remark = quoteBean.getRemark();
        if (!StringUtil.isEmpty(remark)) {
            List<String> prvIds = new ArrayList<String>();
            prvIds.addAll(modPrvIds);
            prvIds.addAll(addPrvIds);
            updateRemark(taskId, insbQuotetotalinfo.getId(), remark, insbQuotetotalinfo.getOperator(), null, prvIds);
        }
        saveInvoiceInfo(quoteBean, modPrvIds); //保存发票信息
        
        procInsureSupplyInfo(quoteBean);
        copySaveSupplyInfo(quoteBean); 
        
        result.setRespCode("00");
        result.setErrorMsg("信息修改成功");
        return result;
    }
    
    //删除供应商相关信息
    private void delPrvInfo(String taskId, String prvId, INSBQuotetotalinfo insbQuotetotalinfo) {
    	INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
        insbQuoteinfo.setQuotetotalinfoid(insbQuotetotalinfo.getId());
        insbQuoteinfo.setInscomcode(prvId);
        insbQuoteinfoDao.deleteByObj(insbQuoteinfo);

        INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
        insbPolicyitem.setTaskid(taskId);
        insbPolicyitem.setInscomcode(prvId);
        insbPolicyitemDao.deleteByObj(insbPolicyitem);

        INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
        insbCarkindprice.setTaskid(taskId);
        insbCarkindprice.setInscomcode(prvId);
        insbCarkindpriceDao.deleteByObj(insbCarkindprice);

        INSBApplicanthis insbApplicanthis = new INSBApplicanthis();
        insbApplicanthis.setTaskid(taskId);
        insbApplicanthis.setInscomcode(prvId);
        insbApplicanthisDao.deleteByObj(insbApplicanthis);

        INSBInsuredhis insbInsuredhis = new INSBInsuredhis();
        insbInsuredhis.setTaskid(taskId);
        insbInsuredhis.setInscomcode(prvId);
        insbInsuredhisDao.deleteByObj(insbInsuredhis);

        INSBLegalrightclaimhis insbLegalrightclaimhis = new INSBLegalrightclaimhis();
        insbLegalrightclaimhis.setTaskid(taskId);
        insbLegalrightclaimhis.setInscomcode(prvId);
        insbLegalrightclaimhisDao.deleteByObj(insbLegalrightclaimhis);
        
        INSBCarconfig carconfig = new INSBCarconfig();
		carconfig.setTaskid(taskId);
		carconfig.setNoti(prvId);
		List<INSBCarconfig> carconfigs = insbCarconfigDao.selectList(carconfig);
		for(INSBCarconfig insbCarconfig : carconfigs){
			insbCarconfigDao.deleteById(insbCarconfig.getId());
		}
    }
    
    //保存发票信息
    private void saveInvoiceInfo(QuoteBean quoteBean, List<String> modPrvIds) {
    	InvoiceInfoBean invoiceInfo = quoteBean.getInvoiceInfo();
		if ( invoiceInfo == null || modPrvIds == null || modPrvIds.isEmpty() ) {
			return;
		}
		String prvId = modPrvIds.get(modPrvIds.size() - 1);

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("taskid", quoteBean.getTaskId());
		paramMap.put("inscomcode", null);
		INSBInvoiceinfo info = insbInvoiceinfoDao.selectByTaskidAndComcode(paramMap);
		INSBInvoiceinfo invoice = new INSBInvoiceinfo();
		if (info == null) { 
			if ( invoiceInfo.getInvoiceType() == 1 || invoiceInfo.getInvoiceType() == 2 ){ //增值税专用发票 或 增值税普通发票(需资料)
				invoice.setAccountnumber(invoiceInfo.getAccountNumber());
				invoice.setBankname(invoiceInfo.getBankName());
				invoice.setCreatetime(new Date());
				//invoice.setNoti(invoiceInfo.getNoti());
				invoice.setEmail(invoiceInfo.getEmail());
				invoice.setIdentifynumber(invoiceInfo.getIdentifyNumber());
				invoice.setInscomcode(prvId);
				invoice.setInvoicetype(invoiceInfo.getInvoiceType());
				invoice.setOperator("渠道");
				invoice.setRegisteraddress(invoiceInfo.getRegisterAddress());
				invoice.setTaskid(quoteBean.getTaskId());
				invoice.setRegisterphone(invoiceInfo.getRegisterPhone());
				insbInvoiceinfoDao.insert(invoice);
			} else if ( invoiceInfo.getInvoiceType() == 0 ) { //增值税普通发票
				invoice.setAccountnumber(null);
				invoice.setBankname(null);
				//invoice.setNoti(invoiceInfo.getNoti());
				invoice.setCreatetime(new Date());
				invoice.setEmail(null);
				invoice.setIdentifynumber(null);
				invoice.setInscomcode(prvId);
				invoice.setInvoicetype(invoiceInfo.getInvoiceType());
				invoice.setOperator("渠道");
				invoice.setRegisteraddress(null);
				invoice.setTaskid(quoteBean.getTaskId());
				invoice.setRegisterphone(null);
				insbInvoiceinfoDao.insert(invoice);
			}
		} else {
			if ( invoiceInfo.getInvoiceType() == 1 || invoiceInfo.getInvoiceType() == 2 ) {		
				invoice.setAccountnumber(invoiceInfo.getAccountNumber());
				invoice.setBankname(invoiceInfo.getBankName());
				invoice.setModifytime(new Date());
				invoice.setEmail(invoiceInfo.getEmail());
				invoice.setIdentifynumber(invoiceInfo.getIdentifyNumber());
				invoice.setInscomcode(prvId);
				invoice.setInvoicetype(invoiceInfo.getInvoiceType());
				//invoice.setNoti(invoiceInfo.getNoti());
				invoice.setOperator("渠道");
				invoice.setRegisteraddress(invoiceInfo.getRegisterAddress());
				invoice.setTaskid(quoteBean.getTaskId());
				invoice.setRegisterphone(invoiceInfo.getRegisterPhone());
				insbInvoiceinfoDao.updateByTaskid(invoice);
			} else if ( invoiceInfo.getInvoiceType() == 0 ) {
				invoice.setAccountnumber(null);
				invoice.setBankname(null);
				invoice.setModifytime(new Date());
				//invoice.setNoti(invoiceInfo.getNoti());
				invoice.setEmail(null);
				invoice.setIdentifynumber(null);
				invoice.setInscomcode(prvId);
				invoice.setInvoicetype(invoiceInfo.getInvoiceType());
				invoice.setOperator("渠道");
				invoice.setRegisteraddress(null);
				invoice.setTaskid(quoteBean.getTaskId());
				invoice.setRegisterphone(null);
				insbInvoiceinfoDao.updateByTaskid(invoice);
			}
		}
    	
    }
    
    //处理配送地址
    private void handleDeliveryAddress(String taskId, DeliveryBean deliveryBean, List<String> modPrvIds) {
    	String addressId = saveOrUpdateDeliveryAddress(taskId, deliveryBean);
        for (String prvId : modPrvIds) {
            INSBOrderdelivery insbOrderdelivery = new INSBOrderdelivery();
            insbOrderdelivery.setTaskid(taskId);
            insbOrderdelivery.setProviderid(prvId);
            insbOrderdelivery = insbOrderdeliveryDao.selectOne(insbOrderdelivery);
            boolean isInsert = false;
            if (insbOrderdelivery == null) {
                INSBOrder insbOrder = new INSBOrder();
                insbOrder.setTaskid(taskId);
                insbOrder.setPrvid(prvId);
                insbOrder = insbOrderDao.selectOne(insbOrder);
                insbOrderdelivery = new INSBOrderdelivery();
                insbOrderdelivery.setCreatetime(new Date());
                insbOrderdelivery.setOrderid(insbOrder.getId());
                isInsert = true;
            }
            insbOrderdelivery.setRecipientname(deliveryBean.getName()); //姓名
            insbOrderdelivery.setRecipientmobilephone(deliveryBean.getPhone()); //电话号码
            insbOrderdelivery.setOperator("渠道");
            insbOrderdelivery.setTaskid(taskId);
            insbOrderdelivery.setProviderid(prvId);
            String deliveryType = deliveryBean.getDeliveryType(); //配送方式
            insbOrderdelivery.setDelivetype(StringUtil.isNotEmpty(deliveryType) ? deliveryType : "1");

            if ("1".equals(deliveryType)) { //0-自取, 1-快递
                insbOrderdelivery.setRecipientprovince(deliveryBean.getProvince());
                insbOrderdelivery.setRecipientcity(deliveryBean.getCity());
                insbOrderdelivery.setRecipientarea(deliveryBean.getArea());
                insbOrderdelivery.setRecipientaddress(deliveryBean.getAddress());
                insbOrderdelivery.setReceiveday(deliveryBean.getReceiveDay());
                insbOrderdelivery.setReceivetime(deliveryBean.getReceiveTime());
                insbOrderdelivery.setIsinvoice(deliveryBean.getIsInvoice());
                insbOrderdelivery.setInvoicetitle(deliveryBean.getInvoiceTitle());
                insbOrderdelivery.setNoti(deliveryBean.getNoti());
                insbOrderdelivery.setIsfreightcollect(deliveryBean.getIsFreightCollect());
                insbOrderdelivery.setLogisticscompany(deliveryBean.getExpressCompanyId()); //物流公司
                insbOrderdelivery.setDeliveryaddressid(addressId);
                String zip = deliveryBean.getZip();
                insbOrderdelivery.setZip(StringUtil.isEmpty(zip) ? "510000" : zip);
            }
            if (isInsert) {
                insbOrderdeliveryDao.insert(insbOrderdelivery);
            } else {
            	insbOrderdelivery.setModifytime(new Date());
                insbOrderdeliveryDao.updateById(insbOrderdelivery);
            }
        }
    }

    //更新备注信息
    private void updateRemark(String taskId, String quoteTotalId, String remark, String operator, String remarkCode, List<String> prvIds) {
        boolean hasSubFlow = false;
        for (String prvId : prvIds) {
            if (updateRemarkSub(quoteTotalId, remark, operator, remarkCode, prvId)) {
                hasSubFlow = true;
            }
        }

        if (!hasSubFlow) { //没有子流程，更新主流程备注信息
            updateRemarkMain(taskId, remark, operator, remarkCode);
        }
    }

    //更新主流程备注信息
    private void updateRemarkMain(String taskId, String remark, String operator, String remarkCode) {
        INSBWorkflowmaintrack insbWorkflowmaintrack = new INSBWorkflowmaintrack();
        insbWorkflowmaintrack.setInstanceid(taskId);
        List<INSBWorkflowmaintrack> insbWorkflowmaintracks = insbWorkflowmaintrackDao.selectList(insbWorkflowmaintrack);
        if (null != insbWorkflowmaintracks && insbWorkflowmaintracks.size() > 0) {
            INSBWorkflowmaintrack workflowmaintrack = insbWorkflowmaintracks.get(0);

            INSBUsercomment insbUsercomment = new INSBUsercomment();
            if (workflowmaintrack == null || StringUtil.isEmpty(workflowmaintrack.getId())) {
                insbUsercomment = null;
            } else {
                insbUsercomment.setTrackid(workflowmaintrack.getId());//任务轨迹id
                insbUsercomment.setTracktype(1);//1：主流程 2：子流程
                insbUsercomment = insbUsercommentDao.selectOne(insbUsercomment);
            }

            if (insbUsercomment != null) {
                insbUsercomment.setCommentcontent(remark);//备注信息
                insbUsercomment.setModifytime(new Date());
                insbUsercommentDao.updateById(insbUsercomment);
            } else {
                insbUsercomment = new INSBUsercomment();
                insbUsercomment.setOperator(operator);
                insbUsercomment.setCreatetime(new Date());
                insbUsercomment.setTrackid(workflowmaintrack.getId());
                insbUsercomment.setTracktype(1);
                insbUsercomment.setCommentcontent(remark);
                //insbUsercomment.setCommentcontenttype(StringUtil.isEmpty(remarkCode) ? 0 : Integer.parseInt(remarkCode));
                //insbUsercomment.setCommenttype(1);
                insbUsercommentDao.insert(insbUsercomment);
            }
        }
    }

    //更新子流程备注信息
    private boolean updateRemarkSub(String quoteTotalId, String remark, String operator, String remarkCode, String inscomcode) {
        INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
        quoteinfo.setQuotetotalinfoid(quoteTotalId);
        quoteinfo.setInscomcode(inscomcode);
        quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
        LogUtil.info("updateRemarkSub-quoteinfo:" + quoteinfo + " " + quoteTotalId + "-" + inscomcode); 
        if (quoteinfo == null) return false;

        String workflowinstanceid = quoteinfo.getWorkflowinstanceid();
        if (StringUtil.isEmpty(workflowinstanceid)) return false;
        INSBWorkflowsub workflowsub = new INSBWorkflowsub();
        workflowsub.setInstanceid(workflowinstanceid);
        workflowsub = insbWorkflowsubDao.selectOne(workflowsub);
        LogUtil.info("updateRemarkSub-workflowsub:" + workflowsub); 
        if (workflowsub == null) return false;

        INSBWorkflowsubtrack subtrack = new INSBWorkflowsubtrack();
        subtrack.setInstanceid(workflowsub.getInstanceid());
        subtrack.setTaskcode(workflowsub.getTaskcode());
        subtrack = insbWorkflowsubtrackDao.selectOne(subtrack);//子流程轨迹信息
        LogUtil.info("updateRemarkSub-subtrack:" + subtrack); 
        if (subtrack == null) return false;
        INSBUsercomment userComment = new INSBUsercomment();
        if (StringUtil.isEmpty(subtrack.getId())) {
            userComment = null;
        } else {
            userComment.setTrackid(subtrack.getId());//任务轨迹id
            userComment.setTracktype(2);//1：主流程 2：子流程
            userComment = insbUsercommentDao.selectOne(userComment);
        }

        if (userComment != null) {
            userComment.setCommentcontent(remark);//备注信息
            userComment.setModifytime(new Date());
            insbUsercommentDao.updateById(userComment);
        } else {
            userComment = new INSBUsercomment();
            userComment.setTrackid(subtrack.getId());//任务轨迹id
            userComment.setTracktype(2);//1：主流程 2：子流程
            userComment.setCommentcontent(remark);//备注信息
            //userComment.setCommentcontenttype(1);//备注内容类型
            //userComment.setCommenttype(1);//备注类型
            userComment.setCreatetime(new Date());
            userComment.setOperator(operator);
            insbUsercommentDao.insert(userComment);//添加用户备注信息
        }

        return true;
    }

    /** 
      * 匹配CarInfoBean，优先级顺序：接口入参  > 根据vehicleId调cif查询的  > 数据库里存的INSBCarinfo和INSBCarmodelinfo
      * INSBCarinfo和INSBCarmodelinfo关联的是taskid，INSBCarinfohis和INSBCarmodelinfohis关联的是taskid和prvid
      * updatetask时CarInfoBean不要求传全部，要匹配全CarInfoBean的字段以此来操作INSBCarinfohis和INSBCarmodelinfohis
      * insbCarinfo不为null时说明是从updatetask接口来，createTaskB时还没有insbCarinfo 
      */
    private CarInfoBean packetCarInfoBean(CarInfoBean carInfoBean, INSBCarinfo insbCarinfo) throws Exception {
        if (carInfoBean == null) { 
            carInfoBean = new CarInfoBean();
        }
        
        //渠道接口车辆信息CarInfoBean的price对应的是INSBCarmodelinfo的policycarprice，是用户手写的投保车价
        carInfoBean.setPolicycarprice(carInfoBean.getPrice());
        carInfoBean.setPrice(null); //忽略接口传进来的，price字段是根据车型库查出来的
        carInfoBean.setVehicleName(null); //忽略接口传进来的，vehicleName字段是根据车型库查出来的
        
        if (carInfoBean.getVehicleId() != null) { //传了车型id调车型查询接口
            CarModelInfoBean carModelInfoBean = getOneCarModel(carInfoBean.getVehicleId());
            if (StringUtil.isEmpty(carModelInfoBean.getVehicleId())) {
            	throw new Exception("查询车型失败，请重试[" + carInfoBean.getVehicleId() + "]");
            }
            mergeCarInfo(carInfoBean, carModelInfoBean);            
        }
        
        if (insbCarinfo != null) {
            if (carInfoBean.getIsNew() == null) {               
                if ("1".equals(insbCarinfo.getIsNew())) {
                    carInfoBean.setIsNew("Y");
                } else {
                	carInfoBean.setIsNew("N");
                }
            }
            if (carInfoBean.getCarLicenseNo() == null)
                carInfoBean.setCarLicenseNo(insbCarinfo.getCarlicenseno());
            if (carInfoBean.getVinCode() == null)
                carInfoBean.setVinCode(insbCarinfo.getVincode());
            if (carInfoBean.getEngineNo() == null)
                carInfoBean.setEngineNo(insbCarinfo.getEngineno());
            if (carInfoBean.getCarProperty() == null)
                carInfoBean.setCarProperty(insbCarinfo.getCarproperty());
            if (carInfoBean.getProperty() == null)
                carInfoBean.setProperty(insbCarinfo.getProperty());
            if (carInfoBean.getIsTransferCar() == null) {
                if ("1".equals(insbCarinfo.getIsTransfercar())) {
                    carInfoBean.setIsTransferCar("Y");
                    carInfoBean.setTransferDate(ModelUtil.conbertToString(insbCarinfo.getTransferdate()));
                } else {
                	carInfoBean.setIsTransferCar("N");
                }
            }
            if (StringUtil.isEmpty(carInfoBean.getRegistDate()))
                carInfoBean.setRegistDate(ModelUtil.conbertToString(insbCarinfo.getRegistdate()));
            if (StringUtil.isEmpty(carInfoBean.getVehicleName())) 
            	carInfoBean.setVehicleName(insbCarinfo.getStandardfullname());
            if (carInfoBean.getPurpose() == null)
                carInfoBean.setPurpose(String.valueOf(insbCarinfo.getCarVehicularApplications()));
            if (StringUtil.isEmpty(carInfoBean.getDrivingArea()))
            	carInfoBean.setDrivingArea(insbCarinfo.getDrivingarea());
            
            //以下是车型信息
            INSBCarmodelinfo insbCarmodelinfo = insbCarmodelinfoDao.selectByCarinfoId(insbCarinfo.getId());
            if (carInfoBean.getAnalogyPrice() == null)
                carInfoBean.setAnalogyPrice(insbCarmodelinfo.getAnalogyprice().toString());
            if (carInfoBean.getAnalogyTaxPrice() == null)
                carInfoBean.setAnalogyTaxPrice(String.valueOf(insbCarmodelinfo.getAnalogytaxprice()));
            if (carInfoBean.getBrandName() == null)
            	carInfoBean.setBrandName(insbCarmodelinfo.getBrandname());
            if (carInfoBean.getDisplacement() == null)
            	carInfoBean.setDisplacement(String.valueOf(insbCarmodelinfo.getDisplacement()));
            if (carInfoBean.getFullWeight() == null)
            	carInfoBean.setFullWeight(String.valueOf(insbCarmodelinfo.getFullweight()));
            if (carInfoBean.getModelLoads() == null)
                carInfoBean.setModelLoads(String.valueOf(insbCarmodelinfo.getLoads()));
            if (carInfoBean.getPrice() == null)
            	carInfoBean.setPrice(String.valueOf(insbCarmodelinfo.getPrice()));
            if (carInfoBean.getSeat() == null)
            	carInfoBean.setSeat(String.valueOf(insbCarmodelinfo.getSeat()));
            if (carInfoBean.getTaxPrice() == null)
                carInfoBean.setTaxPrice(String.valueOf(insbCarmodelinfo.getTaxprice()));
            if (carInfoBean.getFamilyName() == null)
                carInfoBean.setFamilyName(insbCarmodelinfo.getFamilyname());
            if (carInfoBean.getFactoryName() == null)
                carInfoBean.setFactoryName(insbCarmodelinfo.getFactoryname());
            if (carInfoBean.getGearbox() == null)
                carInfoBean.setGearbox(insbCarmodelinfo.getGearbox());
            if (StringUtil.isEmpty(carInfoBean.getVehicleName()))
            	carInfoBean.setVehicleName(insbCarmodelinfo.getStandardfullname());
            if (carInfoBean.getYearStyle() == null)
                carInfoBean.setYearStyle(insbCarmodelinfo.getListedyear());
            if (carInfoBean.getVehicleId() == null) {
                carInfoBean.setVehicleId(insbCarmodelinfo.getVehicleid());
            } 
            Double policycarprice = insbCarmodelinfo.getPolicycarprice();
            if ( carInfoBean.getPolicycarprice() == null && policycarprice != null )
            	carInfoBean.setPolicycarprice(String.valueOf(policycarprice));
            //if ( StringUtil.isEmpty(carInfoBean.getCarPrice()) )
            //	carInfoBean.setCarPrice(insbCarmodelinfo.getCarprice());
        }
        
        if ( carInfoBean.getPolicycarprice() == null ) {
        	carInfoBean.setCarPrice("0"); //不指定车价            
        } else {
        	String strPolicycarprice = carInfoBean.getPolicycarprice().trim();
        	if ( "".equals(strPolicycarprice) || Double.parseDouble(strPolicycarprice) == 0 ) {
        		carInfoBean.setCarPrice("0"); //不指定车价
        		carInfoBean.setPolicycarprice(null);
        	} else {
        		carInfoBean.setCarPrice("2"); //指定车价
        	} 
        }

        return carInfoBean;
    }

    //更新InsbCarinfo和INSBCarmodelinfo
    private INSBCarinfo updateInsbCarinfo(INSBCarinfo insbCarinfo, CarInfoBean carInfoBean) {        
        if (StringUtil.isNotEmpty(carInfoBean.getIsNew())) {
        	if ("Y".equals(carInfoBean.getIsNew())) {
        		insbCarinfo.setIsNew("1");
        	} else {
        		insbCarinfo.setIsNew("0");
        	}
        }
        if (StringUtil.isNotEmpty(carInfoBean.getCarLicenseNo()))
            insbCarinfo.setCarlicenseno(carInfoBean.getCarLicenseNo());
        if (StringUtil.isNotEmpty(carInfoBean.getVinCode()))
            insbCarinfo.setVincode(carInfoBean.getVinCode());
        if (StringUtil.isNotEmpty(carInfoBean.getEngineNo()))
            insbCarinfo.setEngineno(carInfoBean.getEngineNo());
        if (StringUtil.isNotEmpty(carInfoBean.getCarProperty()))
            insbCarinfo.setCarproperty(carInfoBean.getCarProperty());
        if (StringUtil.isNotEmpty(carInfoBean.getProperty()))
            insbCarinfo.setProperty(carInfoBean.getProperty());
        if (StringUtil.isNotEmpty(carInfoBean.getIsTransferCar())) {
            if ("Y".equals(carInfoBean.getIsTransferCar())) {
                insbCarinfo.setIsTransfercar("1");
                insbCarinfo.setTransferdate(ModelUtil.conbertStringToNyrDate(carInfoBean.getTransferDate()));
            } else {
            	insbCarinfo.setIsTransfercar("0");
            }
        }
        if (StringUtil.isNotEmpty(carInfoBean.getRegistDate()))
            insbCarinfo.setRegistdate(ModelUtil.conbertStringToNyrDate(carInfoBean.getRegistDate()));
        if (StringUtil.isNotEmpty(carInfoBean.getVehicleName())) {
            insbCarinfo.setStandardfullname(carInfoBean.getVehicleName());
        }
        if (StringUtil.isNotEmpty(carInfoBean.getPurpose()))
            insbCarinfo.setCarVehicularApplications(Integer.parseInt(carInfoBean.getPurpose()));
        if(StringUtil.isNotEmpty(carInfoBean.getDrivingArea()))
            insbCarinfo.setDrivingarea(carInfoBean.getDrivingArea());
        insbCarinfoDao.updateById(insbCarinfo);
        LogUtil.info("INSBCarinfo|报表数据埋点|"+JSONObject.fromObject(insbCarinfo).toString());

        INSBCarmodelinfo insbCarmodelinfo = insbCarmodelinfoDao.selectByCarinfoId(insbCarinfo.getId());
        if (StringUtil.isNotEmpty(carInfoBean.getAnalogyPrice()))
            insbCarmodelinfo.setAnalogyprice(Double.parseDouble(carInfoBean.getAnalogyPrice()));
        if (StringUtil.isNotEmpty(carInfoBean.getAnalogyTaxPrice()))
            insbCarmodelinfo.setAnalogytaxprice(Double.parseDouble(carInfoBean.getAnalogyTaxPrice()));
        if (StringUtil.isNotEmpty(carInfoBean.getBrandName()))
            insbCarmodelinfo.setBrandname(carInfoBean.getBrandName());
        if (StringUtil.isNotEmpty(carInfoBean.getDisplacement()))
            insbCarmodelinfo.setDisplacement(Double.parseDouble(carInfoBean.getDisplacement()));
        if (StringUtil.isNotEmpty(carInfoBean.getFullWeight()))
            insbCarmodelinfo.setFullweight(Double.parseDouble(carInfoBean.getFullWeight()));
        if (StringUtil.isNotEmpty(carInfoBean.getModelLoads())) {
            insbCarmodelinfo.setLoads(Double.parseDouble(carInfoBean.getModelLoads()));
            insbCarmodelinfo.setUnwrtweight(Double.parseDouble(carInfoBean.getModelLoads()));
        }
        //if (StringUtil.isNotEmpty(carInfoBean.getPolicycarprice()))
        //    insbCarmodelinfo.setPolicycarprice(Double.parseDouble(carInfoBean.getPolicycarprice()));
        if (StringUtil.isNotEmpty(carInfoBean.getPrice()))
            insbCarmodelinfo.setPrice(Double.parseDouble(carInfoBean.getPrice()));
        if (StringUtil.isNotEmpty(carInfoBean.getSeat()))
            insbCarmodelinfo.setSeat(Integer.parseInt(carInfoBean.getSeat()));
        if (StringUtil.isNotEmpty(carInfoBean.getTaxPrice()))
            insbCarmodelinfo.setTaxprice(Double.parseDouble(carInfoBean.getTaxPrice()));
        if (StringUtil.isNotEmpty(carInfoBean.getFamilyName()))
            insbCarmodelinfo.setFamilyname(carInfoBean.getFamilyName());
        if (StringUtil.isNotEmpty(carInfoBean.getFactoryName()))
            insbCarmodelinfo.setFactoryname(carInfoBean.getFactoryName());
        if (StringUtil.isNotEmpty(carInfoBean.getGearbox()))
            insbCarmodelinfo.setGearbox(carInfoBean.getGearbox());
        if (StringUtil.isNotEmpty(carInfoBean.getVehicleName())) {
            insbCarmodelinfo.setStandardfullname(carInfoBean.getVehicleName());
            insbCarmodelinfo.setStandardname(carInfoBean.getVehicleName());
        }
        if (StringUtil.isNotEmpty(carInfoBean.getYearStyle()))
            insbCarmodelinfo.setListedyear(carInfoBean.getYearStyle());
        if (StringUtil.isNotEmpty(carInfoBean.getVehicleId())) {
            insbCarmodelinfo.setVehicleid(carInfoBean.getVehicleId());
        }
        insbCarmodelinfo.setCarprice(carInfoBean.getCarPrice());
        if ( "0".equals(carInfoBean.getCarPrice()) ) { //不指定车价
        	insbCarmodelinfo.setPolicycarprice(null);
        } else if ( "2".equals(carInfoBean.getCarPrice()) ) { //指定车价
        	insbCarmodelinfo.setPolicycarprice(Double.parseDouble(carInfoBean.getPolicycarprice()));
        }
        
        insbCarmodelinfoDao.updateById(insbCarmodelinfo);
        
        return insbCarinfo;
    }

    private void addTaxCarkindprice(String taskId, String prvId) {
        INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
        insbCarkindprice.setInskindtype("3");
        insbCarkindprice.setOperator("渠道");
        insbCarkindprice.setTaskid(taskId);
        insbCarkindprice.setCreatetime(new Date());
        insbCarkindprice.setInscomcode(prvId);
        insbCarkindprice.setInskindcode("VehicleTax");
        insbCarkindprice.setRiskname("车船税");
        insbCarkindprice.setNotdeductible("0");
        insbCarkindprice.setPreriskkind("VehicleCompulsoryIns");
        insbCarkindprice.setSelecteditem("代缴");
        insbCarkindprice.setAmount(new Double(1));
        insbCarkindpriceDao.insert(insbCarkindprice);
        
        INSBCarconfig insbCarconfig = new INSBCarconfig();
		insbCarconfig.setOperator(insbCarkindprice.getOperator());
		insbCarconfig.setCreatetime(insbCarkindprice.getCreatetime());
		insbCarconfig.setTaskid(insbCarkindprice.getTaskid());
		insbCarconfig.setInskindtype(insbCarkindprice.getInskindtype());
		insbCarconfig.setNotdeductible(insbCarkindprice.getNotdeductible());
		insbCarconfig.setInskindcode(insbCarkindprice.getInskindcode());
		insbCarconfig.setAmount(insbCarkindprice.getAmount().toString());
		insbCarconfig.setSelecteditem(insbCarkindprice.getSelecteditem());
		insbCarconfig.setPreriskkind(insbCarkindprice.getPreriskkind());
		insbCarconfig.setPlankey(insbCarkindprice.getPlankey());
		insbCarconfig.setNoti(prvId); //放到备注中便于查数据
		insbCarconfigDao.insert(insbCarconfig);
    }

    private void addEfcCarkindprice(String taskId, String prvId) {
        INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
        insbCarkindprice.setInskindtype("2");
        insbCarkindprice.setOperator("渠道");
        insbCarkindprice.setTaskid(taskId);
        insbCarkindprice.setCreatetime(new Date());
        insbCarkindprice.setModifytime(new Date());
        insbCarkindprice.setInscomcode(prvId);
        insbCarkindprice.setInskindcode("VehicleCompulsoryIns");
        insbCarkindprice.setRiskname("交强险");
        insbCarkindprice.setNotdeductible("0");
        insbCarkindprice.setPreriskkind("");
        insbCarkindprice.setSelecteditem("购买");
        insbCarkindprice.setAmount(new Double(1));
        insbCarkindpriceDao.insert(insbCarkindprice);
        
        INSBCarconfig insbCarconfig = new INSBCarconfig();
		insbCarconfig.setOperator(insbCarkindprice.getOperator());
		insbCarconfig.setCreatetime(insbCarkindprice.getCreatetime());
		insbCarconfig.setTaskid(insbCarkindprice.getTaskid());
		insbCarconfig.setInskindtype(insbCarkindprice.getInskindtype());
		insbCarconfig.setNotdeductible(insbCarkindprice.getNotdeductible());
		insbCarconfig.setInskindcode(insbCarkindprice.getInskindcode());
		insbCarconfig.setAmount(insbCarkindprice.getAmount().toString());
		insbCarconfig.setSelecteditem(insbCarkindprice.getSelecteditem());
		insbCarconfig.setPreriskkind(insbCarkindprice.getPreriskkind());
		insbCarconfig.setPlankey(insbCarkindprice.getPlankey());
		insbCarconfig.setNoti(prvId); //放到备注中便于查数据
		insbCarconfigDao.insert(insbCarconfig);
    }

    private void precessCarinfohis(String taskId, String prvId, INSBCarinfohis insbCarinfohis,
                                   INSBPerson insbCarownerPerson, CarInfoBean carInfoBean, boolean isUpdate) {
        if (insbCarownerPerson != null) {
            insbCarinfohis.setOwnername(insbCarownerPerson.getName());
            insbCarinfohis.setOwner(insbCarownerPerson.getId());
        } else {
            List<INSBCarinfo> insbCarinfos = insbCarinfoDao.queryBytaskid(taskId);
            if (insbCarinfos.size() > 0) {
                insbCarinfohis.setOwnername(insbCarinfos.get(0).getOwnername());
                insbCarinfohis.setOwner(insbCarinfos.get(0).getOwner());
            }
        }
        if (carInfoBean.getIsNew() != null && "Y".equals(carInfoBean.getIsNew())) {
            insbCarinfohis.setIsNew("1");
            insbCarinfohis.setCarlicenseno("新车未上牌");
        } else {
            insbCarinfohis.setIsNew("0");
            insbCarinfohis.setCarlicenseno(carInfoBean.getCarLicenseNo());
        }
        if (carInfoBean.getVinCode() != null)
            insbCarinfohis.setVincode(carInfoBean.getVinCode());
        if (carInfoBean.getEngineNo() != null)
            insbCarinfohis.setEngineno(carInfoBean.getEngineNo());
        if (carInfoBean.getCarProperty() != null)
            insbCarinfohis.setCarproperty(carInfoBean.getCarProperty());
        if (carInfoBean.getProperty() != null)
            insbCarinfohis.setProperty(carInfoBean.getProperty());
        if (carInfoBean.getIsTransferCar() != null) {
            if ("Y".equals(carInfoBean.getIsTransferCar())) {
                insbCarinfohis.setIsTransfercar("1");
                insbCarinfohis.setTransferdate(ModelUtil.conbertStringToNyrDate(carInfoBean.getTransferDate()));
            } else {
                insbCarinfohis.setIsTransfercar("0");
            }
        }

        if (carInfoBean.getRegistDate() != null)
            insbCarinfohis.setRegistdate(ModelUtil.conbertStringToNyrDate(carInfoBean.getRegistDate()));

        if (carInfoBean.getPurpose() != null)
            insbCarinfohis.setCarVehicularApplications(Integer.valueOf(carInfoBean.getPurpose()));
        if (carInfoBean.getDrivingArea() != null)
            insbCarinfohis.setDrivingarea(carInfoBean.getDrivingArea());
        insbCarinfohis.setModifytime(new Date());
        if (isUpdate) {
            insbCarinfohisDao.updateById(insbCarinfohis);
        } else {
            insbCarinfohis.setOperator("渠道");
            insbCarinfohis.setCreatetime(new Date());
            insbCarinfohisDao.insert(insbCarinfohis);
        }
    }

    // riskType:1=交强险，0=商业险
    private void addPolicyitem(String taskId, String prvId, BaseInsureInfoBean baseInsureInfoBean, String insbCarinfoId, Map<String, String> map, String riskType) {
        INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
        insbPolicyitem.setTaskid(taskId);
        insbPolicyitem.setRisktype(riskType);
        insbPolicyitem.setInscomcode(prvId);
        if (map.get("insbApplicantPersonId") != null) {
            insbPolicyitem.setApplicantid(map.get("insbApplicantPersonId"));
            insbPolicyitem.setApplicantname(map.get("insbApplicantPersonName"));
        } else {
            INSBPerson applicantPerson = insbPersonDao.selectApplicantPersonByTaskId(taskId);
            insbPolicyitem.setApplicantid(applicantPerson.getId());
            insbPolicyitem.setApplicantname(applicantPerson.getName());
        }
        if (map.get("insbInsuredPersonId") != null) {
            insbPolicyitem.setInsuredid(map.get("insbInsuredPersonId"));
            insbPolicyitem.setInsuredname(map.get("insbInsuredPersonName"));
        } else {
            INSBPerson insuredPerson = insbPersonDao.selectInsuredPersonByTaskId(taskId);
            insbPolicyitem.setInsuredid(insuredPerson.getId());
            insbPolicyitem.setInsuredname(insuredPerson.getName());
        }
        if (map.get("insbCarownerPersonId") != null) {
            insbPolicyitem.setCarownerid(map.get("insbCarownerPersonId"));
            insbPolicyitem.setCarownername(map.get("insbCarownerPersonName"));
        } else {
            INSBPerson carownerPerson = insbPersonDao.selectCarOwnerPersonByTaskId(taskId);
            insbPolicyitem.setCarownerid(carownerPerson.getId());
            insbPolicyitem.setCarownername(carownerPerson.getName());
        }

        insbPolicyitem.setCarinfoid(insbCarinfoId);

        if (StringUtil.isNotEmpty(baseInsureInfoBean.getStartDate()))
            insbPolicyitem.setStartdate(ModelUtil.conbertStringToNyrDate(baseInsureInfoBean.getStartDate()));
        if (StringUtil.isNotEmpty(baseInsureInfoBean.getEndDate()))
            insbPolicyitem.setEnddate(ModelUtil.conbertStringToNyrDate(baseInsureInfoBean.getEndDate()));

        if (map != null) {
            insbPolicyitem.setAgentnum((String) map.get("jobnum"));
        }
        Date nowDate = new Date();
        insbPolicyitem.setOperator("渠道");
        insbPolicyitem.setCreatetime(nowDate);
        insbPolicyitem.setModifytime(nowDate);
        insbPolicyitemDao.insert(insbPolicyitem);
    }

    //保存INSBCarmodelinfohis表信息
    private void processCarmodelinfohis(String taskId, String prvId, INSBCarmodelinfohis insbCarmodelinfohis,
                                        CarInfoBean carInfoBean, String insbCarinfoId, boolean isUpdate) {
        if (StringUtil.isNotEmpty(carInfoBean.getAnalogyPrice()))
            insbCarmodelinfohis.setAnalogyprice(Double.valueOf(carInfoBean.getAnalogyPrice()));
        if (StringUtil.isNotEmpty(carInfoBean.getAnalogyTaxPrice()))
            insbCarmodelinfohis.setAnalogytaxprice(Double.valueOf(carInfoBean.getAnalogyTaxPrice()));
        insbCarmodelinfohis.setBrandname(carInfoBean.getBrandName());
        if (StringUtil.isNotEmpty(carInfoBean.getDisplacement()))
            insbCarmodelinfohis.setDisplacement(Double.valueOf(carInfoBean.getDisplacement()));
        if (StringUtil.isNotEmpty(carInfoBean.getFullWeight()))
            insbCarmodelinfohis.setFullweight(Double.valueOf(carInfoBean.getFullWeight()));
        if (StringUtil.isNotEmpty(carInfoBean.getPrice()))
            insbCarmodelinfohis.setPrice(Double.valueOf(carInfoBean.getPrice()));
        if (StringUtil.isNotEmpty(carInfoBean.getTaxPrice()))
            insbCarmodelinfohis.setTaxprice(Double.valueOf(carInfoBean.getTaxPrice()));
        insbCarmodelinfohis.setVehicleid(carInfoBean.getVehicleId());
        insbCarmodelinfohis.setFamilyname(carInfoBean.getFamilyName());
        insbCarmodelinfohis.setFactoryname(carInfoBean.getFactoryName());
        insbCarmodelinfohis.setGearbox(carInfoBean.getGearbox());
        insbCarmodelinfohis.setStandardfullname(carInfoBean.getVehicleName());
        insbCarmodelinfohis.setStandardname(carInfoBean.getVehicleName());
        insbCarmodelinfohis.setListedyear(carInfoBean.getYearStyle());

        if (StringUtil.isNotEmpty(carInfoBean.getModelLoads())) {
            insbCarmodelinfohis.setLoads(Double.valueOf(carInfoBean.getModelLoads()));
            insbCarmodelinfohis.setUnwrtweight(Double.valueOf(carInfoBean.getModelLoads()));
        }
        if (StringUtil.isNotEmpty(carInfoBean.getSeat()))
            insbCarmodelinfohis.setSeat(Integer.valueOf(carInfoBean.getSeat()));
        
        insbCarmodelinfohis.setModifytime(new Date());
        insbCarmodelinfohis.setCarinfoid(insbCarinfoId);
        //RbCode在匹配车型年款那会查询
        //insbCarmodelinfohis.setJyCode(getRbCode(insbCarmodelinfohis.getVehicleid(), insbCarmodelinfohis.getInscomcode(), taskId));
        //insbCarmodelinfohis.setRbCode(insbCarmodelinfohis.getJyCode());
        insbCarmodelinfohis.setCarprice(carInfoBean.getCarPrice());
        //if ( StringUtil.isNotEmpty(carInfoBean.getPolicycarprice()) )
        //    insbCarmodelinfohis.setPolicycarprice(Double.valueOf(carInfoBean.getPolicycarprice()));
        if ( "0".equals(carInfoBean.getCarPrice()) ) { //不指定车价
        	insbCarmodelinfohis.setPolicycarprice(null);
        } else if ( "2".equals(carInfoBean.getCarPrice()) ) { //指定车价
        	insbCarmodelinfohis.setPolicycarprice(Double.parseDouble(carInfoBean.getPolicycarprice()));
        }
        
        if (isUpdate) {
            insbCarmodelinfohisDao.updateById(insbCarmodelinfohis);
        } else {
            insbCarmodelinfohis.setOperator("渠道");
            insbCarmodelinfohis.setCreatetime(new Date());
            insbCarmodelinfohisDao.insert(insbCarmodelinfohis);
        }        
    }
    
    //车型年款匹配
    private boolean saveCarmodelinfoToData(String taskId, String prvId) throws Exception {
    	LogUtil.info("开始saveCarmodelinfoToDataForChn:taskid=" + taskId + "prvId=" + prvId);
    	boolean modelYearRet = false;   	
		modelYearRet = appInsuredQuoteService.saveCarmodelinfoToData(taskId, prvId, "");       
    	LogUtil.info("结果saveCarmodelinfoToDataForChn:taskid=" + taskId + "prvId=" + prvId + "modelYearRet=" + modelYearRet);
    	
    	//if (!modelYearRet) {
    	//	throw new Exception("车型年款无法匹配，请重新核实");
    	//} 
    	
        return modelYearRet;
    }

    private void addBizCarkindprice(String taskId, String prvId, BizInsureInfoBean bizInsureInfoBean) {
        for (RiskKindBean riskKindBean : bizInsureInfoBean.getRiskKinds()) {
            if ("VehicleTax".equals(riskKindBean.getRiskCode()) || "VehicleCompulsoryIns".equals(riskKindBean.getRiskCode())
                    || riskKindBean.getRiskCode().startsWith("Ncf"))
                continue;
            INSBRiskkindconfig riskkindconfig = insbRiskkindconfigDao.selectKindByKindcode(riskKindBean.getRiskCode());
            INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
            insbCarkindprice.setCreatetime(new Date());
            insbCarkindprice.setOperator("渠道");
            insbCarkindprice.setTaskid(taskId);
            insbCarkindprice.setInscomcode(prvId);
            insbCarkindprice.setInskindcode(riskKindBean.getRiskCode());
            insbCarkindprice.setRiskname(riskkindconfig.getRiskkindname());
            if (StringUtil.isNotEmpty(riskKindBean.getAmount()))
                insbCarkindprice.setAmount(Double.parseDouble(riskKindBean.getAmount()));
            if ("CombustionIns".equals(riskkindconfig.getRiskkindcode()) || "TheftIns".equals(riskkindconfig.getRiskkindcode())) {
                if (Double.parseDouble(riskKindBean.getAmount()) != 1) {
                    riskKindBean.setAmount("2");
                }
            }
            insbCarkindprice.setInskindtype(riskkindconfig.getType());
            insbCarkindprice.setSelecteditem(getSelectedItem(riskKindBean.getAmount(), riskkindconfig));
            INSBCarkindprice ncfCarkindprice = null;
            if ("Y".equals(riskKindBean.getNotDeductible())) {
                insbCarkindprice.setNotdeductible("1");
                INSBRiskkindconfig notRiskkindConfig = new INSBRiskkindconfig();
                notRiskkindConfig.setPrekindcode(riskKindBean.getRiskCode());
                notRiskkindConfig.setType("1");
                notRiskkindConfig = insbRiskkindconfigDao.selectOne(notRiskkindConfig);
                ncfCarkindprice = new INSBCarkindprice();
                ncfCarkindprice.setTaskid(taskId);
                ncfCarkindprice.setCreatetime(new Date());
                ncfCarkindprice.setInscomcode(prvId);
                ncfCarkindprice.setOperator("渠道");
                ncfCarkindprice.setInskindcode(notRiskkindConfig.getRiskkindcode());
                ncfCarkindprice.setRiskname(notRiskkindConfig.getRiskkindname());
                ncfCarkindprice.setAmount(1.0000);
                ncfCarkindprice.setInskindtype("1"); // 不计免赔
                ncfCarkindprice.setNotdeductible("0");
                ncfCarkindprice.setPreriskkind(riskKindBean.getRiskCode());
            } else {
                insbCarkindprice.setNotdeductible("0");
            }
            if (ncfCarkindprice != null && "Y".equals(riskKindBean.getNotDeductible())) {
                insbCarkindpriceDao.insert(ncfCarkindprice);
            }
            insbCarkindpriceDao.insert(insbCarkindprice);
            
            INSBCarconfig insbCarconfig = new INSBCarconfig();
			insbCarconfig.setOperator(insbCarkindprice.getOperator());
			insbCarconfig.setCreatetime(insbCarkindprice.getCreatetime());
			insbCarconfig.setTaskid(insbCarkindprice.getTaskid());
			insbCarconfig.setInskindtype(insbCarkindprice.getInskindtype());
			insbCarconfig.setNotdeductible(insbCarkindprice.getNotdeductible());
			insbCarconfig.setInskindcode(insbCarkindprice.getInskindcode());
			insbCarconfig.setAmount(insbCarkindprice.getAmount().toString());
			insbCarconfig.setSelecteditem(insbCarkindprice.getSelecteditem());
			insbCarconfig.setPreriskkind(insbCarkindprice.getPreriskkind());
			insbCarconfig.setPlankey(insbCarkindprice.getPlankey());
			insbCarconfig.setNoti(prvId); //放到备注中便于查数据
			insbCarconfigDao.insert(insbCarconfig);
        }
    }

    public String saveOrUpdateDeliveryAddress(String taskId, DeliveryBean deliveryBean) {
        INSBCarowneinfo carowner = new INSBCarowneinfo();
        carowner.setTaskid(taskId);
        carowner = insbCarowneinfoDao.selectOne(carowner);
        INSBPerson person = insbPersonDao.selectById(carowner.getPersonid());
        INSBDeliveryaddress address = new INSBDeliveryaddress();
        address.setTaskid(taskId);
        INSBDeliveryaddress deaddress = insbDeliveryaddressDao.selectOne(address);
        String deliveType = deliveryBean.getDeliveryType();
        
        if (null == deaddress) {
            address.setOperator("渠道");
            address.setCreatetime(new Date());
            address.setOwnername(person.getName());
            address.setOwneridcardno(person.getIdcardno());
            address.setRecipientname(deliveryBean.getName());
            address.setNoti(deliveryBean.getNoti());
            
            if ("1".equals(deliveType)) {
                address.setRecipientmobilephone(deliveryBean.getPhone());
                address.setRecipientprovince(deliveryBean.getProvince());
                address.setRecipientcity(deliveryBean.getCity());
                address.setRecipientarea(deliveryBean.getArea());
                address.setRecipientaddress(deliveryBean.getAddress());
                String zip = deliveryBean.getZip();
                address.setZip(StringUtil.isEmpty(zip) ? "510000" : zip);
            }
            address.setDelivetype(StringUtil.isNotEmpty(deliveType) ? deliveType : "1");
            insbDeliveryaddressDao.insert(address);
            return address.getId();
        }
        if ("1".equals(deliveType)) {
            if (StringUtil.isNotEmpty(deliveryBean.getName())) deaddress.setRecipientname(deliveryBean.getName());
            if (StringUtil.isNotEmpty(deliveryBean.getPhone())) deaddress.setRecipientmobilephone(deliveryBean.getPhone());
            if (StringUtil.isNotEmpty(deliveryBean.getProvince())) deaddress.setRecipientprovince(deliveryBean.getProvince());
            if (StringUtil.isNotEmpty(deliveryBean.getCity())) deaddress.setRecipientcity(deliveryBean.getCity());
            if (StringUtil.isNotEmpty(deliveryBean.getArea())) deaddress.setRecipientarea(deliveryBean.getArea());
            if (StringUtil.isNotEmpty(deliveryBean.getAddress())) deaddress.setRecipientaddress(deliveryBean.getAddress());
        }
        deaddress.setNoti(deliveryBean.getNoti());
        deaddress.setDelivetype(StringUtil.isNotEmpty(deliveType) ? deliveType : "1");
        deaddress.setModifytime(new Date());
        insbDeliveryaddressDao.updateById(deaddress);

        return deaddress.getId();
    }
    
    //检查转换发票信息
    private boolean transAndCheckInvoiceInfo(QuoteBean quoteBean) {
    	InvoiceInfoBean invoiceInfoBean = quoteBean.getInvoiceInfo();
    	if (invoiceInfoBean == null || invoiceInfoBean.getInvoiceType() == null) {
    		return true;
    	}
    	
    	if (invoiceInfoBean.getInvoiceType() == 0) {
    		INSBRegion insbRegion = new INSBRegion();
        	insbRegion.setParentid("420000"); //湖北
        	insbRegion.setComcode(quoteBean.getInsureAreaCode()); 
        	if ( insbRegionDao.selectCount(insbRegion) > 0 ) {
        		invoiceInfoBean.setInvoiceType(2); 
        	}
    	} 
    	
    	if (invoiceInfoBean.getInvoiceType() == 1 || invoiceInfoBean.getInvoiceType() == 2) {
    		if ( StringUtil.isEmpty(invoiceInfoBean.getAccountNumber()) || 
    			 StringUtil.isEmpty(invoiceInfoBean.getBankName()) || 
    			 StringUtil.isEmpty(invoiceInfoBean.getEmail()) || 
    			 StringUtil.isEmpty(invoiceInfoBean.getIdentifyNumber()) || 
    			 StringUtil.isEmpty(invoiceInfoBean.getRegisterAddress()) || 
    			 StringUtil.isEmpty(invoiceInfoBean.getRegisterPhone()) ) {
    			return false;
    		}
    	}
    	quoteBean.setInvoiceInfo(invoiceInfoBean); 
    	
    	return true;
    }
    
    //检查转换新车发票价
    private boolean transAndCheckPrice(CarInfoBean carInfoBean) {    
        if ( carInfoBean.getIsNew() == null && carInfoBean.getRegistDate() == null ) {
        	return true;
        }
        
        boolean isNew = false;
        if ("Y".equals(carInfoBean.getIsNew())) {
        	isNew = true;
        } else {
        	if (carInfoBean.getRegistDate() != null) {
        		int days = ModelUtil.daysBetween(ModelUtil.conbertStringToNyrDate(carInfoBean.getRegistDate()), new Date());
        		if (days < 270) isNew = true;
            }
        }
        if (isNew && carInfoBean.getPrice() == null) {
        	return false;
        } 
        
        return true;
    }
    
    //公共入参信息转换和检查
    private boolean transAndCheckImputCommon(QuoteBean quoteBean, StringBuffer msg, String sourceFrom) {
    	CarInfoBean carInfoBean = quoteBean.getCarInfo();
    	if (carInfoBean != null) {
    		String carLicenseNo = carInfoBean.getCarLicenseNo(); //车牌号
    		String isNew = carInfoBean.getIsNew();
    		String vinCode = carInfoBean.getVinCode(); //车架号
    		String engineNo = carInfoBean.getEngineNo(); //发动机号
    		
    		if ("Y".equals(isNew)) {
    			carInfoBean.setCarLicenseNo("新车未上牌");
    		} else {
	    		if (carLicenseNo != null) { 
	    			carLicenseNo = carLicenseNo.toUpperCase();
	    			carInfoBean.setCarLicenseNo(carLicenseNo);
	    			
	    			if (!ParaValidatorUtils.checkCarLicenseNo(carLicenseNo)) {
	    				msg.append("请输入正确的车牌号");
	    				return false;
	    			}
	    		}
    		}
    		
    		if (vinCode != null) {
    			vinCode = vinCode.toUpperCase();
    			carInfoBean.setVinCode(vinCode);
    			
    			if (vinCode.contains("*")) {
    				msg.append("请传完整的车架号");
    				return false;
    			}
    		}
    		if (engineNo != null) {
    			engineNo = engineNo.toUpperCase();
    			carInfoBean.setEngineNo(engineNo);
    			
    			if (engineNo.length()<1) {
    				msg.append("请传完整的发动机号");
    				return false;
    			}
    		}
    		
    		if ( !transAndCheckPrice(carInfoBean) ) {
    			msg.append("新车未上牌或者初登日期小于270天，请传新车发票价");
				return false;
    		}
    		
    		quoteBean.setCarInfo(carInfoBean);
    	}
    	if ("updateTask".equals(sourceFrom)) { 
    		if ( !transAndCheckInvoiceInfo(quoteBean) ) {
    			msg.append("请完整上传发票所需的信息");
				return false;
    		}
    	}
    	if("updateTask".equals(sourceFrom)) { 
    		if(!checkIdcardInfo(quoteBean)) {
    			msg.append("请传完整的证件类型和证件号码");
    			return false;
    		}
    	}
    	if("chnCreateTaskA".equals(sourceFrom)) {
    		String idcardNo = quoteBean.getCarOwner().getIdcardNo();
    		if(quoteBean.getCarOwner().getIdcardNo() != null && !"".equals(quoteBean.getCarOwner().getIdcardNo())) {
    			if(!ParaValidatorUtils.isValidatedAllIdcard(quoteBean.getCarOwner().getIdcardNo())) {
    				msg.append("请输入正确的证件号码");
    				return false;
    			}
    			quoteBean.getCarOwner().setIdcardNo(idcardNo.toUpperCase());
    		}
    	}else {
			if(!checkIdcardNo(quoteBean)) {
				msg.append("请输入正确的证件号码");
				return false;
			}
		}
    	if(!checkPersonOfName(quoteBean)) {
    		msg.append("请输入正确的姓名");
    		return false;
    	}
    	
    	return true;
    }
    
    //查询车辆信息--商业化接口
    private JSONObject getLastYearInsurePolicy(QuoteBean quoteBean, Map<String, String> map) throws Exception {
        //Map<String, String> map = insbChannelagreementService.getDeptIdByChannelinnercodeAndPrvcode(quoteBean.getChannelId(), quoteBean.getInsureAreaCode());

        INSBAgent insbAgent = new INSBAgent();
        insbAgent.setJobnum(map.get("jobnum"));
        insbAgent = insbAgentDao.selectOne(insbAgent);

        JSONObject object = new JSONObject();
        String quickflag = "0"; //0 正常投保  1快速续保

        object.put("flag", "XB"); //不传车辆信息
        object.put("personName", quoteBean.getCarOwner().getName()); //车主名称
        object.put("personIdno", quoteBean.getCarOwner().getIdcardNo());
        object.put("quickflag", quickflag);
        object.put("areaId", quoteBean.getInsureAreaCode());
        //object.put("provinceCode", provinceCode);
        object.put("eid", UUIDUtils.random());
        object.put("singlesite", insbAgent.getDeptid());
        object.put("agentnum", insbAgent.getJobnum());
        object.put("channelId", quoteBean.getChannelId());
        object.put("vincode", quoteBean.getCarInfo().getVinCode());

        JSONObject inParas = new JSONObject();
        inParas.put("car.specific.license", quoteBean.getCarInfo().getCarLicenseNo());
        object.put("inParas", inParas);

        if (StringUtil.isNotEmpty(insbAgent.getDeptid())) {
            object.put("platCode", inscDeptService.getPingTaiDeptId(insbAgent.getDeptid()));
        }

        String queryIn = object.toString();
        JSONObject jsonObject = null; //查询结果
        int iCount = 1; 
        
        while (iCount <= 8) { //循环查询8次
        	if (iCount != 1) {
        		Thread.sleep(4 * 1000);
        	}
        	
	        LogUtil.info(iCount + "==请求商业化车辆信息平台查询==：" + queryIn);
	        String resultJson = CloudQueryUtil.getLastYearInsurePolicy(queryIn);
	        LogUtil.info(iCount + "==接收商业化车辆信息平台查询==：" + resultJson);
	        	  
	        String status = "";  
	        if (StringUtil.isNotEmpty(resultJson)) {
	        	jsonObject = JSONObject.fromObject(resultJson);	        
		        if (jsonObject.containsKey("status")) {
		            status = jsonObject.getString("status");
		        }
	        }
	        
	        //0成功 1等待 2失败 3输入身份证  7输入vincode
	        if ("1".equals(status)) { //等待查询状态
	        	jsonObject = null;
	        } else if ( "success".equals(status) || "0".equals(status) ) { //车辆信息查询成功
	        	break;
	        } else { //车辆信息查询失败
	        	jsonObject = null; 
	        	break;
	        }
	        
	        iCount++;
        }

        return jsonObject;
    }
    
    private String addYears(String strDate, int years) {
    	if (StringUtil.isEmpty(strDate)) return null;
    	try {
			Date date = ModelUtil.conbertStringToDate(strDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.YEAR, years);
			return ModelUtil.conbertToStringsdf(calendar.getTime());
		} catch (Exception e) {
			LogUtil.error("addYears异常");
			e.printStackTrace();
		}
    	return null;
    }

    //设置需要返回的去年投保过的保险公司以及保险配置
    private void setLasYearInsureInfoRet(QuoteBean result, JSONObject jsonObject, QuoteBean quoteBean) {
        //if (StringUtil.isEmpty(quoteBean.getCarOwner().getIdcardNo())) return;
        JSONObject lastYearSupplierJb = jsonObject.getJSONObject("lastYearSupplierBean");
        JSONArray lastYearRiskinfosJa = jsonObject.getJSONArray("lastYearRiskinfos");
        JSONObject lastYearPolicyJb = jsonObject.getJSONObject("lastYearPolicyBean");

        if (lastYearSupplierJb != null && !lastYearSupplierJb.isNullObject()) { //保险公司
            result.setPrvId(lastYearSupplierJb.getString("supplierid"));
            result.setPrvName(lastYearSupplierJb.getString("suppliername"));
        }
        
        BizInsureInfoBean bizInsureInfoBean = null;
        BaseInsureInfoBean efcInsureInfo = null; 
        BaseInsureInfoBean taxInsureInfo = null;
        
        if (lastYearPolicyJb != null && !lastYearPolicyJb.isNullObject()) {
        	String startDate = lastYearPolicyJb.getString("startdate");
        	//String enddate = lastYearPolicyJb.getString("enddate");
        	String jqstartdate = lastYearPolicyJb.getString("jqstartdate");
        	//String jqenddate = lastYearPolicyJb.getString("jqenddate");     	
        	String jqprem = lastYearPolicyJb.getString("jqprem");
        	
        	if (StringUtil.isNotEmpty(startDate)) {
        		bizInsureInfoBean = new BizInsureInfoBean();
        		
        		bizInsureInfoBean.setStartDate(startDate);
            	bizInsureInfoBean.setEndDate(addYears(startDate, 1));
        	}
        	if (bizInsureInfoBean != null && StringUtil.isNotEmpty(jqstartdate) && StringUtil.isNotEmpty(jqprem)) {
        		efcInsureInfo = new BaseInsureInfoBean();
        		
        		efcInsureInfo.setStartDate(jqstartdate);
            	efcInsureInfo.setEndDate(addYears(jqstartdate, 1));
            	efcInsureInfo.setPremium(jqprem);
            	efcInsureInfo.setAmount("1");
        	}    	
        }
        
        if (bizInsureInfoBean != null) {
	        if (lastYearRiskinfosJa != null && !lastYearRiskinfosJa.isEmpty()) { //险种信息
	            List<RiskKindBean> riskKinds = new ArrayList<RiskKindBean>();
	            
	            Map<String, JSONObject> ncfRiskMap = new HashMap<String, JSONObject>(); //不计免赔险种
	            List<JSONObject> notNcfRisks = new ArrayList<JSONObject>(); //非不计免赔险种
	            for (int i = 0; i < lastYearRiskinfosJa.size(); i++) {
	            	JSONObject lastYearRiskinfoJb = lastYearRiskinfosJa.getJSONObject(i);
	            	
	            	String kindCode = lastYearRiskinfoJb.getString("kindcode");
            		if (StringUtil.isNotEmpty(kindCode) && kindCode.startsWith("Ncf") 
            				&& !"NcfDriverPassengerIns".equals(kindCode) && !"NcfClause".equals(kindCode)
            				&& !"NcfBasicClause".equals(kindCode) && !"NcfAddtionalClause".equals(kindCode)) { 
            			ncfRiskMap.put(kindCode, lastYearRiskinfoJb);
            		} else {
            			notNcfRisks.add(lastYearRiskinfoJb);
            		}
	            }
	        	
	            for (int i = 0; i < notNcfRisks.size(); i++) {
	                JSONObject lastYearRiskinfoJb = notNcfRisks.get(i);
	                RiskKindBean riskKindBean = new RiskKindBean();
	                
	                String kindCode = lastYearRiskinfoJb.getString("kindcode");
                	if ("VehicleCompulsoryIns".equals(kindCode)) { //交强险
                		continue;
                	} else if ("VehicleTax".equals(kindCode)) { //车船税
                		taxInsureInfo = new BaseInsureInfoBean();
                		taxInsureInfo.setLateFee(lastYearRiskinfoJb.getString("prem"));
                		continue;
                	} 
	
	                riskKindBean.setAmount(lastYearRiskinfoJb.getString("amount"));
	                riskKindBean.setRiskCode(kindCode);
	                riskKindBean.setRiskName(lastYearRiskinfoJb.getString("kindname"));
	                riskKindBean.setPremium(lastYearRiskinfoJb.getString("prem"));
	                
	                JSONObject ncfRisk = ncfRiskMap.get("Ncf" + kindCode);
	                if (ncfRisk != null) {
	                	riskKindBean.setNotDeductible("Y");
	                	riskKindBean.setNcfPremium(ncfRisk.getString("prem"));
	                }
	
	                riskKinds.add(riskKindBean);
	            }
	            if (riskKinds.isEmpty()) {
	            	bizInsureInfoBean = null;
	            } else {
	            	bizInsureInfoBean.setRiskKinds(riskKinds);
	            }
	        } else {
	        	bizInsureInfoBean = null;
	        }
        }
        
        InsureInfoBean insureInfoBean = null;
        if (bizInsureInfoBean != null) {
        	insureInfoBean = new InsureInfoBean();
        	insureInfoBean.setBizInsureInfo(bizInsureInfoBean);
        	insureInfoBean.setEfcInsureInfo(efcInsureInfo);
	        insureInfoBean.setTaxInsureInfo(taxInsureInfo);
        }
	    result.setInsureInfo(insureInfoBean);
    }

    //创建报价a，调cif查车辆信息自动填充
    @Override
    public QuoteBean chnCreateTaskA(QuoteBean quoteBean) throws Exception {
        QuoteBean result = new QuoteBean();
        result.setRespCode("00");
        CarInfoBean carInfoBean = new CarInfoBean();
        
        //检查入参
        if ( quoteBean.getCarInfo().getCarLicenseNo().contains("新车") || 
        	 quoteBean.getCarInfo().getCarLicenseNo().contains("未上牌") ) {
        	result.setRespCode("01");
            result.setErrorMsg("车辆信息查询失败");
            return result;
        }
        StringBuffer msg = new StringBuffer();
        if ( !transAndCheckImputCommon(quoteBean, msg, "chnCreateTaskA") ) {
        	result.setRespCode("01");
            result.setErrorMsg(msg.toString());
            return result;
        }
        
        Map<String, String> map = insbChannelagreementService.getDeptIdByChannelinnercodeAndPrvcode(quoteBean.getChannelId(), quoteBean.getInsureAreaCode());
        if (map == null) {
            LogUtil.error("查询不到渠道相关配置信息[" + quoteBean.getChannelId() + "," + quoteBean.getInsureAreaCode() + "]");
            result.setRespCode("01");
            result.setErrorMsg("查询不到渠道相关配置信息");
            return result;
        }
        
        Date date = new Date();
        JSONObject jsonObject = null;
        INSBChncarqrycount oldData = null;
        
    	Map<String, Object> countMap = new HashMap<String, Object>();
    	countMap.put("channelinnercode", quoteBean.getChannelId());
    	countMap.put("carlicenseno", quoteBean.getCarInfo().getCarLicenseNo());
        
        List<INSBChncarqrycount> oldDatas = insbChncarqrycountDao.selectCountByCidAndDay(countMap);
        if (!oldDatas.isEmpty()) {
        	INSBChncarqrycount oldDataIt = oldDatas.get(0);
        	Date createDate = oldDataIt.getCreatetime();
        	Date nextYearDate = getNextYearDate(createDate);
        	if (date.getTime() >= createDate.getTime() && date.getTime() <= nextYearDate.getTime()) {
        		oldData = oldDataIt;
        	}  
        }
    	
    	StringBuffer powerMsg = new StringBuffer("没有调用该接口的权限!");
        if ( !hasInterfacePower("10", quoteBean.getInsureAreaCode(), quoteBean.getChannelId(), powerMsg, oldData) ) { //10-车辆信息查询权限
            result.setRespCode("01");
            result.setErrorMsg(powerMsg.toString());
            return result;
        }

        jsonObject = getLastYearInsurePolicy(quoteBean, map);
        
        if (StringUtil.isEmpty(jsonObject)) {
        	result.setRespCode("01");
            result.setErrorMsg("车辆信息查询失败");
            return result;
        }
        
        JSONObject lastYearCarinfoBeanJb = jsonObject.getJSONObject("lastYearCarinfoBean");
        if ( lastYearCarinfoBeanJb == null || lastYearCarinfoBeanJb.isNullObject() ) {
        	result.setRespCode("01");
            result.setErrorMsg("车辆信息查询失败");
            return result;
        }
        
        JSONObject carModelMap = null;
        JSONArray jsonArray = jsonObject.getJSONArray("carModels");
        if (jsonArray != null && jsonArray.size() > 0) {
            int index = 0;
            double lowestPrice = jsonArray.getJSONObject(0).optDouble("price");
            for (int i = 1; i < jsonArray.size(); i++) {
                if (jsonArray.getJSONObject(i).optDouble("price") < lowestPrice) {
                    index = i;
                    lowestPrice = jsonArray.getJSONObject(i).optDouble("price");
                }
            }
            carModelMap = jsonArray.getJSONObject(index);
        }

        if (carModelMap == null) {
        	result.setRespCode("01");
            result.setErrorMsg("车辆信息查询失败");
            return result;
        }
        
        String taskid = WorkFlowUtil.startWorkflowProcess("0");//0投保 1续保
        result.setTaskId(taskid);
        setLasYearInsureInfoRet(result, jsonObject, quoteBean);

        /**2017-01-05 17:30:25 add by chenjianglong【渠道】记录各渠道调用创建报价A接口的清单表，需要增加一列记录出单工号**/
        String jobnum = "";
        if (map != null) {
            jobnum = null == map.get("jobnum") ? "" : (String)map.get("jobnum");
        } 
        jobnum = jobnum.trim();
        
        String uuid = UUIDUtils.random();
        addChncarqryDatas(quoteBean, taskid, jobnum, date, uuid); //查询车辆信息成功记录一下表
        /***************************************************************************************************/
        uptChncarqrycount(quoteBean, taskid, date, uuid, oldData);

        Map<String, INSBPerson> personMap = savePerson(quoteBean, new HashMap<String, String>(), taskid);
        INSBPerson insbCarownerPerson = personMap.get("insbCarownerPerson");
        //INSBPerson insbApplicantPerson = personMap.get("insbApplicantPerson");
        //INSBPerson insbInsuredPerson = personMap.get("insbInsuredPerson");
        //INSBPerson insbLegalrightclaimPerson = personMap.get("insbLegalrightclaimPerson");

        INSBCarinfo insbCarinfo = new INSBCarinfo();
        String engineno = lastYearCarinfoBeanJb.getString("engineno");
        carInfoBean.setEngineNo(ModelUtil.hiddenEngineNo(engineno));
        carInfoBean.setVehicleName(carModelMap.optString("vehiclename"));
        
        String registerdate = lastYearCarinfoBeanJb.getString("registerdate");
        String vinCode = lastYearCarinfoBeanJb.getString("vehicleframeno");
        carInfoBean.setRegistDate(registerdate.split(" ")[0]);
        carInfoBean.setVinCode(ModelUtil.hiddenVin(vinCode));

        insbCarinfo.setOperator("渠道");
        insbCarinfo.setCreatetime(date);
        insbCarinfo.setModifytime(date);
        insbCarinfo.setCarlicenseno(lastYearCarinfoBeanJb.getString("vehicleno"));
        insbCarinfo.setVincode(lastYearCarinfoBeanJb.getString("vehicleframeno"));
        insbCarinfo.setStandardfullname(carModelMap.optString("vehiclename"));//车型信息描述
        insbCarinfo.setEngineno(engineno);
        insbCarinfo.setOwnername(insbCarownerPerson.getName());
        insbCarinfo.setOwner(insbCarownerPerson.getId());
        insbCarinfo.setRegistdate(ModelUtil.conbertStringToNyrDate(registerdate));
        insbCarinfo.setIsTransfercar("0"); // 一律认为不是过户车
        insbCarinfo.setProperty("0");//所属性质(默认:个人用车)
        insbCarinfo.setCarproperty("1");//车辆使用性质(默认:家庭自用)
        insbCarinfo.setIsNew("0");// 0 旧车/1 新车
        insbCarinfo.setCarVehicularApplications(0); // 车辆用途 默认
        insbCarinfo.setDrivingarea("0");
//		insbCarinfo.setDrivinglicenseurl(carInfo.get("brandimg"));//行驶证图片路径
        insbCarinfo.setBrandimg(lastYearCarinfoBeanJb.getString("brandimg"));//车辆照片（平台提供）
        insbCarinfo.setTaskid(result.getTaskId());
        
        insbCarinfoDao.insert(insbCarinfo);
        LogUtil.info("INSBCarinfo|报表数据埋点|"+JSONObject.fromObject(insbCarinfo).toString());

        INSBCarmodelinfo insbCarmodelinfo = new INSBCarmodelinfo();
        insbCarmodelinfo.setOperator("渠道");
        insbCarmodelinfo.setCreatetime(date);
        insbCarmodelinfo.setModifytime(date);
        insbCarmodelinfo.setCarinfoid(insbCarinfo.getId());
        insbCarmodelinfo.setBrandname(carModelMap.optString("brandname"));
        insbCarmodelinfo.setStandardfullname(carModelMap.optString("vehiclename"));
        insbCarmodelinfo.setPrice(0.0);
        insbCarmodelinfo.setTaxprice(0.0);
        insbCarmodelinfo.setAnalogyprice(0.0);
        insbCarmodelinfo.setAnalogytaxprice(0.0);
        insbCarmodelinfo.setLoads(0.0);
        insbCarmodelinfo.setUnwrtweight(0.0);
        if (StringUtil.isNotEmpty(carModelMap.optString("analogyprice")))
            insbCarmodelinfo.setAnalogyprice(new Double(carModelMap.optString("analogyprice")));
        if (StringUtil.isNotEmpty(carModelMap.optString("analogytaxprice")))
            insbCarmodelinfo.setAnalogytaxprice(new Double(carModelMap.optString("analogytaxprice")));
        insbCarmodelinfo.setVehicleid(carModelMap.optString("vehicleId"));
        insbCarmodelinfo.setPrice(Double.parseDouble(carModelMap.optString("price")));
        String displacement = carModelMap.optString("displacement");
        if (StringUtil.isNotEmpty(displacement)) {
            insbCarmodelinfo.setDisplacement(Double.parseDouble(displacement));
        }
//		insbCarmodelinfo.setUnwrtweight(Double.parseDouble(carmodel.get(0).get));//核定载重量
        if (StringUtil.isNotEmpty(carModelMap.optString("seat")))
            insbCarmodelinfo.setSeat(Integer.parseInt(carModelMap.optString("seat")));
        insbCarmodelinfo.setCarprice("0"); //没有指定车价
        if (StringUtil.isNotEmpty(carModelMap.optString("fullweight")))
            insbCarmodelinfo.setFullweight(Double.parseDouble(carModelMap.optString("fullweight")));
        insbCarmodelinfo.setFamilyname(carModelMap.optString("familyname"));
        insbCarmodelinfo.setStandardname(carModelMap.optString("vehiclename"));
        insbCarmodelinfo.setIsstandardcar("0");
        
        insbCarmodelinfoDao.insert(insbCarmodelinfo);

        INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
        insbQuotetotalinfo.setTaskid(result.getTaskId());
        insbQuotetotalinfo.setCreatetime(date);
        insbQuotetotalinfo.setOperator("渠道");
        insbQuotetotalinfo.setInscitycode(quoteBean.getInsureAreaCode());
        if (StringUtil.isNotEmpty(quoteBean.getMsgType())) {
            insbQuotetotalinfo.setSourceFrom(quoteBean.getMsgType());
        } else {
            insbQuotetotalinfo.setSourceFrom("channel");
        }
        insbQuotetotalinfo.setPurchaserChannel(quoteBean.getChannelId());
        insbQuotetotalinfo.setCallBackUrl(quoteBean.getCallbackurl());
        insbQuotetotalinfo.setPurchaserid(quoteBean.getChannelUserId());

        insbQuotetotalinfo.setAgentnum(jobnum); 

        INSBRegion insbRegion = new INSBRegion();
        insbRegion.setComcode(quoteBean.getInsureAreaCode());
        insbRegion = insbRegionDao.selectOne(insbRegion);
        insbQuotetotalinfo.setInsprovincecode(insbRegion.getParentid());
        insbQuotetotalinfo.setInscityname(insbRegion.getComcodename());

        INSBRegion parentRegion = new INSBRegion();
        parentRegion.setComcode(insbRegion.getParentid());
        parentRegion = insbRegionDao.selectOne(parentRegion);
        insbQuotetotalinfo.setInsprovincename(parentRegion.getComcodename());
        insbQuotetotalinfoDao.insert(insbQuotetotalinfo);
        LogUtil.info("INSBQuotetotalinfo|报表数据埋点|"+JSONObject.fromObject(insbQuotetotalinfo).toString());
        
        result.setCarInfo(carInfoBean);

        //mini保存agenttask数据
        saveAgentTask(result.getTaskId(),quoteBean.getChannelUserId(),quoteBean.getChannelId());
        
        return result;
    }

    //记录渠道车辆信息查询记录表
    private void addChncarqryDatas(QuoteBean quoteBean, String taskId, String jobnum, Date date, String uuid) {
        INSBChncarqry chncarqry = new INSBChncarqry();
        chncarqry.setCarlicenseno(quoteBean.getCarInfo().getCarLicenseNo());
        chncarqry.setChannelinnercode(quoteBean.getChannelId());
        chncarqry.setCreatetime(date);
        chncarqry.setIdcardno(quoteBean.getCarOwner().getIdcardNo());
        chncarqry.setCarowner(quoteBean.getCarOwner().getName());
        chncarqry.setTaskid(taskId);
        chncarqry.setId(uuid);
        chncarqry.setJobnum(jobnum);
        insbChncarqryDao.createSubTable(chncarqry); //如果没有分表就创建
        insbChncarqryDao.addChncarqryDatas(chncarqry);
    }

    //记录渠道车辆信息查询次数统计表
    private void uptChncarqrycount(QuoteBean quoteBean, String taskId, Date date, String uuid, INSBChncarqrycount oldData) {
        String channelId = quoteBean.getChannelId();
        String carowner = quoteBean.getCarOwner().getName();
        String carlicenseno = quoteBean.getCarInfo().getCarLicenseNo();
        String idcardno = quoteBean.getCarOwner().getIdcardNo();
        
        if (oldData == null) {
            INSBChncarqrycount chncarqrycount = new INSBChncarqrycount();
            chncarqrycount.setChannelinnercode(channelId);
            chncarqrycount.setCreatetime(date);
            chncarqrycount.setModifytime(date);
            chncarqrycount.setCallcount(1);
            chncarqrycount.setId(uuid);
            chncarqrycount.setIdcardno(idcardno);
            chncarqrycount.setCarowner(carowner);
            chncarqrycount.setCarlicenseno(carlicenseno);
            insbChncarqrycountDao.addChncarqrycountDatas(chncarqrycount);
        } else {
        	oldData.setModifytime(date); 
        	insbChncarqrycountDao.addCount(oldData);
        }
    }
    
    private Date getNextYearDate(Date oldDate) {
    	Calendar calendar = Calendar.getInstance();
		calendar.setTime(oldDate);
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
		Date newDate = calendar.getTime();
    	
    	String strNewDate = ModelUtil.conbertToString(newDate) + " 00:00:00";
    	return ModelUtil.conbertStringToDate(strNewDate);
    }

    //查询车型
    private CarModelInfoBean getOneCarModel(String vehicleId) {
        CarModelInfoBean carModelInfoBean = new CarModelInfoBean();
        String jsonStr = CloudQueryUtil.getOneCarModel(vehicleId);
        if (StringUtil.isNotEmpty(jsonStr)) {
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            carModelInfoBean.setAnalogyPrice(jsonObject.optString("analogyprice"));
            carModelInfoBean.setAnalogyTaxPrice(jsonObject.optString("analogytaxprice"));
            carModelInfoBean.setBrandName(jsonObject.optString("brandname"));
            carModelInfoBean.setDisplacement(jsonObject.optString("displacement"));
            if (StringUtil.isEmpty(carModelInfoBean.getDisplacement()))
                carModelInfoBean.setDisplacement("1");
            carModelInfoBean.setFullWeight(jsonObject.optString("fullweight"));
            carModelInfoBean.setMaketDate(jsonObject.optString("maketdate"));
            carModelInfoBean.setModelLoads(jsonObject.optString("modelLoads"));
            if (StringUtil.isEmpty(carModelInfoBean.getModelLoads()))
                carModelInfoBean.setModelLoads("0.0");
            carModelInfoBean.setPrice(jsonObject.optString("price"));
            carModelInfoBean.setSeat(jsonObject.optString("seat"));
            carModelInfoBean.setTaxPrice(jsonObject.optString("taxprice"));
            carModelInfoBean.setVehicleName(jsonObject.optString("vehiclename"));
            carModelInfoBean.setYearStyle(jsonObject.optString("yearstyle"));
            carModelInfoBean.setFamilyName(jsonObject.optString("familyname"));
            carModelInfoBean.setFactoryName(jsonObject.optString("factoryname"));
            carModelInfoBean.setGearbox(jsonObject.optString("gearbox"));
            carModelInfoBean.setVehicleId(vehicleId);
        }
        return carModelInfoBean;
    }

    //调车型接口查出的车型信息CarModelInfoBean 转换为 渠道接口车辆信息CarInfoBean
    private void mergeCarInfo(CarInfoBean carInfoBean, CarModelInfoBean carModelInfoBean) {
        if (carInfoBean.getAnalogyPrice() == null)
            carInfoBean.setAnalogyPrice(carModelInfoBean.getAnalogyPrice());
        if (carInfoBean.getAnalogyTaxPrice() == null)
            carInfoBean.setAnalogyTaxPrice(carModelInfoBean.getAnalogyTaxPrice());
        if (carInfoBean.getBrandName() == null)
            carInfoBean.setBrandName(carModelInfoBean.getBrandName());
        if (carInfoBean.getDisplacement() == null)
            carInfoBean.setDisplacement(carModelInfoBean.getDisplacement());
        if (carInfoBean.getFullWeight() == null)
            carInfoBean.setFullWeight(carModelInfoBean.getFullWeight());
        if (carInfoBean.getMaketDate() == null)
            carInfoBean.setMaketDate(carModelInfoBean.getMaketDate());
        if (carInfoBean.getModelLoads() == null)
            carInfoBean.setModelLoads(carModelInfoBean.getModelLoads()); 
        carInfoBean.setPrice(carModelInfoBean.getPrice());
        if (carInfoBean.getSeat() == null)
            carInfoBean.setSeat(carModelInfoBean.getSeat());
        if (carInfoBean.getTaxPrice() == null)
            carInfoBean.setTaxPrice(carModelInfoBean.getTaxPrice());
        if (carInfoBean.getVehicleName() == null)
            carInfoBean.setVehicleName(carModelInfoBean.getVehicleName());
        if (carInfoBean.getYearStyle() == null)
            carInfoBean.setYearStyle(carModelInfoBean.getYearStyle());

        //填一些默认值
        if (carInfoBean.getCarProperty() == null)
            carInfoBean.setCarProperty("1");
        if (carInfoBean.getDrivingArea() == null)
            carInfoBean.setDrivingArea("0");
        if (carInfoBean.getIsNew() == null)
            carInfoBean.setIsNew("N");
        if (carInfoBean.getIsTransferCar() == null)
            carInfoBean.setIsTransferCar("N");
        if (carInfoBean.getProperty() == null)
            carInfoBean.setProperty("0");
        if (carInfoBean.getPurpose() == null)
            carInfoBean.setPurpose("0");
    }

    //创建任务b接口检查入参
    private void chkQuoteInput(QuoteBean quoteBean, QuoteBean temp) {
        CarInfoBean chkCarInfo = quoteBean.getCarInfo();
        PersonBean chkCarOwner = quoteBean.getCarOwner();
        List<ProviderBean> providerBeans = quoteBean.getProviders();
        if (StringUtil.isEmpty(quoteBean.getInsureAreaCode())) {
            temp.setErrorMsg("投保地区不能为空");
            return;
        }
        if (null == providerBeans || providerBeans.size() == 0) {
            temp.setErrorMsg("请至少选择一家供应商");
            return;
        }
        if (chkCarInfo != null) {
            if (StringUtil.isNotEmpty(chkCarInfo.getIsNew()) && "Y".equals(chkCarInfo.getIsNew())) {

            } else if (StringUtil.isEmpty(chkCarInfo.getCarLicenseNo())) {
                temp.setErrorMsg("车牌号不能为空");
                return;
            } 
            
            if (StringUtil.isEmpty(chkCarInfo.getVinCode())) {
                temp.setErrorMsg("车架号不能为空");
                return;
            }
            if (StringUtil.isEmpty(chkCarInfo.getEngineNo())) {
                temp.setErrorMsg("发动机号不能为空");
                return;
            }
            if (StringUtil.isEmpty(chkCarInfo.getRegistDate())) {
                temp.setErrorMsg("初登日期不能为空");
                return;
            }           
            
        } else {
            temp.setErrorMsg("车辆信息不能为空");
            return;
        }
        
        if (chkCarOwner != null) {
            if (StringUtil.isEmpty(chkCarOwner.getName())) {
                temp.setErrorMsg("车主姓名不能为空");
                return;
            }
        } else {
            temp.setErrorMsg("车主信息不能为空");
            return;
        }
        
        StringBuffer msg = new StringBuffer();
        if ( !transAndCheckImputCommon(quoteBean, msg, "createTaskB") ) {
            temp.setErrorMsg(msg.toString());
            return;
        }
    }

    private String getSelectedItem(String amount, INSBRiskkindconfig riskkindconfig) {
        Double d = Double.parseDouble(amount);
        String strAmount = String.valueOf(d.longValue());
        String datatemp = riskkindconfig.getDatatemp();
        datatemp = datatemp.substring(1);
        datatemp = datatemp.substring(0, datatemp.length() - 1);
        JSONObject item = JSONObject.fromObject(datatemp);
        SelectOption selectOption = new SelectOption();
        JSONArray valueArray = JSONArray.fromObject(item.get("VALUE"));
        for (int i = 0; i < valueArray.size(); i++) {
            JSONObject obj = JSONObject.fromObject(valueArray.get(i));
            String unit = obj.getString("UNIT");
            String key = obj.getString("KEY");
            String value = obj.getString("VALUE");

            if (value.equals(strAmount)) {
                RisksData riskData = new RisksData();
                riskData.setUNIT(unit);
                riskData.setKEY(key);
                riskData.setVALUE(value);
                selectOption.setVALUE(riskData);
                break;
            }
        }
        selectOption.setTYPE((String) item.get("TYPE"));
        JSONObject jsonObject = JSONObject.fromObject(selectOption);
        String selectedItem = "[" + jsonObject.toString() + "]";
        return selectedItem;
    }
    
    //判断渠道有没有调用接口的权限--无msg出参
    public boolean hasInterfacePower(String id, String city, String channelinnercode) {
    	return hasInterfacePower(id, city, channelinnercode, new StringBuffer(), null);
    }

    //判断渠道有没有调用接口的权限 
    public boolean hasInterfacePower(String id, String city, String channelinnercode, StringBuffer msg, INSBChncarqrycount oldData) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("city", city);
        map.put("channelinnercode", channelinnercode);
        map.put("agreementstatus", "1");
        List<Map<String, Object>> interfaces = insbAgreementinterfaceDao.getByChannelinnercode(map);
        if (interfaces != null && interfaces.size() > 0) {
        	if ("10".equals(id)) { //10-创建报价a
        		return checkAIsFree(interfaces.get(0), msg, channelinnercode, oldData);
            } else {
            	return true;
            }
        } else {
            return false;
        }
    }
    
    //判断a接口是否要收费
    private boolean checkAIsFree(Map<String, Object> map, StringBuffer msg, String channelinnercode, INSBChncarqrycount oldData) {
    	String isfree = map.get("isfree").toString();
    	String extendspattern = "1";
    	Object objExtendspattern = map.get("extendspattern");
    	if (objExtendspattern != null) {
    		extendspattern = objExtendspattern.toString();
    	}
    	int freetimes = 0;
    	Object objFreeTimes = map.get("monthfree");
		if (objFreeTimes != null) {
			freetimes = Integer.parseInt(objFreeTimes.toString());
		}
		
		if ("1".equals(isfree) && "1".equals(extendspattern) && oldData == null) { //收费 && 免费次数用完后禁止使用 &&
			//Date date = new Date();
			Map<String, Object> mapCon = new HashMap<String, Object>();
			mapCon.put("channelinnercode", channelinnercode); 
			//mapCon.put("modifytimeend", ModelUtil.conbertToStringsdf(date));	
			
			long yearChargeCount = insbChncarqrycountDao.countData(mapCon);
			
			if (yearChargeCount >= freetimes) {
				msg.append("免费使用次数已用完，请联系商务人员/运营人员开通");
				return false;
			}
		}
		
		return true;
    }

    @Override
    protected BaseDao<INSBWorkflowmain> getBaseDao() {
        return null;
    }

    /**
     * @param taskId 主流程id
     * @param prvId  供应商编码
     * @return 最近的起保日期
     */
    @Override
    public Date getStartDate(String taskId, String prvId) {
        Date startDate = null;
        Map<String, String> param = new HashMap<>();
        param.put("taskid", taskId);
        param.put("inscomcode", prvId);
        List<INSBPolicyitem> policyitemList = insbPolicyitemDao.getListByMap(param);

        if (null != policyitemList && policyitemList.size() > 0) {
            for (INSBPolicyitem policyitem : policyitemList) {

                if (null != startDate && startDate.after(policyitem.getStartdate())) {
                    startDate = policyitem.getStartdate();

                } else {
                    startDate = policyitem.getStartdate();
                }
            }
        }
        return startDate;
    }

    //取得收银台支付地址
    public QuoteBean pay(QuoteBean quoteBean) throws Exception {
        QuoteBean result = new QuoteBean();
        String channelId = quoteBean.getChannelId();
        String taskId = quoteBean.getTaskId();
        String prvId = quoteBean.getPrvId();
        result.setRespCode("00");
        
        INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
        insbQuotetotalinfo.setTaskid(quoteBean.getTaskId());
        insbQuotetotalinfo.setPurchaserChannel(channelId);
        insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
        if (!quoteBean.getChannelId().equals(insbQuotetotalinfo.getPurchaserChannel())) {
            result.setRespCode("01");
            result.setErrorMsg("请提供正确的任务号");
            return result;
        }
        
        if (!hasInterfacePower("14", insbQuotetotalinfo.getInscitycode(), channelId)) { //14-支付请求权限
            result.setRespCode("01");
            result.setErrorMsg("没有调用该接口的权限!");
            return result;
        }

        Map<String, String> map = insbWorkflowsubDao.getCurrentTaskcodeOfSubFlow(quoteBean.getTaskId(), quoteBean.getPrvId());
        String taskCode = "";
        if (map != null && map.size() > 0) {
            taskCode = map.get("taskcode");
        }
        if ( !"20".equals(taskCode) && !"14".equals(quoteBean.getTaskState()) ) {
        	result.setRespCode("01");
            result.setErrorMsg("保单状态不正确，暂时无法支付" + taskCode);
            result.setPayUrl(null);
            return result;
        }
        
        if ( "20".equals(taskCode) ) { //旧流程
	        boolean pinCodeBJCheck = chnService.pinCodeBJCheck(insbQuotetotalinfo, prvId, null);
	        if ( !pinCodeBJCheck ) {
	        	result.setRespCode("01");
	            result.setErrorMsg("北京地区非企业车投保，请在有效期内补充被保人资料");
	            return result;
	        }
        }
        
    	/* if ( "14".equals(quoteBean.getTaskState()) ) { //新流程报价成功
    		boolean needUploadImg = chnService.getImageInfos(quoteBean, "14");
            result.setMsgType(quoteBean.getMsgType());
            result.setImageInfos(quoteBean.getImageInfos());
    	} */
        
        INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
        insbQuoteinfo.setQuotetotalinfoid(insbQuotetotalinfo.getId());
        insbQuoteinfo.setInscomcode(prvId);
        insbQuoteinfo = insbQuoteinfoDao.selectOne(insbQuoteinfo);  
        
        if (!isWorkTime(taskId, prvId)) {
            // 次日起保
            Date qibaoDate = getStartDate(quoteBean.getTaskId(), quoteBean.getPrvId());
            Date now = new Date();

            long diff = qibaoDate.getTime() - now.getTime();
            if (diff < 0) {
                result.setRespCode("01");
                result.setErrorMsg("您好，您的订单已超过有效期，请申请全额退款并且重新提交报价，感谢您的支持！");
                result.setPayUrl(null);
                return result;
            }
            boolean flag = diff / (1000 * 60 * 24 * 24) == 1;
            if (flag) {
                result.setRespCode("01");
                result.setErrorMsg("对不起，您的订单是次日起保，现在是非工作时间，保险公司无法进行承保处理，请修改起保日期后再重新提交报价，感谢您的支持！");
                result.setPayUrl(null);
                return result;
            } else {
                result.setMsgType("00");
                result.setErrorMsg("您好，现在是非工作时间，若继续支付，订单将在上班后第一时间为您确认。感谢您的支持！");
            }
        }
        
        Date payValidDate = null;
        if ("20".equals(taskCode)) {
        	payValidDate = chnService.getPayValidDate(taskId, prvId, true, false);
        } else if ("14".equals(quoteBean.getTaskState())) {
        	payValidDate = chnService.getQuoteValidDate(taskId, prvId, insbQuoteinfo.getWorkflowinstanceid(), true, true);
        }
        if (payValidDate != null && (new Date().getTime() > payValidDate.getTime())) {
        	result.setRespCode("01");
            result.setErrorMsg("您好，您的订单已超过支付有效期，请重新提交报价，感谢您的支持！");
            result.setPayUrl(null);
            return result;
    	}
            
        Map<String, String> param = new HashMap<String, String>();
        param.put("taskid", taskId);
        param.put("inscomcode", prvId);
        List<INSBPolicyitem> policyitemList = insbPolicyitemDao.getListByMap(param);
        if (policyitemList == null || policyitemList.size() < 1) {
            result.setRespCode("01");
            result.setErrorMsg("供应商" + prvId + ":未查到保单信息");
            return result;
        }
        
        String random = RandomStringUtils.randomAlphanumeric(32);
        quoteBean.setCreateTime(String.valueOf(System.currentTimeMillis()));
        quoteBean.setChannelId(channelId);
        result.setPayUrl(ValidateUtil.getConfigValue("channel.payurl") + random);
        redisClient.set(CHNPaymentService.CHANNEL_PAYMENT_MODULE, random, JsonUtils.serialize(quoteBean), CHNPaymentService.PAYURL_VALIDTIME_SECONDS);

        INSBOrder oldOrder = null;
        INSBOrder insbOrder = new INSBOrder();
        insbOrder.setTaskid(quoteBean.getTaskId());
        List<INSBOrder> insbOrders = insbOrderDao.selectList(insbOrder);
        for(INSBOrder tmpOrder : insbOrders){
        	if(tmpOrder.getPrvid().equals(prvId)){
        		oldOrder = tmpOrder;
        	}else{
        		tmpOrder.setOrderstatus("4"); // 将其他订单的状态改为关闭
        		insbOrderDao.updateById(tmpOrder);
        	}
        }
        
        String agentnum = policyitemList.get(0).getAgentnum();
        INSBAgent insbAgent = new INSBAgent();
        insbAgent.setJobnum(agentnum);
        insbAgent = insbAgentDao.selectOne(insbAgent);
        insbOrder = new INSBOrder();
        insbOrder.setOperator("渠道");// 操作员
        insbOrder.setCreatetime(new Date());// 创建时间
        insbOrder.setTaskid(taskId);// 流程实例id
        insbOrder.setOrderno(GenerateSequenceUtil.generateSequenceNo(taskId, insbQuoteinfo.getWorkflowinstanceid()));// 订单号
        insbOrder.setOrderstatus("1");// 渠道的单生成订单后即可支付，不需经过核保
        insbOrder.setPaymentstatus("0");// 支付状态todo
        insbOrder.setDeliverystatus("0");// 配送状态todo
        insbOrder.setBuyway(insbQuoteinfo.getBuybusitype());// 销售渠道(01传统02网销03电销)todo
        insbOrder.setAgentcode(insbAgent.getJobnum());// 代理人编码
        insbOrder.setAgentname(insbAgent.getName());// 代理人姓名
        insbOrder.setInputusercode(insbAgent.getJobnum());// 录单人

        // 计算应付保费
        double discountPremium = 0d, premium = 0d;
        INSBCarkindprice carkindprice = new INSBCarkindprice();
        carkindprice.setTaskid(taskId);
        carkindprice.setInscomcode(prvId);
        List<INSBCarkindprice> carkindpriceList = insbCarkindpriceDao.selectList(carkindprice);
        for (INSBCarkindprice ckp : carkindpriceList) {
            if (ckp.getDiscountCharge() != null) {
                discountPremium += ckp.getDiscountCharge();// 折后保费
                if (ckp.getAmountprice() != null && ckp.getAmountprice().compareTo(0.0d) > 0) {
                    premium += ckp.getAmountprice();// 保费
                } else {
                    premium += ckp.getDiscountCharge();// 如果没有折前保费则使用折后保费
                }
            }
        }

        insbOrder.setTotalproductamount(premium);// 订单标价总金额
        insbOrder.setTotalpaymentamount(discountPremium);// 实付金额
        if (premium > discountPremium) {
            insbOrder.setTotalpromotionamount(premium - discountPremium);// 优惠金额
        }
        insbOrder.setDeptcode(insbQuoteinfo.getDeptcode());// 出单网点
        insbOrder.setCurrency("RMB");
        insbOrder.setPrvid(prvId);
        if (oldOrder != null) {
        	insbOrder.setId(oldOrder.getId());
        	insbOrderDao.updateById(insbOrder);
        } else {
        	insbOrderDao.insert(insbOrder);
        }

        if(null != result.getPayUrl()){
            /* 记录chn推cm的时间点开始*/
            try {
                Map<String, String> phaseTime = new HashMap<>();
                phaseTime.put("channelId", quoteBean.getChannelId());
                phaseTime.put("taskId", taskId);
                phaseTime.put("prvId", quoteBean.getPrvId()==null?"":quoteBean.getPrvId());
                phaseTime.put("taskState", quoteBean.getTaskState()==null?"":quoteBean.getTaskState());
                phaseTime.put("phaseType", "submitPayChnCallCM");
                phaseTime.put("phaseSeq", "7");
                chnService.recordPhaseTime(phaseTime);
            }catch (Exception e){
                LogUtil.info("记录chn推cm支付的时间点异常:"+taskId+e.getMessage());
            }
            /* 记录chn推cm的时间点结束*/
        }

        return result;
    }

    /**
     * 检出证件类型
     * @param quoteBean
     * @return
     */
    public boolean checkIdcardInfo(QuoteBean quoteBean) {
    	PersonBean applicant = quoteBean.getApplicant();
    	PersonBean insured = quoteBean.getInsured();
    	PersonBean beneficiary = quoteBean.getBeneficiary();
    	PersonBean carOwner = quoteBean.getCarOwner();
    	if(applicant != null) {
    		if(!checkIdcardNoAndType(applicant)) return false;
		}
    	if(insured != null) {
    		if(!checkIdcardNoAndType(insured)) return false;
    	}
    	if(beneficiary != null) {
    		if(!checkIdcardNoAndType(beneficiary)) return false;
    	}
    	if(carOwner != null) {
    		if(!checkIdcardNoAndType(carOwner)) return false;
    	}
    	return true;
    }
    /**
     * 检查是否有这两个值，要么同时有，要么同时没有ok
     * @param person
     * @return
     */
    private boolean checkIdcardNoAndType(PersonBean person) {
    	
		if(StringUtil.isNotEmpty(person.getIdcardNo()) && StringUtil.isEmpty(person.getIdcardType())) {
			return false;
		}else if(StringUtil.isEmpty(person.getIdcardNo()) && StringUtil.isNotEmpty(person.getIdcardType())){
			return false;
		}
    	
    	return true;
    }
    /**
     * 对于名字和证件校验
     * @param quoteBean
     * @return
     */
    public boolean checkIdcardNo(QuoteBean quoteBean) {
    	PersonBean applicant = quoteBean.getApplicant();
    	PersonBean insured = quoteBean.getInsured();
    	PersonBean beneficiary = quoteBean.getBeneficiary();
    	PersonBean carowne = quoteBean.getCarOwner();
    	if(carowne != null) {
    		if(!checkPeopleInfo(carowne)) return false;
    	}
    	if(applicant != null) {
    		if(!checkPeopleInfo(applicant)) return false;
    	}
    	if(insured != null) {
    		if(!checkPeopleInfo(insured)) return false;
    	}
    	if(beneficiary != null) {
    		if(!checkPeopleInfo(beneficiary)) return false;
    	}
    	quoteBean.setApplicant(applicant);
    	quoteBean.setInsured(insured);
    	quoteBean.setCarOwner(carowne);
    	quoteBean.setBeneficiary(beneficiary);
    	return true;
    }
    /**
     * 校验人的信息
     * @param person
     * @return
     */
    private boolean checkPeopleInfo(PersonBean person) {
    	String idcardNo = person.getIdcardNo();
    	String idcardType = person.getIdcardType();

		if(StringUtil.isNotEmpty(idcardType) && StringUtil.isNotEmpty(idcardNo)) {
//			身份证的校验
			if("0".equals(idcardType)) {
				if(!ParaValidatorUtils.isValidatedAllIdcard(idcardNo)) return false;
//			驾驶证的校验
			}else if ("2".equals(idcardType)) {
				if(!ParaValidatorUtils.checkDrivingLicense(idcardNo)) return false;
//			社会组织代码的校验
			}else if ("6".equals(idcardType)) {
				if(!ParaValidatorUtils.checkOrgCodeCert(idcardNo)) return false;
//			社会信用代码证校验
			}else if ("8".equals(idcardType)) {
				if(!ParaValidatorUtils.checkCreditNo(idcardNo)) return false;
//			户口本号码校验
			}else if ("1".equals(idcardType)) {
				if(!ParaValidatorUtils.checkHouseholdRegister(idcardNo)) return false;
//			军官证的校验
			}else if ("3".equals(idcardType)) {
				if(!ParaValidatorUtils.checkSoldierCard(idcardNo)) return false;
//			护照号码的校验
			}else if ("4".equals(idcardType)) {
				if(!ParaValidatorUtils.checkPassPort(idcardNo)) return false;
//			港澳回乡证的校验
			}else if ("5".equals(idcardType)) {
				if(!ParaValidatorUtils.checkHKMK(idcardNo)) return false;
			}
			person.setIdcardNo(idcardNo.toUpperCase());
		}
		
    	return true;
    }
    private boolean checkPersonOfName(QuoteBean quoteBean) {
    	PersonBean applicant = quoteBean.getApplicant();
    	PersonBean insured = quoteBean.getInsured();
    	PersonBean beneficiary = quoteBean.getBeneficiary();
    	PersonBean carowne = quoteBean.getCarOwner();
    	if(applicant != null && StringUtil.isNotEmpty(applicant.getName())) {
//			对名字的校验
			if(!ParaValidatorUtils.checkName(applicant.getName())) return false;
		}
    	if(insured != null && StringUtil.isNotEmpty(insured.getName())) {
//			对名字的校验
    		if(!ParaValidatorUtils.checkName(insured.getName())) return false;
    	}
    	if(beneficiary != null && StringUtil.isNotEmpty(beneficiary.getName())) {
//			对名字的校验
    		if(!ParaValidatorUtils.checkName(beneficiary.getName())) return false;
    	}
    	if(carowne != null && StringUtil.isNotEmpty(carowne.getName())) {
//			对名字的校验
    		if(!ParaValidatorUtils.checkName(carowne.getName())) return false;
    	}
    	return true;
    }
}
