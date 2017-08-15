package com.storemax.android.devices.unipay;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import com.storemax.android.devices.bean.UnipayResultBean;
import com.storemax.android.devices.exception.NotSupportException;

public class P990Unipay extends GeneralUnipay {
	
	@Override
	protected String getConsumeParamName() {
		// TODO Auto-generated method stub
		return "amount";
	}
	
	@Override
	protected String getCancelParamName() {
		// TODO Auto-generated method stub
		return "oldTrace";
	}
	
	@Override
	protected String getTransName(int name) {
		switch (name) {
		case TransName.LOGIN:
			return "签到";
		case TransName.CONSUME:
			return "消费";
		case TransName.CANCEL:
			return "消费撤销";
		case TransName.TRADE_QUERY:
			return "交易查询";
		case TransName.BALANCE:
			return "余额查询";
		case TransName.ACCOUNTS:
			return "结算";
		case TransName.SYSMANAGE:
			return "系统管理";
		case TransName.PRINT:
			return "打印";
		}
		return "";
	}
	
	@Override
	public void wecharPay(Activity context, String scanCode,String orderNum, int money,
			Object ext) throws NotSupportException {
		throw new NotSupportException();
	}
	
	@Override
	public void alipay(Activity context, String scanCode,String orderNum, int money, Object ext)
			throws NotSupportException {
		throw new NotSupportException();
		
	}
	
	@Override
	public void wechartCancel(Activity context, String orderNo, Object ext)
			throws NotSupportException {
		throw new NotSupportException();
		
	}
	
	@Override
	public void alipayCancel(Activity context, String orderNo, Object ext)
			throws NotSupportException {
		throw new NotSupportException();
		
	}
	
	@Override
	public UnipayType getUnipayType() {
		// TODO Auto-generated method stub
		return UnipayType.P990;
	}

	@Override
	public void unipayOrderStatus(Activity context, String orderNum,
			TradeType type, Object extObj) throws NotSupportException {
		
		throw new NotSupportException();
		
	}

	@Override
	public UnipayResultBean getOrderStatusResult(Intent intent)
			throws NotSupportException {
		throw new NotSupportException();
	}

	
}
