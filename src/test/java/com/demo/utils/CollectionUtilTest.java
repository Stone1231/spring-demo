package com.demo.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;


public class CollectionUtilTest {


	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();	
	
//	public CollectionUtilTest() {
//	}

	// 20120319
//	public static <E> boolean isEmpty(Collection<E> values) {
//		return (values == null || values.isEmpty());
//	}
//
//	public static <K, V> boolean isEmpty(Map<K, V> values) {
//		return (values == null || values.isEmpty());
//	}
	
	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.02, time.warmup: 0.00,
	// time.bench: 0.02	
	public void isEmpty(){	
		List<String> list = new ArrayList<String>();
		Boolean result = CollectionUtil.isEmpty(list);
		assertNotNull(result);
		assertTrue(result.booleanValue());
		
		list.add("test");
		result = CollectionUtil.isEmpty(list);
		assertNotNull(result);
		assertFalse(result.booleanValue());
			
		Map<String,String> map = null;		
		result = CollectionUtil.isEmpty(map);
		assertNotNull(result);
		assertTrue(result.booleanValue());
		
		map = new HashMap<String, String>();	
		map.put("key1", "value1");
		result = CollectionUtil.isEmpty(map);
		assertNotNull(result);
		assertFalse(result.booleanValue());	
		
//		Boolean result = BooleanUtil.createBoolean(true);
//		assertNotNull(result);
//		assertTrue(result.booleanValue());
//		//
//		result = BooleanUtil.createBoolean(false);
//		assertFalse(result.booleanValue());
	}

//	public static <E> boolean notEmpty(Collection<E> values) {
//		return (values != null && !values.isEmpty());
//	}
//
//	public static <K, V> boolean notEmpty(Map<K, V> values) {
//		return (values != null && !values.isEmpty());
//	}
	
	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.02, time.warmup: 0.00,
	// time.bench: 0.02		
	public void notEmpty(){	
		List<String> list = new ArrayList<String>();
		Boolean result = CollectionUtil.notEmpty(list);
		assertNotNull(result);
		assertFalse(result.booleanValue());
		
		list.add("test");
		result = CollectionUtil.notEmpty(list);
		assertNotNull(result);
		assertTrue(result.booleanValue());
		
		Map<String,String> map = null;		
		result = CollectionUtil.notEmpty(map);
		assertNotNull(result);
		assertFalse(result.booleanValue());
		
		map = new HashMap<String, String>();	
		map.put("key1", "value1");
		result = CollectionUtil.notEmpty(map);
		assertNotNull(result);
		assertTrue(result.booleanValue());	
	}

//	public static <E> int size(Collection<E> values) {
//		int result = 0;
//		if (values != null) {
//			result = values.size();
//		}
//		return result;
//	}
//
//	public static <K, V> int size(Map<K, V> values) {
//		int result = 0;
//		if (values != null) {
//			result = values.size();
//		}
//		return result;
//	}
	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.02, time.warmup: 0.00,
	// time.bench: 0.02		
	public void size(){	
		List<String> list = new ArrayList<String>();
		int size = CollectionUtil.size(list);
		assertNotNull(size);
		assertTrue(size==0);
		
		list.add("test");
		size = CollectionUtil.size(list);
		assertNotNull(size);
		assertTrue(size==1);
		
		Map<String,String> map = null;		
		size = CollectionUtil.size(map);
		assertNotNull(size);
		assertTrue(size==0);
		
		map = new HashMap<String, String>();	
		map.put("key1", "value1");
		size = CollectionUtil.size(map);
		assertNotNull(size);
		assertTrue(size==1);			
	}
	
	
//	public static <E> Set<E> checkDuplicate(List<E> values) {
//		Set<E> result = new LinkedHashSet<E>();
//		//
//		Set<E> buff = new LinkedHashSet<E>();
//		for (E e : values) {
//			if (!buff.add(e)) {
//				result.add(e);
//			}
//		}
//		return result;
//	}
	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.02, time.warmup: 0.00,
	// time.bench: 0.02		
	public void checkDuplicate(){		
		List<String> list = new ArrayList<String>();
		list.add("test1");
		list.add("test2");
		Set<String> set = CollectionUtil.checkDuplicate(list);
		assertNotNull(set);
		assertTrue(set.size()==0);
		
		list.add("test1");
		set = CollectionUtil.checkDuplicate(list);
		assertNotNull(set);
		assertTrue(set.size()>0);
	}	
}
