package io.ffem.reports.util

import android.content.Context
import android.preference.PreferenceManager
import androidx.annotation.StringRes

/**
 * Various utility functions to get/set values from/to SharedPreferences.
 */
object PreferencesUtil {
    /**
     * Gets a preference key from strings
     *
     * @param context the context
     * @param keyId   the key id
     * @return the string key
     */
    private fun getKey(context: Context, @StringRes keyId: Int): String {
        return context.getString(keyId)
    }

    /**
     * Sets a long value to preferences.
     *
     * @param context the context
     * @param keyId   the key id
     * @param value   the value
     */
    fun setLong(context: Context, @StringRes keyId: Int, value: Long) {
        setLong(context, getKey(context, keyId), value)
    }

    /**
     * Sets a long value to preferences.
     *
     * @param context the context
     * @param key     the int key id
     */
    fun setLong(context: Context?, key: String?, value: Long) {
        val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

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

    fun removeKey(context: Context, @StringRes keyId: Int) {
        removeKey(context, getKey(context, keyId))
    }

    /**
     * Removes the key from the preferences.
     *
     * @param context the context
     * @param key     the key id
     */
    private fun removeKey(context: Context, key: String) {
        val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }
}