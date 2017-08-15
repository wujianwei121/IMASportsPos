package com.storemax.android.devices.magcard;

import com.storemax.android.devices.listener.OnDeviceOperaListener;
import com.storemax.android.devices.listener.OnDeviceStateListener;

import android.app.Activity;

public abstract class BaseMagcard {
	
	public abstract void openDevice(Activity activity,
			OnDeviceStateListener listener);
	
	public abstract void startDevice(OnDeviceOperaListener listener);
	
	public abstract void stopDevice() throws Exception;
	
	public abstract void closeDevice();
	
	public abstract String getOnFailMsg(String code);
}
