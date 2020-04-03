package devpub.blogengine.service

import kotlin.math.roundToInt

interface RandomStringGenerator {
    companion object {
        const val DEFAULT_SYMBOLS = "abcdefghijklmnopqrstuvwxyz0123456789"
    }

    fun generate(min: Int, max: Int, symbols: String = DEFAULT_SYMBOLS): String {
        return generate(min + ((max - min) * Math.random()).roundToInt(), symbols)
    }

    fun generate(length: Int, symbols: String = DEFAULT_SYMBOLS): String
}