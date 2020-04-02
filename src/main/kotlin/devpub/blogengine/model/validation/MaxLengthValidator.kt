package devpub.blogengine.model.validation

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

open class MaxLengthValidator: ConstraintValidator<MaxLength, String> {
    private var length = 0
    private var trimmed = true

    override fun initialize(annotation: MaxLength) {
        length = annotation.value
        trimmed = annotation.trimmed
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return value == null || (if(trimmed) value.trim() else value).length <= length
    }
}