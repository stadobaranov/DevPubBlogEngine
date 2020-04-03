package devpub.blogengine.repository

import devpub.blogengine.model.entity.Captcha
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface CaptchaRepository: BaseRepository<Captcha> {
    @Query("""
        select c 
        from Captcha c
        where c.secretCode = :secretCode
    """)
    fun findBySecretCode(@Param("secretCode") secretCode: String): Captcha?

    @Modifying
    @Query("""
        delete
        from Captcha c
        where c.createdAt <= :dateTime
    """)
    fun deleteBefore(@Param("dateTime") dateTime: LocalDateTime): Int
}