package com.zzb.cm.Interface.controller;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.JsonUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCDeptService;
import com.cninsure.system.service.INSCUserService;
import com.cninsure.system.service.OnlineService;
import com.common.HttpClientUtil;
import com.common.PagingParams;
import com.common.XMPPUtils;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import com.zzb.cm.Interface.model.ForwardTaskModel;
import com.zzb.cm.Interface.model.GoToEdiQuoteModel;
import com.zzb.cm.Interface.model.TaskResultWriteBackModel;
import com.zzb.cm.Interface.model.goToFairyQuoteModel;
import com.zzb.cm.Interface.model.lockTaskModel;
import com.zzb.cm.Interface.model.recoverytaskModel;
import com.zzb.cm.Interface.model.updateTaskStatusModel;
import com.zzb.cm.Interface.service.ExternalInterFaceService;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.cm.service.INSBFlowerrorService;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.mobile.model.IDCardModel;

@Controller
@RequestMapping("/interface/*")
public class InterFaceController extends BaseController {
    private static final String ONLINE_USERS = "online_users";
    private static final String ONLINE = "cm:zzb:online_user";
    @Resource InterFaceService interFaceService;
	@Resource INSBFlowerrorService insbFlowerrorService;
	@Resource INSBProviderService insbProviderService;
	@Resource INSCUserService inscUserService;
	@Resource OnlineService onlineService ;
	//@Resource EDITaskPolling eDITaskPolling ;
	@Resource
	private INSCDeptService deptService;
	@Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;

	@Resource
	private IRedisClient redisClient;
	@Resource
	private ExternalInterFaceService enternalInterFaceService;

