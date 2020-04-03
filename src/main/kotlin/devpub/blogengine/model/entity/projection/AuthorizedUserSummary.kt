package devpub.blogengine.model.entity.projection

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthorizedUserSummary(
    val id: Int,
    val name: String,
    @JsonProperty("photo") val avatar: String?,
    val email: String,
    val moderation: Boolean,
    val moderationCount: Int,
    val settings: Boolean
) {
    @Deprecated("Конструктор для JPA")
    constructor(
        id: Int,
        name: String,
        avatar: String?,
        email: String,
        moderation: Boolean,
        moderationCount: Long
    ): this(id, name, avatar, email, moderation, moderationCount.toInt(), moderation)
}