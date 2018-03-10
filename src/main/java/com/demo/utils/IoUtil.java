package com.demo.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * java.util.zip.ZipOutputStream;
 * #issue: 不支援中文檔名
 * 
 * #fix
 * import org.apache.tools.zip.ZipOutputStream;
 */
//import org.apache.tools.zip.ZipOutputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 將save/load -> 改成read/write
 *
 */
public class IoUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(IoUtil.class);

	/**
	 * 預設讀取重試次數
	 */
	public static final int DEFAULT_READ_RETRY_NUMBER = 15;

	/**
	 * 預設讀取重試暫停毫秒
	 */
	public static final long DEFAULT_READ_RETRY_PAUSE_MILLS = 100L;

	/**
	 * 讀取重試補償
	 */
	protected static final int[] READ_RETRY_BACK_OFF = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2 };

	public static final int DEFALUT_BLOCK_LENGTH = 1024;// bytes

	private IoUtil() {
	}

	/**
	 * 建構輸入串流
	 *
	 * BufferedInputStream
	 *
	 * @param fileName
	 * @return
	 */
	public static InputStream createInputStream(String fileName) {
		InputStream result = null;
		if (StringUtil.isNotBlank(fileName)) {
			result = createInputStream(new File(fileName));
		}
		return result;
	}

	/**
	 * 建構輸入串流
	 *
	 * BufferedInputStream
	 *
	 * @param file
	 * @return
	 */
	public static InputStream createInputStream(File file) {
		InputStream result = null;
		//
		try {
			if (FileUtil.isExist(file)) {
				result = new BufferedInputStream(new FileInputStream(file));
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during createInputStream()", ex);
		}

		return result;
	}

	// BufferedOutputStream
	public static OutputStream createOutputStream(String value) {
		OutputStream result = null;
		if (StringUtil.isNotBlank(value)) {
			result = createOutputStream(new File(value));
		}
		return result;
	}

	// BufferedOutputStream
	public static OutputStream createOutputStream(File value) {
		OutputStream result = null;
		if (value != null) {
			// 建目錄
			FileUtil.md(value.getPath(), true);
			try {
				result = new BufferedOutputStream(new FileOutputStream(value));
			} catch (Exception ex) {
				LOGGER.error("Exception encountered during createOutputStream()", ex);
			}
		}
		return result;
	}

	public static ZipOutputStream createZipOutputStream(String value) {
		ZipOutputStream result = null;
		if (StringUtil.isNotBlank(value)) {
			result = createZipOutputStream(new File(value));
		}
		return result;
	}

	public static ZipOutputStream createZipOutputStream(File value) {
		ZipOutputStream result = null;
		if (value != null) {
			try {
				OutputStream out = createOutputStream(value);
				result = new ZipOutputStream(out);
			} catch (Exception ex) {
				LOGGER.error("Exception encountered during createZipOutputStream()", ex);
			}
		}
		return result;
	}

	// BufferedReader
	public static Reader createReader(String value) {
		Reader result = null;
		if (StringUtil.isNotBlank(value)) {
			result = createReader(new File(value));
		}
		return result;
	}

	// BufferedReader
	public static Reader createReader(File value) {
		Reader result = null;
		if (FileUtil.isExist(value)) {
			try {
				result = new BufferedReader(new FileReader(value));
			} catch (Exception ex) {
				LOGGER.error("Exception encountered during createReader()", ex);
			}
		}
		return result;
	}

	// BufferedWriter
	public static Writer createWriter(String value) {
		Writer result = null;
		if (StringUtil.isNotBlank(value)) {
			result = createWriter(new File(value));
		}
		return result;
	}

	// BufferedWriter
	public static Writer createWriter(File value) {
		Writer result = null;
		if (value != null) {
			FileUtil.md(value.getPath(), true);
			try {
				result = new BufferedWriter(new FileWriter(value, false));
			} catch (Exception ex) {
				LOGGER.error("Exception encountered during createWriter()", ex);
			}
		}
		return result;
	}

	/**
	 *
	 * @param fileName
	 * @return
	 */
	public static PrintWriter createPrintWriter(String value) {
		PrintWriter result = null;
		if (StringUtil.isNotBlank(value)) {
			result = createPrintWriter(new File(value));
		}
		return result;
	}

	public static PrintWriter createPrintWriter(File value) {
		PrintWriter result = null;
		if (value != null) {
			// 建目錄
			FileUtil.md(value.getPath(), true);
			try {
				result = new PrintWriter(new BufferedOutputStream(new FileOutputStream(value)));
			} catch (Exception ex) {
				LOGGER.error("Exception encountered during createPrintWriter()", ex);
			}
		}
		return result;
	}

	// --------------------------------------------------
	// 20110815
	// --------------------------------------------------

	/**
	 * 關閉reader
	 *
	 * @param reader
	 */
	public static void close(Reader reader) {

		try {
			if (reader != null) {
				reader.close();
			}
		} catch (Exception ex) {
			//
		}
	}

	/**
	 * 關閉writer
	 *
	 * @param writer
	 */
	public static void close(Writer writer) {
		try {
			if (writer != null) {
				writer.close();
			}
		} catch (Exception ex) {
			//
		}
	}

	/**
	 * 關閉in串流
	 *
	 * @param in
	 */
	public static void close(InputStream in) {
		try {
			if (in != null) {
				in.close();
			}
		} catch (Exception ex) {
			//
		}
	}

	/**
	 * 關閉out串流
	 *
	 * @param out
	 */
	public static void close(OutputStream out) {

		try {
			if (out != null) {
				out.close();
			}
		} catch (Exception ex) {
			//
		}
	}

	/**
	 * 關閉in
	 * 
	 * @param in
	 */
	public static void close(ObjectInput in) {

		try {
			if (in != null) {
				in.close();
			}
		} catch (Exception ex) {
			//
		}
	}

	/**
	 * 關閉out
	 * 
	 * @param out
	 */
	public static void close(ObjectOutput out) {

		try {
			if (out != null) {
				out.close();
			}
		} catch (Exception ex) {
			//
		}
	}

	/**
	 * 關閉
	 *
	 * @param closeable
	 */
	public static void close(Closeable closeable) {

		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (Exception ex) {
			//
		}
	}

	// --------------------------------------------------
	// 20110920
	/**
	 *
	 * 將outputStream轉成inputStream
	 *
	 * @param outputStream
	 * @return
	 */
	public static InputStream toInputStream(OutputStream value) {
		ByteArrayInputStream result = null;
		if (value instanceof ByteArrayOutputStream) {
			try {
				result = new ByteArrayInputStream(((ByteArrayOutputStream) value).toByteArray());
			} catch (Exception ex) {
				LOGGER.error("Exception encountered during toInputStream()", ex);
			}
		}
		return result;
	}

	public static int available(InputStream in) {
		return available(in, DEFAULT_READ_RETRY_NUMBER, DEFAULT_READ_RETRY_PAUSE_MILLS);
	}

	/**
	 * 
	 * @param inputStream
	 * @param retryNumber
	 *            重試次數, 0=無限
	 * @param retryPauseMills
	 * @return
	 */
	public static int available(InputStream inputStream, int retryNumber, long retryPauseMills) {
		int result = 0;
		//
		AssertUtil.notNull(inputStream, "The InputStream must not be null");
		//
		try {
			// #issue:
			// 1.把資料讀完,再取available第2次會卡死
			// 2.AutoCloseInputStream會close, available=0,會卡死
			/**
			 * while (result == 0) {
			 * 
			 * result = inputStream.available();
			 * 
			 * }
			 */
			// #fix 加個重試次數判斷
			int tries = 0;
			while (true) {
				result = inputStream.available();
				if (result != 0) {
					break;
				}
				tries++;
				// 預設100*10+200*5=2000共等待2秒
				if (retryNumber != 0 && tries >= retryNumber) {
					break;
				}
				// 讀取重試暫停毫秒
				long pauseMills = readRetryPause(tries, retryPauseMills);
				ThreadUtil.sleep(pauseMills);
			}

		} catch (Exception ex) {
			LOGGER.error("Exception encountered during available()", ex);
		}
		//
		return result;
	}

	public static byte[] read(String fileName) {
		AssertUtil.notNull(fileName, "The FileName must not be null");
		//
		return read(new File(fileName));
	}

	public static byte[] read(File file) {
		byte[] result = null;
		boolean exist = FileUtil.isExist(file);
		//
		AssertUtil.isTrue(exist, "The File is not exist");
		//
		InputStream in = null;
		try {
			in = createInputStream(file);
			result = read(in);
		} finally {
			close(in);
		}
		//
		return result;
	}

	public static byte[] read(InputStream inputStream) {
		return read(inputStream, DEFALUT_BLOCK_LENGTH, DEFAULT_READ_RETRY_NUMBER, DEFAULT_READ_RETRY_PAUSE_MILLS, null);
	}

	/**
	 * 
	 * @param inputStream
	 * @param blockLength
	 *            每次讀取的長度
	 * @return
	 */
	public static byte[] read(InputStream inputStream, int blockLength) {
		return read(inputStream, blockLength, DEFAULT_READ_RETRY_NUMBER, DEFAULT_READ_RETRY_PAUSE_MILLS, null);
	}

	/**
	 * 
	 * @param inputStream
	 * @param blockLength
	 *            每次讀取的長度
	 * @param action
	 *            外接處理邏輯
	 * @return
	 */
	public static byte[] read(InputStream inputStream, int blockLength, InputStreamCallback action) {

		return read(inputStream, blockLength, DEFAULT_READ_RETRY_NUMBER, DEFAULT_READ_RETRY_PAUSE_MILLS, action);
	}

	/**
	 * 不要使用read(), 效率會非常低
	 * 
	 * 應該用以下兩種之一
	 * 
	 * 1.read(byte b[], int off, int len)
	 * 
	 * 2.read(byte b[])
	 * 
	 * @param inputStream
	 * @param blockLength
	 *            每次讀取的長度
	 * @param retryNumber
	 *            重試次數, 0=無限
	 * @param retryPauseMills
	 * @param action
	 *            外接處理邏輯
	 * @return
	 */
	public static byte[] read(InputStream inputStream, int blockLength, int retryNumber, long retryPauseMills,
			InputStreamCallback action) {
		byte[] result = null;
		//
		AssertUtil.notNull(inputStream, "The InputStream must not be null");
		//
		ByteArrayOutputStream out = null;
		// defalut
		int buffLength = DEFALUT_BLOCK_LENGTH;
		try {
			if (blockLength > 0) {
				buffLength = blockLength;
			}
			//
			out = new ByteArrayOutputStream();
			byte[] buff = new byte[buffLength];
			//
			int read = -1;
			// 重試次數判斷
			int tries = 0;
			//
			while ((read = inputStream.read(buff)) != -1) {
				// 當讀不到資料時
				if (read == 0) {
					tries++;
					// 預設100*10+200*5=2000共等待2秒
					if (retryNumber != 0 && tries >= retryNumber) {
						break;
					}
					// 讀取重試暫停毫秒
					long pauseMills = readRetryPause(tries, retryPauseMills);
					ThreadUtil.sleep(pauseMills);
				} else {
					// read > 0
					out.write(buff, 0, read);

					// action
					boolean next = doInAction(action, buff);
					if (!next) {
						break;
					}
				}
			}
			//
			if (out.size() > 0) {
				result = out.toByteArray();
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during read()", ex);
		} finally {
			close(out);
		}
		//
		return result;
	}

	protected static boolean doInAction(InputStreamCallback action, byte[] blockArray) {
		if (action != null) {
			try {
				return action.doInAction(blockArray);
			} catch (Exception ex) {
				LOGGER.error("Exception encountered during doInAction()", ex);
			}
		}
		return true;
	}

	/**
	 * 寫入writer
	 *
	 * @param writer
	 * @param value
	 * @return
	 */
	public static boolean write(Writer writer, String value) {
		boolean result = false;
		try {
			if (writer != null && value != null) {
				writer.write(value);
				result = true;
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during write()", ex);
		}
		//
		return result;
	}

	/**
	 * 寫入outputStream
	 *
	 * @param out
	 * @param buffs
	 */
	public static boolean write(OutputStream out, byte[] buffs) {
		boolean result = false;
		try {
			if (out != null && buffs != null) {
				out.write(buffs);
				result = true;
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during write()", ex);
		}
		//
		return result;
	}

	/**
	 * 把字串寫出成檔案
	 *
	 * @param fileName
	 * @param value
	 * @return
	 */
	public static boolean writeToString(String fileName, String value) {
		return writeToString(fileName, value, EncodingUtil.UTF_8);
	}

	/**
	 * 把字串寫出成檔案
	 *
	 * @param fileName
	 * @param value
	 * @param encoding
	 * @return
	 */
	public static boolean writeToString(String fileName, String value, String encoding) {
		if (fileName == null) {
			return false;
		}
		return writeToString(new File(fileName), value, encoding);
	}

	/**
	 * 把字串寫出成檔案
	 *
	 * @param file
	 * @param content
	 * @return
	 */
	public static boolean writeToString(File file, String content) {
		return writeToString(file, content, EncodingUtil.UTF_8);
	}

	/**
	 * 把字串寫出成檔案
	 *
	 * @param file
	 * @param content
	 * @param encoding
	 * @return
	 */
	public static boolean writeToString(File file, String content, String encoding) {
		boolean result = false;
		//
		try {
			FileUtils.writeStringToFile(file, content, encoding);
			result = true;
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during writeToString()", ex);
		}
		return result;
	}

	/**
	 * 計算讀取重試暫停毫秒
	 *
	 * @param retryPauseMills
	 * @param tries
	 * @return
	 */
	public static long readRetryPause(int tries, long retryPauseMills) {
		int triesCount = tries;
		if (triesCount >= READ_RETRY_BACK_OFF.length) {
			triesCount = READ_RETRY_BACK_OFF.length - 1;
		}
		return (retryPauseMills * READ_RETRY_BACK_OFF[triesCount]);
	}

	public static void reset(InputStream in) {
		try {
			if (in != null && in.markSupported()) {
				in.reset();
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during reset()", ex);
		}
	}
}
