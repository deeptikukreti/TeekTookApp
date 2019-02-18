package com.v2infotech.android.tiktok.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponseParser {
    @SerializedName("Version")
    public String Version;
    @SerializedName("responseMessage")
    public String responseMessage;
    @SerializedName("status")
    public String status;
   // @SerializedName("EmployeeDetails")
 //  public List<LoginEmployeeDetailResponse> employeeDetail;
}
