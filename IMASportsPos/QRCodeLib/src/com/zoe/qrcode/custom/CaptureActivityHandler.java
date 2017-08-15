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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zoe.qrcode.BarcodeFormat;
import com.zoe.qrcode.DecodeHintType;
import com.zoe.qrcode.Result;
import com.zoe.qrcode.android.camera.CameraManager;

import java.util.Collection;
import java.util.Map;

/**
 * 操作句柄
 * 
 * @ClassName: CaptureActivityHandler
 * @author A18ccms a18ccms_gmail_com
 * @date 2015年5月25日 上午9:42:03
 *
 */
public final class CaptureActivityHandler extends Handler {
	/**
	 * 主程序句柄
	 */
	private final ScanBarcodeView mHold;
	/**
	 * 解析线程
	 */
	private final DecodeThread decodeThread;
	/**
	 * 状态
	 */
	private State state;
	/**
	 * 相机控制器
	 */
	private final CameraManager cameraManager;

	/**
	 * 状态
	 * 
	 * @ClassName: State
	 * @author A18ccms a18ccms_gmail_com
	 * @date 2015年5月25日 上午9:50:43
	 *
	 */
	private enum State {
		PREVIEW, SUCCESS, DONE
	}

	/**
	 * 操作句柄构造函数
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param context
	 * @param decodeFormats
	 * @param baseHints
	 * @param characterSet
	 * @param cameraManager
	 */
	public CaptureActivityHandler(ScanBarcodeView context,
			Collection<BarcodeFormat> decodeFormats,
			Map<DecodeHintType, ?> baseHints, String characterSet,
			CameraManager cameraManager, boolean flag) {
		this.mHold = context;
		// 启动解析进程
		decodeThread = new DecodeThread(mHold, decodeFormats, baseHints,
				characterSet, new ViewfinderResultPointCallback(mHold));
		decodeThread.start();
		state = State.SUCCESS;

		// Start ourselves capturing previews and decoding.
		this.cameraManager = cameraManager;
		cameraManager.startPreview();
		if (flag)
			restartPreviewAndDecode();
	}

	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
		case DecodeInterface.restart_preview:
			restartPreviewAndDecode();
			break;
		case DecodeInterface.decode_succeeded:
			state = State.SUCCESS;
			Bundle bundle = message.getData();
			Bitmap barcode = null;
			float scaleFactor = 1.0f;
			if (bundle != null) {
				byte[] compressedBitmap = bundle
						.getByteArray(DecodeThread.BARCODE_BITMAP);
				if (compressedBitmap != null) {
					barcode = BitmapFactory.decodeByteArray(compressedBitmap,
							0, compressedBitmap.length, null);
					// Mutable copy:
					barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
				}
				scaleFactor = bundle
						.getFloat(DecodeThread.BARCODE_SCALED_FACTOR);
			}
			mHold.handleDecode((Result) message.obj, barcode, scaleFactor);
			break;
		case DecodeInterface.decode_failed:
			// We're decoding as fast as possible, so when one decode fails,
			// start another.
			state = State.PREVIEW;
			Log.i("zoe", "decode decode_failed");
			cameraManager.requestPreviewFrame(decodeThread.getHandler(),
					DecodeInterface.decode);
			break;
		}
	}

	public void quitSynchronously() {
		state = State.DONE;
		cameraManager.stopPreview();
		Message quit = Message.obtain(decodeThread.getHandler(),
				DecodeInterface.quit);
		quit.sendToTarget();
		try {
			// Wait at most half a second; should be enough time, and onPause()
			// will timeout quickly
			decodeThread.join(500L);
		} catch (InterruptedException e) {
			// continue
		}

		// Be absolutely sure we don't send any queued up messages
		removeMessages(DecodeInterface.decode_succeeded);
		removeMessages(DecodeInterface.decode_failed);
	}

	/**
	 * 开始扫描
	 * 
	 * @Title: restartPreviewAndDecode
	 */
	private void restartPreviewAndDecode() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			Log.i("zoe", "decode restartPreviewAndDecode");
			cameraManager.requestPreviewFrame(decodeThread.getHandler(),
					DecodeInterface.decode);
			mHold.drawViewfinder();
		}
	}

	/**
	 * 解析结果
	 * 
	 * @ClassName: DecodeInterface
	 * @author A18ccms a18ccms_gmail_com
	 * @date 2015年5月25日 上午9:50:53
	 *
	 */
	public interface DecodeInterface {
		int decode = 0x11000001;
		int decode_failed = 0x11000002;
		int decode_succeeded = 0x11000003;
		int launch_product_query = 0x11000004;
		int quit = 0x11000005;
		int restart_preview = 0x11000006;
		int return_scan_result = 0x11000007;
	}

}
