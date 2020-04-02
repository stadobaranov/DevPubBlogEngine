package devpub.blogengine.service

import devpub.blogengine.model.ValidationErrorsResponse
import org.springframework.validation.FieldError
import kotlin.reflect.KClass

interface ValidationErrorsResponseMaker {
    fun make(requestType: KClass<out Any>, fieldErrors: List<FieldError>): ValidationErrorsResponse

    fun make(requestType: KClass<out Any>, fieldName: String, fieldError: String): ValidationErrorsResponse
}