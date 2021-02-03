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

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.ffem.reports.ConstantKey;
import io.ffem.reports.R;
import io.ffem.reports.util.ApiUtil;
import io.ffem.reports.util.PreferencesUtil;

/**
 * The base activity with common functions.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private int appTheme = R.style.AppTheme_Main;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int appThemeResId = -1;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                String theme = bundle.getString("theme");
                if (theme != null) {
                    theme = String.valueOf(theme.charAt(0)).toUpperCase() +
                            theme.substring(1, Math.min(theme.length(), 10)).toLowerCase();
                    appThemeResId = getThemeResourceId(theme);

                    String packageName = Objects.requireNonNull(getCallingActivity()).getPackageName();

                    PreferencesUtil.setString(this, ConstantKey.APP_THEME, theme);
                    PreferencesUtil.setString(this, theme, packageName);
                }
            } catch (Exception ignored) {
            }
        }

        if (appThemeResId == -1) {
            String theme = PreferencesUtil.getString(this, ConstantKey.APP_THEME, "");
            if (!theme.isEmpty()) {
                String packageName = PreferencesUtil.getString(this, theme, "");
                if (!packageName.isEmpty() && ApiUtil.isAppInstalled(this, packageName)) {
                    appThemeResId = getThemeResourceId(theme);
                }
            }
        }

        if (appThemeResId != -1) {
            appTheme = appThemeResId;
        }

        updateTheme();
    }

    private int getThemeResourceId(String theme) {
        int resourceId = -1;
        try {
            Class res = R.style.class;
            Field field = res.getField("AppTheme_" + theme);
            resourceId = field.getInt(null);

        } catch (Exception ignored) {
        }

        return resourceId;
    }

    private void updateTheme() {

        setTheme(appTheme);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        int windowBackground = typedValue.data;
        getWindow().setBackgroundDrawable(new ColorDrawable(windowBackground));

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            try {
                setSupportActionBar(toolbar);
            } catch (Exception ignored) {
                // do nothing
            }
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        setTitle(mTitle);
    }

    @Override
    public void setTitle(CharSequence title) {
        TextView textTitle = findViewById(R.id.textToolbarTitle);
        if (textTitle != null && title != null) {
            mTitle = title.toString();
            textTitle.setText(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        TextView textTitle = findViewById(R.id.textToolbarTitle);
        if (textTitle != null && titleId != 0) {
            mTitle = getString(titleId);
            textTitle.setText(titleId);
        }
    }
}


