package com.kk.utils;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author sam
 * 
 * @version v0.1
 * 
 * Created on 2013-06-24
 * 
 * Revision History:
 * Date   		Reviser		Description
 * ----   		-------   	----------------------------------------------------
 * 
 * Description:字符的加密解密
 */
public class EncryptUtil {

	/**
	 * Description:MD5加密
	 * 
	 * @param plain
	 * 				加密数据
	 * @return
	 * 				加密后数据
	 */
	public static String Md5(String plain) {
		String str = DigestUtils.md5Hex(plain);
		return str;
	}

	/**
	 * Description:HEX加密
	 * 
	 * @param plain
	 * 				加密数据
	 * @return
	 * 				加密后数据
	 */
	public static String shaHex(String plain) {
		return DigestUtils.shaHex(plain);
	}
	
	private static final String key = "Keytec@GnetIBD_CP";

	// 解密数据
	public static String decrypt(String message) throws Exception {

		byte[] bytesrc = convertHexString(message);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec((Md5(key).substring(0,8).toUpperCase()).getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec((Md5(key).substring(0,8).toUpperCase()).getBytes("UTF-8"));

		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

		byte[] retByte = cipher.doFinal(bytesrc);
		
		String str = java.net.URLDecoder.decode(new String(retByte), "utf-8");
		
		return str;
	}
	private static byte[] convertHexString(String ss) {
		byte digest[] = new byte[ss.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}

		return digest;
	}
	
	public static String DESEncrypt(String message) throws Exception {
		
		String md5Str = java.net.URLEncoder.encode(message.toLowerCase(), "utf-8");
		
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

		DESKeySpec desKeySpec = new DESKeySpec((Md5(key).substring(0,8).toUpperCase()).getBytes("UTF-8"));

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec((Md5(key).substring(0,8).toUpperCase()).getBytes("UTF-8"));
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
		
		byte[] bt = cipher.doFinal(md5Str.getBytes("UTF-8"));
		
		return toHexString(bt).toUpperCase();
	}

	public static String toHexString(byte b[]) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String plainText = Integer.toHexString(0xff & b[i]);
			if (plainText.length() < 2)
				plainText = "0" + plainText;
			hexString.append(plainText);
		}

		return hexString.toString();
	}
	
	
	public static void main(String[] args) throws Exception {		
		System.out.println("加密前的数据为: "+Md5("123456"));
		String a = DESEncrypt(Md5("123456"));
		System.out.println("加密后的数据为:" + a);
		String b = decrypt(a);
		System.out.println("解密后的数据:" + b);

	}
}
