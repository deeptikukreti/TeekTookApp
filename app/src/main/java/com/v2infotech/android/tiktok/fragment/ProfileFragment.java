package com.v2infotech.android.tiktok.fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.v2infotech.android.tiktok.R;
import com.v2infotech.android.tiktok.Utils.CircleTransform;
import com.v2infotech.android.tiktok.Utils.CommonMethod;
import com.v2infotech.android.tiktok.Utils.Utility;
import com.v2infotech.android.tiktok.activity.EditProfileActivity;
import com.v2infotech.android.tiktok.activity.HomeActivity;
import com.v2infotech.android.tiktok.activity.LoginActivity;
import com.v2infotech.android.tiktok.activity.SideNavigationActivity;
import com.v2infotech.android.tiktok.database.DbHelper;
import com.v2infotech.android.tiktok.model.LoginResponseData;
import com.v2infotech.android.tiktok.model.UserProfileResponse;
import com.v2infotech.android.tiktok.progressbar.BallTriangleDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.POST;
import static com.v2infotech.android.tiktok.Utils.Contants.BASE_URL;
import static com.v2infotech.android.tiktok.Utils.Contants.GET_PROFILE_API;
import static com.v2infotech.android.tiktok.Utils.Contants.LOGIN_API;
import static com.v2infotech.android.tiktok.Utils.Contants.NO_INERNET_CONNECTION;
import static com.v2infotech.android.tiktok.Utils.Contants.PROFILE_CONTROLLER;
import static com.v2infotech.android.tiktok.Utils.Contants.REGISTER_CONTROLLER;


