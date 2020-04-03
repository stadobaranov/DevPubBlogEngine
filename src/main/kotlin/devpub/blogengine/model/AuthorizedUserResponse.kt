package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import devpub.blogengine.model.entity.projection.AuthorizedUserSummary

class AuthorizedUserResponse(summary: AuthorizedUserSummary?) {
    @JsonProperty("user")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val summary: AuthorizedUserSummary? = summary

    val result get() = summary != null

    override fun hashCode(): Int {
        return summary.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is AuthorizedUserResponse && summary == other.summary
    }
}