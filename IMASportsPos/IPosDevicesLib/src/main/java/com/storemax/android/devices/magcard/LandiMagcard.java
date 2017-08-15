package com.storemax.android.devices.magcard;

import android.app.Activity;

import com.landicorp.android.eptapi.DeviceService;
import com.landicorp.android.eptapi.device.MagCardReader;
import com.landicorp.android.eptapi.device.MagCardReader.OnSearchListener;
import com.landicorp.android.eptapi.exception.ReloginException;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.exception.ServiceOccupiedException;
import com.landicorp.android.eptapi.exception.UnsupportMultiProcess;
import com.storemax.android.devices.DeviceFactory;
import com.storemax.android.devices.bean.DeviceExceptionBean;
import com.storemax.android.devices.device.IDevice;
import com.storemax.android.devices.listener.OnDeviceOperaListener;
import com.storemax.android.devices.listener.OnDeviceStateListener;

public class LandiMagcard extends BaseMagcard {
	MagCardReader magCardReader;
	OnDeviceOperaListener listener;
	IDevice idevice = null;
	
	public LandiMagcard(IDevice device) {
		idevice = device;
	}
	
	@Override
	public void openDevice(Activity activity, OnDeviceStateListener listener) {
		idevice.openDevice(activity, listener);
		
	}
	
	@Override
	public void startDevice(OnDeviceOperaListener listener) {
		magCardReader = MagCardReader.getInstance();
		this.listener = listener;
		try {
			magCardReader.searchCard(searchListener);
		} catch (RequestException ex) {
			if (null != listener) {
				listener.onThrowException(new DeviceExceptionBean(ex));
			}
			ex.printStackTrace();
		}
	}
	
	@Override
	public void stopDevice() {
		if (null != magCardReader) {
			try {
				magCardReader.stopSearch();
			} catch (RequestException ex) {
				if (null != listener) {
					listener.onThrowException(new DeviceExceptionBean(ex));
				}
				ex.printStackTrace();
			}
		}
	}
	
	@Override
	public void closeDevice() {
		listener = null;
		idevice.closeDevice();
		
	}
	
	OnSearchListener searchListener = new OnSearchListener() {
		
		@Override
		public void onCrash() {
			if (null != listener) {
				listener.onCash();
			}
		}
		
		@Override
		public void onFail(int arg0) {
			if (null != listener) {
				listener.onFail(arg0, getOnFailMsg(arg0 + ""), null);
			}
		}
		
		@Override
		public void onCardStriped(boolean[] arg0, String[] arg1) {
			if (null != listener) {
				listener.onSuccess(DeviceFactory.SUCCESS, "成功", arg1);
			}
		}
		
		@Override
		public boolean checkValid(int[] trackStates, String[] track) {
			return true;
		}
	};
	
	@Override
	public String getOnFailMsg(String code) {
		int failCode = Integer.parseInt(code);
		switch (failCode) {
		
		}
		return "未知错误，错误码：" + code;
	}
}
