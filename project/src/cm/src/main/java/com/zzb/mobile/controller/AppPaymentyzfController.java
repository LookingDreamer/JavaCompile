package com.zzb.mobile.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.zzb.mobile.dao.AppPaymentyzfDao;
import com.zzb.mobile.entity.AppPaymentyzf;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.AppPaymentyzfService;
import com.zzb.model.AppPaymentQueryModel;
@Controller
@RequestMapping("/mobile/basedata/my/*")
public class AppPaymentyzfController extends BaseController{

@Resource
private AppPaymentyzfService appPaymentyzfService;
@Resource
private AppPaymentyzfDao appPaymentyzfDao;

    @RequestMapping(value="/getBankType",method=RequestMethod.POST)
    @ResponseBody
    public CommonModel getBankType(){
		return appPaymentyzfService.getBankType();
	}	
    
    @RequestMapping(value="/getidCardType",method=RequestMethod.POST)
    @ResponseBody
    public CommonModel getidCardType(){
		return appPaymentyzfService.getidCardType();
			
	}
    
    @RequestMapping(value="/ProvinceInfo",method=RequestMethod.POST)
    @ResponseBody
    public CommonModel ProvinceInfo(){
		return appPaymentyzfService.ProvinceInfo();
			
	}
    
    @RequestMapping(value="/CityInfo",method=RequestMethod.POST)
    @ResponseBody
    public CommonModel CityInfo(@RequestBody String provinceID){
		return appPaymentyzfService.CityInfo(provinceID);
			
	}
  
    @RequestMapping(value="/Infosyzf",method=RequestMethod.POST)
    @ResponseBody
    public CommonModel Infosyzf(@RequestBody JSONObject params){
        return appPaymentyzfService.Infosyzf(params);
			
	}
  
    @RequestMapping(value="/Signyzf",method=RequestMethod.POST)
    @ResponseBody
    public CommonModel Signyzf(@RequestBody JSONObject params){
    	
    	return appPaymentyzfService.Signyzf(params);
    }
 
    @RequestMapping(value="/getCardType",method=RequestMethod.POST)
    @ResponseBody
    public CommonModel getCardType(){
    	return appPaymentyzfService.getCardType();
    	
    }
    @RequestMapping(value="/querySignInfos",method=RequestMethod.POST)
    @ResponseBody
    public CommonModel querySignInfos(@RequestBody String jobNum){
		return appPaymentyzfService.querySignInfos(jobNum);
			
	}
    @RequestMapping(value="/querySignInfos1",method=RequestMethod.POST)
    @ResponseBody
    public CommonModel querySignInfos1(@RequestBody String jobNum){
		return appPaymentyzfService.querySignInfos1(jobNum);
			
	}
    /**
     * 获取支付金额
     * @param orderid 订单id
     * @return
     */
    @RequestMapping(value="/getPaymentAmount",method=RequestMethod.POST)
    @ResponseBody
    public CommonModel getPaymentAmount(@RequestBody JSONObject params){
    	return appPaymentyzfService.getPaymentAmount(params.getString("processInstanceId"),params.getString("prvid"));
    	
    }
    
    
}
