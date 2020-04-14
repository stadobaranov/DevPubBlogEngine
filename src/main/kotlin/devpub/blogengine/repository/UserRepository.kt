package devpub.blogengine.repository

import devpub.blogengine.model.entity.User
import devpub.blogengine.model.entity.projection.AuthorizedUserSummary
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: BaseRepository<User>, UserRepositoryCustom {
    @Query("""
        select u.isModerator 
        from User u 
        where u.id = :id
    """)
    fun checkIsModerator(@Param("id") id: Int): Boolean?

    @Query("""
        select new devpub.blogengine.model.entity.projection.AuthorizedUserSummary(
            u.id,
            u.name,
            u.avatar,
            u.email,
            u.isModerator,
            (
                select count(p)
                from Post p
                where
                    p.moderationStatus = 'NEW' and
                    p.isActive = 1
            )
        )
        from User u
        where
            u.id = :id
    """)
    fun findAuthorizedSummary(@Param("id") id: Int): AuthorizedUserSummary?

    @Query("""
        select new devpub.blogengine.model.entity.projection.AuthorizedUserSummary(
            u.id,
            u.name,
            u.avatar,
            u.email,
            u.isModerator,
            (
                select count(p)
                from Post p
                where
                    p.moderationStatus = 'NEW' and
                    p.isActive = 1
            )
        )
        from User u
        where
            u.email = :email and
            u.passwordHash = :passwordHash
    """)
    fun findAuthorizedSummary(
        @Param("email") email: String,
        @Param("passwordHash") passwordHash: String
    ): AuthorizedUserSummary?

    @Query("""
        select u
        from User u
        where u.email = :email
    """)
    fun findByEmail(@Param("email") email: String): User?

    @Modifying
    @Query("""
        update User u
        set 
            u.resetCode = null, 
            u.passwordHash = :newPasswordHash
        where
            u.resetCode = :resetCode
    """)
    fun changePasswordByResetCode(
        @Param("resetCode") resetCode: String,
        @Param("newPasswordHash") newPasswordHash: String
    ): Int
}