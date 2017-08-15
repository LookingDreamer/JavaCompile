package com.zzb.cm.controller;

import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.StringUtil;
import com.common.CloudQueryUtil;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBFilelibrary;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.entity.INSBRiskInfo;
import com.zzb.cm.service.INSBCarinfoService;
import com.zzb.cm.service.INSBCarkindpriceService;
import com.zzb.cm.service.INSBCarmodelinfoService;
import com.zzb.cm.service.INSBCarmodelinfohisService;
import com.zzb.cm.service.INSBCommonWorkflowTrackservice;
import com.zzb.cm.service.INSBFilelibraryService;
import com.zzb.cm.service.INSBManualPriceService;
import com.zzb.cm.service.INSBMultiplePriceListService;
import com.zzb.cm.service.INSBPersonService;
import com.zzb.cm.service.INSBPlatcarpriceService;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.cm.service.INSBQuotetotalinfoService;
import com.zzb.cm.service.INSBSpecifydriverService;
import com.zzb.cm.service.INSBUserremarkService;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBOperatorcommentService;
import com.zzb.conf.service.INSBPolicyitemService;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.conf.service.INSBWorkflowsubtrackService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.lastinsured.CarModel;
import com.zzb.mobile.model.lastinsured.LastYearPolicyInfoBean;
import com.zzb.mobile.model.policyoperat.PolicyCommitParam;
import com.zzb.mobile.service.AppMyOrderInfoService;
/**
 * CM系统 车型投保  多方报价列表
 */ 
@Controller
@RequestMapping("/multiplelist/*")
public class INSBMultiplePriceListController extends BaseController {
	@Resource
	private INSBQuoteinfoService insbQuoteinfoService;
	@Resource
	private INSBProviderService insbProviderService;
	@Resource
	private INSBCarinfoService insbCarinfoService;
	@Resource
	private INSBPersonService insbPersonService;
	@Resource
	private INSBPlatcarpriceService insbPlatcarpriceService;
	@Resource
	private INSBCarkindpriceService insbCarkindpriceService;
	@Resource
	private INSBUserremarkService insbUserremarkService;
	@Resource
	private INSBQuotetotalinfoService insbQuotetotalinfoService;
	@Resource
	private INSBManualPriceService insbManualPriceService;
	@Resource
	private INSBWorkflowsubtrackService insbWorkflowsubtrackService;
	@Resource
	private INSBUsercommentService insbUsercommentService;
	@Resource
	private INSBOperatorcommentService insbOperatorcommentService;
	@Resource
	private INSBCommonWorkflowTrackservice insbCommonWorkflowTrackservice;
	@Resource
	private INSBCarmodelinfohisService insbCarmodelinfohisService;
	@Resource
	private INSBFilelibraryService insbFilelibraryService;
	@Resource
	private INSBPolicyitemService policyitemService;
	@Resource
	private INSBAgentService insbAgentService;
	@Resource
	private AppMyOrderInfoService appMyOrderInfoService;
	@Resource
	private INSBMultiplePriceListService insbMultiplePriceListService;
	@Resource
	private INSBSpecifydriverService insbSpecifydriverService;
	@Resource
	private INSBCarmodelinfoService insbCarmodelinfoService;
	@Resource
	private INSBWorkflowsubService insbWorkflowsubService;

	@Resource
	private IRedisClient redisClient;

