package com.demo.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
//import com.google.i18n.phonenumbers.NumberParseException;
import com.demo.exception.CommonsRuntimeException;

public class PhoneUtilTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.47 [+- 0.05], round.block: 0.02 [+- 0.01], round.gc: 0.00 [+-
	// 0.00], GC.calls: 4, GC.time: 0.01, time.total: 0.47, time.warmup: 0.00,
	// time.bench: 0.47
	public void phoneFormatter() throws CommonsRuntimeException {
		String result = PhoneUtil.PhoneFormatter("886", "+886912345678");

		assertEquals("0912345678", result);
	}
}
