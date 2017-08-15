package com.zzb.conf.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.cninsure.core.utils.LogUtil;
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
import com.cninsure.core.utils.DateUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.conf.dao.INSBChannelDao;
import com.zzb.conf.entity.INSBOperatorcomment;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.entity.INSBPaychannel;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.service.INSBOperatorcommentService;
import com.zzb.conf.service.INSBOrderManageService;
import com.zzb.conf.service.INSBOrderpaymentService;
import com.zzb.conf.service.INSBPaychannelService;
import com.zzb.conf.service.INSBPolicyManageService;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.model.OrderQueryModel;
import com.zzb.model.PolicyDetailedModel;

@Controller
@RequestMapping("/order/*")
public class INSBOrderController extends BaseController{ 
	@Resource
	private INSBCarinfoService insbCarinfoService; 
	@Resource
	private INSBOrderManageService insbOrderManageService;
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
	private INSBPaychannelService isnbPaychannelService;
	@Resource
	private INSBOrderpaymentService insbOrderpaymentService;
	@Resource
	private INSBInsuresupplyparamService insbInsuresupplyparamService;
	@Resource
	private INSBChannelDao insbChannelDao;
	
	
	@RequestMapping(value="/query",method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView queryOrder(HttpSession session) throws ControllerException{
		ModelAndView mav = new ModelAndView("zzbconf/insbOrderManagement");
		List<INSCCode> statusList = codeService.queryINSCCodeByCode("orderstatus", "orderstatus");
		mav.addObject("statusList", statusList);
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		String deptid = user.getUserorganization();
		//Map<String, Object> rowList = insbOrderManageService.queryOrderList(new OrderQueryModel(),deptid);
		OrderQueryModel queryModel = new OrderQueryModel();
		queryModel.setStartdate(DateUtil.getCurrentDate());
		queryModel.setEnddate(DateUtil.getCurrentDate());
		mav.addObject("queryModel", queryModel);
		mav.addObject("allData", null);
		return mav;
	}
	
	/**
	 * 查看详情
	 * @param queryModel
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/queryorderlist",method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView queryOrderList(HttpSession session,@ModelAttribute OrderQueryModel queryModel) throws ControllerException{
		ModelAndView mav = new ModelAndView("zzbconf/insbOrderManagement");
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
	public ModelAndView queryDetail(@RequestParam(value="id",required=false) String id,@RequestParam(value="pid",required=false) String pid,
			@RequestParam(value="codename",required=false)String codename,@RequestParam(value="payid",required=false) String payid,
			@RequestParam(value="taskcode",required=false) String taskcode) throws ControllerException{
		LogUtil.info("id-->>"+id+",pid-->>"+pid);
		ModelAndView mav = new ModelAndView("zzbconf/insbOrderDetailed");
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
			mav.addObject("insborderid","insborderid");
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
			String channelcode = totalinfo.getPurchaserChannel();
			if(StringUtil.isNotEmpty(channelcode)) {
				List<Map<String, String>> mapList = insbChannelDao.selectSenderInfoByCityAndChannelcode(totalinfo.getInscitycode(), channelcode);
				LogUtil.info(mapList);
				for(Map<String,String> senderMap : mapList) {


					if ("1".equals(senderMap.get("isdefined")) && senderMap.get("senderaddress") != null && !"".equals(senderMap.get("senderchannel"))  ) {
						mav.addObject("senderaddress", senderMap.get("senderaddress"));
						mav.addObject("senderchannel", senderMap.get("senderchannel"));
						mav.addObject("sendername", senderMap.get("sendername"));
						mav.addObject("senderphone", senderMap.get("senderphone"));

					

					} else  if("1".equals(senderMap.get("pisdefined"))){

						mav.addObject("senderaddress", senderMap.get("paddress"));
						mav.addObject("senderchannel", senderMap.get("pchannel"));
						mav.addObject("sendername", senderMap.get("pname"));
						mav.addObject("senderphone", senderMap.get("pphone"));
					}
				}
			}
			INSBQuoteinfo quote = new INSBQuoteinfo();
			quote.setQuotetotalinfoid(totalinfo.getId());
			quote.setInscomcode(prvid);
			quote = insbQuoteinfoService.queryOne(quote);
			subInstanceId = quote.getWorkflowinstanceid();
		}
		Map<String,Object> carInsTaskInfo = new HashMap<String, Object>();
		Map<String,Object> insConfigInfo = insbCarkindpriceService.getCarInsConfigByInscomcode(prvid,taskid);
		carInsTaskInfo.put("insConfigInfo",insConfigInfo);
		carInsTaskInfo.put("supplyParams", insbInsuresupplyparamService.getParamsByTask(taskid, prvid, false));
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
		
		//添加支付payflowno
		INSBOrderpayment paymentInfo = insbOrderpaymentService.queryById(payid);
		
		LogUtil.info("++++++orderpayid=" + payid +", taskid=" + taskid  + ", INSBOrderpayment=" + paymentInfo);
		if(paymentInfo != null){
			//获得调用接口url(from config)
			paymentInfo.setSecpayorderrequrl(insbOrderService.getQuerySecondPayURL(order.getPaymentmethod()));
			paymentInfo.setPayorderno(order.getOrderno());
			paymentInfo.setTaskcode(taskcode);
		}
		mav.addObject("paymentInfo", paymentInfo);
				
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
	 * @param instanceid
	 * @param inscomcode
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "lastPriceInfo",method = RequestMethod.GET)
	public ModelAndView searchMoney( String instanceid, String inscomcode) throws ControllerException {
		ModelAndView mav = new ModelAndView("zzbconf/lastPriceInfo");
		Map<String,Object> insConfigInfo = inshCarkindpriceService.getCarInsConfigByInscomcode(inscomcode, instanceid);//保险配置信息
		mav.addObject("insConfigInfo",insConfigInfo);
		return mav;	
	}
}
