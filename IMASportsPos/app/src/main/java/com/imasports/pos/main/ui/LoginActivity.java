package com.imasports.pos.main.ui;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.example.framwork.widget.CustomerClearEditText;
import com.imasports.pos.R;
import com.imasports.pos.base.BaseNoTitleActivity;
import com.imasports.pos.main.presenter.LoginPresenter;
import com.imasports.pos.main.view.ILoginView;

import butterknife.BindView;

/**
 * Created by lenovo on 2017/8/9.
 */

public class LoginActivity extends BaseNoTitleActivity implements CustomerClearEditText.OnClearListener, ILoginView {
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_net_setting)
    TextView tvNetSetting;
    @BindView(R.id.et_store_name)
    CustomerClearEditText etStoreName;
    @BindView(R.id.et_user_acount)
    CustomerClearEditText etUserAcount;
    @BindView(R.id.et_pwd)
    CustomerClearEditText etPwd;
    private LoginPresenter loginPresenter;


    @Override
    protected void initData() {
        loginPresenter = new LoginPresenter(this, this);
    }

    @Override
    protected void initView() {
        etStoreName.setClearListener(this);
        etPwd.setOnEditorActionListener(onkey);
        btnLogin.setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void onClickListener(View v) {
        loginPresenter.login();
    }

    @Override
    public void onClear() {
        etUserAcount.setText("");
        etPwd.setText("");
    }

    TextView.OnEditorActionListener onkey = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView arg0, int key, KeyEvent arg2) {
            // 先隐藏键盘
            if (key == EditorInfo.IME_ACTION_GO || key == 1) {
                onClick(btnLogin);
                return true;
            }
            return false;
        }
    };

    @Override
    public void showLoading() {
        showProgress();
    }

    @Override
    public void hideLoading() {
        hideProgress();
    }

    @Override
    public void showSuccess() {

    }

    @Override
    public void showFailed() {

    }

    @Override
    public String getStoreName() {
        return etStoreName.getText().toString().trim();
    }

    @Override
    public String getAcount() {
        return etUserAcount.getText().toString().trim();
    }

    @Override
    public String getPassWord() {
        return etPwd.getText().toString().trim();
    }
}
