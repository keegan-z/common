package com.keegan.commondemo.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.keegan.common.permissionutil.PermissionListener;
import com.keegan.common.permissionutil.PermissionUtil;
import com.keegan.common.util.AppManager;
import com.keegan.common.util.AppMonitor;
import com.kongzue.dialog.interfaces.OnBackClickListener;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.MessageDialog;
import com.kongzue.dialog.v3.WaitDialog;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.jessyan.autosize.internal.CustomAdapt;

public abstract class   BaseActivity extends AppCompatActivity implements CustomAdapt,IBaseView {

    public ViewModelProvider mViewModelProvider;

    protected abstract void initViewModel();

    protected abstract void initViews();

    protected abstract void initData();

    private PermissionListener permissionListener = null;

    private AppMonitor.Callback callback = new AppMonitor.Callback() {
        @Override
        public void onAppForeground() {

            //App切换到前台
            onAppForegroundCallBack();
        }

        @Override
        public void onAppBackground() {

            //App切换到后台

            onAppBackgroundCallBack();

        }

        @Override
        public void onAppUIDestroyed() {

            //App被杀死了

        }
    };

    public void onAppForegroundCallBack() {

    }

    public void onAppBackgroundCallBack() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelProvider.Factory mViewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        mViewModelProvider = new ViewModelProvider(this,mViewModelFactory);
        initViewModel();

        AppManager.getAppManager().addActivity(this);

        //初始化沉浸式
        //mmersionBarUtils.initImmersionBar(this);
        //注册回调
        AppMonitor.get().register(callback);

        initViews();
        initData();


        dialogConfig();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        AppManager.getAppManager().finishActivity(this);

        //注销回调
        AppMonitor.get().unRegister(callback);
    }

    /**
     * 在需要检测权限处调用该方法，同时实现回调接口即可
     */
    public void checkPermission(int requestCode, @NonNull PermissionListener permissionListener, @NonNull String... permissions) {
        this.permissionListener = permissionListener;
        if (PermissionUtil.hasPermission(this, permissions)) {
            List<String> grantedList = new ArrayList<>();
            Collections.addAll(grantedList, permissions);
            permissionListener.onSucceed(requestCode, grantedList);
        } else {
            PermissionUtil.requestPermissions(this, requestCode, permissions);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionListener != null) {
            PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, permissionListener);
        }
    }


    @Override
    public void launchActivity(Class<? extends Activity> pClass, Bundle bundle, int resCode) {
        Intent jumpIntent = new Intent();
        if (bundle != null) {
            jumpIntent.putExtra("bundle", bundle);
        }
        jumpIntent.setClass(this, pClass);
        if (resCode == -1) {
            startActivity(jumpIntent);
        } else {
            startActivityForResult(jumpIntent, resCode);
        }
    }



    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 667;
    }


    private void dialogConfig() {
        DialogSettings.init();
        DialogSettings.modalDialog = true;
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        DialogSettings.theme = DialogSettings.THEME.LIGHT; //全局对话框明暗风格，提供两种可选主题，LIGHT, DARK
        DialogSettings.tipTheme = DialogSettings.THEME.DARK;
        //DialogSettings.checkRenderscriptSupport(this);
        DialogSettings.DEBUGMODE = false;
        DialogSettings.isUseBlur = false;
        DialogSettings.autoShowInputKeyboard = true;
        DialogSettings.titleTextInfo = new TextInfo().setFontSize(14).setBold(true).setFontColor(Color.parseColor("#333333"));
        DialogSettings.contentTextInfo = new TextInfo().setFontSize(13).setBold(false).setFontColor(Color.parseColor("#333333"));
        DialogSettings.buttonTextInfo = new TextInfo().setFontSize(15).setBold(false).setFontColor(Color.parseColor("#4795ED"));
        DialogSettings.menuTextInfo = new TextInfo().setFontSize(15).setBold(false).setFontColor(Color.parseColor("#4795ED"));

        //DialogSettings.backgroundColor = Color.BLUE;
        //DialogSettings.titleTextInfo = new TextInfo().setFontSize(50);
        //DialogSettings.buttonPositiveTextInfo = new TextInfo().setFontColor(Color.GREEN);
    }


    @Override
    public void showLoading(String message) {

        WaitDialog.show(this, message).setOnBackClickListener(new OnBackClickListener() {
            @Override
            public boolean onBackClick() {
                WaitDialog.dismiss();
                return false;
            }
        });


    }

    @Override
    public void hideLoading() {
        WaitDialog.dismiss(500);
    }

    @Override
    public void showMessage(String title, String message, String okText, String cancleText) {
        MessageDialog.show(this, title, message, okText, cancleText)

                .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        onMessageCallBack();
                        return false;
                    }
                });
    }


    public void onMessageCallBack() {

    }

    @Override
    public void showBottomMenu(String[] texts) {

        BottomMenu.show(this, texts, new OnMenuItemClickListener() {
            @Override
            public void onClick(String text, int index) {
                onBottomMenuSelect(text, index);
            }
        })
                .setCancelButtonTextInfo(new TextInfo().setFontSize(15).setBold(false).setFontColor(Color.parseColor("#999999")));
    }

    @Override
    public void showBottomMenu(String title, String[] texts) {

        BottomMenu.show(this, texts, new OnMenuItemClickListener() {
            @Override
            public void onClick(String text, int index) {
                onBottomMenuSelect(text, index);
            }
        }).setTitle(title)
                .setCancelButtonTextInfo(new TextInfo().setFontSize(15).setBold(false).setFontColor(Color.parseColor("#999999")));
        ;
    }

    public void onBottomMenuSelect(String text, int index) {

    }


}

