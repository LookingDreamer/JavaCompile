package com.zzb.cm.controller;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Log4jConfigurer;

import com.cninsure.core.utils.LogUtil;
import com.common.ModelUtil;
import com.mysql.jdbc.log.Log;
import com.zzb.conf.entity.INSBOperatorcomment;
import com.zzb.conf.service.INSBOperatorcommentService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml"})
public class INSBBusinessMyTaskControllerTest {

	 static {  
	        try {  
	            Log4jConfigurer.initLogging("classpath:config/log4j.properties");  
	        } catch (FileNotFoundException ex) {  
	            System.err.println("Cannot Initialize log4j");  
	        }  
	    }  
	@Autowired
	private INSBOperatorcommentService insbOperatorcommentService;
	@Test
	public void test() {
//		Map<String, Object> params = new HashMap<String,Object>();
//		params.put("trackid", null);
//		params.put("trackType", null);
//		params.put("commentcontent", "<");
//		params.put("size", 100);
//		List<INSBOperatorcomment> insbOperatorcomments = insbOperatorcommentService.getList(params);
//		for(INSBOperatorcomment insbOperatorcomment : insbOperatorcomments){
//			System.out.println("原始："+insbOperatorcomment.getCommentcontent());
//			System.out.println("替换:"+ModelUtil.replaceHtml(insbOperatorcomment.getCommentcontent()));
//			
//			System.out.println("-----------------------------------------------------------------------------------------------------");
//		}
		
		List<INSBOperatorcomment> insbOperatorcomments =
				insbOperatorcommentService.getOperatorCommentByMaininstanceid("707944", "20223715");
		for(INSBOperatorcomment insbOperatorcomment : insbOperatorcomments){
			LogUtil.info(insbOperatorcomment.toString());
			//System.out.println(insbOperatorcomment.toString());
		}
	}

}
