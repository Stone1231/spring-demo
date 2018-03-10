package com.demo.utils;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class SerializeUtilTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	protected List<Object> mockList() {
		List<Object> result = new ArrayList<Object>();
		result.add("TEST_STRING");
		result.add("測試字串");
		result.add(new Date());
		result.add(new Integer(0));
		result.add(new StringBuilder("StringBuilder"));
		return result;
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void sizeOf() {
		List<Object> value = mockList();
		long result = MemoryUtil.sizeOf(value);
		// 210
		System.out.println(result);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	// round: 0.53 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00],
	// GC.calls: 0, GC.time: 0.00, time.total: 0.55, time.warmup: 0.00, time.bench:
	// 0.55
	public void serialize() {
		List<Object> value = mockList();
		byte[] result = null;
		//
		result = SerializeUtil.serialize(value);
		// 334
		System.out.println(result.length);
		assertNotNull(result);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test(expected = IllegalArgumentException.class)
	public void serializeException() {
		List<Object> value = mockList();
		//
		SerializeUtil.serialize(value, null);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	// round: 0.54 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00],
	// GC.calls: 0, GC.time: 0.00, time.total: 0.54, time.warmup: 0.00, time.bench:
	// 0.54
	public void deserialize() {
		List<Object> list = mockList();
		byte[] value = SerializeUtil.serialize(list);

		List<Object> result = null;
		//
		result = SerializeUtil.deserialize(value);
		//
		System.out.println(result.size() + ", " + result);
		assertNotNull(result);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	// round: 0.70 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00],
	// GC.calls: 0, GC.time: 0.00, time.total: 0.72, time.warmup: 0.00, time.bench:
	// 0.72
	public void kryo() {
		List<Object> value = mockList();
		byte[] result = null;
		//
		result = SerializeUtil.kryo(value);
		// 95
		System.out.println(result.length);
		assertNotNull(result);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	// round: 0.62 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00],
	// GC.calls: 0, GC.time: 0.00, time.total: 0.62, time.warmup: 0.00, time.bench:
	// 0.62
	public void dekryo() {
		List<Object> list = mockList();
		byte[] value = SerializeUtil.kryo(list);
		//
		List<Object> result = SerializeUtil.dekryo(value, ArrayList.class);
		//
		System.out.println(result.size() + ", " + result);
		assertNotNull(result);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	// round: 0.60 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00],
	// GC.calls: 0, GC.time: 0.00, time.total: 0.60, time.warmup: 0.00, time.bench:
	// 0.60
	public void kryoWriteClass() {
		List<Object> value = mockList();
		byte[] result = null;
		//
		result = SerializeUtil.kryoWriteClass(value);
		// 116
		System.out.println(result.length);
		assertNotNull(result);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	// round: 0.60 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+- 0.00],
	// GC.calls: 0, GC.time: 0.00, time.total: 0.61, time.warmup: 0.00, time.bench:
	// 0.61
	public void dekryoReadClass() {
		List<Object> list = mockList();
		byte[] value = SerializeUtil.kryoWriteClass(list);
		//
		List<Object> result = SerializeUtil.dekryoReadClass(value);
		//
		System.out.println(result.size() + ", " + result);
		assertNotNull(result);
	}

}
