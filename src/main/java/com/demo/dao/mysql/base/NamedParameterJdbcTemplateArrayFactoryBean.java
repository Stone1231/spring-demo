package com.demo.dao.mysql.base;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.demo.base.service.AbstractFactoryBean;
import com.demo.utils.AssertUtil;

public class NamedParameterJdbcTemplateArrayFactoryBean extends AbstractFactoryBean<NamedParameterJdbcTemplate[]> {

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(NamedParameterJdbcTemplateArrayFactoryBean.class);

	private NamedParameterJdbcTemplate[] namedParameterJdbcTemplates;

	public NamedParameterJdbcTemplateArrayFactoryBean(DataSource[] dataSources) {
		setTargetDataSources(dataSources);
	}

	public NamedParameterJdbcTemplateArrayFactoryBean() {

	}

	public void init() {
		super.init();
		//
	}

	public void setTargetDataSources(DataSource[] dataSources) {
		AssertUtil.notNull(dataSources, "The DataSources must not be null");
		//
		this.namedParameterJdbcTemplates = new NamedParameterJdbcTemplate[dataSources.length];
		for (int i = 0; i < dataSources.length; i++) {
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSources[i]);
			this.namedParameterJdbcTemplates[i] = namedParameterJdbcTemplate;
		}
	}

	@Override
	public NamedParameterJdbcTemplate[] getObject() throws Exception {
		return namedParameterJdbcTemplates;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.namedParameterJdbcTemplates != null) ? this.namedParameterJdbcTemplates.getClass()
				: NamedParameterJdbcTemplate[].class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
