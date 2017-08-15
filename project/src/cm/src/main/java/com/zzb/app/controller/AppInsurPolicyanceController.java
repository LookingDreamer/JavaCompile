package com.zzb.app.controller;

import com.cninsure.core.exception.ControllerException;
import com.common.redis.IRedisClient;
import com.zzb.app.service.AppFastRenewalService;
import com.zzb.app.service.AppVehicleService;
import com.zzb.cm.service.INSBCarmodelinfoService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/cpmap/*")
public class AppInsurPolicyanceController {

	public static final String HOT_KEY_LIST = "hot_key_list";
	public static final String MODULE = "cm:zzb";
	@Resource
	private INSBCarmodelinfoService carmodelinfoService;
	@Resource
	private AppFastRenewalService appFastRenewalService;

	@Resource
	private AppVehicleService appVehicleService;

	@Resource
	private IRedisClient redisClient;

	/**
	 * TODO 参数转化
	 * 供应商列表接口 
	 * Y必须 N 
	 * zone 地区ID Y 
	 * channe 销售价格渠道 Y 
	 * comCode 代理人所属网点编码 Y 
	 * jobId 代理人工号  Y 
	 * insureWay 投保方式（net 网销，phone 电销，human 地面） N 
	 * isAvailable 渠道是否可用(可用的：true,不可用：false) N 
	 * 请求方式 POST 
	 * 请求地址 /cpmap/access
	 * 
	 * @param MSM_PARAM
	 *            必须的参数 MSM_PARAM:zone=440100;channel=2;jobId=620005858;comCode=
	 *            1244191733
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=PROVIDER;CmdId=search")
	@ResponseBody
	public String queryProvider(@RequestParam String MSM_PARAM)
			throws ControllerException {
		return appFastRenewalService.getAllProviderList(MSM_PARAM);
	}

	/**
	 * 按关键字搜索车型列表
	 * 
	 * url: /cpmap/access/ 
	 * type: post 
	 * MSM_HEADER 必须参数
	 * 
	 * @param MSM_PARAM  【dbtype=1;key=奥迪A6;count=10;off=1】   key(关键字)。 count(分页参数一页显示多少条数据)。off(分页参数当前页)
	 * @param MSM_HEADER  token 信息
	 *           
	 * @return
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=CAR;CmdId=NewSearchList;CmdVer=100;Token=test")
	@ResponseBody
	public String queryVehicleByType(String MSM_PARAM,
			String MSM_HEADER) {
		String result = "";
		try {
			result = appVehicleService.queryVehicleByType(MSM_HEADER, MSM_PARAM);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 查询热门车型
	 * 默认查询最热门车型前五条
	 * 
	 * url: /cpmap/access/ 
	 * type: post 
	 * 
	 * 无参数
	 * @return
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=CAR;CmdId=HotKeyList")
	@ResponseBody
	public Map<String, Object> queryHotKeyList() {
		Map<String, Object> result = new HashMap<String,Object>();
		Set<String> resultSet = redisClient.zrevrange(MODULE, HOT_KEY_LIST, 5);
		result.put("hotKeyList", resultSet);
		return result;
	}

	/**
	 * 按品牌系列获取车辆配置及车型规格
	 * 
	 * TODO  前台url 参数 为知  返回数据格式未知
	 * 
	 * @return
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST)
	@ResponseBody
	public String updateCarOwerInfo(String MSM_PARAM,String MSM_HEADER){
		return appVehicleService.queryVehicleByBrandName(MSM_HEADER, MSM_PARAM);
	}
	
	/**
	 * 支付和快递方式查询接口
	 * 请求方式 POST
	 * 请求地址 /cpmap/access
	 * @param MSM_PARAM 必须的参数
	 * 	id 供应商ID Y
	 *	cityCode 城市编码  N
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=PROVIDER;CmdId=PaymentofDairlyInfo;CmdVer=1.0.0;Token=")
	@ResponseBody
	public String queryPayExpress(@RequestParam String MSM_PARAM)throws ControllerException{
		return appFastRenewalService.queryPayExpressinfo(MSM_PARAM);
	}
	 
	/**
	 * 收货地址查询接口
	 * @param MSM_PARAM 必须的参数
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=DELIVERY;CmdId=METHOD;CmdVer=1.0.0;Token=")
	@ResponseBody
	public String queryShippingAddress(@RequestParam String MSM_PARAM)throws ControllerException{
		return appFastRenewalService.queryShippingAddressinfo(MSM_PARAM);
	}
	/**
	 * TODO 任务id应该从公共方法获取
	 * 获取任务id
	 * @param MSM_PARAM 必须的参数
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=Create")
	@ResponseBody
	public String queryTaskId(@RequestParam String MSM_PARAM)throws ControllerException{
		return appFastRenewalService.getTaskId(MSM_PARAM);
	}
	
	/**
	 * TODO参数转换
	 * 获取代理人操作权限
	 * @param MSM_PARAM 必须的参数  functional_id=B88C62ADF49D4343AA76829209CCD614   代理人id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=ATM;CmdId=GetFunctionAPI")
	@ResponseBody
	public String queryAgentPowerById(@RequestParam String MSM_PARAM)throws ControllerException{
		return appFastRenewalService.queryAgentPowerById(MSM_PARAM);
	}
	
	/**
	 * 绑定代理人与任务id
	 * @param MSM_PARAM 必须的参数  multiQuoteId=b49897f9e3df4888ae446deb66328f22;accountId=B88C62ADF49D4343AA76829209CCD614
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=Binding;CmdVer=100;Token=test")
	@ResponseBody
	public String bindingTaskIdAndAgentId(@RequestParam String MSM_PARAM)throws ControllerException{
		return appFastRenewalService.bindingTaskIdAndAgentId(MSM_PARAM);
	}

	/**
	 * 绑定保险公司与任务id
	 * @param MSM_PARAM 必须的参数 multiQuoteId=b49897f9e3df4888ae446deb66328f22;content=<supplierIdList><row>101</row><row>1056</row><row>107</row></supplierIdList>
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=SupplierIdList")
	@ResponseBody
	public String bindingTaskIdAndProviderIds(@RequestParam String MSM_PARAM)throws ControllerException{
		return appFastRenewalService.bindingTaskIdAndProviderIds(MSM_PARAM);
	}
	
	/**
	 * 保存投保地区
	 * @param MSM_PARAM 必须的参数 multiQuoteId=b49897f9e3df4888ae446deb66328f22;content=<insureAddress><provinceName>广东</provinceName><provinceCode>440000</provinceCode><cityName>广州市</cityName><cityCode>440100</cityCode></insureAddress>
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=InsureAddress;CmdVer=100;Token=test")
	@ResponseBody
	public String saveInsuranceArea(@RequestParam String MSM_PARAM)throws ControllerException{
		return appFastRenewalService.saveInsuranceArea(MSM_PARAM);
	}
	
	/**
	 * 保存投保日期区间
	 * @param MSM_PARAM 必须的参数 multiQuoteId=b49897f9e3df4888ae446deb66328f22;content=<insureDate><trafficInsEffectDate>2015-09-12 00:00:00</trafficInsEffectDate><trafficInsInvalidDate>2016-09-11 23:59:59</trafficInsInvalidDate><businessInsEffectDate>2015-09-12 00:00:00</businessInsEffectDate><businessInsInvalidDate>2016-09-11 23:59:59</businessInsInvalidDate></insureDate>
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=InsureDate")
	@ResponseBody
	public String saveInsuranceDate(@RequestParam String MSM_PARAM)throws ControllerException{
		return appFastRenewalService.saveInsuranceDate(MSM_PARAM);
	}
	
	/**
	 * 保存被保人信息and投保人信息
	 * @param MSM_PARAM 必须的参数multiQuoteId=b49897f9e3df4888ae446deb66328f22;content=<insuredInfo><personInfo><name>未上牌</name><certificateType>1</certificateType><certNumber>150621198008216214</certNumber><tel>13569412548</tel><email>hejie@qq.com</email></personInfo></insuredInfo>
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=insuredInfo")
	@ResponseBody
	public String saveInsurancePeopleInfo(@RequestParam String MSM_PARAM)throws ControllerException{
		return appFastRenewalService.saveInsurancePeopleInfo(MSM_PARAM);
	}
	
	/**
	 * 联系人信息  （和投保人信息一致）
	 * @param MSM_PARAM 必须的参数<contactsInfo><personInfo><name>合杰</name><certNumber>360103197605078221</certNumber><certificateType>1</certificateType><personName>合杰</personName><mobile>15226008691</mobile><email>test@qq.com</email></personInfo></contactsInfo>
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=ContactsInfo")
	@ResponseBody
	public String saveInsuranceContactsInfo(@RequestParam String MSM_PARAM)throws ControllerException{
		return appFastRenewalService.saveInsuranceContactsInfo(MSM_PARAM);
	}
	
	/**
	 * 保存车主信息 
	 * 请求方式 POST 
	 * 请求地址 /cpmap/access
	 * 
	 * @param MSM_PARAM
	 *            必须的参数 multiQuoteId=ef8dc768d51f4723a4f21c5a7285f95c;content=<ownerInfo><personInfo><name>合杰</name><certificateType>1</certificateType><certNumber>530129199005302693</certNumber><tel>13569412548</tel><email></email></personInfo></ownerInfo>
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=ownerInfo")
	@ResponseBody
	public String saveDriverOwerInfo(@RequestParam String MSM_PARAM)
			throws ControllerException {
		return appFastRenewalService.saveDriverOwerInfo(MSM_PARAM);
	}
	
	/**
	 * 保存车辆信息（行驶区域，平均行驶里程，上家商业承保公司ID） 
	 * 请求方式 POST 
	 * 请求地址 /cpmap/access
	 * 
	 * @param MSM_PARAM
	 *            必须的参数 multiQuoteId=ef8dc768d51f4723a4f21c5a7285f95c;content=<recentInsure><lastComId>0</lastComId><avkt>小于3万公里</avkt><drivingRegion>1</drivingRegion></recentInsure>
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=RecentInsure")
	@ResponseBody
	public String saveOtherCarInfo(@RequestParam String MSM_PARAM)
			throws ControllerException {
		return appFastRenewalService.saveOtherCarInfo(MSM_PARAM);
	}
	
	/**
	 * 保存车辆信息（基本信息） 
	 * 请求方式 POST 
	 * 请求地址 /cpmap/access
	 * 
	 * @param MSM_PARAM
	 *            必须的参数 multiQuoteId=b49897f9e3df4888ae446deb66328f22;content=<vehicleInfo><modelId>162</modelId><owner>未上牌</owner><modelCode>奥迪FV7181TCVT轿车</modelCode><totalVehicleWeight>1460</totalVehicleWeight><plateNumber>暂未上牌</plateNumber><firstRegisterDate>2014-08-10</firstRegisterDate>
	 *            	<vin>8888888888888888</vin><engineNo>123456789</engineNo><newVehicleFlag>true</newVehicleFlag><chgOwnerFlag >false</chgOwnerFlag><chgOwnerDate></chgOwnerDate><institutionType>1</institutionType><useProperty>1</useProperty><licenseModelCode>奥迪FV7181TCVT轿车</licenseModelCode>
	 *            <modelDesc>hahahahah</modelDesc><ratedPassengerCapacity>5</ratedPassengerCapacity><displacement>1.781</displacement><wholeWeight>1460</wholeWeight><tonnage>012</tonnage><rulePriceProvideType>0</rulePriceProvideType><userReplacementValue></userReplacementValue></vehicleInfo>
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=vehicleInfo")
	@ResponseBody
	public String saveBasicCarInfo(@RequestParam String MSM_PARAM)
			throws ControllerException {
		return appFastRenewalService.saveBasicCarInfo(MSM_PARAM);
	}
	
	/**
	 * 指定驾驶人 
	 * 请求方式 POST 
	 * 请求地址 /cpmap/access
	 * 
	 * @param MSM_PARAM
	 *            必须的参数
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=ownerInfo;CmdVer=100;Token=test")
	@ResponseBody
	public String updateDriverInfo(@RequestParam String MSM_PARAM)
			throws ControllerException {
		return appFastRenewalService.updateDriverInfo(MSM_PARAM);
	}
	
	/**
	 * 保险配置 
	 * 请求方式 POST 
	 * 请求地址 /cpmap/access
	 * 
	 * @param MSM_PARAM
	 *            必须的参数
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=InsureConfig")
	@ResponseBody
	public String updateInsurConfigure(@RequestParam String MSM_PARAM)
			throws ControllerException {
		return appFastRenewalService.updateInsureConfigure(MSM_PARAM);
	}

	/**
	 * 修改多方报价状态 
	 * 请求方式 POST 
	 * 请求地址 /cpmap/access
	 * 
	 * @param MSM_PARAM
	 *            必须的参数 MSM_PARAM:multiQuoteId=b49897f9e3df4888ae446deb66328f22
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access", method = RequestMethod.POST, params = "MSM_HEADER=CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=Save")
	@ResponseBody
	public String updateQuoteStatus(@RequestParam String MSM_PARAM)
			throws ControllerException {
		return appFastRenewalService.updateQuoteStatus(MSM_PARAM);
	}
	
	/**
	 * 获取保险配置信息 
	 * 请求方式 POST 
	 * 请求地址 /cpmap/insureConfig
	 * 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/insureConfig", method = RequestMethod.POST)
	@ResponseBody
	public String commonInsureConfig(@RequestParam String plankey)
			throws ControllerException {
		return appFastRenewalService.commonInsureConfig(plankey);
	}
	
	/**
	 * 根据选择保险公司获取补充信息交集
	 * 
	 * 请求方式 POST 
	 * 请求地址 /cpmap/supplementInfo
	 * 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/supplementInfo", method = RequestMethod.POST)
	@ResponseBody
	public String supplementInfo(@RequestParam String taskid)
			throws ControllerException {
		return appFastRenewalService.supplementInfo(taskid);
	}
	
}
