package com.demo.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class StringUtilTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(StringUtilTest.class);

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	public static class ByteArrayTest extends StringUtilTest {

		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		@Test
		// round: 0.01 [+- 0.01], round.block: 0.00 [+- 0.00], round.gc: 0.00
		// [+- 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.02, time.warmup:
		// 0.00, time.bench: 0.02
		public void innerTrim_List() {
			String value = "42 4D";
			//
			byte[] byteArray = value.getBytes();
			List<Byte> buff = new LinkedList<Byte>();
			// List<Byte> buff = new ArrayList<Byte>();
			for (int i = 0; i < byteArray.length; i++) {
				byte entry = byteArray[i];
				if (entry == (byte) 32) {
					continue;
				}
				buff.add(entry);
			}
			//
			String result = null;
			if (buff.size() > 0) {
				// result=
				Byte[] byteBuff = buff.toArray(new Byte[] {});
				result = new String(ArrayUtils.toPrimitive(byteBuff));
			}
			// System.out.println(result);
		}

		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		@Test
		// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00
		// [+- 0.00], GC.calls: 2, GC.time: 0.01, time.total: 0.01, time.warmup:
		// 0.00, time.bench: 0.01
		public void innerTrim_ByteArrayOutputStream() throws Exception {
			String value = "42 4D";
			//
			byte[] byteArray = value.getBytes();
			ByteArrayOutputStream buff = new ByteArrayOutputStream();
			for (int i = 0; i < byteArray.length; i++) {
				byte entry = byteArray[i];
				if (entry == (byte) 32) {
					continue;
				}
				buff.write(entry);
			}
			buff.close();
			//
			String result = null;
			if (buff.size() > 0) {
				result = new String(buff.toByteArray());
			}
			// System.out.println(result);
		}

		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		@Test
		// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00
		// [+- 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.01, time.warmup:
		// 0.00, time.bench: 0.01
		public void innerTrim_StringBuilder() throws Exception {
			String value = "42 4D";
			//
			byte[] byteArray = value.getBytes();
			StringBuilder buff = new StringBuilder();
			for (int i = 0; i < byteArray.length; i++) {
				byte entry = byteArray[i];
				if (entry == (byte) 32) {
					continue;
				}
				buff.append(new String(new byte[] { entry }));
			}
			//
			String result = null;
			if (buff.length() > 0) {
				result = buff.toString();
			}
			// System.out.println(result);
		}

		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		@Test
		// round: 0.07 [+- 0.01], round.block: 0.00 [+- 0.00], round.gc: 0.00
		// [+- 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.08, time.warmup:
		// 0.00, time.bench: 0.08
		public void innerTrim_FixArray() throws Exception {
			String value = "42 4D";
			//
			byte[] byteArray = value.getBytes();
			byte[] buff = new byte[byteArray.length];
			int count = 0;
			for (int i = 0; i < byteArray.length; i++) {
				byte entry = byteArray[i];
				if (entry == (byte) 32) {
					continue;
				}
				buff[count++] = entry;
			}
			//
			String result = null;
			if (count > 0) {
				result = new String(ByteUtil.getBytes(buff, 0, count));
			}
			// System.out.println(result);
		}

		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		@Test
		// round: 0.08 [+- 0.01], round.block: 0.01 [+- 0.00], round.gc: 0.00
		// [+- 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.09, time.warmup:
		// 0.00, time.bench: 0.09
		public void innerTrim_StringReplace() throws Exception {
			String value = "42 4D";
			//
			String buff = StringUtil.replace(value, " ", "");
			//
			String result = null;
			if (buff.length() > 0) {
				result = buff.toString();
			}
			// System.out.println(result);
		}
	}

	public static class CharArrayTest extends StringUtilTest {

		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		@Test
		// round: 0.01 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00
		// [+- 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.01, time.warmup:
		// 0.00, time.bench: 0.01
		public void innerTrim_List() {
			String value = "42 4D";
			//
			char[] charArray = value.toCharArray();
			List<Character> buff = new LinkedList<Character>();
			for (int i = 0; i < charArray.length; i++) {
				char entry = charArray[i];
				if (entry == ' ') {
					continue;
				}
				buff.add(entry);
			}
			//
			String result = null;
			if (buff.size() > 0) {
				Character[] charBuff = buff.toArray(new Character[] {});
				result = new String(ArrayUtils.toPrimitive(charBuff));
			}
			System.out.println(result);
		}

		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		@Test
		// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00
		// [+- 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.01, time.warmup:
		// 0.00, time.bench: 0.01
		public void innerTrim_ByteArrayOutputStream() throws Exception {
			String value = "42 4D";
			//
			char[] charArray = value.toCharArray();
			ByteArrayOutputStream buff = new ByteArrayOutputStream();
			for (int i = 0; i < charArray.length; i++) {
				char entry = charArray[i];
				if (entry == ' ') {
					continue;
				}
				buff.write(entry);
			}
			buff.close();
			//
			String result = null;
			if (buff.size() > 0) {
				result = new String(buff.toByteArray());
			}
			// System.out.println(result);
		}

		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		@Test
		// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00
		// [+- 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.01, time.warmup:
		// 0.00, time.bench: 0.01
		public void innerTrim_StringBuilder() throws Exception {
			String value = "42 4D";
			//
			char[] charArray = value.toCharArray();
			StringBuilder buff = new StringBuilder();
			for (int i = 0; i < charArray.length; i++) {
				char entry = charArray[i];
				if (entry == ' ') {
					continue;
				}
				buff.append(entry);
			}
			//
			String result = null;
			if (buff.length() > 0) {
				result = buff.toString();
			}
			// System.out.println(result);
		}

		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		@Test
		// round: 0.00 [+- 0.00], round.block: 0.00 [+- 0.00], round.gc: 0.00
		// [+- 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.01, time.warmup:
		// 0.00, time.bench: 0.01
		public void innerTrim_FixArray() throws Exception {
			String value = "42 4D";
			//
			char[] charArray = value.toCharArray();
			char[] buff = new char[charArray.length];
			int count = 0;
			for (int i = 0; i < charArray.length; i++) {
				char entry = charArray[i];
				if (entry == ' ') {
					continue;
				}
				buff[count++] = entry;
			}
			//
			String result = null;
			if (count > 0) {
				char[] charBuff = new char[count];
				System.arraycopy(buff, 0, charBuff, 0, count);
				result = new String(charBuff);
			}
			// System.out.println(result);
		}

		@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
		@Test
		// round: 0.08 [+- 0.01], round.block: 0.01 [+- 0.00], round.gc: 0.00
		// [+- 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.09, time.warmup:
		// 0.00, time.bench: 0.09
		public void innerTrim_StringReplace() throws Exception {
			String value = "42 4D";
			//
			String buff = StringUtil.replace(value, String.valueOf(' '), "");
			//
			String result = null;
			if (buff.length() > 0) {
				result = buff.toString();
			}
			// System.out.println(result);
		}
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	// round: 0.08 [+- 0.01], round.block: 0.01 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 2, GC.time: 0.00, time.total: 0.09, time.warmup: 0.01,
	// time.bench: 0.09
	public void innerTrim() {
		String value = "42 4D";
		String result = StringUtil.innerTrim(value);
		assertTrue(result.indexOf(" ") < 0);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.65 [+- 0.08], round.block: 0.02 [+- 0.03], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.05, time.total: 0.70, time.warmup: 0.00,
	// time.bench: 0.70
	public void notEmpty() {
		String value = "12 3";
		boolean result = false;

		result = StringUtil.notEmpty(value);
		assertTrue(result);

		value = "";
		result = StringUtil.notEmpty(value);
		assertFalse(result);

	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.41 [+- 0.04], round.block: 0.01 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.42, time.warmup: 0.00,
	// time.bench: 0.42
	public void notBlank() {

		String value = "123";
		boolean result = false;

		result = StringUtil.notBlank(value);
		assertTrue(result);

		value = " ";
		result = StringUtil.notBlank(value);
		assertFalse(result);

	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	// round: 0.42 [+- 0.04], round.block: 0.01 [+- 0.00], round.gc: 0.00 [+-
	// 0.00], GC.calls: 3, GC.time: 0.01, time.total: 0.44, time.warmup: 0.02,
	// time.bench: 0.42
	public void isNullOrEmpty() {

		String value = "123";
		boolean result = false;

		result = StringUtil.isNullOrEmpty(value);
		assertFalse(result);

		value = "";
		result = StringUtil.isNullOrEmpty(value);
		assertTrue(result);

		value = null;
		result = StringUtil.isNullOrEmpty(value);
		assertTrue(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void replace() {
		String value = "abcdef_bcd_bcd";
		String result = null;

		result = StringUtil.replace(value, "bcd", "123");
		System.out.println(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void log() {
		long realChecksum = 1234L;
		long checksum = 5555L;
		LOGGER.error("Checksum [{}] not equal expected [{}]", realChecksum, checksum);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void aaa() {
		String result = "123";
		result.replace("23", "aa");
		System.out.println(result);
		//
		result = result.replace("23", "aa");
		System.out.println(result);

	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void isValidBANO() {
		assertTrue(StringUtil.isValidBANO("13136368"));
		assertTrue(StringUtil.isValidBANO("00147377"));// 協泉企業股份有限公司
		assertTrue(StringUtil.isValidBANO("00146074"));// 青山有限公司
		assertTrue(StringUtil.isValidBANO("22099131"));// 台灣積體電路製造股份有限公司

		assertFalse(StringUtil.isValidBANO(null));
		assertFalse(StringUtil.isValidBANO(""));
		assertFalse(StringUtil.isValidBANO("13136367"));
		assertFalse(StringUtil.isValidBANO("13136367a"));
		assertFalse(StringUtil.isValidBANO("1313636A"));
		assertFalse(StringUtil.isValidBANO(" 1313636A "));
		assertFalse(StringUtil.isValidBANO("A3136367"));
		assertFalse(StringUtil.isValidBANO("2209 131"));
		// 2017/11/23 cheng
		System.out.println(StringUtil.isValidBANO("2a0b9c3d"));
		System.out.println(StringUtil.isValidBANO("a2b9c1d1"));
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void isValidCreditCardNo() {
		assertTrue(StringUtil.isValidCreditCardNo("1234567891234563"));
		assertTrue(StringUtil.isValidCreditCardNo(" 1234567891234563 "));

		assertFalse(StringUtil.isValidCreditCardNo(null));
		assertFalse(StringUtil.isValidCreditCardNo(""));
		assertFalse(StringUtil.isValidCreditCardNo("123456789123456a"));
		assertFalse(StringUtil.isValidCreditCardNo("a234567891234563"));
		assertFalse(StringUtil.isValidCreditCardNo(" a234567891234563 "));
		assertFalse(StringUtil.isValidCreditCardNo("% 234567891234563"));
		// 2017/11/23 cheng
		System.out.println(StringUtil.isValidCreditCardNo("1a3b567891234563"));
		System.out.println(StringUtil.isValidCreditCardNo("a2b4567891234563"));
	}
}
