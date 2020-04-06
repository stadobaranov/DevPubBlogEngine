package devpub.blogengine.model

import devpub.blogengine.ApplicationMessages
import devpub.blogengine.model.entity.User
import devpub.blogengine.model.request.NullIfEmptyStringProperty
import devpub.blogengine.model.request.PropertyAlias
import devpub.blogengine.model.validation.MaxLength
import devpub.blogengine.model.validation.MinLength
import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.Email

abstract class UpdateUserProfileRequest {
    @get:MaxLength(
        value = User.MAX_NAME_LENGTH,
        message = ApplicationMessages.INVALID_MAX_USER_NAME_LENGTH
    )
    var name by NullIfEmptyStringProperty()

    @get:Email(message = ApplicationMessages.INVALID_EMAIL)
    @get:MaxLength(
        value = User.MAX_EMAIL_LENGTH,
        message = ApplicationMessages.INVALID_MAX_USER_EMAIL_LENGTH
    )
    var email by NullIfEmptyStringProperty()

    @get:MinLength(
        value = User.MIN_PASSWORD_LENGTH,
        message = ApplicationMessages.INVALID_MIN_USER_PASSWORD_LENGTH
    )
    @get:MaxLength(
        value = User.MAX_PASSWORD_LENGTH,
        message = ApplicationMessages.INVALID_MAX_USER_PASSWORD_LENGTH
    )
    var password by NullIfEmptyStringProperty()

    var removeAvatar: Boolean = false

    @Deprecated("Параметр для запроса")
    var removePhoto by PropertyAlias(this::removeAvatar)

    abstract val avatar: MultipartFile?
}