package com.v2infotech.android.tiktok.framework;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.Part;

/**
 * Created by test on 5/8/2018.
 */

public interface RetrofitInterface {

    @Multipart
    @PATCH("ProfilePhoto")
    Call<ResponseData> uploadImage(@Part MultipartBody.Part image, @Part("sadmission") RequestBody sadmission, @Part("address") RequestBody address, @Part("deviceId") RequestBody deviceId);
//    Call<ResponseData> detials
//            (
//
//            );
//,@Part("description") RequestBody description
}
