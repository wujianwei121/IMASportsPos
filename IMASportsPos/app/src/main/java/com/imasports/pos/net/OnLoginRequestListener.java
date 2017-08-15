package com.imasports.pos.net;

public interface OnLoginRequestListener {
    void requestSuccess(String bean);

    void requestFailed(String error);

    //用于hide loading
    void requestFinish();
}