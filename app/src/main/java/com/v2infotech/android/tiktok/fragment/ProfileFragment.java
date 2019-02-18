package com.v2infotech.android.tiktok.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.v2infotech.android.tiktok.R;
import com.v2infotech.android.tiktok.Utils.CircleTransform;
import com.v2infotech.android.tiktok.activity.EditProfileActivity;
import com.v2infotech.android.tiktok.activity.SideNavigationActivity;
import com.v2infotech.android.tiktok.database.DbHelper;
import com.v2infotech.android.tiktok.model.LoginResponseData;
import com.v2infotech.android.tiktok.model.UserProfileResponse;


public class ProfileFragment extends Fragment {
    View view;
    TextView profileMenu, username, id_tiktok_txt;
    Button edit_profile_button;
    DbHelper dbHelper;
    EditText user_bio;
    ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        dbHelper = new DbHelper(getActivity());
         imageView = view.findViewById(R.id.profile_photo);
        edit_profile_button = view.findViewById(R.id.edit_profile_button);
        id_tiktok_txt = view.findViewById(R.id.id_tiktok_txt);
        username = view.findViewById(R.id.username);
        profileMenu = view.findViewById(R.id.profileMenu);
        user_bio = view.findViewById(R.id.user_bio);
        Picasso.with(getContext()).load(R.drawable.user_profile).transform(new CircleTransform()).into(imageView);

        SharedPreferences sp = getActivity().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String email = sp.getString("name", "");
        String id_tiktok = sp.getString("tiktok_id", "");
        String biooo = sp.getString("bio", "");
        if (email != null && id_tiktok != null && biooo != null) {
            id_tiktok_txt.setText(id_tiktok);
            username.setText(email);
            user_bio.setText(biooo);
        }
/*
        SharedPreferences sp1 = getActivity().getSharedPreferences("USER_PREFS_EDIT", Context.MODE_PRIVATE);
        String email1 = sp.getString("user_name", "");
        String id_tiktok1 = sp.getString("tiktok_id", "");
        String bio = sp.getString("bio", "");

        if(sp1!=null){
            id_tiktok_txt.setText(email1);
            username.setText(id_tiktok1);
            user_bio.setText(bio);
        }

//        UserProfileResponse userProfileResponse = dbHelper.getUserProfileData(id_tiktok);
//
//        if (userProfileResponse != null) {
//            id_tiktok_txt.setText(userProfileResponse.getUser_tiktok_id());
//            username.setText(userProfileResponse.getUser_Name());
//            user_bio.setText(userProfileResponse.getUser_Bio());
//        }
else {*/
        else{
            LoginResponseData loginResponseData = dbHelper.getUserDataByLoginId(email);
        if (loginResponseData != null) {

            String id_tikok = "@" + loginResponseData.getUserPassword();
            id_tiktok_txt.setText(id_tikok);

            String tik_tokusername = loginResponseData.getUserName();
            username.setText(tik_tokusername);
        }
    }

     //  }

        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SideNavigationActivity.class);
                startActivity(i);
            }
        });

        edit_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(i);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sp = getActivity().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String email = sp.getString("name", "");
        String id_tiktok = sp.getString("tiktok_id", "");
        String biooo = sp.getString("bio", "");
        String image_uri = sp.getString("image_uri", "");
        if (email != null && id_tiktok != null && biooo != null) {
            id_tiktok_txt.setText(id_tiktok);
            username.setText(email);
            user_bio.setText(biooo);
        }
        else{
            LoginResponseData loginResponseData = dbHelper.getUserDataByLoginId(email);
            if (loginResponseData != null) {

                String id_tikok = "@" + loginResponseData.getUserPassword();
                id_tiktok_txt.setText(id_tikok);

                String tik_tokusername = loginResponseData.getUserName();
                username.setText(tik_tokusername);
            }
        }
        if(image_uri !=null){
            Uri uri_image=Uri.parse(image_uri);
            Picasso.with(getContext()).load(uri_image).placeholder(R.drawable.user_profile).transform(new CircleTransform()).into(imageView);
        }
/*
        SharedPreferences sp1 = getActivity().getSharedPreferences("USER_PREFS_EDIT", Context.MODE_PRIVATE);
        String email1 = sp.getString("user_name", "");
        String id_tiktok1 = sp.getString("tiktok_id", "");
        String bio = sp.getString("bio", "");

        if(sp1!=null){
            id_tiktok_txt.setText(email1);
            username.setText(id_tiktok1);
            user_bio.setText(bio);
        }

//        UserProfileResponse userProfileResponse = dbHelper.getUserProfileData(id_tiktok);
//
//        if (userProfileResponse != null) {
//            id_tiktok_txt.setText(userProfileResponse.getUser_tiktok_id());
//            username.setText(userProfileResponse.getUser_Name());
//            user_bio.setText(userProfileResponse.getUser_Bio());
//        }
else {*/

        }
    //}
}
