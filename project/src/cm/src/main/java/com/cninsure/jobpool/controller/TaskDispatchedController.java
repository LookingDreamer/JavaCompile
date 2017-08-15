package com.cninsure.jobpool.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.jobpool.Task;
import com.cninsure.jobpool.dispatch.DispatchTaskService;
import com.cninsure.jobpool.timer.SchedulerService;

import net.sf.json.JSONObject;

@Controller
public class TaskDispatchedController {
		
	@Resource
	private DispatchTaskService dispatchTaskService;
	@Resource
	private  SchedulerService schedulerService;
		
	@RequestMapping(value = "/task/dispatched", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String,Object> dispatched(@RequestBody String json) {
	    Map<String,Object> map=new HashMap<>();
	    try {
			LogUtil.info("task dispatched req=" + json);
	    	Task task = JSON.parseObject(json, Task.class);
	    	int i = dispatchTaskService.taskDispatched(task);
	    	map.put("result", "=i task dispatched=");
	    	map.put("resultCode", i);
	    	map.put("param", json);
	    	LogUtil.info(map.toString());

	    	if(i==1){
	    		map.put("code",1);
		        map.put("msg","success");
	    	}else if(i==2){
	    		map.put("code",2);
		        map.put("msg","needDel");
	    	}else{
	    		map.put("code",0);
		        map.put("msg","failed");
	    	}
	    }
	    catch (Exception ex) {
	    	map.put("error", "修改分配到人任务异常");
	    	LogUtil.error(map.toString(),ex);
	    	ex.printStackTrace();
	    	map.put("code",2);
	    	map.put("msg",json);
	    }
	   return map;
	}
	
	@RequestMapping(value = "/task/recly", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String,Object> recly(@RequestBody String json) {
	    Map<String,Object> map=new HashMap<>();
	    try {
	    	Task task = JSON.parseObject(json, Task.class);
	    	int i = dispatchTaskService.reclyDispatched(task);
	    	map.put("result", "=i task recly req=");
	    	map.put("resultCode", i);
	    	map.put("param", json);
	    	LogUtil.info(map.toString());
	    	if(i==1){
	    		map.put("code",1);
		        map.put("msg","success");
	    	}else{
	    		map.put("code",0);
		        map.put("msg","failed");
	    	}
	    }
	    catch (Exception ex) {
	    	map.put("error", "修改分配到人任务回收异常");
	    	LogUtil.error(map.toString(),ex);
	    	ex.printStackTrace();
	    	map.put("code",-1);
	    	map.put("msg",json);
	    }
	   return map;
	}
	
	@RequestMapping(value = "/task/overtimeRedisTimerDeal", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String,Object> overtimeRedisTimerDeal(@RequestBody String json) {
	    Map<String,Object> map=new HashMap<>();
	    try {
	    	map.put("result", "task overtimeRedisTimerDeal req=");
	    	map.put("param", json);
	    	LogUtil.info(map.toString());
	    	JSONObject jsonObj = JSONObject.fromObject(json);
	    	schedulerService.dealOverTimeJob(jsonObj.optString("keyMessage"));
	    	map.put("code",1);
		    map.put("msg","success");
	    }
	    catch (Exception ex) {
	    	map.put("result", "超时任务通知cm调用处理方法异常");
	    	LogUtil.error(map.toString(),ex);
	    	ex.printStackTrace();
	    	map.put("code",0);
	        map.put("msg","failed");
	    }
	    return map;
	}
}
