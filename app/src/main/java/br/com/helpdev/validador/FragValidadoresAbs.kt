package br.com.helpdev.validador

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.core.text.HtmlCompat

abstract class FragValidadoresAbs : Fragment(), View.OnClickListener {

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
        binding.validateButton.setOnClickListener(this)
        binding.officialSources.text = HtmlCompat.fromHtml(
            getString(R.string.fontes_oficiais),
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        binding.officialSources.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onDestroyView() {
        binding.validateButton.setOnClickListener(null)
        binding.officialSources.movementMethod = null
        views = null
        super.onDestroyView()
    }

    override fun onClick(view: View) {
        hideKeyboard()
        if (view.id == R.id.bt_validar) validate()
    }

    private fun hideKeyboard() {
        val currentActivity = activity ?: return
        val currentFocus = currentActivity.currentFocus ?: return
        val inputMethodManager = currentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }

    private fun validate() {
        val value = binding.documentValue.text.toString().trim()
        if (value.isEmpty()) {
            binding.documentValue.error = getString(R.string.entre_com_valor)
            return
        }

        val isValid = when (binding.documentsSpinner.selectedItemPosition) {
            0 -> DocumentValidators.isCpfValid(value)
            1 -> CnpjValidator.isValid(value)
            2 -> DocumentValidators.isPisValid(value)
            3 -> DocumentValidators.isRenavamValid(value)
            else -> false
        }

        binding.validationResult.setTextColor(if (isValid) {
            resources.getColor(R.color.colorPrimaryDark, requireContext().theme)
        } else {
            Color.RED
        })
        binding.validationResult.setText(if (isValid) R.string.valor_valido else R.string.valor_invalido)
    }

    private class FragmentViews(root: View) {
        val documentsSpinner: Spinner = root.findViewById(R.id.sp_docs)
        val documentValue: EditText = root.findViewById(R.id.edit_valor_doc)
        val validateButton: View = root.findViewById(R.id.bt_validar)
        val validationResult: TextView = root.findViewById(R.id.tv_resultado_validar)
        val officialSources: TextView = root.findViewById(R.id.tv_fontes_oficiais)
    }
}
