package com.v2infotech.android.tiktok.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.v2infotech.android.tiktok.R;
import com.v2infotech.android.tiktok.Utils.CommonMethod;
import com.v2infotech.android.tiktok.Utils.Utility;
import com.v2infotech.android.tiktok.progressbar.BallTriangleDialog;

import org.json.JSONException;
import org.json.JSONObject;

import static com.v2infotech.android.tiktok.Utils.Contants.BASE_URL;
import static com.v2infotech.android.tiktok.Utils.Contants.FORGOT_PASSWORD_API;
import static com.v2infotech.android.tiktok.Utils.Contants.NO_INERNET_CONNECTION;
import static com.v2infotech.android.tiktok.Utils.Contants.REGISTER_CONTROLLER;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText verify_id_txt, verify_otp_edt;
    private Button next_btn, verify_otp_btn;
    JSONObject jsonObject;
    BallTriangleDialog pDialog;
    int Status;
    String Message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        jsonObject = new JSONObject();
        getIds();
    }

    private void getIds() {
        verify_id_txt = findViewById(R.id.verify_id_txt);
        verify_otp_edt = findViewById(R.id.verify_otp_edt);
        next_btn = findViewById(R.id.next_btn);
        verify_otp_btn = findViewById(R.id.verify_otp_btn);
        verify_otp_edt.setVisibility(View.GONE);
        verify_otp_btn.setVisibility(View.GONE);
        next_btn.setOnClickListener(this);
        verify_otp_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_btn:
                if (checkvalidation()) {
                    String email = verify_id_txt.getText().toString();
                    try {
                        jsonObject.put("email", email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    forgot_password();
                }
                return;
            default:
                return;

        }
    }

    private boolean checkvalidation() {
        if (this.verify_id_txt.getText().toString().trim().length() == 0) {
            CommonMethod.showAlert("please enter email id", this);
            return false;
        }
        return true;
    }


    public void forgot_password() {
        if (Utility.isOnline(this)) {
            pDialog = new BallTriangleDialog(this);
            pDialog.show();
            String tag_json_obj = "timeStampRequest";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + REGISTER_CONTROLLER + FORGOT_PASSWORD_API, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("Message from server", jsonObject.toString());
                            try {
                                Status = jsonObject.getInt("Status");
                                Message = jsonObject.getString("Message");
                                //Toast.makeText(LoginActivity.this, Status + " " + Message, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            if (Status == 1) {
                                Intent intent2 = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                startActivity(intent2);
                                finish();
                            } else {
                                CommonMethod.showAlert(Message, ForgotPasswordActivity.this);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    CommonMethod.showAlert("Network Issues Found", ForgotPasswordActivity.this);
                    Log.e("Message from server", volleyError.toString());
                }
            });
            //AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(ForgotPasswordActivity.this).add(jsonObjectRequest);
        } else {
            CommonMethod.showAlert(NO_INERNET_CONNECTION, ForgotPasswordActivity.this);
        }

    }
}