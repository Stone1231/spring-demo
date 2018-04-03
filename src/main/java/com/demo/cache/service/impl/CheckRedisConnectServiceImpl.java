package com.demo.cache.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Service;

import redis.clients.jedis.exceptions.JedisConnectionException;

import com.demo.base.service.AbstractCommonService;
import com.demo.cache.AbstractCacheService;
import com.demo.cache.aspect.CacheAspect;
import com.demo.cache.service.CheckRedisConnectService;
import com.demo.utils.StringUtil;
import com.demo.utils.service.ScheduledThreadService;

@Service
public class CheckRedisConnectServiceImpl extends AbstractCacheService implements CheckRedisConnectService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheAspect.class);

	/**
	 * 檢查Redis 連線時間間隔，預設 3分鐘
	 */
	private long pingRedisInterval = 3 * 60 * 1000L;

	private long initialDelay = 1 * 10L;

	@Autowired
	private ScheduledThreadService scheduledThreadService;

	private Map<String, Boolean> connectStates;

	@Override
	protected void init() {
		super.init();
		//
		connectStates = new ConcurrentHashMap<>();

		Map<String, JedisConnectionFactory> jedisConnectionFactorys = this.applicationContext
				.getBeansOfType(JedisConnectionFactory.class);

		LOGGER.info("Check Redis Connect initializing.");
		//
		long delay = initialDelay;
		for (Map.Entry<String, JedisConnectionFactory> entry : jedisConnectionFactorys.entrySet()) {
			String factoryName = entry.getKey();
			JedisConnectionFactory jedisConnectionFactory = entry.getValue();

			if (jedisConnectionFactory == null) {
				LOGGER.warn("JedisConnectionFactory '" + factoryName + "' is null.");
				continue;
			}
			//
			connectStates.put(factoryName, false);

			scheduledThreadService.scheduleAtFixedRate(new PingRedis(jedisConnectionFactory, factoryName), delay,
					pingRedisInterval, TimeUnit.MILLISECONDS);
			delay += 10;
		}
	}

	public boolean getConnectState(String factoryName) {
		return connectStates.get(factoryName);

	}

	public void setConnectState(String factoryName, boolean connectState) {
		this.connectStates.put(factoryName, connectState);
	}

	public long getPingRedisInterval() {
		return pingRedisInterval;
	}

	public void setPingRedisInterval(long pingRedisInterval) {
		this.pingRedisInterval = pingRedisInterval;
	}

	public long getInitialDelay() {
		return initialDelay;
	}

	public void setInitialDelay(long initialDelay) {
		this.initialDelay = initialDelay;
	}

	protected class PingRedis implements Runnable {

		private JedisConnectionFactory jedisConnectionFactory;
		private String factoryName;

		public PingRedis(JedisConnectionFactory jedisConnectionFactory, String factoryName) {
			this.jedisConnectionFactory = jedisConnectionFactory;
			this.factoryName = factoryName;
		}

		@Override
		public void run() {
			RedisConnection conn = null;
			try {
				LOGGER.info("Ping " + factoryName + " Redis Connection");
				conn = jedisConnectionFactory.getConnection();
				String ping = conn.ping();

				if (StringUtil.isNullOrEmpty(ping) || !StringUtil.equals(ping, "PONG")) {
					LOGGER.info(factoryName + " Connection is lost");
					//
					setConnectState(factoryName, false);

				} else {
					setConnectState(factoryName, true);
				}

			} catch (JedisConnectionException ex) {
				setConnectState(factoryName, false);
				LOGGER.warn(factoryName + " Connection is lost", ex);
			} catch (Exception ex) {
				setConnectState(factoryName, false);
				LOGGER.warn("Exception encountered during PingRedis.run()", ex);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (Exception ex2) {
						//
					}
				}
			}
		}
	}

}
