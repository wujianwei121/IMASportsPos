package com.imasports.pos.main.model.bean;

import java.io.Serializable;

/**
 * 功能实体
 *
 * @author A18ccms a18ccms_gmail_com
 * @ClassName: FuncItemBean
 * @date 2015年8月27日 下午2:21:43
 */
public class FuncItemBean implements Serializable {
    /**
     * 功能名称
     */
    private String funcName;
    /**
     * 功能ID
     */
    private int funcId;
    /**
     * 功能图片
     */
    private int funcImg;

    /**
     * 获取 funcName的值
     *
     * @return funcName
     * @see #funcName
     */

    public String getFuncName() {
        return funcName;
    }

    /**
     * 设置 funcName 的值
     *
     * @param funcName 要设置的 funcName
     * @see #funcName
     */

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    /**
     * 获取 funcId的值
     *
     * @return funcId
     * @see #funcId
     */

    public int getFuncId() {
        return funcId;
    }

    /**
     * 设置 funcId 的值
     *
     * @param funcId 要设置的 funcId
     * @see #funcId
     */

    public void setFuncId(int funcId) {
        this.funcId = funcId;
    }

    /**
     * 获取 funcImg的值
     *
     * @return funcImg
     * @see #funcImg
     */

    public int getFuncImg() {
        return funcImg;
    }

    /**
     * 设置 funcImg 的值
     *
     * @param funcImg 要设置的 funcImg
     * @see #funcImg
     */

    public void setFuncImg(int funcImg) {
        this.funcImg = funcImg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FuncItemBean that = (FuncItemBean) o;

        if (funcId != that.funcId) return false;
        if (funcImg != that.funcImg) return false;
        return funcName.equals(that.funcName);

    }

    @Override
    public int hashCode() {
        int result = funcName.hashCode();
        result = 31 * result + funcId;
        result = 31 * result + funcImg;
        return result;
    }
}
