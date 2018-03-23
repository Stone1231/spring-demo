package com.demo.dao.mysql.base;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.base.service.AbstractFactoryBean;
import com.demo.utils.AssertUtil;

public class BasicDataSourceArrayFactoryBean extends AbstractFactoryBean<BasicDataSource[]> {

	private static final Logger LOGGER = LoggerFactory.getLogger(BasicDataSourceArrayFactoryBean.class);

	// props
	public static final String MAX_DATA_SOURCE_SIZE = "maxDataSourceSize";

	public static final int DEFAULT_MAX_DATA_SOURCE_SIZE = 1;

	public static final String URL = "url";

	public static final String DEFAULT_URL = null;

	public static final String DRIVER_CLASSNAME = "driverClassName";

	public static final String DEFAULT_DRIVER_CLASSNAME = null;

	public static final String USERNAME = "username";

	public static final String DEFAULT_USERNAME = null;

	public static final String PASSWORD = "password";

	public static final String DEFAULT_PASSWORD = null;

	public static final String MAX_TOTAL = "maxTotal";

	public static final int DEFAULT_MAX_TOTAL = 8;

	public static final String INITIAL_SIZE = "initialSize";

	public static final int DEFAULT_INITIAL_SIZE = 0;

	public static final String MAX_WAIT_MILLIS = "maxWaitMillis";

	public static final long DEFAULT_MAX_WAIT_MILLIS = -1L;

	public static final String MIN_IDLE = "minIdle";

	public static final int DEFAULT_MIN_IDLE = 0;

	public static final String MAX_IDLE = "maxIdle";

	public static final int DEFAULT_MAX_IDLE = 8;
	//
	public static final String TIME_BETWEEN_EVICTION_RUNS_MILLIS = "timeBetweenEvictionRunsMillis";

	public static final long DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = -1L;

	public static final String MIN_EVICTABLE_IDLE_TIME_MILLIS = "minEvictableIdleTimeMillis";

	public static final long DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS = 1800000L;

	public static final String VALIDATION_QUERY = "validationQuery";

	public static final String DEFAULT_VALIDATION_QUERY = null;

	public static final String TEST_WHILE_IDLE = "testWhileIdle";

	public static final boolean DEFAULT_TEST_WHILE_IDLE = false;

	public static final String TEST_ON_BORROW = "testOnBorrow";

	public static final boolean DEFAULT_TEST_ON_BORROW = true;

	public static final String TEST_ON_RETURN = "testOnReturn";

	public static final boolean DEFAULT_TEST_ON_RETURN = false;
	//
	public static final String POOL_PREPARED_STATEMENTS = "poolPreparedStatements";

	public static final boolean DEFAULT_POOL_PREPARED_STATEMENTS = false;

	public static final String REMOVE_ABANDONED_ONBORROW = "removeAbandonedOnBorrow";

	public static final boolean DEFAULT_REMOVE_ABANDONED_ONBORROW = false;

	public static final String REMOVE_EABANDONED_TIMEOUT = "removeAbandonedTimeout";

	public static final int DEFAULT_REMOVE_EABANDONED_TIMEOUT = 300;

	public static final String LOG_ABANDONED = "logAbandoned";

	public static final boolean DEFAULT_LOG_ABANDONED = false;

	private BasicDataSource[] basicDataSourceArray;

	public BasicDataSourceArrayFactoryBean() {
		//
	}

