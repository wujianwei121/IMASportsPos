package com.imasports.pos.base;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.example.framwork.adapter.AnimationType;
import com.example.framwork.adapter.SuperBaseAdapter;
import com.example.framwork.base.BaseActivity;
import com.example.framwork.ricyclerview.DividerGridItemDecoration;
import com.example.framwork.widget.superrecyclerview.recycleview.ProgressStyle;
import com.example.framwork.widget.superrecyclerview.recycleview.SuperRecyclerView;

import butterknife.ButterKnife;

/**
 * Created by wanjingyu on 2016/10/9.
 */

public abstract class BaseNoTitleActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void initButterKnife() {
        ButterKnife.bind(this);
    }

    protected abstract void initView();

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
