package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonProperty
import devpub.blogengine.ApplicationMessages
import javax.validation.constraints.NotNull

class LoginUserRequest {
    @JsonProperty("e_mail")
    @NotNull(message = ApplicationMessages.FIELD_IS_REQUIRED)
    lateinit var email: String

    @NotNull(message = ApplicationMessages.FIELD_IS_REQUIRED)
    lateinit var password: String
}