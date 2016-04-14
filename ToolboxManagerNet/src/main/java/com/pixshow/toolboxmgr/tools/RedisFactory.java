package com.pixshow.toolboxmgr.tools;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisFactory {

	// Redis服务器IP
	private  String addr = "127.0.0.1";

	// Redis的端口号
	private  int port = 6379;

	// 访问密码
	private  String auth = "";

	// 可用连接实例的最大数目，默认值为8；
	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
//	private  static int MAX_ACTIVE = 1024;
	private  static int MAX_ACTIVE = 1024;

	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private static  int MAX_IDLE = 200;
//	private static  int MAX_IDLE = 200;

	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private static  int MAX_WAIT = 60000;

	private static  int TIMEOUT = 60000;

	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private static boolean TEST_ON_BORROW = false;
	private static boolean TEST_WHILE_IDLE = false;
	private static boolean TEST_ON_RETURN = false;

	private  JedisPool jedisPool = null;
	private static JedisPoolConfig config;

	/**
	 * 初始化Redis连接池
	 */
	static {
		try {
			config = new JedisPoolConfig();
			config.setMaxActive(MAX_ACTIVE);
			config.setMaxIdle(MAX_IDLE);
			config.setMaxWait(MAX_WAIT);
			config.setTestOnReturn(TEST_ON_RETURN);
			config.setTestWhileIdle(TEST_WHILE_IDLE);
			config.setTestOnBorrow(TEST_ON_BORROW);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取Jedis实例
	 * 
	 * @return
	 */
	public synchronized Jedis getJedis() {
		try {
			if (jedisPool != null) {
				Jedis resource = jedisPool.getResource();
				return resource;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public RedisFactory(String addr, int port) {
		super();
		this.addr = addr;
		this.port = port;
		jedisPool = new JedisPool(config, addr, port, TIMEOUT);
	}

	/**
	 * 释放jedis资源
	 * 
	 * @param jedis
	 */
	public void returnResource(final Jedis jedis) {
		if (jedis != null) {
			jedisPool.returnResource(jedis);
		}
	}
}
