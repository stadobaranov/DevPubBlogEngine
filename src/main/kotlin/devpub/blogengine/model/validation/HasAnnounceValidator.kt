package devpub.blogengine.model.validation

import devpub.blogengine.service.AnnounceExtractor
import devpub.blogengine.service.exception.AnnounceExtractionException
import org.springframework.beans.factory.annotation.Autowired
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

open class HasAnnounceValidator @Autowired constructor(
    private val announceExtractor: AnnounceExtractor
): ConstraintValidator<HasAnnounce, String> {
    override fun isValid(text: String?, context: ConstraintValidatorContext): Boolean {
        if(text == null) {
            return false
        }

        try {
            announceExtractor.extract(text)
        }
        catch(exception: AnnounceExtractionException) {
            return false
        }

        return true
    }
}