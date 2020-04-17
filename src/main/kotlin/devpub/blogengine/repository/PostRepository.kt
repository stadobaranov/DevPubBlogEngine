package devpub.blogengine.repository

import devpub.blogengine.model.entity.ModerationStatus
import devpub.blogengine.model.entity.Post
import devpub.blogengine.model.entity.projection.DetailedPostSummary
import devpub.blogengine.model.entity.projection.PostCountToDate
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface PostRepository: BaseRepository<Post>, PostRepositoryCustom {
    @Query("""
        select new devpub.blogengine.model.entity.projection.DetailedPostSummary(
            p.id, 
            p.title,
            p.text,
            a.id,
            a.name,
            p.publishedAt,
            p.isActive,
            p.moderationStatus,
            (select count(pl) from PostLike pl where pl.post = p),
            (select count(pd) from PostDislike pd where pd.post = p),
            p.viewCount
        )
        from Post p
        inner join p.author a
        where
            p.id = :id
    """)
    fun findDetailedSummary(@Param("id") id: Int): DetailedPostSummary?

    @Query("""
        select new devpub.blogengine.model.entity.projection.PostCountToDate(
            cast(p.publishedAt as date),
            count(p)
        )
        from Post p
        where
            p.moderationStatus = 'ACCEPTED' and 
            p.isActive = 1 and
            p.publishedAt <= :publishedBefore
        group by cast(p.publishedAt as date)
    """)
    fun findAllCountToDates(
        @Param("publishedBefore") publishedBefore: LocalDateTime = LocalDateTime.now()
    ): MutableList<PostCountToDate>

    @Modifying
    @Query("""
        update Post p
        set p.viewCount = p.viewCount + 1
        where p.id = :id
    """)
    fun incrementViews(@Param("id") id: Int): Int

    @Modifying
    @Query("""
        update Post p 
        set 
            p.moderator.id = :moderatorId,
            p.moderationStatus = :status
        where 
            p.id = :id
    """)
    fun moderate(
        @Param("id") id: Int,
        @Param("moderatorId") moderatorId: Int,
        @Param("status") status: ModerationStatus
    ): Int
}