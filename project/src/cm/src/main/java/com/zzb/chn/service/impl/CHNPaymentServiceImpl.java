package com.zzb.chn.service.impl;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCDeptService;
import com.common.HttpClientUtil;
import com.common.ModelUtil;
import com.common.WorkflowFeedbackUtil;
import com.common.redis.IRedisClient;
import com.zzb.chn.bean.Channel;
import com.zzb.chn.bean.PayInfo;
import com.zzb.chn.bean.QuoteBean;
import com.zzb.chn.service.CHNChannelQuoteService;
import com.zzb.chn.service.CHNChannelService;
import com.zzb.chn.service.CHNPaymentService;
import com.zzb.chn.util.JsonUtils;
import com.zzb.cm.dao.INSBApplicantDao;
import com.zzb.cm.dao.INSBApplicanthisDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBApplicant;
import com.zzb.cm.entity.INSBApplicanthis;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.conf.dao.CHNBankaccountinfoDao;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBAgreementpaymethodDao;
import com.zzb.conf.dao.INSBChannelDao;
import com.zzb.conf.dao.INSBOrderpaymentDao;
import com.zzb.conf.dao.INSBPaychannelmanagerDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.entity.CHNBankaccountinfo;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBChannel;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.entity.INSBPaychannelmanager;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.service.INSBChannelagreementService;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.conf.service.INSBWorkflowmaintrackService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.AppMyOrderInfoService;
import com.zzb.mobile.util.AmountUtil;
import com.zzb.mobile.util.HttpClient;
import com.zzb.mobile.util.MappingType;
import com.zzb.mobile.util.PayConfigMappingMgr;

import net.sf.json.JSONObject;

@Service
@Transactional
public class CHNPaymentServiceImpl implements CHNPaymentService {
    @Resource
    private INSBChannelagreementService insbChannelagreementService;
    @Resource
    private INSBOrderpaymentDao insbOrderpaymentDao;
    @Resource
    private INSBOrderDao insbOrderDao;
    @Resource
    private INSBQuoteinfoDao insbQuoteinfoDao;
    @Resource
    private INSBPolicyitemDao insbPolicyitemDao;
    @Resource
    private INSBAgentDao insbAgentDao;
    @Resource
    private INSCDeptDao inscDeptDao;
    @Resource
    private INSBApplicanthisDao insbApplicanthisDao;
    @Resource
    private INSBApplicantDao insbApplicantDao;
    @Resource
    private INSBPersonDao insbPersonDao;
    @Resource
    private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
    @Resource
    private INSBProviderDao insbProviderDao;
    @Resource
    private INSBAgreementpaymethodDao insbAgreementpaymethodDao;
    @Resource
    private INSBChannelDao insbChannelDao;
    @Resource
    private INSBWorkflowsubDao insbWorkflowsubDao;
    @Resource
    private INSBPaychannelmanagerDao insbPaychannelmanagerDao;
    @Resource
    private INSCDeptService inscDeptService;
    @Resource
    private CHNBankaccountinfoDao chnBankaccountinfoDao;
    @Resource
    private INSBWorkflowmaintrackService insbWorkflowmaintrackService;
    @Resource
	private INSBWorkflowmainService workflowmainService;
    @Resource
    private IRedisClient redisClient;
    @Resource
    private AppMyOrderInfoService appMyOrderInfoService;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private CHNChannelService chnService;
    @Autowired
    private CHNChannelQuoteService quoteService;

    private static String split = "X";
    private static String refundSplit = "Y";

    /**
     * 渠道验证请求有效性接口
     * 返回配置的支付方式列表
     */
    @Override
    public PayInfo verify(PayInfo payInfo) {
        PayInfo result = new PayInfo();
        result.setCode("0000");
        String jsonStr = String.valueOf(redisClient.get(CHNPaymentService.CHANNEL_PAYMENT_MODULE, payInfo.getBizId()));
        LogUtil.info("获取redis支付数据成功,bizId=" + payInfo.getBizId() + ",quoteBean=" + jsonStr);
        if (StringUtil.isEmpty(jsonStr)) {
            result.setCode("13");
            result.setMsg("支付请求已失效，请重新请求支付");
            return result;
        }
        QuoteBean quoteBean = JsonUtils.deserialize(jsonStr, QuoteBean.class);
        long invalidTime = Long.parseLong(quoteBean.getCreateTime());
        //申请支付后 超过30分钟视为无效支付
        if ((System.currentTimeMillis() - invalidTime) > CHNPaymentService.PAYURL_VALIDTIME_SECONDS * 1000) {
            redisClient.del(CHNPaymentService.CHANNEL_PAYMENT_MODULE, payInfo.getBizId());
            result.setCode("13");
            result.setMsg("支付请求已失效，请重新请求支付");
            return result;
        }
        INSBOrder insbOrder = new INSBOrder();
        insbOrder.setTaskid(quoteBean.getTaskId());
        insbOrder.setPrvid(quoteBean.getPrvId());
        insbOrder = insbOrderDao.selectOne(insbOrder);
        if (insbOrder == null) {
            result.setCode("13");
            result.setMsg("支付订单不存在");
            return result;
        }
        result.setTaskId(insbOrder.getTaskid());
        result.setBizId(insbOrder.getOrderno());
        result.setPrvId(insbOrder.getPrvid());
        result.setRetUrl(quoteBean.getRetUrl()); // TODO 如果为空，则从后台取渠道的跳转地址

        int totalAmount = AmountUtil.trans2Fen(insbOrder.getTotalpaymentamount());
        // 查找已经支付的金额
        INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
        insbOrderpayment.setTaskid(quoteBean.getTaskId());
        insbOrderpayment.setPayresult("02");
        List<INSBOrderpayment> insbOrderpayments = insbOrderpaymentDao.selectList(insbOrderpayment);
        int paiedAmount = 0;
        if(insbOrderpayments.size() > 0){
        	for(INSBOrderpayment orderpayment : insbOrderpayments){
        		paiedAmount += AmountUtil.trans2Fen(orderpayment.getAmount());
        	}
        }
        // TODO 根据规则计算待付金额
        int waitAmout = totalAmount - paiedAmount;
        result.setAmount(waitAmout);
        if(waitAmout <= 0){
        	result.setMsg("待支付金额必须大于零");
        	result.setCode("0000");
        	return result;
        }
        if ("2".equals(insbOrder.getOrderstatus())) { // 已经足额支付
        	result.setMsg("已支付");
        	result.setOrderState("02");
        	INSBOrderpayment orderpayment = new INSBOrderpayment();
        	orderpayment.setTaskid(quoteBean.getTaskId());
        	orderpayment.setOrderid(insbOrder.getId());
        	orderpayment = insbOrderpaymentDao.selectOne(orderpayment);
        	result.setBizTransactionId(orderpayment.getPayflowno());
        	
        	return result;
        }
        // 查询代理人机构
        Map<String, String> map = new HashMap<String,String>();
		map.put("taskid", quoteBean.getTaskId());
		map.put("companyid", quoteBean.getPrvId());
		INSBQuoteinfo insbQuoteinfo = insbQuoteinfoDao.getByTaskidAndCompanyid(map);
		INSBProvider insbProvider = insbProviderDao.selectById(insbQuoteinfo.getInscomcode().substring(0, 4));
		result.setProductName(insbProvider.getPrvshotname());
        INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
        insbPolicyitem.setTaskid(quoteBean.getTaskId());
        insbPolicyitem.setInscomcode(insbQuoteinfo.getInscomcode());
        insbPolicyitem = insbPolicyitemDao.selectList(insbPolicyitem).get(0);
        INSBAgent insbAgent = insbAgentDao.selectByJobnum(insbPolicyitem.getAgentnum());
        INSCDept inscDept = inscDeptDao.selectById(insbAgent.getDeptid());
        result.setAreaCode(inscDept.getCity());
        // 查询投保人信息
        INSBApplicant insbApplicant = new INSBApplicant();
        INSBApplicanthis insbApplicanthis = new INSBApplicanthis();
        insbApplicanthis.setTaskid(quoteBean.getTaskId());
        insbApplicanthis.setInscomcode(insbQuoteinfo.getInscomcode());
        insbApplicanthis = insbApplicanthisDao.selectOne(insbApplicanthis);
        if (insbApplicanthis == null) {
            insbApplicant = new INSBApplicant();
            insbApplicant.setTaskid(quoteBean.getTaskId());
            insbApplicant = insbApplicantDao.selectOne(insbApplicant);
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
        INSBPerson insbPerson = insbPersonDao.selectById(insbApplicanthis.getPersonid());
        Map<String, String> channelParam = new HashMap<String, String>();
        channelParam.put("applicantMobile", insbPerson.getCellphone());
        channelParam.put("applicantName", insbPerson.getName());
        channelParam.put("applicantCardNo", insbPerson.getIdcardno());
        channelParam.put("applicantCardType", insbPerson.getIdcardtype() == null ? null : insbPerson.getIdcardtype().toString());
        result.setChannelParam(channelParam);
        LogUtil.info("关闭verify方法，bizId=" + payInfo.getBizId() + ",result=" + result);
        
        quoteBean.setInsureAreaCode(insbQuoteinfo.getInscitycode());
        result.setChannelList(getPayChannels(payInfo, quoteBean));
        return result;
    }
    
    //查询配置获得支持的支付方式 
    private List<Channel> getPayChannels(PayInfo payInfo, QuoteBean quoteBean){
    	List<Channel> listResult = new ArrayList<Channel>();
    	
    	String channelId = quoteBean.getChannelId();
    	String prvId = quoteBean.getPrvId();
    	String city = quoteBean.getInsureAreaCode();
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("channelinnercode", channelId);
		map.put("providerid", prvId);
		map.put("city", city);
    	List<Map<String, Object>> payChannels = insbAgreementpaymethodDao.getByChannelinnercodeAndPrvid(map);
        LogUtil.info("获取到payChannels渠道支付方式:%s", payChannels);
    	
    	for(Map<String, Object> itMap : payChannels){
    		String id = (String)itMap.get("id");
    		String paytype = (String)itMap.get("paytype");
    		
    		String payChannelId = PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_CHANNEL, id);
    		String payChannelName = PayConfigMappingMgr.getPayNameByCmCode(MappingType.PAY_CHANNEL, id);
    		String payType = PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_TYPE, paytype);
    		String payTypeDesc = PayConfigMappingMgr.getPayNameByCmCode(MappingType.PAY_TYPE, paytype);
    		
    		if (payChannelId != null && !"".equals(payChannelId)) {
	    		Channel channel = new Channel();
	            channel.setChannelId(payChannelId);
	            channel.setChannelName(payChannelName);
	            channel.setPayType(payType);
	            channel.setPayTypeDesc(payTypeDesc);
	            listResult.add(channel);
    		}
    	}
        LogUtil.info("获取配置中的支付方式列表:%s", listResult.toArray());
    	
