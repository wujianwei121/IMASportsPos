package com.example.framwork.base;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;

import com.example.framwork.R;
import com.example.framwork.baseapp.AppManager;
import com.example.framwork.noHttp.CallServer;
import com.example.framwork.statusbar.StatusBarUtil;
import com.example.framwork.utils.ProgressActivityUtils;
import com.example.framwork.utils.Toast;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;


public abstract class BaseActivity extends AppCompatActivity implements OnClickListener {
    protected int layoutResID;
    protected Activity context;
    protected KProgressHUD progressHUD;
    protected ProgressActivityUtils progressActivityUtils;
    private String imgurl = "";
    protected LinearLayoutManager linearLayoutManager;
    protected GridLayoutManager gridLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        if (isUseEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        context = this;
        if (getContentLayout() != 0) {
            setContentView(getContentLayout());
        }
        initButterKnife();
        AppManager.getAppManager().addActivity(this);
        initData();
    }

    public void hideStatusBar() {

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }


    protected void setStatusBar() {
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
     * 设置点击事件
     */
    protected void setClickListener() {
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput(EditText view) {
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        mInputMethodManager
                .hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 打开软键盘
     */
    protected void showSoftInput(EditText view) {
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        mInputMethodManager
                .showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 获取布局文件
     *
     * @return 布局文件ID
     */
    protected abstract int getContentLayout();

    /**
     * 按需重写   如果添加头部无需重写
     */
    protected void initButterKnife() {
    }


    protected abstract void initData();


    /**
     * 点击事件
     */
    public abstract void onClickListener(View v);

    protected void showProgress() {
        if (progressHUD == null)
            progressHUD = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        progressHUD.show();
    }

    protected void showProgress(String hint, Boolean isCancel) {
        if (progressHUD == null)
            progressHUD = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(hint).setCancellable(isCancel).setAnimationSpeed(2).setDimAmount(0.5f);
        progressHUD.show();
    }


    protected void hideProgress() {
        if (progressHUD != null) {
            progressHUD.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        onClickListener(v);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        CallServer.getRequestInstance().cancelBySign(context);
        if (isUseEventBus() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    /**
     * 是否使用 EventBus
     *
     * @return
     */
    protected boolean isUseEventBus() {
        return false;
    }


    /***
     * 功能：长按图片保存到手机
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuItem.OnMenuItemClickListener handler = new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle() == "保存图片到手机") {
                    new SaveImage().execute(); // Android 4.0以后要使用线程来访问网络
                } else {
                    return false;
                }
                return true;
            }
        };
        if (v instanceof WebView) {
            WebView.HitTestResult result = ((WebView) v).getHitTestResult();
            if (result != null) {
                int type = result.getType();
                if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    imgurl = result.getExtra();
                    menu.setHeaderTitle("提示");
                    menu.add(0, v.getId(), 0, "保存图片到手机").setOnMenuItemClickListener(handler);
                    menu.add(0, v.getId(), 0, "查看大图").setOnMenuItemClickListener(handler);
                }
            }
        }
    }

    /***
     * 功能：用线程保存图片
     *
     * @author wangyp
     */
    private class SaveImage extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                String sdcard = Environment.getExternalStorageDirectory().toString();
                File file = new File(sdcard + "/Pictures");
                if (!file.exists()) {
                    file.mkdirs();
                }
                int idx = imgurl.lastIndexOf(".");
                String ext = imgurl.substring(idx);
                file = new File(sdcard + "/Pictures/" + new Date().getTime() + ext);
                InputStream inputStream = null;
                URL url = new URL(imgurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(20000);
                if (conn.getResponseCode() == 200) {
                    inputStream = conn.getInputStream();
                }
                byte[] buffer = new byte[4096];
                int len = 0;
                FileOutputStream outStream = new FileOutputStream(file);
                while ((len = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                outStream.close();
                result = "图片已保存至：" + file.getAbsolutePath();
                //更新系统相册
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                sendBroadcast(intent);
            } catch (Exception e) {
                result = "保存失败！" + e.getLocalizedMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.getInstance().showDefaultToast(context, result);
        }
    }
}

