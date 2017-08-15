package com.zzb.cm.Interface.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;

import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.monitor.main.MonitorUtil;
import com.cninsure.system.service.INSCDeptService;
import com.common.CMMethod;
import com.common.ConstUtil;
import com.common.ModelUtil;
import com.zzb.chn.service.CHNPaymentService;
import com.zzb.cm.Interface.entity.car.model.ConfigInfo;
import com.zzb.cm.Interface.service.ExternalInterFaceService;
import com.zzb.cm.dao.INSBInsuredDao;
import com.zzb.cm.dao.INSBInsuredhisDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.entity.INSBAutoconfigshowQueryModel;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.service.INSBAutoconfigshowService;
import com.zzb.conf.service.INSBPolicyitemService;
import com.zzb.conf.service.INSBPrvaccountkeyService;
import com.zzb.mobile.model.IDCardModel;

import net.sf.json.JSONObject;

@Service
public class ExternalInterFaceServiceImp implements ExternalInterFaceService {
	@Resource
	INSBQuoteinfoDao insbQuoteinfoDao;
	@Resource
	INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	INSBPrvaccountkeyService insbPrvaccountkeyService;
	@Resource
	INSBInsuredDao insbInsuredDao;
	@Resource
	INSBInsuredhisDao insbInsuredhisDao;
	@Resource
	INSBPersonDao insbPersonDao;
	@Resource
	INSBQuoteinfoService insbQuoteinfoService;
	@Resource
	INSBAutoconfigshowService insbAutoconfigshowService;
	@Resource
	INSBPolicyitemService insbPolicyitemService;
	@Resource
	INSCDeptService inscDeptService;
	@Resource
    private CHNPaymentService chnPaymentService;
	
	/**
	 * 获取本地回写url
	 * @return
	 */
	public String getLocalWriteBackUrl(){
		return "http://" + ValidateUtil.getConfigValue("localhost.ip") + ":" + ValidateUtil.getConfigValue("localhost.port") + "/" + ValidateUtil.getConfigValue("localhost.projectName");
	}
	
	/**
	 * 发送请求精灵接口
	 * @param taskId
	 * @param inscomcode
	 * @param deptCode
	 * @param quoteType
	 * @param params
	 * @return
	 */
	public Map<String, String> goToFairy(String taskId, String inscomcode, String deptCode,
			String quoteType, Map<String, Object> params){
		Map<String, String> result = new HashMap<String, String>();
		try {
			String taskType = params.get("taskType").toString();
			List<Object> targetUrl = this.getTargeturl(quoteType, inscomcode, deptCode, CMMethod.getConfTypeByTaskType(taskType));
			
			//测试使用
//			List<Object> targetUrl = new ArrayList<Object>();
//			INSBElfconf elf = new INSBElfconf();
//			elf.setId("b3984fd00faa11e7f37403e0f9c69060");
//			elf.setElfpath("http://10.68.14.50:8080/interface/quote");
//			elf.setNoti("applypinbj");
//			targetUrl.add(elf);
			ConfigInfo configInfo = this.getConfigInfo(inscomcode, deptCode, "robot");
			if (StringUtil.isEmpty(configInfo)) {
				LogUtil.error("zzb" + "====申请/提交验证码----获取保险公司配置数据信息：taskId=" + taskId + ",inscomcode=" + inscomcode + ", 配置信息:(无)");
			} else {
				LogUtil.info("zzb" + "====申请/提交验证码----获取保险公司配置数据信息：taskId=" + taskId + ",inscomcode=" + inscomcode + ", " + "配置信息:" + configInfo.getConfigMap());
			}
			params.put("configInfo", configInfo);
			params.put("deptCode", deptCode);
			//获取orgCode
			params.put("orgCode", inscDeptService.getPingTaiDeptId(deptCode));// 法人机构代码
			LogUtil.info("开始==通知精灵接口(北京流程), taskid=" + taskId + ", targetUrl=" + targetUrl + ", 请求参数:" + JSONObject.fromObject(params).toString());
			//请求精灵
			result = MonitorUtil.go2RobotTwo(taskId, inscomcode, taskType, params, targetUrl);
			
			LogUtil.info("结束==成功发送精灵接口(北京流程), taskid=" + taskId + ", 精灵返回结果result=" + JSONObject.fromObject(result).toString());
			result.put("status", "success");
		} catch (Exception e) {
			LogUtil.error("通知精灵接口(北京流程)发生异常taskid=" + taskId);
			e.printStackTrace();
			result.put("status", "fail");
		}
		
		return result;
	}
	
