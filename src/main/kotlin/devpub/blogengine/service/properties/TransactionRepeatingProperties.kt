package devpub.blogengine.service.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import kotlin.math.max
import kotlin.math.min

@ConfigurationProperties(prefix = "blog-engine.transaction-repeating")
@ConstructorBinding
open class TransactionRepeatingProperties(maxAttempts: Int, minDelay: Long, maxDelay: Long) {
    val maxAttempts = maxAttempts
    val minDelay = min(minDelay, maxDelay)
    val maxDelay = max(minDelay, maxDelay)
}