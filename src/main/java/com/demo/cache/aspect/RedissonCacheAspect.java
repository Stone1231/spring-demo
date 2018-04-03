package com.demo.cache.aspect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.demo.base.service.AbstractCommonService;
import com.demo.cache.anno2.CacheEvict;
import com.demo.cache.anno2.CacheLoad;
import com.demo.cache.anno2.CachePut;
import com.demo.cache.anno2.CacheUpdate;
import com.demo.cache.anno2.Cacheable;
import com.demo.cache.anno2.Caching;
import com.demo.cache.service.RedissonManager;
import com.demo.cache.service.impl.CheckRedissonConnectServiceImpl;
import com.demo.utils.ClassUtil;
import com.demo.utils.CollectionUtil;
import com.demo.utils.NumberUtil;
import com.demo.utils.StringUtil;

@Aspect
public class RedissonCacheAspect extends AbstractCommonService {

	private static transient final Logger LOGGER = LogManager.getLogger(RedissonCacheAspect.class);

	@Autowired
	@Qualifier("checkRedissonConnectService")
	private transient CheckRedissonConnectServiceImpl checkRedissonConnectService;

	private transient Map<String, RedissonManager> cacheManagers;

	/**
	 * redisMode = 1:sentinel , 2:cluster defualt 1
	 */
	private int redisMode = 1;

	// private String clusterCacheManagerName = "clusterCacheManager";

	// /**
	// * redission client
	// *
	// * cacheMangerName, redissonClient
	// */
	// private transient Map<String, RedissonClient> redissonClients = new
	// HashMap<String, RedissonClient>();

	protected void init() {
		super.init();

		this.cacheManagers = applicationContext.getBeansOfType(RedissonManager.class);
		//
		// for (Map.Entry<String, RedissonManager> entry :
		// cacheManagers.entrySet()) {
		// String key = entry.getKey();
		// RedissonManager rm = entry.getValue();
		// RedissonClient redissonClient = rm.getRedissonClient();
		// if (redissonClient != null) {
		// redissonClients.put(key, redissonClient);
		// }
		// }
	}

	// public Map<String, RedissonManager> getCacheManagers() {
	// return cacheManagers;
	// }
	//
	// public Map<String, RedissonClient> getRedissonClients() {
	// return redissonClients;
	// }

	// public RedissonManager getCacheManager(String cacheManager) {
	// return getCacheManager(cacheManager, "");
	// }

	public void setRedisMode(int redisMode) {
		this.redisMode = redisMode;
	}

	// get cache manager
	/**
	 * 取得 cacheManager
	 * 
	 * @param cacheManager
	 * @param dbId
	 * @return
	 */
	private RedissonManager getCacheManager(String cacheManager, String dbId) {
		RedissonManager result = null;
		String managerName = cacheManager;
		if (redisMode == 2) {
			// managerName = clusterCacheManagerName;
		} else {
			// LOGGER.debug("dbId = " + dbId);
			int intDbId = NumberUtil.toInt(dbId);
			if (intDbId > 1) {
				managerName = cacheManager + "_" + dbId;
			}

		}

		if (CollectionUtil.isNotEmpty(cacheManagers)) {
			result = cacheManagers.get(managerName);
		}

		return result;
	}

	// public RedissonClient getRedissonClient(String cacheManager) {
	// return redissonClients.get(cacheManager);
	// }

	// /**
	// * 取得cache
	// *
	// * @param cacheName
	// * @return
	// */
	// public List<Cache> getCaches(String cacheName) {
	// List<Cache> result = new ArrayList<Cache>();
	//
	// // 判斷cache名稱是否為空
	// if (StringUtil.isNullOrEmpty(cacheName))
	// return result;
	//
	// for (RedissonManager rm : cacheManagers.values()) {
	// Cache cache = rm.getCache(cacheName);
	// if (cache != null) {
	// result.add(cache);
	// }
	// }
	//
	// return result;
	// }

