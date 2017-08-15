package com.example.framwork.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.example.framwork.R;


public class CustomerClearEditText extends ContainsEmojiEditText implements
        OnFocusChangeListener, TextWatcher {
    private Drawable mClearDrawable;
    //有内容时候的背景色
    private int mClearBgS;
    //无内容时候的背景色
    private int mClearBgN;
    private boolean hasFoucs;
    /**
     * 默认的清除按钮图标资源
     */
    private static final int ICON_CLEAR_DEFAULT = R.drawable.delete;
    private OnClearListener clearListener;

    public CustomerClearEditText(Context context) {
        super(context, null);
        init(context, null);
    }

    public CustomerClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomerClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        // 获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText);
        // 获取清除按钮图标资源
        int iconClear =
                typedArray.getResourceId(R.styleable.ClearEditText_iconClear, ICON_CLEAR_DEFAULT);
        mClearBgN = typedArray.getResourceId(R.styleable.ClearEditText_et_bg_n, R.color.transparent);
        mClearBgS = typedArray.getResourceId(R.styleable.ClearEditText_et_bg_s, R.color.transparent);
        mClearDrawable = ContextCompat.getDrawable(context, iconClear);
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        setBackgroundResource(mClearBgN);
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    //清除所有输入框内容监听
    public interface OnClearListener {
        void onClear();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    this.setText("");
                    if (clearListener != null) {
                        clearListener.onClear();
                    }
                }
            }
        }

        return super.onTouchEvent(event);
    }

    public void setClearListener(OnClearListener clearListener) {
        this.clearListener = clearListener;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }


    protected void setClearIconVisible(boolean visible) {
        if (visible) {
            setBackgroundResource(mClearBgS);
        } else {
            setBackgroundResource(mClearBgN);
        }
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int count,
                              int after) {
        if (hasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    public void setShakeAnimation() {
        this.setAnimation(shakeAnimation(5));
    }


    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }


}
