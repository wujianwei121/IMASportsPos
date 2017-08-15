package com.example.framwork.base;

import android.app.Activity;

import com.example.framwork.noHttp.OnRequestListener;
import com.yanzhenjie.nohttp.rest.Request;

import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by wanjingyu on 2016/10/8.
 */

public abstract class BasePresenter {
    public CommonModel model;
    private Activity activity;

    public BasePresenter(Activity activity) {
        this.model = new CommonModel();
        this.activity = activity;
    }



    /**
     * 当请求里面需要添加请求头 先获取request
     * 最后要调用model.execute
     *
     * @return
     */
    public Request<JSONObject> getRequest(HashMap info, String url, String methodName) {
        return model.getRequest(info,url,methodName);
    }

    /**
     * 自定义requestlistener
     *
     * @param info
     * @param requestListener
     */
    public void post(HashMap info,String methodName,  OnRequestListener requestListener) {
        model.resultPostModel(activity, info, methodName,requestListener);
    }
    public void get(HashMap info,String methodName,  OnRequestListener requestListener) {
        model.resultGettModel(activity, info, methodName,requestListener);
    }
    public void postCustomUrl(HashMap info, String url, OnRequestListener requestListener) {
        model.resultCustomUrlModel(activity, info, url, requestListener);
    }

    public void postCustonUrl(HashMap info, String url, OnRequestListener requestListener) {
        model.resultCustomUrlModel(activity, info, url, requestListener);
    }


}
