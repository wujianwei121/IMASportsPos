package com.storemax.android.devices.unipay;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.storemax.android.devices.bean.UnipayResultBean;
import com.storemax.android.devices.exception.BussNoErrorException;
import com.storemax.android.devices.exception.NotSupportException;

public abstract class BaseUnipay {
	
	public int getUnipayConsumeRequestCode() {
		return UNIPAY_REQUEST_CODE_CONSUME;
	}
	
	public int getUnipayCancelRequestCode() {
		return UNIPAY_REQUEST_CODE_CANCEL;
	}
	
	public int getWechartRequestCode() {
		return WECHART_REQUEST_CODE;
	}
	
	public int getAlipayRequestCode() {
		return ALIPAY_REQUEST_CODE;
	}
	
	public int getWechartCancelRequestCode() {
		return WECHART_CANCEL_REQUEST_CODE;
	}
	
	public int getAlipayCancelRequestCode() {
		return ALIPAY_CANCEL_REQUEST_CODE;
	}
	
	public int getUnipayBalanceRequestCode() {
		return UNIPAY_REQUEST_CODE_BALANCE;
	}
	
	private static final int UNIPAY_REQUEST_CODE_BASE_CODE = 0xffff;
	
	/**
	 * 银联签到
	 */
	public static final int UNIPAY_REQUEST_CODE_LOGIN = UNIPAY_REQUEST_CODE_BASE_CODE - 1;
	/**
	 * 银联消费
	 */
	public static final int UNIPAY_REQUEST_CODE_CONSUME = UNIPAY_REQUEST_CODE_BASE_CODE - 2;
	/**
	 * 银联撤单
	 */
	public static final int UNIPAY_REQUEST_CODE_CANCEL = UNIPAY_REQUEST_CODE_BASE_CODE - 3;
	/**
	 * 银联余额查询
	 */
	public static final int UNIPAY_REQUEST_CODE_BALANCE = UNIPAY_REQUEST_CODE_BASE_CODE - 4;
	/**
	 * 银联结算
	 */
	public static final int UNIPAY_REQUEST_CODE_ACCOUNTS = UNIPAY_REQUEST_CODE_BASE_CODE - 5;
	/**
	 * 系统管理
	 */
	public static final int UNIPAY_REQUEST_CODE_SYSMANAGE = UNIPAY_REQUEST_CODE_BASE_CODE - 6;
	/**
	 * 打印
	 */
	public static final int UNIPAY_REQUEST_CODE_PRINT = UNIPAY_REQUEST_CODE_BASE_CODE - 7;
	/**
	 * 微信支付请求
	 */
	public static final int WECHART_REQUEST_CODE = UNIPAY_REQUEST_CODE_BASE_CODE - 8;
	/**
	 * 支付宝支付请求
	 */
	public static final int ALIPAY_REQUEST_CODE = UNIPAY_REQUEST_CODE_BASE_CODE - 9;
	/**
	 * 微信撤单请求
	 */
	public static final int WECHART_CANCEL_REQUEST_CODE = UNIPAY_REQUEST_CODE_BASE_CODE - 10;
	/**
	 * 支付宝撤单请求
	 */
	public static final int ALIPAY_CANCEL_REQUEST_CODE = UNIPAY_REQUEST_CODE_BASE_CODE - 11;
	/**
	 * 交易查询
	 */
	public static final int UNIPAY_TRADE_QUERY__REQUEST_CODE = UNIPAY_REQUEST_CODE_BASE_CODE - 12;
	/**
	 * 交易单号查询
	 */
	public static final int UNIPAY_GET_ORDER_STATUS_REQUEST_CODE = UNIPAY_REQUEST_CODE_BASE_CODE - 13;
	/**
	 * 银联设备注册
	 */
	public static final int UNIPAY_REQUEST_CODE_REGIST = UNIPAY_REQUEST_CODE_BASE_CODE - 14;
	
	/**
	 * 注册
	 */
	public void unipayRegist(Activity context) throws NotSupportException {
		throw new NotSupportException();
	}
	
	/**
	 * 签到
	 */
	public abstract void unipayLogin(Activity context) throws NotSupportException;
	
	/**
	 * 消费
	 */
	public abstract void unipayConsume(Activity context, int money, String orderNo, Object ext) throws NotSupportException;;
	
	/**
	 * 微信支付
	 */
	public abstract void wecharPay(Activity context, String scanCode, String orderNum, int money, Object ext) throws NotSupportException, BussNoErrorException;
	
	/**
	 * 支付宝支付
	 */
	public abstract void alipay(Activity context, String scanCode, String orderNum, int money, Object ext) throws NotSupportException, BussNoErrorException;
	
	/**
	 * 撤单
	 */
	public abstract void unipayCancel(Activity context, String tradeNo, Object ext);
	
	/**
	 * @Title: wechartCancel
	 * @Description: 微信支付撤销
	 * @param context
	 * @param orderNo
	 */
	public abstract void wechartCancel(Activity context, String orderNo, Object ext) throws NotSupportException;
	
