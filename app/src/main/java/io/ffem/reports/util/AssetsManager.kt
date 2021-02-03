package io.ffem.reports.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import timber.log.Timber;

public final class AssetsManager {

    private static AssetsManager assetsManager;
    private final AssetManager manager;

    private final String json;

    public AssetsManager(Context context) {
        this.manager = context.getAssets();

        json = loadJsonFromAsset(Constants.TESTS_META_FILENAME);

    }

    public static AssetsManager getInstance(Context context) {
        if (assetsManager == null) {
            assetsManager = new AssetsManager(context);
        }

        return assetsManager;
    }

    public String loadJsonFromAsset(String fileName) {
        String json;
        InputStream is = null;
        try {
            if (manager == null) {
                return null;
            }

            is = manager.open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);

            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Timber.e(ex);
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Timber.e(e);
                }
            }
        }
        return json;
    }

    public String getJson() {
        return json;
    }
}
