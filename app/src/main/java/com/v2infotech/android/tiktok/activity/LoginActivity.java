package com.v2infotech.android.tiktok.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;
import com.v2infotech.android.tiktok.R;
import com.v2infotech.android.tiktok.Utils.CircleTransform;
import com.v2infotech.android.tiktok.Utils.CommonMethod;
import com.v2infotech.android.tiktok.Utils.Contants;
import com.v2infotech.android.tiktok.Utils.Utility;
import com.v2infotech.android.tiktok.model.LoginResponseParser;
import com.v2infotech.android.tiktok.progressbar.BallTriangleDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static com.v2infotech.android.tiktok.Utils.Contants.BASE_URL;
import static com.v2infotech.android.tiktok.Utils.Contants.LOGIN_API;
import static com.v2infotech.android.tiktok.Utils.Contants.NO_INERNET_CONNECTION;
import static com.v2infotech.android.tiktok.Utils.Contants.REGISTER_CONTROLLER;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView forgot_password_txt;
    private EditText user_name_txt, user_password_txt;
    private ImageView user_profile, facebook, google, twitter;
    private Button login_btn, signup_btn;
    private CheckBox remember_me_checkbox;
    private BallTriangleDialog pDialog;
    private String mResponce;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    LoginResponseParser loginResponceParser;
    CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    String name, password, Message;
    int Status;
    JSONObject jsonObject;

    class C04811 implements TextWatcher {
        C04811() {
        }

        public void afterTextChanged(Editable mEdit) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            LoginActivity.this.remember_me_checkbox.setChecked(false);
        }
    }

    class C04822 implements TextWatcher {
        C04822() {
        }

        public void afterTextChanged(Editable mEdit) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            LoginActivity.this.remember_me_checkbox.setChecked(false);
            if (LoginActivity.this.user_password_txt.getText().toString().startsWith(" ")) {
                CommonMethod.showAlert("Spaces not allowed", LoginActivity.this);
                LoginActivity.this.user_password_txt.setText("");
            }
        }
    }


    class C04833 implements View.OnFocusChangeListener {
        C04833() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
//            if (hasFocus) {
//                LoginActivity.this.rlBackgroundAdmission.setBackgroundResource(R.drawable.round_rectangle);
//            }
//            if (!hasFocus) {
//                LoginActivity.this.rlBackgroundAdmission.setBackgroundResource(R.drawable.roundgrey_rectangle);
//            }
        }
    }

    class C04844 implements View.OnFocusChangeListener {
        C04844() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
//            if (hasFocus) {
//                Login.this.rlBackgroundPassword.setBackgroundResource(R.drawable.round_rectangle);
//            }
//            if (!hasFocus) {
//                Login.this.rlBackgroundPassword.setBackgroundResource(R.drawable.roundgrey_rectangle);
//            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sp = getSharedPreferences("USER_SESSION_ID", Context.MODE_PRIVATE);
        String session_id = sp.getString("session_id", "");
        jsonObject = new JSONObject();
        FacebookSdk.sdkInitialize(getApplicationContext());
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        getId();
        this.loginPreferences = getSharedPreferences("loginPrefs", 0);
        this.loginPrefsEditor = this.loginPreferences.edit();
        this.saveLogin = Boolean.valueOf(this.loginPreferences.getBoolean("saveLogin", false));
        if (this.saveLogin.booleanValue()) {
            this.user_name_txt.setText(this.loginPreferences.getString("user_name", ""));
            this.user_password_txt.setText(this.loginPreferences.getString("password", ""));
            this.remember_me_checkbox.setChecked(true);
        }

        this.user_name_txt.addTextChangedListener(new C04811());
        this.user_password_txt.addTextChangedListener(new C04822());
        this.user_name_txt.setOnFocusChangeListener(new C04833());
        this.user_password_txt.setOnFocusChangeListener(new C04844());
        FacebookLogin();
        //googleLogin();
        method();

    }

    private void getId() {
        user_name_txt = findViewById(R.id.user_name_txt);
        user_password_txt = findViewById(R.id.user_password_txt);
        forgot_password_txt = findViewById(R.id.forgot_password_txt);
        user_profile = findViewById(R.id.user_profile);
        facebook = findViewById(R.id.facebook);
        google = findViewById(R.id.google);
        twitter = findViewById(R.id.twitter);
        login_btn = findViewById(R.id.login_btn);
        signup_btn = findViewById(R.id.signup_btn);
        remember_me_checkbox = findViewById(R.id.remember_me_checkbox);
        Picasso.with(this).load(R.drawable.user_profile).transform(new CircleTransform()).into(user_profile);

        signup_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        facebook.setOnClickListener(this);
        // google.setOnClickListener(this);
        remember_me_checkbox.setOnClickListener(this);
        forgot_password_txt.setOnClickListener(this);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.remember_me_checkbox:
                ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.user_name_txt.getWindowToken(), 0);
                this.name = this.user_name_txt.getText().toString();
                this.password = this.user_password_txt.getText().toString();
                if (this.remember_me_checkbox.isChecked()) {
                    this.loginPrefsEditor.putBoolean("saveLogin", true);
                    this.loginPrefsEditor.putString("user_name", this.name);
                    this.loginPrefsEditor.putString("password", this.password);
                    this.loginPrefsEditor.commit();
                } else {
                    this.loginPrefsEditor.clear();
                    this.loginPrefsEditor.commit();
                }
                doSomethingElse();
                return;

            case R.id.login_btn:

                if (checkvalidation()) {
                    String uid = user_name_txt.getText().toString();
                    String password = user_password_txt.getText().toString();
                    try {
                        jsonObject.put("uid", uid);
                        jsonObject.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    login();
                }
                 /*   DbHelper dbHelper = new DbHelper(this);
                    LoginResponseData loginResponseData = dbHelper.getUserDataByLoginId(user_name_txt.getText().toString());
                    if (loginResponseData != null) {
                        this. name =loginResponseData.getUserName();
                        //   this. email =loginResponseData.getEmailAddress();
                        this. password =user_password_txt.getText().toString();
                        String id_tikok="@"+loginResponseData.getUserPassword();
                        if (loginResponseData.getUserPassword().equals(password)) {
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("USER_PREFS", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("email", email);
                            editor.putString("tiktok_id", id_tikok);
                            editor.putString("name", name);
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            CommonMethod.showAlert("Please enter correct password", LoginActivity.this);
                        }
                    }*/
                return;
            case R.id.signup_btn:
                Intent intent1 = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent1);
                return;
            case R.id.facebook:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email", "user_birthday"));
                FacebookLogin();
                return;
            case R.id.google:
                //  googleSignIn();

            case R.id.forgot_password_txt:
                Intent intent2 = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent2);
            default:
                return;
        }
    }

    private void doSomethingElse() {
    }

    private boolean checkvalidation() {
        if (this.user_name_txt.getText().toString().trim().length() == 0) {
            CommonMethod.showAlert("please enter user name", this);
            //    this.ivUserIcon.setImageResource(R.mipmap.active_user_icon);
            return false;
        } else if (this.user_password_txt.getText().toString().length() == 0) {
            CommonMethod.showAlert("please enter password", this);
            return false;
        }
        return true;
    }


    //login with facebook........................
    public void FacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResults) {
                        final String facebookTokenId = loginResults.getAccessToken().getToken();
                        final String fbid = loginResults.getAccessToken().getUserId();
                        final String providerType = "facebook";
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResults.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object,
                                                            GraphResponse response) {
                                        // TODO Auto-generated method stub
                                        Log.v("ActivityLogin",
                                                response.toString());
                                        String profilePicUrl = null;
                                        try {
                                            Log.v("ActivityLogin", object.toString());
                                            if (object.has("picture")) {
                                                profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                            }
                                            String fbemail = object.getString("email");
                                            String first_name = object.getString("first_name");
                                            String last_name = object.getString("last_name");
                                            String email = object.getString("email");
                                            String id = object.getString("id");
                                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                                            Log.d("hhs", fbemail + " " + first_name + " " + last_name + " " + email);
                                            Toast.makeText(LoginActivity.this, fbemail + " " + first_name + " " + last_name + " " + email, Toast.LENGTH_LONG).show();
                                            String fbId = fbid;
                                            String fbTokenId = facebookTokenId;
//                                            detail = "?userMobile=" + fbemail + "&userPassword=" + fbId + "&type=social" + "&userId=" + fbId;
//                                            userId = fbId;
//                                            url = Contants.SERVICE_BASE_URL + Contants.Login + detail;
//                                            Login(url);

                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block

                                            e.printStackTrace();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters
                                .putString(
                                        "fields",
                                        "picture.type(large),id,name,email,gender,birthday,first_name,last_name");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Log.e("dd", "facebook login canceled");
                    }

                    @Override
                    public void onError(FacebookException e) {
                        Log.e("dd", "facebook login failed error");
                    }
                });
    }

    // how to get a hash code in android
    public void method() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.vinay.tiktok", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("MY KEY HASH:", sign);
                //  Toast.makeText(getApplicationContext(), sign, Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
            }
        }
    }

    /* //login with google....................
     public void googleLogin() {
         GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestIdToken(getString(R.string.default_web_client_id))
                 .requestEmail()
                 .build();
         mGoogleApiClient = new GoogleApiClient.Builder(this)
                 .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                     @Override
                     public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                     }
                 } *//* OnConnectionFailedListener *//*)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(Contants.LOG_TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(Contants.LOG_TAG, "onAuthStateChanged:signed_out");
                }// ...
            }
        };
    }


    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
*/
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(Contants.LOG_TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String emailId = acct.getId();
                        String tokenId = acct.getIdToken();
                        String email = acct.getEmail();
                        String name = acct.getDisplayName();
                        String given_name = acct.getGivenName();
                        String photo = String.valueOf(acct.getPhotoUrl());
                        Log.d("", "okk" + email + emailId + tokenId);
                        Toast.makeText(LoginActivity.this, email + " " + emailId + " " + tokenId + name + " " + given_name + "  " + "  " + photo, Toast.LENGTH_SHORT).show();
//                        detail = "?userMobile=" + email + "&userPassword=" + emailId + "&type=social" + "&userId=" + emailId;
//                        url = Contants.SERVICE_BASE_URL + Contants.Login + detail;
//                        userId = emailId;
//                        Login(url);
                        Log.d(Contants.LOG_TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(Contants.LOG_TAG, "signInWithCredential", task.getException());
                           /* Toast.makeText(ActivityLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();*/
                        }
                    }
                });
    }

    //*********************Simple login*********************//


    public void login() {
        if (Utility.isOnline(this)) {
            pDialog = new BallTriangleDialog(this);
            pDialog.show();
            String tag_json_obj = "timeStampRequest";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + REGISTER_CONTROLLER + LOGIN_API, jsonObject,
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
                                SharedPreferences settings = getSharedPreferences("USER_SESSION_ID", Context.MODE_PRIVATE);
                                settings.edit().clear().commit();
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("USER_SESSION_ID", 0);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("session_id", Message);
                                editor.commit();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                CommonMethod.showAlert(Message, LoginActivity.this);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    CommonMethod.showAlert("Network Issues Found", LoginActivity.this);
                    Log.e("Message from server", volleyError.toString());
                }
            });
            //AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(LoginActivity.this).add(jsonObjectRequest);
        } else {
            CommonMethod.showAlert(NO_INERNET_CONNECTION, LoginActivity.this);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
