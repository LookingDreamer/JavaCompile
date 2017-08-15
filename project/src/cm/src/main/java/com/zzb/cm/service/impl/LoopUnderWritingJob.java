package com.zzb.cm.service.impl;

import java.io.IOException;
import java.util.*;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.StringUtil;
import com.zzb.conf.service.impl.INSBWorkflowmaintrackServiceImpl;
import net.sf.json.JSONObject;

import org.apache.http.client.ClientProtocolException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import com.cninsure.core.utils.LogUtil;
import com.common.HttpClientUtil;

/**
 * 核保回写轮询job
 * liuchao
 */
@Service
public class LoopUnderWritingJob implements Job{

	public static String LOCALSYSTEMURL = "";
	
	static{
		ResourceBundle resourceBundle = ResourceBundle.getBundle("config/underwritingloop");
		LOCALSYSTEMURL = resourceBundle.getString("gotofairyorediquote.url");
	}
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String maininstanceId = context.getJobDetail().getJobDataMap().getString("maininstanceId");
		String subInstanceId = context.getJobDetail().getJobDataMap().getString("subInstanceId");
		String inscomcode = context.getJobDetail().getJobDataMap().getString("inscomcode");
        String loopid = context.getJobDetail().getJobDataMap().getString("loopid");
		//获得按创建时间倒序排序的核保节点轨迹列表，下标为0的是最后核保途径
		@SuppressWarnings("unchecked")
		List<String> underwritingList = (List<String>)context.getJobDetail().getJobDataMap().get("underwritingList");

		LogUtil.info("======核保轮询任务开始执行！======maininstanceId="+maininstanceId+",subInstanceId="+subInstanceId
				+",inscomcode="+inscomcode+",underwritingList="+underwritingList.toString()+",loopid="+loopid);

		//没有查询到核保途径（通常不会出现这种情况）
		if (underwritingList == null || underwritingList.isEmpty()) {
			LogUtil.info("======没有查询到精灵或EDI核保轨迹，发起核保轮询任务失败！======maininstanceId="+maininstanceId+",subInstanceId="+subInstanceId
					+",inscomcode="+inscomcode);
			return;
		}

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
				LogUtil.info("taskid="+maininstanceId+",subtaskid="+subInstanceId +",providerid="+inscomcode + ",当前任务状态:" + underwritingList.get(0));
				return ;
			}
				
			RequestMap = goToFairyOrEdiQuote(maininstanceId, inscomcode, "admin", "insurequery", loopQueryType);
			LogUtil.info("message:"+RequestMap);

			reObj = JSONObject.fromObject(RequestMap);
			if(reObj.getBoolean("result")){
				LogUtil.info("taskid="+maininstanceId+",subtaskid="+subInstanceId +",providerid="+inscomcode + ",已成功发起" + queryTypeDesc + "核保轮询任务");
			}else{
				detectLastOne = true;
				updateLoopunderwritingdetail(maininstanceId, subInstanceId, inscomcode, "fail", "发起" + queryTypeDesc + "核保轮询失败");
			}
		} catch (Exception e) {
			LogUtil.info("taskid="+maininstanceId+",subtaskid="+subInstanceId +",providerid="+inscomcode + ",发起核保轮询任务出现服务异常");
			e.printStackTrace();
			detectLastOne = true;
			updateLoopunderwritingdetail(maininstanceId, subInstanceId, inscomcode, "fail", "发起核保轮询任务出现服务异常");
		}

		if (detectLastOne) {
			LogUtil.info("taskid=" + maininstanceId + ",subtaskid=" + subInstanceId + ",providerid=" + inscomcode + ",nextFireTime="
					+ context.getNextFireTime() + "," + context.getTrigger().getNextFireTime() + ",检查是否最后一个轮询任务");
			//轮询次数用完
			if (context.getNextFireTime() == null) {
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

		LogUtil.info("调用轮询状态工作流推送接口:mainInstanceId="+maininstanceId+",subInstanceId="+subInstanceId+",result="+result);
		Map<String, Object> temp = new HashMap<String, Object>();

		try {
			String message = HttpClientUtil.doPostJson(LOCALSYSTEMURL + "/business/ordermanage/loopWorkFlowToNext", params);
			LogUtil.info("返回信息:" + message);

			if (StringUtil.isNotEmpty(message)) {
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
			String message = HttpClientUtil.doPostJson(LOCALSYSTEMURL + "/business/ordermanage/updateLoopunderwritingdetail", params);
			LogUtil.info("返回信息:" + message);

			if (StringUtil.isNotEmpty(message)) {
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
			String message = HttpClientUtil.doPostJson(LOCALSYSTEMURL+"/business/ordermanage/logLoopunderwritingdetail", params);
			LogUtil.info("返回信息:"+message);

			if (StringUtil.isNotEmpty(message)) {
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
		String message = HttpClientUtil.doGet(LOCALSYSTEMURL+"/business/ordermanage/goToFairyOrEdiQuote", params);
		LogUtil.info("返回信息:"+message);

		Map<String, Object> temp = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(message)) {
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

		return JSONObject.fromObject(temp).toString();
	}
}
