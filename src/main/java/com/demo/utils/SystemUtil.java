package com.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Used to process System.
 */
public class SystemUtil {

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(SystemUtil.class);

	public static final String FILE_ENCODING = getProperty("file.encoding");

	public static final String FILE_SEPARATOR = getProperty("file.separator");

	public static final String JAVA_CLASS_PATH = getProperty("java.class.path");

	public static final String JAVA_CLASS_VERSION = getProperty("java.class.version");

	public static final String JAVA_COMPILER = getProperty("java.compiler");

	public static final String JAVA_EXT_DIRS = getProperty("java.ext.dirs");

	public static final String JAVA_HOME = getProperty("java.home");

	public static final String JAVA_IO_TMPDIR = getProperty("java.io.tmpdir");

	public static final String JAVA_LIBRARY_PATH = getProperty("java.library.path");

	public static final String JAVA_RUNTIME_NAME = getProperty("java.runtime.name");

	public static final String JAVA_RUNTIME_VERSION = getProperty("java.runtime.version");

	public static final String JAVA_SPECIFICATION_NAME = getProperty("java.specification.name");

	public static final String JAVA_SPECIFICATION_VENDOR = getProperty("java.specification.vendor");

	public static final String JAVA_SPECIFICATION_VERSION = getProperty("java.specification.version");

	public static final String JAVA_VENDOR = getProperty("java.vendor");

	public static final String JAVA_VENDOR_URL = getProperty("java.vendor.url");

	public static final String JAVA_VERSION = getProperty("java.version");

	public static final String JAVA_VM_INFO = getProperty("java.vm.info");

	public static final String JAVA_VM_NAME = getProperty("java.vm.name");

	public static final String JAVA_VM_SPECIFICATION_NAME = getProperty("java.vm.specification.name");

	public static final String JAVA_VM_SPECIFICATION_VENDOR = getProperty("java.vm.specification.vendor");

	public static final String JAVA_VM_SPECIFICATION_VERSION = getProperty("java.vm.specification.version");

	public static final String JAVA_VM_VENDOR = getProperty("java.vm.vendor");

	public static final String JAVA_VM_VERSION = getProperty("java.vm.version");

	public static final String LINE_SEPARATOR = getProperty("line.separator");

	public static final String OS_ARCH = getProperty("os.arch");

	public static final String OS_NAME = getProperty("os.name");

	public static final String OS_VERSION = getProperty("os.version");

	public static final String PATH_SEPARATOR = getProperty("path.separator");

	public static final String USER_COUNTRY = (getProperty("user.country") == null ? getProperty("user.region")
			: getProperty("user.country"));

	public static final String USER_DIR = getProperty("user.dir");

	public static final String USER_HOME = getProperty("user.home");

	public static final String USER_LANGUAGE = getProperty("user.language");

	public static final String USER_NAME = getProperty("user.name");

	// Java version
	// -----------------------------------------------------------------------

	// 1.2f for JDK 1.2
	// 1.31f for JDK 1.3.1
	public static final float JAVA_VERSION_FLOAT = getJavaVersionAsFloat();

	// 120 for JDK 1.2
	// 131 JDK 1.3.1
	public static final int JAVA_VERSION_INT = getJavaVersionAsInt();

	// Java version checks
	// -----------------------------------------------------------------------

	public static final boolean IS_JAVA_1_1 = getJavaVersionMatches("1.1");

	public static final boolean IS_JAVA_1_2 = getJavaVersionMatches("1.2");

	public static final boolean IS_JAVA_1_3 = getJavaVersionMatches("1.3");

	public static final boolean IS_JAVA_1_4 = getJavaVersionMatches("1.4");

	public static final boolean IS_JAVA_1_5 = getJavaVersionMatches("1.5");

	// Operating system checks
	// -----------------------------------------------------------------------
	public static final boolean IS_OS_AIX = getOSMatches("AIX");

	public static final boolean IS_OS_HP_UX = getOSMatches("HP-UX");

	public static final boolean IS_OS_IRIX = getOSMatches("Irix");

