package com.demo.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.demo.model.TestModel;

public class JsonUntilTest {
	
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void writeAndRead() {
		
		TestModel model = new TestModel();
		model.setUserName("user name");
		model.setUserId("ID");
		model.setNickName("dog");
		
		String jsonString =  JsonUtil.writeJSON(model);		
		TestModel model2 = JsonUtil.readJSON(jsonString, TestModel.class);
		
		System.out.println(jsonString);
		
		assertTrue(model.getUserId().equals(model2.getUserId()));
	}

}
