package devpub.blogengine.repository

import devpub.blogengine.model.entity.Tag
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TagRepository: BaseRepository<Tag> {
    @Query("""
        select t.name.value
        from Tag t 
        where exists(
            select t2p 
            from TagToPost t2p 
            where 
                t2p.tag = t and
                t2p.post.id = :postId
        )
    """)
    fun findAllNamesByPost(@Param("postId") postId: Int): MutableSet<String>

    @Query("""
        select t
        from Tag t
        where t.name.value in :names
    """)
    fun findAllByNames(@Param("names") names: Set<String>): MutableList<Tag>
}