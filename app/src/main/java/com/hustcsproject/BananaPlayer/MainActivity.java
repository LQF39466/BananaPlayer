package com.hustcsproject.BananaPlayer;

import android.Manifest;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hustcsproject.BananaPlayer.adapter.SongAdapter;
import com.hustcsproject.BananaPlayer.base.BaseActivity;
import com.hustcsproject.BananaPlayer.model.SongModel;
import com.hustcsproject.BananaPlayer.utils.ScanMusicUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Describe:
 * <p>播放器的主页</p>
 *
 */
public class MainActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private SeekBar seekbar;
    private TextView tvSongName;
    private ImageButton btnPrev;
    private ImageButton btnStart;
    private ImageButton btnNext;
    private ImageButton btnPlayLogic;

    private SongAdapter mAdapter;
    private MusicPlayerHelper helper;

    private int PlayLogic;

    /**
     * 歌曲数据源
     */
    private final List<SongModel> songsList = new ArrayList<>();

    /**
     * 当前播放歌曲游标位置
     */
    private int mPosition = 0;

    /**
     * 设置页面标题
     */
    @Override
    public String getTootBarTitle() {
        return "Banana Player";
    }

    @Override
    public int getToolBarRightImg() {
        return R.drawable.ic_baseline_autorenew_24;
    }

    /**
     * 点击右上角刷新数据
     */
    @Override
    public View.OnClickListener getToolBarRightImgClick() {
        return v -> {
            startAnimation(v);
            initData();
        };
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
        mRecyclerView = findViewById(R.id.mRecyclerView);
        seekbar = findViewById(R.id.seekbar);
        tvSongName = findViewById(R.id.tvSongName);
        btnPrev = findViewById(R.id.prevButton);
        btnStart = findViewById(R.id.playButton);
        btnNext = findViewById(R.id.nexButton);
        btnPlayLogic = findViewById(R.id.playLogicButton);
        PlayLogic = 0;

        initPlayHelper();
        initRecycleView();
    }

    /**
     * 初始化音乐帮助类
     */
    private void initPlayHelper() {
        // Init 播放 Helper
        helper = new MusicPlayerHelper(seekbar, tvSongName);

        findViewById(R.id.tvSongName).setSelected(true);

        helper.setOnCompletionListener(mp -> {
            Log.e(TAG, "next()");
            //下一曲
            next();
        });
    }

    /**
     * 初始化列表
     */
    private void initRecycleView() {
        // Init Adapter
        mAdapter = new SongAdapter(mContext);
        //添加数据源
        mAdapter.addAll(songsList);
        // RecyclerView 增加适配器
        mRecyclerView.setAdapter(mAdapter);
        // RecyclerView 增加布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //增加渲染特效
        mRecyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_item_right_slipe));
        // 需要重新启动布局时调用此方法
        mRecyclerView.scheduleLayoutAnimation();
        // Adapter 增加 Item 监听
        mAdapter.setItemClickListener((object, position) -> {
            mPosition = position;
            //播放歌曲
            play((SongModel) object, true);
        });
    }

    /**
     * 设置监听
     */
    @Override
    public void initListener() {
        btnStart.setOnClickListener(this::onClick);
        btnPrev.setOnClickListener(this::onClick);
        btnNext.setOnClickListener(this::onClick);
    }

    public void startNowPlayingActivity(View view) {
        Intent intent = new Intent(this, NowPlaying.class);
        intent.putExtra("songName", songsList.get(mPosition).getName());
        intent.putExtra("artist", songsList.get(mPosition).getSinger());
        intent.putExtra("duration",songsList.get(mPosition).getDuration());
        intent.putExtra("size",songsList.get(mPosition).getSize());
        intent.putExtra("path",songsList.get(mPosition).getPath());
        intent.putExtra("imagePath", songsList.get(mPosition).getImagePath());
        startActivity(intent);
    }

    /**
     * 初始化数据局
     */
    @Override
    public void initData() {
        // 请求读写权限
        RxPermissions rxPermissions = new RxPermissions(this);
        Disposable subscribe = rxPermissions.request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        ).subscribe(aBoolean -> {
            if (!aBoolean) {
                showToast("缺少存储权限，将会导致部分功能无法使用");
            } else {
                refreshMusic();
            }
        });
        boolean disposed = subscribe.isDisposed();
        Log.d(TAG, "initData: " + disposed);
    }

    /**
     * 刷新音乐
     */
    private void refreshMusic() {
        // 刷新多媒体数据库
        MediaScannerConnection.scanFile(this,
                new String[]{Environment.getExternalStorageDirectory().getAbsolutePath()},
                new String[]{"audio/aac", "audio/amr", "audio/flac", "audio/mpeg", "audio/midi", "audio/ogg"},
                new MediaScannerConnection.MediaScannerConnectionClient() {
                    @Override
                    public void onMediaScannerConnected() {

                    }

                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        runOnUiThread(() -> {
                            showInitLoadView();
                            List<SongModel> musicData = ScanMusicUtils.getMusicData(mContext);
                            if (!musicData.isEmpty()) {
                                hideNoDataView();
                                if (!songsList.isEmpty()) {
                                    songsList.clear();
                                }
                                songsList.addAll(musicData);
                                mAdapter.refresh(songsList);
                            } else {
                                showNoDataView();
                            }
                            hideInitLoadView();
                        });
                    }
                });
    }


    /**
     * 处理点击事件
     */
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.prevButton) {
            // 上一曲
            last();
        } else if (id == R.id.playButton) {
            // 播放/暂停
            play(songsList.get(mPosition), false);
        } else if (id == R.id.nexButton) {
            // 下一曲
            next();
        }
    }

    /**
     * 修改播放逻辑
     */
    public void onClickPlayLogic(View view0) {
        PlayLogic = (PlayLogic + 1) % 3;
        switch (PlayLogic) {
            case 0: // 顺序播放
                btnPlayLogic.setImageResource(R.drawable.play_order);
                break;
            case 1: // 随机播放
                btnPlayLogic.setImageResource(R.drawable.play_random);
                break;
            case 2: // 单曲循环
                btnPlayLogic.setImageResource(R.drawable.play_one);
                break;
            default:
                break;
        }
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
                // btnStart.setText(R.string.btn_play); 切换按钮
                btnStart.setImageResource(R.drawable.three_play);
                pause();
            } else {
                //进行切换歌曲播放
                helper.playBySongModel(songModel, isRestPlayer);
                btnStart.setImageResource(R.drawable.three_pause);

                // 正在播放的列表进行更新哪一首歌曲正在播放 主要是为了更新列表里面的显示
                for (int i = 0; i < songsList.size(); i++) {
                    songsList.get(i).setPlaying(mPosition == i);
                    mAdapter.notifyItemChanged(i);
                }
            }
        } else {
            showToast("当前的播放地址无效");
        }
    }


    /**
     * 上一首
     */
    private void last() {
        switch (PlayLogic) {
            case 0:
                mPosition--;
                //如果上一曲小于0则取最后一首
                if (mPosition < 0) {
                    mPosition = songsList.size() - 1;
                }
                break;
            case 1:
                int newPosition = (int) (Math.random() * songsList.size());
                while (songsList.size() != 1 && newPosition == mPosition) {
                    newPosition = (int) (Math.random() * songsList.size());
                }
                mPosition = newPosition;
                break;
            default:
                break;
        }
        play(songsList.get(mPosition), true);
    }

    /**
     * 下一首
     */
    private void next() {
        switch (PlayLogic) {
            case 0:
                mPosition++;
                //如果下一曲大于歌曲数量则取第一首
                if (mPosition >= songsList.size()) {
                    mPosition = 0;
                }
                break;
            case 1:
                int newPosition = (int) (Math.random() * songsList.size());
                while (songsList.size() != 1 && newPosition == mPosition) {
                    newPosition = (int) (Math.random() * songsList.size());
                }
                mPosition = newPosition;
                break;
            default:
                break;
        }
        play(songsList.get(mPosition), true);
    }

    /**
     * 暂停播放
     */
    private void pause() {
        helper.pause();
    }

    /**
     * 开启动画360度旋转特效
     */
    private void startAnimation(View v) {
        Animation loadAnimation =
                AnimationUtils.loadAnimation(mContext, R.anim.ic_baseline_autorenew_24_rotate);
        // 设置速度器 LinearInterpolator是匀速加速器
        loadAnimation.setInterpolator(new LinearInterpolator());
        // 设置动画时长，以毫秒为单位
        loadAnimation.setDuration(1_000);
        // 参数为true时，动画播放完后，view会维持在最终的状态。而默认值是false，也就是动画播放完后，view会恢复原来的状态
        loadAnimation.setFillAfter(false);
        v.startAnimation(loadAnimation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.destroy();
    }
}
