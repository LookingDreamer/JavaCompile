package com.zzb.cm.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.DispatchService;
import com.cninsure.jobpool.Task;
import com.cninsure.jobpool.dispatch.DispatchTaskService;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCUserService;
import com.common.CloudQueryUtil;
import com.common.HttpClientUtil;
import com.common.TaskConst;
import com.common.WorkFlowUtil;
import com.common.WorkflowFeedbackUtil;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import com.zzb.app.service.AppQuotationService;
import com.zzb.app.service.AppSupplementItemService;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.cm.controller.vo.SupplementInfoVO;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBFilebusinessDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBFilebusiness;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.entity.INSBSupplement;
import com.zzb.cm.service.INSBCarinfoService;
import com.zzb.cm.service.INSBFilelibraryService;
import com.zzb.cm.service.INSBManualPriceService;
import com.zzb.cm.service.INSBMyTaskService;
import com.zzb.cm.service.INSBOrderService;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.cm.service.INSBSupplementService;
import com.zzb.conf.dao.INSBWorkflowmainDao;
import com.zzb.conf.dao.INSBWorkflowmaintrackdetailDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.dao.INSBWorkflowsubtrackdetailDao;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowmaintrackdetail;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.entity.INSBWorkflowsubtrackdetail;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.WorkFlowRuleInfo;
import com.zzb.mobile.model.lastindanger.LastClaimBackInfo;
import com.zzb.mobile.model.lastinsured.CarModel;
import com.zzb.mobile.model.lastinsured.LastYearPolicyInfoBean;
import com.zzb.mobile.service.AppOtherRequestService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



@Service
@Transactional
public class INSBManualPriceServiceImpl extends BaseServiceImpl<INSBWorkflowmain> implements
		INSBManualPriceService {
    public static final String SUPPLEMENT_INFO_VO = "cm:zzb:manual_price:supplement_info_vo";
    private static String WORKFLOWURL = "";

    @Resource
	private INSBSupplementService insbSupplementService;
	static {
		// 读取相关的配置
		ResourceBundle resourceBundle = ResourceBundle.getBundle("config/config");
		WORKFLOWURL = resourceBundle.getString("workflow.url");
	}
	@Resource
	private INSBWorkflowmainDao insbWorkflowmainDao;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBWorkflowsubDao insbWorkflowsubDao;
	@Resource
	private INSBFilelibraryService insbFilelibraryService;
	@Resource
	private INSBFilebusinessDao insbFilebusinessDao;
	@Resource
	private AppQuotationService appQuotationService;
	@Resource
	private AppOtherRequestService appOtherRequestService;
	@Resource
	private DispatchService dispatchService;
	@Resource
	private AppSupplementItemService appSupplementItemService;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
	@Resource
	private INSBOrderService insbOrderService;
	@Resource
	private INSBQuoteinfoService insbQuoteinfoService;
	@Resource
	private INSBWorkflowsubService workflowsubService;
    @Resource
	private INSBCarinfoService insbCarinfoService;
	@Resource
    private IRedisClient redisClient;
	@Resource
	private DispatchTaskService dispatchTaskService;
	@Resource
	private INSBWorkflowmaintrackdetailDao insbWorkflowmaintrackdetailDao;
	@Resource
	private INSBWorkflowsubtrackdetailDao insbWorkflowsubtrackdetailDao;
	@Resource
	private INSCUserService inscUserService; 
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBMyTaskService myTaskService;
	
	@Override
	protected BaseDao<INSBWorkflowmain> getBaseDao() {
		return insbWorkflowmainDao;
	}

	/**
	 * 通过流程实例id和保险公司code得到工作流子流程数据
	 */
	public INSBWorkflowsub getWorkflowSub(String instanceId, String inscomcode) {
		INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
		quotetotalinfo.setTaskid(instanceId);
		quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
		INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
		quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
		quoteinfo.setInscomcode(inscomcode);
		quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
		INSBWorkflowsub workflowsub = new INSBWorkflowsub();
		workflowsub.setInstanceid(quoteinfo.getWorkflowinstanceid());
		workflowsub.setMaininstanceid(instanceId);
		workflowsub = insbWorkflowsubDao.selectOne(workflowsub);
		return workflowsub;
	}
	/**
	 * 报价通过或报价退回修改按钮调用接口
	 */
	@Override
	public String quoteTotailPricePassOrBackForEdit(String instanceId, String userid, String quoteResult,String incoms) {
		LogUtil.info(instanceId+"在quoteTotailPricePassOrBackForEdit中：参数incoms="+incoms+"参数userid="+userid+"参数quoteResult="+quoteResult);
		INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();
		insbWorkflowsub.setInstanceid(instanceId);
		insbWorkflowsub = insbWorkflowsubDao.selectOne(insbWorkflowsub);
		List<INSBWorkflowsub> insbWorkflowsubList = insbWorkflowsubDao.selectSubModelByMainInstanceId(insbWorkflowsub.getMaininstanceid());
		INSBQuoteinfo insbQuoteinfo = null;
		// 查询是否为非费改地区
		//boolean isfeeflag = insbOrderService.isFeeflagArea(null, instanceId);
			for( INSBWorkflowsub workflowsub : insbWorkflowsubList ) {
				String processinstanceid = workflowsub.getInstanceid();
				insbQuoteinfo = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(processinstanceid);
				if(null==insbQuoteinfo||!incoms.contains(insbQuoteinfo.getInscomcode())){//不处理。未勾选此子公司，不处理此子公司任务
					continue;
				}
				boolean dealflag = myTaskService.checkCloseTask(workflowsub.getMaininstanceid(), insbQuoteinfo.getInscomcode(), userid);
				if(dealflag){//任务已经不处于当前操作人处理范围，或者已经关闭
					continue;
				}
				taskthreadPool4workflow.execute(new Runnable() {
					@Override
					public void run() {
						try {
							quotePricePassOrBackForEdit(processinstanceid, userid, quoteResult);
						}catch(Exception ex) {
							ex.printStackTrace();
						}
					}
				});
			}
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("status", "success");
			String quoteType = "";
			if( "3".equals(quoteResult) ) {
				quoteType = "报价通过";
			} else if( "2".equals(quoteResult) ) {
				quoteType = "退回修改";
			} else if( "1".equals(quoteResult) ) {
				quoteType= "重新报价";
			}
			resultMap.put("msg", "后台正在发起勾选公司" + quoteType + "，请等候......");
			return JSONObject.fromObject(resultMap).toString();
	}
	
	/**
	 * 报价通过或报价退回修改按钮调用接口
	 */
	@Resource InterFaceService interFaceService;
	@Override
	public String quotePricePassOrBackForEdit(String instanceId, String userid, String quoteResult) {
		interFaceService.saveHisInfo(instanceId, "person", "A");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 组织参数
			Map<String, String> params = new HashMap<String, String>();
			Map<String, Object> datas = new HashMap<String, Object>();
			Map<String, Object> data = new HashMap<String, Object>();

			data.put("result", quoteResult);//报价结果：1获取报价途径，2报价退回，3选择保价，4终止报价
			if("3".equals(quoteResult)){//报价通过
				data.put("gztorgway", "0");//gztorgway 0 表示 直接报价成功  1 表示 转人工报价
			} else if("1".equals(quoteResult)){//转人工
				data.put("gztorgway", "1");
			}
			datas.put("taskName", "人工规则报价");
			datas.put("data", data);//数据
			datas.put("processinstanceid", Integer.parseInt(instanceId));//流程实例ID
			datas.put("userid",userid);//userid

			JSONObject datasJSON = JSONObject.fromObject(datas);
			params.put("datas", datasJSON.toString());
			LogUtil.info(instanceId+"在quotePricePassOrBackForEdit中调用工作流：参数datas="+params.get("datas"));

			String taskFeedBack = "人工规则报价";
			if("1".equals(quoteResult)){
				taskFeedBack = "转人工报价";
			}else if("2".equals(quoteResult)){
				taskFeedBack = WorkflowFeedbackUtil.quote_sendback;
			}else if("3".equals(quoteResult)){
				taskFeedBack = WorkflowFeedbackUtil.quote_complete;
			}
			WorkflowFeedbackUtil.setWorkflowFeedback(null, instanceId, "7", "Completed", "人工规则报价", taskFeedBack, userid);

			//调取工作流人工规则报价接口
			String result = HttpClientUtil.doGet(WORKFLOWURL+"/process/completeSubTask", params);
            LogUtil.info(instanceId+","+quoteResult+","+userid+"在quotePricePassOrBackForEdit中调用工作流接口返回："+result);
            resultMap.put("status", "success");
			resultMap.put("msg", "操作成功！");
			//返回信息{"fail":"errorTask '1212' not found","message":"fail"}
			/*JSONObject jo = JSONObject.fromObject(result);
			if("success".equals(jo.getString("message"))){
				resultMap.put("status", "success");
				resultMap.put("msg", "操作成功！");
				//messageManager.sendMessage4Agent4Refuse(null,instanceId,"人工规则报价");//添加发消息
			}else if("fail".equals(jo.getString("message"))){
				resultMap.put("status", "fail");
				resultMap.put("msg", jo.getString("reason"));
			}*/
			return JSONObject.fromObject(resultMap).toString();
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", "fail");
			resultMap.put("msg", "系统错误！");
			return JSONObject.fromObject(resultMap).toString();
		}
	}

	/**
	 * 退回修改按钮调用接口和报价通过按钮调用相同接口
	 */
