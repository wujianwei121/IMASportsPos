package com.example.framwork.noHttp;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.example.framwork.noHttp.Bean.BaseResponseBean;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RestRequest;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.json.JSONObject;


/**
 * Created by ${wjw} on 2016/3/31.
 */
public class BeanJsonResult<T> extends RestRequest<T> {
    private Class<T> clazz;

    public BeanJsonResult(String url, RequestMethod requestMethod, Class<T> clazz) {
        super(url, requestMethod);
        this.clazz = clazz;
    }

    public BeanJsonResult(String url, Class<T> clazz) {
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

    public static void execute(final Activity activity, final Request request, final OnRequestListener requestListener) {
        CallServer.getRequestInstance().add(activity, request, new HttpCallBack<JSONObject>() {
            @Override
            public void onSucceed(int what, JSONObject response) {
                if (activity.isFinishing()) {
                    return;
                }
                Logger.d("返回原始数据:" + response);
                if (null != response) {
                    BaseResponseBean bean = null;
                    try {
                        bean = BaseResponseBean.parseObj(response.toString(), BaseResponseBean.class);
                        if (bean.isSuccess()) {
                            requestListener.requestSuccess(bean);
                        } else {
                            requestListener.requestFailed(bean.getMessage() == null ? "获取数据失败" : bean.getMessage());
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        requestListener.requestFailed("数据解析失败");
                    }
                } else {
                    requestListener.requestFailed("获取数据失败");
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
