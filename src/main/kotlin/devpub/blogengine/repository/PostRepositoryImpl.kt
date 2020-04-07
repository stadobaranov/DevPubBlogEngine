package devpub.blogengine.repository

import devpub.blogengine.model.PostOrder
import devpub.blogengine.model.PostStatus
import devpub.blogengine.model.entity.ModerationStatus
import devpub.blogengine.model.entity.Post
import devpub.blogengine.model.entity.PostComment
import devpub.blogengine.model.entity.PostDislike
import devpub.blogengine.model.entity.PostLike
import devpub.blogengine.model.entity.Tag
import devpub.blogengine.model.entity.TagName
import devpub.blogengine.model.entity.TagToPost
import devpub.blogengine.model.entity.User
import devpub.blogengine.model.entity.projection.ModeratedPostSummary
import devpub.blogengine.model.entity.projection.PostStatistics
import devpub.blogengine.model.entity.projection.PostSummary
import devpub.blogengine.model.pagination.UnorderedPageable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

private val postOrdersToJpql = mapOf(
    PostOrder.RECENT to "publishedAt desc",
    PostOrder.POPULAR to "commentCount desc",
    PostOrder.BEST to "likeCount desc",
    PostOrder.EARLY to "publishedAt asc"
)

private fun postOrderToJpql(order: PostOrder): String {
    return postOrdersToJpql[order] ?: error("Неизвестный порядок постов")
}

private typealias PostPredicatesBuilder = (
    CriteriaBuilder, CriteriaQuery<PostSummary>, Root<Post>
) -> MutableList<Predicate>

