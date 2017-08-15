package com.imasports.pos.common;

/**
 * Created by lenovo on 2017/8/9.
 */

public class FusionConstant {
    /**
     * 扫描超时时间 20秒，设置为0时，不超时
     */
    public static final int SCAN_TIME_OUT = 20;
    /**
     * 连接超时 1000*30 30秒
     */
    public static final int HTTP_CONNECT_TIMEOUT = 60000;

    /**
     * 服务停掉或网络断开标识
     */
    public static final int INTER_ERROR = 0x9000;
    /**
     * 鉴权失败状态码
     */
    public static final int OAUTH_FAILURE = 401;

    /**
     * 判断DEVICE_TYPE机型是否匹配
     *
     * @param deviceType 需要验证的机型
     * @return
     */
    public static boolean isDeviceMatch(String deviceType) {
        String versionTag = Constants.DEVICE_TYPE;
        if (deviceType.equalsIgnoreCase(versionTag)) {
            return true;
        }
        return false;
    }

}
