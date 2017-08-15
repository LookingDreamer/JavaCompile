package com.zzb.conf.controller;

import java.util.*;

import javax.annotation.Resource;

import com.zzb.cm.service.INSBWorkflowDataService;
import com.zzb.conf.entity.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.utils.LogUtil;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.mobile.service.INSBPayChannelService;
import com.zzb.model.WorkFlow4TaskModel;

/**
 *
 * 工作流信息
 *
 *
 * 所有流程状态只有一条信息（记录当前流程节点信息）
 *
 * 
 * @author zhangwei
 *
 */
@Controller
@RequestMapping("/workflow/*") 
public class INSBWorkFlowDataContorller {
	@Resource
	private INSBWorkflowmainService workflowmainService;
	@Resource
	private INSBWorkflowsubService workflowsubService;
	@Resource
	private INSBPayChannelService insbPayChannelService;
	@Resource
	private INSBWorkflowDataService workflowDataService;


	/**
	 * 先向池中加入任务
	 * 
	 * @param dataModel
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "startTask", method = RequestMethod.GET)
	@ResponseBody
	public void startTaskFromWorlFlow(WorkFlow4TaskModel dataModel) {
		workflowDataService.startTaskFromWorlFlow(dataModel);
	}

	@RequestMapping(value = "endtask", method = RequestMethod.GET)
	@ResponseBody
	public void endTaskFromWorkFlow(WorkFlow4TaskModel dataModel) {
		workflowDataService.endTaskFromWorkFlow(dataModel);
	}

	@RequestMapping(value = "justTest", method = RequestMethod.GET)
	@ResponseBody
	public String justTest() {
		String mainInstanceId="1851568";
		INSBWorkflowmain workflowmain = workflowmainService.selectByInstanceId(mainInstanceId);
		System.out.println("#############################");

		System.out.println(workflowmain);
		return "justTest";
	}

	/**
	 * 通过主流程实例Id得到所有的子流程实例id
	 * 
	 * @param instanceId
	 * @return
	 */
	@RequestMapping(value = "getinstancebymaininstance", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getSubInstanceIdByMainInstanceId(String instanceId) {
		return workflowsubService.queryInstanceIdsByMainInstanceId(instanceId);
	}
	
	/**
	 * 通过主流程实例Id得到所有的子流程实例id
	 * 
	 * @param instanceId
	 * @return
	 */
	@RequestMapping(value = "getdatabyinstanceid4redis", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,String> getDataByMainInstanceId(String instanceId) {
		return workflowsubService.getDataByMainInstanceId(instanceId);
	}

	/**
	 * 结束主流程
	 * 
	 * @param instanceId
	 */
	@RequestMapping(value = "endmaininstance", method = RequestMethod.POST)
	@ResponseBody
	public void endMainInstance(String instanceId) {
		workflowmainService.deleteWorkFlowMainData(instanceId);
	}

	/**
	 * 结束子流程
	 * 
	 * @param instanceId
	 */
	@RequestMapping(value = "endsubinstance", method = RequestMethod.POST)
	@ResponseBody
	public void endSubInstance(String instanceId) {
		workflowsubService.deleteWorkFlowSubData(instanceId);
	}

	/**
	 * 根据实例id和供应商id返回第N次报价方式
	 * 
	 * @param processinstanceid
	 *            实例id
	 * @param providerid
	 *            供应商id
	 * @param quotecode          上次报价名称 （0：初始、1：EDI 、 2： 精灵 、 3：规则报价 、4：人工调整 、 5：人工规则录单 、6： 人工录单）
	 * @return 当前报价方式（如果是精灵或者edi返回当前精灵或者edi的id）
	 */
	@RequestMapping(value = "getquotetype", method = RequestMethod.GET)
	@ResponseBody
	public String getQuoteType(Long processinstanceid, String providerid, String quotecode) {
		return workflowDataService.getQuoteType(processinstanceid, providerid, quotecode);
	}
/**
	 * 判断平台查询
	 * 
	 */
	@RequestMapping(value = "getptway", method = RequestMethod.GET)
	@ResponseBody
	public String getPTway(Long processinstanceid, String providerid) {
		String taskid = String.valueOf(processinstanceid);
		return workflowmainService.getPTway(taskid, providerid);
	}
	
	/**
	 * 判断自动规则
	 */
	@RequestMapping(value = "getGZway", method = RequestMethod.GET)
	@ResponseBody
	public String getGZway(Long processinstanceid, String providerid) {
		String taskid = String.valueOf(processinstanceid);
		return workflowmainService.getGZway(taskid, providerid);
	}

	/**
	 * 根据实例id和供应商id返回第N次 核保方式
	 * 
	 * @param processinstanceid
	 *            实例id
	 * @param providerid
	 *            供应商id
	 * @param lastcode
	 *            上一次失败的核保方式,第一次请求0（1=EDI暂存,2=精灵暂存,3=人工核保,4=EDI自动核保,5=精灵自动核保,6=核保查询）
	 * @return 1=EDI暂存,2=精灵暂存,3=人工核保,4=EDI自动核保,5=精灵自动核保,6=核保查询
	 */
	@RequestMapping(value = "getunderwritingtype", method = RequestMethod.GET)
	@ResponseBody
	public String getUnderwritingType(Long processinstanceid, String providerid, String lastcode) {
		String taskid = String.valueOf(processinstanceid);
		String result = workflowmainService.getContracthbType(taskid, providerid, lastcode, "underwriting");
		LogUtil.info("taskid="+taskid+",providerid="+providerid+",获取核保方式,return=" + result);
		return result;
	}

	/**
	 * 根据实例id和供应商id返回第N次 承保保方式
	 * 
	 * @param processinstanceid
	 *            实例id
	 * @param providerid
	 *            供应商id
	 * @param lastcode
	 *            上一次失败的承保方式,第一次请求0（EDI:1 精灵：2 人工：3）
	 * @return
	 */
	@RequestMapping(value = "getcontracttype", method = RequestMethod.GET)
	@ResponseBody
	public String getContractType(Long processinstanceid, String providerid, String lastcode) {
		String taskid = String.valueOf(processinstanceid);
		return workflowmainService.getContractcbType(taskid, providerid, lastcode, "contract");
	}
	/**
	 * 判断规则平台查询
	 * 
	 */
	@RequestMapping(value = "getPTGZway", method = RequestMethod.GET)
	@ResponseBody
	public String getPTGZway(Long processinstanceid, String providerid) {
		String taskid = String.valueOf(processinstanceid);
		return workflowmainService.getGZPTway(taskid, providerid);
	}
	/**
	 * 判断EDI平台查询
	 * 
	 */
	@RequestMapping(value = "getPTEDIway", method = RequestMethod.GET)
	@ResponseBody
	public String getPTEDIway(Long processinstanceid, String providerid) {
		String taskid = String.valueOf(processinstanceid);
		return workflowmainService.getEDIPTway(taskid, providerid);
	}

	/**
	 * 查询code表信息并返回当前最新版本号接口
	 * @return
	 */
	@RequestMapping(value = "getNewVersion", method = RequestMethod.GET)
	@ResponseBody
	public String getNewVersion(){
		return insbPayChannelService.getNewVersion();
	}
	
}
