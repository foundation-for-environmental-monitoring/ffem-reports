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

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import io.ffem.reports.R
import io.ffem.reports.databinding.ActivityAboutBinding
import io.ffem.reports.helper.ApkHelper.isTestDevice
import io.ffem.reports.util.ApiUtil

/**
 * Activity to display info about the app.
 */
class AboutActivity : BaseActivity() {
    private lateinit var binding: ActivityAboutBinding

    private var dialog: NoticesDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.textVersion.text = ApiUtil.getAppVersion(this)

        setTitle(R.string.about)
    }

    /**
     * Displays legal information.
     */
    fun onSoftwareNoticesClick(@Suppress("UNUSED_PARAMETER") view: View) {
        if (!isTestDevice(this)) {
            dialog = NoticesDialogFragment.newInstance()
            dialog!!.show(supportFragmentManager, "NoticesDialog")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}