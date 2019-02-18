package com.v2infotech.android.tiktok.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.v2infotech.android.tiktok.R;
import com.v2infotech.android.tiktok.Utils.CommonMethod;
import com.v2infotech.android.tiktok.Utils.HttpUtility;
import com.v2infotech.android.tiktok.database.DbHelper;
import com.v2infotech.android.tiktok.model.LoginResponseData;
import com.v2infotech.android.tiktok.model.LoginResponseParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.v2infotech.android.tiktok.Utils.Contants.BASE_URL;
import static com.v2infotech.android.tiktok.Utils.Contants.LOGIN;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    EditText full_name_edt, email_address_edt, password_edt;
    Button signup_btn;
    private ProgressDialog pDialog;
    private String mResponce;
    LoginResponseParser loginResponceParser;
    LoginResponseData loginResponseData;
    String name, email, pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signup);
        getIds();

    }

    private void getIds() {
        full_name_edt = findViewById(R.id.full_name_edt);
        email_address_edt = findViewById(R.id.email_address_edt);
        password_edt = findViewById(R.id.password_edt);
        signup_btn = findViewById(R.id.signup_btn);
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
                        editor.putString("tiktok_id", "@"+pass);
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
                }
                return;
            default:
                return;
        }
    }


    private class SignUpAsync extends AsyncTask<String, Void, String> {
        private SignUpAsync() {
        }

        protected String doInBackground(String... params) {
            //      Toast.makeText(Login.this, "okk", Toast.LENGTH_SHORT).show();
            CallServiceCategory();
            return null;
        }

        protected void onPostExecute(String result) {
            hideProgressDialog();
            Gson gson = new Gson();
            try {
                loginResponceParser = (LoginResponseParser) gson.fromJson(mResponce, LoginResponseParser.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            if (loginResponceParser == null) {
                CommonMethod.showAlert(getString(R.string.networkError_Message), SignupActivity.this);
            } else if (loginResponceParser.status.equals("200")) {
//
//                for (int i = 0; i < loginResponceParser.employeeDetail.size(); i++) {
//                    LoginActivity.this.startActivity(new Intent(
//                            LoginActivity.this, SignupActivity.class));
//                    LoginActivity.this.finish();
//                }
            } else {
                CommonMethod.showAlert(loginResponceParser.responseMessage.toString().trim(), SignupActivity.this);
            }
        }

        protected void onPreExecute() {

            showProgressDialog();
        }

        protected void onProgressUpdate(Void... values) {
            // hideProgressDialog();
        }
    }


    private void showProgressDialog() {
        this.pDialog.show();
    }

    private void hideProgressDialog() {
        if (this.pDialog.isShowing()) {
            this.pDialog.dismiss();
        }
    }

    public String CallServiceCategory() {
        Map<String, String> params = new HashMap();
        params.put("EmpId", this.full_name_edt.getText().toString().trim());
        params.put("password", this.email_address_edt.getText().toString().trim());
        params.put("password", this.password_edt.getText().toString().trim());
        System.out.println("Params" + params);
        try {
            HttpUtility.sendPostRequestForLogin(BASE_URL + LOGIN, params);
            String[] response = HttpUtility.readMultipleLinesRespone();
            System.out.println("responsesssss" + response);
            if (0 < response.length) {
                String line = response[0];
                this.mResponce = line;
                System.out.println("zxZAxASXaX " + this.mResponce);
                return line;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        HttpUtility.disconnect();
        return this.mResponce;
    }
}
