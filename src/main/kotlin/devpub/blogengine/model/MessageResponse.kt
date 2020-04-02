package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonProperty

data class MessageResponse(
    @JsonProperty("message") val value: String
)