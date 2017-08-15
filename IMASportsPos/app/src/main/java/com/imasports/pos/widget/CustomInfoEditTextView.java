package com.imasports.pos.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.example.framwork.utils.DateUtil;
import com.imasports.pos.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by lenovo on 2017/8/14.
 */

public class CustomInfoEditTextView extends LinearLayout implements View.OnClickListener {


    public static final int MODE_BIRTHDATE = 1;
    public static final int MODE_WEIGHT = 2;
    public static final int MODE_HEIGHT = 3;
    private TextView tv_prompt;//编辑时提示textview
    private EditText et_info;
    private TextView tv_content;//不可编辑时候textview
    private ImageView iv_mandatory;//是否必填小图标
    private ImageView iv_arrow;
    private String sPrompt, sContent, sHint;
    private boolean bShowArrow, bEditable;
    private int iMandatoryVisibility, iMode = 0;
    private int iPromptTextColor, iEditTextColor, iLength, iInputType;
    private Context mContext;
    private OptionsPickerView pvHeightOptions;
    private OptionsPickerView pvWeightOptions;
    private TimePickerView pvBirthdayOptions;

    public CustomInfoEditTextView(Context context) {
        super(context);
        initAttrs(context, null);
        init();
    }

    public CustomInfoEditTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    public CustomInfoEditTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomInfoEditStyle);
        sPrompt = typedArray.getString(R.styleable.CustomInfoEditStyle_cie_prompt_text);
        sContent = typedArray.getString(R.styleable.CustomInfoEditStyle_cie_edit_text);
        sHint = typedArray.getString(R.styleable.CustomInfoEditStyle_cie_edit_hint);
        iMandatoryVisibility = typedArray.getInteger(R.styleable.CustomInfoEditStyle_cie_mandatory_visibility, 1);
        iEditTextColor = typedArray.getResourceId(R.styleable.CustomInfoEditStyle_cie_edit_textcolor, R.color.text_color);
        iPromptTextColor = typedArray.getResourceId(R.styleable.CustomInfoEditStyle_cie_prompt_textcolor, R.color.text_color);
        bShowArrow = typedArray.getBoolean(R.styleable.CustomInfoEditStyle_cie_show_arrow, false);
        bEditable = typedArray.getBoolean(R.styleable.CustomInfoEditStyle_cie_editable, true);
        iLength = typedArray.getInteger(R.styleable.CustomInfoEditStyle_cie_edit_text_length, 10);
        iInputType = typedArray.getInteger(R.styleable.CustomInfoEditStyle_cie_edit_text_inputtype, 2);
        iMode = typedArray.getInteger(R.styleable.CustomInfoEditStyle_cie_mode, 0);
        typedArray.recycle();
    }

    private void init() {
        setBackgroundResource(R.color.white);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_info_edit_text, this, true);
        tv_prompt = (TextView) findViewById(R.id.tv_info);
        et_info = (EditText) findViewById(R.id.et_info);
        tv_content = (TextView) findViewById(R.id.tv_content);
        iv_mandatory = (ImageView) findViewById(R.id.iv_mandatory);
        iv_arrow = (ImageView) findViewById(R.id.iv_arrow);
        if (iLength>0) {
            et_info.setFilters(new InputFilter[]{new InputFilter.LengthFilter(iLength)});
        }
        if (iInputType==1){
            et_info.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        switch (iMandatoryVisibility) {
            case 0:
                iv_mandatory.setVisibility(GONE);
                break;
            case 1:
                iv_mandatory.setVisibility(VISIBLE);
                break;
            case 2:
                iv_mandatory.setVisibility(INVISIBLE);
                break;
        }
        tv_prompt.setTextColor(ContextCompat.getColor(mContext, iPromptTextColor));
        et_info.setTextColor(ContextCompat.getColor(mContext, iEditTextColor));
        tv_prompt.setText(sPrompt);
        setEditText(sContent);
        if (bShowArrow) {
            iv_arrow.setVisibility(VISIBLE);
        } else {
            iv_arrow.setVisibility(GONE);
        }

        if (iMode != 0)
            setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (iMode == MODE_HEIGHT) {
            showHeightOptionsPicker();
        } else if (iMode == MODE_WEIGHT) {
            showWeightOptionsPicker();
        } else if (iMode == MODE_BIRTHDATE) {
            showBirthdayOptionsPicker();
        }
    }

    private void showWeightOptionsPicker() {
        if (pvWeightOptions == null) {
            pvWeightOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {

                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    sContent = getWeightData().get(options1);
                    tv_content.setText(new StringBuilder().append(sContent).append("kg"));
                }
            })
                    .setSubmitText("确定")//确定按钮文字
                    .setCancelText("取消")//取消按钮文字
                    .setTitleText("选择体重（kg）")//标题
                    .setSubmitColor(ContextCompat.getColor(mContext, R.color.red))//确定按钮文字颜色
                    .setCancelColor(ContextCompat.getColor(mContext, R.color.gary_82))
                    .setTitleBgColor(ContextCompat.getColor(mContext, R.color.gray_d8))//标题背景颜色 Night mode
                    .setTitleColor(ContextCompat.getColor(mContext, R.color.gary_82))//标题背景颜色 Night mode
                    .build();
            pvWeightOptions.setNPicker(getWeightData(), null, null);
        }
        pvWeightOptions.show();
    }

    private void showHeightOptionsPicker() {
        if (pvHeightOptions == null) {
            pvHeightOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {

                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    sContent = getHeightData().get(options1);
                    tv_content.setText(new StringBuilder().append(sContent).append("cm"));
                }
            })
                    .setSubmitText("确定")//确定按钮文字
                    .setCancelText("取消")//取消按钮文字
                    .setTitleText("选择身高（cm）")//标题
                    .setSubmitColor(ContextCompat.getColor(mContext, R.color.red))//确定按钮文字颜色
                    .setCancelColor(ContextCompat.getColor(mContext, R.color.gary_82))
                    .setTitleBgColor(ContextCompat.getColor(mContext, R.color.gray_d8))//标题背景颜色 Night mode
                    .setTitleColor(ContextCompat.getColor(mContext, R.color.gary_82))//标题背景颜色 Night mode
                    .build();
            pvHeightOptions.setNPicker(getHeightData(), null, null);
        }
        pvHeightOptions.show();
    }

    private void showBirthdayOptionsPicker() {
        if (pvBirthdayOptions == null) {
            Calendar selectedDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            endDate.set(DateUtil.getInstance().getYear(), DateUtil.getInstance().getMonth() - 1, DateUtil.getInstance().getDay());

            pvBirthdayOptions = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {//选中事件回调
                    sContent = DateUtil.getInstance().date2Str(date, "yyyy.MM.dd");
                    tv_content.setText(sContent);
                }
            })
                    .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                    .setSubmitText("确定")//确定按钮文字
                    .setCancelText("取消")//取消按钮文字
                    .setTitleText("选择生日")//标题
                    .setSubmitColor(ContextCompat.getColor(mContext, R.color.red))//确定按钮文字颜色
                    .setCancelColor(ContextCompat.getColor(mContext, R.color.gary_82))
                    .setTitleBgColor(ContextCompat.getColor(mContext, R.color.gray_d8))//标题背景颜色 Night mode
                    .setTitleColor(ContextCompat.getColor(mContext, R.color.gary_82))//标题背景颜色 Night mode
                    .isCyclic(false)//是否循环滚动
                    .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                    .setRangDate(null, endDate)//起始终止年月日设定
                    .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                    .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                    .build();
        }
        pvBirthdayOptions.show();
    }

    private List<String> getWeightData() {
        List<String> weightList = new ArrayList<>();
        for (int i = 20; i < 200; i++) {
            weightList.add(String.valueOf(i));
        }
        return weightList;
    }

    private List<String> getHeightData() {
        List<String> weightList = new ArrayList<>();
        for (int i = 50; i < 230; i++) {
            weightList.add(String.valueOf(i));
        }
        return weightList;
    }

    public String getsContent() {
        return sContent;
    }

    public void setEditText(String s) {
        sContent = s;
        if (!bEditable) {
            et_info.setVisibility(GONE);
            tv_content.setVisibility(VISIBLE);
            if (sContent == null || TextUtils.isEmpty(sContent)) {
                tv_content.setHint(sHint);
            } else {
                tv_content.setText(s);
            }
        } else {
            tv_content.setVisibility(GONE);
            et_info.setVisibility(VISIBLE);
            if (sContent == null || TextUtils.isEmpty(sContent)) {
                et_info.setHint(sHint);
            } else {
                et_info.setText(s);
            }
        }
    }
}
