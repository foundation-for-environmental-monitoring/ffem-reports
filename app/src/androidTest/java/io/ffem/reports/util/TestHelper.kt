package io.ffem.reports.util

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObjectNotFoundException
import androidx.test.uiautomator.UiSelector
import io.ffem.reports.R
import timber.log.Timber

object TestHelper {
    var mDevice: UiDevice? = null
    fun clickExternalSourceButton(id: String) {
        if (TestConstant.IS_TEST_ID == id) {
            TestUtil.nextSurveyPage(2, "Crop Recommendation")
            clickExternalSourceButton(0)
        }
    }

    @JvmOverloads
    fun clickExternalSourceButton(index: Int, text: String? = TestConstant.GO_TO_TEST) {
        var buttonText = text
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            buttonText = buttonText!!.toUpperCase()
        }
        TestUtil.findButtonInScrollable(buttonText)
        val buttons = mDevice!!.findObjects(By.text(buttonText))
        buttons[index].click()
        mDevice!!.waitForWindowUpdate("", 2000)
        TestUtil.sleep(4000)
    }

    fun clickExternalSourceButton(context: Context, text: String) {
        try {
            var buttonText = text
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (buttonText != null) {
                    buttonText = buttonText.toUpperCase()
                }
            }
            if (buttonText != null) {
                TestUtil.findButtonInScrollable(buttonText)
                mDevice!!.findObject(UiSelector().text(buttonText)).click()
            }

            // New Android OS seems to popup a button for external app
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M
                    && (text == TestConstant.USE_EXTERNAL_SOURCE || text == TestConstant.GO_TO_TEST)) {
                TestUtil.sleep(1000)
                mDevice!!.findObject(By.text(context.getString(R.string.app_name))).click()
                TestUtil.sleep(1000)
            }
            mDevice!!.waitForWindowUpdate("", 2000)
        } catch (e: UiObjectNotFoundException) {
            Timber.e(e)
        }
    }

    fun gotoSurveyForm() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val intent = context.packageManager.getLaunchIntentForPackage(TestConstant.EXTERNAL_SURVEY_PACKAGE_NAME)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        mDevice!!.waitForIdle()
        TestUtil.sleep(1000)
        val addButton = mDevice!!.findObject(UiSelector()
                .resourceId(TestConstant.EXTERNAL_SURVEY_PACKAGE_NAME + ":id/enter_data"))
        try {
            if (addButton.exists() && addButton.isEnabled) {
                addButton.click()
            }
        } catch (e: UiObjectNotFoundException) {
            Timber.e(e)
        }
        mDevice!!.waitForIdle()
        TestUtil.clickListViewItem("Fertilizer Recommendation")
        mDevice!!.waitForIdle()
        val goToStartButton = mDevice!!.findObject(UiSelector()
                .resourceId(TestConstant.EXTERNAL_SURVEY_PACKAGE_NAME + ":id/jumpBeginningButton"))
        try {
            if (goToStartButton.exists() && goToStartButton.isEnabled) {
                goToStartButton.click()
            }
        } catch (e: UiObjectNotFoundException) {
            Timber.e(e)
        }
        mDevice!!.waitForIdle()
    }
}