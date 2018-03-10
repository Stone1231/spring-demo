package com.demo.utils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileTypeUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileTypeUtil.class);
	/**
	 * offset = 0
	 */
	private static final List<FileType> offsetZeroFileTypes = new ArrayList<>();

	private static int maxOffsetZeroLength = 0;

	/**
	 * offset > 0
	 */
	private static final List<FileType> offsetGTZeroFileTypes = new ArrayList<>();

	private static int maxOffsetGTZeroLength = 0;

	/**
	 * offset < 0, 表示在檔尾
	 */
	private static final List<FileType> offsetLTZeroFileTypes = new ArrayList<>();

	/**
	 * 多個FileType應對到ExtensionType
	 */
	private static Map<FileType, ExtensionType> extensionTypeMapping = new HashMap<>();

	static {
		new Static();
	}

	protected static class Static {
		public Static() {
			initFileType();
			initExtensionType();
		}
	}

	/**
	 * 初始各種offset的FileType
	 */
	protected static void initFileType() {
		FileType[] fileTypes = FileType.values();
		for (FileType fileType : fileTypes) {
			int offset = fileType.getOffset();
			int length = fileType.getLength();
			// offset = 0
			if (offset == 0) {
				offsetZeroFileTypes.add(fileType);
				maxOffsetZeroLength = Math.max(maxOffsetZeroLength, length);
				// offset > 0
			} else if (offset > 0) {
				offsetGTZeroFileTypes.add(fileType);
				maxOffsetGTZeroLength = Math.max(maxOffsetGTZeroLength, (offset + length));
			}
			// offset < 0
			else {
				offsetLTZeroFileTypes.add(fileType);
			}
		}
	}

	/**
	 * 初始 多個FileType應對到ExtensionType的mapping
	 */
	protected static void initExtensionType() {
		// ---------------------------------------------------
		// Image
		// ---------------------------------------------------
		extensionTypeMapping.put(FileType.BMP, ExtensionType.BMP);
		//
		extensionTypeMapping.put(FileType.GIF87a, ExtensionType.GIF);
		extensionTypeMapping.put(FileType.GIF89a, ExtensionType.GIF);
		//
		extensionTypeMapping.put(FileType.JP2, ExtensionType.JP2);
		//
		extensionTypeMapping.put(FileType.JPG_D807, ExtensionType.JPG);
		extensionTypeMapping.put(FileType.JPG_JFIF, ExtensionType.JPG);
		extensionTypeMapping.put(FileType.JPG_EXIF, ExtensionType.JPG);
		extensionTypeMapping.put(FileType.JPG_EOS_1D, ExtensionType.JPG);
		extensionTypeMapping.put(FileType.JPG_D500, ExtensionType.JPG);
		extensionTypeMapping.put(FileType.JPG_SPIFF, ExtensionType.JPG);
		//
		extensionTypeMapping.put(FileType.PCX_FILE_1, ExtensionType.PCX);
		extensionTypeMapping.put(FileType.PCX_FILE_2, ExtensionType.PCX);
		extensionTypeMapping.put(FileType.PCX_FILE_3, ExtensionType.PCX);
		//
		extensionTypeMapping.put(FileType.PNG, ExtensionType.PNG);
		//
		extensionTypeMapping.put(FileType.PBM_ASCII, ExtensionType.PBM);
		extensionTypeMapping.put(FileType.PGM_ASCII, ExtensionType.PGM);
		extensionTypeMapping.put(FileType.PPM_ASCII, ExtensionType.PPM);
		//
		extensionTypeMapping.put(FileType.PBM_BINARY, ExtensionType.PBM);
		extensionTypeMapping.put(FileType.PGM_BINARY, ExtensionType.PGM);
		extensionTypeMapping.put(FileType.PPM_BINARY, ExtensionType.PPM);
		//
		extensionTypeMapping.put(FileType.TGA, ExtensionType.TGA);
		//
		extensionTypeMapping.put(FileType.TIF_FILE_1, ExtensionType.TIF);
		extensionTypeMapping.put(FileType.TIF_FILE_2, ExtensionType.TIF);
		extensionTypeMapping.put(FileType.TIF_FILE_3, ExtensionType.TIF);
		extensionTypeMapping.put(FileType.TIF_FILE_4, ExtensionType.TIF);

		// ---------------------------------------------------
		// Office
		// ---------------------------------------------------
		extensionTypeMapping.put(FileType.DOC_WORD_DOCUMENT_SUBHEADER, ExtensionType.DOC);

		extensionTypeMapping.put(FileType.XLS_EXCEL_SPREADSHEET_SUBHEADER_1, ExtensionType.XLS);
		extensionTypeMapping.put(FileType.XLS_EXCEL_SPREADSHEET_SUBHEADER_2, ExtensionType.XLS);
		extensionTypeMapping.put(FileType.XLS_EXCEL_SPREADSHEET_SUBHEADER_3, ExtensionType.XLS);
		extensionTypeMapping.put(FileType.XLS_EXCEL_SPREADSHEET_SUBHEADER_4, ExtensionType.XLS);
		extensionTypeMapping.put(FileType.XLS_EXCEL_SPREADSHEET_SUBHEADER_5, ExtensionType.XLS);
		extensionTypeMapping.put(FileType.XLS_EXCEL_SPREADSHEET_SUBHEADER_6, ExtensionType.XLS);
		extensionTypeMapping.put(FileType.XLS_EXCEL_SPREADSHEET_SUBHEADER_7, ExtensionType.XLS);

		extensionTypeMapping.put(FileType.PPT_POWERPOINT_PRESENTATION_SUBHEADER_1, ExtensionType.PPT);
		extensionTypeMapping.put(FileType.PPT_POWERPOINT_PRESENTATION_SUBHEADER_2, ExtensionType.PPT);
		extensionTypeMapping.put(FileType.PPT_POWERPOINT_PRESENTATION_SUBHEADER_3, ExtensionType.PPT);
		extensionTypeMapping.put(FileType.PPT_POWERPOINT_PRESENTATION_SUBHEADER_4, ExtensionType.PPT);
		extensionTypeMapping.put(FileType.PPT_POWERPOINT_PRESENTATION_SUBHEADER_5, ExtensionType.PPT);
		extensionTypeMapping.put(FileType.PPT_POWERPOINT_PRESENTATION_SUBHEADER_6, ExtensionType.PPT);

		extensionTypeMapping.put(FileType.VSD_VISIO_SUBHEADER, ExtensionType.VSD);

		// ---------------------------------------------------
		// File
		// ---------------------------------------------------
		extensionTypeMapping.put(FileType.PDF, ExtensionType.PDF);
		extensionTypeMapping.put(FileType.EPS_FILE_1, ExtensionType.EPS);
		extensionTypeMapping.put(FileType.EPS_FILE_2, ExtensionType.EPS);
	}

	private FileTypeUtil() {
	}

	/**
	 * 取得檔案類型
	 * 
	 * @param fileName
	 * @return
	 */
	public static FileType getFileType(String fileName) {
		AssertUtil.notNull(fileName, "The FileName must not be null");
		//
		return getFileType(new File(fileName));
	}

	/**
	 * 取得檔案類型
	 * 
	 * @param file
	 * @return
	 */
	public static FileType getFileType(File file) {
		FileType result = null;
		boolean exist = FileUtil.isExist(file);
		//
		AssertUtil.isTrue(exist, "The File is not exist [" + file.getPath() + "]");
		//
		InputStream in = null;
		try {
			in = IoUtil.createInputStream(file);
			result = getFileType(in);
		} finally {
			IoUtil.close(in);
		}
		return result;
	}

	/**
	 * 取得檔案類型
	 * 
	 * @param in
	 *            表示尚未讀出byte[]的stream
	 * @return
	 */
	public static FileType getFileType(InputStream in) {
		FileType result = null;
		try {
			// 1.從檔頭取byte[], length=maxOffsetZeroLength
			in.mark(0);
			// 暫存的fileType
			FileType buffType = searchOffsetZero(in);
			// 有檔頭
			if (buffType != null) {
				switch (buffType) {
				// OLECF 有多種檔案格式,故還須由offset再判斷
				case OLECF:
					if (in.markSupported()) {
						in.reset();
						buffType = searchOffsetGTZero(in);
						if (buffType != null) {
							result = buffType;
						} else {
							// 若連offset都沒,則有可能是其它office文件
							// result = FileType
						}

					} else {
						LOGGER.warn(in.getClass().getName() + " mark/reset not supported");
					}
					break;
				// 非OLECF, 就取其FileType
				default:
					result = buffType;
					break;
				}
				// 無檔頭
			} else {
				if (in.markSupported()) {
					in.reset();
					in.mark(0);
					// 2.當檔頭無法判斷時,有可能有offset
					buffType = searchOffsetGTZero(in);
					if (buffType != null) {
						result = buffType;
					} else {
						in.reset();
						in.mark(0);
						// 3.當檔頭及offset無法判斷時,有可能會是在檔尾
						buffType = searchOffsetLTZero(in);
						if (buffType != null) {
							result = buffType;
						}
					}
				} else {
					// new BufferedInputStream(new FileInputStream(fileName))
					LOGGER.warn(in.getClass().getName() + " mark/reset not supported");
				}
			}
		} catch (Exception ex) {
			//
		} finally {
			IoUtil.reset(in);
		}
		return result;
	}

	/**
	 * 取得檔案類型
	 * 
	 * @param value
	 *            表示已經讀取出來的byte[]
	 * @return
	 */
	public static FileType getFileType(byte[] value) {
		FileType result = null;
		try {
			// 1.從檔頭取byte[], length=maxOffsetZeroLength
			// 暫存的fileType
			FileType buffType = searchOffsetZero(value);
			// 有檔頭
			if (buffType != null) {
				switch (buffType) {
				// OLECF 有多種檔案格式,故還須由offset再判斷
				case OLECF: {
					buffType = searchOffsetGTZero(value);
					if (buffType != null) {
						result = buffType;
					} else {
						// 若連offset都沒,則有可能是其它office文件
						// result = FileType
					}
					break;
				}
				// 非OLECF, 就取其FileType
				default: {
					result = buffType;
					break;
				}
				}
				// 無檔頭
			} else {
				// 2.當檔頭無法判斷時,有可能有offset
				buffType = searchOffsetGTZero(value);
				if (buffType != null) {
					result = buffType;
				} else {
					// 3.當檔頭及offset無法判斷時,有可能會是在檔尾
					buffType = searchOffsetLTZero(value);
					if (buffType != null) {
						result = buffType;
					}
				}
			}
		} catch (Exception ex) {
			//
		}
		return result;
	}

	/**
	 * offset = 0
	 * 
	 * @param in
	 * @return
	 */
	protected static FileType searchOffsetZero(InputStream in) {
		FileType result = null;
		// 讀取出來的byte[]
		byte[] read = null;
		int size = offsetZeroFileTypes.size();
		if (size > 0) {
			// 1.從檔頭取byte[], length=maxOffsetZeroLength
			read = IoUtil.read(in, maxOffsetZeroLength, new InputStreamCallback() {
				public boolean doInAction(byte[] blockArray) {
					// true=繼續往下一段byte[], false=中斷
					return false;
				}
			});
			result = searchOffsetZero(read);
		}
		return result;
	}

	/**
	 * offset = 0
	 * 
	 * @param value
	 * @return
	 */
	protected static FileType searchOffsetZero(byte[] value) {
		FileType result = null;
		// 是否找到
		boolean found = false;
		// 判斷檔案類型 offset = 0
		for (FileType fileType : offsetZeroFileTypes) {
			// 從檔頭開始, 讀取byte[]
			byte[] buff = ByteUtil.getBytes(value, 0, fileType.getLength());
			found = ObjectUtil.equals(buff, fileType.getBytes());
			if (found) {
				result = fileType;
				break;
			}
		}
		return result;
	}

	/**
	 * offset > 0
	 * 
	 * @param in
	 * @return
	 */
	protected static FileType searchOffsetGTZero(InputStream in) {
		FileType result = null;
		// 讀取出來的byte[]
		byte[] read = null;
		int size = offsetGTZeroFileTypes.size();
		if (size > 0) {
			// 2.當檔頭無法判斷時,有可能有offset, length=maxOffsetGreaterThanZeroLength
			read = IoUtil.read(in, maxOffsetGTZeroLength, new InputStreamCallback() {
				public boolean doInAction(byte[] blockArray) {
					// true=繼續往下一段byte[], false=中斷
					return false;
				}
			});
			result = searchOffsetGTZero(read);
		}
		return result;
	}

	/**
	 * offset = 0
	 * 
	 * @param value
	 * @return
	 */
	protected static FileType searchOffsetGTZero(byte[] value) {
		FileType result = null;
		// 是否找到
		boolean found = false;
		// 判斷檔案類型 offset > 0
		for (FileType fileType : offsetGTZeroFileTypes) {
			// 從offset開始, 讀取byte[]
			byte[] buff = ByteUtil.getBytes(value, fileType.getOffset(), fileType.getLength());
			found = ObjectUtil.equals(buff, fileType.getBytes());
			if (found) {
				result = fileType;
				break;
			}
		}
		return result;
	}

	/**
	 * offset < 0
	 * 
	 * @param in
	 * @return
	 */
	protected static FileType searchOffsetLTZero(InputStream in) {
		FileType result = null;
		// 讀取出來的byte[]
		byte[] read = null;
		int size = offsetLTZeroFileTypes.size();
		if (size > 0) {
			// 3.當檔頭及offset無法判斷時,有可能會是在檔尾
			// offset < 0, 表示讀取到檔尾, 一次讀10k,全部讀完
			read = IoUtil.read(in, 16 * 64 * 10, new InputStreamCallback() {
				public boolean doInAction(byte[] blockArray) {
					// true=繼續往下一段byte[], false=中斷
					return true;
				}
			});
			result = searchOffsetLTZero(read);
		}
		return result;
	}

	/**
	 * offset < 0
	 * 
	 * @param value
	 * @return
	 */
	protected static FileType searchOffsetLTZero(byte[] value) {
		FileType result = null;
		// 是否找到
		boolean found = false;
		// 判斷檔案類型 offset <0
		for (FileType fileType : offsetLTZeroFileTypes) {
			// 取檔尾的byte[]
			byte[] buff = ByteUtil.getBytes(value, (value.length - fileType.getLength()), fileType.getLength());
			// tga, 54525545564953494F4E2D5846494C452E00
			found = ObjectUtil.equals(buff, fileType.getBytes());
			if (found) {
				result = fileType;
				break;
			}
		}
		return result;
	}

	/**
	 * 取得副檔名類型
	 * 
	 * @param fileName
	 * @return
	 */
	public static ExtensionType getExtensionType(String fileName) {
		AssertUtil.notNull(fileName, "The FileName must not be null");
		//
		return getExtensionType(new File(fileName));
	}

	/**
	 * 取得副檔名類型
	 * 
	 * @param file
	 * @return
	 */
	public static ExtensionType getExtensionType(File file) {
		ExtensionType result = null;
		boolean exist = FileUtil.isExist(file);
		//
		AssertUtil.isTrue(exist, "The File is not exist [" + file.getPath() + "]");
		//
		InputStream in = null;
		try {
			in = IoUtil.createInputStream(file);
			result = getExtensionType(in);
		} catch (Exception ex) {
			//
		} finally {
			IoUtil.close(in);
		}
		return result;
	}

	/**
	 * 取得副檔名類型
	 * 
	 * @param in
	 * @return
	 */
	public static ExtensionType getExtensionType(InputStream in) {
		FileType fileType = getFileType(in);
		if (fileType != null) {
			return extensionTypeMapping.get(fileType);
		}
		return null;
	}

	/**
	 * 取得副檔名類型
	 * 
	 * @param value
	 * @return
	 */
	public static ExtensionType getExtensionType(byte[] value) {
		FileType fileType = getFileType(value);
		if (fileType != null) {
			return extensionTypeMapping.get(fileType);
		}
		return null;
	}
}
