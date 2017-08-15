package com.zzb.cm.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.cninsure.core.utils.StringUtil;
import com.zzb.cm.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.cninsure.system.service.INSCDeptService;
import com.common.ConstUtil;
import com.common.PagingParams;
import com.common.TaskConst;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import com.zzb.cm.entity.INSBFlowerror;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.conf.entity.INSBOperatorcomment;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.entity.INSBPaychannel;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBBusinessmanagegroupService;
import com.zzb.conf.service.INSBOperatorcommentService;
import com.zzb.conf.service.INSBOrderpaymentService;
import com.zzb.conf.service.INSBPaychannelService;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.conf.service.INSBWorkflowmaintrackService;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.conf.service.INSBWorkflowsubtrackService;
import com.zzb.mobile.model.lastinsured.LastYearPolicyInfoBean;
/**
 * 车险任务管理
 */
@Controller
@RequestMapping("/business/cartaskmanage/*")
public class INSBBusinessCarTaskManageController extends BaseController {
    @Resource
	private INSBCarinfoService insbCarinfoService; 
	@Resource
	private INSBAgentService insbAgentService;
	@Resource
	private INSBPersonService insbPersonService;
	@Resource
	private INSBCarkindpriceService insbCarkindpriceService;
	@Resource
	private INSBWorkflowsubtrackService insbWorkflowsubtrackService;
	@Resource
	private INSBUsercommentService insbUsercommentService;
	@Resource
	private INSBWorkflowsubService insbWorkflowsubService;
	@Resource
	private INSCCodeService inscCodeService;
	@Resource
	private INSCDeptService inscDeptService;
	@Resource
	private INSBBusinessmanagegroupService businessmanagegroupService;
	@Resource 
	private INSBFlowerrorService insbFlowerrorService;
	@Resource 
	private INSBCarmodelinfoService insbCarmodelinfoService;
	@Resource
	private INSBOperatorcommentService insbOperatorcommentService;
	@Resource
	private INSBOrderpaymentService insbOrderpaymentService;
	@Resource
	private INSBPaychannelService insbPaychannelService;
	@Resource
	private INSBOrderService insbOrderService;
	@Resource
	private IRedisClient redisClient;
	@Resource
	private INSBInsuresupplyparamService insbInsuresupplyparamService;

	//车险任务页面任务类型标记对应的工作流程编码集合
	//任务类型A 报价
	private final String[] ATASKTYPELIST = {"2","3","4","6","7","8","13","14","31"};
	//任务类型B 核保
	private final String[] BTASKTYPELIST = {"17","18","19","16"};
	//任务类型C 支付
	private final String[] CTASKTYPELIST = {"20"};
	//任务类型D 二次支付
	private final String[] DTASKTYPELIST = {"21"};
	//任务类型E 承保打单
	private final String[] ETASKTYPELIST = {"28","23","25","26","27"};
	//任务类型F 配送
	private final String[] FTASKTYPELIST = {"24"};
	
	//跳转车险任务管理页面
	@RequestMapping(value="cartaskmanagelist", method=RequestMethod.GET)
	public ModelAndView toCarTaskManageListPage(HttpSession session){
		ModelAndView mav = new ModelAndView("cm/cartaskmanage/carTaskManage");
//		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
//		INSCUser temp = new INSCUser();
//		temp.setUserorganization(loginUser.getUserorganization());
//		mav.addObject("uList", inscUserService.queryList(temp));
		//添加车险任务管理页面任务类型下拉框选项 
		mav.addObject("workFlowNodeList", inscCodeService.getWorkFlowNodesForCarTaskQuery());
		return mav;
	}
	
