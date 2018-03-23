package com.demo.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.builder.ToStringBuilder;


public class MapCacheUtil<K, V> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapCacheUtil.class);

	private static final long serialVersionUID = -2148096947184157614L;

	private transient Map<K, V> cache = new ConcurrentHashMap<>();

	/**
	 * 存放value=null的key
	 */
	private transient Set<K> nullValueKeys = new CopyOnWriteArraySet<>();

	/**
	 * 讀寫鎖
	 */
	private transient ReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * 讀鎖
	 */
	private transient Lock readLock = lock.readLock();

	/**
	 * 寫鎖
	 */
	private transient Lock writeLock = lock.writeLock();

	/**
	 * 停用
	 */
	private AtomicBoolean disable = new AtomicBoolean(false);

	public MapCacheUtil() {
		//
	}

	public V get(K key) {
		V result = null;
		if (!disable.get()) {
			try {
				result = doGet(key);
			} catch (InterruptedException ex) {
				LOGGER.error("Exception encountered during get()", ex);
				Thread.currentThread().interrupt();
			}
		}
		return result;
	}

	protected V doGet(K key) throws InterruptedException {
		V result = null;
		// #issue must lock
		// #fix 讀鎖
		readLock.lockInterruptibly();
		try {
			result = cache.get(key);
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during doGet()", ex);
		} finally {
			readLock.unlock();
		}
		return result;
	}

	public V put(K key, V value) {
		V result = null;
		if (!disable.get()) {
			try {
				result = doPut(key, value);
			} catch (InterruptedException ex) {
				LOGGER.error("Exception encountered during put()", ex);
				Thread.currentThread().interrupt();
			}
		}
		return result;
	}

	protected V doPut(K key, V value) throws InterruptedException {
		V result = null;
		// #issue must lock
		// #fix 寫鎖
		writeLock.lockInterruptibly();
		try {
			if (value != null) {
				result = cache.put(key, value);
			} else {
				// 存放value=null的key
				nullValueKeys.add(key);
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during doPut()", ex);
		} finally {
			writeLock.unlock();
		}
		return result;
	}

	public void lock() {
		if (!disable.get()) {
			writeLock.lock();
		}
	}

	public void unlock() {
		if (!disable.get()) {
			writeLock.unlock();
		}
	}

	public void lockInterruptibly() throws InterruptedException {
		if (!disable.get()) {
			writeLock.lockInterruptibly();
		}
	}

	protected boolean isNullValue(K key) {
		boolean result = false;
		try {
			result = doIsNullValue(key);
		} catch (InterruptedException ex) {
			LOGGER.error("Exception encountered during isNullValue()", ex);
			Thread.currentThread().interrupt();
		}
		return result;
	}

	protected boolean doIsNullValue(K key) throws InterruptedException {
		boolean result = false;
		readLock.lockInterruptibly();
		try {
			if (!disable.get()) {
				result = nullValueKeys.contains(key);
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during doIsNullValue()", ex);
		} finally {
			readLock.unlock();
		}
		return result;
	}

	/**
	 * 是否為value!=null的key
	 */
	public boolean isNotNullValue(K key) {
		return !isNullValue(key);
	}

	public boolean isDisable() {
		return disable.get();
	}

	public void setDisable(boolean disable) {
		this.disable.set(disable);
	}

	public void clear() {
		try {
			doClear();
		} catch (InterruptedException ex) {
			LOGGER.error("Exception encountered during clear()", ex);
			Thread.currentThread().interrupt();
		}
	}

	protected void doClear() throws InterruptedException {
		writeLock.lockInterruptibly();
		try {
			cache.clear();
			nullValueKeys.clear();

		} catch (Exception ex) {
			LOGGER.error("Exception encountered during doClear()", ex);
		} finally {
			writeLock.unlock();
		}
	}

	public V remove(K k) {
		V result = null;
		if (!disable.get()) {
			try {
				doRemove(k);
			} catch (InterruptedException ex) {
				LOGGER.error("Exception encountered during remove()", ex);
				Thread.currentThread().interrupt();
			}
		}
		return result;
	}

	public V doRemove(K k) throws InterruptedException {
		V result = null;
		// #issue must lock
		// #fix 寫鎖
		writeLock.lockInterruptibly();
		try {
			if (k != null) {
				if (cache.containsKey(k)) {
					result = cache.remove(k);
				} else if (nullValueKeys.contains(k)) {
					nullValueKeys.remove(k);
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during doRemove()", ex);
		} finally {
			writeLock.unlock();
		}
		return result;
	}

	public boolean contains(K k) {
		return cache.containsKey(k);
	}

	public Map<K, V> getCache() {
		return cache;
	}

	public void setCache(Map<K, V> cache) {
		this.cache = cache;
	}

	public List<K> getKeys() {
		List<K> result = new LinkedList<>();
		for (K key : cache.keySet()) {
			result.add(key);
		}
		return result;
	}

	public List<V> getValues() {
		List<V> result = new LinkedList<>();
		for (V value : cache.values()) {
			result.add(value);
		}
		return result;
	}

	public int size() {
		return cache.size();
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("disable", disable);
		builder.append("cache", cache);
		builder.append("nullValueKeys", nullValueKeys);
		return builder.toString();
	}

	@SuppressWarnings("unchecked")
	public Object clone() {
		MapCacheUtil<K, V> copy = null;
		copy = (MapCacheUtil<K, V>) clone();
		/**
		 * copy.lock = new ReentrantReadWriteLock();
		 * 
		 * copy.readLock = lock.readLock();
		 * 
		 * copy.writeLock = lock.readLock();
		 */
		copy.cache = clone(cache);
		copy.nullValueKeys = clone(nullValueKeys);
		return copy;
	}
	
	


	/**
	 * 物件複製
	 *
	 * @param value
	 * @return 物件
	 */
	public <T> T clone(Object value) {
		T result = null;
		if (value != null) {
			result = CloneUtil.clone(value);
		}
		return result;
	}
}
