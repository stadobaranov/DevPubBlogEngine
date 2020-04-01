package devpub.blogengine.model.entity

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "tag2post")
open class TagToPost(id: Int = 0): Persistent(id) {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    open lateinit var post: Post

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    open lateinit var tag: Tag
}