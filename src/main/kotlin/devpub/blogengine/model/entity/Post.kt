package devpub.blogengine.model.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "posts")
open class Post(id: Int = 0): Persistent(id) {
    open lateinit var title: String

    @Column(columnDefinition = "text")
    open lateinit var text: String

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    open lateinit var author: User

    @Column(name = "time")
    open lateinit var publishedAt: LocalDateTime

    @Column(name = "is_active", columnDefinition = "tinyint")
    open var isActive = false

    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status", columnDefinition = "enum('NEW', 'ACCEPTED', 'DECLINED')")
    open lateinit var moderationStatus: ModerationStatus

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id")
    open var moderator: User? = null

    @Column(name = "view_count")
    open var viewCount = 0
}