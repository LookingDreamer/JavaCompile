package com.zzb.chn.job;

import com.cninsure.core.utils.LogUtil;
import com.common.HttpClientUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

/**
 * 工作池 定时器执行回收
 *
 * @author hxx
 */
@Service
public class NotiJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
            int num = 0;
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            String url = dataMap.getString("url");
            String json = dataMap.getString("json");
            callNum(url, json,num);
    }

    public void callNum(String url, String json, int num) {
        try {
            if (5 > num) {
                if (num != 0) { 
                	Thread.sleep(5 * 1000);
                }
                String result = HttpClientUtil.doPostJsonString(url, json);
                num += 1;
                if (result == null) {
                    LogUtil.info("回调 " + num + " 次都失败了,你看着办吧:url=" + url + " json=" + json);
                    callNum(url, json, num);
                }else {

					LogUtil.info("回调 结果" +result+" 第 "+ num + " 次 url="+url+" json=" + json);
				}
            } else if(num==5){
                num += 1;
                Thread.sleep(60 * 1000);
                String result = HttpClientUtil.doPostJsonString(url, json);
                LogUtil.info("回调 " + num + " 次:url=" + url + " json=" + json +"结果 "+result);
            }
        } catch (Exception e) {
            LogUtil.error("回调异常:" + e.getMessage());
            e.printStackTrace();
        }

    }
}