//	@Override
//	public String quotePriceBackForEdit(String instanceId, String inscomcode, String userid) {
//		try {
//			// 组织参数
//			INSBWorkflowsub workflowsub = getWorkflowSub(instanceId, inscomcode);
//			Map<String, String> params = new HashMap<String, String>();
//			Map<String, String> datas = new HashMap<String, String>();
//			datas.put("userid", userid);//userid
//			datas.put("taskid", workflowsub.getTaskid());//任务ID
//			datas.put("getpriceresult", "3");//报价结果为失败
//			datas.put("processinstanceid", instanceId);//流程实例ID
//			JSONObject datasJSON = JSONObject.fromObject(datas);
//			params.put("datas", datasJSON.toString());//数据
//			params.put("claim", "1");//下一节点是否自动认领（0为不需要，1为需要）
//			//调取工作流报价退回修改接口
//			String result = HttpClientUtil.doGet(WORKFLOWURL+"/process/completeSubTask", params);
//			System.out.println(result);
//			//返回信息{"message":null}
//			JSONObject jo = JSONObject.fromObject(result);
//			if("success".equals(jo.getString("message"))){
//				//退回修改执行成功，数据库中同步处理数据
//			}else{
////				throw new Exception(jo.getString("fail"));
//				throw new Exception("提交报价退回修改失败！");
//			}
//			return "success";
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "fail";
//		}
//	}

	/**
	 * 转人工处理按钮调用接口和报价通过、退回修改按钮调用相同接口
	 */
