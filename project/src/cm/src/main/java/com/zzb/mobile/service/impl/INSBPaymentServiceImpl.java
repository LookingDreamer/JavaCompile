package com.zzb.mobile.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

import javax.annotation.Resource;

import com.common.WorkflowFeedbackUtil;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.cm.dao.*;
import com.zzb.cm.entity.*;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.cm.service.INSBQuotetotalinfoService;
import com.zzb.conf.service.*;
import com.zzb.mobile.service.AppPermissionService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCDeptService;
import com.common.HttpClientUtil;
import com.common.ModelUtil;
import com.zzb.cm.dao.INSBCarkindpriceDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBOrderpaymentDao;
import com.zzb.conf.dao.INSBPaychannelmanagerDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.INSBPrvaccountkeyDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.dao.INSBWorkflowsubtrackDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.entity.INSBPaychannelmanager;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBPrvaccountkey;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import com.zzb.mobile.dao.AppPaymentyzfDao;
import com.zzb.mobile.dao.InsbpaymentpasswordDao;
import com.zzb.mobile.entity.AppPaymentyzf;
import com.zzb.mobile.entity.Insbpaymentpassword;
import com.zzb.mobile.model.CallBackParam;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.PayParam;
import com.zzb.mobile.service.INSBPaymentService;
import com.zzb.mobile.util.AmountUtil;
import com.zzb.mobile.util.HttpClient;
import com.zzb.mobile.util.MappingType;
import com.zzb.mobile.util.PayConfigMappingMgr;
@Service
public class INSBPaymentServiceImpl implements INSBPaymentService {
	@Resource
	private InsbpaymentpasswordDao insbpaymentpasswordDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBOrderpaymentDao insbOrderpaymentDao;
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;
	@Resource
	private INSBWorkTimeService insbWorkTimeService;
	@Resource
	private INSBPrvaccountmanagerService insbPrvaccountmanagerservice;
	@Resource
	private INSBWorkflowsubDao insbWorkflowsubDao;
	@Resource
	private INSCDeptService inscDeptService;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSBPaychannelmanagerDao insbPaychannelmanagerDao;
	@Resource
	private INSBCarkindpriceDao insbCarkindpriceDao;
	@Resource
	private AppPaymentyzfDao appPaymentyzfDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBWorkflowsubtrackDao insbWorkflowsubtrackDao;
	@Resource
	private INSBProviderDao insbProviderDao;
	@Resource
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	@Resource
	private INSBPrvaccountkeyDao insbPrvaccountkey;
	@Resource
	private AppPermissionService appPermissionService;
	@Resource
	private INSBWorkflowmainService workflowmainService;

	@Resource
	private INSBQuotetotalinfoDao quotetotalinfoDao;
	@Resource
	private INSBCarinfohisDao carinfohisDao;

	@Resource
	private INSBOrderpaymentService insbOrderpaymentService;
	@Resource
	private INSBQuoteinfoService insbQuoteinfoService;
	@Resource
	private INSBPersonDao insbPersonDao;
	@Resource
	private INSBPolicyitemService insbPolicyitemService;
	@Resource
	private INSBCarmodelinfohisDao insbCarmodelinfohisDao;
	@Resource
	private InterFaceService interFaceService;
	@Resource
	INSBQuotetotalinfoService insbQuotetotalinfoService;// 报价信息总表

	private static Logger logger = Logger.getLogger(INSBPaymentServiceImpl.class);
	private static String split = "X";

	/**
	 * 是否需要二次支付：0 不需要，1 需要，-1 缺少数据无法判断
	 */
	private int needSecPay(String providerid, String deptcode, String paychannelid) {
		LogUtil.info("是否需要二支：provideid:%s deptid:%s paychannelId:%s", providerid, deptcode, paychannelid);

		if (StringUtil.isEmpty(providerid) || StringUtil.isEmpty(deptcode) || StringUtil.isEmpty(paychannelid)) {
			LogUtil.error("有参数为空");
			return -1;
		}

		INSBPaychannelmanager insbPaychannelmanager = new INSBPaychannelmanager();
		insbPaychannelmanager.setProviderid(providerid);
		insbPaychannelmanager.setDeptid(deptcode);
		insbPaychannelmanager.setPaychannelid(paychannelid);
		List<INSBPaychannelmanager> pcmList = insbPaychannelmanagerDao.selectList(insbPaychannelmanager);

		if (pcmList.isEmpty()) {
			//没有网点数据则获取平台数据
			String ptdeptcode = inscDeptService.getPingTaiDeptId(deptcode);
			insbPaychannelmanager.setDeptid(ptdeptcode);
			pcmList = insbPaychannelmanagerDao.selectList(insbPaychannelmanager);
		}

		LogUtil.info("是否需要二支：pcmList.size():%s", pcmList.size());
		if (pcmList.size() != 0 && "1".equals(pcmList.get(0).getCollectiontype())) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * 设置第三方商户
	 * @param providerid
	 * @param deptcode
	 * @param paychannelid
	 * @param payInfoJson
     */
	private void setMerchant(String providerid, String deptcode, String paychannelid, JSONObject payInfoJson) {
		LogUtil.info("setMerchant：provideid:%s deptid:%s paychannelId:%s", providerid, deptcode, paychannelid);

		if (StringUtil.isEmpty(providerid) || StringUtil.isEmpty(deptcode) || StringUtil.isEmpty(paychannelid)) {
			LogUtil.error("setMerchant有参数为空");
			return;
		}

		INSBPaychannelmanager insbPaychannelmanager = new INSBPaychannelmanager();
		insbPaychannelmanager.setProviderid(providerid);
		insbPaychannelmanager.setDeptid(deptcode);
		insbPaychannelmanager.setPaychannelid(paychannelid);
		List<INSBPaychannelmanager> pcmList = insbPaychannelmanagerDao.selectList(insbPaychannelmanager);

		if (pcmList.isEmpty()) {
			//没有网点数据则获取平台数据
			String ptdeptcode = inscDeptService.getPingTaiDeptId(deptcode);
			insbPaychannelmanager.setDeptid(ptdeptcode);
			pcmList = insbPaychannelmanagerDao.selectList(insbPaychannelmanager);
		}
		if (!pcmList.isEmpty()) {
			if ("1".equals(pcmList.get(0).getCollectiontype()) || "2".equals(pcmList.get(0).getCollectiontype()) || "3".equals(pcmList.get(0).getCollectiontype())) {
				payInfoJson.put("merchant", "");
			} else {
				payInfoJson.put("merchant", pcmList.get(0).getCollectiontype());
			}
		} else {
			payInfoJson.put("merchant", "");
		}

	}

	/**
	 * bug-1545 cm：392915 这个有效期过了，还可以支付。
	 * 当前是否过了报价有效期
	 * @param processInstanceId
	 * @param inscomcode
	 * @return
	 */
	public boolean isExpired(String processInstanceId, String inscomcode) {
		// 判断是否过期报价周期和起保时间
		Date busStartDate = null, strStartDate = null;
		INSBPolicyitem policyitem = new INSBPolicyitem();
		policyitem.setTaskid(processInstanceId);
		policyitem.setInscomcode(inscomcode);
		List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectList(policyitem);
		for (int i = 0; i < policyitemList.size(); i++) {
			if("0".equals(policyitemList.get(i).getRisktype())){//商业险保单
				busStartDate = policyitemList.get(i).getStartdate();
			}else if("1".equals(policyitemList.get(i).getRisktype())){//交强险保单
				strStartDate = policyitemList.get(i).getStartdate();
			}
		}

		INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
		quotetotalinfo.setTaskid(processInstanceId);
		quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
		INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
		quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
		quoteinfo.setInscomcode(inscomcode);
		quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);

		//获取选择报价轨迹时间
		INSBWorkflowsubtrack subtrack = new INSBWorkflowsubtrack();
		subtrack.setMaininstanceid(processInstanceId);
		subtrack.setInstanceid(quoteinfo.getWorkflowinstanceid());
		subtrack.setTaskcode("14");//选择投保
		subtrack = insbWorkflowsubtrackDao.selectOne(subtrack);//选择投保子流程轨迹信息

		//报价有效期
		INSBProvider atainteger = insbProviderDao.selectById(inscomcode);
		Date quotesuccessTimes = null;
		if(null == subtrack || null == atainteger || null == atainteger.getQuotationvalidity()){
			quotesuccessTimes = ModelUtil.gatFastPaydate(busStartDate, strStartDate);
		}else{
			quotesuccessTimes = ModelUtil.gatFastPaydateToNow(busStartDate, strStartDate, subtrack.getCreatetime(), atainteger.getQuotationvalidity());
		}
		LogUtil.info("报价有效期"+processInstanceId+","+inscomcode+"到期时间="+quotesuccessTimes);

		return ModelUtil.daysBetween(quotesuccessTimes, new Date()) > 0;
	}
	@Override
	public CommonModel pay(PayParam payParam) {
		JSONObject payInfoJson = payParam.getPayJsonStr();
		payParam.setBizId(payInfoJson.getString("bizId"));
		CommonModel result = new CommonModel();
		result.setStatus("success");
		INSBAgent insbAgent = new INSBAgent();
		insbAgent.setJobnum(payParam.getJobNum());
		insbAgent = insbAgentDao.selectOne(insbAgent);
		if (insbAgent == null) {
			insbAgent = new INSBAgent();
			insbAgent.setTempjobnum(payParam.getJobNum());
			insbAgent = insbAgentDao.selectOne(insbAgent);
		}
		if (insbAgent == null) {
			result.setStatus("fail");
			result.setMessage("用户不存在！");
			return result;
		}

		//权限包
		Map<String,Object> permissionMap = appPermissionService.checkPermission(payParam.getJobNum(), "", "pay");
		if ((int)permissionMap.get("status") == 2 || (int)permissionMap.get("status") == -1) {
			result.setStatus(CommonModel.STATUS_FAIL);
			result.setMessage((String) permissionMap.get("message"));
			return result;
		}

		// 假如密码不空，需要先验证密码是否正确
		if (StringUtils.isNotBlank(payParam.getPassword())) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("jobNum", payParam.getJobNum());
			map.put("password", payParam.getPassword());
			Insbpaymentpassword insbpaymentpassword = insbpaymentpasswordDao.selectOnebyMap(map);
			if (insbpaymentpassword == null) {
				result.setStatus("fail");
				result.setMessage("支付密码不正确！");
				return result;
			}
		}
		if(StringUtils.isNotBlank(payParam.getBankCardInfoId())){
			CommonModel model = pipleBanKCardInfo(insbAgent, payInfoJson, payParam.getBankCardInfoId());
			if("fail".equals(model.getStatus())){
				result.setMessage(model.getMessage());
				result.setStatus("fail");
				return result;
			}
		}
		INSBOrder insbOrder = new INSBOrder();
		insbOrder.setOrderno(payParam.getBizId()); // bizId中存放的是insbOrder表中的orderNo
		insbOrder = insbOrderDao.selectOne(insbOrder);
		if (insbOrder == null) {
			result.setStatus("fail");
			result.setMessage("未找到订单");
			return result;
		}
		if (!"1".equals(insbOrder.getOrderstatus())) {
			result.setStatus("fail");
			result.setMessage("订单状态不正确");
			return result;
		}

