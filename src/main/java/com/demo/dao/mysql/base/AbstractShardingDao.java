package com.demo.dao.mysql.base;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import com.demo.base.service.AbstractCommonService;
import com.demo.utils.AssertUtil;

/**
 * 操作分庫
 */
public class AbstractShardingDao extends AbstractCommonService {

	/**
	 * 讀寫
	 */
	private transient DataSource[] dataSources;

	/**
	 * 讀寫
	 */
	private transient NamedParameterJdbcTemplate[] jdbcTemplates;

	public void init() throws RuntimeException {
		super.init();
		//
		this.dataSources = applicationContext.getBean("shardingDataSourceArrayFactoryBean",
				LazyConnectionDataSourceProxy[].class);

		this.jdbcTemplates = applicationContext.getBean("shardingJdbcTemplateArrayFactoryBean",
				NamedParameterJdbcTemplate[].class);
	}

	/**
	 * 取得分庫dataSource
	 */
	protected DataSource getDataSource(int dbId) {
		AssertUtil.isTrue(dbId > 0, "The DbId must be greater than zero");
		//
		return dataSources[dbId - 1];
	}

	/**
	 * 取得分庫jdbcTemplate
	 */
	protected NamedParameterJdbcTemplate getJdbcTemplate(int dbId) {
		AssertUtil.isTrue(dbId > 0, "The DbId must be greater than zero");
		//
		return jdbcTemplates[dbId - 1];
	}
}
