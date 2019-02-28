package com.v2infotech.android.tiktok.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.v2infotech.android.tiktok.R;
import com.v2infotech.android.tiktok.Utils.CommonMethod;
import com.v2infotech.android.tiktok.Utils.Utility;
import com.v2infotech.android.tiktok.database.DbHelper;
import com.v2infotech.android.tiktok.model.LoginResponseData;
import com.v2infotech.android.tiktok.model.LoginResponseParser;
import com.v2infotech.android.tiktok.model.SignUpResponseData;

import org.json.JSONException;
import org.json.JSONObject;

import static com.v2infotech.android.tiktok.Utils.Contants.BASE_URL;
import static com.v2infotech.android.tiktok.Utils.Contants.REGISTER_API;
import static com.v2infotech.android.tiktok.Utils.Contants.REGISTER_CONTROLLER;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    EditText full_name_edt, email_address_edt, password_edt, phone_number_txt;
    Button signup_btn;
    private ProgressDialog pDialog;
    private String mResponce;
    LoginResponseParser loginResponceParser;
    LoginResponseData loginResponseData;
    String name, email, pass, phone_number;
    JSONObject jsonObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signup);
        jsonObject = new JSONObject();
        getIds();

    }

    private void getIds() {
        full_name_edt = findViewById(R.id.full_name_edt);
        email_address_edt = findViewById(R.id.email_address_edt);
        password_edt = findViewById(R.id.password_edt);
        signup_btn = findViewById(R.id.signup_btn);
        phone_number_txt = findViewById(R.id.phone_number_txt);
        signup_btn.setOnClickListener(this);
    }

    private boolean checkvalidation() {
        if (this.full_name_edt.getText().toString().trim().length() == 0) {
            CommonMethod.showAlert("please enter name", this);
            full_name_edt.requestFocus();
            //    this.ivUserIcon.setImageResource(R.mipmap.active_user_icon);
            return false;
        } else if (this.email_address_edt.getText().toString().length() == 0) {
            CommonMethod.showAlert("please enter email address", this);
            email_address_edt.requestFocus();
            return false;
        } else if (phone_number_txt.getText().toString().length() == 0) {
            CommonMethod.showAlert("please enter phone number", this);
            return false;
        } else if (phone_number_txt.getText().toString().length() != 10) {
            CommonMethod.showAlert("please enter 10 digit phone number", this);
            return false;
        } else if (this.password_edt.getText().toString().length() == 0) {
            CommonMethod.showAlert("please enter password", this);
            password_edt.requestFocus();
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_btn:
                if (checkvalidation()) {
                    this.email = email_address_edt.getText().toString();
                    this.name = full_name_edt.getText().toString();
                    this.pass = password_edt.getText().toString();
                    this.phone_number = phone_number_txt.getText().toString();
                    try {
                        jsonObject.put("name", name);
                        jsonObject.put("email", email);
                        jsonObject.put("contact", phone_number);
                        jsonObject.put("password", pass);
                        Log.d("", "jsonobject " + jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    register();
                }
                DbHelper dbHelper = new DbHelper(this);
                this.loginResponseData = new LoginResponseData();
                this.loginResponseData = dbHelper.getUserDataByLoginId(email);
                if (this.loginResponseData == null) {
                    LoginResponseData loginResponseData1 = new LoginResponseData();
                    loginResponseData1.setEmailAddress(this.email);
                    loginResponseData1.setUserName(this.name);
                    loginResponseData1.setUserPassword(this.pass);
                    dbHelper.upsertUserData(loginResponseData1);

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("USER_PREFS", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("email", email);
                    editor.putString("tiktok_id", "@" + pass);
                    editor.putString("name", name);
                    editor.commit();
                    email_address_edt.setText("");
                    full_name_edt.setText("");
                    password_edt.setText("");
                    Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    CommonMethod.showAlert("User Already exist", this);
                }

                return;
            default:
                return;
        }
    }


    public void register() {
        if (Utility.isOnline(this)) {
            //pDialog = new BallPulseIndicatorDialog(context);
//            pDialog.show();
            String tag_json_obj = "timeStampRequest";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + REGISTER_CONTROLLER + REGISTER_API, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("Message from server", jsonObject.toString());
                            try {
                                String Status = jsonObject.getString("Status");
                                String Message = jsonObject.getString("Message");

                                Toast.makeText(SignupActivity.this, Status + " " + Message, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    CommonMethod.showAlert("Please upload image  1", SignupActivity.this);
                    Log.e("Message from server", volleyError.toString());
                }
            });
            //AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(SignupActivity.this).add(jsonObjectRequest);
        } else {
            CommonMethod.showAlert("Please upload image", SignupActivity.this);
        }

    }


}