	/**
	 * 页面跳转
	 * @param session
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "multiplelist", method = RequestMethod.GET)
	@ResponseBody 
	public ModelAndView orderList(HttpSession session,String taskid) throws ControllerException {
		ModelAndView mav = new ModelAndView("cm/valetcatalogue/multiplePriceList");
//		String taskid="18968";
		Map<String, Object> agentInfo = insbAgentService.getCarTaskAgentInfo(taskid,"COMMON");//代理人信息
		//获取保险公司code列表（报价期间使用三个参数，非报价期间使用一个参数的重载方法）
		List<String> inscomcodeList = insbQuotetotalinfoService.getInscomcodeListByInstanceId(taskid);
		List<Map<String, Object>> carInsTaskInfoList = new ArrayList<Map<String,Object>>();//子任务信息列表
		for (int i = 0; i < inscomcodeList.size(); i++) {
			String inscomcode = inscomcodeList.get(i);
			INSBProvider iProvider=insbProviderService.queryById(inscomcode);
			Map<String, Object> deptinfo= insbMultiplePriceListService.getDeptInfo(taskid, inscomcode);
//			Map<String, Object> carInfo = insbCarinfoService.getCarTaskCarInfo(taskid, inscomcode, "SHOW");//车辆信息
//			Map<String, Object> relationPersonInfo = insbPersonService.getCarTaskRelationPersonInfo(taskid, inscomcode , "SHOW");//关系人信息
//			Map<String, Object> otherInfo = insbCarinfoService.getCarTaskOtherInfo(taskid, inscomcode, "SHOW");//其他信息
			Map<String, Object> taskAllInfo = insbCarinfoService.getTaskAllInfo(taskid, inscomcode);
//			Map<String,Object> insConfigInfo = insbCarkindpriceService.getCarInsConfigByInscomcode(inscomcode, taskid);//保险配置信息
			//查询给操作员的备注（新版使用）
//			List<INSBOperatorcomment> operatorcommentList = insbOperatorcommentService.getOperatorCommentByMaininstanceid(taskid, inscomcode);
			//查询影像信息
			List<INSBFilelibrary> imageList = insbFilelibraryService.queryByFilebusinessCode(taskid);
//			Map<String, Object> remark = new HashMap<String, Object>();//备注信息
//			remark.put("opcommentList", operatorcommentList);
			//获取补充信息中车型上面部分信息
			Map<String, Object> replenishInfo = insbManualPriceService.getReplenishInfo(taskid);
			//平台车型信息
			CarModel platCarModel = insbManualPriceService.getPlatCarModelMessage(taskid);
			//获取补充信息中车型下面部分信息
			Map<String, Object> localdbReplenishInfo = insbManualPriceService.getLocaldbReplenishInfo(taskid, inscomcode, (String)agentInfo.get("deptCode"));
//			taskAllInfo.put("insConfigInfo",insConfigInfo);
			taskAllInfo.put("deptinfo", deptinfo);
			taskAllInfo.put("inscomcode", inscomcode);
			taskAllInfo.put("inscomname", iProvider.getPrvshotname());
			taskAllInfo.put("imageList", imageList);
//			taskAllInfo.put("remarkinfo", remark);
			taskAllInfo.put("replenishInfo", replenishInfo);
			taskAllInfo.put("platCarModel", platCarModel);
			taskAllInfo.put("localdbReplenishInfo", localdbReplenishInfo);
			// 添加投保单号信息
			Map<String, Object> proposalInfo =  policyitemService.getPolicyNumInfo(taskid);
			mav.addObject("proposalInfo", proposalInfo);
			carInsTaskInfoList.add(taskAllInfo);
		}
		mav.addObject("showEditFlag","1");//是否显示修改功能（0不显示，1显示）
		mav.addObject("agentInfo",agentInfo);
		mav.addObject("carInsTaskInfoList",carInsTaskInfoList);
		mav.addObject("taskid", taskid);
		return mav;
	}
	
	/**
	 * 提交投保接口
	 * 接口描述：通过实例id提交投保推送工作流
	 * 请求方式 POST
	 * 请求地址 /mobile/insured/myTask/policySubmit
	 * @param processInstanceId 流程实例id, inscomcode 保险公司code
	 * agentnum 代理人工号, notice 投保备注(提交投保前一个页面录入，由于此页面并没有生成订单表记录，故推迟到提交投保时录入)
	 * totalproductamount 订单总金额（可以从提交页面直接获取）
	 */
	@RequestMapping(value = "policySubmit", method = RequestMethod.POST)
	@ResponseBody
	public String policySubmit(@ModelAttribute PolicyCommitParam policyCommitParam)
			throws ControllerException {
		System.out.println("come in");
		policyCommitParam.setTotalproductamount(insbCarkindpriceService.getTotalProductAmount(policyCommitParam.getProcessInstanceId(), policyCommitParam.getInscomcode()));
		String flag = appMyOrderInfoService.policySubmit(policyCommitParam.getProcessInstanceId(), 
				policyCommitParam.getInscomcode(), policyCommitParam.getAgentnum(), 
				policyCommitParam.getTotalproductamount(), policyCommitParam.getTotalpaymentamount());
		CommonModel result = new CommonModel();
		Map<String, Object> body = new HashMap<String, Object>();
		if("success".equals(flag)){
			body.put("inscomcode", policyCommitParam.getInscomcode());// 保险公司code
			body.put("processInstanceId", policyCommitParam.getProcessInstanceId());// 流程实例id
			result.setBody(body);
			result.setStatus("success");
			result.setMessage("提交投保成功！");
		}else{
			result.setStatus("fail");
			if(flag!=null){
				result.setMessage(flag);
			}else{
				result.setMessage("提交投保失败！");
			}
		}
		return result.getStatus();
	}
	
