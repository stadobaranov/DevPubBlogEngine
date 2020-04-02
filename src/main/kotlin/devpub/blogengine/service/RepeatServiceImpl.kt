package devpub.blogengine.service

import devpub.blogengine.service.exception.RepeatException
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import kotlin.math.roundToLong

@Service
open class RepeatServiceImpl: RepeatService {
    private var executionStatusHolder = ThreadLocal<ExecutionStatus>()

    override fun <T> executeRepeatedly(
        maxAttempts: Int,
        minDelay: Long,
        maxDelay: Long,
        execution: () -> T
    ): T {
        val firstCall = executionStatusHolder.get() == null

        if(firstCall) {
            executionStatusHolder.set(ExecutionStatus.EXECUTING)
        }

        try {
            var attempts = if(maxAttempts < 1) 1 else maxAttempts

            while(true) {
                attempts--

                try {
                    return execution()
                }
                catch(exception: RepeatException) {
                    if(!firstCall) {
                        throw exception
                    }
                    else if(attempts == 0) {
                        throw exception.cause
                    }

                    delay(minDelay, maxDelay)
                }
            }
        }
        finally {
            if(firstCall) {
                executionStatusHolder.remove()
            }
        }
    }

    private fun delay(minDelay: Long, maxDelay: Long) {
        var remaining = minDelay + ((maxDelay - minDelay) * Math.random()).roundToLong()

        if(remaining > 0) {
            var startTime = System.nanoTime()

            while(true) {
                try {
                    Thread.sleep(remaining)
                    break
                }
                catch(exception: InterruptedException) {
                    val time = System.nanoTime()
                    remaining -= TimeUnit.NANOSECONDS.toMillis(time - startTime)
                    startTime = time
                }
            }
        }
    }
}

private enum class ExecutionStatus {
    EXECUTING
}