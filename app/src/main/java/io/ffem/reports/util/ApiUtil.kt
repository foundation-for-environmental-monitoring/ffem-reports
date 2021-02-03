package io.ffem.reports.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.lang.reflect.Field;

import io.ffem.reports.R;

/**
 * Utility functions for api related actions.
 */
public final class ApiUtil {

    private ApiUtil() {
    }

    /**
     * Gets the app version.
     *
     * @return The version name and number
     */
    public static String getAppVersion(Context context) {
        String version = "";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            version = String.format("%s %s", context.getString(R.string.version), packageInfo.versionName);

        } catch (PackageManager.NameNotFoundException ignored) {
            // do nothing
        }
        return version;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static int getThemeResourceId(String theme) {
        int resourceId = -1;
        try {
            Class res = R.style.class;
            Field field = res.getField("AppTheme_" + theme);
            resourceId = field.getInt(null);

        } catch (Exception ignored) {
        }

        return resourceId;
    }
}
