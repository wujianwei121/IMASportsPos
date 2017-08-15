package com.imasports.pos;

import com.example.framwork.BaseApplictaion;
import com.yanzhenjie.nohttp.Logger;

/**
 * Created by lenovo on 2017/8/9.
 */

public class IMAApplication extends BaseApplictaion{
    @Override
    public void onCreate() {
        super.onCreate();
        // 开始NoHttp的调试模式, 这样就能看到请求过程和日志
        Logger.setTag("IMA");
        Logger.setDebug(BuildConfig.DEBUG);
    }
}
