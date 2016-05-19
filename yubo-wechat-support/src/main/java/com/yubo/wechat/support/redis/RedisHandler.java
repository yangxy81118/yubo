package com.yubo.wechat.support.redis;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Redis 连接处理工具类
 * 
 * @author young.jason
 *
 */
@Component
public class RedisHandler {

	private static final Logger logger = LoggerFactory.getLogger(RedisHandler.class);

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
	
	
	public Jedis getRedisClient() throws Exception {
		
		try{
			return jedisPool.getResource();
		}catch(JedisConnectionException connException){
			logger.error("获取Redis链接出错,{}",connException.getMessage());
		}catch (Exception e) {
			logger.error("获取Redis链接出错,",e);
			throw e;
		}
		return null;
	}
	
}
