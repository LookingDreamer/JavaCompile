package com.zzb.cm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.JsonUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCDeptService;
import com.common.HttpClientUtil;
import com.common.PagingParams;
import com.common.redis.IRedisClient;
import com.zzb.chn.util.JsonUtils;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBCarinfoService;
import com.zzb.cm.service.INSBCarkindpriceService;
import com.zzb.cm.service.INSBCarmodelinfoService;
import com.zzb.cm.service.INSBInsuresupplyparamService;
import com.zzb.cm.service.INSBOrderService;
import com.zzb.cm.service.INSBPersonService;
import com.zzb.conf.dao.INSBChannelDao;
import com.zzb.conf.entity.INSBChannel;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBOperatorcommentService;
import com.zzb.conf.service.INSBOrderpaymentService;
import com.zzb.conf.service.INSBPaychannelService;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.conf.service.INSBWorkflowsubtrackService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.util.AmountUtil;
import com.zzb.mobile.util.HttpClient;
import com.zzb.mobile.util.MappingType;
import com.zzb.mobile.util.PayConfigMappingMgr;

import net.sf.json.JSONObject;

/**
 * 车险任务管理
 */
@Controller
@RequestMapping("/business/refundtaskmanage/*")
public class INSBBusinessRefundTaskManageController extends BaseController {
    @Resource
	private INSBCarinfoService insbCarinfoService; 
	@Resource
	private INSBAgentService insbAgentService;
	@Resource
	private INSBPersonService insbPersonService;
	@Resource
	private INSBCarkindpriceService insbCarkindpriceService;
	@Resource
	private INSBWorkflowsubtrackService insbWorkflowsubtrackService;
	@Resource
	private INSBUsercommentService insbUsercommentService;
	@Resource
	private INSBWorkflowsubService insbWorkflowsubService;
	@Resource
	private INSCDeptService inscDeptService;
	@Resource 
	private INSBCarmodelinfoService insbCarmodelinfoService;
	@Resource
	private INSBOperatorcommentService insbOperatorcommentService;
	@Resource
	private INSBOrderpaymentService insbOrderpaymentService;
	@Resource
	private INSBPaychannelService insbPaychannelService;
	@Resource
	private INSBOrderService insbOrderService;
	@Resource
	private IRedisClient redisClient;
	@Resource
	private INSBInsuresupplyparamService insbInsuresupplyparamService;
	@Resource
    private ThreadPoolTaskExecutor taskthreadPool4workflow;
	@Resource
    private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBChannelDao insbChannelDao;
	
	//跳转退款任务管理页面
	@RequestMapping(value="refundtaskmanagelist", method=RequestMethod.GET)
	public ModelAndView toCarTaskManageListPage(HttpSession session){
		ModelAndView mav = new ModelAndView("cm/cartaskmanage/refundTaskManage");
		return mav;
	}
	
//	/**
//	 * 生成机构树调用方法
//	 */
//	@RequestMapping(value="querydepttree",method=RequestMethod.POST)
//	@ResponseBody
//	public List<Map<String, Object>> queryProList(@RequestParam(value="id",required=false) String parentcode) throws ControllerException{
//		LogUtil.debug("parentcode=", parentcode);
//		return inscDeptService.selectDeptTreeByParentCode(parentcode);
//	}
//
//	@RequestMapping(value="queryparttree",method=RequestMethod.POST)
//	@ResponseBody
//	public List<Map<String, Object>> queryPartList(@RequestParam(value="id",required=false) String parentcode,HttpSession session) throws ControllerException{
//		LogUtil.debug("parentcode=", parentcode);
//		INSCUser loginUser=(INSCUser)session.getAttribute("insc_user");
//		String userorganization=loginUser.getUserorganization();
//		Map<String,String> params=new HashMap<String,String>();
//		params.put("parentcode", parentcode);
//		params.put("userorganization", userorganization);
//		List<Map<String,Object>> list=inscDeptService.selectPartTreeByParentCode(params);
//		return list;
//	}

//	@RequestMapping(value="queryparttreecheckall",method=RequestMethod.POST)
//	@ResponseBody
//	public List<Map<String, Object>> queryparttreecheckall(@RequestParam(value="id",required=false) String parentcode,HttpSession session) throws ControllerException{
//		LogUtil.debug("parentcode=", parentcode);
//		INSCUser loginUser=(INSCUser)session.getAttribute("insc_user");
//		String userorganization=loginUser.getUserorganization();
//		Map<String,String> params=new HashMap<String,String>();
//		params.put("parentcode", parentcode);
//		params.put("userorganization", userorganization);
//		List<Map<String,Object>> list=inscDeptService.selectPartTreeByParentCodeCheckAll(params);
//		return list;
//	}
	
	//查询退款任务数据
	@RequestMapping(value = "showrefundtaskmanagelist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> showCarTaskManageList(HttpSession session, @ModelAttribute PagingParams pagingParams,
													 String carlicenseno, String taskid, String paymentransaction,
													 String channelinnercode, String deptcode, String refundstatus,
													 String payamount, String refundamount, String refundtype,
													 String taskcreatetimeup, String taskcreatetimedown) throws ControllerException{
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		INSCDept d = inscDeptService.getOrgDeptByDeptCode(loginUser.getUserorganization());
	
		//组织查询参数
		Map<String, Object> paramMap = BeanUtils.toMap(pagingParams);
		paramMap.put("carlicenseno", carlicenseno);//车牌号
		paramMap.put("taskid", taskid);
		paramMap.put("paymentransaction", paymentransaction);

		paramMap.put("userorganization", d.getDeptinnercode());//树形结构code
		paramMap.put("deptcode", deptcode);//出单网点机构id
		paramMap.put("channelinnercode", channelinnercode);
		paramMap.put("refundstatus", refundstatus);

		paramMap.put("payamount", payamount);
		paramMap.put("refundamount", refundamount);
		paramMap.put("refundtype", refundtype);

		paramMap.put("taskcreatetimeup", taskcreatetimeup);//任务创建时间上限
		paramMap.put("taskcreatetimedown", taskcreatetimedown);//任务创建时间下限

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total",insbOrderpaymentService.refundCount(paramMap));
		map.put("rows", insbOrderpaymentService.refund(paramMap));
	    return map;
	}

