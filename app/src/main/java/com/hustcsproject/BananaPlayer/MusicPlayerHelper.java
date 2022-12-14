package com.hustcsproject.BananaPlayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hustcsproject.BananaPlayer.model.SongModel;
import com.hustcsproject.BananaPlayer.utils.ScanMusicUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Describe:
 * <p>音乐播放器帮助类</p>
 * 可播放格式：AAC、AMR、FLAC、MP3、MIDI、OGG、PCM
 *
 */
public class MusicPlayerHelper implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        SeekBar.OnSeekBarChangeListener {
    public static String TAG = MusicPlayerHelper.class.getSimpleName();
    private static final int MSG_CODE = 0x01;
    private static final long MSG_TIME = 1_000L;

    private final MusicPlayerHelperHandler mHandler;
    /**
     * 播放器
     */
    private final MediaPlayer player;

    /**
     * 进度条
     */
    private final SeekBar seekBar;

    /**
     * 显示播放信息
     */
    private final TextView songName;
    private final TextView timer;

    /**
     * 当前的播放歌曲信息
     */
    private SongModel songModel;

    public MusicPlayerHelper(SeekBar seekBar, TextView songName, TextView timer) {
        mHandler = new MusicPlayerHelperHandler(this);
        player = new MediaPlayer();
        // 设置媒体流类型
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnBufferingUpdateListener(this);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);

        this.seekBar = seekBar;
        this.seekBar.setOnSeekBarChangeListener(this);
        this.songName = songName;
        this.timer = timer;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
        int currentProgress =
                seekBar.getMax() * player.getCurrentPosition() / player.getDuration();
        //Log.e(TAG, currentProgress + "% play --> " + percent + "% buffer");
    }

    /**
     * 当前 Song 播放完毕
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        //Log.e(TAG, "onCompletion");
        if (mOnCompletionListener != null) {
            mOnCompletionListener.onCompletion(mp);
        }
    }

    /**
     * 当前 Song 已经准备好
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        //Log.e(TAG, "onPrepared");
        mp.start();
    }


    /**
     * 播放
     *
     * @param songModel    播放源
     * @param isRestPlayer true 切换歌曲 false 不切换
     */
    public void playBySongModel(@NonNull SongModel songModel, @NonNull Boolean isRestPlayer) {
        this.songModel = songModel;
        //Log.e(TAG, "playBySongModel Url: " + songModel.getPath());
        if (isRestPlayer) {
            //重置多媒体
            player.reset();
            // 设置数据源
            if (!TextUtils.isEmpty(songModel.getPath())) {
                try {
                    player.setDataSource(songModel.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            player.prepareAsync();
        } else {
            player.start();
        }
        //发送更新命令
        mHandler.sendEmptyMessage(MSG_CODE);
    }

    /**
     * 暂停
     */
    public void pause() {
        //Log.e(TAG, "pause");
        if (player.isPlaying()) {
            player.pause();
        }
        mHandler.removeMessages(MSG_CODE);
    }

    /**
     * 是否正在播放
     */
    public Boolean isPlaying() {
        return player.isPlaying();
    }

    /**
     * 消亡 必须在 Activity 或者 Fragment onDestroy() 调用 以防止内存泄露
     */
    public void destroy() {
        // 释放掉播放器
        player.release();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 用于监听SeekBar进度值的改变
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    /**
     * 用于监听SeekBar开始拖动
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeMessages(MSG_CODE);
    }

    /**
     * 用于监听SeekBar停止拖动  SeekBar停止拖动后的事件
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        //Log.i(TAG, "onStopTrackingTouch " + progress);
        // 得到该首歌曲最长秒数
        int musicMax = player.getDuration();
        // SeekBar最大值
        int seekBarMax = seekBar.getMax();
        //计算相对当前播放器歌曲的应播放时间
        float msec = progress / (seekBarMax * 1.0F) * musicMax;
        // 跳到该曲该秒
        player.seekTo((int) msec);
        mHandler.sendEmptyMessageDelayed(MSG_CODE, MSG_TIME);
    }

    private String getCurrentPlayingInfo() {
        return String.format("%s\t\t",songModel.getName());
    }

    private String getCurrentPlayingInfo(int currentTime, int maxTime) {
        return String.format("%s / %s", ScanMusicUtils.formatTime(currentTime), ScanMusicUtils.formatTime(maxTime));
    }

    private OnCompletionListener mOnCompletionListener;


    /**
     * Register a callback to be invoked when the end of a media source
     * has been reached during playback.
     *
     * @param listener the callback that will be run
     */
    public void setOnCompletionListener(@NonNull OnCompletionListener listener) {
        this.mOnCompletionListener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when playback of
     * a media source has completed.
     */
    interface OnCompletionListener {
        /**
         * Called when the end of a media source is reached during playback.
         *
         * @param mp the MediaPlayer that reached the end of the file
         */
        void onCompletion(MediaPlayer mp);
    }

    static class MusicPlayerHelperHandler extends Handler {
        WeakReference<MusicPlayerHelper> weakReference;

        public MusicPlayerHelperHandler(MusicPlayerHelper helper) {
            super(Looper.getMainLooper());
            this.weakReference = new WeakReference<>(helper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_CODE) {
                int pos = 0;
                //如果播放且进度条未被按压
                if (weakReference.get().player.isPlaying() && !weakReference.get().seekBar.isPressed()) {
                    int position = weakReference.get().player.getCurrentPosition();
                    int duration = weakReference.get().player.getDuration();
                    if (duration > 0) {
                        // 计算进度（获取进度条最大刻度*当前音乐播放位置 / 当前音乐时长）
                        pos = (int) (weakReference.get().seekBar.getMax() * position / (duration * 1.0f));
                    }
                    weakReference.get().songName.setText(weakReference.get().getCurrentPlayingInfo());
                    weakReference.get().timer.setText(weakReference.get().getCurrentPlayingInfo(position, duration));
                }
                weakReference.get().seekBar.setProgress(pos);
                sendEmptyMessageDelayed(MSG_CODE, MSG_TIME);
            }
        }
    }
}
