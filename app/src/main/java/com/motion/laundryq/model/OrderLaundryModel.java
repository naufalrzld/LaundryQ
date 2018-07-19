package com.motion.laundryq.model;

import java.util.ArrayList;
import java.util.List;

public class OrderLaundryModel {
    private String orderID;
    private String laundryID;
    private String userID;
    private String addressPick;
    private String addressDetailPick;
    private double latPick;
    private double lngPick;
    private int total;
    private String datePickup;
    private String timePickup;
    private String dateDelivery;
    private String timeDelivery;
    private String dateOrder;
    private List<CategoryModel> categories;

    public OrderLaundryModel() {
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getLaundryID() {
        return laundryID;
    }

    public void setLaundryID(String laundryID) {
        this.laundryID = laundryID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAddressPick() {
        return addressPick;
    }

    public void setAddressPick(String addressPick) {
        this.addressPick = addressPick;
    }

    public String getAddressDetailPick() {
        return addressDetailPick;
    }

    public void setAddressDetailPick(String addressDetailPick) {
        this.addressDetailPick = addressDetailPick;
    }

    public double getLatPick() {
        return latPick;
    }

    public void setLatPick(double latPick) {
        this.latPick = latPick;
    }

    public double getLngPick() {
        return lngPick;
    }

    public void setLngPick(double lngPick) {
        this.lngPick = lngPick;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDatePickup() {
        return datePickup;
    }

    public void setDatePickup(String datePickup) {
        this.datePickup = datePickup;
    }

    public String getTimePickup() {
        return timePickup;
    }

    public void setTimePickup(String timePickup) {
        this.timePickup = timePickup;
    }

    public String getDateDelivery() {
        return dateDelivery;
    }

    public void setDateDelivery(String dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    public String getTimeDelivery() {
        return timeDelivery;
    }

    public void setTimeDelivery(String timeDelivery) {
        this.timeDelivery = timeDelivery;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
    }
}
