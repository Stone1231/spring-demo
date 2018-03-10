package com.demo.utils;

import java.io.File;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CryptoUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(CryptoUtil.class);

	private static final int BUFFER_SIZE = 16384;

	private CryptoUtil() {
	}

	public static String md5(String message) {
		String md5 = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] barr = md.digest(message.getBytes()); // 將 byte 陣列加密
			StringBuilder sb = new StringBuilder(); // 將 byte 陣列轉成 16 進制
			for (int i = 0; i < barr.length; i++) {
				sb.append(byte2Hex(barr[i]));
			}
			String hex = sb.toString();
			md5 = hex.toUpperCase(); // 一律轉成大寫
		} catch (Exception e) {
			LOGGER.error("Exception encountered during md5()", e);
		}
		return md5;
	}

	public static String hmac_sha256(String key, String message) {
		String hash = null;
		try {
			String algorithm = "HmacSHA256";
			Mac mac = Mac.getInstance(algorithm);
			SecretKeySpec secret = new SecretKeySpec(key.getBytes(), algorithm);
			mac.init(secret);
			hash = Base64.encodeBase64String(mac.doFinal(message.getBytes()));
		} catch (Exception e) {
			LOGGER.error("Exception encountered during hmac_sha256()", e);
		}
		return hash;
	}

	public static String aesEncrypt(String key, String message) {
		String encrypted = "";
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(key.getBytes("UTF-8"));
			kgen.init(secureRandom);
			/**
			 * kgen.init(128, new SecureRandom(key.getBytes()));
			 */
			SecretKey skey = kgen.generateKey();
			byte[] raw = skey.getEncoded();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypt = cipher.doFinal(message.getBytes());
			encrypted = Base64.encodeBase64String(encrypt);
		} catch (Exception e) {
			LOGGER.error("Exception encountered during aseEncrypt()", e);
		}
		return encrypted;
	}

	public static String aesDecrypt(String key, String cipherText) {
		String decrypted = "";
		try {
			byte[] buff = Base64.decodeBase64(cipherText);
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(key.getBytes("UTF-8"));
			kgen.init(secureRandom);
			/**
			 * kgen2.init(128, new SecureRandom(key.getBytes()));
			 */
			SecretKey skey2 = kgen.generateKey();
			byte[] raw2 = skey2.getEncoded();
			SecretKeySpec skeySpec2 = new SecretKeySpec(raw2, "AES");
			Cipher cipher2 = Cipher.getInstance("AES");
			cipher2.init(Cipher.DECRYPT_MODE, skeySpec2);
			byte[] decrypt = cipher2.doFinal(buff);
			decrypted = new String(decrypt);
		} catch (Exception e) {
			LOGGER.error("Exception encountered during aseDecrypt()", e);
		}
		return decrypted;
	}

	private static String byte2Hex(byte b) {
		String[] h = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
		int i = b;
		if (i < 0) {
			i += 256;
		}
		return h[i / 16] + h[i % 16];
	}

	public static String computeSha256(byte[] bytes) {

		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Exception encountered during computeSha256()", e);
		}

		if (digest == null) {
			return null;
		}

		digest.reset();

		byte[] byteData = null;
		byteData = digest.digest(bytes);
		StringBuilder sb = new StringBuilder();

		if (bytes == null) {
			return null;
		}

		/**
		 * 舊版
		 *
		 * for (int i = 0; i < bytes.length; i++) {
		 *
		 * sb.append(Integer.toString((bytes[i] & 0xff) + 0x100,
		 *
		 * 16).substring(1)); }
		 */
		// 修正版
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	public static String nioComputeSha256(File file) {
		RandomAccessFile rndFile = null;
		try {
			rndFile = new RandomAccessFile(file.getAbsoluteFile(), "r");
			MessageDigest hashSum = MessageDigest.getInstance("SHA-256");
			byte[] buffer = new byte[BUFFER_SIZE];
			byte[] partialHash = null;
			long read = 0;

			// calculate the hash of the hole file for the test
			long offset = rndFile.length();
			int unitSize;
			while (read < offset) {
				unitSize = (int) (((offset - read) >= BUFFER_SIZE) ? BUFFER_SIZE : (offset - read));
				rndFile.read(buffer, 0, unitSize);

				hashSum.update(buffer, 0, unitSize);

				read += unitSize;
			}

			rndFile.close();
			partialHash = new byte[hashSum.getDigestLength()];
			partialHash = hashSum.digest();

			if (partialHash == null || partialHash.length == 0) {
				return null;
			}

			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < partialHash.length; i++) {
				sb.append(Integer.toString((partialHash[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();

		} catch (Exception e) {
			LOGGER.error("Exception encountered during nioComputeSha256()", e);
		} finally {
			IoUtil.close(rndFile);
		}
		return null;
	}
}
