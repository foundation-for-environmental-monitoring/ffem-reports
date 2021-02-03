package io.ffem.reports.util;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import java.util.List;

import io.ffem.reports.R;
import timber.log.Timber;

import static io.ffem.reports.util.TestUtil.clickListViewItem;
import static io.ffem.reports.util.TestUtil.findButtonInScrollable;
import static io.ffem.reports.util.TestUtil.nextSurveyPage;
import static io.ffem.reports.util.TestUtil.sleep;

public final class TestHelper {

    public static UiDevice mDevice;

    private TestHelper() {
    }

    public static void clickExternalSourceButton(String id) {
        if (TestConstant.IS_TEST_ID.equals(id)) {
            nextSurveyPage(2, "Crop Recommendation");
            clickExternalSourceButton(0);
        }
    }

    public static void clickExternalSourceButton(int index) {
        clickExternalSourceButton(index, TestConstant.GO_TO_TEST);
    }

    public static void clickExternalSourceButton(int index, String text) {

        String buttonText = text;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            buttonText = buttonText.toUpperCase();
        }

        findButtonInScrollable(buttonText);

        List<UiObject2> buttons = mDevice.findObjects(By.text(buttonText));
        buttons.get(index).click();

        mDevice.waitForWindowUpdate("", 2000);

        sleep(4000);
    }

    public static void clickExternalSourceButton(Context context, String text) {
        try {

            String buttonText = text;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (buttonText != null) {
                    buttonText = buttonText.toUpperCase();
                }
            }

            if (buttonText != null) {
                findButtonInScrollable(buttonText);
                mDevice.findObject(new UiSelector().text(buttonText)).click();
            }

            // New Android OS seems to popup a button for external app
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M
                    && (text.equals(TestConstant.USE_EXTERNAL_SOURCE)
                    || text.equals(TestConstant.GO_TO_TEST))) {
                sleep(1000);
                mDevice.findObject(By.text(context.getString(R.string.app_name))).click();
                sleep(1000);
            }

            mDevice.waitForWindowUpdate("", 2000);

        } catch (UiObjectNotFoundException e) {
            Timber.e(e);
        }
    }

    public static void gotoSurveyForm() {

        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(TestConstant.EXTERNAL_SURVEY_PACKAGE_NAME);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);

        mDevice.waitForIdle();

        sleep(1000);

        UiObject addButton = mDevice.findObject(new UiSelector()
                .resourceId(TestConstant.EXTERNAL_SURVEY_PACKAGE_NAME + ":id/enter_data"));

        try {
            if (addButton.exists() && addButton.isEnabled()) {
                addButton.click();
            }
        } catch (UiObjectNotFoundException e) {
            Timber.e(e);
        }

        mDevice.waitForIdle();

        clickListViewItem("Fertilizer Recommendation");

        mDevice.waitForIdle();

        UiObject goToStartButton = mDevice.findObject(new UiSelector()
                .resourceId(TestConstant.EXTERNAL_SURVEY_PACKAGE_NAME + ":id/jumpBeginningButton"));

        try {
            if (goToStartButton.exists() && goToStartButton.isEnabled()) {
                goToStartButton.click();
            }
        } catch (UiObjectNotFoundException e) {
            Timber.e(e);
        }

        mDevice.waitForIdle();
    }
}
