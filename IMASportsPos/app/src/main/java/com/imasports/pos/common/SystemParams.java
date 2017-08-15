package com.imasports.pos.common;

import android.content.Context;

import com.imasports.pos.utils.SharedPrefControl;

/**
 * Created by lenovo on 2017/8/9.
 */

public class SystemParams {
    private static SystemParams _instance = new SystemParams();
    private static final String Tag = SystemParams.class.getSimpleName();

    // vip会员登录方式
    private static final String VIP_LOGIN_TYPE = "vip_login_type";
    /**
     * 设备号
     */
    private static final String DEVICEID = "device_data";


    /**
     * 射频卡(带磁条)
     */
    public static final int RF_CARD = 0xF001;

    /**
     * 磁条卡
     */
    public static final int MAG_CARD = 0xF002;

    /**
     * 仅射频卡(无磁条)
     */
    public static final int RF_ONLY_CARD = 0xF003;

    /**
     * 不发卡
     */
    public static final int NO_PROVIDE_CARD = 0xF004;

    private SystemParams() {
    }

    public static SystemParams getInstance() {
        if (_instance == null) {
            _instance = new SystemParams();
        }
        return _instance;
    }

    /**
     * 保存设备号到本地
     *
     * @param context
     * @param deviceId
     */
    public void saveDeviceId(Context context, String deviceId) {
        SharedPrefControl sp = new SharedPrefControl(context);
        sp.setSPValue(Tag, DEVICEID, deviceId);
    }

    /**
     * 从本地获取设备号
     *
     * @param context
     * @return
     */
    public String getDeviceid(Context context) {
        SharedPrefControl sp = new SharedPrefControl(context);
        return sp.getSPValue(Tag, DEVICEID, "");
    }

    /**
     * 会员卡默认类型
     */
    public void setVipLoginType(Context cnt, int value) {
        SharedPrefControl sp = new SharedPrefControl(cnt);
        sp.setSPValue(Tag, VIP_LOGIN_TYPE, value);
    }

    public int getVipLoginType(Context cnt) {
        SharedPrefControl sp = new SharedPrefControl(cnt);
        return sp.getSPValue(Tag, VIP_LOGIN_TYPE, MAG_CARD);
    }


}
