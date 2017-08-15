package com.zzb.mobile.controller;

import javax.annotation.Resource;

import com.zzb.mobile.model.CommonModel;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cninsure.core.controller.BaseController;
import com.zzb.mobile.model.MyOrderParam;
import com.zzb.mobile.service.INSBMyOrderService;

/*
 * 我的订单接口
 */
@Controller
@RequestMapping("/mobile/*")
public class INSBMyOrderController extends BaseController{
	@Resource
	private INSBMyOrderService insbMyOrderService;

	/*
	 * 我的订单接口
	 */
	@RequestMapping(value = "basedata/myTask/getMyOrderList", method = RequestMethod.POST)
	@ResponseBody
	public String getMyOrderList(@RequestBody MyOrderParam myOrderParam) {
		return insbMyOrderService.showMyOrder(myOrderParam.getAgentnum(),
				myOrderParam.getCarlicenseno(), myOrderParam.getInsuredname(),
				myOrderParam.getOrderstatus(), myOrderParam.getLimit(),
				myOrderParam.getOffset(),myOrderParam.getTaskcreatetimeup(),myOrderParam.getTaskcreatetimedown(),
				myOrderParam.getPurchaserid(),myOrderParam.getPurchaserchannel());
	}
	
	/*
	 * 显示出单网点接口
	 */
	@RequestMapping(value = "basedata/myTask/getDeptList", method = RequestMethod.POST)
	@ResponseBody
	public String getDeptList(@RequestParam String processinstanceid,@RequestParam String inscomcode) {
		return insbMyOrderService.showSingleSite(processinstanceid,inscomcode);
	}

	/*
	 * 我的订单接口
	 */
	@RequestMapping(value = "basedata/myTask/getMyOrderListForChn", method = RequestMethod.POST)
	@ResponseBody
	public String getMyOrderListForChn(@RequestBody MyOrderParam myOrderParam) {
		CommonModel commonModel = insbMyOrderService.showMyOrderForChnNew(myOrderParam.getAgentnum(),
				myOrderParam.getCarlicenseno(), myOrderParam.getInsuredname(),
				myOrderParam.getOrderstatus(), myOrderParam.getLimit(),
				myOrderParam.getOffset(), myOrderParam.getTaskcreatetimeup(), myOrderParam.getTaskcreatetimedown(),
		myOrderParam.getPurchaserchannel(),myOrderParam.getPurchaserid(),null);
		return JSONObject.fromObject(commonModel).toString();
	}
}
