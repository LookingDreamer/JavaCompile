package com.zzb.mobile.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.zzb.app.model.bean.AppInstanceIdAndComcode;
import com.zzb.app.model.bean.AppKindPriceJson;
import com.zzb.app.model.bean.TaskAndRisk;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.service.INSBLastyearinsureinfoService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.APPBorderService;
import com.zzb.mobile.service.ApplicationBookService;
import com.zzb.mobile.service.INSBRiskKindPriceService;

/**
 * 
 * @author Administrator 此接口包括，报价明细接口（页面）,和获取订单详情信息接口（页面）
 *投保书页面
 */
@Controller
@RequestMapping("/mobile/basedata/AppDetaileMobile/*")
public class AppDetaileMobileController extends BaseController {

	@Resource
	private INSBRiskKindPriceService insbRiskKindPriceService;
	@Resource
	private APPBorderService appBorderService;
	@Resource
	private ApplicationBookService bookService;
	@Resource
	private INSBLastyearinsureinfoService insbLastyearinsureinfoService;
//	@Resource
//	private INSBCarkindpriceService insbCarkindpriceService;
	
//	@Resource
//	private AppRiskStatusService riskStatusService;
	/*
	 *  价格明细接口（页面）
	 */
	@RequestMapping(value = "riskKindPrice", method = RequestMethod.POST)
	@ResponseBody
	public String riskKindPrice(
			@RequestBody AppInstanceIdAndComcode instanceIdAndComcode) {

		return insbRiskKindPriceService.allDtailesKind(instanceIdAndComcode.getProcessInstanceId(),
				instanceIdAndComcode.getInscomcode());
	}

	/*
	 *  获取订单详情信息接口（页面）
	 */
	@RequestMapping(value = "aPPborder", method = RequestMethod.POST)
	@ResponseBody
	public String aPPborder(@RequestBody TaskAndRisk taskAndRisk) {

		return appBorderService.borderListToJson(taskAndRisk.getProcessInstanceId(),taskAndRisk.getPrvid());
	}
	/*
	 * 通过任务id和保险公司code，查询获得修改前的保险配置信息
	 */
	@RequestMapping(value="getDeployItem",method=RequestMethod.POST)
	@ResponseBody
	public String getDeployItem(@RequestBody AppKindPriceJson appKindPriceJson){
//		CommonModel commonModel=new CommonModel();
		return insbRiskKindPriceService.kindPriceList(appKindPriceJson.getProcessInstanceId(), appKindPriceJson.getInscomcode());
//		List<CarInsConfigVo>List= insbCarkindpriceService.getCarInsConfigByInscomcode(appKindPriceJson.getInscomcode(),appKindPriceJson.getProcessInstanceId());
//		JSONArray jsonObject=JSONArray.fromObject(List);
//		if(List!=null){
//			commonModel.setBody(jsonObject);
//			commonModel.setMessage("成功");
//			commonModel.setStatus("success");
//		}
//		JSONObject commonjsonObject=JSONObject.fromObject(commonModel);
//		return commonjsonObject.toString();
	}
	/*
	 * 此方法为解析json数据方法(以对象形式传入的形式 如：{key:value,...})
	 * json数据格式如：[{"TYPE":"01","VALUE":{"KEY":"1","UNIT":"","VALUE":"1"}}],
	 * kindPriceJson为前台传递的参数名称
	 * String inskindcode,String processInstanceId,String kindPriceJson
	 */
	@RequestMapping(value="getJsonResult",method=RequestMethod.POST)
	@ResponseBody
	public void getJsonResult(@RequestBody AppKindPriceJson appKindPriceJson){
		//判断险别名称进行修改
		String[] appInsKindCode=appKindPriceJson.getInskindcode().split(",");
		
		for(int i=0;i<appInsKindCode.length;i++){
		List<INSBCarkindprice> carkindpriceList=
				insbRiskKindPriceService.kindPrice(appKindPriceJson.getProcessInstanceId(),appInsKindCode[i]);
		JSONArray priceArrayItem=JSONArray.fromObject(appKindPriceJson.getKindPriceJson());
		List<String>strList=new ArrayList<String>();
			for (INSBCarkindprice carkindprice:carkindpriceList) {
				strList.add(priceArrayItem.get(i).toString());
				carkindprice.setSelecteditem("["+priceArrayItem.get(i).toString()+"]");
				
				JSONObject jsonArrayOBJ= (JSONObject) JSONArray.fromObject(appKindPriceJson.getKindPriceJson()).get(i);
			    Map<String, Object> mapJson1 = JSONObject.fromObject(jsonArrayOBJ.get("VALUE")); 
			    double a= Double.parseDouble((String) mapJson1.get("VALUE"));
			    carkindprice.setAmount(a);
				insbRiskKindPriceService.updateKindPriceItem(carkindprice);
				//System.out.println("更新");
			}
		}
	}
	/**
	 * 
	 * @param taskAndRisk
	 * @return 投保书页面
	 */
	@RequestMapping(value="getAppbook",method=RequestMethod.POST)
	@ResponseBody
	public String getAppbook(@RequestBody TaskAndRisk taskAndRisk){
		
		return bookService.applicationBoolSource(taskAndRisk.getProcessInstanceId(), taskAndRisk.getRisktype(),taskAndRisk.getPrvid());
	}
	/**
	 * 历史理赔次数@RequestBody String carno
	 */
	@RequestMapping(value="claimNumber",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel claimNumber(@RequestBody String taskid){
		return insbLastyearinsureinfoService.queryLastYearClainInfo(taskid);
	}
}
















					