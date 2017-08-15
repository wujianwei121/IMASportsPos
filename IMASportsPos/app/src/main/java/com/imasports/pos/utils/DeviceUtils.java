package com.imasports.pos.utils;

import android.content.Context;
import android.text.TextUtils;

import com.imasports.pos.common.FusionConstant;
import com.imasports.pos.common.FusionUnitType;
import com.imasports.pos.common.SystemParams;
import com.storemax.android.devices.DeviceFactory;

/**
 * Created by lenovo on 2017/8/9.
 */

public class DeviceUtils {
    /**
     * 获取设备ID
     *
     * @return
     * @Title: getDeviceID
     */
    public static String getDeviceID(Context context) {
        String mDeviceID = SystemParams.getInstance().getDeviceid(context);//先从本地获取
        if (TextUtils.isEmpty(mDeviceID)) {
            try {
                if (FusionConstant.isDeviceMatch(FusionUnitType.DeviceType.DEVICETYPE_UNIPAY_A82)
                        || FusionConstant.isDeviceMatch(FusionUnitType.DeviceType.DEVICETYPE_UNIPAY_A83)) {//银联a8、沃银A8、汇宜A8
                    // : 16/12/15 银联a8 deviceID()
                    mDeviceID = DeviceFactory.getDeviceId();
                } else {//暂时处理
                    mDeviceID = DeviceInfoUtil.getDeviceUniqueID(context);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            SystemParams.getInstance().saveDeviceId(context, mDeviceID);
        }
        return mDeviceID;
    }
}
