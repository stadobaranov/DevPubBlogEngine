package devpub.blogengine.repository

import devpub.blogengine.model.entity.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: BaseRepository<User> {
    @Query("""
        select u.isModerator 
        from User u 
        where u.id = :id
    """)
    fun checkIsModerator(@Param("id") id: Int): Boolean?
}