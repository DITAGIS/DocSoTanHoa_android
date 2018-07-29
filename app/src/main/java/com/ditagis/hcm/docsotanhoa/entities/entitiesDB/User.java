package com.ditagis.hcm.docsotanhoa.entities.entitiesDB;

import java.util.List;

public class User {
    private String userName;
    private String passWord;
    private String nam;
    private String ky;
    private String dot;
    private String staffPhone;
    private String staffName;
    private List<String> lstMay;

    public User() {
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getNam() {
        return nam;
    }

    public void setNam(String nam) {
        this.nam = nam;
    }

    public String getKy() {
        return ky;
    }

    public void setKy(String ky) {
        this.ky = ky;
    }

    public String getDot() {
        return dot;
    }

    public void setDot(String dot) {
        this.dot = dot;
    }

    public String getStaffPhone() {
        return staffPhone;
    }

    public void setStaffPhone(String staffPhone) {
        this.staffPhone = staffPhone;
    }

    public List<String> getLstMay() {
        return lstMay;
    }

    public void setLstMay(List<String> lstMay) {
        this.lstMay = lstMay;
    }
}
