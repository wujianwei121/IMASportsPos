package com.storemax.android.devices.bean;

/**
 * 交易调用的返回类
 * @author Administrator
 *
 */
public class NYPosResponeBean {
	//响应码
	private String resp_cd;
	//响应信息
	private String resp_msg;
	//交易码
	private  String tran_cd;
	//商户号
	private String mcht_cd;
	//终端号
	private  String term_id;
	//终端流水号
	private String term_seq;
	//终端批次号
	private String term_batch;
	//交易日期
	private String tran_dt_tm;
	//商户订单号
	private String order_id;
	//系统订单号
	private String sys_order_id;
	//账号
	private String pri_acct_no;
	//交易金额
	private String tran_amt;
	//渠道订单号
	private String ins_order_id;
	//扫码渠道	
	private String qr_type;
	//清算日期
	private String sett_dt;
	//系统订单号
	private String befer_no;
	//预授权号
	private String pre_auth_id;
	//签名
	private String sign_img;
	//卡有效期
	private String card_exp; 
	//订单信息或者
	private String order_str;
	private String open_id;//扫码交易
	//付款银行
	private String pay_bank;
	//支付宝用户ID
	private String buyer_id;
	
	
	public String getResp_cd() {
		return resp_cd;
	}
	public void setResp_cd(String resp_cd) {
		this.resp_cd = resp_cd;
	}
	public String getResp_msg() {
		return resp_msg;
	}
	public void setResp_msg(String resp_msg) {
		this.resp_msg = resp_msg;
	}
	public String getMcht_cd() {
		return mcht_cd;
	}
	public void setMcht_cd(String mcht_cd) {
		this.mcht_cd = mcht_cd;
	}
	public String getTerm_id() {
		return term_id;
	}
	public void setTerm_id(String term_id) {
		this.term_id = term_id;
	}
	public String getTerm_seq() {
		return term_seq;
	}
	public void setTerm_seq(String term_seq) {
		this.term_seq = term_seq;
	}
	public String getTerm_batch() {
		return term_batch;
	}
	public void setTerm_batch(String term_batch) {
		this.term_batch = term_batch;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getSys_order_id() {
		return sys_order_id;
	}
	public void setSys_order_id(String sys_order_id) {
		this.sys_order_id = sys_order_id;
	}
	public String getTran_dt_tm() {
		return tran_dt_tm;
	}
	public void setTran_dt_tm(String tran_dt_tm) {
		this.tran_dt_tm = tran_dt_tm;
	}
	public String getPri_acct_no() {
		return pri_acct_no;
	}
	public void setPri_acct_no(String pri_acct_no) {
		this.pri_acct_no = pri_acct_no;
	}
	public String getTran_amt() {
		return tran_amt;
	}
	public void setTran_amt(String tran_amt) {
		this.tran_amt = tran_amt;
	}
	public String getIns_order_id() {
		return ins_order_id;
	}
	public void setIns_order_id(String ins_order_id) {
		this.ins_order_id = ins_order_id;
	}
	public String getQr_type() {
		return qr_type;
	}
	public void setQr_type(String qr_type) {
		this.qr_type = qr_type;
	}
	public String getPre_auth_id() {
		return pre_auth_id;
	}
	public void setPre_auth_id(String pre_auth_id) {
		this.pre_auth_id = pre_auth_id;
	}
	public String getSign_img() {
		return sign_img;
	}
	public void setSign_img(String sign_img) {
		this.sign_img = sign_img;
	}
	public String getCard_exp() {
		return card_exp;
	}
	public void setCard_exp(String card_exp) {
		this.card_exp = card_exp;
	}
	public String getOrder_str() {
		return order_str;
	}
	public void setOrder_str(String order_str) {
		this.order_str = order_str;
	}
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
	public String getPay_bank() {
		return pay_bank;
	}
	public void setPay_bank(String pay_bank) {
		this.pay_bank = pay_bank;
	}
	public String getBuyer_id() {
		return buyer_id;
	}
	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}
	public String getTran_cd() {
		return tran_cd;
	}
	public void setTran_cd(String tran_cd) {
		this.tran_cd = tran_cd;
	}
	public String getSett_dt() {
		return sett_dt;
	}
	public void setSett_dt(String sett_dt) {
		this.sett_dt = sett_dt;
	}
	public String getBefer_no() {
		return befer_no;
	}
	public void setBefer_no(String befer_no) {
		this.befer_no = befer_no;
	}
}
