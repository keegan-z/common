package com.keegan.common;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.webkit.WebView;

import androidx.multidex.MultiDex;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.keegan.common.util.AppMonitor;
import com.keegan.common.util.ContextUtil;
import com.kongzue.dialog.util.BaseDialog;

import me.jessyan.autosize.AutoSizeConfig;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ContextUtil.init(this);
        XLog.init(BuildConfig.DEBUG ? LogLevel.ALL : LogLevel.NONE);
        AutoSizeConfig.getInstance().setCustomFragment(true);
        initWebViewDataDirectory(this);//适配androidP以上版本webview多进程的问题

        AppMonitor.get().initialize(this);


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onTerminate() {
        //空祖家dialog 清楚句柄（防止内存泄漏）
        BaseDialog.unload();
        super.onTerminate();
    }

    /**
     * 为webView设置目录后缀
     *
     * @param context
     */
    public void initWebViewDataDirectory(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName(context);
            if (!context.getPackageName().equals(processName)) {//判断是否是默认进程名称
                WebView.setDataDirectorySuffix(processName);
            }
        }
    }

    /**
     * 得到进程名称
     *
     * @param context
     * @return
     */
    public String getProcessName(Context context) {
        try {
            if (context == null)
                return null;
            ActivityManager manager = (ActivityManager)
                    context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo processInfo :
                    manager.getRunningAppProcesses()) {
                if (processInfo.pid == android.os.Process.myPid()) {
                    return processInfo.processName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