	/**
	 * 取cache value值
	 * 
	 * @param cacheManager
	 * @param cacheName
	 * @param key
	 * @return
	 */
	private Object getValue(Cache cache, Object key) {
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
	private void putValue(Cache cache, Object key, Object value) {
		cache.put(key, value);
	}

	@Around(value = "@annotation(com.demo.cache.anno2.Cacheable)")
	public Object cacheable(ProceedingJoinPoint pjp) throws Throwable {

		Method method = getMethod(pjp);
		LOGGER.debug("Redisson Cacheable Join point : " + getProceedName(pjp, method));

		Cacheable cacheable = method.getAnnotation(Cacheable.class);

		Object[] args = pjp.getArgs();

		String dbId = parseKey(cacheable.dbId(), method, args);

		boolean connected = checkConnection(cacheable.cacheManager(), dbId);

		Object result = null;
		result = doCacheable(method, args, cacheable, result, connected);

		if (result == null) {
			LOGGER.debug("Not in cache, proceed " + getProceedName(pjp, method));
			result = pjp.proceed();
			doCacheable(method, args, cacheable, result, connected);
		}

		return result;
	}

	@Around(value = "@annotation(com.demo.cache.anno2.CacheEvict)")
	public Object cacheEvict(ProceedingJoinPoint pjp) throws Throwable {

		Method method = getMethod(pjp);
		LOGGER.debug("CacheEvict Join point : " + getProceedName(pjp, method));

		CacheEvict cacheEvict = method.getAnnotation(CacheEvict.class);

		Object[] args = pjp.getArgs();

		String dbId = parseKey(cacheEvict.dbId(), method, args);

		boolean connected = checkConnection(cacheEvict.cacheManager(), dbId);

		Object result = doCacheEvict(method, args, cacheEvict, connected);

		LOGGER.debug("Cache evicted, proceed " + getProceedName(pjp, method));

		result = pjp.proceed();

		return result;
	}

	@Around(value = "@annotation(com.demo.cache.anno2.CachePut)")
	public Object cachePut(ProceedingJoinPoint pjp) throws Throwable {

		Method method = getMethod(pjp);
		LOGGER.debug("CachePut Join point : " + getProceedName(pjp, method));

		CachePut cachePut = method.getAnnotation(CachePut.class);

		Object[] args = pjp.getArgs();

		Object result = pjp.proceed();

		String dbId = parseKey(cachePut.dbId(), method, args);

		boolean connected = checkConnection(cachePut.cacheManager(), dbId);

		doCachePut(method, args, cachePut, result, connected);

		return result;
	}

	@Around(value = "@annotation(com.demo.cache.anno2.CacheUpdate)")
	public Object cacheUpdate(ProceedingJoinPoint pjp) throws Throwable {

		Method method = getMethod(pjp);
		LOGGER.debug("CacheUpdate Join point : " + getProceedName(pjp, method));

		CacheUpdate cacheUpdate = method.getAnnotation(CacheUpdate.class);

		Object[] args = pjp.getArgs();

		Object result = pjp.proceed();

		String dbId = parseKey(cacheUpdate.dbId(), method, args);

		boolean connected = checkConnection(cacheUpdate.cacheManager(), dbId);

		doCacheUpdate(method, args, cacheUpdate, result, connected);

		return result;
	}

	@Around(value = "@annotation(com.demo.cache.anno2.Caching)")
	public Object caching(ProceedingJoinPoint pjp) throws Throwable {

		Method method = getMethod(pjp);
		LOGGER.debug("Caching Join point : " + getProceedName(pjp, method));

		Caching caching = method.getAnnotation(Caching.class);

		Object result = null;

		Object[] args = pjp.getArgs();

		if (caching.cacheable().length > 0) {
			for (Cacheable cacheable : caching.cacheable()) {
				String dbId = parseKey(cacheable.dbId(), method, args);

				boolean connected = checkConnection(cacheable.cacheManager(), dbId);

				Object cacheObject = doCacheable(method, args, cacheable, result, connected);// ???result

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

				boolean connected = checkConnection(cacheEvict.cacheManager(), dbId);

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

				boolean connected = checkConnection(cachePut.cacheManager(), dbId);

				doCachePut(method, args, cachePut, result, connected);
			}
		}

		if (caching.update().length > 0) {
			if (result == null) {
				result = pjp.proceed();
			}

			for (CacheUpdate cacheUpdate : caching.update()) {

				String dbId = parseKey(cacheUpdate.dbId(), method, args);

				boolean connected = checkConnection(cacheUpdate.cacheManager(), dbId);

				doCacheUpdate(method, args, cacheUpdate, result, connected);
			}
		}

		if (caching.load().length > 0) {
			if (result == null) {
				result = pjp.proceed();
			}

			for (CacheLoad cacheLoad : caching.load()) {

				String dbId = parseKey(cacheLoad.dbId(), method, args);

				boolean connected = checkConnection(cacheLoad.cacheManager(), dbId);

				doCacheLoad(method, args, cacheLoad, result, connected);
			}
		}

		return result;
	}

	@Around(value = "@annotation(com.demo.cache.anno2.CacheLoad)")
	public Object cacheLoad(ProceedingJoinPoint pjp) throws Throwable {

		Method method = getMethod(pjp);
		LOGGER.debug("CacheLoad Join point : " + getProceedName(pjp, method));

		CacheLoad cacheLoad = method.getAnnotation(CacheLoad.class);

		Object[] args = pjp.getArgs();

		Object result = pjp.proceed();

		String dbId = parseKey(cacheLoad.dbId(), method, args);

		boolean connected = checkConnection(cacheLoad.cacheManager(), dbId);

		doCacheLoad(method, args, cacheLoad, result, connected);
		return result;
	}

	private Method getMethod(ProceedingJoinPoint pjp) throws Throwable {
		Method method = null;
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Object targetObject = pjp.getTarget();
		method = targetObject.getClass().getMethod(signature.getMethod().getName(),
				signature.getMethod().getParameterTypes());

		return method;
	}

	@SuppressWarnings("unused")
	private Object doCacheable(Method method, Object[] args, Cacheable cacheable, Object pjpResult,
			boolean isRedisConnect) {
		Object result = null;
		String key = null;
		RedissonManager cacheManager = null;
		String managerName = null;

		try {
			managerName = cacheable.cacheManager();

			String dbId = parseKey(cacheable.dbId(), method, args);
			cacheManager = getCacheManager(managerName, dbId);

			key = cacheable.key();
			if (StringUtil.contains(cacheable.key(), "#")) {
				key = parseKey(cacheable.key(), method, args);
			}

			if (cacheable.ignoreCase()) {
				key = key.toLowerCase();
			}

			LOGGER.debug("Key : " + key);
			// 取出cache
			if (cacheable.cacheNames().length > 0) {
				for (String cacheName : cacheable.cacheNames()) {

					LOGGER.info("CacheName : " + cacheName);

					Cache cache = cacheManager.getCache(cacheName);
					if (pjpResult == null) {
						if (isRedisConnect) { // 確認redis 連線
							LOGGER.debug(method.getName() + "(), Get from cache");
							if (cacheable.hashFlag()) {
								cacheName = parseKey(cacheName, method, args);
							} else {
								result = getValue(cache, key);
							}
						}
					} else {
						result = pjpResult;
						if (isRedisConnect) { // 確認redis 連線
							LOGGER.debug(method.getName() + "(), Put to cache");
							putValue(cache, key, pjpResult);
						}
					}
				}
			}
		} catch (RedisConnectionFailureException e) {
			String clientName = "";
			if (managerName != null && cacheManager != null) {
				clientName = cacheManager.getClientName();
			}
			LOGGER.warn("'" + clientName + "' Connection is lost");
		} catch (RedisException e) {
			LOGGER.error("RedisException encountered during doCacheable()", e);
		} catch (Exception e) {
			LOGGER.error("Exception encountered during doCacheable()", e);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private Object doCacheEvict(Method method, Object[] args, CacheEvict cacheEvict, boolean isRedisConnect) {
		Object result = null;
		String key = null;
		RedissonManager cacheManager = null;
		String managerName = null;

		try {
			managerName = cacheEvict.cacheManager();
			LOGGER.debug("Cache Manager : " + managerName);
			String dbId = parseKey(cacheEvict.dbId(), method, args);
			cacheManager = getCacheManager(managerName, dbId);

			key = cacheEvict.key();
			if (StringUtil.contains(cacheEvict.key(), "#") || StringUtil.contains(cacheEvict.key(), "T(")) {
				key = parseKey(cacheEvict.key(), method, args);
			}

			List<Object> keys = null;

			if (StringUtil.isNotEmpty(cacheEvict.keys())) {
				keys = (List<Object>) parseValue(cacheEvict.keys(), method, args);
				LOGGER.debug("keys : " + keys);
			}

			if (cacheEvict.ignoreCase()) {
				key = key.toLowerCase();
			}

			LOGGER.debug("Key : " + key);
			// 移除cache
			if (cacheEvict.cacheNames().length > 0) {
				for (String cacheName : cacheEvict.cacheNames()) {
					LOGGER.debug("CacheName : " + cacheName);
					if (CollectionUtil.isNotEmpty(keys)) {
						for (Object keyValue : keys) {
							String keyValueStr = String.valueOf(keyValue);
							if (cacheEvict.ignoreCase()) {
								keyValueStr = keyValueStr.toLowerCase();
							}
							String fullKey = key + keyValueStr;

							if (isRedisConnect && !cacheEvict.hashFlag()) {
								cacheManager.getCache(cacheName).evict(fullKey);
							}
						}
					} else {
						if (isRedisConnect && !cacheEvict.hashFlag()) {
							cacheManager.getCache(cacheName).evict(key);
						}
					}
				}
			}
		} catch (RedisConnectionFailureException e) {
			String clientName = "";
			if (managerName != null && cacheManager != null) {
				clientName = cacheManager.getClientName();
				checkRedissonConnectService.setConnectState(clientName, false);
			}
			LOGGER.warn("'" + clientName + "' Connection is lost");
		} catch (Exception e) {
			LOGGER.error("Exception encountered during doCacheEvict()", e);
		}
		return result;
	}

	private Object doCachePut(Method method, Object[] args, CachePut cachePut, Object result, boolean isRedisConnect) {
		String key = null;
		RedissonManager cacheManager = null;
		String managerName = null;

		if (result != null) {
			try {
				managerName = cachePut.cacheManager();
				LOGGER.debug("Cache Manager : " + managerName);
				String dbId = parseKey(cachePut.dbId(), method, args);
				cacheManager = getCacheManager(managerName, dbId);

				key = cachePut.key();
				if (StringUtil.contains(cachePut.key(), "#")) {
					key = parseKey(cachePut.key(), method, args);
				}

				if (cachePut.ignoreCase()) {
					key = key.toLowerCase();
				}

				LOGGER.debug("Key : " + key);

				if (cachePut.cacheNames().length > 0) {
					for (String cacheName : cachePut.cacheNames()) {
						LOGGER.debug("cacheName : " + cacheName);
						if (isRedisConnect && !cachePut.hashFlag()) {
							cacheManager.getCache(cacheName).put(key, result);
						}
					}
				}
			} catch (RedisConnectionFailureException e) {
				String clientName = "";
				if (managerName != null && cacheManager != null) {
					clientName = cacheManager.getClientName();
					checkRedissonConnectService.setConnectState(clientName, false);
				}
				LOGGER.warn("'" + clientName + "' Connection is lost");
			} catch (Exception e) {
				LOGGER.error("Exception encountered during doCachePut()", e);
			}
		}
		return result;
	}

	private Object doCacheUpdate(Method method, Object[] args, CacheUpdate cacheUpdate, Object result,
			boolean isRedisConnect) {
		String key = null;
		RedissonManager cacheManager = null;
		String managerName = null;

		try {
			managerName = cacheUpdate.cacheManager();
			LOGGER.debug("Cache Manager : " + managerName);
			String dbId = parseKey(cacheUpdate.dbId(), method, args);
			cacheManager = getCacheManager(managerName, dbId);

			key = cacheUpdate.key();
			if (StringUtil.contains(cacheUpdate.key(), "#")) {
				key = parseKey(cacheUpdate.key(), method, args);
			}
			if (cacheUpdate.ignoreCase()) {
				key = key.toLowerCase();
			}
			LOGGER.debug("Key : " + key);

			// 寫入 redis
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

						// 更新cache
						if (isRedisConnect) { // 確認redis 連線
							cache.put(key, object);
						}
					}
				}
			}
		} catch (RedisConnectionFailureException e) {
			String clientName = "";
			if (managerName != null && cacheManager != null) {
				clientName = cacheManager.getClientName();
				checkRedissonConnectService.setConnectState(clientName, false);
			}
			LOGGER.warn("'" + clientName + "' Connection is lost");
		} catch (Exception e) {
			LOGGER.error("Exception encountered during doCacheUpdate()", e);
		}
		return result;
	}

