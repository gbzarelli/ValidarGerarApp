package br.com.helpdev.validador

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RadioGroup
import br.com.helpdev.libvalidadores.*
import kotlinx.android.synthetic.main.frag_validadores_abs.*

/**
 * Created by Guilherme Biff Zarelli on 09/11/16.
 */
abstract class FragValidadoresAbs : Fragment(), View.OnClickListener, RadioGroup.OnCheckedChangeListener {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_validadores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bt_gerar.setOnClickListener(this)
        bt_validar.setOnClickListener(this)
        layout_valor_gerado.setOnClickListener(this)

        radio_group.setOnCheckedChangeListener(this)
        if (savedInstanceState == null) {
            checkRadioGroup()
        }
    }

    private fun checkRadioGroup() {
        hideKeyboard()
        val checkedId = radio_group.checkedRadioButtonId
        if (checkedId == R.id.rb_gerar) {
            layout_gerar.visibility = View.VISIBLE
            disableLayoutValidator()
        } else if (checkedId == R.id.rb_validar) {
            layout_validar.visibility = View.VISIBLE
            disableLayoutGenerate()
        }
    }

    private fun hideKeyboard() {
        try {
            if (activity == null || activity!!.currentFocus == null) return
            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken, 0)
        } catch (t: Throwable) {
        }

    }

    private fun disableLayoutValidator() {
        layout_validar!!.visibility = View.GONE
        tv_resultado_validar!!.text = ""
    }

    private fun disableLayoutGenerate() {
        layout_gerar!!.visibility = View.GONE
        layout_valor_gerado!!.visibility = View.GONE
        tv_resultado_gerar!!.text = ""

    }

    override fun onClick(v: View) {
        hideKeyboard()
        when {
            v.id == R.id.bt_gerar -> generate()
            v.id == R.id.bt_validar -> validate()
            v.id == R.id.layout_valor_gerado -> {
                Snackbar.make(layout_valor_gerado, R.string.valor_copiado, Snackbar.LENGTH_LONG).show()
                setClipboard(tv_resultado_gerar.text.toString())
            }
        }
    }

    private fun validate() {
        val valorDoc = edit_valor_doc.text.toString().trim { it <= ' ' }
        if (valorDoc.isEmpty()) {
            edit_valor_doc!!.error = getString(R.string.entre_com_valor)
            return
        }
        var addMessage = "\n"
        var validado = false
        when (sp_docs.selectedItemPosition) {
            0//CPF
            -> validado = CPF.validateCPF(valorDoc)
            1//CNPJ
            -> validado = CNPJ.validateCNPJ(valorDoc)
            2//PIS
            -> validado = PIS.validatePIS(valorDoc)
            3//CARTÃO DE CREDITO
            -> {
                validado = CreditCard.isValidCreditCardNumber(valorDoc)
                if (validado) {
                    val cardID = CreditCard.getCardID(valorDoc)
                    if (cardID != null) {
                        addMessage += getString(R.string.descricao_credito, cardID.name)
                    }
                }
            }
            4//renavam
            -> validado = RENAVAM.validateRENAVAM(valorDoc)
        }
        tv_resultado_validar.visibility = View.VISIBLE
        addMessage = if (validado) {
            tv_resultado_validar.setTextColor(resources.getColor(R.color.colorPrimaryDark))
            getString(R.string.valor_valido) + addMessage
        } else {
            tv_resultado_validar.setTextColor(Color.RED)
            getString(R.string.valor_invalido) + addMessage
        }
        tv_resultado_validar.text = addMessage
    }

    private fun setClipboard(text: String) {
        if (null == activity) return
        val clipboard = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText(getString(R.string.app_name), text)
        clipboard.primaryClip = clip
    }

    /**
     * <item>CPF</item>
     * <item>CNPJ</item>
     * <item>PIS</item>
     * <item>CARTÃO DE CREDITO</item>
     */
    private fun generate() {
        var valorGerado = ""
        var valorGerado2: String? = null
        when (sp_docs!!.selectedItemPosition) {
            0//CPF
            -> valorGerado = CPF.getCPF(true)
            1//CNPJ
            -> valorGerado = CNPJ.getCNPJ(true)
            2//PIS
            -> valorGerado = PIS.getPIS(true)
            3//CARTÃO DE CREDITO
            -> {
                valorGerado = CreditCard.generateCreditCardNumber()
                val cardID = CreditCard.getCardID(valorGerado)
                if (cardID != null) {
                    valorGerado2 = getString(R.string.descricao_credito, cardID.name)
                }
            }
            4//renavam
            -> valorGerado = RENAVAM.getRENAVAM()
        }

        tv_resultado_gerar.text = valorGerado
        if (valorGerado2 == null) {
            tv_resultado_gerar_2.visibility = View.GONE
        } else {
            tv_resultado_gerar_2.visibility = View.VISIBLE
            tv_resultado_gerar_2.text = valorGerado2
        }
        layout_valor_gerado!!.visibility = View.VISIBLE
    }

    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        checkRadioGroup()
    }
}
