package devpub.blogengine.model.validation

import devpub.blogengine.ApplicationMessages
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [TagNameSetValidator::class])
annotation class TagNames(
    val message: String = ApplicationMessages.INVALID_TAG_NAMES,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)