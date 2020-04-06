package devpub.blogengine.model

import devpub.blogengine.ApplicationMessages
import javax.validation.constraints.NotNull

class SendUserResetCodeRequest {
    @NotNull(message = ApplicationMessages.FIELD_IS_REQUIRED)
    lateinit var email: String
}