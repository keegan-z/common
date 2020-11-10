package com.keegan.commondemo.base;

import android.app.Activity;
import android.os.Bundle;

public interface IBaseView {

    void launchActivity(Class<? extends Activity> pClass, Bundle bundle, int resCode);

    void showLoading(String message);

    void hideLoading();

    void showMessage(String title, String message, String okText, String cancleText);

    void showBottomMenu(String[] texts);

    /**
     * 底部菜单
     *
     * @param title
     * @param texts
     */
    void showBottomMenu(String title, String[] texts);


}
