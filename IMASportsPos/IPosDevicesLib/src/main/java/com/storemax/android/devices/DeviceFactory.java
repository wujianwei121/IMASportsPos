package com.storemax.android.devices;

import android.content.Context;
import android.text.TextUtils;

import com.storemax.android.devices.device.BaseDevice;
import com.storemax.android.devices.device.LandiDevice;
import com.storemax.android.devices.device.UnipayA8Device;
import com.storemax.android.devices.listener.OnDeviceInfoReceivedListener;
import com.storemax.android.devices.unipay.BaseUnipay.UnipayType;

public class DeviceFactory {

	public static String SP_TAG = "DEVICE_INFO";

	public static String SP__PARAM_DEVIDE_ID = "DEVIDE_ID";

	public static String SP__PARAM_DEVIDE_TYPE = "DEVIDE_TYPE";
	/**
	 * 
	 */
	private static DeviceType deviceType;

	private static BaseDevice baseDevice;

	private static String deviceSn;

	private static GetDeviceInfo getDeviceInfoCallback;

	private static final String DEVICE_TYPE_KEY = "device_type";

	public static Context context;

	private DeviceFactory() {

	}

	public static void initDevice(Context context, GetDeviceInfo getDeviceInfo) {
		getDeviceInfoCallback = getDeviceInfo;
		DeviceFactory.context = context;
		if (getDeviceInfo == null) {
		} else {
			deviceType = getDeviceInfo.getDeviceType();
			baseDevice = getDeviceInstance();
			deviceSn = Utils.getSPValue(context, SP__PARAM_DEVIDE_ID);
			if (TextUtils.isEmpty(deviceSn)) {
				baseDevice.getDeviceSN(context, new OnDeviceInfoReceivedListener() {

					@Override
					public void getDeviceSn(String sn) {
						deviceSn = sn;
						Utils.setSPValue(DeviceFactory.context, SP_TAG, SP__PARAM_DEVIDE_ID, sn);

					}
				});
			}
		}

	}

	public static BaseDevice getDeviceInstance() {

		// 从缓存中读出设备类型
		if (baseDevice == null) {
			if (getDeviceInfoCallback != null) {
				deviceType = getDeviceInfoCallback.getDeviceType();
			}
			synchronized (DeviceFactory.class) {
				if (baseDevice == null) {
					switch (deviceType) {
					case NYPOS:
					case SPD_A8:
					case P990:
					case LANID:
					case UNIPAY_SPD_MOBILE_A8: {
						baseDevice = new LandiDevice(deviceType);
						break;
					}
					case UNIPAY_UMS: {
						baseDevice = new UnipayA8Device();
						break;
					}
					case UNKNOWN: {
						baseDevice = null;
						break;
					}
					}
				}
			}
		}
		return baseDevice;
	}

	public static String getDeviceId() {
		return android.os.Build.SERIAL;
	}

	public static DeviceType getDeviceType() {
		return deviceType;
	}

	public static final int SUCCESS = 0x0000;
	public static final int ERROR = -1;

	public enum DeviceType {
		/**
		 * 浦发汇宜支付
		 */
		NYPOS("NYPOS", "浦发汇宜支付", UnipayType.NY_POS),
		/**
		 * LANDI 系列
		 */
		LANID("LANDI", "联迪系列机器", UnipayType.AUTO),
		/**
		 * p990(包括浦发和银联)
		 */
		P990("p990", "银联A8", UnipayType.P990),
		/**
		 * p990(包括浦发和银联)
		 */
		SPD_A8("SPD_A8", "浦发A8", UnipayType.SPD_A8),
		/**
		 * 银联A8
		 */
		UNIPAY_UMS("unipay_a8", "银联A8", UnipayType.UNIPAY_UMS_A8),
		/**
		 * 沃银数据，浦发移动支付A8
		 */
		UNIPAY_SPD_MOBILE_A8("wo_a8", "浦发移动支付A8", UnipayType.SPD_MOBILE),
		/**
		 * 未知
		 */
		UNKNOWN("UNKNOWN", "未知备", UnipayType.UNKNOWN);

		/**
		 * 名称
		 */
		private String deviceName;
		/**
		 * 标签
		 */
		private String deviceTag;
		/**
		 * 支付类型
		 */
		private UnipayType unipayType;

		/**
		 * 支付方式
		 * @param deviceTag
		 * @param deviceName
		 * @param unType
		 */
		private DeviceType(String deviceTag, String deviceName, UnipayType unType) {
			this.deviceName = deviceName;
			this.deviceTag = deviceTag;
			this.unipayType = unType;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see Enum#toString()
		 */
		@Override
		public String toString() {
			return this.deviceName;
		}

		/**
		 * 获取银联支付方式名称
		 * 
		 * @Title: getMethodName
		 * @return
		 */
		public String getDeviceName() {
			return this.deviceName;
		}

		/**
		 * 获取银联支付方式名称
		 * 
		 * @Title: getMethodName
		 * @return
		 */
		public String getDeviceTag() {
			return this.deviceTag;
		}

		public UnipayType getUnipayType() {
			return unipayType;
		}

	}

	public interface GetDeviceInfo {
		public DeviceType getDeviceType();
	}
}
