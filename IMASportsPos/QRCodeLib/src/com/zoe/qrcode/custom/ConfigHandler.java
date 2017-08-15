package com.zoe.qrcode.custom;

import com.zoe.qrcode.android.camera.FrontLightMode;

/**
 * ���þ��
 * 
 * @ClassName: ConfigHandler
 * @author A18ccms a18ccms_gmail_com
 * @date 2015��5��21�� ����5:50:03
 *
 */
public class ConfigHandler {
	/***
	 * ��ȡ�Ƿ�����Զ��Խ�
	 * 
	 * @Title: CameraAutoFocus
	 * @return
	 */
	public static boolean CameraAutoFocus() {
		return true;
	}

	/**
	 * ������ȡ����
	 * 
	 * @Title: CameraContinuousFocus
	 * @return
	 */
	public static boolean CameraContinuousFocus() {
		return true;
	}

	public static FrontLightMode CameraFrontLightMode() {
		return FrontLightMode.OFF;
	}

	public static boolean CameraDisableExposure() {
		return true;
	}

	public static boolean CameraInvertScan() {
		return false;
	}

	public static boolean CameraDisableBarcodeSceneMode() {
		return true;
	}

	// DISABLE_METERING
	public static boolean CameraDisableMetering() {
		return true;
	}

	public static boolean CameraVibrate() {
		return false;
	}

	public static boolean CameraPlayBeep() {
		return true;
	}

	public static boolean CameraProductFormats() {
		return true;
	}

	public static boolean CameraDecode1DIndustrial() {
		return true;
	}

	// KEY_DECODE_QR
	public static boolean CameraDecodeQR() {
		return true;
	}

	// DECODE_DATA_MATRIX
	public static boolean CameraDecodeDataMatrix() {
		return true;
	}

	// DECODE_AZTEC
	public static boolean CameraDecodeAztec() {
		return false;
	}

	public static boolean CameraDecodePdf147() {
		return false;
	}

	public static boolean CameraDisableAutoOrientation() {
		return true;
	}

	// COPY_TO_CLIPBOARD
	public static boolean CameraCopyToClipboard() {
		return false;
	}

	public static boolean CameraSuppleMental() {
		return true;
	}
}
