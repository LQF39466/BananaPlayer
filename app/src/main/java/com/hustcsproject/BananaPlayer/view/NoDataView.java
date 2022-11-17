package com.hustcsproject.BananaPlayer.view;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hustcsproject.BananaPlayer.R;


/**
 * <p>NoDataView 没有数据页面</p>
 *
 */
public class NoDataView extends RelativeLayout {

    private final RelativeLayout mRlNoDataRoot;
    private final ImageView mImgNoDataView;

    public NoDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_no_data,this);
        mRlNoDataRoot = findViewById(R.id.rl_no_data_root);
        mImgNoDataView = findViewById(R.id.img_no_data);
    }

    public void setNoDataView(@DrawableRes int imgResId){
        mImgNoDataView.setImageResource(imgResId);
    }
}