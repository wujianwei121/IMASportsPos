package com.storemax.android.devices.unipay;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.storemax.android.devices.DeviceFactory;
import com.storemax.android.devices.bean.UnipayResultBean;
import com.storemax.android.devices.exception.BussNoErrorException;
import com.storemax.android.devices.exception.NotSupportException;

/**
 * @Title: SpdMobileUnipay.java
 * @Package com.storemax.android.devices.unpiay
 * @copyright ◎2002-2007 Nanjing StoreMax Tech. Co. Ltd. All Rights Reserved.
 * @Description: 浦发银联O2O支付
 * @author A18ccms A18ccms_gmail_com
 * @date 2016年12月16日 下午2:07:06
 * @version V1.0
 */
public class SpdMobileUnipay extends GeneralUnipay {
	
	private static final String NEED_RETRY = "needQuery";
	private static final String CANCEL = "-";
	private static final String CONSUME = "*";
	
	@Override
	public int getUnipayConsumeRequestCode() {
		return UNIPAY_REQUEST_CODE_CONSUME;
	}
	
	@Override
	public int getUnipayCancelRequestCode() {
		return UNIPAY_REQUEST_CODE_CANCEL;
	}
	
	@Override
	public int getWechartRequestCode() {
		return WECHART_REQUEST_CODE;
	}
	
	@Override
	public int getAlipayRequestCode() {
		return ALIPAY_REQUEST_CODE;
	}
	
	@Override
	protected String getConsumeParamName() {
		return "amount";
	}
	
	@Override
	protected String getCancelParamName() {
		return "oldTrace";
	}
	
	@Override
	protected String getTransName(int name) {
		switch (name) {
		case TransName.LOGIN:
			return "O2O签到";
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
		case TransName.SCAN_CODE:
			return "O2O主扫";
		case TransName.PRINT:
			return "打印";
			
		}
		return "";
	}
	
	@Override
	public void wecharPay(Activity context, String scanCode,String orderNum, int money,
			Object ext) throws BussNoErrorException {
		DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
		if (scanCode.startsWith("13")) {
			Intent intent = new Intent();
			intent.setComponent(getMobileComponentName());
			intent.putExtra("transName", getTransName(TransName.SCAN_CODE));
			formatConsumeMoney(intent, "amount", money);
			intent.putExtra("code", scanCode);
			context.startActivityForResult(intent,
					BaseUnipay.WECHART_REQUEST_CODE);
		} else {
			throw new BussNoErrorException();
		}
	}
	
	@Override
	public void alipay(Activity context,String scanCode, String orderNum, int money, Object ext)
			throws BussNoErrorException {
		DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
		if (scanCode.startsWith("28")) {
			Intent intent = new Intent();
			intent.setComponent(getMobileComponentName());
			intent.putExtra("transName", getTransName(TransName.SCAN_CODE));
			formatConsumeMoney(intent, "amount", money);
			intent.putExtra("code", scanCode);
			context.startActivityForResult(intent, BaseUnipay.ALIPAY_REQUEST_CODE);
		} else {
			throw new BussNoErrorException();
		}
	}
	
	@Override
	public void wechartCancel(Activity context, String tradeNo, Object ext) {
		DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
		Intent intent = new Intent();
		intent.setComponent(getMobileComponentName());
		intent.putExtra("transName", "O2O扫码撤销");
		intent.putExtra("oldOrderNo", tradeNo);
		
		context.startActivityForResult(intent,
				BaseUnipay.WECHART_CANCEL_REQUEST_CODE);
		
	}
	
	@Override
	public void alipayCancel(Activity context, String tradeNo, Object ext) {
		DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
		Intent intent = new Intent();
		intent.setComponent(getMobileComponentName());
		intent.putExtra("transName", "O2O扫码撤销");
		intent.putExtra("oldOrderNo", tradeNo);
		
		context.startActivityForResult(intent,
				BaseUnipay.ALIPAY_CANCEL_REQUEST_CODE);
		
	}
	
	@Override
	protected void setConsumeOrderInfo(Intent intent, String orderNo) {
		// 不需要，不实现
		
	}
	
	@Override
	public UnipayType getUnipayType() {
		return UnipayType.SPD_MOBILE;
	}
	
