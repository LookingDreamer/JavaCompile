package com.zzb.extra.service.impl;
//
//import java.text.ParsePosition;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by Heweicheng on 2016/7/11.
// */
//import javax.annotation.Resource;
//
//import org.apache.poi2.hssf.record.formula.functions.Pearson;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import com.cninsure.core.utils.LogUtil;
//import com.cninsure.core.utils.UUIDUtils;
//import com.cninsure.system.entity.INSCCode;
//import com.cninsure.system.service.INSCCodeService;
//import com.common.ConfigUtil;
//import com.common.HttpSender;
//import com.common.ModelUtil;
//import com.zzb.chn.bean.PersonBean;
//import com.zzb.chn.bean.QuoteBean;
//import com.zzb.chn.service.CHNChannelQuoteService;
//import com.zzb.chn.service.CHNChannelService;
//import com.zzb.chn.service.CHNCommissionRateService;
//import com.zzb.chn.service.impl.CHNChannelQuoteServiceImpl;
//import com.zzb.chn.util.JsonUtils;
//import com.zzb.conf.entity.INSBOrderpayment;
//import com.zzb.conf.service.INSBOrderpaymentService;
//import com.zzb.extra.dao.INSBAccountDetailsDao;
//import com.zzb.extra.dao.INSBAccountWithdrawDao;
//import com.zzb.extra.dao.INSBMiniDateDao;
//import com.zzb.extra.dao.INSBMiniPermissionDao;
//import com.zzb.extra.dao.INSBMiniRoleDao;
//import com.zzb.extra.dao.INSBMiniUserRoleDao;
//import com.zzb.extra.dao.INSBRolePermissionDao;
//import com.zzb.extra.entity.INSBAgentPrize;
//import com.zzb.extra.entity.INSBAgentTask;
//import com.zzb.extra.entity.INSBAgentUser;
//import com.zzb.extra.entity.INSBMiniPermission;
//import com.zzb.extra.entity.INSBMiniRole;
//import com.zzb.extra.entity.INSBMiniUserRole;
//import com.zzb.extra.entity.INSBRolePermission;
//import com.zzb.extra.model.AgentTaskModel;
//import com.zzb.extra.model.MiniOrderListMappingModel;
//import com.zzb.extra.service.INSBAccountDetailsService;
//import com.zzb.extra.service.INSBAgentPrizeService;
//import com.zzb.extra.service.INSBAgentTaskService;
//import com.zzb.extra.service.INSBAgentUserService;
//import com.zzb.extra.service.INSBConditionsService;
//import com.zzb.extra.service.INSBMiniDateService;
//import com.zzb.extra.service.INSBMiniOrderTraceService;
//import com.zzb.extra.service.INSBMiniUserRoleService;
//import com.zzb.extra.service.WxMsgTemplateService;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration(value = "src/main/webapp")
//@ContextConfiguration(locations = { "classpath:config/spring-config.xml", "classpath:config/spring-security-config.xml",
//		"classpath:config/spring-mvc-config.xml",
//		"classpath:config/spring-config-db.xml", })
public class WxTemplateServiceTest {
//
//	@Resource
//	CHNChannelQuoteService charQueueService;
//
//	@Resource
//	WxMsgTemplateService wxMsgTemplateService;
//
//	@Resource
//	INSBAccountDetailsService insbAccountDetailsService;
//
//	@Resource
//	INSBAgentTaskService insbAgentTaskService;
//
//	@Resource
//	CHNChannelService chnChannelService;
//
//	@Resource
//	INSBOrderpaymentService insbOrderpaymentService;
//
//	@Resource
//	CHNCommissionRateService chnCommissionRateService;
//
//	@Resource
//	private INSBAccountDetailsDao insbAccountDetailsDao;
//
//	@Resource
//	private INSBAccountWithdrawDao insbAccountWithdrawDao;
//
//	@Resource
//	private INSBConditionsService insbConditionsService;
//
//	@Resource
//	private INSCCodeService inscCodeService;
//
//	@Resource
//	private INSBMiniDateDao insbMiniDateDao;
//
//	@Resource
//	private INSBMiniOrderTraceService insbMiniOrderTraceService;
//
//	@Resource
//	private INSBAgentPrizeService insbAgentPrizeService;
//
//	@Resource
//	private INSBMiniDateService insbDate;
//
//	@Resource
//	private INSBMiniRoleDao insbMiniRoleDao;
//
//	@Resource
//	private INSBMiniUserRoleDao insbMiniUserRoleDao;
//
//	@Resource
//	private INSBMiniUserRoleService inssss;
//
//	@Resource
//	private INSBMiniPermissionDao insbMiniPermissionDao;
//
//	@Resource
//	private INSBRolePermissionDao insbRolePermissionDao;
//
//	@Resource
//	private INSBAgentUserService insbAgentUserService;
//
//	private static Map<String, String> httpHead = new HashMap<>();
//
//	static {
//		httpHead.put("innerPipe", "zheshiyigemimi!");
//		httpHead.put("Content-Type", "application/json;charset=utf-8");
//	}
//
//	@Test
//	public void queryAgentName() {
//		String name = insbAccountWithdrawDao.queryAgentNameByAccountid("246f9f3c35c4370adb74230361c11a66");
//		System.out.println(name);
//	}
//
//	@Test
//	public void updateOrderState(){
//
//		insbMiniOrderTraceService.updateOrderTraceState("1835105","20434419","12");
//	}
//	@Test
//	public void updateStatusToZero(){
//		updateStatusToFour("1265027", "20194419");
//	}
//
//	private void updateStatusToZero(String taskid,String providercode){
//		try{
//			LogUtil.info("updateStatusToZero called:"+taskid+" "+providercode);
//			INSBAgentPrize insbAgentPrize = new INSBAgentPrize();
//			insbAgentPrize.setTaskid(taskid);
//			insbAgentPrize.setProvidercode(providercode);
//			insbAgentPrize.setStatus("4");//已支付待发放
//			insbAgentPrize = insbAgentPrizeService.queryOne(insbAgentPrize);
//			if(null != insbAgentPrize && taskid.equals(insbAgentPrize.getTaskid())){
//				insbAgentPrize.setStatus("0");//改成未使用
//				insbAgentPrize.setTaskid("");
//				insbAgentPrize.setProvidercode("");
//				insbAgentPrizeService.updateById(insbAgentPrize);
//			}
//		}catch (Exception e){
//			LogUtil.info("updateStatusToZero:"+e.getMessage());
//		}
//	}
//
//	private void updateStatusToFour(String taskid,String providercode){
//		try{
//			LogUtil.info("updateStatusToFour called:"+taskid+" "+providercode);
//			INSBAgentPrize insbAgentPrize = new INSBAgentPrize();
//			insbAgentPrize.setTaskid(taskid);
//			insbAgentPrize.setProvidercode(providercode);
//			insbAgentPrize.setStatus("3");//待发放
//			insbAgentPrize = insbAgentPrizeService.queryOne(insbAgentPrize);
//			if(null != insbAgentPrize && taskid.equals(insbAgentPrize.getTaskid())){
//				insbAgentPrize.setStatus("4");//支付完成改成 已支付待发放
//				insbAgentPrizeService.updateById(insbAgentPrize);
//			}
//		}catch (Exception e){
//			LogUtil.info("updateStatusToFour:"+e.getMessage());
//		}
//	}
//
//	@Test
//	public void queryParams() {
//		AgentTaskModel agentTaskModel = new AgentTaskModel();
//		agentTaskModel.setAgentid("71a35090a6bb9dc5074273ef67ac1df0");
//		agentTaskModel.setTaskid("1840372");
//		agentTaskModel.setProvidercode("20194419");
//		insbConditionsService.queryParams(agentTaskModel);
//		String json = JsonUtils.serialize(insbConditionsService.queryParams(agentTaskModel));
//		System.out.println(json);
//
//	}
//
//	@Test
//	public void genCommission() {
//		AgentTaskModel agentTaskModel = new AgentTaskModel();
//		agentTaskModel.setChannelid("qd_newssj");
//		agentTaskModel.setTaskid("1882168");
//		agentTaskModel.setProvidercode("20214401");
//		agentTaskModel.setCommissionFlag("quote");
//		String json =  chnCommissionRateService.queryTaskCommission(agentTaskModel);
//		System.out.println(json );
//
//		Map<String,Object> res = JsonUtils.deserialize(json,Map.class);
//		Map<String,Object> result = (Map)res.get("result");
//		List<Map<String,Object>> rows = (List<Map<String,Object>>)result.get("rows");
//		for(Map<String,Object> row:rows){
//			System.out.println("====================");
//			System.out.println(row.get("taskid").toString());
//			System.out.println(row.get("commissionrateid").toString());
//			System.out.println(row.get("counts").toString());
//			System.out.println(row.get("channelId").toString());
//		}
//
//
//	}
//
//	@Test
//	public void queryCommissionInfo() {
//		Map<String,Object > params = new HashMap<>();
//		params.put("page",1);
//		params.put("pageSize",10);
//		//params.put("taskid","1265027");
//		params.put("providerid","20194419");
//		params.put("channelid","nqd_minizzb2016");
//		//params.put("commissionflag","quote");
//		//params.put("deptid5","1244200000");
//		params.put("commissiontype","01");
//		String json =  chnCommissionRateService.queryCommissionRateRule(params);
//		System.out.println(json);
//
//	}
//	@Test
//	public void queryPlatInfo() {
//		Map<String,Object > params = new HashMap<>();
//		params.put("prvId","");
//		params.put("taskId","1874092");
//		String taskId="1273883";
//		String prvId = "";
//		String json =  chnChannelService.queryPlatInfo(taskId,prvId,"nqd_minizzb2016");
//		System.out.println(json);
//
//	}
//
//	@Test
//	public void testDay(){
//		System.out.println("=================================");
//		System.out.println(this.getLastDay(new Date()));
//	}
//
//	private Date getLastDay(Date day){
//		//生效时间默认为下一天0点
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(day);   //设置当前日期
//		calendar.add(Calendar.DATE, -1); //日期加1天
//		Date time =calendar.getTime();
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String dateString = formatter.format(time);
//		dateString +=" 23:59:59";
//		ParsePosition parsePosition = new ParsePosition(0);
//		Date time2 = formatter2.parse(dateString,parsePosition);
//		return time2;
//	}
//
//	@Test
//	public void queryCommissionRateRule() {
//		Map<String,Object > params = new HashMap<>();
//		params.put("page",1);
//		params.put("pageSize",10);
//		params.put("deptid5","1244194001");
//		params.put("providerid","20194419");
//		params.put("channelid","nqd_minizzb2016");
//		//params.put("commissionflag","quote");
//		params.put("commissiontype","03");
//		String json =  chnCommissionRateService.queryCommissionRateRule(params);
//		System.out.println(json);
//
//	}
//
//	@Test
//	public void refundCompleteTask() throws Exception {
//		String result;
//		result = wxMsgTemplateService.refundCompleteTask("1835027", "20214419", "nqd_minizzb2016");
//		Thread.sleep(100000);
//		System.out.println(result);
//	}
//
//	@Test
//	public void queryList() {
//		QuoteBean quoteBean = new QuoteBean();
//		quoteBean.setChannelUserId("71a35090a6bb9dc5074273ef67ac1df0");
//		quoteBean.setTaskId("1835035");
//		quoteBean.setPageNum("1");
//		quoteBean.setPageSize("10");
//		chnChannelService.queryList(quoteBean, "nqd_minizzb2016");
//	}
//
//	@Test
//	public void query() {
//		QuoteBean quoteBean = new QuoteBean();
//		quoteBean.setChannelUserId("71a35090a6bb9dc5074273ef67ac1df0");
//		quoteBean.setTaskId("1837182");
//		quoteBean.setPrvId("20074419");
//		quoteBean.setPageNum("1");
//		quoteBean.setPageSize("10");
//		chnChannelService.query(quoteBean, "nqd_minizzb2016");
//	}
//
//	@Test
//	public void getNoti() {
//		System.out.println(this.getNoti("1800467", "测试", "奖金", "101"));
//		System.out.println(this.getNoti("1800467", "测试", "奖金", "102"));
//		System.out.println(this.getNoti("1800467", "测试", "奖金", "103"));
//		System.out.println(this.getNoti("1800467", "测试", "奖金", "104"));
//	}
//
//	@Test
//	public void sign() {
//		try {
//			INSCCode inscCode = new INSCCode();
//			inscCode.setParentcode("ChannelForMini");
//			inscCode.setCodetype("channelurl");
//			inscCode.setCodevalue("01");
//			inscCode = inscCodeService.queryOne(inscCode);
//			String url = "";
//			String signUrl = "";
//			if (null != inscCode) {
//				url = inscCode.getProp2() + "/channel/miniOrderQuery";
//				signUrl = inscCode.getProp2() + "/channel/getSignForInner";
//			}
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("refundOrder", "refundOrder");
//			// map.put("taskId", "617693");
//			// map.put("prvId", "20074419");
//			map.put("pageSize", "5");
//			map.put("pageNum", "1");
//			String json = JsonUtils.serialize(map);
//			httpHead.put("channelId", "nqd_minizzb2016");
//			String sign = HttpSender.doPost(signUrl, json, httpHead, "UTF-8");
//			httpHead.put("sign", sign);
//			String res = HttpSender.doPost(url, json, httpHead, "UTF-8");
//			System.out.println(res);
//			MiniOrderListMappingModel model = JsonUtils.deserialize(res, MiniOrderListMappingModel.class);
//			System.out.println(model.getBody().getRows().get(0).get("taskId"));
//			System.out.println(JsonUtils.serialize(model));
//		} catch (Exception e) {
//
//		}
//	}
//
//	@Test
//	public void testChannelService() {
//		QuoteBean quoteBean = chnChannelService.getTaskQueryByTaskIdAndDeptId("1829972", "20214419", true);
//		System.out.println(JsonUtils.serialize(quoteBean));
//	}
//
//	@Test
//	public void testOrderPayment() {
//		INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
//		insbOrderpayment.setTaskid("104975");
//		insbOrderpayment.setPayresult("01");
//		insbOrderpayment = insbOrderpaymentService.queryOne(insbOrderpayment);
//		String paytime;
//		Map<String, Object> map = new HashMap<String, Object>();
//		if (insbOrderpayment != null) {
//			map.put("paytime", (!"".equals(ModelUtil.conbertToStringsdf(insbOrderpayment.getPaytime())))
//					? ModelUtil.conbertToStringsdf(insbOrderpayment.getPaytime())
//					: ModelUtil.conbertToStringsdf(insbOrderpayment.getCreatetime()));
//		} else {
//			map.put("paytime", "");
//		}
//		System.out.println(JsonUtils.serialize(map));
//	}
//
//	@Test
//	public void wxMsgTemplateServiceTest() {
//
//		//wxMsgTemplateService.sendUnCompeleteImage("1837005", "20194419", "71a35090a6bb9dc5074273ef67ac1df0", "", "");
//		//wxMsgTemplateService.sendUnderwritingFailMsg("1837233", "20074419", "0.01", "不给你核保");
//
//		wxMsgTemplateService.systemApplyRefundMsg("456456","123123","5e8cd836f9bd5a5ec62a1719dbfd0877","500");
//
//		/*
//		 * System.out.println(ConfigUtil.getPropString("minichannelid",
//		 * "nqd_minizzb2016"));
//		 * saveAgentTask("6234567","4343ewfee323","nqd_minizzb2016");
//		 */
//
//		/*
//		 * wxMsgTemplateService.sendApplyRefundMsg("645154","20054419",
//		 * "77133ab576b3c056c579ddaca4cb3da8","330");
//		 * wxMsgTemplateService.sendUnCompeleteImage("645154","20054419",
//		 * "77133ab576b3c056c579ddaca4cb3da8","影像1，影像2","20160804 10:10:20");
//		 *
//		 * System.out.println(wxMsgTemplateService.getBaseUrl("WeChat",
//		 * "minizzburl", "01", "oYT6zvxB3d127fqKgn32AiVWHtbA", "myRedPacket"));
//		 * //wxMsgTemplateService.sendDeliveryMsg("645154", "20054419");
//		 * wxMsgTemplateService.sendRewardMsg("645154","20054419",
//		 * "77133ab576b3c056c579ddaca4cb3da8","110","02","",0);
//		 * //wxMsgTemplateService.sendRewardMsg(
//		 * "6e39a59e319532fdcbee6aad92ea3b8e", "20", "02","02"); Double amount =
//		 * 120d; //wxMsgTemplateService.sendRewardMsg(
//		 * "6e39a59e319532fdcbee6aad92ea3b8e", "80", "01", "02");
//		 * wxMsgTemplateService.sendRewardMsg(
//		 * "77133ab576b3c056c579ddaca4cb3da8", String.valueOf(amount), "01",
//		 * "02",2); //wxMsgTemplateService.sendRewardMsg(
//		 * "77133ab576b3c056c579ddaca4cb3da8", "10", "01","分享");
//		 * //wxMsgTemplateService.sendRewardMsg(
//		 * "77133ab576b3c056c579ddaca4cb3da8", "40", "04","推荐");
//		 * //wxMsgTemplateService.sendRewardMsg(
//		 * "77133ab576b3c056c579ddaca4cb3da8", "50", "05","抽奖");
//		 * wxMsgTemplateService.sendPaidMsg("645154","20054419",
//		 * "77133ab576b3c056c579ddaca4cb3da8","5000","01");
//		 * wxMsgTemplateService.sendPaidMsg("645154","20054419",
//		 * "77133ab576b3c056c579ddaca4cb3da8","200","02");
//		 * wxMsgTemplateService.sendCashMsg("6f70a02fc1f78890e4a5a9a33452a3f2",
//		 * "100", "13"); wxMsgTemplateService.sendInsuredMsg("656849",
//		 * "20074419", "77133ab576b3c056c579ddaca4cb3da8");
//		 * //wxMsgTemplateService.sendUnderwritingSuccessMsg("656849",
//		 * "20074419", "15263.16");
//		 * wxMsgTemplateService.sendCashMsg("6f70a02fc1f78890e4a5a9a33452a3f2",
//		 * "200", "14"); wxMsgTemplateService.sendRewardMsg("645154","20054419",
//		 * "77133ab576b3c056c579ddaca4cb3da8","60","03","",0);
//		 * wxMsgTemplateService.sendRewardMsg("645154","20054419",
//		 * "77133ab576b3c056c579ddaca4cb3da8","70","04",
//		 * "77133ab576b3c056c579ddaca4cb3da8",0);
//		 * wxMsgTemplateService.sendRewardMsg("645154","20054419",
//		 * "77133ab576b3c056c579ddaca4cb3da8","80","05","",0); //
//		 * wxMsgTemplateService.sendUnderwritingSuccessMsg("645154", "20054419",
//		 * "5000"); wxMsgTemplateService.sendUnderwritingFailMsg("645154",
//		 * "20054419", "5000", "不给你核保");
//		 */
//
//	}
//
//	private void saveAgentTask(String taskId, String agentid, String channelId) {
//		try {
//			if (channelId.equals(ConfigUtil.getPropString("miniChannelId", "nqd_minizzb2016"))) {
//				INSBAgentTask insbAgentTask = new INSBAgentTask();
//				insbAgentTask.setAgentid(agentid);
//				insbAgentTask.setTaskid(taskId);
//				Long total = insbAgentTaskService.queryCount(insbAgentTask);
//				if (total <= 0) {
//					insbAgentTask.setStatus("0");
//					insbAgentTask.setCreatetime(new Date());
//					insbAgentTaskService.saveAgentTask(insbAgentTask);
//				}
//			}
//		} catch (Exception e) {
//			LogUtil.info(
//					"saveAgentTask error ! taskid=" + taskId + " agentid=" + agentid + " errmsg=" + e.getMessage());
//		}
//	}
//
//	private String getNoti(String taskid, String activityname, String prizetype, String type) {
//		String noti = "";
//		Map<String, Object> queryMap = new HashMap<String, Object>();
//		queryMap.put("taskid", taskid);
//		List<Map<String, Object>> results = insbAccountDetailsDao.queryBusinessListByTaskid(queryMap);
//		Map<String, Object> result = new HashMap<String, Object>();
//		if (null != results && results.size() > 0) {
//			result = results.get(0);
//		}
//		switch (type) {
//		case "101": /* 酬金 */
//			if (result.containsKey("推荐人")) {
//				result.remove("推荐人");
//			}
//			if (result.containsKey("赏金率")) {
//				result.remove("赏金率");
//			}
//			noti = JsonUtils.serialize(result);
//			break;
//		case "102": /* 奖券兑现 */
//			if (result.containsKey("推荐人")) {
//				result.remove("推荐人");
//			}
//			if (result.containsKey("赏金率")) {
//				result.remove("赏金率");
//			}
//			if (result.containsKey("商业险酬金率")) {
//				result.remove("商业险酬金率");
//			}
//			if (result.containsKey("交强险酬金率")) {
//				result.remove("交强险酬金率");
//			}
//			result.put("奖券类型", prizetype);
//			noti = JsonUtils.serialize(result);
//			break;
//		case "103": /* 奖金 */
//			if (result.containsKey("推荐人")) {
//				result.remove("推荐人");
//			}
//			if (result.containsKey("赏金率")) {
//				result.remove("赏金率");
//			}
//			if (result.containsKey("商业险酬金率")) {
//				result.remove("商业险酬金率");
//			}
//			if (result.containsKey("交强险酬金率")) {
//				result.remove("交强险酬金率");
//			}
//			result.put("奖金类型", prizetype);
//			result.put("活动名称", activityname);
//			noti = JsonUtils.serialize(result);
//			break;
//		case "104": /* 赏金 */
//			if (result.containsKey("商业险酬金率")) {
//				result.remove("商业险酬金率");
//			}
//			if (result.containsKey("交强险酬金率")) {
//				result.remove("交强险酬金率");
//			}
//			noti = JsonUtils.serialize(result);
//			break;
//		case "201": /* 提现 */
//
//		default:
//			;
//
//		}
//
//		return noti;
//	}
//
//	@Test
//	public void testInsertDate() {
//
//		int aa = insbDate.getWorkday(3);
//
//		System.out.println(aa);
//
//
//
//	}
//	@Test
//	public void testRole() {
//
//		INSBMiniRole ll = new INSBMiniRole();;
//
//
//		ll.setId(UUIDUtils.random());
//		ll.setName("普通成员");
//		ll.setCreatetime(new Date());
//		ll.setRolecode("03");
//
//		insbMiniRoleDao.saveObject(ll);
//
//	}
//	@Test
//	public void testSelectAgent() {
//
//		List<INSBAgentUser> ls = insbMiniUserRoleDao.selectAgentUser(new HashMap<>());
//		System.out.println(ls);
//	}
//	@Test
//	public void testInsert() {
//		INSBMiniUserRole insb = new INSBMiniUserRole();
//		inssss.insert(insb);
//	}
//
//	@Test
//	public void testInsertMiniPermission() {
//		INSBMiniPermission in = null;
//		for(int i =0;i<9;i++) {
//
//			in = new INSBMiniPermission();
//			in.setId(UUIDUtils.random());
//			in.setPermissionname("查看家里的事情"+i);
//			in.setPerdesc("只有才可以全员都可以");
//			in.setCreatetime(new Date());
//			in.setUrl("www.baidu.com");
//
//			insbMiniPermissionDao.saveObject(in);
//		}
//
//
//	}
//	@Test
//	public void testInsertRP() {
//		INSBRolePermission rp = new INSBRolePermission();
//		rp.setId(UUIDUtils.random());
//		rp.setPermissionid("96a343925cbe8939b3a8ccc401101352");
//		rp.setRoleid("8f0b994200c987aebfe38fdfc313d660");
//		rp.setCreatetime(new Date());
//		insbRolePermissionDao.saveObject(rp);
//	}
//
//	@Test
//	public void testSelectObject() {
//
//		List<Map<String, Object>> rows = insbMiniUserRoleDao.selectRoleByUserid("27e148828d2771083f99c24ce10c9793");
//		System.out.println(rows);
//
//	}
//	@Test
//	public void testselectUserAttr() {
//		String openid = "oxXTZwIZdvRPPc4qbXZecpnKmtKs";
//		String json = inssss.selectUserAttr(openid);
//		System.out.println(json);
//	}
//	@Test
//	public void testAgentRole() {
//		INSBAgentUser agent = new INSBAgentUser();
//		agent.setOpenid("oxXTZwFlH6PCJRi2C002YkpqexMY");
//		agent.setCity("东莞市");
//		insbAgentUserService.saveOrUpdateAgentWeiXin(agent);
//
//	}
//	@Test
//	public void testSubmit() throws Exception {
//		CHNChannelQuoteServiceImpl charQueueServic = new CHNChannelQuoteServiceImpl();
//		String taskid = "373114";
//		String channelid = "0045f0201ff0468d148187d561cd3bce";
//		QuoteBean quoteBean = new QuoteBean();
//		quoteBean.setChannelId(channelid);
//		quoteBean.setTaskId(taskid);
//		PersonBean applicant = new PersonBean();
//		PersonBean insured = new PersonBean();
//		PersonBean beneficiary = new PersonBean();
//		PersonBean carowner = new PersonBean();
//		applicant.setName("石贵武");
//		applicant.setIdcardNo("341004198805070075");
//		applicant.setIdcardType("0");
//		insured.setName("石贵武");
//		insured.setIdcardNo("341004198805070075");
//		insured.setIdcardType("0");
//		beneficiary.setName("石贵武");
//		beneficiary.setIdcardNo("341004198805070075");
//		beneficiary.setIdcardType("0");
//		carowner.setName("石贵武");
//		carowner.setIdcardNo("341004198805070075");
//		carowner.setIdcardType("0");
//		quoteBean.setApplicant(applicant);
//		quoteBean.setInsured(insured);
//		quoteBean.setBeneficiary(beneficiary);
//		quoteBean.setCarOwner(carowner);
////		charQueueServic.checkIdcardInfo(quoteBean);
//		boolean flag = charQueueServic.checkIdcardNo(quoteBean);
//
//		System.out.println(flag);
//	}
}

