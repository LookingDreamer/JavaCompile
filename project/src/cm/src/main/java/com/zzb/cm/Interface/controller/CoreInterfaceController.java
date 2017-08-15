package com.zzb.cm.Interface.controller;

import javax.annotation.Resource;

import com.cninsure.core.utils.StringUtil;
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
import com.zzb.cm.Interface.service.CoreInterFaceService;

@Controller
@RequestMapping("/policyInterface/*")
public class CoreInterfaceController extends BaseController { 
	@Resource
	private CoreInterFaceService coreInterFaceService;
	@Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
	/** 
	 * 核心查询接口
	 * @param orgCode
	 * @param companyId
	 * @param policyCode
	 * @param plateNum
	 * @param insuredName
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "insurance",produces="text/html;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String insurance(@RequestParam(value="orgCode") String orgCode,@RequestParam(value="companyId") String companyId,@RequestParam(value="policyCode",required=false) String policyCode,
			@RequestParam(value="plateNum",required=false) String plateNum,@RequestParam(value="insuredName",required=false) String insuredName) throws ControllerException{
		
		/**
		 * 根据入参查询数据，然后返回json，该接口的文档说明是【保单查询返回接口.docx】
		 * 也就是传过去的参数至少为三个：机构编码+保险公司编码+另外一个参数
		 * ①根据机构编码和保险公司编码去insborder表中查询，对应着出单机构和供应商，查出一条或者多条数据，得到对应的taskid
		 * bizTransactionId = taskid + "@" + companyId;
		 * 
		 * 
		 */
		
		return JSONObject.fromObject(coreInterFaceService.insurance(orgCode, companyId, policyCode, plateNum, insuredName)).toString();
	}
	
	/** 
	 * 核心反向关闭接口
	 * @param saveJson
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "save",produces="text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String save(@RequestBody String saveJson) throws ControllerException{
		
		/**
		 * 该接口的文档说明是【核心保存反向关闭接口.docx】
		 * 根据核心返回的json（就是saveJson接收到的json串），得到 车牌号，保险公司code，将json里面的信息保存到数据库里面，并推动工作流关闭流程
		 */
		//接口关闭使用
		LogUtil.info("反向关闭接口关闭使用");
		return null;
		//return JSONArray.fromObject(coreInterFaceService.save(saveJson)).toString();
	}

	/**
	 * 当承保完毕的时候推送到核心
	 * @param taskid
	 * @param companyid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "pushtocore",produces="text/html;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String pushtocore(@RequestParam(value="taskid") String taskid,@RequestParam(value="companyid") String companyid) throws ControllerException{
		if (StringUtil.isEmpty(taskid) || StringUtil.isEmpty(companyid)) {
			return "{\"resultCode\":\"0\",\"message\":\"参数不符合要求\"}";
		}

		/**
		 * 该接口的文档说明是【保网与核心对接.docx】
		 */
		taskthreadPool4workflow.execute(new Runnable() {
			@Override
			public void run() {
				try {
					coreInterFaceService.pushtocore(taskid,companyid);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		return "{\"resultCode\":\"1\",\"message\":\"\"}";
	}
	
	/**
	 * 当承保完毕的时候推送到核心后，核心回调接口
	 * @param json
	 * @return 200
	 * @throws ControllerException
	 */
	@RequestMapping(value = "callback",produces="text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String callback(@RequestBody String json) throws ControllerException{
		
		return coreInterFaceService.callback(json);
	}

	/**
	 * 手动反向关闭
	 */
	@RequestMapping(value = "closstask",produces="text/html;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String closstask(@RequestParam String taskid,@RequestParam String inscomcode) throws ControllerException{
		
		return coreInterFaceService.closstask(taskid,inscomcode);
	}
	
}
