package com.storemax.android.devices.rfcard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.storemax.android.devices.bean.DeviceExceptionBean;
import com.storemax.android.devices.bean.RFCardAuthBean;
import com.storemax.android.devices.bean.RFCardBlockBean;
import com.storemax.android.devices.device.IDevice;
import com.storemax.android.devices.listener.OnDeviceOperaListener;
import com.storemax.android.devices.listener.OnDeviceStateListener;
import com.storemax.android.devices.listener.OnRFCardReadListener;
import com.storemax.android.devices.listener.OnRFCardSearchListener;
import com.storemax.android.devices.listener.OnRFCardWriteListener;
import com.ums.upos.sdk.card.m1.AuthEntity;
import com.ums.upos.sdk.card.m1.BlockEntity;
import com.ums.upos.sdk.card.m1.KeyTypeEnum;
import com.ums.upos.sdk.card.m1.M1CardManager;
import com.ums.upos.sdk.cardslot.CardInfoEntity;
import com.ums.upos.sdk.cardslot.CardSlotManager;
import com.ums.upos.sdk.cardslot.CardSlotTypeEnum;
import com.ums.upos.sdk.cardslot.CardTypeEnum;
import com.ums.upos.sdk.cardslot.OnCardInfoListener;
import com.ums.upos.sdk.cardslot.SwipeSlotOptions;
import com.ums.upos.sdk.exception.CallServiceException;
import com.ums.upos.sdk.exception.SdkException;
import com.ums.upos.sdk.system.BaseSystemManager;

public class UnipayA8RFCard extends BaseRFCard {
	OnRFCardSearchListener searchListener;
	OnDeviceOperaListener operaListener;
	Activity activity;
	private String currentCardNo;
	private CardSlotManager cardSlotManager = null;
	IDevice idevice;
	
	public UnipayA8RFCard(IDevice idevice) {
		this.idevice = idevice;
	}
	
	@Override
	public void openDevice(Activity activity,
			final OnDeviceStateListener listener) {
		this.activity = activity;
		idevice.openDevice(activity, listener);
	}
	
	@Override
	public void searchCard(OnRFCardSearchListener listener) throws Exception {
		searchListener = listener;
		cardSlotManager = new CardSlotManager();
		Set<CardSlotTypeEnum> slotTypes = new HashSet<CardSlotTypeEnum>();// 轮询的卡槽设备集
		slotTypes.add(CardSlotTypeEnum.RF);// 非接卡卡槽
		Set<CardTypeEnum> cardTypes = new HashSet<CardTypeEnum>();// 卡类型集
		cardTypes.add(CardTypeEnum.M1CARD);// M1CARD
		int timeout = 5000;
		
		Map<CardSlotTypeEnum, Bundle> options = new HashMap<CardSlotTypeEnum, Bundle>();
		Bundle bundle = new Bundle();
		bundle.putBoolean(SwipeSlotOptions.LRC_CHECK, false);
		options.put(CardSlotTypeEnum.RF, bundle);
		cardSlotManager.setConfig(options);// 设置卡槽参数
		cardSlotManager.readCard(slotTypes, cardTypes, timeout,
				readCardInfoListener, null);
		
	}
	
	public void setOnDeviceOperaListener(OnDeviceOperaListener listener) {
		this.operaListener = listener;
	}
	
	@Override
	public void stopDevice() throws Exception {
		cardSlotManager.stopRead();
	}
	
	@Override
	public void closeDevice() throws Exception {
		this.operaListener = null;
		BaseSystemManager.getInstance().deviceServiceLogout();
	}
	
