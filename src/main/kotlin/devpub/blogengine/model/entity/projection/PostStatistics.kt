package devpub.blogengine.model.entity.projection

import java.time.LocalDateTime

private fun toInt(value: Long?): Int {
    return value?.toInt() ?: 0
}

data class PostStatistics(
    val postsCount: Int,
    val likesCount: Int,
    val dislikesCount: Int,
    val viewsCount: Int,
    val firstPublication: LocalDateTime?
) {
    @Deprecated("Конструктор для JPA")
    constructor(
        postsCount: Long,
        likesCount: Long?,
        dislikesCount: Long?,
        viewsCount: Long?,
        firstPublication: LocalDateTime?
    ): this(postsCount.toInt(), toInt(likesCount), toInt(dislikesCount), toInt(viewsCount), firstPublication)
}