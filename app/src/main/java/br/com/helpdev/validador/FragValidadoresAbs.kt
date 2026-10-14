package br.com.helpdev.validador

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import br.com.helpdev.libvalidadores.CNPJ
import br.com.helpdev.libvalidadores.CPF
import br.com.helpdev.libvalidadores.CreditCard
import br.com.helpdev.libvalidadores.PIS
import br.com.helpdev.libvalidadores.RENAVAM

abstract class FragValidadoresAbs : Fragment(), View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private var views: FragmentViews? = null
    private val binding: FragmentViews
        get() = checkNotNull(views) { "Fragment view is not available" }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.frag_validadores, container, false)
        views = FragmentViews(root)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.generateButton.setOnClickListener(this)
        binding.validateButton.setOnClickListener(this)
        binding.generatedValueLayout.setOnClickListener(this)
        binding.documentTypeGroup.setOnCheckedChangeListener(this)
        if (savedInstanceState == null) checkRadioGroup()
    }

    override fun onDestroyView() {
        binding.generateButton.setOnClickListener(null)
        binding.validateButton.setOnClickListener(null)
        binding.generatedValueLayout.setOnClickListener(null)
        binding.documentTypeGroup.setOnCheckedChangeListener(null)
        views = null
        super.onDestroyView()
    }

    private fun checkRadioGroup() {
        hideKeyboard()
        when (binding.documentTypeGroup.checkedRadioButtonId) {
            R.id.rb_gerar -> {
                binding.generateLayout.visibility = View.VISIBLE
                disableLayoutValidator()
            }
            R.id.rb_validar -> {
                binding.validateLayout.visibility = View.VISIBLE
                disableLayoutGenerate()
            }
        }
    }

    private fun hideKeyboard() {
        try {
            val currentActivity = activity ?: return
            val currentFocus = currentActivity.currentFocus ?: return
            val inputMethodManager = currentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        } catch (_: Throwable) {
        }
    }

    private fun disableLayoutValidator() {
        binding.validateLayout.visibility = View.GONE
        binding.validationResult.text = ""
    }

    private fun disableLayoutGenerate() {
        binding.generateLayout.visibility = View.GONE
        binding.generatedValueLayout.visibility = View.GONE
        binding.generatedValue.text = ""
    }

    override fun onClick(view: View) {
        hideKeyboard()
        when (view.id) {
            R.id.bt_gerar -> generate()
            R.id.bt_validar -> validate()
            R.id.layout_valor_gerado -> {
                Snackbar.make(binding.generatedValueLayout, R.string.valor_copiado, Snackbar.LENGTH_LONG).show()
                setClipboard(binding.generatedValue.text.toString())
            }
        }
    }

    private fun validate() {
        val value = binding.documentValue.text.toString().trim { it <= ' ' }
        if (value.isEmpty()) {
            binding.documentValue.error = getString(R.string.entre_com_valor)
            return
        }

        var messageSuffix = "\n"
        val isValid = when (binding.documentsSpinner.selectedItemPosition) {
            0 -> CPF.validateCPF(value)
            1 -> CNPJ.validateCNPJ(value)
            2 -> PIS.validatePIS(value)
            3 -> {
                val valid = CreditCard.isValidCreditCardNumber(value)
                if (valid) {
                    CreditCard.getCardID(value)?.let { messageSuffix += getString(R.string.descricao_credito, it.name) }
                }
                valid
            }
            4 -> RENAVAM.validateRENAVAM(value)
            else -> false
        }

        binding.validationResult.visibility = View.VISIBLE
        messageSuffix = if (isValid) {
            binding.validationResult.setTextColor(resources.getColor(R.color.colorPrimaryDark, requireContext().theme))
            getString(R.string.valor_valido) + messageSuffix
        } else {
            binding.validationResult.setTextColor(Color.RED)
            getString(R.string.valor_invalido) + messageSuffix
        }
        binding.validationResult.text = messageSuffix.trim()
    }

    private fun setClipboard(text: String) {
        val currentActivity = activity ?: return
        val clipboard = currentActivity.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText(getString(R.string.app_name), text)
        clipboard.setPrimaryClip(clip)
    }

    private fun generate() {
        var generatedValue = ""
        var generatedDescription: String? = null
        when (binding.documentsSpinner.selectedItemPosition) {
            0 -> generatedValue = CPF.getCPF(true)
            1 -> generatedValue = CNPJ.getCNPJ(true)
            2 -> generatedValue = PIS.getPIS(true)
            3 -> {
                generatedValue = CreditCard.generateCreditCardNumber()
                generatedDescription = CreditCard.getCardID(generatedValue)?.let {
                    getString(R.string.descricao_credito, it.name)
                }
            }
            4 -> generatedValue = RENAVAM.getRENAVAM()
        }

        binding.generatedValue.text = generatedValue
        if (generatedDescription == null) {
            binding.generatedDescription.visibility = View.GONE
        } else {
            binding.generatedDescription.visibility = View.VISIBLE
            binding.generatedDescription.text = generatedDescription
        }
        binding.generatedValueLayout.visibility = View.VISIBLE
    }

    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        checkRadioGroup()
    }

    private class FragmentViews(root: View) {
        val documentsSpinner: Spinner = root.findViewById(R.id.sp_docs)
        val documentTypeGroup: RadioGroup = root.findViewById(R.id.radio_group)
        val validateLayout: View = root.findViewById(R.id.layout_validar)
        val generateLayout: View = root.findViewById(R.id.layout_gerar)
        val documentValue: EditText = root.findViewById(R.id.edit_valor_doc)
        val validateButton: View = root.findViewById(R.id.bt_validar)
        val generateButton: View = root.findViewById(R.id.bt_gerar)
        val validationResult: TextView = root.findViewById(R.id.tv_resultado_validar)
        val generatedValueLayout: View = root.findViewById(R.id.layout_valor_gerado)
        val generatedValue: TextView = root.findViewById(R.id.tv_resultado_gerar)
        val generatedDescription: TextView = root.findViewById(R.id.tv_resultado_gerar_2)
    }
}
