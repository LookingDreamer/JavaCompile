package com.zzb.cm.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCDeptService;
import com.common.MiscConst;
import com.common.WorkflowFeedbackUtil;
import com.zzb.cm.controller.vo.ManualPushFlowVo;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.service.INSBWorkflowsubService;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.cninsure.core.controller.BaseController;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.common.PagingParams;
import com.common.WorkFlowUtil;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.service.INSBManualPriceService;
import com.zzb.cm.service.INSBManualPushFlowService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.INSBPaymentService;
import net.sf.json.JSONObject;

/**
 * 人工推送工作流	
 * 
 */
@Controller
@RequestMapping("/business/manualpushflow/*")
public class INSBBusinessManualPushFlowController extends BaseController{
	@Resource
	private INSBManualPushFlowService insbManualPushFlowService;
	@Resource
	private INSCCodeService inscCodeService;
	@Resource
	private INSBManualPriceService insbManualPriceService;
	@Resource
	private INSBPaymentService insbPaymentService;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSCCodeDao inscCodeDao ;
    @Resource
    private INSBWorkflowsubService workflowsubService;
	@Resource
	private INSCDeptService inscDeptService;
	
	/**
	 * 跳转至任务列表页面 	杨威
	 */
	@RequestMapping(value = "totasklist", method = RequestMethod.GET)
	public ModelAndView	toTaskList(){
		ModelAndView mav=new ModelAndView("cm/workflow/insbworkflow");
		mav.addObject("workFlowNodeList", inscCodeService.getWorkFlowNodesForCarTaskQuery().stream().filter(e ->
				!"30".equals(e.get("codevalue")) && !"34".equals(e.get("codevalue"))
		).collect(Collectors.toCollection(ArrayList<Map<String, String>>::new)));
		return mav;
	}

	/**
	 * 任务列表查询
	 * 杨威	2016/7/1
	 */
	@RequestMapping(value = "showtasklist", method = RequestMethod.GET)
	@ResponseBody
	public String showTaskList(HttpSession session, @ModelAttribute PagingParams pagingParams,String mainInstanceId,String tasktype,String taskstatus,
							   String taskcreatetimeup,String taskcreatetimedown){
		INSCUser user = (INSCUser)session.getAttribute("insc_user");
		if (user == null) {
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("records", "10000");
			map.put("page", 1);
			map.put("total",0);
			map.put("rows", Collections.EMPTY_LIST);
			return JSONObject.fromObject(map).toString();
		}

		String deptinnercode = null;
		if (StringUtil.isNotEmpty(user.getUserorganization())) {
			INSCDept d = inscDeptService.getOrgDeptByDeptCode(user.getUserorganization());
			if (d != null) {
				deptinnercode = d.getDeptinnercode();
			} else {
				LogUtil.error(user.getUsercode()+"所属机构"+user.getUserorganization()+"不存在");
			}
		} else {
			LogUtil.error(user.getUsercode()+"所属机构为空");
		}

		//组织入参
		Map<String,Object> param=BeanUtils.toMap(pagingParams);
		param.put("mainInstanceId", mainInstanceId);
		param.put("tasktype", tasktype);
		param.put("taskstatus", taskstatus);
		param.put("taskcreatetimeup", taskcreatetimeup);
		param.put("taskcreatetimedown", taskcreatetimedown);
		param.put("deptinnercode", deptinnercode);
		return insbManualPushFlowService.showTaskList(param);
	}
	
	
	/**
	 * 报价/核保任务成功按钮  
	 */
	@RequestMapping(value = "underwritesuccess", method = RequestMethod.POST)
	@ResponseBody
	public String  underwritesuccess(HttpSession session, @RequestBody List<ManualPushFlowVo> manualPushFlowVoList){
		LogUtil.info("干预--任务完成：" + JSONArray.fromObject(manualPushFlowVoList).toString());
		INSCUser user=(INSCUser)session.getAttribute("insc_user");
		String operator=user.getUsercode();
		//使cm业务工作节点和工作流节点一直
		//this.toBecomesame(instanceid, type, forname,operator);
		String result = null;

		for (ManualPushFlowVo vo : manualPushFlowVoList) {
			//报价/核保任务成功,调用工作流.组织调用工作流的参数
			result = insbManualPushFlowService.underWriteSuccess(vo.getSubinstanceid(), operator, vo.getTaskcode(), vo.getMaininstanceid(), vo.getInscomcode());
		}

		return result;
	}
	
