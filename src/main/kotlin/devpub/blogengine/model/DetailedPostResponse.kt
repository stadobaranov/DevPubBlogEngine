package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonProperty

data class DetailedPostResponse(
    val id: Int,
    val title: String,
    val text: String,
    @JsonProperty("user") val author: PostAuthor,
    @JsonProperty("time") val publishedAt: String,
    val likeCount: Int,
    val dislikeCount: Int,
    val viewCount: Int,
    val comments: List<Comment>,
    @JsonProperty("tags") val tagNames: Set<String>
) {
    data class Comment(
        val id: Int,
        val text: String,
        @JsonProperty("user") val author: Author,
        @JsonProperty("time") val createdAt: String
    ) {
        data class Author(
            val id: Int,
            val name: String,
            @JsonProperty("photo") val avatar: String?
        )
    }
}