	/**
	 * 供工作流调用该接口，通知精灵进行任务处理。
	 * @param processinstanceid 工作流id
	 * @param incoid 报价的保险公司id
	 * @return json 用于反馈接收的状态
	 * @throws Exception 
	 */
	@RequestMapping(value = "goToFairyQuote",produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	@Deprecated
	public String goToFairyQuote(@RequestBody goToFairyQuoteModel model) throws Exception{
		String processinstanceid=model.getProcessinstanceid();
		String incoid=model.getIncoid();
		String touserid=model.getTouserid();
		String taskType = model.getTaskType();
		LogUtil.info("进入==goToFairyQuote==Controler==数据获取接口：taskid=" + processinstanceid + ",inscomcode=" + incoid + ",touserid=" + touserid + ",taskType=" + taskType + "====");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("processinstanceid", processinstanceid);
		resultMap.put("incoid", incoid);
		resultMap.put("touserid", touserid);
		resultMap.put("result", true);
		interFaceService.goToFairyQuote(processinstanceid, incoid, touserid, taskType);
		return JsonUtil.getJsonString(resultMap);
		
	}
	
	/**
	 * 供工作流调用该接口，通过EDI进行报价。
	 * @param processinstanceid 工作流id
	 * @param incoid 报价的保险公司id
	 * @return json 用于反馈接收的状态
	 * @throws Exception 
	 */
	@RequestMapping(value = "ediquote",produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	@Deprecated
	public String goToEdiQuote(@RequestBody GoToEdiQuoteModel model) throws Exception{
		LogUtil.info("进入==goToFairyQuote==Controler==数据获取接口：taskid=" + model.getProcessinstanceid() + ",inscomcode=" + model.getIncoid() + ",touserid=" + model.getEdiid() + ",taskType=" + model.getTaskType() + "====");
		return interFaceService.goToEdiQuote(model.getProcessinstanceid(), model.getIncoid(), model.getEdiid(), model.getTaskType());
	}
	
	/**
	 * 获取大对象
  	 * @param json
	 * @return 大对象
	 * @throws ControllerException
	 */
	@RequestMapping(value = "getpacket",produces="application/json;charset=UTF-8",method = RequestMethod.POST)
	@ResponseBody
	public String packet(@RequestBody JSONObject json,HttpServletRequest request) throws ControllerException{
		String taskId = json.getString("taskId");
		String processType = json.getString("processType");
		String monitorid = json.getString("monitorid");
		String taskType = json.getString("taskType");
//		try { 
//			String ip = IpUtil.getIpAddr(request); 
//			Boolean isExip = IpUtil.isIpExist(request);
//			if(!isExip){
//				LogUtil.info("进入==Controler==数据获取接口：taskid=" + taskId.split("@")[0] + ",inscomcode=" + taskId.split("@")[1] + ";" + ip +";ip地址受限不能访问！");
//				return "ip地址受限不能访问！";
//			}
//			LogUtil.info("进入==Controler==数据获取接口：taskid=" + taskId.split("@")[0] + ",inscomcode=" + taskId.split("@")[1] + ";" + ip);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		LogUtil.info("进入==Controler==数据获取接口：taskid=" + taskId.split("@")[0] + ",inscomcode=" + taskId.split("@")[1] + ",json:" + json + "====");
		return JSONObject.toJSONStringWithDateFormat(interFaceService.getPacket(taskId.split("@")[0],taskId.split("@")[1],processType,monitorid,taskType), "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
	}
	
	/**
	 * 保存大对象
	 * @param json 大对象
	 * @return 
	 * @throws ControllerException
	 */
	@RequestMapping(value = "savePacket",produces="application/json;charset=UTF-8",method = RequestMethod.POST)
	@ResponseBody
	public String savePacket(@RequestBody String json) throws ControllerException{
		LogUtil.info("收到数据回写：" + json);
		try{
			String result = JSONObject.toJSONString(interFaceService.savePacket(json));
			LogUtil.info("收到数据回写结果：" + result);
			return result;
		}catch (Exception e){
			LogUtil.error("数据回写失败：" + e.getMessage());	
			e.printStackTrace();
		}
		return "回写时发生异常";
	}
	
	/**
	 * 接口处理完成调用工作流接口	
	 * @param model
	 * @return   
	 * String  
	 * @throws
	 * @date 2015年11月9日 下午2:33:04
	 */
	@RequestMapping(value = "TaskResultWriteBack", method = RequestMethod.POST)
	@ResponseBody
	public void TaskResultWriteBack(@RequestBody TaskResultWriteBackModel model)  throws ControllerException{
		interFaceService.TaskResultWriteBack(model.getTaskid(), model.getInscomcode(),model.getUserid(), model.getResult(),model.getQuotename(), model.getTasktyep(),null,null,null);
	}
	
	/**
	 * 任务状态更新
	 * @param model
	 * @return
	 * @throws ControllerException   
	 * Map<String,Object>  
	 * @throws
	 * @author 
	 * @date 2015年11月13日 下午4:28:33
	 */
	@RequestMapping(value = "updateTaskStatus",produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	@Deprecated
	public Map<String,Object> updateTaskStatus(@RequestBody updateTaskStatusModel model) throws ControllerException{
		return interFaceService.updateTaskStatus(model.getTaskId(),model.getTaskStatus(),model.getProcessType());
	}

	/**
	 * 精灵任务回收接口
	 * @param taskId
	 * @param taskType
	 * @return   
	 * String  
	 * @throws
	 * @author 
	 * @date 2015年11月10日 下午8:03:43
	 */
	@RequestMapping(value = "recoverytask",produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	@Deprecated
	public String recoverytask(@RequestBody recoverytaskModel model) throws ControllerException{
		
		return interFaceService.recoverytask(model.getTaskId(), model.getTaskType());
	}
	
	
	@RequestMapping(value = "locktask", method = RequestMethod.POST)
	@ResponseBody
	@Deprecated
	public String lockTask(@RequestBody lockTaskModel model) throws ControllerException{
		
		return interFaceService.lockTask(model.getTaskId(), model.getIsLocked(), model.getInsurancecompanyid());
	 
	}
	
	@RequestMapping(value = "forwardtask", method = RequestMethod.POST)
	@ResponseBody
	@Deprecated
	public String forwardTask(@RequestBody ForwardTaskModel model) throws ControllerException{
		
		return interFaceService.forwardTask(model.getTaskid(), model.getInsurancecompanyid(), model.getFromuser(), model.getTouser());
	}
	
	/**
	 * 消息-edi
	 * @param taskid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "message4edi", method = RequestMethod.GET)
	@ResponseBody
	@Deprecated
	public String message4EDI(@RequestParam(value="taskid") String taskid,@RequestParam(value="insurancecompanyid") String insurancecompanyid) throws ControllerException{
//		JSONObject jsonObject = JSONObject.fromObject(interFaceService.getMessage(taskid));
//		return jsonObject.toString();
		return "";
	}
	
	/**
	 * 消息-精灵
	 * @param taskid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "message4fairy", method = RequestMethod.GET)
	@ResponseBody
	@Deprecated
	public void message4Fairy(@RequestParam(value="taskid") String taskid) throws ControllerException{
//		try {
//			AbstractXMPPConnection connection = XMPPUtils.getConnection();
//			AbstractXMPPConnection connection = XMPPUtils.getConnection("2@localhost", "2", "localhost", 5222, "localhost", "Spark");
//			String userJID = "fengjl@localhost";
//			XMPPUtils.sendMsg(connection, userJID, JSONObject.fromObject(interFaceService.getMessage(taskid)).toString());
//			connection.disconnect();
//		} catch (SmackException | IOException | XMPPException e) {
//			e.printStackTrace();
//		}
		try {
			XMPPUtils.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "getonlineusers",produces="text/html;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String getonlineusers() throws Exception{
//		ConcurrentLinkedQueue<String> queue = XMPPUtils.getInstance().getMessage();
//		Map<String, Object> onLineUsers = new HashMap<String, Object>();
//		if(redisClient.get("onlineUsers")==null||"".equals(redisClient.get("onlineUsers"))){
//		}else{
//			onLineUsers = JsonUtil.parseJSONToMap(redisClient.get("onlineUsers").toString());
//		}
		return ("redis当前缓存在线用户：<br>" + redisClient.get(Constants.CM_ZZB, ONLINE_USERS).toString());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "onlineusers", method = RequestMethod.GET)
	@ResponseBody
	public void onlineusers(@RequestParam(value="jabber") String jabber,@RequestParam(value="status") String status,@RequestParam(value="usercode") String usercode,@RequestParam(value="secret") String secret) throws Exception{
		String newsecret = getResult(jabber+"@"+status);
//		http://119.29.111.199:9090/plugins/presence/status?jid=zhangdi@car.zzb&type=text
		if(newsecret.equals(secret)){
			if("onLine".equals(status)){
				redisClient.set(ONLINE, usercode,	 jabber);
				LogUtil.info("--登入---usercode:" + usercode + "jabberid:" + jabber + ",时间：" + LocalDateTime.now());
				onlineService.changeOnlinestatus(jabber.split("@")[0], 1);
			}else{
				String pluginUrl = "http://" + ValidateUtil.getConfigValue("fairy.host") + ":9090/plugins/presence/status?jid=" + usercode + "@" + ValidateUtil.getConfigValue("fairy.serviceName") + "&type=text";
//				String pluginUrl = "http://" + "119.29.111.199" + ":9090/plugins/presence/status?jid=" + usercode + "@" + "car.zzb" + "&type=text";
				String result = HttpClientUtil.doGet(pluginUrl, null);
				if(StringUtil.isNotEmpty(result)&&result.contains("Unavailable")){
					redisClient.del(ONLINE, usercode);
					LogUtil.info("--用户退出---usercode:" + usercode + "jabberid:" + jabber + ",时间：" + LocalDateTime.now());
					onlineService.changeOnlinestatus(jabber.split("@")[0], 0);
				}
				
			}
		}
		
		
	}

	/**
	 *
	 * @param userCode
	 * @param status
	 * @throws add by hewc for websocket
	 */
	@RequestMapping(value = "newonlineusers/{usercode}/{status}", method = RequestMethod.GET)
	@ResponseBody
	public void newonlineusers(@PathVariable(value = "usercode") String userCode,@PathVariable(value = "status") String status) throws Exception{
		if("onLine".equals(status)){
			redisClient.set(ONLINE, userCode,	 userCode+"@websocket");
			LogUtil.info("--登入---usercode:" + userCode  + ",时间：" + LocalDateTime.now());
			onlineService.changeOnlinestatus(userCode, 1);
		}else{
			redisClient.del(ONLINE, userCode);
			LogUtil.info("--用户退出---usercode:" + userCode + ",时间：" + LocalDateTime.now());
			onlineService.changeOnlinestatus(userCode, 0);
		}
	}
	
	
	@RequestMapping(value = "offline", method = RequestMethod.GET)
	@ResponseBody
	public void offline() throws Exception{
		XMPPUtils.getInstance("fengjl@localhost","111");
	}
	
	public static  String  getResult(String inputStr)
    {
        BigInteger bigInteger=null;

        try {
         MessageDigest md = MessageDigest.getInstance("MD5");   
         byte[] inputData = inputStr.getBytes(); 
         md.update(inputData);
         bigInteger = new BigInteger(md.digest()); 
        } catch (Exception e) {e.printStackTrace();}
        return bigInteger.toString(16);
    }

	/**
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "pushtocif",produces="text/html;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String pushtocif(@RequestParam(value="taskid") String taskid,@RequestParam(value="companyid") String companyid) throws Exception{
		//return net.sf.json.JSONObject.fromObject(interFaceService.pushtocif(taskid,companyid)).toString();
		taskthreadPool4workflow.execute(new Runnable() {
			@Override
			public void run() {
				try {
					 interFaceService.pushtocif(taskid,companyid);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return "success";
	}
	
	/**
	 * 跳转到列表页面
	 * 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "onlinuserinfo", method = RequestMethod.GET)
	public ModelAndView list() throws ControllerException {
		ModelAndView mav = new ModelAndView("system/onlinuserinfo");
		return mav;
	}
	/**
	 * 在线用户信息list
	 */
	@RequestMapping(value = "showinfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> showinfo(@RequestParam(value="usercode",required=false) String usercode,
			@RequestParam(value="username",required=false) String username,
			@RequestParam(value="onlinestatus",required=false) String onlinestatus,
			@RequestParam(value="userorganization",required=false) String userorganization,
			@RequestParam(value="groupname",required=false) String groupname,
			@ModelAttribute PagingParams para) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		INSCUser query = new INSCUser();
		if(!StringUtil.isEmpty(usercode)){
			query.setUsercode(usercode);
		}
		if(!StringUtil.isEmpty(username)){
			query.setName(username);
		}
		if(!StringUtil.isEmpty(userorganization)){
			query.setUserorganization(userorganization);
		}
		if(!StringUtil.isEmpty(onlinestatus)){
			query.setOnlinestatus(Integer.valueOf(onlinestatus));
		}else{
			query.setOnlinestatus(1);
		}
		Map<String,Object> queryMap = new HashMap<>();
		queryMap = BeanUtils.toMap(query,para);
		queryMap.put("groupname", groupname);
		map = inscUserService.initonlineList(queryMap);
		return map;
	}

	/**
	 * 查询条件 机构树（异步）
	 * 
	 * @return
	 */
	@RequestMapping(value = "initdepttree", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> initDeptTree(
			@RequestParam(value = "id", required = false) String parentcode) {
		return deptService.queryTreeList(parentcode);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	@RequestMapping(value = "sss", method = RequestMethod.POST)
	@ResponseBody
	@Deprecated
	public void sss(@RequestBody String json) {
		net.sf.json.JSONObject dataPacket = net.sf.json.JSONObject.fromObject(json);
		interFaceService.valiedatePremiu(dataPacket);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	@RequestMapping(value = "getConfigInfo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getConfigInfo(@RequestBody String json) {
		net.sf.json.JSONObject dataPacket = net.sf.json.JSONObject.fromObject(json);
		if(!dataPacket.containsKey("inscomcode")||!dataPacket.containsKey("type")||!dataPacket.containsKey("singlesite")||!dataPacket.containsKey("key")){
			return "请求参数不完整。";
		}
		String inscomcode = dataPacket.getString("inscomcode");
		String type = dataPacket.getString("type");
		String singlesite = dataPacket.getString("singlesite");
		String key = dataPacket.getString("key");
		LogUtil.info("进入==Controler==配置信息：inscomcode" + inscomcode + ",type=" + type );
		return interFaceService.getConfigInfo(inscomcode, type, singlesite, key);
	}
	
	public static void main(String[] args) {
		String a = HttpClientUtil.doGet("http://119.29.111.199:9090/plugins/presence/status?jid=zhangdi@car.zzb&type=text", null);
		System.out.println(a);
	}
	@RequestMapping(value = "adddadikey", method = RequestMethod.GET)
	public ModelAndView addClassify(@RequestParam(value="taskid",required=true) String taskid,
			@RequestParam(value="inscomcode",required=true) String inscomcode){
		ModelAndView mav = new ModelAndView("cm/common/dadi");
		mav.addObject("taskid", taskid);
		mav.addObject("inscomcode", inscomcode);
		return mav;
	}
	@RequestMapping(value = "savedadikey",produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String saveClassify(@RequestParam(value="taskid",required=true) String taskid,
			@RequestParam(value="inscomcode",required=true) String inscomcode,
			@RequestParam(value="dadikey",required=true) String dadikey){
		if(StringUtil.isEmpty(taskid)||StringUtil.isEmpty(inscomcode)||StringUtil.isEmpty(dadikey)){
			return "fail";
		}else{
			try {
				redisClient.set(InterFaceService.DADIKEY, taskid + "@" + inscomcode, dadikey,20*60);
			} catch (Exception e) {
				e.printStackTrace();
				return JSONObject.toJSONString("fail");
			}
			LogUtil.info(redisClient.get(InterFaceService.DADIKEY, taskid + "@" + inscomcode));
			return JSONObject.toJSONString("success");
		}
	}
	

	/**
	 * 获取规则平台查询数据
	 * 部分需要造
  	 * @param taskid 工作流id
	 * @param insurancecompanyid 报价的保险公司id
	 * @return 大对象
	 * @throws ControllerException
	 */
	@RequestMapping(value = "getgzquerydata",produces="application/json;charset=UTF-8",method = RequestMethod.POST)
	@ResponseBody
	public String getGzQueryData(@RequestBody JSONObject json,HttpServletRequest request) throws ControllerException{
		String taskId = json.getString("taskId");
		String processType = json.getString("processType");
		String monitorid = json.getString("monitorid");
		String taskType = json.getString("taskType");
		LogUtil.info("进入==Controler==数据获取接口：taskid=" + taskId.split("@")[0] + ",inscomcode=" + taskId.split("@")[1] + ",json:" + json + "====");
		return JSONObject.toJSONStringWithDateFormat(interFaceService.getGzQueryData(taskId.split("@")[0],taskId.split("@")[1],processType,taskType,monitorid), "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
	}
	
	/**
	 * 机构在某个时间段报价总数量, 对外(供大数据)接口
	 * @param deptInnerCode 机构内部编码
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping(value="deptallquotecount", produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getDeptAllQuoteCount(@RequestParam(value="deptinnercode", required=true) String deptInnerCode,
			@RequestParam(value="starttime", required = true) String startTime,
			@RequestParam(value="endtime", required = true) String endTime){
		if(StringUtil.isEmpty(deptInnerCode) || StringUtil.isEmpty(startTime) || StringUtil.isEmpty(startTime)){
			return "请求参数为空";
		}
		
		return enternalInterFaceService.queryDeptAllQuoteCount(deptInnerCode, startTime, endTime);
	}
	
	/**
	 * task-1152(供大数据调用) 某段时间内，cm向cif推送(承保完成)的所有保单个数，及对应的保单号（含商业险保单号和交强险保单号）和车牌号
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping(value="getallpolicybytimeperiod", produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getallPolicyByTimePeriod(@RequestParam(value="starttime", required = true) String startTime,
			@RequestParam(value="endtime", required = true) String endTime){
		if(StringUtil.isEmpty(startTime) || StringUtil.isEmpty(startTime)){
			return "请求参数为空";
		}
		
		return enternalInterFaceService.getAllPolicynoByTimePeriod(startTime, endTime);
	}

	/**
	 * 平安二维码支付、保存大对象
	 * @param json 大对象
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "saveQRCodePacket",produces="application/json;charset=UTF-8",method = RequestMethod.POST)
	@ResponseBody
	public String saveQRCodePacket(@RequestBody String json) throws ControllerException{
		LogUtil.info("收到数据回写：" + json);
		try{
			String result = JSONObject.toJSONString(interFaceService.saveQRCodePacket(json));
			LogUtil.info("收到数据回写结果：" + result);
			return result;
		}catch (Exception e){
			LogUtil.error("数据回写失败：" + e.getMessage());
			e.printStackTrace();
		}
		return "回写时发生异常 ";
	}
	
	/**
	 * 北京平台, 核保完成，支付前，验证被保人身份证信息以及获取验证码
	 * @param idcardmodel
	 * @return
	 */
	@RequestMapping(value = "checkidcardandgetpin",produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String checkIDCardAndGetPin(@RequestBody IDCardModel idcardmodel){
		Map<String, String> result = enternalInterFaceService.checkIDCardAndGetPinCode(idcardmodel, "zzb");
		return JSONObject.toJSONString(result);
	}
	
	
	
	/**
	 * 北京平台, (供精灵回调)
	 * 1. 点击申请验证码，精灵验证被保人身份证信息以及获取验证码 回调此接口保存申请验证码结果
	 * 2. 前端提交验证码，CM请求精灵后同样回调此接口保存前端提交验证码结果
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "savepinwriteback",produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String savePINCodeWriteBack(@RequestBody String json){
		return enternalInterFaceService.savePinCodeFromFairy(json);
	}
	
	/**
	 * 北京平台, 前端获取身份证信息验证的结果，验证码结果
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "getpincodebj",produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getPinCodeBJ(@RequestBody String jsonstr){
		LogUtil.info("====前端获取身份证信息验证的结果，验证码结果,jsonstr=" + jsonstr);
		net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(jsonstr);
		String agentId = json.getString("agentId");
		String taskId = json.getString("taskId");
		String inscomcode = json.getString("inscomcode");
		return enternalInterFaceService.getPincodeResultInfo(agentId, taskId, inscomcode);
	}
	
	/**
	 * 北京平台, 重新发起申请验证码
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "reapplypin",produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String reapplyPin(@RequestBody String jsonstr){
		net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(jsonstr);
		String agentId = json.getString("agentId");
		String taskId = json.getString("taskId");
		String inscomcode = json.getString("inscomcode");
		return enternalInterFaceService.reapplyPinCode(agentId, taskId, inscomcode);
	}
	
	/**
	 * 北京平台, 前端录入验证码提交
	 * @param json
	 * @return 失败，可重新输入验证码再次提交; 若成功，就可选择支付方式，进行支付
	 */
	@RequestMapping(value = "commitpincode",produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String commitPinCode(@RequestBody String jsonstr){
		net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(jsonstr);
		String taskId = json.getString("taskId");
		String inscomcode = json.getString("inscomcode");
		String pincode = json.getString("pincode");
		String agentId = json.getString("agentId");
		Map<String, String> result = enternalInterFaceService.commitPinCode(agentId, taskId, inscomcode, pincode);
		return net.sf.json.JSONObject.fromObject(result).toString();
	}
	
	/**
	 * 北京平台, 前端获取电子保单
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "getelecpolicy",produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getElectronPolicy(@RequestBody String jsonstr){
		net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(jsonstr);
		String taskId = json.getString("taskId");
		return enternalInterFaceService.getElecPolicyPathInfo(taskId);
	}
}