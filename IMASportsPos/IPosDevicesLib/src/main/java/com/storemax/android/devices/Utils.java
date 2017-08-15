package com.storemax.android.devices;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.storemax.framework.device.R;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Utils {
	/**
	 * 配置缓存
	 */
	private static Map<String, String> mPropertiesCache;

	/**
	 * 获取配置文件 <BR>
	 * 配置初始化时会缓存在内存中，若内存中不存在则到配置文件中获取 [简要描述其作用]<BR>
	 * [详细描述其作用]<BR>
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public synchronized static String getProperties(Context context, String key) {
		if (mPropertiesCache == null) {
			mPropertiesCache = new HashMap<String, String>();
		}
		String res = mPropertiesCache.get(key);
		if (TextUtils.isEmpty(res)) {
			res = getPropertiesFromFile(context, key);
		}
		return res;
	}

	private static String getPropertiesFromFile(Context context, String key) {
		String res = null;
		Properties properties = new Properties();
		try {
			properties.load(context.getResources().openRawResource(R.raw.config));
			res = properties.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public static String getSPValue(Context context, String paramString) {
		SharedPreferences sp = context.getSharedPreferences(DeviceFactory.SP_TAG, 0);
		if (sp != null) {
			return sp.getString(paramString, "");
		}
		return "";
	}

	/**
	 * [设置SharedPreferences 值]
	 * 
	 * @Title: setSPValue
	 * @param paramString
	 * @param tag
	 * @param value
	 * @return
	 */
	public static boolean setSPValue(Context context, String tag, String paramString, String value) {
		SharedPreferences sp = context.getSharedPreferences(tag, 0);
		if (sp != null) {
			SharedPreferences.Editor editor = sp.edit();
			editor.putString(paramString, value);
			return editor.commit();
		}
		return false;
	}

	public static boolean isAlipayCode(String code) {
		return true;
	}

	public static boolean isWechatCode(String code) {
		return true;
	}
	
	public static String formatMoney(double value){
		DecimalFormat df = new DecimalFormat("0.00");
		return String.valueOf(df.format(value));
	}
}
