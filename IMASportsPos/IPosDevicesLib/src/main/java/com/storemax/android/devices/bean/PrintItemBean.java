package com.storemax.android.devices.bean;

import android.text.Layout.Alignment;

import com.storemax.android.devices.printer.BasePrinter.AlignType;
import com.storemax.android.devices.printer.BasePrinter.FontSize;
import com.storemax.android.devices.printer.BasePrinter.LineType;

public class PrintItemBean {
	/**
	 * 打印文字
	 */
	public String text;
	/**
	 * 打印类型
	 */
	public LineType type;
	/**
	 * 对齐方式
	 */
	public Alignment align;
	/**
	 * 字体大小
	 */
	public FontSize fontSize;
	/**
	*
	*/
	public Object tag;
	/**
	 * 距离上一行的距离
	 */
	public int top;
	/**
	 * 距离左右的距离
	 */
	public int left;
	/**
	 * 换行后距离左边的距离
	 */
	public int wrapLeft;
	/**
	 * 
	 */
	public int innerWidth;
	/**
	 * 是否自动换行
	 */
	public boolean autoWrap = true;
	/**
	 * 换行后的对齐方式
	 */
	public Alignment wrapAlign = Alignment.ALIGN_NORMAL;
	
	public AlignType innerType = AlignType.NONE;
}
