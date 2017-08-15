package com.zzb.mobile.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.jobpool.Pool;
import com.cninsure.jobpool.Task;
import com.cninsure.jobpool.dispatch.DispatchTaskService;
import com.zzb.conf.service.INSBWorkflowsubService;

import com.zzb.mobile.model.lastinsured.LastYearPolicyInfoBean;
import net.sf.json.JSONObject;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.common.JsonUtil;
import com.common.WorkFlowUtil;
import com.zzb.cm.controller.vo.RenewalItemVO;
import com.zzb.cm.service.INSBRenewalService;
import com.zzb.extra.entity.INSBAgentTask;
import com.zzb.extra.model.SearchProviderModelForMinizzb;
import com.zzb.extra.service.INSBAgentTaskService;
import com.zzb.mobile.CheckAddQuoteProviderRoleModel;
import com.zzb.mobile.model.AppInsureinfoBean;
import com.zzb.mobile.model.CallPlatformQueryModel;
import com.zzb.mobile.model.CarModelInfoModel;
import com.zzb.mobile.model.CarowerInfoModel;
import com.zzb.mobile.model.ChoiceProviderIdsModel;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.DriversInfoModel;
import com.zzb.mobile.model.ExtendCommonModel;
import com.zzb.mobile.model.InsuredConfigModel;
import com.zzb.mobile.model.InsuredDateModel;
import com.zzb.mobile.model.InsuredOneConfigModel;
import com.zzb.mobile.model.InsuredQuoteCreateModel;
import com.zzb.mobile.model.LastInsuredRenewaByCarModel;
import com.zzb.mobile.model.OtherInsuredInfoModel;
import com.zzb.mobile.model.PeopleInfoModel;
import com.zzb.mobile.model.QueryLastInsuredByNumOrCarModel;
import com.zzb.mobile.model.RenewaSubmitModel;
import com.zzb.mobile.model.RenewalQuoteinfoModel;
import com.zzb.mobile.model.RenewaltemSaveModel;
import com.zzb.mobile.model.SaveCarInfoModel;
import com.zzb.mobile.model.SaveUploadImageModel;
import com.zzb.mobile.model.SearchCarModel;
import com.zzb.mobile.model.SearchProviderModel;
import com.zzb.mobile.model.SelectCarModel;
import com.zzb.mobile.model.UpdateSingleSiteModel;
import com.zzb.mobile.model.WorkFlowRestartConfModel;
import com.zzb.mobile.model.WorkflowStartQuoteModel;
import com.zzb.mobile.service.AppInsuredQuoteCopyService;
import com.zzb.mobile.service.AppInsuredQuoteService;

@Controller
@RequestMapping("/mobile/insured/quote/*")
public class AppInsuredQuoteController extends BaseController{

	@Resource
	private AppInsuredQuoteService insuredQuoteService;

	@Resource
	private INSBAgentTaskService insbAgentTaskService;

	@Resource
	private AppInsuredQuoteCopyService insuredQuoteCopyService;

    @Resource
    private INSBRenewalService renewalService;
    @Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
    @Resource
    private INSCCodeService codeService;
	@Resource
	private INSBWorkflowsubService workflowsubService;
	@Resource
	private DispatchTaskService dispatchService;
	
