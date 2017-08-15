package com.storemax.android.devices.listener;

import com.storemax.android.devices.bean.DeviceExceptionBean;

public interface OnDeviceOperaListener {
	
	public void onSuccess(int code, String msg, Object obj);
	
	public void onFail(int code, String msg, Object obj);
	
	public void onCash();
	
	public void onThrowException(DeviceExceptionBean bean);
}
