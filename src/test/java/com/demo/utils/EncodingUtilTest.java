package com.demo.utils;

import org.apache.commons.codec.binary.Hex;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class EncodingUtilTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	protected static String bytesToHexString(byte[] src) {

		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void bytesToHexString() {
		byte[] value = new byte[] { 66, 77 };
		String result = bytesToHexString(value);// 424d
		System.out.println(result);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void encodeHex() throws Exception {
		byte[] value = new byte[] { 66, 77 };
		String result = EncodingUtil.encodeHex(value);
		System.out.println(result);// 424d
		//
		byte[] buff = EncodingUtil.decodeHex("424d");
		SystemUtil.println(buff);// 66, 77
		//
		buff = EncodingUtil.decodeHex("424D");
		SystemUtil.println(buff);// 66, 64 -> 66, 77

		value = new byte[] { 66, 64 };
		result = EncodingUtil.encodeHex(value);
		System.out.println(result);// 4240

		value = new byte[] { 66, 77 };
		result = new String(Hex.encodeHex(value, true));
		System.out.println(result);// 424d
		//
		result = new String(Hex.encodeHex(value, false));
		System.out.println(result);// 424D
		//
		buff = Hex.decodeHex("424d".toCharArray());
		SystemUtil.println(buff);// 66, 77
		//
		buff = Hex.decodeHex("424D".toCharArray());
		SystemUtil.println(buff);// 66, 77
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void encodeHex2() throws Exception {
		String result = EncodingUtil.encodeHex("P1".getBytes());
		System.out.println(result);// 5031
		result = EncodingUtil.encodeHex("P2".getBytes());
		System.out.println(result);// 5032
		result = EncodingUtil.encodeHex("P3".getBytes());
		System.out.println(result);// 5033
		result = EncodingUtil.encodeHex("P4".getBytes());
		System.out.println(result);// 5034
		result = EncodingUtil.encodeHex("P5".getBytes());
		System.out.println(result);// 5035
		result = EncodingUtil.encodeHex("P6".getBytes());
		System.out.println(result);// 5036
	}

}
