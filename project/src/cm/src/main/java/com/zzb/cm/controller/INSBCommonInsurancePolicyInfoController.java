package com.zzb.cm.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.alibaba.fastjson.JSON;
import com.common.JsonUtil;
import com.zzb.cm.controller.vo.*;
import com.zzb.cm.dao.INSBCarinfohisDao;
import com.zzb.cm.service.*;
import com.zzb.conf.entity.INSBAgent;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.common.PagingParams;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.entity.INSBFilelibrary;
import com.zzb.cm.entity.INSBInvoiceinfo;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.conf.entity.INSBCertification;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBCertificationService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.AppInsuredQuoteService;



/**
 * 保单信息公共页面
 */
@Controller
@RequestMapping("/common/insurancepolicyinfo/*")
public class INSBCommonInsurancePolicyInfoController extends BaseController {
	@Resource
	private INSBSpecifydriverService insbSpecifydriverService;
	@Resource
	private INSBCarinfoService insbCarinfoService;
	@Resource
	private INSBCarmodelinfoService insbCarmodelinfoService;
	@Resource
	private INSBPersonService insbPersonService;
	@Resource
	private INSBCarkindpriceService insbCarkindpriceService;
	@Resource
	private INSBManualPriceService insbManualPriceService;
	@Resource
	private HttpServletRequest request;
	@Resource
	private INSCCodeService inscCodeService;
	@Resource
	private INSBFilelibraryService insbFilelibraryService;
	@Resource
	private INSBFilelibraryService inscFilelibraryService;
	@Resource
	private AppInsuredQuoteService appInsuredQuoteService;
	@Resource
	private INSCCodeDao inscCodeDao;
	@Resource
	private INSBCertificationService insbCertificationService;
	@Resource 
	private INSBAgentService insbAgentservice;
	@Resource
	private INSBCarinfohisDao insbCarinfohisDao;
	@Resource
	private INSBQuotetotalinfoService insbQuotetotalinfoService;
	@Resource
	private INSBMyTaskService myTaskService;
	@Resource
	private INSBInsuresupplyparamService insbInsuresupplyparamService;
	@Resource
	private RulePlatformQueryService rulePlatformQueryService;
	//跳出投保单信息所需弹出框
	@RequestMapping(value="showOrEditInsurancePolicyInfo", method=RequestMethod.GET)
	public ModelAndView showOrEditInsurancePolicyInfo(String mark, String taskid, String num, String inscomcode){
		ModelAndView mav = new ModelAndView();
		if("appointDriver".equalsIgnoreCase(mark)){
			//跳出指定驾驶人弹出框
			mav.setViewName("cm/common/appointDriver");
			mav.addObject("driversInfo",insbSpecifydriverService.getSpecifydriversInfoByCarinfoId(taskid,inscomcode,"SHOW"));
		}else if("editOtherInfo".equalsIgnoreCase(mark)){
			//跳出修改其他信息弹出框
			mav.setViewName("cm/common/editOtherInfo");
			Map<String, Object> otherInfo = insbCarinfoService.getCarTaskOtherInfo(taskid,inscomcode, "EDIT");
			List<Map<String, Object>> specifydriversInfo = insbSpecifydriverService.getSpecifydriversInfoByCarinfoId(taskid,inscomcode,"EDIT");
			Map<String, Object> GenderAndLicenseTypeCodeList = insbSpecifydriverService.getCodeOfGenderAndLicenseType();
			CommonModel deptListInfo = appInsuredQuoteService.getProvider(taskid, inscomcode);
			Map<String,String> inscompany=insbCarinfoService.getInscompany(inscomcode);
			//增加服务器时间
//			Date date=new Date();
//			//Date businessstartdate=(Date)otherInfo.get("businessstartdate");
//			//Long ninetyDate=(new Date(otherInfo.get("businessstartdate")).getTime()+(90*24*3600*1000);
//			//Long ninetyDate=businessstartdate.getTime()+(90*24*3600*1000);
//			//Date ninetyday=new Date(ninetyDate);
//			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
//			String todaydate=dateFormat.format(date);
//			//String nintydate=dateFormat.format(ninetyday);
			mav.addObject("GenderAndLicenseTypeCodeList",GenderAndLicenseTypeCodeList);
			mav.addObject("otherInfo",otherInfo);
			mav.addObject("driversInfo",specifydriversInfo);
			mav.addObject("deptListInfo",deptListInfo);
			mav.addObject("inscompany",inscompany);
//			mav.addObject("todaydate",todaydate);
//			//mav.addObject("nintydate",nintydate);
			
		}else if("editCarInfo".equalsIgnoreCase(mark)){
			//跳出修改车辆信息弹出框
			mav.setViewName("cm/common/editCarInfo");
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
			mav.setViewName("cm/common/editRelationPersonInfo");
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
		}else if("editSupplyParam".equalsIgnoreCase(mark)){
			//跳出修改补充数据项弹出框
			mav.setViewName("cm/common/editSupplyParam");
			mav.addObject("supplyParams", insbInsuresupplyparamService.getParamsByTask(taskid, inscomcode, true));
			mav.addObject("taskid", taskid);
			mav.addObject("inscomcode", inscomcode);
			mav.addObject("CertKindList", inscCodeService.queryINSCCodeByCode("CertKinds","CertKinds"));//证件类型列表
		}
		mav.addObject("num", num);
		return mav;
	}

