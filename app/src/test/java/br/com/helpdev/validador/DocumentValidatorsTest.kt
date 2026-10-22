package br.com.helpdev.validador

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DocumentValidatorsTest {

    @Test
    fun acceptsFormattedAndUnformattedCpf() {
        assertTrue(DocumentValidators.isCpfValid("37790288830"))
        assertTrue(DocumentValidators.isCpfValid("377.902.888-30"))
    }

    @Test
    fun rejectsInvalidCpfLengthsCharactersAndRepeatedDigits() {
        assertFalse(DocumentValidators.isCpfValid("3779028883"))
        assertFalse(DocumentValidators.isCpfValid("377.902.888-3A"))
        assertFalse(DocumentValidators.isCpfValid("11111111111"))
    }

    @Test
    fun acceptsNineAndElevenDigitRenavamFormats() {
        assertTrue(DocumentValidators.isRenavamValid("12345678900"))
        assertTrue(DocumentValidators.isRenavamValid("123456789"))
    }

    @Test
    fun rejectsInvalidPisAndRenavamInputs() {
        assertFalse(DocumentValidators.isPisValid("12044500607"))
        assertFalse(DocumentValidators.isPisValid("12044500A06"))
        assertFalse(DocumentValidators.isRenavamValid("12345678901"))
        assertFalse(DocumentValidators.isRenavamValid("12345678"))
    }
}
