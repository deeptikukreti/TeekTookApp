package com.v2infotech.android.tiktok.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.v2infotech.android.tiktok.R;


public class SquareActivity extends BaseCameraActivity {

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, SquareActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);
        onCreateActivity();
    }
}
