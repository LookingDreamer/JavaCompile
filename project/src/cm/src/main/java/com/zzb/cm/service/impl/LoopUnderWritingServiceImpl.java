package com.zzb.cm.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.timer.SchedulerService;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.cm.service.LoopUnderWritingService;
import com.zzb.conf.service.INSBWorkflowmaintrackService;

import net.sf.json.JSONObject;

/**
 * 核保回写轮询job
 * liuchao
 */
@Service
@Transactional
public class LoopUnderWritingServiceImpl implements LoopUnderWritingService{

	/*public static String LOCALSYSTEMURL = "";
	
	static{
		ResourceBundle resourceBundle = ResourceBundle.getBundle("config/underwritingloop");
		LOCALSYSTEMURL = resourceBundle.getString("gotofairyorediquote.url");
	}*/
	@Resource
	private INSBWorkflowmaintrackService insbWorkflowmaintrackService;
	@Resource 
	private InterFaceService interFaceService;
	@Resource
	private SchedulerService schedule;
	
	
	@Override
	public void executeService(Map<String, Object> contextParam) throws Exception {
		String maininstanceId = String.valueOf(contextParam.get("maininstanceId"));
		String subInstanceId = String.valueOf(contextParam.get("subInstanceId"));
		String inscomcode = String.valueOf(contextParam.get("inscomcode"));
        String loopid = String.valueOf(contextParam.get("loopid"));
		//获得按创建时间倒序排序的核保节点轨迹列表，下标为0的是最后核保途径
		@SuppressWarnings("unchecked")
		List<String> underwritingList = (List<String>)contextParam.get("underwritingList");

		LogUtil.info("======核保轮询任务开始执行！======maininstanceId=%s,subInstanceId=%s,inscomcode=%s,underwritingList=%s,loopid=%s", maininstanceId, subInstanceId, inscomcode, underwritingList.toString(), loopid);

		//没有查询到核保途径（通常不会出现这种情况）
		if (underwritingList == null || underwritingList.isEmpty()) {
			LogUtil.info("======没有查询到精灵或EDI核保轨迹，发起核保轮询任务失败！======maininstanceId=%s,subInstanceId=%s,inscomcode=%s", maininstanceId, subInstanceId, inscomcode);
			return;
		}
		String nextloop = String.valueOf(contextParam.get("nextloop"));
		String taskName = "核保轮询";
		StringBuffer jobName = new StringBuffer(subInstanceId);
		jobName.append('_');
		jobName.append(taskName);
		jobName.append('_');
		jobName.append(inscomcode);
		jobName.append('_');
		jobName.append(loopid);
		jobName.append('_');
		jobName.append(nextloop);
		String value = maininstanceId + "_" + inscomcode;
		Date now = new Date();
		long timeout = 15000;//默认为十五秒之后执行定时任务
		//检查此次轮询是倒数第几次轮询,总共执行三次轮询，nextloop为3则下次轮询为倒数第二次，为2则下次为倒数最后一次
		if("3".equals(nextloop)){
			timeout = 120000; //倒数第三次执行轮询时间2分钟之后
		}else if("2".equals(nextloop)){
			timeout = 180000; //倒数第二次执行轮询时间3分钟之后
		}else if("1".equals(nextloop)){
			timeout = 300000; //倒数第一次执行轮询时间5分钟之后
		}else if(null == nextloop || "0".equals(nextloop)){//直接结束
		}else{//其他情况，轮询次数超过3次的情况
			timeout = 60000; //轮询时间1分钟之后
		}
		Date executeTime = new Date(now.getTime() + timeout); //执行时间
		schedule.dealLoopOverTime(jobName.toString(), taskName, executeTime, value, String.valueOf(timeout), null, 0);
		
		String RequestMap = null;
		JSONObject reObj = null;
		boolean detectLastOne = false;

		try {
			logLoopunderwritingdetail(loopid, "start", "(无)", "{n}");
			String loopQueryType = null;
			String queryTypeDesc = null;
			if("16".equals(underwritingList.get(0)) || "40".equals(underwritingList.get(0))){//edi核保16
				loopQueryType = "EDI";
				queryTypeDesc = "EDI";
			}else if("17".equals(underwritingList.get(0)) || "41".equals(underwritingList.get(0))){
				loopQueryType = "fairy";
				queryTypeDesc = "精灵";
			}else{
				LogUtil.info("taskid=%s,subtaskid=%s,providerid=%s,当前任务状态:%s", maininstanceId, subInstanceId, inscomcode, underwritingList.get(0));
				return ;
			}
				
			RequestMap = goToFairyOrEdiQuote(maininstanceId, inscomcode, "admin", "insurequery", loopQueryType);
			LogUtil.info("message:"+RequestMap);

			reObj = JSONObject.fromObject(RequestMap);
			if(reObj.getBoolean("result")){
				LogUtil.info("taskid=%s,subtaskid=%s,providerid=%s,已成功发起%s核保轮询任务" , maininstanceId, subInstanceId, inscomcode, queryTypeDesc);
			}else{
				detectLastOne = true;
				updateLoopunderwritingdetail(maininstanceId, subInstanceId, inscomcode, "fail", "发起" + queryTypeDesc + "核保轮询失败");
			}
		} catch (Exception e) {
			LogUtil.info("taskid=%s,subtaskid=%s,providerid=%s,发起核保轮询任务出现服务异常");
			e.printStackTrace();
			detectLastOne = true;
			updateLoopunderwritingdetail(maininstanceId, subInstanceId, inscomcode, "fail", "发起核保轮询任务出现服务异常");
		}
		
		LogUtil.info("taskid=%s,subtaskid=%s,providerid=%s,nextloop=%s ,检查是否最后一个轮询任务", maininstanceId, subInstanceId, inscomcode, nextloop);
		if (detectLastOne) {
			//轮询次数用完
			if (null == nextloop) {
				//更新轮询结果
				updateLoopunderwritingdetail(maininstanceId, subInstanceId, inscomcode, "stop", "");
				// 工作流推送
				loopWorkFlowToNext(maininstanceId, subInstanceId, inscomcode, "1", null);
			}
		}
		
	}

