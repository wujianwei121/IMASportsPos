package com.imasports.pos.main.presenter;

import android.app.Activity;

import com.example.framwork.base.BasePresenter;
import com.example.framwork.noHttp.Bean.BaseResponseBean;
import com.example.framwork.utils.Toast;
import com.imasports.pos.common.Constants;
import com.imasports.pos.main.model.bean.LoginSuccessBean;
import com.imasports.pos.main.view.ILoginView;
import com.imasports.pos.net.LoginBeanJsonResult;
import com.imasports.pos.net.OnLoginRequestListener;
import com.imasports.pos.utils.AppUtil;
import com.imasports.pos.utils.DeviceUtils;
import com.imasports.pos.utils.Goto;
import com.imasports.pos.utils.MD5;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by lenovo on 2017/8/9.
 */

public class LoginPresenter extends BasePresenter {
    private Activity activity;
    private ILoginView view;

    public LoginPresenter(Activity activity, ILoginView view) {
        super(activity);
        this.activity = activity;
        this.view = view;
    }

    public void login() {
        view.showLoading();
        Random random = new Random();
        String ranMD5 = MD5.MD5Encode(random.nextInt(1000) + "");
        HashMap info = new HashMap();
        String nowTime = String.valueOf(System.currentTimeMillis());
        info.put("client_id", Constants.CLIENT_ID);
        info.put("grant_type", "password");
        info.put("username", view.getAcount());
        info.put("password", view.getPassWord());
        Request<JSONObject> request = getRequest(info, Constants.LOGIN_HOST_URL, "/oauth/token");
        request.addHeader("merchantno", view.getStoreName());
        request.addHeader("posclientid", DeviceUtils.getDeviceID(activity));
        request.addHeader("timestamp", nowTime);
        request.addHeader("noncestr", ranMD5);
        request.addHeader("type", "0");
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("appkey", Constants.CLIENT_ID);
        parameters.put("timestamp", nowTime);
        parameters.put("noncestr", ranMD5);
        String signature = AppUtil.createSign(parameters, Constants.CLIENT_SECRET);
        request.addHeader("signature", signature);
        LoginBeanJsonResult.loginExecute(activity, request, new OnLoginRequestListener() {
            @Override
            public void requestSuccess(String bean) {
                LoginSuccessBean loginInfo = BaseResponseBean.parseObj(bean, LoginSuccessBean.class);
                Logger.d(loginInfo.access_token + "----" + loginInfo.MerchantNo);
                Goto.toMainActivity(activity);
            }

            @Override
            public void requestFailed(String error) {
                Toast.getInstance().showErrorToast(activity, error);
            }

            @Override
            public void requestFinish() {
                view.hideLoading();
            }
        });
    }
}
