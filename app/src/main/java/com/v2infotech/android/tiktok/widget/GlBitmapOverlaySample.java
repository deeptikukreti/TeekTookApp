package com.v2infotech.android.tiktok.widget;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import tiktok.android.v2infotech.com.camerarecoder.egl.filter.GlOverlayFilter;


public class GlBitmapOverlaySample extends GlOverlayFilter {

    private Bitmap bitmap;

    public GlBitmapOverlaySample(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    protected void drawCanvas(Canvas canvas) {
        if (bitmap != null && !bitmap.isRecycled()) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }
}
