package com.zzb.cm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.zzb.cm.entity.INSBQuotetotalinfo;

import com.zzb.cm.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCUser;
import com.common.TaskConst;
import com.zzb.cm.controller.vo.InsRecordNumberVO;
import com.zzb.cm.entity.INSBFilelibrary;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.conf.entity.INSBOperatorcomment;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBAutoconfigshowService;
import com.zzb.conf.service.INSBOperatorcommentService;
import com.zzb.conf.service.INSBPolicyitemService;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.conf.service.INSBWorkflowsubtrackService;

/**
 * 人工录单 
 */
@Controller
@RequestMapping("/business/manualrecord/*")
public class INSBBusinessManualRecordController extends BaseController {
	@Resource
	private INSBCarinfoService insbCarinfoService;
	@Resource
	private INSBPersonService insbPersonService;
	@Resource
	private INSBPolicyitemService insbPolicyitemService;
	@Resource
	private INSBCarkindpriceService insbCarkindpriceService;
	@Resource
	private INSHCarkindpriceService inshCarkindpriceService;
	@Resource
	private INSBAgentService insbAgentService;
	@Resource
	private INSBQuotetotalinfoService insbQuotetotalinfoService;
	@Resource
	private INSBUsercommentService insbUsercommentService;
	@Resource
	private INSBOperatorcommentService insbOperatorcommentService;
	@Resource
	private INSBOrderService insbOrderService;
	@Resource
	private INSBCommonWorkflowTrackservice insbCommonWorkflowTrackservice;
	@Resource
	private INSBFilelibraryService insbFilelibraryService;
	@Resource
	private INSBWorkflowsubtrackService insbWorkflowsubtrackService;
	@Resource
	private INSBQuoteinfoService insbQuoteinfoService;
	@Resource
	private INSBAutoconfigshowService insbAutoconfigShowService;
	@Resource
	private INSBInsuresupplyparamService insbInsuresupplyparamService;
	
