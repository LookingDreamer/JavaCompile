package com.zzb.cm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.system.entity.INSCUser;
import com.zzb.cm.service.INSBCarinfoService;
import com.zzb.cm.service.INSBCarkindpriceService;
import com.zzb.cm.service.INSBCommonWorkflowTrackservice;
import com.zzb.cm.service.INSBOrderService;
import com.zzb.cm.service.INSBPersonService;
import com.zzb.cm.service.INSBQuotetotalinfoService;
import com.zzb.conf.entity.INSBOperatorcomment;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.entity.INSBWorkflowmaintrack;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBOperatorcommentService;
import com.zzb.conf.service.INSBPolicyitemService;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.conf.service.INSBWorkflowmaintrackService;
import com.zzb.conf.service.INSBWorkflowsubtrackService;
/**
 * 人工调整
 */
@Controller
@RequestMapping("/business/manualadjustment/*")
public class INSBBusinessManualAdjustmentController extends BaseController {
	@Resource
	private INSBCarinfoService insbCarinfoService;
	@Resource
	private INSBPersonService insbPersonService;
	@Resource
	private INSBCarkindpriceService insbCarkindpriceService;
	@Resource
	private INSBAgentService insbAgentService;
	@Resource
	private INSBQuotetotalinfoService insbQuotetotalinfoService;
	@Resource
	private INSBCommonWorkflowTrackservice insbCommonWorkflowTrackservice;
	@Resource
	private INSBWorkflowmaintrackService insbWorkflowmaintrackService;
	@Resource
	private INSBUsercommentService insbUsercommentService;
	@Resource
	private INSBPolicyitemService policyitemService;
	@Resource
	private INSBWorkflowsubtrackService insbWorkflowsubtrackService;
	@Resource
	private INSBOrderService insbOrderService;
	@Resource
	private INSBOperatorcommentService insbOperatorcommentService;
	//跳转人工调整页面
	@RequestMapping(value="showmanualadjustment", method=RequestMethod.GET)
	public ModelAndView showmanualadjustment(HttpSession session,String taskid,String taskcode){
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		ModelAndView mav = new ModelAndView("cm/manualadjustment/Adjustment");
		//返回页面数据
		Map<String, Object> agentInfo = insbAgentService.getCarTaskAgentInfo(taskid,"COMMON");
		List<String> inscomcodeList = insbQuotetotalinfoService.getInscomcodeListByInstanceId(taskid, taskcode, loginUser.getUsercode());
//		List<String> inscomcodeList = insbQuotetotalinfoService.getInscomcodeListByInstanceId(taskid);
		List<Map<String, Object>> carInsTaskInfoList = new ArrayList<Map<String,Object>>();
		// 添加投保单号信息
		Map<String, Object> proposalInfo =  policyitemService.getPolicyNumInfo(taskid);
		for (int i = 0; i < inscomcodeList.size(); i++) {
			String inscomcode = inscomcodeList.get(i);
			// 添加支付信息
			Map<String, Object> paymentinfo = insbOrderService.getPaymentinfo(taskid,inscomcode);
			Map<String, Object> temp = new HashMap<String, Object>();
			Map<String, Object> carInfo = insbCarinfoService.getCarTaskCarInfo(taskid, inscomcode, "SHOW");
			Map<String, Object> relationPersonInfo = insbPersonService.getCarTaskRelationPersonInfo(taskid, inscomcode , "SHOW");
			Map<String, Object> otherInfo = insbCarinfoService.getCarTaskOtherInfo(taskid, inscomcode, "SHOW");
			Map<String,Object> insConfigInfo = insbCarkindpriceService.getCarInsConfigByInscomcode(inscomcode, taskid);
			
			INSBWorkflowsubtrack workflowsubtrack = insbWorkflowsubtrackService.getSubTrackByInscomcode(taskid, inscomcode);
			//记录打开，标识任务已经在处理中
	        insbWorkflowsubtrackService.addTrackdetail(workflowsubtrack,loginUser.getUsercode());
			
			//用户备注信息，第二个参数（1主流程轨迹、 2子流程轨迹）
//			List<INSBUsercomment> usercomment = insbUsercommentService.getNearestUserComment(taskid, inscomcode, workflowsubtrack.getTaskcode());
			List<Map<String, Object>> usercomment = insbUsercommentService.getNearestUserCommentAndType(taskid, inscomcode);
			INSBUsercomment dqusercomment = insbUsercommentService.selectUserCommentByTrackid(workflowsubtrack.getId(), 2);
			
			List<INSBOperatorcomment> operatorcommentList = insbOperatorcommentService.getOperatorCommentByMaininstanceid(taskid, inscomcode);
			Map<String,Object>remark=new HashMap<String, Object>();
			if(usercomment!=null && usercomment.size()>0){
				remark.put("usercomment", usercomment.get(0));//当前节点之前的最新一条备注
			}
			remark.put("opcommentList", operatorcommentList);
			remark.put("dqusercomment", dqusercomment);//当前节点的备注
			temp.put("insConfigInfo",insConfigInfo);
			temp.put("inscomcode", inscomcode);
			temp.put("carInfo", carInfo);
			temp.put("relationPersonInfo", relationPersonInfo);
			temp.put("otherInfo", otherInfo);
			temp.put("remarkinfo", remark);
			carInsTaskInfoList.add(temp);
			mav.addObject("paymentinfo",paymentinfo);
		}
		mav.addObject("showEditFlag","1");//是否显示修改功能（0不显示，1显示）
		mav.addObject("agentInfo",agentInfo);
		mav.addObject("carInsTaskInfoList",carInsTaskInfoList);
		mav.addObject("proposalInfo",proposalInfo);
		//查询主流程轨迹
		List<Map<String, Object>> workflowinfoList=insbCommonWorkflowTrackservice.getWorkFlowTrack(taskid);
//		for (Map<String, Object> map : workflowinfoList) {
//			//信息录入
//			if("1".equals(map.get("taskcode"))){
//				String instanceid = (String)map.get("instanceid");
//				Map<String,Object>  AgentQuotetotal=insbQuotetotalinfoService.getAgentInfoByTaskId(instanceid);
//				map.putAll(AgentQuotetotal);
//			}else if("2".equals(map.get("taskcode"))){
//				map.put("QuoteInfo",insbWorkflowsubtrackService.getQuoteInfo(taskid));
//			}else{
//				Map<String,Object> userInfo=insbWorkflowmaintrackService.getUserInfo(taskid);
//				map.putAll(userInfo);
//			}
//		}
		mav.addObject("workflowinfoList",workflowinfoList);	
		return mav;
	}
	//人工调整页面投保单号修改
		@RequestMapping(value="showOrEditInsureRecordNumber", method=RequestMethod.GET)
		public ModelAndView showOrEditInsureRecordNumber(String taskid,String inscomcode){
			ModelAndView mav = new ModelAndView("cm/manualadjustment/editInsureRecordNumber");
			// 添加投保单号信息
			Map<String, Object> proposalInfo =  policyitemService.getPolicyNumInfo(taskid);
			// 添加支付信息
			Map<String, Object> paymentinfo = insbOrderService.getPaymentinfo(taskid,inscomcode);
			mav.addObject("proposalInfo",proposalInfo);
			mav.addObject("paymentinfo",paymentinfo);
			mav.addObject("taskid",taskid);
			return mav;
		}
	//人工调整页面投保单号保存修改信息

//		@RequestMapping(value = "editInsureRecordNumber", method = RequestMethod.POST)
//		@ResponseBody
//		public String editOtherInfo(HttpSession session, @ModelAttribute InsRecordNumberVO insRecordNumber) {
//			System.out.println("---------taskid-----"+insRecordNumber.getTaskid());
//			INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
//			insRecordNumber.setOperator(loginUser.getUsercode().toString());
//			String flag = insbPolicyitemService.editPolicyNumInfo(insRecordNumber);
//			
//			if("success".equals(flag)){
//				return "success";
//			}else{
//				return "error";
//			}
//		}
	//跳转已完成流程页面
	@RequestMapping(value="showfinishprocess", method=RequestMethod.GET)
	public ModelAndView showFinishProcess(String taskid,String inscomcode){
		ModelAndView mav = new ModelAndView("cm/manualadjustment/finishProcess");
		//返回页面数据
		//inscomcode瞎的
		Map<String, Object> agentInfo = insbAgentService.getCarTaskAgentInfo(taskid,"COMMON");
		List<String> inscomcodeList = insbQuotetotalinfoService.getInscomcodeListByInstanceId(taskid,inscomcode);
		List<Map<String, Object>> carInsTaskInfoList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < inscomcodeList.size(); i++) {
			//String inscomcode = inscomcodeList.get(i);
			Map<String, Object> temp = new HashMap<String, Object>();
			Map<String, Object> carInfo = insbCarinfoService.getCarTaskCarInfo(taskid, inscomcode, "SHOW");
			Map<String, Object> relationPersonInfo = insbPersonService.getCarTaskRelationPersonInfo(taskid, inscomcode , "SHOW");
			Map<String, Object> otherInfo = insbCarinfoService.getCarTaskOtherInfo(taskid, inscomcode, "SHOW");
			Map<String,Object> insConfigInfo = insbCarkindpriceService.getCarInsConfigByInscomcode(inscomcode, taskid);
			INSBWorkflowmaintrack insbWorkflowmaintrack=insbWorkflowmaintrackService.getMainTrackByInscomcode(taskid);
			INSBUsercomment insbUsercomment=insbUsercommentService.selectUserCommentByTrackid(insbWorkflowmaintrack.getId(),1);
			List<INSBOperatorcomment> operatorcommentList=insbOperatorcommentService.selectOperatorCommentByTrackid(insbWorkflowmaintrack.getId(), 1);
			Map<String,Object>remark=new HashMap<String, Object>();
			remark.put("usercomment",insbUsercomment);
			remark.put("opcommentList", operatorcommentList);
			temp.put("insConfigInfo",insConfigInfo);
			temp.put("inscomcode", inscomcode);
			temp.put("carInfo", carInfo);
			temp.put("relationPersonInfo", relationPersonInfo);
			temp.put("otherInfo", otherInfo);
			temp.put("remarkinfo", remark);
			carInsTaskInfoList.add(temp);
			
		}
		//查询主流程轨迹
		List<Map<String, Object>> workflowinfoList=insbCommonWorkflowTrackservice.getWorkFlowTrack(taskid);
		mav.addObject("workflowinfoList",workflowinfoList);	
		mav.addObject("agentInfo",agentInfo);
		mav.addObject("carInsTaskInfoList",carInsTaskInfoList);
		return mav;
	}
	/**
	 *人工调整页面按钮调整成功按钮
	 * @param instanceid
	 * @return 
	 */
	@RequestMapping(value="finashAdjust", method=RequestMethod.POST)
	@ResponseBody
	public String finashAdjust(@RequestParam String instanceid,@RequestParam String result){
		System.out.println("-----------------"+instanceid);
		String servicestr=insbQuotetotalinfoService.adjustcomplete(instanceid,result);
		if(servicestr!=null || "true".equals(servicestr)){
			return "true";
		}else{
			return "false";
		}
	}
}
