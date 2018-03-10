package com.demo.utils;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.google.common.primitives.UnsignedBytes;

public class ByteUtilTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void lexicographicalComparator() {
		byte[] arrayA = new byte[] { 1, 2, 3, 4, 5 };
		byte[] arrayB = new byte[] { 1, 2, 3, 4, 6 };
		Comparator<byte[]> comparator = UnsignedBytes.lexicographicalComparator();
		int result = comparator.compare(arrayA, arrayB);// a < b = -1
		System.out.println(result);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void byteBuffer() {
		byte[] arrayA = new byte[] { 1, 2, 3, 4, 5 };
		byte[] arrayB = new byte[] { 1, 2, 3, 4, 6 };
		ByteBuffer bufferA = ByteBuffer.wrap(arrayA);
		ByteBuffer bufferB = ByteBuffer.wrap(arrayB);
		int result = bufferA.compareTo(bufferB);// a < b = -1
		System.out.println(result);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void arraysEquals() {
		byte[] arrayA = new byte[] { 1, 2, 3, 4, 5 };
		byte[] arrayB = new byte[] { 1, 2, 3, 4, 6 };
		boolean result = Arrays.equals(arrayA, arrayB); // a==b => false
		System.out.println(result);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void objectUtilEquals() {
		byte[] arrayA = new byte[] { 1, 2, 3, 4, 5 };
		byte[] arrayB = new byte[] { 1, 2, 3, 4, 5 };
		boolean result = ObjectUtil.equals(arrayA, arrayB);// a==b => false
		System.out.println(result);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void toBytes() {
		byte[] result = ByteUtil.toBytes("2015-05-01", "UTF-8");
		// SystemUtil.println(dir);
		result = ByteUtil.toBytes((String) null, null);
		result = ByteUtil.toBytes((String) null, "UTF-8");
		assertNotNull(result);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void modifyByteArray() {
		byte[] value = new byte[] { 0, 1, 2 };
		SystemUtil.println(value);// 0, 1, 2
		//
		modifyByteArray(value);
		assertEquals(value[0], 10);
		assertEquals(value[1], 20);
		SystemUtil.println(value);// 10, 20, 2
		//
		newByteArray(value);
		SystemUtil.println(value);// 10, 20, 2
		assertEquals(value[0], 10);
		assertEquals(value[1], 20);
	}

	public void modifyByteArray(byte[] value) {
		value[0] = (byte) 10;
		value[1] = (byte) 20;
	}

	public void newByteArray(byte[] value) {
		value = new byte[] { 99 };
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void toShortBytes() {
		int intTest = 1;

		byte[] testBytes = ByteUtil.toShortBytes(intTest);

		byte[] sourceBytes = new byte[2];
		sourceBytes[1] = (byte) (0x00000001);
		sourceBytes[0] = (byte) (0x00000000);

		Boolean result = true;

		for (int i = 0; i < sourceBytes.length; i++) {
			if (sourceBytes[i] != testBytes[i]) {
				result = false;
				break;
			}
		}

		assertNotNull(result);
		assertTrue(result.booleanValue());
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void toBytes2() {
		byte testByte = (byte) (0x00000001);
		byte[] testBytes = ByteUtil.toBytes(testByte);

		boolean result = (testBytes[0] == testByte);
		assertNotNull(result);
		assertTrue(result);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void fromShortInt() {
		short testShort = 1;
		byte[] testBytes = ByteUtil.toShortBytes(testShort);
		int testInt = ByteUtil.fromShortInt(testBytes);

		Boolean result = (testInt == 1);
		assertNotNull(result);
		assertTrue(result.booleanValue());
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void toInt() {

		byte[] testBytes = new byte[4];
		testBytes[0] = (byte) (0x00000000);
		testBytes[1] = (byte) (0x00000001);
		testBytes[2] = (byte) (0x00000002);
		testBytes[3] = (byte) (0x00000003);

		int result = ByteUtil.toInt(testBytes);
		System.out.println(result);
		assertEquals(66051, result);
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void toSingleBytes() {

		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);

		byte[] sourceBytes = new byte[3];
		sourceBytes[0] = (byte) (0x00000001);
		sourceBytes[1] = (byte) (0x00000002);
		sourceBytes[2] = (byte) (0x00000003);

		byte[] testBytes = ByteUtil.toSingleBytes(list);

		Boolean result = true;
		for (int i = 0; i < sourceBytes.length; i++) {
			if (sourceBytes[i] != testBytes[i]) {
				result = false;
				break;
			}
		}
		assertNotNull(result);
		assertTrue(result.booleanValue());
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void toChar() {
		char result = ByteUtil.toChar("AB".getBytes());
		System.out.println(result);
		assertNotNull(result);
	}
}
