package com.zzb.cm.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.DispatchService;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.cninsure.system.service.INSCDeptService;
import com.cninsure.system.service.INSCUserService;
import com.cninsure.system.tool.Cm4WorkflowFilter;
import com.common.ConstUtil;
import com.common.JsonUtil;
import com.common.RedisException;
import com.common.TaskConst;
import com.common.WorkFlowException;
import com.common.redis.CMRedisClient;
import com.mysql.fabric.xmlrpc.base.Params;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.cm.controller.vo.DeliveryInfoItem;
import com.zzb.cm.controller.vo.LoopUnderWritingVO;
import com.zzb.cm.controller.vo.MediumPaymentVo;
import com.zzb.cm.controller.vo.OrderManageVO01;
import com.zzb.cm.controller.vo.PayInfoItem;
import com.zzb.cm.controller.vo.UnderWtringLoopResultVO;
import com.zzb.cm.controller.vo.UnderwritingResultVO;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.entity.INSBFilelibrary;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBCarinfoService;
import com.zzb.cm.service.INSBCarinfohisService;
import com.zzb.cm.service.INSBCarkindpriceService;
import com.zzb.cm.service.INSBCommonWorkflowTrackservice;
import com.zzb.cm.service.INSBFilelibraryService;
import com.zzb.cm.service.INSBInsuredhisService;
import com.zzb.cm.service.INSBInsuresupplyparamService;
import com.zzb.cm.service.INSBOrderService;
import com.zzb.cm.service.INSBPersonService;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.cm.service.INSBQuotetotalinfoService;
import com.zzb.cm.service.INSBUnderwritingService;
import com.zzb.conf.component.SupplementCache;
import com.zzb.conf.dao.INSBChannelDao;
import com.zzb.conf.dao.INSBOrderdeliveryDao;
import com.zzb.conf.dao.INSBOrderpaymentDao;
import com.zzb.conf.dao.INSBPaychannelDao;
import com.zzb.conf.dao.INSBPaychannelmanagerDao;
import com.zzb.conf.entity.INSBAgreement;
import com.zzb.conf.entity.INSBCertification;
import com.zzb.conf.entity.INSBDistributiontype;
import com.zzb.conf.entity.INSBOperatorcomment;
import com.zzb.conf.entity.INSBOrderdelivery;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.entity.INSBPaychannel;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBRegion;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.entity.INSBWorkflowmaintrack;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBAgreementService;
import com.zzb.conf.service.INSBAutoconfigshowService;
import com.zzb.conf.service.INSBCertificationService;
import com.zzb.conf.service.INSBDistributiontypeService;
import com.zzb.conf.service.INSBGroupmembersService;
import com.zzb.conf.service.INSBOperatorcommentService;
import com.zzb.conf.service.INSBOrderdeliveryService;
import com.zzb.conf.service.INSBPaychannelService;
import com.zzb.conf.service.INSBPolicyitemService;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.conf.service.INSBWorkflowmaintrackService;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.conf.service.INSBWorkflowsubtrackService;
import com.zzb.mobile.model.CommonModel;

import net.sf.json.JSONObject;

/**
 * CM系统 订单管理 
 */
