package com.demo.utils;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class CloneUtilTest {
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void testClone() {
		Date value = new Date();
		System.out.println("value: " + value);
		//
		Date cloneValue = null;
		cloneValue = CloneUtil.clone(value);
		//
		cloneValue.setYear(50);// 1950
		System.out.println("modified cloneValue: " + cloneValue);
		System.out.println("after value: " + value);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void cloneList() {
		List<Object> value = new LinkedList<>();
		value.add(new Date());
		value.add(Locale.TRADITIONAL_CHINESE);
		System.out.println("value: " + value);
		//
		List<Object> cloneValue = null;
		cloneValue = CloneUtil.clone(value);
		//
		assertEquals(value.size(), cloneValue.size());
		//
		Date date = (Date) cloneValue.get(0);
		date.setYear(50);// 1950
		System.out.println("modified cloneValue: " + cloneValue);
		System.out.println("after value: " + value);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void cloneBytes() {
		byte[] value = new byte[] { 1, 2, 3, 4, 5 };
		System.out.println(value);
		SystemUtil.println(value);
		//
		byte[] cloneValue = null;
		cloneValue = CloneUtil.clone(value);
		//
		System.out.println(cloneValue);
		assertEquals(value.length, cloneValue.length);
		//
		cloneValue[0] = 100;
		System.out.println("modified cloneValue: " + cloneValue[0]);
		System.out.println("after value: " + value[0]);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.02 [+- 0.01], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.05, time.warmup: 0.00,
	// time.bench: 0.05
	public void cloneWithList() {
		List<String> value = new LinkedList<String>();
		value.add("aaa");
		value.add("bbb");
		value.add("ccc");
		//
		List<String> result = null;
		result = CloneUtil.clone(value);
		//
		result.remove(0);
		//
		System.out.println("old: " + value);
		System.out.println("new: " + result);
		assertTrue(value.size() == 3);
	}
}
