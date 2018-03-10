package com.demo.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class ArrayUtilTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void addUnique() {
		Class<?>[] x = new Class[] { String.class };
		Class<?>[] y = new Class[] { Integer.class };
		//
		Class<?>[] result = null;
		result = ArrayUtil.addUnique(x, y, Class[].class);
		assertEquals(2, result.length);
		//
		y = new Class[] { String.class };
		result = ArrayUtil.addUnique(x, y, Class[].class);
		assertEquals(1, result.length);
		//
		result = ArrayUtil.addUnique(null, y, Class[].class);
		assertEquals(1, result.length);
		//
		result = ArrayUtil.addUnique(x, null, Class[].class);
		assertEquals(1, result.length);
		//
		String[] stringResult = ArrayUtil.addUnique(new String[] { "a", "b", null }, new String[] { "c", null },
				String[].class);
		SystemUtil.println(stringResult);
		assertEquals(4, stringResult.length);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void add() {
		byte[] x = new byte[] { 1, 2, 3 };
		byte[] y = new byte[] { 4, 5, 6 };
		//
		byte[] result = null;
		result = ArrayUtil.add(x, y);
		//
		SystemUtil.println(result); // 1, 2, 3, 4, 5, 6
		assertEquals(6, result.length);
		//
		y = new byte[] { 7, 8 };
		result = ArrayUtil.add(result, y);
		SystemUtil.println(result); // 1, 2, 3, 4, 5, 6
		assertEquals(8, result.length);
		//
		x = null;
		y = new byte[] { 7, 8, 9 };
		result = ArrayUtil.add(x, y);
		SystemUtil.println(result); // 7, 8, 9
		assertEquals(3, result.length);
		//
		x = new byte[0];
		y = new byte[] { 7, 8, 9 };
		result = ArrayUtil.add(x, y);
		SystemUtil.println(result); // 7, 8, 9
		assertEquals(3, result.length);
		//
		x = new byte[] { 10, 11, 12 };
		y = null;
		result = ArrayUtil.add(x, y);
		SystemUtil.println(result); // 10, 11, 12
		assertEquals(3, result.length);
		//
		x = new byte[] { 10, 11, 12 };
		y = new byte[0];
		result = ArrayUtil.add(x, y);
		SystemUtil.println(result); // 10, 11, 12
		assertEquals(3, result.length);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void addTwoDimension() {
		byte[] aaa = new byte[] { 0, 1, 2 };
		byte[] bbb = new byte[] { 10, 20, 30 };
		//
		byte[] FLASH_EOF_BYTES = new byte[] { 0x00 };

		byte[][] newBuffs = ArrayUtil.add(FLASH_EOF_BYTES, aaa, bbb);
		SystemUtil.println(newBuffs);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.01
	public void contains() {
		String[] values = new String[] { "aaa", "bbb" };
		//
		boolean result = false;
		result = ArrayUtil.contains(values, "aaa");
		//
		System.out.println(result);
		assertTrue(result);
		//
		result = ArrayUtil.contains(null, "aaa");
		assertFalse(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.00
	public void isEmpty() {
		String[] values = new String[2];

		boolean result = false;
		result = ArrayUtil.isEmpty(values);
		//
		System.out.println(result);
		assertFalse(result);
		//
		result = ArrayUtil.isEmpty(null);
		assertTrue(result);
		result = ArrayUtil.isEmpty(new String[] {});
		assertTrue(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.00
	public void isNotEmpty() {
		String[] values = new String[2];

		boolean result = false;
		result = ArrayUtil.isNotEmpty(values);
		//
		System.out.println(result);
		assertTrue(result);
		//
		result = ArrayUtil.isNotEmpty(null);
		assertFalse(result);
		result = ArrayUtil.isNotEmpty(new String[] {});
		assertFalse(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.00
	public void indexOf() {
		String[] values = new String[] { "aaa", "bbb" };
		int result = ArrayUtil.indexOf(values, "bbb");
		assertTrue(result == 1);
	}
}
