package devpub.blogengine.model.entity

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue(PostVote.DISLIKE_VALUE.toString())
open class PostDislike: PostVote()