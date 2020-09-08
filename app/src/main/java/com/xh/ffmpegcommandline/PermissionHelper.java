package com.xh.ffmpegcommandline;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhang on 2019-12-03.
 */
public class PermissionHelper {

    /**
     * 检查没有的权限
     *
     * @param context
     * @param permissions 检查的权限
     * @return 没有（需要申请）的权限
     */
    public static List<String> checkWithoutPermissions(Context context, String... permissions) {
        if (ContainerUtil.isEmpty(permissions)) {
            return null;
        }
        List<String> withoutPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                withoutPermissions.add(permission);
            }
        }
        return withoutPermissions;
    }

    public static void requestPermissions(Activity activity, int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public static void requestPermissions(Fragment fragment, int requestCode, String... permissions) {
        fragment.requestPermissions(permissions, requestCode);
    }


}
