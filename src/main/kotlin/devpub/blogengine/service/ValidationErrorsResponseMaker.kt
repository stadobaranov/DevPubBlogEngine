package devpub.blogengine.service

import devpub.blogengine.model.ValidationErrorsResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import kotlin.reflect.KClass

interface ValidationErrorsResponseMaker {
    fun makeEntity(
        requestType: KClass<*>,
        fieldErrors: List<FieldError>,
        status: HttpStatus = HttpStatus.BAD_REQUEST
    ): ResponseEntity<ValidationErrorsResponse> {
        return ResponseEntity.status(status).body(make(requestType, fieldErrors))
    }

    fun make(requestType: KClass<*>, fieldErrors: List<FieldError>): ValidationErrorsResponse

    fun makeEntity(
        requestType: KClass<*>,
        fieldName: String,
        fieldError: String,
        status: HttpStatus = HttpStatus.BAD_REQUEST
    ): ResponseEntity<ValidationErrorsResponse> {
        return ResponseEntity.status(status).body(make(requestType, fieldName, fieldError))
    }

    fun make(requestType: KClass<*>, fieldName: String, fieldError: String): ValidationErrorsResponse
}