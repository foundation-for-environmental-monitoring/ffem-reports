package io.ffem.reports.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.test.uiautomator.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import timber.log.Timber

/**
 * Utility functions for automated testing
 */
object TestUtil {
    fun sleep(time: Int) {
        try {
            Thread.sleep(time.toLong())
        } catch (e: InterruptedException) {
            Timber.e(e)
        }
    }

    fun findButtonInScrollable(name: String?) {
        val listView = UiScrollable(UiSelector().className(ScrollView::class.java.name))
        listView.maxSearchSwipes = 10
        listView.waitForExists(5000)
        try {
            listView.scrollTextIntoView(name)
        } catch (ignored: Exception) {
        }
    }

    fun clickListViewItem(name: String) {
        val listView = UiScrollable(UiSelector())
        listView.maxSearchSwipes = 4
        listView.waitForExists(3000)
        val listViewItem: UiObject
        try {
            if (listView.scrollTextIntoView(name)) {
                listViewItem = listView.getChildByText(UiSelector()
                        .className(TextView::class.java.name), "" + name + "")
                listViewItem.click()
            }
        } catch (ignored: UiObjectNotFoundException) {
        }
    }

    private fun swipeLeft() {
        TestHelper.mDevice!!.waitForIdle()
        TestHelper.mDevice!!.swipe(500, 400, 50, 400, 4)
        TestHelper.mDevice!!.waitForIdle()
    }

    private fun swipeDown() {
        for (i in 0..2) {
            TestHelper.mDevice!!.waitForIdle()
            TestHelper.mDevice!!.swipe(300, 400, 300, 750, 4)
        }
    }

    fun childAtPosition(
            parentMatcher: Matcher<View?>, position: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return (parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position))
            }
        }
    }

    fun nextSurveyPage(context: Context) {
        TestHelper.clickExternalSourceButton(context, "Launch")
    }

    @JvmOverloads
    fun nextSurveyPage(times: Int, tabName: String? = "") {
        var tab = TestHelper.mDevice!!.findObject(By.text(tabName))
        if (tab == null) {
            for (i in 0..11) {
                swipeLeft()
                TestHelper.mDevice!!.waitForIdle()
                tab = TestHelper.mDevice!!.findObject(By.text(tabName))
                if (tab != null) {
                    break
                }
                tab = TestHelper.mDevice!!.findObject(By.text("Soil Tests 1"))
                if (tab != null) {
                    for (ii in 0 until times) {
                        TestHelper.mDevice!!.waitForIdle()
                        swipeLeft()
                        sleep(300)
                        tab = TestHelper.mDevice!!.findObject(By.text(tabName))
                        if (tab != null) {
                            break
                        }
                    }
                    break
                }
            }
        }
        swipeDown()
        TestHelper.mDevice!!.waitForIdle()
    }
}