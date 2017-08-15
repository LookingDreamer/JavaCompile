package com.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.SpringContextHandle;
import com.cninsure.core.utils.StringUtil;
import com.zzb.cm.service.INSBWorkflowDataService;
import com.zzb.model.WorkFlow4TaskModel;

import net.sf.json.JSONObject;

/**
 * 调用工作流接口
 * 
 * @author hejie
 *
 */
//@Component
public class WorkFlowUtil {

	private static String WORKFLOW = "";

	static {
		// 读取相关的配置
		ResourceBundle resourceBundle = ResourceBundle.getBundle("config/config");
		WORKFLOW = resourceBundle.getString("workflow.url");
	}

	private static INSBWorkflowDataService workflowDataService;

	/**
	 * 启动流程
	 * 
	 * @param renewal
	 *            0 投保 1 快速续保
	 * @return
	 */
	public static String startWorkflowProcess(String renewal) {
		String path = WORKFLOW + "/process/startWorkflowProcess";
		Map<String, String> map = new HashMap<String, String>();
		map.put("renewal", renewal);

		String result = HttpClientUtil.doGet(path, map);
        LogUtil.info("startWorkflowProcess调用工作流返回：" + result);

		JSONObject jsonObject = JSONObject.fromObject(result);
		return null == jsonObject.getString("processinstanceid") ? ""
				: jsonObject.getString("processinstanceid");
	}

