package br.com.helpdev.validador


import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    companion object {
        const val VALID_CPF = "37790288830"
        const val INVALID_CPF = "37791288830"
        const val PACKAGE = "br.com.helpdev.validador"
    }

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getTargetContext()
        Assert.assertTrue(appContext.packageName.startsWith(PACKAGE))
    }

    @Test
    fun shouldDisplayDefault() {
        onView(withId(R.id.layout_validar))
                .check(matches(isDisplayed()))
    }

    @Test
    fun cpfMustBeValidatedSuccessfully() {
        val editText = onView(allOf(withId(R.id.edit_valor_doc), isDisplayed()))
        editText.perform(replaceText(VALID_CPF), closeSoftKeyboard())

        val btValidate = onView(allOf(withId(R.id.bt_validar), isDisplayed()))
        btValidate.perform(click())

        val tvResult = onView(allOf(withId(R.id.tv_resultado_validar), isDisplayed()))
        tvResult.check(matches(withText(R.string.valor_valido)))
    }

    @Test
    fun cpfMustBeValidatedFailed() {
        val editText = onView(allOf(withId(R.id.edit_valor_doc), isDisplayed()))
        editText.perform(replaceText(INVALID_CPF), closeSoftKeyboard())

        val btValidate = onView(allOf(withId(R.id.bt_validar), isDisplayed()))
        btValidate.perform(click())

        val tvResult = onView(allOf(withId(R.id.tv_resultado_validar), isDisplayed()))
        tvResult.check(matches(withText(R.string.valor_invalido)))
    }

}