	/**
	 * 获取精灵或EDI的请求网址
	 * @param quotetype  报价类型：01-EDI 02-精灵
	 * @param inscomcode 供应商
	 * @param deptcode 结构code
	 * @param conftype 配置类型  01：报价配置 :02：核保配置:03：续保配置.04:承保配置
	 * @return
	 */
	public List<Object> getTargeturl(String quotetype, String inscomcode, String deptcode, String conftype){
		List<Object> returnList = new ArrayList<Object>();
		List<String> tempquotetype = new ArrayList<String>();
		tempquotetype.add(quotetype);
		
		INSBAutoconfigshowQueryModel queryModel = new INSBAutoconfigshowQueryModel();
		queryModel.setProviderid(inscomcode);
		queryModel.setDeptId(deptcode);
		queryModel.setQuoteList(tempquotetype);
		queryModel.setConftype(conftype);
		Map<String,Object>  resultMap = insbAutoconfigshowService.getElfEdiByNearestDept(queryModel);
		
		if(resultMap.get("elf") !=null){
			returnList.add(resultMap.get("elf"));
		}
		if(resultMap.get("edi")!=null){
			returnList.add(resultMap.get("edi"));
		}
		
		return returnList;
	}
	
	private ConfigInfo getConfigInfo(String inscomcode, String deptid, String processType) {
		try {
			ConfigInfo configInfo = new ConfigInfo();
			List<Map<String, Object>> resultInfo = insbPrvaccountkeyService.queryConfigInfo(deptid, inscomcode.substring(0, 4), "edi".equals(processType) ? "2" : "1");
			Map<String, String> dataMap = new HashMap<>();
			for(Map<String, Object> tempMap : resultInfo){
				String tempKey = String.valueOf(tempMap.get("paramname"));
				if(StringUtil.isNotEmpty(tempKey)){
					dataMap.put(tempKey, String.valueOf(tempMap.get("paramvalue")));
				}
			}
			configInfo.setConfigMap(dataMap);
			return configInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 核保完成，支付前，验证被保人身份证信息以及获取验证码
	 * @param idcardmodel
	 * @param from 由掌中宝前端过来的修改person表，其它不修改person表
	 * @return
	 */
	@Override
	public Map<String, String> checkIDCardAndGetPinCode(IDCardModel idcardmodel, String from){
		Map<String, String> returnMap = new HashMap<String, String>();
		LogUtil.info(from + "申请验证码-1-进入接口, 验证被保人身份证信息并获取验证码, 参数=" + JSONObject.fromObject(idcardmodel).toString());
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		String taskId = idcardmodel.getTaskid();
		String inscomcode = idcardmodel.getInscomcode();
		String agentId = idcardmodel.getAgentid();
		try {
			//取被保人信息
			INSBInsuredhis insbinsuredhis = new INSBInsuredhis();
			insbinsuredhis.setTaskid(taskId);
			insbinsuredhis.setInscomcode(inscomcode);
			insbinsuredhis = insbInsuredhisDao.selectOne(insbinsuredhis);
			if(insbinsuredhis == null){
				returnMap.put("status", "fail");
				returnMap.put("errormsg", "申请验证码没有找到被保人信息!");
				LogUtil.error(from + "申请验证码没有找到被保人信息!taskid=" + taskId);
				return returnMap;
			}
			INSBPerson person = insbPersonDao.selectById(insbinsuredhis.getPersonid());

			//北京流程添加投保单号给精灵
			Map<String,String> hashMap = new HashedMap();
			hashMap = getProNoByTaskid(taskId, inscomcode);
			if(hashMap.containsKey("jqpolicyno")){
				paramsMap.put("jqpolicyno", hashMap.get("jqpolicyno"));
			}
			if(hashMap.containsKey("sypolicyno")){
				paramsMap.put("sypolicyno", hashMap.get("sypolicyno"));
			}
			if(hashMap.containsKey("applicantname")){
				paramsMap.put("applicantname", hashMap.get("applicantname"));//投保人姓名
			}

			//从前端过来的请求修改被保人信息
			if(from.equals("zzb")){
				LogUtil.info(from + "申请验证码修改被保人信息, taskid=" + taskId + ", 旧数据:" + JSONObject.fromObject(person).toString());
				person.setOperator(agentId);
				person.setName(idcardmodel.getName());
				person.setGender(ModelUtil.getGenderByIdCard(idcardmodel.getCardnum()));
				person.setIdcardtype(0); //身份证
				person.setIdcardno(idcardmodel.getCardnum());
				person.setNation(idcardmodel.getNation()); //民族
				person.setBirthday(DateUtil.parse(idcardmodel.getBirthday()));
				person.setAddress(idcardmodel.getAddress());
				person.setExpdate(idcardmodel.getExpdate());
				person.setAuthority(idcardmodel.getAuthority());
				person.setCellphone(idcardmodel.getTelephone());
				person.setExpstartdate(DateUtil.parse(idcardmodel.getExpstartdate()));	//新增的开始期限
				person.setExpenddate(DateUtil.parse(idcardmodel.getExpenddate()));	//结束期限
				int upRes = insbPersonDao.updateById(person);
				if(upRes == 1){
					LogUtil.info(from + "成功修改被保人信息, taskid=" + taskId + ", 新数据:" + JSONObject.fromObject(person).toString());
				}
			}

			paramsMap.put("taskId", taskId);
			paramsMap.put("insComId", inscomcode.substring(0,4));
			//加入被保人身份证信息
			paramsMap.put("taskType", ConstUtil.TASKTYPE_APPLY_PIN_BJ);//applypinbj申请验证码
			paramsMap.put("pinType", "0"); //0-申请验证码，1-是提交验证码
			paramsMap.put("name", person.getName());//被保人
			paramsMap.put("gender", person.getGender() + "");
			paramsMap.put("idcardnum", person.getIdcardno());
			paramsMap.put("nation", person.getNation());
			paramsMap.put("birthday", StringUtil.isEmpty(person.getBirthday())?"":DateUtil.toString(person.getBirthday()));
			paramsMap.put("address", person.getAddress());
			paramsMap.put("expdate", person.getExpdate());
			paramsMap.put("email", person.getEmail());//邮箱
			//新增开始结束期限
			paramsMap.put("expstartdate", StringUtil.isEmpty(person.getExpstartdate())?"":DateUtil.toString(person.getExpstartdate()));
			paramsMap.put("expenddate", StringUtil.isEmpty(person.getExpenddate())?"":DateUtil.toString(person.getExpenddate()));
			paramsMap.put("authority", person.getAuthority());
			paramsMap.put("telephone", person.getCellphone());
			paramsMap.put("callbackurlbj", this.getLocalWriteBackUrl() + "/interface/savepinwriteback");//精灵回调CM调用接口
			INSBQuoteinfo insbquoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(taskId, inscomcode);
			if(insbquoteinfo == null){
				returnMap.put("status", "fail");
				returnMap.put("errormsg", from + "申请验证码未找到报价信息!");
				LogUtil.error(from + "申请验证码未找到报价信息!taskid=" + taskId);
				return returnMap;
			}
			
			/*ConfigInfo configInfo = this.getConfigInfo(inscomcode, insbquoteinfo.getDeptcode(), "robot");
			if (StringUtil.isEmpty(configInfo)) {
				LogUtil.error(from + "====申请验证码-1.1-获取保险公司配置数据信息：taskId=" + taskId + ",inscomcode=" + inscomcode + ", 配置信息:(无)");
			} else {
				LogUtil.info(from + "====申请验证码-1.1-获取保险公司配置数据信息：taskId=" + taskId + ",inscomcode=" + inscomcode + ", " + "配置信息:" + configInfo.getConfigMap());
			}
			paramsMap.put("configInfo", configInfo);*/
			
			LogUtil.info(from + "申请验证码-2-开始精灵验证被保人身份证信息并获取验证码, taskid=" + taskId + ", 请求参数:" + paramsMap.toString());
		
			//请求精灵接口
			String quoteType = "02"; //报价类型选精灵
			Map<String, String> result = this.goToFairy(taskId, inscomcode, insbquoteinfo.getDeptcode(), quoteType, paramsMap);
			
			
			
			returnMap.put("status", result.get("status"));
			
			//申请成功后，更新申请状态
			if(result.containsKey("flag") && (result.get("flag").equals("true"))){
				LogUtil.info(from + "申请验证码-3-结束精灵验证被保人身份证信息并获取验证码,taskid=" + taskId + ", 精灵返回结果result=" + result.toString());
				
				insbinsuredhis.setApplystatus(ConstUtil.PIN_APPLING);
				insbinsuredhis.setOperator(agentId);
				insbInsuredhisDao.updateById(insbinsuredhis);
			} else {
				LogUtil.error("申请验证码-4-结束精灵验证被保人身份证信息并获取验证码[BJ] 发生异常, taskid=" + taskId + ", 精灵返回结果result=" + result.toString());
			}
		} catch (Exception e) {
			LogUtil.error("通知精灵验证被保人身份证信息并获取验证码 发生异常, taskid=" + taskId);
			e.printStackTrace();
			returnMap.put("status", "fail");
			returnMap.put("errormsg", "申请验证码请求精灵接口发生异常!");
		}
		
		return returnMap;
	}
	
	/**
	 * 核保完成，支付前，前端输入验证码确认
	 * @param idcardmodel
	 * @return
	 */
	@Override
	public Map<String, String> commitPinCode(String agentId, String taskId, String inscomcode, String pincode){
		LogUtil.info("提交验证码=1=进入接口, 参数taskid=" + taskId +", inscomcode=" + inscomcode + ", 验证码pincode=" + pincode + ",agentId=" + agentId);
		Map<String, String> returnMap = new HashMap<String, String>();
		if(StringUtil.isEmpty(taskId) || StringUtil.isEmpty(inscomcode) || StringUtil.isEmpty(pincode)){
			returnMap.put("status", "fail");
			returnMap.put("errormsg", "参数不能为空!");
//			returnMap.put("errorcode", ConstUtil.PIN_COMMIT_FAIL);
			LogUtil.error("参数有误!taskid=" + taskId +", inscomcode=" + inscomcode +", pincode=" + pincode + ",agentId=" + agentId);
			return returnMap;
		}
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		//取被保人信息
		INSBInsuredhis insbinsuredhis = new INSBInsuredhis();
		insbinsuredhis.setTaskid(taskId);
		insbinsuredhis.setInscomcode(inscomcode);
		insbinsuredhis = insbInsuredhisDao.selectOne(insbinsuredhis);
		if(insbinsuredhis == null || StringUtil.isEmpty(pincode)){
			returnMap.put("status", "fail");
			returnMap.put("errormsg", "没有找到被保人验证码信息!");
//			returnMap.put("errorcode", ConstUtil.PIN_COMMIT_FAIL);
			LogUtil.error("没有找到被保人验证码信息!taskid=" + taskId +", inscomcode=" + inscomcode);
			return returnMap;
		}
		
		//0表示正在获取中 1-申请成功，2-姓名身份证信息错误，3-手机号绑定超限，4-身份证绑定超限,
		if(StringUtil.isEmpty(insbinsuredhis.getApplystatus())){//没有点申请的情况下、直接选择提交验证码
			returnMap.put("status", "fail");
			returnMap.put("errormsg", "请先点击申请验证码按钮后，再点击提交按钮");
			LogUtil.error("申请验证码步骤还没完成" + "! taskid=" + taskId + ", inscomcode=" + inscomcode);
			return returnMap;
		}else if (!insbinsuredhis.getApplystatus().equals(ConstUtil.PIN_APPLY_SUCCESS)){
			returnMap.put("status", "fail");
			returnMap.put("errormsg", "申请验证码的状态不是成功, 申请的结果为" + insbinsuredhis.getApplystatus());
//			returnMap.put("errorcode", ConstUtil.PIN_COMMIT_FAIL);
			LogUtil.error("验证码的状态不是1, 当前状态为" + insbinsuredhis.getApplystatus() + "! taskid=" + taskId + ", inscomcode=" + inscomcode);
			return returnMap;
		}

		insbinsuredhis.setPincode(pincode);//录入前端输入的验证码

		//取被保人相关详细信息
		INSBPerson person = insbPersonDao.selectById(insbinsuredhis.getPersonid());
		paramsMap.put("taskId", taskId);
		paramsMap.put("insComId", inscomcode.substring(0,4));
		//加入被保人身份证信息
		paramsMap.put("taskType", ConstUtil.TASKTYPE_APPLY_PIN_BJ);//applypinbj申请验证码
		paramsMap.put("pinType", "1"); //0-申请验证码，1-是提交验证码
		paramsMap.put("pinCode", pincode); //前端输入的验证码
		paramsMap.put("telephone", person.getCellphone()); //手机号
		//新增申请验证码时候的人物参数
		paramsMap.put("birthday", DateUtil.toString(person.getBirthday()));
		paramsMap.put("idcardnum", person.getIdcardno());
		paramsMap.put("authority", person.getAuthority());
		paramsMap.put("address", person.getAddress());
		paramsMap.put("name", person.getName());//被保人
		paramsMap.put("gender", person.getGender() + "");
		String expdate = person.getExpdate();	//"2015.05.19-2025.05.19" 有限期间格式
		paramsMap.put("expdate", expdate);
		/*if(StringUtil.isNotEmpty(expdate)){
			paramsMap.put("expstartdate", expdate.substring(0, expdate.indexOf("-")).replace(".","-"));
			paramsMap.put("expenddate", expdate.substring(expdate.indexOf("-")+1, expdate.length()).replace(".","-"));
		}*/
		//0603提交验证码 期限从表里面带出
		paramsMap.put("expstartdate",DateUtil.toString(person.getExpstartdate()));
		paramsMap.put("expenddate",DateUtil.toString(person.getExpenddate()));
		paramsMap.put("email", person.getEmail());//邮箱
		paramsMap.put("nation", person.getNation());//民族

		paramsMap.put("callbackurlbj", this.getLocalWriteBackUrl() + "/interface/savepinwriteback");//精灵回调CM调用接口

		//北京流程 新增投保单号带过去提交验证码
		Map<String,String> hashMap = new HashedMap();
		hashMap = getProNoByTaskid(taskId, inscomcode);
		if(hashMap.containsKey("jqpolicyno")){
			paramsMap.put("jqpolicyno", hashMap.get("jqpolicyno"));
		}
		if(hashMap.containsKey("sypolicyno")){
			paramsMap.put("sypolicyno", hashMap.get("sypolicyno"));
		}
		if(hashMap.containsKey("applicantname")){
			paramsMap.put("applicantname", hashMap.get("applicantname"));
		}

		INSBQuoteinfo insbquoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(taskId, inscomcode);
		LogUtil.info("提交验证码=2=开始请求精灵, taskid=" + taskId + ", 请求参数:" + paramsMap.toString());
		try {
			//请求精灵接口
			String quoteType = "02"; //报价类型选精灵
			Map<String, String> result = this.goToFairy(taskId, inscomcode, insbquoteinfo.getDeptcode(), quoteType, paramsMap);

			if(result.containsKey("flag") && (result.get("flag").equals("true"))){
				insbinsuredhis.setCommitstatus(ConstUtil.PIN_COMMITTING);
				insbinsuredhis.setOperator(agentId);
				LogUtil.info("提交验证码=3=结束请求精灵提交验证码,taskid=" + taskId + ", 精灵返回结果result=" + result.toString());
			} else {
				LogUtil.error("提交验证码=4=结束精灵提交验证码 发生异常, taskid=" + taskId + ", 精灵返回结果result=" + result.toString());
			}
			insbInsuredhisDao.updateById(insbinsuredhis);
			returnMap.put("status", "success");
		} catch (Exception e) {
			LogUtil.error("通知精灵提交验证码 发生异常, taskid=" + taskId);
			e.printStackTrace();
			returnMap.put("status", "fail");
			returnMap.put("errormsg", "通知请求精灵提交验证码 发生异常!");
		}
		
		return returnMap;
	}
	
	/**
	 * 重新发起申请验证码
	 * @param agentId
	 * @param taskId
	 * @param inscomcode
	 * @return
	 */
	public String reapplyPinCode(String agentId, String taskId, String inscomcode){
		Map<String, String> result = new HashMap<String, String>();
		IDCardModel idcardmodel = new IDCardModel();
		INSBInsuredhis insbinsuredhis = new INSBInsuredhis();
		insbinsuredhis.setTaskid(taskId);
		insbinsuredhis.setInscomcode(inscomcode);
		insbinsuredhis = insbInsuredhisDao.selectOne(insbinsuredhis);
		if(insbinsuredhis == null){
			result.put("status", "fail");
			result.put("errormsg", "没有找到被保人验证码信息!");
			return JSONObject.fromObject(result).toString();
		}
		INSBPerson person = insbPersonDao.selectById(insbinsuredhis.getPersonid());
		idcardmodel.setTaskid(taskId);
		idcardmodel.setInscomcode(inscomcode);
		idcardmodel.setAgentid(agentId);
		idcardmodel.setName(person.getName());
		idcardmodel.setSex(person.getGender() == 1? "女" : "男");
		idcardmodel.setCardnum(person.getIdcardno());
		idcardmodel.setName(person.getNation());
		idcardmodel.setBirthday(DateUtil.toString(person.getBirthday()));
		idcardmodel.setAddress(person.getAddress());
		idcardmodel.setExpdate(person.getExpdate());
		idcardmodel.setTelephone(person.getCellphone());
		
		result = this.checkIDCardAndGetPinCode(idcardmodel, "zzb重新");
		return JSONObject.fromObject(result).toString();
	}
	
	/**
	 * 精灵回调, 保存身份证验证信息和验证码
	 * @param json
	 */
	@Override
	public String savePinCodeFromFairy(String json){
		//{"inscomcode":"202244","taskType":"applypinbj","pinType":"0","taskid":"1842446","result":"true","statusCode":"0", "errorMessage":""}
		LogUtil.info("收到==精灵验证码信息==回调, 开始保存数据, json=" + json);
		JSONObject jsonObj = JSONObject.fromObject(json);
		
		String result = jsonObj.getString("result");
		String pinType = jsonObj.getString("pinType");//0-申请验证码，1-提交验证码
		String taskId = jsonObj.getString("taskId");
		String taskType = jsonObj.getString("taskType"); //用于拓展,目前就只有一个applypinbj
		String inscomcode = jsonObj.getString("inscomcode");
		//获取被保人信息
		INSBInsuredhis insbinsuredhis = new INSBInsuredhis();
		insbinsuredhis.setTaskid(taskId);
		insbinsuredhis.setInscomcode(inscomcode);
		insbinsuredhis = insbInsuredhisDao.selectOne(insbinsuredhis);
		if(insbinsuredhis == null){
			LogUtil.error("错误提示：未找到被保人信息,无法保存精灵验证码回调数据, taskId=" + taskId + ", inscomcode=" + inscomcode);
			return "success";
		}
		//pinType 0-申请验证码，1-提交验证码
		String oldStatus = null;
		String statusNewCode = jsonObj.getString("statusCode");
		boolean chnPinCodeSecondPay = false;
		
		if(result.equals("true") == false){
			String errorMessage = jsonObj.getString("errorMessage");
			if(StringUtil.isNotEmpty(pinType) && pinType.equals("0")){
				LogUtil.info("精灵回调=申请验证码=操作失败, taskId=" + taskId + ", inscomcode=" + inscomcode + ", 错误原因errorMessage=" + errorMessage);
			} else {
				LogUtil.info("精灵回调=提交验证码=操作失败, taskId=" + taskId + ", inscomcode=" + inscomcode + ", 错误原因errorMessage=" + errorMessage);
			}
			insbinsuredhis.setPinerrmsg(errorMessage);
		} else {
			//Applystatus: 0-正在获取中 1-验证码发送成功，2-验证码发送失败，3-姓名身份证信息错误，4-申请验证码数据有误，5-代码异常
			//Commitstatus: 0-正在提交中 1-验证码提交成功，2-验证码提交失败，3-验证码有误,重新输入，4-提交验证码数据有误（验证码为空，投保单号为空），5-代码异常
			if(StringUtil.isNotEmpty(statusNewCode)){
				if(StringUtil.isNotEmpty(pinType) && pinType.equals("0")){
					oldStatus = insbinsuredhis.getApplystatus();
					insbinsuredhis.setApplystatus(statusNewCode);
					if(statusNewCode.equals("1")){
						//验证码申请成功，设置获取时间用于超时
						insbinsuredhis.setPindatetime(new Date());
					}
				} else if (StringUtil.isNotEmpty(pinType) && pinType.equals("1")) {
					oldStatus = insbinsuredhis.getCommitstatus();
					insbinsuredhis.setCommitstatus(statusNewCode);
					
					//渠道支付前置验证码提交成功要推二支工作流
					if ("1".equals(statusNewCode)) { 
						chnPinCodeSecondPay = true;
					}
				}
				insbinsuredhis.setNoti("fairywb");//备注精灵回写
			}
		}
		//保存数据
		insbInsuredhisDao.updateById(insbinsuredhis);
		
		LogUtil.info("结束=成功保存精灵验证码信息=! taskId=" + taskId + ", inscomcode=" + inscomcode + 
				", 保存类型pinType=" + (pinType.equals("0")? "申请" : "提交") + ", 状态码由 " + oldStatus + " 转为 " + statusNewCode);
		
		if (chnPinCodeSecondPay) {
			try {
				chnPaymentService.chnPinCodeSecondPay(taskId, inscomcode);
			} catch (Exception e) {
				LogUtil.error(taskId + "-" + inscomcode + "chnPinCodeSecondPay异常：" + e.getMessage()); 
				e.printStackTrace();
			}
		}
		
		return "success";
	}
	
	/**
	 * 前端获取"身份证验证结果" 和 "验证码最终结果" 信息
	 * @param taskId
	 * @param inscomcode
	 * @return
	 */
	@Override
	public String getPincodeResultInfo(String agentId, String taskId, String inscomcode){
		LogUtil.info("开始获取验证码状态信息, taskId=" + taskId + ", inscomcode=" + inscomcode +"', 代理人agentId=" + agentId);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if(StringUtil.isEmpty(taskId) || StringUtil.isEmpty(inscomcode)){
			returnMap.put("status", "fail");
			returnMap.put("errormsg", "参数不能为空!");
			LogUtil.error("参数有误!taskid=" + taskId +", inscomcode=" + inscomcode + ",agentId=" + agentId);
			return JSONObject.fromObject(returnMap).toString();
		}
		
		INSBInsuredhis insbinsuredhis = new INSBInsuredhis();
		insbinsuredhis.setTaskid(taskId);
		insbinsuredhis.setInscomcode(inscomcode);
		insbinsuredhis = insbInsuredhisDao.selectOne(insbinsuredhis);
		if(insbinsuredhis == null){
			returnMap.put("status", "fail");
			returnMap.put("errormsg", "获取验证码结果信息失败, 原因没有找到被保人信息!");
			LogUtil.error("前端获取验证码结果信息失败, 原因没有找到被保人信息!taskid=" + taskId);
			return JSONObject.fromObject(returnMap).toString();
		}
		
		//造假数据与前端联调
//		insbinsuredhis.setApplystatus(new Random().nextInt(6) + "");
//		insbinsuredhis.setCommitstatus(new Random().nextInt(6) + "");
//		insbinsuredhis.setPindatetime(new Date());
		
		if(insbinsuredhis.getPindatetime() != null && 
				new Date().getTime()-insbinsuredhis.getPindatetime().getTime() > (24 * 60 * 60 * 1000L)){
			insbinsuredhis.setCommitstatus(ConstUtil.PIN_COMMIT_TIMEOUT);
			insbInsuredhisDao.updateById(insbinsuredhis);
		}
		
		
		returnMap.put("status", "success");
		//applysta: 0-正在获取中 1-验证码发送成功，2-验证码发送失败，3-姓名身份证信息错误，4-申请验证码数据有误，5-未知异常, null-没有状态数据
		returnMap.put("applysta", insbinsuredhis.getApplystatus());
		
		//0-正在提交中 1-验证码提交成功，2-验证码提交失败，3-验证码有误,重新输入，4-提交验证码数据有误（验证码为空，投保单号为空），5-未知异常, null-没有状态数据
		returnMap.put("commitsta", insbinsuredhis.getCommitstatus());
		
		LogUtil.info("结束获取验证码状态信息, taskId=" + taskId + ", 返回结果:" + returnMap.toString());
		return JSONObject.fromObject(returnMap).toString();
	}
	
	/**
	 * 前端获取电子保单cos路径
	 */
	@Override
	public String getElecPolicyPathInfo(String taskId) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "success");
		//交强
		String jpElecPolicyFilePath = insbPolicyitemService.getElecPolicyFilePath(taskId, "1");
		//商业
		String syElecPolicyFilePath = insbPolicyitemService.getElecPolicyFilePath(taskId, "0");
		resultMap.put("jpElecPolicyFilePath", jpElecPolicyFilePath == null? "" : jpElecPolicyFilePath);
		resultMap.put("syElecPolicyFilePath", syElecPolicyFilePath == null? "" : syElecPolicyFilePath);
		
		return JSONObject.fromObject(resultMap).toString();
	}
	
