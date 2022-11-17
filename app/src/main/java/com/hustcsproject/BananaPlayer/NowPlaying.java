package com.hustcsproject.BananaPlayer;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class NowPlaying extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        Intent intent = getIntent();
        TextView musicNameTextView = findViewById(R.id.musicNameTextView);
        TextView musicianNameTextView = findViewById(R.id.musicianNameTextView);
        musicNameTextView.setText(intent.getStringExtra("songName"));
        musicianNameTextView.setText(intent.getStringExtra("artist"));
    }

    public void onClickStop() {
        super.onClick(findViewById(R.id.playStopButton));
    }

}