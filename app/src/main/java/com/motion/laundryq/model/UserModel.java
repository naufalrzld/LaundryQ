package com.motion.laundryq.model;

public class UserModel {
    private String userID;
    private String nama;
    private String noTlp;
    private String email;
    private AddressModel address;

    public UserModel() {
    }

    public UserModel(String nama, String noTlp, String email) {
        this.nama = nama;
        this.noTlp = noTlp;
        this.email = email;
    }

    public UserModel(AddressModel address) {
        this.address = address;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoTlp() {
        return noTlp;
    }

    public void setNoTlp(String noTlp) {
        this.noTlp = noTlp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }
}
