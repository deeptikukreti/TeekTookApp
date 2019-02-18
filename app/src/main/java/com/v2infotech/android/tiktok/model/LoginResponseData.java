package com.v2infotech.android.tiktok.model;

public class LoginResponseData {

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String emailAddress;

    public String userName;

    public String userPassword;

    public LoginResponseData() {

    }

    public LoginResponseData(String emailAddress, String userName, String userPassword) {
        this.emailAddress = emailAddress;
        this.userName = userName;
        this.userPassword = userPassword;

    }


}
