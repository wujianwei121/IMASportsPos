package com.storemax.android.devices.unipay;

import android.content.Intent;

public class SdpA8Unipay extends P990Unipay {
	
	@Override
	public UnipayType getUnipayType() {
		return UnipayType.SPD_A8;
	}
	
	@Override
	protected void setConsumeOrderInfo(Intent intent, String orderNo) {
		intent.putExtra("orderNo", orderNo);
	}
	
}
