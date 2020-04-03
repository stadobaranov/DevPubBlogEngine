package devpub.blogengine.service

import org.springframework.stereotype.Service
import java.security.SecureRandom

@Service
open class RandomStringGeneratorImpl: RandomStringGenerator {
    private val random = SecureRandom()

    override fun generate(length: Int, symbols: String): String {
        val builder = StringBuilder(length)

        for(i in 1..length) {
            builder.append(symbols[random.nextInt(symbols.length)])
        }

        return builder.toString()
    }
}