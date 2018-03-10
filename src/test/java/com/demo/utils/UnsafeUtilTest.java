package com.demo.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.esotericsoftware.kryo.Kryo;
import com.demo.utils.memory.MemoryCounter;

public class UnsafeUtilTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void sizeOf() {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 100; i++) {
			list.add(i);
		}
		//
		long result = UnsafeUtil.sizeOf(list);
		// 24
		System.out.println("List mem size: " + result);
		//
		String stringVaule = "abc";
		// 24
		result = UnsafeUtil.sizeOf(stringVaule);
		System.out.println("String mem size: " + result);
		//
		Date date = new Date();
		result = UnsafeUtil.sizeOf(date);
		// 24
		System.out.println("Date mem size: " + result);
		//
		Integer integerValue = new Integer(0);
		result = UnsafeUtil.sizeOf(integerValue);
		// 16
		System.out.println("Integer mem size: " + result);
	}
}
