package com.storemax.android.devices.listener;

import com.storemax.android.devices.bean.DeviceExceptionBean;

public interface OnRFCardWriteListener {
	
	public void onSuccess();
	
	public void onFail(int code, String msg, Object obj);
	
	public void onCash();
	
	public void onThrowException(DeviceExceptionBean ex);
}
