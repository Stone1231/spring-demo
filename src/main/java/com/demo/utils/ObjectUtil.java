package com.demo.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to process Object.
 */
public class ObjectUtil {

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(ObjectUtil.class);

	public static final Object EMPTY_OBJECT = new Object();

	private ObjectUtil() {
	}

	public static Object toObject(Object value) {
		return toObject(value, null);
	}

	public static Object toObject(Object value, Object defaultValue) {
		return (value != null) ? value : defaultValue;
	}

	/**
	 * <p>
	 * Compares two objects for equality, where either one or both objects may
	 * be <code>null</code>.
	 * </p>
	 *
	 * <pre>
	 * ObjectHelper.equals(null, null)                  = true
	 * ObjectHelper.equals(null, "")                    = false
	 * ObjectHelper.equals("", null)                    = false
	 * ObjectHelper.equals("", "")                      = true
	 * ObjectHelper.equals(Boolean.TRUE, null)          = false
	 * ObjectHelper.equals(Boolean.TRUE, "true")        = false
	 * ObjectHelper.equals(Boolean.TRUE, Boolean.TRUE)  = true
	 * ObjectHelper.equals(Boolean.TRUE, Boolean.FALSE) = false
	 * </pre>
	 *
	 * @param x
	 *            Object the first object, may be <code>null</code>
	 * @param y
	 *            Object the second object, may be <code>null</code>
	 * @return <code>true</code> if the values of both objects are the same
	 */
	public static boolean equals(Object x, Object y) {
		if (x == y) {
			return true;
		}
		if ((x == null) || (y == null)) {
			return false;
		}
		return x.equals(y);
	}

//	public static boolean ___equals(byte[] x, byte[] y) {
//		if (x == y) {
//			return true;
//		}
//		if ((x == null) || (y == null)) {
//			return false;
//		}
//		//
//		if (x.length == y.length) {
//			for (int i = 0; i < x.length; i++) {
//				if (!(x[i] == y[i]))
//					return false;
//			}
//			return true;
//		}
//		return false;
//	}

	public static boolean equals(byte[] x, byte[] y) {
		if (x == y)
			return true;
		if (x == null || y == null)
			return false;

		int length = x.length;
		if (y.length != length)
			return false;

		for (int i = 0; i < length; i++)
			if (x[i] != y[i])
				return false;

		return true;
	}

	// 20111206
	public static boolean equals(Object[] x, Object[] y) {
		if (x == y) {
			return true;
		}
		if ((x == null) || (y == null)) {
			return false;
		}
		//
		if (x.length == y.length) {
			for (int i = 0; i < x.length; i++) {
				if (!x[i].equals(y[i]))
					return false;
			}
			return true;
		}
		return false;
	}

	public static String identityToString(Object object) {
		if (object == null) {
			return null;
		}
		return appendIdentityToString(null, object).toString();
	}

	public static StringBuffer appendIdentityToString(StringBuffer buffer,
			Object object) {
		if (object == null) {
			return null;
		}
		if (buffer == null) {
			buffer = new StringBuffer();
		}
		return buffer.append(object.getClass().getName()).append('@')
				.append(Integer.toHexString(System.identityHashCode(object)));
	}

	// ------------------------------------------------------

	public static <T> String toString(T value) {
		return toString(value, StringUtil.COMMA_SPACE);
	}

	public static <T> String toString(T value, String splitter) {
		StringBuilder result = new StringBuilder();
		if (isArray(value))// 基本型別array
		{
			// Class<?> clazz = value.getClass().getComponentType();
			StringBuilder className = new StringBuilder(value.getClass()
					.getName());
			char type = className.charAt(1);
			if (type != '[' && type != 'L') {
				switch (type) {
				case 'Z':
					boolean[] booleans = (boolean[]) value;
					for (int i = 0; i < booleans.length; i++) {
						boolean element = booleans[i];
						result.append(element);
						if (i < booleans.length - 1) {
							result.append(splitter);
						}
					}
					break;
				case 'C':
					char[] chars = (char[]) value;
					for (int i = 0; i < chars.length; i++) {
						char element = chars[i];
						result.append(element);
						if (i < chars.length - 1) {
							result.append(splitter);
						}
					}
					break;
				case 'B':
					byte[] bytes = (byte[]) value;
					for (int i = 0; i < bytes.length; i++) {
						byte element = bytes[i];
						result.append(element);
						if (i < bytes.length - 1) {
							result.append(splitter);
						}
					}
					break;
				case 'S':
					short[] shorts = (short[]) value;
					for (int i = 0; i < shorts.length; i++) {
						short element = shorts[i];
						result.append(element);
						if (i < shorts.length - 1) {
							result.append(splitter);
						}
					}
					break;
				case 'I':
					int[] ints = (int[]) value;
					for (int i = 0; i < ints.length; i++) {
						int element = ints[i];
						result.append(element);
						if (i < ints.length - 1) {
							result.append(splitter);
						}
					}
					break;
				case 'J':
					long[] longs = (long[]) value;
					for (int i = 0; i < longs.length; i++) {
						long element = longs[i];
						result.append(element);
						if (i < longs.length - 1) {
							result.append(splitter);
						}
					}
					break;
				case 'F':
					float[] floats = (float[]) value;
					for (int i = 0; i < floats.length; i++) {
						float element = floats[i];
						result.append(element);
						if (i < floats.length - 1) {
							result.append(splitter);
						}
					}
					break;
				case 'D':
					double[] doubles = (double[]) value;
					for (int i = 0; i < doubles.length; i++) {
						double element = doubles[i];
						result.append(element);
						if (i < doubles.length - 1) {
							result.append(splitter);
						}
					}
					break;
				}
			}
		} else {
			result.append(value);
		}
		// return (value != null ? value.toString() : null);
		return result.toString();
	}

	public static <T> String toString(T[] values) {
		return toString(values, StringUtil.COMMA_SPACE);
	}

	public static <T> String toString(T[] values, String splitter) {
		StringBuilder result = new StringBuilder();
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				T element = values[i];
				result.append(toString(element));
				//
				if (i < values.length - 1) {
					result.append("\n");
				}
			}
		}
		return result.toString();
	}

	// ------------------------------------------------------

	public static Object maskNull(Object object) {
		return (object == null) ? EMPTY_OBJECT : object;
	}

	public static Object unmaskNull(Object object) {
		return (object == EMPTY_OBJECT) ? null : object;
	}

	public static Object createObject(InvocationHandler handler) {
		return Proxy.newProxyInstance(handler.getClass().getClassLoader(),
				new Class[] { Object.class }, handler);
	}

	public static boolean isArray(Object obj) {
		return ((obj != null) && (obj.getClass().isArray()));
	}

	public static boolean isNotArray(Object obj) {
		return ((obj == null) || (!obj.getClass().isArray()));
	}

	public static boolean isEmpty(Object[] array) {
		return ((array == null) || (array.length == 0));
	}

	public static boolean isNotEmpty(Object[] array) {
		return ((array != null) && (array.length > 0));
	}
	
	public static boolean notEmpty(Object[] array) {
		return ((array != null) && (array.length > 0));
	}
}
