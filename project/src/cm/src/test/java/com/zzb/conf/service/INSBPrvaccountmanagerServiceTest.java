package com.zzb.conf.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.entity.INSBPrvaccountkey;
import com.zzb.conf.entity.INSBPrvaccountmanager;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBPrvaccountmanagerServiceTest{
	
	@Resource
	private INSBPrvaccountmanagerService se;
	
	
//	@Test
	public void testSaveOrUpdateKey(){
		
		INSCUser user = new INSCUser();
		user.setUsercode("test");
		
		INSBPrvaccountkey model = new INSBPrvaccountkey();
		model.setParamname("eee");
		model.setParamvalue("fff");
		model.setManagerid("53e253d1414907026d20d7dfe3edc5f6");
		
		String deptId = "1244000000";
//		se.saveOrUpdateKey(user, model, deptId);
	}
	
//	@Test
	public void testSaveOrUpdate(){
		INSCUser user = new INSCUser();
		user.setUsercode("test");
		
		INSBPrvaccountmanager model = new INSBPrvaccountmanager();
		model.setDeptid("1232000001");
		model.setProviderid("2005");
		model.setUsetype("1");
		model.setId(null);
		
		se.saveOrUpdate(user, model);
	}
	
	@Test
	public void testGetKeyDataListPage(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", 10);
		map.put("managerid", "1");
		map.put("offset", 0);
		map.put("order", "asc");
		map.put("deptId", "1244000001");
		map.put("usetype", "1");
		map.put("providerid", "2007");
		
		
		List<INSBPrvaccountkey> list = (List<INSBPrvaccountkey>) se.getKeyDataListPage(map).get("rows");
		for(INSBPrvaccountkey model:list){
			System.out.println(model.getId()+"======"+model.getParamvalue());
		}
	}
	
	
//	@Test
	public void testGetDataListPage(){
		
//		select a.providerid,a.usetype,b.prvshotname prvname,a.id,a.noti from INSBPrvaccountmanager a left join 
//		insbprovider b on a.providerid=b.prvcode GROUP BY a.providerid,a.usetype ORDER BY  a.deptid DESC
	
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", 10);
		map.put("deptid", "1211000000");
		map.put("offset", 0);
		map.put("order", "asc");
		map.put("providerid", null);
		map.put("usetype", null);
		
		System.out.println(se.getDataListPage(map));
	}
	

}