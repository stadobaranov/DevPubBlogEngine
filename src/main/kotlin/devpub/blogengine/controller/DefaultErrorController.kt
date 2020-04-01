package devpub.blogengine.controller

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest

private const val ERROR_PATH = "error"

@Controller
open class DefaultErrorController: ErrorController {
    override fun getErrorPath(): String {
        return ERROR_PATH
    }

    @RequestMapping(ERROR_PATH)
    open fun error(httpRequest: HttpServletRequest): ResponseEntity<Any> {
        val statusCode = httpRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)

        val status = when(statusCode) {
            null -> HttpStatus.INTERNAL_SERVER_ERROR
            else -> parseHttpStatus(statusCode.toString())
        }

        return ResponseEntity.status(status).build()
    }

    private fun parseHttpStatus(statusCode: String): HttpStatus {
        try {
            return HttpStatus.valueOf(statusCode.toInt())
        }
        catch(exception: NumberFormatException) {
            return HttpStatus.INTERNAL_SERVER_ERROR
        }
    }
}