	private String loopWorkFlowToNext(String maininstanceId, String subInstanceId, String inscomcode, String result, String msg) {
		if (StringUtil.isEmpty(maininstanceId) || StringUtil.isEmpty(inscomcode)) {
			LogUtil.error("maininstanceId and inscomcode must not be null");
			return null;
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("mainInstanceId", maininstanceId);
		params.put("subInstanceId", subInstanceId);
		params.put("inscomcode", inscomcode);
		params.put("loopStatus", result);
		params.put("msg", msg);

		LogUtil.info("调用轮询状态工作流推送接口:mainInstanceId=%s ,subInstanceId=%s ,result=%s", maininstanceId, subInstanceId, result);
		Map<String, Object> temp = new HashMap<String, Object>();

		try {
			//String message = HttpClientUtil.doPostJson(LOCALSYSTEMURL + "/business/ordermanage/loopWorkFlowToNext", params);
			Map<String, Object> message = insbWorkflowmaintrackService.loopWorkFlowToNext(maininstanceId, subInstanceId, inscomcode, result, msg);
			LogUtil.info("返回信息:%s", message.toString());

			if (StringUtil.isNotEmpty(message.toString())) {
				JSONObject jo = JSONObject.fromObject(message);
				temp.put("msg", jo.getString("msg"));

				if ("success".equals(jo.getString("status"))) {
					temp.put("result", true);
				} else {
					temp.put("result", false);
				}
			} else {
				temp.put("result", false);
				temp.put("msg", "接口异常无数据返回");
			}
		} catch (Exception e) {
			temp.put("result", false);
			temp.put("msg", "请求接口异常无数据返回");
		}

		return JSONObject.fromObject(temp).toString();
	}

	private String updateLoopunderwritingdetail(String maininstanceId, String subInstanceId, String inscomcode, String loopResult, String msg) {
		if (StringUtil.isEmpty(maininstanceId) || StringUtil.isEmpty(inscomcode)) {
			LogUtil.error("maininstanceId and inscomcode must not be null");
			return null;
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("mainInstanceId", maininstanceId);
		params.put("subInstanceId", subInstanceId);
		params.put("inscomcode", inscomcode);
		params.put("loopStatus", loopResult);
		params.put("msg", msg);

		LogUtil.info("调用更新轮询结果详情接口:mainInstanceId="+maininstanceId+",subInstanceId="+subInstanceId+",loopStatus="+loopResult);
		Map<String, Object> temp = new HashMap<String, Object>();

		try {
			//String message = HttpClientUtil.doPostJson(LOCALSYSTEMURL + "/business/ordermanage/updateLoopunderwritingdetail", params);
			Map<String, Object> message = insbWorkflowmaintrackService.updateLoopUnderWritingDetail(maininstanceId, subInstanceId, inscomcode, loopResult, msg);
			LogUtil.info("返回信息:%s", message.toString());

			if (StringUtil.isNotEmpty(message.toString())) {
				JSONObject jo = JSONObject.fromObject(message);
				temp.put("msg", jo.getString("msg"));

				if ("success".equals(jo.getString("status"))) {
					temp.put("result", true);
				} else {
					temp.put("result", false);
				}
			} else {
				temp.put("result", false);
				temp.put("msg", "接口异常无数据返回");
			}
		} catch (Exception e) {
			temp.put("result", false);
			temp.put("msg", "请求接口异常无数据返回");
		}

		return JSONObject.fromObject(temp).toString();
	}

	private String logLoopunderwritingdetail(String loopId, String loopResult, String msg, String times) {
	    if (StringUtil.isEmpty(loopId)) {
	        LogUtil.error("loopid must not be null");
            return null;
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("mainInstanceId", loopId);
        params.put("subInstanceId", DateUtil.getCurrentDateTime());
		params.put("loopStatus", loopResult);
        params.put("msg", "第"+times+"次轮询#"+msg);

        LogUtil.info("调用记录轮询结果详情接口:loopId="+loopId+",loopResult="+loopResult+",times="+times);
		Map<String, Object> temp = new HashMap<String, Object>();

		try {
			//String message = HttpClientUtil.doPostJson(LOCALSYSTEMURL+"/business/ordermanage/logLoopunderwritingdetail", params);
			Map<String, Object> message = insbWorkflowmaintrackService.logLoopUnderWritingDetail(loopId, DateUtil.getCurrentDateTime(), loopResult, times+"#"+msg);
			LogUtil.info("返回信息:%s", message.toString());

			if (StringUtil.isNotEmpty(message.toString())) {
				JSONObject jo = JSONObject.fromObject(message);
				temp.put("msg", jo.getString("msg"));

				if ("success".equals(jo.getString("status"))) {
					temp.put("result", true);
				} else {
					temp.put("result", false);
				}
			} else {
				temp.put("result", false);
				temp.put("msg", "接口异常无数据返回");
			}
		} catch (Exception e) {
			temp.put("result", false);
			temp.put("msg", "请求接口异常无数据返回");
		}

		return JSONObject.fromObject(temp).toString();
    }

	/**
	 * spring不能成功注入解决方案为：使用http请求调用精灵或edi核保回写
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public String goToFairyOrEdiQuote(String maininstanceId, String inscomcode, String userCode, String backFlag, String fOrEDIFlag) throws IOException{
		Map<String, String> params = new HashMap<String, String>();
		params.put("maininstanceId", maininstanceId);
		params.put("inscomcode", inscomcode);
		params.put("userCode", userCode);
		params.put("backFlag", backFlag);
		params.put("forediflag", fOrEDIFlag);

		LogUtil.info("开始调用核保查询能力接口:maininstanceId="+maininstanceId+",inscomcode="+inscomcode+
				",userCode="+userCode+",backFlag="+backFlag+",fOrEDIFlag="+fOrEDIFlag);
		//String message = HttpClientUtil.doGet(LOCALSYSTEMURL+"/business/ordermanage/goToFairyOrEdiQuote", params);
		Map<String, Object> result = new HashMap<String, Object>();
		String message = "";
		try {
			if("EDI".equals(fOrEDIFlag)){
				message = interFaceService.goToEdiQuote(maininstanceId, inscomcode, userCode, backFlag);
				result.put("status", "success");
				result.put("msg", message);
			}else if("fairy".equals(fOrEDIFlag)){
				message = interFaceService.goToFairyQuote(maininstanceId, inscomcode, userCode, backFlag);
				result.put("status", "success");
				result.put("msg", message);
			}else{
				result.put("status", "fail");
				result.put("msg", "EDI或fairy标记不匹配！");
			}
		} catch (Exception e) {
			result.put("status", "fail");
			result.put("msg", "服务器内部异常！");
			e.printStackTrace();
		}
		LogUtil.info("返回信息:%s", message.toString());

		Map<String, Object> temp = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(result.toString())) {
			JSONObject jo = JSONObject.fromObject(result);
			temp.put("msg", jo.getString("msg"));

			if ("success".equals(jo.getString("status"))) {
				temp.put("result", true);
			} else {
				temp.put("result", false);
			}
		} else {
			temp.put("result", false);
			temp.put("msg", "接口异常无数据返回");
		}

		return JSONObject.fromObject(temp).toString();
	}
}
