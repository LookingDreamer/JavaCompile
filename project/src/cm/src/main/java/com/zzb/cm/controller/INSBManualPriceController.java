package com.zzb.cm.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.tool.Cm4WorkflowFilter;
import com.common.PagingParams;
import com.common.TaskConst;
import com.zzb.cm.entity.INSBFilelibrary;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBSupplement;
import com.zzb.cm.service.INSBCarinfoService;
import com.zzb.cm.service.INSBCarkindpriceService;
import com.zzb.cm.service.INSBCarmodelinfohisService;
import com.zzb.cm.service.INSBCommonWorkflowTrackservice;
import com.zzb.cm.service.INSBFilelibraryService;
import com.zzb.cm.service.INSBManualPriceService;
import com.zzb.cm.service.INSBOrderService;
import com.zzb.cm.service.INSBPersonService;
import com.zzb.cm.service.INSBPlatcarpriceService;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.cm.service.INSBQuotetotalinfoService;
import com.zzb.cm.service.INSBSupplementService;
import com.zzb.cm.service.INSBUserremarkService;
import com.zzb.conf.entity.INSBOperatorcomment;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBAutoconfigshowService;
import com.zzb.conf.service.INSBGroupmembersService;
import com.zzb.conf.service.INSBOperatorcommentService;
import com.zzb.conf.service.INSBPolicyitemService;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.conf.service.INSBWorkflowsubtrackService;
import com.zzb.mobile.model.lastinsured.CarModel;

import net.sf.json.JSONObject;

/**
 * CM系统 人工报价     
 */ 
@Controller
@RequestMapping("/business/manualprice/*")
public class INSBManualPriceController extends BaseController {
	@Resource
	private INSBCarinfoService insbCarinfoService;
	@Resource
	private INSBSupplementService insbSupplementService;
	@Resource
	private INSBPersonService insbPersonService;
	@Resource
	private INSBPlatcarpriceService insbPlatcarpriceService;
	@Resource
	private INSBCarkindpriceService insbCarkindpriceService;
	@Resource
	private INSBAgentService insbAgentService;
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
	private INSBOrderService insbOrderService;
	@Resource
	private INSBPolicyitemService insbPolicyitemService;
	@Resource
	private INSBQuoteinfoService insbQuoteinfoService;
	@Resource
	private INSBAutoconfigshowService  insbAutoconfigshowService;
	@Resource
	private INSBGroupmembersService  insbGroupmembersService;
	
