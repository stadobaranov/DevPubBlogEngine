package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonProperty

data class PostPageResponse(
    @JsonProperty("count") val totalCount: Int,
    @JsonProperty("posts") val entries: List<Entry>
) {
    data class Entry(
        val id: Int,
        val title: String,
        val announce: String,
        @JsonProperty("user") val author: PostAuthor,
        @JsonProperty("time") val publishedAt: String,
        val likeCount: Int,
        val dislikeCount: Int,
        val commentCount: Int,
        val viewCount: Int
    )
}