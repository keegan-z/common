package com.keegan.common.util;


import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.keegan.common.R;

public class ImmersionBarUtils {


    public static final int IMMERSIVE_ON_DARK_BAR = 0;
    public static final int IMMERSIVE_ON_WHITE_BAR = 1;


    public static void changeImmersiveFontStatus(AppCompatActivity activity, int type) {
        switch (type) {
            case IMMERSIVE_ON_DARK_BAR:
                ImmersionBar.with(activity).statusBarDarkFont(true).flymeOSStatusBarFontColor(R.color.colorText33).init();
                break;
            case IMMERSIVE_ON_WHITE_BAR:
                ImmersionBar.with(activity).statusBarDarkFont(false).flymeOSStatusBarFontColor(R.color.colorWhite).init();
                break;
        }
    }

    public static void initImmersionBar(AppCompatActivity activity) {
        //在BaseActivity里初始化
        ImmersionBar.with(activity)
                .statusBarDarkFont(true)
                .flymeOSStatusBarFontColor(R.color.colorText33)
                .init();
    }

}
