package com.common.redis;

import java.util.*;

import javax.annotation.PostConstruct;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Created by @Author Chen Haoming on 2016/6/20.
 */
@Component("redisClient")
public class CMRedisClient implements IRedisClient {

    private static JedisPool JEDIS_POOL;
    /**
     * 有些类的static方法引用CMRedisClient。因为无法通过spring注入实例，暂时提供一个static的实例供他们使用。后面尽量重构他们，消除static方法。
     */
    private static CMRedisClient INSTANCE;

    private int maxActive;

    private int maxIdle;

    private int maxWait;

    private String ip;

    private String password;

    private int port;

    {
        // 读取相关的配置
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config/config");
        maxActive = Integer.parseInt(resourceBundle.getString("redis.pool.maxActive"));
        maxIdle = Integer.parseInt(resourceBundle.getString("redis.pool.maxIdle"));
        maxWait = Integer.parseInt(resourceBundle.getString("redis.pool.maxWait"));
        port = Integer.parseInt(resourceBundle.getString("redis.port"));
        ip = resourceBundle.getString("redis.ip");
        password = resourceBundle.getString("redis.pwd");
    }

    public static CMRedisClient getInstance() {
        return INSTANCE;
    }

    @PostConstruct
    public synchronized void postConstruct() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxActive);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWait);
        JEDIS_POOL = StringUtils.isEmpty(password) ? new JedisPool(config, ip, port, maxWait) : new JedisPool(config, ip, port, maxWait, password);
        synchronized (CMRedisClient.class) {
            if (null == INSTANCE) {
                INSTANCE = this;
            }
        }
    }

    private String buildKey(String key, String module) {
        return module + ":" + key;
    }

    @Override
    public boolean set(String module, String key, Object obj) {
        if (Number.class.isAssignableFrom(obj.getClass())) {
            return this.set(module, key, obj.toString());
        }
        return this.set(module, key, JSONObject.fromObject(obj).toString());
    }

    @Override
    public boolean set(String module, String key, String value) {
        Jedis jedis = null;
		boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            jedis.set(buildKey(key, module), value);
            return true;
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
            return false;
        } finally {
            closeResource(jedis, broken);
        }
    }

    @Override
    public boolean set(String module, String key, Object obj, int seconds) {
        if (Number.class.isAssignableFrom(obj.getClass())) {
            return this.set(module, key, obj.toString(), seconds);
        }
        return this.set(module, key, JSONObject.fromObject(obj).toString(), seconds);
    }

    @Override
    public boolean set(String module, String key, String value, int seconds) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            jedis.setex(buildKey(key, module), seconds, value);
            return true;
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
            return false;
        } finally {
            closeResource(jedis, broken);
        }
    }

    @Override
    public boolean set(int dbindex, String module, String key, String value) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            jedis.select(dbindex);
            jedis.set(buildKey(key, module), value);
            return true;
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
            return false;
        } finally {
            closeResource(jedis, broken);
        }
    }

    public boolean set(int dbindex, String module, String key, String value, int seconds) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            jedis.select(dbindex);
            jedis.setex(buildKey(key, module), seconds, value);
            return true;
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
            return false;
        } finally {
            closeResource(jedis, broken);
        }
    }

    public boolean set(int dbindex, String module, String key, Object obj, int seconds) {
        if (Number.class.isAssignableFrom(obj.getClass())) {
            return this.set(dbindex, module, key, obj.toString(), seconds);
        }
        return this.set(dbindex, module, key, JSONObject.fromObject(obj).toString(), seconds);
    }

    @Override
    public boolean del(String module, String key) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            jedis.del(buildKey(key, module));
            return true;
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
            return false;
        } finally {
            closeResource(jedis, broken);
        }
    }

    public boolean del(int dbindex, String module, String key) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            jedis.select(dbindex);
            jedis.del(buildKey(key, module));
            return true;
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
            return false;
        } finally {
            closeResource(jedis, broken);
        }
    }

    @Override
    public Object get(int dbindex, String module, String key) {
    	Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            jedis.select(dbindex);
            return jedis.get(buildKey(key, module));
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
            return null;
        } finally {
            closeResource(jedis, broken);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(int dbindex, String module, String key, Class<T> clazz) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            jedis.select(dbindex);
            String value = jedis.get(buildKey(key, module));
            Object c = JSONObject.toBean(JSONObject.fromObject(value), clazz);
            return (T) c;
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
            return null;
        } finally {
            closeResource(jedis, broken);
        }
    }

    @Override
    public Object get(String module, String key) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            return jedis.get(buildKey(key, module));
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
            return null;
        } finally {
            closeResource(jedis, broken);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String module, String key, Class<T> clazz) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            String value = jedis.get(buildKey(key, module));
            Object c = JSONObject.toBean(JSONObject.fromObject(value), clazz);
            return (T) c;
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
            return null;
        } finally {
            closeResource(jedis, broken);
        }
    }

    @Override
    public <T> boolean setList(String module, String key, List<T> value) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            String arrayJson = JSONArray.fromObject(value).toString();
            jedis = JEDIS_POOL.getResource();
            jedis.set(buildKey(key, module), arrayJson);
            return true;
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
            return false;
        } finally {
            closeResource(jedis, broken);
        }
    }
    @Override
    public <T> boolean setList(String module, String key, List<T> value, int seconds) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            String arrayJson = JSONArray.fromObject(value).toString();
            jedis = JEDIS_POOL.getResource();
            jedis.setex(buildKey(key, module), seconds,arrayJson);

            return true;
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
            return false;
        } finally {
            closeResource(jedis, broken);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getList(String module, String key, Class<T> clazz) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            String value = jedis.get(buildKey(key, module));
            if (value != null) {
                JSONArray jsonarray = JSONArray.fromObject(value);
                List<T> l = JSONArray.toList(jsonarray, clazz);
                return l;
            } else {
                return new ArrayList<T>();
            }
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
        } finally {
            closeResource(jedis, broken);
        }
        return null;
    }

    @Override
    public int addOne(String module, String key) {
        Jedis jedis = null;
        long result = 0;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            result = jedis.incr(buildKey(key, module));
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
        } finally {
            closeResource(jedis, broken);
        }
        return (int) result;
    }

    @Override
	public int minusOne(String module, String key) {
    	Jedis jedis = null;
        long result = 0;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            result = jedis.decr(buildKey(key, module));
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
        } finally {
            closeResource(jedis, broken);
        }
        return (int) result;
	}

    @Override
    public int atomicIncr(String module, String rdKey) {
        Jedis jedis = null;
        long result = 0;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            result = jedis.incr(buildKey(rdKey, module));
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
        } finally {
            closeResource(jedis, broken);
        }
        return (int) result;
    }

    @Override
    public void expire(String module,String rdKey, int second) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            jedis.expire(buildKey(rdKey, module),second);
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
        } finally {
            closeResource(jedis, broken);
        }
    }

    @Override
    public String rename(String oldKey, String newKey) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            return jedis.rename(oldKey, newKey);
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
            return "";
        } finally {
            closeResource(jedis, broken);
        }
    }

    @Override
    public Set<String> zrevrange(String module, String key, long end) {
        return zrevrange(module, key, 0, end);
    }

    @Override
    public Set<String> zrevrange(String module, String key, long start, long end) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            Set<String> rangSet = jedis.zrevrange(buildKey(key, module), start, end);
            return rangSet;
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
            return new HashSet<>();
        } finally {
            closeResource(jedis, broken);
        }
    }

    @Override
    public Set<String> keys(int dbindex, String pattern) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            jedis.select(dbindex);
            Set<String> keySet = jedis.keys(pattern);
            return keySet;
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
            return new HashSet<String>();
        } finally {
            closeResource(jedis, broken);
        }
    }

    @Override
    public Set<String> keys(String pattern) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            Set<String> keySet = jedis.keys(pattern);
            return keySet;
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
            return new HashSet<String>();
        } finally {
            closeResource(jedis, broken);
        }
    }
    
    public Set<String> getKeys(String module, String rdKey) {
		Jedis jedis = null;
        boolean broken = false;
		try {
			jedis = JEDIS_POOL.getResource();
			Set<String> keySet = jedis.keys(buildKey(rdKey, module));
			return keySet;
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
			return new HashSet<String>();
		} finally {
            closeResource(jedis, broken);
		}
	}

	public Long lpush(String module, String key, String... values) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            return jedis.lpush(buildKey(key, module), values);
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
        } finally {
            closeResource(jedis, broken);
        }
        return -1L;
    }

    public List<String> brpop(String module, String key, int timeout) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            List<String> values = jedis.brpop(timeout, buildKey(key, module));
            return values;
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
        } finally {
            closeResource(jedis, broken);
        }
        return Collections.emptyList();
    }

    @Override
    public Long lrem(String module, String key, Integer count, String value) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            return jedis.lrem(buildKey(key, module), count, value);
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
        } finally {
            closeResource(jedis, broken);
        }
        return 0L;
    }

    public List<String> lrange(String module, String key, long start, long end) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            return jedis.lrange(buildKey(key, module), start, end);
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
        } finally {
            closeResource(jedis, broken);
        }
        return Collections.emptyList();
    }

    public Long llen(String module, String key) {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = JEDIS_POOL.getResource();
            return jedis.llen(buildKey(key, module));
        } catch (JedisException e) {
            e.printStackTrace();
            broken = handleJedisException(e);
        } finally {
            closeResource(jedis, broken);
        }
        return -1L;
    }

    /**
     * Handle jedisException, write log and return whether the connection is broken.
     */
    protected boolean handleJedisException(JedisException jedisException) {
        if (jedisException instanceof JedisConnectionException) {
            System.out.println("---Redis connection lost");
        } else if (jedisException instanceof JedisDataException) {
            if ((jedisException.getMessage() != null) && (jedisException.getMessage().indexOf("READONLY") != -1)) {
                System.out.println("Redis connection are read-only");
            } else {
                // dataException, isBroken=false
                return false;
            }
        } else {
            System.out.println("Jedis exception happen");
        }

        System.out.println("broken conn");
        return true;
    }

    /**
     * Return jedis connection to the pool, call different return methods depends on the conectionBroken status.
     */
    protected void closeResource(Jedis jedis, boolean conectionBroken) {
        try {
            if (conectionBroken) {
                JEDIS_POOL.returnBrokenResource(jedis);
            } else {
                JEDIS_POOL.returnResource(jedis);
            }
        } catch (Exception e) {
            System.out.println("return back jedis failed, will fore close the jedis.");
            destroyJedis(jedis);
        }
    }

	/**
	 * 在Pool以外强行销毁Jedis.
	 */
	public void destroyJedis(Jedis jedis) {
		if ((jedis != null) && jedis.isConnected()) {
			try {
				try {
					jedis.quit();
				} catch (Exception e) {
				}
				jedis.disconnect();
			} catch (Exception e) {
			}
		}
	}
}
