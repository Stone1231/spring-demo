package com.demo.utils;

import com.rits.cloning.Cloner;

/**
 * 複製工具類
 */
public final class CloneUtil {

	/**
	 * https://code.google.com/p/cloning/wiki/Usage
	 * 
	 * Cloner is thread safe.
	 */
	private static Cloner cloner = new Cloner();

	private CloneUtil() {
	}

	@SuppressWarnings("unchecked")
	public static <T> T clone(Object value) {
		T result = null;
		if (value != null) {
			// Cloner cloner = new Cloner();

			// #refactor to static field
			result = (T) cloner.deepClone(value);
		}
		return result;
	}
}
