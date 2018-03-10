package com.demo.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.demo.utils.MemoryUtil;
import com.demo.utils.memory.MemoryCounter;
import com.esotericsoftware.kryo.Kryo;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MemoryUtilTest {
	
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
		long result = MemoryUtil.sizeOf(list);
		// 2076
		System.out.println("List mem size: " + result);
		//
		String stringVaule = "abc";
		// 0
		result = MemoryUtil.sizeOf(stringVaule);
		System.out.println("String mem size: " + result);
		//
		Date date = new Date();
		result = MemoryUtil.sizeOf(date);
		// 24
		System.out.println("Date mem size: " + result);
		//
		Integer integerValue = new Integer(0);
		result = MemoryUtil.sizeOf(integerValue);
		// 16
		System.out.println("Integer mem size: " + result);
		//
		Kryo kryo = new Kryo();
		result = MemoryUtil.sizeOf(kryo);
		// 759579
		System.out.println("Kryo mem size: " + result);
		//
		MemoryCounter memCounter = new MemoryCounter();
		result = MemoryUtil.sizeOf(memCounter);
		// 408
		System.out.println("MemoryCounter mem size: " + result);
		//
		StringBuilder buff = new StringBuilder();
		result = MemoryUtil.sizeOf(buff);
		// 408
		System.out.println("StringBuilder mem size: " + result);
	}

}