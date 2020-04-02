package devpub.blogengine.repository

import devpub.blogengine.model.entity.TagToPost
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TagToPostRepository: BaseRepository<TagToPost> {
    @Query("""
        select t2p 
        from TagToPost t2p 
        inner join fetch t2p.tag 
        where t2p.post.id = :postId
    """)
    fun findAllWithFetchedTagsByPost(@Param("postId") postId: Int): MutableList<TagToPost>
}