	/**
	 * 人工规则报价页面跳转    加载初始数据
	 * @param taskid 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "manualpricelist", method = RequestMethod.GET)
	public ModelAndView manualpricelist(HttpSession session, String taskid, String taskcode) throws ControllerException {
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		ModelAndView mav = new ModelAndView("cm/manualprice/manualPrice");
        //精灵报价标识
        boolean elfquoteFlag = false;
//		taskid = "1";//测试使用
		Map<String, Object> agentInfo = insbAgentService.getCarTaskAgentInfo(taskid,"COMMON");//代理人信息
		//获取保险公司code列表（报价期间使用三个参数，非报价期间使用一个参数的重载方法）
		List<String> inscomcodeList = insbQuotetotalinfoService.getInscomcodeListByInstanceId(taskid, taskcode, loginUser.getUsercode());
		List<Map<String, Object>> carInsTaskInfoList = new ArrayList<Map<String,Object>>();//子任务信息列表
		for (int i = 0; i < inscomcodeList.size(); i++) {
			String inscomcode = inscomcodeList.get(i);
//			Map<String, Object> taskAllInfo = new HashMap<String, Object>();
//			Map<String, Object> carInfo = insbCarinfoService.getCarTaskCarInfo(taskid, inscomcode, "SHOW");//车辆信息
//			Map<String, Object> relationPersonInfo = insbPersonService.getCarTaskRelationPersonInfo(taskid, inscomcode , "SHOW");//关系人信息
//			Map<String, Object> otherInfo = insbCarinfoService.getCarTaskOtherInfo(taskid, inscomcode, "SHOW");//其他信息
			Map<String, Object> taskAllInfo = insbCarinfoService.getTaskAllInfo(taskid, inscomcode);
			Map<String,Object> insConfigInfo = insbCarkindpriceService.getCarInsConfigByInscomcode(inscomcode, taskid);//保险配置信息
			//得到子流程最新轨迹信息 ，得到主流程轨迹使用：insbWorkflowmaintrackService.getMainTrackByInscomcode(taskid)
			INSBWorkflowsubtrack workflowsubtrack = insbWorkflowsubtrackService.getSubTrackByInscomcode(taskid, inscomcode);
			//记录打开，标识任务已经在处理中
	        insbWorkflowsubtrackService.addTrackdetail(workflowsubtrack,loginUser.getUsercode());
			//用户备注信息，第二个参数（1主流程轨迹、 2子流程轨迹）
//			List<INSBUsercomment> usercomment = insbUsercommentService.getNearestUserComment(taskid, inscomcode, workflowsubtrack.getTaskcode());
			List<Map<String, Object>> usercomment = insbUsercommentService.getNearestUserCommentAndType(taskid, inscomcode);
			INSBUsercomment dqusercomment = insbUsercommentService.selectUserCommentByTrackid(workflowsubtrack.getId(), 2);
			//操作员备注信息，第二个参数（1主流程轨迹、 2子流程轨迹）弃用
//			List<INSBOperatorcomment> operatorcommentList = insbOperatorcommentService.selectOperatorCommentByTrackid(workflowsubtrack.getId(), 2);
			//查询给操作员的备注（新版使用）
			List<INSBOperatorcomment> operatorcommentList = insbOperatorcommentService.getOperatorCommentByMaininstanceid(taskid, inscomcode);
			//查询影像信息
			List<INSBFilelibrary> imageList = insbFilelibraryService.queryByFilebusinessCode(taskid);
//			List<INSBFilelibrary> imageListSub = insbFilelibraryService.queryByFilebusinessCode(workflowsubtrack.getInstanceid());
//			if(imageList!=null && imageList.size()>0){
//				if(imageListSub!=null && imageListSub.size()>0){
//					imageList.addAll(imageListSub);
//				}
//			}else{
//				if(imageListSub!=null && imageListSub.size()>0){
//					imageList = imageListSub;
//				}else{
//					imageList = new ArrayList<INSBFilelibrary>();
//				}
//			}
			Map<String, Object> remark = new HashMap<String, Object>();//备注信息
			if(usercomment!=null && usercomment.size()>0){
				remark.put("usercomment", usercomment.get(0));//当前节点之前的最新一条备注
			}
			remark.put("dqusercomment", dqusercomment);//当前节点的备注
			remark.put("opcommentList", operatorcommentList);
			//获取补充信息中车型上面部分信息
			Map<String, Object> replenishInfo = insbManualPriceService.getReplenishInfo(taskid);
			//平台车型信息
			CarModel platCarModel = insbManualPriceService.getPlatCarModelMessage(taskid);
			//获取补充信息中车型下面部分信息
			Map<String, Object> localdbReplenishInfo = insbManualPriceService.getLocaldbReplenishInfo(taskid, inscomcode, (String)agentInfo.get("deptCode"));
			taskAllInfo.put("subInstanceId",workflowsubtrack.getInstanceid());
			taskAllInfo.put("insConfigInfo",insConfigInfo);
			taskAllInfo.put("inscomcode", inscomcode);
//			taskAllInfo.put("carInfo", carInfo);
//			taskAllInfo.put("relationPersonInfo", relationPersonInfo);
//			taskAllInfo.put("otherInfo", otherInfo);
			taskAllInfo.put("imageList", imageList);
			taskAllInfo.put("remarkinfo", remark);
			taskAllInfo.put("replenishInfo", replenishInfo);
			taskAllInfo.put("platCarModel", platCarModel);
			taskAllInfo.put("localdbReplenishInfo", localdbReplenishInfo);
			// 添加投保单号信息
//			Map<String, Object> proposalInfo =  policyitemService.getPolicyNumInfo(taskid);
			Map<String, Object> proposalInfo = insbPolicyitemService.getPolicyNumInfo2(taskid,inscomcode);
			//mav.addObject("proposalInfo", proposalInfo);
			taskAllInfo.put("proposalInfo", proposalInfo);
			/**
			 * hxx  判断 是否具有 核保能力
			 */
			INSBQuoteinfo quoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(taskid, inscomcode);
			String deptid =quoteinfo.getDeptcode();//出单网点
			Map<String, Object> deptmap = new HashMap<String, Object>();
			deptmap.put("providerid", inscomcode);
//			deptmap.put("deptid", deptid);
			deptmap.put("conftype", "02");
			
			List<String> tempDeptIds = insbAutoconfigshowService.getParentDeptIds4Show(deptid);
			deptmap.put("deptList", tempDeptIds);
			
