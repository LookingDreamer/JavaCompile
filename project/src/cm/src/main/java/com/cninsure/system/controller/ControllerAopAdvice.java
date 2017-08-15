package com.cninsure.system.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.dao.INSCCmLogsDao;
import com.cninsure.system.entity.INSCCmLogs;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.tool.Cm4WorkflowFilter;
import com.zzb.cm.Interface.controller.IpUtil;
import com.zzb.cm.controller.vo.MediumPaymentVo;
import com.zzb.conf.dao.INSBWorkflowmainDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowsub;

@Aspect
@Component
public class ControllerAopAdvice {

	@Resource
	private INSCCmLogsDao cmLogsDao;
	@Resource
	private INSBWorkflowsubDao workflowsubDao;
	@Resource
	private INSBWorkflowmainDao workflowmainDao;
	
//	@Around("execution(* com..controller.*.*(..))")
	public Object aroundMethod(ProceedingJoinPoint  joinpoint) {
		Object obj = null;
		String ip =null;
		INSCUser loginUser = null;
		String className;
		String method=null;
		String loginUserCode=null;
		Date enterMethodDate = new Date();
		Date exitMethodDate =null;
		
		ServletRequestAttributes t = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();  
		HttpServletRequest req = t.getRequest();
		try {
			ip = IpUtil.getIpAddr(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		loginUser =(INSCUser) req.getSession().getAttribute("insc_user");
		if(loginUser!=null){
			loginUserCode = loginUser.getUsercode();
		}
		
		className = joinpoint.getTarget().getClass().getSimpleName();
		method = joinpoint.getSignature().getName();
		List<Object> args = Arrays.asList(joinpoint.getArgs());
		
		try {
			obj = joinpoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
            exitMethodDate = new Date();
        }

		wirteCMLog(ip,loginUserCode,className,method,args.toString(),enterMethodDate,exitMethodDate);
		return obj;
	}
	
	
	
	/**
	 * 记录Controller日志
	 * @return
	 */
//	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private boolean wirteCMLog(String ip,String loginUser,String className,String method,String args,Date enterMethodDate,Date exitMethodDate){
		long enLong = enterMethodDate.getTime();
		long exLong = exitMethodDate.getTime();
		
		INSCCmLogs cmLogModel = new INSCCmLogs();
		cmLogModel.setLoginip(ip);
		cmLogModel.setClassname(className);
		cmLogModel.setMethod(method);
		cmLogModel.setEntertime(enterMethodDate);
		cmLogModel.setExittime(exitMethodDate);
		cmLogModel.setLasttime((int)(exLong-enLong));
		cmLogModel.setOperator(loginUser);
		
//		cmLogsDao.insert(cmLogModel);
		return false;
	}
	
	/**
	 * 所有人工处理结点进行操作人验证
	 */
	@Around("@annotation(cmFilter)")
	public Object beforAdvic4WorkflowHandler(ProceedingJoinPoint  joinpoint,Cm4WorkflowFilter cmFilter){
		
		Object obj = null;
		List<Object> args = Arrays.asList(joinpoint.getArgs());
		
		String userCode="";
		try {
			HttpSession session = (HttpSession) joinpoint.getArgs()[0];
			INSCUser user = (INSCUser) session.getAttribute("insc_user");
			userCode = user.getUsercode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		if("baojia".equals(cmFilter.taskType())){
			return filterBaojia(joinpoint,obj,userCode);
		}
		if("hebao".equals(cmFilter.taskType())){
			return filterHebao(joinpoint,userCode);
		}
		if("chengbao".equals(cmFilter.taskType())){
			return filterChengbao(joinpoint,userCode);
		}
		return obj;
	}
	
	
	/**
	 * 
	 * 报价 权限过滤
	 * 
	 * 
	 * /business/manualprice/quotePricePassOrBackForEdit
	 * 
	 * @param joinpoint
	 * @param obj
	 * @param userCode
	 * @return
	 */
	private Object filterBaojia(ProceedingJoinPoint  joinpoint,Object obj,String userCode){
		
		Map<String, Object> params  = (Map<String, Object>) joinpoint.getArgs()[1];
		if(params==null){
			return commonReturn(joinpoint);
		}
		String instanceId = (String) params.get("instanceId");
		if(instanceId==null){
			return commonReturn(joinpoint);
		}
		INSBWorkflowsub subModel =  workflowsubDao.selectByInstanceId(instanceId);
		if(subModel==null){
			return commonReturn(joinpoint);
		}
		if(subModel.getOperator()==null){
			return commonReturn(joinpoint);
		}
		if(!userCode.equals(subModel.getOperator())){
			Map<String,String>  resultMap = new HashMap<String,String>();
			resultMap.put("status", "userChang");
			resultMap.put("msg", "任务已经被改派");
			String result = JSONObject.fromObject(resultMap).toString();
			return result;
		}else{
			return commonReturn(joinpoint);		
		}
	}
	
	/**
	 * 核保权限过滤
	 * 
	 *  /business/ordermanage/manualUnderWritingSuccess
	 * @param joinpoint
	 * @param obj
	 * @param userCode
	 * @return
	 */
	private Object filterHebao(ProceedingJoinPoint  joinpoint,String userCode){
		MediumPaymentVo mediumPaymentVo = (MediumPaymentVo)joinpoint.getArgs()[1];
		if(mediumPaymentVo==null){
			return commonReturn(joinpoint);
		}
		if(mediumPaymentVo.getTaskid()==null||"".equals(mediumPaymentVo.getTaskid())){
			return commonReturn(joinpoint);
		}
		INSBWorkflowsub subModel =  workflowsubDao.selectByInstanceId(mediumPaymentVo.getProcessinstanceid());
		if(subModel==null){
			return commonReturn(joinpoint);
		}
		if(subModel.getOperator()==null||"".equals(subModel.getOperator())){
			return commonReturn(joinpoint);
		}
		if(!userCode.equals(subModel.getOperator())){
			Map<String,String>  resultMap = new HashMap<String,String>();
			resultMap.put("status", "userChang");
			resultMap.put("msg", "任务已经被改派");
			return "userChang";
		}else{
			return commonReturn(joinpoint);
		}
	}
	/**
	 * 
	 * 
	 * /business/ordermanage/undwrtsuccess
	 * @param joinpoint
	 * @param userCode
	 * @return
	 */
	private Object filterChengbao(ProceedingJoinPoint  joinpoint,String userCode){
		String taskid = (String)joinpoint.getArgs()[1];
		if(taskid==null){
			return commonReturn(joinpoint);
		}
		INSBWorkflowmain workflowmainModel = workflowmainDao.selectByInstanceId(taskid);
		if(workflowmainModel==null){
			return commonReturn(joinpoint);
		}
		if(workflowmainModel.getOperator()==null){
			return commonReturn(joinpoint);
		}
		if(userCode.equals(workflowmainModel.getOperator())){
			return commonReturn(joinpoint);
		}else{
			Map<String,String>  resultMap = new HashMap<String,String>();
			resultMap.put("status", "userChang");
			resultMap.put("msg", "已经被改派");
			return "userChang";
		}
	}
	
	/**
	 * 环绕通知 通用返回
	 * 
	 * @param joinpoint
	 * @return
	 */
	private Object commonReturn(ProceedingJoinPoint  joinpoint){
		Object obj = null;
		try {
			obj =  joinpoint.proceed();
		} catch (Throwable e) {
			LogUtil.warn("切面异常---参数错误");
		}
		return obj;
	}
}
