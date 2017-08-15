package com.storemax.android.devices.bean;

public class DeviceExceptionBean {
	public DeviceExceptionBean(Exception ex) {
		origExcept = ex;
	}
	
	Exception origExcept;
}
