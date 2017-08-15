package com.redis;

import java.util.Set;

import redis.clients.jedis.Jedis;


public class RedisSortSets {
	static Jedis jedis = RedisClient.getClient();
	
	public static void test1(){
//		jedis.zadd("hotKeyList", 1, "一汽大众捷达");
//		jedis.zadd("hotKeyList", 1, "奥迪A6");
//		jedis.zadd("hotKeyList", 1, "奥拓");
//		jedis.zadd("hotKeyList", 1, "马自达6");
//		jedis.zadd("hotKeyList", 1, "本田雅阁");
//		jedis.zrem("hotKeyList", "本田雅阁1");
//		Set<String>  aa = jedis.zrange("hotKeyList", 0, -1);
		
		
		
//		System.out.println(aa);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Set<String> aa = jedis.keys("17677");
		System.out.println(aa);
		
//		try {
//			com.common.RedisClient.set("person", "true",30);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		System.out.println(com.common.RedisClient.get("person"));
//		test1();
//		jedis.zincrby("hotKeyList", 1, "本田雅阁");
//		System.out.println(jedis.zscore("hotKeyList", "奥拓"));
//		Set<String> rangSet =  jedis.zrevrange("hotKeyList", 0, -1);
//		System.out.println(rangSet);
//		System.out.println(jedis.zrank("hotKeyList", "奥迪"));
		
//		Map<String,Object> aa = JSONObject.fromObject(map); 
//		for(String  strkey:aa.keySet()){
//			System.out.println(strkey);
//			System.out.println("aa==="+aa.get(strkey).toString());
//		}
		
//		PoolKeyModel bb = new PoolKeyModel();
//		bb.setPoolKey("1200000000");
//		List<Task> aa = Pool.getAll(bb);
//		for (Task task : aa) {
//			System.out.println("==任务池"+task);
//		}
//		String name = (String) com.common.RedisClient.get("wanglin_online");
//		System.out.println(name);
//		List<TimerJob> timestr =  com.common.RedisClient.getList("TimerJob", TimerJob.class);
//		for (TimerJob task : timestr) {
//			System.out.println("定时任务池"+task);
//		}
		
//		List<String> onlineUserCodes = new ArrayList<String>();
//		String onlineUserStr = (String) com.common.RedisClient.get("onlineUsers");
//		Map<String, Object> tempMap = JSONObject.fromObject(onlineUserStr);
//		System.out.println(tempMap);
//		
//		for (String usercode : tempMap.keySet()) {
//			Map<String, String> userData = JSONObject.fromObject(tempMap.get(usercode));
//			System.out.println(userData);
//		}
//		System.out.println(result);
//		
//		PoolKeyModel aa = new PoolKeyModel();
//		aa.setPoolKey("1200000000");
//		
//		
//		Pool.del(aa, "86", "87", "206637");
		   
//		jedis.del("TimerJob");
//		jedis.del("TaskPool-1200000000");
//		System.out.println(jedis.get("c2135e6c8bc2b60b4ace09fad8d766e8"));
	}
}
