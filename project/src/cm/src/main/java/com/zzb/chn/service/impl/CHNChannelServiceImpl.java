package com.zzb.chn.service.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.apache.commons.beanutils.PropertyUtils;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cninsure.core.quartz.manager.abs.AbsSchedulerManager;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.security.SecurityDefaultInterceptor;
import com.cninsure.system.service.INSCCodeService;
import com.cninsure.system.service.INSCDeptService;
import com.common.CloudQueryUtil;
import com.common.ModelUtil;
import com.common.redis.IRedisClient;
import com.zzb.chn.bean.AgreementAreaBean;
import com.zzb.chn.bean.BaseInsureInfoBean;
import com.zzb.chn.bean.BizInsureInfoBean;
import com.zzb.chn.bean.CarInfoBean;
import com.zzb.chn.bean.CarModelInfoBean;
import com.zzb.chn.bean.CityBean;
import com.zzb.chn.bean.DeliveryBean;
import com.zzb.chn.bean.DeptInfoBean;
import com.zzb.chn.bean.DrivingInfoBean;
import com.zzb.chn.bean.IDCardBean;
import com.zzb.chn.bean.ImageInfoBean;
import com.zzb.chn.bean.InsureInfoBean;
import com.zzb.chn.bean.InsureSupplyBean;
import com.zzb.chn.bean.ListQuoteBean;
import com.zzb.chn.bean.PersonBean;
import com.zzb.chn.bean.ProviderBean;
import com.zzb.chn.bean.QuoteBean;
import com.zzb.chn.bean.RiskKindBean;
import com.zzb.chn.job.NotiJob;
import com.zzb.chn.service.CHNChannelQuoteService;
import com.zzb.chn.service.CHNChannelService;
import com.zzb.chn.service.CHNCommissionRateService;
import com.zzb.chn.service.INSBThirdCommissionService;
import com.zzb.chn.util.CHNTokenGenerate;
import com.zzb.chn.util.CallChnInterfaceUtil;
import com.zzb.chn.util.JsonUtils;
import com.zzb.chn.util.StatusCodeMapperUtil;
import com.zzb.cm.Interface.service.ExternalInterFaceService;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarinfohisDao;
import com.zzb.cm.dao.INSBCarkindpriceDao;
import com.zzb.cm.dao.INSBCarmodelinfohisDao;
import com.zzb.cm.dao.INSBFilebusinessDao;
import com.zzb.cm.dao.INSBFlowerrorDao;
import com.zzb.cm.dao.INSBInsuresupplyparamDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.dao.INSBSpecialkindconfigDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.entity.INSBCarmodelinfohis;
import com.zzb.cm.entity.INSBFilebusiness;
import com.zzb.cm.entity.INSBFilelibrary;
import com.zzb.cm.entity.INSBFlowerror;
import com.zzb.cm.entity.INSBInsuresupplyparam;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.entity.INSBSpecialkindconfig;
import com.zzb.cm.service.INSBCarinfoService;
import com.zzb.cm.service.INSBFilelibraryService;
import com.zzb.cm.service.INSBFlowerrorService;
import com.zzb.conf.dao.INSBAgreementareaDao;
import com.zzb.conf.dao.INSBAgreementdeptDao;
import com.zzb.conf.dao.INSBAgreementinterfaceDao;
import com.zzb.conf.dao.INSBChannelDao;
import com.zzb.conf.dao.INSBOrderdeliveryDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.INSBRegionDao;
import com.zzb.conf.dao.INSBUsercommentDao;
import com.zzb.conf.dao.INSBWorkflowmainDao;
import com.zzb.conf.dao.INSBWorkflowmaintrackDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.dao.INSBWorkflowsubtrackDao;
import com.zzb.conf.entity.INSBAgreementinterface;
import com.zzb.conf.entity.INSBChannel;
import com.zzb.conf.entity.INSBOrderdelivery;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBRegion;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowmaintrack;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import com.zzb.conf.service.INSBChannelService;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.extra.model.AgentTaskModel;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.ExtendCommonModel;
import com.zzb.mobile.model.IDCardModel;
import com.zzb.mobile.model.InsuranceImageBean;
import com.zzb.mobile.service.AppInsuredQuoteService;
import com.zzb.mobile.service.INSBMyOrderService;
import com.zzb.model.WorkFlow4TaskModel;
import com.zzb.ocr.bean.DrivingEntity;
import com.zzb.ocr.bean.IDCardEntity;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class CHNChannelServiceImpl implements CHNChannelService {
	private static ResourceBundle config = ResourceBundle.getBundle("config/config");
	
    @Autowired
    private INSBProviderService provierService;
    @Resource
    private INSCCodeService insccodeservice;
    @Resource
    private INSBFilelibraryService insbFilelibraryService;
    @Resource(name = "ocrClient")
    private com.zzb.ocr.client.OCRClient ocrClient;
    @Resource
    private INSBFilebusinessDao insbFilebusinessDao;
    @Resource
    private INSBWorkflowmainDao insbWorkflowmainDao;
    @Resource
    private INSBCarkindpriceDao insbCarkindpriceDao;
    @Resource
    private INSBSpecialkindconfigDao insbSpecialkindconfigDao;
    @Resource
    private INSCCodeDao inscCodeDao;
    @Resource
    private INSBPolicyitemDao insbPolicyitemDao;
    @Resource
    private INSBChannelService insbchannelservice;
    @Resource
    private INSCDeptDao inscDeptDao;
    @Resource
    private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
    @Resource
    private ThreadPoolTaskExecutor taskthreadPool4workflow;
    @Resource
    private INSBQuoteinfoDao insbQuoteinfoDao;
    @Resource
    private INSBWorkflowsubDao insbWorkflowsubDao;
    @Resource
    private INSBUsercommentDao insbUsercommentDao;
    @Resource
    private INSBChannelDao insbChannelDao;
    @Resource
    private INSBFlowerrorService insbFlowerrorService;
    @Resource
    private INSBOrderdeliveryDao insbOrderdeliveryDao;
    @Resource
    private INSBPersonDao insbPersonDao;
    @Resource
    private INSBCarinfohisDao insbCarinfohisDao;
    @Resource
    private INSBCarinfoDao insbCarinfoDao;
    @Resource
    private INSBCarmodelinfohisDao insbCarmodelinfohisDao;
    @Resource
    private INSBProviderDao insbProviderDao;
    @Resource
    private INSBWorkflowsubtrackDao insbWorkflowsubtrackDao;
    @Resource
    private INSBAgreementareaDao insbAgreementareaDao;
    @Resource
	private INSBMyOrderService insbMyOrderService;
    @Resource
    private CHNChannelQuoteService channelchnQuoteService;
    @Resource
	private INSBFlowerrorDao insbFlowerrorDao;
    @Resource 
    private INSCCodeService inscCodeService;
    @Resource
	private INSBUsercommentService insbUsercommentService;
    @Resource
    private IRedisClient redisClient;
    @Resource
    private INSBWorkflowmaintrackDao insbWorkflowmaintrackDao;
    @Resource
    private INSBAgreementdeptDao insbAgreementdeptDao;
    @Resource
	private AppInsuredQuoteService insuredQuoteService;
    @Resource
    private CHNCommissionRateService chnCommissionRateService;
    @Resource
    private INSBCarinfoService insbCarinfoService;
    @Resource
    private INSBAgreementinterfaceDao insbAgreementinterfaceDao;
    @Resource
	private ExternalInterFaceService enternalInterFaceService;
    @Resource
    private INSBRegionDao insbRegionDao;
    @Resource
	private INSBInsuresupplyparamDao insbInsuresupplyparamDao;
    @Resource
    private INSBThirdCommissionService insbThirdCommissionService;
    @Resource
	private INSCDeptService inscDeptService;
    
    //渠道获取调用接口的token凭证
    @Override
    public QuoteBean getToken(String channelId, String channelSecret) {
        QuoteBean result = new QuoteBean();
        try {
            INSBChannel channel = new INSBChannel();
            channel.setChannelinnercode(channelId);
            channel.setChannelsecret(channelSecret);

            List<INSBChannel> queryList = insbchannelservice.queryList(channel);
            if (queryList.size() == 0) {
                result.setRespCode("01");
                result.setErrorMsg("渠道信息不存在！");
                return result;
            }else{
                /**2016-10-19 15:57:30 chenjianglong add INSBChannel有记录还需要判断，是否有地区没被删除**/
                boolean deleteflag = true;
                for (INSBChannel insbChannel : queryList){
                    /**INSBChannel 有没被删除的子节点**/
                    if((null == insbChannel.getDeleteflag() || !"0".equals(insbChannel.getDeleteflag()))
                                && "0".equals(insbChannel.getChildflag())){
                        deleteflag = false;
                        break;
                    }
                }
                if(deleteflag){
                    result.setRespCode("01");
                    result.setErrorMsg("渠道信息不存在！");
                    return result;
                }
            }

            String token = CHNTokenGenerate.generateToken();
            long invalidTime = System.currentTimeMillis();
            redisClient.set(CHNChannelService.CHANNEL_MODULE, channelId, token + "_" + invalidTime, 7200);
            result.setAccessToken(token);
            result.setRespCode("00");
            result.setChannelId(channelId);
            result.setExpiresTime("7200");
        } catch (Exception e) {
            result.setRespCode("01");
            result.setChannelId(channelId);
            result.setErrorMsg(channelId + "获取token异常");
        }
        return result;
    }

    //获取供应商
    @Override
    public QuoteBean getProviders(String channelId, String insureAreaCode) {
        QuoteBean bean = new QuoteBean();
        ;
        try {
        	if (!channelchnQuoteService.hasInterfacePower("2", insureAreaCode, channelId)) { //2-供应商查询
        		bean.setRespCode("01");
        		bean.setErrorMsg("没有调用该接口的权限!");
                return bean;
            }
        	
            bean.setRespCode("00");
            bean.setChannelId(channelId);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("channelinnercode", channelId);
            map.put("city", insureAreaCode);
            List<INSBProvider> providers = provierService.queryProByInsureAreaCode2(map);
            List<ProviderBean> probeans = new ArrayList<ProviderBean>();
            if (providers != null && providers.size() > 0) {
                for (INSBProvider pro : providers) {
                    ProviderBean probean = new ProviderBean();
                    probean.setPrvId(pro.getPrvcode());
                    probean.setPrvName(pro.getPrvshotname());
                    probean.setBusinessType(StringUtil.isNotEmpty(pro.getBusinesstype()) ? pro.getBusinesstype() : "01");
                    probeans.add(probean);
                }
                bean.setProviders(probeans);
            } else {
                bean.setRespCode("01");
                bean.setErrorMsg("该地区暂未开放供应商");

            }
        } catch (Exception e) {
            LogUtil.info("获取供应商列表异常" + e.getStackTrace());
            bean.setRespCode("01");
            bean.setErrorMsg("获取供应商列表失败！");
        }
        return bean;
    }

    //查询车型
    public QuoteBean queryCarModelInfos(QuoteBean quoteBean) throws Exception {
        QuoteBean resultBean = new QuoteBean();
        resultBean.setRespCode("00");
        String vehicleName = quoteBean.getCarInfo().getVehicleName();
        String carLicenseNo = quoteBean.getCarInfo().getCarLicenseNo();
        
        Map<String, String> params = new HashMap<String, String>();
		params.put("flag", "2");
		params.put("vehicleName", vehicleName);
		params.put("pageSize", quoteBean.getPageSize());
		params.put("pageNumber", quoteBean.getPageNum());
		if ( StringUtil.isNotEmpty(carLicenseNo) && !"新车未上牌".equals(carLicenseNo) ) {
			params.put("vehicleno", carLicenseNo);
		}

		String result = CloudQueryUtil.jingYouCarModelSearchVin(params);
		LogUtil.info("queryCarModelInfos-jingYouCarModelSearchVin-reuslt:" + result);
		if( StringUtil.isEmpty(result) ) {
			resultBean.setRespCode("01");
			resultBean.setErrorMsg("没有数据返回，请查看参数是否有误");
			return resultBean;
		}
		
		JSONObject jsonObject = JSONObject.fromObject(result);
		JSONArray jsonArray = jsonObject.getJSONArray("List");
    	if ( jsonArray == null || jsonArray.isEmpty() ) {
    		resultBean.setTotalSize("0"); 
    		return resultBean;
    	}
		int totalCount = jsonObject.containsKey("RecTotal") ? jsonObject.getInt("RecTotal") : 0;     
    	resultBean.setTotalSize(String.valueOf(totalCount));
    	
    	List<CarModelInfoBean> carInfos = new ArrayList<CarModelInfoBean>();
		for(int i = 0; i < jsonArray.size(); i++){
			JSONObject object = jsonArray.getJSONObject(i);
			
			CarModelInfoBean carInfo = new CarModelInfoBean();
            carInfo.setVehicleCode(object.getString("vehiclecode"));
            carInfo.setVehicleName(object.getString("vehiclename"));
        	carInfo.setVehicleId(object.getString("vehicleId"));
        	carInfo.setGearbox(object.getString("gearbox"));
        	carInfo.setMaketDate(object.getString("maketdate"));
        	carInfo.setPrice(this.getFormat(object.getDouble("price")));  
        	carInfo.setSeat(object.getString("seat"));
        	carInfo.setTaxPrice(this.getFormat(object.getDouble("taxprice"))); 
        	carInfo.setYearStyle(object.getString("yearstyle"));
        	
            carInfos.add(carInfo);
		}
        
        resultBean.setCarModelInfos(carInfos);
        return resultBean;
    }

    //接收工作流回调
    public void callback(WorkFlow4TaskModel dataModel) {
        LogUtil.info(dataModel.getMainInstanceId() + "收到回调工作流通知" + JsonUtils.serialize(dataModel));
        String taskId = dataModel.getMainInstanceId();

        String taskCode = dataModel.getTaskCode();
        if ("14".equals(taskCode) || "20".equals(taskCode)) {
            genCommission(dataModel);
        }

        if ( !StatusCodeMapperUtil.callbackState.contains(taskCode) || 
        		(taskCode.equals("33") && StringUtil.isNotEmpty(dataModel.getSubInstanceId())) ) {
            LogUtil.info(taskId + "该状态不需要回调 -> " + taskCode);
            return;
        }
  
        if ("24".equals(taskCode)) {
        	INSBWorkflowmaintrack mainTrackCon = new INSBWorkflowmaintrack();
        	mainTrackCon.setInstanceid(taskId);
        	mainTrackCon.setTaskcode("27");
        	List<INSBWorkflowmaintrack> mainTracks = insbWorkflowmaintrackDao.selectList(mainTrackCon);
        	
        	if (mainTracks == null || mainTracks.isEmpty()) {
        		LogUtil.info(taskId + "是自动承保已回调23不回调24");
        		return;
        	}
        }

        taskthreadPool4workflow.execute(new Runnable() {
            @Override
            public void run() {
            	INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
                insbQuotetotalinfo.setTaskid(taskId);
            	insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
            	
                QuoteBean res = assembleCallbackInfo(dataModel, insbQuotetotalinfo);
                String deptCode = null;
                String taskState = res.getTaskState();
                String respCode = res.getRespCode();
                if (res.getDeptInfo() != null) {
                	deptCode = res.getDeptInfo().getDeptcode();
                }
                
                if ( !"99".equals(respCode) ) {
                	if (!"FirstPayLastInsure".equals(insbQuotetotalinfo.getSourceFrom())) { //旧流程
	                	setNotCallBackField(res);
	                }
	                String jsonStr = JsonUtils.serialize(res);
	                LogUtil.info(taskId + " " + insbQuotetotalinfo.getPurchaserChannel() + "回调的报文:" + jsonStr);
	                
	                String channelinnercode = insbQuotetotalinfo.getPurchaserChannel();
	                if ( StringUtil.isNotEmpty(channelinnercode) ) {
	                	INSBChannel insbChannel = new INSBChannel();
	                    insbChannel.setChannelinnercode(channelinnercode);
	                    insbChannel.setChildflag("1");
	                    String url = insbChannelDao.selectForCallBackURl(insbChannel);
	                    
	                    if (StringUtil.isEmpty(url)) {
	                        LogUtil.info(taskId + channelinnercode + "没有配置回调地址");
	                    } else {
		                	Scheduler sched = AbsSchedulerManager.getScheduler();
		                    String jobName = "channelcbk_" + taskId + "_" + taskCode + dataModel.getSubInstanceId();
		                    JobDetail job = JobBuilder.newJob(NotiJob.class).withIdentity(jobName)
		                            .usingJobData("url", url).usingJobData("json", jsonStr).build();
		                    Date startTime = DateBuilder.futureDate(0, DateBuilder.IntervalUnit.SECOND);
		                    SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(jobName).startAt(startTime).build();
		                    try {
		                        sched.scheduleJob(job, trigger);
		                    } catch (SchedulerException e) {
		                        e.printStackTrace();
		                    }
	                    }
	                } 
	                
	                String yblchannelinnercode = config.getString("youbaolian.channelinnercode");
	                if ( HEBEI_DEPTCODE.equals(deptCode) && ACCEPT_SUCCESS_TASKCODE.equals(taskState) && !yblchannelinnercode.equals(channelinnercode) ) { 
	                	INSBChannel insbChannel = new INSBChannel();
		                insbChannel.setChannelinnercode(yblchannelinnercode);
		                insbChannel.setChildflag("1");
		                String url = insbChannelDao.selectForCallBackURl(insbChannel);
		                
		                Scheduler sched = AbsSchedulerManager.getScheduler();
		                String jobName = "channelyblcbk_" + taskId + "_" + taskCode + dataModel.getSubInstanceId();
		                JobDetail job = JobBuilder.newJob(NotiJob.class).withIdentity(jobName)
		                        .usingJobData("url", url).usingJobData("json", jsonStr).build();
		                Date startTime = DateBuilder.futureDate(0, DateBuilder.IntervalUnit.SECOND);
		                SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(jobName).startAt(startTime).build();
		                try {
		                    sched.scheduleJob(job, trigger);
		                } catch (SchedulerException e) {
		                    e.printStackTrace();
		                }
	                }
                }
                
                if ( INSURE_SUCCESS_TASKCODE.equals(taskState) && HEBEI_DEPTCODE.equals(deptCode) ) {
	                try {
	                	insbThirdCommissionService.thirdCommission(dataModel, insbQuotetotalinfo); 
	                } catch (Exception e) {
	                	LogUtil.error(taskId + "thirdCommission-error:" + e.getMessage()); 
	                    e.printStackTrace();
	                }
                }
                
            }
        });
    }

    public void genCommission(WorkFlow4TaskModel dataModel) {
        try {
            Map<String, String> channelMap = insbChannelDao.querychanneltype(dataModel.getMainInstanceId());
            if (channelMap != null && ("02".equals(channelMap.get("channeltype")) || "03".equals(channelMap.get("channeltype")))) {
                LogUtil.info("genCommission channelMap " + JsonUtils.serialize(channelMap) + " dataModel " + JsonUtils.serialize(dataModel));
                AgentTaskModel agentTaskModel = new AgentTaskModel();
                agentTaskModel.setChannelid(channelMap.get("channelinnercode"));
                agentTaskModel.setTaskid(dataModel.getMainInstanceId());
                agentTaskModel.setProvidercode(dataModel.getProviderId());
                String taskCode = "";
                if ("20".equals(dataModel.getTaskCode())) {
                    taskCode = "insured";
                }else if ("14".equals(dataModel.getTaskCode())) {
                    taskCode = "quote";
                }
                agentTaskModel.setCommissionFlag(taskCode);
                chnCommissionRateService.queryTaskCommission(agentTaskModel);
            }
        } catch (Exception e) {
            LogUtil.info("genCommission error");
            e.printStackTrace();
        }
    }
    
    //报价单列表查询
    @Override
    public String queryList(QuoteBean quoteBean, String channelId) {
    	String channelUserId = quoteBean.getChannelUserId();
		String taskState = quoteBean.getTaskState();
		String taskId = quoteBean.getTaskId();
		ListQuoteBean result = new ListQuoteBean();
		
    	try {	
			if ( (StringUtil.isEmpty(channelUserId) || StringUtil.isEmpty(channelId)) && StringUtil.isEmpty(taskId) ) {
			    result.setRespCode("01");
			    result.setErrorMsg("请传渠道ID和渠道用户ID或者任务ID");
			    return JsonUtils.serialize(result);
			}
			
			String interfaceId = "15"; //queryList接口
            if (!channelchnQuoteService.hasInterfacePower(interfaceId, null, channelId)) { 
            	result.setRespCode("01");
            	result.setErrorMsg("没有调用该接口的权限!");
                return JsonUtils.serialize(result);
            }
			
			if (taskState == null) {
				taskState = "3"; //查询全部
			} else if ("14".equals(taskState)) {
				taskState = "1"; //待投保
			} else if ("20".equals(taskState)) {
				taskState = "2"; //待支付
			}
			
			String strPageNum = quoteBean.getPageNum();
			String strPageSize = quoteBean.getPageSize();
			int pageNum = (StringUtil.isNotEmpty(strPageNum) && !(strPageNum.equals("0"))) ? Integer.parseInt(strPageNum) : 1;
			int pageSize = StringUtil.isNotEmpty(strPageSize) ? Integer.parseInt(strPageSize) : 5;
			long offset = pageSize * (pageNum - 1);
			int limit = pageSize * pageNum;
			
			CommonModel commonModel = insbMyOrderService.showMyOrderForChn(null, null, null, taskState, limit,
			        offset, null, null, channelId, channelUserId, taskId);
			
			if ("success".equals(commonModel.getStatus())) {
				result.setRespCode("00");
			    result.setErrorMsg("queryList成功");
			    
			    Map<String, Object> bodyResult = (Map<String, Object>)commonModel.getBody();
			    long llTotaLnum = (long)bodyResult.get("totalnum");
			    List<Map<String, Object>> resultList = (List<Map<String, Object>>)bodyResult.get("resultlist");
			    
			    result.setTotalNum((int)llTotaLnum);
			    
			    List<QuoteBean> quoteBeanList = new ArrayList<QuoteBean>();
			    for (Map<String, Object> resultMap : resultList) {
			    	QuoteBean qBean = new QuoteBean();
			    	String mainTaskcode = (String)resultMap.get("taskcode");
			    	
			    	qBean.setTaskId((String)resultMap.get("processInstanceId"));
			    	qBean.setCreateTime((String)resultMap.get("createtime"));
			    	
			    	CarInfoBean carInfo = new CarInfoBean();
			    	carInfo.setCarLicenseNo((String)resultMap.get("carlicenseno"));
			    	qBean.setCarInfo(carInfo);
			    	
			    	PersonBean insured = new PersonBean();
			    	insured.setName((String)resultMap.get("insuredname"));   	
			    	String idcardno = (String)resultMap.get("idcardno");
			    	Integer idcardtype = (Integer)resultMap.get("idcardtype");
			    	if (StringUtil.isNotEmpty(idcardno) && idcardtype != null && idcardtype == 0) { //身份证模糊化
			    		insured.setIdcardNo(idcardno.substring(0, 10) + "****" + idcardno.substring(14));
			    	} else {
			    		insured.setIdcardNo(idcardno);
			    	}
			    	insured.setGender(String.valueOf(resultMap.get("gender")));
			    	insured.setIdcardType(String.valueOf(resultMap.get("idcardtype")));
			    	insured.setMobile((String)resultMap.get("cellphone"));
			    	qBean.setInsured(insured);
			    	
			    	List<Map<String, Object>> quoteInfos = (List<Map<String, Object>>)resultMap.get("quoteInfoList");
			    	if (quoteInfos == null || quoteInfos.isEmpty()) {
			    		quoteBeanList.add(qBean);
			    		continue;
			    	}
			    	
			    	List<ProviderBean> providers = new ArrayList<ProviderBean>();
			    	for (Map<String, Object> quoteInfo : quoteInfos) {
			    		ProviderBean providerBean = new ProviderBean();

			    		providerBean.setPrvId((String)quoteInfo.get("inscomcode"));
			    		String taskcode = (String)quoteInfo.get("taskcode");           		
			    		String tempTaskState = (String)quoteInfo.get("taskstate");
			            if ("end".equals(tempTaskState)) {
			            	taskcode = mainTaskcode;
			            }
			            
			            String prvStateCode = StatusCodeMapperUtil.states.get(taskcode);
			    		
			    		providerBean.setPrvCode(StatusCodeMapperUtil.stateDescription.get(prvStateCode));
			    		providerBean.setPrvStateCode(prvStateCode);
			    		providerBean.setPrvName((String)quoteInfo.get("prvshotname"));
			    		providerBean.setTotalPrice((String)quoteInfo.get("quoteamount"));
			    		
			    		QuoteBean quoteBeanImg = new QuoteBean();
			    		this.getImageInfos(quoteBeanImg, prvStateCode, false);
			    		providerBean.setMsgType(quoteBeanImg.getMsgType());
			    		providerBean.setImageInfos(quoteBeanImg.getImageInfos());
			    		
			    		if (StringUtil.isNotEmpty(taskId)) {
							Date payValidDate = getPayValidDate(taskId, providerBean.getPrvId(), false, true);
							Date quoteValidDate = getQuoteValidDate(taskId, providerBean.getPrvId(), 
								 								 	 (String)quoteInfo.get("workflowinstanceid"), false, true);
							if (payValidDate != null) providerBean.setPayValidTime(ModelUtil.conbertToStringsdf(payValidDate));
							if (quoteValidDate != null) providerBean.setQuoteValidTime(ModelUtil.conbertToStringsdf(quoteValidDate));							
							
							providerBean.setMsg( getComment(taskId, (String)quoteInfo.get("workflowinstanceid"), 
									providerBean.getPrvId(), taskcode, tempTaskState, true) );
			    		}
			    		
			    		providers.add(providerBean);
			    	}
			    	qBean.setProviders(providers);
			    	
			    	quoteBeanList.add(qBean);
			    }
			    
			    result.setQuoteBeanList(quoteBeanList);
			} else {
				result.setRespCode("01");
			    result.setErrorMsg("queryList失败showMyOrderForChn");
			}
			
		} catch (Exception e) {
			LogUtil.error("queryList异常：" + e.getMessage());
			e.printStackTrace();
			result.setRespCode("01");
		    result.setErrorMsg("queryList异常：" + e.getMessage());
		}
    	return JsonUtils.serialize(result);
    }

    //比queryTask更详细的出参信息
    @Override
    public QuoteBean query(QuoteBean quoteBean, String channelId) {
        QuoteBean result = new QuoteBean();
        if (StringUtil.isEmpty(quoteBean.getTaskId()) || StringUtil.isEmpty(channelId) || StringUtil.isEmpty(quoteBean.getPrvId())) {
            result.setRespCode("01");
            result.setErrorMsg("taskId 和渠道ID 不能为空");
            return result;
        }
        String taskId = quoteBean.getTaskId();
        String comcode = quoteBean.getPrvId();
        result = getTaskQueryByTaskIdAndDeptId(taskId, comcode, true);
        if (result.getRespCode().equals("00")) {
            getCarLicense(result, taskId, comcode, true);
            getApplicantPerson(result, taskId);
            getBeneficiaryPerson(result, taskId);
            getInsuredPerson(result, taskId);
        }

        return result;
    }
    
    //检查核保补充数据项是否已经在其它表存在
    private boolean checkInsureSupply(INSBPerson insbCarownerPerson, INSBPerson insbApplicantPerson, 
    		INSBPerson insbInsuredPerson, INSBPerson insbLegalrightclaimPerson, INSBInsuresupplyparam insbInsuresupplyparam) {
    	
    	switch (insbInsuresupplyparam.getItemcode()) {
	        case "ownerMobile": //补充信息.车主手机号码
	        	if ( StringUtil.isNotEmpty(insbCarownerPerson.getCellphone()) ) {
	        		insbInsuresupplyparam.setItemvalue(insbCarownerPerson.getCellphone());
	        	}
	            break;
	        case "ownerEmail": //补充信息.车主邮箱
	        	if ( StringUtil.isNotEmpty(insbCarownerPerson.getEmail()) ) {
	        		insbInsuresupplyparam.setItemvalue(insbCarownerPerson.getEmail()); 
	        	}
	            break;
	        case "ownerAddress": //补充信息.车主身份证地址
	        	//if ( StringUtil.isNotEmpty(insbCarownerPerson.getAddress()) ) {
	        	//	insbInsuresupplyparam.setItemvalue(insbCarownerPerson.getAddress()); 
	        	//}
	            break;
	        case "insuredMobile": //补充信息.被保人手机号码
	        	if ( StringUtil.isNotEmpty(insbInsuredPerson.getCellphone()) ) {
	        		insbInsuresupplyparam.setItemvalue(insbInsuredPerson.getCellphone());
	        	}
	            break;
	        case "insuredEmail": //补充信息.被保人邮箱
	        	if ( StringUtil.isNotEmpty(insbInsuredPerson.getEmail()) ) {
	        		insbInsuresupplyparam.setItemvalue(insbInsuredPerson.getEmail());
	        	}
	            break;
	        case "insuredAddress": //补充信息.被保人身份证地址
	        	//if ( StringUtil.isNotEmpty(insbInsuredPerson.getAddress()) ) {
	        	//	insbInsuresupplyparam.setItemvalue(insbInsuredPerson.getAddress());
	        	//}
	            break;
	        case "applicantMobile": //补充信息.投保人手机号码
	        	if ( StringUtil.isNotEmpty(insbApplicantPerson.getCellphone()) ) {
	        		insbInsuresupplyparam.setItemvalue(insbApplicantPerson.getCellphone());
	        	}
	            break;
	        case "applicantEmail": //补充信息.投保人邮箱
	        	if ( StringUtil.isNotEmpty(insbApplicantPerson.getEmail()) ) {
	        		insbInsuresupplyparam.setItemvalue(insbApplicantPerson.getEmail());
	        	}                
	            break;
	        case "applicantAddress": //补充信息.投保人身份证地址
	        	//if ( StringUtil.isNotEmpty(insbApplicantPerson.getAddress()) ) {
	        	//	insbInsuresupplyparam.setItemvalue(insbApplicantPerson.getAddress());
	        	//}
	            break;
	        case "drivingLicenseAddress": //补充信息.行驶证地址
	        	                   
	            break;
	        case "claimantName": //补充信息.权益索赔人姓名
	        	if ( StringUtil.isNotEmpty(insbLegalrightclaimPerson.getName()) ) {
	        		insbInsuresupplyparam.setItemvalue(insbLegalrightclaimPerson.getName());
	        	}                  
	            break;
	        case "claimantDocumentType": //补充信息.权益索赔人证件类型
	        	if ( StringUtil.isNotEmpty(insbLegalrightclaimPerson.getIdcardtype() + "") ) {
	        		insbInsuresupplyparam.setItemvalue(insbLegalrightclaimPerson.getIdcardtype() + "");
	        	}                    
	            break;
	        case "claimantDocumentNumber": //补充信息.权益索赔人证件号码
	        	if ( StringUtil.isNotEmpty(insbLegalrightclaimPerson.getIdcardno()) ) {
	        		insbInsuresupplyparam.setItemvalue(insbLegalrightclaimPerson.getIdcardno());
	        	}                   
	            break;
	        case "claimantMobile": //补充信息.权益索赔人手机号码
	        	if ( StringUtil.isNotEmpty(insbLegalrightclaimPerson.getCellphone()) ) {
	        		insbInsuresupplyparam.setItemvalue(insbLegalrightclaimPerson.getCellphone());
	        	}                   
	            break;
	        case "claimantEmail": //补充信息.权益索赔人邮箱
	        	if ( StringUtil.isNotEmpty(insbLegalrightclaimPerson.getEmail()) ) {
	        		insbInsuresupplyparam.setItemvalue(insbLegalrightclaimPerson.getEmail());
	        	}                 
	            break;
	        default: 
	            break;
	    }
    	if ( StringUtil.isNotEmpty(insbInsuresupplyparam.getItemvalue()) ) {
    		insbInsuresupplyparamDao.updateById(insbInsuresupplyparam);
    		return false;
    	} 
    		
    	return true;
    }
    
    //取得影像信息 status:1-核保退回 2-提交核保
    @Override
	public boolean getImageInfos(QuoteBean quoteBean, String taskCode) {
    	return getImageInfos(quoteBean, taskCode, true);
    }
    
    //取得影像信息 status:1-核保退回 2-提交核保
    @Override
	public boolean getImageInfos(QuoteBean quoteBean, String taskCode, boolean queryAlread) {
		boolean needUploadImg = false;
		String status = "";
        if ("19".equals(taskCode)) { //核保退回修改
        	status = "1";
        } else if ("14".equals(taskCode)) { //报价成功
        	status = "2";
        }
        if (status == "") return needUploadImg;
        		
    	String taskId = quoteBean.getTaskId();
    	String prvId = quoteBean.getPrvId();
    	
    	ExtendCommonModel cmNeed = insuredQuoteService.needUploadImageByCodeType(taskId, prvId, status);
    	LogUtil.info(status + "," + taskId + "," + prvId + "需要上传影像:" + JsonUtils.serialize(cmNeed));

    	CommonModel cmAlready = new CommonModel();
    	cmAlready.setStatus(CommonModel.STATUS_SUCCESS);
		cmAlready.setBody(new ArrayList<Map<String, String>>());
    	/* if (queryAlread) {
    		cmAlready = insuredQuoteService.alreadyUploadImage(taskId);
    	} else {
    		cmAlready.setStatus(CommonModel.STATUS_SUCCESS);
    		cmAlready.setBody(new ArrayList<Map<String, String>>());
    	}
    	LogUtil.info(queryAlread + "," + taskId + "已经上传影像:" + JsonUtils.serialize(cmAlready)); */
    	
    	if (CommonModel.STATUS_SUCCESS.equals(cmAlready.getStatus()) && 
    			CommonModel.STATUS_SUCCESS.equals(cmNeed.getStatus())) {
    		
			//List<Map<String, String>> listAl = (List<Map<String, String>>) cmAlready.getBody();
			//Map<String, Map<String, String>> mapAls = new HashMap<String, Map<String, String>>(); 
    		List<InsuranceImageBean> listNeed = (List<InsuranceImageBean>) cmNeed.getBody();
    		if (cmNeed.getExtend() != null) {
    			List<InsureSupplyBean> insureSupplyBeans = new ArrayList<InsureSupplyBean>();
    			List<INSBInsuresupplyparam> insbInsuresupplyparams = (List<INSBInsuresupplyparam>) cmNeed.getExtend();
    			
    	        INSBPerson insbCarownerPerson = insbPersonDao.selectCarOwnerPersonByTaskId(taskId); 
    	        INSBPerson insbApplicantPerson = insbPersonDao.selectApplicantPersonByTaskId(taskId);
    	        INSBPerson insbInsuredPerson = insbPersonDao.selectInsuredPersonByTaskId(taskId);
    	        INSBPerson insbLegalrightclaimPerson = insbPersonDao.selectBeneficiaryByTaskId(taskId);
    			
    			for (INSBInsuresupplyparam insbInsuresupplyparam : insbInsuresupplyparams) {
    				boolean checkFlag = checkInsureSupply(insbCarownerPerson, insbApplicantPerson, 
    						insbInsuredPerson, insbLegalrightclaimPerson, insbInsuresupplyparam);
    				if (checkFlag) {
	    				InsureSupplyBean insureSupplyBean = new InsureSupplyBean();
	    				insureSupplyBean.setItemCode(insbInsuresupplyparam.getItemcode());
	    				insureSupplyBean.setItemName(insbInsuresupplyparam.getItemname());
	    				insureSupplyBean.setItemInputType(insbInsuresupplyparam.getIteminputtype());
	    				insureSupplyBean.setItemOptions(insbInsuresupplyparam.getItemoptions());
	    				insureSupplyBeans.add(insureSupplyBean);
    				}
    			}
    			
    			if (!insureSupplyBeans.isEmpty()) {
        			needUploadImg = true;
        			quoteBean.setMsgType("01"); 
        			quoteBean.setInsureSupplys(insureSupplyBeans); 
        		}
    		}
    		
    		//for (Map<String, String> mapAl : listAl) {
    		//	mapAls.put(mapAl.get("filetype"), mapAl);
    		//}
    		
    		List<ImageInfoBean> listResult = new ArrayList<ImageInfoBean>();
    		for (InsuranceImageBean needImage : listNeed) {
    			String riskimgtype = needImage.getRiskimgtype();
    			String riskimgtypename = needImage.getRiskimgtypename();
    			String codetype = needImage.getCodetype();
    			
    			ImageInfoBean resultBean = new ImageInfoBean();
    			resultBean.setImageName(riskimgtypename);
    			
    			if (StringUtil.isNotEmpty(riskimgtype)) {
    				resultBean.setImageType(riskimgtype);  				
    			} else {
    				INSCCode inscCode = new INSCCode();
    				inscCode.setParentcode("insuranceimage");
    				inscCode.setCodetype(codetype);
    				List<INSCCode> inscCodeList = inscCodeService.queryList(inscCode);
    				
    				if (inscCodeList != null && !inscCodeList.isEmpty()) {
    					resultBean.setImageType(inscCodeList.get(0).getCodevalue()); 
    				}
    			}
    			
    			/* if (mapAls.get(codetype) != null) {
    				resultBean.setUpload("Y");
    			} else {
    				resultBean.setUpload("N");
    				needUploadImg = true;
    			} */
    			
    			listResult.add(resultBean);
    		}
    		
    		/* if (needUploadImg) {
    			quoteBean.setMsgType("01"); 
    			quoteBean.setImageInfos(listResult);
    		} */
    		if (!listResult.isEmpty()) {
    			needUploadImg = true;
    			quoteBean.setMsgType("01"); 
    			quoteBean.setImageInfos(listResult);
    		}
    	}
    	
    	return needUploadImg;
    }

    //组装回调信息
    public QuoteBean assembleCallbackInfo(WorkFlow4TaskModel dataModel, INSBQuotetotalinfo quotetotalinfo) {
        QuoteBean result = new QuoteBean();
        String taskId = dataModel.getMainInstanceId();
        String subInstanceId = dataModel.getSubInstanceId();
        String providerId = dataModel.getProviderId();
        try {
            String taskCode;
            String taskCodeDesc;
            result.setTaskId(taskId);
            if ("FirstPayLastInsure".equals(quotetotalinfo.getSourceFrom())) {// 新旧流程状态分开映射
            	taskCode = StatusCodeMapperUtil.newStates.get(dataModel.getTaskCode());
                taskCodeDesc = StatusCodeMapperUtil.newStateDes.get(taskCode);
            } else {
            	taskCode = StatusCodeMapperUtil.states.get(dataModel.getTaskCode());
                taskCodeDesc = StatusCodeMapperUtil.stateDescription.get(taskCode);
            }
            result.setChannelId(quotetotalinfo.getPurchaserChannel());
            result.setChannelUserId(quotetotalinfo.getPurchaserid());
            result.setTaskState(taskCode);
            result.setTaskStateDescription(taskCodeDesc);
            if ( StringUtil.isEmpty(dataModel.getSubInstanceId()) && StringUtil.isEmpty(dataModel.getProviderId()) ) {
                INSBWorkflowsub workflowsub = new INSBWorkflowsub();
                workflowsub.setMaininstanceid(taskId);
                workflowsub.setTaskcode("33");
                workflowsub = insbWorkflowsubDao.selectOne(workflowsub);
                if ( workflowsub != null ) {
                	subInstanceId = workflowsub.getInstanceid();
                } else {
                	LogUtil.info(taskId + "没查询出33的workflowsub");
                	result.setRespCode("99"); //99-不回调给渠道
                	result.setErrorMsg(taskId + "没查询出33的workflowsub"); 
                	return result;
                }
            }
         
            if (StringUtil.isEmpty(providerId)) {
                INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
                quoteinfo.setWorkflowinstanceid(subInstanceId);
                quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
                
                int i = 1;
                while (quoteinfo == null) {
                	try {
        				Thread.sleep(i * 1000);
        			} catch (InterruptedException e) {
        				LogUtil.error("assembleCallbackInfo-Thread.sleep异常：" + e.getMessage());
        				e.printStackTrace();
        			}
                	
                	LogUtil.info("assembleCallbackInfo第" + i + "次查询quoteinfo：" + subInstanceId);
                	quoteinfo = new INSBQuoteinfo();
                    quoteinfo.setWorkflowinstanceid(subInstanceId);
                	quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
                	i++;
                	if (i >= 4) break;
                }
                
                if (quoteinfo == null) {
                	result.setRespCode("01");
                    result.setErrorMsg("查询不到quoteinfo：" + subInstanceId);
                    return result;
                } else {
                	providerId = quoteinfo.getInscomcode();
                }
            }
            
            INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
    		quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
    		quoteinfo.setInscomcode(providerId);
            quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
    		
            INSCDept inscDept = inscDeptService.queryById(quoteinfo.getDeptcode());
            String[] parentcodes = inscDept.getParentcodes().split("[+]");
            LogUtil.info(taskId + providerId + "callback-parentcodes[3]:" + parentcodes[3]);
            //查询平台机构
            inscDept = inscDeptService.queryById(parentcodes[3]);
            DeptInfoBean deptInfoBean = new DeptInfoBean();
            deptInfoBean.setJobnum(quotetotalinfo.getAgentnum());
            deptInfoBean.setDeptcode(parentcodes[3]);
            deptInfoBean.setComname(inscDept.getComname());
            result.setDeptInfo(deptInfoBean);
            
            if ( StringUtil.isNotEmpty(quotetotalinfo.getSourceFrom()) || 
            		(HEBEI_DEPTCODE.equals(parentcodes[3]) && ACCEPT_SUCCESS_TASKCODE.equals(taskCode)) ) {
            	LogUtil.info(taskId + "是渠道的单");
            } else {
            	result.setRespCode("99"); //99-不回调
            	LogUtil.info(taskId + "不是渠道的单不需要回调"); 
            	return result;
            }
            
            result.setPrvId(providerId);
            result.setErrorMsg(getComment(taskId, subInstanceId, providerId, dataModel.getTaskCode(), dataModel.getTaskStatus(), false));
            INSBProvider provider = provierService.queryByPrvcode(providerId.substring(0, 4));
            result.setPrvName(provider.getPrvshotname());
            
            if ("33".equals(taskCode)) {
            	DeliveryBean deliveryBean = getDelivery(taskId, providerId, quotetotalinfo);
            	int i = 1;
                while ( deliveryBean == null || (!"2".equals(deliveryBean.getDeliveryType()) && 
               		 StringUtil.isEmpty(deliveryBean.getExpressNo())) ) {
                	try {
        				Thread.sleep(i * 1000);
        			} catch (InterruptedException e) {
        				LogUtil.error("assembleCallbackInfo-getDelivery-Thread.sleep异常：" + e.getMessage());
        				e.printStackTrace();
        			}
                	
                	LogUtil.info("assembleCallbackInfo第" + i + "次查询delivery：" + taskId + "," + providerId);
                	deliveryBean = getDelivery(taskId, providerId, quotetotalinfo);
                	i++;
                	if (i >= 4) break;
                }
                result.setDelivery(deliveryBean);
                if ( deliveryBean == null || (!"2".equals(deliveryBean.getDeliveryType()) && 
                		 StringUtil.isEmpty(deliveryBean.getExpressNo())) ) {
                	result.setRespCode("01");
                    result.setErrorMsg("查询配送信息失败：" + taskId + "," + providerId);
                    return result;
                }
            }
            
            InsureInfoBean infoBean = new InsureInfoBean();
            INSBPolicyitem pitem = new INSBPolicyitem();
            pitem.setTaskid(taskId);
            pitem.setInscomcode(providerId);
            
            List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectList(pitem);

            if ( !"13".equals(taskCode) ) {
	            Double discountCharge = null; 
	            for (INSBPolicyitem piIt : policyitemList) {
	            	if ( "0".equals(piIt.getRisktype()) ) { //商业险
	            		discountCharge = piIt.getDiscountCharge();
	            		break;
	            	}
	            }
	            int i = 1;
	            while (discountCharge == null) {
	            	try {
	    				Thread.sleep(i * 1000);
	    			} catch (InterruptedException e) {
	    				LogUtil.error("assembleCallbackInfo-discountCharge-Thread.sleep异常：" + e.getMessage());
	    				e.printStackTrace();
	    			}
	            	
	            	LogUtil.info("assembleCallbackInfo第" + i + "次查询discountCharge：" + taskId + "," + providerId);
	            	policyitemList = insbPolicyitemDao.selectList(pitem);
	            	for (INSBPolicyitem piIt : policyitemList) {
	                	if ( "0".equals(piIt.getRisktype()) ) { //商业险
	                		discountCharge = piIt.getDiscountCharge();
	                		break;
	                	}
	                }
	            	i++;
	            	if (i >= 3) break;
	            }
	            if (discountCharge == null) {
	            	LogUtil.info("查询不到discountCharge：" + taskId + "," + providerId); 
	            } 
            }

            //险种信息
            INSBCarkindprice carprice = new INSBCarkindprice();
            carprice.setTaskid(taskId);
            carprice.setInscomcode(providerId);
            getCarLicense(result, taskId, providerId, true);
            
            getApplicantPerson(result, taskId);
            getBeneficiaryPerson(result, taskId);
            getInsuredPerson(result, taskId);
            
            result.setInsureAreaCode(quotetotalinfo.getInscitycode());
            getImageInfos(result, taskCode, false); //影像信息

            //支付有效期 起始时间点
            Date syStartDate = null, jqStartDate = null;
            List<INSBCarkindprice> carkindpriceList = insbCarkindpriceDao.selectList(carprice); //险种列表
            if (policyitemList != null && policyitemList.size() > 0) {
                // 交强险保额
                double efcAmount = 0.0000d;
                // 交强险保费
                double efcPremium = 0.0000d;
                // 车船税金额
                double taxAmount = 0.0000d;
                // 车船税滞纳金
                double taxLateFee = 0.0000d;
                // 车船税
                BaseInsureInfoBean taxInsureInfoBean = null;
                for (INSBPolicyitem policyitem : policyitemList) {
                    if ("1".equals(policyitem.getRisktype())) {
                        // 交强险
                        jqStartDate = policyitem.getStartdate();
                        BaseInsureInfoBean baseInsureInfoBean = new BaseInsureInfoBean();
                        if (policyitem.getStartdate() != null) {
                            baseInsureInfoBean.setStartDate(ModelUtil.conbertToString(policyitem.getStartdate()));
                        }
                        if (policyitem.getEnddate() != null) {
                            baseInsureInfoBean.setEndDate(ModelUtil.conbertToString(policyitem.getEnddate()));
                        }
                        for (INSBCarkindprice carkindprice : carkindpriceList) {
                            if ("2".equals(carkindprice.getInskindtype())) {
                                if (carkindprice.getAmount() != null) {
                                    efcAmount += douFormat(carkindprice.getAmount());
                                }
                                if (carkindprice.getDiscountCharge() != null) {
                                    efcPremium += douFormat(carkindprice.getDiscountCharge());
                                }
                            } else if ("3".equals(carkindprice.getInskindtype())) {
                                if (null == taxInsureInfoBean) {
                                    taxInsureInfoBean = new BaseInsureInfoBean();
                                }
                                if ("VehicleTax".equals(carkindprice.getInskindcode())) {
                                    // 车船税金额
                                    if (carkindprice.getDiscountCharge() != null) {
                                        taxAmount += douFormat(carkindprice.getDiscountCharge());
                                    }
                                } else {
                                    // 车船税滞纳金
                                    if (carkindprice.getDiscountCharge() != null) {
                                        taxLateFee += douFormat(carkindprice.getDiscountCharge());
                                    }
                                }
                            }
                        }
                        //车船税
                        if (taxInsureInfoBean != null) {

                            taxInsureInfoBean.setTaxFee(getFormat(taxAmount));
                            taxInsureInfoBean.setLateFee(getFormat(taxLateFee));
                            infoBean.setTaxInsureInfo(taxInsureInfoBean);

                        }
                        //交强险
                        if (StringUtil.isNotEmpty(policyitem.getPolicyno()))
                            baseInsureInfoBean.setPolicyNo(policyitem.getPolicyno());
                        if (efcAmount > 0) {
                        	baseInsureInfoBean.setAmount("1.00");
                        } else {
                        	baseInsureInfoBean.setAmount(getFormat(efcAmount));
                        }
                        baseInsureInfoBean.setPremium(getFormat(efcPremium));
                        if (null != policyitem.getTotalepremium()) {
                            infoBean.setTotalPremium(policyitem.getTotalepremium().toString());
                        }
                        //2016-10-18 10:01:04 chenjianglong 新增交强险折扣率
                        if(null != policyitem.getDiscountRate()){
                            baseInsureInfoBean.setDiscountRate(getFormat4(policyitem.getDiscountRate()));
                        }
                        infoBean.setEfcInsureInfo(baseInsureInfoBean);
                    } else {
                        syStartDate = policyitem.getStartdate();
                        getBizInsureInfo(infoBean, policyitem, carkindpriceList, null);
                    }

                }
            }
            
            boolean newFlowQuoteValidDate = false;
            if ("19".equals(taskCode) && "FirstPayLastInsure".equals(quotetotalinfo.getSourceFrom())) {
            	newFlowQuoteValidDate = true;
            }

            if ("14".equals(taskCode) || newFlowQuoteValidDate) {
                Date quoteDate = getQuoteValidDate(taskId, subInstanceId, providerId, syStartDate, jqStartDate, false);
                if (null != quoteDate) {
                    result.setQuoteValidTime(ModelUtil.conbertToStringsdf(quoteDate));
                }
            }

            if("20".equals(taskCode)) {
                Date payDate = getPayValidDate(taskId, providerId, syStartDate, jqStartDate, false);
                if (null != payDate)
                    result.setPayValidTime(ModelUtil.conbertToStringsdf(payDate));
                
                result.setMsgType("00");
                result.setErrorMsg("请在" + result.getPayValidTime() + "前完成支付");
            }
            if ("21".equals(taskCode) && !channelchnQuoteService.isWorkTime(taskId, providerId)) { //21-支付确认中
            	result.setMsgType("00");
            	result.setErrorMsg("您好，现在是非工作时间，您的支付将在上班后第一时间为您确认。感谢您的支持！");
            }
            result.setRespCode("00");
            result.setInsureInfo(infoBean);

            String jsonStr = JsonUtils.serialize(result);
            LogUtil.info(taskId + "全量回调报文：" + jsonStr);
            
        } catch (Exception e) {
            LogUtil.error(taskId + "回调查询异常：" + e.getMessage());
            e.printStackTrace();
            result.setRespCode("01");
            result.setErrorMsg(taskId + "该任务回调异常：" + subInstanceId + e.getMessage());
        }

        return result;
    }
    
    //设置不回调的字段
    private void setNotCallBackField(QuoteBean result) {
    	result.setDeptInfo(null);
    	
    	//车辆信息
    	CarInfoBean carInfoBean = result.getCarInfo();
    	result.setInsureAreaCode(null);
    	carInfoBean.setIsNew(null);
    	carInfoBean.setEngineNo(null);
    	carInfoBean.setVinCode(null);
    	carInfoBean.setRegistDate(null);
    	carInfoBean.setIsTransferCar(null);
    	carInfoBean.setTransferDate(null);
    	//车辆信息-车型
    	carInfoBean.setVehicleId(null);
    	carInfoBean.setPrice(null);
    	carInfoBean.setProperty(null);
    	carInfoBean.setModelLoads(null);
    	carInfoBean.setPurpose(null);
    	carInfoBean.setSeat(null);
    	carInfoBean.setFullWeight(null);
    	carInfoBean.setDrivingArea(null);
    	carInfoBean.setDisplacement(null);
    	carInfoBean.setPolicycarprice(null); //投保车价
    	carInfoBean.setGearbox(null); //车型配置
    	carInfoBean.setYearPrice(null); //年款                   	
    	carInfoBean.setVehicleTypeName(null); //车辆种类
    	result.setCarInfo(carInfoBean);
    	
    	result.setApplicant(null);
    	result.setBeneficiary(null);
    	result.setInsured(null);
    	
    	PersonBean carOwner = result.getCarOwner();
    	carOwner.setIdcardNo(null);
    	carOwner.setIdcardType(null);
    	carOwner.setEmail(null); 
    	result.setCarOwner(carOwner);
    }

    //图像识别
    @Override
    public QuoteBean recognizeImage(HttpServletRequest request, QuoteBean bean) {
        ImageInfoBean imageInfo = bean.getImageInfo();
        QuoteBean quoteBean = new QuoteBean();
        INSCCode icode = new INSCCode();
        icode.setCodevalue(imageInfo.getImageType());
        icode.setParentcode("insuranceimage");
        INSCCode queryOne = insccodeservice.queryOne(icode);
        if (!(queryOne.getCodename().contains("身份证") || queryOne.getCodename().contains("行驶证"))) {
            quoteBean.setRespCode("01");
            quoteBean.setErrorMsg("只可识别行驶证和身份证");
            return quoteBean;
        }
        
        String powerId = "";
        if (queryOne.getCodename().contains("身份证")) {
        	powerId = "8"; //8-身份证影像识别
        } else if (queryOne.getCodename().contains("行驶证")) {
        	powerId = "7"; //7-行驶证影像识别
        }
        if (!channelchnQuoteService.hasInterfacePower(powerId, null, bean.getChannelId())) { 
   		 	quoteBean.setRespCode("01");
            quoteBean.setErrorMsg("没有调用该接口的权限!");
            return quoteBean;
        }
        
        Map<String, Object> map = null;
        if (!StringUtil.isEmpty(imageInfo.getImageUrl()))
            map = insbFilelibraryService.uploadOneFileByUrl(request, imageInfo.getImageUrl(), "影像识别文件." + imageInfo.getImageMode(), queryOne.getCodetype(), "影像识别文件", "admin");
        if (!StringUtil.isEmpty(imageInfo.getImageContent()) && (map == null || map.get("status").equals("fail")))
            map = insbFilelibraryService.uploadOneFile(request, imageInfo.getImageContent(), "影像识别文件." + imageInfo.getImageMode(), queryOne.getCodetype(), "影像识别文件", "admin");
        INSBFilelibrary insbFilelibrary = insbFilelibraryService.queryById((String) map.get("fileid"));
        String filephsicypath = insbFilelibrary.getFilephysicalpath();
        try {
            if (queryOne.getCodename().contains("行驶证")) {
                DrivingEntity dri = ocrClient.ocrDriving(filephsicypath);
                if (dri == null) {
                    quoteBean.setRespCode("01");
                    quoteBean.setErrorMsg("行驶证识别失败");
                    String[] list = new String[1];
                    list[0] = insbFilelibrary.getId();
                    insbFilelibraryService.deleteFileLibraryData(list);
                    return quoteBean;
                }
                DrivingInfoBean drivingInfo = new DrivingInfoBean();
                drivingInfo.setCarLicenseNo(dri.getCardno());
                drivingInfo.setName(dri.getName());
                drivingInfo.setAddress(dri.getAddress());
                drivingInfo.setVehicleType(dri.getVehicleType());
                drivingInfo.setCarProperty(dri.getUseCharacte());
                drivingInfo.setVehicleName(dri.getModel());
                drivingInfo.setVinCode(dri.getVin());
                drivingInfo.setEngineNo(dri.getEnginePN());
                drivingInfo.setRegistDate(dri.getRegisterDate());
                drivingInfo.setIssuedate(dri.getIssuedate());
                quoteBean.setRespCode("00");
                quoteBean.setDrivingInfo(drivingInfo);
            }
            if (queryOne.getCodename().contains("身份证")) {
                IDCardEntity idcard = ocrClient.ocrIdCard(filephsicypath);
                if (idcard == null) {
                    quoteBean.setRespCode("01");
                    quoteBean.setErrorMsg("身份证识别失败");
                    String[] list = new String[1];
                    list[0] = insbFilelibrary.getId();
                    insbFilelibraryService.deleteFileLibraryData(list);
                    return quoteBean;
                }
                PersonBean personInfo = new PersonBean();
                if (StringUtil.isNotEmpty(idcard.getName())) personInfo.setName(idcard.getName()); 
                if (StringUtil.isNotEmpty(idcard.getCardno())) personInfo.setIdcardNo(idcard.getCardno());
                if (StringUtil.isNotEmpty(idcard.getSex())) personInfo.setGender(idcard.getSex());
                if (StringUtil.isNotEmpty(idcard.getFolk())) personInfo.setFolk(idcard.getFolk());
                if (StringUtil.isNotEmpty(idcard.getBirthday())) personInfo.setBirthday(idcard.getBirthday());
                if (StringUtil.isNotEmpty(idcard.getAddress())) personInfo.setAddress(idcard.getAddress());
                
                if (StringUtil.isNotEmpty(idcard.getIssueAuthority())) personInfo.setIssueAuthority(idcard.getIssueAuthority());
                if (StringUtil.isNotEmpty(idcard.getValidPeriod())) personInfo.setValidPeriod(idcard.getValidPeriod());
                
                quoteBean.setRespCode("00");
                quoteBean.setPersonInfo(personInfo);
            }
        } catch (IOException | JAXBException e) {
            quoteBean.setRespCode("01");
            quoteBean.setErrorMsg("识别失败");
            String[] list = new String[1];
            list[0] = insbFilelibrary.getId();
            insbFilelibraryService.deleteFileLibraryData(list);
            return quoteBean;
        }
        quoteBean.setRespCode("00");
        String[] list = new String[1];
        list[0] = insbFilelibrary.getId();
        insbFilelibraryService.deleteFileLibraryData(list);
        return quoteBean;
    }

    //上传影像
    @Override
    public QuoteBean uploadImage(HttpServletRequest request, QuoteBean quoteBean) {
        List<ImageInfoBean> imageInfos = quoteBean.getImageInfos();
        QuoteBean result = new QuoteBean();
        result.setRespCode("00");
        
        INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
        insbQuotetotalinfo.setTaskid(quoteBean.getTaskId());
        insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
        if (!channelchnQuoteService.hasInterfacePower("6", insbQuotetotalinfo.getInscitycode(), quoteBean.getChannelId())) { //6-提交影像文件
        	result.setRespCode("01");
        	result.setErrorMsg("没有调用该接口的权限!");
            return result;
        }
        
        for (int i = 0; i < imageInfos.size(); i++) {
            ImageInfoBean imageInfoBean = imageInfos.get(i);
            String taskId = quoteBean.getTaskId();
            INSCCode queryOne = new INSCCode();
            queryOne.setCodetype("otherimage");
            queryOne.setCodename("其它");
            /* INSCCode icode = new INSCCode();
            icode.setCodevalue(imageInfoBean.getImageType());
            icode.setParentcode("insuranceimage");
            INSCCode queryOne = insccodeservice.queryOne(icode);
            //如果有重复的数据，先删除
            if (!StringUtil.isEmpty(taskId)) {
                INSBFilebusiness param = new INSBFilebusiness();
                param.setCode(taskId);
                param.setType(queryOne.getCodetype());
                List<INSBFilebusiness> selectList = insbFilebusinessDao.selectList(param);
                if (selectList.size() > 0) {
                    List<String> list = new ArrayList<String>();
                    String[] list1 = new String[selectList.size()];
                    for (int j = 0; j < selectList.size(); j++) {
                        INSBFilebusiness insbFilebusiness = selectList.get(j);
                        list1[j] = insbFilebusiness.getFilelibraryid();
                        list.add(insbFilebusiness.getFilelibraryid());
                    }
                    insbFilelibraryService.deleteFileLibraryData(list1);
                    insbFilebusinessDao.deleteByIdInBatch(list);
                }
            } */
            Map<String, Object> map = null;
            if (StringUtil.isNotEmpty(imageInfoBean.getImageUrl()))
                map = insbFilelibraryService.uploadOneFileByUrl(request, imageInfoBean.getImageUrl(), "影像上传." + imageInfoBean.getImageMode(), queryOne.getCodetype(), queryOne.getCodename(), "admin");
            if (StringUtil.isNotEmpty(imageInfoBean.getImageContent()) && (map == null || map.get("status").equals("fail")))
                map = insbFilelibraryService.uploadOneFile(request, imageInfoBean.getImageContent(), "影像上传." + imageInfoBean.getImageMode(), queryOne.getCodetype(), queryOne.getCodename(), "admin");
            if (map.get("status").equals("success")) {
                INSBFilebusiness insbFilebusiness = new INSBFilebusiness();
                insbFilebusiness.setCreatetime(new Date());
                insbFilebusiness.setOperator("admin");
                insbFilebusiness.setType(queryOne.getCodetype());
                insbFilebusiness.setFilelibraryid((String) map.get("fileid"));
                if (StringUtil.isNotEmpty(taskId))
                    insbFilebusiness.setCode(taskId);
                else
                    insbFilebusiness.setCode("");
                insbFilebusinessDao.insert(insbFilebusiness);
            } else {
            	result.setRespCode("01");
            	result.setErrorMsg("影像上传失败");
                return result;
            }
        }
        return result;
    }

    /**
     * 任务查询
     */
    @Override
    public QuoteBean getTaskQueryByTaskIdAndDeptId(String taskId, String deptId, boolean qDetail) {
        QuoteBean quoteBean = new QuoteBean();
        quoteBean.setTaskId(taskId);
        quoteBean.setPrvId(deptId);
        if (StringUtil.isEmpty(taskId) || StringUtil.isEmpty(deptId)) {
            quoteBean.setRespCode("01");
            quoteBean.setErrorMsg("taskId or prvId is null");
            return quoteBean;
        }
        quoteBean.setRespCode("00");
        try {
            INSBWorkflowmain insbWorkflowmain = insbWorkflowmainDao.selectByInstanceId(taskId);
            if (null == insbWorkflowmain) {
                quoteBean.setRespCode("01");
                quoteBean.setErrorMsg("主任务号 " + taskId + " 不存在");
                return quoteBean;
            }
            // 支付有效期 起始时间点
            Date syStartDate = null, jqStartDate = null;

            Map<String, String> map = new HashMap<String, String>();
            map.put("taskid", taskId);
            map.put("inscomcode", deptId);
            Map<String, Object> kindMap = new HashMap<String, Object>();
            kindMap.put("taskId", taskId);
            kindMap.put("inscomcode", deptId);
            Map<String, String> mapcode = new HashMap<String, String>();
            mapcode.put("taskid", taskId);
            mapcode.put("companyid", deptId);
            INSBQuoteinfo insqi = insbQuoteinfoDao.getByTaskidAndCompanyid(mapcode);
            if (null == insqi) {
                quoteBean.setRespCode("01");
                quoteBean.setErrorMsg("供应商 " + deptId + " 不存在");
                return quoteBean;
            } else if (StringUtil.isEmpty(insqi.getWorkflowinstanceid())) {
                quoteBean.setRespCode("01");
                quoteBean.setErrorMsg(taskId + "该任务未提交报价");
                return quoteBean;
            }

            INSBProvider provider = provierService.queryByPrvcode(deptId.substring(0, 4));
            quoteBean.setPrvName(provider.getPrvshotname());
            getCarLicense(quoteBean, taskId, deptId, false);
            String insbworkflowsubid = insqi.getWorkflowinstanceid();
            INSBWorkflowsub insbWorkflowsub = insbWorkflowsubDao.selectByInstanceId(insbworkflowsubid);
            if (null == insbWorkflowsub) {
                quoteBean.setRespCode("00");
                quoteBean.setTaskState("2");
                quoteBean.setTaskStateDescription("报价中");
                return quoteBean;
            }
            INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
            insbQuotetotalinfo.setTaskid(taskId);
            String taskCode = insbWorkflowsub.getTaskcode();    //状态
            if ("end".equals(insbWorkflowsub.getTaskstate())) {
                taskCode = insbWorkflowmain.getTaskcode();
            }
            quoteBean.setDelivery(getDelivery(taskId, deptId));
            quoteBean.setErrorMsg(getComment(taskId, insbWorkflowsub.getInstanceid(), deptId, taskCode, insbWorkflowsub.getTaskstate(), true));
            INSBQuotetotalinfo result = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
            
            String interfaceId = "11"; //queryTask接口
            if (qDetail) interfaceId = "16"; //query接口
            if (!channelchnQuoteService.hasInterfacePower(interfaceId, result.getInscitycode(), result.getPurchaserChannel())) { //11-订单查询
                QuoteBean quoteBeanPrv = new QuoteBean();
                quoteBeanPrv.setRespCode("01");
                quoteBeanPrv.setErrorMsg("没有调用该接口的权限!");
                return quoteBeanPrv;
            }
            
            String tCode;
            String tDesc;
            if (null != result && StringUtil.isNotEmpty(result.getSourceFrom())) {
                if (result.getSourceFrom().equals("channel")) {
                    tCode = StatusCodeMapperUtil.states.get(taskCode);
                    tDesc = StatusCodeMapperUtil.stateDescription.get(tCode);
                } else {
                    tCode = StatusCodeMapperUtil.newStates.get(taskCode);
                    tDesc = StatusCodeMapperUtil.newStateDes.get(tCode);

                }
            } else {
                quoteBean.setRespCode("01");
                quoteBean.setErrorMsg(taskId+"不是渠道的单");
                tCode = StatusCodeMapperUtil.states.get(taskCode);
                tDesc = StatusCodeMapperUtil.stateDescription.get(tCode);
            }

            quoteBean.setChannelUserId(result.getPurchaserid());
            quoteBean.setTaskState(tCode);
            quoteBean.setTaskStateDescription(tDesc);
            
            this.getImageInfos(quoteBean, tCode, false);
            
            // 报价中
            if (tCode.equals("2")) {
                quoteBean.setRespCode("00");
                quoteBean.setTaskState("2");
                quoteBean.setTaskStateDescription("报价中");
                return quoteBean;
            }
            // 人工报价中
            if (tCode.equals("8")) {
                quoteBean.setRespCode("00");
                quoteBean.setTaskState("8");
                quoteBean.setTaskStateDescription("人工报价中");
                return quoteBean;
            }
            // 核保中
            if (tCode.equals("16")) {
                quoteBean.setRespCode("00");
                quoteBean.setTaskState("16");
                quoteBean.setTaskStateDescription("核保中");
                return quoteBean;
            }
            if (tCode.equals("13")) {
                return quoteBean;
            }

            //region 价格险种
            List<INSBPolicyitem> policyitemList = insbPolicyitemDao.getListByMap(map);
            List<INSBCarkindprice> carkindpriceList = insbCarkindpriceDao.selectByTaskidAndInscomcode(kindMap);
            List<INSBSpecialkindconfig> specialkindconfigList = insbSpecialkindconfigDao.selectByTaskIdAndInscomcode(map);

            InsureInfoBean insureInfoBean = new InsureInfoBean();
            if (policyitemList != null && policyitemList.size() > 0) {
                // 交强险保额
                double efcAmount = 0.0000d;
                // 交强险保费
                double efcPremium = 0.0000d;
                // 车船税金额
                double taxAmount = 0.0000d;
                // 车船税滞纳金
                double taxLateFee = 0.0000d;
                // 车船税
                BaseInsureInfoBean taxInsureInfoBean = null;
                for (INSBPolicyitem policyitem : policyitemList) {
                    if ("1".equals(policyitem.getRisktype())) {
                        syStartDate = policyitem.getStartdate();
                        // 交强险
                        BaseInsureInfoBean baseInsureInfoBean = new BaseInsureInfoBean();
                        if (policyitem.getStartdate() != null) {
                            baseInsureInfoBean.setStartDate(ModelUtil.conbertToString(policyitem.getStartdate()));
                        }
                        if (policyitem.getEnddate() != null) {
                            baseInsureInfoBean.setEndDate(ModelUtil.conbertToString(policyitem.getEnddate()));
                        }
                        for (INSBCarkindprice carkindprice : carkindpriceList) {
                            if ("2".equals(carkindprice.getInskindtype())) {
                                if (carkindprice.getAmount() != null) {
                                    efcAmount += douFormat(carkindprice.getAmount());
                                }
                                if (carkindprice.getDiscountCharge() != null) {
                                    efcPremium += douFormat(carkindprice.getDiscountCharge());
                                }
                            } else if ("3".equals(carkindprice.getInskindtype())) {
                                if (null == taxInsureInfoBean) {
                                    taxInsureInfoBean = new BaseInsureInfoBean();
                                }
                                if ("VehicleTax".equals(carkindprice.getInskindcode())) {
                                    // 车船税金额
                                    if (carkindprice.getDiscountCharge() != null) {
                                        taxAmount += douFormat(carkindprice.getDiscountCharge());
                                    }
                                } else {
                                    // 车船税滞纳金
                                    if (carkindprice.getDiscountCharge() != null) {
                                        taxLateFee += douFormat(carkindprice.getDiscountCharge());
                                    }
                                }
                            }
                        }

                        //车船税
                        if (taxInsureInfoBean != null) {
                            taxInsureInfoBean.setTaxFee(getFormat(taxAmount));
                            taxInsureInfoBean.setLateFee(getFormat(taxLateFee));
                            insureInfoBean.setTaxInsureInfo(taxInsureInfoBean);

                        }
                        //交强险
                        if (StringUtil.isNotEmpty(policyitem.getPolicyno()))
                            baseInsureInfoBean.setPolicyNo(policyitem.getPolicyno());
                        if (efcAmount > 0) {
                        	baseInsureInfoBean.setAmount("1.00");
                        } else {
                        	baseInsureInfoBean.setAmount(getFormat(efcAmount));
                        }
                        baseInsureInfoBean.setPremium(getFormat(efcPremium));
                        if (null != policyitem.getDiscountRate())
                            baseInsureInfoBean.setDiscountRate(getFormat4(policyitem.getDiscountRate()));
                        insureInfoBean.setEfcInsureInfo(baseInsureInfoBean);
                        if (null != policyitem.getTotalepremium())
                            insureInfoBean.setTotalPremium(getFormat(policyitem.getTotalepremium()));
                    } else {
                        jqStartDate = policyitem.getStartdate();

                        getBizInsureInfo(insureInfoBean, policyitem, carkindpriceList, specialkindconfigList);
                    }
                }
            }

            Date quoteDate = getQuoteValidDate(taskId, insbworkflowsubid, deptId, syStartDate, jqStartDate, true);
            if (null != quoteDate) {
                quoteBean.setQuoteValidTime(ModelUtil.conbertToStringsdf(quoteDate));
            }
            Date payDate = getPayValidDate(taskId, deptId, syStartDate, jqStartDate, true);
            if (null != payDate) {
                quoteBean.setPayValidTime(ModelUtil.conbertToStringsdf(payDate));
            }
            quoteBean.setRespCode("00");
            quoteBean.setInsureInfo(insureInfoBean);
            return quoteBean;
        } catch (Exception e) {
            LogUtil.error(taskId + "查询异常：" + e.getMessage());
            e.printStackTrace();
            quoteBean.setRespCode("01");
            quoteBean.setErrorMsg(taskId + "查询异常：" + e.getMessage());
            return quoteBean;
        }
    }

    //查询商业险信息
    private void getBizInsureInfo(InsureInfoBean insureInfoBean, INSBPolicyitem policyitem,
                                  List<INSBCarkindprice> carkindpriceList,
                                  List<INSBSpecialkindconfig> specialkindconfigList) {
        // 获取商业险特殊险别
        List<Map<String, Object>> codeList = inscCodeDao.selectByType("riskkindconfig");
        List<String> riskKindConfigList = new ArrayList<String>();
        for (Map<String, Object> map : codeList) {
            riskKindConfigList.add((String) map.get("codename"));
        }
        // 商业险不计免赔保费
        double biznfcPremium = 0.0000d;
        // 商业总险保费
        double bizPremium = 0.0000d;

        // 商业险
        BizInsureInfoBean bizInsureInfoBean = null;

        List<RiskKindBean> riskKindBeanList = null;
        for (INSBCarkindprice carkindprice : carkindpriceList) {
            // 0 表示商业险 1 表示不计免赔
            if ("0".equals(carkindprice.getInskindtype()) || "NcfBasicClause".equals(carkindprice.getInskindcode()) ||
                    "Ncfaddtionalclause".equals(carkindprice.getInskindcode()) || "NcfClause".equals(carkindprice.getInskindcode())||
                    "NcfDriverPassengerIns".equals(carkindprice.getInskindcode())) {
//				还需要对selecteditem进行解析，只有"TYPE":"01"才能进入。
//				JSONArray itemArray = JSONArray.fromObject(carkindprice.getSelecteditem());
//				JSONObject item=JSONObject.fromObject(itemArray.get(0));
//				if("01".equals(item.get("TYPE"))){
                // 商业险
                if (bizInsureInfoBean == null) {
                    bizInsureInfoBean = new BizInsureInfoBean();
                    riskKindBeanList = new ArrayList<RiskKindBean>();
                }
                RiskKindBean riskKindBean = new RiskKindBean();
                if (carkindprice.getInskindcode().equals("Ncfaddtionalclause")) {
                    riskKindBean.setRiskCode("NcfAddtionalClause");
                } else {
                    riskKindBean.setRiskCode(carkindprice.getInskindcode());
                }
                riskKindBean.setRiskName(carkindprice.getRiskname());
                boolean isSpecialRisk = false; // 是否特殊险别
                for (String riskKindConfig : riskKindConfigList) {
                    if (riskKindConfig.equals(riskKindBean.getRiskCode())) {
                        isSpecialRisk = true;
                        break;
                    }
                }

                if (isSpecialRisk) {
                    // 特殊险别的情况暂不考虑

                } else {
                    // 非特殊险别
                    if (carkindprice.getAmount() != null) {
                        riskKindBean.setAmount(getFormat(douFormat(carkindprice.getAmount())));
                    }
                    if (carkindprice.getDiscountCharge() != null) {
                        if(carkindprice.getInskindcode().equals("NcfBasicClause")||carkindprice.getInskindcode().equals("Ncfaddtionalclause")
                                ||carkindprice.getInskindcode().equals("NcfClause")||carkindprice.getInskindcode().equals("NcfDriverPassengerIns")){

                            biznfcPremium += douFormat(carkindprice.getDiscountCharge());
                        }else {
                            bizPremium += douFormat(carkindprice.getDiscountCharge());
                        }
                        riskKindBean.setPremium(getFormat(douFormat(carkindprice.getDiscountCharge())));

                    }
                    riskKindBean.setNotDeductible("N");
                    // 不计免赔
                    for (INSBCarkindprice ncfKind : carkindpriceList) {
                        if ("1".equals(ncfKind.getInskindtype())
                                && carkindprice.getInskindcode().equals(ncfKind.getPreriskkind())) {
                            riskKindBean.setNotDeductible("Y");
                            if (ncfKind.getDiscountCharge() != null && douFormat(ncfKind.getDiscountCharge()) > 0) {
                                riskKindBean.setNcfPremium(getFormat(douFormat(ncfKind.getDiscountCharge())));
                                biznfcPremium += douFormat(ncfKind.getDiscountCharge());
                            } else {
                                riskKindBean.setNcfPremium("0.0000");
                            }
                            break;
                        }
                    }

                    riskKindBeanList.add(riskKindBean);
                }
            }
        }

        //商业险
        if (bizInsureInfoBean != null) {
            if (policyitem.getStartdate() != null) {
                bizInsureInfoBean.setStartDate(ModelUtil.conbertToString(policyitem.getStartdate()));
            }
            if (policyitem.getEnddate() != null) {
                bizInsureInfoBean.setEndDate(ModelUtil.conbertToString(policyitem.getEnddate()));
            }
            if (StringUtil.isNotEmpty(policyitem.getPolicyno())) {
                bizInsureInfoBean.setPolicyNo(policyitem.getPolicyno());
            }
            if (null != policyitem.getDiscountCharge())
                bizInsureInfoBean.setPremium(getFormat(bizPremium + biznfcPremium));
//                bizInsureInfoBean.setPremium(getFormat(policyitem.getPremium()));
            bizInsureInfoBean.setRiskKinds(riskKindBeanList);
            if (null != policyitem.getDiscountRate())
                bizInsureInfoBean.setDiscountRate(getFormat4(policyitem.getDiscountRate()));
            bizInsureInfoBean.setNfcPremium(getFormat(biznfcPremium));
            if (null != policyitem.getTotalepremium()) {
                insureInfoBean.setTotalPremium(getFormat(policyitem.getTotalepremium()));
            }
            insureInfoBean.setBizInsureInfo(bizInsureInfoBean);
        }
    }

    private double douFormat(Double target) {
        double tar = 0d;
        if (target != null) {
            tar = target.doubleValue();
        }
        return tar;
    }

    public static final DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    public static final DecimalFormat decimalFormat4 = new DecimalFormat("#0.0000");

    // Double -> String
    private String getFormat(double target) {
        return decimalFormat.format(target);
    }

    // Double -> String
    private String getFormat4(double target) {
        return decimalFormat4.format(target);
    }

    /**
     * 查询当前节点之前的用户备注信息
     * 入参     instanceid： 主流程实例id ; inscomcode 保险公司code ; dqtaskcode 当前节点code
     */
    public List<INSBUsercomment> getNearestUserComment(String instanceid,
                                                       String inscomcode, String dqtaskcode, String status) {
        // 组织参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("instanceid", instanceid);
        params.put("inscomcode", inscomcode);
        params.put("dqtaskcode", dqtaskcode);
        if (status.equals("end") || status.equals("Closed")) {
            return insbUsercommentDao.getNearestUserComment2(params);
        } else {
            return insbUsercommentDao.getNearestUserComment(params);
        }
    }

    // 获取最近一天业管备注
    public String getComment(String taskId, String subinstanceid, String inscomcode, String dqtaskcode, String status, boolean queryFlag) {
        String result = null;
        
        if ("37".equals(dqtaskcode)) { //37包含多种情况需要特别处理
        	INSBWorkflowmain insbWorkflowmain = insbWorkflowmainDao.selectByInstanceId(taskId); 
	    	INSBWorkflowsub workflowsubcon = new INSBWorkflowsub();
	    	workflowsubcon.setMaininstanceid(taskId);
	        List<INSBWorkflowsub> workflowsubs = insbWorkflowsubDao.selectList(workflowsubcon);
	        
	        for (INSBWorkflowsub workflowsub : workflowsubs) {
	        	String taskCode = workflowsub.getTaskcode();
	        	if ("end".equals(workflowsub.getTaskstate())) {
	        		taskCode = insbWorkflowmain.getTaskcode();
	        	}
	        	taskCode = StatusCodeMapperUtil.states.get(taskCode);
		        if ("25".equals(taskCode) || "33".equals(taskCode)) { 
		        	return null;
		        }
	        }
    	}

        if (StatusCodeMapperUtil.quoteFaileState.contains(dqtaskcode)) {
            result = getQuoteFailMsg(taskId, inscomcode, queryFlag);
        } else if (StatusCodeMapperUtil.commentState.contains(dqtaskcode)) {
            List<INSBUsercomment> usercomment = getNearestUserComment(taskId, inscomcode, dqtaskcode, status);
            if (usercomment != null && usercomment.size() > 0) {
                result = usercomment.get(0).getCommentcontent();
            } else {
                LogUtil.info(subinstanceid + " 供应商： " + inscomcode + "给用户的备注条数为 " + usercomment.size());
            }
        }
        return result;
    }
    
    private String getQuoteFailMsg(String taskId, String inscomcode, boolean queryFlag) {
    	String result = null;
    	result = getQuoteFailMsg(taskId, inscomcode);
    	
    	if (!queryFlag) {
	    	int i = 1;
	    	while (result == null) {
	    		try {
	    			Thread.sleep(i * 1000);
	    		} catch (InterruptedException e) {
	    			LogUtil.error("getQuoteFailMsg-Thread.sleep异常：" + e.getMessage());
	    			e.printStackTrace();
	    		}
	    		
	    		LogUtil.info("getQuoteFailMsg第" + i + "次查询：" + taskId + "-" + inscomcode + "-" + queryFlag);
	    		result = getQuoteFailMsg(taskId, inscomcode);
	    		i++;
	    		if (i >= 4) break;
	    	}
    	}
    	
    	if (result == null) {
    		result = "系统网路开小差了";
    	}
    	
    	return result;
    }
    
    private String getQuoteFailMsg(String taskId, String inscomcode) {
    	String result = null;
    	
    	//是否有重复投保的供应商
    	INSBFlowerror repeatCon = new INSBFlowerror();
    	repeatCon.setTaskid(taskId);
    	repeatCon.setErrorcode("12"); //重复投保
		long repeatSize = insbFlowerrorDao.selectCount(repeatCon);
		if ( repeatSize > 0 ) {
			INSCCode query = new INSCCode();
			query.setCodetype("fronterrorinfo");
			query.setCodevalue("12");
			List<INSCCode> inscCodeList = inscCodeService.queryList(query);
			result = inscCodeList.get(0).getCodename();
			return result;
		}
    	
    	//判断是否是主流程到达后面的阶段
		List<INSBUsercomment> usercomment = insbUsercommentService.getNearestUserComment2(taskId, inscomcode, null);
		if (usercomment != null && usercomment.size() > 0) {
			String commentStr = "";
			for(INSBUsercomment comment : usercomment){
				if ( StringUtil.isEmpty(comment.getCommentcontent()) || "渠道".equals(comment.getOperator()) ) continue;
				commentStr = comment.getCommentcontent();				
				if (StringUtil.isNotEmpty(commentStr)) return commentStr;
			}
		}
    	
    	//规则承保政策限制提示
		INSBFlowerror insbFlowerror = new INSBFlowerror();
		insbFlowerror.setTaskid(taskId);
		insbFlowerror.setInscomcode(inscomcode);
		insbFlowerror.setFiroredi("4");
		List<INSBFlowerror> insbFlowerrors = insbFlowerrorDao.selectOneTipGuizeshow(insbFlowerror);
        
		Date guizheTime = null;
		if (null != insbFlowerrors && insbFlowerrors.size() > 0) {
			for (INSBFlowerror flowerror : insbFlowerrors) {
				if ("0".equals(flowerror.getFlowcode())) { //提示性规则
					//flowerror.getErrordesc()
				} else if ("10".equals(flowerror.getFlowcode())) { //重复投保提示
					result = "该车辆上年保单未到期，请符合承保日期时再提交";
				} else { //2承保政策限制 3 阻断性规则
					result = flowerror.getErrordesc();
				}
				
				guizheTime = flowerror.getCreatetime();
			}
			
			if (result != null) {
				result = result.replace("\"", "");
				result = result.replace("，转人工处理", "");
				result = result.replace("，需要转人工处理，请点击“我要人工报价”!", "");
			}
		}		

    	INSBFlowerror queryInsbFlowerror = new INSBFlowerror();
		queryInsbFlowerror.setTaskid(taskId);
		queryInsbFlowerror.setInscomcode(inscomcode);
		queryInsbFlowerror.setFlowname("报价失败");
		List<INSBFlowerror> dataINSBFlowerrorList = insbFlowerrorDao.selectList(queryInsbFlowerror);

		if (dataINSBFlowerrorList != null && !dataINSBFlowerrorList.isEmpty()) {
			INSBFlowerror dataInsbFlowerror = dataINSBFlowerrorList.get(0);
			
			Date quoteTime = dataInsbFlowerror.getCreatetime();
			if ( result != null && guizheTime.getTime() >= quoteTime.getTime() ) {
				return result; //规则提示信息时间靠后
			} else {
				result = null;
			}
			
			INSCCode query = new INSCCode();
			query.setCodetype("fronterrorinfo");
			query.setCodevalue(dataInsbFlowerror.getErrorcode());
			List<INSCCode> inscCodeList = inscCodeService.queryList(query);
			
			if (inscCodeList != null && inscCodeList.size() > 0) {
				String codename = inscCodeList.get(0).getCodename();
				
				if ("12".equals(dataInsbFlowerror.getErrorcode())) {
					String errordesc = dataInsbFlowerror.getErrordesc();

					if ( StringUtil.isNotEmpty(errordesc) && (errordesc.contains("商业险终保日期") || errordesc.contains("交强险终保日期")) ) {
						result = codename.replace("desc", errordesc);

					} else {
						result = codename.replace("desc，", "");
					}
				} else {
					result = codename;
				}
				
				if (result != null) {
					result = result.replace("，需要转人工处理，请点击“我要人工报价”", "");
				}
			} 
		}

    	return result;
    }
    
    //查询配送地址--判断是否北京平台流程
    private DeliveryBean getDelivery(String taskid, String inscomcode, INSBQuotetotalinfo quotetotalinfo) {
    	DeliveryBean deliveryBean = getDelivery(taskid, inscomcode);
    	
    	INSBRegion insbRegion = new INSBRegion();
    	insbRegion.setParentid("110000"); //北京
    	insbRegion.setComcode(quotetotalinfo.getInscitycode()); 
    	if ( insbRegionDao.selectCount(insbRegion) > 0 ) {
    		String strResult = enternalInterFaceService.getElecPolicyPathInfo(taskid);
    		JSONObject jsonObject = JSONObject.fromObject(strResult);
    		//String status = jsonObject.getString("result");
    		LogUtil.info(taskid + "getElecPolicyPathInfo:" + strResult); 

    		String jpElecPolicyFilePath = jsonObject.getString("jpElecPolicyFilePath");
    		String syElecPolicyFilePath = jsonObject.getString("syElecPolicyFilePath");
            
    		deliveryBean.setJpElecPolicyFilePath(jpElecPolicyFilePath);
    		deliveryBean.setSyElecPolicyFilePath(syElecPolicyFilePath);
    		deliveryBean.setDeliveryType("2"); //电子保单
    	}
    	
    	return deliveryBean;
    }

    //查询配送地址--目前渠道只有快递方式
    public DeliveryBean getDelivery(String taskid, String inscomcode) {
        DeliveryBean deliveryBean = null;
        Map<String, Object> map = new HashMap<>();
        map.put("taskid", taskid);
        map.put("inscomcode", inscomcode);
        INSBOrderdelivery orderdelivery = insbOrderdeliveryDao.getOrderdeliveryByTaskId(map);
        if (null != orderdelivery) {
            deliveryBean = new DeliveryBean();
            String deliveryType = orderdelivery.getDelivetype();
            //String deliveTypeName = "0".equals(deliveryType) ? "自取" : "快递"; //配送方式编码 0=自取/1=快递
            deliveryBean.setDeliveryType(deliveryType);
            if ("1".equals(deliveryType) || "3".equals(deliveryType)) { //3-电子保单
                deliveryBean.setName(orderdelivery.getRecipientname());
                deliveryBean.setMobile(orderdelivery.getRecipientmobilephone());
                deliveryBean.setPhone(orderdelivery.getRecipientmobilephone());
                deliveryBean.setProvince(orderdelivery.getRecipientprovince());
                deliveryBean.setCity(orderdelivery.getRecipientcity());
                deliveryBean.setArea(orderdelivery.getRecipientarea());
                deliveryBean.setAddress(orderdelivery.getRecipientaddress());
                deliveryBean.setExpressNo(orderdelivery.getTracenumber());
                deliveryBean.setExpressNumber(orderdelivery.getTracenumber());
                deliveryBean.setExpressCompanyName(orderdelivery.getLogisticscompany());
                deliveryBean.setZip(orderdelivery.getZip());
                if (StringUtil.isNotEmpty(orderdelivery.getLogisticscompany())) {
                    INSCCode code = new INSCCode();
                    code.setCodetype("logisticscompany");
                    code.setCodevalue(orderdelivery.getLogisticscompany());
                    code = inscCodeDao.selectOne(code);
                    deliveryBean.setExpressCompanyName(code.getCodename());
                }
            } else {
                INSCDept dept = inscDeptDao.selectById(orderdelivery.getDeptcode());
                if (dept != null) {
                    deliveryBean.setProvince(dept.getProvince());
                    deliveryBean.setCity(dept.getCity());
                    deliveryBean.setArea(dept.getCounty());
                    deliveryBean.setAddress(dept.getAddress());
                    deliveryBean.setOutDept(dept.getShortname());
                }
            }
        }
        return deliveryBean;
    }

    //获取车牌信息
    public void getCarLicense(QuoteBean quoteBean, String taskId, String comcode, Boolean isall) {
        INSBCarinfohis his = new INSBCarinfohis();
        his.setTaskid(taskId);
        his.setInscomcode(comcode);
        INSBCarinfohis carinfohis = insbCarinfohisDao.selectOne(his);
        CarInfoBean carInfoBean = new CarInfoBean();
        PersonBean carOwnerBean = new PersonBean();
        if (null != carinfohis) {
        	INSBPerson insbPerson = insbPersonDao.selectById(carinfohis.getOwner());

            carInfoBean.setCarLicenseNo(carinfohis.getCarlicenseno());
            carInfoBean.setVehicleName(carinfohis.getStandardfullname());
            carInfoBean.setCarProperty(carinfohis.getCarproperty());
            if (isall) {
            	carInfoBean.setDrivingArea(carinfohis.getDrivingarea());
            	if (carinfohis.getCarVehicularApplications() != null) {
            		carInfoBean.setPurpose(String.valueOf(carinfohis.getCarVehicularApplications()));
            	}
            	carInfoBean.setProperty(carinfohis.getProperty());
            	carInfoBean.setIsTransferCar(carinfohis.getIsTransfercar());
            	carInfoBean.setTransferDate(ModelUtil.conbertToString(carinfohis.getTransferdate()));
            	carInfoBean.setIsNew(carinfohis.getIsNew());
                carInfoBean.setEngineNo(carinfohis.getEngineno()); //ModelUtil.hiddenEngineNo(carinfohis.getEngineno())
                carInfoBean.setVinCode(carinfohis.getVincode()); //ModelUtil.hiddenVin(carinfohis.getVincode())
                if (null != carinfohis.getRegistdate())
                    carInfoBean.setRegistDate(ModelUtil.conbertToString(carinfohis.getRegistdate()));
                INSBCarmodelinfohis insbCarmodelinfohis = new INSBCarmodelinfohis();
                insbCarmodelinfohis.setCarinfoid(carinfohis.getId());
                INSBCarmodelinfohis carmodelinfohis = insbCarmodelinfohisDao.selectOne(insbCarmodelinfohis);
                
                if (carmodelinfohis == null) {
                	Map<String, Object> mapCond = new HashMap<String, Object>();
                	mapCond.put("taskid", taskId);
                	mapCond.put("inscomcode", comcode);
                	carmodelinfohis = insbCarmodelinfohisDao.selectModelInfoByTaskIdAndPrvId(mapCond);
                }
                
                if (null != carmodelinfohis) {
                    if (null != carmodelinfohis.getDisplacement())
                        carInfoBean.setDisplacement(carmodelinfohis.getDisplacement().toString());
                    if (null != carmodelinfohis.getSeat())
                        carInfoBean.setSeat(carmodelinfohis.getSeat().toString());
                    if(null!=carmodelinfohis.getStandardfullname())
                    	carInfoBean.setVehicleName(carmodelinfohis.getStandardfullname());
                    
                    carInfoBean.setVehicleId(carmodelinfohis.getVehicleid());
                    if (carmodelinfohis.getPrice() != null) {
                    	carInfoBean.setPrice(carmodelinfohis.getPrice().toString());
                    }
                    if (carmodelinfohis.getLoads() != null) {
                    	carInfoBean.setModelLoads(carmodelinfohis.getLoads().toString());
                    }
                	if (carmodelinfohis.getFullweight() != null) {
                		carInfoBean.setFullWeight(carmodelinfohis.getFullweight().toString());
                	}
                	if (carmodelinfohis.getPolicycarprice() != null) {
                		carInfoBean.setPolicycarprice(carmodelinfohis.getPolicycarprice().toString()); //投保车价
                	}
                	carInfoBean.setGearbox(carmodelinfohis.getGearbox()); //车型配置
                	carInfoBean.setYearPrice(carmodelinfohis.getListedyear()); //年款                   	
                	carInfoBean.setVehicleTypeName(carmodelinfohis.getSyvehicletypename()); //车辆种类
                }

                if (null != insbPerson) {
                    carOwnerBean.setIdcardNo(insbPerson.getIdcardno());
                    carOwnerBean.setEmail(insbPerson.getEmail()); 
                    if (null != insbPerson.getIdcardtype())
                        carOwnerBean.setIdcardType(insbPerson.getIdcardtype().toString());                   
                }
            }
            carOwnerBean.setName(carinfohis.getOwnername());
            if (null != insbPerson) {
            	carOwnerBean.setMobile(insbPerson.getCellphone());
            	carOwnerBean.setPhone(insbPerson.getCellphone());
            }
        } else {
            INSBCarinfo carinfo = insbCarinfoDao.selectCarinfoByTaskId(taskId);
            if (null != carinfo) {
            	INSBPerson insbPerson = insbPersonDao.selectById(carinfo.getOwner());
            	
                carInfoBean.setCarLicenseNo(carinfo.getCarlicenseno());
                carInfoBean.setVehicleName(carinfo.getStandardfullname());
                carInfoBean.setCarProperty(carinfo.getCarproperty());
                if (isall) {
                	carInfoBean.setDrivingArea(carinfo.getDrivingarea());
                	if (carinfo.getCarVehicularApplications() != null) {
                		carInfoBean.setPurpose(String.valueOf(carinfo.getCarVehicularApplications()));
                	}
                	carInfoBean.setProperty(carinfo.getProperty());
                	carInfoBean.setIsTransferCar(carinfo.getIsTransfercar());
                	carInfoBean.setTransferDate(ModelUtil.conbertToString(carinfo.getTransferdate()));
                	carInfoBean.setIsNew(carinfo.getIsNew());
                    carInfoBean.setEngineNo(carinfo.getEngineno()); //ModelUtil.hiddenEngineNo(carinfo.getEngineno())
                    carInfoBean.setVinCode(carinfo.getVincode()); //ModelUtil.hiddenVin(carinfo.getVincode())
                    if (null != carinfo.getRegistdate())
                        carInfoBean.setRegistDate(ModelUtil.conbertToString(carinfo.getRegistdate()));
                    INSBCarmodelinfohis insbCarmodelinfo = new INSBCarmodelinfohis();
                    insbCarmodelinfo.setCarinfoid(carinfo.getId());
                    INSBCarmodelinfohis carmodelinfo = insbCarmodelinfohisDao.selectOne(insbCarmodelinfo);
                    if (null != carmodelinfo) {
                        if (null != carmodelinfo.getDisplacement())
                            carInfoBean.setDisplacement(carmodelinfo.getDisplacement().toString());
                        if (null != carmodelinfo.getSeat())
                            carInfoBean.setSeat(carmodelinfo.getSeat().toString());
                        carInfoBean.setVehicleName(carmodelinfo.getStandardfullname());
                        
                        carInfoBean.setVehicleId(carmodelinfo.getVehicleid());
                        if (carmodelinfo.getPrice() != null) {
                        	carInfoBean.setPrice(carmodelinfo.getPrice().toString());
                        }
                        if (carmodelinfo.getLoads() != null) {
                        	carInfoBean.setModelLoads(carmodelinfo.getLoads().toString());
                        }
                    	if (carmodelinfo.getFullweight() != null) {
                    		carInfoBean.setFullWeight(carmodelinfo.getFullweight().toString());
                    	}
                    	if (carmodelinfo.getPolicycarprice() != null) {
                    		carInfoBean.setPolicycarprice(carmodelinfo.getPolicycarprice().toString()); //投保车价
                    	}
                    	carInfoBean.setGearbox(carmodelinfo.getGearbox()); //车型配置
                    	carInfoBean.setYearPrice(carmodelinfo.getListedyear()); //年款                   	
                    	carInfoBean.setVehicleTypeName(carmodelinfo.getSyvehicletypename()); //车辆种类
                    }
                    
                    if (null != insbPerson) {
                        carOwnerBean.setIdcardNo(insbPerson.getIdcardno());
                        carOwnerBean.setEmail(insbPerson.getEmail()); 
                        if (null != insbPerson.getIdcardtype())
                            carOwnerBean.setIdcardType(insbPerson.getIdcardtype().toString());
                    }
                }
                carOwnerBean.setName(carinfo.getOwnername());
                if (null != insbPerson) {
                	carOwnerBean.setMobile(insbPerson.getCellphone());
                	carOwnerBean.setPhone(insbPerson.getCellphone());
                }
            } else {
                carInfoBean.setCarLicenseNo("未上牌");
            }

        }
        
        //String idcardNo = carOwnerBean.getIdcardNo();
        //if (StringUtil.isNotEmpty(idcardNo)) {
        //	carOwnerBean.setIdcardNo(idcardNo.substring(0, 10) + "****" + idcardNo.substring(14));
        //}
        quoteBean.setCarOwner(carOwnerBean);
        quoteBean.setCarInfo(carInfoBean);
    }

    //根据taskid 和 供应商id 获取InsuredPerson
    public void getInsuredPerson(QuoteBean quoteBean, String taskId) {
        INSBPerson insbInsured = insbPersonDao.selectInsuredPersonByTaskId(taskId);
        PersonBean infoBean = new PersonBean();
        if (insbInsured != null) {
            infoBean.setName(insbInsured.getName());
            if (null != insbInsured.getGender())
                infoBean.setGender(insbInsured.getGender().toString());
            infoBean.setIdcardNo(insbInsured.getIdcardno());
            if (null != insbInsured.getIdcardtype())
                infoBean.setIdcardType(insbInsured.getIdcardtype().toString());
            infoBean.setMobile(insbInsured.getCellphone());
            infoBean.setEmail(insbInsured.getEmail());
            //quoteBean.setInsured(infoBean);

        } else {
            INSBPerson carOwer = insbPersonDao.selectCarOwnerPersonByTaskId(taskId);
            if (carOwer != null)
                infoBean.setName(carOwer.getName());
            if (null != carOwer.getGender())
                infoBean.setGender(carOwer.getGender().toString());
            infoBean.setIdcardNo(carOwer.getIdcardno());
            if (null != carOwer.getIdcardtype())
                infoBean.setIdcardType(carOwer.getIdcardtype().toString());
            infoBean.setMobile(carOwer.getCellphone());
            infoBean.setEmail(carOwer.getEmail());
        }

        //String idcardNo = infoBean.getIdcardNo();
        //if (StringUtil.isNotEmpty(idcardNo)) {
        //	infoBean.setIdcardNo(idcardNo.substring(0, 10) + "****" + idcardNo.substring(14));
        //}
        quoteBean.setInsured(infoBean);

    }

    //根据taskid 和 供应商id 受益人信息
    public void getBeneficiaryPerson(QuoteBean quoteBean, String taskId) {
        INSBPerson beneficiary = insbPersonDao.selectBeneficiaryByTaskId(taskId);
        PersonBean infoBean = new PersonBean();
        if (beneficiary != null) {
            infoBean.setName(beneficiary.getName());
            if (null != beneficiary.getGender())
                infoBean.setGender(beneficiary.getGender().toString());
            infoBean.setIdcardNo(beneficiary.getIdcardno());
            if (null != beneficiary.getIdcardtype())
                infoBean.setIdcardType(beneficiary.getIdcardtype().toString());
            infoBean.setMobile(beneficiary.getCellphone());
            infoBean.setEmail(beneficiary.getEmail());

        } else {
            INSBPerson carOwer = insbPersonDao.selectCarOwnerPersonByTaskId(taskId);
            if (carOwer != null)
                infoBean.setName(carOwer.getName());
            if (null != carOwer.getGender())
                infoBean.setGender(carOwer.getGender().toString());
            infoBean.setIdcardNo(carOwer.getIdcardno());
            if (null != carOwer.getIdcardtype())
                infoBean.setIdcardType(carOwer.getIdcardtype().toString());
            infoBean.setMobile(carOwer.getCellphone());
            infoBean.setEmail(carOwer.getEmail());
        }

        //String idcardNo = infoBean.getIdcardNo();
        //if (StringUtil.isNotEmpty(idcardNo)) {
        //	infoBean.setIdcardNo(idcardNo.substring(0, 10) + "****" + idcardNo.substring(14));
        //}
        quoteBean.setBeneficiary(infoBean);

    }

    //根据taskid 和 供应商id 获取投保人信息
    public void getApplicantPerson(QuoteBean quoteBean, String taskId) {
        INSBPerson applicant = insbPersonDao.selectApplicantPersonByTaskId(taskId);
        PersonBean infoBean = new PersonBean();
        if (applicant != null) {
            infoBean.setName(applicant.getName());
            if (null != applicant.getGender())
                infoBean.setGender(applicant.getGender().toString());
            infoBean.setIdcardNo(applicant.getIdcardno());
            if (null != applicant.getIdcardtype())
                infoBean.setIdcardType(applicant.getIdcardtype().toString());
            infoBean.setMobile(applicant.getCellphone());
            infoBean.setEmail(applicant.getEmail());

        } else {
            INSBPerson carOwer = insbPersonDao.selectCarOwnerPersonByTaskId(taskId);
            if (carOwer != null)
                infoBean.setName(carOwer.getName());
            if (null != carOwer.getGender())
                infoBean.setGender(carOwer.getGender().toString());
            infoBean.setIdcardNo(carOwer.getIdcardno());
            if (null != carOwer.getIdcardtype())
                infoBean.setIdcardType(carOwer.getIdcardtype().toString());
            infoBean.setMobile(carOwer.getCellphone());
            infoBean.setEmail(carOwer.getEmail());
        }

        //String idcardNo = infoBean.getIdcardNo();
        //if (StringUtil.isNotEmpty(idcardNo)) {
        //	infoBean.setIdcardNo(idcardNo.substring(0, 10) + "****" + idcardNo.substring(14));
        //}
        quoteBean.setApplicant(infoBean);
    }

    //获取报价有效期
    private Date getQuoteValidDate(String taskId, String subId, String inscomcode, Date busStartDate, Date strStartDate, boolean queryFlag) {
    	Date now = new Date();
        //获取选择报价轨迹时间
        INSBWorkflowsubtrack subtrackCon = new INSBWorkflowsubtrack();
        subtrackCon.setMaininstanceid(taskId);
        subtrackCon.setInstanceid(subId);
        subtrackCon.setTaskcode("14");//选择投保
        INSBWorkflowsubtrack subtrack = insbWorkflowsubtrackDao.selectOne(subtrackCon);//选择投保子流程轨迹信息
        
        if (subtrack == null && queryFlag) return null;
        
        int i = 1;
        while (subtrack == null) {
        	try {
				Thread.sleep(i * 1000);
			} catch (InterruptedException e) {
				LogUtil.error("getQuoteValidDate-Thread.sleep异常：" + e.getMessage());
				e.printStackTrace();
			}
        	
        	LogUtil.info("getQuoteValidDate第" + i + "次查询：" + taskId + "-" + subId);
        	subtrack = insbWorkflowsubtrackDao.selectOne(subtrackCon);//选择投保子流程轨迹信息
        	i++;
        	if (i >= 4) break;
        }
        
        //报价有效期
        Date quotesuccessTimes = null;
        INSBProvider atainteger = insbProviderDao.selectById(inscomcode);
        if ( null != atainteger && null != atainteger.getQuotationvalidity() ) {
            Date dateQuote = subtrack == null ? now : subtrack.getCreatetime();
            quotesuccessTimes = ModelUtil.gatFastPaydateToNow(busStartDate, strStartDate, dateQuote, atainteger.getQuotationvalidity());
        } else {
            quotesuccessTimes = ModelUtil.gatFastPaydate(busStartDate, strStartDate);
        }
        LogUtil.info("报价有效期taskid" + taskId + "到期时间为=" + quotesuccessTimes + "供应商id=" + inscomcode);
        
        if (quotesuccessTimes != null) {
        	String strVaildTime = channelchnQuoteService.getWorkVaildTime(taskId, inscomcode, quotesuccessTimes, true);
        	LogUtil.info("getWorkVaildTime报价有效期taskid" + taskId + "到期时间为=" + strVaildTime + "供应商id=" + inscomcode);
        	quotesuccessTimes = ModelUtil.conbertStringToDate(strVaildTime);
        }
        
        return quotesuccessTimes;
    }
    
    //获取报价有效期public
    public Date getQuoteValidDate(String taskId, String prvId, String subInstanceId, boolean addOneDay, boolean queryFlag) throws Exception {
    	INSBPolicyitem pitem = new INSBPolicyitem();
        pitem.setTaskid(taskId);
        pitem.setInscomcode(prvId);
        List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectList(pitem);
    	
        Date syStartDate = null, jqStartDate = null;
        if (policyitemList != null && policyitemList.size() > 0) {
	        for (INSBPolicyitem policyitem : policyitemList) {
	            if ("1".equals(policyitem.getRisktype())) {
	            	jqStartDate = policyitem.getStartdate();
	            } else {
	            	syStartDate = policyitem.getStartdate();
	            }
	        }
        }
        Date result = getQuoteValidDate(taskId, subInstanceId, prvId, syStartDate, jqStartDate, queryFlag);
        //if (result != null && addOneDay) {
        //	result = ModelUtil.nowDateAddOneDay(result);
        //}
        
        return result;
    }

    //获取支付有效期
    private Date getPayValidDate(String taskid, String inscomcode, Date syStartDate, Date jqStartDate, boolean queryFlag) {
    	Date now = new Date();
        Date paysuccessTimes = null;
        //获取流程走到支付节点的时间
        Map<String, Object> map = new HashMap<>();
        map.put("taskid", taskid);
        map.put("inscomcode", inscomcode);
        INSBWorkflowsubtrack insbWorkflowsubtrack = insbWorkflowsubtrackDao.selectOneByTaskidAndInscomcode(map);
        
        if (insbWorkflowsubtrack == null && queryFlag) return null;
        
        int i = 1;
        while (insbWorkflowsubtrack == null) {
        	try {
				Thread.sleep(i * 1000);
			} catch (InterruptedException e) {
				LogUtil.error("getPayValidDate-Thread.sleep异常：" + e.getMessage());
				e.printStackTrace();
			}
        	
        	LogUtil.info("getPayValidDate第" + i + "次查询：" + taskid + "-" + inscomcode);
        	insbWorkflowsubtrack = insbWorkflowsubtrackDao.selectOneByTaskidAndInscomcode(map);
        	i++;
        	if (i >= 4) break;
        }

        INSBProvider insbProvider = insbProviderDao.selectById(inscomcode);
        if (null != insbProvider) {
            if (null == insbProvider.getPayvalidity()) {
            	paysuccessTimes = ModelUtil.gatFastPaydate(syStartDate, jqStartDate);
            } else {
                Date dateQuote = insbWorkflowsubtrack == null ? now : insbWorkflowsubtrack.getCreatetime();
                paysuccessTimes = ModelUtil.gatFastPaydateToNow(syStartDate, jqStartDate,dateQuote, insbProvider.getPayvalidity());
            }

        }
        LogUtil.info("支付有效期taskid" + taskid + "到期时间为=" + paysuccessTimes + "供应商id=" + inscomcode);
        
        if (paysuccessTimes != null) {
        	String strVaildTime = channelchnQuoteService.getWorkVaildTime(taskid, inscomcode, paysuccessTimes, false);
        	LogUtil.info("getWorkVaildTime支付有效期taskid" + taskid + "到期时间为=" + strVaildTime + "供应商id=" + inscomcode);
        	paysuccessTimes = ModelUtil.conbertStringToDate(strVaildTime);
        }
        
        return paysuccessTimes;
    }
    
    //获取支付有效期public
    public Date getPayValidDate(String taskId, String prvId, boolean addOneDay, boolean queryFlag) throws Exception {
    	INSBPolicyitem pitem = new INSBPolicyitem();
        pitem.setTaskid(taskId);
        pitem.setInscomcode(prvId);
        List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectList(pitem);
    	
        Date syStartDate = null, jqStartDate = null;
        if (policyitemList != null && policyitemList.size() > 0) {
	        for (INSBPolicyitem policyitem : policyitemList) {
	            if ("1".equals(policyitem.getRisktype())) {
	            	jqStartDate = policyitem.getStartdate();
	            } else {
	            	syStartDate = policyitem.getStartdate();
	            }
	        }
        }
        Date result = getPayValidDate(taskId, prvId, syStartDate, jqStartDate, queryFlag);
        //if (result != null && addOneDay) {
        //	result = ModelUtil.nowDateAddOneDay(result);
        //}
        
        return result;
    }

    //获取投保地区
    @Override
    public QuoteBean getAgreementAreas(QuoteBean quoteBean) {
        QuoteBean resultOut = new QuoteBean();
        String channelId = quoteBean.getChannelId();
        String provCode = quoteBean.getAgreementProvCode();
        if (StringUtil.isEmpty(provCode)) {
        	provCode = null; //查所有省
        }
        
    	resultOut.setRespCode("00");
        
        HashMap<String, Object> tempMap = new HashMap<String, Object>();
        tempMap.put("channelinnercode", channelId);
        tempMap.put("province", provCode);
        List<Map<String, Object>> areaDatas = insbAgreementareaDao.getAgreeAreaByChninnercode(tempMap);
        List<AgreementAreaBean> agreementAreas = new ArrayList<AgreementAreaBean>();
        
        for (Map<String, Object> areaData : areaDatas) {              
            String province = (String)areaData.get("province");
            String provinceName = (String)areaData.get("provincename");
            String city = (String)areaData.get("city");
            String cityName = (String)areaData.get("cityname");
            
            AgreementAreaBean agreementAreaMatch = null;
            for (AgreementAreaBean agreementArea : agreementAreas) {
            	if (province.equals(agreementArea.getProvince())) {
            		agreementAreaMatch = agreementArea;
            		break;
            	} 
            }
            
            if (agreementAreaMatch == null) {
            	agreementAreaMatch = new AgreementAreaBean();
            	agreementAreaMatch.setProvince(province);
            	agreementAreaMatch.setProvinceName(provinceName);
            	
            	List<CityBean> cityBeans = new ArrayList<CityBean>();
            	CityBean cityBean = new CityBean();
            	cityBean.setCity(city);
            	cityBean.setCityName(cityName);
            	cityBeans.add(cityBean);
            	
            	agreementAreaMatch.setCitys(cityBeans);
            	agreementAreas.add(agreementAreaMatch);
            } else {
            	List<CityBean> cityBeans = agreementAreaMatch.getCitys();
            	
            	CityBean cityBean = new CityBean();
            	cityBean.setCity(city);
            	cityBean.setCityName(cityName);
            	cityBeans.add(cityBean);
            }    	     
        }

        if (agreementAreas.size() > 0) {
        	resultOut.setAgreementAreas(agreementAreas);
        } else {
        	resultOut.setRespCode("01");
        	resultOut.setErrorMsg("暂没有地区支持车险业务");
        }
        
        return resultOut;
    }
    
	/**
	 * 获取token--渠道内嵌页面
	 */
    @Override
    public QuoteBean getInnerToken(QuoteBean quoteBeanIn) throws Exception {
        QuoteBean resultOut = new QuoteBean();
    	String channelId = quoteBeanIn.getChannelId();
    	String channelUserId = quoteBeanIn.getChannelUserId();
    	
    	if (!channelchnQuoteService.hasInterfacePower("12", null, channelId)) { //12-获取token--渠道内嵌页面
    		resultOut.setRespCode("01");
    		resultOut.setErrorMsg("没有调用该接口的权限!");
            return resultOut;
        }
        
        if ( StringUtils.isEmpty(channelId) || StringUtils.isEmpty(channelUserId) ) {
        	resultOut.setRespCode("01");
        	resultOut.setErrorMsg("渠道ID和用户ID不能为空！");
            return resultOut;
        }

        String token = CHNTokenGenerate.generateToken();
        redisClient.set(CHANNEL_INNER_TOKEN, token, JsonUtils.serialize(quoteBeanIn),300);
        
        //resultOut.setAccessToken(token);
        resultOut.setInnerAuthCode(token);
        resultOut.setRespCode("00");
        return resultOut;
    }
    
    /**
	 * 获取token--渠道内嵌页面
	 */
    @Override
    public QuoteBean verifyInnerToken(QuoteBean quoteBeanIn, HttpServletRequest request) throws Exception {
        QuoteBean resultOut = new QuoteBean();
        resultOut.setRespCode("00");

        //String token = quoteBeanIn.getAccessToken();
        String token = quoteBeanIn.getInnerAuthCode();
        String sessionId = request.getSession().getId();
        String cacheSessionValue = (String)redisClient.get(SecurityDefaultInterceptor.CM_SECURITY_SESSION, sessionId);
        LogUtil.info("▇channel-verifyInnerToken-sessionId：" + sessionId);
        LogUtil.info("▇channel-verifyInnerToken-sessionValue：" + cacheSessionValue);

        if ( StringUtil.isNotEmpty(cacheSessionValue) ) { //渠道内嵌首页验证通过后但是重新加载了
            String[] csvArra = cacheSessionValue.split("#"); //格式：时间#jsonStr
            LogUtil.info("▇channel-verifyInnerToken-sessionValue_jsonStr：" + csvArra[1]);
            QuoteBean innerTokenBeanCsv = JsonUtils.deserialize(csvArra[1], QuoteBean.class);
            resultOut.setChannelId(innerTokenBeanCsv.getChannelId());
            resultOut.setChannelUserId(innerTokenBeanCsv.getChannelUserId());
            resultOut.setRetUrl(innerTokenBeanCsv.getRetUrl());
            resultOut.setInnerShowTitle(innerTokenBeanCsv.getInnerShowTitle());
            return resultOut;
        }

        String innerTokenValue = String.valueOf(redisClient.get(CHANNEL_INNER_TOKEN, token));
        if ( StringUtil.isEmpty(innerTokenValue) ) {
            resultOut.setRespCode("01");
            resultOut.setErrorMsg("无效的请求");
            return resultOut;
        }

        LogUtil.info("▇channel-verifyInnerToken-value：" + innerTokenValue);
        QuoteBean innerTokenBean = JsonUtils.deserialize(innerTokenValue, QuoteBean.class);
        resultOut.setChannelId(innerTokenBean.getChannelId());
        resultOut.setChannelUserId(innerTokenBean.getChannelUserId());
        resultOut.setRetUrl(innerTokenBean.getRetUrl());
        resultOut.setInnerShowTitle(innerTokenBean.getInnerShowTitle());

        redisClient.del(CHANNEL_INNER_TOKEN, token);
        long invalidTime = System.currentTimeMillis();
        redisClient.set(SecurityDefaultInterceptor.CM_SECURITY_SESSION, sessionId, invalidTime + "#" + innerTokenValue,24*60*60);

        return resultOut;
    }

    @Override
    public String queryPlatInfo(String taskId,String prvId ,String channelId,String flowType){
        Map<String ,Object> resultMap = new HashMap<>();
        try {
            String interfaceId = "19";
            if (!channelchnQuoteService.hasInterfacePower(interfaceId, null, channelId)) {
                if("newflow".equals(flowType)) {
                    resultMap.put("code", "-1");
                    resultMap.put("msg", "没有调用该接口的权限!");
                }else {
                    resultMap.put("respCode", "01");
                    resultMap.put("errorMsg", "没有调用该接口的权限!");
                }
                return JsonUtils.serialize(resultMap);
            }
            INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
            insbQuotetotalinfo.setTaskid(taskId);
            insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);


            if (insbQuotetotalinfo != null && channelId.equals(insbQuotetotalinfo.getPurchaserChannel())) {
                Map<String, Object> otherInfo = insbCarinfoService.getCarTaskOtherInfo(taskId, prvId, "SHOW");//其他信息
                Map<String, Object> rulequery = insbCarinfoService.getCarInfoByTaskId(taskId);//平台规则查询信息

                INSBAgreementinterface insbAgreementinterface = new INSBAgreementinterface();
                insbAgreementinterface.setInterfaceid(interfaceId);
                insbAgreementinterface.setChannelinnercode(channelId);
                insbAgreementinterface = insbAgreementinterfaceDao.selectOne(insbAgreementinterface);

                if(insbAgreementinterface == null){
                    if("newflow".equals(flowType)) {
                        resultMap.put("code", "-1");
                        resultMap.put("msg", "没有调用该接口的权限!");
                    }else {
                        resultMap.put("respCode", "01");
                        resultMap.put("errorMsg", "没有调用该接口的权限!");
                    }
                    return JsonUtils.serialize(resultMap);
                }

                //1:投保类型
                //0-关闭 1-开启
                if("1".equals(insbAgreementinterface.getPv1())) {
                    resultMap.put("firstInsureType", otherInfo.getOrDefault("firstInsureType", ""));//投保类型
                }

                //2：出险次数
                //0-关闭 1-开启
                if("1".equals(insbAgreementinterface.getPv2())) {
                    Map<String, Object> claimtimes = new HashMap<>();
                    claimtimes.put("syclaimtimes", otherInfo.getOrDefault("syclaimtimes", ""));//商业险出险次数
                    claimtimes.put("jqclaimtimes", otherInfo.getOrDefault("jqclaimtimes", ""));//交强险出险次数
                    resultMap.put("claimtimes", claimtimes);
                }

                //3:上年保单
                //0-关闭 1-开启
                if("1".equals(insbAgreementinterface.getPv3())) {
                    Map<String, Object> lastYearPolicyInfo = new HashMap<>();
                    //商业险平台记录
                    lastYearPolicyInfo.put("inscorpname0", rulequery.getOrDefault("inscorpname0", ""));//上年投保公司
                    lastYearPolicyInfo.put("policyid0", rulequery.getOrDefault("policyid0", ""));//保单号
                    lastYearPolicyInfo.put("policystarttime0", rulequery.getOrDefault("policystarttime0", ""));//平台起保日期
                    lastYearPolicyInfo.put("policyendtime0", rulequery.getOrDefault("policyendtime0", ""));//平台终保日期
                    //交强险平台记录
                    lastYearPolicyInfo.put("inscorpname1", rulequery.getOrDefault("inscorpname1", ""));//上年投保公司
                    lastYearPolicyInfo.put("policyid1", rulequery.getOrDefault("policyid1", ""));//保单号
                    lastYearPolicyInfo.put("policystarttime1", rulequery.getOrDefault("policystarttime1", ""));//平台起保日期
                    lastYearPolicyInfo.put("policyendtime1", rulequery.getOrDefault("policyendtime1", ""));//平台终保日期
                    resultMap.put("lastYearPolicyInfo", lastYearPolicyInfo);
                }
                //4：出险信息
                //0-关闭 1-开启
                if("1".equals(insbAgreementinterface.getPv4())) {
                    Map<String, Object> claimesInfo = new HashMap<>();
                    //商业险
                    claimesInfo.put("syclaimes", rulequery.getOrDefault("syclaimes", ""));//
        /*
         <#list rulequery.syclaimes as syclaim>
				    <tr>
						<td >${syclaim.casestarttimec0}</td>
						<td >${syclaim.caseendtimec0}</td>
						<td >${syclaim.claimamountc0}</td>
						<td >${syclaim.policyidc0}</td>
						<td >${syclaim.inscorpnamec0}</td>
					</tr>
				</#list>
         */
                    //交强险
                    claimesInfo.put("jqclaimes", rulequery.getOrDefault("jqclaimes", ""));//
        /*
        <#list rulequery.jqclaimes as jqclaim>
				    <tr>
						<td>${jqclaim.casestarttimec1}</td>
						<td>${jqclaim.caseendtimec1}</td>
						<td>${jqclaim.claimamountc1}</td>
						<td>${jqclaim.policyidc1}</td>
						<td>${jqclaim.inscorpnamec1}</td>
					</tr>
				</#list>
         */
                    resultMap.put("claimesInfo", claimesInfo);
                }
                if("newflow".equals(flowType)) {
                    resultMap.put("code", "0");
                    resultMap.put("msg", "查询平台信息成功！");
                }else {
                    resultMap.put("respCode", "00");
                    resultMap.put("errorMsg", "查询平台信息成功！");
                }
            } else {
                if("newflow".equals(flowType)) {
                    resultMap.put("code", "-1");
                    resultMap.put("msg", "没有查询该任务平台信息的权限！");
                }else {
                    resultMap.put("respCode", "01");
                    resultMap.put("errorMsg", "没有查询该任务平台信息的权限！");
                }
            }
        }catch (Exception e){
            LogUtil.info(taskId+"查询平台信息异常！"+e.getMessage());
            e.printStackTrace();
            if("newflow".equals(flowType)) {
                resultMap.put("code", "-1");
                resultMap.put("msg", "查询平台信息异常，请联系管理员！");
            }else {
                resultMap.put("respCode", "01");
                resultMap.put("errorMsg", "查询平台信息异常，请联系管理员！");
            }
        }

        return JsonUtils.serialize(resultMap);
    }

    public String recordPhaseTime(Map<String,String> map){
        String res = "";
        String channelId = map.getOrDefault("channelId","");
        String json = JsonUtils.serialize(map);
        try {
            INSCCode inscCode = new INSCCode();
            inscCode.setParentcode("ChannelForMini");
            inscCode.setCodetype("channelurl");
            inscCode.setCodevalue("01");
            inscCode = inscCodeService.queryOne(inscCode);
            LogUtil.info("记录时间埋点开始："+json);
            res = CallChnInterfaceUtil.callInterface(inscCode.getProp2(), "recordPhaseTime", json,channelId);
            LogUtil.info("记录时间埋点完成："+json+" res="+res);
        }catch (Exception e){
            LogUtil.info("记录时间埋点异常："+json+e.getMessage());
            e.printStackTrace();
        }
        return  res;
    }
    
    /**
	 * 北京平台，核保完成支付前，验证被保人身份证信息以及获取验证码
	 */
    @Override
    public QuoteBean checkIDCardAndGetPin(QuoteBean quoteBeanIn) throws Exception {
    	QuoteBean resultOut = new QuoteBean();
    	resultOut.setRespCode("00");
    	
    	String taskId = quoteBeanIn.getTaskId();
		String inscomcode = quoteBeanIn.getPrvId();
		IDCardBean insuredIdCardBean = quoteBeanIn.getInsuredIdCard();
		
    	INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
        insbQuotetotalinfo.setTaskid(taskId);
        insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
        
		String agentId = insbQuotetotalinfo.getAgentnum();
		resultOut = this.pinCodeBJCheckInterface(insbQuotetotalinfo, inscomcode, null);
		if ( "01".equals(resultOut.getRespCode()) ) {
			return resultOut;
		}
		IDCardModel idCardModel = new IDCardModel();
		PropertyUtils.copyProperties(idCardModel, insuredIdCardBean);
		
		idCardModel.setTaskid(taskId);
		idCardModel.setInscomcode(inscomcode);
		idCardModel.setAgentid(agentId);
		
		Map<String, String> result = enternalInterFaceService.checkIDCardAndGetPinCode(idCardModel, "zzb");
		
		String status = result.get("status");
		if ( "fail".equals(status) ) {
			resultOut.setRespCode("01");
			resultOut.setErrorMsg(result.get("errormsg"));
			return resultOut;
		}
		
		return resultOut;
    }
    
    /**
	 * 北京平台，录入验证码提交
	 * @return 失败，可重新输入验证码再次提交；若成功，就可选择支付方式，进行支付
	 */
    @Override
    public QuoteBean commitPinCode(QuoteBean quoteBeanIn) throws Exception {
    	QuoteBean resultOut = new QuoteBean();
    	resultOut.setRespCode("00");
    	
    	String taskId = quoteBeanIn.getTaskId();
		String inscomcode = quoteBeanIn.getPrvId();
		String pinCode = quoteBeanIn.getPinCode();
		
    	INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
        insbQuotetotalinfo.setTaskid(taskId);
        insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
        
		String agentId = insbQuotetotalinfo.getAgentnum();
		resultOut = this.pinCodeBJCheckInterface(insbQuotetotalinfo, inscomcode, null);
		if ( "01".equals(resultOut.getRespCode()) ) {
			return resultOut;
		}
		Map<String, String> result = enternalInterFaceService.commitPinCode(agentId, taskId, inscomcode, pinCode);
		
		String status = result.get("status");
		if ( "fail".equals(status) ) {
			resultOut.setRespCode("01");
			resultOut.setErrorMsg(result.get("errormsg"));
			return resultOut;
		}
		
		return resultOut;
    }
    
    /**
	 * 北京平台，获取身份证信息验证的结果，验证码结果
	 */
    @Override
    public QuoteBean getPinCodeBJ(QuoteBean quoteBeanIn) throws Exception {
    	QuoteBean resultOut = new QuoteBean();
    	resultOut.setRespCode("00");
    	
    	String taskId = quoteBeanIn.getTaskId();
		String inscomcode = quoteBeanIn.getPrvId();
		
    	INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
        insbQuotetotalinfo.setTaskid(taskId);
        insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
        
		String agentId = insbQuotetotalinfo.getAgentnum();
		resultOut = this.pinCodeBJCheckInterface(insbQuotetotalinfo, inscomcode, null);
		if ( "01".equals(resultOut.getRespCode()) ) {
			return resultOut;
		}
		
		String strResult = enternalInterFaceService.getPincodeResultInfo(agentId, taskId, inscomcode);
		
		JSONObject jsonObject = JSONObject.fromObject(strResult);
		String status = jsonObject.getString("status");
		if ( "fail".equals(status) ) {
			resultOut.setRespCode("01");
			resultOut.setErrorMsg(jsonObject.getString("errormsg"));
			return resultOut;
		} else if ( "success".equals(status) ) {
			resultOut.setApplyStatus(jsonObject.getString("applysta"));
			resultOut.setCommitStatus(jsonObject.getString("commitsta"));
		}
		
		return resultOut;
    }
    
    /**
	 * 北京平台，重新发起申请验证码
	 */
    @Override
    public QuoteBean reapplyPin(QuoteBean quoteBeanIn) throws Exception {
    	QuoteBean resultOut = new QuoteBean();
    	resultOut.setRespCode("00");
    	
    	String taskId = quoteBeanIn.getTaskId();
		String inscomcode = quoteBeanIn.getPrvId();
		
    	INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
        insbQuotetotalinfo.setTaskid(taskId);
        insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
        
		String agentId = insbQuotetotalinfo.getAgentnum();
		resultOut = this.pinCodeBJCheckInterface(insbQuotetotalinfo, inscomcode, null);
		if ( "01".equals(resultOut.getRespCode()) ) {
			return resultOut;
		}
		
		String strResult = enternalInterFaceService.reapplyPinCode(agentId, taskId, inscomcode);
		
		JSONObject jsonObject = JSONObject.fromObject(strResult);
		String status = jsonObject.getString("status");
		if ( "fail".equals(status) ) {
			resultOut.setRespCode("01");
			resultOut.setErrorMsg(jsonObject.getString("errormsg"));
			return resultOut;
		}
		
		return resultOut;
    }
    
    //北京平台流程相关接口检查--只有北京流程&被保人为非企业车&是指定的四家保险公司，才允许调用这些接口
    private QuoteBean pinCodeBJCheckInterface(INSBQuotetotalinfo insbQuotetotalinfo, String prvId, Date payValidDate) throws Exception {
    	QuoteBean resultOut = new QuoteBean();
    	resultOut.setRespCode("00");
    	
    	if ( !StatusCodeMapperUtil.pinCodePrv.contains(prvId.substring(0, 4)) ) {
    		resultOut.setRespCode("01");
    		resultOut.setErrorMsg("该订单不需验证码，请勿调用此接口");
    		return resultOut;
    	}
    	
    	INSBRegion insbRegion = new INSBRegion();
    	insbRegion.setParentid("110000"); //北京
    	insbRegion.setComcode(insbQuotetotalinfo.getInscitycode()); 
    	if ( insbRegionDao.selectCount(insbRegion) <= 0 ) {
    		resultOut.setRespCode("01");
    		resultOut.setErrorMsg("该订单不需验证码，请勿调用此接口");
    		return resultOut;
    	}
    	
    	INSBPerson insuredPerson = insbPersonDao.selectInsuredPersonByTaskId(insbQuotetotalinfo.getTaskid());
    	int idCardType = insuredPerson.getIdcardtype();
    	//“6-组织机构代码证”、“8-社会信用代码证”、“9-税务登记证”、“10-营业执照”
    	if ( StatusCodeMapperUtil.enterpriseIdCardType.contains(idCardType + "") ) {
    		resultOut.setRespCode("01");
    		resultOut.setErrorMsg("该订单不需验证码，请勿调用此接口");
    		return resultOut;
    	}
    	
    	if ( "FirstPayLastInsure".equals(insbQuotetotalinfo.getSourceFrom()) ) {
    		if ( payValidDate == null ) {
    			payValidDate = this.getPayValidDate(insbQuotetotalinfo.getTaskid(), prvId, false, true);
    		}
    		if ( new Date().getTime() > payValidDate.getTime() ) {
    			resultOut.setRespCode("01");
        		resultOut.setErrorMsg("订单超过有效期，请申请退款");
        		return resultOut;
    		}
    	}
    	
    	return resultOut;
    }
    
    //北京平台流程相关接口检查
    public boolean pinCodeBJCheck(INSBQuotetotalinfo insbQuotetotalinfo, String prvId, Date payValidDate) throws Exception {
    	QuoteBean quoteBean = pinCodeBJCheckInterface(insbQuotetotalinfo, prvId, payValidDate);
    	if ( "01".equals(quoteBean.getRespCode()) ) {
    		return true;
    	}
    	
    	//北京地区非企业车
		String strResult = enternalInterFaceService.getPincodeResultInfo(insbQuotetotalinfo.getAgentnum(), insbQuotetotalinfo.getTaskid(), prvId);
		
		JSONObject jsonObject = JSONObject.fromObject(strResult);
		String status = jsonObject.getString("status");
		if ( "fail".equals(status) ) {
			throw new Exception(jsonObject.getString("errormsg"));
		} else if ( "success".equals(status) ) {
			//1-验证码提交成功
			if ( "1".equals(jsonObject.getString("commitsta")) ) {
				return true;
			} 
		}
    	
    	return false;
    }

    public QuoteBean getAgreementAreasForChn(QuoteBean quoteBean){
        QuoteBean resultOut = new QuoteBean();
        String channelId = quoteBean.getChannelId();
        String provCode = quoteBean.getAgreementProvCode();
        if (StringUtil.isEmpty(provCode)) {
            provCode = null; //查所有省
        }

        resultOut.setRespCode("00");

        HashMap<String, Object> tempMap = new HashMap<String, Object>();
        tempMap.put("channelinnercode", channelId);
        tempMap.put("province", provCode);
        List<Map<String, Object>> areaDatas = insbAgreementareaDao.getAgreeAreaAndJobNumByChninnercode(tempMap);
        List<AgreementAreaBean> agreementAreas = new ArrayList<AgreementAreaBean>();

        for (Map<String, Object> areaData : areaDatas) {
            String province = (String)areaData.get("province");
            String provinceName = (String)areaData.get("provincename");
            String city = (String)areaData.get("city");
            String cityName = (String)areaData.get("cityname");
            String jobnum = (String)areaData.get("jobnum");

            AgreementAreaBean agreementAreaMatch = null;
            for (AgreementAreaBean agreementArea : agreementAreas) {
                if (province.equals(agreementArea.getProvince())) {
                    agreementAreaMatch = agreementArea;
                    break;
                }
            }

            if (agreementAreaMatch == null) {
                agreementAreaMatch = new AgreementAreaBean();
                agreementAreaMatch.setProvince(province);
                agreementAreaMatch.setProvinceName(provinceName);

                List<CityBean> cityBeans = new ArrayList<CityBean>();
                CityBean cityBean = new CityBean();
                cityBean.setCity(city);
                cityBean.setCityName(cityName);
                cityBean.setJobnum(jobnum);
                cityBeans.add(cityBean);

                agreementAreaMatch.setCitys(cityBeans);
                agreementAreas.add(agreementAreaMatch);
            } else {
                List<CityBean> cityBeans = agreementAreaMatch.getCitys();
                boolean cityExists = false;
                //有重复地区时只取一笔记录
                for(CityBean cityBeanIn : cityBeans){
                    if(city.equals(cityBeanIn.getCity())){
                        cityExists = true;
                    }
                }
                if( !cityExists ) {
                    CityBean cityBean = new CityBean();
                    cityBean.setCity(city);
                    cityBean.setCityName(cityName);
                    cityBean.setJobnum(jobnum);
                    cityBeans.add(cityBean);
                }
            }
        }

        if (agreementAreas.size() > 0) {
            resultOut.setAgreementAreas(agreementAreas);
        } else {
            resultOut.setRespCode("01");
            resultOut.setErrorMsg("暂没有地区支持车险业务");
        }

        return resultOut;

    }
    
}
 