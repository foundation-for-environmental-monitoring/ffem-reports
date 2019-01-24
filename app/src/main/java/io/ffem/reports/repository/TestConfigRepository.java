package io.ffem.reports.repository;


import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.List;

import androidx.annotation.Nullable;
import io.ffem.reports.model.TestConfig;
import io.ffem.reports.model.TestInfo;
import io.ffem.reports.util.AssetsManager;

public class TestConfigRepository {

    private final AssetsManager assetsManager;

    public TestConfigRepository(Application application) {
        assetsManager = new AssetsManager(application);
    }

    /**
     * Get the test details from json config.
     *
     * @param id the test id
     * @return the test object
     */
    public TestInfo getTestInfo(final String id) {

        TestInfo testInfo;
        testInfo = getTestInfoItem(assetsManager.getJson(), id);
        if (testInfo != null) {
            return testInfo;
        }
        return null;
    }

    @Nullable
    private TestInfo getTestInfoItem(String json, String id) {

        List<TestInfo> testInfoList;
        try {
            TestConfig testConfig = new Gson().fromJson(json, TestConfig.class);
            if (testConfig != null) {
                testInfoList = testConfig.getTests();

                for (TestInfo testInfo : testInfoList) {
                    if (testInfo.getUuid().equalsIgnoreCase(id)) {
                        return testInfo;
                    }
                }
            }
        } catch (JsonSyntaxException e) {
            // do nothing
        }

        return null;
    }
}
