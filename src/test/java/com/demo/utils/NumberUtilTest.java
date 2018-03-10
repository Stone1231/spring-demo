package com.demo.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class NumberUtilTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void safeGet() {
		Byte testByte = null;
		Byte resultByte = NumberUtil.safeGet(testByte);
		Boolean result = (resultByte == NumberUtil.DEFAULT_BYTE);
		assertNotNull(result);
		assertTrue(result.booleanValue());

		testByte = 1;
		resultByte = NumberUtil.safeGet(testByte);
		result = (resultByte == 1);
		assertNotNull(result);
		assertTrue(result.booleanValue());

		Short testShort = null;
		Short resultShort = NumberUtil.safeGet(testShort);
		result = (resultShort == NumberUtil.DEFAULT_SHORT);
		assertNotNull(result);
		assertTrue(result.booleanValue());

		testShort = 1;
		resultShort = NumberUtil.safeGet(testShort);
		result = (resultShort == 1);
		assertNotNull(result);
		assertTrue(result.booleanValue());

		Integer testInt = null;
		Integer resultInt = NumberUtil.safeGet(testInt);
		result = (resultInt == NumberUtil.DEFAULT_INT);
		assertNotNull(result);
		assertTrue(result.booleanValue());

		testInt = 1;
		resultInt = NumberUtil.safeGet(testInt);
		result = (resultInt == 1);
		assertNotNull(result);
		assertTrue(result.booleanValue());

		Long testLong = null;
		Long resultLong = NumberUtil.safeGet(testLong);
		result = (resultLong == NumberUtil.DEFAULT_LONG);
		assertNotNull(result);
		assertTrue(result.booleanValue());

		testLong = 1L;
		resultLong = NumberUtil.safeGet(testLong);
		result = (resultLong == 1L);
		assertNotNull(result);
		assertTrue(result.booleanValue());

		float testFloat = 1f;
		float resultFloat = NumberUtil.safeGet(testFloat);
		result = (resultFloat == 1L);
		assertNotNull(result);
		assertTrue(result.booleanValue());

		result = (resultLong == 1d);
		assertNotNull(result);
		assertTrue(result.booleanValue());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void divide() {
		double result = NumberUtil.divide("1", "0");
		assertTrue(Double.compare(0d, result) == 0);
		//
		result = NumberUtil.divide(1d, 1d);
		assertTrue(Double.compare(1d, result) == 0);
		//
		result = NumberUtil.divide(1d, 0);
		assertTrue(Double.compare(0d, result) == 0);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void toInt() {
		long value = Long.MAX_VALUE;
		int result = NumberUtil.toInt(value);
		System.out.println(result);
		//
		value = Long.MIN_VALUE;
		result = NumberUtil.toInt(value);
		System.out.println(result);
		//
		value = 10000L;
		result = NumberUtil.toInt(value);
		System.out.println(result);
		//
		result = NumberUtil.toInt(new Long(Long.MAX_VALUE));
		System.out.println(result);
		//
		result = NumberUtil.toInt(new Long(Long.MIN_VALUE));
		System.out.println(result);
		//
		result = NumberUtil.toInt(new Long(10000L));
		System.out.println(result);
	}
}
