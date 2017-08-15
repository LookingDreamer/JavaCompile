package com.zzb.chn.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import com.cninsure.core.utils.LogUtil;
import com.common.redis.CMRedisClient;

/**
 * 工作池 定时器执行回收
 *
 * @author hxx
 */
@Service
public class DelRedisJob implements Job {
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            String module = dataMap.getString("module");
            String key = dataMap.getString("key");
            LogUtil.info("删除redis数据,key=" + module + ":" + key);
            CMRedisClient.getInstance().del(module, key);
    }

}
