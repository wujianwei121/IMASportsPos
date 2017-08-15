package com.storemax.android.devices.printer;

import android.text.Layout.Alignment;
import android.text.TextPaint;

import com.huiyi.nypos.pay.thirdpay.aidl.AidlPrint;
import com.storemax.android.devices.bean.PrintItemBean;
import com.storemax.android.devices.bean.PrintResultBean;

public abstract class BasePrinter {
	
	/**
	 * @Title: writeTitleLine
	 * @Description: 默认 字体大小：BIG 对齐方式：CENTER 打印方式：TEXT
	 * @param text
	 */
	public PrintItemBean writeTitleLine(String text) {
		return writeLine(text, FontSize.BIG, Alignment.ALIGN_CENTER,
				LineType.TEXT);
	}
	
	public PrintItemBean writeMidLine(String text) {
		return writeLine(text, FontSize.NORMAL, Alignment.ALIGN_CENTER,
				LineType.TEXT);
	}
	
	public PrintItemBean writeTextJustified(String left, String right) {
		return writeTextJustified(left, right, 0, FontSize.NORMAL,
				Alignment.ALIGN_NORMAL);
	}
	
	/**
	 * @Title: writeTextJustified
	 * @Description: 左右对齐
	 * @param left
	 * @param right
	 * @param splitWidth
	 * @return
	 */
	public PrintItemBean writeTextJustified(String left, String right,
			int splitWidth) {
		return writeTextJustified(left, right, splitWidth, FontSize.NORMAL,
				Alignment.ALIGN_NORMAL);
	}
	
	public PrintItemBean writeTextJustified(String left, String right,
			int splitWidth, Alignment align) {
		return writeTextJustified(left, right, splitWidth, FontSize.NORMAL,
				align);
	}
	
	/**
	 * @Title: writeDetailTitle
	 * @param left
	 * @param center
	 * @param right
	 * @return
	 */
	public PrintItemBean writeDetailTitle(String left, String center,
			String right) {
		return writeDetailTitle(left, center, right, FontSize.NORMAL);
	}
	
	/**
	 * @Title: writeDetailItem
	 * @param center
	 * @param right
	 * @return
	 */
	public PrintItemBean writeDetailItem(String center, String right) {
		return writeDetailItem("", center, right, FontSize.NORMAL);
	}
	
	/**
	 * @Title: writeTitleLine
	 * @Description: 默认 字体大小：NORMAL 对齐方式：LEFT 打印方式：TEXT
	 * @param text
	 */
	public PrintItemBean writeLine(String text) {
		return writeLine(text, FontSize.NORMAL, Alignment.ALIGN_NORMAL,
				LineType.TEXT);
	}
	
	/**
	 * @Title: writeTitleLine
	 * @Description: 打印二维码
	 * @param text
	 */
	public PrintItemBean writeBarcode(String text) {
		return writeLine(text, FontSize.BIG, Alignment.ALIGN_CENTER,
				LineType.BARCODE);
	}
	
	public abstract PrintItemBean writeDetailTitle(String left, String center,
			String right, FontSize size);
	
	public abstract PrintItemBean writeDetailItem(String left, String center,
			String right, FontSize size);
	
	public abstract PrintItemBean writeTextJustified(String left, String right,
			int splitWidth, FontSize size, Alignment wrapAlign);
	
	public abstract PrintItemBean writeLine(String text, FontSize fontSize,
			Alignment align, LineType type);
	
	/**
	 * @Title: writeTitleLine
	 * @Description: 打印一维码
	 * @param text
	 */
	public abstract PrintItemBean writeQrCode(String text);
	
	public abstract PrintItemBean writeSingleLine();
	
	public abstract PrintItemBean writeDoubleLine();
	
	/**
	 * @Title: 打印
	 * @Description: 开始生成打印小票的图片
	 */
	public abstract void startPrint();

	public abstract void startNYPosPrint(AidlPrint aidlPrint);
	
	protected abstract TextPaint getPaint(FontSize size);
	
	protected abstract TextPaint createPaint(int size);
	
	public abstract int getPrinterWidth();
	
	public abstract PrintResultBean getResultInfo(Object obj);
	
	public enum AlignType {
		/**
		 * 左右对齐文字
		 */
		TEXT_JUSTIFIED, DETAIL_TITLE, DETAIL_ITEM, NONE
	}
	
	/**
	 * @Title: PrinterFactory.java
	 * @Package com.storemax.android.devices
	 * @copyright ◎2002-2007 Nanjing StoreMax Tech. Co. Ltd. All Rights Reserved.
	 * @Description: 打印的字体大小
	 * @author A18ccms A18ccms_gmail_com
	 * @date 2016年12月12日 上午11:30:29
	 * @version V1.0
	 */
	public enum FontSize {
		/**
		 * 超大
		 */
		LARGE,
		/**
		 * 大
		 */
		BIG,
		/**
		 * 正常
		 */
		NORMAL,
		/**
		 * 小
		 */
		SMALL
	}
	
	/**
	 * @Title: PrinterFactory.java
	 * @Package com.storemax.android.devices
	 * @copyright ◎2002-2007 Nanjing StoreMax Tech. Co. Ltd. All Rights Reserved.
	 * @Description: 打印类型
	 * @author A18ccms A18ccms_gmail_com
	 * @date 2016年12月12日 上午11:28:10
	 * @version V1.0
	 */
	public enum LineType {
		
		/**
		 * 文字
		 */
		TEXT,
		/**
		 * 二维码
		 */
		QR_CODE,
		/**
		 * 图片
		 */
		PICTURE,
		/**
		 * 条形码
		 */
		BARCODE,
		/**
		 * 单行线
		 */
		SINGLE_LINE,
		/**
		 * 双行线
		 */
		DOUBLE_LINE
	}
	
	/**
	 * 打印机缺纸
	 */
	public static final int PRINT_NO_PAPER = 0xfe01;
	/**
	 * 打印完毕
	 */
	public static final int PRINT_SUCCESS = 0xfe00;
	/**
	 * 打印其它错误
	 */
	public static final int PRINT_ERROR = 0xfeff;
}
