package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonProperty
import devpub.blogengine.ApplicationMessages
import devpub.blogengine.model.entity.User
import devpub.blogengine.model.validation.MaxLength
import devpub.blogengine.model.validation.MinLength
import javax.validation.constraints.NotNull

class ResetUserPasswordRequest: ProtectedByCaptchaRequest() {
    @JsonProperty("code")
    @NotNull(message = ApplicationMessages.FIELD_IS_REQUIRED)
    lateinit var resetCode: String

    @JsonProperty("password")
    @NotNull(message = ApplicationMessages.FIELD_IS_REQUIRED)
    @MinLength(
        value = User.MIN_PASSWORD_LENGTH,
        message = ApplicationMessages.INVALID_MIN_USER_PASSWORD_LENGTH
    )
    @MaxLength(
        value = User.MAX_PASSWORD_LENGTH,
        message = ApplicationMessages.INVALID_MAX_USER_PASSWORD_LENGTH
    )
    lateinit var newPassword: String
}