	/**
	 * 机构在某个时间段报价总数量, 对外(供大数据)接口
	 * @param deptInnerCode 机构内部编码
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@Override
	public String queryDeptAllQuoteCount(String deptInnerCode, String startTime, String endTime) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("deptinnercode", deptInnerCode);
		param.put("starttime", startTime);
		param.put("endtime", endTime);
		LogUtil.info("查询机构在某个时间段报价总数量,请求参数" + param.toString());
		List<Map<String, String>> resultData = insbQuoteinfoDao.queryDeptAllQuoteCount(param);
		
		//返回
		int totalcount = 0;
		StringBuffer sbf = new StringBuffer();
		if(resultData != null && resultData.size() > 0){
			totalcount = resultData.size();
			Map<String, String> temp = null;
			for(int i = 0; i< totalcount; i++){
				temp = resultData.get(i);
				if(i == (totalcount - 1)){
					sbf.append(temp.get("platenumber"));
				} else {
					sbf.append(temp.get("platenumber")).append(",");
				}
			}
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("totalcount", String.valueOf(totalcount));
		result.put("platenumbers", sbf.toString());
		return JSONObject.fromObject(result).toString();
	}
	
	/**
	 * task-1152(供大数据调用) 某段时间内，cm向cif推送(承保完成)的所有保单个数，及对应的保单号（含商业险保单号和交强险保单号）和车牌号
	 * @return
	 */
	public String getAllPolicynoByTimePeriod(String startTime, String endTime){
		Map<String, String> param = new HashMap<String, String>();
		param.put("starttime", startTime);
		param.put("endtime", endTime);
		List<Map<String, String>> resultData = insbPolicyitemDao.selectAllPolicynoByTimePeriod(param);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("totalcount", resultData == null ? "0" : resultData.size() + "");
		result.put("policyiteminfo", resultData == null ? "" : resultData);
		return JSONObject.fromObject(result).toString();
	}

	/**
	*根据任务号和inscomcode查询投保单号和投保人信息
	 *@return
	**/
	private Map getProNoByTaskid(String taskid, String inscomcode){
		Map<String,String> hashmap = new HashedMap();
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskid);
		insbPolicyitem.setInscomcode(inscomcode);
		List<INSBPolicyitem> insbPolicyitemList = insbPolicyitemDao.selectList(insbPolicyitem);
		if(insbPolicyitemList!=null && insbPolicyitemList.size()>0) {
			for (INSBPolicyitem insbPolicyitem1 : insbPolicyitemList) {
				if("0".equals(insbPolicyitem1.getRisktype()) && StringUtil.isNotEmpty(insbPolicyitem1.getProposalformno())){//商业险
					hashmap.put("sypolicyno",insbPolicyitem1.getProposalformno());
				}else if("1".equals(insbPolicyitem1.getRisktype()) && StringUtil.isNotEmpty(insbPolicyitem1.getProposalformno())){//交强险
					hashmap.put("jqpolicyno",insbPolicyitem1.getProposalformno());
				}
				hashmap.put("applicantname",insbPolicyitem1.getApplicantname());//投保人姓名
			}
		}
		return hashmap;
	}
}
