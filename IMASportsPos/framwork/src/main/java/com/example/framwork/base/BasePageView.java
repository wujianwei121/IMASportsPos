package com.example.framwork.base;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;

import com.example.framwork.adapter.AnimationType;
import com.example.framwork.adapter.SuperBaseAdapter;
import com.example.framwork.baseapp.AppManager;
import com.example.framwork.noHttp.CallServer;
import com.example.framwork.utils.ProgressActivityUtils;
import com.example.framwork.utils.Toast;
import com.example.framwork.widget.superrecyclerview.recycleview.ProgressStyle;
import com.example.framwork.widget.superrecyclerview.recycleview.SuperRecyclerView;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;


public abstract class BasePageView {
    protected Activity activity;
    protected KProgressHUD progressHUD;
    protected ProgressActivityUtils progressActivityUtils;
    protected LinearLayoutManager linearLayoutManager;
    protected GridLayoutManager gridLayoutManager;

    public BasePageView(Activity activity) {
        this.activity = activity;
    }

    // 初始化SwipeLayout
    protected void initSwipeLayout(SwipeRefreshLayout mSwipeLayout, SwipeRefreshLayout.OnRefreshListener refreshListener) {
        mSwipeLayout.setOnRefreshListener(refreshListener);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
//        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);
//        mSwipeLayout.setDistanceToTriggerSync(400);// 设置手指在屏幕下拉多少距离会触发下拉刷新
//        mSwipeLayout.setProgressBackgroundColorSchemeResource(android.R.color.white); // 设定下拉圆圈的背景
//        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE); // 设置圆圈的大小
    }

    protected void initRecyclerView(SuperRecyclerView recyclerView, SuperBaseAdapter adapter, SuperRecyclerView.LoadingListener loadingListener) {
        linearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setRefreshEnabled(false);
        recyclerView.setLoadMoreEnabled(true);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);//上拉加载的样式
        recyclerView.setLoadingListener(loadingListener);
        adapter.setItemAnimation(AnimationType.SLIDE_FROM_LEFT);//设置显示的动画
        adapter.setShowItemAnimationEveryTime(false);//是否每次都会执行动画,默认是false,该方便测试
        recyclerView.setAdapter(adapter);
    }

    protected void showSwipeRefreshLayout(final SwipeRefreshLayout swipeRefreshLayout) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(new Runnable() {

                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    protected void hideSwipeRefreshLayout(final SwipeRefreshLayout swipeRefreshLayout) {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.post(new Runnable() {

                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }


    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput(EditText view) {
        InputMethodManager mInputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        mInputMethodManager
                .hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 打开软键盘
     */
    protected void showSoftInput(EditText view) {
        InputMethodManager mInputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        mInputMethodManager
                .showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }


    protected void showProgress() {
        if (progressHUD == null)
            progressHUD = KProgressHUD.create(activity).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        progressHUD.show();
    }

    protected void showProgress(String hint, Boolean isCancel) {
        if (progressHUD == null)
            progressHUD = KProgressHUD.create(activity).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(hint).setCancellable(isCancel).setAnimationSpeed(2).setDimAmount(0.5f);
        progressHUD.show();
    }


    protected void hideProgress() {
        if (progressHUD != null) {
            progressHUD.dismiss();
        }
    }


    /**
     * 是否使用 EventBus
     *
     * @return
     */
    protected boolean isUseEventBus() {
        return false;
    }


}

