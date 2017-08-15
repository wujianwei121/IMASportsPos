package com.example.framwork.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.framwork.R;
import com.example.framwork.noHttp.BeanJsonResult;
import com.example.framwork.widget.ProgressActivity;


/**
 * Created by ${wjw} on 2016/4/15.
 */
public class ProgressActivityUtils {
    private Context context;
    private ProgressActivity progressActivity;


    public ProgressActivityUtils(Context context, ProgressActivity progressActivity) {
        this.context = context;
        this.progressActivity = progressActivity;
    }

    /**
     * 显示内容
     */
    public void showContent() {
        if (progressActivity != null) {
            progressActivity.showContent();
        }
    }

    /**
     * 显示提示用户登录
     *
     * @param clickListener 登录点击回调
     */
    public void showNoLogin(View.OnClickListener clickListener) {
        if (progressActivity != null) {
            progressActivity.showError(null, "当前用户未登录", "", "请登录", clickListener);
        }
    }

    public void showNoWifiError(String error, View.OnClickListener clickListener) {
        if (progressActivity != null) {
            progressActivity.showError(ContextCompat.getDrawable(context, R.drawable.no_wifi), error, "", "重试", clickListener);
        }
    }

    public void showiError(String error, View.OnClickListener clickListener) {
        if (progressActivity != null) {
            progressActivity.showError(ContextCompat.getDrawable(context, R.drawable.no_info), error, "", "重试", clickListener);
        }
    }

    public void showNoWifiError(int src, String error, View.OnClickListener clickListener) {
        if (progressActivity != null) {
            progressActivity.showError(ContextCompat.getDrawable(context, src), error, "", "重试", clickListener);
        }
    }

    public void showiError(int src, String error, View.OnClickListener clickListener) {
        if (progressActivity != null) {
            progressActivity.showError(ContextCompat.getDrawable(context, src), error, "", "重试", clickListener);
        }
    }

    public void showEmptry(int src, String title) {
        if (progressActivity != null) {
            progressActivity.showEmpty(ContextCompat.getDrawable(context, src), title, "");
        }
    }

    public void showEmptry(String title) {
        if (progressActivity != null) {
            progressActivity.showEmpty(ContextCompat.getDrawable(context, R.drawable.no_info), title, "");
        }
    }

    public void showLoading() {
        if (progressActivity != null) {
            progressActivity.showLoading();
        }
    }
}
