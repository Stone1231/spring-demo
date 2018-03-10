package com.demo.utils;

import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class ClassUtilTest {
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void forName() {
		Class<?> result = ClassUtil.forName("java.lang.String");
		assertNotNull(result);
	}

}
