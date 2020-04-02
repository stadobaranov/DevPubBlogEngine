package devpub.blogengine.model.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "post_comments")
open class PostComment(id: Int = 0): Persistent(id) {
    companion object {
        const val MAX_TEXT_LENGTH = 65535
    }

    @Column(columnDefinition = "text")
    open lateinit var text: String

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    open lateinit var post: Post

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    open var parent: PostComment? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    open lateinit var author: User

    @Column(name = "time")
    open lateinit var createdAt: LocalDateTime
}