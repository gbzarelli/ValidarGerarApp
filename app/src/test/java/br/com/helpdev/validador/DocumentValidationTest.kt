package br.com.helpdev.validador

import br.com.helpdev.libvalidadores.CNPJ
import br.com.helpdev.libvalidadores.CPF
import br.com.helpdev.libvalidadores.CreditCard
import br.com.helpdev.libvalidadores.PIS
import br.com.helpdev.libvalidadores.RENAVAM
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DocumentValidationTest {

    @Test
    fun validatesCpfSamples() {
        assertTrue(CPF.validateCPF("37790288830"))
        assertFalse(CPF.validateCPF("37791288830"))
    }

    @Test
    fun validatesCnpjSamples() {
        assertTrue(CNPJ.validateCNPJ("11222333000181"))
        assertFalse(CNPJ.validateCNPJ("11222333000180"))
    }

    @Test
    fun validatesPisSamples() {
        assertTrue(PIS.validatePIS("12044500606"))
        assertFalse(PIS.validatePIS("12044500607"))
    }

    @Test
    fun validatesCreditCardSamples() {
        assertTrue(CreditCard.isValidCreditCardNumber("4111111111111111"))
        assertFalse(CreditCard.isValidCreditCardNumber("4111111111111112"))
    }

    @Test
    fun validatesRenavamSamples() {
        assertTrue(RENAVAM.validateRENAVAM("12345678900"))
        assertFalse(RENAVAM.validateRENAVAM("12345678901"))
    }
}
