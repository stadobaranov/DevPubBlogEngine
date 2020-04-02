package devpub.blogengine.model.validation

import devpub.blogengine.model.entity.Tag
import devpub.blogengine.model.entity.TagName
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class TagNameSetValidator: ConstraintValidator<TagNames, Set<TagName>> {
    override fun isValid(tagNames: Set<TagName>?, context: ConstraintValidatorContext): Boolean {
        if(tagNames == null) {
            return true
        }

        tagNames.forEach {
            if(it.value.length > Tag.MAX_NAME_LENGTH) {
                return false
            }
        }

        return true
    }
}