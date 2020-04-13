package devpub.blogengine.controller.validation

import devpub.blogengine.model.ValidationErrorsResponse
import devpub.blogengine.service.ValidationErrorsResponseMaker
import org.springframework.http.ResponseEntity
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

class ExceptionToValidationErrorsMapper<R: Any>(
    private val validationErrorsResponseMaker: ValidationErrorsResponseMaker,
    private val requestType: KClass<R>
) {
    private val exceptionTypeToFields = arrayListOf<Pair<KClass<out Exception>, KProperty1<R, *>>>()

    fun map(mapping: Pair<KClass<out Exception>, KProperty1<R, *>>): ExceptionToValidationErrorsMapper<R> {
        exceptionTypeToFields.add(mapping)
        return this
    }

    fun makeResponse(exception: Exception): ValidationErrorsResponse {
        return makeResponse(exception, validationErrorsResponseMaker::make)
    }

    fun makeResponseEntity(exception: Exception): ResponseEntity<ValidationErrorsResponse> {
        return makeResponse(exception) {
            requestType, fieldName, fieldError ->
                return@makeResponse validationErrorsResponseMaker.makeEntity(requestType, fieldName, fieldError)
        }
    }

    private fun <R> makeResponse(
        exception: Exception,
        maker: (requestType: KClass<*>, fieldName: String, fieldError: String) -> R
    ): R {
        exceptionTypeToFields.forEach {
            if(it.first.isInstance(exception)) {
                return maker(requestType, it.second.name, exception.message!!)
            }
        }

        throw exception
    }
}

fun <R: Any> ValidationErrorsResponseMaker.buildMapper(requestType: KClass<R>): ExceptionToValidationErrorsMapper<R> {
    return ExceptionToValidationErrorsMapper(this, requestType)
}