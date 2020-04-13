package devpub.blogengine.service

import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MaxUploadSizeExceededException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

typealias MaxUploadSizeExceededExceptionHandler =
    (MaxUploadSizeExceededException, HttpServletRequest, HttpServletResponse) -> ResponseEntity<*>?

interface MaxUploadSizeExceededExceptionHandlingService {
    fun registerSimpleHandler(
        method: String,
        uri: String,
        handler: (MaxUploadSizeExceededException) -> ResponseEntity<*>
    ) {
        return registerHandler {
            exception, request, _ ->
                if(method.equals(request.method, true) && uri.equals(request.requestURI, true)) {
                    return@registerHandler handler(exception)
                }

                return@registerHandler null
        }
    }

    fun registerHandler(handler: MaxUploadSizeExceededExceptionHandler)

    fun handle(
        exception: MaxUploadSizeExceededException,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<*>?
}