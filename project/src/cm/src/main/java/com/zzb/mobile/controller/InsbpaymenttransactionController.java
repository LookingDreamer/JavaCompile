package com.zzb.mobile.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.zzb.mobile.service.InsbpaymenttransactionService;
@Controller
@RequestMapping("/mobile/basedata/my/*")
public class InsbpaymenttransactionController extends BaseController{

@Resource
private InsbpaymenttransactionService insbpaymenttransactionService; 


   
@RequestMapping(value="/getPaymenttransaction",method=RequestMethod.POST)
@ResponseBody
public String getPaymenttransaction(@RequestBody Date date){
	
	return insbpaymenttransactionService.getPaymenttransaction(date);
}




}
