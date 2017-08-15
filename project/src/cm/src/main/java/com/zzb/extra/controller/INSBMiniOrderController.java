package com.zzb.extra.controller;


import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.zzb.chn.util.JsonUtils;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.*;
import com.zzb.conf.entity.INSBOperatorcomment;
import com.zzb.conf.entity.INSBPaychannel;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.service.*;
import com.zzb.extra.model.MiniOrderQueryModel;
import com.zzb.extra.service.INSBMiniOrderManageService;
import com.zzb.model.PolicyDetailedModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/miniOrder/*")
public class INSBMiniOrderController extends BaseController{
	@Resource
	private INSBCarinfoService insbCarinfoService;
	@Resource
	private INSBMiniOrderManageService insbOrderManageService;
	@Resource
	private INSCCodeService codeService;
	@Resource
	private INSBProviderService providerService;
	@Resource
	private INSBPolicyManageService insbPolicyManageService;
	@Resource
	private INSHCarkindpriceService inshCarkindpriceService;
	@Resource
	private INSBCarkindpriceService insbCarkindpriceService;
	@Resource
	private INSBOrderService insbOrderService;
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
	@RequestMapping(value="/query",method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView queryOrder(HttpSession session,String agentid) throws ControllerException{
		System.out.println("agentid="+agentid);
		ModelAndView mav = new ModelAndView("extra/agentuseredit");
		List<INSCCode> statusList = codeService.queryINSCCodeByCode("orderstatus", "orderstatus");
		mav.addObject("statusList", statusList);
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		String deptid = user.getUserorganization();
		//Map<String, Object> rowList = insbOrderManageService.queryOrderList(new OrderQueryModel(),deptid);
		MiniOrderQueryModel queryModel = new MiniOrderQueryModel();
		queryModel.setStartdate(DateUtil.getCurrentDate());
		queryModel.setEnddate(DateUtil.getCurrentDate());
		queryModel.setAgentid(agentid);
		mav.addObject("queryModel", queryModel);
		mav.addObject("allData", null);
		System.out.println(JsonUtils.serialize(mav));
		return mav;
	}

	/**
	 * 查看详情
	 * @param queryModel
	 * @return
	 * @throws com.cninsure.core.exception.ControllerException
	 */
	@RequestMapping(value="/queryorderlist",method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView queryOrderList(HttpSession session,@ModelAttribute MiniOrderQueryModel queryModel) throws ControllerException{
		System.out.println(JsonUtils.serialize(queryModel));
		ModelAndView mav = new ModelAndView("extra/agentuseredit");
		//保单状态
		List<INSCCode> statusList = codeService.queryINSCCodeByCode("orderstatus", "orderstatus");
		mav.addObject("statusList", statusList);
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		String deptid = user.getUserorganization();
		queryModel.setUserCode(user.getUsercode());
		Map<String, Object> rowList = insbOrderManageService.queryOrderList(queryModel,deptid);
		mav.addObject("allData", rowList);
		mav.addObject("queryModel", queryModel);
		return mav;
	}

	@RequestMapping(value="/queryprovidertree",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryprovidertree(@RequestParam(value="id",required=false) String parentcode) throws ControllerException{
		return providerService.queryTreeList(parentcode);
	}

	@RequestMapping(value="/queryorderdetail",method= RequestMethod.POST)
	@ResponseBody
	public ModelAndView queryDetail(@RequestParam(value="id",required=false) String id,@RequestParam(value="pid",required=false) String pid,@RequestParam(value="codename",required=false)String codename) throws ControllerException{
		System.out.println(id+"-->>id");
		System.out.println(pid+"-->>pid");
		ModelAndView mav = new ModelAndView("../extra/insbMiniOrderDetailed");
		mav.addObject("codename",codename);
		INSBOrder order = insbOrderService.queryById(id);
		String prvid ="";
		if(order!=null){
			prvid = order.getPrvid();
		}
		PolicyDetailedModel detailedModel = null;
		if(order!=null && order.getPaymentmethod()!=null && !"".equals(order.getPaymentmethod()) ){
			INSBPaychannel pc = isnbPaychannelService.queryById(order.getPaymentmethod());
			if(pc!=null){
				mav.addObject("paytypes",pc.getPaychannelname());
			}
		}
		if(id!=null && !"".equals(id)){
			detailedModel = insbPolicyManageService.queryOrderDetailedByOId(id,prvid,"orderid");
		}else{
			detailedModel = insbPolicyManageService.queryOrderDetailedByOId(pid,prvid,"");
		}
		Map<String, Object> otherInfo = insbCarinfoService.getCarTaskOtherInfo(order.getTaskid(), prvid, "SHOW");//其他信息
		mav.addObject("otherInfo", otherInfo);
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

	/**
	 * 查看最后一次报价
	 * @param taskid
	 * @param taskcode
	 * @return
	 * @throws com.cninsure.core.exception.ControllerException
	 */
	@RequestMapping(value = "lastPriceInfo",method = RequestMethod.GET)
	public ModelAndView searchMoney( String instanceid, String inscomcode) throws ControllerException {
		ModelAndView mav = new ModelAndView("zzbconf/lastPriceInfo");
		Map<String,Object> insConfigInfo = inshCarkindpriceService.getCarInsConfigByInscomcode(inscomcode, instanceid);//保险配置信息
		mav.addObject("insConfigInfo",insConfigInfo);
		return mav;	
	}
}
