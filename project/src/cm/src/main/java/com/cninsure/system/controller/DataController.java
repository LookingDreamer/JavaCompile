package com.cninsure.system.controller;

/**
 * test
 */
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSBOrgagentlogDao;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.manager.scm.INSASyncService;
import com.cninsure.system.manager.scm.INSCSyncService;
import com.cninsure.system.manager.scm.INSPSyncService;
import com.cninsure.system.manager.scm.impl.INSASyncServiceImpl;
import com.cninsure.system.manager.scm.impl.INSCSyncServiceImpl;
import com.cninsure.system.manager.scm.impl.INSPSyncServiceImpl;
import com.zzb.cm.entity.RULE_engine;
import com.zzb.cm.service.RULE_engineService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/systemdata/*")
public class DataController {
	@Resource
	private INSCSyncService syncService;
	@Resource
	private INSASyncService agtService;
	@Resource
	private INSPSyncService providerService;
	@Resource
	private INSBOrgagentlogDao insbOrgagentlogDao;
	@Resource
	private RULE_engineService ruleEngineService;

	@Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
	
	@RequestMapping(value = "data" ,method =RequestMethod.GET )
	public ModelAndView data()throws ControllerException{
		ModelAndView model = new ModelAndView("system/syncdata");
		return model;
	}
	
	@RequestMapping(value = "sync",method= RequestMethod.POST,produces = {"text/json;charset=UTF-8"})
	@ResponseBody
	public String sync(HttpSession session, @RequestParam(value="synctype", required=false)  String synctype,@RequestParam(value="agentcode", required=false)String agentcode,@RequestParam(value="comcode", required=false)String comcode)throws ControllerException{
		Map<String, Object> result= new HashMap<String, Object>();
		try{
			String operator = ((INSCUser)session.getAttribute("insc_user")).getUsercode();
			if ("dept".equalsIgnoreCase(synctype)) {
				if (syncService.getSyncCount() >= 0) {
					result.put("success", false);
					result.put("returnMsg", "机构数据正在同步中请勿重复点击");
					return JSONObject.fromObject(result).toString();
				} else {
					taskthreadPool4workflow.execute(new Runnable() {
						@Override
						public void run() {
							syncService.getSyncDeptData(operator,comcode);
						}
					});
					result.put("success", true);
					result.put("returnMsg", "机构同步中，请稍后查询同步状态信息");
					return JSONObject.fromObject(result).toString();
				}
			} else if ("agt".equalsIgnoreCase(synctype)) {
				if (agtService.getSyncCount() >= 0) {
					result.put("success", false);
					result.put("returnMsg", "代理人数据正在同步中请勿重复点击");
					return JSONObject.fromObject(result).toString();
				} else {
					taskthreadPool4workflow.execute(new Runnable() {
						@Override
						public void run() {
							if(StringUtil.isNotEmpty(agentcode)){
								agtService.getAgentData(operator,agentcode);
							}else{
								agtService.getAgentDataorOrg(operator, null);
							}
						}
					});
					result.put("success", true);
					result.put("returnMsg", "代理人同步中，请稍后查询同步状态信息");
					return JSONObject.fromObject(result).toString();
				}
			} else if ("insbprovider".equalsIgnoreCase(synctype)) {
				if (providerService.getSyncCount() >= 0) {
					result.put("success", false);
					result.put("returnMsg", "供应商数据正在同步中请勿重复点击");
					return JSONObject.fromObject(result).toString();
				} else {
					taskthreadPool4workflow.execute(new Runnable() {
						@Override
						public void run() {
							providerService.getSyncProviderData(operator);
						}
					});
					result.put("success", true);
					result.put("returnMsg", "供应商同步中，请稍后查询同步状态信息");
					return JSONObject.fromObject(result).toString();
				}
			} else {
				result.put("success", false);
				result.put("returnMsg", "同步类型错误");
				return JSONObject.fromObject(result).toString();
			}
		}catch(Exception e){
			LogUtil.info("同步数据异常：synctype="+synctype);
			e.printStackTrace();
			result.put("success", false);
			result.put("returnMsg", "同步数据异常");
		}
		return JSONObject.fromObject(result).toString();
	}
	
	@RequestMapping(value = "status",method= RequestMethod.POST,produces = {"text/json;charset=UTF-8"})
	@ResponseBody
	public String status()throws ControllerException{
		String msg = "<br/>";
		if (syncService.getSyncCount() >= 0) {
			if (syncService.getSyncProcess() < 0) {
				msg += "机构数据同步中，当前进度：机构数据查询中...";
			} else {
				msg += "机构数据同步中，当前进度：" + syncService.getSyncProcess() + "/" + syncService.getSyncCount();
			}
		} else {
			msg += "当前未执行机构数据同步，";
			Date maxSyncdate = insbOrgagentlogDao
					.getMaxSyncdate(Integer.valueOf(1));
			if (maxSyncdate != null) {
				msg += "上次同步时间是：" + DateUtil.toDateTimeString(maxSyncdate);
			} else {
				msg += "也未同步过数据";
			}
		}
		msg += "<br/>";
		if (agtService.getSyncCount() >= 0) {
			if (agtService.getSyncProcess() < 0) {
				msg += "机构数据同步中，当前进度：代理人数据查询中...";
			} else {
				msg += "代理人数据同步中，当前进度：" + agtService.getSyncProcess() + "/" + agtService.getSyncCount();
			}
		} else {
			msg += "当前未执行代理人数据同步，";
			Date maxSyncdate = insbOrgagentlogDao
					.getMaxSyncdate(Integer.valueOf(2));
			if (maxSyncdate != null) {
				msg += "上次同步时间是：" + DateUtil.toDateTimeString(maxSyncdate);
			} else {
				msg += "也未同步过数据"; 
			}
		}
		msg += "<br/>";
		if (providerService.getSyncCount() >= 0) {
			if (providerService.getSyncProcess() < 0) {
				msg += "机构数据同步中，当前进度：供应商数据查询中...";
			} else {
				msg += "供应商数据同步中，当前进度：" + providerService.getSyncProcess() + "/" + providerService.getSyncCount();
			}
		} else {
			msg += "当前未执行供应商数据同步，";
			Date maxSyncdate = insbOrgagentlogDao
					.getMaxSyncdate(Integer.valueOf(3));
			if (maxSyncdate != null) {
				msg += "上次同步时间是：" + DateUtil.toDateTimeString(maxSyncdate);
			} else {
				msg += "也未同步过数据";
			}
		}
		Map<String, Object> result  = new HashMap<String, Object>();
		result.put("success", true);
		result.put("returnMsg", msg);
		return JSONObject.fromObject(result).toString();
	}
	
	
	@RequestMapping(value = "ruleengine",method= RequestMethod.POST,produces = {"text/plain;charset=UTF-8"})
	@ResponseBody
	public String ruleengine(
			@RequestParam(value="id", required=true)  String id,
			@RequestParam(value="rule_engine_id", required=true)Integer rule_engine_id,
			@RequestParam(value="rule_base_type", required=true)Integer rule_base_type,
			@RequestParam(value="city_id", required=true)Integer city_id,
			@RequestParam(value="company_id", required=true)  Integer company_id,
			@RequestParam(value="rule_base_name", required=true)String rule_base_name,
			@RequestParam(value="rule_base_postil", required=true)String rule_base_postil,
			@RequestParam(value="status", required=true)Integer status
			)
			throws ControllerException{
		
		RULE_engine ruleEngine = new RULE_engine();
//		ruleEngine.setId(id);//RULE_engine表的id字段为自增类型不需要传id
		ruleEngine.setRule_engine_id(rule_engine_id);
		ruleEngine.setRule_base_type(rule_base_type);
		ruleEngine.setCity_id(city_id);
		ruleEngine.setCompany_id(company_id);
		ruleEngine.setRule_base_name(rule_base_name);
		ruleEngine.setRule_base_postil(rule_base_postil);
		ruleEngine.setStatus(status);
		ruleEngine.setLast_updated(new Date());
		Map<String, Object> result  = new HashMap<String, Object>();
		LogUtil.info("规则发布同步数据："+ruleEngine.toString());
		result.put("success", true);
		result.put("returnMsg", "保存成功");
		try {
			ruleEngineService.saveOrudpateRuleEngine(ruleEngine);
		} catch (Exception e) {
			result.put("success", false);
			result.put("returnMsg", ruleEngine.toString());
		}
		return JSONObject.fromObject(result).toString();
	}
	public static void main(String[] args) {
		if(StringUtil.isNotEmpty("")){
			System.out.println("1");
		}else{
			System.out.println("0");
		}
	}
}
