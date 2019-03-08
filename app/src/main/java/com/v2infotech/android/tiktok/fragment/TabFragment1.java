package com.v2infotech.android.tiktok.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.v2infotech.android.tiktok.R;
import com.v2infotech.android.tiktok.Utils.DividerItemDecoration;
import com.v2infotech.android.tiktok.adapter.FollowingAdapter;
import com.v2infotech.android.tiktok.adapter.VideoRecyclerViewAdapter;
import com.v2infotech.android.tiktok.model.VideoInfo;
import com.v2infotech.android.tiktok.ui.ExoPlayerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.LinearLayout.VERTICAL;

public class TabFragment1 extends Fragment {

    Context context;
    View view;
    RecyclerView recycler_view;
    VideoInfo videoInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_tab_fragment1, container, false);
        init();
        return view;
    }

    private void init() {
        recycler_view = view.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.setAdapter(new FollowingAdapter(context, getDataList()));
    }

    private List<VideoInfo> getDataList() {
        List<VideoInfo> videoInfoList = new ArrayList<>();
        videoInfo = new VideoInfo();
        videoInfo.setUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        videoInfoList.add(videoInfo);
        videoInfo = new VideoInfo();
        videoInfo.setUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4");
        videoInfoList.add(videoInfo);
        videoInfo = new VideoInfo();
        videoInfo.setUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4");
        videoInfoList.add(videoInfo);
        videoInfo = new VideoInfo();
        videoInfo.setUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        videoInfoList.add(videoInfo);
        videoInfo = new VideoInfo();
        videoInfo.setUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        videoInfoList.add(videoInfo);
        videoInfo = new VideoInfo();
        videoInfo.setUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        videoInfoList.add(videoInfo);
        return videoInfoList;
    }
}

