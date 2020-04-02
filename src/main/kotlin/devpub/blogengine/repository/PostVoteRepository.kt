package devpub.blogengine.repository

import devpub.blogengine.model.entity.PostVote
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PostVoteRepository: BaseRepository<PostVote> {
    @Query("""
        select pv
        from PostVote pv
        where
            pv.voter.id = :voterId and
            pv.post.id = :postId
    """)
    fun findByVoterAndPost(
        @Param("voterId") voterId: Int,
        @Param("postId") postId: Int
    ): PostVote?
}