	private Object doCacheLoad(Method method, Object[] args, CacheLoad cacheLoad, Object result,
			boolean isRedisConnect) {
		String key = null;
		RedissonManager cacheManager = null;
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

				if (cacheLoad.ignoreCase()) {
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

			} catch (RedisConnectionFailureException e) {
				String clientName = "";
				if (managerName != null && cacheManager != null) {
					clientName = cacheManager.getClientName();
					checkRedissonConnectService.setConnectState(clientName, false);
				}
				LOGGER.warn("'" + clientName + "' Connection is lost");
			} catch (Exception e) {
				LOGGER.error("Exception encountered during doCacheLoad()", e);
			}
		}
		return result;
	}

	private String parseKey(String key, Method method, Object[] args) {
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

	private Object parseValue(String value, Method method, Object[] args) {
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

	private boolean checkConnection(String managerName) {
		return checkConnection(managerName, "");
	}

	private boolean checkConnection(String managerName, String dbId) {
		boolean result = true;
		LOGGER.debug("managerName = " + managerName + ", dbId = " + dbId);
		RedissonManager cacheManager = getCacheManager(managerName, dbId);
		if (cacheManager != null) {
			String clientName = cacheManager.getClientName();
			result = checkRedissonConnectService.getConnectState(clientName);
			LOGGER.debug("Check " + clientName + ", " + result);
		}
		return result;
	}

	private String getProceedName(ProceedingJoinPoint pjp, Method method) {
		return pjp.getTarget().getClass().getSimpleName() + "." + method.getName() + "()";
	}

}
