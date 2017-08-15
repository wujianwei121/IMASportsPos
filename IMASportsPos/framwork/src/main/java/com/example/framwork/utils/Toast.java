package com.example.framwork.utils;

import android.content.Context;
import android.text.TextUtils;

import com.sdsmdg.tastytoast.TastyToast;

/**
 * Created by mc on 16/12/16.
 */

public class Toast {
    /**
     * 内部类实现单例模式
     * 延迟加载，减少内存开销
     *
     * @author xuzhaohu
     */
    private static class SingletonHolder {
        private static Toast instance = new Toast();
    }

    /**
     * 私有的构造函数
     */
    private Toast() {

    }

    public static Toast getInstance() {
        return Toast.SingletonHolder.instance;
    }

    public void showSuccessToast(Context context, String message) {
        if (context == null || TextUtils.isEmpty(message)) {
            return;
        }
        TastyToast.makeText(context, message, TastyToast.LENGTH_SHORT,
                TastyToast.SUCCESS);
    }

    public void showWarningToast(Context context, String message) {
        TastyToast.makeText(context, message, TastyToast.LENGTH_SHORT,
                TastyToast.WARNING);
    }

    public void showErrorToast(Context context, String message) {
        TastyToast.makeText(context, message, TastyToast.LENGTH_SHORT,
                TastyToast.ERROR);
    }

    public void showInfoToast(Context context, String message) {
        TastyToast.makeText(context, message, TastyToast.LENGTH_SHORT,
                TastyToast.INFO);
    }

    public void showDefaultToast(Context context, String message) {
        TastyToast.makeText(context, message, TastyToast.LENGTH_SHORT,
                TastyToast.DEFAULT);
    }


    public void showConfusingToast(Context context, String message) {
        TastyToast.makeText(context, message, TastyToast.LENGTH_SHORT,
                TastyToast.CONFUSING);
    }

}
