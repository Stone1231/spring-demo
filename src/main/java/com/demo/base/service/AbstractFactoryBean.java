package com.demo.base.service;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.collections.ExtendedProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.demo.base.service.AbstractCommonService;

public abstract class AbstractFactoryBean<T> extends AbstractCommonService implements FactoryBean<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFactoryBean.class);

	private Resource configLocation;

	private Properties properties;

	/**
	 * properties改成使用ExtendedProperties
	 */
	protected transient ExtendedProperties extendedProperties = new ExtendedProperties();

	public AbstractFactoryBean() {
		//
	}

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	protected void init() {
		super.init();
		//
		try {
			mergeProperties();
		} catch (Exception e) {
			LOGGER.error("Exception encountered during init()", e);
		}
	}

	/**
	 * 合併設定
	 * 
	 * properties改成使用 extendedProperties 取屬性
	 * 
	 * @throws Exception
	 */
	protected void mergeProperties() throws IOException {
		Properties props = new Properties();
		if (this.configLocation != null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(new StringBuilder().append("Loading config from ").append(this.configLocation).toString());
			}
			PropertiesLoaderUtils.fillProperties(props, this.configLocation);
		}
		if (this.properties != null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(new StringBuilder().append("Loading properties from setProperties(Properties)").toString());
			}
			props.putAll(this.properties);
			// 清除原properties,省mem,因之後會使用extendedProperties了
			this.properties.clear();
		}
		//
		if (props.size() > 0) {
			this.extendedProperties = ExtendedProperties.convertProperties(props);
			if (this.extendedProperties.size() > 0) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info(new StringBuilder().append("Pros: " + extendedProperties).toString());
				}
			}
		}
	}
}
