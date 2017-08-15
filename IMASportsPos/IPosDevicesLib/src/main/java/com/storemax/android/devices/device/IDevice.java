package com.storemax.android.devices.device;

import android.content.Context;

import com.storemax.android.devices.listener.OnDeviceStateListener;

public interface IDevice {
	void openDevice(Context context, OnDeviceStateListener listener);
	
	void closeDevice();
	
}