	/**
	 * 支付/二次支付成功  
	 */
	@RequestMapping(value = "paySuccess", method = RequestMethod.POST)
	@ResponseBody
	public String  paySuccess(HttpSession session, @RequestBody List<ManualPushFlowVo> manualPushFlowVoList){
		LogUtil.info("干预--支付/二支成功：" + JSONArray.fromObject(manualPushFlowVoList).toString());
		INSCUser user=(INSCUser)session.getAttribute("insc_user");
		String operator=user.getUsercode();
		//使cm业务工作节点和工作流节点一直
		//this.toBecomesame(instanceid, type, forname,operator);
		String result = null;

		for (ManualPushFlowVo vo : manualPushFlowVoList) {
			//报价/核保任务成功,调用工作流.组织调用工作流的参数
			result = insbManualPushFlowService.paySuccess(vo.getSubinstanceid(), operator, vo.getTaskcode(), vo.getMaininstanceid(), vo.getInscomcode());
		}

		return result;
	}
	
	/**
	 * 支付失败
	 */
	@RequestMapping(value = "underwritefail", method = RequestMethod.POST)
	@ResponseBody
	public String  underwritefail(HttpSession session, @RequestBody List<ManualPushFlowVo> manualPushFlowVoList){
		LogUtil.info("干预--支付失败：" + JSONArray.fromObject(manualPushFlowVoList).toString());
		INSCUser user=(INSCUser)session.getAttribute("insc_user");
		String operator=user.getUsercode();
		//使cm业务工作节点和工作流节点一直
		//this.toBecomesame(instanceid, type, forname,operator);
		String result = null;

		for (ManualPushFlowVo vo : manualPushFlowVoList) {
			//报价/核保任务成功,调用工作流.组织调用工作流的参数
			result = insbManualPushFlowService.underwritefail(vo.getSubinstanceid());
		}

		return result;
	}
	
	/**
	 * 承保打单成功
	 */
	@RequestMapping(value = "undwrtSuccess", method = RequestMethod.POST)
	@ResponseBody
	public String  undwrtSuccess(HttpSession session, @RequestBody List<ManualPushFlowVo> manualPushFlowVoList){
		LogUtil.info("干预--承保打单成功：" + JSONArray.fromObject(manualPushFlowVoList).toString());
		INSCUser user=(INSCUser)session.getAttribute("insc_user");
		String operator=user.getUsercode();
		//使cm业务工作节点和工作流节点一直
		//this.toBecomesame(instanceid, type, forname,operator);
		String result = null;

		for (ManualPushFlowVo vo : manualPushFlowVoList) {
			//报价/核保任务成功,调用工作流.组织调用工作流的参数
			result = insbManualPushFlowService.undwrtSuccess(vo.getInstanceid(), operator, vo.getTaskcode(), vo.getInscomcode());
		}

		return result;
	}
	
	/**
	 * 承保打单配送成功
	 */
	@RequestMapping(value = "unwrtdeliverySuccess", method = RequestMethod.POST)
	@ResponseBody
	public String  unwrtdeliverySuccess(HttpSession session, @RequestBody List<ManualPushFlowVo> manualPushFlowVoList){
		LogUtil.info("干预--承保打单配送成功：" + JSONArray.fromObject(manualPushFlowVoList).toString());
		INSCUser user=(INSCUser)session.getAttribute("insc_user");
		String operator=user.getUsercode();
		//使cm业务工作节点和工作流节点一直
		//this.toBecomesame(instanceid, type, forname,operator);
		String result = null;

		for (ManualPushFlowVo vo : manualPushFlowVoList) {
			//报价/核保任务成功,调用工作流.组织调用工作流的参数
			result = insbManualPushFlowService.unwrtDeliverySuccess(vo.getInstanceid(), operator, vo.getTaskcode(), vo.getInscomcode());
		}

		return result;
	}
	
	/**
	 * 配送成功
	 */
	@RequestMapping(value = "deliverySuccess", method = RequestMethod.POST)
	@ResponseBody
	public String  deliverySuccess(HttpSession session, @RequestBody List<ManualPushFlowVo> manualPushFlowVoList){
		LogUtil.info("干预--配送成功：" + JSONArray.fromObject(manualPushFlowVoList).toString());
		INSCUser user=(INSCUser)session.getAttribute("insc_user");
		String operator=user.getUsercode();
		//使cm业务工作节点和工作流节点一直
		//this.toBecomesame(instanceid, type, forname,operator);
		String result = null;

		for (ManualPushFlowVo vo : manualPushFlowVoList) {
			//报价/核保任务成功,调用工作流.组织调用工作流的参数
			result = insbManualPushFlowService.deliverySuccess(vo.getInstanceid(), operator, vo.getInscomcode());
		}

		return result;
	}
	