open class PostRepositoryImpl(
    @PersistenceContext private val entityManager: EntityManager
): PostRepositoryCustom {
    override fun findPageSummaries(
        pageable: UnorderedPageable,
        order: PostOrder,
        publishedBefore: LocalDateTime
    ): Page<PostSummary> {
        val jpql = """
            select new devpub.blogengine.model.entity.projection.PostSummary(
                p.id,
                p.title,
                p.text,
                a.id,
                a.name,
                p.publishedAt as publishedAt,
                (select count(l) from PostLike l where l.post = p) as likeCount,
                (select count(d) from PostDislike d where d.post = p),
                (select count(c) from PostComment c where c.post = p) as commentCount,
                p.viewCount
            )
            from Post p
            inner join p.author a
            where
                p.moderationStatus = 'ACCEPTED' and
                p.isActive = 1 and
                p.publishedAt <= :publishedBefore
            order by ${postOrderToJpql(order)}
        """

        val query = entityManager.createQuery(jpql, PostSummary::class.java)
                                 .setParameter("publishedBefore", publishedBefore)

        return RepositoryUtils.loadPage(query, pageable) {
            val countJpql = """
                select count(p)
                from Post p
                where
                    p.moderationStatus = 'ACCEPTED' and
                    p.isActive = 1 and
                    p.publishedAt <= :publishedBefore  
            """

            return@loadPage entityManager.createQuery(countJpql)
                                         .setParameter("publishedBefore", publishedBefore)
                                         .singleResult as Long
        }
    }

    override fun findPageSummariesBySearchQuery(
        searchQuery: String,
        pageable: Pageable,
        publishedBefore: LocalDateTime
    ): Page<PostSummary> {
        return findPageSummariesByCriteria(pageable, publishedBefore) {
            cb, _, post ->
                val predicates = ArrayList<Predicate>()

                if(searchQuery.isNotEmpty()) {
                    predicates.add(
                        cb.like(cb.lower(post.get<String>("title")), "%${searchQuery.toLowerCase()}%")
                    )
                }

                return@findPageSummariesByCriteria predicates
        }
    }

    override fun findPageSummariesByDate(
        date: LocalDate,
        pageable: Pageable,
        publishedBefore: LocalDateTime
    ): Page<PostSummary> {
        return findPageSummariesByCriteria(pageable, publishedBefore) {
            cb, _, post ->
                return@findPageSummariesByCriteria mutableListOf(
                    cb.equal(post.get<LocalDateTime>("publishedAt").`as`(LocalDate::class.java), date)
                )
        }
    }

    override fun findPageSummariesByTag(
        tagName: TagName,
        pageable: Pageable,
        publishedBefore: LocalDateTime
    ): Page<PostSummary> {
        return findPageSummariesByCriteria(pageable, publishedBefore) {
            cb, postQuery, post ->
                val tagToPostQuery = postQuery.subquery(TagToPost::class.java)
                val tagToPost = tagToPostQuery.from(TagToPost::class.java)
                val tag = tagToPost.join<TagToPost, Tag>("tag", JoinType.INNER)

                tagToPostQuery.select(tagToPost)
                              .where(
                                  cb.and(
                                      cb.equal(tag.get<TagName>("name").get<String>("value"), tagName.value),
                                      cb.equal(tagToPost.get<Post>("post"), post)
                                  )
                              )

                return@findPageSummariesByCriteria mutableListOf(cb.exists(tagToPostQuery))
        }
    }

    private fun findPageSummariesByCriteria(
        pageable: Pageable,
        publishedBefore: LocalDateTime,
        predicatesBuilder: PostPredicatesBuilder
    ): Page<PostSummary> {
        return PostPageByCriteriaLoader(entityManager, pageable, publishedBefore, predicatesBuilder).load()
    }

    override fun findPageSummariesByAuthor(
        authorId: Int,
        status: PostStatus,
        pageable: Pageable
    ): Page<PostSummary> {
        return PostPageByAuthorLoader(entityManager, authorId, status, pageable).load()
    }

    override fun findModeratedPageSummaries(
        moderatorId: Int,
        status: ModerationStatus,
        pageable: Pageable
    ): Page<ModeratedPostSummary> {
        val cb: CriteriaBuilder = entityManager.criteriaBuilder

        val postQuery = cb.createQuery(ModeratedPostSummary::class.java)
        val post = postQuery.from(Post::class.java)
        val author = post.join<Post, User>("author", JoinType.INNER)

        var predicate = cb.and(
            cb.equal(post.get<ModerationStatus>("moderationStatus"), status),
            cb.equal(post.get<Boolean>("isActive"), true)
        )

        if(status != ModerationStatus.NEW) {
            predicate = cb.and(cb.equal(post.get<User>("moderator").get<Int>("id"), moderatorId), predicate)
        }

        postQuery.select(
                     cb.construct(
                         ModeratedPostSummary::class.java,
                         post.get<Int>("id"),
                         post.get<String>("title"),
                         post.get<String>("text"),
                         author.get<Int>("id"),
                         author.get<String>("name"),
                         post.get<LocalDateTime>("publishedAt")
                     )
                 )
                 .where(predicate)

        return RepositoryUtils.loadPage(entityManager.createQuery(postQuery), pageable) {
            val countQuery = RepositoryUtils.toCountQuery(cb, Post::class, postQuery)
            return@loadPage entityManager.createQuery(countQuery).singleResult
        }
    }

    override fun findStatistics(userId: Int?): PostStatistics {
        val cb = entityManager.criteriaBuilder

        val postStatisticsQuery = cb.createQuery(PostStatistics::class.java)
        val post = postStatisticsQuery.from(Post::class.java)

        val likesQuery = postStatisticsQuery.subquery(Long::class.java)
        val likes = likesQuery.from(PostLike::class.java)
        likesQuery.select(cb.count(likes))

        val dislikesQuery = postStatisticsQuery.subquery(Long::class.java)
        val dislikes = dislikesQuery.from(PostDislike::class.java)
        dislikesQuery.select(cb.count(dislikes))

        postStatisticsQuery.select(
            cb.construct(
                PostStatistics::class.java,
                cb.count(post),
                likesQuery.selection,
                dislikesQuery.selection,
                cb.sum(post.get<Int>("viewCount")),
                cb.least(post.get<LocalDateTime>("publishedAt"))
            )
        )

        if(userId != null) {
            postStatisticsQuery.where(cb.equal(post.get<User>("author").get<Int>("id"), userId))
            likesQuery.where(cb.equal(likes.get<User>("voter").get<Int>("id"), userId))
            dislikesQuery.where(cb.equal(dislikes.get<User>("voter").get<Int>("id"), userId))
        }

        return entityManager.createQuery(postStatisticsQuery).singleResult
    }
}

