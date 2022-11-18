package com.hustcsproject.BananaPlayer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NowPlaying extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        Intent intent = getIntent();
        TextView musicNameTextView = findViewById(R.id.musicNameTextView);
        TextView musicianNameTextView = findViewById(R.id.musicianNameTextView);
        TextView musicLocalFilePathTextView = findViewById(R.id.musicDurationTextView);
        TextView musicDurationTextView = findViewById(R.id.musicLocalFilePathTextView);
        TextView musicSizeTextView = findViewById(R.id.musicSizeTextView);

        String str="<unknown>";
        if(intent.getStringExtra("songName")!=null) {
            musicNameTextView.setText("歌曲名："+intent.getStringExtra("songName"));
        }else{
            musicNameTextView.setText("歌曲名："+str);
        }
        if(intent.getStringExtra("artist")!=null){
            musicianNameTextView.setText("艺人名："+intent.getStringExtra("artist"));
        }else{
            musicianNameTextView.setText("艺人名："+str);
        }
        if(intent.getStringExtra("path")!=null){
            musicLocalFilePathTextView.setText("文件位置："+intent.getStringExtra("path"));
        }else{
            musicLocalFilePathTextView.setText("文件位置："+str);
        }
        if(intent.getStringExtra("duration")!=null){
            musicDurationTextView.setText("歌曲时长："+intent.getStringExtra("duration"));
        }else{
            musicDurationTextView.setText("歌曲时长："+str);
        }
        if(intent.getStringExtra("size")!=null)
        {
            musicSizeTextView.setText("歌曲大小："+intent.getStringExtra("size"));
        }else{
            musicSizeTextView.setText("歌曲大小："+str);
        }


    }

}