package com.demo.cache.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.redisson.api.Node;
import org.redisson.api.NodesGroup;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.cache.AbstractCacheService;
import com.demo.cache.service.CheckRedisConnectService;
import com.demo.utils.service.ScheduledThreadService;

@Service
public class CheckRedissonConnectServiceImpl extends AbstractCacheService implements CheckRedisConnectService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CheckRedissonConnectServiceImpl.class);

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

		Map<String, RedissonClient> redissonClients = this.applicationContext.getBeansOfType(RedissonClient.class);
		
		LOGGER.info("Check Redisson Connect initializing.");
		//
		long delay = initialDelay;
		for (Map.Entry<String, RedissonClient> entry : redissonClients.entrySet()) {
			String clientName = entry.getKey();
			RedissonClient redissonClient = entry.getValue();
			
			if(redissonClient == null) {
				LOGGER.warn("RedissonClient '" + clientName + "' is null.");
				continue;
			}
			
			connectStates.put(clientName, false);

			scheduledThreadService.scheduleAtFixedRate(new PingRedis(redissonClient, clientName), delay,
					pingRedisInterval, TimeUnit.MILLISECONDS);
			delay += 10;
		}
		
	}
	

	public boolean getConnectState(String clientName) {
		return connectStates.get(clientName);

	}

	public void setConnectState(String clientName, boolean connectState) {
		this.connectStates.put(clientName, connectState);
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

		private RedissonClient redissonClient;
		private String clientName;

		public PingRedis(RedissonClient redissonClient, String clientName) {
			this.redissonClient = redissonClient;
			this.clientName = clientName;
		}

		@Override
		public void run() {
			NodesGroup<Node> nodeGroup = null;
			try {
				LOGGER.info("Ping " + clientName + " Redis Connection");
				nodeGroup = redissonClient.getNodesGroup();
				boolean ping = nodeGroup.pingAll();
				
				if(!ping) {
					LOGGER.info(clientName + " Connection is lost");
				}

				setConnectState(clientName, ping);

			} catch (Exception ex) {
				setConnectState(clientName, false);
				LOGGER.warn("Exception encountered during PingRedis.run()", ex);
			} finally {
				
			}
		}
	}

}
