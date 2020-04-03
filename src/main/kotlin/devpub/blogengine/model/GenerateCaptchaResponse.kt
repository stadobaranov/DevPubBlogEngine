package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonProperty

data class GenerateCaptchaResponse(
    @JsonProperty("secret") val secretCode: String,
    @JsonProperty("image") val encodedImage: String
)