package com.zzb.cif;

import com.common.redis.CMRedisClient;
import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.jobpool.DispatchService;
import com.zzb.app.service.AppSupplementItemService;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.cm.controller.CifController;
import com.zzb.cm.controller.vo.FindLastClaimBackInfoVo;
import com.zzb.conf.dao.INSBAutoconfigDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.dao.OFUserDao;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBRiskimg;
import com.zzb.conf.service.INSBRiskimgService;
import com.zzb.conf.service.INSBRiskkindService;
import com.zzb.conf.service.INSBTasksetService;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.mobile.controller.AppMyOrderInfoController;
import com.zzb.mobile.dao.AppInsuredQuoteDao;
import com.zzb.mobile.model.SearchProviderModel;
import com.zzb.mobile.model.policyoperat.PolicyCommitParam;
import com.zzb.mobile.service.AppInsuredQuoteService;
import com.zzb.mobile.service.impl.AppPaymentServiceImpl;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class CifContorlTest {			
	@Resource
	CifController cifController;
	@Resource
	INSBAutoconfigDao autoconfigDao ;
	@Resource
	AppInsuredQuoteDao inquDao;
	@Resource
	AppInsuredQuoteService service;
	@Resource
	INSBTasksetService insbTasksetService;
	@Resource
	INSBRiskkindService insbRiskkindService;
	@Resource
	INSBRiskimgService insbRiskimgService;
	@Resource
	DispatchService ds;
	@Resource
	INSBWorkflowmainService ws;
	@Resource
	private INSBProviderDao insbProviderDao;
	@Resource
	AppSupplementItemService apps;
	@Resource
	AppMyOrderInfoController orc ;
	@Resource
	private InterFaceService facdService;
	@Resource
	private OFUserDao udao;
	@Resource
	private INSBWorkflowsubDao sudao;
	@Resource
	private  AppPaymentServiceImpl payser;
	
//	@Test
	public void test1(){
		
//		OFUser user = new OFUser();
//		user.setUsername("huangxx1234512");
//		user.setPlainPassword("1");
//		user.setName("2");
//		user.setCreationDate(1L);
//		user.setModificationDate(1L);
//		udao.updateByUserName(user);
//		Map<String, String> agreeMap = new HashMap<String, String>();
//		agreeMap.put("province","370000");
//		agreeMap.put("city", "370100" );
//		List<Map<String, String>> asf = inquDao.getAgreementProByCity(agreeMap);
//		System.out.println(asf);
//		tes();
//		String userid = insbTasksetService.getRenewalUserData("201137");
//		System.out.println(userid);
//		INSBRiskkind riskkind =new INSBRiskkind();
//		riskkind.setRiskid("3fb67bf08ad111e5e1bc674038377d3e");
//		System.out.println(insbRiskkindService.queryListByVo(riskkind));
//		tes();
//		ds.dispatchAll();
//		 f();
		INSBProvider insbProvider = insbProviderDao.selectFatherProvider("200737");
		System.out.println(insbProvider);
//		policsubmit();
//		findLastClaimBackInfo();
//		t2(); 
		
//		System.out.println(sudao.selectSubInsIdExc("38556"));
	}
	
	
	
	public void t2(){
//		facdService.getPacket("8253", "206637","");
	}
	
	public void f(){
		System.out.println(ws.getContracthbType("851", "2005", "", "contract"));
		System.out.println(ws.getQuoteType("851", "2005", "1"));

	}
	
	public  void t(){
		INSBRiskimg riskimg = new INSBRiskimg();
		riskimg.setRiskid("3fb67bf08ad111e5e1bc674038377d3e");
		System.out.println(insbRiskimgService.queryListVo(riskimg));
	}
	
	/**
	 * 平台查询
	 */
//	@Test
	public void findLastClaimBackInfo(){
		FindLastClaimBackInfoVo vo = new FindLastClaimBackInfoVo();
		vo.setJobnum("620428021");
		vo.setTaskId("36996");
		cifController.findLastClaimBackInfo(vo);
	}
	
	
	public static void main(String[] args) throws Exception {
//		Map<String,Map<String,String>> result = new HashMap<String,Map<String,String>>();
//		List<String> onlineUserCodes = new ArrayList<String>();
//		String onlineUserStr = (String) com.common.RedisClient.get("onlineUsers");
//		Map<String, Object> tempMap = JSONObject.fromObject(onlineUserStr);
//		System.out.println(tempMap);
//		for (String usercode : tempMap.keySet()) {
//			Map<String, String> userData = JSONObject.fromObject(tempMap.get(usercode));
//			result.put(usercode, userData);
//		}
//		Task task =Pool.get("851", "855", "2005");
//		System.out.println(task);
//		RedisClient.setList("onlineusers", new ArrayList<Task>());
//		System.out.println(Pool.getAll());
//		RedisClient.del("onlineusers");
//		RedisClient.del("TaskPool");
//		JSONObject json =new JSONObject();
//		json.accumulate("id", "121");
//		INSCUser user =(INSCUser)JSONObject.toBean(json, INSCUser.class);
		
//		System.out.println(RedisClient.get("TaskPool"));
		CMRedisClient.getInstance().set("cm:test", "1", 1);
//		java.util.List<Task> tasks	=Pool.getAll();
//		for (Task task : tasks) {
//			System.out.println(task);
//		}
		
//		System.out.println(RedisClient.get("onlineUsers"));
		
	}
	/**
	 * 保险公司
	 */
	public void tes(){
		SearchProviderModel model=new SearchProviderModel();
		model.setAgentid("00023bf053c411e5d12d53043cee09e7");
		model.setChannel("");
		model.setCity("370100");
		model.setProvince("370000");
		service.searchProvider(model);
//		service.recommendProvider(model);
	}
	/**
	 * 投保
	 */
	public void policsubmit(){
		PolicyCommitParam p = new PolicyCommitParam();
		p.setAgentnum("620304333");
		p.setInscomcode("2016");
//		p.setNotice("");
		p.setProcessInstanceId("270");
		p.setTotalpaymentamount(0);
		p.setTotalproductamount(0);
		orc.policySubmit(p);
	}
}
