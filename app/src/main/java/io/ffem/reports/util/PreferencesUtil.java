package io.ffem.reports.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import androidx.annotation.StringRes;

/**
 * Various utility functions to get/set values from/to SharedPreferences.
 */
@SuppressWarnings("SameParameterValue")
public final class PreferencesUtil {

    private PreferencesUtil() {
    }

    /**
     * Gets a preference key from strings
     *
     * @param context the context
     * @param keyId   the key id
     * @return the string key
     */
    private static String getKey(Context context, @StringRes int keyId) {
        return context.getString(keyId);
    }

    /**
     * Sets a long value to preferences.
     *
     * @param context the context
     * @param keyId   the key id
     * @param value   the value
     */
    @SuppressWarnings({"unused"})
    public static void setLong(Context context, @StringRes int keyId, long value) {
        setLong(context, getKey(context, keyId), value);
    }

    /**
     * Sets a long value to preferences.
     *
     * @param context the context
     * @param key     the int key id
     */
    @SuppressWarnings("WeakerAccess")
    public static void setLong(Context context, String key, long value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * Gets a string value from preferences.
     *
     * @param context      the context
     * @param keyId        the key id
     * @param defaultValue default value
     * @return the stored string value
     */
    public static String getString(Context context, String keyId, String defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(keyId, defaultValue);
    }

    /**
     * Sets a string value to preferences.
     *
     * @param context the context
     * @param keyId   the key id
     */
    @SuppressWarnings("WeakerAccess")
    public static void setString(Context context, String keyId, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putString(keyId, value);
        editor.apply();
    }

    @SuppressWarnings({"unused"})
    public static void removeKey(Context context, @StringRes int keyId) {
        removeKey(context, getKey(context, keyId));
    }

    /**
     * Removes the key from the preferences.
     *
     * @param context the context
     * @param key     the key id
     */
    private static void removeKey(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

}