	@Override
	public void init() throws RuntimeException {
		super.init();
		//
		try {
			if (this.basicDataSourceArray != null) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info(new StringBuilder().append("Inject from setBasicDataSourceArray()").toString());
				}
			} else {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("Using createBasicDataSourceArray()");
				}
				this.basicDataSourceArray = createBasicDataSourceArray();
			}
		} catch (Exception e) {
			LOGGER.error("Exception encountered during init()", e);
		}
	}

	/**
	 * 建立多個BasicDataSource
	 * 
	 * @return
	 * @throws Exception
	 */
	protected BasicDataSource[] createBasicDataSourceArray() throws Exception {
		BasicDataSource[] result = null;
		//
		result = new BasicDataSource[extendedProperties.getInt(MAX_DATA_SOURCE_SIZE, DEFAULT_MAX_DATA_SOURCE_SIZE)];
		//
		for (int i = 0; i < result.length; i++) {
			BasicDataSource dataSource = new BasicDataSource();
			//
			String url = nextUrl(extendedProperties.getString(URL, DEFAULT_URL), i);
			LOGGER.info("url: {}", url);
			dataSource.setUrl(url);
			dataSource.setDriverClassName(extendedProperties.getString(DRIVER_CLASSNAME, DEFAULT_DRIVER_CLASSNAME));
			dataSource.setUsername(extendedProperties.getString(USERNAME, DEFAULT_USERNAME));
			dataSource.setPassword(extendedProperties.getString(PASSWORD, DEFAULT_PASSWORD));
			//
			dataSource.setMaxTotal(extendedProperties.getInt(MAX_TOTAL, DEFAULT_MAX_TOTAL));
			dataSource.setInitialSize(extendedProperties.getInt(INITIAL_SIZE, DEFAULT_INITIAL_SIZE));
			dataSource.setMaxWaitMillis(extendedProperties.getLong(MAX_WAIT_MILLIS, DEFAULT_MAX_WAIT_MILLIS));
			dataSource.setMinIdle(extendedProperties.getInt(MIN_IDLE, DEFAULT_MIN_IDLE));
			dataSource.setMaxIdle(extendedProperties.getInt(MAX_IDLE, DEFAULT_MAX_IDLE));
			//
			dataSource.setTimeBetweenEvictionRunsMillis(extendedProperties.getLong(TIME_BETWEEN_EVICTION_RUNS_MILLIS,
					DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS));
			dataSource.setMinEvictableIdleTimeMillis(
					extendedProperties.getLong(MIN_EVICTABLE_IDLE_TIME_MILLIS, DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS));
			dataSource.setValidationQuery(extendedProperties.getString(VALIDATION_QUERY, DEFAULT_VALIDATION_QUERY));
			dataSource.setTestWhileIdle(extendedProperties.getBoolean(TEST_WHILE_IDLE, DEFAULT_TEST_WHILE_IDLE));
			dataSource.setTestOnBorrow(extendedProperties.getBoolean(TEST_ON_BORROW, DEFAULT_TEST_ON_BORROW));
			dataSource.setTestOnReturn(extendedProperties.getBoolean(TEST_ON_RETURN, DEFAULT_TEST_ON_RETURN));
			//
			dataSource.setPoolPreparedStatements(
					extendedProperties.getBoolean(POOL_PREPARED_STATEMENTS, DEFAULT_POOL_PREPARED_STATEMENTS));
			dataSource.setRemoveAbandonedOnBorrow(
					extendedProperties.getBoolean(REMOVE_ABANDONED_ONBORROW, DEFAULT_REMOVE_ABANDONED_ONBORROW));
			dataSource.setRemoveAbandonedTimeout(
					extendedProperties.getInt(REMOVE_EABANDONED_TIMEOUT, DEFAULT_REMOVE_EABANDONED_TIMEOUT));
			dataSource.setLogAbandoned(extendedProperties.getBoolean(LOG_ABANDONED, DEFAULT_LOG_ABANDONED));
			//
			result[i] = dataSource;
		}

		return result;
	}

	/**
	 * jdbc:mysql://10.16.211.102:3306/neweggsso_db?useUnicode=yes&
	 * characterEncoding=UTF-8
	 * 
	 * @param url
	 * @param i
	 * @return
	 */
	protected String nextUrl(String url, int i) {
		AssertUtil.notNull(url, "The Url must not be null");
		//
		StringBuilder result = new StringBuilder();
		if (i < 1) {
			return url;
		}
		//
		StringBuilder jdbc = new StringBuilder();
		StringBuilder database = new StringBuilder();
		StringBuilder param = new StringBuilder();
		int pos = url.lastIndexOf('/');
		if (pos > -1) {
			jdbc.append(url.substring(0, pos + 1));
			database.append(url.substring(pos + 1, url.length()));
			pos = database.indexOf("?");
			if (pos > -1) {
				param.append(database.substring(pos, database.length()));
				database = new StringBuilder(database.substring(0, pos));
			}
		}
		//
		result.append(jdbc);
		result.append(database);
		result.append("_");
		result.append(i + 1);
		result.append(param);
		return result.toString();
	}

	@Override
	public BasicDataSource[] getObject() throws Exception {
		return basicDataSourceArray;
	}

	public BasicDataSource[] getBasicDataSourceArray() {
		return basicDataSourceArray;
	}

	public void setBasicDataSourceArray(BasicDataSource[] basicDataSourceArray) {
		this.basicDataSourceArray = basicDataSourceArray;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.basicDataSourceArray != null) ? this.basicDataSourceArray.getClass() : BasicDataSource[].class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
