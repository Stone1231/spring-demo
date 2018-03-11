package com.demo.utils.service;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 定期排程線程服務
 */
public interface ScheduledThreadService
{
	int getCorePoolSize();

	void setCorePoolSize(int corePoolSize);
	
	Future<?> submit(Callable<?> callable);

	Future<?> submit(Runnable runnable);

    ScheduledFuture<?> schedule(Callable<?> callable, long delay, TimeUnit unit);

    ScheduledFuture<?> schedule(Runnable runnable, long delay, TimeUnit unit);

    ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long initialDelay, long period,
            TimeUnit unit);

    ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long initialDelay, long delay,
            TimeUnit unit);
}
