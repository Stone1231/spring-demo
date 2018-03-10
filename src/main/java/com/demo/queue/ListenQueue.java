package com.demo.queue;

public interface ListenQueue<E> extends CommonRunnable {

	long getListenMills();

	void setListenMills(long listenMills);

	boolean offer(E e);

	void process(E e);
}
