package com.hustcsproject.BananaPlayer;

import com.hustcsproject.BananaPlayer.adapter.SongAdapter;
import com.hustcsproject.BananaPlayer.base.BaseActivity;
import com.hustcsproject.BananaPlayer.model.SongModel;
import com.hustcsproject.BananaPlayer.utils.UmEventUtils;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NowPlaying extends BaseActivity {

    private SeekBar sbMusic;
    private TextView tvSongName;
    private TextView tvMusicianName;
    private ImageView ivAlbumCover;
    private ImageButton btnPrevious;
    private ImageButton btnPlayStop;
    private ImageButton btnNext;

    private SongAdapter mAdapter;
    private MusicPlayerHelper helper;

    /**
     * 歌曲数据源
     */
    private final List<SongModel> songsList = new ArrayList<>();

    /**
     * 当前播放歌曲游标位置
     */
    private int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
    }

    /**
     * 设置页面标题
     */
    @Override
    public String getTootBarTitle() {
        return "音乐播放器";
    }

    @Override
    public int getToolBarRightImg() {
        return R.drawable.ic_baseline_autorenew_24;
    }

    /**
     * 绑定布局文件
     */
    @Override
    public int onBindLayout() {
        return R.layout.activity_main;
    }

    /**
     * 初始化页面组件
     */
    @Override
    public void initView() {
        sbMusic = findViewById(R.id.musicSeekBar);
        tvSongName = findViewById(R.id.musicNameTextView);
        tvMusicianName = findViewById(R.id.musicianNameTextView);
        btnPrevious = findViewById(R.id.previousButton);
        btnPlayStop = findViewById(R.id.playStopButton);
        btnNext = findViewById(R.id.nextButton);
        ivAlbumCover = findViewById(R.id.albumCoverImageView);
    }

    /**
     * 设置监听
     */
    @Override
    public void initListener() {
        sbMusic.setOnClickListener(this::onClick);
        tvSongName.setOnClickListener(this::onClick);
        tvMusicianName.setOnClickListener(this::onClick);
        btnPrevious.setOnClickListener(this::onClick);
        btnPlayStop.setOnClickListener(this::onClick);
        btnNext.setOnClickListener(this::onClick);
        ivAlbumCover.setOnClickListener(this::onClick);
    }

    /**
     * 初始化数据局
     */
    @Override
    public void initData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 处理点击事件
     */
    private void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnLast) {
            // 上一曲
            last();
            UmEventUtils.lastMusic();
        } else if (id == R.id.btnStar) {
            // 播放/暂停
            play(songsList.get(mPosition), false);
        } else if (id == R.id.btnNext) {
            // 下一曲
            next();
            UmEventUtils.nextMusic();
        }
    }

    /**
     * 上一首
     */
    private void last() {
        mPosition--;
        //如果上一曲小于0则取最后一首
        if (mPosition < 0) {
            mPosition = songsList.size() - 1;
        }
        play(songsList.get(mPosition), true);
    }

    /**
     * 播放歌曲
     *
     * @param songModel    播放源
     * @param isRestPlayer true 切换歌曲 false 不切换
     */
    private void play(SongModel songModel, Boolean isRestPlayer) {
        if (!TextUtils.isEmpty(songModel.getPath())) {
            Log.e(TAG, String.format("当前状态：%s  是否切换歌曲：%s", helper.isPlaying(), isRestPlayer));
            // 当前若是播放，则进行暂停
            if (!isRestPlayer && helper.isPlaying()) {
                pause();
                UmEventUtils.pauseMusic();
            } else {
                //进行切换歌曲播放
                helper.playBySongModel(songModel, isRestPlayer);
                // 正在播放的列表进行更新哪一首歌曲正在播放 主要是为了更新列表里面的显示
                for (int i = 0; i < songsList.size(); i++) {
                    songsList.get(i).setPlaying(mPosition == i);
                    mAdapter.notifyItemChanged(i);
                }
                if (!isRestPlayer) {
                    UmEventUtils.playMusic();
                }
                UmEventUtils.playMusicInfo(songModel);
            }
        } else {
            showToast("当前的播放地址无效");
        }
    }

    /**
     * 下一首
     */
    private void next() {
        mPosition++;
        //如果下一曲大于歌曲数量则取第一首
        if (mPosition >= songsList.size()) {
            mPosition = 0;
        }
        play(songsList.get(mPosition), true);
    }

    /**
     * 暂停播放
     */
    private void pause() {
        helper.pause();
    }
}