			List<String> quotetypes = insbAutoconfigshowService.queryByProId(deptmap);
			if(!quotetypes.isEmpty()&&(quotetypes.contains("02")||quotetypes.contains("01"))){//包含精灵或EDI 核保能力
				taskAllInfo.put("isRequote", "1");
			}else{
				taskAllInfo.put("isRequote", "0");
			}
			List<INSBSupplement> allsup = insbSupplementService.getSupplementsBytaskid(taskid,inscomcode);
			List<INSBSupplement> suplist1= new ArrayList<INSBSupplement>();
			List<INSBSupplement> suplist2= new ArrayList<INSBSupplement>();
			for(int j = 0; j < allsup.size(); j++){
				if(j%2==0){
					suplist1.add(allsup.get(j));
				}else{
					suplist2.add(allsup.get(j));
				}
			}
			taskAllInfo.put("supplements1",suplist1);//补充项集合1
			taskAllInfo.put("supplements2",suplist2);//补充项集合1
//			List<INSBWorkflowsubtrack> quoteList = insbOrderService.getQuoteBackTrack(workflowsubtrack.getInstanceid());
//			List<String> quoteList = insbOrderService.getQuoteBackTrackStr(workflowsubtrack.getInstanceid());
//			if(quoteList!=null && quoteList.size()>0){
				//存在精灵，edi报价的能力  
//				taskAllInfo.put("isRequote", "1");
//				taskAllInfo.put("isRequote", Integer.valueOf(quoteList.size()).toString());
//			}else{
//				mav.addObject("isRequote", "0");
//				taskAllInfo.put("isRequote", "0");
//			}
            //判断是否有精灵报价
            deptmap.put("conftype", "01");
            quotetypes = insbAutoconfigshowService.queryByProId(deptmap);
            if(!quotetypes.isEmpty()&&quotetypes.contains("02")){
                elfquoteFlag = true;
            }
			carInsTaskInfoList.add(taskAllInfo);
			
		}
		
		//获取平台规则查询的数据信息
		Map<String, Object> rulequery = insbCarinfoService.getCarInfoByTaskId(taskid); 
		if(null!=rulequery.get("result")){
			mav.addObject("rulequeryresult","规则平台查询失败");
		}
		mav.addObject("rulequery",rulequery);
		mav.addObject("showEditFlag","1");//是否显示修改功能（0不显示，1显示）
		mav.addObject("agentInfo",agentInfo);
		mav.addObject("carInsTaskInfoList",carInsTaskInfoList);
		mav.addObject("taskid", taskid);
		mav.addObject("taskcode", taskcode);
		mav.addObject("inscomcode", inscomcodeList.get(0));//先只一个
		mav.addObject("inscomcodes", insbQuotetotalinfoService.getInscomcodeAndNameListByInstanceId(taskid,taskcode,loginUser.getUsercode()));
		//查询主流程轨迹
		List<Map<String, Object>> workflowinfoList=insbCommonWorkflowTrackservice.getWorkFlowTrack(taskid);
		mav.addObject("workflowinfoList",workflowinfoList);
		//查询是否费改地区
//		boolean isfeeflag=insbOrderService.isFeeflagArea(taskid, null);
//		if(isfeeflag==true){
		mav.addObject("isfeeflag","1");//不用判断是否为费改地区，统一都是费改处理
