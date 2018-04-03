package com.demo.utils.service.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.demo.utils.service.ScheduledThreadService;


/**
 * 執行緒服務
 */
@Service
public class ScheduledThreadServiceImpl implements ScheduledThreadService, InitializingBean, DisposableBean {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(ScheduledThreadServiceImpl.class);

	/**
	 * executor的最大數目
	 */
	private int corePoolSize = 10;

	private ScheduledThreadPoolExecutor scheduledThreadPool = null;

	public ScheduledThreadServiceImpl() {
	}

	@Override
	public int getCorePoolSize() {
		return corePoolSize;
	}

	@Override
	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	@Override
	public Future<?> submit(Callable<?> callable) {
		return scheduledThreadPool.submit(callable);
	}

	@Override
	public Future<?> submit(Runnable runnable) {
		return scheduledThreadPool.submit(runnable);
	}

	@Override
	public ScheduledFuture<?> schedule(Callable<?> callable, long delay, TimeUnit unit) {
		return scheduledThreadPool.schedule(callable, delay, unit);
	}

	@Override
	public ScheduledFuture<?> schedule(Runnable runnable, long delay, TimeUnit unit) {
		return scheduledThreadPool.schedule(runnable, delay, unit);
	}

	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long initialDelay, long period, TimeUnit unit) {
		return scheduledThreadPool.scheduleAtFixedRate(runnable, initialDelay, period, unit);
	}

	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long initialDelay, long delay, TimeUnit unit) {
		return scheduledThreadPool.scheduleWithFixedDelay(runnable, initialDelay, delay, unit);
	}

	public void afterPropertiesSet() throws Exception {
		LOGGER.debug("ScheduledThreadServie init...");
		this.scheduledThreadPool = new ScheduledThreadPoolExecutor(corePoolSize);
	}

	public void destroy() throws Exception {
		scheduledThreadPool.shutdown();
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
