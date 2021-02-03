/*
 * Copyright (C) ffem (Foundation for Environmental Monitoring)
 *
 * This file is part of ffem reports
 *
 * ffem Reports is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * ffem Reports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ffem Reports. If not, see <http://www.gnu.org/licenses/>.
 */
package io.ffem.reports.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import io.ffem.reports.ConstantKey
import io.ffem.reports.R
import io.ffem.reports.util.ApiUtil
import io.ffem.reports.util.PreferencesUtil
import java.util.*
import kotlin.math.min

/**
 * The base activity with common functions.
 */
abstract class BaseActivity : AppCompatActivity() {
    private var appTheme = R.style.AppTheme_Main
    private var mTitle: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var appThemeResId = -1
        val bundle = intent.extras
        if (bundle != null) {
            try {
                var theme = bundle.getString("theme")
                if (theme != null) {
                    theme = theme[0].toString().toUpperCase(Locale.ROOT) +
                            theme.substring(1, min(theme.length, 10)).toLowerCase(Locale.ROOT)
                    appThemeResId = ApiUtil.getThemeResourceId(theme)
                    val packageName = callingActivity?.packageName
                    PreferencesUtil.setString(this, ConstantKey.APP_THEME, theme)
                    PreferencesUtil.setString(this, theme, packageName)
                }
            } catch (ignored: Exception) {
            }
        }
        if (appThemeResId == -1) {
            val theme = PreferencesUtil.getString(this, ConstantKey.APP_THEME, "")
            if (theme!!.isNotEmpty()) {
                val packageName = PreferencesUtil.getString(this, theme, "")
                if (packageName!!.isNotEmpty() && ApiUtil.isAppInstalled(this, packageName)) {
                    appThemeResId = ApiUtil.getThemeResourceId(theme)
                }
            }
        }
        if (appThemeResId != -1) {
            appTheme = appThemeResId
        }
        updateTheme()
    }

    private fun updateTheme() {
        setTheme(appTheme)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val typedValue = TypedValue()
        theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
        val windowBackground = typedValue.data
        window.setBackgroundDrawable(ColorDrawable(windowBackground))
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        if (toolbar != null) {
            try {
                setSupportActionBar(toolbar)
            } catch (ignored: Exception) {
                // do nothing
            }
        }
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = ""
        }
    }

    override fun onResume() {
        super.onResume()
        if (supportActionBar != null) {
            supportActionBar!!.title = ""
        }

        if (mTitle != null) {
            title = mTitle
        }
    }

    override fun setTitle(title: CharSequence) {
        val textTitle = findViewById<TextView>(R.id.textToolbarTitle)
        if (textTitle != null) {
            mTitle = title.toString()
            textTitle.text = title
        }
    }

    override fun setTitle(titleId: Int) {
        val textTitle = findViewById<TextView>(R.id.textToolbarTitle)
        if (textTitle != null && titleId != 0) {
            mTitle = getString(titleId)
            textTitle.setText(titleId)
        }
    }
}