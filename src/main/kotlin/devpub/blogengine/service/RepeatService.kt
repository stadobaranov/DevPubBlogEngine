package devpub.blogengine.service

import devpub.blogengine.service.exception.RepeatException
import kotlin.reflect.KClass

fun repeat(cause: Exception): Nothing {
    throw RepeatException(cause)
}

fun <T> repeatIfThrown(exceptionType: KClass<out Exception> = Exception::class, block: () -> T): T {
    try {
        return block()
    }
    catch(exception: Exception) {
        if(exceptionType.isInstance(exception)) {
            repeat(exception)
        }

        throw exception
    }
}

interface RepeatService {
    fun <T> executeRepeatedly(maxAttempts: Int, minDelay: Long, maxDelay: Long, execution: () -> T): T
}