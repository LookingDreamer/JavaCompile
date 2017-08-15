package com.zzb.mobile.service.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCCodeService;
import com.cninsure.system.service.INSCDeptService;
import com.common.HttpClientUtil;
import com.common.redis.IRedisClient;
import com.zzb.chn.bean.Channel;
import com.zzb.chn.bean.PayInfo;
import com.zzb.chn.bean.QuoteBean;
import com.zzb.chn.util.JsonUtils;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.cm.entity.INSBApplicant;
import com.zzb.cm.entity.INSBApplicanthis;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.entity.INSBFlowerror;
import com.zzb.cm.entity.INSBFlowinfo;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBApplicantService;
import com.zzb.cm.service.INSBApplicanthisService;
import com.zzb.cm.service.INSBCarkindpriceService;
import com.zzb.cm.service.INSBFlowerrorService;
import com.zzb.cm.service.INSBFlowinfoService;
import com.zzb.cm.service.INSBOrderService;
import com.zzb.cm.service.INSBPersonService;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.cm.service.INSBQuotetotalinfoService;
import com.zzb.conf.dao.INSBPaychannelmanagerDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.entity.INSBPaychannelmanager;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBOrderpaymentService;
import com.zzb.conf.service.INSBPaychannelmanagerService;
import com.zzb.conf.service.INSBPolicyitemService;
import com.zzb.conf.service.INSBWorkTimeService;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.ldap.LdapMd5;
import com.zzb.mobile.dao.InsbpaymentpasswordDao;
import com.zzb.mobile.entity.AppPaymentyzf;
import com.zzb.mobile.entity.INSBPayResult;
import com.zzb.mobile.entity.Insbpaymentpassword;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.AppPaymentCallBackService;
import com.zzb.mobile.service.AppPaymentService;
import com.zzb.mobile.service.AppPaymentyzfService;
import com.zzb.mobile.service.INSBPayChannelService;
import com.zzb.mobile.service.INSBPayResultService;
import com.zzb.mobile.service.InsbpaymentpasswordService;
import com.zzb.mobile.service.InsbpaymenttransactionService;
import com.zzb.mobile.util.EncodeUtils;
import com.zzb.mobile.util.HttpClient;
import com.zzb.mobile.util.MappingType;
import com.zzb.mobile.util.PayConfigMappingMgr;
import com.zzb.mobile.util.PayStatus;

@Service
public class AppPaymentServiceImpl implements AppPaymentService{
    private static final String CHANNEL_PAYMENT = "cm:zzb:payment:channel_payment";
    private static Logger logger = Logger.getLogger(AppPaymentServiceImpl.class);
	private String[] fields ={"channelId","bizId","agentOrg"};

	@Resource 
	private INSBOrderpaymentService insbOrderpaymentService;
	@Resource 
	private INSBOrderService insbOrderService;
	@Resource
	private INSBQuotetotalinfoService insbQuotetotalinfoService;
	@Resource
	private INSBQuoteinfoService insbQuoteinfoService;
	@Resource 
	private INSCDeptService  inscDeptService;
	@Resource 
	private INSBPayResultService insbPayResultService;
	@Resource 
	private InsbpaymentpasswordService insbPaymentpasswordService;
	@Resource
	private InsbpaymentpasswordDao insbpaymentpasswordDao;
	@Resource
	private AppPaymentyzfService appPaymentyzfService;
	@Resource 
	private AppPaymentCallBackService appPaymentCallBackService;
	@Resource 
	private INSBPolicyitemService insbPolicyitemService;
	@Resource 
	private InterFaceService interFaceService;
	@Resource 
	private INSBCarkindpriceService insbCarkindpriceService;
	@Resource 
	private INSBPaychannelmanagerService insbPaychannelmanagerService;
	@Resource 
	private INSBPayChannelService insbPaychannelService;
	@Resource 
	private INSBFlowerrorService insbFlowerrorService;
	@Resource 
	private INSBFlowinfoService insbFlowinfoService;
	@Resource 
	private INSBAgentService insbAgentService;
	@Resource 
	private INSCCodeService inscCodeService;
	@Resource 
	private INSBWorkflowsubService insbWorkflowsubService;
	@Resource private INSBApplicantService insbApplicantService;
	@Resource private INSBApplicanthisService insbApplicanthisService;
	@Resource private INSBPersonService insbPersonService;
	@Resource private INSBPaychannelmanagerDao insbPaychannelmanagerDao;
	@Resource
	private InsbpaymenttransactionService insbPaymenttransactionService ;
	@Resource
    private HttpServletRequest request;
	@Resource
	private INSBWorkTimeService insbWorkTimeService;
	@Resource
    private INSBWorkflowmainService workflowmainService;
    @Resource
    private IRedisClient redisClient;

	/**
	 * 卡+支付密码支付  
	 */
	@Override
	public CommonModel pay(String jobNum, JSONObject payInfoJson,String password) {
		logger.info("进入pay1方法  jobNum="+jobNum+",password="+password+",payInfoJson="+payInfoJson.toString());
		if(StringUtils.isEmpty(password))
			return this.pay(jobNum, payInfoJson); 
		CommonModel model =new CommonModel();
		HashMap<String,Object> map =new HashMap<String,Object>();
		map.put("jobNum", jobNum);
		map.put("password", password);
		Insbpaymentpassword insbpaypwd=insbpaymentpasswordDao.selectOnebyMap(map);
		if(insbpaypwd!=null)
			return this.pay(jobNum, payInfoJson);
		model.setStatus("fail");
		model.setMessage("安全支付密码不正确！");
		logger.info("关闭pay1方法");
		return model;
	}
	
	@Override
	public CommonModel pay(String jobNum, JSONObject payInfoJson,String password,String bankCardInfoId) {
		logger.info("进入pay方法  jobNum="+jobNum+",password="+password+",bankCardInfoId="+bankCardInfoId+",payInfoJson="+payInfoJson.toString());
		if(bankCardInfoId==null || bankCardInfoId.trim().equals("")){
			return pay( jobNum,  payInfoJson, password);
		}else{
			CommonModel model = this.pipleBanKCardInfo(jobNum, payInfoJson, bankCardInfoId);
			if(model.getStatus().equals("success")){
				return this.pay(jobNum, (JSONObject)model.getBody());
			}else{
				return model;
			}
			
		}
			
			

	}
	/**
	 * 支付
	 */
	@Override
	public CommonModel pay(String jobNum,JSONObject payInfoJson) {
		String bizId = payInfoJson.getString("bizId");
		logger.info("进入pay2方法  bizId="+bizId+",param="+ payInfoJson);
		CommonModel model =new CommonModel();
		//验证是否是第一次支付
		this.validatePayment(bizId,payInfoJson);
		model = validate(payInfoJson.toString()); //验证参数关键字段是否存在
		if(model.getStatus().equals("fail"))
			return model;
		model = validateAgent(jobNum); //验证代理人信息是否存在
		if(model.getStatus().equals("fail"))
			return model;
		model = validateOrder(bizId); //验证订单是否存在，以及订单状态
		if(model.getStatus().equals("fail"))
			return model;
		INSBOrderpayment  pay =this.insbOrderpaymentService.selectBySerialNumber(bizId);
		//验证是否在业管上班时间
		INSBQuoteinfo insbQuoteinfo = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(pay.getSubinstanceid());
		/*Boolean inWorkTime = insbWorkTimeService.inWorkTime(new Date(), insbQuoteinfo.getDeptcode());
		if(!inWorkTime){
			model.setStatus("fail");
			model.setMessage("不在业管上班时间内");	
			this.payFlowErrorLog(bizId,model.getMessage());
			return model;
		}*/
		
		String channelId=PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_CHANNEL,payInfoJson.getString("channelId"));
		if(StringUtils.isEmpty(channelId)){
			model.setStatus("fail");
			model.setMessage("未找到匹配的支付渠道");
			this.payFlowErrorLog(bizId,model.getMessage());
			return model;
		}
		//判断订单是否可支付
		 
		HashMap<String, String> paramGet = new HashMap<String,String>();
		paramGet.put("processinstanceid", pay.getSubinstanceid());
		
