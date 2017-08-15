package com.example.framwork.noHttp;

import android.app.Activity;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.download.DownloadQueue;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;

import org.json.JSONObject;


/**
 * @author WUJIANWEI;
 */
public class CallServer {

    private static CallServer callServer;
    /**
     * 请求队列.
     */
    private RequestQueue requestQueue;
    /**
     * 下载队列
     */
    private static DownloadQueue downloadQueue;
    private Request<JSONObject> request;

    public Request<JSONObject> getRequest() {
        return request;
    }

    private CallServer() {
        requestQueue = NoHttp.newRequestQueue();
    }

    /**
     * 请求队列.
     */
    public synchronized static CallServer getRequestInstance() {
        if (callServer == null)
            callServer = new CallServer();
        return callServer;
    }

    /**
     * 下载队列.
     */
    public static DownloadQueue getDownloadInstance() {
        if (downloadQueue == null)
            downloadQueue = NoHttp.newDownloadQueue();
        return downloadQueue;
    }

    /**
     * 添加一个请求到请求队列
     *
     * @param context     上下文
     * @param request     请求对象
     * @param callBack    接受回调结果
     * @param what        what，当多个请求用同一个responseListener接受结果时，用来区分请求
     * @param isCanCancel 请求是否能被用户取消
     */
    public <T> void add(Activity context, Request<JSONObject> request, HttpCallBack<JSONObject> callBack, int what, boolean isCanCancel) {
        request.setCancelSign(context);
        this.request = request;
        requestQueue.add(what, request, new ResponseListener<>(request, context, callBack, isCanCancel));
    }

    public <T> void addDownLoad(DownloadRequest downloadRequest,
                                com.yanzhenjie.nohttp.download.DownloadListener downloadListener,
                                int what) {
        downloadQueue.add(what, downloadRequest, downloadListener);
    }

    /**
     * 取消这个sign标记的所有请求.
     */
    public void cancelBySign(Object sign) {
        if (null != request)
            request.cancelBySign(sign);
    }

    /**
     * 取消队列中所有请求.
     */
    public void cancelAll() {
        requestQueue.cancelAll();
    }

    /**
     * 退出app时停止所有请求.
     */
    public void stopAll() {
        requestQueue.stop();
    }
}
