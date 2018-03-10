package com.demo.utils;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class CryptoUtilTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void md5() {

		String a = "abc";
		String b = CryptoUtil.md5(a);
		String c = "900150983CD24FB0D6963F7D28E17F72";

		Boolean result = b.equals(c);
		assertNotNull(result);
		assertTrue(result.booleanValue());

	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void hmac_sha256() {

		String a = "abc";
		String b = CryptoUtil.hmac_sha256("a", a);
		String c = "tP/mfSZ9nrGfgczW/83hRDKU0AiS1bZI8D5ZC1YlDO0=";
		System.out.println(b);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void aesEncrypt() {
		String a = "abc";
		String b = CryptoUtil.aesEncrypt("a", a);
		System.out.println(b);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void aesDecrypt() {
		String a = "+TP8iwyooEKkr4IEV7mnfg==";
		String b = CryptoUtil.aesDecrypt("a", a);
		String c = "abc";
		Boolean result = b.equals(c);
		assertNotNull(result);
		assertTrue(result.booleanValue());
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void computeSha256() {
		String result = null;
		result = CryptoUtil.computeSha256("abc".getBytes());

		assertNotNull(result);
		assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad", result);
		System.out.println(result);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void nioComputeSha256() {
		String result = null;
		result = CryptoUtil.nioComputeSha256(new File("build.gradle"));

		assertNotNull(result);
		System.out.println(result);
	}
}