public class ProfileFragment extends Fragment implements View.OnClickListener {
    View view;
    TextView profileMenu, username, id_tiktok_txt;
    Button edit_profile_button;
    DbHelper dbHelper;
    EditText user_bio;
    ImageView imageView;
    JSONObject jsonObject;
    BallTriangleDialog pDialog;
    UserProfileResponse userProfileResponse;
    String session_id;
    private List<UserProfileResponse> userdata = new ArrayList<UserProfileResponse>();
    int Status;
    String name, contact, email, Profile_Pic, description, Message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        SharedPreferences sp = getActivity().getSharedPreferences("USER_SESSION_ID", Context.MODE_PRIVATE);
        session_id = sp.getString("session_id", "");
        jsonObject = new JSONObject();
        dbHelper = new DbHelper(getActivity());
        userProfileResponse = new UserProfileResponse();
        try {
            jsonObject.put("sessid", session_id);
            Log.d("s4ession_id", session_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getUserProfile();
        getIds();
        return view;
    }

    private void getIds() {
        imageView = view.findViewById(R.id.profile_photo);
        edit_profile_button = view.findViewById(R.id.edit_profile_button);
        id_tiktok_txt = view.findViewById(R.id.id_tiktok_txt);
        username = view.findViewById(R.id.username);
        profileMenu = view.findViewById(R.id.profileMenu);
        user_bio = view.findViewById(R.id.user_bio);

        profileMenu.setOnClickListener(this);
        edit_profile_button.setOnClickListener(this);
        userdata = dbHelper.GetAllUserListData();
        id_tiktok_txt.setText(session_id);
        for (int i = 0; i < userdata.size(); i++) {
            username.setText(userdata.get(i).getName());
            user_bio.setText(userdata.get(i).getDescription());
            Picasso.with(getContext()).load(userdata.get(i).getProfile_Pic()).placeholder(R.drawable.user_profile).transform(new CircleTransform()).into(imageView);
        }
        /*SharedPreferences sp = getActivity().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String email = sp.getString("name", "");
        String id_tiktok = sp.getString("tiktok_id", "");
        String biooo = sp.getString("bio", "");
        String image_uri = sp.getString("image_uri", "");
        String video_uri = sp.getString("video_uri", "");
        if (email != null && id_tiktok != null && biooo != null) {
            id_tiktok_txt.setText(id_tiktok);
            username.setText(email);
            user_bio.setText(biooo);
        }

        if (image_uri != null) {
            Uri uri_image = Uri.parse(image_uri);
            Picasso.with(getContext()).load(uri_image).placeholder(R.drawable.user_profile).transform(new CircleTransform()).into(imageView);
        } else {
            LoginResponseData loginResponseData = dbHelper.getUserDataByLoginId(email);
            if (loginResponseData != null) {

                String id_tikok = "@" + loginResponseData.getUserPassword();
                id_tiktok_txt.setText(id_tikok);

                String tik_tokusername = loginResponseData.getUserName();
                username.setText(tik_tokusername);
            }
        }
*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profileMenu:
                Intent i = new Intent(getActivity(), SideNavigationActivity.class);
                startActivity(i);
                // getActivity().finish();
                return;

            case R.id.edit_profile_button:
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
                return;

            default:
                return;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
       // getUserProfile();
        userdata = dbHelper.GetAllUserListData();
        id_tiktok_txt.setText(session_id);
        for (int i = 0; i < userdata.size(); i++) {
            username.setText(userdata.get(i).getName());
            user_bio.setText(userdata.get(i).getDescription());
            Picasso.with(getContext()).load(userdata.get(i).getProfile_Pic()).placeholder(R.drawable.user_profile).transform(new CircleTransform()).into(imageView);
        }
/*
        SharedPreferences sp = getActivity().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String email = sp.getString("name", "");
        String id_tiktok = sp.getString("tiktok_id", "");
        String biooo = sp.getString("bio", "");
        String image_uri = sp.getString("image_uri", "");
        String video_uri = sp.getString("video_uri", "");
        if (email != null && id_tiktok != null && biooo != null) {
            id_tiktok_txt.setText(id_tiktok);
            username.setText(email);
            user_bio.setText(biooo);
        } else {
            LoginResponseData loginResponseData = dbHelper.getUserDataByLoginId(email);
            if (loginResponseData != null) {

                String id_tikok = "@" + loginResponseData.getUserPassword();
                id_tiktok_txt.setText(id_tikok);

                String tik_tokusername = loginResponseData.getUserName();
                username.setText(tik_tokusername);
            }
        }
        if (image_uri != null) {
            Uri uri_image = Uri.parse(image_uri);
            Picasso.with(getContext()).load(uri_image).placeholder(R.drawable.user_profile).transform(new CircleTransform()).into(imageView);
        }*/


    }


    public void getUserProfile() {
        if (Utility.isOnline(getActivity())) {
            pDialog = new BallTriangleDialog(getActivity());
            pDialog.show();
            String tag_json_obj = "timeStampRequest";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(POST, BASE_URL + PROFILE_CONTROLLER + GET_PROFILE_API, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("Message from server", jsonObject.toString());

                            try {
                                Status = jsonObject.getInt("Status");
                                Message = jsonObject.getString("Message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (Status == 1) {
                                try {
                                    JSONArray jsonArray = new JSONArray();
                                    jsonArray = jsonObject.getJSONArray("newdata");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject json = jsonArray.getJSONObject(i);
                                        userProfileResponse.setName(json.getString("name"));
                                        userProfileResponse.setContact(json.getString("contact"));
                                        userProfileResponse.setEmail(json.getString("email"));
                                        userProfileResponse.setProfile_Pic(json.getString("Profile_Pic"));
                                        userProfileResponse.setDescription(json.getString("description"));
                                    }
                                    dbHelper.upsertUserData(userProfileResponse);

//                                Status = jsonObject.getInt("Status");
//                                Message = jsonObject.getString("Message");
                                    //Toast.makeText(LoginActivity.this, Status + " " + Message, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                CommonMethod.showAlert(Message, getActivity());
                            }
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                           /* if (Status == 1) {
                                SharedPreferences pref = getActivity().getSharedPreferences("USER_SESSION_ID", 0);
                                SharedPreferences.Editor editor = pref.edit();
                               // editor.putString("session_id", Message);
                                editor.commit();

                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                                // getActivity().finish();
                            } else {
                              //  CommonMethod.showAlert(Message, getActivity());
                            }*/
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    CommonMethod.showAlert("Network Issues Found", getActivity());
                    Log.e("Message from server", volleyError.toString());
                }
            });
            //AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(getActivity()).add(jsonObjectRequest);
        } else {
            CommonMethod.showAlert(NO_INERNET_CONNECTION, getActivity());
        }

    }


}
