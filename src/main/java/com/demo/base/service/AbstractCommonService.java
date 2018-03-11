package com.demo.base.service;

import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import com.demo.utils.ConfigUtil;
import com.demo.utils.service.ThreadService;

/**
 * 抽象BaseService
 */
public abstract class AbstractCommonService implements CommonService, ApplicationContextAware, BeanFactoryAware,
		BeanNameAware, ResourceLoaderAware, InitializingBean, DisposableBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommonService.class);

	protected ApplicationContext applicationContext;

	protected DefaultListableBeanFactory beanFactory;

	/**
	 * bean名稱,等於id
	 */
	protected String beanName;

	protected ResourceLoader resourceLoader;

	/**
	 * hack修正
	 */
	private static AtomicBoolean hack = new AtomicBoolean(false);
	/**
	 * 線程服務
	 */
	@Autowired
	//@Qualifier("threadService")
	protected ThreadService threadService;

	public AbstractCommonService() {
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 設定ApplicationContext
	 *
	 * @param applicationContext
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
		// #fix circular reference
		if (this.beanFactory != null && !hack.get()) {
			this.beanFactory.setAllowRawInjectionDespiteWrapping(true);
			hack.set(true);
		}
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public ThreadService getThreadService() {
		return threadService;
	}

	/**
	 * 設定ThreadService
	 *
	 * @param threadService
	 */
	public void setThreadService(ThreadService threadService) {
		this.threadService = threadService;
	}

	public final void afterPropertiesSet() {
		try {
			init();
		} catch (Exception ex) {
			throw new BeanInitializationException("Initialization of Service failed", ex);
		}
	}

	/**
	 * 初始化
	 */
	protected void init() {
		if (ConfigUtil.isDebug()) {
			int mod = getClass().getModifiers();
			if (!Modifier.isAbstract(mod)) {
				LOGGER.info("Initialization of " + getClass().getSimpleName());
			}
		}
	}

	/**
	 * 銷毀
	 *
	 * @throws Exception
	 */
	public final void destroy() {
		try {
			uninit();
		} catch (Exception ex) {
			throw new BeanInitializationException("Uninitialization of Service failed", ex);
		}
	}

	/**
	 * 銷毀化
	 *
	 * @throws Exception
	 */
	protected void uninit() {
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
