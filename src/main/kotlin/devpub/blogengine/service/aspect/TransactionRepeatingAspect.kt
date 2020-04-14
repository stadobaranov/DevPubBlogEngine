package devpub.blogengine.service.aspect

import devpub.blogengine.service.RepeatService
import devpub.blogengine.service.properties.TransactionRepeatingProperties
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Aspect
@Component
open class TransactionRepeatingAspect @Autowired constructor(
    private val transactionRepeatingProperties: TransactionRepeatingProperties,
    private val repeatService: RepeatService
): Ordered {
    override fun getOrder(): Int {
        return AspectOrders.TRANSACTION_REPEATING_ORDER
    }

    @Around("@annotation(transactional)")
    open fun executeRepeatedly(joinPoint: ProceedingJoinPoint, transactional: Transactional): Any? {
        return repeatService.executeRepeatedly(
            transactionRepeatingProperties.maxAttempts,
            transactionRepeatingProperties.minDelay,
            transactionRepeatingProperties.maxDelay
        ) { joinPoint.proceed() }
    }
}