//	@Override
//	public String quotePriceToManual(String instanceId, String inscomcode) {
//		System.out.println("转人工处理按钮调用接口");
//		return null;
//	}

	@Override
	public String quoteRefuseUnderwrite(String maininstanceId, String processinstanceid, String inscomcode, String mainorsub, String from, final String operator) {
		// 查询是否为非费改地区
		boolean isfeeflag = insbOrderService.isFeeflagArea(maininstanceId, null);
		if( !isfeeflag ) {
			// 是非费改地区
			List<INSBWorkflowsub> insbWorkflowsubList = insbWorkflowsubDao.selectSubModelByMainInstanceId(maininstanceId);
			for( INSBWorkflowsub workflowsub : insbWorkflowsubList ) {
				String subinstanceId = workflowsub.getInstanceid();
				try {
					taskthreadPool4workflow.execute(new Runnable() {
						@Override
						public void run() {
							String message = refuseUnderwrite(maininstanceId, subinstanceId, inscomcode, mainorsub, from, operator);
							LogUtil.info("非费改地区拒绝承保taskid=" + maininstanceId + "###提交子任务id=" + subinstanceId + "返回信息：" + message);
						}
					});
				} catch(Exception ex) {
					LogUtil.info("非费改地区拒绝承保taskid=" + maininstanceId + "###提交子任务id=" + subinstanceId + "异常：");
					ex.printStackTrace();
				}
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("status", "success");
			resultMap.put("msg", "后台正在发起全部拒绝承保，请等候......");
			return JSONObject.fromObject(resultMap).toString();
		} else {
			return refuseUnderwrite(maininstanceId, processinstanceid, inscomcode, mainorsub, from, operator);
		}
	}
	
	/**
	 * 拒绝承保按钮调用接口
	 */
	@Override
	public String refuseUnderwrite(String maininstanceId, String subinstanceId, String inscomcode, String mainorsub, String from, String operator) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//修改订单状态
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("taskid", maininstanceId);
		map.put("inscomcode", inscomcode);
		INSBOrder order = insbOrderDao.selectOrderByTaskId(map);
		if(order!=null){
			order.setOrderstatus("5");
			insbOrderDao.updateById(order);
		}

		// 组织参数
		Map<String, String> params = new HashMap<String, String>();
		Map<String, Object> datas = new HashMap<String, Object>();

		boolean ismain = false;

		if("main".equals(mainorsub)){
			datas.put("processinstanceid", maininstanceId);//流程实例id
			ismain = true;
		}else{
			datas.put("processinstanceid", subinstanceId);//流程实例id
		}

		datas.put("process", mainorsub);//主或子流程
		datas.put("from", from);//后台拒绝承保
		JSONObject datasJSON = JSONObject.fromObject(datas);
		params.put("datas", datasJSON.toString());

		resultMap.put("status", "success");
		resultMap.put("msg", "操作成功！");

		String taskcode = "";
		if(!ismain){
			INSBWorkflowsub newModel = insbWorkflowsubDao.selectByInstanceId(subinstanceId);
			if (newModel != null) {
				taskcode = newModel.getTaskcode();
			}
		}

		String taskname = "", taskfeedback = "";
		if (StringUtil.isNotEmpty(taskcode)) {
			if ("8".equals(taskcode)) {
				taskname = "人工报价";
				taskfeedback = WorkflowFeedbackUtil.quote_reject;
			} else if ("18".equals(taskcode)) {
				taskname = "人工核保";
				taskfeedback = WorkflowFeedbackUtil.underWriting_reject;
			}
			workflowsubService.updateWorkFlowSubData(maininstanceId, subinstanceId, taskcode, "Completed", taskname, taskfeedback, operator);
			WorkflowFeedbackUtil.setWorkflowFeedback(maininstanceId, subinstanceId, "37", "Closed", null,
					WorkflowFeedbackUtil.quote_reject+"#业管拒绝承保", "admin");
		}

		if (ismain) {
			WorkflowFeedbackUtil.setWorkflowFeedback(maininstanceId, null, "37", "Closed", null,
					WorkflowFeedbackUtil.quote_reject+"#业管拒绝承保", "admin");
		}
		
		//通知调度删除任务
        taskthreadPool4workflow.execute(new Runnable() {
			@Override
			public void run() {
				Task task = new Task();
				//主流程任务
				if ("main".equals(mainorsub)) {
					task.setProInstanceId(maininstanceId);
				} else {//子流程任务
					task.setProInstanceId(maininstanceId);
					task.setSonProInstanceId(subinstanceId);
				}
				task.setDispatchUser(from);
				task.setPrvcode(inscomcode);
				LogUtil.info("maininstanceId=" + maininstanceId + "," + "subinstanceId=" + subinstanceId + "," + "operator=" + from +
						"在refuseUnderwrite中通知调度删除任务");
				dispatchTaskService.deleteTask(task);
			}
		});
		try {
			taskthreadPool4workflow.execute(new Runnable() {
				@Override
				public void run() {
					try {
						//休眠，防止上面的updateWorkFlowSubData跟工作流通知回调后的处理产生死锁
						Thread.sleep(1000);
					} catch (Exception e){}

					//调取工作流终止工作流程接口
					String result = HttpClientUtil.doGet(WORKFLOWURL+"/process/abortProcessById", params);
					LogUtil.info(maininstanceId+","+subinstanceId+","+inscomcode+"在refuseUnderwrite中调用工作流接口abortProcessById返回："+result);
				}
			});
		} catch (Exception e) {
			LogUtil.info(maininstanceId+","+subinstanceId+","+inscomcode+"在refuseUnderwrite中异步调用工作流接口出错："+e.getMessage());
			//resultMap.put("status", "fail");
			//resultMap.put("msg", "工作流异步调用失败！");
		}

		return JSONObject.fromObject(resultMap).toString();
	}

	/**
	 * 打回任务按钮调用接口
	 * 如果不存在子流程，只传递主流程实例id,不用传递保险公司code
	 * 最新结果：改为调用张伟提供拒接任务接口
	 */
	@Override
	public String releaseTask(String instanceId, String inscomcode, int instanceType, INSCUser loginUser) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			dispatchService.refuseTask(instanceId, inscomcode, instanceType, loginUser);
			result.put("status", "success");
			result.put("msg", "打回任务成功！");
			return JSONObject.fromObject(result).toString();
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "fail");
			result.put("msg", "打回任务失败！"+e.getMessage());
			return JSONObject.fromObject(result).toString();
		}
	}
	
	/**
	 * 报价打回任务按钮调用接口
	 * 如果不存在子流程，只传递主流程实例id,不用传递保险公司code
	 */
	@Override
	public String quoteReleaseTask(String instanceId, String inscomcode, int instanceType, INSCUser loginUser) {
		String taskid = "";
		if( "1".equals(instanceType) ) { // 主工作流id
			return releaseTask(instanceId, inscomcode, instanceType, loginUser);
		} else { // 子工作流id
			INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();
			insbWorkflowsub.setInstanceid(instanceId);
			insbWorkflowsub = insbWorkflowsubDao.selectOne(insbWorkflowsub);
			taskid = insbWorkflowsub.getMaininstanceid();
		}
		// 查询是否为非费改地区
		boolean isfeeflag = insbOrderService.isFeeflagArea(taskid, null);
		if( !isfeeflag ) {
			// 是非费改地区
			List<INSBWorkflowsub> insbWorkflowsubList = insbWorkflowsubDao.selectSubModelByMainInstanceId(taskid);
			for( INSBWorkflowsub workflowsub : insbWorkflowsubList ) {
				String processinstanceid = workflowsub.getInstanceid();
				INSBQuoteinfo insbQuoteinfo = insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(processinstanceid);
				String insComcode = insbQuoteinfo.getInscomcode();
				try {
					taskthreadPool4workflow.execute(new Runnable() {
						@Override
						public void run() {
							String message = releaseTask(processinstanceid, insComcode, instanceType, loginUser);
						}
					});
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			Map<String, String> result = new HashMap<String, String>();
			result.put("status", "success");
			result.put("msg", "后台正在发起全部打回任务，请等候......");
			return JSONObject.fromObject(result).toString();
		} else {
			return releaseTask(instanceId, inscomcode, instanceType, loginUser);
		}
	}

	@Override
	public Map<String, Object> fileUpLoad(HttpServletRequest request, MultipartFile file,
			String fileType, String fileDescribes, String userCode,String processinstanceid,String taskid) {
		Map<String, Object> map = new HashMap<String,Object>();
		if(checkIsImage(file.getContentType())){
//			if(StringUtil.isEmpty(processinstanceid)){
//				map.put("status", "fail");
//				map.put("msg", "任务流水号不能为空");
//			}else{
				LogUtil.info(taskid+"--"+file.toString()+"--"+fileType+"--"+fileDescribes+"--"+userCode);
				map = insbFilelibraryService.uploadOneFile(request,file, fileType, fileDescribes, userCode);

				if("success".equals((String)map.get("status"))){
					map.put("filedescribe", fileDescribes);
					map.put("taskid", taskid);
					map.put("subInstanceId", processinstanceid);
					INSBFilebusiness insbFilebusiness = new INSBFilebusiness();
					insbFilebusiness.setCreatetime(new Date());
					insbFilebusiness.setOperator(null == userCode ? "" : userCode);
					insbFilebusiness.setType(fileType);
					insbFilebusiness.setFilelibraryid((String)map.get("fileid"));
//					insbFilebusiness.setCode(processinstanceid);
					insbFilebusiness.setCode(taskid);
					insbFilebusinessDao.insert(insbFilebusiness);
				}
//			}
		}else{
			map.put("status", "fail");
			map.put("msg", "文件类型不符合要求");
		}
		return map;
	}

	//判断是否为图片类型文件
	public boolean checkIsImage(String contentType){
		if("image/bmp".equalsIgnoreCase(contentType) || "image/gif".equalsIgnoreCase(contentType) || 
				"image/jpeg".equalsIgnoreCase(contentType) || "image/png".equalsIgnoreCase(contentType)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 规则试算调用接口
	 */
	@Override
    public String ruleCalculation(String instanceId, String subInstanceId, String inscomcode, boolean priceLimitFlag) {
        String result = "";//{"success":true,"resultMsg":"信息"}
        // 调用规则试算接口
        if(priceLimitFlag){//放开对所有公司车价浮动范围的限制
            result = appQuotationService.getQuotationInfo(subInstanceId, instanceId, inscomcode);
        }else{//需要试算前做校验
            result = appQuotationService.getQuotationValidatedInfo(subInstanceId, instanceId, inscomcode);
            if(JSONObject.fromObject(result).getBoolean("success")){
                result = appQuotationService.getQuotationInfo(subInstanceId, instanceId, inscomcode);
            }
        }
        if(!StringUtils.isEmpty(result)){
            if(JSONObject.fromObject(result).getBoolean("success")){
                //查询试算总保费
                JSONObject jo = JSONObject.fromObject(result);
                jo.put("totalAmountprice", insbQuoteinfoDao.getTotalDiscountAmountPrice(subInstanceId));
                result = jo.toString();
            }
        }

        LogUtil.info(instanceId+","+inscomcode+"规则试算结果："+result);
        return result;
    }

	/**
	 * 获取补充信息中车型上面部分信息
	 */
	@Override
	public Map<String, Object> getReplenishInfo(String instanceId) {
		Map<String, Object> temp = new HashMap<String, Object>();
		// 从redis里获取数据
		LastClaimBackInfo lastClaimBackInfo = CloudQueryUtil.getLastClaimBackInfo(instanceId);
		Map<String, Object> rulequery = insbCarinfoService.getCarInfoByTaskId(instanceId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(lastClaimBackInfo != null){
			temp.put("noClaimDiscountCoefficient", lastClaimBackInfo.getNoClaimDiscountCoefficient());//无赔款优待系数
			temp.put("claimTimes", lastClaimBackInfo.getClaimTimes());//平台商业险理赔次数
			temp.put("bwLastClaimSum", lastClaimBackInfo.getLastClaimSum());//平台商业险理赔总金额
//			temp.put("firstInsureType", lastClaimBackInfo.getFirstInsureType());//投保类型(非首次投保:0,新车首次投保:1,旧车首次投保:2)
			if("0".equals(lastClaimBackInfo.getFirstInsureType())){
				temp.put("firstInsureType", "非首次投保");//投保类型(非首次投保:0,新车首次投保:1,旧车首次投保:2)
			}else if("1".equals(lastClaimBackInfo.getFirstInsureType())){
				temp.put("firstInsureType", "新车首次投保");//投保类型(非首次投保:0,新车首次投保:1,旧车首次投保:2)
			}else if("2".equals(lastClaimBackInfo.getFirstInsureType())){
				temp.put("firstInsureType", "旧车首次投保");//投保类型(非首次投保:0,新车首次投保:1,旧车首次投保:2)
			}
			temp.put("loyalty", lastClaimBackInfo.getLoyalty());//平台客户忠诚度系数
			temp.put("loyaltyReasons", lastClaimBackInfo.getLoyaltyReasons());//客户忠诚度原因
			temp.put("compulsoryClaimRate", lastClaimBackInfo.getCompulsoryClaimRate());//交强险理赔系数
			temp.put("platformCarPrice", lastClaimBackInfo.getPlatformCarPrice());//平台车价(部分地区)
			temp.put("compulsoryClaimTimes", lastClaimBackInfo.getCompulsoryClaimTimes());//平台交强险理赔次数
			temp.put("noClaimDiscountReason", lastClaimBackInfo.getNoClaimDiscountCoefficientReasons());//无赔款折扣浮动原因
			temp.put("compulsoryFloatReason", lastClaimBackInfo.getCompulsoryClaimRateReasons());//交强险浮动原因
			temp.put("traveltaxLateFee", lastClaimBackInfo.getVehicleTaxOverdueFine());//车船税滞纳金
			temp.put("previousPayTax", lastClaimBackInfo.getLwArrearsTax());//往年补交税额
			temp.put("compulsoryDiscountPremium", lastClaimBackInfo.getEfcDiscount());//交强险折扣保费(部分地区)
			temp.put("dangerType", lastClaimBackInfo.getRiskClass());//风险类别
			temp.put("isTelemarketing", lastClaimBackInfo.getPureESale());//是否通过纯电销投保
			temp.put("compulsoryIllegalFloat", lastClaimBackInfo.getEfcFloat());//交强险违法浮动
			temp.put("claimrate", lastClaimBackInfo.getBwCommercialClaimRate());//上年商业险赔付率
			temp.put("trafficoffencediscount", lastClaimBackInfo.getTrafficOffenceDiscount());//交通违法系数(部分地区)
			if(lastClaimBackInfo.getCurrenttime()!=null){
				temp.put("renewInfoDate", sdf.format(lastClaimBackInfo.getCurrenttime()));//数据最后更新时间
			}
			temp.put("status", "success");//查询结果状态
			temp.put("periodOfValidity", "当日有效");//数据有效期
		}
		if(null==rulequery.get("result")){
			temp.put("loyaltyReasons", rulequery.get("loyaltyreasons"));//平台客户忠诚度系数
			temp.put("firstInsureType", rulequery.get("firstinsuretype"));
			temp.put("noClaimDiscountCoefficient", rulequery.get("noclaimdiscountcoefficient"));//无赔款优待系数
			temp.put("noClaimDiscountReason", rulequery.get("noclaimdiscountcoefficientreasons"));//无赔款折扣浮动原因
			temp.put("claimTimes", rulequery.get("claimtimes"));//平台商业险理赔次数
			temp.put("bwLastClaimSum", rulequery.get("bwlastclaimsum"));//平台商业险理赔总金额
			temp.put("compulsoryClaimRate", rulequery.get("compulsoryclaimrate"));//交强险理赔系数
			temp.put("compulsoryClaimTimes", rulequery.get("compulsoryclaimtimes"));//平台交强险理赔次数
			temp.put("compulsoryFloatReason", rulequery.get("compulsoryclaimratereasons"));//交强险浮动原因
			temp.put("traveltaxLateFee", rulequery.get("vehicletaxoverduefine"));//车船税滞纳金
			temp.put("previousPayTax", rulequery.get("lwarrearstax"));//往年补缴税额
			temp.put("compulsoryDiscountPremium", rulequery.get("efcdiscount"));//交强险折扣保费(部分地区)
			temp.put("dangerType", rulequery.get("riskclass"));//风险类别
			temp.put("isTelemarketing", rulequery.get("pureesale"));//是否通过纯电销投保
//			temp.put("compulsoryIllegalFloat", rulequery.get(""));//交强险违法浮动
			temp.put("platformCarPrice", rulequery.get("platformcarprice"));//平台车价(部分地区)
			temp.put("drunking", rulequery.get("drunkdrivingrate"));//酒驾系数
			temp.put("claimrate", rulequery.get("bwcommercialclaimrate"));//上年商业险赔付率
			temp.put("trafficoffencediscount", rulequery.get("trafficoffencediscount"));//交通违法系数(部分地区)
			temp.put("status", "success");//查询结果状态
			if(rulequery.get("creattime")!=null){
				temp.put("renewInfoDate", rulequery.get("creattime"));//数据最后更新时间 
			}
			temp.put("periodOfValidity", "当日有效");//数据有效期
		}
//		LastYearPolicyInfoBean lastYearPolicyInfoBean = redisClient.get(instanceId,LastYearPolicyInfoBean.class);
//		if(null != lastYearPolicyInfoBean){
//			LastYearClaimBean lastYearClaimBean = lastYearPolicyInfoBean.getLastYearClaimBean();
//			if(lastYearClaimBean!=null){
//				temp.put("claimrate", lastYearClaimBean.getClaimrate());//上年商业险赔付率
//				temp.put("trafficoffencediscount", lastYearClaimBean.getTrafficoffencediscount());//交通违法系数(部分地区)
//				if(lastYearClaimBean.getClaimtimes()!=0){
//					temp.put("claimTimes", lastYearClaimBean.getClaimtimes());//平台商业险理赔次数
//				}
//				if(lastClaimBackInfo.getFirstInsureType()==null && !("".equals(lastClaimBackInfo.getFirstInsureType()))){
//					if("0".equals(lastClaimBackInfo.getFirstInsureType())){
//						temp.put("firstInsureType", "非首次投保");//投保类型(非首次投保:0,新车首次投保:1,旧车首次投保:2)
//					}else if("1".equals(lastClaimBackInfo.getFirstInsureType())){
//						temp.put("firstInsureType", "新车首次投保");//投保类型(非首次投保:0,新车首次投保:1,旧车首次投保:2)
//					}else if("2".equals(lastClaimBackInfo.getFirstInsureType())){
//						temp.put("firstInsureType", "旧车首次投保");//投保类型(非首次投保:0,新车首次投保:1,旧车首次投保:2)
//					}
//				}
//			}
//		}
		return temp;
	}

	/**
	 * 获取平台车型信息
	 */
	@Override
	public CarModel getPlatCarModelMessage(String instanceId) {
		// 获取平台车型信息
		CarModel carModel = new CarModel();
		LastYearPolicyInfoBean lastYearPolicyInfoBean = redisClient.get(Constants.TASK, instanceId,LastYearPolicyInfoBean.class);
//		if(null != lastYearPolicyInfoBean){
//			if(lastYearPolicyInfoBean.getCarModel()!=null){
//				carModel = lastYearPolicyInfoBean.getCarModel();
//			}
//		}
		return carModel;
	}
	
	/**
	 * 获取补充信息中车型下面部分信息
	 */
	@Override
	public Map<String, Object> getLocaldbReplenishInfo(String instanceId, String inscomcode, String deptCode) {
		// 通过缓存通过子任务号获取，下部分信息通过上部分信息查询结果手动录入存入缓存
		Map<String, Object> result = new HashMap<String, Object>();
		//缓存中的补充信息
		SupplementInfoVO supplementInfoVO = redisClient.get(SUPPLEMENT_INFO_VO, instanceId+"_"+inscomcode, SupplementInfoVO.class);
		if(supplementInfoVO!=null){
			//补充信息项详情json信息
			String supplementItem = appSupplementItemService.getSupplierSupplementItem(inscomcode,deptCode);
			JSONObject supplementItemJSONOBJ= JSONObject.fromObject(supplementItem);
			JSONArray resultVal = null;
			if(supplementItemJSONOBJ.getBoolean("success")){
				resultVal = supplementItemJSONOBJ.getJSONArray("resultVal");
			}
			if(resultVal!=null){
				//翻译补充信息字典
				result.put("firstInsureType", getSupplementItemValue("firstInsureType",supplementInfoVO.getFirstInsureType(),resultVal));//投保类型
				result.put("compulsoryClaimTimes", getSupplementItemValue("firstInsureType",supplementInfoVO.getCompulsoryClaimTimes(),resultVal));//上年交强理赔次数
				result.put("lastCommercialClaimSum", getSupplementItemValue("firstInsureType",supplementInfoVO.getLastCommercialClaimSum(),resultVal));//商业险理赔金额
				result.put("lastCommercialPremium", getSupplementItemValue("firstInsureType",supplementInfoVO.getLastCommercialPremium(),resultVal));//上年商业保险费
				result.put("commercialClaimRate", getSupplementItemValue("firstInsureType",supplementInfoVO.getCommercialClaimRate(),resultVal));//上年商业赔付率
				result.put("specialVehicleType", getSupplementItemValue("firstInsureType",supplementInfoVO.getSpecialVehicleType(),resultVal));//特种车类型
				result.put("commercialClaimTimes", getSupplementItemValue("firstInsureType",supplementInfoVO.getCommercialClaimTimes(),resultVal));//上年商业理赔次数
				result.put("registerArea", getSupplementItemValue("firstInsureType",supplementInfoVO.getRegisterArea(),resultVal));//车辆归属区域
				result.put("customDiscount", getSupplementItemValue("firstInsureType",supplementInfoVO.getCustomDiscount(),resultVal));//自定义折扣
				result.put("useMotorcadeMode", getSupplementItemValue("firstInsureType",supplementInfoVO.getUseMotorcadeMode(),resultVal));//使用车队优惠
				result.put("vehicularApplications", getSupplementItemValue("firstInsureType",supplementInfoVO.getVehicularApplications(),resultVal));//车辆用途
				result.put("fullLoadCalculationType", getSupplementItemValue("firstInsureType",supplementInfoVO.getFullLoadCalculationType(),resultVal));//自重计算标准
			}else{
				result = BeanUtils.toMap(supplementInfoVO);
			}
		}
		return result;
	}

	/**
	 * 翻译补充信息字典
	 */
	public String getSupplementItemValue(String paramName, Object paramValueObj, JSONArray resultVal) {
		String paramValue = paramValueObj!=null?paramValueObj.toString():"";
		JSONObject jsonOBJ = null;
		for (Object obj : resultVal) {
			jsonOBJ = JSONObject.fromObject(obj);
			if(paramName.equals(jsonOBJ.getString("enName"))){
				if(jsonOBJ.getString("valScope")!=null && !("".equals(jsonOBJ.getString("valScope")))){
					String[] selectItems = jsonOBJ.getString("valScope").split(",");
					for (int i = 0; i < selectItems.length; i++) {
						String[] selectItem = selectItems[i].split(":");
						if(selectItem[1].equals(paramValue)){
							return selectItem[0];
						}
					}
					if(paramValue==null && "".equals(paramValue)){
						return jsonOBJ.getString("defaultVal");
					}else{
						return paramValue;
					}
				}else{
					if(paramValue==null && "".equals(paramValue)){
						return jsonOBJ.getString("defaultVal");
					}else{
						return paramValue;
					}
				}
			}
		}
		return paramValue;
	}
	
	/**
	 * 修改补充信息
	 */
	@Override
	public String editLocaldbReplenishInfo(String instanceId, String inscomcode, INSBSupplement supplementInfoVO) {
		// 通过缓存通过子任务号获取，下部分信息通过上部分信息查询结果手动录入存入缓存
		if(redisClient.set(SUPPLEMENT_INFO_VO, instanceId+"_"+inscomcode, supplementInfoVO)){
			return "success";
		}else{
			return "fail";
		}
	}
	/**
	 * 修改补充信息
	 */
	@Override
	public String editLocaldbSupplementInfo(String inscoms, List<INSBSupplement> supplements){
		String[] inscomcodeList = inscoms.split(",");
		for(INSBSupplement model:supplements){
			for(String inscom:inscomcodeList){ 
				if(StringUtil.isNotEmpty(inscom)){
					model.setProviderid(inscom);
					insbSupplementService.updateBykeyidandproviderValue(model);
				}
			}
    		
    	}
		return "success";
	}
	/**
	 * 查询补充信息
	 */
	@Override
	public SupplementInfoVO getReplenishInfo(String instanceId, String inscomcode) {
		// 获取缓存里面的补充信息
		SupplementInfoVO supplementInfoVO = redisClient.get(SUPPLEMENT_INFO_VO, instanceId+"_"+inscomcode, SupplementInfoVO.class);
		return supplementInfoVO!=null?supplementInfoVO:new SupplementInfoVO();
	}
	
	/**
	 * 获取补充信息弹出框要显示的信息
	 */
	@Override
	public List<Map<String, Object>> getReplenishSelectItemsInfo(String instanceId, String inscomcode, String deptCode) {
		// 通过缓存通过子任务号获取，下部分信息通过上部分信息查询结果手动录入存入缓存
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		//缓存中的补充信息
		SupplementInfoVO supplementInfoVO = redisClient.get(SUPPLEMENT_INFO_VO, instanceId+"_"+inscomcode, SupplementInfoVO.class);
		//补充信息项详情json信息
		String supplementItem = appSupplementItemService.getSupplierSupplementItem(inscomcode, deptCode);
//		System.out.println(supplementItem);
		JSONObject supplementItemJSONOBJ= JSONObject.fromObject(supplementItem);
		JSONArray resultVal = null;
		if(supplementItemJSONOBJ.getBoolean("success")){
			resultVal = supplementItemJSONOBJ.getJSONArray("resultVal");
		}
		if(resultVal!=null){
			if(supplementInfoVO!=null){
				addReplenishItem("firstInsureType", supplementInfoVO.getFirstInsureType(), resultVal, result);//投保类型
				addReplenishItem("compulsoryClaimTimes", supplementInfoVO.getCompulsoryClaimTimes(), resultVal, result);//上年交强理赔次数
				addReplenishItem("lastCommercialClaimSum", supplementInfoVO.getLastCommercialClaimSum(), resultVal, result);//商业险理赔金额
				addReplenishItem("lastCommercialPremium", supplementInfoVO.getLastCommercialPremium(), resultVal, result);//上年商业保险费
				addReplenishItem("commercialClaimRate", supplementInfoVO.getCommercialClaimRate(), resultVal, result);//上年商业赔付率
				addReplenishItem("specialVehicleType", supplementInfoVO.getSpecialVehicleType(), resultVal, result);//特种车类型
				addReplenishItem("commercialClaimTimes", supplementInfoVO.getCommercialClaimTimes(), resultVal, result);//上年商业理赔次数
				addReplenishItem("registerArea", supplementInfoVO.getRegisterArea(), resultVal, result);//车辆归属区域
				addReplenishItem("customDiscount", supplementInfoVO.getCustomDiscount(), resultVal, result);//自定义折扣
				addReplenishItem("useMotorcadeMode", supplementInfoVO.getUseMotorcadeMode(), resultVal, result);//使用车队优惠
				addReplenishItem("vehicularApplications", supplementInfoVO.getVehicularApplications(), resultVal, result);//车辆用途
				addReplenishItem("fullLoadCalculationType", supplementInfoVO.getFullLoadCalculationType(), resultVal, result);//自重计算标准
			}else{//缓存中没有补充信息则以原始数据展示
				addReplenishItem("firstInsureType", null, resultVal, result);//投保类型
				addReplenishItem("compulsoryClaimTimes", null, resultVal, result);//上年交强理赔次数
				addReplenishItem("lastCommercialClaimSum", null, resultVal, result);//商业险理赔金额
				addReplenishItem("lastCommercialPremium", null, resultVal, result);//上年商业保险费
				addReplenishItem("commercialClaimRate", null, resultVal, result);//上年商业赔付率
				addReplenishItem("specialVehicleType", null, resultVal, result);//特种车类型
				addReplenishItem("commercialClaimTimes", null, resultVal, result);//上年商业理赔次数
				addReplenishItem("registerArea", null, resultVal, result);//车辆归属区域
				addReplenishItem("customDiscount", null, resultVal, result);//自定义折扣
				addReplenishItem("useMotorcadeMode", null, resultVal, result);//使用车队优惠
				addReplenishItem("vehicularApplications", null, resultVal, result);//车辆用途
				addReplenishItem("fullLoadCalculationType", null, resultVal, result);//自重计算标准
			}
		}
		return result;
	}
	
	/**
	 * 添加补充信息弹出窗口显示可修改项
	 */
	public void addReplenishItem(String paramName, Object paramValueObj, JSONArray resultVal, List<Map<String, Object>> result){
		String paramValue = paramValueObj!=null?paramValueObj.toString():"";
		JSONObject jsonOBJ = null;
		for (Object obj : resultVal) {
			jsonOBJ = JSONObject.fromObject(obj);
			if(paramName.equals(jsonOBJ.getString("enName"))){
				Map<String, Object> temp = new HashMap<String, Object>();
				temp.put("cnName", jsonOBJ.getString("cnName"));//中文名
				temp.put("enName", jsonOBJ.getString("enName"));//英文名
				if(jsonOBJ.getString("valScope")!=null && !("".equals(jsonOBJ.getString("valScope")))){
					String[] selectItems = jsonOBJ.getString("valScope").split(",");
					List<Map<String, Object>> selectList = new ArrayList<Map<String,Object>>();
					for (int i = 0; i < selectItems.length; i++) {
						String[] selectItemTemp = selectItems[i].split(":");
						Map<String, Object> selectItem = new HashMap<String, Object>();
						selectItem.put("key", selectItemTemp[0]);
						selectItem.put("value", selectItemTemp[1]);
						if(selectItemTemp[1].equals(paramValue)){
							selectItem.put("isSelected", "Y");
						}else{
							selectItem.put("isSelected", "N");
						}
						selectList.add(selectItem);
					}
					temp.put("valScope", selectList);//下拉选项
				}else{
					if("Double".equals(jsonOBJ.getString("type")) || "Integer".equals(jsonOBJ.getString("type"))){
						temp.put("defaultVal", "".equals(paramValue)?jsonOBJ.getString("defaultVal"):paramValue);//不是下拉选项则附有默认值
						temp.put("isCheck", "Y");
					}else if("Boolean".equals(jsonOBJ.getString("type"))){
						List<Map<String, Object>> selectListTemp = new ArrayList<Map<String,Object>>();
						Map<String, Object> selectItemTrue = new HashMap<String, Object>();
						selectItemTrue.put("value", true);
						selectItemTrue.put("key", "是");
						Map<String, Object> selectItemFalse = new HashMap<String, Object>();
						selectItemFalse.put("value", false);
						selectItemFalse.put("key", "否");
						selectListTemp.add(selectItemTrue);
						selectListTemp.add(selectItemFalse);
						temp.put("valScope", selectListTemp);//下拉选项
					}else{
						temp.put("defaultVal", "".equals(paramValue)?jsonOBJ.getString("defaultVal"):paramValue);//不是下拉选项则附有默认值
						temp.put("isCheck", "N");
					}
				}
				result.add(temp);//如果此保险公司支持此选项则为结果添加相应数据
			}
		}
	}
	
	/**
	 * 通知正在处理的子任务的业管，此任务已经被取消
	 * @param maininstanceId
	 * @param subinstanceId
	 * @param carinfo
	 */
	private void remindColseSubtask(String maininstanceId,String subinstanceId,INSBCarinfo carinfo){
		Map<String, String> param = new HashMap<String, String>();
		//通知正在处理的业管此任务或已被代理人关闭
		param.put("subTaskId", subinstanceId);
		INSBWorkflowsubtrackdetail lastmodel = insbWorkflowsubtrackdetailDao.selectLastModelBySubInstanceId(param);
		if(null!=lastmodel && "Proccessing".equals(lastmodel.getOperatorstate())){
			INSCUser user = inscUserService.getByUsercode(lastmodel.getOperator());
			taskthreadPool4workflow.execute(new Runnable() {
				@Override
				public void run() {
					// 得到系统用户发送消息
					INSCUser fromUser = inscUserService.getByUsercode("admin");
					try {
						LogUtil.info("**===任务关闭任务调度查找业管---向业管发送消息---maintaskid="+maininstanceId+"=taskCode="+lastmodel.getTaskcode());
						dispatchTaskService.sendXmppMessage4User(fromUser, user, "taskDel@"+"车牌："+(null==carinfo?"XXXX":carinfo.getCarlicenseno())+"，车主："+(null==carinfo?"XXX":carinfo.getOwnername())+" 的报价，该报价将自动关闭并移除");
						LogUtil.info("**===任务关闭发送xmpp消息成功,maintaskid="+maininstanceId+"=taskCode="+lastmodel.getTaskcode());
			
					} catch (Exception e) {
						LogUtil.error("***===任务关闭发送xmpp消息失败--向业管发送消息异常---maintaskid="+maininstanceId+"=taskCode="+lastmodel.getTaskcode());
						e.printStackTrace();
						//throw e;把异常丢给外层处理
					}
				}
			});
		}
	}
	/**
	 * 拒绝承保按钮调用接口前端调用wy
	 */
	@Override
	public String refuseUnderwrite2(String maininstanceId, String subinstanceId, String inscomcode, String mainorsub, String from) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//修改订单状态
		Map<String,Object> map = new HashMap<String,Object>();
		// 组织参数
		Map<String, String> params = new HashMap<String, String>();
		Map<String, Object> datas = new HashMap<String, Object>();
		//查询车信息和车主信息
		INSBCarinfo carinfo = insbCarinfoDao.selectCarinfoByTaskId(maininstanceId);
		if("main".equals(mainorsub)){
			//通知正在处理的业管此任务或已被代理人关闭
			INSBWorkflowmaintrackdetail lastmodel = insbWorkflowmaintrackdetailDao.copyTaskFeed4CompletedState(maininstanceId);
			LogUtil.info("通知正在处理的业管此任务或已被代理人关闭maintaskid="+maininstanceId+"=taskCode="+lastmodel.getTaskcode());
			if(null!=lastmodel && (TaskConst.QUOTING_1.equals(lastmodel.getTaskcode()) || TaskConst.QUOTING_2.equals(lastmodel.getTaskcode()))){
				List<String> subs = workflowsubService.queryInstanceIdsByMainInstanceId(maininstanceId);
				for(String instanceid:subs){
					//通知处理中的业管，此任务已经关闭
					remindColseSubtask(maininstanceId, instanceid, carinfo);
				}
			}else{
				if(null!=lastmodel && "Proccessing".equals(lastmodel.getOperatorstate())){
					INSCUser user = inscUserService.getByUsercode(lastmodel.getOperator());
					taskthreadPool4workflow.execute(new Runnable() {
						@Override
						public void run() {
							// 得到系统用户发送消息
							INSCUser fromUser = inscUserService.getByUsercode("admin");
							try {
								LogUtil.info("**===任务关闭业管---向打开任务业管发送消息---maintaskid="+maininstanceId+"=taskCode="+lastmodel.getTaskcode());
								dispatchTaskService.sendXmppMessage4User(fromUser, user,"taskDel@"+"车牌："+(null==carinfo?"XXXX":carinfo.getCarlicenseno())+"，车主："+(null==carinfo?"XXX":carinfo.getOwnername())+" 的报价，该报价将自动关闭并移除");
								LogUtil.info("**===任务关闭业管---向打开任务业管发送xmpp消息成功,maintaskid="+maininstanceId+"=taskCode="+lastmodel.getTaskcode());
					
							} catch (Exception e) {
								LogUtil.error("***===任务关闭发送xmpp消息失败--向业管发送消息异常---maintaskid="+maininstanceId+"=taskCode="+lastmodel.getTaskcode());
								e.printStackTrace();
								//throw e;把异常丢给外层处理
							}
						}
					});
				}
			}
			
			datas.put("processinstanceid", maininstanceId);//流程实例id
			//取消整个任务的订单状态
			INSBOrder temp = new INSBOrder();
			temp.setTaskid(maininstanceId);
			List<INSBOrder> templist = insbOrderDao.selectList(temp);
			if(templist!=null && !templist.isEmpty()){
				for(INSBOrder order : templist){
					order.setOrderstatus("5");
					insbOrderDao.updateById(order);
				}
//				insbOrderDao.updateInBatch(templist);
			}
		}else{
			//通知处理中的业管，此任务已经关闭
			remindColseSubtask(maininstanceId, subinstanceId, carinfo);
			datas.put("processinstanceid", subinstanceId);
			//取消子流程的订单状态
			map.put("taskid", maininstanceId);
			map.put("inscomcode", inscomcode);
			INSBOrder order = insbOrderDao.selectOrderByTaskId(map);
			if(order!=null){
				order.setOrderstatus("5");
				insbOrderDao.updateById(order);
			}
		}
		datas.put("process", mainorsub);//主或子流程
		datas.put("from", from);//前台拒绝承保
		JSONObject datasJSON = JSONObject.fromObject(datas);
		params.put("datas", datasJSON.toString());
		
		//调取工作流终止工作流程接口
		String result = HttpClientUtil.doGet(WORKFLOWURL + "/process/abortProcessById", params);
        LogUtil.info(maininstanceId + "," + subinstanceId + "," + inscomcode + "," + mainorsub + "," + from +
				"在refuseUnderwrite2中调用工作流abortProcessById结果：" + result);
        
        //通知调度删除任务
        taskthreadPool4workflow.execute(new Runnable() {
			@Override
			public void run() {
				Task task = new Task();
				//主流程任务
				if ("main".equals(mainorsub)) {
					task.setProInstanceId(maininstanceId);
				} else {//子流程任务
					task.setProInstanceId(maininstanceId);
					task.setSonProInstanceId(subinstanceId);
				}
				task.setDispatchUser(from);
				task.setPrvcode(inscomcode);
				LogUtil.info("maininstanceId=" + maininstanceId + "," + "subinstanceId=" + subinstanceId + "," + "operator=" + from +
						"在refuseUnderwrite2中通知调度删除任务");
				dispatchTaskService.deleteTask(task);

			}
		});
        resultMap.put("status", "success");
		resultMap.put("msg", "操作成功！");
		/*JSONObject jo = JSONObject.fromObject(result);
		if("success".equals(jo.getString("message"))){
			resultMap.put("status", "success");
			resultMap.put("msg", "操作成功！");
			//终止工作流程执行成功，数据库中同步处理数据
		}else if("fail".equals(jo.getString("message"))){
			resultMap.put("status", "fail");
			resultMap.put("msg", "工作流拒绝承保返回错误信息！");
		}else{
			resultMap.put("status", "fail");
			resultMap.put("msg", "工作流调用没有返回信息！");
		}*/
		return JSONObject.fromObject(resultMap).toString();
	}

	@Override
	public Map<String,String> endPayTask(String instanceId,String usercode) {
		LogUtil.info("关闭二次支付---start---instanceId="+instanceId);
		WorkflowFeedbackUtil.setWorkflowFeedback(null, instanceId, "21", "Completed", "二次支付确认", "二次支付失败", usercode);
		String result =WorkFlowUtil.abortProcessByIdWorkflow(instanceId, "sub", "front");

		JSONObject jo = JSONObject.fromObject(result);
		LogUtil.info(jo+"=result,关闭二次支付---end---instanceId="+instanceId);
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("status", "success");
		resultMap.put("msg", "操作成功！");
		/*if("success".equals(jo.getString("message"))){
			resultMap.put("status", "success");
			resultMap.put("msg", "操作成功！");
		}else if("fail".equals(jo.getString("message"))){
			throw new RuntimeException("二次支付失败推工作流失败！");
		}else{
			resultMap.put("status", "fail");
			resultMap.put("msg", "工作流调用没有返回信息！");
		}*/
		return  resultMap;
	}
}