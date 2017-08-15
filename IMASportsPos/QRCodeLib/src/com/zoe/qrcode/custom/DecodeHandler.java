/*
 * Copyright (C) 2010 ZXing authors
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.zoe.qrcode.BinaryBitmap;
import com.zoe.qrcode.DecodeHintType;
import com.zoe.qrcode.MultiFormatReader;
import com.zoe.qrcode.PlanarYUVLuminanceSource;
import com.zoe.qrcode.ReaderException;
import com.zoe.qrcode.Result;
import com.zoe.qrcode.common.HybridBinarizer;
import com.zoe.qrcode.custom.CaptureActivityHandler.DecodeInterface;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * 解析操作句柄
 *
 * @ClassName: DecodeHandler
 * @author A18ccms a18ccms_gmail_com
 *
 */
public final class DecodeHandler extends Handler {
    /**
     * 标记
     */
    private static final String TAG = DecodeHandler.class.getSimpleName();
    /**
     * 主程序句柄
     */
    private final ScanBarcodeView mHold;
    /**
     * 多种码解析
     */
    private final MultiFormatReader multiFormatReader;
    /**
     * 状态
     */
    private boolean running = true;

    public DecodeHandler(ScanBarcodeView activity,
                         Map<DecodeHintType, Object> hints) {
        multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);
        this.mHold = activity;
    }

    @Override
    public void handleMessage(Message message) {
        if (!running) {
            return;
        }
        switch (message.what) {
            case DecodeInterface.decode:
                decode((byte[]) message.obj, message.arg1, message.arg2);
                break;
            case DecodeInterface.quit:
                running = false;
                Looper.myLooper().quit();
                break;
        }
    }

    /**
     * Decode the data within the viewfinder rectangle, and time how long it
     * took. For efficiency, reuse the same reader objects from one decode to
     * the next.
     *
     * @param data   The YUV preview frame.
     * @param width  The width of the preview frame.
     * @param height The height of the preview frame.
     */
    private void decode(byte[] data, int width, int height) {
        Log.i(TAG, "decode");
        if (data == null || data.length <= 0) {
            return;
        }
        long start = System.currentTimeMillis();
        Result rawResult = null;
        // PlanarYUVLuminanceSource source =
        // activity.getCameraManager().buildLuminanceSource(data, width,
        // height);
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                rotatedData[x * height + height - y - 1] = data[x + y * width];
        }
        int tmp = width;
        width = height;
        height = tmp;
        PlanarYUVLuminanceSource source = mHold.getCameraManager()
                .buildLuminanceSource(rotatedData, width, height);

        if (source != null) {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                rawResult = multiFormatReader.decodeWithState(bitmap);
            } catch (ReaderException re) {
                // continue
            } finally {
                multiFormatReader.reset();
            }
        }

        Handler handler = mHold.getCaptureHandler();
        if (rawResult != null) {
            // Don't log the barcode contents for security.
            long end = System.currentTimeMillis();
            Log.d(TAG, "Found barcode in " + (end - start) + " ms");
            if (handler != null) {
                Message message = Message.obtain(handler,
                        DecodeInterface.decode_succeeded, rawResult);
                Bundle bundle = new Bundle();
                bundleThumbnail(source, bundle);
                message.setData(bundle);
                message.sendToTarget();
            }
        } else {
            if (handler != null) {
                Log.i(TAG, "decode_failed");
                Message message = Message.obtain(handler,
                        DecodeInterface.decode_failed);
                message.sendToTarget();
            }
        }
    }

    /**
     * 解析图片
     *
     * @param source
     * @param bundle
     * @Title: bundleThumbnail
     */
    private static void bundleThumbnail(PlanarYUVLuminanceSource source,
                                        Bundle bundle) {
        int[] pixels = source.renderThumbnail();
        int width = source.getThumbnailWidth();
        int height = source.getThumbnailHeight();
        Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height,
                Bitmap.Config.ARGB_8888);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
        bundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray());
        bundle.putFloat(DecodeThread.BARCODE_SCALED_FACTOR, (float) width
                / source.getWidth());
    }

}
