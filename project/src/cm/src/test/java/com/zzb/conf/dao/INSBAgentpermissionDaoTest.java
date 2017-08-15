package com.zzb.conf.dao;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.dao.INSCDeptDao;
import com.zzb.conf.entity.INSBAgentpermission;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBAgentpermissionDaoTest {

	@Resource
	private INSBAgentpermissionDao dao;
	@Resource
	private INSBAgentDao aDao;
	@Resource
	private INSCDeptDao deptDao;
	
	/**
	 * 
	 * 批量新增
	 * 
	 * @param list
	 */
	@Test
	public void testInsertInBatch() {
		
		AtomicInteger num = new AtomicInteger(0);
		
		ThreadPoolExecutor exe = new ThreadPoolExecutor(2, 4, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10),new ThreadPoolExecutor.CallerRunsPolicy() );
		
		List<String> deptList = deptDao.selectAllChildren("1237000000");
		List<String> agentIdList =  aDao.selectAgentIdByDeptIds(deptList);
		for(String agentId:agentIdList){
			exe.execute(new Runnable() {
				
				@Override
				public void run() {
					num.addAndGet(1);
					System.out.println(num);
					System.out.println(Thread.currentThread().getName());
					
					INSBAgentpermission model1 = new INSBAgentpermission();
					model1.setCreatetime(new Date());
					model1.setOperator("admin");
					model1.setAgentid(agentId);
					model1.setPermissionid("1");
					model1.setPermissionname("续保");
					model1.setFrontstate(1);
					model1.setFunctionstate(1);
					model1.setAbort(1);
					model1.setId(UUIDUtils.random());
					model1.setCreatetime(new Date());
					
					INSBAgentpermission model2 = new INSBAgentpermission();
					model2.setCreatetime(new Date());
					model2.setOperator("admin");
					model2.setAgentid(agentId);
					model2.setPermissionid("2");
					model2.setPermissionname("核保");
					model2.setFrontstate(1);
					model2.setFunctionstate(1);
					model2.setAbort(1);
					model2.setId(UUIDUtils.random());
					model2.setCreatetime(new Date());
					
					

					INSBAgentpermission model3 = new INSBAgentpermission();
					model3.setCreatetime(new Date());
					model3.setOperator("admin");
					model3.setAgentid(agentId);
					model3.setPermissionid("3");
					model3.setPermissionname("投保");
					model3.setFrontstate(1);
					model3.setFunctionstate(1);
					model3.setAbort(1);
					model3.setId(UUIDUtils.random());
					model3.setCreatetime(new Date());
					
					
					INSBAgentpermission model4 = new INSBAgentpermission();
					model4.setCreatetime(new Date());
					model4.setOperator("admin");
					model4.setAgentid(agentId);
					model4.setPermissionid("4");
					model4.setPermissionname("基础查询");
					model4.setFrontstate(1);
					model4.setFunctionstate(1);
					model4.setAbort(1);
					model4.setId(UUIDUtils.random());
					model4.setCreatetime(new Date());
					
					
					INSBAgentpermission model5 = new INSBAgentpermission();
					model5.setCreatetime(new Date());
					model5.setOperator("admin");
					model5.setAgentid(agentId);
					model5.setPermissionid("5");
					model5.setPermissionname("其他");
					model5.setFrontstate(1);
					model5.setFunctionstate(1);
					model5.setAbort(1);
					model5.setId(UUIDUtils.random());
					model5.setCreatetime(new Date());
					
					
					INSBAgentpermission model6 = new INSBAgentpermission();
					model6.setCreatetime(new Date());
					model6.setOperator("admin");
					model6.setAgentid(agentId);
					model6.setPermissionid("6");
					model6.setPermissionname("支付");
					model6.setFrontstate(1);
					model6.setFunctionstate(1);
					model6.setAbort(1);
					model6.setId(UUIDUtils.random());
					model6.setCreatetime(new Date());
					
					
					INSBAgentpermission model7 = new INSBAgentpermission();
					model7.setCreatetime(new Date());
					model7.setOperator("admin");
					model7.setAgentid(agentId);
					model7.setPermissionid("7");
					model7.setPermissionname("报价");
					model7.setFrontstate(1);
					model7.setFunctionstate(1);
					model7.setAbort(1);
					model7.setId(UUIDUtils.random());
					model7.setCreatetime(new Date());
					
					dao.insert(model1);
					dao.insert(model2);
					dao.insert(model3);
					dao.insert(model4);
					dao.insert(model5);
					dao.insert(model6);
					dao.insert(model7);
				}
			});
			
		}
		
	};

}