		if(isExpired(insbOrder.getTaskid(),insbOrder.getPrvid())) {
			result.setStatus("fail");
			result.setMessage("订单已过有效期！");
			return result;
		}

		Date payDate = getPayTime(insbOrder.getTaskid(), insbOrder.getPrvid());
		if (payDate != null && ModelUtil.daysBetween(payDate, new Date()) > 0) {
			result.setStatus("fail");
			result.setMessage("订单已过有效期！");
			return result;
		}

		// 找到子任务号
		Map<String, String> map = new HashMap<String, String>();
		map.put("taskid", insbOrder.getTaskid());
		map.put("companyid", insbOrder.getPrvid());
		INSBQuoteinfo insbQuoteinfo = insbQuoteinfoDao.getByTaskidAndCompanyid(map);

		INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();
		insbWorkflowsub.setMaininstanceid(insbOrder.getTaskid());
		insbWorkflowsub.setInstanceid(insbQuoteinfo.getWorkflowinstanceid());
		insbWorkflowsub = insbWorkflowsubDao.selectOne(insbWorkflowsub);
		if ("21".equals(insbWorkflowsub.getTaskcode())) {
			result.setStatus("fail");
			result.setMessage("订单已经支付完成，不可重复支付");
			return result;
		}

		INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
		insbOrderpayment.setTaskid(insbOrder.getTaskid()); // 设置任务id
		insbOrderpayment.setOperator(payParam.getJobNum()); // 操作员
		insbOrderpayment.setSubinstanceid(insbQuoteinfo.getWorkflowinstanceid());
		insbOrderpayment.setOrderid(insbOrder.getId()); // 订单表id
		insbOrderpayment.setPaychannelid(payInfoJson.getString("channelId")); // 支付渠道
		insbOrderpayment.setNoti(payInfoJson.getString("payType")); // 支付方式
		insbOrderpayment.setAmount(insbOrder.getTotalpaymentamount());
		insbOrderpayment.setCurrencycode("RMB"); // 币种编码
		insbOrderpayment.setPayresult("01"); // 支付状态：待支付-00，正在支付-01, 支付完成-02 等等
		insbOrderpayment.setPaymentransaction(payParam.getBizId());
		insbOrderpayment.setCreatetime(new Date()); // 创建时间
		insbOrderpayment.setModifytime(new Date());

		if(payParam.getIsInsureQuery() != null && "false".equals(payParam.getIsInsureQuery())) {

		} else {
			insbOrderpaymentDao.insert(insbOrderpayment); // 保存支付轨迹
		}

		insbOrder.setPaymentmethod(payInfoJson.getString("channelId"));
		insbOrder.setNoti(payInfoJson.getString("payType"));
		insbOrder.setModifytime(new Date());
		insbOrderDao.updateById(insbOrder); // 记录最后一次支付方式

		int secPay = needSecPay(insbOrder.getPrvid(), insbQuoteinfo.getDeptcode(), payInfoJson.getString("channelId"));
		LogUtil.info("是否需要二支：%s, %s：%s", insbOrder.getTaskid(), insbOrder.getPrvid(), secPay);

		//http://pm.baoxian.in/zentao/bug-view-8253.html
		if (secPay == 1) {
			int inWorkTime = insbWorkTimeService.inWorkTime(new Date(), insbQuoteinfo.getDeptcode(), payDate);
			if (inWorkTime == 0) {
				result.setStatus("fail");
				result.setMessage("不在业管上班时间内");
				return result;
			} else if (inWorkTime == -1) {
				result.setStatus("fail");
				result.setMessage("订单已过有效期！");
				return result;
			}
		}

