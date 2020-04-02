package devpub.blogengine.model.validation

import devpub.blogengine.model.entity.PersistentUtils
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

open class EntityIdValidator: ConstraintValidator<EntityId, Int> {
    override fun isValid(id: Int?, context: ConstraintValidatorContext): Boolean {
        return id == null || PersistentUtils.isValidId(id)
    }
}