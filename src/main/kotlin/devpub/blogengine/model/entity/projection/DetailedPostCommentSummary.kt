package devpub.blogengine.model.entity.projection

import java.time.LocalDateTime

data class DetailedPostCommentSummary(
    val id: Int,
    val text: String,
    val authorId: Int,
    val authorName: String,
    val authorAvatar: String?,
    val createdAt: LocalDateTime
)