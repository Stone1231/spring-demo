package com.demo.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

/**
 * The Class SubstringTest.
 */
public class SubstringTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	/**
	 * Substring.
	 *
	 * memory leak
	 */
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 84, GC.time: 0.17, time.total: 0.65, time.warmup: 0.01,
	// time.bench: 0.64
	public void substring() {
		List<String> result = new ArrayList<String>(10000);
		result.add(new Substring().substring());
	}

	/**
	 * The Class Substring.
	 */
	protected class Substring {

		/** The value. */
		// 1M
		private String value = new String(new byte[1 * 1024 * 1024]);

		/**
		 * Substring.
		 *
		 * @return the string
		 */
		public String substring() {
			return this.value.substring(0, 2);
		}
	}

	/**
	 * New string substring.
	 *
	 * #fix 1
	 */
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	// round: 0.00 [+- 0.01], round.block: 0.00 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 92, GC.time: 0.17, time.total: 0.64, time.warmup: 0.01,
	// time.bench: 0.63
	public void newStringSubstring() {
		List<String> result = new ArrayList<String>(10000);
		result.add(new NewStringSubstring().substring());
	}

	/**
	 * The Class NewStringSubstring.
	 */
	protected class NewStringSubstring {

		/** The value. */
		// 1M
		private String value = new String(new byte[1 * 1024 * 1024]);

		/**
		 * Substring.
		 *
		 * @return the string
		 */
		public String substring() {
			return new String(this.value.substring(0, 2));
		}
	}

	/**
	 * String builder substring.
	 *
	 * #fix 2
	 */
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	// @BenchmarkOptions(benchmarkRounds = 1000, warmupRounds = 1, concurrency =
	// 1000)
	public void stringBuilderSubstring() {
		List<String> result = new ArrayList<String>(10000);
		result.add(new StringBuilderSubstring().substring());
	}

	/**
	 * The Class StringBuilderSubstring.
	 */
	protected class StringBuilderSubstring {

		/** The value. */
		// 1M
		private String value = new String(new byte[1 * 1024 * 1024]);

		/**
		 * Substring.
		 *
		 * @return the string
		 */
		public String substring() {
			StringBuilder buff = new StringBuilder();
			buff.append(value);
			return buff.substring(0, 2);
		}
	}
}
