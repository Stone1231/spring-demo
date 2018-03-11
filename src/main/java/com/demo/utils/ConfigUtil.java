package com.demo.utils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.ServletContextResource;

import com.demo.base.util.AbstractCommonUtil;

/**
 * 1.預設設定檔: src/test/config/etc/config.xml
 *
 * 2.直接使用static方法取值
 * 
 * 3.可利用spring重新給設定檔路徑configLocation
 */
public class ConfigUtil extends AbstractCommonUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtil.class);

	/**
	 * 預設設定檔目錄
	 *
	 * src/test/config/etc
	 */
	private static final String DEFAULT_CONFIG_DIR = "src" + File.separator + "test" + File.separator + "config";

	/**
	 * 預設設定檔檔名
	 *
	 * config.xml
	 */
	private static final String DEFAULT_CONFIG_FILE_NAME = "config.xml";

	/**
	 * 預設設定檔
	 *
	 * src/test/config/etc/config.xml
	 */
	private static final String DEFAULT_CONFIG_FILE = DEFAULT_CONFIG_DIR + File.separator + "etc" + File.separator
			+ DEFAULT_CONFIG_FILE_NAME;

	/**
	 * 設定檔
	 */
	private static String configFile = DEFAULT_CONFIG_FILE;

	/**
	 * 設定檔資源,由spring注入
	 *
	 * file:src/test/config/etc/config.xml
	 */
	private static Resource configLocation;

	// private static DefaultConfigurationBuilder configurationBuilder;

	private static CombinedConfiguration configuration;

	// --------------------------------------------------------
	/**
	 * 預設log4j設定檔檔名
	 * 
	 * log4j.properties
	 */
	private static final String DEFAULT_LOG4J_CONFIG_FILE_NAME = "log4j.properties";

	private static final String DEFAULT_LOG4J2_CONFIG_FILE_NAME = "log4j2.xml";

	/**
	 * 預設log4j設定檔
	 * 
	 * log4j.properties
	 */
	private static final String DEFAULT_LOG4J_CONFIG_FILE = DEFAULT_LOG4J_CONFIG_FILE_NAME;

	/**
	 * log4j設定檔
	 */
	private static String log4jConfigFile = DEFAULT_LOG4J_CONFIG_FILE;

	/**
	 * log4j設定檔,由spring注入
	 */
	private static Resource log4jConfigLocation;

	// --------------------------------------------------------
	// custom
	// --------------------------------------------------------

	/**
	 * 預設客製目錄
	 */
	private static String DEFAULT_CUSTOM_DIR = "custom";

	// --------------------------------------------------------

	/**
	 * 預設input目錄
	 * 
	 * custom/input
	 */
	private static final String DEFAULT_INPUT_DIR = DEFAULT_CUSTOM_DIR + File.separator + "input";

	/**
	 * input目錄
	 */
	private static String inputDir = DEFAULT_INPUT_DIR;

	/**
	 * input目錄資源,由spring注入
	 */
	private static Resource inputDirLocation;

	// --------------------------------------------------------

	/**
	 * 預設output目錄
	 * 
	 * custom/output
	 */
	private static final String DEFAULT_OUTPUT_DIR = DEFAULT_CUSTOM_DIR + File.separator + "output";

	/**
	 * output目錄
	 */
	private static String outputDir = DEFAULT_OUTPUT_DIR;

	/**
	 * output目錄資源,由spring注入
	 */
	private static Resource outputDirLocation;

	// --------------------------------------------------------
	/**
	 * 預設download目錄
	 */
	private static final String DEFAULT_DOWNLOAD_DIR = DEFAULT_CUSTOM_DIR + File.separator + "download";

	/**
	 * download目錄
	 */
	private static String downloadDir = DEFAULT_DOWNLOAD_DIR;

	/**
	 * download目錄資源,由spring注入
	 */
	private static Resource downloadDirLocation;

	// --------------------------------------------------------
	/**
	 * 預設upload目錄
	 */
	private static final String DEFAULT_UPLOAD_DIR = DEFAULT_CUSTOM_DIR + File.separator + "upload";

	/**
	 * upload目錄
	 */
	private static String uploadDir = DEFAULT_UPLOAD_DIR;

	/**
	 * upload目錄資源,由spring注入
	 */
	private static Resource uploadDirLocation;
	//
	/**
	 * 是否除錯
	 *
	 * config-debug.xml
	 */
	public static final String DEBUG = "configUtil.debug";

	static {
		new Static();
	}

	protected static class Static {
		public Static() {
			buildConfiguration(true, null);
		}
	}

	public ConfigUtil() {
		//
	}

	public static String getConfigFile() {
		return configFile;
	}

	public static void setConfigFile(String configFile) {
		ConfigUtil.configFile = configFile;
		ConfigUtil.configLocation = null;
		//
		buildConfiguration(false, configFile);
	}

	/**
	 * spring注入
	 *
	 * @return
	 */
	public static Resource getConfigLocation() {
		return configLocation;
	}

	public static void setConfigLocation(Resource configLocation) {
		ConfigUtil.configLocation = configLocation;
		ConfigUtil.configFile = getFile(configLocation);
		//
		buildConfiguration(false, null);
	}

	/**
	 * 取得檔名含路徑
	 *
	 * @param resource
	 * @return
	 */
	public static String getFile(Resource resource) {
		String result = null;
		try {
			if (resource != null) {
				result = resource.getFile().getPath();
			}
		} catch (Exception ex) {
			//
		}
		return result;
	}

	// --------------------------------------------------------

	public static String getLog4jConfigFile() {
		return log4jConfigFile;
	}

	public static void setLog4jConfigFile(String log4jConfigFile) {
		ConfigUtil.log4jConfigFile = log4jConfigFile;
		ConfigUtil.log4jConfigLocation = null;
		//
		// int pos = log4jConfigFile.indexOf("log4j2");
		// // log4j
		// if (pos < 0) {
		// PropertyConfigurator.configure(log4jConfigFile);
		// LOGGER.info("Using Log4j");
		//
		// } else {
		// log4j2
//		LoggerContext context = (LoggerContext) LogManager.getContext(false);
//		context.setConfigLocation(new File(log4jConfigFile).toURI());
		LOGGER.info("Using Log4j2");
		// }
	}

	/**
	 * spring注入
	 * 
	 * @return
	 */
	public static Resource getLog4jConfigLocation() {
		return log4jConfigLocation;
	}

	public static void setLog4jConfigLocation(Resource log4jConfigLocation) throws IOException {
		ConfigUtil.log4jConfigLocation = log4jConfigLocation;
		ConfigUtil.log4jConfigFile = getFile(log4jConfigLocation);
		if (log4jConfigLocation != null) {
			//
			// int pos = log4jConfigFile.indexOf("log4j2");
			// // log4j
			// if (pos < 0) {
			// PropertyConfigurator.configure(log4jConfigFile);
			// LOGGER.info("Using Log4j");
			// } else {
			// log4j2
//			LoggerContext context = (LoggerContext) LogManager.getContext(false);
//			context.setConfigLocation(log4jConfigLocation.getURI());
			LOGGER.info("Using Log4j2");
			// }
		}
	}

	// --------------------------------------------------------

	public static String getInputDir() {
		return inputDir;
	}

	public static void setInputDir(String inputDir) {
		ConfigUtil.inputDir = inputDir;
		ConfigUtil.inputDirLocation = null;
		//
		buildDir(DEFAULT_INPUT_DIR, inputDirLocation, inputDir);
	}

	public static Resource getInputDirLocation() {
		return inputDirLocation;
	}

	public static void setInputDirLocation(Resource inputDirLocation) {
		ConfigUtil.inputDirLocation = inputDirLocation;
		ConfigUtil.inputDir = getFile(inputDirLocation);
		//
		buildDir(DEFAULT_INPUT_DIR, inputDirLocation, null);
	}

	// --------------------------------------------------------
	public static String getOutputDir() {
		return outputDir;
	}

	public static void setOutputDir(String outputDir) {
		ConfigUtil.outputDir = outputDir;
		ConfigUtil.outputDirLocation = null;
		//
		buildDir(DEFAULT_OUTPUT_DIR, outputDirLocation, outputDir);
	}

	public static Resource getOutputDirLocation() {
		return outputDirLocation;
	}

	public static void setOutputDirLocation(Resource outputDirLocation) {
		ConfigUtil.outputDirLocation = outputDirLocation;
		ConfigUtil.outputDir = getFile(outputDirLocation);
		//
		buildDir(DEFAULT_OUTPUT_DIR, outputDirLocation, null);
	}

	// --------------------------------------------------------
	public static String getDownloadDir() {
		return downloadDir;
	}

	public static void setDownloadDir(String downloadDir) {
		ConfigUtil.downloadDir = downloadDir;
		ConfigUtil.downloadDirLocation = null;
		//
		buildDir(DEFAULT_DOWNLOAD_DIR, downloadDirLocation, downloadDir);
	}

	public static Resource getDownloadDirLocation() {
		return downloadDirLocation;
	}

	public static void setDownloadDirLocation(Resource downloadDirLocation) {
		ConfigUtil.downloadDirLocation = downloadDirLocation;
		ConfigUtil.downloadDir = getFile(downloadDirLocation);
		//
		buildDir(DEFAULT_DOWNLOAD_DIR, downloadDirLocation, null);
	}

	// --------------------------------------------------------
	public static String getUploadDir() {
		return uploadDir;
	}

	public static void setUploadDir(String uploadDir) {
		ConfigUtil.uploadDir = uploadDir;
		ConfigUtil.uploadDirLocation = null;
		//
		buildDir(DEFAULT_UPLOAD_DIR, uploadDirLocation, uploadDir);
	}

	public static Resource getUploadDirLocation() {
		return uploadDirLocation;
	}

	public static void setUploadDirLocation(Resource uploadDirLocation) {
		ConfigUtil.uploadDirLocation = uploadDirLocation;
		ConfigUtil.uploadDir = getFile(uploadDirLocation);
		//
		buildDir(DEFAULT_UPLOAD_DIR, uploadDirLocation, null);
	}

	// --------------------------------------------------------

	/**
	 * 建構configuration
	 *
	 * @param firstBuild
	 *            是否第一次建構
	 * @param assignFile
	 *            指定檔案
	 */
	protected static void buildConfiguration(boolean firstBuild, String assignFile) {
		try {
			DefaultConfigurationBuilder configurationBuilder = buildConfigurationBuilder(firstBuild, assignFile);
			if (configurationBuilder == null) {
				if (firstBuild) {
					return;
				} else {
					StringBuilder buff = new StringBuilder();
					buff.append("can not find configLocation:");
					buff.append((assignFile != null ? assignFile : configLocation));
					LOGGER.error(buff.toString());
					return;
				}
			}
			//
			configuration = configurationBuilder.getConfiguration(true);
			configuration.setForceReloadCheck(true);

		} catch (Exception ex) {
			LOGGER.error("Exception encountered during buildConfiguration()", ex);
		}
	}

	/**
	 * 建構configurationBuilder
	 *
	 * @param assignFile
	 * @return
	 */
	protected static DefaultConfigurationBuilder buildConfigurationBuilder(boolean firstBuild, String assignFile) {
		DefaultConfigurationBuilder result = null;
		//
		try {
			// 當沒使用spring注入時,或指定設定檔
			if (configLocation == null || assignFile != null) {
				String fileName = (assignFile != null ? assignFile : DEFAULT_CONFIG_FILE);
				File file = new File(fileName);
				if (file.exists()) {
					result = new DefaultConfigurationBuilder(file);
					result.setFile(file);

					if (firstBuild) {
						LOGGER.info("Initialization of file [" + fileName + "]");
					} else {
						LOGGER.info("Reinitialization of file [" + fileName + "]");
					}
				} else {
					LOGGER.warn("[" + fileName + "] File does not exist");
				}

			}
			// 使用spring注入時
			else {
				// file:src/test/config/etc/config.xml
				// src/test/config/etc/config.xml
				URL url = configLocation.getURL();
				LOGGER.info("Reinitialization with Spring [" + (url != null ? url.getFile() : null) + "]");
				if (url != null) {
					result = new DefaultConfigurationBuilder(url);
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during buildConfigurationBuilder()", ex);
		}
		//
		return result;
	}

	/**
	 * 建構目錄
	 *
	 * @param defaultDir
	 * @param resource
	 */
	protected static void buildDir(String defaultDir, Resource resource, String assignDir) {
		try {
			// 當沒使用spring注入時,或指定目錄
			if (resource == null || assignDir != null) {
				File file = new File(assignDir != null ? assignDir : defaultDir);
				FileUtil.md(file);
			}
			// 使用spring注入時
			else {
				// /WEB-INF/xml
				// /custom/output
				if (resource instanceof ServletContextResource) {
					ServletContextResource recource = (ServletContextResource) resource;
					// 1./aaa/WEB-INF/xml
					// 2./aaa/custom/output
					FileUtil.md(recource.getFile().getAbsolutePath());
				}
				// file:xml
				// xml
				// custom/output
				else {
					URL url = resource.getURL();
					if (url != null) {
						FileUtil.md(url.getFile());
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Exception encountered during buildDir()", ex);
		}
	}

	public static boolean isDebug() {
		try {
			return getBoolean(DEBUG, false);
		} catch (Exception ex) {
		}
		return true;
	}

	public static CombinedConfiguration getConfiguration() {
		return configuration;
	}

	public static boolean isEmpty() {
		return configuration.isEmpty();
	}

	public static boolean containsKey(String key) {
		return configuration.containsKey(key);
	}

	public static void addProperty(String key, Object value) {
		configuration.addProperty(key, value);
	}

	public static void setProperty(String key, Object value) {
		configuration.setProperty(key, value);
	}

	public static void clearProperty(String key) {
		configuration.clearProperty(key);
	}

	public static Object getProperty(String key) {
		return configuration.getProperty(key);
	}

	@SuppressWarnings("rawtypes")
	public static Iterator getKeys(String prefix) {
		return configuration.getKeys(prefix);
	}

	@SuppressWarnings("rawtypes")
	public static Iterator getKeys() {
		return configuration.getKeys();
	}

	public static Properties getProperties(String key) {
		return configuration.getProperties(key);
	}

	public static boolean getBoolean(String key) {
		return configuration.getBoolean(key);
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return configuration.getBoolean(key, defaultValue);
	}

	public static byte getByte(String key) {
		return configuration.getByte(key);
	}

	public static byte getByte(String key, byte defaultValue) {
		return configuration.getByte(key, defaultValue);
	}

	public static short getShort(String key) {
		return configuration.getShort(key);
	}

	public static short getShort(String key, short defaultValue) {
		return configuration.getShort(key, defaultValue);
	}

	public static int getInt(String key) {
		return configuration.getInt(key);
	}

	public static int getInt(String key, int defaultValue) {
		return configuration.getInt(key, defaultValue);
	}

	public static long getLong(String key) {
		return configuration.getLong(key);
	}

	public static long getLong(String key, long defaultValue) {
		return configuration.getLong(key, defaultValue);
	}

	public static float getFloat(String key) {
		return configuration.getFloat(key);
	}

	public static float getFloat(String key, float defaultValue) {
		return configuration.getFloat(key, defaultValue);
	}

	public static double getDouble(String key) {
		return configuration.getDouble(key);
	}

	public static double getDouble(String key, double defaultValue) {
		return configuration.getDouble(key, defaultValue);
	}

	public static BigDecimal getBigDecimal(String key) {
		return configuration.getBigDecimal(key);
	}

	public static BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		return configuration.getBigDecimal(key, defaultValue);
	}

	public static BigInteger getBigInteger(String key) {
		return configuration.getBigInteger(key);
	}

	public static BigInteger getBigInteger(String key, BigInteger defaultValue) {
		return configuration.getBigInteger(key, defaultValue);
	}

	public static String getString(String key) {
		return configuration.getString(key);
	}

	public static String getString(String key, String defaultValue) {
		return configuration.getString(key, defaultValue);
	}

	public static String[] getStringArray(String key) {
		return configuration.getStringArray(key);
	}

	public static Map<String, String> getMap(String key) {
		Map<String, String> result = null;
		//
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		sb.append(".entry");
		List<HierarchicalConfiguration> list = configurationsAt(sb.toString());
		if (!list.isEmpty()) {
			result = new LinkedHashMap<>();
			for (HierarchicalConfiguration node : list) {
				String nodeKey = node.getString("key");
				String nodeValue = node.getString("value");
				result.put(nodeKey, nodeValue);
			}
		}
		return result;
	}

	public static Map<String, String> getMap(String key, Map<String, String> defaultValue) {
		Map<String, String> result = getMap(key);
		if (result == null || result.isEmpty()) {
			result = defaultValue;
		}
		return result;
	}

	public static List<HierarchicalConfiguration> configurationsAt(String key) {
		return configuration.configurationsAt(key);
	}

	public static SubnodeConfiguration configurationAt(String key) {
		return configurationAt(key, false);
	}

	public static List getList(String key) {
		return configuration.getList(key);
	}

	public static SubnodeConfiguration configurationAt(String key, boolean supportUpdates) {
		return configuration.configurationAt(key, supportUpdates);
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("configLocation", configLocation);
		builder.append("inputDirLocation", inputDirLocation);
		builder.append("outputDirLocation", outputDirLocation);
		builder.append("downloadDirLocation", downloadDirLocation);
		builder.append("uploadDirLocation", uploadDirLocation);
		return builder.toString();
	}
}
