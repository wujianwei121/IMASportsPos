package com.storemax.android.devices.bean;

import java.io.Serializable;

import com.storemax.android.devices.unipay.BaseUnipay.TransName;

public class UnipayResultBean implements Serializable {

	/**
	 * 
	 */
	public String resultCode = UNIPAY_RESULT_SUCCESS;
	/**
	 * 
	 */
	public String payType = "";
	/**
	 * 
	 */
	public int amount = 0;
	/**
	 * 6位凭证号
	 */
	public String traceNo = "";
	/**
	 * 参考号
	 */
	public String referenceNo = "";
	/**
	 * 扫码的交易流水号
	 */
	public String tradeNo = "";
	/**
	 * 
	 */
	public String cardNo = "";
	/**
	 * 
	 */
	public String tradeType = "";
	/**
	 * 
	 */
	public String reason = "";
	/**
	 * 
	 */
	public String orderNo = "";
	/**
	 * 系统订单号，用于返回浦发宜汇系统订单
	 */
	public String sysOrderNo = "";
	/**
	 * 
	 */
	public Object ext;

	/**
	 * 银联结果未知，需要查询
	 */
	public static final String UNIPAY_RESULT_NEED_RETRY = "UNIPAY_RESULT_NEED_RETRY";
	/**
	 * 银联结果未知，需要查询
	 */
	public static final String UNIPAY_RESULT_NEED_LOGIN = "UNIPAY_RESULT_NEED_LOGIN";
	/**
	 * 消费成功
	 */
	public static final String CONSUME_SUCCESS = "CONSUME_SUCCESS";
	/**
	 * 消费失败
	 */
	public static final String CONSUME_FAIL = "CONSUME_FAIL";
	/**
	 * 消费退款
	 */
	public static final String CONSUME_REFOUND = "CONSUME_REFOUND";
	/**
	 * 消费被撤销
	 */
	public static final String CONSUME_CANCEL = "CONSUME_CANCEL";
	/**
	 * 消费超时
	 */
	public static final String CONSUME_OVERTIME = "CONSUME_OVERTIME";
	/**
	 * 消费被关闭
	 */
	public static final String CONSUME_CLOSED = "CONSUME_CLOSED";
	/**
	 * 消费被冲正
	 */
	public static final String CONSUME_CHONGCHENG = "CONSUME_CHONGCHENG";
	/**
	 * 系统错误
	 */
	public static final String SYSTEM_ERROR = "SYSTEM_ERROR";
	/**
	 * 未定义的错误
	 */
	public static final String UNKNOW_ERROR = "UNKNOW_ERROR";

	public static final String UNIPAY_RESULT_SUCCESS = "0";

	public static final String UNIPAY_RESULT_ERROR = "1";

	public static final String PAY_TYPE_WECHART = "微信";

	public static final String PAY_TYPE_ALIPAY = "支付宝";

	public static final String PAY_TYPE_UNIPAY_CARD = "银行卡";

}
