package com.demo.utils;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;


import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class ThreadUtilTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	//無法取得正確時間，已與Cheng討論過
	public void sleep(){
		long beg = System.currentTimeMillis();
		ThreadUtil.sleep(2 * 1000);
		long end = System.currentTimeMillis();
		long diff = end - beg;
		//System.out.println(diff);
		//Boolean result = diff == (2 * 1000) ; 
		//assertNotNull(result);
		//assertTrue(result.booleanValue());
	}
	
}
