package com.zzb.cm.service.impl;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.DispatchService;
import com.cninsure.jobpool.Task;
import com.cninsure.jobpool.dispatch.DispatchTaskService;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.dao.INSCUserDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCDeptService;
import com.common.*;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import com.zzb.app.service.AppQuotationService;
import com.zzb.chn.service.CHNChannelService;
import com.zzb.chn.util.JsonUtils;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.cm.controller.vo.DeliveryInfoItem;
import com.zzb.cm.controller.vo.OrderManageVO;
import com.zzb.cm.controller.vo.OrderManageVO01;
import com.zzb.cm.dao.*;
import com.zzb.cm.entity.*;
import com.zzb.cm.exception.ManualUnderWritingException;
import com.zzb.cm.service.*;
import com.zzb.conf.component.MessageManager;
import com.zzb.conf.dao.*;
import com.zzb.conf.entity.*;
import com.zzb.conf.service.*;
import com.zzb.ldap.LdapMd5;
import com.zzb.mobile.service.AppInsuredQuoteService;
import com.zzb.mobile.util.PayConfigMappingMgr;
import com.zzb.mobile.util.PayStatus;
import com.zzb.model.WorkFlow4TaskModel;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class INSBOrderServiceImpl extends BaseServiceImpl<INSBOrder> implements INSBOrderService {
	@Resource
	private INSBWorkflowmainDao insbWorkflowmainDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSBInsuredDao insbInsuredDao;
	@Resource
	private INSBPersonDao insbPersonDao;
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private INSBOrderdeliveryDao insbOrderDeliveryDao;
	@Resource
	private INSBQuotetotalinfoDao quotetotalinfoDao;
	@Resource
	private INSBOrderpaymentDao insbOrderpaymentDao;
	@Resource
	private INSBProviderDao insbProviderDao;
	@Resource
	private INSCDeptDao inscDeptDao;
	@Resource
	private INSCCodeDao inscCodeDao;
    @Resource
    private INSBPaychannelDao insbPaychannelDao;
    @Resource
	private INSBQuoteinfoService quoteInfoService;
	@Resource
	private INSBQuotetotalinfoService quotetotalInfoService;
	@Resource
	private INSBOrderpaymentDao orderpaymentDao;
	@Resource
	private INSBUsercommentService insbUsercommentService;
	@Resource
	private INSBCertificationDao insbCertificationDao;
	@Resource
	private INSBWorkflowsubDao insbWorkflowsubDao;
	@Resource
	private AppQuotationService appQuotationService;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;
	@Resource
	private DispatchService dispatchService;
	@Resource
	private DispatchTaskService dispatchTaskService;
	@Resource
	private INSBOrderdeliveryService insbOrderdeliveryService;
	@Resource
	private INSBOrderdeliveryDao insbOrderdeliveryDao;
	@Resource
	private MessageManager messageManager;
	@Resource
	private INSBProviderService insbProviderService;
	@Resource
	private INSBCarowneinfoService insbCarowneinfoService;
	@Resource
	private INSBPersonService insbPersonService;
	@Resource
	private INSBDeliveryaddressService insbDeliveryaddressService;
	@Resource
	private AppInsuredQuoteService appInsuredQuoteService;
	@Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
    @Resource
    private INSBWorkflowsubtrackService insbWorkflowsubtrackService;
	@Resource
	private INSBOperatorcommentService insbOperatorcommentService;
	@Resource
	private INSBGroupmembersDao insbGroupmembersDao;
	@Resource
	private IRedisClient redisClient;
	@Resource
	private INSBWorkflowsubService workflowsubService;
	@Resource
	private INSCUserDao inscUserDao;
	@Resource
	private CHNChannelService channelService;
	@Resource
	private INSBCommonQuoteinfoService commonQuoteinfoService;
	@Resource
	private INSBWorkflowsubDao workflowsubDao;
	@Resource
	private INSBCarinfohisDao carinfohisDao;
	@Resource
	private INSBWorkflowmainService workflowmainService;
	@Resource
	private INSBMsgPushService insbMsgPushService;
	@Resource
	private INSCDeptService inscDeptService;
	@Resource
	private INSBUnderwritingService insbUnderwritingService;
	@Resource
	private INSBWorkflowmaintrackdetailService insbWorkflowmaintrackdetailService;

	private static String WORKFLOW = "";
	private static String QUERYSECONDPAY_REQURL_FENQIBAO="";
	private static String QUERYSECONDPAY_REQURL_UNIONPAY="";

	static {
		// 读取相关的配置
		ResourceBundle resourceBundle = ResourceBundle.getBundle("config/config");
		WORKFLOW = resourceBundle.getString("workflow.url");
		QUERYSECONDPAY_REQURL_FENQIBAO = resourceBundle.getString("querysecondpay.requrl.fenqibao");
    	QUERYSECONDPAY_REQURL_UNIONPAY = resourceBundle.getString("querysecondpay.requrl.unionpay");
	}

	@Override
	protected BaseDao<INSBOrder> getBaseDao() {
		return insbOrderDao;
	}
	
	@Override
	public String getQuerySecondPayURL(String payChannelId){
		String returnVal = null;
		if(payChannelId != null && !payChannelId.equals("")){
			switch(payChannelId){
				case "28":
					returnVal = QUERYSECONDPAY_REQURL_FENQIBAO;
					break;
				case "30":
					returnVal = QUERYSECONDPAY_REQURL_UNIONPAY;
					break;
			}
		}
		return returnVal;
	}

	@Override
	public Map<String, Object> getTaskSummary(String taskid,String inscomcode) {
		INSBPerson person = null;
		INSBAgent agent = null;
		INSBProvider provider = null;
		INSBQuotetotalinfo quotetotalinfo = null;
		INSCDept dept = new INSCDept();
		Map<String, Object> tempMap = new HashMap<String, Object>();
		List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectPolicyitemList(taskid);
		quotetotalinfo = new INSBQuotetotalinfo();
		quotetotalinfo.setTaskid(taskid);
		quotetotalinfo = quotetotalinfoDao.selectOne(quotetotalinfo);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("taskid", taskid);
		map.put("inscomcode", inscomcode);
		INSBOrder order = insbOrderDao.selectOrderByTaskId(map);
		INSBCarinfo carinfo = insbCarinfoDao.selectCarinfoByTaskId(taskid);
		INSBInsured insured = insbInsuredDao.selectInsuredByTaskId(taskid);
		//投保单号组织信息
		if(policyitemList.size()>0){
			for (INSBPolicyitem temp : policyitemList) {
				if("1".equals(temp.getRisktype())){
					tempMap.put("ciproposalno", temp.getProposalformno());
					tempMap.put("cipremium", temp.getPremium());
				}else{
					tempMap.put("biproposalno", temp.getProposalformno());
					tempMap.put("bipremium", temp.getPremium());
				}
			}
		}
		//供应商
		if(quotetotalinfo!=null){
			tempMap.put("agentteam", quotetotalinfo.getTeam());
		}
		if(insured!=null){
			person = insbPersonDao.selectById(insured.getPersonid());
			//tempMap.put("prvid", order.getPrvid());
		}
		if(order != null){
			agent = insbAgentDao.selectAgentByAgentCode(order.getAgentcode());
		}

		if (order != null) {
			tempMap.put("id", order.getId());
			tempMap.put("operator", order.getOperator());
			if (order.getCreatetime() != null) {
				// 订单创建时间
				tempMap.put("createtime", ModelUtil.conbertToString(order.getCreatetime()));
			}
			if (order.getModifytime() != null) {
				// 订单修改时间
				tempMap.put("modifytime", ModelUtil.conbertToString(order.getModifytime()));
			}
			// 订单号
			tempMap.put("orderno", order.getOrderno());
			// 网点
			tempMap.put("deptcode", order.getDeptcode());
			dept = inscDeptDao.selectByComcode(order.getDeptcode());
			if(dept!=null)
			tempMap.put("deptname", dept.getComname());
			// 供应商
			tempMap.put("prvid", order.getPrvid());
			provider = insbProviderDao.queryByPrvcode(order.getPrvid());
			//投保公司
			tempMap.put("provider", provider.getPrvshotname());
			// 订单状态
			tempMap.put("orderstatus", order.getOrderstatus());
			tempMap.put("operating1", "applytask(\'" + taskid + "\',\'"+ order.getOrderstatus() +"\');");
		}

		if (carinfo != null) {
			// 车牌号
			tempMap.put("carlicenseno", carinfo.getCarlicenseno());
		}

		if (person != null) {
			// 用户备注
			tempMap.put("noti", person.getNoti());
			tempMap.put("insuredname", person.getName());
		}
		if (agent != null) {
			tempMap.put("agentcode",agent.getAgentcode());
			tempMap.put("agentname",agent.getName());
			// 电话
			tempMap.put("mobile", agent.getMobile());
			// 资格证号
			tempMap.put("cgfns", agent.getCgfns());
			// 执业证/展业证号
			tempMap.put("licenseno", agent.getLicenseno());
			// 证件号码
			tempMap.put("idno", agent.getIdno());
		}
		
		
		return tempMap;
	}
	@Override
	public String updatePaymentinfo(String taskid,String inscomcode,String paymentmethod,String checkcode,String insurecono,String tradeno,String subInstanceId,String agentnum){
		INSBOrder order=new INSBOrder();
		order.setTaskid(taskid);
		order.setPrvid(inscomcode);
		order=insbOrderDao.selectOne(order);
		if(order!=null){
			try{
				order.setPaymentmethod(paymentmethod);
				System.out.println("----servicegetPaymentmethod--------------"+order.getPaymentmethod());
				insbOrderDao.updateById(order);
//				校验码，暂不需要
//				INSBPolicyitem policyitem=insbPolicyitemDao.selectPolicyitemByTaskId(taskid);
//				policyitem.setCheckcode(checkcode);
				INSBOrderpayment orderpayment=new INSBOrderpayment();
				// orderpayment.setOrderid(order.getId());
				orderpayment.setSubinstanceid(subInstanceId);
				INSBOrderpayment orderpaymenttemp =  insbOrderpaymentDao.selectOneByCode(orderpayment);
				if(orderpaymenttemp!=null){
					orderpaymenttemp.setInsurecono(insurecono);
					orderpaymenttemp.setTradeno(tradeno);
					orderpaymenttemp.setPaychannelid(paymentmethod);
//					insbPolicyitemDao.updateById(policyitem);
					insbOrderpaymentDao.updateById(orderpaymenttemp);
				}else{
					//创建一个新的orderpayment记录
					INSBQuotetotalinfo temp =new INSBQuotetotalinfo();
					temp.setTaskid(taskid);
					INSBQuotetotalinfo total = quotetotalInfoService.queryOne(temp);
					INSBQuoteinfo subtemp =new INSBQuoteinfo();
					subtemp.setQuotetotalinfoid(total.getId());
					subtemp.setInscomcode(inscomcode);
					INSBQuoteinfo subinfo = quoteInfoService.queryOne(subtemp);
					
					INSBOrderpayment orderPayment = new INSBOrderpayment();
					orderPayment.setTaskid(taskid);   //设置任务id
					orderPayment.setOperator(agentnum);      //操作员
					orderPayment.setSubinstanceid(subinfo.getWorkflowinstanceid());
					orderPayment.setOrderid(order.getId());         //订单表id
					orderPayment.setPaychannelid(paymentmethod); //支付通道id
//	    		orderPayment.setPaychanneltype(payChannel.getSupportterminal());// 支付渠道类型--支持终端
//   			orderPayment.setPaystyle(payChannel.getPaytype());               //支付方式--支付类型
					orderPayment.setAmount((subinfo.getQuotediscountamount()==null?0:subinfo.getQuotediscountamount())
							+(order.getTotaldeliveryamount()==null?0:order.getTotaldeliveryamount()));   
//			    orderPayment.setAmount((order.getTotalpaymentamount()==null?0:order.getTotalpaymentamount())
//					+(order.getTotaldeliveryamount()==null?0:order.getTotaldeliveryamount()));   
					//支付金额（实付金额）+配送费  ，判断是否为空
					orderPayment.setCurrencycode(order.getCurrency());       //币种编码
					orderPayment.setPayresult("00");         //支付状态：待支付-00，正在支付-01, 支付完成-02 等等
					orderPayment.setCreatetime(new Date());  //创建时间
					insbOrderpaymentDao.insert(orderPayment);  //插入一条数据
				}
				return "success";
			}catch(Exception e){
				throw new RuntimeException();
			}
		}
		return "fail";
	}
	@Override
	public Map<String, Object> getPaymentinfo(String taskid,String inscomcode) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("taskid",  taskid);
		map.put("inscomcode", inscomcode);
		INSBOrder order=insbOrderDao.selectOrderByTaskId(map);
		Map<String, Object> tempMap = new HashMap<String, Object>();
		if(order!=null){
			INSBOrderdelivery orderdelivery=insbOrderDeliveryDao.getOrderdeliveryByTaskId(map);
			INSBPolicyitem policyitem=insbPolicyitemDao.selectPolicyitemByTaskId(taskid);
			INSBOrderpayment orderpayment = new INSBOrderpayment();
//			orderpayment.setOrderid(order.getId());
			orderpayment.setTaskid(order.getTaskid());
			orderpayment.setPayresult("02");
			List<INSBOrderpayment> orderpayments = insbOrderpaymentDao.selectList(orderpayment);
			//orderpayment.setInsurecono(insurecono);
			double sumFee = 0;
			if (order != null) {
				// 支付方式id
				tempMap.put("paymentmethodid", order.getPaymentmethod());//字典id
				//出单网点
				tempMap.put("deptcode", order.getDeptcode());//字典id
				//支付方式
//				tempMap.put("paymentmethod", order.getPaychannelname());//支付方式名称。
			}
			if(orderdelivery!=null){
				//快递费
				tempMap.put("totaldeliveryamount", orderdelivery.getFee());
				if(!StringUtils.isEmpty(orderdelivery.getDelivetype())){
					INSCCode deliveryTypeCode = new INSCCode();
					deliveryTypeCode.setCodetype("deliveType");
					deliveryTypeCode.setParentcode("deliveType");
					deliveryTypeCode.setCodevalue(orderdelivery.getDelivetype());
					deliveryTypeCode = inscCodeDao.selectOne(deliveryTypeCode);
					if(deliveryTypeCode!=null){
						tempMap.put("deliverymethod", deliveryTypeCode.getCodename());//配送方式
					}else{
						tempMap.put("deliverymethod", "自取");//配送方式
					}
				}else{
					tempMap.put("deliverymethod", "自取");//配送方式
				}
				sumFee+=orderdelivery.getFee()==null?0:orderdelivery.getFee();
			}
			if (policyitem != null) {
				// 校验码
				tempMap.put("checkcode", policyitem.getCheckcode());
				// 保费
				//tempMap.put("premium", policyitem.getPremium());
				//sumFee+=policyitem.getPremium()==null?0:policyitem.getPremium();
			}
			if(null!=orderpayments&&orderpayments.size()>0){
				for(INSBOrderpayment orderpay:orderpayments){
					Date creatime=orderpay.getCreatetime();
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String createtime=sdf.format(creatime);
					System.out.println(createtime);
					INSBPaychannel insbpaychannel=insbPaychannelDao.selectById(orderpay.getPaychannelid());
					String payResult=orderpay.getPayresult(); //支付结果
					if("02".equals(payResult)){ //02代表支付成功
						if( insbpaychannel != null ){
							tempMap.put("paymentmethod", insbpaychannel.getPaychannelname());
						}
						// 支付手续费
						tempMap.put("paypoundage",orderpay.getPaypoundage());
						// 支付跟踪号
						tempMap.put("tradeno", orderpay.getTradeno());
						tempMap.put("creatime", createtime);
						tempMap.put("result", "支付成功");
						tempMap.put("amount", orderpay.getAmount());
						if(StringUtil.isNotEmpty(orderpay.getPaymentransaction())){
							orderpayment.setPaymentransaction((orderpayment.getPaymentransaction()==null?"":orderpayment.getPaymentransaction())+orderpay.getPaymentransaction()+"<br/>");
						}
					}
				}
				// 支付流水号
				if(StringUtil.isNotEmpty(orderpayment.getPaymentransaction()))
					tempMap.put("paymentransaction", orderpayment.getPaymentransaction());
//				sumFee+=orderpayment.getPaypoundage()==null?0:orderpayment.getPaypoundage();
//				tempMap.put("premium", orderpayment.getAmount()); //与上面的保费，待确定取那个
//				sumFee+=orderpayment.getAmount()==null?0:orderpayment.getAmount();
			}
			tempMap.put("premium", order.getTotalpaymentamount()-
					(order.getTotaldeliveryamount()==null?0.0d:order.getTotaldeliveryamount())); //保费
			
			//总计
			tempMap.put("total", order.getTotalpaymentamount());
		}
		return tempMap;
	}
	
	@Override
	public List<Map<String, Object>> getSecondPaymentinfo(String taskid,String inscomcode) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("taskid",  taskid);
		map.put("inscomcode", inscomcode);
		INSBOrder order=insbOrderDao.selectOrderByTaskId(map);
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		if(order!=null){
			INSBOrderdelivery orderdelivery=insbOrderDeliveryDao.getOrderdeliveryByTaskId(map);
			INSBPolicyitem policyitem=insbPolicyitemDao.selectPolicyitemByTaskId(taskid);
			INSBOrderpayment orderpayment = new INSBOrderpayment();
			orderpayment.setTaskid(order.getTaskid());
//			orderpayment.setOrderid(order.getId());
//			orderpayment.setPayresult("02");
			List<INSBOrderpayment> orderpayments = insbOrderpaymentDao.selectList(orderpayment);
			if(null!=orderpayments&&orderpayments.size()>0){
				for(INSBOrderpayment orderpay:orderpayments){
					if("02".equals(orderpay.getPayresult()) || "03".equals(orderpay.getPayresult())){
					Map<String, Object> tempMap = new HashMap<String, Object>();
					INSBPaychannel insbpaychannel=insbPaychannelDao.selectById(orderpay.getPaychannelid());
					tempMap.put("paychannelid", orderpay.getPaychannelid());
					if( insbpaychannel != null ){
						tempMap.put("paymentmethod", insbpaychannel.getPaychannelname());
						
						//需求819, 在cm后台二次支付任务页面“支付信息”模块文字后新增“查询订单结果”按钮,仅支持“银联快捷支付”-30和“分期保支付”-28
						if(insbpaychannel.getId().equals("28") || insbpaychannel.getId().equals("30")){
							tempMap.put("showqueryorderbtn", "1"); //显示支付订单详情标识
							tempMap.put("secpayorderrequrl", this.getQuerySecondPayURL(insbpaychannel.getId()));
						}
					}
					if(orderdelivery!=null){
						//快递费
						tempMap.put("totaldeliveryamount", orderdelivery.getFee());
					}
					Date creatime=orderpay.getCreatetime();
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String createtime=sdf.format(creatime);
					String payResult=orderpay.getPayresult(); //支付结果
					tempMap.put("paymentransaction", orderpay.getPaymentransaction());//支付询价号
					tempMap.put("orderno", order.getOrderno());
					tempMap.put("payflowno", orderpay.getPayflowno()); //支付流水号
					if("02".equals(payResult)){ //02代表支付成功
						// 支付手续费
						tempMap.put("paypoundage",orderpay.getPaypoundage());
						// 支付跟踪号
						tempMap.put("tradeno", orderpay.getTradeno());
						tempMap.put("creatime", createtime);
						tempMap.put("result", "支付成功");
						tempMap.put("amount", orderpay.getAmount());//金额
					}else if("03".equals(payResult) && "02".equals(orderpay.getRefundtype())){
						tempMap.put("creatime", createtime);
						tempMap.put("tradeno", orderpay.getTradeno());
						tempMap.put("result", "已退款");
						tempMap.put("refundtype", "差额退款");
						tempMap.put("amount", orderpay.getAmount());//金额
					}
					tempMap.put("premium", order.getTotalpaymentamount()-
							(order.getTotaldeliveryamount()==null?0.0d:order.getTotaldeliveryamount())); //保费
					
					//总计
					tempMap.put("total", order.getTotalpaymentamount());
					list.add(tempMap);
					}
				}
			}
			
		}
		return list;
	}
	/*
	 * 二次支付成功的接口方法
	 * 任务id - taskid,主流程id，用来查询order表条件 
	 * 实例流程id - instanceid
	 */
	@Override
	public String getMediumPayment(String instanceid,String inscomcode,String taskid,String usercode) throws Exception{
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("taskid", taskid);
		map.put("inscomcode", inscomcode);
		//二次支付改Orderstatus为打单
		INSBOrder temp = insbOrderDao.selectOrderByTaskId(map);
		temp.setOrderstatus("2");
//		insbOrderDao.updateById(temp);
		
		//如果首单时间为空则更新代理人首单时间和taskid
		INSBAgent agent = new INSBAgent();
		agent.setJobnum(temp.getAgentcode());
		agent = insbAgentDao.selectOne(agent);
		if(null!=agent&&null==agent.getFirstOderSuccesstime()){
			agent.setFirstOderSuccesstime(new Date());
			agent.setTaskid(temp.getTaskid());
			insbAgentDao.updateById(agent);
		}
		
		Map<String,String> par = new HashMap<String,String>();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userid", usercode);
		params.put("processinstanceid", Integer.parseInt(instanceid));
		params.put("taskName","二次支付确认");
		params.put("result", "3");
		Map<String, String> dataMap= new HashMap<String,String>();
		dataMap.put("acceptway", JSONObject.fromObject(workflowmainService.getContractcbType(taskid, inscomcode, "0", "contract")).getString("quotecode"));
		params.put("data", dataMap);
		JSONObject bo= JSONObject.fromObject(params);
		par.put("datas", bo.toString());

		WorkflowFeedbackUtil.setWorkflowFeedback(taskid, instanceid, "21", "Completed", "二次支付确认", WorkflowFeedbackUtil.secpayment_complete, usercode);

		String message = HttpClientUtil.doGet(WORKFLOW+"/process/completeSubTask", par);
        LogUtil.info(taskid+","+inscomcode+"推二次支付确认工作流结果："+message);
        
        /*if(message !=null && "" != message){
        	JSONObject result = JSONObject.fromObject(message);
        	if(result.get("message").equals("success"))
        	{*/
        		if(temp != null && "28".equals(temp.getPaymentmethod())){
        			LogUtil.info("众安二次支付确认后支付成功:" + instanceid);
        			INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
        			insbOrderpayment.setTaskid(taskid);
        			insbOrderpayment.setSubinstanceid(instanceid);
        			insbOrderpayment.setPayresult("02");
        			insbOrderpayment = insbOrderpaymentDao.selectOneByCode(insbOrderpayment);
        			LogUtil.info("查询车牌号，任务号：" + taskid + "，保险公司：" + temp.getPrvid());
        			
        			Map<String,String> quoteinfoMap = new HashMap<>();
        			quoteinfoMap.put("taskid", taskid);
        			quoteinfoMap.put("companyid", temp.getPrvid());
        			INSBQuoteinfo insbQuoteinfo = insbQuoteinfoDao.getByTaskidAndCompanyid(quoteinfoMap);
        			
        			INSBCarinfohis carinfohisParam = new INSBCarinfohis();
        			carinfohisParam.setTaskid(taskid);
        			carinfohisParam.setInscomcode(temp.getPrvid());
        			INSBCarinfohis carinfohis = carinfohisDao.selectOne(carinfohisParam);
        			
        			Map<String, Object> map1 = new HashMap<>();
        			map1.put("bizId", temp.getOrderno());
        			map1.put("payDate", String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((insbOrderpayment.getPaytime())))); //保网垫付保费日期
        			BigDecimal bd1 = new BigDecimal(insbOrderpayment.getAmount()).setScale(2,RoundingMode.HALF_UP);
        			map1.put("amount",String.valueOf(bd1)); //实际支付金额
        			//车牌号
        			String carNo =  insbQuoteinfo.getPlatenumber();
        			if(null != carNo && !"".equals(carNo) && !"新车未上牌".equals(carNo) ){
        				map1.put("carNo",carNo);//车牌号
        			}else{
        				//没有车牌号用车架号后六位代替
        				if(null != carinfohis.getVincode()){
        					LogUtil.info("任务号：" + taskid +"的车牌为空，车架号为：" + carinfohis.getVincode());
        					map1.put("carNo", carinfohis.getVincode().substring((carinfohis.getVincode().length() - 6), carinfohis.getVincode().length()));
        				}else{
        					LogUtil.info("任务号：" + taskid +"的车牌为空，车架号为也为空" );
        					map1.put("carNo", "");
        				}
        			}
        			
        			String jsonStr = JsonUtils.serialize(map1);
        			LogUtil.info("众安二次支付确认后回调支付平台参数:" + jsonStr);
        			JSONObject json = null;
        			try {
        				LogUtil.info("众安二次支付确认后回调支付平台URL：" + PayConfigMappingMgr.getSecondPayCallbackUrl());
        				String retStr = HttpSender.doPost(PayConfigMappingMgr.getSecondPayCallbackUrl(), jsonStr);
        				LogUtil.info("支付平台响应结果:" + retStr);
        			} catch (Exception e) {
        				LogUtil.info("二次支付确认后回调支付平台出现异常，异常信息：" + e.getMessage());
        			}
        		}
        		/*}
        }else{
        	LogUtil.warn("推二次支付确认工作流失败");
        }*/
        //子流程任务完成，报价阶段成功继续主流程
        /*String taskname = "报价";
    	if("15".equals(insbWorkflowmainDao.selectByInstanceId(taskid).getTaskcode())){
    		taskname = "快速续保";
    	}
    	WorkFlowUtil.subCompletedContinueMainTask(taskid, taskname,
    		JSONObject.fromObject(workflowmainService.getContractcbType(taskid, inscomcode, "0", "contract")).getString("quotecode"), inscomcode);*/
		return "success";
	}

	 /*
     * 配送成功接口
     */
	@Override
	public String getDeliverySuccess(String processinstanceid,String usercode,String inscomcode,String deltype,String codevalue,String tracenumber) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("taskid", processinstanceid);
		map.put("inscomcode", inscomcode);
		INSBOrder temp = insbOrderDao.selectOrderByTaskId(map);
		temp.setOrderstatus("4");      //订单状态为完成 4
		insbOrderDao.updateById(temp);
		
		INSBOrderdelivery orderdelivery = new INSBOrderdelivery();
		orderdelivery.setTaskid(processinstanceid);
		orderdelivery.setProviderid(inscomcode);
		orderdelivery = insbOrderdeliveryDao.selectOne(orderdelivery);
		if(orderdelivery!=null){
			orderdelivery.setDelivetype(deltype);
			if("1".equals(deltype)){//如果是快递需要录入配送编号和快递公司
				orderdelivery.setTracenumber(tracenumber);
				orderdelivery.setLogisticscompany(codevalue);
			}
			insbOrderdeliveryDao.updateById(orderdelivery);//更新配送方式
		}else{
			orderdelivery = new INSBOrderdelivery();
			orderdelivery.setCreatetime(new Date());
			orderdelivery.setOperator(usercode);
			orderdelivery.setTaskid(processinstanceid);
			orderdelivery.setOrderid(temp.getId());
			orderdelivery.setDeptcode(temp.getDeptcode());
			orderdelivery.setDelivetype(deltype);
			if("1".equals(deltype)){//如果是快递需要录入配送编号和快递公司
				orderdelivery.setTracenumber(tracenumber);
				orderdelivery.setLogisticscompany(codevalue);
			}
			orderdelivery.setProviderid(inscomcode);
			INSBProvider provider = insbProviderDao.queryByPrvcode(inscomcode);
			if(provider!=null){
				orderdelivery.setProvidername(provider.getPrvname());
			}
			insbOrderdeliveryDao.insert(orderdelivery);
		}

		//完成时修改配送任务在ready状态下的操作人为已完成的用户
		LogUtil.info("修改配送任务的操作人为当前用户=taskid="+processinstanceid+"=userid="+usercode);
		try {
			INSBWorkflowmaintrackdetail insbWorkflowmaintrackdetail = new INSBWorkflowmaintrackdetail();
			insbWorkflowmaintrackdetail.setInstanceid(processinstanceid);
			insbWorkflowmaintrackdetail.setTaskcode("24");
			insbWorkflowmaintrackdetail.setTaskstate("Ready");
			insbWorkflowmaintrackdetail = insbWorkflowmaintrackdetailService.queryOne(insbWorkflowmaintrackdetail);
			LogUtil.info("===主流程轨迹数据==="+JSONObject.fromObject(insbWorkflowmaintrackdetail).toString());

			if(insbWorkflowmaintrackdetail!=null && StringUtil.isNotEmpty(usercode)){
				insbWorkflowmaintrackdetail.setOperator(usercode);
				insbWorkflowmaintrackdetailService.updateById(insbWorkflowmaintrackdetail);
				LogUtil.info("更新操作人成功=taaskid="+processinstanceid);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String,String> par = new HashMap<String,String>();
		Map<String,Object> params = new HashMap<String,Object>();

		params.put("userid", usercode);
		params.put("processinstanceid", Integer.parseInt(processinstanceid));
		params.put("taskName","配送");
		JSONObject bo= JSONObject.fromObject(params);
		par.put("datas",bo.toString());

		WorkflowFeedbackUtil.setWorkflowFeedback(processinstanceid, null, "24", "Completed", "配送", WorkflowFeedbackUtil.underWrittn_deliver, usercode);
		String message = HttpClientUtil.doGet(WORKFLOW+"/process/completeTask", par);
        LogUtil.info(processinstanceid+","+inscomcode+"推配送工作流结果："+message);
        return "success";
		/*JSONObject result= JSONObject.fromObject(message);
		if(result.getString("message").equals("success")){
			return "success";
		} else {
			throw new RuntimeException("工作流配送失败！");
		}*/
		
	}

	public String undwrtDeliverySuccess(String processinstanceid, String usercode, String inscomcode, String deltype, String codevalue, String tracenumber, String taskcode) {
		Map<String, String> map = new HashMap<String, String>();
		try{
			String message = insbUnderwritingService.undwrtSuccess(processinstanceid,usercode,taskcode,inscomcode);
			if( !"success".equals(message) ) {
				map.put("status", "fail");
				map.put("msg", message);
				return JSONObject.fromObject(map).toString();
			}
		}catch(Exception e){
			e.printStackTrace();
			map.put("status", "error1");
			return JSONObject.fromObject(map).toString();
		}
		LogUtil.info("结束承保打单--taskid="+processinstanceid+"--taskcode="+taskcode);

		//若成功先返回success,后改变表里的状态.所以加一个线程等待
		taskthreadPool4workflow.execute(new Runnable() {
			@Override
			public void run() {
				//工作流服务那边查询状态不对的时候会休眠两秒，所以这里以最坏情况做3秒休眠
				LogUtil.info("承保打单配送强制休眠开始--taskid=" + processinstanceid);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				//查询表里的字段是否改变,若改变才进行下一步操作
				String currentaskcode = workflowmainService.selectaskcode(processinstanceid);
				if (!"24".equals(currentaskcode)) {
					//若不是配送的taskcode,再等待2秒
					try {
						Thread.sleep(2000);
						currentaskcode = workflowmainService.selectaskcode(processinstanceid);
						//如果还没有改变,则返回失败信息
						if (!"24".equals(currentaskcode)) {
							LogUtil.info("承保打单配送失败--taskid="+processinstanceid+"--currentaskcode="+currentaskcode);
							return;
							/*map.put("status", "taskcoderror");
							return JSONObject.fromObject(map).toString();*/
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				LogUtil.info("承保打单配送强制休眠结束--taskid=" + processinstanceid + "--deltype=" + deltype + "--comcodevalue=" + codevalue + "--tracenumber=" + tracenumber);

				try {
					getDeliverySuccess(processinstanceid, usercode, inscomcode, deltype, codevalue, tracenumber);
				} catch (Exception e) {
					e.printStackTrace();
					/*map.put("status", "error2");
					return JSONObject.fromObject(map).toString();*/
				}
			}
		});

		map.put("status", "success");
		return JSONObject.fromObject(map).toString();
	}

	/*
         * 订单管理页面liuchao
         */
	@Override
	public Map<String,Object> allSelectOrderManageCode(OrderManageVO01 myTaskVo01, INSCUser loginUser) {
		Map<String,Object> result = new HashMap<>();
		int limit = myTaskVo01.getLimit();
		int offset = (myTaskVo01.getCurrentpage()-1)*limit;
		/*Map<String,Object> params = BeanUtils.toMap(myTaskVo01);
		//查询分页数据
		params.put("loginUserId", loginUser.getId());
		params.put("userorganization", loginUser.getDeptinnercode());//登陆者机构id
		params.put("offset", offset);//偏移量
		params.put("limit", limit);//每页显示条数
        params.put("deliveryType", myTaskVo01.getDeliveryType());
        params.put("paymentMethod", myTaskVo01.getPaymentMethod());*/
        //查询所有数据 BUG-8150
        Map<String,Object> all = BeanUtils.toMap(myTaskVo01);
        all.put("loginUserId", loginUser.getId());
        all.put("userorganization", loginUser.getDeptinnercode());//登陆者机构id
        all.put("offset", 0);//
        all.put("limit", 1000);//应对应所能处理的任务数量
        all.put("deliveryType", myTaskVo01.getDeliveryType());
        all.put("paymentMethod", myTaskVo01.getPaymentMethod());
        long totalSize=0;
		List<Map<String,Object>> orderManageList= new LinkedList<>();
		String taskStatus = myTaskVo01.getTaskstatus();
		//先查所有数据
		if("01".equals(taskStatus) || StringUtils.isBlank(taskStatus)){
			//查询是否有领取支付任务的权限，若usercode不为空，则说明有，若为空则没有
			boolean flag=insbOrderDao.checkPayPower(loginUser.getId());
			LogUtil.info("业管："+loginUser.getName()+",flag="+flag+"。(若flag=false没有权限;true有权限。)");
			if(flag){
				orderManageList.addAll(insbOrderDao.selectOrderWorkflowsub(all));
			}else{
				result.put("orderManageList",null);
				result.put("currentPage", 1);
				result.put("totalPages", 1);
				result.put("totalSize", 0);
				return result;
			}
		}
		if("02".equals(taskStatus) || "03".equals(taskStatus) || StringUtils.isBlank(taskStatus)){
			orderManageList.addAll(insbOrderDao.selectOrderWorkflowmain(all));
		}
		if("04".equals(taskStatus) || StringUtils.isBlank(taskStatus)){
			orderManageList.addAll(insbOrderDao.selectCertification(all));
		}
		//权限判断
		List<Map<String, Object>> tempcarTaskList = null;
		List<Map<String, String>> privileges = null;
		if(null!=orderManageList&&orderManageList.size()>0){//如果查询的list不为空才进入权限判断
			privileges = insbGroupmembersDao.selectGroupIdinfosByUserCodes4Tasklist(loginUser.getUsercode());
			StringBuffer privilegeStr = new StringBuffer();
			for(Map<String, String> privilege:privileges){//解析所有业管组及权限信息
				if(null!=privilege.get("tasktype")&&privilege.get("tasktype").contains(",")){
					String[] tasks = privilege.get("tasktype").split(",");
					for(String task:tasks){
						if(StringUtil.isNotEmpty(task))
							privilegeStr.append(task+"@"+privilege.get("deptid")+"@"+privilege.get("provideid"));
					}
				}else{
					privilegeStr.append(privilege.get("tasktype")+"@"+privilege.get("deptid")+"@"+privilege.get("provideid"));
				}
			}
			if (privilegeStr.toString().length()>0) {//有业管权限人员根据其具体权限控制可查看任务
				LogUtil.debug(loginUser.getUsercode()+"task用户业管权限privilege="+privilegeStr.toString());
				tempcarTaskList = new ArrayList<Map<String, Object>>();
				try{
					for(Map<String, Object> cartask:orderManageList){
						String cartaskinfo = "";
						if(!StringUtil.isEmpty(cartask.get("codevalue"))){//如果任务类型为空则不在权限判定范围
							cartaskinfo = String.valueOf(cartask.get("codevalue")) +"@"+ String.valueOf(cartask.get("comcode"))+"@"+(String.valueOf(cartask.get("inscomcode")).substring(0,4));
						}else if("999".equals(cartask.get("taskcode"))){//认证任务
							cartaskinfo = "h@"+ String.valueOf(cartask.get("deptid"))+"@";
						}else{
							cartaskinfo = "@"+ String.valueOf(cartask.get("comcode"))+"@"+(String.valueOf(cartask.get("inscomcode")).substring(0,4));
						}
						if(StringUtil.isNotEmpty(cartaskinfo)&&!privilegeStr.toString().contains(cartaskinfo)){//不在权限组内，删除不显示
							LogUtil.debug(cartask.get("mInstanceid")+"=taskid="+loginUser.getUsercode()+"task用户业管权限无privilege="+cartaskinfo);
							//carTaskList.remove(cartask);
						}else{
							//过滤权限后的总记录数
							totalSize ++;
							tempcarTaskList.add(cartask);
						}
					}
				}catch(Exception e){
					LogUtil.info(loginUser.getUsercode()+"task用户业管权限判断异常cartasklist="+orderManageList);
					e.printStackTrace();
				}
			}
		}
		
		/*//清空原list,重新查询分页内容
		orderManageList.clear();
		if("01".equals(taskStatus) || StringUtils.isBlank(taskStatus)){
			//查询是否有领取支付任务的权限，若usercode不为空，则说明有，若为空则没有
			boolean flag=insbOrderDao.checkPayPower(loginUser.getId());
			LogUtil.info("业管："+loginUser.getName()+",flag="+flag+"。(若flag=false没有权限;true有权限。)");
			if(flag){
				orderManageList.addAll(insbOrderDao.selectOrderWorkflowsub(params));
			}else{
				result.put("orderManageList",null);
				result.put("currentPage", 1);
				result.put("totalPages", 1);
				result.put("totalSize", 0);
				return result;
			}
		}

		if("02".equals(taskStatus) || "03".equals(taskStatus) || StringUtils.isBlank(taskStatus)){
			orderManageList.addAll(insbOrderDao.selectOrderWorkflowmain(params));
		}

		if("04".equals(taskStatus) || StringUtils.isBlank(taskStatus)){
			orderManageList.addAll(insbOrderDao.selectCertification(params));
		}*/
		//装入备注信息
		if(null!=tempcarTaskList){
			for(Map<String,Object> orderManage : tempcarTaskList){
				if(!"999".equals(orderManage.get("taskcode"))){
					//修改为显示所有的备注信息 
					//查询给操作员备注信息
					String maininstanceid = (String)orderManage.get("mInstanceid");
					String inscomcode = (String) orderManage.get("inscomcode");
					List<String> operatorNotiList = insbOperatorcommentService.getOperCommentByMaininstanceid(maininstanceid, inscomcode);
					if(operatorNotiList!=null && operatorNotiList.size() > 0){
						String noti = operatorNotiList.get(0);
						//将所有的给操作人备注信息返回给前端
						//如果备注超过二十五个字符,则点击更多按钮,显示全部的备注
						if(noti.length()>25){
							String subOperatorComment=noti.substring(0,25)+"...";
							subOperatorComment += "<a href=\"javascript:openNoti('" + maininstanceid +"','" + inscomcode + "', '2');\">更多</a>";
							orderManage.put("operatorcommentList",subOperatorComment);
						}else{
							orderManage.put("operatorcommentList",noti+"<a href=\"javascript:openNoti('" + maininstanceid +"','" + inscomcode + "', '2');\">更多</a>");
						}
					}
					//查询给用户备注信息
					/*List<String> notiList = insbUsercommentService.getUserComment(maininstanceid, inscomcode);
				//判断所有给用户备注的集合是否为空
				if(notiList!=null && notiList.size() > 0){
					String usercommentlist = notiList.get(0);
					//将所有的备注返回
					//如果备注超过二十五个字符,则点击更多按钮,显示全部的备注
					if(usercommentlist.length()>25){
						String subUsercommentlist=usercommentlist.substring(0,25)+"...";
						subUsercommentlist += "<a href=\"javascript:openNoti('" + maininstanceid +"','" + inscomcode + "', '1');\">更多</a>";
						orderManage.put("usercommentlist",subUsercommentlist);
					}else{
						orderManage.put("usercommentlist",usercommentlist+"<a href=\"javascript:openNoti('" + maininstanceid +"','" + inscomcode + "', '1');\">更多</a>");
					}
				}*/
					//查询给用户备注信息
					List<Map<String, Object>> notiList = insbUsercommentService.getUserCommentAndType(maininstanceid, inscomcode);
					if (notiList != null && notiList.size() > 0) {
						INSBUsercomment usc = (INSBUsercomment) notiList.get(0).get("userComment");
						String content = usc.getCommentcontent();
						//如果备注超过二十五个字符,则点击更多按钮,显示全部的备注
						content = content.length() > 25 ? content.substring(0, 25) + "..." : content;
						orderManage.put("usercommentlist", content + "<a href=\"javascript:openNoti('" + maininstanceid + "','" + inscomcode + "', '1');\">更多</a>");
					}
					
					//查询用户备注信息
					List<Map<String, Object>> commentList = insbUsercommentService.getNearestUserCommentAndType(maininstanceid, inscomcode);
					if (commentList != null && commentList.size() > 0) {
						INSBUsercomment usc = (INSBUsercomment) commentList.get(0).get("userComment");
						String content = usc.getCommentcontent();
						//如果备注超过二十五个字符,则点击更多按钮,显示全部的备注
						content = content.length() > 25 ? content.substring(0, 25) + "..." : content;
						orderManage.put("uscs", content + "<a href=\"javascript:openNoti('" + maininstanceid + "','" + inscomcode + "', '3');\">更多</a>");
					}
				}
			}
		}
		
		
		if(null!=tempcarTaskList){
			result.put("orderManageList", tempcarTaskList.subList(offset,(int) (offset+limit>totalSize?totalSize:offset+limit)));
			result.put("currentPage", myTaskVo01.getCurrentpage());
			result.put("totalPages", totalSize%limit==0?totalSize/limit:(totalSize/limit+1));
			result.put("totalSize", totalSize);
		}else{
			//如果没有权限就返回空列表
			result.put("orderManageList", null);
			result.put("currentPage", myTaskVo01.getCurrentpage());
			result.put("totalPages", totalSize%limit==0?totalSize/limit:(totalSize/limit+1));
			result.put("totalSize", 0);
		}
		return result;
	}

	/*
	 * 组织分页和分页条件
	 */
	public Map<String,Object> getLimitAndPageSize(OrderManageVO myTaskVo, String userId){
		//组织分页信息
		Map<String, Object> map = BeanUtils.toMap(myTaskVo);
		//组织分页查询条件
		map.put("qsimple",myTaskVo.getQsimple());
		map.put("orderstatus",myTaskVo.getOrderstatus());
		map.put("carlicenseno",myTaskVo.getCarlicenseno());
		map.put("insuredname", myTaskVo.getInsuredname());
		map.put("instanceid", myTaskVo.getInstanceid());
		map.put("prvid", myTaskVo.getProvidename());
		map.put("startdate", myTaskVo.getStarTime());
		map.put("enddate", myTaskVo.getEndTime());
		map.put("usertype", myTaskVo.getUsertype());
		map.put("phonenumber", myTaskVo.getPhoto());
		map.put("agentname", myTaskVo.getAgentname());
		map.put("agentnum", myTaskVo.getAgentnum());
		map.put("userId", userId);
		myTaskVo.setTotals(insbOrderDao.selectOrderManageCount(map));
		long total = myTaskVo.getTotals();
		int currentPage = myTaskVo.getCurrentpage()==0?1:myTaskVo.getCurrentpage();
		int pageSize = myTaskVo.getPagesize()==0?5:myTaskVo.getPagesize();
		int limit = (currentPage - 1) * pageSize;
		int totalpage = (int) (total / pageSize);// 总页数
		int mod = (int) (total % pageSize);// 最后一页的条数
		if (mod != 0){
			totalpage++;
		}
		myTaskVo.setTotalpage(totalpage);
		myTaskVo.setCurrentpage(currentPage);
		map.put("pageSize", pageSize);
		map.put("limit", limit);
		return map;
	}


	/*
	 * 支付完成or重新核保 接口
	 * issecond 二次支付为1,支付为0,重新核保为2
	 */
	@Override
	public String getPaySuccess(String processinstanceid,String inscomcode,String taskid,String usercode,String issecond) throws Exception {
		
		if(issecond.equals("0")){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("taskid", taskid);
			map.put("inscomcode", inscomcode);
			//支付只针对几种方式在这个页面上,改Orderstatus为打单
			INSBOrder insbOrder = insbOrderDao.selectOrderByTaskId(map);
			if(StringUtils.isBlank(insbOrder.getPaymentmethod())){
				return "请选择支付方式";
			}

			if("2".equals(insbOrder.getOrderstatus())){
				return "订单已支付";
			}
			
			//LINING 20160315 START 获取当前代理人是否为正式用户，如果不是不允许进行支付操作
			INSBAgent agent = new INSBAgent();
			agent.setJobnum(insbOrder.getAgentcode());
			List<INSBAgent> agentList = insbAgentDao.selectList(agent);
			if( agentList == null || agentList.size() < 1 ) {
				return "payFail";
			}
			//LINING 20160315 END 获取当前代理人是否为正式用户，如果不是不允许进行支付操作

			//bug-2543生产环境CM后台无配送方式时业管可以点支付成功
			INSBOrderdelivery orderdelivery = new INSBOrderdelivery();
			orderdelivery.setTaskid(taskid);
			orderdelivery.setProviderid(inscomcode);
			orderdelivery = insbOrderdeliveryService.queryOne(orderdelivery);
			if (orderdelivery == null) {
				orderdelivery = new INSBOrderdelivery();
				orderdelivery.setCreatetime(new Date());
				orderdelivery.setProviderid(inscomcode);   //供应商id
				//供应商信息
				INSBProvider provider = insbProviderService.queryByPrvcode(inscomcode);
				orderdelivery.setProvidername(provider.getPrvshotname());//供应商名称简写
				orderdelivery.setOperator("admin");             //操作员code
				orderdelivery.setTaskid(taskid);      //任务id
				orderdelivery.setOrderid(insbOrder.getId());      //订单orderno  id
				orderdelivery.setDeptcode(insbOrder.getDeptcode());  //出单网点
				orderdelivery.setDelivetype("0");       //配送方式，自取0或者 快递1 3电子保单
				orderdelivery.setDeliveryside("保网");           //默认保网
				orderdelivery.setNoti("代理人在前端未选择支付方式和配送方式，直接去柜台刷卡");

				//北京流程 判断代理人是否是北京、修改默认配送方式为电子保单
				for(INSBAgent insbAgent:agentList) {
					INSCDept dept = new INSCDept();
					dept.setComcode(insbAgent.getDeptid());
					dept = inscDeptService.queryOne(dept);
					if (dept != null) {
						INSCDept userDept = inscDeptService.getPlatformDept(dept.getId());
						if (userDept != null && (ConstUtil.PLATFORM_BEIJING_DEPTCODE.equals(userDept.getComcode()))) {
							orderdelivery.setDelivetype("3");       //配送方式，自取0或者 快递1     3电子保单
							break;
						}
					}
				}
				insbOrderdeliveryService.insert(orderdelivery);
			}

			insbOrder.setOrderstatus("2");
			insbOrder.setPaymentstatus("02");
			insbOrder.setModifytime(new Date());
			insbOrderDao.updateById(insbOrder);
			
			//如果首单时间为空则更新代理人首单时间和taskid
			agent.setJobnum(insbOrder.getAgentcode());
			agent = insbAgentDao.selectOne(agent);
			if(null==agent.getFirstOderSuccesstime()){
				agent.setFirstOderSuccesstime(new Date());
				agent.setTaskid(insbOrder.getTaskid());
				insbAgentDao.updateById(agent);
			}
			
			INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
			insbOrderpayment.setTaskid(insbOrder.getTaskid()); // 设置任务id
			insbOrderpayment.setOperator(insbOrder.getAgentcode()); // 操作员
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("taskid", insbOrder.getTaskid());
			map1.put("companyid", insbOrder.getPrvid());
			INSBQuoteinfo insbQuoteinfo = insbQuoteinfoDao.getByTaskidAndCompanyid(map1);
			insbOrderpayment.setSubinstanceid(insbQuoteinfo.getWorkflowinstanceid());
			insbOrderpayment.setOrderid(insbOrder.getId()); // 订单表id
			insbOrderpayment.setPaychannelid("4"); // 支付渠道
			insbOrderpayment.setNoti("03"); // 支付方式
			insbOrderpayment.setAmount(insbOrder.getTotalpaymentamount());
			insbOrderpayment.setCurrencycode("RMB"); // 币种编码
			insbOrderpayment.setPayresult("02");
			insbOrderpayment.setCreatetime(new Date()); // 创建时间
			insbOrderpayment.setModifytime(new Date());
			insbOrderpayment.setPaytime(new Date());
			insbOrderpaymentDao.insert(insbOrderpayment);
			
		}else{
			//重新提交核保的操作
			INSBOrderpayment pay = new INSBOrderpayment();
			pay.setTaskid(taskid);
			pay.setSubinstanceid(processinstanceid);
			pay = orderpaymentDao.selectOne(pay);
			if(pay!=null){
				if(PayStatus.PAYING.equals(pay.getPayresult())){
					return "paying";
				}
			}
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userid",usercode);
		params.put("processinstanceid", Integer.parseInt(processinstanceid));
		params.put("taskName","支付");
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("issecond", issecond); //支付接口有个参数名字是issecond，是二次支付为1,不是为0,重新核保是2
		if ("0".equals(issecond)) {
			data.put("acceptway", JSONObject.fromObject(workflowmainService.getContractcbType(taskid, inscomcode, "0", "contract")).getString("quotecode"));
			WorkflowFeedbackUtil.setWorkflowFeedback(taskid, processinstanceid, "20", "Completed", "支付", WorkflowFeedbackUtil.payment_complete, usercode);
		} else if ("1".equals(issecond)) {
			WorkflowFeedbackUtil.setWorkflowFeedback(taskid, processinstanceid, "21", "Completed", "二次支付确认", WorkflowFeedbackUtil.secpayment_complete, usercode);
		} else if ("2".equals(issecond)) {
			WorkflowFeedbackUtil.setWorkflowFeedback(taskid, processinstanceid, "20", "Completed", "支付", WorkflowFeedbackUtil.payment_reunderWriting, usercode);
			data.put("underwriteway", "3");//支付任务重新核保直接到人工核保
			//data.put("underwriteway", JSONObject.fromObject(workflowmainService.getContracthbType(taskid, inscomcode, "3", "underwriting")).optString("quotecode"));
		}
		params.put("data", data);
		JSONObject jsonb = JSONObject.fromObject(params);
		Map<String,String> par = new HashMap<String,String>();
		par.put("datas", jsonb.toString());


		String message = HttpClientUtil.doGet(WORKFLOW+"/process/completeSubTask", par);
		LogUtil.info("支付信息为:" + jsonb.toString() + ". 获取工作流支付返回信息:" + message);

        String result = "";
        //支付时http超时
        if (StringUtil.isEmpty(message) && "0".equals(issecond)) {
            //直接查询支付流程的状态
            INSBWorkflowsubtrack workflowsubtrack = insbWorkflowsubtrackService.getSubTrack(taskid, processinstanceid, "20");
            if (workflowsubtrack != null && "Completed".equalsIgnoreCase(workflowsubtrack.getTaskstate())) {
                result = "success";
            } else {
                result = "fail";
            }
        } else {
           /* JSONObject resultmsg = JSONObject.fromObject(message);
            if ("success".equals(resultmsg.get("message"))) {*/
                if ("0".equals(issecond)) {
                    try {
                        messageManager.sendMessage4Agent("admin", null, processinstanceid, "21");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                result = "success";
           /* } else {
                throw new RuntimeException("返回的支付失败信息");
            }*/
        }
       /* if ("0".equals(issecond)) {//无论是否通知工作流成功，确认支付成功的任务报价完成推到下一个主流程节点
        	String taskname = "报价";
        	if("15".equals(insbWorkflowmainDao.selectByInstanceId(taskid).getTaskcode())){
        		taskname = "快速续保";
        	}
        	WorkFlowUtil.subCompletedContinueMainTask(taskid, taskname,
        		JSONObject.fromObject(workflowmainService.getContractcbType(taskid, inscomcode, "0", "contract")).getString("quotecode"), inscomcode);
        }*/
        return result;
	}

	/*
	 * 人工录单完成
	 */
	@Resource
    InterFaceService interFaceService;
	@Override
	public String ManualRecording(String processinstanceid,String isRebackWriting,String usercode) {
		LogUtil.info("processinstanceid=" + processinstanceid + ", isRebackWriting=" + isRebackWriting);
		try{
            //isRebackWriting是否选择 同时完成回写选项,0为是 选择同时完成回写
			if(isRebackWriting.equals("0")){
				interFaceService.saveHisInfo(processinstanceid, "person", "A");
				
				taskthreadPool4workflow.execute(new Runnable() {
					@Override
					public void run() {
						try {
							Map<String,Object> params = new HashMap<String,Object>();
							params.put("userid", usercode);
							params.put("processinstanceid", Integer.parseInt(processinstanceid));
							Map<String,Object> data = new HashMap<String,Object>();
							data.put("result", "3");  //报价结果，成功是3
							params.put("data", data);
							JSONObject jsonb = JSONObject.fromObject(params);
							Map<String,String> par = new HashMap<String,String>();
							par.put("datas", jsonb.toString());

							WorkflowFeedbackUtil.setWorkflowFeedback(null, processinstanceid, "8", "Completed", "人工报价", WorkflowFeedbackUtil.quote_complete, usercode);
							
							String message = HttpClientUtil.doGet(WORKFLOW+"/process/completeLudanAndWriteBack", par);
							LogUtil.info(processinstanceid+"录单完成调用工作流接口completeLudanAndWriteBack返回结果：" + message);

						}catch(Exception ex) {
							ex.printStackTrace();
						}
					}
				});
				/*JSONObject result= JSONObject.fromObject(message);
				if(result.getString("message").equals("success")){
					this.discountChargeDataBack(processinstanceid);
					return "success";
				}
				else{
					return "fail";
				}*/
				this.discountChargeDataBack(processinstanceid);
				return "success";
			}else if(isRebackWriting.equals("1")){
				// 查询是否为非费改地区
				boolean isfeeflag = isFeeflagArea(null, processinstanceid);
				if( !isfeeflag ) {
					// 是非费改地区
					loopManualRecording(processinstanceid, usercode);
					return "success";
				}

				interFaceService.saveHisInfo(processinstanceid, "person", "A");
				taskthreadPool4workflow.execute(new Runnable() {
					@Override
					public void run() {
						try {
							//并不选择同时完成回写
							Map<String,Object> params = new HashMap<String,Object>();
							params.put("userid", usercode);
							params.put("processinstanceid", Integer.parseInt(processinstanceid));
							params.put("taskName","人工报价");
							Map<String,Object> data = new HashMap<String,Object>();
							data.put("result", "3");  //报价结果，成功是3
							params.put("data", data);
							JSONObject jsonb = JSONObject.fromObject(params);
							Map<String,String> par = new HashMap<String,String>();
							par.put("datas", jsonb.toString());
			
							WorkflowFeedbackUtil.setWorkflowFeedback(null, processinstanceid, "8", "Completed", "人工报价", WorkflowFeedbackUtil.quote_complete, usercode);
			
							String message = HttpClientUtil.doGet(WORKFLOW+"/process/completeSubTask", par);
			                LogUtil.info(processinstanceid+"录单完成调用工作流接口completeSubTask返回结果："+message);
						}catch(Exception ex) {
							ex.printStackTrace();
						}
					}
				});
				this.discountChargeDataBack(processinstanceid);
				return "success";
				/*JSONObject result= JSONObject.fromObject(message);
				if(result.getString("message").equals("success")){
					this.discountChargeDataBack(processinstanceid);
					return "success";
				}
				else{
					return "fail";
				}*/
			}else{
				return "fail";
				//不会出现的情况
			}
		}catch(Exception e){
            LogUtil.error("子任务"+processinstanceid+"人工录单完成操作出错："+e.getMessage());
			e.printStackTrace();
			return "fail";
		}
		
	}
	
	private void loopManualRecording(String instanceid, final String usercode) {
		INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();
		insbWorkflowsub.setInstanceid(instanceid);
		insbWorkflowsub = insbWorkflowsubDao.selectOne(insbWorkflowsub);
		String taskid = insbWorkflowsub.getMaininstanceid();
		List<INSBWorkflowsub> insbWorkflowsubList = insbWorkflowsubDao.selectSubModelByMainInstanceId(taskid);

		for( INSBWorkflowsub workflowsub : insbWorkflowsubList ) {
			final String processinstanceid = workflowsub.getInstanceid();
			interFaceService.saveHisInfo(processinstanceid, "person", "A");

			taskthreadPool4workflow.execute(new Runnable() {
				@Override
				public void run() {
					try {
						LogUtil.info("非费改地区录单完成taskid=" + taskid + "###提交子任务id=" + processinstanceid);

						Map<String,Object> params = new HashMap<String,Object>();
						params.put("userid", usercode);
						params.put("processinstanceid", Integer.parseInt(processinstanceid));
						params.put("taskName","人工报价");
						Map<String,Object> data = new HashMap<String,Object>();
						data.put("result", "3");  //报价结果，成功是3
						params.put("data", data);
						JSONObject jsonb = JSONObject.fromObject(params);
						Map<String,String> par = new HashMap<String,String>();
						par.put("datas", jsonb.toString());

						WorkflowFeedbackUtil.setWorkflowFeedback(null, processinstanceid, "8", "Completed", "人工报价", WorkflowFeedbackUtil.quote_complete, usercode);

						String message = HttpClientUtil.doGet(WORKFLOW+"/process/completeSubTask", par);
						LogUtil.info("非费改地区录单完成taskid=" + taskid + "###提交子任务id=" + processinstanceid + "推送工作流结束，推送结果：" + message);

						//JSONObject result= JSONObject.fromObject(message);
						//if(result.getString("message").equals("success")){
							INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
							quoteinfo.setWorkflowinstanceid(workflowsub.getInstanceid());
							quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
                            INSBWorkflowsub workSub = insbWorkflowsubDao.selectByInstanceId(workflowsub.getInstanceid());
							Map<String,Object> paramsMap = new HashMap<String,Object>();
							paramsMap.put("subinstanceid", workflowsub.getInstanceid());
							paramsMap.put("maininstanceid", workSub.getMaininstanceid());
							paramsMap.put("inscomcode", quoteinfo.getInscomcode());
							insbOrderDao.priceDataBackForManualRecording(paramsMap);
						//}

					}catch(Exception ex) {
						LogUtil.info("非费改地区录单完成taskid=" + taskid + "###提交子任务id=" + processinstanceid + "异常：" + ex.getMessage());
						ex.printStackTrace();
					}
				}
			});
		}
	}
	
	/*
	 * 人工录单成功后总保费数据回写
	 */
	public void discountChargeDataBack(String subinstanceid){
		INSBWorkflowsub workSub = insbWorkflowsubDao.selectByInstanceId(subinstanceid);
		INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
		quoteinfo.setWorkflowinstanceid(subinstanceid);
		quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("subinstanceid", subinstanceid);
		params.put("maininstanceid", workSub.getMaininstanceid());
		params.put("inscomcode", quoteinfo.getInscomcode());
		insbOrderDao.priceDataBackForManualRecording(params);
	}
	
	/**
	 * 通过主流程id，判断是否为费改地区.如果不能查询到相应的数据,返回默认为费改地区 true
	 * @param taskid  主流程id
	 * @param instanceid  子流程id，当主流程id为空，子流程id必须传
	 * @return
	 */
	@Override
	public boolean isFeeflagArea(String taskid, String instanceid) {
		// 默认为是费改地区
		boolean isFeeflagArea = true;
		if( StringUtils.isBlank(taskid) && StringUtils.isBlank(instanceid) ) {
			return isFeeflagArea;
		}
		if( StringUtils.isBlank(taskid) ) {
			INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();
			insbWorkflowsub.setInstanceid(instanceid);
			insbWorkflowsub = insbWorkflowsubDao.selectOne(insbWorkflowsub);
			if( insbWorkflowsub == null ) {
				return isFeeflagArea;
			}
			taskid = insbWorkflowsub.getMaininstanceid();
		}
		Map<String,String> agentData = quotetotalinfoDao.selectAgentDataByTaskId(taskid);
		if( agentData == null || agentData.size() < 1 ) {
			return isFeeflagArea;
		}
		String agentnum = agentData.get("agentnum");
		if(!StringUtils.isEmpty(agentnum)){
			Map<String, Object> resultAgentData =  appInsuredQuoteService.getChangeFee(null,agentnum);
			// 费改地区返回的isfeeflag是false
			if((boolean)resultAgentData.get("isfeeflag")){
				isFeeflagArea = false;
			}
		}
		return isFeeflagArea;
	}
	
	/*
	 * 人工录单退回修改接口
	 */
	@Override
	public String ManualRecordingBack(String processinstanceid,String usercode) {
		LogUtil.info("ManualRecordingBack----processinstanceid="+processinstanceid);
		try{
			// 查询是否为非费改地区
			boolean isfeeflag = isFeeflagArea(null, processinstanceid);
			if( !isfeeflag ) {
				// 是非费改地区
				loopManualRecordingBack(processinstanceid, usercode);
			}
			taskthreadPool4workflow.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Map<String,Object> params = new HashMap<String,Object>();
						params.put("userid",usercode);
						params.put("processinstanceid", Integer.parseInt(processinstanceid));
						params.put("taskName","人工报价");
						Map<String,Object> data = new HashMap<String,Object>();
						data.put("result", "2");  //报价结果，成功是3
						params.put("data", data);
						JSONObject jsonb = JSONObject.fromObject(params);
						Map<String,String> par = new HashMap<String,String>();
						par.put("datas", jsonb.toString());
			
						WorkflowFeedbackUtil.setWorkflowFeedback(null, processinstanceid, "8", "Completed", "人工报价", WorkflowFeedbackUtil.quote_sendback, usercode);
			
						String message = HttpClientUtil.doGet(WORKFLOW+"/process/completeSubTask", par);
			            LogUtil.info(processinstanceid + "人工录单退回修改调用工作流接口completeSubTask返回结果："+ message);
					}catch(Exception ex) {
						LogUtil.info("人工录单退回修改###提交子任务id=" + processinstanceid + "异常：" + ex.getMessage());
						ex.printStackTrace();
					}
				}
			});
			return "success";
			/*JSONObject result= JSONObject.fromObject(message);
			if(result.getString("message").equals("success")){
				return "success";
			}
			else{
				return "fail";
			}*/
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
		
	}
	
	private void loopManualRecordingBack(String instanceid, final String usercode) {
		INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();
		insbWorkflowsub.setInstanceid(instanceid);
		insbWorkflowsub = insbWorkflowsubDao.selectOne(insbWorkflowsub);
		String taskid = insbWorkflowsub.getMaininstanceid();
		LogUtil.info("非费改地区人工录单退回修改taskid=" + taskid + "###提交子任务id=" + instanceid);

		List<INSBWorkflowsub> insbWorkflowsubList = insbWorkflowsubDao.selectSubModelByMainInstanceId(taskid);
		for( INSBWorkflowsub workflowsub : insbWorkflowsubList ) {
			final String processinstanceid = workflowsub.getInstanceid();
			taskthreadPool4workflow.execute(new Runnable() {
				@Override
				public void run() {
					try {
                        Map<String,Object> params = new HashMap<String,Object>();
                        params.put("userid",usercode);
                        params.put("processinstanceid", Integer.parseInt(processinstanceid)); // ???
                        params.put("taskName","人工报价");
                        Map<String,Object> data = new HashMap<String,Object>();
                        data.put("result", "2");  //报价结果，成功是3
                        params.put("data", data);
                        JSONObject jsonb = JSONObject.fromObject(params);
                        Map<String,String> par = new HashMap<String,String>();
                        par.put("datas", jsonb.toString());

						WorkflowFeedbackUtil.setWorkflowFeedback(null, processinstanceid, "8", "Completed", "人工报价", WorkflowFeedbackUtil.quote_sendback, usercode);

                        String message = HttpClientUtil.doGet(WORKFLOW+"/process/completeSubTask", par);
                        LogUtil.info("非费改地区人工录单退回修改taskid=" + taskid + "###提交子任务id=" + processinstanceid + "推送工作流结束，返回结果："+ message);

                        /*JSONObject result= JSONObject.fromObject(message);
                        if(result != null && result.getString("message").equals("success")){
                        }*/

					} catch(Exception ex) {
						LogUtil.info("非费改地区人工录单退回修改taskid=" + taskid + "###提交子任务id=" + processinstanceid + "推送异常：" + ex.getMessage());
						ex.printStackTrace();
					}
				}
			});
		}
	}
	
	/*
	 * 人工录单重新报价
	 */
	@Override
	public String ManualRecordingRe(String processinstanceid,String usercode) {
		LogUtil.info("ManualRecordingRe----processinstanceid="+processinstanceid);
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("userid",usercode);
			params.put("processinstanceid", Integer.parseInt(processinstanceid));
			params.put("taskName","人工报价");
			INSBWorkflowsub workflowsub = insbWorkflowsubDao.selectByInstanceId(processinstanceid);
			INSBQuoteinfo quote = quoteInfoService.getQuoteinfoByWorkflowinstanceid(processinstanceid);
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("result", "1");  //报价结果1 获取报价方式
			
			//调用规则判断承保限制条件
			appInsuredQuoteService.getPriceParamWay(data, workflowsub.getMaininstanceid(), quote.getInscomcode(), "0");
			params.put("data", data);
			JSONObject jsonb = JSONObject.fromObject(params);
			Map<String,String> par = new HashMap<String,String>();
			par.put("datas", jsonb.toString());

			WorkflowFeedbackUtil.setWorkflowFeedback(null, processinstanceid, "8", "Completed", "人工报价", WorkflowFeedbackUtil.quote_redo, usercode);

			String message = HttpClientUtil.doGet(WORKFLOW+"/process/completeSubTask", par);
            LogUtil.info(processinstanceid+"重新报价调用工作流接口completeSubTask返回结果："+message);

			return "success";
			/*JSONObject result= JSONObject.fromObject(message);
			if(result.getString("message").equals("success")){
				return "success";
			}
			else{
				return "fail";
			}*/
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
		
	}
	
	/*
	 * 人工回写完成按钮
	 */
	@Override
	public String manualWriteBackSuccess(String processinstanceid,String usercode) {
        LogUtil.info("manualWriteBackSuccess---processinstanceid="+processinstanceid);
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			//INSBWorkflowmain workflowmain =  insbWorkflowmainDao.selectINSBWorkflowmainByInstanceId(processinstanceid);
			params.put("userid", usercode);
			params.put("processinstanceid", Integer.parseInt(processinstanceid));
			params.put("taskName","人工回写");
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("result", "3");  //是3人工回写成功
			params.put("data", data);
			JSONObject jsonb = JSONObject.fromObject(params);
			Map<String,String> par = new HashMap<String,String>();
			par.put("datas", jsonb.toString());

			String message = HttpClientUtil.doGet(WORKFLOW+"/process/completeSubTask", par);
            LogUtil.info(processinstanceid+"人工回写调用工作流接口completeSubTask返回结果："+message);
            return "success";
			/*JSONObject result= JSONObject.fromObject(message);
			if(result.getString("message").equals("success")){
				return "success";
			}
			else{
				return "fail";
			}*/
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
		
	}

    /*
     * 人工回写退回修改接口
     */
	@Override
	public String manualWriteBack(String processinstanceid,String usercode) {
        LogUtil.info("manualWriteBack---processinstanceid="+processinstanceid);
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			//INSBWorkflowmain workflowmain =  insbWorkflowmainDao.selectINSBWorkflowmainByInstanceId(processinstanceid);
			params.put("userid", usercode);
			params.put("processinstanceid", Integer.parseInt(processinstanceid));
			params.put("taskName","人工回写");
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("result", "2");  //是2人工回写退回修改
			params.put("data", data);
			JSONObject jsonb = JSONObject.fromObject(params);
			Map<String,String> par = new HashMap<String,String>();
			par.put("datas", jsonb.toString());
			
			String message = HttpClientUtil.doGet(WORKFLOW+"/process/completeSubTask", par);
            LogUtil.info(processinstanceid+"人工回写退回修改调用工作流接口completeSubTask返回结果："+message);
            return "success";
			/*JSONObject jo = JSONObject.fromObject(message);
			if("success".equals(jo.getString("message"))){
				return "success";
			}else
				return "fail";*/
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
		
	}

	private void log(String taskId,String insCode,String str)
	{
		LogUtil.info("任务ID:"+taskId+"@"+insCode+","+str);
	}

	/*
	 *
	 * 人工核保成功按钮,taskid 是主流程id，processinstanceid 是子流程id 在这里
	 *
	 *
	 */
	@Override
	public void manualUnderWritingSuccess(String processinstanceid, String taskid, String inscomcode, String usercode) throws Exception {
		/**
		 * hxx 人工核保完成 修改 insborderpayment  根据德英 的逻辑 全部 不使用
		 */ 
//		orderPayUpdate(processinstanceid, taskid, inscomcode);
        checkPolicyStartDate(taskid, inscomcode);
		checkProposalNumber(taskid, inscomcode);

		this.log(taskid,inscomcode,"开始保存历史记录......");
		interFaceService.saveHisInfo(processinstanceid, "person", "B");
		this.log(taskid,inscomcode,"完成保存历史记录");

        //人工核保改Orderstatus为待支付
        this.log(taskid,inscomcode,"开始保存订单数据......");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("taskid", taskid);
        map.put("inscomcode", inscomcode);
        INSBOrder temp = insbOrderDao.selectOrderByTaskId(map);
        temp.setOrderstatus("1");
        insbOrderDao.updateById(temp);
		this.log(taskid,inscomcode,"完成保存订单数据");
		LogUtil.info("INSBOrder|报表数据埋点|"+JSONObject.fromObject(temp).toString());
        //-------------------
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userid", usercode);
        params.put("processinstanceid", Integer.parseInt(processinstanceid));
        params.put("taskName","人工核保");
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("result", "3");//人工核保完成的值是3
        params.put("data", data);
        JSONObject jsonb = JSONObject.fromObject(params);
        Map<String,String> par = new HashMap<String,String>();
        par.put("datas", jsonb.toString());

		this.log(taskid,inscomcode,"开始推动核保完成工作流");
		taskthreadPool4workflow.execute(new Runnable() {
			@Override
			public void run() {
				try {
					WorkflowFeedbackUtil.setWorkflowFeedback(taskid, processinstanceid, "18", "Completed", "人工核保", WorkflowFeedbackUtil.underWriting_complete, usercode);

					LogUtil.info("人工核保完成开始推送工作流:taskid="+taskid+",inscomcode="+inscomcode+",参数="+par.get("datas"));
					String message = HttpClientUtil.doGet(WORKFLOW+"/process/completeSubTask", par);
					LogUtil.info("人工核保完成结束推送工作流:taskid="+taskid+",inscomcode="+inscomcode+",message="+message);

                    /*JSONObject jo = JSONObject.fromObject(message);
                    if("success".equals(jo.getString("message"))){
                    }*/

				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		this.log(taskid,inscomcode,"完成启动线程调用推动工作流");
	}

	private void checkProposalNumber(String taskid, String inscomcode) {
		int ic = commonQuoteinfoService.getInsureConfigType(taskid, inscomcode);
		boolean hasbusi = false, hasstr = false;

		INSBPolicyitem policyitem = new INSBPolicyitem();
		policyitem.setTaskid(taskid);
		policyitem.setInscomcode(inscomcode);
		List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectList(policyitem);
		if (!policyitemList.isEmpty()) {
			Optional<INSBPolicyitem> businessPolicyOpt = policyitemList.stream().filter(i -> "0".equals(i.getRisktype())).findFirst();
			if (businessPolicyOpt.isPresent()) {
				hasbusi = StringUtil.isNotEmpty(businessPolicyOpt.get().getProposalformno());
			}
			Optional<INSBPolicyitem> compulsoryPolicyItemOpt = policyitemList.stream().filter(i -> "1".equals(i.getRisktype())).findFirst();
			if (compulsoryPolicyItemOpt.isPresent()) {
				hasstr = StringUtil.isNotEmpty(compulsoryPolicyItemOpt.get().getProposalformno());
			}
		}

		if ((ic == 1 || ic == 3) && !hasbusi) {
			throw new ManualUnderWritingException("商业险投保单号不能为空");
		}
		if ((ic == 2 || ic == 3 || ic == 4) && !hasstr) {
			throw new ManualUnderWritingException("交强险投保单号不能为空");
		}
	}

    private void checkPolicyStartDate(String taskid, String inscomcode) {
        INSBPolicyitem policyitem = new INSBPolicyitem();
        policyitem.setTaskid(taskid);
        policyitem.setInscomcode(inscomcode);
        List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectList(policyitem);
        if (!policyitemList.isEmpty()) {
            Optional<INSBPolicyitem> businessPolicyOpt = policyitemList.stream().filter(i -> "0".equals(i.getRisktype())).findFirst();
            if (businessPolicyOpt.isPresent()) {
                checkPolicyStartDate(businessPolicyOpt);
            }
            Optional<INSBPolicyitem> compulsoryPolicyItemOpt = policyitemList.stream().filter(i -> "1".equals(i.getRisktype())).findFirst();
            if (compulsoryPolicyItemOpt.isPresent()) {
                checkPolicyStartDate(compulsoryPolicyItemOpt);
            }
        }
    }

    private void checkPolicyStartDate(Optional<INSBPolicyitem> businessPolicyOpt) {
        INSBPolicyitem businessPolicyItem = businessPolicyOpt.get();
        Date startdate = businessPolicyItem.getStartdate();
        if (null != startdate) {
            Calendar startTime = Calendar.getInstance();
			long now = startTime.getTimeInMillis();

            startTime.setTime(startdate);
			long startDays = startTime.getTimeInMillis();

            if (startDays <= now) {
                 throw new ManualUnderWritingException("起保日期已过期！");
            }
        }
    }

    private void orderPayUpdate(String processinstanceid, String taskid, String inscomcode) {
		//是否存在数据
		LogUtil.info("核保通过 ——orderPayUpdate:==== processinstanceid="+processinstanceid + ",taskid="+taskid+",inscomcode="+inscomcode);
		try {
			INSBOrderpayment orderPaymentTemp = new INSBOrderpayment();
			orderPaymentTemp.setSubinstanceid(processinstanceid);
			orderPaymentTemp = insbOrderpaymentDao.selectOne(orderPaymentTemp);
			if(orderPaymentTemp!=null){
				Map<String,Object> maporder = new HashMap<String,Object>();
				maporder.put("taskid", taskid);
				maporder.put("inscomcode", inscomcode);              //保险公司id
				INSBOrder order = insbOrderDao.selectOrderByTaskId(maporder);
				if(order==null){
					return ;
				}
				Double totalpaymentamount  = order.getTotalpaymentamount();
				BigDecimal totalpaymentamountDecimal = new BigDecimal(String.valueOf(totalpaymentamount==null?0:totalpaymentamount));
				BigDecimal totaldeliveryamountDecimal = new BigDecimal(String.valueOf(order.getTotaldeliveryamount()==null?0:order.getTotaldeliveryamount()));
				orderPaymentTemp.setAmount(totalpaymentamountDecimal.add(totaldeliveryamountDecimal).setScale(2, RoundingMode.HALF_UP).doubleValue());   
				orderPaymentTemp.setMd5(LdapMd5.Md5Encode(orderPaymentTemp.getAmount()+orderPaymentTemp.getPayresult()+orderPaymentTemp.getPaymentransaction()));
				orderPaymentTemp.setModifytime(new Date());
				insbOrderpaymentDao.updateById(orderPaymentTemp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.info("核保通过——orderPayUpdate :====processinstanceid="+processinstanceid + ",taskid="+taskid+",inscomcode="+inscomcode + "Error");
		}
	}
	
	
	

	/*
	 * 人工核保退回修改
	 */
	@Override
	public String manualUnderWritingBack(String processinstanceid,String usercode) {
		try{
			LogUtil.info("核保退回: processinstanceid=" + processinstanceid + ",usercode=" + usercode);
			workflowsubService.updateWorkFlowSubData(null, processinstanceid, "18", "Completed", "人工核保", WorkflowFeedbackUtil.underWriting_sendback, usercode);
			channelCallback (processinstanceid, "18", "Completed", "人工核保");
			workflowsubService.updateWorkFlowSubData(null, processinstanceid, "19", "Reserved", "核保退回", "", "admin");
			channelCallback (processinstanceid, "19", "Reserved", "核保退回");

			//调度移除任务
			Task task = new Task();
			task.setSonProInstanceId(processinstanceid);
			task.setTaskTracks("admin");
			task.setTaskName("人工核保");
			String maininstanceId = null;

			try {
				maininstanceId = workflowsubDao.selectMainInstanceIdBySubInstanceId(processinstanceid);
				task.setProInstanceId(maininstanceId);
				dispatchTaskService.completeTask(task, "end");
			} catch (Exception e) {
				LogUtil.error(maininstanceId + "," + processinstanceid + "核保退回时移除任务出错：" + e.getMessage());
				e.printStackTrace();
			}

			//发核保退回消息
			insbMsgPushService.sendInsureGoBackMsg(processinstanceid, maininstanceId, usercode);

			/*Map<String,Object> params = new HashMap<String,Object>();
			params.put("userid",usercode);
			params.put("processinstanceid", Integer.parseInt(processinstanceid));
			params.put("taskName","人工核保");
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("result", "2");//人工核保退回修改的值2
			params.put("data", data);
			JSONObject jsonb = JSONObject.fromObject(params);
			Map<String,String> par = new HashMap<String,String>();
			par.put("datas", jsonb.toString());*/

			//WorkflowFeedbackUtil.setWorkflowFeedback(null, processinstanceid, "18", "Completed", "人工核保", WorkflowFeedbackUtil.underWriting_sendback, usercode);

            //LogUtil.info("核保退回修改开始推送工作流:processinstanceid="+processinstanceid+",参数="+par.get("datas"));
			//String message = HttpClientUtil.doGet(WORKFLOW+"/process/completeSubTask", par);
            //LogUtil.info("核保退回修改结束推送工作流:processinstanceid="+processinstanceid+",结果="+message);

			//JSONObject jo = JSONObject.fromObject(message);
			//if("success".equals(jo.getString("message"))){
				return "success";
			//}else
			//	return "fail";
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
	}

	private void channelCallback (String subTaskId, String taskCode, String taskState, String taskName) {
		String mainInstancId = insbWorkflowsubDao.selectMainInstanceIdBySubInstanceId(subTaskId);
		INSBQuoteinfo quoteinfo = insbQuoteinfoDao.queryQuoteinfoByWorkflowinstanceid(subTaskId);
		WorkFlow4TaskModel dataModel = new WorkFlow4TaskModel();
		dataModel.setMainInstanceId(mainInstancId);
		dataModel.setSubInstanceId(subTaskId);
		dataModel.setTaskCode(taskCode);
		dataModel.setTaskName(taskName);
		dataModel.setTaskStatus(taskState);
		dataModel.setDataFlag(2);
		dataModel.setProviderId(quoteinfo != null ? quoteinfo.getInscomcode() : "");
		taskthreadPool4workflow.execute(new Runnable() {
			@Override
			public void run() {
				try {
					channelService.callback(dataModel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/*
	 * 保存配送完成后，配送单号和物流公司的编号信息
	 */
	@Override
	public String saveTraceNumber(String processinstanceid, String tracenumber,
			String codevalue,String inscomcode) {
		try{
			INSBOrderdelivery orderDelivery = new INSBOrderdelivery();
			orderDelivery.setTaskid(processinstanceid);
			orderDelivery.setProviderid(inscomcode);
			orderDelivery = insbOrderDeliveryDao.selectOne(orderDelivery);
			orderDelivery.setTracenumber(tracenumber);
			orderDelivery.setLogisticscompany(codevalue);
//			orderDelivery.setDelivetype(deltype);
			int i = insbOrderDeliveryDao.updateById(orderDelivery);
			if(i==1){
				return "success";
			}else{
				return "fail";
			}
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
	}
	/**
	 * 【重新核保】接口
	 */
	@Override
	public String manualUnderWritingRestart(String processinstanceid,
			String taskid, String inscomcode, String usercode) {
		
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("userid", usercode);
			params.put("processinstanceid", Integer.parseInt(processinstanceid));
			params.put("taskName","人工核保");
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("result", "1");//人工核保重新核保1
			//重新获取核保途径
			String result = workflowmainService.getContracthbType(taskid, inscomcode, "", "underwriting");
			JSONObject jsonObject = JSONObject.fromObject(result);
			if(StringUtil.isNotEmpty(jsonObject.optString("quotecode"))){
				data.put("underwriteway", jsonObject.optString("quotecode"));
			}else{//返回核保途径为空，则默认重启人工核保
				data.put("underwriteway", "3");
			}
			params.put("data", data);
			JSONObject jsonb = JSONObject.fromObject(params);
			Map<String,String> par = new HashMap<String,String>();
			par.put("datas", jsonb.toString());

			WorkflowFeedbackUtil.setWorkflowFeedback(taskid, processinstanceid, "18", "Completed", "人工核保", WorkflowFeedbackUtil.underWriting_redo, usercode);

            LogUtil.info(result+"=核保途径，重新核保开始推送工作流:taskid="+taskid+",inscomcode="+inscomcode+",参数="+par.get("datas"));
			String message = HttpClientUtil.doGet(WORKFLOW+"/process/completeSubTask", par);
            LogUtil.info("重新核保结束推送工作流:taskid="+taskid+",inscomcode="+inscomcode+",结果="+message);
            return "success";
			/*JSONObject jo = JSONObject.fromObject(message);
			if("success".equals(jo.getString("message"))){
				return "success";
			}else
				return "fail";*/
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
	}

	@Override
	public List<INSBOrder> getListByPrvidLike(INSBOrder order) {
		return insbOrderDao.selectByPrvidLike(order);
	}

	/*
	 * 认证任务认领方法liuchao
	 */
	@Override
	public Map<String, Object> getCertificationTask(String userId,
			String cfTaskId) {
		Map<String, Object> result = new HashMap<String, Object>();
		INSBCertification certification = insbCertificationDao.selectById(cfTaskId);
		if(certification == null){
			result.put("status", "fail");
			result.put("msg", "未查询到此任务，请刷新列表后重试！");
		}else{
			if(StringUtils.isEmpty(certification.getDesignatedoperator())){
				certification.setDesignatedoperator(userId);
				insbCertificationDao.updateById(certification);
				result.put("status", "success");
				result.put("msg", "认领任务成功！");
			}else{
				result.put("status", "fail");
				result.put("msg", "此任务已被认领！");
			}
		}
		return result;
	}
	@Override
	public Map<String, Object> getCertificationTask2(String userCode,
													String cfTaskId) {
		Map<String, Object> result = new HashMap<String, Object>();
		INSBCertification certification = insbCertificationDao.selectById(cfTaskId);
		if(certification == null){
			result.put("status", "fail");
			result.put("msg", "未查询到此任务！");
			return result;
		}
		INSCUser user = inscUserDao.selectByUserCode(userCode);
		if (user == null) {
			result.put("status", "fail");
			result.put("msg", "用户名错误！");
			return result;
		}
		certification.setDesignatedoperator(userCode);
		insbCertificationDao.updateById(certification);
		result.put("status", "success");
		result.put("msg", "认领任务成功！");
		return result;
	}

	/*
	 * 车险任务认领方法liuchao
	 */
	@Override
	public Map<String, Object> getCarInsureTask(String maininstanceId, String subInstanceId, 
			String inscomcode, String mainOrsub, INSCUser loginUser) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			LogUtil.info("认领任务开始——getCarInsureTask====maininstanceId="+maininstanceId + ",inscomcode="+inscomcode+",mainOrsub="+mainOrsub + "loginUser"+loginUser.getUsercode()); 
			if("1".equals(mainOrsub)){//主流程
				INSBWorkflowmain workMain = insbWorkflowmainDao.selectByInstanceId(maininstanceId);
				if(workMain == null){
					result.put("status", "fail");
					result.put("msg", "未查询到此任务，请刷新列表后重试！");
				}else if(TaskConst.END_33.equals(workMain.getTaskcode())|| TaskConst.CLOSE_37.equals(workMain.getTaskcode())){
					result.put("status", "fail");
					result.put("msg", "查询到此任务已经关闭或结束，请刷新列表后重试！");
				}else{
					if(StringUtils.isEmpty(workMain.getOperator())){
						dispatchService.getTask(maininstanceId, inscomcode, 1, loginUser, loginUser);
						result.put("status", "success");
						result.put("msg", "认领任务成功！");
					}else{
						result.put("status", "fail");
						result.put("msg", "此任务已被认领！");
					}
				}
			}else if("2".equals(mainOrsub)){//子流程
				INSBWorkflowsub workSub = insbWorkflowsubDao.selectByInstanceId(subInstanceId);
				if(workSub == null){
					result.put("status", "fail");
					result.put("msg", "未查询到此任务，请刷新列表后重试！");
				}else if(TaskConst.END_33.equals(workSub.getTaskcode())|| TaskConst.CLOSE_37.equals(workSub.getTaskcode())){
					result.put("status", "fail");
					result.put("msg", "查询到此任务已经关闭或结束，请刷新列表后重试！");
				}else{
					if(StringUtils.isEmpty(workSub.getOperator())){
						dispatchService.getTask(subInstanceId, inscomcode, 2, loginUser, loginUser);
						result.put("status", "success");
						result.put("msg", "认领任务成功！");
					}else if("admin".equals(workSub.getOperator())){
						dispatchService.reassignment4NoRedis(subInstanceId, inscomcode, 2, loginUser, loginUser);
						result.put("status", "success");
						result.put("msg", "认领任务成功！");
					}else{
						result.put("status", "fail");
						result.put("msg", "此任务已被认领！");
					}
				}
			}else{
				result.put("status", "fail");
				result.put("msg", "传入类型不匹配，请刷新列表后重试！");
			}
			return result;
		} catch (RedisException e) {
			e.printStackTrace();
			LogUtil.info("认领任务——getCarInsureTask====maininstanceId="+maininstanceId + ",error="+e.getMessage());
			result.put("status", "fail");
			result.put("msg", "认领任务失败，请重试！");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.info("认领任务——getCarInsureTask====maininstanceId="+maininstanceId + ",error="+e.getMessage());
			result.put("status", "fail");
			result.put("msg", "认领任务失败！");
			return result;
		}
	}

	/**
	 * liuchao  支付任务重新发起核保时判断核保途径
	 */
	@Override
	public List<INSBWorkflowsubtrack> getUnderwritingTrack(String subInstanceId) {
		return insbOrderDao.getUnderwritingTrack(subInstanceId);
	}

	/*
	 * redisClient.set(taskId+"@"+insurancecompanyid+"@insure", "true");
	 *
	 */
	@Override
	public String getUnderWritingResult(String taskid, String inscomcode) {
		return (String) redisClient.get(Constants.CM_ZZB, taskid+"@"+inscomcode+"@insure");
	}

	@Override
	public String getQuoteBackResult(String taskid, String inscomcode) {
		return (String) redisClient.get(Constants.CM_ZZB, taskid+"@"+inscomcode+"@quote");
	}

	@Override
	public List<INSBWorkflowsubtrack> getQuoteBackTrack(String subInstanceId) {
		return insbOrderDao.getQuoteBackTrack(subInstanceId);
	}

	@Override
	public List<String> getQuoteBackTrackStr(String subInstanceId) {
		return insbOrderDao.getQuoteBackTrackStr(subInstanceId);
	}

	@Override
	public List<String> getUnderwritingTrackStr(String subInstanceId) {
		return insbOrderDao.getUnderwritingTrackStr(subInstanceId);
	}

	@Override
	public String editDeliveryInfo(DeliveryInfoItem deliveryinfo, INSCUser user){
		
		try{
			INSBOrderdelivery orderDelivery = new INSBOrderdelivery();
			orderDelivery.setTaskid(deliveryinfo.getTaskid());
			orderDelivery.setProviderid(deliveryinfo.getInscomcode());
			orderDelivery = insbOrderdeliveryService.queryOne(orderDelivery);
			
			INSBOrder order = new INSBOrder();
			order.setTaskid(deliveryinfo.getTaskid());
			order.setPrvid(deliveryinfo.getInscomcode());
//			order = insbOrderService.queryOne(order);
			order = insbOrderDao.selectOne(order);
			
			 //将输入的地址信息当做一条新的地址插入
			 INSBDeliveryaddress deliaddr = new INSBDeliveryaddress();
			 deliaddr.setRecipientname(deliveryinfo.getRecipientname());        //收件人姓名
			 deliaddr.setRecipientmobilephone(deliveryinfo.getRecipientmobilephone());//联系电话
			 deliaddr.setRecipientaddress(deliveryinfo.getRecipientaddress()); //详细地址
			 deliaddr.setZip(deliveryinfo.getZip());               //邮编
			 //获得中间表INSBCarowneinfo中的personid
			 INSBCarowneinfo carowneinfo = new INSBCarowneinfo();
			 carowneinfo.setTaskid(deliveryinfo.getTaskid());
			 carowneinfo = insbCarowneinfoService.queryOne(carowneinfo);
			 INSBPerson person = new INSBPerson();
			 person.setId(carowneinfo.getPersonid());
			 person = insbPersonService.queryById(person.getId());//找到车主，获得车主的信息
			 deliaddr.setCreatetime(new Date());            //创建时间
			 deliaddr.setOperator(user.getUsercode());                //操作员
			 deliaddr.setOwnername(person.getName());         //车主姓名
			 deliaddr.setOwneridcardno(person.getIdcardno());  //车主身份idcardno
			 deliaddr.setRecipientprovince(deliveryinfo.getProvince());
			 deliaddr.setRecipientcity(deliveryinfo.getCity());
			 deliaddr.setRecipientarea(deliveryinfo.getArea());
			 insbDeliveryaddressService.insert(deliaddr);  //插入一条新的地址操作
			
			if(orderDelivery!=null){
				//更新操作
//				orderDelivery.setDeliveryside(deliveryinfo.getDeliveryside());暂不修改 配送方式
				orderDelivery.setRecipientname(deliveryinfo.getRecipientname());
				orderDelivery.setRecipientmobilephone(deliveryinfo.getRecipientmobilephone());
				orderDelivery.setRecipientaddress(deliveryinfo.getRecipientaddress());
				orderDelivery.setZip(deliveryinfo.getZip());
				orderDelivery.setModifytime(new Date());
				orderDelivery.setDeliveryaddressid(deliaddr.getId()); //test
				orderDelivery.setProviderid(order.getPrvid());   //供应商id
				//供应商信息
				INSBProvider provider = new INSBProvider();
				provider = insbProviderService.queryByPrvcode(order.getPrvid());
				orderDelivery.setProvidername(provider.getPrvshotname());//供应商名称简写
				orderDelivery.setCreatetime(new Date());         //创建时间 
				orderDelivery.setOperator(user.getUsercode());             //操作员
				orderDelivery.setTaskid(order.getTaskid());      //任务id
				orderDelivery.setOrderid(order.getId());      //订单orderno  id
				orderDelivery.setDeptcode(order.getDeptcode());  //出单网点
				orderDelivery.setDelivetype("1");       //配送方式，自取0或者 快递1
				orderDelivery.setDeliveryside("保网");           //默认保网   
				orderDelivery.setRecipientprovince(deliveryinfo.getProvince());
				orderDelivery.setRecipientcity(deliveryinfo.getCity());
				orderDelivery.setRecipientarea(deliveryinfo.getArea());
				insbOrderdeliveryService.updateById(orderDelivery);
			}else{
				//插入操作
//				orderDelivery.setDeliveryside(deliveryinfo.getDeliveryside());
				orderDelivery = new INSBOrderdelivery();
				orderDelivery.setRecipientname(deliveryinfo.getRecipientname());
				orderDelivery.setRecipientmobilephone(deliveryinfo.getRecipientmobilephone());
				orderDelivery.setRecipientaddress(deliveryinfo.getRecipientaddress());
				orderDelivery.setZip(deliveryinfo.getZip());
				orderDelivery.setCreatetime(new Date());
				orderDelivery.setDeliveryaddressid(deliaddr.getId()); //test
				orderDelivery.setProviderid(order.getPrvid());   //供应商id
				//供应商信息
				INSBProvider provider = new INSBProvider();
				provider = insbProviderService.queryByPrvcode(order.getPrvid());
				orderDelivery.setProvidername(provider.getPrvshotname());//供应商名称简写
				orderDelivery.setOperator(user.getUsercode());             //操作员code
				orderDelivery.setTaskid(order.getTaskid());      //任务id
				orderDelivery.setOrderid(order.getId());      //订单orderno  id
				orderDelivery.setDeptcode(order.getDeptcode());  //出单网点
				orderDelivery.setDelivetype("1");       //配送方式，自取0或者 快递1
				orderDelivery.setDeliveryside("保网");           //默认保网
				orderDelivery.setRecipientprovince(deliveryinfo.getProvince());
				orderDelivery.setRecipientcity(deliveryinfo.getCity());
				orderDelivery.setRecipientarea(deliveryinfo.getArea());
				insbOrderdeliveryService.insert(orderDelivery);
			}
			return "success";
		}catch(Exception e){
			throw new RuntimeException();
		}
		
	}

	@Override
	public List<String> getUnderwTrackStr(String subInstanceId) {
		return insbOrderDao.getQuoteBackTrackStr(subInstanceId);
	}

	@Override
	public String getUnderwResult(String taskid, String inscomcode) {
		return (String) redisClient.get(Constants.CM_ZZB, taskid+"@"+inscomcode+"@approved");
	}

	@Override
	public INSBOrder getOneOrderByTaskIdAndInscomcode(String taskid, String inscomcode)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("taskid",  taskid);
		map.put("inscomcode", inscomcode);
		return insbOrderDao.selectOrderByTaskId(map);
	}

	@Override
	public Page<INSBOrder> selectOrdersWithFairyQRPay(Date fromDateTime, Date toDateTime, Pageable pageable) {
		return insbOrderDao.selectOrdersWithFairyQRPay(fromDateTime, toDateTime, pageable);
	}
}