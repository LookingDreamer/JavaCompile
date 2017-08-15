package com.zzb.cm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCDeptService;
import com.common.PagingParams;
import com.zzb.app.service.AppCodeService;
import com.zzb.cm.service.INSBManualPriceService;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.mobile.model.CallPlatformQueryModel;
import com.zzb.mobile.model.CarModelInfoModel;
import com.zzb.mobile.model.ChoiceProviderIdsModel;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.InsuredConfig;
import com.zzb.mobile.model.InsuredConfigModel;
import com.zzb.mobile.model.InsuredQuoteCreateModel;
import com.zzb.mobile.model.SaveCarInfoModel;
import com.zzb.mobile.model.SearchProviderModel;
import com.zzb.mobile.model.SelectCarModel;
import com.zzb.mobile.model.WorkflowStartQuoteModel;
import com.zzb.mobile.service.AppInsuredQuoteService;
import com.zzb.mobile.service.AppRegisteredService;
import com.zzb.model.AppCodeModel;



/**
 * CM系统 车型投保
 */ 
@Controller
@RequestMapping("/modelinsurance/*")
public class INSBModelInsuranceController extends BaseController {
	@Resource
	private AppRegisteredService registeredService;
	@Resource
    private HttpServletRequest request;
	@Resource
	private AppCodeService codeService;
	@Resource
	private INSCDeptService inscDeptService;
	@Resource
	private INSBAgentService insbAgentService;
	@Resource
	private AppInsuredQuoteService insuredQuoteService;
	@Resource
	private INSBManualPriceService insbManualPriceService;
	/**
	 * 页面跳转  
	 * @param 
	 * @return
	 * @throws ControllerException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "modelinsurance", method = RequestMethod.GET)
	@ResponseBody 
	public ModelAndView modelinsurance(HttpSession session,@ModelAttribute InsuredConfig model,String id) throws ControllerException {
		ModelAndView mav = new ModelAndView("cm/modelinsurance/modelInsurance");
		//获取代理人信息
		INSBAgent temp = new INSBAgent();
		temp.setId(id);
//		temp.setId("f4514660537e11e5d12d53043cee09e7");
		INSBAgent agent= insbAgentService.getAgentInfo(temp);
		INSCDept dept =inscDeptService.queryById(agent.getDeptid());
		agent.setComname(dept.getComname());
		Map<String, String>  quotearea =insuredQuoteService.getQuoteAreaByAgentid(id);
		
		//获取投保信息（证件/行驶区域）
//		Map<String, Object> insMap=insbAgentService.getInsuranceInfo();
		mav.addObject("INSBAgent", agent); 
		//所属区域
		mav.addObject("quotearea", quotearea);
//		mav.addObject("insuranceinfo", insMap);		
		//所属性质
		List<Map<String, String>> UserType = insuredQuoteService.getCarUpdateByCode("UserType");
		mav.addObject("UserType", UserType);
		//车辆性质
		List<Map<String, String>> UseProps = insuredQuoteService.getCarUpdateByCode("UseProps");
		mav.addObject("UseProps",UseProps);
		//车价选择
		List<Map<String, String>> priceselection = insuredQuoteService.getCarUpdateByCode("priceselection");
		mav.addObject("priceselection", priceselection);
		return mav;
	}
	/**
	 * 信息录入
	 * @param plankey
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/saveinsurance", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView saveinsuranceScheme(HttpSession session)throws ControllerException{
		ModelAndView mav = new ModelAndView("");
//		String processinstanceid = null;
		System.out.println("进来了！");
//		CommonModel configmodel= insuredQuoteService.insuranceScheme("dzrmx",processinstanceid);
//		mav.addObject("configmodel", configmodel);
		return mav;
	}
	
	/**
	 * 根据车牌进行云查询
	 * @param plankey
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/getcarinfobycarnumber", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel getCarinfobyCarnumber(@RequestParam(value="carNumber") String carNumber)throws ControllerException{
		String processinstanceid = UUID.randomUUID().toString();
		CommonModel lastinsuremodel= insuredQuoteService.queryLastInsuredInfo(processinstanceid, carNumber);
		System.out.println(lastinsuremodel);
		return lastinsuremodel;
	}
	
	@RequestMapping(value = "/initcarmodelbrand", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initcarmodelbrand(HttpSession session)throws ControllerException{
		ModelAndView mav = new ModelAndView("cm/modelinsurance/carmodelbrand");
		System.out.println("进来了！");
//		CommonModel configmodel= insuredQuoteService.insuranceScheme("dzrmx",processinstanceid);
//		mav.addObject("configmodel", configmodel);
		return mav;
	}
	/**
	 * 1.调用工作流接口，获取实例id
	 * 2.根据代理人id获取代理人信息
	 * 3，保存车主信息，向车主信息表，人员表插入数据
	 * 4.向车辆信息表插入数据
	 * 5.报价信息总表插入投保地区
	 * 6.保存保单信息，插入一条数据默认为商业险
	 * @param createModel
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/create",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel createInsuredInit(@ModelAttribute InsuredQuoteCreateModel createModel,HttpSession session) throws ControllerException{
		INSCUser inscUser = (INSCUser) session.getAttribute("insc_user");
		createModel.setUserid(inscUser.getId());
		return insuredQuoteService.createInsuredInit(createModel);
	}
	/**
	 * 平台查询，获取上年保单信息，并入库
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/callplatformquery", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel callPlatformQuery(@ModelAttribute CallPlatformQueryModel model){
		System.out.println(model);
		return insuredQuoteService.callPlatformQuery(model);
	}
	
	/**
	 * 找不到车型--车型搜索
	 * @param modelName  	模糊查询车型名称
	 * @param pageSize		每页显示条数
	 * @param currentPage	当前第几页
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/searchcarmodel", method = RequestMethod.GET)
	@ResponseBody
	public String searchCarModel(@RequestParam(value="modelname") String modelName,@ModelAttribute PagingParams para)throws ControllerException{
		String currentPage = para.getOffset() + 1 + "";
		String pageSize = "5";
		CommonModel model =  insuredQuoteService.searchCarModel(modelName,pageSize,currentPage);
		Map<Object, Object> map = new HashMap<>();
		map.put("records", "10000");
		map.put("page", 1);
		if ("success".equals(model.getStatus())) {
			map.put("total",Long.valueOf(model.getMessage()));
		}else {
			map.put("total",0);
		}
		map.put("rows", model.getBody());
		JSONObject jsonObject = JSONObject.fromObject(map);
		return jsonObject.toString();
	}
	
	/**
	 * 获取保险配置方案列表
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/schemelist", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel schemeList(@RequestParam(value="processinstanceid") String processinstanceid,
			@RequestParam(value="agentnotitype", required=false) String agentnotitype)throws ControllerException{
		return insuredQuoteService.schemeList(processinstanceid, agentnotitype);
	}
	
	/**
	 * 查询保险配置信息
	 * @param plankey
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/insurancescheme", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel insuranceScheme(@RequestParam(value="plankey") String plankey,@RequestParam(value="processinstanceid") String processinstanceid)throws ControllerException{
		return insuredQuoteService.insuranceScheme(plankey, processinstanceid);
	}
	
	
	/**
	 * 查询供应商信息
	 * @param plankey
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/getproviderinfo", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel getProviderInfo(HttpSession session,@RequestParam(value="agentid") String agentid,@RequestParam(value="processinstanceid") String processinstanceid)throws ControllerException{
		SearchProviderModel providermodel = new SearchProviderModel();
		providermodel.setProcessinstanceid(processinstanceid);
//		providermodel.setChannel(agent.getChannelid());//渠道代码
		providermodel.setAgentid(agentid);//代理人ID
		Map<String, String>  quotearea =insuredQuoteService.getQuoteAreaByAgentid(agentid);
		providermodel.setCity(quotearea.get("cityCode"));//市代码
		providermodel.setProvince(quotearea.get("provinceCode"));//省代码
		return insuredQuoteService.searchProvider(providermodel);
	}
	/**
	 * 保存选中车型信息
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/selectcarmodel", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel updateCarModel(@ModelAttribute SelectCarModel model)throws ControllerException{
		return insuredQuoteService.carModelInfo(model);
	}
	/**
	 * 1.根据实例id查询车辆信息表，获取操作员信息
	 * 2.更新车辆信息表数据
	 * 3.向车型信息表插入车型信息
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/carmodelinfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel carModelInfo(@ModelAttribute CarModelInfoModel model)throws ControllerException{
		return insuredQuoteService.updateCarModelAndCarinfo(model);
	}
	
	/**
	 * 1.根据实例id查询报价信息总表，获取id
	 * 2.根据实例id查询车辆信息
	 * 3.向报价信息表插入数据，一个报价公司一条
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/choiceproviderids", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel choiceProviderIds(@ModelAttribute ChoiceProviderIdsModel model)throws ControllerException{
		return insuredQuoteService.choiceProviderIds(model);
	}
	
	/**
	 * 更新车辆信息表数据
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/savecarinfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel saveCarInfo(@ModelAttribute SaveCarInfoModel model)throws ControllerException{
		return insuredQuoteService.saveCarInfo(model);
	}
	
	/**
	 * 1.根据实例id查询已选择的报价公司（报价信息总表，报价信息表）
	 * 2.向保险险别选择表插入选择的已选择 的险别
	 * 3.保险公司险别报价表分别插入报价公司与选择险别对应关系
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/insuredconfig", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel insuredConfig(@RequestBody InsuredConfigModel model)throws ControllerException{
		return insuredQuoteService.insuredConfig(model);
	}
	
	/**
	 * 验证保险配置不支持的报价公司
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/verificationinsuredconfig", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel verificationInsuredConfig(@RequestBody InsuredConfigModel model)throws ControllerException{
		return insuredQuoteService.verificationInsuredConfig(model);
	}
	
	/**
	 * 投保信息录入成功，调用工作流获取子流程报价id
	 * @param processinstanceid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/workflowstartquote", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel  workflowStartQuote(@RequestBody WorkflowStartQuoteModel model)throws ControllerException{
		return insuredQuoteService.workflowStartQuote(model);
	}
	
	/**
	 * 初始化前端app页面字典表字段
	 * 
	 * @param String types ="itemtype,orderstatus,orderStatus" 
	 * @return json形式  List<AppCodeModel>   model包括  codeName,codeValue,codeType  三个字段
	 */
	@RequestMapping(value="/initcodetypes",method=RequestMethod.GET)
	@ResponseBody
	public List<AppCodeModel>  initAppCode(  String types){
		return codeService.queryByCodetype(types);
	} 
	
