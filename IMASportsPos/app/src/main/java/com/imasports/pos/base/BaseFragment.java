package com.imasports.pos.base;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.framwork.adapter.AnimationType;
import com.example.framwork.adapter.SuperBaseAdapter;
import com.example.framwork.base.BaseCommonFragment;
import com.example.framwork.ricyclerview.DividerGridItemDecoration;
import com.example.framwork.widget.superrecyclerview.recycleview.ProgressStyle;
import com.example.framwork.widget.superrecyclerview.recycleview.SuperRecyclerView;

import butterknife.ButterKnife;

/**
 * Created by lenovo on 2017/2/22.
 */

public abstract class BaseFragment extends BaseCommonFragment {
    @Override
    public void initButterKnife(View view) {
        ButterKnife.bind(this, view);
    }


    protected void initRecyclerView(SuperRecyclerView recyclerView, SuperBaseAdapter adapter, SuperRecyclerView.LoadingListener loadingListener) {
        linearLayoutManager = new LinearLayoutManager(context);
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
