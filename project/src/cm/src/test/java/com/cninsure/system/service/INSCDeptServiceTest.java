package com.cninsure.system.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.StringUtil;
import com.cninsure.quartz.test.SimpleService;
import com.common.JsonUtil;
import com.zzb.app.service.QuotewayService;
import com.zzb.conf.service.INSBPermissionsetService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.InsuredOneConfigModel;
import com.zzb.mobile.model.WorkflowStartQuoteModel;
import com.zzb.mobile.service.AppInsuredQuoteCopyService;
import com.zzb.mobile.service.AppInsuredQuoteService;
import com.zzb.mobile.service.INSBPayChannelService;
import com.zzb.mobile.service.impl.AppInsuredQuoteServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSCDeptServiceTest {

	@Resource
	private INSCDeptService service;

	@Resource
	private QuotewayService quotewayService;

	@Resource
	private SimpleService simpleService;

	@Resource
	private AppInsuredQuoteService appInsuredQuoteService;

	@Resource
	private INSBPermissionsetService permissionsetService;

	@Test
	public void testQueryListByPcode4Group(){
		List<Map<String, String>> deptList = service.queryListByPcode4Group("1137195000");
		System.out.println(deptList);
	}

	@Test
	public void testSelectPermissionBySetId(){
		String str = quotewayService.getpermissionsadd("2032",false);
		System.out.println(str);
	}

	@Test
	public void testExpiredClose(){
		simpleService.expiredClose();
	}

	@Test
	public void testWorkflowStartQuote(){
		WorkflowStartQuoteModel model = new WorkflowStartQuoteModel();
		model.setProcessinstanceid("52892");
		appInsuredQuoteService.workflowStartQuote(model);

	}

	@Test
	public void testTestAgentPermission() {
		permissionsetService.initTestAgentPermission("","30000116");
	}
	@Resource
	private AppInsuredQuoteCopyService insuredQuoteCopyService;
	@Test
	public void test5() throws Exception {

		CommonModel cm =  insuredQuoteCopyService.copy("669186","208544");
		System.out.println(JsonUtil.serialize(cm));

	}
}