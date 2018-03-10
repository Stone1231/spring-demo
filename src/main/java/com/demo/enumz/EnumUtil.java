package com.demo.enumz;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.demo.utils.NumberUtil;

public class EnumUtil {

	public EnumUtil() {
		//
	}

	/**
	 * 依byte取得enum
	 *
	 * @param enumType
	 * @param value
	 * @return
	 */
	public static <T extends Enum<T>> T valueOf(Class<T> enumType, byte value) {
		T result = null;
		//
		T[] constants = enumType.getEnumConstants();
		for (T entry : constants) {
			if (entry instanceof ByteEnum) {
				ByteEnum enumz = (ByteEnum) entry;
				if (enumz.getValue() == value) {
					result = entry;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 依int取得enum
	 *
	 * @param enumType
	 * @param value
	 * @return
	 */
	public static <T extends Enum<T>> T valueOf(Class<T> enumType, int value) {
		T result = null;
		//
		T[] constants = enumType.getEnumConstants();
		for (T entry : constants) {
			if (entry instanceof IntEnum) {
				IntEnum enumz = (IntEnum) entry;
				if (enumz.getValue() == value) {
					result = entry;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 依name取得enum
	 *
	 * @param enumType
	 * @param value
	 * @return
	 */
	public static <T extends Enum<T>> T nameOf(Class<T> enumType, String name) {
		return Enum.valueOf(enumType, name);
	}

	/**
	 * 判斷列舉的常數,是否唯一
	 *
	 * 唯一,傳回size=0
	 *
	 * 重複,傳回重複的常數
	 *
	 * @param enumType
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public static <T extends Enum<T>> List<T> checkUnique(Class<T> enumType) {
		List<T> result = new LinkedList<>();
		//
		Set buffs = new LinkedHashSet();
		T[] constants = enumType.getEnumConstants();
		for (T entry : constants) {

			// ByteEnum
			if (entry instanceof ByteEnum) {
				ByteEnum enumz = (ByteEnum) entry;
				processUnique(enumz.getValue(), entry, buffs, result);
			}
			// IntEnum
			else if (entry instanceof IntEnum) {
				IntEnum enumz = (IntEnum) entry;
				processUnique(enumz.getValue(), entry, buffs, result);
			}
		}
		return result;
	}

	/**
	 * 處理唯一, 把重複的放到list上
	 *
	 * @param value
	 * @param entry
	 * @param buffs
	 * @param duplicates
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static <T extends Enum<T>> void processUnique(Object value, T entry, Set buffs, List<T> duplicates) {
		if (!buffs.contains(value)) {
			buffs.add(value);
		} else {
			duplicates.add(entry);
		}
	}

	/**
	 * 隨機取得列舉元素
	 *
	 * @param enumType
	 * @return
	 */
	public static <T extends Enum<T>> T randomType(Class<T> enumType) {
		T result = null;
		//
		if (enumType != null) {
			T[] constants = enumType.getEnumConstants();
			int index = NumberUtil.randomInt(0, constants.length);
			result = constants[index];
		}
		return result;
	}

	/**
	 * 取得所有列舉元素
	 *
	 * @param enumType
	 * @return
	 */
	public static <T extends Enum<T>> T[] values(Class<T> enumType) {
		T[] result = null;
		//
		if (enumType != null) {
			result = enumType.getEnumConstants();
		}
		return result;
	}

	/**
	 * 取得所有列舉元素, list
	 *
	 * @param enumType
	 * @return
	 */
	public static <T extends Enum<T>> List<T> listValues(Class<T> enumType) {
		List<T> result = new LinkedList<>();
		//
		if (enumType != null) {
			result = Arrays.asList(enumType.getEnumConstants());
		}
		return result;
	}

}
