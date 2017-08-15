package com.storemax.android.devices.printer;

import android.app.Activity;
import android.os.RemoteException;
import android.text.Layout.Alignment;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;

import com.huiyi.nypos.pay.thirdpay.aidl.AidlPrint;
import com.huiyi.nypos.pay.thirdpay.aidl.AidlThirdListener;
import com.landicorp.android.eptapi.device.Printer;
import com.landicorp.android.eptapi.device.Printer.Format;
import com.landicorp.android.eptapi.exception.RequestException;
import com.storemax.android.devices.bean.DeviceExceptionBean;
import com.storemax.android.devices.bean.PrintItemBean;
import com.storemax.android.devices.bean.PrintResultBean;
import com.storemax.android.devices.device.IDevice;
import com.storemax.android.devices.listener.OnDeviceOperaListener;
import com.storemax.android.devices.listener.OnDeviceStateListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LandiPrinter extends BasePrinter {

    private ArrayList<PrintItemBean> printDatasoure = null;

    private int paperWidth = 32;

    protected int paperHeight = 0;

    public static final int WIDTH = 32;

    OnDeviceOperaListener listener;

    Activity activity;
    IDevice idevice;

    public LandiPrinter(Activity activity, OnDeviceOperaListener lister,
                        IDevice idevice) {
        this.activity = activity;
        listener = lister;
        this.idevice = idevice;
    }

    @Override
    public PrintItemBean writeDetailTitle(String left, String center,
                                          String right, FontSize size) {
        String title = formatDetailTitle(left, center, right);
        return writeLine(title, size, Alignment.ALIGN_CENTER, LineType.TEXT);
    }

    @Override
    public PrintItemBean writeDetailItem(String left, String center,
                                         String right, FontSize size) {
        return writeLine(
                FormatGoodsListItems(new String[]{center, right}, false),
                size, Alignment.ALIGN_NORMAL, LineType.TEXT);
    }

    @Override
    public PrintItemBean writeTextJustified(String left, String right,
                                            int splitWidth, FontSize size, Alignment wrapAlign) {
        ArrayList<String> lines = FormatListItems(new String[]{left, right});
        for (int i = 0; null != lines && i < lines.size(); i++) {
            writeLine(lines.get(i), size, Alignment.ALIGN_CENTER, LineType.TEXT);
        }
        return null;
    }

    @Override
    public PrintItemBean writeLine(String text, FontSize fontSize,
                                   Alignment align, LineType type) {

        if (null == printDatasoure) {
            printDatasoure = new ArrayList<PrintItemBean>();
            paperHeight = 0;
        }

        ArrayList<String> lines = lineAutoWrap(text);
        PrintItemBean pb = new PrintItemBean();
        for (int i = 0; null != lines && i < lines.size(); i++) {
            PrintItemBean item = new PrintItemBean();
            if (i == 0) {
                pb = item;
            }

            item.text = lines.get(i);
            item.align = align;
            item.fontSize = fontSize;
            item.type = type;
            printDatasoure.add(item);
        }

        return pb;
    }

    @Override
    public PrintItemBean writeQrCode(String text) {
        return writeLine(text, FontSize.NORMAL, Alignment.ALIGN_CENTER,
                LineType.QR_CODE);
    }

    @Override
    public PrintItemBean writeSingleLine() {
        String text = "--------------------------------";
        return writeLine(text, FontSize.NORMAL, Alignment.ALIGN_CENTER,
                LineType.TEXT);
    }

    @Override
    public PrintItemBean writeDoubleLine() {
        String text = "================================";
        return writeLine(text, FontSize.NORMAL, Alignment.ALIGN_CENTER,
                LineType.TEXT);
    }


    @Override
    public void startNYPosPrint(AidlPrint aidlPrint) {
        Log.d("hmz", "startNYPosPrint");
        if (aidlPrint == null) {
            return;
        }
        JSONObject json = getPrintJsonObject();
        aidlPrint(aidlPrint, json);
    }

    /**
     * 获取汇宜机器打印Json对象
     *
     * @return
     */
    private JSONObject getPrintJsonObject() {
        JSONObject json = new JSONObject();
        JSONArray listJsonArray = new JSONArray();
        JSONObject itemJsonObj;
        try {
            for (int i = 0; null != printDatasoure && i < printDatasoure.size(); i++) {
                PrintItemBean item = printDatasoure.get(i);
                itemJsonObj = new JSONObject();
                if (item.type == LineType.TEXT
                        || item.type == LineType.SINGLE_LINE
                        || item.type == LineType.DOUBLE_LINE) {
                    itemJsonObj.put("content-type", "txt");
                } else if (item.type == LineType.BARCODE) {
                    itemJsonObj.put("content-type", "one-dimension");
                } else if (item.type == LineType.QR_CODE) {
                    itemJsonObj.put("content-type", "two-dimension");
                } else if (item.type == LineType.PICTURE) {
                    itemJsonObj.put("content-type", "jpg");
                } else {
                    itemJsonObj.put("content-type", "txt");
                }

                if (item.fontSize == FontSize.BIG) {
                    if (item.type == LineType.BARCODE) {
                        itemJsonObj.put("size", "2");//条码特殊处理
                    } else {
                        itemJsonObj.put("size", "3");
                    }
                } else if (item.fontSize == FontSize.NORMAL) {
                    itemJsonObj.put("size", "2");
                } else {
                    itemJsonObj.put("size", "1");
                }
                if (item.align == Alignment.ALIGN_CENTER) {
                    itemJsonObj.put("position", "center");
                } else if (item.align == Alignment.ALIGN_NORMAL) {
                    itemJsonObj.put("position", "left");
                } else {
                    itemJsonObj.put("position", "center");
                }
                itemJsonObj.put("content", item.text);
                listJsonArray.put(itemJsonObj);
            }
            json.put("spos", listJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 汇宜机器AIDL打印
     *
     * @param aidlPrint
     * @param json
     */
    private void aidlPrint(AidlPrint aidlPrint, JSONObject json) {
        String jsonStr = json.toString();
        Log.d("main", "jsonStr= " + jsonStr);
        if (!TextUtils.isEmpty(json.toString())) {
            try {
                aidlPrint.print(json.toString(), null, new AidlThirdListener.Stub() {
                    @Override
                    public void thirdSuccess(String s) throws RemoteException {
                        Log.d("hmz", "aidlPrint thirdSuccess");
                        if (null != listener) {
                            listener.onSuccess(BasePrinter.PRINT_SUCCESS, "", null);
                        }
                    }

                    @Override
                    public void thirdFailed(String s, String s1) throws RemoteException {
                        Log.e("hmz", "aidlPrint thirdFailed s=" + s + ",s1=" + s1);
                        if (null != listener) {
                            if (s.equals("240")) {
                                listener.onFail(BasePrinter.PRINT_NO_PAPER, s1, null);
                            } else {
                                listener.onFail(BasePrinter.PRINT_ERROR, "打印失败", null);
                            }

                        }

                    }
                });
            } catch (RemoteException e) {
                Log.e("hmz", "aidlPrint RemoteException e=" + e.getMessage());
                if (null != listener) {
                    listener.onThrowException(new DeviceExceptionBean(e));
                }
                e.printStackTrace();
            }
        }
    }

    @Override
    public void startPrint() {
        if (null != idevice) {
            idevice.openDevice(activity, new OnDeviceStateListener() {

                @Override
                public void onSuccess(int code, String msg, Object obj) {
                    print();

                }

                @Override
                public void onFail(int code, String msg, Object obj) {
                    listener.onFail(code, msg, obj);

                }

                @Override
                public void onCrash() {
                    listener.onCash();

                }

                @Override
                public void onThrowException(DeviceExceptionBean ex) {
                    listener.onThrowException(ex);

                }
            });
        }
    }

    private void print() {
        try {
            new Printer.Progress() {
                @Override
                public void doPrint(Printer landiPrinter) throws Exception {

                    for (int i = 0; null != printDatasoure && i < printDatasoure.size(); i++) {
                        PrintItemBean item = printDatasoure.get(i);
                        if (item.type == LineType.TEXT) {
                            Format format = new Format();
                            format.setHzScale(Format.HZ_SC2x2);
                            format.setHzSize(Format.HZ_DOT24x24);
                            format.setAscScale(Format.ASC_SC1x1);
                            format.setAscSize(Format.ASC_DOT7x7);

                            if (item.fontSize == FontSize.BIG) {
                                format.setHzScale(Format.HZ_SC2x2);
                                landiPrinter.setFormat(format);
                                switch (item.align) {
                                    case ALIGN_CENTER:
                                        landiPrinter.printMid(item.text + "\n");
                                        break;
                                    case ALIGN_NORMAL:
                                        landiPrinter.println(item.text);
                                        break;
                                    case ALIGN_OPPOSITE:

                                        break;
                                }
                            } else if (item.fontSize == FontSize.NORMAL) {
                                format.setHzScale(Format.HZ_SC1x1);
                                landiPrinter.setFormat(format);
                                switch (item.align) {
                                    case ALIGN_CENTER:
                                        landiPrinter.printMid(item.text + "\n");
                                        break;
                                    case ALIGN_NORMAL:
                                        landiPrinter.println(item.text);
                                        break;
                                    case ALIGN_OPPOSITE:

                                        break;
                                }
                            }
                        } else if (item.type == LineType.BARCODE) {
                            int width = 3;
                            if (item.innerWidth != 0) {
                                width = item.innerWidth;
                            }
                            landiPrinter.printBarCode(Printer.Alignment.CENTER,
                                    width, 48, item.text);
                        }
                    }
                    landiPrinter.feedLine(5);
                }

                @Override
                public void onFinish(int arg0) {
                    if (arg0 == Printer.ERROR_NONE) {
                        if (null != listener) {
                            listener.onSuccess(BasePrinter.PRINT_SUCCESS, "", null);
                        }
                    } else if (arg0 == Printer.ERROR_PAPERENDED) {
                        listener.onFail(BasePrinter.PRINT_NO_PAPER, getFailMsgByCode(arg0), null);
                    } else {
                        listener.onFail(BasePrinter.PRINT_ERROR, getFailMsgByCode(arg0), null);
                    }

                }

                @Override
                public void onCrash() {
                    if (null != listener) {
                        listener.onCash();
                    }
                }
            }.start();
        } catch (RequestException e) {
            if (null != listener) {
                listener.onThrowException(new DeviceExceptionBean(e));
            }
            e.printStackTrace();
        }

    }

    @Override
    protected TextPaint getPaint(FontSize size) {
        return null;
    }

    @Override
    protected TextPaint createPaint(int size) {
        return null;
    }

    private String getFailMsgByCode(int code) {
        switch (code) {
            case Printer.ERROR_NONE:
                return "正常状态，无错误";
            case Printer.ERROR_LIFTHEAD:
                return "打印头抬起";
            case Printer.ERROR_WORKON:
                return "打印机电源处于打开状态";
            case Printer.ERROR_BMBLACK:
                return "黑标探测器	检测到黑色信号";
            case Printer.ERROR_BUSY:
                return "打印机处于忙状态";
            case Printer.ERROR_NOBM:
                return "没有找到黑标";
            case Printer.ERROR_PAPERJAM:
                return "卡纸";
            case Printer.ERROR_MOTORERR:
                return "打印机芯故障(过快或者过慢)没有找到对齐位置,纸张回到原来位置";
            case Printer.ERROR_PAPERENDING:
                return "纸张将要用尽，还允许打印";
            case Printer.ERROR_LOWVOL:
                return "低压保护";
            case Printer.ERROR_BUFOVERFLOW:
                return "缓冲模式下所操作的位置超出范围";
            case Printer.ERROR_OVERHEAT:
                return "打印头过热";
            case Printer.ERROR_HARDERR:
                return "硬件错误";

            case Printer.ERROR_CUTPOSITIONERR:
                return "切纸刀不在原位";
            case Printer.ERROR_LOWTEMP:
                return "低温保护或AD 出错";
            case Printer.ERROR_COMMERR:
                return "手座机状态正常，但通讯失败";
            default:
                return "未定义的错误" + code;
        }
    }

    @Override
    public int getPrinterWidth() {
        return WIDTH;
    }

    @Override
    public PrintResultBean getResultInfo(Object obj) {
        return null;
    }

    /**
     * @param value
     * @param format 是否格式化
     * @return
     * @Title: FormatGoodsListItems
     */
    protected String FormatGoodsListItems(String[] value, boolean format) {
        String goodsPrice = "0.0";
        String goodsCount = "0";

        if (value.length == 2) {
            goodsPrice = value[1];
            goodsCount = value[0];
        }
        // 一行一共可以打印32个字母
        StringBuilder sb = new StringBuilder();
        if (format) {
            goodsCount = String.format("% 19.0f", Double.valueOf(goodsCount));
        } else {
            goodsCount = getBackSpace(goodsCount);
        }
        goodsPrice = String.format("% 13.2f", Double.valueOf(goodsPrice));
        sb.append(goodsCount);
        sb.append(goodsPrice);
        return sb.toString();
    }

    /**
     * 获取商品数量前的空格数及商品数量的字符串
     *
     * @param str
     * @return
     * @Title: getBackSpace
     */
    private String getBackSpace(String str) {
        StringBuilder sb = new StringBuilder();
        int num = 19 - str.length();
        if (num > 0) {
            for (int i = 0; i < num; i++) {
                sb.append(" ");
            }

        }
        sb.append(str);
        return sb.toString();
    }

    protected ArrayList<String> lineAutoWrap(String value) {
        ArrayList<String> printString = new ArrayList<String>();

        int width = calculatePlaces(value);

        if ((getPrinterWidth() - width) >= 0) {
            printString.add(value);
        } else {
            printString
                    .addAll(getSplitStringByLength(value, getPrinterWidth()));

        }
        return printString;
    }

    protected ArrayList<String> FormatListItems(String[] value) {
        ArrayList<String> printString = new ArrayList<String>();
        String leftString = null;
        String rightString = null;

        if (value.length == 2) {
            leftString = value[0];
            rightString = value[1];
        }
        int leftWidth = calculatePlaces(value[0]);
        int rightWidth = getPrinterWidth() - leftWidth;
        StringBuilder sb = new StringBuilder();
        sb.append(leftString);
        int blank = getPrinterWidth() - calculatePlaces(leftString)
                - calculatePlaces(rightString);
        if (blank > 0) {
            for (int i = 0; i < blank; i++) {
                sb.append(" ");
            }
            sb.append(rightString);
            printString.add(sb.toString());
        } else {
            ArrayList<String> right = getSplitStringByLength(rightString,
                    rightWidth);
            StringBuilder leftBlank = new StringBuilder();

            if (null != right && right.size() > 0) {
                printString.add(leftString + right.get(0));
            }
            for (int k = 0; k < leftWidth; k++) {
                leftBlank.append(" ");
            }
            for (int i = 1; i < right.size(); i++) {
                printString.add(leftBlank + right.get(i));
            }
        }
        return printString;
    }

    public static int calculatePlaces(String str) {
        int m = 0;
        char arr[] = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];
            m += getCharPlaces(c);
        }
        return m;
    }

    public static ArrayList<String> getSplitStringByLength(String text,
                                                           int length) {

        int valueSize = calculatePlaces(text);

        ArrayList<String> arrResult = new ArrayList<String>();
        int index = 0;
        do {
            if (valueSize - index > 0) {
                // 当前还有可取的字符串
                if (valueSize - index > length) {
                    // 此时还可以再分行
                    String strValue = getSubString(index, index + length, text);
                    arrResult.add(strValue);
                    index += calculatePlaces(strValue);
                } else {
                    // 此时不能再分行
                    arrResult.add(getSubString(index, valueSize, text));
                    break;
                }
            } else {
                break;
            }
        } while (true);
        Log.e("adsf", arrResult.size() + "");
        return arrResult;
    }

    public static String getSubString(int start, int end, String src) {
        int m = 0;
        StringBuilder sBuilder = new StringBuilder();

        char arr[] = src.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            int charlen = getCharPlaces(arr[i]);
            if (start < m + charlen && charlen + m <= end) {
                sBuilder.append(arr[i]);
                m += charlen;
            } else if (m + charlen >= end) {
                break;
            } else {
                m += charlen;
            }
        }
        return sBuilder.toString();
    }

    public static int getCharPlaces(char value) {
        int m = 0;
        if ((value >= 0x0391 && value <= 0xFFE5)) // 中文字符
        {
            m = m + 2;
        } else if ((value >= 0x0000 && value <= 0x00FF)) // 英文字符
        {
            m = m + 1;
        }
        return m;
    }

    public static String formatDetailTitle(String left, String center,
                                           String right) {
        int leftWidth = 10;
        int centerWidth = 10;
        int rightWidth = 12;
        StringBuilder sb = new StringBuilder();
        sb.append(left);
        int width = calculatePlaces(left);
        int blank = leftWidth - width;
        for (int i = 0; i < blank; i++) {
            sb.append(" ");
        }
        width = calculatePlaces(center);
        blank = centerWidth - width;
        for (int i = 0; i < blank; i++) {
            sb.append(" ");
        }
        sb.append(center);
        width = calculatePlaces(right);
        blank = rightWidth - width;
        for (int i = 0; i < blank; i++) {
            sb.append(" ");
        }
        sb.append(right);
        return sb.toString();
    }
}