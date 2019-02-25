package com.v2infotech.android.tiktok.framework;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by test on 4/23/2018.
 */

public class BackPress implements OnBackPressedListener  {

    private final FragmentActivity activity;

    public BackPress(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public void doBack() {
        activity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}
