package br.com.helpdev.validador

import android.support.annotation.IntegerRes
import android.support.annotation.StringRes
import android.support.test.espresso.Espresso
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import android.widget.TextView
import org.hamcrest.Matcher
import org.hamcrest.Matchers

object EspressoUtils {

    fun matchesText(@IntegerRes resIdTextView: Int, @StringRes resIdString: Int) {
        Espresso.onView(Matchers.allOf(ViewMatchers.withId(resIdTextView), ViewMatchers.isDisplayed()))
                .check(ViewAssertions.matches(ViewMatchers.withText(resIdString)))
    }

    fun inputTextAndCloseKeyboard(@IntegerRes resIdEdit: Int, value: String) {
        Espresso.onView(Matchers.allOf(ViewMatchers.withId(resIdEdit), ViewMatchers.isDisplayed()))
                .perform(ViewActions.replaceText(value), ViewActions.closeSoftKeyboard())
    }

    fun checkIsDisplayedViewFromId(@IntegerRes resId: Int) {
        Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(resId), ViewMatchers.isDisplayed())
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    fun performClickAndCheckStringOfView(@IntegerRes resIdRadioButton: Int, @StringRes resIdString: Int) {
        Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(resIdRadioButton), ViewMatchers.isDisplayed())
        ).perform(ViewActions.click()).check(ViewAssertions.matches(ViewMatchers.withText(resIdString)))
    }

    fun performClickSpinnerAndCheckValue(@IntegerRes resIdSpinner: Int, position: Int, @StringRes resIdString: Int) {
        Espresso.onView(
                Matchers.allOf(ViewMatchers.withId(resIdSpinner), ViewMatchers.isDisplayed())
        ).perform(ViewActions.click())

        Espresso.onData(Matchers.anything())
                .atPosition(position)
                .perform(ViewActions.click())
                .check(ViewAssertions.matches(ViewMatchers.withText(resIdString)))
    }

    fun getTextFromResId(@IntegerRes resId: Int) = getText(ViewMatchers.withId(resId))

    fun getText(matcher: Matcher<View>): String {
        var value = ""
        Espresso.onView(matcher).perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(TextView::class.java)
            }

            override fun getDescription(): String {
                return "Getting text from a TextView"
            }

            override fun perform(uiController: UiController, view: View) {
                val tv = view as TextView //Save, because of check in getConstraints()
                value = tv.text.toString()
            }
        })
        return value
    }
}