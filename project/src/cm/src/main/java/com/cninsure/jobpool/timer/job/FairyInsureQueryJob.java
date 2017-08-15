package com.cninsure.jobpool.timer.job;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.timer.SchedulerService;
import com.common.redis.CMRedisClient;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.cm.entity.INSBFairyInsureErrorLog;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.service.INSBFairyInsureErrorLogService;
import com.zzb.cm.service.INSBOrderService;
import net.sf.json.JSONObject;
import org.quartz.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * author: wz
 * date: 2017/3/25.
 */
@Service
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class FairyInsureQueryJob implements Job,IFairyInsureQuery {
    @Resource
    private INSBOrderService insbOrderService;

    @Resource
    private SchedulerService schedule;
    
    @Resource
    INSBFairyInsureErrorLogService insbFairyInsureErrorLogService;

    @Resource
    InterFaceService interFaceService;

    @Resource
    ThreadPoolTaskExecutor executor;

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LogUtil.info("平安二维码或者EDI精灵承保定时查询任务开始执行...");
        Calendar start = Calendar.getInstance();
        start.set(Calendar.DAY_OF_MONTH, start.get(Calendar.DAY_OF_MONTH) - 1);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);

        Calendar end = Calendar.getInstance();
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        end.set(Calendar.MILLISECOND, 999);

        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable;
        Page<INSBOrder> page = null;
        while (page == null || !page.isLast()) {

            pageable = new MyPageable(pageNumber++, pageSize);

            page = insbOrderService.selectOrdersWithFairyQRPay(start.getTime(), end.getTime(), pageable);

            LogUtil.info("平安二维码或者EDI精灵承保定时查询开始处理第%s页, 内容: %s", pageNumber, JSONObject.fromObject(page));

            if (page != null && page.getContent() != null && !page.getContent().isEmpty()) {
                for (INSBOrder order : page.getContent()) {
                    QueryRequest request = new QueryRequest(executor, order, 5);
                    executor.execute(request);
                }
            }
        }
        LogUtil.info("平安二维码或者EDI精灵承保定时查询完成数据库检索数据");
    }

    private static class MyPageable implements Pageable {
        private int pageNumber;
        private int pageSize;

        public MyPageable(int pageNumber, int pageSize) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        @Override
        public int getPageNumber() {
            return pageNumber;
        }

        @Override
        public int getPageSize() {
            return pageSize;
        }

        @Override
        public int getOffset() {
            return pageNumber * pageSize;
        }

        @Override
        public Sort getSort() {
            return null;
        }

        @Override
        public Pageable next() {
            return null;
        }

        @Override
        public Pageable previousOrFirst() {
            return null;
        }

        @Override
        public Pageable first() {
            return null;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }
    }

    private class QueryRequest implements Runnable {
        private static final int DEFAULT_RETRY_TIMES = 5;
        private Integer retryTimes;
        private Integer currentTimes = 0;
        private INSBOrder order;
        private ThreadPoolTaskExecutor executor;

        public QueryRequest(ThreadPoolTaskExecutor executor, INSBOrder order, Integer retryTimes) {
            this.executor = executor;
            this.order = order;
            this.retryTimes = retryTimes;
        }

        public INSBOrder getOrder() {
            return order;
        }

        public void setOrder(INSBOrder order) {
            this.order = order;
        }

        public ThreadPoolTaskExecutor getExecutor() {
            return executor;
        }

        public void setExecutor(ThreadPoolTaskExecutor executor) {
            this.executor = executor;
        }

        public int getRetryTimes() {
            return retryTimes == null ? DEFAULT_RETRY_TIMES : retryTimes;
        }

        public void setRetryTimes(Integer retryTimes) {
            if (retryTimes >= 0) {
                this.retryTimes = retryTimes;
            }
        }

        @Override
        public void run() {
            try {
                LogUtil.info("平安二维码或者EDI精灵承保定时查询开始第%d次查询[taskId=%s, providerId=%s]，订单内容: %s",
                        ++currentTimes, order.getTaskid(), order.getPrvid(), JSONObject.fromObject(order));
                String result = interFaceService.goToFairyQuote(order.getTaskid(), order.getPrvid(), "admin", "approvedquery@qrcode");
                Boolean isSuccessful = false;
                com.alibaba.fastjson.JSONObject jsonObject = null;
                if (!StringUtils.isEmpty(result)) {
                    jsonObject = com.alibaba.fastjson.JSONObject.parseObject(result);
                    isSuccessful = jsonObject.getBoolean("result");
                }

                if (!isSuccessful) {
                    LogUtil.error("平安二维码或者EDI精灵承保定时查询请求失败[taskId=%s, providerId=%s], 承保查询接口返回: %s",
                            order.getTaskid(), order.getPrvid(), result);
                    if (currentTimes <= retryTimes) {
                        LogUtil.error("平安二维码或者EDI精灵承保定时查询将重新发起查询[taskId=%s, providerId=%s]",
                                order.getTaskid(), order.getPrvid());
                        executor.execute(this);
                        return;
                    }
                } else {
                    LogUtil.info("平安二维码或者EDI精灵承保定时查询请求成功[taskId=%s, providerId=%s]",
                            order.getTaskid(), order.getPrvid());
                }

                String taskId = order.getTaskid();
                String prvId = order.getPrvid();
                INSBFairyInsureErrorLog query = new INSBFairyInsureErrorLog();
                query.setTaskId(taskId);
                query.setInsuranceCompanyId(prvId);

                INSBFairyInsureErrorLog insureErrorLog = insbFairyInsureErrorLogService.queryOne(query);
                if (insureErrorLog == null) {
                    insureErrorLog = new INSBFairyInsureErrorLog();
                    insureErrorLog.setTaskId(taskId);
                    insureErrorLog.setInsuranceCompanyId(prvId);
                    Date now = new Date();
                    insureErrorLog.setCreateTime(now);
                    insureErrorLog.setModifyTime(now);
                    insureErrorLog.setOperator("fairy insure query job");
                    insureErrorLog.setRequestSuccess(isSuccessful);
                    insbFairyInsureErrorLogService.insert(insureErrorLog);
                } else {
                    Date now = new Date();
                    insureErrorLog.setModifyTime(now);
                    insureErrorLog.setErrorCode("");
                    insureErrorLog.setErrorDesc("");
                    insureErrorLog.setRequestSuccess(isSuccessful);
                    insbFairyInsureErrorLogService.updateById(insureErrorLog);
                }

            } catch (Exception e) {
                LogUtil.error("平安二维码或者EDI精灵承保定时查询异常。异常信息：" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

	@Override
	public void execute() {
		try {
			execute(null);
		} catch (JobExecutionException e) {
			LogUtil.error("定时承保查询异常。异常信息：%s", e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void timerForFairyInsureQuery(String key, String timeStr) {
		// TODO Auto-generated method stub
    	String status = String.valueOf(CMRedisClient.getInstance().get(6,"Scheduler", key));
    	if(StringUtil.isEmpty(status)){//如果当前没有加入一个定时执行的任务处理
    		Date startTime = new Date();
    		LogUtil.info("%s开始启动定时任务用于%s定时执行，加入定时任务时间：%s", status, timeStr, startTime);
        	long timeout = 1000;//默认多一秒
    		try {
        		timeout = (DateUtil.parse(format.format(startTime)+" "+timeStr, "yyyy-MM-dd HH:mm:ss").getTime()-startTime.getTime())+timeout;
        		startTime = new Date(timeout+startTime.getTime());
    			schedule.deleteHistoryJobAndStartNewJob(key,key,startTime,"1",timeout+"",key);
    			LogUtil.info("%s开始启动定时任务用于%s定时执行，执行任务时间：%s", status, timeStr, startTime);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
		
	}
}
