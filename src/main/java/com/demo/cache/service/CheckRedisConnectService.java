package com.demo.cache.service;

public interface CheckRedisConnectService {

	boolean getConnectState(String factoryName);

	public void setConnectState(String factoryName, boolean connectState);

	long getPingRedisInterval();

	void setPingRedisInterval(long pingRedisInterval);

	long getInitialDelay();

	void setInitialDelay(long initialDelay);
}
