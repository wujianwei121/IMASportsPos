package com.imasports.pos.main;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.framwork.widget.CircleImageView;
import com.imasports.pos.R;
import com.imasports.pos.base.BaseNoTitleActivity;
import com.imasports.pos.common.FusionUnitType;
import com.imasports.pos.main.adapter.FuncItemAdapter;
import com.imasports.pos.main.model.bean.FuncItemBean;
import com.imasports.pos.utils.Goto;

import java.util.List;

import butterknife.BindView;


public class MainActivity extends BaseNoTitleActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.iv_header)
    CircleImageView ivHeader;
    @BindView(R.id.tv_store_name)
    TextView tvStoreName;
    @BindView(R.id.tv_staff_name)
    TextView tvStaffName;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.gv_main)
    GridView gvMain;
    private FuncItemAdapter funcAdapter;//视图适配器
    private List<FuncItemBean> funcDataList;//功能数据
    private MainActivityBiz mainActivityBiz;


    @Override
    protected void initData() {
        initFunc();
        funcAdapter = new FuncItemAdapter(this, R.layout.item_func_main, funcDataList);
        gvMain.setAdapter(funcAdapter);
    }

    @Override
    protected void initView() {
        gvMain.setOnItemClickListener(this);

    }

    @Override
    protected int getContentLayout() {
        return R.layout.acitivity_main;
    }

    /**
     * 初始化功能菜单列表
     * 因服务器没有实现，所以暂时从本地资源列表中读取
     */
    private void initFunc() {
        mainActivityBiz = new MainActivityBiz(context);
        funcDataList = mainActivityBiz.getFunctions();
        if (funcDataList == null) {
            funcDataList = mainActivityBiz.getAllFunctions();
            mainActivityBiz.setFunctions(funcDataList);
        }
    }

    @Override
    public void onClickListener(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FuncItemBean funcItemBean = (FuncItemBean) parent.getAdapter().getItem(position);
        switch (funcItemBean.getFuncId()) {
            case FusionUnitType.FuncType.FUNC_MEMBER_ENTER:
                Goto.toMemberRegActivity(context);
                break;
            case FusionUnitType.FuncType.FUNC_COURSE_SALE:
                Goto.toCourseListActivity(context);
                break;
        }

    }

}
