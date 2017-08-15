package com.storemax.android.devices.unipay;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.xml;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.storemax.android.devices.bean.UnipayResultBean;
import com.storemax.android.devices.exception.BussNoErrorException;
import com.storemax.android.devices.exception.NotSupportException;
import com.ums.AppHelper;

public class UmsA8Unipay extends BaseUnipay {
	
	@Override
	public void unipayLogin(Activity context) throws NotSupportException {
		throw new NotSupportException();
		
	}
	
	@Override
	public void unipayConsume(Activity context, int money, String orderNo, Object ext) throws NotSupportException {
		Intent intent = new Intent();
		intent.setAction("com.ums.transcontroller.call");
		intent.putExtra("appName", "银行卡收款");
		intent.putExtra("transId", "消费");
		intent.putExtra("transData", "{\"amt\":\"" + money + "\"}");
		context.startActivityForResult(intent, UNIPAY_REQUEST_CODE_CONSUME);
		
	}
	
	@Override
	public void wecharPay(Activity context, String scanCode, String orderNum, int money, Object ext) throws NotSupportException, BussNoErrorException {
		Intent intent = new Intent();
		intent.setAction("com.ums.transcontroller.call");
		intent.putExtra("appName", "POS 通");
		intent.putExtra("transId", "POS通");
		intent.putExtra("transData", "{\"amt\":\"" + money + "\",\"tradeType\":\"useScan\"}");
		context.startActivityForResult(intent, WECHART_REQUEST_CODE);
	}
	
	@Override
	public void alipay(Activity context, String scanCode, String orderNum, int money, Object ext) throws NotSupportException, BussNoErrorException {
		Intent intent = new Intent();
		intent.setAction("com.ums.transcontroller.call");
		intent.putExtra("appName", "POS 通");
		intent.putExtra("transId", "POS通");
		intent.putExtra("transData", "{\"amt\":\"" + money + "\",\"tradeType\":\"useScan\"}");
		context.startActivityForResult(intent, ALIPAY_REQUEST_CODE);
		
	}
	
	@Override
	public void unipayCancel(Activity context, String tradeNo, Object ext) {
		Intent intent = new Intent();
		intent.setAction("com.ums.transcontroller.call");
		intent.putExtra("appName", "银行卡收款");
		intent.putExtra("transId", "撤销");
		intent.putExtra("transData", "{\"orgTraceNo\":\"" + tradeNo + "\"}");
		context.startActivityForResult(intent, UNIPAY_REQUEST_CODE_CANCEL);
		
	}
	
	@Override
	public void wechartCancel(Activity context, String orderNo, Object ext) throws NotSupportException {
		Intent intent = new Intent();
		intent.setAction("com.ums.transcontroller.call");
		intent.putExtra("appName", "POS 通");
		intent.putExtra("transId", "消费撤销");
		intent.putExtra("transData", "{\"oldTraceNo\":\"" + orderNo + "\"}");
		context.startActivityForResult(intent, WECHART_CANCEL_REQUEST_CODE);
		
	}
	
	@Override
	public void alipayCancel(Activity context, String orderNo, Object ext) throws NotSupportException {
		Intent intent = new Intent();
		intent.setAction("com.ums.transcontroller.call");
		intent.putExtra("appName", "POS 通");
		intent.putExtra("transId", "消费撤销");
		intent.putExtra("transData", "{\"oldTraceNo\":\"" + orderNo + "\"}");
		context.startActivityForResult(intent, ALIPAY_CANCEL_REQUEST_CODE);
		
	}
	
	@Override
	public void unipayBalance(Activity context, Object ext) throws NotSupportException {
		Intent intent = new Intent();
		intent.setAction("com.ums.transcontroller.call");
		intent.putExtra("appName", "银行卡收款");
		intent.putExtra("transId", "余额查询");
		intent.putExtra("transData", "{}");
		context.startActivityForResult(intent, UNIPAY_REQUEST_CODE_BALANCE);
	}
	
	@Override
	public void unipayAccounts(Activity context, Object ext) throws NotSupportException {
		
		String ver = getSubVersion(context);
		if (!TextUtils.isEmpty(ver)) {
			if (ver.startsWith("2")) {
				Intent intent = new Intent();
				intent.setAction("com.ums.transcontroller.call");
				intent.putExtra("appName", "银行卡收款");
				intent.putExtra("transId", "结算");
				intent.putExtra("transData", "{}");
				context.startActivityForResult(intent, UNIPAY_REQUEST_CODE_ACCOUNTS);
			} else {
				Intent intent = new Intent();
				intent.setAction("com.ums.transcontroller.call");
				intent.putExtra("appName", "公共资源");
				intent.putExtra("transId", "换班");
				intent.putExtra("transData", "{}");
				context.startActivityForResult(intent, UNIPAY_REQUEST_CODE_ACCOUNTS);
			}
		}
	}
	
