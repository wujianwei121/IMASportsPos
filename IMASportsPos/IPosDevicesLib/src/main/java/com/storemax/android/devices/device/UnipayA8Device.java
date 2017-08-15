package com.storemax.android.devices.device;

import android.app.Activity;
import android.content.Context;

import com.storemax.android.devices.DeviceFactory;
import com.storemax.android.devices.bean.DeviceExceptionBean;
import com.storemax.android.devices.listener.OnDeviceInfoReceivedListener;
import com.storemax.android.devices.listener.OnDeviceOperaListener;
import com.storemax.android.devices.listener.OnDeviceStateListener;
import com.storemax.android.devices.magcard.BaseMagcard;
import com.storemax.android.devices.magcard.UnipayA8Magcard;
import com.storemax.android.devices.printer.BasePrinter;
import com.storemax.android.devices.printer.UnipayA8Printer;
import com.storemax.android.devices.rfcard.BaseRFCard;
import com.storemax.android.devices.rfcard.UnipayA8RFCard;
import com.storemax.android.devices.unipay.BaseUnipay;
import com.storemax.android.devices.unipay.UmsA8Unipay;
import com.ums.upos.sdk.exception.CallServiceException;
import com.ums.upos.sdk.exception.SdkException;
import com.ums.upos.sdk.system.BaseSystemManager;
import com.ums.upos.sdk.system.OnServiceStatusListener;

public class UnipayA8Device extends BaseDevice implements IDevice {
	
	@Override
	public BasePrinter getPrinter(Activity activity,
			OnDeviceOperaListener lister) {
		return new UnipayA8Printer(activity, lister);
	}
	
	@Override
	public BaseRFCard getRFCard() {
		return new UnipayA8RFCard(this);
	}
	
	@Override
	public BaseMagcard getMagcard() {
		return new UnipayA8Magcard(this);
	}
	
	@Override
	public void getDeviceSN(Context context,
			final OnDeviceInfoReceivedListener listener) {
		
		openDevice(context, new OnDeviceStateListener() {
			
			@Override
			public void onThrowException(DeviceExceptionBean ex) {

			}
			
			@Override
			public void onSuccess(int code, String msg, Object obj) {
				
				try {
					String sn = BaseSystemManager.getInstance().readSN();
					if (listener != null) {
						listener.getDeviceSn(sn);
				//		Log.e("unipay A8 sn ", "OK ");
					}
				} catch (SdkException e) {
					e.printStackTrace();
				} catch (CallServiceException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFail(int code, String msg, Object obj) {

			}
			
			@Override
			public void onCrash() {

			}
		});
		
	}
	
	@Override
	public void openDevice(Context context, final OnDeviceStateListener listener) {
		try {
			BaseSystemManager.getInstance().deviceServiceLogin(context, null,
					"99999998", new OnServiceStatusListener() {
						@Override
						public void onStatus(int arg0) {
							if (0 == arg0 || 2 == arg0 || 100 == arg0) {
								if (listener != null) {
						//			Log.e("myseti", "openDevice success");
									listener.onSuccess(DeviceFactory.SUCCESS,
											"", null);
								}
							} else {
								if (listener != null) {
							//		Log.e("myseti", "openDevice error");
									listener.onFail(DeviceFactory.ERROR, "",
											null);
								}
							}
						}
					});
		} catch (SdkException e) {
			if (listener != null) {
				listener.onThrowException(new DeviceExceptionBean(e));
			}
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void closeDevice() {
		try {
			BaseSystemManager.getInstance().deviceServiceLogout();
	//		Log.e("myseti", "closeDevice");
		} catch (SdkException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public BaseUnipay getUnipay() {
		return new UmsA8Unipay();
	}
	
	@Override
	public IDevice getIDevice() {
		return this;
	}
}
