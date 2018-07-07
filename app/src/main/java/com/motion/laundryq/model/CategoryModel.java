package com.motion.laundryq.model;

public class CategoryModel {
    private String categoryID;
    private String categoryName;
    private String categoryUnit;
    private Integer categoryPrice;

    public CategoryModel() {
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryUnit() {
        return categoryUnit;
    }

    public void setCategoryUnit(String categoryUnit) {
        this.categoryUnit = categoryUnit;
    }

    public Integer getCategoryPrice() {
        return categoryPrice;
    }

    public void setCategoryPrice(Integer categoryPrice) {
        this.categoryPrice = categoryPrice;
    }
}