	/*
	 * 跳转人工录单页面
	 */
	@RequestMapping(value="showmanualrecord", method=RequestMethod.GET)
	public ModelAndView showManualRecord(String taskid,String taskcode,HttpSession session){
		ModelAndView mav = new ModelAndView("cm/manualadjustment/manualRecord");
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		//返回页面数据
		mav.addObject("taskid", taskid);
        //精灵报价标识
        boolean elfquoteFlag = false;
		Map<String, Object> agentInfo = insbAgentService.getCarTaskAgentInfo(taskid,"COMMON");
		List<String> inscomcodeList = insbQuotetotalinfoService.getInscomcodeListByInstanceId(taskid, taskcode, loginUser.getUsercode());
		List<Map<String, Object>> carInsTaskInfoList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < inscomcodeList.size(); i++) {
			String inscomcode = inscomcodeList.get(i);
			mav.addObject("inscomcode", inscomcode);

			Map<String, Object> temp = new HashMap<String, Object>();
			Map<String, Object> carInfo = insbCarinfoService.getCarTaskCarInfo(taskid, inscomcode, "SHOW");
			Map<String, Object> relationPersonInfo = insbPersonService.getCarTaskRelationPersonInfo(taskid, inscomcode , "SHOW");
			Map<String, Object> otherInfo = insbCarinfoService.getCarTaskOtherInfo(taskid, inscomcode, "SHOW");
			Map<String,Object> insConfigInfo = insbCarkindpriceService.getCarInsConfigByInscomcode(inscomcode, taskid);
			//得到子流程最新轨迹信息 ，得到主流程轨迹使用：insbWorkflowmaintrackService.getMainTrackByInscomcode(taskid)
		    //INSBWorkflowmaintrack workflowmaintrack = insbWorkflowmaintrackService.getMainTrackByInscomcode(taskid);
		    INSBWorkflowsubtrack workflowsubtrack = insbWorkflowsubtrackService.getSubTrackByInscomcode(taskid, inscomcode);
		    
		    //记录打开，标识任务已经在处理中
	        insbWorkflowsubtrackService.addTrackdetail(workflowsubtrack,loginUser.getUsercode());
			//用户备注信息，第二个参数（1主流程轨迹、 2子流程轨迹）
		    List<INSBFilelibrary> imageList = getImageList(taskid, workflowsubtrack);
//			INSBUsercomment usercomment = insbUsercommentService.selectUserCommentByTrackid(workflowsubtrack.getId(), 2);
//			List<INSBOperatorcomment> operatorcommentList = insbOperatorcommentService.selectOperatorCommentByTrackid(workflowsubtrack.getId(), 2);
//			Map<String, Object> remark = new HashMap<String, Object>();//备注信息
//			remark.put("usercomment", usercomment);
//			remark.put("opcommentList", operatorcommentList);
//		    List<INSBUsercomment> usercomment = insbUsercommentService.getNearestUserComment(taskid, inscomcode, workflowsubtrack.getTaskcode());
			List<Map<String, Object>> usercomment = insbUsercommentService.getNearestUserCommentAndType(taskid, inscomcode);
			INSBUsercomment dqusercomment = insbUsercommentService.selectUserCommentByTrackid(workflowsubtrack.getId(), 2);
			//操作员备注信息，第二个参数（1主流程轨迹、 2子流程轨迹）
			//List<INSBOperatorcomment> operatorcommentList = insbOperatorcommentService.selectOperatorCommentByTrackid(workflowsubtrack.getId(), 2);
			List<INSBOperatorcomment> operatorcommentList = insbOperatorcommentService.getOperatorCommentByMaininstanceid(taskid, inscomcode);
			Map<String, Object> remark = new HashMap<String, Object>();//备注信息
			if(usercomment!=null && usercomment.size()>0){
				remark.put("usercomment", usercomment.get(0));//当前节点之前的最新一条备注
			}
			temp.put("usercomment", usercomment);
			remark.put("dqusercomment", dqusercomment);//当前节点的备注
			remark.put("opcommentList", operatorcommentList);
			temp.put("inscomcode", inscomcode);
			temp.put("remarkinfo", remark);
			temp.put("insConfigInfo",insConfigInfo);
			temp.put("carInfo", carInfo);
			temp.put("relationPersonInfo", relationPersonInfo);
			temp.put("otherInfo", otherInfo);
			temp.put("imageList", imageList);
			temp.put("subInstanceId",workflowsubtrack.getInstanceid());
			
			Map<String, Object> tasksummary = insbOrderService.getTaskSummary(taskid,inscomcode);
			mav.addObject("tasksummary", tasksummary);         //创建时间，createtime
			
			/**
			 * hxx  判断 是否具有 核保能力
			 */
			INSBQuoteinfo quoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(taskid, inscomcode);
			String deptid =quoteinfo.getDeptcode();//出单网点
			Map<String, Object> deptmap = new HashMap<String, Object>();
			deptmap.put("providerid", inscomcode);
//			deptmap.put("deptid", deptid);
			deptmap.put("conftype", "02");
			
			List<String> tempDeptIds = insbAutoconfigShowService.getParentDeptIds4Show(deptid);
			deptmap.put("deptList", tempDeptIds);
			
			List<String> quotetypes = insbAutoconfigShowService.queryByProId(deptmap);
			if(!quotetypes.isEmpty()&&(quotetypes.contains("02")||quotetypes.contains("01"))){//包含精灵或EDI 核保能力
				temp.put("isRequote", "1");
			}else{
				temp.put("isRequote", "0");
			}
			
//			List<INSBWorkflowsubtrack> quoteList = insbOrderService.getQuoteBackTrack(workflowsubtrack.getInstanceid());
//			if(quoteList!=null && quoteList.size()>0){
//				//存在精灵，edi报价的能力  
//				temp.put("isRequote", Integer.valueOf(quoteList.size()).toString());
//			}else{
//				temp.put("isRequote", "0");
//			}
			
			
			Map<String, Object> proposalInfo = insbPolicyitemService.getPolicyNumInfo2(taskid,inscomcode);
			temp.put("proposalInfo",proposalInfo);
            //判断是否有精灵报价
            deptmap.put("conftype", "01");
            quotetypes = insbAutoconfigShowService.queryByProId(deptmap);
            if(!quotetypes.isEmpty()&&quotetypes.contains("02")){
                elfquoteFlag = true;
            }

            //核保补充数据项
            temp.put("supplyParams", insbInsuresupplyparamService.getParamsByTask(taskid, inscomcode, false));

			carInsTaskInfoList.add(temp);
		}
		mav.addObject("agentInfo",agentInfo);
		mav.addObject("carInsTaskInfoList",carInsTaskInfoList);
		mav.addObject("showEditFlag", "1");  //是否显示修改功能，0--不显示，1--显示
		//查询主流程轨迹
		List<Map<String, Object>> workflowinfoList=insbCommonWorkflowTrackservice.getWorkFlowTrack(taskid);
		mav.addObject("workflowinfoList",workflowinfoList);	
		//判断是否为费改地区
		boolean isfeeflag=insbOrderService.isFeeflagArea(taskid, null);
		if(isfeeflag){
			mav.addObject("isfeeflag","1");
		}else{
			mav.addObject("isfeeflag","0");
		}
        mav.addObject("elfquoteFlag",elfquoteFlag?"1":"0");
        