	/**
	 * @Title: alipayCancel
	 * @Description: 支付宝支付撤销
	 * @param context
	 * @param orderNo
	 */
	public abstract void alipayCancel(Activity context, String orderNo, Object ext) throws NotSupportException;
	
	/**
	 * 查询余额
	 */
	public abstract void unipayBalance(Activity context, Object ext) throws NotSupportException;
	
	/**
	 * 结算
	 */
	public abstract void unipayAccounts(Activity context, Object ext) throws NotSupportException;
	
	/**
	 * 系统管理
	 */
	public abstract void unipaySysManager(Activity context, Object ext) throws NotSupportException;
	
	/**
	 * 小票打印
	 */
	public abstract void unipayPrint(Activity context, String tradeNo, Object ext) throws NotSupportException;
	
	/**
	 * 默认打印最后一张小票
	 */
	public abstract void unipayLastPrint(Activity context, Object ext) throws NotSupportException;
	
	/**
	 * 交易查询
	 */
	public abstract void unipayTradeQuery(Activity context, Object ext) throws NotSupportException;
	
	/**
	 * 支付状态查询
	 */
	public abstract void unipayOrderStatus(Activity context, String orderNum, TradeType type, Object extObj) throws NotSupportException;
	
	/**
	 * 支付状态结果查询
	 */
	public abstract UnipayResultBean getOrderStatusResult(Intent intent) throws NotSupportException;
	
	/**
	 * @Title: startUpdateActivity
	 * @Description:
	 * @param context
	 * @throws NotSupportException
	 */
	public abstract void startUpdateActivity(Context context,String pkgName) throws NotSupportException;
	
	public String getSubVersion(Context context) throws NotSupportException{
		throw new NotSupportException();
	}
	/**
	 * 获取当前主包名
	 */
	public abstract UnipayType getUnipayType();
	
	/**
	 * 根据具体应用，获取其ComponentName
	 */
	protected abstract ComponentName getUnipayComponentName();
	
	protected abstract void setConsumeOrderInfo(Intent intent, String orderNo);
	
	public abstract UnipayResultBean getResultInfo(Intent intent);
	
	public static final String PARAM_TRANS_NAME = "TRANS_NAME";
	
	public interface TransName {
		public int UNKNOWN = -1;
		public int LOGIN = 0;
		public int CONSUME = 1;
		public int CANCEL = 2;
		public int BALANCE = 3;
		public int ACCOUNTS = 4;
		public int SYSMANAGE = 5;
		public int SCAN_CODE = 6;
		public int PRINT = 7;
		public int WECHART = 8;
		public int ALIPAY = 9;
		public int TRADE_QUERY = 10;
	}
	
	/**
	 * 当前返回的Intent是否已经包含结果
	 */
	public static final String IS_RESULT = "IS_RESULT";
	public static final String INTENT_RESULT = "INTENT_RESULT";
	
	public enum TradeType {
		/**
		 * 消费
		 */
		CONSUME,
		/**
		 * 撤消
		 */
		CANCEL
	}
	
	public enum UnipayType {
		AUTO("", "", ""),
		UNKNOWN("", "", ""),
		UNIPAY_UMS_A8("银联商务", "com.ums.upos.uapi", ""),
		SPD_A8("浦发支付_A8", "com.landicorp.android.spdpay", "com.landicorp.android.spdpay.MainActivity"),
		SPD_MOBILE("浦发移动支付", "com.landicorp.android.unionpay.spd.qrcode", "com.landicorp.android.unionpay.spd.qrcode.MainActivity"),
		P990("P990", "com.landicorp.android.payservice", "com.landicorp.android.payservice.MainActivity"),
		NY_POS("NYPos", "com.huiyi.nypos.pay", "com.huiyi.nypos.pay.thirdpay.activity.ThirdPayActivity"),
		WO_A8_UNIPAY("沃银A8银联支付","com.landicorp.android.unionpay","com.landicorp.android.unionpay.MainActivity");
		/**
		 * 名称
		 */
		private String unipayName;
		/**
		 * 主包名
		 */
		private String packageName;
		/**
		 * activity名
		 */
		private String activityName;

		/**
		 * 支付方式
		 * @param unipayName
		 * @param packageName
		 * @param activityName
		 */
		private UnipayType(String unipayName, String packageName, String activityName) {
			this.unipayName = unipayName;
			this.packageName = packageName;
			this.activityName = activityName;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @see Enum#toString()
		 */
		@Override
		public String toString() {
			return this.unipayName;
		}
		
		/**
		 * 获取银联支付方式名称
		 * 
		 * @Title: getMethodName
		 * @return
		 */
		public String getUnipayName() {
			return this.unipayName;
		}
		
		/**
		 * 获取银联支付方式名称
		 * 
		 * @Title: getMethodName
		 * @return
		 */
		public String getPackageName() {
			return this.packageName;
		}
		
		public String getActivityName() {
			return this.activityName;
		}
		
	}
	
}
