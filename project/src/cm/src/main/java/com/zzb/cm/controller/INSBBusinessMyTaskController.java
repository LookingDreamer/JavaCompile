package com.zzb.cm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCDeptService;
import com.common.ConstUtil;
import com.common.TaskConst;
import com.zzb.cm.controller.vo.MyTaskVo;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBFilelibrary;
import com.zzb.conf.entity.INSBOperatorcomment;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBOperatorcommentService;
import com.zzb.conf.service.INSBPolicyitemService;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.conf.service.INSBWorkflowsubtrackService;

/**
 * CM系统 我的任务
 */
@Controller
@RequestMapping("/business/mytask/*")
public class INSBBusinessMyTaskController extends BaseController {

	@Resource
	private INSBCarinfoService insbCarinfoService;
	@Resource
	private INSBOrderService insbOrderService;
	@Resource
	private INSBPersonService insbPersonService;
	@Resource
	private INSBQuotetotalinfoService insbQuotetotalinfoService;
	@Resource
	private INSBCarkindpriceService insbCarkindpriceService;
	@Resource
	private INSBMyTaskService myTaskService;
	@Resource
	private INSCDeptService deptService;
	@Resource
	private INSBAgentService insbAgentService;
	@Resource
	private INSBProviderService insbProviderService;
	@Resource
	private INSBCarkindpriceService carkindpriceService;
	@Resource
	private INSBUsercommentService insbUsercommentService;
	@Resource
	private INSBOperatorcommentService insbOperatorcommentService;
	@Resource
	private INSBPolicyitemService policyitemService;
	@Resource
	private INSBCommonWorkflowTrackservice insbCommonWorkflowTrackservice;
	@Resource
	private INSBFilelibraryService insbFilelibraryService;
	@Resource
	private INSBWorkflowsubtrackService insbWorkflowsubtrackService;
	@Resource
	private INSBWorkflowsubService insbWorkflowsubService;
    @Resource
    private INSBQuoteinfoService insbQuoteinfoService;
    @Resource
    private INSBWorkflowmainService insbWorkflowmainService;
	@Resource
	private INSBInsuresupplyparamService insbInsuresupplyparamService;
	
	/**
	 * 点击菜单初始化查询我的任务
	 */
	@RequestMapping(value = "queryTask", method = RequestMethod.GET)
	public ModelAndView queryTask(HttpSession session) throws ControllerException {
		ModelAndView mav = new ModelAndView("cm/mytask/myTask");
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		MyTaskVo taskVo = new MyTaskVo();
		/*用于分页查询 需要分页时放开
		taskVo.setCurrentpage(1);*/
		taskVo.setCreateby(operator.getUsercode());
		/*if(operator.getDeptinnercode().startsWith("02027")){//
			LogUtil.info("陕西平台限制数据查询时间，平台内部code：%s",operator.getDeptinnercode());
			taskVo.setDeptinnercode(operator.getDeptinnercode());
		}*/
		taskVo.setDeptid(deptService.queryByComCode(operator.getUserorganization()));
		mav.addObject("allData", myTaskService.queryTaskList(taskVo));
		mav.addObject("countDayTask", myTaskService.countDayTask(taskVo));
		mav.addObject("countMonthTask", myTaskService.countMonthTask(taskVo));
		/*用于高级搜索任务务类型下拉框的数据 需要高级搜索时放开
		mav.addObject("taskTypeList", codeService.queryMyTaskCode("workflowNodelName", "workflowNodelName"));*/
		return mav;
	}
		
	/**
	 * 我的任务搜索
	 */
	@RequestMapping(value = "queryMyTask", method = RequestMethod.POST)
	public ModelAndView queryMyTask(MyTaskVo taskVo,HttpSession session) throws ControllerException {
		ModelAndView mav = new ModelAndView("cm/mytask/myTaskSon");
		INSCUser createBy = (INSCUser) session.getAttribute("insc_user");
		taskVo.setCreateby(createBy.getUsercode());
		taskVo.setDeptid(deptService.queryByComCode(createBy.getUserorganization()));
		mav.addObject("allData", myTaskService.queryTaskList(taskVo));
		mav.addObject("countDayTask", myTaskService.countDayTask(taskVo));
		mav.addObject("countMonthTask", myTaskService.countMonthTask(taskVo));
		//mav.addObject("taskTypeList", codeService.queryMyTaskCode("workflowNodelName", "workflowNodelName"));
		return mav;
	}
	
	/**
	 *查看支付结果弹出框
	 */
	@RequestMapping(value = "getPayResult", method = RequestMethod.GET)
	@ResponseBody
	public String getPayResult(String taskid){
		return myTaskService.getPayResult(taskid);
	}

	@RequestMapping(value = "checkCloseTask", method = RequestMethod.GET)
	@ResponseBody
	public boolean checkCloseTask(String instanceId, String providerid,HttpSession session){
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		return myTaskService.checkCloseTask(instanceId, providerid,loginUser.getUsercode());
	}
	