		return mav;
	}

	//跳出修改投保单号弹出框
	@RequestMapping(value="showOrEditInsureRecordNumber", method=RequestMethod.GET)
	public ModelAndView showOrEditInsureRecordNumber(String taskid){
		ModelAndView mav = new ModelAndView("cm/manualadjustment/editInsureRecordNumber");
		Map<String, Object> policyNumInfo = insbPolicyitemService.getPolicyNumInfo(taskid);
		mav.addObject("policyNumInfo",policyNumInfo);
		return mav;
	}

	/**
	 * 修改投保单号信息
	 */
	@RequestMapping(value = "editInsureRecordNumber", method = RequestMethod.POST)
	@ResponseBody
	public String editOtherInfo(HttpSession session, @ModelAttribute InsRecordNumberVO insRecordNumber) {
		LogUtil.info("----------getTaskid----------"+insRecordNumber.getTaskid());
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		insRecordNumber.setOperator(loginUser.getUsercode().toString());
		String flag = insbPolicyitemService.editPolicyNumInfo(insRecordNumber);
		if("success".equals(flag)){
			return "success";
		}else{
			return "error";
		}
	}
	
	/*
	 * 人工回写
	 */
	@RequestMapping(value="showWriteBackRecord", method=RequestMethod.GET)
	public ModelAndView showWriteBackRecord(String taskid,String taskcode,HttpSession session){
		ModelAndView mav = new ModelAndView("cm/manualadjustment/manualWriteBack");
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		//返回页面数据
		Map<String, Object> agentInfo = insbAgentService.getCarTaskAgentInfo(taskid,"COMMON");
		//List<String> inscomcodeList = insbQuotetotalinfoService.getInscomcodeListByInstanceId(taskid);
		List<String> inscomcodeList = insbQuotetotalinfoService.getInscomcodeListByInstanceId(taskid, taskcode, loginUser.getUsercode());
		List<Map<String, Object>> carInsTaskInfoList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < inscomcodeList.size(); i++) {
			String inscomcode = inscomcodeList.get(i);
			Map<String, Object> temp = new HashMap<String, Object>();
			Map<String, Object> carInfo = insbCarinfoService.getCarTaskCarInfo(taskid, inscomcode, "SHOW");
			Map<String, Object> relationPersonInfo = insbPersonService.getCarTaskRelationPersonInfo(taskid, inscomcode , "SHOW");
			Map<String, Object> otherInfo = insbCarinfoService.getCarTaskOtherInfo(taskid, inscomcode, "SHOW");
			Map<String,Object> insConfigInfo = insbCarkindpriceService.getCarInsConfigByInscomcode(inscomcode, taskid);
			//得到子流程最新轨迹信息 ，得到主流程轨迹使用：insbWorkflowmaintrackService.getMainTrackByInscomcode(taskid)
			//INSBWorkflowmaintrack workflowmaintrack = insbWorkflowmaintrackService.getMainTrackByInscomcode(taskid);
		    INSBWorkflowsubtrack workflowsubtrack = insbWorkflowsubtrackService.getSubTrackByInscomcode(taskid, inscomcode);
		    //记录打开，标识任务已经在处理中
	        insbWorkflowsubtrackService.addTrackdetail(workflowsubtrack,loginUser.getUsercode());
		    List<INSBFilelibrary> imageList = getImageList(taskid, workflowsubtrack);
//			INSBUsercomment usercomment = insbUsercommentService.selectUserCommentByTrackid(workflowsubtrack.getId(), 2);
//			//操作员备注信息，第二个参数（1主流程轨迹、 2子流程轨迹）
//			List<INSBOperatorcomment> operatorcommentList = insbOperatorcommentService.selectOperatorCommentByTrackid(workflowsubtrack.getId(), 2);
//			Map<String, Object> remark = new HashMap<String, Object>();//备注信息
//			remark.put("usercomment", usercomment);
//			remark.put("opcommentList", operatorcommentList);
//		    List<INSBUsercomment> usercomment = insbUsercommentService.getNearestUserComment(taskid, inscomcode, workflowsubtrack.getTaskcode());
			List<Map<String, Object>> usercomment = insbUsercommentService.getNearestUserCommentAndType(taskid, inscomcode);
			INSBUsercomment dqusercomment = insbUsercommentService.selectUserCommentByTrackid(workflowsubtrack.getId(), 2);
			//操作员备注信息，第二个参数（1主流程轨迹、 2子流程轨迹）
			//List<INSBOperatorcomment> operatorcommentList = insbOperatorcommentService.selectOperatorCommentByTrackid(workflowsubtrack.getId(), 2);
			List<INSBOperatorcomment> operatorcommentList = insbOperatorcommentService.getOperatorCommentByMaininstanceid(taskid, inscomcode);
			Map<String, Object> remark = new HashMap<String, Object>();//备注信息
			if(usercomment!=null && usercomment.size()>0){
				remark.put("usercomment", usercomment.get(0));//当前节点之前的最新一条备注
			}
			remark.put("dqusercomment", dqusercomment);//当前节点的备注
			remark.put("opcommentList", operatorcommentList);
			temp.put("remarkinfo", remark);
			temp.put("insConfigInfo",insConfigInfo);
			temp.put("inscomcode", inscomcode);
			temp.put("carInfo", carInfo);
			temp.put("relationPersonInfo", relationPersonInfo);
			temp.put("otherInfo", otherInfo);
			temp.put("imageList", imageList);
			temp.put("subInstanceId",workflowsubtrack.getInstanceid());
			carInsTaskInfoList.add(temp);
			Map<String, Object> tasksummary = insbOrderService.getTaskSummary(taskid,inscomcode);
			mav.addObject("tasksummary", tasksummary);         //创建时间，createtime
		}
		Map<String, Object> proposalInfo = insbPolicyitemService.getPolicyNumInfo2(taskid,inscomcodeList.get(0));
		mav.addObject("agentInfo",agentInfo);
		mav.addObject("carInsTaskInfoList",carInsTaskInfoList);
		mav.addObject("proposalInfo",proposalInfo);
		mav.addObject("showEditFlag", "1");  //是否显示修改功能，0--不显示，1--显示
		//查询主流程轨迹
		List<Map<String, Object>> workflowinfoList=insbCommonWorkflowTrackservice.getWorkFlowTrack(taskid);
		mav.addObject("workflowinfoList",workflowinfoList);	
		return mav;
	}
	
	/*
	 * 人工核保
	 */
	@RequestMapping(value="showUnderwritingRecord", method=RequestMethod.GET)
	public ModelAndView showUnderwritingRecord(HttpSession session,String taskid,String taskcode,String inscomcode){
		ModelAndView mav = new ModelAndView("cm/manualadjustment/manualUnderWriting");
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		mav.addObject("taskid", taskid);
		mav.addObject("inscomcode", inscomcode);
        //精灵报价标识
        boolean elfquoteFlag = false;
		//返回页面数据
		Map<String,Object> agentInfo = insbAgentService.getCarTaskAgentInfo(taskid,"COMMON");
		//List<String> inscomcodeList = insbQuotetotalinfoService.getInscomcodeListByInstanceId(taskid);
		INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
		quotetotalinfo.setTaskid(taskid);
		quotetotalinfo = insbQuotetotalinfoService.queryOne(quotetotalinfo);

		if (quotetotalinfo != null) {
			mav.addObject("isRenewal", "1".equals(quotetotalinfo.getIsrenewal()));
		}

		List<Map<String, Object>> carInsTaskInfoList = new ArrayList<Map<String,Object>>();

		List<String> inscomcodeList = new ArrayList<>();
		inscomcodeList.add(inscomcode);

		for (int i = 0; i < inscomcodeList.size(); i++) {
			inscomcode = inscomcodeList.get(i);
			Map<String, Object> temp = new HashMap<String, Object>();
			Map<String, Object> carInfo = insbCarinfoService.getCarTaskCarInfo(taskid, inscomcode, "SHOW");
			Map<String, Object> relationPersonInfo = insbPersonService.getCarTaskRelationPersonInfo(taskid, inscomcode , "SHOW");
			Map<String, Object> otherInfo = insbCarinfoService.getCarTaskOtherInfo(taskid, inscomcode, "SHOW");
			Map<String,Object> insConfigInfo = insbCarkindpriceService.getCarInsConfigByInscomcode(inscomcode, taskid);
			//得到子流程最新轨迹信息 ，得到主流程轨迹使用：insbWorkflowmaintrackService.getMainTrackByInscomcode(taskid)
		   //INSBWorkflowmaintrack workflowmaintrack = insbWorkflowmaintrackService.getMainTrackByInscomcode(taskid);
		    INSBWorkflowsubtrack workflowsubtrack = insbWorkflowsubtrackService.getSubTrackByInscomcode(taskid, inscomcode);
		    //记录打开，标识任务已经在处理中
	        insbWorkflowsubtrackService.addTrackdetail(workflowsubtrack,operator.getUsercode());
		    
		    List<INSBFilelibrary> imageList = getImageList(taskid, workflowsubtrack);
		    //当前节点之前的备注信息（入参     instanceid： 主流程实例id ; inscomcode 保险公司code ; dqtaskcode 当前节点code）
//		    List<INSBUsercomment> usercomment = insbUsercommentService.getNearestUserComment(taskid, inscomcode, workflowsubtrack.getTaskcode());
			List<Map<String, Object>> usercomment = insbUsercommentService.getNearestUserCommentAndType(taskid, inscomcode);
		    //当前节点的备注信息
			INSBUsercomment dqusercomment = insbUsercommentService.selectUserCommentByTrackid(workflowsubtrack.getId(), 2);
			//操作员备注信息，第二个参数（1主流程轨迹、 2子流程轨迹）
			//List<INSBOperatorcomment> operatorcommentList = insbOperatorcommentService.selectOperatorCommentByTrackid(workflowsubtrack.getId(), 2);
			List<INSBOperatorcomment> operatorcommentList = insbOperatorcommentService.getOperatorCommentByMaininstanceid(taskid, inscomcode);
			Map<String, Object> remark = new HashMap<String, Object>();//备注信息
			if(usercomment!=null && usercomment.size()>0){
				remark.put("usercomment", usercomment.get(0));//当前节点之前的最新一条备注
			}
			remark.put("dqusercomment", dqusercomment);//当前节点的备注
			remark.put("opcommentList", operatorcommentList);
			temp.put("subInstanceId",workflowsubtrack.getInstanceid());
			temp.put("remarkinfo", remark);
			temp.put("usercomment", usercomment);
			
			temp.put("insConfigInfo",insConfigInfo);
			temp.put("inscomcode", inscomcode);
			temp.put("carInfo", carInfo);
			temp.put("relationPersonInfo", relationPersonInfo);
			temp.put("otherInfo", otherInfo);
			temp.put("imageList", imageList);
			carInsTaskInfoList.add(temp);
			Map<String, Object> tasksummary = insbOrderService.getTaskSummary(taskid,inscomcode);
			mav.addObject("tasksummary", tasksummary);         //创建时间，createtime
			
			INSBQuoteinfo insbQuoteinfo=insbQuoteinfoService.getByTaskidAndCompanyid(taskid, inscomcode);
			Map<String, Object> deptmap = new HashMap<String, Object>();
			deptmap.put("providerid", inscomcode);
			deptmap.put("conftype", "02");
			List<String> tempDeptIds = insbAutoconfigShowService.getParentDeptIds4Show(insbQuoteinfo.getDeptcode());
			deptmap.put("deptList", tempDeptIds);
			List<String> quotetypes = insbAutoconfigShowService.queryByProId(deptmap);
			if(quotetypes.contains("02")&&quotetypes.contains("01")){
				mav.addObject("isReub", "2");    //存在精灵，edihe核保的能力 
			}else if(quotetypes.contains("02")||quotetypes.contains("01")){
				mav.addObject("isReub", "1");
			}else{
				mav.addObject("isReub", "0");
			}
			
			//判断是否有自核能力，查看轨迹表获得
			List<String> taskcodeList = new ArrayList<String>();
			taskcodeList.add(TaskConst.EDI_AUTO_INSURE);
			taskcodeList.add(TaskConst.ELF_AUTO_INSURE);
			Map<String, Object> taskcodeMap = new HashMap<String, Object>();
			taskcodeMap.put("taskid", taskid);
			taskcodeMap.put("taskcodelist", taskcodeList);
			List<INSBWorkflowsubtrack> trackList = insbWorkflowsubtrackService.selectByTaskcodeList(taskcodeMap);
			if(trackList!= null && trackList.size() > 0){
				mav.addObject("isautoinsure","1"); //有自核能力
			} else {
				mav.addObject("isautoinsure","0");
			}
            //判断是否有精灵报价
            deptmap.put("conftype", "01");
            quotetypes = insbAutoconfigShowService.queryByProId(deptmap);
            if(!quotetypes.isEmpty()&&quotetypes.contains("02")){
                elfquoteFlag = true;
            }

			//核保补充数据项
			temp.put("supplyParams", insbInsuresupplyparamService.getParamsByTask(taskid, inscomcode, false));
		}
		Map<String, Object> proposalInfo = insbPolicyitemService.getPolicyNumInfo2(taskid,inscomcode);
		mav.addObject("agentInfo",agentInfo);
		mav.addObject("carInsTaskInfoList",carInsTaskInfoList);
		mav.addObject("proposalInfo",proposalInfo);
		mav.addObject("showEditFlag", "1");  //是否显示修改功能，0--不显示，1--显示
		mav.addObject("paymentinfo", insbOrderService.getPaymentinfo(taskid,inscomcode));//支付信息
		//查询主流程轨迹
		List<Map<String, Object>> workflowinfoList=insbCommonWorkflowTrackservice.getWorkFlowTrack(taskid);
		mav.addObject("workflowinfoList",workflowinfoList);
        mav.addObject("elfquoteFlag",elfquoteFlag?"1":"0");
		return mav;
	}
	
	/**
	 * 查看报价金额
	 * @param instanceid
	 * @param inscomcode
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "searchMoney",method = RequestMethod.GET)
	public ModelAndView searchMoney(String instanceid, String inscomcode) throws ControllerException {
		ModelAndView mav = new ModelAndView("cm/manualadjustment/showSearchMoney");
		Map<String,Object> insConfigInfo = inshCarkindpriceService.getCarInsConfigByInscomcode(inscomcode, instanceid);//保险配置信息
		mav.addObject("insConfigInfo",insConfigInfo);
		return mav;	
	}
	
	private List<INSBFilelibrary> getImageList(String taskid,INSBWorkflowsubtrack workflowsubtrack) {
		List<INSBFilelibrary> imageList = insbFilelibraryService.queryByFilebusinessCode(taskid);
//		List<INSBFilelibrary> imageListSub = insbFilelibraryService.queryByFilebusinessCode(workflowsubtrack.getInstanceid());
//		if(imageList!=null && imageList.size()>0){
//			if(imageListSub!=null && imageListSub.size()>0){
//				imageList.addAll(imageListSub);
//			}
//		}else{
//			if(imageListSub!=null && imageListSub.size()>0){
//				imageList = imageListSub;
//			}else{  
//				imageList = new ArrayList<INSBFilelibrary>();
//			}
//		}
		return imageList;
	}
}
