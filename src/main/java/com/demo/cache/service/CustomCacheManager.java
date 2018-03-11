package com.demo.cache.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;

public class CustomCacheManager implements CacheManager, InitializingBean {

	private final List<CacheManager> cacheManagers = new ArrayList<>();

	private boolean fallbackToNoOpCache = false;

	/**
	 * jedis factory
	 */
	private String factoryName;
	

	/**
	 * Construct an empty CustomCacheManager, with delegate CacheManagers to be
	 * added via the {@link #setCacheManagers "cacheManagers"} property.
	 */
	public CustomCacheManager() {
	}

	/**
	 * Construct a CustomCacheManager from the given delegate CacheManagers.
	 * 
	 * @param cacheManagers
	 *            the CacheManagers to delegate to
	 */
	public CustomCacheManager(CacheManager... cacheManagers) {
		setCacheManagers(Arrays.asList(cacheManagers));
	}

	public List<CacheManager> getCacheManagers() {
		return cacheManagers;
	}

	/**
	 * Specify the CacheManagers to delegate to.
	 */
	public void setCacheManagers(Collection<CacheManager> cacheManagers) {
		this.cacheManagers.addAll(cacheManagers);
	}

	/**
	 * Indicate whether a {@link NoOpCacheManager} should be added at the end of
	 * the delegate list. In this case, any {@code getCache} requests not
	 * handled by the configured CacheManagers will be automatically handled by
	 * the {@link NoOpCacheManager} (and hence never return {@code null}).
	 */
	public void setFallbackToNoOpCache(boolean fallbackToNoOpCache) {
		this.fallbackToNoOpCache = fallbackToNoOpCache;
	}

	@Override
	public void afterPropertiesSet() {
		if (this.fallbackToNoOpCache) {
			this.cacheManagers.add(new NoOpCacheManager());
		}
	}

	@Override
	public Cache getCache(String name) {
		for (CacheManager cacheManager : this.cacheManagers) {
			Cache cache = cacheManager.getCache(name);
			if (cache != null) {
				return cache;
			}
		}
		return null;
	}

	@Override
	public Collection<String> getCacheNames() {
		Set<String> names = new LinkedHashSet<>();
		for (CacheManager manager : this.cacheManagers) {
			names.addAll(manager.getCacheNames());
		}
		return Collections.unmodifiableSet(names);
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

}