	/**
	 *任务转发弹出框
	 */
	@RequestMapping(value = "preTransformTask", method = RequestMethod.GET)
	public ModelAndView preTransformTask(String maintaskid,String taskcode,String providerid,HttpSession session){
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		ModelAndView mav = new ModelAndView("cm/mytask/preTransformTask");
		List<String> inscomcodeList = insbQuotetotalinfoService.getInscomcodeListByInstanceId(maintaskid, taskcode, loginUser.getUsercode());
		List<INSBProvider> providerList = insbProviderService.getInscomNameList(inscomcodeList);
		mav.addObject("providerid",providerid);
		mav.addObject("maintaskid",maintaskid);
		mav.addObject("providerList",providerList);
		mav.addObject("taskcode",taskcode);

        INSBQuoteinfo quoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(maintaskid, providerid);
        if (quoteinfo != null) {
           mav.addObject("subinstanceid", quoteinfo.getWorkflowinstanceid());
        } else {
            mav.addObject("subinstanceid", "");
        }

		return mav;
	}
	
	/**
	 * 任务转发
	 */
	@RequestMapping(value = "transformTask", method = RequestMethod.GET)
	@ResponseBody
	public String TransformTask(String taskcode,String maintaskid,String inscomcode,String providerid,String subinstanceid,String tousercode,HttpSession session){
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		int instanceType = 0;
		String instanceId = null;
		if(TaskConst.QUOTING_6.equals(taskcode)||TaskConst.QUOTING_7.equals(taskcode)||TaskConst.QUOTING_8.equals(taskcode)
				||TaskConst.VERIFYING_18.equals(taskcode)||TaskConst.PAYING_20.equals(taskcode)||TaskConst.PAYINGSUCCESS_SECOND_21.equals(taskcode)){
			instanceType = 2;
			Map<String,String> map = new HashMap<String,String>();
			map.put("maintaskid", maintaskid);
			map.put("operator", loginUser.getUsercode());
			map.put("inscomcode", inscomcode);
			instanceId = insbWorkflowsubService.getTransformTaskInstanceid(map);
		}else{
			// 调整,如果主流程的taskcode不是2,则表示子流程结束.
			INSBWorkflowmain insbWorkflowmain = insbWorkflowmainService.selectByInstanceId(maintaskid);
			if( TaskConst.QUOTING_2.equals(insbWorkflowmain.getTaskcode()) ) {
	            if (StringUtil.isEmpty(subinstanceid)) {
	                instanceType = 1;
	                instanceId = maintaskid;
	            } else {
	                instanceType = 2;
	                instanceId = subinstanceid;
	            }
			} else {
				instanceType = 1;
                instanceId = maintaskid;
			}
		}
		try {
			myTaskService.transformTask(instanceId, providerid, instanceType, (INSCUser) session.getAttribute("insc_user"), tousercode);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "faild";
		}
	}
	
