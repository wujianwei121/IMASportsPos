package com.storemax.android.devices.bean;

import com.storemax.android.devices.rfcard.BaseRFCard.KeyType;

public class RFCardAuthBean {
	/**
	 * 扇区号
	 */
	private int sectorNo;
	/**
	 * 密钥
	 */
	private byte[] pwd;
	/**
	 * 密钥类型
	 */
	private KeyType keyType = KeyType.KEY_A;
	
	public int getSectorNo() {
		return sectorNo;
	}
	
	public byte[] getPwd() {
		return pwd;
	}
	
	public KeyType getKeyType() {
		return keyType;
	}
	
	public void setSectorNo(int sectorNo) {
		this.sectorNo = sectorNo;
	}
	
	public void setPwd(byte[] pwd) {
		this.pwd = pwd;
	}
	
	public void setKeyType(KeyType keyType) {
		this.keyType = keyType;
	}
	
}
