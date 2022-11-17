package com.hustcsproject.BananaPlayer.utils;

import android.widget.Toast;

import com.hustcsproject.BananaPlayer.MyApp;


/**
 * <p>吐司工具类</p>
 *
 */
public class ToastUtil {

    public static void showToast(String message) {
        Toast.makeText(MyApp.getInstance(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(int resid) {
        Toast.makeText(MyApp.getInstance(), MyApp.getInstance().getString(resid), Toast.LENGTH_SHORT)
                .show();
    }
}