	/**
	 * 拒绝承保或者取消任务
	 */
	@RequestMapping(value = "refuseInsuranceUw", method = RequestMethod.POST)
	@ResponseBody
	public String  refuseInsuranceUw(HttpSession session, @RequestBody List<ManualPushFlowVo> manualPushFlowVoList){
        LogUtil.info("干预--拒保或取消：" + JSONArray.fromObject(manualPushFlowVoList).toString());
		INSCUser user=(INSCUser)session.getAttribute("insc_user");
		String operator=user.getUsercode();
		//使cm业务工作节点和工作流节点一直
		//this.toBecomesame(instanceid, type, forname,operator);
		String result = null, closeCause = "人工干预拒绝承保", feedback = WorkflowFeedbackUtil.quote_reject;

		for (ManualPushFlowVo vo : manualPushFlowVoList) {
			if (StringUtil.isEmpty(vo.getCmtaskcode())) {
			    LogUtil.error(vo.getMaininstanceid()+"状态参数值为空");
                continue;
            }

            if ("cancel".equalsIgnoreCase(vo.getRemark())) {
                closeCause = "人工干预取消任务";
                if ("sub".equalsIgnoreCase(vo.getType())) {
                    feedback = WorkflowFeedbackUtil.quote_cancel;
                } else {
                    feedback = WorkflowFeedbackUtil.multiQuote_cancel;
                }
            }

            if ("sub".equalsIgnoreCase(vo.getType())) {
                WorkflowFeedbackUtil.setWorkflowFeedback(vo.getMaininstanceid(), vo.getSubinstanceid(), vo.getCmtaskcode(), "Completed", null, feedback, operator);
                WorkflowFeedbackUtil.setWorkflowFeedback(vo.getMaininstanceid(), vo.getSubinstanceid(), "37", "Closed", null, feedback+"#"+closeCause, operator);

                result = insbManualPriceService.refuseUnderwrite2(vo.getMaininstanceid(), vo.getSubinstanceid(), vo.getInscomcode(), vo.getType(), "back");

            } else if (StringUtil.isNotEmpty(vo.getCmtaskcode()) && !MiscConst.noCancelNodeList.contains(vo.getCmtaskcode())){
                List<INSBWorkflowsub> subList = workflowsubService.selectSubModelByMainInstanceId(vo.getMaininstanceid());
                if (subList != null) {
                    for (INSBWorkflowsub sub : subList) {
                        WorkflowFeedbackUtil.setWorkflowFeedback(sub.getMaininstanceid(), sub.getInstanceid(), sub.getTaskcode(), "Completed", null, feedback, operator);
                        WorkflowFeedbackUtil.setWorkflowFeedback(sub.getMaininstanceid(), sub.getInstanceid(), "37", "Closed", null, feedback+"#"+closeCause, operator);
                    }
                }

                WorkflowFeedbackUtil.setWorkflowFeedback(vo.getMaininstanceid(), null, "37", "Closed", null, feedback+"#"+closeCause, operator);
                result = insbManualPriceService.refuseUnderwrite2(vo.getMaininstanceid(), "", "", vo.getType(), "back");
            }
		}

		return result;
	}
	
	/**
	 * 强转人工
	 */
	@RequestMapping(value = "pushManualInterface", method = RequestMethod.POST)
	@ResponseBody
	public String  pushManualInterface(HttpSession session, @RequestBody List<ManualPushFlowVo> manualPushFlowVoList){
		LogUtil.info("干预--转人工：" + JSONArray.fromObject(manualPushFlowVoList).toString());
		INSCUser user=(INSCUser)session.getAttribute("insc_user");
		String operator=user.getUsercode();
		//使cm业务工作节点和工作流节点一直
		//this.toBecomesame(instanceid, type, forname,operator);
		//传递参数
		String message=null;

		for (ManualPushFlowVo vo : manualPushFlowVoList) {
			String taskName = vo.getTaskname();
			if (StringUtil.isEmpty(taskName)) {
				taskName = inscCodeDao.getcodeNameBycodeValue(vo.getTaskcode());
			}

			if ("25".equals(vo.getTaskcode()) || "26".equals(vo.getTaskcode())) {//自动承保
				Map<String, Object> data = new HashMap<>(1);
				data.put("result", "1");
				data.put("acceptway", "3");
				WorkflowFeedbackUtil.setWorkflowFeedback(vo.getMaininstanceid(), null, vo.getTaskcode(), "Completed", taskName, WorkflowFeedbackUtil.manual_complete, operator);
				message = WorkFlowUtil.undwrtSuccess(vo.getInstanceid(), operator, taskName, data);

			} else if ("7".equals(vo.getTaskcode())) {//人工规则报价
				WorkflowFeedbackUtil.setWorkflowFeedback(null, vo.getSubinstanceid(), vo.getTaskcode(), "Completed", taskName, WorkflowFeedbackUtil.manual_complete, operator);
				message = insbManualPushFlowService.pushManualInterface(vo.getInstanceid(), operator);
			} else {
				WorkflowFeedbackUtil.setWorkflowFeedback(null, vo.getInstanceid(), vo.getTaskcode(), "Completed", taskName, WorkflowFeedbackUtil.manual_complete, operator);
				message = insbManualPushFlowService.completeTaskWorkflow("1", vo.getInstanceid(), operator, taskName, vo.getTaskcode());
			}
		}

		Map<String,String> map=new HashMap<String,String>();
		if(StringUtil.isNotEmpty(message)){
			JSONObject result=JSONObject.fromObject(message);
			if("success".equals(result.getString("message"))){
				map.put("status","success");
				map.put("msg","操作成功!");
			}else if("fail".equals(result.getString("message"))){
				map.put("status","fail");
				map.put("msg","工作流操作失败!");
			}
		}else{
			map.put("status","fail");
			map.put("msg","工作流调用失败,返回空!");
		}
		return JSONObject.fromObject(map).toString();
	}
	
