package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonProperty
import devpub.blogengine.ApplicationMessages
import devpub.blogengine.model.entity.PersistentUtils
import devpub.blogengine.model.validation.EntityId
import javax.validation.constraints.NotNull

class ModeratePostRequest {
    @JsonProperty("post_id")
    @EntityId
    var id = PersistentUtils.INVALID_ID

    @NotNull(message = ApplicationMessages.FIELD_IS_REQUIRED)
    lateinit var decision: ModerationDecision
}