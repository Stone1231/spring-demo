package com.demo.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public abstract class AbstractListenQueue<E> extends AbstractCommonRunnable
		implements ListenQueue<E> {

	private Queue<E> elements = new ConcurrentLinkedQueue<E>();

	private static final long DEFAULT_LISTEN_MILLS = 50L;

	private long listenMills = DEFAULT_LISTEN_MILLS;

	public AbstractListenQueue() {
	}

	public long getListenMills() {
		return listenMills;
	}

	public void setListenMills(long listenMills) {
		this.listenMills = listenMills;
	}

	public boolean offer(E e) {
		boolean result = false;
		synchronized (elements) {
			result = elements.offer(e);
		}
		return result;
	}

	public void execute() {
		while (true) {
			try {
				if (isDisable()) {
					break;
				}
				doExecute();
				TimeUnit.MILLISECONDS.sleep(listenMills);
			} catch (Exception ex) {
				// ex.printStackTrace();
			}
		}
	}

	protected void doExecute() {
		E e = null;
		try {
			synchronized (elements) {
				while (!elements.isEmpty()) {
					try {
						e = elements.poll();
						if (e != null) {
							process(e);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
