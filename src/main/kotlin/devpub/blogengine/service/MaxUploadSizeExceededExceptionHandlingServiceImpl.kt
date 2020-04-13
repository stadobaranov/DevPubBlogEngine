package devpub.blogengine.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MaxUploadSizeExceededException
import java.util.concurrent.CopyOnWriteArrayList
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
open class MaxUploadSizeExceededExceptionHandlingServiceImpl: MaxUploadSizeExceededExceptionHandlingService {
    private val handlers = CopyOnWriteArrayList<MaxUploadSizeExceededExceptionHandler>()

    override fun registerHandler(handler: MaxUploadSizeExceededExceptionHandler) {
        handlers.add(handler)
    }

    override fun handle(
        exception: MaxUploadSizeExceededException,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<*>? {
        handlers.forEach {
            val result = it(exception, request, response)

            if(result != null) {
                return result
            }
        }

        return null
    }
}