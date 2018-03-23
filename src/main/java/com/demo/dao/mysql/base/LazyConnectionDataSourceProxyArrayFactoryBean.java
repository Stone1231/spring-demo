package com.demo.dao.mysql.base;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import com.demo.base.service.AbstractFactoryBean;
import com.demo.utils.AssertUtil;

public class LazyConnectionDataSourceProxyArrayFactoryBean
		extends AbstractFactoryBean<LazyConnectionDataSourceProxy[]> {

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(LazyConnectionDataSourceProxyArrayFactoryBean.class);

	private LazyConnectionDataSourceProxy[] lazyConnectionDataSourceProxys;

	public LazyConnectionDataSourceProxyArrayFactoryBean(DataSource[] dataSources) {
		setTargetDataSources(dataSources);
	}

	public LazyConnectionDataSourceProxyArrayFactoryBean() {

	}

	public void init() {
		super.init();
		//
	}

	public void setTargetDataSources(DataSource[] dataSources) {
		AssertUtil.notNull(dataSources, "The DataSources must not be null");
		//
		this.lazyConnectionDataSourceProxys = new LazyConnectionDataSourceProxy[dataSources.length];
		for (int i = 0; i < dataSources.length; i++) {
			LazyConnectionDataSourceProxy lazyConnectionDataSourceProxy = new LazyConnectionDataSourceProxy(
					dataSources[i]);
			this.lazyConnectionDataSourceProxys[i] = lazyConnectionDataSourceProxy;
		}
	}

	@Override
	public LazyConnectionDataSourceProxy[] getObject() throws Exception {
		return lazyConnectionDataSourceProxys;
	}

	@Override
	public Class<?> getObjectType() {
		return ((this.lazyConnectionDataSourceProxys != null) ? this.lazyConnectionDataSourceProxys.getClass()
				: LazyConnectionDataSourceProxy[].class);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
