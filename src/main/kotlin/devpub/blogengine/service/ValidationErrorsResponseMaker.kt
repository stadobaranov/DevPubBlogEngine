package devpub.blogengine.service

import devpub.blogengine.model.ValidationErrorsResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

interface ValidationErrorsResponseMaker {
    fun makeEntity(
        requestType: KClass<*>,
        fieldErrors: List<FieldError>,
        status: HttpStatus = HttpStatus.BAD_REQUEST
    ): ResponseEntity<ValidationErrorsResponse> {
        return ResponseEntity.status(status).body(make(requestType, fieldErrors))
    }

    fun make(requestType: KClass<*>, fieldErrors: List<FieldError>): ValidationErrorsResponse

    fun <R: Any> makeEntity(
        requestType: KClass<R>,
        field: KProperty1<R, *>,
        fieldError: String,
        status: HttpStatus = HttpStatus.BAD_REQUEST
    ): ResponseEntity<ValidationErrorsResponse> {
        return ResponseEntity.status(status).body(make(requestType, field, fieldError))
    }

    fun <R: Any> make(requestType: KClass<R>, field: KProperty1<R, *>, fieldError: String): ValidationErrorsResponse
}