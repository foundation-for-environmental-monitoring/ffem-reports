package io.ffem.reports.util

import android.content.Context
import android.content.pm.PackageManager
import io.ffem.reports.R
import io.ffem.reports.R.style

/**
 * Utility functions for api related actions.
 */
object ApiUtil {
    /**
     * Gets the app version.
     *
     * @return The version name and number
     */
    fun getAppVersion(context: Context): String {
        var version = ""
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = String.format("%s %s", context.getString(R.string.version), packageInfo.versionName)
        } catch (ignored: PackageManager.NameNotFoundException) {
            // do nothing
        }
        return version
    }

    fun isAppInstalled(context: Context, packageName: String?): Boolean {
        return try {
            context.packageManager.getApplicationInfo(packageName!!, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun getThemeResourceId(theme: String): Int {
        var resourceId = -1
        try {
            val res: Class<*> = style::class.java
            val field = res.getField("AppTheme_$theme")
            resourceId = field.getInt(null)
        } catch (ignored: Exception) {
        }
        return resourceId
    }
}