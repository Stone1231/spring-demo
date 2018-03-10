package com.demo.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCommonRunnable implements CommonRunnable {

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(AbstractCommonRunnable.class);
	
	private boolean disable;

	private boolean logEnable;

	public AbstractCommonRunnable() {
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public boolean isLogEnable() {
		return logEnable;
	}

	public void setLogEnable(boolean logEnable) {
		this.logEnable = logEnable;
	}

	public void run() {
		try {
			execute();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
