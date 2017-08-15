package com.lzgapi.order.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.cninsure.core.utils.StringUtil;
import com.lzgapi.order.service.LzgDataTransferService;
import com.zzb.mobile.model.policyoperat.PolicyInfoQueryParam02;
import com.zzb.mobile.service.AppMyOrderInfoService;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.LogUtil;
import com.lzgapi.order.service.LzgOrderService;
import com.zzb.conf.entity.INSBOrderlistenpush;
import com.zzb.conf.service.INSBOrderlistenpushService;
import com.zzb.mobile.model.CommonModel;

/**
 * 
 * cm向懒掌柜提供接口信息
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/cm4lzg/order/*")
public class LzgOrderContorller {
	@Resource
	private LzgOrderService lzgOrderService;
	@Resource
	private INSBOrderlistenpushService insbOrderlistenpushService;
	@Resource
    private HttpServletResponse response;
	@Resource
	private LzgDataTransferService lzgDataTransferService;

	@Resource
	private AppMyOrderInfoService appMyOrderInfoService;
	
	/**
	 * 逻辑删除取消订单
	 * @return
	 */
	@RequestMapping(value="cancelorder",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> cancelOrder(String params){
		LogUtil.info("===LZG取消订单---开始");
		Map<String,String> result = new HashMap<String,String>();
		try {
			 Map<String,String> temp = lzgOrderService.cancleOrderFromLZG(params);
			 LogUtil.info("===LZG取消订单---结束状态---temp="+temp);
			 if(temp==null||temp.isEmpty()){
				 result.put("status", "FAIL");
				result.put("message", "订单取消失败,参数错误");
			 }else{
				 result = temp;
			 }
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "FAIL");
			result.put("message", e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 接口描述：产品进入接口
	 * 请求地址   /cm4lzg/order/getProduct
	 * @param regInfoJSON 注册信息
	 */
	@RequestMapping(value = "getProduct", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel getProduct(@RequestParam(value="token") String token, 
			@RequestParam(value="lzgOtherUserId") String lzgOtherUserId, 
			@RequestParam(value="lzgRequirementId") String lzgRequirementId, 
			@RequestParam(value="lzgProductCode") String lzgProductCode, 
			@RequestParam(value="lzgManagerId") String lzgManagerId) throws ControllerException {
		CommonModel model = lzgOrderService.getProduct(token,lzgOtherUserId,lzgManagerId,lzgRequirementId,lzgProductCode);
		if(model.getStatus().equals("success")){
			JSONObject jsonObject = JSONObject.fromObject(model.getBody());
			response.addHeader("token", jsonObject.getString("token"));
		}
		return model;
	}
	
	/**
	 * 接口描述：订单继续处理接口
	 * 请求地址   /cm4lzg/order/orderContinue
	 * @param regInfoJSON 注册信息
	 */
	@RequestMapping(value = "orderContinue", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel orderContinue(@RequestParam(value="token") String token, 
			@RequestParam(value="lzgOtherUserId") String lzgOtherUserId, 
			@RequestParam(value="lzgRequirementId") String lzgRequirementId, 
			@RequestParam(value="lzgOtherOrderNo") String lzgOtherOrderNo) throws ControllerException {
		CommonModel model = lzgOrderService.orderContinue(token,lzgOtherUserId,lzgRequirementId,lzgOtherOrderNo);
		if(model.getStatus().equals("success")){
			JSONObject jsonObject = JSONObject.fromObject(model.getBody());
			response.addHeader("token", jsonObject.getString("token"));
		}
		return model;
	}
	/**
	 * 接口描述：定时任务==订单监听推送表保存失败时定时更新
	 * 请求地址   /cm4lzg/order/updateaddfail
	 * @param 
	 */
	@RequestMapping(value = "/updateaddfail", method = RequestMethod.POST)
	@ResponseBody
	public void updateAddFail(@RequestBody INSBOrderlistenpush orderfail){
		insbOrderlistenpushService.updateAddFail(orderfail);
	}

	/**
	 * 接口描述：报价列表
	 * 请求地址   /cm4lzg/order/getQuoteInfoList
	 * @param
	 */
	@RequestMapping(value = "getQuoteInfoList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getQuoteInfoList(@RequestBody Map<String, Object> params ) throws ControllerException {
		String taskId = String.valueOf(params.get("taskId"));
		String orderStatus = String.valueOf(params.get("orderStatus"));
		String resp = lzgDataTransferService.getQuoteInfoList(taskId, orderStatus);
		return resp;
	}

	/**
	 * 查询保单详细信息接口
	 * @param policyInfoQueryParam02 policyno 保单号, queryflag 查询标记位（cm、cf）
	 */
	@RequestMapping(value = "/getPolicyitemDetailInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getPolicyitemDetailInfo02(@RequestBody PolicyInfoQueryParam02 policyInfoQueryParam02)
			throws ControllerException {
		return appMyOrderInfoService.getPolicyitemDetailInfo02(policyInfoQueryParam02.getPolicyno(),
				policyInfoQueryParam02.getQueryflag(), policyInfoQueryParam02.getPolicytype());
	}


	/**
	 * 接口简单说明
	 * 请求方式	POST
	 * 请求地址	/cm4lzg/order/checkToken/
	 * @param lzgToken  token
	 * @param clienttype  客户端类型
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/checkToken",method=RequestMethod.GET)
	@ResponseBody
	public CommonModel checkLzgToken(String lzgToken, @RequestHeader(value = "X-CLIENT-TYPE", required = false) String clienttype) throws ControllerException{
		CommonModel model = new CommonModel();
		if(StringUtil.isEmpty(lzgToken)){
			model.setStatus("fail");
			model.setMessage("lzgToken不能为空！");
			return model;
		}
		Map<String,Object> params = new HashMap<>();
		params.put("checkToken",lzgToken);
		model = lzgDataTransferService.checkToken(params);
		if(model.getStatus().equals("success")){
			JSONObject jsonObject = JSONObject.fromObject(model.getBody());
			response.addHeader("token", jsonObject.getString("token"));
		}
		return model;
	}


}
