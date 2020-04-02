package devpub.blogengine.model.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "users")
open class User(id: Int = 0): Persistent(id) {
    open lateinit var name: String

    open lateinit var email: String

    @Column(name = "password")
    open lateinit var passwordHash: String

    @Column(name = "photo", columnDefinition = "text")
    open var avatar: String? = null

    @Column(name = "reg_time")
    open lateinit var registeredAt: LocalDateTime

    @Column(name = "is_moderator", columnDefinition = "tinyint")
    open var isModerator = false

    @Column(name = "code")
    open var resetCode: String? = null
}