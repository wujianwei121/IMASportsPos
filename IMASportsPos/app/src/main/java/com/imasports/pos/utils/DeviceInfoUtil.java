package com.imasports.pos.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.yanzhenjie.nohttp.Logger;

/**
 * Created by lenovo on 2017/8/9.
 */

public class DeviceInfoUtil {
    /**
     * 标记
     */
    private static final String TAG = DeviceInfoUtil.class.getSimpleName();

    /**
     * 获取设备唯一编码
     *
     * @param context
     * @return
     * @throws Exception
     */
    public synchronized static String getDeviceUniqueID(Context context)
            throws Exception {
        String res = null;
        if (context != null) {
            try {
                res = getDeviceID(context);
            } catch (Exception ex) {
                Logger.e(ex.getMessage());
            }
            if (TextUtils.isEmpty(res)) {
                try {
                    res = getMacAddress(context);
                } catch (Exception ex) {
                    Logger.e(ex.getMessage());
                }
            }
            if (TextUtils.isEmpty(res)) {
                try {
                    res = getSerialNumber(context);
                } catch (Exception ex) {
                    Logger.e(ex.getMessage());
                }
            }
            if (TextUtils.isEmpty(res)) {
                try {
                    res = getAndroidID(context);
                } catch (Exception ex) {
                    Logger.e(ex.getMessage());
                }
            }
            if (TextUtils.isEmpty(res)) {
                try {
                    res = getIMSI(context);
                } catch (Exception ex) {
                    Logger.e(ex.getMessage());
                }
            }

        }
        return res;
    }

    /**
     * 获取设备ID
     *
     * @param context
     * @return
     * @throws Exception
     */
    public synchronized static String getDeviceID(Context context)
            throws Exception {
        try {
            TelephonyManager telephonyManager;
            telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getDeviceId();
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 获取设备IMSI
     *
     * @param context
     * @return
     * @throws Exception
     */
    public synchronized static String getIMSI(Context context) throws Exception {
        try {
            TelephonyManager telephonyManager;
            telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getSubscriberId();
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 获取WIFI地址
     *
     * @param context
     * @return
     * @throws Exception
     */
    public synchronized static String getMacAddress(Context context)
            throws Exception {
        try {
            WifiManager wifi = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            return info.getMacAddress();
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 获取AndroidID
     *
     * @param context
     * @return
     * @throws Exception
     */
    public synchronized static String getAndroidID(Context context)
            throws Exception {
        try {
            return Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 获取设备序列号
     *
     * @param context
     * @return
     * @throws Exception
     */
    public synchronized static String getSerialNumber(Context context)
            throws Exception {
        try {
            return android.os.Build.SERIAL;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
