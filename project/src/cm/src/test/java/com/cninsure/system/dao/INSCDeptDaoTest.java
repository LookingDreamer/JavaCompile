package com.cninsure.system.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.system.entity.INSCDept;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSCDeptDaoTest {

	@Resource
	private INSCDeptDao dao;

	
	@Test
	public void testSelectComCodeByComtypeAndParentCodes4EDI(){
		List<String> result = dao.selectComCodeByComtypeAndParentCodes4EDI("1211000000");
		System.out.println(result);
	}
	
	@Test
	public void testSelectAllChildren(){
		List<String> result = dao.selectAllChildren("1200000000");
		System.out.println(result);
	}
	
	@Test
	public  void initDeptParentCodes(){
		
		List<INSCDept> result = new ArrayList<INSCDept>();
		
//		List<INSCDept> list = dao.selectAll();
//		for(INSCDept model:list){
//			for(INSCDept m1:list){
//				if(m1.getParentcodes()==null){
//					break;
//				}
//				if(model.getUpcomcode().equals(m1.getComcode())){
//					model.setParentcodes(m1.getParentcodes()+"+"+model.getUpcomcode());
//					result.add(model);
//				}
//				
//			}
//		}
		dao.insertInBatch(result);
	}
	
	@Test
	public void testSeleDeptIdByInnerCode(){
		String actual = dao.seleDeptIdByInnerCode("02008005");
		Assert.assertEquals("121100001", actual);
	}
	
}