@Controller
@RequestMapping("/business/ordermanage/*")  
public class INSBBusinessOrderManageController extends BaseController {	
	@Resource
	private INSBPaychannelService insbPaychannelService;
	@Resource
	private INSBOrderpaymentDao insbOrderpaymentDao;
	@Resource
	private INSBCarinfoService insbCarinfoService;
    @Resource
    private INSBCarinfohisService insbCarinfohisService;
	@Resource
	private INSBUnderwritingService insbUnderwritingService;
	@Resource
	private INSBOrderService insbOrderService;
	@Resource
	private INSBPersonService insbPersonService;
	@Resource
	private INSBQuotetotalinfoService insbQuotetotalinfoService;
	@Resource
	private INSBCarkindpriceService insbCarkindpriceService;
	@Resource
	private INSBWorkflowmainService workflowmainService;
	@Resource
	private INSBOrderdeliveryService insbOrderdeliveryService;
	@Resource
	private INSBAgentService insbAgentService;
	@Resource
	private INSBProviderService insbProviderService;
	@Resource
	private INSBPolicyitemService policyitemService;
	@Resource
	private INSBWorkflowsubtrackService insbWorkflowsubtrackService;
	@Resource
	private INSCCodeService inscCodeService;
	@Resource
	private INSBWorkflowmaintrackService insbWorkflowmaintrackService;
	@Resource
	private INSBUsercommentService insbUsercommentService;
	@Resource
	private INSBOperatorcommentService insbOperatorcommentService;
	@Resource
	private INSBCommonWorkflowTrackservice insbCommonWorkflowTrackservice;
	@Resource
	private DispatchService dispatchService;
	@Resource
	private INSBFilelibraryService insbFilelibraryService;
	@Resource
	private INSBQuoteinfoService insbQuoteinfoService;
	@Resource
	private INSCUserService inscUserService;
	@Resource
	private INSBCertificationService certificationService;
	@Resource
	private INSBPaychannelDao insbPaychannelDao;
	@Resource
	private INSBPaychannelmanagerDao insbPaychannelmanagerDao;
	@Resource 
	private InterFaceService interFaceService;
	@Resource 
	private INSCDeptService inscDeptService;
	@Resource
	private INSBOrderdeliveryDao insbOrderdeliveryDao;
	@Resource
	private INSBWorkflowsubService insbWorkflowsubService;
	@Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
	@Resource
	private INSBAutoconfigshowService insbAutoconfigshowService;
	@Resource
	private INSBGroupmembersService  insbGroupmembersService;
	@Resource
	private INSBInsuredhisService insuredhisService;
	@Resource
	private INSBInsuresupplyparamService insbInsuresupplyparamService;
	@Resource
	private INSBAgreementService insbAgreementService;
	@Resource
	private INSBDistributiontypeService insbDistributiontypeService ;
	@Resource
	private INSBChannelDao insbChannelDao;
	/**
	 * 跳转订单管理页面liuchao
	 * @param session
	 */
	@RequestMapping(value = "ordermanagelist", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView ordermanagelist(HttpSession session) throws ControllerException {
		ModelAndView mav = new ModelAndView("cm/ordermanage/orderManage");
		return mav;
	}
	
	/**
	 * 更新订单管理子页面方法liuchao
	 * @param session
	 */
	@RequestMapping(value = "QueryOrdermanagelist", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView queryOrdermanagelist(HttpSession session, OrderManageVO01 myTaskVo01) throws ControllerException {
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		ModelAndView mav = new ModelAndView("cm/ordermanage/myOrderManage");
		Map<String,Object> result = insbOrderService.allSelectOrderManageCode(myTaskVo01, loginUser);
		mav.addObject("orderManage", result);
		return mav;
	}
	
	/**
	 * 认证任务认领方法liuchao
	 * @param session
	 */
	@RequestMapping(value = "getCertificationTask", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getCertificationTask(HttpSession session, String cfTaskId) throws ControllerException {
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("认证任务认领操作人=="+loginUser);
		return insbOrderService.getCertificationTask(loginUser.getUsercode(), cfTaskId);
	}
	
	/**
	 * 车险任务认领方法liuchao
	 * @param session
	 */
	@RequestMapping(value = "getCarInsureTask", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getCarInsureTask(HttpSession session, String maininstanceId,
			String subInstanceId, String inscomcode, String mainOrsub,String statu) throws ControllerException {
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		if(TaskConst.PAYING_20.equals(statu)&&!insbGroupmembersService.queryUserGroupPrivileges(loginUser.getId(), "9")){//支付任务，先判断是否有权限认领
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("status", "fail");
			resultMap.put("msg", "申请任务失败，业管没有执行权限！");
			return resultMap;
		}
		LogUtil.info("车险任务认领操作人"+loginUser);
		return insbOrderService.getCarInsureTask(maininstanceId, subInstanceId, inscomcode, mainOrsub, loginUser);
	}
	/**
	 * 支付任务承保查询
	 * @param taskid
	 * @param inscomcode
	 * @param subInstanceId
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "payunderwriteSearch", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> payunderwriteSearch(String taskid,String inscomcode,String subInstanceId,HttpSession session) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			//获得按创建时间倒序排序的核保节点轨迹列表，下标为0的是最后核保途径
			List<String> quoteList = insbOrderService.getQuoteBackTrackStr(subInstanceId);
			String RequestMap = null;
			JSONObject reObj = null;
			if(quoteList!=null && quoteList.size()>0){
				LogUtil.info("%s支付任务承保查询获取核保节点轨迹列表 大小%s,操作人==%s", taskid, quoteList.size(), operator.getUsercode());
				if("3".equals(quoteList.get(0))){//edi报价
					RequestMap = interFaceService.goToEdiQuote(taskid, inscomcode, "admin", "approvedquery@qrcode");
					reObj = JSONObject.fromObject(RequestMap);
					if(reObj.getBoolean("result")){
						result.put("status", "success");
						result.put("msg", "已发EDI承保查询任务，请等候......");
					}else{
						result.put("status", "fail");
						result.put("msg", "自动化查询失败");
					}
				}else if("4".equals(quoteList.get(0))){//精灵报价
					RequestMap = interFaceService.goToFairyQuote(taskid, inscomcode, "admin", "approvedquery@qrcode");
					reObj = JSONObject.fromObject(RequestMap);
					if(reObj.getBoolean("result")){
						result.put("status", "success");
						result.put("msg", "已发精灵承保查询任务，请等候......");
					}else{
						result.put("status", "fail");
						result.put("msg", "自动化查询失败");
					}
				}else{//未知核保途径
					result.put("status", "fail");
					result.put("msg", "此任务是通过未知承保途径，不支持承保查询！");
				}
			}else{//没有查询到核保途径
				result.put("status", "fail");
				result.put("msg", "该供应商无自动化能力");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "fail");
			result.put("msg", "自动化查询异常失败");
			return result;
		}
		
	}
	/**
	 * 【 支付任务重新发起  已修改的】核保回写liuchao
	 */
	@RequestMapping(value = "reUnderwriting", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> reUnderwriting(String maininstanceId, String subInstanceId, 
			String inscomcode,int underCount,String ciPolicyNo,String biPolicyNo) throws ControllerException {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
            //检验保单号信息
            result = policyitemService.verifyPolicyno(ciPolicyNo,biPolicyNo,maininstanceId,inscomcode);
            String verifyResult = result.get("status")+"";
            if ("fail".equals(verifyResult)){
                return result;
            }
			/**
			 * hxx  判断 是否具有 核保能力 具有报价能力的精灵/EDI 一定具有核保能力，有核保能力的不一定具有报价能力
			 */
			INSBQuoteinfo quoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(maininstanceId, inscomcode);
			String deptid =quoteinfo.getDeptcode();//出单网点
			Map<String, Object> deptmap = new HashMap<String, Object>();
			deptmap.put("providerid", inscomcode);
			deptmap.put("conftype", "02");
			List<String> tempDeptIds = insbAutoconfigshowService.getParentDeptIds4Show(deptid);
			deptmap.put("deptList", tempDeptIds);
			List<String> quotetypes = insbAutoconfigshowService.queryByProId(deptmap);
			String RequestMap = null;
			JSONObject reObj = null;
			if(quotetypes.isEmpty()){
				result.put("status", "fail");
				result.put("msg", "此任务没有回写能力，请确认后重试！");
				return result;
			}
			
			if(quotetypes.contains("02")&&quotetypes.contains("01")){
				if(underCount ==1){
					RequestMap = interFaceService.goToFairyQuote(maininstanceId, inscomcode, "admin", "insurequery");
					reObj = JSONObject.fromObject(RequestMap);
					if(reObj.getBoolean("result")){
						result.put("status", "success");
						result.put("msg", "已发精灵核保查询任务，请等候......");
					}else{
						result.put("status", "fail");
						result.put("msg", "系统故障，请联系管理员");
					}
					
				}else if(underCount ==2){
					RequestMap = interFaceService.goToEdiQuote(maininstanceId, inscomcode, "admin", "insurequery");
					reObj = JSONObject.fromObject(RequestMap);
					if(reObj.getBoolean("result")){
						result.put("status", "success");
						result.put("msg", "已发EDI核保查询任务，请等候......");
					}else{
						result.put("status", "fail");
						result.put("msg", "系统故障，请联系管理员");
					}
				}
			}else if(quotetypes.contains("02")&&underCount ==1){
				RequestMap = interFaceService.goToFairyQuote(maininstanceId, inscomcode, "admin", "insurequery");
				reObj = JSONObject.fromObject(RequestMap);
				if(reObj.getBoolean("result")){
					result.put("status", "success");
					result.put("msg", "已发精灵核保查询任务，请等候......");
				}else{
					result.put("status", "fail");
					result.put("msg", "系统故障，请联系管理员");
				}
			}else if(quotetypes.contains("01")&&underCount ==1){
				RequestMap = interFaceService.goToEdiQuote(maininstanceId, inscomcode, "admin", "insurequery");
				reObj = JSONObject.fromObject(RequestMap);
				if(reObj.getBoolean("result")){
					result.put("status", "success");
					result.put("msg", "已发EDI核保查询任务，请等候......");
				}else{
					result.put("status", "fail");
					result.put("msg", "系统故障，请联系管理员");
				}
			}else{//未知核保途径
					result.put("status", "fail");
					result.put("msg", "此任务是通过未知核保途径，不支持核保回写！");
				}
			return result;
		} catch (Exception e) {
			result.put("status", "fail");
			result.put("msg", "系统故障，请联系管理员");
			e.printStackTrace();
			return result;
		}
	}
	
	private Map<String, Object> loopReQuoteBack(String maininstanceId, String subInstanceId, 
			String inscomcode,int underCount) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			/**
			 * hxx  判断 是否具有 核保能力 具有报价能力的精灵/EDI 一定具有核保能力，有核保能力的不一定具有报价能力
			 */
			INSBQuoteinfo quoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(maininstanceId, inscomcode);
			String deptid =quoteinfo.getDeptcode();//出单网点
			Map<String, Object> deptmap = new HashMap<String, Object>();
			deptmap.put("providerid", inscomcode);
//			deptmap.put("deptid", deptid);
			deptmap.put("conftype", "02");
			
			List<String> tempDeptIds = insbAutoconfigshowService.getParentDeptIds4Show(deptid);
			deptmap.put("deptList", tempDeptIds);
			List<String> quotetypes = insbAutoconfigshowService.queryByProId(deptmap);
			String RequestMap = null;
			JSONObject reObj = null;
			if(quotetypes.isEmpty()){
				result.put("status", "fail");
				result.put("msg", "此任务没有回写能力，请确认后重试！");
				return result;
			}
			if(quotetypes.contains("02")){//精灵报价
				RequestMap = interFaceService.goToFairyQuote(maininstanceId, inscomcode, "admin", "quotequery");
				reObj = JSONObject.fromObject(RequestMap);
				if(reObj.getBoolean("result")){
					result.put("status", "success");
					result.put("msg", "已发精灵报价查询任务，请等候......");
					return result;
				}else{
					result.put("status", "fail");
					result.put("msg", "系统故障，请联系管理员");
				}
			}else if(quotetypes.contains("01")){//edi报价
				RequestMap = interFaceService.goToEdiQuote(maininstanceId, inscomcode, "admin", "quotequery");
				reObj = JSONObject.fromObject(RequestMap);
				if(reObj.getBoolean("result")){
					result.put("status", "success");
					result.put("msg", "已发EDI报价查询任务，请等候......");
					return result;
				}else{
					result.put("status", "fail");
					result.put("msg", "系统故障，请联系管理员");
				}
			}else{//未知核保途径
				result.put("status", "fail");
				result.put("msg", "此任务是通过未知报价途径，不支持报价回写！");
			}
			return result;
		} catch (Exception e) {
			result.put("status", "fail");
			result.put("msg", "系统故障，请联系管理员");
			e.printStackTrace();
			return result;
		}
	}
	
	@RequestMapping(value = "reQuoteBack", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> reQuoteBack(String maininstanceId, String subInstanceId, 
			String inscomcode,int underCount) throws ControllerException {
		Map<String, Object> result = new HashMap<String, Object>();
		// 查询是否为非费改地区
		boolean isfeeflag = insbOrderService.isFeeflagArea(maininstanceId, null);
		if( !isfeeflag ) {
			// 是非费改地区
			List<INSBWorkflowsub> insbWorkflowsubList = insbWorkflowsubService.selectSubModelByMainInstanceId(maininstanceId);
			for( INSBWorkflowsub workflowsub : insbWorkflowsubList ) {
				String subinstanceId = workflowsub.getInstanceid();
				INSBQuoteinfo insbQuoteinfo = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(subinstanceId);
				String insComcode = insbQuoteinfo.getInscomcode();
				try {
					taskthreadPool4workflow.execute(new Runnable() {
						@Override
						public void run() {
							loopReQuoteBack(maininstanceId, subinstanceId, insComcode, underCount);
						}
					});
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			result.put("status", "success");
			result.put("msg", "后台正在发起全部报价回写，请等候......");
			return result;
		} else {
			return loopReQuoteBack(maininstanceId, subInstanceId, inscomcode,underCount);
		}
	}
	
	// 临时待支付页面显示链接
	@RequestMapping(value = "underpaidding", method = RequestMethod.GET)
	public ModelAndView underpaidding(String taskid, String taskcode,HttpSession session,String inscomcode,String frompage)
			throws ControllerException {
		ModelAndView mav = new ModelAndView("cm/ordermanage/underpaidding");
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		Map<String, Object> temp = new HashMap<String, Object>();
		List<Map<String, Object>> carInsTaskInfoList = new ArrayList<Map<String,Object>>();
		mav.addObject("taskcode", taskcode);
		mav.addObject("taskid", taskid);
		
		//List<String> inscomcodeList = insbQuotetotalinfoService.getInscomcodeListByInstanceId(taskid);
		//String inscomcode ;	
		//代理人信息
		Map<String, Object> agentInfo = insbAgentService.getCarTaskAgentInfo(taskid,"DETAIL");//代理人信息
		mav.addObject("agentInfo", agentInfo);

		// 添加任务摘要信息
		Map<String, Object> tasksummary = insbOrderService.getTaskSummary(taskid,inscomcode);
		mav.addObject("tasksummary", tasksummary);

		// 添加车辆信息
		Map<String, Object> carInfo = insbCarinfoService.getCarTaskCarInfo(taskid, inscomcode, "SHOW");//车辆信息
		temp.put("carInfo", carInfo);

		// 添加关系人信息
		Map<String, Object> relationPersonInfo = insbPersonService.getCarTaskRelationPersonInfo(taskid,inscomcode, "show");
		temp.put("relationPersonInfo", relationPersonInfo);
		
		//北京流程 支付，二支节点显示被保人身份证信息
		INSCDept dept = new INSCDept();
		dept.setComcode(operator.getUserorganization());
		dept = inscDeptService.queryOne(dept);
		if(dept != null){
			INSCDept userDept = inscDeptService.getPlatformDept(dept.getId());
			if(userDept != null && (ConstUtil.PLATFORM_BEIJING_DEPTCODE.equals(userDept.getComcode()))){
				if ("20".equals(taskcode) || "21".equals(taskcode)) {
					mav.addObject("showbbrIDinfo", true);
				}
			}
		}

		// 添加其他信息
		Map<String, Object> otherInfo = insbCarinfoService.getCarTaskOtherInfo(taskid,inscomcode, "show");
		temp.put("otherInfo", otherInfo);
		
		// 添加保险配置信息
		Map<String,Object> insConfigInfo = insbCarkindpriceService.getCarInsConfigByInscomcode(inscomcode, taskid);
		temp.put("insConfigInfo",insConfigInfo);
		
		// 添加投保单号信息
		//Map<String, Object> proposalInfo = policyitemService.getPolicyNumInfo(taskid);
		Map<String, Object> proposalInfo = policyitemService.getPolicyNumInfo2(taskid,inscomcode);
		mav.addObject("proposalInfo", proposalInfo);
		
		//查询主流程轨迹
		List<Map<String, Object>> workflowinfoList=insbCommonWorkflowTrackservice.getWorkFlowTrack(taskid);
		INSBWorkflowsubtrack workflowsubtrack = insbWorkflowsubtrackService.getSubTrackByInscomcode(taskid, inscomcode);
		temp.put("subInstanceId",workflowsubtrack.getInstanceid());
		insbWorkflowsubtrackService.addTrackdetail(workflowsubtrack,operator.getUsercode());//线下支付处理加打开处理轨迹
		// 添加支付信息
		Map<String, Object> paymentinfo = insbOrderService.getPaymentinfo(taskid,inscomcode);
		if(paymentinfo!=null && !"".equals(paymentinfo)){		
			mav.addObject("paymentinfo", paymentinfo);
			if(paymentinfo.get("paymentmethodid")!=null && !"".equals(paymentinfo.get("paymentmethodid"))){
				String paymentmethod =(String) paymentinfo.get("paymentmethodid");
				INSBPaychannel pc = insbPaychannelService.queryById(paymentmethod);
				if(pc!=null){
						mav.addObject("paytype",pc.getPaychannelname());
				 }
				INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
				insbOrderpayment.setTaskid(taskid);
				insbOrderpayment.setSubinstanceid(workflowsubtrack.getInstanceid());
				insbOrderpayment = insbOrderpaymentDao.selectOneByCode(insbOrderpayment);
				if(insbOrderpayment!=null ){
					mav.addObject("insbOrderpayment",insbOrderpayment);
				}
			}
		}		
		// 添加保费信息
		mav.addObject("premiumInfo",insbCarkindpriceService.getPremiumInfo(taskid,inscomcode));
		temp.put("taskid", taskid);
		
		// 添加备注信息
		INSBUsercomment dqusercomment = insbUsercommentService.selectUserCommentByTrackid(workflowsubtrack.getId(), 2);
		 //当前节点之前的备注信息（入参     instanceid： 主流程实例id ; inscomcode 保险公司code ; dqtaskcode 当前节点code）
//	    List<INSBUsercomment> usercomment = insbUsercommentService.getNearestUserComment(taskid, inscomcode, workflowsubtrack.getTaskcode());
		List<Map<String, Object>> usercomment = insbUsercommentService.getNearestUserCommentAndType(taskid, inscomcode);
		List<INSBFilelibrary> imageList = getImageList(taskid, workflowsubtrack);
		//List<INSBOperatorcomment> operatorcommentList = insbOperatorcommentService.selectOperatorCommentByTrackid(workflowsubtrack.getId(), 2);
		List<INSBOperatorcomment> operatorcommentList = insbOperatorcommentService.getOperatorCommentByMaininstanceid(taskid, inscomcode);
		Map<String, Object> remark = new HashMap<String, Object>();//备注信息
		if(usercomment!=null && usercomment.size()>0){
			remark.put("usercomment", usercomment.get(0));//当前节点之前的最新一条备注
		}
		remark.put("dqusercomment", dqusercomment);
		remark.put("opcommentList", operatorcommentList);
		temp.put("usercomment", usercomment);
		temp.put("remarkinfo", remark);
		temp.put("inscomcode", inscomcode);
		temp.put("imageList", imageList);

		//核保补充数据项
		temp.put("supplyParams", insbInsuresupplyparamService.getParamsByTask(taskid, inscomcode, false));

		carInsTaskInfoList.add(temp);
		mav.addObject("inscomcode",inscomcode);
		mav.addObject("carInsTaskInfoList",carInsTaskInfoList);
		for (Map<String, Object> map : workflowinfoList) {
			//信息录入
			if("1".equals(map.get("taskcode"))){
				//String instanceid = (String)map.get("instanceid");
				Map<String,Object>  AgentQuotetotal=insbQuotetotalinfoService.getAgentInfoByTaskId(taskid);
				if(AgentQuotetotal!=null){
					map.putAll(AgentQuotetotal);
				}
			}else if("2".equals(map.get("taskcode"))){
				map.put("QuoteInfo",insbWorkflowsubtrackService.getQuoteInfo(taskid));
			}else{
	//			Map<String,Object> userInfo=insbWorkflowmaintrackService.getUserInfo(taskid);
	//			map.putAll(userInfo);
			}
		}
		mav.addObject("workflowinfoList",workflowinfoList);
		mav.addObject("taskid",taskid);
		mav.addObject("inscomcode",inscomcode);
		mav.addObject("frompage", frompage);
		return mav;
	}

	/**
	 * 待承保打单页面显示链接
	 * 
	 */ 
	@RequestMapping(value = "underwriting", method = RequestMethod.GET)
//	@Cm4WorkflowFilter(taskType="chengbao")
	public ModelAndView underwriting(HttpSession session,String taskid,String taskcode,String inscomcode,String frompage)throws ControllerException {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		ModelAndView mav = new ModelAndView("cm/ordermanage/underwriting");
		Map<String, Object> agentInfo = insbAgentService.getCarTaskAgentInfo(taskid,"COMMON");//代理人信息
		mav.addObject("agentInfo",agentInfo);
		List<Map<String, Object>> carInsTaskInfoList = new ArrayList<Map<String,Object>>();
		//String inscomcode = insbQuotetotalinfoService.getInscomcodeListByInstanceId(taskid).get(0);
		Map<String, Object> temp = new HashMap<String, Object>();
		Map<String, Object> carInfo = insbCarinfoService.getCarTaskCarInfo(taskid, inscomcode, "SHOW");//车辆信息
		Map<String, Object> relationPersonInfo = insbPersonService.getCarTaskRelationPersonInfo(taskid,inscomcode, "SHOW");
		Map<String, Object> otherInfo = insbCarinfoService.getCarTaskOtherInfo(taskid,inscomcode, "show");
		Map<String,Object> insConfigInfo = insbCarkindpriceService.getCarInsConfigByInscomcode(inscomcode, taskid);
		INSBWorkflowsubtrack workflowsubtrack = insbWorkflowsubtrackService.getSubTrackByInscomcode(taskid, inscomcode);
		INSBWorkflowmaintrack workflowmaintrack = insbWorkflowmaintrackService.getMainTrackByInscomcode(taskid);
		insbWorkflowmaintrackService.addTrackdetail(workflowmaintrack,operator.getUsercode());
		INSBUsercomment dqusercomment = null;
//		List<INSBUsercomment> userComment = null;
		List<Map<String, Object>> userComment = null;
		List<INSBOperatorcomment> operatorcommentList = new ArrayList<INSBOperatorcomment>();
		if(workflowmaintrack!=null){
//			userComment = insbUsercommentService.getNearestUserComment(taskid, inscomcode, workflowmaintrack.getTaskcode());
			userComment = insbUsercommentService.getNearestUserCommentAndType(taskid, inscomcode);
			dqusercomment = insbUsercommentService.selectUserCommentByTrackid(workflowmaintrack.getId(), 1);
//			operatorcommentList = insbOperatorcommentService.selectOperatorCommentByTrackid(workflowmaintrack.getId(), 1);
			operatorcommentList = insbOperatorcommentService.getOperatorCommentByMaininstanceid(taskid, inscomcode);
		}
		List<INSBFilelibrary> imageList = getImageList(taskid, workflowsubtrack);
		Map<String, Object> remark = new HashMap<String, Object>();//备注信息
		remark.put("usercomment", (userComment!=null&&userComment.size()>0)?userComment.get(0):null);
		remark.put("dqusercomment", dqusercomment);
		remark.put("opcommentList", operatorcommentList);
		temp.put("usercomment", userComment);
		temp.put("subInstanceId",workflowsubtrack.getInstanceid());
		temp.put("insConfigInfo",insConfigInfo);
		temp.put("inscomcode", inscomcode);
		temp.put("carInfo", carInfo);
		temp.put("relationPersonInfo", relationPersonInfo);
		temp.put("otherInfo", otherInfo);
		temp.put("imageList", imageList);
		temp.put("remarkinfo", remark);
		Map<String, Object> proposalInfo = policyitemService.getPolicyNumInfo2(taskid,inscomcode);
		temp.put("proposalInfo",proposalInfo);

		//核保补充数据项
		temp.put("supplyParams", insbInsuresupplyparamService.getParamsByTask(taskid, inscomcode, false));

		carInsTaskInfoList.add(temp);
		mav.addObject("taskid", taskid);
		mav.addObject("taskcode", taskcode);
		mav.addObject("showEditFlag", "0");
		mav.addObject("inscomcode", inscomcode);
		mav.addObject("carInsTaskInfoList",carInsTaskInfoList);		
		// 添加任务摘要信息
//		Map<String, Object> tasksummary = insbOrderService.getTaskSummary(taskid);
//		mav.addObject("tasksummary", tasksummary);
		// 添加保单号信息
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("taskid", taskid);
		map1.put("inscomcode", inscomcode);
		List<INSBPolicyitem> policyitemList = policyitemService.selectByInscomTask(map1);
		Map<String,Object> policyInfo = new HashMap<String, Object>();
		if(policyitemList!=null&&policyitemList.size()>0){
			for (int i = 0; i < policyitemList.size(); i++) {
				if("0".equals(policyitemList.get(i).getRisktype())){
					policyInfo.put("businessPolicyNum", policyitemList.get(i).getPolicyno());
				}else if("1".equals(policyitemList.get(i).getRisktype())){
					policyInfo.put("strongPolicyNum", policyitemList.get(i).getPolicyno());
				}
			}
		}
		mav.addObject("policyInfo", policyInfo);
		// 添加支付信息
		mav.addObject("paymentinfo", insbOrderService.getPaymentinfo(taskid,inscomcode));
		// 添加保费信息
		mav.addObject("premiumInfo",insbCarkindpriceService.getPremiumInfo(taskid,inscomcode));
//==========配送信息=================================================================================
		//配送信息
		INSBOrderdelivery orderdelivery = new INSBOrderdelivery();
		orderdelivery.setTaskid(taskid);
		orderdelivery.setProviderid(inscomcode);
		orderdelivery = insbOrderdeliveryService.queryOne(orderdelivery);
		Map<String,Object> orderdeliveryinfo = new HashMap<String,Object>();
//				orderdeliveryinfo.put("deliverytype","自取");  //先默认是自取
		//获取配送方式codeList
//		INSCCode deliveryTypeCode = new INSCCode();
//		deliveryTypeCode.setCodetype("deliveType");
//		deliveryTypeCode.setParentcode("deliveType");
//		List<INSCCode> deliveryTypeList = inscCodeService.queryList(deliveryTypeCode);
		List<INSCCode> deliveryTypeList = getDeliveryType(taskid, inscomcode);
		mav.addObject("deliveryTypeList", deliveryTypeList);//配送方式集合
		if(orderdelivery!=null){
			INSBProvider provider = new INSBProvider();
			provider = insbProviderService.queryByPrvcode(orderdelivery.getProviderid());
			orderdeliveryinfo.put("orderfromid",provider.getChanneltype());   //订单来源编号
			INSCCode code = new INSCCode();
			code.setCodetype("Channel");
			code.setParentcode("Channel");
			if(!StringUtils.isEmpty(provider.getChanneltype())){
				code.setCodevalue(provider.getChanneltype());
				code = inscCodeService.queryOne(code);
				orderdeliveryinfo.put("orderfrom",code.getCodename());   //订单来源
			}else{
				orderdeliveryinfo.put("orderfrom","未知");   //订单来源
			}
			orderdeliveryinfo.put("deliveryside", orderdelivery.getDeliveryside());//配送方 默认保网
			orderdeliveryinfo.put("deliverytype",orderdelivery.getDelivetype());
			//收件人姓名
			orderdeliveryinfo.put("recipientname", orderdelivery.getRecipientname());
			//收件人手机号
			orderdeliveryinfo.put("recipientmobilephone", orderdelivery.getRecipientmobilephone());
			//详细地址
			Map<String, String> params = new HashMap<String, String>();
			params.put("mainInstanceId", taskid);
			params.put("orderId", orderdelivery.getOrderid());
			orderdeliveryinfo.put("recipientaddress", insbOrderdeliveryDao.getOrderdeliveryAddress(params));
//					orderdeliveryinfo.put("recipientaddress", orderdelivery.getRecipientaddress());
			//邮编
			orderdeliveryinfo.put("zip", orderdelivery.getZip());
		}
		mav.addObject("orderdeliveryinfo",orderdeliveryinfo);
		//出单网点
		INSBQuoteinfo quote = new INSBQuoteinfo();
		INSBQuotetotalinfo quotetotal = new INSBQuotetotalinfo();
		quotetotal.setTaskid(taskid);
		quotetotal = insbQuotetotalinfoService.queryOne(quotetotal);
		quote.setQuotetotalinfoid(quotetotal.getId());
		quote.setInscomcode(inscomcode);
		quote = insbQuoteinfoService.queryOne(quote);
		INSCDept dept = new INSCDept();
		dept.setComcode(quote.getDeptcode());
		dept = inscDeptService.queryOne(dept);
		if(dept!=null){
			mav.addObject("deptInfo", dept);
		}else{
			mav.addObject("deptInfo", new INSCDept());
		}
		//物流公司集合
		List<INSCCode> comList = inscCodeService.queryINSCCodeByCode("logisticscompany", "logisticscompany");
		System.out.println(comList);
		mav.addObject("comList",comList);
//=================================================================================================
		// 查询主流程轨迹
		List<Map<String, Object>> workflowinfoList=insbCommonWorkflowTrackservice.getWorkFlowTrack(taskid);
		mav.addObject("workflowinfoList",workflowinfoList);	

		//1是edi报价
		if("1".equals(quote.getTaskstatus())){
			mav.addObject("isReunw", "1");  
		}else if("2".equals(quote.getTaskstatus())){
			String res = workflowmainService.getContractcbType(taskid, inscomcode, "1", "contract");//2 精灵报价
			JSONObject jsonO = JSONObject.fromObject(res);
			if("2".equals(jsonO.getString("quotecode"))){
				mav.addObject("isReunw", "2");
			}else{
				mav.addObject("isReunw", "0");
			}
		}else{
			mav.addObject("isReunw", "0");
		}
		mav.addObject("frompage", frompage);
		return mav;
	}
	
	private List<INSBFilelibrary> getImageList(String taskid,INSBWorkflowsubtrack workflowsubtrack) {
		List<INSBFilelibrary> imageList = insbFilelibraryService.queryByFilebusinessCode(taskid);
		return imageList;
	}
	
	@RequestMapping(value = "undwrtsuccess", method = RequestMethod.GET)
	@ResponseBody
	@Cm4WorkflowFilter(taskType="chengbao")
	public String undwrtSuccess(HttpSession session,String taskid,String taskcode,String inscomcode){
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		String userid = operator.getUsercode();
		String message = insbUnderwritingService.undwrtSuccess(taskid,userid,taskcode,inscomcode);

		Map<String, String> map = new HashMap<String, String>();
		if( "success".equals(message) ) {
			map.put("status", "success");
		} else {
			map.put("status", "fail");
			map.put("msg", message);
		}
		return JSONObject.fromObject(map).toString();
	}

	@RequestMapping(value = "undwrtsearch", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> undwrtSearch(String taskid,String inscomcode,String subInstanceId,int underCount,HttpSession session) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			//获得按创建时间倒序排序的核保节点轨迹列表，下标为0的是最后核保途径
			List<String> quoteList = insbOrderService.getQuoteBackTrackStr(subInstanceId);
			LogUtil.info("%s获取核保节点轨迹列表,操作人==%s",taskid,operator.getUsercode());
			String RequestMap = null;
			JSONObject reObj = null;
			if(underCount > quoteList.size()){
				result.put("status", "fail");
				result.put("msg", "重新查询承保结果失败");
				return result;
			}
			if(quoteList!=null && quoteList.size()>0){
				if("3".equals(quoteList.get(underCount-1))){//edi报价
					RequestMap = interFaceService.goToEdiQuote(taskid, inscomcode, "admin", "approvedquery");
					reObj = JSONObject.fromObject(RequestMap);
					if(reObj.getBoolean("result")){
						result.put("status", "success");
						result.put("msg", "已发EDI承保查询任务，请等候......");
					}else{
						result.put("status", "fail");
						result.put("msg", "自动化查询失败，已转人工");
					}
				}else if("4".equals(quoteList.get(underCount-1))){//精灵报价
					RequestMap = interFaceService.goToFairyQuote(taskid, inscomcode, "admin", "approvedquery");
					reObj = JSONObject.fromObject(RequestMap);
					if(reObj.getBoolean("result")){
						result.put("status", "success");
						result.put("msg", "已发精灵承保查询任务，请等候......");
					}else{
						result.put("status", "fail");
						result.put("msg", "自动化查询失败，已转人工");
					}
				}else{//未知核保途径
					result.put("status", "fail");
					result.put("msg", "此任务是通过未知承保途径，不支持承保查询！");
				}
			}else{//没有查询到核保途径
				result.put("status", "fail");
				result.put("msg", "此任务没有查询到精灵或edi承保记录，请确认后重试！");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "fail");
			result.put("msg", "自动化查询失败，已转人工");
			return result;
		}
		
	}
	
	@RequestMapping(value = "searchPolicyItem", method = RequestMethod.POST)
	@ResponseBody
	public String searchPolicyItem(@RequestBody Map<String, Object> map) {
		List<INSBPolicyitem> policyitemList = policyitemService.selectByInscomTask(map);
		Map<String,Object> policyInfo = new HashMap<String, Object>();
		policyInfo.put("businessPolicyNum", "");
		policyInfo.put("strongPolicyNum", "");
		String[] policystatus = {"9","9"};
		if(policyitemList!=null && policyitemList.size()>0){
			for (int i = 0; i < policyitemList.size(); i++) {
				if("0".equals(policyitemList.get(i).getRisktype())){//商业险
					policyInfo.put("businessPolicyNum", policyitemList.get(i).getPolicyno()==null?"":policyitemList.get(i).getPolicyno());
				}else if("1".equals(policyitemList.get(i).getRisktype())){//交强险
					policyInfo.put("strongPolicyNum", policyitemList.get(i).getPolicyno()==null?"":policyitemList.get(i).getPolicyno());
				}
				if(policyitemList.get(i).getPolicystatus()!=null){
					policystatus[i] =  policyitemList.get(i).getPolicystatus();
				}
			}
		}
		policyInfo.put("policystatus", "有效");
		for(int j=0;j<policyitemList.size();j++){
			if(!policystatus[j].equals("1")){
				policyInfo.put("policystatus", "未生效");
				break ;
			}
		}
		policyInfo.put("status", "success");
		return JSONObject.fromObject(policyInfo).toString();
	}
	
	
	// 待配送页面显示链接
	@RequestMapping(value = "delivery", method = RequestMethod.GET)
	public ModelAndView delivery(String taskid,String taskcode,String inscomcode,String frompage,HttpSession session)
			throws ControllerException {
		ModelAndView mav = new ModelAndView("cm/ordermanage/delivery");
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		Map<String, Object> temp = new HashMap<String, Object>();
		List<Map<String, Object>> carInsTaskInfoList = new ArrayList<Map<String,Object>>();
		mav.addObject("taskid", taskid);
		mav.addObject("taskcode", taskcode);
		mav.addObject("inscomcode", inscomcode);
		// 添加任务摘要信息
		Map<String, Object> tasksummary = insbOrderService.getTaskSummary(taskid,inscomcode);
		mav.addObject("tasksummary", tasksummary);
		//代理人信息
		Map<String, Object> agentInfo = insbAgentService.getCarTaskAgentInfo(taskid,"DETAIL");
		mav.addObject("agentInfo", agentInfo);
		//车辆信息
		Map<String, Object> carInfo = insbCarinfoService.getCarTaskCarInfo(taskid, inscomcode, "SHOW");
		temp.put("carInfo", carInfo);
		//子流程
		INSBWorkflowsubtrack workflowsubtrack = insbWorkflowsubtrackService.getSubTrackByInscomcode(taskid, inscomcode);
	    INSBWorkflowmaintrack workflowmaintrack = insbWorkflowmaintrackService.getMainTrackByInscomcode(taskid);
	    insbWorkflowmaintrackService.addTrackdetail(workflowmaintrack,operator.getUsercode());
		//用户备注信息，第二个参数（1主流程轨迹、 2子流程轨迹）
//	    List<INSBUsercomment> usercomment = insbUsercommentService.getNearestUserComment(taskid, inscomcode, workflowmaintrack.getTaskcode());
		List<Map<String, Object>> usercomment = insbUsercommentService.getNearestUserCommentAndType(taskid, inscomcode);
	    INSBUsercomment dqusercomment = insbUsercommentService.selectUserCommentByTrackid(workflowmaintrack.getId(), 1);
	  
		List<INSBFilelibrary> imageList = getImageList(taskid, workflowsubtrack);
		//List<INSBOperatorcomment> operatorcommentList = insbOperatorcommentService.selectOperatorCommentByTrackid(workflowsubtrack.getId(), 2);
		List<INSBOperatorcomment> operatorcommentList = insbOperatorcommentService.getOperatorCommentByMaininstanceid(taskid, inscomcode);
		Map<String, Object> remark = new HashMap<String, Object>();//备注信息
		if(usercomment!=null && usercomment.size()>0){
			remark.put("usercomment", usercomment.get(0));//当前节点之前的最新一条备注
		}
		remark.put("dqusercomment", dqusercomment);
		remark.put("opcommentList", operatorcommentList);
		temp.put("remarkinfo", remark);
		temp.put("inscomcode", inscomcode);
		temp.put("imageList", imageList);

		//核保补充数据项
		temp.put("supplyParams", insbInsuresupplyparamService.getParamsByTask(taskid, inscomcode, false));

		carInsTaskInfoList.add(temp);
		
		mav.addObject("carInsTaskInfoList",carInsTaskInfoList);
		// 添加保单号信息
		//Map<String, Object> policyInfo =  policyitemService.getPolicyNumInfo(taskid);
		Map<String, Object> policyInfo = policyitemService.getPolicyNumInfo2(taskid,inscomcode);
		mav.addObject("policyInfo", policyInfo);
		// 添加支付信息
		Map<String, Object> paymentinfo = insbOrderService.getPaymentinfo(taskid,inscomcode);
		mav.addObject("paymentinfo", paymentinfo);
		//添加保费
		mav.addObject("premiumInfo",insbCarkindpriceService.getPremiumInfo(taskid,inscomcode));
		//配送信息
		INSBOrderdelivery orderdelivery = new INSBOrderdelivery();
		orderdelivery.setTaskid(taskid);
		orderdelivery.setProviderid(inscomcode);
		orderdelivery = insbOrderdeliveryService.queryOne(orderdelivery);
		Map<String,Object> orderdeliveryinfo = new HashMap<String,Object>();
//		orderdeliveryinfo.put("deliverytype","自取");  //先默认是自取
		//获取配送方式codeList
//		INSCCode deliveryTypeCode = new INSCCode();
//		deliveryTypeCode.setCodetype("deliveType");
//		deliveryTypeCode.setParentcode("deliveType");
//		List<INSCCode> deliveryTypeList = inscCodeService.queryList(deliveryTypeCode);
		List<INSCCode> deliveryTypeList = getDeliveryType(taskid, inscomcode);
		mav.addObject("deliveryTypeList", deliveryTypeList);//配送方式集合
		if(orderdelivery!=null){
            orderdeliveryinfo.put("deliverytype",orderdelivery.getDelivetype());
            orderdeliveryinfo.put("logisticscompany",orderdelivery.getLogisticscompany());
            orderdeliveryinfo.put("tracenumber",orderdelivery.getTracenumber());

			INSBProvider provider = insbProviderService.queryByPrvcode(orderdelivery.getProviderid());
			orderdeliveryinfo.put("orderfromid",provider.getChanneltype());   //订单来源编号

			if(!StringUtils.isEmpty(provider.getChanneltype())){
                INSCCode code = new INSCCode();
                code.setCodetype("Channel");
                code.setParentcode("Channel");
				code.setCodevalue(provider.getChanneltype());
				code = inscCodeService.queryOne(code);
				orderdeliveryinfo.put("orderfrom",code.getCodename());   //订单来源
			}else{
				orderdeliveryinfo.put("orderfrom","未知");   //订单来源
			}

			orderdeliveryinfo.put("deliveryside", orderdelivery.getDeliveryside());//配送方 默认保网
			//收件人姓名
			orderdeliveryinfo.put("recipientname", orderdelivery.getRecipientname());
			//收件人手机号
			orderdeliveryinfo.put("recipientmobilephone", orderdelivery.getRecipientmobilephone());
			//详细地址
			Map<String, String> params = new HashMap<String, String>();
			params.put("mainInstanceId", taskid);
			params.put("orderId", orderdelivery.getOrderid());
			orderdeliveryinfo.put("recipientaddress", insbOrderdeliveryDao.getOrderdeliveryAddress(params));
			//邮编
			orderdeliveryinfo.put("zip", orderdelivery.getZip());
		}
		
		//出单网点+外加渠道寄件人信息
		INSBQuoteinfo quote = new INSBQuoteinfo();
		INSBQuotetotalinfo quotetotal = new INSBQuotetotalinfo();
		quotetotal.setTaskid(taskid);
		quotetotal = insbQuotetotalinfoService.queryOne(quotetotal);
		String channelcode = quotetotal.getPurchaserChannel();
		if(StringUtil.isNotEmpty(channelcode)) {

			List<Map<String, String>> senderList = insbChannelDao.selectSenderInfoByCityAndChannelcode(quotetotal.getInscitycode(), channelcode);
			for(Map<String, String> senderMap:senderList ) {


				if( "1".equals(senderMap.get("isdefined"))) {
					orderdeliveryinfo.put("senderaddress", senderMap.get("senderaddress"));
					orderdeliveryinfo.put("senderchannel", senderMap.get("senderchannel"));
					orderdeliveryinfo.put("sendername", senderMap.get("sendername"));
					orderdeliveryinfo.put("senderphone", senderMap.get("senderphone"));
				}else if("1".equals(senderMap.get("pisdefined"))) {
					orderdeliveryinfo.put("senderaddress", senderMap.get("paddress"));
					orderdeliveryinfo.put("senderchannel", senderMap.get("pchannel"));
					orderdeliveryinfo.put("sendername", senderMap.get("pname"));
					orderdeliveryinfo.put("senderphone", senderMap.get("pphone"));

				}
			}
		}
		mav.addObject("orderdeliveryinfo",orderdeliveryinfo);
		quote.setQuotetotalinfoid(quotetotal.getId());
		quote.setInscomcode(inscomcode);
		quote = insbQuoteinfoService.queryOne(quote);
		INSCDept dept = new INSCDept();
		dept.setComcode(quote.getDeptcode());
		dept = inscDeptService.queryOne(dept);
		if(dept!=null){
			mav.addObject("deptInfo", dept);
		}else{
			mav.addObject("deptInfo", new INSCDept());
		}

		//物流公司集合
		List<INSCCode> comList = inscCodeService.queryINSCCodeByCode("logisticscompany", "logisticscompany");
		mav.addObject("comList",comList);
		//是否修改标记
		mav.addObject("showEditFlag", "0");  //是否显示修改功能，0--不显示，1--显示
		//查询主流程轨迹
		List<Map<String, Object>> workflowinfoList=insbCommonWorkflowTrackservice.getWorkFlowTrack(taskid);
		mav.addObject("workflowinfoList",workflowinfoList);	
		mav.addObject("frompage", frompage);
		return mav;
	}

	// 下拉框查询demo
	@RequestMapping(value = "showinscoms", method = RequestMethod.GET)
	@ResponseBody
	public String showInscoms(
			@RequestParam(value = "page", required = false) int page,
			@ModelAttribute Params para, @ModelAttribute INSBCarinfo carinfo)
			throws ControllerException {
		Map<String, Object> map = BeanUtils.toMap(carinfo, para);
		String result = insbCarinfoService.showMytaskInfo(map, page);
		return result;
	}
	
	/**
	 * 跳转修改配送信息
	 */
	@RequestMapping(value = "preEditDeliveryInfo", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView preEditDeliveryInfo(String taskid,String inscomcode){
		ModelAndView mav = new ModelAndView("cm/ordermanage/editDeliveryInfo");
		INSBOrderdelivery orderDelivery = new INSBOrderdelivery();
		orderDelivery.setTaskid(taskid);
		orderDelivery.setProviderid(inscomcode);
		orderDelivery = insbOrderdeliveryService.queryOne(orderDelivery);
		List<INSBRegion> province =  insbOrderdeliveryDao.getRegionByParentId("0");
		if(orderDelivery!=null){
			mav.addObject("orderDelivery",orderDelivery);
			List<INSBRegion> city = insbOrderdeliveryDao.getRegionByParentId(orderDelivery.getRecipientprovince());
			List<INSBRegion> area = insbOrderdeliveryDao.getRegionByParentId(orderDelivery.getRecipientcity());
			mav.addObject("city",city);
			mav.addObject("area",area);
		}else{
			mav.addObject("orderDelivery",new INSBOrderdelivery());
		}
		mav.addObject("province",province);
		mav.addObject("taskid",taskid);
		mav.addObject("inscomcode",inscomcode);
		return mav;
	}
	
	/**
	 * 修改配送信息
	 */
	@RequestMapping(value = "editDeliveryInfo", method = RequestMethod.POST)
	@ResponseBody
	public String editDeliveryInfo(DeliveryInfoItem deliveryinfo,HttpSession session){
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		try{
			LogUtil.info("--修改配送信息操作人=="+operator.getUsercode());
			return insbOrderService.editDeliveryInfo(deliveryinfo, operator);
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
		
	}
	/*
	 * 查询省市区的信息
	 */
	@RequestMapping(value = "getRegionInfo", method = RequestMethod.GET)
	@ResponseBody
	public List<INSBRegion> getRegionInfo(String parentid) {
		return insbOrderdeliveryDao.getRegionByParentId(parentid);
	}
	
	/*
	 * 配送成功接口
	 * MediumPaymentVo 这个类，其实是算是一个通用的类，因为工作流部分接口需要的数据基本都有
	 */
	@RequestMapping(value = "getDeliverySuccess", method = RequestMethod.POST)
	@ResponseBody
	public String getDeliverySuccess(@RequestBody MediumPaymentVo mediumPaymentVo,HttpSession session){
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("delivery--------"+mediumPaymentVo.getProcessinstanceid()+"操作人=="+operator.getUsercode());
		try{
			return insbOrderService.getDeliverySuccess(
					mediumPaymentVo.getProcessinstanceid(),operator.getUsercode(),mediumPaymentVo.getInscomcode(),mediumPaymentVo.getDeltype(),
					mediumPaymentVo.getCodevalue(),mediumPaymentVo.getTracenumber());
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
	}
	
	/*
	 * 支付成功or支付页面的重新核保 接口
	 */
	@RequestMapping(value = "getPaySuccess", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getPaySuccess(@RequestBody MediumPaymentVo mediumPaymentVo,HttpSession session){
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("paysuccess--------"+mediumPaymentVo.getProcessinstanceid()+"操作人=="+operator.getUsercode());
		if(!"2".equals(mediumPaymentVo.getIssecond())){//issecond=2则是重新核保，不用判断是否有支付权限。否则需要进入判断是否有相应的支付和二次支付权限操作
			if("0".equals(mediumPaymentVo.getIssecond())&&!insbGroupmembersService.queryUserGroupPrivileges(operator.getId(), "9")){//支付
				return "noright";
			}else if("1".equals(mediumPaymentVo.getIssecond())&&!insbGroupmembersService.queryUserGroupPrivileges(operator.getId(), "a")){//二次支付
				return "noright";
			}
		}
		try{
			if("0".equals(mediumPaymentVo.getUnderWritingFlag())){
				//任务分配前
				return insbOrderService.getPaySuccess(mediumPaymentVo.getProcessinstanceid(),mediumPaymentVo.getInscomcode(),mediumPaymentVo.getTaskid(),"admin",mediumPaymentVo.getIssecond());
			}else if("1".equals(mediumPaymentVo.getUnderWritingFlag())){
				//任务分配后
				return insbOrderService.getPaySuccess(mediumPaymentVo.getProcessinstanceid(),mediumPaymentVo.getInscomcode(),mediumPaymentVo.getTaskid(),operator.getUsercode(),mediumPaymentVo.getIssecond());
			}else{
				return "fail";
			}
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
	}
	
	/*
	 * 领取任务并支付成功or支付页面的重新核保 接口
	 */
	@RequestMapping(value = "getAndPaySuccess", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getAndPaySuccess(@RequestBody MediumPaymentVo mediumPaymentVo,HttpSession session){
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		//先查看业管账号有没有权限
		if(!insbGroupmembersService.queryUserGroupPrivileges(operator.getId(), "9")){
			LogUtil.info("该业管没有认领该支付任务的权限, 子任务id=="+mediumPaymentVo.getProcessinstanceid()+"操作人=="+operator.getUsercode());
			return "fail";
		}
		try {
			taskthreadPool4workflow.execute(new Runnable() {
				@Override
				public void run() {
					//认领任务
					LogUtil.info("车险任务认领操作人"+operator.getUsercode()+"主任务号"+mediumPaymentVo.getTaskid()+"子任务号"+mediumPaymentVo.getProcessinstanceid());
					insbOrderService.getCarInsureTask(mediumPaymentVo.getTaskid(), mediumPaymentVo.getProcessinstanceid(),mediumPaymentVo.getInscomcode() , "2", operator);
					//写入流程轨迹
					INSBWorkflowsubtrack workflowsubtrack = insbWorkflowsubtrackService.getSubTrackByInscomcode(mediumPaymentVo.getTaskid(), mediumPaymentVo.getInscomcode());
					insbWorkflowsubtrackService.addTrackdetail(workflowsubtrack,operator.getUsercode());//线下支付处理加打开处理轨迹
				}
			});
		} catch(Exception ex) {
			ex.printStackTrace();
			return "fail";
		}
		//推支付成功流程
		LogUtil.info("paysuccess--------"+mediumPaymentVo.getProcessinstanceid()+"操作人=="+operator.getUsercode());
		if(!"2".equals(mediumPaymentVo.getIssecond())){//issecond=2则是重新核保，不用判断是否有支付权限。否则需要进入判断是否有相应的支付和二次支付权限操作
			if("0".equals(mediumPaymentVo.getIssecond())&&!insbGroupmembersService.queryUserGroupPrivileges(operator.getId(), "9")){//支付
				return "noright";
			}else if("1".equals(mediumPaymentVo.getIssecond())&&!insbGroupmembersService.queryUserGroupPrivileges(operator.getId(), "a")){//二次支付
				return "noright";
			}
		}
		try{
			if("0".equals(mediumPaymentVo.getUnderWritingFlag())){
				//任务分配前
				return insbOrderService.getPaySuccess(mediumPaymentVo.getProcessinstanceid(),mediumPaymentVo.getInscomcode(),mediumPaymentVo.getTaskid(),"admin",mediumPaymentVo.getIssecond());
			}else if("1".equals(mediumPaymentVo.getUnderWritingFlag())){
				//任务分配后
				return insbOrderService.getPaySuccess(mediumPaymentVo.getProcessinstanceid(),mediumPaymentVo.getInscomcode(),mediumPaymentVo.getTaskid(),operator.getUsercode(),mediumPaymentVo.getIssecond());
			}else{
				return "fail";
			}
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
	}
	
	/*
	 * 人工录单成功接口
	 */
	@RequestMapping(value = "getManualRecording", method = RequestMethod.POST)
	@ResponseBody
	public String getManualRecording(@RequestBody MediumPaymentVo mediumPaymentVo,HttpSession session){
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("getManualRecording--------"+mediumPaymentVo.getProcessinstanceid()+"人工录单成功操作人=="+operator.getUsercode());
		return insbOrderService.ManualRecording(mediumPaymentVo.getProcessinstanceid(), mediumPaymentVo.getIsRebackWriting(),operator.getUsercode());
	}
	
	/*
	 * 人工录单 退回修改接口按钮
	 */
	@RequestMapping(value = "manualRecordingBack", method = RequestMethod.POST)
	@ResponseBody
	public String manualRecordingBack(@RequestBody MediumPaymentVo mediumPaymentVo,HttpSession session){
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("manualRecordingBack--------"+mediumPaymentVo.getProcessinstanceid()+"人工录单退回修改操作人=="+operator.getUsercode());
		return insbOrderService.ManualRecordingBack(mediumPaymentVo.getProcessinstanceid(),operator.getUsercode());
	}
	
	/*
	 * 人工录单 重新报价
	 */
	@RequestMapping(value = "manualRecordingRe", method = RequestMethod.POST)
	@ResponseBody
	public String manualRecordingRe(@RequestBody MediumPaymentVo mediumPaymentVo,HttpSession session){
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("manualRecordingRe--------"+mediumPaymentVo.getProcessinstanceid()+"人工录单重新报价操作人=="+operator.getUsercode());
		return insbOrderService.ManualRecordingRe(mediumPaymentVo.getProcessinstanceid(),operator.getUsercode());
	}
	
	/*
	 * 人工回写 完成接口
	 */
	@RequestMapping(value = "manualWriteBackSuccess", method = RequestMethod.POST)
	@ResponseBody
	public String manualWriteBackSuccess(@RequestBody MediumPaymentVo mediumPaymentVo,HttpSession session){
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("manualWriteBackSuccess--------"+mediumPaymentVo.getProcessinstanceid()+"人工回写完成操作人=="+operator.getUsercode());
		return insbOrderService.manualWriteBackSuccess(mediumPaymentVo.getProcessinstanceid(),operator.getUsercode());
	}
	
	/*
	 * 人工回写 退回修改接口
	 */
	@RequestMapping(value = "manualWriteBack", method = RequestMethod.POST)
	@ResponseBody
	public String manualWriteBack(@RequestBody MediumPaymentVo mediumPaymentVo,HttpSession session){
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("manualWriteBack--------"+mediumPaymentVo.getProcessinstanceid()+"人工回写退回修改操作人=="+operator.getUsercode());
		return insbOrderService.manualWriteBack(mediumPaymentVo.getProcessinstanceid(),operator.getUsercode());
	}
	
	/*
	 * 人工核保完成接口,processinstanceid 子流程id，taskid 主流程id
	 */
	@RequestMapping(value = "manualUnderWritingSuccess", method = RequestMethod.POST)
	@ResponseBody
	@Cm4WorkflowFilter(taskType="hebao")
	public CommonModel manualUnderWritingSuccess(HttpSession session, @RequestBody MediumPaymentVo mediumPaymentVo){
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("manualUnderWritingSuccess--------"+mediumPaymentVo.getProcessinstanceid()+"人工核保完成操作人=="+operator.getUsercode());
        CommonModel commonModel = new CommonModel();

		try{
            Map<String, Object> result = new HashMap<String, Object>();
            //检验保单号信息
            result = policyitemService.verifyPolicyno(mediumPaymentVo.getCiPolicyNo(),mediumPaymentVo.getBiPolicyNo(),
                    mediumPaymentVo.getTaskid(),mediumPaymentVo.getInscomcode());
            String verifyResult = result.get("status")+"";
            if ("fail".equals(verifyResult)){
                commonModel.setStatus("fail");
                commonModel.setMessage(result.get("msg")+"");
                return commonModel;
            }

			insbOrderService.manualUnderWritingSuccess(mediumPaymentVo.getProcessinstanceid(),mediumPaymentVo.getTaskid(),mediumPaymentVo.getInscomcode(),operator.getUsercode());
            commonModel.setStatus(CommonModel.STATUS_SUCCESS);

		}catch(Exception e){
			e.printStackTrace();
			LogUtil.info(mediumPaymentVo.getTaskid()+"manualUnderWritingError--------"+mediumPaymentVo.getProcessinstanceid());
			commonModel.setStatus(CommonModel.STATUS_FAIL);
            commonModel.setMessage(e.getLocalizedMessage());
		}
        return commonModel;
	}
	
	/*
	 * 人工核保退回修改接口
	 */
	@RequestMapping(value = "manualUnderWritingBack", method = RequestMethod.POST)
	@ResponseBody
	public String manualUnderWritingBack(@RequestBody MediumPaymentVo mediumPaymentVo,HttpSession session){
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("manualUnderWritingBack--------"+mediumPaymentVo.getProcessinstanceid()+"人工核保退回修改操作人=="+operator.getUsercode());
		String status =  insbOrderService.manualUnderWritingBack(mediumPaymentVo.getProcessinstanceid(),operator.getUsercode());
		return status;
	}

	/*
	 * 重新核保【核保回写】接口,processinstanceid 子流程id，taskid 主流程id
	 */
	@RequestMapping(value = "manualUnderWritingRestart", method = RequestMethod.POST)
	@ResponseBody
	public String manualUnderWritingRestart(@RequestBody MediumPaymentVo mediumPaymentVo,HttpSession session){
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
        LogUtil.info("manualUnderWritingRestart--------"+mediumPaymentVo.getProcessinstanceid()+"--重新核保操作人"+operator.getUsercode());
		return insbOrderService.manualUnderWritingRestart(mediumPaymentVo.getProcessinstanceid(),mediumPaymentVo.getTaskid(),mediumPaymentVo.getInscomcode(),operator.getUsercode());
	}
	
	/*
	 * 配送完成后，保存配送单号和物流公司等信息
	 */
	@RequestMapping(value = "saveTracenumber", method = RequestMethod.POST)
	@ResponseBody
	public String saveTracenumber(@RequestBody MediumPaymentVo mediumPaymentVo){
		LogUtil.info("saveTracenumber--------"+mediumPaymentVo.getProcessinstanceid()+","+mediumPaymentVo.getInscomcode()+","+mediumPaymentVo.getTracenumber());
		return insbOrderService.saveTraceNumber(mediumPaymentVo.getProcessinstanceid(), mediumPaymentVo.getTracenumber(), mediumPaymentVo.getCodevalue(),mediumPaymentVo.getInscomcode());
	}
	
	/**
	 * 跳转修改保单号
	 */
	@RequestMapping(value = "preEditPolicyNumber", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView preEditPolicyNumber(String taskid,String inscomcode){
		ModelAndView mav = new ModelAndView("cm/common/editPolicyNumber");
		Map<String,Object> map1 = new HashMap<String, Object>();
		map1.put("taskid", taskid);
		map1.put("inscomcode", inscomcode);
		Map<String,Object> policyInfo = new HashMap<String, Object>();
		List<INSBPolicyitem> policyitemList = policyitemService.selectByInscomTask(map1);
		boolean hasbusi = false;
		boolean hasstr = false;
		if(policyitemList!=null && policyitemList.size()>0){
			for (int i = 0; i < policyitemList.size(); i++) {
				if("0".equals(policyitemList.get(i).getRisktype())){//商业险
					hasbusi = true;
					policyInfo.put("businessPolicyNum", policyitemList.get(i).getPolicyno());
				}else if("1".equals(policyitemList.get(i).getRisktype())){//交强险
					hasstr = true;
					policyInfo.put("strongPolicyNum", policyitemList.get(i).getPolicyno());
				}
			}
			mav.addObject("hasstr",hasstr);
			mav.addObject("hasbusi",hasbusi);
		}
		mav.addObject("policyInfo", policyInfo);
		//mav.addObject("policyInfo",policyitemService.getPolicyNumInfo(taskid));
		mav.addObject("taskid",taskid);
		mav.addObject("inscomcode",inscomcode);
		return mav;
	}

	/**
	 * 跳转修改保单号 
	 */
	@RequestMapping(value = "preEditPolicyNumber1", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView preEditPolicyNumber1(String taskid,String inscomcode){
		ModelAndView mav = new ModelAndView("cm/common/editPolicyNumber1");
		Map<String,Object> map1 = new HashMap<String, Object>();
		map1.put("taskid", taskid);
		map1.put("inscomcode", inscomcode);
		Map<String,Object> policyInfo = new HashMap<String, Object>();
		List<INSBPolicyitem> policyitemList = policyitemService.selectByInscomTask(map1);
		boolean hasbusi = false;
		boolean hasstr = false;
		if(policyitemList!=null && policyitemList.size()>0){
			for (int i = 0; i < policyitemList.size(); i++) {
				if("0".equals(policyitemList.get(i).getRisktype())){//商业险
					hasbusi = true;
					policyInfo.put("businessPolicyNum", policyitemList.get(i).getPolicyno());
				}else if("1".equals(policyitemList.get(i).getRisktype())){//交强险
					hasstr = true;
					policyInfo.put("strongPolicyNum", policyitemList.get(i).getPolicyno());
				}
			}
			mav.addObject("hasstr",hasstr);
			mav.addObject("hasbusi",hasbusi);
		}
		mav.addObject("policyInfo", policyInfo);
		//mav.addObject("policyInfo",policyitemService.getPolicyNumInfo(taskid));
		mav.addObject("taskid",taskid);
		mav.addObject("inscomcode",inscomcode);
		return mav;
	}

	/**
	 * 修改保单号
	 */
	@RequestMapping(value = "editPolicyNumber", method = RequestMethod.GET)
	@ResponseBody
	public String editPolicyNumber(String cipolicyno,String bipolicyno,String taskid,String inscomcode){
		LogUtil.info("==修改单号=taskid=" + taskid + ",inscomcode=" + inscomcode + ",商业险投保单号改为：" + bipolicyno + ",交强险投保单号改为：" + cipolicyno);
		return policyitemService.updatePolicyNumInfo(cipolicyno,bipolicyno,taskid,inscomcode);
	}

	/**
	 * 修改保单号
	 */
	@RequestMapping(value = "editPolicyNumber1", method = RequestMethod.GET)
	@ResponseBody
	public String editPolicyNumber1(String cipolicyno,String bipolicyno,String taskid,String inscomcode){
		LogUtil.info("==修改单号1=taskid=" + taskid + ",inscomcode=" + inscomcode + ",商业险投保单号改为：" + bipolicyno + ",交强险投保单号改为：" + cipolicyno);
		return policyitemService.updatePolicyNumInfo1(cipolicyno,bipolicyno,taskid,inscomcode);
	}

	/**
	 * 跳转修改投保单号
	 */
	@RequestMapping(value = "preEditProposalNumber", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView preEditProposalNumber(String taskid,String inscomcode){
		ModelAndView mav = new ModelAndView("cm/common/editProposalNumber");
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("taskid", taskid);
		map.put("inscomcode", inscomcode);

        boolean hasbusi = false;//商业险标识
        boolean hasstr = false;//交强险标识

        //查询此任务中的保险公司已选择的险别记录
        INSBCarkindprice carkindprice = new INSBCarkindprice();
        carkindprice.setTaskid(taskid);
        carkindprice.setInscomcode(inscomcode);
        //查询商业险
        carkindprice.setInskindtype("0");
        List<INSBCarkindprice>  busiCarkindpriceList = insbCarkindpriceService.queryList(carkindprice);
        //快速续保
        INSBCarinfohis carinfohis = new INSBCarinfohis();
        carinfohis.setTaskid(taskid);
        carinfohis.setInscomcode(inscomcode);
        carinfohis = insbCarinfohisService.queryOne(carinfohis);
        //有新增的商业险信息或者快速续保勾选"与上年一致"
        if((busiCarkindpriceList!=null&&busiCarkindpriceList.size()>0)||
                (carinfohis!=null&&"1".equals(carinfohis.getInsureconfigsameaslastyear()))){
            hasbusi = true;
        }
        //查询交强险
        carkindprice.setInskindtype("2");
        List<INSBCarkindprice> strCarkindpriceList = insbCarkindpriceService.queryList(carkindprice);
        if(strCarkindpriceList!=null&&strCarkindpriceList.size()>0){
            hasstr = true;
        }

        //获取投保单信息
        List<INSBPolicyitem> list = policyitemService.selectByInscomTask(map);
        for (INSBPolicyitem temp : list) {
            if("0".equals(temp.getRisktype())&&hasbusi){
                mav.addObject("biProposalInfo",temp.getProposalformno());
                mav.addObject("biProposalId",temp.getId());
            }else if("1".equals(temp.getRisktype())&&hasstr){
                mav.addObject("ciProposalInfo",temp.getProposalformno());
                mav.addObject("ciProposalId",temp.getId());
            }
        }

		mav.addObject("hasstr", hasstr);
		mav.addObject("hasbusi", hasbusi);
		mav.addObject("taskid",taskid);
		mav.addObject("inscomcode",inscomcode);

//		INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
//		quotetotalinfo.setTaskid(taskid);
//		quotetotalinfo = insbQuotetotalinfoService.queryOne(quotetotalinfo);
//		if (quotetotalinfo != null) {
//			mav.addObject("isRenewal", "1".equals(quotetotalinfo.getIsrenewal()));
//		}

		return mav;
	}
	
	/**
	 *  跳转修改投保单号 second,wang
	 */
	@RequestMapping(value = "preEditProposalNumber2", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView preEditProposalNumber2(String taskid,String inscomcode,String num){
		ModelAndView mav = new ModelAndView("cm/common/editProposalNumber2");
		HashMap<String, Object> map = new HashMap<String,Object>();

		map.put("taskid", taskid);
		map.put("inscomcode", inscomcode);
		List<INSBPolicyitem> list = policyitemService.selectByInscomTask(map);
		boolean hasbusi = false;
		boolean hasstr = false;
		for (INSBPolicyitem temp : list) {
			if("0".equals(temp.getRisktype())){
				mav.addObject("biProposalInfo",temp.getProposalformno());
				mav.addObject("biProposalId",temp.getId());
				hasbusi = true;
			}else if("1".equals(temp.getRisktype())){
				mav.addObject("ciProposalInfo",temp.getProposalformno());
				mav.addObject("ciProposalId",temp.getId());
				hasstr = true;
			}
		}
		mav.addObject("hasstr", hasstr);
		mav.addObject("hasbusi", hasbusi);
		mav.addObject("taskid",taskid);
		mav.addObject("inscomcode",inscomcode);
		mav.addObject("num",num);
		return mav;
	}

	/**
	 * 修改投保单号
	 */
	@RequestMapping(value = "editProposalNumber", method = RequestMethod.GET)
	@ResponseBody
	public String editProposalNumber(String ciproposalno,String biproposalno,String inscomcode,String taskid){
		LogUtil.info("===修改单号=taskid=" + taskid + ",inscomcode=" + inscomcode + ",商业险投保单号改为：" + biproposalno +",交强险投保单号改为：" + ciproposalno);
        Map<String,Object> verifyMap = policyitemService.verifyPolicyno(ciproposalno,biproposalno,taskid,inscomcode);
        String verifyResult = verifyMap.get("status")+"";
        if ("fail".equals(verifyResult)){
            return JSONObject.fromObject(verifyMap).toString();
        }
		return policyitemService.updateProposalNumInfo(ciproposalno,biproposalno,taskid,inscomcode);
	}
	/**
	 * 跳转修改平台信息
	 */
	@RequestMapping(value = "editCommonPlatinfo", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView editCommonPlatinfo(String taskid,String inscomcode,String num){
		ModelAndView mav = new ModelAndView("cm/common/editCommonPlatinfo");
		Map<String,Object> map1 = new HashMap<String, Object>();
		map1.put("taskid", taskid);
		map1.put("inscomcode", inscomcode);
		//商业险理赔次数
	    Map<String, String> commercialClaimTimesMap = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(
                            CMRedisClient.getInstance().get(3, SupplementCache.MODULE_NAME, "application.commercialClaimTimes")), Map.class);
	    Map<String, String> compulsoryClaimTimesMap = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(
                            CMRedisClient.getInstance().get(3, SupplementCache.MODULE_NAME, "application.compulsoryClaimTimes")), Map.class);
	    Map<String, String> commercialProviderMap = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(
                CMRedisClient.getInstance().get(3, SupplementCache.MODULE_NAME, "application.lastInsureCo")), Map.class);
		List<Map<String, String>> commercialProviderMaplist = new ArrayList<Map<String, String>>();
		if(null!=compulsoryClaimTimesMap){
			HashMap<String,String> matedataMap;
			Iterator iterator = commercialProviderMap.entrySet().iterator();
	        while(iterator.hasNext()) {
	           	Map.Entry entry = (Entry) iterator.next();
	           	matedataMap = new HashMap<>();
	           	matedataMap.put("key", entry.getKey().toString());
	           	matedataMap.put("value", entry.getValue().toString());
	           	commercialProviderMaplist.add(matedataMap);
	        }
		}
		List<Map<String, String>> compulsorymatedataMaplist = new ArrayList<Map<String, String>>();
		if(null!=compulsoryClaimTimesMap){
			HashMap<String,String> matedataMap;
			Iterator iterator = compulsoryClaimTimesMap.entrySet().iterator();
	        while(iterator.hasNext()) {
	           	Map.Entry entry = (Entry) iterator.next();
	           	matedataMap = new HashMap<>();
	           	matedataMap.put("key", entry.getKey().toString());
	           	matedataMap.put("value", entry.getValue().toString());
	           	compulsorymatedataMaplist.add(matedataMap);
	        }
		}
		List<Map<String, String>> commercialmatedataMaplist = new ArrayList<Map<String, String>>();
		if(null!=commercialClaimTimesMap){
			HashMap<String,String> matedataMap;
			Iterator iterator = commercialClaimTimesMap.entrySet().iterator();
	        while(iterator.hasNext()) {
	           	Map.Entry entry = (Entry) iterator.next();
	           	matedataMap = new HashMap<>();
	           	matedataMap.put("key", entry.getKey().toString());
	           	matedataMap.put("value", entry.getValue().toString());
	           	commercialmatedataMaplist.add(matedataMap);
	        }
		}
		mav.addObject("commercialProviderMaplist", commercialProviderMaplist);
		mav.addObject("compulsorymatedataMaplist", compulsorymatedataMaplist);
		mav.addObject("commercialmatedataMaplist", commercialmatedataMaplist);
		mav.addObject("taskid",taskid);
		mav.addObject("num", num);
		mav.addObject("inscomcode",inscomcode);
		return mav;
	}
	//支付信息页面
	@RequestMapping(value = "editpaymentInfo", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView editpaymentInfo(PayInfoItem infoItem){
		ModelAndView mav=new ModelAndView("cm/common/paymentInfoItem");
		Map<String, String>IdAndName=new HashMap<String, String>();
		Map<String, Object> paymentinfo =insbOrderService.getPaymentinfo(infoItem.getTaskid(),infoItem.getInscomcode());
		List<String> channelIdList = insbPaychannelmanagerDao.selectPaychannelIdByAgreementId(infoItem.getDeptcode(), infoItem.getInscomcode());
		List<Map<String,String>> channelIdMapList=new ArrayList<Map<String,String>>();
		for(String id:channelIdList){
			INSBPaychannel insbPaychannel=new INSBPaychannel();
			insbPaychannel=insbPaychannelDao.selectById(id);
			IdAndName.put(id, insbPaychannel.getPaychannelname());
		}
		channelIdMapList.add(IdAndName);
		mav.addObject("paymethodlist", channelIdList);
		mav.addObject("channelIdMapList", channelIdMapList);
		if(!paymentinfo.isEmpty()){
		mav.addObject("paymentinfo", paymentinfo);
		mav.addObject("taskid",infoItem.getTaskid());
		mav.addObject("inscomcode",infoItem.getInscomcode());
		mav.addObject("subInstanceId",infoItem.getSubInstanceId());
		}else {
		mav.addObject("paymentinfo", paymentinfo);
		mav.addObject("taskid",infoItem.getTaskid());
		mav.addObject("inscomcode",infoItem.getInscomcode());
		mav.addObject("subInstanceId",infoItem.getSubInstanceId());
		}
		return mav;
		
	}

	//支付信息页面修改信息updatePaymentinfo(String taskid,String paymentmethod,String checkcode,String insurecono,String tradeno)
	@RequestMapping(value = "updatepaymentInfo", method = RequestMethod.POST)
	@ResponseBody
	public String updatepaymentInfo(PayInfoItem infoItem,HttpSession session){
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		try{
			return insbOrderService.updatePaymentinfo(infoItem.getTaskid(),infoItem.getInscomcode(),infoItem.getPaymentmethod(),
					infoItem.getCheckcode(),infoItem.getInsurecono(),infoItem.getTradeno(),infoItem.getSubInstanceId(),operator.getUsercode());
		}catch(Exception e){
			return "fail";
		}
	}

	//任务池页面，任务申请按钮，调用工作流
	@RequestMapping(value = "getWorkFlowMsg", method = RequestMethod.GET)
	@ResponseBody
	public String getWorkFlowMsg(HttpSession session,String taskid,String providerId,String taskcode){
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		
		String strResult=null;
		Integer integer=new Integer(taskcode);
		try {
			if(integer==999){
				INSBCertification certificationVo=new INSBCertification();
				certificationVo =certificationService.queryById(taskid);
				certificationVo.setDesignatedoperator(operator.getUsercode());
				certificationService.updateDesignatedoperator(certificationVo);
				//dispatchService.getTask(taskid, providerId, 1, operator, operator);
			}
			if(integer==20){
				INSCUser inscUser= inscUserService.getByUsercode("admin");
				INSBQuotetotalinfo insbQuotetotalinfo=new INSBQuotetotalinfo();
				insbQuotetotalinfo.setTaskid(taskid);
				insbQuotetotalinfo=insbQuotetotalinfoService.queryOne(insbQuotetotalinfo);
				INSBQuoteinfo insbQuoteinfo=new INSBQuoteinfo();
				insbQuoteinfo.setQuotetotalinfoid(insbQuotetotalinfo.getId());
				insbQuoteinfo.setInscomcode(providerId);
				insbQuoteinfo=insbQuoteinfoService.queryOne(insbQuoteinfo);
//				dispatchService.getTask(insbQuoteinfo.getWorkflowinstanceid(), providerId, 0, operator, operator);
				
				dispatchService.reassignment(insbQuoteinfo.getWorkflowinstanceid(), providerId, 2, inscUser, operator);
			}else{
				dispatchService.getTask(taskid, providerId, 1, operator, operator);
			}
			strResult="success";
			LogUtil.info("任务申请成功,操作人=="+operator.getUsercode());
		} catch (WorkFlowException e) {
			strResult="fail";
			e.printStackTrace();
		} catch (RedisException e) {
			strResult="fail";
			e.printStackTrace();
		}
		return strResult;
	}

	/*
	 * 获取核保回写结果
	 */
	@RequestMapping(value = "getUnderWritingResult", method = RequestMethod.GET)
	@ResponseBody
	public String getUnderWritingResult(MediumPaymentVo mediumPaymentVo){
		LogUtil.info("getUnderWritingResult--------");
		String str = insbOrderService.getUnderWritingResult(mediumPaymentVo.getTaskid(), mediumPaymentVo.getInscomcode());
		LogUtil.info("--------"+str);
		if(str==null){
			str = "false";
		}
		return str;
	}
	
	/*
	 * 获取重新查询承保
	 */
	@RequestMapping(value = "getUnderwResult", method = RequestMethod.GET)
	@ResponseBody
	public String getUnderwResult(MediumPaymentVo mediumPaymentVo){
		LogUtil.info("getUnderwResult--------");
		String str = insbOrderService.getUnderwResult(mediumPaymentVo.getTaskid(), mediumPaymentVo.getInscomcode());
		LogUtil.info("--------"+str);
		if(str==null){
			str = "false";
		}
		return str;
	}
	
	/*
	 * 获取报价回写结果
	 */
	@RequestMapping(value = "getreQuoteBackResult", method = RequestMethod.GET)
	@ResponseBody
	public String getreQuoteBackResult(MediumPaymentVo mediumPaymentVo){
		LogUtil.info("getreQuoteBackResult--------");
		String str = insbOrderService.getQuoteBackResult(mediumPaymentVo.getTaskid(), mediumPaymentVo.getInscomcode());
		LogUtil.info("--------"+str);
		if(str==null){
			str = "stop";
		}
		return str;
	}
	
	/**
	 * liuchao
	 * 人工核保页面核保轮询操作调用接口  
	 * 点击轮序按钮，调用工作流进入轮询节点，工作流调用成功，会调用轮询接口，页面直接回到我的任务页面，轮询接口返回结果后重新调用工作流接口
	 */
	@RequestMapping(value = "loopUnderwriting", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> loopUnderwriting(HttpSession session, String maininstanceId, String subInstanceId, String inscomcode) {
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("loopUnderwriting start.......%s,%s,%s,%s", maininstanceId, subInstanceId, inscomcode, loginUser.getUsercode());
		return insbWorkflowmaintrackService.loopUnderwriting(maininstanceId, subInstanceId, inscomcode, loginUser.getUsercode());
	}
	
	/**
	 * liuchao
	 * 人工核保推送到核保轮询节点后，工作流回调接口  
	 * 每5分钟调用核保回写一次，一共调用三次（间隔时间和调用次数可以配置），每次使用流程中最新的精灵或EDI核保途径发起核保回写，
	 * 根据最后核保轮询回写结果，调用工作流对应接口
	 */
	@RequestMapping(value = "toLoopUnderwriting", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> toLoopUnderwriting(@RequestBody LoopUnderWritingVO loopUnderWritingVo) {
		LogUtil.info("toDoLoopUnderwriting start,taskid=" + loopUnderWritingVo.getMainInstanceId() + ",subtaskid=" + loopUnderWritingVo.getSubInstanceId()
				+ ",inscomcode=" + loopUnderWritingVo.getInscomcode());

		try {
			taskthreadPool4workflow.execute(new Runnable() {
				@Override
				public void run() {
					Map<String, Object> result = insbWorkflowmaintrackService.toLoopUnderwriting(loopUnderWritingVo.getMainInstanceId(),
							loopUnderWritingVo.getSubInstanceId(), loopUnderWritingVo.getInscomcode(), null);

					LogUtil.info("toDoLoopUnderwriting taskid=" + loopUnderWritingVo.getMainInstanceId() +
							",subtaskid=" +loopUnderWritingVo.getSubInstanceId() + ",result="+JsonUtil.serialize(result));
				}
			});
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		Map<String, Object> result = new HashMap<>(2);
		result.put("status", "success");
		result.put("msg", "核保查询请求已接收");

		return result;
	}
	
	/**
	 * liuchao
	 * 核保回写轮询后处理回写结果接口，精灵或EDI核保回写后调用
	 */
	@RequestMapping(value = "loopUWResultHandler", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> loopUWResultHandler(@RequestBody UnderwritingResultVO underwritingResultVO) {
		LogUtil.info("loopUWResultHandler start......."+underwritingResultVO.toString());
		return insbWorkflowmaintrackService.loopUWResultHandler(underwritingResultVO.getSubInstanceId(),
				underwritingResultVO.getResult(), null, null, null);
	}

	/**
	 * 推轮询节点工作流
	 */
	@RequestMapping(value = "loopWorkFlowToNext", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> loopWorkFlowToNext(@RequestBody LoopUnderWritingVO loopUnderWritingVo) {
		LogUtil.info("loopWorkFlowToNext start......."+JsonUtil.serialize(loopUnderWritingVo));
		return insbWorkflowmaintrackService.loopWorkFlowToNext(loopUnderWritingVo.getMainInstanceId(), loopUnderWritingVo.getSubInstanceId(),
				loopUnderWritingVo.getInscomcode(), loopUnderWritingVo.getLoopStatus(), loopUnderWritingVo.getMsg());
	}

	/**
	 * 记录轮询日志详情
	 */
	@RequestMapping(value = "logLoopunderwritingdetail", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> logLoopunderwritingdetail(@RequestBody LoopUnderWritingVO loopUnderWritingVo) {
		LogUtil.info("logLoopunderwritingdetail start......."+JsonUtil.serialize(loopUnderWritingVo));
		String times = "", msg = "";

		if (StringUtil.isNotEmpty(loopUnderWritingVo.getMsg())) {
			String[] data = loopUnderWritingVo.getMsg().split("#");
			if (data != null) {
				if (data.length == 1) {
					times = data[0];
				} else if (data.length > 1) {
					times = data[0];
					msg = data[1];
				}
			}
		}

		return insbWorkflowmaintrackService.logLoopUnderWritingDetail(loopUnderWritingVo.getMainInstanceId(), loopUnderWritingVo.getSubInstanceId(),
				loopUnderWritingVo.getLoopStatus(), times+"#"+msg);
	}

	/**
	 * 更新轮询日志详情
	 */
	@RequestMapping(value = "updateLoopunderwritingdetail", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateLoopunderwritingdetail(@RequestBody LoopUnderWritingVO loopUnderWritingVo) {
		LogUtil.info("updateLoopunderwritingdetail start......."+JsonUtil.serialize(loopUnderWritingVo));

		return insbWorkflowmaintrackService.updateLoopUnderWritingDetail(loopUnderWritingVo.getMainInstanceId(), loopUnderWritingVo.getSubInstanceId(),
				loopUnderWritingVo.getInscomcode(), loopUnderWritingVo.getLoopStatus(), loopUnderWritingVo.getMsg());
	}
	
	/**
	 * liuchao
	 * 精灵或EDI核保回写http请求调用
	 * @throws Exception 
	 */
	@RequestMapping(value = "goToFairyOrEdiQuote", method = RequestMethod.GET, produces="text/html;charset=UTF-8")
	@ResponseBody
	public String goToFairyOrEdiQuote(UnderWtringLoopResultVO underWtringLoopResultVO) {
		LogUtil.info("goToFairyOrEdiQuote httpRequestStart......."+underWtringLoopResultVO.toString());
		Map<String, Object> result = new HashMap<String, Object>();
		String message = "";
		try {
			if("EDI".equals(underWtringLoopResultVO.getForediflag())){
				message = interFaceService.goToEdiQuote(underWtringLoopResultVO.getMaininstanceId(), 
						underWtringLoopResultVO.getInscomcode(), underWtringLoopResultVO.getUserCode(), 
						underWtringLoopResultVO.getBackFlag());
				result.put("status", "success");
				result.put("msg", message);
			}else if("fairy".equals(underWtringLoopResultVO.getForediflag())){
				message = interFaceService.goToFairyQuote(underWtringLoopResultVO.getMaininstanceId(), 
						underWtringLoopResultVO.getInscomcode(), underWtringLoopResultVO.getUserCode(), 
						underWtringLoopResultVO.getBackFlag());
				result.put("status", "success");
				result.put("msg", message);
			}else{
				result.put("status", "fail");
				result.put("msg", "EDI或fairy标记不匹配！");
			}
		} catch (Exception e) {
			result.put("status", "fail");
			result.put("msg", "服务器内部异常！");
			e.printStackTrace();
		}
		return JSONObject.fromObject(result).toString();
	}
	
	/**
	 * 承保打单配送按钮的请求调用
	 */
	@RequestMapping(value = "undwrtDeliverySuccess", method = RequestMethod.GET)
	@ResponseBody
	public String undwrtDeliverySuccess(HttpSession session,
			String inscomcode,String tracenumber,String comcodevalue,String deltype,
			String taskid,String taskcode){
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		String userid = operator.getUsercode();

		return insbOrderService.undwrtDeliverySuccess(taskid, userid, inscomcode, deltype, comcodevalue, tracenumber, taskcode);
	}
	
	/*
	 * 同时认领并完成自取任务成功 接口
	 */
	@RequestMapping(value = "batchDeliSuccess", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String batchDeliSuccess(@RequestBody MediumPaymentVo mediumPaymentVo,HttpSession session){
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("delivery--------"+mediumPaymentVo.getProcessinstanceid()+"操作人=="+operator.getUsercode());
		try{
			dispatchService.getTask(mediumPaymentVo.getProcessinstanceid(), mediumPaymentVo.getInscomcode(), 1, operator, operator);
			return insbOrderService.getDeliverySuccess(
					mediumPaymentVo.getProcessinstanceid(),operator.getUsercode(),mediumPaymentVo.getInscomcode(),mediumPaymentVo.getDeltype(),
					mediumPaymentVo.getCodevalue(),mediumPaymentVo.getTracenumber());
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
	}
	
	/**
	 * 跳转修改被保人身份证信息页面
	 */
	@RequestMapping(value = "preEditIdCardInfo", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView preEditIdCardInfo(HttpSession session, String taskid,String inscomcode){
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		ModelAndView mav = new ModelAndView("cm/common/editidcard");
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("taskid", taskid);
		map.put("inscomcode", inscomcode);
		
		INSBPerson person = null;
		INSBInsuredhis insbinsuredhis = new INSBInsuredhis();
		insbinsuredhis.setTaskid(taskid);
		insbinsuredhis.setInscomcode(inscomcode);
		insbinsuredhis = insuredhisService.queryOne(insbinsuredhis);
		if(insbinsuredhis == null){
			person = new INSBPerson();
			person.setOperator(operator.getUsercode());
			person.setCreatetime(new Date());
			insbPersonService.insert(person);
			insbinsuredhis = new INSBInsuredhis();
			insbinsuredhis.setOperator(operator.getUsercode());
			insbinsuredhis.setCreatetime(new Date());
			insbinsuredhis.setTaskid(taskid);
			insbinsuredhis.setInscomcode(inscomcode);
			insbinsuredhis.setPersonid(person.getId());
			insuredhisService.insert(insbinsuredhis);
		} else {
			person = insbPersonService.queryById(insbinsuredhis.getPersonid());
		}
		
		mav.addObject("taskid", taskid);
		mav.addObject("inscomcode", inscomcode);
		mav.addObject("name", person.getName());
		if(0==person.getIdcardtype()){		//证件类型是身份证、才做显示
			mav.addObject("idcardno", person.getIdcardno());
		}
		mav.addObject("sex", person.getGender());
		mav.addObject("nation", person.getNation());
		mav.addObject("birthday", StringUtil.isEmpty(person.getBirthday())? "": DateUtil.toString(person.getBirthday()));
		mav.addObject("address", person.getAddress());
		mav.addObject("authority",person.getAuthority());
		mav.addObject("expdate",person.getExpdate());
		mav.addObject("cellphone",person.getCellphone());
		mav.addObject("email",person.getEmail());
		mav.addObject("expstartdate",StringUtil.isEmpty(person.getExpstartdate())?"":DateUtil.toString(person.getExpstartdate()));
		mav.addObject("expenddate",StringUtil.isEmpty(person.getExpenddate())?"":DateUtil.toString(person.getExpenddate()));
		return mav;
	}
	
	/**
	 * cm支付、二支工作台申请验证码
	 * @param session
	 * @param taskid
	 * @param inscomcode
	 */
	@RequestMapping(value = "cmapplypin", produces="application/json;charset=UTF-8")
	@ResponseBody
	public String cmApplyPin(HttpSession session, String taskid, String inscomcode) throws ControllerException {
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		return insuredhisService.cmApplyPin(user, taskid, inscomcode);
	}
	
	/**
	 * cm支付、二支工作台保存被保人身份证信息
	 * 
	 */
	@RequestMapping(value = "saveidcardinfo")
	@ResponseBody
	public String saveIDCard(HttpSession session, String taskid, String inscomcode, String name, String idcardno,
			String sex, String nation, String birthday, String address, String authority, String expdate, String cellphone, String expstartdate, String expenddate, String email) throws ControllerException {
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("===CM支付、二支工作台修改被保人身份证信息=taskid=" + taskid + ",inscomcode=" + inscomcode +", operator=" + user.getUsercode());
		return insuredhisService.saveIDCardInfo(user, taskid, inscomcode, name, idcardno, sex, nation, birthday, address, authority, expdate, cellphone, expstartdate, expenddate, email);
	}

	/**
	 * 获取配送方式
	 * @param inscomcode 供应商code
	 * @return
	 */
	private List<INSCCode> getDeliveryType(String taskId, String inscomcode) {
		//获取供应商配置的协议配送方式
		String types = "";
		List<INSBDistributiontype> distritypeList = null;
		INSBAgreement agreement = insbAgreementService.getAgreementByTaskIdAndInscomcode(taskId, inscomcode);
		if (agreement != null) {
			String agreementId = agreement.getId();
			INSBDistributiontype distritype = new INSBDistributiontype();
			distritype.setAgreementid(agreementId);
			distritypeList = insbDistributiontypeService.queryList(distritype);
		}
		//获取配送方式codeList
		INSCCode deliveryTypeCode = new INSCCode();
		deliveryTypeCode.setCodetype("deliveType");
		deliveryTypeCode.setParentcode("deliveType");
		List<INSCCode> deliveryTypeList = inscCodeService.queryList(deliveryTypeCode);
		if (distritypeList != null && distritypeList.size() > 0) {
			//ps:协议配送方式表(码表code:distritype)与订单配送表(码表code:deliveType)中的配送方式所对应value不同，需转化
			for (INSBDistributiontype distritype : distritypeList) {
				String type = distritype.getDistritype().trim();
				if ("1".equals(type)) {//自取(协议配送方式表)
					types += "0";
				} else if ("2".equals(type)) {//快递(协议配送方式表)
					types += "1";
				} else {//电子保单(协议配送方式表)
					types += "3";
				}
			}
		}
		if (!"".equals(types)) {//若协议配送表有配置配送方式则按配置显示，否则显示全部配送方式
			for (int i = 0; i < deliveryTypeList.size(); ) {
				String str = deliveryTypeList.get(i).getCodevalue().trim();
				if (!types.contains(str)) {
					deliveryTypeList.remove(i);
				} else {
					i++;
				}
			}
		}
		return deliveryTypeList;
	}
}

