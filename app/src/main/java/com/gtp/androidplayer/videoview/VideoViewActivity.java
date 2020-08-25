package com.gtp.androidplayer.videoview;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;
import com.gtp.androidplayer.R;
import com.gtp.androidplayer.been.VideoInfo;


public class VideoViewActivity extends AppCompatActivity {
    VideoView videoView;
    MediaController mediaController;
    private VideoInfo videoInfo;
    boolean isSHU = true;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_view);
        intent = getIntent();
        videoInfo = intent.getParcelableExtra("VIDEO_INFO");

        initVideoViewAndMediaController();
    }

    private void initVideoViewAndMediaController() {
        videoView = (VideoView) findViewById(R.id.videoview_view);
        //创建MediaController对象
        mediaController = new MediaController(this);
        videoView.setVideoPath(videoInfo.getFilePath());
        //VideoView与MediaController建立关联
        videoView.setMediaController(mediaController);
        //MediaController与VideoView建立关联
        mediaController.setMediaPlayer(videoView);
        //让VideoView获取焦点
        videoView.requestFocus();
        videoView.start();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int mHeight = metric.widthPixels;     // 屏幕宽度（像素）
        int mWidth = metric.heightPixels;   // 屏幕高度（像素）

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏状态是隐藏任务栏与状态栏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
            isSHU = false;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    (int) mHeight, (int) (mWidth));
            // videoView.setLayoutParams(params);

        } else {
            //竖屏时显示任务栏，清楚flags
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            isSHU = true;
            //我这里是横竖比例16：9，这个比例随便多少，但一般是16：9，或者4：3
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    (int) mHeight, (int) (mWidth * 9 / 16));
            // videoView.setLayoutParams(params);
        }
    }


}
