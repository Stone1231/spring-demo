package com.demo.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.zip.CRC32;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.text.StrSubstitutor;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

public class StringUtil2 {

	private static final Logger LOGGER = LoggerFactory.getLogger(StringUtil.class);
    public static final Joiner SLASH_JOINER = Joiner.on('/');
    public static final Joiner BACKSLASH_JOINER = Joiner.on('\\');
    public static final Joiner DOT_JOINER = Joiner.on(',');
    public static final Splitter SLASH_SPLITTER = Splitter.on('/');

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String trim(String str) {
        return str == null ? "" : str.trim();
    }

    public static boolean startsIgnoreCaseWith(String str, String prefix) {
        return str != null && prefix != null && str.toLowerCase().startsWith(prefix.toLowerCase());
    }

    public static boolean equalsIgnoreCase(String str1, String query) {
        return str1 != null && str1.equalsIgnoreCase(query);
    }

    public static boolean equals(String str1, String query) {
        return str1 != null && str1.equals(query);
    }

    public static String toUpper(String str) {
        return str == null ? "" : str.toUpperCase();
    }

    public static String toLower(String str) {
        return str == null ? "" : str.toLowerCase();
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static String generateAuthorization(String userId, String adminToken) {
        return encodeBase64(concat(userId, ":", adminToken));
    }

    public static String generateAuthorization(String userId, String adminToken, String deviceId) {
        return encodeBase64(concat(userId, ":", adminToken, ":", deviceId));
    }

    public static String generateAuthorization(String userId, String adminToken, String deviceId, String language) {
        return encodeBase64(concat(userId, ":", adminToken, ":", deviceId, ":", language));
    }

    public static String getUserIdByAuthorization(String authorization, String adminToken) {
        if (isNullOrEmpty(authorization) || isNullOrEmpty(adminToken)) {
            return null;
        }
        String[] decodedDatas = decodeBase64(authorization).split(":");
        return decodedDatas.length >= 2 && decodedDatas[1].equals(adminToken) ? decodedDatas[0] : null;
    }

    public static String concat(Object... parts) {
        if (parts == null || parts.length <= 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (Object part : parts) {
            result.append(part);
        }
        return result.toString();
    }

    public static boolean containsIgnoreCase(String str, String query) {
        if (isNullOrEmpty(str) || isNullOrEmpty(query)) {
            return false;
        }
        return str.toLowerCase().contains(query.toLowerCase());
    }

    public static String formatByNameParameters(String pattern, Map<String, Object> valuesMap) {
        StrSubstitutor strSubstitutor = new StrSubstitutor(valuesMap);
        strSubstitutor.setVariablePrefix("{{");
        strSubstitutor.setVariableSuffix("}}");
        return strSubstitutor.replace(pattern);
    }

    public static String escapeHTML(String str) {
        return StringEscapeUtils.escapeHtml4(str);
    }

    public static String escapeSql(String str) {
        return str == null ? "" : str.replace("'", "''");
    }

    public static String escapeSqlLikeString(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("'", "''").replaceAll("([_%])", "\\\\$1");
    }

    @SuppressWarnings("deprecation")
    public static String encodeMD5(String str) {
        if (str == null) {
            return "";
        }
        return Hashing.md5().hashString(str, Charsets.UTF_8).toString();
    }

    public static String encodeSha256(String str) {
        if (str == null) {
            return "";
        }
        return Hashing.sha256().hashString(str, Charsets.UTF_8).toString();
    }

    public static String encodeSha256(byte[] data) {
        if (data == null) {
            return "";
        }
        return Hashing.sha256().hashBytes(data).toString();
    }

//    public static String encodeSha384(String str) {
//        if (str == null) {
//            return "";
//        }
//        return Hashing.sha384().hashString(str, Charsets.UTF_8).toString();
//    }

    public static String encodeCrc32(String str) {
        if (str == null) {
            return "";
        }
        CRC32 crc = new CRC32();
        crc.update(str.getBytes(Charsets.UTF_8));
        return Long.toHexString(crc.getValue());
    }

    public static String encodeBase64(String str) {
        if (str == null) {
            return "";
        }
        return BaseEncoding.base64().encode(str.getBytes(Charsets.UTF_8));
    }

    public static String encodeBase64(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        return BaseEncoding.base64().encode(bytes);
    }

    public static String decodeBase64(String base64String) {
        if (base64String == null) {
            return "";
        }
        return new String(BaseEncoding.base64().decode(base64String), Charsets.UTF_8);
    }

    public static byte[] decodeBase64AsBytes(String base64String) {
        if (base64String == null) {
            return null;
        }
        return BaseEncoding.base64().decode(base64String);
    }

    public static Cipher getAesCipher(int keySize, String password, int cipherOpMode) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes(Charsets.UTF_8));
            keyGenerator.init(keySize, secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(cipherOpMode, skeySpec);
            return cipher;
        } catch (Exception ex) {
        	LOGGER.warn("getAesCipher error: {}", ex.getMessage());
        }
        return null;
    }

