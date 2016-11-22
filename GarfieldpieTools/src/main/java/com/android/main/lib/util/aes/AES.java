package com.android.main.lib.util.aes;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	/*
	 * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
	 * 此处使用AES-128-CBC加密模式，key需要为16位。
	 */
	private static final String SKEY = "Pkkj@~_^&&123_78";

	// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
	private static final String IVSTR = "0201080306050704";

	/**
	 * 加密
	 * 
	 * @param sSrc
	 * @return
	 */
	public static String Encrypt(String sSrc) {
		try {
			if (sSrc == null || sSrc.length() < 2) {
				return null;
			}
			if (SKEY == null) {
				System.out.print("Key为空null");
				return null;
			}
			// 判断Key是否为16位
			if (SKEY.length() != 16) {
				System.out.print("Key长度不是16位");
				return null;
			}
			byte[] raw = SKEY.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
			IvParameterSpec iv = new IvParameterSpec(IVSTR.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(sSrc.getBytes());

			return ObjectSerializer.encodeBytes(encrypted);
		} catch (Exception ex) {
			return null;
		}

	}

	/**
	 * 解密
	 * 
	 * @param sSrc
	 * @return
	 */
	public static String Decrypt(String sSrc) {
		try {
			// 判断Key是否正确
			if (sSrc == null || sSrc.length() < 16) {
				return null;
			}
			if (SKEY == null) {
				System.out.print("Key为空null");
				return null;
			}
			// 判断Key是否为16位
			if (SKEY.length() != 16) {
				System.out.print("Key长度不是16位");
				return null;
			}
			byte[] raw = SKEY.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(IVSTR.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

			byte[] encrypted1 = ObjectSerializer.decodeBytes(sSrc);
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original);
				return originalString;
			} catch (Exception e) {
				return null;
			}
		} catch (Exception ex) {
			return null;
		}
	}

	public static void main(String[] s) {
		long timestamp = System.currentTimeMillis();

		String TAG = "mobile";

		String cSrc = "1|1|25845"; // 待加密的字符串 TAG + + "|" + timestamp;

		// 加密
		long lStart = System.currentTimeMillis();
		String enString = "";
		try {
			enString = AES.Encrypt(cSrc); // 对字符串进行加密
			System.out.println("enString:" + enString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long lUseTime = System.currentTimeMillis() - lStart;

		// 解密
		lStart = System.currentTimeMillis();
		String DeString = "";
		try {
			DeString = AES.Decrypt(enString); // 对加密串进行解密
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lUseTime = System.currentTimeMillis() - lStart;

	}

}
