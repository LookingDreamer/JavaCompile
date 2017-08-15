package com.zzb.app.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.exception.ControllerException;
import com.zzb.app.model.bean.SupplementItem;
import com.zzb.app.service.AppSupplementItemService;

@Controller
@RequestMapping(value="/supplementItem/*")
public class AppSupplementItemController {

	@Resource
	private AppSupplementItemService appSupplementItemService;
	
	@RequestMapping(value="/getSupplierSupplementItem")
	@ResponseBody
	public String getSupplierSupplementItem(@RequestBody SupplementItem supplementItem) throws ControllerException {
		String result = null;
		result = appSupplementItemService.getSupplierSupplementItem(supplementItem.getSupplierId(),supplementItem.getDeptId());
		return result;
	}
}
