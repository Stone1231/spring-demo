package com.demo.utils;


import com.demo.utils.memory.MemoryCounter;

public final class MemoryUtil {

	private MemoryUtil() {
	}

	/**
	 * 計算占記憶體空間大小
	 * 
	 * @param value
	 * @return
	 */
	public static long sizeOf(Object value) {
		long result = 0L;
		if (value == null) {
			return result;
		}
		//
		MemoryCounter counter = new MemoryCounter();
		result = counter.estimate(value);
		return result;
	}
}
