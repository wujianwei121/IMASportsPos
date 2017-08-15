package com.imasports.pos.main;

import android.content.Context;
import android.widget.ImageView;

import com.example.framwork.utils.SPUtils;
import com.imasports.pos.R;
import com.imasports.pos.main.model.bean.FuncItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wjw on 2016/10/17.
 */
public class MainActivityBiz {
    public static final String GRID_CONFIG = "gridconfig";
    private Context mContext;

    public MainActivityBiz(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 保存用户设置的功能顺序
     */
    public void setFunctions(List<FuncItemBean> funcDataList) {
        SPUtils.getInstance().saveObject(mContext, GRID_CONFIG, funcDataList);
    }

    /**
     * 读取用户设置的功能排序
     */
    public List<FuncItemBean> getFunctions() {
        return (List<FuncItemBean>) SPUtils.getInstance().get(mContext, GRID_CONFIG, null);
    }

    /**
     * 获取所以功能
     *
     * @return
     */
    public List<FuncItemBean> getAllFunctions() {
        List<FuncItemBean> mFuncDataList = new ArrayList<FuncItemBean>();
        FuncItemBean bean1 = new FuncItemBean();
        bean1.setFuncId(1001);
        bean1.setFuncName("会员登记");
        mFuncDataList.add(bean1);
        FuncItemBean bean2 = new FuncItemBean();
        bean2.setFuncId(1002);
        bean2.setFuncName("课程销售");
        mFuncDataList.add(bean2);
        FuncItemBean bean3 = new FuncItemBean();
        bean3.setFuncId(1003);
        bean3.setFuncName("票卡核销");
        mFuncDataList.add(bean3);
        FuncItemBean bean4 = new FuncItemBean();
        bean4.setFuncId(1004);
        bean4.setFuncName("会员充值");
        mFuncDataList.add(bean4);
        FuncItemBean bean5 = new FuncItemBean();
        bean5.setFuncId(1005);
        bean5.setFuncName("订单查询");
        mFuncDataList.add(bean5);
        FuncItemBean bean6 = new FuncItemBean();
        bean6.setFuncId(1006);
        bean6.setFuncName("会员查询");
        mFuncDataList.add(bean6);
        return mFuncDataList;
    }

    /**
     * 设置功能图标（非必现  主页功能图标错乱  故采用这种方式）
     *
     * @param imageView
     * @param bean
     */
    public void setFuncImg(ImageView imageView, FuncItemBean bean) {
        switch (bean.getFuncId()) {
            case 1001://会员登记
                imageView.setImageResource(R.mipmap.ic_hydj);
                break;
            case 1002://课程销售
                imageView.setImageResource(R.mipmap.ic_kcxs);
                break;
            case 1003://票卡核销
                imageView.setImageResource(R.mipmap.ic_pkhx);
                break;
            case 1004://会员充值
                imageView.setImageResource(R.mipmap.ic_hycz);
                break;
            case 1005://订单查询
                imageView.setImageResource(R.mipmap.ic_ddcx);
                break;
            case 1006://会员查询
                imageView.setImageResource(R.mipmap.ic_hycx);
                break;
            default:
                break;
        }
    }
}
