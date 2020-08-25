package com.gtp.androidplayer.surfaceview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gtp.androidplayer.R;
import com.gtp.androidplayer.been.VideoInfo;
import com.gtp.androidplayer.custom.MysurfaceView;
import com.gtp.androidplayer.utils.CommTools;


public class SurfaceActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;
    private VideoInfo videoInfo;
    private ImageView imgPlay;
    private ImageView imgBack;
    private SeekBar seekBar;
    private TextView tvTotalTime;
    private TextView tvPlayTime;
    private TextView tvSort;
    private ImageView ivAll;
    private TextView iconText;
    private ImageView icon;
    private MysurfaceView mysurfaceView;
    private boolean isFull = true;
    private boolean isPlaying = true;
    private String sort;
    private HideControl hideControl;
    LinearLayout layoutBottom;
    LinearLayout layoutTop;
    LinearLayout layoutCenter;
    private TextView  speedBtn;
    private boolean isChangeSpeed= true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_surface);
        initView();
        initData();
        initEvent();
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.M) {//6.0设置倍数播放
            speedBtn.setVisibility(View.VISIBLE);
        }else{
            speedBtn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mysurfaceView.setFullScreen();
    }

    private void initView() {
        imgBack = (ImageView) findViewById(R.id.test_sur_iv_back);
        imgBack.setOnClickListener(this);
        imgPlay = (ImageView) findViewById(R.id.test_sur_iv_play);
        imgPlay.setOnClickListener(this);
        ivAll = (ImageView) findViewById(R.id.test_sur_iv_full);
        ivAll.setOnClickListener(this);
        seekBar = (SeekBar) findViewById(R.id.test_sur_seekbar);
        tvTotalTime = (TextView) findViewById(R.id.test_sur_tv_total_time);
        tvPlayTime = (TextView) findViewById(R.id.test_sur_tv_start_time);
        tvSort = (TextView) findViewById(R.id.test_sur_tv_sort);
        mysurfaceView = (MysurfaceView) findViewById(R.id.test_sur_view);
        layoutBottom = (LinearLayout) findViewById(R.id.test_sur_bottom_ll);
        layoutTop = (LinearLayout) findViewById(R.id.test_sur_top_ll);
        layoutCenter = (LinearLayout) findViewById(R.id.test_show_center_ll);
        icon = (ImageView) findViewById(R.id.test_sur_icon);
        iconText = (TextView) findViewById(R.id.test_sur_icontext);

        speedBtn=findViewById(R.id.test_sur_tv_speed);
        speedBtn.setOnClickListener(this);

    }

    private void initData() {
        intent = getIntent();
        videoInfo = intent.getParcelableExtra("VIDEO_INFO");
        sort = intent.getStringExtra("VIDEO_SORT");
        tvSort.setText(sort);

        mysurfaceView.setUrl(videoInfo.getFilePath());

        hideControl = new HideControl();
        hideControl.startHideTimer();
    }

    private void initEvent() {
        //mysurfaceView.setFullScreen();
        mysurfaceView.setOnVideoPlayingListener(new MysurfaceView.OnVideoPlayingListener() {
            @Override
            public void onVideoSizeChanged(int vWidth, int vHeight) {

            }

            @Override
            public void onPlaying(int duration, int percent) {
                Log.i("surface", "播放进度" + "总时长" + duration + " 当前播放进度" + percent);
                seekBar.setMax(duration);
                seekBar.setProgress(percent);
                tvPlayTime.setText(CommTools.LongToHms(percent));
            }

            @Override
            public void onStart() {
                Toast.makeText(SurfaceActivity.this, "开始播放", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPlayOver() {
                finish();
            }

            /**播放总时长*/
            @Override
            public void onVideoSize(int videoSize) {
                tvTotalTime.setText(CommTools.LongToHms(videoSize));
                seekBar.setMax(videoSize);
            }

            @Override
            public void onAwake(MotionEvent event) {


                switch (event.getAction()) {
                    /**
                     * 点击的开始位置
                     */
                    case MotionEvent.ACTION_DOWN:
                        //tvTouchShowStart.setText("起始位置：(" + event.getX() + "," + event.getY());
                        hideControl.startHideTimer();

                        //hideControl.startHideTimer();
                        break;
                    /**
                     * 触屏实时位置
                     */
                    case MotionEvent.ACTION_MOVE:
                        //tvTouchShow.setText("实时位置：(" + event.getX() + "," + event.getY());
                        break;
                    /**
                     * 离开屏幕的位置
                     */
                    case MotionEvent.ACTION_UP:
                        //tvTouchShow.setText("结束位置：(" + event.getX() + "," + event.getY());
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onSlideChangeProgress(float persent) {
                int newProgress = seekBar.getProgress();
                int slideSize = (int) persent;
                int changeSize = seekBar.getMax() / 50;
                layoutCenter.setVisibility(View.VISIBLE);
                if (slideSize > 150) {
                    newProgress += changeSize;
                    seekBar.setProgress(newProgress);
                    mysurfaceView.seekTo(newProgress);
                    tvPlayTime.setText("" + CommTools.LongToHms(newProgress));

                    icon.setImageResource(R.mipmap.forward);
                    iconText.setText("" + CommTools.LongToHms(newProgress));
                } else if (slideSize < -200) {
                    newProgress -= changeSize;
                    seekBar.setProgress(newProgress);
                    mysurfaceView.seekTo(newProgress);
                    tvPlayTime.setText("" + CommTools.LongToHms(newProgress));

                    icon.setImageResource(R.mipmap.rewind);
                    iconText.setText("" + CommTools.LongToHms(newProgress));
                }

            }

            @Override
            public void onLightChange(int persent) {
                layoutCenter.setVisibility(View.VISIBLE);
                icon.setImageResource(R.mipmap.light);
                iconText.setText("" + persent + "%");
            }

            @Override
            public void onSoundChange(int persent) {
                layoutCenter.setVisibility(View.VISIBLE);
                icon.setImageResource(R.mipmap.sound);
                iconText.setText("" + persent + "%");
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                /**当进度条停止点击修改的时候触发*/
                /**取得当前进度条的刻度*/
                if (b) {
                    mysurfaceView.seekTo(i);
                    tvPlayTime.setText("" + CommTools.LongToHms(i));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                /**当进度条停止拖动修改的时候触发*/
                /**取得当前进度条的刻度*/
//                int progress = seekBar.getProgress();
//                /**设置当前播放的位置*/
//                mysurfaceView.seekTo(progress);
//                tvPlayTime.setText(""+CommTools.LongToHms(progress));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mysurfaceView.setUrl(videoInfo.getFilePath());
        mysurfaceView.play();
    }

    @Override
    public void finish() {
        super.finish();
        mysurfaceView.finishVideo();
        hideControl.endHideTimer();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.test_sur_iv_back:
                finish();
                break;
            case R.id.test_sur_iv_play:
                mysurfaceView.pause();

                if (isPlaying) {
                    imgPlay.setImageResource(R.mipmap.video_pause);
                    isPlaying = false;
                } else {
                    imgPlay.setImageResource(R.mipmap.video_start);
                    isPlaying = true;
                }
                break;
            case R.id.test_sur_iv_full:
                isFull();
                break;
            case R.id.test_sur_bottom_ll:
                //hideControl.startHideTimer();
                //hideControl.resetHideTimer();
                break;
            case R.id.test_sur_tv_speed:
                  changePlaySpeed();
               // Toast.makeText(this, "2222", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void changePlaySpeed() {
        if(isChangeSpeed){
            mysurfaceView.setPlayerSpeed(2);
            Toast.makeText(this, "2倍速", Toast.LENGTH_SHORT).show();
            isChangeSpeed = false;
            speedBtn.setText("/2.0X");
        } else {
            mysurfaceView.setPlayerSpeed(1);
            Toast.makeText(this, "1倍速", Toast.LENGTH_SHORT).show();
            isChangeSpeed = true;
            speedBtn.setText("/1.0X");
        }
    }

    public void isFull() {
        if (isFull) {
            mysurfaceView.setHalfScreen();
            ivAll.setImageResource(R.mipmap.video_full);
            isFull = false;
        } else {
            mysurfaceView.setFullScreen();
            ivAll.setImageResource(R.mipmap.video_shrink);
            isFull = true;
        }
    }


    public class HideControl {
        public final static int MSG_HIDE = 0x01;
        public final static int MSG_SHOW = 0x02;
        private boolean isShowing = true;

        private HideHandler mHideHandler;

        public HideControl() {
            mHideHandler = new HideHandler();
        }

        public class HideHandler extends Handler {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_HIDE:
                        layoutBottom.setVisibility(View.INVISIBLE);
                        layoutTop.setVisibility(View.INVISIBLE);
                        layoutCenter.setVisibility(View.INVISIBLE);
                        Log.d("gtp", "INVISIBLE");
                        break;
                    case MSG_SHOW:
                        layoutBottom.setVisibility(View.VISIBLE);
                        layoutTop.setVisibility(View.VISIBLE);
                        hideControl.startHideTimer();
                        Log.d("gtp", "VISIBLE");
                        break;
                }

            }
        }

        private Runnable hideRunable = new Runnable() {

            @Override
            public void run() {
                if (isShowing) {
                    isShowing = false;
                    Log.d("gtp", "VISIBLE" + isShowing);
                    mHideHandler.obtainMessage(MSG_HIDE).sendToTarget();
                    Log.d("gtp", "MSG_HIDE" + MSG_HIDE);
                } else {
                    isShowing = true;
                    mHideHandler.obtainMessage(MSG_SHOW).sendToTarget();
                    Log.d("gtp", "MSG_SHOW" + MSG_SHOW);
                }
            }
        };

        public void startHideTimer() {//开始计时,三秒后执行runable
            mHideHandler.removeCallbacks(hideRunable);
            if (isShowing) {
                mHideHandler.postDelayed(hideRunable, 3500);
            } else {
                mHideHandler.postDelayed(hideRunable, 200);

            }
        }

        public void endHideTimer() {//移除runable,将不再计时
            mHideHandler.removeCallbacks(hideRunable);
        }

//        public void resetHideTimer() {//重置计时
//           mHideHandler.removeCallbacks(hideRunable);
//            startHideTimer();
//        }

    }

}