	/**
	 * 1.根据代理人id查询代理人所属机构 
	 * 2.根据机构查询所属区域
	 * @param agentid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/getquotearea",method=RequestMethod.GET)
	@ResponseBody
	public CommonModel getQuoteArea(@RequestParam(value="agentid") String agentid) throws ControllerException{
		return insuredQuoteService.getQuoteArea(agentid);
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
	public CommonModel createInsuredInit(@RequestBody InsuredQuoteCreateModel createModel) throws ControllerException{
        LogUtil.info("请求投保：" + JsonUtil.serialize(createModel));
		CommonModel cm = insuredQuoteService.createInsuredInit(createModel);
		if("success".equals(cm.getStatus()) && createModel.getChanneluserid()!=null && createModel.getChanneluserid().trim().length()>0){
			INSBAgentTask insbAgentTask = new INSBAgentTask();
			insbAgentTask.setAgentid(createModel.getChanneluserid().trim());
			Map<String, Object> body = (Map<String, Object>)cm.getBody();
			insbAgentTask.setTaskid((String)body.get("processinstanceid"));
			insbAgentTask.setStatus("0");
			insbAgentTask.setCreatetime(new Date());
			insbAgentTaskService.saveAgentTask(insbAgentTask);
		}
		return cm;
	}

	/**
	 * task-122 按照任务号，提供该单所有数据接口，供前端使用
	 * 将该报价的车辆信息、关系人信息、投保公司、保险配置复制，同时跳转到车险投保首页，将这些信息带入到新的报价流程中。
	 * @param model
	 * @return
	 * @throws ControllerException
     */
	@RequestMapping(value="/copy",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel copy(@RequestBody SearchProviderModel model) throws ControllerException{
		LogUtil.info("复制并创建新报价,taskId:" + model.getProcessinstanceid()+"inscomcode:"+model.getPrvid());
		CommonModel cm = insuredQuoteCopyService.copy(model.getProcessinstanceid(), model.getPrvid());
		return cm;
	}

	/**
	 * 车辆云查询--推荐上年选择的保险公司
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/recommendprovider", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel recommendProvider(@RequestBody SearchProviderModel model)throws ControllerException{
		return insuredQuoteService.recommendProvider(model);
	}
	/**
	 * 云查询，查询上年保单信息
	 * @param processinstanceid 实例id
	 * @param plateNumber 车牌号
	 * @return CommonModel
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/querylastinsuredinfo", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel queryLastInsuredInfo(@RequestParam(value="processinstanceid") String processinstanceid ,@RequestParam(value="plateNumber") String plateNumber)throws ControllerException{
		return insuredQuoteService.queryLastInsuredInfo(processinstanceid, plateNumber);
	}
	
	/**
	 * 国政通查询--热门推荐车型
	 * 默认查询最热门车型前五条
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/recommendcarmodel", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel recommendCarModel()throws ControllerException{
		return insuredQuoteService.recommendCarModel();
	}
	
	
	/**
	 * 找不到车型--车型搜索
	 * param modelName  	模糊查询车型名称
	 * param pageSize		每页显示条数
	 * param currentPage	当前第几页
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/searchcarmodel", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel searchCarModel(@RequestBody SearchCarModel model)throws ControllerException{
		return insuredQuoteService.searchCarModel(model.getModelname(), model.getPagesize(), model.getCurrentpage());
	}
	
	/**
	 * 找不到车型--车型搜索
	 * param modelName  	模糊查询车型名称
	 * param pageSize		每页显示条数
	 * param currentPage	当前第几页
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/searchcarmodelvin", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel searchCarModelVin(@RequestBody SearchCarModel model)throws ControllerException{
		return insuredQuoteService.searchCarModelVin(model.getModelname(), model.getPagesize(), model.getCurrentpage(), model.getCarlicenseno(), model.getAgentid());
	}
	
	/**
	 * 保存选中车型信息
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/selectcarmodel", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel updateCarModel(@RequestBody SelectCarModel model)throws ControllerException{
        LogUtil.info("保存车型信息：" + JsonUtil.serialize(model));
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
	public CommonModel carModelInfo(@RequestBody CarModelInfoModel model)throws ControllerException{
        LogUtil.info("保存车辆与车型信息：" + JsonUtil.serialize(model));
        return insuredQuoteService.updateCarModelAndCarinfo(model);
	}
	/**
	 * 更新车辆信息表数据
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/savecarinfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel saveCarInfo(@RequestBody SaveCarInfoModel model)throws ControllerException{
        LogUtil.info("保存车辆信息：" + JsonUtil.serialize(model));
		return insuredQuoteService.saveCarInfo(model);
	}
	
	/**
	 * 1.根据区域查询协议应用范围，获取该区域协议 
	 * 2.根据协议id获取供应商列表 
	 * 3.根据代理人供应商关系表查询供应商列表，和2结果取交集
	 * 4.根据交集查询供应商表，获取供应商列表
	 * 5.根据供应商id查询协议表，获取该供应商对应的协议列表 
	 * 6.根据协议id，查询出单网点表，获取分公司和网点id,对应的机构id，以及地址信息
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/searchprovider", method = RequestMethod.POST)
	@ResponseBody
	public ExtendCommonModel searchProvider(@RequestBody SearchProviderModel model)throws ControllerException{
		 LogUtil.info("查询供应商信息：" + JsonUtil.serialize(model));
		//是否检查数据完整，1 检查  0 不检查（前端偶尔不调用savecarinfo接口，导致车辆相关信息丢失）
		String strInspect=model.getInspect();
		if(strInspect!=null&&strInspect.equals(SearchProviderModel.INSPECT_1)){
			ExtendCommonModel commonModel =insuredQuoteService.inspectInfo(model.getProcessinstanceid());
			if(!commonModel.getStatus().equals(CommonModel.STATUS_SUCCESS)){
				LogUtil.info("查询供应商信息-数据校验：" + JsonUtil.serialize(commonModel));
				return commonModel;
			}
		}
		return insuredQuoteService.searchProvider(model);
	}
///channel/queryChannelProviderList?city=440100&channelinnercode=minizzb
	@RequestMapping(value = "/searchproviderForMinizzb", method = RequestMethod.POST)
	@ResponseBody
	public ExtendCommonModel searchProviderForMinizzb(@RequestBody SearchProviderModelForMinizzb model)throws ControllerException{
		String channelinnercode = "minizzb";//usersource
		model.setUsersource(channelinnercode);
		return insuredQuoteService.searchProviderForMinizzb(model);
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
	public CommonModel choiceProviderIds(@RequestBody ChoiceProviderIdsModel model)throws ControllerException{
        LogUtil.info("保存供应商：" + JsonUtil.serialize(model));
		return insuredQuoteService.choiceProviderIds(model);
	}
	/**
	 * 获取保险配置方案列表
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/schemelist", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel schemeList(@RequestParam(value="processinstanceid", required=false) String processinstanceid,
			@RequestParam(value="agentnotitype") String agentnotitype)throws ControllerException{
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
	 * 查询已选择的保险配置信息
	 * @param proid        供应商id
	 * @param processinstanceid       任务id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/queryselectedinsure", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel querySelectedInsure(@RequestParam(value="proid",required=false) String proid,@RequestParam(value="processinstanceid")String processinstanceid)throws ControllerException{
		return insuredQuoteService.querySelectedInsure(proid, processinstanceid);
	}
	
	/**
	 * 投保信息查询 多方
	 * @param processinstanceid
	 * @param inscomcode
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/queryinsureinfo", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel queryInsureInfo(@RequestParam(value="processinstanceid") String processinstanceid,@RequestParam(value="inscomcode") String inscomcode)throws ControllerException{
		return insuredQuoteService.queryInsureInfoHis(processinstanceid, inscomcode);
	}
	
	/**
	 * 保存投保信息
	 * @param appInsureinfoBean
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/saveorupdateinsureinfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel saveOrUpdateInsureInfo(@RequestBody AppInsureinfoBean appInsureinfoBean)throws ControllerException{
        LogUtil.info("保存投保信息：" + JsonUtil.serialize(appInsureinfoBean));
		return insuredQuoteService.saveOrUpdateInsureInfo(appInsureinfoBean);
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
        LogUtil.info("保存保险配置：" + JsonUtil.serialize(model));
		return insuredQuoteService.insuredConfig(model);
	}
	/**
	 * 1.根据实例id获取选择的报价信息总表id
	 * 2.获取报价信息表，报价公司id列表
	 * 3.根据供应商id查询险种投保数据项，获取需要填写的补充信息
	 * @param processinstanceid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/supplementinfo", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel supplementInfo(@RequestParam(value="processinstanceid") String processinstanceid)throws ControllerException{
		return insuredQuoteService.supplementInfo(processinstanceid);
	}
	/**
	 * 保存被保人、投保人 
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/savepeopleinfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel peopleInfo(@RequestBody PeopleInfoModel model)throws ControllerException{
        LogUtil.info("保存人员信息：" + JsonUtil.serialize(model));
		return insuredQuoteService.peopleInfo(model);
	}
	/**
	 * 保存车主信息
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/carowerinfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel carowerInfo(@RequestBody CarowerInfoModel model)throws ControllerException{
        LogUtil.info("保存车主信息：" + JsonUtil.serialize(model));
		return insuredQuoteService.carowerInfo(model);
	}
	/**
	 * 保存投保时间
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/insureddate", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel insuredDate(@RequestBody InsuredDateModel model)throws ControllerException{
        LogUtil.info("保存投保日期：" + JsonUtil.serialize(model));
		return insuredQuoteService.insuredDate(model);
	}
	
	/**
	 * 保存指定驾驶人信息
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/driversinfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel driversInfo(@RequestBody DriversInfoModel model)throws ControllerException{
        LogUtil.info("保存驾驶人信息：" + JsonUtil.serialize(model));
		return insuredQuoteService.driversInfo(model);
	}
	/**
	 * 1.更新车辆信息表，增加行驶区域，上年云南查询投保公司
	 * 2.更新投保人，被保人，车主信息，增加邮箱，联系方式
	 * 3.保存投保补充信息
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/otherinsuredinfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel otherInsuredInfo(@RequestBody OtherInsuredInfoModel model)throws ControllerException{
        LogUtil.info("保存其他信息：" + JsonUtil.serialize(model));
		return insuredQuoteService.otherInsuredInfo(model);
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
	public ExtendCommonModel needUploadImage(@RequestParam(value="processinstanceid",required=true) String processinstanceid
	,@RequestParam(value="companyId",required=false) String companyId
	,@RequestParam(value="status",required=false) String status
	)throws ControllerException{
		//return insuredQuoteService.needUploadImage(processinstanceid);
		LogUtil.info("接口needuploadimage提交的数据：processinstanceid = " + processinstanceid +" 协议companyId:"+companyId+" status"+status);
		return insuredQuoteService.needUploadImageByCodeType(processinstanceid, companyId, status);
	}
	/**
	 * 查询已经上传成功的影像信息
	 * @param processinstanceid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/alreadyuploadimage", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel alreadyUploadImage(@RequestParam(value="processinstanceid") String processinstanceid)throws ControllerException{
		return insuredQuoteService.alreadyUploadImage(processinstanceid);
	}
	/**
	 * 查询 我的影像
	 * @param agentnum 工号
	 * @param carlicenseno 车牌
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/myuploadimage", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel myUploadImage(@RequestParam(value="agentnum",required=true) String agentnum,@RequestParam(value="carlicenseno",required=false) String carlicenseno)throws ControllerException{
		return insuredQuoteService.myUploadImage(agentnum,carlicenseno);
	}

	/**
	 * 更新报价总表insurancebooks字段为1
	 * @param processinstanceid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/saveinsurancebooks", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel saveInsuranceBooks(@RequestParam(value="processinstanceid") String processinstanceid)throws ControllerException{
		return insuredQuoteService.saveInsuranceBooks(processinstanceid);
	}
	/**
	 * 根据协议获取的出单网点
	 * @param pid 协议id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/getprovidersinglesite", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel getProviderSingleSite(@RequestParam(value="pid") String pid,@RequestParam(value="processinstanceid") String processinstanceid)throws ControllerException{
		return insuredQuoteService.getProviderSingleSite(processinstanceid, pid);
	}
	/**
	 * 修改供应商出单网点
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/updatesinglesite", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel updateSingleSite(@RequestBody UpdateSingleSiteModel model)throws ControllerException{
        LogUtil.info("保存出单网点：" + JsonUtil.serialize(model));
		return insuredQuoteService.updateSingleSite(model);
	}
	/**
	 * 根据协议id查询该供应商云查询需要输入的条件
	 * @param pid 供应商id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/querytermsbypid", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel queryTermsByPid(@RequestParam(value="pid") String pid)throws ControllerException{
		return insuredQuoteService.queryTermsByAgreeid(pid);
	}
	/**
	 * 保存上传的影像
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/saveuploadimage", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel saveUploadImage(@RequestBody SaveUploadImageModel model)throws ControllerException{
		return insuredQuoteService.saveUploadImage(model);
	}
	/**
	 * 调用云查询接口，根据上年投保单号查询或者车架号和发动机号查询，获取上年保单数据
	 * param type 类型  0 商业险保单号 1交强险保单号
	 * param data 传入数据
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/querylastinsuredbynumorcar", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel queryLastInsuredByNumOrCar(@RequestBody QueryLastInsuredByNumOrCarModel model)throws ControllerException{
		return insuredQuoteService.queryLastInsuredByNumOrCar(model);
	}
	/**
	 * 查询车辆车型信息
	 * @param processinstanceid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/querycarinfomodel", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel queryCarinfoModel(@RequestParam(value="processinstanceid") String processinstanceid)throws ControllerException{
		return insuredQuoteService.queryCarinfoModel(processinstanceid);
	}
	/**
	 * 查询报价公司,过滤已经选择的保险公司
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/addquoteprovider", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel addQuoteProvider(@RequestBody SearchProviderModel model)throws ControllerException{
		return insuredQuoteService.addQuoteProvider(model);
	}
	
	/**
	 * 单个报价公司保险配置修改
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/updateinduredconfig", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel updateInduredConfig(@RequestBody InsuredOneConfigModel model)throws ControllerException{
        LogUtil.info("保存供应商保险配置：" + JsonUtil.serialize(model));
		return insuredQuoteService.updateInduredConfig(model);
	}

	/**
	 * minizzb支持多个修改
	 * 单个报价公司保险配置修改
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/updateinduredconfigForMinizzb", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel updateinduredconfigForMinizzb(@RequestBody InsuredOneConfigModel model)throws ControllerException{
        LogUtil.info("保存供应商保险配置：" + JsonUtil.serialize(model));

		CommonModel commonModel = new CommonModel();
		String pid = model.getPid();
		if(StringUtil.isEmpty(pid)){
			commonModel.setStatus("fail");
			commonModel.setMessage("供应商id不能为空");
			return commonModel;
		}
		String[] pids = pid.split(",");
		for(String proid : pids) {
			commonModel = insuredQuoteService.updateInduredConfig(model);
			if("fail".equals(commonModel.getStatus()))break;
		}
		return commonModel;
	}
	
	/**
	 * 投保信息录入成功，调用工作流获取子流程报价id
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/workflowstartquote", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel  workflowStartQuote(@RequestBody WorkflowStartQuoteModel model)throws ControllerException{
        LogUtil.info("启动投保工作流：" + JsonUtil.serialize(model));
		return insuredQuoteService.workflowStartQuote(model);
	}
	
	/**
	 * 在已经选择的增加报价公司
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/saveaddquoteqrovider", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel  saveAddQuoteProvider(@RequestBody ChoiceProviderIdsModel model)throws ControllerException{
        LogUtil.info("增加供应商：" + JsonUtil.serialize(model));
		return insuredQuoteService.saveAddQuoteProvider(model);
	}
	
	/**
	 * 修改保险配置，调用工作流
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/workflowrestartconf", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel  workFlowRestartConf(@RequestBody WorkFlowRestartConfModel model)throws ControllerException{
        LogUtil.info("重启投保工作流：" + JsonUtil.serialize(model));
		return insuredQuoteService.workFlowRestartConf(model);
	}

	/**
	 * 支持多家保险公司重新报价
	 * 修改保险配置，调用工作流
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/workflowrestartconfForMinizzb", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel  workFlowRestartConfForMinizzb(@RequestBody WorkFlowRestartConfModel model)throws ControllerException{
        LogUtil.info("minizzb重启投保工作流：" + JsonUtil.serialize(model));
		CommonModel commonModel = new CommonModel();
		String pid = model.getPid();
		if(StringUtil.isEmpty(pid)){
			commonModel.setStatus("fail");
			commonModel.setMessage("供应商id不能为空");
			return commonModel;
		}
		String[] pids = pid.split(",");
		for(String proid : pids) {
			commonModel = insuredQuoteService.workFlowRestartConf(model);
			if("fail".equals(commonModel.getStatus()))break;
		}
		return commonModel;
	}
	
	/**
	 * 验证单个报价公司修改保险配置，险别信息验证，调用规则验证
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/checkupdateinsureconf", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel  checkUpdateInsureConf(@RequestBody InsuredOneConfigModel model)throws ControllerException{
        LogUtil.info("校验供应商保险配置：" + JsonUtil.serialize(model));
		return insuredQuoteService.checkUpdateInsureConf(model);
	}

	/**
	 * minizzb 支持多个修改
	 * 验证单个报价公司修改保险配置，险别信息验证，调用规则验证
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/checkupdateinsureconfForMinizzb", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel  checkUpdateInsureConfForMinizzb(@RequestBody InsuredOneConfigModel model)throws ControllerException{
        LogUtil.info("校验供应商保险配置：" + JsonUtil.serialize(model));
		CommonModel commonModel = new CommonModel();
		String pid = model.getPid();
		if(StringUtil.isEmpty(pid)){
			commonModel.setStatus("fail");
			commonModel.setMessage("供应商id不能为空");
			return commonModel;
		}
		String[] pids = pid.split(",");
		for(String proid : pids) {
			commonModel = insuredQuoteService.checkUpdateInsureConf(model);
			if("fail".equals(commonModel.getStatus()))break;
		}
		return commonModel;
	}

	/**
	 * 平台查询，获取上年保单信息，并入库
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/callplatformquery", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel callPlatformQuery(@RequestBody CallPlatformQueryModel model){
		return insuredQuoteService.callPlatformQuery(model);
	}

	@RequestMapping(value = "/callInsureInfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel callInsureInfo(@RequestBody CallPlatformQueryModel model){
		LogUtil.info("查询投保信息 callInsureInfo：" + JsonUtil.serialize(model));
		CommonModel commonModel = new CommonModel();
		if (model == null || StringUtil.isEmpty(model.getQueryFlag())) {
			commonModel.setStatus("fail");
			commonModel.setMessage("QueryFlag不能为空");
			return commonModel;
		}
		//queryFlag ：quickInsurancePolicy 我要比价，平台查询； queryLastYearPolicy 平台查询的

		return insuredQuoteService.callPlatformQuery(model);
	}

	/**
	 * 页面初始化 (车辆信息)
	 * @param processinstanceid 实例id
	 * @param webpagekey 0 车辆信息，1车型信息，2供应商列表 
	 * @return 
	 */
	@RequestMapping(value = "/findinitwebpage", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel findInitWebPage(@RequestParam(value="processinstanceid") String processinstanceid,@RequestParam(value="webpagekey") String webpagekey){
		return insuredQuoteService.findInitWebPage(processinstanceid,webpagekey);
	}
	
	/**快速续保接口--start*************/

    /**
     * 快速续保，查询供应商协议公司
     * @param model
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/fastrenewproviders", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel fastRenewProviders(@RequestBody SearchProviderModel model)throws ControllerException{
        LogUtil.info("快速续保查询供应商：" + JsonUtil.serialize(model));
        return insuredQuoteService.fastRenewProviders(model);
    }

	/**
	 * 车牌+车主搜索上一年保单信息
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/lastinsuredrenewabycar", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel lastInsuredRenewaByCar(@RequestBody LastInsuredRenewaByCarModel model)throws ControllerException{
        LogUtil.info("快速续保查询上年投保信息：" + JsonUtil.serialize(model));
		return insuredQuoteService.lastInsuredRenewaByCar(model);
	}

    /**
     * 保存续保投保信息：
     * @param model
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/saverenewalquoteinfo", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel saverenewalquoteinfo(@RequestBody RenewalQuoteinfoModel model)throws ControllerException{
        LogUtil.info("快速续保保存投保信息：" + JsonUtil.serialize(model));
        return insuredQuoteService.saveRenewalQuoteinfo(model);
    }

    /**
     * 获取续保查询数据项
     * @return
     */
    @RequestMapping(value = "/getrenewalquoteitems", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel getrenewalquoteitems(@RequestBody RenewalQuoteinfoModel model){
        CommonModel result = new CommonModel();

        List<RenewalItemVO> renewalConfigItems = renewalService.getRenewalConfigItems(model.getAgreementid(), model.getProcessinstanceid(), model.getInscomcode());

        result.setStatus("success");
        result.setBody(renewalConfigItems);
        return result;
    }

    /**
     * 保存续保查询数据项：
     * @param model
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/saverenewalquoteitems", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel saverenewalquoteitems(@RequestBody RenewaltemSaveModel model)throws ControllerException{
        LogUtil.info("快速续保保存数据项：" + JsonUtil.serialize(model));
        CommonModel result = new CommonModel();
        boolean resultData = renewalService.saveRenewalQuoteitems(model);

        if (resultData) {
            result.setStatus("success");
            result.setMessage("保存成功");
        } else {
            result.setStatus("fail");
            result.setMessage("保存失败");
        }

        return result;
    }
	
	/**
	 * 快速续保，平台查询，获取上年保单信息，并入库
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/fastrenewcallplatformquery", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel fastRenewCallPlatformQuery(@RequestBody CallPlatformQueryModel model){
		return insuredQuoteService.fastRenewCallPlatformQuery(model);
	}


    /**
     * 快速续保提交
     * @param model
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/renewasubmit", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel renewaSubmit(@RequestBody RenewaSubmitModel model)throws ControllerException{
        LogUtil.info("续保投保：" + JsonUtil.serialize(model));
        return insuredQuoteService.renewaSubmit(model);
    }

    /**
     * 续保退回修改获取数据
     * @return
     */
    @RequestMapping(value = "/getbackrenewaldata", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel getbackrenewaldata(@RequestBody RenewalQuoteinfoModel model){
        CommonModel result = new CommonModel();
        Map<String, Object> data = new HashMap<>(2);

        List<RenewalItemVO> renewalConfigItems = renewalService.getRenewalConfigItems(model.getAgreementid(), model.getProcessinstanceid(), model.getInscomcode());
        data.put("renewalItems", renewalConfigItems);

        Map<String,Object> map = new HashMap<>();
        List<INSCCode> agentNotis = codeService.queryINSCCodeByCode("agentnoti", "agentnoti1");
        if(agentNotis != null && agentNotis.size() >0 ){
            for (INSCCode inscCode : agentNotis) {
                map.put(inscCode.getCodename(), inscCode.getCodevalue());
            }
        }
        data.put("agentnoti", map);

        result.setStatus("success");
        result.setBody(data);
        return result;
    }

    /**
     * 续保退回修改提交保存
     * @param model
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/renewasubmitback", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel renewasubmitback(@RequestBody RenewaltemSaveModel model)throws ControllerException{
        LogUtil.info("续保退回修改提交保存：" + JsonUtil.serialize(model));
        CommonModel result = new CommonModel();

        boolean resultData = renewalService.saveRenewalQuoteitems(model);

        try {
            String workflowinstanceid = insuredQuoteService.saveRemarkInTabSubtrack(model.getProcessinstanceid(), model.getInscomcode(), model.getRemark(), model.getRemarkcode(), "admin");

			workflowsubService.updateWorkFlowSubData(null, workflowinstanceid, "19", "Completed", "核保退回", "修改提交", "admin");
			workflowsubService.updateWorkFlowSubData(null, workflowinstanceid, "18", "Reserved", "人工核保", "", null);
			taskthreadPool4workflow.execute(new Runnable() {
				@Override
				public void run() {
					Task task = new Task();
					task.setProInstanceId(model.getProcessinstanceid());
					task.setSonProInstanceId(workflowinstanceid);
					task.setPrvcode(model.getInscomcode());
					task.setTaskTracks("admin");
					task.setTaskName("人工核保");
					task.setTaskcode("18");
					Pool.addOrUpdate(task);
					try {
						Thread.sleep(2000);
						dispatchService.dispatchTask(task);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
            //WorkFlowUtil.updateInsuredInfoNoticeWorkflow(workflowinstanceid, "admin", "核保退回", "");
        } catch (Exception e) {
            LogUtil.error("续保退回修改提交保存失败("+model.getProcessinstanceid()+","+model.getInscomcode()+")："+e.getMessage());
            e.printStackTrace();
        }

        if (resultData) {
            result.setStatus("success");
            result.setMessage("保存成功");
        } else {
            result.setStatus("fail");
            result.setMessage("保存失败");
        }

        return result;
    }

    /**快速续保接口--end***************/
	
	/**
	 * 根据主流程id获取    保存投保书的的页面位置（报价信息总表）
	 * @param processinstanceid
	 * @return
	 */
	@RequestMapping(value = "/getwebpagekey", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel getWebpagekey(@RequestParam(value="processinstanceid") String processinstanceid){
		return insuredQuoteService.getWebpagekey(processinstanceid);
	}
	
	/**
	 * 增加报价公司，调用工作流 （老的）
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/addquoteproviderworkflow", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel addQuoteProviderWorkflowold(@RequestBody ChoiceProviderIdsModel model){
		return insuredQuoteService.addQuoteProviderWorkflowold(model);
	}
	
	/**
	 * 投保书详情查询
	 * @param processinstanceid
	 * @return
	 */
	@RequestMapping(value = "/insurancebooksearch", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel insuranceBookSearch(@RequestParam(value="processinstanceid") String processinstanceid){
		return insuredQuoteService.insuranceBookSearch(processinstanceid);
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
		return insuredQuoteService.deleteUpdateImage(processinstanceid,fileid);
	}
	
	/**
	 * 查询保险配置信息--被保人/投保人/权益索赔人信息
	 * @param taskid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/queryOtherPersonInfo", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel queryOtherPersonInfo(@RequestParam(value="taskid") String taskid)throws ControllerException{
		return insuredQuoteService.queryOtherPersonInfo(taskid);
	}
	
	/**
	 * 投保信息查询 单方
	 * @param processinstanceid
	 * @param inscomcode
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/queryinsureinfohis", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel queryInsureInfoHis(@RequestParam(value="processinstanceid") String processinstanceid,@RequestParam(value="inscomcode") String inscomcode)throws ControllerException{
		return insuredQuoteService.queryInsureInfoHis(processinstanceid,inscomcode);
	}
	/**
	 * 查询平台返回的出险信息
	 * @param processinstanceid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/queryclaiminfo", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel queryClaiminfo(@RequestParam(value="processinstanceid") String processinstanceid)throws ControllerException{
		return insuredQuoteService.queryClaiminfo(processinstanceid);
	}
	/**
	 * 新增供应商校验险种，调用规则
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/checkaddquoteproviderrole", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel checkAddQuoteProviderRole(@RequestBody CheckAddQuoteProviderRoleModel model)throws ControllerException {
		return insuredQuoteService.checkAddQuoteProviderRole(model);
	}
	
	/**
	 * 增加报价公司，调用工作流
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/addquoteproviderworkflownew", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel addQuoteProviderWorkflow(@RequestBody CheckAddQuoteProviderRoleModel model){
		return insuredQuoteService.addQuoteProviderWorkflow(model);
	}

	/**
	 * 增加报价公司，调用工作流
	 * param taskid
	 * @return
	 */
	@RequestMapping(value = "/pay", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel pay(@RequestBody JSONObject jsonObject){
		String taskid = jsonObject.getString("taskid");
		return insuredQuoteService.pay(taskid);
	}
}
