package devpub.blogengine.model.converter

import org.springframework.core.convert.converter.Converter
import kotlin.reflect.KClass

open class StringToEnumConverter<T: Enum<T>> protected constructor(
    type: KClass<out T>,
    defaultValue: T? = null
): Converter<String, T> {
    private val values = type.java.enumConstants.associateByTo(hashMapOf()) { it.name.toUpperCase() }
    private val defaultValue = defaultValue

    override fun convert(name: String): T? {
        return values[name.toUpperCase()] ?: defaultValue
    }
}