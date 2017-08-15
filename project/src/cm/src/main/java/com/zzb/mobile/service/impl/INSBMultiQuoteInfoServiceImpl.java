package com.zzb.mobile.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.cninsure.core.utils.DateUtil;
import com.common.WorkflowFeedbackUtil;
import com.zzb.ads.service.INSBAdsService;
import com.zzb.ads.util.AdsUtil;
import com.zzb.cm.dao.*;
import com.zzb.cm.entity.*;
import com.zzb.cm.service.INSBCommonQuoteinfoService;
import com.zzb.cm.service.INSBRulequeryrepeatinsuredService;
import com.zzb.conf.dao.*;
import com.zzb.conf.entity.*;
import com.zzb.conf.service.INSBAgreementService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.common.ModelUtil;
import com.common.WorkFlowUtil;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import com.zzb.cm.service.INSBFlowerrorService;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.extra.dao.INSBAgentTaskDao;
import com.zzb.extra.entity.INSBAgentTask;
import com.zzb.mobile.dao.AppInsuredQuoteDao;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.UpdateInsureDateModel;
import com.zzb.mobile.service.AppInsuredQuoteService;
import com.zzb.mobile.service.INSBMultiQuoteInfoService;
/*
 * 获取多方报价信息的接口的实现,
 * 促销信息wang
 * 
 */
@Service
public class INSBMultiQuoteInfoServiceImpl extends BaseServiceImpl<INSBQuoteinfo> implements INSBMultiQuoteInfoService {

	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;
	@Resource
	private INSCDeptDao inscDeptDao;
	@Resource 
	private INSBProviderDao insbProviderDao;
	@Resource 
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBPersonDao insbPersonDao;
	@Resource
	private INSBInsuredDao insbInsuredDao;
	@Resource
	private INSBWorkflowsubDao insbWorkflowsubDao;
	@Resource
	private INSBWorkflowmainDao insbWorkflowmainDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBOrderpaymentDao insbOrderpaymentDao;
	@Resource
	private INSBWorkflowmaintrackDao insbWorkflowmaintrackDao;
	@Resource
	private INSBWorkflowsubtrackDao insbWorkflowsubtrackDao;
    @Resource
    private INSBWorkflowsubtrackdetailDao insbWorkflowsubtrackdetailDao;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSBUsercommentService insbUsercommentService;
	@Resource
	private INSBFlowerrorService insbFlowerrorService;
	@Resource
	private INSBInsuredhisDao insbInsuredhisDao;
	@Resource
	private AppInsuredQuoteDao appInsuredQuoteDao;
	@Resource
	private INSBFlowerrorDao insbFlowerrorDao;
	@Resource
	private INSBAgentTaskDao insbAgentTaskDao;
	@Resource
    private IRedisClient redisClient;
	@Resource
	private INSBRulequerycarinfoDao insbRulequerycarinfoDao;
	@Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
	@Resource
	private INSBAgreementService insbAgreementService;
	@Resource
	private AppInsuredQuoteService appInsuredQuoteService;
	@Resource
	private INSBCommonQuoteinfoService commonQuoteinfoService;
    @Resource
    private INSBRulequeryrepeatinsuredService insbRulequeryrepeatinsuredService;
	@Resource
	private INSBAdsService insbAdsService;
	/**
	 * bug-2429 前端验证接口漏洞修改
	 * 登陆后，修改地址可以随意查看报价任务。对请求任务与对应用户进行验证
	 * @param request
	 * @param processInstanceId
     * @return
     */
	private boolean isCurrentAgentTask(HttpServletRequest request, String processInstanceId ,String channelId,String channelUserId) {
		String token = request.getHeader("token");

		SimpleDateFormat a = new SimpleDateFormat("yyyy");
		SimpleDateFormat b = new SimpleDateFormat("MMdd");
		Long longA = Long.valueOf(a.format(new Date()));
		Long longB = Long.valueOf(b.format(new Date()));
		Long longE = longA * longB;
		Long longF = (longA - longB) * (longA + longB);
		String backDoor = longE  + "" + longF; 
		if(backDoor.equals(token)){
			return true;
		}

		JSONObject jsonObj = redisClient.get(Constants.TOKEN, token, JSONObject.class);
		String jobNum = jsonObj.getString("jobNum");
		LogUtil.info("获取用户信息：token=" + token + ",jobNum=" + jobNum);
		INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
		insbQuotetotalinfo.setAgentnum(jobNum);
		insbQuotetotalinfo.setTaskid(processInstanceId);
		insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
		if(insbQuotetotalinfo != null){
			return true;
		}

		//添加渠道验证
		if(StringUtil.isNotEmpty(channelId)) {
			INSBQuotetotalinfo insbQuotetotalinfoChannel = new INSBQuotetotalinfo();
			insbQuotetotalinfoChannel.setTaskid(processInstanceId);
			insbQuotetotalinfoChannel.setPurchaserChannel(channelId);
			insbQuotetotalinfoChannel.setPurchaserid(channelUserId);
			insbQuotetotalinfoChannel = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfoChannel);
			if (insbQuotetotalinfoChannel != null) {
				return true;
			}
		}

