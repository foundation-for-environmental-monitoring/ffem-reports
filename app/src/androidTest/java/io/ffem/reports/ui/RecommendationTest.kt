package io.ffem.reports.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import io.ffem.reports.R
import io.ffem.reports.ui.MainActivity
import io.ffem.reports.util.TestConstant
import io.ffem.reports.util.TestHelper
import io.ffem.reports.util.TestUtil
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecommendationTest {
    @Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("io.ffem.reports", appContext.packageName)
    }

    @Test
    fun recommendationTest() {
        Espresso.onView(CoreMatchers.allOf(ViewMatchers.withId(R.id.button_ok), ViewMatchers.withText("Launch ffem Collect"),
                TestUtil.childAtPosition(
                        TestUtil.childAtPosition(
                                ViewMatchers.withId(R.id.layout),
                                1),
                        3),
                ViewMatchers.isDisplayed())).perform(ViewActions.click())
        TestHelper.gotoSurveyForm()
        TestHelper.clickExternalSourceButton(TestConstant.IS_TEST_ID)
    }
}