package com.example.framwork.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.framwork.noHttp.CallServer;
import com.example.framwork.ricyclerview.DividerGridItemDecoration;
import com.example.framwork.utils.ProgressActivityUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.greenrobot.eventbus.EventBus;


public abstract class BaseCommonFragment extends Fragment implements View.OnClickListener {
    private int layoutResID;
    protected Activity context = null;
    protected View rootView = null;
    protected ProgressActivityUtils progressActivityUtils;
    protected KProgressHUD progressHUD;
    protected LinearLayoutManager linearLayoutManager;
    protected GridLayoutManager gridLayoutManager;

    /**
     * 此方法可以得到上下文对象
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        rootView = inflater.inflate(initLayoutResID(), container, false);
        initButterKnife(rootView);
        initData();
        initView();
        return rootView;
    }

    protected abstract void initData();

    protected abstract void initView();


    protected void initButterKnife(View view) {
    }

    /**
     * 初始化layout，此方法里只能写layoutResID = R.layout.xxx
     */
    public int initLayoutResID() {
        return layoutResID;
    }

    /**
     * 设置点击事件
     */
    protected void setClickListener() {
    }

    /**
     * 点击事件
     */
    protected void onClickListener(View v) {
    }

    @Override
    public void onClick(View v) {
        onClickListener(v);
    }


    protected void showProgress() {
        if (progressHUD == null) {
            progressHUD = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        }
        progressHUD.show();
    }

    protected void showProgress(String hint, Boolean isCancel) {
        if (progressHUD == null)
            progressHUD = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(hint).setCancellable(isCancel).setAnimationSpeed(2).setDimAmount(0.5f);
        progressHUD.show();
    }

    protected void hideProgress() {
        if (progressHUD != null && progressHUD.isShowing())
            progressHUD.dismiss();
    }

    // 初始化SwipeLayout
    protected void initSwipeLayout(SwipeRefreshLayout mSwipeLayout, SwipeRefreshLayout.OnRefreshListener refreshListener) {
        mSwipeLayout.setOnRefreshListener(refreshListener);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
//        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright);
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
     * 隐藏软键盘
     */
    protected void hideSoftInput(EditText view) {
        InputMethodManager mInputMethodManager =
                (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        mInputMethodManager
                .hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 打开软键盘
     */
    protected void showSoftInput(EditText view) {
        InputMethodManager mInputMethodManager =
                (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        mInputMethodManager
                .showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    @Override
    public void onDestroyView() {

        if (isUseEventBus() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyView();
    }

    @Override
    public void onAttach(Activity activity) {
        if (isUseEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        if (context != null) {
            CallServer.getRequestInstance().cancelBySign(context);
        }
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
}
