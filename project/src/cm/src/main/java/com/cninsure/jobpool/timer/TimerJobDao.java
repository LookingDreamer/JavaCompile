package com.cninsure.jobpool.timer;


import com.common.redis.IRedisClient;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class TimerJobDao {
    private static final String MODULE = "cm:job_pool";
    private static final String REDIS_KEY = "timer_job";

	@Resource
	private IRedisClient redisClient;

	public void saveorupdate(TimerJob  timerJob){
		del(timerJob.getTask().getProInstanceId(),timerJob.getTask().getSonProInstanceId(),timerJob.getTask().getPrvcode());
		List<TimerJob> jobs = (List<TimerJob>) redisClient.getList(MODULE, REDIS_KEY, TimerJob.class);
		jobs.add(timerJob);
		redisClient.setList(MODULE, REDIS_KEY, jobs);
	}

	public void del(String proInstanceId,String sonProInstanceId, String prvcode) {
		List<TimerJob> jobs = (List<TimerJob>) redisClient.getList(MODULE, REDIS_KEY, TimerJob.class);
		if(jobs.size()>0){
			for (TimerJob timerJob2 : jobs) {
				if(sonProInstanceId!=null){
					if (sonProInstanceId.equals(timerJob2.getTask().getSonProInstanceId())) {
						jobs.remove(timerJob2);
					}
				}else if(proInstanceId.equals(timerJob2.getTask().getProInstanceId())){
					jobs.remove(timerJob2);
				}
			}
			redisClient.del(MODULE, REDIS_KEY);
			redisClient.setList(MODULE, REDIS_KEY, jobs);
		}
		
	}

	public TimerJob findTimerJob(String proInstanceId, String prvcode) {
		List<TimerJob> jobs = (List<TimerJob>) redisClient.getList(MODULE, REDIS_KEY, TimerJob.class);
		for (TimerJob timerJob2 : jobs) {
			if (proInstanceId.equals(timerJob2.getTask().getProInstanceId())
					&& prvcode.equals(timerJob2.getTask().getPrvcode())) {
				return timerJob2;
			}
		}
		return null;
	}

	public List<TimerJob> findAll() {
		List<TimerJob> jobs = (List<TimerJob>) redisClient.getList(MODULE, REDIS_KEY, TimerJob.class);
		return jobs;
	}
}
