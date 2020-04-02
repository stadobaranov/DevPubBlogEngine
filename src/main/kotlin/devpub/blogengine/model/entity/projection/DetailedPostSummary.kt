package devpub.blogengine.model.entity.projection

import devpub.blogengine.model.entity.ModerationStatus
import java.time.LocalDateTime

data class DetailedPostSummary(
    val id: Int,
    val title: String,
    val text: String,
    val authorId: Int,
    val authorName: String,
    val publishedAt: LocalDateTime,
    val isActive: Boolean,
    val moderationStatus: ModerationStatus,
    val likeCount: Int,
    val dislikeCount: Int,
    val viewCount: Int
) {
    @Deprecated("Конструктор для JPA")
    constructor(
        id: Int,
        title: String,
        text: String,
        authorId: Int,
        authorName: String,
        publishedAt: LocalDateTime,
        isActive: Boolean,
        moderationStatus: ModerationStatus,
        likeCount: Long,
        dislikeCount: Long,
        viewCount: Int
    ): this(id, title, text, authorId, authorName, publishedAt, isActive, moderationStatus, likeCount.toInt(), dislikeCount.toInt(), viewCount)
}