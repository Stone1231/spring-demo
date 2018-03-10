package com.demo.utils;

/**
 * Used to process boolean.
 */
public final class BooleanUtil {

	/**
	 * Default value is false.
	 */
	public static final boolean DEFAULT_VALUE = false;

	/**
	 * False string is "0".
	 */
	public static final String FALSE_STRING = "0";

	/**
	 * True string is "1".
	 */
	public static final String TRUE_STRING = "1";

	/**
	 * False int is 0.
	 */
	public static final int FALSE_INT = 0;

	/**
	 * True int is 1.
	 */
	public static final int TRUE_INT = 1;

	private BooleanUtil() {
	}
	// --------------------------------------------------

	/**
	 * Create Boolean object.
	 *
	 * @param value
	 *            boolean
	 * @return Boolean
	 */
	public static Boolean createBoolean(final boolean value) {
		if (value) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Create Boolean object.
	 *
	 * @param value
	 *            String
	 * @return Boolean
	 */
	public static Boolean createBoolean(final String value) {
		if (value == null) {
			return DEFAULT_VALUE;
		} else {
			final boolean buff = toBoolean(value, DEFAULT_VALUE);
			if (buff) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
	}

	/**
	 * 字串轉布林.
	 *
	 * @param value
	 *            String
	 * @return boolean
	 */
	public static boolean toBoolean(final String value) {
		return toBoolean(value, DEFAULT_VALUE);
	}

	/**
	 * 字串轉布林.
	 *
	 * @param value
	 *            String
	 * @param defaultValue
	 *            boolean
	 * @return boolean
	 */
	public static boolean toBoolean(final String value, final boolean defaultValue) {
		if (value == null || value.length() == 0) {
			return defaultValue;
		} else {
			if ("true".equalsIgnoreCase(value)) {
				return true;
			}
			if ("on".equalsIgnoreCase(value)) {
				return true;
			}
			if ("yes".equalsIgnoreCase(value)) {
				return true;
			}
			//
			if ("false".equalsIgnoreCase(value)) {
				return false;
			}
			if ("off".equalsIgnoreCase(value)) {
				return false;
			}
			if ("no".equalsIgnoreCase(value)) {
				return false;
			}
			//
			switch (value.charAt(0)) {
			case '1':
			case 't':
			case 'T':
			case 'y':
			case 'Y':
				return true;
			case '0':
			case 'f':
			case 'F':
			case 'n':
			case 'N':
				return false;
			default:
				return defaultValue;
			}
		}
	}

	/**
	 * 隨機產生布林.
	 *
	 * @return boolean
	 */
	public static boolean randomBoolean() {
		return NumberUtil.randomBoolean();
	}

	/**
	 * 安全讀取布林.
	 *
	 * @param value
	 *            Boolean
	 * @return boolean
	 */
	public static boolean safeGet(final Boolean value) {
		if (value == null) {
			return DEFAULT_VALUE;
		}
		return value.booleanValue();
	}

	/**
	 * 布林轉字串.
	 *
	 * @param value
	 *            Boolean
	 * @return String
	 */
	public static String toString(final Boolean value) {
		if (value == null) {
			return FALSE_STRING;
		}
		return toString(value.booleanValue());
	}

	/**
	 * 布林轉字串.
	 *
	 * true="1",false="0"
	 *
	 * @param value
	 *            boolean
	 * @return String
	 */
	public static String toString(final boolean value) {
		if (value) {
			return TRUE_STRING;
		}
		return FALSE_STRING;
	}

	/**
	 * 布林轉整數.
	 *
	 * @param value
	 *            Boolean
	 * @return int
	 */
	public static int toInt(final Boolean value) {
		if (value == null) {
			return FALSE_INT;
		}
		return toInt(value.booleanValue());
	}

	/**
	 * 布林轉整數.
	 *
	 * @param value
	 *            boolean
	 * @return int
	 */
	public static int toInt(final boolean value) {
		if (value) {
			return TRUE_INT;
		}
		return FALSE_INT;
	}
}
