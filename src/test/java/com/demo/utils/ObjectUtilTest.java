package com.demo.utils;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class ObjectUtilTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.75 [+- 0.00], round.block: 0.02 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 5, GC.time: 0.04, time.total: 0.77, time.warmup: 0.00,
	// time.bench: 0.77
	public void toObject() {
		Object value = ObjectUtil.toObject("a");
		Boolean result = (value == "a");
		assertNotNull(result);
		assertTrue(result.booleanValue());

		Object defaultValue = ObjectUtil.toObject(null);
		result = (defaultValue != null);
		assertNotNull(result);
		assertFalse(result.booleanValue());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.75 [+- 0.01], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 4, GC.time: 0.04, time.total: 0.79, time.warmup: 0.00,
	// time.bench: 0.79
	public void equals() {

		Object x = "1";
		Object y = "1";
		Boolean result = ObjectUtil.equals(x, y);
		assertNotNull(result);
		assertTrue(result.booleanValue());

		x = null;
		y = "";
		result = ObjectUtil.equals(x, y);
		assertNotNull(result);
		assertFalse(result.booleanValue());

		x = "1";
		y = "2";
		result = ObjectUtil.equals(x, y);
		assertNotNull(result);
		assertFalse(result.booleanValue());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.80 [+- 0.01], round.block: 0.01 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 5, GC.time: 0.05, time.total: 0.82, time.warmup: 0.00,
	// time.bench: 0.82
	public void maskNull() {

		Object obj = null;
		Object resultObj = ObjectUtil.maskNull(obj);
		Boolean result = (resultObj == ObjectUtil.EMPTY_OBJECT);
		assertNotNull(result);
		assertTrue(result.booleanValue());

		obj = "1";
		resultObj = ObjectUtil.maskNull(obj);
		result = (resultObj == "1");
		assertNotNull(result);
		assertTrue(result.booleanValue());

	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.84 [+- 0.01], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 5, GC.time: 0.03, time.total: 0.86, time.warmup: 0.00,
	// time.bench: 0.86
	public void unmaskNull() {

		Object obj = ObjectUtil.EMPTY_OBJECT;
		Object resultObj = ObjectUtil.unmaskNull(obj);
		Boolean result = (resultObj == null);
		assertNotNull(result);
		assertTrue(result.booleanValue());

		obj = "1";
		resultObj = ObjectUtil.unmaskNull(obj);
		result = (resultObj == "1");
		assertNotNull(result);
		assertTrue(result.booleanValue());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.78 [+- 0.01], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 5, GC.time: 0.05, time.total: 0.80, time.warmup: 0.00,
	// time.bench: 0.79
	public void isArray() {

		Object obj = null;
		Boolean result = ObjectUtil.isArray(obj);
		assertNotNull(result);
		assertFalse(result.booleanValue());

		obj = new Object[1];
		result = ObjectUtil.isArray(obj);
		assertNotNull(result);
		assertTrue(result.booleanValue());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.77 [+- 0.01], round.block: 0.00 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 4, GC.time: 0.04, time.total: 0.79, time.warmup: 0.00,
	// time.bench: 0.79
	public void isNotArray() {

		Object obj = null;
		Boolean result = ObjectUtil.isNotArray(obj);
		assertNotNull(result);
		assertTrue(result.booleanValue());

		obj = new Object[1];
		result = ObjectUtil.isNotArray(obj);
		assertNotNull(result);
		assertFalse(result.booleanValue());

	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.76 [+- 0.00], round.block: 0.01 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 4, GC.time: 0.02, time.total: 0.78, time.warmup: 0.01,
	// time.bench: 0.77
	public void isEmpty() {

		Object[] Obj = null;
		Boolean result = ObjectUtil.isEmpty(Obj);
		assertNotNull(result);
		assertTrue(result.booleanValue());

		Obj = new Object[0];
		result = ObjectUtil.isEmpty(Obj);
		assertNotNull(result);
		assertTrue(result.booleanValue());

	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.74 [+- 0.01], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 4, GC.time: 0.03, time.total: 0.76, time.warmup: 0.00,
	// time.bench: 0.76
	public void notEmpty() {

		Object[] Obj = null;
		Boolean result = ObjectUtil.notEmpty(Obj);
		assertNotNull(result);
		assertFalse(result.booleanValue());

		Obj = new Object[6666];
		result = ObjectUtil.notEmpty(Obj);
		assertNotNull(result);
		assertTrue(result.booleanValue());
	}
	
}
