package com.keegan.common.permissionutil;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;


public class PermissionUtil {


    public static boolean hasPermission(@NonNull Context context, @NonNull String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String permission : permissions) {
            boolean hasPermission = (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                return false;
            }
        }
        return true;
    }

    public static void requestPermissions(Object o, int requestCode, String... permissions) {
        if (o instanceof Activity) {
            ActivityCompat.requestPermissions(((Activity) o), permissions, requestCode);
        } else if (o instanceof Fragment) {
            ((Fragment) o).requestPermissions(permissions, requestCode);
        }
    }

    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[]
            grantResults, @NonNull PermissionListener listener) {
        List<String> grantedList = new ArrayList<>();
        List<String> deniedList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                grantedList.add(permissions[i]);
            } else {
                deniedList.add(permissions[i]);
            }
        }

        if (deniedList.isEmpty()) {
            listener.onSucceed(requestCode, grantedList);
        } else {
            listener.onFailed(requestCode, deniedList);
        }
    }


    public static boolean hasAlwaysDeniedPermission(@NonNull Activity activity, @NonNull List<String> deniedPermissions) {
        for (String deniedPermission : deniedPermissions) {
            if (shouldShowRationalePermissions(activity, deniedPermission)) {
                return true;
            }
        }
        return false;
    }

    public static boolean shouldShowRationalePermissions(Object o, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }

        boolean rationale = false;
        for (String permission : permissions) {
            if (o instanceof Activity) {
                rationale = ActivityCompat.shouldShowRequestPermissionRationale((Activity) o, permission);
            } else if (o instanceof Fragment) {
                rationale = ((Fragment) o).shouldShowRequestPermissionRationale(permission);
            } else if (o instanceof android.app.Fragment) {
                rationale = ((android.app.Fragment) o).shouldShowRequestPermissionRationale(permission);
            }
            if (rationale) {
                return true;
            }
        }
        return false;
    }
}
