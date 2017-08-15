package com.zzb.conf.controller;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Controller;

//@Controller
//@Aspect
public class LogAop {
//	@Around("execution(* com.*.*.controller.*ller.*(..))")
	public Object test(ProceedingJoinPoint point) throws Throwable{
		  System.out.println("begin around");  
		  Signature signature = point.getSignature();
		  //获取将要执行的方法名称  
	       String methodName = signature.getName();
	       String shortName =signature.toShortString();
	       //获取执行方法的参数  
	       Object[] args = point.getArgs();  
	       //从参数列表中获取参数对象  
	       for(Object obj : args){//查看参数值  
	         System.out.println(shortName+"********"+methodName+"***********"+obj);  
	       }  
		   Object object = point.proceed();  
		   System.out.println("end around");  
		   return object;  
	}
} 
