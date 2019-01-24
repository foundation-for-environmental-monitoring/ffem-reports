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

package io.ffem.reports.ui;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import io.ffem.reports.R;
import io.ffem.reports.databinding.ActivityAboutBinding;
import io.ffem.reports.util.ApiUtil;

/**
 * Activity to display info about the app.
 */
public class AboutActivity extends BaseActivity {

    private NoticesDialogFragment dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAboutBinding b = DataBindingUtil.setContentView(this, R.layout.activity_about);

        b.textVersion.setText(ApiUtil.getAppVersion(this));

        setTitle(R.string.about);
    }

    /**
     * Displays legal information.
     */
    public void onSoftwareNoticesClick(View view) {
        dialog = NoticesDialogFragment.newInstance();
        dialog.show(getFragmentManager(), "NoticesDialog");
    }

    public void onHomeClick(View view) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