	/**
	 * 生成机构树调用方法
	 */
	@RequestMapping(value="querydepttree",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> queryProList(@RequestParam(value="id",required=false) String parentcode) throws ControllerException{
		LogUtil.debug("parentcode=", parentcode);
		return inscDeptService.selectDeptTreeByParentCode(parentcode);
	}
	
	@RequestMapping(value="queryparttree",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> queryPartList(@RequestParam(value="id",required=false) String parentcode,HttpSession session) throws ControllerException{
		LogUtil.debug("parentcode=", parentcode);
		INSCUser loginUser=(INSCUser)session.getAttribute("insc_user");
		String userorganization=loginUser.getUserorganization();
		Map<String,String> params=new HashMap<String,String>();
		params.put("parentcode", parentcode);
		params.put("userorganization", userorganization);
		List<Map<String,Object>> list=inscDeptService.selectPartTreeByParentCode(params);
		return list;
	}

	@RequestMapping(value="queryparttreecheckall",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> queryparttreecheckall(@RequestParam(value="id",required=false) String parentcode,HttpSession session) throws ControllerException{
		LogUtil.debug("parentcode=", parentcode);
		INSCUser loginUser=(INSCUser)session.getAttribute("insc_user");
		String userorganization=loginUser.getUserorganization();
		Map<String,String> params=new HashMap<String,String>();
		params.put("parentcode", parentcode);
		params.put("userorganization", userorganization);
		List<Map<String,Object>> list=inscDeptService.selectPartTreeByParentCodeCheckAll(params);
		return list;
	}
	
	//车辆任务列表页面初始化加载数据和查询车辆任务数据
	@RequestMapping(value = "showcartaskmanagelist", method = RequestMethod.GET)
	@ResponseBody
	public String showCarTaskManageList(HttpSession session, @ModelAttribute PagingParams pagingParams, String carlicensenoOrinsuredname
			, String carlicenseno, String usertype, String insuredname, String tasktype, String taskstatus
			, String taskcreatetimeup, String taskcreatetimedown, String agentName, String agentNum, String ad_instanceId
			, String operatorname, String deptcode, String inscomcode ,String taskstate, String channelinnercode , String singleFlag) throws ControllerException{
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		
		INSCDept d = inscDeptService.getOrgDeptByDeptCode(loginUser.getUserorganization());
	
		//组织查询参数
		Map<String, Object> paramMap = BeanUtils.toMap(pagingParams);
		paramMap.put("carlicensenoOrinsuredname", carlicensenoOrinsuredname);//车牌号或被保人姓名
		paramMap.put("carlicenseno", carlicenseno);//车牌号
		paramMap.put("usertype", usertype);//用户类型
		paramMap.put("usercode", loginUser.getUsercode());//用户类型
		paramMap.put("insuredname", insuredname);//被保人姓名
		paramMap.put("taskstatus", taskstatus);//任务分配状态 ，1组分配中，2人分配中，3已分配到人
		paramMap.put("taskcreatetimeup", taskcreatetimeup);//任务创建时间上限
		paramMap.put("taskcreatetimedown", taskcreatetimedown);//任务创建时间下限
		paramMap.put("agentName", agentName);//代理人姓名
		paramMap.put("agentNum", agentNum);//代理人工号
		paramMap.put("ad_instanceId", ad_instanceId);//流程id
		paramMap.put("operatorname", operatorname);//操作人
		paramMap.put("userorganization", d.getDeptinnercode());//树形结构code
		paramMap.put("deptcode", deptcode);//出单网点机构id
		paramMap.put("inscomcode", inscomcode);//投保保险公司code
		paramMap.put("taskstate", taskstate);//订单状态 1待处理，2已完成
		paramMap.put("singleFlag", singleFlag);//优化查询按钮标记
		String tasktypeList = null;//任务类型集合
		if("A".equals(tasktype)){
			//tasktypeList = Arrays.asList(ATASKTYPELIST);
		}else if("B".equals(tasktype)){
			//tasktypeList = Arrays.asList(BTASKTYPELIST);
		}else if("C".equals(tasktype)){
			//tasktypeList = Arrays.asList(CTASKTYPELIST);
		}else if("D".equals(tasktype)){
			//tasktypeList = Arrays.asList(DTASKTYPELIST);
		}else if("E".equals(tasktype)){
			//tasktypeList = Arrays.asList(ETASKTYPELIST);
		}else if("F".equals(tasktype)){
			//tasktypeList = Arrays.asList(FTASKTYPELIST);
		}else if(tasktype != null){
			//tasktypeList = new ArrayList<String>();
			//解决部分关闭任务查询不出问题
			if(tasktype.equals("30"))
				tasktypeList= TaskConst.CLOSE_37; //30-拒绝承保关闭, 37-关闭
			tasktypeList = tasktype;
		}
		paramMap.put("tasktype", tasktypeList);
		paramMap.put("channelinnercode", channelinnercode);
		//表分区查询
		paramMap.put("platforminnercode", inscDeptService.getPlatformInnercode(d.getDeptinnercode()));
	    return insbCarinfoService.getJSONOfCarTaskListByMap(paramMap);

	}

	//得到任务详情数据
	@RequestMapping(value = "showcartaskdetail", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView showCarTaskDetail(HttpSession session, String maininstanceid, String inscomcode, String subinstanceid, String taskcode){
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		ModelAndView mav = new ModelAndView("cm/cartaskmanage/carTaskDetail");
		//返回页面数据
		Map<String, Object> agentInfo = insbAgentService.getCarTaskAgentInfo(maininstanceid,"DETAIL");//代理人信息
		Map<String, Object> carInfo = insbCarinfoService.getCarTaskCarInfo(maininstanceid, inscomcode, "SHOW");//车辆信息
		Map<String, Object> carmodelinfo = insbCarmodelinfoService.getCarmodelInfoByCarinfoId(maininstanceid,(String)carInfo.get("carinfoId"),inscomcode,"CARINFODIALOG");
		Map<String, Object> relationPersonInfo = insbPersonService.getCarTaskRelationPersonInfo(maininstanceid, inscomcode , "SHOW");//关系人信息
		Map<String, Object> otherInfo = insbCarinfoService.getCarTaskOtherInfo(maininstanceid, inscomcode, "SHOW");//其他信息
		Map<String, Object> rulequery = insbCarinfoService.getCarInfoByTaskId(maininstanceid);//平台规则查询信息
		
		INSBOrder order=insbOrderService.getOneOrderByTaskIdAndInscomcode(maininstanceid, inscomcode);
		INSBWorkflowsubtrack workflowsubtrack = insbWorkflowsubtrackService.getSubTrackByInscomcode(maininstanceid, inscomcode);
		INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
		insbOrderpayment.setTaskid(maininstanceid);
		if(subinstanceid!=null && !"null".equals(subinstanceid)){
			if(maininstanceid.equals(subinstanceid)){
				INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();
				insbWorkflowsub.setMaininstanceid(maininstanceid);
				insbWorkflowsub.setTaskcode("33");
				insbWorkflowsub = insbWorkflowsubService.queryOne(insbWorkflowsub);
				if(insbWorkflowsub!=null && insbWorkflowsub.getInstanceid()!=null){
					insbOrderpayment.setSubinstanceid(insbWorkflowsub.getInstanceid());
				}				
			}else{
				insbOrderpayment.setSubinstanceid(subinstanceid);
			}
		}		
		List<INSBOrderpayment> payInfos = insbOrderpaymentService.queryList(insbOrderpayment);//zhifu信息	
		List<INSBOrderpayment> payInfoss = new ArrayList<INSBOrderpayment>();
		if(payInfos!=null){
			for (INSBOrderpayment payment : payInfos) {
				INSBOrderpayment payments = new INSBOrderpayment();
				payments.setAmount(payment.getAmount());
				payments.setPaymentransaction(payment.getPaymentransaction());
				payments.setCreatetime(payment.getCreatetime());
				payments.setTradeno(payment.getTradeno());
				payments.setCurrencycode(payment.getCurrencycode());
				payments.setPayflowno(payment.getPayflowno());
				payments.setPayorderno(order.getOrderno());//订单编号
				if(payment.getPaychannelid()!=null && !"".equals(payment.getPaychannelid())){
					INSBPaychannel paychannel = insbPaychannelService.queryById(payment.getPaychannelid());
					String channelId = payment.getPaychannelid();
					payments.setPaychannelid(channelId);
					payments.setPaychannelname(paychannel.getPaychannelname());
					//28分期保，30银联
					if(channelId.equals("28") || channelId.equals("30")){
						//获得调用接口url(from config)
						payments.setSecpayorderrequrl(insbOrderService.getQuerySecondPayURL(channelId));
						//任务类型
						payments.setTaskcode(taskcode);
					}
				}
				if(payment.getRefundtype()!=null && !"".equals(payment.getRefundtype())){
					if("02".equals(payment.getRefundtype())){
						payments.setRefundtype("差额退款");
					}else {
						payments.setRefundtype("全额退款");
					}
				}
				if(payment.getPayresult()!=null && !"".equals(payment.getPayresult())){
					if("01".equals(payment.getPayresult())){
						payments.setPayresult("未支付");
					}else if("02".equals(payment.getPayresult())){
						payments.setPayresult("支付成功");
					}else if("03".equals(payment.getPayresult())){
						payments.setPayresult("退款");
					}else if("04".equals(payment.getPayresult())){
						payments.setPayresult("已过期");
					}else if("05".equals(payment.getPayresult())){
						payments.setPayresult("支付失败");
					}else{
						payments.setPayresult(payment.getPayresult());
					}
					//9555 【8月】【准生产环境】在CM执行修改支付方式之后，支付流程中会多出一条支付状态为“00”的支付记录
					//http://pm.baoxian.in/zentao/bug-view-9555.html
					if ("00".equals(payment.getPayresult())) continue;
				}
				if(subinstanceid.equals(payment.getSubinstanceid()) || maininstanceid.equals(subinstanceid)){//是当前报价公司，子流程实例的支付信息才显示
					payInfoss.add(payments);
				}
			}
		}
//		if(maininstanceid.equals(subinstanceid)){
//			INSBUsercomment uc = insbUsercommentService.selectUserCommentByTrackid(
//					insbWorkflowmaintrackService.getMainTrack(maininstanceid, taskcode).getId(), 1);
//			if(uc!=null){
//				otherInfo.put("noti", uc.getCommentcontent());//主流程用户备注信息;
//			}
//		}else{
//			INSBUsercomment uc = insbUsercommentService.selectUserCommentByTrackid(
//					insbWorkflowsubtrackService.getSubTrack(maininstanceid, subinstanceid, taskcode).getId(), 2);
//			if(uc!=null){
//				otherInfo.put("noti", uc.getCommentcontent());//子流程用户备注信息;
//			}
//		}
		
		//查询给操作员的备注信息
		List<INSBOperatorcomment> operatorcommentList = insbOperatorcommentService.getOperatorCommentByMaininstanceid(maininstanceid, inscomcode);
		if(operatorcommentList!=null && operatorcommentList.size()>0){
			otherInfo.put("operatorcomment",operatorcommentList);
		}
		//查询备注信息
		/*List<INSBUsercomment> usercomment = insbUsercommentService.getAllUserComment(maininstanceid, inscomcode);
		if(usercomment!=null && usercomment.size()>0){
//			otherInfo.put("noti", usercomment.get(0).getCommentcontent());
			otherInfo.put("noti", usercomment);
		}*/
		//查询给用户的备注信息
		List<Map<String, Object>> usc = insbUsercommentService.getUserCommentAndType(maininstanceid, inscomcode);
		if (usc != null && usc.size() > 0) {
			otherInfo.put("noti", usc);
		}

		//查询用户的备注信息
		List<Map<String, Object>> commentList = insbUsercommentService.getNearestUserCommentAndType(maininstanceid, inscomcode);
		if (commentList != null && commentList.size() > 0) {
			otherInfo.put("userComments", commentList);
		}
		
		Map<String,Object> insConfigInfo = insbCarkindpriceService.getCarInsConfigByInscomcode(inscomcode, maininstanceid);//已选保险配置信息
		//获取redis里平台信息
		LastYearPolicyInfoBean lastYearPolicyInfoBean = redisClient.get(Constants.TASK, maininstanceid, LastYearPolicyInfoBean.class);
		if(lastYearPolicyInfoBean!=null){
			if(lastYearPolicyInfoBean.getLastYearClaimBean()!=null){
				mav.addObject("claimtimes", lastYearPolicyInfoBean.getLastYearClaimBean().getClaimtimes());//出险次数
			}
		}
		//北京平台支付，二支节点显示被保人身份证信息
		INSCDept dept = new INSCDept();
		dept.setComcode(loginUser.getUserorganization());
		dept = inscDeptService.queryOne(dept);
		if(dept != null){
			INSCDept userDept = inscDeptService.getPlatformDept(dept.getId());
			if(userDept != null && (ConstUtil.PLATFORM_BEIJING_DEPTCODE.equals(userDept.getComcode()))){
				if(carInfo.containsKey("property") && Integer.parseInt(carInfo.get("property").toString()) == 0){
					if("20".equals(taskcode) || "21".equals(taskcode)){
						mav.addObject("showbbrIDinfo",true);
					}
				}
			}
		}
		mav.addObject("insConfigInfo",insConfigInfo);
		mav.addObject("agentInfo",agentInfo);
		mav.addObject("carInfo",carInfo);
		mav.addObject("carmodelinfo",carmodelinfo);
		mav.addObject("relationPersonInfo",relationPersonInfo);
		mav.addObject("otherInfo",otherInfo);
		mav.addObject("payInfos",payInfoss);
		mav.addObject("rulequery",rulequery);
		//核保补充数据项
		Map<String, Map> supplyParam = new HashMap<>(1);
		supplyParam.put("supplyParams", insbInsuresupplyparamService.getParamsByTask(maininstanceid, inscomcode, false));
		mav.addObject("carInsTaskInfo", supplyParam);
//		mav.addObject("taskid", maininstanceid);
	    return mav;
	}
	
	//车险任务查看任务流程
	@RequestMapping(value = "showWorkflowTrack", method = RequestMethod.GET)
	public ModelAndView showWorkflowTrack(String instanceId, String inscomcode){
		ModelAndView mav = new ModelAndView("cm/cartaskmanage/showWorkflowTrack");
		//返回页面数据
		Map<String, Object> workflowTrackInfo = insbWorkflowsubService.showWorkflowTrack(instanceId, inscomcode);//任务流程轨迹集合
		mav.addObject("workflowTrackInfo",workflowTrackInfo);
		return mav;
	}
	
	/**
	 * 调度分配验证是否任务被打开
	 */
	@RequestMapping(value = "dispatchCheck", method = RequestMethod.GET, produces="text/html;charset=UTF-8")
	@ResponseBody
	public String carTaskDispatchCheck(HttpSession session, String maininstanceId, String subInstanceId, 
			String inscomcode, String userCode, String operator){
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		return insbCarinfoService.carTaskDispatch(userCode, maininstanceId, subInstanceId, inscomcode, operator, loginUser);
	}
	
	/**
	 * 调度分配调用接口
	 */
	@RequestMapping(value = "dispatch", method = RequestMethod.GET, produces="text/html;charset=UTF-8")
	@ResponseBody
	public String carTaskDispatch(HttpSession session, String maininstanceId, String subInstanceId, 
			String inscomcode, String userCode, String operator){
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		return insbCarinfoService.carTaskDispatch(userCode, maininstanceId, subInstanceId, inscomcode, operator, loginUser);
	}
	
	/**
	 * 停止调度调用接口
	 */
	@RequestMapping(value = "stopdispatch", method = RequestMethod.GET, produces="text/html;charset=UTF-8")
	@ResponseBody
	public String stopCarTaskDispatch(HttpSession session, String maininstanceId, String subInstanceId, 
			String inscomcode){
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		return insbCarinfoService.stopCarTaskDispatch(maininstanceId, subInstanceId, inscomcode, loginUser);
	}
	
	/**
	 * 重启调度调用接口
	 */
	@RequestMapping(value = "restartdispatch", method = RequestMethod.GET, produces="text/html;charset=UTF-8")
	@ResponseBody
	public String restartCarTaskDispatch(HttpSession session, String maininstanceId, String subInstanceId, 
			String inscomcode, String userCode){
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		return insbCarinfoService.restartCarTaskDispatch(userCode, maininstanceId, subInstanceId, inscomcode, loginUser);
	}
	
	/**
	 * 跳转支持单一查询的车险任务页面
	 */
	@RequestMapping(value="cartaskManageListForQuery", method=RequestMethod.GET)
	public ModelAndView toCartaskManageListForQueryPage(){
		ModelAndView mav = new ModelAndView("cm/cartaskmanage/cartaskmanagelistforquery");
		return mav;
	}
	
	/**
	 * 按照当前登陆人初始化同组成员
	 * @return
	 */
	@RequestMapping(value="initUserList4Dispatch" ,method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String,String>> initUserList4Dispatch(HttpSession session){
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		return businessmanagegroupService.getMembersByMember(loginUser);
	}
	
	//查看错误信息
	@RequestMapping(value = "showflowerror", method = RequestMethod.GET)
	public ModelAndView showFlowError(String maininstanceid, String inscomcode, String inscomName){
		ModelAndView mav = new ModelAndView("cm/cartaskmanage/flowErrorDetail");
		INSBFlowerror query = new INSBFlowerror();
		query.setTaskid(maininstanceid);
		query.setInscomcode(inscomcode);
//		String inscompanyName =null;
//		try {
//			inscompanyName= new String(inscomName.getBytes("ISO-8859-1"), "UTF-8");   //URLDecoder.decode(inscomName, "UTF-8");
//		}catch (Exception e){
//			LogUtil.info("inscomName is encode error"+ e.getMessage());
//		}
		mav.addObject("errorList", insbFlowerrorService.queryList(query));
		mav.addObject("inscomName",inscomName);
		mav.addObject("maininstanceid", maininstanceid);
	    return mav;
	}
	
	//跳转车险任务管理页面（只支持查询）
	@RequestMapping(value="cartaskmanagelistforquery", method=RequestMethod.GET)
	public ModelAndView cartaskmanagelistforquery(){
		ModelAndView mav = new ModelAndView("cm/cartaskmanage/cartaskmanagelistforquery");
		return mav;
	}
}
