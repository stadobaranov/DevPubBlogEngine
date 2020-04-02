package devpub.blogengine.repository

import devpub.blogengine.model.entity.PostComment
import devpub.blogengine.model.entity.projection.DetailedPostCommentSummary
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import javax.persistence.LockModeType

@Repository
interface PostCommentRepository: BaseRepository<PostComment> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("""
        select c.id
        from PostComment c
        where 
            c.id = :id and
            c.post.id = :postId
    """)
    fun lockForShare(
        @Param("id") id: Int,
        @Param("postId") postId: Int
    ): Int?

    @Query("""
        select new devpub.blogengine.model.entity.projection.DetailedPostCommentSummary(
            c.id,
            c.text,
            a.id,
            a.name,
            a.avatar,
            c.createdAt
        )
        from PostComment c
        inner join c.author a
        where c.post.id = :postId
        order by c.createdAt asc
    """)
    fun findAllDetailedSummariesByPost(
        @Param("postId") postId: Int
    ): MutableList<DetailedPostCommentSummary>
}