package devpub.blogengine.repository

import devpub.blogengine.model.entity.ModerationStatus
import devpub.blogengine.model.entity.Post
import devpub.blogengine.model.entity.Tag
import devpub.blogengine.model.entity.TagName
import devpub.blogengine.model.entity.TagToPost
import devpub.blogengine.model.entity.projection.PostCountToTagName
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.NoResultException
import javax.persistence.PersistenceContext
import javax.persistence.criteria.JoinType

open class TagRepositoryImpl(
    @PersistenceContext private val entityManager: EntityManager
): TagRepositoryCustom {
    override fun findMaxPostCountGroupedByName(publishedBefore: LocalDateTime): Long {
        val jpql = """
            select count(p) as c
            from TagToPost t2p
            inner join t2p.post p
            where
                p.moderationStatus = 'ACCEPTED' and
                p.isActive = 1 and
                p.publishedAt <= :publishedBefore
            group by t2p.tag.id
            order by c desc
        """

        try {
            return entityManager.createQuery(jpql)
                                .setParameter("publishedBefore", publishedBefore)
                                .setMaxResults(1)
                                .singleResult as Long
        }
        catch(exception: NoResultException) {
            return 0
        }
    }

    override fun findAllPostCountToNames(
        searchQuery: String,
        publishedBefore: LocalDateTime
    ): MutableList<PostCountToTagName> {
        val cb = entityManager.criteriaBuilder

        val tagToPostQuery = cb.createQuery(PostCountToTagName::class.java)
        val tagToPost = tagToPostQuery.from(TagToPost::class.java)
        val post = tagToPost.join<TagToPost, Post>("post", JoinType.INNER)
        val tag = tagToPost.join<TagToPost, Tag>("tag", JoinType.INNER)

        var predicate = cb.and(
            cb.equal(post.get<ModerationStatus>("moderationStatus"), ModerationStatus.ACCEPTED),
            cb.equal(post.get<Boolean>("isActive"), true),
            cb.lessThanOrEqualTo(post.get<LocalDateTime>("publishedAt"), publishedBefore)
        )

        if(searchQuery.isNotEmpty()) {
            predicate = cb.and(
                predicate,

                cb.like(
                    cb.lower(tag.get<TagName>("name").get<String>("value")), "%${searchQuery.toLowerCase()}%"
                )
            )
        }

        tagToPostQuery.select(
                          cb.construct(
                              PostCountToTagName::class.java,
                              tag.get<String>("name"),
                              cb.count(post)
                          )
                      )
                      .where(predicate)
                      .groupBy(tag)

        return entityManager.createQuery(tagToPostQuery).resultList
    }
}