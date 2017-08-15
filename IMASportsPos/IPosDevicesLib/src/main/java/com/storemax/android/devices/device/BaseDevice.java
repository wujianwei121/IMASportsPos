package com.storemax.android.devices.device;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.storemax.android.devices.DeviceFactory;
import com.storemax.android.devices.listener.OnDeviceInfoReceivedListener;
import com.storemax.android.devices.listener.OnDeviceOperaListener;
import com.storemax.android.devices.magcard.BaseMagcard;
import com.storemax.android.devices.printer.BasePrinter;
import com.storemax.android.devices.rfcard.BaseRFCard;
import com.storemax.android.devices.unipay.BaseUnipay;
import com.storemax.android.devices.unipay.NYPosUnipay;
import com.storemax.android.devices.unipay.P990Unipay;
import com.storemax.android.devices.unipay.SdpA8Unipay;
import com.storemax.android.devices.unipay.SpdMobileUnipay;
import com.storemax.android.devices.unipay.UmsA8Unipay;
import com.storemax.android.devices.unipay.BaseUnipay.UnipayType;

public abstract class BaseDevice {

	private static BaseUnipay currenUnipay = null;

	public abstract void getDeviceSN(Context context, OnDeviceInfoReceivedListener listener);

	public abstract BasePrinter getPrinter(Activity activity, OnDeviceOperaListener lister);

	public abstract IDevice getIDevice();

	public abstract BaseRFCard getRFCard();

	public abstract BaseMagcard getMagcard();

	public abstract BaseUnipay getUnipay();

	protected BaseUnipay createUnipay(UnipayType type) {

		if (type == UnipayType.AUTO) {
			UnipayType temp = autoCreateUnipay();
			if (temp != UnipayType.UNKNOWN) {
				return createUnipayByType(autoCreateUnipay());
			} else {
				return null;
			}
		} else {
			return createUnipayByType(type);
		}
	}

	protected BaseUnipay createUnipayByType(UnipayType type) {

		switch (type) {
		case SPD_MOBILE:
			return new SpdMobileUnipay();
		case P990:
			return new P990Unipay();
		case SPD_A8:
			return new SdpA8Unipay();
		case UNIPAY_UMS_A8:
			return new UmsA8Unipay();
		case NY_POS:
			return new NYPosUnipay();
		default: {
			return null;
		}
		}
	}

	private UnipayType autoCreateUnipay() {

		final PackageManager packageManager = DeviceFactory.context.getPackageManager();
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		List<String> pName = new ArrayList<String>();
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				pName.add(pn);
			}
		}
		for (UnipayType unipay : UnipayType.values()) {
			if (pName.contains(unipay.getPackageName())) {
				return unipay;
			}
		}
		return UnipayType.UNKNOWN;
	}
}
