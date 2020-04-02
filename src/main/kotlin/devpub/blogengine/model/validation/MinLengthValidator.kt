package devpub.blogengine.model.validation

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

open class MinLengthValidator: ConstraintValidator<MinLength, String> {
    private var length = 0
    private var trimmed = false

    override fun initialize(annotation: MinLength) {
        length = annotation.value
        trimmed = annotation.trimmed
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return value == null || (if(trimmed) value.trim() else value).length >= length
    }
}