		String doGet = HttpClientUtil.doGet(ValidateUtil.getConfigValue("workflow.url")+"/process/isCanPay", paramGet);
		logger.info("请求工作流是否可支付接口=== processinstanceid="+ pay.getSubinstanceid()+",url="+ValidateUtil.getConfigValue("workflow.url")+"/process/isCanPay");
		if(StringUtil.isEmpty(doGet)){
			model.setStatus("fail");
			model.setMessage("访问工作流出错");	
			this.payFlowErrorLog(bizId,model.getMessage());
			return model;
		}
		JSONObject doGetJson = JSONObject.fromObject(doGet);
		if(!doGetJson.containsKey("message")||!doGetJson.getBoolean("message")){
			model.setStatus("fail");
			model.setMessage("订单暂时无法支付");	
			this.payFlowErrorLog(bizId,model.getMessage());
			return model;
		}
		//验证同一订单下是否有一个保险公司已经支付或在支付中
		INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
		insbOrderpayment.setPaymentransaction(bizId);
		List<INSBOrderpayment> orderpaymentList = insbOrderpaymentService.queryList(insbOrderpayment);
		for (INSBOrderpayment insbOrderpayment2 : orderpaymentList) {
			/**
			 * 2016 -02-02 支付状态判断 ，应通过 支付渠道判断
			 */
//			if(PayStatus.PAYING.equals(insbOrderpayment2.getPayresult())&&!payInfoJson.getString("payType").equals("08")){
//				model.setStatus("fail");
//				model.setMessage("订单已经在支付中");	
//				return model;
//			}
			if(insbOrderpayment2.getPayflowno()!=null){
				CommonModel modelPay =queryPayResultFromPayPlat(insbOrderpayment2.getPayflowno());
				logger.info("pay2方法  bizId="+bizId+",查询支付结果接口  结果 ："+ modelPay.getStatus());
				if("success".equals(modelPay.getStatus())){
					model.setStatus("fail");
					model.setMessage("订单已经支付成功，不可重复支付");	
					this.payFlowErrorLog(bizId,model.getMessage());
					return model;
				}
			}
			if(PayStatus.PAYED.equals(insbOrderpayment2.getPayresult())){
				logger.info("pay2方法  bizId="+bizId+",订单已经有保险公司支付成功，不可重复支付");
				model.setStatus("fail");
				model.setMessage("订单已经有保险公司支付成功，不可重复支付");	
				this.payFlowErrorLog(bizId,model.getMessage());
				return model;
			}
		}
		INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();
		insbWorkflowsub.setMaininstanceid(orderpaymentList.get(0).getTaskid());
		List<INSBWorkflowsub> insbWorkflowsubList = insbWorkflowsubService.queryList(insbWorkflowsub);
		for (INSBWorkflowsub insbWorkflowsub2 : insbWorkflowsubList) {
			if("21".equals(insbWorkflowsub2.getTaskcode())){
				logger.info("pay2方法  bizId="+bizId+",Taskcode="+ insbWorkflowsub2.getTaskcode()+",订单已经支付完成，不可重复支付");
				model.setStatus("fail");
				model.setMessage("订单已经支付完成，不可重复支付");	
				this.payFlowErrorLog(bizId,model.getMessage());
				return model;
			}
		}
		payInfoJson.put("amount", String.valueOf(pay.getAmount()));
		payInfoJson.put("channelId", pay.getPaychannelid());
		INSBAgent insbAgent = new INSBAgent();
		insbAgent.setJobnum(jobNum);
		insbAgent = insbAgentService.queryOne(insbAgent);
		payInfoJson.put("agentOrg",insbAgent.getDeptid());
		this.updateOrderPayment(bizId,"","", "", PayStatus.PAYING, new Date());
		this.updateOrder(bizId, null, "06");//更改订单状态
		switch(channelId){
		case "payeco":
			model= payeco(payInfoJson.toString()); break;
		case "yeepay":
			model= yeepay(payInfoJson.toString());break;
		case "alipay":
			model= alipay(payInfoJson.toString());break;
		case "bestpay":
			model= bestpay(payInfoJson.toString());break;
		case "tenpay":
			model= tenpay(payInfoJson.toString());break;
		case "99bill":
			model= kqbill(payInfoJson.toString());break;
		case "testPay":
			model= testPay(bizId);break;
		case "huaan":
			model= huaan(payInfoJson.toString());break;
		case "axatp":
			model= axatp(payInfoJson.toString());break;
		case "alltrust":
			model= alltrust(payInfoJson.toString());break;
		default:model= null;
		}
		if(model==null||model.getStatus().equals("fail")){
			this.updateOrderPayment(bizId,null,null,null, PayStatus.ERROR, new Date());
			this.updateOrder(bizId, null, "05");//更改订单状态
			if(model!=null){
				this.payFlowErrorLog(bizId,model.getMessage());
			}
		}
		logger.info("关闭pay2方法");
		return model;
	}
	private void validatePayment(String bizId, JSONObject payInfoJson) {
		INSBOrderpayment payment = insbOrderpaymentService.selectBySerialNumber(bizId);
		if(!StringUtil.isEmpty(payment.getPayflowno())){
			INSBOrderpayment orderPayment = new INSBOrderpayment();
			orderPayment.setTaskid(payment.getTaskid());   //设置任务id
			orderPayment.setOperator(payment.getOperator());      //操作员
			orderPayment.setSubinstanceid(payment.getSubinstanceid());
			orderPayment.setOrderid(payment.getOrderid());         //订单表id
			orderPayment.setPaychannelid(payment.getPaychannelid()); //支付通道id
			orderPayment.setAmount(payment.getAmount());   
			orderPayment.setCurrencycode(payment.getCurrencycode());       //币种编码
			orderPayment.setPayresult(payment.getPayresult());         //支付状态：待支付-0，正在支付-1, 支付完成-2 等等
			orderPayment.setCreatetime(new Date());  //创建时间
			insbOrderpaymentService.insert(orderPayment);  //插入一条数据
			bizId=orderPayment.getPaymentransaction();
			payInfoJson.put("bizId", bizId);

			INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
			try {
				PropertyUtils.copyProperties(insbOrderpayment, orderPayment);
				insbOrderpayment.setId(UUIDUtils.random());
				insbOrderpayment.setModifytime(null);
				insbOrderpaymentService.insert(insbOrderpayment);
			} catch (Exception e) {
				LogUtil.error("bizid:" + orderPayment.getPaymentransaction() + ",支付接口轨迹拷贝出错1");
			}
		}
	}

	private CommonModel alltrust(String payInfoJson) {

		JSONObject obj = JSONObject.fromObject(payInfoJson);
		String bizId = obj.getString("bizId");
		logger.info("进入alltrust方法===:bizId="+bizId+",payInfoJson="+payInfoJson);
		INSBOrderpayment orderpayment = new INSBOrderpayment();
		orderpayment.setPaymentransaction(bizId);
		orderpayment = insbOrderpaymentService.queryOne(orderpayment);
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(orderpayment.getTaskid());
		insbPolicyitem.setRisktype("1");
		INSBQuoteinfo quoteinfo = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(orderpayment.getSubinstanceid());
		insbPolicyitem.setInscomcode(quoteinfo.getInscomcode());
		insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
		if(insbPolicyitem==null){
			insbPolicyitem = new INSBPolicyitem();
			insbPolicyitem.setTaskid(orderpayment.getTaskid());
			insbPolicyitem.setRisktype("0");
			insbPolicyitem.setInscomcode(quoteinfo.getInscomcode());
			insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
		}
		obj.put("bizId", insbPolicyitem.getProposalformno());//给支付平台传投保单号
		
		CommonModel model =new CommonModel();
		if(StringUtil.isEmpty(insbPolicyitem.getInscomcode())
				||!insbPolicyitem.getInscomcode().startsWith("2022")){
			model.setStatus("fail");
			model.setMessage("只有永诚保险公司才可以执行永诚链接支付");
			return model;
		}else{
			obj.put("insOrg", "2022");
			obj.remove("processInstanceId");
			obj.remove("prvid");
			payInfoJson=obj.toString();
		}
		obj = this.buildHuaanHeader(payInfoJson);
		obj = this.buildHuaanpayInfo(obj);
		/*logger.info("执行edi支付申请方法==bizId："+bizId+",taskid："+insbPolicyitem.getTaskid()+",inscomcode："+insbPolicyitem.getInscomcode());
		try {
			interFaceService.goToEdiQuote(insbPolicyitem.getTaskid(),insbPolicyitem.getInscomcode(),"admin","pay");
		} catch (Exception e) {
			e.printStackTrace();
		}
		INSBFlowinfo insbFlowinfo = new INSBFlowinfo();
		insbFlowinfo.setTaskid(insbPolicyitem.getTaskid());
		insbFlowinfo.setInscomcode(insbPolicyitem.getInscomcode());
		insbFlowinfo = insbFlowinfoService.queryOne(insbFlowinfo);
		if(insbFlowinfo==null||"18".equals(insbFlowinfo.getFlowcode())){
			model.setStatus("fail");
			model.setMessage("申请支付失败");
			return model;
		}*/
		String result = HttpClient.sendPost(obj.getString("bizId"),PayConfigMappingMgr.getPayUrl(), obj.toString());
		logger.info("请求支付平台===:bizId="+ bizId+",url="+PayConfigMappingMgr.getPayUrl()+",param="+obj.toString()+",结果="+result);
		if(!StringUtil.isEmpty(result)){
			try{
				JSONObject json =JSONObject.fromObject(result);
				if(json.containsKey("code") && /*json.containsKey("msg") &&*/ json.containsKey("bizTransactionId")){
					if(json.getString("code").equals("0000")){
						model.setStatus("success");						
						//model.setMessage(json.getString("msg"));
						model.setBody(json);
						this.updateOrderPayment(bizId, json.getString("bizTransactionId"), null, null, null, null);
					}else{
						model.setStatus("fail");
						//model.setMessage(json.getString("msg"));
						if(json.containsKey("payResultDesc")){
							model.setMessage(json.getString("payResultDesc"));
						}else{
							model.setMessage("支付失败！");
						}
						model.setBody(json);
					}
				}else{
					model.setStatus("fail");
					model.setMessage("系统出错！" + result);
				}
			}catch(Exception e){
				e.printStackTrace();
				model.setStatus("fail");
				model.setMessage("访问支付平台失败！" +result);
			}
		}else{
			model.setStatus("fail");
			model.setMessage("访问支付平台失败！");
		}
		logger.info("关闭alltrust方法===:bizId="+bizId);
		return model;
	}

	/**
	 * 
	 * @param 安盛天平快速支付
	 * @return
	 */
	private CommonModel axatp(String payInfoJson) {

		JSONObject obj = JSONObject.fromObject(payInfoJson);
		String bizId = obj.getString("bizId");
		logger.info("进入axatp方法===:bizId="+bizId+",payInfoJson="+payInfoJson);
		INSBOrderpayment orderpayment = new INSBOrderpayment();
		orderpayment.setPaymentransaction(bizId);
		orderpayment = insbOrderpaymentService.queryOne(orderpayment);
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(orderpayment.getTaskid());
		insbPolicyitem.setRisktype("1");
		INSBQuoteinfo quoteinfo = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(orderpayment.getSubinstanceid());
		insbPolicyitem.setInscomcode(quoteinfo.getInscomcode());
		insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
		if(insbPolicyitem==null){
			insbPolicyitem = new INSBPolicyitem();
			insbPolicyitem.setTaskid(orderpayment.getTaskid());
			insbPolicyitem.setRisktype("0");
			insbPolicyitem.setInscomcode(quoteinfo.getInscomcode());
			insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
		}
		obj.put("bizId", insbPolicyitem.getProposalformno());//给支付平台传投保单号
		
		CommonModel model =new CommonModel();
		if(StringUtil.isEmpty(insbPolicyitem.getInscomcode())
				||!insbPolicyitem.getInscomcode().startsWith("2026")){
			model.setStatus("fail");
			model.setMessage("只有安盛天平保险公司才可以执行安盛天平快速支付");
			return model;
		}else{
			obj.put("insOrg", "2026");
			payInfoJson=obj.toString();
		}
		if(!obj.containsKey("bizTransactionId")){
			obj = this.buildAxatpHeaderOne(obj);
			obj = this.buildAxatppayInfo(insbPolicyitem,obj);
			/*logger.info("执行edi支付申请方法==bizId："+bizId+",taskid："+insbPolicyitem.getTaskid()+",inscomcode："+insbPolicyitem.getInscomcode());
			try {
				interFaceService.goToEdiQuote(insbPolicyitem.getTaskid(),insbPolicyitem.getInscomcode(),"admin","pay");
			} catch (Exception e) {
				e.printStackTrace();
			}
			INSBFlowinfo insbFlowinfo = new INSBFlowinfo();
			insbFlowinfo.setTaskid(insbPolicyitem.getTaskid());
			insbFlowinfo.setInscomcode(insbPolicyitem.getInscomcode());
			insbFlowinfo = insbFlowinfoService.queryOne(insbFlowinfo);
			if(insbFlowinfo==null||"18".equals(insbFlowinfo.getFlowcode())){
				model.setStatus("fail");
				model.setMessage("申请支付失败");
				return model;
			}*/
		}else{
			obj = this.buildAxatpHeaderTwo(obj);
		}
		String result = HttpClient.sendPost(obj.getString("bizId"),PayConfigMappingMgr.getPayUrl(), obj.toString());
		logger.info("请求支付平台===:bizId="+ bizId+",url="+PayConfigMappingMgr.getPayUrl()+",param="+obj.toString()+",结果="+result);
		if(!StringUtil.isEmpty(result)){
			try{
				JSONObject json =JSONObject.fromObject(result);
				if(json.containsKey("code")){
					if(json.getString("code").equals("0000")){
						model.setStatus("success");						
						//model.setMessage(json.getString("msg"));
						model.setBody(json);
						if(json.containsKey("bizTransactionId"))
							this.updateOrderPayment(bizId, json.getString("bizTransactionId"), null, null, null, null);
					}else{
						model.setStatus("fail");
						//model.setMessage(json.getString("msg"));
						if(json.containsKey("payResultDesc")){
							model.setMessage(json.getString("payResultDesc"));
						}else{
							model.setMessage("支付失败！");
						}
						model.setBody(json);
					}
				}else{
					model.setStatus("fail");
					model.setMessage("系统出错！" + result);
				}
			}catch(Exception e){
				e.printStackTrace();
				model.setStatus("fail");
				model.setMessage("访问支付平台失败！" +result);
			}
		}else{
			model.setStatus("fail");
			model.setMessage("访问支付平台失败！");
		}
		logger.info("关闭axatp方法===:bizId="+bizId);
		return model;
	}

	private JSONObject buildAxatppayInfo(INSBPolicyitem insbPolicyitem,JSONObject jsonStr) {
		INSBPolicyitem insbPolicyitem1 = new INSBPolicyitem();
		insbPolicyitem1.setTaskid(insbPolicyitem.getTaskid());
		insbPolicyitem1.setInscomcode(insbPolicyitem.getInscomcode());
		JSONObject params = jsonStr.getJSONObject("channelParam");
		List<INSBPolicyitem> queryList = insbPolicyitemService.queryList(insbPolicyitem1);
		for (INSBPolicyitem insbPolicyitem2 : queryList) {
			if(params.get("applicantNo")!=null)
				params.put("applicantNo", params.get("applicantNo").toString()+","+insbPolicyitem2.getProposalformno());
			else
				params.put("applicantNo", insbPolicyitem2.getProposalformno());
		}
		jsonStr.put("channelParam", params);
		return jsonStr;
	}

	/**
	 * 
	 * @param 安盛天平快速支付，第二步
	 * @return
	 */
	private JSONObject buildAxatpHeaderTwo(JSONObject json) {
		String channleId = json.getString("channelId");
		String payType = json.getString("payType");
		json.put("channelId", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_CHANNEL, channleId));
		json.put("payType", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_TYPE, payType));
		json.remove("agentOrg");
		json.remove("amount");
		json.remove("insOrg");
		return json;
	}
	/**
	 * 
	 * @param 安盛天平快速支付，第一步
	 * @return
	 */
	private JSONObject buildAxatpHeaderOne(JSONObject json) {
		
		String channleId = json.getString("channelId");
		String payType = json.getString("payType");
		BigDecimal temp_amount = new BigDecimal(json.getString("amount")); 
		int amount   = temp_amount.multiply(new BigDecimal("100")).setScale (2,   BigDecimal.ROUND_DOWN).intValue();

		json.put("amount", amount);
		json.put("channelId", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_CHANNEL, channleId));
		json.put("payType", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_TYPE, payType));
		json.put("notifyUrl", "http://"+ValidateUtil.getConfigValue("localhost.ip")+":"+ValidateUtil.getConfigValue("localhost.port")+"/"+ValidateUtil.getConfigValue("localhost.projectName")+"/mobile/pay/callback");
		json.put("notifyType", "POST");
		json.put("paySource",PayConfigMappingMgr.getPaySource());
		String deptcode = json.getString("agentOrg");
		INSCDept legalDept = inscDeptService.getLegalPersonDept(deptcode);
		json.put("agentOrg", legalDept.getUpcomcode());
		return json;
	}

	private CommonModel huaan(String payInfoJson) {

		JSONObject obj = JSONObject.fromObject(payInfoJson);
		String bizId = obj.getString("bizId");
		logger.info("进入huaan方法===:bizId="+bizId+",payInfoJson="+payInfoJson);
		INSBOrderpayment orderpayment = new INSBOrderpayment();
		orderpayment.setPaymentransaction(bizId);
		orderpayment = insbOrderpaymentService.queryOne(orderpayment);
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(orderpayment.getTaskid());
		insbPolicyitem.setRisktype("1");
		INSBQuoteinfo quoteinfo = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(orderpayment.getSubinstanceid());
		insbPolicyitem.setInscomcode(quoteinfo.getInscomcode());
		insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
		if(insbPolicyitem==null){
			insbPolicyitem = new INSBPolicyitem();
			insbPolicyitem.setTaskid(orderpayment.getTaskid());
			insbPolicyitem.setRisktype("0");
			insbPolicyitem.setInscomcode(quoteinfo.getInscomcode());
			insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
		}
		obj.put("bizId", insbPolicyitem.getProposalformno());//给支付平台传投保单号
		
		CommonModel model =new CommonModel();
		if(StringUtil.isEmpty(insbPolicyitem.getInscomcode())
				||!insbPolicyitem.getInscomcode().startsWith("2043")){
			model.setStatus("fail");
			model.setMessage("只有华安保险公司才可以执行华安支付");
			return model;
		}else{
			obj.put("insOrg", "2043");
			obj.remove("processInstanceId");
			obj.remove("prvid");
			payInfoJson=obj.toString();
		}
		obj = this.buildHuaanHeader(payInfoJson);
		obj = this.buildHuaanpayInfo(obj);
		/*logger.info("执行edi支付申请方法==bizId："+bizId+",taskid："+insbPolicyitem.getTaskid()+",inscomcode："+insbPolicyitem.getInscomcode());
		try {
			interFaceService.goToEdiQuote(insbPolicyitem.getTaskid(),insbPolicyitem.getInscomcode(),"admin","pay");
		} catch (Exception e) {
			e.printStackTrace();
		}
		INSBFlowinfo insbFlowinfo = new INSBFlowinfo();
		insbFlowinfo.setTaskid(insbPolicyitem.getTaskid());
		insbFlowinfo.setInscomcode(insbPolicyitem.getInscomcode());
		insbFlowinfo = insbFlowinfoService.queryOne(insbFlowinfo);
		if(insbFlowinfo==null||"18".equals(insbFlowinfo.getFlowcode())){
			model.setStatus("fail");
			model.setMessage("申请支付失败");
			return model;
		}*/
		String result = HttpClient.sendPost(obj.getString("bizId"),PayConfigMappingMgr.getPayUrl(), obj.toString());
		logger.info("请求支付平台===:bizId="+ bizId+",url="+PayConfigMappingMgr.getPayUrl()+",param="+obj.toString()+",结果="+result);
		if(!StringUtil.isEmpty(result)){
			try{
				JSONObject json =JSONObject.fromObject(result);
				if(json.containsKey("code") && /*json.containsKey("msg") &&*/ json.containsKey("bizTransactionId")){
					if(json.getString("code").equals("0000")){
						model.setStatus("success");						
						//model.setMessage(json.getString("msg"));
						model.setBody(json);
						this.updateOrderPayment(bizId, json.getString("bizTransactionId"), null, null, null, null);
					}else{
						model.setStatus("fail");
						//model.setMessage(json.getString("msg"));
						if(json.containsKey("payResultDesc")){
							model.setMessage(json.getString("payResultDesc"));
						}else{
							model.setMessage("支付失败！");
						}
						model.setBody(json);
					}
				}else{
					model.setStatus("fail");
					model.setMessage("系统出错！" + result);
				}
			}catch(Exception e){
				e.printStackTrace();
				model.setStatus("fail");
				model.setMessage("访问支付平台失败！" +result);
			}
		}else{
			model.setStatus("fail");
			model.setMessage("访问支付平台失败！");
		}
		logger.info("关闭huaan方法===:bizId="+bizId);
		return model;
	}

	private JSONObject buildHuaanpayInfo(JSONObject jsonStr) {
		Map<String, Object> params = new HashMap<String, Object>();
		String proposalformno = jsonStr.getString("bizId");//投保单号
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setProposalformno(proposalformno);
		insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
		String idCardName=insbPolicyitem.getApplicantname();
		INSCDept inscDept = inscDeptService.queryById(jsonStr.getString("agentOrg"));
		String startDate = DateUtil.toString(insbPolicyitem.getStartdate(), "yyyy-MM-dd");
		params.put("idCardName", idCardName);
		params.put("startDate", startDate);
		params.put("departCode", StringUtil.isEmpty(inscDept.getCity())?inscDept.getProvince():inscDept.getCity());
		INSBPolicyitem insbPolicyitem1 = new INSBPolicyitem();
		insbPolicyitem1.setTaskid(insbPolicyitem.getTaskid());
		insbPolicyitem1.setInscomcode(insbPolicyitem.getInscomcode());
		List<INSBPolicyitem> queryList = insbPolicyitemService.queryList(insbPolicyitem1);
		for (INSBPolicyitem insbPolicyitem2 : queryList) {
			if(params.get("applicantNo")!=null)
				params.put("applicantNo", params.get("applicantNo").toString()+","+insbPolicyitem2.getProposalformno());
			else
				params.put("applicantNo", insbPolicyitem2.getProposalformno());
		}
		jsonStr.put("channelParam", params);
		return jsonStr;
	}

	public CommonModel pipleBanKCardInfo(String jobNum,JSONObject payInfoJson,String bankCardInfoId){
		String channelId=PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_CHANNEL,payInfoJson.getString("channelId"));
		CommonModel model =new CommonModel();
		if(channelId.equals("bestpay")){
			try{
				model = this.validateAgent(jobNum);
				if(model.getStatus().equals("fail"))
					return model;
				INSBAgent agent = (INSBAgent)model.getBody();
				AppPaymentyzf yzf = appPaymentyzfService.queryById(bankCardInfoId);
				if(!agent.getName().equals(yzf.getName())){
					model.setStatus("fail");
					model.setMessage("银行卡与代理人姓名不一致！");
					return model;
				}
				JSONObject params = payInfoJson.getJSONObject("channelParam");
				params.put("bankCardNo", yzf.getBankcardno());
				params.put("bankCardType", yzf.getBankcardType());
				params.put("bankCardMobile", yzf.getPhone());
				params.put("bankArea", yzf.getKhcitycode());
				params.put("bankCode", yzf.getFkbankcode());
				params.put("bankName", yzf.getFkbankname());
				params.put("idCardName", yzf.getName());
				params.put("idCardNo", yzf.getIdcardno());
				params.put("idCardType", yzf.getIdcradType());
				params.put("idCardAddress", yzf.getRelationaddress());
				if(!"cashcardflag".equals(yzf.getBankcardType())){
					params.put("creditValidTime", yzf.getPeriodyear());
					params.put("creditValidCode", yzf.getValiadatecode());
				}
				payInfoJson.put("channelParam", params);
				model.setStatus("success");
				model.setMessage("加载银行卡成功！");
				model.setBody(payInfoJson);
				return model;
			}catch(Exception e){
				model.setStatus("fail");
				model.setMessage("查询翼支付签约信息失败！" + e.getMessage());
				return model;
			}
		}
		return null;
	}
	/**
	 * 验证代理人是否存在
	 * @param jobNum -工号
	 * @return
	 */
	private CommonModel validateAgent(String jobNum) {
		INSBAgent temp =new INSBAgent();
		CommonModel model =new CommonModel();
		temp.setJobnum(jobNum);
		INSBAgent agent = insbAgentService.queryOne(temp);
		if(agent==null){
			temp =new INSBAgent();
			temp.setTempjobnum(jobNum);
			agent = insbAgentService.queryOne(temp);
		}
		if(agent==null){
			model.setStatus("fail");
			model.setMessage("用户不存在！");
		}else{
			model.setStatus("success");
			model.setMessage("用户查询成功！");
			model.setBody(agent);
		}
		return model;
	}
	/**
	 * 验证报文格式是否正确
	 * @param payInfoJson -支付报json文字符串
	 * @return
	 */
	private CommonModel validate(String payInfoJson){
		JSONObject payInfo = JSONObject.fromObject(payInfoJson);
		StringBuffer sb = new StringBuffer();
		CommonModel model =new CommonModel();
		for(String s:fields){
			if(payInfo.containsKey(s))
				continue;
			else
				sb.append(s).append(",");
		}
		if(sb.toString().length()>1){
			model.setStatus("fail");
			model.setMessage("缺少以下支付必要信息："+sb.toString());
		}else{
			model.setStatus("success");
			model.setMessage("验证通过！");	
		}
		return model;
	}
	/**
	 * 验证订单是否存在以及订单状态
	 * @param bizId -支付流水号
	 * @return
	 */
	private CommonModel validateOrder( String bizId) {
		CommonModel model =new CommonModel();
		INSBOrderpayment payment = insbOrderpaymentService.selectBySerialNumber(bizId);
		
		if(payment==null){
			model.setStatus("fail");
			model.setMessage("订单不存在！");
		}else{
			if(!insbOrderpaymentService.validateOrder(payment)){
				model.setStatus("fail");
				model.setMessage("信息已经被篡改！");	
				return model;
			}
			if(payment.getPayresult().equals("02")){
				model.setStatus("fail");
				model.setMessage("已支付成功，不能重复支付！");
				this.queryPayResult(bizId);
			}else{
				model.setStatus("success");
				model.setMessage("订单查询成功！");
			}
		}
		return model;
	}
	/**
	 * 易联支付
	 * @param payInfoJson -支付报文
	 * @return
	 */
	private CommonModel payeco(String payInfoJson){
		JSONObject obj = JSONObject.fromObject(payInfoJson);
		String bizId = obj.getString("bizId");
		logger.info("进入payeco方法===:bizId="+bizId+",payInfoJson="+payInfoJson);
		INSBOrderpayment orderpayment = new INSBOrderpayment();
		orderpayment.setPaymentransaction(bizId);
		orderpayment = insbOrderpaymentService.queryOne(orderpayment);
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(orderpayment.getTaskid());
		insbPolicyitem.setRisktype("1");
		INSBQuoteinfo quoteinfo = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(orderpayment.getSubinstanceid());
		insbPolicyitem.setInscomcode(quoteinfo.getInscomcode());
		insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
		if(insbPolicyitem==null){
			insbPolicyitem = new INSBPolicyitem();
			insbPolicyitem.setTaskid(orderpayment.getTaskid());
			insbPolicyitem.setRisktype("0");
			insbPolicyitem.setInscomcode(quoteinfo.getInscomcode());
			insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
		}
		obj.put("bizId", insbPolicyitem.getProposalformno());
		obj = this.buildHeader(obj);
		obj = this.buildPayecobankInfo(obj);
		CommonModel model =new CommonModel();
		/*if(!this.ediQuery(bizId)){
			model.setStatus("fail");
			model.setMessage("申请支付失败");
			return model;
		}*/
		String result = HttpClient.sendPost(obj.getString("bizId"),PayConfigMappingMgr.getPayUrl(), obj.toString());
		logger.info("请求支付平台===:bizId="+bizId+",url="+PayConfigMappingMgr.getPayUrl()+",param="+obj.toString()+",结果="+result);
		if(!StringUtil.isEmpty(result)){
			try{
				JSONObject json =JSONObject.fromObject(result);
				if(json.containsKey("code") && json.containsKey("msg") && json.containsKey("bizTransactionId")){
					if(json.getString("code").equals("0000")){
						model.setStatus("success");						
						model.setMessage(json.getString("msg"));
						model.setBody(json);
						Date transDate=null;
						try {
							transDate = DateUtil.parse(json.getString("transDate"),"yyyy-MM-dd HH:mm:ss");
						} catch (Exception e) {
							e.printStackTrace();
						}
						this.updateOrderPayment(bizId, json.getString("bizTransactionId"), null, json.getString("paySerialNo"), json.getString("payResult"), transDate);
					}else{
						model.setStatus("fail");
						model.setMessage(json.getString("msg"));
						if(json.containsKey("payResultDesc")){
							model.setMessage(json.getString("payResultDesc"));
						}else{
							model.setMessage("支付失败！");
						}
						model.setBody(json);
					}
				}else{
					model.setStatus("fail");
					model.setMessage("系统出错！" + result);
				}
			}catch(Exception e){
				e.printStackTrace();
				model.setStatus("fail");
				model.setMessage("访问支付平台失败！" +result);
			}
		}else{
			model.setStatus("fail");
			model.setMessage("访问支付平台失败！");
		}
		logger.info("关闭payeco方法===:bizId="+bizId);
		return model;
	}
	
	

	private CommonModel yeepay(String payInfoJson){
		return null;
	}
	/**
	 * 支付宝
	 * @param payInfoJson
	 * @return
	 */
	private CommonModel alipay(String payInfoJson){
		return null;
	}
	/**
	 * 翼支付
	 * @param payInfoJson
	 * @return
	 */
	private CommonModel bestpay(String payInfoJson){
		JSONObject obj = JSONObject.fromObject(payInfoJson);
		String bizId = obj.getString("bizId");
		logger.info("进入bestpay方法===:bizId="+bizId+",payInfoJson="+payInfoJson);
		INSBOrderpayment orderpayment = new INSBOrderpayment();
		orderpayment.setPaymentransaction(bizId);
		orderpayment = insbOrderpaymentService.queryOne(orderpayment);
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(orderpayment.getTaskid());
		insbPolicyitem.setRisktype("1");
		INSBQuoteinfo quoteinfo = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(orderpayment.getSubinstanceid());
		insbPolicyitem.setInscomcode(quoteinfo.getInscomcode());
		insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
		if(insbPolicyitem==null){
			insbPolicyitem = new INSBPolicyitem();
			insbPolicyitem.setTaskid(orderpayment.getTaskid());
			insbPolicyitem.setRisktype("0");
			insbPolicyitem.setInscomcode(quoteinfo.getInscomcode());
			insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
		}
		obj.put("bizId", insbPolicyitem.getProposalformno());
		obj = this.buildHeader(obj);
		obj = this.buildBestpaybankInfo(obj);
		CommonModel model =new CommonModel();
		/*if(!this.ediQuery(bizId)){
			model.setStatus("fail");
			model.setMessage("申请支付失败");
			return model;
		}*/
		String result = HttpClient.sendPost(obj.getString("bizId"),PayConfigMappingMgr.getPayUrl(), obj.toString());
		logger.info("请求支付平台===:bizId="+ bizId+",url="+PayConfigMappingMgr.getPayUrl()+",param="+obj.toString()+",结果="+result);
		try{
			if(result==null){
				model.setStatus("fail");
				model.setMessage("访问支付平台失败！");
			}
			JSONObject json =JSONObject.fromObject(result);
			if(json.containsKey("code") && json.containsKey("msg") ){
				if(json.getString("code").equals("0000")){
					model.setStatus("success");						
					model.setMessage(json.getString("msg"));
					model.setBody(json);
					Date transDate=null;
					try {
						transDate = DateUtil.parse(json.getString("transDate"),"yyyy-MM-dd HH:mm:ss");
					} catch (Exception e) {
						e.printStackTrace();
					}
					this.updateOrderPayment(bizId, json.getString("bizTransactionId"), null, json.getString("paySerialNo"), json.getString("payResult"), transDate);
				}else{
					model.setStatus("fail");
					model.setMessage(json.getString("msg"));
					if(json.containsKey("payResultDesc")){
						model.setMessage(json.getString("payResultDesc"));
					}else{
						model.setMessage("支付失败！");
					}
					model.setBody(json);
				}
			}else{
				model.setStatus("fail");
				model.setMessage("系统出错！" +result);
			}
		}catch(Exception e){
			model.setStatus("fail");
			model.setMessage("访问支付平台失败！" +result);
		}
		logger.info("关闭bestpay方法===:bizId="+bizId);
		return model;
	}
	
	private CommonModel tenpay(String payInfoJson){
		return null;
	}
	/**
	 * 查询支付结果
	 * @param bizTransactionId
	 * @return
	 * @throws IOException
	 */
	private CommonModel queryResult(String bizId){
		logger.info("进入queryResult方法=== bizId:"+bizId);
		CommonModel model =new CommonModel();
		//首先查询订单状态
		INSBOrderpayment payment = insbOrderpaymentService.selectBySerialNumber(bizId);
		if(payment!=null && payment.getPayresult().equals(PayStatus.PAYED)){
			model.setStatus("success");
			model.setMessage("已支付");
			return model;
		}else if(payment!=null && payment.getPayresult().equals(PayStatus.ERROR)){
			model.setStatus("fail");
			model.setMessage("支付失败");
			return model;
		}
		//再查询回调信息，如果回调信息存在也认为是支付成功
		INSBPayResult insbPayResult = new INSBPayResult();
		insbPayResult.setBizId(bizId);
		if(!StringUtil.isEmpty(payment.getPayflowno()))
			insbPayResult.setPayflowno(payment.getPayflowno());
		List<INSBPayResult> queryList = insbPayResultService.queryList(insbPayResult);
		if(queryList.size()>0&&"02".equals(queryList.get(0).getOrderState())){
			model.setStatus("success");
			model.setMessage("已支付");
			return model;
		}
		if(StringUtil.isEmpty(payment.getPayflowno())){
			model.setStatus("fail");
			model.setMessage("订单未支付，请支付之后再试");
			return model;
		}
		model = this.queryPayResultFromPayPlat(payment.getPayflowno());
		if(model.getStatus().equals("success")){
			CommonModel model2 = this.completeTask(payment.getPaymentransaction());
			if(model2.getStatus().equals("success")){
				model.setBody(model.getBody());
				JSONObject json = (JSONObject)model.getBody();
				payment=this.updateOrderPayment(payment.getPaymentransaction(),payment.getPayflowno(),null, json.getString("paySerialNo"), PayStatus.PAYED, new Date());
				this.updateOrder(bizId, "2", "04");//更改订单状态
			}else{
				model.setMessage(model.getMessage()+"-"+model2.getMessage());
			}
		}

		logger.info("关闭queryResult方法=== bizId:"+bizId);
		return model;
	}
	private Date str2Date(String date){
		DateFormat df =new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
		try {
			return df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @param payInfoJson
	 * @return
	 */
	private CommonModel kqbill(String payInfoJson){
		JSONObject obj = JSONObject.fromObject(payInfoJson);
		String bizId = obj.getString("bizId");
		logger.info("进入kqbill方法===:bizId="+bizId+",payInfoJson="+payInfoJson);
		INSBOrderpayment orderpayment = new INSBOrderpayment();
		orderpayment.setPaymentransaction(bizId);
		orderpayment = insbOrderpaymentService.queryOne(orderpayment);
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(orderpayment.getTaskid());
		insbPolicyitem.setRisktype("1");
		INSBQuoteinfo quoteinfo = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(orderpayment.getSubinstanceid());
		insbPolicyitem.setInscomcode(quoteinfo.getInscomcode());
		insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
		if(insbPolicyitem==null){
			insbPolicyitem = new INSBPolicyitem();
			insbPolicyitem.setTaskid(orderpayment.getTaskid());
			insbPolicyitem.setRisktype("0");
			insbPolicyitem.setInscomcode(quoteinfo.getInscomcode());
			insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
		}
		obj.put("bizId", insbPolicyitem.getProposalformno());
		
		String company = insbPolicyitem.getInscomcode().substring(0, 4);
		String strChannelId = null;
		switch(company){
			case "2016":
				strChannelId = "11";
				break;
			case "2005":
				strChannelId = "10";
				break;
			case "2088":
				strChannelId = "12";
				break;
			case "2044":
				strChannelId = "13";
				break;
		}
		if (strChannelId != null) {
			INSBPaychannelmanager insbPaychannelmanager = new INSBPaychannelmanager();
			insbPaychannelmanager.setProviderid(quoteinfo.getInscomcode());
			insbPaychannelmanager.setDeptid(quoteinfo.getDeptcode());
			insbPaychannelmanager.setPaychannelid(orderpayment.getPaychannelid());
			List<INSBPaychannelmanager> pcmList = insbPaychannelmanagerService.queryList(insbPaychannelmanager);
			if (pcmList.size()!=0&&"1".equals(pcmList.get(0).getCollectiontype())) {
				obj.put("channelId", strChannelId);
			}
		}
		
		
		obj = this.kqbillbuildHeader(obj);
		obj = this.buildKqbillpayInfo(insbPolicyitem,obj);
		CommonModel model =new CommonModel();
		/*if(!this.ediQuery(bizId)){
			model.setStatus("fail");
			model.setMessage("申请支付失败");
			return model;
		}*/
		String result = HttpClient.sendPost(obj.getString("bizId"),PayConfigMappingMgr.getPayUrl(), obj.toString());
		logger.info("请求支付平台===:bizId="+ bizId+",url="+PayConfigMappingMgr.getPayUrl()+",param="+obj.toString()+",结果="+result);
		try{
			if(result==null){
				model.setStatus("fail");
				model.setMessage("访问支付平台失败！");
			}
			JSONObject json =JSONObject.fromObject(result);
			if(json.containsKey("code")){
				if(json.getString("code").equals("0000")){
					model.setStatus("success");						
					model.setMessage("支付请求已经发起，请进入快钱支付页面");
					model.setBody(json);
					this.updateOrderPayment(bizId, json.getString("bizTransactionId"), null, null, null, null);
				}else{
					model.setStatus("fail");
					model.setMessage(json.getString("msg"));
					if(json.containsKey("payResultDesc")){
						model.setMessage(json.getString("payResultDesc"));
					}else{
						model.setMessage("支付失败！");
					}
					model.setBody(json);
				}
			}else{
				model.setStatus("fail");
				model.setMessage("系统出错！" +result);
			}
		}catch(Exception e){
			model.setStatus("fail");
			model.setMessage("访问支付平台失败！" +result);
		}
		logger.info("关闭kqbill方法===:bizId="+bizId);
		return model;
	}
	
	private JSONObject buildKqbillpayInfo(INSBPolicyitem insbPolicyitem,JSONObject jsonStr) {
		logger.info("开始 buildKqbillpayInfo，jsonStr="+jsonStr);
		Map<String, Object> params = new HashMap<String, Object>();

		if(insbPolicyitem.getInscomcode().startsWith("2016")
				||insbPolicyitem.getInscomcode().startsWith("2088")
				||insbPolicyitem.getInscomcode().startsWith("2005")
				||insbPolicyitem.getInscomcode().startsWith("2044")){
			INSBPolicyitem insbPolicyitem1 = new INSBPolicyitem();
			insbPolicyitem1.setTaskid(insbPolicyitem.getTaskid());
			insbPolicyitem1.setInscomcode(insbPolicyitem.getInscomcode());
			List<INSBPolicyitem> queryList = insbPolicyitemService.queryList(insbPolicyitem1);
			for (INSBPolicyitem insbPolicyitem2 : queryList) {
				if(params.get("applicantNo")!=null)
					params.put("applicantNo", params.get("applicantNo").toString()+","+insbPolicyitem2.getProposalformno());
				else
					params.put("applicantNo", insbPolicyitem2.getProposalformno());
			}
		}
		if(insbPolicyitem.getInscomcode().startsWith("2016")
				||insbPolicyitem.getInscomcode().startsWith("2088")){
			INSBCarkindprice queryInsbCarkindprice = new INSBCarkindprice();
			queryInsbCarkindprice.setTaskid(insbPolicyitem.getTaskid());
			queryInsbCarkindprice.setInscomcode(insbPolicyitem.getInscomcode());
			List<INSBCarkindprice> resultInsbCarkindpriceList = insbCarkindpriceService.queryList(queryInsbCarkindprice);
			BigDecimal forcePremium=new BigDecimal("0");
			BigDecimal businessPremium=new BigDecimal("0");
			BigDecimal taxPremium=new BigDecimal("0");
			for(INSBCarkindprice dataInsbCarkindprice : resultInsbCarkindpriceList){
				BigDecimal bigDecimal = new BigDecimal(String.valueOf(dataInsbCarkindprice.getDiscountCharge()==null?0:dataInsbCarkindprice.getDiscountCharge()));
				if(dataInsbCarkindprice.getInskindtype().equals("0")
						||dataInsbCarkindprice.getInskindtype().equals("1")){
					businessPremium=businessPremium.add(bigDecimal);
				}
				if(dataInsbCarkindprice.getInskindtype().equals("2")){
					forcePremium=forcePremium.add(bigDecimal);
				}
				if(dataInsbCarkindprice.getInskindtype().equals("3")){
					taxPremium=taxPremium.add(bigDecimal);
				}
			}
			params.put("forcePremium", forcePremium.multiply(new BigDecimal("100")).setScale (2,   BigDecimal.ROUND_DOWN).intValue());//交强险总保费
			params.put("businessPremium", businessPremium.multiply(new BigDecimal("100")).setScale (2,   BigDecimal.ROUND_DOWN).intValue());//商业险总保费
			if(insbPolicyitem.getInscomcode().startsWith("2088")){
				params.put("taxPremium", taxPremium.multiply(new BigDecimal("100")).setScale (2,   BigDecimal.ROUND_DOWN).intValue());//车船税保费
			}
		}
		if(insbPolicyitem.getInscomcode().startsWith("2016")){
			params.put("applicantNo", insbPolicyitem.getProposalformno());
		}
		INSBQuoteinfo insbQuoteinfo=insbQuoteinfoService.getByTaskidAndCompanyid(insbPolicyitem.getTaskid(), insbPolicyitem.getInscomcode());
		if(insbPolicyitem.getInscomcode().startsWith("2005")){
			params.put("outDept", insbQuoteinfo.getDeptcode());
		}

		Map<String, String> param = new HashMap<String, String>();
		param.put("deptid", insbQuoteinfo.getDeptcode());
		param.put("providerid", insbPolicyitem.getInscomcode());
		INSBPaychannelmanager queryManager = insbPaychannelmanagerDao.queryManager(param);
		params.put("merchantNo", queryManager==null?"":queryManager.getSettlementno());//结算商户号
		jsonStr.put("channelParam", params);

		logger.info("结束 buildKqbillpayInfo，jsonStr="+jsonStr);
		return jsonStr;
	}

	private JSONObject kqbillbuildHeader(JSONObject json) {
		logger.info("开始 kqbillbuildHeader，json="+json);
		
		String channleId = json.getString("channelId");
		String payType = json.getString("payType");
		BigDecimal temp_amount = new BigDecimal(json.getString("amount")); 
		int amount   = temp_amount.multiply(new BigDecimal("100")).setScale (2,   BigDecimal.ROUND_DOWN).intValue();

		json.put("amount", amount);
		json.put("channelId", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_CHANNEL, channleId));
		json.put("payType", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_TYPE, payType));
		json.put("notifyUrl", "http://"+ValidateUtil.getConfigValue("localhost.ip")+":"+ValidateUtil.getConfigValue("localhost.port")+"/"+ValidateUtil.getConfigValue("localhost.projectName")+"/mobile/pay/callback");
		json.put("notifyType", "POST");
		json.put("paySource",PayConfigMappingMgr.getPaySource());
		String deptcode = json.getString("agentOrg");
		INSCDept legalDept = inscDeptService.getLegalPersonDept(deptcode);
		json.put("agentOrg", legalDept.getUpcomcode());
		INSCDept dept = inscDeptService.queryById(deptcode);
		json.put("areaCode",dept.getProvince());
		logger.info("结束 kqbillbuildHeader，json="+json);
		return json;
	} 

	/**
	 * 生成支付头信息
	 * @param payInfoJson
	 * @return
	 */
	private JSONObject buildHeader(JSONObject payInfoJson) {
		JSONObject json =JSONObject.fromObject(payInfoJson);
		String channleId = json.getString("channelId");
		//String channelName = json.getString("channelName");
		String payType = json.getString("payType");
		BigDecimal temp_amount = new BigDecimal(json.getString("amount")); 
		int amount   = temp_amount.multiply(new BigDecimal("100")).setScale (2,   BigDecimal.ROUND_DOWN).intValue();  

		json.put("amount", amount);
		json.put("channelId", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_CHANNEL, channleId));
