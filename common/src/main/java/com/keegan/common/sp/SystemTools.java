package com.keegan.common.sp;


import android.content.Context;

import java.util.Map;


public class SystemTools {

    //spKey
    public static final String CHANNEL = "channel";//渠道号
    public static final String PAGE_CONFIG = "pageConfig";//首页设置
    public static final String PARITIES_QUOTATION_COLOR = "PARITIES_QUOTATION_COLOR";//行情显示色
    public static final String PARITIES_SCALE = "paritiesScale";//保留小数位
    public static final String LOAD_DEFAULT_PARITIES_FLAG = "load_default_parities_flag";//是否第一次加載外汇列表
    public static final String CURRENT_PARITIES= "current_parities";//保存上次选中外汇

    public static final String TEL_NUM = "telNum";//手机号
    public static final String PASSWORD = "password";//密码
    public static final String ADVERT = "advert";//广告页

    public static final String LUANCHFLAG = "luanchFlag";//是否第一次加載app


    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "hl365_sys";

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




    public static void put(Context context, String key, Object object){
        getInstance().put(context,key,object);
    }

    public static Object get(Context context, String key, Object defaultObject) {

        return  getInstance().get(context,key,defaultObject);
    }


    public static void remove(Context context, String key) {
        getInstance().remove(context,key);
    }

    public  static void clear(Context context) {
        getInstance().clear(context);
    }

    public static boolean contains(Context context, String key) {
        return  getInstance().contains(context,key);
    }

    public static Map<String, ?> getAll(Context context) {
        return  getInstance().getAll(context);
    }
}
