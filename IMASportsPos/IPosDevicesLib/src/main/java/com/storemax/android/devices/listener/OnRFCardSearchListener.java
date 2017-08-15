package com.storemax.android.devices.listener;

public interface OnRFCardSearchListener {
	public void onSuccess();
	
	public void onFail(int code, String msg, Object obj);
	
	public void onCrash();
}
