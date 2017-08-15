package com.redis;


import redis.clients.jedis.Jedis;

public class RedisClient {
  public static Jedis jedis = null;
  public static String host ="203.195.141.57";
  public static int port =6379;
  public static Jedis getClient(){
    if(jedis == null){
      jedis = new Jedis(host,port);
    }
    return jedis;
  }
}