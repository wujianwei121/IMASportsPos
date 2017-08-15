package com.example.framwork.utils;

import com.yanzhenjie.nohttp.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期操作工具类.
 *
 * @author wujw
 */

public class DateUtil {
    public String FORMAT = "yyyy-MM-dd HH:mm:ss";
    public String FORMAT_YMD = "yyyy-MM-dd";

    /**
     * 内部类实现单例模式
     * 延迟加载，减少内存开销
     *
     * @author xuzhaohu
     */
    private static class SingletonHolder {
        private static DateUtil instance = new DateUtil();
    }

    /**
     * 私有的构造函数
     */
    private DateUtil() {

    }

    public static DateUtil getInstance() {
        return DateUtil.SingletonHolder.instance;
    }


    public Date str2Date(String str) {
        return str2Date(str, null);
    }

    public String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /*获取年月日*/
    public String getYMR(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(date);
    }

    public Date str2Date(String str, String format) {
        if (str == null || str.length() == 0) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(str);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;

    }

    public Calendar str2Calendar(String str) {
        return str2Calendar(str, null);

    }

    public Calendar str2Calendar(String str, String format) {

        Date date = str2Date(str, format);
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c;

    }

    /**
     * 获取当前时间作为文件名
     *
     * @return
     */
    public String dataToFileName() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    public String date2Str(Calendar c) {// yyyy-MM-dd HH:mm:ss
        return date2Str(c, null);
    }

    public String date2Str(Calendar c, String format) {
        if (c == null) {
            return null;
        }
        return date2Str(c.getTime(), format);
    }

    public String date2Str(Date d) {// yyyy-MM-dd HH:mm:ss
        return date2Str(d, null);
    }

    public String date2Str(Date d, String format) {// yyyy-MM-dd HH:mm:ss
        if (d == null) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(d);
        return s;
    }

    public String getCurDateStr() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
                + c.get(Calendar.DAY_OF_MONTH) + "-"
                + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE)
                + ":" + c.get(Calendar.SECOND);
    }

    /**
     * 获得当前日期的字符串格式
     *
     * @param format
     * @return
     */
    public String getCurDateStr(String format) {
        Calendar c = Calendar.getInstance();
        return date2Str(c, format);
    }

    // 格式到秒
    public String getMillon(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(time);
    }

    // 格式到天
    public String getDay(long time) {
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    } // 格式到月日

    public String getMonthDay(long time) {
        return new SimpleDateFormat("MM.dd").format(time);
    }

    // 格式到毫秒
    public String getSMillon(long time) {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(time);
    }

    public String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public long stringToLong(String strTime) {
        Date date = null; // String类型转成date类型
        try {
            date = stringToDate(strTime, FORMAT);
            if (date != null) {
                long currentTime = dateToLong(date); // date类型转成long类型
                return currentTime;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }// date类型转换为long类型

    // date要转换的date类型的时间
    public long dateToLong(Date date) {
        return date.getTime();
    }

    public String getChatTime(String timesamp) {
        long time = stringToLong(timesamp);
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(timesamp);
        int temp = Integer.parseInt(sdf.format(today))
                - Integer.parseInt(sdf.format(otherDay));

        switch (temp) {
            case 0:
                result = getHourAndMin(time);
                break;
            case 1:
                result = "昨天 " + getHourAndMin(time);
                break;
            case 2:
                result = "前天 " + getHourAndMin(time);
                break;

            default:
                result = getMonthDay(time);
                break;
        }
        return result;

    }

    /**
     * fasle:输入的日期小于当前日期
     * true:输入的日期大于当前日期
     */
    public boolean compareDate(String DATE1) {

        boolean result = true;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(getCurDateStr());
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(dt1);
            c2.setTime(dt2);
            int t = c1.compareTo(c2);
            if (t == -1) {
                result = false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }


    public String getTodayOrYesterday(long date) {//date 是存储的时间戳
        //所在时区时8，系统初始时间是1970-01-01 80:00:00，注意是从八点开始，计算的时候要加回去
        int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        long today = (System.currentTimeMillis() + offSet) / 86400000;
        long start = (date + offSet) / 86400000;
        long intervalTime = start - today;
        //-2:前天,-1：昨天,0：今天,1：明天,2：后天
        String strDes = "";
        if (intervalTime == 0) {
            strDes = "今天";
        } else if (intervalTime == -1) {
            strDes = "昨天";
        } else {
            strDes = getDay(date);//直接显示时间
        }
        return strDes;
    }

    //将整型时间戳转化为格式化日期
    public String getFormatDate(long timestamp) {
        String formatDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        formatDate = sdf.format(new Date(timestamp * 1000));
        return formatDate;
    }

    public long mssToDay(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
//        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
//        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
//        long seconds = (mss % (1000 * 60)) / 1000;
        return days;
    }

    public long mssToMin(long mss) {
//        long days = mss / (1000 * 60 * 60 * 24);
//        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
//        long seconds = (mss % (1000 * 60)) / 1000;
        return minutes;
    }

    public int getYear() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        return year;
    }

    public int getMonth() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        return month;
    }

    public int getDay() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        return day;
    }
}
