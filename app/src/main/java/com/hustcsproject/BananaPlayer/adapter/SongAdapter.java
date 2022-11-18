package com.hustcsproject.BananaPlayer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hustcsproject.BananaPlayer.R;
import com.hustcsproject.BananaPlayer.base.BaseAdapter;
import com.hustcsproject.BananaPlayer.model.SongModel;

/**
 * Describe:
 * <p>歌曲适配器</p>
 *
 */
public class SongAdapter extends BaseAdapter<SongModel, SongAdapter.SongViewHolder> {
    public SongAdapter(Context context) {
        super(context);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.item_songs_list;
    }

    @Override
    protected SongViewHolder onCreateHolder(View view) {
        return new SongViewHolder(view);
    }

    @Override
    protected void onBindData(SongViewHolder holder, SongModel songModel, int positon) {
        holder.tvSongName.setText(songModel.getName());
        holder.ivSongImage.setTag(songModel.getName());
        if (TextUtils.equals((String) holder.ivSongImage.getTag(), songModel.getName()) && songModel.getPlaying()) {
            holder.ivSongImage.setImageResource(R.drawable.ic_baseline_headset_24);
        } else {
            holder.ivSongImage.setImageResource(R.drawable.ic_baseline_music_note_24);
        }
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {

        ImageView ivSongImage;
        TextView tvSongName;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSongImage = itemView.findViewById(R.id.ivSongImage);
            tvSongName = itemView.findViewById(R.id.tvSongName);
        }
    }
}
