package com.cninsure.jobpool.timer.job;

public interface IFairyInsureQuery {
	/**
	 * 执行待支付承保查询定时任务的业务逻辑
	 */
	public void execute();
	
	/**
     * 启动定时任务,确定一天中某个时间开始处理的定时器
     * @param key 保存业务逻辑key，用于标记业务类型和组织业务参数用_隔开
     * @param timeStr HH:mm:ss
     */
	public void timerForFairyInsureQuery(String key, String timeStr);
}
