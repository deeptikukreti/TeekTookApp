package com.v2infotech.android.tiktok.model;

import com.google.gson.annotations.SerializedName;

public class UserProfileResponse {

        @SerializedName("Image_profile")
        public String Image_profile;
        @SerializedName("Image_Video")
        public String Image_video;
        @SerializedName("User_Name")
        public String User_Name;
        @SerializedName("User_tiktok_id")
        public String User_tiktok_id;
        @SerializedName("User_Bio")
        public String User_Bio;

    public String getImage_profile() {
        return Image_profile;
    }

    public void setImage_profile(String image_profile) {
        Image_profile = image_profile;
    }

    public String getImage_video() {
        return Image_video;
    }

    public void setImage_video(String image_video) {
        Image_video = image_video;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getUser_tiktok_id() {
        return User_tiktok_id;
    }

    public void setUser_tiktok_id(String user_tiktok_id) {
        User_tiktok_id = user_tiktok_id;
    }

    public String getUser_Bio() {
        return User_Bio;
    }

    public void setUser_Bio(String user_Bio) {
        User_Bio = user_Bio;
    }
}




