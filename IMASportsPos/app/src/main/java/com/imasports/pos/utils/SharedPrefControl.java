package com.imasports.pos.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.framwork.utils.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * Created by lenovo on 2017/8/9.
 */

public class SharedPrefControl {

    private Context mContext;

    public SharedPrefControl(Context context) {
        mContext = context;
    }

    /**
     * [读取SharedPreferences值]
     *
     * @param paramString
     * @param tag
     * @param defult
     * @return
     * @Title: getSPValue
     */
    public String getSPValue(String paramString, String tag, String defult) {
        SharedPreferences sp = mContext.getSharedPreferences(paramString, 0);
        if (sp != null) {
            return sp.getString(tag, defult);
        }
        return defult;
    }

    /**
     * [读取SharedPreferences值]
     *
     * @param paramString
     * @param tag
     * @param defult
     * @return
     * @Title: getSPValue
     */
    public int getSPValue(String paramString, String tag, int defult) {
        SharedPreferences sp = mContext.getSharedPreferences(paramString, 0);
        if (sp != null) {
            return sp.getInt(tag, defult);
        }
        return defult;
    }

    /**
     * [读取SharedPreferences值]
     *
     * @param paramString
     * @param tag
     * @param defult
     * @return
     * @Title: getSPValue
     */
    public boolean getSPValue(String paramString, String tag, boolean defult) {
        SharedPreferences sp = mContext.getSharedPreferences(paramString, 0);
        if (sp != null) {
            return sp.getBoolean(tag, defult);
        }
        return defult;
    }

    /**
     * [设置SharedPreferences 值]
     *
     * @param paramString
     * @param tag
     * @param value
     * @return
     * @Title: setSPValue
     */
    public boolean setSPValue(String paramString, String tag, String value) {
        SharedPreferences sp = mContext.getSharedPreferences(paramString, 0);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(tag, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * [设置SharedPreferences 值]
     *
     * @param paramString
     * @param tag
     * @param value
     * @return
     * @Title: setSPValue
     */
    public boolean setSPValue(String paramString, String tag, int value) {
        SharedPreferences sp = mContext.getSharedPreferences(paramString, 0);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(tag, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * [设置SharedPreferences 值]
     *
     * @param paramString
     * @param tag
     * @param value
     * @return
     * @Title: setSPValue
     */
    public boolean setSPValue(String paramString, String tag, boolean value) {
        SharedPreferences sp = mContext.getSharedPreferences(paramString, 0);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(tag, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * [批量设置SharedPreferences 值]
     *
     * @param paramString
     * @param map
     * @return
     * @Title: setSPValue
     */
    public boolean setSPValue(String paramString, Map<String, String> map) {
        SharedPreferences sp = mContext.getSharedPreferences(paramString, 0);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                editor.putString(entry.getKey(), entry.getValue());
            }
            return editor.commit();
        }
        return false;
    }

    /**
     * 保存对象到本地
     *
     * @param paramString
     * @param object
     * @param key
     * @return
     */
    public boolean setObjectToShare(String paramString, Object object, String key) {
        SharedPreferences share = mContext.getSharedPreferences(paramString, 0);
        if (object == null) {
            SharedPreferences.Editor editor = share.edit().remove(key);
            return editor.commit();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // 将对象放到OutputStream中
        // 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray()));
        try {
            baos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = share.edit();
        // 将编码后的字符串写到base64.xml文件中
        editor.putString(key, objectStr);
        return editor.commit();
    }

    /**
     * 获取保存本地的对象
     *
     * @param paramString
     * @param key
     * @return
     */
    public Object getObjectFromShare(String paramString, String key) {
        SharedPreferences share = mContext.getSharedPreferences(paramString, 0);
        try {
            String wordBase64 = share.getString(key, "");
            // 将base64格式字符串还原成byte数组
            if (wordBase64 == null || wordBase64.equals("")) { // 不可少，否则在下面会报java.io.StreamCorruptedException
                return null;
            }
            byte[] objBytes = Base64.decode(wordBase64);
            ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            // 将byte数组转换成product对象
            Object obj = ois.readObject();
            bais.close();
            ois.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
