package com.storemax.android.devices.bean;

public class UnipayExtObjBean {

	/**
	 * 第三访应用ID
	 */
	private String orderId;
	/**
	 * 原第三方应用ID 用在撤单上
	 */
	private String orginOrderId;
	/**
	 * 相关交易金额 以分为单位
	 */
	private int money = 0;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrginOrderId() {
		return orginOrderId;
	}

	public void setOrginOrderId(String orginOrderId) {
		this.orginOrderId = orginOrderId;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
}