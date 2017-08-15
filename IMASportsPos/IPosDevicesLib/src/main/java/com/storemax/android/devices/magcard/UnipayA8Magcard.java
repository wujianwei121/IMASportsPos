package com.storemax.android.devices.magcard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.storemax.android.devices.bean.DeviceExceptionBean;
import com.storemax.android.devices.device.IDevice;
import com.storemax.android.devices.listener.OnDeviceOperaListener;
import com.storemax.android.devices.listener.OnDeviceStateListener;
import com.ums.upos.sdk.cardslot.CardInfoEntity;
import com.ums.upos.sdk.cardslot.CardSlotManager;
import com.ums.upos.sdk.cardslot.CardSlotTypeEnum;
import com.ums.upos.sdk.cardslot.CardTypeEnum;
import com.ums.upos.sdk.cardslot.OnCardInfoListener;
import com.ums.upos.sdk.cardslot.SwipeSlotOptions;
import com.ums.upos.sdk.exception.CallServiceException;
import com.ums.upos.sdk.exception.SdkException;
import com.ums.upos.sdk.system.BaseSystemManager;
import com.ums.upos.sdk.system.OnServiceStatusListener;

public class UnipayA8Magcard extends BaseMagcard {
	
	private Activity activity;
	private OnDeviceOperaListener operaListener;
	private CardSlotManager cardSlotManager = null;
	private IDevice idevice;
	
	public UnipayA8Magcard(IDevice idevice) {
		this.idevice = idevice;
	}
	
	@Override
	public void openDevice(Activity activity,
			final OnDeviceStateListener stateListener) {
		this.activity = activity;
		idevice.openDevice(activity, stateListener);
	}
	
	@Override
	public void startDevice(OnDeviceOperaListener listener) {
		this.operaListener = listener;
//		Log.e("myseti", "startDevice magcard");
		cardSlotManager = new CardSlotManager();
		searchCardInfo();
	}
	
	@Override
	public void stopDevice() {
		try {
	//		Log.e("myseti", "stopDevice magcard");
			cardSlotManager.stopRead();
		} catch (SdkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CallServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void closeDevice() {
		
		this.operaListener = null;
		idevice.closeDevice();
	}
	
	private void searchCardInfo() {
		
		Set<CardSlotTypeEnum> slotTypes = new HashSet<CardSlotTypeEnum>();// 轮询的卡槽设备集
		slotTypes.add(CardSlotTypeEnum.SWIPE);// 刷卡卡槽
		
		Set<CardTypeEnum> cardTypes = new HashSet<CardTypeEnum>();// 卡类型集
		cardTypes.add(CardTypeEnum.MAG_CARD);// 磁条卡
		int timeout = 5000;
		try {
			Map<CardSlotTypeEnum, Bundle> options = new HashMap<CardSlotTypeEnum, Bundle>();
			Bundle bundle = new Bundle();
			bundle.putBoolean(SwipeSlotOptions.LRC_CHECK, false);
			options.put(CardSlotTypeEnum.SWIPE, bundle);
			cardSlotManager.setConfig(options);// 设置卡槽参数
			/**
			 * 读取卡信息
			 * slotTypes 轮询的卡槽设备集
			 * cardTypes 卡类型集
			 * timeout 轮询超时时间
			 * listener 读卡结果回调实例
			 */
			cardSlotManager.readCard(slotTypes, cardTypes, timeout,
					new OnCardInfoListener() {
						
						@Override
						public void onCardInfo(int arg0, CardInfoEntity arg1) {
							if (0 != arg0) {
								operaListener.onFail(-1, "刷卡失败", null);
							} else {
								switch (arg1.getActuralEnterType()) {
								case MAG_CARD:
									if (null != operaListener) {
										String[] result = new String[] {
												arg1.getTk1(), arg1.getTk2(),
												arg1.getTk3() };
										operaListener
												.onSuccess(0, "成功", result);
									}
									stopDevice();
									break;
								default:
									operaListener.onFail(-1, "刷卡失败", null);
									break;
								}
							}
						}
					}, null);
		} catch (SdkException e) {
			operaListener.onThrowException(new DeviceExceptionBean(e));
			e.printStackTrace();
		} catch (CallServiceException e) {
			operaListener.onThrowException(new DeviceExceptionBean(e));
			e.printStackTrace();
		}
	}
	
	@Override
	public String getOnFailMsg(String code) {
		return null;
	}
}
