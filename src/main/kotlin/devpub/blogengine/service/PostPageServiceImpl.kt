package devpub.blogengine.service

import devpub.blogengine.model.ModeratedPostPageRequest
import devpub.blogengine.model.ModeratedPostPageResponse
import devpub.blogengine.model.PageRequest
import devpub.blogengine.model.PostAuthor
import devpub.blogengine.model.PostPageByDateRequest
import devpub.blogengine.model.PostPageBySearchQueryRequest
import devpub.blogengine.model.PostPageByTagRequest
import devpub.blogengine.model.PostPageForCurrentUserRequest
import devpub.blogengine.model.PostPageRequest
import devpub.blogengine.model.PostPageResponse
import devpub.blogengine.model.entity.projection.ModeratedPostSummary
import devpub.blogengine.model.entity.projection.PostSummary
import devpub.blogengine.model.pagination.Pagination
import devpub.blogengine.model.pagination.UnorderedPagination
import devpub.blogengine.repository.PostRepository
import devpub.blogengine.service.aspect.Authorized
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val defaultSort = Sort.by(Sort.Direction.DESC, "publishedAt")

@Service
open class PostPageServiceImpl @Autowired constructor(
    private val announceExtractor: AnnounceExtractor,
    private val prettyTimeFormatter: PrettyTimeFormatter,
    private val authorizationService: AuthorizationService,
    private val postRepository: PostRepository
): PostPageService {
    private fun getCurrentUserId(): Int {
        return authorizationService.getCurrentUserIdOrThrowUnauthorizedException()
    }

    private fun paginationOf(request: PageRequest): Pagination {
        return Pagination(request.offset.toLong(), request.limit, defaultSort)
    }

    @Transactional(readOnly = true)
    override fun get(request: PostPageRequest): PostPageResponse {
        return createResponse(
            postRepository.findPageSummaries(
                UnorderedPagination(request.offset.toLong(), request.limit), request.order
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getBySearchQuery(request: PostPageBySearchQueryRequest): PostPageResponse {
        return createResponse(postRepository.findPageSummariesBySearchQuery(request.query, paginationOf(request)))
    }

    @Transactional(readOnly = true)
    override fun getByDate(request: PostPageByDateRequest): PostPageResponse {
        return createResponse(postRepository.findPageSummariesByDate(request.date, paginationOf(request)))
    }

    @Transactional(readOnly = true)
    override fun getByTag(request: PostPageByTagRequest): PostPageResponse {
        return createResponse(postRepository.findPageSummariesByTag(request.tag, paginationOf(request)))
    }

    @Authorized
    @Transactional(readOnly = true)
    override fun getForCurrentUser(request: PostPageForCurrentUserRequest): PostPageResponse {
        return createResponse(
            postRepository.findPageSummariesByAuthor(getCurrentUserId(), request.status, paginationOf(request))
        )
    }

    private fun createResponse(page: Page<PostSummary>): PostPageResponse {
        return PostPageResponse(
            page.totalElements.toInt(), page.content.map { postSummaryToResponseEntry(it) }
        )
    }

    private fun postSummaryToResponseEntry(summary: PostSummary): PostPageResponse.Entry {
        return PostPageResponse.Entry(
            summary.id,
            summary.title,
            announceExtractor.extract(summary.text),
            PostAuthor(summary.authorId, summary.authorName),
            prettyTimeFormatter.format(summary.publishedAt),
            summary.likeCount,
            summary.dislikeCount,
            summary.commentCount,
            summary.viewCount
        )
    }

    @Authorized(moderator = true)
    @Transactional(readOnly = true)
    override fun getForModeration(request: ModeratedPostPageRequest): ModeratedPostPageResponse {
        return createResponse(
            postRepository.findModeratedPageSummaries(getCurrentUserId(), request.status, paginationOf(request))
        )
    }

    private fun createResponse(page: Page<ModeratedPostSummary>): ModeratedPostPageResponse {
        return ModeratedPostPageResponse(
            page.totalElements.toInt(), page.content.map { moderatedPostSummaryToResponseEntry(it) }
        )
    }

    private fun moderatedPostSummaryToResponseEntry(summary: ModeratedPostSummary): ModeratedPostPageResponse.Entry {
        return ModeratedPostPageResponse.Entry(
            summary.id,
            summary.title,
            announceExtractor.extract(summary.text),
            PostAuthor(summary.authorId, summary.authorName),
            prettyTimeFormatter.format(summary.publishedAt)
        )
    }
}