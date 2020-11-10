package com.keegan.common.sp;

import android.content.Context;

import java.util.Map;

public class UserToos {

    //spKey
    public static final String ISLOGIN = "islogin";//是否已经登录

    public static final String USERNAME = "username";//用户名
    public static final String HEAD_URL = "headUrl";//头像
    public static final String NICKNAME = "nickname";//昵称
    public static final String TOKEN = "token";//token

    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "hl365_user";


    private volatile static SharePrefsHelper spHelper;


    private static SharePrefsHelper getInstance() {
        if (spHelper == null) {

            synchronized (SharePrefsHelper.class) {
                if (spHelper == null) {
                    spHelper = new SharePrefsHelper(FILE_NAME);
                }
            }
        }

        return spHelper;
    }



    public static void put(Context context, String key, Object object) {
        getInstance().put(context, key, object);
    }

    public static Object get(Context context, String key, Object defaultObject) {

        return getInstance().get(context, key, defaultObject);
    }


    public static void remove(Context context, String key) {
        getInstance().remove(context, key);
    }

    public static void clear(Context context) {
        getInstance().clear(context);
    }

    public static boolean contains(Context context, String key) {
        return getInstance().contains(context, key);
    }

    public static Map<String, ?> getAll(Context context) {
        return getInstance().getAll(context);
    }
}
