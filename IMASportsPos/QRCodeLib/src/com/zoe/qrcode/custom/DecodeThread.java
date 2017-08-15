/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zoe.qrcode.custom;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.zoe.qrcode.BarcodeFormat;
import com.zoe.qrcode.DecodeHintType;
import com.zoe.qrcode.ResultPointCallback;

/**
 * This thread does all the heavy lifting of decoding the images.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class DecodeThread extends Thread {

	public static final String BARCODE_BITMAP = "barcode_bitmap";
	public static final String BARCODE_SCALED_FACTOR = "barcode_scaled_factor";

	private final ScanBarcodeView mHold;
	private final Map<DecodeHintType, Object> hints;
	private Handler handler;
	private final CountDownLatch handlerInitLatch;

	public DecodeThread(ScanBarcodeView activity,
			Collection<BarcodeFormat> decodeFormats,
			Map<DecodeHintType, ?> baseHints, String characterSet,
			ResultPointCallback resultPointCallback) {

		this.mHold = activity;
		handlerInitLatch = new CountDownLatch(1);

		hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
		if (baseHints != null) {
			hints.putAll(baseHints);
		}

		// The prefs can't change while the thread is running, so pick them up
		// once here.
		if (decodeFormats == null || decodeFormats.isEmpty()) {
			decodeFormats = EnumSet.noneOf(BarcodeFormat.class);
			if (ConfigHandler.CameraProductFormats()) {
				decodeFormats.addAll(DecodeFormatManager.PRODUCT_FORMATS);
			}
			if (ConfigHandler.CameraDecode1DIndustrial()) {
				decodeFormats.addAll(DecodeFormatManager.INDUSTRIAL_FORMATS);
			}
			if (ConfigHandler.CameraDecodeQR()) {
				decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
			}
			if (ConfigHandler.CameraDecodeDataMatrix()) {
				decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
			}
			if (ConfigHandler.CameraDecodeAztec()) {
				decodeFormats.addAll(DecodeFormatManager.AZTEC_FORMATS);
			}
			if (ConfigHandler.CameraDecodePdf147()) {
				decodeFormats.addAll(DecodeFormatManager.PDF417_FORMATS);
			}
		}
		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

		if (characterSet != null) {
			hints.put(DecodeHintType.CHARACTER_SET, characterSet);
		}
		hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK,
				resultPointCallback);
		Log.i("DecodeThread", "Hints: " + hints);
	}

	Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}

	@Override
	public void run() {
		Looper.prepare();
		handler = new DecodeHandler(mHold, hints);
		handlerInitLatch.countDown();
		Looper.loop();
	}

}
