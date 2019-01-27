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

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import io.ffem.reports.R;
import io.ffem.reports.util.AlertUtil;

public class MainActivity extends BaseActivity {

    private static final String EXTERNAL_APP_PACKAGE_NAME = "io.ffem.collect";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataBindingUtil.setContentView(this, R.layout.activity_main);

        setTitle(R.string.app_name);
    }

    public void onOkClicked(View view) {
        Intent intent = getPackageManager()
                .getLaunchIntentForPackage(EXTERNAL_APP_PACKAGE_NAME);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            closeApp(1000);
        } else {
            alertDependantAppNotFound();
        }
    }

    private void alertDependantAppNotFound() {
        String message = String.format("%s\r\n\r\n%s", "ffem Collect is not installed.",
                "Please install the ffem Collect app from Play store.");

        AlertUtil.showAlert(this, R.string.notFound, message, R.string.close,
                (dialogInterface, i) -> closeApp(0),
                null, null);
    }

    private void closeApp(int delay) {
        (new Handler()).postDelayed(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            } else {
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            }
        }, delay);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onInfoClick(MenuItem item) {
        final Intent intent = new Intent(getBaseContext(), AboutActivity.class);
        startActivity(intent);
    }
}