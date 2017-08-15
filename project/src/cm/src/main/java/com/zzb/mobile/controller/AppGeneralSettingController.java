package com.zzb.mobile.controller;

import javax.annotation.Resource;

import com.zzb.conf.service.INSBRegionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.GeneralSetPram;
import com.zzb.mobile.service.AppGeneralSettingService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mobile/basedata/my/generalSetting/*")
public class AppGeneralSettingController extends BaseController{
	
	@Resource
	private AppGeneralSettingService generalSettingService;
	@Resource
	private INSBAgentService insbAgentService;

	@Resource
	private INSBRegionService regionservice;

	@RequestMapping(value="/updateMsgAcceptingStatus",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel updateMsgAcceptingStatus(@RequestBody GeneralSetPram generalSetPram) throws ControllerException{
		return generalSettingService.updateMsgAcceptingStatus(generalSetPram.getJobNum(), generalSetPram.getMessage());
	}
	
	@RequestMapping(value="/updatePictureUploadQuality",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel updatePictureUploadQuality(@RequestBody GeneralSetPram generalSetPram) throws ControllerException{
		return generalSettingService.updatePictureUploadQuality(generalSetPram.getJobNum(), generalSetPram.getImageCompression());
	}

	/**
	 * 修改续保提醒时间
	 * @param remindTime 续保提醒时间
	 * @param jobNum 代理人工号
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/updateRemindRenewalTime",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel updateRemindRenewalTime(@RequestBody GeneralSetPram generalSetPram) throws ControllerException{
		CommonModel commonModel = new CommonModel();
		INSBAgent agent = new INSBAgent();
		agent.setJobnum(generalSetPram.getJobNum());
		agent = insbAgentService.queryOne(agent);
		if(agent==null){
			commonModel.setStatus("fail");
			commonModel.setMessage("代理人不存在");
			return commonModel;
		}
		agent.setRenewaltime(Integer.parseInt(generalSetPram.getRemindTime()));
		insbAgentService.saveOrUpdateAgent(new INSCUser(), agent);
		commonModel.setStatus("success");
		commonModel.setMessage("续保提醒时间修改成功");
		return commonModel;
	}
	
	@RequestMapping(value="/getCommonOutDept",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel getCommonOutDept(@RequestBody GeneralSetPram generalSetPram) throws ControllerException{
		return generalSettingService.getCommonOutDept(generalSetPram.getJobNum());
	}
	
	/**
	 * 修改 常用出单网点
	 * @param usuallyBranchCode 常用出单网点代码 
	 * @param agent 代理人工号
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/updateCommonOutDept",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel updateCommonOutDept(@RequestBody GeneralSetPram generalSetPram) throws ControllerException{
		CommonModel commonModel = new CommonModel();
		INSBAgent agent = new INSBAgent();
		agent.setJobnum(generalSetPram.getJobNum());
		agent = insbAgentService.queryOne(agent);
		if(agent==null){
			commonModel.setStatus("fail");
			commonModel.setMessage("代理人不存在");
			return commonModel;
		}
		agent.setCommonusebranch(generalSetPram.getUsuallyBranchCode());
		insbAgentService.saveOrUpdateAgent(new INSCUser(), agent);
		commonModel.setStatus("success");
		commonModel.setMessage("常用出单网点修改成功");
		return commonModel;
	}
	
	/**
	 * 修改个人信息发送验证码
	 * @param generalSetPram
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/sendValidateCode",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel sendValidateCode(@RequestBody GeneralSetPram generalSetPram) throws ControllerException{
		return generalSettingService.sendValidateCode(generalSetPram.getPhone(), generalSetPram.getJobNum());
	}
	
	@RequestMapping(value="/updateAgentPhone",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel updateAgentPhone(@RequestBody GeneralSetPram generalSetPram) throws ControllerException{
		return generalSettingService.updateAgentPhone(generalSetPram.getPhone(), generalSetPram.getValidateCode(), generalSetPram.getJobNum());
	}
	
	@RequestMapping(value="/getAllProvince",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel getAllProvince() throws ControllerException{
		return generalSettingService.getAllProvincefromSD();
	}
	
	@RequestMapping(value="/getAllCity",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel getAllCity(@RequestBody GeneralSetPram generalSetPram) throws ControllerException{
		return generalSettingService.getAllCity(generalSetPram.getParentCode());
	}
	
	@RequestMapping(value="/getAllCountry",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel getAllCountry(@RequestBody GeneralSetPram generalSetPram) throws ControllerException{
		return generalSettingService.getAllCountry(generalSetPram.getParentCode());
	}
	
	@RequestMapping(value="/getRegionByParentCode",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel getCityByProvince(@RequestBody GeneralSetPram generalSetPram) throws ControllerException{
		return generalSettingService.getRegionByParentCode(generalSetPram.getParentCode());
	}
	
	@RequestMapping(value="/getRegionByCode",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel getCityByCode(@RequestBody GeneralSetPram generalSetPram) throws ControllerException{
		return generalSettingService.getRegionByCode(generalSetPram.getCode());
	}
	@RequestMapping(value="/queryGeneralSetting",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel getMsgAcceptingStatus(@RequestBody GeneralSetPram generalSetPram) throws ControllerException{
		return generalSettingService.queryGeneralSetting(generalSetPram.getJobNum());
	}
	/**
	 * 查询代理人所在机构的工作时间
	 * @param agentJobnum
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/queryWorkTime",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel queryWorkTime(@RequestBody GeneralSetPram generalSetPram) throws ControllerException{
		return generalSettingService.queryWorkTime(generalSetPram.getJobNum());
	}
	
	/**
	 * 待支付配送地址-全国省份
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/getAllProvinceForPS",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel getAllProvinceForPS() throws ControllerException{
		return generalSettingService.getAllProvince();
	}
	 /**
	  * 待支付配送地址-全省地市
	  * @param generalSetPram
	  * @return
	  * @throws ControllerException
	  */
	@RequestMapping(value="/getAllCityForPS",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel getAllCityForPS(@RequestBody GeneralSetPram generalSetPram) throws ControllerException{
		return generalSettingService.getAllCityForPS(generalSetPram.getParentCode());
	}
	
	/**
	 * 待支付配送地址-全市区县
	 * @param generalSetPram
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/getAllCountryForPS",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel getAllCountryForPS(@RequestBody GeneralSetPram generalSetPram) throws ControllerException{
		return generalSettingService.getAllCityForPS(generalSetPram.getParentCode());
	}
	/**
	 * 修改地区信息
	 * @param generalSetPram
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/updateRegioncode",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel updateRegioncode(@RequestBody GeneralSetPram generalSetPram) throws ControllerException{
		return generalSettingService.updateRegioncode(generalSetPram.getJobNum(),generalSetPram.getProvinceCode(),
				generalSetPram.getCityCode(),generalSetPram.getCountyCode());
	}

	/**
	 * 全国可以注册省份
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/getRegisterProvince",method= RequestMethod.GET)
	@ResponseBody
	public CommonModel getRegisterProvince() throws ControllerException{
		return generalSettingService.getRegisterProvince();
	}
}
