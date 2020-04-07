package devpub.blogengine.model

data class PostStatisticsResponse(
    val postsCount: Int,
    val likesCount: Int,
    val dislikesCount: Int,
    val viewsCount: Int,
    val firstPublication: String
)