	/**
	 * 
	 * 接口描述：文件上传
	 * 请求地址	fileUpLoad
	 * @param 
	 */
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String addImage(HttpSession session, @RequestParam("file") MultipartFile file,
			@RequestParam("fileType") String fileType,@RequestParam("fileDescribes") String fileDescribes,
			@RequestParam("processinstanceid") String processinstanceid,@RequestParam("taskid") String taskid) {
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		Map<String, Object> map = insbManualPriceService.fileUpLoad(request, file, fileType, fileDescribes, loginUser.getUsercode(), processinstanceid, taskid);
		return JSONObject.fromObject(map).toString();
	}
	
	/**
	 * 删除已经上传的影像
	 * @param processinstanceid 实例id
	 * @param fileid 图片id
	 * @return
	 */
	@RequestMapping(value = "/deleteupdateimage", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel deleteUpdateImage(@RequestParam(value="processinstanceid") String processinstanceid,@RequestParam(value="fileid") String fileid){
		return insuredQuoteService.deleteUpdateImage(processinstanceid, fileid);
	}

	/**
	 * 1.根据实例id查询报价公司列表
	 * 2.查询供应商关联的险种
	 * 3.查询险种关联的影响挂件列表
	 * @param processinstanceid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/needuploadimage", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel needUploadImage(@RequestParam(value="processinstanceid") String processinstanceid)throws ControllerException{
		return insuredQuoteService.needUploadImage(processinstanceid); 
	}

}
