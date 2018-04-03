package com.demo.cache.aspect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.demo.base.service.AbstractCommonService;
import com.demo.cache.anno.CacheEvict;
import com.demo.cache.anno.CacheLoad;
import com.demo.cache.anno.CachePut;
import com.demo.cache.anno.CacheUpdate;
import com.demo.cache.anno.Cacheable;
import com.demo.cache.anno.Caching;
import com.demo.cache.service.CheckRedisConnectService;
import com.demo.cache.service.CustomCacheManager;
import com.demo.utils.ClassUtil;
import com.demo.utils.CollectionUtil;
import com.demo.utils.NumberUtil;
import com.demo.utils.StringUtil;

import redis.clients.jedis.exceptions.JedisConnectionException;

@Aspect
public class CacheAspect extends AbstractCommonService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheAspect.class);

	@Autowired
	private transient CheckRedisConnectService checkRedisConnectService;

	private transient Map<String, CustomCacheManager> cacheManagers;

	/**
	 * 本地cache manager
	 */
	private transient Map<String, CacheManager> localCacheManagers;

	/**
	 * redis樣板
	 * 
	 * cacheMangerName, redisTemplate
	 */
	private transient Map<String, RedisTemplate<?, ?>> redisTemplates = new HashMap<String, RedisTemplate<?, ?>>();

	/**
	 * 預設存活期限, 秒
	 * 
	 * cacheMangerName, long
	 */
	private transient Map<String, Long> redisDefaultExpirations = new HashMap<String, Long>();

	private transient Map<String, JedisConnectionFactory> jedisConnectionFactorys = new HashMap<String, JedisConnectionFactory>();

	protected void init() {
		super.init();
		//
		this.cacheManagers = applicationContext.getBeansOfType(CustomCacheManager.class);
		this.localCacheManagers = applicationContext.getBeansOfType(CacheManager.class);
		//
		for (Map.Entry<String, CustomCacheManager> entry : cacheManagers.entrySet()) {
			String key = entry.getKey();
			LOGGER.debug("cacheManagers key : " + key);
			CustomCacheManager ncm = entry.getValue();
			for (CacheManager cm : ncm.getCacheManagers()) {
				if (cm instanceof RedisCacheManager) {
					RedisCacheManager rcm = (RedisCacheManager) cm;
					RedisTemplate<?, ?> redisTemplate = (RedisTemplate<?, ?>) ClassUtil.getDeclaredFieldValue(rcm,
							"redisOperations");
					if (redisTemplate != null) {
						redisTemplates.put(key, redisTemplate);
					}
					// 2017/08/31
					// 預設存活期限
					Long defaultExpiration = (Long) ClassUtil.getDeclaredFieldValue(rcm, "defaultExpiration");
					if (defaultExpiration != null) {
						redisDefaultExpirations.put(key, defaultExpiration);
					}
					//
					RedisConnectionFactory rc = redisTemplate.getConnectionFactory();
					if (rc instanceof JedisConnectionFactory) {
						JedisConnectionFactory jrc = (JedisConnectionFactory) rc;
						jedisConnectionFactorys.put(key, jrc);
					}
				}
			}
		}
		//

	}

	public Map<String, CustomCacheManager> getCacheManagers() {
		return cacheManagers;
	}

	public Map<String, CacheManager> getLocalCacheManagers() {
		return localCacheManagers;
	}

	public Map<String, RedisTemplate<?, ?>> getRedisTemplates() {
		return redisTemplates;
	}

	public Map<String, Long> getRedisDefaultExpirations() {
		return redisDefaultExpirations;
	}

	public CustomCacheManager getCacheManager(String cacheManager) {
		return getCacheManager(cacheManager, "");
	}

	// get cache manager
	/**
	 * 取得 cacheManager
	 * 
	 * @param cacheManager
	 * @param dbId
	 * @return
	 */
	public CustomCacheManager getCacheManager(String cacheManager, String dbId) {
		CustomCacheManager result = null;
		String managerName = cacheManager;
		 LOGGER.debug("dbId = " + dbId);
		int intDbId = NumberUtil.toInt(dbId);
		if (intDbId > 1) {
			managerName = cacheManager + "_" + dbId;
		}
		 LOGGER.debug("managerName = " + managerName);
		if (CollectionUtil.isNotEmpty(cacheManagers)) {
			result = cacheManagers.get(managerName);
		}
		return result;
	}

	// get local cache manager
	public CacheManager getLocalCacheManager(String localCacheManager) {
		CacheManager result = null;
		//
		if (StringUtil.notEmpty(localCacheManager) && CollectionUtil.isNotEmpty(localCacheManagers)) {
			result = localCacheManagers.get(localCacheManager);
		}
		return result;
	}

	public RedisTemplate<?, ?> getRedisTemplate(String cacheManager) {
		return redisTemplates.get(cacheManager);
	}

	public long getRedisDefaultExpiration(String cacheManager) {
		Long defaultExpiration = redisDefaultExpirations.get(cacheManager);
		return (defaultExpiration == null ? 0 : defaultExpiration);
	}

	/**
	 * 取得cache
	 * 
	 * @param cacheName
	 * @return
	 */
	public List<Cache> getCaches(String cacheName) {
		List<Cache> result = new ArrayList<Cache>();

		// 判斷cache名稱是否為空
		if (StringUtil.isNullOrEmpty(cacheName))
			return result;

		for (CustomCacheManager ncm : cacheManagers.values()) {
			for (CacheManager cm : ncm.getCacheManagers()) {
				Cache cache = cm.getCache(cacheName);
				if (cache != null) {
					result.add(cache);
				}
			}
		}

		return result;
	}

	// get jedis connection factory
	public JedisConnectionFactory getJedisConnectionFactory(Integer dbId) {
		JedisConnectionFactory result = null;
		if (dbId == null || dbId < 0)
			return result;
		for (JedisConnectionFactory jcf : jedisConnectionFactorys.values()) {
			if (dbId.equals(jcf.getDatabase())) {
				result = jcf;
			}
		}

		return result;
	}

	// get jedis dbId
	public Map<String, String> getJedisDatabaseIndexes() {
		Map<String, String> dbList = new TreeMap<String, String>();
		for (Entry<String, JedisConnectionFactory> jcf : jedisConnectionFactorys.entrySet()) {
			String name = jcf.getKey();
			String dbId = Integer.toString(jcf.getValue().getDatabase());
			if (name != null && dbId != null) {
				dbList.put(name, dbId);
			}
		}
		return dbList;
	}

	/**
	 * 取cache value值
	 * 
	 * @param cacheManager
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public Object getValue(Cache cache, Object key) {
		Object result = null;
		ValueWrapper valueWrapper = cache.get(key);
		if (valueWrapper != null) {
			result = valueWrapper.get();
		}
		return result;
	}

	/**
	 * 放cache value值
	 * 
	 * @param cacheManager
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public void putValue(Cache cache, Object key, Object value) {
		cache.put(key, value);
	}

	// get hash value
	public Object getHashValue(String cacheName, Object key, String cacheManagerName) {
		Object result = null;
		try {
			RedisTemplate<?, ?> redisTemplate = getRedisTemplate(cacheManagerName);
			HashOperations<String, Object, Object> hashOperation = (HashOperations<String, Object, Object>) redisTemplate
					.opsForHash();
			result = hashOperation.get(cacheName, key);
			if (null == result) {
				return null;
			}
		} catch (Exception ex) {
			LOGGER.error("Redis connection exception", ex);
		}
		return result;
	}

	// put hash value
	public void putHashValue(String cacheName, Object key, Object value, String cacheManagerName) {
		try {
			RedisTemplate<?, ?> redisTemplate = getRedisTemplate(cacheManagerName);
			HashOperations<String, Object, Object> hashOperation = (HashOperations<String, Object, Object>) redisTemplate
					.opsForHash();
			hashOperation.put(cacheName, key, value);
		} catch (Exception ex) {
			LOGGER.error("Redis connection exception", ex);
		}
	}

	// remove table list
	public void removeTableList(List<String> nameList, String cacheManagerName) {
		try {
			RedisTemplate<?, ?> redisTemplate = getRedisTemplate(cacheManagerName);
			HashOperations<String, Object, Object> hashOperation = (HashOperations<String, Object, Object>) redisTemplate
					.opsForHash();
			for (String tableName : nameList) {
				Set<Object> keySet = hashOperation.keys(tableName);
				List<Object> keyList = new ArrayList<>(keySet);
				for (Object item : keyList) {
					hashOperation.delete(tableName, item);
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Redis connection exception", ex);
		}
	}

	@Around(value = "@annotation(com.demo.cache.anno.Cacheable)")
	public Object cacheable(ProceedingJoinPoint pjp) throws Throwable {
		Object result = null;
		//
		Cacheable cacheable = null;
		Method method = null;
		Object[] args = null;

		// try {
		// 轉換JoinPoint
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Object targetObject = pjp.getTarget();
		method = targetObject.getClass().getMethod(signature.getMethod().getName(),
				signature.getMethod().getParameterTypes());

		args = pjp.getArgs();

		LOGGER.debug("Cacheable Join point : " + getProceedName(pjp, method));

		// 取得Cacheable annotation 內容
		cacheable = method.getAnnotation(Cacheable.class);
		String dbId = parseKey(cacheable.dbId(), method, args);
		// 檢查redis 連線
		boolean connected = checkConnection(cacheable.cacheManager(), dbId);
		// if (!connected) {
		// return pjp.proceed();
		// }

		result = doCacheable(method, args, cacheable, result, connected);
		// } catch (Exception e) {
		// LOGGER.error("Exception encountered during cacheable()", e);
		// }
		//
		if (result == null) {
			LOGGER.debug("Not in cache, proceed " + getProceedName(pjp, method));
			result = pjp.proceed();
			doCacheable(method, args, cacheable, result, connected);
		}
		//
		// LOGGER.debug("Result : " + result);
		return result;
	}

	@Around(value = "@annotation(com.demo.cache.anno.CacheEvict)")
	public Object cacheEvict(ProceedingJoinPoint pjp) throws Throwable {
		Object result = null;
		CacheEvict cacheEvict = null;
		Method method = null;
		// try {
		// 轉換JoinPoint
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Object targetObject = pjp.getTarget();
		method = targetObject.getClass().getMethod(signature.getMethod().getName(),
				signature.getMethod().getParameterTypes());

		Object[] args = pjp.getArgs();

		LOGGER.debug("CacheEvict Join point : " + getProceedName(pjp, method));

		// 取得CacheEvict annotation 內容
		cacheEvict = method.getAnnotation(CacheEvict.class);

		String dbId = parseKey(cacheEvict.dbId(), method, args);

		// 檢查redis 連線
		boolean connected = checkConnection(cacheEvict.cacheManager(), dbId);
		// if (!connected) {
		// return pjp.proceed();
		// }

		result = doCacheEvict(method, args, cacheEvict, connected);

		// } catch (Exception e) {
		// LOGGER.error("Exception encountered during cacheEvict()", e);
		// }
		LOGGER.debug("Cache evicted, proceed " + getProceedName(pjp, method));
		result = pjp.proceed();
		return result;
	}

	@Around(value = "@annotation(com.demo.cache.anno.CachePut)")
	public Object cachePut(ProceedingJoinPoint pjp) throws Throwable {
		Object result = null;
		CachePut cachePut = null;
		Method method = null;
		Object[] args = null;
		// try {
		// 轉換JoinPoint
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Object targetObject = pjp.getTarget();
		method = targetObject.getClass().getMethod(signature.getMethod().getName(),
				signature.getMethod().getParameterTypes());
		args = pjp.getArgs();
		LOGGER.debug("CachePut Join point : " + getProceedName(pjp, method));

		// } catch (Exception e) {
		// LOGGER.error("Exception encountered during cachePut()", e);
		// }
		// LOGGER.debug("before cache put, execute class :" + method.getName());
		result = pjp.proceed();

		// 取得CachePut annotation 內容
		cachePut = method.getAnnotation(CachePut.class);

		String dbId = parseKey(cachePut.dbId(), method, args);

		// 檢查redis 連線
		boolean connected = checkConnection(cachePut.cacheManager(), dbId);
		// if (!connected) {
		// return result;
		// }
		//
		doCachePut(method, args, cachePut, result, connected);
		return result;
	}

	@Around(value = "@annotation(com.demo.cache.anno.CacheUpdate)")
	public Object cacheUpdate(ProceedingJoinPoint pjp) throws Throwable {
		Object result = null;
		CacheUpdate cacheUpdate = null;
		Method method = null;
		Object[] args = null;
		// 轉換JoinPoint
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Object targetObject = pjp.getTarget();
		method = targetObject.getClass().getMethod(signature.getMethod().getName(),
				signature.getMethod().getParameterTypes());
		args = pjp.getArgs();
		LOGGER.debug("CacheUpdate Join point : " + getProceedName(pjp, method));
		//
		result = pjp.proceed();
		// 取得CacheUpdate annotation 內容
		cacheUpdate = method.getAnnotation(CacheUpdate.class);

		String dbId = parseKey(cacheUpdate.dbId(), method, args);

		// 檢查redis 連線
		boolean connected = checkConnection(cacheUpdate.cacheManager(), dbId);
		// if (!connected) {
		// return result;
		// }
		//
		doCacheUpdate(method, args, cacheUpdate, result, connected);
		return result;
	}

	@Around(value = "@annotation(com.demo.cache.anno.Caching)")
	public Object caching(ProceedingJoinPoint pjp) throws Throwable {
		Object result = null;
		// try {
		// 轉換JoinPoint
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Object targetObject = pjp.getTarget();
		Method method = targetObject.getClass().getMethod(signature.getMethod().getName(),
				signature.getMethod().getParameterTypes());

		Object[] args = pjp.getArgs();

		LOGGER.debug("Caching Join point : " + getProceedName(pjp, method));

		// 取得Caching annotation 內容
		Caching caching = method.getAnnotation(Caching.class);

		if (caching.cacheable().length > 0) {
			for (Cacheable cacheable : caching.cacheable()) {

				String dbId = parseKey(cacheable.dbId(), method, args);
				// 檢查redis 連線
				boolean connected = checkConnection(cacheable.cacheManager(), dbId);
				// if (!connected) {
				// continue;
				// }
				Object cacheObject = doCacheable(method, args, cacheable, result, connected);
				result = cacheObject;
				if (cacheObject == null) {
					if (result == null) {
						result = pjp.proceed();
					}
					cacheObject = doCacheable(method, args, cacheable, result, connected);
				}
			}
		}

		if (caching.evict().length > 0) {
			for (CacheEvict cacheEvict : caching.evict()) {

				String dbId = parseKey(cacheEvict.dbId(), method, args);
				// 檢查redis 連線
				boolean connected = checkConnection(cacheEvict.cacheManager(), dbId);
				// if (!connected) {
				// continue;
				// }
				doCacheEvict(method, args, cacheEvict, connected);
			}
			if (result == null) {
				result = pjp.proceed();
			}

		}

		if (caching.put().length > 0) {
			if (result == null) {
				result = pjp.proceed();
			}

			for (CachePut cachePut : caching.put()) {

				String dbId = parseKey(cachePut.dbId(), method, args);
				// 檢查redis 連線
				boolean connected = checkConnection(cachePut.cacheManager(), dbId);
				// if (!connected) {
				// continue;
				// }
				doCachePut(method, args, cachePut, result, connected);
			}
		}

		if (caching.update().length > 0) {
			if (result == null) {
				result = pjp.proceed();
			}

			for (CacheUpdate cacheUpdate : caching.update()) {

				String dbId = parseKey(cacheUpdate.dbId(), method, args);
				// 檢查redis 連線
				boolean connected = checkConnection(cacheUpdate.cacheManager(), dbId);
				// if (!connected) {
				// continue;
				// }
				doCacheUpdate(method, args, cacheUpdate, result, connected);
			}
		}

		if (caching.load().length > 0) {
			if (result == null) {
				result = pjp.proceed();
			}

			for (CacheLoad cacheLoad : caching.load()) {

				String dbId = parseKey(cacheLoad.dbId(), method, args);
				// 檢查redis 連線
				boolean connected = checkConnection(cacheLoad.cacheManager(), dbId);
				// if (!connected) {
				// continue;
				// }
				doCacheLoad(method, args, cacheLoad, result, connected);
			}
		}

		// } catch (Exception e) {
		// LOGGER.error("Exception encountered during caching()", e);
		// }
		return result;
	}

	@Around(value = "@annotation(com.demo.cache.anno.CacheLoad)")
	public Object cacheLoad(ProceedingJoinPoint pjp) throws Throwable {
		Object result = null;
		//
		CacheLoad cacheLoad = null;
		Method method = null;
		Object[] args = null;

		// 轉換JoinPoint
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Object targetObject = pjp.getTarget();
		method = targetObject.getClass().getMethod(signature.getMethod().getName(),
				signature.getMethod().getParameterTypes());

		args = pjp.getArgs();

		LOGGER.debug("CacheLoad Join point : " + getProceedName(pjp, method));

		result = pjp.proceed();

		// 取得CachePut annotation 內容
		cacheLoad = method.getAnnotation(CacheLoad.class);

		String dbId = parseKey(cacheLoad.dbId(), method, args);

		// 檢查redis 連線
		boolean connected = checkConnection(cacheLoad.cacheManager(), dbId);
		// if (!connected) {
		// return result;
		// }
		//
		doCacheLoad(method, args, cacheLoad, result, connected);
		return result;
	}

	@SuppressWarnings("unused")
	protected Object doCacheable(Method method, Object[] args, Cacheable cacheable, Object pjpResult,
			boolean isRedisConnect) {
		Object result = null;
		String key = null;
		CustomCacheManager cacheManager = null;
		String managerName = null;
		// local cache manager
		CacheManager localCacheManager = null;
		String localManagerName = null;
		try {
			managerName = cacheable.cacheManager();
			 LOGGER.debug("Cache Manager Name: " + managerName);

			String dbId = parseKey(cacheable.dbId(), method, args);
			cacheManager = getCacheManager(managerName, dbId);

			// 2016/08/29
			localManagerName = cacheable.localCacheManager();
			localCacheManager = getLocalCacheManager(localManagerName);
			//
			key = cacheable.key();
			if (StringUtil.contains(cacheable.key(), "#")) {
				key = parseKey(cacheable.key(), method, args);
			}

			// key 忽略大小寫，統一轉小寫
			if (cacheable.ignoreCase()) {
				// LOGGER.debug("Cacheable key ignore case.");
				key = key.toLowerCase();
			}

			// service_service_name:box.android.prd
			LOGGER.debug("Key : " + key);
			// 取出cache
			if (cacheable.cacheNames().length > 0) {
				for (String cacheName : cacheable.cacheNames()) {
					LOGGER.debug("CacheName : " + cacheName);
					//
					Cache cache = cacheManager.getCache(cacheName);
					Cache localCache = null;
					if (pjpResult == null) {
						// 2016/08/29
						// 有 local cache manager
						if (localCacheManager != null) {
							localCache = localCacheManager.getCache(cacheName);
							synchronized (localCache) {
								result = getValue(localCache, key);
								// 有 local cache
								if (result != null) {
									LOGGER.debug(method.getName() + "(), Get from local");
									return result;
								} else {
									// 無 local cache時, 取 redis cache, 再放回local
									// cache
									// redis耗時
									if (isRedisConnect) { // 確認redis 連線
										LOGGER.debug(method.getName() + "(), Get from cache");
										result = getValue(cache, key);
										// 放local cache
										if (result != null) {
											putValue(localCache, key, result);
										}
									}
								}
							}
						} else {
							// 無 local cache manager
							if (isRedisConnect) { // 確認redis 連線
								LOGGER.debug(method.getName() + "(), Get from cache");
								if (cacheable.hashFlag()) {
									cacheName = parseKey(cacheName, method, args);
									result = getHashValue(cacheName, key, cacheable.cacheManager());
								} else {
									result = getValue(cache, key);
								}
							}
						}
					} else {
						//
						result = pjpResult;
						if (localCacheManager != null) {
							localCache = localCacheManager.getCache(cacheName);
							if (localCache != null) {
								synchronized (localCache) {
									LOGGER.debug(method.getName() + "(), Put to local");
									putValue(localCache, key, pjpResult);
								}
							}
						}
						if (isRedisConnect) { // 確認redis 連線
							LOGGER.debug(method.getName() + "(), Put to cache");
							if (cacheable.hashFlag()) {
								cacheName = parseKey(cacheName, method, args);
								putHashValue(cacheName, key, pjpResult, cacheable.cacheManager());
							} else {
								putValue(cache, key, pjpResult);
							}
						}
					}
				}
			}
		} catch (JedisConnectionException e) {
			String factoryName = "";
			if (managerName != null && cacheManager != null) {
				factoryName = cacheManager.getFactoryName();
				checkRedisConnectService.setConnectState(factoryName, false);
			}
			LOGGER.warn("'" + factoryName + "' Connection is lost");
		} catch (RedisConnectionFailureException e) {
			String factoryName = "";
			if (managerName != null && cacheManager != null) {
				factoryName = cacheManager.getFactoryName();
				checkRedisConnectService.setConnectState(factoryName, false);
			}
			LOGGER.warn("'" + factoryName + "' Connection is lost");
		} catch (Exception e) {
			LOGGER.error("Exception encountered during doCacheable()", e);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	protected Object doCacheEvict(Method method, Object[] args, CacheEvict cacheEvict, boolean isRedisConnect) {
		Object result = null;
		String key = null;
		CustomCacheManager cacheManager = null;
		String managerName = null;
		// local cache manager
		CacheManager localCacheManager = null;
		String localManagerName = null;
		try {
			managerName = cacheEvict.cacheManager();
			LOGGER.debug("Cache Manager : " + managerName);
			String dbId = parseKey(cacheEvict.dbId(), method, args);
			cacheManager = getCacheManager(managerName, dbId);

			// 2016/09/02
			localManagerName = cacheEvict.localCacheManager();
			localCacheManager = getLocalCacheManager(localManagerName);
			//
			key = cacheEvict.key();
			if (StringUtil.contains(cacheEvict.key(), "#") || StringUtil.contains(cacheEvict.key(), "T(")) {
				key = parseKey(cacheEvict.key(), method, args);
			}

			List<Object> keys = null;

			if (StringUtil.isNotEmpty(cacheEvict.keys())) {
				keys = (List<Object>) parseValue(cacheEvict.keys(), method, args);
				LOGGER.debug("keys : " + keys);
			}

			// key 忽略大小寫，統一轉小寫
			if (cacheEvict.ignoreCase()) {
				// LOGGER.debug("CacheEvict key ignore case.");
				key = key.toLowerCase();
			}
			LOGGER.debug("Key : " + key);
			// 移除cache
			// if (cacheManager != null && cacheEvict != null &&
			// cacheEvict.cacheNames().length > 0 && key != null) {
			if (cacheEvict.cacheNames().length > 0) {
				for (String cacheName : cacheEvict.cacheNames()) {
					LOGGER.debug("CacheName : " + cacheName);
					if (CollectionUtil.isNotEmpty(keys)) {
						List<String> keyList = new ArrayList<String>();
						if (cacheEvict.hashFlag()) {
							keyList.add(parseKey(cacheName, method, args));
						}
						for (Object keyValue : keys) {
							String keyValueStr = String.valueOf(keyValue);
							if (cacheEvict.ignoreCase()) {
								keyValueStr = keyValueStr.toLowerCase();
							}
							String fullKey = key + keyValueStr;
							LOGGER.debug("Key : " + fullKey);
							if (localCacheManager != null) {
								Cache localCache = localCacheManager.getCache(cacheName);
								synchronized (localCache) {
									localCache.evict(fullKey);
								}
							}
							if (cacheEvict.hashFlag()) {
								keyList.add(fullKey);
							}
							//
							if (isRedisConnect && !cacheEvict.hashFlag()) { // 確認redis 連線
								cacheManager.getCache(cacheName).evict(fullKey);
							}
						}
						if (cacheEvict.hashFlag() && isRedisConnect) {
							removeTableList(keyList, cacheEvict.cacheManager());
						}
					} else {
						if (localCacheManager != null) {
							Cache localCache = localCacheManager.getCache(cacheName);
							synchronized (localCache) {
								localCache.evict(key);
							}
						}
						if (isRedisConnect) { // 確認redis 連線
							if (cacheEvict.hashFlag()) {
								List<String> cacheNameList = new ArrayList<String>();
								cacheNameList.add(parseKey(cacheName, method, args));
								removeTableList(cacheNameList, cacheEvict.cacheManager());
							} else {
								cacheManager.getCache(cacheName).evict(key);
							}
						}
					}
				}

			}
		} catch (JedisConnectionException e) {
			String factoryName = "";
			if (managerName != null && cacheManager != null) {
				factoryName = cacheManager.getFactoryName();
				checkRedisConnectService.setConnectState(factoryName, false);
			}
			LOGGER.warn("'" + factoryName + "' Connection is lost");
		} catch (RedisConnectionFailureException e) {
			String factoryName = "";
			if (managerName != null && cacheManager != null) {
				factoryName = cacheManager.getFactoryName();
				checkRedisConnectService.setConnectState(factoryName, false);
			}
			LOGGER.warn("'" + factoryName + "' Connection is lost");
		} catch (Exception e) {
			LOGGER.error("Exception encountered during doCacheEvict()", e);
		}
		return result;
	}

	protected Object doCachePut(Method method, Object[] args, CachePut cachePut, Object result,
			boolean isRedisConnect) {
		String key = null;
		CustomCacheManager cacheManager = null;
		String managerName = null;
		// local cache manager
		CacheManager localCacheManager = null;
		String localManagerName = null;
		if (result != null) {
			try {
				managerName = cachePut.cacheManager();
				LOGGER.debug("Cache Manager : " + managerName);
				String dbId = parseKey(cachePut.dbId(), method, args);
				cacheManager = getCacheManager(managerName, dbId);

				// 2016/09/02
				localManagerName = cachePut.localCacheManager();
				localCacheManager = getLocalCacheManager(localManagerName);

				key = cachePut.key();
				if (StringUtil.contains(cachePut.key(), "#")) {
					key = parseKey(cachePut.key(), method, args);
				}
				// key 忽略大小寫，統一轉小寫
				if (cachePut.ignoreCase()) {
					// LOGGER.debug("CachePut key ignore case.");
					key = key.toLowerCase();
				}

				LOGGER.debug("Key : " + key);

				// 寫入 redis
				// if (cacheManager != null && cachePut != null &&
				// cachePut.cacheNames().length > 0 && key != null) {
				if (cachePut.cacheNames().length > 0) {
					for (String cacheName : cachePut.cacheNames()) {
						LOGGER.debug("cacheName : " + cacheName);
						if (localCacheManager != null) {
							Cache localCache = localCacheManager.getCache(cacheName);
							synchronized (localCache) {
								localCache.put(key, result);
							}
						}
						if (isRedisConnect) { // 確認redis 連線
							if (cachePut.hashFlag()) {
								putHashValue(cacheName, key, result, cachePut.cacheManager());
							} else {
								cacheManager.getCache(cacheName).put(key, result);
							}
						}
					}
				}
			} catch (JedisConnectionException e) {
				String factoryName = "";
				if (managerName != null && cacheManager != null) {
					factoryName = cacheManager.getFactoryName();
					checkRedisConnectService.setConnectState(factoryName, false);
				}
				LOGGER.warn("'" + factoryName + "' Connection is lost");
			} catch (RedisConnectionFailureException e) {
				String factoryName = "";
				if (managerName != null && cacheManager != null) {
					factoryName = cacheManager.getFactoryName();
					checkRedisConnectService.setConnectState(factoryName, false);
				}
				LOGGER.warn("'" + factoryName + "' Connection is lost");
			} catch (Exception e) {
				LOGGER.error("Exception encountered during doCachePut()", e);
			}
		}
		return result;
	}

	protected Object doCacheUpdate(Method method, Object[] args, CacheUpdate cacheUpdate, Object result,
			boolean isRedisConnect) {
		String key = null;
		CustomCacheManager cacheManager = null;
		String managerName = null;
		// local cache manager
		CacheManager localCacheManager = null;
		String localManagerName = null;
		try {
			managerName = cacheUpdate.cacheManager();
			LOGGER.debug("Cache Manager : " + managerName);
			String dbId = parseKey(cacheUpdate.dbId(), method, args);
			cacheManager = getCacheManager(managerName, dbId);

			// 2016/09/02
			localManagerName = cacheUpdate.localCacheManager();
			localCacheManager = getLocalCacheManager(localManagerName);

			key = cacheUpdate.key();
			if (StringUtil.contains(cacheUpdate.key(), "#")) {
				key = parseKey(cacheUpdate.key(), method, args);
			}
			// key 忽略大小寫，統一轉小寫
			if (cacheUpdate.ignoreCase()) {
				// LOGGER.debug("CachePut key ignore case.");
				key = key.toLowerCase();
			}
			LOGGER.debug("Key : " + key);

			// 寫入 redis
			// if (cacheManager != null && cacheUpdate != null &&
			// cacheUpdate.cacheNames().length > 0 && key != null) {
			if (cacheUpdate.cacheNames().length > 0) {
				for (String cacheName : cacheUpdate.cacheNames()) {
					LOGGER.debug("cacheName : " + cacheName);
					// cacheManager.getCache(cacheName).put(key, result);
					Cache cache = cacheManager.getCache(cacheName);
					ValueWrapper vw = cache.get(key);
					if (vw == null) {
						LOGGER.warn("Key '" + key + "' does not exist in cache");
						return null;
					} else {
						Object object = vw.get();
						Class<?> clazz = object.getClass();
						String[] values = cacheUpdate.values();
						for (String value : values) {
							int pos = value.indexOf("#");
							if (pos < 0) {
								continue;
							}
							String fieldName = new StringBuilder(value.substring(pos + 1)).toString();
							Field field = ClassUtil.getDeclaredField(clazz, fieldName);
							if (field == null) {
								LOGGER.warn("Field '" + fieldName + "' does not exist in " + clazz.getName());
								continue;
							}
							//
							Object objectValue = parseValue(value, method, args);
							if (objectValue == null) {
								LOGGER.warn("Value '" + value + "' is null. Maybe method args missing");
								continue;
							}
							ClassUtil.setFieldValue(object, field, objectValue);
						}
						//
						if (localCacheManager != null) {
							Cache localCache = localCacheManager.getCache(cacheName);
							synchronized (localCache) {
								localCache.put(key, object);
							}
						}
						// 更新cache
						if (isRedisConnect) { // 確認redis 連線
							cache.put(key, object);
						}
					}
				}
			}
		} catch (JedisConnectionException e) {
			String factoryName = "";
			if (managerName != null && cacheManager != null) {
				factoryName = cacheManager.getFactoryName();
				checkRedisConnectService.setConnectState(factoryName, false);
			}
			LOGGER.warn("'" + factoryName + "' Connection is lost");
		} catch (RedisConnectionFailureException e) {
			String factoryName = "";
			if (managerName != null && cacheManager != null) {
				factoryName = cacheManager.getFactoryName();
				checkRedisConnectService.setConnectState(factoryName, false);
			}
			LOGGER.warn("'" + factoryName + "' Connection is lost");
		} catch (Exception e) {
			LOGGER.error("Exception encountered during doCacheUpdate()", e);
		}
		return result;
	}

	protected Object doCacheLoad(Method method, Object[] args, CacheLoad cacheLoad, Object result,
			boolean isRedisConnect) {
		String key = null;
		CustomCacheManager cacheManager = null;
		String managerName = null;
		if (result != null) {
			try {
				managerName = cacheLoad.cacheManager();
				LOGGER.debug("Cache Manager : " + managerName);
				String dbId = parseKey(cacheLoad.dbId(), method, args);
				cacheManager = getCacheManager(managerName, dbId);

				key = cacheLoad.key();
				if (StringUtil.contains(cacheLoad.key(), "#")) {
					key = parseKey(cacheLoad.key(), method, args);
				}
				// key 忽略大小寫，統一轉小寫
				if (cacheLoad.ignoreCase()) {
					// LOGGER.debug("CacheLoad key ignore case.");
					key = key.toLowerCase();
				}

				LOGGER.debug("Key : " + key);

				// TODO @CacheLoad暫無實作
				// cacheManager+localCacheManger

				// // 寫入 redis
				// if (cacheManager != null && cachePut != null &&
				// cachePut.cacheNames().length > 0 && key != null) {
				// for (String cacheName : cachePut.cacheNames()) {
				// LOGGER.debug("cacheName : " + cacheName);
				// cacheManager.getCache(cacheName).put(key, result);
				// }
				// }

			} catch (JedisConnectionException e) {
				String factoryName = "";
				if (managerName != null && cacheManager != null) {
					factoryName = cacheManager.getFactoryName();
					checkRedisConnectService.setConnectState(factoryName, false);
				}
				LOGGER.warn("'" + factoryName + "' Connection is lost");
			} catch (RedisConnectionFailureException e) {
				String factoryName = "";
				if (managerName != null && cacheManager != null) {
					factoryName = cacheManager.getFactoryName();
					checkRedisConnectService.setConnectState(factoryName, false);
				}
				LOGGER.warn("'" + factoryName + "' Connection is lost");
			} catch (Exception e) {
				LOGGER.error("Exception encountered during doCacheLoad()", e);
			}
		}
		return result;
	}

	public String parseKey(String key, Method method, Object[] args) {
		// 獲取被攔截方法參數名列表
		LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
		String[] paraNameArr = u.getParameterNames(method);
		// 使用SPEL進行key的解析
		ExpressionParser parser = new SpelExpressionParser();
		// SPEL上下文
		StandardEvaluationContext context = new StandardEvaluationContext();
		// 把方法參數放入SPEL上下文中
		for (int i = 0; i < paraNameArr.length; i++) {
			context.setVariable(paraNameArr[i], args[i]);
		}
		return parser.parseExpression(key).getValue(context, String.class);
	}

	public Object parseValue(String value, Method method, Object[] args) {
		// 獲取被攔截方法參數名列表
		LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
		String[] paraNameArr = u.getParameterNames(method);
		// 使用SPEL進行key的解析
		ExpressionParser parser = new SpelExpressionParser();
		// SPEL上下文
		StandardEvaluationContext context = new StandardEvaluationContext();
		// 把方法參數放入SPEL上下文中
		for (int i = 0; i < paraNameArr.length; i++) {
			context.setVariable(paraNameArr[i], args[i]);
		}
		return parser.parseExpression(value).getValue(context, Object.class);
	}

	public boolean checkConnection(String managerName) {
		return checkConnection(managerName, "");
	}

	public boolean checkConnection(String managerName, String dbId) {
		boolean result = false;
		LOGGER.debug("managerName = " + managerName + ", dbId = " + dbId);
		CustomCacheManager cacheManager = getCacheManager(managerName, dbId);
		if (cacheManager != null) {
			String factoryName = cacheManager.getFactoryName();
			result = checkRedisConnectService.getConnectState(factoryName);
			LOGGER.debug("Check " + factoryName + ", " + result);
		}
		return result;
	}

	public String getProceedName(ProceedingJoinPoint pjp, Method method) {
		return pjp.getTarget().getClass().getSimpleName() + "." + method.getName() + "()";
	}

}