	/**
	 * 更新保存补充数据项
	 */
	@RequestMapping(value = "savesupplyparam", method = RequestMethod.POST)
	@ResponseBody
	public String savesupplyparam(HttpSession session, SupplyParamVo supplyparam){
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");

		try {
			insbInsuresupplyparamService.saveByTask(supplyparam.getSupplyparam(), supplyparam.getTaskid(), supplyparam.getInscomcode(), loginUser.getUsercode());
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	/**
	 * 修改其他信息
	 */
	@RequestMapping(value = "editOtherInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> editOtherInfo(HttpSession session, @ModelAttribute OtherInformationVO otherInformation,
			@ModelAttribute INSBInvoiceinfo insbInvoiceinfo ) {
		Map<String,String> result = new HashMap<String,String>();
		if("platform".equals(otherInformation.getInfoType())){//修改平台信息需求任务2650报价或核保后还没有平台信息的手工补充平台信息保存接口
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("bwCommercialClaimTimes", otherInformation.getCommercialtimes());
			jsonObject.put("bwCompulsoryClaimTimes", otherInformation.getCompulsorytimes());
			jsonObject.put("loyaltyReasons", otherInformation.getLoyaltyreasons());
			jsonObject.put("insCorpId", otherInformation.getPreinscode());
			jsonObject.put("policyStartTime", otherInformation.getCompulsorystartdate().substring(0, 10));
			jsonObject.put("policyEndTime", otherInformation.getCompulsoryenddate());
			JSONObject syjsonObject = new JSONObject();
			syjsonObject.put("insCorpId", otherInformation.getPreinscode());
			syjsonObject.put("policyStartTime", otherInformation.getBusinessstartdate().substring(0, 10));
			syjsonObject.put("policyEndTime", otherInformation.getBusinessenddate());
			rulePlatformQueryService.saveRulequeryotherinfo(otherInformation.getTaskid(), jsonObject);
			rulePlatformQueryService.saveAllPolicies(otherInformation.getTaskid(), syjsonObject, jsonObject);
			LogUtil.info("%s=taskid修改平台信息：%s", otherInformation.getTaskid(), jsonObject.toString());
			result.put("status", "success");
		}else{
			Date date = new Date();
			if(!StringUtils.isEmpty(otherInformation.getBusinessstartdate())){
				Date busdate = String2Date(otherInformation.getBusinessstartdate());
				if(date.after(busdate)){
					result.put("status", "fail");
					result.put("msg", "商业险起保日期应晚于当前日期！");
					return result;
				}
			}
			if(!StringUtils.isEmpty(otherInformation.getCompulsorystartdate())){
				Date strdate = String2Date(otherInformation.getCompulsorystartdate());
				if(date.after(strdate)){
					result.put("status", "fail");
					result.put("msg", "交强险起保日期应晚于当前日期！");
					return result;
				}
			}
			INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
			otherInformation.setOperator(loginUser.getUsercode().toString());
			String flag = insbSpecifydriverService.updateOtherInfo(otherInformation,insbInvoiceinfo);
			if("success".equals(flag)){
				result.put("status", "success");
			}else{
				result.put("status", "fail");
				result.put("msg", "修改失败！请稍后重试！");
			}
		}
		return result;
	}
	
	/**
	 * String转Date
	 * */
	public Date String2Date(String dateStr){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = dateStr.trim();
		if(dateString.length()>10){
			dateString = dateString.substring(0, 10);
		}
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	@RequestMapping(value = "editCarOwnerInfo", method = RequestMethod.POST)
	@ResponseBody
	public String editCarOwnerInfo(INSBPerson carowner,String taskid) {
		return insbPersonService.updateCarOwnerInfo(carowner,taskid);
	}

	/**
	 * 更新保存车辆信息和车型信息
	 */
	@RequestMapping(value = "savecarinfo", method = RequestMethod.POST)
	@ResponseBody
	public String savecarinfo(HttpSession session, @ModelAttribute CarinfoVO carinfo,INSBCarmodelinfo carmodelinfo){
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		carinfo.setOperator(loginUser.getUsercode().toString());
		carmodelinfo.setOperator(loginUser.getUsercode().toString());
		String flag = insbCarinfoService.editCarInfoAndModelInfo(carinfo,carmodelinfo);
		if("success".equals(flag)){
			return "success";
		}else{
			return "error";
		}
	}
	
	/**
	 * 更新保存车型信息
	 */
	@RequestMapping(value = "updatecarmodelinfo", method = RequestMethod.POST)
	@ResponseBody
	public String updateCarinfo(HttpSession session, CarModelInfoVO carModelInfoVO){
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		carModelInfoVO.setOperator(loginUser.getUsercode());
		String flag = insbCarmodelinfoService.updateCarModelInfoByInstanceid(carModelInfoVO);
		if("success".equals(flag)){
			return "success";
		}else{
			return "error";
		}
	}
	
	/**
	 * 修改保险配置信息
	 */
	@RequestMapping(value = "saveInsuranceConfig", method = RequestMethod.POST)
	public ModelAndView saveInsuranceConfig(HttpSession session, CarInsuranceConfigVo insConfigVo) {
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
        LogUtil.info(loginUser.getUsercode()+"修改保险配置：" + JsonUtil.serialize(insConfigVo));
        ModelAndView mav = new ModelAndView("cm/common/insuranceConfig");
        //判断该任务是否处于操作人修改处理任务中
        boolean dealflag = myTaskService.checkCloseTask(insConfigVo.getTaskInstanceId(), insConfigVo.getThisInscomcode(), loginUser.getUsercode());
        if(dealflag){//任务已关闭或者不属于该处理人处理
        	mav.addObject("changingflag", dealflag);
        	return mav;
        }
		//修改保险配置数据
		long start = System.currentTimeMillis();
		insConfigVo.setOperator(loginUser.getUsercode().toString());
		insbCarinfohisDao.updateInsureconfigsameaslastyear(insConfigVo.getTaskInstanceId(), insConfigVo.getThisInscomcode(), insConfigVo.getInsureconfigsameaslastyear());
        String flag = insbCarkindpriceService.editInsuranceConfig(insConfigVo);

		long editEnd = System.currentTimeMillis();
		LogUtil.info("start-editEnd:"+(editEnd-start));
		//返回数据页面局部刷新
		if("SUCCESS".equalsIgnoreCase(flag)){
			Map<String, Object> carInsTaskInfo = new HashMap<String, Object>();
			Map<String,Object> insConfigInfo = insbCarkindpriceService.getCarInsConfigByInscomcode(insConfigVo.getThisInscomcode(),insConfigVo.getTaskInstanceId());
			Map<String,Object> otherInfo = new HashMap<String, Object>();
			otherInfo.put("taskid", insConfigVo.getTaskInstanceId());
			Map<String,Object> carInfo = new HashMap<String, Object>();
			carInfo.put("inscomname", insConfigVo.getThisInscomname());
			carInfo.put("insureconfigsameaslastyear", insConfigVo.getInsureconfigsameaslastyear());
			List<Map<String,Object>> carInsTaskInfoList = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < insConfigVo.getInscomcodeList().size(); i++) {
				Map<String,Object> temp = new HashMap<String, Object>();
				temp.put("inscomcode", insConfigVo.getInscomcodeList().get(i));
				carInsTaskInfoList.add(temp);
			}
			carInsTaskInfo.put("otherInfo",otherInfo);
			carInsTaskInfo.put("inscomcode",insConfigVo.getThisInscomcode());
			carInsTaskInfo.put("insConfigInfo",insConfigInfo);
			carInsTaskInfo.put("carInfo",carInfo);
			mav.addObject("carInsTaskInfoList", carInsTaskInfoList);
			mav.addObject("carInsTaskInfo", carInsTaskInfo);
			mav.addObject("carInsTaskInfo_index", insConfigVo.getCarInsTaskInfoindex());

			INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
			quotetotalinfo.setTaskid(insConfigVo.getTaskInstanceId());
			quotetotalinfo = insbQuotetotalinfoService.queryOne(quotetotalinfo);
			if (quotetotalinfo != null) {
				mav.addObject("isRenewal", "1".equals(quotetotalinfo.getIsrenewal()));
			}

			long readEnd = System.currentTimeMillis();
			LogUtil.info("editEnd-readEnd:"+(readEnd-editEnd));
			return mav;
		}
		return null;
	}
	
	/**
	 * 特殊险别显示修改保险配置信息弹出框
	 */
	@RequestMapping(value = "showSpecialRiskkindcfg", method = RequestMethod.GET)
	public ModelAndView showSpecialRiskkindcfg(String mInstanceid, String inscomcode, String riskkindcode, String riskkindname
			, String inscomname, String inscomcodeList, String showEdit) {
		LogUtil.info("mInstanceid"+mInstanceid+"+inscomcode"+inscomcode+"+riskkindcode"+riskkindcode+"+showEdit"+showEdit
				+"+riskkindname"+riskkindname+"+inscomname"+inscomname+"+inscomcodeList"+inscomcodeList);
		ModelAndView mav = new ModelAndView("cm/common/specialRiskkindcfg");
		List<Map<String,Object>> specialRiskkindcfgList = insbCarkindpriceService.getSpecialRiskkindcfg(mInstanceid, inscomcode, riskkindcode);
		mav.addObject("specialRiskkindcfgList", specialRiskkindcfgList);
		mav.addObject("mInstanceid", mInstanceid);
		mav.addObject("inscomcode", inscomcode);
		mav.addObject("riskkindcode", riskkindcode);
		mav.addObject("riskkindname", riskkindname);
		mav.addObject("inscomname", inscomname);
		mav.addObject("inscomcodeList", inscomcodeList);
		mav.addObject("showEdit", showEdit);
		return mav;
	}
	
	/**
	 * 修改特殊险别配置信息
	 */
	@RequestMapping(value = "editSpecialRiskkindcfg", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> editSpecialRiskkindcfg(HttpSession session, SpecialRiskkindCfgVo specialRiskkindCfg) {
		LogUtil.info(specialRiskkindCfg.getmInstanceid()+"+"+specialRiskkindCfg.getInscomcode()
				+"+"+specialRiskkindCfg.getRiskkindcode()+"+"+specialRiskkindCfg.getSpcRiskkindCfg().size()
				+"+"+specialRiskkindCfg.getEditToAll()+"+"+specialRiskkindCfg.getInscomcodeList());
		for (SpecialRiskkindCfg temp:specialRiskkindCfg.getSpcRiskkindCfg()) {
			LogUtil.info(temp.getId()+"+"+temp.getCfgKey()+"+"+temp.getCfgValue());
		}
		Map<String, String> result = new HashMap<String, String>();
		try {
			INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
			specialRiskkindCfg.setOperator(loginUser.getUsercode());
			result = insbCarkindpriceService.editSpecialRiskkindcfg(specialRiskkindCfg);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "fail");
			result.put("msg", "修改险别配置信息失败！");
			return result;
		}
	}
	
	/**
	 * 刷新保险配置信息
	 */
	@RequestMapping(value = "refreshInsuranceConfig", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView refreshInsuranceConfig(String instanceId,String inscomcode,
			String inscomname,String inscomcodeList,String num) {
	    ModelAndView mav = new ModelAndView("cm/common/insuranceConfig");
		Map<String, Object> carInsTaskInfo = new HashMap<String, Object>();
		Map<String,Object> insConfigInfo = insbCarkindpriceService.getCarInsConfigByInscomcode(inscomcode,instanceId);
		Map<String,Object> otherInfo = new HashMap<String, Object>();
		otherInfo.put("taskid", instanceId);

		INSBCarinfohis carinfohis = new INSBCarinfohis();
		carinfohis.setTaskid(instanceId);
		carinfohis.setInscomcode(inscomcode);
		carinfohis = insbCarinfohisDao.selectOne(carinfohis);

		Map<String,Object> carInfo = new HashMap<String, Object>();
		carInfo.put("inscomname", inscomname);

		if (carinfohis != null) {
			carInfo.put("insureconfigsameaslastyear", carinfohis.getInsureconfigsameaslastyear());
		}

		List<Map<String,Object>> carInsTaskInfoList = new ArrayList<Map<String,Object>>();
		String[] comList = inscomcodeList.split(",");
		for (int i = 0; i < comList.length; i++) {
			Map<String,Object> temp = new HashMap<String, Object>();
			temp.put("inscomcode", comList[i]);
			carInsTaskInfoList.add(temp);
		}
		carInsTaskInfo.put("otherInfo",otherInfo);
		carInsTaskInfo.put("inscomcode",inscomcode);
		carInsTaskInfo.put("insConfigInfo",insConfigInfo);
		carInsTaskInfo.put("carInfo",carInfo);
		mav.addObject("carInsTaskInfoList", carInsTaskInfoList);
		mav.addObject("carInsTaskInfo", carInsTaskInfo);
		mav.addObject("carInsTaskInfo_index", num);
		return mav;
	}
	
	/**
	 * 修改保存关系人信息
	 */
	@RequestMapping(value = "editRelationPersonInfo", method = RequestMethod.POST)
	@ResponseBody
	public String editRelationPersonInfo(HttpSession session, @ModelAttribute INSBRelationPersonVO relationPersonVO){
		LogUtil.info("修改保存关系人信息,taskId="+relationPersonVO.getTaskid());
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		relationPersonVO.setOperator(loginUser.getUsercode().toString());
		String flag = insbPersonService.editRelationPersonInfo(relationPersonVO);
		if("success".equals(flag)){
			return "success";
		}else{
			return "error";
		}
	}
	
	//得到重选车型信息
	@RequestMapping(value = "showCarModelInfo", method = RequestMethod.GET)
	@ResponseBody
	public String showCarTaskManageList(@ModelAttribute PagingParams pagingParams, String standardfullname) throws ControllerException{
//		Map<String, Object> paramMap = BeanUtils.toMap(pagingParams);
//		paramMap.put("standardfullname", standardfullname);
	    return insbCarmodelinfoService.reselectCarModelInfo(pagingParams, standardfullname);
	}
	
	/**
	 * 添加影像信息弹出框
	 * @param subInstanceId
	 * @param taskid
	 */
	@RequestMapping(value = "addImageDialog", method = RequestMethod.GET)
	public ModelAndView addImageDialog(@RequestParam String subInstanceId,@RequestParam String taskid) {
		List<INSCCode> inscCodeList = inscCodeService.queryINSCCodeByCode("insuranceimage", "");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("inscCodeList", inscCodeList);
		map.put("subInstanceId", subInstanceId);
		map.put("taskid", taskid);
		
		ModelAndView mav = new ModelAndView("cm/common/imageAddDialog");
		mav.addObject("data", map);
		return mav;

	}
	
	/**
	 * 上传影像
	 * @param file 文件
	 * @param fileType 文件类型
	 * @param fileDescribes 文件描述
	 * @param processinstanceid 实例id
	 * @return
	 */
	@RequestMapping(value = "addImage", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String addImage(HttpSession session, @RequestParam("file") MultipartFile[] file,
			@RequestParam("fileType") String fileType,@RequestParam("fileDescribes") String fileDescribes,
			@RequestParam("processinstanceid") String processinstanceid,@RequestParam("taskid") String taskid) {
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		List<Map<String,Object>> listmap = new ArrayList<Map<String,Object>>(){};
		for(int i=0;i<file.length;i++){
			MultipartFile filex = file[i];
			Map<String,Object> map = insbManualPriceService.fileUpLoad(request, filex, fileType, fileDescribes, loginUser.getUsercode(),processinstanceid,taskid);
			listmap.add(map);
		}
		if(listmap.size()==0){
			listmap.add(new HashMap<String,Object>(){{put("status", "fail");put("msg", "上传失败");}});
		}
		String resultStr = JSON.toJSONString(listmap); //JSONObject.fromObject(listmap).toString();
		return resultStr;
	}

	/**
	 * 图片展示
	 * @param subInstanceId 子流程实例id
	 * @param taskid 主流程实例id
	 * @param pictureId 图片id
	 * @return
	 */
	@RequestMapping(value = "previewPicture", method = RequestMethod.GET)
	public ModelAndView previewPicture(@RequestParam String subInstanceId,@RequestParam String taskid,@RequestParam String pictureId) {
		ModelAndView mav = new ModelAndView("cm/common/previewPicturel");
		//List<INSBFilelibrary> imageList = insbFilelibraryService.queryByFilebusinessCode(StringUtil.isEmpty(subInstanceId)?taskid:subInstanceId);
		List<INSBFilelibrary> imageList = getImgList(subInstanceId, taskid);
		for (int i = 0; i < imageList.size(); i++) {
			if(pictureId.equals(imageList.get(i).getId())){
				mav.addObject("index", i);
				break;
			}
		}
		mav.addObject("subInstanceId", subInstanceId);
		mav.addObject("imageList", imageList);
		mav.addObject("taskid", taskid);
		return mav;

	}

	/**
	 * 根据代理人找到上传的图片
	 * @param jobnum 代理人工号
	 * @param types 图片类型
	 * @return
	 */
	@RequestMapping(value = "agentview", method = RequestMethod.GET)
	public ModelAndView agentviewPicture(@RequestParam String jobnum,@RequestParam String types) {
		ModelAndView mav = new ModelAndView("cm/common/agentview");
		INSBCertification cer = new INSBCertification();
		INSBAgent agent = insbAgentservice.getAgent(jobnum);

		List<INSBFilelibrary> imageList = new ArrayList<INSBFilelibrary>();
		if(agent == null) {
			mav.addObject("imageList", imageList);
			return mav;
		}

		cer.setAgentnum(agent.getTempjobnum());
		cer = insbCertificationService.queryOne(cer);
		String idz="";
		String idf="";
		String bankid="";
		String quz="";
		String quf="";
		if(cer!=null){
			idz = cer.getIdcardpositive();//身份证正面 idcardpositive
			idf = cer.getIdcardopposite();//身份证背面 idcardopposite
			bankid = cer.getBankcardpositive();//银行卡正面照文件id bankcardpositive
			quz = cer.getQualificationpositive();//资格证照片页文件id qualificationpositive
			quf = cer.getQualificationpage();//资格证信息页文件id qualificationpage
		}

		INSBFilelibrary idzfile = insbFilelibraryService.queryById(idz);
		INSBFilelibrary idffile = insbFilelibraryService.queryById(idf);
		INSBFilelibrary bankidfile = insbFilelibraryService.queryById(bankid);


		if(types.equals("ids")){
			imageList.add(idzfile);
			imageList.add(idffile);
			mav.addObject("index", 1);
		}else if(types.equals("bank")){
			imageList.add(bankidfile);
			mav.addObject("index", 0);
		}else if(types.equals("qus")){
			if(org.springframework.util.StringUtils.hasLength(quz)) {
				INSBFilelibrary quzfile = insbFilelibraryService.queryById(quz);
				imageList.add(quzfile);
			}
			if(org.springframework.util.StringUtils.hasLength(quf)) {
				INSBFilelibrary quffile = insbFilelibraryService.queryById(quf);
				imageList.add(quffile);
			}
			mav.addObject("index", 1);
		}
		mav.addObject("imageList", imageList);
		return mav;

	}
	
	/**
	 * 图片删除
	 * @param imgid
	 * @param taskid
	 * @param subInstanceId
	 * @return
	 */
	@RequestMapping(value = "deleteimg", method = RequestMethod.POST)
	@ResponseBody
	public String deleteimg(String imgid,String taskid,String subInstanceId) {
		String[] ids = {imgid};
		LogUtil.info("删除图片：" + ids + " 任务号："+taskid+ " subInstanceId:"+subInstanceId);
		if(inscFilelibraryService.deleteFileLibraryData(ids)){
			List<INSBFilelibrary> imageList = getImgList(subInstanceId, taskid);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("taskid", taskid);
			map.put("subInstanceId", subInstanceId);
			map.put("imageList", imageList);
			return JSONObject.fromObject(map).toString();
		}else{
			return "faild";
		}
	}

	/**
	 * 校验图片类型是否已存在
	 * @param fileType
	 * @param taskid
	 * @param processinstanceid
	 * @return
	 */
	@RequestMapping(value = "checkImgTypeExist", method = RequestMethod.POST)
	@ResponseBody
	public String checkImgTypeExist(String fileType,String processinstanceid,String taskid) {
		List<INSBFilelibrary> imageList = getImgList(processinstanceid, taskid);
		for (INSBFilelibrary temp : imageList) {
			if(fileType.equals(temp.getFiletype())){
				return "faild";
			}
		}
		return "success";
	}
	
	/*
	 * 添加的方法，没有使用中
	 */
	@RequestMapping(value = "chooseProperty", method = RequestMethod.POST)
	@ResponseBody
	public String chooseProperty(String num) {
		System.out.println(num);
		
		Map<String, String> param2 = new HashMap<String, String>();
		param2.put("codetype", "institutiontype");
		List<INSCCode> list = inscCodeDao.selectINSCCodeByCode(param2);//车辆所属性质列表
			return JSONObject.fromObject(list).toString();
	}
	
	private List<INSBFilelibrary> getImgList(String subInstanceId, String taskid) {
		//查询影像信息
		List<INSBFilelibrary> imageList = insbFilelibraryService.queryByFilebusinessCode(taskid);
		List<INSBFilelibrary> imageListSub = insbFilelibraryService.queryByFilebusinessCode(subInstanceId);
		if(imageList!=null && imageList.size()>0){
			if(imageListSub!=null && imageListSub.size()>0){
				imageList.addAll(imageListSub);
			}
		}else{
			if(imageListSub!=null && imageListSub.size()>0){
				imageList = imageListSub;
			}else{
				imageList = new ArrayList<INSBFilelibrary>();
			}
		}
		return imageList;
	}
}
