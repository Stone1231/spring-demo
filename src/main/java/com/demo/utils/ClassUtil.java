package com.demo.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Used to process Class.
 */
public final class ClassUtil {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

	private ClassUtil() {
	}

	@SuppressWarnings("static-access")
	public static Class<?> forName(String name) {
		Class<?> result = null;
		try {
			result = Thread.currentThread().getClass().forName(name);
		} catch (Exception ex) {
			try {
				result = Class.forName(name);
			} catch (Exception ex2) {
				//
			}
		}
		return result;
	}

	public static Method getDeclaredMethod(Class<?> clazz, String methodName) {
		Method result = null;
		//
		if (clazz != null) {
			Method[] methods = clazz.getDeclaredMethods();
			if (methods != null) {
				for (Method entry : methods) {
					if (entry.getName().equals(methodName)) {
						result = entry;
						break;
					}
				}
			}
		}
		return result;
	}

	public static Class<?>[] getParameterTypes(Method method) {
		Class<?>[] result = null;
		if (method != null) {
			result = method.getParameterTypes();
		}
		return result;
	}

	/**
	 * 由object建構物件
	 *
	 * @param value
	 * @return
	 */
	public static <T> T newInstance(Object value) {
		T result = null;
		if (value != null) {
			result = newInstance(value.getClass());
		}
		return result;
	}

	/**
	 * 由className建構物件
	 *
	 * @param className
	 * @return
	 */
	public static <T> T newInstance(String className) {
		return newInstance(forName(className));
	}

	/**
	 * 由class建構物件
	 *
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<?> clazz) {
		T result = null;

		// #fix 目前以此方式最快
		try {
			if (clazz != null && !clazz.isInterface()) {
				result = (T) clazz.newInstance();
			}
		} catch (Exception ex) {
			//
		}
		return result;
	}

	public static Field getDeclaredField(Class<?> clazz, String fieldName) {
		Field result = null;
		//
		if (clazz != null) {
			Field[] fields = getDeclaredFields(clazz);
			if (fields != null) {
				for (Field entry : fields) {
					if (entry.getName().equals(fieldName)) {
						result = entry;
						break;
					}
				}
			}
		}
		return result;
	}

	public static Field[] getDeclaredFields(Class<?> clazz) {
		Field[] fields = new Field[0];
		try {
			if (clazz != null) {
				fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
				}
			}
		} catch (Exception ex) {
			//
		}
		//
		if (clazz != null) {
			Class<?> superClazz = clazz.getSuperclass();
			if (superClazz != null && !superClazz.equals(Object.class)) {
				Field[] superFields = getDeclaredFields(superClazz);
				if (superFields.length > 0) {
					fields = ArrayUtil.addUnique(fields, superFields, Field[].class);
				}
			}
		}
		//
		return fields;
	}

	public static <T> T getDeclaredFieldValue(Object value, String fieldName) {
		T result = null;
		if (value != null && fieldName != null) {
			Field field = getDeclaredField(value.getClass(), fieldName);
			result = getFieldValue(value, field);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(Object value, Field field) {
		T result = null;
		if (value != null && field != null) {
			try {
				boolean oldAccessible = field.isAccessible();
				boolean changeAccessible = false;
				if (!oldAccessible) {
					field.setAccessible(true);
					changeAccessible = true;
				}
				//
				result = (T) field.get(value);
				//
				if (changeAccessible) {
					field.setAccessible(oldAccessible);
				}
			} catch (Exception ex) {
				//
			}
		}
		return result;
	}

	public static boolean setDeclaredFieldValue(Object value, String fieldName, Object setValue) {
		Field field = getDeclaredField(value.getClass(), fieldName);
		return setFieldValue(value, field, setValue);
	}

	public static boolean setFieldValue(Object value, Field field, Object setValue) {
		boolean result = false;
		if (value != null && field != null) {
			try {
				boolean oldAccessible = field.isAccessible();
				boolean changeAccessible = false;
				if (!oldAccessible) {
					field.setAccessible(true);
					changeAccessible = true;
				}
				//
				field.set(value, setValue);
				//
				if (changeAccessible) {
					field.setAccessible(oldAccessible);
				}
				//
				result = true;
			} catch (Exception ex) {
				//
			}
		}
		return result;
	}
}
