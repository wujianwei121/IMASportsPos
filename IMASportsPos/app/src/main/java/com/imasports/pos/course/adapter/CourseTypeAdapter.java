package com.imasports.pos.course.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.imasports.pos.R;
import com.imasports.pos.course.model.bean.CourseTypeBean;

import java.util.List;

/**
 * Created by lenovo on 2017/8/11.
 */

public class CourseTypeAdapter extends BaseExpandableListAdapter {
    private List<CourseTypeBean> group;
    private List<List<CourseTypeBean>> child;
    private LayoutInflater mInflater;
    private Context mContext;
    private Onclick mOnclick;

    /**
     * 当前点击情况
     */
    public enum Onclick {
        /**
         * 点击一级菜单
         */
        GROUP,
        /**
         * 点击二级菜单
         */
        CHILD
    }

    public void setmOnclick(Onclick mOnclick) {
        this.mOnclick = mOnclick;
    }

    public CourseTypeAdapter(List<CourseTypeBean> group, List<List<CourseTypeBean>> child, Context mContext) {
        this.group = group;
        this.child = child;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public int getGroupCount() {
        return group == null ? 0 : group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child == null ? 0 : child.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group == null ? null : group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child == null || child.get(groupPosition) == null ? null : child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_course_type_groupview, null);
            viewHolder = new ViewHolder();
            viewHolder.tvGroup = (TextView) convertView.findViewById(R.id.tv_group);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvGroup.setText(group.get(groupPosition).getCategoryName());
        if (group.get(groupPosition).getIsPressed()) {
            if (mOnclick == Onclick.GROUP) {
                viewHolder.tvGroup.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
                viewHolder.tvGroup.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            } else {
                viewHolder.tvGroup.setBackgroundColor(ContextCompat.getColor(mContext, R.color.bg_grey));
                viewHolder.tvGroup.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            }
        } else {
            viewHolder.tvGroup.setBackgroundColor(ContextCompat.getColor(mContext, R.color.bg_grey));
            viewHolder.tvGroup.setTextColor(ContextCompat.getColor(mContext, R.color.text_color));
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_course_type_childview, null);
            viewHolder = new ViewHolder();
            viewHolder.tvGroup = (TextView) convertView.findViewById(R.id.tv_child);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvGroup.setText(child.get(groupPosition).get(childPosition).getCategoryName());
        if (child.get(groupPosition).get(childPosition).getIsPressed()) {
            if (mOnclick == Onclick.CHILD) {
                viewHolder.tvGroup.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
                viewHolder.tvGroup.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            }
        } else {
            viewHolder.tvGroup.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            viewHolder.tvGroup.setTextColor(ContextCompat.getColor(mContext, R.color.gray_9b));
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

class ViewHolder {
    TextView tvGroup;
}