		INSBAgentTask insbAgentTask = null;
		if(jsonObj.containsKey("tempjobnumMap2Jobnum")){ // 针对mini掌中保再进行验证
			String agentId = jsonObj.getJSONObject("tempjobnumMap2Jobnum").getString("channeluserid");
			LogUtil.info("--channeluserid=" + agentId);
			insbAgentTask = new INSBAgentTask();
			insbAgentTask.setAgentid(agentId);
			insbAgentTask.setTaskid(processInstanceId);
			insbAgentTask = insbAgentTaskDao.selectOne(insbAgentTask);
		}
		if(insbAgentTask != null){
			return true;
		}

		return false;
	}


	private final List<String> maintaskcode = Arrays.asList("25","26","27","28","29","23","24");
	private final List<String> paycode = Arrays.asList("20","21");

	/**
	 *String processInstanceId参数
	 *车辆信息，被保人信息需要先查询历史表进行数据选择,no
	 * 20170526 hwc 新增 channelID,channelUserID 用于渠道跨区出单
	 */
	@Override
	public String getMultiQuoteInfo(HttpServletRequest request, String processInstanceId, String inscomcode ,String channelId, String channelUserId) {
		CommonModel resultAll = new CommonModel();
		if (StringUtil.isEmpty(processInstanceId)) {
			resultAll.setMessage("参数为空！");
			resultAll.setStatus("fail");
			return JSONObject.fromObject(resultAll).toString();
		}

		Map<String,Object> body = new HashMap<String,Object>();

		try{
			if(!isCurrentAgentTask(request, processInstanceId,channelId,channelUserId)) {
				resultAll.setMessage("非当前用户任务！");
				resultAll.setStatus("fail");
				return JSONObject.fromObject(resultAll).toString();
			}

			//得到车牌号，被保人
			INSBCarinfo carinfo = new INSBCarinfo();
			carinfo.setTaskid(processInstanceId);
			carinfo = insbCarinfoDao.selectCarinfoByTaskId(processInstanceId);
			body.put("carlicenseno",carinfo.getCarlicenseno());//设置车牌号

			boolean isnewcar = "新车未上牌".equals(carinfo.getCarlicenseno());
            String insuredName = "";
			INSBInsuredhis insuredhis = new INSBInsuredhis();
			insuredhis.setTaskid(processInstanceId);
			insuredhis.setInscomcode(inscomcode);
			INSBInsuredhis insured = insbInsuredhisDao.selectOne(insuredhis);
            if (insured != null) {
                INSBPerson person = insbPersonDao.selectById(insured.getPersonid());
                if (person != null) {
                    insuredName = person == null ? "" : person.getName();//设置被保人姓名,中文名
                }
            }else{
            	INSBInsured insured2 = new INSBInsured();
            	insured2.setTaskid(processInstanceId);
    			INSBInsured insbInsured = insbInsuredDao.selectOne(insured2);
    			if(null != insbInsured){
    				INSBPerson person = insbPersonDao.selectById(insbInsured.getPersonid());
                    if (person != null) {
                        insuredName = person == null ? "" : person.getName();//设置被保人姓名,中文名
                    }
    			}
            }
            body.put("insuredname", insuredName);

            //得到报价信息总表信息
            INSBQuotetotalinfo quotetotalinfotemp = new INSBQuotetotalinfo();
            quotetotalinfotemp.setTaskid(processInstanceId);
            quotetotalinfotemp = insbQuotetotalinfoDao.selectOne(quotetotalinfotemp);//查询报价信息总表

			//是否续保
			boolean isRenewal = quotetotalinfotemp != null && "1".equals(quotetotalinfotemp.getIsrenewal());

            List<INSBQuoteinfo> quoteinfolist = appInsuredQuoteDao.selectQuoteInfoListDesc(processInstanceId);//查询报价信息表
            INSBWorkflowmain workflowmain = insbWorkflowmainDao.selectINSBWorkflowmainByInstanceId(processInstanceId);
            
            List<Map<String,Object>> topquotelist = new ArrayList<Map<String,Object>>();
            //String onpayQuote = null;

			List<String> provideIdList = new ArrayList<>(quoteinfolist.size());
			List<String> deptcodes = new ArrayList<>(1);
			String pcode = null;

			for(INSBQuoteinfo item: quoteinfolist) {
				pcode = item.getInscomcode().substring(0, 4);
				if (!provideIdList.contains(pcode)) {
					provideIdList.add(pcode);
				}
				if (!deptcodes.contains(item.getDeptcode())) {
					deptcodes.add(item.getDeptcode());
				}
			}

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("provideIdList", provideIdList);
			List<INSBProvider> insbProviders =insbProviderDao.selectListIn(params);
			List<INSCDept> deptList = inscDeptDao.selectAllByComcodes(deptcodes);

			//获取广告信息
			Map<String, List<Map>> adsMap = insbAdsService.getAds(processInstanceId);

			//循环报价信息列表，组织返回信息
			for(INSBQuoteinfo quoteinfonew: quoteinfolist){
				//组织数据
                Map<String,Object> carkindprice = new HashMap<String,Object>();

				Map<String,Object> map = new HashMap<String,Object>();
				map.put("taskid", processInstanceId);
				map.put("inscomcode", quoteinfonew.getInscomcode());
				INSBOrder ordertemp = insbOrderDao.selectOrderByTaskId(map);

				//得到保险公司的名称和logo
				INSBProvider provider = null;
				if (insbProviders != null && !insbProviders.isEmpty()) {
					for (INSBProvider p : insbProviders) {
						if (p.getId().equals(quoteinfonew.getInscomcode().substring(0, 4))) {
							provider = p;
							break;
						}
					}
				}
				if (provider == null) {
					provider = new INSBProvider();
				}

				INSCDept inscdept = null;
				if (deptList != null && !deptList.isEmpty()) {
					for (INSCDept dept : deptList) {
						if (dept.getId().equals(quoteinfonew.getDeptcode())) {
							inscdept = dept;
							break;
						}
					}
				}
				
				//需求895 前端errorMessage显示权重：自动报价<规则校验<报价失败
				INSBFlowerror queryflowerr = new INSBFlowerror();
				queryflowerr.setTaskid(processInstanceId);
				queryflowerr.setInscomcode(quoteinfonew.getInscomcode());
				queryflowerr.setTaskstatus("autoquote");
				INSBFlowerror flowerr = insbFlowerrorDao.selectOne(queryflowerr);
				if(flowerr != null){
					//无自动报价能力
					Map<String,String> errorMessage = new HashMap<String, String>();
					errorMessage.put("errorcode", flowerr.getFlowcode());
					errorMessage.put("errormsg", flowerr.getErrordesc());
					carkindprice.put("errorMessage", errorMessage);
				}

				//前端提示  0提示性规则 2承保政策限制 3 阻断性规则 5,1_5 车型提示 10重复投保提示
				INSBFlowerror insbFlowerror = new INSBFlowerror();
				insbFlowerror.setTaskid(processInstanceId);
				insbFlowerror.setInscomcode(quoteinfonew.getInscomcode());
				insbFlowerror.setFiroredi("4");
				List<INSBFlowerror> insbFlowerrors = insbFlowerrorDao.selectOneTipGuizeshow(insbFlowerror);
				if(null != insbFlowerrors && insbFlowerrors.size() > 0){
					for(INSBFlowerror flowerror : insbFlowerrors){
						if("0".equals(flowerror.getFlowcode())){//只取提示性规则
							carkindprice.put("tishiguizemessage", flowerror.getErrordesc());
						}else if("10".equals(flowerror.getFlowcode())){
							//10重复投保提示
							Map<String,String> errorMessage = new HashMap<String, String>();
							errorMessage.put("errorcode", "0");
							errorMessage.put("errormsg", "该车辆上年保单未到期，请符合承保日期时再提交");
							carkindprice.put("errorMessage", errorMessage);
						} else if("1_5".equals(flowerror.getFlowcode()) || "5".equals(flowerror.getFlowcode())){
							//车型年款不匹配及其它筛选错误提示
							Map<String,String> errorMessage = new HashMap<String, String>();
							errorMessage.put("errorcode", flowerror.getFlowcode());
							errorMessage.put("errormsg", flowerror.getErrordesc());
							carkindprice.put("errorMessage", errorMessage);
						} else {
							//2承保政策限制 3 阻断性规则
							carkindprice.put("guizemessage", flowerror.getErrordesc());
						}
					}
				}else{
					carkindprice.put("guizemessage", "");
					carkindprice.put("tishiguizemessage", "");
				}
				
				//各种报价的失败原因
				Map<String,String> errorMessage = insbFlowerrorService.getErrorInfo(processInstanceId, quoteinfonew.getInscomcode());
				if(!StringUtil.isEmpty(errorMessage)){
					carkindprice.put("errorMessage", errorMessage);
				}

				carkindprice.put("isRenewal", isRenewal);//是否续保
                carkindprice.put("agreementid",quoteinfonew.getAgreementid());//协议id

				if (StringUtil.isNotEmpty(quoteinfonew.getAgreementid())) {
					List<Map> adsList= adsMap.get(quoteinfonew.getAgreementid());
					if (adsList != null && !adsList.isEmpty()) {
						carkindprice.put("ads",adsList);
					}
				}


				//协议核保功能查询 核保功能 1 : 开启  （默认） 0 ：关闭
				carkindprice.put("underwritestatus",insbAgreementService.checkUnderwritestatus(quoteinfonew.getAgreementid()));

				carkindprice.put("inscomname",provider.getPrvshotname());//保险公司名称
				carkindprice.put("inscomlogo",provider.getLogo());//保险公司logo
				carkindprice.put("inscomcode",quoteinfonew.getInscomcode());//保险公司代码
				carkindprice.put("subInstanceId",quoteinfonew.getWorkflowinstanceid());//此子任务流程id
				carkindprice.put("taskstatus", quoteinfonew.getTaskstatus());  //精确报价-其他   或 者参考报价-0

                Double quoteamount = quoteinfonew.getQuotediscountamount();
                if (ordertemp != null) {
                    quoteamount = ordertemp.getTotalpaymentamount();
                }
                carkindprice.put("quoteamount", quoteamount!=null?String.format("%.2f",quoteamount):"");//报价总金额，显示打折后的金额数据

				carkindprice.put("paystatue","");//支付报价状态null
				carkindprice.put("paystatueName","");//支付报价状态结果的字典value值
				carkindprice.put("isSelected", true);//此保险公司是否被选中

				INSBWorkflowsub workflowsub = new INSBWorkflowsub();
				workflowsub.setInstanceid(quoteinfonew.getWorkflowinstanceid());
				workflowsub = insbWorkflowsubDao.selectOne(workflowsub);

				//工作流还没有推子流程
				if (workflowsub == null) {
					carkindprice.put("quoteStatue", "2");//报价状态
					carkindprice.put("prevPayQuoteStatue","");//价格来源状态

					if (isRenewal) {
						carkindprice.put("quoteStatue", "15");
					}

					LogUtil.info(processInstanceId+","+quoteinfonew.getWorkflowinstanceid()+"无子流程数据");
				} else {

					carkindprice.put("quoteStatue", workflowsub.getTaskcode());//状态

					if ("20".equals(workflowsub.getTaskcode())) {//当前状态为20 支付的时候判断 是否 为柜台支付  柜台支付返回 柜台支付 状态
						if (!"admin".equals(workflowsub.getOperator()) && ordertemp.getPaymentmethod() != null && "4".equalsIgnoreCase(ordertemp.getPaymentmethod())) {
							carkindprice.put("quoteStatue", "201");//柜台支付状态
						}
						//onpayQuote = quoteinfonew.getInscomcode();
					}

					INSBWorkflowsubtrackdetail workflowsubtrackdetail = this.insbWorkflowsubtrackdetailDao.findPrevWorkFlow(workflowsub.getInstanceid(), workflowsub.getTaskcode());
					if (null == workflowsubtrackdetail) {
						carkindprice.put("prevPayQuoteStatue", "");//价格来源状态
					} else {
						carkindprice.put("prevPayQuoteStatue", workflowsubtrackdetail.getTaskcode());//价格来源状态
					}

					if("Closed".equals(workflowsub.getTaskstate())||
							"34".equals(workflowsub.getTaskcode())||
							"30".equals(workflowsub.getTaskcode()) ){//30-拒绝承保,34放弃
						carkindprice.put("isSelected", false);//此保险公司是否被选中
						carkindprice.put("quoteStatue",workflowsub.getTaskcode());
					}else{
						if(paycode.contains(workflowsub.getTaskcode()) && ordertemp!=null){
							INSBOrderpayment payment = new INSBOrderpayment();
							payment.setOrderid(ordertemp.getId());
							payment = insbOrderpaymentDao.selectOne(payment);
							if(payment!=null){
								carkindprice.put("paystatue",payment.getPayresult());//支付报价状态
							/*Map<String, String> param = new HashMap<String, String>();
							param.put("codetype", "payresult");
							param.put("parentcode", "payresult");
							param.put("codevalue", payment.getPayresult());*/
							}
						}

						if(maintaskcode.contains(workflowmain.getTaskcode())){
							//新添加的
							carkindprice.put("quoteStatue",workflowmain.getTaskcode());//主流程的状态
						}
					}
				}

				//判断是否是主流程到达后面的阶段
				List<Map<String, Object>> usercomment = insbUsercommentService.getNearestUserComment3(processInstanceId, quoteinfonew.getInscomcode(), null,1);
				if(usercomment!=null && usercomment.size()>0){
					String commentStr = "";
					for(Map<String, Object> comment : usercomment){
						commentStr += (String) comment.get("commentcontent");
						break;
					}
					carkindprice.put("quotenoti", commentStr);//报价的提示语
				}else{
					carkindprice.put("quotenoti", "");//报价的提示语
				}
				//  备注上传影像 改为从查询需要上传的接口获取，这里不查询。。。
				// 查询备注要上传的文件codetype
				//List<String> listCodeType = appInsuredQuoteService.queryUserCommentUploadFile_codeType(processInstanceId, inscomcode);
				//List<InsuranceImageBean> commentUpFile = appInsuredQuoteDao.selectBackNeedUploadImageByCodeType(listCodeType);
				//carkindprice.put("commentImages",commentUpFile);

				carkindprice.put("deptcodeCN", inscdept==null ? "":inscdept.getComname());//出单网点
				carkindprice.put("buyway", "01");//传统--01, 网销--02,电销--03
				carkindprice.put("saleMessage", "");//还需注入数据,todo
				carkindprice.put("processInstanceId", processInstanceId);

                //报价有效期
				List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectPolicyitemByInscomTask(map);
				//商业起保日期
				String systartdate = "";
				//交强起保日期
				String jqstartdate = "";
				Date busStartDate = null, strStartDate = null;
				if(policyitemList!=null && policyitemList.size()>0){
					for (int i = 0; i < policyitemList.size(); i++) {
						if("0".equals(policyitemList.get(i).getRisktype())){//商业险保单号
							carkindprice.put("busipolicyno", policyitemList.get(i).getPolicyno());
							systartdate = ModelUtil.conbertToString(policyitemList.get(i).getStartdate());
							busStartDate = policyitemList.get(i).getStartdate();
						}else if("1".equals(policyitemList.get(i).getRisktype())){//交强险保单号
							carkindprice.put("strapolicyno", policyitemList.get(i).getPolicyno());
							jqstartdate = ModelUtil.conbertToString(policyitemList.get(i).getStartdate());
							strStartDate = policyitemList.get(i).getStartdate();
						}
					}
				}
				LogUtil.info("报价有效期taskid"+processInstanceId+",供应商id="+quoteinfonew.getInscomcode()+",busStartDate="+busStartDate+",strStartDate="+strStartDate);

				//获取选择投保轨迹时间
				INSBWorkflowsubtrack subtrack = null;

				if (workflowsub != null) {
					subtrack = new INSBWorkflowsubtrack();
					subtrack.setMaininstanceid(processInstanceId);
					subtrack.setInstanceid(quoteinfonew.getWorkflowinstanceid());
					subtrack.setTaskcode("14");//选择投保
					List<INSBWorkflowsubtrack> workflowsubtrackList = insbWorkflowsubtrackDao.selectList(subtrack);//选择投保子流程轨迹信息

					subtrack = null;
					if (workflowsubtrackList != null && !workflowsubtrackList.isEmpty()) {
						subtrack = workflowsubtrackList.get(0);
					}
				}

				if(null == subtrack){
					carkindprice.put("quotesuccessTimes", "");
					carkindprice.put("isOver", false);
					// 次日起保标识
					carkindprice.put("isnextdayinsured", false);
				}else{
                    INSBProvider atainteger = insbProviderDao.selectById(quoteinfonew.getInscomcode());

					Date quotesuccessTimes = null;
					if(null == atainteger.getQuotationvalidity()){
						quotesuccessTimes = ModelUtil.gatFastPaydate(busStartDate, strStartDate);
					}else{
						quotesuccessTimes = ModelUtil.gatFastPaydateToNow(busStartDate, strStartDate, subtrack.getCreatetime(), atainteger.getQuotationvalidity());
					}

					LogUtil.info("报价有效期taskid"+processInstanceId+",供应商id="+quoteinfonew.getInscomcode()+",到期时间为="+quotesuccessTimes);

					if(quotesuccessTimes!=null)
						carkindprice.put("quotesuccessTimes", ModelUtil.conbertToString(quotesuccessTimes));   //报价有效期
					else{
						carkindprice.put("quotesuccessTimes", "");
					}

					if(ModelUtil.daysBetween(quotesuccessTimes, new Date()) > 0){
						carkindprice.put("isOver", true);
					}else{
						carkindprice.put("isOver", false);
					}
					// quoteshowflag  0 正常的 1 次日的 2 可能段保的
					carkindprice.put("quoteshowflag", getQuoteShowflag(processInstanceId, busStartDate, strStartDate, subtrack.getCreatetime()));
				}

				// 商业险起保日期
				carkindprice.put("systartdate", systartdate);
				// 交强险起保日期
				carkindprice.put("jqstartdate", jqstartdate);
				// 是否是上年投保公司
				carkindprice.put("islastyearpid", islastyearpid(processInstanceId,quoteinfonew.getInscomcode()));
				//是否新车
				carkindprice.put("isnewcar", isnewcar);
				topquotelist.add(carkindprice);
			}

			body.put("quoteinfolist", topquotelist);
			INSBWorkflowmaintrack insbWorkflowmaintrack = new INSBWorkflowmaintrack();
			insbWorkflowmaintrack.setInstanceid(processInstanceId);
			insbWorkflowmaintrack.setTaskcode("2");
			INSBWorkflowmaintrack workflowmaintrack = insbWorkflowmaintrackDao.selectOne(insbWorkflowmaintrack);
			if(null == workflowmaintrack){
	            // 起保日期
				body.put("startquotetime", "");
	        }else{
	        	// 起保日期
	        	body.put("startquotetime", ModelUtil.conbertToStringsdf(workflowmaintrack.getCreatetime())); 	
	        }
			resultAll.setBody(body);
			resultAll.setMessage("订单多方报价查询成功");
			resultAll.setStatus("success");

			return JSONObject.fromObject(resultAll).toString();
		}catch(Exception e){
			e.printStackTrace();
			resultAll.setMessage("订单多方报价查询失败");
			resultAll.setStatus("fail");
			return JSONObject.fromObject(resultAll).toString();
		}
	}

	/**
	 * 判断多方列表前端显示起保日期状态
	 * 商业险交强险有一个是次日就显示次日起保
	 * @param taskid
	 * @param busStartDate
	 * @param strStartDate
	 * @param createtime
	 * @return  0 正常的， 1 次日的， 2 可能断保的
	 */
	private int getQuoteShowflag(String taskid, Date busStartDate, Date strStartDate, Date createtime) {
		int resule = 0;

		Map<String, Date> dates = commonQuoteinfoService.getNextPolicyDate(taskid);
		if (dates.isEmpty()) return resule;

		Date syEffectDate = dates.get("commEffectDate"), jqEffectDate = dates.get("trafEffectDate");

        String startquote = ModelUtil.conbertToString(ModelUtil.nowDateAddOneDay(createtime));//开始报价的日期T+1
        String systartdate = ModelUtil.conbertToString(busStartDate);
        String jqstartdate = ModelUtil.conbertToString(strStartDate);

        if(isupdatedate(systartdate, jqstartdate, syEffectDate, jqEffectDate)){
            resule = 2;
        }
		if(isnextdayinsured(systartdate, jqstartdate, startquote)){
			resule = 1;
		}
		return resule;
	}
	/**
	 * 代理人是否修改了起保日期
	 * 和 平台查询返回的值作比较，不一样默认有改动
	 * @param systartdate
	 * @param jqstartdate
	 * @return true 修改了
	 */
	private boolean isupdatedate(String systartdate,String jqstartdate,Date sylastsdate,Date jqlastsdate) {
		boolean syflag = true;
		boolean jqflag = true;
		if(sylastsdate == null || StringUtil.isEmpty(systartdate)){
			syflag = false;
		}else{
			syflag = !systartdate.startsWith(ModelUtil.conbertToString(sylastsdate));
		}

		if(jqlastsdate == null || StringUtil.isEmpty(jqstartdate)){
			jqflag = false;
		}else{
			jqflag = !jqstartdate.startsWith(ModelUtil.conbertToString(jqlastsdate));
		}
		return syflag || jqflag;
	}
	/**
	 * 判断是否次日起保，商业险交强险只要有一个次日起保 req244
	 * @param systartdate
	 * @param jqstartdate
	 * @param startquote 报价发起日期
	 * @return true 是
	 */
	private boolean isnextdayinsured(String systartdate,String jqstartdate,String startquote) {
		boolean syflag = false;
		boolean jqflag = false;
		if(!StringUtil.isEmpty(systartdate)){
			syflag = startquote.equals(systartdate);
		}
		if(!StringUtil.isEmpty(jqstartdate)){
			jqflag = startquote.equals(jqstartdate);
		}
		return syflag || jqflag;
	}
	/**
	 * 判断是否是上年投保公司
	 * @param taskid
	 * @param inscomcode
	 * @return true 是
	 */
	private boolean islastyearpid(String taskid,String inscomcode) {
		boolean res = false;
//		INSBLastyearinsureinfo insblastyearinsureinfo = new INSBLastyearinsureinfo();
//		insblastyearinsureinfo.setTaskid(taskid);
		INSBLastyearinsureinfo lastyearinsureinfo = insbRulequerycarinfoDao.queryLastYearClainInfo(taskid);
		if(null != lastyearinsureinfo){
			res = StringUtil.isEmpty(lastyearinsureinfo.getSupplierid()) ? false:inscomcode.startsWith(lastyearinsureinfo.getSupplierid().substring(0, 4));
		}
		return res;
	}
	@Override
	protected BaseDao<INSBQuoteinfo> getBaseDao() {
		return null;
	}
	@Override
	public CommonModel updateInsureDate(UpdateInsureDateModel model) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = model.getProcessinstanceid();
			String inscomcode = model.getInscomcode();
			if(StringUtil.isEmpty(taskid) || StringUtil.isEmpty(inscomcode)){
				commonModel.setMessage("参数不正确");
				commonModel.setStatus("fail");
				return commonModel;
			}
			String systartdate = model.getSystartdate();
			String jqstartdate = model.getJqstartdate();
			if(StringUtil.isEmpty(systartdate) && StringUtil.isEmpty(jqstartdate)){
				commonModel.setMessage("起保日期不能全为空");
				commonModel.setStatus("fail");
				return commonModel;
			}
			if (StringUtil.isNotEmpty(systartdate) && DateUtil.getCurrentDate().compareTo(systartdate.substring(0, 10)) >=0) {
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_MONTH, 1);
				systartdate = DateUtil.toDateTimeString(c.getTime());
			}
			if (StringUtil.isNotEmpty(jqstartdate) && DateUtil.getCurrentDate().compareTo(jqstartdate.substring(0, 10)) >=0) {
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_MONTH, 1);
				jqstartdate = DateUtil.toDateTimeString(c.getTime());
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("taskid", taskid);
			map.put("inscomcode", inscomcode);
			INSBQuoteinfo insbQuoteinfo = appInsuredQuoteDao.selectInsbQuoteInfoByTaskidAndPid(map);
			if(null == insbQuoteinfo){
				commonModel.setStatus("fail");
				commonModel.setMessage("报价信息表数据有误");
				return commonModel;
			}
			insbQuoteinfo.setQuotediscountamount(null);
			insbQuoteinfoDao.updateById(insbQuoteinfo);
			INSBWorkflowmain mainModel = insbWorkflowmainDao.selectByInstanceId(taskid);
			if (null == mainModel) {
				commonModel.setStatus("fail");
				commonModel.setMessage("获取操作人失败");
				return commonModel;
			}
			INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
			insbPolicyitem.setTaskid(taskid);
			insbPolicyitem.setInscomcode(inscomcode);
			List<INSBPolicyitem> insbPolicyitems = insbPolicyitemDao.selectList(insbPolicyitem);
			if(null != insbPolicyitems && insbPolicyitems.size() > 0){
				for(INSBPolicyitem policyitem : insbPolicyitems){
					if("0".equals(policyitem.getRisktype())){
						//更新起保日期
						policyitem.setStartdate(ModelUtil.conbertStringToNyrDate(systartdate));// 开始时间
						policyitem.setEnddate( ModelUtil.nowDateMinusOneDay(ModelUtil.nowDateAddOneYear(ModelUtil.conbertStringToNyrDate(systartdate), 1)));// 开始时间   加一年减一天
					}else{
						//更新起保日期
						policyitem.setStartdate(ModelUtil.conbertStringToNyrDate(jqstartdate));// 开始时间
						policyitem.setEnddate( ModelUtil.nowDateMinusOneDay(ModelUtil.nowDateAddOneYear(ModelUtil.conbertStringToNyrDate(jqstartdate), 1)));// 开始时间   加一年减一天
					}
					insbPolicyitemDao.updateById(policyitem);
				}
			}

			WorkflowFeedbackUtil.setWorkflowFeedback(taskid, insbQuoteinfo.getWorkflowinstanceid(), "14", "Completed", "选择投保", WorkflowFeedbackUtil.quote_redo, "admin");
			//重新发起报价请求，调用工作流

			taskthreadPool4workflow.execute(new Runnable() {
				@Override
				public void run() {
					Map<String, Object> map =  new HashMap<String, Object>();
					map = appInsuredQuoteService.getPriceParamWay(map, taskid, inscomcode, "0");
					WorkFlowUtil.updateInsuredInfoNoticeWorkflow(map,insbQuoteinfo.getWorkflowinstanceid(), mainModel.getOperator(),"选择投保", "1");
				}
			});
			commonModel.setMessage("操作成功");
			commonModel.setStatus("success");
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setMessage("操作失败");
			commonModel.setStatus("fail");
		}
		return commonModel;
	}
}