    	return listResult;
    }
    
    //推二支
    private void secondPay(QuoteBean quoteBean, String subId) {
    	String url = ValidateUtil.getConfigValue("workflow.url") + "/process/completeSubTask";

        Map<String ,Object> mapPay=new HashMap<>();
        Map<String,Object> mapPayFlow =new HashMap<>();

        mapPay.put("issecond", "1");
        mapPayFlow.put("data",mapPay);
        mapPayFlow.put("userid", "admin");
        mapPayFlow.put("processinstanceid", Long.parseLong(subId));
        mapPayFlow.put("taskName", "支付");
        JSONObject jsonObject = JSONObject.fromObject(mapPayFlow);
        Map<String, String> params = new HashMap<>();
        params.put("datas", jsonObject.toString());

        WorkflowFeedbackUtil.setWorkflowFeedback(null, subId, "20", "Completed", "支付", "支付成功", "admin");

        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
            	int iCount = 0;
          	
            	while (iCount <= 3) { //最多推4次
            		if (iCount != 0) { //第一次不sleep
                		try {
							Thread.sleep(iCount * 1000);
						} catch (InterruptedException e) {
							LogUtil.error(iCount + "secondPay-Thread.sleep异常：" + e.getMessage());
							e.printStackTrace();
						}
                	}
            		
	            	LogUtil.info(iCount + "渠道推支付工作流" + quoteBean.getTaskId() + "params-datas:" + jsonObject.toString());
	            	String retStr = HttpClientUtil.doGet(url, params);
	            	LogUtil.info(iCount + "渠道推支付工作流" + quoteBean.getTaskId() + "retStr:" + retStr);
	
			        if ( StringUtils.isNotEmpty(retStr) && "success".equals(JSONObject.fromObject(retStr).get("message")) ) {
			            LogUtil.info("渠道推支付工作流成功" + quoteBean.getTaskId());
			            break;
			        }
			        
			        iCount++;
            	}
            }    
        } );
    }
    
    //提供给cm验证码成功的时候调用去推新流程的二支
    @Override
    public boolean chnPinCodeSecondPay(String taskId, String prvId) throws Exception {
    	String qbJsonStr = String.valueOf( redisClient.get(CHNPaymentService.CHANNEL_PAYMENT_MODULE, "chnPinCodeSecondPay" + taskId + prvId) );
    	LogUtil.info(taskId + prvId + "chnPinCodeSecondPay:" + qbJsonStr);
    	
    	if (StringUtil.isNotEmpty(qbJsonStr)) {
    		redisClient.del(CHNPaymentService.CHANNEL_PAYMENT_MODULE, "chnPinCodeSecondPay" + taskId + prvId);
    		QuoteBean quoteBean = JsonUtils.deserialize(qbJsonStr, QuoteBean.class);
    		QuoteBean qbResult = callOk(quoteBean);
    		if ( "00".equals(qbResult.getRespCode()) ) {
    			return true;
    		}
    	}
    	
    	return false;
    }

    //旧流程备用金支付/新流程核保成功推二支
    @Override
    public QuoteBean callOk(QuoteBean quoteBean) throws Exception {
        QuoteBean result = new QuoteBean();
        
        if (null == quoteBean || StringUtils.isEmpty(quoteBean.getTaskId()) || 
        		StringUtils.isEmpty(quoteBean.getPrvId()) || StringUtils.isEmpty(quoteBean.getChannelId())) {
            result.setRespCode("01");
            result.setErrorMsg("任务号、渠道id和供应商编码不能为空");
            return result;
        } else if (null == quoteBean.getInsureInfo() || StringUtils.isEmpty(quoteBean.getInsureInfo().getTotalPremium())){
            result.setRespCode("01");
            result.setErrorMsg("支付金额不能为空");
            return result;
        }
        String taskid = quoteBean.getTaskId();
        String prvid = quoteBean.getPrvId();
        String channelId = quoteBean.getChannelId();
        Map<String, Object> qmap = new HashMap<>();
        qmap.put("taskid", taskid);
        qmap.put("purchaserchannel", channelId);
        INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.select(qmap);

        if(null == quotetotalinfo){
            result.setRespCode("01");
            result.setErrorMsg("任务号：" + taskid + "或渠道id：" + channelId + "不存在");
            return result;
        }
        
        if ( !"FirstPayLastInsure".equals(quotetotalinfo.getSourceFrom()) && 
        	 !quoteService.hasInterfacePower("17", quotetotalinfo.getInscitycode(), channelId) ) { //17-ok旧流程备用金支付
        	result.setRespCode("01");
        	result.setErrorMsg("没有调用该接口的权限");
            return result;
        }
        
        Date payValidDate = chnService.getPayValidDate(taskid, prvid, false, true);
        LogUtil.info("支付有效日期为:[" + payValidDate + "]");
        boolean pinCodeBJCheck = chnService.pinCodeBJCheck(quotetotalinfo, prvid, payValidDate);
        if ( !pinCodeBJCheck ) {
        	//新流程要等待验证码成功才能推二支
        	if ( "FirstPayLastInsure".equals(quotetotalinfo.getSourceFrom()) ) {
            	long seconds = (payValidDate.getTime() - new Date().getTime()) / 1000;
        		redisClient.set(CHNPaymentService.CHANNEL_PAYMENT_MODULE, "chnPinCodeSecondPay" + taskid + prvid, 
        				JsonUtils.serialize(quoteBean), (int)seconds);
        	}
        	
        	result.setRespCode("01");
        	result.setMsgType("02"); 
            result.setErrorMsg("北京地区非企业车投保，请在有效期内补充被保人资料");
            return result;
        }
        
        /**2016-10-27 16:17:54 chenjianglong add 任务超过支付有效期不能支付逻辑 begin**/
        if ( !"FirstPayLastInsure".equals(quotetotalinfo.getSourceFrom()) && null != payValidDate ) { //新流程核保成功推二支的情况不检查支付有效期
            int c = new Date().compareTo(payValidDate);
            LogUtil.info("支付有效日期比较结果:[" + c + "]");
            if (c > 0) {
                result.setRespCode("01");
                result.setErrorMsg("您好，您的订单已超过支付有效期，请重新提交报价，感谢您的支持！");
                return result;
            }
        }
        /**2016-10-27 16:17:54 chenjianglong add 任务超过支付有效期不能支付逻辑 end**/

        Map<String, String> insbOrderMap = new HashMap<String, String>();
        insbOrderMap.put("taskid", taskid);
        insbOrderMap.put("companyid", prvid);
        INSBQuoteinfo quoteinfo = insbQuoteinfoDao.getByTaskidAndCompanyid(insbOrderMap);
        if(quoteinfo == null){
            result.setRespCode("01");
            result.setErrorMsg("任务号：" + taskid + "和供应商编码" + prvid + "不存在");
            return result;
        }
        double amount = Double.parseDouble(quoteBean.getInsureInfo().getTotalPremium());
        INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
        insbPolicyitem.setTaskid(taskid);
        insbPolicyitem.setInscomcode(prvid);
        insbPolicyitem = insbPolicyitemDao.selectList(insbPolicyitem).get(0);
        if(amount != insbPolicyitem.getTotalepremium()){
            result.setRespCode("01");
            result.setErrorMsg("任务号：" + taskid + "和供应商编码"+ prvid + "的支付金额不正确，输入金额：" + amount + "实际金额" + insbPolicyitem.getTotalepremium());
            return result;
        }
        String subId = quoteinfo.getWorkflowinstanceid(); //quoteinfo.getWorkflowinstanceid() 默认子工作流已经存在
        
        if ( !"FirstPayLastInsure".equals(quotetotalinfo.getSourceFrom()) ) {
            INSBWorkflowsub sub = new INSBWorkflowsub();
            sub.setInstanceid(subId);
            INSBWorkflowsub flow = insbWorkflowsubDao.selectOne(sub);
            if(null==flow || !"20".equals(flow.getTaskcode().trim())){
                result.setRespCode("01");
                result.setErrorMsg("任务号：" + taskid + "和供应商编码" + prvid + "无效的支付状态");
                return result;
            }
        }

        secondPay(quoteBean, subId); //推二支
        
        if ("channel".equals(quotetotalinfo.getSourceFrom())) {
        	INSBOrder insbOrder = new INSBOrder();
    		insbOrder.setTaskid(taskid);
    		insbOrder.setPrvid(prvid);
    		insbOrder = insbOrderDao.selectOne(insbOrder);
    		insbOrder.setPaymentmethod("27"); //27-渠道垫付
    		insbOrderDao.updateById(insbOrder);
    		
            INSBOrderpayment order = new INSBOrderpayment();
            order.setOperator("渠道");
            order.setCreatetime(new Date());
            order.setModifytime(new Date());
            order.setNoti("03");
            order.setTaskid(taskid);
            order.setPaychannelid("27"); //27-渠道垫付,原来是5
            order.setAmount(insbPolicyitem.getTotalepremium());
            order.setPaytime(new Date());
            order.setPayresult("02");
            order.setCurrencycode("RMB");
            order.setPayflowno(channelId);
            order.setPaymentransaction(taskid + "-" + prvid);
            order.setTradeno(channelId);
            order.setSubinstanceid(subId);
            insbOrderpaymentDao.insert(order);
        } else if ("FirstPayLastInsure".equals(quotetotalinfo.getSourceFrom())) {
        	String diffAmount = quoteBean.getDiffAmount();
        	if (StringUtil.isNotEmpty(diffAmount)) {
            	INSBOrderpayment order = new INSBOrderpayment();
                order.setOperator("渠道");
                order.setCreatetime(new Date());
                order.setModifytime(new Date());
                order.setNoti("03");
                order.setTaskid(taskid);
                order.setPaychannelid("26"); //26-保网垫付
                order.setAmount(Double.parseDouble(diffAmount));
                order.setPaytime(new Date());
                order.setPayresult("02");
                order.setCurrencycode("RMB");
                order.setPayflowno(channelId);
                order.setPaymentransaction(taskid + "-" + prvid);
                order.setTradeno(channelId);
                order.setSubinstanceid(subId);
                insbOrderpaymentDao.insert(order);
        	}
        }
        result.setRespCode("00");
        result.setErrorMsg("推支付成功");

        /**2016-11-02 16:05:39 chenjianglong add 如果是在非工作时间推送的支付成功 begin**/
        if(!quoteService.isWorkTime(taskid, prvid)){
            result.setMsgType("00");
            result.setErrorMsg("您好，现在是非工作时间，若继续支付，订单将在上班后第一时间为您确认，感谢您的支持！");
        }
        /**2016-11-02 16:05:39 chenjianglong add 如果是在非工作时间推送的支付成功 end**/

        return result;
    }
    
    //新流程渠道垫付推核保接口
    @Override
    public QuoteBean channelPayPolicy(QuoteBean quoteBean) throws Exception {
        QuoteBean result = new QuoteBean();
        
        if (null == quoteBean || StringUtils.isEmpty(quoteBean.getTaskId()) || 
        		StringUtils.isEmpty(quoteBean.getPrvId()) || StringUtils.isEmpty(quoteBean.getChannelId())) {
            result.setRespCode("01");
            result.setErrorMsg("任务号，渠道id和供应商编码不能为空");
            return result;
        }
        if (null == quoteBean.getInsureInfo() || StringUtils.isEmpty(quoteBean.getInsureInfo().getTotalPremium())) {
            result.setRespCode("01");
            result.setErrorMsg("支付金额不能为空");
            return result;
        }
        String taskid = quoteBean.getTaskId();
        String prvid = quoteBean.getPrvId();
        String channelId = quoteBean.getChannelId();

        Map<String ,Object> qmap = new HashMap<>();
        qmap.put("taskid", taskid);
        qmap.put("purchaserchannel", channelId);
        INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.select(qmap);
        if (null == quotetotalinfo) {
            result.setRespCode("01");
            result.setErrorMsg("任务号" + taskid + "或渠道id" + channelId + "不存在");
            return result;
        }
        
        if ( !quoteService.hasInterfacePower("18", quotetotalinfo.getInscitycode(), channelId) ) { //18-payByChannel新流程备用金支付
           	result.setRespCode("01");
           	result.setErrorMsg("没有调用该接口的权限");
            return result;
        }

        Map<String, String> insbOrderMap = new HashMap<String, String>();
        insbOrderMap.put("taskid", taskid);
        insbOrderMap.put("companyid", prvid);
        INSBQuoteinfo quoteinfo = insbQuoteinfoDao.getByTaskidAndCompanyid(insbOrderMap);
        if (quoteinfo == null) {
            result.setRespCode("01");
            result.setErrorMsg("任务号" + taskid + "和供应商编码" + prvid + "不存在");
            return result;
        }
        
        double amount = Double.parseDouble(quoteBean.getInsureInfo().getTotalPremium());
        INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
        insbPolicyitem.setTaskid(taskid);
        insbPolicyitem.setInscomcode(prvid);
        insbPolicyitem = insbPolicyitemDao.selectList(insbPolicyitem).get(0);
        if (amount != insbPolicyitem.getTotalepremium()) {
            result.setRespCode("01");
            result.setErrorMsg("任务号" + taskid + "和供应商编码" + prvid + "的支付金额不正确，输入金额" + amount + "实际金额" + insbPolicyitem.getTotalepremium());
            return result;
        }
        
        String subId = quoteinfo.getWorkflowinstanceid(); //quoteinfo.getWorkflowinstanceid() 默认子工作流已经存在
        INSBWorkflowsub sub = new INSBWorkflowsub();
        sub.setInstanceid(subId);
        INSBWorkflowsub flow = insbWorkflowsubDao.selectOne(sub);
        if (null == flow || !"14".equals(flow.getTaskcode().trim())) {
            result.setRespCode("01");
            result.setErrorMsg("任务号" + taskid + "和供应商编码" + prvid + "无效的支付状态");
            return result;
        }

         /* 记录chn推cm的时间点开始*/
        try {
            Map<String, String> phaseTime = new HashMap<>();
            phaseTime.put("channelId", quoteBean.getChannelId()==null?"":quoteBean.getChannelId());
            phaseTime.put("taskId", taskid);
            phaseTime.put("prvId", prvid==null?"":prvid);
            phaseTime.put("taskState", quoteBean.getTaskState()==null?"":quoteBean.getTaskState());
            phaseTime.put("phaseType", "submitPayChnCallCM");
            phaseTime.put("phaseSeq", "7");
            chnService.recordPhaseTime(phaseTime);
        }catch (Exception e){
            LogUtil.info("记录chn推cm支付的时间点异常:"+taskid+e.getMessage());
        }
          /* 记录chn推cm的时间点结束*/
        
        //boolean needUploadImg = chnService.getImageInfos(quoteBean, "14");
        //result.setMsgType(quoteBean.getMsgType());
        //result.setImageInfos(quoteBean.getImageInfos());

        String agentnum = insbPolicyitem.getAgentnum();
        /* 记录chn推cm的时间点开始*/
        try {
            Map<String, String> phaseTime = new HashMap<>();
            phaseTime.put("channelId", quoteBean.getChannelId()==null?"":quoteBean.getChannelId());
            phaseTime.put("taskId", taskid);
            phaseTime.put("prvId", prvid==null?"":prvid);
            phaseTime.put("taskState", quoteBean.getTaskState()==null?"":quoteBean.getTaskState());
            phaseTime.put("phaseType", "submitInsureChnCallCM");
            phaseTime.put("phaseSeq", "9");
            chnService.recordPhaseTime(phaseTime);
        }catch (Exception e){
            LogUtil.info("记录chn推cm支付的时间点异常:"+taskid+e.getMessage());
        }
                       /* 记录chn推cm的时间点结束*/
		String message = appMyOrderInfoService.policySubmit(taskid, prvid, agentnum, 0, 0);
		LogUtil.info("channelPayPolicy提交核保返回[" + taskid + "," + prvid + "]:" + message);
		if (!"success".equals(message)) {
			result.setRespCode("01");
            result.setErrorMsg("提交核保失败：" + message);
            return result;
		}
		
		INSBOrder insbOrder = new INSBOrder();
		insbOrder.setTaskid(taskid);
		insbOrder.setPrvid(prvid);
		insbOrder = insbOrderDao.selectOne(insbOrder);
		insbOrder.setPaymentmethod("27"); //27-渠道垫付
		insbOrderDao.updateById(insbOrder);
        
        INSBOrderpayment order = new INSBOrderpayment();
        order.setOperator("渠道");
        order.setCreatetime(new Date());
        order.setModifytime(new Date());
        order.setNoti("03");
        order.setTaskid(taskid);
        order.setPaychannelid("27"); //27-渠道垫付,原来是5
        order.setAmount(insbPolicyitem.getTotalepremium());
        order.setPaytime(new Date());
        order.setPayresult("02");
        order.setCurrencycode("RMB");
        order.setPayflowno(channelId);
        order.setPaymentransaction(taskid + "-" + prvid);
        order.setTradeno(channelId);
        order.setSubinstanceid(subId);
        insbOrderpaymentDao.insert(order);
        
        result.setRespCode("00");
        result.setErrorMsg("成功");
         /* 记录chn推cm的时间点开始*/
        try {
            Map<String, String> phaseTime = new HashMap<>();
            phaseTime.put("channelId", quoteBean.getChannelId()==null?"":quoteBean.getChannelId());
            phaseTime.put("taskId", taskid);
            phaseTime.put("prvId", prvid==null?"":prvid);
            phaseTime.put("taskState", quoteBean.getTaskState()==null?"":quoteBean.getTaskState());
            phaseTime.put("phaseType", "payReturnCMCallChn");
            phaseTime.put("phaseSeq", "8");
            chnService.recordPhaseTime(phaseTime);
        }catch (Exception e){
            LogUtil.info("记录payReturnCMCallChn时间点异常:"+taskid+e.getMessage());
        }
        /* 记录chn推cm的时间点结束*/

        /**2016-11-02 16:05:39 chenjianglong add 如果是在非工作时间推送的支付成功 begin**/
        if(!quoteService.isWorkTime(taskid, prvid)){
            result.setMsgType("00");
            result.setErrorMsg("您好，现在是非工作时间，若继续支付，订单将在上班后第一时间为您确认，感谢您的支持！");
        }
        /**2016-11-02 16:05:39 chenjianglong add 如果是在非工作时间推送的支付成功 end**/

        return result;
    }

    @Override
    public PayInfo pay(PayInfo payInfo, QuoteBean quoteBean) {
        PayInfo result = new PayInfo();
        result.setCode("0000");
        INSBOrder insbOrder = new INSBOrder();
        insbOrder.setTaskid(quoteBean.getTaskId());
        insbOrder.setPrvid(quoteBean.getPrvId());
        insbOrder = insbOrderDao.selectOne(insbOrder);
        result.setRetUrl(quoteBean.getRetUrl());
        if ("2".equals(insbOrder.getOrderstatus())) { // 已经支付
            result.setMsg("已支付");
            result.setOrderState("02");
            INSBOrderpayment orderpayment = new INSBOrderpayment();
            orderpayment.setTaskid(quoteBean.getTaskId());
            orderpayment.setOrderid(insbOrder.getId());
            orderpayment = insbOrderpaymentDao.selectOne(orderpayment);
            result.setBizTransactionId(orderpayment.getPayflowno());

            return result;

        }
        String cmChannelId = PayConfigMappingMgr.getCmCodeByPayCode(MappingType.PAY_CHANNEL, payInfo.getChannelId(), payInfo.getPayType());
        insbOrder.setPaymentstatus("01");
        insbOrder.setModifytime(new Date());
        insbOrder.setPaymentmethod(cmChannelId);
        insbOrderDao.updateById(insbOrder);
        Map<String, String> map = new HashMap<String, String>();
        map.put("taskid", quoteBean.getTaskId());
        map.put("companyid", quoteBean.getPrvId());
        INSBQuoteinfo insbQuoteinfo = insbQuoteinfoDao.getByTaskidAndCompanyid(map);

        int totalAmount = AmountUtil.trans2Fen(insbOrder.getTotalpaymentamount());
        // 查找已经支付的金额
        INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
        insbOrderpayment.setTaskid(quoteBean.getTaskId());
        insbOrderpayment.setPayresult("02");
        List<INSBOrderpayment> insbOrderpayments = insbOrderpaymentDao.selectList(insbOrderpayment);
        Double paiedAmount = 0.0D;
        String bizId = insbOrder.getOrderno();
        if(insbOrderpayments.size() > 0){
        	for(INSBOrderpayment orderpayment : insbOrderpayments){
        		paiedAmount += orderpayment.getAmount();
        		if(bizId.compareTo(orderpayment.getPaymentransaction()) < 0){
        			bizId = orderpayment.getPaymentransaction();
        		}
        	}
        }
        
        bizId = bizId.split(split)[0] + split + (Long.valueOf(bizId.split(split)[1]) + 1L);
        
        int amount = AmountUtil.trans2Fen(paiedAmount);
        // TODO 根据规则计算待付金额
        int waitAmout = totalAmount - amount;
        if(waitAmout < 0){
        	 LogUtil.info("待支付金额必须大于零 bizId=" + bizId);
        	 result.setMsg("待支付金额必须大于零");
        	 return result;
        }
        result.setAmount(waitAmout);
        
        insbOrderpayment = new INSBOrderpayment();
        insbOrderpayment.setTaskid(quoteBean.getTaskId());
        insbOrderpayment.setCreatetime(new Date());
        insbOrderpayment.setModifytime(new Date());
        insbOrderpayment.setOperator("渠道");
        insbOrderpayment.setOrderid(insbOrder.getId());
        insbOrderpayment.setPaychannelid(cmChannelId);
        insbOrderpayment.setAmount(insbOrder.getTotalpaymentamount());
        insbOrderpayment.setCurrencycode("RMB");
        insbOrderpayment.setPayresult("01");
        insbOrderpayment.setSubinstanceid(insbQuoteinfo.getWorkflowinstanceid());
        insbOrderpayment.setPaymentransaction(bizId);
        insbOrderpaymentDao.insert(insbOrderpayment);
        payInfo.setBizId(bizId);

        LogUtil.info("进入pay2方法  bizId=" + bizId + ",param=" + JsonUtils.serialize(payInfo));
        
        payInfo.setAmount(waitAmout);
        String taskId = quoteBean.getTaskId();
        INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
        insbQuotetotalinfo.setTaskid(taskId);
        insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
        
        map = insbChannelagreementService.getDeptIdByChannelinnercodeAndPrvcode(quoteBean.getChannelId(), insbQuotetotalinfo.getInscitycode());

        INSBAgent insbAgent = new INSBAgent();
        insbAgent.setJobnum(map.get("jobnum"));
        insbAgent = insbAgentDao.selectOne(insbAgent);
        
        INSCDept inscDept = inscDeptDao.selectById(insbAgent.getDeptid());
        String agentOrg = inscDept.getParentcodes().split("\\+")[3];
        
        payInfo.setAgentOrg(agentOrg);
        payInfo.setNotifyUrl("http://" + ValidateUtil.getConfigValue("localhost.ip") + ":" + 
        		ValidateUtil.getConfigValue("localhost.port") + "/" 
        		+ ValidateUtil.getConfigValue("localhost.projectName") + "/payService/callback");
        payInfo.setNotifyType("POST");
        
        INSBChannel insbChannel = new INSBChannel();
        insbChannel.setChannelinnercode(quoteBean.getChannelId());
        insbChannel.setDeptid(payInfo.getAgentOrg());
        insbChannel = insbChannelDao.selectList(insbChannel).get(0);
        payInfo.setPaySource("zzb.system." + (StringUtils.isBlank(insbChannel.getShortname())?
                insbChannel.getChannelname():insbChannel.getShortname()));

        payInfo.setInsOrg(insbOrder.getPrvid());
        
        // 针对不同的支付方式完善数据
        switch (payInfo.getChannelId()) {
            case "payeco":
                break;
            case "yeepay":
                result = yeepay(payInfo);
                break;
            case "alipay":
                result = alipay(payInfo);
                break;
            case "bestpay":
                result = bestpay(payInfo,insbOrderpayment);
                break;
            case "tenpay":
                result = tenpay(payInfo);
                break;
            case "99bill":
                result = _99bill(payInfo);
                break;
            case "testPay":
                result = testPay(payInfo);
                break;
            case "huaan":
                result = huaan(payInfo);
                break;
            case "axatp":
                result = axatp(payInfo);
                break;
            case "alltrust":
                result = alltrust(payInfo);
                break;
            case "baowangPay":
                result= baowangPay(payInfo);
                break;
            case "CHNPay":
                result= CHNPay(payInfo);
                break;
            case "baofu" :
                result = baofu(payInfo, insbOrder);
                break;
            default:

        }

        if("baowangPay".equals(payInfo.getChannelId()) || "CHNPay".equals(payInfo.getChannelId())){
            result.setCode("0000");
            result.setPayResult("02");
            result.setOrderState("02");
            result.setAmount(payInfo.getAmount());
            result.setBizId(payInfo.getBizId());
            return  result;
        }
        String jsonStr = JSONObject.fromObject(payInfo).toString();

        LogUtil.info("支付请求参数：" + jsonStr);
        jsonStr = HttpClient.sendPost(payInfo.getBizId(), PayConfigMappingMgr.getPayUrl(), jsonStr);

        LogUtil.info("支付返回结果：" + jsonStr);
        result = JsonUtils.deserialize(jsonStr, PayInfo.class);
        if(!StringUtils.isEmpty(result.getBizTransactionId())){
        	insbOrderpayment.setPayflowno(result.getBizTransactionId());
        	insbOrderpaymentDao.updateById(insbOrderpayment);
            payInfo.setTaskId(quoteBean.getTaskId());
            payInfo.setBizTransactionId(result.getBizTransactionId());
            //将银行卡信息保存到insbbankaccountinfo表
            saveBankInfo(payInfo);
        }
        return result;
    }  
    
	public String callback(PayInfo payInfo) {
		String payInfoJson = JsonUtils.serialize(payInfo);
		LogUtil.info("收到支付回调:" + payInfoJson);
		threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
				INSBOrder insbOrder = new INSBOrder();
				String taskId = payInfo.getBizId().split(split)[0];
				insbOrder.setTaskid(taskId);
				insbOrder.setOrderstatus("1");
                if (!StringUtils.isBlank(payInfo.getInsOrg())) {
                    insbOrder.setPrvid(payInfo.getInsOrg());
                }
				insbOrder = insbOrderDao.selectOne(insbOrder);
				if (insbOrder == null) { 
					LogUtil.info("订单都未找到:orderNo=" + payInfo.getBizId());
					return;
				}
				if("2".equals(insbOrder.getOrderstatus())){
					LogUtil.info("订单已支付:orderNo=" + payInfo.getBizId());
					return;
				}

				Double amount = 0.0;
				if (payInfo.getAmount() != null){
					amount = payInfo.getAmount() / 100.0;
				}else{
					LogUtil.info("支付回调的结果中支付金额为空:orderNo=" + payInfo.getBizId());
				}

                Date date = new Date();
                if (!StringUtils.isEmpty(payInfo.getTransDate()) && payInfo.getTransDate().contains("-") ){
                    LogUtil.info("渠道支付返回，订单：" + payInfo.getBizId() + "支付时间为：" + payInfo.getTransDate());
                    if(payInfo.getTransDate().trim().length() > 10 ){
                        date = ModelUtil.conbertStringToDate(payInfo.getTransDate());
                    }else
                        date = ModelUtil.conbertStringToNyrDate(payInfo.getTransDate());
                }else{
                    LogUtil.error("支付回调的结果中支付时间为:paydate=" + payInfo.getTransDate() + "不符合规则！");
                }

				INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
				insbOrderpayment.setPayflowno(payInfo.getBizTransactionId());
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
					insbOrderpayment.setPaymentransaction(payInfo.getBizId());
					insbOrderpayment.setOrderid(insbOrder.getId()); // 订单表id
					insbOrderpayment.setPaychannelid(PayConfigMappingMgr.getCmCodeByPayCode(MappingType.PAY_CHANNEL, payInfo.getChannelId())); // 支付渠道
					insbOrderpayment.setNoti(PayConfigMappingMgr.getCmCodeByPayCode(MappingType.PAY_CHANNEL, payInfo.getPayType())); // 支付方式
					insbOrderpayment.setAmount(amount);
					insbOrderpayment.setCurrencycode("RMB"); // 币种编码
					insbOrderpayment.setPayresult(payInfo.getOrderState());
					insbOrderpayment.setCreatetime(new Date()); // 创建时间
					insbOrderpayment.setModifytime(new Date());
					insbOrderpayment.setPayflowno(payInfo.getBizTransactionId());
					insbOrderpayment.setPaytime(date);
					insbOrderpayment.setTradeno(payInfo.getPaySerialNo());
					insbOrderpayment.setCreatetime(new Date());
					insbOrderpayment.setModifytime(new Date());
					insbOrderpaymentDao.insert(insbOrderpayment);
				} else {
					if(amount > 0.0)
						insbOrderpayment.setAmount(amount);
					insbOrderpayment.setPayresult(payInfo.getOrderState());
					insbOrderpayment.setPaytime(date);
					insbOrderpayment.setTradeno(payInfo.getPaySerialNo());
					insbOrderpayment.setModifytime(new Date());
					insbOrderpaymentDao.updateById(insbOrderpayment);
				}
		
				if ("02".equals(payInfo.getOrderState())) { // 支付成功，更新insborder然后推工作流
					INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();
					insbWorkflowsub.setMaininstanceid(taskId);
					insbWorkflowsub.setInstanceid(insbOrderpayment.getSubinstanceid());
					insbWorkflowsub.setTaskcode("14");
					insbWorkflowsub = insbWorkflowsubDao.selectOne(insbWorkflowsub);
					
					if(insbWorkflowsub != null){ // 如果是报价后支付需要推提交核保工作流
						payInfo.setBizId(payInfo.getBizId() + refundSplit + "1");  // 报价支付结果
						Map<String, String> param = new HashMap<String, String>();
			            param.put("taskid", taskId);
			            param.put("inscomcode", insbOrder.getPrvid());
			            List<INSBPolicyitem> policyitemList = insbPolicyitemDao.getListByMap(param);
			            String agentnum = policyitemList.get(0).getAgentnum();
                        /* 记录chn推cm的时间点开始*/
                        try {
                            Map<String, String> phaseTime = new HashMap<>();
                            phaseTime.put("channelId", payInfo.getChannelId()==null?"":payInfo.getChannelId());
                            phaseTime.put("taskId", taskId);
                            phaseTime.put("prvId", insbOrder.getPrvid()==null?"":insbOrder.getPrvid());
                            phaseTime.put("taskState", "14");
                            phaseTime.put("phaseType", "submitInsureChnCallCM");
                            phaseTime.put("phaseSeq", "9");
                            chnService.recordPhaseTime(phaseTime);
                        }catch (Exception e){
                            LogUtil.info("记录chn推cm支付的时间点异常:"+taskId+e.getMessage());
                        }
                       /* 记录chn推cm的时间点结束*/
						String message = appMyOrderInfoService.policySubmit(taskId, insbOrder.getPrvid(), agentnum, 0, 0);
						LogUtil.info("提交核保返回[" + taskId + "]:" + message);
						
					}else{
						payInfo.setBizId(payInfo.getBizId() + refundSplit + "2");  // 核保支付结果
						int totalAmount = AmountUtil.trans2Fen(insbOrder.getTotalpaymentamount());
				        // 查找已经支付的金额
				        insbOrderpayment = new INSBOrderpayment();
				        insbOrderpayment.setTaskid(taskId);
				        insbOrderpayment.setPayresult("02");
				        List<INSBOrderpayment> insbOrderpayments = insbOrderpaymentDao.selectList(insbOrderpayment);
				        Double paiedAmount = 0.0D;
				        String bizId = insbOrder.getOrderno();
				        if(insbOrderpayments.size() > 0){
				        	for(INSBOrderpayment orderpayment : insbOrderpayments){
				        		paiedAmount += orderpayment.getAmount();
				        		if(bizId.compareTo(orderpayment.getPaymentransaction()) < 0){
				        			bizId = orderpayment.getPaymentransaction();
				        		}
				        	}
				        }
				        int intAmount = AmountUtil.trans2Fen(paiedAmount);
				        // TODO 根据规则计算待付金额
				        int waitAmout = totalAmount - intAmount;
						if(waitAmout == 0){ // 已全部付清
							insbOrder.setOrderstatus("2"); // 待承保打单
							insbOrder.setPaymentstatus("02");
							completeTask(insbOrder, insbOrderpayments.get(0));
							
						}else{
							insbOrder.setOrderstatus("1"); // 待承保打单
							insbOrder.setPaymentstatus("01"); // 支付中
						}
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
					}
					String message = HttpClient.sendPost(payInfo.getBizId(), PayConfigMappingMgr.getPayNoticeUrl(), JsonUtils.serialize(payInfo));
					LogUtil.info("通知支付返回[" + taskId + "]:" + message);
				}
            }});
		return "0000";
	}

    private void saveBankInfo(PayInfo payInfo) {
        CHNBankaccountinfo account = new CHNBankaccountinfo();
        if(payInfo.getChannelParam() != null && payInfo.getChannelParam().get("bankCardNo") != null){
            String bankCardNo = payInfo.getChannelParam().get("bankCardNo"); // 账号（银行卡号）
            String bankCardAddress = payInfo.getChannelParam().get("bankCardAddress");  //开户银行所属省市
            String idCardName = payInfo.getChannelParam().get("idCardName");  //账户名（持卡人姓名）
            String bankCardName = payInfo.getChannelParam().get("bankName"); //发卡银行
            String bankCode = payInfo.getChannelParam().get("bankCode");
            account.setAccountname(idCardName);
            account.setAccountno(bankCardNo);
            account.setBankname(bankCardName);
            account.setProvince(bankCardAddress);
            account.setBankcode(bankCode);
            account.setTaskid(payInfo.getTaskId());
            account.setPayflowno(payInfo.getBizTransactionId());
            account.setCreatetime(new Date());
            account.setModifytime(new Date());
            chnBankaccountinfoDao.saveOrUpdate(account);
        }else{
            LogUtil.info("任务号为：" + payInfo.getTaskId()+ "没有包含银行卡信息");
        }
    }

    private PayInfo yeepay(PayInfo payInfo) {
        return payInfo;
    }

    private PayInfo alipay(PayInfo payInfo) {
        return payInfo;
    }

    private PayInfo bestpay(PayInfo payInfo, INSBOrderpayment insbOrderpayment) {
    	if("mobile".equals(payInfo.getPayType())){
	    	INSBOrderpayment payment = new INSBOrderpayment();
	    	payment.setTaskid(insbOrderpayment.getTaskid());
	        List<INSBOrderpayment> insbOrderpayments = insbOrderpaymentDao.selectList(payment);
	        String bizId = payInfo.getBizId();
	        if(insbOrderpayments.size() > 0){
	        	bizId = insbOrderpayments.get(0).getPaymentransaction();
	        	for(INSBOrderpayment orderpayment : insbOrderpayments){
	        		if(bizId.compareTo(orderpayment.getPaymentransaction()) < 0){
	        			bizId = orderpayment.getPaymentransaction();
	        		}
	        	}
	        	bizId = bizId.split(split)[0] + split + (Long.valueOf(bizId.split(split)[1]) + 1L);
	        }
	        insbOrderpayment.setPaymentransaction(bizId);
	        payInfo.setBizId(bizId);
	        insbOrderpaymentDao.updateById(insbOrderpayment);
        }
        return payInfo;
    }

    private PayInfo tenpay(PayInfo payInfo) {
        return payInfo;
    }

    private PayInfo _99bill(PayInfo payInfo) {
        return payInfo;
    }

    private PayInfo testPay(PayInfo payInfo) {
        return payInfo;
    }

    private PayInfo huaan(PayInfo payInfo) {
        return payInfo;
    }

    private PayInfo axatp(PayInfo payInfo) {
        return payInfo;
    }

    private PayInfo alltrust(PayInfo payInfo) {
        return payInfo;
    }

    /**
     * 保网垫付
     * */
    private PayInfo baowangPay(PayInfo payInfo){
        //保网垫付，生产支付记录，并推二支
        String taskId = payInfo.getTaskId();
        INSBOrder order = new INSBOrder();
        order.setTaskid(taskId);
        order = insbOrderDao.selectOne(order);
        if(order == null){
            payInfo.setCode("-1");
            payInfo.setMsg("保网垫付异常，根据任务号：" + taskId + " 没有找到对应的订单记录");
            return payInfo;
        }

        Map<String, String> param = new HashMap<String, String>();
        param.put("taskid", taskId);
        param.put("companyid", order.getPrvid());
        INSBQuoteinfo insbQuoteinfo = insbQuoteinfoDao.getByTaskidAndCompanyid(param);

        INSBOrderpayment orderPayment = new INSBOrderpayment();
        BigDecimal amountDecimal = new BigDecimal(payInfo.getAmount());
        Double amount = amountDecimal.divide((new BigDecimal(100)),2,BigDecimal.ROUND_HALF_UP).doubleValue();
        orderPayment.setTaskid(order.getTaskid());
        orderPayment.setAmount(amount);
        orderPayment.setOrderid(order.getId());
        orderPayment.setPaychannelid(payInfo.getChannelId());
        orderPayment.setPaymentransaction(taskId + "-" + order.getPrvid());
        orderPayment.setCurrencycode("RMB");
        orderPayment.setPayresult("02");
        orderPayment.setSubinstanceid(insbQuoteinfo.getWorkflowinstanceid());
        orderPayment.setOperator("支付平台");
        orderPayment.setCreatetime(new Date());
        orderPayment.setModifytime(new Date());
        orderPayment.setPaytime(new Date());
        insbOrderpaymentDao.insert(orderPayment);
        completeTask(order, orderPayment); //推二支
        return payInfo;
    }

    /**
     * 保网垫付
     * */
    private PayInfo  CHNPay(PayInfo payInfo){
        //保网垫付，生产支付记录，并推二支
        String taskId = payInfo.getTaskId();
        INSBOrder order = new INSBOrder();
        order.setTaskid(taskId);
        order = insbOrderDao.selectOne(order);
        if(order == null){
            payInfo.setCode("-1");
            payInfo.setMsg("保网垫付异常，根据任务号：" + taskId + " 没有找到对应的订单记录");
            return payInfo;
        }

        Map<String, String> param = new HashMap<String, String>();
        param.put("taskid", taskId);
        param.put("companyid", order.getPrvid());
        INSBQuoteinfo insbQuoteinfo = insbQuoteinfoDao.getByTaskidAndCompanyid(param);

        INSBOrderpayment orderPayment = new INSBOrderpayment();
        BigDecimal amountDecimal = new BigDecimal(payInfo.getAmount());
        Double amount = amountDecimal.divide((new BigDecimal(100)),2,BigDecimal.ROUND_HALF_UP).doubleValue();
        orderPayment.setTaskid(order.getTaskid());
        orderPayment.setAmount(amount);
        orderPayment.setOrderid(order.getId());
        orderPayment.setPaychannelid(payInfo.getChannelId());
        orderPayment.setPaymentransaction(taskId + "-" + order.getPrvid());
        orderPayment.setCurrencycode("RMB");
        orderPayment.setPayresult("02");
        orderPayment.setSubinstanceid(insbQuoteinfo.getWorkflowinstanceid());
        orderPayment.setOperator("支付平台");
        orderPayment.setCreatetime(new Date());
        orderPayment.setModifytime(new Date());
        orderPayment.setPaytime(new Date());
        insbOrderpaymentDao.insert(orderPayment);
        return payInfo;
    }

    private PayInfo baofu(PayInfo payInfo, INSBOrder insbOrder) {
        INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
        insbPolicyitem.setTaskid(insbOrder.getTaskid());
        insbPolicyitem.setInscomcode(insbOrder.getPrvid());
        List<INSBPolicyitem> list = insbPolicyitemDao.selectList(insbPolicyitem);
        Map<String, String> channelParam = payInfo.getChannelParam();
        if(channelParam == null)
            channelParam = new HashMap<String, String>();
        if(list.size() > 0) {
            channelParam.put("applicantName", list.get(0).getApplicantname());
            channelParam.put("idCardName", list.get(0).getApplicantname());
        }

        payInfo.setChannelParam(channelParam);
        payInfo.setInsOrg(insbOrder.getPrvid());
        payInfo.setPrvId(insbOrder.getPrvid());
        return payInfo;
    }

    public static Map<String, String> toMap(PayInfo payInfo) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("agentOrg", payInfo.getAgentOrg());
        map.put("amount", String.valueOf(payInfo.getAmount()));
        map.put("areaCode", payInfo.getAreaCode());
        map.put("bizId", payInfo.getBizId());
        map.put("bizTransactionId", payInfo.getBizTransactionId());
        map.put("channelId", payInfo.getChannelId());
        map.put("channelName", payInfo.getChannelName());
        map.put("code", payInfo.getCode());
        map.put("insOrg", payInfo.getInsOrg());
        map.put("insSerialNo", payInfo.getInsSerialNo());
        map.put("ipAddress", payInfo.getIpAddress());
        map.put("msg", payInfo.getMsg());
        map.put("nonceStr", payInfo.getNonceStr());
        map.put("notifyType", payInfo.getNotifyType());
        map.put("notifyUrl", payInfo.getNotifyUrl());
