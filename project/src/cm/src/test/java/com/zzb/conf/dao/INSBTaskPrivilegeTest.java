package com.zzb.conf.dao;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zzb.conf.entity.INSBTaskprivilege;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBTaskPrivilegeTest {
	@Resource
	private INSBTaskprivilegeDao dao;

	@Test
	public void testInsert() {
		INSBTaskprivilege c = new INSBTaskprivilege();
		c.setCreatetime(new Date());
		c.setOperator("1");
		c.setCode("1010000");
		c.setPcode("1000000");
		c.setName("代理人及设备管理后台系统");
		c.setLevel(1);
		c.setorderflag(1);
		dao.insert(c);

		INSBTaskprivilege c1 = new INSBTaskprivilege();
		c1.setCreatetime(new Date());
		c1.setOperator("1");
		c1.setCode("1010100");
		c1.setPcode("1010000");
		c1.setName("掌中保管理");
		c1.setLevel(2);
		c1.setorderflag(1);
		dao.insert(c1);
		for (int i = 1; i <= 14; i++) {
			INSBTaskprivilege c11 = new INSBTaskprivilege();
			c11.setCreatetime(new Date());
			c11.setOperator("1");
			if(i<10){
				c11.setCode("101010" + i);
			}else{
				c11.setCode("10101" + i);
			}
			
			c11.setPcode("1010100");
			switch (i) {

			case 1:
				c11.setName("查看");
				break;
			case 2:
				c11.setName("设备导出数据");
				break;
			case 3:
				c11.setName("设备解绑");
				break;
			case 4:
				c11.setName("变更授权状态");
				break;
			case 5:
				c11.setName("更换机器");
				break;
			case 6:
				c11.setName("设备绑定");
				break;
			case 7:
				c11.setName("设备导入");
				break;
			case 8:
				c11.setName("激活码导出");
				break;
			case 9:
				c11.setName("激活码删除");
				break;
			case 10:
				c11.setName("重置激活码");
				break;
			case 11:
				c11.setName("激活码导入");
				break;
			case 12:
				c11.setName("设置所有账号登录");
				break;
			case 13:
				c11.setName("设备删除");
				break;
			case 14:
				c11.setName("设备批量移动");
				break;
			}
			c11.setLevel(3);
			c11.setorderflag(i);
			dao.insert(c11);

		}

		INSBTaskprivilege c2 = new INSBTaskprivilege();
		c2.setCreatetime(new Date());
		c2.setOperator("1");
		c2.setCode("1010200");
		c2.setPcode("1010000");
		c2.setName("业管人员管理");
		c2.setLevel(2);
		c2.setorderflag(1);
		dao.insert(c2);
		
		INSBTaskprivilege c21 = new INSBTaskprivilege();
		c21.setCreatetime(new Date());
		c21.setOperator("1");
		c21.setCode("1010201");
		c21.setPcode("1010200");
		c21.setName("查看");
		c21.setLevel(3);
		c21.setorderflag(1);
		dao.insert(c21);

		INSBTaskprivilege c3 = new INSBTaskprivilege();
		c3.setCreatetime(new Date());
		c3.setOperator("1");
		c3.setCode("1010300");
		c3.setPcode("1010000");
		c3.setName("代理人管理");
		c3.setLevel(2);
		c3.setorderflag(1);
		dao.insert(c3);
		
		for(int i=1;i<=13;i++){
			INSBTaskprivilege c31 = new INSBTaskprivilege();
			c31.setCreatetime(new Date());
			c31.setOperator("1");
			if(i<10){
				c31.setCode("101030" + i);
			}else{
				c31.setCode("10103" + i);
			}
			c31.setPcode("1010300");
			
			switch (i){
			
			case 1:
				c31.setName("查看");
				break;
			case 2:
				c31.setName("基本");
				break;
			case 3:
				c31.setName("导出代理人数据");
				break;
			case 4:
				c31.setName("导出机器绑定数据");
				break;
			case 5:
				c31.setName("导出功能配置");
				break;
			case 6:
				c31.setName("绑定或解绑");
				break;
			case 7:
				c31.setName("设置终端登录");
				break;
			case 8:
				c31.setName("设置网站登录");
				break;
			case 9:
				c31.setName("设置移动登录");
				break;
			case 10:
				c31.setName("编辑功能配置");
				break;
			case 11:
				c31.setName("创建限定供应商");
				break;
			case 12:
				c31.setName("删除限定供应商");
				break;
			case 13:
				c31.setName("修改代理人VIP等级");
				break;
			}
			
			c31.setLevel(3);
			c31.setorderflag(i);
			dao.insert(c31);
		}
	}
}
