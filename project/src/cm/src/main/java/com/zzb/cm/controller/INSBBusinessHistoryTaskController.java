package com.zzb.cm.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBCarkindpriceService;
import com.zzb.cm.service.INSBOrderService;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.cm.service.INSBQuotetotalinfoService;
import com.zzb.conf.entity.INSBOperatorcomment;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.entity.INSBPaychannel;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.entity.baseData.INSBQueryhistoryinfo;
import com.zzb.conf.service.INSBOperatorcommentService;
import com.zzb.conf.service.INSBOrderManageService;
import com.zzb.conf.service.INSBOrderpaymentService;
import com.zzb.conf.service.INSBPaychannelService;
import com.zzb.conf.service.INSBPolicyManageService;
import com.zzb.conf.service.INSBPolicyitemService;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.conf.service.INSBWorkflowmaintrackService;
import com.zzb.model.PolicyDetailedModel;

@Controller
@RequestMapping("/business/history/*")
public class INSBBusinessHistoryTaskController extends BaseController{
	@Resource
	private INSBWorkflowmaintrackService insbworkflowmaintrackservice;
	@Resource
	private INSBOrderService insbOrderService;
	@Resource
	private INSBOrderManageService insbOrderManageService;
	@Resource
	private INSCCodeService codeService;
	@Resource
	private INSBProviderService providerService;
	@Resource
	private INSBPolicyManageService insbPolicyManageService;
	@Resource
	private INSBCarkindpriceService insbCarkindpriceService;
	@Resource
	private INSBUsercommentService insbUsercommentService;
	@Resource
	private INSBOperatorcommentService insbOperatorcommentService;
	@Resource
	private INSBQuotetotalinfoService insbQuotetotalinfoService;
	@Resource
	private INSBQuoteinfoService insbQuoteinfoService;
	@Resource
	private INSBPolicyitemService insbPolicyitemService;
	@Resource
	private INSBOrderpaymentService isnbOrderpaymentService;
	@Resource
	private INSBPaychannelService isnbPaychannelService;
	
	/**
	 * 
	 * 跳转至我的历史任务界面
	 */
	@RequestMapping("historyTask")
	public ModelAndView finishedTask(){
		ModelAndView mav=new ModelAndView("cm/mytask/myHistoryTask");
		return mav;	
	}
	
	/**
	 * 查询我的历史任务信息
	 * @param session
	 * @param instanceid
	 * @param carlicenseno
	 * @param bname
	 * @param taskcreatetimeup
	 * @param taskcreatetimedown
	 * @param limit
	 * @param currentpage
	 * @return
	 */
	@RequestMapping("queryMyHistoryTask")
	@ResponseBody
	public ModelAndView queryMyHistorytask(HttpSession session,String maininstanceid,String carlicenseno,
			String bname,String taskcreatetimeup,String taskcreatetimedown,Integer limit,Integer currentpage){
		INSCUser loginUser=(INSCUser)session.getAttribute("insc_user");
		ModelAndView mav=new ModelAndView("cm/mytask/myHistoryTaskSon");
		INSBQueryhistoryinfo insbqueryhistoryinfo=new INSBQueryhistoryinfo();
		insbqueryhistoryinfo.setBname(bname);
		insbqueryhistoryinfo.setCarlicenseno(carlicenseno);
		insbqueryhistoryinfo.setCurrentpage(currentpage);
		insbqueryhistoryinfo.setMaininstanceid(maininstanceid);
		insbqueryhistoryinfo.setLimit(limit);
		insbqueryhistoryinfo.setTaskcreatetimedown(taskcreatetimedown);
		insbqueryhistoryinfo.setTaskcreatetimeup(taskcreatetimeup);
		Map<String,Object>result=insbworkflowmaintrackservice.queryMyHistorytask(insbqueryhistoryinfo,loginUser);
		mav.addObject("historyTask",result);
		return mav;
	}
	
	/**
	 * 详情信息
	 * @param id
	 * @param pid
	 * @param codename
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/queryorderdetail",method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView queryDetail(@RequestParam(value="id",required=false) String id,@RequestParam(value="pid",required=false) String pid,@RequestParam(value="codename",required=false)String codename) throws ControllerException{
		LogUtil.info("id-->>"+id+",pid-->>"+pid);
		ModelAndView mav = new ModelAndView("cm/mytask/myHistoryTaskDetail");
		mav.addObject("codename",codename);
		INSBOrder order = insbOrderService.queryById(id);
		String prvid ="";
		if(order!=null){
			prvid = order.getPrvid();
		}
		PolicyDetailedModel detailedModel = null;
		if(null!=id&&!"".equals(id)){
			mav.addObject("insborderid","insborderid");
			INSBOrderpayment payment = new INSBOrderpayment();
			payment.setOrderid(id);
			payment = isnbOrderpaymentService.queryOne(payment);
			if(payment!=null){
				INSBPaychannel pc = isnbPaychannelService.queryById(payment.getPaychannelid());
				if(pc!=null){
					mav.addObject("paytypes",pc.getPaychannelname());
				}
			}			
			detailedModel = insbPolicyManageService.queryOrderDetailedByOId(id,prvid,"orderid");
		}else{
			detailedModel = insbPolicyManageService.queryOrderDetailedByOId(pid,prvid,"");
		}
		mav.addObject("detailedModel", detailedModel);
		String taskid ="";
		String orderstatus="";
		String subInstanceId = "";
		if(order!=null){
			taskid = order.getTaskid();
			prvid = order.getPrvid();
			orderstatus = order.getOrderstatus();
			INSBQuotetotalinfo totalinfo = new INSBQuotetotalinfo();
			totalinfo.setTaskid(taskid);
			totalinfo = insbQuotetotalinfoService.queryOne(totalinfo);
			INSBQuoteinfo quote = new INSBQuoteinfo();
			quote.setQuotetotalinfoid(totalinfo.getId());
			quote.setInscomcode(prvid);
			quote = insbQuoteinfoService.queryOne(quote);
			subInstanceId = quote.getWorkflowinstanceid();
		}
		Map<String,Object> insConfigInfo = insbCarkindpriceService.getCarInsConfigByInscomcode(prvid,taskid);
		Map<String,Object> carInsTaskInfo = new HashMap<String, Object>();
		carInsTaskInfo.put("insConfigInfo",insConfigInfo);
		mav.addObject("carInsTaskInfo", carInsTaskInfo);
		//查询影像信息
		List<PolicyDetailedModel> imginfo = insbPolicyManageService.queryImgInfo(id);
		//给用户的备注信息
		List<INSBUsercomment> usercomment  = new ArrayList<INSBUsercomment>();
		//给操作员的备注信息
		List<INSBOperatorcomment> operatorcommentList = new ArrayList<INSBOperatorcomment>();
		List<String> node =  Arrays.asList(new String[]{"3","4"});
		if(node.contains(orderstatus)){
			operatorcommentList = insbOperatorcommentService.getOperatorCommentByMaininstanceid(taskid, prvid);
		}else{
			operatorcommentList = insbOperatorcommentService.getOperatorCommentByMaininstanceid(taskid, prvid);
		}
		//当前节点之前的备注信息（入参 ： 主流程实例taskid ; prvid 保险公司code ; dqtaskcode 当前节点code）
	    usercomment = insbUsercommentService.getNearestUserComment(taskid, prvid, null);
	    mav.addObject("usercomment", usercomment);
	    mav.addObject("taskid", taskid);
	    mav.addObject("subInstanceId", subInstanceId);
		mav.addObject("operatorcommentList", operatorcommentList);
		mav.addObject("imginfo", imginfo);
		return mav;
	}
	
}

	

