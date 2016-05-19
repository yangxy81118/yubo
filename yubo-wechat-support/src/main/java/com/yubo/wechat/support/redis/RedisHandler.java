package com.yubo.wechat.support.redis;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis 连接处理工具类
 * 
 * @author young.jason
 *
 */
@Component
public class RedisHandler {

	
	@Value("${redis.host}")
	private String host;
	
	@Value("${redis.port}")
	private int port;
	
	@Value("${redis.maxActive}")
	private int maxActive;
	
	@Value("${redis.maxIdle}")
	private int maxIdle;
	
	@Value("${redis.maxWait}")
	private long maxWait;
	
	private JedisPool jedisPool;
	
	@PostConstruct
	public void initRedisPool(){
		
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(maxActive);
		config.setMaxIdle(maxIdle);
		config.setMaxWait(maxWait);
		config.setTestOnBorrow(false);
		
		jedisPool = new JedisPool(config, host, port);
	}
	
	
	public Jedis getRedisClient(){
		return jedisPool.getResource();
	}
	
}
