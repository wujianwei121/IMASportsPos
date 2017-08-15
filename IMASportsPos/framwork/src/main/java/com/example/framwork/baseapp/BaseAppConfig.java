package com.example.framwork.baseapp;

import android.content.Context;

import com.example.framwork.utils.CommonUtil;
import com.example.framwork.utils.FileUtil;

import java.io.File;

/**
 * Created by lenovo on 2017/2/22.
 */

public class BaseAppConfig {
    private volatile static BaseAppConfig instance;
    /**
     * App根目录.
     */
    public static String APP_PATH_ROOT;
    /**
     * 下载文件目录
     */
    public static String DOWNLOAD_PATH;
    public static String IMAGE_PATH;
    public static String VERSION_NUM;//版本号
    public static String CACHE_PATH;
    public static boolean getCachePath = false;
    public static String SERVICE_PATH = " ";

    public static void init(Context context, String fileName) {
        APP_PATH_ROOT = FileUtil.getInstance().getRootPath().getAbsolutePath() + File.separator + fileName;
        DOWNLOAD_PATH = APP_PATH_ROOT + File.separator + "downLoad";
        CACHE_PATH = APP_PATH_ROOT + File.separator + "cache";
        IMAGE_PATH = APP_PATH_ROOT + File.separator + "image";
        VERSION_NUM = CommonUtil.getVersion(context);
        initFile();
    }

    public static void initFile() {
        FileUtil.getInstance().initDirectory(APP_PATH_ROOT);
        getCachePath = FileUtil.getInstance().initDirectory(CACHE_PATH);
        FileUtil.getInstance().initDirectory(DOWNLOAD_PATH);
        FileUtil.getInstance().initDirectory(IMAGE_PATH);
    }

}
