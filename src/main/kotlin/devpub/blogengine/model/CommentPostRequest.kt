package devpub.blogengine.model

import com.fasterxml.jackson.annotation.JsonProperty
import devpub.blogengine.ApplicationMessages
import devpub.blogengine.model.entity.PersistentUtils
import devpub.blogengine.model.entity.PostComment
import devpub.blogengine.model.validation.EntityId
import devpub.blogengine.model.validation.MaxLength
import devpub.blogengine.model.validation.SafeHtml
import javax.validation.constraints.NotBlank

class CommentPostRequest {
    @JsonProperty("post_id")
    @EntityId
    var id = PersistentUtils.INVALID_ID

    @JsonProperty("parent_id")
    var parentCommentId = PersistentUtils.INVALID_ID

    @NotBlank(message = ApplicationMessages.FIELD_IS_REQUIRED)
    @MaxLength(
        value = PostComment.MAX_TEXT_LENGTH,
        message = ApplicationMessages.INVALID_MAX_POST_COMMENT_TEXT_LENGTH
    )
    @SafeHtml
    lateinit var text: String
}