package com.storemax.android.devices.listener;

import com.storemax.android.devices.bean.DeviceExceptionBean;

public interface OnRFCardReadListener {
	
	public void onSuccess(byte[] data);
	
	public void onFail(int code, String msg, Object obj);
	
	public void onCash();

	public void onThrowException(DeviceExceptionBean deviceExceptionBean);
}
