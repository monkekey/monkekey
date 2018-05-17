package com.yumi.butler.utils;


import com.yumi.netty.tools.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DESHelper {
	/**
	 * 加密函数
	 * 
	 * @param message 加密数据
	 * @return 返回加密后的数据
	 */
	public static String encrypt(String message, String key) {
		try {
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			return toHexString(cipher.doFinal(message.getBytes("UTF-8")));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	/**
	 * 解密函数
	 * 
	 * @param message 解密数据
	 * @return 返回解密后的数据
	 */
	public static String decrypt(String message, String key) {

		try {
			byte[] bytesrc = convertHexString(message);
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
			byte[] retByte = cipher.doFinal(bytesrc);
			return new String(retByte);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	public static byte[] convertHexString(String ss) {
		byte digest[] = new byte[ss.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}

		return digest;
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
	
	public static String encrypt(String ss) {
		return encrypt(ss, "sixiangjia".substring(0, 8));
	}
	
	public static String decrypt(String ss) {
		return decrypt(ss, "sixiangjia".substring(0, 8));
	}

	public static void main(String[] args) throws Exception {

//		System.out.println(decrypt("4caa423df9fc3016b7c6940f2f8e84b3"));
//		System.out.println(decrypt("b54ff13f3a3e91fd"));
//		System.out.println(encrypt("lxb"));
//		System.out.println(decrypt("772593b04e4950e78f810050a3f7056b"));
		
//		String sourceStr = pwd;
//		System.out.println("sourceStr  is:" + sourceStr);
//		String encryptStr = encrypt("123456");
//		System.out.println("encryptStr is:" + encryptStr);
//		String decryptStr = decrypt("a838ad6c468184b4c94474d81d981a8e", user.concat("sixiangjia").substring(0, 8));
//		System.out.println("decryptStr is:" + decryptStr);
//		System.out.print(DateUtils.parseDate("2018-02-01", "yyyy-MM-dd"));

//		BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
//		System.out.println(encoder.encode("lf13579"));

		String appid = "1269319501";
		String secret = "herechina20160423190813826005801";
		String encryptStr = encrypt(secret, StringUtils.sortByChart(appid.concat("wechat_mch")).substring(0, 8));
		System.out.println(encryptStr);
//		String decryptStr = decrypt(encryptStr, StringUtils.sortByChart(appid.concat("lavandeinn")).substring(0, 8));
//		System.out.println(decryptStr);
//		System.out.println(decryptStr.equals(secret));
//
//		System.out.println(decrypt("687b40c19925f4aa01afaede0d09d6822a0af88a0db0a6da0a3eb19ec1fd11c280288602b152f214", StringUtils.sortByChart(appid.concat("lavandeinn")).substring(0, 8)));

//        System.out.println("1231231".lastIndexOf("."));
	}
}
