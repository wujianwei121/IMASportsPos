package com.imasports.pos.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/8/9.
 */

public class AppUtil {
    /**
     * 创建签名
     *
     * @param parameters
     * @param partnerkey
     * @return
     */
    public static String createSign(Map<String, String> parameters, String partnerkey) {
        StringBuilder sb = new StringBuilder();
        List akeys = new ArrayList(parameters.keySet());
        //ASCII码从小到大排序
        Collections.sort(akeys);
        //拼接成字符串stringA
        for (Object k : akeys) {
            String v = parameters.get(k);
            // 如果参数的值为空不参与签名
            if (null != v && !TextUtils.isEmpty(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        //在stringA最后拼接上key得到stringSignTemp字符串
        sb.append("key=" + partnerkey);
        // MD5运算，再将得到的字符串所有字符转换为大写
        return MD5.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
    }
}
