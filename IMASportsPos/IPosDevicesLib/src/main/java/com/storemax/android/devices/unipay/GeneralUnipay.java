package com.storemax.android.devices.unipay;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.storemax.android.devices.DeviceFactory;
import com.storemax.android.devices.bean.UnipayResultBean;
import com.storemax.android.devices.exception.NotSupportException;

public abstract class GeneralUnipay extends BaseUnipay {

    @Override
    public void unipayLogin(Activity context) {
        DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
        Intent intent = new Intent();
        intent.setComponent(getUnipayComponentName());
        intent.putExtra("transName", getTransName(TransName.LOGIN));
        context.startActivityForResult(intent, BaseUnipay.UNIPAY_REQUEST_CODE_LOGIN);

    }

    @Override
    public void unipayConsume(Activity context, int money, String orderNo, Object ext) {
        DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
        Intent intent = new Intent();
        intent.setComponent(getUnipayComponentName());
        intent.putExtra("transName", getTransName(TransName.CONSUME));
        setConsumeOrderInfo(intent, orderNo);
        formatConsumeMoney(intent, getConsumeParamName(), money);
        context.startActivityForResult(intent, BaseUnipay.UNIPAY_REQUEST_CODE_CONSUME);

    }

    @Override
    public void unipayCancel(Activity context, String tradeNo, Object ext) {
        DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
        Intent intent = new Intent();
        intent.setComponent(getUnipayComponentName());
        intent.putExtra("transName", getTransName(TransName.CANCEL));
        intent.putExtra(getCancelParamName(), tradeNo);
        context.startActivityForResult(intent, BaseUnipay.UNIPAY_REQUEST_CODE_CANCEL);

    }

    @Override
    public void unipayBalance(Activity context, Object ext) {
        DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
        Intent intent = new Intent();
        intent.setComponent(getUnipayComponentName());
        intent.putExtra("transName", getTransName(TransName.BALANCE));
        context.startActivityForResult(intent, BaseUnipay.UNIPAY_REQUEST_CODE_BALANCE);

    }

    @Override
    public void unipayAccounts(Activity context, Object ext) {
        DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
        Intent intent = new Intent();
        intent.setComponent(getUnipayComponentName());
        intent.putExtra("transName", getTransName(TransName.ACCOUNTS));
        context.startActivityForResult(intent, BaseUnipay.UNIPAY_REQUEST_CODE_ACCOUNTS);

    }

    @Override
    public void unipaySysManager(Activity context, Object ext) throws NotSupportException {
        DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
        Intent intent = new Intent();
        intent.setComponent(getUnipayComponentName());
        intent.putExtra("transName", getTransName(TransName.SYSMANAGE));
        context.startActivityForResult(intent, BaseUnipay.UNIPAY_REQUEST_CODE_SYSMANAGE);

    }

    @Override
    public void unipayPrint(Activity context, String tradeNo, Object ext) {
        DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
        Intent intent = new Intent();
        intent.setComponent(getUnipayComponentName());
        intent.putExtra("transName", getTransName(TransName.PRINT));
        context.startActivityForResult(intent, BaseUnipay.UNIPAY_REQUEST_CODE_PRINT);

    }

    @Override
    public void unipayLastPrint(Activity context, Object ext) throws NotSupportException {
        throw new NotSupportException();
    }

    @Override
    public void unipayTradeQuery(Activity context, Object ext) {
        DeviceFactory.getDeviceInstance().getIDevice().closeDevice();
        Intent intent = new Intent();
        intent.setComponent(getUnipayComponentName());
        intent.putExtra("transName", getTransName(TransName.TRADE_QUERY));
        context.startActivityForResult(intent, BaseUnipay.UNIPAY_TRADE_QUERY__REQUEST_CODE);
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

    protected abstract String getConsumeParamName();

    protected abstract String getCancelParamName();

    protected abstract String getTransName(int name);

    protected void formatConsumeMoney(Intent intent, String key, int money) {
        intent.putExtra(key, String.format("%012d", money));
    }

    @Override
    protected void setConsumeOrderInfo(Intent intent, String orderNo) {

    }

    /**
     * 银联支付
     * @return
     */
    @Override
    protected ComponentName getUnipayComponentName() {
        return new ComponentName(UnipayType.WO_A8_UNIPAY.getPackageName(), UnipayType.WO_A8_UNIPAY.getActivityName());
    }

    /**
     * 微信或支付宝支付
     * @return
     */
    protected ComponentName getMobileComponentName() {
        return new ComponentName(UnipayType.SPD_MOBILE.getPackageName(), UnipayType.SPD_MOBILE.getActivityName());
    }

    @Override
    public void startUpdateActivity(Context context, String pkgName) throws NotSupportException {
        throw new NotSupportException();
    }

}
