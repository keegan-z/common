
package com.keegan.commondemo.net;


import androidx.annotation.StringRes;

import com.keegan.common.util.ContextUtil;


public class ErrorCode {
    public static final int SUCCESS = 1;

    public static final int RELOGIN = 401;
    public static final int REMOTE_LOGIN = 91011;


    public static String getErrorMessage(int errorCode) {
        return getErrorMessage(errorCode, "");
    }

    /**
     * get error message with error code
     *
     * @param errorCode error code
     * @return error message
     */
    public static String getErrorMessage(int errorCode, String errorMsg) {
        switch (errorCode) {
            case RELOGIN://重新登录
                break;
        }
        return errorMsg;
    }

    private static String getString(@StringRes int resId) {
        return ContextUtil.getContext().getString(resId);
    }
}
