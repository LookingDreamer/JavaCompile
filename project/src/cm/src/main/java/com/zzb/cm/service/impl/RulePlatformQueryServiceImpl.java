package com.zzb.cm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.common.*;
import com.zzb.cm.service.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.Task;
import com.cninsure.jobpool.dispatch.DispatchTaskService;
import com.cninsure.jobpool.timer.SchedulerService;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCDeptService;
import com.zzb.cm.Interface.service.FlowInfo;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarmodelinfoDao;
import com.zzb.cm.dao.INSBFlowerrorDao;
import com.zzb.cm.dao.INSBLastyearinsurestatusDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBRulequerycarinfoDao;
import com.zzb.cm.dao.INSBRulequeryclaimsDao;
import com.zzb.cm.dao.INSBRulequeryotherinfoDao;
import com.zzb.cm.dao.INSBRulequeryrepeatinsuredDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.entity.INSBFlowerror;
import com.zzb.cm.entity.INSBFlowinfo;
import com.zzb.cm.entity.INSBLastyearinsurestatus;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBRulequerycarinfo;
import com.zzb.cm.entity.INSBRulequeryclaims;
import com.zzb.cm.entity.INSBRulequeryotherinfo;
import com.zzb.cm.entity.INSBRulequeryrepeatinsured;
import com.zzb.conf.component.SupplementCache;
import com.zzb.conf.dao.INSBAgreementDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.entity.INSBAgreement;
import com.zzb.conf.entity.INSBAutoconfigshow;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.service.INSBAutoconfigshowService;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.mobile.dao.AppInsuredQuoteDao;
import com.zzb.mobile.model.CommonModel;

@Service
@Transactional
public class RulePlatformQueryServiceImpl implements RulePlatformQueryService {

	@Resource
	private INSBLastyearinsurestatusDao insbLastyearinsurestatusDao;
	@Resource
	private INSBWorkflowmainService workflowmainService;
	@Resource
	private INSBRulequerycarinfoDao insbRulequerycarinfoDao;
	@Resource
	private INSBRulequeryrepeatinsuredDao insbRulequeryrepeatinsuredDao;
	@Resource
	private INSBRulequeryclaimsDao insbRulequeryclaimsDao;
	@Resource
	private INSBRulequeryotherinfoDao insbRulequeryotherinfoDao;
	@Resource
	private INSBFlowinfoService insbFlowinfoService;//工作流自状态流程信息表
	@Resource
	private INSBFlowlogsService insbFlowlogsService;
	@Resource
	private  Scheduler sched;
	@Resource
	private AppInsuredQuoteDao appInsuredQuoteDao;
	@Resource
	private INSBFlowerrorDao insbFlowerrorDao;
	@Resource
	private INSBCommonQuoteinfoService INSBCommonQuoteinfoService;
	@Resource
	private SupplementCache supplementCache;
	@Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
	@Resource
	private INSBPolicyitemDao policyitemDao;
	@Resource
	private INSBProviderDao insbProviderDao;
    @Resource
    private INSBWorkflowsubDao insbWorkflowsubDao;
    @Resource
	private INSBAutoconfigshowService insbAutoconfigshowService;
    @Resource
	private INSCDeptService inscDeptservice;
    @Resource
    private SchedulerService schedulerService;
    @Resource
    private INSBCarinfoDao insbCarinfoDao;
    @Resource
    private INSBCarmodelinfoDao insbCarmodelinfoDao;
    @Resource
    private INSBManualPriceService insbManualPriceService;
    @Resource
    private INSBQuoteinfoDao insbQuoteinfoDao;
    @Resource
    private INSBAgreementDao insbAgreementDao;
    @Resource
	private DispatchTaskService dispatchTaskService;
	@Resource
	private INSBRulequeryrepeatinsuredService insbRulequeryrepeatinsuredService;
	
