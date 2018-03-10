package com.demo.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;


/**
 * Used to process Encoding.
 */
public class EncodingUtil {

	public static final String BIG5 = "BIG5";

	public static final String CP850 = "CP850";

	public static final String GBK = "GBK";

	public static final String UTF_8 = "UTF-8";

	public static final String ISO_8859_1 = "ISO-8859-1";

	private EncodingUtil() {
	}

	/**
	 * UTF-8編碼
	 *
	 * @param value
	 * @param srcEncoding
	 * @return
	 */
	public static String encodeUtf8(String value, String srcEncoding) {
		return encodeString(value, srcEncoding, UTF_8);
	}

	/**
	 * BIG5編碼
	 *
	 * @param value
	 * @param srcEncoding
	 * @return
	 */
	public static String encodeBig5(String value, String srcEncoding) {
		return encodeString(value, srcEncoding, BIG5);
	}

	/**
	 * 字串編碼
	 *
	 * @param value
	 * @param srcEncoding
	 * @param destEncoding
	 * @return
	 */
	public static String encodeString(String value, String srcEncoding, String destEncoding) {
		String result = null;
		//
		try {
			if (value != null) {
				byte[] bytes = value.getBytes(srcEncoding);
				result = new String(bytes, destEncoding);
			}
		} catch (Exception ex) {
			//
		}
		return result;
	}

	/**
	 * cp850->big5
	 *
	 * @param value
	 * @return
	 */
	public static String cp850ToBig5(String value) {
		return encodeString(value, CP850, BIG5);
	}

	/**
	 * url編碼
	 *
	 * @param value
	 * @return
	 */
	public static String encodeUrl(String value) {
		return encodeUrl(value, UTF_8);
	}

	/**
	 * url編碼
	 *
	 * @param value
	 * @param encoding
	 * @return
	 */
	public static String encodeUrl(String value, String encoding) {
		String result = null;
		try {
			if (value != null && encoding != null) {
				result = URLEncoder.encode(value, encoding);
			}
		} catch (Exception ex) {
			//
		}
		return result;
	}

	/**
	 * url解碼
	 *
	 * @param value
	 * @return
	 */
	public static String decodeUrl(String value) {
		return decodeUrl(value, UTF_8);
	}

	/**
	 * url解碼
	 *
	 * @param value
	 * @param encoding
	 * @return
	 */
	public static String decodeUrl(String value, String encoding) {
		String result = null;
		try {
			if (value != null && encoding != null) {
				result = URLDecoder.decode(value, encoding);
			}
		} catch (Exception ex) {
			//
		}
		return result;
	}

	public static String encodeHex(String value) {
		return encodeHex(value, EncodingUtil.UTF_8);
	}

	/**
	 *
	 * 編碼,將字串轉成16進位string,如:abc -> 616263
	 *
	 * @param value
	 * @param charsetName
	 * @return
	 */
	public static String encodeHex(String value, String charsetName) {
		byte[] buffs = ByteUtil.toBytes(value, charsetName);
		return encodeHex(buffs);
	}

	/**
	 * 編碼,將byte[]轉成16進位hexString,如:97, 98, 99 -> 616263
	 *
	 * @param value
	 * @return
	 */
	public static String encodeHex(byte[] values) {
		return encodeHex(values, true);
	}

	public static String encodeHex(byte[] values, boolean toLowerCase) {
		return new String(Hex.encodeHex(values, toLowerCase));
	}

	public static byte[] decodeHex(byte[] values) {
		return decodeHex(ByteUtil.toString(values));
	}

	/**
	 * 解碼,將16進位hexString轉成byte[],如:616263 -> 97, 98, 99
	 *
	 * @param hexValue
	 * @return
	 */
	public static byte[] decodeHex(String value) {
		try {
			return Hex.decodeHex(value.toCharArray());
		} catch (Exception ex) {
			//
		}
		return null;
	}

	public static String decodeHexString(String value) {
		return decodeHexString(value, EncodingUtil.UTF_8);
	}

	/**
	 * 解碼,將16進位hexString轉成string,如:616263 -> abc
	 *
	 * @param hexValue
	 * @return
	 */
	public static String decodeHexString(String value, String charsetName) {
		byte[] buffs = decodeHex(value);
		return ByteUtil.toString(buffs, charsetName);
	}

	public static String encodeBase64(String value) {
		return encodeBase64(value, EncodingUtil.UTF_8);
	}

	public static String encodeBase64(String value, String charsetName) {
		byte[] buffs = ByteUtil.toBytes(value, charsetName);
		return encodeBase64(buffs, charsetName);
	}

	public static String encodeBase64(byte[] values) {
		return encodeBase64(values, "UTF-8");
	}

	/**
	 * 編碼,無key
	 *
	 * @param value
	 * @return
	 */
	public static String encodeBase64(byte[] values, String charsetName) {
		String result = null;
		if (ByteUtil.isNotEmpty(values)) {
			try {
				/**
				 * 不要再用 sun.BASE64Encoder, sun.BASE64Decoder
				 * 
				 * 改成 org.apache.commons.codec.binary.Base64
				 * 
				 * BASE64Encoder encoder = new BASE64Encoder();
				 * 
				 * result = encoder.encodeBuffer(values).trim();
				 */
				byte[] encoded = Base64.encodeBase64(values);
				result = (encoded != null ? new String(encoded, charsetName) : null);
			} catch (Exception ex) {
				//
			}
		}
		return result;
	}

	public static byte[] decodeBase64(byte[] values) {
		return decodeBase64(ByteUtil.toString(values));
	}

	public static byte[] decodeBase64(String value) {
		return decodeBase64(value, EncodingUtil.UTF_8);
	}

	/**
	 * 解碼,無key
	 *
	 * @param value
	 * @return
	 */
	public static byte[] decodeBase64(String value, String charsetName) {
		byte[] result = new byte[0];
		if (StringUtil.isNotEmpty(value) && StringUtil.isNotEmpty(charsetName)) {
			try {
				/**
				 * 不要再用 sun.BASE64Encoder, sun.BASE64Decoder
				 * 
				 * 改成 org.apache.commons.codec.binary.Base64
				 * 
				 * BASE64Decoder decoder = new BASE64Decoder();
				 * 
				 * result = decoder.decodeBuffer(value);
				 */
				result = Base64.decodeBase64(ByteUtil.toBytes(value, charsetName));
			} catch (Exception ex) {
				//
			}
		}
		return result;
	}

	public static String decodeBase64String(String value) {
		return decodeBase64String(value, EncodingUtil.UTF_8);
	}

	/**
	 * 解碼,將16進位hexString轉成string,如:616263 -> abc
	 *
	 * @param hexValue
	 * @return
	 */
	public static String decodeBase64String(String value, String charsetName) {
		byte[] buffs = decodeBase64(value);
		return ByteUtil.toString(buffs, charsetName);
	}
}
