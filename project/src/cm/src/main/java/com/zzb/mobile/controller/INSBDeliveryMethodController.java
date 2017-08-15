package com.zzb.mobile.controller;

import javax.annotation.Resource;

import com.cninsure.core.utils.LogUtil;
import com.common.JsonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzb.mobile.model.DeliveryAddressInfoParam;
import com.zzb.mobile.model.DeliveryMethodParam;
import com.zzb.mobile.service.INSBDeliveryMethodService;

@Controller
@RequestMapping("/mobile/*")
public class INSBDeliveryMethodController {
	@Resource
	private INSBDeliveryMethodService insbDeliveryMethodService;
	/*
	 * 接口，获得两种配送方式信息的接口，自取以及快递，参数订单id
	 */
	@RequestMapping(value = "basedata/myTask/getDeliveryInfo", method = RequestMethod.POST)
	@ResponseBody
	public String getDeliveryInfo(@RequestBody DeliveryMethodParam deliveryMethodParam) {
		return insbDeliveryMethodService.showDeliveryInfo(deliveryMethodParam.getOrderno(),deliveryMethodParam.getChannelId(),deliveryMethodParam.getChannelUserId());
	}
	
	/*
	 * 接口，添加新的快递地址，省市区三个字段
	 * 新增渠道用户ID和渠道ID，用户跨区出单
	 */
	@RequestMapping(value = "basedata/myTask/addNewAddress", method = RequestMethod.POST)
	@ResponseBody
	public String addNewAddress(@RequestBody DeliveryAddressInfoParam deliveryAddressInfo){
		return insbDeliveryMethodService.addNewAddress(deliveryAddressInfo.getProcessInstanceId(),deliveryAddressInfo.getAgentnum(), deliveryAddressInfo.getRecipientname(), deliveryAddressInfo.getRecipientmobilephone(), deliveryAddressInfo.getProvince(),deliveryAddressInfo.getCity(),deliveryAddressInfo.getArea(), deliveryAddressInfo.getRecipientaddress(), deliveryAddressInfo.getZipCode(), deliveryAddressInfo.getChannelId(),deliveryAddressInfo.getChannelUserId());
	}
	/*
	 * 接口，修改地址信息 
	 */
	@RequestMapping(value = "basedata/myTask/editAddress", method = RequestMethod.POST)
	@ResponseBody
	public String editAddress(@RequestBody DeliveryAddressInfoParam deliveryAddressInfo){
		return insbDeliveryMethodService.editAddress(deliveryAddressInfo.getAddressId(),deliveryAddressInfo.getAgentnum(), deliveryAddressInfo.getRecipientname(), deliveryAddressInfo.getRecipientmobilephone(), deliveryAddressInfo.getProvince(),deliveryAddressInfo.getCity(),deliveryAddressInfo.getArea(), deliveryAddressInfo.getRecipientaddress(), deliveryAddressInfo.getZipCode(),deliveryAddressInfo.getOrderno());
	}
	
	/*
	 * 保存配送信息,表明选中的配送地址信息
	 */
	@RequestMapping(value = "basedata/myTask/saveOrderDelivery", method = RequestMethod.POST)
	@ResponseBody
	public String saveOrderDelivery(@RequestBody DeliveryAddressInfoParam deliveryAddressInfo){
        LogUtil.info("保存配送信息：" + JsonUtil.serialize(deliveryAddressInfo));
		return insbDeliveryMethodService.saveOrderDelivery(deliveryAddressInfo.getAddressId(),deliveryAddressInfo.getAgentnum(), deliveryAddressInfo.getOrderno(),deliveryAddressInfo.getDelivetytype());
	}
	/*
	 * 删除地址接口
	 */
	@RequestMapping(value = "basedata/myTask/deleteAddress", method = RequestMethod.POST)
	@ResponseBody
	public String deleteAddress(@RequestBody DeliveryAddressInfoParam deliveryAddressInfo){
		return insbDeliveryMethodService.deleteAddress(deliveryAddressInfo.getAddressId());
	}
}
