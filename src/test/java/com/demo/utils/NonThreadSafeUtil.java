package com.demo.utils;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

public class NonThreadSafeUtil {

	// issue 1: non thread safe
	private static StringBuilder stringBuilder = new StringBuilder();

	// issue 2: thread safe, but fail
	private static StringBuffer stringBuffer = new StringBuffer();

	// fix 2:
	private static transient Lock lock = new ReentrantLock();

	// fix 3:
	private static ThreadLocal<StringBuilder> holder = new ThreadLocal<StringBuilder>();

	// fix 4:
	// http://commons.apache.org/proper/commons-pool/examples.html
	private static GenericObjectPool<StringBuilder> stringBufferPool;

	// issue 1:
	private static ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

	// fix 2:
	private static GenericObjectPool<ByteArrayOutputStream> byteArrayOutputStreamPool;

	static {
		stringBufferPool = new GenericObjectPool<StringBuilder>(
				new PoolableObjectFactory<StringBuilder>() {

					public StringBuilder makeObject() throws Exception {
						return new StringBuilder();
					}

					public void destroyObject(StringBuilder obj)
							throws Exception {
					}

					public boolean validateObject(StringBuilder obj) {
						return true;
					}

					public void activateObject(StringBuilder obj)
							throws Exception {
					}

					public void passivateObject(StringBuilder obj)
							throws Exception {
						obj.setLength(0);
					}
				});
		// stringBufferPool.setMaxActive(100);
		//

		byteArrayOutputStreamPool = new GenericObjectPool<ByteArrayOutputStream>(
				new PoolableObjectFactory<ByteArrayOutputStream>() {

					public ByteArrayOutputStream makeObject() throws Exception {
						return new ByteArrayOutputStream();
					}

					public void destroyObject(ByteArrayOutputStream obj)
							throws Exception {
						obj.close();
					}

					public boolean validateObject(ByteArrayOutputStream obj) {
						return true;
					}

					public void activateObject(ByteArrayOutputStream obj)
							throws Exception {
					}

					public void passivateObject(ByteArrayOutputStream obj)
							throws Exception {
						obj.flush();
						obj.reset();
					}
				});
		// byteArrayOutputStreamPool.setMaxActive(100);
	}

	// --------------------------------------------------------
	// string
	// --------------------------------------------------------
	public static String toStringV1(String value) {
		String result = null;
		try {
			result = stringBuilder.append(value).toString();
			stringBuilder.delete(0, value.length());
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static String toStringV2(String value) {
		String result = null;
		try {
			result = stringBuffer.append(value).toString();
			stringBuffer.delete(0, value.length());
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static String toStringV1Fix1(String value) {
		String result = null;
		try {
			// fix 1: new object
			StringBuilder buff = new StringBuilder();
			//
			result = buff.append(value).toString();
			buff.delete(0, value.length());

		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static String toStringV1Fix2(String value) {
		String result = null;
		try {
			// fix 2: lock
			lock.lockInterruptibly();
			try {
				result = stringBuilder.append(value).toString();
				stringBuilder.delete(0, value.length());
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				lock.unlock();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static String toStringV1Fix3(String value) {
		String result = null;
		try {
			// issue 3: memory leak
			StringBuilder buff = holder.get();
			if (buff == null) {
				buff = new StringBuilder();
				holder.set(buff);
			}
			//
			result = buff.append(value).toString();
			buff.delete(0, value.length());
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static String toStringV1Fix4(String value) {
		String result = null;
		//
		StringBuilder buff = null;
		try {
			// fix 4: pool
			buff = stringBufferPool.borrowObject();
			//
			result = buff.append(value).toString();
			// buff.delete(0, value.length());
		} catch (Exception ex) {
			// ex.printStackTrace();
		} finally {
			try {
				if (buff != null) {
					stringBufferPool.returnObject(buff);
				}
			} catch (Exception ex) {
			}
		}
		return result;
	}

	// --------------------------------------------------------
	// outputstream
	// --------------------------------------------------------
	public static byte[] toBytes(int value) {
		byte[] result = null;
		try {
			byteArrayOutputStream.write(value);
			result = byteArrayOutputStream.toByteArray();
			//
			byteArrayOutputStream.reset();
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	public static byte[] toBytesFix1(int value) {
		byte[] result = null;
		//
		ByteArrayOutputStream buff = null;
		try {
			// fix 1: new object
			buff = new ByteArrayOutputStream();
			//
			buff.write(value);
			result = buff.toByteArray();
			//
			buff.reset();
		} catch (Exception ex) {
			// ex.printStackTrace();
		} finally {
			try {
				if (buff != null) {
					buff.close();
				}
			} catch (Exception ex) {
			}
		}
		return result;
	}

	public static byte[] toBytesFix2(int value) {
		byte[] result = null;
		//
		ByteArrayOutputStream buff = null;
		try {
			// fix 2: pool
			buff = byteArrayOutputStreamPool.borrowObject();
			//
			buff.write(value);
			result = buff.toByteArray();
			// buff.reset();
		} catch (Exception ex) {
			// ex.printStackTrace();
		} finally {
			try {
				if (buff != null) {
					byteArrayOutputStreamPool.returnObject(buff);
				}
			} catch (Exception ex) {
			}
		}
		return result;
	}
}
