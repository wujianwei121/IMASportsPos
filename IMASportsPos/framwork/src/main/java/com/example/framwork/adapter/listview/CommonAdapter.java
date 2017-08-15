package com.example.framwork.adapter.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.example.framwork.adapter.BaseViewHolder;

import java.util.List;


/**
 * ListView 通用的adapter
 *
 * @author hongyang
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private int layoutId;

    public CommonAdapter(Context context, int layoutId, List<T> datas) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        if (mDatas != null && mDatas.size() != 0) {
            return mDatas.size();
        } else {
            return 0;
        }
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder = BaseViewHolder.get(mContext, convertView, parent,
                layoutId, position);
        convert(holder, getItem(position), position);
        return holder.getConvertView();
    }

    public abstract void convert(BaseViewHolder holder, T t, int p);

}
