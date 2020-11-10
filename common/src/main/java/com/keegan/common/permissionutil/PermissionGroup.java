package com.keegan.common.permissionutil;

import android.Manifest;

/**
 * 为了适配 8.0 权限问题，
 * 申请一个权限改为申请整个权限组，
 * 因为只会弹窗一次，
 * 所以这么做是没有什么问题的
 */
public class PermissionGroup {

    /**
     * Storage权限
     */
    public static String[] ABS_STORAGE = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Phone权限
     */
    public static String[] ABS_PHONE = new String[]{
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS
    };
}
