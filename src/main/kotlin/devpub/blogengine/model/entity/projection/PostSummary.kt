package devpub.blogengine.model.entity.projection

import java.time.LocalDateTime

data class PostSummary(
    val id: Int,
    val title: String,
    val text: String,
    val authorId: Int,
    val authorName: String,
    val publishedAt: LocalDateTime,
    val likeCount: Int,
    val dislikeCount: Int,
    val commentCount: Int,
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
        likeCount: Long,
        dislikeCount: Long,
        commentCount: Long,
        viewCount: Int
    ): this(id, title, text, authorId, authorName, publishedAt, likeCount.toInt(), dislikeCount.toInt(), commentCount.toInt(), viewCount)
}