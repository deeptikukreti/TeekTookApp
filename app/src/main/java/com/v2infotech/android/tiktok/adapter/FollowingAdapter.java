package com.v2infotech.android.tiktok.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.xvideoplayer.MxVideoPlayer;
import com.example.xvideoplayer.MxVideoPlayerWidget;
import com.v2infotech.android.tiktok.R;
import com.v2infotech.android.tiktok.Utils.CircularImageView;
import com.v2infotech.android.tiktok.model.VideoInfo;
import com.v2infotech.android.tiktok.ui.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.v2infotech.android.tiktok.adapter.VideoRecyclerViewAdapter.VIEW_TYPE_EMPTY;
import static com.v2infotech.android.tiktok.adapter.VideoRecyclerViewAdapter.VIEW_TYPE_NORMAL;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.MyClass> {

    private Context context;
    private List<VideoInfo> resultList;

    public FollowingAdapter(Context context, List<VideoInfo> resultList) {
        this.context = context;
        this.resultList = resultList;
    }


    @NonNull
    @Override
    public FollowingAdapter.MyClass onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_following, viewGroup, false);
        MyClass myClass = new MyClass(v);
        return myClass;
    }

    @Override
    public void onBindViewHolder(@NonNull final FollowingAdapter.MyClass myClass, final int i) {
        myClass.videoPlayerWidget.startPlay(resultList.get(i).getUrl(), MxVideoPlayer.SCREEN_LAYOUT_LIST);
        RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(5000);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setInterpolator(new LinearInterpolator());
//        rotate.setFillAfter(true);
        myClass.rotate_profile.setAnimation(rotate);
    }

    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class MyClass extends RecyclerView.ViewHolder {

        CircularImageView rotate_profile;
        MxVideoPlayerWidget videoPlayerWidget;

        public MyClass(View itemView) {
            super(itemView);
            rotate_profile = itemView.findViewById(R.id.rotate_profile);
            videoPlayerWidget = (MxVideoPlayerWidget) itemView.findViewById(R.id.list_video_player);
        }
    }
}