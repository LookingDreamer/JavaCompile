package com.cninsure.jobpool;


import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.dispatch.DispatchTaskService;
import com.cninsure.system.dao.INSCUserDao;
import com.cninsure.system.entity.INSCUser;
import com.common.RedisException;
import com.common.WorkFlowException;
import com.zzb.conf.dao.INSBBusinessmanagegroupDao;
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class DispatchServiceTest {

	@Resource
	private DispatchService service;
	@Resource
	private INSBBusinessmanagegroupDao bDao;
	@Resource
	private INSCUserDao userDao;
	@Resource
	private DispatchTaskService taskservice;
	
	
	@Test
	public void testCompletedAllTaskByCore(){
		service.completedAllTaskByCore("42464", "42467");
	}
	
	
	@Test
	public void testDispathAll(){
		Task task = new Task();
		task.setTaskName("人工报价");
		task.setDispatchflag(true);
		task.setProInstanceId("41602");
		task.setSonProInstanceId("");
		task.setPrvcode("200737");
		service.dispatchAll(task);
	}
	
	@Test
	public void testRestartTask() throws WorkFlowException{
//		service.dispatchAll();
//		String decodePwd = StringUtil.md5Base64FromHex("4QrcOUm6Wau+VuBX8g+IPg==");
		String aaa = StringUtil.md5Base64("123456");
		System.out.println(aaa);
//		System.out.println(decodePwd);
	}
	
	@Test
	public void testGetTask() throws WorkFlowException, RedisException{
		INSCUser user = userDao.selectByUserCode("baihong01");
		INSCUser toUser = userDao.selectByUserCode("wanglin");
		service.getTask("1281", "100613", 2, user, toUser);
	}
	
	@Test
	public void testRefuse() throws WorkFlowException, RedisException{
		INSCUser user = userDao.selectByUserCode("baihong01");
		service.refuseTask("268", "100613", 2, user);
	}
	
	@Test
	public void testLogin(){
		taskservice.userLoginForTask("JH");
	}
	
	@Test
	public void testLogoff(){
		taskservice.userLogoutForTask("JH");
	}
	
	/**
	 * 改派
	 * @throws WorkFlowException 
	 * @throws RedisException 
	 */
	@Test
	public void testReassignment() throws WorkFlowException, RedisException{
		INSCUser fromUser = userDao.selectByUserCode("fengjl");
		INSCUser toUser = userDao.selectByUserCode("wanglin");
		
		service.reassignment("2055", "200211", 1, fromUser, toUser);
	}
	
	/**
	 * 暂停任务
	 * @throws WorkFlowException 
	 * @throws RedisException 
	 */
	@Test 
	public void testPauseTask() throws WorkFlowException, RedisException{
//		service.pauseTask("524", "100613", 1);
	}
	
	
	@Test
	public void testDispatchAll(){
		
//		INSBBusinessmanagegroup group = bDao.selectById("524b85cae89f326bfd16ab22b3784bc8");
//		
//		Task task = new Task();
//		task.setCreatetime(ModelUtil.sdf.format(new  Date()));
//		task.setDispatchflag(true);
//		task.setGroup(group);
//		task.setSonProInstanceId("1");
//		Pool.addOrUpdate(task); 		
//		
//		try {
//			service.dispatchAll();
//		} catch (WorkFlowException e) {
//			e.printStackTrace();
//		}
//		List<Task> result = Pool.getAll();
//		System.out.println(result);
//		
//		System.out.println(task.toString());
		
		
//		try {
//			service.dispatchAll();
//		} catch (WorkFlowException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
