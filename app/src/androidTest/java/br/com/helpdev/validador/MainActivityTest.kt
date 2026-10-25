package br.com.helpdev.validador

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import android.text.Spanned
import android.text.style.URLSpan
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.helpdev.validador.EspressoUtils.checkErrorText
import br.com.helpdev.validador.EspressoUtils.checkIsDisplayedViewFromId
import br.com.helpdev.validador.EspressoUtils.inputTextAndCloseKeyboard
import br.com.helpdev.validador.EspressoUtils.matchesText
import br.com.helpdev.validador.EspressoUtils.performClickAndCheckStringOfView
import br.com.helpdev.validador.EspressoUtils.performClickSpinnerAndCheckValue
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    companion object {
        const val PACKAGE = "br.com.helpdev.validador"
    }

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun appUsesExpectedPackageAndShowsValidationScreen() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertTrue(appContext.packageName.startsWith(PACKAGE))
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
    fun showsIndependentLocalValidationDisclaimerAndOfficialSources() {
        androidx.test.espresso.Espresso.onView(androidx.test.espresso.matcher.ViewMatchers.withId(R.id.layout_validar))
            .check { view, _ ->
                val sourceId = view.resources.getIdentifier("tv_fontes_oficiais", "id", view.context.packageName)
                Assert.assertNotEquals("The official sources section should have a view", 0, sourceId)
                val sourceView = view.findViewById<TextView>(sourceId)
                Assert.assertNotNull("The official sources section should be present", sourceView)
                val scopeView = view.findViewById<TextView>(R.id.tv_escopo_validacao)
                Assert.assertTrue(scopeView.text.toString().contains("não afiliado"))
                Assert.assertTrue(scopeView.text.toString().contains("nem confirma existência"))
                val text = sourceView.text.toString()
                Assert.assertTrue(text.contains("CPF"))
                Assert.assertTrue(text.contains("CNPJ alfanumérico"))
                Assert.assertTrue(text.contains("Cálculo do dígito verificador do CNPJ"))
                Assert.assertTrue(text.contains("PIS/NIT"))
                Assert.assertTrue(text.contains("RENAVAM"))
                Assert.assertTrue(sourceView.linksClickable)
                val sourceText = sourceView.text as Spanned
                val links = sourceText.getSpans(0, sourceText.length, URLSpan::class.java)
                Assert.assertEquals("Every listed identifier should link to an official source", 5, links.size)
                val actualLinks = links.associate { link ->
                    val label = sourceText.subSequence(
                        sourceText.getSpanStart(link),
                        sourceText.getSpanEnd(link)
                    ).toString()
                    label to link.url
                }
                Assert.assertEquals(
                    "Each government source label should point to its intended official URL",
                    mapOf(
                        "CPF — Receita Federal" to "https://www.gov.br/pt-br/servicos/consultar-cadastro-de-pessoas-fisicas",
                        "CNPJ alfanumérico — Receita Federal" to "https://www.gov.br/receitafederal/pt-br/acesso-a-informacao/acoes-e-programas/programas-e-atividades/cnpj-alfanumerico/cnpj-alfa",
                        "Cálculo do dígito verificador do CNPJ — Receita Federal" to "https://www.gov.br/receitafederal/pt-br/centrais-de-conteudo/publicacoes/documentos-tecnicos/cnpj",
                        "PIS/NIT — INSS" to "https://www.gov.br/pt-br/servicos/realizar-inscricao-no-inss",
                        "RENAVAM — Senatran" to "https://www.gov.br/pt-br/servicos/consultar-dados-de-veiculo-na-base-renavam"
                    ),
                    actualLinks
                )
            }
    }

    @Test
    fun validatesCpfLocally() {
        validateDocument(0, R.string.cpf, "37790288830", R.string.valor_valido)
    }

    @Test
    fun validatesNumericCnpjLocally() {
        validateDocument(1, R.string.cnpj, "11222333000181", R.string.valor_valido)
    }

    @Test
    fun validatesAlphanumericCnpjLocally() {
        validateDocument(1, R.string.cnpj, "12ABC34501DE35", R.string.valor_valido)
    }

    @Test
    fun validatesPisLocally() {
        validateDocument(2, R.string.pis, "12044500606", R.string.valor_valido)
    }

    @Test
    fun validatesRenavamLocally() {
        validateDocument(3, R.string.renavan, "12345678900", R.string.valor_valido)
    }

    @Test
    fun invalidCheckDigitsAreRejected() {
        validateDocument(1, R.string.cnpj, "12ABC34501DE36", R.string.valor_invalido)
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
        validateDocument(0, R.string.cpf, "37790288830", R.string.valor_valido)
    }

    private fun validateDocument(position: Int, label: Int, value: String, expected: Int) {
        performClickSpinnerAndCheckValue(R.id.sp_docs, position, label)
        inputTextAndCloseKeyboard(R.id.edit_valor_doc, value)
        performClickAndCheckStringOfView(R.id.bt_validar, R.string.validar)
        matchesText(R.id.tv_resultado_validar, expected)
    }
}
