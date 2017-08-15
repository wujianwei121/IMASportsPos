package com.example.framwork.base;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.example.framwork.baseapp.BaseAppConfig;
import com.example.framwork.noHttp.BeanJsonResult;
import com.example.framwork.noHttp.FastJsonRequest;
import com.example.framwork.noHttp.OnRequestListener;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;


import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by wanjingyu on 2016/10/8.
 */

public class CommonModel {

    /*
    * 一般post请求是调用*/
    public void resultPostModel(Activity context, HashMap info, String methodName, OnRequestListener requestListener) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(BaseAppConfig.SERVICE_PATH + methodName,
                RequestMethod.POST);
        request.add(info);
        BeanJsonResult.execute(context, request, requestListener);
    }
    /*
        * 一般post请求是调用*/
    public void resultGettModel(Activity context, HashMap info, String methodName, OnRequestListener requestListener) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(BaseAppConfig.SERVICE_PATH + methodName,
                RequestMethod.GET);
        request.add(info);
        BeanJsonResult.execute(context, request, requestListener);
    }
    /*
      * 自定义URL的model*/
    public void resultCustomUrlModel(Activity context, HashMap info, String URL, OnRequestListener requestListener) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(BaseAppConfig.SERVICE_PATH,
                RequestMethod.POST);
        request.add(info);
        BeanJsonResult.execute(context, request, requestListener);
    }


    public Request<JSONObject> getRequest(HashMap info, String url, String methodName) {
        // FastJson
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(url + methodName, RequestMethod.POST);
        request.add(info);
        return request;
    }

    /**
     * 需要配合resultFileModel
     *
     * @param context
     * @param request
     * @param requestListener
     */
    public void execute(Activity context, Request request, OnRequestListener requestListener) {
        BeanJsonResult.execute(context, request, requestListener);
    }
}
