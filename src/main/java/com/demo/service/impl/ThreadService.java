package com.demo.service.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.AbstractService;


@Service
public class ThreadService extends AbstractService {
	
	
	private ExecutorService executorService;
	private int coreSize = 1;
	
	public ThreadService(int coreSize) {
		super();
		this.coreSize = coreSize;
	}
	
	public ThreadService(){
	}

	/**
	 * 执行任务
	 * @param task 任务
	 */
	public void sumbit(Runnable task){

		executorService.submit(task);
	}

	@Override
	public void doStart() {
		// TODO Auto-generated method stub
		executorService = Executors.newFixedThreadPool(coreSize);		
	}

	@Override
	public void doStop() {
		// TODO Auto-generated method stub
		executorService.shutdown();
	}
}
