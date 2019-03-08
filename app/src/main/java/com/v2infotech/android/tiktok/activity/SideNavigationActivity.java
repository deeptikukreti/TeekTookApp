package com.v2infotech.android.tiktok.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import com.v2infotech.android.tiktok.fragment.ProfileFragment;
import com.v2infotech.android.tiktok.progressbar.BallTriangleDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;

import static com.v2infotech.android.tiktok.Utils.Contants.BASE_URL;
import static com.v2infotech.android.tiktok.Utils.Contants.LOGIN_API;
import static com.v2infotech.android.tiktok.Utils.Contants.LOGOUT_API;
import static com.v2infotech.android.tiktok.Utils.Contants.NO_INERNET_CONNECTION;
import static com.v2infotech.android.tiktok.Utils.Contants.REGISTER_CONTROLLER;


public class SideNavigationActivity extends AppCompatActivity {

    private TextView help_centre_txt, terms_of_use_txt, privacy_policy_txt, copyright_policy_txt, share_profile_txt, back_arrow_icon, cache_size,
            clear_cache_txt, privacy_safety_txt, logout_txt;
    private BallTriangleDialog pDialog;
    JSONObject jsonObject;
    int Status;
    String Message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_navigation);
        jsonObject = new JSONObject();
        getIds();
        initializeCache();
    }

    private void getIds() {
        help_centre_txt = findViewById(R.id.help_centre_txt);
        terms_of_use_txt = findViewById(R.id.terms_of_use_txt);
        privacy_policy_txt = findViewById(R.id.privacy_policy_txt);
        copyright_policy_txt = findViewById(R.id.copyright_policy_txt);
        share_profile_txt = findViewById(R.id.share_profile_txt);
        back_arrow_icon = findViewById(R.id.back_arrow_icon);
        clear_cache_txt = findViewById(R.id.clear_cache_txt);
        privacy_safety_txt = findViewById(R.id.privacy_safety_txt);
        logout_txt = findViewById(R.id.logout_txt);
        help_centre_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SideNavigationActivity.this, HelpCentreActivity.class);
                startActivity(intent);
                finish();
            }
        });
        terms_of_use_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SideNavigationActivity.this, TermsOfUseActivity.class);
                startActivity(intent);
                finish();
            }
        });
        privacy_policy_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SideNavigationActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
                finish();
            }
        });
        copyright_policy_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SideNavigationActivity.this, CopyRightPolicyActivity.class);
                startActivity(intent);
                finish();
            }
        });
        privacy_safety_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SideNavigationActivity.this, PrivacyAndSafetyActivity.class);
                startActivity(intent);
                finish();
            }
        });
        back_arrow_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        clear_cache_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCache(SideNavigationActivity.this);
            }
        });
        share_profile_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "https://play.google.com/store/apps/details?id=com.zhiliaoapp.musically&hl=en_IN";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
            }
        });
        logout_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("USER_SESSION_ID", Context.MODE_PRIVATE);
                String session_id = sp.getString("session_id", "");
                try {
                    jsonObject.put("sessid", session_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//
                //
                logout();
            }
        });
    }

    private void initializeCache() {
        long size = 0;
        size += getDirSize(this.getCacheDir());
        size += getDirSize(this.getExternalCacheDir());
        ((TextView) findViewById(R.id.cache_size)).setText(readableFileSize(size));
    }

    public long getDirSize(File dir) {
        long size = 0;
        for (File file : dir.listFiles()) {
            if (file != null && file.isDirectory()) {
                size += getDirSize(file);
            } else if (file != null && file.isFile()) {
                size += file.length();
            }
        }
        return size;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0 Bytes";
        final String[] units = new String[]{"Bytes", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            Toast.makeText(this, "exception", Toast.LENGTH_SHORT).show();

        }
    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();
                    return false;
                }
                initializeCache();
            }
            initializeCache();
            //  ((TextView) findViewById(R.id.cache_size)).setText(readableFileSize(size));
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            //Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
            return dir.delete();
        } else {
            return false;
        }
    }

    public void logout() {
        if (Utility.isOnline(this)) {
            pDialog = new BallTriangleDialog(this);
            pDialog.show();
            String tag_json_obj = "timeStampRequest";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + REGISTER_CONTROLLER + LOGOUT_API, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("Message from server", jsonObject.toString());
                            try {
                                Status = jsonObject.getInt("Status");
                                Message = jsonObject.getString("Message");
                             //   Toast.makeText(SideNavigationActivity.this, Status + " " + Message, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            if (Status == 1) {
                                SharedPreferences settings = getSharedPreferences("USER_SESSION_ID", Context.MODE_PRIVATE);
                                settings.edit().clear().commit();
                                //  String session_id = settings.getString("session_id", "");
                                //  Toast.makeText(SideNavigationActivity.this, session_id, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SideNavigationActivity.this, HomeActivity.class));
                                finish();

                            } else {
                                CommonMethod.showAlert(Message, SideNavigationActivity.this);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    CommonMethod.showAlert("Network Issues Found", SideNavigationActivity.this);
                    Log.e("Message from server", volleyError.toString());
                }
            });
            //AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(SideNavigationActivity.this).add(jsonObjectRequest);
        } else {
            CommonMethod.showAlert(NO_INERNET_CONNECTION, SideNavigationActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
