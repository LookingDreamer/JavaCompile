
package com.zzb.conf.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zzb.conf.entity.INSBAutoconfigshow;
import com.zzb.conf.entity.INSBAutoconfigshowQueryModel;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBAutoconfigshowServiceTest{
	
	
	@Resource
	private INSBAutoconfigshowService service;
	
	@Test
	public void testAutoOrArtificial(){
		List<String> quoteList = new ArrayList<String>(); 
		quoteList.add("01");
		quoteList.add("02");
		
		INSBAutoconfigshowQueryModel queryModel = new INSBAutoconfigshowQueryModel();
		queryModel.setDeptId("1211191001");
		queryModel.setProviderid("200537");
		queryModel.setQuoteList(quoteList);
		
		 List<INSBAutoconfigshow> list  = service.autoOrArtificial(queryModel);
		 System.out.println(list);
		
	}
	
	
	
	@Test
	public void testGetElfEdiByNearestDept(){
		List<String> quoteList = new ArrayList<String>(); 
		quoteList.add("01");
		
		INSBAutoconfigshowQueryModel queryModel = new INSBAutoconfigshowQueryModel();
		queryModel.setDeptId("1237185031");
		queryModel.setProviderid("204337");
		queryModel.setQuoteList(quoteList);
		queryModel.setConftype("01");
		
		
		Map<String,Object> result = service.getElfEdiByNearestDept(queryModel);
		System.out.println(result);
	}
	
	@Test
	public void testQueryByProId(){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("providerid", "200537");
		map.put("conftype", "01");
		
		List<String> tempDeptIds = service.getParentDeptIds4Show("1211191001");
		map.put("deptList", tempDeptIds);
		
		List<String> list = service.queryByProId(map);
		
		
		System.out.println(list);
		
	}
}
