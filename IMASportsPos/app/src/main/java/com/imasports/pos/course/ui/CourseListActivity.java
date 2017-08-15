package com.imasports.pos.course.ui;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.framwork.widget.ProgressActivity;
import com.example.framwork.widget.superrecyclerview.recycleview.SuperRecyclerView;
import com.imasports.pos.R;
import com.imasports.pos.base.BaseTitleActivity;
import com.imasports.pos.course.adapter.CourseTypeAdapter;
import com.imasports.pos.course.model.bean.CourseTypeBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lenovo on 2017/8/11.
 */

public class CourseListActivity extends BaseTitleActivity {
    @BindView(R.id.tv_member_name)
    TextView tvMemberName;
    @BindView(R.id.tv_member_change)
    TextView tvMemberChange;
    @BindView(R.id.iv_member_scanning)
    ImageView ivMemberScanning;
    @BindView(R.id.et_member_search)
    EditText etMemberSearch;
    @BindView(R.id.expandable_list_view)
    ExpandableListView expandableListView;
    @BindView(R.id.rv_container)
    SuperRecyclerView rvContainer;
    @BindView(R.id.progressActivity)
    ProgressActivity progressActivity;
    @BindView(R.id.tv_cart_total_count)
    TextView tvCartTotalCount;
    @BindView(R.id.rl_cart_total_count)
    RelativeLayout rlCartTotalCount;
    @BindView(R.id.tv_cart_total_price_title)
    TextView tvCartTotalPriceTitle;
    @BindView(R.id.tv_cart_total_price)
    TextView tvCartTotalPrice;
    @BindView(R.id.btn_cart_settle)
    Button btnCartSettle;
    private List<CourseTypeBean> groupArray;
    private List<List<CourseTypeBean>> childArray;
    private CourseTypeAdapter expandableListAdapterAdapter;

    @Override
    protected void setCommonTitle() {
        actionBar.setCenterText("课程销售");
    }

    @Override
    protected void initData() {
        groupArray = new ArrayList<>();
        childArray = new ArrayList<>();
        List<CourseTypeBean> tempArray = new ArrayList<CourseTypeBean>();
        for (int j = 0; j < 4; j++) {
            CourseTypeBean bean = new CourseTypeBean();
            bean.setCategoryName("第" + j + "题");
            tempArray.add(bean);
        }
        for (int i = 0; i < 20; i++) {
            CourseTypeBean bean = new CourseTypeBean();
            bean.setCategoryName("第" + i + "页");
            groupArray.add(bean);
            childArray.add(tempArray);
        }

    }

    @Override
    protected void initView() {
        expandableListAdapterAdapter = new CourseTypeAdapter(groupArray, childArray, context);
        expandableListView.setAdapter(expandableListAdapterAdapter);
        click();
    }

    private void click() {
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < groupArray.size(); i++) {//展开一项 关闭其他分组
                    if (i != groupPosition) {
                        groupArray.get(i).setIsPressed(false);
                        expandableListView.collapseGroup(i);
                    }
                }
            }
        });
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                CourseTypeBean item = groupArray.get(groupPosition);
                showProduct(item, CourseTypeAdapter.Onclick.GROUP);
                return false;//默认为false，设为true时，点击事件不会展开Group
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CourseTypeBean item = childArray.get(groupPosition).get(childPosition);
                showProduct(item, CourseTypeAdapter.Onclick.CHILD);
                return false;
            }
        });
    }

    /**
     * 显示某分类下的商品
     *
     * @param item
     */
    private void showProduct(CourseTypeBean item, CourseTypeAdapter.Onclick level) {
        item.setIsPressed(true);
        expandableListAdapterAdapter.setmOnclick(level);
        expandableListAdapterAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_course;
    }

    @Override
    public void onClickListener(View v) {

    }

}
