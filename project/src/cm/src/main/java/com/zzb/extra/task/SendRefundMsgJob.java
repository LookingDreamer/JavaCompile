package com.zzb.extra.task;

import com.alibaba.fastjson.JSONObject;
import com.zzb.extra.service.INSBAccountDetailsService;
import com.zzb.extra.service.WxMsgTemplateService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;
import com.cninsure.core.utils.LogUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Heweicheng on 2016/8/4.
 */
@Service
public class SendRefundMsgJob implements Job {

    @Resource
    private WxMsgTemplateService wxMsgTemplateService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String taskid = dataMap.getString("taskid");
        String providercode = dataMap.getString("providercode");
        String agentid = dataMap.getString("agentid");
        String amount = dataMap.getString("amount");
        String result = wxMsgTemplateService.sendRefundMsg(taskid,providercode,agentid,amount);
        LogUtil.info("SendRefundMsgJob,dataMap=" + JSONObject.toJSONString(dataMap) +" :result="+result);

    }
}
