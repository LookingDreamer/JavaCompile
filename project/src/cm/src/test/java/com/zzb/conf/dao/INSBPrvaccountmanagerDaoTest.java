package com.zzb.conf.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.core.utils.BeanUtils;
import com.zzb.conf.entity.INSBPrvaccountmanager;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBPrvaccountmanagerDaoTest {
	
	@Resource
	private INSBPrvaccountmanagerDao dao;
	
	
	@Test
	public void testSelectListPageNew(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<String> deptList = new ArrayList<String>();
		deptList.add("1292370001");
		deptList.add("1237000000");
		map.put("deptIds", deptList);
		map.put("usetype", "1");
		
		map.put("limit", 10);
		map.put("offset", 0);
		map.put("order", "asc");
		
		
		List<INSBPrvaccountmanager> list = dao.selectListPageNew(map);
		System.out.println(list);
	}
	
	@Test
	public void testbatchInsert(){
		INSBPrvaccountmanager model = new INSBPrvaccountmanager();
		model.setCreatetime(new Date());
		model.setOperator("admin");
		model.setDeptid("1237000000");
		model.setAccount("ex_wangjing003");
		model.setOrg("37981500");
		model.setPermission("2");
		model.setPrvname("商河支公司");
		model.setNoti("山东中华联合");
		
		dao.insert(model);
		
	}
	
	
	
	@Test
	public void  testSelectListPage(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", 10);
//		map.put("account", 1);
		map.put("org", "123");
		map.put("offset", 0);
		map.put("order", "asc");
		
		
		List<INSBPrvaccountmanager>  result = dao.selectListPage(map);
		System.out.println(result);
	}
	
	
	@Test
	public void  testExtendsPage(){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("deptid", "1221000001");
		map.put("providerid", null);
		map.put("usetype", null);
		map.put("offset",0);
		map.put("limit", 10);
		
		List<INSBPrvaccountmanager> aa = dao.extendsPage(map);
		System.out.println(aa.get(0).getNoti());
	}

}