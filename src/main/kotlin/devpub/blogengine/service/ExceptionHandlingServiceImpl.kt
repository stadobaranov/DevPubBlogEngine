package devpub.blogengine.service

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
open class ExceptionHandlingServiceImpl: ExceptionHandlingService {
    private val handlers = ConcurrentHashMap<String, ExceptionHandler>()

    override fun register(uri: String, handler: ExceptionHandler) {
        handlers[uri] = handler
    }

    override fun handle(exception: Exception, request: HttpServletRequest, response: HttpServletResponse): Any? {
        val handler = handlers[request.requestURI]

        if(handler == null) {
            return null
        }

        return handler(exception, request, response)
    }
}