	/**
	 *
	 * @param session
	 * @param taskid 任务号
	 * @param tl "tl" : 通联支付
	 * @return
	 * @throws ControllerException
     */
	@RequestMapping(value = "refund", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> refund(HttpSession session, String taskid, String tl) throws ControllerException{
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		Map<String, Object> map = new HashMap<String, Object>();
		INSBOrderpayment orderpayment = new INSBOrderpayment();
		orderpayment.setTaskid(taskid);
		orderpayment.setOperator(operator == null ? "admin" : operator.getUsercode());
		orderpayment.setRefundstatus(1);
		orderpayment.setNoti("退款任务完成，operator " + operator.getUsercode() + " time " + DateUtil.getCurrentDateTime());
		if (StringUtil.isNotEmpty(tl) && ("tl" .equals(tl))) {
			/*{
					"bizId": "订单号",
					"bizTransactionId": "支付流水号",
					"channelId": "渠道接口",
					"payType": "支付方式",
					"Amount": "这单的金额"
					"refundAmount": "退款金额"
				}
			 */
			try{
				Map<String, Object> refundtask = insbOrderpaymentService.refundtask(taskid);
				JSONObject json = new JSONObject();
				json.put("bizId", refundtask.get("paymentransaction"));
				json.put("bizTransactionId", refundtask.get("payflowno"));
				json.put("channelId", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_CHANNEL, String.valueOf(refundtask.get("paychannelid"))));
				json.put("payType", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_TYPE, String.valueOf(refundtask.get("paytype"))));
				json.put("Amount", AmountUtil.trans2Fen(String.valueOf(refundtask.get("payamount"))));
				json.put("refundAmount", AmountUtil.trans2Fen(String.valueOf(refundtask.get("refundamount"))));

				LogUtil.info("refund支付平台请求:" + JsonUtil.getJsonString(json));
				String jsonStr = HttpClient.sendPostJson(null, PayConfigMappingMgr.getRefundmentUrl(), json);
				LogUtil.info("refund支付平台响应结果:" + jsonStr);
				json = JSONObject.fromObject(jsonStr);
				if (CommonModel.STATUS_SUCCESS.equals(json.getString("code"))) {
					insbOrderpaymentService.updateRefundstatus(orderpayment);
					map.put("success", true);
					map.put("msg", "该退款任务已完成!");
					sendMsgToChn(taskid);
				} else {
					map.put("success", false);
					map.put("msg", "退款失败，失败原因：" + json.get("msg"));
				}

			}catch(Exception e){
				LogUtil.error("refund支付平台异常" + e);
				e.printStackTrace();
				map.put("success", false);
				map.put("msg","退款失败，失败原因：支付平台异常");
			}
		} else {
			insbOrderpaymentService.updateRefundstatus(orderpayment);
			map.put("success", true);
			map.put("msg", "该退款任务已完成!");
			sendMsgToChn(taskid);
		}

		return map;
	}

	//通知渠道
	private void sendMsgToChn(String taskId) {
		taskthreadPool4workflow.execute(new Runnable() {
            @Override
            public void run() {
            	INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
                insbQuotetotalinfo.setTaskid(taskId);
            	insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
            	
            	String channelinnercode = insbQuotetotalinfo.getPurchaserChannel();
            	LogUtil.info(taskId + "refund-sendMsgToChn-channelinnercode:" + channelinnercode);
            	if ( StringUtil.isEmpty(channelinnercode) ) {
            		return;
            	}
            	
            	INSBChannel insbChannel = new INSBChannel();
                insbChannel.setChannelinnercode(channelinnercode);
                insbChannel.setChildflag("1");
                List<INSBChannel> insbChannels = insbChannelDao.selectList(insbChannel);
                
                String url = "";
                for (INSBChannel insbChannelIt : insbChannels) {
                	if ( !"0".equals(insbChannelIt.getDeleteflag()) ) {
                		url = insbChannelIt.getWebaddress();
                		break;
                	}
                }
                
                LogUtil.info(taskId + "refund-sendMsgToChn-url:" + url);
                if ( StringUtil.isEmpty(url) ) {
                	LogUtil.info(taskId + "refund-sendMsgToChn:url为空");
            		return;
            	}
            	
            	Map<String, String> msg = new HashMap<String, String>();
            	msg.put("taskId", taskId);
            	msg.put("code", "0");
            	msg.put("taskState", "24");
            	msg.put("taskStateDescription", "退款成功");
            	
            	String jsonStr = JsonUtils.serialize(msg);
            	for(int i = 0; i < 5; i++) {
            		LogUtil.info(i + "-" + taskId + "refund-sendMsgToChn:" + jsonStr);
            		String result = HttpClientUtil.doPostJsonString(url, jsonStr);
            		LogUtil.info(i + "-" + taskId + "refund-sendMsgToChn-result:" + result);
            		if (result == null) {
            			if (i != 4) {
	            			try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
            			}
            		} else {
            			break;
            		}
            	}
				
            }
		});
	}
	
}
