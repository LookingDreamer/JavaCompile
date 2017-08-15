package com.zzb.extra.task;

import com.alibaba.fastjson.JSONObject;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.common.HttpSender;
import com.zzb.chn.util.JsonUtils;
import com.zzb.extra.service.INSBMiniOrderTraceService;
import com.zzb.extra.service.WxMsgTemplateService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Heweicheng on 2016/10/21.
 */
@Service
public class SendRefundCompleteJob implements Job {

    @Resource
    private INSCCodeService inscCodeService;
    @Resource
    private INSBMiniOrderTraceService insbMiniOrderTraceService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            Map<String, String> httpHead = new HashMap<>();
            httpHead.put("innerPipe", "zheshiyigemimi!");
            httpHead.put("Content-Type", "application/json;charset=utf-8");
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            LogUtil.info("RefundCompleteJob dataMap execute=" + JSONObject.toJSONString(dataMap));
            String taskid = dataMap.getString("taskid");
            String providercode = dataMap.getString("providercode");
            String channelId = dataMap.getString("channelId");
            INSCCode inscCode = new INSCCode();
            inscCode.setParentcode("ChannelForMini");
            inscCode.setCodetype("channelurl");
            inscCode.setCodevalue("01");
            inscCode = inscCodeService.queryOne(inscCode);
            String url = "";
            String signUrl = "";
            if (null != inscCode) {
                url = inscCode.getProp2() + "/channel/refundComplete";
                signUrl = inscCode.getProp2() + "/channel/getSignForInner";
            }else {
                LogUtil.info("渠道地址未配置");
                return;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("taskId", taskid);
            map.put("prvId", providercode);
            String json = JsonUtils.serialize(map);
            httpHead.put("channelId",channelId);
            String sign = HttpSender.doPost(signUrl, json, httpHead, "UTF-8");
            httpHead.put("sign",sign);
            //System.out.println("json="+json+"  sign="+sign+" url="+url+" signUrl="+signUrl);
            String res  = HttpSender.doPost(url, json, httpHead, "UTF-8");
            //System.out.println("RefundCompleteJob,dataMap execute=" + JSONObject.toJSONString(dataMap) + " :result=" + res);
            LogUtil.info("RefundCompleteJob dataMap execute=" + JSONObject.toJSONString(dataMap) + " :result=" + res);
            //更新跟单状态
            insbMiniOrderTraceService.updateOrderTraceState(taskid,providercode,"13");
        }catch (Exception e){
            LogUtil.info("RefundCompleteJob 失败 dataMap=" + JSONObject.toJSONString(context.getJobDetail().getJobDataMap()) + " :result=" + e.getMessage());
        }

    }
}
