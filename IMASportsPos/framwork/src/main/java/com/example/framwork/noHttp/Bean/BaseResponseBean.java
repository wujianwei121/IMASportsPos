package com.example.framwork.noHttp.Bean;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

public class BaseResponseBean {


    /**
     * 服务端业务数据
     */
    @JSONField(name = "Data")
    private String data;
    /**
     * 服务端业务错误码
     */
    @JSONField(name = "Code")
    private String code;


    @JSONField(name = "Message")
    private String message;
    @JSONField(name = "Success")
    private boolean success;


    @Override
    public String toString() {
        return "BaseResponseBean{" +
                "data='" + data + '\'' +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    /**
     * 业务是否成功
     */
    public boolean isSuccess() {
        if (getCode() != null)
            return getCode().equals("10000");
        else
            return true;
    }

    public String getCode() {
        if (code == null)
            return "";
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * 解析JsonObject，你的{@link E}必须提供默认无参构造
     *
     * @param clazz 要解析的实体类的class
     * @param <E>   实体类泛型
     * @return 实体类
     */
    public <E> E parseObject(Class<E> clazz) {
        E e = null;
        try {
            e = JSON.parseObject(getData(), clazz);
        } catch (Exception e1) {
            e1.printStackTrace();
            // 服务端数据格式错误时，返回data的空构造
            try {
                e = clazz.newInstance();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return e;
    }

    /**
     * 解析JsonArray，你的{@link E}必须提供默认无参构造
     *
     * @param clazz 要解析的实体类的class
     * @param <E>   实体类泛型
     * @return 实体类的List集合
     */
    public <E> List<E> parseList(Class<E> clazz) {
        List<E> e = new ArrayList<>();
        try {
            e = JSON.parseArray(getData(), clazz);
        } catch (Exception e1) {
        }
        return e;
    }

    /**
     * 解析JsonObject，你的{@link E}必须提供默认无参构造
     *
     * @param clazz 要解析的实体类的class
     * @param <E>   实体类泛型
     * @return 实体类
     */
    public static <E> E parseObj(String data, Class<E> clazz) {
        E e = null;
        try {
            e = JSON.parseObject(data, clazz);
        } catch (Exception e1) {
            e1.printStackTrace();
            // 服务端数据格式错误时，返回data的空构造
            try {
                e = clazz.newInstance();
            } catch (Exception e2) {
            }
        }
        return e;
    }

    /**
     * 解析JsonArray，封装为捕获异常
     *
     * @param clazz 要解析的实体类的class
     * @param <E>   实体类泛型
     * @return 实体类的List集合
     */
    public static <E> List<E> parseArray(String data, Class<E> clazz) {
        List<E> e = new ArrayList<>();
        try {
            e = JSON.parseArray(data, clazz);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return e;
    }
}