	@RequestMapping(value = "insuredconflist", method = RequestMethod.GET)
	@ResponseBody 
	public CommonModel insuredConfList(@RequestParam(value="taskid") String taskid,@RequestParam(value="inscomcode") String inscomcode) throws ControllerException {
		return insbMultiplePriceListService.insuredConfList(taskid,inscomcode);
	}
	
	/**
	 * 历史理赔次数@RequestBody String carno
	 */
	@RequestMapping(value="claimNumber",method=RequestMethod.POST)
	@ResponseBody
	public String claimNumber(@RequestParam String taskid){
		LastYearPolicyInfoBean lastYearPolicyInfoBean = redisClient.get(Constants.TASK, taskid,LastYearPolicyInfoBean.class);
		String lastYearClaimBean = null;
		if (lastYearPolicyInfoBean != null && null != lastYearPolicyInfoBean.getLastYearClaimBean()) {
			lastYearClaimBean = JSONObject.fromObject(lastYearPolicyInfoBean.getLastYearClaimBean()).toString();
		}else {
			INSBQuotetotalinfo queryquotetotalinfo = new INSBQuotetotalinfo();
			queryquotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoService.queryOne(queryquotetotalinfo);
			INSBCarinfo queryCarinfo = new INSBCarinfo();
			queryCarinfo.setTaskid(taskid);
			INSBCarinfo carinfo = insbCarinfoService.queryOne(queryCarinfo);
			lastYearPolicyInfoBean = queryLastInsuredInfoByPlateNumber(taskid, carinfo.getCarlicenseno(), quotetotalinfo.getInsprovincecode(), carinfo.getPreinscode());
			if (lastYearPolicyInfoBean != null && null != lastYearPolicyInfoBean.getLastYearClaimBean()) {
				lastYearClaimBean = JSONObject.fromObject(lastYearPolicyInfoBean.getLastYearClaimBean()).toString();
			}
		}
		return lastYearClaimBean;
	}
	
	/**
	 * 上一年险种
	 */
	@RequestMapping(value="getplant",method=RequestMethod.GET)
	@ResponseBody
	public String getplant(@RequestParam(value="taskid") String taskid){
		LastYearPolicyInfoBean lastYearPolicyInfoBean = redisClient.get(Constants.TASK, taskid,LastYearPolicyInfoBean.class);
		List<INSBRiskInfo> lastYearRiskinfos = null;
		if (lastYearPolicyInfoBean == null) {
			INSBQuotetotalinfo queryquotetotalinfo = new INSBQuotetotalinfo();
			queryquotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoService.queryOne(queryquotetotalinfo);
			INSBCarinfo queryCarinfo = new INSBCarinfo();
			queryCarinfo.setTaskid(taskid);
			INSBCarinfo carinfo = insbCarinfoService.queryOne(queryCarinfo);
			lastYearPolicyInfoBean = queryLastInsuredInfoByPlateNumber(taskid, carinfo.getCarlicenseno(), quotetotalinfo.getInsprovincecode(), carinfo.getPreinscode());
		}
		if (lastYearPolicyInfoBean != null) {
			lastYearRiskinfos = JSONArray.toList(lastYearPolicyInfoBean.getLastYearRiskinfos(), INSBRiskInfo.class);
		}
		if (lastYearRiskinfos != null && lastYearRiskinfos.size() > 0) {
			for (INSBRiskInfo insbRiskInfo : lastYearRiskinfos) {
				if ("1".equals(insbRiskInfo.getAmount())) {
					insbRiskInfo.setAmount("投保");
				}
			}
		}
		Map<Object, Object> map = new HashMap<>();
		map.put("records", "10000");
		map.put("page", 1);
		if (lastYearRiskinfos != null) {
			map.put("total", lastYearRiskinfos.size());
			map.put("rows", lastYearRiskinfos);
		}else {
			map.put("total", 0);
			map.put("rows", null);
		}
		JSONObject jsonObject = JSONObject.fromObject(map);
		return jsonObject.toString();
	}
	