		// 取得支付方式(跟支付平台保持一致)
		String channelId = PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_CHANNEL, payInfoJson.getString("channelId"));
		if (StringUtils.isEmpty(channelId)) {
			result.setStatus("fail");
			result.setMessage("未找到匹配的支付渠道");
			return result;
		}

		// TODO 判断支付通道是否支持客户端

		// TODO 判断支付通道是否仅支持edi核保

		// 判断订单是否可支付
		HashMap<String, String> paramGet = new HashMap<String, String>();
		paramGet.put("processinstanceid", insbOrderpayment.getSubinstanceid());
		logger.info("请求工作流是否可支付接口URl: "+ValidateUtil.getConfigValue("workflow.url")+"/process/isCanPay?processinstanceid=" +insbOrderpayment.getSubinstanceid() );
		String doGet = HttpClientUtil.doGet(ValidateUtil.getConfigValue("workflow.url") + "/process/isCanPay", paramGet);
		logger.info("请求工作流是否可支付接口返回结果" + doGet);
		if (StringUtil.isEmpty(doGet)) {
			result.setStatus("fail");
			result.setMessage("访问工作流出错");
			return result;
		}
		JSONObject doGetJson = JSONObject.fromObject(doGet);
		if (!doGetJson.containsKey("message") || !doGetJson.getBoolean("message")) {
			result.setStatus("fail");
			result.setMessage("订单暂时无法支付");
			return result;
		}

		payInfoJson.put("amount", AmountUtil.trans2Fen(insbOrder.getTotalpaymentamount()));
		if(!payInfoJson.containsKey("agentOrg") || StringUtils.isEmpty(payInfoJson.getString("agentOrg")))
			payInfoJson.put("agentOrg", insbAgent.getDeptid());
		payInfoJson.put("bizId", payParam.getBizId());
		payInfoJson.put("payType", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_TYPE, payInfoJson.getString("payType")));
		Map<String, String> channelParam = (Map<String, String>) payInfoJson.get("channelParam");
		if(channelParam == null) {
			channelParam = new HashMap<String, String>();
			payInfoJson.put("channelParam", channelParam);
		}
		setAgentOrg(payInfoJson);
		setMerchant(insbOrder.getPrvid(), insbQuoteinfo.getDeptcode(), payInfoJson.getString("channelId"), payInfoJson);
		switch (channelId) { // 根据不同的支付方式完善数据
			case "payeco":
				payeco(payInfoJson);
				break;
			case "yeepay":
				yeepay(payInfoJson);
				break;
			case "alipay":
				alipay(payInfoJson);
				break;
			case "bestpay":
				bestpay(payInfoJson, insbOrderpayment);
				break;
			case "tenpay":
				tenpay(payInfoJson);
				break;
			case "99bill":
				_99bill(payInfoJson, insbOrder.getTaskid(), insbOrder.getPrvid(), insbQuoteinfo.getDeptcode());
				break;
			case "huaan":
				huaan(payInfoJson, insbOrder.getTaskid(), insbOrder.getPrvid());
				break;
			case "yingda":
				yingda(payInfoJson, insbOrder.getTaskid(), insbOrder.getPrvid(),insbQuoteinfo.getDeptcode());
				break;
			case "chengtai":
				channelParam.put("orderNo", insbOrder.getInsorderno());
				payInfoJson.put("channelParam", channelParam);
				break;
			case "axatp":
				axatp(payInfoJson, insbOrder.getTaskid(), insbOrder.getPrvid());
				break;
			case "anxin":
				anxin(payInfoJson, insbOrder.getTaskid(), insbOrder.getPrvid());
				break;
			case "alltrust":
				alltrust(payInfoJson, insbOrder.getTaskid(), insbOrder.getPrvid());
				break;
			case "fund":
				channelParam.put("orderNo", insbOrder.getInsorderno());
				payInfoJson.put("channelParam", channelParam);
				break;
			case "urtrust":  //众城支付申请参数
				urtrust(payInfoJson, insbOrder);
				break;
			case "jintai":  //锦泰支付申请参数
				alltrust(payInfoJson, insbOrder.getTaskid(), insbOrder.getPrvid());
				break;
			case "taiping":
				if(insbOrder.getInsorderno() != null)
					channelParam.put("orderNo", insbOrder.getInsorderno());
				else {
					INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
					insbPolicyitem.setTaskid(insbOrder.getTaskid());
					List<INSBPolicyitem> list = insbPolicyitemDao.selectList(insbPolicyitem);
					if(list.size() > 0)
						channelParam.put("orderNo", list.get(0).getCheckcode());
				}
				payInfoJson.put("channelParam", channelParam);
				break;
			case "yongcheng":
				alltrust(payInfoJson, insbOrder.getTaskid(), insbOrder.getPrvid());
				break;
			case "ccic":  //大地支付申请参数
				ccic(payInfoJson, insbOrder.getTaskid(), insbOrder.getPrvid(), insbQuoteinfo.getDeptcode());
				break;
			case "liberty":  //利宝支付申请参数
				liberty(payInfoJson, insbOrder);
				break;
			case "wxpay":
				wxpay(payInfoJson);
				break;
			case "yongan":
				yongan(payInfoJson, insbOrder.getTaskid());
				break;
			case "zhongan":
				zhongan(payInfoJson,insbOrder,insbQuoteinfo);break;
			case "unionpay":
				unionpay(payInfoJson);
				break;
			case "baofu" :
				baofu(payInfoJson, insbOrder.getTaskid(), insbOrder.getPrvid());
				break;
			case "zhufeng":  //珠峰支付申请参数
				zhufeng(payInfoJson, insbOrder.getTaskid(), insbOrder.getPrvid(), insbQuoteinfo.getDeptcode());
				break;
			case "tianan":  //天安支付申请参数
				tianan(payInfoJson, insbOrder);
				break;
			case "yangguang" ://阳光支付申请参数
				yangguang(payInfoJson, insbOrder);
				break;
			case "zhongmei" :
				zhongmei(payInfoJson, insbQuoteinfo);
				break;
			case "asiapacific" : //亚太支付申请
				asiapacific(payInfoJson, insbOrder.getTaskid(), insbOrder.getPrvid());
				break;
			case "robot":
				if(!payParam.getPayJsonStr().containsKey("sid"))
				{
					result.setStatus("fail");
					result.setMessage("参数错误，没有sid");
					return  result;

				}
				String sid=(String)payParam.getPayJsonStr().get("sid");
				if(payParam.getIsInsureQuery() != null && "true".equals(payParam.getIsInsureQuery())){
					String callbackUrl = PayConfigMappingMgr.getRobotCallbackUrl();
					try {
						logger.info("订单：" + insbOrder.getOrderno() + "发起精灵核保查询，保险公司为：" + insbOrder.getPrvid());
						String queryResult = interFaceService.goToFairyQuote(insbOrder.getTaskid(),insbOrder.getPrvid(), "admin", "insurequery@qrcode", callbackUrl, insbOrder.getOrderno(),sid );
						JSONObject reObj = JSONObject.fromObject(queryResult);
						if(reObj.getBoolean("result")){
							result.setStatus("success");
							result.setMessage("已发精灵核保查询任务，请等候......");
						}else{
							result.setStatus("fail");
							result.setMessage("系统故障，请联系管理员");
						}

						return  result;
					} catch (Exception e) {
						e.printStackTrace();
						result.setStatus("fail");
						result.setMessage("支付平台服务异常");
						return  result;
					}
				}
				else
				{
					payInfoJson.put("sid",sid);
				}
				break;
			default:
				;
		}
		if (!"99bill".equals(channelId))
			payInfoJson.put("channelId", channelId);
		buildHeader(payInfoJson);

		String jsonStr = payInfoJson.toString();
		LogUtil.info("请求支付平台参数:" + jsonStr);
		JSONObject json = null;
		try{
			jsonStr = HttpClient.sendPost(payParam.getBizId(), PayConfigMappingMgr.getPayUrl(), jsonStr);
			LogUtil.info("支付平台响应结果:" + jsonStr);
			json = JSONObject.fromObject(jsonStr);
			if (json.containsKey("code")) {
				insbOrderpayment.setModifytime(new Date());
				insbOrderpayment.setPayflowno(json.optString("bizTransactionId"));
				if(json.containsKey("quickposNo")) // 记录快刷一支的缴费号
					insbOrderpayment.setPayorderno(json.getString("quickposNo"));
				insbOrderpaymentDao.updateById(insbOrderpayment); // 保存支付轨迹
				result.setBody(json);
				if (!"0000".equals(json.getString("code"))) {
					result.setStatus("fail");
					result.setMessage(json.optString("msg"));
				}
			}
		}catch(Exception e){
			result.setStatus("fail");
			result.setMessage("支付平台服务异常");
		}
		return result;
	}

	private void unionpay(JSONObject payInfoJson) {

	}

	@Override
	public String callBack(CallBackParam params) {
		LogUtil.info("收到支付回调:" + JSONObject.fromObject(params).toString());
		threadPoolTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				INSBOrder insbOrder = new INSBOrder();
				insbOrder.setTaskid(params.getBizId().split(split)[0]);
				insbOrder.setOrderstatus("1");
				if (!StringUtils.isBlank(params.getInsOrg())) {
					insbOrder.setPrvid(params.getInsOrg());
				}
				insbOrder = insbOrderDao.selectOne(insbOrder);
				if (insbOrder == null) {
					LogUtil.info("订单都未找到:orderNo=" + params.getBizId());
					return;
				}
				if ("02".equals(insbOrder.getOrderstatus())) {
					LogUtil.info("订单已支付:orderNo=" + params.getBizId());
					return;
				}

				Double amount = 0.0;
				if (!StringUtil.isEmpty(params.getAmount())) {
					amount = Integer.valueOf(params.getAmount()) / 100.0;
				} else {
					LogUtil.info("支付回调的结果中支付金额为空:orderNo=" + params.getBizId());
				}

				Date date = new Date();
				logger.info("订单：" + params.getBizId() + "支付时间为：" + params.getTransDate());
				if (!StringUtil.isEmpty(params.getTransDate()) && params.getTransDate().contains("-")) {//如何判断paydate的格式是否是“YYYY-MM-DD HH:mm:SS”
					if (params.getTransDate().trim().length() > 10) {
						date = ModelUtil.conbertStringToDate(params.getTransDate());
					} else
						date = ModelUtil.conbertStringToNyrDate(params.getTransDate());
				} else {
					LogUtil.error("支付回调的结果中支付时间为:paydate=" + params.getTransDate() + "不符合规则！");
				}

				INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
				insbOrderpayment.setPayflowno(params.getBizTransactionId());
				insbOrderpayment = insbOrderpaymentDao.selectOne(insbOrderpayment);
				if (insbOrderpayment == null) { // 未找到，得插入一条
					insbOrderpayment = new INSBOrderpayment();
					insbOrderpayment.setTaskid(insbOrder.getTaskid()); // 设置任务id
					insbOrderpayment.setOperator(insbOrder.getAgentcode()); // 操作员

					Map<String, String> map = new HashMap<String, String>();
					map.put("taskid", insbOrder.getTaskid());
					map.put("companyid", insbOrder.getPrvid());
					INSBQuoteinfo insbQuoteinfo = insbQuoteinfoDao.getByTaskidAndCompanyid(map);
					insbOrderpayment.setSubinstanceid(insbQuoteinfo.getWorkflowinstanceid());

					insbOrderpayment.setOrderid(insbOrder.getId()); // 订单表id
					insbOrderpayment.setPaychannelid(PayConfigMappingMgr.getCmCodeByPayCode(MappingType.PAY_CHANNEL, params.getChannelId())); // 支付渠道
					insbOrderpayment.setNoti(PayConfigMappingMgr.getCmCodeByPayCode(MappingType.PAY_CHANNEL, params.getPayType())); // 支付方式
					insbOrderpayment.setAmount(amount);
					insbOrderpayment.setCurrencycode("RMB"); // 币种编码
					insbOrderpayment.setPayresult(params.getOrderState());
					insbOrderpayment.setCreatetime(new Date()); // 创建时间
					insbOrderpayment.setModifytime(new Date());
					insbOrderpayment.setPayflowno(params.getBizTransactionId());
					insbOrderpayment.setPaytime(date);
					insbOrderpayment.setTradeno(params.getPaySerialNo());
					insbOrderpayment.setCreatetime(new Date());
					insbOrderpayment.setModifytime(new Date());
					insbOrderpaymentDao.insert(insbOrderpayment);
				} else {
					if (amount > 0.0)
						insbOrderpayment.setAmount(amount);
					insbOrderpayment.setPayresult(params.getOrderState());
					insbOrderpayment.setPaytime(date);
					insbOrderpayment.setTradeno(params.getPaySerialNo());
					insbOrderpayment.setModifytime(new Date());
					insbOrderpaymentDao.updateById(insbOrderpayment);
				}


				if ("02".equals(params.getOrderState())) { // 支付成功，更新insborder然后推工作流
					insbOrder.setOrderstatus("2"); // 待承保打单
					insbOrder.setPaymentstatus("02");
					insbOrder.setPaymentmethod(insbOrderpayment.getPaychannelid());
					insbOrder.setNoti(insbOrderpayment.getNoti());
					insbOrderDao.updateById(insbOrder);
					completeTask(insbOrder, insbOrderpayment);
					//如果首单时间为空则更新代理人首单时间和taskid
					INSBAgent agent = new INSBAgent();
					agent.setAgentcode(insbOrder.getAgentcode());
					agent = insbAgentDao.selectOne(agent);
					if (null == agent.getFirstOderSuccesstime()) {
						agent.setFirstOderSuccesstime(new Date());
						agent.setTaskid(insbOrder.getTaskid());
						insbAgentDao.updateById(agent);
					}
				}
			}
		});
		return "0000";
	}

	@Override
	public CommonModel queryPayResult(String orderNo) {
		return  queryPayResult(orderNo, false);
	}

	public void save(INSBOrder insbOrder)
	{
		insbOrderDao.updateById(insbOrder);
	}

	@Override
	public CommonModel queryPayResult(String orderNo,  Boolean isUnderwriteQuote){
		CommonModel result = new CommonModel();
		result.setStatus(CommonModel.STATUS_SUCCESS);
		INSBOrder insbOrder = new INSBOrder();
		insbOrder.setOrderno(orderNo); // bizId中存放的是insbOrder表中的orderNo
		insbOrder = insbOrderDao.selectOne(insbOrder);
		if (insbOrder == null) {
			result.setStatus(CommonModel.STATUS_FAIL);
			result.setMessage("未找到订单");
			return result;
		}
		if (CommonModel.PAID_CODE.equals(insbOrder.getPaymentstatus())) {
			result.setMessage("已支付");
			result.setBody("0000");
			return result;
		}

		// 判断是否为平安二维码支付，如果是，通知精灵去查询，只有通知，没有承保查询的结果
		if(insbOrder.getPaymentmethod() != null){
			String payChannel = PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_CHANNEL, insbOrder.getPaymentmethod());
			if (StringUtils.isEmpty(payChannel)) {
				result.setStatus(CommonModel.STATUS_FAIL);
				result.setMessage("未找到匹配的支付渠道");
				return result;
			}else if("robot".equals(payChannel)){
				if(isUnderwriteQuote){//是否需要调用承保查询接口
					logger.info("平安二维码支付查询支付结果，调用承保查询接口！任务号："+  insbOrder.getTaskid() + "供应商：" + insbOrder.getPrvid());
					try {
						if(CommonModel.PAID_FAILED.equals(insbOrder.getPaymentstatus())){
							logger.info("平安二维码支付查询支付结果，调用承保查询接口！任务号："+  insbOrder.getTaskid() + "推承保查询之前状态为支付失败");
							insbOrder.setPaymentstatus(CommonModel.PAYING_CODE);
							this.save(insbOrder);
						}
						logger.info("平安二维码支付查询支付结果，调用承保查询接口！任务号：" + insbOrder.getTaskid() + "置初始状态成功！");
						String resultMap = interFaceService.goToFairyQuote(insbOrder.getTaskid(), insbOrder.getPrvid(), "admin", "approvedquery@qrcode");
						JSONObject reObj = JSONObject.fromObject(resultMap);
						if(reObj.getBoolean("result")){
							result.setStatus(CommonModel.STATUS_CHECK);
							result.setMessage("已发精灵承保查询任务，请等候......");
						}else{
							result.setStatus(CommonModel.STATUS_FAIL);
							result.setMessage("系统故障，请联系管理员");
						}
						return  result;
					} catch (Exception e) {
						logger.info("平安二维码支付查询支付结果，调用承保查询接口！任务号："+  insbOrder.getTaskid()+"通知精灵查询出现异常");
						e.printStackTrace();
						result.setStatus(CommonModel.STATUS_FAIL);
						result.setMessage("通知精灵查询出现异常，请联系管理员");
						return  result;
					}

				}else{
					if (CommonModel.PAID_FAILED.equals(insbOrder.getPaymentstatus())){
						result.setStatus(CommonModel.STATUS_FAIL);
						result.setMessage("支付失败");
						return result;
					}
					result.setStatus(CommonModel.STATUS_CHECK);
					result.setMessage("已发精灵承保查询任务，请等候......");
				}
				return  result;
			}
		}

		INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
		insbOrderpayment.setOrderid(insbOrder.getId());
		List<INSBOrderpayment> list = insbOrderpaymentDao.selectList(insbOrderpayment);
		for (INSBOrderpayment payment : list) {
			if (CommonModel.PAID_CODE.equals(payment.getPayresult())) { // 再推一次工作流
				insbOrder.setOrderstatus("2"); // 待承保打单
				insbOrder.setPaymentstatus(CommonModel.PAID_CODE);
				insbOrder.setModifytime(new Date());
				insbOrderDao.updateById(insbOrder);
				completeTask(insbOrder, payment);
				result.setMessage("已支付");
				//如果首单时间为空则更新代理人首单时间和taskid
				INSBAgent agent = new INSBAgent();
				agent.setAgentcode(insbOrder.getAgentcode());
				agent = insbAgentDao.selectOne(agent);
				if(null==agent.getFirstOderSuccesstime()){
					agent.setFirstOderSuccesstime(new Date());
					agent.setTaskid(insbOrder.getTaskid());
					insbAgentDao.updateById(agent);
				}
				return result;
			}
			if (payment.getPayflowno() != null) {
				CommonModel model = queryPayResultFromPayPlat(payment.getPayflowno());
				if (CommonModel.STATUS_SUCCESS.equals(model.getStatus())) {
					// 查询到支付成功，需要保存信息并推工作流
					JSONObject json = (JSONObject) model.getBody();
					Date date = new Date();

					if (!StringUtil.isEmpty(json.optString("transDate")) && json.optString("transDate").contains("-") ){
						logger.info("查询支付结果，订单："+ orderNo + "支付时间为：" + json.optString("transDate"));
						if(json.optString("transDate").trim().length() > 10)
							date =  ModelUtil.conbertStringToDate(json.optString("transDate").trim());
						else
							date = ModelUtil.conbertStringToNyrDate(json.optString("transDate").trim());
					}else
						LogUtil.error("支付结果查询回调信息中支付时间为:paydate=" +json.optString("transDate") +"不符合规则！");

					insbOrder.setModifytime(new Date());
					insbOrder.setOrderstatus("2"); // 待承保打单
					insbOrder.setPaymentstatus(CommonModel.PAID_CODE);
					insbOrderDao.updateById(insbOrder);
					//如果首单时间为空则更新代理人首单时间和taskid
					INSBAgent agent = new INSBAgent();
					agent.setAgentcode(insbOrder.getAgentcode());
					agent = insbAgentDao.selectOne(agent);
					if(null==agent.getFirstOderSuccesstime()){
						agent.setFirstOderSuccesstime(new Date());
						agent.setTaskid(insbOrder.getTaskid());
						insbAgentDao.updateById(agent);
					}
					payment.setPayresult(json.getString("orderState"));
					payment.setPaytime(date);
					payment.setModifytime(new Date());
					payment.setTradeno(json.getString("paySerialNo"));
					insbOrderpaymentDao.updateById(payment);

					completeTask(insbOrder, payment);
					result.setMessage("已支付");
					return result;
				}
			}
		}
		result.setStatus(CommonModel.STATUS_FAIL);
		result.setMessage("未支付");

		return result;
	}

	@Override
	public CommonModel queryPayResultFromPayPlat(String payFlowNo) {
		logger.info("查询支付平台,流水号:" + payFlowNo);
		CommonModel model = new CommonModel();
		model.setStatus("success");
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("bizId", payFlowNo);
		String result = HttpClient.doPayGet(PayConfigMappingMgr.getQueryResultUrl(), map);
		logger.info("查询支付平台响应结果:" + result);
		if (StringUtil.isEmpty(result)) {
			model.setStatus("fail");
			model.setMessage("访问支付平台消息失败:" + result);
			return model;
		}
		JSONObject json = null;
		try {
			json = JSONObject.fromObject(result);
		} catch (Exception e) {
			model.setStatus("fail");
			model.setMessage("支付平台返回信息异常");
			model.setBody(result);
			return model;
		}
		if (json.containsKey("code")) {
			if (json.getString("code").equals("0000")) {
				if (json.containsKey("orderState") && json.getString("orderState").equals("02")) {
					model.setStatus("success");
					model.setMessage("已支付");
					model.setBody(json);
				} else if (json.containsKey("orderState") && json.getString("orderState").equals("01")) {
					model.setStatus("waiting");
					if (json.containsKey("msg"))
						model.setMessage(json.getString("msg"));
					model.setBody(json);
				} else {
					model.setStatus("fail");
					if (json.containsKey("msg"))
						model.setMessage(json.getString("msg"));
					model.setBody(json);
				}
			} else {
				model.setStatus("fail");
				if (json.containsKey("msg"))
					model.setMessage(json.getString("msg"));
				model.setBody(json);
			}

		} else {
			model.setStatus("fail");
			model.setMessage("返回消息不正确！" + result);
			model.setBody(json);
		}
		return model;
	}

	@Override
	public CommonModel completeTask(INSBOrder insbOrder, INSBOrderpayment insbOrderpayment) {
		String orderNo = insbOrder.getOrderno();
		Map<String, String> insbOrderMap = new HashMap<String, String>();
		insbOrderMap.put("taskid", insbOrder.getTaskid());
		insbOrderMap.put("companyid", insbOrder.getPrvid());
		INSBQuoteinfo insbQuoteinfo = insbQuoteinfoDao.getByTaskidAndCompanyid(insbOrderMap);

		CommonModel model = new CommonModel();
		model.setStatus("success");
		String url = ValidateUtil.getConfigValue("workflow.url") + "/process/completeSubTask";
		try {
			INSBWorkflowsub sub = new INSBWorkflowsub();
			sub.setInstanceid(insbQuoteinfo.getWorkflowinstanceid());
			INSBWorkflowsub flow = insbWorkflowsubDao.selectOne(sub);

			logger.info("是否需要二支："+insbOrder.getTaskid()+", "+insbOrder.getPrvid()+", 订单号："+orderNo);
			int secPay = needSecPay(insbQuoteinfo.getInscomcode(), insbQuoteinfo.getDeptcode(), insbOrderpayment.getPaychannelid());

			Map<String, Object> map1 = new HashMap<String, Object>();
			Map<String, Object> map = new HashMap<String, Object>();

			String subTaskId = insbQuoteinfo.getWorkflowinstanceid();
			String agentId = flow.getOperator();
			if (StringUtils.isEmpty(agentId)) {
				agentId = "admin";
			}

			// 1需要二次支付，0不需要二次支付
			if (secPay == 0) {
				map1.put("issecond", "0");
				map1.put("acceptway", JSONObject.fromObject(workflowmainService.getContractcbType(insbOrder.getTaskid(), insbOrder.getPrvid(), "0", "contract")).getString("quotecode"));
			} else {
				map1.put("issecond", "1");
			}
			map.put("data", map1);
			map.put("userid", "admin");
			map.put("processinstanceid", Long.parseLong(subTaskId));
			map.put("taskName", "支付");
			JSONObject jsonObject = JSONObject.fromObject(map);
			Map<String, String> params = new HashMap<String, String>();
			params.put("datas", jsonObject.toString());
			//String retStr = null;retStr = 
			LogUtil.info("推支付工作流,订单号:" + orderNo + " params:" + jsonObject.toString());

			try {
				WorkflowFeedbackUtil.setWorkflowFeedback(null, subTaskId, "20", "Completed", "支付", "02".equals(insbOrder.getPaymentstatus())?"支付成功":"支付完成", agentId);
				HttpClientUtil.doGet(url, params);
			} catch (Exception e) {
				LogUtil.error("推支付工作流失败,订单号:" + orderNo, e);
			}
			/*LogUtil.info("推支付工作流响应,订单号:" + orderNo + " result:" + retStr);
			if (StringUtil.isEmpty(retStr)) {
				model.setStatus("fail");
				model.setMessage("工作流流转失败(工作流调用失败)");
				return model;
			}
			JSONObject obj = JSONObject.fromObject(retStr);
			if (obj.get("message").equals("success")) {*/
				model.setStatus("success");
				model.setMessage("工作流流转成功!");
			/*} else {
				model.setStatus("fail");
				model.setMessage("工作流流转失败(" + obj.getString("reason") + ")");
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			model.setStatus("fail");
			model.setMessage("工作流流转失败(" + e.getMessage() + ")");
			LogUtil.info("推支付工作流失败,订单号:" + orderNo + " 错误信息:" + e.getMessage());
		}
		return model;
	}



	private void payeco(JSONObject payInfoJson) {
		JSONObject channelParam = payInfoJson.getJSONObject("channelParam");
		String bankCardType = channelParam.getString("bankCardType");
		String idCardType = channelParam.getString("idCardType");
		channelParam.put("bankCardType", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.BANK_TYPE, bankCardType));
		channelParam.put("idCardType", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.CARD_TYPE, idCardType));
		payInfoJson.put("channelParam", channelParam);
	}

	private void yeepay(JSONObject payInfoJson) {
	}

	private void alipay(JSONObject payInfoJson) {
	}

	private void bestpay(JSONObject payInfoJson, INSBOrderpayment insbOrderpayment) {
		if("mobile".equals(payInfoJson.getString("payType"))){
			INSBOrderpayment payment = new INSBOrderpayment();
			payment.setTaskid(insbOrderpayment.getTaskid());
			List<INSBOrderpayment> insbOrderpayments = insbOrderpaymentDao.selectList(payment);
			String bizId = payInfoJson.getString("bizId");
			if(insbOrderpayments.size() > 0){
				for(INSBOrderpayment orderpayment : insbOrderpayments){
					if(bizId.compareTo(orderpayment.getPaymentransaction()) < 0){
						bizId = orderpayment.getPaymentransaction();
					}
				}
			}
			bizId = bizId.split(split)[0] + split + (Long.valueOf(bizId.split(split)[1]) + 1L);
			insbOrderpayment.setPaymentransaction(bizId);
			payInfoJson.put("bizId", bizId);
			insbOrderpaymentDao.updateById(insbOrderpayment);
		}else{
			JSONObject channelParam = payInfoJson.getJSONObject("channelParam");
			String bankCardType = channelParam.getString("bankCardType");
			String idCardType = channelParam.getString("idCardType");
			String bankCode = channelParam.getString("bankCode");
			channelParam.put("bankCardType", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.BANK_TYPE, bankCardType));
			channelParam.put("bankCode", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.BANK_CODE, bankCode));
			channelParam.put("bankName", PayConfigMappingMgr.getPayNameByCmCode(MappingType.BANK_CODE, bankCode));
			channelParam.put("idCardType", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.CARD_TYPE, idCardType));
			payInfoJson.put("channelParam", channelParam);

		}
	}

	private void tenpay(JSONObject payInfoJson) {
	}

	private void wxpay(JSONObject payInfoJson) {
		JSONObject obj = new JSONObject();
		obj.put("agentOrg", payInfoJson.getString("agentOrg")); //分账账户机构编码
		int amount = payInfoJson.getInt("amount");
//		String charge = PayConfigMappingMgr.getPayCodeByCmCode(MappingType.CHARGE_TYPE, "20");
//		BigDecimal dec = new BigDecimal(charge);
//		amount = (int) (amount * dec.doubleValue()); // 扣除3.9‰手续费 存在精度问题
//		obj.put("amount", amount);

		BigDecimal allAmount2Fen = new BigDecimal(amount);
		String rate = PayConfigMappingMgr.getPayCodeByCmCode(MappingType.CHARGE_TYPE, "20");
		BigDecimal dec = new BigDecimal(rate);

		Double partAmount2Fen = Math.floor(dec.multiply(allAmount2Fen).doubleValue());//向下取整
		obj.put("amount", partAmount2Fen.intValue()); //分账金额
		JSONArray array = new JSONArray();
		array.add(obj);
		payInfoJson.put("ledgers", array);  //分账金额
		payInfoJson.put("agentOrg", "1600000000"); //总账账户机构编码
	}

	private void _99bill(JSONObject payInfoJson, String taskId, String prvId, String deptCode) {
		// 能走一支的不走二支
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskId);
		insbPolicyitem.setInscomcode(prvId);
		List<INSBPolicyitem> list = insbPolicyitemDao.selectList(insbPolicyitem);

		INSBPaychannelmanager insbPaychannelmanager = new INSBPaychannelmanager();
		insbPaychannelmanager.setProviderid(prvId);
		insbPaychannelmanager.setDeptid(deptCode);
		insbPaychannelmanager.setPaychannelid(payInfoJson.getString("channelId"));
		List<INSBPaychannelmanager> pcmList = insbPaychannelmanagerDao.selectList(insbPaychannelmanager);

		if(pcmList.isEmpty()){
			String ptdeptcode =inscDeptService.getPingTaiDeptId(deptCode);
			insbPaychannelmanager.setDeptid(ptdeptcode);
			pcmList = insbPaychannelmanagerDao.selectList(insbPaychannelmanager);
		}

		if (pcmList.size() == 0 || !"1".equals(pcmList.get(0).getCollectiontype()) || list.size() == 0) {
			payInfoJson.put("channelId", "99bill");
			return;
		}
		String company = prvId.substring(0, 4);
		String strChannelId = null;
		switch (company) {
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
		Map<String, Object> channelParam = (Map<String, Object>) payInfoJson.get("channelParam");
		if(channelParam == null) {
			channelParam = new HashMap<String, Object>();
			payInfoJson.put("channelParam", channelParam);
		}
		if(strChannelId != null){
			payInfoJson.put("channelId", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_CHANNEL, strChannelId));
		}else{ // 不需要向保险公司申请快刷号
			payInfoJson.put("channelId", "99bill");
		}
		INSCDept dept = inscDeptService.queryById(deptCode);
		payInfoJson.put("areaCode", dept.getProvince());
		if ("2016".equals(company) || "2088".equals(company) || "2005".equals(company) || "2044".equals(company)) {
			if (list.size() == 1) {
				channelParam.put("applicantNo", list.get(0).getProposalformno());
			} else if (list.size() == 2) { // 交强险在前，商业险在后
				if ("0".equals(list.get(0).getRisktype())) {
					channelParam.put("applicantNo", list.get(0).getProposalformno() + "," + list.get(1).getProposalformno());
				} else {
					channelParam.put("applicantNo", list.get(1).getProposalformno() + "," + list.get(0).getProposalformno());
				}
			}

			//众城网页支付、众城快刷用同样的参数
			Map<String, Object> queryMap = new HashMap<>();
			queryMap.put("taskid", taskId);

			INSBQuotetotalinfo quotetotalinfo = quotetotalinfoDao.select(queryMap);

			if (StringUtils.isNotEmpty(quotetotalinfo.getInscitycode())) {
				channelParam.put("areacode", quotetotalinfo.getInscitycode());//投保地区代码(细化到地级市)
			} else {
				channelParam.put("areacode", "");
			}
		}

		if ("2016".equals(company) || "2088".equals(company)) {
			Integer forcePremium = 0;
			Integer businessPremium = 0;
			Integer taxPremium = 0;
			for(INSBPolicyitem item : list ){
				if("0".equals(item.getRisktype())){ // 商业险
					businessPremium= AmountUtil.trans2Fen(item.getDiscountCharge());
				}else if("1".equals(item.getRisktype())){ // 交强险
					forcePremium  = AmountUtil.trans2Fen(item.getDiscountCharge());
				}
			}

			INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
			insbCarkindprice.setTaskid(insbPolicyitem.getTaskid());
			insbCarkindprice.setInscomcode(insbPolicyitem.getInscomcode());
			insbCarkindprice.setInskindtype("3");
			insbCarkindprice.setInskindcode("VehicleTax");
			insbCarkindprice = insbCarkindpriceDao.selectOne(insbCarkindprice); // 车船税
			if(insbCarkindprice != null)
				taxPremium = AmountUtil.trans2Fen(insbCarkindprice.getDiscountCharge());
//			List<INSBCarkindprice> resultInsbCarkindpriceList = insbCarkindpriceDao.selectList(insbCarkindprice);
//			for (INSBCarkindprice dataInsbCarkindprice : resultInsbCarkindpriceList) {
//				Integer amount = 0;
//				if(dataInsbCarkindprice.getDiscountCharge() != null)
//					amount = AmountUtil.trans2Fen(dataInsbCarkindprice.getDiscountCharge());
//				if (dataInsbCarkindprice.getInskindtype().equals("0") || dataInsbCarkindprice.getInskindtype().equals("1")) {
//					businessPremium = businessPremium + amount;
//				}
//				if (dataInsbCarkindprice.getInskindtype().equals("2")) {
//					forcePremium = forcePremium + amount;
//				}
//				if (dataInsbCarkindprice.getInskindtype().equals("3")) {
//					taxPremium = taxPremium + amount;
//				}
//			}
			channelParam.put("forcePremium", String.valueOf(forcePremium));// 交强险总保费
			channelParam.put("businessPremium", String.valueOf(businessPremium));// 商业险总保费
			channelParam.put("taxPremium", String.valueOf(taxPremium));// 车船税保费
		}
		if ("2005".equals(company)) {
			channelParam.put("outDept", deptCode);
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("deptid", deptCode);
		map.put("providerid", prvId);
		INSBPaychannelmanager queryManager = insbPaychannelmanagerDao.queryManager(map);
		channelParam.put("merchantNo", queryManager == null ? "" : queryManager.getSettlementno());// 结算商户号
		payInfoJson.put("channelParam", channelParam);
	}

	private void huaan(JSONObject payInfoJson, String taskId, String prvId) {
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskId);
		insbPolicyitem.setInscomcode(prvId);
		List<INSBPolicyitem> list = insbPolicyitemDao.selectList(insbPolicyitem);
		Map<String, String> channelParam = (Map<String, String>) payInfoJson.get("channelParam");
		if(channelParam == null)
			channelParam = new HashMap<String, String>();
		INSCDept inscDept = inscDeptService.queryById(payInfoJson.getString("agentOrg"));
		channelParam.put("departCode", StringUtil.isEmpty(inscDept.getCity()) ? inscDept.getProvince() : inscDept.getCity());
		if(list.size() > 0){
			channelParam.put("idCardName", list.get(0).getApplicantname());
			channelParam.put("startDate", DateUtil.toString(list.get(0).getStartdate(), "yyyy-MM-dd"));
			if (list.size() == 1) {
				channelParam.put("applicantNo", list.get(0).getProposalformno());
			} else if (list.size() == 2) { // 交强险在前，商业险在后
				if ("0".equals(list.get(0).getRisktype())) {
					channelParam.put("applicantNo", list.get(0).getProposalformno() + "," + list.get(1).getProposalformno());
				} else {
					channelParam.put("applicantNo", list.get(1).getProposalformno() + "," + list.get(0).getProposalformno());
				}
			}
		}
		payInfoJson.put("channelParam", channelParam);
	}


	private void yingda(JSONObject payInfoJson, String taskId, String prvId, String deptCode) {
		logger.info("进入英大数据拼装方法taskId="+taskId+",prvId="+prvId+",deptCode="+deptCode+",payInfoJson="+payInfoJson);
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskId);
		insbPolicyitem.setInscomcode(prvId);
		List<INSBPolicyitem> list = insbPolicyitemDao.selectList(insbPolicyitem);
		Map<String, String> channelParam = (Map<String, String>) payInfoJson.get("channelParam");
		if(channelParam == null)
			channelParam = new HashMap<String, String>();
		logger.info("英大查询inscDept前");
		INSCDept inscDept = inscDeptService.queryById(payInfoJson.getString("agentOrg"));
		logger.info("英大查询inscDept后，city="+inscDept.getCity()+",province="+inscDept.getProvince());
		channelParam.put("departCode", StringUtil.isEmpty(inscDept.getCity()) ? inscDept.getProvince() : inscDept.getCity());
		logger.info("英大departCode值为:" + channelParam.get("departCode"));
		if (list.size() > 0){
			if (list.size() == 1) {
				channelParam.put("applicantNo", list.get(0).getProposalformno());
			} else if (list.size() == 2) { // 交强险在前，商业险在后
				if ("0".equals(list.get(0).getRisktype())) {
					channelParam.put("applicantNo", list.get(0).getProposalformno() + "," + list.get(1).getProposalformno());
				} else {
					channelParam.put("applicantNo", list.get(1).getProposalformno() + "," + list.get(0).getProposalformno());
				}
			}
		}

		//支付渠道信息
		Map<String, Object> pakMap = new HashMap<String, Object>();
		String ptdeptcode =inscDeptService.getPingTaiDeptId(deptCode); //找平台编码
		pakMap.put("deptId",ptdeptcode);
		pakMap.put("providerid", prvId.substring(0,4));
		pakMap.put("usetype", "2");//EDI
		pakMap.put("offset", 0);
		pakMap.put("limit", 100);
		List<INSBPrvaccountkey> pak = insbPrvaccountkey.extendsPage(pakMap);
		for(int j = 0; j<pak.size() ; j++){
			if("comCode".equals(pak.get(j).getParamname())) channelParam.put("comCode",pak.get(j).getParamvalue());//渠道代码 不可空
			if("userCode".equals(pak.get(j).getParamname())) channelParam.put("userCode",pak.get(j).getParamvalue());//userCode 不可空
		}

		Integer forcePremium = 0;
		Integer businessPremium = 0;
		Integer taxPremium = 0;
		for(INSBPolicyitem item : list ){
			if("0".equals(item.getRisktype())){ // 商业险
				businessPremium= AmountUtil.trans2Fen(item.getDiscountCharge());
			}else if("1".equals(item.getRisktype())){ // 交强险
				forcePremium  = AmountUtil.trans2Fen(item.getDiscountCharge());
			}
		}

		INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
		insbCarkindprice.setTaskid(insbPolicyitem.getTaskid());
		insbCarkindprice.setInscomcode(insbPolicyitem.getInscomcode());
		insbCarkindprice.setInskindtype("3");
		insbCarkindprice.setPreriskkind("VehicleCompulsoryIns");
		logger.info("英大insbCarkindprice查询前");
		insbCarkindprice = insbCarkindpriceDao.selectOne(insbCarkindprice); // 车船税
		if(insbCarkindprice != null) {
			taxPremium = AmountUtil.trans2Fen(insbCarkindprice.getDiscountCharge());
		}
		logger.info("英大insbCarkindprice查询后taxPremium="+taxPremium);
		channelParam.put("forcePremium", String.valueOf(forcePremium));// 交强险总保费
		channelParam.put("businessPremium", String.valueOf(businessPremium));// 商业险总保费
		channelParam.put("taxPremium", String.valueOf(taxPremium));// 车船税保费
		payInfoJson.put("channelParam", channelParam);
	}

	private void axatp(JSONObject payInfoJson, String taskId, String prvId) {

		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskId);
		insbPolicyitem.setInscomcode(prvId);
		List<INSBPolicyitem> list = insbPolicyitemDao.selectList(insbPolicyitem);
		Map<String, String> channelParam = (Map<String, String>) payInfoJson.get("channelParam");
		if(channelParam == null)
			channelParam = new HashMap<String, String>();
		if (list.size() == 1) {
			channelParam.put("applicantNo", list.get(0).getProposalformno());
			channelParam.put("InsuranceBeginTime",DateUtil.toString(list.get(0).getStartdate(), "yyyy-MM-dd HH:mm:ss"));//起保日期
			channelParam.put("payAmount",list.get(0).getPremium().toString());
		} else if (list.size() == 2) { // 交强险在前，商业险在后
			String startdate0 = DateUtil.toString(list.get(0).getStartdate(), "yyyy-MM-dd HH:mm:ss");
			String startdate1 = DateUtil.toString(list.get(1).getStartdate(), "yyyy-MM-dd HH:mm:ss");
			if ("0".equals(list.get(0).getRisktype())) {
				channelParam.put("applicantNo", list.get(0).getProposalformno() + "," + list.get(1).getProposalformno());
				channelParam.put("InsuranceBeginTime",startdate0+","+startdate1);//起保日期
				channelParam.put("payAmount",list.get(0).getPremium().toString() + "," + list.get(1).getPremium().toString());
			} else {
				channelParam.put("applicantNo", list.get(1).getProposalformno() + "," + list.get(0).getProposalformno());
				channelParam.put("InsuranceBeginTime",startdate1+","+startdate0);//起保日期
				channelParam.put("payAmount",list.get(1).getPremium().toString() + "," + list.get(0).getPremium().toString());
			}
		}

		//SELECT c.* FROM insbPerson c, insbInsured a WHERE c.id = a.personid AND c.taskid = '273178';
		INSBPerson insbPerson = insbPersonDao.selectInsuredPersonByTaskId(taskId);
		if(insbPerson == null){
			Map map = new HashMap();
			map.put("taskid",taskId);
			map.put("inscomcode",prvId);
			//SELECT b.* FROM insbinsuredhis a , insbperson b WHERE a.personid = b.id AND a.taskid = #{taskid} AND a.inscomcode = #{inscomcode};
			try {
				insbPerson = insbPersonDao.selectInsuredHisPerson(map);
				if(insbPerson == null){
					LogUtil.info(taskId+" - "+ prvId + " 被保人为空！");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		channelParam.put("CertificateNo",insbPerson.getIdcardno());//被保人证件号
		channelParam.put("PersonnelName",list.get(0).getInsuredname());//被保人姓名


		payInfoJson.put("channelParam", channelParam);
	}

	/**
	 *   安心支付传递投保单号
	 * @param payInfoJson
	 * @param taskId
	 * @param prvId
	 */
	private void anxin(JSONObject payInfoJson, String taskId, String prvId) {
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskId);
		insbPolicyitem.setInscomcode(prvId);
		List<INSBPolicyitem> list = insbPolicyitemDao.selectList(insbPolicyitem);
		Map<String, String> channelParam = (Map<String, String>) payInfoJson.get("channelParam");
		if(channelParam == null)
			channelParam = new HashMap<String, String>();
		if (list.size() == 1) {
			channelParam.put("applicantNo", list.get(0).getProposalformno());
		} else if (list.size() == 2) { // 交强险在前，商业险在后
			if ("0".equals(list.get(0).getRisktype())) {
				channelParam.put("applicantNo", list.get(0).getProposalformno() + "," + list.get(1).getProposalformno());
			} else {
				channelParam.put("applicantNo", list.get(1).getProposalformno() + "," + list.get(0).getProposalformno());
			}
		}
		payInfoJson.put("channelParam", channelParam);
	}

	/**
	 * 永诚链接支付
	 * */
	private void alltrust(JSONObject payInfoJson, String taskId, String prvId) {
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskId);
		insbPolicyitem.setInscomcode(prvId);
		List<INSBPolicyitem> list = insbPolicyitemDao.selectList(insbPolicyitem);
		Map<String, String> channelParam = (Map<String, String>) payInfoJson.get("channelParam");
		if(channelParam == null)
			channelParam = new HashMap<String, String>();
		if (list.size() == 1) {
			channelParam.put("applicantNo", list.get(0).getProposalformno());
		} else if (list.size() == 2) { // 交强险在前，商业险在后
			if ("1".equals(list.get(0).getRisktype())) {
				channelParam.put("applicantNo", list.get(0).getProposalformno() + "," + list.get(1).getProposalformno());
			} else {
				channelParam.put("applicantNo", list.get(1).getProposalformno() + "," + list.get(0).getProposalformno());
			}
		}
		payInfoJson.put("channelParam", channelParam);
	}

	/**
	 * 众城链接支付
	 */
	private void urtrust(JSONObject payInfoJson, INSBOrder insbOrder) {
		alltrust(payInfoJson, insbOrder.getTaskid(), insbOrder.getPrvid());

		Map<String, String> channelParam = (Map<String, String>) payInfoJson.get("channelParam");
		if (channelParam == null)
			channelParam = new HashMap<String, String>();

		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("taskid", insbOrder.getTaskid());

		INSBQuotetotalinfo quotetotalinfo = quotetotalinfoDao.select(queryMap);

		if (StringUtils.isNotEmpty(quotetotalinfo.getInscitycode())) {
			channelParam.put("areacode", quotetotalinfo.getInscitycode());//投保地区代码(细化到地级市)
		} else {
			channelParam.put("areacode", "");
		}

		payInfoJson.put("channelParam", channelParam);
	}

	/**
	 * 大地链接支付
	 * @param  deptCode 代理人所属机构编码
	 * */
	private void ccic(JSONObject payInfoJson, String taskId, String prvId, String  deptCode){
//		DecimalFormat df = new DecimalFormat("#.00");
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskId);
		insbPolicyitem.setInscomcode(prvId);
		List<INSBPolicyitem> list = insbPolicyitemDao.selectList(insbPolicyitem);
		Map<String, String> channelParam = (Map<String, String>) payInfoJson.get("channelParam");
		if(channelParam == null)
			channelParam = new HashMap<String, String>();
		if (list.size() > 0) {
			channelParam.put("applicantNo", list.get(0).getProposalformno());
			channelParam.put("effectDate", DateUtil.toString(list.get(0).getStartdate(), "yyyy-MM-dd"));
		}

		//支付渠道信息
		Map<String, Object> pakMap = new HashMap<String, Object>();
		String ptdeptcode =inscDeptService.getPingTaiDeptId(deptCode); //找平台编码
		pakMap.put("deptId",ptdeptcode);
		pakMap.put("providerid", prvId.substring(0, 4));//大地
		pakMap.put("usetype", "2");//EDI
		pakMap.put("offset", 0);
		pakMap.put("limit", 100);
		List<INSBPrvaccountkey> pak = insbPrvaccountkey.extendsPage(pakMap);
		for(int j = 0; j<pak.size() ; j++){
			if("channelCode".equals(pak.get(j).getParamname())) channelParam.put("channelCode",pak.get(j).getParamvalue());//渠道代码 不可空
			if("channelComCode".equals(pak.get(j).getParamname())) channelParam.put("channelComCode",pak.get(j).getParamvalue());//渠道机构代码 不可空
			if("channelProductCode".equals(pak.get(j).getParamname())) channelParam.put("channelProductCode",pak.get(j).getParamvalue());//渠道产品代码   不可空
			if("regionCode".equals(pak.get(j).getParamname())) channelParam.put("regionCode",pak.get(j).getParamvalue());//地区标识 不可空
		}
		payInfoJson.put("channelParam", channelParam);
	}

	/**
	 * 阳光支付
	 * @param payInfoJson
	 * @param insbOrder
	 */
	private void yangguang(JSONObject payInfoJson, INSBOrder insbOrder) {
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(insbOrder.getTaskid());
		insbPolicyitem.setInscomcode(insbOrder.getPrvid());
		List<INSBPolicyitem> list = insbPolicyitemDao.selectList(insbPolicyitem);
		Map<String, String> channelParam = (Map<String, String>) payInfoJson.get("channelParam");
		if(null == channelParam)
			channelParam = new HashMap<String, String>();
		//有商业险投保单号用商业险，没有用交强险
		if(list.size() == 1) {
			channelParam.put("proposalNo", list.get(0).getProposalformno());
		} else if(list.size() == 2) {
			if("0".equals(list.get(0).getRisktype())) {
				channelParam.put("proposalNo", list.get(0).getProposalformno());
			} else {
				channelParam.put("proposalNo", list.get(1).getProposalformno());
			}
		}
		payInfoJson.put("channelParam", channelParam);
	}
	
	private void zhongmei(JSONObject payInfoJson, INSBQuoteinfo insbQuoteinfo) {
		Map<String, String> channelParam = (Map<String, String>) payInfoJson.get("channelParam");
		if (null == channelParam) {
			channelParam = new HashMap<String, String>();
		}
		JSONObject jsonObject = JSONObject.fromObject(insbQuoteinfo.getNoti());
		
		channelParam.put("payUrl", jsonObject.getString("payUrl"));
		channelParam.put("proposalNo", jsonObject.getString("proposalNo"));
		payInfoJson.put("channelParam", channelParam);
	}

	/**
	 * 天安链接支付
	 */
	private void tianan(JSONObject payInfoJson, INSBOrder insbOrder) {
		Map<String, String> channelParam = (Map<String, String>) payInfoJson.get("channelParam");
		if (channelParam == null)
			channelParam = new HashMap<String, String>();

		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("taskid", insbOrder.getTaskid());

		INSBQuotetotalinfo quotetotalinfo = quotetotalinfoDao.select(queryMap);

		INSBQuoteinfo queryInsbQuoteinfo = new INSBQuoteinfo();
		queryInsbQuoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
		queryInsbQuoteinfo.setInscomcode(insbOrder.getPrvid());

		queryInsbQuoteinfo = insbQuoteinfoService.queryOne(queryInsbQuoteinfo);

		if (!StringUtil.isEmpty(queryInsbQuoteinfo)) {
			String sqMisc = queryInsbQuoteinfo.getNoti();

			com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(sqMisc);

			String tradeNo = jsonObject.getString("tradeNo");
			String proposalNo = jsonObject.getString("proposalNo");

			if (StringUtils.isNotEmpty(tradeNo)) {
				channelParam.put("orderNo", tradeNo);//支付流水号
			} else {
				channelParam.put("orderNo", "");
			}
			if (StringUtils.isNotEmpty(proposalNo)) {
				channelParam.put("proposalNo", proposalNo);//主投保单号
			} else {
				channelParam.put("proposalNo", "");
			}
		}
		payInfoJson.put("channelParam", channelParam);
	}

	/**
	 * 珠峰链接支付
	 */
	private void zhufeng(JSONObject payInfoJson, String taskId, String prvId, String  deptCode) {
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskId);
		insbPolicyitem.setInscomcode(prvId);
		List<INSBPolicyitem> list = insbPolicyitemDao.selectList(insbPolicyitem);
		Map<String, String> channelParam = (Map<String, String>) payInfoJson.get("channelParam");
		if(channelParam == null)
			channelParam = new HashMap<String, String>();
		if (list.size() > 0) {
			channelParam.put("applicantNo", list.get(0).getProposalformno());
		}

		Map<String, Object> applicantMap = new HashMap<>();
		applicantMap.put("taskid", taskId);
		applicantMap.put("inscomcode", prvId);

		INSBPerson applicantPerson = insbPersonDao.selectApplicantHisPerson(applicantMap);

		if (applicantPerson.getName() != null) {
			channelParam.put("applicantName", applicantPerson.getName());//投保人姓名
		} else {
			channelParam.put("applicantName", "");
		}
		String company = prvId.substring(0,4);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deptId", deptCode);
		map.put("providerid", company);
		map.put("usetype", 2);
		map.put("order", "asc");
		map.put("offset",0);
		map.put("limit", 10);
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = insbPrvaccountmanagerservice.getKeyDataListPage(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<INSBPrvaccountkey> rows = (List<INSBPrvaccountkey>)result.get("rows");
		for(INSBPrvaccountkey listmap:rows){
			if(listmap.getParamname().equals("CompanyCode")){
				channelParam.put("companyCode", listmap.getParamvalue());
			}
		}
		payInfoJson.put("ChannelParam",channelParam);
	}


	/**
	 * 利宝链接支付
	 */
	private void liberty(JSONObject payInfoJson, INSBOrder insbOrder) {
		alltrust(payInfoJson, insbOrder.getTaskid(), insbOrder.getPrvid());

		Map<String, String> channelParam = (Map<String, String>) payInfoJson.get("channelParam");
		if (channelParam == null)
			channelParam = new HashMap<String, String>();

		Map<String, Object> applicantMap = new HashMap<>();
		applicantMap.put("taskid", insbOrder.getTaskid());
		applicantMap.put("inscomcode", insbOrder.getPrvid());

		INSBPerson applicantPerson = insbPersonDao.selectApplicantHisPerson(applicantMap);

		if (applicantPerson.getName() != null) {
			channelParam.put("applicantPersonName", applicantPerson.getName());//投保人姓名
		} else {
			channelParam.put("applicantPersonName", "");
		}

		INSBPerson insuredPerson = insbPersonDao.selectInsuredHisPerson(applicantMap);

		if (insuredPerson.getName() != null) {
			channelParam.put("insuredPersonName", insuredPerson.getName());//被保险人姓名
		} else {
			channelParam.put("insuredPersonName", "");
		}

		payInfoJson.put("channelParam", channelParam);
	}

	/**
	 * 永安支付
	 * orderNo : 支付号
	 * */
	private void yongan(JSONObject payInfoJson, String taskId){
		INSBOrder insbOrder = new INSBOrder();
		insbOrder.setTaskid(taskId);
		insbOrder = insbOrderDao.selectOne(insbOrder);
		Map<String, String> channelParam = (Map<String, String>) payInfoJson.get("channelParam");
		if(channelParam == null)
			channelParam = new HashMap<String, String>();

		if(insbOrder.getInsorderno() != null && !"".equals(insbOrder.getInsorderno())){
			channelParam.put("orderNo",insbOrder.getInsorderno());
		}
		payInfoJson.put("channelParam", channelParam);
	}

	/**
	 * 亚太支付信息获取
	 * @param payInfoJson
	 * @param taskId
	 * @param prvId
	 */
	private void asiapacific(JSONObject payInfoJson, String taskId, String prvId) {
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskId);
		insbPolicyitem.setInscomcode(prvId);
		List<INSBPolicyitem> list = insbPolicyitemDao.selectList(insbPolicyitem);
		Map<String, String> channelParam = new HashMap<>();

		Map<String, Object> map = new HashMap<>();
		map.put("taskid", taskId);
		map.put("inskindcode", prvId);
		map.put("inskindtype", 3);//查询车船税

		List<INSBCarkindprice> carkindList = insbCarkindpriceDao.selectCarkindpriceList(map);
		//计算车船税+滞纳金
		double taxPremium = 0;
		if(carkindList.size() > 0) {
			for (INSBCarkindprice carkind : carkindList) {
				taxPremium += carkind.getDiscountCharge();
			}
			LogUtil.info("车船税+滞纳金:{}", taxPremium);
		} else {
			taxPremium = list.get(0).getTotalepremium();
			for (INSBPolicyitem item : list) {
				taxPremium -= item.getPremium();
			}
			LogUtil.info("总金额-商业险:{}", taxPremium);
		}

		JSONArray businessList = new JSONArray();
		DecimalFormat decimal = new DecimalFormat("#.00");
		decimal.setRoundingMode(RoundingMode.HALF_UP);
		for(INSBPolicyitem item : list) {
			Map<String, String> businessItem = new HashMap<>();
			businessItem.put("ProposalNo", item.getProposalformno());
			businessItem.put("PayerName", item.getCarownername());
			if("1".equals(item.getRisktype())) {
				//交强险保费 + 车船税
				double efcAmount = taxPremium + item.getPremium();
				businessItem.put("Amount", AmountUtil.trans2Fen(decimal.format(efcAmount).toString()));
			} else {
				businessItem.put("Amount", AmountUtil.trans2Fen(decimal.format(item.getPremium()).toString()));
			}
			businessList.add(businessItem);
		}
		channelParam.put("businessList", businessList.toString());
		payInfoJson.put("channelParam", channelParam);
	}

	private void baofu(JSONObject payInfoJson, String taskId, String prvId) {
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(taskId);
		insbPolicyitem.setInscomcode(prvId);
		List<INSBPolicyitem> list = insbPolicyitemDao.selectList(insbPolicyitem);
		Map<String, String> channelParam = (Map<String, String>) payInfoJson.get("channelParam");
		if(channelParam == null)
			channelParam = new HashMap<String, String>();
		if(list.size() > 0) {
			channelParam.put("applicantName", list.get(0).getApplicantname());
			channelParam.put("idCardName", list.get(0).getApplicantname());
		}

		payInfoJson.put("channelParam", channelParam);
	}

	private void buildHeader(JSONObject payInfoJson) {
//		if(payInfoJson.containsKey("ledgers") && payInfoJson.containsKey("amount")){
//			JSONArray jsonArray = payInfoJson.getJSONArray("ledgers");
//			for(int i = 0; i < jsonArray.size(); i++){
//				JSONObject jsonObj = jsonArray.getJSONObject(i);
//				jsonObj.put("amount", AmountUtil.trans2Fen(jsonObj.getString("amount")));
//			}
//		}
		payInfoJson.put("notifyUrl", "http://" + ValidateUtil.getConfigValue("localhost.ip") + ":" + ValidateUtil.getConfigValue("localhost.port")
				+ "/" + ValidateUtil.getConfigValue("localhost.projectName") + "/mobile/pay/callback");
		payInfoJson.put("notifyType", "POST");
		payInfoJson.put("paySource", PayConfigMappingMgr.getPaySource());
		String key = sign(payInfoJson);
		payInfoJson.put("sign", key);
	}

	private void setAgentOrg(JSONObject payInfoJson) {
		String deptcode = payInfoJson.getString("agentOrg");
		INSCDept legalDept = inscDeptService.getLegalPersonDept(deptcode);
		payInfoJson.put("agentOrg", legalDept.getUpcomcode());
	}

	private static String sign(JSONObject payInfoJson) {
		String signStr = "";
		for (String k : list) {
			if (StringUtils.isNotBlank(payInfoJson.optString(k, null))) {
				try {
					signStr += "&" + k + "=" + URLEncoder.encode(payInfoJson.getString(k), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		signStr = signStr.substring(1) + "&key=" + PayConfigMappingMgr.getSignKey();
		return StringUtil.md5Hex(signStr).toUpperCase();
	}

	private CommonModel pipleBanKCardInfo(INSBAgent insbAgent, JSONObject payInfoJson,String bankCardInfoId){
		String channelId=PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_CHANNEL,payInfoJson.getString("channelId"));
		CommonModel model =new CommonModel();
		model.setStatus("success");
		if(channelId.equals("bestpay")){
			try{
				AppPaymentyzf yzf = appPaymentyzfDao.selectById(bankCardInfoId);
				if(!insbAgent.getName().equals(yzf.getName())){
					model.setStatus("fail");
					model.setMessage("银行卡与代理人姓名不一致！");
					return model;
				}
				LogUtil.info(insbAgent.getName() + "的银行卡信息:" + yzf.getBankcardType() + yzf.getFkbankcode() + yzf.getIdcradType() );
				JSONObject params = payInfoJson.getJSONObject("channelParam");
				params.put("bankCardNo", yzf.getBankcardno());
				params.put("bankCardType", yzf.getBankcardType()); // 在后面会做映射
				params.put("bankCardMobile", yzf.getPhone());
				params.put("bankArea", yzf.getKhcitycode());
				params.put("bankCode", yzf.getFkbankcode());
				params.put("bankName", yzf.getFkbankname());
				params.put("idCardName", yzf.getName());
				params.put("idCardNo", yzf.getIdcardno());
				params.put("idCardType", yzf.getIdcradType());
				params.put("idCardAddress", yzf.getRelationaddress());
				if(!"cashcardflag".equals(yzf.getBankcardType())){
					params.put("creditValidTime", (yzf.getPeriodmonth().length() == 1 ? "0" : "") + yzf.getPeriodmonth() + yzf.getPeriodyear().substring(2));
					params.put("creditValidCode", yzf.getValiadatecode());
				}
				payInfoJson.put("channelParam", params);
				LogUtil.info(insbAgent.getName() + "-channelParam:" + JSONObject.fromObject(params));
				model.setStatus("success");
				model.setMessage("加载银行卡成功！");
				model.setBody(payInfoJson);
				return model;
			}catch(Exception e){
				LogUtil.error("翼支付签约信息异常", e);
				model.setStatus("fail");
				model.setMessage("查询翼支付签约信息失败！");
				return model;
			}
		}
		return null;
	}


	/**
	 * 众安分期支付
	 * */
	private void zhongan(JSONObject payInfoJson,INSBOrder insbOrder,INSBQuoteinfo quoteinfo) {
		//请求体
		String bizId = payInfoJson.getString("bizId");
		logger.info("进入zhongan方法===:bizId=" + bizId + ",payInfoJson=" + payInfoJson);
		INSBOrderpayment orderpayment = new INSBOrderpayment();
		orderpayment.setPaymentransaction(bizId);
		orderpayment = insbOrderpaymentService.queryOne(orderpayment);
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("taskid", orderpayment.getTaskid());
		INSBQuotetotalinfo quotetotalinfo = quotetotalinfoDao.select(queryMap);
		Map<String ,Object> channelParam = new HashMap<>();
		if(quotetotalinfo.getInsprovincename()!=null&& quotetotalinfo.getInscityname()!=null&&
				StringUtils.isNotEmpty(quotetotalinfo.getInsprovincename())&&StringUtils.isNotEmpty(quotetotalinfo.getInscityname())){
			channelParam.put("insureArea", quotetotalinfo.getInsprovincename() + quotetotalinfo.getInscityname());//投保地区(细化到地级市)
		}
		else if(payInfoJson.containsKey("insureArea"))
		{
			String insureAreaEx = payInfoJson.getString("insureArea");
			channelParam.put("insureArea",insureAreaEx);
		}
		else{
			channelParam.put("insureArea", "");
		}

		INSBCarinfohis carinfohisParam = new INSBCarinfohis();
		carinfohisParam.setTaskid(insbOrder.getTaskid());
		carinfohisParam.setInscomcode(insbOrder.getPrvid());
		INSBCarinfohis carinfohis = carinfohisDao.selectOne(carinfohisParam);
		Date registDate = carinfohis.getRegistdate();//车辆初登日期 日期格式YYYYMMDD
		channelParam.put("debutDate", new java.text.SimpleDateFormat("yyyyMMdd").format(registDate));//初登日期

		logger.info("查询投保人身份证号，任务号：" + insbOrder.getTaskid() + "，保险公司：" + insbOrder.getPrvid());
		Map<String, Object> applicantMap = new HashMap<>();
		applicantMap.put("taskid",insbOrder.getTaskid());
		applicantMap.put("inscomcode",insbOrder.getPrvid());
		INSBPerson applicantPerson = insbPersonDao.selectApplicantHisPerson(applicantMap);
		if(applicantPerson.getName() != null){
			channelParam.put("policyHoldersName",applicantPerson.getName());//投保人姓名
		}else{
			channelParam.put("policyHoldersName", "");
		}
		if(applicantPerson.getIdcardtype()  == 0){
			channelParam.put("policyHoldersCertId",applicantPerson.getIdcardno());//投保人身份证号
		}else{
			channelParam.put("policyHoldersCertId", "");
		}
		logger.info("任务号：" + insbOrder.getTaskid()+"的订单，投保人姓名：" + applicantPerson.getName() +"投保人身份证号：" + applicantPerson.getIdcardno() );

		INSBPerson carOwner = insbPersonDao.selectCarOwnerPersonByTaskId(insbOrder.getTaskid());
		if(carOwner.getIdcardtype() == 0){
			channelParam.put("carOwnerCertId",carOwner.getIdcardno()); //车主身份证号
		}else {
			channelParam.put("carOwnerCertId", "");
		}
		logger.info("任务号：" + insbOrder.getTaskid()+"的订单，车主身份证号" + carOwner.getIdcardno() );

		INSBPerson insuredPerson = insbPersonDao.selectInsuredHisPerson(applicantMap);
		if(insuredPerson.getIdcardtype() == 0){
			channelParam.put("insuredCertId",insuredPerson.getIdcardno());//被保险人身份证号码
		}else{
			channelParam.put("insuredCertId", "");
		}
		logger.info("任务号：" + insbOrder.getTaskid()+"的订单，被保险人身份证号" + insuredPerson.getIdcardno() );

		logger.info("查询车牌号，任务号：" + insbOrder.getTaskid() + "，保险公司：" + insbOrder.getPrvid());
		Map<String,String> quoteinfoMap = new HashMap<>();
		quoteinfoMap.put("taskid",insbOrder.getTaskid());
		quoteinfoMap.put("companyid", insbOrder.getPrvid());
		INSBQuoteinfo insbQuoteinfo = insbQuoteinfoDao.getByTaskidAndCompanyid(quoteinfoMap);
		String carNo =  insbQuoteinfo.getPlatenumber();
		if(null != carNo && !"".equals(carNo) && !"新车未上牌".equals(carNo) ){
			channelParam.put("carNo",carNo);//车牌号
		}else{
			if(null != carinfohis.getVincode()){
				logger.info("任务号：" + insbOrder.getTaskid() +"的车牌为空，车架号为：" + carinfohis.getVincode());
				channelParam.put("carNo", carinfohis.getVincode().substring((carinfohis.getVincode().length() - 6), carinfohis.getVincode().length()));
			}else{
				logger.info("任务号：" + insbOrder.getTaskid() +"的车牌为空，车架号为也为空" );
				channelParam.put("carNo", "");
			}
		}

		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(insbOrder.getTaskid());
		insbPolicyitem.setRisktype("0"); //商业险
		insbPolicyitem.setInscomcode(insbOrder.getPrvid());
		insbPolicyitem = insbPolicyitemService.queryOne(insbPolicyitem);
		Boolean bizEmptyFlag = true;
		if(insbPolicyitem != null){
			bizEmptyFlag = false;
			Date startDate = insbPolicyitem.getStartdate();
			logger.info("任务号：" + insbOrder.getTaskid()+"含有商业险保单生效日，保险公司：" + insbOrder.getPrvid()+ "商业险保单生效日：" + startDate);
			channelParam.put("policyNo", new java.text.SimpleDateFormat("yyyyMMdd").format(startDate));//商业险保单生效日
		}

		channelParam.put("agentId", insbOrder.getAgentcode());//agentId
		channelParam.put("secondHandCarFlag",carinfohis.getIsTransfercar()); //是否过户	0-否 1-是

		INSBCarmodelinfohis carmodelinfohis = insbCarmodelinfohisDao.selectModelInfoByTaskIdAndPrvId(applicantMap);
		if(null != carmodelinfohis.getTaxprice() && carmodelinfohis.getTaxprice() > 0 ) {//新车购置价
			BigDecimal b1 = new BigDecimal(carmodelinfohis.getTaxprice()).setScale(2, RoundingMode.HALF_UP);
			channelParam.put("carPrice", b1.doubleValue());
		}else{
			channelParam.put("carPrice", 0);
		}
		logger.info("任务号：" + insbOrder.getTaskid() + "的车辆购置价为：" + (carmodelinfohis.getTaxprice()>0 ? carmodelinfohis.getTaxprice():0));

		logger.info("查询是否购买交强险参数，任务号：" + insbOrder.getTaskid()+"，保险公司：" + insbOrder.getPrvid());
		INSBPolicyitem traPolicyitem = new INSBPolicyitem();
		traPolicyitem.setTaskid(insbOrder.getTaskid());
		traPolicyitem.setRisktype("1"); //交强险
		traPolicyitem.setInscomcode(insbOrder.getPrvid());
		traPolicyitem = insbPolicyitemService.queryOne(traPolicyitem);
		if(traPolicyitem != null){
			logger.info("订单：" + insbOrder.getTaskid() + "购买了交强险");
			channelParam.put("compulsoryInsurance","1");//是否购买交强险0-否 1-是
			if(bizEmptyFlag){
				Date traStartDate = traPolicyitem.getStartdate();
				logger.info("任务号：" + insbOrder.getTaskid()+"是投的单交，保险公司：" + insbOrder.getPrvid()+ "交强险保单生效日：" + traStartDate);
				channelParam.put("policyNo", new java.text.SimpleDateFormat("yyyyMMdd").format(traStartDate));//交强险保单生效日
			}
		}else{
			logger.info("订单：" + insbOrder.getTaskid() + "没有购买交强险");
			channelParam.put("compulsoryInsurance","0");
		}

		channelParam.put("useCharacter",carinfohis.getCarproperty()); //车辆使用性质
		channelParam.put("vehicleType",carinfohis.getProperty());//"vehicleType ": "车辆所属性质	0-个人用车 1-企业用车 2-机关团队用车" insbcarinfohis property
		payInfoJson.put("channelParam",channelParam);
	}

	private static List<String> list = new ArrayList<String>();
	static {
		list.add("agentOrg");
		list.add("amount");
		list.add("areaCode");
		list.add("bizId");
		list.add("bizTransactionId");
		list.add("channelId");
		list.add("channelName");
		list.add("code");
		list.add("description");
		list.add("insOrg");
		list.add("insSerialNo");
		list.add("ipAddress");
		list.add("msg");
		list.add("nonceStr");
		list.add("notifyType");
		list.add("notifyUrl");
		// list.add("orderNo");
		list.add("orderState");
		list.add("payResult");
		list.add("payResultDesc");
		list.add("paySerialNo");
		list.add("paySource");
		list.add("payType");
		list.add("payTypeDesc");
		list.add("payUrl");
		list.add("reference");
		list.add("showurl");
		list.add("transDate");
		list.add("validTime");
		list.add("retUrl");
		Collections.sort(list);
	}

	public Date getPayTime(Object taskid, Object inscomcode) {
		LogUtil.info("getPayTime "+taskid+", "+inscomcode);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("taskid", taskid);
		map.put("inscomcode",inscomcode);
		List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectPolicyitemByInscomTask(map);
		Date syStartDate = null, jqStartDate = null;
		for(INSBPolicyitem insbPolicyitem : policyitemList){
			if("0".equals(insbPolicyitem.getRisktype())){
				syStartDate = insbPolicyitem.getStartdate();
			}else{
				jqStartDate = insbPolicyitem.getStartdate();
			}
		}
		if(syStartDate ==null && jqStartDate == null) {
			return null;
		}
		//获取流程走到支付节点的时间
		INSBWorkflowsubtrack insbWorkflowsubtrack = insbWorkflowsubtrackDao.selectOneByTaskidAndInscomcode(map);
		if(null != insbWorkflowsubtrack){
			//支付有效期
			INSBProvider insbProvider = insbProviderDao.selectById(inscomcode.toString());
			//选取先过期的日期
			Date quotesuccessTimes = null;
			if(null != insbProvider){
				if(null == insbProvider.getPayvalidity()){
					quotesuccessTimes = ModelUtil.gatFastPaydate(syStartDate, jqStartDate);
				}else{
					quotesuccessTimes = ModelUtil.gatFastPaydateToNow(syStartDate, jqStartDate, insbWorkflowsubtrack.getCreatetime(), insbProvider.getPayvalidity());
				}
			}
			LogUtil.info("getPayTime "+taskid+", "+inscomcode+"支付到期时间="+quotesuccessTimes);
			return quotesuccessTimes;
		}
		return null;
	}

	public static void main(String[] args) {

		String jsonStr = "{\n" +
				"    \"channelId\": \"robot\",\n" +
				"    \"channelName\": \"\",\n" +
				"    \"payType\": \"qrcode\",\n" +
				"    \"payTypeDesc\": \"\",\n" +
				"    \"paySource\": \"zzb.system.core\",\n" +
				"    \"amount\": 5643200,\n" +
				"    \"notifyUrl\": \"http://119.29.64.47:7080/cm/mobile/pay/callback\",\n" +
				"    \"notifyType\": \"POST\",\n" +
				"    \"agentOrg\": \"1261000000\",\n" +
				"    \"insOrg\": \"2011440101\",\n" +
				"    \"bizId\": \"16445352X146000000\",\n" +
				"    \"channelParam\": {},\n" +
				"    \"areaCode\": \"\",\n" +
				"    \"sign\": \"\",\n" +
				"    \"retUrl\": \"https://morg.52zzb.com/#/insuredInfo/2011440101-16445352\"\n" +
				"}";

		JSONObject json = JSONObject.fromObject(jsonStr);
		String signStr = sign(json);
		System.out.println("signStr = "+signStr);

	}
}
