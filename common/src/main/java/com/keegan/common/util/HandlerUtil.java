package com.keegan.common.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HandlerUtil {
    private static Handler mainHandler;

    static {
        mainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 获取主线程 Handler
     *
     * @return mainHandler
     */
    public static Handler getMainHandler() {
        if (mainHandler == null) {
            mainHandler = new Handler(Looper.getMainLooper());
        }
        return mainHandler;
    }

    /**
     * 获取主线程 Handler
     *
     * @param callback The callback interface in which to handle messages, or null.
     * @return The main thread handler
     */
    public static Handler getMainHandlerWithCallback(@Nullable Handler.Callback callback) {
        return new Handler(Looper.getMainLooper(), callback);
    }

    /**
     * 创建与 HandlerThread 关联的异步 Handler
     *
     * @param tag HandlerThread 标识
     * @return The asyn handler
     */
    public static Handler createAsynHandler(@NonNull String tag) {
        HandlerThread ht = new HandlerThread(tag);
        ht.start();
        return new Handler(ht.getLooper());
    }

    /**
     * 创建与 HandlerThread 关联的异步 Handler
     *
     * @param tag      HandlerThread 标识
     * @param priority 线程优先级
     * @return The asyn handler
     */
    public static Handler createAsynHandler(@NonNull String tag, int priority) {
        HandlerThread ht = new HandlerThread(tag, priority);
        ht.start();
        return new Handler(ht.getLooper());
    }

    /**
     * 创建与 HandlerThread 关联的异步 Handler
     *
     * @param tag      HandlerThread 标识
     * @param callback Handler inner interface
     * @return The asyn handler
     */
    public static Handler createAsynHandler(@NonNull String tag, @Nullable Handler.Callback callback) {
        HandlerThread ht = new HandlerThread(tag);
        ht.start();
        return new Handler(ht.getLooper(), callback);
    }

    /**
     * 创建与 HandlerThread 关联的异步 Handler
     *
     * @param tag      HandlerThread 标识
     * @param priority 线程优先级
     * @param callback Handler inner interface
     * @return The asyn handler
     */
    public static Handler createAsynHandler(@NonNull String tag, int priority, @Nullable Handler.Callback callback) {
        HandlerThread ht = new HandlerThread(tag, priority);
        ht.start();
        return new Handler(ht.getLooper(), callback);
    }


}
