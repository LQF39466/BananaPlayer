package com.hustcsproject.BananaPlayer.utils;

import com.hustcsproject.BananaPlayer.MyApp;
import com.hustcsproject.BananaPlayer.model.SongModel;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.annotations.NonNull;

/**
 * 当前工具类的内容在使用的过程中直接可删除，以及调用的地方都直接删除
 *
 */
public class UmEventUtils {

    /**
     * 上一曲
     */
    public static void lastMusic() {
        MobclickAgent.onEvent(MyApp.getInstance().getApplicationContext(), "click", "last_music");
    }

    /**
     * 下一曲
     */
    public static void nextMusic() {
        Map<String, Object> params = new HashMap<>();
        MobclickAgent.onEvent(MyApp.getInstance().getApplicationContext(), "click", "next_music");
    }

    /**
     * 停止
     */
    public static void stopMusic() {
        MobclickAgent.onEvent(MyApp.getInstance().getApplicationContext(), "click", "stop_music");
    }

    /**
     * 暂停
     */
    public static void pauseMusic() {
        MobclickAgent.onEvent(MyApp.getInstance().getApplicationContext(), "click", "pause_music");
    }

    /**
     * 播放
     */
    public static void playMusic() {
        MobclickAgent.onEvent(MyApp.getInstance().getApplicationContext(), "click", "play_music");
    }

    /**
     * 自动播放下一首
     */
    public static void autoPlayNextMusic() {
        MobclickAgent.onEvent(MyApp.getInstance().getApplicationContext(), "auto", "auto_play_next_music");
    }

    /**
     * 刷新音乐
     */
    public static void refreshMusic() {
        MobclickAgent.onEvent(MyApp.getInstance().getApplicationContext(), "click", "refresh_music");
    }

    /**
     * 播放音乐信息
     */
    public static void playMusicInfo(@NonNull SongModel songModel) {
        Map<String, Object> params = new HashMap<>(6);
        params.put("name", songModel.getName() == null ? "" : songModel.getName());
        params.put("imagePath", songModel.getImagePath() == null ? "" : songModel.getImagePath());
        params.put("singer", songModel.getSinger() == null ? "" : songModel.getSinger());
        params.put("path", songModel.getPath() == null ? "" : songModel.getPath());
        params.put("duration", songModel.getDuration());
        params.put("size", songModel.getSize());
        MobclickAgent.onEventObject(MyApp.getInstance().getApplicationContext(), "play_music_info", params);
    }

    /**
     * 列表播放音乐
     */
    public static void listPlayMusic(@NonNull SongModel songModel) {
        Map<String, Object> params = new HashMap<>(6);
        params.put("name", songModel.getName() == null ? "" : songModel.getName());
        params.put("imagePath", songModel.getImagePath() == null ? "" : songModel.getImagePath());
        params.put("singer", songModel.getSinger() == null ? "" : songModel.getSinger());
        params.put("path", songModel.getPath() == null ? "" : songModel.getPath());
        params.put("duration", songModel.getDuration());
        params.put("size", songModel.getSize());
        MobclickAgent.onEventObject(MyApp.getInstance().getApplicationContext(), "list_play_music", params);
    }

}
