package com.example.framwork.noHttp;


import com.example.framwork.noHttp.Bean.BaseResponseBean;

/**
 * 返回base类型
 */
public interface OnRequestListener {
    void requestSuccess(BaseResponseBean bean);

    void requestFailed(String error);

    //用于hide loading
    void requestFinish();
}
