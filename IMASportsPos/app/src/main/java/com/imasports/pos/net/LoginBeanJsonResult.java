package com.imasports.pos.net;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.example.framwork.noHttp.Bean.BaseResponseBean;
import com.example.framwork.noHttp.CallServer;
import com.example.framwork.noHttp.HttpCallBack;
import com.imasports.pos.main.model.bean.LoginFailBean;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RestRequest;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.json.JSONObject;

/**
 * Created by lenovo on 2017/8/9.
 */

public class LoginBeanJsonResult<T> extends RestRequest<T> {
    private Class<T> clazz;

    public LoginBeanJsonResult(String url, RequestMethod requestMethod, Class<T> clazz) {
        super(url, requestMethod);
        this.clazz = clazz;
    }

    public LoginBeanJsonResult(String url, Class<T> clazz) {
        this(url, RequestMethod.GET, clazz);
    }

    @Override
    public T parseResponse(Headers responseHeaders, byte[] responseBody) {
        String res = StringRequest.parseResponseString(responseHeaders, responseBody);
        try {
            return JSON.parseObject(res, clazz);
        } catch (Exception e) {
            Logger.e(e);
            try {
                // 所以传进来的JavaBean一定要提供默认无参构造
                return clazz.newInstance();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public interface LoginRestLinstener {
        void loginRest(Activity activity);
    }

    public static void loginExecute(final Activity activity, final Request request, final OnLoginRequestListener requestListener) {
        CallServer.getRequestInstance().add(activity, request, new HttpCallBack<JSONObject>() {
            @Override
            public void onSucceed(int what, JSONObject response) {
                if (activity.isFinishing()) {
                    return;
                }
                Logger.d("返回原始数据:" + response);
                if (null != response) {
                    LoginFailBean bean = null;
                    try {
                        bean = BaseResponseBean.parseObj(response.toString(), LoginFailBean.class);
                        if (bean.error != null) {
                            requestListener.requestFailed(bean.error_description == null ? "登录失败" : bean.error_description);
                        } else {
                            requestListener.requestSuccess(response.toString());
                            Logger.d("success--------------------------");
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        requestListener.requestFailed("登录失败");
                    }
                } else {
                    requestListener.requestFailed("登录失败");
                }
            }

            @Override
            public void onFailed(int what, String exception) {
                if (activity.isFinishing()) {
                    return;
                }
                requestListener.requestFailed(exception);
            }

            @Override
            public void onFinish() {
                requestListener.requestFinish();
            }

        }, 0, false);
    }
}
