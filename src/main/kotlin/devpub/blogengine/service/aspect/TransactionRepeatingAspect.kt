package devpub.blogengine.service.aspect

import devpub.blogengine.service.RepeatService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import kotlin.math.max
import kotlin.math.min

@Aspect
@Component
open class TransactionRepeatingAspect @Autowired constructor(
    @Value("\${blog-engine.transaction-repeating.max-attempts}") maxAttempts: Int,
    @Value("\${blog-engine.transaction-repeating.min-delay}") minDelay: Long,
    @Value("\${blog-engine.transaction-repeating.max-delay}") maxDelay: Long,
    repeatService: RepeatService
): Ordered {
    private val maxAttempts = maxAttempts
    private val minDelay = min(minDelay, maxDelay)
    private val maxDelay = max(minDelay, maxDelay)
    private val repeatService = repeatService

    override fun getOrder(): Int {
        return AspectOrders.TRANSACTION_REPEATING_ORDER
    }

    @Around("@annotation(transactional)")
    open fun executeRepeatedly(joinPoint: ProceedingJoinPoint, transactional: Transactional): Any? {
        return repeatService.executeRepeatedly(maxAttempts, minDelay, maxDelay) { joinPoint.proceed() }
    }
}