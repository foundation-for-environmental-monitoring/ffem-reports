package io.ffem.reports.helper

import android.content.Context
import android.provider.Settings

object ApkHelper {
    fun isTestDevice(context: Context): Boolean {
        try {
            val testLabSetting: String =
                Settings.System.getString(context.contentResolver, "firebase.test.lab")
            return "true" == testLabSetting
        } catch (ignored: Exception) {
            // do nothing
        }
        return false
    }
}
