package com.zzb.conf.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.core.utils.UUIDUtils;
import com.zzb.conf.entity.INSBAutoconfig;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBAutoconfigDaoTest{
	
	@Resource
	private INSBAutoconfigDao dao;
	
	@Test
	public void testInsert(){
		List<INSBAutoconfig> list = new ArrayList<INSBAutoconfig>();
		
		INSBAutoconfig m1 = new INSBAutoconfig();
		m1.setDeptid("1244191004");
		m1.setProviderid("201637");
		m1.setContentid("fa5ffd7d7d121c87e4121b00e04718f6");
		m1.setQuotetype("01");
		m1.setId(UUIDUtils.random());
		m1.setOperator("admin");
		m1.setConftype("01");
		m1.setCreatetime(new Date());
		
		INSBAutoconfig m2 = new INSBAutoconfig();
		m2.setDeptid("1237191002");
		m2.setProviderid("201637");
		m2.setContentid("fa5ffd7d7d121c87e4121b00e04718f6");
		m2.setQuotetype("01");
		m2.setId(UUIDUtils.random());
		m2.setOperator("admin");
		m2.setConftype("01");
		m2.setCreatetime(new Date());
		
		
		INSBAutoconfig m3 = new INSBAutoconfig();
		m3.setDeptid("1237191007");
		m3.setProviderid("201637");
		m3.setContentid("fa5ffd7d7d121c87e4121b00e04718f6");
		m3.setQuotetype("01");
		m3.setId(UUIDUtils.random());
		m3.setOperator("admin");
		m3.setConftype("01");
		m3.setCreatetime(new Date());
		
		INSBAutoconfig m4 = new INSBAutoconfig();
		m4.setDeptid("1237191045");
		m4.setProviderid("201637");
		m4.setContentid("fa5ffd7d7d121c87e4121b00e04718f6");
		m4.setQuotetype("01");
		m4.setId(UUIDUtils.random());
		m4.setOperator("admin");
		m4.setConftype("01");
		m4.setCreatetime(new Date());
		
		INSBAutoconfig m5 = new INSBAutoconfig();
		m5.setDeptid("1237191027");
		m5.setProviderid("201637");
		m5.setContentid("fa5ffd7d7d121c87e4121b00e04718f6");
		m5.setQuotetype("01");
		m5.setId(UUIDUtils.random());
		m5.setOperator("admin");
		m5.setConftype("01");
		m5.setCreatetime(new Date());
		
		INSBAutoconfig m6 = new INSBAutoconfig();
		m6.setDeptid("1237191033");
		m6.setProviderid("201637");
		m6.setContentid("fa5ffd7d7d121c87e4121b00e04718f6");
		m6.setQuotetype("01");
		m6.setId(UUIDUtils.random());
		m6.setOperator("admin");
		m6.setConftype("01");
		m6.setCreatetime(new Date());
		
		INSBAutoconfig m7 = new INSBAutoconfig();
		m7.setDeptid("1237191040");
		m7.setProviderid("201637");
		m7.setContentid("fa5ffd7d7d121c87e4121b00e04718f6");
		m7.setQuotetype("01");
		m7.setId(UUIDUtils.random());
		m7.setOperator("admin");
		m7.setConftype("01");
		m7.setCreatetime(new Date());
		
		
		list.add(m1);
		list.add(m2);
		list.add(m3);
		list.add(m4);
		list.add(m5);
		list.add(m6);
		list.add(m7);
		
		dao.insertInBatch(list);
	}
}