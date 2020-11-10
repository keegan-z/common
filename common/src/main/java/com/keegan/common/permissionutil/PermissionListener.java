package com.keegan.common.permissionutil;

import java.util.List;

public interface PermissionListener {

    void onSucceed(int requestCode, List<String> grantedList);
    void onFailed(int requestCode, List<String> deniedList);
}
