package devpub.blogengine.service

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

typealias ExceptionHandler = (Exception, HttpServletRequest, HttpServletResponse) -> Any?

interface ExceptionHandlingService {
    fun register(uri: String, handler: ExceptionHandler)

    fun handle(exception: Exception, request: HttpServletRequest, response: HttpServletResponse): Any?
}