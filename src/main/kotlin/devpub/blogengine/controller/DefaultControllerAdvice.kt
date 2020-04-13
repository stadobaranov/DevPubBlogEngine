package devpub.blogengine.controller

import devpub.blogengine.ApplicationMessages
import devpub.blogengine.model.MessageResponse
import devpub.blogengine.service.MaxUploadSizeExceededExceptionHandlingService
import devpub.blogengine.service.exception.GlobalSettingValueConversionException
import devpub.blogengine.service.exception.ModelIntegrityException
import devpub.blogengine.service.exception.ModelNotFoundException
import devpub.blogengine.service.exception.UnauthorizedException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.StringTrimmerEditor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.multipart.MaxUploadSizeExceededException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val logger = LoggerFactory.getLogger(DefaultControllerAdvice::class.java)

@ControllerAdvice
open class DefaultControllerAdvice @Autowired constructor(
    private val maxUploadSizeExceededExceptionHandlingService: MaxUploadSizeExceededExceptionHandlingService
) {
    @InitBinder
    open fun initDataBinder(dataBinder: WebDataBinder) {
        dataBinder.registerCustomEditor(String::class.java, StringTrimmerEditor(false))
    }

    @ExceptionHandler(UnauthorizedException::class)
    open fun handleUnauthorizedException(): Any {
        return buildEmptyResponse(HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(ModelIntegrityException::class)
    open fun handleModelIntegrityException(): Any {
        return buildResponseForBadRequest()
    }

    @ExceptionHandler(ModelNotFoundException::class)
    open fun handleModelNotFoundException(): Any {
        return buildEmptyResponse(HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(GlobalSettingValueConversionException::class)
    open fun handleGlobalSettingValueConversionException(): Any {
        return buildResponseForBadRequest()
    }

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    open fun handleMaxUploadSizeExceededException(
        exception: MaxUploadSizeExceededException,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Any {
        return maxUploadSizeExceededExceptionHandlingService.handle(exception, request, response) ?: buildResponseForBadRequest()
    }

    private fun buildResponseForBadRequest(): ResponseEntity<Any> {
        return ResponseEntity.badRequest().body(MessageResponse(ApplicationMessages.BAD_REQUEST))
    }

    private fun buildEmptyResponse(status: HttpStatus): ResponseEntity<Any> {
        return ResponseEntity.status(status).build()
    }
}