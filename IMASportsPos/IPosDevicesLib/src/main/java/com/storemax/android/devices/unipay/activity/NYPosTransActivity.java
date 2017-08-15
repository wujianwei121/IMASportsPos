package com.storemax.android.devices.unipay.activity;

import com.huiyi.nypos.pay.thirdpay.ConstrantParam;
import com.huiyi.nypos.pay.thirdpay.TranseType;
import com.storemax.android.devices.DeviceFactory;
import com.storemax.android.devices.bean.UnipayResultBean;
import com.storemax.android.devices.exception.NotSupportException;
import com.storemax.android.devices.unipay.BaseUnipay;
import com.storemax.android.devices.unipay.BaseUnipay.TransName;
import com.storemax.android.devices.unipay.BaseUnipay.UnipayType;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * 
 * @author Blue
 *
 */
public class NYPosTransActivity extends Activity {
	/**
	 * 是否要自动签到
	 */
	private boolean isNeedLogin = false;

	BaseUnipay unipay;

	private static final String IS_NEED_LOGIN = "IS_NEED_LOGIN";
	private Handler handler = new Handler();
	private String inparam = null;
	private int transName = TransName.UNKNOWN;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		unipay = DeviceFactory.getDeviceInstance().getUnipay();
		if (null != savedInstanceState) {
			isNeedLogin = savedInstanceState.getBoolean(IS_NEED_LOGIN);
			inparam = savedInstanceState.getString(ConstrantParam.THIRDPARAM);
			transName = savedInstanceState.getInt(BaseUnipay.PARAM_TRANS_NAME);
		} else {
			isNeedLogin = false;
			Intent intent = getIntent();
			inparam = intent.getStringExtra(ConstrantParam.THIRDPARAM);
			transName = intent.getIntExtra(BaseUnipay.PARAM_TRANS_NAME, TransName.UNKNOWN);
		}

		handler.postDelayed(startUnipay, 1000);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(ConstrantParam.THIRDPARAM, inparam);
		outState.putSerializable(BaseUnipay.PARAM_TRANS_NAME, transName);
		super.onSaveInstanceState(outState);
	}

	private Runnable startUnipay = new Runnable() {

		@Override
		public void run() {
			Intent intent = new Intent();
			intent.putExtra(ConstrantParam.THIRDPARAM, inparam);
			intent.setComponent(
					new ComponentName(UnipayType.NY_POS.getPackageName(), UnipayType.NY_POS.getActivityName()));
			NYPosTransActivity.this.startActivityForResult(intent, BaseUnipay.UNIPAY_REQUEST_CODE_CONSUME);
		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			UnipayResultBean result = unipay.getResultInfo(data);
			if (null != result) {
				if (result.resultCode.equals(UnipayResultBean.UNIPAY_RESULT_NEED_LOGIN)) {
					try {
						isNeedLogin = true;
						unipay.unipayLogin(this);
					} catch (NotSupportException e) {
						e.printStackTrace();
					}
				} else if (transName != TransName.LOGIN && isNeedLogin
						&& result.resultCode.equals(UnipayResultBean.UNIPAY_RESULT_SUCCESS)) {
					isNeedLogin = false;
					handler.post(startUnipay);
				} else {
					Intent intent = new Intent();
					intent.putExtra(BaseUnipay.IS_RESULT, true);
					intent.putExtra(BaseUnipay.INTENT_RESULT, result);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		} else {
			setResult(RESULT_CANCELED);
			finish();
		}

	}
}
