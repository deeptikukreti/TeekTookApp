package com.v2infotech.android.tiktok.widget;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.v2infotech.android.tiktok.R;

import java.io.InputStream;

import tiktok.android.v2infotech.com.camerarecoder.egl.filter.GlBilateralFilter;
import tiktok.android.v2infotech.com.camerarecoder.egl.filter.GlBoxBlurFilter;
import tiktok.android.v2infotech.com.camerarecoder.egl.filter.GlBulgeDistortionFilter;
import tiktok.android.v2infotech.com.camerarecoder.egl.filter.GlCGAColorspaceFilter;
import tiktok.android.v2infotech.com.camerarecoder.egl.filter.GlFilter;
import tiktok.android.v2infotech.com.camerarecoder.egl.filter.GlFilterGroup;
import tiktok.android.v2infotech.com.camerarecoder.egl.filter.GlGaussianBlurFilter;
import tiktok.android.v2infotech.com.camerarecoder.egl.filter.GlGrayScaleFilter;
import tiktok.android.v2infotech.com.camerarecoder.egl.filter.GlInvertFilter;
import tiktok.android.v2infotech.com.camerarecoder.egl.filter.GlLookUpTableFilter;
import tiktok.android.v2infotech.com.camerarecoder.egl.filter.GlMonochromeFilter;
import tiktok.android.v2infotech.com.camerarecoder.egl.filter.GlSepiaFilter;
import tiktok.android.v2infotech.com.camerarecoder.egl.filter.GlSharpenFilter;
import tiktok.android.v2infotech.com.camerarecoder.egl.filter.GlSphereRefractionFilter;
import tiktok.android.v2infotech.com.camerarecoder.egl.filter.GlToneCurveFilter;
import tiktok.android.v2infotech.com.camerarecoder.egl.filter.GlToneFilter;
import tiktok.android.v2infotech.com.camerarecoder.egl.filter.GlVignetteFilter;
import tiktok.android.v2infotech.com.camerarecoder.egl.filter.GlWeakPixelInclusionFilter;


public enum Filters {
    NORMAL,
    BILATERAL,
    BOX_BLUR,
    BULGE_DISTORTION,
    CGA_COLOR_SPACE,
    GAUSSIAN_BLUR,
    GLAY_SCALE,
    INVERT,
    LOOKUP_TABLE,
    MONOCHROME,
    OVERLAY,
    SEPIA,
    SHARPEN,
    SPHERE_REFRACTION,
    TONE_CURVE,
    TONE,
    VIGNETTE,
    WEAKPIXELINCLUSION,
    FILTER_GROUP;

    public static GlFilter getFilterInstance(Filters filter, Context context) {
        switch (filter) {
            case BILATERAL:
                return new GlBilateralFilter();
            case BOX_BLUR:
                return new GlBoxBlurFilter();
            case BULGE_DISTORTION:
                return new GlBulgeDistortionFilter();
            case CGA_COLOR_SPACE:
                return new GlCGAColorspaceFilter();
            case GAUSSIAN_BLUR:
                return new GlGaussianBlurFilter();
            case GLAY_SCALE:
                return new GlGrayScaleFilter();
            case INVERT:
                return new GlInvertFilter();
            case LOOKUP_TABLE:
                return new GlLookUpTableFilter(BitmapFactory.decodeResource(context.getResources(), R.drawable.lookup_sample));
            case MONOCHROME:
                return new GlMonochromeFilter();
            case OVERLAY:
                return new GlBitmapOverlaySample(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round));
            case SEPIA:
                return new GlSepiaFilter();
            case SHARPEN:
                return new GlSharpenFilter();
            case SPHERE_REFRACTION:
                return new GlSphereRefractionFilter();
            case TONE_CURVE:
                try {
                    InputStream inputStream = context.getAssets().open("acv/tone_cuver_sample.acv");
                    return new GlToneCurveFilter(inputStream);
                } catch (Exception e) {
                    return new GlFilter();
                }
            case TONE:
                return new GlToneFilter();
            case VIGNETTE:
                return new GlVignetteFilter();
            case WEAKPIXELINCLUSION:
                return new GlWeakPixelInclusionFilter();
            case FILTER_GROUP:
                return new GlFilterGroup(new GlMonochromeFilter(), new GlVignetteFilter());

            default:
                return new GlFilter();
        }

    }

}
