package devpub.blogengine.model.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "captcha_codes")
open class Captcha(id: Int = 0): Persistent(id) {
    @Column(columnDefinition = "tinytext")
    open lateinit var code: String

    @Column(name = "secret_code", columnDefinition = "tinytext")
    open lateinit var secretCode: String

    @Column(name = "time")
    open lateinit var createdAt: LocalDateTime
}