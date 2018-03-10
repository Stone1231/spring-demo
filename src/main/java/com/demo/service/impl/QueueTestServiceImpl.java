package com.demo.service.impl;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.demo.queue.AbstractListenQueue;
import com.demo.queue.ListenQueue;
import com.demo.queue.ListenQueueGroup;
import com.demo.queue.ListenQueueGroupImpl;
import com.demo.service.QueueTestService;


@Service
public class QueueTestServiceImpl implements QueueTestService {


	@Value("${qt.threads}")
	private int threadCount;

    private transient ListenQueueGroup<Integer> listenQueueGroup;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(QueueTestServiceImpl.class);
	
	@PostConstruct
	protected void init() {
		buildQueue(threadCount);
	}
	

	@Override
	public void sendData(int sec) {

		try {			
			send(sec);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void sendDataSync(int sec) {
		try {
			this.listenQueueGroup.offer(sec);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	protected void send(int sec) {
		try {
			Thread.sleep(sec*1000);
			LOGGER.info("sec:" + sec);
		} catch (Exception ex) {
			// TODO: handle exception
			LOGGER.error(ex.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void buildQueue(int threads) {
		ListenQueue<Integer> listenQueues[] = new Queue[threads];
		for (int i = 0; i < listenQueues.length; ++i) {
			Queue<Integer> queue = new Queue<Integer>();
			listenQueues[i] = queue;
		}
		this.listenQueueGroup = new ListenQueueGroupImpl(listenQueues);
	}

	protected class Queue<E> extends AbstractListenQueue<Integer> {

		public Queue(){		
			try {
				//一些初始化設定  ex:client = createClient();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void process(Integer sec) {	
			send(sec);
		}
	}

}
