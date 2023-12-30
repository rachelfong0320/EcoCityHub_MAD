package com.example.ecocity;

public class HelperClass {

    String username, gender, date, contNum, email,address , pass1;
    int point;
    boolean privacy;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContNum() {
        return contNum;
    }

    public void setContNum(String contNum) {
        this.contNum = contNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPass1() {
        return pass1;
    }

    public void setPass1(String pass1) {
        this.pass1 = pass1;
    }

    public HelperClass(String username, String gender, String date, String contNum, String email, String address, String pass1) {
        this.username = username;
        this.gender = gender;
        this.date = date;
        this.contNum = contNum;
        this.email = email;
        this.address = address;
        this.pass1 = pass1;
        this.privacy = false;
        this.point=20;
    }

    public HelperClass(String username, String gender, String date, String contNum, String email, String address, String pass1, boolean privacy, int point) {
        this.username = username;
        this.gender = gender;
        this.date = date;
        this.contNum = contNum;
        this.email = email;
        this.address = address;
        this.pass1 = pass1;
        this.privacy = privacy;
        this.point=point;
    }

    public boolean isPrivacy() {
        return privacy;
    }

    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