//		}else{
//			mav.addObject("isfeeflag","0");			
//		}
        mav.addObject("elfquoteFlag",elfquoteFlag?"1":"0");
		return mav;
	}
	
	/**
	 * 获取补充信息中车型以上部分信息
	 */
	@RequestMapping(value = "getReplenishInfo", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getReplenishInfo(String instanceId, String inscomcode) {
		return JSONObject.fromObject(insbManualPriceService.getReplenishInfo(instanceId)).toString();
	}
	
	/**
	 * 修改补充信息弹出框,获取补充信息下面部分
	 */
	@RequestMapping(value = "modifyaddmessage", method = RequestMethod.GET)
	public ModelAndView additionalinformation(String instanceId, String inscomcode, String deptCode, String num,HttpSession session) {
		ModelAndView mav = new ModelAndView("cm/manualprice/editAddMessage");
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		List<Map<String,String>> inscoms = insbQuotetotalinfoService.getInscomcodeAndNameListByInstanceId(instanceId,TaskConst.QUOTING_7,loginUser.getUsercode());
		List<String> inscodes = new ArrayList<String>();
		for(Map<String,String> inscmo:inscoms){
			inscodes.add(inscmo.get("inscomcode"));
		}
		List<INSBSupplement> replenishSelectItemsInfo = insbSupplementService.getSupplementsBytaskid(instanceId,inscodes);
		List<INSBSupplement> thisInscomeitems = insbSupplementService.getSupplementsBytaskid(instanceId,inscomcode);//查询当前这家保险公司的规则试算因子，修改修改页面时候的显示值
		for(INSBSupplement inscmoitem:thisInscomeitems){
			for(INSBSupplement supplement:replenishSelectItemsInfo){
				if(inscmoitem.getKeyid().equals(supplement.getKeyid())){
					supplement.setMetadataValueKey(inscmoitem.getMetadataValueKey());
					break;
				}
			}
		}
		mav.addObject("replenishSelectItemsInfo", replenishSelectItemsInfo);
		mav.addObject("instanceId", instanceId);
		mav.addObject("inscomcodes", inscoms);//人工规则报价
		return mav;

	}
	
	/**
	 * 修改补充信息
	 */
	@RequestMapping(value = "editlocaldbreplenishinfo", method = RequestMethod.POST)
	@ResponseBody
	public String editLocaldbReplenishInfo(HttpSession session,HttpServletRequest req) {
		String count = req.getParameter("paramcount");
		String instanceId = req.getParameter("instanceId");
		String inscoms = req.getParameter("inscoms");
		if(StringUtil.isEmpty(count)){
			return "fail";
		}
		List<INSBSupplement> supplements = new ArrayList<INSBSupplement>();
		String temp;INSBSupplement sup;
		for(int i=0 ; i<Integer.valueOf(count); i++){
			temp = req.getParameter("metadataValue"+i);
			sup = new INSBSupplement();
			sup.setTaskid(instanceId);
			sup.setKeyid(temp.split("@")[1]);
			sup.setMetadataValueKey(temp.split("@")[0]);
			sup.setOptime(new Date());
			sup.setOpuser(((INSCUser) session.getAttribute("insc_user")).getUsercode());
			supplements.add(sup); 
		}
		return insbManualPriceService.editLocaldbSupplementInfo(inscoms, supplements);
		
	}

	/**
	 * 获得平台车价信息
	 * @param pagingParams taskid inscomcode
	 */
	@RequestMapping(value = "getplatcarmessage", method = RequestMethod.GET)
	@ResponseBody
	public String getPlatCarMessage(@ModelAttribute PagingParams pagingParams,String taskid,String inscomcode) {
		Map<String, Object> paramMap = BeanUtils.toMap(pagingParams);
		paramMap.put("taskid", taskid);
		paramMap.put("companyid", inscomcode);
		return insbPlatcarpriceService.getPlatCarPriceInfoJSONByMap(paramMap);
	}
	
	/**
	 * 报价通过、退回修改、转人工处理按钮调用接口，传入报价结果参数标记不同
	 * 转人工处理确认为跳转到获取报价途径
	 * 报价结果：1获取报价途径，2报价退回，3选择保价，4终止报价
	 */
	@RequestMapping(value = "quotePricePassOrBackForEdit", method = RequestMethod.POST, produces="text/html;charset=UTF-8")
	@ResponseBody
	@Cm4WorkflowFilter(taskType="baojia")
	public String quotePricePassOrBackForEdit(HttpSession session, @RequestBody Map<String, Object> params) {
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		String instanceId = (String) params.get("instanceId");
		String inscomtex = (String) params.get("inscomtex");
		String quoteResult = (String) params.get("quoteResult");
		return insbManualPriceService.quoteTotailPricePassOrBackForEdit(instanceId, loginUser.getUsercode(), quoteResult,inscomtex);
	}
	
	/**
	 * 退回修改按钮调用接口和报价通过按钮调用相同接口
	 */
//	@RequestMapping(value = "quotePriceBackForEdit", method = RequestMethod.POST)
//	@ResponseBody
//	public String quotePriceBackForEdit(HttpSession session, @RequestBody Map<String, Object> params) {
//		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
//		String instanceId = (String) params.get("instanceId");
//		String inscomcode = (String) params.get("inscomcode");
//		String quoteResult = (String) params.get("quoteResult");
////		return insbManualPriceService.quotePriceBackForEdit(instanceId, inscomcode,loginUser.getUsercode());
//		return insbManualPriceService.quotePricePassOrBackForEdit(instanceId, inscomcode,loginUser.getUsercode(), quoteResult);
//	}
	
	/**
	 * 转人工处理按钮调用接口和报价通过、退回修改按钮调用相同接口
	 */
//	@RequestMapping(value = "quotePriceToManual", method = RequestMethod.POST)
//	@ResponseBody
//	public String quotePriceToManual(@RequestBody Map<String, Object> params) {
//		String instanceId = (String) params.get("instanceId");
//		String inscomcode = (String) params.get("inscomcode");
//		return insbManualPriceService.quotePriceToManual(instanceId, inscomcode);
//	}
	
	/**
	 * 拒绝承保按钮调用接口
	 */
	@RequestMapping(value = "refuseUnderwrite", method = RequestMethod.POST)
	@ResponseBody
	public String refuseUnderwrite(HttpSession session, @RequestBody Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String maininstanceId = (String) params.get("maininstanceId");//主流程id
			String subinstanceId = (String) params.get("subinstanceId");//子流程id
			String inscomcode = (String) params.get("inscomcode");//保险公司code
			String mainorsub = (String) params.get("mainorsub");//主或子流程
            INSCUser operator = (INSCUser) session.getAttribute("insc_user");
			return insbManualPriceService.refuseUnderwrite(maininstanceId, subinstanceId, inscomcode, mainorsub, "back", operator.getUsercode());
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", "fail");
			resultMap.put("msg", "系统错误！");
			return JSONObject.fromObject(resultMap).toString();
		}
	}
	
	/**
	 * 拒绝承保按钮调用接口
	 */
	@RequestMapping(value = "quoteRefuseUnderwrite", method = RequestMethod.POST)
	@ResponseBody
	public String quoteRefuseUnderwrite(HttpSession session, @RequestBody Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String maininstanceId = (String) params.get("maininstanceId");//主流程id
			String subinstanceId = (String) params.get("subinstanceId");//子流程id
			String inscomcode = (String) params.get("inscomcode");//保险公司code
			String mainorsub = (String) params.get("mainorsub");//主或子流程
            INSCUser operator = (INSCUser) session.getAttribute("insc_user");
			return insbManualPriceService.quoteRefuseUnderwrite(maininstanceId, subinstanceId, inscomcode, mainorsub, "back", operator.getUsercode());
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", "fail");
			resultMap.put("msg", "系统错误！");
			return JSONObject.fromObject(resultMap).toString();
		}
	}
	
	/**
	 * 打回任务按钮调用接口
	 * instanceType 1:主 2：子
	 */
	@RequestMapping(value = "releaseTask", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String releaseTask(HttpSession session, @RequestBody Map<String, Object> params) {
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		String instanceId = (String) params.get("instanceId");
		String inscomcode = (String) params.get("inscomcode");
		String statu = (String) params.get("statu");
		int instanceType = Integer.parseInt((String)params.get("instanceType"));
		//查询用户是否有二次支付流程的执行权限，如果没有则直接返回无权限，不继续以下操作
		if (TaskConst.PAYINGSUCCESS_SECOND_21.equals(statu)&&!insbGroupmembersService.queryUserGroupPrivileges(loginUser.getId(), "a")) {
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("status", "noright");
			resultMap.put("msg", "打回任务失败,没有执行的权限！");
			return JSONObject.fromObject(resultMap).toString();
		} else {
			return insbManualPriceService.releaseTask(instanceId, inscomcode, instanceType, loginUser);
		}
		
	}
	
	/**
	 * 报价打回任务按钮调用接口
	 * instanceType 1:主 2：子
	 */
	@RequestMapping(value = "quoteReleaseTask", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String quoteReleaseTask(HttpSession session, @RequestBody Map<String, Object> params) {
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		String instanceId = (String) params.get("instanceId");
		String inscomcode = (String) params.get("inscomcode");
		int instanceType = Integer.parseInt((String)params.get("instanceType"));
		return insbManualPriceService.quoteReleaseTask(instanceId, inscomcode, instanceType, loginUser);
	}
	
	/**
	 * 规则试算调用接口
	 */
	@RequestMapping(value = "ruleCalculation", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String ruleCalculation(@RequestParam String instanceId, @RequestParam String subInstanceId, 
			@RequestParam String inscomcode, @RequestParam boolean priceLimitFlag) {
		return insbManualPriceService.ruleCalculation(instanceId, subInstanceId, inscomcode, priceLimitFlag);
	}
	
	
	/**
	 * 关闭二次支付任务
	 * 
	 * @param instanceId
	 * @return
	 */
	@RequestMapping(value="endPayTask",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> endPayTask(HttpSession session, String instanceId){
		LogUtil.info("关闭二次支付action---start---instanceId="+instanceId);
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		//查询用户是否有二次支付流程的执行权限，如果没有则直接返回无权限，不继续以下操作
		if(!insbGroupmembersService.queryUserGroupPrivileges(loginUser.getId(), "a")){
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("status", "noright");
			resultMap.put("msg", "工作流调用失败，没有执行的权限！");
			return resultMap;
		}else{
			return insbManualPriceService.endPayTask(instanceId, loginUser.getUsercode());
		}
	}

}
