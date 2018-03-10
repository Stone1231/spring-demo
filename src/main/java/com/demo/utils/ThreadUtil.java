package com.demo.utils;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ThreadUtil.class);

	public static void sleep(long millis) {
		try {
			// 0表無限
			if (millis == 0) {
				Thread.sleep(Long.MAX_VALUE);
			} else {
				TimeUnit.MILLISECONDS.sleep(millis);
			}
		} catch (Exception ex) {
			//
		}
	}

	/**
	 * 迴圈
	 *
	 * <=0, =>無間隔
	 *
	 * >0, => 間隔毫秒
	 *
	 * @param millis
	 */
	public static void loop(long millis) {
		while (true) {
			if (millis > 0) {
				sleep(millis);
			}
		}
	}
}
