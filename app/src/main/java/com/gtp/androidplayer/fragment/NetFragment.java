package com.gtp.androidplayer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gtp.androidplayer.MainActivity;
import com.gtp.androidplayer.R;
import com.gtp.androidplayer.adapter.VideoAdapter;
import com.gtp.androidplayer.been.VideoInfo;
import com.gtp.androidplayer.surfaceview.SurfaceActivity;
import com.gtp.androidplayer.utils.Constant;
import com.gtp.androidplayer.videoview.VideoViewActivity;
import java.util.ArrayList;
import java.util.List;


public class NetFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<VideoInfo> mData;
    private VideoAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_net, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.fm_net_recycler);
    }

    private void initData() {
        mData = new ArrayList<>();
        mData = Constant.getVideoInfo();
        adapter = new VideoAdapter(getActivity(), mData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(VideoInfo video, int position) {
                Intent intent = new Intent();
                intent.putExtra("VIDEO_INFO", video);
                intent.putExtra("VIDEO_SORT", position + "/" + mData.size());
                intent.putExtra("VIDEO_TYPE", 1);
                switch (MainActivity.flag) {
                    case MainActivity.VIDEOVIEW_FLAG:
                        intent.setClass(getActivity(), VideoViewActivity.class);
                        startActivity(intent);
                        break;
                    case MainActivity.SURFACEVIEW_FLAG:
                        intent.setClass(getActivity(), SurfaceActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

}
