package com.demo.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class IoUtilTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void read() {
		final String FILE_NAME = "build.gradle";
		InputStream value = IoUtil.createInputStream(FILE_NAME);

		// 第1次讀
		byte[] buff = IoUtil.read(value, 1024);
		System.out.println("length: " + buff.length);// 358912
		assertNotNull(buff);

		// 第2次讀,InputStream無法再讀一次,傳回null
		buff = IoUtil.read(value, 1024);
		assertNull(buff);
		//
		IoUtil.close(value);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void doInAction() {
		final String FILE_NAME = "build.gradle";
		File file = new File(FILE_NAME);
		// 檔案總長度
		long length = file.length();
		System.out.println("file length: " + length);// 358912
		//
		InputStream value = IoUtil.createInputStream(file);
		//
		final int BLOCK_LENGTH = 1024;
		int expectedBlockCount = (int) Math.ceil((length / (BLOCK_LENGTH * 1.0d)));
		System.out.println("expectedBlockCount: " + expectedBlockCount);
		//
		final AtomicInteger actualBlockCount = new AtomicInteger();
		final boolean READ_NEXT_BLOCK = false;
		//
		byte[] buff = IoUtil.read(value, BLOCK_LENGTH, new InputStreamCallback() {
			public boolean doInAction(byte[] blockArray) {
				// System.out
				// .println("block length: " + blockArray.length);

				actualBlockCount.incrementAndGet();

				// true=繼續往下一段byte[], false=中斷
				return READ_NEXT_BLOCK;
				// return false;
			}
		});

		System.out.println("actualBlockCount: " + actualBlockCount);
		System.out.println("byteArray length: " + buff.length);// 358912

		assertNotNull(buff);

		if (READ_NEXT_BLOCK) {
			assertEquals(expectedBlockCount, actualBlockCount.get());
		} else {
			assertEquals(1, actualBlockCount.get());
		}

		//
		IoUtil.close(value);
	}
}
