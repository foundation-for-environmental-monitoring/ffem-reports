package io.ffem.reports.ui;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import io.ffem.reports.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private static Matcher<View> childAtPosition(
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

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("io.ffem.reports", appContext.getPackageName());
    }

    @Test
    public void mainActivityTest() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.actionInfo), withContentDescription("Info"),
                        childAtPosition(
                                allOf(withId(R.id.layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.textLinkSoftwareNotices), withText("Legal Information"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                4),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Navigate Up"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.textLinkSoftwareNotices), withText("Legal Information"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                4),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction textView = onView(
                allOf(withText("This software incorporates material from the projects listed below"),
                        childAtPosition(
                                allOf(withId(R.id.about_container),
                                        childAtPosition(
                                                IsInstanceOf.instanceOf(android.widget.ScrollView.class),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("This software incorporates material from the projects listed below")));

        ViewInteraction textView2 = onView(
                allOf(withText("Apache License 2.0"),
                        childAtPosition(
                                allOf(withId(R.id.about_container),
                                        childAtPosition(
                                                IsInstanceOf.instanceOf(android.widget.ScrollView.class),
                                                0)),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Apache License 2.0")));

        ViewInteraction textView3 = onView(
                allOf(withText("CC BY 4.0 International"),
                        childAtPosition(
                                allOf(withId(R.id.about_container),
                                        childAtPosition(
                                                IsInstanceOf.instanceOf(android.widget.ScrollView.class),
                                                0)),
                                3),
                        isDisplayed()));
        textView3.check(matches(withText("CC BY 4.0 International")));

        ViewInteraction textView4 = onView(
                allOf(withText("Eclipse Public License 1.0"),
                        childAtPosition(
                                allOf(withId(R.id.about_container),
                                        childAtPosition(
                                                IsInstanceOf.instanceOf(android.widget.ScrollView.class),
                                                0)),
                                5),
                        isDisplayed()));
        textView4.check(matches(withText("Eclipse Public License 1.0")));

        ViewInteraction textView5 = onView(
                allOf(withText("MIT License"),
                        childAtPosition(
                                allOf(withId(R.id.about_container),
                                        childAtPosition(
                                                IsInstanceOf.instanceOf(android.widget.ScrollView.class),
                                                0)),
                                7),
                        isDisplayed()));
        textView5.check(matches(withText("MIT License")));

        ViewInteraction textView6 = onView(
                allOf(withText("Robolectric"),
                        childAtPosition(
                                allOf(withId(R.id.about_container),
                                        childAtPosition(
                                                IsInstanceOf.instanceOf(android.widget.ScrollView.class),
                                                0)),
                                8),
                        isDisplayed()));
        textView6.check(matches(withText("Robolectric")));

        ViewInteraction textView7 = onView(
                allOf(withText("Material Icons"),
                        childAtPosition(
                                allOf(withId(R.id.about_container),
                                        childAtPosition(
                                                IsInstanceOf.instanceOf(android.widget.ScrollView.class),
                                                0)),
                                4),
                        isDisplayed()));
        textView7.check(matches(withText("Material Icons")));

        ViewInteraction textView8 = onView(
                allOf(withText("Android Open Source Project  •  AssertJ  •  Gson  •  Timber  •  "),
                        childAtPosition(
                                allOf(withId(R.id.about_container),
                                        childAtPosition(
                                                IsInstanceOf.instanceOf(android.widget.ScrollView.class),
                                                0)),
                                2),
                        isDisplayed()));
        textView8.check(matches(withText("Android Open Source Project  •  AssertJ  •  Gson  •  Timber  •  ")));

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("Navigate Up"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        pressBack();

        ViewInteraction button = onView(
                allOf(withId(R.id.button_ok),
                        childAtPosition(
                                allOf(withId(R.id.layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button_ok), withText("Launch ffem Collect"),
                        childAtPosition(
                                allOf(withId(R.id.layout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatButton.perform(click());
    }
}
