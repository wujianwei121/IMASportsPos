package com.storemax.android.devices.device;

import android.app.Activity;
import android.content.Context;

import com.landicorp.android.eptapi.DeviceService;
import com.landicorp.android.eptapi.exception.ReloginException;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.exception.ServiceOccupiedException;
import com.landicorp.android.eptapi.exception.UnsupportMultiProcess;
import com.landicorp.android.eptapi.utils.SystemInfomation;
import com.storemax.android.devices.DeviceFactory;
import com.storemax.android.devices.DeviceFactory.DeviceType;
import com.storemax.android.devices.bean.DeviceExceptionBean;
import com.storemax.android.devices.listener.OnDeviceInfoReceivedListener;
import com.storemax.android.devices.listener.OnDeviceOperaListener;
import com.storemax.android.devices.listener.OnDeviceStateListener;
import com.storemax.android.devices.magcard.BaseMagcard;
import com.storemax.android.devices.magcard.LandiMagcard;
import com.storemax.android.devices.printer.BasePrinter;
import com.storemax.android.devices.printer.LandiPrinter;
import com.storemax.android.devices.rfcard.BaseRFCard;
import com.storemax.android.devices.rfcard.LandiRfCard;
import com.storemax.android.devices.unipay.BaseUnipay;

public class LandiDevice extends BaseDevice implements IDevice {
    private DeviceType deviceType;

    public LandiDevice(DeviceType dType) {
        deviceType = dType;
    }

    @Override
    public void getDeviceSN(Context context,
                            OnDeviceInfoReceivedListener listener) {
        if (null != listener)
            listener.getDeviceSn(SystemInfomation.getDeviceInfo().getSerialNo());
    }

    @Override
    public BasePrinter getPrinter(Activity activity,
                                  OnDeviceOperaListener lister) {
        return new LandiPrinter(activity, lister, this);
    }

    @Override
    public BaseRFCard getRFCard() {
        return new LandiRfCard(this);
    }

    @Override
    public BaseMagcard getMagcard() {
        return new LandiMagcard(this);
    }

    @Override
    public void openDevice(Context context, OnDeviceStateListener listener) {

        try {
            DeviceService.login(context);
            if (null != listener) {
                listener.onSuccess(DeviceFactory.SUCCESS, "成功", null);
            }
        } catch (RequestException e) {
            if (null != listener) {
                listener.onThrowException(new DeviceExceptionBean(e));
            }
            e.printStackTrace();
        } catch (ServiceOccupiedException e) {
            if (null != listener) {
                listener.onThrowException(new DeviceExceptionBean(e));
            }
            e.printStackTrace();
        } catch (ReloginException e) {
            if (null != listener) {
                listener.onThrowException(new DeviceExceptionBean(e));
            }
            e.printStackTrace();
        } catch (UnsupportMultiProcess e) {
            if (null != listener) {
                listener.onThrowException(new DeviceExceptionBean(e));
            }
            e.printStackTrace();
        }
    }

    @Override
    public void closeDevice() {
        DeviceService.logout();
    }

    @Override
    public BaseUnipay getUnipay() {
        return createUnipay(deviceType.getUnipayType());
    }

    @Override
    public IDevice getIDevice() {
        return this;
    }
}
