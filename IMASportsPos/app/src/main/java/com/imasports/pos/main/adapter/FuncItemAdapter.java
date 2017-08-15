package com.imasports.pos.main.adapter;

import android.content.Context;
import android.widget.ImageView;


import com.example.framwork.adapter.BaseViewHolder;
import com.example.framwork.adapter.listview.CommonAdapter;
import com.imasports.pos.R;
import com.imasports.pos.main.MainActivityBiz;
import com.imasports.pos.main.model.bean.FuncItemBean;

import java.util.List;

public class FuncItemAdapter extends CommonAdapter<FuncItemBean> {
    private MainActivityBiz mainActivityBiz;

    public FuncItemAdapter(Context context, int layoutId, List<FuncItemBean> datas) {
        super(context, layoutId, datas);
        mainActivityBiz = new MainActivityBiz(context);
    }


    @Override
    public void convert(BaseViewHolder holder, FuncItemBean funcItemBean, int p) {
        holder.setText(R.id.func_title, funcItemBean.getFuncName() != null ? funcItemBean.getFuncName() : "");
        mainActivityBiz.setFuncImg((ImageView) holder.getView(R.id.func_ico), funcItemBean);
    }

}
