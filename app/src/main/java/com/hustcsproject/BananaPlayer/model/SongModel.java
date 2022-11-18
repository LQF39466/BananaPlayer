package com.hustcsproject.BananaPlayer.model;

/**
 * 歌曲实体模型
 */
public class SongModel {

    /**
     * 歌曲名字
     */
    private String name;

    /**
     * 作家
     */
    private String singer;

    /**
     * 路径
     */
    private String path;

    /**
     * 时长
     */
    private int duration;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 是否正在播放
     */
    private Boolean isPlaying = false;

    public SongModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Boolean getPlaying() {
        return isPlaying;
    }

    public void setPlaying(Boolean playing) {
        isPlaying = playing;
    }
}
