package io.ffem.reports.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import timber.log.Timber;

import static io.ffem.reports.util.TestHelper.clickExternalSourceButton;
import static io.ffem.reports.util.TestHelper.mDevice;

/**
 * Utility functions for automated testing
 */
public class TestUtil {

    private TestUtil() {
    }

    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Timber.e(e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    static void findButtonInScrollable(String name) {
        UiScrollable listView = new UiScrollable(new UiSelector().className(ScrollView.class.getName()));
        listView.setMaxSearchSwipes(10);
        listView.waitForExists(5000);
        try {
            listView.scrollTextIntoView(name);
        } catch (Exception ignored) {
        }
    }

    static boolean clickListViewItem(String name) {
        UiScrollable listView = new UiScrollable(new UiSelector());
        listView.setMaxSearchSwipes(4);
        listView.waitForExists(3000);
        UiObject listViewItem;
        try {
            if (listView.scrollTextIntoView(name)) {
                listViewItem = listView.getChildByText(new UiSelector()
                        .className(TextView.class.getName()), "" + name + "");
                listViewItem.click();
            } else {
                return false;
            }
        } catch (UiObjectNotFoundException e) {
            return false;
        }
        return true;
    }

    private static void swipeLeft() {
        mDevice.waitForIdle();
        mDevice.swipe(500, 400, 50, 400, 4);
        mDevice.waitForIdle();
    }

    private static void swipeDown() {
        for (int i = 0; i < 3; i++) {
            mDevice.waitForIdle();
            mDevice.swipe(300, 400, 300, 750, 4);
        }
    }

    public static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    static void nextSurveyPage(Context context) {
        clickExternalSourceButton(context, "Launch");
    }

    static void nextSurveyPage(int times) {
        nextSurveyPage(times, "");
    }

    static void nextSurveyPage(int times, String tabName) {

        UiObject2 tab = mDevice.findObject(By.text(tabName));
        if (tab == null) {
            for (int i = 0; i < 12; i++) {
                swipeLeft();
                mDevice.waitForIdle();
                tab = mDevice.findObject(By.text(tabName));
                if (tab != null) {
                    break;
                }
                tab = mDevice.findObject(By.text("Soil Tests 1"));
                if (tab != null) {
                    for (int ii = 0; ii < times; ii++) {
                        mDevice.waitForIdle();
                        swipeLeft();
                        sleep(300);
                        tab = mDevice.findObject(By.text(tabName));
                        if (tab != null) {
                            break;
                        }
                    }

                    break;
                }
            }
        }

        swipeDown();
        mDevice.waitForIdle();
    }
}
