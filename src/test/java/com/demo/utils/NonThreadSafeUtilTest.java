package com.demo.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class NonThreadSafeUtilTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	// --------------------------------------------------------
	// string
	// --------------------------------------------------------
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void toStringV1() {
		String result = null;
		//
		result = NonThreadSafeUtil.toStringV1("abc");
		//
		if (!result.equals("abc") || result.length() != 3) {
			System.out.println("error length: " + result.length() + ", " + result);
		}
		System.out.println("[" + Thread.currentThread().getId() + "] " + result);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void toStringV1MultiThread() throws Exception {
		final int NUM_OF_THREADS = 1;
		final int NUM_OF_COUNT = 1;
		final AtomicInteger errorCounter = new AtomicInteger(0);
		//
		long beg = System.currentTimeMillis();
		ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
		for (int i = 0; i < NUM_OF_THREADS; i++) {
			service.submit(new Runnable() {
				public void run() {
					String result = null;
					//
					for (int i = 0; i < NUM_OF_COUNT; i++) {
						result = NonThreadSafeUtil.toStringV1("abc");

						// assert
						if (!result.equals("abc") || result.length() != 3) {
							errorCounter.incrementAndGet();
							System.out.println("[ERROR] length: " + result.length() + ", " + result);
						}
					}
					System.out.println("[" + Thread.currentThread().getId() + "] " + result);
				}
			});
		}
		//
		service.shutdown();
		service.awaitTermination(1, TimeUnit.MINUTES);
		//
		long end = System.currentTimeMillis();
		System.out.println(NUM_OF_THREADS * NUM_OF_COUNT + " times: " + (end - beg) + " mills. ");
		System.out.println("errorCounter: " + errorCounter.get());
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void toStringV2() {
		String result = null;
		//
		result = NonThreadSafeUtil.toStringV2("abc");
		//
		if (!result.equals("abc") || result.length() != 3) {
			System.out.println("error length: " + result.length() + ", " + result);
		}
		//
		System.out.println("[" + Thread.currentThread().getId() + "] " + result);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)

	@Test
	public void toStringV2MultiThread() throws Exception {
		final int NUM_OF_THREADS = 1;
		final int NUM_OF_COUNT = 1;
		final AtomicInteger errorCounter = new AtomicInteger(0);
		//
		long beg = System.currentTimeMillis();
		ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
		for (int i = 0; i < NUM_OF_THREADS; i++) {
			service.submit(new Runnable() {
				public void run() {
					String result = null;
					//
					for (int i = 0; i < NUM_OF_COUNT; i++) {
						result = NonThreadSafeUtil.toStringV2("abc");

						// assert
						if (!result.equals("abc") || result.length() != 3) {
							errorCounter.incrementAndGet();
							System.out.println("[ERROR] length: " + result.length() + ", " + result);
						}
					}
					System.out.println("[" + Thread.currentThread().getId() + "] " + result);
				}
			});
		}
		//
		service.shutdown();
		service.awaitTermination(1, TimeUnit.MINUTES);
		//
		long end = System.currentTimeMillis();
		System.out.println(NUM_OF_THREADS * NUM_OF_COUNT + " times: " + (end - beg) + " mills. ");
		System.out.println("errorCounter: " + errorCounter.get());
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void toStringV1Fix1() throws Exception {
		final int NUM_OF_THREADS = 1;
		final int NUM_OF_COUNT = 1;
		final AtomicInteger errorCounter = new AtomicInteger(0);
		//
		long beg = System.currentTimeMillis();
		ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
		for (int i = 0; i < NUM_OF_THREADS; i++) {
			service.submit(new Runnable() {
				public void run() {
					String result = null;
					//
					for (int i = 0; i < NUM_OF_COUNT; i++) {
						result = NonThreadSafeUtil.toStringV1Fix1("abc");

						// assert
						if (!result.equals("abc") || result.length() != 3) {
							errorCounter.incrementAndGet();
							System.out.println("[ERROR] length: " + result.length() + ", " + result);
						}
					}
					System.out.println("[" + Thread.currentThread().getId() + "] " + result);
				}
			});
		}
		//
		service.shutdown();
		service.awaitTermination(1, TimeUnit.MINUTES);
		//
		long end = System.currentTimeMillis();
		System.out.println(NUM_OF_THREADS * NUM_OF_COUNT + " times: " + (end - beg) + " mills. ");
		System.out.println("errorCounter: " + errorCounter.get());
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void toStringV1Fix2() throws Exception {
		final int NUM_OF_THREADS = 1;
		final int NUM_OF_COUNT = 1;
		final AtomicInteger errorCounter = new AtomicInteger(0);
		//
		long beg = System.currentTimeMillis();
		ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
		for (int i = 0; i < NUM_OF_THREADS; i++) {
			service.submit(new Runnable() {
				public void run() {
					String result = null;
					//
					for (int i = 0; i < NUM_OF_COUNT; i++) {
						result = NonThreadSafeUtil.toStringV1Fix2("abc");

						// assert
						if (!result.equals("abc") || result.length() != 3) {
							errorCounter.incrementAndGet();
							System.out.println("[ERROR] length: " + result.length() + ", " + result);
						}
					}
					System.out.println("[" + Thread.currentThread().getId() + "] " + result);
				}
			});
		}
		//
		service.shutdown();
		service.awaitTermination(1, TimeUnit.MINUTES);
		//
		long end = System.currentTimeMillis();
		System.out.println(NUM_OF_THREADS * NUM_OF_COUNT + " times: " + (end - beg) + " mills. ");
		System.out.println("errorCounter: " + errorCounter.get());
	}

	@Test
	public void toStringV1Fix3() throws Exception {
		final int NUM_OF_THREADS = 1;
		final int NUM_OF_COUNT = 1;
		final AtomicInteger errorCounter = new AtomicInteger(0);
		//
		long beg = System.currentTimeMillis();
		ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
		for (int i = 0; i < NUM_OF_THREADS; i++) {
			service.submit(new Runnable() {
				public void run() {
					String result = null;
					//
					for (int i = 0; i < NUM_OF_COUNT; i++) {
						result = NonThreadSafeUtil.toStringV1Fix3("abc");

						// assert
						if (!result.equals("abc") || result.length() != 3) {
							errorCounter.incrementAndGet();
							System.out.println("[ERROR] length: " + result.length() + ", " + result);
						}
					}
					System.out.println("[" + Thread.currentThread().getId() + "] " + result);
				}
			});
		}
		//
		service.shutdown();
		service.awaitTermination(1, TimeUnit.MINUTES);
		//
		long end = System.currentTimeMillis();
		System.out.println(NUM_OF_THREADS * NUM_OF_COUNT + " times: " + (end - beg) + " mills. ");
		System.out.println("errorCounter: " + errorCounter.get());
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void toStringV1Fix4() throws Exception {
		final int NUM_OF_THREADS = 1;
		final int NUM_OF_COUNT = 1;
		final AtomicInteger errorCounter = new AtomicInteger(0);
		//
		long beg = System.currentTimeMillis();
		ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
		for (int i = 0; i < NUM_OF_THREADS; i++) {
			service.submit(new Runnable() {
				public void run() {
					String result = null;
					//
					for (int i = 0; i < NUM_OF_COUNT; i++) {
						result = NonThreadSafeUtil.toStringV1Fix4("abc");

						// assert
						if (!result.equals("abc") || result.length() != 3) {
							errorCounter.incrementAndGet();
							System.out.println("[ERROR] length: " + result.length() + ", " + result);
						}
					}
					System.out.println("[" + Thread.currentThread().getId() + "] " + result);
				}
			});
		}
		//
		service.shutdown();
		service.awaitTermination(1, TimeUnit.MINUTES);
		//
		long end = System.currentTimeMillis();
		System.out.println(NUM_OF_THREADS * NUM_OF_COUNT + " times: " + (end - beg) + " mills. ");
		System.out.println("errorCounter: " + errorCounter.get());
	}

	// --------------------------------------------------------
	// outputstream
	// --------------------------------------------------------
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void toBytes() throws Exception {
		final int NUM_OF_THREADS = 1;
		final int NUM_OF_COUNT = 1;
		final AtomicInteger errorCounter = new AtomicInteger(0);
		//
		long beg = System.currentTimeMillis();
		ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
		for (int i = 0; i < NUM_OF_THREADS; i++) {
			service.submit(new Runnable() {
				public void run() {
					byte[] result = null;
					//
					for (int i = 0; i < NUM_OF_COUNT; i++) {
						result = NonThreadSafeUtil.toBytes(100);// 100

						// assert
						if (result == null || result.length != 1 || result[0] != 100) {
							errorCounter.incrementAndGet();
							SystemUtil.println("[ERROR] length: " + result.length + ", ", result);
						}
					}
					SystemUtil.println("[" + Thread.currentThread().getId() + "] ", result);
				}
			});
		}
		//
		service.shutdown();
		service.awaitTermination(1, TimeUnit.MINUTES);
		//
		long end = System.currentTimeMillis();
		System.out.println(NUM_OF_THREADS * NUM_OF_COUNT + " times: " + (end - beg) + " mills. ");
		System.out.println("errorCounter: " + errorCounter.get());
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void toBytesFix1() throws Exception {
		final int NUM_OF_THREADS = 1;
		final int NUM_OF_COUNT = 1;
		final AtomicInteger errorCounter = new AtomicInteger(0);
		//
		long beg = System.currentTimeMillis();
		ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
		for (int i = 0; i < NUM_OF_THREADS; i++) {
			service.submit(new Runnable() {
				public void run() {
					byte[] result = null;
					//
					for (int i = 0; i < NUM_OF_COUNT; i++) {
						result = NonThreadSafeUtil.toBytesFix1(100);// 100

						// assert
						if (result == null || result.length != 1 || result[0] != 100) {
							errorCounter.incrementAndGet();
							SystemUtil.println("[ERROR] length: " + result.length + ", ", result);
						}
					}
					SystemUtil.println("[" + Thread.currentThread().getId() + "] ", result);
				}
			});
		}
		//
		service.shutdown();
		service.awaitTermination(1, TimeUnit.MINUTES);
		//
		long end = System.currentTimeMillis();
		System.out.println(NUM_OF_THREADS * NUM_OF_COUNT + " times: " + (end - beg) + " mills. ");
		System.out.println("errorCounter: " + errorCounter.get());
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void toBytesFix2() throws Exception {
		final int NUM_OF_THREADS = 1;
		final int NUM_OF_COUNT = 1;
		final AtomicInteger errorCounter = new AtomicInteger(0);
		//
		long beg = System.currentTimeMillis();
		ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS);
		for (int i = 0; i < NUM_OF_THREADS; i++) {
			service.submit(new Runnable() {
				public void run() {
					byte[] result = null;
					//
					for (int i = 0; i < NUM_OF_COUNT; i++) {
						result = NonThreadSafeUtil.toBytesFix2(100);// 100

						// assert
						if (result == null || result.length != 1 || result[0] != 100) {
							errorCounter.incrementAndGet();
							SystemUtil.println("[ERROR] length: " + result.length + ", ", result);
						}
					}
					SystemUtil.println("[" + Thread.currentThread().getId() + "] ", result);
				}
			});
		}
		//
		service.shutdown();
		service.awaitTermination(1, TimeUnit.MINUTES);
		//
		long end = System.currentTimeMillis();
		System.out.println(NUM_OF_THREADS * NUM_OF_COUNT + " times: " + (end - beg) + " mills. ");
		System.out.println("errorCounter: " + errorCounter.get());
	}
}
