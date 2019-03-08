package com.v2infotech.android.tiktok.fragment;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xvideoplayer.MxVideoPlayer;
import com.v2infotech.android.tiktok.R;
import com.v2infotech.android.tiktok.adapter.FollowingAdapter;
import com.v2infotech.android.tiktok.model.VideoInfo;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.SENSOR_SERVICE;

public class TabFragment2 extends Fragment {

    Context context;
    View view;
    RecyclerView recycler_view;
    VideoInfo videoInfo;
    private SensorManager mSensorManager;
    private MxVideoPlayer.MxAutoFullscreenListener mSensorEventListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_tab_fragment2, container, false);
        init();
        return view;
    }

    private void init() {
        recycler_view = view.findViewById(R.id.recycler_view);

        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.setAdapter(new FollowingAdapter(context, getDataList()));
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new MxVideoPlayer.MxAutoFullscreenListener();
    }

    private List<VideoInfo> getDataList() {
        List<VideoInfo> videoInfoList = new ArrayList<>();
        videoInfo = new VideoInfo();
        videoInfo.setUrl("http://112.253.22.163/4/p/p/q/v/ppqvlatwcebccqgrthiutjkityurza/hc.yinyuetai.com/59EC014EDDFE31808075899973863AAD.flv");
        videoInfoList.add(videoInfo);
        return videoInfoList;
    }
//
//    @Override
//    public void onBackPressed() {
//        if (MxVideoPlayer.backPress()) {
//            return;
//        }
//        super.onBackPressed();
//    }

    @Override
    public void onResume() {
        super.onResume();
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
        MxVideoPlayer.releaseAllVideos();
    }
}