    public static String encryptAes(String secretKey, String ivParam, String value) {
        if (value == null) {
            return "";
        }
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(Charsets.UTF_8), "AES");
            IvParameterSpec iv = new IvParameterSpec(ivParam.getBytes(Charsets.UTF_8));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            return BaseEncoding.base64().encode(cipher.doFinal(value.getBytes(Charsets.UTF_8)));
        } catch (Exception ex) {
        	LOGGER.warn("encryptAes error: {}", ex.getMessage());
        }
        return "";
    }

    public static String encryptAes(int keySize, String password, String value) {
        if (value == null) {
            return "";
        }
        try {
            Cipher cipher = getAesCipher(keySize, password, Cipher.ENCRYPT_MODE);
            return BaseEncoding.base64().encode(cipher.doFinal(value.getBytes(Charsets.UTF_8)));
        } catch (Exception ex) {
        	LOGGER.warn("encryptAes error: {}", ex.getMessage());
        }
        return "";
    }

    public static String decryptAes(String secretKey, String ivParam, String encryptedValue) {
        if (encryptedValue == null) {
            return "";
        }
        try {
            IvParameterSpec iv = new IvParameterSpec(ivParam.getBytes(Charsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(Charsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            return new String(cipher.doFinal(BaseEncoding.base64().decode(encryptedValue)), Charsets.UTF_8);
        } catch (Exception ex) {
        	LOGGER.warn("decryptAes error: {}", ex.getMessage());
        }
        return "";
    }

    public static String decryptAes(int keySize, String password, String encryptedValue) {
        if (encryptedValue == null) {
            return "";
        }
        try {
            Cipher cipher = getAesCipher(keySize, password, Cipher.DECRYPT_MODE);
            return new String(cipher.doFinal(BaseEncoding.base64().decode(encryptedValue)), Charsets.UTF_8);
        } catch (Exception ex) {
        	LOGGER.warn("decryptAes error: {}", ex.getMessage());
        }
        return "";
    }

    public static Locale countryToLocale(String country) {
        country = (country != null ? country.toLowerCase() : "");
        if ("tw".equals(country)) {
            return Locale.TAIWAN;
        } else if ("jp".equals(country)) {
            return Locale.JAPAN;
        } else if ("es".equals(country)) {
            return new Locale("es", "ES");
        } else if ("cn".equals(country)) {
            return Locale.CHINA;
        }
        return Locale.ENGLISH;
    }

    public static ResourceBundle getBundleByCountry(String baseName, String country) {
        return ResourceBundle.getBundle(baseName, countryToLocale(country));
    }

    public static String translateByCountry(String baseName, String country, String key) {
        if (key != null) {
            try {
                return StringUtil2.getBundleByCountry(baseName, country).getString(key);
            } catch (MissingResourceException ex) { // ignore
            }
        }
        return key;
    }

    public static Locale languageToLocale(String language) {
        language = (language != null ? language.toLowerCase() : "");
        if ("zh_tw".equals(language)) {
            return Locale.TAIWAN;
        } else if ("ja_jp".equals(language)) {
            return Locale.JAPAN;
        } else if ("es_es".equals(language)) {
            return new Locale("es", "ES");
        } else if ("zh_cn".equals(language)) {
            return Locale.CHINA;
        }
        return Locale.ENGLISH;
    }

    public static ResourceBundle getBundleByLanguage(String baseName, String language) {
        return ResourceBundle.getBundle(baseName, languageToLocale(language));
    }

    public static String translateByLanguage(String baseName, String language, String key) {
        if (key != null) {
            try {
                return StringUtil2.getBundleByLanguage(baseName, language).getString(key);
            } catch (MissingResourceException ex) { // ignore
            }
        }
        return key;
    }

    public static String toNotNullValue(String str, String defaultValue) {
        return (str != null ? str : defaultValue);
    }

    public static String toNotNullValue(String str) {
        return toNotNullValue(str, "");
    }

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException uhe) {
        	LOGGER.error("get host name error", uhe);
        }
        return "";
    }

    public static String toString(StackTraceElement[] stackTrace) {
        StringBuilder result = new StringBuilder();
        for (int i = 0, max = stackTrace.length - 1; i <= max; i++) {
            result.append("\tat ");
            result.append(stackTrace[i]);
            if (i < max) {
                result.append('\n');
            }
        }
        return result.toString();
    }

    public static List<String> trimAndSplit(String values, String separator) {
        if (isNullOrEmpty(values)) {
            return new ArrayList<String>();
        }
        return Arrays.asList(values.trim().replaceAll(concat("\\s*", separator, "\\s*"), separator).split(separator));
    }

}