//		map.put("orderNo", payInfo.getOrderNo());
        map.put("orderState", payInfo.getOrderState());
        map.put("payResult", payInfo.getPayResult());
        map.put("payResultDesc", payInfo.getPayResultDesc());
        map.put("paySerialNo", payInfo.getPaySerialNo());
        map.put("paySource", payInfo.getPaySource());
        map.put("payType", payInfo.getPayType());
        map.put("payTypeDesc", payInfo.getPayTypeDesc());
        map.put("payUrl", payInfo.getPayUrl());
        map.put("reference", payInfo.getReference());
        map.put("transDate", payInfo.getTransDate());
        map.put("validTime", payInfo.getValidTime());
        map.put("retUrl", payInfo.getRetUrl());
        map.put("description", payInfo.getDescription());
        return map;
    }

    public static String verify(Map<String, String> map, String key) {
        String signStr = "";
        List<String> keys = new ArrayList<String>(map.keySet());
        Collections.sort(keys);
        for (String k : keys) {
            if (StringUtils.isNotEmpty(map.get(k))) {
                try {
                    signStr += "&" + k + "=" + URLEncoder.encode(map.get(k), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        signStr = signStr.substring(1) + "&key=" + key;
        return StringUtil.md5Hex(signStr).toUpperCase();
    }

    @Override
    public PayInfo queryResult(String bizTransactionId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("bizTransactionId", bizTransactionId);
        params.put("bizId", bizTransactionId);
        String jsonStr = HttpClient.doPayGet(PayConfigMappingMgr.getQueryResultUrl(), params);
        LogUtil.info("支付查询返回结果：" + jsonStr);
        PayInfo result = JsonUtils.deserialize(jsonStr, PayInfo.class);
        callback(result);
        return result;
    }
    
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
			
			//hxx  获取数据 网点数据没有获取平台数据
			INSBPaychannelmanager insbPaychannelmanager = new INSBPaychannelmanager();
			insbPaychannelmanager.setProviderid(insbQuoteinfo.getInscomcode());
			insbPaychannelmanager.setDeptid(insbQuoteinfo.getDeptcode());
			insbPaychannelmanager.setPaychannelid(insbOrderpayment.getPaychannelid());
			List<INSBPaychannelmanager> pcmList = insbPaychannelmanagerDao.selectList(insbPaychannelmanager);
			if(pcmList.isEmpty()){
				String ptdeptcode = inscDeptService.getPingTaiDeptId(insbQuoteinfo.getDeptcode());
				insbPaychannelmanager.setDeptid(ptdeptcode);
				pcmList = insbPaychannelmanagerDao.selectList(insbPaychannelmanager);
			}
			
			String subTaskId = insbQuoteinfo.getWorkflowinstanceid();
			String agentId = flow.getOperator();
			if (StringUtils.isEmpty(agentId))
				agentId = "admin";
			Map<String, Object> map1 = new HashMap<String, Object>();
			Map<String, Object> map = new HashMap<String, Object>();

			if (pcmList.size() != 0 && "1".equals(pcmList.get(0).getCollectiontype())) {
				map1.put("issecond", "0");// 1需要二次支付，0不需要二次支付
				map1.put("acceptway", JSONObject.fromObject(workflowmainService.getContractcbType(insbOrder.getTaskid(), insbOrder.getPrvid(), "0", "contract")).getString("quotecode"));
			} else {
				map1.put("issecond", "1");// 1需要二次支付，0不需要二次支付
			}
			map.put("data", map1);
			map.put("userid", "admin");
			map.put("processinstanceid", Long.parseLong(subTaskId));
			map.put("taskName", "支付");
			JSONObject jsonObject = JSONObject.fromObject(map);
			Map<String, String> params = new HashMap<String, String>();
			params.put("datas", jsonObject.toString());

            WorkflowFeedbackUtil.setWorkflowFeedback(null, subTaskId, "20", "Completed", "支付", "02".equals(insbOrder.getPaymentstatus())?"支付成功":"支付完成", agentId);

			LogUtil.info("推支付工作流,订单号:" + orderNo + " params:" + jsonObject.toString());
			String retStr = HttpClientUtil.doGet(url, params);
			LogUtil.info("推支付工作流结果,订单号:" + orderNo + " retStr:" + retStr);

			/*if (StringUtils.isEmpty(retStr)) {
				model.setStatus("fail");
				return model;
			}
			JSONObject obj = JSONObject.fromObject(retStr);
			if (obj.get("message").equals("success")) {*/
				model.setStatus("success");
			/*} else {
				model.setStatus("fail");
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			model.setStatus("fail");
			LogUtil.info("推支付工作流失败,订单号:" + orderNo + " 错误信息:" + e.getMessage());
		}
		return model;
	}

    /**
     * 只记录申请退款的操作，不做实际退款操作（退款操作线下执行）
     * */
    public String refund(QuoteBean quoteBean){
        String taskId = quoteBean.getTaskId();
        String prvId = quoteBean.getPrvId();
        INSBOrder insbOrder = new INSBOrder();
        insbOrder.setTaskid(taskId);
        insbOrder.setPrvid(prvId); 
        insbOrder = insbOrderDao.selectOne(insbOrder);
        if (insbOrder == null) {
            LogUtil.info("退款失败，任务号："+ taskId + " ,错误信息：根据任务号没有找到订单信息");
            return "-1";
        }
//        Integer orderAmount = (int)(insbOrder.getTotalpaymentamount() * 100); // 订单总金额——实际需支付的总金额
        int orderAmount = new BigDecimal(100).multiply(new BigDecimal(insbOrder.getTotalpaymentamount())).setScale(0,BigDecimal.ROUND_HALF_UP).toBigInteger().intValue();
        LogUtil.info("订单金额以分为单位并且四舍五入：" + orderAmount);
        Map<String, String> param = new HashMap<String, String>();
        param.put("taskid", taskId);
        param.put("companyid", prvId);
        INSBQuoteinfo insbQuoteinfo = insbQuoteinfoDao.getByTaskidAndCompanyid(param);

        INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
        insbOrderpayment.setTaskid(taskId);
        insbOrderpayment.setSubinstanceid(insbQuoteinfo.getWorkflowinstanceid());;
        List<INSBOrderpayment> insbOrderpayments = insbOrderpaymentDao.selectList(insbOrderpayment);
        Integer paiedAmount = 0; // 已付金额
        List<INSBOrderpayment> paiedOrderpayments = new ArrayList<INSBOrderpayment>(); // 已支付记录

        String refundId = insbOrder.getOrderno().replace(split, refundSplit);
        Boolean isRefunded = false;
        Boolean hasDiffRefunded = false; //差额退款
        Boolean hasFullRefunded = false; //全额退款
        
        if (insbOrderpayments.size() > 0) {
            for (INSBOrderpayment orderpayment : insbOrderpayments) {
                if ("02".equals(orderpayment.getPayresult())){
                    BigDecimal orderpaymentTmp = new BigDecimal(orderpayment.getAmount());
                    Integer orderPaymentInt = orderpaymentTmp.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toBigInteger().intValue();
                    paiedAmount += orderPaymentInt;
                    paiedOrderpayments.add(orderpayment);
//                    insbOrderpayment = orderpayment;
                }else if ("03".equals(orderpayment.getPayresult())) {
                    if ( "01".equals(orderpayment.getRefundtype()) ) {
                    	hasFullRefunded = true;
                    	isRefunded = true;
                    } else if ( "02".equals(orderpayment.getRefundtype()) ) {
                    	hasDiffRefunded = true;
                    }
                }
            }
        }else{
            LogUtil.info("退款失败，任务号："+ taskId + " ,错误信息：根据任务号没有找到已经支付成功的记录");
            return "-1";
        }

        if(!isRefunded){ 
            INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();
            insbWorkflowsub.setMaininstanceid(taskId);
            insbWorkflowsub.setInstanceid(insbQuoteinfo.getWorkflowinstanceid());
            insbWorkflowsub = insbWorkflowsubDao.selectOne(insbWorkflowsub);  //查看核保状态

            INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
            insbQuotetotalinfo.setTaskid(taskId);
            insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
            Map<String, String> map = insbChannelagreementService.getDeptIdByChannelinnercodeAndPrvcode(insbQuotetotalinfo.getPurchaserChannel(), insbQuotetotalinfo.getInscitycode());
            INSBAgent insbAgent = new INSBAgent();
            insbAgent.setJobnum(map.get("jobnum"));
            insbAgent = insbAgentDao.selectOne(insbAgent);
            INSCDept inscDept = inscDeptDao.selectById(insbAgent.getDeptid());
            String agentOrg = inscDept.getParentcodes().split("\\+")[3];
            refundId = refundId.split(refundSplit)[0] + refundSplit + (Long.valueOf(refundId.split(refundSplit)[1]) + 1L);
            // 根据核保状态决定退款的方式是全额退款（核保失败）还是差额退款
            if("37".equals(insbWorkflowsub.getTaskcode()) || "19".equals(insbWorkflowsub.getTaskcode())){//核保失败 全额退款 (01 全额退款， 02 差额退款)
                INSBOrderpayment refund = new INSBOrderpayment();
                refund.setAmount((-paiedAmount) / 100.0);
                refund.setPayorderno(paiedOrderpayments.get(0).getPaymentransaction());
                refund.setPaymentransaction(refundId);
                refund.setRefundtype("01");
                refund.setPaychannelid(paiedOrderpayments.get(0).getPaychannelid());
                refund.setNoti(paiedOrderpayments.get(0).getNoti());
                refund.setTaskid(taskId);
                refund.setPayresult("03");
                refund.setOperator("支付平台");
                refund.setOrderid(insbOrder.getId());
                refund.setCreatetime(new Date());
                refund.setModifytime(new Date());
                refund.setCurrencycode("RMB");
                refund.setPaytime(new Date());
                refund.setMerchantid(agentOrg);
                refund.setSubinstanceid(insbQuoteinfo.getWorkflowinstanceid());
                insbOrderpaymentDao.insert(refund);
             }

             if("20".equals(insbWorkflowsub.getTaskcode())){  //核保成功，差额退款,推二支 (01 全额退款， 02 差额退款)
                 Integer refundAmount = orderAmount - paiedAmount ;// 需退金额 orderAmount 核保金额   paiedAmount 报价金额
                 if( refundAmount >= 50000 || (hasDiffRefunded && !hasFullRefunded) ){
                     //全额退款
                     INSBOrderpayment refund = new INSBOrderpayment();
                     refund.setAmount((-paiedAmount) / 100.0);
                     if (hasDiffRefunded && !hasFullRefunded) {
                    	 refund.setAmount((-orderAmount) / 100.0);
                     }
                     refund.setPayorderno(paiedOrderpayments.get(0).getPaymentransaction());
                     refund.setPaymentransaction(refundId);
                     refund.setRefundtype("01");
                     refund.setPaychannelid(paiedOrderpayments.get(0).getPaychannelid());
                     refund.setNoti(paiedOrderpayments.get(0).getNoti());
                     refund.setTaskid(taskId);
                     refund.setPayresult("03");
                     refund.setOperator("支付平台");
                     refund.setOrderid(insbOrder.getId());
                     refund.setCreatetime(new Date());
                     refund.setModifytime(new Date());
                     refund.setCurrencycode("RMB");
                     refund.setPaytime(new Date());
                     refund.setMerchantid(agentOrg);
                     refund.setSubinstanceid(insbQuoteinfo.getWorkflowinstanceid());
                     insbOrderpaymentDao.insert(refund);
                     
                     LogUtil.info(taskId + "核保成功大于等于阈值调用全额退款关闭开始");
                     String closeFlowResult = insbWorkflowmaintrackService.chnRefundThenCloseflow(taskId, null, "全额退款", "渠道");
                     LogUtil.info(taskId + "核保成功大于等于阈值调用全额退款关闭返回：" + closeFlowResult);
                 }else if(refundAmount > 0 && refundAmount < 50000){
                     //保网垫付
                     LogUtil.info("退款请求无效，任务号为：" + taskId + "的订单无需退款,核保成功保费大于报价成功支付保费的阈值内");
                     return "-1";
                 }else if(refundAmount == 0){
                     //直接推二支
                     //completeTask(insbOrder,paiedOrderpayments.get(0));
                 }else{
                     //差额退款 ，推二支
                     INSBOrderpayment refund = new INSBOrderpayment();
                     refund.setAmount((refundAmount) / 100.0);
                     refund.setPayorderno(paiedOrderpayments.get(0).getPaymentransaction());
                     refund.setPaymentransaction(refundId);
                     refund.setRefundtype("02");
                     refund.setPaychannelid(paiedOrderpayments.get(0).getPaychannelid());
                     refund.setNoti(paiedOrderpayments.get(0).getNoti());
                     refund.setTaskid(taskId);
                     refund.setPayresult("03");
                     refund.setOperator("支付平台");
                     refund.setOrderid(insbOrder.getId());
                     refund.setCreatetime(new Date());
                     refund.setModifytime(new Date());
                     refund.setCurrencycode("RMB");
                     refund.setPaytime(new Date());
                     refund.setMerchantid(agentOrg);
                     refund.setSubinstanceid(insbQuoteinfo.getWorkflowinstanceid());
                     insbOrderpaymentDao.insert(refund);
                     //completeTask(insbOrder,paiedOrderpayments.get(0));
                 }
             }
        }else{
            // 已经退款，无需再次执行
            LogUtil.info("退款请求无效，任务号为："+ taskId +" 的订单已经退款了，不能再次执行退款操作");
            return "-1";
        }
        return "0000";
    }

	public String refundV2(QuoteBean quoteBean) {
		String taskId = quoteBean.getTaskId();
		INSBOrder insbOrder = new INSBOrder();
		insbOrder.setTaskid(taskId);
		insbOrder = insbOrderDao.selectOne(insbOrder);
		if (insbOrder == null) {
			return "-1";
		}
		Map<String, String> param = new HashMap<String, String>();
		param.put("taskid", taskId);
		param.put("companyid", quoteBean.getPrvId());
		INSBQuoteinfo insbQuoteinfo = insbQuoteinfoDao.getByTaskidAndCompanyid(param);
		Integer orderAmount = (int)(insbOrder.getTotalpaymentamount() * 100); // 订单总金额
		INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
		insbOrderpayment.setTaskid(taskId);
		insbOrderpayment.setSubinstanceid(insbQuoteinfo.getWorkflowinstanceid());;
		List<INSBOrderpayment> insbOrderpayments = insbOrderpaymentDao.selectList(insbOrderpayment);
		Integer paiedAmount = 0; // 已付金额
		Integer refundedAmount = 0; // 已退金额
		List<INSBOrderpayment> paiedOrderpayments = new ArrayList<INSBOrderpayment>(); // 已支付记录
		List<INSBOrderpayment> refundedOrderpayments = new ArrayList<INSBOrderpayment>(); // 已退款记录
		List<INSBOrderpayment> refundOrderpayments = new ArrayList<INSBOrderpayment>(); // 需要退款记录

		String refundId = insbOrder.getOrderno().replace(split, refundSplit);
		if (insbOrderpayments.size() > 0) {
			for (INSBOrderpayment orderpayment : insbOrderpayments) {
				if ("02".equals(orderpayment.getPayresult())){
					paiedAmount += (int)(orderpayment.getAmount() * 100);
					paiedOrderpayments.add(orderpayment);
					insbOrderpayment = orderpayment;
				}else if ("03".equals(orderpayment.getPayresult())) {
					refundedAmount += (int)(orderpayment.getAmount() * 100);
					refundedOrderpayments.add(orderpayment);
					if (refundId.compareTo(orderpayment.getPaymentransaction()) < 0) {
						refundId = orderpayment.getPaymentransaction();
					}
				}
			}
		}
		
		INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();
		insbWorkflowsub.setMaininstanceid(taskId);
		insbWorkflowsub.setInstanceid(insbQuoteinfo.getWorkflowinstanceid());
//			insbWorkflowsub.setTaskcode("14");
		insbWorkflowsub = insbWorkflowsubDao.selectOne(insbWorkflowsub);
		Integer refundAmount = paiedAmount + refundedAmount - orderAmount; // 需退金额
		if("37".equals(insbWorkflowsub.getTaskcode()) || ("20".equals(insbWorkflowsub.getTaskcode()) && refundAmount < 0.0D)
                || "19".equals(insbWorkflowsub.getTaskcode()) && refundAmount < 0.0D){ // 核保失败，全额退款
			for (INSBOrderpayment paied : paiedOrderpayments){
				Integer pamount = (int)(paied.getAmount() * 100);
				Integer ramount = 0;
				String bizId = paied.getPaymentransaction();
				for (INSBOrderpayment refunded : refundedOrderpayments){
					if(bizId.equals(refunded.getPayorderno())){
						ramount += (int)(refunded.getAmount() * 100);
					}
				}
				if(pamount + ramount > 0){
					refundId = refundId.split(refundSplit)[0] + refundSplit + (Long.valueOf(refundId.split(refundSplit)[1]) + 1L);
					INSBOrderpayment refund = new INSBOrderpayment();
					refund.setAmount((- pamount - ramount) / 100.0);
					refund.setPayorderno(paied.getPaymentransaction());
					refund.setPaymentransaction(refundId);
					refund.setPaychannelid(paied.getPaychannelid());
					refund.setNoti(paied.getNoti());
					refundOrderpayments.add(refund);
				}
			}
		}else if("20".equals(insbWorkflowsub.getTaskcode()) && refundAmount > 0.0D){ // 核保成功，差额退款
			boolean flag = false; // 是否一次退完
			for (INSBOrderpayment paied : paiedOrderpayments){
				Double pamount = paied.getAmount();
				Double ramount = 0.0D;
				String bizId = paied.getPaymentransaction();
				for (INSBOrderpayment refunded : refundedOrderpayments){
					if(bizId.equals(refunded.getPayorderno())){
						ramount += refunded.getAmount();
					}
				}
				if(pamount + ramount > refundAmount){
					refundId = refundId.split(refundSplit)[0] + refundSplit + (Long.valueOf(refundId.split(refundSplit)[1]) + 1L);
					INSBOrderpayment refund = new INSBOrderpayment();
					refund.setAmount((-refundAmount) / 100.0);
					refund.setPayorderno(paied.getPaymentransaction());
					refund.setPaychannelid(paied.getPaychannelid());
					refund.setNoti(paied.getNoti());
					refund.setPaymentransaction(refundId);
					refundOrderpayments.add(refund);
					flag = true;
					break;
				}
			}
			if(!flag){
				for (INSBOrderpayment paied : paiedOrderpayments){
					Integer pamount = (int)(paied.getAmount() * 100);
					Integer ramount = 0;
					String bizId = paied.getPaymentransaction();
					for (INSBOrderpayment refunded : refundedOrderpayments){
						if(bizId.equals(refunded.getPayorderno())){
							ramount += (int)(refunded.getAmount() * 100);
						}
					}
					if(pamount + ramount > 0){
						refundId = refundId.split(refundSplit)[0] + refundSplit + (Long.valueOf(refundId.split(refundSplit)[1]) + 1L);
						INSBOrderpayment refund = new INSBOrderpayment();
						if(pamount + ramount > refundAmount)
							refund.setAmount((-refundAmount) / 100.0);
						else
							refund.setAmount((-pamount - ramount) / 100.0);
						refund.setPayorderno(paied.getPaymentransaction());
						refund.setPaychannelid(paied.getPaychannelid());
						refund.setNoti(paied.getNoti());
						refund.setPaymentransaction(refundId);
						refundOrderpayments.add(refund);
						refundAmount -= pamount + ramount;
					}
					if(refundAmount <= 0.0)
						break;
				}	
			}
		}
		if(refundOrderpayments.size() > 0){
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
	        insbQuotetotalinfo.setTaskid(taskId);
	        insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			Map<String, String> map = insbChannelagreementService.getDeptIdByChannelinnercodeAndPrvcode(insbQuotetotalinfo.getPurchaserChannel(), insbQuotetotalinfo.getInscitycode());
	        INSBAgent insbAgent = new INSBAgent();
	        insbAgent.setJobnum(map.get("jobnum"));
	        insbAgent = insbAgentDao.selectOne(insbAgent);
	        INSCDept inscDept = inscDeptDao.selectById(insbAgent.getDeptid());
	        String agentOrg = inscDept.getParentcodes().split("\\+")[3];

			for(INSBOrderpayment refund : refundOrderpayments){
				PayInfo payInfo = new PayInfo();
				payInfo.setAgentOrg(agentOrg);
				payInfo.setAmount((int)(refund.getAmount() * -100));
				payInfo.setBizId(refund.getPayorderno());
				payInfo.setRefundId(refund.getPaymentransaction());
				String jsonStr = JsonUtils.serialize(payInfo);
				LogUtil.info("请求退款参数:" + jsonStr);
				jsonStr = HttpClient.sendPost(payInfo.getBizId(), PayConfigMappingMgr.getRefundUrl(), jsonStr);
				LogUtil.info("请求退款返回:" + jsonStr);
				PayInfo res = JsonUtils.deserialize(jsonStr, PayInfo.class);
				
				refund.setTaskid(taskId);
				refund.setPayresult(res.getOrderState());
				refund.setOperator("支付平台");
				refund.setOrderid(insbOrder.getId());
				refund.setCreatetime(new Date());
				refund.setModifytime(new Date());
				refund.setCurrencycode("RMB");
				if(StringUtils.isNotBlank(payInfo.getTransDate())){

						refund.setPaytime(ModelUtil.conbertStringToDate(payInfo.getTransDate()));

				}
				refund.setPayflowno(res.getBizTransactionId());
				refund.setMerchantid(agentOrg);
				refund.setSubinstanceid(insbQuoteinfo.getWorkflowinstanceid());
				insbOrderpaymentDao.insert(refund);
			}
			
		}else{
			LogUtil.info("没钱可退(refund):" + taskId);
		}

		return "0000";
	}


    public static void main(String args[]){
        QuoteBean bean = new QuoteBean();
        bean.setTaskId("");

    }


	
}
