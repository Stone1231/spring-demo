package com.demo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * 序列化
 */
public final class SerializeUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(SerializeUtil.class);

	private static final String THE_KRYO_MUST_NOT_BE_NULL = "The Kryo must not be null";
	private static final String THE_VALUE_MUST_NOT_BE_NULL = "The Value must not be null";

	private SerializeUtil() {
	}

	/**
	 * jdk 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] serialize(Object value) {
		byte[] result = new byte[0];
		//
		AssertUtil.notNull(value, THE_VALUE_MUST_NOT_BE_NULL);
		long sizeOf = MemoryUtil.sizeOf(value);
		int size = NumberUtil.toInt(sizeOf + 64);
		//
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream(size);
			boolean serialized = serialize(value, baos);
			if (serialized) {
				result = baos.toByteArray();
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during serialize()", ex);
		} finally {
			IoUtil.close(baos);
		}
		return result;
	}

	/**
	 * jdk 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @param outputStream
	 * @return
	 */
	public static boolean serialize(Object value, OutputStream outputStream) {
		boolean result = false;
		//
		AssertUtil.notNull(value, THE_VALUE_MUST_NOT_BE_NULL);
		AssertUtil.notNull(outputStream, "The OutputStream must not be null");
		//
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(outputStream);
			out.writeObject(value);
			result = true;
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during serialize()", ex);
		} finally {
			IoUtil.close(out);
		}
		return result;
	}

	/**
	 * jdk 反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param value
	 * @return
	 */
	public static <T> T deserialize(byte[] value) {
		T result = null;
		//
		AssertUtil.notNull(value, THE_VALUE_MUST_NOT_BE_NULL);
		//
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(value);
			result = deserialize(bais);
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during deserialize()", ex);
		} finally {
			IoUtil.close(bais);
		}
		return result;
	}

	/**
	 * jdk 反序列化
	 * 
	 * inputStream -> object
	 * 
	 * @param inputStream
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(InputStream inputStream) {
		T result = null;
		//
		AssertUtil.notNull(inputStream, "The InputStream must not be null");
		//
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(inputStream);
			result = (T) in.readObject();
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during deserialize()", ex);
		} finally {
			IoUtil.close(in);
		}
		return result;
	}

	public static byte[] kryo(Object value) {
		return kryo(new Kryo(), value);
	}

	/**
	 * kryo 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] kryo(Kryo kryo, Object value) {
		byte[] result = new byte[0];
		//
		AssertUtil.notNull(kryo, THE_KRYO_MUST_NOT_BE_NULL);
		AssertUtil.notNull(value, THE_VALUE_MUST_NOT_BE_NULL);

		long sizeOf = MemoryUtil.sizeOf(value);
		int size = NumberUtil.toInt(sizeOf + 64);
		//
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream(size);
			boolean serialized = kryo(kryo, value, baos, size);
			if (serialized) {
				result = baos.toByteArray();
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during kryo()", ex);
		} finally {
			IoUtil.close(baos);
		}
		return result;
	}

	public static boolean kryo(Object value, OutputStream outputStream, int bufferSize) {
		return kryo(new Kryo(), value, outputStream, bufferSize);
	}

	/**
	 * keyo 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @param outputStream
	 * @return
	 */
	public static boolean kryo(Kryo kryo, Object value, OutputStream outputStream, int bufferSize) {
		boolean result = false;
		//
		AssertUtil.notNull(kryo, THE_KRYO_MUST_NOT_BE_NULL);
		AssertUtil.notNull(value, THE_VALUE_MUST_NOT_BE_NULL);
		AssertUtil.notNull(outputStream, "The OutputStream must not be null");
		//
		Output out = null;
		try {
			out = new Output(outputStream, bufferSize);
			kryo.writeObject(out, value);
			result = true;
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during kryo()", ex);
		} finally {
			IoUtil.close(out);
		}
		//
		return result;
	}

	public static <T> T dekryo(byte[] value, Class<?> clazz) {
		AssertUtil.notNull(value, THE_VALUE_MUST_NOT_BE_NULL);
		//
		int bufferSize = value.length;
		return dekryo(new Kryo(), value, bufferSize, clazz);
	}

	/**
	 * kryo 反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param value
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T dekryo(Kryo kryo, byte[] value, int bufferSize, Class<?> clazz) {
		T result = null;
		//
		AssertUtil.notNull(kryo, THE_KRYO_MUST_NOT_BE_NULL);
		AssertUtil.notNull(value, THE_VALUE_MUST_NOT_BE_NULL);
		//
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(value);
			result = (T) dekryo(kryo, bais, bufferSize, clazz);
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during dekryo()", ex);
		} finally {
			IoUtil.close(bais);
		}
		return result;
	}

	/**
	 * kryo 反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param inputStream
	 * @param clazz
	 * @return
	 */
	public static <T> T dekryo(Kryo kryo, InputStream inputStream, int bufferSize, Class<T> clazz) {
		T result = null;
		//
		AssertUtil.notNull(kryo, THE_KRYO_MUST_NOT_BE_NULL);
		AssertUtil.notNull(inputStream, "The InputStream must not be null");
		AssertUtil.notNull(clazz, "The Class must not be null");
		//
		Input in = null;
		try {
			in = new Input(inputStream, bufferSize);
			result = kryo.readObject(in, clazz);
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during dekryo()", ex);
		} finally {
			IoUtil.close(in);
		}
		//
		return result;
	}

	public static byte[] kryoWriteClass(Object value) {
		return kryoWriteClass(new Kryo(), value);
	}

	/**
	 * kryo 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] kryoWriteClass(Kryo kryo, Object value) {
		byte[] result = new byte[0];
		//
		AssertUtil.notNull(kryo, THE_KRYO_MUST_NOT_BE_NULL);
		AssertUtil.notNull(value, THE_VALUE_MUST_NOT_BE_NULL);

		long sizeOf = MemoryUtil.sizeOf(value);
		int size = NumberUtil.toInt(sizeOf + 64);
		//
		ByteArrayOutputStream baos = null;
		Output out = null;
		try {
			baos = new ByteArrayOutputStream(size);
			out = new Output(baos, size);
			//
			kryo.writeClassAndObject(out, value);
			result = out.toBytes();
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during kryoWriteClass()", ex);
		} finally {
			IoUtil.close(baos);
			IoUtil.close(out);
		}
		return result;
	}

	public static <T> T dekryoReadClass(byte[] value) {
		return dekryoReadClass(new Kryo(), value);
	}

	/**
	 * kryo 反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T dekryoReadClass(Kryo kryo, byte[] value) {
		T result = null;
		//
		AssertUtil.notNull(kryo, THE_KRYO_MUST_NOT_BE_NULL);
		AssertUtil.notNull(value, THE_VALUE_MUST_NOT_BE_NULL);
		//
		Input in = null;
		try {
			in = new Input(value);
			result = (T) kryo.readClassAndObject(in);
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during dekryoReadClass()", ex);
		} finally {
			IoUtil.close(in);
		}
		return result;
	}

}
