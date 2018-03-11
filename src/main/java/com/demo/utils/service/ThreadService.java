package com.demo.utils.service;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 線程服務
 */
public interface ThreadService
{
	int getCorePoolSize();

	void setCorePoolSize(int corePoolSize);

	int getKeepAliveSeconds();

	void setKeepAliveSeconds(int keepAliveSeconds);

	int getMaxPoolSize();

	void setMaxPoolSize(int maxPoolSize);

	int getQueueCapacity();

	void setQueueCapacity(int queueCapacity);

	//
	Future<?> submit(Callable<?> callable);

	Future<?> submit(Runnable runnable);
}
