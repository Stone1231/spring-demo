package com.demo.utils.service.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.demo.utils.service.ThreadService;

/**
 * 執行緒服務
 */
public class ThreadServiceImpl implements ThreadService, InitializingBean, DisposableBean {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(ThreadServiceImpl.class);

	/**
	 * executor的最大數目
	 */
	private int maxExecutorSize;

	private int corePoolSize;

	private int keepAliveSeconds;

	/**
	 * thread的最大數目
	 */
	private int maxPoolSize;

	private int queueCapacity;

	private boolean waitForTasksToCompleteOnShutdown;

	private boolean allowCoreThreadTimeOut;

	private boolean daemon;

	private ThreadPoolTaskExecutor taskExecutors[] = null;

	private AtomicInteger counter = new AtomicInteger(0);

	public ThreadServiceImpl() {
	}

	public int getMaxExecutorSize() {
		return maxExecutorSize;
	}

	public void setMaxExecutorSize(int maxExecutorSize) {
		this.maxExecutorSize = maxExecutorSize;
	}

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getKeepAliveSeconds() {
		return keepAliveSeconds;
	}

	public void setKeepAliveSeconds(int keepAliveSeconds) {
		this.keepAliveSeconds = keepAliveSeconds;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getQueueCapacity() {
		return queueCapacity;
	}

	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}

	public Future<?> submit(Callable<?> callable) {
		return getNextExecutor().submit(callable);
	}

	public Future<?> submit(Runnable runnable) {
		return getNextExecutor().submit(runnable);
	}

	protected SchedulingTaskExecutor getNextExecutor() {
		int index = counter.getAndIncrement() % maxExecutorSize;
		return taskExecutors[index];
	}

	public boolean isWaitForTasksToCompleteOnShutdown() {
		return waitForTasksToCompleteOnShutdown;
	}

	public void setWaitForTasksToCompleteOnShutdown(boolean waitForTasksToCompleteOnShutdown) {
		this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
	}

	public boolean isAllowCoreThreadTimeOut() {
		return allowCoreThreadTimeOut;
	}

	public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
		this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
	}

	public boolean isDaemon() {
		return daemon;
	}

	public void setDaemon(boolean daemon) {
		this.daemon = daemon;
	}

	public void afterPropertiesSet() throws Exception {
		this.taskExecutors = new ThreadPoolTaskExecutor[maxExecutorSize];
		//
		int singleCorePoolSize = corePoolSize / maxExecutorSize;
		int singleMaxPoolSize = maxPoolSize / maxExecutorSize;
		int singleQueueCapacity = queueCapacity / maxExecutorSize;
		//
		int lastCorePoolSize = singleCorePoolSize + corePoolSize % maxExecutorSize;
		int lastMaxPoolSize = singleMaxPoolSize + maxPoolSize % maxExecutorSize;
		int lastQueueCapacity = singleQueueCapacity + queueCapacity % maxExecutorSize;
		for (int i = 0; i < taskExecutors.length; ++i) {
			ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
			// when [0-3]
			if (i != taskExecutors.length - 1) {
				poolTaskExecutor.setCorePoolSize(singleCorePoolSize);
				poolTaskExecutor.setMaxPoolSize(singleMaxPoolSize);
				poolTaskExecutor.setQueueCapacity(singleQueueCapacity);

			} // when [4] 最後一個
			else {
				poolTaskExecutor.setCorePoolSize(lastCorePoolSize);
				poolTaskExecutor.setMaxPoolSize(lastMaxPoolSize);
				poolTaskExecutor.setQueueCapacity(lastQueueCapacity);
			}
			//
			poolTaskExecutor.setKeepAliveSeconds(keepAliveSeconds);
			poolTaskExecutor.setWaitForTasksToCompleteOnShutdown(waitForTasksToCompleteOnShutdown);
			poolTaskExecutor.setAllowCoreThreadTimeOut(allowCoreThreadTimeOut);
			poolTaskExecutor.setDaemon(daemon);
			poolTaskExecutor.initialize();
			//
			taskExecutors[i] = poolTaskExecutor;
		}
	}

	public void destroy() throws Exception {
		for (SchedulingTaskExecutor executor : taskExecutors) {
			if (executor instanceof ThreadPoolTaskExecutor) {
				ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) executor;
				ThreadPoolExecutor poolExecutor = taskExecutor.getThreadPoolExecutor();
				poolExecutor.shutdown();
			}
		}
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
