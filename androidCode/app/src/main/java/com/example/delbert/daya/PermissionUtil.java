package com.example.delbert.daya;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YuJin on 2016-08-12.
 */
public class PermissionUtil {
    public static final int PERMISSION_MIC = 101;
    public static final int PERMISSION_LOCATION = 102;
    public static final int PERMISSION_STORAGE = 103;
    public static final int PERMISSION_ADRRESS = 104;
    public static final int PERMISSION_CALL = 105;

    public static boolean checkAndRequestPermission(Activity activity, int permissionRequestCode, String... permissions) {
        Log.d("xxx", "checkAndRequestPermission1 PermissionUtil");
        String[] requiredPermissions = getRequiredPermissions(activity, permissions);

        if (requiredPermissions.length > 0 && !activity.isDestroyed()) {
            ActivityCompat.requestPermissions(activity, requiredPermissions, permissionRequestCode);
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkAndRequestPermission(Fragment fragment, int permissionRequestCode, String... permissions) {
        Log.d("xxx", "checkAndRequestPermission2 PermissionUtil");
        String[] requiredPermissions = getRequiredPermissions(fragment.getContext() != null ?
                fragment.getContext() : fragment.getActivity(), permissions);

        if (requiredPermissions.length > 0 && fragment.isAdded()) {
            fragment.requestPermissions(requiredPermissions, permissionRequestCode);
            return false;
        } else {
            return true;
        }
    }

    public static String[] getRequiredPermissions(Context context, String... permissions) {
        Log.d("xxx", "getRequiredPermission PermissionUtil");
        List<String> requiredPermissions = new ArrayList<>();

        // Context가 null이면 무조건 권한을 요청하도록 requiredPermissions가 존재한다고 reutrn 한다
        if (context == null) return requiredPermissions.toArray(new String[1]);

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                requiredPermissions.add(permission);
            }
        }

        return requiredPermissions.toArray(new String[requiredPermissions.size()]);
    }

    public static boolean verifyPermissions(int[] grantResults) {
        Log.d("xxx", "verifyPermission PermissionUtil");
        // At least one result must be checked.
        if (grantResults.length < 1) return false;

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) return false;
        }
        return true;
    }


}
