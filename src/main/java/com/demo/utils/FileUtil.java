package com.demo.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(IoUtil.class);

	private FileUtil() {
	}

	/**
	 * 目錄或檔案是否"存在",判斷null跟file.exists()
	 *
	 * @param fileName
	 * @return
	 */
	public static boolean isExist(String fileName) {
		if (fileName == null) {
			return false;
		}
		return isExist(new File(fileName));
	}

	/**
	 * 目錄或檔案是否"存在",判斷null跟file.exists()
	 *
	 * @param file
	 * @return
	 */
	public static boolean isExist(File file) {
		boolean result = false;
		//
		try {
			if (file != null) {
				result = file.exists();
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during isExist()", ex);
		}
		return result;
	}

	/**
	 * 目錄或檔案是否"不存在"
	 *
	 * @param fileName
	 * @return
	 */
	public static boolean isNotExist(String fileName) {
		if (fileName == null) {
			return false;
		}
		return isNotExist(new File(fileName));
	}

	/**
	 * 目錄或檔案是否"不存在"
	 *
	 * @param file
	 * @return
	 */
	public static boolean isNotExist(File file) {
		boolean result = false;
		try {
			if (file == null || !file.exists()) {
				result = true;
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during isNotExist()", ex);
		}
		return result;
	}

	/**
	 * 轉成url
	 *
	 * @param fileName
	 * @return
	 */
	public static URL toUrl(String fileName) {
		if (fileName == null) {
			return null;
		}
		return toUrl(new File(fileName));
	}

	/**
	 * 轉成url
	 *
	 * @param file
	 * @return
	 */
	public static URL toUrl(File file) {
		URL result = null;
		try {
			if (isExist(file)) {
				// result = file.toURL();
				result = file.toURI().toURL();
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during toUrl()", ex);
		}
		return result;
	}

	/**
	 * 轉成url
	 *
	 * @param files
	 * @return
	 */
	public static URL[] toUrls(File[] files) {
		URL[] result = new URL[0];
		//
		if (files != null) {
			result = new URL[files.length];
			for (int i = 0; i < files.length; i++) {
				result[i] = toUrl(files[i]);
			}
		}
		return result;
	}

	/**
	 * 轉成檔案
	 *
	 * @param spec
	 * @return
	 */
	public static File toFile(String spec) {
		File result = null;
		try {
			if (spec != null) {
				URL url = new URL(spec);
				result = toFile(url);
			}

		} catch (Exception ex) {
			LOGGER.error("Exception encountered during toFile()", ex);
		}
		return result;
	}

	/**
	 * 轉成檔案
	 *
	 * 可以轉成file,但file可能不存在
	 *
	 * @param url
	 * @return
	 */
	public static File toFile(URL url) {
		File result = null;
		//
		if (url != null && url.getProtocol().equals("file")) {
			result = new File(url.getFile().replace("/", File.separator));
		}
		return result;
	}

	/**
	 * 轉成檔案
	 *
	 * @param urls
	 * @return
	 */
	public static File[] toFiles(URL[] urls) {
		File[] result = new File[0];
		//
		if (urls != null) {
			result = new File[urls.length];
			for (int i = 0; i < urls.length; i++) {
				result[i] = toFile(urls[i]);
			}
		}
		return result;
	}

	/**
	 * 由class取得目錄
	 *
	 * @param clazz
	 * @return
	 */
	public static String getDirByClass(Class<?> clazz) {
		return getDirByClass((File) null, clazz);
	}

	/**
	 * 由class取得目錄
	 *
	 * @param parentDir
	 * @param clazz
	 * @return
	 */
	public static String getDirByClass(String parentDir, Class<?> clazz) {
		return getDirByPackage(new File(parentDir), clazz.getPackage());
	}

	/**
	 * 由class取得目錄
	 *
	 * @param parentDir
	 * @param clazz
	 * @return
	 */
	public static String getDirByClass(File parentDir, Class<?> clazz) {
		return getDirByPackage(parentDir, clazz.getPackage());
	}

	/**
	 * 由package取得目錄
	 *
	 * @param packagz
	 * @return
	 */
	public static String getDirByPackage(Package packagz) {
		return getDirByPackage((File) null, packagz);
	}

	/**
	 * 由package取得目錄
	 *
	 * @param parentDir
	 * @param packagz
	 * @return
	 */
	public static String getDirByPackage(String parentDir, Package packagz) {
		return getDirByPackage(new File(parentDir), packagz.getName());
	}

	/**
	 * 由package取得目錄
	 *
	 * @param parentDir
	 * @param packagz
	 * @return
	 */
	public static String getDirByPackage(File parentDir, Package packagz) {
		return getDirByPackage(parentDir, packagz.getName());
	}

	/**
	 * 由package取得目錄
	 *
	 * @param packageName
	 * @return
	 */
	public static String getDirByPackage(String packageName) {
		return getDirByPackage(null, packageName);
	}

	/**
	 * 由package取得目錄
	 *
	 * @param parentDir
	 * @param packageName
	 * @return
	 */
	public static String getDirByPackage(File parentDir, String packageName) {
		if (packageName == null) {
			return null;
		}
		//
		StringTokenizer st = new StringTokenizer(packageName, ".");
		StringBuilder dir = new StringBuilder();
		if (parentDir == null) {
			dir.append(System.getProperty("user.dir"));
		} else {
			// 絕對路徑
			dir.append(parentDir.getAbsolutePath());

		}
		while (st.hasMoreTokens()) {
			dir.append(File.separator);
			dir.append(st.nextToken());
		}
		return dir.toString();
	}

	/**
	 * 取得副檔名
	 *
	 * @param fileName
	 * @return
	 */
	public static String getExtension(String fileName) {
		if (fileName == null) {
			return null;
		}
		return getExtension(new File(fileName));
	}

	/**
	 * 取得副檔名
	 *
	 * @param file
	 * @return
	 */
	public static String getExtension(File file) {
		String result = null;
		if (file != null) {
			String fileName = file.getName();
			int i = fileName.lastIndexOf('.');
			if (i > 0 && i < fileName.length() - 1) {
				result = fileName.substring(i + 1);
			}
		}
		return result;
	}

	// -----------------------------------------------------
	// class 系統,資源
	// -----------------------------------------------------
	/**
	 * class 系統, 取得資源, /applicationContext.xml
	 *
	 * @param name
	 * @return
	 */
	public static URL getResource(String name) {
		if (name == null) {
			return null;
		}
		return FileUtil.class.getResource(name);
	}

	/**
	 * class 系統, 取得資源檔名, /applicationContext.xml
	 *
	 * @param name
	 * @return
	 */
	public static String getResourceFile(String name) {
		String result = null;
		//
		URL url = getResource(name);
		if (url != null) {
			result = url.getFile();
		}
		return result;
	}

	/**
	 * class 系統, 取得串流, /applicationContext.xml
	 *
	 * java.io.BufferedInputStream@1103d94
	 *
	 * @param name
	 * @return
	 */
	public static InputStream getResourceStream(String name) {
		if (name == null) {
			return null;
		}
		return FileUtil.class.getResourceAsStream(name);
	}

	/**
	 * class 系統, 取得資源轉為屬性內容, /applicationContext.xml
	 *
	 * @param name
	 * @return
	 */
	public static Properties getProperties(String name) {
		Properties result = null;
		InputStream in = null;
		try {
			in = getResourceStream(name);
			if (in != null) {
				result = new Properties();
				result.load(in);
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during getProperties()", ex);
		} finally {
			// 關閉串流
			IoUtil.close(in);
		}
		return result;
	}

	// -----------------------------------------------------
	/**
	 * 建目錄
	 *
	 * @param pathName
	 * @return
	 */
	public static boolean md(String pathName) {
		return md(pathName, false);
	}

	/**
	 * 建目錄,若已存在,則不建
	 *
	 * 可建立不存在的子目錄
	 *
	 * @param pathName
	 * @param includeFileName
	 *            目錄名稱是否包含檔名
	 * @return
	 */
	public static boolean md(String pathName, boolean includeFileName) {
		boolean result = false;
		//
		try {
			if (StringUtil.isNotEmpty(pathName)) {
				StringBuilder buff = new StringBuilder();
				// output/x/y/z/aaa.log
				if (includeFileName) {
					buff.append(StringUtil.excludeLast(pathName, "/"));
					// 判斷目錄分割字元為 / 或 \
					if (pathName.equals(buff.toString())) {
						buff = new StringBuilder();
						buff.append(StringUtil.excludeLast(pathName, "\\"));
					}
					if (buff.toString().equals(pathName)) {
						buff = new StringBuilder();
					}
				} else {
					buff.append(pathName);
				}
				//
				if (buff.length() > 0) {
					result = md(new File(buff.toString()));
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during md()", ex);

		}
		return result;
	}

	/**
	 * 建目錄,若已存在,則不建
	 *
	 * @param file
	 * @return
	 */
	public static boolean md(File file) {
		boolean result = false;
		//
		try {
			if (file != null && isNotExist(file)) {
				result = file.mkdirs();
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during md()", ex);
		}
		return result;
	}

	/**
	 * 讀取目錄檔案, 含子目錄/檔案
	 *
	 * @param pathName
	 * @return
	 */
	public static File[] dir(String pathName) {
		return dir(pathName, null);
	}

	/**
	 * 讀取目錄檔案, 含子目錄/檔案
	 *
	 * @param pathName
	 * @param filter
	 * @return
	 */
	public static File[] dir(String pathName, FileFilter filter) {
		File[] result = new File[0];
		//
		if (pathName == null) {
			return result;
		} else {
			result = dir(new File(pathName), filter);
		}
		return result;
	}

	/**
	 * 讀取目錄檔案, 含子目錄/檔案
	 *
	 * @param file
	 * @return
	 */
	public static File[] dir(File file) {
		return dir(file, null);
	}

	/**
	 * 讀取目錄檔案, 含子目錄/檔案
	 *
	 * @param file
	 * @param filter
	 * @return
	 */
	public static File[] dir(File file, FileFilter filter) {
		File[] result = new File[0];
		//
		try {
			if (isExist(file)) {
				if (filter != null) {
					result = file.listFiles(filter);
				} else {
					result = file.listFiles();
				}
				//
				if (result != null) {
					for (File entry : result) {
						result = ArrayUtil.addUnique(result, dir(entry, filter), File[].class);
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during dir()", ex);
		}

		return result;
	}

	/**
	 * 刪除檔案
	 *
	 * @param fileName
	 * @return
	 */
	public static boolean delete(String fileName) {
		if (fileName == null) {
			return false;
		}
		return delete(new File(fileName));
	}

	/**
	 * 刪除檔案
	 *
	 * @param file
	 * @return
	 */
	public static boolean delete(File file) {
		return FileUtils.deleteQuietly(file);
	}

	/**
	 * 組合目錄
	 *
	 * @param path1
	 * @param path2
	 * @return
	 */
	public static String combine(String path1, String path2) {
		String result = null;
		try {
			StringBuilder buff = new StringBuilder();
			if (path1 != null) {
				buff.append(path1);
			}
			if (path2 != null) {
				if (path1 != null && path1.length() > 0) {
					buff.append("/");
				}
				buff.append(path2);
			}
			//
			if (buff.length() > 0) {
				result = buff.toString();
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during combine()", ex);
		}
		return result;
	}

	/**
	 *
	 * @param path
	 *            目錄
	 * @param pattern
	 *            注意前面不加 "." xml, txt, jpg ...
	 * @return
	 */
	public static List<String> getFiles(String path, String... patterns) {
		List<String> result = new LinkedList<>();
		try {
			File dir = new File(path);
			String[] fileNames = dir.list(new FileExtensionFilter(patterns));
			for (String fileName : fileNames) {
				result.add(fileName);
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during getFiles()", ex);
		}
		return result;
	}

	/**
	 * 檢查路徑所在之硬碟剩餘空間
	 * 
	 * @param path
	 * @return bytes
	 */
	public static Long getUsableSpace(String path) {
		File file = new File(path);
		return file.getUsableSpace();
	}

	/**
	 * 
	 * @param originalPath
	 * @param newPath
	 * @return
	 */
	public static File renameFile(String originalPath, String newPath) {
		File oldFileName = new File(originalPath);
		File newFileName = new File(newPath);
		if (oldFileName.renameTo(newFileName)) {
			return newFileName;
		} else {
			return null;
		}
	}

	/**
	 * 檔案副檔名filter
	 */
	public static class FileExtensionFilter implements FilenameFilter {

		private Set<String> exts = new HashSet<>();

		public FileExtensionFilter(String... extensions) {
			for (String ext : extensions) {
				exts.add(ext.toLowerCase().trim());
			}
		}

		public boolean accept(File dir, String name) {
			for (String ext : exts) {
				if ((name.endsWith(ext) && (name.charAt(name.length() - ext.length() - 1)) == '.')) {
					return true;
				}
			}
			return false;
		}
	}
}
