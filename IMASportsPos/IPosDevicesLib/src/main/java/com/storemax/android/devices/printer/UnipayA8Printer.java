package com.storemax.android.devices.printer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout.Alignment;
import android.text.TextPaint;

import com.huiyi.nypos.pay.thirdpay.aidl.AidlPrint;
import com.storemax.android.devices.bean.PrintItemBean;
import com.storemax.android.devices.bean.PrintResultBean;
import com.storemax.android.devices.listener.OnDeviceOperaListener;
import com.ums.AppHelper;

public class UnipayA8Printer extends PrintByImage {
	
	public static final int FONT_SIZE_BIG = 40;
	public static final int FONT_SIZE_LARGE = 55;
	public static final int FONT_SIZE_SMALL = 55;
	public static final int FONT_SIZE_NORMAL = 25;
	
	private TextPaint paintBig;
	private TextPaint paintNormal;
	private TextPaint paintSmall;
	private TextPaint paintLarge;
	
	public static final int WIDTH = 400;
	OnDeviceOperaListener listener;
	Activity activity;
	
	public UnipayA8Printer(Activity activity, OnDeviceOperaListener lister) {
		this.activity = activity;
		listener = lister;
	}
	
	@Override
	public int getPrinterWidth() {
		return WIDTH;
		
	}
	
	@Override
	public PrintResultBean getResultInfo(Object obj) {
		PrintResultBean bean = new PrintResultBean();
		bean.code = BasePrinter.PRINT_ERROR;
		bean.msg = "打印信息入参错误！";
		if (obj instanceof Intent) {
			Intent intent = (Intent) obj;
			if (intent != null) {
				String code = intent.getStringExtra("resultCode");
				if (code.equals(Success + "")) {
					bean.code = BasePrinter.PRINT_SUCCESS;
					bean.msg = "打印成功";
				} else if (code.equals(Printer_PaperLack + "")) {
					bean.code = BasePrinter.PRINT_NO_PAPER;
					bean.msg = "打印失败";
				} else if (code.equals(Printer_Print_Fail + "")) {
					bean.code = BasePrinter.PRINT_ERROR;
					bean.msg = "打印失败";
				} else if (code.equals(Printer_AddPrnStr_Fail + "")) {
					bean.code = BasePrinter.PRINT_ERROR;
					bean.msg = "设置字符串缓冲失败";
				} else if (code.equals(Printer_AddImg_Fail + "")) {
					bean.code = BasePrinter.PRINT_ERROR;
					bean.msg = "设置图片缓冲失败";
				} else if (code.equals(Printer_Busy + "")) {
					bean.code = BasePrinter.PRINT_ERROR;
					bean.msg = "打印机忙";
				} else if (code.equals(Printer_Wrong_Package + "")) {
					bean.code = BasePrinter.PRINT_ERROR;
					bean.msg = "打印数据包格式错";
				} else if (code.equals(Printer_Fault + "")) {
					bean.code = BasePrinter.PRINT_ERROR;
					bean.msg = "打印机故障";
				} else if (code.equals(Printre_TooHot + "")) {
					bean.code = BasePrinter.PRINT_ERROR;
					bean.msg = "打印机过热";
				} else if (code.equals(Printer_UnFinished + "")) {
					bean.code = BasePrinter.PRINT_ERROR;
					bean.msg = "打印未完成";
				} else if (code.equals(Printer_NoFontLib + "")) {
					bean.code = BasePrinter.PRINT_ERROR;
					bean.msg = "打印机未装字库";
				} else if (code.equals(Printer_OutOfMemory + "")) {
					bean.code = BasePrinter.PRINT_ERROR;
					bean.msg = "数据包过长";
				} else if (code.equals(Printer_Other_Error + "")) {
					bean.code = BasePrinter.PRINT_ERROR;
					bean.msg = "其他异常错误";
				} else {
					bean.code = BasePrinter.PRINT_ERROR;
					bean.msg = "未知其他异常错误";
				}
			}
		}
		return bean;
	}
	
	public final static int Success = 0;
	
	public final static int Printer_Base_Error = -1000;
	/** 打印失败 */
	public final static int Printer_Print_Fail = Printer_Base_Error - 1;
	/** 设置字符串缓冲失败 */
	public final static int Printer_AddPrnStr_Fail = Printer_Base_Error - 2;
	/** 设置图片缓冲失败 */
	public final static int Printer_AddImg_Fail = Printer_Base_Error - 3;
	/** 打印机忙 */
	public final static int Printer_Busy = Printer_Base_Error - 4;
	/** 打印机缺纸 */
	public final static int Printer_PaperLack = Printer_Base_Error - 5;
	/** 打印数据包格式错 */
	public final static int Printer_Wrong_Package = Printer_Base_Error - 6;
	/** 打印机故障 */
	public final static int Printer_Fault = Printer_Base_Error - 7;
	/** 打印机过热 */
	public final static int Printre_TooHot = Printer_Base_Error - 8;
	/** 打印未完成 */
	public final static int Printer_UnFinished = Printer_Base_Error - 9;
	/** 打印机未装字库 */
	public final static int Printer_NoFontLib = Printer_Base_Error - 10;
	/** 数据包过长 */
	public final static int Printer_OutOfMemory = Printer_Base_Error - 11;
	/** 其他异常错误 */
	public final static int Printer_Other_Error = Printer_Base_Error - 999;
	
	@Override
	public PrintItemBean writeQrCode(String text) {
		return null;
	}
	
	@Override
	public PrintItemBean writeSingleLine() {
		String text = "---------------------------------------------------------";
		return writeLine(text, FontSize.NORMAL, Alignment.ALIGN_CENTER,
				LineType.TEXT);
	}
	
	@Override
	public PrintItemBean writeDoubleLine() {
		String text = "============================";
		return writeLine(text, FontSize.NORMAL, Alignment.ALIGN_CENTER,
				LineType.TEXT);
	}
	
	@Override
	public void startPrint() {
		createImage();
		AppHelper.callPrint(activity, PrintByImage.imagePath);
	}

	@Override
	public void startNYPosPrint(AidlPrint aidlPrint) {

	}

	@Override
	protected TextPaint getPaint(FontSize size) {
		switch (size) {
		case BIG: {
			if (null == paintBig) {
				paintBig = createPaint(FONT_SIZE_BIG);
			}
			return paintBig;
			
		}
		case NORMAL: {
			if (null == paintNormal) {
				paintNormal = createPaint(FONT_SIZE_NORMAL);
			}
			return paintNormal;
			
		}
		case SMALL: {
			if (null == paintSmall) {
				paintSmall = createPaint(FONT_SIZE_SMALL);
			}
			return paintSmall;
			
		}
		case LARGE: {
			if (null == paintLarge) {
				paintLarge = createPaint(FONT_SIZE_LARGE);
			}
			return paintLarge;
		}
		default: {
			if (null == paintNormal) {
				paintNormal = createPaint(FONT_SIZE_NORMAL);
			}
			return paintNormal;
		}
		}
	}
	
	@Override
	protected TextPaint createPaint(int size) {
		TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		paint.setStrokeWidth(2);
		paint.setColor(Color.BLACK);
		paint.setTypeface(Typeface.DEFAULT);
		paint.setTextSize(size);
		return paint;
	}
}