	@Override
	public UnipayResultBean getResultInfo(Intent intent) {
		UnipayResultBean result = new UnipayResultBean();
		
		result.resultCode = UnipayResultBean.UNIPAY_RESULT_SUCCESS;
		String amt = intent.getStringExtra("amount");
		String cardNo = intent.getStringExtra("cardNo");
		String refNo = intent.getStringExtra("referenceNo");
		String traceNo = intent.getStringExtra("traceNo");
		String tradeNo = intent.getStringExtra("tradeNo");
		String reason = intent.getStringExtra("reason");
		String orderNo = intent.getStringExtra("orderNo");
		String payType = intent.getStringExtra("tradeType");
		if (!TextUtils.isEmpty(amt)) {
			result.amount = Integer.parseInt(amt);
		}
		if (!TextUtils.isEmpty(cardNo)) {
			result.cardNo = cardNo;
		}
		if (!TextUtils.isEmpty(refNo)) {
			result.referenceNo = refNo;
		}
		if (!TextUtils.isEmpty(traceNo)) {
			result.traceNo = traceNo;
		}
		if (!TextUtils.isEmpty(tradeNo)) {
			result.tradeNo = tradeNo;
		}
		if (!TextUtils.isEmpty(reason)) {
			result.reason = reason;
			if (reason.equalsIgnoreCase(NEED_RETRY)) {
				result.resultCode = UnipayResultBean.UNIPAY_RESULT_NEED_RETRY;
			}
		}
		
		if (!TextUtils.isEmpty(orderNo)) {
			result.orderNo = orderNo;
		}
		
		if (!TextUtils.isEmpty(payType)) {
			if (payType.equals("支付宝")) {
				result.payType = UnipayResultBean.PAY_TYPE_ALIPAY;
			} else if (payType.equals("微信支付")) {
				result.payType = UnipayResultBean.PAY_TYPE_WECHART;
			} else {
				result.payType = UnipayResultBean.PAY_TYPE_UNIPAY_CARD;
			}
		}
		
		return result;
	}
	
	@Override
	public void unipayOrderStatus(Activity context, String orderNum,
			TradeType type, Object extObj) throws NotSupportException {
		DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
		Intent intent = new Intent();
		intent.setComponent(getUnipayComponentName());
		intent.putExtra("transName", "O2O按订单号查询");
		intent.putExtra("orderNo", orderNum);
		if (type == TradeType.CANCEL) {
			intent.putExtra("status", CANCEL);
		} else if (type == TradeType.CONSUME) {
			intent.putExtra("status", CONSUME);
		}
		context.startActivityForResult(intent,
				BaseUnipay.UNIPAY_GET_ORDER_STATUS_REQUEST_CODE);
		
	}
	
	@Override
	public UnipayResultBean getOrderStatusResult(Intent intent)
			throws NotSupportException {
		UnipayResultBean result = new UnipayResultBean();
		
		String amt = intent.getStringExtra("amount");
		String cardNo = intent.getStringExtra("cardNo");
		String refNo = intent.getStringExtra("referenceNo");
		String traceNo = intent.getStringExtra("traceNo");
		String tradeNo = intent.getStringExtra("tradeNo");
		String reason = intent.getStringExtra("reason");
		String orderNo = intent.getStringExtra("orderNo");
		String payType = intent.getStringExtra("tradeType");
		String statusCode = intent.getStringExtra("statusCode");
		
		result.resultCode = getOrderStatusCode(statusCode);
		
		if (!TextUtils.isEmpty(amt)) {
			result.amount = Integer.parseInt(amt);
		}
		if (!TextUtils.isEmpty(cardNo)) {
			result.cardNo = cardNo;
		}
		if (!TextUtils.isEmpty(refNo)) {
			result.referenceNo = refNo;
		}
		if (!TextUtils.isEmpty(traceNo)) {
			result.traceNo = traceNo;
		}
		if (!TextUtils.isEmpty(tradeNo)) {
			result.tradeNo = tradeNo;
		}
		if (!TextUtils.isEmpty(reason)) {
			result.reason = reason;
			if (reason.equalsIgnoreCase(NEED_RETRY)) {
				result.resultCode = UnipayResultBean.UNIPAY_RESULT_NEED_RETRY;
			}
		}
		if (!TextUtils.isEmpty(orderNo)) {
			result.orderNo = orderNo;
		}
		
		if (!TextUtils.isEmpty(payType)) {
			if (payType.equals("支付宝")) {
				result.payType = UnipayResultBean.PAY_TYPE_ALIPAY;
			} else if (payType.equals("微信支付")) {
				result.payType = UnipayResultBean.PAY_TYPE_WECHART;
			} else {
				result.payType = UnipayResultBean.PAY_TYPE_UNIPAY_CARD;
			}
			
		}
		return result;
	}
	
	private String getOrderStatusCode(String code) {
		
		if ("0000".equals(code)) {
			return UnipayResultBean.CONSUME_SUCCESS;
		} else if ("0001".equals(code)) {
			return UnipayResultBean.CONSUME_FAIL;
		} else if ("0002".equals(code)) {
			return UnipayResultBean.CONSUME_CANCEL;
		} else if ("0003".equals(code)) {
			return UnipayResultBean.CONSUME_REFOUND;
		} else if ("0004".equals(code)) {
			return UnipayResultBean.CONSUME_CLOSED;
		} else if ("0005".equals(code)) {
			return UnipayResultBean.CONSUME_CHONGCHENG;
		} else if ("0900".equals(code)) {
			return UnipayResultBean.SYSTEM_ERROR;
		}
		return UnipayResultBean.UNKNOW_ERROR;
	}
}
