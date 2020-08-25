package com.gtp.androidplayer.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.gtp.androidplayer.MainActivity;
import com.gtp.androidplayer.R;
import com.gtp.androidplayer.adapter.VideoInfoAdapter;
import com.gtp.androidplayer.been.VideoInfo;
import com.gtp.androidplayer.surfaceview.SurfaceActivity;
import com.gtp.androidplayer.utils.CommTools;
import com.gtp.androidplayer.videoview.VideoViewActivity;

import java.util.ArrayList;
import java.util.List;


public class FileFragment extends Fragment {

    private List<VideoInfo> mData;
    private VideoInfoAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_file, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.main_recycler);
    }

    private void initData() {
        mData = new ArrayList<>();
        String[] attr = new String[]{
                MediaStore.MediaColumns.DATA,
                BaseColumns._ID,
                MediaStore.MediaColumns.TITLE,
                MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.Video.VideoColumns.DURATION,
                MediaStore.MediaColumns.SIZE
        };
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, attr,
                null, null, null);
        Cursor cursor2 = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor != null) { //遍历本地视频文件
            while (cursor.moveToNext()) {
                VideoInfo info = new VideoInfo();
                info.setFilePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
                info.setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)));
                info.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE)));
                info.setTime(CommTools.LongToHms(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION))));
                info.setSize(CommTools.LongToPoint(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE))));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inDither = false;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                info.setB(MediaStore.Video.Thumbnails.getThumbnail(getActivity().getContentResolver(), id, MediaStore.Images.Thumbnails.MICRO_KIND, options));
                mData.add(info);
            }
        }
        if (cursor2 != null) {//便利本地音频文件
            while (cursor2.moveToNext()) {
                VideoInfo info = new VideoInfo();
                info.setFilePath(cursor2.getString(cursor2.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
                info.setMimeType(cursor2.getString(cursor2.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)));
                info.setTitle(cursor2.getString(cursor2.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE)));
                info.setTime(CommTools.LongToHms(cursor2.getInt(cursor2.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION))));
                info.setSize(CommTools.LongToPoint(cursor2.getLong(cursor2.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE))));
//                int id = cursor2.getInt(cursor2.getColumnIndexOrThrow(BaseColumns._ID));
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inDither = false;
//                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                info.setB(MediaStore.Video.Thumbnails.getThumbnail(getActivity().getContentResolver(), id,
//                        MediaStore.Images.Thumbnails.MICRO_KIND, options));

                mData.add(info);
            }
        }

        adapter = new VideoInfoAdapter(getActivity(), mData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setOnItemClickListener(new VideoInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(VideoInfo videoInfo, int position) {
                Intent intent = new Intent();
                intent.putExtra("VIDEO_INFO", videoInfo);
                intent.putExtra("VIDEO_SORT", position + "/" + mData.size());
                intent.putExtra("VIDEO_TYPE", 0);
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