private abstract class PostPageLoader(
    private val entityManager: EntityManager,
    private val pageable: Pageable
) {
    protected abstract fun buildPredicates(
        cb: CriteriaBuilder,
        postQuery: CriteriaQuery<PostSummary>,
        post: Root<Post>
    ): Array<Predicate>

    fun load(): Page<PostSummary> {
        val cb: CriteriaBuilder = entityManager.criteriaBuilder

        val postQuery = cb.createQuery(PostSummary::class.java)
        val post = postQuery.from(Post::class.java)

        val likesQuery = postQuery.subquery(Long::class.java)
        val likes = likesQuery.from(PostLike::class.java)
        likesQuery.select(cb.count(likes)).where(cb.equal(likes.get<Post>("post"), post))

        val dislikesQuery = postQuery.subquery(Long::class.java)
        val dislikes = dislikesQuery.from(PostDislike::class.java)
        dislikesQuery.select(cb.count(dislikes)).where(cb.equal(dislikes.get<Post>("post"), post))

        val commentsQuery = postQuery.subquery(Long::class.java)
        val comments = commentsQuery.from(PostComment::class.java)
        commentsQuery.select(cb.count(comments)).where(cb.equal(comments.get<Post>("post"), post))

        val author = post.join<Post, User>("author", JoinType.INNER)

        postQuery.select(
                     cb.construct(
                         PostSummary::class.java,
                         post.get<Int>("id"),
                         post.get<String>("title"),
                         post.get<String>("text"),
                         author.get<Int>("id"),
                         author.get<Int>("name"),
                         post.get<LocalDateTime>("publishedAt"),
                         likesQuery.selection,
                         dislikesQuery.selection,
                         commentsQuery.selection,
                         post.get<Int>("viewCount")
                     )
                 )
                 .where(cb.and(*buildPredicates(cb, postQuery, post)))

        if(pageable.sort.isSorted) {
            postQuery.orderBy(pageable.sort.map { RepositoryUtils.sortOrderToJpa(cb, post, it) }.toList())
        }

        return RepositoryUtils.loadPage(entityManager.createQuery(postQuery), pageable) {
            val countQuery = RepositoryUtils.toCountQuery(cb, Post::class, postQuery)
            return@loadPage entityManager.createQuery(countQuery).singleResult
        }
    }
}

private class PostPageByCriteriaLoader(
    entityManager: EntityManager,
    pageable: Pageable,
    publishedBefore: LocalDateTime,
    predicatesBuilder: PostPredicatesBuilder
): PostPageLoader(entityManager, pageable) {
    private val publishedBefore = publishedBefore
    private val predicatesBuilder = predicatesBuilder

    override fun buildPredicates(
        cb: CriteriaBuilder,
        postQuery: CriteriaQuery<PostSummary>,
        post: Root<Post>
    ): Array<Predicate> {
        val predicates = predicatesBuilder(cb, postQuery, post)

        predicates.add(cb.equal(post.get<ModerationStatus>("moderationStatus"), ModerationStatus.ACCEPTED))
        predicates.add(cb.equal(post.get<Boolean>("isActive"), true))
        predicates.add(cb.lessThanOrEqualTo(post.get<LocalDateTime>("publishedAt"), publishedBefore))

        return predicates.toTypedArray()
    }
}

private class PostPageByAuthorLoader(
    entityManager: EntityManager,
    authorId: Int,
    status: PostStatus,
    pageable: Pageable
): PostPageLoader(entityManager, pageable) {
    private val authorId = authorId
    private val status = status

    override fun buildPredicates(
        cb: CriteriaBuilder,
        postQuery: CriteriaQuery<PostSummary>,
        post: Root<Post>
    ): Array<Predicate> {
        var predicate = cb.equal(post.get<User>("author").get<Int>("id"), authorId)

        if(status == PostStatus.INACTIVE) {
            predicate = cb.and(predicate, cb.equal(post.get<Boolean>("isActive"), false))
        }
        else {
            val moderationStatus = when(status) {
                PostStatus.PENDING -> ModerationStatus.NEW
                PostStatus.DECLINED -> ModerationStatus.DECLINED
                PostStatus.PUBLISHED -> ModerationStatus.ACCEPTED
                else -> error("Неизвестный статус поста")
            }

            predicate = cb.and(
                predicate,
                cb.equal(post.get<ModerationStatus>("moderationStatus"), moderationStatus),
                cb.equal(post.get<Boolean>("isActive"), true)
            )
        }

        return arrayOf(predicate)
    }
}