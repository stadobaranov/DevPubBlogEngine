package devpub.blogengine.repository

import devpub.blogengine.model.PostOrder
import devpub.blogengine.model.PostStatus
import devpub.blogengine.model.entity.ModerationStatus
import devpub.blogengine.model.entity.TagName
import devpub.blogengine.model.entity.projection.ModeratedPostSummary
import devpub.blogengine.model.entity.projection.PostSummary
import devpub.blogengine.model.pagination.UnorderedPageable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate
import java.time.LocalDateTime

interface PostRepositoryCustom {
    fun findPageSummaries(
        pageable: UnorderedPageable,
        order: PostOrder,
        publishedBefore: LocalDateTime = LocalDateTime.now()
    ): Page<PostSummary>

    fun findPageSummariesBySearchQuery(
        searchQuery: String,
        pageable: Pageable,
        publishedBefore: LocalDateTime = LocalDateTime.now()
    ): Page<PostSummary>

    fun findPageSummariesByDate(
        date: LocalDate,
        pageable: Pageable,
        publishedBefore: LocalDateTime = LocalDateTime.now()
    ): Page<PostSummary>

    fun findPageSummariesByTag(
        tagName: TagName,
        pageable: Pageable,
        publishedBefore: LocalDateTime = LocalDateTime.now()
    ): Page<PostSummary>

    fun findPageSummariesByAuthor(
        authorId: Int,
        status: PostStatus,
        pageable: Pageable
    ): Page<PostSummary>

    fun findModeratedPageSummaries(
        moderatorId: Int,
        status: ModerationStatus,
        pageable: Pageable
    ): Page<ModeratedPostSummary>
}