//		json.put("channelName", PayConfigMappingMgr.getPayNameByCmCode(MappingType.PAY_CHANNEL, channleId));
		json.put("payType", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_TYPE, payType));
//		json.put("payTypeDesc", PayConfigMappingMgr.getPayNameByCmCode(MappingType.PAY_TYPE, payType));
		json.put("notifyUrl", "http://"+ValidateUtil.getConfigValue("localhost.ip")+":"+ValidateUtil.getConfigValue("localhost.port")+"/"+ValidateUtil.getConfigValue("localhost.projectName")+"/mobile/pay/callback");
		json.put("notifyType", "POST");
		json.put("paySource",PayConfigMappingMgr.getPaySource());
		String deptcode = json.getString("agentOrg");
		INSCDept legalDept = inscDeptService.getLegalPersonDept(deptcode);
		json.put("agentOrg", legalDept.getUpcomcode());
		String key = EncodeUtils.SignPayInfo(json);
		json.put("sign", key);
		return json;
	}
	/**
	 * 生成华安支付头信息
	 * @param payInfoJson
	 * @return
	 */
	private JSONObject buildHuaanHeader(String payInfoJson) {
		JSONObject json =JSONObject.fromObject(payInfoJson);
		String channleId = json.getString("channelId");
		//String channelName = json.getString("channelName");
		String payType = json.getString("payType");
		BigDecimal temp_amount = new BigDecimal(json.getString("amount")); 
		int amount   = temp_amount.multiply(new BigDecimal("100")).setScale (2,   BigDecimal.ROUND_DOWN).intValue(); 

		INSCDept legalDept = inscDeptService.getLegalPersonDept(json.getString("agentOrg"));
		json.put("agentOrg", legalDept.getUpcomcode());
		json.put("amount", amount);
		json.put("channelId", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_CHANNEL, channleId));
		//json.put("channelName", PayConfigMappingMgr.getPayNameByCmCode(MappingType.PAY_CHANNEL, channleId));
		json.put("payType", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_TYPE, payType));
		//json.put("payTypeDesc", PayConfigMappingMgr.getPayNameByCmCode(MappingType.PAY_TYPE, payType));
		json.put("notifyUrl", "http://"+ValidateUtil.getConfigValue("localhost.ip")+":"+ValidateUtil.getConfigValue("localhost.port")+"/"+ValidateUtil.getConfigValue("localhost.projectName")+"/mobile/pay/callback");
		json.put("notifyType", "POST");
		json.put("paySource",PayConfigMappingMgr.getPaySource());
		String key = EncodeUtils.SignPayInfo(json);
		json.put("sign", key);
		return json;
	}
	/**
	 * 易连支付银行卡和身份证信息
	 * @param jsonStr
	 * @return
	 */
	private JSONObject buildPayecobankInfo(JSONObject jsonStr) {
		JSONObject params = jsonStr.getJSONObject("channelParam");
		String bankCardType=params.getString("bankCardType");
		String idCardType = params.getString("idCardType");
		params.put("bankCardType", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.BANK_TYPE, bankCardType));
		params.put("idCardType", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.CARD_TYPE, idCardType));
		jsonStr.put("channelParam", params);
		return jsonStr;
	}
	private JSONObject buildBestpaybankInfo(JSONObject jsonStr) {
		JSONObject params = jsonStr.getJSONObject("channelParam");
		String bankCardType=params.getString("bankCardType");
		String idCardType = params.getString("idCardType");
		String bankCode = params.getString("bankCode");
		params.put("bankCardType", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.BANK_TYPE, bankCardType));
		params.put("bankCode", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.BANK_CODE, bankCode));
		params.put("bankName", PayConfigMappingMgr.getPayNameByCmCode(MappingType.BANK_CODE, bankCode));
		params.put("idCardType", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.CARD_TYPE, idCardType));
		jsonStr.put("channelParam", params);
		return jsonStr;
	}
	
	@Override
	public CommonModel queryPayResult(String bizId) {
		return this.queryResult(bizId);
	}

	@Override
	public CommonModel getPaymentChannel() {

		return null;
	}
	

	@Override
	public CommonModel getPaymentBankInfo(String jobNum) {
		return null;
	}
	/**
	 * 提交工作流
	 */
	public CommonModel completeTask(String bizId) {
		logger.info("进入 支付接口==调用工作流==，bizId="+bizId);
		CommonModel model =new CommonModel();
		//String url  = PayConfigMappingMgr.getFlowUrl();
		String url  = ValidateUtil.getConfigValue("workflow.url")+"/process/completeSubTask";
		try {
			INSBOrderpayment payment = insbOrderpaymentService.selectBySerialNumber(bizId);
			INSBWorkflowsub sub =new INSBWorkflowsub();
			sub.setInstanceid(payment.getSubinstanceid());
			INSBWorkflowsub flow = insbWorkflowsubService.queryOne(sub);
			
			INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
			insbQuoteinfo.setWorkflowinstanceid(payment.getSubinstanceid());
			insbQuoteinfo = insbQuoteinfoService.queryOne(insbQuoteinfo);
			INSBPaychannelmanager insbPaychannelmanager = new INSBPaychannelmanager();
			insbPaychannelmanager.setProviderid(insbQuoteinfo.getInscomcode());
			insbPaychannelmanager.setDeptid(insbQuoteinfo.getDeptcode());
			insbPaychannelmanager.setPaychannelid(payment.getPaychannelid());
			List<INSBPaychannelmanager> pcmList = insbPaychannelmanagerService.queryList(insbPaychannelmanager);
			String taskid =payment.getSubinstanceid();
			String agentId = flow.getOperator();
			if(StringUtils.isEmpty(agentId))
				agentId="admin";
			Map<String, Object> map1 = new HashMap<String, Object>();			
			Map<String, Object> map = new HashMap<String, Object>();
			
			if(pcmList.size()!=0&&"1".equals(pcmList.get(0).getCollectiontype())){
				map1.put("issecond", "0");//1需要二次支付，0不需要二次支付
				map1.put("acceptway", JSONObject.fromObject(workflowmainService.getContractcbType(flow.getMaininstanceid(), insbQuoteinfo.getInscomcode(), "0", "contract")).getString("quotecode"));
				
			}else{
				map1.put("issecond", "1");//1需要二次支付，0不需要二次支付
			}
			map.put("data", map1);
			map.put("userid", "admin");
			map.put("processinstanceid", Long.parseLong(taskid));
			map.put("taskName", "支付");
			JSONObject jsonObject = JSONObject.fromObject(map);
			Map<String, String> params = new HashMap<String, String>();
			params.put("datas", jsonObject.toString());
			String retStr=null;
			try {
				retStr = HttpClientUtil.doGet(url, params);
			} catch (Exception e) {
			}
			LogUtil.info("支付接口==调用工作流==，bizId："+bizId+"，url:"+url+"params:datas="+jsonObject.toString());
			if(StringUtil.isEmpty(retStr)){
				model.setStatus("fail");
				model.setMessage("工作流流转失败(工作流调用失败)");
				this.updatePayResultStatusBybizId(bizId,1);
				return model;
			}
			String a =new String(retStr.getBytes(),"utf-8");
			JSONObject obj = JSONObject.fromObject(retStr);
			if(obj.get("message").equals("success")){
				model.setStatus("success");
				model.setMessage("工作流流转成功!");
			}else{
				model.setStatus("fail");
				model.setMessage("工作流流转失败("+obj.getString("reason")+")");
				this.updatePayResultStatusBybizId(bizId,1);
			}
		} catch (Exception e) {
			e.printStackTrace();	
			model.setStatus("fail");
			model.setMessage("工作流流转失败("+e.getMessage()+")");
			LogUtil.info("支付接口==调用工作流==失败，bizId："+bizId+"，信息："+e.getMessage());
			this.updatePayResultStatusBybizId(bizId,1);
		}
		logger.info("关闭 支付接口==调用工作流==，bizId="+bizId);
		return model;
	}
	private void updatePayResultStatusBybizId(String bizId,Integer status){

			INSBPayResult payResult = insbPayResultService.selectPayBybizId(bizId);
			if(payResult==null)return;
			payResult.setStatus(status);
			payResult.setModifytime(new Date());
			insbPayResultService.updateById(payResult);
	}
	public INSBOrderpayment updateOrderPayment(String bizId,String payFlowNo,String payOrderNo,String paySerialNo, String payResult, Date trasDate) {
		logger.info("进入updateOrderPayment方法=== bizId:"+bizId+",payFlowNo:"+payFlowNo+",payOrderNo:"+payOrderNo+",paySerialNo:"+paySerialNo+",payResult:"+payResult+",trasDate:"+trasDate);
		INSBOrderpayment payment = insbOrderpaymentService.selectBySerialNumber(bizId);
		if(!StringUtil.isEmpty(payResult)){
			payment.setPayresult(payResult); 
			payment.setMd5(LdapMd5.Md5Encode(String.valueOf(payment.getAmount())+payResult+bizId));
		}
		if(!StringUtil.isEmpty(trasDate))payment.setPaytime(trasDate);
		if(!StringUtil.isEmpty(paySerialNo))payment.setTradeno(paySerialNo);
		if(!StringUtil.isEmpty(payOrderNo))payment.setPayorderno(payOrderNo);
		if(!StringUtil.isEmpty(payFlowNo))payment.setPayflowno(payFlowNo);
		payment.setModifytime(new Date());
		insbOrderpaymentService.updateById(payment);
		
		INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
		try {
			PropertyUtils.copyProperties(insbOrderpayment, payment);
			insbOrderpayment.setId(UUIDUtils.random());
			insbOrderpayment.setModifytime(null);
			insbOrderpaymentService.insert(insbOrderpayment);
		} catch (Exception e) {
			LogUtil.error("bizid:" + payment.getPaymentransaction() + ",支付接口轨迹拷贝出错2");
		}
		logger.info("关闭updateOrderPayment方法=== bizId:"+bizId);
		return payment;
	}
	@Override
	public CommonModel queryPayResultFromPayPlat(String bizId) {
		logger.info("进入queryPayResultFromPayPlat方法=== bizId:"+bizId);
		CommonModel model =new CommonModel();
		HashMap<String,String> map=new HashMap<String,String>();
		map.put("bizId", bizId);
		String result = HttpClient.doPayGet(PayConfigMappingMgr.getQueryResultUrl(), map);
		logger.info("请求查询支付结果接口===:bizId="+ bizId+",url="+PayConfigMappingMgr.getQueryResultUrl()+",结果="+result);
		if(StringUtil.isEmpty(result)){
			model.setStatus("fail");
			model.setMessage("访问支付平台消息失败" + result);
			return model;
		}
		JSONObject json=null;
		try {
			json = JSONObject.fromObject(result);
		} catch (Exception e) {
			model.setStatus("fail");
			model.setMessage("支付平台返回信息异常");
			model.setBody(result);
			return model;
		}
		if(json.containsKey("code")){
			if(json.getString("code").equals("0000")){
				if(json.containsKey("orderState") && json.getString("orderState").equals("02")){
					model.setStatus("success");
					model.setMessage("已支付");
					model.setBody(json);
				}else if(json.containsKey("orderState") && json.getString("orderState").equals("01")){
					model.setStatus("waiting");
					if(json.containsKey("msg"))
						model.setMessage(json.getString("msg"));
					model.setBody(json);
				}else{
					model.setStatus("fail");
					if(json.containsKey("msg"))
						model.setMessage(json.getString("msg"));
					model.setBody(json);
				}
			}else{
				model.setStatus("fail");
				if(json.containsKey("msg"))
					model.setMessage(json.getString("msg"));
				model.setBody(json);
			}

		}else{
			model.setStatus("fail");
			model.setMessage("返回消息不正确！"+result);
			model.setBody(json);
		}
		logger.info("关闭queryPayResultFromPayPlat方法=== bizId:"+bizId);
		return model;
	}

	@Override
	public CommonModel testPay(String bizId) {
		CommonModel model =new CommonModel();
		if(StringUtil.isEmpty(bizId)){
			model.setStatus("fail");
			model.setMessage("订单出错！");
			return model;
		}

		INSBOrderpayment payment = insbOrderpaymentService.selectBySerialNumber(bizId);
		if(payment!=null){
			payment.setModifytime(new Date());
			payment.setPayflowno(bizId);
			insbOrderpaymentService.updateById(payment);
		}
		this.updateOrderPayment(bizId,null,null,null, PayStatus.PAYING, new Date());

		INSBOrderpayment orderpayment = new INSBOrderpayment();
		orderpayment.setPaymentransaction(bizId);
		orderpayment = insbOrderpaymentService.queryOne(orderpayment);
		INSBQuoteinfo quoteinfo = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(orderpayment.getSubinstanceid());
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(orderpayment.getTaskid());
		insbPolicyitem.setRisktype("1");
		insbPolicyitem.setInscomcode(quoteinfo.getInscomcode());
		insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
		if(insbPolicyitem==null){
			insbPolicyitem = new INSBPolicyitem();
			insbPolicyitem.setTaskid(orderpayment.getTaskid());
			insbPolicyitem.setRisktype("0");
			insbPolicyitem.setInscomcode(quoteinfo.getInscomcode());
			insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
		}
		appPaymentCallBackService.callBack(insbPolicyitem.getProposalformno(),bizId, bizId, bizId, "1.0", new Date().toLocaleString(), "支付成功", "02", "支付成功","","");
		model.setStatus("success");
		model.setMessage("支付成功！");
		return model;
	}
	public void updateOrder(String bizId,String orderStatus,String paymentStatus){
		logger.info("进入updateOrder方法==bizId："+bizId+",orderStatus："+orderStatus+",paymentStatus："+paymentStatus);
		INSBOrderpayment orderpayment = insbOrderpaymentService.selectBySerialNumber(bizId);
		if(orderpayment!=null&&!StringUtil.isEmpty(orderpayment.getOrderid())){
			INSBOrder order = new INSBOrder();
			order = insbOrderService.queryById(orderpayment.getOrderid());
			if(order!=null){
				if(!StringUtil.isEmpty(orderStatus))order.setOrderstatus(orderStatus);
				if(!StringUtil.isEmpty(paymentStatus))order.setPaymentstatus(paymentStatus);
				order.setModifytime(new Date());
				insbOrderService.updateById(order);
			}
		}
		logger.info("关闭updateOrder方法==bizId："+bizId);
	}

	@Override
	public CommonModel updateOrderPayment(String bizId, String payResult) {
		CommonModel model = new CommonModel();
		if(payResult.equals(PayStatus.PAYING)){
			//验证同一订单下是否有一个保险公司已经支付或在支付中
			INSBOrderpayment orderpayment = new INSBOrderpayment();
			orderpayment.setPaymentransaction(bizId);
			List<INSBOrderpayment> orderpaymentList = insbOrderpaymentService.queryList(orderpayment);
			for (INSBOrderpayment insbOrderpayment2 : orderpaymentList) {
				if(PayStatus.PAYING.equals(insbOrderpayment2.getPayresult())){
					model.setStatus("fail");
					model.setMessage("订单已经在支付中");	
					return model;
				}
				if(PayStatus.PAYED.equals(insbOrderpayment2.getPayresult())){
					model.setStatus("fail");
					model.setMessage("订单已经有保险公司支付成功，不可重复支付");	
					return model;
				}
			}
			INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();
			insbWorkflowsub.setMaininstanceid(orderpaymentList.get(0).getTaskid());
			List<INSBWorkflowsub> insbWorkflowsubList = insbWorkflowsubService.queryList(insbWorkflowsub);
			for (INSBWorkflowsub insbWorkflowsub2 : insbWorkflowsubList) {
				if(21<Integer.valueOf(insbWorkflowsub2.getTaskcode())){
					model.setStatus("fail");
					model.setMessage("订单已经支付完成，不可重复支付");	
					return model;
				}
			}
		}
		INSBOrderpayment insbOrderpayment = insbOrderpaymentService.selectBySerialNumber(bizId);
		insbOrderpayment.setPayresult(payResult);
		insbOrderpayment.setMd5(LdapMd5.Md5Encode(insbOrderpayment.getAmount()+payResult+insbOrderpayment.getPaymentransaction()));
		insbOrderpayment.setPaytime(new Date());
		insbOrderpayment.setModifytime(new Date());
		insbOrderpaymentService.updateById(insbOrderpayment);
		model.setStatus("success");
		return model;
	}

	@Override
	public CommonModel validateOrderPayment(String bizId) {
		CommonModel model = new CommonModel();
		//验证同一订单下是否有一个保险公司已经支付或在支付中
		INSBOrderpayment orderpayment = new INSBOrderpayment();
		orderpayment.setPaymentransaction(bizId);
		List<INSBOrderpayment> orderpaymentList = insbOrderpaymentService.queryList(orderpayment);
		for (INSBOrderpayment insbOrderpayment2 : orderpaymentList) {
			if(PayStatus.PAYING.equals(insbOrderpayment2.getPayresult())){
				model.setStatus("fail");
				model.setMessage("订单已经在支付中");	
				return model;
			}
			if(PayStatus.PAYED.equals(insbOrderpayment2.getPayresult())){
				model.setStatus("fail");
				model.setMessage("订单已经有保险公司支付成功，不可重复支付");	
				return model;
			}
		}
		INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();
		insbWorkflowsub.setMaininstanceid(orderpaymentList.get(0).getTaskid());
		List<INSBWorkflowsub> insbWorkflowsubList = insbWorkflowsubService.queryList(insbWorkflowsub);
		for (INSBWorkflowsub insbWorkflowsub2 : insbWorkflowsubList) {
			if(21<Integer.valueOf(insbWorkflowsub2.getTaskcode())){
				model.setStatus("fail");
				model.setMessage("订单已经支付完成，不可重复支付");	
				return model;
			}
		}
		model.setStatus("success");
		return model;
	}
	private boolean ediQuery(String bizId){
		INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
		insbOrderpayment.setPaymentransaction(bizId);
		insbOrderpayment = insbOrderpaymentService.queryOne(insbOrderpayment);
		
		INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
		insbQuoteinfo.setWorkflowinstanceid(insbOrderpayment.getSubinstanceid());
		insbQuoteinfo = insbQuoteinfoService.queryOne(insbQuoteinfo);

		INSBPaychannelmanager insbPaychannelmanager = new INSBPaychannelmanager();
		insbPaychannelmanager.setProviderid(insbQuoteinfo.getInscomcode());
		insbPaychannelmanager.setDeptid(insbQuoteinfo.getDeptcode());
		insbPaychannelmanager.setPaychannelid(insbOrderpayment.getPaychannelid());
		List<INSBPaychannelmanager> pcmList = insbPaychannelmanagerService.queryList(insbPaychannelmanager);
		if(pcmList.size()!=0&&"1".equals(pcmList.get(0).getCollectiontype())){//一支任务 edi申请
			logger.info("执行edi支付申请方法==bizId："+bizId+",taskid："+insbOrderpayment.getTaskid()+",inscomcode："+insbQuoteinfo.getInscomcode());
			try {
				interFaceService.goToEdiQuote(insbOrderpayment.getTaskid(),insbQuoteinfo.getInscomcode(),"admin","pay");
			} catch (Exception e) {
				e.printStackTrace();
			}

			INSBFlowinfo insbFlowinfo = new INSBFlowinfo();
			insbFlowinfo.setTaskid(insbOrderpayment.getTaskid());
			insbFlowinfo.setInscomcode(insbQuoteinfo.getInscomcode());
			insbFlowinfo = insbFlowinfoService.queryOne(insbFlowinfo);
			if(insbFlowinfo==null||"18".equals(insbFlowinfo.getFlowcode()))
				return false;
		}
		return true;
	}
	private void payFlowErrorLog(String bizid,String errordesc){ 
		
		INSBOrderpayment orderpayment = new INSBOrderpayment();
		orderpayment.setPaymentransaction(bizid);
		orderpayment = insbOrderpaymentService.queryOne(orderpayment);
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(orderpayment.getTaskid());
		insbPolicyitem.setRisktype("1");
		INSBQuoteinfo quoteinfo = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(orderpayment.getSubinstanceid());
		insbPolicyitem.setInscomcode(quoteinfo.getInscomcode());
		insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
		if(insbPolicyitem==null){
			insbPolicyitem = new INSBPolicyitem();
			insbPolicyitem.setTaskid(orderpayment.getTaskid());
			insbPolicyitem.setRisktype("0");
			insbPolicyitem.setInscomcode(quoteinfo.getInscomcode());
			insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
		}
		INSBFlowerror queryInsbFlowerror = new INSBFlowerror();
		queryInsbFlowerror.setTaskid(insbPolicyitem.getTaskid());
		queryInsbFlowerror.setInscomcode(insbPolicyitem.getInscomcode());
		queryInsbFlowerror.setFlowcode(insbPolicyitem.getProposalformno());
		INSBFlowerror dataInsbFlowerror = new INSBFlowerror();
		dataInsbFlowerror = insbFlowerrorService.queryOne(queryInsbFlowerror);
		if(StringUtil.isEmpty(dataInsbFlowerror)){
			dataInsbFlowerror = new INSBFlowerror();
		}
		dataInsbFlowerror.setOperator("admin");
		dataInsbFlowerror.setTaskid(insbPolicyitem.getTaskid());
		dataInsbFlowerror.setInscomcode(insbPolicyitem.getInscomcode());
		dataInsbFlowerror.setFlowcode(insbPolicyitem.getProposalformno());
		dataInsbFlowerror.setFlowname("支付失败");
		dataInsbFlowerror.setFiroredi("2");
		dataInsbFlowerror.setTaskstatus("pay");
		dataInsbFlowerror.setResult("false");
		dataInsbFlowerror.setErrordesc(errordesc);
		if(!StringUtil.isEmpty(dataInsbFlowerror.getId())){
			dataInsbFlowerror.setModifytime(new Date());
			insbFlowerrorService.updateById(dataInsbFlowerror);
		}else{
			dataInsbFlowerror.setCreatetime(new Date());
			insbFlowerrorService.insert(dataInsbFlowerror);
		}
	}

	@Override
	public String paySuccess(String bizId, String payType) {
		logger.info("进入支付成功接口==bizId："+bizId+",payType:"+payType);
		if(StringUtil.isEmpty(bizId)||StringUtil.isEmpty(payType))
			return "200";
		payType = PayConfigMappingMgr.getCmCodeByPayCode(MappingType.PAY_CHANNEL,payType);
		if(StringUtil.isEmpty(payType))
			return "200";
		INSBQuoteinfo quoteinfo = insbQuoteinfoService.queryById(bizId);
		INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoService.queryById(quoteinfo.getQuotetotalinfoid());
		insbPaychannelService.choosePayChannel(payType, "admin", quotetotalinfo.getTaskid(), quoteinfo.getInscomcode());

		INSBOrderpayment payment = new INSBOrderpayment();
		payment.setSubinstanceid(quoteinfo.getWorkflowinstanceid());
		payment = insbOrderpaymentService.queryOne(payment);
		if(payment!=null){
			CommonModel model = this.completeTask(payment.getPaymentransaction());
			if(model.getStatus().equals("success")){
				payment=this.updateOrderPayment(payment.getPaymentransaction(),payment.getPayflowno(),null, null, PayStatus.PAYED, new Date());
				this.updateOrder(bizId, "2", "04");//更改订单状态
			}
		}
		logger.info("关闭支付成功接口==bizId："+bizId+",payType:"+payType);
		return "200";
	}

	@Override
	public String channelPay(String taskId, String companyId) {
		logger.info("进入渠道支付接口==taskId："+taskId+",companyId:"+companyId);
		INSBQuoteinfo quoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(taskId, companyId);
		if(quoteinfo==null||StringUtil.isEmpty(quoteinfo.getWorkflowinstanceid())){
			logger.info("关闭渠道支付接口==taskId："+taskId+",companyId:"+companyId+",单子不存在");
			return null;
		}
		
		INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();
		insbWorkflowsub.setInstanceid(quoteinfo.getWorkflowinstanceid());
		insbWorkflowsub = insbWorkflowsubService.queryOne(insbWorkflowsub);
		if(!insbWorkflowsub.getTaskcode().equals("20")){
			logger.info("关闭渠道支付接口==taskId："+taskId+",companyId:"+companyId+",单子不存在");
			return null;
		}
		
		JSONObject jsonobj = JSONObject.fromObject(insbPaychannelService.choosePayChannel1("0", "admin", taskId, companyId));
		if(jsonobj.getString("status").equals("fail"))return null;
		long invalidTime = System.currentTimeMillis();
		redisClient.set(CHANNEL_PAYMENT, jsonobj.get("body").toString(),invalidTime,24*60*60);
		logger.info("关闭渠道支付接口==taskId："+taskId+",companyId:"+companyId);
		return jsonobj.get("body").toString();
	}

	@Override
	public PayInfo verify(PayInfo payInfo) {
		LogUtil.info("进入verify方法，bizId=" + payInfo.getBizId());
		PayInfo result = new PayInfo();
		String jsonStr = String.valueOf(redisClient.get(CHANNEL_PAYMENT,  payInfo.getBizId()));
		if (jsonStr == null || "null".equals(jsonStr)) {
			result.setCode("13");
			result.setMsg("无效的支付请求");
			return result;
		}
		QuoteBean quoteBean = JsonUtils.deserialize(jsonStr, QuoteBean.class);		
		long invalidTime = Long.parseLong(quoteBean.getCreateTime());
		if ((System.currentTimeMillis() - invalidTime) > 1800 * 1000) {
			redisClient.del(CHANNEL_PAYMENT,  payInfo.getBizId());
			result.setCode("13");
			result.setMsg("无效的支付请求");
			return result;
		}
		INSBOrder insbOrder = new INSBOrder();
		insbOrder.setTaskid(quoteBean.getTaskId());
		insbOrder.setPrvid(quoteBean.getPrvId());
		insbOrder = insbOrderService.queryOne(insbOrder);
		if(insbOrder == null){
			result.setCode("13");
			result.setMsg("支付订单不存在");
			return result;
		}
		result.setTaskId(quoteBean.getTaskId());

		BigDecimal temp_amount = new BigDecimal(String.valueOf(insbOrder.getTotalpaymentamount()));
		int amount = temp_amount.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).intValue();
		result.setAmount(amount);
		// 查询代理人机构
		INSBQuoteinfo insbQuoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(quoteBean.getTaskId(), quoteBean.getPrvId());
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(quoteBean.getTaskId());
		insbPolicyitem.setInscomcode(insbQuoteinfo.getInscomcode());
		insbPolicyitem = insbPolicyitemService.queryList(insbPolicyitem).get(0);
		INSBAgent insbAgent = insbAgentService.getAgent(insbPolicyitem.getAgentnum());
		INSCDept inscDept = inscDeptService.queryById(insbAgent.getDeptid());
		result.setAreaCode(inscDept.getProvince());
		// 查询投保人信息
		INSBApplicant insbApplicant = new INSBApplicant();
		INSBApplicanthis insbApplicanthis = new INSBApplicanthis();
		insbApplicanthis.setTaskid(quoteBean.getTaskId());
		insbApplicanthis.setInscomcode(insbQuoteinfo.getInscomcode());
		insbApplicanthis = insbApplicanthisService.queryOne(insbApplicanthis);
		if (insbApplicanthis == null) {
			insbApplicant = new INSBApplicant();
			insbApplicant.setTaskid(quoteBean.getTaskId());
			insbApplicant = insbApplicantService.queryOne(insbApplicant);
			if (insbApplicant == null) {
				LogUtil.error("关闭verify方法，bizId=" + payInfo.getBizId() + ",投保人信息不存在");
				result.setCode("13");
				result.setMsg("非法的支付请求");
				return result;
			} else {
				try {
					insbApplicanthis = new INSBApplicanthis();
					PropertyUtils.copyProperties(insbApplicanthis, insbApplicant);
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					LogUtil.error("关闭verify方法，bizId=" + payInfo.getBizId() + ",获取投保人信息出错");
					result.setCode("13");
					result.setMsg("非法的支付请求");
					return result;
				}
			}
		}
		INSBPerson insbPerson = insbPersonService.queryById(insbApplicanthis.getPersonid());
		Map<String, String> channelParam = new HashMap<String, String>();
		channelParam.put("applicantMobile", insbPerson.getCellphone());
		channelParam.put("applicantName", insbPerson.getName());
		channelParam.put("applicantCardNo", insbPerson.getIdcardno());
		channelParam.put("applicantCardType", insbPerson.getIdcardtype() == null ? null : insbPerson.getIdcardtype().toString());
		result.setChannelParam(channelParam);
		LogUtil.info("关闭verify方法，bizId=" + payInfo.getBizId() + ",result=" + result);
		List<Channel> list = new ArrayList<Channel>();
		Channel channel = new Channel();
		channel.setChannelId("payeco");
		channel.setChannelName("易联");
		channel.setPayType("voice");
		channel.setPayTypeDesc("语音");
		list.add(channel);
		channel = new Channel();
		channel.setChannelId("bestpay");
		channel.setChannelName("易联");
		channel.setPayType("signed");
		channel.setPayTypeDesc("签约");
		list.add(channel);
		channel = new Channel();
		channel.setChannelId("99bill");
		channel.setChannelName("快钱");
		channel.setPayType("mobile");
		channel.setPayTypeDesc("移动");
		list.add(channel);
		result.setChannelList(list);
		return result;
	}
	@Override
	public PayInfo pay(PayInfo payInfo, QuoteBean quoteBean) {
		PayInfo result = new PayInfo();
		result.setCode("0000");
		INSBOrder insbOrder = new INSBOrder();
		insbOrder.setTaskid(quoteBean.getTaskId());
		insbOrder.setPrvid(quoteBean.getPrvId());
		insbOrder = insbOrderService.queryOne(insbOrder);

		if ("".equals(insbOrder.getOrderstatus())) { // 已经支付
			result.setCode("502");
			result.setMsg("已支付");
		} else if ("".equals(insbOrder.getOrderstatus())) { // 待支付
			result.setCode("502");
			result.setMsg("已支付");
		}

		INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
		insbOrderpayment.setTaskid(quoteBean.getTaskId());
		insbOrderpayment.setCreatetime(new Date());
		insbOrderpayment.setModifytime(new Date());
		insbOrderpayment.setOperator("渠道");
		insbOrderpayment.setOrderid(insbOrder.getId());
		insbOrderpayment.setPaychannelid("");
		insbOrderpayment.setAmount(insbOrder.getTotalpaymentamount());
		insbOrderpayment.setCurrencycode("RMB");
		insbOrderpayment.setPaymentransaction(insbPaymenttransactionService.getPaymenttransaction(new Date()));
		insbOrderpayment.setPayresult("0");
		insbOrderpayment.setMd5(LdapMd5.Md5Encode(insbOrderpayment.getAmount() + insbOrderpayment.getPayresult()
				+ insbOrderpayment.getPaymentransaction()));
		insbOrderpayment.setSubinstanceid("");

		insbOrderpaymentService.insert(insbOrderpayment);
		payInfo.setBizId(insbOrderpayment.getPaymentransaction());

		String bizId = insbOrderpayment.getPaymentransaction();
		logger.info("进入pay2方法  bizId=" + bizId + ",param=" + payInfo);

		JSONObject payInfoJson = JSONObject.fromObject(payInfo);
		payInfoJson.put("amount", String.valueOf(insbOrderpayment.getAmount()));
		payInfoJson.put("channelId", insbOrderpayment.getPaychannelid());
		this.updateOrderPayment(bizId, "", "", "", PayStatus.PAYING, new Date());
		CommonModel model = null;
		switch (payInfo.getChannelId()) {
		case "payeco":
			model = payeco(payInfoJson.toString());
			break;
		case "yeepay":
			model = yeepay(payInfoJson.toString());
			break;
		case "alipay":
			model = alipay(payInfoJson.toString());
			break;
		case "bestpay":
			model = bestpay(payInfoJson.toString());
			break;
		case "tenpay":
			model = tenpay(payInfoJson.toString());
			break;
		case "99bill":
			model = kqbill(payInfoJson.toString());
			break;
		case "testPay":
			model = testPay(bizId);
			break;
		case "huaan":
			model = huaan(payInfoJson.toString());
			break;
		case "axatp":
			model = axatp(payInfoJson.toString());
			break;
		case "alltrust":
			model = alltrust(payInfoJson.toString());
			break;
		default:
			model = null;
		}
		if (model == null || model.getStatus().equals("fail")) {
			this.updateOrderPayment(bizId, null, null, null, PayStatus.ERROR, new Date());
			if (model != null) {
				this.payFlowErrorLog(bizId, model.getMessage());
			}
		}
		logger.info("关闭pay2方法");
		JSONObject object = JSONObject.fromObject(model.getBody());
		return (PayInfo) JSONObject.toBean(object, PayInfo.class);
	}
}






