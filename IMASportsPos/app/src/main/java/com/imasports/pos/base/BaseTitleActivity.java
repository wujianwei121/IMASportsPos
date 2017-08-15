package com.imasports.pos.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.framwork.adapter.AnimationType;
import com.example.framwork.adapter.SuperBaseAdapter;
import com.example.framwork.base.BaseActivity;
import com.example.framwork.ricyclerview.DividerGridItemDecoration;
import com.example.framwork.widget.customtoolbar.CommonTitle;
import com.example.framwork.widget.superrecyclerview.recycleview.ProgressStyle;
import com.example.framwork.widget.superrecyclerview.recycleview.SuperRecyclerView;
import com.imasports.pos.R;

import butterknife.ButterKnife;

/**
 * Created by wanjingyu on 2016/10/9.
 */

public abstract class BaseTitleActivity extends BaseActivity {
    protected CommonTitle actionBar;
    private LinearLayout contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_title);
        initParentView();
        setCommonTitle();
        initView();
    }


    protected abstract void initView();

    private void initParentView() {
        LayoutInflater inflater = getLayoutInflater();
        contentLayout = (LinearLayout) findViewById(R.id.content_layout);
        actionBar = (CommonTitle) findViewById(R.id.action_bar);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentLayout.addView(inflater.inflate(getContentLayout(), null), params);
        actionBar.getLeftRes().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ButterKnife.bind(this);
    }

    public View getContentView() {
        return contentLayout;
    }

    protected abstract void setCommonTitle();


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initData();
    }

    protected void initRecyclerView(SuperRecyclerView recyclerView, SuperBaseAdapter adapter, SuperRecyclerView.LoadingListener loadingListener) {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setRefreshEnabled(false);
        recyclerView.setLoadMoreEnabled(true);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);//上拉加载的样式
        recyclerView.setLoadingListener(loadingListener);
        adapter.setItemAnimation(AnimationType.SLIDE_FROM_LEFT);//设置显示的动画
        adapter.setShowItemAnimationEveryTime(false);//是否每次都会执行动画,默认是false,该方便测试
        recyclerView.setAdapter(adapter);
    }

    public void initRecyclerViewGrid(SuperRecyclerView recyclerView, SuperBaseAdapter adapter, SuperRecyclerView.LoadingListener loadingListener, int columns, int span) {
        gridLayoutManager = new GridLayoutManager(context, columns);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setRefreshEnabled(false);
        recyclerView.setLoadMoreEnabled(true);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);//上拉加载的样式
        recyclerView.setLoadingListener(loadingListener);
        adapter.setItemAnimation(AnimationType.SLIDE_FROM_LEFT);//设置显示的动画
        adapter.setShowItemAnimationEveryTime(false);//是否每次都会执行动画,默认是false,该方便测试
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(context, span));
    }

}
