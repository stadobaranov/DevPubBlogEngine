package devpub.blogengine.model.entity

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue(PostVote.LIKE_VALUE.toString())
open class PostLike: PostVote()