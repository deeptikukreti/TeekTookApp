package com.v2infotech.android.tiktok.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.v2infotech.android.tiktok.R;
import com.v2infotech.android.tiktok.camera.BaseCameraActivity;


public class VideoFragment extends Fragment {
View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_video, container, false);
//        Intent intent = new Intent(getActivity(),BaseCameraActivity.class);
//               startActivity(intent);
        return  view;
    }


}
