package devpub.blogengine.controller

import devpub.blogengine.ApplicationMessages
import devpub.blogengine.model.MessageResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.TypeMismatchException
import org.springframework.beans.propertyeditors.StringTrimmerEditor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.multipart.support.MissingServletRequestPartException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val logger = LoggerFactory.getLogger(DefaultControllerAdvice::class.java)

@ControllerAdvice
open class DefaultControllerAdvice {
    @InitBinder
    open fun initDataBinder(dataBinder: WebDataBinder) {
        dataBinder.registerCustomEditor(String::class.java, StringTrimmerEditor(false))
    }

    @ExceptionHandler(Exception::class)
    open fun handleException(exception: Exception, request: HttpServletRequest, response: HttpServletResponse): Any {
        return when(exception) {
            is BindException -> buildResponseForBadRequest()
            is MethodArgumentNotValidException -> buildResponseForBadRequest()
            is HttpMessageNotReadableException -> buildResponseForBadRequest()
            is MissingServletRequestParameterException -> buildResponseForBadRequest()
            is MissingServletRequestPartException -> buildResponseForBadRequest()
            is TypeMismatchException -> buildResponseForBadRequest()
            is HttpRequestMethodNotSupportedException -> buildResponseForMethodNotAllowed()
            is HttpMediaTypeNotAcceptableException -> buildResponseForNotAcceptable()
            is HttpMediaTypeNotSupportedException -> buildResponseForUnsupportedMediaType()
            else -> {
                logger.error("Поймано необработанное исключение", exception)
                buildEmptyResponse(HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }
    }

    private fun buildResponseForBadRequest(): ResponseEntity<Any> {
        return ResponseEntity.badRequest()
                             .body(MessageResponse(ApplicationMessages.BAD_REQUEST))
    }

    private fun buildResponseForMethodNotAllowed(): ResponseEntity<Any> {
        return buildEmptyResponse(HttpStatus.METHOD_NOT_ALLOWED)
    }

    private fun buildResponseForUnsupportedMediaType(): ResponseEntity<Any> {
        return buildEmptyResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    }

    private fun buildResponseForNotAcceptable(): ResponseEntity<Any> {
        return buildEmptyResponse(HttpStatus.NOT_ACCEPTABLE)
    }

    private fun buildEmptyResponse(status: HttpStatus): ResponseEntity<Any> {
        return ResponseEntity.status(status).build()
    }
}