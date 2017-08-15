package com.storemax.android.devices.printer;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.storemax.android.devices.bean.PrintItemBean;
import com.zoe.qrcode.BarcodeFormat;
import com.zoe.qrcode.MultiFormatWriter;
import com.zoe.qrcode.WriterException;
import com.zoe.qrcode.common.BitMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public abstract class PrintByImage extends BasePrinter {
	
	private ArrayList<PrintItemBean> printDatasoure = null;
	
	private int paperWidth = 0;
	
	protected int paperHeight = 0;
	
	public static final String imagePath = Environment
			.getExternalStorageDirectory() + "/printImage.png";
	
	public PrintByImage() {
		paperWidth = getPrinterWidth();
	}
	
	@Override
	public PrintItemBean writeDetailTitle(String left, String center,
			String right, FontSize size) {
		int eachWidth = paperWidth / 3;
		TextPaint paint = getPaint(size);
		float widthLeft = paint.measureText(left);
		float widthCenter = paint.measureText(center);
		float widthtRight = paint.measureText(right);
		float blankWidth = paint.measureText(" ");
		StringBuilder sb = new StringBuilder();
		int leftBlank = (int) (((eachWidth - widthLeft) / 2) / blankWidth);
		for (int i = 0; i < leftBlank; i++) {
			sb.append(" ");
		}
		sb.append(left);
		int centerBlank = (int) (((eachWidth - widthCenter) / blankWidth) + leftBlank);
		for (int i = 0; i < centerBlank; i++) {
			sb.append(" ");
		}
		sb.append(center);
		int rightBlank = (int) (((eachWidth - widthtRight) / blankWidth));
		for (int i = 0; i < rightBlank; i++) {
			sb.append(" ");
		}
		sb.append(right);
		
		return writeLine(sb.toString(), FontSize.NORMAL,
				Alignment.ALIGN_OPPOSITE, LineType.TEXT);
	}
	
	@Override
	public PrintItemBean writeDetailItem(String left, String center,
			String right, FontSize size) {
		int eachWidth = paperWidth / 3;
		TextPaint paint = getPaint(size);
		float widthLeft = paint.measureText(left);
		float widthCenter = paint.measureText(center);
		float widthtRight = paint.measureText(right);
		float blankWidth = paint.measureText(" ");
		StringBuilder sb = new StringBuilder();
		int leftBlank = (int) (((eachWidth - widthLeft) / 2) / blankWidth);
		for (int i = 0; i < leftBlank; i++) {
			sb.append(" ");
		}
		sb.append(left);
		int centerBlank = (int) (((eachWidth - widthCenter) / blankWidth) + leftBlank);
		for (int i = 0; i < centerBlank; i++) {
			sb.append(" ");
		}
		sb.append(center);
		int rightBlank = (int) (((eachWidth - widthtRight) / blankWidth));
		for (int i = 0; i < rightBlank; i++) {
			sb.append(" ");
		}
		sb.append(right);
		
		return writeLine(sb.toString(), FontSize.NORMAL,
				Alignment.ALIGN_OPPOSITE, LineType.TEXT);
	}
	
	@Override
	public PrintItemBean writeTextJustified(String left, String right,
			int splitWidth, FontSize size, Alignment wrapAlign) {
		TextPaint paint = getPaint(size);
		float widthLeft = paint.measureText(left);
		float widthtRight = paint.measureText(right);
		float blankWidth = paint.measureText(" ");
		
		if ((widthLeft + splitWidth + widthtRight) > paperWidth) {
			// 此处要考虑换行
			StringBuilder sb = new StringBuilder(left);
			for (int i = 0; i < splitWidth; i++) {
				sb.append(" ");
			}
			
			PrintItemBean item = writeLine(sb.toString(), size,
					Alignment.ALIGN_NORMAL, LineType.TEXT);
			item.type = LineType.TEXT;
			item.innerType = AlignType.TEXT_JUSTIFIED;
			item.wrapLeft = (int) (widthLeft + splitWidth);
			item.wrapAlign = wrapAlign;
			
			StaticLayout layout = new StaticLayout(right, getPaint(size),
					(int) (paperWidth - widthLeft - blankWidth), wrapAlign, 1F,
					0, false);
			item.tag = layout;
			paperHeight += layout.getHeight();
			return item;
		} else {
			// 此处不考虑换行的问题
			
			int count = (int) ((paperWidth - widthLeft - widthtRight) / blankWidth);
			StringBuilder sb = new StringBuilder(left);
			
			for (int i = 0; i < count; i++) {
				sb.append(" ");
			}
			sb.append(right);
			return writeLine(sb.toString(), size, Alignment.ALIGN_CENTER,
					LineType.TEXT);
		}
	}
	
	@Override
	public PrintItemBean writeLine(String text, FontSize fontSize,
			Alignment align, LineType type) {
		
		if (null == printDatasoure) {
			printDatasoure = new ArrayList<PrintItemBean>();
			paperHeight = 0;
		}
		PrintItemBean item = new PrintItemBean();
		item.text = text;
		item.align = align;
		item.fontSize = fontSize;
		item.type = type;
		if (item.type == LineType.TEXT) {
			if (item.fontSize == FontSize.BIG) {
				StaticLayout layout = new StaticLayout(item.text,
						getPaint(item.fontSize), paperWidth, item.align, 1F, 0,
						false);
				item.tag = layout;
				paperHeight += layout.getHeight();
				if (layout.getLineCount() > 1 && item.align != item.wrapAlign) {
					layout = new StaticLayout(item.text,
							getPaint(item.fontSize), paperWidth,
							item.wrapAlign, 1F, 0, true);
				}
				item.tag = layout;
			} else if (item.fontSize == FontSize.NORMAL) {
				
				StaticLayout layout = new StaticLayout(item.text,
						getPaint(item.fontSize), paperWidth, item.align, 1F, 0,
						false);
				if (layout.getLineCount() > 1 && item.align != item.wrapAlign) {
					layout = new StaticLayout(item.text,
							getPaint(item.fontSize), paperWidth,
							item.wrapAlign, 1F, 0, true);
				}
				item.tag = layout;
				paperHeight += layout.getHeight();
				
			}
		} else if (item.type == LineType.BARCODE) {
			try {
				Bitmap bmp = createBarcode(item.text, BarcodeFormat.CODE_128,
						400, 80);
				item.tag = bmp;
				paperHeight += bmp.getHeight();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		printDatasoure.add(item);
		return item;
	}
	
	/**
	 * @Title: CreateCode
	 * @param 条码值
	 * @param 条码格式
	 * @param 条码宽度
	 * @param 条码宽度
	 */
	private Bitmap createBarcode(String str, BarcodeFormat type, int bmpWidth,
			int bmpHeight) throws Exception {
		
		BitMatrix matrix = null;
		
		try {
			matrix = (new MultiFormatWriter()).encode(str, type, bmpWidth,
					bmpHeight);
		} catch (WriterException var10) {
			var10.printStackTrace();
			throw new Exception("Failed to encode bitmap");
		}
		
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		
		for (int bitmap = 0; bitmap < height; ++bitmap) {
			for (int x = 0; x < width; ++x) {
				if (matrix.get(x, bitmap)) {
					pixels[bitmap * width + x] = -16777216;
				} else {
					pixels[bitmap * width + x] = -1;
				}
			}
		}
		
		Bitmap var11 = Bitmap.createBitmap(width, height,
				Config.ARGB_8888);
		var11.setPixels(pixels, 0, width, 0, 0, width, height);
		return var11;
	}
	
	protected void createImage() {
		try {
			// 打印高度计算：每多一行加30
			Bitmap bitmap = Bitmap.createBitmap(paperWidth, paperHeight,
					Config.ARGB_8888);
			
			Canvas canvas = new Canvas(bitmap);
			
			Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
			p.setColor(Color.WHITE);// 设置灰色
			p.setStyle(Paint.Style.FILL);// 设置填满
			canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), p);// 正方形
			
			int space = 0;
			for (int i = 0; i < printDatasoure.size(); i++) {
				PrintItemBean item = printDatasoure.get(i);
				canvas.translate(0, space);
				if (item.innerType == AlignType.NONE) {
					if (item.tag instanceof StaticLayout) {
						// 打印的内空为字
						StaticLayout layout = (StaticLayout) item.tag;
						space = layout.getHeight();
						layout.draw(canvas);
					} else if (item.tag instanceof Bitmap) {
						Bitmap bm = (Bitmap) item.tag;
						canvas.drawBitmap(bm, (paperWidth - bm.getWidth()) / 2,
								0, null);
						space = bm.getHeight();
					}
				} else if (item.innerType == AlignType.TEXT_JUSTIFIED) {
					
					StaticLayout leftLayout = new StaticLayout(item.text,
							getPaint(item.fontSize), paperWidth, item.align,
							1F, 0, false);
					leftLayout.draw(canvas);
					StaticLayout layout = (StaticLayout) item.tag;
					canvas.translate(item.wrapLeft, 0);
					layout.draw(canvas);
					space = layout.getHeight();
					canvas.translate(-item.wrapLeft, 0);
				}
			}
			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
			
			System.out.println(imagePath);
			FileOutputStream os = new FileOutputStream(new File(imagePath));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
