package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonProperty
import devpub.blogengine.model.entity.PersistentUtils
import devpub.blogengine.model.validation.EntityId

class VotePostRequest {
    @JsonProperty("post_id")
    @EntityId
    var postId = PersistentUtils.INVALID_ID
}