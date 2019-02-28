package com.v2infotech.android.tiktok.model;

public class SignUpResponseData {


    public String emailAddress;

    public String userName;

    public String userPassword;

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


    public SignUpResponseData() {
    }

    public int getStataus() {
        return Stataus;
    }

    public void setStataus(int stataus) {
        Stataus = stataus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int Stataus;
    public String message;

    public SignUpResponseData(String emailAddress, String userName, String userPassword) {
        this.emailAddress = emailAddress;
        this.userName = userName;
        this.userPassword = userPassword;

    }
}
