package br.com.helpdev.validador;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import br.com.helpdev.libvalidadores.CNPJ;
import br.com.helpdev.libvalidadores.CPF;
import br.com.helpdev.libvalidadores.CreditCard;
import br.com.helpdev.libvalidadores.PIS;
import br.com.helpdev.libvalidadores.RENAVAM;

/**
 * Created by demantoide on 09/11/16.
 */

public abstract class FragValidadoresAbs extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private RadioGroup radioGroup;
    private Spinner spinner;
    private View layoutValidar, layoutGerar, layoutValorGerado;
    private EditText documento;
    private TextView resultadoGerar, resultadoValidar, resultadoGerar2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_validadores, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutValidar = view.findViewById(R.id.layout_validar);
        layoutGerar = view.findViewById(R.id.layout_gerar);
        layoutValorGerado = view.findViewById(R.id.layout_valor_gerado);

        resultadoGerar = (TextView) view.findViewById(R.id.tv_resultado_gerar);
        resultadoGerar2 = (TextView) view.findViewById(R.id.tv_resultado_gerar_2);
        resultadoValidar = (TextView) view.findViewById(R.id.tv_resultado_validar);

        spinner = (Spinner) view.findViewById(R.id.sp_docs);
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        documento = (EditText) view.findViewById(R.id.edit_valor_doc);

        view.findViewById(R.id.bt_gerar).setOnClickListener(this);
        view.findViewById(R.id.bt_validar).setOnClickListener(this);
        view.findViewById(R.id.layout_valor_gerado).setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(this);
        if (savedInstanceState == null) {
            checkRadioGroup();
        }
    }

    protected void checkRadioGroup() {
        hideKeyboard();
        int checkedId = radioGroup.getCheckedRadioButtonId();
        if (checkedId == R.id.rb_gerar) {
            layoutGerar.setVisibility(View.VISIBLE);
            disableLayoutValidar();
        } else if (checkedId == R.id.rb_validar) {
            layoutValidar.setVisibility(View.VISIBLE);
            disableLayoutGerar();
        }
    }

    private void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Throwable t) {
        }
    }

    private void disableLayoutValidar() {
        layoutValidar.setVisibility(View.GONE);
        resultadoValidar.setText("");
    }

    private void disableLayoutGerar() {
        layoutGerar.setVisibility(View.GONE);
        layoutValorGerado.setVisibility(View.GONE);
        resultadoGerar.setText("");

    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        if (v.getId() == R.id.bt_gerar) {
            gerar();
        } else if (v.getId() == R.id.bt_validar) {
            validar();
        } else if (v.getId() == R.id.layout_valor_gerado) {
            Snackbar.make(layoutValorGerado, R.string.valor_copiado, Snackbar.LENGTH_LONG).show();
            setClipboard(resultadoGerar.getText().toString());
        }
    }

    private void validar() {
        String valorDoc = documento.getText().toString().trim();
        if (valorDoc.isEmpty()) {
            documento.setError(getString(R.string.entre_com_valor));
            return;
        }
        String addMensagem = "\n";
        boolean validado = false;
        switch (spinner.getSelectedItemPosition()) {
            case 0://CPF
                validado = CPF.validateCPF(valorDoc);
                break;
            case 1://CNPJ
                validado = CNPJ.validateCNPJ(valorDoc);
                break;
            case 2://PIS
                validado = PIS.validatePIS(valorDoc);
                break;
            case 3://CARTÃO DE CREDITO
                validado = CreditCard.isValidCreditCardNumber(valorDoc);
                if (validado) {
                    CreditCard.EnumCreditCard cardID = CreditCard.getCardID(valorDoc);
                    if (cardID != null) {
                        addMensagem += getString(R.string.descricao_credito, cardID.name());
                    }
                }
                break;
            case 4://renavam
                validado = RENAVAM.validateRENAVAM(valorDoc);
                break;
        }
        resultadoValidar.setVisibility(View.VISIBLE);
        if (validado) {
            resultadoValidar.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            addMensagem = getString(R.string.valor_valido) + addMensagem;
        } else {
            resultadoValidar.setTextColor(Color.RED);
            addMensagem = getString(R.string.valor_invalido) + addMensagem;
        }
        resultadoValidar.setText(addMensagem);
    }

    private void setClipboard(String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText(getString(R.string.app_name), text);
        clipboard.setPrimaryClip(clip);
    }

    /**
     * <item>CPF</item>
     * <item>CNPJ</item>
     * <item>PIS</item>
     * <item>CARTÃO DE CREDITO</item>
     */
    protected void gerar() {
        String valorGerado = "";
        String valorGerado2 = null;
        switch (spinner.getSelectedItemPosition()) {
            case 0://CPF
                valorGerado = CPF.getCPF(true);
                break;
            case 1://CNPJ
                valorGerado = CNPJ.getCNPJ(true);
                break;
            case 2://PIS
                valorGerado = PIS.getPIS(true);
                break;
            case 3://CARTÃO DE CREDITO
                valorGerado = CreditCard.generateCreditCardNumber();
                CreditCard.EnumCreditCard cardID = CreditCard.getCardID(valorGerado);
                if (cardID != null) {
                    valorGerado2 = getString(R.string.descricao_credito, cardID.name());
                }
                break;
            case 4://renavam
                valorGerado = RENAVAM.getRENAVAM();
                break;
        }

        resultadoGerar.setText(valorGerado);
        if (valorGerado2 == null) {
            resultadoGerar2.setVisibility(View.GONE);
        } else {
            resultadoGerar2.setVisibility(View.VISIBLE);
            resultadoGerar2.setText(valorGerado2);
        }
        layoutValorGerado.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        checkRadioGroup();
    }
}
