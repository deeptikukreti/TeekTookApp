package com.v2infotech.android.tiktok.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.v2infotech.android.tiktok.R;

public class PrivacyAndSafetyActivity extends AppCompatActivity {
TextView back_arrow_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_and_safety);

        back_arrow_icon = (TextView) findViewById(R.id.back_arrow_icon);

        back_arrow_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PrivacyAndSafetyActivity.this,SideNavigationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
