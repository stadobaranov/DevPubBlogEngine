package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonProperty
import devpub.blogengine.ApplicationMessages
import devpub.blogengine.model.entity.User
import devpub.blogengine.model.validation.MaxLength
import devpub.blogengine.model.validation.MinLength
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class RegisterUserRequest: ProtectedByCaptchaRequest() {
    @NotBlank(message = ApplicationMessages.FIELD_IS_REQUIRED)
    @MaxLength(
        value = User.MAX_NAME_LENGTH,
        message = ApplicationMessages.INVALID_MAX_USER_NAME_LENGTH
    )
    lateinit var name: String

    @JsonProperty("e_mail")
    @NotBlank(message = ApplicationMessages.FIELD_IS_REQUIRED)
    @Email(message = ApplicationMessages.INVALID_EMAIL)
    @MaxLength(
        value = User.MAX_EMAIL_LENGTH,
        message = ApplicationMessages.INVALID_MAX_USER_EMAIL_LENGTH
    )
    lateinit var email: String

    @NotNull(message = ApplicationMessages.FIELD_IS_REQUIRED)
    @MinLength(
        value = User.MIN_PASSWORD_LENGTH,
        message = ApplicationMessages.INVALID_MIN_USER_PASSWORD_LENGTH
    )
    @MaxLength(
        value = User.MAX_PASSWORD_LENGTH,
        message = ApplicationMessages.INVALID_MAX_USER_PASSWORD_LENGTH
    )
    lateinit var password: String
}