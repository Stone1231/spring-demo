package com.demo.cache.service;

import java.util.Map;

import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;

public class RedissonManager extends RedissonSpringCacheManager {
	
	private RedissonClient redissonClient;

	/**
	 * redisson client
	 */
	private String clientName;
	
	private String redissonCacheManagerName;
	
	private CacheConfig cacheConfig;

	public RedissonManager(RedissonClient redisson, Map<String, ? extends CacheConfig> config) {
		super(redisson, config);
		this.redissonClient = redisson;
	}
	
	public RedissonManager(RedissonClient redisson, Map<String, ? extends CacheConfig> config, Codec codec) {
		super(redisson, config, codec);
		this.redissonClient = redisson;
    }
	
	public RedissonManager(RedissonClient redisson, String configLocation) {
		super(redisson, configLocation, null);
		this.redissonClient = redisson;
	}

	public RedissonManager(RedissonClient redisson, String configLocation, Codec codec) {
		super(redisson, configLocation, codec);
		this.redissonClient = redisson;
    }
	
	public RedissonClient getRedissonClient() {
		return redissonClient;
	}

	public void setRedissonClient(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getRedissonCacheManagerName() {
		return redissonCacheManagerName;
	}

	public void setRedissonCacheManagerName(String redissonCacheManagerName) {
		this.redissonCacheManagerName = redissonCacheManagerName;
	}

	public CacheConfig getCacheConfig() {
		return cacheConfig;
	}

	public void setCacheConfig(CacheConfig cacheConfig) {
		this.cacheConfig = cacheConfig;
	}
	
	

}
