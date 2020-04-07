package devpub.blogengine.service

import devpub.blogengine.ApplicationMessages
import devpub.blogengine.model.AuthorizedUser
import devpub.blogengine.model.CommentPostRequest
import devpub.blogengine.model.CommentPostResponse
import devpub.blogengine.model.DetailedPostResponse
import devpub.blogengine.model.ModeratePostRequest
import devpub.blogengine.model.ModerationDecision
import devpub.blogengine.model.PostAuthor
import devpub.blogengine.model.PostCountToDatesRequest
import devpub.blogengine.model.PostCountToDatesResponse
import devpub.blogengine.model.PostStatisticsResponse
import devpub.blogengine.model.ResultResponse
import devpub.blogengine.model.SavePostRequest
import devpub.blogengine.model.VotePostRequest
import devpub.blogengine.model.entity.ModerationStatus
import devpub.blogengine.model.entity.PersistentUtils
import devpub.blogengine.model.entity.Post
import devpub.blogengine.model.entity.PostComment
import devpub.blogengine.model.entity.PostDislike
import devpub.blogengine.model.entity.PostLike
import devpub.blogengine.model.entity.PostVote
import devpub.blogengine.model.entity.Tag
import devpub.blogengine.model.entity.TagName
import devpub.blogengine.model.entity.TagToPost
import devpub.blogengine.model.entity.User
import devpub.blogengine.model.entity.projection.DetailedPostSummary
import devpub.blogengine.model.entity.projection.PostStatistics
import devpub.blogengine.repository.PostCommentRepository
import devpub.blogengine.repository.PostRepository
import devpub.blogengine.repository.PostVoteRepository
import devpub.blogengine.repository.TagRepository
import devpub.blogengine.repository.TagToPostRepository
import devpub.blogengine.repository.UserRepository
import devpub.blogengine.service.aspect.Authorized
import devpub.blogengine.service.exception.MissingPostCommentException
import devpub.blogengine.service.exception.MissingPostException
import devpub.blogengine.service.exception.MissingUserException
import devpub.blogengine.service.exception.PostNotFoundException
import devpub.blogengine.util.DateTimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.Month

