package com.v2infotech.android.tiktok.progressbar;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

import com.v2infotech.android.tiktok.R;


public class BallTriangleDialog extends AlertDialog {

    private CharSequence message;
    public BallTriangleDialog(Context context) {
        this(context, R.style.BallPulseLoadingDialog);
        setvalue();
    }

    public BallTriangleDialog(Context context, CharSequence message) {
        this(context);
        this.message = message;
        setvalue();
    }

    public BallTriangleDialog(Context context, CharSequence message, int theme) {
        this(context, theme);
        this.message = message;
        setvalue();
    }
    public BallTriangleDialog(Context context, int theme) {
        super(context, theme);
    }

    public BallTriangleDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setvalue();
    }

    private void setvalue() {
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.custom_progress_dialog);
        setCanceledOnTouchOutside(false);
        initMessage();
    }
    private void initMessage() {
        if (message != null && message.length() > 0) {
           // ((TextView) findViewById(R.id.spots_title)).setText(message);
        }
    }
}