	/**
	 * 平台查询
	 * @param taskid 实例id
	 * @param plateNumber 车牌号
	 * @param provinceCode 市代码
	 * @param provid  上年投保公司id
	 * @return
	 */
	private LastYearPolicyInfoBean queryLastInsuredInfoByPlateNumber(String taskid,String plateNumber,String provinceCode,String provid){
		//先从redis里面取值，取到直接返回 
//		LastYearPolicyInfoBean yearPolicyInfoBean = redisClient.get(taskid,LastYearPolicyInfoBean.class);
		//quickflag  0 正常投保  1快速续保
		String quickflag = "0";
		//不传车辆信息
		JSONObject object = new JSONObject();
		object.put("flag", "XB");
		//上年投保公司有就传，没有不传
		if(!StringUtil.isEmpty(provid)){
			quickflag = "1";
			object.put("robotId", provid);
		}
		object.put("quickflag", quickflag);
		JSONObject inParas = new JSONObject();
		inParas.put("car.specific.license", plateNumber);
		object.put("inParas", inParas);
		object.put("areaId", provinceCode);
		object.put("eid", UUID.randomUUID());
		//测试数据
		//String resultJson = "{\"carModels\":[{\"analogyprice\":0,\"analogytaxprice\":0,\"brandname\":\"雪铁龙\",\"carmodelCode\":\"\",\"carmodelsource\":\"\",\"configname\":\"\",\"displacement\":\"2.253\",\"factoryname\":\"神龙汽车有限公司\",\"familyid\":\"\",\"familyname\":\"C5\",\"fullweight\":\"0.0\",\"gearbox\":\"手自一体\",\"gearboxtype\":\"\",\"jqvehicletype\":\"\",\"kindname\":\"\",\"maketdate\":\"\",\"manufacturer\":\"1\",\"modelLoads\":\"0.0\",\"price\":176900,\"seat\":\"5\",\"serchtimes\":\"\",\"supplierId\":\"\",\"syvehicletype\":\"\",\"taxprice\":192000,\"type\":\"\",\"vehiclecode\":\"XTAACD0017\",\"vehiclename\":\"东风雪铁龙DC7237DT轿车\",\"vehicleno\":\"鲁A0158D\",\"vehicletype\":\"轿车\",\"yearstyle\":\"限制到月\"}],\"carowner\":{\"addrss\":\"\",\"email\":\" \",\"idno\":\"121213131313113\",\"idtype\":\"1\",\"mobile\":\"1213131131313\",\"name\":\"济南神泰经贸有限责任公司\"},\"lastYearCarinfoBean\":{\"area\":\"3\",\"chgownerflag\":\"0\",\"engineno\":\"0036940\",\"registerdate\":\"2011-03-15 00:00:00\",\"usingnature\":\"\",\"vehicleframeno\":\"LDCA13R4482038939\",\"vehicleno\":\"鲁A0158D\"},\"lastYearClaimBean\":null,\"lastYearPolicyBean\":{\"discount\":0,\"enddate\":\"2016-03-25 00:00:00\",\"insureder\":{\"addrss\":\"\",\"email\":\"123@qq.com\",\"idno\":\"1213131313313\",\"idtype\":\"1\",\"mobile\":\"15226008691\",\"name\":\"合杰\"},\"jqenddate\":\"2016-03-25 00:00:00\",\"jqpolicyno\":\"\",\"jqprem\":800,\"jqstartdate\":\"2015-03-25 00:00:00\",\"policyno\":\"\",\"proper\":{\"addrss\":\"\",\"email\":\"21313qq.com\",\"idno\":\"77743085-1\",\"idtype\":\"99\",\"mobile\":\"\",\"name\":\"济南神泰经贸有限公司\"},\"startdate\":\"2015-03-25 00:00:00\",\"sumprem\":3336,\"syprem\":3336,\"tax\":0},\"lastYearRiskinfos\":[{\"amount\":185900,\"kindcode\":\"VehicleDemageIns\",\"kindname\":\"车辆损失险\",\"prem\":1591},{\"amount\":100000,\"kindcode\":\"ThirdPartyIns\",\"kindname\":\"第三者责任险\",\"prem\":747},{\"amount\":10000,\"kindcode\":\"DriverIns\",\"kindname\":\"司机责任险\",\"prem\":29},{\"amount\":10000,\"kindcode\":\"PassengerIns\",\"kindname\":\"乘客责任险\",\"prem\":73},{\"amount\":113357,\"kindcode\":\"TheftIns\",\"kindname\":\"全车盗抢险\",\"prem\":441},{\"amount\":1,\"kindcode\":\"NcfBasicClause\",\"kindname\":\"基本险不计免赔\",\"prem\":454},{\"amount\":1,\"kindcode\":\"VehicleCompulsoryIns\",\"kindname\":\"交强险\",\"prem\":800},{\"amount\":1,\"kindcode\":\"VehicleTax\",\"kindname\":\"车船税\",\"prem\":900}],\"lastYearSupplierBean\":{\"supplierid\":\"200237\",\"suppliername\":\"国寿财险山东\"},\"message\":\"成功\",\"status\":\"0\"}";
		String resultJson = CloudQueryUtil.getLastYearInsurePolicy(object.toString());
		JSONObject jsonObject=JSONObject.fromObject(resultJson);
		LastYearPolicyInfoBean yearPolicyInfoBean = (LastYearPolicyInfoBean) JSONObject.toBean(jsonObject, LastYearPolicyInfoBean.class);
		return yearPolicyInfoBean;
	}
	//跳出投保单信息所需弹出框
	@RequestMapping(value="EditInsurancePolicyInfo", method=RequestMethod.GET)
	public ModelAndView showOrEditInsurancePolicyInfo(String mark, String taskid, String num, String inscomcode){
		ModelAndView mav = new ModelAndView();
		if("appointDriver".equalsIgnoreCase(mark)){
			//跳出指定驾驶人弹出框
			mav.setViewName("cm/valetcatalogue/appointDriver");
			mav.addObject("driversInfo",insbSpecifydriverService.getSpecifydriversInfoByCarinfoId(taskid,inscomcode,"SHOW"));
		}else if("editOtherInfo".equalsIgnoreCase(mark)){
			//跳出修改其他信息弹出框
			mav.setViewName("cm/valetcatalogue/editOtherInfoList");
			Map<String, Object> otherInfo = insbCarinfoService.getCarTaskOtherInfo(taskid,inscomcode, "EDIT");
			List<Map<String, Object>> specifydriversInfo = insbSpecifydriverService.getSpecifydriversInfoByCarinfoId(taskid,inscomcode,"EDIT");
			Map<String, Object> GenderAndLicenseTypeCodeList = insbSpecifydriverService.getCodeOfGenderAndLicenseType();
			//增加服务器时间
//				Date date=new Date();
//				//Date businessstartdate=(Date)otherInfo.get("businessstartdate");
//				//Long ninetyDate=(new Date(otherInfo.get("businessstartdate")).getTime()+(90*24*3600*1000);
//				//Long ninetyDate=businessstartdate.getTime()+(90*24*3600*1000);
//				//Date ninetyday=new Date(ninetyDate);
//				SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
//				String todaydate=dateFormat.format(date);
//				//String nintydate=dateFormat.format(ninetyday);
			mav.addObject("GenderAndLicenseTypeCodeList",GenderAndLicenseTypeCodeList);
			mav.addObject("otherInfo",otherInfo);
			mav.addObject("driversInfo",specifydriversInfo);
//				mav.addObject("todaydate",todaydate);
//				//mav.addObject("nintydate",nintydate);
			
		}else if("editCarInfo".equalsIgnoreCase(mark)){
			//跳出修改车辆信息弹出框
			mav.setViewName("cm/valetcatalogue/editModelInfo");
			Map<String, Object> carInfo = insbCarinfoService.getCarTaskCarInfo(taskid,inscomcode, "EDIT");
			Map<String, Object> Carmodelinfo = insbCarmodelinfoService.getCarmodelInfoByCarinfoId(taskid,(String)carInfo.get("carinfoId"),inscomcode,"CARINFODIALOG");
			mav.addObject("carInfo", carInfo);
			mav.addObject("Carmodelinfo", Carmodelinfo);
		}else if("editCarModelInfo".equalsIgnoreCase(mark)){
			//跳出修改车型信息弹出框
			mav.setViewName("cm/common/editCarModelInfo");
			Map<String, Object> carInfo = insbCarinfoService.getCarTaskCarInfo(taskid,inscomcode, "CARMODEL");
			Map<String, Object> Carmodelinfo  = insbCarmodelinfoService.getCarmodelInfoByCarinfoId(taskid,(String)carInfo.get("carinfoId"),inscomcode,"CARMODELDIALOG");
			mav.addObject("carInfo", carInfo);
			mav.addObject("Carmodelinfo", Carmodelinfo);
		}else if("editRelationPersonInfo".equalsIgnoreCase(mark)){
			//跳出修改关系人信息弹出框
			mav.setViewName("cm/valetcatalogue/editRelationInfo");
			Map<String, Object> relationPersonInfo = insbPersonService.getCarTaskRelationPersonInfo(taskid,inscomcode,"EDIT");
			mav.addObject("relationPersonInfo", relationPersonInfo);
		}else if("showOwnerInfo".equalsIgnoreCase(mark) || "showInsuredInfo".equalsIgnoreCase(mark) || "showApplicantInfo".equalsIgnoreCase(mark)
				 || "showPersonForRightInfo".equalsIgnoreCase(mark) || "showLinkPersonInfo".equalsIgnoreCase(mark)){
			//跳出关系人或车主个人信息弹出框
			mav.setViewName("cm/common/personInfo");
			Map<String, Object> person = new HashMap<String, Object>();
			if("showOwnerInfo".equalsIgnoreCase(mark)){
				person = insbPersonService.getOneOfCarTaskRelationPersonInfo(taskid, inscomcode, "OWNER");
			}else if("showInsuredInfo".equalsIgnoreCase(mark)){
				person = insbPersonService.getOneOfCarTaskRelationPersonInfo(taskid, inscomcode, "INSURED");
			}else if("showApplicantInfo".equalsIgnoreCase(mark)){
				person = insbPersonService.getOneOfCarTaskRelationPersonInfo(taskid, inscomcode, "APPLICANT");
			}else if("showPersonForRightInfo".equalsIgnoreCase(mark)){
				person = insbPersonService.getOneOfCarTaskRelationPersonInfo(taskid, inscomcode, "PERSONFORRIGHT");
			}else if("showLinkPersonInfo".equalsIgnoreCase(mark)){
				person = insbPersonService.getOneOfCarTaskRelationPersonInfo(taskid, inscomcode, "LINKPERSON");
			}
			mav.addObject("person", person);
			mav.addObject("taskid", taskid);
			mav.addObject("mark", mark);
		}
		mav.addObject("num", num);
		return mav;
	}
	
