package br.com.helpdev.validador

/** Checks only CNPJ structure and check digits; it does not query an official registry. */
internal object CnpjValidator {
    private val separators = Regex("[./\\-\\s]")
    private val firstDigitWeights = intArrayOf(5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
    private val secondDigitWeights = intArrayOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)

    fun isValid(value: String): Boolean {
        val normalized = value.uppercase().replace(separators, "")
        if (normalized.length != 14) return false
        if (normalized.take(12).any { it !in '0'..'9' && it !in 'A'..'Z' }) return false
        if (normalized.takeLast(2).any { it !in '0'..'9' }) return false
        if (normalized.all { it == normalized.first() }) return false

        val firstDigit = calculateDigit(normalized.take(12), firstDigitWeights)
        val secondDigit = calculateDigit(normalized.take(13), secondDigitWeights)
        return normalized[12].digitToInt() == firstDigit && normalized[13].digitToInt() == secondDigit
    }

    private fun calculateDigit(value: String, weights: IntArray): Int {
        var sum = 0
        value.indices.forEach { index ->
            sum += (value[index].code - 48) * weights[index]
        }
        val remainder = sum % 11
        return if (remainder < 2) 0 else 11 - remainder
    }
}
