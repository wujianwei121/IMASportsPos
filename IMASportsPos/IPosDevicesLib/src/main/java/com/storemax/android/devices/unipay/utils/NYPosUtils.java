package com.storemax.android.devices.unipay.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class NYPosUtils {
	public static final String UTF8 = "UTF-8";

	/**
	 * HMAC加密 数据
	 * 
	 * @param data
	 *            utf-8
	 * @param secret
	 *            utf-8
	 * @return
	 * @throws Exception
	 */
	public static String encryptHMAC(String data, String secret) throws Exception {

		return encryptBASE64(encryptHMAC(data.getBytes(UTF8), secret.getBytes(UTF8)));
	}

	/**
	 * HMAC加密 数据
	 * 
	 * @param data
	 * @param secret
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptHMAC(byte[] data, byte[] secret) throws Exception {
		byte[] bytes = null;
		SecretKey secretKey = new SecretKeySpec(secret, "HmacMD5");
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		bytes = mac.doFinal(data);
		return bytes;
	}

	/**
	 * base64加密
	 * 
	 * @param data
	 * @return
	 */
	public static String encryptBASE64(byte[] data) {
		return Base64.encodeToString(data, Base64.NO_WRAP);
	}

	/**
	 * 整数金额转
	 */
	public static String consueMoney(int money) {
		DecimalFormat df = new DecimalFormat("0.00");
		return String.valueOf(df.format(((double) money) / 100));
	}

	/**
	 * ������
	 * 
	 * @param csn
	 * @return
	 */
	public static String getOrderId() {
		return getCurrentTime() + getRandom(8);
	}

	/**
	 * �����
	 * 
	 * @param n
	 * @return
	 */
	public static String getRandom(int length) {
		String base = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static String getCurrentTime() {
		Date currentTime = new Date();
		SimpleDateFormat formater = new SimpleDateFormat("yyMMddHHmmss");
		return formater.format(currentTime);
	}
}