	@Override
	public void unipaySysManager(Activity context, Object ext) throws NotSupportException {
		throw new NotSupportException();
		
	}
	
	@Override
	public void unipayPrint(Activity context, String tradeNo, Object ext) throws NotSupportException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void unipayTradeQuery(Activity context, Object ext) throws NotSupportException {
		
	}
	
	@Override
	public UnipayType getUnipayType() {
		return UnipayType.UNIPAY_UMS_A8;
	}
	
	@Override
	protected ComponentName getUnipayComponentName() {
		// 没有用
		return null;
	}
	
	@Override
	protected void setConsumeOrderInfo(Intent intent, String orderNo) {
		// 没有用
		
	}
	
	@Override
	public UnipayResultBean getResultInfo(Intent intent) {
		UnipayResultBean resultBean = new UnipayResultBean();
		
		if (null != intent) {
			
			String result = intent.getStringExtra("result");
			if ((result == null) || (result.isEmpty())) {
				return resultBean;
			}
			System.out.println(result);
			try {
				JSONObject json = new JSONObject(result);
				String resultCode = json.getString("resultCode");
				if (resultCode.equals("0")) {
					// 状态为0才有接口的返回信息
					JSONObject info = new JSONObject(json.getString("transData"));
					String resCode = info.getString("resCode");
					if (resCode.equals("00")) {
						resultBean.resultCode = UnipayResultBean.UNIPAY_RESULT_SUCCESS;
						resultBean.amount = (int) (info.optDouble("amt") * 100); // 交易金额
						// resultBean.//info.getString("batchNo"); //批次号
						resultBean.traceNo = info.optString("traceNo"); // 凭证号
						resultBean.referenceNo = info.optString("refNo"); // 参考号
						resultBean.cardNo = info.optString("cardNo"); // 卡号
						resultBean.orderNo = info.optString("orderNo");
						// info.getString("cardIssuerCode"); //发卡行ID
						// info.getString("cardAcquirerCode"); //收单行ID
						String payType = "";
						String strMemInfo = info.optString("memInfo");
						
						if (!TextUtils.isEmpty(strMemInfo)) {
							JSONObject memInfo = new JSONObject(strMemInfo);
							if (null != memInfo) {
								resultBean.orderNo = memInfo.optString("orderNo");
								payType = memInfo.optString("channelName"); // 支付渠道
							}
						}
						if (!TextUtils.isEmpty(payType)) {
							if (payType.contains("微信")) {
								resultBean.payType = UnipayResultBean.PAY_TYPE_WECHART;
							} else if (payType.contains("支付宝")) {
								resultBean.payType = UnipayResultBean.PAY_TYPE_ALIPAY;
							} else {
								resultBean.payType = UnipayResultBean.PAY_TYPE_UNIPAY_CARD;
							}
						} else {
							resultBean.payType = UnipayResultBean.PAY_TYPE_UNIPAY_CARD;
						}
						
					} else {
						resultBean.resultCode = UnipayResultBean.UNIPAY_RESULT_ERROR;
						resultBean.reason = info.optString("resDesc");
					}
				} else {
					// 直接返回错误码和错误信息
					resultBean.resultCode = UnipayResultBean.UNIPAY_RESULT_ERROR;
					resultBean.reason = json.optString("resultMsg");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
			resultBean.resultCode = UnipayResultBean.UNIPAY_RESULT_ERROR;
			resultBean.reason = "";
		}
		return resultBean;
	}
	
	@Override
	public void unipayOrderStatus(Activity context, String orderNum, TradeType type, Object extObj) throws NotSupportException {
		Intent intent = new Intent();
		intent.setAction("com.ums.transcontroller.call");
		intent.putExtra("appName", "POS 通");
		intent.putExtra("transId", "订单查询");
		intent.putExtra("transData", "{\"tradeType\":\"manual\",\"orderNo\":\"" + orderNum + "\"}");
		context.startActivityForResult(intent, UNIPAY_GET_ORDER_STATUS_REQUEST_CODE);
		
	}
	
	@Override
	public UnipayResultBean getOrderStatusResult(Intent intent) throws NotSupportException {
		UnipayResultBean resultBean = new UnipayResultBean();
		
		String result = intent.getStringExtra("result");
		if ((result == null) || (result.isEmpty())) {
			return resultBean;
		}
		System.out.println(result);
		try {
			JSONObject json = new JSONObject(result);
			String resultCode = json.getString("resultCode");
			if (resultCode.equals("0")) {
				// 状态为0才有接口的返回信息
				JSONObject info = new JSONObject(json.getString("transData"));
				String resCode = info.getString("resCode");
				resultBean.resultCode = UnipayResultBean.UNIPAY_RESULT_ERROR;
				resultBean.reason = info.optString("resDesc");
				if (resCode.equals("00")) {
					resultBean.resultCode = UnipayResultBean.UNIPAY_RESULT_SUCCESS;
					resultBean.amount = (int) (info.optDouble("amt") * 100); // 交易金额
					// resultBean.//info.getString("batchNo"); //批次号
					resultBean.traceNo = info.optString("traceNo"); // 凭证号
					resultBean.referenceNo = info.optString("refNo"); // 参考号
					resultBean.cardNo = info.optString("cardNo"); // 卡号
					resultBean.orderNo = info.optString("orderNo");
					// info.getString("cardIssuerCode"); //发卡行ID
					// info.getString("cardAcquirerCode"); //收单行ID
					String payType = "";
					String strMemInfo = info.optString("memInfo");
					
					if (!TextUtils.isEmpty(strMemInfo)) {
						JSONObject memInfo = new JSONObject(strMemInfo);
						if (null != memInfo) {
							resultBean.orderNo = memInfo.optString("orderNo");
							payType = memInfo.optString("channelName"); // 支付渠道
						}
					}
					if (!TextUtils.isEmpty(payType)) {
						if (payType.contains("微信")) {
							resultBean.payType = UnipayResultBean.PAY_TYPE_WECHART;
						} else if (payType.contains("支付宝")) {
							resultBean.payType = UnipayResultBean.PAY_TYPE_ALIPAY;
						} else {
							resultBean.payType = UnipayResultBean.PAY_TYPE_UNIPAY_CARD;
						}
					} else {
						resultBean.payType = UnipayResultBean.PAY_TYPE_UNIPAY_CARD;
					}
					String orderStatus = info.optString("orgTradeState");
					String statusMsg = info.optString("orgTradeMsg");
					if (!TextUtils.isEmpty(orderStatus)) {
						if ("0".equals(orderStatus)) {
							// 成功
							resultBean.resultCode = UnipayResultBean.CONSUME_SUCCESS;
							resultBean.reason = statusMsg;
						} else if ("1".equals(orderStatus)) {
							// 1：超时
							resultBean.resultCode = UnipayResultBean.UNIPAY_RESULT_NEED_RETRY;
							resultBean.reason = statusMsg;
						} else if ("2".equals(orderStatus)) {
							// 已撤销
							resultBean.resultCode = UnipayResultBean.CONSUME_CANCEL;
							resultBean.reason = statusMsg;
						} else if ("3".equals(orderStatus)) {
							// 已退货
							resultBean.resultCode = UnipayResultBean.CONSUME_REFOUND;
							resultBean.reason = statusMsg;
						} else if ("4".equals(orderStatus)) {
							// 已冲正
							resultBean.resultCode = UnipayResultBean.CONSUME_CHONGCHENG;
							resultBean.reason = statusMsg;
						} else if ("5".equals(orderStatus)) {
							// 失败
							resultBean.resultCode = UnipayResultBean.CONSUME_FAIL;
							resultBean.reason = statusMsg;
						}
					}
				} else {
					resultBean.resultCode = UnipayResultBean.UNIPAY_RESULT_ERROR;
					resultBean.reason = info.optString("resDesc");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return resultBean;
	}
	
	@Override
	public void unipayLastPrint(Activity context, Object ext) throws NotSupportException {
		throw new NotSupportException();
	}
	
	@Override
	public void startUpdateActivity(Context context, String pkgName) throws NotSupportException {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=" + pkgName));
		ComponentName cn = new ComponentName("com.ums.appstore", "com.appmarket.details.DetailsActivity");
		intent.setComponent(cn);
		context.startActivity(intent);
	}
	
	@Override
	public String getSubVersion(Context context) throws NotSupportException {
		try {
			String json = AppHelper.getBaseSysInfo(context);
			JSONObject object = new JSONObject(json);
			return object.getString("SoftwareVer");
		} catch (Exception ex) {
			return "";
		}
	}
	
}
