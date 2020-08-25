package com.gtp.androidplayer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gtp.androidplayer.adapter.ViewPagerAdapter;
import com.gtp.androidplayer.fragment.FileFragment;
import com.gtp.androidplayer.fragment.NetFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btVideo;
    private Button btSurface;
    private Button btTexture;
    private Button btMusic;
    private TextView tvTitle;
    private ViewPager viewPager;
    private TabLayout tableLayout;
    private String[] attr = new String[]{"本地视频", "网络视频"};
    private List<Fragment> fragments;
    private FileFragment fileFragment;
    private NetFragment netFragment;
    private ViewPagerAdapter adapter;

    public static int flag = 0;
    public static final int VIDEOVIEW_FLAG = 0;
    public static final int SURFACEVIEW_FLAG = 1;
    public static final int TEXTUREVIEW_FLAG = 2;
    public static final int MUSIC_FLAG = 3;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissionREAD_EXTERNAL_STORAGE(this);//动态权限申请
        initView();//获取组件
        initData();//初始化界面
    }

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // showDialog("External storage", context, Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    private void initView() {
        btVideo = (Button) findViewById(R.id.main_video);
        btVideo.setOnClickListener(this);
        btSurface = (Button) findViewById(R.id.main_surface);
        btSurface.setOnClickListener(this);
        btTexture = (Button) findViewById(R.id.main_texture);
        btTexture.setOnClickListener(this);
        btMusic = (Button) findViewById(R.id.main_music);
        btMusic.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.main_title);
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        tableLayout = (TabLayout) findViewById(R.id.main_tab);

    }

    private void initData() {
        fragments = new ArrayList<>();
        fileFragment = new FileFragment();
        netFragment = new NetFragment();
        fragments.add(fileFragment);
        fragments.add(netFragment);
        tableLayout.addTab(tableLayout.newTab().setText(attr[0]));
        tableLayout.addTab(tableLayout.newTab().setText(attr[1]));
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), this, attr, fragments);
        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_video:
                Toast.makeText(this, "切换到VideoVIew", Toast.LENGTH_SHORT).show();
                flag = VIDEOVIEW_FLAG;
                break;
            case R.id.main_surface:
                Toast.makeText(this, "切换到SurfaceView", Toast.LENGTH_SHORT).show();
                flag = SURFACEVIEW_FLAG;
                break;

        }
    }
}
