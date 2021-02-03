package io.ffem.reports.util

import android.content.Context
import android.content.res.AssetManager
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

class AssetsManager(context: Context) {
    private val manager: AssetManager?
    val json: String?
    fun loadJsonFromAsset(fileName: String?): String? {
        val json: String
        var `is`: InputStream? = null
        try {
            if (manager == null) {
                return null
            }
            `is` = manager.open(fileName!!)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            json = String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            Timber.e(ex)
            return null
        } finally {
            if (`is` != null) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    Timber.e(e)
                }
            }
        }
        return json
    }

    companion object {
        private var assetsManager: AssetsManager? = null
        @JvmStatic
        fun getInstance(context: Context): AssetsManager? {
            if (assetsManager == null) {
                assetsManager = AssetsManager(context)
            }
            return assetsManager
        }
    }

    init {
        manager = context.assets
        json = loadJsonFromAsset(Constants.TESTS_META_FILENAME)
    }
}