	/**
	 * 查询子流程是否处于选择投保节点
	 */
	@RequestMapping(value="getProcessStatus",method=RequestMethod.POST)
	@ResponseBody
	public String getProcessStatus(@RequestParam String taskid,@RequestParam String provid){
		String result;
		INSBQuotetotalinfo totalInfo = new INSBQuotetotalinfo();
		totalInfo.setTaskid(taskid);
		totalInfo = insbQuotetotalinfoService.queryOne(totalInfo);
		INSBQuoteinfo quoteInfo = new INSBQuoteinfo();
		quoteInfo.setQuotetotalinfoid(totalInfo.getId());
		quoteInfo.setInscomcode(provid);
		quoteInfo = insbQuoteinfoService.queryOne(quoteInfo);
		String subInstanceid = quoteInfo.getWorkflowinstanceid();
		INSBWorkflowsub workflowsub = new INSBWorkflowsub();
		workflowsub.setInstanceid(subInstanceid);
		workflowsub = insbWorkflowsubService.queryOne(workflowsub);
		String workflowcode = workflowsub.getTaskcode();
		if ("14".equals(workflowcode)) {
			result = "0";
		}else {
			result = "1";
		}
		return result;
		
	}
	
	/**
	 * 检查是否有流程处于核保阶段
	 * 
	 */
	@RequestMapping(value="/checkSubStep",method=RequestMethod.POST)
	@ResponseBody
	public String  checkSubStep(@RequestParam String mainTaskid,@RequestParam String agentnum ){
		String province = insbAgentService.getAgentProvince(agentnum);
		if ("370000".equals(province)) {
			INSBWorkflowsub workflowsub = new INSBWorkflowsub();
			workflowsub.setMaininstanceid(mainTaskid);
			List<INSBWorkflowsub> allWorkflowsubs = insbWorkflowsubService.queryList(workflowsub);
			List<String> allStatus = new ArrayList<String>();
			for (INSBWorkflowsub insbWorkflowsub : allWorkflowsubs) {
				allStatus.add(insbWorkflowsub.getTaskcode());
			}
			List<String > hebaoList = Arrays.asList("18","17","16","19","20","21");
			boolean flag = false;
			for (String string : allStatus) {
				if(hebaoList.contains(string)){
					flag = true;
					break;
				}
			}
			if (flag) {
				return "1";
			}else {
				return "0";
			}
		}else {
			return "0";
		}
	}
}
