package com.storemax.android.devices.unipay;

import com.google.gson.Gson;
import com.huiyi.nypos.pay.thirdpay.ConstrantParam;
import com.huiyi.nypos.pay.thirdpay.ThirdTransRequest;
import com.huiyi.nypos.pay.thirdpay.TranseType;
import com.huiyi.nypos.pay.thirdpay.aidl.ThirdParam;
import com.storemax.android.devices.DeviceFactory;
import com.storemax.android.devices.Utils;
import com.storemax.android.devices.bean.NYPosResponeBean;
import com.storemax.android.devices.bean.UnipayExtObjBean;
import com.storemax.android.devices.bean.UnipayResultBean;
import com.storemax.android.devices.exception.BussNoErrorException;
import com.storemax.android.devices.exception.NotSupportException;
import com.storemax.android.devices.unipay.activity.NYPosTransActivity;
import com.storemax.android.devices.unipay.utils.NYPosUtils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class NYPosUnipay extends GeneralUnipay {

	public static final String APP_ID = "0000001";// 汇宜提供
	public static final String APP_VALUE = "123456";// 汇宜提供

	private static final String ALIPAY = "ALIPAY";
	private static final String WECHAT = "WECHAT";

	private Gson gson = null;

	@Override
	public void unipayRegist(Activity context) throws NotSupportException {
		DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
		Intent intent = new Intent();
		intent.setComponent(getUnipayComponentName());
		ThirdTransRequest request = new ThirdTransRequest();
		request.setOrder_id(NYPosUtils.getOrderId());
		request.setTran_cd(TranseType.ACTIVE_FLOW);
		String data = createJson(request);
		String signature = "";
		try {
			signature = NYPosUtils.encryptHMAC(data, APP_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ThirdParam param = new ThirdParam();
		param.setApp_id(APP_ID);
		param.setSignature(signature);
		param.setData(data);
		intent.putExtra(ConstrantParam.THIRDPARAM, createJson(param));
		context.startActivityForResult(intent, BaseUnipay.UNIPAY_REQUEST_CODE_REGIST);
	}

	@Override
	public void unipayLogin(Activity context) {
		DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
		Intent intent = new Intent();
		intent.setComponent(getUnipayComponentName());
		ThirdTransRequest request = new ThirdTransRequest();
		request.setTran_cd(TranseType.SIGN_TYPE);
		request.setOrder_id(NYPosUtils.getOrderId());
		String data = createJson(request);
		String signature = "";
		try {
			signature = NYPosUtils.encryptHMAC(data, APP_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ThirdParam param = new ThirdParam();
		param.setApp_id(APP_ID);
		param.setSignature(signature);
		param.setData(data);
		intent.putExtra(BaseUnipay.PARAM_TRANS_NAME, TransName.LOGIN);
		intent.putExtra(ConstrantParam.THIRDPARAM, createJson(param));
		context.startActivityForResult(intent, BaseUnipay.UNIPAY_REQUEST_CODE_LOGIN);
	}

	@Override
	public void unipayConsume(Activity context, int money, String orderNo, Object ext) {
		DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
		Intent intent = new Intent(context, NYPosTransActivity.class);
		ThirdTransRequest request = new ThirdTransRequest();
		request.setTran_cd(getConsumeParamName());
		request.setIsPrint("1");
		request.setTran_amt(NYPosUtils.consueMoney(money));
		request.setOrder_id(orderNo);
		String data = createJson(request);

		String signature = "";
		try {
			signature = NYPosUtils.encryptHMAC(data, APP_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ThirdParam param = new ThirdParam();
		param.setApp_id(APP_ID);
		param.setSignature(signature);
		param.setData(data);
		intent.putExtra(ConstrantParam.THIRDPARAM, createJson(param));
		context.startActivityForResult(intent, BaseUnipay.UNIPAY_REQUEST_CODE_CONSUME);
	}

	@Override
	public void unipayCancel(Activity context, String tradeNo, Object ext) {
		DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
		if (ext instanceof UnipayExtObjBean) {
			UnipayExtObjBean extObj = (UnipayExtObjBean) ext;
			Intent intent = new Intent(context, NYPosTransActivity.class);
			ThirdTransRequest request = new ThirdTransRequest();
			request.setTran_cd(getCancelParamName());
			request.setOrig_sys_order_id(extObj.getOrginOrderId());
			request.setOrig_term_seq(tradeNo);
			request.setIsPrint("1");
			request.setTran_amt(NYPosUtils.consueMoney(extObj.getMoney()));
			request.setOrder_id(extObj.getOrderId());
			String data = createJson(request);
			String signature = "";
			try {
				signature = NYPosUtils.encryptHMAC(data, APP_VALUE);
			} catch (Exception e) {
				e.printStackTrace();
			}

			ThirdParam param = new ThirdParam();
			param.setApp_id(APP_ID);
			param.setSignature(signature);
			param.setData(data);
			intent.putExtra(ConstrantParam.THIRDPARAM, createJson(param));
			context.startActivityForResult(intent, BaseUnipay.UNIPAY_REQUEST_CODE_CANCEL);
		}
	}

	@Override
	public void unipayBalance(Activity context, Object ext) {
		DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
		Intent intent = new Intent(context, NYPosTransActivity.class);
		ThirdTransRequest request = new ThirdTransRequest();
		request.setTran_cd(TranseType.BANLANCE_TYPE);
		request.setOrder_id(NYPosUtils.getOrderId());
		String data = createJson(request);
		String signature = "";
		try {
			signature = NYPosUtils.encryptHMAC(data, APP_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ThirdParam param = new ThirdParam();
		param.setApp_id(APP_ID);
		param.setSignature(signature);
		param.setData(data);
		intent.putExtra(ConstrantParam.THIRDPARAM, createJson(param));
		context.startActivityForResult(intent, 1);
	}

	@Override
	public void unipayAccounts(Activity context, Object ext) {
		DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
		Intent intent = new Intent(context, NYPosTransActivity.class);
		ThirdTransRequest request = new ThirdTransRequest();
		request.setTran_cd(TranseType.SETTLE);
		request.setOrder_id(NYPosUtils.getOrderId());
		String data = createJson(request);
		String signature = "";
		try {
			signature = NYPosUtils.encryptHMAC(data, APP_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ThirdParam param = new ThirdParam();
		param.setApp_id(APP_ID);
		param.setSignature(signature);
		param.setData(data);
		intent.putExtra(ConstrantParam.THIRDPARAM, createJson(param));
		context.startActivityForResult(intent, BaseUnipay.UNIPAY_REQUEST_CODE_ACCOUNTS);
	}

	@Override
	public void unipaySysManager(Activity context, Object ext) throws NotSupportException {
		throw new NotSupportException();
	}

	@Override
	protected String getConsumeParamName() {
		return TranseType.CONSUME_TYPE;
	}

	@Override
	protected String getCancelParamName() {
		return TranseType.REVOKE_TYPE;
	}

	@Override
	protected String getTransName(int name) {
		return "";
	}

	@Override
	public void wecharPay(Activity context, String scanCode, String orderNum, int money, Object ext)
			throws NotSupportException, BussNoErrorException {
		DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
		Intent intent = new Intent(context, NYPosTransActivity.class);
		ThirdTransRequest request = new ThirdTransRequest();
		request.setTran_cd(TranseType.SCAN_PAY_TYPE);
		request.setIsPrint("1");
		request.setScan_auth_tp(WECHAT);
		request.setIns_order_id(scanCode);
		request.setTran_amt(NYPosUtils.consueMoney(money));
		request.setOrder_id(orderNum);
		String data = createJson(request);
		String signature = "";
		try {
			signature = NYPosUtils.encryptHMAC(data, APP_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ThirdParam param = new ThirdParam();
		param.setApp_id(APP_ID);
		param.setSignature(signature);
		param.setData(data);
		intent.putExtra(ConstrantParam.THIRDPARAM, createJson(param));
		context.startActivityForResult(intent, BaseUnipay.WECHART_REQUEST_CODE);

	}

	@Override
	public void alipay(Activity context, String scanCode, String orderNum, int money, Object ext)
			throws NotSupportException, BussNoErrorException {
		DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
		Intent intent = new Intent(context, NYPosTransActivity.class);
		ThirdTransRequest request = new ThirdTransRequest();
		request.setTran_cd(TranseType.SCAN_PAY_TYPE);
		request.setIsPrint("1");
		request.setScan_auth_tp(ALIPAY);
		request.setIns_order_id(scanCode);
		request.setTran_amt(NYPosUtils.consueMoney(money));
		request.setOrder_id(orderNum);
		String data = createJson(request);
		String signature = "";

		try {
			signature = NYPosUtils.encryptHMAC(data, APP_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ThirdParam param = new ThirdParam();
		param.setApp_id(APP_ID);
		param.setSignature(signature);
		param.setData(data);
		intent.putExtra(ConstrantParam.THIRDPARAM, createJson(param));
		context.startActivityForResult(intent, BaseUnipay.WECHART_REQUEST_CODE);

	}

	@Override
	public void wechartCancel(Activity context, String tradeNo, Object ext) throws NotSupportException {
		DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
		if (ext instanceof UnipayExtObjBean) {
			UnipayExtObjBean extObj = (UnipayExtObjBean) ext;
			Intent intent = new Intent(context, NYPosTransActivity.class);
			ThirdTransRequest request = new ThirdTransRequest();
			request.setTran_cd(TranseType.SCAN_REVOKE_TYPE);
			request.setOrig_sys_order_id(extObj.getOrginOrderId());
			request.setOrig_term_seq(tradeNo);
			request.setIsPrint("1");
			request.setScan_auth_tp(WECHAT);
			request.setTran_amt(NYPosUtils.consueMoney(extObj.getMoney()));

			request.setOrder_id(extObj.getOrderId());
			String data = createJson(request);
			String signature = "";
			try {
				signature = NYPosUtils.encryptHMAC(data, APP_VALUE);
			} catch (Exception e) {
				e.printStackTrace();
			}

			ThirdParam param = new ThirdParam();
			param.setApp_id(APP_ID);
			param.setSignature(signature);
			param.setData(data);
			intent.putExtra(ConstrantParam.THIRDPARAM, createJson(param));
			context.startActivityForResult(intent, BaseUnipay.WECHART_CANCEL_REQUEST_CODE);
		}
	}

	@Override
	public void alipayCancel(Activity context, String tradeNo, Object ext) throws NotSupportException {
		DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
		if (ext instanceof UnipayExtObjBean) {
			UnipayExtObjBean extObj = (UnipayExtObjBean) ext;
			Intent intent = new Intent(context, NYPosTransActivity.class);
			ThirdTransRequest request = new ThirdTransRequest();
			request.setTran_cd(TranseType.SCAN_REVOKE_TYPE);
			request.setOrig_sys_order_id(extObj.getOrginOrderId());
			request.setOrig_term_seq(tradeNo);
			request.setIsPrint("1");
			request.setScan_auth_tp(ALIPAY);
			request.setTran_amt(NYPosUtils.consueMoney(extObj.getMoney()));

			request.setOrder_id(extObj.getOrderId());
			String data = createJson(request);
			String signature = "";
			try {
				signature = NYPosUtils.encryptHMAC(data, APP_VALUE);
			} catch (Exception e) {
				e.printStackTrace();
			}

			ThirdParam param = new ThirdParam();
			param.setApp_id(APP_ID);
			param.setSignature(signature);
			param.setData(data);
			intent.putExtra(ConstrantParam.THIRDPARAM, createJson(param));
			context.startActivityForResult(intent, BaseUnipay.ALIPAY_CANCEL_REQUEST_CODE);
		}

	}

	@Override
	public void unipayOrderStatus(Activity context, String orderNum, TradeType type, Object obj)
			throws NotSupportException {
		DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
		Intent intent = new Intent(context, NYPosTransActivity.class);
		ThirdTransRequest request = new ThirdTransRequest();
		request.setTran_cd("931");
		if (null != obj && obj instanceof UnipayExtObjBean) {
			UnipayExtObjBean extObj = (UnipayExtObjBean) obj;
			request.setOrig_sys_order_id(extObj.getOrginOrderId());
		}
		request.setIsPrint("1");
		String data = createJson(request);
		String signature = "";
		try {
			signature = NYPosUtils.encryptHMAC(data, APP_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ThirdParam param = new ThirdParam();
		param.setApp_id(APP_ID);
		param.setSignature(signature);
		param.setData(data);
		intent.putExtra(ConstrantParam.THIRDPARAM, createJson(param));
		context.startActivityForResult(intent, BaseUnipay.UNIPAY_GET_ORDER_STATUS_REQUEST_CODE);

	}

	@Override
	public UnipayResultBean getResultInfo(Intent intent) {
		if (intent.getBooleanExtra(BaseUnipay.IS_RESULT, false)) {
			return (UnipayResultBean) intent.getSerializableExtra(BaseUnipay.INTENT_RESULT);
		} else {
			UnipayResultBean result = new UnipayResultBean();
			result.resultCode = UnipayResultBean.UNKNOW_ERROR;
			result.reason = "银联接口返回错误,请重试！";
			if (null != intent) {
				String respCd = intent.getStringExtra(ConstrantParam.RESP_CD);
				if (!TextUtils.isEmpty(respCd)) {
					if (intent.getStringExtra(ConstrantParam.RESP_CD).equals("00")) {
						String response = intent.getStringExtra(ConstrantParam.THIRDPARAM);
						Log.e("MyLog", response);
						NYPosResponeBean thirdTransResponse = (NYPosResponeBean) getObject(response,
								NYPosResponeBean.class);
						if (null != thirdTransResponse) {
							result.traceNo = thirdTransResponse.getTerm_seq();
							result.orderNo = thirdTransResponse.getTerm_seq();
							result.referenceNo = thirdTransResponse.getBefer_no(); // 参考号
							result.sysOrderNo = thirdTransResponse.getSys_order_id();
							if (!TextUtils.isEmpty(thirdTransResponse.getTran_amt())) {
								result.amount = Integer.parseInt(thirdTransResponse.getTran_amt());
							}
							result.reason = thirdTransResponse.getResp_msg();
							result.resultCode = UnipayResultBean.UNIPAY_RESULT_SUCCESS;
							result.cardNo = thirdTransResponse.getPri_acct_no();
						}
					} else if (intent.getStringExtra(ConstrantParam.RESP_CD).equals("C1")) {
						result.resultCode = UnipayResultBean.UNIPAY_RESULT_NEED_LOGIN;
					} else if (intent.getStringExtra(ConstrantParam.RESP_CD).equals("203")
							|| intent.getStringExtra(ConstrantParam.RESP_CD).equals("98")
							|| intent.getStringExtra(ConstrantParam.RESP_CD).equals("205")) {
						result.resultCode = UnipayResultBean.UNIPAY_RESULT_NEED_RETRY;
					}
				}
			}
			return result;
		}
	}

	@Override
	public UnipayResultBean getOrderStatusResult(Intent intent) throws NotSupportException {
		throw new NotSupportException();
	}

	@Override
	public void unipayPrint(Activity context, String tradeNo, Object ext) {
		DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
		if (TextUtils.isEmpty(tradeNo)) {
			// 调用打印最后一笔
			try {
				unipayLastPrint(context, ext);
			} catch (NotSupportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Intent intent = new Intent();
			intent.setComponent(getUnipayComponentName());
			ThirdTransRequest request = new ThirdTransRequest();
			request.setOrder_id(NYPosUtils.getOrderId());
			request.setOrig_sys_order_id(tradeNo);
			request.setTran_cd(TranseType.ANY_REPRINT);
			String data = createJson(request);
			String signature = "";
			try {
				signature = NYPosUtils.encryptHMAC(data, APP_VALUE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ThirdParam param = new ThirdParam();
			param.setApp_id(APP_ID);
			param.setSignature(signature);
			param.setData(data);
			intent.putExtra(ConstrantParam.THIRDPARAM, createJson(param));
			context.startActivityForResult(intent, BaseUnipay.UNIPAY_REQUEST_CODE_PRINT);
		}
	}

	@Override
	public void unipayLastPrint(Activity context, Object ext) throws NotSupportException {
		DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
		Intent intent = new Intent();
		intent.setComponent(getUnipayComponentName());
		ThirdTransRequest request = new ThirdTransRequest();
		request.setOrder_id(NYPosUtils.getOrderId());
		request.setTran_cd(TranseType.LAST_REPRINT);
		String data = createJson(request);
		String signature = "";
		try {
			signature = NYPosUtils.encryptHMAC(data, APP_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ThirdParam param = new ThirdParam();
		param.setApp_id(APP_ID);
		param.setSignature(signature);
		param.setData(data);
		intent.putExtra(ConstrantParam.THIRDPARAM, createJson(param));
		context.startActivityForResult(intent, BaseUnipay.UNIPAY_REQUEST_CODE_PRINT);
	}

	@Override
	public UnipayType getUnipayType() {
		return UnipayType.NY_POS;
	}

	private String createJson(Object obj) {
		if (null == gson) {
			gson = new Gson();
		}
		return gson.toJson(obj);
	}

	private Object getObject(String json, Class<?> type) {
		if (null == gson) {
			gson = new Gson();
		}
		return gson.fromJson(json, type);
	}
}
