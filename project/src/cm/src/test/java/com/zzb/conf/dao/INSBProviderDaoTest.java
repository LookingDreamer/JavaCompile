package com.zzb.conf.dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zzb.conf.entity.INSBProvider;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBProviderDaoTest  {
	
	@Resource
	private INSBProviderDao dao;
	
	@Test
	public void test(){
		System.out.println(getProvId("","207737"));
	}
	public String getProvId(String result,String id){
		
		INSBProvider prvModel = dao.selectById(id);
		
		if(prvModel.getPrvgrade()==null){
			return null;
		}
		if(prvModel.getPrvgrade().equals("01")){
			System.out.println(prvModel.getId());
			result = prvModel.getId();
		}else{
			getProvId(result,prvModel.getParentcode());
		}
		return result;
	}
	
}