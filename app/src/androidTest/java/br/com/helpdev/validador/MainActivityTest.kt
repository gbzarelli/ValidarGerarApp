package br.com.helpdev.validador


import android.support.test.InstrumentationRegistry
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import br.com.helpdev.libvalidadores.CNPJ
import br.com.helpdev.libvalidadores.CPF
import br.com.helpdev.libvalidadores.PIS
import br.com.helpdev.libvalidadores.RENAVAM
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
        val appContext = InstrumentationRegistry.getTargetContext()
        Assert.assertTrue(appContext.packageName.startsWith(PACKAGE))
    }

    @Test
    fun app_ShouldDisplay_DefaultScreen_Successfully() {
        checkIsDisplayedViewFromId(R.id.layout_validar)
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


    private fun performGenerateAndCheckLayout() {
        performClickAndCheckStringOfView(R.id.rb_gerar, R.string.gerar)
        performClickAndCheckStringOfView(R.id.bt_gerar, R.string.gerar)
        checkIsDisplayedViewFromId(R.id.layout_valor_gerado)
    }

}
