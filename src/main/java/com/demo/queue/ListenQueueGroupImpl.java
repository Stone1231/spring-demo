package com.demo.queue;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.BeanInitializationException;

public class ListenQueueGroupImpl<E> implements ListenQueueGroup<E> {

	private ListenQueue<E> listQueues[] = null;

	private int listQueueSize;

	private AtomicInteger counter = new AtomicInteger(0);

	public ListenQueueGroupImpl(ListenQueue<E> listQueues[]) {
		try {
			this.listQueues = listQueues;
			this.listQueueSize = listQueues.length;
			for (ListenQueue<E> listenQueue : listQueues) {
				Thread thread = new Thread(listenQueue);
				thread.start();
			}
		} catch (Exception ex) {
			throw new BeanInitializationException(
					"Initialization of ListenQueueGroupImpl failed", ex);
		}
	}

	protected ListenQueue<E> getNextQueue() {
		if (Integer.MAX_VALUE == counter.get()) {
			counter.set(0);
		}
		int index = counter.getAndIncrement() % listQueueSize;
		return listQueues[index];
	}

	public boolean offer(E e) {
		return getNextQueue().offer(e);
	}
}
