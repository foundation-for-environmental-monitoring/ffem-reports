package io.ffem.reports.util

import android.content.Context
import android.preference.PreferenceManager

/**
 * Various utility functions to get/set values from/to SharedPreferences.
 */
object PreferencesUtil {

    /**
     * Gets a string value from preferences.
     *
     * @param context      the context
     * @param keyId        the key id
     * @param defaultValue default value
     * @return the stored string value
     */
    fun getString(context: Context?, keyId: String?, defaultValue: String?): String? {
        val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
        return sharedPreferences.getString(keyId, defaultValue)
    }

    /**
     * Sets a string value to preferences.
     *
     * @param context the context
     * @param keyId   the key id
     */
    fun setString(context: Context?, keyId: String?, value: String?) {
        val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString(keyId, value)
        editor.apply()
    }
}