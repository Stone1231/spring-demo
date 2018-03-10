package com.demo.utils;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class BooleanUtilTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.02, time.warmup: 0.00,
	// time.bench: 0.02
	public void createBoolean() {
		Boolean result = BooleanUtil.createBoolean(true);
		assertNotNull(result);
		assertTrue(result.booleanValue());
		//

		result = BooleanUtil.createBoolean(false);
		assertNotNull(result);
		assertFalse(result.booleanValue());

		result = BooleanUtil.createBoolean("true");
		assertNotNull(result);
		assertTrue(result.booleanValue());

		result = BooleanUtil.createBoolean("on");
		assertNotNull(result);
		assertTrue(result.booleanValue());

		result = BooleanUtil.createBoolean("yes");
		assertNotNull(result);
		assertTrue(result.booleanValue());

		result = BooleanUtil.createBoolean("false");
		assertNotNull(result);
		assertFalse(result.booleanValue());

		result = BooleanUtil.createBoolean("off");
		assertNotNull(result);
		assertFalse(result.booleanValue());

		result = BooleanUtil.createBoolean("no");
		assertNotNull(result);
		assertFalse(result.booleanValue());

		result = BooleanUtil.createBoolean(null);
		assertNotNull(result);
		assertFalse(result.booleanValue());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.02, time.warmup: 0.00,
	// time.bench: 0.02
	public void toBoolean() {
		boolean result = BooleanUtil.toBoolean(null);
		assertFalse(result);

		result = BooleanUtil.toBoolean("");
		assertFalse(result);

		result = BooleanUtil.toBoolean("true");
		assertTrue(result);

		result = BooleanUtil.toBoolean("on");
		assertNotNull(result);
		assertTrue(result);

		result = BooleanUtil.toBoolean("yes");
		assertNotNull(result);
		assertTrue(result);

		result = BooleanUtil.toBoolean("false");
		assertNotNull(result);
		assertFalse(result);

		result = BooleanUtil.toBoolean("off");
		assertNotNull(result);
		assertFalse(result);

		result = BooleanUtil.toBoolean("no");
		assertNotNull(result);
		assertFalse(result);

		//
		result = BooleanUtil.toBoolean("1");
		assertTrue(result);
		//
		result = BooleanUtil.toBoolean("t");
		assertTrue(result);
		//
		result = BooleanUtil.toBoolean("T");
		assertTrue(result);
		//
		result = BooleanUtil.toBoolean("y");
		assertTrue(result);
		//
		result = BooleanUtil.toBoolean("Y");
		assertTrue(result);

		//
		result = BooleanUtil.toBoolean("0");
		assertFalse(result);
		//
		result = BooleanUtil.toBoolean("f");
		assertFalse(result);
		//
		result = BooleanUtil.toBoolean("F");
		assertFalse(result);
		//
		result = BooleanUtil.toBoolean("n");
		assertFalse(result);
		//
		result = BooleanUtil.toBoolean("N");
		assertFalse(result);

		//
		result = BooleanUtil.toBoolean("*");
		assertFalse(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.02, time.warmup: 0.00,
	// time.bench: 0.02
	public void randomBoolean() {
		Boolean result = BooleanUtil.randomBoolean();
		assertNotNull(result);
		result = result || !result;
		assertTrue(result.booleanValue());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.02, time.warmup: 0.00,
	// time.bench: 0.02
	public void safeGet() {
		Boolean result = BooleanUtil.safeGet(null);
		assertNotNull(result);
		assertFalse(result.booleanValue());

		result = BooleanUtil.safeGet(true);
		assertNotNull(result);
		assertTrue(result.booleanValue());

		result = BooleanUtil.safeGet(false);
		assertNotNull(result);
		assertFalse(result.booleanValue());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.02, time.warmup: 0.00,
	// time.bench: 0.02
	public void toStringz() {
		String result = BooleanUtil.toString(null);
		assertEquals("0", result);
		//
		result = BooleanUtil.toString(Boolean.TRUE);
		assertEquals("1", result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.02, time.warmup: 0.00,
	// time.bench: 0.02
	public void toStringWithBoolean() {
		String result = BooleanUtil.toString(true);
		assertEquals("1", result);

		result = BooleanUtil.toString(false);
		assertEquals("0", result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.02, time.warmup: 0.00,
	// time.bench: 0.02
	public void toInt() {
		int result = BooleanUtil.toInt(true);
		assertEquals(1, result);

		result = BooleanUtil.toInt(false);
		assertEquals(0, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.02, time.warmup: 0.00,
	// time.bench: 0.02
	public void toIntWithBoolean() {
		int result = BooleanUtil.toInt(null);
		assertEquals(0, result);

		result = BooleanUtil.toInt(Boolean.TRUE);
		assertEquals(1, result);

		result = BooleanUtil.toInt(Boolean.FALSE);
		assertEquals(0, result);

	}
}
