package com.hcicloud.sap.common;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
	private static Logger log = Logger.getLogger(RedisUtil.class);
//	private static String ADDR = new PropertiesLoader("system.properties").getProperty("redis_ip");
//	private static int PORT = new PropertiesLoader("system.properties").getInteger("redis_port");

	//本地的测试
//	private static String ADDR = "10.0.1.227";
//	private static int PORT = 8888;

	//51.1 上的测试
	private static String ADDR = "192.169.51.1";
	private static int PORT = 6379;

	private static int MAX_ACTIVE = 1024;
	private static int MAX_IDLE = 30;
	private static int MAX_WAIT = 10000;
	private static int TIMEOUT = 10000;
	private static boolean TEST_ON_BORROW = true;
	private static JedisPool jedisPool = null;
	static {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(MAX_ACTIVE);
			config.setMaxIdle(MAX_IDLE);
			config.setMaxWaitMillis(MAX_WAIT);
			config.setTestOnBorrow(TEST_ON_BORROW);
			jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public synchronized static Jedis getJedis() {
		try {
			if (jedisPool != null) {
				Jedis resource = jedisPool.getResource();
				return resource;
			} else {
				return null;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 释放jedis资源
	 * 
	 * @param jedis
	 */
	public static void returnResource(final Jedis jedis) {
		if (jedis != null) {
			jedisPool.returnResource(jedis);
		}
	}
}