	/***
	 * {"taskid":"12121","inscomcode":"202744"}
	 */
	@Override
	public CommonModel startRuleQuery(String json) {
		CommonModel commonModel = new CommonModel();
		try {
			JSONObject jsonObject = JSONObject.fromObject(json);
			if(!jsonObject.containsKey("taskid") || !jsonObject.containsKey("inscomcode")){
				commonModel.setStatus("fail");
				commonModel.setMessage("数据不完整");
			}
			String taskid = jsonObject.getString("taskid");
			String inscomcode = jsonObject.getString("inscomcode");
			//更新规则平台状态表
			Date nowdate = new Date();
			INSBLastyearinsurestatus insbLastyearinsurestatus = new INSBLastyearinsurestatus();
			insbLastyearinsurestatus.setTaskid(taskid);
			INSBLastyearinsurestatus lastyearinsurestatus = insbLastyearinsurestatusDao.selectOne(insbLastyearinsurestatus);
			if(null != lastyearinsurestatus){
				lastyearinsurestatus.setModifytime(nowdate);
				lastyearinsurestatus.setGzptstarttime(nowdate);
				lastyearinsurestatus.setGzptflag("0");//默认未超时
				insbLastyearinsurestatusDao.updateById(lastyearinsurestatus);
				LogUtil.info("开始调用平台规则查询taskid="+ taskid +"=供应商id="+ inscomcode + "=更新规则平台状态表成功=");
			}else{
				LogUtil.info("开始调用平台规则查询taskid="+ taskid +"=供应商id="+ inscomcode + "=规则平台状态表数据不存在,数据有问题=");
			}
			boolean issuccess = true;
			LogUtil.info("开始调用平台规则查询taskid="+ taskid +"=供应商id="+ inscomcode);
			try {
				String realinscomcode = getHaveGzqueryProvid(taskid);
//				String result = getQueryGuizeDataparam(taskid, inscomcode,"quote", "admin",realinscomcode);
				LogUtil.info(realinscomcode+"开始调用平台规则查询taskid="+ taskid +"=供应商id="+ inscomcode +"=接口返回result="+ true);
			} catch (Exception e) {
				e.printStackTrace();
				issuccess = false;
				LogUtil.info("开始调用平台规则查询taskid="+ taskid +"=供应商id="+ inscomcode + "抛异常了啦");
			}
			long endtime = nowdate.getTime() + 60000;
			//启动定时器
			Date startTime = new Date(endtime);
			//获取子流程id 
			Map<String, String> map = new HashMap<String, String>();
			map.put("taskid", taskid);
			map.put("inscomcode", inscomcode);
			INSBQuoteinfo quoteinfo = appInsuredQuoteDao.selectInsbQuoteInfoByTaskidAndPid(map);
			if(null != quoteinfo){
				if(issuccess){
					String jobName = quoteinfo.getWorkflowinstanceid()+"_平台查询_"+taskid;
					//开启任务前清除历史定时任务
					schedulerService.deleteHistoryJobAndStartNewJob(jobName, "平台查询", startTime, inscomcode, "40000", "TaskOvertimeJob4EdiFairy");
				}else{
					LogUtil.info("开始调用平台规则查询taskid="+taskid+"=抛异常了,直接推流程");
					nextTodoRuleQuery(taskid, inscomcode, quoteinfo.getWorkflowinstanceid());
				}
				
			}else{
				LogUtil.info("开始调用平台规则查询taskid="+taskid+"报价信息表没有数据,数据有误");
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
	 * 判断代理人已经选择的供应商列表中有没有拥有规则平台查询能力的供应商
	 * @return 返回拥有规则平台查询能力的供应商id
	 */
	private String getHaveGzqueryProvid(String taskid){
		String result = "";
		List<INSBQuoteinfo> insbQuoteinfos = appInsuredQuoteDao.getSelectedProvidersByTaskid(taskid);
		if(null != insbQuoteinfos && insbQuoteinfos.size() > 0){
			INSCDept deptPlatform = inscDeptservice.getPlatformDept(insbQuoteinfos.get(0).getDeptcode());
			List<INSBAutoconfigshow> lists = insbAutoconfigshowService.getOneAbilityByDeptIdQuotype(null!=deptPlatform?deptPlatform.getComcode():insbQuoteinfos.get(0).getDeptcode().substring(0,5)+"00000", "05", "02");
			for(INSBQuoteinfo insbQuoteinfo : insbQuoteinfos){
				for(int i = 0;i < lists.size();i ++ ){
					if(!insbQuoteinfo.getInscomcode().startsWith(lists.get(i).getProviderid())){
						result = lists.get(i).getProviderid();
						break;
					}
				}
			}
		}
		LogUtil.info("代理人选择的拥有规则平台查询能力taskid="+taskid+"=的供应商为pid="+result);
		return result;
	}
	
	/**
	 * 如果存在,先删除
	 * @param sched
	 * @param jobName
	 */
	private void deleteHistoryJob(Scheduler sched, String jobName) {
		JobKey jobKey = new JobKey(jobName);
		try {
			if(sched.deleteJob(jobKey)){
				LogUtil.info("开始调用平台规则查询jobname="+jobName+"已经存在先删除");
			}else{
				LogUtil.info("开始调用平台规则查询jobname="+jobName+"不存在");
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	private String getQueryGuizeDataparam(String taskid,String inscomcode,String taskType,String touserid,String realinscomcode) throws Exception{
		LogUtil.info("规则平台查询getQueryGuizeDataparam:taskid=" + taskid + ",inscomcode=" + inscomcode + ",代理人选择拥有能力的供应商id=" + realinscomcode);
		Map<String, String> messageMap = new HashMap<String, String>();
		if(!StringUtil.isEmpty(realinscomcode)){
			inscomcode = realinscomcode;
			messageMap.put("requestDataUrl", "http://"+ValidateUtil.getConfigValue("localhost.ip")+":" + ValidateUtil.getConfigValue("localhost.port") + "/"+ValidateUtil.getConfigValue("localhost.projectName")+"/interface/getpacket");
		}else{
			messageMap.put("requestDataUrl", "http://"+ValidateUtil.getConfigValue("localhost.ip")+":" + ValidateUtil.getConfigValue("localhost.port") + "/"+ValidateUtil.getConfigValue("localhost.projectName")+"/interface/getgzquerydata");
		}
		messageMap.put("isReserved", "true");//备用平台标识true表示备用标识有效，false表示不是备用
		messageMap.put("processType", "robot");//精灵、人工、edi//robot
		messageMap.put("isrenewal", "false");//是否是续保-true续保单-false非续保单
		messageMap.put("isTry", "false");//是否试用任务
		messageMap.put("enquiryId", taskid+"@"+inscomcode);//单方询价号
		messageMap.put("taskStatus", "0");//流程状态
		messageMap.put("isLocked", "false");//是否锁定任务
		messageMap.put("taskType", taskType);
		messageMap.put("companyId", inscomcode);//保险公司id
		messageMap.put("businessId", taskid+"@"+inscomcode);//taskid,任务id
		messageMap.put("callBackUrl", "http://"+ValidateUtil.getConfigValue("localhost.ip")+":" + ValidateUtil.getConfigValue("localhost.port") + "/"+ValidateUtil.getConfigValue("localhost.projectName")+"/workflow/saverulequeryinfo");
		messageMap.put("monitorid", "1111_2222");//监控用的,目前是虚拟写死的
		INSBFlowinfo qinsbFlowinfo = new INSBFlowinfo();
		qinsbFlowinfo.setTaskid(taskid);
		qinsbFlowinfo.setInscomcode(inscomcode);
		qinsbFlowinfo.setFiroredi("0");
		INSBFlowinfo dataInsbFlowinfo = insbFlowinfoService.queryOne(qinsbFlowinfo);
		if(dataInsbFlowinfo==null){
			INSBFlowinfo insbFlowinfo = new INSBFlowinfo();
			insbFlowinfo.setOperator("sys");
			insbFlowinfo.setCreatetime(new Date());
			insbFlowinfo.setTaskid(taskid);
			insbFlowinfo.setInscomcode(inscomcode);
			insbFlowinfo.setCurrenthandle(touserid);
			insbFlowinfo.setFlowcode(FlowInfo.quoteapply.getCode());
			insbFlowinfo.setFlowname(FlowInfo.quoteapply.getDesc());
			insbFlowinfo.setFiroredi("0");
			insbFlowinfo.setIslock("0");
			insbFlowinfo.setTaskstatus(taskType);
			insbFlowinfoService.insert(insbFlowinfo);
			insbFlowlogsService.logs(insbFlowinfo);
		}else{
			dataInsbFlowinfo.setOverflowcode(dataInsbFlowinfo.getFlowcode());
			dataInsbFlowinfo.setOverflowname(dataInsbFlowinfo.getFlowname());
			dataInsbFlowinfo.setModifytime(new Date());
			dataInsbFlowinfo.setCurrenthandle(touserid);
			dataInsbFlowinfo.setTaskstatus(taskType);
			dataInsbFlowinfo.setFlowcode(FlowInfo.guizequerty.getCode());
			dataInsbFlowinfo.setFlowname(FlowInfo.guizequerty.getDesc());
			insbFlowinfoService.updateById(dataInsbFlowinfo);
			insbFlowlogsService.logs(dataInsbFlowinfo);
		}
		String targetUrl = ValidateUtil.getConfigValue("gzrule.query");//"http://119.29.80.164:8080";
		LogUtil.info("规则平台查询getQueryGuizeDataparam:taskid=" + taskid + ",inscomcode=" + inscomcode + ",touserid=" + touserid + ",调用精灵任务通知接口：" + targetUrl + ",请求参数：" + JSONObject.fromObject(messageMap).toString() + "====");
		String result = HttpClientUtil.doPostJson(targetUrl, messageMap, "UTF-8");
		LogUtil.info("规则平台查询getQueryGuizeDataparam:taskid=" + taskid + ",inscomcode=" + inscomcode + ",touserid=" + touserid + ",调用精灵任务通知接口：" + targetUrl + ",result：" + result + "====");
		if(!StringUtil.isEmpty(result)){
			INSBFlowinfo qinsbFlowinfo1 = new INSBFlowinfo();
			qinsbFlowinfo1.setTaskid(taskid);
			qinsbFlowinfo1.setInscomcode(inscomcode);
			qinsbFlowinfo1.setFiroredi("0");
			qinsbFlowinfo1.setTaskstatus(taskType);
			INSBFlowinfo dataInsbFlowinfo1 = insbFlowinfoService.queryOne(qinsbFlowinfo1);
			dataInsbFlowinfo1.setOverflowcode(dataInsbFlowinfo1.getFlowcode());
			dataInsbFlowinfo1.setOverflowname(dataInsbFlowinfo1.getFlowname());
			dataInsbFlowinfo1.setModifytime(new Date());
			dataInsbFlowinfo1.setCurrenthandle(touserid);
			dataInsbFlowinfo1.setTaskstatus(taskType);
			dataInsbFlowinfo1.setFlowcode(FlowInfo.guizequerty.getCode());
			dataInsbFlowinfo1.setFlowname(FlowInfo.guizequerty.getDesc());
			insbFlowinfoService.updateById(dataInsbFlowinfo1);
			insbFlowlogsService.logs(dataInsbFlowinfo1);
		}
		Map<String,Object> RequestMap = new HashMap<String,Object>();
		RequestMap.put("result", true);
		RequestMap.put("processinstanceid", taskid);
		RequestMap.put("incoid", inscomcode);
		RequestMap.put("ediid", touserid);
		RequestMap.put("taskType", taskType);
		LogUtil.info("规则平台查询getQueryGuizeDataparam:taskid=" + taskid + ",inscomcode=" + inscomcode + ",touserid=" + touserid + ",返回结果：" + result + "====");
		return JSONObject.fromObject(RequestMap).toString();
	}
	
	/**
	 * 江苏流程
	 */
	@Override
	public  String saveRuleQueryInfoJiangSu(String json) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		String result = "success";
		try {
			LogUtil.info("江苏流程平常查询回调CM, json=" + json);
			String taskid = null;
			if(jsonObject.containsKey("carinfo")){
				JSONObject carinfo = JSONObject.fromObject(jsonObject.get("carinfo"));
				if(carinfo.containsKey("orderno")){
					taskid = carinfo.getString("orderno");
					saveJiangSuInfo(jsonObject, taskid);
					
					LogUtil.info("江苏流程平常查询回调CM, 数据保存完毕!");
				} else {
					LogUtil.info("江苏流程平常查询回调CM, 任务号为空, taskId=" + carinfo.getString("orderno"));
					result = "fail";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "fail";
		}
		return result;
	}
	
	/**
	 * (江苏流程)保存平台查询返回的数据 
	 * @param jsonObject
	 * @param taskid
	 */
	public void saveJiangSuInfo(JSONObject jsonObject, String taskid){
		JSONObject carinfo = JSONObject.fromObject(jsonObject.get("carinfo"));
		INSBRulequerycarinfo insbRulequerycarinfo = new INSBRulequerycarinfo();
		insbRulequerycarinfo.setTaskid(taskid);
		insbRulequerycarinfo = insbRulequerycarinfoDao.selectOne(insbRulequerycarinfo);
		if(null == insbRulequerycarinfo){
			insbRulequerycarinfo = new INSBRulequerycarinfo();
			insbRulequerycarinfo.setCreatetime(new Date());
			insbRulequerycarinfo.setModifytime(new Date());
			insbRulequerycarinfo.setOperator("jgguizequery");
			insbRulequerycarinfo.setTaskid(taskid);
			insbRulequerycarinfo.setLicenseno(getStringByObjectkey(carinfo, "vehicleno")); //车牌号
			insbRulequerycarinfo.setVinno(getStringByObjectkey(carinfo, "vin"));//车架号
			insbRulequerycarinfo.setEngineno(getStringByObjectkey(carinfo, "engineNum"));//测试发动机号
			insbRulequerycarinfo.setCarbrandname(getStringByObjectkey(carinfo, "carbrandName"));//车辆品牌名称
			insbRulequerycarinfo.setModelcode(getStringByObjectkey(carinfo, "modelCode"));//车型编码
			insbRulequerycarinfo.setDisplacement(getDoubleByObjectkey(carinfo, "displacement"));//排量
			insbRulequerycarinfo.setSeatcnt(getInterByObjectkey(carinfo, "seancnt"));//核定载客
			insbRulequerycarinfo.setFullload(getDoubleByObjectkey(carinfo, "fullLoad"));//整备质量
			insbRulequerycarinfo.setModelload(getDoubleByObjectkey(carinfo, "modelLoad"));//核定载质量
			insbRulequerycarinfo.setEnrolldate(getStringByObjectkey(carinfo, "registerdate"));//初登日期
			insbRulequerycarinfo.setSupplierid(getStringByObjectkey(carinfo, "supplierid"));//精灵进行交管信息查询的公司
			
			//下面的接口没有返回,设为默认值""
			insbRulequerycarinfo.setPrice(getDoubleByObjectkey(carinfo, "price"));
			insbRulequerycarinfo.setTaxprice(getDoubleByObjectkey(carinfo, "taxPrice"));
			insbRulequerycarinfo.setAnalogyprice(getDoubleByObjectkey(carinfo, "analogyPrice"));
			insbRulequerycarinfo.setAnalogytaxprice(getDoubleByObjectkey(carinfo, "analogyTaxPrice"));
			insbRulequerycarinfo.setCarmodeldate(getStringByObjectkey(carinfo, "carModelDate"));
			insbRulequerycarinfo.setTrademodelcode(getStringByObjectkey(carinfo, "vehicleInfo.tradeModelCode"));
			insbRulequerycarinfo.setSelfinsurerate(getDoubleByObjectkey(carinfo, "car.specific.selfInsureRate"));
			insbRulequerycarinfo.setSelfchannelrate(getDoubleByObjectkey(carinfo, "car.specific.selfChannelRate"));
			insbRulequerycarinfo.setNcdrate(getDoubleByObjectkey(carinfo, "car.specific.NcdRate"));
			insbRulequerycarinfo.setVehicletype(getStringByObjectkey(carinfo, "vehicleInfo.vehicleType"));
			insbRulequerycarinfo.setBasicriskpremium(getDoubleByObjectkey(carinfo, "quoteItems_ruleItem.basicRiskPremium"));
			insbRulequerycarinfo.setInsureco(getStringByObjectkey(carinfo, "plateform.InsureCo"));
			
			insbRulequerycarinfoDao.insert(insbRulequerycarinfo);
		} else {
			insbRulequerycarinfo.setModifytime(new Date());
			if (StringUtil.isEmpty(insbRulequerycarinfo.getLicenseno()))
				insbRulequerycarinfo.setLicenseno(getStringByObjectkey(carinfo, "vehicleno"));
			if (StringUtil.isEmpty(insbRulequerycarinfo.getEngineno()))
				insbRulequerycarinfo.setEngineno(getStringByObjectkey(carinfo, "engineNum"));
			if (StringUtil.isEmpty(insbRulequerycarinfo.getVinno()))
				insbRulequerycarinfo.setVinno(getStringByObjectkey(carinfo, "vin"));
			if (StringUtil.isEmpty(insbRulequerycarinfo.getCarbrandname()))
				insbRulequerycarinfo.setCarbrandname(getStringByObjectkey(carinfo, "carbrandName"));
			if (insbRulequerycarinfo.getDisplacement() == 0.0)
				insbRulequerycarinfo.setDisplacement(getDoubleByObjectkey(carinfo, "displacement"));
			if (insbRulequerycarinfo.getSeatcnt() == 0)
				insbRulequerycarinfo.setSeatcnt(getInterByObjectkey(carinfo, "seancnt"));
			if (insbRulequerycarinfo.getModelload() == 0.0)
				insbRulequerycarinfo.setModelload(getDoubleByObjectkey(carinfo, "modelLoad"));
			if (insbRulequerycarinfo.getFullload() == 0.0)
				insbRulequerycarinfo.setFullload(getDoubleByObjectkey(carinfo, "fullLoad"));
			if (StringUtil.isEmpty(insbRulequerycarinfo.getModelcode()))
				insbRulequerycarinfo.setModelcode(getStringByObjectkey(carinfo, "modelCode"));
			if (StringUtil.isEmpty(insbRulequerycarinfo.getEnrolldate()))
				insbRulequerycarinfo.setEnrolldate(getStringByObjectkey(carinfo, "registerdate"));
			if (StringUtil.isEmpty(insbRulequerycarinfo.getSupplierid()))
				insbRulequerycarinfo.setSupplierid(getStringByObjectkey(carinfo, "supplierid"));
			
			//下面的接口没有返回,设为默认值""
			if (insbRulequerycarinfo.getPrice() == 0.0)
				insbRulequerycarinfo.setPrice(getDoubleByObjectkey(carinfo, "price"));
			if (insbRulequerycarinfo.getTaxprice() == 0.0)
				insbRulequerycarinfo.setTaxprice(getDoubleByObjectkey(carinfo, "taxPrice"));
			if (insbRulequerycarinfo.getAnalogyprice() == 0.0)
				insbRulequerycarinfo.setAnalogyprice(getDoubleByObjectkey(carinfo, "analogyPrice"));
			if (insbRulequerycarinfo.getAnalogytaxprice() == 0.0)
				insbRulequerycarinfo.setAnalogytaxprice(getDoubleByObjectkey(carinfo, "analogyTaxPrice"));
			if (StringUtil.isEmpty(insbRulequerycarinfo.getCarmodeldate()))
				insbRulequerycarinfo.setCarmodeldate(getStringByObjectkey(carinfo, "carModelDate"));
			if (StringUtil.isEmpty(insbRulequerycarinfo.getTrademodelcode()))
				insbRulequerycarinfo.setTrademodelcode(getStringByObjectkey(carinfo, "vehicleInfo.tradeModelCode"));
			if (insbRulequerycarinfo.getSelfinsurerate() == 0.0)
				insbRulequerycarinfo.setSelfinsurerate(getDoubleByObjectkey(carinfo, "car.specific.selfInsureRate"));
			if (insbRulequerycarinfo.getSelfchannelrate() == 0.0)
				insbRulequerycarinfo.setSelfchannelrate(getDoubleByObjectkey(carinfo, "car.specific.selfChannelRate"));
			if (insbRulequerycarinfo.getNcdrate() == 0.0)
				insbRulequerycarinfo.setNcdrate(getDoubleByObjectkey(carinfo, "car.specific.NcdRate"));
			if (StringUtil.isEmpty(insbRulequerycarinfo.getVehicletype()))
				insbRulequerycarinfo.setVehicletype(getStringByObjectkey(carinfo, "vehicleInfo.vehicleType"));
			if (insbRulequerycarinfo.getBasicriskpremium() == 0.0)
				insbRulequerycarinfo.setBasicriskpremium(getDoubleByObjectkey(carinfo, "quoteItems_ruleItem.basicRiskPremium"));
			if (StringUtil.isEmpty(insbRulequerycarinfo.getInsureco()))
				insbRulequerycarinfo.setInsureco(getStringByObjectkey(carinfo, "plateform.InsureCo"));
			
			insbRulequerycarinfoDao.updateById(insbRulequerycarinfo);
		}
		
		
		
		// 补充数据库中车辆信息
		INSBCarinfo insbcar = new INSBCarinfo();
		insbcar.setTaskid(taskid);
		insbcar = insbCarinfoDao.selectOne(insbcar);
		if (insbcar != null) {
			if (StringUtil.isEmpty(insbcar.getCarlicenseno()))
				insbcar.setCarlicenseno(insbRulequerycarinfo.getLicenseno());
			if (StringUtil.isEmpty(insbcar.getVincode()))
				insbcar.setVincode(insbRulequerycarinfo.getVinno());
			if (StringUtil.isEmpty(insbcar.getEngineno()))
				insbcar.setEngineno(insbRulequerycarinfo.getEngineno());
			if(StringUtil.isEmpty(insbcar.getRegistdate()))
				insbcar.setRegistdate(DateUtil.parseDateTime(insbRulequerycarinfo.getEnrolldate()));
			insbCarinfoDao.updateById(insbcar);
			LogUtil.info("INSBCarinfo|报表数据埋点|"+JSONObject.fromObject(insbcar).toString());

			INSBCarmodelinfo insbcarmodel = new INSBCarmodelinfo();
			insbcarmodel.setCarinfoid(insbcar.getId());
			insbcarmodel = insbCarmodelinfoDao.selectOne(insbcarmodel);
			if (insbcarmodel != null) {
				if (StringUtil.isEmpty(insbcarmodel.getBrandname()))
					insbcarmodel.setBrandname(insbRulequerycarinfo.getCarbrandname());
				if (StringUtil.isEmpty(insbcarmodel.getDisplacement()))
					insbcarmodel.setDisplacement(insbRulequerycarinfo.getDisplacement());
				if (StringUtil.isEmpty(insbcarmodel.getSeat()))
					insbcarmodel.setSeat(insbRulequerycarinfo.getSeatcnt());
				if (StringUtil.isEmpty(insbcarmodel.getUnwrtweight()))
					insbcarmodel.setUnwrtweight(insbRulequerycarinfo.getModelload());
				if (StringUtil.isEmpty(insbcarmodel.getFullweight()))
					insbcarmodel.setFullweight(insbRulequerycarinfo.getFullload());
				if (StringUtil.isEmpty(insbcarmodel.getInsuranceCode()))
					insbcarmodel.setInsuranceCode(insbRulequerycarinfo.getModelcode());

				insbCarmodelinfoDao.updateById(insbcarmodel);
			}
		}
		
		//保存电销类型
		saveJiangSuOtherinfo(taskid, carinfo);
	}
	
	private void saveJiangSuOtherinfo(String taskid,JSONObject carinfo){
		INSBRulequeryotherinfo insbRulequeryotherinfo = new INSBRulequeryotherinfo();
		insbRulequeryotherinfo.setTaskid(taskid);
		INSBRulequeryotherinfo rulequeryotherinfo = insbRulequeryotherinfoDao.selectOne(insbRulequeryotherinfo);
		//是否是否通过电销投保, 0或1
		String pureESale = getStringByObjectkey(carinfo, "pureESale");
		if(StringUtil.isEmpty(pureESale)){
			pureESale = "0";//不是纯电销
		}
		if(null == rulequeryotherinfo){
			rulequeryotherinfo = new INSBRulequeryotherinfo();
			rulequeryotherinfo.setCreatetime(new Date());
			rulequeryotherinfo.setModifytime(new Date());
			rulequeryotherinfo.setOperator("jgguizequery");
			rulequeryotherinfo.setTaskid(taskid);
			rulequeryotherinfo.setPureesale(pureESale);
			//下面所有接口未返回, 设为默认
			rulequeryotherinfo.setNoclaimdiscountcoefficient(getDoubleByObjectkey(carinfo, "noClaimDiscountCoefficient"));
			rulequeryotherinfo.setNoclaimdiscountcoefficientreasons(getStringByObjectkey(carinfo, "noClaimDiscountCoefficientReasons"));
			rulequeryotherinfo.setLoyaltyreasons(getStringByObjectkey(carinfo, "loyaltyReasons"));
			rulequeryotherinfo.setTrafficoffencediscount(getDoubleByObjectkey(carinfo, "trafficOffenceDiscount"));
			rulequeryotherinfo.setCompulsoryclaimrate(getDoubleByObjectkey(carinfo, "compulsoryClaimRate"));
			rulequeryotherinfo.setCompulsoryclaimratereasons(getStringByObjectkey(carinfo, "compulsoryClaimRateReasons"));
			rulequeryotherinfo.setFirstinsuretype(getStringByObjectkey(carinfo, "firstInsureType"));
			rulequeryotherinfo.setBwcommercialclaimtimes(getStringByObjectkey(carinfo, "bwCommercialClaimTimes"));
			rulequeryotherinfo.setBwlastclaimsum(getDoubleByObjectkey(carinfo, "bwLastClaimSum"));
			rulequeryotherinfo.setBwcompulsoryclaimtimes(getStringByObjectkey(carinfo, "bwCompulsoryClaimTimes"));
			rulequeryotherinfo.setBwlastcompulsoryclaimsum(getDoubleByObjectkey(carinfo, "bwLastCompulsoryClaimSum"));
			rulequeryotherinfo.setClaimtimes(getInterByObjectkey(carinfo, "claimTimes"));
			rulequeryotherinfo.setCompulsoryclaimtimes(getInterByObjectkey(carinfo, "compulsoryClaimTimes"));
			rulequeryotherinfo.setLastclaimsum(getDoubleByObjectkey(carinfo, "lastClaimSum"));
			rulequeryotherinfo.setSyendmark(getStringByObjectkey(carinfo, "SYendMark"));
			rulequeryotherinfo.setErrormsgsy(getStringByObjectkey(carinfo, "errorMsgSY"));
			rulequeryotherinfo.setJqendmark(getStringByObjectkey(carinfo, "JQendMark"));
			rulequeryotherinfo.setErrormsgjq(getStringByObjectkey(carinfo, "errorMsgJQ"));
			rulequeryotherinfo.setEfcdiscount(getStringByObjectkey(carinfo, "efcDiscount"));
			rulequeryotherinfo.setVehicletaxoverduefine(getStringByObjectkey(carinfo, "vehicleTaxOverdueFine"));
			rulequeryotherinfo.setRiskclass(getStringByObjectkey(carinfo, "riskClass"));
			rulequeryotherinfo.setBwcommercialclaimrate(getStringByObjectkey(carinfo, "bwCommercialClaimRate"));
			rulequeryotherinfo.setLwarrearstax(getStringByObjectkey(carinfo, "lwArrearsTax"));
			rulequeryotherinfo.setPlatformcarprice(getStringByObjectkey(carinfo, "platformCarPrice"));
			rulequeryotherinfo.setDrunkdrivingrate(getStringByObjectkey(carinfo, "DrunkDrivingRate"));
			insbRulequeryotherinfoDao.insert(rulequeryotherinfo);
		}else{
			rulequeryotherinfo.setModifytime(new Date());
			if (StringUtil.isEmpty(rulequeryotherinfo.getPureesale()))
				rulequeryotherinfo.setPureesale(pureESale);
			
			//下面所有接口未返回, 设为默认
			if (rulequeryotherinfo.getNoclaimdiscountcoefficient() == 0.0)
				rulequeryotherinfo.setNoclaimdiscountcoefficient(getDoubleByObjectkey(carinfo, "noClaimDiscountCoefficient"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getNoclaimdiscountcoefficientreasons()))
				rulequeryotherinfo.setNoclaimdiscountcoefficientreasons(getStringByObjectkey(carinfo, "noClaimDiscountCoefficientReasons"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getLoyaltyreasons()))
				rulequeryotherinfo.setLoyaltyreasons(getStringByObjectkey(carinfo, "loyaltyReasons"));
			if (rulequeryotherinfo.getTrafficoffencediscount() == 0.0)
				rulequeryotherinfo.setTrafficoffencediscount(getDoubleByObjectkey(carinfo, "trafficOffenceDiscount"));
			if (rulequeryotherinfo.getCompulsoryclaimrate() == 0.0)
				rulequeryotherinfo.setCompulsoryclaimrate(getDoubleByObjectkey(carinfo, "compulsoryClaimRate"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getCompulsoryclaimratereasons()))
				rulequeryotherinfo.setCompulsoryclaimratereasons(getStringByObjectkey(carinfo, "compulsoryClaimRateReasons"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getFirstinsuretype()))
				rulequeryotherinfo.setFirstinsuretype(getStringByObjectkey(carinfo, "firstInsureType"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getBwcommercialclaimtimes()))
				rulequeryotherinfo.setBwcommercialclaimtimes(getStringByObjectkey(carinfo, "bwCommercialClaimTimes"));
			if (rulequeryotherinfo.getBwlastclaimsum() == 0.0)
				rulequeryotherinfo.setBwlastclaimsum(getDoubleByObjectkey(carinfo, "bwLastClaimSum"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getBwcompulsoryclaimtimes()))
				rulequeryotherinfo.setBwcompulsoryclaimtimes(getStringByObjectkey(carinfo, "bwCompulsoryClaimTimes"));
			if (rulequeryotherinfo.getBwlastcompulsoryclaimsum() == 0.0)
				rulequeryotherinfo.setBwlastcompulsoryclaimsum(getDoubleByObjectkey(carinfo, "bwLastCompulsoryClaimSum"));
			if (rulequeryotherinfo.getClaimtimes() == 0)
				rulequeryotherinfo.setClaimtimes(getInterByObjectkey(carinfo, "claimTimes"));
			if (rulequeryotherinfo.getCompulsoryclaimtimes() == 0)
				rulequeryotherinfo.setCompulsoryclaimtimes(getInterByObjectkey(carinfo, "compulsoryClaimTimes"));
			if (rulequeryotherinfo.getLastclaimsum() == 0.0)
				rulequeryotherinfo.setLastclaimsum(getDoubleByObjectkey(carinfo, "lastClaimSum"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getSyendmark()))
				rulequeryotherinfo.setSyendmark(getStringByObjectkey(carinfo, "SYendMark"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getErrormsgsy()))
				rulequeryotherinfo.setErrormsgsy(getStringByObjectkey(carinfo, "errorMsgSY"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getJqendmark()))
				rulequeryotherinfo.setJqendmark(getStringByObjectkey(carinfo, "JQendMark"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getErrormsgjq()))
				rulequeryotherinfo.setErrormsgjq(getStringByObjectkey(carinfo, "errorMsgJQ"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getEfcdiscount()))
				rulequeryotherinfo.setEfcdiscount(getStringByObjectkey(carinfo, "efcDiscount"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getVehicletaxoverduefine()))
				rulequeryotherinfo.setVehicletaxoverduefine(getStringByObjectkey(carinfo, "vehicleTaxOverdueFine"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getRiskclass()))
				rulequeryotherinfo.setRiskclass(getStringByObjectkey(carinfo, "riskClass"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getBwcommercialclaimrate()))
				rulequeryotherinfo.setBwcommercialclaimrate(getStringByObjectkey(carinfo, "bwCommercialClaimRate"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getLwarrearstax()))
				rulequeryotherinfo.setLwarrearstax(getStringByObjectkey(carinfo, "lwArrearsTax"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getPlatformcarprice()))
				rulequeryotherinfo.setPlatformcarprice(getStringByObjectkey(carinfo, "platformCarPrice"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getDrunkdrivingrate()))
				rulequeryotherinfo.setDrunkdrivingrate(getStringByObjectkey(carinfo, "DrunkDrivingRate"));
			insbRulequeryotherinfoDao.updateById(rulequeryotherinfo);
		}
	}
	
	/**
	 * 平台信息回调格式化json，去除value是空字符串的
	 * @param json
	 */
	public void formatJSON(JSONObject json){
		Iterator<Map<String, Object>> ite = json.entrySet().iterator();
		List<String> removeKeys = new ArrayList<String>();
		while(ite.hasNext()){
			Map.Entry<String, Object> temp = (Entry<String, Object>) ite.next(); 
			if(StringUtil.isEmpty(temp.getValue())){
				removeKeys.add(temp.getKey());
			}
		}
		
		for(String key : removeKeys){
			json.remove(key);
		}
	}
	
	@Override
	public  String saveRuleQueryInfo(String json) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		this.formatJSON(jsonObject);
		String taskid = "";
		String result = "success";
		try {
			if(jsonObject.containsKey("taskId")){
				taskid = jsonObject.getString("taskId");
			}
			LogUtil.info("taskid=" + taskid + "收到平台信息回调,json=" + json);

			if ("12".equals(jsonObject.optString("errorcode"))){ // 如果是重复投保,关闭所有任务
				String errordesc = jsonObject.optString("errordesc");
				if (StringUtil.isEmpty(errordesc)) errordesc = "重复投保";
				LogUtil.info("taskid=" + taskid + ",errorcode=" + jsonObject.get("errorcode") + ",errordesc=" + errordesc);

				List<Map<String, Object>> subList = insbWorkflowsubDao.selectSubModelInfoByMainInstanceId(taskid);
                boolean closeAll = false;

				for(Map<String, Object> subModelInfo : subList){
					saveFlowerrorToManWork(taskid, String.valueOf(subModelInfo.get("inscomcode")), errordesc, "12", "报价失败");

                    WorkflowFeedbackUtil.setWorkflowFeedback(taskid, String.valueOf(subModelInfo.get("instanceid")), String.valueOf(subModelInfo.get("taskcode")),
                            "Completed", null, WorkflowFeedbackUtil.writeback_duplication, "admin");
                    WorkflowFeedbackUtil.setWorkflowFeedback(taskid, String.valueOf(subModelInfo.get("instanceid")), "37", "Closed", null,
                            WorkflowFeedbackUtil.writeback_duplication+"#平台查询重复投保关闭", "admin");
                    closeAll = true;
					
					try {
						boolean f = sched.deleteJob(new JobKey(String.valueOf(subModelInfo.get("instanceid")) + "_平台查询_"+taskid));
						if(!f)
							f = sched.deleteJob(new JobKey(String.valueOf(subModelInfo.get("instanceid")) + "_备用平台查询_"+taskid));
					} catch (SchedulerException e) {
						LogUtil.info("taskid=" + taskid + ",subtaskid=" + subModelInfo.get("instanceid") + ",删除调度异常1");
						e.printStackTrace();
					}
				}

				if (closeAll) {
                    final String maininstanceId = taskid;
    		        taskthreadPool4workflow.execute(new Runnable() {
    					@Override
    					public void run() {
							WorkflowFeedbackUtil.setWorkflowFeedback(maininstanceId, null, "37", "Closed", null, WorkflowFeedbackUtil.writeback_duplication+"#平台查询重复投保关闭", "admin");
							insbManualPriceService.refuseUnderwrite2(maininstanceId, "", "", "main", "back");

							//通知调度删除任务
    						Task task = new Task();
    						//主流程任务
    						task.setProInstanceId(maininstanceId);
    						
    						LogUtil.info("maininstanceId=" + maininstanceId + ",在saveRuleQueryInfo中通知调度删除任务");
    						dispatchTaskService.deleteTask(task);

    					}
    		        });
                }

				return "fail";
			}

			if(jsonObject.containsKey("plateform.InsureCo")){
				LogUtil.info("0规则平台查询回调taskid="+ taskid + "=plateform.InsureCo="+ jsonObject.getString("plateform.InsureCo"));
			}

			//保存数据
			if(!jsonObject.containsKey("succeed") || !"success".equals(jsonObject.getString("succeed"))) {
				// 居然平台信息是失败的,那就直接返回
				LogUtil.error("9规则平台查询回调taskid=" + taskid + "=失败了=");
				return result;
			}

			INSBWorkflowsub insbwfsub = new INSBWorkflowsub();
			insbwfsub.setMaininstanceid(taskid);
			List<INSBWorkflowsub> allSubList = insbWorkflowsubDao.selectList(insbwfsub);
			StringBuffer sbf = new StringBuffer();
			if(allSubList != null){
				String instanceId = null;
				INSBQuoteinfo quoteInfo = null;
				String agreementId = null;
				INSBAgreement insbAgreement = null;

				for(INSBWorkflowsub temp : allSubList){
					instanceId = temp.getInstanceid();
					boolean isguizequote = false;
					quoteInfo = insbQuoteinfoDao.queryQuoteinfoByWorkflowinstanceid(instanceId);
					if(quoteInfo != null){
						agreementId = quoteInfo.getAgreementid();
						if(!StringUtil.isEmpty(agreementId)){
							insbAgreement = insbAgreementDao.selectById(agreementId);
							//规则报价是否启动
							if(insbAgreement != null && insbAgreement.getAgreementrule() != null
									&& insbAgreement.getAgreementrule().equals("1")){
								isguizequote = true;
							}
						}
					}
					sbf.append(instanceId).append(":").append(isguizequote).append(",");
				}
			}

			String allwfsubAgreementRule = sbf.toString();
			LogUtil.info("saveRuleQueryInfo-检查供应商规则报价, taskId=" + taskid + ",子流程规则报价启用状态{" + allwfsubAgreementRule + "}(包含true走规则报价平台信息)");

			//判断险项投保方式
			int insureType = INSBCommonQuoteinfoService.getInsureConfigType(taskid,"");
			LogUtil.info("0规则平台查询回调taskid="+ taskid + "，判断险项投保方式："+insureType);
			JSONObject carinfo = JSONObject.fromObject(jsonObject.get("task"));
			this.formatJSON(carinfo);

			//1:单商,2:单交,3:混保, 将平台信息拆分成"基础平台信息"和"规则报价平台信息"
			if(1==insureType){
				if(allwfsubAgreementRule.contains("true")){
					//有"规则报价能力" 走 "规则报价平台信息"
					if(!jsonObject.containsKey("firstInsureType")||!jsonObject.containsKey("bwCommercialClaimTimes")||!jsonObject.containsKey("taskId")
							||!jsonObject.containsKey("noClaimDiscountCoefficient")||!jsonObject.containsKey("claimTimes")
							||!carinfo.containsKey("car.specific.selfInsureRate")||!carinfo.containsKey("car.specific.selfChannelRate")
							||!carinfo.containsKey("vehicleInfo.tradeModelCode")){
						result = "taskid="+ taskid + ",单商(规则报价平台信息)保存信息失败,数据不完整.以下信息中至少缺少一项：" +
								"firstInsureType("+jsonObject.containsKey("firstInsureType")+")," +
								"bwCommercialClaimTimes("+jsonObject.containsKey("bwCommercialClaimTimes")+")," +
								"taskId("+jsonObject.containsKey("taskId")+")," +
								"noClaimDiscountCoefficient("+jsonObject.containsKey("noClaimDiscountCoefficient")+")," +
								"claimTimes("+jsonObject.containsKey("claimTimes")+")," +
								"car.specific.selfInsureRate("+carinfo.containsKey("car.specific.selfInsureRate")+")," +
								"car.specific.selfChannelRate("+carinfo.containsKey("car.specific.selfChannelRate")+"),"+
								"vehicleInfo.tradeModelCode("+carinfo.containsKey("vehicleInfo.tradeModelCode")+")";
						LogUtil.info(result);
						return result;
					}
					//



				} else {
					if(!jsonObject.containsKey("firstInsureType")||!jsonObject.containsKey("bwCommercialClaimTimes")||!jsonObject.containsKey("taskId")
							||!jsonObject.containsKey("noClaimDiscountCoefficient")||!jsonObject.containsKey("claimTimes")){
						result = "taskid="+ taskid + ",单商(基础平台信息)保存信息失败,数据不完整.以下信息中至少缺少一项：" +
								"firstInsureType("+jsonObject.containsKey("firstInsureType")+")," +
								"bwCommercialClaimTimes("+jsonObject.containsKey("bwCommercialClaimTimes")+")," +
								"taskId("+jsonObject.containsKey("taskId")+")," +
								"noClaimDiscountCoefficient("+jsonObject.containsKey("noClaimDiscountCoefficient")+")," +
								"claimTimes("+jsonObject.containsKey("claimTimes")+")";
						LogUtil.info(result);
						return result;
					}
				}
			}else if(2==insureType || insureType == 4){
				if(!jsonObject.containsKey("bwCompulsoryClaimTimes")||!jsonObject.containsKey("compulsoryClaimTimes")||!jsonObject.containsKey("taskId")){
					result = "taskid="+ taskid + ",单交保存信息失败,数据不完整.以下信息中至少缺少一项："
							+"bwCompulsoryClaimTimes("+jsonObject.containsKey("bwCompulsoryClaimTimes")+")," +
							"compulsoryClaimTimes("+jsonObject.containsKey("compulsoryClaimTimes")+")," +
							"taskId("+jsonObject.containsKey("taskId")+")";
					LogUtil.info(result);
					return result;
				}
			}else if(3==insureType){
				if(allwfsubAgreementRule.contains("true")){
					//有"规则报价能力" 走 "规则报价平台信息"
					if(!jsonObject.containsKey("firstInsureType")||!jsonObject.containsKey("bwCommercialClaimTimes")
							||!jsonObject.containsKey("noClaimDiscountCoefficient")||!jsonObject.containsKey("compulsoryClaimRate")
							||!jsonObject.containsKey("claimTimes")||!jsonObject.containsKey("compulsoryClaimTimes")
							||!jsonObject.containsKey("taskId")||!carinfo.containsKey("car.specific.selfInsureRate")
							||!carinfo.containsKey("car.specific.selfChannelRate")
							||!carinfo.containsKey("vehicleInfo.tradeModelCode")||!jsonObject.containsKey("bwCompulsoryClaimTimes")){
						result = "taskid="+ taskid + ",混保(规则报价平台信息)保存信息失败,数据不完整.以下信息中至少缺少一项：" +
								"firstInsureType("+jsonObject.containsKey("firstInsureType")+")," +
								"bwCommercialClaimTimes("+jsonObject.containsKey("bwCommercialClaimTimes")+"),"+
								"noClaimDiscountCoefficient("+jsonObject.containsKey("noClaimDiscountCoefficient")+")," +
								"compulsoryClaimRate("+jsonObject.containsKey("compulsoryClaimRate")+")," +
								"claimTimes("+jsonObject.containsKey("claimTimes")+")," +
								"compulsoryClaimTimes("+jsonObject.containsKey("compulsoryClaimTimes")+")," +
								"taskId("+jsonObject.containsKey("taskId")+"),"+
								"car.specific.selfInsureRate("+carinfo.containsKey("car.specific.selfInsureRate")+")," +
								"car.specific.selfChannelRate("+carinfo.containsKey("car.specific.selfChannelRate")+"),"+
								"vehicleInfo.tradeModelCode("+carinfo.containsKey("vehicleInfo.tradeModelCode")+")," +
								"bwCompulsoryClaimTimes("+jsonObject.containsKey("bwCompulsoryClaimTimes")+")";
						LogUtil.info(result);
						return result;
					}
				} else {
					if(!jsonObject.containsKey("firstInsureType")||!jsonObject.containsKey("bwCommercialClaimTimes")
							||!jsonObject.containsKey("noClaimDiscountCoefficient")||!jsonObject.containsKey("compulsoryClaimRate")
							||!jsonObject.containsKey("claimTimes")||!jsonObject.containsKey("compulsoryClaimTimes")
							||!jsonObject.containsKey("taskId") ||!jsonObject.containsKey("bwCompulsoryClaimTimes")){
						result = "taskid="+ taskid + ",混保(基础平台信息)保存信息失败,数据不完整.以下信息中至少缺少一项：" +
								"firstInsureType("+jsonObject.containsKey("firstInsureType")+")," +
								"bwCommercialClaimTimes("+jsonObject.containsKey("bwCommercialClaimTimes")+"),"+
								"noClaimDiscountCoefficient("+jsonObject.containsKey("noClaimDiscountCoefficient")+")," +
								"compulsoryClaimRate("+jsonObject.containsKey("compulsoryClaimRate")+")," +
								"claimTimes("+jsonObject.containsKey("claimTimes")+")," +
								"compulsoryClaimTimes("+jsonObject.containsKey("compulsoryClaimTimes")+")," +
								"taskId("+jsonObject.containsKey("taskId")+"),"+
								"bwCompulsoryClaimTimes("+jsonObject.containsKey("bwCompulsoryClaimTimes")+")";
						LogUtil.info(result);
						return result;
					}
				}
			} else if (insureType == 5  || insureType == -1){
				LogUtil.info("taskid="+ taskid + ", 险项投保方式为5或者-1直接return! insureType=" + insureType);
				result = "fail";
				return result;
			} else {
				LogUtil.info("taskid="+ taskid + ", 未知的险项投保方式 insureType=" + insureType);
				result = "fail";
				return result;
			}

			LogUtil.info("0规则平台查询回调taskid="+ taskid + "=数据校验通过=");

			final String finalTaskid = taskid;

			// 平台信息完整,保存
			LogUtil.info("taskid=" + finalTaskid + ",保存回写数据,开始时间：" + DateUtil.getCurrentDate("hh:mm:ss"));
			/* 注释掉if，因为如果数据回写有新的字段 无法保存
				INSBRulequerycarinfo insbRulequerycarinfo = new INSBRulequerycarinfo();
				insbRulequerycarinfo.setTaskid(finalTaskid);
				List<INSBRulequerycarinfo> list = insbRulequerycarinfoDao.selectList(insbRulequerycarinfo);
				if (list.isEmpty() || !"guizequery".equals(list.get(0).getOperator())) {
				*/
			if (jsonObject.containsKey("task")) {
				saveRuleCarinfo(jsonObject, finalTaskid);
			}
			if (jsonObject.containsKey("bizPolicies") || jsonObject.containsKey("efcPolicies")) {
				saveAllPolicies(finalTaskid, jsonObject.get("bizPolicies"), jsonObject.get("efcPolicies"));
				updatePolicyitemsDate(finalTaskid, jsonObject.get("bizPolicies"), jsonObject.get("efcPolicies"));
			}
			if (jsonObject.containsKey("bizClaims") || jsonObject.containsKey("efcClaims")) {
				saveAllClaims(finalTaskid, jsonObject.get("bizClaims"), jsonObject.get("efcClaims"));
			}
			saveRulequeryotherinfo(finalTaskid, jsonObject);
			//}

			LogUtil.info("taskid=" + finalTaskid + ",保存回写数据,结束时间：" + DateUtil.getCurrentDate("hh:mm:ss"));

			final String finalallwfsubAgreementRule = allwfsubAgreementRule;

			//先关闭定时任务,然后再推平台查询结束工作流(311版本独立出来)
			taskthreadPool4workflow.execute(new Runnable() {
				@Override
				public void run() {
					//初始化保存一下查询得来的已有补充项的值
					//311版本，有规则报价能力才保存
					if(finalallwfsubAgreementRule != null && finalallwfsubAgreementRule.contains("true")){
						LogUtil.info("taskid=" + finalTaskid + ",保存规则报价平台信息补充项,开始时间：" + DateUtil.getCurrentDate("hh:mm:ss"));
						// (这里有时执行很慢，但必须要在下面推工作流之前执行，否则以上数据因为事务原因没有保存进数据库，导致收到工作流通知之后的操作不能及时获取到数据)
						supplementCache.saveTaskidSupplementDatas(finalTaskid);
						LogUtil.info("taskid=" + finalTaskid + ",保存规则报价平台信息补充项,结束时间：" + DateUtil.getCurrentDate("hh:mm:ss"));
					}

					// 如果并发会怎么样？
					INSBWorkflowsub param = new INSBWorkflowsub();
					param.setMaininstanceid(finalTaskid);
					param.setTaskcode(TaskConst.QUOTEWAIT_53);
					List<INSBWorkflowsub> subs = insbWorkflowsubDao.selectList(param);
					for (INSBWorkflowsub sub : subs) {
						// 先删除定时任务,然后调用工作流推结束平台查询
						try {
							boolean f = sched.deleteJob(new JobKey(sub.getInstanceid() + "_平台查询_"+sub.getMaininstanceid()));
							if(!f)
								f = sched.deleteJob(new JobKey(sub.getInstanceid() + "_备用平台查询_"+sub.getMaininstanceid()));
							if(!f)
								LogUtil.info("taskid=" + finalTaskid + ",subtaskid=" + sub.getInstanceid() + ",没找到定时任务");
						} catch (SchedulerException e) {
							LogUtil.info("taskid=" + finalTaskid + ",subtaskid=" + sub.getInstanceid() + ",删除调度异常2");
							e.printStackTrace();
						}
						if(!"Completed".equals(sub.getTaskstate())){//如果还没有结束则，推送该平台查询节点完成
							WorkflowFeedbackUtil.setWorkflowFeedback(null, sub.getInstanceid(), "53", "Completed", "平台查询", "完成", "admin");
							INSBQuoteinfo quoteInfo = insbQuoteinfoDao.queryQuoteinfoByWorkflowinstanceid(sub.getInstanceid());
							Map<String, Object> datamap = new HashMap<String, Object>(10);
							datamap.put("gzway", JSONObject.fromObject(workflowmainService.getGZway(sub.getMaininstanceid(), quoteInfo.getInscomcode())).getString("gzway"));
							String callback = WorkFlowUtil.updateInsuredInfoNoticeWorkflow(datamap,sub.getInstanceid(), "admin", "平台查询", "1");
							LogUtil.info("taskid=%s,subtaskid=%s,推送结束平台查询返回结果=%s", finalTaskid, sub.getInstanceid(), callback);
						}
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
			result = "fail";
		}
		return result;
	}
	
	/**
	 * 保存规则返回的车辆信息
	 * @param jsonObject
	 * @param taskid
	 */
	public void saveRuleCarinfo(JSONObject jsonObject,String taskid){
		JSONObject carinfo = JSONObject.fromObject(jsonObject.get("task"));
		INSBRulequerycarinfo insbRulequerycarinfo = new INSBRulequerycarinfo();
		insbRulequerycarinfo.setTaskid(taskid);
		insbRulequerycarinfo = insbRulequerycarinfoDao.selectOne(insbRulequerycarinfo);
		if(null == insbRulequerycarinfo){
			insbRulequerycarinfo = new INSBRulequerycarinfo();
			insbRulequerycarinfo.setCreatetime(new Date());
			insbRulequerycarinfo.setModifytime(new Date());
			insbRulequerycarinfo.setOperator("guizequery");
			insbRulequerycarinfo.setTaskid(taskid);
			insbRulequerycarinfo.setCarbrandname(getStringByObjectkey(carinfo, "carBrandName"));
			insbRulequerycarinfo.setPrice(getDoubleByObjectkey(carinfo, "price"));
			insbRulequerycarinfo.setTaxprice(getDoubleByObjectkey(carinfo, "taxPrice"));
			insbRulequerycarinfo.setAnalogyprice(getDoubleByObjectkey(carinfo, "analogyPrice"));
			insbRulequerycarinfo.setAnalogytaxprice(getDoubleByObjectkey(carinfo, "analogyTaxPrice"));
			insbRulequerycarinfo.setCarmodeldate(getStringByObjectkey(carinfo, "carModelDate"));
			insbRulequerycarinfo.setSeatcnt(getInterByObjectkey(carinfo, "seatCnt"));
			insbRulequerycarinfo.setModelload(getDoubleByObjectkey(carinfo, "modelLoad"));
			insbRulequerycarinfo.setFullload(getDoubleByObjectkey(carinfo, "fullLoad"));
			insbRulequerycarinfo.setDisplacement(getDoubleByObjectkey(carinfo, "displacement"));
			insbRulequerycarinfo.setTrademodelcode(getStringByObjectkey(carinfo, "vehicleInfo.tradeModelCode"));
			insbRulequerycarinfo.setSelfinsurerate(getDoubleByObjectkey(carinfo, "car.specific.selfInsureRate"));
			insbRulequerycarinfo.setSelfchannelrate(getDoubleByObjectkey(carinfo, "car.specific.selfChannelRate"));
			insbRulequerycarinfo.setNcdrate(getDoubleByObjectkey(carinfo, "car.specific.NcdRate"));
			insbRulequerycarinfo.setVehicletype(getStringByObjectkey(carinfo, "vehicleInfo.vehicleType"));
			insbRulequerycarinfo.setBasicriskpremium(getDoubleByObjectkey(carinfo, "quoteItems_ruleItem.basicRiskPremium"));
			insbRulequerycarinfo.setInsureco(getStringByObjectkey(carinfo, "plateform.InsureCo"));
			insbRulequerycarinfo.setLicenseno(getStringByObjectkey(jsonObject, "licenseNo"));
			insbRulequerycarinfo.setEngineno(getStringByObjectkey(jsonObject, "engineNo"));
			insbRulequerycarinfo.setVinno(getStringByObjectkey(jsonObject, "vinNo"));
			insbRulequerycarinfo.setEnrolldate(getStringByObjectkey(jsonObject, "enrollDate"));
			insbRulequerycarinfo.setModelcode(getStringByObjectkey(carinfo, "modelCode"));
			insbRulequerycarinfoDao.insert(insbRulequerycarinfo);
		}else{
			insbRulequerycarinfo.setOperator("guizequery");
			insbRulequerycarinfo.setModifytime(new Date());
			if (StringUtil.isEmpty(insbRulequerycarinfo.getCarbrandname()))
				insbRulequerycarinfo.setCarbrandname(getStringByObjectkey(carinfo, "carBrandName"));
			if(insbRulequerycarinfo.getPrice() == 0.0)
				insbRulequerycarinfo.setPrice(getDoubleByObjectkey(carinfo, "price"));
			if (insbRulequerycarinfo.getTaxprice() == 0.0)
				insbRulequerycarinfo.setTaxprice(getDoubleByObjectkey(carinfo, "taxPrice"));
			if (insbRulequerycarinfo.getAnalogyprice() == 0.0)
				insbRulequerycarinfo.setAnalogyprice(getDoubleByObjectkey(carinfo, "analogyPrice"));
			if (insbRulequerycarinfo.getAnalogytaxprice() == 0.0)
				insbRulequerycarinfo.setAnalogytaxprice(getDoubleByObjectkey(carinfo, "analogyTaxPrice"));
			if (StringUtil.isEmpty(insbRulequerycarinfo.getCarmodeldate()))
				insbRulequerycarinfo.setCarmodeldate(getStringByObjectkey(carinfo, "carModelDate"));
			if (insbRulequerycarinfo.getSeatcnt() == 0)
				insbRulequerycarinfo.setSeatcnt(getInterByObjectkey(carinfo, "seatCnt"));
			if (insbRulequerycarinfo.getModelload() == 0.0)
				insbRulequerycarinfo.setModelload(getDoubleByObjectkey(carinfo, "modelLoad"));
			if (insbRulequerycarinfo.getFullload() == 0.0)
				insbRulequerycarinfo.setFullload(getDoubleByObjectkey(carinfo, "fullLoad"));
			if (insbRulequerycarinfo.getDisplacement() == 0.0)
				insbRulequerycarinfo.setDisplacement(getDoubleByObjectkey(carinfo, "displacement"));
			if (StringUtil.isEmpty(insbRulequerycarinfo.getTrademodelcode()))
				insbRulequerycarinfo.setTrademodelcode(getStringByObjectkey(carinfo, "vehicleInfo.tradeModelCode"));
			if (insbRulequerycarinfo.getSelfinsurerate() == 0.0)
				insbRulequerycarinfo.setSelfinsurerate(getDoubleByObjectkey(carinfo, "car.specific.selfInsureRate"));
			if (insbRulequerycarinfo.getSelfchannelrate() == 0.0)
				insbRulequerycarinfo.setSelfchannelrate(getDoubleByObjectkey(carinfo, "car.specific.selfChannelRate"));
			if (insbRulequerycarinfo.getNcdrate() == 0.0)
				insbRulequerycarinfo.setNcdrate(getDoubleByObjectkey(carinfo, "car.specific.NcdRate"));
			if (StringUtil.isEmpty(insbRulequerycarinfo.getVehicletype()))
				insbRulequerycarinfo.setVehicletype(getStringByObjectkey(carinfo, "vehicleInfo.vehicleType"));
			if (insbRulequerycarinfo.getBasicriskpremium() == 0.0)
				insbRulequerycarinfo.setBasicriskpremium(getDoubleByObjectkey(carinfo, "quoteItems_ruleItem.basicRiskPremium"));
			if (StringUtil.isEmpty(insbRulequerycarinfo.getInsureco()))
				insbRulequerycarinfo.setInsureco(getStringByObjectkey(carinfo, "plateform.InsureCo"));
			if (StringUtil.isEmpty(insbRulequerycarinfo.getLicenseno()))
				insbRulequerycarinfo.setLicenseno(getStringByObjectkey(jsonObject, "licenseNo"));
			if (StringUtil.isEmpty(insbRulequerycarinfo.getEngineno()))
				insbRulequerycarinfo.setEngineno(getStringByObjectkey(jsonObject, "engineNo"));
			if (StringUtil.isEmpty(insbRulequerycarinfo.getVinno()))
				insbRulequerycarinfo.setVinno(getStringByObjectkey(jsonObject, "vinNo"));
			if (StringUtil.isEmpty(insbRulequerycarinfo.getEnrolldate()))
				insbRulequerycarinfo.setEnrolldate(getStringByObjectkey(jsonObject, "enrollDate"));
			if (StringUtil.isEmpty(insbRulequerycarinfo.getModelcode()))
				insbRulequerycarinfo.setModelcode(getStringByObjectkey(carinfo, "modelCode"));
			insbRulequerycarinfoDao.updateById(insbRulequerycarinfo);
		}
	}

	//更新保险期间
	private void updatePolicyitemsDate(String taskid, Object sypolicies, Object jqpolicies) {
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskid);
		List<INSBPolicyitem> insbPolicyitems = policyitemDao.selectList(insbPolicyitem);
		if(null == insbPolicyitems || insbPolicyitems.isEmpty()) return;

		List<INSBRulequeryrepeatinsured> policieslist = new ArrayList<INSBRulequeryrepeatinsured>();
		Date syEffectDate = null, syExpireDate = null,
				jqEffectDate = null, jqExpireDate = null;

		if (sypolicies != null) {
			JSONArray syarray = JSONArray.fromObject(sypolicies);
			for (int i = 0; i < syarray.size(); i++) {
				JSONObject syobject = JSONObject.fromObject(syarray.get(i));
				policieslist.add(getINSBRulequeryrepeatinsured(syobject, taskid, "0"));
			}

			if (!policieslist.isEmpty()) {
				insbRulequeryrepeatinsuredService.sortPolicies(policieslist);

				INSBRulequeryrepeatinsured last = policieslist.get(0);
				Map<String, Date> dates = ModelUtil.calNextPolicyDate(last.getPolicystarttime(), last.getPolicyendtime());
				syEffectDate = dates.get("startDate");
				syExpireDate = dates.get("endDate");
			}
		}

		policieslist.clear();

		if (jqpolicies != null) {
			JSONArray jqarray = JSONArray.fromObject(jqpolicies);
			for (int i = 0; i < jqarray.size(); i++) {
				JSONObject jqobject = JSONObject.fromObject(jqarray.get(i));
				policieslist.add(getINSBRulequeryrepeatinsured(jqobject, taskid, "1"));
			}

			if (!policieslist.isEmpty()) {
				insbRulequeryrepeatinsuredService.sortPolicies(policieslist);

				INSBRulequeryrepeatinsured last = policieslist.get(0);
				Map<String, Date> dates = ModelUtil.calNextPolicyDate(last.getPolicystarttime(), last.getPolicyendtime());
				jqEffectDate = dates.get("startDate");
				jqExpireDate = dates.get("endDate");
			}
		}

		if (syEffectDate != null || jqEffectDate != null) {
			for (INSBPolicyitem policyitem : insbPolicyitems) {
				if ("0".equals(policyitem.getRisktype()) && syEffectDate != null) {
					policyitem.setStartdate(syEffectDate);
					policyitem.setEnddate(syExpireDate);
					policyitemDao.updateById(policyitem);
				} else if ("1".equals(policyitem.getRisktype()) && jqEffectDate != null){
					policyitem.setStartdate(jqEffectDate);
					policyitem.setEnddate(jqExpireDate);
					policyitemDao.updateById(policyitem);
				}
			}
		}
	}

	/**
	 * 保存重复投保记录
	 * @param taskid
	 * @param sypolicies
	 * @param jqpolicies
	 */
	public void saveAllPolicies(String taskid,Object sypolicies,Object jqpolicies){
		//先删除已存在的
		INSBRulequeryrepeatinsured insbRulequeryrepeatinsured = new INSBRulequeryrepeatinsured();
		insbRulequeryrepeatinsured.setTaskid(taskid);
		List<INSBRulequeryrepeatinsured> insbRulequeryrepeatinsureds = insbRulequeryrepeatinsuredDao.selectList(insbRulequeryrepeatinsured);
		// 只保留第一次存储的数据,后续不再插入或更新
		if(insbRulequeryrepeatinsureds != null && insbRulequeryrepeatinsureds.size() > 0) return;

		List<INSBRulequeryrepeatinsured> policieslist = new ArrayList<INSBRulequeryrepeatinsured>();
		JSONArray syarray = JSONArray.fromObject(sypolicies);
		for(int i = 0;i < syarray.size();i ++){
			JSONObject syobject = JSONObject.fromObject(syarray.get(i));
			policieslist.add(getINSBRulequeryrepeatinsured(syobject, taskid, "0"));
		}
		JSONArray jqarray = JSONArray.fromObject(jqpolicies);
		for(int i = 0;i < jqarray.size();i ++){
			JSONObject jqobject = JSONObject.fromObject(jqarray.get(i));
			policieslist.add(getINSBRulequeryrepeatinsured(jqobject, taskid, "1"));
		}
		insbRulequeryrepeatinsuredDao.insertInBatch(policieslist);
	}
	
	private INSBRulequeryrepeatinsured getINSBRulequeryrepeatinsured(JSONObject object,String taskid,String type){
		INSBRulequeryrepeatinsured insbRulequeryrepeatinsured = new INSBRulequeryrepeatinsured();
		insbRulequeryrepeatinsured.setCreatetime(new Date());
		insbRulequeryrepeatinsured.setModifytime(new Date());
		insbRulequeryrepeatinsured.setOperator("guizequery");
		insbRulequeryrepeatinsured.setTaskid(taskid);
		insbRulequeryrepeatinsured.setRisktype(type);
		insbRulequeryrepeatinsured.setInscorpid(getStringByObjectkey(object, "insCorpId"));

		String code = InsCompany.getCode(getStringByObjectkey(object, "insCorpCode")),
				name = InsCompany.getCode(getStringByObjectkey(object, "insCorpName"));

		if(StringUtils.isNotBlank(code)){
			insbRulequeryrepeatinsured.setInscorpcode(code);
		}else if(StringUtils.isNotBlank(name)){
			insbRulequeryrepeatinsured.setInscorpcode(name);
		} else if (StringUtils.isNotBlank(insbRulequeryrepeatinsured.getInscorpid())) {
			insbRulequeryrepeatinsured.setInscorpcode(insbRulequeryrepeatinsured.getInscorpid());
		}
		
		if(StringUtils.isNotBlank(insbRulequeryrepeatinsured.getInscorpcode())){
			INSBProvider insbPorvider = insbProviderDao.selectById(insbRulequeryrepeatinsured.getInscorpcode());
			if(insbPorvider != null)
				insbRulequeryrepeatinsured.setInscorpname(insbPorvider.getPrvname());
		}else
			if(StringUtils.isNotBlank(getStringByObjectkey(object, "insCorpName"))){
				insbRulequeryrepeatinsured.setInscorpname(getStringByObjectkey(object, "insCorpName"));
		}
		

		insbRulequeryrepeatinsured.setPolicystarttime(getStringByObjectkey(object, "policyStartTime"));
		insbRulequeryrepeatinsured.setPolicyendtime(getStringByObjectkey(object, "policyEndTime"));
		insbRulequeryrepeatinsured.setPolicyid(getStringByObjectkey(object, "policyId"));
		return insbRulequeryrepeatinsured;
	}
	/**
	 * 保存理赔信息
	 * @param taskid
	 * @param syclaims
	 * @param jqclaims
	 */
	private void saveAllClaims(String taskid,Object syclaims,Object jqclaims){
		INSBRulequeryclaims insbRulequeryclaims = new INSBRulequeryclaims();
		insbRulequeryclaims.setTaskid(taskid);
		List<INSBRulequeryclaims> insbRulequeryclaims2 = insbRulequeryclaimsDao.selectList(insbRulequeryclaims);
		if(insbRulequeryclaims2 != null && insbRulequeryclaims2.size() > 0) return;
		List<INSBRulequeryclaims> rulequeryclaims = new ArrayList<INSBRulequeryclaims>();
		if(syclaims != null){
			JSONArray syarray = JSONArray.fromObject(syclaims);
			for(int i = 0;i < syarray.size();i ++){
				JSONObject syobject = JSONObject.fromObject(syarray.get(i));
				rulequeryclaims.add(getINSBRulequeryclaims(syobject, taskid, "0"));
			}
		}
		if(jqclaims != null){
			JSONArray jqarray = JSONArray.fromObject(jqclaims);
			for(int i = 0;i < jqarray.size();i ++){
				JSONObject jqobject = JSONObject.fromObject(jqarray.get(i));
				rulequeryclaims.add(getINSBRulequeryclaims(jqobject, taskid, "1"));
			}
		}
		insbRulequeryclaimsDao.insertInBatch(rulequeryclaims);
	}
	
	private INSBRulequeryclaims getINSBRulequeryclaims(JSONObject object,String taskid,String type){
		INSBRulequeryclaims insbRulequeryclaims = new INSBRulequeryclaims();
		insbRulequeryclaims.setCreatetime(new Date());
		insbRulequeryclaims.setModifytime(new Date());
		insbRulequeryclaims.setOperator("guizequery");
		insbRulequeryclaims.setTaskid(taskid);
		insbRulequeryclaims.setInscorpid(getStringByObjectkey(object, "insCorpId"));

		String code = InsCompany.getCode(getStringByObjectkey(object, "insCorpCode")),
				name = InsCompany.getCode(getStringByObjectkey(object, "insCorpName"));

		if(StringUtils.isNotBlank(code)){
			insbRulequeryclaims.setInscorpcode(code);
		}else if(StringUtils.isNotBlank(name)){
			insbRulequeryclaims.setInscorpcode(name);
		} else if (StringUtils.isNotBlank(insbRulequeryclaims.getInscorpid())) {
			insbRulequeryclaims.setInscorpcode(insbRulequeryclaims.getInscorpid());
		}

		if (StringUtils.isNotBlank(insbRulequeryclaims.getInscorpcode())) {
			INSBProvider insbPorvider = insbProviderDao.selectById(insbRulequeryclaims.getInscorpcode());
			if (insbPorvider != null)
				insbRulequeryclaims.setInscorpname(insbPorvider.getPrvname());
		} else if (StringUtils.isNotBlank(getStringByObjectkey(object, "insCorpName"))) {
			insbRulequeryclaims.setInscorpname(getStringByObjectkey(object, "insCorpName"));
		}

		insbRulequeryclaims.setCasestarttime(getStringByObjectkey(object, "caseStartTime"));
		insbRulequeryclaims.setCaseendtime(getStringByObjectkey(object, "caseEndTime"));
		insbRulequeryclaims.setClaimamount(getDoubleByObjectkey(object, "claimAmount"));
		insbRulequeryclaims.setPolicyid(getStringByObjectkey(object, "policyId"));
		insbRulequeryclaims.setRisktype(type);
		return insbRulequeryclaims;
	}
	
	public void saveRulequeryotherinfo(String taskid,JSONObject jsonObject){
		INSBRulequeryotherinfo insbRulequeryotherinfo = new INSBRulequeryotherinfo();
		insbRulequeryotherinfo.setTaskid(taskid);
		INSBRulequeryotherinfo rulequeryotherinfo = insbRulequeryotherinfoDao.selectOne(insbRulequeryotherinfo);
		if(null == rulequeryotherinfo){
			rulequeryotherinfo = new INSBRulequeryotherinfo();
			rulequeryotherinfo.setCreatetime(new Date());
			rulequeryotherinfo.setModifytime(new Date());
			rulequeryotherinfo.setOperator("guizequery");
			rulequeryotherinfo.setTaskid(taskid);
			rulequeryotherinfo.setNoclaimdiscountcoefficient(getDoubleByObjectkey(jsonObject, "noClaimDiscountCoefficient"));
			rulequeryotherinfo.setNoclaimdiscountcoefficientreasons(getStringByObjectkey(jsonObject, "noClaimDiscountCoefficientReasons"));
			rulequeryotherinfo.setLoyaltyreasons(getStringByObjectkey(jsonObject, "loyaltyReasons"));
			rulequeryotherinfo.setTrafficoffencediscount(getDoubleByObjectkey(jsonObject, "trafficOffenceDiscount"));
			rulequeryotherinfo.setCompulsoryclaimrate(getDoubleByObjectkey(jsonObject, "compulsoryClaimRate"));
			rulequeryotherinfo.setCompulsoryclaimratereasons(getStringByObjectkey(jsonObject, "compulsoryClaimRateReasons"));
			rulequeryotherinfo.setFirstinsuretype(getStringByObjectkey(jsonObject, "firstInsureType"));
			rulequeryotherinfo.setBwcommercialclaimtimes(getStringByObjectkey(jsonObject, "bwCommercialClaimTimes"));
			rulequeryotherinfo.setBwlastclaimsum(getDoubleByObjectkey(jsonObject, "bwLastClaimSum"));
			rulequeryotherinfo.setBwcompulsoryclaimtimes(getStringByObjectkey(jsonObject, "bwCompulsoryClaimTimes"));
			rulequeryotherinfo.setBwlastcompulsoryclaimsum(getDoubleByObjectkey(jsonObject, "bwLastCompulsoryClaimSum"));
			rulequeryotherinfo.setClaimtimes(getInterByObjectkey(jsonObject, "claimTimes"));
			rulequeryotherinfo.setCompulsoryclaimtimes(getInterByObjectkey(jsonObject, "compulsoryClaimTimes"));
			rulequeryotherinfo.setLastclaimsum(getDoubleByObjectkey(jsonObject, "lastClaimSum"));
			rulequeryotherinfo.setSyendmark(getStringByObjectkey(jsonObject, "SYendMark"));
			rulequeryotherinfo.setErrormsgsy(getStringByObjectkey(jsonObject, "errorMsgSY"));
			rulequeryotherinfo.setJqendmark(getStringByObjectkey(jsonObject, "JQendMark"));
			rulequeryotherinfo.setErrormsgjq(getStringByObjectkey(jsonObject, "errorMsgJQ"));
			rulequeryotherinfo.setEfcdiscount(getStringByObjectkey(jsonObject, "efcDiscount"));
			rulequeryotherinfo.setVehicletaxoverduefine(getStringByObjectkey(jsonObject, "vehicleTaxOverdueFine"));
			rulequeryotherinfo.setRiskclass(getStringByObjectkey(jsonObject, "riskClass"));
			rulequeryotherinfo.setPureesale(getStringByObjectkey(jsonObject, "pureESale"));
			rulequeryotherinfo.setBwcommercialclaimrate(getStringByObjectkey(jsonObject, "bwCommercialClaimRate"));
			rulequeryotherinfo.setLwarrearstax(getStringByObjectkey(jsonObject, "lwArrearsTax"));
			rulequeryotherinfo.setPlatformcarprice(getStringByObjectkey(jsonObject, "platformCarPrice"));
			rulequeryotherinfo.setDrunkdrivingrate(getStringByObjectkey(jsonObject, "DrunkDrivingRate"));
			insbRulequeryotherinfoDao.insert(rulequeryotherinfo);
		}else{
			rulequeryotherinfo.setOperator("guizequery");
			rulequeryotherinfo.setModifytime(new Date());
			if (rulequeryotherinfo.getNoclaimdiscountcoefficient() == 0.0)
				rulequeryotherinfo.setNoclaimdiscountcoefficient(getDoubleByObjectkey(jsonObject, "noClaimDiscountCoefficient"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getNoclaimdiscountcoefficientreasons()))
				rulequeryotherinfo.setNoclaimdiscountcoefficientreasons(getStringByObjectkey(jsonObject, "noClaimDiscountCoefficientReasons"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getLoyaltyreasons()))
				rulequeryotherinfo.setLoyaltyreasons(getStringByObjectkey(jsonObject, "loyaltyReasons"));
			if (rulequeryotherinfo.getTrafficoffencediscount() == 0.0)
				rulequeryotherinfo.setTrafficoffencediscount(getDoubleByObjectkey(jsonObject, "trafficOffenceDiscount"));
			if (rulequeryotherinfo.getCompulsoryclaimrate() == 0.0)
				rulequeryotherinfo.setCompulsoryclaimrate(getDoubleByObjectkey(jsonObject, "compulsoryClaimRate"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getCompulsoryclaimratereasons()))
				rulequeryotherinfo.setCompulsoryclaimratereasons(getStringByObjectkey(jsonObject, "compulsoryClaimRateReasons"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getFirstinsuretype()))
				rulequeryotherinfo.setFirstinsuretype(getStringByObjectkey(jsonObject, "firstInsureType"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getBwcommercialclaimtimes()))
				rulequeryotherinfo.setBwcommercialclaimtimes(getStringByObjectkey(jsonObject, "bwCommercialClaimTimes"));
			if (rulequeryotherinfo.getBwlastclaimsum() == 0.0)
				rulequeryotherinfo.setBwlastclaimsum(getDoubleByObjectkey(jsonObject, "bwLastClaimSum"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getBwcompulsoryclaimtimes()))
				rulequeryotherinfo.setBwcompulsoryclaimtimes(getStringByObjectkey(jsonObject, "bwCompulsoryClaimTimes"));
			if (rulequeryotherinfo.getBwlastcompulsoryclaimsum() == 0.0)
				rulequeryotherinfo.setBwlastcompulsoryclaimsum(getDoubleByObjectkey(jsonObject, "bwLastCompulsoryClaimSum"));
			if (rulequeryotherinfo.getClaimtimes() == 0)
				rulequeryotherinfo.setClaimtimes(getInterByObjectkey(jsonObject, "claimTimes"));
			if (rulequeryotherinfo.getCompulsoryclaimtimes() == 0)
				rulequeryotherinfo.setCompulsoryclaimtimes(getInterByObjectkey(jsonObject, "compulsoryClaimTimes"));
			if (rulequeryotherinfo.getLastclaimsum() == 0.0)
				rulequeryotherinfo.setLastclaimsum(getDoubleByObjectkey(jsonObject, "lastClaimSum"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getSyendmark()))
				rulequeryotherinfo.setSyendmark(getStringByObjectkey(jsonObject, "SYendMark"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getErrormsgsy()))
				rulequeryotherinfo.setErrormsgsy(getStringByObjectkey(jsonObject, "errorMsgSY"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getJqendmark()))
				rulequeryotherinfo.setJqendmark(getStringByObjectkey(jsonObject, "JQendMark"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getErrormsgjq()))
				rulequeryotherinfo.setErrormsgjq(getStringByObjectkey(jsonObject, "errorMsgJQ"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getEfcdiscount()))
				rulequeryotherinfo.setEfcdiscount(getStringByObjectkey(jsonObject, "efcDiscount"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getVehicletaxoverduefine()))
				rulequeryotherinfo.setVehicletaxoverduefine(getStringByObjectkey(jsonObject, "vehicleTaxOverdueFine"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getRiskclass()))
				rulequeryotherinfo.setRiskclass(getStringByObjectkey(jsonObject, "riskClass"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getPureesale()))
				rulequeryotherinfo.setPureesale(getStringByObjectkey(jsonObject, "pureESale"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getBwcommercialclaimrate()))
				rulequeryotherinfo.setBwcommercialclaimrate(getStringByObjectkey(jsonObject, "bwCommercialClaimRate"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getLwarrearstax()))
				rulequeryotherinfo.setLwarrearstax(getStringByObjectkey(jsonObject, "lwArrearsTax"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getPlatformcarprice()))
				rulequeryotherinfo.setPlatformcarprice(getStringByObjectkey(jsonObject, "platformCarPrice"));
			if (StringUtil.isEmpty(rulequeryotherinfo.getDrunkdrivingrate()))
				rulequeryotherinfo.setDrunkdrivingrate(getStringByObjectkey(jsonObject, "DrunkDrivingRate"));
			insbRulequeryotherinfoDao.updateById(rulequeryotherinfo);
		}
	}

	/**
	 * 
	 * @param jsonObject
	 * @param key
	 * @return
	 */
	private String getStringByObjectkey(JSONObject jsonObject,String key){
		return jsonObject.containsKey(key) ? jsonObject.getString(key) : "";
	}
	/**
	 * 非首次投保:0,新车首次投保:1,旧车首次投保:2
	 * @param firstInsureType
	 * @return
	 */
	private String convertFirstInsureTypeToCm(String firstInsureType){
		Map<String, String> map = new HashMap<String, String>();
		map.put("非首次投保", "0");
		map.put("新车首次投保", "1");
		map.put("旧车首次投保", "2");
		return StringUtil.isEmpty(map.get(firstInsureType))?"":map.get(firstInsureType);
	}
	/**
	 * 
	 * @param jsonObject
	 * @param key
	 * @return
	 */
	private Double getDoubleByObjectkey(JSONObject jsonObject,String key){
		return StringUtil.isEmpty(getStringByObjectkey(jsonObject, key)) ? 0.0 : jsonObject.getDouble(key);
	}
	
	/**
	 * 
	 * @param jsonObject
	 * @param key
	 * @return
	 */
	private Integer getInterByObjectkey(JSONObject jsonObject,String key){
		return StringUtil.isEmpty(getStringByObjectkey(jsonObject, key)) ? 0 : jsonObject.getInt(key);
	}

	/**
	 * 推工作流节点,平台查询
	 */
	@Override
	public void nextTodoRuleQuery(String taskid, String inscomcode,	String subwkid) {
		LogUtil.info("规则平台查询60秒未返回保存数据taskid="+taskid+"=供应商id="+inscomcode+"=子流程id="+subwkid);
		saveFlowerrorToManWork(taskid, inscomcode, "规则平台查询60秒未返回数据,转人工规则", "12", "规则校验");
		if(StringUtil.isEmpty(subwkid)){
			//获取子流程id 
			Map<String, String> map = new HashMap<String, String>();
			map.put("taskid", taskid);
			map.put("inscomcode", inscomcode);
			INSBQuoteinfo quoteinfo = appInsuredQuoteDao.selectInsbQuoteInfoByTaskidAndPid(map);
			if(null != quoteinfo){
				subwkid = quoteinfo.getWorkflowinstanceid();
			}
		}
		String callback = WorkFlowUtil.updateInsuredInfoNoticeWorkflow(null,subwkid, "admin", "平台查询", "");
		LogUtil.info("规则平台查询60秒未返回保存数据taskid="+taskid+"=供应商id="+inscomcode+"=子流程id="+subwkid+"=平台查询返回结果="+callback);
	}
	
	/**
	 * 保存操作记录,工作流转人工判断标识
	 * @param taskid
	 * @param inscomcode
	 * @param errdesc 描述
	 * @param flowcode 对应INSBFlowerror表firoredi字段      1  规则返回的转人工,5年款匹配没有匹配到  6 非费改地区 走人工规则报价 7 特种车转人工   8 代理人备注    9  投保日期   10 转人工规则 11普通平台查询60秒为查到数据
	 */
	private void saveFlowerrorToManWork(String taskid,String inscomcode,String errdesc,String flowcode,String flowName){
		LogUtil.info("saveFlowerrorToManWork"+taskid+",供应商="+inscomcode+",描述="+errdesc);
		INSBFlowerror insbFlowerror = new INSBFlowerror();
		insbFlowerror.setTaskid(taskid);
		insbFlowerror.setInscomcode(inscomcode);
		insbFlowerror.setFiroredi("4");
		insbFlowerror.setFlowcode(flowcode);
		INSBFlowerror flowerror = insbFlowerrorDao.selectOne(insbFlowerror);
		if(null != flowerror){
			//备份已经存在的
			flowerror.setModifytime(new Date());
			flowerror.setTaskid(taskid+"_temp");
			flowerror.setInscomcode(inscomcode);
			flowerror.setFlowname(flowName);
			flowerror.setFiroredi("4");
			insbFlowerrorDao.updateById(flowerror);
		}
		INSBFlowerror newflowerror = new INSBFlowerror();
		newflowerror.setOperator("caryear");
		newflowerror.setCreatetime(new Date());
		newflowerror.setTaskid(taskid);
		newflowerror.setInscomcode(inscomcode);
		newflowerror.setFlowcode(flowcode);//规则级别
		newflowerror.setFlowname(flowName);
		newflowerror.setErrorcode(flowcode);
		newflowerror.setFiroredi("4");
		newflowerror.setTaskstatus("guize5");
		newflowerror.setErrordesc(errdesc);
		insbFlowerrorDao.insert(newflowerror);
	}
	
	
	public static void main(String[] args) {
		//System.out.println(StringUtils.isEmpty(null));
		/*Date endDate = ModelUtil.conbertStringToNyrDate("2017-02-28");
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		System.out.println(end.get(Calendar.DAY_OF_MONTH));
		end.set(Calendar.DAY_OF_MONTH, 29);
		System.out.println(end.getTime());
		String json ="{\"bizPolicies\":[{\"policyEndTime\":\"2017-06-06 00:00:00\",\"policyStartTime\":\"2016-06-06 00:00:00\"}],\"bizClaims\":[{\"insCorpCode\":\"CAIC\",\"policyId\":\"605202016371500003353\",\"caseStartTime\":\"2017-01-26 09:40:00\",\"claimAmount\":\"1800.0\",\"caseEndTime\":\"2017-02-04 10:42:00\"}],\"bwCommercialClaimTimes\":\"去年发生一次理赔\",\"compulsoryClaimRate\":\"0.70\",\"licenseNo\":\"鲁PKU263\",\"compulsoryClaimTimes\":\"0\",\"bwCompulsoryClaimTimes\":\"连续三年没有理赔\",\"firstInsureType\":\"非首次投保\",\"trafficOffenceDiscount\":\"1.00\",\"claimTimes\":\"1\",\"taskId\":\"20281491\",\"enrollDate\":\"2014-06-05\",\"succeed\":\"success\",\"bwLastClaimSum\":\"1800.00\",\"task\":{\"carBrandName\":\"雪佛兰SGM7209ATA轿车\",\"quoteItems_ruleItem.basicRiskPremium\":\"1660.988160\",\"modelCode\":\"MRD1011SHT\",\"modelLoad\":0,\"taxPrice\":\"157300.00\",\"car.specific.selfInsureRate\":\"0.85\",\"seatCnt\":5,\"car.specific.selfChannelRate\":\"0.85\",\"price\":\"144900.00\",\"vehicleInfo.tradeModelCode\":\"BTYOMSUC0001\",\"displacement\":\"1.998\",\"vehicleInfo.vehicleType\":\"六座以下客车\",\"carModelDate\":\"201302\",\"plateform.InsureCo\":\"利宝保险有限公司\"},\"vinNo\":\"LSGGG54Y0DS284929\",\"noClaimDiscountCoefficient\":\"1.00\",\"compulsoryClaimRateReasons\":\"连续三年没有理赔\",\"loyaltyReasons\":\"\",\"noClaimDiscountCoefficientReasons\":\"新保或上年发生1次赔款\",\"engineNo\":\"140490777\",\"lastClaimSum\":\"1800.00\"}";
		JSONObject jsonObject = JSONObject.fromObject(json);
		System.out.println(jsonObject.get("bizPolicies")+"=="+jsonObject.get("efcPolicies"));
		if(jsonObject.get("efcPolicies") != null){
			System.out.println(jsonObject.get("efcPolicies"));
		}
		JSONArray jqarray = JSONArray.fromObject(jsonObject.get("efcPolicies"));
		System.out.println(jqarray);
		for(int i = 0;i < jqarray.size();i ++){
			System.out.println(jqarray.size());
		}*/
	}
}
