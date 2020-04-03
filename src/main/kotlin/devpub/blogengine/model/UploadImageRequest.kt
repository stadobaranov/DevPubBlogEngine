package devpub.blogengine.model

import devpub.blogengine.ApplicationMessages
import devpub.blogengine.model.validation.Image
import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.NotNull

class UploadImageRequest {
    @NotNull(message = ApplicationMessages.FIELD_IS_REQUIRED)
    @Image
    lateinit var image: MultipartFile
}