	@Override
	public void readBlock(RFCardAuthBean auth, RFCardBlockBean block,
			OnRFCardReadListener listener) {
		try {
			M1CardManager m1CardManager = new M1CardManager();
			
			AuthEntity authEntity = new AuthEntity();
			authEntity.setBlkNo(auth.getSectorNo());// 待认证的块号
			authEntity.setKeyType(getKeyType(auth.getKeyType()));// 密钥类型
			authEntity.setPwd(auth.getPwd());// 块密码
			authEntity.setSerialNo(currentCardNo);// 卡片序列号
			
			m1CardManager.authority(authEntity);
			
			BlockEntity blockEntity = new BlockEntity();
			blockEntity.setBlkData(block.getBlkData());// 块数据
			blockEntity.setBlkNo(block.getBlkNo());// 块号
			
			int result = m1CardManager.readBlock(blockEntity);// 读取块数据
			if (result == 0) {
				if (listener != null) {
					listener.onSuccess(blockEntity.getBlkData());
				}
			} else {
				if (listener != null) {
					listener.onFail(result, getErrorMsgByCode(result), null);
				}
			}
		} catch (SdkException e) {
			if (listener != null) {
				listener.onThrowException(new DeviceExceptionBean(e));
			}
			e.printStackTrace();
		} catch (CallServiceException e) {
			if (listener != null) {
				listener.onThrowException(new DeviceExceptionBean(e));
			}
			e.printStackTrace();
		}
	}
	
	private OnCardInfoListener readCardInfoListener = new OnCardInfoListener() {
		
		@Override
		public void onCardInfo(int arg0, CardInfoEntity arg1) {
			if (0 != arg0) {
				if (searchListener != null) {
					searchListener.onFail(BaseRFCard.SEARCH_CARD_FAIL, "寻卡失败！",
							null);
				}
			} else {
				
				switch (arg1.getActuralEnterType()) {
				case M1CARD:
					if (searchListener != null) {
						currentCardNo = arg1.getCardNo();
						if ((null == currentCardNo)
								|| TextUtils.isEmpty(currentCardNo)) {
							searchListener.onFail(BaseRFCard.SEARCH_CARD_FAIL,
									"卡内码获取错误", null);
						} else {
							
							searchListener.onSuccess();
						}
					}
					break;
				default:
					if (searchListener != null) {
						searchListener.onFail(BaseRFCard.SEARCH_CARD_FAIL,
								"寻卡失败,该卡不是M1卡！", null);
					}
					break;
				}
			}
		}
		
	};
	
	@Override
	public void writeBlock(RFCardAuthBean auth, RFCardBlockBean block,
			OnRFCardWriteListener listener) {
		try {
			M1CardManager m1CardManager = new M1CardManager();
			
			AuthEntity authEntity = new AuthEntity();
			authEntity.setBlkNo(auth.getSectorNo());// 待认证的块号
			authEntity.setKeyType(getKeyType(auth.getKeyType()));// 密钥类型
			authEntity.setPwd(auth.getPwd());// 块密码
			authEntity.setSerialNo(currentCardNo);// 卡片序列号
			int result = m1CardManager.authority(authEntity);
			BlockEntity blockEntity = new BlockEntity();
			blockEntity.setBlkData(block.getBlkData());// 块数据
			blockEntity.setBlkNo(block.getBlkNo());// 块号
			result = m1CardManager.writeBlock(blockEntity);
			if (listener != null) {
				if (0 == result) {
					listener.onSuccess();
				} else {
					listener.onFail(result, "", null);
				}
			}
			
		} catch (SdkException e) {
			if (listener != null) {
				listener.onThrowException(new DeviceExceptionBean(e));
			}
			e.printStackTrace();
		} catch (CallServiceException e) {
			if (listener != null) {
				listener.onThrowException(new DeviceExceptionBean(e));
			}
			e.printStackTrace();
		}
	}
	
	@Override
	public void writeCardNumber(String cardNo) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String readCardNumber() {
		// TODO Auto-generated method stub
		return currentCardNo;
	}
	
	public KeyTypeEnum getKeyType(KeyType type) {
		switch (type) {
		case KEY_A: {
			return KeyTypeEnum.KEY_A;
		}
		case KEY_B: {
			return KeyTypeEnum.KEY_B;
		}
		}
		return null;
	}
	
	@Override
	public String getErrorMsgByCode(int code) {
		// TODO Auto-generated method stub
		return "";
	}
}
