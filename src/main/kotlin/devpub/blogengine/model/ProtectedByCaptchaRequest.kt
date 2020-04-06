package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonProperty
import devpub.blogengine.ApplicationMessages
import javax.validation.constraints.NotNull

open class ProtectedByCaptchaRequest {
    @JsonProperty("captcha")
    @NotNull(message = ApplicationMessages.FIELD_IS_REQUIRED)
    lateinit var captchaCode: String

    @JsonProperty("captcha_secret")
    @NotNull(message = ApplicationMessages.FIELD_IS_REQUIRED)
    lateinit var captchaSecretCode: String
}