	public static final boolean IS_OS_LINUX = getOSMatches("Linux")
			|| getOSMatches("LINUX");

	public static final boolean IS_OS_MAC = getOSMatches("Mac");

	public static final boolean IS_OS_MAC_OSX = getOSMatches("Mac OS X");

	public static final boolean IS_OS_OS2 = getOSMatches("OS/2");

	public static final boolean IS_OS_SOLARIS = getOSMatches("Solaris");

	public static final boolean IS_OS_SUN_OS = getOSMatches("SunOS");

	public static final boolean IS_OS_WINDOWS = getOSMatches("Windows");

	public static final boolean IS_OS_WINDOWS_2000 = getOSMatches("Windows",
			"5.0");

	public static final boolean IS_OS_WINDOWS_95 = getOSMatches("Windows 9",
			"4.0");

	public static final boolean IS_OS_WINDOWS_98 = getOSMatches("Windows 9",
			"4.1");

	public static final boolean IS_OS_WINDOWS_ME = getOSMatches("Windows",
			"4.9");

	public static final boolean IS_OS_WINDOWS_NT = getOSMatches("Windows NT");

	public static final boolean IS_OS_WINDOWS_XP = getOSMatches("Windows",
			"5.1");

	private SystemUtil() {
	}

	public static float getJavaVersion() {
		return JAVA_VERSION_FLOAT;
	}

	private static float getJavaVersionAsFloat() {
		if (JAVA_VERSION == null) {
			return 0f;
		}
		String str = JAVA_VERSION.substring(0, 3);
		if (JAVA_VERSION.length() >= 5) {
			str = str + JAVA_VERSION.substring(4, 5);
		}
		return Float.parseFloat(str);
	}

	private static int getJavaVersionAsInt() {
		if (JAVA_VERSION == null) {
			return 0;
		}
		String str = JAVA_VERSION.substring(0, 1);
		str = str + JAVA_VERSION.substring(2, 3);
		if (JAVA_VERSION.length() >= 5) {
			str = str + JAVA_VERSION.substring(4, 5);
		} else {
			str = str + "0";
		}
		return Integer.parseInt(str);
	}

	private static boolean getJavaVersionMatches(String versionPrefix) {
		if (JAVA_VERSION == null) {
			return false;
		}
		return JAVA_VERSION.startsWith(versionPrefix);
	}

	private static boolean getOSMatches(String osNamePrefix) {
		if (OS_NAME == null) {
			return false;
		}
		return OS_NAME.startsWith(osNamePrefix);
	}

	private static boolean getOSMatches(String osNamePrefix,
			String osVersionPrefix) {
		if (OS_NAME == null || OS_VERSION == null) {
			return false;
		}
		return OS_NAME.startsWith(osNamePrefix)
				&& OS_VERSION.startsWith(osVersionPrefix);
	}

	// -----------------------------------------------------------------------

	public static String getProperty(String key) {
		return getProperty(key, null);
	}

	public static String getProperty(String key, String defaultValue) {
		String value = null;
		try {
			value = System.getProperty(key);
		} catch (Exception ex) {
		}
		return StringUtil.isNotEmpty(value) ? value : defaultValue;
	}

	public static String setProperty(String key, String value) {
		return System.setProperty(key, value);
	}

	public static boolean isJavaVersionAtLeast(float requiredVersion) {
		return (JAVA_VERSION_FLOAT >= requiredVersion);
	}

	public static boolean isJavaVersionAtLeast(int requiredVersion) {
		return (JAVA_VERSION_INT >= requiredVersion);
	}

	public static <T> void println(T value) {
		System.out.println(ObjectUtil.toString(value));
	}

	public static <T> void println(T[] values) {
		System.out.println(ObjectUtil.toString(values));
	}

	public static <T> void println(String title, final T[] values) {
		System.out.print(title);
		System.out.println(ObjectUtil.toString(values));
	}

	public static void println(final byte[] values) {
		println("", values);
	}

	public static void println(String title, final byte[] values) {
		System.out.print(title);
		System.out.println(ObjectUtil.toString(values));
	}

}
