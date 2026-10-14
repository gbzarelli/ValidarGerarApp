package br.com.helpdev.validador


import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.helpdev.libvalidadores.CNPJ
import br.com.helpdev.libvalidadores.CPF
import br.com.helpdev.libvalidadores.CreditCard
import br.com.helpdev.libvalidadores.PIS
import br.com.helpdev.libvalidadores.RENAVAM
import br.com.helpdev.validador.EspressoUtils.checkErrorText
import br.com.helpdev.validador.EspressoUtils.inputTextAndCloseKeyboard
import br.com.helpdev.validador.EspressoUtils.performClickAndCheckStringOfView
import br.com.helpdev.validador.EspressoUtils.checkIsDisplayedViewFromId
import br.com.helpdev.validador.EspressoUtils.getTextFromResId
import br.com.helpdev.validador.EspressoUtils.matchesText
import br.com.helpdev.validador.EspressoUtils.performClickSpinnerAndCheckValue
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
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertTrue(appContext.packageName.startsWith(PACKAGE))
    }

    @Test
    fun app_ShouldDisplay_DefaultScreen_Successfully() {
        checkIsDisplayedViewFromId(R.id.layout_validar)
    }

    @Test
    fun contentStartsBelowStatusBar() {
        androidx.test.espresso.Espresso.onView(androidx.test.espresso.matcher.ViewMatchers.withId(R.id.activity_main))
                .check { view, _ ->
                    val topInset = ViewCompat.getRootWindowInsets(view)
                            ?.getInsets(WindowInsetsCompat.Type.statusBars())?.top ?: 0
                    val originalPadding = view.resources.getDimensionPixelSize(R.dimen.activity_vertical_margin)
                    Assert.assertTrue(
                            "Main content should include the status-bar inset",
                            view.paddingTop >= originalPadding + topInset
                    )
                }
    }

    @Test
    fun cpf_MustBe_Validated_Successfully() {
        inputTextAndCloseKeyboard(R.id.edit_valor_doc, VALID_CPF)
        performClickAndCheckStringOfView(R.id.bt_validar, R.string.validar)
        matchesText(R.id.tv_resultado_validar, R.string.valor_valido)
    }

    @Test
    fun cpf_MustBe_Validated_Failed() {
        inputTextAndCloseKeyboard(R.id.edit_valor_doc, INVALID_CPF)
        performClickAndCheckStringOfView(R.id.bt_validar, R.string.validar)
        matchesText(R.id.tv_resultado_validar, R.string.valor_invalido)
    }

    @Test
    fun cnpj_MustBe_GeneratedAndValidated_Successfully() {
        performClickSpinnerAndCheckValue(R.id.sp_docs, 1, R.string.cnpj)
        performGenerateAndCheckLayout()
        assert(CNPJ.validateCNPJ(getTextFromResId(R.id.tv_resultado_gerar)))
    }

    @Test
    fun cpf_MustBe_GeneratedAndValidated_Successfully() {
        performClickSpinnerAndCheckValue(R.id.sp_docs, 0, R.string.cpf)
        performGenerateAndCheckLayout()
        assert(CPF.validateCPF(getTextFromResId(R.id.tv_resultado_gerar)))
    }

    @Test
    fun renavan_MustBe_GeneratedAndValidated_Successfully() {
        performClickSpinnerAndCheckValue(R.id.sp_docs, 4, R.string.renavan)
        performGenerateAndCheckLayout()
        assert(RENAVAM.validateRENAVAM(getTextFromResId(R.id.tv_resultado_gerar)))
    }

    @Test
    fun pis_MustBe_GeneratedAndValidated_Successfully() {
        performClickSpinnerAndCheckValue(R.id.sp_docs, 2, R.string.pis)
        performGenerateAndCheckLayout()
        assert(PIS.validatePIS(getTextFromResId(R.id.tv_resultado_gerar)))
    }

    @Test
    fun canValidateAndGenerateEachDocumentType() {
        val validators = listOf<(String) -> Boolean>(
                { CPF.validateCPF(it) },
                { CNPJ.validateCNPJ(it) },
                { PIS.validatePIS(it) },
                { CreditCard.isValidCreditCardNumber(it) },
                { RENAVAM.validateRENAVAM(it) }
        )
        val labels = listOf(R.string.cpf, R.string.cnpj, R.string.pis, R.string.credit_card, R.string.renavan)

        validators.forEachIndexed { position, validator ->
            performClickSpinnerAndCheckValue(R.id.sp_docs, position, labels[position])
            performGenerateAndCheckLayout()
            Assert.assertTrue("Generated document at position $position should validate", validator(getTextFromResId(R.id.tv_resultado_gerar)))
        }
    }

    @Test
    fun emptyDocumentShowsRequiredError() {
        performClickAndCheckStringOfView(R.id.bt_validar, R.string.validar)
        checkErrorText(R.id.edit_valor_doc, R.string.entre_com_valor)
    }

    @Test
    fun fragmentWorksAfterActivityRecreation() {
        mActivityTestRule.activity.runOnUiThread { mActivityTestRule.activity.recreate() }
        checkIsDisplayedViewFromId(R.id.layout_validar)
        inputTextAndCloseKeyboard(R.id.edit_valor_doc, VALID_CPF)
        performClickAndCheckStringOfView(R.id.bt_validar, R.string.validar)
        matchesText(R.id.tv_resultado_validar, R.string.valor_valido)
    }


    private fun performGenerateAndCheckLayout() {
        performClickAndCheckStringOfView(R.id.rb_gerar, R.string.gerar)
        performClickAndCheckStringOfView(R.id.bt_gerar, R.string.gerar)
        checkIsDisplayedViewFromId(R.id.layout_valor_gerado)
    }

}
