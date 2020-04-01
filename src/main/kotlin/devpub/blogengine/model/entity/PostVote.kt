package devpub.blogengine.model.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.DiscriminatorColumn
import javax.persistence.DiscriminatorType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Inheritance
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Inheritance
@Table(name = "post_votes")
@DiscriminatorColumn(name = "value", discriminatorType = DiscriminatorType.INTEGER, columnDefinition = "tinyint")
open class PostVote(id: Int = 0): Persistent(id) {
    companion object {
        const val LIKE_VALUE = 1
        const val DISLIKE_VALUE = -1
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    open lateinit var voter: User

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    open lateinit var post: Post

    @Column(columnDefinition = "tinyint", insertable = false, updatable = false)
    open var value = 0

    @Column(name = "time")
    open lateinit var createdAt: LocalDateTime

    val isLike get() = value == LIKE_VALUE

    val isDislike get() = value == DISLIKE_VALUE
}