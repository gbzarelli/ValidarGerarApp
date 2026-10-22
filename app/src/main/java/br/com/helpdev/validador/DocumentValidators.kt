package br.com.helpdev.validador

/** Offline format and check-digit validation only; no registry lookup or document generation. */
internal object DocumentValidators {
    private val separators = Regex("[./\\-\\s]")
    private val pisWeights = intArrayOf(3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
    private val renavamWeights = intArrayOf(2, 3, 4, 5, 6, 7, 8, 9, 2, 3)

    fun isCpfValid(value: String): Boolean {
        val digits = normalizeNumeric(value) ?: return false
        if (digits.length != 11 || digits.all { it == digits.first() }) return false
        val firstDigit = modulo11Digit(digits.take(9), 10)
        val secondDigit = modulo11Digit(digits.take(10), 11)
        return digits[9].digitToInt() == firstDigit && digits[10].digitToInt() == secondDigit
    }

    fun isPisValid(value: String): Boolean {
        val digits = normalizeNumeric(value) ?: return false
        if (digits.length != 11 || digits.all { it == digits.first() }) return false
        val sum = digits.take(10).indices.sumOf { index ->
            digits[index].digitToInt() * pisWeights[index]
        }
        val candidate = 11 - (sum % 11)
        val checkDigit = if (candidate == 10 || candidate == 11) 0 else candidate
        return digits[10].digitToInt() == checkDigit
    }

    fun isRenavamValid(value: String): Boolean {
        var digits = normalizeNumeric(value) ?: return false
        if (digits.length == 9) digits = "00$digits"
        if (digits.length != 11 || digits.all { it == digits.first() }) return false

        val reversedBase = digits.take(10).reversed()
        val sum = reversedBase.indices.sumOf { index ->
            reversedBase[index].digitToInt() * renavamWeights[index]
        }
        val candidate = 11 - (sum % 11)
        val checkDigit = if (candidate >= 10) 0 else candidate
        return digits.last().digitToInt() == checkDigit
    }

    private fun normalizeNumeric(value: String): String? {
        val normalized = value.trim().replace(separators, "")
        return normalized.takeIf { it.isNotEmpty() && it.all { character -> character in '0'..'9' } }
    }

    private fun modulo11Digit(value: String, initialWeight: Int): Int {
        val sum = value.indices.sumOf { index ->
            value[index].digitToInt() * (initialWeight - index)
        }
        val remainder = sum % 11
        return if (remainder < 2) 0 else 11 - remainder
    }
}