	/**
	 * 退回修改
	 */
	@RequestMapping(value = "callBackInterface", method = RequestMethod.POST)
	@ResponseBody
	public String  callBackInterface(HttpSession session, @RequestBody List<ManualPushFlowVo> manualPushFlowVoList){
		LogUtil.info("干预--退回修改：" + JSONArray.fromObject(manualPushFlowVoList).toString());
		INSCUser user=(INSCUser)session.getAttribute("insc_user");
		String operator=user.getUsercode();
		//使cm业务工作节点和工作流节点一直
		//this.toBecomesame(instanceid, type, forname,operator);
		String result = null;

		for (ManualPushFlowVo vo : manualPushFlowVoList) {
			//退回修改调用工作流的参数
			result = insbManualPushFlowService.callBackInterface(vo.getInstanceid(), operator, vo.getTaskcode());
		}

		return result;
	}
	
	/**
	 * 查询支付状态
	 * @param manualPushFlowVoList
	 * @return
	 */
	@RequestMapping(value = "/queryPayResult", method = RequestMethod.POST)
	@ResponseBody
	public String queryPayResult(@RequestBody List<ManualPushFlowVo> manualPushFlowVoList) {
		CommonModel result = null;
		Map<String, String> map = new HashMap<>();

		for (ManualPushFlowVo vo : manualPushFlowVoList) {
			INSBOrder insbOrder = new INSBOrder();
			insbOrder.setTaskid(vo.getMaininstanceid());
			insbOrder.setPrvid(vo.getInscomcode());
			insbOrder = insbOrderDao.selectOne(insbOrder);

			String bizid = null;
			if (insbOrder != null) {
				bizid = insbOrder.getOrderno();
			}

			try {
				result = insbPaymentService.queryPayResult(bizid);
			} catch (Exception e) {
				map.put("status", "fail");
				map.put("msg", "未支付");
				return JSONObject.fromObject(map).toString();
			}
		}

		if(result!=null){
			JSONObject  jsonObj = JSONObject.fromObject(result);
			if("success".equals(jsonObj.getString("status"))){
				map.put("status","success");
				map.put("msg","操作成功"+jsonObj.getString("message"));
			}else if("fail".equals(jsonObj.getString("status"))){
				map.put("status","fail");
				map.put("msg","操作失败"+jsonObj.getString("message"));
			}
		}else{
			map.put("status","fail");
			map.put("msg","查看操作失败");
		}
		return JSONObject.fromObject(map).toString();
	}
	
	/**
	 * 一致方法
	 * @param session
	 * @param manualPushFlowVoList
	 */
	@RequestMapping(value = "/togetherWorkflow", method = RequestMethod.POST)
	@ResponseBody
	public String togetherWorkflow(HttpSession session, @RequestBody List<ManualPushFlowVo> manualPushFlowVoList){
		LogUtil.info("干预--状态同步：" + JSONArray.fromObject(manualPushFlowVoList).toString());
		INSCUser user=(INSCUser)session.getAttribute("insc_user");
		String operator=user.getUsercode();
		//使cm业务工作节点和工作流节点一致
		Map<String,String> result = insbManualPushFlowService.syncWorkflowStatus(manualPushFlowVoList, operator);
		return JSONObject.fromObject(result).toString();
	}
}