@Service
open class PostServiceImpl @Autowired constructor(
    private val authorizationService: AuthorizationService,
    private val postRepository: PostRepository,
    private val postCommentRepository: PostCommentRepository,
    private val tagRepository: TagRepository,
    private val tagToPostRepository: TagToPostRepository,
    private val userRepository: UserRepository,
    private val voteRepository: PostVoteRepository
): PostService {
    private fun getCurrentUserId(): Int {
        return authorizationService.getCurrentUserIdOrThrowUnauthorizedException()
    }

    private fun getCurrentUser(): AuthorizedUser {
        return authorizationService.getCurrentUserOrThrowUnauthorizedException()
    }

    @Transactional(readOnly = true)
    override fun getDetails(id: Int): DetailedPostResponse {
        val details = postRepository.findDetailedSummary(id)

        if(details != null) {
            val now = LocalDateTime.now()

            if(details.isActive && details.moderationStatus == ModerationStatus.ACCEPTED && details.publishedAt <= now) {
                return getDetailedResponse(details)
            }
            else {
                val currentUser = authorizationService.getCurrentUser()

                if(currentUser != null) {
                    if(details.isActive && currentUser.isModerator || currentUser.id == details.authorId) {
                        return getDetailedResponse(details)
                    }
                }
            }
        }

        throw PostNotFoundException(ApplicationMessages.POST_NOT_FOUND)
    }

    private fun getDetailedResponse(details: DetailedPostSummary): DetailedPostResponse {
        val comments = postCommentRepository.findAllDetailedSummariesByPost(details.id).map {
            return@map DetailedPostResponse.Comment(
                it.id,
                it.text,
                DetailedPostResponse.Comment.Author(it.authorId, it.authorName, it.authorAvatar),
                DateTimeUtils.format(it.createdAt)
            )
        }

        val tagNames = tagRepository.findAllNamesByPost(details.id)

        return DetailedPostResponse(
            details.id,
            details.title,
            details.text,
            PostAuthor(details.authorId, details.authorName),
            DateTimeUtils.format(details.publishedAt),
            details.likeCount,
            details.dislikeCount,
            details.viewCount,
            comments,
            tagNames
        )
    }

    @Transactional(readOnly = true)
    override fun getCountToDates(request: PostCountToDatesRequest): PostCountToDatesResponse {
        return PostCountToDatesResponse.of(postRepository.findAllCountToDates(getEndOfYearOrNow(request.year)))
    }

    private fun getEndOfYearOrNow(year: Int): LocalDateTime {
        val now = LocalDateTime.now()

        if(year < 0 || year > now.year) {
            return now
        }

        val endOfYear = LocalDateTime.of(
            year, Month.DECEMBER, 31, 23, 59, 59
        )

        return if(endOfYear > now) now else endOfYear
    }

    @Transactional(readOnly = true)
    override fun getStatistics(userId: Int?): PostStatisticsResponse {
        return createStatisticsResponse(postRepository.findStatistics(userId))
    }

    private fun createStatisticsResponse(statistics: PostStatistics): PostStatisticsResponse {
        val firstPublication = statistics.firstPublication

        return PostStatisticsResponse(
            statistics.postsCount,
            statistics.likesCount,
            statistics.dislikesCount,
            statistics.viewsCount,
            if(firstPublication != null) DateTimeUtils.format(firstPublication) else "-"
        )
    }

    @Authorized
    @Transactional
    override fun create(request: SavePostRequest): ResultResponse {
        val authorId = getCurrentUserId()

        if(userRepository.lockForShare(authorId) == null) {
            throw MissingUserException(ApplicationMessages.MISSING_POST_AUTHOR)
        }

        val post = Post()
        post.author = User(authorId)
        post.moderationStatus = ModerationStatus.NEW
        fillPostFromRequest(post, request)
        postRepository.save(post)
        createTags(post, request.tags)

        return ResultResponse.of(true)
    }

    private fun createTags(post: Post, tagNames: Set<TagName>) {
        if(tagNames.isNotEmpty()) {
            getAndCreateNotExistingTags(tagNames).forEach {
                val t2p = TagToPost()
                t2p.post = post
                t2p.tag = it
                tagToPostRepository.save(t2p)
            }
        }
    }

    @Authorized(dirty = false)
    @Transactional
    override fun update(id: Int, request: SavePostRequest): ResultResponse {
        val post = postRepository.findAndLockForUpdate(id)

        if(post == null) {
            throw PostNotFoundException(ApplicationMessages.POST_NOT_FOUND)
        }

        val currentUser = getCurrentUser()

        if(currentUser.id == post.author.id) {
            post.moderationStatus = ModerationStatus.NEW
        }
        else if(!currentUser.isModerator || !post.isActive) {
            throw PostNotFoundException(ApplicationMessages.POST_NOT_FOUND)
        }

        fillPostFromRequest(post, request)
        postRepository.save(post)
        retainTags(post, request.tags)

        return ResultResponse.of(true)
    }

    private fun retainTags(post: Post, tagNames: Set<TagName>) {
        val tagToPosts = tagToPostRepository.findAllWithFetchedTagsByPost(post.id)

        val deletableTagToPostIds = tagToPosts.filter { it.tag.name !in tagNames }
                                              .mapTo(hashSetOf()) { it.id }

        if(deletableTagToPostIds.isNotEmpty()) {
            tagToPostRepository.deleteAll(deletableTagToPostIds)
        }

        if(tagNames.isNotEmpty()) {
            val boundTagNames = tagToPosts.filter { it.id !in deletableTagToPostIds }
                                          .mapTo(hashSetOf()) { it.tag.name }

            var flushRepository = false

            getAndCreateNotExistingTags(tagNames).forEach {
                if(it.name !in boundTagNames) {
                    val t2p = TagToPost()
                    t2p.post = post
                    t2p.tag = it

                    repeatIfThrown(DataIntegrityViolationException::class) {
                        tagToPostRepository.save(t2p)
                        flushRepository = true
                    }
                }
            }

            if(flushRepository) {
                repeatIfThrown(DataIntegrityViolationException::class) {
                    tagToPostRepository.flush()
                }
            }
        }
    }

    private fun fillPostFromRequest(post: Post, request: SavePostRequest) {
        val now = LocalDateTime.now()
        post.title = request.title
        post.text = request.text
        post.publishedAt = if(now.isAfter(request.time)) now else request.time
        post.isActive = request.active
    }

    private fun getAndCreateNotExistingTags(tagNames: Set<TagName>): MutableList<Tag> {
        val tags = tagRepository.findAllByNames(tagNames.mapTo(hashSetOf()) { it.value })

        if(tags.size != tagNames.size) {
            val existingTagNames = tags.mapTo(hashSetOf()) { it.name }
            var flushRepository = false

            tagNames.forEach {
                if(it !in existingTagNames) {
                    val newTag = Tag()
                    newTag.name = it

                    repeatIfThrown(DataIntegrityViolationException::class) {
                        tagRepository.save(newTag)
                        flushRepository = true
                    }

                    tags.add(newTag)
                }
            }

            if(flushRepository) {
                repeatIfThrown(DataIntegrityViolationException::class) {
                    tagRepository.flush()
                }
            }
        }

        return tags
    }

    @Authorized
    @Transactional
    override fun comment(request: CommentPostRequest): CommentPostResponse {
        val authorId = getCurrentUserId()

        if(userRepository.lockForShare(authorId) == null) {
            throw MissingUserException(ApplicationMessages.MISSING_COMMENT_AUTHOR)
        }

        val id = request.id

        if(postRepository.lockForShare(id) == null) {
            throw MissingPostException(ApplicationMessages.MISSING_POST)
        }

        val comment = PostComment()
        comment.post = Post(id)
        comment.text = request.text
        comment.author = User(authorId)
        comment.createdAt = LocalDateTime.now()

        val parentCommentId = request.parentCommentId

        if(PersistentUtils.isValidId(parentCommentId)) {
            if(postCommentRepository.lockForShare(parentCommentId, id) == null) {
                throw MissingPostCommentException(ApplicationMessages.MISSING_PARENT_COMMENT)
            }

            comment.parent = PostComment(parentCommentId)
        }

        return CommentPostResponse(postCommentRepository.save(comment).id)
    }

    @Authorized(moderator = true)
    @Transactional
    override fun moderate(request: ModeratePostRequest) {
        val moderatorId = getCurrentUserId()

        if(userRepository.lockForShare(moderatorId) == null) {
            throw MissingUserException(ApplicationMessages.MISSING_POST_MODERATOR)
        }

        val status = when(request.decision) {
            ModerationDecision.ACCEPT -> ModerationStatus.ACCEPTED
            ModerationDecision.DECLINE -> ModerationStatus.DECLINED
        }

        if(postRepository.moderate(request.id, moderatorId, status) == 0) {
            throw MissingPostException(ApplicationMessages.MISSING_POST)
        }
    }

    @Authorized
    @Transactional
    override fun like(request: VotePostRequest): ResultResponse {
        return vote(request, PostVote::isLike) { PostLike() }
    }

    @Authorized
    @Transactional
    override fun dislike(request: VotePostRequest): ResultResponse {
        return vote(request, PostVote::isDislike) { PostDislike() }
    }

    private fun vote(
        request: VotePostRequest,
        isDuplicate: (PostVote) -> Boolean,
        builder: () -> PostVote
    ): ResultResponse {
        val postId = request.postId
        val currentUserId = authorizationService.getCurrentUserIdOrThrowUnauthorizedException()
        val existingVote = voteRepository.findByVoterAndPost(currentUserId, postId)

        if(existingVote != null) {
            if(isDuplicate(existingVote)) {
                return ResultResponse.of(false)
            }

            voteRepository.delete(existingVote)
            voteRepository.flush()
        }

        if(userRepository.lockForShare(currentUserId) == null) {
            throw MissingUserException(ApplicationMessages.MISSING_POST_VOTER)
        }
        else if(postRepository.lockForShare(postId) == null) {
            throw MissingPostException(ApplicationMessages.MISSING_POST)
        }

        val vote = builder()
        vote.voter = User(currentUserId)
        vote.post = Post(postId)
        vote.createdAt = LocalDateTime.now()

        repeatIfThrown(DataIntegrityViolationException::class) {
            voteRepository.saveAndFlush(vote)
        }

        return ResultResponse.of(true)
    }
}