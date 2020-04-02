package devpub.blogengine.model.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [MaxLengthValidator::class])
annotation class MaxLength(
    val value: Int,
    val trimmed: Boolean = true,
    val message: String,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)