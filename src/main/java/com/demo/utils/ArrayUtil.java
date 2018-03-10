package com.demo.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * Used to process array.
 */
public final class ArrayUtil {

	private ArrayUtil() {
	}

	public static boolean contains(Object[] values, Object value) {
		boolean result = false;
		for (int i = 0; values != null && i < values.length; i++) {
			if (ObjectUtil.equals(values[i], value)) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * 
	 * object[] -> string[]
	 * 
	 * String[] stringArray = Arrays.copyOf(objectArray, objectArray.length,
	 * String[].class);
	 * 
	 * Arrays.asList(Object_Array).toArray(new String[Object_Array.length]);
	 * 
	 * @param original
	 * @param newLength
	 * @param newType
	 * @return
	 */
	public static <T, U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
		return Arrays.copyOf(original, newLength, newType);
	}

	/**
	 * 合併陣列,元素不重複,有唯一性
	 *
	 * @param x
	 * @param y
	 * @param newType
	 * @return
	 */
	public static <T> T[] addUnique(Object[] x, Object[] y, Class<? extends T[]> newType) {
		//
		int xlength = 0;
		if (x != null) {
			xlength = x.length;
		}
		//
		List<Object> buff = new LinkedList<>();
		if (y != null) {
			for (int i = 0; i < y.length; i++) {
				// by value
				boolean contains = contains(x, y[i]);
				// not equals
				if (!contains) {
					buff.add(y[i]);
				}
			}
		}
		//
		Object[] result = new Object[xlength + buff.size()];
		if (x != null) {
			for (int i = 0; i < x.length; i++) {
				result[i] = x[i];
			}
		}
		for (int i = 0; i < buff.size(); i++) {
			result[xlength + i] = buff.get(i);
		}
		//
		return copyOf(result, result.length, newType);
	}

	/**
	 * 合併陣列,元素可重複
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public static byte[] add(byte[] x, byte[] y) {
		if (x == null || x.length == 0) {
			byte[] result = new byte[y.length];
			System.arraycopy(y, 0, result, 0, y.length);
			return result;
		} else if (y == null || y.length == 0) {
			byte[] result = new byte[x.length];
			System.arraycopy(x, 0, result, 0, x.length);
			return result;
		} else {
			byte[] result = new byte[x.length + y.length];
			System.arraycopy(x, 0, result, 0, x.length);
			System.arraycopy(y, 0, result, x.length, y.length);
			return result;
		}
	}

	/**
	 * byte[][] 加入一個 byte[].
	 *
	 * @param y
	 *            the y
	 * @param buffs
	 *            the buffs
	 * @return the byte[][]
	 */
	public static byte[][] add(final byte[] y, final byte[]... buffs) {
		byte[][] result = new byte[buffs.length + 1][];
		//
		for (int i = 0; i < buffs.length; i++) {
			result[i] = buffs[i];
		}
		result[buffs.length] = y;
		return result;
	}

	/**
	 * 陣列是否為空值.
	 *
	 * @param <T>
	 *            the generic type
	 * @param value
	 *            the value
	 * @return true, if is empty
	 */
	public static <T> boolean isEmpty(final T[] value) {
		return (value == null || value.length == 0);
	}

	/**
	 * 陣列是否不為空值.
	 *
	 * @param <T>
	 *            the generic type
	 * @param value
	 *            the value
	 * @return true, if is not empty
	 */
	public static <T> boolean isNotEmpty(final T[] value) {
		return (value != null && value.length > 0);
	}
	
    public static <T> int indexOf(T[] value, T comparedTo) {
        if (value == null) {
            return -1;
        }
        for (int i = 0, size = value.length; i < size; i++) {
            if (value[i] == comparedTo) {
                return i;
            }
        }
        return -1;
    }

}
