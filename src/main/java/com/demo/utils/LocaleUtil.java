package com.demo.utils;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocaleUtil  {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(LocaleUtil.class);

	public final static String LOCALE = "localeUtil.locale";

	private static Locale locale = null;

	static {
		new Static();
	}

	protected static class Static {
		public Static() {
			try {
				if (locale == null) {
					locale = Locale.getDefault();
				}
			} catch (Exception ex) {
				//LOGGER.warn("Exception encountered during Static()", ex);
				locale = Locale.getDefault();
			}
		}
	}

	private LocaleUtil() {
	}

	public static Locale getLocale() {
		return locale;
	}

	public static void setLocale(Locale locale) {
		LocaleUtil.locale = locale;
	}

	public static Locale toLocale(String language, String country, String variant) {
		Locale result = null;
		if (language == null) {
			result = null;
		} else if (country == null) {
			result = new Locale(language, "");
		} else if (variant == null) {
			result = new Locale(language, country);
		} else {
			result = new Locale(language, country, variant);
		}
		return result;
	}

	/**
	 * 字串轉區域
	 *
	 * @param value
	 *            如:zh_TW,en_US
	 * @return
	 */
	public static Locale toLocale(String value) {
		String[] values = null;
		if (value != null && value.indexOf("-") > -1) {
			values = StringUtils.splitPreserveAllTokens(value, "-");
		} else if (value != null && value.indexOf("_") > -1) {
			values = StringUtils.splitPreserveAllTokens(value, "_");
		}
		return toLocale(values);
	}

	public static Locale toLocale(String[] values) {
		if (values == null) {
			return toLocale(null, null, null);
		}
		//
		String[] array = new String[3];
		for (int i = 0; i < array.length && i < values.length; i++) {
			array[i] = values[i];
		}
		return toLocale(array[0], array[1], array[2]);
	}

	/**
	 * 區域轉字串
	 *
	 * @param value
	 * @return
	 */
	public static String toString(Locale value) {
		String result = null;
		if (value != null) {
			result = value.toString();
		}
		return result;
	}

	// 20111011
	public static boolean isNotBlank(Locale value) {
		int len = 0;
		if (value == null || (len = value.toString().length()) == 0) {
			return false;
		}
		for (int i = 0; i < len; i++) {
			if ((!Character.isWhitespace(value.toString().charAt(i)))) {
				return true;
			}
		}
		return false;
	}

}