	/*
	 * 二次支付页面，wangyang
	 */
	@RequestMapping(value = "mediumpayment", method = RequestMethod.GET)
	public ModelAndView mediumPayment(HttpSession session,String taskid,String taskcode,String inscomcode) throws ControllerException {
		ModelAndView mav = new ModelAndView("cm/ordermanage/mediumPayment");
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		INSBCarinfo carinfo = new INSBCarinfo();
		carinfo.setTaskid(taskid);
		mav.addObject("taskcode", taskcode);
		//代理人信息
		Map<String, Object> agentInfo = insbAgentService.getCarTaskAgentInfo(taskid,"DETAIL");//代理人信息
		mav.addObject("agentInfo", agentInfo);
		
		Map<String, Object> temp = new HashMap<String, Object>();
		List<Map<String, Object>> carInsTaskInfoList = new ArrayList<Map<String,Object>>();
		//List<String> inscomcodeList = insbQuotetotalinfoService.getInscomcodeListByInstanceId(taskid);
		//String inscomcode = inscomcodeList.get(0);	
		
		Map<String, Object> carInfo = insbCarinfoService.getCarTaskCarInfo(taskid, inscomcode, "SHOW");//车辆信息
		Map<String, Object> relationPersonInfo = insbPersonService.getCarTaskRelationPersonInfo(taskid, inscomcode , "SHOW");//关系人信息
		Map<String, Object> otherInfo = insbCarinfoService.getCarTaskOtherInfo(taskid, inscomcode, "SHOW");//其他信息
		Map<String,Object> insConfigInfo = insbCarkindpriceService.getCarInsConfigByInscomcode(inscomcode, taskid);//保险配置信息
		
		// 添加保费信息
		mav.addObject("premiumInfo",carkindpriceService.getPremiumInfo(taskid,inscomcode));
		// 添加任务摘要信息
		Map<String, Object> tasksummary = insbOrderService
				.getTaskSummary(taskid,inscomcode);
		mav.addObject("tasksummary", tasksummary);
		mav.addObject("inscomcode", inscomcode);
		
		temp.put("carInfo", carInfo);
		temp.put("relationPersonInfo", relationPersonInfo);
		temp.put("otherInfo", otherInfo);
		temp.put("insConfigInfo",insConfigInfo);
		
		//得到子流程最新轨迹信息 ，得到主流程轨迹使用：insbWorkflowmaintrackService.getMainTrackByInscomcode(taskid)
//	    INSBWorkflowmaintrack workflowmaintrack = insbWorkflowmaintrackService.getMainTrackByInscomcode(taskid);
	    INSBWorkflowsubtrack workflowsubtrack = insbWorkflowsubtrackService.getSubTrackByInscomcode(taskid, inscomcode);
	    //记录打开，标识任务已经在处理中
        insbWorkflowsubtrackService.addTrackdetail(workflowsubtrack,operator.getUsercode());
	    List<INSBFilelibrary> imageList = getImageList(taskid, workflowsubtrack);
//		INSBUsercomment usercomment = insbUsercommentService.selectUserCommentByTrackid(workflowmaintrack.getId(), 1);
//		//操作员备注信息，第二个参数（1主流程轨迹、 2子流程轨迹）
//		List<INSBFilelibrary> imageList = getImageList(taskid, workflowsubtrack);
//		List<INSBOperatorcomment> operatorcommentList = insbOperatorcommentService.selectOperatorCommentByTrackid(workflowmaintrack.getId(), 1);
//		Map<String, Object> remark = new HashMap<String, Object>();//备注信息
//		remark.put("usercomment", usercomment);
//		remark.put("opcommentList", operatorcommentList);
//	    List<INSBUsercomment> usercomment = insbUsercommentService.getNearestUserComment(taskid, inscomcode, workflowsubtrack.getTaskcode());
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
		temp.put("subInstanceId",workflowsubtrack.getInstanceid());
        temp.put("inscomcode", inscomcode);
        temp.put("imageList", imageList);

		//核保补充数据项
		temp.put("supplyParams", insbInsuresupplyparamService.getParamsByTask(taskid, inscomcode, false));

		carInsTaskInfoList.add(temp);
		mav.addObject("carInsTaskInfoList",carInsTaskInfoList);
		// 添加投保单号信息
		Map<String, Object> proposalInfo =  policyitemService.getPolicyNumInfo2(taskid,inscomcode);
		mav.addObject("proposalInfo", proposalInfo);
		// 添加支付信息
		List<Map<String, Object>> paymentinfo = insbOrderService.getSecondPaymentinfo(taskid,inscomcode);
		mav.addObject("paymentinfo", paymentinfo);
		mav.addObject("showEditFlag", "0");  //是否显示修改功能，0--不显示，1--显示
		String balanceFlag = "0";//差额补齐标识：0--不显示，1--显示
		if (paymentinfo != null && paymentinfo.size() > 0) {
			for (Map<String, Object> payList : paymentinfo) {
				String payMethod = (String) payList.get("paymentmethod");
				if (payMethod != null && payMethod.trim().contains("差额补齐")) {
					balanceFlag = "1";//差额补齐标识：0--不显示，1--显示
					break;
				}
			}
		}
		mav.addObject("balanceFlag", balanceFlag);//差额补齐标识：0--不显示，1--显示

		//北京平台支付，二支节点显示被保人身份证信息
		INSCDept dept = new INSCDept();
		dept.setComcode(operator.getUserorganization());
		dept = deptService.queryOne(dept);
		if(dept != null){
			INSCDept userDept = deptService.getPlatformDept(dept.getId());
			if(userDept != null && (ConstUtil.PLATFORM_BEIJING_DEPTCODE.equals(userDept.getComcode()))){
				if ("20".equals(taskcode) || "21".equals(taskcode)) {
					mav.addObject("showbbrIDinfo", true);
				}
			}
		}
		
		//查询主流程轨迹
		List<Map<String, Object>> workflowinfoList=insbCommonWorkflowTrackservice.getWorkFlowTrack(taskid);
		mav.addObject("workflowinfoList",workflowinfoList);	
		return mav;
	}

	private List<INSBFilelibrary> getImageList(String taskid,INSBWorkflowsubtrack workflowsubtrack) {
		List<INSBFilelibrary> imageList = insbFilelibraryService.queryByFilebusinessCode(taskid);
		return imageList;
	}
	
	/**
	 * 根据主流程号 taskid,保险公司code,备注类型(1-给用户,2-给操作人员,3-用户备注)
	 * @param taskid
	 * @param inscomcode
	 * @param notiType
	 * @return
	 */
	@RequestMapping(value = "queryusercomment", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView queryUserComment(String taskid,String inscomcode,String notiType){
		ModelAndView mav=new ModelAndView("cm/mytask/allUserCommentInfo");
		if("1".equals(notiType)){
			List<Map<String, Object>> notiList = insbUsercommentService.getUserCommentAndType(taskid, inscomcode);
			mav.addObject("allComment",notiList);
			mav.addObject("notiType", "给用户备注");
		}else if ("2".equals(notiType)){
			List<String> notiList=insbOperatorcommentService.getOperCommentByMaininstanceid(taskid, inscomcode);
			mav.addObject("allComment",notiList);
			mav.addObject("notiType", "给操作员备注");
		}else {
			List<Map<String, Object>> notiList = insbUsercommentService.getNearestUserCommentAndType(taskid, inscomcode);
			mav.addObject("allComment",notiList);
			mav.addObject("notiType", "用户备注");
		}
		return mav;
	}
}
