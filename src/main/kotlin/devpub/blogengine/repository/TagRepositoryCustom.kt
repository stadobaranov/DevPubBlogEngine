package devpub.blogengine.repository

import devpub.blogengine.model.entity.projection.PostCountToTagName
import java.time.LocalDateTime

interface TagRepositoryCustom {
    fun findMaxPostCountGroupedByName(publishedBefore: LocalDateTime = LocalDateTime.now()): Long

    fun findAllPostCountToNames(
        searchQuery: String,
        publishedBefore: LocalDateTime = LocalDateTime.now()
    ): MutableList<PostCountToTagName>
}