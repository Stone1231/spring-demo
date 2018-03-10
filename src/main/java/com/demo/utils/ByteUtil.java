package com.demo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ByteUtil {

	private ByteUtil() {
	}

	// --------------------------------------------------
	// toBytes:
	// --------------------------------------------------
	// boolean
	// char
	// String

	// byte
	// short
	// int
	// long
	// float
	// double

	public static byte[] toBytes(Boolean value) {
		return toBytes(BooleanUtil.safeGet(value));
	}

	/**
	 * boolean -> byte[]
	 *
	 * @param value
	 * @return
	 */
	public static byte[] toBytes(boolean value) {
		// bool -> byte=0/1
		return new byte[] { (byte) (value ? 0x01 : 0x00) };
	}

	public static byte[] toBytes(Character value) {
		return toBytes(CharUtil.safeGet(value));
	}

	public static byte[] toBytes(char value) {
		byte[] result = new byte[2];
		result[1] = (byte) (value & 0x000000ff);
		result[0] = (byte) ((value & 0x0000ff00) >>> 8);
		return result;
	}

	public static byte[] toBytes(String value) {
		return toBytes(value, EncodingUtil.UTF_8);
	}

	/**
	 * String -> byte[]
	 *
	 * @param value
	 * @param charsetName
	 * @return
	 */
	public static byte[] toBytes(String value, String charsetName) {
		byte[] result = new byte[0];
		try {
			if (value != null) {
				if (charsetName != null) {
					/**
					 * #issue 較慢
					 * 
					 * Charset charset = Charset.forName(charsetName);
					 * 
					 * result = value.getBytes(charset);
					 */
					result = value.getBytes(charsetName);

				} else {
					result = value.getBytes();
				}
			}
		} catch (Exception ex) {
			//
		}
		return result;
	}

	public static byte[] toBytes(Byte value) {
		return toBytes(NumberUtil.safeGet(value));
	}

	/**
	 * byte -> byte[]
	 *
	 * @param value
	 * @param charsetName
	 * @return
	 */
	public static byte[] toBytes(byte value) {
		byte[] result = new byte[1];
		result[0] = value;
		return result;
	}

	public static byte[] toBytes(Short value) {
		return toBytes(NumberUtil.safeGet(value));
	}

	/**
	 * short -> byte[]
	 *
	 * @param value
	 * @param charsetName
	 * @return
	 */
	public static byte[] toBytes(short value) {
		byte[] result = new byte[2];
		result[1] = (byte) (value & 0x000000ff);
		result[0] = (byte) ((value & 0x0000ff00) >>> 8);
		return result;
	}

	public static byte[] toShortBytes(Integer value) {
		return toShortBytes(NumberUtil.safeGet(value));
	}

	/**
	 * int -> short -> 2 byte
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toShortBytes(int value) {
		return toBytes((short) value);
	}

	public static byte[] toBytes(Integer value) {
		return toBytes(NumberUtil.safeGet(value));
	}

	/**
	 * int -> byte[]
	 *
	 * @param value
	 * @return
	 */
	public static byte[] toBytes(int value) {
		byte[] result = new byte[4];
		result[3] = (byte) (value & 0x000000ff);
		result[2] = (byte) ((value & 0x0000ff00) >>> 8);
		result[1] = (byte) ((value & 0x00ff0000) >>> 16);
		result[0] = (byte) ((value & 0xff000000) >>> 24);
		return result;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toBytes(Long value) {
		return toBytes(NumberUtil.safeGet(value));
	}

	/**
	 * long -> byte[]
	 *
	 * @param value
	 * @return
	 */
	public static byte[] toBytes(long value) {
		byte[] result = new byte[8];

		result[7] = (byte) (value & 0x00000000000000ffl);
		result[6] = (byte) ((value & 0x000000000000ff00l) >>> 8);
		result[5] = (byte) ((value & 0x0000000000ff0000l) >>> 16);
		result[4] = (byte) ((value & 0x00000000ff000000l) >>> 24);

		result[3] = (byte) ((value & 0x000000ff00000000l) >>> 32);
		result[2] = (byte) ((value & 0x0000ff0000000000l) >>> 40);
		result[1] = (byte) ((value & 0x00ff000000000000l) >>> 48);
		result[0] = (byte) ((value & 0xff00000000000000l) >>> 56);
		return result;
	}

	public static byte[] toBytes(Float value) {
		return toBytes(NumberUtil.safeGet(value));
	}

	public static byte[] toBytes(float value) {
		int intValue = Float.floatToRawIntBits(value);
		return toBytes(intValue);
	}

	public static byte[] toBytes(Double value) {
		return toBytes(NumberUtil.safeGet(value));
	}

	public static byte[] toBytes(double value) {
		long longValue = Double.doubleToRawLongBits(value);
		return toBytes(longValue);
	}

	/**
	 * object -> byte[]
	 *
	 * @param value
	 * @return
	 */
	public static byte[] toBytes(Object value) {
		byte[] result = new byte[0];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(value);
			result = bos.toByteArray();
		} catch (Exception ex) {
			//

		} finally {
			IoUtil.close(out);
			IoUtil.close(bos);
		}
		return result;
	}

	// -------------------------------------------------------
	// 0xff=255,16進位
	// = 1111 1111,(2進位)

	/**
	 * byte[] -> boolean
	 *
	 * @param value
	 * @return
	 */
	public static boolean toBoolean(byte[] value) {
		return (value == null || value.length == 0) ? false : value[0] != 0x00;
	}

	public static char toChar(byte[] value) {
		if (value == null || value.length != 2) {
			return 0x00;
		}
		return (char) ((0xff & value[0]) << 8 | (0xff & value[1]) << 0);
	}

	public static String toString(byte[] value) {
		return toString(value, EncodingUtil.UTF_8);
	}

	/**
	 * byte[] -> String
	 *
	 * @param value
	 * @param charsetName
	 * @return
	 */
	public static String toString(byte[] value, String charsetName) {
		String result = null;

		try {
			if (value != null) {
				/**
				 * #issue 較慢
				 * 
				 * Charset charset = Charset.forName(charsetName);
				 * 
				 * result = new String(value, charset);
				 */
				result = new String(value, charsetName);
			}
		} catch (Exception ex) {
			//
		}
		return result;
	}

	/**
	 * List<Integer> -> byte[]
	 * 
	 * @param list
	 * @return
	 */
	public static byte[] toSingleBytes(List<Integer> list) {
		byte[] array = new byte[list.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = ByteUtil.toShortBytes(list.get(i))[1];
		}
		return array;
	}

	/**
	 * byte[] -> byte
	 *
	 * @param value
	 * @return
	 */
	public static byte toByte(byte[] value) {
		return (value == null || value.length == 0) ? 0x0 : value[0];
	}

	/**
	 * byte[] -> short
	 *
	 * @param value
	 * @return
	 */
	public static short toShort(byte[] value) {
		if (value == null || value.length != 2) {
			return 0x0;
		}
		return (short) ((0xff & value[0]) << 8 | (0xff & value[1]) << 0);
	}

	public static int toShortInt(byte[] value) {
		return toShortInt(value, 0);
	}

	/**
	 * (short)byte[] -> 2 byte -> short -> int
	 *
	 * @param value
	 * @param offset
	 * @return
	 */
	public static int toShortInt(byte[] value, int offset) {
		int result = 0;
		result = result | (value[offset + 1] & 0xff);
		result = result | ((value[offset + 0] & 0xff) << 8);
		return result;
	}

	public static int toInt(byte[] value) {
		return toInt(value, 0);
	}

	/**
	 * byte[] -> int
	 *
	 * @param value
	 * @param offset
	 * @return
	 */
	public static int toInt(byte[] value, int offset) {
		int result = 0;
		result = result | (value[offset + 3] & 0xff);
		result = result | (((int) value[offset + 2] & 0xff) << 8);
		result = result | (((int) value[offset + 1] & 0xff) << 16);
		result = result | (((int) value[offset + 0] & 0xff) << 24);
		return result;
	}

	public static long toLong(byte[] value) {
		return toLong(value, 0);
	}

	/**
	 * byte[] -> long
	 *
	 * @param value
	 * @param offset
	 * @return
	 */
	public static long toLong(byte[] value, int offset) {
		long result = 0L;
		result = result | (value[7] & 0xff);
		result = result | (((long) value[offset + 6] & 0xff) << 8);
		result = result | (((long) value[offset + 5] & 0xff) << 16);
		result = result | (((long) value[offset + 4] & 0xff) << 24);
		result = result | (((long) value[offset + 3] & 0xff) << 32);
		result = result | (((long) value[offset + 2] & 0xff) << 40);
		result = result | (((long) value[offset + 1] & 0xff) << 48);
		result = result | (((long) value[offset + 0] & 0xff) << 56);
		return result;
	}

	public static float toFloat(byte[] value) {
		int intValue = toInt(value);
		return Float.intBitsToFloat(intValue);
	}

	public static double toDouble(byte[] value) {
		long longValue = toLong(value);
		return Double.longBitsToDouble(longValue);
	}

	public static Object toObject(byte[] value) {
		Object result = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(value);
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			result = in.readObject();
		} catch (Exception ex) {
			//
		} finally {
			IoUtil.close(bis);
			IoUtil.close(in);
		}
		return result;
	}

	// -------------------------------------------------------

	/**
	 * 列印byte[]內容
	 *
	 * @param value
	 */
	public static void println(byte[] value) {
		StringBuilder sb = new StringBuilder();
		if (value != null) {
			for (int i = 0; i < value.length; i++) {
				sb.append(value[i]);
				if (i < value.length - 1) {
					sb.append(" ");
				}
			}
		}
		if (sb.length() > 0) {
			System.out.println(sb.toString());
		}
	}

	/**
	 * 讀取byte[], 從pos開始, 讀取長度length
	 * 
	 * 與UnsafeHelper.getByteArray() 效率一樣
	 * 
	 * @param value
	 * @param pos
	 * @param length
	 * @return
	 */
	public static byte[] getBytes(byte[] value, int pos, int length) {
		byte[] result = new byte[length];
		try {
			System.arraycopy(value, pos, result, 0, length);
		} catch (Exception ex) {
			//
		}
		return result;
	}

	/**
	 * 壓縮byte[]
	 *
	 * @param value
	 * @return
	 */
	public static byte[] compress(byte[] value) {
		byte[] result = null;
		try {
			if (value != null) {
				byte[] buff = new byte[65536 * 8];// 512k
				Deflater compresser = new Deflater();
				compresser.setInput(value);
				compresser.finish();
				int length = compresser.deflate(buff);
				//
				result = Arrays.copyOf(buff, length);
			}
		} catch (Exception ex) {
			//
		}
		return result;
	}

	/**
	 * 解壓縮byte[]
	 *
	 * @param value
	 * @return
	 */
	public static byte[] decompress(byte[] value) {
		byte[] result = null;
		try {
			if (value != null) {
				byte[] buff = new byte[65536 * 8];// 512k
				Inflater decompresser = new Inflater();
				decompresser.setInput(value);
				int length = decompresser.inflate(buff);
				decompresser.end();
				//
				result = Arrays.copyOf(buff, length);
			}
		} catch (Exception ex) {
			//
		}
		return result;
	}

	/**
	 * 是否為空值
	 *
	 * @param values
	 * @return
	 */
	public static boolean isEmpty(byte[] values) {
		return (values == null || values.length == 0);
	}

	/**
	 * 是否不為空值
	 *
	 * @param values
	 * @return
	 */
	public static boolean isNotEmpty(byte[] values) {
		return (values != null && values.length > 0);
	}

	public static byte[] randomBytes(int length) {
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[i] = NumberUtil.randomByte();
		}
		return result;
	}

	public static int fromShortInt(byte[] value) {
		return fromShortInt(value, 0);
	}

	/**
	 * (short)byte[] -> 2 byte -> short -> int
	 * 
	 * @param value
	 * @param offset
	 * @return
	 */
	public static int fromShortInt(byte[] value, int offset) {
		int result = 0;
		result = result | (value[offset + 1] & 0xff);
		result = result | ((value[offset + 0] & 0xff) << 8);
		return result;
	}
}
