package com.storemax.android.devices.rfcard;

import android.app.Activity;

import com.storemax.android.devices.bean.RFCardAuthBean;
import com.storemax.android.devices.bean.RFCardBlockBean;
import com.storemax.android.devices.listener.OnDeviceStateListener;
import com.storemax.android.devices.listener.OnRFCardReadListener;
import com.storemax.android.devices.listener.OnRFCardSearchListener;
import com.storemax.android.devices.listener.OnRFCardWriteListener;
import com.ums.upos.sdk.utils.common.ByteUtils;

public abstract class BaseRFCard {
	
	private byte[] getKeyA(byte[] block0) {
		return new byte[] { (byte) (block0[0] + 4), block0[1],
				(byte) (block0[2] + 4), (byte) ((~block0[3]) + 4),
				(byte) (block0[0] - 4), 0x60 };
	}
	
	public byte[] getDefaultKeyA() {
		try {
			return getKeyA(ByteUtils.hexString2ByteArray(readCardNumber()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public enum KeyType {
		KEY_A, KEY_B
	}
	
	public byte[] getEmptyKey() {
		try {
			return ByteUtils.hexString2ByteArray("FFFFFFFFFFFF");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static final int SEARCH_CARD_FAIL = -1;
	
	public static final int GET_CARD_NUMBER_ERROR = -2;
	
	public abstract void searchCard(OnRFCardSearchListener listener)
			throws Exception;
	
	public abstract void readBlock(RFCardAuthBean auth, RFCardBlockBean block,
			OnRFCardReadListener listener);
	
	public abstract void writeBlock(RFCardAuthBean auth, RFCardBlockBean block,
			OnRFCardWriteListener listener);
	
	public abstract void stopDevice() throws Exception;
	
	public abstract void closeDevice() throws Exception;
	
	public abstract void writeCardNumber(String cardNo) throws Exception;
	
	public abstract String readCardNumber() throws Exception;
	
	public abstract String getErrorMsgByCode(int code);
	
	public abstract void openDevice(Activity activity,
			OnDeviceStateListener listener);
	
	public static final int ERROR_CODE_UNSUPPORT = 0xff01;
}
