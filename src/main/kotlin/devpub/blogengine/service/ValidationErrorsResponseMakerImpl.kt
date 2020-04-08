package devpub.blogengine.service

import com.fasterxml.jackson.annotation.JsonProperty
import devpub.blogengine.model.ValidationErrorsResponse
import org.springframework.stereotype.Service
import org.springframework.validation.FieldError
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.superclasses
import kotlin.reflect.jvm.javaField

@Service
open class ValidationErrorsResponseMakerImpl: ValidationErrorsResponseMaker {
    private val requestParameterMappers = ConcurrentHashMap<KClass<*>, RequestParameterMapper>()

    override fun make(requestType: KClass<*>, fieldErrors: List<FieldError>): ValidationErrorsResponse {
        val mapper = getMapper(requestType)

        return ValidationErrorsResponse(
            fieldErrors.associate { mapper.map(it.field) to it.defaultMessage }
        )
    }

    override fun make(requestType: KClass<*>, fieldName: String, fieldError: String): ValidationErrorsResponse {
        val mapper = getMapper(requestType)

        return ValidationErrorsResponse(
            mapper.map(fieldName), fieldError
        )
    }

    private fun getMapper(requestType: KClass<*>): RequestParameterMapper {
        var mapper = requestParameterMappers[requestType]

        if(mapper == null) {
            val newMapper = createRequestParameterMapper(requestType)
            val oldMapper = requestParameterMappers.putIfAbsent(requestType, newMapper)
            mapper = oldMapper ?: newMapper
        }

        return mapper
    }

    private fun createRequestParameterMapper(requestType: KClass<*>): RequestParameterMapper {
        val fieldNameOverrides = hashMapOf<String, String>()
        requestType.superclasses.forEach { fillFieldNameOverrides(it, fieldNameOverrides) }
        fillFieldNameOverrides(requestType, fieldNameOverrides)
        return RequestParameterMapper(fieldNameOverrides)
    }

    private fun fillFieldNameOverrides(type: KClass<*>, fieldNameOverrides: MutableMap<String, String>) {
        type.declaredMemberProperties.forEach {
            val javaField = it.javaField

            if(javaField != null) {
                val name = findJsonPropertyValueIn(javaField.annotations.iterator())

                if(name != null) {
                    fieldNameOverrides[it.name] = name
                    return@forEach
                }
            }

            val name = findJsonPropertyValueIn(it.getter.annotations.listIterator())

            if(name != null) {
                fieldNameOverrides[it.name] = name
            }
        }
    }

    private fun findJsonPropertyValueIn(iterator: Iterator<Annotation>): String? {
        while(iterator.hasNext()) {
            val annotation = iterator.next()

            if(annotation is JsonProperty) {
                return annotation.value
            }
        }

        return null
    }
}

private class RequestParameterMapper(
     val fieldNameOverrides: Map<String, String>
) {
    fun map(fieldName: String): String {
        return fieldNameOverrides[fieldName] ?: fieldName
    }
}