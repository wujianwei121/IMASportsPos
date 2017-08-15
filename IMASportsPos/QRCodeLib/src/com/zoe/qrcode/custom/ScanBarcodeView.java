package com.zoe.qrcode.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.zoe.qrcode.BarcodeFormat;
import com.zoe.qrcode.DecodeHintType;
import com.zoe.qrcode.Result;
import com.zoe.qrcode.ResultPoint;
import com.zoe.qrcode.android.camera.CameraManager;
import com.zoe.qrcode.custom.CaptureActivityHandler.DecodeInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * ??????
 * 
 * @ClassName: ScanBarcodeView
 * @author A18ccms a18ccms_gmail_com
 * @date 2015??5??23?? ????2:32:09
 *
 */
public class ScanBarcodeView extends ViewGroup implements SurfaceHolder.Callback {
	/**
	 * ???
	 */
	private static final String TAG = ScanBarcodeView.class.getSimpleName();
	/**
	 * ?��???? ????????��?????????
	 */
	private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192,
			128, 64 };
	/**
	 * ?????????????
	 */
	private static final long ANIMATION_DELAY = 80L;
	/**
	 * ???????????????
	 */
	private static final int CURRENT_POINT_OPACITY = 0xA0;
	/**
	 * ??????????????
	 */
	private static final int MAX_RESULT_POINTS = 20;
	/**
	 * ???????��
	 */
	private static final int POINT_SIZE = 6;
	/**
	 * ?????????
	 */
	private CameraManager cameraManager;
	/**
	 * ??????????
	 */
	private CaptureActivityHandler handler;
	/**
	 * ??????
	 */
	private Collection<BarcodeFormat> decodeFormats;
	/**
	 * ??????
	 */
	private String characterSet;
	/**
	 * ????????
	 */
	private Map<DecodeHintType, ?> decodeHints;
	/**
	 * ??????
	 */
	private Result savedResultToShow;
	/**
	 * ????
	 */
	private final Paint paint;
	/**
	 * ?????
	 */
	private Bitmap resultBitmap;
	/**
	 * ?????
	 */
	private final int maskColor;
	/**
	 * ??????
	 */
	private final int resultColor;
	/**
	 * ????????
	 */
	private final int laserColor;
	/**
	 * ????????
	 */
	private final int resultPointColor;
	/**
	 * ?????
	 */
	private int scannerAlpha;
	/**
	 * ??????????
	 */
	private List<ResultPoint> possibleResultPoints;
	/**
	 * ?????��??????????
	 */
	private List<ResultPoint> lastPossibleResultPoints;
	/**
	 * ????????????????
	 */
	private int ScreenRate;
	/**
	 * ???????????
	 */
	private static float density;
	/**
	 * ???????????????
	 */
	private static final int CORNER_WIDTH = 10;
	/**
	 * ?����?????????��??
	 */
	private int slideTop;
	/**
	 * ?��?????????????????????
	 */
	private static final int SPEEN_DISTANCE = 3;
	/**
	 * ?????��??��?????????????????
	 */
	private static final int MIDDLE_LINE_PADDING = 5;
	/**
	 * ?????��??��??????
	 */
	private static final int MIDDLE_LINE_WIDTH = 5;
	/**
	 * ????????????
	 */
	boolean isFirst;
	/**
	 * ????
	 */
	private SurfaceView mSurfaceView;
	/**
	 * ???
	 */
	private SurfaceHolder mSurfaceHolder;

	/**
	 * ??????</p> ??????
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param context
	 * @param attrs
	 */
	public ScanBarcodeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundColor(Color.TRANSPARENT);

		setWillNotDraw(false);
		density = context.getResources().getDisplayMetrics().density;
		// ???????????dp
		ScreenRate = (int) (20 * density);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		maskColor = Color.argb(100, 255, 246, 94);
		resultColor = Color.argb(176, 255, 255, 255);
		laserColor = Color.argb(255, 255, 246, 94);
		resultPointColor = Color.argb(192, 255, 189, 33);
		scannerAlpha = 0;
		possibleResultPoints = new ArrayList<ResultPoint>(5);
		lastPossibleResultPoints = null;

	}

	/**
	 * ????
	 * 
	 * @Title: onResume
	 */
	public void onResume() {
		Log.i(TAG, "onResume");
		cameraManager = new CameraManager(this.getContext());
		initCamera(mSurfaceHolder);
	}

	/**
	 * ????
	 * 
	 * @Title: onPause
	 */
	public void onPause() {
		Log.i(TAG, "onPause");
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		cameraManager.closeDriver();
		// historyManager = null; // Keep for onActivityResult
		if (mSurfaceHolder != null) {
			mSurfaceHolder.removeCallback(this);
		}
	}

	/**
	 * ????????
	 * 
	 * @Title: initCamera
	 * @param surfaceHolder
	 */
	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			return;
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG,
					"initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, decodeFormats,
						decodeHints, characterSet, cameraManager,
						resultBitmap == null);
			}
			decodeOrStoreSavedBitmap(null, null);
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			if (mOnScanListener != null) {
				mOnScanListener.onScanError(
						OnScanListener.ERROR_CAMERA_CANNOT_OPEN, ioe);
			}
		} catch (RuntimeException e) {
			Log.w(TAG, "Unexpected error initializing camera", e);
			if (mOnScanListener != null) {
				mOnScanListener.onScanError(
						OnScanListener.ERROR_CAMERA_CANNOT_OPEN, e);
			}
		}
	}

	/**
	 * ??????????
	 * 
	 * @Title: getCaptureHandler
	 * @return
	 */
	public Handler getCaptureHandler() {
		return handler;
	}

	/**
	 * ????????????
	 * 
	 * @Title: getCameraManager
	 * @return
	 */
	public CameraManager getCameraManager() {
		return cameraManager;
	}

	/**
	 * ??????????????
	 * 
	 * @Title: decodeOrStoreSavedBitmap
	 * @param bitmap
	 * @param result
	 */
	private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
		// Bitmap isn't used yet -- will be used soon
		if (handler == null) {
			savedResultToShow = result;
		} else {
			if (result != null) {
				savedResultToShow = result;
			}
			if (savedResultToShow != null) {
				Message message = Message.obtain(handler,
						DecodeInterface.decode_succeeded, savedResultToShow);
				handler.sendMessage(message);
			}
			savedResultToShow = null;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i(TAG, "surfaceCreated");
		initCamera(holder);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.i(TAG, "surfaceChanged");

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG, "surfaceDestroyed");
	}

	/**
	 * ???????
	 * 
	 * @Title: addPossibleResultPoint
	 * @param point
	 */
	public void addPossibleResultPoint(ResultPoint point) {
		List<ResultPoint> points = possibleResultPoints;
		synchronized (points) {
			points.add(point);
			int size = points.size();
			if (size > MAX_RESULT_POINTS) {
				// trim it
				points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
			}
		}
	}

	/**
	 * ???????,???????
	 * 
	 * @Title: handleDecode
	 * @param rawResult
	 * @param barcode
	 * @param scaleFactor
	 */
	public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
		boolean fromLiveScan = barcode != null;
		if (fromLiveScan) {
			drawResultPoints(barcode, scaleFactor, rawResult);
		}
		resultBitmap = barcode;
		if (mOnScanListener != null) {
			mOnScanListener.onScanResult(rawResult, barcode, scaleFactor);
		}

	}

	/**
	 * ????????
	 * 
	 * @Title: drawResultPoints
	 * @param barcode
	 * @param scaleFactor
	 * @param rawResult
	 */
	private void drawResultPoints(Bitmap barcode, float scaleFactor,
			Result rawResult) {
		ResultPoint[] points = rawResult.getResultPoints();
		if (points != null && points.length > 0) {
			Canvas canvas = new Canvas(barcode);
			Paint paint = new Paint();
			paint.setColor(Color.parseColor("#c099cc00"));
			if (points.length == 2) {
				paint.setStrokeWidth(4.0f);
				drawLine(canvas, paint, points[0], points[1], scaleFactor);
			} else if (points.length == 4
					&& (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A || rawResult
							.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
				// Hacky special case -- draw two lines, for the barcode and
				// metadata
				drawLine(canvas, paint, points[0], points[1], scaleFactor);
				drawLine(canvas, paint, points[2], points[3], scaleFactor);
			} else {
				paint.setStrokeWidth(10.0f);
				for (ResultPoint point : points) {
					if (point != null) {
						canvas.drawPoint(scaleFactor * point.getX(),
								scaleFactor * point.getY(), paint);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @Title: drawLine
	 * @param canvas
	 * @param paint
	 * @param a
	 * @param b
	 * @param scaleFactor
	 */
	private static void drawLine(Canvas canvas, Paint paint, ResultPoint a,
			ResultPoint b, float scaleFactor) {
		if (a != null && b != null) {
			canvas.drawLine(scaleFactor * a.getX(), scaleFactor * a.getY(),
					scaleFactor * b.getX(), scaleFactor * b.getY(), paint);
		}
	}

	/**
	 * ??????
	 * 
	 * @Title: drawViewfinder
	 */
	public void drawViewfinder() {
		Bitmap resultBitmap = this.resultBitmap;
		this.resultBitmap = null;
		if (resultBitmap != null) {
			resultBitmap.recycle();
		}
		invalidate();
	}

	/**
	 * ??????
	 */
	public OnScanListener mOnScanListener;

	/**
	 * ????????????
	 * 
	 * @Title: setOnScanListener
	 * @param listener
	 */
	public void setOnScanListener(OnScanListener listener) {
		mOnScanListener = listener;
	}

	/**
	 * 
	 * @ClassName: onScanListener
	 * @author A18ccms a18ccms_gmail_com
	 * @date 2015??5??25?? ????9:34:05
	 *
	 */
	public interface OnScanListener {
		/**
		 * ??--??????????
		 */
		int ERROR_CAMERA_CANNOT_OPEN = 1001;

		/**
		 * ?????
		 * 
		 * @Title: onScanError
		 * @param msg
		 */
		void onScanError(int msg, Exception ex);

		void onScanResult(Result rawResult, Bitmap barcode, float scaleFactor);
	}

	/**
	 * ???????? {@inheritDoc}
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (cameraManager == null) {
			return; // not ready yet, early draw before done configuring
		}
		Log.i(TAG, "onDraw");
		Rect frame = cameraManager.getFramingRect();
		Rect previewFrame = cameraManager.getFramingRectInPreview();
		if (frame == null || previewFrame == null) {
			return;
		}
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		Log.i(TAG, "onDraw width:" + width + " height:" + height);

		Log.i(TAG, "onDraw frame.left:" + frame.left + " frame.top:"
				+ +frame.top + "frame.right:" + frame.right + "frame.bottom:"
				+ frame.bottom);
		// Draw the exterior (i.e. outside the framing rect) darkened
		paint.setColor(resultBitmap != null ? resultColor : maskColor);

		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
				paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);
		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(CURRENT_POINT_OPACITY);
			canvas.drawBitmap(resultBitmap, null, frame, paint);
		} else {

			paint.setColor(laserColor);
			canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH,
					frame.top + ScreenRate, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right,
					frame.top + ScreenRate, paint);
			canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
					+ ScreenRate, frame.bottom, paint);
			canvas.drawRect(frame.left, frame.bottom - ScreenRate, frame.left
					+ CORNER_WIDTH, frame.bottom, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.bottom
					- CORNER_WIDTH, frame.right, frame.bottom, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom
					- ScreenRate, frame.right, frame.bottom, paint);

			if (!isFirst) {
				isFirst = true;
				slideTop = frame.top;
			}
			// ?????��????,????????��?��???????????SPEEN_DISTANCE
			slideTop += SPEEN_DISTANCE;
			if (slideTop >= frame.bottom) {
				slideTop = frame.top;
			}
			// paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
			// scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
			// canvas.drawRect(frame.left + middle_line_padding, slideTop
			// - MIDDLE_LINE_WIDTH / 2, frame.right - MIDDLE_LINE_PADDING,
			// slideTop + MIDDLE_LINE_WIDTH / 2, paint);

			// ?��?????????
			// Draw a red "laser scanner" line through the middle to show
			// decoding is active
			paint.setColor(laserColor);
			paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
			scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
			int middle = frame.height() / 2 + frame.top;
			canvas.drawRect(frame.left + MIDDLE_LINE_PADDING, middle
					- MIDDLE_LINE_WIDTH / 2, frame.right - MIDDLE_LINE_PADDING,
					middle + MIDDLE_LINE_WIDTH / 2, paint);

			float scaleX = frame.width() / (float) previewFrame.width();
			float scaleY = frame.height() / (float) previewFrame.height();

			List<ResultPoint> currentPossible = possibleResultPoints;
			List<ResultPoint> currentLast = lastPossibleResultPoints;
			int frameLeft = frame.left;
			int frameTop = frame.top;
			if (currentPossible.isEmpty()) {
				lastPossibleResultPoints = null;
			} else {
				possibleResultPoints = new ArrayList<ResultPoint>(5);
				lastPossibleResultPoints = currentPossible;
				paint.setAlpha(CURRENT_POINT_OPACITY);
				paint.setColor(resultPointColor);
				synchronized (currentPossible) {
					for (ResultPoint point : currentPossible) {
						canvas.drawCircle(frameLeft
								+ (int) (point.getX() * scaleX), frameTop
								+ (int) (point.getY() * scaleY), POINT_SIZE,
								paint);
					}
				}
			}
			if (currentLast != null) {
				paint.setAlpha(CURRENT_POINT_OPACITY / 2);
				paint.setColor(resultPointColor);
				synchronized (currentLast) {
					float radius = POINT_SIZE / 2.0f;
					for (ResultPoint point : currentLast) {
						canvas.drawCircle(frameLeft
								+ (int) (point.getX() * scaleX), frameTop
								+ (int) (point.getY() * scaleY), radius, paint);
					}
				}
			}

			// Request another update at the animation interval, but only
			// repaint the laser line,
			// not the entire viewfinder mask.
			postInvalidateDelayed(ANIMATION_DELAY, frame.left - POINT_SIZE,
					frame.top - POINT_SIZE, frame.right + POINT_SIZE,
					frame.bottom + POINT_SIZE);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		Log.i(TAG, "onLayout changed:" + changed + " l:" + l + " t:" + t
				+ " r:" + r + " b:" + b);
		mSurfaceView = new SurfaceView(this.getContext());
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(r - l, b - t);
		this.addView(mSurfaceView, para);
		mSurfaceView.layout(l, t, r, b);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View v = getChildAt(i);
			v.measure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	/**
	 * ????????
	 * 
	 * @Title: continueScan
	 */
	public void continueScan(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(DecodeInterface.restart_preview,
					delayMS);
		}
	}
}
