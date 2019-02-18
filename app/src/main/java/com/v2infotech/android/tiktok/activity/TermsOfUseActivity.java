package com.v2infotech.android.tiktok.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.v2infotech.android.tiktok.R;
import com.v2infotech.android.tiktok.Utils.WebViewClientImpl;

public class TermsOfUseActivity extends AppCompatActivity {
    private WebView webView = null;
    private TextView back_arrow_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_use);

        this.webView = (WebView) findViewById(R.id.webview);

        back_arrow_icon = (TextView) findViewById(R.id.back_arrow_icon);

        back_arrow_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TermsOfUseActivity.this,SideNavigationActivity.class);
                startActivity(intent);
                finish();
            }
        });


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        webView.setWebViewClient(webViewClient);

        webView.loadUrl("https://www.tiktok.com/i18n/terms/");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}