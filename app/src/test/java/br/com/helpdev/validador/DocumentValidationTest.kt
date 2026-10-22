package br.com.helpdev.validador

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DocumentValidationTest {

    @Test
    fun validatesCpfSamples() {
        assertTrue(DocumentValidators.isCpfValid("37790288830"))
        assertFalse(DocumentValidators.isCpfValid("37791288830"))
    }

    @Test
    fun validatesCnpjSamples() {
        assertTrue(CnpjValidator.isValid("11222333000181"))
        assertFalse(CnpjValidator.isValid("11222333000180"))
    }

    @Test
    fun validatesPisSamples() {
        assertTrue(DocumentValidators.isPisValid("12044500606"))
        assertFalse(DocumentValidators.isPisValid("12044500607"))
    }

    @Test
    fun validatesRenavamSamples() {
        assertTrue(DocumentValidators.isRenavamValid("12345678900"))
        assertFalse(DocumentValidators.isRenavamValid("12345678901"))
    }
}
