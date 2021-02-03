package io.ffem.reports.repository

import android.app.Application
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.ffem.reports.model.TestConfig
import io.ffem.reports.model.TestInfo
import io.ffem.reports.util.AssetsManager

class TestConfigRepository(application: Application?) {
    private val assetsManager: AssetsManager = AssetsManager(application!!)

    /**
     * Get the test details from json config.
     *
     * @param id the test id
     * @return the test object
     */
    fun getTestInfo(id: String): TestInfo? {
        return getTestInfoItem(assetsManager.json, id)
    }

    private fun getTestInfoItem(json: String?, id: String): TestInfo? {
        val testInfoList: List<TestInfo>
        try {
            val testConfig = Gson().fromJson(json, TestConfig::class.java)
            if (testConfig != null) {
                testInfoList = testConfig.tests!!
                for (testInfo in testInfoList) {
                    if (testInfo.uuid.equals(id, ignoreCase = true)) {
                        return testInfo
                    }
                }
            }
        } catch (e: JsonSyntaxException) {
            // do nothing
        }
        return null
    }

}