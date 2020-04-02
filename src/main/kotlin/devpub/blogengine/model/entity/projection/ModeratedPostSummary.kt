package devpub.blogengine.model.entity.projection

import java.time.LocalDateTime

data class ModeratedPostSummary(
    val id: Int,
    val title: String,
    val text: String,
    val authorId: Int,
    val authorName: String,
    val publishedAt: LocalDateTime
)