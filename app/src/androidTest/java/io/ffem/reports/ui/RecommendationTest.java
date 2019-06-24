package io.ffem.reports.ui;


import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.ffem.reports.R;
import io.ffem.reports.util.TestConstant;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static io.ffem.reports.util.TestHelper.clickExternalSourceButton;
import static io.ffem.reports.util.TestHelper.gotoSurveyForm;
import static io.ffem.reports.util.TestHelper.mDevice;
import static io.ffem.reports.util.TestUtil.childAtPosition;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RecommendationTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void initialize() {
        if (mDevice == null) {
            mDevice = UiDevice.getInstance(getInstrumentation());
        }
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("io.ffem.reports", appContext.getPackageName());
    }

    @Test
    public void recommendationTest() {
        onView(allOf(withId(R.id.button_ok), withText("Launch ffem Collect"),
                childAtPosition(
                        childAtPosition(
                                withId(R.id.layout),
                                1),
                        3),
                isDisplayed())).perform(click());

        gotoSurveyForm();

        clickExternalSourceButton(TestConstant.IS_TEST_ID);
    }
}
