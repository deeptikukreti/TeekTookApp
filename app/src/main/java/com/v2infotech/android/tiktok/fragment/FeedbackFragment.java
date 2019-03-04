package com.v2infotech.android.tiktok.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.v2infotech.android.tiktok.R;
import com.v2infotech.android.tiktok.Utils.DividerItemDecoration;
import com.v2infotech.android.tiktok.adapter.VideoRecyclerViewAdapter;
import com.v2infotech.android.tiktok.model.VideoInfo;
import com.v2infotech.android.tiktok.ui.ExoPlayerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static android.widget.LinearLayout.VERTICAL;


public class FeedbackFragment extends Fragment {
    View view;
    //  @BindView(R.id.recyclerViewFeed)
    ExoPlayerRecyclerView recyclerViewFeed;
    private List<VideoInfo> videoInfoList = new ArrayList<>();
    private VideoRecyclerViewAdapter mAdapter;
    private boolean firstTime = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feedback, container, false);
        recyclerViewFeed = view.findViewById(R.id.recyclerViewFeed);
        ButterKnife.bind(getActivity());
        prepareVideoList();
        recyclerViewFeed.setVideoInfoList(videoInfoList);
        mAdapter = new VideoRecyclerViewAdapter(videoInfoList);
        recyclerViewFeed.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, false));
        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider_drawable);
        recyclerViewFeed.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        recyclerViewFeed.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFeed.setAdapter(mAdapter);

        if (firstTime) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    recyclerViewFeed.playVideo();
                }
            });
            firstTime = false;
        }
        recyclerViewFeed.scrollToPosition(0);
        return view;
    }

    private void prepareVideoList() {
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setId(1);
        videoInfo.setUserHandle("@h.pandya");
        videoInfo.setTitle("Do you think the concept of marriage will no longer exist in the future?");
        videoInfo.setCoverUrl("https://androidwave.com/media/images/exo-player-in-recyclerview-in-android-1.png");
        videoInfo.setUrl("https://androidwave.com/media/androidwave-video-1.mp4");

        VideoInfo videoInfo2 = new VideoInfo();
        videoInfo2.setId(2);
        videoInfo2.setUserHandle("@hardik.patel");
        videoInfo2.setTitle("If my future husband doesn't cook food as good as my mother should I scold him?");
        videoInfo2.setCoverUrl("https://androidwave.com/media/images/exo-player-in-recyclerview-in-android-2.png");
        videoInfo2.setUrl("https://androidwave.com/media/androidwave-video-2.mp4");

        VideoInfo videoInfo3 = new VideoInfo();
        videoInfo3.setId(3);
        videoInfo3.setUserHandle("@arun.gandhi");
        videoInfo3.setTitle("Give your opinion about the Ayodhya temple controversy.");
        videoInfo3.setCoverUrl("https://androidwave.com/media/images/exo-player-in-recyclerview-in-android-3.png");
        videoInfo3.setUrl("https://androidwave.com/media/androidwave-video-3.mp4");

        VideoInfo videoInfo4 = new VideoInfo();
        videoInfo4.setId(4);
        videoInfo4.setUserHandle("@sachin.patel");
        videoInfo4.setTitle("When did kama founders find sex offensive to Indian traditions");
        videoInfo4.setCoverUrl("https://androidwave.com/media/images/exo-player-in-recyclerview-in-android-4.png");
        videoInfo4.setUrl("https://androidwave.com/media/androidwave-video-6.mp4");

        VideoInfo videoInfo5 = new VideoInfo();
        videoInfo5.setId(5);
        videoInfo5.setUserHandle("@monika.sharma");
        videoInfo5.setTitle("When did you last cry in front of someone?");
        videoInfo5.setCoverUrl("https://androidwave.com/media/images/exo-player-in-recyclerview-in-android-5.png");
        videoInfo5.setUrl("https://androidwave.com/media/androidwave-video-5.mp4");

        videoInfoList.add(videoInfo);
        videoInfoList.add(videoInfo2);
        videoInfoList.add(videoInfo3);
        videoInfoList.add(videoInfo4);
        videoInfoList.add(videoInfo5);
        videoInfoList.add(videoInfo);
        videoInfoList.add(videoInfo2);
        videoInfoList.add(videoInfo3);
        videoInfoList.add(videoInfo4);
        videoInfoList.add(videoInfo5);

    }

//    @Override
//    protected void onPause() {
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                recyclerViewFeed.onPausePlayer();
//            }
//        });
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                recyclerViewFeed.onRestartPlayer();
//            }
//        });
//        super.onResume();
//    }

    @Override
    public void onDestroy() {
        if (recyclerViewFeed != null)
            recyclerViewFeed.onRelease();
        super.onDestroy();
    }
}
