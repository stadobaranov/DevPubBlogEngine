package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ValidationErrorsResponse(
    @JsonProperty("errors") val messages: Map<String, String>
) {
    val result get() = false

    constructor(field: String, message: String): this(mapOf(field to message))
}