	/**
	 * 获得用户待办信息
	 * 
	 * @param userName
	 * @return
	 */
	public static String getUserTaskInfo(String userName) {
		String path = WORKFLOW + "/process/getTasksByUserid";
		Map<String, String> userMap = new HashMap<String, String>();
		userMap.put("userid", userName);
		Map<String, String> params = new HashMap<String, String>();
		params.put("datas", JSONObject.fromObject(userMap).toString());
		String result = HttpClientUtil.doGet(path, params);
		if (result != null) {
			try {
				result = java.net.URLDecoder.decode(result, "UTF-8");
				if (result != null) {
					result = java.net.URLDecoder.decode(result, "UTF-8");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 规则报价
	 * 
	 * @param ruleResult
	 * @param subtaskid
	 * @param userid
	 * @return
	 */
	public static String completeSubTask(String ruleResult, String subtaskid,
			String userid,Map<String, Object> rMap) {
		String path = WORKFLOW + "/process/completeSubTask";
		int rresult = 1;// 失败
		if (StringUtils.isNotBlank(ruleResult)) {
			JSONObject jobj = JSONObject.fromObject(ruleResult);
			if (jobj != null) {
				boolean success = jobj.getBoolean("success");
				if (success) {
					rresult = 3;// 成功
				}
			}
		}
		rMap.put("result", rresult + "");

		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("processinstanceid", subtaskid);
		userMap.put("userid", userid);
		userMap.put("taskName", "规则报价");
		userMap.put("data", rMap);

		Map<String, String> params = new HashMap<String, String>();
		params.put("datas", JSONObject.fromObject(userMap).toString());

		String result = HttpClientUtil.doGet(path, params);
        LogUtil.info(subtaskid+"在completeSubTask调用工作流返回：" + result);
        //completeSubTask(processInstanceId,taskName);
		return result;
	}

	/**
	 * 投保开始 通知工作流是投保，还是快速续保
	 *
	 * @param taskId
	 *            流程id
	 * @param createby
	 *            操作人
	 * @param processInstanceId
	 *            主流程id
	 */
	public static void noticeWorkflowRenewalOrInsure(String taskId,
			String createby, String processInstanceId) {
		String path = WORKFLOW + "/process/completeMessageInput";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", createby);
		map.put("claim", 1);
		map.put("taskid", Long.parseLong(taskId));
		map.put("processinstanceid", Long.parseLong(processInstanceId));
		JSONObject jsonObject = JSONObject.fromObject(map);
		Map<String, String> params = new HashMap<String, String>();
		params.put("datas", jsonObject.toString());
		HttpClientUtil.doGet(path, params);
	}

	/**
	 * 投保信息录入完成，启动报价，按照供应商
	 * 
	 * @param createby
	 * 
	 * @param processInstanceId
	 *            主流程id
	 * @param incoids
	 *            报价公司列表 json字符串
	 * @param renewal
	 *            0 投保 1快速续保
	 */
	public static String noticeWorkflowStartQuote(String createby,
			String processInstanceId, Map<String,Object> incoids,String renewal) {
		String path = WORKFLOW + "/process/completeMessageInput";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", createby);
		map.put("processinstanceid", Long.parseLong(processInstanceId));
		map.put("incoids", incoids);
		map.put("renewal", renewal);
		JSONObject jsonObject = JSONObject.fromObject(map);
		Map<String, String> params = new HashMap<String, String>();
		params.put("datas", jsonObject.toString());

		String result = HttpClientUtil.doGet(path, params);
        LogUtil.info(processInstanceId+"在noticeWorkflowStartQuote调用工作流返回：" + result);

        return result;
	}

	/**
	 * 拒绝任务
	 * 
	 * @param userid
	 *            拒绝人
	 * @param processinstanceid
	 *            流程实例id
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	public static String releaseTaskWorkflow(String mainprocessinstanceid,
			String incoid, String userid, String processinstanceid)
			throws ParseException, UnsupportedEncodingException, IOException {
		String path = WORKFLOW + "/process/releaseTask";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mainprocessinstanceid", mainprocessinstanceid);
		map.put("incoid", incoid);
		map.put("userid", userid);
		map.put("processinstanceid", Long.parseLong(processinstanceid));

		
		JSONObject jsonObject = JSONObject.fromObject(map);
		Map<String, String> params = new HashMap<String, String>();
		params.put("datas", jsonObject.toString());
//		return HttpClientUtil.doTaskGet(path, params);
		return "{\"message\":\"success\"}";
	}

	public static String completeTaskWorkflow(String ruleResult,
			String processinstanceid, String userid, String taskName,Map<String, String> map1) {
		String path = WORKFLOW + "/process/completeSubTask";
		if(null == map1){
			map1 = new HashMap<String, String>();
		}
		map1.put("result", ruleResult);
		if("平台查询".equals(taskName)){//未返回平台信息，先通过平台查询节点
			map1.put("ptisful", "0");//表示直接推送平台查询结束，没有返回平台查询信息
		}
		if("精灵核保暂存".equals(taskName)||"精灵自动核保".equals(taskName)){
			map1.put("underwriteway", "3");
		}
		if("EDI核保暂存".equals(taskName)||"EDI自动核保".equals(taskName)){
			map1.put("result", "1");
			if(!"1".equals(ruleResult)){
				map1.put("underwriteway", ruleResult);
			}else{
				map1.put("underwriteway", "3");
			}
		}
		if(taskName.contains("承保")){
			path = WORKFLOW + "/process/completeTask";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", map1);
		map.put("userid", userid);
		map.put("taskName", taskName);
		map.put("processinstanceid", Long.parseLong(processinstanceid));

		JSONObject jsonObject = JSONObject.fromObject(map);
		Map<String, String> params = new HashMap<String, String>();
		params.put("datas", jsonObject.toString());

		String result = HttpClientUtil.doGet(path, params);
        LogUtil.info(processinstanceid+"在completeTaskWorkflow调用工作流返回：" + result);
        //completeSubTask(processInstanceId,taskName);
        return result;
	}

	/**
	 * 改派任务
	 * 
	 * @param mainprocessinstanceid
	 *            主实例id
	 * @param incoid
	 *            供应商id
	 * @param userid
	 *            以前操作人
	 * @param processinstanceid
	 *            实例id
	 * @param targetuserid
	 *            目标业管
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	public static String delegateTaskWorkflow(String mainprocessinstanceid,
			String incoid, String userid, String processinstanceid,
			String targetuserid) throws ParseException,
			UnsupportedEncodingException, IOException {
		String path = WORKFLOW + "/process/delegateTask";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mainprocessinstanceid", mainprocessinstanceid);
		map.put("incoid", incoid);
		map.put("userid", userid);
		map.put("processinstanceid", processinstanceid);
		map.put("targetuserid", targetuserid);

		JSONObject jsonObject = JSONObject.fromObject(map);
		Map<String, String> params = new HashMap<String, String>();
		params.put("datas", jsonObject.toString());
//		return HttpClientUtil.doTaskGet(path, params);
		return "{\"message\":\"success\"}";
		
	}

	/**
	 * 认领任务
	 * 
	 * @param mainprocessinstanceid
	 * @param incoid
	 * @param userid
	 * @param processinstanceid
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	public static String claimTaskWorkflow(String mainprocessinstanceid,
			String incoid, String userid, String processinstanceid)
			throws ParseException, UnsupportedEncodingException, IOException {
		String path = WORKFLOW + "/process/claimTask";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mainprocessinstanceid", mainprocessinstanceid);
		map.put("incoid", incoid);
		map.put("userid", userid);
		map.put("processinstanceid", processinstanceid);

		JSONObject jsonObject = JSONObject.fromObject(map);
		Map<String, String> params = new HashMap<String, String>();
		params.put("datas", jsonObject.toString());
//		return HttpClientUtil.doTaskGet(path, params);
		return "{\"message\":\"success\"}";
	}

	/**
	 * 重新获取报价途径
	 * 
	 * @param processinstanceid
	 *            实例id
	 * @param userid
	 *            用户
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	public static String workFlowCompleteSubTask(String processinstanceid,
			String userid) throws ParseException, UnsupportedEncodingException,
			IOException {
		String path = WORKFLOW + "/process/completeSubTask";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("processinstanceid", Long.parseLong(processinstanceid));
		jsonObject.put("userid", userid);
		jsonObject.put("taskName", "选择投保");// 节点名称
		JSONObject json = new JSONObject();
		json.put("result", "1");
		jsonObject.put("data", json);
		Map<String, String> params = new HashMap<String, String>();
		params.put("datas", jsonObject.toString());
		//completeSubTask(processInstanceId,taskName);
		return HttpClientUtil.doTaskGet(path, params);
	}

	/**
	 * 增加报价公司
	 * 
	 * @param processInstanceId
	 * @param createby
	 * @param incoids
	 * @return
	 */
	public static String workFlowOnlyStartSubProcess(String processInstanceId,
			String createby, List<String> incoids) {
		String path = WORKFLOW + "/process/onlyStartSubProcess";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", createby);
		map.put("processinstanceid", Long.parseLong(processInstanceId));
		map.put("incoids", incoids);
		JSONObject jsonObject = JSONObject.fromObject(map);
		Map<String, String> params = new HashMap<String, String>();
		params.put("datas", jsonObject.toString());
		return HttpClientUtil.doGet(path, params);
	}

	/**
	 * 待承保打单
	 * 
	 * @param processInstanceId
	 * @param userid
	 * @param taskName
	 * @param data
	 * @return
	 */
	public static String undwrtSuccess(String processInstanceId, String userid,
			String taskName, Map<String, Object> data) {
		String path = WORKFLOW + "/process/completeTask";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userid);
		map.put("processinstanceid", Long.parseLong(processInstanceId));
		if (taskName != null) {
			map.put("taskName", taskName);
		}
		if (data != null) {
			map.put("data", data);
		}
		JSONObject jsonObject = JSONObject.fromObject(map);
		Map<String, String> params = new HashMap<String, String>();
		params.put("datas", jsonObject.toString());

		String result = HttpClientUtil.doGet(path, params);
        LogUtil.info(processInstanceId+params+"在undwrtSuccess调用工作流返回：" + result);
        //completeMainTask(processInstanceId,taskName);
        return result;
	}

	/**
	 * 工作流调cm出调度池逻辑放到cm系统里,完成主任务
	 * @param processInstanceId
	 * @param taskName
	 * @param taskStatus
	 */
	public static void completeMainTaskDealCmData(String processInstanceId,String taskName,String taskStatus){
		//工作流调cm出调度池逻辑放到cm系统里
		LogUtil.info(processInstanceId + "在completeMainTaskDealCmData节点名称参数是：" + taskName);
		if(null == workflowDataService){
			//WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			workflowDataService = (INSBWorkflowDataService)SpringContextHandle.getBean(INSBWorkflowDataService.class);
		}
		WorkFlow4TaskModel dataModel = new WorkFlow4TaskModel();
		dataModel.setMainInstanceId(processInstanceId);
		dataModel.setTaskName(taskName);
		dataModel.setTaskStatus(taskStatus);
		dataModel.setDataFlag(1);
		dataModel.setTaskCode(WorkflowAttachHandleUtil.getBundleResource(taskName));
		workflowDataService.endTaskFromWorkFlow(dataModel);
	}

	/**
	 * 工作流调cm出调度池逻辑放到cm系统里,完成子任务
	 *
	 * @param processInstanceId
	 * @param taskName
	 * @param taskStatus
	 */
	public static void completeSubTaskDealCmData(String processInstanceId,String taskName,String taskStatus){
		//工作流调cm出调度池逻辑放到cm系统里
		LogUtil.info(processInstanceId + "在completeSubTaskDealCmData节点名称参数是：" + taskName);
		if(!"子流程前置".equals(taskName) && StringUtil.isNotEmpty(taskName) && StringUtil.isNotEmpty(processInstanceId)){//子流程前置节点不需要处理cm记录流程数据
			if(null == workflowDataService){
				//WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
				workflowDataService = (INSBWorkflowDataService)SpringContextHandle.getBean(INSBWorkflowDataService.class);
			}
			WorkFlow4TaskModel dataModel = new WorkFlow4TaskModel();
			dataModel.setMainInstanceId(null);
			dataModel.setSubInstanceId(processInstanceId);
			dataModel.setTaskName(taskName);
			dataModel.setTaskStatus(taskStatus);
			dataModel.setDataFlag(1);
			dataModel.setTaskCode(WorkflowAttachHandleUtil.getBundleResource(taskName));
			workflowDataService.endTaskFromWorkFlow(dataModel);
		}
	}

	/**
	 * 取消任务，关闭cm业务流程数据
	 * @param processInstanceId
	 * @param mainprocessInstanceId
	 * @param process
	 * @param from
	 */
	public static void abortTaskDealCmData(String processInstanceId, String mainprocessInstanceId, String process, String from){
		if("main".equals(process)){
			completeSubTaskDealCmData(processInstanceId,"关闭","Closed");
			List<String> subs = workflowDataService.getSubInstanceIdByInsbWorkflowMainInstanceId(processInstanceId);
			if(null != subs){
				for(String sub:subs){
					completeSubTaskDealCmData(sub,"关闭","Closed");
				}
			}
		}else if ("sub".equals(process)) {
			completeSubTaskDealCmData(processInstanceId,"关闭","Closed");
		}
	}

	/**
	 * 报价退回，核保退回 修改投保信息
	 * @param result 
	 * @param processinstanceid 流程实例ID
	 * @param userid 用户id
	 * @param taskName 节点名称
	 * @return
	 */
	public static String updateInsuredInfoNoticeWorkflow(Map<String, Object> map,String processinstanceid, String userid, String taskName,String result) {
		String path = WORKFLOW + "/process/completeSubTask";
		Map<String, Object> data = new HashMap<String, Object>();
		if (null == map){
			map = new HashMap<String, Object>();
		}else{
			Set<Map.Entry<String, Object>> entrySet = map.entrySet();
			//将关系集合entrySet进行迭代，存放到迭代器中                
			Iterator<Map.Entry<String, Object>> it2 = entrySet.iterator();
			while(it2.hasNext()){
			        Map.Entry<String, Object> me = it2.next();//获取Map.Entry关系对象me
			        //String key2 = me.getKey();//通过关系对象获取key
			        //String value2 = me.getValue();//通过关系对象获取value
			        //System.out.println("key: "+key2+"-->value: "+value2);
			        data.put(me.getKey(), me.getValue());
			}
		}
		map.put("userid", userid);
		map.put("taskName", taskName);
		if(!StringUtil.isEmpty(result)){
			data.put("result", result);
			if("平台查询".equals(taskName)){//通过
				data.put("ptisful", "1");//表示直接推送平台查询结束，平台查询信息成功
			}
		}
		map.put("data", data);
		map.put("processinstanceid", Long.parseLong(processinstanceid));
		JSONObject jsonObject = JSONObject.fromObject(map);
		Map<String, String> params = new HashMap<String, String>();
		params.put("datas", jsonObject.toString());

		String wResult = HttpClientUtil.doGet(path, params);
		//completeSubTask(processInstanceId,taskName);
        LogUtil.info(processinstanceid+"在updateInsuredInfoNoticeWorkflow调用工作流参数："+jsonObject.toString()+"返回；" + wResult);

        return wResult;
	}
	
	/**
	 * 推子流程前置结束组装参数（重载方法）
	 * @param processinstanceid 流程实例ID
	 * @param userid 用户id
	 * @param taskName 节点名称
	 * @param param 推流程参数
	 * @return
	 */
	public static String updateInsuredInfoNoticeWorkflow(String processinstanceid, String userid, String taskName,Map<String, Object> param) {
		String path = WORKFLOW + "/process/completeSubTask";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userid);
		map.put("taskName", taskName);
		map.put("data", param);
		map.put("processinstanceid", Long.parseLong(processinstanceid));
		JSONObject jsonObject = JSONObject.fromObject(map);
		Map<String, String> params = new HashMap<String, String>();
		params.put("datas", jsonObject.toString());

		String wResult = HttpClientUtil.doGet(path, params);
        LogUtil.info(processinstanceid+"在updateInsuredInfoNoticeWorkflow调用工作流参数："+jsonObject.toString()+"返回；" + wResult);
        //completeSubTask(processInstanceId,taskName);
        return wResult;
	}
	
	
	public static String abortProcessByIdWorkflow(String instanceId, String mainorsub, String from) {
		String path = WORKFLOW + "/process/abortProcessById";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("processinstanceid", instanceId);//流程实例id
		map.put("process", mainorsub);//主或子流程
		map.put("from", from);//后台拒绝承保
		
		Map<String, String> params = new HashMap<String, String>();
		JSONObject datasJSON = JSONObject.fromObject(map);
		params.put("datas", datasJSON.toString());
		
		String wResult = HttpClientUtil.doGet(path, params);
        LogUtil.info(instanceId+"在abortProcessByIdWorkflow调用工作流返回：" + wResult);

        return wResult;
	}
	
	public static String abortProcessByMainIdSubid(String instanceId, String mainorsub, String from,String mainid) {
		String path = WORKFLOW + "/process/abortProcessById";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("processinstanceid", instanceId);//流程实例id
		map.put("process", mainorsub);//主或子流程
		map.put("from", from);//后台拒绝承保
		map.put("mainprocessinstanceid", mainid);
		Map<String, String> params = new HashMap<String, String>();
		JSONObject datasJSON = JSONObject.fromObject(map);
		params.put("datas", datasJSON.toString());
		
		String wResult = HttpClientUtil.doGet(path, params);
        LogUtil.info("abortProcessByMainIdSubid重复投保关闭流程="+mainid+"=调用工作流返回：" + wResult);

        return wResult;
	}
	
	public static void main(String[] args) throws Exception, IOException {
		// noticeWorkflowRenewalOrInsure("","","");
		// {"processinstanceid":138,"renewal":0,"userId":"zhangjc","incoids":
		// ["pingan","taipingyang"],"taskId":163}
//		List<String> list = new ArrayList<String>();
//		list.add("2022");
//		list.add("2085");
//		String res = noticeWorkflowStartQuote("zhangjc", "1287", null,"0");
//		System.out.println(res);
//		subCompletedContinueMainTask("1485469", "3", "20774401");
		boolean flag = true;
		System.out.println(String.valueOf(flag));
		
		/*List<String> subs = new ArrayList<String>();
		for(String sub:subs){
			System.out.println(sub);
		}*/
	}

	/**
	 * http://localhost:8080/workflow/process/completeSubTask
	 * ?datas={"processinstanceid":162,"userid":"admin",taskName:"等待报价请求",data:{null}}
	 * @param processinstanceid 子流程
	 * @return
	 */
	public static String resquestQuotationToWorkflow(String processinstanceid,String taskname,Map<String, Object> data) {
		String path = WORKFLOW + "/process/completeSubTask";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskName", taskname);
		map.put("userid", "admin");
		if(null!=data){
			map.put("data", data);
		}
		map.put("processinstanceid", Long.parseLong(processinstanceid));
		JSONObject jsonObject = JSONObject.fromObject(map);
		Map<String, String> params = new HashMap<String, String>();
		params.put("datas", jsonObject.toString());

		String wResult = HttpClientUtil.doGet(path, params);
        LogUtil.info(processinstanceid+"在resquestQuotationToWorkflow调用工作流返回：" + wResult);
        //completeSubTask(processInstanceId,taskName);
        return wResult;
	}
	/**
	 * 子流程报价完成，推承保节点
	 * @param processinstanceid
	 * @param acceptway
	 * @param incoid
	 * @return
	 */
	public static String subCompletedContinueMainTask(String processinstanceid,String taskname,String acceptway,String incoid) {
		String path = WORKFLOW + "/process/completeTask";
        Map<String, String> dataMap= new HashMap<String,String>();
        dataMap.put("result", "1");
        dataMap.put("acceptway", acceptway);
        dataMap.put("incoid", incoid);
        Map<String, Object> paramMap= new HashMap<String,Object>();
        paramMap.put("data", dataMap);
        paramMap.put("userid", "admin");
        paramMap.put("taskName", taskname);
        paramMap.put("processinstanceid", Long.parseLong(processinstanceid));

        JSONObject jsonObject = JSONObject.fromObject(paramMap);
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("datas", jsonObject.toString());
        String result = HttpClientUtil.doGet(path, requestParams);
        LogUtil.info(processinstanceid+requestParams+"调用工作流接口返回信息：" + result);
        //completeMainTask(processInstanceId,taskName);
        return result;
    }

	public static String completeTaskWorkflowRecheck(String ruleResult, String workflowinstanceid, String operator,
			String taskName, Map<String, Object> map1) {
		String path = WORKFLOW + "/process/completeSubTask";
		if(null == map1){
			map1 = new HashMap<String, Object>();
		}
		map1.put("result", ruleResult);
		if("平台查询".equals(taskName)){//未返回平台信息，先通过平台查询节点
			map1.put("ptisful", "0");//表示直接推送平台查询结束，没有返回平台查询信息
		}
		if("精灵核保暂存".equals(taskName)||"精灵自动核保".equals(taskName)){
			map1.put("underwriteway", "3");
		}
		if("EDI核保暂存".equals(taskName)||"EDI自动核保".equals(taskName)){
			map1.put("result", "1");
			if(!"1".equals(ruleResult)){
				map1.put("underwriteway", ruleResult);
			}else{
				map1.put("underwriteway", "3");
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", map1);
		map.put("userid", operator);
		map.put("taskName", taskName);
		map.put("processinstanceid", Long.parseLong(workflowinstanceid));

		JSONObject jsonObject = JSONObject.fromObject(map);
		Map<String, String> params = new HashMap<String, String>();
		params.put("datas", jsonObject.toString());

		String result = HttpClientUtil.doGet(path, params);
        LogUtil.info(workflowinstanceid+"在completeTaskWorkflow调用工作流返回：" + result);
        //completeSubTask(processInstanceId,taskName);
        return result;
		
	}
}
