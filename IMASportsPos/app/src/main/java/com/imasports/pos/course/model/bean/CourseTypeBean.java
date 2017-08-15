package com.imasports.pos.course.model.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/8/11.
 */

public class CourseTypeBean implements Serializable {
    private static final long serialVersionUID = -84111077840747L;
    /**
     * 一级分类标识
     */
    public static final int PARENTID = 0;
    private Long id;
    /**
     * 分类ID
     */
    private int categoryID;
    /**
     * 父分类ID  ParentId==0 一级分类
     */
    private int parentId;
    /**
     * 分类名称
     */
    private String categoryName;

    private boolean isPressed = false;

    public CourseTypeBean(Long id, int categoryID, int parentId,
                          String categoryName) {
        this.id = id;
        this.categoryID = categoryID;
        this.parentId = parentId;
        this.categoryName = categoryName;
    }

    public CourseTypeBean() {
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setIsPressed(boolean value) {
        isPressed = value;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseTypeBean that = (CourseTypeBean) o;

        if (categoryID != that.categoryID) return false;
        if (parentId != that.parentId) return false;
        return categoryName != null ? categoryName.equals(that.categoryName) : that.categoryName == null;

    }

    @Override
    public int hashCode() {
        int result = categoryID;
        result = 31 * result + parentId;
        result = 31 * result + (categoryName != null ? categoryName.hashCode() : 0);
        return result;
    }

}
