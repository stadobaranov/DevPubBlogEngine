package devpub.blogengine.model.validation

import devpub.blogengine.util.FileUtils
import org.springframework.web.multipart.MultipartFile
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

open class ImageValidator: ConstraintValidator<Image, MultipartFile> {
    private val availableExtensions = hashSetOf(
        "png", "jpg", "jpeg", "gif"
    )

    override fun isValid(imageFile: MultipartFile?, context: ConstraintValidatorContext): Boolean {
        if(imageFile == null) {
            return true
        }

        val originalFileName = imageFile.originalFilename

        if(originalFileName != null) {
            val extension = FileUtils.getExtension(originalFileName)
            return extension != null && extension.toLowerCase() in availableExtensions
        }

        return false
    }
}