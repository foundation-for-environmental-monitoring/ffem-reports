package io.ffem.reports.ui

import android.widget.ScrollView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import io.ffem.reports.R
import io.ffem.reports.ui.MainActivity
import io.ffem.reports.util.TestUtil
import org.hamcrest.Matchers
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("io.ffem.reports", appContext.packageName)
    }

    @Test
    fun mainActivityTest() {
        val actionMenuItemView = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.actionInfo), ViewMatchers.withContentDescription("Info"),
                        TestUtil.childAtPosition(
                                TestUtil.childAtPosition(
                                        ViewMatchers.withId(R.id.toolbar),
                                        1),
                                0),
                        ViewMatchers.isDisplayed()))
        actionMenuItemView.perform(ViewActions.click())
        val appCompatTextView = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.textLinkSoftwareNotices), ViewMatchers.withText("Legal Information"),
                        TestUtil.childAtPosition(
                                TestUtil.childAtPosition(
                                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                                        1),
                                4),
                        ViewMatchers.isDisplayed()))
        appCompatTextView.perform(ViewActions.click())
        val appCompatImageButton2 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withContentDescription("Navigate Up"),
                        TestUtil.childAtPosition(
                                TestUtil.childAtPosition(
                                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                                        0),
                                0),
                        ViewMatchers.isDisplayed()))
        appCompatImageButton2.perform(ViewActions.click())
        val appCompatTextView2 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.textLinkSoftwareNotices), ViewMatchers.withText("Legal Information"),
                        TestUtil.childAtPosition(
                                TestUtil.childAtPosition(
                                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                                        1),
                                4),
                        ViewMatchers.isDisplayed()))
        appCompatTextView2.perform(ViewActions.click())
        val textView = Espresso.onView(
                Matchers.allOf(ViewMatchers.withText("This software incorporates material from the projects listed below"),
                        TestUtil.childAtPosition(
                                Matchers.allOf(ViewMatchers.withId(R.id.about_container),
                                        TestUtil.childAtPosition(
                                                IsInstanceOf.instanceOf(ScrollView::class.java),
                                                0)),
                                0),
                        ViewMatchers.isDisplayed()))
        textView.check(ViewAssertions.matches(ViewMatchers.withText("This software incorporates material from the projects listed below")))
        val textView2 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withText("Apache License 2.0"),
                        TestUtil.childAtPosition(
                                Matchers.allOf(ViewMatchers.withId(R.id.about_container),
                                        TestUtil.childAtPosition(
                                                IsInstanceOf.instanceOf(ScrollView::class.java),
                                                0)),
                                1),
                        ViewMatchers.isDisplayed()))
        textView2.check(ViewAssertions.matches(ViewMatchers.withText("Apache License 2.0")))
        val textView3 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withText("CC BY 4.0 International"),
                        TestUtil.childAtPosition(
                                Matchers.allOf(ViewMatchers.withId(R.id.about_container),
                                        TestUtil.childAtPosition(
                                                IsInstanceOf.instanceOf(ScrollView::class.java),
                                                0)),
                                3),
                        ViewMatchers.isDisplayed()))
        textView3.check(ViewAssertions.matches(ViewMatchers.withText("CC BY 4.0 International")))
        val textView4 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withText("Eclipse Public License 1.0"),
                        TestUtil.childAtPosition(
                                Matchers.allOf(ViewMatchers.withId(R.id.about_container),
                                        TestUtil.childAtPosition(
                                                IsInstanceOf.instanceOf(ScrollView::class.java),
                                                0)),
                                5),
                        ViewMatchers.isDisplayed()))
        textView4.check(ViewAssertions.matches(ViewMatchers.withText("Eclipse Public License 1.0")))
        val textView5 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withText("MIT License"),
                        TestUtil.childAtPosition(
                                Matchers.allOf(ViewMatchers.withId(R.id.about_container),
                                        TestUtil.childAtPosition(
                                                IsInstanceOf.instanceOf(ScrollView::class.java),
                                                0)),
                                7),
                        ViewMatchers.isDisplayed()))
        textView5.check(ViewAssertions.matches(ViewMatchers.withText("MIT License")))
        val textView6 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withText("Robolectric"),
                        TestUtil.childAtPosition(
                                Matchers.allOf(ViewMatchers.withId(R.id.about_container),
                                        TestUtil.childAtPosition(
                                                IsInstanceOf.instanceOf(ScrollView::class.java),
                                                0)),
                                8),
                        ViewMatchers.isDisplayed()))
        textView6.check(ViewAssertions.matches(ViewMatchers.withText("Robolectric")))
        val textView7 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withText("Material Icons"),
                        TestUtil.childAtPosition(
                                Matchers.allOf(ViewMatchers.withId(R.id.about_container),
                                        TestUtil.childAtPosition(
                                                IsInstanceOf.instanceOf(ScrollView::class.java),
                                                0)),
                                4),
                        ViewMatchers.isDisplayed()))
        textView7.check(ViewAssertions.matches(ViewMatchers.withText("Material Icons")))
        val textView8 = Espresso.onView(
                Matchers.allOf(ViewMatchers.withText("Android Open Source Project  •  AssertJ  •  Gson  •  Timber  •  "),
                        TestUtil.childAtPosition(
                                Matchers.allOf(ViewMatchers.withId(R.id.about_container),
                                        TestUtil.childAtPosition(
                                                IsInstanceOf.instanceOf(ScrollView::class.java),
                                                0)),
                                2),
                        ViewMatchers.isDisplayed()))
        textView8.check(ViewAssertions.matches(ViewMatchers.withText("Android Open Source Project  •  AssertJ  •  Gson  •  Timber  •  ")))
        Espresso.pressBack()
        val appCompatImageButton = Espresso.onView(
                Matchers.allOf(ViewMatchers.withContentDescription("Navigate up"),
                        TestUtil.childAtPosition(
                                Matchers.allOf(ViewMatchers.withId(R.id.toolbar),
                                        TestUtil.childAtPosition(
                                                ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                                                0)),
                                1),
                        ViewMatchers.isDisplayed()))
        appCompatImageButton.perform(ViewActions.click())
        val appCompatButton = Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(R.id.button_ok), ViewMatchers.withText("Launch ffem Collect"),
                        TestUtil.childAtPosition(
                                TestUtil.childAtPosition(
                                        ViewMatchers.withId(R.id.layout),
                                        1),
                                3),
                        ViewMatchers.isDisplayed()))
        appCompatButton.perform(ViewActions.click())
    }
}