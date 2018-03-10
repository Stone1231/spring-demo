package com.demo.utils;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class DateUtilTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void DateSplitTest() {
		Date value = new Date("2015/05/20 23:00");
		value.getTime();
		String pattern = "yyyy-MM-dd";
		int splitHour = 4;

		SimpleDateFormat sdf = DateUtil.createSimpleDateFormat(pattern, null, null);
		String result = sdf.format(value);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(value.getTime());
		System.out.println(cal.get(Calendar.HOUR_OF_DAY));
		String no = String.format("%02d", (cal.get(Calendar.HOUR_OF_DAY) / splitHour) + 1);

		System.out.println(result + "-" + no);

	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	// round: 0.45 [+- 0.04], round.block: 0.03 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.46, time.warmup: 0.00,
	// time.bench: 0.46
	//
	// round: 0.47 [+- 0.05], round.block: 0.03 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 1, GC.time: 0.01, time.total: 0.48, time.warmup: 0.01,
	// time.bench: 0.47
	public void DateToStringTest() {
		Date value = new Date("2015/05/20 15:00");
		String result = DateUtil.toString(value, "yyyy-MM-dd", 4);
		// System.out.println(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.42 [+- 0.04], round.block: 0.01 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.42, time.warmup: 0.00,
	// time.bench: 0.42
	public void startOfDay() {
		long result = 0L;
		// 20160215
		long time = 1455505930000L;
		result = DateUtil.startOfDay(time);

		assertEquals(result, 1455494400000L);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.45 [+- 0.05], round.block: 0.01 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.47, time.warmup: 0.01,
	// time.bench: 0.46
	public void endOfDate() {
		long result = 0L;
		// 20160215
		long time = 1455505930000L;
		result = DateUtil.endOfDay(time);

		assertEquals(result, 1455580799999L);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.45 [+- 0.05], round.block: 0.01 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.45, time.warmup: 0.00,
	// time.bench: 0.45
	public void dateOffset() {
		long result = 0L;
		// 19700121
		long time = 1728000000L;

		result = DateUtil.dateOffset(time);
		assertEquals(20L, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.45 [+- 0.04], round.block: 0.01 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.45, time.warmup: 0.00,
	// time.bench: 0.45
	public void getHour() {
		long result = 0L;
		// 20170707 07:07:07
		long time = 1499411227000L;

		result = DateUtil.getHour(time);
		assertEquals(7L, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.42 [+- 0.04], round.block: 0.01 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.42, time.warmup: 0.00,
	// time.bench: 0.42
	public void getMinute() {
		long result = 0L;
		// 20170707 07:07:07
		long time = 1499411227000L;

		result = DateUtil.getMinute(time);
		assertEquals(7L, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.42 [+- 0.04], round.block: 0.01 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.44, time.warmup: 0.02,
	// time.bench: 0.42
	public void getSecond() {
		long result = 0L;
		// 20170707 07:07:07
		long time = 1499411227000L;

		result = DateUtil.getSecond(time);
		assertEquals(7L, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.41 [+- 0.04], round.block: 0.01 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.42, time.warmup: 0.00,
	// time.bench: 0.42
	public void getMinuteSliceIndex() {
		long result = 0;

		// 20190909 09:09:09
		long time = 1568020149000L;
		result = DateUtil.getMinuteSliceIndex(time, 2);

		assertEquals(5, result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.45 [+- 0.05], round.block: 0.01 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.02, time.total: 0.46, time.warmup: 0.00,
	// time.bench: 0.46
	public void getUTCTime() {
		Long result = DateUtil.getUTCTime();

		assertTrue(result <= System.currentTimeMillis());

		System.out.println(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.60 [+- 0.06], round.block: 0.01 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 4, GC.time: 0.01, time.total: 0.61, time.warmup: 0.00,
	// time.bench: 0.61
	public void getUTCTimeInOpenFireFormat() {
		String result = null;
		result = DateUtil.getUTCTimeInOpenFireFormat();

		assertNotNull(result);

		System.out.println(result);
	}

}
