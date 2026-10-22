package br.com.helpdev.validador

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CnpjValidatorTest {

    @Test
    fun validatesExistingNumericCnpj() {
        assertTrue(CnpjValidator.isValid("11.222.333/0001-81"))
        assertFalse(CnpjValidator.isValid("11.222.333/0001-80"))
    }

    @Test
    fun validatesOfficialAlphanumericCnpjExample() {
        assertTrue(CnpjValidator.isValid("12.ABC.345/01DE-35"))
        assertFalse(CnpjValidator.isValid("12.ABC.345/01DE-36"))
    }

    @Test
    fun rejectsInvalidLengthAndCharacters() {
        assertFalse(CnpjValidator.isValid("12ABC34501DE3"))
        assertFalse(CnpjValidator.isValid("12ABC34501D!35"))
        assertFalse(CnpjValidator.isValid("AAAAAAAAAAAAAA"))
    }
}
