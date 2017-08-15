package com.zzb.extra.controller;


import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.common.PagingParams;
import com.zzb.chn.util.JsonUtils;
import com.zzb.conf.service.*;
import com.zzb.extra.entity.INSBMiniOrderTrace;
import com.zzb.extra.model.INSBMiniOrderTraceModel;
import com.zzb.extra.service.INSBMiniCallChnService;
import com.zzb.extra.service.INSBMiniOrderManageService;
import com.zzb.extra.service.INSBMiniOrderTraceService;
import com.zzb.extra.util.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/miniOrderTrace/*")
public class INSBMiniOrderTraceController extends BaseController{

	@Resource
	private INSBMiniOrderTraceService insbMiniOrderTraceService;
	@Resource
	private INSCCodeService codeService;
	@Resource
	private INSBProviderService providerService;
	@Resource
	private INSBMiniOrderManageService insbMiniOrderManageService;

	@Resource
	private INSBMiniCallChnService insbMiniCallChnService;

	@RequestMapping(value="/query",method=RequestMethod.GET)
	public ModelAndView query() throws ControllerException{
		ModelAndView mav = new ModelAndView("extra/order/miniOrderTrace");
		List<INSCCode> operateStatusList = codeService.queryINSCCodeByCode("minizzbOrder", "operateStatus");
		mav.addObject("operateStateList", operateStatusList);
		List<INSCCode> refundReasonList = codeService.queryINSCCodeByCode("minizzbOrder", "refundReason");
		mav.addObject("refundReasonList", refundReasonList);
		return mav;
	}

	@RequestMapping(value="/queryOrderList",method=RequestMethod.GET)
	@ResponseBody
	public String queryOrderList(@ModelAttribute PagingParams para,@ModelAttribute INSBMiniOrderTraceModel insbMiniOrderTraceModel) throws ControllerException{
		Map<String, Object> map = BeanUtils.toMap(para, insbMiniOrderTraceModel);
		//System.out.println(JsonUtils.serialize(map));
		return insbMiniOrderTraceService.queryOrderList(map);
	}

	@RequestMapping(value="/updateOrderOperateState",method=RequestMethod.POST)
	@ResponseBody
	public String updateOrderOperateState(HttpSession session,@ModelAttribute INSBMiniOrderTrace insbMiniOrderTrace) throws ControllerException{

		try {
			LogUtil.info("updateOrderOperateState old:"+JsonUtils.serialize(insbMiniOrderTrace));
			INSCUser user = (INSCUser) session.getAttribute("insc_user");
			insbMiniOrderTrace.setModifytime(new Date());
			insbMiniOrderTrace.setOperator(user.getUsercode());

			Map<Object,Object> queryMap = new HashMap<Object,Object>();
			queryMap.put("channelId","nqd_minizzb2016");
			queryMap.put("taskId",insbMiniOrderTrace.getTaskid());
			//queryMap.put("taskState",insbMiniOrderTrace.getTaskstate());
			List<Map<Object,Object>> channelOrderList = insbMiniOrderManageService.queryOrderPagingList(queryMap);
			if(channelOrderList!=null&&channelOrderList.size()>0){
				Map<Object,Object> channel_task = channelOrderList.get(0);
				String taskState = String.valueOf(channel_task.get("taskState"));
				if(StringUtil.isNotEmpty(taskState)  && !taskState.equals(insbMiniOrderTrace.getTaskstate())){
					LogUtil.info("用户已处理该笔订单！"+JsonUtils.serialize(insbMiniOrderTrace));
					insbMiniOrderTrace.setTaskstate(taskState);
					switch (taskState){
						case "12":
							insbMiniOrderTrace.setOperatestate("4");
							insbMiniOrderTrace.setReason("6");//用户主动发起了退款
							break;//用户已发起退款
						case "6":
							insbMiniOrderTrace.setOperatestate("5");
							break;//待完成交易
						case "11":
							insbMiniOrderTrace.setOperatestate("6");
							break;//已促成交易
						default:
							break;
					}
					insbMiniOrderTrace.setNoti("用户自己处理该笔订单");
					insbMiniOrderTraceService.updateOrderOperateState(insbMiniOrderTrace);
					return ParamUtils.resultMap(true, "用户已处理该笔订单！");
				}
			}
			//代用户申请退款
			if("3".equals(insbMiniOrderTrace.getOperatestate())){
				insbMiniOrderTrace.setTaskstate("12");
				insbMiniOrderTraceService.updateOrderOperateState(insbMiniOrderTrace);
				insbMiniCallChnService.applyRefund(insbMiniOrderTrace.getTaskid(),insbMiniOrderTrace.getProvidercode());
			}else{
				insbMiniOrderTraceService.updateOrderOperateState(insbMiniOrderTrace);
			}

			LogUtil.info("updateOrderOperateState new:"+JsonUtils.serialize(insbMiniOrderTrace));
			return ParamUtils.resultMap(true, "处理成功");
		}catch (Exception e){
			LogUtil.info("updateOrderOperateState异常：msg="+e.getMessage());
			return ParamUtils.resultMap(false, "系统错误");
		}

	}



	@RequestMapping(value="/queryprovidertree",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryprovidertree(@RequestParam(value="id",required=false) String parentcode) throws ControllerException{
		return providerService.queryTreeList(parentcode);
	}

}
